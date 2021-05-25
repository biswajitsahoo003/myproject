package com.tcl.gvg.exceptionhandler.propertyhandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * The {@code PropertyHandler} class represents setting up of Property files for
 * the exception messages The {@code PropertyHandler} will be configured in
 * Spring configuration bean or XML files The Location of the property file
 * informations are configuration in this class
 *
 * @author MRajakum
 * @since JDK1.0
 */
public class PropertyHandler {

	private static PropertyHandler instance = null;

	private Properties properties = null;

	private static final Map<String, String> propertiesMapper = new HashMap<>();

	private PropertyHandler() {
	}

	public static synchronized PropertyHandler getInstance() {
		if (instance == null)
			instance = new PropertyHandler();
		return instance;
	}

	public static String getProperty(String key) {
		return propertiesMapper.get(key);

	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
		propertiesMapper.putAll(properties.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString())));
	}
}
