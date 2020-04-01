package com.mycomp.socialApp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * domain class to store message to be sent here body can be used to store
 * direct message in case messageType = text or path to video message if
 * uploaded to s3 bucket or any other storage
 * 
 * @author Rahil
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	private String messageBody;
	private MessageType messageType;
}
