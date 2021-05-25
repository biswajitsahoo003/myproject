package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the user_notification_settings database table.
 * 
 */
@Entity
@Table(name="user_notification_settings")
@NamedQuery(name="UserNotificationSetting.findAll", query="SELECT u FROM UserNotificationSetting u")
public class UserNotificationSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_time")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name="is_active")
	private Integer isActive;

	@Column(name="is_notification_enabled")
	private Integer isNotificationEnabled;

	@Column(name="notification_type")
	private String notificationType;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_time")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date updatedTime;

	private String userid;

	//bi-directional many-to-one association to NotificationAction
	@ManyToOne
	@JoinColumn(name="action_id")
	private NotificationAction notificationAction;

	public UserNotificationSetting() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsNotificationEnabled() {
		return this.isNotificationEnabled;
	}

	public void setIsNotificationEnabled(Integer isNotificationEnabled) {
		this.isNotificationEnabled = isNotificationEnabled;
	}

	public String getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public NotificationAction getNotificationAction() {
		return this.notificationAction;
	}

	public void setNotificationAction(NotificationAction notificationAction) {
		this.notificationAction = notificationAction;
	}

}