package com.tcl.dias.oms.gsc.validation;

import io.vavr.control.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List validator for gsc quote related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IterableValidation<T> extends DataValidation<T> {

    private List<Function<T, Validation<String, T>>> iterableValidations;

    @Override
    public List<Function<T, Validation<String, T>>> getIterableValidations() {
        return iterableValidations;
    }

    @Override
    public void setIterableValidations(List<Function<T, Validation<String, T>>> iterableValidations) {
        this.iterableValidations = iterableValidations;
    }

    public List<String> apply(Iterable<T> values) {
        final AtomicInteger index = new AtomicInteger(0);
        List<List<String>> messageBuffer = new ArrayList<>();
        for (T t : values) {
            List<String> messages = iterableValidations.stream().map(v -> v.apply(t)).filter(Validation::isInvalid)
                    .map(Validation::getError).map(msg -> String.format("Index: %s - %s", index.intValue(), msg))
                    .collect(Collectors.toList());
            messageBuffer.add(messages);
            index.incrementAndGet();
        }
        return messageBuffer.stream().flatMap(List::stream).collect(Collectors.toList());
    }
}
