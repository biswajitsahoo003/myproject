package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * MstServiceCommunity Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_service_community")
@NamedQuery(name = "MstServiceCommunity.findAll", query = "SELECT m FROM MstServiceCommunity m")
public class MstServiceCommunity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "service_cmmunity")
	private String serviceCmmunity;

	@Column(name = "service_subtype")
	private String serviceSubtype;

	public MstServiceCommunity() {
	}

	public String getServiceCmmunity() {
		return this.serviceCmmunity;
	}

	public void setServiceCmmunity(String serviceCmmunity) {
		this.serviceCmmunity = serviceCmmunity;
	}

	public String getServiceSubtype() {
		return this.serviceSubtype;
	}

	public void setServiceSubtype(String serviceSubtype) {
		this.serviceSubtype = serviceSubtype;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}