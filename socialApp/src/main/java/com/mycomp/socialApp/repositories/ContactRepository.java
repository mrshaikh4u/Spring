package com.mycomp.socialApp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mycomp.socialApp.domain.Contact;

/**
 * JPA repository for Contact Entity
 * 
 * @author Rahil
 *
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
	@Query("FROM Contact t where t.contactName = ?1 ")
	public Optional<Contact> findByName(String contactName);
}
