
package com.tcl.dias.serviceactivation.cramer.muxsync;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for getMuxDetails complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="getMuxDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COPFID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestingSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AEndHandOffDtls" type="{http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail}handOffDetail" minOccurs="0"/>
 *         &lt;element name="ZEndHandOffDtls" type="{http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail}handOffDetail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMuxDetails", propOrder = { "serviceID", "copfid", "requestID", "requestingSystem", "requestType",
		"orderType", "aEndHandOffDtls", "zEndHandOffDtls" })
@XmlRootElement
public class GetMuxDetails {

	@XmlElement(name = "ServiceID")
	protected String serviceID;
	@XmlElement(name = "COPFID")
	protected String copfid;
	@XmlElement(name = "RequestID")
	protected String requestID;
	@XmlElement(name = "RequestingSystem")
	protected String requestingSystem;
	@XmlElement(name = "RequestType")
	protected String requestType;
	@XmlElement(name = "OrderType")
	protected String orderType;
	@XmlElement(name = "AEndHandOffDtls")
	protected HandOffDetail aEndHandOffDtls;
	@XmlElement(name = "ZEndHandOffDtls")
	protected HandOffDetail zEndHandOffDtls;

	/**
	 * Gets the value of the serviceID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getServiceID() {
		return serviceID;
	}

	/**
	 * Sets the value of the serviceID property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setServiceID(String value) {
		this.serviceID = value;
	}

	/**
	 * Gets the value of the copfid property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCOPFID() {
		return copfid;
	}

	/**
	 * Sets the value of the copfid property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCOPFID(String value) {
		this.copfid = value;
	}

	/**
	 * Gets the value of the requestID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRequestID() {
		return requestID;
	}

	/**
	 * Sets the value of the requestID property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRequestID(String value) {
		this.requestID = value;
	}

	/**
	 * Gets the value of the requestingSystem property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRequestingSystem() {
		return requestingSystem;
	}

	/**
	 * Sets the value of the requestingSystem property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRequestingSystem(String value) {
		this.requestingSystem = value;
	}

	/**
	 * Gets the value of the requestType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * Sets the value of the requestType property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRequestType(String value) {
		this.requestType = value;
	}

	/**
	 * Gets the value of the orderType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * Sets the value of the orderType property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOrderType(String value) {
		this.orderType = value;
	}

	/**
	 * Gets the value of the aEndHandOffDtls property.
	 * 
	 * @return possible object is {@link HandOffDetail }
	 * 
	 */
	public HandOffDetail getAEndHandOffDtls() {
		return aEndHandOffDtls;
	}

	/**
	 * Sets the value of the aEndHandOffDtls property.
	 * 
	 * @param value allowed object is {@link HandOffDetail }
	 * 
	 */
	public void setAEndHandOffDtls(HandOffDetail value) {
		this.aEndHandOffDtls = value;
	}

	/**
	 * Gets the value of the zEndHandOffDtls property.
	 * 
	 * @return possible object is {@link HandOffDetail }
	 * 
	 */
	public HandOffDetail getZEndHandOffDtls() {
		return zEndHandOffDtls;
	}

	/**
	 * Sets the value of the zEndHandOffDtls property.
	 * 
	 * @param value allowed object is {@link HandOffDetail }
	 * 
	 */
	public void setZEndHandOffDtls(HandOffDetail value) {
		this.zEndHandOffDtls = value;
	}

}
