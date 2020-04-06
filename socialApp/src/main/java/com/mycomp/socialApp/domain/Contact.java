package com.mycomp.socialApp.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "TBL_CONTACT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "contact_id")
	private Long contactID;

	@Column(name = "Contact_name")
	private String contactName;

	@Column(name = "FB_ID")
	private String facebookID;

	@Column(name = "Twitter_ID")
	private String twitterID;
	
	

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ToString.Exclude
	private Set<UserTransactions> userTransactions = new HashSet<UserTransactions>();

}
