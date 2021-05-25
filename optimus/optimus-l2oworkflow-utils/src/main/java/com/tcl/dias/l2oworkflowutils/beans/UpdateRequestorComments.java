package com.tcl.dias.l2oworkflowutils.beans;

public class UpdateRequestorComments {

    private String value;
    private String quoteCode;
    private String productName;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQuoteCode() {
        return quoteCode;
    }

    public void setQuoteCode(String quoteCode) {
        this.quoteCode = quoteCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "UpdateRequestorComments{" +
                "value='" + value + '\'' +
                ", quoteCode='" + quoteCode + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}