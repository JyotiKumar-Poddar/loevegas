package com.leo.vegas.service;

import com.leo.vegas.entity.WalletAccount;

import java.math.BigDecimal;

public interface WalletAccountService {


	/**
	 * Method for debit operation for a given transaction
	 *
	 * @param accountId  wallet account id
	 * @param amount transaction amount
	 * @param transactionId transaction id
	 */
	void debitWalletAccount(Long accountId, BigDecimal amount, String transactionId);

	/**
	 * Method for credit operation for a given transaction
	 *
	 * @param accountId  wallet account id
	 * @param amount transaction amount
	 * @param transactionId transaction id
	 */
	void creditWalletAccount(Long accountId, BigDecimal amount, String transactionId);

	/**
	 * Method to get the user wallet account
	 * @param userId user id
	 * @return wallet account if present else null.
	 */
	WalletAccount findByUserId(String userId);
}
