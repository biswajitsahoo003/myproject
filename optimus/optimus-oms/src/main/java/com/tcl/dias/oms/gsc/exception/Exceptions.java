package com.tcl.dias.oms.gsc.exception;

import java.util.List;

import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.vavr.control.Try;

/**
 * Exception class to handle all exception thrown / given by vavr library This
 * class will be removed , please replace it with
 * {@link TclCommonRuntimeException}
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Deprecated
public class Exceptions {

	private Exceptions() {
		/* static usage */
	}

	/**
	 * Validate error with message code and errors
	 *
	 * @param messageCode
	 * @param errors
	 * @return {@link TCLValidationError}
	 */
	public static TCLValidationError validationError(String messageCode, List<String> errors) {
		return new TCLValidationError(messageCode, errors);
	}

	/**
	 * Validate error with message code and error message
	 *
	 * @param messageCode
	 * @param message
	 * @return {@link TCLValidationError}
	 */
	public static TCLValidationError validationError(String messageCode, String message) {
		return new TCLValidationError(messageCode, message);
	}

	/**
	 * Wrapper functionality to wrap exceptions with message code and error message
	 *
	 * @param messageCode
	 * @param message
	 * @param cause
	 * @return {@link TCLException}
	 */
	public static TCLException wrapException(String messageCode, String message, Throwable cause) {
		return new TCLException(messageCode, message, cause);
	}

	/**
	 * Basic not found functionality
	 *
	 * @param objectId
	 * @param messageCode
	 * @return {@link ObjectNotFoundException}
	 */
	public static ObjectNotFoundException notFound(String objectId, String messageCode) {
		String errorMessage = String.format("Object with id: %s not found", objectId);
		return new ObjectNotFoundException(messageCode, errorMessage);
	}

	/**
	 * Basic not found functionality with message
	 *
	 * @param messageCode
	 * @param message
	 * @return {@link ObjectNotFoundException}
	 */
	public static ObjectNotFoundException notFoundWithMessage(String messageCode, String message) {
		return new ObjectNotFoundException(messageCode, message);
	}

	/**
	 * Not found error with Try clause
	 *
	 * @param messageCode
	 * @param message
	 * @param             <T>
	 * @return
	 */
	public static <T> Try<T> notFoundError(String messageCode, String message) {
		return Try.failure(notFoundWithMessage(messageCode, message));
	}

	/**
	 * Not found error with Try clause
	 *
	 * @param objectId
	 * @param messageCode
	 * @param             <T>
	 * @return
	 */
	public static <T> Try<T> notFoundByIdError(String objectId, String messageCode) {
		return Try.failure(notFound(objectId, messageCode));
	}
}
