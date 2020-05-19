package com.leo.vegas.model;

import com.leo.vegas.util.TransactionStatus;
import com.leo.vegas.util.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletTransactionModel {

	private String transactionId;
	private BigDecimal transactionAmount;
	private TransactionStatus transactionStatus;
	private TransactionType transactionType;

}
