package com.tcl.dias.billing.controller.v1;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.tcl.dias.billing.service.v1.FinanceService;
import com.tcl.dias.billing.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/finance/v1")
public class FinanceController {

	@Autowired
	FinanceService financeService;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * API to get all the invoices for current logged in user.
	 * 
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Finance.GET_ALL_FINANCE_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/customer/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getAllCustomerLedgerDetails(@RequestParam(required = false, value = "param") String param) throws TclCommonException {
		List<Map<String, Object>> financeDeatils = financeService.getAllCustomerLedgerDetails(param);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, financeDeatils,
				Status.SUCCESS);
	}

	/**
	 * API to get all open transactions (SOA)- INVOICE,
	 * PAYMENTS,TDS_PROVISIONS,CREDIT NOTE in a pdf
	 * 
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Finance.GET_ALL_SOA_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/customer/statementOfAccountsPDF", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> getAllOpenTxnsPDF(@RequestParam(required = true, value = "sapCode") String sapCode)
			throws TclCommonException {

		String paramArr[] = { "INVOICE", "PAYMENT", "TDS?PROVISION", "CREDIT NOTE", "TDS RECEIVED" };
		List<String> paramList = Arrays.stream(paramArr).collect(Collectors.toList());
		Map<String, Object> variable = objectMapper.convertValue(financeService.getSOA_PDF(paramList, sapCode),
				Map.class);

		Context context = new Context();
		context.setVariables(variable);
		String fileName = "Satement Of Account " + ".pdf";
		String html = templateEngine.process("sof_template", context);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			PDFGenerator.createPdf(html, bos);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		byte[] pdfBytes = bos.toByteArray();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		// Here you have to set the actual filename of your pdf
		headers.setContentDispositionFormData(fileName, fileName);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		return response;

	}

}
