package com.tcl.dias.products.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 * This file contains object creation methods used in test cases.
 *
 *
 * @author Syed Ali
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class WebexObjectCreator {
	public Set<Map<String, Object>> createObjects() {
		Set<Map<String, Object>> objs = new HashSet<>();
		Map<String, Object> a = new HashMap<>();
		Map<String, Object> b = new HashMap<>();
		Map<String, Object> c = new HashMap<>();
		objs.add(a);
		objs.add(b);
		objs.add(c);
		return objs;
	}
}
