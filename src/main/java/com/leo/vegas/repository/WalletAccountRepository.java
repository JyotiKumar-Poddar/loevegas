package com.leo.vegas.repository;

import com.leo.vegas.entity.WalletAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletAccountRepository extends CrudRepository<WalletAccount, Long> {

	WalletAccount findByUserId(String userId);
}
