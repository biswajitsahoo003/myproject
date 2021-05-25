package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class CramerResponseBean {
    private String status;
    private String message;
    private List<JsonNode> list;

    private String errorCode;
    private String errorShortDescription;
    private String errorLongDescription;
    private String module;

    private Integer id;
    private String name;
    private String alias1;
    private String alias2;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<JsonNode> getList() {
        return list;
    }

    public void setList(List<JsonNode> list) {
        this.list = list;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorShortDescription() {
        return errorShortDescription;
    }

    public void setErrorShortDescription(String errorShortDescription) {
        this.errorShortDescription = errorShortDescription;
    }

    public String getErrorLongDescription() {
        return errorLongDescription;
    }

    public void setErrorLongDescription(String errorLongDescription) {
        this.errorLongDescription = errorLongDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias1() {
        return alias1;
    }

    public void setAlias1(String alias1) {
        this.alias1 = alias1;
    }

    public String getAlias2() {
        return alias2;
    }

    public void setAlias2(String alias2) {
        this.alias2 = alias2;
    }

    @Override
    public String toString() {
        return "CramerResponseBean{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", list=" + list +
                ", errorCode='" + errorCode + '\'' +
                ", errorShortDescription='" + errorShortDescription + '\'' +
                ", errorLongDescription='" + errorLongDescription + '\'' +
                ", module='" + module + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", alias1='" + alias1 + '\'' +
                ", alias2='" + alias2 + '\'' +
                '}';
    }
}
