package com.tcl.dias.products.npl.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.products.dto.DataCenterBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.dto.ServiceAreaMatrixNPLDto;
import com.tcl.dias.products.npl.service.v1.NPLProductService;
import com.tcl.dias.products.swagger.constants.SwaggerConstants.ApiOperations.Products;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class for NPL product related operations
 * 
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/products/npl", produces = MediaType.APPLICATION_JSON_VALUE)
public class NPLProductController {

	@Autowired
	NPLProductService nplProductService;

	/**
	 * SLA values for NPL product
	 * 
	 * @param serviceVariant Value of service variant
	 * @param accessTopology Value of access topology
	 * @return ResponseResource<List<SLADto>> Returns the SLA values of NPL product
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_SLA_VALUE_NPL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SLADto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sladetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SLADto>> getSlaValue(
			@RequestParam(value = "serviceVarient", required = true) String serviceVarient,
			@RequestParam(value = "accessTopology", required = true) String accessTopology) throws TclCommonException {
		List<SLADto> response = nplProductService.getSlaValue(serviceVarient, accessTopology);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Minimum assured networkUptime for NPL product
	 * 
	 * @param serviceVariant Value of service variant
	 * @param accessTopology Value of access topology
	 * @return ResponseResource<String> Returns the minimal network uptime value for NPL product
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_NPL_ASSURED_UPTIME)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SLADto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/assured-uptime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getMinimalUptime() throws TclCommonException {
		String response = nplProductService.getMinimalUptime();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get NPL Pop Details for all city or else specific city
	 *
	 * @param cityName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_NPL_POP_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceAreaMatrixNPLDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/popdetails")
	public ResponseResource<List<ServiceAreaMatrixNPLDto>> getPopDetails(
			@RequestParam(value = "cityName", required = false) final String cityName) throws TclCommonException {

		final List<ServiceAreaMatrixNPLDto> response = nplProductService.getNplPopLocationDetails(cityName);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = Products.GET_NPL_DATA_CENTER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SLADto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/dataCenterDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<DataCenterBean>> getServiceAreaMatrixDc(@RequestParam(value = "cityName", required = false) final String cityName) throws TclCommonException {
		List<DataCenterBean> dataCenterBeans = nplProductService.getServiceAreaMatrixDc(cityName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, dataCenterBeans,
				Status.SUCCESS);
	}
	@ApiOperation(value = Products.GET_CITY_LIST_DATA_CENTER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/dc/cites", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getCitiesForDataCenter() throws TclCommonException {
		List<String> cities = nplProductService.getCitiesForDataCenter();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, cities,
				Status.SUCCESS);
	}
	
}
