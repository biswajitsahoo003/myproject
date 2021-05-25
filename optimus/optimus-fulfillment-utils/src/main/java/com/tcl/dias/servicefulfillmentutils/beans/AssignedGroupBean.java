package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *AssignedGroupBean used to group based on assigne name
 */
public class AssignedGroupBean {

	private String groupName;

	private Long openCount=0L;

	private Long assignetCount=0L;

	private Long closedCount=0L;

	private Long holdCount=0L;
	
	private Long pendingCount=0L;
	
	private Long inprogressCount=0L;
	
	private Long reopenCount=0L;



	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	

	public Long getOpenCount() {
		return openCount;
	}

	public void setOpenCount(Long openCount) {
		this.openCount = openCount;
	}

	public Long getAssignetCount() {
		return assignetCount;
	}

	public void setAssignetCount(Long assignetCount) {
		this.assignetCount = assignetCount;
	}

	public Long getClosedCount() {
		return closedCount;
	}

	public void setClosedCount(Long closedCount) {
		this.closedCount = closedCount;
	}

	public Long getHoldCount() {
		return holdCount;
	}

	public void setHoldCount(Long holdCount) {
		this.holdCount = holdCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssignedGroupBean other = (AssignedGroupBean) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

	public Long getPeningCount() {
		return pendingCount;
	}

	public void setPeningCount(Long peningCount) {
		this.pendingCount = peningCount;
	}

	public Long getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(Long pendingCount) {
		this.pendingCount = pendingCount;
	}

	public Long getInprogressCount() {
		return inprogressCount;
	}

	public void setInprogressCount(Long inprogressCount) {
		this.inprogressCount = inprogressCount;
	}

	public Long getReopenCount() {
		return reopenCount;
	}

	public void setReopenCount(Long reopenCount) {
		this.reopenCount = reopenCount;
	}
	
	

}
