package com.leo.vegas.controller;


import com.leo.vegas.entity.WalletAccount;
import com.leo.vegas.entity.WalletTransaction;
import com.leo.vegas.exception.ApiException;
import com.leo.vegas.model.TransactionModel;
import com.leo.vegas.model.mapper.TransactionModelMapper;
import com.leo.vegas.service.impl.WalletAccountServiceImpl;
import com.leo.vegas.service.impl.WalletTransactionServiceImpl;
import com.leo.vegas.util.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.leo.vegas.exception.ApiErrorMessageAndCode.USER_NOT_FOUND;
import static com.leo.vegas.exception.ApiErrorMessageAndCode.invalidUser;

/**
 * Controller responsible for current balance, making debit,credit to the wallet account
 * and also transaction history.
 */
@RestController
@Slf4j
public class TransactionController {


	private final WalletAccountServiceImpl walletAccountServiceImpl;
	private final WalletTransactionServiceImpl walletTransactionServiceImpl;

	@Autowired
	public TransactionController(WalletAccountServiceImpl walletAccountServiceImpl,
	                             WalletTransactionServiceImpl walletTransactionServiceImpl) {
		this.walletAccountServiceImpl = walletAccountServiceImpl;
		this.walletTransactionServiceImpl = walletTransactionServiceImpl;
	}

	/**
	 * Method to get wallet account balance
	 *
	 * @param userId user id
	 * @return {@link com.leo.vegas.model.WalletAccountModel} containing account info.
	 */
	@GetMapping("/account/{userId}")
	public ResponseEntity<?> geAccount(@PathVariable String userId) {
		log.info("Fetching account details for user id: {}", userId);
		WalletAccount walletAccount = validateAndGetWalletAccount(userId);
		return ResponseEntity.ok(TransactionModelMapper.toWalletModelFunc(walletAccount));
	}

	/**
	 * Method to get list of transactions
	 *
	 * @param userId user id
	 * @return {@link com.leo.vegas.model.WalletTransactionModel} containing account info.
	 */
	@GetMapping("/transaction/{userId}")
	public ResponseEntity<?> getTransaction(@PathVariable String userId) {
		log.info("Fetching transaction details for user id: {}", userId);
		validateAndGetWalletAccount(userId);
		List<WalletTransaction> walletTransaction = walletTransactionServiceImpl.findTransactionsByUserId(userId);
		return ResponseEntity.ok(TransactionModelMapper.toWalletTransactionsModelFunc(walletTransaction));
	}

	/**
	 * Method for debit transaction
	 *
	 * @param transactionModel {@link TransactionModel}
	 * @return transaction with status {@link com.leo.vegas.model.WalletTransactionModel}
	 */
	@PostMapping("/debit")
	public ResponseEntity<?> debitWalletAccount(@Valid @RequestBody final TransactionModel transactionModel) {
		log.info("Debit Transaction  request for transaction id: {} ", transactionModel.getTransactionId());
		validateAndGetWalletAccount(transactionModel.getUserId());
		transactionModel.setTransactionType(TransactionType.DEBIT);
		WalletTransaction walletTransaction = walletTransactionServiceImpl.saveTransaction(transactionModel);
		return ResponseEntity.ok(TransactionModelMapper.toWalletTransactionModelFunc(walletTransaction));
	}

	/**
	 * Method for credit transaction
	 *
	 * @param transactionModel {@link TransactionModel}
	 * @return transaction with status {@link com.leo.vegas.model.WalletTransactionModel}
	 */
	@PostMapping("/credit")
	public ResponseEntity<?> creditWalletAccount(@Valid @RequestBody final TransactionModel transactionModel) {
		log.info("Credit Transaction  request for transaction id: {} ", transactionModel.getTransactionId());
		validateAndGetWalletAccount(transactionModel.getUserId());
		transactionModel.setTransactionType(TransactionType.CREDIT);
		WalletTransaction walletTransaction = walletTransactionServiceImpl.saveTransaction(transactionModel);
		return ResponseEntity.ok(TransactionModelMapper.toWalletTransactionModelFunc(walletTransaction));
	}


	private WalletAccount validateAndGetWalletAccount(@PathVariable String userId) {
		WalletAccount walletAccount = walletAccountServiceImpl.findByUserId(userId);
		if (walletAccount == null) {
			throw new ApiException(invalidUser, USER_NOT_FOUND);
		}
		return walletAccount;
	}

}
