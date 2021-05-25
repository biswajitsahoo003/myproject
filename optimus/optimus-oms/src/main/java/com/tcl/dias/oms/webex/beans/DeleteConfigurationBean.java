package com.tcl.dias.oms.webex.beans;

import java.util.List;

import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;

/**
 * Delete configuration bean for sending request for delete api
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class DeleteConfigurationBean {

	private String dealId;
	private List<Integer> configurationIds;
	private List<QuoteUcaasBean> ucaasQuotes;
	private List<GscQuoteConfigurationBean> gscQuoteConfigurations;

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public List<Integer> getConfigurationIds() {
		return configurationIds;
	}

	public void setConfigurationIds(List<Integer> configurationIds) {
		this.configurationIds = configurationIds;
	}

	public List<QuoteUcaasBean> getUcaasQuotes() {
		return ucaasQuotes;
	}

	public void setUcaasQuotes(List<QuoteUcaasBean> ucaasQuotes) {
		this.ucaasQuotes = ucaasQuotes;
	}

	public List<GscQuoteConfigurationBean> getGscQuoteConfigurations() {
		return gscQuoteConfigurations;
	}

	public void setGscQuoteConfigurations(List<GscQuoteConfigurationBean> gscQuoteConfigurations) {
		this.gscQuoteConfigurations = gscQuoteConfigurations;
	}

	@Override
	public String toString() {
		return "DeleteConfigurationBean{" + "dealId='" + dealId + '\'' + ", configurationIds=" + configurationIds
				+ ", ucaasQuotes=" + ucaasQuotes + ", gscQuoteConfigurations=" + gscQuoteConfigurations + '}';
	}
}
