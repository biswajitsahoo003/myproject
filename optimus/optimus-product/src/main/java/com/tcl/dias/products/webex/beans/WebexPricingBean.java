package com.tcl.dias.products.webex.beans;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import com.tcl.dias.common.webex.beans.WebexPriceConversionBean;
import com.tcl.dias.products.webex.constants.WebexProductServiceMatrixConstant;

/**
 * Pricing bean for webex
 *
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexPricingBean {

	private Integer uID;
	private String country;
	private String phoneType;
	private Integer destId;
	private String destinationName;
	private BigDecimal nrc;
	private BigDecimal mrc;
	private BigDecimal highRate;
	private String comments;

	/**
	 * Default constructor
	 * 
	 */
	public WebexPricingBean() {
		super();
	}

	/**
	 * TO convert from webexPricingBean to webexPriceConversionBean
	 *
	 * @param webexPricingBean
	 * @return
	 */
	public static WebexPriceConversionBean toWebexPriceConversionBean(WebexPricingBean webexPricingBean,
			String existingCurrency, String inputCurrency) {
		WebexPriceConversionBean conversion = new WebexPriceConversionBean();
		conversion.setExistingCurrency(existingCurrency);
		conversion.setInputCurrency(inputCurrency);
		conversion.setuID(webexPricingBean.getuID());
		conversion.setCountry(webexPricingBean.getCountry());
		conversion.setPhoneType(webexPricingBean.getPhoneType());
		conversion.setDestId(webexPricingBean.getDestId());
		conversion.setComments(webexPricingBean.getComments());
		conversion.setDestinationName(webexPricingBean.getDestinationName());
		conversion.setMrc(webexPricingBean.getMrc());
		conversion.setNrc(webexPricingBean.getNrc());
		conversion.setHighRate(webexPricingBean.getHighRate());
		return conversion;
	}

	/**
	 * TO convert from webexPriceConversionBean to webexPricingBean
	 *
	 * @param webexPriceConversionBean
	 * @return
	 */
	public static WebexPricingBean fromWebexPriceConversionBean(WebexPriceConversionBean webexPriceConversionBean) {
		WebexPricingBean pricingBean = new WebexPricingBean();
		pricingBean.setuID(webexPriceConversionBean.getuID());
		pricingBean.setCountry(webexPriceConversionBean.getCountry());
		pricingBean.setPhoneType(webexPriceConversionBean.getPhoneType());
		pricingBean.setDestId(webexPriceConversionBean.getDestId());
		pricingBean.setComments(webexPriceConversionBean.getComments());
		pricingBean.setDestinationName(webexPriceConversionBean.getDestinationName());
		pricingBean.setMrc(webexPriceConversionBean.getMrc());
		pricingBean.setNrc(webexPriceConversionBean.getNrc());
		pricingBean.setHighRate(webexPriceConversionBean.getHighRate());
		return pricingBean;
	}

	/**
	 * Convert any view set to webex pricing bean
	 *
	 * @param priceView
	 * @return
	 */
	public static WebexPricingBean toWebexPricingBean(Map<String, Object> priceView) {
		WebexPricingBean webexPricingBean = new WebexPricingBean();
		webexPricingBean.setCountry(String.valueOf(priceView.get(WebexProductServiceMatrixConstant.COUNTRY)));
		webexPricingBean.setPhoneType(String.valueOf(priceView.get(WebexProductServiceMatrixConstant.PHONETYPE)));
		webexPricingBean.setDestId((Integer) priceView.get(WebexProductServiceMatrixConstant.DESTID));
		webexPricingBean.setDestinationName(String.valueOf(priceView.get(WebexProductServiceMatrixConstant.DESTNAME)));
		webexPricingBean.setNrc(Objects.nonNull(priceView.get(WebexProductServiceMatrixConstant.NRC))
				? new BigDecimal(String.valueOf(priceView.get(WebexProductServiceMatrixConstant.NRC)))
				: null);
		webexPricingBean.setMrc(Objects.nonNull(priceView.get(WebexProductServiceMatrixConstant.MRC))
				? new BigDecimal(String.valueOf(priceView.get(WebexProductServiceMatrixConstant.MRC)))
				: null);
		webexPricingBean.setHighRate(Objects.nonNull(priceView.get(WebexProductServiceMatrixConstant.RATE))
				? new BigDecimal(String.valueOf(priceView.get(WebexProductServiceMatrixConstant.RATE)))
				: null);
		webexPricingBean.setComments((String) priceView.get(WebexProductServiceMatrixConstant.COMMENTS));
		return webexPricingBean;
	}

	public Integer getuID() {
		return uID;
	}

	public void setuID(Integer uID) {
		this.uID = uID;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getDestId() {
		return destId;
	}

	public void setDestId(Integer destId) {
		this.destId = destId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public BigDecimal getHighRate() {
		return highRate;
	}

	public void setHighRate(BigDecimal highRate) {
		this.highRate = highRate;
	}

	public BigDecimal getNrc() {
		return nrc;
	}

	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	public BigDecimal getMrc() {
		return mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
