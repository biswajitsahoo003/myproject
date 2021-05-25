package com.tcl.dias.location.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * This class is used to hold the response for bulk upload of NPL 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class BulkUploadNplResponse implements Serializable {
	private static final long serialVersionUID = -7410988290187762659L;
	
	private List<SiteDetailBean> link;

	public List<SiteDetailBean> getLink() {
		return link;
	}

	public void setLink(List<SiteDetailBean> link) {
		this.link = link;
	}
	

}
