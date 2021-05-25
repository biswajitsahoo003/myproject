package com.tcl.dias.serviceinventory.beans;

/**
 * Bean to return path information for a given template
 * @author Srinivasa Raghavan
 */
public class TemplatePathInterfaceDetail {
	private String interfaceName;
	private String pathName;
	private Boolean status;

	public TemplatePathInterfaceDetail() {
		this.status = true;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		if (this.pathName != null)
			return this.pathName.hashCode();
		else
			return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TemplatePathInterfaceDetail && this.pathName != null)
			return this.pathName.equalsIgnoreCase(((TemplatePathInterfaceDetail) obj).pathName);
		else
			return false;
	}
}
