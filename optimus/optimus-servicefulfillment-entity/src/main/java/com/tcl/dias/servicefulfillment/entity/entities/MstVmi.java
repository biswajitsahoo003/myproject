package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "mst_vmi")
@NamedQuery(name = "MstVmi.findAll", query = "SELECT m FROM MstVmi m")
public class MstVmi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String description;

	@Column(name = "grand_total")
	private Integer grandTotal;

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "qty_left")
	private Integer qtyLeft;

	@Column(name = "rental_material_code")
	private String rentalMaterialCode;

	@Column(name = "sale_material_code")
	private String saleMaterialCode;

	@Column(name = "sum_of_qty")
	private Integer sumOfQty;

	@Column(name = "so_number")
	private String soNumber;

	@Column(name = "po_landing_date")
	private String poLandingDate;

	@Column(name = "po_number")
	private String poNumber;

	@Column(name = "shipped_date_cisco")
	private String shippedDateCisco;

	@Column(name = "received_date_warehouse")
	private String receivedDateWarehouse;

	@Column(name = "vmi_status_date")
	private String vmiStatusDate;

	// bi-directional many-to-one association to MstVmiTransaction
	@OneToMany(mappedBy = "mstVmi", cascade = CascadeType.ALL)
	private List<MstVmiTransaction> mstVmiTransactions;

	public MstVmi() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getGrandTotal() {
		return this.grandTotal;
	}

	public void setGrandTotal(Integer grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getQtyLeft() {
		return this.qtyLeft;
	}

	public void setQtyLeft(Integer qtyLeft) {
		this.qtyLeft = qtyLeft;
	}

	public String getRentalMaterialCode() {
		return this.rentalMaterialCode;
	}

	public void setRentalMaterialCode(String rentalMaterialCode) {
		this.rentalMaterialCode = rentalMaterialCode;
	}

	public String getSaleMaterialCode() {
		return this.saleMaterialCode;
	}

	public void setSaleMaterialCode(String saleMaterialCode) {
		this.saleMaterialCode = saleMaterialCode;
	}

	public Integer getSumOfQty() {
		return this.sumOfQty;
	}

	public void setSumOfQty(Integer sumOfQty) {
		this.sumOfQty = sumOfQty;
	}

	public List<MstVmiTransaction> getMstVmiTransactions() {
		return this.mstVmiTransactions;
	}

	public void setMstVmiTransactions(List<MstVmiTransaction> mstVmiTransactions) {
		this.mstVmiTransactions = mstVmiTransactions;
	}

	public MstVmiTransaction addMstVmiTransaction(MstVmiTransaction mstVmiTransaction) {
		getMstVmiTransactions().add(mstVmiTransaction);
		mstVmiTransaction.setMstVmi(this);

		return mstVmiTransaction;
	}

	public MstVmiTransaction removeMstVmiTransaction(MstVmiTransaction mstVmiTransaction) {
		getMstVmiTransactions().remove(mstVmiTransaction);
		mstVmiTransaction.setMstVmi(null);

		return mstVmiTransaction;
	}

	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}

	public String getPoLandingDate() {
		return poLandingDate;
	}

	public void setPoLandingDate(String poLandingDate) {
		this.poLandingDate = poLandingDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getShippedDateCisco() {
		return shippedDateCisco;
	}

	public void setShippedDateCisco(String shippedDateCisco) {
		this.shippedDateCisco = shippedDateCisco;
	}

	public String getReceivedDateWarehouse() {
		return receivedDateWarehouse;
	}

	public void setReceivedDateWarehouse(String receivedDateWarehouse) {
		this.receivedDateWarehouse = receivedDateWarehouse;
	}

	public String getVmiStatusDate() {
		return vmiStatusDate;
	}

	public void setVmiStatusDate(String vmiStatusDate) {
		this.vmiStatusDate = vmiStatusDate;
	}

}