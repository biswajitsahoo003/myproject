package com.tcl.dias.common.beans;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class SIComponentBean {
	
	    private Integer id;
	    private String componentName;
	    private String createdBy;
	    private Timestamp createdDate;
	    private String isActive;
	    private Integer siServiceDetailId;
	    private String updatedBy;
	    private Timestamp updatedDate;
	    private String uuid;
	    private List<SIComponentAttributeBean> siComponentAttributes;
	    
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getComponentName() {
			return componentName;
		}
		public void setComponentName(String componentName) {
			this.componentName = componentName;
		}
		public String getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}
		public Timestamp getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Timestamp createdDate) {
			this.createdDate = createdDate;
		}
		public String getIsActive() {
			return isActive;
		}
		public void setIsActive(String isActive) {
			this.isActive = isActive;
		}
		public Integer getSiServiceDetailId() {
			return siServiceDetailId;
		}
		public void setSiServiceDetailId(Integer siServiceDetailId) {
			this.siServiceDetailId = siServiceDetailId;
		}
		public String getUpdatedBy() {
			return updatedBy;
		}
		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}
		public Timestamp getUpdatedDate() {
			return updatedDate;
		}
		public void setUpdatedDate(Timestamp updatedDate) {
			this.updatedDate = updatedDate;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public List<SIComponentAttributeBean> getSiComponentAttributes() {
			return siComponentAttributes;
		}
		public void setSiComponentAttributes(List<SIComponentAttributeBean> siComponentAttributes) {
			this.siComponentAttributes = siComponentAttributes;
		}

	@Override
	public String toString() {
		return "SIComponentBean{" +
				"id=" + id +
				", componentName='" + componentName + '\'' +
				", createdBy='" + createdBy + '\'' +
				", createdDate=" + createdDate +
				", isActive='" + isActive + '\'' +
				", siServiceDetailId=" + siServiceDetailId +
				", updatedBy='" + updatedBy + '\'' +
				", updatedDate=" + updatedDate +
				", uuid='" + uuid + '\'' +
				", siComponentAttributes=" + siComponentAttributes +
				'}';
	}
}
