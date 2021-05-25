package com.tcl.dias.wfe.drools.feasibilty;

public class Feasiblity {
private Integer bandwidth = new Integer(0);
private String lastMile="";
private String connectiontype="";
private String category="";
private int Rank;
private String srchcondition="";

public Integer getBandwidth() {
	return bandwidth;
}
public void setBandwidth(Integer bandwidth) {
	this.bandwidth = bandwidth;
}
public String getSrchcondition() {
	return srchcondition;
}
public void setSrchcondition(String srchcondition) {
	this.srchcondition = srchcondition;
}
public String getLastMile() {
	return lastMile;
}
public void setLastMile(String lastMile) {
	this.lastMile = lastMile;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}

public String getConnectiontype() {
	return connectiontype;
}
public int getRank() {
	return Rank;
}
public void setRank(int rank) {
	Rank = rank;
}
public void setConnectiontype(String connectiontype) {
	this.connectiontype = connectiontype;
}
public void applyDiscount(String bwtype) {
	this.srchcondition = bwtype;
}
}
