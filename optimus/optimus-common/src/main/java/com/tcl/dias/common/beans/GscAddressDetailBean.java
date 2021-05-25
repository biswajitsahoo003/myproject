package com.tcl.dias.common.beans;

import com.tcl.dias.common.beans.DomesticVoiceAddressDetail;

import java.util.List;

/**
 * <Comments>
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GscAddressDetailBean {

    private List<DomesticVoiceAddressDetail> domesticVoiceAddressDetailList;

    public List<DomesticVoiceAddressDetail> getDomesticVoiceAddressDetailsList() {
        return domesticVoiceAddressDetailList;
    }

    public void setDomesticVoiceAddressDetailsList(List<DomesticVoiceAddressDetail> domesticVoiceAddressDetailList) {
        this.domesticVoiceAddressDetailList = domesticVoiceAddressDetailList;
    }
}
