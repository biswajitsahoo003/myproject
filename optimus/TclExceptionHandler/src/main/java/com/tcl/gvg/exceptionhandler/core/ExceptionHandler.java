package com.tcl.gvg.exceptionhandler.core;

import com.tcl.gvg.exceptionhandler.engine.IExceptionEngine;
import com.tcl.gvg.exceptionhandler.engine.TclBusinessExceptionEngine;
import com.tcl.gvg.exceptionhandler.engine.TclCheckedExceptionEngine;
import com.tcl.gvg.exceptionhandler.engine.TclUncheckedExceptionEngine;
import com.tcl.gvg.exceptionhandler.utils.ExceptionBean;

/**
 * The {@code ExceptionHandler} class acts as the core component for 
 * the Exception framework, {@code ExceptionHandler} pass the messageCode and cause 
 * through the Business,Checked and Unchecked Exception Engine.
 * 
 * <p>
 * The {@code ExceptionHandler} sets the {@code ExceptionBean} which would be further passed 
 * to the {@code BaseException}
 * 
 *
 * @author MRajakum
 * @since   JDK1.0
 */
public class ExceptionHandler {

	private ExceptionBean exceptionBean;

	/**
	 * ExceptionHandler- Constructor constructs the Exception Engines and routes the request thru various engines and get the ExceptionBean.
	 * @param messageCode
	 * @param cause
	 */
	public ExceptionHandler(String messageCode,Throwable cause) {
		IExceptionEngine baseExceptionEngine=new TclBusinessExceptionEngine();
		IExceptionEngine knownExceptionEngine=new TclCheckedExceptionEngine();
		IExceptionEngine systemExceptionEngine=new TclUncheckedExceptionEngine();
		baseExceptionEngine.setNextEngine(systemExceptionEngine);
		systemExceptionEngine.setNextEngine(knownExceptionEngine);
		setExceptionBean(baseExceptionEngine.process(messageCode,cause));
	}

	public ExceptionBean getExceptionBean() {
		return exceptionBean;
	}

	public void setExceptionBean(ExceptionBean exceptionBean) {
		this.exceptionBean = exceptionBean;
	}




	
	

}
