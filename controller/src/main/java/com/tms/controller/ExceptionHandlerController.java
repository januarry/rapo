package com.tms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tms.api.model.Error;
import com.tms.api.model.ErrorDetail;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(Exception.class)
public final ResponseEntity<Error> handleAllExceptions(Exception ex, WebRequest request,HttpServletRequest httpRequest) {
	Error error = new Error();
	String httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.toString();
	error.setCode(httpStatus);
	error.setMessage(ex.getMessage());
	
	
	
	List<ErrorDetail> errorDetailList = new ArrayList<>();
	ErrorDetail errorDetail = new ErrorDetail();
	  errorDetail.setCode(httpStatus);
	  errorDetail.setMessage(ex.getMessage());
	  errorDetailList.add(errorDetail);
	  error.setDetails(errorDetailList);

	  
  return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
}
	

}
