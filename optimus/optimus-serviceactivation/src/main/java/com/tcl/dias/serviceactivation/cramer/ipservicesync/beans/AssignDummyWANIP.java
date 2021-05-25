
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.tcl.dias.serviceactivation.beans.CramerServiceHeader;

/**
 * <p>
 * Java class for assignDummyWANIP complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="assignDummyWANIP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices}cramerServiceHeader" minOccurs="0"/>
 *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VPNID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastmileType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "assignDummyWANIP", propOrder = { "header", "serviceID", "vpnid", "lastmileType" })
@XmlRootElement
public class AssignDummyWANIP {

	protected CramerServiceHeader header;
	@XmlElement(name = "ServiceID")
	protected String serviceID;
	@XmlElement(name = "VPNID")
	protected String vpnid;
	protected String lastmileType;

	/**
	 * Gets the value of the header property.
	 * 
	 * @return possible object is {@link CramerServiceHeader }
	 * 
	 */
	public CramerServiceHeader getHeader() {
		return header;
	}

	/**
	 * Sets the value of the header property.
	 * 
	 * @param value allowed object is {@link CramerServiceHeader }
	 * 
	 */
	public void setHeader(CramerServiceHeader value) {
		this.header = value;
	}

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
	 * Gets the value of the vpnid property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVPNID() {
		return vpnid;
	}

	/**
	 * Sets the value of the vpnid property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setVPNID(String value) {
		this.vpnid = value;
	}

	/**
	 * Gets the value of the lastmileType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLastmileType() {
		return lastmileType;
	}

	/**
	 * Sets the value of the lastmileType property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setLastmileType(String value) {
		this.lastmileType = value;
	}

}
