package com.rs4u.ticketbooking.to;

import com.rs4u.ticketbooking.utils.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTO implements Validatable {

	private String userName;

	private String password;

	private String role;

	@Override
	public boolean isValid() throws Exception {

		return !(CommonUtils.isStringNullorEmpty(userName) || CommonUtils.isStringNullorEmpty(password));
	}

}
