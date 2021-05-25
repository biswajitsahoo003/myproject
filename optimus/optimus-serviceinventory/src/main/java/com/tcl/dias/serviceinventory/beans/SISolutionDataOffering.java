package com.tcl.dias.serviceinventory.beans;

import java.util.List;

import com.tcl.dias.common.beans.MasterVRFBean;

/**
 * Class for SI solution offering
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SISolutionDataOffering {
    private Integer siServiceDetailId;
    private String linkType;
    private SolutionAttributes primary;
    private SolutionAttributes secondary;
    /*private List<ComponentBean> components;*/
    private List<AttributeDetail> primaryAttributes;
    private List<AttributeDetail> secondaryAttributes;
    private Integer locationId;
    private Boolean isMasterVrf=false;
    private MasterVRFBean primaryMasterVRFDetail;
    private MasterVRFBean secondaryMasterVRFDetail;
    private String isMultiVRF;
    
    
    

   

	public MasterVRFBean getSecondaryMasterVRFDetail() {
		return secondaryMasterVRFDetail;
	}

	public void setSecondaryMasterVRFDetail(MasterVRFBean secondaryMasterVRFDetail) {
		this.secondaryMasterVRFDetail = secondaryMasterVRFDetail;
	}

	public MasterVRFBean getPrimaryMasterVRFDetail() {
		return primaryMasterVRFDetail;
	}

	public void setPrimaryMasterVRFDetail(MasterVRFBean primaryMasterVRFDetail) {
		this.primaryMasterVRFDetail = primaryMasterVRFDetail;
	}

	public String getIsMultiVRF() {
		return isMultiVRF;
	}

	public void setIsMultiVRF(String isMultiVRF) {
		this.isMultiVRF = isMultiVRF;
	}

	public Boolean getIsMasterVrf() {
		return isMasterVrf;
	}

	public void setIsMasterVrf(Boolean isMasterVrf) {
		this.isMasterVrf = isMasterVrf;
	}

	

	public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }



    public Integer getSiServiceDetailId() {
        return siServiceDetailId;
    }

    public void setSiServiceDetailId(Integer siServiceDetailId) {
        this.siServiceDetailId = siServiceDetailId;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public SolutionAttributes getPrimary() {
        return primary;
    }

    public void setPrimary(SolutionAttributes primary) {
        this.primary = primary;
    }

    public SolutionAttributes getSecondary() {
        return secondary;
    }

    public void setSecondary(SolutionAttributes secondary) {
        this.secondary = secondary;
    }


    public List<AttributeDetail> getPrimaryAttributes() {
        return primaryAttributes;
    }

    public void setPrimaryAttributes(List<AttributeDetail> primaryAttributes) {
        this.primaryAttributes = primaryAttributes;
    }

    public List<AttributeDetail> getSecondaryAttributes() {
        return secondaryAttributes;
    }

    public void setSecondaryAttributes(List<AttributeDetail> secondaryAttributes) {
        this.secondaryAttributes = secondaryAttributes;
    }



}
