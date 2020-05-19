package com.leo.vegas.model;

import com.leo.vegas.util.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionModel {

	@NonNull
	private BigDecimal amount;
	@NonNull
	private String userId;

	private TransactionType transactionType;

	@NonNull
	@NotEmpty
	private String transactionId;
}
