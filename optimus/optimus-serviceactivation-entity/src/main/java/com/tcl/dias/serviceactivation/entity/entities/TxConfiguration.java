package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the tx_configuration database table.
 * 
 */
@Entity
@Table(name="tx_configuration")
@NamedQuery(name="TxConfiguration.findAll", query="SELECT t FROM TxConfiguration t")
public class TxConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	
	@Column(name="order_code")
	private String orderCode;

	@Column(name="sc_order_id")
	private Integer scOrderId;
	
	@Column(name="service_id")
	private String serviceId;

	@Column(name="service_code")
	private String serviceCode;

	@Column(name="service_contents")
	private String serviceContents;
	
	@Column(name = "clr_response")
	private String clrResponse;	

	private String status;

	@Column(name="created_time")
	private Timestamp createdTime;

	
	@Column(name="updated_time")
	private Timestamp updatedTime;

	private Integer version;
	

	//bi-directional many-to-one association to NodeToConfigure
	@OneToMany(mappedBy="txConfiguration")
	private List<NodeToConfigure> nodeToConfigures;


	public TxConfiguration() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}


	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getScOrderId() {
		return this.scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceContents() {
		return this.serviceContents;
	}

	public void setServiceContents(String serviceContents) {
		this.serviceContents = serviceContents;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<NodeToConfigure> getNodeToConfigures() {
		return this.nodeToConfigures;
	}

	public void setNodeToConfigures(List<NodeToConfigure> nodeToConfigures) {
		this.nodeToConfigures = nodeToConfigures;
	}

	public NodeToConfigure addNodeToConfigure(NodeToConfigure nodeToConfigure) {
		getNodeToConfigures().add(nodeToConfigure);
		nodeToConfigure.setTxConfiguration(this);

		return nodeToConfigure;
	}

	public NodeToConfigure removeNodeToConfigure(NodeToConfigure nodeToConfigure) {
		getNodeToConfigures().remove(nodeToConfigure);
		nodeToConfigure.setTxConfiguration(null);

		return nodeToConfigure;
	}	

    public String getClrResponse() {
        return clrResponse;
    }

    public void setClrResponse(String clrResponse) {
        this.clrResponse = clrResponse;
    }
}