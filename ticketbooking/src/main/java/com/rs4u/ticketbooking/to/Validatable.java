package com.rs4u.ticketbooking.to;

/**
 * Any class implementing this interface assume to be validatable entity
 * 
 * @author Rahil
 *
 */
public interface Validatable {
	public boolean isValid() throws Exception;
}
