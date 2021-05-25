package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * ServiceCosCriteria Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class ServiceCosCriteriaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer serviceCosId;
	private String bwBpsunit;
	private String classificationCriteria;
	private String cosName;
	private String cosPercent;
	private String dhcpVal1;
	private String dhcpVal2;
	private String dhcpVal3;
	private String dhcpVal4;
	private String dhcpVal5;
	private String dhcpVal6;
	private String dhcpVal7;
	private String dhcpVal8;
	private Timestamp endDate;
	private String ipprecedenceVal1;
	private String ipprecedenceVal2;
	private String ipprecedenceVal3;
	private String ipprecedenceVal4;
	private String ipprecedenceVal5;
	private String ipprecedenceVal6;
	private String ipprecedenceVal7;
	private String ipprecedenceVal8;
	private boolean isEdited;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Timestamp startDate;
	
	private Set<AclPolicyCriteriaBean> aclPolicyCriterias;

	public Integer getServiceCosId() {
		return serviceCosId;
	}

	public void setServiceCosId(Integer serviceCosId) {
		this.serviceCosId = serviceCosId;
	}

	public String getBwBpsunit() {
		return bwBpsunit;
	}

	public void setBwBpsunit(String bwBpsunit) {
		this.bwBpsunit = bwBpsunit;
	}

	public String getClassificationCriteria() {
		return classificationCriteria;
	}

	public void setClassificationCriteria(String classificationCriteria) {
		this.classificationCriteria = classificationCriteria;
	}

	public String getCosName() {
		return cosName;
	}

	public void setCosName(String cosName) {
		this.cosName = cosName;
	}

	public String getCosPercent() {
		return cosPercent;
	}

	public void setCosPercent(String cosPercent) {
		this.cosPercent = cosPercent;
	}

	public String getDhcpVal1() {
		return dhcpVal1;
	}

	public void setDhcpVal1(String dhcpVal1) {
		this.dhcpVal1 = dhcpVal1;
	}

	public String getDhcpVal2() {
		return dhcpVal2;
	}

	public void setDhcpVal2(String dhcpVal2) {
		this.dhcpVal2 = dhcpVal2;
	}

	public String getDhcpVal3() {
		return dhcpVal3;
	}

	public void setDhcpVal3(String dhcpVal3) {
		this.dhcpVal3 = dhcpVal3;
	}

	public String getDhcpVal4() {
		return dhcpVal4;
	}

	public void setDhcpVal4(String dhcpVal4) {
		this.dhcpVal4 = dhcpVal4;
	}

	public String getDhcpVal5() {
		return dhcpVal5;
	}

	public void setDhcpVal5(String dhcpVal5) {
		this.dhcpVal5 = dhcpVal5;
	}

	public String getDhcpVal6() {
		return dhcpVal6;
	}

	public void setDhcpVal6(String dhcpVal6) {
		this.dhcpVal6 = dhcpVal6;
	}

	public String getDhcpVal7() {
		return dhcpVal7;
	}

	public void setDhcpVal7(String dhcpVal7) {
		this.dhcpVal7 = dhcpVal7;
	}

	public String getDhcpVal8() {
		return dhcpVal8;
	}

	public void setDhcpVal8(String dhcpVal8) {
		this.dhcpVal8 = dhcpVal8;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getIpprecedenceVal1() {
		return ipprecedenceVal1;
	}

	public void setIpprecedenceVal1(String ipprecedenceVal1) {
		this.ipprecedenceVal1 = ipprecedenceVal1;
	}

	public String getIpprecedenceVal2() {
		return ipprecedenceVal2;
	}

	public void setIpprecedenceVal2(String ipprecedenceVal2) {
		this.ipprecedenceVal2 = ipprecedenceVal2;
	}

	public String getIpprecedenceVal3() {
		return ipprecedenceVal3;
	}

	public void setIpprecedenceVal3(String ipprecedenceVal3) {
		this.ipprecedenceVal3 = ipprecedenceVal3;
	}

	public String getIpprecedenceVal4() {
		return ipprecedenceVal4;
	}

	public void setIpprecedenceVal4(String ipprecedenceVal4) {
		this.ipprecedenceVal4 = ipprecedenceVal4;
	}

	public String getIpprecedenceVal5() {
		return ipprecedenceVal5;
	}

	public void setIpprecedenceVal5(String ipprecedenceVal5) {
		this.ipprecedenceVal5 = ipprecedenceVal5;
	}

	public String getIpprecedenceVal6() {
		return ipprecedenceVal6;
	}

	public void setIpprecedenceVal6(String ipprecedenceVal6) {
		this.ipprecedenceVal6 = ipprecedenceVal6;
	}

	public String getIpprecedenceVal7() {
		return ipprecedenceVal7;
	}

	public void setIpprecedenceVal7(String ipprecedenceVal7) {
		this.ipprecedenceVal7 = ipprecedenceVal7;
	}

	public String getIpprecedenceVal8() {
		return ipprecedenceVal8;
	}

	public void setIpprecedenceVal8(String ipprecedenceVal8) {
		this.ipprecedenceVal8 = ipprecedenceVal8;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Set<AclPolicyCriteriaBean> getAclPolicyCriterias() {
		
		if(aclPolicyCriterias==null) {
			aclPolicyCriterias=new HashSet<>();
		}
		return aclPolicyCriterias;
	}

	public void setAclPolicyCriterias(Set<AclPolicyCriteriaBean> aclPolicyCriterias) {
		this.aclPolicyCriterias = aclPolicyCriterias;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}