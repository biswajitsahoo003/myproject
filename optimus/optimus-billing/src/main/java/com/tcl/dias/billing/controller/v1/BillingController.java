package com.tcl.dias.billing.controller.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.billing.beans.InvoiceSearchRequestBean;
import com.tcl.dias.billing.service.v1.BillingService;
import com.tcl.dias.billing.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the BillingController class.
 * 
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/billing/v1")
public class BillingController {

	@Autowired
	BillingService billingService;
	/**
	 * API to get all the invoices for current logged in user.
	 * 
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.FECTH_ALL_INVOICES_FOR_USER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/invoices", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getAllInvoices(@RequestParam(required = false) String classification) throws TclCommonException {
		List<Map<String, Object>> invoices = billingService.getInvoicesForUser(new ArrayList<Integer>(), classification);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, invoices,
				Status.SUCCESS);
	}

	/**
	 * API to get all the invoices for current logged in user.
	 * 
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.FECTH_ALL_INVOICES_FOR_USER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/invoices/{leID}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getAllInvoicesForUser(@PathVariable("leID") int leID, @RequestParam(required = false) String classification)
			throws TclCommonException {
		List<Map<String, Object>> invoices = billingService.getInvoicesForUser(Arrays.asList(leID), classification);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, invoices,
				Status.SUCCESS);
	}

	/**
	 * API to get a particular Invoice for User.
	 * 
	 * @param invoiceId
	 * @return {@Response}
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.GET_INVOICE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/viewInvoice")
	public ResponseEntity<byte[]> viewInvoice(@RequestBody Map<String, String> invoiceDetail) throws TclCommonException {
		byte[] pdfBytes= billingService.downloadInvoice(invoiceDetail);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
		headers.setContentDispositionFormData("fileName", invoiceDetail.get("fileName"));
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		return response;
	}

	/**
	 * API to get a particular Invoice for User.
	 * 
	 * @param invoiceId
	 * @return {@Response}
	 * @throws TclCommonException
	 * @throws IOException
	 */
	/*@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.GET_INVOICE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/downloadFile")
	public ResponseEntity<byte[]> downloadInvoice(@RequestBody Map<String, String> invoiceDetail)
			throws TclCommonException {
		byte[] pdfBytes = billingService.downloadInvoice(invoiceDetail);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
		// Here you have to set the actual filename of your pdf
		headers.setContentDispositionFormData("fileName", invoiceDetail.get("fileName"));
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		return response;
	}*/
	
	/**
	 * API to get a particular Invoice for User.
	 * 
	 * @param invoiceId
	 * @return {@Response}
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.GET_INVOICE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/downloadFile")
	public ResponseEntity<byte[]> downloadInvoice(
			@RequestParam("source") String source,
			@RequestParam("fileName") String fileName, 
			@RequestParam("filePath") String filePath,
			@RequestParam(value="serviceType", required=false) String serviceType,
			@RequestParam(value="invoiceNo") String invoiceNo)
			throws TclCommonException {
		Map<String,String> newFileName=new HashMap<>();
		byte[] pdfBytes = billingService.downloadInvoice(source, fileName, filePath,serviceType,invoiceNo, newFileName);
		//ByteArrayResource resource = new ByteArrayResource(pdfBytes);
		if(newFileName.get("fileName")!=null && newFileName.get("fileName")!="" && !newFileName.get("fileName").isEmpty()) {
			fileName=newFileName.get("fileName");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
		headers.setContentDispositionFormData("fileName",fileName);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		return response;
	}

	/**
	 * API to get a particular Invoice for User.
	 * 
	 * @param invoiceId
	 * @return {@Response}
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.GET_MULTIPLE_INVOICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/downloadinvoices")
	public ResponseEntity<byte[]> downloadInvoices(@RequestBody Map<String, List<Map<String, String>>> invoiceDetails)
			throws TclCommonException, IOException {

		List<Map<String, String>> invoiceList = invoiceDetails.get("invoices");

		byte[] pdfBytes = billingService.downloadInvoices(invoiceList);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/zip"));
		// Here you have to set the actual filename of your pdf
		headers.setContentDispositionFormData("fileName", "invoices.zip");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		return response;
	}
	
	
	/**
	 * API to get all the service Ids with details for the given InvoiceNo.
	 * 
	 * @param invoiceNo
	 * @return {@Response} contains ServiceId with the details.
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.FECTH_ALL_INVOICES_FOR_USER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/details/{invoiceNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getAllServiceIdsForAInvoice(@PathVariable("invoiceNo") String invoiceNo) throws TclCommonException {
		List<Map<String, Object>> invoices = billingService.getAllServiceIds(invoiceNo);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, invoices,
				Status.SUCCESS);
	}
	
	
	/**
	 * API to search the invoice details using InvoiceNo or ServiceId or PO Number or Alias.
	 * 
	 * @param bean, InvoiceSearchRequestBean contains the input details.
	 * @param invoiceNo
	 * @param serviceId
	 * @param poNumber
	 * @param alias
	 * @return {@Response} contains ServiceId with the details.
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.FECTH_ALL_INVOICES_FOR_USER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/searchBy", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getAllnvoiceIds(@RequestBody InvoiceSearchRequestBean bean,
			@RequestParam(value="invoiceNo", required=false) String invoiceNo,
			@RequestParam(value="serviceId", required=false) String serviceId,
			@RequestParam(value="poNumber", required=false) String poNumber,
			@RequestParam(value="alias", required=false) String alias) throws TclCommonException {
		List<Map<String, Object>> invoices = billingService.searchByParam(invoiceNo, serviceId, poNumber, alias, bean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, invoices,
				Status.SUCCESS);
	}

	/**
	 * API to get filtered invoices for logged in partner user.
	 *
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Billing.FECTH_ALL_INVOICES_FOR_USER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/partner/invoices", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getAllInvoicesForPartnerUser(@RequestParam(required = false,value="partnerLeId") Integer partnerLeId,@RequestParam(required = false,value="customerLeId") Integer customerLeId, @RequestParam(required = false,value="classification") String classification)
			throws TclCommonException {
		List<Map<String, Object>> invoices = billingService.getInvoicesForPartnerUser(partnerLeId,customerLeId, classification);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, invoices,
				Status.SUCCESS);
	}
}
