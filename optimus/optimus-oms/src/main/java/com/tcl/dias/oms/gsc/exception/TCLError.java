package com.tcl.dias.oms.gsc.exception;

import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * To handle TCL error This class will be removed , please replace it with
 * {@link TclCommonRuntimeException}
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Deprecated
public class TCLError extends Error {
	private final String messageCode;

	public TCLError(String messageCode, String message) {
		super(message);
		this.messageCode = messageCode;
	}

	public TCLError(String messageCode, String message, Throwable cause) {
		super(message, cause);
		this.messageCode = messageCode;
	}

	public TCLError(String messageCode, Throwable cause) {
		super(cause);
		this.messageCode = messageCode;
	}

	public String messageCode() {
		return messageCode;
	}
}
