package com.tcl.dias.validations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;

/**
 * Bean validation custom response handler
 * 
 * @author NAVEEN GUNASEKARAN
 *
 */
@ControllerAdvice
@RestController
public class OptimusResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Method to handle all the bean validation exceptions and return the custom
	 * response
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseResource<Object> handleAllExceptions(Exception ex, WebRequest request) {
		OptimusResponseErrors errorDetails = new OptimusResponseErrors(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseResource<>(ResponseResource.R_CODE_INTERNAL_SERVER_ERROR, ResponseResource.RES_FAILURE,
				errorDetails, Status.ERROR);
	}

	/*
	 * Overriding the handleMethodArgumentNotValid with the custom error response
	 * when the handleMethodArgumentNotValid bean validation failed.
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<OptimusResponseErrors> beanValidationErrors = new ArrayList<>();

		for (FieldError fr : ex.getBindingResult().getFieldErrors()) {
			OptimusResponseErrors errorDetails = new OptimusResponseErrors(new Date(), "Validation Failed",
					fr.getDefaultMessage());
			beanValidationErrors.add(errorDetails);
		}
		return new ResponseEntity<>(new ResponseResource<Object>(ResponseResource.R_CODE_BAD_REQUEST,
				ResponseResource.RES_FAILURE, beanValidationErrors, Status.ERROR), HttpStatus.BAD_REQUEST);
	}

}