package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the notification_entity database table.
 * 
 */
@Entity
@Table(name="notification_entity")
@NamedQuery(name="NotificationEntity.findAll", query="SELECT n FROM NotificationEntity n")
public class NotificationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	private String handler;

	@Column(name="is_active")
	private Integer isActive;

	private String name;

	public NotificationEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHandler() {
		return this.handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}