package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscMultipleLESolutionBean;

/**
 * Bean for Teams DR QuoteToLe
 *
 * @author Syed Ali
 *
 */
public class QuoteToLeDetailsBean {
	private Integer quoteLeId;
	private String contractPeriod;
	private Boolean isDocusign;
	private Boolean isManualCofSigned;
	private TeamsDRSolutionBean teamsDRSolution;
	private List<GscMultipleLESolutionBean> voiceSolutions;
	private List<QuoteToLeBean> legalEntities;

	// Only for GSIP MACD case
	private Integer supplierLegalId;
	private String classification;

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public String getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public TeamsDRSolutionBean getTeamsDRSolution() {
		return teamsDRSolution;
	}

	public void setTeamsDRSolution(TeamsDRSolutionBean teamsDRSolution) {
		this.teamsDRSolution = teamsDRSolution;
	}

	public List<QuoteToLeBean> getLegalEntities() {
		return legalEntities;
	}

	public void setLegalEntities(List<QuoteToLeBean> legalEntities) {
		this.legalEntities = legalEntities;
	}

	public Boolean getDocusign() {
		return isDocusign;
	}

	public void setDocusign(Boolean docusign) {
		isDocusign = docusign;
	}

	public Boolean getManualCofSigned() {
		return isManualCofSigned;
	}

	public void setManualCofSigned(Boolean manualCofSigned) {
		isManualCofSigned = manualCofSigned;
	}

	public Integer getSupplierLegalId() {
		return supplierLegalId;
	}

	public void setSupplierLegalId(Integer supplierLegalId) {
		this.supplierLegalId = supplierLegalId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public List<GscMultipleLESolutionBean> getVoiceSolutions() {
		return voiceSolutions;
	}

	public void setVoiceSolutions(List<GscMultipleLESolutionBean> voiceSolutions) {
		this.voiceSolutions = voiceSolutions;
	}

	@Override
	public String toString() {
		return "QuoteToLeDetailsBean{" + "quoteLeId=" + quoteLeId + ", contractPeriod='" + contractPeriod + '\''
				+ ", teamsDRSolution=" + teamsDRSolution + ", gscSolutions=" + voiceSolutions + ", legalEntities="
				+ legalEntities + ", isDocusign=" + isDocusign + ", isManualCofSigned=" + isManualCofSigned
				+ ", supplierLegalId=" + supplierLegalId + ", classification='" + classification + '\'' + '}';
	}
}
