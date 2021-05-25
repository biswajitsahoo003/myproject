package com.tcl.dias.oms.teamsdr.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.constants.CommonConstants;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Objects;

/**
 * Utility class with generic methods for Teams DR product
 *
 * @author Srinivasa Raghavan
 */
public class TeamsDRUtils {

    /**
     * Check for null - if not null return the price, else return 0
     *
     * @param price
     * @return
     */
    public static double checkForNull(Double price) {
        if (Objects.nonNull(price))
            return price;
        else
            return 0.0;
    }

    /**
     * Calculate new price value
     *
     * @param change
     * @param value
     * @return
     */
    public static Double calculateNewValue(double change, Double value) {
        if (change < 0) {
            return (value + Math.abs(change));
        } else {
            return (value - Math.abs(change));
        }
    }

    /**
     * Load object from JSON file resource in classpath
     *
     * @param jsonFilePath
     * @param typeReference
     * @param               <T>
     * @return
     */
    public static <T> T fromJsonFile(String jsonFilePath, TypeReference<T> typeReference) {
        URL url = Resources.getResource(jsonFilePath);
        CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
        try {
            return new ObjectMapper().readValue(charSource.openStream(), typeReference);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return null;
    }

	/**
	 * Extract contract period months ("12 months") from string to integer
	 *
	 * @param contractTerm
	 * @return
	 */
	public static Integer extractContractPeriodFromString(String contractTerm) {
		Integer contractPeriod = Integer.parseInt(contractTerm.replaceAll("[^0-9]", CommonConstants.EMPTY));
		return contractPeriod;
	}

    /**
     * Check for null - if not null return the price, else return 0
     *
     * @param price
     * @return
     */
    public static BigDecimal checkForNull(BigDecimal price) {
        if (Objects.nonNull(price))
            return price;
        else
            return BigDecimal.ZERO;
	}

	/**
	 * Check for null - if not null return the price, else return 0
	 *
	 * @param price
	 * @return
	 */
	public static BigDecimal checkNullAndReturnPrice(String price) {
		if (Objects.nonNull(price))
			return new BigDecimal(price);
		else
			return BigDecimal.ZERO;
	}
}
