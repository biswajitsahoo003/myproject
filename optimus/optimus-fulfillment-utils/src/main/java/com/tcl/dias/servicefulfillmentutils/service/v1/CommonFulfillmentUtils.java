package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.SapInterfaceMapping;
import com.tcl.dias.servicefulfillment.entity.entities.SapVendorBuyerMapping;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.SapInterfaceMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.SapVendorBuyerMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
	ScComponentRepository scComponentRepository;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	SapInterfaceMappingRepository sapInterfaceMappingRepository;
	
	@Autowired
	SapVendorBuyerMappingRepository sapVendorBuyerMappingRepository;
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	GscFlowGroupRepository gscFlowGroupRepository;

	@Autowired
	FlowGroupAttributeRepository flowGroupAttributeRepository;

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
		LOGGER.info("1 getComponentAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	
	/**
	 * 
	 * getComponentAndAdditionalAttributes - get the component Attr Map
	 * 
	 * @param scServiceDetailId
	 * @param componentName
	 * @param siteType
	 * @return {@linkplain Map}
	 */
	public Map<String, String> getComponentAndAdditionalAttributes(Integer scServiceDetailId, String componentName,
			String siteType) {
		LOGGER.info("Entering getComponentAndAdditionalAttributes with scServiceId : {} componentName : {} siteType : {}",
				scServiceDetailId, componentName, siteType);
		List<ScComponentAttribute> scComponentAttrs = scComponentAttributesRepository
				.findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetailId, componentName, siteType);
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttrs) {
			if("Y".equalsIgnoreCase(scComponentAttribute.getIsAdditionalParam())) {
				Integer addServiceParamId = StringUtils.isEmpty(scComponentAttribute.getAttributeValue())?0:Integer.valueOf(scComponentAttribute.getAttributeValue());
				Optional<ScAdditionalServiceParam> optScAddServParam = scAdditionalServiceParamRepository.findById(addServiceParamId);
				if(optScAddServParam.isPresent()) {
					scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(), optScAddServParam.get().getValue());
				}
			}else {
				scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
						scComponentAttribute.getAttributeValue());
			}
		}
		LOGGER.info(" getComponentAndAdditionalAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	
	public Map<String, String> getComponentAttributes(Integer scServiceDetailId, String componentName,
			String siteType, List<String> attributeName) {
		LOGGER.info("Entering getComponentAttributes with scServiceId : {} componentName : {} siteType : {}",
				scServiceDetailId, componentName, siteType);
		List<ScComponentAttribute> scComponentAttrs = scComponentAttributesRepository
				.findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetailId, componentName, siteType, attributeName);
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttrs) {
			if("Y".equalsIgnoreCase(scComponentAttribute.getIsAdditionalParam())) {
				Integer addServiceParamId = StringUtils.isEmpty(scComponentAttribute.getAttributeValue())?0:Integer.valueOf(scComponentAttribute.getAttributeValue());
				Optional<ScAdditionalServiceParam> optScAddServParam = scAdditionalServiceParamRepository.findById(addServiceParamId);
				if(optScAddServParam.isPresent()) {
					scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(), optScAddServParam.get().getValue());
				}
			}else {
				scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
			}
		}
		LOGGER.info("2 getComponentAttributes response : {}", scComponentAttributesAMap);
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
	public Map<String, String> getComponentAttributesDetails(List<String> attributeName,Integer scServiceDetailId, String componentName,
			String siteType) {
		
		LOGGER.info("Entering getComponentAttributes with scServiceId : {} componentName : {} siteType : {} attributeName: {}",
				scServiceDetailId, componentName, siteType,attributeName);

		List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
				.findByScServiceDetailIdAndAttributeNameInAndScComponent_componentNameAndScComponent_siteType(
						scServiceDetailId, attributeName, componentName, siteType);

		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttributes) {
			scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
		}
		LOGGER.info("3 getComponentAttributes response : {}", scComponentAttributesAMap);
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
		LOGGER.info("4 getComponentAttributes response : {}", scOrderAttributesMap);
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
		subCategoryMap.put("Hot Upgrade-LM Shifting", "NEW");
		subCategoryMap.put("Hot Downgrade-LM Shifting", "NEW");
		subCategoryMap.put("Parallel Shifting", "NEW");
		
		return subCategoryMap;
		
	}


	/**
	 *
	 * This function return optimus order sub category and corresponding Termination type
	 * @author Thamizhselvi Perumal
	 * @return
	 */
	public Map<String,String> terminationTypeSapData(){
		Map<String,String> terminationTypeMap = new HashMap<>();
		terminationTypeMap.put("Hot Upgrade-LM Shifting", "SHIFTING");
		terminationTypeMap.put("Hot Downgrade-LM Shifting", "SHIFTING");
		terminationTypeMap.put("LM Shifting-BSO Change", "CHANGE BSO");
		return terminationTypeMap;

	}


	/**
	 * Optimus - Sap Interface mapping information
	 * @author AnandhiV
	 * 
	 * @return
	 */
	public Map<String,String> sapInterfaceMappingData(){
		Map<String,String> sapInterfaceMap = new HashMap<>();
		List<SapInterfaceMapping> sapInterfaceMappings = sapInterfaceMappingRepository.findByStatus(CommonConstants.BACTIVE);
		if(sapInterfaceMappings!=null && !sapInterfaceMappings.isEmpty()) {
			sapInterfaceMappings.stream().forEach(detail->{
				sapInterfaceMap.put(detail.getOptimusInterface(), detail.getSapInterface());
			});
		}
		return sapInterfaceMap;
	}
	
	/**
	 * 
	 * Sap vendor - Sap buyer mapping
	 * @author AnandhiV
	 * @return
	 */
	public Map<String,String> sapBuyerMappingData(){
		Map<String,String> sapBuyerMap = new HashMap<>();
		List<SapVendorBuyerMapping> sapVendorBuyerMappings = sapVendorBuyerMappingRepository.findByStatus(CommonConstants.BACTIVE);
		if(sapVendorBuyerMappings!=null && !sapVendorBuyerMappings.isEmpty()) {
			sapVendorBuyerMappings.stream().forEach(detail->{
				sapBuyerMap.put(detail.getVendorCode(), detail.getBuyerCode());
			});
		}
		return sapBuyerMap;
	}

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
	
	public Map<String, String> getComponentAttributesByScComponent(Integer scComponentId) {
		LOGGER.info("Entering getComponentAttributes with scComponentId : {}", scComponentId);
		List<ScComponentAttribute> scComponentAttrs = scComponentAttributesRepository
				.findByScComponent_id(scComponentId);
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttrs) {
			scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
		}
		LOGGER.info("getComponentAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	public Map<String, String> getServiceAttributesAttributesWithAdditionalParam(
			List<ScServiceAttribute> scComponentAttributes) {

		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScServiceAttribute scComponentAttribute : scComponentAttributes) {
			try {
				if (scComponentAttribute.getIsAdditionalParam() != null
						&& scComponentAttribute.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
							.findById(Integer.valueOf(scComponentAttribute.getAttributeValue()));
					if (scAdditionalServiceParam.isPresent()) {
						scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
								scAdditionalServiceParam.get().getValue());
					}
				} else {
					scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
							scComponentAttribute.getAttributeValue());
				}
			} catch (Exception e) {
				LOGGER.info("Error while fetching service attribute detail for the attribute -->{}",
						scComponentAttribute.getAttributeName());
				LOGGER.error("Error log", e);
			}
		}
		LOGGER.info("getServiceAttributesAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	public Map<String, Object> getComponentAttributesAttributesWithAdditionalParam(
			List<ScComponentAttribute> scComponentAttributes) {

		Map<String, Object> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttributes) {
			try {
				if (scComponentAttribute.getIsAdditionalParam() != null
						&& scComponentAttribute.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
							.findById(Integer.valueOf(scComponentAttribute.getAttributeValue()));
					if (scAdditionalServiceParam.isPresent()) {
						scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
								scAdditionalServiceParam.get().getValue());
					}
				} else {
					scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
							scComponentAttribute.getAttributeValue());
				}
			} catch (Exception e) {

				LOGGER.info("Error while fetching component attribute detail for the attribute -->{}",
						scComponentAttribute.getAttributeName());
				LOGGER.error("Error log", e);

			}
		}
		LOGGER.info("getServiceAttributesAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	public Map<String, Object> getOrderAttributes(List<ScOrderAttribute> scOrderAttributes) {

		Map<String, Object> scComponentAttributesAMap = new HashMap<>();
		for (ScOrderAttribute scComponentAttribute : scOrderAttributes) {
			try {
				scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
						scComponentAttribute.getAttributeValue());
			} catch (Exception e) {

				LOGGER.info("Error while fetching order attribute detail for the attribute -->{}",
						scComponentAttribute.getAttributeName());
				LOGGER.error("Error log", e);

			}
		}
		LOGGER.info("getOrderAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}
	
	public Map<String, String> getScOrdertAttributesDetails(List<String> attributeValue,ScOrder scOrder) {

		List<ScOrderAttribute> scOrderAttributes = scOrderAttributeRepository.findByAttributeNameInAndScOrder(attributeValue, scOrder);
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
	 * getComponentAttributes - based on component
	 * 
	 * @param scServiceDetailId
	 * @param scComponent
	 * @param attributeValue
	 * @return {@linkplain Map}
	 */
	public Map<String, String> getComponentAttributesDetails(List<String> attributeValue,Integer scServiceDetailId, ScComponent scComponent) {

		List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
				.findByScComponentAndAttributeNameInAndScServiceDetailId(scComponent, attributeValue,
						scServiceDetailId);
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttributes) {
			scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
		}
		LOGGER.info("getComponentAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;

	}

	/**
	 * Method to fetch flowgroup attributes based
	 * on flow group id.
	 * @param flowGroupId
	 * @return
	 */
	public Map<String, Object> getFlowGroupAttributes(Integer flowGroupId){
		if(Objects.nonNull(flowGroupId)){
			Optional<GscFlowGroup> flowGroupOptional = gscFlowGroupRepository.findById(flowGroupId);
			if(flowGroupOptional.isPresent()){
				GscFlowGroup flowGroup = flowGroupOptional.get();
				List<FlowGroupAttribute> flowGroupAttributes = flowGroupAttributeRepository.findByGscFlowGroup(flowGroup);
				Map<String, Object> batchAttributes = new HashMap<>();
				flowGroupAttributes.forEach(flowGroupAttribute -> {
					batchAttributes.put("attributeId", flowGroupAttribute.getId());
					batchAttributes
							.put(flowGroupAttribute.getAttributeName(), flowGroupAttribute.getAttributeValue());
				});
				return batchAttributes;
			}
		}
		return null;
	}
	
	public static Map<String, String> parseProductDetailAttributeIntoMap(List<ScProductDetailAttribute> scProductDetailAttributeL) {
		Map<String, String> attrMap = new HashMap<>();
		scProductDetailAttributeL.forEach(attr -> {
			attrMap.put(attr.getAttributeName(), attr.getAttributeValue());
		});
		return attrMap;
	}
}
