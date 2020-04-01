package com.mycomp.socialApp.service;

import java.util.List;

import com.mycomp.socialApp.exceptions.DataNotFoundException;
import com.mycomp.socialApp.exceptions.DataNotSavedException;
import com.mycomp.socialApp.to.ContactTO;

public interface UserOperationsService {

	public ContactTO addContact(ContactTO inputContact) throws DataNotSavedException;

	public ContactTO modifyContact(ContactTO inputContact) throws DataNotSavedException;

	public boolean deleteContact(Long contactID) throws DataNotSavedException;

	public List<ContactTO> fetchContacts() throws DataNotFoundException;

	public boolean contactExists(Long id) throws DataNotFoundException;

	public ContactTO searchContact(Long contactID) throws DataNotFoundException;

}
