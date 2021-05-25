package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

import javax.persistence.Column;

import com.tcl.dias.oms.beans.OrderProductComponentBean;

/**@author mpalanis
 * 
 * Bean class to hold Izosdwan cgw order data
 *
 */
public class OrderIzosdwanCgwDetails {
	
	private String cgwMigUserModifiedBW;
	private String cgwMigSuggestedBW;
	private String migrationHeteroBandwidth;
	private String useCase1;
	private String useCase2;
	private String useCase3;
	private String useCase4;
	private String primaryLocation;
	private String secondaryLocation;
	private String primaryLocationId;
	private String secondaryLocationId;
	private Integer id;
	//newly added
	private String useCase1a;
	private String useCase1aBw;
	private Integer useCase1aRefId;
	private String useCase1b;
	private String useCase1bBw;
	private Integer useCase1bRefId;
	private String useCase2Bw;
	private Integer useCase2RefId;
	private String useCase3Bw;
	private Integer useCase3RefId;
	private String useCase4Bw;
	private Integer useCase4RefId;
	private String cosModel;
	private String primaryState;
	private String secondaryState;
	
	public String getUseCase1a() {
		return useCase1a;
	}
	public void setUseCase1a(String useCase1a) {
		this.useCase1a = useCase1a;
	}
	public String getUseCase1aBw() {
		return useCase1aBw;
	}
	public void setUseCase1aBw(String useCase1aBw) {
		this.useCase1aBw = useCase1aBw;
	}
	public Integer getUseCase1aRefId() {
		return useCase1aRefId;
	}
	public void setUseCase1aRefId(Integer useCase1aRefId) {
		this.useCase1aRefId = useCase1aRefId;
	}
	public String getUseCase1b() {
		return useCase1b;
	}
	public void setUseCase1b(String useCase1b) {
		this.useCase1b = useCase1b;
	}
	public String getUseCase1bBw() {
		return useCase1bBw;
	}
	public void setUseCase1bBw(String useCase1b_bw) {
		this.useCase1bBw = useCase1b_bw;
	}
	public Integer getUseCase1bRefId() {
		return useCase1bRefId;
	}
	public void setUseCase1bRefId(Integer useCase1bRefId) {
		this.useCase1bRefId = useCase1bRefId;
	}
	public String getUseCase2Bw() {
		return useCase2Bw;
	}
	public void setUseCase2Bw(String useCase2Bw) {
		this.useCase2Bw = useCase2Bw;
	}
	public Integer getUseCase2RefId() {
		return useCase2RefId;
	}
	public void setUseCase2RefId(Integer useCase2RefId) {
		this.useCase2RefId = useCase2RefId;
	}
	public String getUseCase3Bw() {
		return useCase3Bw;
	}
	public void setUseCase3Bw(String useCase3Bw) {
		this.useCase3Bw = useCase3Bw;
	}
	public Integer getUseCase3RefId() {
		return useCase3RefId;
	}
	public void setUseCase3RefId(Integer useCase3RefId) {
		this.useCase3RefId = useCase3RefId;
	}
	public String getUseCase4Bw() {
		return useCase4Bw;
	}
	public void setUseCase4Bw(String useCase4Bw) {
		this.useCase4Bw = useCase4Bw;
	}
	public Integer getUseCase4RefId() {
		return useCase4RefId;
	}
	public void setUseCase4RefId(Integer useCase4RefId) {
		this.useCase4RefId = useCase4RefId;
	}
	public String getCosModel() {
		return cosModel;
	}
	public void setCosModel(String cosModel) {
		this.cosModel = cosModel;
	}
	
	public String getPrimaryLocationId() {
		return primaryLocationId;
	}
	public void setPrimaryLocationId(String primaryLocationId) {
		this.primaryLocationId = primaryLocationId;
	}
	public String getSecondaryLocationId() {
		return secondaryLocationId;
	}
	public void setSecondaryLocationId(String secondaryLocationId) {
		this.secondaryLocationId = secondaryLocationId;
	}
	private List<OrderProductComponentBean> components;
	
	public String getCgwMigUserModifiedBW() {
		return cgwMigUserModifiedBW;
	}
	public void setCgwMigUserModifiedBW(String cgwMigUserModifiedBW) {
		this.cgwMigUserModifiedBW = cgwMigUserModifiedBW;
	}
	public String getCgwMigSuggestedBW() {
		return cgwMigSuggestedBW;
	}
	public void setCgwMigSuggestedBW(String cgwMigSuggestedBW) {
		this.cgwMigSuggestedBW = cgwMigSuggestedBW;
	}
	public String getMigrationHeteroBandwidth() {
		return migrationHeteroBandwidth;
	}
	public void setMigrationHeteroBandwidth(String migrationHeteroBandwidth) {
		this.migrationHeteroBandwidth = migrationHeteroBandwidth;
	}
	public String getUseCase1() {
		return useCase1;
	}
	public void setUseCase1(String useCase1) {
		this.useCase1 = useCase1;
	}
	public String getUseCase2() {
		return useCase2;
	}
	public void setUseCase2(String useCase2) {
		this.useCase2 = useCase2;
	}
	public String getUseCase3() {
		return useCase3;
	}
	public void setUseCase3(String useCase3) {
		this.useCase3 = useCase3;
	}
	public String getUseCase4() {
		return useCase4;
	}
	public void setUseCase4(String useCase4) {
		this.useCase4 = useCase4;
	}
	public String getPrimaryLocation() {
		return primaryLocation;
	}
	public void setPrimaryLocation(String primaryLocation) {
		this.primaryLocation = primaryLocation;
	}
	public String getSecondaryLocation() {
		return secondaryLocation;
	}
	public void setSecondaryLocation(String secondaryLocation) {
		this.secondaryLocation = secondaryLocation;
	}
	public List<OrderProductComponentBean> getComponents() {
		return components;
	}
	public void setComponents(List<OrderProductComponentBean> components) {
		this.components = components;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrimaryState() {
		return primaryState;
	}

	public void setPrimaryState(String primaryState) {
		this.primaryState = primaryState;
	}

	public String getSecondaryState() {
		return secondaryState;
	}

	public void setSecondaryState(String secondaryState) {
		this.secondaryState = secondaryState;
	}
}
