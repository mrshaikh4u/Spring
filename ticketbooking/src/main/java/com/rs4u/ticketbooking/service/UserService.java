package com.rs4u.ticketbooking.service;

import com.rs4u.ticketbooking.exceptions.DataNotSavedException;
import com.rs4u.ticketbooking.to.UserTO;

/**
 * Contact service with supported operations
 * 
 * @author Rahil
 *
 */
public interface UserService {
	public UserTO login(UserTO inputUser) throws DataNotSavedException;

	public UserTO signUP(UserTO inputUser) throws DataNotSavedException;

}
