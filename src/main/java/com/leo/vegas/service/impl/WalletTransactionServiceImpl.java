package com.leo.vegas.service.impl;

import com.leo.vegas.entity.WalletAccount;
import com.leo.vegas.entity.WalletTransaction;
import com.leo.vegas.exception.ApiException;
import com.leo.vegas.model.TransactionModel;
import com.leo.vegas.repository.WalletAccountRepository;
import com.leo.vegas.repository.WalletTransactionRepository;
import com.leo.vegas.service.WalletTransactionService;
import com.leo.vegas.util.TransactionStatus;
import com.leo.vegas.util.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.leo.vegas.exception.ApiErrorMessageAndCode.TRANSACTION_ID_EXISTS;
import static com.leo.vegas.exception.ApiErrorMessageAndCode.invalidTransactionId;

@Service
@Slf4j
public class WalletTransactionServiceImpl implements WalletTransactionService {

	private final WalletTransactionRepository walletTransactionRepository;
	private final WalletAccountRepository walletAccountRepository;
	private final WalletAccountServiceImpl walletAccountServiceImpl;



	@Autowired
	public WalletTransactionServiceImpl(WalletTransactionRepository walletTransactionRepository,
	                                    WalletAccountRepository walletAccountRepository,
	                                    WalletAccountServiceImpl walletAccountServiceImpl) {
		this.walletTransactionRepository = walletTransactionRepository;
		this.walletAccountRepository = walletAccountRepository;
		this.walletAccountServiceImpl = walletAccountServiceImpl;
	}


	@Override
	public  WalletTransaction saveTransaction(TransactionModel transactionModel) {
		WalletAccount walletAccount = walletAccountRepository.findByUserId(transactionModel.getUserId());
		WalletTransaction transaction;
		synchronized (this) {
			boolean transactionIdExists = walletTransactionRepository.existsByTransactionId(transactionModel.getTransactionId());
			if (transactionIdExists) {
				throw new ApiException(invalidTransactionId, TRANSACTION_ID_EXISTS);
			}
			transaction = new WalletTransaction();
			transaction.setTransactionAmount(transactionModel.getAmount().abs());
			transaction.setWalletAccount(walletAccount);
			transaction.setTransactionStatus(TransactionStatus.INITIATED);
			transaction.setTransactionType(transactionModel.getTransactionType());
			transaction.setTransactionId(transactionModel.getTransactionId());
			walletTransactionRepository.save(transaction);
		}
		if (TransactionType.DEBIT.equals(transactionModel.getTransactionType())) {
			walletAccountServiceImpl.debitWalletAccount(walletAccount.getId(), transactionModel.getAmount(), transactionModel.getTransactionId());
		} else if (TransactionType.CREDIT.equals(transactionModel.getTransactionType())) {
			walletAccountServiceImpl.creditWalletAccount(walletAccount.getId(), transactionModel.getAmount(),
						transactionModel.getTransactionId());
		}
		transaction.setTransactionStatus(TransactionStatus.COMPLETED);
		walletTransactionRepository.save(transaction);
		return transaction;
	}

	@Override
	public List<WalletTransaction> findTransactionsByUserId(String userId) {
		return walletTransactionRepository.findByWalletAccountUserId(userId);
	}
}
