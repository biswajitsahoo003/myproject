package com.tcl.dias.common.webex.beans;

/**
 * Request class for Webex product related operations.
 *
 * @author S.Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexProductPricesRequest {
	private String audioModel;
	private String paymentModel;
	private String audioType;
	private String bridge;
	private String existingCurrency;
	private String inputCurrency;
	private String leCountry;

	public WebexProductPricesRequest() {

	}

	public String getAudioModel() {
		return audioModel;
	}

	public void setAudioModel(String audioModel) {
		this.audioModel = audioModel;
	}

	public String getPaymentModel() {
		return paymentModel;
	}

	public void setPaymentModel(String paymentModel) {
		this.paymentModel = paymentModel;
	}

	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public String getExistingCurrency() {
		return existingCurrency;
	}

	public void setExistingCurrency(String existingCurrency) {
		this.existingCurrency = existingCurrency;
	}

	public String getInputCurrency() {
		return inputCurrency;
	}

	public void setInputCurrency(String inputCurrency) {
		this.inputCurrency = inputCurrency;
	}

	public String getLeCountry() {
		return leCountry;
	}

	public void setLeCountry(String leCountry) {
		this.leCountry = leCountry;
	}
}
