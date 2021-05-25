package com.tcl.dias.oms.partner.beans.dnb;

/**
 * DnB Le Details Bean
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class DnbLeDetailsBean {
    private String entityName;
    private Integer dunsId;
    private boolean isVerified;
    private Integer optimusCustomerLeId;
    private String source;
    private Integer tempCustomerLeId;
    private String type;
    private Integer optimusCustomerId;
    private String currency;
    private  String fySegmentation;
    private boolean isMappedEndCustomer=false;

    public Integer getTempCustomerLeId() {
        return tempCustomerLeId;
    }

    public void setTempCustomerLeId(Integer tempCustomerLeId) {
        this.tempCustomerLeId = tempCustomerLeId;
    }
    public Integer getOptimusCustomerLeId() {
        return optimusCustomerLeId;
    }

    public void setOptimusCustomerLeId(Integer optimusCustomerLeId) {
        this.optimusCustomerLeId = optimusCustomerLeId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getDunsId() {
        return dunsId;
    }

    public void setDunsId(Integer dunsId) {
        this.dunsId = dunsId;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getOptimusCustomerId() {
        return optimusCustomerId;
    }

    public void setOptimusCustomerId(Integer optimusCustomerId) {
        this.optimusCustomerId = optimusCustomerId;
    }


    public String getFySegmentation() {
        return fySegmentation;
    }

    public void setFySegmentation(String fySegmentation) {
        this.fySegmentation = fySegmentation;
    }


    public boolean isMappedEndCustomer() {
        return isMappedEndCustomer;
    }

    public void setMappedEndCustomer(boolean mappedEndCustomer) {
        isMappedEndCustomer = mappedEndCustomer;
    }
}
