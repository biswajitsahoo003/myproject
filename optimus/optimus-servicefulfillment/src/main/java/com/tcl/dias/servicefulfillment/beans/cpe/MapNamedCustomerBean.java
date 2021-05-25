package com.tcl.dias.servicefulfillment.beans.cpe;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class MapNamedCustomerBean extends TaskDetailsBaseBean {
    private String isNamedCustomer;
    private String ciscoCustomerName;

    public String getIsNamedCustomer() {
        return isNamedCustomer;
    }

    public void setIsNamedCustomer(String isNamedCustomer) {
        this.isNamedCustomer = isNamedCustomer;
    }

    public String getCiscoCustomerName() {
        return ciscoCustomerName;
    }

    public void setCiscoCustomerName(String ciscoCustomerName) {
        this.ciscoCustomerName = ciscoCustomerName;
    }

    @Override
    public String toString() {
        return "MapNamedCustomerBean{" +
                "isNamedCustomer='" + isNamedCustomer + '\'' +
                ", ciscoCustomerName='" + ciscoCustomerName + '\'' +
                '}';
    }
}
