package com.tcl.dias.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.customer.bean.GstnInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This file Contains customerContracting Entity information
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerContractingAddressInfo {

    private Integer locationId;
    private AddressDetail address;
    private Integer isDefault;
    private List<GstnInfo> gstnInfoList=new ArrayList<>();

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public AddressDetail getAddress() {
        return address;
    }

    public void setAddress(AddressDetail address) {
        this.address = address;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public List<GstnInfo> getGstnInfoList() {
        return gstnInfoList;
    }

    public void setGstnInfoList(List<GstnInfo> gstnInfoList) {
        this.gstnInfoList = gstnInfoList;
    }
}
