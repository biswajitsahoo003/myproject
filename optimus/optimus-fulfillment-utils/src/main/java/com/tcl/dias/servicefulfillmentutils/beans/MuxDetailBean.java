package com.tcl.dias.servicefulfillmentutils.beans;

public class MuxDetailBean extends TaskDetailsBaseBean {

    private String endMUXNodeIP;
    private String endMUXNodeName;
    private String endMuxNodePort;
    
	private String isPEInternalCablingRequired;
	private String peEndPhysicalPort;
	
	private String lastmileException;
	private String lastmileConnectScenerio;
	private String remarks;
	private String action;
	
	private String skipAutoClr;
	private String exceptionClousureCategory;
	private String exceptionClosureRemarks;

		    
    public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLastmileException() {
		return lastmileException;
	}

	public void setLastmileException(String lastmileException) {
		this.lastmileException = lastmileException;
	}

	public String getLastmileConnectScenerio() {
		return lastmileConnectScenerio;
	}

	public void setLastmileConnectScenerio(String lastmileConnectScenerio) {
		this.lastmileConnectScenerio = lastmileConnectScenerio;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIsPEInternalCablingRequired() {
		return isPEInternalCablingRequired;
	}

	public void setIsPEInternalCablingRequired(String isPEInternalCablingRequired) {
		this.isPEInternalCablingRequired = isPEInternalCablingRequired;
	}

	public String getEndMuxNodePort() {
		return endMuxNodePort;
	}

	public void setEndMuxNodePort(String endMuxNodePort) {
		this.endMuxNodePort = endMuxNodePort;
	}

	public String getEndMUXNodeIP() {
        return endMUXNodeIP;
    }

    public void setEndMUXNodeIP(String endMUXNodeIP) {
        this.endMUXNodeIP = endMUXNodeIP;
    }

    public String getEndMUXNodeName() {
        return endMUXNodeName;
    }

    public void setEndMUXNodeName(String endMUXNodeName) {
        this.endMUXNodeName = endMUXNodeName;
    }
       

    public String getPeEndPhysicalPort() {
		return peEndPhysicalPort;
	}

	public void setPeEndPhysicalPort(String peEndPhysicalPort) {
		this.peEndPhysicalPort = peEndPhysicalPort;
	}

	public String getSkipAutoClr() {
		return skipAutoClr;
	}

	public void setSkipAutoClr(String skipAutoClr) {
		this.skipAutoClr = skipAutoClr;
	}

	public String getExceptionClousureCategory() {
		return exceptionClousureCategory;
	}

	public void setExceptionClousureCategory(String exceptionClousureCategory) {
		this.exceptionClousureCategory = exceptionClousureCategory;
	}

	public String getExceptionClosureRemarks() {
		return exceptionClosureRemarks;
	}

	public void setExceptionClosureRemarks(String exceptionClosureRemarks) {
		this.exceptionClosureRemarks = exceptionClosureRemarks;
	}

	@Override
    public String toString() {
        return "MuxDetailBean{" +
                "endMUXNodeIP='" + endMUXNodeIP + '\'' +
                ", endMUXNodeName='" + endMUXNodeName + '\'' +
                '}';
    }
}
