package com.rs4u.ticketbooking.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycomp.socialApp.domain.Contact;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContactRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ContactRepository contactRepository;

	@Test
	public void whenSaveContact_thenReturnSavedContact() {
		Contact contact = new Contact();
		contact.setContactName("Rahil");
		Contact persistedContact = entityManager.persist(contact);
		entityManager.flush();
		Optional<Contact> retrievedContact = contactRepository.findById(persistedContact.getContactID());
		assertTrue(retrievedContact.get()
		                           .getContactName()
		                           .equals(contact.getContactName()));
	}

	@Test
	public void whenDeleteContact_thenReturnNull() {
		Contact contact = new Contact();
		contact.setContactName("Rahil");
		Contact persistedContact = entityManager.persist(contact);
		entityManager.flush();
		Optional<Contact> retrievedContact = contactRepository.findById(persistedContact.getContactID());
		Long savedContactID = retrievedContact.get()
		                                      .getContactID();

		contactRepository.deleteById(savedContactID);
		Optional<Contact> findById = contactRepository.findById(savedContactID);
		assertFalse(findById.isPresent());
	}

}
