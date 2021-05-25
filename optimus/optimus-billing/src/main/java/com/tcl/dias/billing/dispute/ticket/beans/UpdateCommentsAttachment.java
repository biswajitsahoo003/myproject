
package com.tcl.dias.billing.dispute.ticket.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="remarks" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ticketNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="updatedBy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="attachmentContent" type="{http://CESV/NGP_CES_Update_Comments_Attachment.tws}ArrayOf_string"/&gt;
 *         &lt;element name="attachmentName" type="{http://CESV/NGP_CES_Update_Comments_Attachment.tws}ArrayOf_string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "remarks",
    "ticketNo",
    "updatedBy",
    "attachmentContent",
    "attachmentName"
})
@XmlRootElement(name = "updateCommentsAttachment", namespace = "http://CESV/NGP_CES_Update_Comments_Attachment.tws")
public class UpdateCommentsAttachment {

    @XmlElement(namespace = "http://CESV/NGP_CES_Update_Comments_Attachment.tws", required = true)
    protected String remarks;
    @XmlElement(namespace = "http://CESV/NGP_CES_Update_Comments_Attachment.tws", required = true)
    protected String ticketNo;
    @XmlElement(namespace = "http://CESV/NGP_CES_Update_Comments_Attachment.tws", required = true)
    protected String updatedBy;
    @XmlElement(namespace = "http://CESV/NGP_CES_Update_Comments_Attachment.tws", required = true)
    protected ArrayOfString3 attachmentContent;
    @XmlElement(namespace = "http://CESV/NGP_CES_Update_Comments_Attachment.tws", required = true)
    protected ArrayOfString3 attachmentName;

    /**
     * Gets the value of the remarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of the remarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarks(String value) {
        this.remarks = value;
    }

    /**
     * Gets the value of the ticketNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketNo() {
        return ticketNo;
    }

    /**
     * Sets the value of the ticketNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketNo(String value) {
        this.ticketNo = value;
    }

    /**
     * Gets the value of the updatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the value of the updatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdatedBy(String value) {
        this.updatedBy = value;
    }

    /**
     * Gets the value of the attachmentContent property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString3 }
     *     
     */
    public ArrayOfString3 getAttachmentContent() {
        return attachmentContent;
    }

    /**
     * Sets the value of the attachmentContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString3 }
     *     
     */
    public void setAttachmentContent(ArrayOfString3 value) {
        this.attachmentContent = value;
    }

    /**
     * Gets the value of the attachmentName property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString3 }
     *     
     */
    public ArrayOfString3 getAttachmentName() {
        return attachmentName;
    }

    /**
     * Sets the value of the attachmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString3 }
     *     
     */
    public void setAttachmentName(ArrayOfString3 value) {
        this.attachmentName = value;
    }

}
