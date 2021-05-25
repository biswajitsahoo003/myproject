package com.tcl.dias.servicefulfillment.beans.teamsdr;

import java.util.List;

/**
 * @author Syed Ali.
 * @createdAt 05/02/2021, Friday, 17:57
 */
public class TeamsDRMgRequestBean {
	private Integer mgId;
	private Integer attachmentId;
	private String mgRemotelyAccessible;
	private String remoteAccessAfterHardening;
	private String verifySbcOnboarded;
	private String verfiySnmsTrapsAndPolling;
	private String generateCSR;
	private String dnsForFQDN;
	private List<SerialNumberBean> serialNumberBeans;

	public Integer getMgId() {
		return mgId;
	}

	public void setMgId(Integer mgId) {
		this.mgId = mgId;
	}

	public String getMgRemotelyAccessible() {
		return mgRemotelyAccessible;
	}

	public void setMgRemotelyAccessible(String mgRemotelyAccessible) {
		this.mgRemotelyAccessible = mgRemotelyAccessible;
	}

	public String getRemoteAccessAfterHardening() {
		return remoteAccessAfterHardening;
	}

	public void setRemoteAccessAfterHardening(String remoteAccessAfterHardening) {
		this.remoteAccessAfterHardening = remoteAccessAfterHardening;
	}

	public String getVerifySbcOnboarded() {
		return verifySbcOnboarded;
	}

	public void setVerifySbcOnboarded(String verifySbcOnboarded) {
		this.verifySbcOnboarded = verifySbcOnboarded;
	}

	public String getVerfiySnmsTrapsAndPolling() {
		return verfiySnmsTrapsAndPolling;
	}

	public void setVerfiySnmsTrapsAndPolling(String verfiySnmsTrapsAndPolling) {
		this.verfiySnmsTrapsAndPolling = verfiySnmsTrapsAndPolling;
	}

	public String getGenerateCSR() {
		return generateCSR;
	}

	public void setGenerateCSR(String generateCSR) {
		this.generateCSR = generateCSR;
	}

	public String getDnsForFQDN() {
		return dnsForFQDN;
	}

	public void setDnsForFQDN(String dnsForFQDN) {
		this.dnsForFQDN = dnsForFQDN;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public List<SerialNumberBean> getSerialNumberBean() {
		return serialNumberBeans;
	}

	public void setSerialNumberBean(List<SerialNumberBean> serialNumberBeans) {
		this.serialNumberBeans = serialNumberBeans;
	}

	@Override
	public String toString() {
		return "TeamsDRMgRequestBean [mgId=" + mgId + ", attachmentId=" + attachmentId + ", mgRemotelyAccessible="
				+ mgRemotelyAccessible + ", remoteAccessAfterHardening=" + remoteAccessAfterHardening
				+ ", verifySbcOnboarded=" + verifySbcOnboarded + ", verfiySnmsTrapsAndPolling="
				+ verfiySnmsTrapsAndPolling + ", generateCSR=" + generateCSR + ", dnsForFQDN=" + dnsForFQDN
				+ ", serialNumberBeans=" + serialNumberBeans + "]";
	}

}
