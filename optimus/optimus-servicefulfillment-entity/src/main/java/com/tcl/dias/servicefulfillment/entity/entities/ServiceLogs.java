/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author vivek
 *
 */


@Entity
@Table(name = "service_logs")
@NamedQuery(name = "ServiceLogs.findAll", query = "SELECT s FROM ServiceLogs s")
public class ServiceLogs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8273057842195147636L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_value")
	private String attributeValue;

	@Column(name = "changed_attribute_value")
	private String changedAttributeValue;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "updated_time")
	private Timestamp updatedTime;
	
    @Column(name = "service_id")
    private Integer serviceId;
    
	@Column(name = "type")
	private String type;
	
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * @param attributeValue the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * @return the changedAttributeValue
	 */
	public String getChangedAttributeValue() {
		return changedAttributeValue;
	}

	/**
	 * @param changedAttributeValue the changedAttributeValue to set
	 */
	public void setChangedAttributeValue(String changedAttributeValue) {
		this.changedAttributeValue = changedAttributeValue;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the updatedTime
	 */
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * @return the serviceId
	 */
	public Integer getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
    
    

}
