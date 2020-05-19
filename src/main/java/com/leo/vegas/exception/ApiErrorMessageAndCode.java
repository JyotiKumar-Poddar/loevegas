package com.leo.vegas.exception;

/**
 * Api Error code and exception message
 */
public interface ApiErrorMessageAndCode {

	String BAD_DATA_001 = "BAD_DATA_001";

	String USER_NOT_FOUND = "USER_NOT_FOUND";

	String TRANSACTION_ID_EXISTS = "TRANSACTION_ID_EXISTS";

	String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR_000";

	String BALANCE_ERROR_001 = "BALANCE_ERROR_001";


	String internalServerError = "Internal server error";
	String invalidUser = "Invalid User";
	String malformedRequest = "Malformed JSON request";
	String invalidTransactionId = "Transaction id already exists";
	String insufficientBalance = "Insufficient Amount";

}
