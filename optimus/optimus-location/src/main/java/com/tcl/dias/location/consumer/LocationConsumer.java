package com.tcl.dias.location.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.tcl.dias.common.beans.CrossConnectLocalITDemarcationBean;
import com.tcl.dias.common.beans.GscAddressDetailBean;
import com.tcl.dias.common.beans.GscProductAddressResponse;
import com.tcl.dias.location.beans.CustomAddressBean;
import com.tcl.dias.location.entity.entities.MstAddress;
import com.tcl.dias.location.beans.LocationResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Splitter;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.ByonBulkUploadDetail;
import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LeStateGstInfoBean;
import com.tcl.dias.common.beans.LocationAddressInfo;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MSTAddressDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteLevelAddressBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.beans.LocationDetails;
import com.tcl.dias.location.beans.MstCountryBean;
import com.tcl.dias.location.constants.ExceptionConstants;
import com.tcl.dias.location.izosdwan.service.v1.IzoSdwanLocationService;
import com.tcl.dias.location.service.v1.LocationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the LocationConsumer.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class LocationConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationConsumer.class);

	@Autowired
	LocationService locationService;

	@Autowired
	IzoSdwanLocationService izoSdwanLocationService;

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.detail}") })
	public String getLocationDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input received location details {}", responseBody.getPayload());
			String[] locationIds = (responseBody.getPayload()).split(",");
			LocationDetails locDetails = new LocationDetails();
			List<Integer> locIds = new ArrayList<>();
			if (locationIds.length > 0) {
				for (String location : locationIds) {
					locIds.add(Integer.valueOf(location));
				}
			}
			locDetails.setLocationIds(locIds);
			LOGGER.info("Input location details {}",locDetails);
			List<LocationDetail> addressDetails = locationService.getAddress(locDetails);
			LOGGER.info("out put location details {}",addressDetails.size());
			if (addressDetails != null && !addressDetails.isEmpty())
				response = Utils.convertObjectToJson(addressDetails.get(0).getUserAddress());
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.poplocation.detail}") })
	public String getPopLocationDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		long start = System.currentTimeMillis();
		try {
			LOGGER.info("Input payload for getting pop location details {}", responseBody);
			String popLocationId = responseBody.getPayload();
			LocationDetail locationDetail = locationService.getAddressForPopId(popLocationId);
			if (locationDetail != null)
				response = Utils.convertObjectToJson(locationDetail);
		} catch (Exception e) {
			LOGGER.error("Error in pop location details");
		}
		long elapsedTime = System.currentTimeMillis() - start;
		LOGGER.info("Exiting getPopLocationDetails  with an execution time : {} ms", elapsedTime);
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.mstaddress.detail}") })
	public String getMstAddressDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input payload for getting mstaddress details {}", responseBody);
			String mstAddressId = responseBody.getPayload();
			AddressDetail addressDetail = locationService.getAddressForMstAddressId(mstAddressId);
			if (addressDetail != null)
				response = Utils.convertObjectToJson(addressDetail);
		} catch (Exception e) {
			LOGGER.error("Error in mst address details");
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.mstaddrbylocationid.detail}") })
	public String getLocationAndMstDetails(Message<String> responseBody) throws TclCommonException {
		String response = null;
		try {
			LOGGER.info("Input payload for getting location details {}", responseBody);
			String locationId = responseBody.getPayload();
			AddressDetail addressDetail = locationService.getAddressForLocationId(locationId);
			if (addressDetail != null)
				response = Utils.convertObjectToJson(addressDetail);
		} catch (Exception e) {
			LOGGER.error("Error in location details");
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.details.feasibility}") })
	public String getLocationDetailsForAllSites(Message<String> responseBody) throws TclCommonException , AmqpRejectAndDontRequeueException{
		String response = "";
		try {
			LOGGER.info("Input received in getLocationDetailsForAllSites method {}", responseBody.getPayload());
			String[] locationIds = (responseBody.getPayload()).split(",");
			LocationDetails locDetails = new LocationDetails();
			List<Integer> locIds = new ArrayList<>();
			if (locationIds.length > 0) {
				for (String location : locationIds) {
					locIds.add(Integer.valueOf(location));
				}
			}
			LOGGER.info("Input location Ids {}",locDetails);
			locDetails.setLocationIds(locIds);
			List<LocationDetail> addressDetails = locationService.getAddress(locDetails);
			LOGGER.info("addressDetails --> {}",addressDetails);
			if (addressDetails != null && !addressDetails.isEmpty()) {				
				response = Utils.convertObjectToJson(addressDetails);
			}
			LOGGER.info("Address details after object to json: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}

		return response;
	}

	/**
	 * Get common country details for given input
	 *
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.country.details}") })
	public String getCountryDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("getCountryDetails() started");
			List<MstCountryBean> countries = locationService.getCountries();
			if (Objects.nonNull(countries) && !countries.isEmpty())
				response = Utils.convertObjectToJson(countries);
		} catch (Exception e) {
			LOGGER.error("Error in country details");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return response;
	}

	/**
	 * 
	 * getLocalITContact
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.localitcontact}") })
	public String getLocalITContact(String responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input received localItcontact {}", responseBody);
			Integer localItContactId = Integer.valueOf((responseBody));
			Map<String, Object> localItMapper = locationService.getLocalItContact(localItContactId);
			response = Utils.convertObjectToJson(localItMapper);
			LOGGER.info("Response for LocalIt Contact details {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting localItContact {}", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * 
	 * getDemarcationDetails
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.demarcation}") })
	public String getDemarcationDetails(String responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input received demarcations {}", responseBody);
			Integer locationId = Integer.valueOf(responseBody);
			Map<String, Object> demarcationMapper = locationService.getDemarcationDetails(locationId);
			response = Utils.convertObjectToJson(demarcationMapper);
			LOGGER.info("Response for demarcation details {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting demarcation details {}", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.country.details.code}") })
	public String getCounturiesByCode(String responseBody) throws TclCommonException {
		String response = "";
		LOGGER.info("Input received country codes {}", responseBody);
		List<String> codes = Splitter.on(",").splitToList(responseBody);
		List<MstCountryBean> countriesByCode = locationService.getCountriesByCode(codes);
		if (Objects.nonNull(countriesByCode)) {
			response = Utils.convertObjectToJson(countriesByCode);
		}
		return response;
	}

	/**
	 * 
	 * Return city detail based on location ID
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.city.details}") })
	public String getCityDetailByLocationId(String responseBody) throws TclCommonException {
		String response = CommonConstants.EMPTY;
		try {
			LOGGER.info("Input received Locations ID {}", responseBody);
			return locationService.returnCityDetailForTheLocation(Integer.valueOf(responseBody));
		} catch (Exception e) {
			LOGGER.error("Error in getting city details {}", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * 
	 * Get Site Types Based on Location IDS
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sites.type.detail}") })
	public String getTypesOfSitedByLocationId(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input received location getTypesOfSitedByLocationId {}", responseBody.getPayload());
			String[] locationIds = (responseBody.getPayload()).split(",");
			List<Integer> locIds = new ArrayList<>();
			if (locationIds.length > 0) {
				for (String location : locationIds) {
					locIds.add(Integer.valueOf(location));
				}
			}
			response = locationService.returnTypeOfSitesByLocationIds(locIds);
			LOGGER.info("Response getTypesOfSitedByLocationId {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.get.country.region.id}") })
	public String getBillingAddress(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {

			LOGGER.info("Input received mstAddress details get country region id {}", responseBody.getPayload());
			MSTAddressDetails mstAddressDetails = (MSTAddressDetails) Utils
					.convertJsonToObject(responseBody.getPayload(), MSTAddressDetails.class);
			response = locationService.getcountryToRegionId(mstAddressDetails.getLocation_Le_Id().toString());
		} catch (Exception e) {
			LOGGER.error("Error in get country rgion id details", e);
		}

		return response != null ? response : "";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.custleid.tostate.queue}") })
	public String getStateAndCityAccordingToLocationId(Message<String> responseBody)
			throws TclCommonException, AmqpRejectAndDontRequeueException {
		String response = "";
		try {
			String[] locationIds = (responseBody.getPayload()).split(",");
			LocationDetails locDetails = new LocationDetails();
			List<Integer> locIds = new ArrayList<>();
			if (locationIds.length > 0) {
				for (String location : locationIds) {
					locIds.add(Integer.valueOf(location));
				}
			}
			locDetails.setLocationIds(locIds);
			Map<String, LeStateGstInfoBean> addressDetails = locationService.getStateAndCityForLocationId(locDetails);
			if (addressDetails != null && !addressDetails.isEmpty()) {				
				response = Utils.convertObjectToJson(addressDetails);
			}
			LOGGER.info("Address details after object to json: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${location.address.state.codevalidation.queue}") })
	@SuppressWarnings("unchecked")
	public Boolean validateStateCode(Message<String> responseBody) throws TclCommonException {
		Boolean response = false;
		try {
			LOGGER.info("Into Validate State Code");
			Map<String, Object> requestPayload = Utils.convertJsonToObject(responseBody.getPayload(), Map.class);
			requestPayload.entrySet().stream().forEach(map -> LOGGER.info("Request pay load content :: key is {} and value is {}", map.getKey(), map.getValue()));
			/*Map<String, Object> locationMapper = Utils.convertJsonToObject(requestPayload.get("request").toString(), Map.class);*/
			response = locationService.validateStateCode((String) requestPayload.get("STATE_CODE"),
					(Integer) requestPayload.get("LOCATION_ID"));
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${location.gst.state.validation.queue}") })
	@SuppressWarnings("unchecked")
	public Boolean validateGstStateAndNumber(Message<String> responseBody) throws TclCommonException {
		Boolean response = false;
		try {
			LOGGER.info("Into Validate State Code");
			Map<String, Object> requestPayload = Utils.convertJsonToObject(responseBody.getPayload(), Map.class);
			requestPayload.entrySet().stream().forEach(map -> LOGGER
					.info("Request pay load content :: key is {} and value is {}", map.getKey(), map.getValue()));
			/*Map<String, Object> locationMapper = Utils.convertJsonToObject(requestPayload.get("request").toString(), Map.class);*/
			response = locationService.validateGstStateAndNumber((String) requestPayload.get("STATE"),
					(String) requestPayload.get("GSTNO"));
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}
		return response;
	}
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.mstaddrbylocationid.detail.list}") })
	public String getLocationAndMstDetailsList(Message<String> responseBody) throws TclCommonException {
		String response = null;
		try {
			LOGGER.info("Input payload for getting location details {}", responseBody);
			String locationId = responseBody.getPayload();

			String[] locationIds = (responseBody.getPayload()).split(",");
			LocationDetails locDetails = new LocationDetails();
			List<Integer> locIds = new ArrayList<>();
			if (locationIds.length > 0) {
				for (String location : locationIds) {
					locIds.add(Integer.valueOf(location));
				}
			}
			locDetails.setLocationIds(locIds);


			List<AddressDetail> addressDetail = locationService.getAddressForListOfLocationId(locIds);
			if (addressDetail != null)
				response = Utils.convertObjectToJson(addressDetail);
		} catch (Exception e) {
			LOGGER.error("Error in location details");
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.crossconnect.localit.demarcation.detail}") })
	public String getCrossconnectLocalItContact(Message<String> requestBody) throws TclCommonException {
		String response = "";
		try {

			LOGGER.info("Input received cross connect local it contact {}", requestBody.getPayload());
			Integer ccLocMappingId = Integer.valueOf(requestBody.getPayload());
//			String[] payLoadObject = (requestBody.getPayload()).split(",");
//			Integer locationId=Integer.parseInt(payLoadObject[0]);
//			Integer customerId=Integer.parseInt(payLoadObject[1]);
//			LOGGER.info("Input received cross connect local it contact {}:"+locationId+"---"+customerId);
//			List<LocationItContact> localItContact = locationService.getCrossConnectLocationItContact(locationId,customerId);
			LOGGER.info("Input received cross connect ccLocMappingId {}", ccLocMappingId);
			CrossConnectLocalITDemarcationBean crossConnectLocalITContactAndDemarcation = locationService.getCrossConnectLocalITContactAndDemarcation(ccLocMappingId);
			if (response != null){
				response = Utils.convertObjectToJson(crossConnectLocalITContactAndDemarcation);
				LOGGER.info("Cross Connect Queue response :: {}", crossConnectLocalITContactAndDemarcation.toString());
			}
		} catch (Exception e) {
			LOGGER.error("Error in get cross connect local IT contact", e);
		}

		return response != null ? response : "";
	}

	/**
	 * Get location detail or create location
	 * @param requestBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.webex.details}") })
	public String getLocationDetailsOrAddLocation(Message<String> requestBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Request body::{}",requestBody.getPayload());
			LocationDetail locationDetail = Utils.convertJsonToObject(requestBody.getPayload(), LocationDetail.class);
			LocationResponse locationResponse = locationService.getOrAddLocation(locationDetail);
			if(Objects.nonNull(locationResponse)){
				response = String.valueOf(locationResponse.getLocationId());
			}
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}
		return response;
	}

	/*
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.pop.location.queue}")})
	public String getPopLocationByLocationId(String responseBody) throws TclCommonException {
		String response = CommonConstants.EMPTY;
		try {
			LOGGER.info("Input received Locations ID {}", responseBody);
			String popLoc = locationService.returnPopDetailsForTheLocation(Integer.valueOf(responseBody));
			if (popLoc != null)
				response = popLoc;

		} catch (Exception e) {
			LOGGER.error("Error in getting pop location details {}", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}


	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.crossconnect.demarcation.detail}") })
	public String getCrossconnectDemarcationDetail(Message<String> requestBody) throws TclCommonException {
		String response = "";
		try {

			LOGGER.info("Input received cross connect demarcation detail {}", requestBody.getPayload());
			String locationId = requestBody.getPayload();
			List<DemarcationBean> localItContact = locationService.getCClocationDemarcationDetailsByLocationID(Integer.valueOf(locationId));
			if (response != null){
				response = Utils.convertObjectToJson(localItContact);
			}
		} catch (Exception e) {
			LOGGER.error("Error in get cross connect demarcation detail", e);
		}

		return response != null ? response : "";
	}

	/**
	 *
	 * Update byon address details in location
	 * @author AnandhiV
	 * @param requestBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.izosdwan.byon.detail}") })
	public void persistLocationForByonDetails(Message<String> requestBody) throws TclCommonException {
		try {

			LOGGER.info("Input received for persisting BYON detail {}", requestBody.getPayload());
			String request = requestBody.getPayload();
			if (request != null) {
				Map<Integer,List<ByonBulkUploadDetail>> byonBulkUploadDetails = IzosdwanUtils.fromJson(request,
						new TypeReference<Map<Integer,List<ByonBulkUploadDetail>>>() {
						});
				if (byonBulkUploadDetails != null && !byonBulkUploadDetails.isEmpty()) {
					izoSdwanLocationService.getLocationUpdateByonDetails(byonBulkUploadDetails);
				}

			}

		} catch (Exception e) {
			LOGGER.error("Error in get cross connect demarcation detail", e);
		}

	}


	@RabbitListener(queuesToDeclare = {@Queue("${gsc.product.address.queue}")})
	public String saveGscAddressDetail(Message<String> request) {
		String response = "";
		try {
			LOGGER.info("GSC Domestic Voice address details received :: {} ", request.getPayload());
			GscAddressDetailBean gscAddressDetailBean = Utils.convertJsonToObject(request.getPayload(), GscAddressDetailBean.class);
			GscProductAddressResponse gscProductAddressResponse = locationService.saveGscAddress(gscAddressDetailBean);
			response = Utils.convertObjectToJson(gscProductAddressResponse);
			return response;
		} catch (Exception e) {
			LOGGER.error("Error in saving address :: {} ", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.partner.contracting.address.queue}") })
	public String getPartnerContractingAddress(String request) throws TclCommonException {
		String response = CommonConstants.EMPTY;
		try {
			LOGGER.info("Input received Locations ID {}", request);
			CustomAddressBean customAddressBean = locationService.returnPartnerContractingAddressForLocId(Integer.valueOf(request));
			response = Utils.convertObjectToJson(customAddressBean);
		} catch (Exception e) {
			LOGGER.error("Error in fetching partner contracting address {}", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}


	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sitelevel.location.queue}") })
	public String getSiteLevelLocationDetails(Message<String> request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input received in getLocationDetailsForAllSites method {}", request.getPayload());
			SiteLevelAddressBean siteLevelAddressBean = Utils.convertJsonToObject(request.getPayload(), SiteLevelAddressBean.class);
			List<LocationAddressInfo> locationAddressInfoList = siteLevelAddressBean.getLocationAddressInfo();
			List<Integer> locIds = new ArrayList<>();
			for(LocationAddressInfo locationAddressInfo : locationAddressInfoList) {
				Integer locationId = locationAddressInfo.getLocationId();
				locIds.add(locationId);
			}
			LocationDetails locDetails = new LocationDetails();
			LOGGER.info("Input location Ids {}",locDetails);
			locDetails.setLocationIds(locIds);
			List<LocationDetail> locationDetails = locationService.getAddress(locDetails);
			LOGGER.info("locationDetails --> {}",locationDetails);
			if (locationDetails != null && !locationDetails.isEmpty()) {			
				for(LocationDetail locationDetail : locationDetails) {
					AddressDetail addressDetail = locationDetail.getUserAddress();
					for(LocationAddressInfo locationAddressInfo : locationAddressInfoList) {
						Integer locationId = locationAddressInfo.getLocationId();
						if(locationId.equals(locationDetail.getLocationId())) {
							locationAddressInfo.setAddressDetail(addressDetail);
						}
					}
				} 
			}
			response = Utils.convertObjectToJson(siteLevelAddressBean);
			
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}
		return response;
		
	}
		
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.izosdwan.location.withcustomer}") })
	public String saveLocaiton(String request) {
		String response = "";
		try {

			if (request != null & !request.isEmpty()) {
				LocationDetail locationDetail = (LocationDetail) Utils.convertJsonToObject(request,
						LocationDetail.class);
				LocationResponse locationResponse = locationService.addAddressWithCustomeLeMapping(locationDetail);
				if (locationResponse != null) {
					response = Utils.convertObjectToJson(locationResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in saving address :: {} ", ExceptionUtils.getStackTrace(e));

		}
		return response;
	}



	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.izosdwan.location.international.withcustomer}") })
	public String saveInternationalLocaiton(String request) {
		String response = "";
		try {

			if (request != null & !request.isEmpty()) {
				LocationDetail locationDetail = (LocationDetail) Utils.convertJsonToObject(request,
						LocationDetail.class);
				LocationResponse locationResponse = locationService
						.addAddressInternationalWithCustomerLeMapping(locationDetail);
				if (locationResponse != null) {
					response = Utils.convertObjectToJson(locationResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in saving address :: {} ", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}


}
