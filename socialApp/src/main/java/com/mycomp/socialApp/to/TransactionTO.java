package com.mycomp.socialApp.to;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transaction Transfer object
 * 
 * @author Rahil
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model to represent single Transaction")
public class TransactionTO {

	@ApiModelProperty(notes = "Transaction id")
	private Long transactionID;

	@ApiModelProperty(notes = "Transaction amount")
	private Double transactionAmount;

	@ApiModelProperty(notes = "Transaction date")
	private LocalDateTime transactionDate;

	@ApiModelProperty(notes = "Transaction Channel")
	private String channel;

}
