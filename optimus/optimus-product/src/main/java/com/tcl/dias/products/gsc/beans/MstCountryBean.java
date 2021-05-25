package com.tcl.dias.products.gsc.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Master Country Bean for GSC Specific Products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MstCountryBean {

	private Integer id;

	private String code;

	private String name;

	private String source;

	private Byte status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MstCountryBean{" + "id=" + id + ", code='" + code + '\'' + ", name='" + name + '\'' + ", source='"
				+ source + '\'' + ", status=" + status + '}';
	}
}
