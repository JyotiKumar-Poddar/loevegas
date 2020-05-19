package com.leo.vegas.entity;

import com.leo.vegas.util.TransactionStatus;
import com.leo.vegas.util.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
			indexes = {
						@Index(name = "TRANSACTION_ID_ACCOUNT_ID_IDX", columnList = "transactionId")
			},
			uniqueConstraints = {
						@UniqueConstraint(
									columnNames = {"transactionid"},
									name = "unique_transactionId"
						)
			})
public class WalletTransaction extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@NonNull
	@Column(updatable = false)
	private String transactionId;

	@NonNull
	@Column(updatable = false)
	private BigDecimal transactionAmount;

	@Enumerated(EnumType.STRING)
	private TransactionStatus transactionStatus;

	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "walletAccount_id")
	public WalletAccount walletAccount;

}
