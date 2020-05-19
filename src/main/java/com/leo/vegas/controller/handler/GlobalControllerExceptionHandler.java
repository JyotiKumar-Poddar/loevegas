package com.leo.vegas.controller.handler;

import com.leo.vegas.exception.ApiErrorMessageAndCode;
import com.leo.vegas.exception.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static com.leo.vegas.exception.ApiErrorMessageAndCode.internalServerError;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Global exception handler
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {


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
