package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Component Request Bean
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ComponentRequestBean {

    private List<ComponentBean> components;

    public List<ComponentBean> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentBean> components) {
        this.components = components;
    }
}
