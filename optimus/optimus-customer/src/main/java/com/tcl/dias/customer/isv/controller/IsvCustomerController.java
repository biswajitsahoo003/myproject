package com.tcl.dias.customer.isv.controller;

import java.util.List;

import com.tcl.dias.common.beans.BillingContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.bean.ServiceResponse;
import com.tcl.dias.customer.dto.AttributesDto;
import com.tcl.dias.customer.dto.CustomerConatctInfoResponseDto;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.dias.customer.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
@RequestMapping("/isv/v1/customers")
public class IsvCustomerController {

	@Autowired
	CustomerService customerService;

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

	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/le/{customerLeId}/download/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<Resource> getAttachments(@PathVariable("customerLeId") Integer customerLeId,
			@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		Resource file = customerService.getAttachments(customerLeId,attachmentId);
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
			@RequestParam("file") MultipartFile file,
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
	 * This Method gets Billing Details from customer le in isv
	 *
	 * @param custLegalId
	 * @throws TclCommonException
	 */
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


}
