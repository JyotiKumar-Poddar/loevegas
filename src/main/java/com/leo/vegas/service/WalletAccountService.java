package com.leo.vegas.service;

import com.leo.vegas.entity.WalletAccount;

import java.math.BigDecimal;

public interface WalletAccountService {


	void debitWalletAccount(Long accountId, BigDecimal amount, String transactionId);

	void creditWalletAccount(Long accountId, BigDecimal amount, String transactionId);

	WalletAccount findByUserId(String userId);
}
