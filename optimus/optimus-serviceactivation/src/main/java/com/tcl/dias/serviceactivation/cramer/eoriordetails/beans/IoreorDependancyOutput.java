
package com.tcl.dias.serviceactivation.cramer.eoriordetails.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ioreorDependancyOutput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ioreorDependancyOutput"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DependancyFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="EORList" type="{http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails}eorList" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="IORList" type="{http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails}iorList" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ObjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ObjectType" type="{http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails}objectType" minOccurs="0"/&gt;
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ioreorDependancyOutput", propOrder = {
    "dependancyFlag",
    "eorList",
    "iorList",
    "objectName",
    "objectType",
    "requestID"
})
public class IoreorDependancyOutput {

    @XmlElement(name = "DependancyFlag")
    protected Boolean dependancyFlag;
    @XmlElement(name = "EORList")
    protected List<EorList> eorList;
    @XmlElement(name = "IORList")
    protected List<IorList> iorList;
    @XmlElement(name = "ObjectName")
    protected String objectName;
    @XmlElement(name = "ObjectType")
    @XmlSchemaType(name = "string")
    protected ObjectType objectType;
    @XmlElement(name = "RequestID")
    protected String requestID;

    /**
     * Gets the value of the dependancyFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDependancyFlag() {
        return dependancyFlag;
    }

    /**
     * Sets the value of the dependancyFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDependancyFlag(Boolean value) {
        this.dependancyFlag = value;
    }

    /**
     * Gets the value of the eorList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eorList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEORList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EorList }
     * 
     * 
     */
    public List<EorList> getEORList() {
        if (eorList == null) {
            eorList = new ArrayList<EorList>();
        }
        return this.eorList;
    }

    /**
     * Gets the value of the iorList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iorList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIORList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IorList }
     * 
     * 
     */
    public List<IorList> getIORList() {
        if (iorList == null) {
            iorList = new ArrayList<IorList>();
        }
        return this.iorList;
    }

    /**
     * Gets the value of the objectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Sets the value of the objectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectName(String value) {
        this.objectName = value;
    }

    /**
     * Gets the value of the objectType property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectType }
     *     
     */
    public ObjectType getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectType }
     *     
     */
    public void setObjectType(ObjectType value) {
        this.objectType = value;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

}
