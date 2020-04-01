package com.mycomp.socialApp.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mycomp.socialApp.domain.Contact;
import com.mycomp.socialApp.domain.UserTransactions;

/**
 * JPA repository for UserTransactions Entity
 * 
 * @author Rahil
 *
 */
@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransactions, Long> {
	@Query("FROM UserTransactions t where t.contact = ?1 ")
	public List<UserTransactions> findByContact(Contact contact);

	@Query("FROM UserTransactions t where t.contact = ?1 and t.transactionDate >= ?2 and t.transactionDate <= ?3")
	public List<UserTransactions> extractDateWiseReport(Contact contact, LocalDateTime fromDate, LocalDateTime toDate);
}
