package com.tcl.dias.products.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.AttributeValue;

/**
 * Entity for view vw_ias_price_book
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)

public class AttributeValueDto {

	@NotNull
	private Integer attrId;

	private String attrValue;

	private String reasonTxt;
	
	private String attrKey;

	

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	@Pattern(regexp = Constants.ISACTIVE_PATTERN_REGEX, message = Constants.ISACTIVE_PATTERN)
	private String isActive;

	public AttributeValueDto() {

	}

	public AttributeValueDto(AttributeValue attributeValue) {
		String uomCd = "";
		String alternateVal = "";
		if (attributeValue != null && !(attributeValue.getIsActive()!=null && attributeValue.getIsActive().equals("N"))) {
			if(attributeValue.getUomMaster()!=null && attributeValue.getUomMaster().getCd()!=null) {
				uomCd = " "+attributeValue.getUomMaster().getCd();
			}
			if (attributeValue.getAttributeValuesAlternate()!=null && attributeValue.getAttributeValuesAlternate().getAttrAlternateValue()!=null) {
				alternateVal = "("+attributeValue.getAttributeValuesAlternate().getAttrAlternateValue()+")";
			}
			this.setAttrId(attributeValue.getId());
			this.setAttrValue(attributeValue.getAttrValues()+uomCd);
			this.setReasonTxt(attributeValue.getReasonTxt());
			this.setIsActive(attributeValue.getIsActive());
			this.setAttrKey(attributeValue.getAttrValues()+uomCd+alternateVal);
		}

	}

	public Integer getAttrId() {
		return attrId;
	}

	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getReasonTxt() {
		return reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}

	@Override
	public String toString() {
		return "AttributeValueDto [attrId=" + attrId + ", attrValue=" + attrValue + ", reasonTxt=" + reasonTxt
				+ ", isActive=" + isActive + "]";
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getAttrKey() {
		return attrKey;
	}

	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attrId == null) ? 0 : attrId.hashCode());
		result = prime * result + ((attrValue == null) ? 0 : attrValue.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((reasonTxt == null) ? 0 : reasonTxt.hashCode());
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
		AttributeValueDto other = (AttributeValueDto) obj;
		if (attrId == null) {
			if (other.attrId != null)
				return false;
		} else if (!attrId.equals(other.attrId))
			return false;
		if (attrValue == null) {
			if (other.attrValue != null)
				return false;
		} else if (!attrValue.equals(other.attrValue))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (reasonTxt == null) {
			if (other.reasonTxt != null)
				return false;
		} else if (!reasonTxt.equals(other.reasonTxt))
			return false;
		return true;
	}

}
