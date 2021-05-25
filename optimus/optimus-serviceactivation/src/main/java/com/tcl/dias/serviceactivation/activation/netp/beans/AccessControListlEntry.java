
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccessControListlEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccessControListlEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SeqNo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sourceSubnet" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
 *                   &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
 *                   &lt;element name="any" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="ANY"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="destinationSubnet" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
 *                   &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
 *                   &lt;element name="any" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="ANY"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="protocol" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TCP"/>
 *               &lt;enumeration value="UDP"/>
 *               &lt;enumeration value="IP"/>
 *               &lt;enumeration value="ICMP"/>
 *               &lt;enumeration value="IPV6"/>
 *               &lt;enumeration value="NONE"/>
 *               &lt;enumeration value="CRTP"/>
 *               &lt;enumeration value="CRUDP"/>
 *               &lt;enumeration value="EGP"/>
 *               &lt;enumeration value="EIGRP"/>
 *               &lt;enumeration value="ENCAP"/>
 *               &lt;enumeration value="ETHER-IP"/>
 *               &lt;enumeration value="GRE"/>
 *               &lt;enumeration value="IDRP"/>
 *               &lt;enumeration value="IGMP"/>
 *               &lt;enumeration value="IGP"/>
 *               &lt;enumeration value="IPV6-FRAG"/>
 *               &lt;enumeration value="IPV6-ICMP"/>
 *               &lt;enumeration value="IPV6-NO-NXT"/>
 *               &lt;enumeration value="IPV6-OPTS"/>
 *               &lt;enumeration value="IPV6-ROUTE"/>
 *               &lt;enumeration value="ISIS"/>
 *               &lt;enumeration value="L2TP"/>
 *               &lt;enumeration value="OSPF-IGPLPIM"/>
 *               &lt;enumeration value="PNNI"/>
 *               &lt;enumeration value="PTP"/>
 *               &lt;enumeration value="RDPLRSVP"/>
 *               &lt;enumeration value="STP"/>
 *               &lt;enumeration value="VRRP"/>
 *               &lt;enumeration value="ISO-IP"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="portNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="condition" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="EQUALS"/>
 *               &lt;enumeration value="NOT_EQUALS"/>
 *               &lt;enumeration value="LESS_THAN"/>
 *               &lt;enumeration value="GREATER_THAN"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sourcePortNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sourceCondition" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="EQUALS"/>
 *               &lt;enumeration value="NOT_EQUALS"/>
 *               &lt;enumeration value="LESS_THAN"/>
 *               &lt;enumeration value="GREATER_THAN"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccessControListlEntry", propOrder = {
    "seqNo",
    "action",
    "sourceSubnet",
    "destinationSubnet",
    "protocol",
    "portNumber",
    "condition",
    "description",
    "sourcePortNumber",
    "sourceCondition",
    "isObjectInstanceModified"
})
public class AccessControListlEntry {

    @XmlElement(name = "SeqNo")
    protected Integer seqNo;
    @XmlElement(name = "Action")
    protected String action;
    protected AccessControListlEntry.SourceSubnet sourceSubnet;
    protected AccessControListlEntry.DestinationSubnet destinationSubnet;
    protected String protocol;
    protected String portNumber;
    protected String condition;
    protected String description;
    protected String sourcePortNumber;
    protected String sourceCondition;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the seqNo property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSeqNo() {
        return seqNo;
    }

    /**
     * Sets the value of the seqNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSeqNo(Integer value) {
        this.seqNo = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the sourceSubnet property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControListlEntry.SourceSubnet }
     *     
     */
    public AccessControListlEntry.SourceSubnet getSourceSubnet() {
        return sourceSubnet;
    }

    /**
     * Sets the value of the sourceSubnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControListlEntry.SourceSubnet }
     *     
     */
    public void setSourceSubnet(AccessControListlEntry.SourceSubnet value) {
        this.sourceSubnet = value;
    }

    /**
     * Gets the value of the destinationSubnet property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControListlEntry.DestinationSubnet }
     *     
     */
    public AccessControListlEntry.DestinationSubnet getDestinationSubnet() {
        return destinationSubnet;
    }

    /**
     * Sets the value of the destinationSubnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControListlEntry.DestinationSubnet }
     *     
     */
    public void setDestinationSubnet(AccessControListlEntry.DestinationSubnet value) {
        this.destinationSubnet = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the portNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortNumber() {
        return portNumber;
    }

    /**
     * Sets the value of the portNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortNumber(String value) {
        this.portNumber = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCondition(String value) {
        this.condition = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the sourcePortNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcePortNumber() {
        return sourcePortNumber;
    }

    /**
     * Sets the value of the sourcePortNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcePortNumber(String value) {
        this.sourcePortNumber = value;
    }

    /**
     * Gets the value of the sourceCondition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceCondition() {
        return sourceCondition;
    }

    /**
     * Sets the value of the sourceCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceCondition(String value) {
        this.sourceCondition = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectInstanceModified(Boolean value) {
        this.isObjectInstanceModified = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
     *         &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
     *         &lt;element name="any" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;enumeration value="ANY"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "v4Address",
        "v6Address",
        "any"
    })
    public static class DestinationSubnet {

        protected IPV4Address v4Address;
        protected IPV6Address v6Address;
        protected String any;

        /**
         * Gets the value of the v4Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV4Address }
         *     
         */
        public IPV4Address getV4Address() {
            return v4Address;
        }

        /**
         * Sets the value of the v4Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV4Address }
         *     
         */
        public void setV4Address(IPV4Address value) {
            this.v4Address = value;
        }

        /**
         * Gets the value of the v6Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV6Address }
         *     
         */
        public IPV6Address getV6Address() {
            return v6Address;
        }

        /**
         * Sets the value of the v6Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV6Address }
         *     
         */
        public void setV6Address(IPV6Address value) {
            this.v6Address = value;
        }

        /**
         * Gets the value of the any property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAny() {
            return any;
        }

        /**
         * Sets the value of the any property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAny(String value) {
            this.any = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
     *         &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
     *         &lt;element name="any" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;enumeration value="ANY"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "v4Address",
        "v6Address",
        "any"
    })
    public static class SourceSubnet {

        protected IPV4Address v4Address;
        protected IPV6Address v6Address;
        protected String any;

        /**
         * Gets the value of the v4Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV4Address }
         *     
         */
        public IPV4Address getV4Address() {
            return v4Address;
        }

        /**
         * Sets the value of the v4Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV4Address }
         *     
         */
        public void setV4Address(IPV4Address value) {
            this.v4Address = value;
        }

        /**
         * Gets the value of the v6Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV6Address }
         *     
         */
        public IPV6Address getV6Address() {
            return v6Address;
        }

        /**
         * Sets the value of the v6Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV6Address }
         *     
         */
        public void setV6Address(IPV6Address value) {
            this.v6Address = value;
        }

        /**
         * Gets the value of the any property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAny() {
            return any;
        }

        /**
         * Sets the value of the any property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAny(String value) {
            this.any = value;
        }

    }

}
