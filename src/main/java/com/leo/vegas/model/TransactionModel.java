package com.leo.vegas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leo.vegas.util.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * Wallet transaction request model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionModel {

	@NonNull
	private BigDecimal amount;
	@NonNull
	private String userId;

	@JsonIgnoreProperties
	private TransactionType transactionType;

	@NonNull
	@NotEmpty
	private String transactionId;
}
