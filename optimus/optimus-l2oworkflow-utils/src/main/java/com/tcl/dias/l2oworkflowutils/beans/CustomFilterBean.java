
package com.tcl.dias.l2oworkflowutils.beans;

import java.util.List;

public class CustomFilterBean {

    private List<FilterTypeBean> filterTypes;
    private String username;
    private String groupName;

    public CustomFilterBean() {
    }

    public CustomFilterBean(String userName) {
        this.username=userName;
    }

    public List<FilterTypeBean> getFilterTypes() {
        return filterTypes;
    }

    public void setFilterTypes(List<FilterTypeBean> filterTypes) {
        this.filterTypes = filterTypes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
