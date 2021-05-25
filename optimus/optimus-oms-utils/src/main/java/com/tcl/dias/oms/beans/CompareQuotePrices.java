package com.tcl.dias.oms.beans;

import java.util.List;
/**
 * This class is the bean class for quoteCompare method used for Compare Quotes API
 * @author SURUCHIA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class CompareQuotePrices {
	private String name;
	private double oldQuote;
	private double newQuote;
	private double delta;
	private List<ComponentQuotePrices> components;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getOldQuotePrice() {
		return oldQuote;
	}

	public void setOldQuotePrice(double oldQuotePrice) {
		this.oldQuote = oldQuotePrice;
	}

	public double getNewQuotePrice() {
		return newQuote;
	}

	public void setNewQuotePrice(double newQuotePrice) {
		this.newQuote = newQuotePrice;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public List<ComponentQuotePrices> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentQuotePrices> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "CompareQuotePrices [name=" + name + ", oldQuote=" + oldQuote + ", newQuote=" + newQuote + ", delta="
				+ delta + ", components=" + components + "]";
	}
}
