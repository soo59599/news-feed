package org.nfactorial.newsfeed.common.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<GlobalApiResponse<?>> businessExceptionHandler(BusinessException e) {
		ErrorCode errorCode = e.getErrorCode();
		GlobalApiResponse<?> response = GlobalApiResponse.builder()
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GlobalApiResponse<?>> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
		String message = e.getBindingResult().getFieldErrors().stream()
			.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
			.collect(Collectors.joining(", "));
		if (message.isBlank()) {
			message = "유효성 검증에 실패했습니다.";
		}
		GlobalApiResponse<?> response = GlobalApiResponse.builder()
			.code("VALIDATION_ERROR")
			.message(message)
			.build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<GlobalApiResponse<?>> bindExceptionHandler(BindException e) {
		String message = e.getBindingResult().getFieldErrors().stream()
			.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
			.collect(Collectors.joining(", "));
		if (message.isBlank()) {
			message = "요청 파라미터 바인딩에 실패했습니다.";
		}
		GlobalApiResponse<?> response = GlobalApiResponse.builder()
			.code("VALIDATION_ERROR")
			.message(message)
			.build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<GlobalApiResponse<?>> maxSizeExceptionHandler (MaxUploadSizeExceededException e) {
		GlobalApiResponse<?> response = GlobalApiResponse.builder()
			.code("FILE_SIZE_EXCEEDED")
			.message("파일 크기가 10MB를 초과했습니다. 더 작은 파일을 업로드해주세요.")
			.build();
		return ResponseEntity.badRequest().body(response);
	}
}
