package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.sfdc.response.bean.AttributesResponseBean;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EtcRecordListBean {

    private AttributesResponseBean attributes;
    private String id;
    private String name;
    private String childOptyId;

    public AttributesResponseBean getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesResponseBean attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChildOptyId() {
        return childOptyId;
    }

    public void setChildOptyId(String childOptyId) {
        this.childOptyId = childOptyId;
    }
}
