package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.List;

/**
 * This file contains the request for removing the unselected profile solutions
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public class ProfileRequest implements Serializable {

	
	private static final long serialVersionUID = 1260184617121843406L;
	
	private List<String> profilesToRemove;

	public List<String> getProfilesToRemove() {
		return profilesToRemove;
	}

	public void setProfilesToRemove(List<String> profilesToRemove) {
		this.profilesToRemove = profilesToRemove;
	}
	
	


}
 