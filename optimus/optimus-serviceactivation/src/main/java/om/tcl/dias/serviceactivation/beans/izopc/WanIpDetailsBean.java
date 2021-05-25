package om.tcl.dias.serviceactivation.beans.izopc;

import java.io.Serializable;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class WanIpDetailsBean extends BaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String wanIpAddress;
	private String secondaryWanIpAddress;
	private String vsnlWanIpAddress;
	private String secondaryVsnlWanIpAddress;
	private String wanIpPool;
	private String secondaryWanIpPool;
	private String publicNATIp;
	private String remarks;
	
	public String getWanIpAddress() {
		return wanIpAddress;
	}
	public void setWanIpAddress(String wanIpAddress) {
		this.wanIpAddress = wanIpAddress;
	}
	public String getSecondaryWanIpAddress() {
		return secondaryWanIpAddress;
	}
	public void setSecondaryWanIpAddress(String secondaryWanIpAddress) {
		this.secondaryWanIpAddress = secondaryWanIpAddress;
	}
	public String getVsnlWanIpAddress() {
		return vsnlWanIpAddress;
	}
	public void setVsnlWanIpAddress(String vsnlWanIpAddress) {
		this.vsnlWanIpAddress = vsnlWanIpAddress;
	}
	public String getSecondaryVsnlWanIpAddress() {
		return secondaryVsnlWanIpAddress;
	}
	public void setSecondaryVsnlWanIpAddress(String secondaryVsnlWanIpAddress) {
		this.secondaryVsnlWanIpAddress = secondaryVsnlWanIpAddress;
	}
	public String getWanIpPool() {
		return wanIpPool;
	}
	public void setWanIpPool(String wanIpPool) {
		this.wanIpPool = wanIpPool;
	}
	public String getSecondaryWanIpPool() {
		return secondaryWanIpPool;
	}
	public void setSecondaryWanIpPool(String secondaryWanIpPool) {
		this.secondaryWanIpPool = secondaryWanIpPool;
	}
	public String getPublicNATIp() {
		return publicNATIp;
	}
	public void setPublicNATIp(String publicNATIp) {
		this.publicNATIp = publicNATIp;
	}
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}