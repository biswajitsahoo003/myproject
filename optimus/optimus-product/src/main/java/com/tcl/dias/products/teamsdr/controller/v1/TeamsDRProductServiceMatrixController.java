package com.tcl.dias.products.teamsdr.controller.v1;

import java.util.List;
import java.util.Set;

import com.tcl.dias.products.teamsdr.beans.TeamsDRMediaGatewayBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.products.swagger.constants.SwaggerConstants;
import com.tcl.dias.products.teamsdr.beans.TeamsDRCityDetailBean;
import com.tcl.dias.products.teamsdr.beans.TeamsDRCountryDetailBean;
import com.tcl.dias.products.teamsdr.beans.TeamsDRLicenseInfoProviderBean;
import com.tcl.dias.products.teamsdr.service.v1.TeamsDRProductServiceMatrixService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/v1/products/teamsdr", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamsDRProductServiceMatrixController {

	@Autowired
	TeamsDRProductServiceMatrixService teamsdrProductServiceMatrixService;

	/**
	 * Controller to get all teamsdr Countries
	 *
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_TEAMSDR_COUNTRIES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRCountryDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/countries")
	public ResponseResource<List<TeamsDRCountryDetailBean>> getCountries(@RequestParam Boolean isTataVoiceAvailable) throws TclCommonException {
		List<TeamsDRCountryDetailBean> response = teamsdrProductServiceMatrixService.getCountries(isTataVoiceAvailable);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get all cities based on given country.
	 * 
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_TEAMSDR_CITIES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRCityDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/cities")
	public ResponseResource<List<TeamsDRCityDetailBean>> getCitiesBasedOnCountry(@RequestParam String country) {
		List<TeamsDRCityDetailBean> response = teamsdrProductServiceMatrixService.getAllCitiesBasedOncountry(country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get license agreement type
	 *
	 * @return
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRCityDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/license/agreementtypes")
	public ResponseResource<Set<String>> getLicenseAgreementTypes() {
		Set<String> response = teamsdrProductServiceMatrixService.getLicenseAgreementTypes();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get license info (description, min/max seats) for tooltips and
	 * validation
	 *
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getlicenseinfo")
	public ResponseResource<List<TeamsDRLicenseInfoProviderBean>> getLicenseByCountries() throws TclCommonException {
		List<TeamsDRLicenseInfoProviderBean> response = teamsdrProductServiceMatrixService.getLicenseInfo();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get all media gateways based on type (sip/tdm)
	 *
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Products.GET_TEAMSDR_MEDIAGATEWAYS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRMediaGatewayBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/mediagateway")
	public ResponseResource<List<TeamsDRMediaGatewayBean>> getMediaGateways(@RequestParam(name = "type") String type,
																		@RequestParam(name = "value") Integer value,
																		@RequestParam(name = "isRedundant") String isRedundant){
		List<TeamsDRMediaGatewayBean> response = teamsdrProductServiceMatrixService.getMediaGateways(type, value, isRedundant);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
