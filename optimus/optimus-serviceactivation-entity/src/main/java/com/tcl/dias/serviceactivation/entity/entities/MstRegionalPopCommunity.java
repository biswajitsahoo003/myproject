package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * MstRegionalPopCommunity Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_regional_pop_community")
@NamedQuery(name = "MstRegionalPopCommunity.findAll", query = "SELECT m FROM MstRegionalPopCommunity m")
public class MstRegionalPopCommunity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "pop_community")
	private String popCommunity;

	@Column(name = "regional_community")
	private String regionalCommunity;

	@Column(name = "router_hostname")
	private String routerHostname;

	public MstRegionalPopCommunity() {
	}

	public String getPopCommunity() {
		return this.popCommunity;
	}

	public void setPopCommunity(String popCommunity) {
		this.popCommunity = popCommunity;
	}

	public String getRegionalCommunity() {
		return this.regionalCommunity;
	}

	public void setRegionalCommunity(String regionalCommunity) {
		this.regionalCommunity = regionalCommunity;
	}

	public String getRouterHostname() {
		return this.routerHostname;
	}

	public void setRouterHostname(String routerHostname) {
		this.routerHostname = routerHostname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}