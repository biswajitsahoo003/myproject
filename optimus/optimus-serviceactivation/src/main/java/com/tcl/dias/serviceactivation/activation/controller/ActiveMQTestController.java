package com.tcl.dias.serviceactivation.activation.controller;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.serviceactivation.activation.core.ActivationConfigProducer;
import com.tcl.dias.serviceactivation.activation.services.ProductActivationConfigurationService;
import com.tcl.dias.serviceactivation.activation.utils.Product;
import com.tcl.dias.serviceactivation.activation.utils.RouterType;
import com.tcl.dias.serviceactivation.activemq.creator.MessageCreator;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the ActiveMQTestController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/activemq")
public class ActiveMQTestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQTestController.class);

	@Autowired
	MessageCreator messageCreator;

	@Autowired
	ActivationConfigProducer netpConfigProducer;

	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@Autowired
	ProductActivationConfigurationService productActivationConfigurationService;

	@Value("${activemq.netp.create.queue}")
	String netpCreateQueue;

	@RequestMapping(value = "/test", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<Response> testNetpXml(@RequestBody String request) throws TclCommonException {
		Response response = messageCreator.convertAndSend(request, netpCreateQueue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/ip", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<Response> testGenerateIpXml(@RequestParam("requestId") String requestId,
			@RequestParam("serviceId") String serviceId, @RequestParam("actionType") String actionType)
			throws TclCommonException {
		Response response = null;
		response = productActivationConfigurationService.processIpConfigurationXml(serviceId, actionType, requestId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/rf", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<Response> testGenerateRfXml(@RequestParam("requestId") String requestId,
			@RequestParam("serviceId") String serviceId, @RequestParam("actionType") String actionType)
			throws TclCommonException {
		Response response = null;
		response = productActivationConfigurationService.processRfConfigurationXml(serviceId, actionType, requestId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/tx", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<Response> testGenerateTxXml(@RequestParam("requestId") String requestId,
			@RequestParam("serviceId") String serviceId, @RequestParam("actionType") String actionType,
			@RequestParam("txType") String txType) throws TclCommonException {
		Response response = null;
		response = productActivationConfigurationService.processTxConfigurationXml(serviceId, actionType, requestId,
				txType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

}
