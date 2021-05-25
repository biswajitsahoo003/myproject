package com.tcl.gvg.exceptionhandler.engine;

import com.tcl.gvg.exceptionhandler.propertyhandler.PropertyHandler;
import com.tcl.gvg.exceptionhandler.utils.ExceptionBean;

/**
 * The {@code TclBusinessExceptionEngine} represents Business Exceptions When
 * the Exception is thrown only with a messageCode ,
 * {@code TclBusinessExceptionEngine} processes the messageCode and gets the
 * message from the property file and set the same.
 *
 * @author MRajakum
 * @since JDK1.0
 */
public class TclBusinessExceptionEngine implements IExceptionEngine {

	private IExceptionEngine exceptionEngine;

	@Override
	public void setNextEngine(IExceptionEngine exceptionEngine) {
		this.exceptionEngine = exceptionEngine;

	}

	/**
	 * process- This method gets the error message from the property file defined
	 * and sets the message accordingly.
	 */
	@Override
	public ExceptionBean process(String messageCode, Throwable cause) {
		ExceptionBean exceptionBean = new ExceptionBean();
		if (messageCode != null && cause == null) {
			String message = PropertyHandler.getProperty(messageCode);
			exceptionBean.setMessage(message);
			exceptionBean.setCause(new Exception(message));
		} else {
			exceptionBean = this.exceptionEngine.process(messageCode, cause);
		}
		return exceptionBean;
	}

}
