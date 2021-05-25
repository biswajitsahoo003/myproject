package com.tcl.dias.location.controller.v1;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.CrossConnectLocalITDemarcationBean;
import com.tcl.dias.location.beans.*;
import com.tcl.dias.location.service.v1.LocationAsyncService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.CgwCustomerLocationMapping;

import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.location.gvpn.service.v1.GvpnLocationService;
import com.tcl.dias.location.service.v1.LocationService;
import com.tcl.dias.location.swagger.constants.SwaggerConstants;
import com.tcl.dias.location.swagger.constants.SwaggerConstants.ApiOperations.Location;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 *
 * This file contains the LocationController.java class. This class contains API
 * informations related to location which will be consumed by location
 * configuration UI.
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private GvpnLocationService gvpnLocationService;

	@Autowired
	LocationAsyncService locationAsyncService;

	/**
	 *
	 * getByPincode- This api will return the pincode details based on the country.
	 * Based on the country , the pincode
	 *
	 * @param pincode
	 * @param country
	 * @return PincodeDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_BY_PINCODE)
	@RequestMapping(value = "/pincode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PincodeDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PincodeDetail> getByPincode(@RequestParam("code") String pincode,
			@RequestParam("country") String country) throws TclCommonException {
		PincodeDetail response = locationService.getPincodeDetails(pincode, country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * updateCustomerAndLeLocations - This API will update the customer and customer le location mapping for CGW locations.
	 *
	 *
	 * @return String
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.UPDATE_CUSTOMER_AND_LE_LOCATION_FOR_CGW)
	@RequestMapping(value = "/cgw/customerle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateCustomerAndLeLocations(@RequestBody CgwCustomerLocationMapping customerLocations)
			throws TclCommonException {
		locationService.updateCustomerAndLeLocations(customerLocations);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	/**
	 *
	 * addAddress - This API will be used to add the location and the locationId
	 * will be returned
	 *
	 *
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<LocationResponse> addLocation(@RequestBody LocationDetail locationDetail)
			throws TclCommonException {
		LocationResponse response = locationService.addAddress(locationDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 *
	 * addAddress - This API will be used to add the location along with customer le mapping and the locationId
	 * will be returned
	 *
	 *
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION_WITH_CUSTOMER_LE)
	@RequestMapping(value="/withcustomerle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<LocationResponse> addLocationWithCustomerLeMapping(@RequestBody LocationDetail locationDetail)
			throws TclCommonException {
		LocationResponse response = locationService.addAddressWithCustomeLeMapping(locationDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * addAddress - This API will be used to add the location and the locationId
	 * will be returned
	 *
	 *
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION)
	@RequestMapping(value="/international",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<LocationResponse> addLocationInternational(@RequestBody LocationDetail locationDetail)
			throws TclCommonException {
		LocationResponse response = locationService.addAddressInternational(locationDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 *
	 * addAddress - This API will be used to add the location along with customer le mapping and the locationId
	 * will be returned
	 *
	 *
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION_WITH_CUSTOMER_LE)
	@RequestMapping(value="/international/withcustomerle",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<LocationResponse> addLocationInternationalWithCustomerLeMapping(@RequestBody LocationDetail locationDetail)
			throws TclCommonException {
		LocationResponse response = locationService.addAddressInternationalWithCustomerLeMapping(locationDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * addAddress - This API will be used to add a list of locations and the
	 * locationIds will be returned
	 *
	 *
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION)
	@RequestMapping(value = "/multiple", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LocationResponse>> addLocations(@RequestBody List<LocationDetail> locationDetailList)
			throws TclCommonException {
		List<LocationResponse> response = locationService.addAddresses(locationDetailList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 *
	 * addAddress - This API will be used to add a list of locations along with customer le mapping and the
	 * locationIds will be returned
	 *
	 *
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION_WITH_CUSTOMER_LE)
	@RequestMapping(value = "/multiple/withcustomerle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LocationResponse>> addLocationsWithCustomerLeMapping(@RequestBody List<LocationDetail> locationDetailList)
			throws TclCommonException {
		List<LocationResponse> response = locationService.addAddressesWithCustomerLeMapping(locationDetailList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 *
	 * addAddress - This API will be used to add a list of locations for Gvpn and the
	 * locationIds will be returned
	 *
	 *
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION)
	@RequestMapping(value = "/multiple/international", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LocationResponse>> addLocationsInternational(@RequestBody List<LocationDetail> locationDetailList)
			throws TclCommonException {
		List<LocationResponse> response = locationService.addAddressesInternational(locationDetailList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 *
	 * addAddress - This API will be used to add a list of locations along with customer le mapping for Gvpn and the
	 * locationIds will be returned
	 *
	 *
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_LOCATION_WITH_CUSTOMER_LE)
	@RequestMapping(value = "/multiple/international/withcustomerle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LocationResponse>> addLocationsInternationalWithCustomerLeMapping(@RequestBody List<LocationDetail> locationDetailList)
			throws TclCommonException {
		List<LocationResponse> response = locationService.addAddressesInternationalWithCustomerLeMapping(locationDetailList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * addPopLocations - This API will be used to add POP locations and the
	 * locationIds will be returned
	 *
	 *
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.ADD_POP_LOCATIONS)
	@RequestMapping(value = "/multiplepops", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LocationResponse>> addPopLocations(@RequestBody List<LocationDetail> locationDetailList)
			throws TclCommonException {
		List<LocationResponse> response = locationService.addPopAddresses(locationDetailList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * updateLocation - This API will be used to update the user location and the
	 * same locationId will be returned
	 *
	 * @param locationDetail
	 * @param locationId
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.UPDATE_LOCATION)
	@RequestMapping(value = "/{locationId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<LocationResponse> updateLocation(@RequestBody LocationDetail locationDetail,
			@PathVariable("locationId") Integer locationId,@RequestParam(value="customerId",required=true) Integer customerId) throws TclCommonException {
		LocationResponse response = locationService.updateAddress(locationDetail, locationId,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * method used to getLocation details for given list of location ids
	 *
	 * @param locationDetails
	 * @return List<LocationDetail>
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.GET_LOCATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LocationDetail>> getLocation(@RequestBody List<Integer> locationDetails)
			throws TclCommonException {
		LocationDetails locationIds = new LocationDetails();
		locationIds.setLocationIds(locationDetails);
		List<LocationDetail> response = locationService.getAddress(locationIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 *
	 * method used to post or update Location details for given list of location ids
	 *
	 * @param locationDetails
	 * @return String
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.POST_OR_UPDATE_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{locationId}/localitcontact", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> postLocationItContact(@PathVariable("locationId") Integer locationId,
			@RequestBody LocationItContact locationItDetails,@RequestParam(value="customerId",required=true) Integer customerId) throws TclCommonException {
		locationService.postLocationItContact(locationItDetails,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);

	}

	/**
	 *
	 * method used to getLocation details for given list of location ids
	 *
	 * @param locationDetails
	 * @return List<LocationDetail>
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.GET_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{locationId}/localitcontact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<LocationItContact> getLocationItContact(@PathVariable("locationId") Integer locationId,@RequestParam(value="customerId",required=true) Integer customerId)
			throws TclCommonException {
		LocationItContact customerLocationItContact = locationService.getLocationItContact(locationId,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLocationItContact, Status.SUCCESS);

	}
	

	@ApiOperation(value = Location.GET_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "crossconnect/{locationId}/localitcontact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LocationItContact>> getCrossConnectLocationItContact(@PathVariable("locationId") Integer locationId,@RequestParam(value="customerId",required=true) Integer customerId)
			throws TclCommonException {
		List<LocationItContact> customerLocationItContact = locationService.getCrossConnectLocationItContact(locationId,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLocationItContact, Status.SUCCESS);

	}
	
	/**
	 *
	 * method used to soft delete Location details for given location id
	 *
	 * @param locationId
	 * @return String
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.DELETE_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{locationId}/localitcontact/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> deleteLocationItContact(@PathVariable("locationId") Integer locationId,@RequestParam(value="customerId",required=true) Integer customerId)
			throws TclCommonException {
		String response = locationService.deleteLocationItContact(locationId,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 *
	 * This method returns the list of legal entity and their site address with
	 * local IT contact details No input is required, it internally fetches the
	 * legal entities mapped to the logged in user
	 *
	 * @return List<LocationItContact>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/localitcontacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SiteLocationItContact>> findSiteLocationITContactsByUserId()
			throws TclCommonException {
		List<SiteLocationItContact> siteLocationItContact = locationService.findSiteLocationITContacts();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteLocationItContact,
				Status.SUCCESS);

	}

	@ApiOperation(value = Location.POST_OR_UPDATE_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/localitcontacts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LocationItContact>> getlocalItConatctDetails(
			@RequestBody Set<Integer> localItContactIds,@RequestParam(value="customerId",required=true)Integer customerId) throws TclCommonException {
		List<LocationItContact> siteLocationItContacts = locationService.getLocItContact(localItContactIds,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteLocationItContacts,
				Status.SUCCESS);

	}

	@ApiOperation(value = Location.POST_OR_UPDATE_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/localitcontacts/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LocationItContact>> getAllLocalItContacts() throws TclCommonException {
		List<LocationItContact> siteLocationItContacts = locationService.getAllLocalItContacts();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteLocationItContacts,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateItLocationAndDemarcation
	 * @param id
	 * @param localITContactId
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.UPDATE_DEMARCATION_AND_LOCAL_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{locationId}/localitdemarcation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<LocationResponse> updateItLocationAndDemarcation(
			@PathVariable("locationId") Integer locationId, @RequestBody DemarcationAndItContactBean request)
			throws TclCommonException {
		LocationResponse response = locationService.updateItLocationAndDemarcation(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = Location.UPDATE_DEMARCATION_AND_LOCAL_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/crossconnect/localitdemarcation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Integer> updateCrossConnectItLocationAndDemarcation(@RequestBody CrossConnectLocalITDemarcationBean request)
			throws TclCommonException {
		Integer response = locationService.updateCrossConnectItLocationAndDemarcation(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = Location.UPDATE_DEMARCATION_AND_LOCAL_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/crossconnect/localitdemarcation/{ccLocMappingId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CrossConnectLocalITDemarcationBean> updateCrossConnectItLocationAndDemarcation(@PathVariable("ccLocMappingId") Integer ccLocMappingId)
			throws TclCommonException {
		CrossConnectLocalITDemarcationBean response = locationService.getCrossConnectLocalITContactAndDemarcation(ccLocMappingId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 *
	 * This method updates the lat long details of the given location id
	 *
	 * @author NAVEEN GUNASEKARAN
	 * @return List<Location>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.LAT_LONG_UPDATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{locationId}/latlong", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<LocationResponse> updateLatLong(@RequestBody LocationDetail locationDetail,
			@PathVariable("locationId") Integer locationId) throws TclCommonException {
		LocationResponse response = locationService.updateLatLong(locationId, locationDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * This method return the list of location details for customer legal entity
	 *
	 * @author Vishesh Awasthi
	 * @return List<AddressDetail>
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/legalentities/locationdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AddressDetail>> getLocationDetailsByCusLeId(
			@RequestParam(name = "customerId", required = false) Integer customerId) throws TclCommonException {
		List<AddressDetail> response = locationService.loadLocationsDetails(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/legalentities/locationdetails/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<List<AddressDetail>>> getLocationDetailsByCusLeIdWithPagination(
			@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "city", required = false) String city,
			@RequestParam(name = "state", required = false) String state,
			@RequestParam(name = "country", required = true) String country,
			@RequestParam(name = "searchBy", required = false) String searchBy,
			@RequestParam(name = "pop", required = false) Boolean pop,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "page", required = true) Integer page,
			@RequestParam(name = "size", required = true) Integer size,
			@RequestParam(name = "connectedSite", required = false) Boolean connectedSite) throws TclCommonException {
		PagedResult<List<AddressDetail>> response = locationService.loadLocationsDetailsWithPagination(customerId, city, country, searchBy, page, size,state,pop,type,connectedSite);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/legalentities/locationdetails/cities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getDistinctCitiesForLoadMyLocation(
			@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "country", required = true) String country) throws TclCommonException {
		List<String> response = locationService.getDistinctCitiesForLoadMyLocation(customerId,country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method return the demacration details for the given location ID
	 *
	 * @author sekhar er
	 * @return DemarcationBean
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{locationId}/demarcation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DemarcationBean> getLocationDemarcationDetails(
			@PathVariable("locationId") Integer locationId) throws TclCommonException {
		DemarcationBean response = locationService.getlocationDemarcationDetailsByLocationID(locationId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * This method return the demacration details for the given location IDs for cross connect
	 *
	 * @return DemarcationBean
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/crossconnect/demarcation/{locationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<DemarcationBean>> getCCLocationDemarcationDetails(
			@PathVariable("locationId") Integer locationId) throws TclCommonException {
		List<DemarcationBean> response = locationService.getCClocationDemarcationDetailsByLocationID(locationId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * This method returns the address for all customer details
	 *
	 * @author ANNE NISHA
	 *
	 * @return List<SiteLocationItContact>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_ADDRESS_BASED_ON_CUSTOMER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteLocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/addresses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SiteLocationItContact>> getAddressBasedOnCustomerDetails() throws TclCommonException {
		List<SiteLocationItContact> response = locationService.getAddressBasedOnCustomer();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 *
	 * This method returns the local It contact details for the local it contact id
	 * that is passed
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_LOCAL_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/localitcontacts/{localItContactId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<LocationItContact> findSiteLocationITContactsByLocalItContactId(
			@PathVariable("localItContactId") Integer localItContactId) throws TclCommonException {
		LocationItContact siteLocationItContact = locationService.findSiteLocationITContactById(localItContactId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteLocationItContact,
				Status.SUCCESS);

	}
	
	/**
	 *
	 * This method returns the local It contact details for the local it contact id
	 * that is passed for cross connect
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_LOCAL_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "crossconnect/localitcontacts/{localItContactIds}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LocationItContact>> findCCSiteLocationITContactsByLocalItContactId(
			@PathVariable("localItContactIds") String localItContactIds) throws TclCommonException {
		List<LocationItContact> siteLocationItContact = locationService.findCCSiteLocationITContactById(localItContactIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteLocationItContact,
				Status.SUCCESS);

	}


	/**
	 *
	 * This method uploads the xlsx file with the list of locations.
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.UPLOAD_LOCATION_EXCEL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/upload", method = RequestMethod.POST)
	@Transactional
	public ResponseResource<Object> locationsExcelUpload(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		Object response = locationService.locationsExcelUpload(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method uploads the xlsx file with the list of locations.
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.UPLOAD_LOCATION_EXCEL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/upload/ias", method = RequestMethod.POST)
	@Transactional
	public ResponseResource<Object> locationsExcelUploadIas(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		Object response = locationService.locationsExcelUploadIas(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method uploads the xlsx file with the list of locations.
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.UPLOAD_LOCATION_EXCEL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/upload/iwan", method = RequestMethod.POST)
	@Transactional
	public ResponseResource<Object> locationsExcelUploadIwan(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		Object response = locationService.locationsExcelUploadIwan(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) { 
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}

	/**
	 *
	 * This method uploads the xlsx file with the list of locations.
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = Location.DOWNLOAD_LOCATION_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/template", method = RequestMethod.POST)
	public ResponseEntity<String> downloadLocationTemplate(@RequestBody LocationTemplateRequest locationTemplate,
			HttpServletResponse response) throws TclCommonException, IOException {
		locationService.downloadLocationTemplate(locationTemplate, response);
		return null;
	}
	
	/**
	 *
	 * This method downloads the izosdwan xlsx file.
	 *@author mpalanis
	 * @return LocationItContact
	 * @throws TclCommonException
	 * @throws IOException
	 */
	
	@ApiOperation(value = Location.DOWNLOAD_LOCATION_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/template/izosdwan", method = RequestMethod.POST)
	public ResponseResource<String> downloadIzoSdwanByonTemplate(@RequestBody LocationTemplateRequest locationTemplate, HttpServletResponse response) throws TclCommonException, IOException {
		locationService.downloadIzoSdwanByonTemplate(locationTemplate,response);
		return null;
	}
	/**
	 *
	 * This method uploads the xlsx file with the list of locations.
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = Location.DOWNLOAD_LOCATION_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/template/ias", method = RequestMethod.POST)
	public ResponseEntity<String> downloadLocationTemplateForIAS(@RequestBody LocationTemplateRequest locationTemplate,
			HttpServletResponse response) throws TclCommonException, IOException {
		locationService.downloadLocationTemplateIas(locationTemplate, response);
		return null;
	}
	
	/**
	 *
	 * This method uploads the xlsx file with the list of locations.
	 *
	 * @return LocationItContact
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = Location.DOWNLOAD_LOCATION_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/template/iwan", method = RequestMethod.POST)
	public ResponseEntity<String> downloadLocationTemplateForIwan(@RequestBody LocationTemplateRequest locationTemplate,
			HttpServletResponse response) throws TclCommonException, IOException {
		locationService.downloadLocationTemplateIwan(locationTemplate, response);
		return null;
	}

	/**
	 * @author VIVEK KUMAR K
	 * getStateDetails
	 * @param country
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.GET_STATE_DETAILS)
	@RequestMapping(value = "/state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstStateBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<MstStateBean>> getStateDetails(
			@RequestParam("country") String country){
		List<MstStateBean> response=gvpnLocationService.getStateDetails(country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
	 * getCityDetails
	 * @param country
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.GET_CITY_DETAILS)
	@RequestMapping(value = "/city", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstStateBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<MstCityBean>> getCityDetails(
			@RequestParam("state") String state){
		List<MstCityBean> response=gvpnLocationService.getCityDetails(state);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * Get Cities for given country
	 *
	 * @param country
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Location.GET_CITIES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstCityBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/cities")
	public ResponseResource<List<MstCityBean>> getCities(@RequestParam final String country)
			throws TclCommonException {
		List<MstCityBean> response = locationService.getCitiesByCountry(country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 *
	 * Get Countries
	 *
	 * @param
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Location.GET_COUNTRY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/countries")
	public ResponseResource<List<MstCountryBean>> getCountries() throws TclCommonException {
		List<MstCountryBean> response = locationService.getCountries();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VISHESH AWASTHI
	 * @param countryCodes
	 * @return List<MstCountryBean>
	 * @throws TclCommonException
	 * @{@link LocationService#getCountriesByCode(List)}
	 */
	@PostMapping("/countries")
	public List<MstCountryBean> getCountriesByCode(@RequestBody List<String> countryCodes)
			throws TclCommonException {
		return locationService.getCountriesByCode(countryCodes);
	}
	
	/**
	 * save list of address
	 * 
	 * @author VISHESH AWASTHI
	 * @param locationDetails
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Location.ADD_LOCATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/emergency/contacts")
	public List<Integer> saveEmergencyAddress(@RequestBody LocationDetails locationDetails)
			throws TclCommonException {
		return locationService.saveLocationEmergencyAddress(locationDetails);
	}
	
	/**
	 * returns list of address details
	 * 
	 * @author VISHESH AWASTHI
	 * @param addressIds
	 * @return
	 * @throws TclCommonException
	 */
	@PostMapping("/emergency/contacts/addresses")
	public List<AddressDetail> getEmergencyAddress(@RequestBody List<Integer> addressIds) throws TclCommonException{
		return locationService.getEmergencyAddress(addressIds);
	}
	

	
	
	/**
	 * Api to get data centre's location id using data center code 
	 * @param dcCode
	 * @return location id
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Location.GET_DC_LOCATION_ID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/location/dcCode")
	public ResponseResource<Integer> getLocationIdForDcCode(@RequestParam(value="dcCode",required=true) String dcCode) throws TclCommonException {
		Integer locationId = locationService.getLocationIdForDcCode(dcCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, locationId,
				Status.SUCCESS);

	}
	
	/**
	 * Api to get data centre code for location id
	 * @param dcCode
	 * @return location id
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Location.GET_DC_CODE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/dcdetails")
	public ResponseResource<List<SolutionBean>> getDataCenterDetails(@RequestBody List<SolutionBean> solutions) throws TclCommonException {
		List<SolutionBean> results = locationService.getLocationCodeForId(solutions);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, results,
				Status.SUCCESS);

	}

	/**
	 * This method helps in generating the NPL download template. 
	 * @param locationTemplate
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 * @link http://www.tatacommunications.com/
	 * @copyright www.tatacommunications.com
	 */
	@ApiOperation(value = Location.DOWNLOAD_LOCATION_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/template/npl", method = RequestMethod.POST)
	public ResponseEntity<String> getLocationTemplateForNPL(@RequestBody LocationTemplateResponseBean locationTemplate,
			HttpServletResponse response) throws TclCommonException, IOException {
		locationService.downloadLocationTemplateForNPL(locationTemplate, response);
		return null;
	}

	
	/**
	 * getCityDetails based on zipCode and state
	 * @param state and zipcode
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.GET_CITY_DETAILS)
	@RequestMapping(value = "/city/zipcode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstStateBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<MstCityBean>> getCityDetailsByZipCode(
			@RequestParam("state") String state,@RequestParam("zipcode") String zipcode){
		List<MstCityBean> response=gvpnLocationService.getCityDetailsByZipCode(zipcode,state);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * getConnected Buildings LatLongDetails from specific latLong
	 * @param latLong
	 * @return
	 * @throws TclCommonException
	 */
	/**
	 * get localIT contact information
	 * @param locationId and customerLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.GET_LAT_LONG)
	@RequestMapping(value = "/buildings/connected", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<ConnectedLocationResponse>> getConnectedBuildingsLatLong(
			@RequestBody ConnectedLocationBean locationBean){
		List<ConnectedLocationResponse> response=locationService.getConnectedBuildings(locationBean.getLatLongList(),locationBean.getDistance());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 *
	 * This method uploads the xlsx file with the list of locations for NPL.
	 *
	 * @return Object
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.UPLOAD_LOCATION_EXCEL_NPL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/upload/npl", method = RequestMethod.POST)
	@Transactional
	public ResponseResource<Object> locationsExcelUploadNpl(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		Object response = locationService.locationsExcelUploadNpl(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
/**
 * @author chetan chaudhary
 * @param type
 * @return
 * @throws TclCommonException
 */
	@ApiOperation(value = Location.LOCATION_USING_ADDRESSID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/location/address", method = RequestMethod.GET)
	public ResponseResource<List<LocationResponseBeanUsingPopAddresssId>> getLocationBasedOnAddressType(@RequestParam(value="type", required=true) String type,@RequestParam(value="locationEnd", required=true) String locationEnd)
			throws TclCommonException {
		List<LocationResponseBeanUsingPopAddresssId> response = locationService.getlocationUsingAddressType(type,locationEnd);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * @param locationId
	 * @param customerLeId
	 * @return {@link LocationItContact}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_LOCAL_IT_CONTACT)
	@RequestMapping(value = "/{locationId}/le/{customerLeId}/localitcontact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<LocationItContact> getLocalITContactByLocation(
			@PathVariable Integer locationId,@PathVariable Integer customerLeId) throws TclCommonException{
		LocationItContact response=locationService.getLocalITContactByLocation(locationId,customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}



	/**
	 *
	 * @param locationId
	 * @param customerLeId
	 * @return {@link LocationItContact}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_LOCAL_IT_CONTACT)
	@RequestMapping(value = "/{locationId}/le/localitcontact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<LocationItContact> getLocalITContactByLocationId(
			@PathVariable Integer locationId,@RequestParam(value="customerLeId", required=true) Integer customerLeId) throws TclCommonException{
		LocationItContact response=locationService.getLocalITContactByLocation(locationId,customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * getByPincode- This api will return the pincode details based on the country.
	 * Based on the country , the pincode
	 *
	 * @param pincode
	 * @param country
	 * @return PincodeDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_BY_PINCODE)
	@RequestMapping(value = "/pincode/cities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PincodeDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<Map<String, Object>>> getCitiesByPincode(@RequestParam("pincode") String pincode) throws TclCommonException {
		List<Map<String, Object>> response = locationService.getCitiesByPincode(pincode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 *  Get Locality Details by Pincode City And Country
	 * @param pincode
	 * @param country
	 * @param city
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_LOCALITY_BY_PINCODE_AND_CITY)
	@RequestMapping(value = "/locality", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PincodeDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<String>> getLocalityByCityAndPincode(@RequestParam("pincode") String pincode,@RequestParam("city") String city) throws TclCommonException {
		List<String> response = locationService.getLocalityByCityAndPincode(pincode,city);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * Get state details by city
	 * @param city
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_LOCALITY_BY_PINCODE_AND_CITY)
	@RequestMapping(value = "/stateinfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PincodeDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getStateByCity(@RequestParam("city") Integer city) throws TclCommonException {
		String response = locationService.getStateInformationOfTheCity(city);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get geocode details using google API.
	 * @param pincode
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_BY_PINCODE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GeocodeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/teamsdr/getlocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GeocodeBean> getLocationUsingGoogleAPI(@RequestParam(name = "country") String country,@RequestParam(name = "pincode") String pincode)
			throws TclCommonException, ParseException {
		GeocodeBean response = locationAsyncService.getGeoCodeDetails(country, pincode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * punching latlong for location id
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.PUNCH_LAT_LONG)
	@RequestMapping(value = "/quote/{quoteId}/punchlatlong", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> punchLatlong( @PathVariable("quoteId") Integer quoteId, @RequestBody List<PunchLatlongBean> latlongBean) throws TclCommonException{
		locationService.updateLatlong(quoteId,latlongBean);	
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
				Status.SUCCESS);

	}
	
	/**
     * 
     * adding custom address into MST table
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = Location.ADD_CUSTOM_ADDRESS)
    @RequestMapping(value = "/addcustomaddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<String> addCustomAddress(@RequestBody CustomAddressBean addressDetail) throws TclCommonException{
        locationService.addCustomAddress(addressDetail);    
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
                Status.SUCCESS);
    }

	/**
	 * GSC API -  get state and city details by country
	 * @param country
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_STATES_CITIES_BY_COUNTRY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gsc/country/{country}/statesCities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, Set<String>>> getStatesCities(@PathVariable("country") String country) throws TclCommonException{
		Map<String, Set<String>> response = locationService.getStatesCitiesDetails(country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
}
