package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity Class
 *
 * @author Manojkumar R
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "bom_rental_sales_code_assoc")
@NamedQuery(name = "BomRentalSalesCodeAssoc.findAll", query = "SELECT b FROM BomRentalSalesCodeAssoc b")
public class BomRentalSalesCodeAssoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bom_id")
	private BomMaster bomMaster;

	@Column(name = "bom_name")
	private String bomName;

	@Column(name = "rental_Code")
	public String rentalCode;

	@Column(name = "sales_Code")
	public String salesCode;

	public BomMaster getBomMaster() {
		return bomMaster;
	}

	public void setBomMaster(BomMaster bomMaster) {
		this.bomMaster = bomMaster;
	}

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getRentalCode() {
		return rentalCode;
	}

	public void setRentalCode(String rentalCode) {
		this.rentalCode = rentalCode;
	}

	public String getSalesCode() {
		return salesCode;
	}

	public void setSalesCode(String salesCode) {
		this.salesCode = salesCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}