package com.tcl.dias.oms.beans;

/**
 * 
 * This file contains the bean to display the Old and New price of the IPC MACD
 * order which will used in order summary screen.
 * 
 *
 * @author DimpleS
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class IPCMACDPricingBeanResponse {

	private double oldArc;
	private double oldMrc;
	private double oldNrc;
	private double newArc;
	private double newMrc;
	private double newNrc;

	public double getOldArc() {
		return oldArc;
	}

	public void setOldArc(double oldArc) {
		this.oldArc = oldArc;
	}

	public double getOldMrc() {
		return oldMrc;
	}

	public void setOldMrc(double oldMrc) {
		this.oldMrc = oldMrc;
	}

	public double getOldNrc() {
		return oldNrc;
	}

	public void setOldNrc(double oldNrc) {
		this.oldNrc = oldNrc;
	}

	public double getNewArc() {
		return newArc;
	}

	public void setNewArc(double newArc) {
		this.newArc = newArc;
	}

	public double getNewMrc() {
		return newMrc;
	}

	public void setNewMrc(double newMrc) {
		this.newMrc = newMrc;
	}

	public double getNewNrc() {
		return newNrc;
	}

	public void setNewNrc(double newNrc) {
		this.newNrc = newNrc;
	}

}
