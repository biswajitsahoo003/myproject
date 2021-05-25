package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the LeStatInfoBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LeStateInfoBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3729064557484187536L;
	private List<LeStateInfo> leStateInfos;

	/**
	 * @return the leStateInfos
	 */
	public List<LeStateInfo> getLeStateInfos() {
		if(leStateInfos==null) {
			leStateInfos=new ArrayList<>();
		}
		return leStateInfos;
	}

	/**
	 * @param leStateInfos the leStateInfos to set
	 */
	public void setLeStateInfos(List<LeStateInfo> leStateInfos) {
		this.leStateInfos = leStateInfos;
	}
	
	
	

}
