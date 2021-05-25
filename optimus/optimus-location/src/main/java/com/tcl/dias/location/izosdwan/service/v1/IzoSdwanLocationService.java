package com.tcl.dias.location.izosdwan.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ByonBulkUploadDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.beans.GeocodeBean;
import com.tcl.dias.location.beans.LocationUploadValidationBean;
import com.tcl.dias.location.constants.ExceptionConstants;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
import com.tcl.dias.location.entity.entities.MstAddress;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstPincode;
import com.tcl.dias.location.entity.entities.MstState;
import com.tcl.dias.location.entity.repository.CustomerLocationRepository;
import com.tcl.dias.location.entity.repository.LocationLeCustomerRepository;
import com.tcl.dias.location.entity.repository.LocationRepository;
import com.tcl.dias.location.entity.repository.MstAddressRepository;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstCountryRepository;
import com.tcl.dias.location.entity.repository.MstLocalityRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstStateRepository;
import com.tcl.dias.location.service.v1.LocationAsyncService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author vpachava
 *
 */
@Transactional
@Service
public class IzoSdwanLocationService {

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	MstAddressRepository mstAddressRepository;

	@Autowired
	MstCountryRepository mstCountryRepository;

	@Autowired
	MstStateRepository mstStateRepository;

	@Autowired
	MstCityRepository mstCityRepository;

	@Autowired
	MstPincodeRespository mstPincodeRespository;

	@Autowired
	MstLocalityRepository mstLocalityRepository;

	@Autowired
	LocationAsyncService locationAsyncService;
	
	@Autowired
	CustomerLocationRepository customerLocationRepository;
	
	@Autowired
	private LocationLeCustomerRepository locationLeCustomerRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.izosdwan.update.byon}")
	String batchQueue;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanLocationService.class);

	public List<Integer> getAddressDeatils(String textToSearch, List<Integer> locationIds) throws TclCommonException {
		List<Integer> locDetails = new ArrayList<>();

		if (locationIds.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.LOCATION_ID_EMPTY_ERROR, ResponseResource.R_CODE_ERROR);
		}

		Optional<List<Location>> locationDetails = locationRepository.findByIdIn(locationIds);

		if (locationDetails.isPresent()) {
			List<Location> locations = locationDetails.get();
			locations.stream().forEach(loc -> {
				Integer userAddressDetailId = loc.getAddressId();
				if (userAddressDetailId != null) {
					try {
						MstAddress userAddressEntity = mstAddressRepository
								.selectBasedOnSearchAndId(userAddressDetailId, textToSearch);
//						MstAddress addr = userAddressEntity.get();
						if (userAddressEntity != null) {
							locDetails.add(loc.getId());
						}
					} catch (Exception e) {
						LOGGER.info("ERROR IN GETTING DETAILS");
					}

				}
			});
		} else {
			throw new TclCommonException(ExceptionConstants.GET_LOCATION_COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}

		return locDetails;
	}

	/**
	 * 
	 * Update byon address details in location
	 * 
	 * @author AnandhiV
	 * @param byonBulkUploadDetails
	 * @return
	 * @throws TclCommonException
	 */
	public void getLocationUpdateByonDetails(Map<Integer, List<ByonBulkUploadDetail>> byonBulkUploadDetails)
			throws TclCommonException {
		byonBulkUploadDetails.forEach((key, value) -> {
			
			Boolean hasErrors = value.stream().filter(byon -> StringUtils.isNotBlank(byon.getErrorMessageToDisplay())).findFirst()
					.isPresent();
			LOGGER.info("Has errors {} fro quote {}",hasErrors,key);
			value.stream().forEach(byon -> {
				try {
					persistAddressUploaded(byon, hasErrors);
				} catch (TclCommonException e) {
					LOGGER.error("Exception while adding address ", e);
					;
				}
			});
		});
		LOGGER.info("Returning to batch with request {}",byonBulkUploadDetails.toString());
		mqUtils.send(batchQueue, Utils.convertObjectToJson(byonBulkUploadDetails));

	}

	private ByonBulkUploadDetail persistAddressUploaded(ByonBulkUploadDetail byonBulkUploadDetail, Boolean hasErrors)
			throws TclCommonException {
		try {
			if (validateAddress(byonBulkUploadDetail.getCountry(), byonBulkUploadDetail.getState(),
					byonBulkUploadDetail.getCity(), byonBulkUploadDetail.getPinCode(),byonBulkUploadDetail)) {
				LocationUploadValidationBean locationUploadValidationBean = new LocationUploadValidationBean();
				GeocodeBean geocodeBean = locationAsyncService.getGeoCodeDetails(byonBulkUploadDetail.getCountry(),
						byonBulkUploadDetail.getState(), byonBulkUploadDetail.getCity(),
						byonBulkUploadDetail.getPinCode(), byonBulkUploadDetail.getAddress(),
						locationUploadValidationBean);
				if (geocodeBean != null && geocodeBean.getLatLng() != null) {
					byonBulkUploadDetail.setLatLong(geocodeBean.getLatLng());
					if (!hasErrors) {
						LOGGER.info("Got successfull response from Google API {}", geocodeBean);
						MstAddress mstAddress = new MstAddress();
						mstAddress.setAddressLineOne(byonBulkUploadDetail.getAddress());
						mstAddress.setCity(byonBulkUploadDetail.getCity());
						mstAddress.setCountry(byonBulkUploadDetail.getCountry());
						mstAddress.setCreatedBy(Utils.getSource());
						mstAddress.setCreatedTime(new Timestamp((new java.util.Date()).getTime()));
						mstAddress.setLocality(byonBulkUploadDetail.getLocality());
						mstAddress.setPincode(byonBulkUploadDetail.getPinCode());
						mstAddress.setState(byonBulkUploadDetail.getState());
						mstAddress = mstAddressRepository.saveAndFlush(mstAddress);

						if (mstAddress.getId() != null) {
							Location location = new Location();
							location.setAddressId(mstAddress.getId());
							location.setApiAddressId(mstAddress.getId());
							location.setLatLong(geocodeBean.getLatLng());
							location.setIsActive(CommonConstants.BACTIVE);
							location.setIsVerified(0);
							location = locationRepository.saveAndFlush(location);
							if (location.getId() != null) {
								byonBulkUploadDetail.setLocationId(location.getId());
								//save in customer location
								Optional<CustomerLocation> customerLocationBean = customerLocationRepository
										.findByLocation_IdAndErfCusCustomerId(location.getId(),
												byonBulkUploadDetail.getErfCustomerId());
								if (!customerLocationBean.isPresent()) {
									CustomerLocation customerLocation = new CustomerLocation();
									customerLocation.setErfCusCustomerId(byonBulkUploadDetail.getErfCustomerId());
									customerLocation.setLocation(location);
									customerLocationRepository.saveAndFlush(customerLocation);
									LOGGER.info("customer id:{}",customerLocation.getId());
								}
								//save in customer le location
								Optional<LocationLeCustomer> locationLeCustomerBean = locationLeCustomerRepository
										.findByErfCusCustomerLeIdAndLocation(byonBulkUploadDetail.getErfCustomerLeId(), location);
								if (!locationLeCustomerBean.isPresent()) {
									LocationLeCustomer locationLeCustomer = new LocationLeCustomer();
									locationLeCustomer.setErfCusCustomerLeId(byonBulkUploadDetail.getErfCustomerLeId());
									locationLeCustomer.setLocation(location);
									locationLeCustomerRepository.saveAndFlush(locationLeCustomer);
									LOGGER.info("customer le id:{}",locationLeCustomer.getId());
								}
							} else {
								byonBulkUploadDetail.setLocationErrorDetails("Unable to persist address in location");
							}
						} else {
							byonBulkUploadDetail.setLocationErrorDetails("Unable to persist the address");
						}
					}
				} else {
					LOGGER.info("ERROR in getting lat long!!! for address {} and id {}",
							byonBulkUploadDetail.getAddress(), byonBulkUploadDetail.getId());
					byonBulkUploadDetail.setLocationErrorDetails(locationUploadValidationBean.getErrorMessage());
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage());
		}
		return byonBulkUploadDetail;
	}
	
	private Boolean validateAddress(String country,String state,String city,String pincode,ByonBulkUploadDetail byonBulkUploadDetail) {
		if(country==null || state==null || city==null || pincode ==null) {
			return false;
		}
//		MstCountry mstCountry = getCountryDetailsByName(country);
//		if(mstCountry==null) {
//			byonBulkUploadDetail.setLocationErrorDetails("Invalid country");
//			return false;
//		}
//		MstState mstState = getStateDetailsByCountryAndName(state, mstCountry);
//		if(mstState==null) {
//			byonBulkUploadDetail.setLocationErrorDetails("Invalid state for the country");
//			return false;
//		}
//		MstCity mstCity = getCityByStateAndName(city, mstState);
//		if(mstCity==null) {
//			byonBulkUploadDetail.setLocationErrorDetails("Invalid city for the state");
//			return false;
//		}
		//List<MstPincode> mstPincode = mstPincodeRespository.findByCodeAndMstCity(pincode, mstCity);
//		if(pincode==null||pincode.isEmpty() || !((pincode.matches("[0-9]+") && pincode.length() >= 4))) {
//			byonBulkUploadDetail.setLocationErrorDetails("Invalid pincode for the city");
//			return false;
//		}
		return true;
	}

	private MstCountry getCountryDetailsByName(String name) {
		MstCountry mstCountry = mstCountryRepository.findByName(name);
		if (mstCountry != null) {
			return mstCountry;
		}
		return null;
	}

	private MstState getStateDetailsByCountryAndName(String name, MstCountry mstCountry) {
		MstState mstState = mstStateRepository.findByNameAndMstCountry(name, mstCountry);
		if (mstState != null) {
			return mstState;
		}
		return null;
	}

	private MstCity getCityByStateAndName(String name, MstState mstState) {
		MstCity mstCity = mstCityRepository.findByNameAndMstState(name, mstState);
		if (mstCity != null) {
			return mstCity;
		}
		return null;
	}

}
