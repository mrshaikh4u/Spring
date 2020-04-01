package com.mycomp.socialApp.to;

import javax.validation.constraints.NotEmpty;

import com.mycomp.socialApp.exceptions.InputParameterInvalidException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contact Transfer object
 * 
 * @author Rahil
 *
 */
@Data
@ApiModel(description = "All details about contact")
@AllArgsConstructor
@NoArgsConstructor
public class ContactTO implements Validatable {

	@ApiModelProperty(notes = "Auto generated contact ID")
	private Long contactID;

	@ApiModelProperty(notes = "Contact's name")
	@NotEmpty(message = "Contact name can not be empty")
	private String contactName;

	@ApiModelProperty(notes = "Contact's facebook id")
	private String facebookID;

	@ApiModelProperty(notes = "Contact's twitter id")
	private String twitterID;

	@ApiModelProperty(notes = "Contact's phone number")
	private String phoneNumber;

	/**
	 * more business validations can be added currently just checking for name
	 */
	@Override
	public boolean validate() throws InputParameterInvalidException {
		if (contactName == null || contactName.isEmpty()) {
			throw new InputParameterInvalidException("ContactTO", "Contact name not passed in input", "name",
			        "null/empty");
		}
		return true;
	}

}
