package com.tcl.dias.oms.gsc.validation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.vavr.control.Validation;

/**
 * Data validator for gsc quote related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DataValidator {

	private DataValidator() {
		/* static usage */
	}

	public static <T> List<String> validate(T t, Function<T, Validation<String, T>>... validations) {
		return Arrays.stream(validations).map(v -> v.apply(t)).filter(Validation::isInvalid).map(Validation::getError)
				.collect(Collectors.toList());
	}
}
