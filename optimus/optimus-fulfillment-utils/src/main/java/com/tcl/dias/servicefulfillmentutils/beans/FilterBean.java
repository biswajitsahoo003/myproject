
package com.tcl.dias.servicefulfillmentutils.beans;

public class FilterBean {

    private Byte isDefault;
    private String filterName;
    private Preference preference;

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(byte aDefault) {
        isDefault = aDefault;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

}
