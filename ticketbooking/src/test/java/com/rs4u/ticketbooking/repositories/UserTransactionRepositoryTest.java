package com.rs4u.ticketbooking.repositories;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.domain.UserTransactions;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTransactionRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserTransactionRepository userTransactionRepository;

	@Autowired
	private ContactRepository contactRepo;

	@Test
	public void whenSaveUserTransaction_thenReturnSavedUserTrans() {

		UserTransactions userTransaction = new UserTransactions();
		Contact contact = new Contact();
		contact.setContactName("Rahil");
		Contact savedContact = contactRepo.save(contact);
		userTransaction.setContact(savedContact);
		userTransaction.setTransactionDate(LocalDateTime.now());
		userTransaction.setTransactionAmount(1.0);
		userTransaction.setChannel("Facebook");
		UserTransactions persistedUserTrans = entityManager.persist(userTransaction);
		entityManager.flush();
		Optional<UserTransactions> retrievedUserTrans = userTransactionRepository.findById(
		        persistedUserTrans.getTransactionID());
		assertTrue(retrievedUserTrans.get()
		                             .getContact()
		                             .getContactID()
		                             .equals(contact.getContactID()));
	}

	@Test
	public void whenSaveUserTrans_thenReturnAsPerDate() {
		UserTransactions userTransaction = new UserTransactions();
		Contact contact = new Contact();
		contact.setContactName("Rahil");
		Contact savedContact = contactRepo.save(contact);
		userTransaction.setContact(savedContact);
		userTransaction.setTransactionDate(LocalDateTime.now());
		userTransaction.setTransactionAmount(1.0);
		userTransaction.setChannel("Facebook");
		UserTransactions persistedUserTrans = entityManager.persist(userTransaction);
		entityManager.flush();
		List<UserTransactions> retrievedUserTrans = userTransactionRepository.extractDateWiseReport(savedContact,
		        LocalDateTime.of(2020, 02, 20, 11, 59), LocalDateTime.of(2021, 02, 20, 11, 59));
		assertTrue(!retrievedUserTrans.isEmpty());

	}

}
