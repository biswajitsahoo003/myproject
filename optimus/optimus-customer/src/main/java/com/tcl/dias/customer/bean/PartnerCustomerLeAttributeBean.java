package com.tcl.dias.customer.bean;

import com.tcl.dias.customer.entity.entities.PartnerLeAttributeValue;

/**
 * Class for PartnerCustomerLeAttribute values
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerCustomerLeAttributeBean {

    private Integer id;
    private Integer partnerLeId;
    private String attributeName;
    private String attributeValue;

    public PartnerCustomerLeAttributeBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Integer getPartnerLeId() {
        return partnerLeId;
    }

    public void setPartnerLeId(Integer partnerLeId) {
        this.partnerLeId = partnerLeId;
    }

    public static PartnerCustomerLeAttributeBean fromPartnerLeAttributeBean(PartnerLeAttributeValue partnerLeAttributeValue){
        PartnerCustomerLeAttributeBean partnerCustomerLeAttributeBean = new PartnerCustomerLeAttributeBean();
        partnerCustomerLeAttributeBean.setId(partnerLeAttributeValue.getId());
        partnerCustomerLeAttributeBean.setPartnerLeId(partnerLeAttributeValue.getPartnerLegalEntity().getId());
        partnerCustomerLeAttributeBean.setAttributeName(partnerLeAttributeValue.getMstLeAttribute().getName());
        partnerCustomerLeAttributeBean.setAttributeValue(partnerLeAttributeValue.getAttributeValues());
        return partnerCustomerLeAttributeBean;
    }
}