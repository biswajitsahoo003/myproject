package com.tcl.dias.oms.partner.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcl.dias.oms.partner.constants.PartnerConstants;

/**
 * File Contains all static methods related to PartnerUtils
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerUtils {

    /**
     * Get product Name in OMS by SFDC recordTypeBean
     *
     * @return {@link Map}
     */
    public static Map<String, String> getProductNameFromSFDCRecordType() {
        Map<String, String> productHashMap = new HashMap<>();
        productHashMap.put(PartnerConstants.SFDC_SALES_RESPONSE_TYPE_IAS, PartnerConstants.ILL_PRODUCT);
        productHashMap.put(PartnerConstants.SFDC_SALES_RESPONSE_TYPE_GVPN, PartnerConstants.GVPN_PRODUCT);
        productHashMap.put(PartnerConstants.SFDC_SALES_RESPONSE_TYPE_GSIP, PartnerConstants.GSC_PRODUCT);
        return productHashMap;
    }

    /**
     * Get Record type Name in SFDC by OMS product
     *
     * @return {@link Map}
     */
    public static Map<String, String> getRecordTypeInSFDCFromOMSProduct() {
        Map<String, String> recordType = new HashMap<>();
        recordType.put(PartnerConstants.ILL_PRODUCT, PartnerConstants.SFDC_SALES_RESPONSE_TYPE_IAS);
        recordType.put(PartnerConstants.GVPN_PRODUCT, PartnerConstants.SFDC_SALES_RESPONSE_TYPE_GVPN);
        recordType.put(PartnerConstants.GSC_PRODUCT, PartnerConstants.SFDC_SALES_RESPONSE_TYPE_GSIP);
        return recordType;
    }

    /**
     * Get oms Stage Name by SFDC Stage Name
     *
     * @return {@link Map}
     */
    public static Map<String, String> getOMSStageName() {
        Map<String, String> stageMap = new HashMap<>();
        stageMap.put(PartnerConstants.SFDC_SALES_RESPONSE_STAGE_ORDER_ACCEPTED, PartnerConstants.SFDC_SALES_FUNNEL_RESPONSE_STAGE_ACCEPTED);
        stageMap.put(PartnerConstants.SFDC_SALES_RESPONSE_STAGE_ORDER_PROCESSING, PartnerConstants.SFDC_SALES_FUNNEL_RESPONSE_STAGE_PROCESSING);
        stageMap.put(PartnerConstants.SFDC_SALES_RESPONSE_STAGE_ORDER_DROPPED, PartnerConstants.SFDC_SALES_FUNNEL_RESPONSE_STAGE_DROPPED);
        stageMap.put(PartnerConstants.SFDC_SALES_RESPONSE_STAGE_IDENTIFIED_OPPORTUNITY, PartnerConstants.SFDC_SALES_FUNNEL_RESPONSE_STAGE_SELECT_CONFIGURATION);
        stageMap.put(PartnerConstants.SFDC_SALES_RESPONSE_STAGE_SHORTLISTED, PartnerConstants.SFDC_SALES_FUNNEL_RESPONSE_STAGE_GET_QUOTE);
        stageMap.put(PartnerConstants.SFDC_SALES_RESPONSE_STAGE_PROPOSAL_SENT, PartnerConstants.SFDC_SALES_FUNNEL_RESPONSE_STAGE_GET_QUOTE);
        stageMap.put(PartnerConstants.SFDC_SALES_RESPONSE_STAGE_VERBAL_AGREEMENT, PartnerConstants.SFDC_SALES_FUNNEL_RESPONSE_STAGE_ORDER_FORM);
        return stageMap;
    }

    /**
     * Get List of applicable classfications in optimus
     *
     * @return {@link List}
     */
    public static List<String> getApplicableClassifications() {
        return Arrays.asList(PartnerConstants.SELL_WITH_CLASSIFICATION, PartnerConstants.SELL_THROUGH_CLASSIFICATION);
    }
}
