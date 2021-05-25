package com.tcl.gvg.exceptionhandler.engine;

import com.tcl.gvg.exceptionhandler.utils.ExceptionBean;


/**
 * The {@code IExceptionEngine} interface is used as the reference for the Business,Checked
 * and Unchecked Exception Engine.
 * 
 * <p>
 * The {@code IExceptionEngine} implements {@code TclBusinessExceptionEngine},
 * {@code TclCheckedExceptionEngine} and {@code TclUncheckedExceptionEngine}
 * 
 *
 * @author MRajakum
 * @since   JDK1.0
 */
public interface IExceptionEngine {

	public void setNextEngine(IExceptionEngine exceptionEngine);

	public ExceptionBean process(String messageCode,Throwable cause);

}
