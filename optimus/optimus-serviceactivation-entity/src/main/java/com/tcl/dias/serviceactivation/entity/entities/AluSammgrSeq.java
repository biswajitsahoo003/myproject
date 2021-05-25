package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * AluSammgrSeq Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="alu_sammgr_seq")
@NamedQuery(name="AluSammgrSeq.findAll", query="SELECT a FROM AluSammgrSeq a")
public class AluSammgrSeq implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sammgr_seqid")
	private Integer sammgrSeqid;

	@Column(name="service_id")
	private String serviceId;

	@Column(name="service_type")
	private String serviceType;
	
	
	public AluSammgrSeq() {
	}

	public Integer getSammgrSeqid() {
		return this.sammgrSeqid;
	}

	public void setSammgrSeqid(Integer sammgrSeqid) {
		this.sammgrSeqid = sammgrSeqid;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

}