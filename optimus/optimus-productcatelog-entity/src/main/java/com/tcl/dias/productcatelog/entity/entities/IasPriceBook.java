package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Entity for view vw_ias_price_book
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Immutable
@Table(name = "vw_ias_price_book")
public class IasPriceBook {

	@Id
	@Column(name = "std_speed")
	private String speed;
	
	@Column(name = "standard")
	private String standard;

	@Column(name = "premium")
	private String premium;

	@Column(name = "extended")
	private String extended;
	
	@Column(name = "compressed")
	private String compressed;
	
	@Column(name = "nrc")
	private String nrc;

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	
	public IasPriceBook() {
		
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getExtended() {
		return extended;
	}

	public void setExtended(String extended) {
		this.extended = extended;
	}

	public String getCompressed() {
		return compressed;
	}

	public void setCompressed(String compressed) {
		this.compressed = compressed;
	}

	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	@Override
	public String toString() {
		return "IasPriceBook [speed=" + speed + ", standard=" + standard + ", premium=" + premium + ", extended="
				+ extended + ", compressed=" + compressed + ", nrc=" + nrc + "]";
	}

	public IasPriceBook(String speed, String standard, String premium, String extended, String compressed, String nrc) {
		super();
		this.speed = speed;
		this.standard = standard;
		this.premium = premium;
		this.extended = extended;
		this.compressed = compressed;
		this.nrc = nrc;
	}

	
}
