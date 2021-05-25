package com.tcl.dias.servicehandover.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillmentutils.beans.GeoCodeAddress;
import com.tcl.dias.servicehandover.beans.renewal.RenewalAttachments;
import com.tcl.dias.servicehandover.beans.renewal.RenewalContractSiteDetails;

import com.tcl.dias.servicehandover.beans.renewal.RenewalValidateDocumentDetails;
import com.tcl.dias.servicehandover.beans.sureaddress.SoapRequestResponse;
import com.tcl.dias.servicehandover.service.SureTaxService;
import com.tcl.dias.servicehandover.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicehandover.util.AddressConstructor;
import com.tcl.dias.servicehandover.util.AddressConstructorIntl;
import com.tcl.dias.servicehandover.util.ContractAddressIntl;
import com.tcl.dias.servicehandover.util.ContractSiteGstAddress;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * OTB-340 This controller performs account/product creation and invoice generation
 * operation for orders placed.
 * 
 * @author arjayapa
 */
@RestController
@RequestMapping("/v1/address") 
public class AddressController {

	@Autowired
	AddressConstructor addressConstructor;
	
	@Autowired
	SureTaxService geoGodeGenerator;
	
	@Autowired
	AddressConstructorIntl addressConstructorIntl;

	/**
	 * api for fetching contracting and site address
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ContractSiteGstAddress.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/contract-site-gst-address/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ContractSiteGstAddress> getContractSiteGstAddress(@PathVariable("task_id") String taskId)
			throws TclCommonException {
		return new ResponseResource<ContractSiteGstAddress>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				addressConstructor.getGstAddress(taskId), Status.SUCCESS);
	}

	
	/**
	 * api for saving contracting and site address
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/save-contract-site-gst-address/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveContractSiteGstAddress(@PathVariable("service_id") String serviceId,@RequestBody ContractSiteGstAddress contractSiteGstAddress)
			throws TclCommonException {
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				addressConstructor.saveGstAddress(serviceId,contractSiteGstAddress), Status.SUCCESS);
	}
	
	/**
	 * api for fetching Vat no and site address Intl
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ContractAddressIntl.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/get-vat-site-address/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ContractAddressIntl> getAddressAndVatIntl(@PathVariable("task_id") String taskId)
			throws TclCommonException {
		return new ResponseResource<ContractAddressIntl>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				addressConstructorIntl.getAddress(taskId), Status.SUCCESS);
	}
	
	/**
	 * api for saving Vat number and site address Intl
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/save-vat-site-address/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveAddressAndVatIntl(@PathVariable("service_id") String serviceId,@RequestBody ContractAddressIntl contractAddressIntl)
			throws TclCommonException {
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				addressConstructorIntl.saveAddress(serviceId,contractAddressIntl), Status.SUCCESS);
	}
	
	/**
	 * api for fetching Geo Code
	 * 
	 * @param geoCodeAddress
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SoapRequestResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/get-geo-code", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> genGeoCode(@RequestBody GeoCodeAddress geoCodeAddress)
			throws TclCommonException {
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				geoGodeGenerator.generateGeoCode(geoCodeAddress), Status.SUCCESS);
	}
	
	/**
	 * api for fetching renewal address
	 *
	 * @param orderCode
	 * @return List<Map<String,Object>>
	 * @author dimpleS
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/renewal/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String,Object>>> getRenewalAddress(@PathVariable("orderCode") String orderCode)
			throws TclCommonException {
		return new ResponseResource<List<Map<String,Object>>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				addressConstructor.getRenewalAddress(orderCode), Status.SUCCESS);
	}
	
	/**
	 * api for saving contracting,site address and po details
	 *
	 * @param renewalValidateDocumentDetails
	 * @return RenewalValidateDocumentDetails
	 * @author dimpleS
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/renewal/save-contract-site-gst-address", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<RenewalValidateDocumentDetails> saveRenewalContractSiteGstAddressPODetails(@RequestBody RenewalValidateDocumentDetails renewalValidateDocumentDetails)
			throws TclCommonException {
		return new ResponseResource<RenewalValidateDocumentDetails>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				addressConstructor.saveRenewalContractSiteGstAddressPODetails(renewalValidateDocumentDetails), Status.SUCCESS);
	}
	
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/renewal/attachments/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<RenewalAttachments> getRenewalAttachments(@PathVariable("orderCode") String orderCode)
			throws TclCommonException {
		return new ResponseResource<RenewalAttachments>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				addressConstructor.getRenewalAttachments(orderCode), Status.SUCCESS);
	}
	
	

	
}