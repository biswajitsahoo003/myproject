package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "provider_component_assoc")
@NamedQuery(name = "ProviderComponentAssoc.findAll", query = "SELECT p FROM ProviderComponentAssoc p")
public class ProviderComponentAssoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "is_active_ind")
	private String isActive;

	// bi-directional many-to-one association to Provider
	@ManyToOne
	private Provider provider;

	// bi-directional many-to-one association to Component
	@ManyToOne
	private Component component;

	public ProviderComponentAssoc() {
		// default constructor
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Component getComponent() {
		return this.component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

}