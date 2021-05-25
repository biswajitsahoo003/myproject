package com.tcl.dias.oms.gsc.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.google.common.primitives.Ints;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.tcl.dias.common.utils.Utils;
import io.vavr.control.Try;
import io.vavr.control.Validation;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Utils for Gsc related products
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscUtils {

    private GscUtils() {
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
     * @param <T>
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
     * Validates a given phone number
     *
     * @param phoneNumber - expected string in format "{ISD Code} {Number}" e.g. "91
     *                    9840480654"
     */
    public static Validation<String, String> validatePhoneNumber(String phoneNumber) {
        String[] numberFragments = phoneNumber.split("\\s+");
        Integer countryCode = Ints.tryParse(numberFragments[0]);
        if (Objects.isNull(countryCode)) {
            return Validation.invalid(String.format("Invalid country code in number %s", phoneNumber));
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String regionCode = phoneUtil.getRegionCodeForCountryCode(countryCode);
        if (Objects.isNull(regionCode)) {
            return Validation.invalid(String.format("Unknown country code %s", countryCode));
        }
        StringBuilder phoneNumberPart = new StringBuilder();
        for (int i = 1; i < numberFragments.length; i++) {
            phoneNumberPart.append(numberFragments[i]).append(" ");
        }
        return Try.success(phoneNumberPart).mapTry(phoneNumberPrt -> phoneUtil.parse(phoneNumberPrt, regionCode))
                .map(phoneNumbr -> {
                    if (phoneUtil.isValidNumber(phoneNumbr)) {
                        return Validation.<String, String>valid(phoneNumber);
                    } else {
                        return Validation.<String, String>invalid(
                                String.format("Invalid phone number %s for region %s", phoneNumberPart, regionCode));
                    }
                }).getOrElseGet(t -> Validation
                        .invalid(String.format("Invalid phone number %s, error: %s", phoneNumber, t.getMessage())));
    }

    /**
     * Load object from JSON file resource in classpath
     *
     * @param jsonFilePath
     * @param typeReference
     * @param <T>
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
}
