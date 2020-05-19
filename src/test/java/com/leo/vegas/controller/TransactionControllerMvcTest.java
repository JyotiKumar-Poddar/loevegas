package com.leo.vegas.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leo.vegas.entity.WalletAccount;
import com.leo.vegas.entity.WalletTransaction;
import com.leo.vegas.model.TransactionModel;
import com.leo.vegas.model.WalletTransactionModel;
import com.leo.vegas.service.impl.WalletAccountServiceImpl;
import com.leo.vegas.service.impl.WalletTransactionServiceImpl;
import com.leo.vegas.util.TransactionStatus;
import com.leo.vegas.util.TransactionType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ApplicationRunner applicationRunner;

	@MockBean
	private WalletAccountServiceImpl walletAccountServiceImpl;

	@MockBean
	private WalletTransactionServiceImpl walletTransactionServiceImpl;

	final ObjectMapper objectMapper = new ObjectMapper();
	private WalletAccount walletAccount;
	private WalletTransaction walletTransaction;


	@BeforeEach
	public void setup() {
		walletAccount = new WalletAccount();
		walletAccount.setUserId("user1@gmail.com");

		walletTransaction = new WalletTransaction();
		walletTransaction.setTransactionAmount(BigDecimal.valueOf(200));
	}


	@Test
	public void shouldReturnWalletAccount() throws Exception {

		when(walletAccountServiceImpl.findByUserId(anyString())).thenReturn(walletAccount);

		this.mockMvc.perform(get("/account/user1@gmail.com")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("user1@gmail.com")));

	}

	@Test
	public void shouldReturnWalletTransaction() throws Exception {

		when(walletAccountServiceImpl.findByUserId(anyString())).thenReturn(walletAccount);
		walletTransaction.setWalletAccount(walletAccount);

		WalletTransactionModel walletTransactionModel = new WalletTransactionModel();
		walletTransactionModel.setTransactionAmount(BigDecimal.valueOf(200));
		when(walletTransactionServiceImpl.findTransactionsByUserId(anyString()))
					.thenReturn(Lists.newArrayList(walletTransaction));
		MvcResult mvcResult = this.mockMvc.perform(get("/transaction/user1@gmail.com")).andDo(print())
					.andExpect(status().isOk()).andReturn();

		List<WalletTransactionModel> walletTransactionModels = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<WalletTransactionModel>>() {
		});

		Assert.isTrue(walletTransactionModels.size() == 1);
		WalletTransactionModel walletTransactionsResult = walletTransactionModels.get(0);
		Assert.isTrue(walletTransactionsResult.getTransactionAmount().compareTo(walletTransactionModel.getTransactionAmount()) == 0);

	}

	@Test
	public void shouldReturnUpdateWalletOnDebitTransaction() throws Exception {

		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setUserId("user1@gmail.com");
		transactionModel.setTransactionId("19909001A3");
		transactionModel.setAmount(BigDecimal.valueOf(199));
		transactionModel.setTransactionType(TransactionType.DEBIT);

		WalletTransaction walletTransactionUpdate = new WalletTransaction();
		walletTransactionUpdate.setTransactionId(transactionModel.getTransactionId());
		walletTransactionUpdate.setTransactionAmount(transactionModel.getAmount());
		walletTransactionUpdate.setTransactionStatus(TransactionStatus.COMPLETED);
		walletTransactionUpdate.setTransactionType(TransactionType.DEBIT);


		when(walletAccountServiceImpl.findByUserId(anyString())).thenReturn(walletAccount);
		when(walletTransactionServiceImpl.saveTransaction(transactionModel))
					.thenReturn(walletTransactionUpdate);

		MvcResult mvcResult = this.mockMvc.perform(post("/debit")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(transactionModel)))
					.andExpect(status().isOk()).andReturn();

		WalletTransactionModel walletTransactionModel = objectMapper.readValue(mvcResult.getResponse()
					.getContentAsString(), WalletTransactionModel.class);

		Assert.isTrue(walletTransactionModel.getTransactionStatus().equals(TransactionStatus.COMPLETED));
		Assert.isTrue(walletTransactionModel.getTransactionAmount().equals(transactionModel.getAmount()));
	}

	@Test
	public void shouldReturnUpdateWalletOnCreditTransaction() throws Exception {
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setUserId("user1@gmail.com");
		transactionModel.setTransactionId("19909001A3");
		transactionModel.setAmount(BigDecimal.valueOf(100));
		transactionModel.setTransactionType(TransactionType.CREDIT);

		WalletTransaction walletTransactionUpdate = new WalletTransaction();
		walletTransactionUpdate.setTransactionId(transactionModel.getTransactionId());
		walletTransactionUpdate.setTransactionAmount(transactionModel.getAmount());
		walletTransactionUpdate.setTransactionStatus(TransactionStatus.COMPLETED);
		walletTransactionUpdate.setTransactionType(TransactionType.CREDIT);


		when(walletAccountServiceImpl.findByUserId(anyString())).thenReturn(walletAccount);
		when(walletTransactionServiceImpl.saveTransaction(transactionModel))
					.thenReturn(walletTransactionUpdate);

		MvcResult mvcResult = this.mockMvc.perform(post("/credit")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(transactionModel)))
					.andExpect(status().isOk()).andReturn();

		WalletTransactionModel walletTransactionModel = objectMapper.readValue(mvcResult.getResponse()
					.getContentAsString(), WalletTransactionModel.class);

		Assert.isTrue(walletTransactionModel.getTransactionStatus().equals(TransactionStatus.COMPLETED));
		Assert.isTrue(walletTransactionModel.getTransactionAmount().equals(transactionModel.getAmount()));

	}

}
