package com.leo.vegas.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity representing Wallet account
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletAccount extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@NonNull
	private BigDecimal availableBalance;

	@NonNull
	@Column(unique = true)
	private String userId;

	@NonNull
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "walletAccount")
	List<WalletTransaction> walletTransactions;

}
