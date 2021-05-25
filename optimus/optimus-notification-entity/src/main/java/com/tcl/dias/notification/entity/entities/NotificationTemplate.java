package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the notification_template database table.
 * 
 */
@Entity
@Table(name="notification_template")
@NamedQuery(name="NotificationTemplate.findAll", query="SELECT n FROM NotificationTemplate n")
public class NotificationTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name="is_active")
	private Integer isActive;

	private String name;

	@Column(name="notification_type")
	private String notificationType;

	@Column(name="template_reference_name")
	private String templateReferenceName;

	//bi-directional many-to-one association to NotificationHeaderFooter
	@ManyToOne
	@JoinColumn(name="notification_header_footer_id")
	private NotificationHeaderFooter notificationHeaderFooter;

	//bi-directional many-to-one association to NotificationTemplateToAction
	@OneToMany(mappedBy="notificationTemplate")
	private List<NotificationTemplateToAction> notificationTemplateToActions;

	public NotificationTemplate() {
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

	public String getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getTemplateReferenceName() {
		return this.templateReferenceName;
	}

	public void setTemplateReferenceName(String templateReferenceName) {
		this.templateReferenceName = templateReferenceName;
	}

	public NotificationHeaderFooter getNotificationHeaderFooter() {
		return this.notificationHeaderFooter;
	}

	public void setNotificationHeaderFooter(NotificationHeaderFooter notificationHeaderFooter) {
		this.notificationHeaderFooter = notificationHeaderFooter;
	}

	public List<NotificationTemplateToAction> getNotificationTemplateToActions() {
		return this.notificationTemplateToActions;
	}

	public void setNotificationTemplateToActions(List<NotificationTemplateToAction> notificationTemplateToActions) {
		this.notificationTemplateToActions = notificationTemplateToActions;
	}

	public NotificationTemplateToAction addNotificationTemplateToAction(NotificationTemplateToAction notificationTemplateToAction) {
		getNotificationTemplateToActions().add(notificationTemplateToAction);
		notificationTemplateToAction.setNotificationTemplate(this);

		return notificationTemplateToAction;
	}

	public NotificationTemplateToAction removeNotificationTemplateToAction(NotificationTemplateToAction notificationTemplateToAction) {
		getNotificationTemplateToActions().remove(notificationTemplateToAction);
		notificationTemplateToAction.setNotificationTemplate(null);

		return notificationTemplateToAction;
	}

}