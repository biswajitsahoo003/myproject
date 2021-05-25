package com.tcl.dias.location.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.location.beans.BulkLocation;
import com.tcl.dias.location.beans.LocationUploadValidationBean;
import com.tcl.dias.location.beans.LocationValidationColumnBean;
import com.tcl.dias.location.constants.ExceptionConstants;
import com.tcl.dias.location.constants.LocationUploadConstants;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstPincode;
import com.tcl.dias.location.entity.entities.MstState;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstCountryRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstStateRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ExcelUtils.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class LocationExcelUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationExcelUtils.class);

	@Autowired
	MstCountryRepository mstCountryRepository;

	@Autowired
	MstStateRepository mstStateRepository;

	@Autowired
	MstCityRepository mstCityRepository;

	@Autowired
	MstPincodeRespository mstPincodeRespository;

	/**
	 * 
	 * extractBulkLocation
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public List<BulkLocation> extractBulkLocation(MultipartFile file,
			Set<LocationUploadValidationBean> validationErrorResponseBeans) throws TclCommonException {
		List<BulkLocation> bulkLocations = new ArrayList<>();
		Map<String, Boolean> stateMapper = new HashMap<>();
		Map<String, Boolean> countryMapper = new HashMap<>();
		Map<String, Boolean> cityMapper = new HashMap<>();
		Map<String, Boolean> pincodeMapper = new HashMap<>();
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8")) {
			Sheet sheet = workbook.getSheetAt(0);// Setting the page to default
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue; // just skip the rows if row number is 0
				}
				LocationUploadValidationBean locationValidation = new LocationUploadValidationBean();
				List<LocationValidationColumnBean> columnValidationBeanList = new ArrayList<>();
				locationValidation.setColumns(columnValidationBeanList);
				locationValidation.setRowDetails(LocationUploadConstants.ROW + row.getRowNum());
				BulkLocation bulkLocation = new BulkLocation();
				processCells(row, columnValidationBeanList, stateMapper, cityMapper, countryMapper, pincodeMapper, bulkLocation);
				if (!(StringUtils.isEmpty(bulkLocation.getAddress()) && StringUtils.isEmpty(bulkLocation.getCity())
						&& StringUtils.isEmpty(bulkLocation.getCountry())
						&& StringUtils.isEmpty(bulkLocation.getPincode())
						&& StringUtils.isEmpty(bulkLocation.getProfiles())
						&& StringUtils.isEmpty(bulkLocation.getState()))) {
					bulkLocations.add(bulkLocation);
					if (!columnValidationBeanList.isEmpty()) {
						validationErrorResponseBeans.add(locationValidation);
					}
				}
			}

		} catch (Exception e) {
			LOGGER.warn("Error in getting details from Excel ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return bulkLocations;
	}
	
	public void processCells(Row row,List<LocationValidationColumnBean> columnValidationBeanList,Map<String, Boolean> stateMapper,Map<String, Boolean> cityMapper,Map<String, Boolean> countryMapper,Map<String, Boolean> pincodeMapper,BulkLocation bulkLocation) {
		int cellCount = 0;
		for (Cell cell : row) {
			cell.setCellType(CellType.STRING);
			String value = cell.getStringCellValue();
			if (value != null) {
				value = value.trim();
				value = StringUtils.replace(value, "\u00A0", "");
			}
			switch (cellCount) {
			case 0:
				if (StringUtils.isEmpty(value)) {
					validateColumValue(columnValidationBeanList, value,
							LocationUploadConstants.HEADER_SERIAL_NO, LocationUploadConstants.SR_CANT_BE_EMPTY);
				}
				break;
			case 1:
				processCountry(countryMapper, columnValidationBeanList, bulkLocation, value);
				break;
			case 2:
				processState(stateMapper, columnValidationBeanList, bulkLocation, value);
				break;
			case 3:
				processCity(cityMapper, columnValidationBeanList, bulkLocation, value);
				break;
			case 4:
				processPincode(pincodeMapper, columnValidationBeanList, bulkLocation, value);
				break;
			case 5:
				processAddress(columnValidationBeanList, bulkLocation, value);
				break;
			case 6:
				processProfiles(columnValidationBeanList, bulkLocation, value);
				break;
			default:
				break;
			}
			cellCount++;
		}
	}

	/**
	 * processProfiles
	 * 
	 * @param columnValidationBeanList
	 * @param bulkLocation
	 * @param value
	 */
	private void processProfiles(List<LocationValidationColumnBean> columnValidationBeanList, BulkLocation bulkLocation,
			String value) {
		if (StringUtils.isBlank(value)) {
			validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_PROFILES,
					LocationUploadConstants.PROFILE_CANT_BE_EMPTY);
		} else {
			LOGGER.info("Profiles {}" , value);
			bulkLocation.setProfiles(value);
		}
	}

	/**
	 * processAddress
	 * 
	 * @param columnValidationBeanList
	 * @param bulkLocation
	 * @param value
	 */
	private void processAddress(List<LocationValidationColumnBean> columnValidationBeanList, BulkLocation bulkLocation,
			String value) {
		if (StringUtils.isBlank(value)) {
			validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_ADDRESS,
					LocationUploadConstants.ADDRESS_CANT_BE_EMPTY);
		} else {
			bulkLocation.setAddress(value);
		}
	}

	/**
	 * processPincode
	 * 
	 * @param pincodeMapper
	 * @param columnValidationBeanList
	 * @param bulkLocation
	 * @param value
	 */
	private void processPincode(Map<String, Boolean> pincodeMapper,
			List<LocationValidationColumnBean> columnValidationBeanList, BulkLocation bulkLocation, String value) {
		if (StringUtils.isBlank(value)) {
			validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_PINCODE,
					LocationUploadConstants.PINCODE_CANT_BE_EMPTY);
		} else {
			LOGGER.info("Pincode {}" , value);
			if (pincodeMapper.get(value) == null) {
				List<MstPincode> mstPincodes = mstPincodeRespository.findByCode(value);
				if (mstPincodes.isEmpty()) {
					pincodeMapper.put(value, false);
					validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_PINCODE,
							LocationUploadConstants.INVALID_INPUT);
				} else {
					pincodeMapper.put(value, true);
				}
			} else if (!pincodeMapper.get(value)) {
				validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_PINCODE,
						LocationUploadConstants.INVALID_INPUT);
			}
			bulkLocation.setPincode(value);
		}
	}

	/**
	 * processCity
	 * 
	 * @param cityMapper
	 * @param columnValidationBeanList
	 * @param bulkLocation
	 * @param value
	 */
	private void processCity(Map<String, Boolean> cityMapper,
			List<LocationValidationColumnBean> columnValidationBeanList, BulkLocation bulkLocation, String value) {
		if (StringUtils.isBlank(value)) {
			validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_CITY,
					LocationUploadConstants.CITY_CANT_BE_EMPTY);
		} else {
			if (cityMapper.get(value) == null) {
				List<MstCity> cityList = mstCityRepository.findByNameAndMstState_Name(value, bulkLocation.getState());
				if (cityList.isEmpty()) {
					cityMapper.put(value, false);
					validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_CITY,
							LocationUploadConstants.INVALID_INPUT);
				} else {
					cityMapper.put(value, true);
				}
			} else if (!cityMapper.get(value)) {
				validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_CITY,
						LocationUploadConstants.INVALID_INPUT);
			}
			bulkLocation.setCity(value);
		}
	}

	/**
	 * processState
	 * 
	 * @param stateMapper
	 * @param columnValidationBeanList
	 * @param bulkLocation
	 * @param value
	 */
	private void processState(Map<String, Boolean> stateMapper,
			List<LocationValidationColumnBean> columnValidationBeanList, BulkLocation bulkLocation, String value) {
		if (StringUtils.isBlank(value)) {
			validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_STATE,
					LocationUploadConstants.STATE_CANT_BE_EMPTY);
		} else {
			if (stateMapper.get(value) == null) {
				MstState mstState = mstStateRepository.findByName(value);
				if (mstState == null) {
					stateMapper.put(value, false);
					validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_STATE,
							LocationUploadConstants.INVALID_INPUT);
				} else {
					stateMapper.put(value, true);
				}
			} else if (!stateMapper.get(value)) {
				validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_STATE,
						LocationUploadConstants.INVALID_INPUT);
			}
			bulkLocation.setState(value);
		}
	}

	/**
	 * processCountry
	 * 
	 * @param countryMapper
	 * @param columnValidationBeanList
	 * @param bulkLocation
	 * @param value
	 */
	private void processCountry(Map<String, Boolean> countryMapper,
			List<LocationValidationColumnBean> columnValidationBeanList, BulkLocation bulkLocation, String value) {
		if (StringUtils.isBlank(value)) {
			validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_COUNTRY,
					LocationUploadConstants.COUNTRY_CANT_BE_EMPTY);
		} else {
			LOGGER.info("Country {}" , value);
			if (countryMapper.get(value) == null) {
				MstCountry mstCountry = mstCountryRepository.findByName(value);
				if (mstCountry == null) {
					countryMapper.put(value, false);
					validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_COUNTRY,
							LocationUploadConstants.INVALID_INPUT);
				} else {
					countryMapper.put(value, true);
				}
			} else if (!countryMapper.get(value)) {
				validateColumValue(columnValidationBeanList, value, LocationUploadConstants.HEADER_COUNTRY,
						LocationUploadConstants.INVALID_INPUT);
			}
			bulkLocation.setCountry(value);
		}
	}

	/**
	 * validateColumValue
	 * 
	 * @param columnValidationBeanList
	 * @param value
	 */
	private void validateColumValue(List<LocationValidationColumnBean> columnValidationBeanList, String value,
			String columnName, String errorMessage) {
		LocationValidationColumnBean columnBeanSR = new LocationValidationColumnBean();
		columnBeanSR.setColumnName(columnName);
		columnBeanSR.setErrorMessage(errorMessage);
		columnValidationBeanList.add(columnBeanSR);
		LOGGER.warn("Error {} :: {} :: {}", value, columnName, errorMessage);
	}

}
