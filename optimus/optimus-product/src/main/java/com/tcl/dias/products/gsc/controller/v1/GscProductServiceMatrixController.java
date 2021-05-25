package com.tcl.dias.products.gsc.controller.v1;

import java.io.IOException;
import java.util.List;

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
import com.tcl.dias.products.gsc.beans.GscProductLocationBean;
import com.tcl.dias.products.gsc.beans.GscServiceMatrixBean;
import com.tcl.dias.products.gsc.service.v1.GscOutboundPricingService;
import com.tcl.dias.products.gsc.service.v1.GscProductServiceMatrixService;
import com.tcl.dias.products.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class for GSC product related operations
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/products/gsc/services", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class GscProductServiceMatrixController {

	@Autowired
	GscProductServiceMatrixService gscProductServiceMatrixService;

	@Autowired
	GscOutboundPricingService gscOutboundPricingService;

	/**
	 * Get All Country for given product
	 *
	 * @param productName
	 * @return {@link GscProductLocationBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_GSC_COUNTRY_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscServiceMatrixBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/{productName}/countries")
	public ResponseResource<GscProductLocationBean> getCountries(@PathVariable("productName") String productName,
			@RequestParam String accessType, @RequestParam(required = false) String secsId,
																 @RequestParam(required = false) String partnerLeId) throws TclCommonException {
		GscProductLocationBean response = gscProductServiceMatrixService.getCountries(productName, accessType, secsId, partnerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get all city location by given product and country name
	 *
	 * @param productName
	 * @param country
	 * @return {@link GscProductLocationBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_GSC_CITY_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscServiceMatrixBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{productName}/countries/{country}/cities")
	public ResponseResource<GscProductLocationBean> getCities(@PathVariable("productName") String productName,
			@PathVariable("country") String country) throws TclCommonException {
		GscProductLocationBean response = gscProductServiceMatrixService.getCities(productName, country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get service area matrix for given product
	 *
	 * @param productName
	 * @param country
	 * @return {@link GscServiceMatrixBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_GSC_COUNTRY_SERIVE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscServiceMatrixBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{productName}/{country}/matrix")
	public ResponseResource<GscServiceMatrixBean> getServices(@PathVariable("productName") String productName,
			@PathVariable("country") String country) throws TclCommonException {
		GscServiceMatrixBean response = gscProductServiceMatrixService.getServiceMatrix(productName, country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Download GSC outbound prices based on file type
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.DOWNLOAD_OUTBOUND_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/download/outbound/prices/{filetype}")
	public ResponseEntity<HttpServletResponse> downloadOutboundPrices(HttpServletResponse response,
			@PathVariable("filetype") String fileType) throws IOException, DocumentException, TclCommonException {
		response = gscOutboundPricingService.downloadOutboundPrices(response, fileType);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return ResponseEntity.ok().headers(headers).body(response);
	}

	/**
	 * Get All applicable services
	 *
	 * @param secsId
	 * @return {@link GscProductLocationBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_GSC_COUNTRY_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscServiceMatrixBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/names")
	public ResponseResource<List<String>> getProductNames(@RequestParam(required = false) String secsId,
														  @RequestParam(required = false) String partnerLeId) throws TclCommonException {
		List<String> response = gscProductServiceMatrixService.getProductNames(secsId, partnerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
