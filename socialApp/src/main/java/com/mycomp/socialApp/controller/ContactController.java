package com.mycomp.socialApp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycomp.socialApp.exceptions.DataNotFoundException;
import com.mycomp.socialApp.exceptions.DataNotSavedException;
import com.mycomp.socialApp.service.ContactService;
import com.mycomp.socialApp.to.ContactTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller class for contact management APIs
 * 
 * @author Rahil
 *
 */
@RestController
@RequestMapping("/api/v1")
@Api(value = "Contact Management APIs", description = "Operations for contact management")
public class ContactController {

	@Autowired
	protected ContactService contactService;

	@PostMapping("/contacts")
	@ApiOperation(value = "Add new contact in the system,returns added contact", response = ContactTO.class)
	public ContactTO addContact(
	        @Valid @RequestBody @ApiParam(value = "input contact's details that needs to be added", required = true) ContactTO inputContact)
	        throws DataNotSavedException {
		return contactService.addContact(inputContact);
	}

	@GetMapping("/contacts/{contactID}")
	@ApiOperation(value = "Search contact,returns contact if found", response = ContactTO.class)
	public ContactTO searchContact(@PathVariable @ApiParam(value = "contact id to search") Long contactID)
	        throws DataNotFoundException {
		return contactService.searchContact(contactID);
	}

	@GetMapping("/contacts")
	@ApiOperation(value = "Fetch all available contacts in the system,returns available contacts")
	public List<ContactTO> fetchContacts() throws DataNotFoundException {
		return contactService.fetchContacts();
	}

	@PutMapping("/contacts/{contactID}")
	@ApiOperation(value = "Update contact in the system ,returns modified contact details", response = ContactTO.class)
	public ContactTO updateContact(@Valid @RequestBody @ApiParam(value = "Contact to update") ContactTO input,
	        @PathVariable @ApiParam(value = "contactID which needs to be updated") Long contactID)
	        throws DataNotSavedException {
		input.setContactID(contactID);
		return contactService.modifyContact(input);
	}

	@DeleteMapping("/contacts/{contactID}")
	@ApiOperation(value = "Delete Contact in the system ,returns 200 success OK upon succesful deletion")
	void deleteContact(@PathVariable @ApiParam(value = "contact id to be deleted") Long contactID)
	        throws DataNotSavedException {
		contactService.deleteContact(contactID);
	}

}
