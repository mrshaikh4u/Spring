package com.rs4u.ticketbooking.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rs4u.ticketbooking.domain.User;

/**
 * JPA repository for Contact Entity
 * 
 * @author Rahil
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("FROM User t where t.userName = ?1 ")
	public Optional<User> findByUserName(String userName);
}
