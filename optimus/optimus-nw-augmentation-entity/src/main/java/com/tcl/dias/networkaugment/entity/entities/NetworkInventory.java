package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * NetworkInventory Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="network_inventory")
@NamedQuery(name = "NetworkInventory.findAll", query = "SELECT a FROM NetworkInventory a")
public class NetworkInventory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "order_code")
	private String orderCode;
	
	@Column(name = "request_id")
	private String requestId;

	@Column(name = "type")
	private String type;

	@Column(name = "request",columnDefinition = "text")
	private String request;

	@Column(name = "response",columnDefinition = "text")
	private String response;

	@Column(name = "created_date")
	private Timestamp createdDate;

	public NetworkInventory() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderCode(String orderCode) {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
}