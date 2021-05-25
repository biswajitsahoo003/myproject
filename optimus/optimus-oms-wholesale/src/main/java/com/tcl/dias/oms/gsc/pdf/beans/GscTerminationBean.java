package com.tcl.dias.oms.gsc.pdf.beans;

/**
 * Gsc termination data format bean
 *
 * @author PRABUBALSUBRAMANIAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscTerminationBean {

    private String terminationName;
    private String terminationRate;
    private String phoneType;
    private String terminationId;
    private String comments;

    public String getTerminationName() {
        return terminationName;
    }

    public void setTerminationName(String terminationName) {
        this.terminationName = terminationName;
    }

    public String getTerminationRate() {
        return terminationRate;
    }

    public void setTerminationRate(String terminationRate) {
        this.terminationRate = terminationRate;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getTerminationId() {
        return terminationId;
    }

    public void setTerminationId(String terminationId) {
        this.terminationId = terminationId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
