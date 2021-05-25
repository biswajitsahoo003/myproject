package com.tcl.dias.oms.izosdwan.pdf.beans;

import java.io.Serializable;

public class CpeBomDetailsCof  implements Serializable {

    private String techSpecification;
    private String partCode;
    private String description;

    public String getTechSpecification() {
        return techSpecification;
    }

    public void setTechSpecification(String techSpecification) {
        this.techSpecification = techSpecification;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
