package com.tcl.dias.common.beans;

import java.util.List;

/**
 * 
 * @author vpachava
 *
 */

public class VproxySolutionsBean {

	private String solutionName;

	private List<VproxyProductOfferingBean> vproxyProductOfferingBeans;
	
	
	private List<VproxyQuestionnaireDet> vproxyQuestionnaireDets;

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public List<VproxyProductOfferingBean> getVproxyProductOfferingBeans() {
		return vproxyProductOfferingBeans;
	}

	public void setVproxyProductOfferingBeans(List<VproxyProductOfferingBean> vproxyProductOfferingBeans) {
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
