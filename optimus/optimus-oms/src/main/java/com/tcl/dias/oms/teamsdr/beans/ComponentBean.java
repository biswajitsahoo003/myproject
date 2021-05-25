package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.tcl.dias.oms.beans.AttributeDetail;

/**
 * Component Bean for MG.
 */
public class ComponentBean {
    private String name;
    private List<AttributeDetail> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeDetail> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDetail> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ComponentBean{" + "name='" + name + '\'' + ", attributes=" + attributes + '}';
    }
}
