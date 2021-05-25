package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class LLDAndMigrationBean extends TaskDetailsBaseBean {

    private List<SolutionAttachmentBean> lldDocumentIds;
    private List<SolutionAttachmentBean> migrationDocumentIds;
    private List<SolutionAttachmentBean> supportingDocumentIds;

    // For teamsdr..
	private String securityCertificateName;
	private List<SolutionAttachmentBean> teamsDRDocumentIds;

    public List<SolutionAttachmentBean> getLldDocumentIds() {
        return lldDocumentIds;
    }

    public void setLldDocumentIds(List<SolutionAttachmentBean> lldDocumentIds) {
        this.lldDocumentIds = lldDocumentIds;
    }

    public List<SolutionAttachmentBean> getMigrationDocumentIds() {
        return migrationDocumentIds;
    }

    public void setMigrationDocumentIds(List<SolutionAttachmentBean> migrationDocumentIds) {
        this.migrationDocumentIds = migrationDocumentIds;
    }

	public List<SolutionAttachmentBean> getSupportingDocumentIds() {
		return supportingDocumentIds;
	}

	public void setSupportingDocumentIds(List<SolutionAttachmentBean> supportingDocumentIds) {
		this.supportingDocumentIds = supportingDocumentIds;
	}

	public String getSecurityCertificateName() {
		return securityCertificateName;
	}

	public void setSecurityCertificateName(String securityCertificateName) {
		this.securityCertificateName = securityCertificateName;
	}

	public List<SolutionAttachmentBean> getTeamsDRDocumentIds() {
		return teamsDRDocumentIds;
	}

	public void setTeamsDRDocumentIds(List<SolutionAttachmentBean> teamsDRDocumentIds) {
		this.teamsDRDocumentIds = teamsDRDocumentIds;
	}
}
