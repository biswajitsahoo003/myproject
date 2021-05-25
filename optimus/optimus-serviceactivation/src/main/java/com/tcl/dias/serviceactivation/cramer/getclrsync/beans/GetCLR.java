
package com.tcl.dias.serviceactivation.cramer.getclrsync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.tcl.dias.serviceactivation.beans.CramerServiceHeader;
import com.tcl.dias.serviceactivation.beans.ObjectType;


/**
 * <p>Java class for getCLR complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCLR">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async}cramerServiceHeader" minOccurs="0"/>
 *         &lt;element name="objectType" type="{http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async}objectType" minOccurs="0"/>
 *         &lt;element name="objectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="relationshipType" type="{http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async}relationShip" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCLR", propOrder = {
    "header",
    "objectType",
    "objectName",
    "relationshipType"
})
@XmlRootElement
public class GetCLR {

    protected CramerServiceHeader header;
    @XmlSchemaType(name = "string")
    protected ObjectType objectType;
    protected String objectName;
    @XmlSchemaType(name = "string")
    protected RelationShip relationshipType;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link CramerServiceHeader }
     *     
     */
    public CramerServiceHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerServiceHeader }
     *     
     */
    public void setHeader(CramerServiceHeader value) {
        this.header = value;
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
     * Gets the value of the relationshipType property.
     * 
     * @return
     *     possible object is
     *     {@link RelationShip }
     *     
     */
    public RelationShip getRelationshipType() {
        return relationshipType;
    }

    /**
     * Sets the value of the relationshipType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelationShip }
     *     
     */
    public void setRelationshipType(RelationShip value) {
        this.relationshipType = value;
    }

}
