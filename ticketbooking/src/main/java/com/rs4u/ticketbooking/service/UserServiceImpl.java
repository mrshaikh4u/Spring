package com.rs4u.ticketbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rs4u.ticketbooking.exceptions.DataNotSavedException;
import com.rs4u.ticketbooking.repositories.UserRepository;
import com.rs4u.ticketbooking.to.UserTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	@Qualifier("RESTInputValidator")
	protected ValidationService validationService;

	@Override
	public UserTO login(UserTO inputUser) throws DataNotSavedException {
		validateInput(inputUser);
		return null;
	}

	protected void validateInput(UserTO inputUser) {
		validationService.setValidatableObj(inputUser);
		validationService.validate();
	}

	@Override
	public UserTO signUP(UserTO inputUser) throws DataNotSavedException {
		// TODO Auto-generated method stub
		return null;
	}

}
