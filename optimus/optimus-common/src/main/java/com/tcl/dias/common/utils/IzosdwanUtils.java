package com.tcl.dias.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
/**
 * 
 * This is the utils class for IZOSDWAN
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzosdwanUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanUtils.class);
	public static <T> T fromJson(String jsonStr, TypeReference<T> valueType) {
		T object = null;
		try {
			object = new ObjectMapper().readValue(jsonStr, valueType);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return object;
	}
	
	public static Map<String,List<String>> getComponentsAndSubComponentsMap(){
		Map<String,List<String>> map = new HashMap<>();
		// VPN port or Internet port
		List<String> portSubcomponentList = new ArrayList<>();
		portSubcomponentList.add("Fixed Port(MRC)");
		portSubcomponentList.add("Fixed Port");
		portSubcomponentList.add("Port NRC");
		map.put("Internet Port", portSubcomponentList);
		map.put("VPN Port", portSubcomponentList);
		//CPE 
		List<String> cpeSubComponentList = new ArrayList<>();
		cpeSubComponentList.add("CPE Hardware - Rental");
		cpeSubComponentList.add("CPE Hardware - Outright");
		cpeSubComponentList.add("CPE Installation");
		cpeSubComponentList.add("CPE Custom Tax");
		cpeSubComponentList.add("CPE Delivery");
		cpeSubComponentList.add("CPE Local Tax");
		cpeSubComponentList.add("CPE Support");
		cpeSubComponentList.add("CPE totals");
		map.put("CPE", cpeSubComponentList);
		List<String> lastmileSubComponentList = new ArrayList<>();
		lastmileSubComponentList.add("LM MAN BW");
		lastmileSubComponentList.add("LM MAN MUX");
		lastmileSubComponentList.add("LM MAN inbuilding");
		lastmileSubComponentList.add("MAN OCP");
		lastmileSubComponentList.add("MAN Rentals");
		lastmileSubComponentList.add("MAN OTC");
		lastmileSubComponentList.add("PROW Value (OTC)");
		lastmileSubComponentList.add("PROW Value (ARC)");
		lastmileSubComponentList.add("Provider Charge");
		lastmileSubComponentList.add("Provider Charge OTC");
		lastmileSubComponentList.add("Mast Charge offnet");
		lastmileSubComponentList.add("Radwin");
		lastmileSubComponentList.add("OTC/NRC - Installation");
		lastmileSubComponentList.add("Mast Charge onnet");
		lastmileSubComponentList.add("ARC Converter Charges");
		lastmileSubComponentList.add("ARC-Colocation");
		lastmileSubComponentList.add("ARC - BW");
		lastmileSubComponentList.add("LM MRC");
		lastmileSubComponentList.add("LM NRC");
		lastmileSubComponentList.add("XConnect MRC");
		lastmileSubComponentList.add("XConnect NRC");
		lastmileSubComponentList.add("ARC Modem charges");
		lastmileSubComponentList.add("NRC Modem charges");
		lastmileSubComponentList.add("NRC Installation");
		lastmileSubComponentList.add("OffnetProvider ARC");
		map.put("Last mile", lastmileSubComponentList);
		List<String> licenseSubComp = new ArrayList<>();
		licenseSubComp.add("IZO SDWAN service charges");
		map.put("IZO SDWAN service charges", licenseSubComp);
		return map;
	}
	
	public static BigDecimal formatBigDecimal(BigDecimal bigDecimal) {
		return bigDecimal.setScale(2, RoundingMode.HALF_UP);
	}
	
	public static String getAddonMetricsBasedOnQuestion(String questionString) {
		if(questionString == null) {
			return IzosdwanCommonConstants.USERS;
		}else if(questionString.toLowerCase().contains(IzosdwanCommonConstants.VOLUME_REQUIRED.toLowerCase())) {
			return IzosdwanCommonConstants.VOLUME;
		}else if(questionString.toLowerCase().contains(IzosdwanCommonConstants.MILLION_CELLS.toLowerCase())) {
			return IzosdwanCommonConstants.MILLION_CELLS;
		}else if(questionString.toLowerCase().contains(IzosdwanCommonConstants.QUANTITY.toLowerCase())) {
			return IzosdwanCommonConstants.QUANTITY;
		}else if(questionString.toLowerCase().contains(IzosdwanCommonConstants.CONNECTORS.toLowerCase())) {
			return IzosdwanCommonConstants.CONNECTORS;
		}else {
			return CommonConstants.EMPTY;
		}
	}
	
	public static List<String> getCpeAttributesForSdwanOverlay(){
		List<String> cpeAttributeList = new ArrayList<>();
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_SERIAL_NO);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_SCOPE);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_SHARED_OR_NOT);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_BASIC_CHASSIS);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_NAME);
		cpeAttributeList.add(IzosdwanCommonConstants.NMC);
		cpeAttributeList.add(IzosdwanCommonConstants.RACKMOUNT);
		cpeAttributeList.add(IzosdwanCommonConstants.SFP);
		cpeAttributeList.add(IzosdwanCommonConstants.SFP_PLUS);
		cpeAttributeList.add(IzosdwanCommonConstants.POWER_CORD);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_DESC);
		cpeAttributeList.add(IzosdwanCommonConstants.NMC_DESC);
		cpeAttributeList.add(IzosdwanCommonConstants.RACKMOUNT_DESC);
		cpeAttributeList.add(IzosdwanCommonConstants.SFP_DESC);
		cpeAttributeList.add(IzosdwanCommonConstants.SFP_PLUS_DESC);
		cpeAttributeList.add(IzosdwanCommonConstants.POWERCORD_DESC);
		cpeAttributeList.add(IzosdwanCommonConstants.L2_PORTS);
		cpeAttributeList.add(IzosdwanCommonConstants.L3_PORTS);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_MAX_BW);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_MODEL_END_OF_LIFE);
		cpeAttributeList.add(IzosdwanCommonConstants.CPE_MODEL_END_OF_SALE);
		cpeAttributeList.add(IzosdwanCommonConstants.ROUTER_COST);
		cpeAttributeList.add(IzosdwanCommonConstants.RACKMOUNT_COST);
		cpeAttributeList.add(IzosdwanCommonConstants.NMC_COST);
		cpeAttributeList.add(IzosdwanCommonConstants.SFP_COST);
		cpeAttributeList.add(IzosdwanCommonConstants.SFP_PLUS_COST);
		cpeAttributeList.add(IzosdwanCommonConstants.POWER_CORD_COST);
		return cpeAttributeList;
	}
}
