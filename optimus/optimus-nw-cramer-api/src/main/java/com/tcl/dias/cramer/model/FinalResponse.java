package com.tcl.dias.cramer.model;

import java.util.List;

public class FinalResponse<T> {

	String status;
	String message;
	List<T> list;
	
	public FinalResponse(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public FinalResponse(String status, String message, List<T> list) {
		super();
		this.status = status;
		this.message = message;
		this.list = list;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	
	
	
	
}
