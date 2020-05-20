package com.leo.vegas.repository;

import com.leo.vegas.entity.WalletTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Transaction repository
 */

@Repository
@Transactional
public interface WalletTransactionRepository extends CrudRepository<WalletTransaction, Long> {

	/**
	 * Method  to get the list of transaction by user id
	 *
	 * @param userId  user id
	 * @return list of {@link WalletTransaction}
	 */
	List<WalletTransaction> findByWalletAccountUserId(String userId);

	/**
	 * Method to check if the transaction id is unique.
	 *
	 * @param transactionId   transaction id
	 * @return boolean value true or false
	 */
	@Query(value = "select case when count(wt)> 0 then true else false end from WalletTransaction wt where lower(wt.transactionId) like lower(:transactionId)")
	boolean existsByTransactionId(String transactionId);

}
