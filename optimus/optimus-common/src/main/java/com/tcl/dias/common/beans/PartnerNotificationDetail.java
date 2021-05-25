package com.tcl.dias.common.beans;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean for Notification related to partner
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerNotificationDetail {

    private String partnerAccountName;
    private String partnerAccountId;
    private String partnerOrgId;
    private String partnerLeName;
    private String partnerLeCUID;

    public String getPartnerAccountName() {
        return partnerAccountName;
    }

    public void setPartnerAccountName(String partnerAccountName) {
        this.partnerAccountName = partnerAccountName;
    }

    public String getPartnerAccountId() {
        return partnerAccountId;
    }

    public void setPartnerAccountId(String partnerAccountId) {
        this.partnerAccountId = partnerAccountId;
    }

    public String getPartnerOrgId() {
        return partnerOrgId;
    }

    public void setPartnerOrgId(String partnerOrgId) {
        this.partnerOrgId = partnerOrgId;
    }

    public String getPartnerLeName() {
        return partnerLeName;
    }

    public void setPartnerLeName(String partnerLeName) {
        this.partnerLeName = partnerLeName;
    }

    public String getPartnerLeCUID() {
        return partnerLeCUID;
    }

    public void setPartnerLeCUID(String partnerLeCUID) {
        this.partnerLeCUID = partnerLeCUID;
    }

    public static PartnerNotificationDetail toConvert(Map<String, Object> map) {
        PartnerNotificationDetail partnerNotificationDetail = new PartnerNotificationDetail();
        partnerNotificationDetail.setPartnerAccountId((String) map.get("PartnerAccountId"));
        partnerNotificationDetail.setPartnerAccountName((String) map.get("PartnerAccountName"));
        partnerNotificationDetail.setPartnerLeCUID((String) map.get("PartnerLeCUID"));
        partnerNotificationDetail.setPartnerLeName((String) map.get("PartnerLeName"));
        partnerNotificationDetail.setPartnerOrgId((String) map.get("PartnerOrgId"));
        return partnerNotificationDetail;
    }

    public static String getOrgId(PartnerNotificationDetail partnerNotificationDetail) {
        return partnerNotificationDetail.getPartnerOrgId();
    }

    @Override
    public String toString() {
        return "PartnerNotificationDetail{" +
                "partnerAccountName='" + partnerAccountName + '\'' +
                ", partnerAccountId='" + partnerAccountId + '\'' +
                ", partnerOrgId='" + partnerOrgId + '\'' +
                ", partnerLeName='" + partnerLeName + '\'' +
                ", partnerLeCUID='" + partnerLeCUID + '\'' +
                '}';
    }
}
