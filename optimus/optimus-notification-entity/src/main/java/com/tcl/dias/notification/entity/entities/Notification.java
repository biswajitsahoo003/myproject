package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the notification database table.
 * 
 */
@Entity
@NamedQuery(name="Notification.findAll", query="SELECT n FROM Notification n")
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	private String content;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_time")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name="erf_prd_catalog_product_name")
	private String erfPrdCatalogProductName;

	@Column(name="notification_status")
	private String notificationStatus;

	@Column(name="notification_type")
	private String notificationType;

	@Column(name="reference_id")
	private Integer referenceId;

	@Column(name="reference_type")
	private String referenceType;
	
	@Column(name="reference_name")
	private String referenceName;
	
	@Column(name="reference_value")
	private String referenceValue;
	
	@Column(name="template_id")
	private Integer templateId;

	private String subject;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_time")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date updatedTime;

	private String uuid;

	//bi-directional many-to-one association to NotificationAction
	@ManyToOne
	@JoinColumn(name="notification_action_id")
	private NotificationAction notificationAction;

	//bi-directional many-to-one association to NotificationRecipient
	@OneToMany(mappedBy="notification")
	private List<NotificationRecipient> notificationRecipients;

	public Notification() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getErfPrdCatalogProductName() {
		return this.erfPrdCatalogProductName;
	}

	public void setErfPrdCatalogProductName(String erfPrdCatalogProductName) {
		this.erfPrdCatalogProductName = erfPrdCatalogProductName;
	}

	public String getNotificationStatus() {
		return this.notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public String getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Integer getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceType() {
		return this.referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public NotificationAction getNotificationAction() {
		return this.notificationAction;
	}

	public void setNotificationAction(NotificationAction notificationAction) {
		this.notificationAction = notificationAction;
	}

	public List<NotificationRecipient> getNotificationRecipients() {
		return this.notificationRecipients;
	}
	
	

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public void setNotificationRecipients(List<NotificationRecipient> notificationRecipients) {
		this.notificationRecipients = notificationRecipients;
	}

	public NotificationRecipient addNotificationRecipient(NotificationRecipient notificationRecipient) {
		getNotificationRecipients().add(notificationRecipient);
		notificationRecipient.setNotification(this);

		return notificationRecipient;
	}

	public NotificationRecipient removeNotificationRecipient(NotificationRecipient notificationRecipient) {
		getNotificationRecipients().remove(notificationRecipient);
		notificationRecipient.setNotification(null);

		return notificationRecipient;
	}

}