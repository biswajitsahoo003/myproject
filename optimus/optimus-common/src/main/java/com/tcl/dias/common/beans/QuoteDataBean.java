package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * Class for QuoteData bean
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteDataBean implements Serializable{

private static final long serialVersionUID = -4091763322418987748L;

private String quoteCode;
private String stage;
private String fromValue;

public QuoteDataBean() {
	
}

public QuoteDataBean(String quoteCode, String stage, String fromValue) {
	this.quoteCode = quoteCode;
	this.stage = stage;
	this.fromValue = fromValue;
}

/**
 * @return the quoteCode
 */
public String getQuoteCode() {
	return quoteCode;
}
/**
 * @param quoteCode the quoteCode to set
 */
public void setQuoteCode(String quoteCode) {
	this.quoteCode = quoteCode;
}
/**
 * @return the stage
 */
public String getStage() {
	return stage;
}
/**
 * @param stage the stage to set
 */
public void setStage(String stage) {
	this.stage = stage;
}
/**
 * @return the fromValue
 */
public String getFromValue() {
	return fromValue;
}
/**
 * @param fromValue the fromValue to set
 */
public void setFromValue(String fromValue) {
	this.fromValue = fromValue;
}


}
