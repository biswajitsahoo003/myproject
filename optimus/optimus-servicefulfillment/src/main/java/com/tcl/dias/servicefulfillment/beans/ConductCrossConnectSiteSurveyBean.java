package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

@JsonInclude(Include.NON_NULL)
public class ConductCrossConnectSiteSurveyBean extends TaskDetailsBaseBean {
	
	
	private String demarcationWing;
	private String demarcationFloor;
	private String demarcationRoom;
	private String demarcationBuildingName;
	private String demarcationWingB;
	private String demarcationFloorB;
	private String demarcationRoomB;
	private String demarcationBuildingNameB;
	private String fdf;
	private String cableLength;
	private String rackSize;
	private String rackSizeB;
	private String heightOfServerRoom;
	private String ibdPathAvailable;
	private String ibdLength;
	private String fibrePatchChordLength;
	private String typeOfInternalCabling;
	private String ibdCapexDeviation;
	private String ibdDeviationReason;
	private String revisedIbdCapex;
	private String siteSurveyRemarks;
	
	public String getDemarcationWing() {
		return demarcationWing;
	}
	public void setDemarcationWing(String demarcationWing) {
		this.demarcationWing = demarcationWing;
	}
	public String getDemarcationFloor() {
		return demarcationFloor;
	}
	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}
	public String getDemarcationRoom() {
		return demarcationRoom;
	}
	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}
	public String getDemarcationBuildingName() {
		return demarcationBuildingName;
	}
	public void setDemarcationBuildingName(String demarcationBuildingName) {
		this.demarcationBuildingName = demarcationBuildingName;
	}
	public String getDemarcationWingB() {
		return demarcationWingB;
	}
	public void setDemarcationWingB(String demarcationWingB) {
		this.demarcationWingB = demarcationWingB;
	}
	public String getDemarcationFloorB() {
		return demarcationFloorB;
	}
	public void setDemarcationFloorB(String demarcationFloorB) {
		this.demarcationFloorB = demarcationFloorB;
	}
	public String getDemarcationRoomB() {
		return demarcationRoomB;
	}
	public void setDemarcationRoomB(String demarcationRoomB) {
		this.demarcationRoomB = demarcationRoomB;
	}
	public String getDemarcationBuildingNameB() {
		return demarcationBuildingNameB;
	}
	public void setDemarcationBuildingNameB(String demarcationBuildingNameB) {
		this.demarcationBuildingNameB = demarcationBuildingNameB;
	}
	public String getFdf() {
		return fdf;
	}
	public void setFdf(String fdf) {
		this.fdf = fdf;
	}
	public String getCableLength() {
		return cableLength;
	}
	public void setCableLength(String cableLength) {
		this.cableLength = cableLength;
	}
	public String getRackSize() {
		return rackSize;
	}
	public void setRackSize(String rackSize) {
		this.rackSize = rackSize;
	}
	public String getRackSizeB() {
		return rackSizeB;
	}
	public void setRackSizeB(String rackSizeB) {
		this.rackSizeB = rackSizeB;
	}
	public String getHeightOfServerRoom() {
		return heightOfServerRoom;
	}
	public void setHeightOfServerRoom(String heightOfServerRoom) {
		this.heightOfServerRoom = heightOfServerRoom;
	}
	public String getIbdPathAvailable() {
		return ibdPathAvailable;
	}
	public void setIbdPathAvailable(String ibdPathAvailable) {
		this.ibdPathAvailable = ibdPathAvailable;
	}
	public String getIbdLength() {
		return ibdLength;
	}
	public void setIbdLength(String ibdLength) {
		this.ibdLength = ibdLength;
	}
	public String getFibrePatchChordLength() {
		return fibrePatchChordLength;
	}
	public void setFibrePatchChordLength(String fibrePatchChordLength) {
		this.fibrePatchChordLength = fibrePatchChordLength;
	}
	public String getTypeOfInternalCabling() {
		return typeOfInternalCabling;
	}
	public void setTypeOfInternalCabling(String typeOfInternalCabling) {
		this.typeOfInternalCabling = typeOfInternalCabling;
	}
	public String getIbdCapexDeviation() {
		return ibdCapexDeviation;
	}
	public void setIbdCapexDeviation(String ibdCapexDeviation) {
		this.ibdCapexDeviation = ibdCapexDeviation;
	}
	public String getIbdDeviationReason() {
		return ibdDeviationReason;
	}
	public void setIbdDeviationReason(String ibdDeviationReason) {
		this.ibdDeviationReason = ibdDeviationReason;
	}
	public String getRevisedIbdCapex() {
		return revisedIbdCapex;
	}
	public void setRevisedIbdCapex(String revisedIbdCapex) {
		this.revisedIbdCapex = revisedIbdCapex;
	}
	public String getSiteSurveyRemarks() {
		return siteSurveyRemarks;
	}
	public void setSiteSurveyRemarks(String siteSurveyRemarks) {
		this.siteSurveyRemarks = siteSurveyRemarks;
	}

}
