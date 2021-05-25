package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;

/**
 * 
 * This bean binds all the price info for the whole quote
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SolutionPricingDetailsBean implements Serializable{
	private List<ProductPricingDetailsBean> productPricingDetailsBeans;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal tcv;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal arc;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal nrc;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal mrc;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal tcvMrc;
	public List<ProductPricingDetailsBean> getProductPricingDetailsBeans() {
		return productPricingDetailsBeans;
	}
	public void setProductPricingDetailsBeans(List<ProductPricingDetailsBean> productPricingDetailsBeans) {
		this.productPricingDetailsBeans = productPricingDetailsBeans;
	}
	public BigDecimal getTcv() {
		return tcv;
	}
	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}
	public BigDecimal getArc() {
		return arc;
	}
	public void setArc(BigDecimal arc) {
		this.arc = arc;
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

	public BigDecimal getTcvMrc() {
		return tcvMrc;
	}

	public void setTcvMrc(BigDecimal tcvMrc) {
		this.tcvMrc = tcvMrc;
	}
}
