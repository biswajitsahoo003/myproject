package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the notification_action database table.
 * 
 */
@Entity
@Table(name="notification_action")
@NamedQuery(name="NotificationAction.findAll", query="SELECT n FROM NotificationAction n")
public class NotificationAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name="erf_prd_catalog_product_name")
	private String erfPrdCatalogProductName;

	@Column(name="is_active")
	private Integer isActive;

	@Column(name="is_default_user_notification_action")
	private Integer isDefaultUserNotificationAction;

	private String name;

	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="notificationAction")
	private List<Notification> notifications;

	//bi-directional many-to-one association to NotificationTemplateToAction
	@OneToMany(mappedBy="notificationAction")
	private List<NotificationTemplateToAction> notificationTemplateToActions;

	//bi-directional many-to-one association to UserNotificationSetting
	@OneToMany(mappedBy="notificationAction")
	private List<UserNotificationSetting> userNotificationSettings;

	public NotificationAction() {
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

	public String getErfPrdCatalogProductName() {
		return this.erfPrdCatalogProductName;
	}

	public void setErfPrdCatalogProductName(String erfPrdCatalogProductName) {
		this.erfPrdCatalogProductName = erfPrdCatalogProductName;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsDefaultUserNotificationAction() {
		return this.isDefaultUserNotificationAction;
	}

	public void setIsDefaultUserNotificationAction(Integer isDefaultUserNotificationAction) {
		this.isDefaultUserNotificationAction = isDefaultUserNotificationAction;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public Notification addNotification(Notification notification) {
		getNotifications().add(notification);
		notification.setNotificationAction(this);

		return notification;
	}

	public Notification removeNotification(Notification notification) {
		getNotifications().remove(notification);
		notification.setNotificationAction(null);

		return notification;
	}

	public List<NotificationTemplateToAction> getNotificationTemplateToActions() {
		return this.notificationTemplateToActions;
	}

	public void setNotificationTemplateToActions(List<NotificationTemplateToAction> notificationTemplateToActions) {
		this.notificationTemplateToActions = notificationTemplateToActions;
	}

	public NotificationTemplateToAction addNotificationTemplateToAction(NotificationTemplateToAction notificationTemplateToAction) {
		getNotificationTemplateToActions().add(notificationTemplateToAction);
		notificationTemplateToAction.setNotificationAction(this);

		return notificationTemplateToAction;
	}

	public NotificationTemplateToAction removeNotificationTemplateToAction(NotificationTemplateToAction notificationTemplateToAction) {
		getNotificationTemplateToActions().remove(notificationTemplateToAction);
		notificationTemplateToAction.setNotificationAction(null);

		return notificationTemplateToAction;
	}

	public List<UserNotificationSetting> getUserNotificationSettings() {
		return this.userNotificationSettings;
	}

	public void setUserNotificationSettings(List<UserNotificationSetting> userNotificationSettings) {
		this.userNotificationSettings = userNotificationSettings;
	}

	public UserNotificationSetting addUserNotificationSetting(UserNotificationSetting userNotificationSetting) {
		getUserNotificationSettings().add(userNotificationSetting);
		userNotificationSetting.setNotificationAction(this);

		return userNotificationSetting;
	}

	public UserNotificationSetting removeUserNotificationSetting(UserNotificationSetting userNotificationSetting) {
		getUserNotificationSettings().remove(userNotificationSetting);
		userNotificationSetting.setNotificationAction(null);

		return userNotificationSetting;
	}

}