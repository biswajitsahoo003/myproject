package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Partner Details Bean
 *
 * @author Arunmani
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerLeCustomerLe{


    private String partnerName;
    private String partnerCuid;
    private Set<PartnerLegalEntityBean> customerLegalEnties;

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerCuid() {
        return partnerCuid;
    }

    public void setPartnerCuid(String partnerCuid) {
        this.partnerCuid = partnerCuid;
    }

    public Set<PartnerLegalEntityBean> getCustomerLegalEnties() {
        return customerLegalEnties;
    }

    public void setCustomerLegalEnties(Set<PartnerLegalEntityBean> customerLegalEnties) {
        this.customerLegalEnties = customerLegalEnties;
    }


}
