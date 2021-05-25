package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class EgressRoutingProfileBean extends TaskDetailsBaseBean{

	private String ccaInventoryUpdate;
	private String ccaInventoryUpdateChangeRequestNumber;
	private String egressRoutingCompletionDate;
	
	public String getCcaInventoryUpdate() {
		return ccaInventoryUpdate;
	}
	public void setCcaInventoryUpdate(String ccaInventoryUpdate) {
		this.ccaInventoryUpdate = ccaInventoryUpdate;
	}
	public String getCcaInventoryUpdateChangeRequestNumber() {
		return ccaInventoryUpdateChangeRequestNumber;
	}
	public void setCcaInventoryUpdateChangeRequestNumber(String ccaInventoryUpdateChangeRequestNumber) {
		this.ccaInventoryUpdateChangeRequestNumber = ccaInventoryUpdateChangeRequestNumber;
	}
	public String getEgressRoutingCompletionDate() {
		return egressRoutingCompletionDate;
	}
	public void setEgressRoutingCompletionDate(String egressRoutingCompletionDate) {
		this.egressRoutingCompletionDate = egressRoutingCompletionDate;
	}
	
}
