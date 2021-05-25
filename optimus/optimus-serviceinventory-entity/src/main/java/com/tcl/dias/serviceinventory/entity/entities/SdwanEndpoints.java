package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Table to have sdwan endpoint based on directory location.
 * @author archchan
 */
@Entity
@Table(name="sdwan_endpoints")
@NamedQuery(name = "SdwanEndpoints.findAll", query = "SELECT s FROM SdwanEndpoints s")
public class SdwanEndpoints implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="op_param")
	private String opParam;

	@Column(name="server_code")
	private String serverCode;

	@Column(name="server_ip")
	private String serverIp;

	@Column(name="server_locaion")
	private String serverLocaion;

	@Column(name="server_password")
	private String serverPassword;

	@Column(name="server_port")
	private String serverPort;

	@Column(name="server_username")
	private String serverUsername;

	private byte status;

	@Column(name="vendor_type")
	private String vendorType;
	
	@Column(name="is_director")
	private byte isDirector;

	public SdwanEndpoints() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpParam() {
		return this.opParam;
	}

	public void setOpParam(String opParam) {
		this.opParam = opParam;
	}

	public String getServerCode() {
		return this.serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerLocaion() {
		return this.serverLocaion;
	}

	public void setServerLocaion(String serverLocaion) {
		this.serverLocaion = serverLocaion;
	}

	public String getServerPassword() {
		return this.serverPassword;
	}

	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

	public String getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerUsername() {
		return this.serverUsername;
	}

	public void setServerUsername(String serverUsername) {
		this.serverUsername = serverUsername;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getVendorType() {
		return this.vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public byte getIsDirector() {
		return isDirector;
	}

	public void setIsDirector(byte isDirector) {
		this.isDirector = isDirector;
	}
	

}
