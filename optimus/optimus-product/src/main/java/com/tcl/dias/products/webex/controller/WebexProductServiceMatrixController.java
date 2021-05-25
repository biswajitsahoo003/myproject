package com.tcl.dias.products.webex.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.products.swagger.constants.SwaggerConstants;
import com.tcl.dias.products.webex.beans.WebexProductCountriesBean;
import com.tcl.dias.products.webex.beans.WebexProductLocationBean;
import com.tcl.dias.products.webex.service.WebexProductServiceMatrixService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class for Webex product related operations
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/products/webex/services", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebexProductServiceMatrixController {

	@Autowired
	WebexProductServiceMatrixService webexProductServiceMatrixService;

	/**
	 * Get countries for given product
	 *
	 * @param productName
	 * @return {@link WebexProductLocationBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_WEBEX_COUNTRY_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexProductLocationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/{type}/{productName}/{bridgeRegion}/{paymentModel}/countries")
	public ResponseResource<WebexProductLocationBean> getCountries(@PathVariable("type") String type,
			@PathVariable("productName") String productName, @PathVariable("bridgeRegion") String bridgeRegion,
			@PathVariable("paymentModel") String paymentModel, @RequestParam String accessType,
			@RequestParam(required = false) Boolean isBridgeCountryDialOut) throws TclCommonException {
		WebexProductLocationBean response = webexProductServiceMatrixService.getCountries(type, productName,
				paymentModel, bridgeRegion, accessType, isBridgeCountryDialOut);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get all countries
	 *
	 * @param productName
	 * @return {@link WebexProductLocationBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_WEBEX_COUNTRY_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexProductCountriesBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/{productName}/all")
	public ResponseResource<WebexProductCountriesBean> getAllCountries(@PathVariable("productName") String productName)
			throws TclCommonException {
		WebexProductCountriesBean response = webexProductServiceMatrixService.getAllCountries(productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Download Webex price list for given product based on file type
	 *
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.DOWNLOAD_WEBEX_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{audioGreeting}/download/{productName}/{paymentModel}/prices/{filetype}")
	public ResponseEntity<HttpServletResponse> downloadPrices(HttpServletResponse response,
			@PathVariable("audioGreeting") String audioGreeting, @PathVariable("productName") String productName,
			@PathVariable("paymentModel") String paymentModel, @PathVariable("filetype") String fileType,
			@RequestParam(required = false) String bridge,
			@RequestParam(required = false) Boolean isBridgeCountryDialOut)
			throws IOException, DocumentException, TclCommonException {
		response = webexProductServiceMatrixService.downloadAllPrices(response, audioGreeting, productName,
				paymentModel, fileType, isBridgeCountryDialOut, bridge);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return ResponseEntity.ok().headers(headers).body(response);
	}
}
