package com.mycomp.socialApp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.exceptions.DataNotFoundException;
import com.mycomp.socialApp.repositories.ContactRepository;
import com.mycomp.socialApp.to.TransactionReportTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * This is generic class for Reporting services This is the example of Template
 * design pattern where parent class provides a template for a task and provide
 * implementation of common functionalities like common inputValidatios /
 * tracking/logging/ also gives hook methods if any customization is needed in
 * future
 * 
 * For eg. if new reporting technique need to be added in future then it just
 * need to give specific implementation of extractReportInternal() all other
 * tasks are already done for him
 * 
 * @author Rahil
 *
 */
@Data
@Slf4j
@Service
public abstract class ReportService {
	protected TransactionReportTO transactionReportTO;

	@Autowired
	protected ContactRepository contactRepo;

	@Autowired
	@Qualifier("RESTInputValidator")
	protected ValidationService restValidationService;

	/**
	 * Generic template method for report generation exposed to controllers.
	 * subclasses can enhance and provide specific implementation based on requested
	 * report from controller
	 * 
	 * @return
	 */
	public TransactionReportTO generateReport() {
		startReportGenerationHook();
		methodStartlog();
		validateInput();
		TransactionReportTO output = extractReportInternal();
		methodEndlog();
		endReportGenerationHook();
		return output;
	}

	/**
	 * Reserved for future enhancement
	 */
	protected void endReportGenerationHook() {
	}

	/**
	 * Reserved for future enhancement
	 */
	protected void startReportGenerationHook() {
	}

	/**
	 * Report generation end logs
	 */
	protected void methodEndlog() {
		log.info("User XYZ extracted report");

	}

	/**
	 * Report generation start logs
	 */
	protected void methodStartlog() {
		log.info("User XYZ is extracting report");
	}

	/**
	 * Helper method for input validations
	 */
	protected void validateInput() {
		restValidationService.setValidatableObj(this.getTransactionReportTO());
		boolean isValid = restValidationService.validate();
		if (isValid) {
			Optional<Contact> optional = contactRepo.findById(this.getTransactionReportTO()
			                                                      .getContact()
			                                                      .getContactID());
			if (!optional.isPresent()) {
				throw new DataNotFoundException("contact not found");
			}
			log.info("input validation passed");
		}
	}

	/**
	 * Sub class has to provide specific implementation for report generation
	 * 
	 * @return
	 */
	public abstract TransactionReportTO extractReportInternal();

}
