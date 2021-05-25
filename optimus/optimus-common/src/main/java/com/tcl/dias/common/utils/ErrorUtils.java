package com.tcl.dias.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.ResponseResource;

/**
 * 
 * @author Manojkumar R
 *
 */
public final class ErrorUtils {

	private ErrorUtils() {
	}

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static String constructResponse(int errorCode, String errorMessage, Status status)
			throws JsonProcessingException {
		ResponseResource<Object> response = new ResponseResource<>(errorCode, errorMessage, status);
		return OBJECT_MAPPER.writeValueAsString(response);
	}

}
