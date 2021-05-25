
package com.tcl.dias.servicefulfillment.beans;

public class ClrCreationFailureResponseBean {

    private String errorCode;
    private String errorLongDescription;
    private String errorShortDescription;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorLongDescription() {
        return errorLongDescription;
    }

    public void setErrorLongDescription(String errorLongDescription) {
        this.errorLongDescription = errorLongDescription;
    }

    public String getErrorShortDescription() {
        return errorShortDescription;
    }

    public void setErrorShortDescription(String errorShortDescription) {
        this.errorShortDescription = errorShortDescription;
    }

}
