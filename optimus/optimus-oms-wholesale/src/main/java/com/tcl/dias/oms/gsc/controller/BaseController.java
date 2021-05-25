package com.tcl.dias.oms.gsc.controller;

import com.google.common.collect.ImmutableMap;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gsc.exception.ObjectNotFoundException;
import com.tcl.dias.oms.gsc.exception.TCLError;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.exception.TCLValidationError;
import com.tcl.dias.oms.gsc.util.GscConstants;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * Base controller class for all public APIs
 * <p>
 * Please use the existing framework used in other products
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Deprecated
public class BaseController {

    protected static final Properties EXCEPTION_PROPERTIES = loadExceptionProperties();

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    /**
     * Load exception properties
     *
     * @return {@link Properties}
     */
    private static Properties loadExceptionProperties() {
        InputStream inputStream = null;
        Properties property = new Properties();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            inputStream = classloader.getResourceAsStream("exception_messages.properties");
            property.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "Error while loading the properties file. Properties file are not available: {}");
        }
        return property;
    }

    /**
     * To error map
     *
     * @param t
     * @param errorId
     * @param responseCode
     * @param errorCode
     * @return Map<String, Object>
     */
    private Map<String, Object> toErrorMap(Throwable t, String errorId, Integer responseCode, String errorCode) {
        return ImmutableMap.of("message", EXCEPTION_PROPERTIES.getProperty(errorCode, errorCode), "responseCode",
                responseCode, "status", "FAILURE", "errorId", errorId, "errorMessage",
                t.getMessage() == null ? "" : t.getMessage());
    }

    /**
     * Handle error functionality
     *
     * @param t
     * @return Map<String, Object>
     */
    protected ResponseEntity<Map<String, Object>> error(Throwable t) {
        String errorId = UUID.randomUUID().toString();
        Map<String, Object> errorBody = null;
        if (t instanceof ObjectNotFoundException) {
            errorBody = toErrorMap(t, errorId, ResponseResource.R_CODE_NOT_FOUND,
                    ((ObjectNotFoundException) t).messageCode());
        } else if (t instanceof TCLValidationError) {
            errorBody = toErrorMap(t, errorId, ResponseResource.R_CODE_BAD_REQUEST,
                    ((TCLValidationError) t).messageCode());
            errorBody = new HashMap<>(errorBody);
            errorBody.put("errorMessage", ((TCLValidationError) t).getErrors());
        } else if (t instanceof TCLException) {
            errorBody = toErrorMap(t, errorId, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR,
                    ((TCLException) t).messageCode());
        } else {
            errorBody = toErrorMap(t, errorId, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR, "");
        }
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(String.format("Error occurred with ID: %s ", errorId), t);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    /**
     * Handle success body
     *
     * @param value
     * @return
     */
    private <T> Map<String, Object> toSuccessBody(T value) {
        return ImmutableMap.of("responseCode", ResponseResource.R_CODE_OK, "message", "OK", "status", "SUCCESS", "data",
                value);
    }

    /**
     * Handle sucess response
     *
     * @param value
     * @return
     */
    protected <T> ResponseEntity<Map<String, Object>> success(T value) {
        return ResponseEntity.ok(toSuccessBody(value));
    }

    /**
     * To validate action
     *
     * @param action
     * @return
     */
    protected Try<String> validateAction(String action) {
        if (!GscConstants.ACTION_CREATE.equals(action) && !GscConstants.ACTION_DELETE.equals(action)
                && !GscConstants.ACTION_UPDATE.equals(action)
                && !GscConstants.ACTION_APPROVE.equalsIgnoreCase(action)) {
            return Try.failure(
                    new TCLError(ExceptionConstants.COMMON_ERROR, String.format("Unknown action type: %s", action)));
        }
        return Try.success(action);
    }
}
