package com.rs4u.ticketbooking.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.rs4u.ticketbooking.domain.User;
import com.rs4u.ticketbooking.to.UserTO;

import lombok.Data;

/**
 * This is Util class for Contact mapping it uses ModelMapper
 * 
 * @author Rahil
 *
 */
@Component
@Data
public class UserMapper implements InitializingBean {

	private ModelMapper userMapper;

	public UserMapper() {
		userMapper = new ModelMapper();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		TypeMap<UserTO, User> typeMap = getUserMapper().createTypeMap(UserTO.class, User.class);
		typeMap.addMappings(mapper -> mapper.skip(User::setUserID));

	}
}
