package com.tcl.dias.oms.gsc.validation;

import io.vavr.control.Validation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Data validation for gsc quote related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DataValidation<T> {

    private List<Function<T, Validation<String, T>>> validations;

    public List<Function<T, Validation<String, T>>> getIterableValidations() {
        return validations;
    }

    public void setIterableValidations(List<Function<T, Validation<String, T>>> iterableValidations) {
        this.validations = iterableValidations;
    }

    public List<String> apply(T t) {
        return validations.stream().map(v -> v.apply(t)).filter(Validation::isInvalid).map(Validation::getError)
                .collect(Collectors.toList());
    }
}
