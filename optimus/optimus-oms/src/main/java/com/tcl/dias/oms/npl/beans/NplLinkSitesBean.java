package com.tcl.dias.oms.npl.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * This file contains the NplLinkSitesBean.java class. Bean class
 *
 * @author THAMIZHSELVI PERUMAL
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NplLinkSitesBean {

    public List<NplSite> getLink() {
        return link;
    }

    public void setLink(List<NplSite> link) {
        this.link = link;
    }

    private List<NplSite> link;


}
