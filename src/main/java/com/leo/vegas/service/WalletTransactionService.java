package com.leo.vegas.service;

import com.leo.vegas.entity.WalletTransaction;
import com.leo.vegas.model.TransactionModel;

import java.util.List;

public interface WalletTransactionService {

	WalletTransaction saveTransaction(TransactionModel transactionModel);

	List<WalletTransaction> findTransactionsByUserId(String userId);
}
