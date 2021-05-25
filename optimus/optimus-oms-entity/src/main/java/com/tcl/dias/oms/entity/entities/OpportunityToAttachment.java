package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Entity class for Opportunity to Attachment table
 *
 * AVALLAPI
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "opportunity_to_attachment")
@NamedQuery(name = "OpportunityToAttachment.findAll", query = "SELECT o FROM OpportunityToAttachment o")
public class OpportunityToAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "opty_id")
    private Integer optyId;

    @Column(name = "attachment_id")
    private Integer attachmentId;

    @Column(name = "attachment_type")
    private String attachmentType;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOptyId() {
        return optyId;
    }

    public void setOptyId(Integer optyId) {
        this.optyId = optyId;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @PrePersist
    public void setCreatedDate() {
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
