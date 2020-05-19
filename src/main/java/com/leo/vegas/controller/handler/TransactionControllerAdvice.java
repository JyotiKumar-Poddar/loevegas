package com.leo.vegas.controller.handler;

import com.leo.vegas.controller.TransactionController;
import com.leo.vegas.exception.ApiErrorMessageAndCode;
import com.leo.vegas.exception.ApiErrorResponse;
import com.leo.vegas.exception.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.leo.vegas.exception.ApiErrorMessageAndCode.internalServerError;
import static com.leo.vegas.exception.ApiErrorMessageAndCode.malformedRequest;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Advice to handle exception thrown in the controller and return appropriate response.
 */
@RestControllerAdvice(assignableTypes = {TransactionController.class})
public class TransactionControllerAdvice extends ResponseEntityExceptionHandler {


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiErrorResponse response = ApiErrorResponse.builder()
					.statusCode(status)
					.applicationErrorCode(ApiErrorMessageAndCode.BAD_DATA_001)
					.message(malformedRequest)
					.timeStamp(LocalDateTime.now())
					.detail(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField()).build();
		return new ResponseEntity<>(response, response.getStatusCode());
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiErrorResponse response = ApiErrorResponse.builder()
					.statusCode(status)
					.applicationErrorCode(ApiErrorMessageAndCode.BAD_DATA_001)
					.message(malformedRequest)
					.timeStamp(LocalDateTime.now())
					.build();
		return new ResponseEntity<>(response, response.getStatusCode());
	}

	@ExceptionHandler({ApiException.class})
	public ResponseEntity<Object> handleInValidRequestException(final ApiException ex) {
		ApiErrorResponse response = ApiErrorResponse.builder()
					.statusCode(BAD_REQUEST)
					.applicationErrorCode(ex.getApplicationErrorCode())
					.message(ex.getMessage())
					.timeStamp(LocalDateTime.now())
					.build();
		return new ResponseEntity<>(response, response.getStatusCode());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleConflict() {
		ApiErrorResponse response = ApiErrorResponse.builder()
					.statusCode(INTERNAL_SERVER_ERROR)
					.applicationErrorCode(ApiErrorMessageAndCode.INTERNAL_SERVER_ERROR)
					.message(internalServerError)
					.timeStamp(LocalDateTime.now())
					.build();
		return new ResponseEntity<>(response, response.getStatusCode());
	}
}
