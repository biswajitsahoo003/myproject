package com.tcl.dias.customer.isv.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.common.keycloack.bean.KeyCloackRoles;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.CustomerAttachmentBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.LegalEntityBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.bean.CustomerLegalEntityBean;
import com.tcl.dias.customer.dto.AttributesDto;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityDto;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.dias.customer.service.v1.PartnerCustomerService;
import com.tcl.dias.customer.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/isv/v1")
public class InternalStakeViewController {

	@Autowired
	CustomerService customerService;
	
	@Autowired
	PartnerCustomerService partnerCustomerService;

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/customers/le", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LegalEntityBean>> getCustomerLegalEntityDetailsByCustomerId(
			@RequestBody List<Integer> customerIds) throws TclCommonException {
		List<LegalEntityBean> customerLegalEntityDto = customerService.getLegalEntitiesForTheCustomers(customerIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/customers/getallcustomer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerDto>> getAllCustomer() throws TclCommonException {
		List<CustomerDto> response = customerService.getAllCustomer();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
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
	@RequestMapping(value = "/customers/le/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerAttachmentBean> upLoadLegalEntityFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam("referenceName") String referenceName, @RequestParam("attachmentType") String attachmentType,
			@RequestParam(name = "productName", required = false) String productName) throws TclCommonException {

		CustomerAttachmentBean response = customerService.uploadMSASSDocuments(file, customerLeId, referenceName,
				attachmentType, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Method to save the uploaded storage container document request Id for
	 * reference.
	 * 
	 * @param requestId
	 * @param customerLeId
	 * @param attachmentType
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID)
	@RequestMapping(value = "/customers/le/msassuploaded", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> saveOrUpdateDocumentUploadedToContainer(@RequestParam("requestId") String requestId,
			@RequestParam(value = "customerLeId", required = false) Integer customerLeId,
			@RequestParam("attachmentType") String attachmentType,
			@RequestParam(name = "productName") String productName, @RequestParam(name = "url") String url)
			throws TclCommonException {
		String response = customerService.updateMSASSDocumentsUploadedToContainer(requestId, customerLeId,
				attachmentType, productName, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/customers/download/countrySpecificFiles/{attachmentId}", method = RequestMethod.GET)
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
	@RequestMapping(value = "/customers/download/tempdownloadurl/countrySpecificFiles/{attachmentId}", method = RequestMethod.GET)
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
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_GSC_OUTBOUND_PRICE_DOCUMENT)
	@RequestMapping(value = "/customers/gsc/outbound/prices", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<Resource> downloadOutboundPrices(@RequestParam("quoteCode") String quoteCode,
														   @RequestParam("fileType") String fileType, @RequestParam("fileName") String fileName)
			throws TclCommonException {
		Resource file = customerService.getOutboundPrices(quoteCode,fileType,fileName);
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
	 * API to get the outbound prices.
	 *
	 * @param quoteCode
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/customers/gsc/object/outbound/prices", method = RequestMethod.GET)
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
			@PathVariable("customerId") @BaseArgument Integer customerId) throws TclCommonException {
		List<CustomerLegalEntityDto> customerLegalEntityDto = customerService
				.findCustomerEntityByCustomerId(customerId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityDto,
				Status.SUCCESS);

	}

	/**
	 * Method to search customer name
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.Customer.SEARCH_CUSTOMER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/searchCustomer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<CustomerDto>> getCustomerNameBySearch(@RequestParam(name = "searchValue",required = true) String searchValue)
			throws TclCommonException {
		List<CustomerDto> customerNames = customerService.getCustomerNameBySearch(searchValue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerNames,
				Status.SUCCESS);
	}
	
	/**
	 * Get all Customer legal entity with pagination and search
	 * getAllUser
	 * @param page
	 * @param size
	 * @param name
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_ALL_CUSTOMER_LE)
	@RequestMapping(value = "/customers/le/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<CustomerLegalEntityBean>> getAllLegalEntityWithPagination(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "customerId", required = false) Integer customerId,
			@RequestBody(required = false) List<Integer> customerLeIds) throws TclCommonException {
		PagedResult<CustomerLegalEntityBean> response = customerService.searchCustomerLegalEntity(name, page, size,customerId,customerLeIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * Get all Customer with pagination and search
	 * @param page
	 * @param size
	 * @param name
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_ALL_CUSTOMER_PAGE)
	@RequestMapping(value = "/customers/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<CustomerDetailBean>> getAllCustomerWithPagination(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "name", required = false) String name) throws TclCommonException {
		PagedResult<CustomerDetailBean> response = customerService.searchCustomer(name, page, size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get all Customer legal entity with pagination and search
	 * getAllUser
	 * @param page
	 * @param size
	 * @param name
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_ALL_CUSTOMER_LE)
	@RequestMapping(value = "/partner/le/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<PartnerLegalEntityBean>> getAllPartnerLegalEntityWithPagination(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "partnerId", required = false) Integer partnerId,
			@RequestBody(required = false) List<Integer> partnerLeIds) throws TclCommonException {
		PagedResult<PartnerLegalEntityBean> response = partnerCustomerService.searchPartnerLegalEntity(name, page, size,partnerId,partnerLeIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * Get all Partner with pagination and search
	 * @param page
	 * @param size
	 * @param name
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_ALL_CUSTOMER_PAGE)
	@RequestMapping(value = "/partner/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<PartnerDetailsBean>> getAllPartnerWithPagination(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "name", required = false) String name) throws TclCommonException {
		PagedResult<PartnerDetailsBean> response = partnerCustomerService.searchPartner(name, page, size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
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
	public ResponseResource<List<com.tcl.dias.customer.bean.AttachmentBean>> getMSADocuments(@PathVariable("customerLeId") Integer custLegalId,
			@RequestParam(name = "productName") String productName) throws TclCommonException {
		List<com.tcl.dias.customer.bean.AttachmentBean> attachments = customerService.getMSADocumentDetails(custLegalId, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachments,
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

	/**
	 * Download the attachment of file storage
	 *
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/download/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<Resource> getAttachments(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
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
	 * Get the temprdownlurl by attachment id
	 *
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
	@RequestMapping(value = "/download/tempdownloadurl/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getTempDownloadUrl(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = customerService.getAttachmentTempDownloadUrl(attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.Customer.UPLOAD_MSA_DOC)
	@RequestMapping(value = "/customers/le/{customerLeId}/secs/{secsId}/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CustomerAttachmentBean> uploadWholesaleLegalEntityFile(@PathVariable("customerLeId") Integer customerLeId, @PathVariable("secsId") Integer secsId,
																				   @RequestParam("file") MultipartFile file, @RequestParam("referenceName") String referenceName,
																				   @RequestParam("attachmentType") String attachmentType, @RequestParam(name = "productName") String productName) throws TclCommonException {

		CustomerAttachmentBean response = customerService.uploadWholesaleLegalEntityFile(file, customerLeId, secsId, referenceName,
				attachmentType, productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

}
