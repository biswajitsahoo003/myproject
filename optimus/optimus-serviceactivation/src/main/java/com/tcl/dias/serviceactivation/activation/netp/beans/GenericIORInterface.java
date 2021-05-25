
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for genericIORInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genericIORInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interface" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ethernetInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}genericIOREthernetInterface" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "genericIORInterface", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_02", propOrder = {
    "_interface",
    "isObjectInstanceModified"
})
public class GenericIORInterface {

    @XmlElement(name = "interface")
    protected GenericIORInterface.Interface _interface;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link GenericIORInterface.Interface }
     *     
     */
    public GenericIORInterface.Interface getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenericIORInterface.Interface }
     *     
     */
    public void setInterface(GenericIORInterface.Interface value) {
        this._interface = value;
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
     *         &lt;element name="ethernetInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}genericIOREthernetInterface" minOccurs="0"/>
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
        "ethernetInterface"
    })
    public static class Interface {

        protected GenericIOREthernetInterface ethernetInterface;

        /**
         * Gets the value of the ethernetInterface property.
         * 
         * @return
         *     possible object is
         *     {@link GenericIOREthernetInterface }
         *     
         */
        public GenericIOREthernetInterface getEthernetInterface() {
            return ethernetInterface;
        }

        /**
         * Sets the value of the ethernetInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link GenericIOREthernetInterface }
         *     
         */
        public void setEthernetInterface(GenericIOREthernetInterface value) {
            this.ethernetInterface = value;
        }

    }

}
