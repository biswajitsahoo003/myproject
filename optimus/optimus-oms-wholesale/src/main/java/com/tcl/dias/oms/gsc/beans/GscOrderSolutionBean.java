package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Product solutions for Order
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOrderSolutionBean {

    private Integer solutionId;
    private String offeringName;
    private String solutionCode;
    private String accessType;
    private String productName;
    private List<GscOrderBean> gscOrders;

    /**
     * @return the solutionId
     */
    public Integer getSolutionId() {
        return solutionId;
    }

    /**
     * @param solutionId the solutionId to set
     */
    public void setSolutionId(Integer solutionId) {
        this.solutionId = solutionId;
    }

    /**
     * @return the gscOrders
     */
    public List<GscOrderBean> getGscOrders() {
        return gscOrders;
    }

    /**
     * @param gscOrders the gscOrders to set
     */
    public void setGscOrders(List<GscOrderBean> gscOrders) {
        this.gscOrders = gscOrders;
    }

    /**
     * @return the offeringName
     */
    public String getOfferingName() {
        return offeringName;
    }

    /**
     * @param offeringName the offeringName to set
     */
    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    /**
     * @return the solutionCode
     */
    public String getSolutionCode() {
        return solutionCode;
    }

    /**
     * @param solutionCode the solutionCode to set
     */
    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    /**
     * @return the accessType
     */
    public String getAccessType() {
        return accessType;
    }

    /**
     * @param accessType the accessType to set
     */
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
