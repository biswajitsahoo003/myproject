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
@Table(name = "user_to_user_groups")
@NamedQuery(name = "UserToUserGroups.findAll", query = "SELECT m FROM UserToUserGroups m")
public class UserToUserGroups implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_group_id")
	private MstUserGroups mstUserGroups;
	
	@Column(name="user_uid")
	private String userUId;

	@Column(name="username")
	private String username;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

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
	 * @return the userUId
	 */
	public String getUserUId() {
		return userUId;
	}

	/**
	 * @param userUId the userUId to set
	 */
	public void setUserUId(String userUId) {
		this.userUId = userUId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the mstUserGroups
	 */
	public MstUserGroups getMstUserGroups() {
		return mstUserGroups;
	}

	/**
	 * @param mstUserGroups the mstUserGroups to set
	 */
	public void setMstUserGroups(MstUserGroups mstUserGroups) {
		this.mstUserGroups = mstUserGroups;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
	

}
