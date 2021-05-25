package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "oms_attachments")
@NamedQuery(name = "OmsAttachment.findAll", query = "SELECT o FROM OmsAttachment o")
public class OmsAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attachment_type")
	private String attachmentType;

	@Column(name = "erf_cus_attachment_id")
	private Integer erfCusAttachmentId;

	@Column(name = "reference_id")
	private Integer referenceId;

	@Column(name = "reference_name")
	private String referenceName;

	// bi-directional many-to-one association to OrderToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_le_id")
	private OrderToLe orderToLe;

	// bi-directional many-to-one association to QuoteToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_le_id")
	private QuoteToLe quoteToLe;

	public OmsAttachment() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttachmentType() {
		return this.attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getErfCusAttachmentId() {
		return this.erfCusAttachmentId;
	}

	public void setErfCusAttachmentId(Integer erfCusAttachmentId) {
		this.erfCusAttachmentId = erfCusAttachmentId;
	}

	public Integer getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceName() {
		return this.referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public OrderToLe getOrderToLe() {
		return this.orderToLe;
	}

	public void setOrderToLe(OrderToLe orderToLe) {
		this.orderToLe = orderToLe;
	}

	public QuoteToLe getQuoteToLe() {
		return this.quoteToLe;
	}

	public void setQuoteToLe(QuoteToLe quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

}