package com.tcl.dias.products.controller.v1;

import java.util.List;
import java.util.Set;

import com.tcl.dias.products.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.dias.products.swagger.constants.SwaggerConstants.ApiOperations.Products;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This is the controller class for the product catalog related services
 * 
 *
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/products")
public class ProductsController {
	
	@Autowired
	ProductsService productFamilyService;
	
	

	/**
	 * Method to retrieve the profiles under a particular product
	 * 
	 * @author Dinahar Vivekanandan
	 * @param productId
	 * @return ResponseResource<List<ProductDto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_BY_PRODUCT_ID)
	@RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<ProductDto>> getByProductFamilyId(@PathVariable("productId") Integer productId)
			throws TclCommonException {
		List<ProductDto> response = productFamilyService.getByProductFamilyId(productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to retrieve all the product families
	 * 
	 * @author Dinahar Vivekanandan
	 * @return ResponseResource<List<ProductFamilyDto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_ALL_PRODUCT)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductFamilyDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<ProductFamilyDto>> getAllProductFamilies() throws TclCommonException {

		List<ProductFamilyDto> response = productFamilyService.getAllProductFamily();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is to retrieve details with respect to the profile
	 * 
	 * @author Dinahar Vivekanandan
	 * @param productOfferingId
	 * @return ResponseResource<ProductDto>
	 * @throws TclCommonException
	 */
	/*@ApiOperation(value = Products.GET_BY_PRODUCT_OFFERING_ID)
	@RequestMapping(value = "/{productId}/profiles/{profileId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })*/
	public ResponseResource<ProductDto> getByProductOfferingId(@PathVariable("productId") Integer productId,
			@PathVariable("profileId") Integer productOfferingId) throws TclCommonException {
		ProductDto response = productFamilyService.getByProductOfferingId(productOfferingId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Method to get service details of a product
	 * 
	 * @author Vinod kumar
	 * @param productId
	 * @return ResponseResource<String>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_SERVICE_DETAILS_BY_PRODUCT_ID)
	@RequestMapping(value = "/{productId}/servicedetails/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getServiceDetails(@PathVariable("productId") Integer productId)
			throws TclCommonException {
		String serviceDetails = productFamilyService.getServiceDetails(productId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceDetails,
				Status.SUCCESS);
	}

	/**
	 * getSlaValueByProductOfferId
	 * 
	 * This method is to get SLA values by passing productOfferId and slaVarientId
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_PRODUCT_LOCATIONS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/locations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getProductLocations(@RequestParam("productName") String productName)
			throws TclCommonException {
		List<String> response = productFamilyService.getProductLocations(productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * getCpeBomDetails
	 * 
	 * Method to retrieve CPE BOM details for the selected profile
	 * 
	 * @author Dinahar V
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
	@RequestMapping(value = "/CPE-BOM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Set<CpeBomDto>> getCpeBom( @RequestParam("bandwidth") Integer bandwidth,
			@RequestParam("portInterface") String portInterface,
			@RequestParam("routingProtocol") String routingProtocol) throws TclCommonException {
		Set<CpeBomDto> response = productFamilyService.getCpeBom(bandwidth, portInterface, routingProtocol);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	
	
	
	
	
	
	/**
	 * getResourceForCpe
	 * 
	 * Method to retrieve resource details for the given list of CPE
	 * 
	 * @author Dinahar V
	 * @param List<Integer> cpeList
	 * @return ResponseResource<List<CpeBomDto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_BOM_RESOURCE_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//  Changed since UI is not storing the product and profile id 
//	@RequestMapping(value = "{productId}/profiles/{profileId}/bom-resources", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/bom-resources", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Set<CpeBomDto>> getCpeBomDetails(@RequestBody List<String> cpeBomIdList) throws TclCommonException {
		Set<CpeBomDto> response = productFamilyService.getCpeBomDetails(cpeBomIdList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * getAttributeValue
	 * 
	 * Method to retrieve attribute for particular product type
	 * 
	 * @author Biswajit Sahoo
	 * @param String attributeCd
	 * @return ResponseResource<Set<AttributeValueDto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_ATTRIBUTE_VALUES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })

	@RequestMapping(value = "/attribute-values", method = RequestMethod.GET)
	public ResponseResource<List<AttributeValueDto>> getAttributeValue(@RequestParam String attributeCd)
			throws TclCommonException {
		List<AttributeValueDto> response = productFamilyService.getAttributeValue(attributeCd);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * getAttributesForProduct - Method to retrieve all attributes for a product
	 * @author Dinahar Vivekanandan
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_ALL_ATTRIBUTES)
@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
		@ApiResponse(code = 403, message = Constants.FORBIDDEN),
		@ApiResponse(code = 422, message = Constants.NOT_FOUND),
		@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
@RequestMapping(value = "/{productId}/attribute-values", method = RequestMethod.GET)
public ResponseResource<Set<ProductAttributeDto>> getAttributesForProduct(@PathVariable Integer productId)
		throws TclCommonException {
	Set<ProductAttributeDto> response = productFamilyService.getAttributesForProduct(productId);
	return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
			Status.SUCCESS);
}
	/**
	 * getProfileList
	 * 
	 * Method to retrieve profile list for particular product
	 * 
	 * @param String profileType
	 * @return  ResponseResource<List<ProductProfileDto>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_PROFILE_LIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{productFamilyId}/profilelist", method = RequestMethod.GET)
	public ResponseResource<List<ProductProfileDto>> getProfileList(@PathVariable Integer productFamilyId)
			throws TclCommonException {
		List<ProductProfileDto> response = productFamilyService.getProfileList(productFamilyId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * This method is used to retrieve component details for custom profile
	 * 
	 * @author Thamizhselvi Perumal
	 * @param productId productOffering Id
	 * @param profileId customProfile Id
	 * @return ResponseResource<ProfileComponentListDto>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_COMPONENT_BY_PRODUCT_OFFERING_ID)
	@RequestMapping(value = "/{productId}/profiles/{profileId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ProfileComponentListDto> getComponentsByProductOfferingId(@PathVariable("productId") Integer productId,
			@PathVariable("profileId") Integer productOfferingId) throws TclCommonException {
		ProfileComponentListDto response = productFamilyService.getComponentListByProductOfferingId(productOfferingId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


	/**
	 * This method is used to retrieve all the CPE Values and their maximum Bandwidth for IAS
	 * @author Suruchi Acharya
	 * @return ResponseResource<List<String>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_ALL_CPE_VALUES_IAS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cpe-values", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource getCpeValues() throws TclCommonException {
		Set<CpeBandwidthBean> response = productFamilyService.getAllCpeValuesIrrespectiveOfBw();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * This method is used to retrieve all the CPE Values and their maximum Bandwidth for GVPN
	 * @author Suruchi Acharya
	 * @return ResponseResource<List<String>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Products.GET_ALL_CPE_VALUES_GVPN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProductDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cpe-values/gvpn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource getCpeValuesGvpn() throws TclCommonException {
		Set<CpeBandwidthBean> response = productFamilyService.getAllCpeValuesIrrespectiveOfBwForGvpn();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

}
