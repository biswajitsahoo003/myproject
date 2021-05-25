package com.tcl.dias.customer.controller.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.*;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.customer.dto.CustomerContractingAddressResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.bean.AttachmentBean;
import com.tcl.dias.customer.bean.BillingAddress;
import com.tcl.dias.customer.bean.CustomerLeAttributeBean;
import com.tcl.dias.customer.bean.CustomerLeBillingRequestBean;
import com.tcl.dias.customer.bean.CustomerLeContactBean;
import com.tcl.dias.customer.bean.CustomerLeContactDetailsBean;
import com.tcl.dias.customer.bean.CustomerLegalEntityRequestBean;
import com.tcl.dias.customer.bean.IzosdwanSupplierResponseBean;
import com.tcl.dias.customer.bean.DocumentBean;
import com.tcl.dias.customer.bean.LeStateGstBean;
import com.tcl.dias.customer.bean.ServiceResponse;
import com.tcl.dias.customer.dto.AttributesDto;
import com.tcl.dias.customer.dto.CustomerConatctInfoResponseDto;
import com.tcl.dias.customer.dto.CustomerContractingAddressResponseDto;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.dto.CustomerLeBillingInfoDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityProductResponseDto;
import com.tcl.dias.customer.dto.OmsDetailsBean;
import com.tcl.dias.customer.entity.entities.Attachment;
import com.tcl.dias.customer.entity.entities.ServiceProviderLegalEntity;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.dias.customer.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * This file contains the CustomerController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	/**
	 * Method returns the billing details for the input cuatomer_legal_Id.
	 * 
	 * @param custLegalId
	 * @return ResponseResource<List<AttributesDto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_BILLING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/{customerLeId}/attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AttributesDto>> getBillingDetailsById(
			@PathVariable("customerLeId") Integer custLegalId,@RequestParam(name = "productName",required = false) String productName) throws TclCommonException {
		List<AttributesDto> attributesDto = customerService.getBillingDetailsById(custLegalId,productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attributesDto,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.Customer.GET_BILLING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/{customerLeId}/billing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<BillingContact>> getBillingContacts(@PathVariable("customerLeId") Integer custLegalId)
			throws TclCommonException {
		List<BillingContact> billingContacts = customerService.getBillingContact(custLegalId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, billingContacts,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.Customer.GET_BILLING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/billingcontacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<BillingContactInfo>> getBillingContacts()
			throws TclCommonException {
		List<BillingContactInfo> billingContacts = customerService.getBillingContactInfo();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, billingContacts,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.Customer.GET_BILLING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/{customerLeId}/billing/{contactId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<BillingContact> getBillingContactsById(@PathVariable("customerLeId") Integer customerLeId,
			@PathVariable("contactId") Integer contactId) throws TclCommonException {
		BillingContact billingContact = customerService.getBillingContactById(contactId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, billingContact,
				Status.SUCCESS);
	}
	
	/**
	 * Method returns the customerlegal entity details list for the input customer
	 * id.
	 *
	 * @param customerId
	 * @return ResponseResource<List<CustomerLegalEntityDto>>
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/partner/{customerId}/le", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerLegalEntityDto>> getCustomerLegalEntityDetailsByCustomerIdForAll(
			@PathVariable("customerId") @BaseArgument Integer customerId) throws TclCommonException {
		List<CustomerLegalEntityDto> customerLegalEntityDto = customerService
				.findCustomerEntityByCustomerIdForAll(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}


	/**
	 * Method returns the customerlegal entity details list for the input customer
	 * id.
	 * 
	 * @param customerId
	 * @return ResponseResource<List<CustomerLegalEntityDto>>
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/{customerId}/le", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerLegalEntityDto>> getCustomerLegalEntityDetailsByCustomerId(
			@PathVariable("customerId") @BaseArgument Integer customerId,@RequestParam(name = "isPartner",required = false) String isPartner) throws TclCommonException {
		List<CustomerLegalEntityDto> customerLegalEntityDto = customerService
				.findCustomerEntityByCustomerId(customerId,isPartner);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}


	/**
	 * Method returns the customerlegal entity details list for the input customer
	 * id and services.
	 *
	 * @param customerId
	 * @param product
	 * @param services
	 * @param country
	 * @return {@link List<CustomerLegalEntityDto>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID_AND_SEVICE)
	@RequestMapping(value = "/{customerId}/le/product/{product}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerLegalEntityDto>> getCustomerLegalEntityDetailsByCustomerIdForService(
			@PathVariable("customerId") @BaseArgument Integer customerId, @PathVariable("product") String product,
			@RequestParam List<String> services, @RequestParam(required = false) String country, @RequestParam String accessType)
			throws TclCommonException {
		List<CustomerLegalEntityDto> customerLegalEntityDto = customerService
				.findCustomerEntityByCustomerIdForService(customerId, product, services, country, accessType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}

	/**
	 * This Method Takes Customer LegalId and SpLegal id as input and it provides
	 * the customer Information and Supplier Information
	 *
	 * @param custLegalId
	 * @param spLegelId
	 * @return Customer conatct info
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_CONATCT_INFO)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/{customerLeId}/sle/{supplierLeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CustomerConatctInfoResponseDto> getContactInfoDetailsById(
			@PathVariable("customerLeId") Integer custLegalId, @PathVariable("supplierLeId") Integer spLegelId)
			throws TclCommonException {

		CustomerConatctInfoResponseDto contactInfoDto = customerService
				.getContactInfoDetaisByCustLegalIdAndSPLegalId(custLegalId, spLegelId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, contactInfoDto,
				Status.SUCCESS);
	}
	
	/**
	 * This Method Takes SpLegal id as input and it provides Supplier Information
	 * @param spLegelId
	 * @return Supplier country info
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_CONATCT_INFO)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sle/{supplierLeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceProviderCountryBean> getTaxationDetailsById(@PathVariable("supplierLeId") Integer spLegelId)
			throws TclCommonException {

		ServiceProviderCountryBean serviceProviderCountryBean = customerService
				.getCountryDetailsBySPLegalId(spLegelId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceProviderCountryBean,
				Status.SUCCESS);
	}

	/**
	 * 
	 * getMSADocuments
	 * 
	 * @param custLegalId
	 * @return List<AttachmentBean>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_MSA_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/{customerLeId}/attachments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AttachmentBean>> getMSADocuments(@PathVariable("customerLeId") Integer custLegalId,
			@RequestParam(name = "productName") String productName) throws TclCommonException {
		List<AttachmentBean> attachments = customerService.getMSADocumentDetails(custLegalId, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachments,
				Status.SUCCESS);
	}

	/**
	 * This Method Takes Customer LegalId productname as input and it provides the
	 * details of currency,sple and INR Information
	 * 
	 * @param customerlegalentityid
	 * @param productname
	 * @return customerLegalEntityProductResponseDto
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_BY_ID)
	@RequestMapping(value = "/le/{customerLeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerLegalEntityProductResponseDto> getCustomerLegalEntityDetailsById(
			@PathVariable("customerLeId") Integer customerlegalentityid, @RequestParam("product") String productname,
			@RequestParam(required = false) Integer quoteLeId)
			throws TclCommonException {
		CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = customerService
				.findCustomerEntityById(customerlegalentityid, productname, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLegalEntityProductResponseDto, Status.SUCCESS);

	}
	
	/**
	 * 
	 * @param customerlegalentityid
	 * @param productname
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_BY_ID)
	@RequestMapping(value = "/oe/le/{customerLeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerLegalEntityProductResponseDto>> getCustomerLegalEntityDetailsByIdForOE(
			@PathVariable("customerLeId") Integer customerlegalentityid, @RequestParam("product") String productname,
			@RequestParam(required = false) Integer quoteLeId)
			throws TclCommonException {
		List<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDto = customerService
				.findCustomerEntityByIdForOE(customerlegalentityid, productname, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLegalEntityProductResponseDto, Status.SUCCESS);

	}

	/**
	 * Method returns the customerlegal entity all details list for the input
	 * customer id.
	 * 
	 * @param customerId
	 * @return ResponseResource<List<CustomerLegalEntityDto>>
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/le", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerDto>> getCustomerLegalEntityAllDetails() throws TclCommonException {
		List<CustomerDto> customerLegalEntityDto = customerService.findCustomerEntityAllDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}

	/**
	 * Method returns the customerlegal entity all details list for the input
	 * customer legal entity id.
	 * 
	 * @param customerleId
	 * @return ResponseResource<List<CustomerLegalEntityDto>>
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_LEGAL_ENTITY_ID)
	@RequestMapping(value = "/les/{customerLeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerLegalEntityDto>> getCustomerLegalEntityDetailsByCustomerLeId(
			@PathVariable("customerLeId") Set<Integer> customerLeId) throws TclCommonException {
		List<CustomerLegalEntityDto> customerLegalEntityDto = customerService
				.findCustomerEntityDetailsByCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_LEGAL_ENTITY_ID)
	@RequestMapping(value = "/le/{customerLeId}/details/currency", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerLegalEntityDto>> getCustomerLegalEntityDetailByCustomerLeId(
			@PathVariable("customerLeId") Integer customerLeId) throws TclCommonException {
		Set<Integer> customerIds = new HashSet<>();
		customerIds.add(customerLeId);
		List<CustomerLegalEntityDto> customerLegalEntityDto = customerService.findCustomerEntityDetailsByCustomerLeId(customerIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}

	/**
	 * Method returns the customerlegal entity all details list for the input
	 * customer legal entity id.
	 * 
	 * @param customerleId
	 * @return ResponseResource<List<CustomerLegalEntityDto>>
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_LEGAL_ENTITY_ID)
	@RequestMapping(value = "/le/{customerLeId}/gst/{gstId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = BillingAddress.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<BillingAddress> getGstDetails(@PathVariable("customerLeId") Integer customerLeId,
			@PathVariable("gstId") Integer leStateGstId) throws TclCommonException {
		BillingAddress response = customerService.getGstDetails(customerLeId, leStateGstId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Method returns the attachment details for the given attachment ID customer
	 * legal entity id.
	 * 
	 * @param customerleId
	 * @return ResponseResource<List<CustomerLegalEntityDto>>
	 */

	@ApiOperation(value = SwaggerConstants.Customer.ATTACHMENT_DEATILS)
	@RequestMapping(value = "/le/{customerLeId}/attachments/{attachmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<AttachmentBean> getCustomerAttchmentDeatilsById(
			@PathVariable("customerLeId") Integer customerLeId, @PathVariable("attachmentId") Integer attachmentId)
			throws TclCommonException {
		AttachmentBean attachment = customerService.findAttachmentByAttachmentId(attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachment,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_OMS_BILING_DETAILS)
	@RequestMapping(value = "/billing/info/oms/{customerLeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OmsDetailsBean> getBillingDetailsForOms(@PathVariable("customerLeId") Integer customerLeId)
			throws TclCommonException {
		OmsDetailsBean omsDetailsBean = new OmsDetailsBean();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsDetailsBean,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_LE_STATE_GST)
	@RequestMapping(value = "/le/{customerLeId}/gst", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LeStateGstBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LeStateGstBean>> getLeStateGst(@PathVariable("customerLeId") Integer customerLeId,
			@RequestParam("leStateGst") String leState) throws TclCommonException {
		List<LeStateGstBean> response = customerService.getLeStateGst(leState, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author PRABHU A
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.ADD_LE_STATE_GST)
	@PostMapping("/le/{customerLeId}/gst")
	public ResponseResource<String> addLeStateGst(@PathVariable("customerLeId") Integer customerLeId,@RequestBody LeStateGstBean leStateGstBean) throws TclCommonException {
		customerService.createLeStateGstInfo(customerLeId,leStateGstBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}
	/**
	 * Takes file and quote details as input and stores the attachment details
	 * 
	 * needed for test upload of Msa/Service agreement document
	 * 
	 * @param file
	 * @param qouteId
	 * @param orderId
	 * @param quoteLeId
	 * @param nameOfTheOperation
	 * @return FileUploadResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_MSA_DOC)
	@RequestMapping(value = "le/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<AttachmentBean> upLoadLegalEntityFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam("referenceName") String referenceName, @RequestParam("attachmentType") String attachmentType,
			@RequestParam(name = "productName", required = false) String productName) throws TclCommonException {

		AttachmentBean response = customerService.uploadFiles(file, customerLeId, referenceName, attachmentType,
				productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Takes file and quote details as input and stores the attachment details
	 * 
	 * needed for test upload of Msa/Service agreement document - not used anywhere
	 * in ui
	 * 
	 * @param file
	 * @param qouteId
	 * @param orderId
	 * @param quoteLeId
	 * @param nameOfTheOperation
	 * @return FileUploadResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_MSA_DOC)
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<AttachmentBean> upLoadCustomerFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestParam("referenceName") String referenceName, @RequestParam("attachmentType") String attachmentType)
			throws TclCommonException {

		AttachmentBean response = customerService.upLoadCustomerFile(file, customerId, referenceName, attachmentType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/le/{customerLeId}/download/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<Resource> getAttachments(@PathVariable("customerLeId") Integer customerLeId,
			@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		Resource file = customerService.getAttachments(customerLeId, attachmentId);
		if (file == null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			return ResponseEntity.ok().headers(headers).body(file);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + file.getFilename());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(file);

	}

	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_FILE_DOC)
	@RequestMapping(value = "/le/{customerLeId}/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> upLoadFile(@PathVariable("customerLeId") Integer customerLeId,
			@RequestParam(name="file") MultipartFile file,
			@RequestParam(value = "orderToLeId", required = false) Integer orderToLeId,
			@RequestParam(value = "quoteToLeId", required = false) Integer quoteToLeId,
			@RequestParam("referenceId") List<Integer> referenceId, @RequestParam("referenceName") String referenceName,
			@RequestParam("attachmentType") String attachmentType, @RequestParam(value = "productName", required = false) String productName) throws TclCommonException {
		ServiceResponse response = customerService.processUploadFiles(file, orderToLeId, quoteToLeId, attachmentType,
				referenceId, referenceName, customerLeId,productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.Customer.GET_TEMP_URL_UPLOAD)
	@RequestMapping(value = "/le/{customerLeId}/upload/url", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> getUploadTemperoryUrl(@PathVariable("customerLeId") Integer customerLeId) throws TclCommonException {
		ServiceResponse response = customerService.processUploadFiles(null, null, null, null,
				null, null, customerLeId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_FILE_DOC)
	@RequestMapping(value = "/getallcustomer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerDto>> getAllCustomer() throws TclCommonException {
		List<CustomerDto> response = customerService.getAllCustomer();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.CUSTOMER_LEGAL_ENTITY)
	@RequestMapping(value = "/le/{customerLeId}/splegal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityProductResponseDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerLegalEntityProductResponseDto> getSupplierLegalEntityBasedOnCust(
			@RequestBody CustomerLegalEntityRequestBean request) throws TclCommonException {
		CustomerLegalEntityProductResponseDto response = customerService.getSupplierLegalEntityBasedOnCust(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.CUSTOMER_LEGAL_ENTITY)
	@RequestMapping(value = "/le/{customerLeId}/contacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityProductResponseDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerLeContactDetailsBean> getCustomerLeContactDetails(
			@PathVariable("customerLeId") Integer customerLeId) throws TclCommonException {
		CustomerLeContactDetailsBean response = customerService.getContactDetailsForTheCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@RequestMapping(value = "/le/{customerLeId}/leContacts",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLeContactDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 422, message = Constants.EXCEPTION_FAILED)
	})
	public ResponseResource<List<CustomerLeContactDetailBean>> getCustomerLeContact(
			@PathVariable("customerLeId") Integer customerLeId) throws  TclCommonException{
		List<CustomerLeContactDetailBean> response = customerService.getContactDetailsByCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	
	/**
	 * This api is to get country list for a given customer legal entity
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.CUSTOMER_LEGAL_ENTITY)
	@RequestMapping(value = "/le/{customerLeId}/countries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<String>> getCustomerLeCountries(
			@PathVariable("customerLeId") Integer customerLeId) throws TclCommonException {
		List<String> response = customerService.getCountriesListForTheCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * Api to update the contact information of a customer legal entity
	 * @param leContactBean
	 * @param customerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.UPDATE_CUSTOMER_LEGAL_ENTITY_CONTACT)
	@RequestMapping(value = "{customerId}/le/{customerLeId}/updateContact", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateCustomerLeContactDetails(@RequestBody CustomerLeContactBean leContactBean, @PathVariable("customerId") Integer customerId,
			@PathVariable("customerLeId") Integer customerLeId) throws TclCommonException {
		customerService.updateContactDetailsForTheCustomerLeId(customerId,customerLeId,leContactBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}

	/**
	 * @author Biswajit
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.CUSTOMER_LEGAL_DATA_CENTER)
	@RequestMapping(value = "/{customerId}/datacenters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityProductResponseDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<DataCenterBean>> getCustomerLeDataCenter(
			@PathVariable("customerId") Integer customerId) throws TclCommonException {
		List<DataCenterBean> response = customerService.getCustomerLeDataCenter(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.CUSTOMER_LEGAL_DATA_CENTER)
	@RequestMapping(value = "/le/{customerLeId}/sscheckthrough", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityProductResponseDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceScheduleBean> attachSSDocument(@PathVariable("customerLeId") Integer customerLeId,
			@RequestBody ServiceScheduleBean serviceScheduleBean) throws TclCommonException {
		ServiceScheduleBean response = customerService.mapSSDocumentToLe(serviceScheduleBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_MSA_DOC)
	@RequestMapping(value = "/upload/ss", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> upLoadSSFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "productName", required = true) String productName) throws TclCommonException {

		String response = customerService.uploadSSDocument(file, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Biswajit
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * 
	 *            Use to edit the customer billing details like (Name,emailId and
	 *            phone number etc)
	 * @param customerLeId
	 * @param CustomerLeBillingRequestBean
	 * @return ResponseResource<CustomerLeBillingInfoDto>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.EDIT_BILLING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/{customerLeId}/billing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseResource<CustomerLeBillingInfoDto> editBillingInfo(@PathVariable("customerLeId") Integer custLegalId,
			@RequestBody CustomerLeBillingRequestBean request) throws TclCommonException {
		CustomerLeBillingInfoDto customerLeBillingInfoDto = customerService.editBillingInfo(custLegalId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLeBillingInfoDto, Status.SUCCESS);
	}

	/**
	 * @author Biswajit
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * 
	 *            Use to map the MSA documet to legal entiyi
	 * 
	 * @param file
	 * @param productName
	 * @return ResponseResource<String>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_MSA_DOC)
	@RequestMapping(value = "/msa", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> upLoadMSAFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "productName", required = true) String productName) throws TclCommonException {
		String response = customerService.uploadMSADocument(file, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Upload country specific template files
	 * 
	 * @param file
	 * @return {@link ServiceResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_FILE_DOC)
	@RequestMapping(value = "/upload/country/files", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> upLoadCountrySpecificFiles(
			@RequestParam(value = "countryName", required = true) String countryName,
			@RequestParam("productName") String productName, @RequestParam(name = "file") MultipartFile file)
			throws TclCommonException {
		ServiceResponse response = customerService.uploadCountrySpecificFiles(file, countryName, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

    /**
     *API to update the uploaded country documents in attachments.
     *
     * @param requestId
     * @param url
     * @param documentId
     * @return {@link ServiceResponse}
     * @throws TclCommonException
     */
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_FILE_DOC)
	@RequestMapping(value = "/update/upload/country/files", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> upLoadedCountrySpecificFiles(
			@RequestParam("requestId") String requestId,@RequestParam(name = "url") String url,
			@RequestParam(name = "documentId") String documentId)
			throws TclCommonException {
		ServiceResponse response = customerService.uploadCountrySpecificFiles(requestId,url,documentId+"_OBJECT");
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * This Method Takes Customer Id, product name, service and access type as input
	 * and it provides the details of currency,sple and INR Information
	 *
	 * @param customerLegalEntityId
	 * @param productName
	 * @param serviceNames
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_BY_ID)
	@RequestMapping(value = "/le/{customerLeId}/product/{product}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Set<CustomerLegalEntityProductResponseDto>> getSupplierLegalEntityDetailsByCustomerLegalIdForService(
			@PathVariable("customerLeId") Integer customerLegalEntityId, @PathVariable("product") String product,
			@RequestParam("services") List<String> services) throws TclCommonException {
		Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = customerService
				.getSupplierLegalEntityDetailsByCustomerLegalIdForService(customerLegalEntityId, product, services);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLegalEntityProductResponseDtos, Status.SUCCESS);

	}


	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/download/countrySpecificFiles/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<Resource> getCountrySpecificAttachments(@PathVariable("attachmentId") Integer attachmentId)
			throws TclCommonException {
		Resource file = customerService.getAttachments(attachmentId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + file.getFilename());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(file);
	}

	/**
	 * Get temp download url for country documents GSIP
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/download/tempdownloadurl/countrySpecificFiles/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getCountrySpecificAttachmentsTempDownloadUrl(@PathVariable("attachmentId") Integer attachmentId)
			throws TclCommonException {
		String tempDownloadUrl = customerService.getAttachmentTempDownloadUrl(attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * API to get the outbound prices.
	 *
	 * @param quoteCode
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/outbound/prices", method = RequestMethod.GET)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseEntity<Resource> downloadOutboundPrices(@RequestParam("quoteCode") String quoteCode,
														   @RequestParam("fileType") String fileType, @RequestParam("fileName") String fileName)
			throws TclCommonException {
		Resource file = customerService.getOutboundPrices(quoteCode, fileType, fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + file.getFilename());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(file);

	}

	/**
	 * Method to save or update the gst number for the legal entity id.
	 * 
	 * @param customerId
	 * @param customerLeId
	 * @param gstNo
	 * @return ResponseResource<String>
	 */

	@ApiOperation(value = SwaggerConstants.Customer.SAVE_OR_UPDATE_LE_GST)
	@RequestMapping(value = "/{customerId}/le/{customerLeId}/legst", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> saveOrUpdateGstNumberForLegalEntity(
			@PathVariable("customerId") @BaseArgument Integer customerId,
			@PathVariable("customerLeId") Integer customerLeId, @RequestParam("gstNo") String gstNo)
			throws TclCommonException {
		String response = customerService.saveOrUpdateLeGst(customerId, customerLeId, gstNo);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Method to get the temporary download url for MSA and SS documents uploaded to
	 * the storage container
	 * 
	 * @param customerLeId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_TEMP_DOWNLOAD_URL)
	@RequestMapping(value = "/le/{customerLeId}/tempdownloadurl/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getTempDownloadUrl(@PathVariable("customerLeId") Integer customerLeId,
			@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = customerService.getTempDownloadUrl(customerLeId, attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}

	/**
	 * 
	 * updateDocumentUploadedDetails - api to update the uploaded details after the
	 * document is stored in the storage container
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.Customer.UPDATE_DOCUMENT_UPLOADED_DETAILS)
	@RequestMapping(value = "/le/{customerLeId}/documentuploaded", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> updateCofUploadedDetails(
			@PathVariable("customerLeId") Integer customerLeId, @RequestParam("referenceId") List<Integer> referenceId,
			@RequestParam("referenceName") String referenceName, @RequestParam("attachmentType") String attachmentType,
			@RequestParam("requestId") String requestId,
			@RequestParam(value = "orderToLeId", required = false) Integer orderToLeId,
			@RequestParam(value = "quoteToLeId", required = false) Integer quoteToLeId,
			@RequestParam(value = "url") String url) throws TclCommonException {
		ServiceResponse response = customerService.updateDocumentUploadedDetails(orderToLeId, quoteToLeId, referenceId,
				referenceName, requestId, attachmentType, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Method to get the temporary download url for other documents uploaded to the
	 * storage container
	 * 
	 * @param customerLeId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_TEMP_DOWNLOAD_URL)
	@RequestMapping(value = "/le/{customerLeId}/tempdownloadurl/documents/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getTempDownloadUrlForDocuments(@PathVariable("customerLeId") Integer customerLeId,
			@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = customerService.getTempDownloadUrlForDocuments(customerLeId, attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}

	/**
	 * Method to generate the passcode
	 * 
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GENERATE_PASSCODE)
	@RequestMapping(value = "/le/{customerLeId}/passcode", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getPasscode(@PathVariable("customerLeId") Integer customerLeId)
			throws TclCommonException {
		String passcode = customerService.generatePassCode(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, passcode,
				Status.SUCCESS);

	}

	/**
	 * Method to generate the passcode to create temp urls for storage container
	 * based on the legal entities
	 * 
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_TEMP_DOWNLOAD_URL)
	@RequestMapping(value = "/{customerId}/le/{customerLeId}/container", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getPasscode(@PathVariable("customerId") Integer customerId,
			@PathVariable("customerLeId") Integer customerLeId, @RequestParam("passcode") String passcode)
			throws TclCommonException {
		customerService.createContainer(customerId, customerLeId, passcode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * Get Currency based on country
	 * 
	 * @param countryName
	 * @return {@link String}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CURRENCY_DETAILS)
	@RequestMapping(value = "/{countryName}/currencies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {

			@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getCurrencyDetails(@PathVariable("countryName") String countryName)
			throws TclCommonException {
		String response = customerService.getCurrencyDetails(countryName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	
	/**
	 * API written for IZOPC
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_BY_ID)
	@RequestMapping(value = "/le/splegal/international", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	
	public ResponseResource<CustomerLegalEntityProductResponseDto> getInternationalSupplierLegalEntityBasedOnCust(
			@RequestBody CustomerLegalEntityRequestBean request) throws TclCommonException {
		CustomerLegalEntityProductResponseDto response = customerService.getInternationalSupplierLegalEntityBasedOnCust(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

@PostMapping(value = "/{customerId}/le/{customerLeId}/attributes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLeAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<List<CustomerLeAttributeBean>> saveAndUpdateCustomerLeAttributes(@PathVariable("customerLeId") Integer customerLeId, @RequestBody List<CustomerLeAttributeBean> attributes) {
		List<CustomerLeAttributeBean> customerLeAttributeBeans =  customerService.saveOrUpdateCustomerLeAttributes(customerLeId, attributes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLeAttributeBeans,
				Status.SUCCESS);
	}

	/**
	 * Method returns the customer Legal attrbutes for the input customer_legal_Id.
	 *
	 * @param custLegalId
	 * @return ResponseResource<List<AttributesDto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_BILLING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{customerId}/le/{customerLeId}/attributes")
	public ResponseResource<List<AttributesDto>> getCustomerLeAttrinutesById(
			@PathVariable("customerLeId") Integer custLegalId,@RequestParam(name="productName",required = false) String productName) throws TclCommonException {
		List<AttributesDto> attributesDto = customerService.getCustomerLeAttributesById(custLegalId, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attributesDto,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.Customer.GET_GST_FOR_CUSTOMER_LEGAL_ENTITY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/le/{customerLeId}/leGst")
	public ResponseResource<String> getGstNumberForCustomerLegalEntity(
			@PathVariable("customerLeId") Integer customerLeId) throws TclCommonException {
		String gstNo = customerService.getGstNumberByCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstNo,
				Status.SUCCESS);
	}

	/**
	 * API to get the outbound prices.
	 *
	 * @param quoteCode
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/object/outbound/prices", method = RequestMethod.GET)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<String> downloadObjectOutboundPrices(@RequestParam("quoteCode") String quoteCode,
														   @RequestParam("fileType") String fileType, @RequestParam("fileName") String fileName)
			throws TclCommonException {
		String temporaryUrl = customerService.getOutboundPricesTemporaryUrl(quoteCode, fileType, fileName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,temporaryUrl,
				Status.SUCCESS);
	}
	
	/**
	 * Method to move the relevant files of a customer legal entity to object storage
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	
	
	@ApiOperation(value = SwaggerConstants.Customer.MOVE_FILES_TO_OBJECT_STORAGE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/movetoobjectstorage")
	public ResponseResource<List<Integer>> getFilesToMoveToObjectStorage() throws TclCommonException {
		 List<Integer> attachmentIdsList = customerService.getFilesToMoveToObjectStorage();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachmentIdsList,
				Status.SUCCESS);
	}

	/**
	 * Get customer details from customerlegalid
	 * @param customerLeId
	 *
	 * @return {@link CustomerDto}
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/le/{customerLeId}/customer")
	public ResponseResource<CustomerDto> getCustomerByCustomerLeId(@PathVariable ("customerLeId") Integer customerLeId) {
		CustomerDto response = customerService.getCustomerByCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * getCustomersList API to List<CustomerDto> for given customerIds
	 * @param customerIds
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LIST)
	@RequestMapping(value = "/list/customers", method = RequestMethod.GET)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<List<CustomerDto>> getCustomersList(@RequestParam("customerIds") List<Integer> customerIds)
			throws TclCommonException {
		List<CustomerDto> customerList = customerService.getCustomerList(customerIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,customerList,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get gst details using customerLeId
	 * @param customerLeId
	 * @return {@link List<CustomerLegalEntityDto>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/macd/{customerLeId}/le/gst", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource <List<CustomerLegalEntityDto>> getGstDetailsByCustomerLeId(@PathVariable Integer customerLeId) throws TclCommonException {
		List<CustomerLegalEntityDto> customerLegalEntityDto = customerService.findCustomerEntityByCustomerLeId(Arrays.asList(customerLeId));
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}

	/**
	 * Method returns the customerContractingAddress entity details list for the input customerLeId
	 * id.
	 *
	 * @param customerLeId
	 * @return ResponseResource<List<CustomerLegalEntityDto>>
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_CONTRACTING_ENTITY_DETAILS_BY_lE_ID)
	@RequestMapping(value = "/{customerLeId}/cca", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerContractingAddressResponseDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerContractingAddressResponseDto> getCustomerContractingAddressListByCustomerLeId(
			@PathVariable("customerLeId") @BaseArgument Integer customerLeId) throws TclCommonException {
		CustomerContractingAddressResponseDto customerContractingAddressResponseDto = customerService.getCustomerContractingAddressesByCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerContractingAddressResponseDto,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 *  Get Supplier details for IZO-SDWAN
	 * @param quoteId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_ALL_SPLE_IZOSDWAN)
	@RequestMapping(value = "/izosdwan/sple/{customerLeId}/{quoteId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<IzosdwanSupplierResponseBean> getSupplierDetailsForIzosdwan(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("customerLeId") Integer customerLeId,
			@RequestParam(value = "isByonOnly", required = false) Boolean isByonOnly)
			throws TclCommonException {
		IzosdwanSupplierResponseBean response = customerService.getAllSupplierDetailsBasedOnQuote(quoteId,
				customerLeId,isByonOnly);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * getGstNumbersByCustomerLeId API to get gst no based on customerLeId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/gstno/{customerLeId}/le", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<List<String>> getGstNumbersByCustomerLeId(@PathVariable Integer customerLeId) throws TclCommonException {
		List<String> customerLegalEntityDto = customerService.getGstNumbersByCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}


	/**
	 *
	 * This method gives all supplier details
	 *
	 * @return {@link List<CustomerLegalEntityProductResponseDto>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_BY_ID)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityProductResponseDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "{customerId}/supplier/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<CustomerLegalEntityProductResponseDto>> getAllSupplierDetails(@PathVariable Integer customerId) {
        List<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = customerService.getAllSupplierDetails(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityProductResponseDtos, Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_FILE_DOC)
	@RequestMapping(value = "/quote/{referenceId}/izosdwan/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
	      @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	      @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	      @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> upLoadFileSSD( @RequestParam(value = "customerLeId", required = false)Integer customerLeId,
	                                       @RequestParam(name="file") MultipartFile file,
	                                       @RequestParam(value = "orderToLeId", required = false) Integer orderToLeId,
	                                       @RequestParam(value = "quoteToLeId") Integer quoteToLeId,
	                                       @RequestParam("referenceName") String referenceName,
	                                       @RequestParam("attachmentType") String attachmentType, @RequestParam(value = "productName", required = false) String productName 
	                                       ,@PathVariable("referenceId") Integer referenceId) throws TclCommonException {
	   ServiceResponse response = customerService.processUploadFilesSDD(file, orderToLeId, quoteToLeId, attachmentType,
	          referenceName, customerLeId,productName,referenceId);
	   return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	         Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/izosdwan/sdd/download/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<Resource> getAttachmentsSDD(
			@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		Resource file = customerService.getAttachmentsSDD(attachmentId);
		if (file == null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			return ResponseEntity.ok().headers(headers).body(file);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + file.getFilename());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(file);

	}
	
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/izosdwan/sdd/download/tempdownloadurl/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getAttachmentsTempUrlSDD(
			@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		String tempUrl = customerService.getAttachmentTempDownloadUrl(attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUrl,
		         Status.SUCCESS);
	}
	/**
	 *
	 * This method whether a customer le account is verified or not
	 *
	 * @return {@link Map<String,String>}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LE_VERIFICATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/le/{customerLeId}/verification", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String,String>> getCustomerLeVerified(@PathVariable Integer customerLeId) {
		Map<String,String> reponse = customerService.getCustomerLeVerification(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, reponse, Status.SUCCESS);
	}

	/**
	 * Get SECS Code By Customer Le ID
	 *
	 * @param customerLeId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LE_VERIFICATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/le/{customerLeId}/secs", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Set<String>> getCustomerLeSecsCode(@PathVariable Integer customerLeId) {
		Set<String> reponse = customerService.getCustomerLeSecsCode(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, reponse, Status.SUCCESS);
	}
	
	/**
	 * API to get the Support Matrix file
	 * 
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@GetMapping(value = "/supportMatrix/downloadFile")
	public void downloadSupportMatrixFile(HttpServletResponse response) throws TclCommonException, IOException {
		try {
			Attachment attachment =customerService.getAttachmentDetailsForCustLe();
			if(Objects.nonNull(attachment)) {
				InputStream input=customerService.getSupportMatrixAttachment(attachment);
				response.setContentType("application/pdf");
				response.setHeader("Content-disposition","attachment;filename=" + attachment.getDisplayName() + ".pdf");
				org.apache.commons.io.IOUtils.copy(input, response.getOutputStream());
				response.flushBuffer();
			}
		} catch (IOException ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	/**
	 * API to upload customer attachment
	 * @param file
	 * @param attachmentName
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_SUPPORT_MATRIX)
	@RequestMapping(value = "/upload/supportMatrix", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<AttachmentBean> uploadSupportMatrixFile(@RequestParam("file") MultipartFile file, @RequestParam("attachmentName") String attachmentName) throws TclCommonException, IOException {
		try {
			AttachmentBean attachmentDetails = customerService.uploadAttachmentDocument(file, attachmentName);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachmentDetails, Status.SUCCESS);
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		
	}

	/**
	 * Get Uploaded Documents By Secs ID
	 *
	 * @param custLegalId
	 * @return List<AttachmentBean>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_MSA_DOCUMENT)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@RequestMapping(value = "/le/{customerLeId}/secs/{secsId}/attachments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AttachmentBean>> getMSADocumentsBySecsId(@PathVariable("customerLeId") Integer custLegalId, @PathVariable("secsId") Integer secsId,
																  @RequestParam(name = "productName") String productName) throws TclCommonException {
		List<AttachmentBean> attachments = customerService.getDocumentDetailsBySecsId(custLegalId, secsId, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachments,
				Status.SUCCESS);
	}

	/**
	 * This Method Takes Customer Id, product name, service and access type as input
	 * and it provides the details of currency,sple and INR Information specific to wholesale and partner
	 *
	 * @param customerLegalEntityId
	 * @param productName
	 * @param serviceNames
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_BY_ID)
	@RequestMapping(value = "/le/{customerLeId}/secs/{secsId}/product/{product}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Set<CustomerLegalEntityProductResponseDto>> getSupplierLegalEntityDetailsByCustomerLegalIdForService(
			@PathVariable("customerLeId") Integer customerLegalEntityId, @PathVariable("secsId") Integer secsId,
			@PathVariable("product") String product) throws TclCommonException {
		Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = customerService
				.getSupplierLegalEntityDetailsByCustomerLegalIdAndSecsdId(customerLegalEntityId, product, secsId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLegalEntityProductResponseDtos, Status.SUCCESS);
	}

	/**
	 * Method to get the temporary download url for TSA and SA documents uploaded to
	 * the storage container
	 *
	 * @param customerLeId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_TEMP_DOWNLOAD_URL)
	@RequestMapping(value = "/le/{customerLeId}/secs/{secsId}/tempdownloadurl/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getTempDownloadUrlForSecsId(@PathVariable("customerLeId") Integer customerLeId,
																@PathVariable("secsId") Integer secsId,
																@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = customerService.getTempDownloadUrlForSecsId(customerLeId, secsId, attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}
	

	/**
	 * * Method to get the temporary upload url for uploading any type of document to object storage. 
	 * This api captures the filename along with extension to send the attachment details to O2C
	 * @param customerLeId
	 * @param file
	 * @param orderToLeId
	 * @param quoteToLeId
	 * @param referenceId
	 * @param referenceName
	 * @param attachmentType
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_FILE_DOC)
	@RequestMapping(value = "/le/{customerLeId}/uploaddocument", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> upLoadAnyDocument(@PathVariable("customerLeId") Integer customerLeId,
			@RequestParam(name="file") MultipartFile file,
			@RequestParam(value = "orderToLeId", required = false) Integer orderToLeId,
			@RequestParam(value = "quoteToLeId", required = false) Integer quoteToLeId,
			@RequestParam("referenceId") List<Integer> referenceId, @RequestParam("referenceName") String referenceName,
			@RequestParam("attachmentType") String attachmentType, @RequestParam(value = "productName", required = false) String productName) throws TclCommonException {
		ServiceResponse response = customerService.processUploadAnyDocument(file, orderToLeId, quoteToLeId, attachmentType,
				referenceId, referenceName, customerLeId,productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	
	/**
	 * This method captures the attachment related details for amy type of document uploaded. The filename along with extension is captured to pass these details to o2c
	 * @param customerLeId
	 * @param documentBean
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.Customer.UPDATE_DOCUMENT_UPLOADED_DETAILS)
	@RequestMapping(value = "/le/{customerLeId}/anydocumentuploaded", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> updateAnyDocumentUploadedDetails(
			@PathVariable("customerLeId") Integer customerLeId, @RequestBody DocumentBean documentBean) throws TclCommonException {
		ServiceResponse response = customerService.updateDocumentUploadedDetails(documentBean.getOrderToLeId(), documentBean.getQuoteToLeId(), documentBean.getReferenceId(),
				documentBean.getReferenceName(), documentBean.getRequestId(), documentBean.getAttachmentType(), documentBean.getUrl());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	/**
	 * Method to get the customer team members based on customer id and team role -customer success manager
	 * @param customerId
	 * @param teamRole
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_TEAM_MEMBER)
	@RequestMapping(value = "/team/members/{customerId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LeOwnerDetailsSfdc>> getCustomerTeamMember(@PathVariable("customerId") Integer customerLeId) throws TclCommonException {
		List<LeOwnerDetailsSfdc> teamMembersList = customerService.getCustomeTeamMemberDetails(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, teamMembersList,
				Status.SUCCESS);
	}






	@PostMapping(value = "/{customerId}/attributes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLeAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<List<CustomerLeAttributeBean>> saveAndUpdateCustomerAttributes(@PathVariable("customerId") Integer customerId, @RequestBody List<CustomerLeAttributeBean> attributes) {
		List<CustomerLeAttributeBean> customerLeAttributeBeans =  customerService.saveOrUpdateCustomerAttributes(customerId, attributes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLeAttributeBeans,
				Status.SUCCESS);
	}
	
	


}



