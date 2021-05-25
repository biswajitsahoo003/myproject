package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.SolutionAttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * @author Syed Ali.
 * @createdAt 08/02/2021, Monday, 10:50
 */
public class TeamsDRLLDAndMigrationBean extends TaskDetailsBaseBean {
	private List<SolutionAttachmentBean> lldDocumentIds;
	private List<SolutionAttachmentBean> migrationDocumentIds;
	private List<SolutionAttachmentBean> supportingDocumentIds;
	private String securityCertificateName;
	private List<SolutionAttachmentBean> certificateDocumentIds;

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

	public List<SolutionAttachmentBean> getCertificateDocumentIds() {
		return certificateDocumentIds;
	}

	public void setCertificateDocumentIds(List<SolutionAttachmentBean> certificateDocumentIds) {
		this.certificateDocumentIds = certificateDocumentIds;
	}

	@Override
	public String toString() {
		return "TeamsDRLLDAndMigrationBean{" + "lldDocumentIds=" + lldDocumentIds + ", migrationDocumentIds="
				+ migrationDocumentIds + ", supportingDocumentIds=" + supportingDocumentIds
				+ ", securityCertificateName='" + securityCertificateName + '\'' + ", certificateDocumentIds="
				+ certificateDocumentIds + '}';
	}
}
