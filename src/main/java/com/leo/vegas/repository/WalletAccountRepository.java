package com.leo.vegas.repository;

import com.leo.vegas.entity.WalletAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Account repository
 */
@Repository
@Transactional
public interface WalletAccountRepository extends CrudRepository<WalletAccount, Long> {

	/**
	 *
	 * @param userId User id to get the wallet account
	 * @return wallet account {@link WalletAccount}
	 */
	WalletAccount findByUserId(String userId);
}
