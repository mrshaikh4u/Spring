package com.mycomp.socialApp.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.domain.ErrorCode;
import com.mycomp.socialApp.domain.Status;
import com.mycomp.socialApp.domain.UserTransactions;
import com.mycomp.socialApp.exceptions.DataNotFoundException;
import com.mycomp.socialApp.exceptions.DataNotSavedException;
import com.mycomp.socialApp.repositories.ContactRepository;
import com.mycomp.socialApp.repositories.UserTransactionRepository;
import com.mycomp.socialApp.to.SendMessageTO;
import com.mycomp.socialApp.utils.AppConstants;
import com.mycomp.socialApp.utils.ContactMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * This is Generic class providing implementation of common functionalities of
 * social channel. leaving specialized functionalities on implementer classes It
 * works on the basis of Template design pattern eg send() method provides
 * template for sending message and leaving how to send message to specific
 * channel (sendAtChannel())
 *
 * This way if any new channel to be added ,it just need to provide
 * implementation of specific way of sending message and rest all operations are
 * already handled for him at parent class
 * 
 * @author Rahil
 *
 */
@Service
@Slf4j
@Data
public abstract class ChannelService {

	protected SendMessageTO sendMessageTO;

	@Autowired
	protected ContactRepository contactRepo;

	@Autowired
	protected UserTransactionRepository userTransactionRepo;

	@Autowired
	protected ContactMapper contactMapper;

	@Autowired
	@Qualifier("RESTInputValidator")
	protected ValidationService restValidationService;

	protected String channel;

	/**
	 * This is generic method to handle common message sending functionalities and
	 * also to handle common tasks like
	 * <ol>
	 * <li>user tracking who is sending message</li>
	 * <li>saving cost after sending message</li>
	 * <li>extra logging if needed</li>
	 * <li>exception handling etc</li>
	 * <li>hook method for future use</li>
	 * </ol>
	 * .
	 * 
	 * @return Status
	 * @throws DataNotSavedException
	 */
	public Status send() throws DataNotSavedException {
		beginSendHook();
		validateInput();
		log.info("Sending message to recipient" + this.getSendMessageTO()
		                                              .getContactID());
		Status status = sendAtChannel();
		Double sendingCost = null;
		if (status.getStatusCode() == ErrorCode.FAILURE) {
			if (isMobileNumberSaved()) {
				status = sendSMS();
				sendingCost = calculateSMSCost();
			} else {
				log.error("Message sending failed and SMS didn't tried");
				return status;
			}
		} else {
			sendingCost = calculateSendingCost();
		}
		status = saveCost(sendingCost);
		endSendHook();
		return status;
	}

	/**
	 * Reserved for future enhancment
	 */
	protected void endSendHook() {
		// TODO Auto-generated method stub

	}

	/**
	 * Reserved for future enhancements
	 */
	protected void beginSendHook() {

	}

	/**
	 * helper method to validate input
	 */
	protected void validateInput() {
		restValidationService.setValidatableObj(this.getSendMessageTO());
		boolean isValid = restValidationService.validate();

		if (isValid) {
			Optional<Contact> optional = contactRepo.findById(this.sendMessageTO.getContactID());
			if (!optional.isPresent()) {
				throw new DataNotFoundException("contact not found");
			}
			log.info("input validation passed");
		}
	}

	/**
	 * Calculate sms sending cost
	 * 
	 * @return
	 */
	protected Double calculateSMSCost() {
		ChannelService channelService = new SMSChannelService();
		log.info("Calculating SMS cost");
		return channelService.calculateSendingCost();
	}

	/**
	 * This method sends SMS
	 * 
	 * @return
	 */
	protected Status sendSMS() {
		ChannelService channelService = new SMSChannelService();
		channelService.setSendMessageTO(this.sendMessageTO);
		log.info("Sending SMS to" + this.sendMessageTO.getContactID());
		return channelService.sendAtChannel();
	}

	/**
	 * This is generic method to save cost , common for all channels
	 * 
	 * @param sendingCost
	 * @return
	 * @throws DataNotSavedException
	 */
	@Transactional
	protected Status saveCost(Double sendingCost) throws DataNotSavedException {
		log.info("User XYZ is trying to save cost for contact " + this.sendMessageTO.getContactID());
		UserTransactions userTransaction = new UserTransactions();

		Optional<Contact> optional = contactRepo.findById(this.sendMessageTO.getContactID());
		if (!optional.isPresent()) {
			throw new DataNotFoundException("contact not found");
		}
		Contact contact = optional.get();
		userTransaction.setContact(contact);
		userTransaction.setTransactionDate(LocalDateTime.now());
		userTransaction.setTransactionAmount(sendingCost);
		userTransaction.setChannel(this.getChannel());
		log.info(new StringBuilder("Transaction ").append(userTransaction)
		                                          .append(" Getting saved")
		                                          .toString());
		try {
			userTransactionRepo.save(userTransaction);
		} catch (Exception ex) {
			log.error("Unable to save transaction " + userTransaction);
			throw new DataNotSavedException("Unable to save transaction ", ex);
		}
		return new Status(ErrorCode.SUCCESS, AppConstants.SUCCESS_DESCRIPTION);
	}

	/**
	 * This method checks if contact has previously saved contact
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	protected boolean isMobileNumberSaved() {
		Optional<Contact> optionalContact;
		try {
			optionalContact = contactRepo.findById(this.sendMessageTO.getContactID());
		} catch (Exception ex) {
			throw new DataNotFoundException("Error occured while fetching contact ", ex);
		}
		if (!optionalContact.isPresent()) {
			String errorMsg = "Contact not found";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
		return optionalContact.get()
		                      .getContactID() != null;
	}

	protected abstract Status sendAtChannel();

	public abstract Double calculateSendingCost();
}
