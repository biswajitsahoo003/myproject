package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;




/**
 * This file contains the MstOmsAttributeBean.java class.
 *
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstOmsAttributeBean  implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4736218973293335530L;

	private Integer id;

	private String category;

	private String createdBy;

	private Date createdTime;

	private String description;

	private Byte isActive;

	private String name;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isActive
	 */
	public Byte getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * convert from MstOmsAttribute to MstOmsAttributeBean  
     * @param quoteToLeBean
     * @return
     */
    public static MstOmsAttributeBean toMstOmsAttributeBean(MstOmsAttribute mstOmsAttribute) {
    	MstOmsAttributeBean mstOmsAttrBean = new MstOmsAttributeBean();
    	mstOmsAttrBean.id=mstOmsAttribute.getId();
    	mstOmsAttrBean.name=mstOmsAttribute.getName();
    	mstOmsAttrBean.category=mstOmsAttribute.getCategory();
    	mstOmsAttrBean.createdBy=mstOmsAttribute.getCreatedBy();
    	mstOmsAttrBean.createdTime=mstOmsAttribute.getCreatedTime();
    	mstOmsAttrBean.isActive=mstOmsAttribute.getIsActive();
        return mstOmsAttrBean;
    }


}
