package com.tcl.dias.servicehandover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.sap.beans.AutoPoResponse;
import com.tcl.dias.sap.beans.GrnResponses;
import com.tcl.dias.servicehandover.service.SapCallBackService;
import com.tcl.servicehandover.bean.ResponseResource;
import com.tcl.servicehandover.bean.Status;

/**
 * This Class is used by the SAP to do a call back
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/sap/")
public class SapCallBackController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SapCallBackController.class);

	@Autowired
	SapCallBackService sapCallBackService;

	@RequestMapping(value = "/grn", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> sapPushBack(@RequestBody GrnResponses request) {
		LOGGER.info("In to the sap push back with request {}", request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				sapCallBackService.processSapPushback(request), Status.SUCCESS);
	}
	
	/**
	 * Sap Consumer for Auto PO Response
	 * @author AnandhiV
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/po", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> sapPushBackForPo(@RequestBody AutoPoResponse request) {
		LOGGER.info("In to the sap push back with request {}", request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				sapCallBackService.processAutoPoResponse(request), Status.SUCCESS);
	}

}
