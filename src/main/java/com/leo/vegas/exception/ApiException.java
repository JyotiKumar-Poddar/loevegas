package com.leo.vegas.exception;


import org.springframework.http.HttpStatus;

/**
 * All business related exception.
 */
public class ApiException extends RuntimeException {

	private String message;
	private String applicationErrorCode;
	private HttpStatus httpStatus;

	public ApiException(String message, String applicationErrorCode) {
		super(message);
		this.message = message;
		this.applicationErrorCode = applicationErrorCode;
	}


	public ApiException(String message, Throwable cause, String applicationErrorCode) {
		super(message, cause);
		this.message = message;
		this.applicationErrorCode = applicationErrorCode;
	}

	public ApiException(Throwable cause, String message, String applicationErrorCode) {
		super(cause);
		this.message = message;
		this.applicationErrorCode = applicationErrorCode;
	}

	public ApiException(String message, Throwable cause, boolean enableSuppression,
	                    boolean writableStackTrace, String applicationErrorCode) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
		this.applicationErrorCode = applicationErrorCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getApplicationErrorCode() {
		return applicationErrorCode;
	}

	public void setApplicationErrorCode(String applicationErrorCode) {
		this.applicationErrorCode = applicationErrorCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
