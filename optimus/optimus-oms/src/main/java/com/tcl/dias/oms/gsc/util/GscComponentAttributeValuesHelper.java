package com.tcl.dias.oms.gsc.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;
import com.tcl.dias.oms.gsc.util.annotations.ComponentAttributeValue;

/**
 * Utility class to extract annotated field and values to an attribute map then
 * pass to a consumer and apply attribute values from a
 * 
 * <pre>
 * Map<String, String>
 * </pre>
 * 
 * to respective annotated fields
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class GscComponentAttributeValuesHelper {

	private static class GscComponentAttributeValueConfig {
		static final MethodType GETTER_METHOD_TYPE = MethodType.methodType(String.class);
		static final MethodType SETTER_METHOD_TYPE = MethodType.methodType(void.class, String.class);
		Map<String, MethodHandle> propertySetterHandles = new HashMap<>();
		Map<String, MethodHandle> propertyGetterHandles = new HashMap<>();
	}

	private static final GscComponentAttributeValueConfig BLANK_CONFIG = new GscComponentAttributeValueConfig();

	private Map<Class<?>, GscComponentAttributeValueConfig> classToConfigMap = new HashMap<>();

	/**
	 * Populate the annotated fields of given bean with values from the Map matching
	 * map key with attributeName using the map data supplied by the supplier
	 * 
	 * @param bean
	 * @param attributeMapSupplier
	 * @param                      <T>
	 * @return
	 */
	public <T> T populateComponentAttributeValues(T bean, Supplier<Map<String, String>> attributeMapSupplier) {
		try {
			GscComponentAttributeValueConfig config = classToConfigMap.get(bean.getClass());
			if (config == null) {
				config = fromClass(bean.getClass());
				classToConfigMap.put(bean.getClass(), config);
			}
			return doPopulateAttributeValues(bean, config, attributeMapSupplier);
		} catch (Throwable t) {
			Throwables.propagate(t);
		}
		return bean;
	}

	private static <T> T doPopulateAttributeValues(T bean, GscComponentAttributeValueConfig config,
			Supplier<Map<String, String>> attributeMapSupplier) throws Throwable {
		Objects.requireNonNull(bean, "Bean cannot be null");
		Objects.requireNonNull(attributeMapSupplier, "Attribute map supplier cannot be null");
		if (config == BLANK_CONFIG) {
			return bean;
		}
		Map<String, String> attributeMap = attributeMapSupplier.get();
		if (!attributeMap.isEmpty()) {
			for (Map.Entry<String, String> e : attributeMap.entrySet()) {
				String attributeName = e.getKey();
				String attributeValue = e.getValue();
				MethodHandle setterHandle = config.propertySetterHandles.get(attributeName);
				if (setterHandle != null) {
					setterHandle.invoke(bean, attributeValue);
				}
			}
		}
		return bean;
	}

	/**
	 * Extract the values of fields annotated to a
	 * 
	 * <pre>
	 * Map<String, String>
	 * </pre>
	 * 
	 * and pass the same to the specified consumer which can save the same to a DB
	 * table or do further processing
	 * 
	 * @param bean
	 * @param attributeMapConsumer
	 * @param                      <T>
	 * @return
	 */
	public <T> T handleComponentAttributeValues(T bean, Consumer<Map<String, String>> attributeMapConsumer) {
		Objects.requireNonNull(bean, "Bean cannot be null");
		Objects.requireNonNull(attributeMapConsumer, "Attribute map consumer cannot be null");
		try {
			GscComponentAttributeValueConfig config = classToConfigMap.get(bean.getClass());
			if (config == null) {
				config = fromClass(bean.getClass());
				classToConfigMap.put(bean.getClass(), config);
			}
			Map<String, String> attributeMap = getAttributeValuesMap(bean, config);
			if (!attributeMap.isEmpty()) {
				attributeMapConsumer.accept(attributeMap);
			}
		} catch (Throwable t) {
			Throwables.propagate(t);
		}
		return bean;
	}

	private static <T> Map<String, String> getAttributeValuesMap(T bean, GscComponentAttributeValueConfig config)
			throws Throwable {
		Map<String, String> map = new HashMap<>();
		if (config == BLANK_CONFIG) {
			return map;
		}
		for (Map.Entry<String, MethodHandle> e : config.propertyGetterHandles.entrySet()) {
			String attributeName = e.getKey();
			String attributeValue = (String) e.getValue().invoke(bean);
			map.put(attributeName, attributeValue);
		}
		return map;
	}

	private static String fieldToGetter(String name) {
		return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private static String fieldToSetter(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private static GscComponentAttributeValueConfig fromClass(Class<?> beanClass) {
		GscComponentAttributeValueConfig config = new GscComponentAttributeValueConfig();
		MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
		try {
			for (Field field : beanClass.getDeclaredFields()) {
				if (!Objects.isNull(field.getDeclaredAnnotation(ComponentAttributeValue.class))) {
					ComponentAttributeValue valueConfig = field.getDeclaredAnnotation(ComponentAttributeValue.class);
					String fieldNameGetter = fieldToGetter(field.getName());
					String fieldNameSetter = fieldToSetter(field.getName());
					MethodHandle fieldGetterHandle = publicLookup.findVirtual(beanClass, fieldNameGetter,
							GscComponentAttributeValueConfig.GETTER_METHOD_TYPE);
					MethodHandle fieldSetterHandle = publicLookup.findVirtual(beanClass, fieldNameSetter,
							GscComponentAttributeValueConfig.SETTER_METHOD_TYPE);
					config.propertyGetterHandles.put(valueConfig.attributeName(), fieldGetterHandle);
					config.propertySetterHandles.put(valueConfig.attributeName(), fieldSetterHandle);
				}
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return config.propertyGetterHandles.isEmpty() && config.propertySetterHandles.isEmpty() ? BLANK_CONFIG : config;
	}
}
