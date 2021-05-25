
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUBackboneIORInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUBackboneIORInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interface" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ALUBackboneEthernetInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneEthernetInterface" minOccurs="0"/>
 *                   &lt;element name="ALUBackboneSerialInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneSerialInterface" minOccurs="0"/>
 *                   &lt;element name="ALUBackbonePOSInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackbonePOSInterface" minOccurs="0"/>
 *                   &lt;element name="ALUBackboneLAGInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneLAGInterface" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "ALUBackboneIORInterface", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "_interface"
})
public class ALUBackboneIORInterface {

    @XmlElement(name = "interface")
    protected ALUBackboneIORInterface.Interface _interface;

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link ALUBackboneIORInterface.Interface }
     *     
     */
    public ALUBackboneIORInterface.Interface getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link ALUBackboneIORInterface.Interface }
     *     
     */
    public void setInterface(ALUBackboneIORInterface.Interface value) {
        this._interface = value;
    }


    /**
     * 
     * 							<p> For dot1Q encapusulation based
     * 							IORs, SVLAN is applicable. </p> <br
     * 							/>
     * 						
     * 
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="ALUBackboneEthernetInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneEthernetInterface" minOccurs="0"/>
     *         &lt;element name="ALUBackboneSerialInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneSerialInterface" minOccurs="0"/>
     *         &lt;element name="ALUBackbonePOSInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackbonePOSInterface" minOccurs="0"/>
     *         &lt;element name="ALUBackboneLAGInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneLAGInterface" minOccurs="0"/>
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
        "aluBackboneEthernetInterface",
        "aluBackboneSerialInterface",
        "aluBackbonePOSInterface",
        "aluBackboneLAGInterface"
    })
    public static class Interface {

        @XmlElement(name = "ALUBackboneEthernetInterface")
        protected ALUBackboneEthernetInterface aluBackboneEthernetInterface;
        @XmlElement(name = "ALUBackboneSerialInterface")
        protected ALUBackboneSerialInterface aluBackboneSerialInterface;
        @XmlElement(name = "ALUBackbonePOSInterface")
        protected ALUBackbonePOSInterface aluBackbonePOSInterface;
        @XmlElement(name = "ALUBackboneLAGInterface")
        protected ALUBackboneLAGInterface aluBackboneLAGInterface;

        /**
         * Gets the value of the aluBackboneEthernetInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ALUBackboneEthernetInterface }
         *     
         */
        public ALUBackboneEthernetInterface getALUBackboneEthernetInterface() {
            return aluBackboneEthernetInterface;
        }

        /**
         * Sets the value of the aluBackboneEthernetInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ALUBackboneEthernetInterface }
         *     
         */
        public void setALUBackboneEthernetInterface(ALUBackboneEthernetInterface value) {
            this.aluBackboneEthernetInterface = value;
        }

        /**
         * Gets the value of the aluBackboneSerialInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ALUBackboneSerialInterface }
         *     
         */
        public ALUBackboneSerialInterface getALUBackboneSerialInterface() {
            return aluBackboneSerialInterface;
        }

        /**
         * Sets the value of the aluBackboneSerialInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ALUBackboneSerialInterface }
         *     
         */
        public void setALUBackboneSerialInterface(ALUBackboneSerialInterface value) {
            this.aluBackboneSerialInterface = value;
        }

        /**
         * Gets the value of the aluBackbonePOSInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ALUBackbonePOSInterface }
         *     
         */
        public ALUBackbonePOSInterface getALUBackbonePOSInterface() {
            return aluBackbonePOSInterface;
        }

        /**
         * Sets the value of the aluBackbonePOSInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ALUBackbonePOSInterface }
         *     
         */
        public void setALUBackbonePOSInterface(ALUBackbonePOSInterface value) {
            this.aluBackbonePOSInterface = value;
        }

        /**
         * Gets the value of the aluBackboneLAGInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ALUBackboneLAGInterface }
         *     
         */
        public ALUBackboneLAGInterface getALUBackboneLAGInterface() {
            return aluBackboneLAGInterface;
        }

        /**
         * Sets the value of the aluBackboneLAGInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ALUBackboneLAGInterface }
         *     
         */
        public void setALUBackboneLAGInterface(ALUBackboneLAGInterface value) {
            this.aluBackboneLAGInterface = value;
        }

    }

}
