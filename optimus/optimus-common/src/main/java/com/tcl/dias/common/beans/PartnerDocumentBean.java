package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean for Document related to partner
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerDocumentBean {

    private Integer id;
    private String name;
    private String urlPath;

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


    public String getUrlPath()
    {
        return urlPath;
    }


    public void setUrlPath(String urlPath)
    {
        this.urlPath = urlPath;
    }
}
