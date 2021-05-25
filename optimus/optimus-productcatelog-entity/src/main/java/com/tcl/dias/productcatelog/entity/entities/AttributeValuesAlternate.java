package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;
/**
 * 
 * @author Biswajit
 *
 */
@Entity
@Table(name = "attribute_values_alternate")
@NamedQuery(name = "AttributeValuesAlternate.findAll", query = "SELECT a FROM AttributeValuesAlternate a")
public class AttributeValuesAlternate  extends BaseEntity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//bi-directional one-to-one association to AttributeValue
	@OneToOne
	@JoinColumn(name="attr_value_id")
	private AttributeValue attributeValue;
	
	@Column(name ="attr_alternate_value")
	String attrAlternateValue;
	
	@Column(name ="effective_from_dt")
	Date effectiveFromDate;
	
	@Column(name ="effective_to_dt")
	Date effectiveToDate;
	
	@Column(name ="reason_txt")
	String reasonTxt;
	
	public AttributeValue getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	public String getAttrAlternateValue() {
		return attrAlternateValue;
	}
	public void setAttrAlternateValue(String attrAlternateValue) {
		this.attrAlternateValue = attrAlternateValue;
	}
	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}
	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}
	public Date getEffectiveToDate() {
		return effectiveToDate;
	}
	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}
	public String getReasonTxt() {
		return reasonTxt;
	}
	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}

}
