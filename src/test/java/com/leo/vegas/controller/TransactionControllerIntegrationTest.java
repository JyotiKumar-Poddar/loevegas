package com.leo.vegas.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.leo.vegas.entity.WalletAccount;
import com.leo.vegas.model.TransactionModel;
import com.leo.vegas.model.WalletAccountModel;
import com.leo.vegas.model.WalletTransactionModel;
import com.leo.vegas.repository.WalletAccountRepository;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Integration test to make sure end to end flow is working as expected multi threaded environment.
 */


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransactionControllerIntegrationTest {

	private static final Logger log = LoggerFactory.getLogger(TransactionControllerIntegrationTest.class);

	@LocalServerPort
	private int port;

	@Autowired
	private WalletAccountRepository walletAccountRepository;

	@Test
	public void integration_test_walletAccount() throws InterruptedException {


		OkHttpClient client = new OkHttpClient();
		HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:" + port + "/credit/").newBuilder();
		String url = urlBuilder.build().toString();
		ObjectMapper mapper = new ObjectMapper();
		class Task implements Runnable {
			@Override
			public void run() {
				try {
					TransactionModel wt = new TransactionModel();
					wt.setTransactionId(String.valueOf(Instant.now().toEpochMilli()));
					wt.setAmount(BigDecimal.valueOf(10));
					wt.setUserId("user1@gmail.com");
					String json = mapper.writeValueAsString(wt);
					RequestBody body = RequestBody.create(
								MediaType.parse("application/json"), json);

					Request request = new Request.Builder()
								.url(url)
								.post(body)
								.build();
					Response response = client.newCall(request).execute();
					WalletTransactionModel walletTransactionModel = mapper.readValue(response.body().string(), WalletTransactionModel.class);
					WalletAccount walletAccount = walletAccountRepository.findByUserId("user1@gmail.com");
					log.info("================ {}", walletAccount.getAvailableBalance());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error in processing request");
				}

			}
		}


		ExecutorService executor = Executors.newFixedThreadPool(20);

		for (int i = 0; i < 10; i++) {
			Runnable worker = new Task();
			/** wait time before creating new thread */
			TimeUnit.MILLISECONDS.sleep(1000);
			executor.execute(worker);
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}

		HttpUrl.Builder urlGetBuilder = HttpUrl.parse("http://localhost:" + port + "/account/user1@gmail.com").newBuilder();
		String getUrl = urlGetBuilder.build().toString();
		Request request = new Request.Builder()
					.url(getUrl)
					.build();
		WalletAccountModel walletAccount = null;
		Response response = null;
		try {
			response = client.newCall(request).execute();
			System.out.println(response.body());
			walletAccount = mapper.readValue(response.body().string(), WalletAccountModel.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assertions.assertEquals(BigDecimal.valueOf(100).intValue(), walletAccount.getAmount().intValue());

	}
}

