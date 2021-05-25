package com.tcl.dias.networkaugmentation.beans;


import javax.persistence.Column;

public class ProcessAccessRightsBean {

    String process_name;

    String menuHeaderName;

    String menuDisplayName;

    String processDisplayName;

    String groupName;

    Integer processId;

    String accessRight;

    String toOrgOnly;

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public String getMenuHeaderName() {
        return menuHeaderName;
    }

    public void setMenuHeaderName(String menuHeaderName) {
        this.menuHeaderName = menuHeaderName;
    }

    public String getMenuDisplayName() {
        return menuDisplayName;
    }

    public void setMenuDisplayName(String menuDisplayName) {
        this.menuDisplayName = menuDisplayName;
    }

    public String getProcessDisplayName() {
        return processDisplayName;
    }

    public void setProcessDisplayName(String processDisplayName) {
        this.processDisplayName = processDisplayName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    public String getToOrgOnly() {
        return toOrgOnly;
    }

    public void setToOrgOnly(String toOrgOnly) {
        this.toOrgOnly = toOrgOnly;
    }
}
