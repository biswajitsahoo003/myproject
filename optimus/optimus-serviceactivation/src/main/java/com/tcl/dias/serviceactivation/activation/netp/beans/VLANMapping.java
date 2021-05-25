
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				<p> For new orders the action type for both SVLAN
 * 				and CVLAN entries will be ADD </p> <br />
 * 				<p> For Change orders following will be the logic
 * 				</p> <p> If SVLAN does not change then the
 * 				action type for the SVLAN will be NOT_APPLICABLE.
 * 				</p> <p> If SVLAN changes then the action
 * 				type will be first remove the old SVLAN and and ADD for
 * 				the new SVLAN </p> <br /> <p> For
 * 				CVLAN ranges </p> <p>
 * 				1.&nbsp;&nbsp;&nbsp;&nbsp;if a range
 * 				gets extended ex: 100-200 becomes 100-300, then 100-200
 * 				will be with action NOT_APPLICABLE and 201-300 will be
 * 				with action ADD </p> <p>
 * 				&nbsp;&nbsp;&nbsp;&nbsp;if a range gets
 * 				</p> <p> 2. if a
 * 				range&nbsp;gets&nbsp;modified ex: 100-200
 * 				becomes&nbsp;150-300 then 100-149 will have action
 * 				REMOVE and&nbsp;201&nbsp;to 300&nbsp;will
 * 				have
 * 				action&nbsp;ADD&nbsp;&nbsp;&nbsp;&nbsp;
 * 				</p> <br /> <p> 3. If a range gets
 * 				replaced with a totally non overlapping
 * 				range&nbsp;ex: 100-200 becomes 300-400 then
 * 				100-200&nbsp;will have action type REMOVE
 * 				and&nbsp;300-400&nbsp;will have action type ADD
 * 				</p> <br /> <p> For terminations the
 * 				action type for all will be REMOVE </p> <br
 * 				/>
 * 			
 * 
 * <p>Java class for VLANMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VLANMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="S1VLAN" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" minOccurs="0"/>
 *         &lt;element name="CVLAN" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enableTagging" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
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
@XmlType(name = "VLANMapping", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "s1VLAN",
    "cvlan",
    "priority",
    "enableTagging"
})
public class VLANMapping {

    @XmlElement(name = "S1VLAN")
    protected HuaweiVLANs s1VLAN;
    @XmlElement(name = "CVLAN")
    protected List<HuaweiVLANs> cvlan;
    @XmlElement(name = "Priority")
    protected String priority;
    protected String enableTagging;

    /**
     * Gets the value of the s1VLAN property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiVLANs }
     *     
     */
    public HuaweiVLANs getS1VLAN() {
        return s1VLAN;
    }

    /**
     * Sets the value of the s1VLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiVLANs }
     *     
     */
    public void setS1VLAN(HuaweiVLANs value) {
        this.s1VLAN = value;
    }

    /**
     * Gets the value of the cvlan property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cvlan property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCVLAN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HuaweiVLANs }
     * 
     * 
     */
    public List<HuaweiVLANs> getCVLAN() {
        if (cvlan == null) {
            cvlan = new ArrayList<HuaweiVLANs>();
        }
        return this.cvlan;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the enableTagging property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableTagging() {
        return enableTagging;
    }

    /**
     * Sets the value of the enableTagging property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableTagging(String value) {
        this.enableTagging = value;
    }

}
