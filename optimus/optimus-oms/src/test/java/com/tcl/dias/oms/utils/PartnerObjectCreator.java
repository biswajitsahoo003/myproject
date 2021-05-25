package com.tcl.dias.oms.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tcl.dias.oms.beans.PartnerEntityRequest;
import com.tcl.dias.oms.partner.beans.dnb.DnBRequestBean;
import com.tcl.dias.oms.partner.beans.dnb.DnbLeDetailsBean;

@Service
public class PartnerObjectCreator {


    public List<DnBRequestBean> createDnbRequestBeanList() {
        List<DnBRequestBean> dnBRequestBeansList = new ArrayList<DnBRequestBean>();
        dnBRequestBeansList.add(createDnBRequestBean());
        dnBRequestBeansList.add(createDnBRequestBean());
        return dnBRequestBeansList;
    }


    public DnBRequestBean createDnBRequestBean() {
        DnBRequestBean dnBRequestBean = new DnBRequestBean();
        dnBRequestBean.setCustomerLeName("testlename");
        dnBRequestBean.setCountryName("testCountry");
        dnBRequestBean.setCountryCode("22");
        dnBRequestBean.setClassification("testclassification");
        return dnBRequestBean;
    }

    public List<DnbLeDetailsBean> createDnBLeDetailsList() {
        List<DnbLeDetailsBean> dnbLeDetailsBeansList = new ArrayList<DnbLeDetailsBean>();
        dnbLeDetailsBeansList.add(createDnBLeDetailsBean());
        dnbLeDetailsBeansList.add(createDnBLeDetailsBean());
        return dnbLeDetailsBeansList;
    }


    public DnbLeDetailsBean createDnBLeDetailsBean() {
        DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
        dnbLeDetailsBean.setDunsId(22);
        dnbLeDetailsBean.setEntityName("testEntity");
        dnbLeDetailsBean.setOptimusCustomerLeId(23);
        dnbLeDetailsBean.setVerified(true);
        return dnbLeDetailsBean;
    }

    public PartnerEntityRequest createPartnerEntityRequestObject() {
        PartnerEntityRequest partnerEntityRequest = new PartnerEntityRequest();
        partnerEntityRequest.setCountry("testCountry");
        partnerEntityRequest.setCustomerName("testName");
        partnerEntityRequest.setCustomerWebsite("www.testwebsite.com");
//        partnerEntityRequest.setErfPartnerId(1234);
//        partnerEntityRequest.setIndustry("testIndustry");
//        partnerEntityRequest.setIndustrySubType("testIndustrysubtype");
        partnerEntityRequest.setRegisteredAddressCity("testCity");
//        partnerEntityRequest.setRegisteredAddressStateProvince("testAddress");
        partnerEntityRequest.setRegisteredAddressStreet("testStreetName");
        partnerEntityRequest.setRegisteredAddressZipPostalCode("34434");
        partnerEntityRequest.setRegistrationNumber("32323");
//        partnerEntityRequest.setSubIndustry("testSubIndustry");
//        partnerEntityRequest.setTypeOfBusiness("testBusiness");
        return partnerEntityRequest;
    }


}
