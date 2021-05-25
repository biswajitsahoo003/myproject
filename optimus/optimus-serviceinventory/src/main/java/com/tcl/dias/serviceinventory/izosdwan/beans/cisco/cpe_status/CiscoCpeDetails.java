
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CiscoCpeDetails implements Serializable {

	@JsonProperty("vdevice-name")
	private String deviceName;
	@JsonProperty("rx-errors")
	private String rxErrors;
	@JsonProperty("tx-kbps")
	private String txkbps;
	@JsonProperty("if-admin-status")
	private String ifadminstatus;
	@JsonProperty("tcp-mss-adjust")
	private String tcpmssadjust;
	@JsonProperty("tx-errors")
	private String txerrors;
	@JsonProperty("tx-pps")
	private String txpps;
	@JsonProperty("ifname")
	private String ifname;
	@JsonProperty("rx-pps")
	private String rxpps;
	@JsonProperty("af-type")
	private String aftype;
	@JsonProperty("shaping-rate")
	private String shapingrate;
	@JsonProperty("if-oper-status")
	private String ifoperstatus;
	@JsonProperty("ifindex")
	private String ifindex;
	@JsonProperty("if-tracker-status")
	private String iftrackerstatus;
	@JsonProperty("num-flaps")
	private String numflaps;
	@JsonProperty("rx-packets")
	private String rxpackets;
	@JsonProperty("vpn-id")
	private String vpnid;
	@JsonProperty("vdevice-host-name")
	private String vdevicehostname;
	@JsonProperty("mtu")
	private String mtu;
	@JsonProperty("rx-drops")
	private String rxdrops;
	@JsonProperty("tx-drops")
	private String txdrops;
	@JsonProperty("ipv6-address")
	private String ipv6address;
	@JsonProperty("hwaddr")
	private String hwaddr;
	@JsonProperty("ip-address")
	private String ipaddress;
	@JsonProperty("vdevice-dataKey")
	private String vdevicedataKey;
	@JsonProperty("tx-octets")
	private String txoctets;
	@JsonProperty("tx-packets")
	private String txpackets;
	@JsonProperty("rx-octets")
	private String rxoctets;
	@JsonProperty("rx-kbps")
	private String rxkbps;
	@JsonProperty("lastupdated")
	private String lastupdated;
	@JsonProperty("port-type")
	private String porttype;
	@JsonProperty("encap-type")
	private String encaptype;

	@JsonProperty("vdevice-name")
	public String getDeviceName() {
		return deviceName;
	}

	@JsonProperty("vdevice-name")
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@JsonProperty("rx-errors")
	public String getRxErrors() {
		return rxErrors;
	}

	@JsonProperty("rx-errors")
	public void setRxErrors(String rxErrors) {
		this.rxErrors = rxErrors;
	}

	@JsonProperty("tx-kbps")
	public String getTxkbps() {
		return txkbps;
	}

	@JsonProperty("tx-kbps")
	public void setTxkbps(String txkbps) {
		this.txkbps = txkbps;
	}

	@JsonProperty("if-admin-status")
	public String getIfadminstatus() {
		return ifadminstatus;
	}

	@JsonProperty("if-admin-status")
	public void setIfadminstatus(String ifadminstatus) {
		this.ifadminstatus = ifadminstatus;
	}

	@JsonProperty("tcp-mss-adjust")
	public String getTcpmssadjust() {
		return tcpmssadjust;
	}

	@JsonProperty("tcp-mss-adjust")
	public void setTcpmssadjust(String tcpmssadjust) {
		this.tcpmssadjust = tcpmssadjust;
	}

	@JsonProperty("tx-errors")
	public String getTxerrors() {
		return txerrors;
	}

	@JsonProperty("tx-errors")
	public void setTxerrors(String txerrors) {
		this.txerrors = txerrors;
	}

	@JsonProperty("tx-pps")
	public String getTxpps() {
		return txpps;
	}

	@JsonProperty("tx-pps")
	public void setTxpps(String txpps) {
		this.txpps = txpps;
	}

	@JsonProperty("ifname")
	public String getIfname() {
		return ifname;
	}

	@JsonProperty("ifname")
	public void setIfname(String ifname) {
		this.ifname = ifname;
	}

	@JsonProperty("rx-pps")
	public String getRxpps() {
		return rxpps;
	}

	@JsonProperty("rx-pps")
	public void setRxpps(String rxpps) {
		this.rxpps = rxpps;
	}

	@JsonProperty("af-type")
	public String getAftype() {
		return aftype;
	}

	@JsonProperty("af-type")
	public void setAftype(String aftype) {
		this.aftype = aftype;
	}

	@JsonProperty("shaping-rate")
	public String getShapingrate() {
		return shapingrate;
	}

	@JsonProperty("shaping-rate")
	public void setShapingrate(String shapingrate) {
		this.shapingrate = shapingrate;
	}

	@JsonProperty("if-oper-status")
	public String getIfoperstatus() {
		return ifoperstatus;
	}

	@JsonProperty("if-oper-status")
	public void setIfoperstatus(String ifoperstatus) {
		this.ifoperstatus = ifoperstatus;
	}

	@JsonProperty("ifindex")
	public String getIfindex() {
		return ifindex;
	}

	@JsonProperty("ifindex")
	public void setIfindex(String ifindex) {
		this.ifindex = ifindex;
	}

	@JsonProperty("if-tracker-status")
	public String getIftrackerstatus() {
		return iftrackerstatus;
	}

	@JsonProperty("if-tracker-status")
	public void setIftrackerstatus(String iftrackerstatus) {
		this.iftrackerstatus = iftrackerstatus;
	}

	@JsonProperty("num-flaps")
	public String getNumflaps() {
		return numflaps;
	}

	@JsonProperty("num-flaps")
	public void setNumflaps(String numflaps) {
		this.numflaps = numflaps;
	}

	@JsonProperty("rx-packets")
	public String getRxpackets() {
		return rxpackets;
	}

	@JsonProperty("rx-packets")
	public void setRxpackets(String rxpackets) {
		this.rxpackets = rxpackets;
	}

	@JsonProperty("vpn-id")
	public String getVpnid() {
		return vpnid;
	}

	@JsonProperty("vpn-id")
	public void setVpnid(String vpnid) {
		this.vpnid = vpnid;
	}

	@JsonProperty("vdevice-host-name")
	public String getVdevicehostname() {
		return vdevicehostname;
	}

	@JsonProperty("vdevice-host-name")
	public void setVdevicehostname(String vdevicehostname) {
		this.vdevicehostname = vdevicehostname;
	}

	@JsonProperty("mtu")
	public String getMtu() {
		return mtu;
	}

	@JsonProperty("mtu")
	public void setMtu(String mtu) {
		this.mtu = mtu;
	}

	@JsonProperty("rx-drops")
	public String getRxdrops() {
		return rxdrops;
	}

	@JsonProperty("rx-drops")
	public void setRxdrops(String rxdrops) {
		this.rxdrops = rxdrops;
	}

	@JsonProperty("tx-drops")
	public String getTxdrops() {
		return txdrops;
	}

	@JsonProperty("tx-drops")
	public void setTxdrops(String txdrops) {
		this.txdrops = txdrops;
	}

	@JsonProperty("ipv6-address")
	public String getIpv6address() {
		return ipv6address;
	}

	@JsonProperty("ipv6-address")
	public void setIpv6address(String ipv6address) {
		this.ipv6address = ipv6address;
	}

	@JsonProperty("hwaddr")
	public String getHwaddr() {
		return hwaddr;
	}

	@JsonProperty("hwaddr")
	public void setHwaddr(String hwaddr) {
		this.hwaddr = hwaddr;
	}

	@JsonProperty("ip-address")
	public String getIpaddress() {
		return ipaddress;
	}

	@JsonProperty("ip-address")
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	@JsonProperty("vdevice-dataKey")
	public String getVdevicedataKey() {
		return vdevicedataKey;
	}

	@JsonProperty("vdevice-dataKey")
	public void setVdevicedataKey(String vdevicedataKey) {
		this.vdevicedataKey = vdevicedataKey;
	}

	@JsonProperty("tx-octets")
	public String getTxoctets() {
		return txoctets;
	}

	@JsonProperty("tx-octets")
	public void setTxoctets(String txoctets) {
		this.txoctets = txoctets;
	}

	@JsonProperty("tx-packets")
	public String getTxpackets() {
		return txpackets;
	}

	@JsonProperty("tx-packets")
	public void setTxpackets(String txpackets) {
		this.txpackets = txpackets;
	}

	@JsonProperty("rx-octets")
	public String getRxoctets() {
		return rxoctets;
	}

	@JsonProperty("rx-octets")
	public void setRxoctets(String rxoctets) {
		this.rxoctets = rxoctets;
	}

	@JsonProperty("rx-kbps")
	public String getRxkbps() {
		return rxkbps;
	}

	@JsonProperty("rx-kbps")
	public void setRxkbps(String rxkbps) {
		this.rxkbps = rxkbps;
	}

	@JsonProperty("lastupdated")
	public String getLastupdated() {
		return lastupdated;
	}

	@JsonProperty("lastupdated")
	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}

	@JsonProperty("port-type")
	public String getPorttype() {
		return porttype;
	}

	@JsonProperty("port-type")
	public void setPorttype(String porttype) {
		this.porttype = porttype;
	}

	@JsonProperty("encap-type")
	public String getEncaptype() {
		return encaptype;
	}

	@JsonProperty("encap-type")
	public void setEncaptype(String encaptype) {
		this.encaptype = encaptype;
	}

	@Override
	public String toString() {
		return "CiscoCpeDetails [deviceName=" + deviceName + ", rxErrors=" + rxErrors + ", txkbps=" + txkbps
				+ ", ifadminstatus=" + ifadminstatus + ", tcpmssadjust=" + tcpmssadjust + ", txerrors=" + txerrors
				+ ", txpps=" + txpps + ", ifname=" + ifname + ", rxpps=" + rxpps + ", aftype=" + aftype
				+ ", shapingrate=" + shapingrate + ", ifoperstatus=" + ifoperstatus + ", ifindex=" + ifindex
				+ ", iftrackerstatus=" + iftrackerstatus + ", numflaps=" + numflaps + ", rxpackets=" + rxpackets
				+ ", vpnid=" + vpnid + ", vdevicehostname=" + vdevicehostname + ", mtu=" + mtu + ", rxdrops=" + rxdrops
				+ ", txdrops=" + txdrops + ", ipv6address=" + ipv6address + ", hwaddr=" + hwaddr + ", ipaddress="
				+ ipaddress + ", vdevicedataKey=" + vdevicedataKey + ", txoctets=" + txoctets + ", txpackets="
				+ txpackets + ", rxoctets=" + rxoctets + ", rxkbps=" + rxkbps + ", lastupdated=" + lastupdated
				+ ", porttype=" + porttype + ", encaptype=" + encaptype + "]";
	}

}