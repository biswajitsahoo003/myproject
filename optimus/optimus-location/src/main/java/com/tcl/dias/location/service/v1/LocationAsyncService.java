package com.tcl.dias.location.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.beans.BulkUploadNplResponse;
import com.tcl.dias.location.beans.GeocodeBean;
import com.tcl.dias.location.beans.LocationBean;
import com.tcl.dias.location.beans.LocationMultipleResponse;
import com.tcl.dias.location.beans.LocationOfferingDetail;
import com.tcl.dias.location.beans.LocationOfferingDetailNPL;
import com.tcl.dias.location.beans.LocationUploadValidationBean;
import com.tcl.dias.location.beans.LocationValidationColumnBean;
import com.tcl.dias.location.beans.SiteDetailBean;
import com.tcl.dias.location.beans.ValidateAddressRequest;
import com.tcl.dias.location.constants.Constants;
import com.tcl.dias.location.constants.ExceptionConstants;
import com.tcl.dias.location.constants.LocationConstants;
import com.tcl.dias.location.constants.LocationUploadConstants;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.MstAddress;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstLocality;
import com.tcl.dias.location.entity.entities.MstPincode;
import com.tcl.dias.location.entity.entities.MstState;
import com.tcl.dias.location.entity.repository.CustomerLocationRepository;
import com.tcl.dias.location.entity.repository.LocationRepository;
import com.tcl.dias.location.entity.repository.MstAddressRepository;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstCountryRepository;
import com.tcl.dias.location.entity.repository.MstLocalityRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstStateRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the LocationAsyncService.java class.
 * This class provides functions to make Async calls to validate address and get lat,long details from google API and persist location details.
 *
 * @author Samuel S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class LocationAsyncService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationAsyncService.class);

	@Autowired
	private MstPincodeRespository mstPincodeRespository;

	@Autowired
	private MstCityRepository mstCityRepository;

	@Autowired
	private MstCountryRepository mstCountryRepository;
	
	@Autowired
	private MstStateRepository mstStateRepository;

	@Value("${api.googlemap.key}")
	private String googleMapApiKey;

	@Value("${api.googlemap}")
	private String googleMapAPI;

	@Value("${rabbitmq.customer.les.queue}")
	private String customerLeToCustomerQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	LocationExcelUtils excelUtils;

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CustomerLocationRepository customerLocationRepository;

	@Autowired
	private MstAddressRepository mstAddressRepository;

	@Autowired
	private MstLocalityRepository mstLocalityRepository;
	
	@Autowired
	private LocationCacheService locationCacheService;

	/**
	 * validateEachRow - this method validates the uploaded location details and
	 * captures if any validation issues row & column wise.
	 * 
	 * @param row
	 * @param validatorBean
	 * @author NAVEEN GUNASEKARAN
	 * @throws TclCommonException
	 */
	@Async
	public CompletableFuture<Boolean> validateEachRow(Workbook workbook, Row row,
			Set<LocationUploadValidationBean> validatorBean, List<LocationOfferingDetail> locationDetailList)
			throws TclCommonException {
		try {

			DataFormatter objDefaultFormat = new DataFormatter();
			FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator stateValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator countryValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator cityValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			if (row.getRowNum() > 0) {
				LOGGER.info("validate and invoke google API for RowNum={}", row.getRowNum());
				String country = "";
				if (null == row.getCell(1)) {
					country = "";
				} else {
					countryValuator.evaluate(row.getCell(1));
					country = StringUtils
							.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(1), countryValuator)));
				}
				country = StringUtils.replace(country, Constants.UA, "");

				String stateArg = "";
				if (null == row.getCell(2)) {
					stateArg = "";
				} else {
					stateValuator.evaluate(row.getCell(2));
					stateArg = StringUtils
							.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(2), stateValuator)));
				}
				stateArg = StringUtils.replace(stateArg, Constants.UA, "");

				String cityArg = "";
				if (null == row.getCell(3)) {
					cityArg = "";
				} else {
					cityValuator.evaluate(row.getCell(3));
					cityArg = StringUtils.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(3), cityValuator)));

				}
				cityArg = StringUtils.replace(cityArg, Constants.UA, "");

				String pincode = "";
				if (null == row.getCell(4)) {
					pincode = "";
				} else {
					objFormulaEvaluator.evaluate(row.getCell(4));
					pincode = StringUtils
							.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(4), objFormulaEvaluator)));

				}
				pincode = StringUtils.replace(pincode, Constants.UA, "");

				String address = StringUtils.trimToEmpty((row.getCell(5).getStringCellValue()));
				address = StringUtils.replace(address, Constants.UA, "");

				String offeringName = StringUtils.trimToEmpty((row.getCell(6).getStringCellValue()));
				offeringName = StringUtils.replace(offeringName, Constants.UA, "");

				validateAddress(row, validatorBean, address, country, stateArg, cityArg, pincode, isIndia(country),
						locationDetailList, offeringName);

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		return CompletableFuture.completedFuture(true);
	}

	/**
	 * validateEachRowForIas - this method validates the uploaded location details
	 * and captures if any validation issues row & column wise.
	 * 
	 * @param row
	 * @param validatorBean
	 * @author NAVEEN GUNASEKARAN
	 * @throws TclCommonException
	 */
	@Async
	public CompletableFuture<Boolean> validateEachRowForIas(Workbook workbook, Row row,
			Set<LocationUploadValidationBean> validatorBean, List<LocationOfferingDetail> locationDetailList)
			throws TclCommonException {
		try {
			DataFormatter objDefaultFormat = new DataFormatter();
			FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator stateValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator countryValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator cityValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			if (row.getRowNum() > 0) {
				String country = "";
				if (null == row.getCell(1)) {
					country = "";
				} else {
					countryValuator.evaluate(row.getCell(1));
					country = StringUtils
							.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(1), countryValuator)));
				}
				country = StringUtils.replace(country, Constants.UA, "");

				String stateArg = "";
				if (null == row.getCell(2)) {
					stateArg = "";
				} else {
					stateValuator.evaluate(row.getCell(2));
					stateArg = StringUtils
							.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(2), stateValuator)));
				}
				stateArg = StringUtils.replace(stateArg, Constants.UA, "");

				String cityArg = "";
				if (null == row.getCell(3)) {
					cityArg = "";
				} else {
					cityValuator.evaluate(row.getCell(3));
					cityArg = StringUtils.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(3), cityValuator)));

				}
				cityArg = StringUtils.replace(cityArg, Constants.UA, "");

				String pincode = "";
				if (null == row.getCell(4)) {
					pincode = "";
				} else {
					objFormulaEvaluator.evaluate(row.getCell(4));
					pincode = StringUtils
							.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(4), objFormulaEvaluator)));

				}
				pincode = StringUtils.replace(pincode, Constants.UA, "");

				String locality = "";

				if (null == row.getCell(5)) {
					locality = "";
				} else {
					objFormulaEvaluator.evaluate(row.getCell(5));
					locality = StringUtils
							.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(5), objFormulaEvaluator)));
				}
				locality = StringUtils.replace(locality, Constants.UA, "");

				String address = StringUtils.trimToEmpty((row.getCell(6).getStringCellValue()));
				address = StringUtils.replace(address, Constants.UA, "");

				String offeringName = StringUtils.trimToEmpty((row.getCell(7).getStringCellValue()));
				offeringName = StringUtils.replace(offeringName, Constants.UA, "");

				validateAddressForIas(row, validatorBean, address, country, stateArg, cityArg, pincode, locality,
						isIndia(country), locationDetailList, offeringName);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		return CompletableFuture.completedFuture(true);
	}

	/**
	 * isIndia
	 * 
	 * @param country
	 * @return
	 */
	private boolean isIndia(String country) {
		Boolean isIndia = false;
		if (country.equalsIgnoreCase("India")) {
			isIndia = true;
		}
		return isIndia;
	}

	public void validateAddress(Row row, Set<LocationUploadValidationBean> validatorBean, String address,
			String country, String stateArg, String cityArg, String pincode, boolean isIndia,
			List<LocationOfferingDetail> locationDetailList, String offeringName) throws TclCommonException {

		try {
			LocationUploadValidationBean validatorBeanForThisRowAddress = new LocationUploadValidationBean();
			List<LocationValidationColumnBean> columnValidationBean = new ArrayList<>();

			validateExcelInput(address, country, stateArg, cityArg, pincode, validatorBeanForThisRowAddress,
					columnValidationBean, validatorBean, row, isIndia);

			constructLocationDetails(country, stateArg, cityArg, pincode, address, offeringName, locationDetailList);

			if (null != validatorBeanForThisRowAddress.getErrorMessage()
					&& !validatorBeanForThisRowAddress.getErrorMessage().isEmpty()) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(validatorBeanForThisRowAddress.getField());
				columnBean.setErrorMessage(validatorBeanForThisRowAddress.getErrorMessage());
				columnValidationBean.add(columnBean);
				validatorBean.add(validatorBeanForThisRowAddress);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public void validateAddressForIas(Row row, Set<LocationUploadValidationBean> validatorBean, String address,
			String country, String stateArg, String cityArg, String pincode, String locality, boolean isIndia,
			List<LocationOfferingDetail> locationDetailList, String offeringName) throws TclCommonException {

		try {
			LocationUploadValidationBean validatorBeanForThisRowAddress = new LocationUploadValidationBean();
			List<LocationValidationColumnBean> columnValidationBean = new ArrayList<>();

			validateExcelInputIas(address, country, stateArg, cityArg, pincode, locality,
					validatorBeanForThisRowAddress, columnValidationBean, validatorBean, row, isIndia);

			constructLocationDetailsForIas(country, stateArg, cityArg, pincode, address, offeringName, locality,
					locationDetailList, validatorBean);

			if (null != validatorBeanForThisRowAddress.getErrorMessage()
					&& !validatorBeanForThisRowAddress.getErrorMessage().isEmpty()) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(validatorBeanForThisRowAddress.getField());
				columnBean.setErrorMessage(validatorBeanForThisRowAddress.getErrorMessage());
				columnValidationBean.add(columnBean);
				validatorBean.add(validatorBeanForThisRowAddress);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * constructLocationDetails
	 * 
	 * @throws TclCommonException
	 */
	private void constructLocationDetails(String country, String stateArg, String cityArg, String pincode,
			String address, String offeringName, List<LocationOfferingDetail> locationDetailList)
			throws TclCommonException {
		GeocodeBean geoResponse = getGeoCodeDetails(country, stateArg, cityArg, pincode, address, null);

		if (!Objects.isNull(geoResponse)) {
			LocationOfferingDetail locationDetail = new LocationOfferingDetail();
			AddressDetail userAddress = new AddressDetail();
			userAddress.setCity(cityArg);
			userAddress.setPincode(pincode);
			if (geoResponse.getLocality() != null) {
				userAddress.setLocality(geoResponse.getLocality());
			} else {
				userAddress.setLocality(cityArg);
			}
			userAddress.setCountry(country);
			userAddress.setAddressLineOne(address);
			userAddress.setSource(LocationConstants.USER_SOURCE.toString());
			userAddress.setState(stateArg);
			userAddress.setLatLong(geoResponse.getLatLng());
			locationDetail.setUserAddress(userAddress);

			AddressDetail apiAddress = new AddressDetail();
			apiAddress.setCity(cityArg);
			apiAddress.setPincode(pincode);
			if (geoResponse.getLocality() != null) {
				apiAddress.setLocality(geoResponse.getLocality());

			}
			apiAddress.setCountry(country);
			apiAddress.setAddressLineOne(address);
			apiAddress.setState(stateArg);
			apiAddress.setSource(LocationConstants.API_SOURCE.toString());
			apiAddress.setLatLong(geoResponse.getLatLng());
			locationDetail.setApiAddress(apiAddress);
			locationDetail.setOfferingName(offeringName);
			locationDetailList.add(locationDetail);
		}
	}

	/**
	 * constructLocationDetails
	 * 
	 * @param locality
	 * @param validatorBean
	 * 
	 * @throws TclCommonException
	 */
	private void constructLocationDetailsForIas(String country, String stateArg, String cityArg, String pincode,
			String address, String offeringName, String locality, List<LocationOfferingDetail> locationDetailList,
			Set<LocationUploadValidationBean> validatorBean) throws TclCommonException {
		GeocodeBean geoResponse = getGeoCodeDetailsForIas(country, stateArg, cityArg, pincode, address, locality, null,
				validatorBean);

		if (!Objects.isNull(geoResponse)) {
			LocationOfferingDetail locationDetail = new LocationOfferingDetail();
			AddressDetail userAddress = new AddressDetail();
			userAddress.setCity(cityArg);
			userAddress.setPincode(pincode);
			userAddress.setLocality(locality);
			userAddress.setCountry(country);
			userAddress.setAddressLineOne(address);
			userAddress.setSource(LocationConstants.USER_SOURCE.toString());
			userAddress.setState(stateArg);
			userAddress.setLatLong(geoResponse.getLatLng());
			locationDetail.setUserAddress(userAddress);

			AddressDetail apiAddress = new AddressDetail();
			apiAddress.setCity(cityArg);
			apiAddress.setPincode(pincode);
			apiAddress.setLocality(locality);
			apiAddress.setCountry(country);
			apiAddress.setAddressLineOne(address);
			apiAddress.setState(stateArg);
			apiAddress.setSource(LocationConstants.API_SOURCE.toString());
			apiAddress.setLatLong(geoResponse.getLatLng());
			locationDetail.setApiAddress(apiAddress);
			locationDetail.setOfferingName(offeringName);
			locationDetailList.add(locationDetail);
		}
	}

	/**
	 * validateExcelInput
	 * 
	 * @param address
	 * @param country
	 * @param stateArg
	 * @param cityArg
	 * @param pincode
	 * @param validatorBeanForThisRowAddress
	 * @param columnValidationBean
	 * @param validatorBean
	 * @param row
	 * @param isIndia
	 */
	private void validateExcelInput(String address, String country, String stateArg, String cityArg, String pincode,
			LocationUploadValidationBean validatorBeanForThisRowAddress,
			List<LocationValidationColumnBean> columnValidationBean, Set<LocationUploadValidationBean> validatorBean,
			Row row, boolean isIndia) {

		if (null == country || country.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_COUNTRY);
			columnBean.setErrorMessage(LocationUploadConstants.COUNTRY_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		} else {

			MstCountry mstCountry = locationCacheService.findCountryByName(country);
			if (mstCountry == null) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_COUNTRY);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}
		}

		if (null == stateArg || stateArg.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_STATE);
			columnBean.setErrorMessage(LocationUploadConstants.STATE_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		} else {

			MstState mstState = locationCacheService.findStateByNameAndMstCountry(stateArg, country);

			if (mstState ==null ) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_STATE);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}

		if (null == cityArg || cityArg.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_CITY);
			columnBean.setErrorMessage(LocationUploadConstants.CITY_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);

		} else {

			MstCity mstCity = locationCacheService.findCityByNameAndMstState(cityArg, stateArg);

			if (mstCity==null) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_CITY);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}

		if (null == pincode || pincode.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_PINCODE);
			columnBean.setErrorMessage(LocationUploadConstants.PINCODE_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		} else if (isIndia) {

			List<MstPincode> mstPincodes = locationCacheService.findPinCode(pincode);
			if (mstPincodes.isEmpty()) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_PINCODE);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}

		if (null == address || address.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_ADDRESS);
			columnBean.setErrorMessage(LocationUploadConstants.ADDRESS_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		}

		if (!columnValidationBean.isEmpty()) {
			validatorBeanForThisRowAddress.setRowDetails(LocationUploadConstants.ROW + row.getRowNum());
			validatorBeanForThisRowAddress.setColumns(columnValidationBean);
			LocationUploadValidationBean validatrBean = new LocationUploadValidationBean();
			validatrBean.setRowDetails(LocationUploadConstants.ROW + row.getRowNum());
			validatrBean.setColumns(columnValidationBean);
			validatorBean.add(validatrBean);
		}

	}

	/**
	 * validateExcelInput
	 * 
	 * @param address
	 * @param country
	 * @param stateArg
	 * @param cityArg
	 * @param pincode
	 * @param locality
	 * @param validatorBeanForThisRowAddress
	 * @param columnValidationBean
	 * @param validatorBean
	 * @param row
	 * @param isIndia
	 */
	private void validateExcelInputIas(String address, String country, String stateArg, String cityArg, String pincode,
			String locality, LocationUploadValidationBean validatorBeanForThisRowAddress,
			List<LocationValidationColumnBean> columnValidationBean, Set<LocationUploadValidationBean> validatorBean,
			Row row, boolean isIndia) {

		if (null == country || country.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_COUNTRY);
			columnBean.setErrorMessage(LocationUploadConstants.COUNTRY_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		} else {

			MstCountry mstCountry = locationCacheService.findCountryByName(country);
			if (mstCountry == null) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_COUNTRY);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}
		}

		if (null == stateArg || stateArg.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_STATE);
			columnBean.setErrorMessage(LocationUploadConstants.STATE_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		} else {

			MstState mstState = locationCacheService.findStateByNameAndMstCountry(stateArg, country);

			if (mstState ==null ) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_STATE);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}

		if (null == cityArg || cityArg.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_CITY);
			columnBean.setErrorMessage(LocationUploadConstants.CITY_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);

		} else {

			MstCity mstCity = locationCacheService.findCityByNameAndMstState(cityArg, stateArg);

			if (mstCity==null) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_CITY);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}

		if (null == pincode || pincode.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_PINCODE);
			columnBean.setErrorMessage(LocationUploadConstants.PINCODE_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		} else if (isIndia) {

			List<MstPincode> mstPincodes = locationCacheService.findPinCode(pincode);
			if (mstPincodes.isEmpty()) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(LocationUploadConstants.HEADER_PINCODE);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}

		if (null == locality || locality.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_LOCALITY);
			columnBean.setErrorMessage(LocationUploadConstants.LOCALITY_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		}

		if (null == address || address.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(LocationUploadConstants.HEADER_ADDRESS);
			columnBean.setErrorMessage(LocationUploadConstants.ADDRESS_CANT_BE_EMPTY);
			columnValidationBean.add(columnBean);
		}

		if (!columnValidationBean.isEmpty()) {
			validatorBeanForThisRowAddress.setRowDetails(LocationUploadConstants.ROW + row.getRowNum());
			validatorBeanForThisRowAddress.setColumns(columnValidationBean);
			LocationUploadValidationBean validatrBean = new LocationUploadValidationBean();
			validatrBean.setRowDetails(LocationUploadConstants.ROW + row.getRowNum());
			validatrBean.setColumns(columnValidationBean);
			validatorBean.add(validatrBean);
		}

	}

	/**
	 * getGeoCodeDetails - this method calls google map api to get the latlng
	 * details of the given address.
	 * 
	 * @param address
	 * @return
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public GeocodeBean getGeoCodeDetails(String country, String stateArg, String cityArg, String pincode,
			String address, LocationUploadValidationBean validationBean) throws TclCommonException {
		GeocodeBean geoCodeBean = new GeocodeBean();
		String geocodeReponse = "";
		String googleurl = "";
		String locality = null;

		try {
			googleurl = StringUtils.join(googleMapAPI, LocationUploadConstants.GOOGLE_API_KEY,
					googleMapApiKey, LocationUploadConstants.ADDRESS, LocationUploadConstants.EQUALTO, country,
					CommonConstants.COMMA, pincode, CommonConstants.COMMA, address, CommonConstants.COMMA, stateArg,
					CommonConstants.COMMA, cityArg);

			RestResponse response = restClientService.get(googleurl);

			if (response.getStatus() == Status.SUCCESS) {
				geocodeReponse = response.getData();
			}

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(geocodeReponse);
			JSONObject jb = (JSONObject) obj;

			JSONArray jsonResultsObject = (JSONArray) jb.get(LocationUploadConstants.RESULTS);
			if (!jsonResultsObject.isEmpty()) {
				JSONObject jsonObject2 = (JSONObject) jsonResultsObject.get(0);
				JSONObject jsonObject3 = (JSONObject) jsonObject2.get(LocationUploadConstants.GEOMETRY);
				JSONObject location = (JSONObject) jsonObject3.get(LocationUploadConstants.LOCATION);

				String latLong = location.get(LocationUploadConstants.LAT) + LocationUploadConstants.COMMA
						+ location.get(LocationUploadConstants.LNG);
				if (StringUtils.isBlank(latLong)) {
					validationBean.setField(LocationUploadConstants.LAT_ERROR);
					validationBean.setErrorMessage(LocationUploadConstants.LAT_LONG_ERROR);
				}
				geoCodeBean.setLatLng(latLong);

				JSONArray addressComponents = (JSONArray) jsonObject2.get(LocationUploadConstants.ADDRESS_COMPONENTS);

				for (int outer = 0; outer < addressComponents.size(); outer++) {

					JSONObject addressComponentObject = (JSONObject) addressComponents.get(outer);
					JSONArray addressComponentsTypes = (JSONArray) addressComponentObject
							.get(LocationUploadConstants.TYPES);

					for (int inner = 0; inner < addressComponentsTypes.size(); inner++) {
						if (addressComponentsTypes.get(inner).toString()
								.equalsIgnoreCase(LocationUploadConstants.LOCALITY)) {
							locality = (String) addressComponentObject.get(LocationUploadConstants.LONG_NAME);
						}
					}
				}

				geoCodeBean.setLocality(locality);
			}

			else {
				if (Objects.isNull(validationBean)) {
					validationBean = new LocationUploadValidationBean();
				}

				if (jb.get(LocationUploadConstants.STATUS).toString()
						.equalsIgnoreCase(LocationUploadConstants.ZERO_RESULTS)) {

					validationBean.setField(LocationUploadConstants.HEADER_ADDRESS);
					validationBean.setErrorMessage(LocationUploadConstants.INVALID_ADDRESS);
				}

				if (!Objects.isNull((JSONArray) jb.get(LocationUploadConstants.ERROR_MSG))) {
					validationBean.setErrorMessage(LocationUploadConstants.TRY_AFTER_SOMETIME);
				}

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return geoCodeBean;
	}

	/**
	 * getGeoCodeDetails - this method calls google map api to get the latlng
	 * details of the given address.
	 * 
	 * @param address
	 * @return
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 * @param local
	 * @param validatorBean
	 * @param locationDetailList
	 */
	public GeocodeBean getGeoCodeDetailsForIas(String country, String stateArg, String cityArg, String pincode,
			String address, String local, LocationUploadValidationBean validationBean,
			Set<LocationUploadValidationBean> validatorBean) throws TclCommonException {
		GeocodeBean geoCodeBean = new GeocodeBean();
		String geocodeReponse = "";
		String googleurl = "";
		String locality = local;

		try {
			googleurl = StringUtils.join(googleMapAPI, LocationUploadConstants.GOOGLE_API_KEY,
					googleMapApiKey, LocationUploadConstants.ADDRESS, LocationUploadConstants.EQUALTO, country,
					CommonConstants.COMMA, pincode, CommonConstants.COMMA, address, CommonConstants.COMMA, stateArg,
					CommonConstants.COMMA, cityArg, CommonConstants.COMMA, locality);

			RestResponse response = restClientService.get(googleurl);
			JSONObject jb = null;
			if (response.getStatus() == Status.SUCCESS) {
				geocodeReponse = response.getData();
			}

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(geocodeReponse);
			jb = (JSONObject) obj;

			JSONArray jsonResultsObject = (JSONArray) jb.get(LocationUploadConstants.RESULTS);
			if (!jsonResultsObject.isEmpty()) {
				JSONObject jsonObject2 = (JSONObject) jsonResultsObject.get(0);
				JSONObject jsonObject3 = (JSONObject) jsonObject2.get(LocationUploadConstants.GEOMETRY);
				JSONObject location = (JSONObject) jsonObject3.get(LocationUploadConstants.LOCATION);

				String latLong = location.get(LocationUploadConstants.LAT) + LocationUploadConstants.COMMA
						+ location.get(LocationUploadConstants.LNG);
				if (StringUtils.isBlank(latLong)) {
					validationBean.setField(LocationUploadConstants.LAT_ERROR);
					validationBean.setErrorMessage(LocationUploadConstants.LAT_LONG_ERROR);
					validatorBean.add(validationBean);
				}
				geoCodeBean.setLatLng(latLong);

			}

			else {
				if (Objects.isNull(validationBean)) {
					validationBean = new LocationUploadValidationBean();
				}

				if (jb != null && jb.get(LocationUploadConstants.STATUS).toString()
						.equalsIgnoreCase(LocationUploadConstants.ZERO_RESULTS)) {

					validationBean.setField(LocationUploadConstants.HEADER_ADDRESS);
					validationBean.setErrorMessage(LocationUploadConstants.INVALID_ADDRESS);
					validatorBean.add(validationBean);
				}

				if (!Objects.isNull((JSONArray) jb.get(LocationUploadConstants.ERROR_MSG))) {
					validationBean.setErrorMessage(LocationUploadConstants.TRY_AFTER_SOMETIME);
				}

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return geoCodeBean;
	}

	/**
	 * 
	 * addAddress - This method will be used to add the location and return the
	 * locationId with offering name for multiple address excel upload
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	@Async
	public CompletableFuture<LocationMultipleResponse> addAddress(LocationOfferingDetail locationOfferingDetail)
			throws TclCommonException {
		LocationMultipleResponse loginResponse = new LocationMultipleResponse();
		try {
			if (locationOfferingDetail.getApiAddress() == null || locationOfferingDetail.getUserAddress() == null) {
				throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
			}
			String user = Utils.getSource();
			validateAddress(locationOfferingDetail.getUserAddress(), true);
			LocationDetail locationDetail = new LocationDetail();
			locationDetail.setApiAddress(locationOfferingDetail.getApiAddress());
			locationDetail.setUserAddress(locationOfferingDetail.getUserAddress());
			Integer userAddressId = persistAddressLocInternational(locationDetail.getUserAddress(),
					LocationConstants.USER_SOURCE.toString(), user);

			LOGGER.info("validate and addAddress for userAddressId={}", userAddressId);

			Integer apiAddressId = persistAddressLocInternational(locationDetail.getApiAddress(),
					LocationConstants.API_SOURCE.toString(), user);
			Location locationEntity = persistLocation(locationDetail, userAddressId, apiAddressId, null, null);
			loginResponse.setLocationId(locationEntity.getId());
			loginResponse.setOfferingName(locationOfferingDetail.getOfferingName());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return CompletableFuture.completedFuture(loginResponse);
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
				|| (StringUtils.isEmpty(address.getLocality()) && isUserAddress)
				|| (StringUtils.isEmpty(address.getLatLong()) && !isUserAddress)) {
			throw new TclCommonException(ExceptionConstants.LOCATION_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * persistAddressLoc -Save address
	 * 
	 * @param addressDetail
	 * @param source
	 * @param isUserAddress
	 * @return Integer
	 * @throws TclCommonException
	 */
	public Integer persistAddressLoc(AddressDetail addressDetail, String source, String user) {
		// Adding apiAddress
		MstCity mstCity = null;
		MstCountry mstCountry = null;
		MstState mstState = null;
		MstLocality mstLocality = null;
		MstPincode mstPincode = null;

		mstCountry = constructCountry(addressDetail.getCountry(), source, user);
		mstState = constructState(addressDetail.getState(), mstCountry, source, user);
		mstCity = constructCity(addressDetail.getCity(), mstState, source, user);
		mstPincode = constructPincode(addressDetail.getPincode(), mstCity, source, user);

		if (!StringUtils.isEmpty(addressDetail.getLocality())) {
			mstLocality = constructLocality(addressDetail.getLocality(), mstCity, source);
		}
		if (mstCity != null && mstCity.getName() != null) {
			MstAddress mstAddress = getMstAddress(mstCity.getName(), addressDetail, source, mstCountry.getName(),
					mstPincode.getCode(), mstLocality, mstState, user);
			mstAddressRepository.save(mstAddress);
			return mstAddress.getId();
		}
		return null;
	}

	/**
	 * getMstAddress
	 * 
	 * @param addressDetail
	 * @param id
	 * @param addressLineOne
	 * @param mstLocality
	 * @param mstState
	 * @param id2
	 * @param id3
	 * @return
	 */
	private MstAddress getMstAddress(String city, AddressDetail addressDetail, String source, String country,
			String pincode, MstLocality mstLocality, MstState mstState, String user) {

		List<MstAddress> msAddresses = mstAddressRepository
				.findByAddressLineOneAndCityAndCountryAndPincodeAndStateAndLocality(Utils.removeUnicode(addressDetail.getAddressLineOne()),
						city, country, pincode, mstState.getName(),
						(mstLocality != null ? mstLocality.getName() : null));
		MstAddress mstAddress = null;
		if (msAddresses == null || msAddresses.isEmpty()) {
			mstAddress = new MstAddress();
			mstAddress.setAddressLineOne(Utils.removeUnicode(addressDetail.getAddressLineOne()));
			mstAddress.setAddressLineTwo(Utils.removeUnicode(addressDetail.getAddressLineTwo()));
			mstAddress.setCity(city);
			mstAddress.setCountry(country);
			if (mstLocality != null) {
				mstAddress.setLocality(mstLocality.getName());
			}
			mstAddress.setPincode(pincode);
			mstAddress.setState(mstState.getName());
			mstAddress.setSource(source);
			mstAddress.setCreatedBy(user);
			mstAddress.setCreatedTime(new Timestamp(new Date().getTime()));
			mstAddress.setPlotBuilding(addressDetail.getPlotBuilding());
			mstAddressRepository.save(mstAddress);

		} else {
			mstAddress = msAddresses.get(0);

		}
		return mstAddress;
	}

	/**
	 * 
	 * constructCountry
	 * 
	 * @param country
	 * @param mstAddress
	 * @return MstCountry
	 * @throws TclCommonException
	 */
	private MstCountry constructCountry(String country, String source, String user) {
		MstCountry mstCountryEntity = locationCacheService.findCountryByName(country);
		if (mstCountryEntity == null) {

			mstCountryEntity = new MstCountry();
			mstCountryEntity.setName(country);
			mstCountryEntity.setSource(source);
			mstCountryEntity.setCreatedBy(user);
			mstCountryEntity.setCreatedTime(new Timestamp(new Date().getTime()));
			mstCountryRepository.save(mstCountryEntity);
		}
		return mstCountryEntity;
	}

	/**
	 * 
	 * constructState
	 * 
	 * @param state
	 * @param mstAddress
	 * @return MstState
	 * @throws TclCommonException
	 */
	private MstState constructState(String state, MstCountry mstCountry, String source, String user) {
		MstState mstStateEntity = locationCacheService.findStateByName(state);
		if (mstStateEntity == null) {
			mstStateEntity = new MstState();
			mstStateEntity.setName(state);
			mstStateEntity.setMstCountry(mstCountry);
			mstStateEntity.setSource(source);
			mstStateEntity.setCreatedBy(user);
			mstStateEntity.setCreatedTime(new Timestamp(new Date().getTime()));
			mstStateRepository.save(mstStateEntity);

		}
		return mstStateEntity;
	}

	/**
	 * 
	 * constructCity
	 * 
	 * @param city
	 * @param mstAddress
	 * @return MstCity
	 * @throws TclCommonException
	 */
	private MstCity constructCity(String city, MstState mstState, String source, String user) {
		MstCity mstCityEntity = locationCacheService.findCityByNameAndMstState(city, mstState.getName());
		if (mstCityEntity == null) {
			MstCity mstCity = new MstCity();
			mstCity.setMstState(mstState);
			mstCity.setName(city);
			mstCity.setSource(source);
			mstCity.setCreatedBy(user);
			mstCity.setCreatedTime(new Timestamp(new Date().getTime()));
			mstCityEntity=mstCityRepository.save(mstCity);
			
		}
		return mstCityEntity;
	}

	/**
	 * 
	 * constructState
	 * 
	 * @param state
	 * @param mstAddress
	 * @return MstState
	 * @throws TclCommonException
	 */
	private MstState constructStateBasedOnCountry(String state, MstCountry mstCountry, String source, String user) {
		MstState mstStateEntity = locationCacheService.findStateByNameAndMstCountry(state, mstCountry.getName());
		if (mstStateEntity == null) {
			mstStateEntity = new MstState();
			mstStateEntity.setName(state);
			mstStateEntity.setMstCountry(mstCountry);
			mstStateEntity.setSource(source);
			mstStateEntity.setCreatedBy(user);
			mstStateEntity.setCreatedTime(new Timestamp(new Date().getTime()));
			mstStateRepository.save(mstStateEntity);

		}
		return mstStateEntity;
	}

	/**
	 * 
	 * constructPincode
	 * 
	 * @param pincode
	 * @param mstAddress
	 * @return mstPincode
	 * @throws TclCommonException
	 */
	private MstPincode constructPincode(String pincode, MstCity mstCity, String source, String user) {
		MstPincode mstPincode = null;
		List<MstPincode> mstPincodeEntitys = locationCacheService.findPinCode(pincode);
		if (mstPincodeEntitys == null || mstPincodeEntitys.isEmpty()) {
			mstPincodeEntitys = new ArrayList<>();
			mstPincode = new MstPincode();
			mstPincode.setMstCity(mstCity);
			mstPincode.setCode(pincode);
			mstPincode.setSource(source);
			mstPincode.setCreatedBy(user);
			mstPincode.setCreatedTime(new Timestamp(new Date().getTime()));
			mstPincodeRespository.save(mstPincode);
			mstPincodeEntitys.add(mstPincode);
		} else {
			mstPincode = mstPincodeEntitys.get(0);
		}
		return mstPincode;
	}

	/**
	 * 
	 * constructLocality
	 * 
	 * @param locality
	 * @param mstAddress
	 * @param mstCity
	 * @param source
	 * @throws TclCommonException
	 */
	private MstLocality constructLocality(String locality, MstCity mstCity, String source) {

		List<MstLocality> mstLocalityEntitys = mstLocalityRepository.findByName(locality);
		MstLocality mstLocalityEntity = null;

		if (mstLocalityEntitys == null || mstLocalityEntitys.isEmpty()) {
			mstLocalityEntity = new MstLocality();
			mstLocalityEntity.setName(locality);
			mstLocalityEntity.setMstCity(mstCity);
			mstLocalityEntity.setSource(source);
			mstLocalityEntity.setCreatedTime(new Timestamp(new Date().getTime()));
			mstLocalityRepository.save(mstLocalityEntity);
		} else {
			mstLocalityEntity = mstLocalityEntitys.get(0);

		}

		return mstLocalityEntity;
	}

	/**
	 * persistLocation - Save location and customer location repo
	 * 
	 * @param locationDetail
	 * @param userAddressId
	 * @param apiAddressId
	 * @return Location
	 */
	public Location persistLocation(LocationDetail locationDetail, Integer userAddressId, Integer apiAddressId,
			Location locationEntity, CustomerLocation customerLocationEntity) {
		if (locationEntity == null)
			locationEntity = new Location();
		locationEntity.setApiAddressId(apiAddressId);
		locationEntity.setAddressId(userAddressId);
		locationEntity.setCreatedDate(new Timestamp(new Date().getTime()));
		locationEntity.setIsActive(CommonConstants.BACTIVE);
		locationEntity.setLatLong(locationDetail.getApiAddress().getLatLong());
		locationEntity.setIsVerified(0);
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
	 * persistAddressLoc -Save address for Gvpn
	 * 
	 * @param addressDetail
	 * @param source
	 * @param isUserAddress
	 * @return Integer
	 * @throws TclCommonException
	 */
	public Integer persistAddressLocInternational(AddressDetail addressDetail, String source, String user) {
		MstCity mstCity = null;
		MstCountry mstCountry = null;
		MstState mstState = null;
		MstLocality mstLocality = null;
		MstPincode mstPincode = null;

		mstCountry = constructCountry(addressDetail.getCountry(), source, user);

		if ((Objects.isNull(addressDetail.getState()) || StringUtils.isEmpty(addressDetail.getState()))
				&& addressDetail.getCountry().equalsIgnoreCase(LocationUploadConstants.SINGAPORE)) {
			mstState = constructStateBasedOnCountry(LocationUploadConstants.SINGAPORE, mstCountry, source, user);
		} else {
			mstState = constructStateBasedOnCountry(addressDetail.getState(), mstCountry, source, user);
		}

		mstCity = constructCity(addressDetail.getCity(), mstState, source, user);
		mstPincode = constructPincode(addressDetail.getPincode(), mstCity, source, user);

		if (!StringUtils.isEmpty(addressDetail.getLocality())) {
			mstLocality = constructLocality(addressDetail.getLocality(), mstCity, source);
		}
		if (mstCity != null && mstCity.getName() != null) {
			MstAddress mstAddress = getMstAddress(mstCity.getName(), addressDetail, source, mstCountry.getName(),
					mstPincode.getCode(), mstLocality, mstState, user);
			mstAddressRepository.save(mstAddress);
			return mstAddress.getId();
		}
		return null;
	}
	
	
	/**
	 * validateEachRowForNpl - this method validates the uploaded location details
	 * and captures if any validation issues row & column wise.
	 * 
	 * @param row
	 * @param validatorBean
	 * @throws TclCommonException
	 */
	@Async
	public CompletableFuture<Boolean> validateEachRowForNpl(Workbook workbook, Row row,
			Set<LocationUploadValidationBean> validatorBean, List<LocationOfferingDetailNPL> locationDetailList)
			throws TclCommonException {
		try {
			ValidateAddressRequest request = new ValidateAddressRequest();
			DataFormatter objDefaultFormat = new DataFormatter();
			FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator stateValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator countryValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			FormulaEvaluator cityValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
			if (row.getRowNum() > 0) {
				

				String offeringName = StringUtils.trimToEmpty((row.getCell(1).getStringCellValue()));
				offeringName = StringUtils.replace(offeringName, Constants.UA, "");				
				
				String countryA = "";
			
				countryA = StringUtils.replace(cellEvaluation(row, objDefaultFormat, countryValuator, 2), Constants.UA, "");

				String stateAArg = "";

				stateAArg = StringUtils.replace(cellEvaluation(row, objDefaultFormat, stateValuator, 3), Constants.UA, "");

				String cityAArg = "";

				cityAArg = StringUtils.replace(cellEvaluation(row, objDefaultFormat, cityValuator, 4), Constants.UA, "");

				String pincodeA = "";

				pincodeA = StringUtils.replace(cellEvaluation(row, objDefaultFormat, objFormulaEvaluator, 5), Constants.UA, "");

				String localityA = "";


				localityA = StringUtils.replace(cellEvaluation(row, objDefaultFormat, objFormulaEvaluator, 6), Constants.UA, "");

				String addressA = StringUtils.trimToEmpty((row.getCell(7).getStringCellValue()));
				addressA = StringUtils.replace(addressA, Constants.UA, "");
				
				String typeA = "";


				typeA = StringUtils.replace(cellEvaluation(row, objDefaultFormat, objFormulaEvaluator, 8), Constants.UA, "");
				
				
				String countryB = "";

				countryB = StringUtils.replace(cellEvaluation(row, objDefaultFormat, countryValuator, 9), Constants.UA, "");

				String stateBArg = "";

				stateBArg = StringUtils.replace(cellEvaluation(row, objDefaultFormat, stateValuator, 10), Constants.UA, "");

				String cityBArg = "";

				cityBArg = StringUtils.replace(cellEvaluation(row, objDefaultFormat, cityValuator, 11), Constants.UA, "");

				String pincodeB = "";

				pincodeB = StringUtils.replace(cellEvaluation(row, objDefaultFormat, objFormulaEvaluator, 12), Constants.UA, "");

				String localityB = "";


				localityB = StringUtils.replace(cellEvaluation(row, objDefaultFormat, objFormulaEvaluator, 13), Constants.UA, "");

				String addressB = StringUtils.trimToEmpty((row.getCell(14).getStringCellValue()));
				addressB = StringUtils.replace(addressB, Constants.UA, "");
				
				String typeB = "";


				typeB = StringUtils.replace(cellEvaluation(row, objDefaultFormat, objFormulaEvaluator, 15), Constants.UA, "");

				setValidateAddressRequest(request, offeringName, countryA, stateAArg, cityAArg, pincodeA, localityA,
						addressA, typeA, countryB, stateBArg, cityBArg, pincodeB, localityB, addressB, typeB);
				
				validateAddressForNpl(row, validatorBean, request, locationDetailList);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		return CompletableFuture.completedFuture(true);
	}

	private String cellEvaluation(Row row, DataFormatter objDefaultFormat, FormulaEvaluator countryValuator, int cellNumber) {
		String countryA;
		if (null == row.getCell(cellNumber)) {
			countryA = "";
		} else {
			countryValuator.evaluate(row.getCell(cellNumber));
			countryA = StringUtils
					.trimToEmpty((objDefaultFormat.formatCellValue(row.getCell(cellNumber), countryValuator)));
		}
		return countryA;
	}

	private void setValidateAddressRequest(ValidateAddressRequest request, String offeringName, String countryA,
			String stateAArg, String cityAArg, String pincodeA, String localityA, String addressA, String typeA,
			String countryB, String stateBArg, String cityBArg, String pincodeB, String localityB, String addressB,
			String typeB) {
		request.setAddressA(addressA);
		request.setAddressB(addressB);
		request.setCityAArg(cityAArg);
		request.setCityBArg(cityBArg);
		request.setCountryA(countryA);
		request.setCountryB(countryB);
		request.setIndiaA(isIndia(countryA));
		request.setIndiaB(isIndia(countryB));
		request.setLocalityA(localityA);
		request.setLocalityB(localityB);
		request.setOfferingName(offeringName);
		request.setPincodeA(pincodeA);
		request.setPincodeB(pincodeB);
		request.setStateAArg(stateAArg);
		request.setStateBArg(stateBArg);
		request.setTypeA(typeA);
		request.setTypeB(typeB);
	}
	
	
	/**
	 * This method is used to validate the addresses entered for site A and site B and construct the location details 
	 * @param row
	 * @param validatorBean
	 * @param validateAddressRequest
	 * @param locationDetailList
	 * @throws TclCommonException
	 */
	
	
	public void validateAddressForNpl(Row row, Set<LocationUploadValidationBean> validatorBean, ValidateAddressRequest validateAddressRequest, List<LocationOfferingDetailNPL> locationDetailList) throws TclCommonException {

		try {
			LocationUploadValidationBean validatorBeanForThisRowAddress = new LocationUploadValidationBean();
			List<LocationValidationColumnBean> columnValidationBean = new ArrayList<>();

			validateExcelInputNpl(validatorBeanForThisRowAddress, columnValidationBean, validatorBean, row, validateAddressRequest );

			constructLocationDetailsForNpl(locationDetailList, validatorBean, validateAddressRequest);
			
					

			if (null != validatorBeanForThisRowAddress.getErrorMessage()
					&& !validatorBeanForThisRowAddress.getErrorMessage().isEmpty()) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(validatorBeanForThisRowAddress.getField());
				columnBean.setErrorMessage(validatorBeanForThisRowAddress.getErrorMessage());
				columnValidationBean.add(columnBean);
				validatorBean.add(validatorBeanForThisRowAddress);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	private void validateExcelInputNpl( LocationUploadValidationBean validatorBeanForThisRowAddress,
			List<LocationValidationColumnBean> columnValidationBean, Set<LocationUploadValidationBean> validatorBean,
			Row row, ValidateAddressRequest validateAddressRequest ) {

		validateCountryColumn(columnValidationBean, validateAddressRequest.getCountryA(), LocationUploadConstants.HEADER_COUNTRY_A, LocationUploadConstants.COUNTRY_A_CANT_BE_EMPTY);
		

		
		validateCountryColumn(columnValidationBean, validateAddressRequest.getCountryB(), LocationUploadConstants.HEADER_COUNTRY_B, LocationUploadConstants.COUNTRY_B_CANT_BE_EMPTY);

		validateStateColumn(columnValidationBean, validateAddressRequest.getStateAArg(), LocationUploadConstants.HEADER_STATE_A, LocationUploadConstants.STATE_A_CANT_BE_EMPTY,validateAddressRequest.getCountryA() );
		

		
		validateStateColumn(columnValidationBean, validateAddressRequest.getStateBArg(), LocationUploadConstants.HEADER_STATE_B, LocationUploadConstants.STATE_B_CANT_BE_EMPTY, validateAddressRequest.getCountryB());

		validateCityColumn(columnValidationBean, validateAddressRequest.getCityAArg(), LocationUploadConstants.HEADER_CITY_A, LocationUploadConstants.CITY_A_CANT_BE_EMPTY, validateAddressRequest.getStateAArg());
		


		validateCityColumn(columnValidationBean,validateAddressRequest.getCityBArg(), LocationUploadConstants.HEADER_CITY_B, LocationUploadConstants.CITY_B_CANT_BE_EMPTY, validateAddressRequest.getStateBArg());
		
		validatePincodeColumn(columnValidationBean, validateAddressRequest.getPincodeA(), LocationUploadConstants.HEADER_PINCODE_A, LocationUploadConstants.PINCODE_A_CANT_BE_EMPTY, validateAddressRequest.isIndiaA());
		

		
		validatePincodeColumn(columnValidationBean, validateAddressRequest.getPincodeB(), LocationUploadConstants.HEADER_PINCODE_B, LocationUploadConstants.PINCODE_B_CANT_BE_EMPTY, validateAddressRequest.isIndiaB());

		validateLocalityColumn(columnValidationBean, validateAddressRequest.getLocalityA(), LocationUploadConstants.HEADER_LOCALITY_A, LocationUploadConstants.LOCALITY_A_CANT_BE_EMPTY );
		

		validateLocalityColumn(columnValidationBean, validateAddressRequest.getLocalityB(), LocationUploadConstants.HEADER_LOCALITY_B, LocationUploadConstants.LOCALITY_B_CANT_BE_EMPTY);


		validateLocalityColumn(columnValidationBean, validateAddressRequest.getAddressA(), LocationUploadConstants.HEADER_ADDRESS_A, LocationUploadConstants.ADDRESS_A_CANT_BE_EMPTY);
			

		validateLocalityColumn(columnValidationBean, validateAddressRequest.getAddressB(), LocationUploadConstants.HEADER_ADDRESS_B, LocationUploadConstants.ADDRESS_B_CANT_BE_EMPTY);
		

			
		validateLocalityColumn(columnValidationBean, validateAddressRequest.getTypeA(), LocationUploadConstants.HEADER_TYPE_A, LocationUploadConstants.TYPE_A_CANT_BE_EMPTY);
		
	
			
		validateLocalityColumn(columnValidationBean, validateAddressRequest.getTypeB(), LocationUploadConstants.HEADER_TYPE_B, LocationUploadConstants.TYPE_B_CANT_BE_EMPTY);

		if (!columnValidationBean.isEmpty()) {
			validatorBeanForThisRowAddress.setRowDetails(LocationUploadConstants.ROW + row.getRowNum());
			validatorBeanForThisRowAddress.setColumns(columnValidationBean);
			LocationUploadValidationBean validatrBean = new LocationUploadValidationBean();
			validatrBean.setRowDetails(LocationUploadConstants.ROW + row.getRowNum());
			validatrBean.setColumns(columnValidationBean);
			validatorBean.add(validatrBean);
		}

	}

	private void validateLocalityColumn(List<LocationValidationColumnBean> columnValidationBean,
			String column, String columnName, String errorMessage) {
		if (null == column || column.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(columnName);
			columnBean.setErrorMessage(errorMessage);
			columnValidationBean.add(columnBean);
		}
	}

	private void validatePincodeColumn(List<LocationValidationColumnBean> columnValidationBean,
			String column, String columName, String errorMessage, boolean isIndia) {
		if (null == column || column.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(columName);
			columnBean.setErrorMessage(errorMessage);
			columnValidationBean.add(columnBean);
		} else if (isIndia) {

			List<MstPincode> mstPincodes = locationCacheService.findPinCode(column);
			if (mstPincodes.isEmpty()) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(columName);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}
	}

	private void validateCityColumn(List<LocationValidationColumnBean> columnValidationBean,
			String column, String columnName, String errorMessage, String state) {
		if (null == column|| column.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(columnName);
			columnBean.setErrorMessage(errorMessage);
			columnValidationBean.add(columnBean);

		} else {

			MstCity mstCity = locationCacheService.findCityByNameAndMstState(column, state);

			if (mstCity==null) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(columnName);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}
	}

	private void validateStateColumn(List<LocationValidationColumnBean> columnValidationBean,
			String column, String columnName, String errorMessage, String country) {
		if (null == column || column.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(columnName);
			columnBean.setErrorMessage(errorMessage);
			columnValidationBean.add(columnBean);
		} else {

			MstState mstState = locationCacheService.findStateByNameAndMstCountry(column, country);

			if (mstState ==null ) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(columnName);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}

		}
	}

	private void validateCountryColumn(List<LocationValidationColumnBean> columnValidationBean,
			String validateColumn, String columnName, String errorMessage) {
		if (null == validateColumn || validateColumn.trim().isEmpty()) {
			LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
			columnBean.setColumnName(columnName);
			columnBean.setErrorMessage(errorMessage);
			columnValidationBean.add(columnBean);
		} else {

			MstCountry mstCountry = locationCacheService.findCountryByName(validateColumn);
			if (mstCountry == null) {
				LocationValidationColumnBean columnBean = new LocationValidationColumnBean();
				columnBean.setColumnName(columnName);
				columnBean.setErrorMessage(LocationUploadConstants.INVALID_INPUT);
				columnValidationBean.add(columnBean);

			}
		}
	}
	
	
	private void constructLocationDetailsForNpl( List<LocationOfferingDetailNPL> locationDetailList,
			Set<LocationUploadValidationBean> validatorBean, ValidateAddressRequest validateAddressRequest) throws TclCommonException {
		LocationOfferingDetailNPL locationDetail = new LocationOfferingDetailNPL();
		
		
		GeocodeBean geoResponseSiteA = getGeoCodeDetailsForNpl(validateAddressRequest.getCountryA(), validateAddressRequest.getStateAArg(), validateAddressRequest.getCityAArg(), validateAddressRequest.getPincodeA(), 
				validateAddressRequest.getAddressA(),validateAddressRequest.getLocalityA(), null,
				validatorBean);
		
		GeocodeBean geoResponseSiteB = getGeoCodeDetailsForNpl(validateAddressRequest.getCountryB(), validateAddressRequest.getStateBArg(), validateAddressRequest.getCityBArg(), validateAddressRequest.getPincodeB(), 
				validateAddressRequest.getAddressB(),validateAddressRequest.getLocalityB(), null,
				validatorBean);

		if (!Objects.isNull(geoResponseSiteA)) {
			
			AddressDetail userAddress = new AddressDetail();
			setAddress(userAddress, validateAddressRequest.getCityAArg(), validateAddressRequest.getPincodeA(), validateAddressRequest.getLocalityA(), validateAddressRequest.getCountryA(),
					validateAddressRequest.getAddressA(), LocationConstants.USER_SOURCE.toString(),validateAddressRequest.getStateAArg(), geoResponseSiteA.getLatLng());
			

			AddressDetail apiAddress = new AddressDetail();

			setAddress(apiAddress, validateAddressRequest.getCityAArg(), validateAddressRequest.getPincodeA(), validateAddressRequest.getLocalityA(), validateAddressRequest.getCountryA(),
					validateAddressRequest.getAddressA(), LocationConstants.API_SOURCE.toString(),validateAddressRequest.getStateAArg(), geoResponseSiteA.getLatLng());
			
			locationDetail.setUserAddressSiteA(userAddress);
			locationDetail.setApiAddressSiteA(apiAddress);
			locationDetail.setOfferingNameSiteA(validateAddressRequest.getOfferingName());
			locationDetail.setTypeSiteA(validateAddressRequest.getTypeA());
			
		} 
		
		
		if(!Objects.isNull(geoResponseSiteB)) {
			AddressDetail userAddressSiteB = new AddressDetail();

			setAddress(userAddressSiteB, validateAddressRequest.getCityBArg(), validateAddressRequest.getPincodeB(), validateAddressRequest.getLocalityB(), validateAddressRequest.getCountryB(),
					validateAddressRequest.getAddressB(), LocationConstants.USER_SOURCE.toString(),validateAddressRequest.getStateBArg(), geoResponseSiteB.getLatLng());
			
			locationDetail.setUserAddressSiteB(userAddressSiteB);

			AddressDetail apiAddressSiteB = new AddressDetail();

			
			setAddress(apiAddressSiteB, validateAddressRequest.getCityBArg(), validateAddressRequest.getPincodeB(), validateAddressRequest.getLocalityB(), validateAddressRequest.getCountryB(),
					validateAddressRequest.getAddressB(), LocationConstants.API_SOURCE.toString(),validateAddressRequest.getStateBArg(), geoResponseSiteB.getLatLng());
			
			
			locationDetail.setApiAddressSiteB(apiAddressSiteB);
			locationDetail.setOfferingNameSiteB(validateAddressRequest.getOfferingName());
			locationDetail.setTypeSiteB(validateAddressRequest.getTypeB());
			
		}
		
		locationDetailList.add(locationDetail);
	}

	private void setAddress(AddressDetail userAddress, String city, String pinCode, String locality, String country, String address, String source, String state, String latLong) {
		userAddress.setCity(city);
		userAddress.setPincode(pinCode);
		userAddress.setLocality(locality);
		userAddress.setCountry(country);
		userAddress.setAddressLineOne(address);
		userAddress.setSource(source);
		userAddress.setState(state);
		userAddress.setLatLong(latLong);
	}
	
	/**
	 * getGeoCodeDetails - this method calls google map api to get the latlng
	 * details of the given address.
	 * 
	 * @param address
	 * @return
	 * @throws TclCommonException
	 * @param local
	 * @param validatorBean
	 * @param locationDetailList
	 */
	public GeocodeBean getGeoCodeDetailsForNpl(String country, String stateArg, String cityArg, String pincode,
			String address, String local, LocationUploadValidationBean validationBean,
			Set<LocationUploadValidationBean> validatorBean) throws TclCommonException {
		GeocodeBean geoCodeBean = new GeocodeBean();
		String geocodeReponse = "";
		String googleurl = "";
		String locality = local;

		try {
			googleurl = StringUtils.join(googleMapAPI, LocationUploadConstants.GOOGLE_API_KEY,
					googleMapApiKey, LocationUploadConstants.ADDRESS, LocationUploadConstants.EQUALTO, country,
					CommonConstants.COMMA, pincode, CommonConstants.COMMA, address, CommonConstants.COMMA, stateArg,
					CommonConstants.COMMA, cityArg, CommonConstants.COMMA, locality);

			RestResponse response = restClientService.get(googleurl);
			JSONObject jb = null;
			if (response.getStatus() == Status.SUCCESS) {
				geocodeReponse = response.getData();
			}

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(geocodeReponse);
			jb = (JSONObject) obj;

			JSONArray jsonResultsObject = (JSONArray) jb.get(LocationUploadConstants.RESULTS);
			if (!jsonResultsObject.isEmpty()) {
				JSONObject jsonObject2 = (JSONObject) jsonResultsObject.get(0);
				JSONObject jsonObject3 = (JSONObject) jsonObject2.get(LocationUploadConstants.GEOMETRY);
				JSONObject location = (JSONObject) jsonObject3.get(LocationUploadConstants.LOCATION);

				String latLong = location.get(LocationUploadConstants.LAT) + LocationUploadConstants.COMMA
						+ location.get(LocationUploadConstants.LNG);
				if (StringUtils.isBlank(latLong)) {
					validationBean.setField(LocationUploadConstants.LAT_ERROR);
					validationBean.setErrorMessage(LocationUploadConstants.LAT_LONG_ERROR);
					validatorBean.add(validationBean);
				}
				geoCodeBean.setLatLng(latLong);

			}

			else {
				if (Objects.isNull(validationBean)) {
					validationBean = new LocationUploadValidationBean();
				}

				if (jb != null && jb.get(LocationUploadConstants.STATUS).toString()
						.equalsIgnoreCase(LocationUploadConstants.ZERO_RESULTS)) {

					validationBean.setField(LocationUploadConstants.HEADER_ADDRESS);
					validationBean.setErrorMessage(LocationUploadConstants.INVALID_ADDRESS);
					validatorBean.add(validationBean);
				}

				if (!Objects.isNull((JSONArray) jb.get(LocationUploadConstants.ERROR_MSG))) {
					validationBean.setErrorMessage(LocationUploadConstants.TRY_AFTER_SOMETIME);
				}

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return geoCodeBean;
	}
	
	
	/**
	 * 
	 * addAddress - This method will be used to add the location and return the
	 * locationId with offering name for multiple address excel upload
	 * 
	 * @param locationDetail
	 * @return LocationResponse
	 * @throws TclCommonException
	 * @author ANNE NISHA 
	 */
	@Async
	public CompletableFuture<BulkUploadNplResponse> addAddressNPL(LocationOfferingDetailNPL locationOfferingDetail)
			throws TclCommonException {
		BulkUploadNplResponse loginResponse = new BulkUploadNplResponse();
		try {
			
			String user = Utils.getSource();
			

			Location locationEntity = persistSiteLocation(user, locationOfferingDetail.getApiAddressSiteA(), locationOfferingDetail.getUserAddressSiteA());
			
			Location locationEntitySiteB = persistSiteLocation(user, locationOfferingDetail.getApiAddressSiteB(), locationOfferingDetail.getUserAddressSiteB());
			
			LocationBean locationBeanSiteB = new LocationBean();
			LocationBean locationBeanSiteA = new LocationBean();
			
			locationBeanSiteB.setLocationId(locationEntitySiteB.getId());
			locationBeanSiteA.setLocationId(locationEntity.getId());
			List<LocationBean> locationBeanListSiteA = new ArrayList<>();
			locationBeanListSiteA.add(locationBeanSiteA);
			
			List<LocationBean> locationBeanListSiteB = new ArrayList<>();
			locationBeanListSiteB.add(locationBeanSiteB);
			
			SiteDetailBean siteDetailBeanSiteA = new SiteDetailBean();
			List<SiteDetailBean> siteDetailBeanList = new ArrayList<>();
			siteDetailBeanSiteA.setSite(locationBeanListSiteA);
			siteDetailBeanSiteA.setOfferingName(locationOfferingDetail.getOfferingNameSiteA());
			siteDetailBeanSiteA.setType(locationOfferingDetail.getTypeSiteA());
			siteDetailBeanList.add(siteDetailBeanSiteA);
			
			SiteDetailBean siteDetailBeanSiteB = new SiteDetailBean();
			siteDetailBeanSiteB.setOfferingName(locationOfferingDetail.getOfferingNameSiteB());
			siteDetailBeanSiteB.setType(locationOfferingDetail.getTypeSiteB());
			siteDetailBeanSiteB.setSite(locationBeanListSiteB);
			
			siteDetailBeanList.add(siteDetailBeanSiteB);
			loginResponse.setLink(siteDetailBeanList);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return CompletableFuture.completedFuture(loginResponse);
	}

	private Location persistSiteLocation(String user, AddressDetail apiAddressSite, AddressDetail userAddressSite )
			throws TclCommonException {
		if (apiAddressSite == null || userAddressSite == null) {
			throw new TclCommonException(ExceptionConstants.LOCATION_ADD_ERROR, ResponseResource.R_CODE_ERROR);
		}
		validateAddress(userAddressSite, true);
		LocationDetail locationDetail = new LocationDetail();
		locationDetail.setApiAddress(apiAddressSite);
		locationDetail.setUserAddress(userAddressSite);
		Integer userAddressId = persistAddressLocInternational(locationDetail.getUserAddress(),
				LocationConstants.USER_SOURCE.toString(), user);

		LOGGER.info("validate and addAddress for userAddressId={}", userAddressId);

		Integer apiAddressId = persistAddressLocInternational(locationDetail.getApiAddress(),
				LocationConstants.API_SOURCE.toString(), user);
		return persistLocation(locationDetail, userAddressId, apiAddressId, null, null);
	}

	/**
	 * Method to get geo code details using google API.
	 * @param pincode
	 * @return
	 * @throws TclCommonException
	 */
	public GeocodeBean getGeoCodeDetails(String country, String pincode) throws TclCommonException, ParseException {
		String geocodeResponse = "";
		GeocodeBean geocodeBean = null;
		try {
			String googleurl = StringUtils.join(googleMapAPI, "?key=", googleMapApiKey, "&components=country:",country,"|postal_code:",
					pincode);

			LOGGER.info("The request URL is :: {}",googleurl);

			RestResponse response = restClientService.get(googleurl);
			if (response.getStatus() == Status.SUCCESS) {
				geocodeResponse = response.getData();
			}
			geocodeBean = parseResults(geocodeResponse);
			geocodeBean.setPincode(pincode);

			if(Objects.isNull(geocodeBean.getState())
					|| Objects.isNull(geocodeBean.getCity())){
				googleurl = StringUtils.join(googleMapAPI, "?key=", googleMapApiKey, "&latlng=",
						geocodeBean.getLatLng());

				LOGGER.info("The request URL is :: {}",googleurl);

				RestResponse apiResponse = restClientService.get(googleurl);
				if (response.getStatus() == Status.SUCCESS) {
					geocodeResponse = apiResponse.getData();
				}
				geocodeBean = parseResults(geocodeResponse);
				geocodeBean.setPincode(pincode);
			}
		} catch (Exception ex) {
			geocodeBean.setErrorMessage("ZERO_RESULTS");
		}
		return geocodeBean;
	}

	/**
	 * Method to parse response from google API.
	 * @param geocodeReponse
	 * @return
	 * @throws ParseException
	 */
	private GeocodeBean parseResults(String geocodeReponse) throws ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(geocodeReponse);
		JSONObject jb = (JSONObject) obj;

		GeocodeBean geocodeBean = new GeocodeBean();
		String latLong = null;

		JSONArray results = (JSONArray) jb.get(LocationUploadConstants.RESULTS);
		if (!results.isEmpty()) {
			JSONObject jsonObject2 = (JSONObject) results.get(0);

			JSONArray addressComponents = (JSONArray) jsonObject2.get(LocationUploadConstants.ADDRESS_COMPONENTS);
			geocodeBean.setFormattedAddr(String.valueOf(jsonObject2.get(LocationUploadConstants.FORMATTED_ADDRESS)));

			for (int outer = 0; outer < addressComponents.size(); outer++) {

				JSONObject addressComponentObject = (JSONObject) addressComponents.get(outer);
				JSONArray addressComponentsTypes = (JSONArray) addressComponentObject
						.get(LocationUploadConstants.TYPES);

				for (int inner = 0; inner < addressComponentsTypes.size(); inner++) {
					if (addressComponentsTypes.get(inner).toString().equalsIgnoreCase(LocationUploadConstants.LOCALITY)) {
						geocodeBean.setCity(String.valueOf(addressComponentObject.get(LocationUploadConstants.LONG_NAME)));
					}else if (addressComponentsTypes.get(inner).toString().equalsIgnoreCase(LocationUploadConstants.ADMIN_AREA_LEVEL_1)) {
						geocodeBean.setState(String.valueOf(addressComponentObject.get(LocationUploadConstants.LONG_NAME)));
					}
				}
			}
			JSONObject jsonObject3 = (JSONObject) jsonObject2.get(LocationUploadConstants.GEOMETRY);
			JSONObject location = (JSONObject) jsonObject3.get(LocationUploadConstants.LOCATION);

			latLong = location.get("lat") + "," + location.get("lng");
			geocodeBean.setLatLng(latLong);
		}
		return geocodeBean;
	}

}
