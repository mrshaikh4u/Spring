package com.rs4u.ticketbooking.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.exceptions.DataNotSavedException;
import com.mycomp.socialApp.to.ContactTO;
import com.mycomp.socialApp.utils.ContactMapper;
import com.rs4u.ticketbooking.repositories.ContactRepository;

@RunWith(SpringRunner.class)
public class ContactServiceImplTest {
	@TestConfiguration
	static class ContactServiceImplTestContextConfiguration {

		@Bean
		public ContactService contactService() {
			return new ContactServiceImpl();
		}

		@Bean
		public ContactMapper contactMapper() {
			return new ContactMapper();
		}

		@Bean
		@Qualifier("RESTInputValidator")
		public ValidationService validationService() {
			return new RestInputValidationService();
		}

	}

	@Autowired
	private ContactService contactService;

	@Autowired
	private ContactMapper mapper;

	@MockBean
	private ContactRepository contactRepo;

	@Before
	public void setUp() {

		when(contactRepo.save(Mockito.any(Contact.class))).then(returnsFirstArg());
		Contact contact = new Contact(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505", null);
		Optional<Contact> optionalContact = Optional.of(contact);
		when(contactRepo.findById(Mockito.anyLong())).thenReturn(optionalContact);
		when(contactRepo.findAll()).thenReturn(prepareContactList());
		doNothing().when(contactRepo)
		           .deleteById(Mockito.anyLong());
		when(contactRepo.existsById(Mockito.anyLong())).thenReturn(true);

	}

	private List<Contact> prepareContactList() {
		return Arrays.asList(new Contact(11l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505", null));
	}

	@Test
	public void whenAdd_thenContactShouldBeAdded() throws DataNotSavedException {
		ContactTO inputContact = new ContactTO(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505");
		ContactTO addedContact = contactService.addContact(inputContact);
		assertTrue(addedContact.getContactName()
		                       .equals(inputContact.getContactName()));
	}

	@Test
	public void whenModify_thenContactShouldBeModified() throws DataNotSavedException {
		ContactTO inputContact = new ContactTO(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505");

		Contact contact = mapper.getContactMapper()
		                        .map(inputContact, Contact.class);
		contactRepo.save(contact);
		inputContact.setContactName("John");
		ContactTO modfiedContact = contactService.modifyContact(inputContact);
		assertTrue(modfiedContact.getContactName()
		                         .equals(inputContact.getContactName()));
	}

	@Test
	public void whenDelete_thenContactShouldBeDeleted() throws DataNotSavedException {
		ContactTO inputContact = new ContactTO(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505");
		ContactTO addedContact = contactService.addContact(inputContact);
		addedContact.setContactID(inputContact.getContactID());
		assertDoesNotThrow(() -> contactService.deleteContact(addedContact.getContactID()));
	}

	@Test
	public void whenSearch_thenContactShouldBeReturned() throws DataNotSavedException {
		ContactTO inputContact = new ContactTO(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505");
		ContactTO addedContact = contactService.addContact(inputContact);
		addedContact.setContactID(inputContact.getContactID());
		ContactTO searchedContact = contactService.searchContact(addedContact.getContactID());
		assertTrue(searchedContact.getContactName()
		                          .equals(inputContact.getContactName()));
	}

	@Test
	public void whenFetchAll_thenContactShouldBeReturned() throws DataNotSavedException {
		ContactTO inputContact = new ContactTO(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505");
		ContactTO addedContact = contactService.addContact(inputContact);
		addedContact.setContactID(inputContact.getContactID());
		List<ContactTO> searchedContact = contactService.fetchContacts();
		assertFalse(searchedContact.isEmpty());
	}

	@Test
	public void whenExists_thenReturnTrue() throws DataNotSavedException {
		ContactTO inputContact = new ContactTO(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505");
		ContactTO addedContact = contactService.addContact(inputContact);
		addedContact.setContactID(inputContact.getContactID());
		assertTrue(contactService.contactExists(addedContact.getContactID()));
	}

}
