package com.tcl.dias.oms.gsc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Quote related pricing information and its configuration
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscQuotePriceBean {

	private Integer id;
	private String name;
	private String productName;
	private String accessType;
	private Double arc;
	private Double mrc;
	private Double nrc;
	private Double tcv;
	private List<GscConfigurationPriceBean> gscConfigPrices = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public List<GscConfigurationPriceBean> getGscConfigPrices() {
		return gscConfigPrices;
	}

	public void setGscConfigPrices(List<GscConfigurationPriceBean> gscConfigPrices) {
		this.gscConfigPrices = gscConfigPrices;
	}

}
