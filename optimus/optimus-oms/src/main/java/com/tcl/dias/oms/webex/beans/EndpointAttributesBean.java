package com.tcl.dias.oms.webex.beans;

import com.tcl.dias.oms.beans.OrderProductComponentBean;

import java.util.List;

/*
 *
 *  @author Syed Ali
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 *
 */
public class EndpointAttributesBean {
    private Integer id;
    private String siteCode;
    private Integer endpointLocationId;
    private List<OrderProductComponentBean> components;

    public EndpointAttributesBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Integer getEndpointLocationId() {
        return endpointLocationId;
    }

    public void setEndpointLocationId(Integer endpointLocationId) {
        this.endpointLocationId = endpointLocationId;
    }

    public List<OrderProductComponentBean> getComponents() {
        return components;
    }

    public void setComponents(List<OrderProductComponentBean> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "EndpointAttributesBean{" +
                "id=" + id +
                ", siteCode='" + siteCode + '\'' +
                ", endpointLocationId=" + endpointLocationId +
                ", components=" + components +
                '}';
    }
}
