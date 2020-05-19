package com.leo.vegas.model.mapper;

import com.leo.vegas.entity.WalletAccount;
import com.leo.vegas.entity.WalletTransaction;
import com.leo.vegas.model.WalletAccountModel;
import com.leo.vegas.model.WalletTransactionModel;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface TransactionModelMapper {

	Function<WalletAccount, WalletAccountModel> toWalletModelFunc = walletAccount -> {
		WalletAccountModel walletAccountModel = new WalletAccountModel();
		walletAccountModel.setAmount(walletAccount.getAvailableBalance());
		walletAccountModel.setUserId(walletAccount.getUserId());
		walletAccountModel.setLastUpdate(walletAccount.getUpdated());
		return walletAccountModel;
	};


	Function<WalletTransaction, WalletTransactionModel> toWalletTransactionModelFunc = walletTransaction -> {
		WalletTransactionModel walletTransactionModel = new WalletTransactionModel();
		walletTransactionModel.setTransactionAmount(walletTransaction.getTransactionAmount());
		walletTransactionModel.setTransactionId(walletTransaction.getTransactionId());
		walletTransactionModel.setTransactionStatus(walletTransaction.getTransactionStatus());
		walletTransactionModel.setTransactionType(walletTransaction.getTransactionType());
		return walletTransactionModel;
	};

	static WalletAccountModel toWalletModelFunc(WalletAccount walletAccount) {
		return toWalletModelFunc.apply(walletAccount);
	}

	static List<WalletTransactionModel> toWalletTransactionsModelFunc(List<WalletTransaction> walletTransactions) {
		return walletTransactions.stream().map(toWalletTransactionModelFunc).collect(Collectors.toList());
	}

	static WalletTransactionModel toWalletTransactionModelFunc(WalletTransaction walletTransaction) {
		return toWalletTransactionModelFunc.apply(walletTransaction);
	}

}
