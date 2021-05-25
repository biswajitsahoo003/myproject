package com.tcl.dias.servicefulfillment.entity.entities;

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

@Entity
@Table(name = "ace_ip_mapping")
@NamedQuery(name = "AceIPMapping.findAll", query = "select aim from AceIPMapping aim")
public class AceIPMapping implements Serializable {

	private static final long serialVersionUID = -7916217943260938595L;
	
	public AceIPMapping() {
		// do nothing
	}
	
	public AceIPMapping(ScServiceDetail scServiceDetail, String aceIp) {
		this.setScServiceDetail(scServiceDetail);
		this.setAceIp(aceIp);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	private ScServiceDetail scServiceDetail;

	@Column(name = "ace_ip")
	private String aceIp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

	public String getAceIp() {
		return aceIp;
	}

	public void setAceIp(String aceIp) {
		this.aceIp = aceIp;
	}

}
