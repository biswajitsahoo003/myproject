package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "provider")
@NamedQuery(name = "Provider.findAll", query = "SELECT p FROM Provider p")
public class Provider extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "alternate_contact_no")
	private String alternateContactNo;

	@Column(name = "contact_no")
	private String contactNo;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;
	
	@Column(name = "nm_alias")
	private String aliasName;
	

	
	

	// bi-directional many-to-one association to ProviderType
	@ManyToOne
	@JoinColumn(name = "provider_type_id")
	private ProviderType providerType;

	// bi-directional many-to-one association to ProviderComponentAssoc
	@OneToMany(mappedBy = "provider")
	private List<ProviderComponentAssoc> providerComponentAssocs;

	public Provider() {
		// default constructor
	}

	public String getAlternateContactNo() {
		return this.alternateContactNo;
	}

	public void setAlternateContactNo(String alternateContactNo) {
		this.alternateContactNo = alternateContactNo;
	}

	public String getContactNo() {
		return this.contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProviderType getProviderType() {
		return this.providerType;
	}

	public void setProviderType(ProviderType providerType) {
		this.providerType = providerType;
	}

	public List<ProviderComponentAssoc> getProviderComponentAssocs() {
		return this.providerComponentAssocs;
	}

	public void setProviderComponentAssocs(List<ProviderComponentAssoc> providerComponentAssocs) {
		this.providerComponentAssocs = providerComponentAssocs;
	}

	public ProviderComponentAssoc addProviderComponentAssoc(ProviderComponentAssoc providerComponentAssoc) {
		getProviderComponentAssocs().add(providerComponentAssoc);
		providerComponentAssoc.setProvider(this);

		return providerComponentAssoc;
	}

	public ProviderComponentAssoc removeProviderComponentAssoc(ProviderComponentAssoc providerComponentAssoc) {
		getProviderComponentAssocs().remove(providerComponentAssoc);
		providerComponentAssoc.setProvider(null);

		return providerComponentAssoc;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}


}