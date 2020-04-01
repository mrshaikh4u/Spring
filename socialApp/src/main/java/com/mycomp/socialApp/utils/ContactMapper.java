package com.mycomp.socialApp.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.to.ContactTO;

import lombok.Data;

/**
 * This is Util class for Contact mapping it uses ModelMapper
 * 
 * @author Rahil
 *
 */
@Component
@Data
public class ContactMapper implements InitializingBean {

	private ModelMapper contactMapper;

	public ContactMapper() {
		contactMapper = new ModelMapper();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		TypeMap<ContactTO, Contact> typeMap = getContactMapper().createTypeMap(ContactTO.class, Contact.class);
		typeMap.addMappings(mapper -> mapper.skip(Contact::setContactID));

	}
}
