package com.tcl.dias.customer.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcl.dias.customer.constants.PartnerCustomerConstants;

/**
 * Utility class for partner customer details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerCustomerUtils {

    /**
     * Get Document Name
     *
     * @return {@link Map}
     */
    public static Map<String, String> getDocumentNameByMstAttributeName() {
        Map<String, String> documentName = new HashMap<>();
        documentName.put(PartnerCustomerConstants.NDA, PartnerCustomerConstants.NDA);
        documentName.put(PartnerCustomerConstants.SELLING_MODE, PartnerCustomerConstants.SELLING_MODE_DOCUMENTS);
        documentName.put(PartnerCustomerConstants.PRODUCT, PartnerCustomerConstants.SERVICE_SCHEDULE);
        documentName.put(PartnerCustomerConstants.DUE, PartnerCustomerConstants.DUE_DILIGENCE);
        documentName.put(PartnerCustomerConstants.COMMON_TC, PartnerCustomerConstants.COMMON_TC);
        documentName.put(PartnerCustomerConstants.MSA, PartnerCustomerConstants.MSA);
        documentName.put(PartnerCustomerConstants.ADDENDUM, PartnerCustomerConstants.ADDENDUM);
        documentName.put(PartnerCustomerConstants.COMPENSATION, PartnerCustomerConstants.COMPENSATION);
        return documentName;
    }

    /**
     * List of documents to be excluded for new partner
     *
     * @return {@link List}
     */
    public static List<String> excludedDocumentsForNewPartner() {
        return Arrays.asList(PartnerCustomerConstants.MST_ATTRIBUTE_SELL_WITH_MSA, PartnerCustomerConstants.MST_ATTRIBUTE_SELL_WITH_ADDENDUM);
    }

    /**
     * List of documents to be excluded for existing partner
     *
     * @return {@link List}
     */
    public static List<String> excludedDocumentsForExistingPartner() {
        return Arrays.asList(PartnerCustomerConstants.MST_ATTRIBUTE_SELL_WITH_NDA, PartnerCustomerConstants.MST_ATTRIBUTE_SELL_WITH_COMMON);
    }

}
