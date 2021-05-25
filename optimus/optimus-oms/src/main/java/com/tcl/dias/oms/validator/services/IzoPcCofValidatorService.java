package com.tcl.dias.oms.validator.services;

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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IzoPcCofValidatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IzoPcCofValidatorService.class);

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
            Optional<ValidatorAttributeBean> processCommerical = validatorAttributes.stream().filter(
                    attribute -> ValidatorConstants.PROCESS_COMMERCIALS.equalsIgnoreCase(attribute.getNodeName())).findAny();
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
            Map<String,Object> subTotalSiteLevelForTcvCalc = new HashMap<>();
            Integer months = null;
            Double totalTcv = 0D;
            boolean[] isInternational = {false};
            if(commercialsList != null && !commercialsList.isEmpty()) {
                commercialsList.stream().forEach(commercialEntry -> {
                    List<Map<String,Object>> siteCommercialsList = (List<Map<String,Object>>) commercialEntry.get(ValidatorConstants.SITE_COMMERCIALS);
                    Map<String,Object> subTotalSiteLevel = new HashMap<>();
                    Double[] subTotalMrc = { 0D };
                    Double[] subTotalNrc = { 0D };
                    Double[] subTotalArc = { 0D };
                    LOGGER.info("siteCommercialsList {}",siteCommercialsList);

					siteCommercialsList.stream().forEach(siteCommercialEntry -> {
						Double[] cpeMrc = { 0D };
						Double[] cpeNrc = { 0D };
						Double[] cpeArc = { 0D };

						LOGGER.info("show main cpe {}, show secondary main cpe {}",
								(Boolean) siteCommercialEntry.get(ValidatorConstants.SHOW_MAIN_CPE),
								(Boolean) siteCommercialEntry.get(ValidatorConstants.SHOW_SECONDARY_MAIN_CPE));

						LOGGER.info("cpe MRC {}, cpe NRc {}, cpe Arc {} in line items loop ", cpeMrc[0], cpeNrc[0],
								cpeArc[0]);

						LOGGER.info("mrc loop");
						subTotalMrc[0] = ((Double) siteCommercialEntry.get(ValidatorConstants.IZOPORT_MRC));
						LOGGER.info("mrc loop exit");

						subTotalNrc[0] = ((Double) siteCommercialEntry.get(ValidatorConstants.IZOPORT_NRC));

						LOGGER.info("arc loop");
						subTotalArc[0] = ((Double) siteCommercialEntry.get(ValidatorConstants.IZOPORT_ARC));
						LOGGER.info("site - subTotal MRc {}, subTotal Nrc {}, subTotal arc {} ", subTotalMrc,
								subTotalNrc, subTotalArc);
						subTotalSiteLevel.put(ValidatorConstants.SUBTOTAL_MRC,
								((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_MRC) == null) ? 0D
										: (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_MRC))
										+ subTotalMrc[0]);
						subTotalSiteLevel.put(ValidatorConstants.SUBTOTAL_NRC,
								((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC) == null) ? 0D
										: (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC))
										+ subTotalNrc[0]);
						subTotalSiteLevel.put(ValidatorConstants.SUBTOTAL_ARC,
								((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC) == null) ? 0D
										: (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC))
										+ subTotalArc[0]);

						LOGGER.info("subtotal for sites so far - subTotal MRc {}, subTotal Nrc {}, subTotal arc {} ",
								subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_MRC),
								subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC),
								subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC));
					});


                    isInternational[0] = (boolean) cofAttributes.get(ValidatorConstants.IS_INTERNATIONAL);
                    LOGGER.info("isInternational :: {}", isInternational[0]);
                    if(!isInternational[0]) {
                        if(Math.abs((Double)subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)-(Double)commercialEntry.get(ValidatorConstants.TOTAL_NRC)) > 1) {
                            LOGGER.info("Total NRC is not matching the subtotal NRC of all sites, total nrc - {}, subTotalNRC - {}", commercialEntry.get(ValidatorConstants.TOTAL_NRC) ,
                                    String.valueOf(subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)));
                            validationMessages.add("Total NRC does not match the subtotal NRC of all sites");
                        } else if(Math.abs((Double)subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)- (Double)commercialEntry.get(ValidatorConstants.TOTAL_ARC)) > 1) {
                            LOGGER.info("Total ARC is not matching the subtotal ARC of all sites, total Arc - {}, subTotalARC - {}", commercialEntry.get(ValidatorConstants.TOTAL_ARC) ,
                                    String.valueOf(subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)));
                            validationMessages.add("Total ARC does not match the subtotal ARC of all sites");
                        }
                    } else {
                        if(Math.abs((Double)subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)-(Double)commercialEntry.get(ValidatorConstants.TOTAL_NRC)) > 1) {
                            LOGGER.info("Total NRC is not matching the subtotal NRC of all sites, total nrc - {}, subTotalNRC - {}", commercialEntry.get(ValidatorConstants.TOTAL_NRC) ,
                                    String.valueOf(subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)));
                            validationMessages.add("Total NRC does not match the subtotal NRC of all sites");
                        } else if(Math.abs((Double)subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_MRC)- (Double)commercialEntry.get(ValidatorConstants.TOTAL_MRC)) > 1) {
                            LOGGER.info("Total MRC is not matching the subtotal MRC of all sites, total Mrc - {}, subTotalMRC - {}", commercialEntry.get(ValidatorConstants.TOTAL_MRC) ,
                                    String.valueOf(subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_MRC)));
                            validationMessages.add("Total MRC does not match the subtotal MRC of all sites");
                        }
                    }

                    subTotalSiteLevelForTcvCalc.put(ValidatorConstants.PROFILEWISE_TOTALMRC, ((subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALMRC) == null) ? 0D :
                            (Double) subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALMRC)) + ((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_MRC) == null) ? 0D : (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_MRC)));
                    subTotalSiteLevelForTcvCalc.put(ValidatorConstants.PROFILEWISE_TOTALNRC, ((subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALNRC) == null) ? 0D :
                            (Double) subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALNRC)) + ((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC) == null) ? 0D : (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_NRC)));
                    subTotalSiteLevelForTcvCalc.put(ValidatorConstants.PROFILEWISE_TOTALARC, ((subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALARC) == null) ? 0D :
                            (Double) subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALARC)) + ((subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC) == null) ? 0D : (Double) subTotalSiteLevel.get(ValidatorConstants.SUBTOTAL_ARC)));

                });

                String termsInMonth = (String) cofAttributes.get(ValidatorConstants.CONTRACT_TERM);
                LOGGER.info("termsInMonth {}", termsInMonth);
                if (termsInMonth.contains("year")) {
                    months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
                }else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
        				Double months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
        				months=months1.intValue();
                } else if (termsInMonth.contains("months")) {
                    months = Integer.valueOf(termsInMonth.replace("months", "").trim());
                } else if (termsInMonth.contains("month")) {
                    months = Integer.valueOf(termsInMonth.replace("month", "").trim());
                }
                if(!isInternational[0]) {
                    totalTcv = (((Double)subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALARC)/12.0) * months) + (Double) subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALNRC);
                    Double tcv = (Double)cofAttributes.get(ValidatorConstants.TOTAL_TCV);
                    LOGGER.info("Total Tcv calculated {}, tcv from quote to le {}", totalTcv, tcv);
                    if(Math.abs(tcv - totalTcv) > 5) {
                        LOGGER.info("Total TCV calculated is not matching the tcv from quote to le, total TCV  calculated - {}, tcv from db - {}", totalTcv, tcv );
                        validationMessages.add("Total TCV calculated does not match the TCV displayed");
                    }
                } else {
                    totalTcv = ((Double)subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALMRC) * months) + (Double) subTotalSiteLevelForTcvCalc.get(ValidatorConstants.PROFILEWISE_TOTALNRC);
                    Double tcv = (Double)cofAttributes.get(ValidatorConstants.TOTAL_TCV);
                    LOGGER.info("Total Tcv calculated {}, tcv from quote to le {}", totalTcv, tcv);
                    if(Math.abs(tcv - totalTcv) > 5) {
                        LOGGER.info("Total TCV calculated is not matching the tcv from quote to le, total TCV  calculated - {}, tcv from db - {}", totalTcv, tcv );
                        validationMessages.add("Total TCV calculated does not match the TCV displayed");
                    }
                }

            }
        }catch (Exception e) {
            LOGGER.error("Error in validating the cof validation", e);
        }


    }

    private void processMACDValidation(Map<String, Object> cofAttributes, List<String> validationMessages) {
        LOGGER.info("MACD Validation {}, {}", (String) cofAttributes.get(ValidatorConstants.ORDER_TYPE),
                (Boolean) cofAttributes.get(ValidatorConstants.IS_MULTICIRCUIT));
        if(((String) cofAttributes.get(ValidatorConstants.ORDER_TYPE)).equalsIgnoreCase(MACDConstants.MACD)
                && !((Boolean) cofAttributes.get(ValidatorConstants.IS_MULTICIRCUIT)))
        {
            LOGGER.info("in not multiciruit loop");
            if(Objects.isNull((String)cofAttributes.get(ValidatorConstants.SERVICE_ID))) {
                LOGGER.info("Service Id is Mandatory");
                validationMessages.add("Service Id is Mandatory");
            }

            if((((String) cofAttributes.get(ValidatorConstants.LINK_TYPE)).equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
                    ||  ((String) cofAttributes.get(ValidatorConstants.LINK_TYPE)).equalsIgnoreCase(MACDConstants.DUAL_SECONDARY))
                    && (Objects.isNull((String) cofAttributes.get(ValidatorConstants.PRIMARY_SERVICE_ID)) ||
                    Objects.isNull((String) cofAttributes.get(ValidatorConstants.SECONDARY_SERVICE_ID)))) {
                LOGGER.info("Primary or Secondary Service Id is null");
                validationMessages.add("Primary or Secondary Service Id is Mandatory");
            }
        }

        if(((String) cofAttributes.get(ValidatorConstants.ORDER_TYPE)).equalsIgnoreCase(MACDConstants.MACD)
                && ((Boolean) cofAttributes.get(ValidatorConstants.IS_MULTICIRCUIT))) {
            List<Map<String,Object>> commercialsList = (List<Map<String,Object>>) cofAttributes.get(ValidatorConstants.COMMERCIALS);
            if(commercialsList != null && !commercialsList.isEmpty()) {
                commercialsList.stream().forEach(commercialEntry -> {
                    List<Map<String,Object>> siteCommercialsList = (List<Map<String,Object>>) commercialEntry.get(ValidatorConstants.SITE_COMMERCIALS);
                    siteCommercialsList.stream().forEach(siteCommercialEntry -> {
                        if(Objects.isNull(siteCommercialEntry.get(ValidatorConstants.SERVICE_ID))) {
                            LOGGER.info("Service id is mandatory for multicircuit");
                            validationMessages.add("Service id is mandatory for multicircuit");
                        }
                    });
                });
            }

            List<Map<String,Object>> solutionsList = (List<Map<String,Object>>) cofAttributes.get(ValidatorConstants.SOLUTIONS);
            if(solutionsList != null && !solutionsList.isEmpty()) {
                solutionsList.stream().forEach(solutionEntry -> {
                    List<Map<String,Object>> siteDetailssList = (List<Map<String,Object>>) solutionEntry.get(ValidatorConstants.SITE_DETAILS);
                    siteDetailssList.stream().forEach(siteDetails -> {
                        if(Objects.isNull(siteDetails.get(ValidatorConstants.SERVICE_ID))) {
                            LOGGER.info("Service id is mandatory for multicircuit in solution details");
                            validationMessages.add("Service id is mandatory for multicircuit in solution details");
                        }


						/* Pt-1768 removing linkType condition for multicircuit

						if((((String) cofAttributes.get(ValidatorConstants.LINK_TYPE)).equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
								||  ((String) cofAttributes.get(ValidatorConstants.LINK_TYPE)).equalsIgnoreCase(MACDConstants.DUAL_SECONDARY))
								&& (Objects.isNull((String) cofAttributes.get(ValidatorConstants.PRIMARY_SERVICE_ID)) ||
										Objects.isNull((String) cofAttributes.get(ValidatorConstants.SECONDARY_SERVICE_ID)))) {
											LOGGER.info("Primary or Secondary Service Id is null in solution details");
											validationMessages.add("Primary or Secondary Service Id is Mandatory in solution details");
										}*/
                    });
                });
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
}
