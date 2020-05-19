package com.leo.vegas;

import com.leo.vegas.entity.WalletAccount;
import com.leo.vegas.repository.WalletAccountRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;


@SpringBootApplication
public class VegasApplication {


	public static void main(String[] args) {
		SpringApplication.run(VegasApplication.class, args);
	}


	@Bean
	ApplicationRunner initWallet(WalletAccountRepository repository) {
		WalletAccount walletAccount = repository.findByUserId("user1@gmail.com");
		if (walletAccount == null) {
			walletAccount = new WalletAccount();
			walletAccount.setAvailableBalance(BigDecimal.ZERO);
			walletAccount.setUserId("user1@gmail.com");
			repository.save(walletAccount);
		}
		return null;
	}

}
