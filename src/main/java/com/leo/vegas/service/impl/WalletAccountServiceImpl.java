package com.leo.vegas.service.impl;

import com.leo.vegas.entity.WalletAccount;
import com.leo.vegas.exception.ApiException;
import com.leo.vegas.repository.WalletAccountRepository;
import com.leo.vegas.service.WalletAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.leo.vegas.exception.ApiErrorMessageAndCode.BALANCE_ERROR_001;
import static com.leo.vegas.exception.ApiErrorMessageAndCode.insufficientBalance;

/**
 * Service that gives helpful method to interact with wallet account.
 */
@Service
@Slf4j
public class WalletAccountServiceImpl implements WalletAccountService {

	private final WalletAccountRepository walletAccountRepository;
	private final Lock lock = new ReentrantLock();

	@Autowired
	public WalletAccountServiceImpl(WalletAccountRepository walletAccountRepository) {
		this.walletAccountRepository = walletAccountRepository;
	}

	@Override
	public void debitWalletAccount(Long accountId, BigDecimal amount, String transactionId) {
		lock.lock();
		try {
			Optional<WalletAccount> walletAccount = walletAccountRepository.findById(accountId);
			if (walletAccount.isPresent()) {
				WalletAccount beforeUpdate = walletAccount.get();
				BigDecimal afterDebitAmount = beforeUpdate.getAvailableBalance().subtract(amount);
				if (afterDebitAmount.compareTo(BigDecimal.ZERO) >= 0) {
					BigDecimal updatedBalance = beforeUpdate.getAvailableBalance().subtract(amount);
					beforeUpdate.setAvailableBalance(updatedBalance);
					walletAccountRepository.save(beforeUpdate);
				} else {
					log.warn("Insufficient fund to make transaction for transaction id {}", transactionId);
					throw new ApiException(insufficientBalance, BALANCE_ERROR_001);
				}
			}
		} finally {
			lock.unlock();
		}
	}


	@Override
	public void creditWalletAccount(final Long accountId, final BigDecimal amount, final String transactionId) {
		lock.lock();
		try {
			Optional<WalletAccount> walletAccount = walletAccountRepository.findById(accountId);
			if (walletAccount.isPresent()) {
				WalletAccount beforeUpdate = walletAccount.get();
				log.info("Before Account balance updated to {}  for transaction id {} thread name {}", beforeUpdate.getAvailableBalance(),
							transactionId, Thread.currentThread().getName());
				BigDecimal updatedBalance = beforeUpdate.getAvailableBalance().add(amount);
				beforeUpdate.setAvailableBalance(updatedBalance);
				WalletAccount updateWalletAccount = walletAccountRepository.save(beforeUpdate);
				log.info("Account balance updated to {}  for transaction id {}", updateWalletAccount.getAvailableBalance(), transactionId);
			}
		}finally {
			lock.unlock();
		}
	}

	@Override
	public WalletAccount findByUserId(String userId) {
		return walletAccountRepository.findByUserId(userId);
	}
}
