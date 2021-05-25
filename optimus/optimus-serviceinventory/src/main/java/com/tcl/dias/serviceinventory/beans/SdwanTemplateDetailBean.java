package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Bean for fetching details of a particular SDWAN Template
 * 
 * @author Srinivasa Raghavan
 */
public class SdwanTemplateDetailBean {

    private List<Integer> templateIds;
    private String templateName;
    private String templateAlias;
    private Integer associatedSitesCount;
    private List<SdwanCpeInfoBean> associatedCpe;
	private List<SdwanPolicyListBean> sdwanPolicies;
    private List<String> associatedSites;
    private List<TemplateSiteDetails> templateSiteDetails;
    private String timestamp;

    public SdwanTemplateDetailBean() {
    }

    public String getTemplateAlias() {
        return templateAlias;
    }

    public void setTemplateAlias(String templateAlias) {
        this.templateAlias = templateAlias;
    }

    public Integer getAssociatedSitesCount() {
        return associatedSitesCount;
    }

    public void setAssociatedSitesCount(Integer associatedSitesCount) {
        this.associatedSitesCount = associatedSitesCount;
    }

    public List<SdwanCpeInfoBean> getAssociatedCpe() {
        return associatedCpe;
    }

    public void setAssociatedCpe(List<SdwanCpeInfoBean> associatedCpe) {
        this.associatedCpe = associatedCpe;
    }

	public List<SdwanPolicyListBean> getSdwanPolicies() {
		return sdwanPolicies;
	}

	public void setSdwanPolicies(List<SdwanPolicyListBean> sdwanPolicies) {
		this.sdwanPolicies = sdwanPolicies;
	}

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<String> getAssociatedSites() {
        return associatedSites;
    }

    public void setAssociatedSites(List<String> associatedSites) {
        this.associatedSites = associatedSites;
    }
    public List<TemplateSiteDetails> getTemplateSiteDetails() {
		return templateSiteDetails;
	}

	public void setTemplateSiteDetails(List<TemplateSiteDetails> templateSiteDetails) {
		this.templateSiteDetails = templateSiteDetails;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

    public List<Integer> getTemplateIds() {
        return templateIds;
    }

    public void setTemplateIds(List<Integer> templateIds) {
        this.templateIds = templateIds;
    }

    @Override
	public String toString() {
		return "SdwanTemplateDetailBean [templateName=" + templateName + ", templateAlias=" + templateAlias
				+ ", associatedSitesCount=" + associatedSitesCount + ", associatedCpe=" + associatedCpe
				+ ", sdwanPolicies=" + sdwanPolicies + ", associatedSites=" + associatedSites + ", templateSiteDetails="
				+ templateSiteDetails + "timestamp" + timestamp + "]";
	}
}
