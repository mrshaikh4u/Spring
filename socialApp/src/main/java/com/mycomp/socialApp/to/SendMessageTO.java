package com.mycomp.socialApp.to;

import javax.validation.constraints.NotEmpty;

import com.mycomp.socialApp.domain.Message;
import com.mycomp.socialApp.exceptions.InputParameterInvalidException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SendMessage Transfer object
 * 
 * @author Rahil
 *
 */
@ApiModel(description = "All details about message to be sent")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendMessageTO implements Validatable {

	@ApiModelProperty(notes = "Auto generated contact ID")
	@NotEmpty(message = "contact id can not be empty for modify operation")
	private Long contactID;

	@ApiModelProperty(notes = "message to be sent")
	@NotEmpty(message = "message can't be empty")
	private Message message;

	@Override
	public boolean validate() throws Exception {
		if (contactID == null || message == null) {
			throw new InputParameterInvalidException("SendMessageTO", "SendMessageTO contains empty contact/message",
			        "SendMessageTO", "null/empty");
		}
		return true;
	}

}
