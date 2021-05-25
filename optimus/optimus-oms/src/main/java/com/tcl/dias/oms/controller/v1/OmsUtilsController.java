package com.tcl.dias.oms.controller.v1;

import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.oms.gsc.service.v1.GlobalOutboundRateCardService;
import com.tcl.dias.oms.gsc.service.v1.GscProductCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the DashboardController.java class.
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/utils")
public class OmsUtilsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsUtilsController.class);

	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	GlobalOutboundRateCardService globalOutboundRateCardService;

	@Autowired
	GscProductCatalogService gscProductCatalogService;

	/**
	 * download Enrichment details in PDF
	 *	
	 * @param response
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 * @throws GeneralSecurityException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_ORDER_ENRICHMENT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/orderenrichment")
	public ResponseResource<String> downloadEnrichmentDetails(HttpServletResponse response,
			@RequestParam("ikey") String hashKey) throws TclCommonException {
		try {
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String quoteCode = splitter[0];
				String username = splitter[1];
				LOGGER.info("quote Code is {} and Username {}", quoteCode, username);
				illQuoteService.downloadEnrichmentDetails(response, quoteCode);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						Status.SUCCESS.toString(), Status.SUCCESS);
			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.FAILURE.toString(),
						Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting downloadEnrichmentDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * download Outbound Prices in PDF
	 *
	 * @param response
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 * @throws GeneralSecurityException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_ORDER_ENRICHMENT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/prices")
	public ResponseResource<HttpServletResponse> downloadOutboundPrices(HttpServletResponse response,
															  @RequestParam("ikey") String hashKey) throws TclCommonException {
		try {
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String quoteCode = splitter[0];
				String username = splitter[1];
				LOGGER.info("quote Code is {} and Username {}", quoteCode, username);
				globalOutboundRateCardService.getOutboundPricesFile(response, quoteCode, null, (byte) 0);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting downloadEnrichmentDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * download Surcharge Prices in PDF
	 *
	 * @param response
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 * @throws GeneralSecurityException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_ORDER_ENRICHMENT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/surcharge/prices")
	public ResponseResource<HttpServletResponse> downloadSurchargePrices(HttpServletResponse response,
														   @RequestParam("ikey") String hashKey) throws TclCommonException {
		try {
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String quoteCode = splitter[0];
				String username = splitter[1];
				LOGGER.info("quote Code is {} and Username {}", quoteCode, username);
				gscProductCatalogService.downloadOutboundSurchargePrices(response).get();
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting downloadEnrichmentDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * download saved outbound or surcharge prices PDF (File Strorage)
	 *
	 * @param response
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 * @throws GeneralSecurityException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_ORDER_ENRICHMENT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/object/outbound/prices")
	public ResponseResource<HttpServletResponse> downloadSavedOutboundOrSurchargePrices(HttpServletResponse response,
																		   @RequestParam("ikey") String hashKey,@RequestParam("fileType") String fileType, @RequestParam("fileName") String fileName) throws TclCommonException {
		try {
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String quoteCode = splitter[0];
				String username = splitter[1];
				LOGGER.info("quote Code is {} and Username {}", quoteCode, username);
				globalOutboundRateCardService.getSavedOutboundPricesFileFromObjectStorage(response,quoteCode, fileType, fileName);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						response, Status.SUCCESS);
			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
						Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting downloadEnrichmentDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * download domestic outbound prices pdf
	 *
	 * @param response
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 * @throws GeneralSecurityException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_ORDER_ENRICHMENT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/domestic/outbound/prices")
	public ResponseResource<HttpServletResponse> downloadDomesticOutboundPrices(HttpServletResponse response,
																		 @RequestParam("ikey") String hashKey) throws TclCommonException {
		try {
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String quoteCode = splitter[0];
				String username = splitter[1];
				LOGGER.info("quote Code is {} and Username {}", quoteCode, username);
				globalOutboundRateCardService.downloadDomesticOutboundPrices(response, quoteCode);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting downloadEnrichmentDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * download saved domestic outbound prices PDF (Object storage)
	 *
	 * @param response
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 * @throws GeneralSecurityException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_ORDER_ENRICHMENT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/object/domestic/outbound/prices/file")
	public ResponseResource<HttpServletResponse> downloadSavedDomesticOutboundPrices(HttpServletResponse response,
																		   @RequestParam("ikey") String hashKey) throws TclCommonException {
		try {
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String quoteCode = splitter[0];
				String username = splitter[1];
				LOGGER.info("quote Code is {} and Username {}", quoteCode, username);
				globalOutboundRateCardService.getSavedDomesticOutboundPricesFileFromObjectStorage(response,quoteCode);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						response, Status.SUCCESS);
			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
						Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting downloadEnrichmentDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
}
