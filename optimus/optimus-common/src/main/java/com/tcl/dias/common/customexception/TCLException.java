package com.tcl.dias.common.customexception;

import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * To handle TCL exception
 * 
 * This class will be removed , please replace it with {@link TclCommonRuntimeException}
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Deprecated
public class TCLException extends RuntimeException {

    private final String messageCode;

    public TCLException(String messageCode) {
        this.messageCode = messageCode;
    }

    public TCLException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }

    public TCLException(String messageCode, String message, Throwable cause) {
        super(message, cause);
        this.messageCode = messageCode;
    }

    public TCLException(String messageCode, Throwable cause) {
        super(cause);
        this.messageCode = messageCode;
    }

    public String messageCode() {
        return messageCode;
    }
}
