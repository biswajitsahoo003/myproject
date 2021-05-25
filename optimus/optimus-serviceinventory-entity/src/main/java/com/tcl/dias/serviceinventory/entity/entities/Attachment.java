package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 *  * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the attachment database table.
 * 
 */
@Entity
@Table(name="attachment")
public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String category;

	@Column(name="content_type_header")
	private String contentTypeHeader;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="is_active")
	private String isActive;

	private String name;

	@Column(name="storage_path_url")
	private String storagePathUrl;

	private String type;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	//bi-directional many-to-one association to SiAttachment
	@OneToMany(mappedBy="attachment", cascade=CascadeType.ALL)
	private Set<SIAttachment> siAttachments;

    public Attachment() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContentTypeHeader() {
		return this.contentTypeHeader;
	}

	public void setContentTypeHeader(String contentTypeHeader) {
		this.contentTypeHeader = contentTypeHeader;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStoragePathUrl() {
		return this.storagePathUrl;
	}

	public void setStoragePathUrl(String storagePathUrl) {
		this.storagePathUrl = storagePathUrl;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Set<SIAttachment> getSiAttachments() {
		return this.siAttachments;
	}

	public void setSiAttachments(Set<SIAttachment> siAttachments) {
		this.siAttachments = siAttachments;
	}
	
}