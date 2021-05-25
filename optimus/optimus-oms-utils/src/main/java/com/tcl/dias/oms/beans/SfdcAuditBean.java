package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * This file contains the SfdcAuditBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SfdcAuditBean {

	private SfdcAttr createOpty;
	private SfdcAttr createProduct;
	private SfdcAttr updateProductPrice;
	private SfdcAttr proposalSent;
	private SfdcAttr verbalAggrement;
	private SfdcAttr updateSite;
	private SfdcAttr cofWonReceived;
	private Boolean showRetrigger;
	private Boolean activeStatus;
	private List<SfdcErrorAudit> sfdcErrorAudit;
	private List<SfdcFullAudit> sfdcFullAudit;

	public SfdcAttr getCreateOpty() {
		return createOpty;
	}

	public void setCreateOpty(SfdcAttr createOpty) {
		this.createOpty = createOpty;
	}

	public SfdcAttr getCreateProduct() {
		return createProduct;
	}

	public void setCreateProduct(SfdcAttr createProduct) {
		this.createProduct = createProduct;
	}

	public SfdcAttr getUpdateProductPrice() {
		return updateProductPrice;
	}

	public void setUpdateProductPrice(SfdcAttr updateProductPrice) {
		this.updateProductPrice = updateProductPrice;
	}

	public SfdcAttr getProposalSent() {
		return proposalSent;
	}

	public void setProposalSent(SfdcAttr proposalSent) {
		this.proposalSent = proposalSent;
	}

	public SfdcAttr getVerbalAggrement() {
		return verbalAggrement;
	}

	public void setVerbalAggrement(SfdcAttr verbalAggrement) {
		this.verbalAggrement = verbalAggrement;
	}

	public SfdcAttr getUpdateSite() {
		return updateSite;
	}

	public void setUpdateSite(SfdcAttr updateSite) {
		this.updateSite = updateSite;
	}

	public SfdcAttr getCofWonReceived() {
		return cofWonReceived;
	}

	public void setCofWonReceived(SfdcAttr cofWonReceived) {
		this.cofWonReceived = cofWonReceived;
	}

	public Boolean getShowRetrigger() {
		return showRetrigger;
	}

	public void setShowRetrigger(Boolean showRetrigger) {
		this.showRetrigger = showRetrigger;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public List<SfdcErrorAudit> getSfdcErrorAudit() {
		return sfdcErrorAudit;
	}

	public void setSfdcErrorAudit(List<SfdcErrorAudit> sfdcErrorAudit) {
		this.sfdcErrorAudit = sfdcErrorAudit;
	}

	public List<SfdcFullAudit> getSfdcFullAudit() {
		return sfdcFullAudit;
	}

	public void setSfdcFullAudit(List<SfdcFullAudit> sfdcFullAudit) {
		this.sfdcFullAudit = sfdcFullAudit;
	}

	@Override
	public String toString() {
		return "SfdcAuditBean [createOpty=" + createOpty + ", createProduct=" + createProduct + ", updateProductPrice="
				+ updateProductPrice + ", proposalSent=" + proposalSent + ", verbalAggrement=" + verbalAggrement
				+ ", updateSite=" + updateSite + ", cofWonReceived=" + cofWonReceived + ", showRetrigger="
				+ showRetrigger + ", activeStatus=" + activeStatus + ", sfdcErrorAudit=" + sfdcErrorAudit
				+ ", sfdcFullAudit=" + sfdcFullAudit + "]";
	}

}
