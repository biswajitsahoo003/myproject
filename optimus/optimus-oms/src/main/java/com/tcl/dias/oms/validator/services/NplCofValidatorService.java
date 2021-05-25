package com.tcl.dias.oms.validator.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.ValidatorConstants;
import com.tcl.dias.oms.validator.beans.ValidatorAttributeBean;
import com.tcl.dias.oms.validator.core.ValidatorFactory;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Cof validator service
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class NplCofValidatorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NplCofValidatorService.class);

	@Autowired
	ValidatorFactory validatorFactory;

	public CommonValidationResponse processCofValidation(Map<String, Object> cofAttributes, String productName,
			String type) {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		List<String> validationMessages = new ArrayList<>();
		commonValidationResponse.setStatus(true);
		try {
			List<ValidatorAttributeBean> validatorAttributes = validatorFactory.getValidatorAttributes(productName,
					type);
			for (ValidatorAttributeBean validatorAttributeBean : validatorAttributes) {
				String xPath = validatorAttributeBean.getxPath();
				if (xPath.equals("/")) {
					processSingleNode(cofAttributes, validationMessages, validatorAttributeBean);
				} else {
					processMultiTreeV1(cofAttributes, validationMessages, validatorAttributeBean, xPath);
				}	
			}
			Optional<ValidatorAttributeBean> processCommerical = validatorAttributes.stream().filter(attribute -> ValidatorConstants.PROCESS_COMMERCIALS.equalsIgnoreCase(attribute.getNodeName())).findAny();
			if(processCommerical.isPresent()) {
				processCommercialsValidation(cofAttributes, validationMessages, type);
			}
			
			

		} catch (Exception e) {
			LOGGER.error("Error in validating the cof validation", e);
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage("Data Error");
		}
		String validationMessage = validationMessages.stream().collect(Collectors.joining(","));
		if (StringUtils.isNotBlank(validationMessage)) {
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage(validationMessage);
		}
		return commonValidationResponse;
	}
	
	@SuppressWarnings("unchecked")
	private void processCommercialsValidation(Map<String, Object> cofAttributes, List<String> validationMessages, String type) throws TclCommonException {
		try {
			
			// MACD specific Validations
			if(type.equalsIgnoreCase(CommonConstants.MACD))
				processMACDValidation(cofAttributes, validationMessages);
		
		List<Map<String,Object>> commercialsList = (List<Map<String,Object>>) cofAttributes.get(ValidatorConstants.COMMERCIALS);
		Map<String,Object> subTotalSiteLevel = new HashMap<>();
		Integer months = null;
		Double totalTcv = 0D;
		if(commercialsList != null && !commercialsList.isEmpty()) {
			commercialsList.stream().forEach(commercialEntry -> {
				if(((String) cofAttributes.get(ValidatorConstants.OFFERING_TYPE)).equalsIgnoreCase(ValidatorConstants.PRIVATE_LINE)) {
				List<Map<String,Object>> siteCommercialsList = (List<Map<String,Object>>) commercialEntry.get(ValidatorConstants.LINK_COMMERCIALS);
				siteCommercialsList.stream().forEach(siteCommercialEntry -> {
					Double subTotalMrc = 0D;
					Double subTotalNrc = 0D;
					Double subTotalArc = 0D;
					
					if(((String) cofAttributes.get(ValidatorConstants.OFFERING_TYPE)).equalsIgnoreCase(ValidatorConstants.PRIVATE_LINE)) {
					subTotalNrc = ((siteCommercialEntry.get(ValidatorConstants.CONNECTIVITY_NRC)==null?0D:(Double) siteCommercialEntry.get(ValidatorConstants.CONNECTIVITY_NRC)) +
							(siteCommercialEntry.get(ValidatorConstants.LINK_MGMT_CHARGES_NRC)==null?0D:(Double)siteCommercialEntry.get(ValidatorConstants.LINK_MGMT_CHARGES_NRC)) + 
							(siteCommercialEntry.get(ValidatorConstants.SHIFTINGCHARGE_NRC)==null?0D:(Double) siteCommercialEntry.get(ValidatorConstants.SHIFTINGCHARGE_NRC)));
					
					
					subTotalArc = ((siteCommercialEntry.get(ValidatorConstants.CONNECTIVITY_ARC)==null?0D:(Double) siteCommercialEntry.get(ValidatorConstants.CONNECTIVITY_ARC)) +
							(siteCommercialEntry.get(ValidatorConstants.LINK_MGMT_CHARGES_ARC)==null?0D:(Double)siteCommercialEntry.get(ValidatorConstants.LINK_MGMT_CHARGES_ARC)) + 
							( siteCommercialEntry.get(ValidatorConstants.SHIFTINGCHARGE_ARC)==null?0D:(Double) siteCommercialEntry.get(ValidatorConstants.SHIFTINGCHARGE_ARC)));
					
				
					}
					
					LOGGER.info("site - subTotal Nrc {}, subTotal arc {} ", subTotalNrc, subTotalArc );
					subTotalSiteLevel.put(ValidatorConstants.SUBTOTAL_NRC, ((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC) == null) ? 0D : (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)) + subTotalNrc);
					subTotalSiteLevel.put(ValidatorConstants.SUBTOTAL_ARC, ((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC) == null) ? 0D : (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)) + subTotalArc);
					
					LOGGER.info("subtotal for sites so far - subTotal Nrc {}, subTotal arc {} ",  
							subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC),  subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC));
				});
			} else if (((String) cofAttributes.get(ValidatorConstants.OFFERING_TYPE)).equalsIgnoreCase(ValidatorConstants.CROSS_CONNECT)) {
				
				List<Map<String,Object>> siteCommercialsList = (List<Map<String,Object>>) commercialEntry.get(ValidatorConstants.CROSS_CONNECT_COMMERCIALS);
				siteCommercialsList.stream().forEach(siteCommercialEntry -> {
					Double subTotalMrc = 0D;
					Double subTotalNrc = 0D;
					Double subTotalArc = 0D;
					
				subTotalNrc = ((siteCommercialEntry.get(ValidatorConstants.CROSSCONNECT_NRC)==null? 0D: (Double) siteCommercialEntry.get(ValidatorConstants.CROSSCONNECT_NRC)) +
						(siteCommercialEntry.get(ValidatorConstants.FIBERENTRY_NRC)==null? 0D : (Double)siteCommercialEntry.get(ValidatorConstants.FIBERENTRY_NRC)));
				
				
				subTotalArc = ((siteCommercialEntry.get(ValidatorConstants.CROSSCONNECT_ARC)==null? 0D:(Double) siteCommercialEntry.get(ValidatorConstants.CROSSCONNECT_ARC)) +
						(siteCommercialEntry.get(ValidatorConstants.FIBERENTRY_ARC)==null?0D:(Double)siteCommercialEntry.get(ValidatorConstants.FIBERENTRY_ARC)));
				
				
			
			
			LOGGER.info("site - subTotal Nrc {}, subTotal arc {} ", subTotalNrc, subTotalArc );
			subTotalSiteLevel.put(ValidatorConstants.SUBTOTAL_NRC, ((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC) == null) ? 0D : (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)) + subTotalNrc);
			subTotalSiteLevel.put(ValidatorConstants.SUBTOTAL_ARC, ((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC) == null) ? 0D : (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)) + subTotalArc);
			
			LOGGER.info("subtotal for sites so far - subTotal Nrc {}, subTotal arc {} ",  
					subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC),  subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC));
				});
			}
			
				if(Math.abs((Double)commercialEntry.get(ValidatorConstants.TOTAL_NRC) - (Double)subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)) > 1) {
					LOGGER.info("Total NRC is not matching the subtotal NRC of all links, total nrc - {}, subTotalNRC - {}", String.valueOf(commercialEntry.get(ValidatorConstants.TOTAL_NRC)) , 
							String.valueOf(subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)));
					validationMessages.add("Total NRC does not match the subtotal NRC of all links");
				} else if (Math.abs((Double)commercialEntry.get(ValidatorConstants.TOTAL_ARC) - (Double)subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)) > 1) {
					LOGGER.info("Total ARC is not matching the subtotal ARC of all links, total Arc - {}, subTotalARC - {}", String.valueOf(commercialEntry.get(ValidatorConstants.TOTAL_ARC)) , 
							String.valueOf(subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)));
					validationMessages.add("Total ARC does not match the subtotal ARC of all links");
				}
				 
				 
			});
			
			String termsInMonth = (String) cofAttributes.get(ValidatorConstants.CONTRACT_TERM);
			LOGGER.info("termsInMonth {}", termsInMonth);
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;

			} else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}
			
			totalTcv = (((Double)subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)/12.0) * months) + (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC);
			Double tcv = (Double)cofAttributes.get(ValidatorConstants.TOTAL_TCV);
			LOGGER.info("Total Tcv calculated {}, tcv from quote to le {}", totalTcv, tcv);
			if(Math.abs(tcv - totalTcv) > 5) {
				LOGGER.info("Total TCV calculated is not matching the tcv from quote to le, total TCV  calculated - {}, tcv from db - {}", totalTcv, tcv ); 
				validationMessages.add("Total TCV calculated does not match the TCV displayed");
			}
			
		}
		}catch (Exception e) {
			LOGGER.error("Error in validating the cof validation", e);
		}
		
		
	}

	private void processMACDValidation(Map<String, Object> cofAttributes, List<String> validationMessages) {
		LOGGER.info("MACD Validation {}, {}", (String) cofAttributes.get(ValidatorConstants.ORDER_TYPE), 
				(Boolean) cofAttributes.get(ValidatorConstants.IS_MULTICIRCUIT));
		if(((String) cofAttributes.get(ValidatorConstants.ORDER_TYPE)).equalsIgnoreCase(MACDConstants.MACD))
			{
			LOGGER.info("in NPL MACD loop");
				if(Objects.isNull((String)cofAttributes.get(ValidatorConstants.SERVICE_ID))) {
					LOGGER.info("Service Id is Mandatory");
					validationMessages.add("Service Id is Mandatory");
				}
			}		
	}

	private void processSingleNode(Map<String, Object> cofAttributes, List<String> validationMessages,
			ValidatorAttributeBean validatorAttributeBean) {
		Object nodeValue = cofAttributes.get(validatorAttributeBean.getNodeName());
		processMessage(validationMessages, validatorAttributeBean, nodeValue);
	}

	@SuppressWarnings("unchecked")
	private void processMultiTree(Map<String, Object> cofAttributes, List<String> validationMessages,
			ValidatorAttributeBean validatorAttributeBean, String xPath) {
		String[] splitter = xPath.split("/");
		if (splitter != null && splitter.length > 0) {
			Map<String, Object> nodeParent = new HashMap<>(cofAttributes);
			int count = 1;
			for (String splitNodeName : splitter) {
				if (StringUtils.isNoneBlank(splitNodeName)) {
					if (splitNodeName.contains("$")) {
						Object nodeValue = nodeParent.get(splitNodeName.replace("$", ""));
						int tempCount = count;
						Map<String, Object> tempNodeParent = new HashMap<>(nodeParent);
						for (Map<String, Object> nodeVal : (List<Map<String, Object>>) nodeValue) {
							Object subNodeValue = nodeVal.get(splitNodeName);
							if (tempCount == splitter.length) {
								processMessage(validationMessages, validatorAttributeBean, subNodeValue);
							} else {
								tempNodeParent = (Map<String, Object>) nodeVal;
							}
							tempCount++;
						}
					} else {
						Object nodeValue = nodeParent.get(splitNodeName);
						if (count == splitter.length) {
							processMessage(validationMessages, validatorAttributeBean, nodeValue);
							break;
						} else {
							nodeParent = (Map<String, Object>) nodeValue;
						}
					}
				}
				count++;
			}
		}
	}

	private void processMultiTreeV1(Map<String, Object> cofAttributes, List<String> validationMessages,
			ValidatorAttributeBean validatorAttributeBean, String xPath) {
		Map<String, Object> req = new HashMap<>();
		boolean flag = true;
		req.put("xPath", xPath+"/"+validatorAttributeBean.getNodeName());
		req.put("nodeParent", cofAttributes);
		req.put("validatorBean", validationMessages);
		req.put("nodeName", validatorAttributeBean.getNodeName());
		while (flag) {
			flag = processTreeValidator(req, flag);
		}
	}

	private void processMessage(List<String> validationMessages, ValidatorAttributeBean validatorAttributeBean,
			Object nodeValue) {
		if (nodeValue == null) {
			validationMessages.add(validatorAttributeBean.getNodeName() + " is Mandatory");
			LOGGER.info("{} is Mandatory", validatorAttributeBean.getNodeName());
		} else if (nodeValue instanceof String && StringUtils.isBlank((String) nodeValue)) {
			validationMessages.add(validatorAttributeBean.getNodeName() + " is Mandatory and cannot be blank");
			LOGGER.info("{} is Mandatory and cannot be blank", validatorAttributeBean.getNodeName());
		}
	}

	
	

	 

	@SuppressWarnings("unchecked")
	private static boolean processTreeValidator(Map<String, Object> req, boolean flag) {
		String xpath = (String) req.get("xPath");
		Object nodeParent = req.get("nodeParent");
		if (xpath.startsWith(CommonConstants.RIGHT_SLASH)) {
			xpath = xpath.replaceFirst(CommonConstants.RIGHT_SLASH, CommonConstants.EMPTY);
			if (xpath.startsWith("$")) {
				String tempPath = xpath.substring(0, xpath.indexOf(CommonConstants.RIGHT_SLASH)).replaceFirst("\\$", CommonConstants.EMPTY);
				LOGGER.info("temp Path {}",tempPath);
				xpath =xpath.replaceFirst("\\$", "");
				xpath =xpath.replaceFirst(tempPath, "");
				LOGGER.info("xPath {}",xpath);
				Object node = ((Map<String, Object>) nodeParent).get(tempPath);
				if(node==null) {
					LOGGER.info("Node is absent");
					return false;
				}else {
					LOGGER.info("Node is present");
				}
				if (node instanceof List) {
					LOGGER.info("List ");
					for (Map<String, Object> nodeV : (List<Map<String, Object>>) node) {
						req.put("xPath", xpath);
						req.put("nodeParent", nodeV);
						flag = processTreeValidator(req, flag);
					}
					nodeParent = node;
					req.put("nodeParent", nodeParent);
				}else {
					nodeParent = node;
					req.put("nodeParent", nodeParent);
					req.put("xPath", xpath);
					flag = processTreeValidator(req, flag);
				}
			}else if (xpath.contains(CommonConstants.RIGHT_SLASH)) {
				String tempPath = xpath.substring(0, xpath.indexOf(CommonConstants.RIGHT_SLASH));
				LOGGER.info(tempPath);
				Object node = ((Map<String, Object>) nodeParent).get(tempPath);
				xpath=xpath.replaceFirst(tempPath, "");
				req.put("xPath", xpath);
				if(node==null) {
					LOGGER.info("Node is absent");
					return false;
				}else {
					LOGGER.info("Node is present");
				}
				if (node instanceof List) {
					for (Map<String, Object> nodeV : (List<Map<String, Object>>) node) {
						req.put("xPath", xpath.replaceFirst("\\$", CommonConstants.EMPTY));
						req.put("nodeParent", nodeV);
						flag = processTreeValidator(req, flag);
					}
				} else {
					nodeParent = node;
					req.put("nodeParent", nodeParent);
					req.put("xPath", xpath);
					flag = processTreeValidator(req, flag);
				}
			} else {
				String nodeName = (String) req.get("nodeName");
				List<String> validationMessages = (List<String>) req.get("validatorBean");
				LOGGER.info("final tree : {}", xpath);
				Object node = ((Map<String, Object>) nodeParent).get(xpath);
				if (node == null) {
					validationMessages.add(nodeName + " is Mandatory");
					LOGGER.info("{} is Mandatory", nodeName);
				} else if (node instanceof String && StringUtils.isBlank((String) node)) {
					validationMessages.add(nodeName + " is Mandatory and cannot be blank");
					LOGGER.info("{} is Mandatory and cannot be blank", nodeName);
				}
				flag = false;
			}

		}
		return flag;
	}
	
	/**
	 * Method to format currency based on locale USD - 100,000. INR 1,00,000
	 * 
	 * @param num
	 * @param currency
	 * @return formatted currency
	 */
	private String getFormattedCurrency(Double num) {

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formatter.setDecimalFormatSymbols(symbols);
		if (num != null) {
			return formatter.format(num);
		} else {
			return num + "";
		}
	}

	public CommonValidationResponse processMMRCofValidation(Map<String, Object> cofAttributes) {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		List<String> validationMessages = new ArrayList<>();
		commonValidationResponse.setStatus(true);
		try {
			Map<String, Object> enrichmentDetailsBean=(Map<String, Object>) cofAttributes.get("enrichmentDetailsBean");
			Map<String, Object> crossConnectEnrichmentBean=(Map<String, Object>)enrichmentDetailsBean.get("crossConnectEnrichmentBean");
			if(crossConnectEnrichmentBean==null||crossConnectEnrichmentBean.isEmpty()) {
				String crossConnectValidationMessage = "Cross Connect Order Enrichment is Empty";
				validationMessages.add(crossConnectValidationMessage);
			}

		} catch (Exception e) {
			LOGGER.error("Error in validating the cof validation", e);
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage("Data Error");
		}
		String validationMessage = validationMessages.stream().collect(Collectors.joining(","));
		if (StringUtils.isNotBlank(validationMessage)) {
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage(validationMessage);
		}
		return commonValidationResponse;
	}
}