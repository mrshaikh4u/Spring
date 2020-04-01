package com.mycomp.socialApp.to;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.mycomp.socialApp.exceptions.InputParameterInvalidException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transaction report validation object
 * 
 * @author Rahil
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model to give transaction report [fromDate - toDate]")
public class TransactionReportTO implements Validatable {
	@ApiModelProperty(notes = "Start Date of report")
	@NotEmpty(message = "From Date is mandatory")
	private LocalDateTime fromDate;
	@ApiModelProperty(notes = "end Date of report")
	@NotEmpty(message = "To Date is mandatory")
	private LocalDateTime toDate;
	@ApiModelProperty(notes = "Transactions in the given duration")
	private List<TransactionTO> transactions;

	@ApiModelProperty(notes = "contact for whom report is extracted")
	@NotEmpty(message = "Contact is required")
	private ContactTO contact;

	@Override
	public boolean validate() throws Exception {
		if (fromDate == null || fromDate == null || invalidContact(contact)) {
			throw new InputParameterInvalidException("TransactionReportTO",
			        "TransactionReportTO contains empty dates/contact", "TransactionReportTO", "null/empty");
		}
		return true;
	}

	private boolean invalidContact(ContactTO contact2) {
		return contact == null || contact.getContactID() == null;
	}
}
