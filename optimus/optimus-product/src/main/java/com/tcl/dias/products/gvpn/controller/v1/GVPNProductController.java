package com.tcl.dias.products.gvpn.controller.v1;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.CpeBomGscDto;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.gvpn.service.v1.GVPNProductService;
import com.tcl.dias.products.swagger.constants.SwaggerConstants.ApiOperations.Products;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for GVPN product related operations
 * 
 * TO DO: Implementation yet to be done - Structure alone created
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/products/GVPN")
public class GVPNProductController {

	@Autowired
	GVPNProductService gvpnProductService;

	/**
	 * TODO : Implementation of Round Trip Delay has to be done - Pending clarification
	 * 
	 * @param gvpnSlaRequest
	 * @return ResponseResource<List<SLADto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_SLA_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SLADto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sladetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProductSlaBean> getSlaValue()
			throws TclCommonException {
		String request = "Resilient, 1";
		ProductSlaBean response = gvpnProductService.processProductSla(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	/**
	 * getCpeBomDetails
	 * 
	 * Method to retrieve CPE BOM details for the selected profile
	 * 
	 * @author Paulraj
	 * @param bandwidth
	 * @param portInterfaceId
	 * @param routingProtocolId
	 * @return ResponseResource<List<String>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_CPE_BOM_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//  Changed since UI is not storing the product and profile id 
//	@RequestMapping(value = "/{productId}/profiles/{profileId}/CPE-BOM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/cpebom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<List<CpeBomDto>> getCpeBom( @RequestParam("bandwidth") Double bandwidth,
				@RequestParam("portInterface") String portInterface,
				@RequestParam("routingProtocol") String routingProtocol,@RequestParam("cpeManagementOption") String cpeManagementOption,@RequestParam(value = "type", required = false) String type) throws TclCommonException {
		List<CpeBomDto> response = gvpnProductService.getCpeBom(bandwidth, portInterface, routingProtocol,cpeManagementOption,type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * getInternationalCpeBomDetails
	 * 
	 * Method to retrieve CPE BOM details for the selected profile
	 * @param bandwidth
	 * @param portInterfaceId
	 * @param routingProtocolId
	 * @return ResponseResource<List<String>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_CPE_BOM_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cpebomintl", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<List<CpeBomDto>> getCpeBomIntl( @RequestParam("bandwidth") Double bandwidth,
																@RequestParam("portInterface") String portInterface,
																@RequestParam("routingProtocol") String routingProtocol,
																@RequestParam("cpeManagementOption") String cpeManagementOption,
																@RequestParam("cpeServiceConfig") String cpeServiceConfig,
																@RequestParam(value = "type", required = false) String type) throws TclCommonException {
		List<CpeBomDto> response = gvpnProductService.getCpeBomInternational(bandwidth, portInterface, routingProtocol, cpeManagementOption, cpeServiceConfig, type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to retrieve CPE BOM details for the gsc gvpn
	 *
	 *
	 * @param bandwidth
	 * @param cubeLicenses
	 * @param noOfMFTCards
	 *
	 * @return {@link List< CpeBomGscDto >}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_CPE_BOM_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cpebomGscGvpn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<CpeBomGscDto>> getCpeBomForGscGvpn(@RequestParam("bandwidth") String bandwidth,
																	@RequestParam(value = "typeOfCpe", required = false) String typeOfCpe,
																	@RequestParam(value = "cubeLicenses", required = false) String cubeLicenses,
																	@RequestParam(value = "noOfMFTCards", required = false) String noOfMFTCards ) throws TclCommonException {
		List<CpeBomGscDto> response = gvpnProductService.getCpeBomForGscGvpn(bandwidth, typeOfCpe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * Method to retrieve power cable against each country
	 *
	 *
	 * @param country
	 *
	 * @return {@link CpeBomGscDto}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_CPE_BOM_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gscCpePowerCable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CpeBomGscDto> getGscCpePowerCableDetails(@RequestParam("country") String country) throws TclCommonException {
		CpeBomGscDto response = gvpnProductService.getGscCpePowerCableDetails(country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * Method to retrieve power cable against each country
	 *
	 *
	 * @param country
	 *
	 * @return {@link CpeBomGscDto}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_CPE_BOM_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gscCpeDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CpeBomGscDto> getGscCpeDetails(@RequestParam("country") String country,
														   @RequestParam("bomName") String bomName,
														   @RequestParam("portInterface") String portInterface,
														   @RequestParam("routingProtocol") String routingProtocol){
		CpeBomGscDto response = gvpnProductService.getGscCpeDetails(country,bomName, portInterface,routingProtocol);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}


}
