package com.tcl.dias.oms.gsc.exception;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * To handle TCL validation error This class will be removed , please replace it
 * with {@link TclCommonRuntimeException}
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Deprecated
public class TCLValidationError extends TCLError {

	private final List<String> errors;

	public TCLValidationError(String messageCode, String message) {
		super(messageCode, message);
		this.errors = ImmutableList.of(message);
	}

	public TCLValidationError(String messageCode, List<String> errors) {
		super(messageCode, "");
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}
}
