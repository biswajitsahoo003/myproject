package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.networkaugment.entity.entities.ScComponentAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScOrderAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceAttribute;
import com.tcl.dias.networkaugment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.networkaugment.entity.repository.ScOrderAttributeRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * This file contains the CommonFulfillmentUtils.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component
public class CommonFulfillmentUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonFulfillmentUtils.class);

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	/**
	 * 
	 * getComponentAttributes - get the component Attr Map
	 * 
	 * @param scServiceDetailId
	 * @param componentName
	 * @param siteType
	 * @return {@linkplain Map}
	 */
	public Map<String, String> getComponentAttributes(Integer scServiceDetailId, String componentName,
			String siteType) {
		LOGGER.info("Entering getComponentAttributes with scServiceId : {} componentName : {} siteType : {}",
				scServiceDetailId, componentName, siteType);
		List<ScComponentAttribute> scComponentAttrs = scComponentAttributesRepository
				.findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetailId, componentName, siteType);
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttrs) {
			scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
		}
		LOGGER.info("getComponentAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	
	
	public Map<String, String> getServiceAttributesAttributes(List<ScServiceAttribute> scComponentAttributes) {

		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScServiceAttribute scComponentAttribute : scComponentAttributes) {
			scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
		}
		LOGGER.info("getServiceAttributesAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	/**
	 * 
	 * getComponentAttributes - get the component Attr Map
	 * 
	 * @param scServiceDetailId
	 * @param componentName
	 * @param siteType
	 * @return {@linkplain Map}
	 */
	public Map<String, String> getComponentAttributesDetails(List<String> attributeValue,Integer scServiceDetailId, String componentName,
			String siteType) {

		List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
				.findByScServiceDetailIdAndAttributeNameInAndScComponent_componentNameAndScComponent_siteType(
						scServiceDetailId, attributeValue, componentName, siteType);

		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttributes) {
			scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
		}
		LOGGER.info("getComponentAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	public Map<String, String> getScOrderAttributes(Integer scOrderId) {
		LOGGER.info("Entering getComponentAttributes with scOrderId : {}",
				scOrderId);
		List<ScOrderAttribute> scOrderAttributes = scOrderAttributeRepository.findByScOrder_IdAndIsActive(scOrderId, CommonConstants.Y);
		Map<String, String> scOrderAttributesMap = new HashMap<>();
		for (ScOrderAttribute scOrderAttribute : scOrderAttributes) {
			scOrderAttributesMap.put(scOrderAttribute.getAttributeName(),
					scOrderAttribute.getAttributeValue());
		}
		LOGGER.info("getComponentAttributes response : {}", scOrderAttributesMap);
		return scOrderAttributesMap;

	}
	
	/**
	 * 
	 * This function return optimus order sub category and corresponding Sap Service type 
	 * @author AnandhiV
	 * @return
	 */
	public Map<String,String> orderSubcategorySapData(){
		Map<String,String> subCategoryMap = new HashMap<>();
		subCategoryMap.put("NEW", "NEW");
		subCategoryMap.put("LM Shifting / BSO Change", "NEW");
		subCategoryMap.put("LM Shifting", "SHIFTING LOCATION");
		subCategoryMap.put("Parallel Upgrade", "NEW");
		subCategoryMap.put("Parallel Downgrade", "NEW");
		subCategoryMap.put("Hot Upgrade", "UPGRADE");
		subCategoryMap.put("Hot Downgrade", "DOWNGRADE");
		subCategoryMap.put("Addition of Sites", "NEW");
		subCategoryMap.put("Hot Upgrade-BSO Change", "NEW");
		subCategoryMap.put("Hot Downgrade-BSO Change", "NEW");
		subCategoryMap.put("LM Shifting-BSO Change", "NEW");
		subCategoryMap.put("Hot Upgrade-LM Shifting", "SHIFTING LOCATION");
		subCategoryMap.put("Hot Downgrade-LM Shifting", "SHIFTING LOCATION");
		subCategoryMap.put("Parallel Shifting", "NEW");
		
		return subCategoryMap;
		
	}
	
	/**
	 * Optimus - Sap Interface mapping information
	 * @author AnandhiV
	 * 
	 * @return
	 */
	
	/**
	 * Method to subtract date by 7 days for crfs
	 */
	public String getPreviousDate(String date)throws TclCommonException
			{
				String result=date;

				LOGGER.info("input date"+date);
		try {
			// Create a date formatter using your format string
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			// Parse the given date string into a Date object.
			// Note: This can throw a ParseException.
			Date myDate = dateFormat.parse(date);

			// Use the Calendar class to subtract one day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(myDate);
			calendar.add(Calendar.DAY_OF_YEAR, -7);

			// Use the date formatter to produce a formatted date string
			Date previousDate = calendar.getTime();
			 result = dateFormat.format(previousDate);
			 LOGGER.info("result date"+result);
		}
		catch(ParseException e)
		{
			LOGGER.info("ParseException"+ e.getCause());
			throw new TclCommonException(e.getMessage(), ResponseResource.R_CODE_ERROR);
		}
		return result;
	}

	/**
	 * Method to parse the date based on given format
	 * @param date
	 * @param formatter
	 * @return
	 * @throws TclCommonException
	 */
	public Date getDate(String date,SimpleDateFormat formatter)throws TclCommonException
	{
		Date result=null;
		try {
			result=formatter.parse(date);
		}
		catch(ParseException e)
			{
				LOGGER.info("ParseException"+ e.getCause());
				throw new TclCommonException(e.getMessage(), ResponseResource.R_CODE_ERROR);
			}
		return result;

	}

}
