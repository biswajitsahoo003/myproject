package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.networkaugment.entity.entities.MstTaskDef;

/**
 * Bean class for MstTaskDef
 *
 * @author VishAwas
 */
public class MstTaskDefBean {
    private String assignedGroup;
    private String key;
    private String name;

    public MstTaskDefBean(MstTaskDef mstTaskDef) {
        this.assignedGroup = mstTaskDef.getAssignedGroup();
        this.key = mstTaskDef.getKey();
        this.name = mstTaskDef.getName();
    }

    public String getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(String assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
