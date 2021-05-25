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
public class BaseException extends Exception{

	private static final long serialVersionUID = 6323819350253711395L;
	
    public BaseException(String message) {
        super(message);
    }
    
    public BaseException(ExceptionHandler handler) {
        super(handler.getExceptionBean().getMessage(),handler.getExceptionBean().getCause());
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
	

}
