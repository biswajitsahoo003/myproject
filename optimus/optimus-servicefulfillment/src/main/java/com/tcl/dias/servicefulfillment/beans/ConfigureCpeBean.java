package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 *  
 * ConfigureCpeBean to configure cpe 
 */
public class ConfigureCpeBean extends TaskDetailsBaseBean {

	private List<AttachmentIdBean> documentIds;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

}
