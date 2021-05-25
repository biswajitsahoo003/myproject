/**
 * 
 */
package com.tcl.dias.oms.entity.entities;

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

/**
 * 
 * Entity Class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_user_groups")
@NamedQuery(name = "MstUserGroups.findAll", query = "SELECT m FROM MstUserGroups m")
public class MstUserGroups implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "group_name")
	private String groupName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_type")
	private MstGroupType mstGroupType;

	@Column(name = "is_region_required")
	private Integer isRegionRequired;

	private Byte status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public MstGroupType getMstGroupType() {
		return mstGroupType;
	}

	public void setMstGroupType(MstGroupType mstGroupType) {
		this.mstGroupType = mstGroupType;
	}

	public Integer getIsRegionRequired() {
		return isRegionRequired;
	}

	public void setIsRegionRequired(Integer isRegionRequired) {
		this.isRegionRequired = isRegionRequired;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}
