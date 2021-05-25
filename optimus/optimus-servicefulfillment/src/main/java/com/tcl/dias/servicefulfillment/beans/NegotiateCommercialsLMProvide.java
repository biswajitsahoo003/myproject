package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used for task - Negotiate Commercials with LM Provider.
 *
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class NegotiateCommercialsLMProvide extends TaskDetailsBaseBean {

    private String negotiateOffnetARCCost;
    private String negotiateOffnetNRCCost;
    private String negotiateOffnetMastCost;
    
    private List<AttachmentIdBean> documentIds;
    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    

    public String getNegotiateOffnetARCCost() {
        return negotiateOffnetARCCost;
    }

    public void setNegotiateOffnetARCCost(String negotiateOffnetARCCost) {
        this.negotiateOffnetARCCost = negotiateOffnetARCCost;
    }

    public String getNegotiateOffnetNRCCost() {
        return negotiateOffnetNRCCost;
    }

    public void setNegotiateOffnetNRCCost(String negotiateOffnetNRCCost) {
        this.negotiateOffnetNRCCost = negotiateOffnetNRCCost;
    }

    public String getNegotiateOffnetMastCost() { return negotiateOffnetMastCost; }

    public void setNegotiateOffnetMastCost(String negotiateOffnetMastCost) { this.negotiateOffnetMastCost = negotiateOffnetMastCost; }
}
