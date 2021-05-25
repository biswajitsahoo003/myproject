package com.tcl.dias.oms.beans;

/**
 * This class is the bean class used for Compare Quotes API for component prices
 * @author SURUCHIA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class ComponentQuotePrices {
	
	private String componentName;
	private double oldQuote;
	private double newQuote;
	private double delta;
	
	public ComponentQuotePrices() {
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public double getOldQuote() {
		return oldQuote;
	}

	public void setOldQuote(double oldQuote) {
		this.oldQuote = oldQuote;
	}

	public double getNewQuote() {
		return newQuote;
	}

	public void setNewQuote(double newQuote) {
		this.newQuote = newQuote;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}


	@Override
	public String toString() {
		return "ComponentQuotePrices{" +
				"componentName='" + componentName + '\'' +
				", oldQuote=" + oldQuote +
				", newQuote=" + newQuote +
				", delta=" + delta +
				'}';
	}
}
