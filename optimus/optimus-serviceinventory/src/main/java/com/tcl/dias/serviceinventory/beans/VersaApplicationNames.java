package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.Set;

public class VersaApplicationNames implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<String> predefinedApplications;
	private Set<String> userDefinedApplications;
	private Set<String> AssociatedPreDefinedApplications;
	private Set<String> AssociatedUserDefApplications;
	
	public Set<String> getPredefinedApplications() {
		return predefinedApplications;
	}
	public void setPredefinedApplications(Set<String> predefinedApplications) {
		this.predefinedApplications = predefinedApplications;
	}
	public Set<String> getUserDefinedApplications() {
		return userDefinedApplications;
	}
	public void setUserDefinedApplications(Set<String> userDefinedApplications) {
		this.userDefinedApplications = userDefinedApplications;
	}
	public Set<String> getAssociatedPreDefinedApplications() {
		return AssociatedPreDefinedApplications;
	}
	public void setAssociatedPreDefinedApplications(Set<String> associatedPreDefinedApplications) {
		AssociatedPreDefinedApplications = associatedPreDefinedApplications;
	}
	public Set<String> getAssociatedUserDefApplications() {
		return AssociatedUserDefApplications;
	}
	public void setAssociatedUserDefApplications(Set<String> associatedUserDefApplications) {
		AssociatedUserDefApplications = associatedUserDefApplications;
	}
	@Override
	public String toString() {
		return "VersaApplicationNames [predefinedApplications=" + predefinedApplications + ", userDefinedApplications="
				+ userDefinedApplications + ", AssociatedPreDefinedApplications=" + AssociatedPreDefinedApplications
				+ ", AssociatedUserDefApplications=" + AssociatedUserDefApplications + "]";
	}
	
	
	
	

}
