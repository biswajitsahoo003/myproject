package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * Entity Class
 *;
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "update_gst_request")
public class UpdateGstRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "quote_to_le_id")
	private Integer quoteToLeId;
	
	@Column(name = "quote_id")
	private Integer quoteId;
	
	@Column(name = "site_id")
	private Integer siteId;
	
	@Column(name = "family_name")
	private String familyName;
    
    @Column(name="is_gst_updated")
    private String isGstUpdated;
    
    @Column(name="site_a_gst")
    private String siteAgst;
    
    @Column(name="site_b_gst")
    private String siteBgst;
    
    @Column(name="user_name")
    private String userName;
    
	public UpdateGstRequest() {
		//super();
	}	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}
	
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}
	
	public String getIsGstUpdated() {
		return isGstUpdated;
	}

	public void setIsGstUpdated(String isGstUpdated) {
		this.isGstUpdated = isGstUpdated;
	}

	public String getSiteAgst() {
		return siteAgst;
	}

	public void setSiteAgst(String siteAgst) {
		this.siteAgst = siteAgst;
	}

	public String getSiteBgst() {
		return siteBgst;
	}

	public void setSiteBgst(String siteBgst) {
		this.siteBgst = siteBgst;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}