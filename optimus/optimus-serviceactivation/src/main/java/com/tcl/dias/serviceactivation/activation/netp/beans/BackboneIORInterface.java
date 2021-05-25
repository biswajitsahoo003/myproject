
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for backboneIORInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="backboneIORInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interface" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="BackbonePOSInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBonePOSInterface" minOccurs="0"/>
 *                   &lt;element name="BackboneEthernetInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBoneEthernetInterface" minOccurs="0"/>
 *                   &lt;element name="BackboneSerialInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBoneSerialInterface" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "backboneIORInterface", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_02", propOrder = {
    "_interface",
    "isObjectInstanceModified"
})
public class BackboneIORInterface {

    @XmlElement(name = "interface")
    protected BackboneIORInterface.Interface _interface;
    protected String isObjectInstanceModified;

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link BackboneIORInterface.Interface }
     *     
     */
    public BackboneIORInterface.Interface getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link BackboneIORInterface.Interface }
     *     
     */
    public void setInterface(BackboneIORInterface.Interface value) {
        this._interface = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsObjectInstanceModified(String value) {
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
     *         &lt;element name="BackbonePOSInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBonePOSInterface" minOccurs="0"/>
     *         &lt;element name="BackboneEthernetInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBoneEthernetInterface" minOccurs="0"/>
     *         &lt;element name="BackboneSerialInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBoneSerialInterface" minOccurs="0"/>
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
        "backbonePOSInterface",
        "backboneEthernetInterface",
        "backboneSerialInterface"
    })
    public static class Interface {

        @XmlElement(name = "BackbonePOSInterface")
        protected BackBonePOSInterface backbonePOSInterface;
        @XmlElement(name = "BackboneEthernetInterface")
        protected BackBoneEthernetInterface backboneEthernetInterface;
        @XmlElement(name = "BackboneSerialInterface")
        protected BackBoneSerialInterface backboneSerialInterface;

        /**
         * Gets the value of the backbonePOSInterface property.
         * 
         * @return
         *     possible object is
         *     {@link BackBonePOSInterface }
         *     
         */
        public BackBonePOSInterface getBackbonePOSInterface() {
            return backbonePOSInterface;
        }

        /**
         * Sets the value of the backbonePOSInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link BackBonePOSInterface }
         *     
         */
        public void setBackbonePOSInterface(BackBonePOSInterface value) {
            this.backbonePOSInterface = value;
        }

        /**
         * Gets the value of the backboneEthernetInterface property.
         * 
         * @return
         *     possible object is
         *     {@link BackBoneEthernetInterface }
         *     
         */
        public BackBoneEthernetInterface getBackboneEthernetInterface() {
            return backboneEthernetInterface;
        }

        /**
         * Sets the value of the backboneEthernetInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link BackBoneEthernetInterface }
         *     
         */
        public void setBackboneEthernetInterface(BackBoneEthernetInterface value) {
            this.backboneEthernetInterface = value;
        }

        /**
         * Gets the value of the backboneSerialInterface property.
         * 
         * @return
         *     possible object is
         *     {@link BackBoneSerialInterface }
         *     
         */
        public BackBoneSerialInterface getBackboneSerialInterface() {
            return backboneSerialInterface;
        }

        /**
         * Sets the value of the backboneSerialInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link BackBoneSerialInterface }
         *     
         */
        public void setBackboneSerialInterface(BackBoneSerialInterface value) {
            this.backboneSerialInterface = value;
        }

    }

}
