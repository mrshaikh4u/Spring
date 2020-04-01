package com.mycomp.socialApp.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
import com.mycomp.socialApp.domain.ErrorCode;
import com.mycomp.socialApp.domain.Message;
import com.mycomp.socialApp.domain.MessageType;
import com.mycomp.socialApp.domain.Status;
import com.mycomp.socialApp.exceptions.DataNotSavedException;
import com.mycomp.socialApp.repositories.ContactRepository;
import com.mycomp.socialApp.repositories.UserTransactionRepository;
import com.mycomp.socialApp.to.SendMessageTO;
import com.mycomp.socialApp.utils.ContactMapper;

@RunWith(SpringRunner.class)
public class TwitterChannelServiceTest {
	@TestConfiguration
	static class ContactServiceImplTestContextConfiguration {

		@Bean
		public ChannelService twitterChannelService() {
			return new TwitterChannelService();
		}

		@Bean
		public ContactMapper contactMapper() {
			return new ContactMapper();
		}

		@Bean
		public ValidationService validationService() {
			return new RestInputValidationService();
		}

	}

	@Autowired
	private ChannelService channelService;

	@MockBean
	private UserTransactionRepository userTransactionRepo;

	@MockBean
	private ContactRepository contactRepo;

	@MockBean
	@Qualifier("RESTInputValidator")
	private ValidationService validationService;

	@Before
	public void setUp() throws DataNotSavedException {

		Contact contact = new Contact(111l, "Rahil", "Rah@FB", "Rah@TWT", "971505050505", null);
		Optional<Contact> optionalContact = Optional.of(contact);
		when(contactRepo.findById(Mockito.anyLong())).thenReturn(optionalContact);

	}

	@Test
	public void whenSendAtChannel_thenStatusSuccess() throws DataNotSavedException {
		channelService.setSendMessageTO(prepareObj());
		Status status = channelService.sendAtChannel();
		assertTrue(status.getStatusCode()
		                 .equals(ErrorCode.SUCCESS));
	}

	@Test
	public void whenCalculateSendingCost_thenReturnVal() throws DataNotSavedException {
		channelService.setSendMessageTO(prepareObj());
		Double val = channelService.calculateSendingCost();
		assertTrue(val.equals(0.01));
	}

	@Test
	public void whenSend_thenStatusSuccessAndChannelVal() throws DataNotSavedException {
		channelService.setSendMessageTO(prepareObj());
		Status status = channelService.send();
		assertTrue(status.getStatusCode()
		                 .equals(ErrorCode.SUCCESS));
		assertTrue(channelService.getChannel()
		                         .equals("Twitter"));
	}

	private SendMessageTO prepareObj() {
		return new SendMessageTO(111l, new Message("test", MessageType.TEXT));
	}

}
