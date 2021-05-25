package com.tcl.dias.oms.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;

/**
 * Dto class for OrderIllsites entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderIllsitesDto {

	@NotNull(message = Constants.ID_NULL)
	private Integer id;


	private Integer erfLocSiteaLocationId;

	private String erfLocSiteaSiteCode;

	private Integer erfLocSitebLocationId;

	private String erfLocSitebSiteCode;

	private String erfLrSolutionId;

	private Byte feasibility;

	private String imageUrl;

	private Byte isTaxExempted;

	private Double mrc;

	private Double nrc;

	private Date requestorDate;

	private String siteCode;

	private String stage;

	private Byte status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfLocSiteaLocationId() {
		return erfLocSiteaLocationId;
	}

	public void setErfLocSiteaLocationId(Integer erfLocSiteaLocationId) {
		this.erfLocSiteaLocationId = erfLocSiteaLocationId;
	}

	public String getErfLocSiteaSiteCode() {
		return erfLocSiteaSiteCode;
	}

	public void setErfLocSiteaSiteCode(String erfLocSiteaSiteCode) {
		this.erfLocSiteaSiteCode = erfLocSiteaSiteCode;
	}

	public Integer getErfLocSitebLocationId() {
		return erfLocSitebLocationId;
	}

	public void setErfLocSitebLocationId(Integer erfLocSitebLocationId) {
		this.erfLocSitebLocationId = erfLocSitebLocationId;
	}

	public String getErfLocSitebSiteCode() {
		return erfLocSitebSiteCode;
	}

	public void setErfLocSitebSiteCode(String erfLocSitebSiteCode) {
		this.erfLocSitebSiteCode = erfLocSitebSiteCode;
	}

	public String getErfLrSolutionId() {
		return erfLrSolutionId;
	}

	public void setErfLrSolutionId(String erfLrSolutionId) {
		this.erfLrSolutionId = erfLrSolutionId;
	}

	public Byte getFeasibility() {
		return feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Byte getIsTaxExempted() {
		return isTaxExempted;
	}

	public void setIsTaxExempted(Byte isTaxExempted) {
		this.isTaxExempted = isTaxExempted;
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

	public Date getRequestorDate() {
		return requestorDate;
	}

	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public OrderIllsitesDto(OrderIllSite orderIllSite) {
		if (orderIllSite!=null) {
			this.setErfLocSiteaLocationId(orderIllSite.getErfLocSiteaLocationId());
			this.setErfLocSiteaSiteCode(orderIllSite.getErfLocSiteaSiteCode());
			this.setErfLocSitebLocationId(orderIllSite.getErfLocSitebLocationId());
			this.setErfLocSitebSiteCode(orderIllSite.getErfLocSitebSiteCode());
			this.setErfLrSolutionId(orderIllSite.getErfLrSolutionId());
			this.setFeasibility(orderIllSite.getFeasibility());
			this.setId(orderIllSite.getId());
			this.setImageUrl(orderIllSite.getImageUrl());
			this.setIsTaxExempted(orderIllSite.getIsTaxExempted());
			this.setMrc(orderIllSite.getMrc());
			this.setNrc(orderIllSite.getNrc());
			this.setRequestorDate(orderIllSite.getRequestorDate());
			this.setSiteCode(orderIllSite.getSiteCode());
			this.setStage(orderIllSite.getStage());
			this.setStatus(orderIllSite.getStatus());
		}
	}

	@Override
	public String toString() {
		return "OrderIllsitesDto [id=" + id + ", erfLocSiteaLocationId=" + erfLocSiteaLocationId
				+ ", erfLocSiteaSiteCode=" + erfLocSiteaSiteCode + ", erfLocSitebLocationId=" + erfLocSitebLocationId
				+ ", erfLocSitebSiteCode=" + erfLocSitebSiteCode + ", erfLrSolutionId=" + erfLrSolutionId
				+ ", feasibility=" + feasibility + ", imageUrl=" + imageUrl + ", isTaxExempted=" + isTaxExempted
				+ ", mrc=" + mrc + ", nrc=" + nrc + ", requestorDate="
				+ requestorDate + ", siteCode=" + siteCode + ", stage=" + stage + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((erfLocSiteaLocationId == null) ? 0 : erfLocSiteaLocationId.hashCode());
		result = prime * result + ((erfLocSiteaSiteCode == null) ? 0 : erfLocSiteaSiteCode.hashCode());
		result = prime * result + ((erfLocSitebLocationId == null) ? 0 : erfLocSitebLocationId.hashCode());
		result = prime * result + ((erfLocSitebSiteCode == null) ? 0 : erfLocSitebSiteCode.hashCode());
		result = prime * result + ((erfLrSolutionId == null) ? 0 : erfLrSolutionId.hashCode());
		result = prime * result + ((feasibility == null) ? 0 : feasibility.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((isTaxExempted == null) ? 0 : isTaxExempted.hashCode());
		result = prime * result + ((mrc == null) ? 0 : mrc.hashCode());
		result = prime * result + ((nrc == null) ? 0 : nrc.hashCode());
		result = prime * result + ((requestorDate == null) ? 0 : requestorDate.hashCode());
		result = prime * result + ((siteCode == null) ? 0 : siteCode.hashCode());
		result = prime * result + ((stage == null) ? 0 : stage.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderIllsitesDto other = (OrderIllsitesDto) obj;
		if (erfLocSiteaLocationId == null) {
			if (other.erfLocSiteaLocationId != null)
				return false;
		} else if (!erfLocSiteaLocationId.equals(other.erfLocSiteaLocationId))
			return false;
		if (erfLocSiteaSiteCode == null) {
			if (other.erfLocSiteaSiteCode != null)
				return false;
		} else if (!erfLocSiteaSiteCode.equals(other.erfLocSiteaSiteCode))
			return false;
		if (erfLocSitebLocationId == null) {
			if (other.erfLocSitebLocationId != null)
				return false;
		} else if (!erfLocSitebLocationId.equals(other.erfLocSitebLocationId))
			return false;
		if (erfLocSitebSiteCode == null) {
			if (other.erfLocSitebSiteCode != null)
				return false;
		} else if (!erfLocSitebSiteCode.equals(other.erfLocSitebSiteCode))
			return false;
		if (erfLrSolutionId == null) {
			if (other.erfLrSolutionId != null)
				return false;
		} else if (!erfLrSolutionId.equals(other.erfLrSolutionId))
			return false;
		if (feasibility == null) {
			if (other.feasibility != null)
				return false;
		} else if (!feasibility.equals(other.feasibility))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (isTaxExempted == null) {
			if (other.isTaxExempted != null)
				return false;
		} else if (!isTaxExempted.equals(other.isTaxExempted))
			return false;
		if (mrc == null) {
			if (other.mrc != null)
				return false;
		} else if (!mrc.equals(other.mrc))
			return false;
		if (nrc == null) {
			if (other.nrc != null)
				return false;
		} else if (!nrc.equals(other.nrc))
			return false;
		if (requestorDate == null) {
			if (other.requestorDate != null)
				return false;
		} else if (!requestorDate.equals(other.requestorDate))
			return false;
		if (siteCode == null) {
			if (other.siteCode != null)
				return false;
		} else if (!siteCode.equals(other.siteCode))
			return false;
		if (stage == null) {
			if (other.stage != null)
				return false;
		} else if (!stage.equals(other.stage))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

}
