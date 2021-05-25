package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

@JsonInclude(Include.NON_NULL)
public class ConductSiteSurveyBean extends TaskDetailsBaseBean {

	private String ospLength;
	private String typeOfCable;
	private String fdf;
	private String cableLength;
	private String typeOfRack;
	private String rackSize;
	private String heightOfServerRoom;
	private String surveyRemarks;
	private String revisedOspCapex;
	private String ospCapex;
	private String ospCapexDeviation;
	private String deviationReason;
	private String ospDistance;
	private String ospPath;
	private String ditPath;

	private String floorDiagramType;
	private String ibdType;
	private String ospType;
	private String ditType;
	private String powerBackup;
	private String siteAddress;
	private String demarcationWing;
	private String demarcationFloor;
	private String demarcationRoom;
	private String demarcationBuildingName;
	private String rowRequired;
	private String prowRequired;
	private String fdName;
	private String ibdName;
	private String permissionRequired;
	private List<AttachmentIdBean> documentIds;

	private Integer sapBundleId;

	private String ibdPathAvailable;
	private String ibdPathRemarks;
	private String ibdPathIssues;
	private String ibdLength;
	private String fibrePatchChordLength;
	private String ibdCapexDeviation;
	private String ibdDeviationReason;
	private String revisedIbdCapex;
	private String typeOfInternalCabling;

	private String endMuxNodeIp;
	private String endMuxNodeName;
	private String endMuxNodePort;

	public String getEndMuxNodeIp() {
		return endMuxNodeIp;
	}

	public void setEndMuxNodeIp(String endMuxNodeIp) {
		this.endMuxNodeIp = endMuxNodeIp;
	}

	public String getEndMuxNodeName() {
		return endMuxNodeName;
	}

	public void setEndMuxNodeName(String endMuxNodeName) {
		this.endMuxNodeName = endMuxNodeName;
	}

	public String getEndMuxNodePort() {
		return endMuxNodePort;
	}

	public void setEndMuxNodePort(String endMuxNodePort) {
		this.endMuxNodePort = endMuxNodePort;
	}

	public Integer getSapBundleId() {
		return sapBundleId;
	}

	public void setSapBundleId(Integer sapBundleId) {
		this.sapBundleId = sapBundleId;
	}

	public String getOspLength() {
		return ospLength;
	}

	public void setOspLength(String ospLength) {
		this.ospLength = ospLength;
	}

	public String getTypeOfCable() {
		return typeOfCable;
	}

	public void setTypeOfCable(String typeOfCable) {
		this.typeOfCable = typeOfCable;
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

	public String getHeightOfServerRoom() {
		return heightOfServerRoom;
	}

	public void setHeightOfServerRoom(String heightOfServerRoom) {
		this.heightOfServerRoom = heightOfServerRoom;
	}

	public String getSurveyRemarks() {
		return surveyRemarks;
	}

	public void setSurveyRemarks(String surveyRemarks) {
		this.surveyRemarks = surveyRemarks;
	}

	public String getOspCapexDeviation() {
		return ospCapexDeviation;
	}

	public void setOspCapexDeviation(String ospCapexDeviation) {
		this.ospCapexDeviation = ospCapexDeviation;
	}

	public String getDeviationReason() {
		return deviationReason;
	}

	public void setDeviationReason(String deviationReason) {
		this.deviationReason = deviationReason;
	}

	public String getOspDistance() {
		return ospDistance;
	}

	public void setOspDistance(String ospDistance) {
		this.ospDistance = ospDistance;
	}

	public String getOspPath() {
		return ospPath;
	}

	public void setOspPath(String ospPath) {
		this.ospPath = ospPath;
	}

	public String getDitPath() {
		return ditPath;
	}

	public void setDitPath(String ditPath) {
		this.ditPath = ditPath;
	}

	public String getFloorDiagramType() {
		return floorDiagramType;
	}

	public void setFloorDiagramType(String floorDiagramType) {
		this.floorDiagramType = floorDiagramType;
	}

	public String getIbdType() {
		return ibdType;
	}

	public void setIbdType(String ibdType) {
		this.ibdType = ibdType;
	}

	public String getOspType() {
		return ospType;
	}

	public void setOspType(String ospType) {
		this.ospType = ospType;
	}

	public String getDitType() {
		return ditType;
	}

	public void setDitType(String ditType) {
		this.ditType = ditType;
	}

	public String getPowerBackup() {
		return powerBackup;
	}

	public void setPowerBackup(String powerBackup) {
		this.powerBackup = powerBackup;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

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

	public String getRowRequired() {
		return rowRequired;
	}

	public void setRowRequired(String rowRequired) {
		this.rowRequired = rowRequired;
	}

	public String getProwRequired() {
		return prowRequired;
	}

	public void setProwRequired(String prowRequired) {
		this.prowRequired = prowRequired;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getIbdName() {
		return ibdName;
	}

	public void setIbdName(String ibdName) {
		this.ibdName = ibdName;
	}

	public String getPermissionRequired() {
		return permissionRequired;
	}

	public void setPermissionRequired(String permissionRequired) {
		this.permissionRequired = permissionRequired;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getOspCapex() {
		return ospCapex;
	}

	public void setOspCapex(String ospCapex) {
		this.ospCapex = ospCapex;
	}

	public String getRevisedOspCapex() {
		return revisedOspCapex;
	}

	public void setRevisedOspCapex(String revisedOspCapex) {
		this.revisedOspCapex = revisedOspCapex;
	}

	public String getIbdPathAvailable() {
		return ibdPathAvailable;
	}

	public void setIbdPathAvailable(String ibdPathAvailable) {
		this.ibdPathAvailable = ibdPathAvailable;
	}

	public String getIbdPathRemarks() {
		return ibdPathRemarks;
	}

	public void setIbdPathRemarks(String ibdPathRemarks) {
		this.ibdPathRemarks = ibdPathRemarks;
	}

	public String getIbdPathIssues() {
		return ibdPathIssues;
	}

	public void setIbdPathIssues(String ibdPathIssues) {
		this.ibdPathIssues = ibdPathIssues;
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

	public String getTypeOfInternalCabling() {
		return typeOfInternalCabling;
	}

	public void setTypeOfInternalCabling(String typeOfInternalCabling) {
		this.typeOfInternalCabling = typeOfInternalCabling;
	}
}
