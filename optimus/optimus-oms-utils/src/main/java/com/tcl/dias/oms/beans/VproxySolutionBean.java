package com.tcl.dias.oms.beans;

import java.util.List;

import com.tcl.dias.common.beans.VproxyProductOfferingBean;
import com.tcl.dias.common.beans.VproxyQuestionnaireDet;

/**
 * 
 * @author vpachava
 *
 */

public class VproxySolutionBean {
	
	private String solutionName;

	private VproxyProductOfferingBean vproxyProductOfferingBeans;
	
	private List<VproxyQuestionnaireDet> vproxyQuestionnaireDets;

	/**
	 * @return the solutionName
	 */
	public String getSolutionName() {
		return solutionName;
	}

	/**
	 * @param solutionName the solutionName to set
	 */
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	/**
	 * @return the vproxyProductOfferingBeans
	 */
	public VproxyProductOfferingBean getVproxyProductOfferingBeans() {
		return vproxyProductOfferingBeans;
	}

	/**
	 * @param vproxyProductOfferingBeans the vproxyProductOfferingBeans to set
	 */
	public void setVproxyProductOfferingBeans(VproxyProductOfferingBean vproxyProductOfferingBeans) {
		this.vproxyProductOfferingBeans = vproxyProductOfferingBeans;
	}

	/**
	 * @return the vproxyQuestionnaireDets
	 */
	public List<VproxyQuestionnaireDet> getVproxyQuestionnaireDets() {
		return vproxyQuestionnaireDets;
	}

	/**
	 * @param vproxyQuestionnaireDets the vproxyQuestionnaireDets to set
	 */
	public void setVproxyQuestionnaireDets(List<VproxyQuestionnaireDet> vproxyQuestionnaireDets) {
		this.vproxyQuestionnaireDets = vproxyQuestionnaireDets;
	}
	
	
	
	

}
