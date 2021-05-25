
package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class FilterTypeBean {

    private String type;
    private List<FilterBean> filters;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FilterBean> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterBean> filters) {
        this.filters = filters;
    }

}
