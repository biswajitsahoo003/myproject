package com.tcl.dias.serviceactivation.cramer.eoriordetails.beans;

import java.util.List;

public class EorIorDependencyBean {

    private String message;
    private List<EorList> eorLists;
    private List<IorList> iorLists;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EorList> getEorLists() {
        return eorLists;
    }

    public void setEorLists(List<EorList> eorLists) {
        this.eorLists = eorLists;
    }

    public List<IorList> getIorLists() {
        return iorLists;
    }

    public void setIorLists(List<IorList> iorLists) {
        this.iorLists = iorLists;
    }

    @Override
    public String toString() {
        return "EorIorDependencyBean{" +
                "message='" + message + '\'' +
                ", eorListList=" + eorLists +
                ", iorLists=" + iorLists +
                '}';
    }
}
