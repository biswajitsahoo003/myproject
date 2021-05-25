package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * ConfirmAccessRingBean.
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmAccessRingBean extends TaskDetailsBaseBean {

    private String accessRingAggregationName;
    private String accessRingBandwidth;
    private String accessRingName;
    private String cableDetails;
    private String chamberDetails;
    private String trenchDetails;
    private String localLoopBandwidth;

    public String getAccessRingAggregationName() {
        return accessRingAggregationName;
    }

    public void setAccessRingAggregationName(String accessRingAggregationName) {
        this.accessRingAggregationName = accessRingAggregationName;
    }

    public String getAccessRingBandwidth() {
        return accessRingBandwidth;
    }

    public void setAccessRingBandwidth(String accessRingBandwidth) {
        this.accessRingBandwidth = accessRingBandwidth;
    }

    public String getAccessRingName() {
        return accessRingName;
    }

    public void setAccessRingName(String accessRingName) {
        this.accessRingName = accessRingName;
    }

    public String getCableDetails() {
        return cableDetails;
    }

    public void setCableDetails(String cableDetails) {
        this.cableDetails = cableDetails;
    }

    public String getChamberDetails() {
        return chamberDetails;
    }

    public void setChamberDetails(String chamberDetails) {
        this.chamberDetails = chamberDetails;
    }

    public String getTrenchDetails() {
        return trenchDetails;
    }

    public void setTrenchDetails(String trenchDetails) {
        this.trenchDetails = trenchDetails;
    }

    public String getLocalLoopBandwidth() {
        return localLoopBandwidth;
    }

    public void setLocalLoopBandwidth(String localLoopBandwidth) {
        this.localLoopBandwidth = localLoopBandwidth;
    }

    @Override
    public String toString() {
        return "ConfirmAccessRingBean{" +
                "accessRingAggregationName='" + accessRingAggregationName + '\'' +
                ", accessRingBandwidth='" + accessRingBandwidth + '\'' +
                ", accessRingName='" + accessRingName + '\'' +
                ", cableDetails='" + cableDetails + '\'' +
                ", chamberDetails='" + chamberDetails + '\'' +
                ", trenchDetails='" + trenchDetails + '\'' +
                ", localLoopBandwidth='" + localLoopBandwidth + '\'' +
                '}';
    }
}
