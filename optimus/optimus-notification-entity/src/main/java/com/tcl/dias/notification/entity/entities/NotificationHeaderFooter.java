package com.tcl.dias.notification.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the notification_header_footer database table.
 * 
 */
@Entity
@Table(name="notification_header_footer")
@NamedQuery(name="NotificationHeaderFooter.findAll", query="SELECT n FROM NotificationHeaderFooter n")
public class NotificationHeaderFooter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Lob
	@Column(name="footer_content")
	private String footerContent;

	@Lob
	@Column(name="header_content")
	private String headerContent;

	@Column(name="is_active")
	private Integer isActive;

	@Column(name="partner_id")
	private Integer partnerId;

	//bi-directional many-to-one association to NotificationTemplate
	@OneToMany(mappedBy="notificationHeaderFooter")
	private List<NotificationTemplate> notificationTemplates;

	public NotificationHeaderFooter() {
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

	public String getFooterContent() {
		return this.footerContent;
	}

	public void setFooterContent(String footerContent) {
		this.footerContent = footerContent;
	}

	public String getHeaderContent() {
		return this.headerContent;
	}

	public void setHeaderContent(String headerContent) {
		this.headerContent = headerContent;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getPartnerId() {
		return this.partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public List<NotificationTemplate> getNotificationTemplates() {
		return this.notificationTemplates;
	}

	public void setNotificationTemplates(List<NotificationTemplate> notificationTemplates) {
		this.notificationTemplates = notificationTemplates;
	}

	public NotificationTemplate addNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().add(notificationTemplate);
		notificationTemplate.setNotificationHeaderFooter(this);

		return notificationTemplate;
	}

	public NotificationTemplate removeNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().remove(notificationTemplate);
		notificationTemplate.setNotificationHeaderFooter(null);

		return notificationTemplate;
	}

}