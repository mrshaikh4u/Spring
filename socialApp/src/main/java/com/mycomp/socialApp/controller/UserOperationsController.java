package com.mycomp.socialApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycomp.socialApp.domain.Status;
import com.mycomp.socialApp.exceptions.DataNotSavedException;
import com.mycomp.socialApp.service.ChannelService;
import com.mycomp.socialApp.service.ReportService;
import com.mycomp.socialApp.to.SendMessageTO;
import com.mycomp.socialApp.to.TransactionReportTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller class for user operations APIs
 * 
 * @author Rahil
 *
 */
@RestController
@RequestMapping("/api/v1/userOperations")
@Api(value = "User operations APIs", description = "Operations to send messages and fetch bill report")
public class UserOperationsController {

	@Autowired
	@Qualifier("faceBookChannel")
	protected ChannelService faceBookChannel;

	@Autowired
	@Qualifier("twitterChannel")
	protected ChannelService twitterChannel;

	@Autowired
	@Qualifier("whatsAppChannel")
	protected ChannelService whatsAppChannel;

	@Autowired
	@Qualifier("SimpleJSONReport")
	protected ReportService reportService;

	@PostMapping("/send/facebook")
	@ApiOperation(value = "Send message over Facebook", response = Status.class)
	public Status sendMessageFaceBook(
	        @RequestBody @ApiParam(value = "send message details", required = true) SendMessageTO sendMessageInput)
	        throws DataNotSavedException {
		return invokeService(faceBookChannel, sendMessageInput);
	}

	@PostMapping("/send/twitter")
	@ApiOperation(value = "Send message over Twitter", response = Status.class)
	public Status sendMessageTwitter(
	        @RequestBody @ApiParam(value = "send message details", required = true) SendMessageTO sendMessageInput)
	        throws DataNotSavedException {
		return invokeService(twitterChannel, sendMessageInput);
	}

	@PostMapping("/send/whatsApp")
	@ApiOperation(value = "Send message over WhatsApp", response = Status.class)
	public Status sendMessageWhatsApp(
	        @RequestBody @ApiParam(value = "send message details", required = true) SendMessageTO sendMessageInput)
	        throws DataNotSavedException {
		return invokeService(whatsAppChannel, sendMessageInput);
	}

	/**
	 * Helper method to set input param and invoke corresponding service
	 * 
	 * @param channelService
	 * @param sendMessageInput
	 * @return
	 * @throws DataNotSavedException
	 */
	private Status invokeService(ChannelService channelService, SendMessageTO sendMessageInput)
	        throws DataNotSavedException {
		channelService.setSendMessageTO(sendMessageInput);
		return channelService.send();
	}

	@PostMapping("/report")
	@ApiOperation(value = "Fetch bill report for specific duration", response = Status.class)
	public TransactionReportTO extractReport(
	        @RequestBody @ApiParam(value = "send message details", required = true) TransactionReportTO reportInput)
	        throws DataNotSavedException {
		reportService.setTransactionReportTO(reportInput);
		return reportService.generateReport();
	}

}
