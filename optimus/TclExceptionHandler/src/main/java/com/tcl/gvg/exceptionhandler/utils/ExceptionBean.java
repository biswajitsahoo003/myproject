package com.tcl.gvg.exceptionhandler.utils;

/**
 * 
 * @author MRajakum
 *
 */
public class ExceptionBean {
	
	private String message;
	
	private Throwable cause;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	
	

}
