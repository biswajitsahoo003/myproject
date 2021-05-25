package com.tcl.dias.oms.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * 
 * This file contains the OmsAttachmentBean.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmsAttachmentBean implements Serializable {

	private static final long serialVersionUID = -6703350109797947542L;

	private Integer id;

	private String attachmentType;

	private Integer erfCusAttachmentId;

	private Integer referenceId;

	private String referenceName;

	private Integer orderToLeId;

	private Integer quoteToLeId;

	public OmsAttachmentBean() {
	}

	public OmsAttachmentBean(OmsAttachment omsAttachment) {
		this.id = omsAttachment.getId();
		this.attachmentType = omsAttachment.getAttachmentType();
		this.erfCusAttachmentId = omsAttachment.getErfCusAttachmentId();
		if (omsAttachment.getOrderToLe() != null)
			this.orderToLeId = omsAttachment.getOrderToLe().getId();
		if (omsAttachment.getQuoteToLe() != null)
			this.quoteToLeId = omsAttachment.getQuoteToLe().getId();
		this.referenceId = omsAttachment.getReferenceId();
		this.referenceName = omsAttachment.getReferenceName();

	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the attachmentType
	 */
	public String getAttachmentType() {
		return attachmentType;
	}

	/**
	 * @param attachmentType
	 *            the attachmentType to set
	 */
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	/**
	 * @return the erfCusAttachmentId
	 */
	public Integer getErfCusAttachmentId() {
		return erfCusAttachmentId;
	}

	/**
	 * @param erfCusAttachmentId
	 *            the erfCusAttachmentId to set
	 */
	public void setErfCusAttachmentId(Integer erfCusAttachmentId) {
		this.erfCusAttachmentId = erfCusAttachmentId;
	}

	/**
	 * @return the referenceId
	 */
	public Integer getReferenceId() {
		return referenceId;
	}

	/**
	 * @param referenceId
	 *            the referenceId to set
	 */
	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	/**
	 * @return the referenceName
	 */
	public String getReferenceName() {
		return referenceName;
	}

	/**
	 * @param referenceName
	 *            the referenceName to set
	 */
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	/**
	 * @return the orderToLeId
	 */
	public Integer getOrderToLeId() {
		return orderToLeId;
	}

	/**
	 * @param orderToLeId
	 *            the orderToLeId to set
	 */
	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}

	/**
	 * @return the quoteToLeId
	 */
	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	/**
	 * @param quoteToLeId
	 *            the quoteToLeId to set
	 */
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

}
