package com.tcl.dias.serviceinventory.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service Inventory related Utils
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceInventoryUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryUtils.class);

	private ServiceInventoryUtils() {
		/* static usage */
	}

	/**
	 * Convert from object to json
	 *
	 * @param object
	 * @return String
	 */
	public static String toJson(Object object) {
		String json = null;
		try {
			json = Utils.convertObjectToJson(object);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return json;
	}

	/**
	 * Convert from json to object
	 *
	 * @param jsonStr
	 * @param valueType
	 * @param           <T>
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, TypeReference<T> valueType) {
		T object = null;
		try {
			object = new ObjectMapper().readValue(jsonStr, valueType);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return object;
	}

	/**
	 * Convert from json to object
	 *
	 * @param jsonStr
	 * @param valueType
	 * @param           <T>
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, Class<T> valueType) {
		T object = null;
		try {
			object = new ObjectMapper().readValue(jsonStr, valueType);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return object;
	}

	/**
	 * Convert output of a native query to a POJO object using specified mapper
	 *
	 * @param rowSupplier
	 * @param rowMapper
	 * @param             <T>
	 * @return
	 */
	public static <T> List<T> mapRows(Supplier<List<Map<String, Object>>> rowSupplier,
			Function<Map<String, Object>, T> rowMapper) {
		Objects.requireNonNull(rowSupplier, "Row supplying function cannot be null");
		Objects.requireNonNull(rowMapper, "Row mapper cannot be null");
		List<Map<String, Object>> rows = rowSupplier.get();
		if (Objects.isNull(rows)) {
			return ImmutableList.of();
		}
		return rows.stream().map(rowMapper::apply).collect(Collectors.toList());
	}

	public static TclCommonRuntimeException error(String code, String message) {
		return new TclCommonRuntimeException(code, new RuntimeException(message));
	}
	
	public static String dateConvertor(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
		return sdf.format(date);

	}
	
	public static Timestamp formatStringToDate(String inpValue) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date formattedAttributeValue = sdf.parse(inpValue);
			return new Timestamp(formattedAttributeValue.getTime());
		} catch (ParseException e) {
			LOGGER.error("Error while parsing date Value : {}", inpValue);
		}
		return null;
	}

	/**
	 *  This method is used for replacing null values.
	 *  
	 * @param <T>
	 * @param <K>
	 * @param map
	 * @param defaultValue
	 * @return
	 */
	public static <T, K> Map<K, T> replaceNullValues(Map<K, T> map, T defaultValue) {
		map = map.entrySet().stream().map(entry -> {
			if (entry.getValue() == null)
				entry.setValue(defaultValue);
			return entry;
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return map;
	}

	/**
	 * This method is used for checking not null.
	 * 
	 * @param strCheck
	 * @return
	 */
	public static boolean checkIfNotNull(String strCheck) {
		try {
			if (strCheck != null) {
				strCheck = strCheck.trim();
				if (!(strCheck.equalsIgnoreCase(""))) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
