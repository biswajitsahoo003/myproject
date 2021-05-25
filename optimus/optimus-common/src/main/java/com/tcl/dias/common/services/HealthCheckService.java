package com.tcl.dias.common.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Health Check Service
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class HealthCheckService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckService.class);

	/**
	 * 
	 * @return
	 */
	public Map<String, String> getHealthCheck() {
		LOGGER.info("invoking customer health check");
		Map<String, String> response = new HashMap<>();
		response.put("service", "UP");
		return response;
	}

}
