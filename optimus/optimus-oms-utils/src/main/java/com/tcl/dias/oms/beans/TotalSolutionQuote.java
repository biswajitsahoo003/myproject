package com.tcl.dias.oms.beans;
/**
 * This class is the bean class for total quotes used in Compare Quotes
 * @author SURUCHIA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class TotalSolutionQuote {
	
	private String name;
	private Double oldQuote;
	private Double newQuote;
	private Double delta;
	private String currencyType;
	
	public Double getOldQuote() {
		return oldQuote;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOldQuote(Double oldQuote) {
		this.oldQuote = oldQuote;
	}
	public Double getNewQuote() {
		return newQuote;
	}
	public void setNewQuote(Double newQuote) {
		this.newQuote = newQuote;
	}
	public Double getDelta() {
		return delta;
	}
	public void setDelta(Double delta) {
		this.delta = delta;
	}


	
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	@Override
	public String toString() {
		return "TotalSolutionQuote [name=" + name + ", oldQuote=" + oldQuote + ", newQuote=" + newQuote + ", delta="
				+ delta + ", currencyType=" + currencyType + "]";
	}
	
	

}
