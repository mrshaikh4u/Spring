package com.mycomp.socialApp.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity class for user transaction
 * 
 * @author Rahil
 *
 */
@Entity
@Table(name = "TBL_User_Transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaction_id")
	private Long transactionID;

	@Column(name = "transaction_amount")
	private Double transactionAmount;

	@NotNull
	private LocalDateTime transactionDate;

	@NotNull
	private String channel;

	@ManyToOne
	@JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
	@EqualsAndHashCode.Exclude
	private Contact contact;

}
