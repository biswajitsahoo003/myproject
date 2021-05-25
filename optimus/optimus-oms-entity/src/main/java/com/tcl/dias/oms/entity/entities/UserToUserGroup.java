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
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "user_to_user_groups")
@NamedQuery(name = "UserToUserGroup.findAll", query = "SELECT u FROM UserToUserGroup u")
public class UserToUserGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_uid")
	private String userUid;

	private String username;
	
	@Column(name = "user_id")
	private Integer userId;

	// bi-directional many-to-one association to MstUserGroup
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_group_id")
	private MstUserGroups mstUserGroup;
	
	
	
	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserToUserGroup() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserUid() {
		return this.userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public MstUserGroups getMstUserGroup() {
		return this.mstUserGroup;
	}

	public void setMstUserGroup(MstUserGroups mstUserGroup) {
		this.mstUserGroup = mstUserGroup;
	}

}