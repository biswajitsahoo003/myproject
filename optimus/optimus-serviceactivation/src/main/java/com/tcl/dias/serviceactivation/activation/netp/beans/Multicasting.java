
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Multicasting complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Multicasting">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wanPIMMode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="SPARSE"/>
 *               &lt;enumeration value="DENSE"/>
 *               &lt;enumeration value="SPARSE-DENSE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="type" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="AUTORP"/>
 *               &lt;enumeration value="STATICRP"/>
 *               &lt;enumeration value="BSR"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="defaultMDT" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="dataMDT" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}DataMDT" minOccurs="0"/>
 *         &lt;element name="RPAddress" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}RPAddress" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RPLoopbackList" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RPLoopbackList" minOccurs="0"/>
 *         &lt;element name="RPLocation" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PE"/>
 *               &lt;enumeration value="CE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="autoDiscoveryOption" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="MDT_SAFI"/>
 *               &lt;enumeration value="DEFAULT"/>
 *               &lt;enumeration value="NONE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Multicasting", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "wanPIMMode",
    "type",
    "defaultMDT",
    "dataMDT",
    "rpAddress",
    "rpLoopbackList",
    "rpLocation",
    "autoDiscoveryOption"
})
public class Multicasting {

    protected String wanPIMMode;
    protected String type;
    protected IPV4Address defaultMDT;
    protected DataMDT dataMDT;
    @XmlElement(name = "RPAddress")
    protected List<RPAddress> rpAddress;
    @XmlElement(name = "RPLoopbackList")
    protected RPLoopbackList rpLoopbackList;
    @XmlElement(name = "RPLocation")
    protected String rpLocation;
    protected String autoDiscoveryOption;

    /**
     * Gets the value of the wanPIMMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWanPIMMode() {
        return wanPIMMode;
    }

    /**
     * Sets the value of the wanPIMMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWanPIMMode(String value) {
        this.wanPIMMode = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the defaultMDT property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getDefaultMDT() {
        return defaultMDT;
    }

    /**
     * Sets the value of the defaultMDT property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setDefaultMDT(IPV4Address value) {
        this.defaultMDT = value;
    }

    /**
     * Gets the value of the dataMDT property.
     * 
     * @return
     *     possible object is
     *     {@link DataMDT }
     *     
     */
    public DataMDT getDataMDT() {
        return dataMDT;
    }

    /**
     * Sets the value of the dataMDT property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataMDT }
     *     
     */
    public void setDataMDT(DataMDT value) {
        this.dataMDT = value;
    }

    /**
     * Gets the value of the rpAddress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rpAddress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRPAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RPAddress }
     * 
     * 
     */
    public List<RPAddress> getRPAddress() {
        if (rpAddress == null) {
            rpAddress = new ArrayList<RPAddress>();
        }
        return this.rpAddress;
    }

    /**
     * Gets the value of the rpLoopbackList property.
     * 
     * @return
     *     possible object is
     *     {@link RPLoopbackList }
     *     
     */
    public RPLoopbackList getRPLoopbackList() {
        return rpLoopbackList;
    }

    /**
     * Sets the value of the rpLoopbackList property.
     * 
     * @param value
     *     allowed object is
     *     {@link RPLoopbackList }
     *     
     */
    public void setRPLoopbackList(RPLoopbackList value) {
        this.rpLoopbackList = value;
    }

    /**
     * Gets the value of the rpLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRPLocation() {
        return rpLocation;
    }

    /**
     * Sets the value of the rpLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRPLocation(String value) {
        this.rpLocation = value;
    }

    /**
     * Gets the value of the autoDiscoveryOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoDiscoveryOption() {
        return autoDiscoveryOption;
    }

    /**
     * Sets the value of the autoDiscoveryOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoDiscoveryOption(String value) {
        this.autoDiscoveryOption = value;
    }

	public List<RPAddress> getRpAddress() {
		return rpAddress;
	}

	public void setRpAddress(List<RPAddress> rpAddress) {
		this.rpAddress = rpAddress;
	}

	public RPLoopbackList getRpLoopbackList() {
		return rpLoopbackList;
	}

	public void setRpLoopbackList(RPLoopbackList rpLoopbackList) {
		this.rpLoopbackList = rpLoopbackList;
	}

	public String getRpLocation() {
		return rpLocation;
	}

	public void setRpLocation(String rpLocation) {
		this.rpLocation = rpLocation;
	}
    

}
