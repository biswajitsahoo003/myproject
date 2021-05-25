package com.tcl.dias.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelchemyQOSSummaryBean {
	private String id;
	private String auditKey;
	private String cuid;
	private String parentLocation;
    private String year;
    private String month;
    private String monthShortName;
    private String direction;
    private String asr;
    private String ner;
    private String jitterTx;
    private String jitterRx;
    private String packetlossTx;
    private String packetlossRx;
    private String pmosTx;
    private String pmosRx;
    private String pdd;
    private String durationTotalMinutes;
    private String loadFileName;
    private String createdAt;
    private String updatedAt;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuditKey() {
		return auditKey;
	}
	public void setAuditKey(String auditKey) {
		this.auditKey = auditKey;
	}
	public String getCuid() {
		return cuid;
	}
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	public String getParentLocation() {
		return parentLocation;
	}
	public void setParentLocation(String parentLocation) {
		this.parentLocation = parentLocation;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMonthShortName() {
		return monthShortName;
	}
	public void setMonthShortName(String monthShortName) {
		this.monthShortName = monthShortName;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getAsr() {
		return asr;
	}
	public void setAsr(String asr) {
		this.asr = asr;
	}
	public String getNer() {
		return ner;
	}
	public void setNer(String ner) {
		this.ner = ner;
	}
	public String getJitterTx() {
		return jitterTx;
	}
	public void setJitterTx(String jitterTx) {
		this.jitterTx = jitterTx;
	}
	public String getJitterRx() {
		return jitterRx;
	}
	public void setJitterRx(String jitterRx) {
		this.jitterRx = jitterRx;
	}
	public String getPacketlossTx() {
		return packetlossTx;
	}
	public void setPacketlossTx(String packetlossTx) {
		this.packetlossTx = packetlossTx;
	}
	public String getPacketlossRx() {
		return packetlossRx;
	}
	public void setPacketlossRx(String packetlossRx) {
		this.packetlossRx = packetlossRx;
	}
	public String getPmosTx() {
		return pmosTx;
	}
	public void setPmosTx(String pmosTx) {
		this.pmosTx = pmosTx;
	}
	public String getPmosRx() {
		return pmosRx;
	}
	public void setPmosRx(String pmosRx) {
		this.pmosRx = pmosRx;
	}
	public String getPdd() {
		return pdd;
	}
	public void setPdd(String pdd) {
		this.pdd = pdd;
	}
	public String getDurationTotalMinutes() {
		return durationTotalMinutes;
	}
	public void setDurationTotalMinutes(String durationTotalMinutes) {
		this.durationTotalMinutes = durationTotalMinutes;
	}
	public String getLoadFileName() {
		return loadFileName;
	}
	public void setLoadFileName(String loadFileName) {
		this.loadFileName = loadFileName;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
		return "TelchemyQOSSummaryBean [id=" + id + ", auditKey=" + auditKey + ", cuid=" + cuid + ", parentLocation="
				+ parentLocation + ", year=" + year + ", month=" + month + ", monthShortName=" + monthShortName
				+ ", direction=" + direction + ", asr=" + asr + ", ner=" + ner + ", jitterTx=" + jitterTx
				+ ", jitterRx=" + jitterRx + ", packetlossTx=" + packetlossTx + ", packetlossRx=" + packetlossRx
				+ ", pmosTx=" + pmosTx + ", pmosRx=" + pmosRx + ", pdd=" + pdd + ", durationTotalMinutes="
				+ durationTotalMinutes + ", loadFileName=" + loadFileName + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
    
	    

}
