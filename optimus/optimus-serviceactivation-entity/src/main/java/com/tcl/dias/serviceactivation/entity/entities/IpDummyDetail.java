package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * IpAddressDetail Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="ip_dummy_details")
@NamedQuery(name="IpDummyDetail.findAll", query="SELECT i FROM IpDummyDetail i")
public class IpDummyDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="customer_side_dummy_ip_address")
	private String customerSideDummyIpAddress;

	@Column(name="tcl_side_dummy_ip_address")
	private String tclSideDummyIpAddress;

	@Column(name="dummy_wan_ip_address_subnet")
	private String dummyWANIpAddressSubnet;

	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "end_date")
	private Timestamp endDate;
	
	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="created_by")
	private String createdBy;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_detail_id")
	private ServiceDetail serviceDetail;

	
	public IpDummyDetail() {
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getCustomerSideDummyIpAddress() {
		return customerSideDummyIpAddress;
	}


	public void setCustomerSideDummyIpAddress(String customerSideDummyIpAddress) {
		this.customerSideDummyIpAddress = customerSideDummyIpAddress;
	}


	public String getTclSideDummyIpAddress() {
		return tclSideDummyIpAddress;
	}


	public void setTclSideDummyIpAddress(String tclSideDummyIpAddress) {
		this.tclSideDummyIpAddress = tclSideDummyIpAddress;
	}


	public String getDummyWANIpAddressSubnet() {
		return dummyWANIpAddressSubnet;
	}


	public void setDummyWANIpAddressSubnet(String dummyWANIpAddressSubnet) {
		this.dummyWANIpAddressSubnet = dummyWANIpAddressSubnet;
	}


	public ServiceDetail getServiceDetail() {
		return serviceDetail;
	}


	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}


	public Timestamp getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}


	public Timestamp getEndDate() {
		return endDate;
	}


	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}