package com.leo.vegas;

import com.leo.vegas.controller.TransactionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VegasApplicationTests {

	@Autowired
	private TransactionController transactionController;

	@Test
	void contextLoads() {
		assertThat(transactionController).isNotNull();
	}

}
