package com.tcl.gvg.exceptionhandler.base;

import com.tcl.gvg.exceptionhandler.core.ExceptionHandler;

/**
 * The {@code BaseException} class extends Exception 
 * that could be used as the base for the {@code TclCommonException}} 
 * <p>
 * The {@code BaseException} accepts {@code ExceptionHandler} as an argument for the one-argument constructor.
 * {@code ExceptionHandler} gives the custom message and exception trace.
 * 
 * {@code TclCommonException} extends {@code Exception}
 *
 * @author MRajakum
 * @since   JDK1.0
 */
public class BaseRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 6323819350253711395L;
	
    public BaseRuntimeException(String message) {
        super(message);
    }
    
    public BaseRuntimeException(ExceptionHandler handler) {
        super(handler.getExceptionBean().getMessage(),handler.getExceptionBean().getCause());
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }
	

}
