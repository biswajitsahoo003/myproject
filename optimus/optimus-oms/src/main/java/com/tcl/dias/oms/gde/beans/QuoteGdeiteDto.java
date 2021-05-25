package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.dto.QuoteProductComponentDto;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;

/**
 * This file contains the IllSiteDto.java class. Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class QuoteGdeiteDto implements Serializable {

	private static final long serialVersionUID = -8682500826159729166L;

	private Integer id;

	private Integer createdBy;

	private Timestamp createdTime;

	private String imageUrl;

	private Byte feasibility;

	private Double mrc;

	private Double nrc;

	private QuoteProductComponentDto quoteProductComponentDto;

	private List<QuoteProductComponentDto> quoteProductComponentDtos;

	private Integer erfLocSiteBLocationId;

	private String erfLocSiteBSiteCode;

	private Integer erfLocSiteALocationId;

	private String erfLocSiteASiteCode;

	private String erfLrSolutionId;

	private String bandwidth;

	public QuoteGdeiteDto() {
		// DO NOTHING
	}

	/**
	 * 
	 * @param site
	 */
	public QuoteGdeiteDto(QuoteIllSite site) {
		super();
		this.id = site.getId();
		this.imageUrl = site.getImageUrl();
		this.feasibility = site.getFeasibility() != null ? site.getFeasibility() : null;
		this.erfLocSiteALocationId = site.getErfLocSiteaLocationId();
		this.erfLocSiteBLocationId = site.getErfLocSitebLocationId();
		this.mrc = site.getMrc();
		this.nrc = site.getNrc();
		this.feasibility = site.getFeasibility();
		this.erfLocSiteASiteCode = site.getErfLocSiteaSiteCode();
		this.erfLocSiteBSiteCode = site.getErfLocSitebSiteCode();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Byte getFeasibility() {
		return feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
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

	public QuoteProductComponentDto getQuoteProductComponentDto() {
		return quoteProductComponentDto;
	}

	public void setQuoteProductComponentDto(QuoteProductComponentDto quoteProductComponentDto) {
		this.quoteProductComponentDto = quoteProductComponentDto;
	}

	public List<QuoteProductComponentDto> getQuoteProductComponentDtos() {
		return quoteProductComponentDtos;
	}

	public void setQuoteProductComponentDtos(List<QuoteProductComponentDto> quoteProductComponentDtos) {
		this.quoteProductComponentDtos = quoteProductComponentDtos;
	}

	public Integer getErfLocSiteBLocationId() {
		return erfLocSiteBLocationId;
	}

	public void setErfLocSiteBLocationId(Integer erfLocSiteBLocationId) {
		this.erfLocSiteBLocationId = erfLocSiteBLocationId;
	}

	public String getErfLocSiteBSiteCode() {
		return erfLocSiteBSiteCode;
	}

	public void setErfLocSiteBSiteCode(String erfLocSiteBSiteCode) {
		this.erfLocSiteBSiteCode = erfLocSiteBSiteCode;
	}

	public Integer getErfLocSiteALocationId() {
		return erfLocSiteALocationId;
	}

	public void setErfLocSiteALocationId(Integer erfLocSiteALocationId) {
		this.erfLocSiteALocationId = erfLocSiteALocationId;
	}

	public String getErfLocSiteASiteCode() {
		return erfLocSiteASiteCode;
	}

	public void setErfLocSiteASiteCode(String erfLocSiteASiteCode) {
		this.erfLocSiteASiteCode = erfLocSiteASiteCode;
	}

	public String getErfLrSolutionId() {
		return erfLrSolutionId;
	}

	public void setErfLrSolutionId(String erfLrSolutionId) {
		this.erfLrSolutionId = erfLrSolutionId;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	@Override
	public String toString() {
		return "QuoteIllSiteDto [id=" + id + ", createdBy=" + createdBy + ", createdTime=" + createdTime + ", imageUrl="
				+ imageUrl + ", feasibility=" + feasibility + ", mrc=" + mrc + ", nrc=" + nrc
				+ ", quoteProductComponentDto=" + quoteProductComponentDto + ", quoteProductComponentDtos="
				+ quoteProductComponentDtos + ", erfLocSiteBLocationId=" + erfLocSiteBLocationId
				+ ", erfLocSiteBSiteCode=" + erfLocSiteBSiteCode + ", erfLocSiteALocationId=" + erfLocSiteALocationId
				+ ", erfLocSiteASiteCode=" + erfLocSiteASiteCode + ", erfLrSolutionId=" + erfLrSolutionId
				+ ", bandwidth=" + bandwidth + "]";
	}

}
