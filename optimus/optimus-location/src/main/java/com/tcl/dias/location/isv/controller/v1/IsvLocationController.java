package com.tcl.dias.location.isv.controller.v1;

import java.util.List;
import java.util.Map;

import com.tcl.dias.location.beans.*;
import com.tcl.dias.location.gvpn.service.v1.GvpnLocationService;

import com.tcl.dias.location.service.v1.LocationAsyncService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
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
@RequestMapping("/isv/v1")
public class IsvLocationController {

	@Autowired
	private LocationService locationService;
	
	
	@Autowired
	GvpnLocationService gvpnLocationService;

	@Autowired
	LocationAsyncService locationAsyncService;

	/**
	 * 
	 * getLocation
	 * 
	 * @param locationDetails
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_LOCATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/locations/details", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
	 * getLocationItContact
	 * 
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationItContact.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/locations/{locationId}/localitcontact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<LocationItContact> getLocationItContact(@PathVariable("locationId") Integer locationId,@RequestParam(value="customerId",required=true) Integer customerId)
			throws TclCommonException {
		LocationItContact customerLocationItContact = locationService.getLocationItContact(locationId,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				customerLocationItContact, Status.SUCCESS);

	}

	/**
	 * 
	 * updateItLocationAndDemarcation
	 * 
	 * @param locationId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.UPDATE_DEMARCATION_AND_LOCAL_IT_CONTACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LocationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/locations/{locationId}/localitdemarcation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<LocationResponse> updateItLocationAndDemarcation(
			@PathVariable("locationId") Integer locationId, @RequestBody DemarcationAndItContactBean request)
			throws TclCommonException {
		LocationResponse response = locationService.updateItLocationAndDemarcation(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getLocationDemarcationDetails
	 * 
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/locations/{locationId}/demarcation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DemarcationBean> getLocationDemarcationDetails(
			@PathVariable("locationId") Integer locationId) throws TclCommonException {
		DemarcationBean response = locationService.getlocationDemarcationDetailsByLocationID(locationId);
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
	@GetMapping("/cities/details")
	public ResponseResource<List<MstCityBean>> getCities(@RequestParam final String country)
			throws TclCommonException {
		List<MstCityBean> response = locationService.getCitiesByCountry(country);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Location.GET_CITIES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstCityBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/cities/states/details")
	public ResponseResource<List<MstCityBean>> getCitiesByState(@RequestParam final String state)
			throws TclCommonException {
		List<MstCityBean> response = locationService.getCitiesByState(state);
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
	 * @author VIVEK KUMAR K
	 * getStateDetails based on country
	 * @param country
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = Location.GET_STATE_DETAILS)
	@RequestMapping(value = "/state/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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

	@ApiOperation(value = Location.GET_COUNTRY_DETAILS)
	@RequestMapping(value = "/country/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<String>> getCountryDetails(){
		List<String> response=gvpnLocationService.getCountryDetails();
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
	 *
	 * @param pincode
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = Location.GET_BY_PINCODE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GeocodeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/teamsdr/getlocation", method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GeocodeBean> getLocationUsingGoogleAPI(@RequestParam(name = "country") String country,
			@RequestParam(name = "pincode") String pincode) throws TclCommonException, ParseException {
		GeocodeBean response = locationAsyncService.getGeoCodeDetails(country, pincode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
