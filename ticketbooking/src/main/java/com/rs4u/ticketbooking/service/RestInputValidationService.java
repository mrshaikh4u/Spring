package com.rs4u.ticketbooking.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rs4u.ticketbooking.exceptions.InputParameterInvalidException;

import lombok.extern.slf4j.Slf4j;

/**
 * This is implementation of Generic ValidationService providing validations for
 * REST input
 * 
 * this demonstrate the benefit of Composition over inheritance existing code
 * doesn't need to be changed for enhanceme nts
 * 
 * @author Rahil
 *
 */
@Service
@Slf4j
@Qualifier("RESTInputValidator")
public class RestInputValidationService extends ValidationService {

	@Override
	public boolean validate() throws InputParameterInvalidException {
		boolean isValid;
		try {
			isValid = this.getValidatableObj()
			              .isValid();
		} catch (Exception e) {
			log.error("Input validation failed");
			throw new InputParameterInvalidException("Invalid REST input", e);
		}
		return isValid;
	}

}
