package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the notification_template_to_action database table.
 * 
 */
@Entity
@Table(name="notification_template_to_action")
@NamedQuery(name="NotificationTemplateToAction.findAll", query="SELECT n FROM NotificationTemplateToAction n")
public class NotificationTemplateToAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="is_active")
	private Integer isActive;

	//bi-directional many-to-one association to NotificationAction
	@ManyToOne
	@JoinColumn(name="action_id")
	private NotificationAction notificationAction;

	//bi-directional many-to-one association to NotificationTemplate
	@ManyToOne
	@JoinColumn(name="template_id")
	private NotificationTemplate notificationTemplate;

	public NotificationTemplateToAction() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public NotificationAction getNotificationAction() {
		return this.notificationAction;
	}

	public void setNotificationAction(NotificationAction notificationAction) {
		this.notificationAction = notificationAction;
	}

	public NotificationTemplate getNotificationTemplate() {
		return this.notificationTemplate;
	}

	public void setNotificationTemplate(NotificationTemplate notificationTemplate) {
		this.notificationTemplate = notificationTemplate;
	}

}