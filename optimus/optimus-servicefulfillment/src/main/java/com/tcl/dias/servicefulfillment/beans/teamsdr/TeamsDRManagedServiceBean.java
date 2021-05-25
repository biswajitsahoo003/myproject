package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * TeamsDR Managed services details bean
 *
 * @author Syed Ali.
 * @createdAt 18/02/2021, Thursday, 14:27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamsDRManagedServiceBean extends TaskDetailsBaseBean {
	private String delegateAdminAccess;
	private String tenantLoginName;
	private String tenantLoginPassWord;
	private List<TeamsDRMSSiteBean> siteDetails;

	public TeamsDRManagedServiceBean() {
	}

	public String getDelegateAdminAccess() {
		return delegateAdminAccess;
	}

	public void setDelegateAdminAccess(String delegateAdminAccess) {
		this.delegateAdminAccess = delegateAdminAccess;
	}

	public String getTenantLoginName() {
		return tenantLoginName;
	}

	public void setTenantLoginName(String tenantLoginName) {
		this.tenantLoginName = tenantLoginName;
	}

	public String getTenantLoginPassWord() {
		return tenantLoginPassWord;
	}

	public void setTenantLoginPassWord(String tenantLoginPassWord) {
		this.tenantLoginPassWord = tenantLoginPassWord;
	}

	public List<TeamsDRMSSiteBean> getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(List<TeamsDRMSSiteBean> siteDetails) {
		this.siteDetails = siteDetails;
	}

	@Override
	public String toString() {
		return "TeamsDRManagedServiceBean [delegateAdminAccess=" + delegateAdminAccess + ", tenantLoginName=" + tenantLoginName + ", tenantLoginPassWord=" + tenantLoginPassWord + "]";
	}

}
