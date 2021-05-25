
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for initiateCLRCreation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="initiateCLRCreation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SERVICEID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SERVICETYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ScenarioType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ListOfFeasibilityId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="COPFID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="orderDetails" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}orderDetails" minOccurs="0"/&gt;
 *         &lt;element name="a_EndDetails" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}endDetails" minOccurs="0"/&gt;
 *         &lt;element name="z_EndDetails" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}endDetails" minOccurs="0"/&gt;
 *         &lt;element name="a_Prot_Offnet_IfaceDtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}protOffnetIfaceDtls" minOccurs="0"/&gt;
 *         &lt;element name="z_Prot_Offnet_IfaceDtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}protOffnetIfaceDtls" minOccurs="0"/&gt;
 *         &lt;element name="a_Wrkr_Offnet_IfaceDtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}wrkrOffnetIfaceDtls" minOccurs="0"/&gt;
 *         &lt;element name="z_Wrkr_Offnet_IfaceDtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}wrkrOffnetIfaceDtls" minOccurs="0"/&gt;
 *         &lt;element name="UCC_Serv_dtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}uccServDetails" minOccurs="0"/&gt;
 *         &lt;element name="UA_Applicable" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}uaApplicable" minOccurs="0"/&gt;
 *         &lt;element name="EHS_Service" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clr_Design_Dtls" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}clrDesignDtls" minOccurs="0"/&gt;
 *         &lt;element name="IPServiceAttributes" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}ipServiceAttributes" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalAttributes" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}additionalAttr" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "initiateCLRCreation", propOrder = {
    "serviceid",
    "servicetype",
    "scenarioType",
    "listOfFeasibilityId",
    "copfid",
    "requestID",
    "orderDetails",
    "aEndDetails",
    "zEndDetails",
    "aProtOffnetIfaceDtls",
    "zProtOffnetIfaceDtls",
    "aWrkrOffnetIfaceDtls",
    "zWrkrOffnetIfaceDtls",
    "uccServDtls",
    "uaApplicable",
    "ehsService",
    "clrDesignDtls",
    "ipServiceAttributes",
    "additionalAttributes"
})
@XmlRootElement(name = "initiateCLRCreation", namespace = "http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr")
public class InitiateCLRCreation {

    @XmlElement(name = "SERVICEID")
    protected String serviceid;
    @XmlElement(name = "SERVICETYPE")
    protected String servicetype;
    @XmlElement(name = "ScenarioType")
    protected String scenarioType;
    @XmlElement(name = "ListOfFeasibilityId")
    protected List<String> listOfFeasibilityId;
    @XmlElement(name = "COPFID")
    protected String copfid;
    @XmlElement(name = "RequestID")
    protected String requestID;
    protected OrderDetails orderDetails;
    @XmlElement(name = "a_EndDetails")
    protected EndDetails aEndDetails;
    @XmlElement(name = "z_EndDetails")
    protected EndDetails zEndDetails;
    @XmlElement(name = "a_Prot_Offnet_IfaceDtls")
    protected ProtOffnetIfaceDtls aProtOffnetIfaceDtls;
    @XmlElement(name = "z_Prot_Offnet_IfaceDtls")
    protected ProtOffnetIfaceDtls zProtOffnetIfaceDtls;
    @XmlElement(name = "a_Wrkr_Offnet_IfaceDtls")
    protected WrkrOffnetIfaceDtls aWrkrOffnetIfaceDtls;
    @XmlElement(name = "z_Wrkr_Offnet_IfaceDtls")
    protected WrkrOffnetIfaceDtls zWrkrOffnetIfaceDtls;
    @XmlElement(name = "UCC_Serv_dtls")
    protected UccServDetails uccServDtls;
    @XmlElement(name = "UA_Applicable")
    protected UaApplicable uaApplicable;
    @XmlElement(name = "EHS_Service")
    protected String ehsService;
    @XmlElement(name = "clr_Design_Dtls")
    protected ClrDesignDtls clrDesignDtls;
    @XmlElement(name = "IPServiceAttributes")
    protected IpServiceAttributes ipServiceAttributes;
    @XmlElement(name = "AdditionalAttributes")
    protected AdditionalAttr additionalAttributes;

    /**
     * Gets the value of the serviceid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERVICEID() {
        return serviceid;
    }

    /**
     * Sets the value of the serviceid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERVICEID(String value) {
        this.serviceid = value;
    }

    /**
     * Gets the value of the servicetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERVICETYPE() {
        return servicetype;
    }

    /**
     * Sets the value of the servicetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERVICETYPE(String value) {
        this.servicetype = value;
    }

    /**
     * Gets the value of the scenarioType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScenarioType() {
        return scenarioType;
    }

    /**
     * Sets the value of the scenarioType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScenarioType(String value) {
        this.scenarioType = value;
    }

    /**
     * Gets the value of the listOfFeasibilityId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listOfFeasibilityId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListOfFeasibilityId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getListOfFeasibilityId() {
        if (listOfFeasibilityId == null) {
            listOfFeasibilityId = new ArrayList<String>();
        }
        return this.listOfFeasibilityId;
    }

    /**
     * Gets the value of the copfid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOPFID() {
        return copfid;
    }

    /**
     * Sets the value of the copfid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOPFID(String value) {
        this.copfid = value;
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

    /**
     * Gets the value of the orderDetails property.
     * 
     * @return
     *     possible object is
     *     {@link OrderDetails }
     *     
     */
    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    /**
     * Sets the value of the orderDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderDetails }
     *     
     */
    public void setOrderDetails(OrderDetails value) {
        this.orderDetails = value;
    }

    /**
     * Gets the value of the aEndDetails property.
     * 
     * @return
     *     possible object is
     *     {@link EndDetails }
     *     
     */
    public EndDetails getAEndDetails() {
        return aEndDetails;
    }

    /**
     * Sets the value of the aEndDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndDetails }
     *     
     */
    public void setAEndDetails(EndDetails value) {
        this.aEndDetails = value;
    }

    /**
     * Gets the value of the zEndDetails property.
     * 
     * @return
     *     possible object is
     *     {@link EndDetails }
     *     
     */
    public EndDetails getZEndDetails() {
        return zEndDetails;
    }

    /**
     * Sets the value of the zEndDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndDetails }
     *     
     */
    public void setZEndDetails(EndDetails value) {
        this.zEndDetails = value;
    }

    /**
     * Gets the value of the aProtOffnetIfaceDtls property.
     * 
     * @return
     *     possible object is
     *     {@link ProtOffnetIfaceDtls }
     *     
     */
    public ProtOffnetIfaceDtls getAProtOffnetIfaceDtls() {
        return aProtOffnetIfaceDtls;
    }

    /**
     * Sets the value of the aProtOffnetIfaceDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtOffnetIfaceDtls }
     *     
     */
    public void setAProtOffnetIfaceDtls(ProtOffnetIfaceDtls value) {
        this.aProtOffnetIfaceDtls = value;
    }

    /**
     * Gets the value of the zProtOffnetIfaceDtls property.
     * 
     * @return
     *     possible object is
     *     {@link ProtOffnetIfaceDtls }
     *     
     */
    public ProtOffnetIfaceDtls getZProtOffnetIfaceDtls() {
        return zProtOffnetIfaceDtls;
    }

    /**
     * Sets the value of the zProtOffnetIfaceDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtOffnetIfaceDtls }
     *     
     */
    public void setZProtOffnetIfaceDtls(ProtOffnetIfaceDtls value) {
        this.zProtOffnetIfaceDtls = value;
    }

    /**
     * Gets the value of the aWrkrOffnetIfaceDtls property.
     * 
     * @return
     *     possible object is
     *     {@link WrkrOffnetIfaceDtls }
     *     
     */
    public WrkrOffnetIfaceDtls getAWrkrOffnetIfaceDtls() {
        return aWrkrOffnetIfaceDtls;
    }

    /**
     * Sets the value of the aWrkrOffnetIfaceDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link WrkrOffnetIfaceDtls }
     *     
     */
    public void setAWrkrOffnetIfaceDtls(WrkrOffnetIfaceDtls value) {
        this.aWrkrOffnetIfaceDtls = value;
    }

    /**
     * Gets the value of the zWrkrOffnetIfaceDtls property.
     * 
     * @return
     *     possible object is
     *     {@link WrkrOffnetIfaceDtls }
     *     
     */
    public WrkrOffnetIfaceDtls getZWrkrOffnetIfaceDtls() {
        return zWrkrOffnetIfaceDtls;
    }

    /**
     * Sets the value of the zWrkrOffnetIfaceDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link WrkrOffnetIfaceDtls }
     *     
     */
    public void setZWrkrOffnetIfaceDtls(WrkrOffnetIfaceDtls value) {
        this.zWrkrOffnetIfaceDtls = value;
    }

    /**
     * Gets the value of the uccServDtls property.
     * 
     * @return
     *     possible object is
     *     {@link UccServDetails }
     *     
     */
    public UccServDetails getUCCServDtls() {
        return uccServDtls;
    }

    /**
     * Sets the value of the uccServDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link UccServDetails }
     *     
     */
    public void setUCCServDtls(UccServDetails value) {
        this.uccServDtls = value;
    }

    /**
     * Gets the value of the uaApplicable property.
     * 
     * @return
     *     possible object is
     *     {@link UaApplicable }
     *     
     */
    public UaApplicable getUAApplicable() {
        return uaApplicable;
    }

    /**
     * Sets the value of the uaApplicable property.
     * 
     * @param value
     *     allowed object is
     *     {@link UaApplicable }
     *     
     */
    public void setUAApplicable(UaApplicable value) {
        this.uaApplicable = value;
    }

    /**
     * Gets the value of the ehsService property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEHSService() {
        return ehsService;
    }

    /**
     * Sets the value of the ehsService property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEHSService(String value) {
        this.ehsService = value;
    }

    /**
     * Gets the value of the clrDesignDtls property.
     * 
     * @return
     *     possible object is
     *     {@link ClrDesignDtls }
     *     
     */
    public ClrDesignDtls getClrDesignDtls() {
        return clrDesignDtls;
    }

    /**
     * Sets the value of the clrDesignDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClrDesignDtls }
     *     
     */
    public void setClrDesignDtls(ClrDesignDtls value) {
        this.clrDesignDtls = value;
    }

    /**
     * Gets the value of the ipServiceAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link IpServiceAttributes }
     *     
     */
    public IpServiceAttributes getIPServiceAttributes() {
        return ipServiceAttributes;
    }

    /**
     * Sets the value of the ipServiceAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link IpServiceAttributes }
     *     
     */
    public void setIPServiceAttributes(IpServiceAttributes value) {
        this.ipServiceAttributes = value;
    }

    /**
     * Gets the value of the additionalAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalAttr }
     *     
     */
    public AdditionalAttr getAdditionalAttributes() {
        return additionalAttributes;
    }

    /**
     * Sets the value of the additionalAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalAttr }
     *     
     */
    public void setAdditionalAttributes(AdditionalAttr value) {
        this.additionalAttributes = value;
    }

}
