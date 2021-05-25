package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * 
 * This file contains the OrderIzosdwanCgwDetail.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_izosdwan_cgw_details")
@NamedQuery(name="OrderIzosdwanCgwDetail.findAll", query="SELECT o FROM OrderIzosdwanCgwDetail o")
public class OrderIzosdwanCgwDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name="hetro_bw")
	private String hetroBw;

	@Column(name="migration_system_bw")
	private String migrationSystemBw;

	@Column(name="migration_user_bw")
	private String migrationUserBw;
	
	@ManyToOne
	private Order order;

	@Column(name="primary_location")
	private String primaryLocation;

	@Column(name="secondary_location")
	private String secondaryLocation;

	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_time")
	private Date updatedTime;

	@Column(name="use_case_1")
	private String useCase1;

	@Column(name="use_case_2")
	private String useCase2;

	@Column(name="use_case_3")
	private String useCase3;

	@Column(name="use_case_4")
	private String useCase4;
	
	//newly added
	@Column(name = "use_case_1a")
	private String useCase1a;
	
	@Column(name = "use_case_1a_bw")
	private String useCase1aBw;
	
	@Column(name = "use_case_1a_ref_Id")
	private Integer useCase1aRefId;
	
	@Column(name = "use_case_1b")
	private String useCase1b;
	
	@Column(name = "use_case_1b_bw")
	private String useCase1bBw;
	
	@Column(name = "use_case_1b_ref_Id")
	private Integer useCase1bRefId;
	
	@Column(name = "use_case_2_bw")
	private String useCase2Bw;
	
	@Column(name = "use_case_2_ref_Id")
	private Integer useCase2RefId;
	
	@Column(name = "use_case_3_bw")
	private String useCase3Bw;
	
	@Column(name = "use_case_3_ref_Id")
	private Integer useCase3RefId;
	
	@Column(name = "use_case_4_bw")
	private String useCase4Bw;
	
	@Column(name = "use_case_4_ref_Id")
	private Integer useCase4RefId;
	
	@Column(name = "cos_model")
	private String cosModel;

	public OrderIzosdwanCgwDetail() {

	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getHetroBw() {
		return this.hetroBw;
	}

	public void setHetroBw(String hetroBw) {
		this.hetroBw = hetroBw;
	}

	public String getMigrationSystemBw() {
		return this.migrationSystemBw;
	}

	public void setMigrationSystemBw(String migrationSystemBw) {
		this.migrationSystemBw = migrationSystemBw;
	}

	public String getMigrationUserBw() {
		return this.migrationUserBw;
	}

	public void setMigrationUserBw(String migrationUserBw) {
		this.migrationUserBw = migrationUserBw;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getPrimaryLocation() {
		return this.primaryLocation;
	}

	public void setPrimaryLocation(String primaryLocation) {
		this.primaryLocation = primaryLocation;
	}

	public String getSecondaryLocation() {
		return this.secondaryLocation;
	}

	public void setSecondaryLocation(String secondaryLocation) {
		this.secondaryLocation = secondaryLocation;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUseCase1() {
		return this.useCase1;
	}

	public void setUseCase1(String useCase1) {
		this.useCase1 = useCase1;
	}

	public String getUseCase2() {
		return this.useCase2;
	}

	public void setUseCase2(String useCase2) {
		this.useCase2 = useCase2;
	}

	public String getUseCase3() {
		return this.useCase3;
	}

	public void setUseCase3(String useCase3) {
		this.useCase3 = useCase3;
	}

	public String getUseCase4() {
		return this.useCase4;
	}

	public void setUseCase4(String useCase4) {
		this.useCase4 = useCase4;
	}

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

	public void setUseCase1bBw(String useCase1bBw) {
		this.useCase1bBw = useCase1bBw;
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
	
}