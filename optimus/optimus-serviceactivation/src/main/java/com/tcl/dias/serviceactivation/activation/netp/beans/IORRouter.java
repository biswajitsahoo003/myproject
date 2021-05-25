
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IORRouter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IORRouter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ActivationCommonLibrary/ace/common/bo/_20113_01}Device">
 *       &lt;sequence>
 *         &lt;element name="iorinterface" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="BackboneIORInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}backboneIORInterface" minOccurs="0"/>
 *                   &lt;element name="GenericIORInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}genericIORInterface" minOccurs="0"/>
 *                   &lt;element name="LAGIORInterface" type="{http://www.tcl.com/2014/2/ipsvc/xsd}LAGIORInterface" minOccurs="0"/>
 *                   &lt;element name="ALUBackboneIORInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneIORInterface" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IORRouter", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2012/_12", propOrder = {
    "iorinterface"
})
public class IORRouter
    extends Device
{

    protected IORRouter.Iorinterface iorinterface;

    /**
     * Gets the value of the iorinterface property.
     * 
     * @return
     *     possible object is
     *     {@link IORRouter.Iorinterface }
     *     
     */
    public IORRouter.Iorinterface getIorinterface() {
        return iorinterface;
    }

    /**
     * Sets the value of the iorinterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link IORRouter.Iorinterface }
     *     
     */
    public void setIorinterface(IORRouter.Iorinterface value) {
        this.iorinterface = value;
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
     *         &lt;element name="BackboneIORInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}backboneIORInterface" minOccurs="0"/>
     *         &lt;element name="GenericIORInterface" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}genericIORInterface" minOccurs="0"/>
     *         &lt;element name="LAGIORInterface" type="{http://www.tcl.com/2014/2/ipsvc/xsd}LAGIORInterface" minOccurs="0"/>
     *         &lt;element name="ALUBackboneIORInterface" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneIORInterface" minOccurs="0"/>
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
        "backboneIORInterface",
        "genericIORInterface",
        "lagiorInterface",
        "aluBackboneIORInterface"
    })
    public static class Iorinterface {

        @XmlElement(name = "BackboneIORInterface")
        protected BackboneIORInterface backboneIORInterface;
        @XmlElement(name = "GenericIORInterface")
        protected GenericIORInterface genericIORInterface;
        @XmlElement(name = "LAGIORInterface")
        protected LAGIORInterface lagiorInterface;
        @XmlElement(name = "ALUBackboneIORInterface")
        protected ALUBackboneIORInterface aluBackboneIORInterface;

        /**
         * Gets the value of the backboneIORInterface property.
         * 
         * @return
         *     possible object is
         *     {@link BackboneIORInterface }
         *     
         */
        public BackboneIORInterface getBackboneIORInterface() {
            return backboneIORInterface;
        }

        /**
         * Sets the value of the backboneIORInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link BackboneIORInterface }
         *     
         */
        public void setBackboneIORInterface(BackboneIORInterface value) {
            this.backboneIORInterface = value;
        }

        /**
         * Gets the value of the genericIORInterface property.
         * 
         * @return
         *     possible object is
         *     {@link GenericIORInterface }
         *     
         */
        public GenericIORInterface getGenericIORInterface() {
            return genericIORInterface;
        }

        /**
         * Sets the value of the genericIORInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link GenericIORInterface }
         *     
         */
        public void setGenericIORInterface(GenericIORInterface value) {
            this.genericIORInterface = value;
        }

        /**
         * Gets the value of the lagiorInterface property.
         * 
         * @return
         *     possible object is
         *     {@link LAGIORInterface }
         *     
         */
        public LAGIORInterface getLAGIORInterface() {
            return lagiorInterface;
        }

        /**
         * Sets the value of the lagiorInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link LAGIORInterface }
         *     
         */
        public void setLAGIORInterface(LAGIORInterface value) {
            this.lagiorInterface = value;
        }

        /**
         * Gets the value of the aluBackboneIORInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ALUBackboneIORInterface }
         *     
         */
        public ALUBackboneIORInterface getALUBackboneIORInterface() {
            return aluBackboneIORInterface;
        }

        /**
         * Sets the value of the aluBackboneIORInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ALUBackboneIORInterface }
         *     
         */
        public void setALUBackboneIORInterface(ALUBackboneIORInterface value) {
            this.aluBackboneIORInterface = value;
        }

    }

}
