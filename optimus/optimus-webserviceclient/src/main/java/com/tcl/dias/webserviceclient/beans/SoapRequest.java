package com.tcl.dias.webserviceclient.beans;

/**
 * SoapRequest Class is used for all the SOAP calls
 * 
 *
 * @author SAMUEL S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public class SoapRequest {
	
	private Object requestObject;
	private String requestString;
	private String url="";
	private String soapActionUri="";
	private int connectionTimeout;
	private int readTimeout;	
	private String pType="digest";
	private String soapVersion="11";
	private String contextPath="";
	private String soapUserName;
	private String soapPwd;
	private String startTag;
	private String endTag;
	
	
	public Object getRequestObject() {
		return requestObject;
	}
	public void setRequestObject(Object requestObject) {
		this.requestObject = requestObject;
	}
	public String getRequestString() {
		return requestString;
	}
	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSoapActionUri() {
		return soapActionUri;
	}
	public void setSoapActionUri(String soapActionUri) {
		this.soapActionUri = soapActionUri;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public String getPType() {
		return pType;
	}
	public void setPType(String pType) {
		this.pType = pType;
	}
	public String getSoapVersion() {
		return soapVersion;
	}
	public void setSoapVersion(String soapVersion) {
		this.soapVersion = soapVersion;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}	
	public String getSoapUserName() {
		return soapUserName;
	}
	public void setSoapUserName(String soapUserName) {
		this.soapUserName = soapUserName;
	}
	public String getSoapPwd() {
		return soapPwd;
	}
	public void setSoapPwd(String soapPwd) {
		this.soapPwd = soapPwd;
	}
	public String getStartTag() {
		return startTag;
	}
	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}
	public String getEndTag() {
		return endTag;
	}
	public void setEndTag(String endTag) {
		this.endTag = endTag;
	}
	
}
