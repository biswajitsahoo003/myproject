package com.tcl.gvg.exceptionhandler.engine;

import com.tcl.gvg.exceptionhandler.code.ExceptionCodesEnum;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.propertyhandler.PropertyHandler;
import com.tcl.gvg.exceptionhandler.utils.ExceptionBean;

/**
 * The {@code TclUncheckedExceptionEngine} represents Unchecked Exceptions When
 * the Exception is thrown with cause and with or without messageCode ,
 * {@code TclUncheckedExceptionEngine} processes cause by checking whether the
 * Exception is a Runtime exception or not, if it is a unchecked Exception A
 * common System Error exception message is thrown.
 *
 * @author MRajakum
 * @since JDK1.0
 */
public class TclUncheckedExceptionEngine implements IExceptionEngine {

	private IExceptionEngine exceptionEngine;

	@Override
	public void setNextEngine(IExceptionEngine exceptionEngine) {
		this.exceptionEngine = exceptionEngine;

	}

	/**
	 * process - This method defines the common error message for unknown error
	 * messages
	 */
	public ExceptionBean process(String messageCode, Throwable cause) {
		ExceptionBean exceptionBean = new ExceptionBean();
		if (cause instanceof TclCommonException) {
			exceptionBean.setMessage(cause.getMessage());
			exceptionBean.setCause(cause.getCause());
			return exceptionBean;
		}
		if (cause instanceof RuntimeException) {
			String exceptionClassName = cause.getClass().getSimpleName();
			ExceptionCodesEnum exceptionEnum = ExceptionCodesEnum.getByClassName(exceptionClassName);
			String message = exceptionEnum != null ? exceptionEnum.getErrorCode()
					: (ExceptionCodesEnum.E_0XOCFFFF.getErrorCode());
			if (messageCode != null) {
				message = PropertyHandler.getProperty(messageCode);
			}
			exceptionBean.setMessage(message);
			exceptionBean.setCause(cause);
		} else {
			exceptionBean = this.exceptionEngine.process(messageCode, cause);
		}
		return exceptionBean;
	}

}
