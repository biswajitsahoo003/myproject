package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.gsc.beans.GscOrderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for dashboard details for product family
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashBoardFamilyBean {

    private String familyName;


    private Long provisionedSiteCount;

    private Long totalCount;

    private List<DashBoardSiteBean> siteBeans;

    private List<DashBoardSiteBean> provisionedSiteBeans;

    private List<OrderIllSiteBean> orderIllSiteBeans;

    private List<GscOrderBean> gscOrderBean;

    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName the familyName to set
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * @return the siteBeans
     */
    public List<DashBoardSiteBean> getSiteBeans() {
        if (siteBeans == null) {
            siteBeans = new ArrayList<>();
        }
        return siteBeans;
    }

    /**
     * @param siteBeans the siteBeans to set
     */
    public void setSiteBeans(List<DashBoardSiteBean> siteBeans) {
        this.siteBeans = siteBeans;
    }


    /**
     * hashCode
     *
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((familyName == null) ? 0 : familyName.hashCode());
        return result;
    }

    /**
     * equals
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DashBoardFamilyBean other = (DashBoardFamilyBean) obj;
        if (familyName == null) {
            if (other.familyName != null)
                return false;
        } else if (!familyName.equals(other.familyName))
            return false;
        return true;
    }

    /**
     * @return the provisionedSiteBeans
     */
    public List<DashBoardSiteBean> getProvisionedSiteBeans() {
        if (provisionedSiteBeans == null) {
            provisionedSiteBeans = new ArrayList<>();
        }
        return provisionedSiteBeans;
    }

    /**
     * @param provisionedSiteBeans the provisionedSiteBeans to set
     */
    public void setProvisionedSiteBeans(List<DashBoardSiteBean> provisionedSiteBeans) {
        this.provisionedSiteBeans = provisionedSiteBeans;
    }

    /**
     * @return the provisionedSiteCount
     */
    public Long getProvisionedSiteCount() {
        return provisionedSiteCount;
    }

    /**
     * @param provisionedSiteCount the provisionedSiteCount to set
     */
    public void setProvisionedSiteCount(Long provisionedSiteCount) {
        this.provisionedSiteCount = provisionedSiteCount;
    }

    /**
     * @return the totalCount
     */
    public Long getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the orderIllSiteBeans
     */
    public List<OrderIllSiteBean> getOrderIllSiteBeans() {
        return orderIllSiteBeans;
    }

    /**
     * @param orderIllSiteBeans the orderIllSiteBeans to set
     */
    public void setOrderIllSiteBeans(List<OrderIllSiteBean> orderIllSiteBeans) {
        this.orderIllSiteBeans = orderIllSiteBeans;
    }

    public List<GscOrderBean> getGscOrderBean() {
        return gscOrderBean;
    }

    public void setGscOrderBean(List<GscOrderBean> gscOrderBean) {
        this.gscOrderBean = gscOrderBean;
    }
}
