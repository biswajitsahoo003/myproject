package com.tcl.dias.servicefulfillment.entity.entities;



import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * 
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="mux_bom_attributes")
@NamedQuery(name="MuxBomAttribute.findAll", query="SELECT b FROM MuxBomAttribute b")
public class MuxBomAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="bom_code")
	private String bomCode;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="is_active_ind")
	private String isActiveInd;

	@Column(name="line_rate")
	private String lineRate;

	@Column(name = "topology_type")
	private String topologyType;

	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	public MuxBomAttribute() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBomCode() {
		return this.bomCode;
	}

	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getTopologyType() {
		return topologyType;
	}

	public void setTopologyType(String topologyType) {
		this.topologyType = topologyType;
	}
}