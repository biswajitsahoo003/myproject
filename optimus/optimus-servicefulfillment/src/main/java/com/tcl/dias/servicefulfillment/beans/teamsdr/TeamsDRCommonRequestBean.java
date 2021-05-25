package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionAttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author Syed Ali.
 * @createdAt 02/02/2021, Tuesday, 13:17
 */
public class TeamsDRCommonRequestBean extends TaskDetailsBaseBean {

	private Integer id;

	private String installationCompleted;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String installationDate;

	private String mgRemotelyAccessible;

	private String gsmcTicket;

	private String firewallOpened;

	private String portOpened;

	private String uatCompleted;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String uatDate;

	private List<SolutionAttachmentBean> documentIds;

	private String configurationCompleted;

	private String configurationDate;

	private List<TeamsDRMgRequestBean> mediagateways;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInstallationCompleted() {
		return installationCompleted;
	}

	public void setInstallationCompleted(String installationCompleted) {
		this.installationCompleted = installationCompleted;
	}

	public String getInstallationDate() {
		return installationDate;
	}

	public void setInstallationDate(String installationDate) {
		this.installationDate = installationDate;
	}

	public String getMgRemotelyAccessible() {
		return mgRemotelyAccessible;
	}

	public void setMgRemotelyAccessible(String mgRemotelyAccessible) {
		this.mgRemotelyAccessible = mgRemotelyAccessible;
	}

	public String getGsmcTicket() {
		return gsmcTicket;
	}

	public void setGsmcTicket(String gsmcTicket) {
		this.gsmcTicket = gsmcTicket;
	}

	public String getFirewallOpened() {
		return firewallOpened;
	}

	public void setFirewallOpened(String firewallOpened) {
		this.firewallOpened = firewallOpened;
	}

	public String getPortOpened() {
		return portOpened;
	}

	public void setPortOpened(String portOpened) {
		this.portOpened = portOpened;
	}

	public String getUatCompleted() {
		return uatCompleted;
	}

	public void setUatCompleted(String uatCompleted) {
		this.uatCompleted = uatCompleted;
	}

	public String getUatDate() {
		return uatDate;
	}

	public void setUatDate(String uatDate) {
		this.uatDate = uatDate;
	}

	public List<SolutionAttachmentBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<SolutionAttachmentBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getConfigurationCompleted() {
		return configurationCompleted;
	}

	public void setConfigurationCompleted(String configurationCompleted) {
		this.configurationCompleted = configurationCompleted;
	}

	public String getConfigurationDate() {
		return configurationDate;
	}

	public void setConfigurationDate(String configurationDate) {
		this.configurationDate = configurationDate;
	}

	public List<TeamsDRMgRequestBean> getMediagateways() {
		return mediagateways;
	}

	public void setMediagateways(List<TeamsDRMgRequestBean> mediagateways) {
		this.mediagateways = mediagateways;
	}

	@Override
	public String toString() {
		return "TeamsDRCommonRequestBean [id=" + id + ", installationCompleted=" + installationCompleted
				+ ", installationDate=" + installationDate + ", mgRemotelyAccessible=" + mgRemotelyAccessible
				+ ", gsmcTicket=" + gsmcTicket + ", firewallOpened=" + firewallOpened + ", portOpened=" + portOpened
				+ ", uatCompleted=" + uatCompleted + ", uatDate=" + uatDate + ", documentIds=" + documentIds
				+ ", configurationCompleted=" + configurationCompleted + ", configurationDate=" + configurationDate
				+ ", mediagateways=" + mediagateways + "]";
	}

}
