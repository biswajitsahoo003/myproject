package com.tcl.dias.networkaugmentation.controller;

import java.util.List;
import java.util.Map;

import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.AutoPoResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugmentation.swagger.constants.SwaggerConstants;
//import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
//import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
//import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupingBean;
//import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoRequest;
//import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * This is the dummy controller class
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/preparefulfillment")
public class PrepareFulfillmentController {
	
//	@Autowired
//	SapService sapService;
//
//	@Autowired
//	ScServiceDetailRepository scServiceDetailRepository;
//	ScServiceDetail scServiceDetail;
//	ScServiceDetailRepository scServiceDetailRepository;

//	
//	/**
//	 * 
//	 * Auto PO request dummpy API
//	 * @author AnandhiV
//	 * @param serviceId
//	 * @return
//	 * @throws TclCommonException
//	 */
//	@ApiOperation(value = "Auto PO request dummpy API")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/autopo", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> triggerAutoPo(@RequestParam(value = "serviceId") Integer serviceId) throws TclCommonException {
//	
//		String response = sapService.processOffnetAutoPo(serviceId,null);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//	
//	@ApiOperation(value = "Auto PO request dummpy API")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/autopo/request", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<AutoPoRequest> getTaskDetails(@RequestParam(value = "serviceId") String serviceId) throws TclCommonException {
//		AutoPoRequest response = sapService.getAutoPORequest(serviceId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 *
//	 * Auto PO request dummpy API
//	 * @author Thamizhselvi perumal
//	 * @param serviceId
//	 * @return
//	 * @throws TclCommonException
//	 */
//	@ApiOperation(value = "Auto PO update dummpy API")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/autopo/update", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> triggerAutoPoUpdate(@RequestParam(value = "serviceId") Integer serviceId) throws TclCommonException {
//		String response = sapService.processOffnetAutoPoUpdate(serviceId,null);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
}
