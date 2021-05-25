package com.tcl.gvg.exceptionhandler.engine;

import org.apache.commons.lang3.StringUtils;

import com.tcl.gvg.exceptionhandler.code.ExceptionCodesEnum;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.propertyhandler.PropertyHandler;
import com.tcl.gvg.exceptionhandler.utils.ExceptionBean;

/**
 * The {@code TclCheckedExceptionEngine} represents Checked Exceptions When the
 * Exception is thrown with cause and with or without messageCode ,
 * {@code TclCheckedExceptionEngine} processes cause and messageCode and sets
 * the message.
 *
 * @author MRajakum
 * @since JDK1.0
 */
public class TclCheckedExceptionEngine implements IExceptionEngine {

	@Override
	public void setNextEngine(IExceptionEngine exceptionEngine) {
		// Do Nothing
	}

	/**
	 * process- This method parse the stack trace and sets the exception according
	 * to the pattern.
	 */
	@Override
	public ExceptionBean process(String messageCode, Throwable cause) {
		ExceptionBean exceptionBean = new ExceptionBean();
		if (cause instanceof TclCommonException) {
			exceptionBean.setMessage(cause.getMessage());
			exceptionBean.setCause(cause.getCause());
			return exceptionBean;
		}
		String exceptionClassName = cause.getClass().getSimpleName();
		ExceptionCodesEnum exceptionEnum = ExceptionCodesEnum.getByClassName(exceptionClassName);
		String message = exceptionEnum != null ? exceptionEnum.getErrorCode()
				: (ExceptionCodesEnum.E_0XOCFFFF.getErrorCode());
		if (messageCode != null) {
			message = PropertyHandler.getProperty(messageCode);
		}
		if (!StringUtils.isNoneBlank(message)) {
			message = cause.getMessage();
		}
		exceptionBean.setCause(cause);
		exceptionBean.setMessage(message);
		return exceptionBean;
	}

}
