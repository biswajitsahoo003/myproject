package com.tcl.gvg.exceptionhandler.custom;

import com.tcl.gvg.exceptionhandler.base.BaseException;
import com.tcl.gvg.exceptionhandler.core.ExceptionHandler;

/**
 * The {@code TclCommonException} class represents a common exception handler
 * that could be used across projects to have consistent exception handling
 * mechanisms independent of layers including API.
 * <p>
 * The {@code TclCommonException} framework segregates Business Exception,
 * Checked Exception and Unchecked Exception and customizes the error message.
 * All the error messages will be placed under a property file, and the message
 * code would be passed with the TclCommonException. property file Configuration
 * will be done in Spring Configuration bean or XML.
 * 
 * {@code TclCommonException} extends {@code BaseException}
 *
 * @author MRajakum
 * @since JDK1.0
 */
public class TclCommonException extends BaseException {

	private int statusCode = 500;

	private static final long serialVersionUID = -6188159661303813974L;

	// For Business
	public TclCommonException(String messageCode) {
		super(new ExceptionHandler(messageCode, null));
	}

	// For Checked and Unchecked
	public TclCommonException(String messageCode, Throwable cause) {
		super(new ExceptionHandler(messageCode, cause));
	}

	// For Checked and Unchecked
	public TclCommonException(Throwable cause) {
		super(new ExceptionHandler(null, cause));
	}

	// For Business
	public TclCommonException(String messageCode, int statusCode) {
		super(new ExceptionHandler(messageCode, null));
		this.statusCode = statusCode != 0 ? statusCode : this.statusCode;
	}

	// For Checked and Unchecked
	public TclCommonException(Throwable cause, int statusCode) {
		super(new ExceptionHandler(null, cause));
		this.statusCode = statusCode != 0 ? statusCode : this.statusCode;
	}

	// For Checked and Unchecked
	public TclCommonException(String messageCode, Throwable cause, int statusCode) {
		super(new ExceptionHandler(messageCode, cause));
		this.statusCode = statusCode != 0 ? statusCode : this.statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
