package com.tcl.dias.oms.gsc.exception;

import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * To handle object not found exception
 * <p>
 * This class will be removed , please replace it with
 * {@link TclCommonRuntimeException}
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Deprecated
public class ObjectNotFoundException extends TCLException {

    public ObjectNotFoundException(String messageCode) {
        super(messageCode);
    }

    public ObjectNotFoundException(String messageCode, String message) {
        super(messageCode, message);
    }

    public ObjectNotFoundException(String messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
    }

    public ObjectNotFoundException(String messageCode, Throwable cause) {
        super(messageCode, cause);
    }
}
