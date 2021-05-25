package com.tcl.dias.oms.gsc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

/**
 * Utility classes for testing helper methods
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscTestUtil {
    private GscTestUtil() {
        /* static usage */
    }

    /**
     * Create an object from test resource json file
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

    /**
     * Load object specified by class from JSON file
     *
     * @param jsonFilePath
     * @param typeClass
     * @param <T>
     * @return
     */
    public static <T> T fromJsonFile(String jsonFilePath, Class<T> typeClass) {
        URL url = Resources.getResource(jsonFilePath);
        CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
        try {
            return new ObjectMapper().readValue(charSource.openStream(), typeClass);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return null;
    }

    /**
     * Read contents of file from classpath
     *
     * @param filePath
     * @return
     */
    public static String readStringFromFile(String filePath) {
        URL url = Resources.getResource(filePath);
        CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
        try {
            return charSource.read();
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        return null;
    }
}
