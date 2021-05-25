package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Bean class for GscPorting
 *
 * @author VISHESH AWASTHI
 */
public class GscPorting {
	private Long totalNumbers;
	Byte isProtingRequired;
	List<GscTfnBean> tfnBeans;

	public Byte getIsPortingRequired() {
		return isProtingRequired;
	}

	public void setIsPortingRequired(Byte isPortingRequired) {
		this.isProtingRequired = isPortingRequired;
	}

	public List<GscTfnBean> getTfnBeans() {
		return tfnBeans;
	}

	public void setTfnBeans(List<GscTfnBean> tfnBeans) {
		this.tfnBeans = tfnBeans;
	}

	public Long getTotalNumbers() {
		return totalNumbers;
	}

	public void setTotalNumbers(Long totalNumbers) {
		this.totalNumbers = totalNumbers;
	}

	@Override
	public String toString() {
		return "GscPorting [isProtingRequired=" + isProtingRequired + ", tfnBeans=" + tfnBeans + "]";
	}

}