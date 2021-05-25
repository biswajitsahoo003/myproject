package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * Entity Class
 *;
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_product_solution_si_link")
public class OrderProductSolutionSiLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	@Column(name = "service_id")
	private String serviceId;

	@Column(name = "product_solution_id")
	private Integer productSolutionId;
	
	@Column(name = "access_type")
	private String accessType;

	@Column(name = "order_to_le_id")
	private Integer quoteToLeId;

    @Column(name= "po_number")
    private String poNumber;
    
    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name ="po_date")
    private String poDate;
    
    @Column(name = "copf_id")
    private String copfId;
    
    @Column(name = "tax_exception")
    private String taxException;
    
	public OrderProductSolutionSiLink() {
		//super();
	}

	public OrderProductSolutionSiLink(String serviceId,  String accessType, Integer quoteToLeId) {
		super();
		this.serviceId = serviceId;
		this.accessType = accessType;
		this.quoteToLeId = quoteToLeId;
	}

	public OrderProductSolutionSiLink(String serviceId, Integer productSolutionId, String accessType, Integer quoteToLeId) {
		super();
		this.serviceId = serviceId;
		this.productSolutionId = productSolutionId;
		this.accessType = accessType;
		this.quoteToLeId = quoteToLeId;
	}
	public OrderProductSolutionSiLink(String serviceId,  String accessType, Integer quoteToLeId,
			String poNumber,  String poDate, String effectiveDate) {
		super();
		this.serviceId = serviceId;
		this.accessType = accessType;
		this.quoteToLeId = quoteToLeId;
		this.poNumber = poNumber;
		this.effectiveDate = effectiveDate;
		this.poDate = poDate;
	}
	
	public OrderProductSolutionSiLink(String serviceId, String accessType, Integer quoteToLeId, String poNumber,
			String effectiveDate, String poDate, String copfId) {
		super();
		this.serviceId = serviceId;
		this.accessType = accessType;
		this.quoteToLeId = quoteToLeId;
		this.poNumber = poNumber;
		this.effectiveDate = effectiveDate;
		this.poDate = poDate;
		this.copfId = copfId;
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getServiceId() {
		return serviceId;
	}


	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	public Integer getProductSolutionId() {
		return productSolutionId;
	}


	public void setProductSolutionId(Integer productSolutionId) {
		this.productSolutionId = productSolutionId;
	}


	public String getAccessType() {
		return accessType;
	}


	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}


	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}


	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getTaxException() {
		return taxException;
	}

	public void setTaxException(String taxException) {
		this.taxException = taxException;
	}	
	
}