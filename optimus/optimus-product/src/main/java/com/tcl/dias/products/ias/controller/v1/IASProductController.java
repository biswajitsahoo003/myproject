package com.tcl.dias.products.ias.controller.v1;

import java.util.List;

import com.tcl.dias.products.dto.DataCenterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.productcatelog.entity.entities.IasPriceBook;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.ias.service.v1.IASProductService;
import com.tcl.dias.products.swagger.constants.SwaggerConstants.ApiOperations.Products;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class for IAS product related services
 * 
 *
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/products/IAS")
public class IASProductController {

	@Autowired
	public IASProductService iasProductService;

	/**
	 * Method to get pricing details
	 * 
	 * @author Dinahar Vivekanandan
	 * @return ResponseResource<List<IasPriceBook>>
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Products.GET_PRICING_DETAILS)
	@RequestMapping(value = "/pricelist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = IasPriceBook.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<IasPriceBook>> getPricingDetails() throws TclCommonException {

		List<IasPriceBook> response = iasProductService.getPrice();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getSlaValue - This method is to get SLA value for a product offering
	 * 
	 * @author Rakesh 
	 * 
	 * @param productOfferingId
	 * @param serviceVariantId
	 * @param popRegionId - to be received from feasibility engine
	 * @param accessTypeId
	 * @param destinationId
	 * @return ResponseResource<List<SLADto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_SLA_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SLADto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/profiles/{profileId}/service-variants/{serviceVariantId}/sladetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SLADto>> getSlaValue(@PathVariable("profileId") Integer productOfferingId,
			@PathVariable("serviceVariantId") Integer serviceVariantId, @RequestParam("popRegionId") Integer popRegionId,
			@RequestParam("accessTypeId") Integer accessTypeId, @RequestParam("destinationId") Integer destinationId)
			throws TclCommonException {
		List<SLADto> response = iasProductService.getSlaValue(productOfferingId, serviceVariantId, popRegionId,accessTypeId,destinationId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	@ApiOperation(value = Products.GET_NPL_DATA_CENTER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SLADto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/dataCenterDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<DataCenterBean>> getDcDetailsForIas(@RequestParam(value = "cityName", required = false) final String cityName) throws TclCommonException {
		List<DataCenterBean> dataCenterBeans = iasProductService.getDcDetailsForIas(cityName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, dataCenterBeans,
				Status.SUCCESS);
	}

}
