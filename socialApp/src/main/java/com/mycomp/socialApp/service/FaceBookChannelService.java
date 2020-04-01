package com.mycomp.socialApp.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mycomp.socialApp.domain.ErrorCode;
import com.mycomp.socialApp.domain.Status;
import com.mycomp.socialApp.utils.AppConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * This class has Specialization sending message over facebook
 * 
 * @author Rahil
 *
 */
@Slf4j
@Service
@Qualifier("faceBookChannel")
public class FaceBookChannelService extends ChannelService {

	@Override
	protected Status sendAtChannel() {
		log.info("Sending FB message to " + this.sendMessageTO.getContactID());
		this.setChannel("Facebook");
		return new Status(ErrorCode.SUCCESS, AppConstants.SUCCESS_DESCRIPTION);
	}

	@Override
	public Double calculateSendingCost() {
		return 0.01;
	}

}
