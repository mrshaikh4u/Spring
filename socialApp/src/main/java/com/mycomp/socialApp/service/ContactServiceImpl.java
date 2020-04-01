package com.mycomp.socialApp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.exceptions.DataNotFoundException;
import com.mycomp.socialApp.exceptions.DataNotSavedException;
import com.mycomp.socialApp.exceptions.InputParameterInvalidException;
import com.mycomp.socialApp.repositories.ContactRepository;
import com.mycomp.socialApp.to.ContactTO;
import com.mycomp.socialApp.utils.CommonUtils;
import com.mycomp.socialApp.utils.ContactMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * This is Contact service implementation class supporting CRUD operation on
 * contact
 * 
 * @author Rahil
 *
 */
@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

	@Autowired
	protected ContactMapper contactMapper;

	@Autowired
	@Qualifier("RESTInputValidator")
	protected ValidationService restValidationService;

	@Autowired
	protected ContactRepository contactRepo;

	/**
	 * This operation adds new contact supports writable transaction and performs
	 * input validation before saving
	 * 
	 * performs name check as same name contact is not allowed as of now
	 * 
	 * @param inputContact - Service input
	 * @return ContactTO - transfer object
	 */
	@Override
	@Transactional
	public ContactTO addContact(ContactTO inputContact) throws InputParameterInvalidException, DataNotSavedException {
		// TODO User tracking can be added in future
		log.info("User XYZ Trying to add contact" + inputContact);
		validateInput(inputContact);
		Optional<Contact> contactByName;
		try {
			contactByName = contactRepo.findByName(inputContact.getContactName());
		} catch (Exception ex) {
			log.error("Error while fetchin contact by name ");
			throw new DataNotFoundException("Error while fetchin contact by name", ex);
		}
		if (contactByName.isPresent()) {
			log.error("contact by name already exists ");
			throw new InputParameterInvalidException("Contact by name already exists");
		}
		Contact savedContact = null;
		try {
			Contact contact = contactMapper.getContactMapper()
			                               .map(inputContact, Contact.class);

			savedContact = contactRepo.save(contact);

		} catch (Exception ex) {
			log.error("Unable to add contact " + inputContact);
			throw new DataNotSavedException("Unable to add contact ", ex);
		}
		return contactMapper.getContactMapper()
		                    .map(savedContact, ContactTO.class);

	}

	/**
	 * This method performs REST input validation as this method is programmed to
	 * interface ValidationService, implementation can be changed at any time in
	 * future without touching this code
	 * 
	 * @param inputContact
	 */
	private void validateInput(ContactTO inputContact) {
		restValidationService.setValidatableObj(inputContact);
		boolean isValid = restValidationService.validate();
		if (isValid) {
			log.info("input validation passed");
		}
	}

	/**
	 * This operation supports contact modification supports writable transaction
	 * first fetches the existing contact with given id then change it as per input
	 * and updates the DB
	 * 
	 * @param inputContact - Service input
	 * @return ContactTO - transfer object
	 */
	@Override
	@Transactional
	public ContactTO modifyContact(ContactTO inputContact)
	        throws DataNotSavedException, InputParameterInvalidException {
		log.info("Trying to modify contact" + inputContact);
		validateInput(inputContact);
		Optional<Contact> optionalContact;
		try {
			optionalContact = contactRepo.findById(inputContact.getContactID());
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while fetching contact ").append(
			        inputContact.getContactID())
			                                                                            .toString();
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!optionalContact.isPresent()) {
			String errorMsg = "Contact with id : " + inputContact.getContactID() + " not found";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
		Contact savedContact = optionalContact.get();
		prepareContactToUpdate(savedContact, inputContact);
		Contact updatedContact = null;
		try {
			updatedContact = contactRepo.save(savedContact);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while modifying contact ").append(savedContact)
			                                                                             .toString();
			log.error(errorMsg);
			throw new DataNotSavedException(errorMsg, ex);
		}
		return contactMapper.getContactMapper()
		                    .map(updatedContact, ContactTO.class);

	}

	/**
	 * This method maps contact details passed in input to entity to be modified
	 * 
	 * @param savedContact - Entity to be modified
	 * @param inputContact - input to the service
	 */
	private void prepareContactToUpdate(Contact savedContact, ContactTO inputContact) {
		log.info(new StringBuilder("preparing object to be modified using ").append(inputContact)
		                                                                    .toString());
		if (!CommonUtils.isStringNullorEmpty(inputContact.getContactName())) {
			savedContact.setContactName(inputContact.getContactName());
		}
		if (!CommonUtils.isStringNullorEmpty(inputContact.getFacebookID())) {
			savedContact.setFacebookID(inputContact.getFacebookID());
		}
		if (!CommonUtils.isStringNullorEmpty(inputContact.getTwitterID())) {
			savedContact.setTwitterID(inputContact.getTwitterID());
		}
		if (!CommonUtils.isStringNullorEmpty(inputContact.getPhoneNumber())) {
			savedContact.setPhoneNumber(inputContact.getPhoneNumber());
		}
	}

	/**
	 * This operation deletes contact in the system if exists
	 * 
	 * @return - true if deletion success , false otherwise
	 */
	@Override
	@Transactional
	public boolean deleteContact(Long contactID) throws DataNotSavedException {
		// TODO user tracking in future
		log.info("User XYZ trying to delete contact " + contactID);
		validateInput(contactID);
		Optional<Contact> findById;
		try {
			findById = contactRepo.findById(contactID);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while fetching contact ").append(contactID)
			                                                                            .toString();
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!findById.isPresent()) {
			throw new DataNotFoundException("Contact id " + contactID + "Not found");
		}
		try {
			contactRepo.deleteById(contactID);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while deleting contact ").append(contactID)
			                                                                            .toString();
			log.error(errorMsg);
			throw new DataNotSavedException(errorMsg, ex);
		}
		return true;
	}

	/**
	 * Helper method for input validation
	 * 
	 * @param contactID
	 */
	private void validateInput(Long contactID) {
		if (contactID == null) {
			throw new InputParameterInvalidException("Contact id not passed to delete contact", "contactID",
			        "null/empty");
		}
	}

	/**
	 * This operation fetches available contacts,supports read only transaction
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ContactTO> fetchContacts() throws DataNotFoundException {
		// TODO user tracking in future can be added
		log.info("User XYZ trying to fetch all contacts");
		List<Contact> allContacts;
		try {
			allContacts = contactRepo.findAll();
		} catch (Exception ex) {
			String errorMsg = "Error occured while fetching contacts ";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (allContacts == null || allContacts.isEmpty()) {
			log.error("No contacts found");
			throw new DataNotFoundException("No contacts found");
		}
		List<ContactTO> lstContactTO = allContacts.stream()
		                                          .map(e -> contactMapper.getContactMapper()
		                                                                 .map(e, ContactTO.class))
		                                          .collect(Collectors.toList());

		return lstContactTO;
	}

	/**
	 * This operation checks if contact exists or not ,supports read only
	 * transaction
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean contactExists(Long id) throws DataNotFoundException, InputParameterInvalidException {
		validateInput(id);
		boolean isExists;
		try {
			isExists = contactRepo.existsById(id);
		} catch (Exception ex) {
			String errorMsg = "error occured while fetching contact";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		return isExists;
	}

	/**
	 * This operation searches for contact with passed id,supports read only
	 * transaction
	 */
	@Override
	@Transactional(readOnly = true)
	public ContactTO searchContact(Long contactID) throws DataNotFoundException {
		// TODO User tracking can be added in future
		log.info("User XYZ trying to search contact" + contactID);
		validateInput(contactID);
		Optional<Contact> optionalContact;
		try {
			optionalContact = contactRepo.findById(contactID);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while fetching contact ").append(contactID)
			                                                                            .toString();
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!optionalContact.isPresent()) {
			throw new DataNotFoundException("Contact id " + contactID + "Not found");
		}
		return contactMapper.getContactMapper()
		                    .map(optionalContact.get(), ContactTO.class);
	}

}
