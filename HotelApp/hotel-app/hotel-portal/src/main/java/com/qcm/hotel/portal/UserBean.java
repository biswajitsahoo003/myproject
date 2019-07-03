package com.qcm.hotel.portal;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;
import com.qcm.hotel.portal.entites.User;
@JsonInclude
public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String firstName;

	private String lastName;

	private String email;
	
	public UserBean(User userEntity) {
		this.id=userEntity.getId();
		this.firstName=userEntity.getFirstName();
		this.lastName=userEntity.getLastName();
		this.email=userEntity.getEmail();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
