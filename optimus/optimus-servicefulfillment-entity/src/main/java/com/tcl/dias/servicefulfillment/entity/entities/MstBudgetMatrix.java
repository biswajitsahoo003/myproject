package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "mst_budget_matrix")
@NamedQuery(name = "MstBudgetMatrix.findAll", query = "SELECT m FROM MstBudgetMatrix m")
public class MstBudgetMatrix implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "cost_center")
	private String costCenter;

	@Column(name = "cpe_type")
	private String cpeType;

	@Column(name = "demand_id")
	private String demandId;

	private String entity;

	@Column(name = "expense_category")
	private String expenseCategory;

	private String gl;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "type_of_expenses")
	private String typeOfExpenses;

	@Column(name = "wbs_level_1")
	private String wbsLevel1;

	@Column(name = "wbs_level_5")
	private String wbsLevel5;

	public MstBudgetMatrix() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCostCenter() {
		return this.costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getCpeType() {
		return this.cpeType;
	}

	public void setCpeType(String cpeType) {
		this.cpeType = cpeType;
	}

	public String getDemandId() {
		return this.demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getExpenseCategory() {
		return this.expenseCategory;
	}

	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	public String getGl() {
		return this.gl;
	}

	public void setGl(String gl) {
		this.gl = gl;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTypeOfExpenses() {
		return this.typeOfExpenses;
	}

	public void setTypeOfExpenses(String typeOfExpenses) {
		this.typeOfExpenses = typeOfExpenses;
	}

	public String getWbsLevel1() {
		return this.wbsLevel1;
	}

	public void setWbsLevel1(String wbsLevel1) {
		this.wbsLevel1 = wbsLevel1;
	}

	public String getWbsLevel5() {
		return this.wbsLevel5;
	}

	public void setWbsLevel5(String wbsLevel5) {
		this.wbsLevel5 = wbsLevel5;
	}

}