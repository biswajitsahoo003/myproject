package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the notification_recipients database table.
 * 
 */
@Entity
@Table(name="notification_recipients")
@NamedQuery(name="NotificationRecipient.findAll", query="SELECT n FROM NotificationRecipient n")
public class NotificationRecipient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="notification_template_id")
	private Integer notificationTemplateId;

	private String recipient;

	@Column(name="recipient_type")
	private String recipientType;

	//bi-directional many-to-one association to Notification
	@ManyToOne
	private Notification notification;

	public NotificationRecipient() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNotificationTemplateId() {
		return this.notificationTemplateId;
	}

	public void setNotificationTemplateId(Integer notificationTemplateId) {
		this.notificationTemplateId = notificationTemplateId;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getRecipientType() {
		return this.recipientType;
	}

	public void setRecipientType(String recipientType) {
		this.recipientType = recipientType;
	}

	public Notification getNotification() {
		return this.notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}