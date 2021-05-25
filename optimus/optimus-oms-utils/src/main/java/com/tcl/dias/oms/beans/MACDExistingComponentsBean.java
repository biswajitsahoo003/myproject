package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Bean class for MACDExistingComponentsBean
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MACDExistingComponentsBean {

    private String serviceId;

    private List<Map> existingComponents= new ArrayList<>();

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<Map> getExistingComponents() {
        return existingComponents;
    }

    public void setExistingComponents(List<Map> existingComponents) {
        this.existingComponents = existingComponents;
    }
}
