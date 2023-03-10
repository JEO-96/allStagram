package com.cos.photogramstart.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.auth.CMRespDto;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(CustomValidationException.class)
	public String validationExeption(CustomValidationException e) {
		// CMRespDto, Script 비교
		// 1. 클라이언트에게 응답할 때는 Script 좋은.
		// 2. Ajax통신 - CMRespDto
		// 3. Android 통신 - CMRespDto
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		} else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	@ExceptionHandler(CustomException.class)
	public String exeption(CustomException e) {
		return Script.back(e.getMessage());
	}
	
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiExeption(CustomValidationApiException e) {
		return new ResponseEntity<CMRespDto<?>>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiExeption(CustomApiException e) {
		return new ResponseEntity<CMRespDto<?>>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	
}
