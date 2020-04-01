package com.mycomp.socialApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.domain.UserTransactions;
import com.mycomp.socialApp.exceptions.DataNotFoundException;
import com.mycomp.socialApp.repositories.ContactRepository;
import com.mycomp.socialApp.repositories.UserTransactionRepository;
import com.mycomp.socialApp.to.ContactTO;
import com.mycomp.socialApp.to.TransactionReportTO;
import com.mycomp.socialApp.to.TransactionTO;
import com.mycomp.socialApp.utils.ContactMapper;
import com.mycomp.socialApp.utils.UserTransactionMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * This is simple implementation to generate JSON report from DB
 * 
 * @author Rahil
 *
 */
@Service
@Slf4j
@Qualifier("SimpleJSONReport")
public class SimpleJSONReport extends ReportService {

	@Autowired
	protected UserTransactionRepository userTransactionRepo;

	@Autowired
	protected ContactRepository contactRepo;

	@Autowired
	protected ContactMapper contactMapper;

	@Autowired
	protected UserTransactionMapper userTransactionMapper;

	/**
	 * Method to extract JSON report from DB input validation is already performed
	 * in parent class This is benefit of Template pattern
	 */
	@Override
	@Transactional
	public TransactionReportTO extractReportInternal() {

		ContactTO contact = this.transactionReportTO.getContact();
		Optional<Contact> optionalContact = null;
		try {
			optionalContact = contactRepo.findById(contact.getContactID());
		} catch (Exception ex) {
			log.error("Error occured while fetcing contact " + contact.getContactID());
			throw new DataNotFoundException(ex.getLocalizedMessage(), ex);
		}

		if (!optionalContact.isPresent()) {
			log.error("Contact not found with pass id : " + contact.getContactID());
			throw new DataNotFoundException("Contact not found in DB");
		}
		Contact contactEntity = optionalContact.get();
		log.info("contact retrieved : " + contactEntity);
		LocalDateTime fromDate = this.transactionReportTO.getFromDate();
		LocalDateTime toDate = this.transactionReportTO.getToDate();
		List<UserTransactions> dateWiseReport;
		try {
			log.info("Trying to fetch from : " + fromDate + " to date : " + toDate);
			dateWiseReport = userTransactionRepo.extractDateWiseReport(contactEntity, fromDate, toDate);
		} catch (Exception ex) {
			String errorMsg = "Unable to fetch transactions for contact : " + contactEntity.getContactID();
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		TransactionReportTO output = new TransactionReportTO();
		output.setContact(contactMapper.getContactMapper()
		                               .map(contactEntity, ContactTO.class));
		output.setFromDate(fromDate);
		output.setToDate(toDate);
		List<TransactionTO> transactionList = dateWiseReport.stream()
		                                                    .map(e -> userTransactionMapper.getTransactionMapper()
		                                                                                   .map(e, TransactionTO.class))
		                                                    .collect(Collectors.toList());

		output.setTransactions(transactionList);
		return output;
	}

}
