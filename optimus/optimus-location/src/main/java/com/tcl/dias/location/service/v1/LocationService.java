package com.tcl.dias.location.service.v1;

import static com.tcl.dias.common.utils.UserType.INTERNAL_USERS;
import static com.tcl.dias.common.utils.UserType.PARTNER;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.location.entity.entities.MstLocationData;
import com.tcl.dias.location.entity.repository.MstLocationDataRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.CgwCustomerLocationMapping;
import com.tcl.dias.common.beans.CrossConnectDemarcations;
import com.tcl.dias.common.beans.CrossConnectLocalITContacts;
import com.tcl.dias.common.beans.CrossConnectLocalITDemarcationBean;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.DomesticVoiceAddressDetail;
import com.tcl.dias.common.beans.GscAddressDetailBean;
import com.tcl.dias.common.beans.GscProductAddressResponse;
import com.tcl.dias.common.beans.LeStateGstInfoBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.MSTAddressDetails;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.beans.BulkUploadNplResponse;
import com.tcl.dias.location.beans.ConnectedLocationResponse;
import com.tcl.dias.location.beans.CustomAddressBean;
import com.tcl.dias.location.beans.DemarcationAndItContactBean;
import com.tcl.dias.location.beans.LocationDetails;
import com.tcl.dias.location.beans.LocationMultipleResponse;
import com.tcl.dias.location.beans.LocationOfferingDetail;
import com.tcl.dias.location.beans.LocationOfferingDetailNPL;
import com.tcl.dias.location.beans.LocationResponse;
import com.tcl.dias.location.beans.LocationResponseBeanUsingPopAddresssId;
import com.tcl.dias.location.beans.LocationTemplateRequest;
import com.tcl.dias.location.beans.LocationTemplateResponseBean;
import com.tcl.dias.location.beans.LocationUploadValidationBean;
import com.tcl.dias.location.beans.LocationValidationColumnBean;
import com.tcl.dias.location.beans.MstCityBean;
import com.tcl.dias.location.beans.MstCountryBean;
import com.tcl.dias.location.beans.PincodeDetail;
import com.tcl.dias.location.beans.PunchLatlongBean;
import com.tcl.dias.location.beans.SiteLocationItContact;
import com.tcl.dias.location.beans.SolutionBean;
import com.tcl.dias.location.constants.ExceptionConstants;
import com.tcl.dias.location.constants.LocationConstants;
import com.tcl.dias.location.constants.LocationUploadConstants;
import com.tcl.dias.location.constants.PincodeConstants;
import com.tcl.dias.location.entity.entities.CCLocalITContact;
import com.tcl.dias.location.entity.entities.CCLocationMapping;
import com.tcl.dias.location.entity.entities.CountryToRegion;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import com.tcl.dias.location.entity.entities.CustomerSiteLocationItContact;
import com.tcl.dias.location.entity.entities.Demarcation;
import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
import com.tcl.dias.location.entity.entities.MstAddress;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstRegion;
import com.tcl.dias.location.entity.entities.MstState;
import com.tcl.dias.location.entity.impl.LocationLeCustomerDao;
import com.tcl.dias.location.entity.repository.CCLocalITContactRepository;
import com.tcl.dias.location.entity.repository.CCLocationMappingRepository;
import com.tcl.dias.location.entity.repository.CountryToRegionRepository;
import com.tcl.dias.location.entity.repository.CustomerLocationItInfoRepository;
import com.tcl.dias.location.entity.repository.CustomerLocationRepository;
import com.tcl.dias.location.entity.repository.DemarcationRepository;
import com.tcl.dias.location.entity.repository.LocationLeCustomerRepository;
import com.tcl.dias.location.entity.repository.LocationRepository;
import com.tcl.dias.location.entity.repository.MstAddressRepository;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstCountryRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstRegionRepository;
import com.tcl.dias.location.entity.repository.MstStateRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * 
 * This file contains the LocationService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
/**
 * @author PARUNACH
 *
 */
@Service
@Transactional
public class LocationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

	@Autowired
	private MstPincodeRespository mstPincodeRespository;

	@Autowired
	private MstCityRepository mstCityRepository;

	@Autowired
	private MstCountryRepository mstCountryRepository;

	@Autowired
	private MstAddressRepository mstAddressRepository;

	@Autowired
	private MstStateRepository mstStateRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CustomerLocationRepository customerLocationRepository;

	@Autowired
	private CustomerLocationItInfoRepository customerLocationItInfoRepository;

	@Autowired
	private LocationLeCustomerRepository locationLeCustomerRepository;

	@Autowired
	private DemarcationRepository demarcationRepository;

	@Value("${api.googlemap.key}")
	private String googleMapApiKey;

	@Value("${api.googlemap}")
	private String googleMapAPI;
	
	@Value("${rabbitmq.product.byon.interface}")
	private String interfaceType;

	@Value("${rabbitmq.customer.les.queue}")
	private String customerLeToCustomerQueue;

	@Autowired
	private UserInfoUtils userInfoUtils;

	@Autowired
	private MQUtils mqUtils;

	private static MissingCellPolicy xRow;

	@Autowired
	private LocationAsyncService locationAsyncService;

	@Autowired
	private LocationLeCustomerDao locationLeCustomerDao;

	@Autowired
	private MstRegionRepository mstRegionRepository;

	@Autowired
	private CountryToRegionRepository countryToRegionRepository;
	
	@Autowired
	private CCLocationMappingRepository ccLocMappingRepository;

	@Autowired
	private CCLocalITContactRepository ccLocalITContactRepository;

	@Autowired
	private MstLocationDataRepository mstLocationDataRepository;

	/**
	 * getPincodeDetails-This method will be used to get the pincode details from
	 * repo and return back the pincode. getPincodeDetails
	 * 
	 * @param pincode
	 * @param country
	 * @return PincodeDetail
	 * @throws TclCommonException
	 */
	public PincodeDetail getPincodeDetails(String pincode, String country) throws TclCommonException {
		if (StringUtils.isEmpty(pincode) || StringUtils.isEmpty(country)) {
			throw new TclCommonException(ExceptionConstants.LOCATION_PIN_ERROR, ResponseResource.R_CODE_ERROR);
		}
		PincodeDetail pincodeDetail = new PincodeDetail();
		try {
			List<String> localities = new ArrayList<>();
			pincodeDetail.setLocalities(localities);
			LOGGER.info("Pincode {} and Country{} received", pincode, country);
			//List<Map<String, Object>> pincodeResponse = mstPincodeRespository.findByPincode(pincode, country);
			List<Map<String, Object>> pincodeResponse = mstPincodeRespository.findByPincodeV1(pincode, country);
			if (pincodeResponse.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			} else {
				pincodeResponse.stream().forEach(pincodeMap -> {
					if (pincodeDetail.getState() == null) {
						pincodeDetail.setPincode((String) pincodeMap.get(PincodeConstants.PINCODE.toString()));
						pincodeDetail.setState((String) pincodeMap.get(PincodeConstants.STATE.toString()));
						pincodeDetail.setCity((String) pincodeMap.get(PincodeConstants.CITY.toString()));
						pincodeDetail.setCountry((String) pincodeMap.get(PincodeConstants.COUNTRY.toString()));
						pincodeDetail.setCityId((Integer) pincodeMap.get(PincodeConstants.CITY_ID.toString()));
					}
					localities.add((String) pincodeMap.get(PincodeConstants.LOCALITY.toString()));

				});
			}
			sortCity(pincodeDetail);
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		}
		return pincodeDetail;
	}

	/**
	 * sortCity
	 * 
	 * @param pincodeDetail
	 */
	private void sortCity(PincodeDetail pincodeDetail) {
		if (pincodeDetail != null && pincodeDetail.getLocalities() != null
				&& !pincodeDetail.getLocalities().isEmpty()) {
			pincodeDetail.getLocalities().sort((l1, l2) -> l1.compareTo(l2));

		}
	}

	/**
	 * 
	 * addAddress - This method will be used to add the location for gvpn and return
	 * the locationId
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	public LocationResponse addAddressInternational(LocationDetail locationDetail) throws TclCommonException {
		LocationResponse loginResponse = new LocationResponse();
		try {
			if (locationDetail.getApiAddress() == null || locationDetail.getUserAddress() == null) {
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			}
			String user = Utils.getSource();
			validateAddress(locationDetail.getUserAddress(), true);
			validateAddress(locationDetail.getApiAddress(), false);
			Integer userAddressId = locationAsyncService.persistAddressLocInternational(locationDetail.getUserAddress(),
					LocationConstants.USER_SOURCE.toString(), user);
			Integer apiAddressId = locationAsyncService.persistAddressLocInternational(locationDetail.getApiAddress(),
					LocationConstants.API_SOURCE.toString(), user);
			Location locationEntity = locationAsyncService.persistLocation(locationDetail, userAddressId, apiAddressId,
					null, null);
			loginResponse.setLocationId(locationEntity.getId());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return loginResponse;
	}
	
	/**
	 * 
	 * addAddress - This method will be used to add the location for gvpn with customer le mapping and return
	 * the locationId
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	public LocationResponse addAddressInternationalWithCustomerLeMapping(LocationDetail locationDetail) throws TclCommonException {
		LocationResponse loginResponse = new LocationResponse();
		try {
			if (locationDetail.getApiAddress() == null || locationDetail.getUserAddress() == null || locationDetail.getErfCusCustomerLeId() == null) {
				LOGGER.error(
						"Either of Api Address, User Address or Customer le id is missing. Values Api Address: {}, User Address: {}, Customer Le Id: {}",
						locationDetail.getApiAddress(), locationDetail.getUserAddress(),
						locationDetail.getErfCusCustomerLeId());
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			}
			String user = Utils.getSource();
			validateAddress(locationDetail.getUserAddress(), true);
			validateAddress(locationDetail.getApiAddress(), false);
			Integer userAddressId = locationAsyncService.persistAddressLocInternational(locationDetail.getUserAddress(),
					LocationConstants.USER_SOURCE.toString(), user);
			Integer apiAddressId = locationAsyncService.persistAddressLocInternational(locationDetail.getApiAddress(),
					LocationConstants.API_SOURCE.toString(), user);
			Location locationEntity = locationAsyncService.persistLocation(locationDetail, userAddressId, apiAddressId,
					null, null);
			addLocationLeCustomer(locationDetail.getErfCusCustomerLeId(), locationEntity);
			loginResponse.setLocationId(locationEntity.getId());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return loginResponse;
	}

	/**
	 * 
	 * addNPLAddress - This method will be used to add the location and return the
	 * locationId
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	public LocationResponse addNPLAddress(LocationDetail locationDetail) throws TclCommonException {
		LocationResponse loginResponse = new LocationResponse();
		Location locationEntity = null;
		try {
			if (locationDetail.getUserAddress() == null && locationDetail.getPopId() == null) {
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			}
			String user = Utils.getSource();
			if (locationDetail.getPopId() != null && !locationDetail.getPopId().isEmpty()) {
				locationEntity = locationRepository.findByPopLocationId(locationDetail.getPopId());
			} else {
				locationDetail.getUserAddress().setLatLong(locationDetail.getLatLong());
				Integer popAddressId = locationAsyncService.persistAddressLoc(locationDetail.getUserAddress(),
						LocationConstants.USER_SOURCE.toString(), user);

				locationEntity = persistNPLLocation(locationDetail, popAddressId, popAddressId, null, null);
			}
			loginResponse.setLocationId(locationEntity.getId());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return loginResponse;
	}

	/**
	 * 
	 * addAddresses - This method will be used to add the list of locations and
	 * return the locationIds
	 * 
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	public List<LocationResponse> addAddresses(List<LocationDetail> locationDetailList) throws TclCommonException {
		List<LocationResponse> locationResponseList = new ArrayList<>();
		try {
			if (locationDetailList == null || locationDetailList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			/**
			 * Java 8 stream expects exception handling inside the for steam().map()
			 * function - hence ordinary for loop is used
			 */

			for (LocationDetail locDetail : locationDetailList) {
				locationResponseList.add(addAddress(locDetail));
			}

			if (locationResponseList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return locationResponseList;
	}
	
	/**
	 * 
	 * addAddresses - This method will be used to add the list of locations along with customer le mapping and
	 * return the locationIds
	 * 
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	public List<LocationResponse> addAddressesWithCustomerLeMapping(List<LocationDetail> locationDetailList) throws TclCommonException {
		List<LocationResponse> locationResponseList = new ArrayList<>();
		try {
			if (locationDetailList == null || locationDetailList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			/**
			 * Java 8 stream expects exception handling inside the for steam().map()
			 * function - hence ordinary for loop is used
			 */

			for (LocationDetail locDetail : locationDetailList) {
				locationResponseList.add(addAddressWithCustomeLeMapping(locDetail));
			}

			if (locationResponseList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return locationResponseList;
	}

	/**
	 * 
	 * addAddress - This method will be used to add the location and return the
	 * locationId
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */

	public LocationResponse addAddress(LocationDetail locationDetail) throws TclCommonException {
		LocationResponse loginResponse = new LocationResponse();
		try {
			if (locationDetail.getApiAddress() == null || locationDetail.getUserAddress() == null) {
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			}

			String user = Utils.getSource();
			validateAddress(locationDetail.getUserAddress(), true);
			validateAddress(locationDetail.getApiAddress(), false);
			Integer userAddressId = locationAsyncService.persistAddressLoc(locationDetail.getUserAddress(),
					LocationConstants.USER_SOURCE.toString(), user);

			Integer apiAddressId = locationAsyncService.persistAddressLoc(locationDetail.getApiAddress(),
					LocationConstants.API_SOURCE.toString(), user);
			Location locationEntity = locationAsyncService.persistLocation(locationDetail, userAddressId, apiAddressId,
					null, null);
			loginResponse.setLocationId(locationEntity.getId());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return loginResponse;

	}

	public void updateCustomerAndLeLocations(CgwCustomerLocationMapping customerLocations) throws TclCommonException {
		validateCgwCustomerLe(customerLocations);
		for (Integer locationId : customerLocations.getLocationIds()) {
			Optional<Location> locationOpt = locationRepository.findById(locationId);
			if (!locationOpt.isPresent()) {
				throw new TclCommonException(ExceptionConstants.LOCATION_UPDATE_ERROR, ResponseResource.R_CODE_ERROR);
			}

			// Add customer location mapping if not present.
			Optional<CustomerLocation> customerLocationOpt = customerLocationRepository.findByLocation_IdAndErfCusCustomerId(locationId, customerLocations.getCustomerId());
			LOGGER.info("Customer location mapping for customer: {}, location:{} is exists: {}", customerLocations.getCustomerId(), locationId, customerLocationOpt.isPresent());
			if (!customerLocationOpt.isPresent()) {
				LOGGER.info("Adding customer location mapping for customer: {}, location:{}", customerLocations.getCustomerId(), locationId);
				addCustomerLocation(customerLocations.getCustomerId(), locationOpt.get());
			}

			// Add location le customer mapping if not present.
			Optional<LocationLeCustomer> locationLeCustomerOpt = locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(customerLocations.getCustomerLeId(), locationOpt.get());
			LOGGER.info("Location le customer mapping for customer: {}, location:{} is exists: {}", customerLocations.getCustomerLeId(), locationId, locationLeCustomerOpt.isPresent());
			if (!locationLeCustomerOpt.isPresent()) {
				LOGGER.info("Adding location le customer mapping for customer: {}, location:{}", customerLocations.getCustomerLeId(), locationId);
				addLocationLeCustomer(customerLocations.getCustomerLeId(), locationOpt.get());
			}
		}
	}

	private void validateCgwCustomerLe(CgwCustomerLocationMapping customerLocations) throws TclCommonException {
		if (Objects.isNull(customerLocations)
				|| Objects.isNull(customerLocations.getCustomerId())
				|| Objects.isNull(customerLocations.getCustomerLeId())
				|| CollectionUtils.isEmpty(customerLocations.getLocationIds())
				|| customerLocations.getLocationIds().stream().anyMatch(locationId -> Objects.isNull(locationId))) {
			throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * addAddressWithCustomeLeMapping - This method will be used to add the location along with customer le mapping and return the
	 * locationId
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */

	public LocationResponse addAddressWithCustomeLeMapping(LocationDetail locationDetail) throws TclCommonException {
		LocationResponse loginResponse = new LocationResponse();
		try {
			if (locationDetail.getApiAddress() == null || locationDetail.getUserAddress() == null || locationDetail.getErfCusCustomerLeId() == null) {
				LOGGER.error(
						"Either of Api Address, User Address or Customer le id is missing. Values Api Address: {}, User Address: {}, Customer Le Id: {}",
						locationDetail.getApiAddress(), locationDetail.getUserAddress(),
						locationDetail.getErfCusCustomerLeId());
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			}

			String user = Utils.getSource();
			validateAddress(locationDetail.getUserAddress(), true);
			validateAddress(locationDetail.getApiAddress(), false);
			Integer userAddressId = locationAsyncService.persistAddressLoc(locationDetail.getUserAddress(),
					LocationConstants.USER_SOURCE.toString(), user);

			Integer apiAddressId = locationAsyncService.persistAddressLoc(locationDetail.getApiAddress(),
					LocationConstants.API_SOURCE.toString(), user);
			Location locationEntity = locationAsyncService.persistLocation(locationDetail, userAddressId, apiAddressId,
					null, null);
			
			addLocationLeCustomer(locationDetail.getErfCusCustomerLeId(), locationEntity);
			
			loginResponse.setLocationId(locationEntity.getId());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return loginResponse;

	}

	/**
	 * Add customer and location mapping.
	 *
	 * @param customerId
	 * @param location
	 * @return
	 */
	private CustomerLocation addCustomerLocation(final Integer customerId, Location location) {
		CustomerLocation customerLocation = new CustomerLocation();
		customerLocation.setLocation(location);
		customerLocation.setErfCusCustomerId(customerId);
		customerLocation = customerLocationRepository.save(customerLocation);
		LOGGER.info("Customer location added for location: {} and customer: {}", location.getId(), customerId);
		return customerLocation;
	}
	
	/**
	 * Add customer le and location mapping.
	 * 
	 * @param customerLeId
	 * @param location
	 * @return
	 */
	private LocationLeCustomer addLocationLeCustomer(final Integer customerLeId, Location location) {
		LocationLeCustomer locationLeCustomer = new LocationLeCustomer();
		locationLeCustomer.setErfCusCustomerLeId(customerLeId);
		locationLeCustomer.setLocation(location);
		locationLeCustomerRepository.saveAndFlush(locationLeCustomer);
		LOGGER.info("customer le id:{}",locationLeCustomer.getId());
		return locationLeCustomer;
	}

	/**
	 * 
	 * addAddresses - This method will be used to add the list of locations for gvpn
	 * and return the locationIds
	 * 
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	public List<LocationResponse> addAddressesInternational(List<LocationDetail> locationDetailList)
			throws TclCommonException {
		List<LocationResponse> locationResponseList = new ArrayList<>();
		try {
			if (locationDetailList == null || locationDetailList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			/**
			 * Java 8 stream expects exception handling inside the for steam().map()
			 * function - hence ordinary for loop is used
			 */
			for (LocationDetail locDetail : locationDetailList) {
				locationResponseList.add(addAddressInternational(locDetail));
			}
			if (locationResponseList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return locationResponseList;
	}
	
	/**
	 * 
	 * addAddresses - This method will be used to add the list of locations along with customer le mapping for gvpn
	 * and return the locationIds
	 * 
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	public List<LocationResponse> addAddressesInternationalWithCustomerLeMapping(List<LocationDetail> locationDetailList)
			throws TclCommonException {
		List<LocationResponse> locationResponseList = new ArrayList<>();
		try {
			if (locationDetailList == null || locationDetailList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			/**
			 * Java 8 stream expects exception handling inside the for steam().map()
			 * function - hence ordinary for loop is used
			 */
			for (LocationDetail locDetail : locationDetailList) {
				locationResponseList.add(addAddressInternationalWithCustomerLeMapping(locDetail));
			}
			if (locationResponseList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return locationResponseList;
	}

	/**
	 * 
	 * addPopAddresses - This method will be used to add the list of PoP locations
	 * and return the locationIds
	 * 
	 * @param locationDetailList
	 * @return List<LocationResponse>
	 * @throws TclCommonException
	 */
	public List<LocationResponse> addPopAddresses(List<LocationDetail> locationDetailList) throws TclCommonException {
		List<LocationResponse> locationResponseList = new ArrayList<>();
		try {
			if (locationDetailList == null || locationDetailList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			/**
			 * Java 8 stream expects exception handling inside the for steam().map()
			 * function - hence ordinary for loop is used
			 */
			for (LocationDetail locDetail : locationDetailList) {
				locationResponseList.add(addNPLAddress(locDetail));
			}
			if (locationResponseList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return locationResponseList;
	}

	/**
	 * 
	 * updateAddress - This method will be used to update the location address
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 */
	public LocationResponse updateAddress(LocationDetail locationDetail, Integer locationId, Integer customerId)
			throws TclCommonException {
		LocationResponse loginResponse = new LocationResponse();
		try {
			if (locationDetail.getLocationId() == null || locationId == null || customerId == null) {
				throw new TclCommonException(ExceptionConstants.LOCATION_UPDATE_ERROR, ResponseResource.R_CODE_ERROR);
			}

			Optional<Location> locationEntity = locationRepository
					.findById(locationDetail.getLocationId() != null ? locationDetail.getLocationId() : locationId);
			if (locationEntity.isPresent()) {
				Optional<MstAddress> userAddress = mstAddressRepository.findById(locationEntity.get().getAddressId());
				Optional<MstAddress> apiAddress = mstAddressRepository.findById(locationEntity.get().getApiAddressId());
				Optional<CustomerLocation> customerLocation = customerLocationRepository
						.findByLocation_IdAndErfCusCustomerId(locationEntity.get().getId(), customerId);
				if (!(userAddress.isPresent() && apiAddress.isPresent())) {
					throw new TclCommonException(ExceptionConstants.LOCATION_UPDATE_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
				String user = Utils.getSource();
				// to do need to check if we are using or not
				Integer userAddressId = locationAsyncService.persistAddressLoc(locationDetail.getUserAddress(),
						LocationConstants.USER_SOURCE.toString(), user);
				Integer apiAddressId = locationAsyncService.persistAddressLoc(locationDetail.getApiAddress(),
						LocationConstants.API_SOURCE.toString(), user);
				Location location = locationAsyncService.persistLocation(locationDetail, userAddressId, apiAddressId,
						locationEntity.get(), customerLocation.get());
				loginResponse.setLocationId(location.getId());
			} else {
				throw new TclCommonException(ExceptionConstants.LOCATION_UPDATE_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		}
		return loginResponse;
	}
	
	
	/**
	 * 
	 * getAddress - This method will retrieve the address details
	 * 
	 * @param locationIds
	 * @return List<LocationDetail>
	 * @throws TclCommonException
	 */
	public List<LocationDetail> getAddress(LocationDetails locationsIds) throws TclCommonException {
		List<LocationDetail> locationDetails = new ArrayList<>();
		try {
		if (CollectionUtils.isEmpty(locationsIds.getLocationIds())) {
			throw new TclCommonException(ExceptionConstants.LOCATION_ID_EMPTY_ERROR, ResponseResource.R_CODE_ERROR);
		}

		List<Integer> locIds = locationsIds.getLocationIds();
		LOGGER.info("Incoming location ids {} {}",locationsIds,locIds);
		Optional<List<Location>> locationDetailEntities = locationRepository.findByIdIn(locIds);
		if (locationDetailEntities.isPresent()) {
			List<Location> locations = locationDetailEntities.get();
			locations.parallelStream().forEach(loc -> {
				LocationDetail locationDetail = new LocationDetail();
				AddressDetail userAddress = new AddressDetail();
				AddressDetail apiAddress = new AddressDetail();
				locationDetail.setApiAddress(apiAddress);
				locationDetail.setUserAddress(userAddress);
				Integer userAddressDetailId = loc.getAddressId();
				Integer apiAddressDetail = loc.getApiAddressId();
				if (userAddressDetailId != null) {
					Optional<MstAddress> userAddressEntity = mstAddressRepository.findById(userAddressDetailId);
					if (userAddressEntity.isPresent()) {
						constructAddress(userAddress, userAddressEntity.get(), loc);
					}
				}
				if (apiAddressDetail != null) {
					Optional<MstAddress> apiAddressEntity = mstAddressRepository.findById(apiAddressDetail);
					if (apiAddressEntity.isPresent()) {
						constructAddress(apiAddress, apiAddressEntity.get(), loc);
					}
				}
				/*
				 * Optional<CustomerLocation> customerLocationEntity =
				 * customerLocationRepository .findByLocation_Id(loc.getId()); if
				 * (customerLocationEntity.isPresent()) {
				 * locationDetail.setCustomerId(customerLocationEntity.get().getErfCusCustomerId
				 * ()); }
				 */
				locationDetail.setLocationId(loc.getId());
				locationDetail.setPopId(loc.getPopLocationId());
				locationDetail.setLatLong(loc.getLatLong());
				locationDetails.add(locationDetail);
			});
		} else {
			throw new TclCommonException(ExceptionConstants.GET_LOCATION_COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		}catch(Exception e) {
			LOGGER.error("Error on getting location details",e);
			throw new TclCommonException(ExceptionConstants.GET_LOCATION_COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return locationDetails;
	}

	public LocationDetail getAddressForPopId(String popId) throws TclCommonException {
		LocationDetail locationDetail = new LocationDetail();
		if (org.apache.commons.lang3.StringUtils.isBlank(popId)) {
			throw new TclCommonException(ExceptionConstants.LOCATION_GET_ERROR, ResponseResource.R_CODE_ERROR);
		}

		Location locationEntity = locationRepository.findByPopLocationId(popId);
		if (locationEntity != null) {

			AddressDetail userAddress = new AddressDetail();
			AddressDetail apiAddress = new AddressDetail();
			locationDetail.setApiAddress(apiAddress);
			locationDetail.setUserAddress(userAddress);
			locationDetail.setPopId(locationEntity.getPopLocationId());
			locationDetail.setTier(locationEntity.getTier());
			Integer userAddressDetailId = locationEntity.getAddressId();
			Integer apiAddressDetail = locationEntity.getApiAddressId();
			Optional<MstAddress> userAddressEntity = mstAddressRepository.findById(userAddressDetailId);
			if (userAddressEntity.isPresent()) {
				constructAddress(userAddress, userAddressEntity.get(), locationEntity);
			}
			Optional<MstAddress> apiAddressEntity = mstAddressRepository.findById(apiAddressDetail);
			if (apiAddressEntity.isPresent()) {
				constructAddress(apiAddress, apiAddressEntity.get(), locationEntity);
			}
			/*
			 * Optional<CustomerLocation> customerLocationEntity =
			 * customerLocationRepository .findByLocation_Id(locationEntity.getId()); if
			 * (customerLocationEntity.isPresent()) {
			 * locationDetail.setCustomerId(customerLocationEntity.get().getErfCusCustomerId
			 * ()); }
			 */
			locationDetail.setLocationId(locationEntity.getId());
		} else {
			throw new TclCommonException(ExceptionConstants.LOCATION_GET_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return locationDetail;
	}

	public AddressDetail getAddressForMstAddressId(String mstAddressId) throws TclCommonException {
		AddressDetail addressDetail = new AddressDetail();
		if (org.apache.commons.lang3.StringUtils.isBlank(mstAddressId)) {
			throw new TclCommonException(ExceptionConstants.LOCATION_GET_ERROR, ResponseResource.R_CODE_ERROR);
		}

		Optional<MstAddress> address = mstAddressRepository.findById(Integer.valueOf(mstAddressId));
		if (address.isPresent()) {
			constructAddress(addressDetail, address.get(), null);
		}
		return addressDetail;
	}

	public AddressDetail getAddressForLocationId(String locationId) throws TclCommonException {
		AddressDetail addressDetail = new AddressDetail();
		Integer locId = Integer.parseInt(locationId);
		Optional<Location> location = locationRepository.findById(locId);
		Integer mstAddrId = location.get().getAddressId();
		if (org.apache.commons.lang3.StringUtils.isBlank(locationId)) {
			throw new TclCommonException(ExceptionConstants.LOCATION_GET_ERROR, ResponseResource.R_CODE_ERROR);
		}

		Optional<MstAddress> address = mstAddressRepository.findById(mstAddrId);
		if (address.isPresent() && location.isPresent()) {
			constructAddress(addressDetail, address.get(), location.get());
		}
		return addressDetail;
	}

	public List<AddressDetail> getAddressForListOfLocationId(List<Integer> locationIds) throws TclCommonException {

		List<AddressDetail> addressDetailList = new ArrayList<>();
		locationIds.stream().forEach(id->{
			AddressDetail addressDetail = new AddressDetail();
			Optional<Location> location= locationRepository.findById(id);
			Integer mstAddrId = location.get().getAddressId();
			if (org.apache.commons.lang3.StringUtils.isBlank(Integer.toString(id))) {
				try {
					throw new TclCommonException(ExceptionConstants.LOCATION_GET_ERROR, ResponseResource.R_CODE_ERROR);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
			Optional<MstAddress> address = mstAddressRepository.findById(mstAddrId);
			if (address.isPresent() && location.isPresent()) {
				constructAddress(addressDetail, address.get(),location.get());
			}
			addressDetailList.add(addressDetail);


		});
		return addressDetailList;
	}


	/**
	 * constructAddress - Construct the Address detail bean
	 * 
	 * @param userAddress
	 * @param userAddressEntity
	 */
	private AddressDetail constructAddress(AddressDetail userAddress, MstAddress userAddressEntity, Location loc) {
		userAddress.setAddressLineOne(Utils.removeUnicode(userAddressEntity.getAddressLineOne()));
		userAddress.setAddressLineTwo(Utils.removeUnicode(userAddressEntity.getAddressLineTwo()));
		userAddress.setSource(userAddressEntity.getSource());
		if (loc != null) {
			userAddress.setLatLong(loc.getLatLong());
			userAddress.setPopLocationId(loc.getPopLocationId());
		}
		userAddress.setCity(userAddressEntity.getCity());
		userAddress.setLocality(userAddressEntity.getLocality());
		userAddress.setPincode(userAddressEntity.getPincode());
		userAddress.setState(userAddressEntity.getState());
		userAddress.setCountry(userAddressEntity.getCountry());
		if(StringUtils.isNotEmpty(userAddressEntity.getCountry())) {
			MstCountry mstCountry = mstCountryRepository.findByName(userAddressEntity.getCountry());
			if(mstCountry!=null) {
				userAddress.setCountryCode(mstCountry.getCode());
			}
		}
		MstRegion mstRegionByCity = mstRegionRepository.findByCountryAndStateAndCity(userAddress.getCountry(),
				userAddress.getState(), userAddress.getCity());
		if (Objects.isNull(mstRegionByCity)) {
			MstRegion mstRegionByCoutry = mstRegionRepository
					.findByCountryAndStateAndCityIsNull(userAddress.getCountry(), userAddress.getState());
			if (Objects.nonNull(mstRegionByCoutry)) {
				userAddress.setRegion(mstRegionByCoutry.getRegionCode());
			}
		} else {
			userAddress.setRegion(mstRegionByCity.getRegionCode());
		}
		return userAddress;

	}

	/**
	 * persistLocation - Save location and customer location repo
	 * 
	 * @param locationDetail
	 * @param userAddressId
	 * @param apiAddressId
	 * @return Location
	 */
	private Location persistNPLLocation(LocationDetail locationDetail, Integer userAddressId, Integer apiAddressId,
			Location locationEntity, CustomerLocation customerLocationEntity) {
		if (locationEntity == null)
			locationEntity = new Location();
		locationEntity.setApiAddressId(apiAddressId);
		locationEntity.setAddressId(userAddressId);
		locationEntity.setCreatedDate(new Timestamp(new Date().getTime()));
		locationEntity.setIsActive(CommonConstants.BACTIVE);
		locationEntity.setLatLong(locationDetail.getUserAddress().getLatLong());
		locationEntity.setIsVerified(0);
		locationEntity.setPopLocationId(locationDetail.getPopId());
		locationRepository.save(locationEntity);
		if (customerLocationEntity == null)
			customerLocationEntity = new CustomerLocation();
		customerLocationEntity.setErfCusCustomerId(locationDetail.getCustomerId());
		customerLocationEntity.setLocation(locationEntity);
		customerLocationRepository.save(customerLocationEntity);
		return locationEntity;
	}

	/**
	 * 
	 * validateAddress - validate for mandatory parameters
	 * 
	 * 
	 * @throws TclCommonException
	 */
	private void validateAddress(AddressDetail address, boolean isUserAddress) throws TclCommonException {
		if (StringUtils.isEmpty(address.getAddressLineOne()))
			address.setAddressLineOne(LocationConstants.NA.toString());
		if (StringUtils.isEmpty(address.getAddressLineOne()) || StringUtils.isEmpty(address.getCity())
				|| StringUtils.isEmpty(address.getCountry()) || StringUtils.isEmpty(address.getPincode())
				|| StringUtils.isEmpty(address.getState())
				// || (StringUtils.isEmpty(address.getLocality()) && isUserAddress)
				|| (StringUtils.isEmpty(address.getLatLong()) && !isUserAddress)) {
			throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * post or update CustomerSiteLocationItContact details
	 *
	 * @param locationItDetails
	 */
	public void postLocationItContact(LocationItContact locationItDetails, Integer customerId)
			throws TclCommonException {

		if (Objects.isNull(locationItDetails) || Objects.isNull(customerId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		validateLocationITDetails(locationItDetails);

		try {
			CustomerSiteLocationItContact customerSiteLocationItContact = new CustomerSiteLocationItContact();

			Optional<CustomerLocation> customerLocation = customerLocationRepository
					.findByLocation_IdAndErfCusCustomerId(locationItDetails.getLocationId(), customerId);
			if (!customerLocation.isPresent())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			Optional<Location> location = locationRepository.findById(locationItDetails.getLocationId());
			if (!location.isPresent())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			Optional<LocationLeCustomer> locationLeCustomer = locationLeCustomerRepository
					.findByErfCusCustomerLeIdAndLocation(locationItDetails.getErfCustomerLeId(), location.get());
			if (!locationLeCustomer.isPresent())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			List<CustomerSiteLocationItContact> customerSiteLocationItContactList = customerLocationItInfoRepository
					.findByCustomerLocation(customerLocation.get());

			if (customerSiteLocationItContactList.isEmpty()) {

				customerSiteLocationItContact.setCustomerLocation(customerLocation.get());

				customerSiteLocationItContact.setName(locationItDetails.getName());
				customerSiteLocationItContact.setEmailId(locationItDetails.getEmail());
				customerSiteLocationItContact.setContactNumber(locationItDetails.getContactNo());
				customerSiteLocationItContact.setIsActive((byte) 1);
				customerSiteLocationItContact.setCustomerLeLocation(locationLeCustomer.get());

				customerLocationItInfoRepository.save(customerSiteLocationItContact);
			} else {
				customerSiteLocationItContactList.get(0).setName(locationItDetails.getName());
				customerSiteLocationItContactList.get(0).setContactNumber(locationItDetails.getContactNo());
				customerSiteLocationItContactList.get(0).setEmailId(locationItDetails.getEmail());
				customerSiteLocationItContactList.get(0).setCustomerLeLocation(locationLeCustomer.get());
				customerLocationItInfoRepository.save(customerSiteLocationItContactList.get(0));

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * 
	 * get CustomerSiteLocationItContact details
	 *
	 * @param locationItDetails
	 */
	public LocationItContact getLocationItContact(Integer locationId, Integer customerId) throws TclCommonException {
		LocationItContact customerSiteLocationItContact = new LocationItContact();

		if (Objects.isNull(locationId) || Objects.isNull(customerId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		try {

			Optional<CustomerLocation> customerLocation = customerLocationRepository
					.findByLocation_IdAndErfCusCustomerId(locationId, customerId);
			if (customerLocation.isPresent()) {
				List<CustomerSiteLocationItContact> customerSiteLocationItContactList = customerLocationItInfoRepository
						.findByCustomerLocationAndIsActive(customerLocation.get(), (byte) 1);
				if (!customerSiteLocationItContactList.isEmpty()) {
					CustomerSiteLocationItContact contact = customerSiteLocationItContactList.get(0);
					customerSiteLocationItContact.setLocationId(locationId);
					customerSiteLocationItContact.setContactNo(contact.getContactNumber());
					customerSiteLocationItContact.setEmail(contact.getEmailId());
					customerSiteLocationItContact.setName(contact.getName());
					customerSiteLocationItContact.setLocalItContactId(contact.getId());
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerSiteLocationItContact;

	}
	
	
	public List<LocationItContact> getCrossConnectLocationItContact(Integer locationId, Integer customerId) throws TclCommonException {
		List<LocationItContact> locationItContactList = new ArrayList<>();

		if (Objects.isNull(locationId) || Objects.isNull(customerId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		try {

			Optional<CustomerLocation> customerLocation = customerLocationRepository
					.findByLocation_IdAndErfCusCustomerId(locationId, customerId);
			if (customerLocation.isPresent()) {
				List<CustomerSiteLocationItContact> customerSiteLocationItContactList = customerLocationItInfoRepository
						.findByCustomerLocation(customerLocation.get());
				if (!customerSiteLocationItContactList.isEmpty()) {
					customerSiteLocationItContactList.stream().forEach(contact -> {
						LocationItContact customerSiteLocationItContact = new LocationItContact();
						customerSiteLocationItContact.setLocationId(locationId);
						customerSiteLocationItContact.setContactNo(contact.getContactNumber());
						customerSiteLocationItContact.setEmail(contact.getEmailId());
						customerSiteLocationItContact.setName(contact.getName());
						customerSiteLocationItContact.setLocalItContactId(contact.getId());
						customerSiteLocationItContact.setIsActive(contact.getIsActive());
						locationItContactList.add(customerSiteLocationItContact);
					});
					
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return locationItContactList;

	}

	private void validateLocationITDetails(LocationItContact locationdetails) throws TclCommonException {
		if (locationdetails.getLocationId() == null || locationdetails.getName() == null
				|| locationdetails.getEmail() == null || locationdetails.getContactNo() == null
				|| locationdetails.getErfCustomerLeId() == null) {
			throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * soft delete CustomerSiteLocationItContact details based on the location id
	 *
	 * @param locationId
	 */
	public String deleteLocationItContact(Integer locationId, Integer customerId) throws TclCommonException {

		String response = null;
		if (Objects.isNull(locationId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		try {

			Optional<CustomerLocation> customerLocation = customerLocationRepository
					.findByLocation_IdAndErfCusCustomerId(locationId, customerId);
			if (!customerLocation.isPresent())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			List<CustomerSiteLocationItContact> customerSiteLocationItContactList = customerLocationItInfoRepository
					.findByCustomerLocation(customerLocation.get());
			if (customerSiteLocationItContactList != null && !customerSiteLocationItContactList.isEmpty()) {
				customerSiteLocationItContactList.get(0).setIsActive((byte) 0);
				customerLocationItInfoRepository.save(customerSiteLocationItContactList.get(0));
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return response;

	}

	/**
	 * getLocationItContact- This method is used to get the localIT Contact
	 *
	 * @throws TclCommonException
	 */

	@Transactional
	public List<LocationItContact> getLocItContact(Set<Integer> locationItContactIds, Integer customerId)
			throws TclCommonException {

		List<LocationItContact> locationItContactList = new ArrayList<>();
		try {

			if (locationItContactIds == null)
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			locationItContactIds.forEach(locationId -> {
				LocationItContact response = new LocationItContact();

				Optional<CustomerLocation> customerLocation = customerLocationRepository
						.findByLocation_IdAndErfCusCustomerId(locationId, customerId);
				if (customerLocation.isPresent()) {
					List<CustomerSiteLocationItContact> customerSiteLocationItContactList = customerLocationItInfoRepository
							.findByCustomerLocationAndIsActive(customerLocation.get(), CommonConstants.BACTIVE);
					if (!customerSiteLocationItContactList.isEmpty()) {
						CustomerSiteLocationItContact contact = customerSiteLocationItContactList.get(0);
						response.setContactNo(contact.getContactNumber());
						response.setEmail(contact.getEmailId());
						response.setName(contact.getName());
						response.setLocationId(locationId);
						response.setLocalItContactId(contact.getId());
						if (contact.getCustomerLeLocation() != null) {
							response.setErfCustomerLeId(contact.getCustomerLeLocation().getErfCusCustomerLeId());

						}
						locationItContactList.add(response);
					}
				}
			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return locationItContactList;
	}

	@Transactional
	public List<LocationItContact> getAllLocalItContacts() throws TclCommonException {

		List<LocationItContact> locationItContactList = new ArrayList<>();
		try {
			Set<Integer> leIds = new HashSet<>();
			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			for (CustomerDetail customerDetail : customerDetails) {
				leIds.add(customerDetail.getCustomerLeId());
			}
			List<Map<String, Object>> localItcontactIds = locationLeCustomerRepository.findLoclItContactsByLeId(leIds);
			ObjectMapper mapper = new ObjectMapper();
			for (Map<String, Object> localItcontactId : localItcontactIds) {
				locationItContactList.add(mapper.convertValue(localItcontactId, LocationItContact.class));
			}
		} catch (

		Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return locationItContactList;
	}

	/**
	 * findSiteLocationITContacts with logged in user id for all the mapped legal
	 * entites. Returns the site / it contact details for all the legal entities.
	 * 
	 * @author NAVEEN GUNASEKARAN
	 * @return List<SiteLocationItContact> - returns the list of site location It
	 *         contact details
	 */
	public List<SiteLocationItContact> findSiteLocationITContacts() throws TclCommonException {
		List<SiteLocationItContact> siteLocationItContact = null;
		try {
			List<CustomerDetail> customerDetailsList = userInfoUtils.getCustomerDetails();

			List<Integer> legalEntityIds = customerDetailsList.stream().map(CustomerDetail::getCustomerLeId)
					.collect(Collectors.toList());

			List<LocationLeCustomer> locationLeCustomers = locationLeCustomerRepository
					.findByErfCusCustomerLeIdIn(legalEntityIds);

			List<Integer> addressIds = locationLeCustomers.stream()
					.map(eachLocationLeCustomer -> eachLocationLeCustomer.getLocation().getAddressId())
					.collect(Collectors.toList());

			List<MstAddress> addressList = mstAddressRepository.findByIdIn(addressIds);

			List<AddressDetail> addressDetailsList = getAddressByMasterAddressCodes(addressList);

			siteLocationItContact = locationLeCustomers.stream()
					.map(eachLeCustomer -> new SiteLocationItContact(eachLeCustomer, addressDetailsList,
							customerDetailsList))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siteLocationItContact;

	}

	/**
	 * Populates the address detail from mstaddress, because mstaddress contains
	 * only the ids
	 * 
	 * @author NAVEEN GUNASEKARAN getAddressByMasterAddressCodes
	 * @param addressList
	 * @return List<AddressDetail>
	 */
	private List<AddressDetail> getAddressByMasterAddressCodes(List<MstAddress> addressList) {
		List<AddressDetail> addressDetailsList = new ArrayList<>();
		for (MstAddress mstAddr : addressList) {
			AddressDetail addr = new AddressDetail();

			addr.setAddressLineOne(Utils.removeUnicode(mstAddr.getAddressLineOne()));
			addr.setAddressLineTwo(Utils.removeUnicode(mstAddr.getAddressLineTwo()));
			addr.setCity(mstAddr.getCity());
			addr.setCountry(mstAddr.getCountry());
			addr.setPincode(mstAddr.getPincode());
			addr.setState(mstAddr.getState());
			addr.setLocality(mstAddr.getLocality());
			addr.setAddressId(mstAddr.getId());
			addr.setSource(mstAddr.getSource());
			addr.setPlotBuilding(mstAddr.getPlotBuilding());
			addressDetailsList.add(addr);
		}
		return addressDetailsList;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateItLocationAndDemarcation
	 * @param request
	 * @throws TclCommonException
	 */
	public LocationResponse updateItLocationAndDemarcation(DemarcationAndItContactBean request)
			throws TclCommonException {
		LocationResponse loginResponse = new LocationResponse();
		try {

			validateRequest(request);
			CustomerSiteLocationItContact locationItContact = updateLocalItContact(request.getContact(),
					request.getCustomerId());
			Integer locationId = updateDemarcation(request.getDemarcation());
			if (locationItContact != null) {
				loginResponse.setLocalItContactId(locationItContact.getId());
			}
			loginResponse.setLocationId(locationId);
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		}
		return loginResponse;
	}
	
	public Integer updateCrossConnectItLocationAndDemarcation(CrossConnectLocalITDemarcationBean request)
			throws TclCommonException {
		//CrossConnectLocationResponse locationResponse = new CrossConnectLocationResponse();
		CCLocationMapping ccLocationMapping = new CCLocationMapping();
		try {

			if ((request.getContacts() == null || request.getContacts().isEmpty())
					&& (request.getDemarcations() == null || request.getDemarcations().isEmpty())) {
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
//			List<Integer> locationItContactIdList = new ArrayList<>();
//			Integer locId = request.getContactList().stream().filter(con -> con.getLocationId()!=null).findFirst().get().getLocationId();
//			request.getContactList().stream().forEach(contact -> {
//				byte isActive = 1;
//				try {
//					if(contact.getLocationId()==null) {
//						contact.setLocationId(locId);
//						isActive = 0;
//					}
//					locationItContactIdList.add(updateCrossConnectLocalItContact(contact, request.getCustomerId(),isActive).getId());
//				} catch (TclCommonException e) {
//					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
//				}
//			});
//			locationResponse.setLocalItContactIdList(locationItContactIdList);
//
//			List<Integer> locationIdList = updateCrossConnectDemarcation(request.getDemarcationList());
//			locationResponse.setLocationIdList(locationIdList);

			Demarcation demarcationSiteA = saveCrossConnectDemarcationSiteA(request.getDemarcations());
			Demarcation demarcationSiteZ = saveCrossConnectDemarcationSiteZ(request.getDemarcations());
			CCLocalITContact localITContactSiteA = saveCrossConnectLocalITContactSiteA(request.getContacts());
			CCLocalITContact localITContactSiteZ = saveCrossConnectLocalITContactSiteZ(request.getContacts());


			ccLocationMapping.setaEndLocId(localITContactSiteA.getId());
			ccLocationMapping.setzEndLocId(localITContactSiteZ.getId());
			ccLocationMapping.setaEndDemarcationId(demarcationSiteA.getId());
			ccLocationMapping.setzEndDemarcationId(demarcationSiteZ.getId());
			ccLocationMapping = ccLocMappingRepository.save(ccLocationMapping);

		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		return ccLocationMapping.getId();
	}

	private Demarcation saveCrossConnectDemarcationSiteA(List<CrossConnectDemarcations> demarcations) {
		// Save site A Demarcation
		Demarcation demarcationSiteA = demarcations.stream().map(crossConnectDemarcations -> {
			DemarcationBean demarcationBean = crossConnectDemarcations.getSiteA().stream().findFirst().get();
			Demarcation siteAdemarcation = new Demarcation();
			siteAdemarcation.setFloor(demarcationBean.getFloor());
			siteAdemarcation.setRoom(demarcationBean.getRoom());
			siteAdemarcation.setWing(demarcationBean.getWing());
			return demarcationRepository.save(siteAdemarcation);
		}).findFirst().get();

		return demarcationSiteA;
	}

	private Demarcation saveCrossConnectDemarcationSiteZ(List<CrossConnectDemarcations> demarcations) {
		// Save site Z Demarcation
		Demarcation demarcationSiteZ = demarcations.stream().map(crossConnectDemarcations -> {
			DemarcationBean demarcationBean = crossConnectDemarcations.getSiteZ().stream().findFirst().get();
			Demarcation siteZdemarcation = new Demarcation();
			siteZdemarcation.setFloor(demarcationBean.getFloor());
			siteZdemarcation.setRoom(demarcationBean.getRoom());
			siteZdemarcation.setWing(demarcationBean.getWing());
			return demarcationRepository.save(siteZdemarcation);
		}).findFirst().get();

		return demarcationSiteZ;
	}

	private CCLocalITContact saveCrossConnectLocalITContactSiteA(List<CrossConnectLocalITContacts> contacts) {
		// Save site A Local IT Contact
		CCLocalITContact ccLocalITContact = contacts.stream().map(crossConnectLocalITContacts -> {
			LocationItContact localITContact = crossConnectLocalITContacts.getSiteA().stream().findFirst().get();
			CCLocalITContact siteALocalITContact = new CCLocalITContact();
			siteALocalITContact.setName(localITContact.getName());
			siteALocalITContact.setContactNumber(localITContact.getContactNo());
			siteALocalITContact.setEmailId(localITContact.getEmail());
			return ccLocalITContactRepository.save(siteALocalITContact);
		}).findFirst().get();

		return ccLocalITContact;
	}

	private CCLocalITContact saveCrossConnectLocalITContactSiteZ(List<CrossConnectLocalITContacts> contacts) {
		// Save site Z Local IT Contact
		CCLocalITContact ccLocalITContact = contacts.stream().map(crossConnectLocalITContacts -> {
			LocationItContact localITContact = crossConnectLocalITContacts.getSiteZ().stream().findFirst().get();
			CCLocalITContact siteZLocalITContact = new CCLocalITContact();
			siteZLocalITContact.setName(localITContact.getName());
			siteZLocalITContact.setContactNumber(localITContact.getContactNo());
			siteZLocalITContact.setEmailId(localITContact.getEmail());
			return ccLocalITContactRepository.save(siteZLocalITContact);
		}).findFirst().get();

		return ccLocalITContact;
	}

	public CrossConnectLocalITDemarcationBean getCrossConnectLocalITContactAndDemarcation(Integer ccLocMappingID) {
		LOGGER.info("CC Loc Mapping ID :: {}", ccLocMappingID);
		CrossConnectLocalITDemarcationBean crossConnectLocalITDemarcationBean = new CrossConnectLocalITDemarcationBean();
		if(Objects.nonNull(ccLocMappingID)) {
			CCLocationMapping ccLocationMapping = ccLocMappingRepository.findById(ccLocMappingID).get();

			List<CrossConnectLocalITContacts> contacts = new ArrayList<>();
			List<CrossConnectDemarcations> demarcations = new ArrayList<>();
			CrossConnectLocalITContacts crossConnectLocalITContacts = new CrossConnectLocalITContacts();
			CrossConnectDemarcations crossConnectDemarcations = new CrossConnectDemarcations();

			// Set Site A Local IT Contact
			LocationItContact siteAlocationItContactBean = new LocationItContact();
			CCLocalITContact siteALocalITContact = ccLocalITContactRepository.findById(ccLocationMapping.getaEndLocId()).get();
			siteAlocationItContactBean.setContactNo(siteALocalITContact.getContactNumber());
			siteAlocationItContactBean.setEmail(siteALocalITContact.getEmailId());
			siteAlocationItContactBean.setName(siteALocalITContact.getName());
			crossConnectLocalITContacts.setSiteA(Arrays.asList(siteAlocationItContactBean));

			// Set Site Z Local IT Contact
			LocationItContact siteZlocationItContactBean = new LocationItContact();
			CCLocalITContact siteZLocalITContact = ccLocalITContactRepository.findById(ccLocationMapping.getzEndLocId()).get();
			siteZlocationItContactBean.setContactNo(siteZLocalITContact.getContactNumber());
			siteZlocationItContactBean.setEmail(siteZLocalITContact.getEmailId());
			siteZlocationItContactBean.setName(siteZLocalITContact.getName());
			crossConnectLocalITContacts.setSiteZ(Arrays.asList(siteZlocationItContactBean));
			contacts.add(crossConnectLocalITContacts);

			crossConnectLocalITDemarcationBean.setContacts(contacts);


			// Set Site A Demarcation Details
			DemarcationBean demarcationBeanSiteA = new DemarcationBean();
			Demarcation demarcationSiteA = demarcationRepository.findById(ccLocationMapping.getaEndDemarcationId()).get();
			demarcationBeanSiteA.setFloor(demarcationSiteA.getFloor());
			demarcationBeanSiteA.setRoom(demarcationSiteA.getRoom());
			demarcationBeanSiteA.setWing(demarcationSiteA.getWing());
			crossConnectDemarcations.setSiteA(Arrays.asList(demarcationBeanSiteA));

			DemarcationBean demarcationBeanSiteZ = new DemarcationBean();
			Demarcation demarcationSiteZ = demarcationRepository.findById(ccLocationMapping.getzEndDemarcationId()).get();
			demarcationBeanSiteZ.setFloor(demarcationSiteZ.getFloor());
			demarcationBeanSiteZ.setRoom(demarcationSiteZ.getRoom());
			demarcationBeanSiteZ.setWing(demarcationSiteZ.getWing());
			crossConnectDemarcations.setSiteZ(Arrays.asList(demarcationBeanSiteZ));
			demarcations.add(crossConnectDemarcations);

			crossConnectLocalITDemarcationBean.setDemarcations(demarcations);

		}
		LOGGER.info("crossConnectLocalITDemarcationBean :: {}", crossConnectLocalITDemarcationBean);
		return crossConnectLocalITDemarcationBean;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ processCustomerLeLocation used to
	 *       persist location against customerLegal entity
	 * @param request
	 * @throws TclCommonException
	 */
	public void processCustomerLeLocation(String request) throws TclCommonException {
		try {
			CustomerLeLocationBean customerLeLocationBean = (CustomerLeLocationBean) Utils.convertJsonToObject(request,
					CustomerLeLocationBean.class);
			if (customerLeLocationBean != null) {
				for (Integer locationId : customerLeLocationBean.getLocationIds()) {
					Optional<Location> opLocation = locationRepository.findById(locationId);
					if (opLocation.isPresent()) {
						Optional<LocationLeCustomer> leOptionalCustomer = locationLeCustomerRepository
								.findByErfCusCustomerLeIdAndLocation(customerLeLocationBean.getErfCustomerLeId(),
										opLocation.get());
						if (leOptionalCustomer.isPresent()) {
							persistLocationDetails(leOptionalCustomer.get(),
									customerLeLocationBean.getErfCustomerLeId(), opLocation.get());
						} else {
							LocationLeCustomer locationLeCustomer = new LocationLeCustomer();
							persistLocationDetails(locationLeCustomer, customerLeLocationBean.getErfCustomerLeId(),
									opLocation.get());
						}
						if (customerLeLocationBean.getCustomerId() != null) {
							Optional<CustomerLocation> customerLocationBean = customerLocationRepository
									.findByLocation_IdAndErfCusCustomerId(opLocation.get().getId(),
											customerLeLocationBean.getCustomerId());
							if (!customerLocationBean.isPresent()) {
								CustomerLocation customerLocation = new CustomerLocation();
								customerLocation.setErfCusCustomerId(customerLeLocationBean.getCustomerId());
								customerLocation.setLocation(opLocation.get());
								customerLocationRepository.save(customerLocation);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("error in persisting location{}", e);
		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateLocationDetails used to update
	 *       location deatils
	 * @param locationLeCustomer
	 * @param erfCustomerLeId
	 * @param location
	 */
	private void persistLocationDetails(LocationLeCustomer locationLeCustomer, Integer erfCustomerLeId,
			Location location) {
		locationLeCustomer.setErfCusCustomerLeId(erfCustomerLeId);
		locationLeCustomer.setLocation(location);
		locationLeCustomerRepository.save(locationLeCustomer);
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateDemarcation used to update
	 *       demarcation details
	 * @param request
	 * @throws TclCommonException
	 */
	private Integer updateDemarcation(DemarcationBean request) throws TclCommonException {
		Integer locationId = null;
		if (request != null) {

			Optional<Location> locationEntity = locationRepository.findById(request.getLocationId());
			if (!locationEntity.isPresent()) {
				throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);

			}
			Location location = locationEntity.get();

			if (location.getDemarcation() == null) {
				Demarcation demarcation = createDemarcation(request);
				location.setDemarcation(demarcation);
				locationRepository.save(location);
				locationId = location.getId();
			} else {
				Demarcation demarcation = updateDemarcation(location, location.getDemarcation(), request);
				location.setDemarcation(demarcation);
				locationRepository.save(location);
				locationId = location.getId();
			}

		}
		return locationId;
	}
	
	private List<Integer> updateCrossConnectDemarcation(List<DemarcationBean> requestList) throws TclCommonException {
		List<Integer> locationIdList = new ArrayList<>();
		Location commonLocation[] = new Location[1];
		DemarcationBean demarcWithoutLoc[] = new DemarcationBean[1];
		if (requestList != null && !requestList.isEmpty()) {
			requestList.stream().forEach(request -> {
				if (request.getLocationId() != null && request.getLocationId()!=0) {
					Optional<Location> locationEntity = locationRepository.findById(request.getLocationId());
					if (!locationEntity.isPresent()) {
						throw new TclCommonRuntimeException(ExceptionConstants.LOCATION_VALIDATION_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
					commonLocation[0] = locationEntity.get();

					if (commonLocation[0].getDemarcation() == null) {
						Demarcation demarcation = createDemarcation(request);
						commonLocation[0].setDemarcation(demarcation);
						locationRepository.save(commonLocation[0]);
						locationIdList.add(commonLocation[0].getId());
					} else {
						Demarcation demarcation = updateDemarcation(commonLocation[0],
								commonLocation[0].getDemarcation(), request);
						commonLocation[0].setDemarcation(demarcation);
						locationRepository.save(commonLocation[0]);
						locationIdList.add(commonLocation[0].getId());
					}
				} else {
					demarcWithoutLoc[0] = request;
				}

			});

			// add z-end location and demarc
			if (demarcWithoutLoc[0] != null && (demarcWithoutLoc[0].getLocationId() == null || demarcWithoutLoc[0].getLocationId()==0)
					&& commonLocation[0] != null) {
				Location location = new Location();
				location.setAddressId(commonLocation[0].getAddressId());
				location.setApiAddressId(commonLocation[0].getApiAddressId());
				//location.setCustomerLocations(commonLocation[0].getCustomerLocations());
				location.setCreatedBy(commonLocation[0].getCreatedBy());
				location.setCreatedDate(commonLocation[0].getCreatedDate());
				location.setDemarcation(createDemarcation(demarcWithoutLoc[0]));
				location.setEndDate(commonLocation[0].getEndDate());
				location.setIsActive(commonLocation[0].getIsActive());
				location.setIsVerified(commonLocation[0].getIsVerified());
				location.setLatLong(commonLocation[0].getLatLong());
				location = locationRepository.save(location);
				
				// save mapping 
				
				CCLocationMapping mapping = new CCLocationMapping();
				mapping.setaEndLocId(commonLocation[0].getId());
				mapping.setzEndLocId(location.getId());
				ccLocMappingRepository.save(mapping);
				locationIdList.add(location.getId());

			}

		}
		return locationIdList;
	}


	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateDemarcation used to update
	 *       demartion related details
	 * @param request
	 * @throws TclCommonException
	 */
	private Demarcation updateDemarcation(Location location, Demarcation demarcation, DemarcationBean request) {
		demarcation.setAppartment(request.getAppartment());
		demarcation.setBuildingAltitude(request.getBuildingAltitude());
		demarcation.setZone(request.getZone());
		demarcation.setWing(request.getWing());
		demarcation.setRoom(request.getRoom());
		demarcation.setFloor(request.getFloor());
		demarcation.getLocations().add(location);
		demarcation.setBuildingName(request.getBuildingName());
		demarcationRepository.save(demarcation);

		return demarcation;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ createDemarcation used to create
	 *       demarcation
	 * @param request
	 * @throws TclCommonException
	 */
	private Demarcation createDemarcation(DemarcationBean request) {

		Demarcation demarcation = new Demarcation();
		demarcation.setAppartment(request.getAppartment());
		demarcation.setBuildingAltitude(request.getBuildingAltitude());
		demarcation.setZone(request.getZone());
		demarcation.setWing(request.getWing());
		demarcation.setRoom(request.getRoom());
		demarcation.setFloor(request.getFloor());
		demarcation.setBuildingName(request.getBuildingName());
		demarcationRepository.save(demarcation);

		return demarcation;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateLocalItContact used to update
	 *       local it contact
	 * @param request
	 * @throws TclCommonException
	 */
	private CustomerSiteLocationItContact updateLocalItContact(LocationItContact contact, Integer customerId)
			throws TclCommonException {
		CustomerSiteLocationItContact locationItContact = null;
		if (contact != null) {
			
			Optional<CustomerLocation> customerLocation = customerLocationRepository
					.findByLocation_IdAndErfCusCustomerId(contact.getLocationId(), customerId);

			if (!customerLocation.isPresent()) {
				throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);

			}
			List<CustomerSiteLocationItContact> customerSiteLocationItContactList = customerLocationItInfoRepository
					.findByCustomerLocationAndIsActive(customerLocation.get(), CommonConstants.BACTIVE);

			Optional<LocationLeCustomer> customerLeLocation = locationLeCustomerRepository
					.findByErfCusCustomerLeIdAndLocation(contact.getErfCustomerLeId(),
							customerLocation.get().getLocation());
			if (!customerLeLocation.isPresent()) {
				throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}

			if (!Objects.isNull(contact.getLocalItContactId()) && contact.getLocalItContactId() != 0) {
				Optional<CustomerSiteLocationItContact> localItContact = customerLocationItInfoRepository
						.findById(contact.getLocalItContactId());
				if (localItContact.isPresent())
					locationItContact = updateSiteItContact(localItContact.get(), contact, customerLeLocation.get());

			} else {
				for (CustomerSiteLocationItContact customerSiteLocationItContact : customerSiteLocationItContactList) {
					customerSiteLocationItContact.setIsActive((byte) 0);
					customerLocationItInfoRepository.save(customerSiteLocationItContact);
				}
				locationItContact = createCustomerSiteItContact(contact, customerLocation.get(),
						customerLeLocation.get());

			}

		}
		return locationItContact;

	}

	private CustomerSiteLocationItContact updateCrossConnectLocalItContact(LocationItContact contact, Integer customerId,byte isActive)
			throws TclCommonException {
		CustomerSiteLocationItContact locationItContact = null;
		if (contact != null) {
			
			Optional<CustomerLocation> customerLocation = customerLocationRepository
					.findByLocation_IdAndErfCusCustomerId(contact.getLocationId(), customerId);

			if (!customerLocation.isPresent()) {
				throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);

			}
			/*List<CustomerSiteLocationItContact> customerSiteLocationItContactList = customerLocationItInfoRepository
					.findByCustomerLocationAndIsActive(customerLocation.get(), CommonConstants.BACTIVE);
*/
			Optional<LocationLeCustomer> customerLeLocation = locationLeCustomerRepository
					.findByErfCusCustomerLeIdAndLocation(contact.getErfCustomerLeId(),
							customerLocation.get().getLocation());
			if (!customerLeLocation.isPresent()) {
				throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}

			if (!Objects.isNull(contact.getLocalItContactId()) && contact.getLocalItContactId() != 0) {
				Optional<CustomerSiteLocationItContact> localItContact = customerLocationItInfoRepository
						.findById(contact.getLocalItContactId());
				if (localItContact.isPresent())
					locationItContact = updateSiteItContact(localItContact.get(), contact, customerLeLocation.get());

			} else {
				/*for (CustomerSiteLocationItContact customerSiteLocationItContact : customerSiteLocationItContactList) {
					customerSiteLocationItContact.setIsActive((byte) 0);
					customerLocationItInfoRepository.save(customerSiteLocationItContact);
				}*/
				locationItContact = createCrossConnectCustomerSiteItContact(contact, customerLocation.get(),
						customerLeLocation.get(),isActive);

			}

		}
		return locationItContact;

	}
	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateSiteItContact used to update
	 *       siteItContact
	 * @throws TclCommonException
	 */
	private CustomerSiteLocationItContact updateSiteItContact(
			CustomerSiteLocationItContact customerSiteLocationItContact, LocationItContact contact,
			LocationLeCustomer locationLeCustomer) {
		// to do need to discuss as getting the first object

		customerSiteLocationItContact.setName(contact.getName());
		customerSiteLocationItContact.setContactNumber(contact.getContactNo());
		customerSiteLocationItContact.setEmailId(contact.getEmail());
		customerSiteLocationItContact.setCustomerLeLocation(locationLeCustomer);
		customerLocationItInfoRepository.save(customerSiteLocationItContact);
		return customerSiteLocationItContact;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ createCustomerSiteItContact used to
	 *       create site location
	 * @throws TclCommonException
	 */
	private CustomerSiteLocationItContact createCustomerSiteItContact(LocationItContact contact,
			CustomerLocation customerLocation, LocationLeCustomer locationLeCustomer) {
		CustomerSiteLocationItContact customerSiteLocationItContact = new CustomerSiteLocationItContact();

		customerSiteLocationItContact.setCustomerLocation(customerLocation);
		customerSiteLocationItContact.setName(contact.getName());
		customerSiteLocationItContact.setEmailId(contact.getEmail());
		customerSiteLocationItContact.setContactNumber(contact.getContactNo());
		customerSiteLocationItContact.setIsActive((byte) 1);
		customerSiteLocationItContact.setCustomerLeLocation(locationLeCustomer);
		customerLocationItInfoRepository.save(customerSiteLocationItContact);

		return customerSiteLocationItContact;
	}
	
	private CustomerSiteLocationItContact createCrossConnectCustomerSiteItContact(LocationItContact contact,
			CustomerLocation customerLocation, LocationLeCustomer locationLeCustomer,byte isActive) {
		CustomerSiteLocationItContact customerSiteLocationItContact = new CustomerSiteLocationItContact();

		customerSiteLocationItContact.setCustomerLocation(customerLocation);
		customerSiteLocationItContact.setName(contact.getName());
		customerSiteLocationItContact.setEmailId(contact.getEmail());
		customerSiteLocationItContact.setContactNumber(contact.getContactNo());
		customerSiteLocationItContact.setIsActive(isActive);
		customerSiteLocationItContact.setCustomerLeLocation(locationLeCustomer);
		customerLocationItInfoRepository.save(customerSiteLocationItContact);

		return customerSiteLocationItContact;
	}

	/**
	 * validateRequest
	 * 
	 * @param request
	 */
	private void validateRequest(DemarcationAndItContactBean request) throws TclCommonException {
		if (request.getContact() == null && request.getDemarcation() == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		}
	}

	/**
	 * Pending implementation loadLocationFromXLS Method to load locations from XLS
	 * file
	 * 
	 * @param multipartFile
	 * @return String
	 * @throws TclCommonException
	 */
	/*
	 * public String loadLocationFromXLS(MultipartFile multipartFile) throws
	 * TclCommonException { Map<Integer, List<String>> data = new HashMap<>();
	 * 
	 * try { InputStream stream = multipartFile.getInputStream(); XSSFWorkbook
	 * workbook = new XSSFWorkbook(stream); XSSFSheet sheet =
	 * workbook.getSheetAt(0);
	 * 
	 * int i = 0; for (Row row : sheet) { data.put(i, new ArrayList<String>()); for
	 * (Cell cell : row) { switch (cell.getCellTypeEnum()) { case STRING:
	 * data.get(i).add(cell.getRichStringCellValue().getString()); break; case
	 * NUMERIC: data.get(i).add(String.valueOf((int) cell.getNumericCellValue()));
	 * break; default: data.get(i).add(null); } } i++; } workbook.close(); } catch
	 * (Exception e) { throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
	 * ResponseResource.R_CODE_ERROR); } return data.toString(); }
	 */

	/**
	 * loadMyLocationByLegalEntityId gets the list of location with legal entity ids
	 * 
	 * @author NAVEEN GUNASEKARAN
	 * @return List<CustomerDetails> - returns the list of customer legal entity
	 *         location details
	 */
	/*
	 * public List<CustomerDetails> loadMyLocationByLegalEntityId() throws
	 * TclCommonException { List<CustomerDetails> customerLeLocationList = new
	 * ArrayList<>(); try { List<CustomerDetail> customerDetailsList =
	 * userInfoUtils.getCustomerDetails();
	 * 
	 * List<Integer> legalEntityIds = customerDetailsList.stream()
	 * .map(eachCustDetail ->
	 * eachCustDetail.getCustomerLeId()).collect(Collectors.toList());
	 * 
	 * List<LocationLeCustomer> locationLeCustomerList =
	 * locationLeCustomerRepository .findByErfCusCustomerLeIdIn(legalEntityIds);
	 * 
	 * List<Integer> addressIds = locationLeCustomerList.stream()
	 * .map(eachLeCustomer ->
	 * eachLeCustomer.getLocation().getAddressId()).collect(Collectors.toList());
	 * 
	 * List<MstAddress> mstAddressList =
	 * mstAddressRepository.findByIdIn(addressIds);
	 * 
	 * List<AddressDetail> addressDetailsList =
	 * getAddressByMasterAddressCodes(mstAddressList);
	 * 
	 * customerDetailsList.stream().forEach(customerDetail -> customerLeLocationList
	 * .add(new CustomerDetails(customerDetail, locationLeCustomerList,
	 * addressDetailsList)));
	 * 
	 * } catch (Exception e) { throw new
	 * TclCommonException(ExceptionConstants.COMMON_ERROR,
	 * ResponseResource.R_CODE_ERROR); } return customerLeLocationList; }
	 */

	/**
	 * loadMyLocationByCustomerId gets the list of location with customer ids
	 * 
	 * @author NAVEEN GUNASEKARAN
	 * @return List<CustomerDetails> - returns the list of customer legal entity
	 *         location details
	 */
	/*
	 * public List<CustomerDetails> loadMyLocationByCustomerId() throws
	 * TclCommonException { List<CustomerDetails> customerLeLocationList = new
	 * ArrayList<>(); try { List<CustomerDetail> customerDetailsList =
	 * userInfoUtils.getCustomerDetails(); List<Integer> customerErfIdList =
	 * customerDetailsList.stream() .map(eachCustDetail ->
	 * eachCustDetail.getErfCustomerId()).collect(Collectors.toList());
	 * 
	 * List<CustomerLocation> customerLocationList = customerLocationRepository
	 * .findByErfCusCustomerIdIn(customerErfIdList);
	 * 
	 * List<Integer> addressIds = customerLocationList.stream() .map(eachLeCustomer
	 * -> eachLeCustomer.getLocation().getAddressId()).collect(Collectors.toList());
	 * 
	 * List<MstAddress> mstAddressList =
	 * mstAddressRepository.findByIdIn(addressIds);
	 * 
	 * List<AddressDetail> addressDetailsList =
	 * getAddressByMasterAddressCodes(mstAddressList);
	 * 
	 * customerDetailsList.stream().forEach(customerDetail -> customerLeLocationList
	 * .add(new CustomerDetails(customerLocationList, customerDetail,
	 * addressDetailsList)));
	 * 
	 * } catch (Exception e) { throw new
	 * TclCommonException(ExceptionConstants.COMMON_ERROR,
	 * ResponseResource.R_CODE_ERROR); } return customerLeLocationList; }
	 */

	public LocationResponse updateLatLong(Integer locationId, LocationDetail locationDetail) throws TclCommonException {
		if (Objects.isNull(locationId) || Objects.isNull(locationDetail)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}

		LocationResponse locationResp = new LocationResponse();
		Location existLocation = null;
		try {
			Optional<Location> existingLocation = locationRepository.findById(locationId);
			if (existingLocation.isPresent()) {
				existLocation = existingLocation.get();
				existLocation.setLatLong(locationDetail.getLatLong());
			}
			if (!Objects.isNull(existLocation)) {
				locationRepository.save(existLocation);
				locationResp.setLocationId(locationId);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return locationResp;
	}

	/**
	 * 
	 * loadLocationsDetails
	 * 
	 * @author Manojkumar R
	 * 
	 * @return List<AddressDetail>
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public List<AddressDetail> loadLocationsDetails(Integer customerId) throws TclCommonException {
		List<AddressDetail> addressDetailsList = new ArrayList<>();
		try {
			Set<Integer> leIds = new HashSet<>();
			if (userInfoUtils.getUserType().equalsIgnoreCase(INTERNAL_USERS.toString()) && customerId != null) {
				LOGGER.info("Calling customer consumer for customer Id {}", customerId);
				LOGGER.info("MDC Filter token value in before Queue call loadLocationsDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				Set<Integer> customerLeIds = (Set<Integer>) Utils.convertJsonToObject(
						(String) mqUtils.sendAndReceive(customerLeToCustomerQueue, String.valueOf(customerId)),
						Set.class);
				LOGGER.info("customerLes {} received for customer Id {}", customerLeIds, customerId);
				List<Map<String, Object>> customerLeLocList = locationLeCustomerRepository
						.findLocationsByLe(customerLeIds);
				ObjectMapper mapper = new ObjectMapper();
				for (Map<String, Object> locationLeCustomer : customerLeLocList) {
					addressDetailsList.add(mapper.convertValue(locationLeCustomer, AddressDetail.class));
				}
			} else {
				LOGGER.info("Calling customer consumer for customer Id {}", customerId);
				List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
				if (customerDetails != null) {
					for (CustomerDetail customerDetail : customerDetails) {
						leIds.add(customerDetail.getCustomerLeId());
					}

					List<Map<String, Object>> customerLeLocList = locationLeCustomerRepository.findLocationsByLe(leIds);
					ObjectMapper mapper = new ObjectMapper();
					for (Map<String, Object> locationLeCustomer : customerLeLocList) {
						addressDetailsList.add(mapper.convertValue(locationLeCustomer, AddressDetail.class));
					}
				}
			}
			if (!addressDetailsList.isEmpty()) {
				addressDetailsList.sort(Comparator.comparing(AddressDetail::getCity));
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return addressDetailsList;
	}

	/**
	 * Get Location Details Based on Customer
	 *
	 * @param customerId
	 * @return {@link List<AddressDetail>}
	 * @throws TclCommonException
	 */
	public List<AddressDetail> loadLocationsDetailsByCustomer(Integer customerId) throws TclCommonException {
		List<AddressDetail> addressDetailsList = new ArrayList<>();
		try {
			if (customerId != null) {
				LOGGER.info("Calling customer consumer for customer Id {}", customerId);
				LOGGER.info("MDC Filter token value in before Queue call loadLocationsDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				Set<Integer> customerLeIds = (Set<Integer>) Utils.convertJsonToObject(
						(String) mqUtils.sendAndReceive(customerLeToCustomerQueue, String.valueOf(customerId)),
						Set.class);
				LOGGER.info("customerLes {} received for customer Id {}", customerLeIds, customerId);
				List<Map<String, Object>> customerLeLocList = locationLeCustomerRepository
						.findLocationsByLe(customerLeIds);
				ObjectMapper mapper = new ObjectMapper();
				for (Map<String, Object> locationLeCustomer : customerLeLocList) {
					addressDetailsList.add(mapper.convertValue(locationLeCustomer, AddressDetail.class));
				}
			}
			if (!addressDetailsList.isEmpty()) {
				addressDetailsList.sort(Comparator.comparing(AddressDetail::getCity));
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return addressDetailsList;
	}

	/**
	 * This method returns the demarcation deatils for given location id
	 * 
	 * 
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	public DemarcationBean getlocationDemarcationDetailsByLocationID(Integer locationId) throws TclCommonException {
		DemarcationBean demarcationBean = new DemarcationBean();
		try {
			Optional<Location> location = locationRepository.findById(locationId);
			if (location.isPresent() && location.get().getDemarcation() != null) {
				Demarcation demarcation = location.get().getDemarcation();
				demarcationBean.setAppartment(demarcation.getAppartment());
				demarcationBean.setBuildingAltitude(demarcation.getBuildingAltitude());
				demarcationBean.setFloor(demarcation.getFloor());
				demarcationBean.setLocationId(locationId);
				demarcationBean.setRoom(demarcation.getRoom());
				demarcationBean.setTower(demarcation.getTower());
				demarcationBean.setWing(demarcation.getWing());
				demarcationBean.setZone(demarcation.getZone());
				demarcationBean.setBuildingName(demarcation.getBuildingName());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return demarcationBean;
	}
	
	/**
	 * This method returns the demarcation deatils for given location id for crossconnect
	 * 
	 * 
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	public List<DemarcationBean> getCClocationDemarcationDetailsByLocationID(Integer locationId)
			throws TclCommonException {
		List<DemarcationBean> demarcationBeanList = new ArrayList<>();
		List<DemarcationBean> demarcationList = new ArrayList<>();
		if (locationId!=null) {
			List<Integer> locationIdList = new ArrayList<>();
			locationIdList.add(locationId);

			List<CCLocationMapping> locMappingList = ccLocMappingRepository.findByAEndLocId(Integer.valueOf(locationIdList.get(0)));
			CCLocationMapping locMapping = locMappingList.stream().skip(locMappingList.size() - 1).findFirst().orElse(new CCLocationMapping());
			Integer[] zendLoctionId= {0};
			if (locMapping != null && locMapping.getzEndLocId() != null) {
				locationIdList.add(locMapping.getzEndLocId());
				 zendLoctionId[0]=locMapping.getzEndLocId();
			}

			locationIdList.stream().forEach(id -> {
				try {
					demarcationBeanList.add(getlocationDemarcationDetailsByLocationID(id));
				} catch (NumberFormatException | TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}
			});
			demarcationBeanList.stream().forEach(demarbean -> {
				if(demarbean.getLocationId()!=zendLoctionId[0]) {
					demarbean.setAendDemarc(true);
				}
				else {
					demarbean.setAendDemarc(false);
				}
				demarcationList.add(demarbean);
				
			
			});
		}
		return demarcationList;

	}

	/**
	 * This method returns the address details for the customer details input
	 * 
	 * 
	 * @param none
	 * @return List<SiteLocationItContact>
	 * @throws TclCommonException
	 */
	public List<SiteLocationItContact> getAddressBasedOnCustomer() throws TclCommonException {

		List<SiteLocationItContact> siteLocationItContactList = new ArrayList<>();
		try {
			List<CustomerDetail> customerDetailsList = userInfoUtils.getCustomerDetails();
			customerDetailsList.stream().forEach(customerDetail -> {
				SiteLocationItContact siteLocationItContact = new SiteLocationItContact();
				siteLocationItContact.setCustomerId(customerDetail.getCustomerId());
				siteLocationItContact.setCustomerName(customerDetail.getCustomerName());
				siteLocationItContact.setLegalEntityId(customerDetail.getCustomerLeId());
				List<LocationLeCustomer> locationLeCustomerList = locationLeCustomerRepository
						.findByErfCusCustomerLeId(customerDetail.getCustomerLeId());
				List<AddressDetail> addressDetailList = new ArrayList<>();
				locationLeCustomerList.stream().forEach(locationLeCustomer -> {
					AddressDetail addressDetail = new AddressDetail();
					CustomerSiteLocationItContact customerSiteLocationItcontact = customerLocationItInfoRepository
							.findByCustomerLeLocationAndIsActive(locationLeCustomer, (byte) 1);
					if (!Objects.isNull(customerSiteLocationItcontact)) {
						LocationItContact locationItContact = new LocationItContact();
						locationItContact.setContactNo(customerSiteLocationItcontact.getContactNumber());
						locationItContact.setEmail(customerSiteLocationItcontact.getEmailId());
						locationItContact.setName(customerSiteLocationItcontact.getName());
						addressDetail.setLocationItContact(locationItContact);
						Optional<MstAddress> mstAddress = mstAddressRepository.findById(
								customerSiteLocationItcontact.getCustomerLocation().getLocation().getAddressId());
						AddressDetail addresDetail = null;
						if (mstAddress.isPresent()) {
							addresDetail = constructAddressDetail(addressDetail, mstAddress.get());

						}
						if (!StringUtils.isEmpty(
								customerSiteLocationItcontact.getCustomerLocation().getLocation().getLatLong())) {
							addresDetail.setLatLong(
									customerSiteLocationItcontact.getCustomerLocation().getLocation().getLatLong());
						}
						addressDetailList.add(addresDetail);

						siteLocationItContact.setAddress(addressDetailList);
					}
				});

				siteLocationItContactList.add(siteLocationItContact);
			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return siteLocationItContactList;
	}

	private AddressDetail constructAddressDetail(AddressDetail addressDetail, MstAddress mstAddress) {

		addressDetail.setAddressLineOne(Utils.removeUnicode(mstAddress.getAddressLineOne()));
		addressDetail.setAddressLineTwo(Utils.removeUnicode(mstAddress.getAddressLineTwo()));
		addressDetail.setAddressId(mstAddress.getId());
		addressDetail.setPlotBuilding(mstAddress.getPlotBuilding());
		addressDetail.setCountry(mstAddress.getCountry());
		addressDetail.setCity(mstAddress.getCity());
		addressDetail.setLocality(mstAddress.getLocality());
		addressDetail.setPincode(mstAddress.getPincode());
		addressDetail.setState(mstAddress.getState());
		addressDetail.setSource(mstAddress.getSource());

		return addressDetail;

	}

	public String getApiAddressOfAllLocationsAndReturn(String responseBody) throws TclCommonException {
		AddressDetail apiAddress = null;
		if (responseBody != null && !responseBody.isEmpty()) {
			String[] locationIds = responseBody.split(",");
			List<Integer> locIds = new ArrayList<>();
			if (locationIds.length > 0) {
				for (String location : locationIds) {
					locIds.add(Integer.valueOf(location));
				}
			}
			Optional<Location> locationDetails = locationRepository.findById(locIds.get(0));
			apiAddress = new AddressDetail();
			constructAddressIfLocationIsPresent(locationDetails, apiAddress);
		}

		return Utils.convertObjectToJson(apiAddress);
	}

	public void constructAddressIfLocationIsPresent(Optional<Location> locationDetails, AddressDetail apiAddress) {
		if (locationDetails.isPresent()) {
			if (locationDetails.get().getAddressId() != null) {
				Optional<MstAddress> address = mstAddressRepository.findById(locationDetails.get().getAddressId());
				if (address.isPresent()) {
					constructAddress(apiAddress, address.get(), locationDetails.get());
				}

			} else if (locationDetails.get().getApiAddressId() != null) {
				Optional<MstAddress> mstApiAddress = mstAddressRepository
						.findById(locationDetails.get().getApiAddressId());

				if (mstApiAddress.isPresent()) {
					constructAddress(apiAddress, mstApiAddress.get(), locationDetails.get());

				}
			}
		}
	}

	/**
	 * This method returns the local it contact details for the given local it
	 * contact id
	 * 
	 * 
	 * @param localItcontact id
	 * @return List<SiteLocationItContact>
	 * @throws TclCommonException
	 */
	public LocationItContact findSiteLocationITContactById(Integer localItContactId) throws TclCommonException {
		LocationItContact localItContact = new LocationItContact();
		if (Objects.isNull(localItContactId) || localItContactId == 0) {
			return localItContact;
		}
		try {
			Optional<CustomerSiteLocationItContact> localItContactEntity = customerLocationItInfoRepository
					.findById(localItContactId);
			if (localItContactEntity.isPresent()) {
				localItContact.setContactNo(localItContactEntity.get().getContactNumber());
				localItContact.setName(localItContactEntity.get().getName());
				localItContact.setEmail(localItContactEntity.get().getEmailId());
				localItContact.setLocalItContactId(localItContactEntity.get().getId());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return localItContact;

	}

	public List<LocationItContact> findCCSiteLocationITContactById(String localItContactIds) throws TclCommonException {
		List<LocationItContact> locationItContactList = new ArrayList<>();
		if(StringUtils.isNotEmpty(localItContactIds)) {
			List<String> localItContactIdList = Arrays.asList(localItContactIds.split(","));
			if(localItContactIdList != null && !localItContactIdList.isEmpty()) {
				localItContactIdList.stream().forEach(id -> {
					try {
						locationItContactList.add(findSiteLocationITContactById(Integer.valueOf(id)));
					} catch (NumberFormatException | TclCommonException e) {
						throw new TclCommonRuntimeException(e);
					}
				});
			}
		}
		return locationItContactList;
	}
		
	/**
	 * locationsExcelUpload - multiple location excel upload, validates and uploads
	 * the multiple locations to location database. If any validation issues then
	 * List<LocationUploadValidationBean> will be returned If location insertion
	 * success then List<LocationMultipleResponse> will be returned
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public Object locationsExcelUpload(MultipartFile file) throws TclCommonException {
		String noResponse = "";
		try {
			List<LocationOfferingDetail> locationDetailList = Collections.synchronizedList(new ArrayList<>());
			
			Set<LocationUploadValidationBean> validationList = validateUploadedDetails(file, locationDetailList);
			
			if (!validationList.isEmpty()) {
				return validationList;
			} else {
				if (!locationDetailList.isEmpty()) {
					return addAddressesOffering(locationDetailList);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return noResponse;
	}

	/**
	 * locationsExcelUpload - multiple location excel upload, validates and uploads
	 * the multiple locations to location database. If any validation issues then
	 * List<LocationUploadValidationBean> will be returned If location insertion
	 * success then List<LocationMultipleResponse> will be returned
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public Object locationsExcelUploadIas(MultipartFile file) throws TclCommonException {
		String noResponse = "";
		try {
			List<LocationOfferingDetail> locationDetailList = Collections.synchronizedList(new ArrayList<>());

			
			Set<LocationUploadValidationBean> validationList = validateUploadedDetailsIas(file, locationDetailList);
			
			if (!validationList.isEmpty()) {
				return validationList;
			} else {
				if (!locationDetailList.isEmpty()) {
					return addAddressesOffering(locationDetailList);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return noResponse;
	}
	
	
	/**
	 * locationsExcelUpload - multiple location excel upload, validates and uploads
	 * the multiple locations to location database. If any validation issues then
	 * List<LocationUploadValidationBean> will be returned If location insertion
	 * success then List<LocationMultipleResponse> will be returned
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public Object locationsExcelUploadIwan(MultipartFile file) throws TclCommonException {
		String noResponse = "";
		try {
			List<LocationOfferingDetail> locationDetailList = Collections.synchronizedList(new ArrayList<>());

			
			Set<LocationUploadValidationBean> validationList = validateUploadedDetailsIas(file, locationDetailList);
			
			if (!validationList.isEmpty()) {
				return validationList;
			} else {
				if (!locationDetailList.isEmpty()) {
					return addAddressesOffering(locationDetailList);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return noResponse;
	}


	/**
	 * downloadLocationTemplate - takes the country list and profile names as input
	 * and generates the dynamic excel template with the default dropdown list
	 * values for country and profiles.
	 * 
	 * @param locationTemplate
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public void downloadLocationTemplate(LocationTemplateRequest locationTemplate, HttpServletResponse response)
			throws IOException, TclCommonException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
			header.createCell(1).setCellValue(LocationUploadConstants.HEADER_COUNTRY);
			header.createCell(2).setCellValue(LocationUploadConstants.HEADER_STATE);
			header.createCell(3).setCellValue(LocationUploadConstants.HEADER_CITY);
			header.createCell(4).setCellValue(LocationUploadConstants.HEADER_PINCODE);
			header.createCell(5).setCellValue(LocationUploadConstants.HEADER_ADDRESS);
			header.createCell(6).setCellValue(LocationUploadConstants.HEADER_PROFILES);

			for (int i = 0; i < 7; i++) {
				CellStyle stylerowHeading = workbook.createCellStyle();
				stylerowHeading.setBorderBottom(BorderStyle.THICK);
				stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
				stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setFontHeightInPoints((short) 10);
				font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
				stylerowHeading.setFont(font);
				stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
				stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

				stylerowHeading.setWrapText(true);
				header.getCell(i).setCellStyle(stylerowHeading);

				DataFormat fmt = workbook.createDataFormat();
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
				sheet.setDefaultColumnStyle(i, textStyle);
			}

			/* country validation begins */
			DataValidation countryDataValidation = null;
			DataValidationConstraint countryConstraint = null;
			DataValidationHelper countryValidationHelper = null;

			countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			CellRangeAddressList countryAddressList = new CellRangeAddressList(1, 50, 1, 1);
			// populate with the list of countries from product catalog
			countryConstraint = countryValidationHelper
					.createExplicitListConstraint(locationTemplate.getCountries().stream().toArray(String[]::new));
			countryDataValidation = countryValidationHelper.createValidation(countryConstraint, countryAddressList);
			countryDataValidation.setSuppressDropDownArrow(true);
			countryDataValidation.setShowErrorBox(true);
			countryDataValidation.setShowPromptBox(true);
			sheet.addValidationData(countryDataValidation);
			sheet.setColumnWidth(0, 1800);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 7500);
			sheet.setColumnWidth(5, 10000);
			sheet.setColumnWidth(6, 8000);

			/* profile validation begins */
			DataValidation profileDataValidation = null;
			DataValidationConstraint profileConstraint = null;
			DataValidationHelper profileValidationHelper = null;
			profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			CellRangeAddressList profileAddressList = new CellRangeAddressList(1, 50, 6, 6);
			// add the selected profiles
			profileConstraint = profileValidationHelper
					.createExplicitListConstraint(locationTemplate.getProfiles().stream().toArray(String[]::new));
			profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
			profileDataValidation.setSuppressDropDownArrow(true);
			profileDataValidation.setShowErrorBox(true);
			profileDataValidation.setShowPromptBox(true);
			sheet.addValidationData(profileDataValidation);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = "location-template.xlsx";
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			try {
				FileCopyUtils.copy(outArray, response.getOutputStream());
			} catch (Exception e) {

			}

			outByteStream.flush();
			outByteStream.close();

		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing downloadLocationTemplate malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.MALFORMED_URL_EXCEPTION, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadLocationTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
	}
	
	// izoSdwan Excel download dataValidation

	public DataValidation izoSdwandataValidation(ArrayList<String> request, String sheetName, Sheet sheet, int x,
			int y) {
		DataValidation dataValidation = null;
		DataValidationConstraint constraint = null;
		DataValidationHelper validationHelper = null;
		validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 200, x, y);
		if (x == 1 || x == 9 || x == 10 || x == 14 || x == 15) {
			constraint = validationHelper.createFormulaListConstraint(sheetName);
		} else {
			constraint = validationHelper.createExplicitListConstraint(request.stream().toArray(String[]::new));
		}
		dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setSuppressDropDownArrow(true);
		dataValidation.setShowErrorBox(true);
		dataValidation.setShowPromptBox(true);
		return dataValidation;
	}

	// izosdwan download excel
	@SuppressWarnings("unchecked")
	public void downloadIzoSdwanByonTemplate(LocationTemplateRequest locationTemplate, HttpServletResponse response)
			throws IOException, TclCommonException {
		String headerName;
		Integer index;
		ArrayList<String> country = (ArrayList<String>) locationTemplate.getCountries();
		ArrayList<String> internetQuality = new ArrayList<>(Arrays.asList("Enterprise Grade", "Retail Grade"));
		ArrayList<String> siteType = new ArrayList<>(Arrays.asList("Single BYON Internet Single CPE",
				"Dual BYON Internet Single CPE", "Dual BYON Internet Dual CPE"));
		ArrayList<String> portMode = new ArrayList<>(Arrays.asList(LocationUploadConstants.HEADER_PORTMODEACTIVE,
				LocationUploadConstants.HEADER_PORTMODEPASSIVE));
		ArrayList<String> acessType = new ArrayList<>(Arrays.asList(LocationUploadConstants.HEADER_ACESSTYPEWIRELINE,
				LocationUploadConstants.HEADER_ACCESSTYPEWIRELESS));
		ArrayList<String> bandwidth = new ArrayList<>();
		ArrayList<String> dropdownvalues = new ArrayList<>();
		// Getting interface details through queue call
		List<String> interfaceTypeDet = new ArrayList<>();
		String queueCallResponse = (String) mqUtils.sendAndReceive(interfaceType, "");
		interfaceTypeDet = Utils.convertJsonToObject(queueCallResponse, List.class);
		//upload sheet
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
		XSSFSheet ByonUpload = workbook.createSheet(LocationUploadConstants.BYON_UPLOAD);
		Row header = ByonUpload.createRow(0);
		/**** header ***********/
		header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
		header.createCell(1).setCellValue(LocationUploadConstants.HEADER_COUNTRY);
		header.createCell(2).setCellValue(LocationUploadConstants.HEADER_STATE);
		header.createCell(3).setCellValue(LocationUploadConstants.HEADER_CITY);
		header.createCell(4).setCellValue(LocationUploadConstants.HEADER_PINCODE);
		header.createCell(5).setCellValue(LocationUploadConstants.HEADER_LOCALITY);
		header.createCell(6).setCellValue(LocationUploadConstants.HEADER_ADDRESS);
		header.createCell(7).setCellValue(LocationUploadConstants.HEADER_INTERNETQUALITY);
		header.createCell(8).setCellValue(LocationUploadConstants.HEADER_SITETYPE);
		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				headerName = LocationUploadConstants.HEADER_PRIMARY;
				index = 9;
			} else {
				headerName = LocationUploadConstants.HEADER_SECONDARY;
				index = 14;
			}
			header.createCell(index++).setCellValue(headerName + " " + LocationUploadConstants.HEADER_PORTBANDWIDTH);
			header.createCell(index++)
					.setCellValue(headerName + " " + LocationUploadConstants.HEADER_LOCALLOOPBANDWDTH);
			header.createCell(index++).setCellValue(headerName + " " + LocationUploadConstants.HEADER_PORTMODE);
			header.createCell(index++).setCellValue(headerName + " " + LocationUploadConstants.HEADER_ACCESSTYPE);
			header.createCell(index).setCellValue(headerName + " " + LocationUploadConstants.HEADER_INTERFACETYPE);
		}
		/***** header ends ******/
		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			if (i == 5 || i == 6 || i == 7 || i == 8) {
				ByonUpload.setColumnWidth(i, 35 * 256);
			}
			if (i != 0 && i < 5) {
				ByonUpload.setColumnWidth(i, 25 * 256);
			}
			if (i > 8) {
				if (i == 10 || i == 15) {
					ByonUpload.setColumnWidth(i, 21 * 256);
				} else {
					ByonUpload.setColumnWidth(i, 15 * 256);
				}
			}
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderLeft(BorderStyle.THICK);
			stylerowHeading.setBorderRight(BorderStyle.THICK);
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);
			stylerowHeading.setWrapText(true);
			header.getCell(i).setCellStyle(stylerowHeading);
			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
			textStyle.setWrapText(true);
			ByonUpload.setDefaultColumnStyle(i, textStyle);
		}
		/********* country sheet*********/
		XSSFSheet countrySheet = workbook.createSheet("countrySheet");
		for (int i = 0, length = country.size(); i < length; i++) {
			String name = country.get(i);
			XSSFRow row = countrySheet.createRow(i);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue(name);
			if (i < bandwidth.size()) {
				String name1 = bandwidth.get(i);
				XSSFCell cell1 = row.createCell(4);
				cell1.setCellValue(name1);
			}
		}
		Name namedCell = workbook.createName();
		namedCell.setNameName("countrySheet");
		namedCell.setRefersToFormula("countrySheet!$A$1:$A$" + country.size());
		/*** country validation starts *********/
		DataValidationHelper countryValidationHelper = null;
		DataValidation countryDataValidation = null;
		countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) ByonUpload);
		DataValidationConstraint constraint = countryValidationHelper.createFormulaListConstraint("countrySheet");
		CellRangeAddressList addressList = new CellRangeAddressList(1, 200, 1, 1);
		countryDataValidation = countryValidationHelper.createValidation(constraint, addressList);
		workbook.setSheetHidden(1, true);
		countryDataValidation.setSuppressDropDownArrow(true);
		countryDataValidation.setShowErrorBox(true);
		countryDataValidation.setShowPromptBox(true);
		ByonUpload.addValidationData(countryDataValidation);
		/******* country validation ends **********/
		ByonUpload.addValidationData(izoSdwandataValidation(internetQuality, "", ByonUpload, 7, 7));
		ByonUpload.addValidationData(izoSdwandataValidation(siteType, "", ByonUpload, 8, 8));
		// bandwidth sheet
		bandwidth = new ArrayList<>(Arrays.asList("2 Mbps", "3 Mbps", "4 Mbps", "5 Mbps", "6 Mbps", "8 Mbps", "10 Mbps",
				"15 Mbps", "20 Mbps", "25 Mbps", "30 Mbps", "35 Mbps", "40 Mbps", "45 Mbps", "50 Mbps", "60 Mbps",
				"70 Mbps", "100 Mbps", "200 Mbps", "250 Mbps", "300 Mbps", "400 Mbps", "500 Mbps", "600 Mbps",
				"620 Mbps", "750 Mbps", "1000 Mbps", "2000 Mbps", "3000 Mbps", "4000 Mbps", "5000 Mbps", "6000 Mbps",
				"7000 Mbps", "8000 Mbps", "9000 Mbps", "10000 Mbps"));
		XSSFSheet BandwidthSheet = workbook.createSheet("BandwidthSheet");
		for (int i = 0, length = bandwidth.size(); i < length; i++) {
			String name = bandwidth.get(i);
			XSSFRow row = BandwidthSheet.createRow(i);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue(name);
		}
		Name namedCell1 = workbook.createName();
		namedCell1.setNameName("BandwidthSheet");
		namedCell1.setRefersToFormula("BandwidthSheet!$A$1:$A$" + bandwidth.size());
		workbook.setSheetHidden(2, true);
		/**** Primary and secondary attributes drop down ******/
		for (int i = 0, j = 9; i < 2; i++, j++) {
			DataValidationHelper dataValidationHelper = null;
			DataValidation dataValidation = null;
			dataValidationHelper = new XSSFDataValidationHelper((XSSFSheet) ByonUpload);
			if (j == 9 || j == 14) {
				ByonUpload
						.addValidationData(izoSdwandataValidation(dropdownvalues, "BandwidthSheet", ByonUpload, j, j));
				j++;
			}
			if (j == 10 || j == 15) {
				ByonUpload
						.addValidationData(izoSdwandataValidation(dropdownvalues, "BandwidthSheet", ByonUpload, j, j));
				j++;
			}
			if (j == 11 || j == 16) {
				ByonUpload.addValidationData(izoSdwandataValidation(portMode, "", ByonUpload, j, j));
				j++;
			}
			if (j == 12 || j == 17) {
				ByonUpload.addValidationData(izoSdwandataValidation(acessType, "", ByonUpload, j, j));
				j++;
			}
			if (j == 13 || j == 18) {
				ByonUpload.addValidationData(
						izoSdwandataValidation((ArrayList<String>) interfaceTypeDet, "", ByonUpload, j, j));
			}
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "byon-template.xlsx";
		response.reset();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {

		}
		outByteStream.flush();
		outByteStream.close();
		}
		catch (MalformedURLException e) {
			LOGGER.warn("Error in processing downloadLocationTemplate malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.MALFORMED_URL_EXCEPTION, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadLocationTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
	}

	/********* download excel izosdwan ends **********/
		
		
		/**
	 * downloadLocationTemplate - takes the country list and profile names as input
	 * and generates the dynamic excel template with the default dropdown list
	 * values for country and profiles.
	 * 
	 * @param locationTemplate
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public void downloadLocationTemplateIas(LocationTemplateRequest locationTemplate, HttpServletResponse response)
			throws IOException, TclCommonException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
			header.createCell(1).setCellValue(LocationUploadConstants.HEADER_COUNTRY);
			header.createCell(2).setCellValue(LocationUploadConstants.HEADER_STATE);
			header.createCell(3).setCellValue(LocationUploadConstants.HEADER_CITY);
			header.createCell(4).setCellValue(LocationUploadConstants.HEADER_PINCODE);
			header.createCell(5).setCellValue(LocationUploadConstants.LOCALITY);
			header.createCell(6).setCellValue(LocationUploadConstants.HEADER_ADDRESS);
			header.createCell(7).setCellValue(LocationUploadConstants.HEADER_PROFILES);

			for (int i = 0; i < 8; i++) {
				CellStyle stylerowHeading = workbook.createCellStyle();
				stylerowHeading.setBorderBottom(BorderStyle.THICK);
				stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
				stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setFontHeightInPoints((short) 10);
				font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
				stylerowHeading.setFont(font);
				stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
				stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

				stylerowHeading.setWrapText(true);
				header.getCell(i).setCellStyle(stylerowHeading);

				DataFormat fmt = workbook.createDataFormat();
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
				sheet.setDefaultColumnStyle(i, textStyle);
			}

			/* country validation begins */
			DataValidation countryDataValidation = null;
			DataValidationConstraint countryConstraint = null;
			DataValidationHelper countryValidationHelper = null;

			countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			CellRangeAddressList countryAddressList = new CellRangeAddressList(1, 50, 1, 1);
			// populate with the list of countries from product catalog
			countryConstraint = countryValidationHelper
					.createExplicitListConstraint(locationTemplate.getCountries().stream().toArray(String[]::new));
			countryDataValidation = countryValidationHelper.createValidation(countryConstraint, countryAddressList);
			countryDataValidation.setSuppressDropDownArrow(true);
			countryDataValidation.setShowErrorBox(true);
			countryDataValidation.setShowPromptBox(true);
			sheet.addValidationData(countryDataValidation);
			sheet.setColumnWidth(0, 1800);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 7500);
			sheet.setColumnWidth(5, 7500);
			sheet.setColumnWidth(6, 10000);
			sheet.setColumnWidth(7, 8000);

			/* profile validation begins */
			DataValidation profileDataValidation = null;
			DataValidationConstraint profileConstraint = null;
			DataValidationHelper profileValidationHelper = null;
			profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			CellRangeAddressList profileAddressList = new CellRangeAddressList(1, 50, 7, 7);
			// add the selected profiles
			profileConstraint = profileValidationHelper
					.createExplicitListConstraint(locationTemplate.getProfiles().stream().toArray(String[]::new));
			profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
			profileDataValidation.setSuppressDropDownArrow(true);
			profileDataValidation.setShowErrorBox(true);
			profileDataValidation.setShowPromptBox(true);
			sheet.addValidationData(profileDataValidation);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = "location-template.xlsx";
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			try {
				FileCopyUtils.copy(outArray, response.getOutputStream());
			} catch (Exception e) {

			}

			outByteStream.flush();
			outByteStream.close();

		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing downloadLocationTemplate malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.MALFORMED_URL_EXCEPTION, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadLocationTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
	}

	
	/**
	 * downloadLocationTemplate - takes the country list and profile names as input
	 * and generates the dynamic excel template with the default dropdown list
	 * values for country and profiles.
	 * 
	 * @param locationTemplate
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public void downloadLocationTemplateIwan(LocationTemplateRequest locationTemplate, HttpServletResponse response)
			throws IOException, TclCommonException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
			header.createCell(1).setCellValue(LocationUploadConstants.HEADER_COUNTRY);
			header.createCell(2).setCellValue(LocationUploadConstants.HEADER_STATE);
			header.createCell(3).setCellValue(LocationUploadConstants.HEADER_CITY);
			header.createCell(4).setCellValue(LocationUploadConstants.HEADER_PINCODE);
			header.createCell(5).setCellValue(LocationUploadConstants.LOCALITY);
			header.createCell(6).setCellValue(LocationUploadConstants.HEADER_ADDRESS);
			header.createCell(7).setCellValue(LocationUploadConstants.HEADER_PROFILES);

			for (int i = 0; i < 8; i++) {
				CellStyle stylerowHeading = workbook.createCellStyle();
				stylerowHeading.setBorderBottom(BorderStyle.THICK);
				stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
				stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setFontHeightInPoints((short) 10);
				font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
				stylerowHeading.setFont(font);
				stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
				stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

				stylerowHeading.setWrapText(true);
				header.getCell(i).setCellStyle(stylerowHeading);

				DataFormat fmt = workbook.createDataFormat();
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
				sheet.setDefaultColumnStyle(i, textStyle);
			}

			/* country validation begins */
			DataValidation countryDataValidation = null;
			DataValidationConstraint countryConstraint = null;
			DataValidationHelper countryValidationHelper = null;

			countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			CellRangeAddressList countryAddressList = new CellRangeAddressList(1, 50, 1, 1);
			// populate with the list of countries from product catalog
			countryConstraint = countryValidationHelper
					.createExplicitListConstraint(locationTemplate.getCountries().stream().toArray(String[]::new));
			countryDataValidation = countryValidationHelper.createValidation(countryConstraint, countryAddressList);
			countryDataValidation.setSuppressDropDownArrow(true);
			countryDataValidation.setShowErrorBox(true);
			countryDataValidation.setShowPromptBox(true);
			sheet.addValidationData(countryDataValidation);
			sheet.setColumnWidth(0, 1800);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 7500);
			sheet.setColumnWidth(5, 7500);
			sheet.setColumnWidth(6, 10000);
			sheet.setColumnWidth(7, 8000);

			/* profile validation begins */
			DataValidation profileDataValidation = null;
			DataValidationConstraint profileConstraint = null;
			DataValidationHelper profileValidationHelper = null;
			profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			CellRangeAddressList profileAddressList = new CellRangeAddressList(1, 50, 7, 7);
			// add the selected profiles
			profileConstraint = profileValidationHelper
					.createExplicitListConstraint(locationTemplate.getProfiles().stream().toArray(String[]::new));
			profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
			profileDataValidation.setSuppressDropDownArrow(true);
			profileDataValidation.setShowErrorBox(true);
			profileDataValidation.setShowPromptBox(true);
			sheet.addValidationData(profileDataValidation);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = "location-template.xlsx";
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			try {
				FileCopyUtils.copy(outArray, response.getOutputStream());
			} catch (Exception e) {

			}

			outByteStream.flush();
			outByteStream.close();

		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing downloadLocationTemplate malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.MALFORMED_URL_EXCEPTION, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadLocationTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
	}
	
	/**
	 * validateUploadedDetails - this method reads the excel value and pass for data
	 * validations.
	 * 
	 * @param fullFilePath
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public Set<LocationUploadValidationBean> validateUploadedDetails(MultipartFile file,
			List<LocationOfferingDetail> locationDetailList)
			throws InvalidFormatException, IOException, TclCommonException {
		Set<LocationUploadValidationBean> validatorBean = Collections.synchronizedSet(new HashSet<>());
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");

			for (Sheet sheet : workbook) {
				LOGGER.info("last row num => {}", sheet.getLastRowNum());
				LOGGER.info("last row num with data => {} ", getLastRowWithData(sheet));
				for (int i = 0; i <= getLastRowWithData(sheet); i++) {

					Cell firstCell = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
					Cell countryCell = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
					Cell stateCell = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
					Cell cityCell = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
					Cell pinZipCell = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
					Cell addressCell = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
					Cell profileCell = sheet.getRow(i).getCell(6, xRow.RETURN_BLANK_AS_NULL);

					List<CompletableFuture<Boolean>> futureList = new ArrayList<>();

					if (firstCell != null && countryCell != null && stateCell != null && cityCell != null
							&& pinZipCell != null && addressCell != null && profileCell != null) {
						futureList.add(locationAsyncService.validateEachRow(workbook, sheet.getRow(i), validatorBean,
								locationDetailList));
					} else {
						if (!isRowBlank(sheet.getRow(i))) {

							List<LocationValidationColumnBean> columnValidationBeanList = new ArrayList<>();
							LocationUploadValidationBean eachRowFullValidation = new LocationUploadValidationBean();
							eachRowFullValidation
									.setRowDetails(LocationUploadConstants.ROW + sheet.getRow(i).getRowNum());

							if (isCellBlank(firstCell) || isCellEmpty(firstCell) || null == firstCell) {
								LocationValidationColumnBean columnBeanCountry = new LocationValidationColumnBean();
								columnBeanCountry.setColumnName(LocationUploadConstants.HEADER_SERIAL_NO);
								columnBeanCountry.setErrorMessage(LocationUploadConstants.SR_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanCountry);
							}

							if (isCellBlank(countryCell) || isCellEmpty(countryCell) || null == countryCell) {
								LocationValidationColumnBean columnBeanCountry = new LocationValidationColumnBean();
								columnBeanCountry.setColumnName(LocationUploadConstants.HEADER_COUNTRY);
								columnBeanCountry.setErrorMessage(LocationUploadConstants.COUNTRY_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanCountry);
							}

							if (isCellBlank(stateCell) || isCellEmpty(stateCell) || null == stateCell) {
								LocationValidationColumnBean columnBeanState = new LocationValidationColumnBean();
								columnBeanState.setColumnName(LocationUploadConstants.HEADER_STATE);
								columnBeanState.setErrorMessage(LocationUploadConstants.STATE_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanState);
							}

							if (isCellBlank(cityCell) || isCellEmpty(cityCell) || null == cityCell) {
								LocationValidationColumnBean columnBeanCity = new LocationValidationColumnBean();
								columnBeanCity.setColumnName(LocationUploadConstants.HEADER_CITY);
								columnBeanCity.setErrorMessage(LocationUploadConstants.CITY_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanCity);
							}

							if (isCellBlank(pinZipCell) || isCellEmpty(pinZipCell) || null == pinZipCell) {
								LocationValidationColumnBean columnBeanPincode = new LocationValidationColumnBean();
								columnBeanPincode.setColumnName(LocationUploadConstants.HEADER_PINCODE);
								columnBeanPincode.setErrorMessage(LocationUploadConstants.PINCODE_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanPincode);
							}

							if (isCellBlank(addressCell) || isCellEmpty(addressCell) || null == addressCell) {
								LocationValidationColumnBean columnBeanAddr = new LocationValidationColumnBean();
								columnBeanAddr.setColumnName(LocationUploadConstants.HEADER_ADDRESS);
								columnBeanAddr.setErrorMessage(LocationUploadConstants.ADDRESS_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanAddr);
							}

							if (isCellBlank(profileCell) || isCellEmpty(profileCell) || null == profileCell) {
								LocationValidationColumnBean columnBeanProfile = new LocationValidationColumnBean();
								columnBeanProfile.setColumnName(LocationUploadConstants.HEADER_PROFILES);
								columnBeanProfile.setErrorMessage(LocationUploadConstants.PROFILE_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanProfile);
							}
							eachRowFullValidation.setColumns(columnValidationBeanList);
							validatorBean.add(eachRowFullValidation);
						} else {
							LOGGER.info("Full row is blank/Invalid input and the row number is {}", sheet.getRow(i));
						}
					}
					CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();

				}
			}
			workbook.close();
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return validatorBean;
	}

	/**
	 * validateUploadedDetails - this method reads the excel value and pass for data
	 * validations.
	 * 
	 * @param fullFilePath
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public Set<LocationUploadValidationBean> validateUploadedDetailsIas(MultipartFile file,
			List<LocationOfferingDetail> locationDetailList)
			throws InvalidFormatException, IOException, TclCommonException {
		Set<LocationUploadValidationBean> validatorBean = Collections.synchronizedSet(new HashSet<>());
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");

			for (Sheet sheet : workbook) {
				LOGGER.info("last row num => {}", sheet.getLastRowNum());
				LOGGER.info("last row num with data => {} ", getLastRowWithData(sheet));
				for (int i = 1; i <= getLastRowWithData(sheet); i++) {

					Cell firstCell = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
					Cell countryCell = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
					Cell stateCell = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
					Cell cityCell = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
					Cell pinZipCell = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
					Cell localityCell = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);

					Cell addressCell = sheet.getRow(i).getCell(6, xRow.RETURN_BLANK_AS_NULL);
					Cell profileCell = sheet.getRow(i).getCell(7, xRow.RETURN_BLANK_AS_NULL);

					List<CompletableFuture<Boolean>> futureList = new ArrayList<>();

					if (firstCell != null && countryCell != null && stateCell != null && cityCell != null
							&& pinZipCell != null && addressCell != null && profileCell != null
							&& localityCell != null) {
						futureList.add(locationAsyncService.validateEachRowForIas(workbook, sheet.getRow(i),
								validatorBean, locationDetailList));
					} else {
						if (!isRowBlank(sheet.getRow(i))) {

							List<LocationValidationColumnBean> columnValidationBeanList = new ArrayList<>();
							LocationUploadValidationBean eachRowFullValidation = new LocationUploadValidationBean();
							eachRowFullValidation
									.setRowDetails(LocationUploadConstants.ROW + sheet.getRow(i).getRowNum());

							if (isCellBlank(firstCell) || isCellEmpty(firstCell) || null == firstCell) {
								LocationValidationColumnBean columnBeanCountry = new LocationValidationColumnBean();
								columnBeanCountry.setColumnName(LocationUploadConstants.HEADER_SERIAL_NO);
								columnBeanCountry.setErrorMessage(LocationUploadConstants.SR_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanCountry);
							}

							if (isCellBlank(countryCell) || isCellEmpty(countryCell) || null == countryCell) {
								LocationValidationColumnBean columnBeanCountry = new LocationValidationColumnBean();
								columnBeanCountry.setColumnName(LocationUploadConstants.HEADER_COUNTRY);
								columnBeanCountry.setErrorMessage(LocationUploadConstants.COUNTRY_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanCountry);
							}

							if (isCellBlank(stateCell) || isCellEmpty(stateCell) || null == stateCell) {
								LocationValidationColumnBean columnBeanState = new LocationValidationColumnBean();
								columnBeanState.setColumnName(LocationUploadConstants.HEADER_STATE);
								columnBeanState.setErrorMessage(LocationUploadConstants.STATE_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanState);
							}

							if (isCellBlank(cityCell) || isCellEmpty(cityCell) || null == cityCell) {
								LocationValidationColumnBean columnBeanCity = new LocationValidationColumnBean();
								columnBeanCity.setColumnName(LocationUploadConstants.HEADER_CITY);
								columnBeanCity.setErrorMessage(LocationUploadConstants.CITY_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanCity);
							}

							if (isCellBlank(pinZipCell) || isCellEmpty(pinZipCell) || null == pinZipCell) {
								LocationValidationColumnBean columnBeanPincode = new LocationValidationColumnBean();
								columnBeanPincode.setColumnName(LocationUploadConstants.HEADER_PINCODE);
								columnBeanPincode.setErrorMessage(LocationUploadConstants.PINCODE_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanPincode);
							}

							if (isCellBlank(localityCell) || isCellEmpty(localityCell) || null == localityCell) {
								LocationValidationColumnBean columnBeanPincode = new LocationValidationColumnBean();
								columnBeanPincode.setColumnName(LocationUploadConstants.HEADER_LOCALITY);
								columnBeanPincode.setErrorMessage(LocationUploadConstants.LOCALITY_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanPincode);
							}

							if (isCellBlank(addressCell) || isCellEmpty(addressCell) || null == addressCell) {
								LocationValidationColumnBean columnBeanAddr = new LocationValidationColumnBean();
								columnBeanAddr.setColumnName(LocationUploadConstants.HEADER_ADDRESS);
								columnBeanAddr.setErrorMessage(LocationUploadConstants.ADDRESS_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanAddr);
							}

							if (isCellBlank(profileCell) || isCellEmpty(profileCell) || null == profileCell) {
								LocationValidationColumnBean columnBeanProfile = new LocationValidationColumnBean();
								columnBeanProfile.setColumnName(LocationUploadConstants.HEADER_PROFILES);
								columnBeanProfile.setErrorMessage(LocationUploadConstants.PROFILE_CANT_BE_EMPTY);
								columnValidationBeanList.add(columnBeanProfile);
							}
							eachRowFullValidation.setColumns(columnValidationBeanList);
							validatorBean.add(eachRowFullValidation);
						} else {
							LOGGER.info("Full row is blank/Invalid input and the row number is {}", sheet.getRow(i));
						}
					}
					CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
				}
			}
			workbook.close();
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return validatorBean;
	}

	/**
	 * addAddresses - This method will be used to add the list of locations and
	 * return the locationIds with offering names
	 * 
	 * @param locationDetailList
	 * @return
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public List<LocationMultipleResponse> addAddressesOffering(List<LocationOfferingDetail> locationDetailList)
			throws TclCommonException {
		
		List<LocationMultipleResponse> locationResponseList = new ArrayList<>();
		try {
			if (locationDetailList == null || locationDetailList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);

			List<CompletableFuture<LocationMultipleResponse>> futureList = new ArrayList<>();

			for (LocationOfferingDetail locDetail : locationDetailList) {
				futureList.add(locationAsyncService.addAddress(locDetail));

			}
			CompletableFuture<Void> allFuture = CompletableFuture
					.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));

			CompletableFuture<List<LocationMultipleResponse>> finalFutureList = allFuture.thenApply(v -> futureList
					.stream().map(future -> future.join()).collect(Collectors.<LocationMultipleResponse>toList()));

			locationResponseList = finalFutureList.get();

			if (locationResponseList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		return locationResponseList;
	}

	public int getLastRowWithData(Sheet currentSheet) {
		int rowCount = 0;
		Iterator<Row> iter = currentSheet.rowIterator();

		while (iter.hasNext()) {
			Row r = iter.next();
			if (!this.isRowBlank(r)) {
				rowCount = r.getRowNum();
			}
		}

		return rowCount;
	}

	public boolean isRowBlank(Row r) {
		boolean ret = true;

		/*
		 * If a row is null, it must be blank.
		 */
		if (r != null) {
			Iterator<Cell> cellIter = r.cellIterator();
			/*
			 * Iterate through all cells in a row.
			 */
			while (cellIter.hasNext()) {
				/*
				 * If one of the cells in given row contains data, the row is considered not
				 * blank.
				 */
				if (!this.isCellBlank(cellIter.next())) {
					ret = false;
					break;
				}
			}
		}

		return ret;
	}

	public boolean isCellBlank(Cell c) {
		return (c == null || c.getCellTypeEnum() == CellType.BLANK);
	}

	public boolean isCellEmpty(Cell c) {
		return c == null || c.getCellTypeEnum() == CellType.BLANK
				|| (c.getCellTypeEnum() == CellType.STRING && c.getStringCellValue().isEmpty());
	}

	/**
	 * Get Cities By Given Country Name
	 *
	 * @param country
	 * @return {@link List<MstCityBean>}
	 * @throws TclCommonException
	 */
	@Transactional
	public List<MstCityBean> getCitiesByCountry(final String country) throws TclCommonException {
		List<MstCityBean> mstCityBeans;
		try {
			List<MstCity> cities = mstCityRepository.findCityByCountry(country);
			if (cities.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.GSC_CITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			mstCityBeans = cities.stream().sorted(Comparator.comparing(MstCity::getName))
					.map(com.tcl.dias.location.beans.MstCityBean::fromMstCity)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return mstCityBeans;
	}

	@Transactional
	public List<MstCityBean> getCitiesByState(final String state) throws TclCommonException {
		List<MstCityBean> mstCityBeans;
		try {
			List<MstCity> cities = mstCityRepository.findByMstState_Name(state);
			if (cities.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.GSC_CITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			mstCityBeans = cities.stream().map(com.tcl.dias.location.beans.MstCityBean::fromMstCity)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return mstCityBeans;
	}

	/**
	 * Get master countries
	 * 
	 * @return {@link List<MstCountryBean>}
	 * @throws TclCommonException
	 */
	public List<MstCountryBean> getCountries() throws TclCommonException {
		List<MstCountryBean> mstCountryBeans = new ArrayList<>();
		try {

			List<MstCountry> mstCountriesList = mstCountryRepository.findAll();

			if (Objects.isNull(mstCountriesList) || mstCountriesList.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}

			mstCountryBeans = mstCountriesList.stream().map(com.tcl.dias.location.beans.MstCountryBean::fromMstCountry)
					.collect(Collectors.toList());

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return mstCountryBeans;
	}

	/**
	 * Get countries by its code
	 *
	 * @param countryCodes
	 * @return {@link List<MstCountryBean>}
	 * @throws TclCommonException
	 */
	public List<MstCountryBean> getCountriesByCode(List<String> countryCodes) throws TclCommonException {
		if (Objects.isNull(countryCodes) || countryCodes.isEmpty())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		List<MstCountry> countries = mstCountryRepository.findByCodeIn(countryCodes);
		if (Objects.isNull(countries) || countries.isEmpty())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		return countries.stream().map(com.tcl.dias.location.beans.MstCountryBean::fromMstCountry)
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * getLocalItContact
	 * 
	 * @param localItContactId
	 * @return
	 */
	public Map<String, Object> getLocalItContact(Integer localItContactId) {
		Optional<CustomerSiteLocationItContact> localItContact = customerLocationItInfoRepository
				.findById(localItContactId);
		Map<String, Object> localItMapper = new HashMap<String, Object>();
		if (localItContact.isPresent()) {
			localItMapper.put("CONTACT_NO", localItContact.get().getContactNumber());
			localItMapper.put("EMAIL", localItContact.get().getEmailId());
			localItMapper.put("NAME", localItContact.get().getName());
		} else {
			LOGGER.info("No localIt detail present for the given localIt Contact id {}", localItContactId);
		}
		return localItMapper;
	}

	/**
	 * 
	 * getDemarcationDetails
	 * 
	 * @param demarcationId
	 * @return
	 */
	public Map<String, Object> getDemarcationDetails(Integer locationId) {
		Demarcation demarcation = demarcationRepository.findByLocationId(locationId);
		Map<String, Object> demarcationMapper = new HashMap<String, Object>();
		if (demarcation != null) {
			demarcationMapper.put("APPARTMENT", demarcation.getAppartment());
			demarcationMapper.put("BUILDING_ALTITUDE", demarcation.getBuildingAltitude());
			demarcationMapper.put("BUILDING_NAME", demarcation.getBuildingName());
			demarcationMapper.put("FLOOR", demarcation.getFloor());
			demarcationMapper.put("ROOM", demarcation.getRoom());
			demarcationMapper.put("TOWER", demarcation.getTower());
			demarcationMapper.put("WING", demarcation.getWing());
			demarcationMapper.put("ZONE", demarcation.getZone());

		} else {
			LOGGER.info("No demarcation detail present for the given location id {}", locationId);
		}
		return demarcationMapper;
	}

	/**
	 * saves and update address
	 * 
	 * @author VISHESH AWASTHI
	 * @param locationDetails
	 * @return
	 * @throws TclCommonException
	 * 
	 */
	public List<Integer> saveLocationEmergencyAddress(LocationDetails locationDetails) throws TclCommonException {
		Objects.requireNonNull(locationDetails);
		List<MstAddress> mstAddressList = new ArrayList<>();
		locationDetails.getAddress().stream().forEach(location -> {
			try {
				if (Objects.isNull(location.getAddressId())) {
					mstAddressList.add(saveLocationDetails(location));
				} else {
					mstAddressList.add(updateLocationDetails(location));
				}
			} catch (Exception e) {
				LOGGER.warn("Error in processing saveLocationEmergencyAddress {}", ExceptionUtils.getStackTrace(e));
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		});
		return mstAddressList.stream().map(MstAddress::getId).collect(Collectors.toList());
	}

	/**
	 * saves new address
	 * 
	 * @param location
	 * @return
	 */
	private MstAddress saveLocationDetails(AddressDetail location) {
		MstAddress address = new MstAddress();
		address.setAddressLineOne(Utils.removeUnicode(location.getAddressLineOne()));
		address.setAddressLineTwo(Utils.removeUnicode(location.getAddressLineTwo()));
		address.setPincode(location.getPincode());
		address.setCity(location.getCity());
		address.setLocality(location.getLocality());
		address.setState(location.getState());
		address.setCountry(location.getCountry());
		address.setSource(LocationConstants.API_SOURCE.toString());
		address.setCreatedTime(new Timestamp(new Date().getTime()));
		return mstAddressRepository.save(address);
	}

	/**
	 * updates Existing address
	 * 
	 * @param location
	 * @return
	 * @throws TclCommonException
	 */
	private MstAddress updateLocationDetails(AddressDetail location) throws TclCommonException {
		Optional<MstAddress> mstAddress = mstAddressRepository.findById(location.getAddressId());
		if (!mstAddress.isPresent()) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		MstAddress address = mstAddress.get();
		address.setAddressLineOne(Utils.removeUnicode(location.getAddressLineOne()));
		address.setAddressLineTwo(Utils.removeUnicode(location.getAddressLineTwo()));
		address.setPincode(location.getPincode());
		address.setCity(location.getCity());
		address.setLocality(location.getLocality());
		address.setCountry(location.getCountry());
		address.setState(location.getState());
		address.setSource(LocationConstants.API_SOURCE.toString());
		address.setCreatedTime(new Timestamp(new Date().getTime()));
		return mstAddressRepository.save(address);
	}

	/**
	 * list address details
	 * 
	 * @param addressIds
	 * @return
	 * @throws TclCommonException
	 */
	public List<AddressDetail> getEmergencyAddress(List<Integer> addressIds) throws TclCommonException {
		Objects.requireNonNull(addressIds);
		Optional<List<MstAddress>> mstAddressList = Optional.ofNullable(mstAddressRepository.findByIdIn(addressIds));
		if (!mstAddressList.isPresent()) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return getAddressByMasterAddressCodes(mstAddressList.get());
	}

	/**
	 * This method is to get data centre's location id from db using dc code as pop
	 * location id.
	 * 
	 * @param dcCode
	 * @return location id
	 * @throws TclCommonException
	 */
	public Integer getLocationIdForDcCode(String dcCode) throws TclCommonException {
		if (StringUtils.isEmpty(dcCode))
			throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR);
		Integer locationId = null;
		Location location = locationRepository.findByPopLocationId(dcCode);
		if (location != null)
			locationId = location.getId();

		return locationId;
	}

	/**
	 * This method is to get data centre's code for location id
	 * 
	 * @param dcCode
	 * @return location id
	 * @throws TclCommonException
	 */
	public List<SolutionBean> getLocationCodeForId(List<SolutionBean> solutions) throws TclCommonException {
		if (Objects.isNull(solutions))
			throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR);

		solutions.forEach(sol -> {
			sol.getSites().forEach(site -> {
				Optional<Location> optLocation = locationRepository.findById(site.getLocationId());
				if (optLocation.isPresent()) {
					site.setDataCenterCode(optLocation.get().getPopLocationId());
					site.setLatLong(optLocation.get().getLatLong());
				}
			});
		});

		return solutions;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<List<AddressDetail>> loadLocationsDetailsWithPagination(Integer customerId, String city,
			String country, String searchBy, Integer page, Integer size, String state,Boolean pop,String type,Boolean connectedSite) throws TclCommonException {
		List<AddressDetail> addressDetailsList = new ArrayList<>();
		try {
			if(pop==null) {
				pop=false;
			}
			if(type==null) {
				type="";
			}
			if(connectedSite==null) {
				connectedSite=false;
			}
			Set<Integer> leIds = new HashSet<>();
			if ((userInfoUtils.getUserType().equalsIgnoreCase(INTERNAL_USERS.toString())|| PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) && customerId != null) {
				LOGGER.info("Calling customer consumer for customer Id {}", customerId);
				LOGGER.info("MDC Filter token value in before Queue call loadLocationsDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				leIds = (Set<Integer>) Utils.convertJsonToObject(
						(String) mqUtils.sendAndReceive(customerLeToCustomerQueue, String.valueOf(customerId)),
						Set.class);
				LOGGER.info("customerLes {} received for customer Id {}", leIds, customerId);

			} else {
				List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
				if (customerDetails != null) {
					for (CustomerDetail customerDetail : customerDetails) {
						leIds.add(customerDetail.getCustomerLeId());
					}
				}
			}
			if (leIds != null && !leIds.isEmpty()) {
				if(pop && type.equals("B")) {
					List<Location> customerLeLocList = locationLeCustomerDao.getPopLocationsBasedOnSearch(
							constructQueryForPopAndReturn(leIds, page, size, city, country, searchBy, true, state, pop,type,connectedSite));
					LOGGER.info("Customer Le to location list : {} ", customerLeLocList);
					if (customerLeLocList != null && !customerLeLocList.isEmpty()) {
						addressDetailsList = constructAddressDetailFromLocation(customerLeLocList);
					}
					LOGGER.info("address detail list : {} ", addressDetailsList);
					if (!addressDetailsList.isEmpty()) {

						addressDetailsList.sort(Comparator.comparing(AddressDetail::getCity));
						List<Location> locationAllCount = locationLeCustomerDao.getPopLocationsBasedOnSearch(
								constructQueryForPopAndReturn(leIds, page, size, city, country,
										searchBy, false, state, pop,type,connectedSite));
						int count = locationAllCount.size();
						LOGGER.info("customer le to location list count is : {} ", count);
						int totalPages = 1;
						if (count > size) {
							totalPages = ((int) locationAllCount.size()) / size;
						}
						return new PagedResult(addressDetailsList, locationAllCount.size(), totalPages);
					}
				} else {
					List<LocationLeCustomer> customerLeLocList = locationLeCustomerDao.getAllLocationsBasedOnSearch(
							constructQueryAndReturn(leIds, page, size, city, country, searchBy, true, state, pop,connectedSite));
					LOGGER.info("Customer Le to location list : {} ", customerLeLocList);
					if (customerLeLocList != null && !customerLeLocList.isEmpty()) {
						addressDetailsList = constructAddressDetailFromLocationLeCustomerList(customerLeLocList);
					}
					LOGGER.info("address detail list : {} ", addressDetailsList);
					if (!addressDetailsList.isEmpty()) {

						addressDetailsList.sort(Comparator.comparing(AddressDetail::getCity));
						List<LocationLeCustomer> customerLeLocListForCount = locationLeCustomerDao
								.getAllLocationsBasedOnSearch(constructQueryAndReturn(leIds, page, size, city, country,
										searchBy, false, state, pop,connectedSite));
						int count = customerLeLocListForCount.size();
						LOGGER.info("customer le to location list count is : {} ", count);
						int totalPages = 1;
						if (count > size) {
							totalPages = ((int) customerLeLocListForCount.size()) / size;
						}
						return new PagedResult(addressDetailsList, customerLeLocListForCount.size(), totalPages);
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return null;
	}

	private String constructQueryAndReturn(Set<Integer> customerLeIds, Integer page, Integer size, String city,
			String country, String searchBy, Boolean withPagination, String state,Boolean pop,Boolean connectedSite) {
		Set<String> customerLeSet = new HashSet<>();
		customerLeIds.stream().forEach(customerLeId -> {
			customerLeSet.add(customerLeId.toString());
		});
		String customerLeList = String.join(",", customerLeSet);
		String query = "select llc.* from location_le_customer as llc,location as l,mst_address as a where llc.location_id=l.id and"
					+ " l.address_id=a.id and llc.erf_cus_customer_le_id in (" + customerLeList + ") ";
		if (!StringUtils.isAllEmpty(city)) {
			query = query + "and a.city='" + city + "'";
		}
		if (!StringUtils.isAllEmpty(country)) {
			query = query + "and a.country='" + country + "'";
		}
		if (!StringUtils.isAllEmpty(state)) {
			query = query + "and a.state='" + state + "'";
		}
		if (!StringUtils.isAllEmpty(searchBy)) {
			query = query + "and a.address_line_one like '%" + searchBy + "%'";
		}
		if (pop) {
			query = query + " and l.pop_location_id is not null ";
		}
		if(connectedSite) {
			query = query + " and llc.is_connected_building=1 ";
		}
		query = query + "group by a.id ";
		if (withPagination) {
			query = query + "limit " + (page - 1)*size + "," + size;
		}
		LOGGER.info("Query : {}", query);
		return query;
	}
	
	private String constructQueryForPopAndReturn(Set<Integer> customerLeIds, Integer page, Integer size, String city,
			String country, String searchBy, Boolean withPagination, String state,Boolean pop,String type,Boolean connectedSite) {
		Set<String> customerLeSet = new HashSet<>();
		customerLeIds.stream().forEach(customerLeId -> {
			customerLeSet.add(customerLeId.toString());
		});
		String customerLeList = String.join(",", customerLeSet);
		String query =null;
		if(pop && type.equals("B")) {
			query = "select l.* from location as l,mst_address as a where"
					+ " l.address_id=a.id ";
		}else {
			query = "select llc.* from location_le_customer as llc,location as l,mst_address as a where llc.location_id=l.id and"
					+ " l.address_id=a.id and llc.erf_cus_customer_le_id in (" + customerLeList + ") ";
		}
		if (!StringUtils.isAllEmpty(city)) {
			query = query + "and a.city='" + city + "'";
		}
		if (!StringUtils.isAllEmpty(country)) {
			query = query + "and a.country='" + country + "'";
		}
		if (!StringUtils.isAllEmpty(state)) {
			query = query + "and a.state='" + state + "'";
		}
		if (!StringUtils.isAllEmpty(searchBy)) {
			query = query + "and a.address_line_one like '%" + searchBy + "%'";
		}
		if (pop) {
			query = query + " and l.pop_location_id is not null ";
		}
		if(connectedSite) {
			query = query + " and llc.is_connected_building=1 ";
		}
		query = query + "group by a.id ";
		if (withPagination) {
			query = query + "limit " + (page - 1)*size + "," + size;
		}
		LOGGER.info("Query : {}", query);
		return query;
	}

	private List<AddressDetail> constructAddressDetailFromLocationLeCustomerList(
			List<LocationLeCustomer> locationLeCustomers) {
		List<AddressDetail> addressDetails = new ArrayList<>();
		locationLeCustomers.stream().forEach(loactionLeCustomer -> {

			if (loactionLeCustomer.getLocation() != null && loactionLeCustomer.getLocation().getAddressId() != null) {
				Optional<MstAddress> mstAddress = mstAddressRepository
						.findById(loactionLeCustomer.getLocation().getAddressId());
				if (mstAddress.isPresent()) {
					AddressDetail addressDetail = new AddressDetail();
					addressDetail.setAddressLineOne(Utils.removeUnicode(mstAddress.get().getAddressLineOne()));
					addressDetail.setAddressId(loactionLeCustomer.getLocation().getAddressId());
					addressDetail.setAddressLineTwo(Utils.removeUnicode(mstAddress.get().getAddressLineTwo()));
					addressDetail.setCity(mstAddress.get().getCity());
					addressDetail.setCountry(mstAddress.get().getCountry());
					addressDetail.setState(mstAddress.get().getState());
					addressDetail.setLocality(mstAddress.get().getLocality());
					addressDetail.setPincode(mstAddress.get().getPincode());
					addressDetail.setSource(mstAddress.get().getSource());
					addressDetail.setLatLong(loactionLeCustomer.getLocation().getLatLong());
					addressDetail.setLocationId(loactionLeCustomer.getLocation().getId());
					addressDetails.add(addressDetail);
				}
			}

		});
		return addressDetails;
	}
	
	private List<AddressDetail> constructAddressDetailFromLocation(
			List<Location> locations) {
		List<AddressDetail> addressDetails = new ArrayList<>();
		locations.stream().forEach(location -> {

			if (location != null && location.getAddressId() != null) {
				Optional<MstAddress> mstAddress = mstAddressRepository
						.findById(location.getAddressId());
				if (mstAddress.isPresent()) {
					AddressDetail addressDetail = new AddressDetail();
					addressDetail.setAddressLineOne(Utils.removeUnicode(mstAddress.get().getAddressLineOne()));
					addressDetail.setAddressId(location.getAddressId());
					addressDetail.setAddressLineTwo(Utils.removeUnicode(mstAddress.get().getAddressLineTwo()));
					addressDetail.setCity(mstAddress.get().getCity());
					addressDetail.setCountry(mstAddress.get().getCountry());
					addressDetail.setState(mstAddress.get().getState());
					addressDetail.setLocality(mstAddress.get().getLocality());
					addressDetail.setPincode(mstAddress.get().getPincode());
					addressDetail.setSource(mstAddress.get().getSource());
					addressDetail.setLatLong(location.getLatLong());
					addressDetail.setLocationId(location.getId());
					addressDetails.add(addressDetail);
				}
			}

		});
		return addressDetails;
	}

	public List<String> getDistinctCitiesForLoadMyLocation(Integer customerId, String country)
			throws TclCommonException {
		List<String> cities = new ArrayList<>();
		try {
			Set<Integer> leIds = new HashSet<>();
			if (Objects.nonNull(customerId)) {
				if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
						|| PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) {
					LOGGER.info("Calling customer consumer for customer Id {}", customerId);
					LOGGER.info("MDC Filter token value in before Queue call loadLocationsDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					leIds = (Set<Integer>) Utils.convertJsonToObject(
							(String) mqUtils.sendAndReceive(customerLeToCustomerQueue, String.valueOf(customerId)),
							Set.class);
					LOGGER.info("customerLes {} received for customer Id {}", leIds, customerId);
				}
			} else {
				List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
				if (customerDetails != null) {
					for (CustomerDetail customerDetail : customerDetails) {
						leIds.add(customerDetail.getCustomerLeId());
					}
				}
			}
			if (leIds != null && !leIds.isEmpty()) {
				List<LocationLeCustomer> customerLeLocList = locationLeCustomerDao.getAllLocationsBasedOnSearch(
						constructQueryAndReturn(leIds, null, null, null, country, null, false, null,false,false));
				if (customerLeLocList != null && !customerLeLocList.isEmpty()) {
					return getDistictCity(customerLeLocList);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return null;
	}

	private List<String> getDistictCity(List<LocationLeCustomer> locationLeCustomers) {
		List<String> cities = new ArrayList<>();
		TreeSet<String> tSet = new TreeSet<>();
		locationLeCustomers.stream().forEach(loactionLeCustomer -> {

			if (loactionLeCustomer.getLocation() != null && loactionLeCustomer.getLocation().getAddressId() != null) {
				Optional<MstAddress> mstAddress = mstAddressRepository
						.findById(loactionLeCustomer.getLocation().getAddressId());
				if (mstAddress.isPresent()) {
					tSet.add(mstAddress.get().getCity());
				}
			}

		});
		if (!tSet.isEmpty()) {
			cities = new ArrayList<>(tSet);
		}
		return cities;
	}

	/**
	 * locationsExcelUpload - multiple location excel upload, validates and uploads
	 * the multiple locations to location database. If any validation issues then
	 * List<LocationUploadValidationBean> will be returned If location insertion
	 * success then List<BulkUploadNplResponse> will be returned
	 * 
	 * @param file
	 * @return Object
	 * @throws TclCommonException
	 */
	public Object locationsExcelUploadNpl(MultipartFile file) throws TclCommonException {
		String noResponse = "";
		try {
			List<LocationOfferingDetailNPL> locationDetailList = Collections.synchronizedList(new ArrayList<>());

			
			Set<LocationUploadValidationBean> validationList = validateUploadedDetailsNpl(file, locationDetailList);
			
			if (!validationList.isEmpty()) {
				return validationList;
			} else {
				if (!locationDetailList.isEmpty()) {
					return addAddressesOfferingNpl(locationDetailList);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return noResponse;
	}

	/**
	 * This method validates the data entered in the excel and returns
	 * Set<LocationUploadValidationBean> if there is any validation error. If there
	 * is no validation error then the address entered in each row is validated.
	 * 
	 * @param file
	 * @param locationDetailList
	 * @return Set<LocationUploadValidationBean>
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws TclCommonException
	 */

	public Set<LocationUploadValidationBean> validateUploadedDetailsNpl(MultipartFile file,
			List<LocationOfferingDetailNPL> locationDetailList)
			throws InvalidFormatException, IOException, TclCommonException {
		Set<LocationUploadValidationBean> validatorBean = Collections.synchronizedSet(new HashSet<>());
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");

			workbook.forEach(sheet -> {
				LOGGER.info("last row num => {}", sheet.getLastRowNum());
				LOGGER.info("last row num with data => {} ", getLastRowWithData(sheet));
				int lastRow = getLastRowWithData(sheet);
				for (int i = 0; i <= lastRow; i++) {

					Cell firstCell = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
					Cell profileCell = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);

					Cell countryACell = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
					Cell stateACell = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
					Cell cityACell = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
					Cell pinZipACell = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
					Cell localityACell = sheet.getRow(i).getCell(6, xRow.RETURN_BLANK_AS_NULL);
					Cell addressACell = sheet.getRow(i).getCell(7, xRow.RETURN_BLANK_AS_NULL);

					Cell typeACell = sheet.getRow(i).getCell(8, xRow.RETURN_BLANK_AS_NULL);
					Cell countryBCell = sheet.getRow(i).getCell(9, xRow.RETURN_BLANK_AS_NULL);
					Cell stateBCell = sheet.getRow(i).getCell(10, xRow.RETURN_BLANK_AS_NULL);
					Cell cityBCell = sheet.getRow(i).getCell(11, xRow.RETURN_BLANK_AS_NULL);
					Cell pinZipBCell = sheet.getRow(i).getCell(12, xRow.RETURN_BLANK_AS_NULL);
					Cell localityBCell = sheet.getRow(i).getCell(13, xRow.RETURN_BLANK_AS_NULL);
					Cell addressBCell = sheet.getRow(i).getCell(14, xRow.RETURN_BLANK_AS_NULL);
					Cell typeBCell = sheet.getRow(i).getCell(15, xRow.RETURN_BLANK_AS_NULL);

					List<CompletableFuture<Boolean>> futureList = new ArrayList<>();

					if (firstCell != null && countryACell != null && stateACell != null && cityACell != null
							&& pinZipACell != null && addressACell != null && profileCell != null
							&& localityACell != null && countryBCell != null && stateBCell != null && cityBCell != null
							&& pinZipBCell != null && addressBCell != null && localityBCell != null && typeACell != null
							&& typeBCell != null) {
						try {
							futureList.add(locationAsyncService.validateEachRowForNpl(workbook, sheet.getRow(i),
									validatorBean, locationDetailList));
						} catch (TclCommonException e) {
							LOGGER.info("Error in validate each row {}", e.getMessage());
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
					} else {
						if (!isRowBlank(sheet.getRow(i))) {
							LOGGER.info("Some column is blank or null in this row");
							List<LocationValidationColumnBean> columnValidationBeanList = new ArrayList<>();
							LocationUploadValidationBean eachRowFullValidation = new LocationUploadValidationBean();
							eachRowFullValidation
									.setRowDetails(LocationUploadConstants.ROW + sheet.getRow(i).getRowNum());

							validateCell(firstCell, columnValidationBeanList, LocationUploadConstants.HEADER_SERIAL_NO,
									LocationUploadConstants.SR_CANT_BE_EMPTY);

							validateCell(countryACell, columnValidationBeanList,
									LocationUploadConstants.HEADER_COUNTRY_A,
									LocationUploadConstants.COUNTRY_A_CANT_BE_EMPTY);

							validateCell(stateACell, columnValidationBeanList, LocationUploadConstants.HEADER_STATE_A,
									LocationUploadConstants.STATE_A_CANT_BE_EMPTY);

							validateCell(cityACell, columnValidationBeanList, LocationUploadConstants.HEADER_CITY_A,
									LocationUploadConstants.CITY_A_CANT_BE_EMPTY);

							validateCell(pinZipACell, columnValidationBeanList,
									LocationUploadConstants.HEADER_PINCODE_A,
									LocationUploadConstants.PINCODE_A_CANT_BE_EMPTY);

							validateCell(localityACell, columnValidationBeanList,
									LocationUploadConstants.HEADER_LOCALITY_A,
									LocationUploadConstants.LOCALITY_A_CANT_BE_EMPTY);

							validateCell(addressACell, columnValidationBeanList,
									LocationUploadConstants.HEADER_ADDRESS_A,
									LocationUploadConstants.ADDRESS_A_CANT_BE_EMPTY);

							validateCell(countryBCell, columnValidationBeanList,
									LocationUploadConstants.HEADER_COUNTRY_B,
									LocationUploadConstants.COUNTRY_B_CANT_BE_EMPTY);

							validateCell(stateBCell, columnValidationBeanList, LocationUploadConstants.HEADER_STATE_B,
									LocationUploadConstants.STATE_B_CANT_BE_EMPTY);

							validateCell(cityBCell, columnValidationBeanList, LocationUploadConstants.HEADER_CITY_B,
									LocationUploadConstants.CITY_B_CANT_BE_EMPTY);

							validateCell(pinZipBCell, columnValidationBeanList,
									LocationUploadConstants.HEADER_PINCODE_B,
									LocationUploadConstants.PINCODE_B_CANT_BE_EMPTY);

							validateCell(localityBCell, columnValidationBeanList,
									LocationUploadConstants.HEADER_LOCALITY_B,
									LocationUploadConstants.LOCALITY_B_CANT_BE_EMPTY);

							validateCell(addressBCell, columnValidationBeanList,
									LocationUploadConstants.HEADER_ADDRESS_B,
									LocationUploadConstants.ADDRESS_B_CANT_BE_EMPTY);

							validateCell(typeACell, columnValidationBeanList, LocationUploadConstants.HEADER_TYPE_A,
									LocationUploadConstants.TYPE_A_CANT_BE_EMPTY);

							validateCell(typeBCell, columnValidationBeanList, LocationUploadConstants.HEADER_TYPE_B,
									LocationUploadConstants.TYPE_B_CANT_BE_EMPTY);

							validateCell(profileCell, columnValidationBeanList, LocationUploadConstants.HEADER_PROFILES,
									LocationUploadConstants.PROFILE_CANT_BE_EMPTY);

							eachRowFullValidation.setColumns(columnValidationBeanList);
							validatorBean.add(eachRowFullValidation);
						} else {
							LOGGER.info("Full row is blank/Invalid input and the row number is {}", sheet.getRow(i));
						}
					}
					CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
				}
			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return validatorBean;
	}

	private void validateCell(Cell firstCell, List<LocationValidationColumnBean> columnValidationBeanList,
			String columnName, String errorMessage) {
		if (isCellBlank(firstCell) || isCellEmpty(firstCell) || null == firstCell) {
			LocationValidationColumnBean columnBeanSR = new LocationValidationColumnBean();
			columnBeanSR.setColumnName(columnName);
			columnBeanSR.setErrorMessage(errorMessage);
			columnValidationBeanList.add(columnBeanSR);
		}
	}

	/**
	 * addAddresses - This method will be used to add the list of locations and
	 * return the locationIds with offering names
	 * 
	 * @param locationDetailList
	 * @return List<BulkUploadNplResponse>
	 * @throws TclCommonException
	 */
	public List<BulkUploadNplResponse> addAddressesOfferingNpl(List<LocationOfferingDetailNPL> locationDetailList)
			throws TclCommonException {
		
		List<BulkUploadNplResponse> locationResponseList = new ArrayList<>();
		try {
			if (locationDetailList == null || locationDetailList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);

			List<CompletableFuture<BulkUploadNplResponse>> futureList = new ArrayList<>();

			locationDetailList.forEach(locDetail -> {
				try {
					futureList.add(locationAsyncService.addAddressNPL(locDetail));
				} catch (TclCommonException e) {
					LOGGER.warn("Error in addAddressNPL {}", e.getMessage());
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}

			});
			CompletableFuture<Void> allFuture = CompletableFuture
					.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));

			CompletableFuture<List<BulkUploadNplResponse>> finalFutureList = allFuture.thenApply(v -> futureList
					.stream().map(future -> future.join()).collect(Collectors.<BulkUploadNplResponse>toList()));

			locationResponseList = finalFutureList.get();

			if (locationResponseList.isEmpty())
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		return locationResponseList;
	}

	/**
	 * this method is used by downloadLocationTemplateForNPL method for checking the
	 * datavalidation
	 * 
	 * @author chetan chaudhary
	 * @param locationTemplate
	 * @param sheet
	 * @param x
	 * @param y
	 * @return the datavalidation
	 * @copyright www.tatacommunications.com
	 */
	public DataValidation dataValidation(LocationTemplateResponseBean locationTemplate, Sheet sheet, int x, int y) {
		DataValidation dataValidation = null;
		DataValidationConstraint constraint = null;
		DataValidationHelper validationHelper = null;
		validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 50, x, y);

		if (x == 1) {
			constraint = validationHelper
					.createExplicitListConstraint(locationTemplate.getProfiles().stream().toArray(String[]::new));
		}
		if (x == 2 || x == 9) {
			constraint = validationHelper
					.createExplicitListConstraint(locationTemplate.getCountries().stream().toArray(String[]::new));
		}
		if (x == 8 || x == 15) {
			constraint = validationHelper
					.createExplicitListConstraint(locationTemplate.getType().stream().toArray(String[]::new));
		}
		dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setSuppressDropDownArrow(true);
		dataValidation.setShowErrorBox(true);
		dataValidation.setShowPromptBox(true);
		return dataValidation;
	}

	/**
	 * this Service class gives you NPL location template
	 * 
	 * @author chetan chaudhary
	 * @param locationTemplate
	 * @param response
	 * @throws IOException
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright www.tatacommunications.com
	 */
	public void downloadLocationTemplateForNPL(LocationTemplateResponseBean locationTemplate,
			HttpServletResponse response) throws IOException, TclCommonException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
			header.createCell(1).setCellValue(LocationUploadConstants.HEADER_PROFILES);
			header.createCell(2).setCellValue(LocationUploadConstants.HEADER_COUNTRY_A);
			header.createCell(3).setCellValue(LocationUploadConstants.HEADER_STATE_A);
			header.createCell(4).setCellValue(LocationUploadConstants.HEADER_CITY_A);
			header.createCell(5).setCellValue(LocationUploadConstants.HEADER_PINCODE_A);
			header.createCell(6).setCellValue(LocationUploadConstants.LOCALITY_A);
			header.createCell(7).setCellValue(LocationUploadConstants.HEADER_ADDRESS_A);
			header.createCell(8).setCellValue(LocationUploadConstants.TYPE_A);
			header.createCell(9).setCellValue(LocationUploadConstants.HEADER_COUNTRY_B);
			header.createCell(10).setCellValue(LocationUploadConstants.HEADER_STATE_B);
			header.createCell(11).setCellValue(LocationUploadConstants.HEADER_CITY_B);
			header.createCell(12).setCellValue(LocationUploadConstants.HEADER_PINCODE_B);
			header.createCell(13).setCellValue(LocationUploadConstants.LOCALITY_B);
			header.createCell(14).setCellValue(LocationUploadConstants.HEADER_ADDRESS_B);
			header.createCell(15).setCellValue(LocationUploadConstants.TYPE_B);

			for (int i = 0; i < 16; i++) {
				CellStyle stylerowHeading = workbook.createCellStyle();
				stylerowHeading.setBorderBottom(BorderStyle.THICK);
				stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
				stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setFontHeightInPoints((short) 10);
				font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
				stylerowHeading.setFont(font);
				stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
				stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

				stylerowHeading.setWrapText(true);
				header.getCell(i).setCellStyle(stylerowHeading);

				DataFormat fmt = workbook.createDataFormat();
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
				sheet.setDefaultColumnStyle(i, textStyle);
			}

			sheet.setColumnWidth(0, 1800);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 8000);
			sheet.setColumnWidth(3, 8000);
			sheet.setColumnWidth(4, 7500);
			sheet.setColumnWidth(5, 10000);
			sheet.setColumnWidth(6, 8000);
			sheet.setColumnWidth(7, 8000);
			sheet.setColumnWidth(8, 8000);
			sheet.setColumnWidth(9, 8000);
			sheet.setColumnWidth(10, 7500);
			sheet.setColumnWidth(11, 10000);
			sheet.setColumnWidth(12, 8000);
			sheet.setColumnWidth(13, 8000);
			sheet.setColumnWidth(14, 8000);
			sheet.setColumnWidth(15, 8000);

			sheet.addValidationData(dataValidation(locationTemplate, sheet, 1, 1));
			sheet.addValidationData(dataValidation(locationTemplate, sheet, 2, 2));
			sheet.addValidationData(dataValidation(locationTemplate, sheet, 8, 8));
			sheet.addValidationData(dataValidation(locationTemplate, sheet, 9, 9));
			sheet.addValidationData(dataValidation(locationTemplate, sheet, 15, 15));

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = "location-template.xlsx";
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			try {
				FileCopyUtils.copy(outArray, response.getOutputStream());
			} catch (Exception e) {

			}

			outByteStream.flush();
			outByteStream.close();

		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing downloadLocationTemplate malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.MALFORMED_URL_EXCEPTION, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadLocationTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
	}

	/**
	 * Method to get connected buildings
	 *
	 * @param latLong
	 * @param kms
	 * @return
	 */
	public List<ConnectedLocationResponse> getConnectedBuildings(List<String> latLong, Integer kms) {
		List<ConnectedLocationResponse> connectedLocations = new ArrayList<>();
		latLong.stream().forEach(latLng -> {
			connectedLocations.addAll(getConnectedBuildingsLatLong(latLng, kms));
		});
		return connectedLocations;
	}

	/**
	 * Method to get connected building latLong details
	 * 
	 * @param latLong
	 * @return
	 */
	private List<ConnectedLocationResponse> getConnectedBuildingsLatLong(String latLong, Integer kms) {

		String latLongArray[] = latLong.split(",");
		List<String> latLongList = Arrays.asList(latLongArray);
		String latitude = latLongList.get(0);
		String longitude = latLongList.get(1);
		Double latitudeRad = Double.parseDouble(latitude) * 0.0174533;
		Double longitudeRad = Double.parseDouble(longitude) * 0.0174533;
		LOGGER.info("Latitude " + latitude + " Longitude" + longitude);
		return findLatLongWithinKm(latitudeRad, longitudeRad, kms);

	}

	/**
	 * Method to find latLong within 1 km
	 *
	 * @param latitudeRad
	 * @param longitudeRad
	 * @return
	 */
	private List<ConnectedLocationResponse> findLatLongWithinKm(Double latitudeRad, Double longitudeRad, Integer kms) {
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> locationIds = new ArrayList<>();
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
		if (customerDetails != null) {
			for (CustomerDetail customerDetail : customerDetails) {
				customerIds.add(customerDetail.getCustomerId());
			}
		}
		LOGGER.info("customerIds" + customerIds);
		return getConnectedBuildings(customerIds, latitudeRad, longitudeRad, kms);
	}

	/**
	 * Method to get connectedBuildings
	 *
	 * @param customerIds
	 * @param latitudeRad
	 * @param longitudeRad
	 * @param kms
	 * @return
	 */
	private List<ConnectedLocationResponse> getConnectedBuildings(List<Integer> customerIds, Double latitudeRad,
			Double longitudeRad, Integer kms) {
		List<String> connectedBuildingList = new ArrayList<>();
		List<ConnectedLocationResponse> connectedBuildingResponse = new ArrayList<>();
		List<Map<String, Object>> locations = locationRepository.getLocationDetail(customerIds);
		locations.stream().forEach(location -> {
			String latLong = (String) location.get("lat_long");
			String addressLineOne = (String) location.get("address_line_one");
			String addressLineTwo = (String) location.get("address_line_two");
			String pincode = (String) location.get("pincode");
			String locality = (String) location.get("locality");
			String state = (String) location.get("state");
			String country = (String) location.get("country");
			StringBuilder address = new StringBuilder();
			address.append(addressLineOne).append(",").append(addressLineTwo).append(",").append(locality).append(",")
					.append(state).append("-").append(pincode).append(",").append(country);
			String addressVal = address.toString().replace("null,", "");
			Integer locationId = (Integer) location.get("id");
			if (StringUtils.isNotBlank(latLong) && !latLong.equals(",") && latLong.contains(",")) {
				String latLngArray[] = latLong.split(",");
				List<String> latLngList = Arrays.asList(latLngArray);
				if (!latLngList.isEmpty()) {

					String lat = latLngList.get(0);
					String lon = latLngList.get(1);
					Double latRad = Double.parseDouble(lat) * 0.0174533;
					Double lonRad = Double.parseDouble(lon) * 0.0174533;
					Double distance = Math
							.acos(Math.sin(latitudeRad) * Math.sin(latRad)
									+ Math.cos(latitudeRad) * Math.cos(latRad) * Math.cos(lonRad - (longitudeRad)))
							* 6371;

					if (distance <= kms && distance != 0) {
						LOGGER.info("latLong" + latLngList.toString());
						LOGGER.info("distance" + distance);
						String latLongFound = lat + "," + lon;
						if (!connectedBuildingList.contains(latLongFound)) {
							connectedBuildingList.add(latLongFound);
							ConnectedLocationResponse conLocation = new ConnectedLocationResponse();
							conLocation.setAddress(addressVal);
							conLocation.setLatitude(lat);
							conLocation.setLongitude(lon);
							conLocation.setTempMarkerData(addressVal);
							conLocation.setLocationId(locationId);
							connectedBuildingResponse.add(conLocation);

						}
					}
				}
			}
		});
		return connectedBuildingResponse;
	}

	/**
	 * addCustomerBillingAddress to mstaddress and location and locetionlecustomer
	 * table
	 * 
	 * @param MSTAddressDetails
	 * @throws TclCommonException
	 */
	public String addBillingAddress(MSTAddressDetails mstAddressDetails) {

		MstAddress address = new MstAddress();
		address.setAddressLineOne(mstAddressDetails.getAddress_Line_One());
		address.setPincode(mstAddressDetails.getPinCode());
		address.setCity(mstAddressDetails.getCity());
		address.setLocality(mstAddressDetails.getLocality());
		address.setCountry(mstAddressDetails.getCountry());
		address.setSource(LocationConstants.API_SOURCE.toString());
		address.setCreatedTime(new Timestamp(new Date().getTime()));
		address.setState(mstAddressDetails.getState());
		address.setCreatedBy(Utils.getSource());
		address = mstAddressRepository.save(address);

		Location locationEntity = new Location();
		locationEntity.setAddressId(address.getId());
		locationEntity.setCreatedDate(new Timestamp(new Date().getTime()));
		locationEntity.setIsActive(CommonConstants.BACTIVE);
		locationEntity.setIsVerified(0);
		locationEntity = locationRepository.save(locationEntity);

//		LocationLeCustomer customer= new LocationLeCustomer();
//		customer.setLocation(locationEntity);
//		customer.setErfCusCustomerLeId(mstAddressDetails.getCustomer_Le_Id());
//		customer=locationLeCustomerRepository.save(customer);
		String locationId = locationEntity.getId().toString();
		return locationId;

	}

	/**
	 * GET CustomerBillingAddress
	 * 
	 * @param MSTAddressDetails
	 * @throws TclCommonException
	 */

	public String getBillingAddress(String customerlocationId) throws TclCommonException {
		Optional<MstAddress> address = null;
		Optional<Location> location = null;
		MSTAddressDetails addressDetail = null;
		// Optional<LocationLeCustomer> locationLeCustomer =
		// locationLeCustomerRepository.findById(Integer.parseInt(customerlocationId));
		// if(locationLeCustomer.isPresent()) {
		location = locationRepository.findById(Integer.parseInt(customerlocationId));
		// }
		if (location.isPresent()) {
			address = mstAddressRepository.findById(location.get().getAddressId());
			addressDetail = constructMstAddress(address.get());
		}

		String response = null;
		try {
			response = Utils.convertObjectToJson(addressDetail).toString();
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return response;
	}

	/**
	 * CONSTRUCT MST ADDRESS
	 * 
	 * @param MSTAddressDetails
	 * @throws TclCommonException
	 */

	private MSTAddressDetails constructMstAddress(MstAddress address) {
		MSTAddressDetails addressDetail = new MSTAddressDetails();
		addressDetail.setAddress_Line_One(Utils.removeUnicode(address.getAddressLineOne()));
		addressDetail.setCity(address.getCity());
		addressDetail.setCountry(address.getCountry());
		// addressDetail.setCustomer_Le_Id(LegalEntityId);
		addressDetail.setLocality(address.getLocality());
		// addressDetail.setLocation_Le_Id(LeCustomerId);
		addressDetail.setPinCode(address.getPincode());
		addressDetail.setState(address.getState());
		return addressDetail;
	}

	/**
	 * @author chetan chaudhary
	 * @param locationType
	 * @param locationEnd
	 * @return List of addresses along with other parameters
	 * @throws TclCommonException
	 */
	public List<LocationResponseBeanUsingPopAddresssId> getlocationUsingAddressType(String locationType,
			String locationEnd) {
		String type = locationType;
		if ((LocationUploadConstants.POP).equalsIgnoreCase(locationType)) {
			type = "TIN";
		}

		List<Map<String, Object>> allAddresses = locationRepository.getAddressDetails(type);
		List<LocationResponseBeanUsingPopAddresssId> typeAddressList = new ArrayList<LocationResponseBeanUsingPopAddresssId>();
		allAddresses.stream().forEach(address -> {
			LocationResponseBeanUsingPopAddresssId locationResponseBeanUsingPopAddresssId = new LocationResponseBeanUsingPopAddresssId();
			Integer locationId = (Integer) address.get("locationID");
			String latlong = (String) address.get("LatLong");
			String effectiveAddress = Arrays
					.asList("AddressLine1", "AddressLine2", "Pincode", "Locality", "City", "State", "Country").stream()
					.map(k -> address.get(k)).filter(Objects::nonNull).map(Object::toString)
					.collect(Collectors.joining(" "));
			locationResponseBeanUsingPopAddresssId.setLocationType(locationType);
			locationResponseBeanUsingPopAddresssId.setLatLong(latlong);
			locationResponseBeanUsingPopAddresssId.setLocationId(locationId);
			locationResponseBeanUsingPopAddresssId.setLocationAddress(effectiveAddress);
			locationResponseBeanUsingPopAddresssId.setLocationEnd(locationEnd);
			typeAddressList.add(locationResponseBeanUsingPopAddresssId);
		});

		return typeAddressList;
	}

	/**
	 *
	 * Get Local IT contact by location
	 *
	 * @param locationId
	 * @param customerLeId
	 * @return {@link LocationItContact}
	 * @throws TclCommonException
	 */
	public LocationItContact getLocalITContactByLocation(Integer locationId, Integer customerLeId)
			throws TclCommonException {
		LocationItContact localItContact = null;
		try {
			Optional<Location> location = locationRepository.findById(locationId);
			if (location.isPresent()) {
				Optional<LocationLeCustomer> locationLeCustomer = locationLeCustomerRepository
						.findByErfCusCustomerLeIdAndLocation(customerLeId, location.get());
				if (locationLeCustomer.isPresent()) {
					CustomerSiteLocationItContact customerSiteLocationItContact = customerLocationItInfoRepository
							.findByCustomerLeLocationAndIsActive(locationLeCustomer.get(), (byte) 1);
					if (Objects.nonNull(customerSiteLocationItContact)) {
						localItContact = new LocationItContact();
						localItContact.setContactNo(customerSiteLocationItContact.getContactNumber());
						localItContact.setName(customerSiteLocationItContact.getName());
						localItContact.setEmail(customerSiteLocationItContact.getEmailId());
						localItContact.setLocalItContactId(customerSiteLocationItContact.getId());
					}
				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return localItContact;
	}

	/**
	 * 
	 * Return city detail based on location ID
	 * 
	 * @param locationId
	 * @return
	 */
	public String returnCityDetailForTheLocation(Integer locationId) {
		if (locationId != null) {
			try {
				Optional<Location> location = locationRepository.findById(locationId);
				if (location.isPresent()) {
					if (location.get().getAddressId() != null) {
						Optional<MstAddress> mstAddress = mstAddressRepository.findById(location.get().getAddressId());
						if (mstAddress.isPresent()) {
							return mstAddress.get().getCity();
						}
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error while fetching the details");
			}
		}
		return CommonConstants.EMPTY;
	}

	/**
	 * 
	 * Get Site Types Based on Location IDS
	 * 
	 * @param locationIds
	 * @return
	 */
	public String returnTypeOfSitesByLocationIds(List<Integer> locationIds) {
		if (locationIds != null && !locationIds.isEmpty()) {
			List<Map<String, Object>> indiaLocationList = locationRepository
					.getIndiaContainingAddressByLocationIds(locationIds);
			List<Map<String, Object>> nonIndiaLocationList = locationRepository
					.getNonIndiaContainingAddressByLocationIds(locationIds);
			Boolean containsIndiaSites = false;
			Boolean containsNonIndiaSites = false;
			if (indiaLocationList != null && !indiaLocationList.isEmpty()) {
				containsIndiaSites = true;
			}
			if (nonIndiaLocationList != null && !nonIndiaLocationList.isEmpty()) {
				containsNonIndiaSites = true;
			}
			if (containsIndiaSites && !containsNonIndiaSites) {
				return CommonConstants.INDIA_SITES;
			} else if (!containsIndiaSites && containsNonIndiaSites) {
				return CommonConstants.INTERNATIONAL_SITES;
			} else if (containsIndiaSites && containsNonIndiaSites) {
				return CommonConstants.INDIA_INTERNATIONAL_SITES;
			}
		}
		return CommonConstants.EMPTY;

	}

	/**
	 * getPincodeDetails-This method will be used to get the pincode details from
	 * repo and return back the pincode. getPincodeDetails
	 *
	 * @param pincode
	 * @param country
	 * @return PincodeDetail
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getCitiesByPincode(String pincode) throws TclCommonException {
		if (StringUtils.isBlank(pincode)) {
			LOGGER.info("Pincode is empty!!");
			throw new TclCommonException(ExceptionConstants.LOCATION_PIN_ERROR, ResponseResource.R_CODE_ERROR);
		}
		List<Map<String, Object>> cities = new ArrayList<>();
		try {

			LOGGER.info("Pincode {} received", pincode);
			List<Map<String, Object>> pincodeResponse = mstPincodeRespository.findCityDetailsByPincode(pincode);
			if (!pincodeResponse.isEmpty()) {
				LOGGER.info("Got City details!!!!");
				pincodeResponse.stream().forEach(pincodeMap -> {
					if (!cities.contains(pincodeMap.get(PincodeConstants.CITY.toString()))) {
						Map<String, Object> cityMap = new HashMap<>();
						cityMap.put(PincodeConstants.CITY.toString(),
								(String) pincodeMap.get(PincodeConstants.CITY.toString()));
						cityMap.put(PincodeConstants.CITY_ID.toString(),
								(Integer) pincodeMap.get(PincodeConstants.CITY_ID.toString()));
						cities.add(cityMap);
					}

				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return cities;
	}

	/**
	 * 
	 * Get Locality Details by Pincode City And Country
	 * 
	 * @param pincode
	 * @param country
	 * @param city
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getLocalityByCityAndPincode(String pincode, String city) throws TclCommonException {
		List<String> localities = new ArrayList<>();
		if (StringUtils.isBlank(pincode) || StringUtils.isBlank(city)) {
			LOGGER.info("Pincode or city is empty!!");
			throw new TclCommonException(ExceptionConstants.LOCATION_PIN_ERROR, ResponseResource.R_CODE_ERROR);
		}
		try {

			List<Map<String, Object>> localityResponse = mstPincodeRespository.findByPincodeAndCity(pincode, city);
			if (!localityResponse.isEmpty()) {
				LOGGER.info("Got Locality details!!!!");
				localityResponse.stream().forEach(pincodeMap -> {
					localities.add((String) pincodeMap.get(PincodeConstants.LOCALITY.toString()));
				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return localities;
	}

	/**
	 * 
	 * Get state details by city
	 * 
	 * @param city
	 * @return
	 * @throws TclCommonException
	 */
	public String getStateInformationOfTheCity(Integer city) throws TclCommonException {
		try {
			if (city == null) {
				LOGGER.info("city is empty!!");
				throw new TclCommonException(ExceptionConstants.LOCATION_PIN_ERROR, ResponseResource.R_CODE_ERROR);
			}
			Optional<MstCity> mstCity = mstCityRepository.findById(city);
			if (mstCity.isPresent() && mstCity.get().getMstState() != null
					&& mstCity.get().getMstState().getName() != null) {
				return mstCity.get().getMstState().getName();
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * GET CustomerBillingAddress
	 * 
	 * @param MSTAddressDetails
	 * @throws TclCommonException
	 */

	public String getcountryToRegionId(String customerlocationId) throws TclCommonException {
		Optional<MstAddress> address = null;
		Optional<Location> location = null;
		MSTAddressDetails addressDetail = new MSTAddressDetails();
		// Optional<LocationLeCustomer> locationLeCustomer =
		// locationLeCustomerRepository.findById(Integer.parseInt(customerlocationId));
		// if(locationLeCustomer.isPresent()) {
		location = locationRepository.findById(Integer.parseInt(customerlocationId));
		// }
		if (location.isPresent()) {
			address = mstAddressRepository.findById(location.get().getAddressId());
			MstCountry country = mstCountryRepository.findByName(address.get().getCountry());
			CountryToRegion countryreg = countryToRegionRepository.findByCountryId(country);
			addressDetail.setCountry_To_Region_Id(countryreg.getId());
		}

		String response = null;
		try {
			response = Utils.convertObjectToJson(addressDetail).toString();
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return response;
	}

	public Map<String, LeStateGstInfoBean> getStateAndCityForLocationId(LocationDetails locationsIds)
			throws TclCommonException {
		AddressDetail addressDetail = new AddressDetail();
		List<AddressDetail> addressDetailList = new ArrayList<>();
		List<LocationDetail> locationDetails = new ArrayList<>();
		// LeStateGstInfoBean leStateGstInfoBean = new LeStateGstInfoBean();
		Map<String, LeStateGstInfoBean> detailsMap = new HashMap<>();

		if (CollectionUtils.isEmpty(locationsIds.getLocationIds())) {
			throw new TclCommonException(ExceptionConstants.LOCATION_ID_EMPTY_ERROR, ResponseResource.R_CODE_ERROR);
		}

		List<Integer> locIds = locationsIds.getLocationIds();
		Optional<List<Location>> locationDetailEntities = locationRepository.findByIdIn(locIds);

		if (locationDetailEntities.isPresent()) {
			List<Location> locations = locationDetailEntities.get();
			locations.parallelStream().forEach(loc -> {
				Integer mstAddrId = loc.getAddressId();
				LocationDetail locationDetail = new LocationDetail();
				Optional<MstAddress> address = mstAddressRepository.findById(mstAddrId);
				if (address.isPresent() && Objects.nonNull(loc)) {
					LeStateGstInfoBean leStateGstInfoBean = new LeStateGstInfoBean();
					leStateGstInfoBean.setCity(address.get().getCity());
					leStateGstInfoBean.setState(address.get().getState());
					detailsMap.put(loc.getId().toString(), leStateGstInfoBean);
				}
			});
		} else {
			throw new TclCommonException(ExceptionConstants.GET_LOCATION_COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}

		return detailsMap;
	}

	/**
	 * validateStateCode
	 */
	public Boolean validateStateCode(String stateCode, Integer locationId) {
		Boolean isValid = false;
		try {
			Optional<Location> location = locationRepository.findById(locationId);
			if (location.isPresent()) {
				if (location.get().getAddressId() != null) {
					Optional<MstAddress> mstAddress = mstAddressRepository.findById(location.get().getAddressId());
					if (mstAddress.isPresent()) {
						String state = mstAddress.get().getState();
						MstState mstState = mstStateRepository.findByName(state);
						String mstStateCode = mstState.getStateCode();
						LOGGER.info("Mst state code for location is {} for state {} and address id {}", mstStateCode, state, location.get().getAddressId());
						if (mstStateCode != null && mstStateCode.trim().equals(stateCode.trim())) {
							isValid = true;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting validateStateCode",e);
		}
		LOGGER.info("is valid value is {} ", isValid);
		return isValid;
	}

	/**
	 * Method to get or add location.
	 * @param locationDetail
	 * @return
	 */
	public LocationResponse getOrAddLocation(LocationDetail locationDetail){
		LocationResponse locationResponse = new LocationResponse();
		String user = Utils.getSource();
		Integer addressId = locationAsyncService.persistAddressLoc(locationDetail.getAddress(),
				LocationConstants.CISCO_WEBEX.toString(), user);
		locationResponse.setLocationId(persistLocation(locationDetail,addressId,null).getId());
		return locationResponse;
	}

	/**
	 * Method to save location details
	 * @param locationDetail
	 * @param addressId
	 * @param customerLocationEntity
	 * @return
	 */
	public Location persistLocation(LocationDetail locationDetail, Integer addressId,
									CustomerLocation customerLocationEntity) {
		List<Location> locations = locationRepository.findByAddressId(addressId);
		if(locations.isEmpty()){
			Location locationEntity = new Location();
			locationEntity.setAddressId(addressId);
			locationEntity.setCreatedDate(new Timestamp(new Date().getTime()));
			locationEntity.setIsActive(CommonConstants.BACTIVE);
			locationEntity.setIsVerified(0);
			locationRepository.save(locationEntity);
			if (customerLocationEntity == null)
				customerLocationEntity = new CustomerLocation();
			customerLocationEntity.setErfCusCustomerId(Objects.nonNull(locationDetail.getCustomerId())?
					locationDetail.getCustomerId():null);
			customerLocationEntity.setLocation(locationEntity);
			customerLocationRepository.save(customerLocationEntity);
			return locationEntity;
		}
		return locations.get(0);
	}


	/*
	 * Return pop location based on location ID
	 *
	 * @param locationId
	 * @return
	 */
	public String returnPopDetailsForTheLocation(Integer locationId) {
		if (locationId != null) {
			try {
				Optional<Location> location = locationRepository.findById(locationId);
				if (location.isPresent()) {
					return location.get().getPopLocationId();

				}
			} catch (Exception e) {
				LOGGER.warn("Error while fetching pop details");
			}
		}
		return CommonConstants.EMPTY;
	}
	
	/**
	 *
	 * Update latlong for given location ID
	 *
	 * @param quoteId and location id list
	 * @return
	 */
	public void updateLatlong(Integer quoteId, List<PunchLatlongBean> latlongList)  throws TclCommonException {
		LOGGER.info("Update latlong request -->" + latlongList);
		if (latlongList != null && !latlongList.isEmpty()) {
			try {
				for (PunchLatlongBean request : latlongList) {
					Optional<Location> location = locationRepository.findById(request.getLocationId());
					LOGGER.info("Fetching location details");
					if (location.isPresent()) {
						location.get().setLatLong(request.getLatlong());
						locationRepository.save(location.get());
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error while fetching location details");
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
	}

	/**
    *
    * address custom address into MST table
    *
    * @param addressDetail
    * @return
    */
  
  
   public void addCustomAddress(CustomAddressBean addressDetail) throws TclCommonException {
       LOGGER.info("Entered into addCustomAddress method -->" + addressDetail); {
           if (addressDetail.getPincode() != null && addressDetail.getCity() != null) {
               LOGGER.info("validation completed" + addressDetail);
               try {
                   MstAddress customAddress = getCustmAddress(addressDetail);
                   MstAddress addedCustomAddress = mstAddressRepository.save(customAddress);
                   if (addedCustomAddress.getId() != null) {
                       LOGGER.info("Custome entry is done in MST table");
                       Location locationValue = setUpdatedLocation(addedCustomAddress, addressDetail);
                       Location addedLocation = locationRepository.save(locationValue);
                       System.out.println(addedLocation.getId());
                       if (addedLocation.getId() != null) {
                           CustomerLocation customerLocation = new CustomerLocation();
                           customerLocation.setLocation(addedLocation);
                           customerLocation.setErfCusCustomerId(addressDetail.getCustomerId());
                           CustomerLocation addedCustLocation = customerLocationRepository.save(customerLocation);
                           System.out.println(addedCustLocation.getId());
                           if (addedCustLocation.getId() != null) {
                               LocationLeCustomer locationLeCustomer = new LocationLeCustomer();
                               locationLeCustomer.setLocation(addedLocation);
                               locationLeCustomer.setErfCusCustomerLeId(addressDetail.getCustomerLeId());
                               LocationLeCustomer addedData = locationLeCustomerRepository.save(locationLeCustomer);
                               System.out.println(addedData.getId());
                           } else {
                               LOGGER.warn("Error while adding new address in Customer location Table");
                               throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
                           }
                          
                       } else {
                           LOGGER.warn("Error while adding new address in Location Table");
                           throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
                       }
                   } else {
                       LOGGER.warn("Error while adding new address in MST Table");
                       throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
                   }
               } catch (Exception e) {
                   LOGGER.warn("Error while fetching location details");
                   throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
               }
                  
           }
              
       }
   }
  
  
   public MstAddress getCustmAddress(CustomAddressBean addressDetail) {
       MstAddress customAddedAddress = new MstAddress();
       customAddedAddress.setAddressLineOne(addressDetail.getAddress_line_one());
       customAddedAddress.setCity(addressDetail.getCity());
       customAddedAddress.setCountry(addressDetail.getCountry());
       customAddedAddress.setSource(addressDetail.getSource());
       customAddedAddress.setCountry(addressDetail.getCountry());
       customAddedAddress.setState(addressDetail.getState());
       customAddedAddress.setPincode(addressDetail.getPincode());
       customAddedAddress.setCreatedBy(addressDetail.getCreated_by());
       customAddedAddress.setLocality(addressDetail.getLocality());
       return customAddedAddress;
   }
  
   public Location setUpdatedLocation(MstAddress addedCustomAddress, CustomAddressBean addressDetail) {
       Location locationValue = new Location();
       locationValue.setAddressId(addedCustomAddress.getId());
       locationValue.setApiAddressId(addedCustomAddress.getId());
       locationValue.setLatLong(addressDetail.getLat_long());
       locationValue.setIsVerified(0);
       locationValue.setIsActive((byte) 1);
       locationValue.setRemarks(addressDetail.getRemarks());
       locationValue.setCreatedDate(new Date());
       return locationValue;
   }

	public GscProductAddressResponse saveGscAddress(GscAddressDetailBean gscAddressDetailBeans){
		List<DomesticVoiceAddressDetail> domesticVoiceAddressDetails = gscAddressDetailBeans.getDomesticVoiceAddressDetailsList();
		List<Integer> addressIds = new ArrayList<>();
		List<Long> count = new ArrayList<>();
		GscProductAddressResponse gscProductAddressResponse = new GscProductAddressResponse();

		Map<DomesticVoiceAddressDetail, Long> uniqueAddressWithCount = domesticVoiceAddressDetails.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		uniqueAddressWithCount.forEach((domesticVoiceAddressDetail, aLong) -> {
			MstAddress address = new MstAddress();
			address.setAddressLineOne(domesticVoiceAddressDetail.getFloor());
			address.setAddressLineTwo(domesticVoiceAddressDetail.getAddress());
			address.setLocality(domesticVoiceAddressDetail.getLocality());
			address.setPincode(domesticVoiceAddressDetail.getPostalCode());
			address.setCity(domesticVoiceAddressDetail.getCity());
			address.setState(domesticVoiceAddressDetail.getState());
			address.setCountry(domesticVoiceAddressDetail.getCountry());
			address.setSource(LocationConstants.API_SOURCE.toString());
			address.setCreatedTime(new Timestamp(new Date().getTime()));
			MstAddress mstAddress = mstAddressRepository.save(address);
			addressIds.add(mstAddress.getId());
			count.add(aLong);
		});
		gscProductAddressResponse.setAddressIds(addressIds);
		gscProductAddressResponse.setUniqueAddressCount(count);
		return gscProductAddressResponse;
	}


	/**
	 * Validate GST state and number
	 *
	 * @param state
	 * @param gstno
	 * @return
	 */
	public Boolean validateGstStateAndNumber(String state, String gstno) {
		String[] spaceSeparated = state.split(" ");
		List<MstState> mstState = mstStateRepository.findByFirstName(spaceSeparated[0]);
		if (Objects.nonNull(mstState) && !mstState.isEmpty()) {
			return gstno.startsWith(mstState.stream().findAny().get().getStateCode());
		}
		return false;
	}

	public CustomAddressBean returnPartnerContractingAddressForLocId(Integer loctionId){
		CustomAddressBean customAddressBean = new CustomAddressBean();
		locationRepository.findById(loctionId).ifPresent(location -> {
			mstAddressRepository.findById(location.getAddressId()).ifPresent(mstAddress -> {
				String address = Utils.removeUnicode(mstAddress.getAddressLineOne());
				address = (Objects.nonNull(mstAddress.getAddressLineTwo()) &&
						StringUtils.isNoneEmpty(mstAddress.getAddressLineTwo())) ?
						address + Utils.removeUnicode(mstAddress.getAddressLineTwo()) : address;
				customAddressBean.setAddress_line_one(address);
				customAddressBean.setPincode(mstAddress.getPincode());
				customAddressBean.setCity(mstAddress.getCity());
				customAddressBean.setState(mstAddress.getState());
				customAddressBean.setCountry(mstAddress.getCountry());
			});
		});
		return customAddressBean;
	}

	public Map<String, Set<String>> getStatesCitiesDetails(String country) throws TclCommonException{
		Objects.requireNonNull(country);
		List<MstLocationData> mstLocationData = mstLocationDataRepository.findByCountryName(country)
				.orElseThrow(() -> new TclCommonException("States and City not found for given country"));
		TreeMap<String, Set<String>> stateCitesDetails = mstLocationData.stream().
				filter(obj -> Objects.nonNull(obj.getStateName()) && Objects.nonNull(obj.getCityName()))
				.collect(Collectors.groupingBy(MstLocationData::getStateName, TreeMap::new,
						Collectors.mapping(MstLocationData::getCityName, Collectors.toCollection(TreeSet::new))));
		return stateCitesDetails;
	}
}

