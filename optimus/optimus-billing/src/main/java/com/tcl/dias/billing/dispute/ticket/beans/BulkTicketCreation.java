
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
 *         &lt;element name="inputDetails" type="{http://CESV/NGP_CES_Bulk_Ticket.tws}NGP_CES_Bulk_Ticket_Details"/&gt;
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
    "inputDetails"
})
@XmlRootElement(name = "bulkTicketCreation")
public class BulkTicketCreation {

    @XmlElement(required = true)
    protected NGPCESBulkTicketDetails inputDetails;

    /**
     * Gets the value of the inputDetails property.
     * 
     * @return
     *     possible object is
     *     {@link NGPCESBulkTicketDetails }
     *     
     */
    public NGPCESBulkTicketDetails getInputDetails() {
        return inputDetails;
    }

    /**
     * Sets the value of the inputDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link NGPCESBulkTicketDetails }
     *     
     */
    public void setInputDetails(NGPCESBulkTicketDetails value) {
        this.inputDetails = value;
    }

}
