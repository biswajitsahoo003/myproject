package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Bean for Complete IBD Work
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class IBDBean extends TaskDetailsBaseBean {

	private List<AttachmentIdBean> documentIds;
	
	private String completionDate;

	private Double cableLength;

	private String fms;

	private String typeOfInternalCabling;

	private String gisLocationId;

	private String typeOfRack;

	private String rackSize;

	private String rackName;

	private Double rackWidth;

	private String rackHeight;

	private Double rackDepth;
	private String rackId;
	private String cableId;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}

	public Double getCableLength() {
		return cableLength;
	}

	public void setCableLength(Double cableLength) {
		this.cableLength = cableLength;
	}

	public String getFms() {
		return fms;
	}

	public void setFms(String fms) {
		this.fms = fms;
	}

	public String getTypeOfInternalCabling() {
		return typeOfInternalCabling;
	}

	public void setTypeOfInternalCabling(String typeOfInternalCabling) {
		this.typeOfInternalCabling = typeOfInternalCabling;
	}

	public String getGisLocationId() {
		return gisLocationId;
	}

	public void setGisLocationId(String gisLocationId) {
		this.gisLocationId = gisLocationId;
	}

	public String getTypeOfRack() {
		return typeOfRack;
	}

	public void setTypeOfRack(String typeOfRack) {
		this.typeOfRack = typeOfRack;
	}

	public String getRackSize() {
		return rackSize;
	}

	public void setRackSize(String rackSize) {
		this.rackSize = rackSize;
	}

	public String getRackName() {
		return rackName;
	}

	public void setRackName(String rackName) {
		this.rackName = rackName;
	}

	public Double getRackWidth() {
		return rackWidth;
	}

	public void setRackWidth(Double rackWidth) {
		this.rackWidth = rackWidth;
	}

	public String getRackHeight() {
		return rackHeight;
	}

	public void setRackHeight(String rackHeight) {
		this.rackHeight = rackHeight;
	}

	public Double getRackDepth() {
		return rackDepth;
	}

	public void setRackDepth(Double rackDepth) {
		this.rackDepth = rackDepth;
	}

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	public String getCableId() {
		return cableId;
	}

	public void setCableId(String cableId) {
		this.cableId = cableId;
	}
}
