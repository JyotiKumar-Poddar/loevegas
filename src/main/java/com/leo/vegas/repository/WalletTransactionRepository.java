package com.leo.vegas.repository;

import com.leo.vegas.entity.WalletTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends CrudRepository<WalletTransaction, Long> {

	List<WalletTransaction> findByWalletAccountUserId(String userId);  //FIXME :check the query

	@Query(value = "select case when count(wt)> 0 then true else false end from WalletTransaction wt where lower(wt.transactionId) like lower(:transactionId)")
	boolean existsByTransactionId(String transactionId);

}
