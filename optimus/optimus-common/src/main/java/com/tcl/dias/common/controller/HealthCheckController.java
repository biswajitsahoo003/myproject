package com.tcl.dias.common.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.services.HealthCheckService;
import com.tcl.dias.common.utils.Status;

/**
 * Health Check Controller
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RestController
@RequestMapping("/health/")
public class HealthCheckController {

	@Autowired
	HealthCheckService healthCheckService;

	@GetMapping
	public ResponseResource<Map<String, String>> getHealthCheck() {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				healthCheckService.getHealthCheck(), Status.SUCCESS);
	}

}
