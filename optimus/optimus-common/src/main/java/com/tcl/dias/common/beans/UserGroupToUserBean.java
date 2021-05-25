package com.tcl.dias.common.beans;

public class UserGroupToUserBean {

	private Integer id;

	private Byte isActive;
	
	private UserGroupBean userGroups;
	
	private String username;
	
	private String firstName;
	
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the isActive
	 */
	public Byte getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the userGroups
	 */
	public UserGroupBean getUserGroups() {
		return userGroups;
	}

	/**
	 * @param userGroups the userGroups to set
	 */
	public void setUserGroups(UserGroupBean userGroups) {
		this.userGroups = userGroups;
	}
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
}
