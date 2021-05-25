package com.tcl.dias.products.izosdwan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.IzoSdwanCpeBomInterface;
import com.tcl.dias.common.beans.IzosdwanBandwidthInterface;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.beans.VproxyAttributeDetails;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.beans.VutmProfileDetailsBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.productcatelog.entity.entities.VwSdwanBwIntfMapping;
import com.tcl.dias.products.dto.ProductDto;
import com.tcl.dias.products.izosdwan.beans.CpeDetails;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;
import com.tcl.dias.products.swagger.constants.SwaggerConstants.ApiOperations.Izosdwan;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 *This is the controller class for IZOSDWAN for Product related API's
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/products/izosdwan")
public class IzosdwanProductController {

	@Autowired
	IzosdwanProductService izosdwanProductService;
	
	/**
	 * 
	 * Get Product Offering details for SDWAN
	 * @author AnandhiV
	 * @param vendor
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Izosdwan.GET_ALL_PROFILES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/profiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseResource<List<VendorProfileDetailsBean>> getCpeValuesGvpn(@RequestParam("vendor") String vendor) throws TclCommonException {
		List<VendorProfileDetailsBean> response = izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor(vendor);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@RequestMapping(value = "/interface", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseResource<List<IzoSdwanCpeBomInterface>> getCpeBomInterface() throws TclCommonException {
		List<IzoSdwanCpeBomInterface> response = izosdwanProductService.getCpeBomInterface();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cpedetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<IzoSdwanCpeBomInterface>> getInterfaceDetails()
			throws TclCommonException {
		List<IzoSdwanCpeBomInterface> response = izosdwanProductService.getCpeBomInterface();
		return new ResponseResource<List<IzoSdwanCpeBomInterface>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	/**
	 *  @author mpalanis
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cofcpedetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CpeDetails> getCofCpeDetails(@RequestBody List<String> request)
			throws TclCommonException {
		CpeDetails response = izosdwanProductService.getCofCpeDetails(request);
		return new ResponseResource<CpeDetails>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sladetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getSlaTierValue(@RequestParam("siteTypeName") String siteTypeName,@RequestParam("cityName") String cityName,@RequestParam("productName") String productName,@RequestParam("vendorName") String vendorName,@RequestParam("countryName") String countryName)
			throws TclCommonException {
		String response = izosdwanProductService.getSlaTierDetails(siteTypeName, cityName, productName,countryName,vendorName);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	/**
	 * This Api is used to get all vroxy profiles information
	 * @author vpachava
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Izosdwan.GET_ALL_VPROXY_PROFILES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/vproxy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<VproxySolutionsBean>> getVproxySolutionDetails(@RequestBody List<VproxyAttributeDetails> request)
			throws TclCommonException {
		List<VproxySolutionsBean> response = izosdwanProductService.getVproxyProductDetails(request);
		return new ResponseResource<List<VproxySolutionsBean>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	

	/**
	 * 
	 * This API is used to get all VUTM profiles
	 * 
	 * @author AnandhiV
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Izosdwan.GET_ALL_VUTM_PROFILES)

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),

			@ApiResponse(code = 403, message = Constants.FORBIDDEN),

			@ApiResponse(code = 422, message = Constants.NOT_FOUND),

			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })

	@RequestMapping(value = "/vutm", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<VutmProfileDetailsBean>> getVutmSolutionDetails() throws TclCommonException {
		List<VutmProfileDetailsBean> response = izosdwanProductService.getVutmProfiles();
		return new ResponseResource<List<VutmProfileDetailsBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * 
	 * This method is used to get all vutm breakout location information
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Izosdwan.GET_ALL_VUTM_PROFILES)

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),

			@ApiResponse(code = 403, message = Constants.FORBIDDEN),

			@ApiResponse(code = 422, message = Constants.NOT_FOUND),

			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })

	@RequestMapping(value = "/vutm/breakoutlocations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getVutmBreakoutLocations() throws TclCommonException {
		List<String> response = izosdwanProductService.getVutmBreakOutLocations();
		return new ResponseResource<List<String>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	 
	/**
	 * This Api is used to get all vroxy profiles information
	 * @author nthangar
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Izosdwan.GET_ALL_VPROXY_PROFILES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bwinterfacetypemapping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<IzosdwanBandwidthInterface>> getVproxySolutionDetails(@RequestParam("vendor") String vendor)
			throws TclCommonException {
		List<IzosdwanBandwidthInterface> response = izosdwanProductService.getbwinterfaceTypes(vendor);
		return new ResponseResource<List<IzosdwanBandwidthInterface>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
}
