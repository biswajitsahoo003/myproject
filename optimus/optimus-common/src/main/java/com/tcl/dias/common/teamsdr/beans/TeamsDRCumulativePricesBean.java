package com.tcl.dias.common.teamsdr.beans;

/**
 * Bean for calculating Teams DR prices cumulatively
 *
 */
public class TeamsDRCumulativePricesBean {

	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;
	private Double totalTcv;
	private Double totalMrc;
	private Double totalNrc;
	private Double totalArc;

	public TeamsDRCumulativePricesBean() {
		this.mrc = 0.0;
		this.nrc = 0.0;
		this.arc = 0.0;
		this.tcv = 0.0;
		this.totalMrc = 0.0;
		this.totalNrc = 0.0;
		this.totalArc = 0.0;
		this.totalTcv = 0.0;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}

	public Double getTotalMrc() {
		return totalMrc;
	}

	public void setTotalMrc(Double totalMrc) {
		this.totalMrc = totalMrc;
	}

	public Double getTotalNrc() {
		return totalNrc;
	}

	public void setTotalNrc(Double totalNrc) {
		this.totalNrc = totalNrc;
	}

	public Double getTotalArc() {
		return totalArc;
	}

	public void setTotalArc(Double totalArc) {
		this.totalArc = totalArc;
	}

	@Override
	public String toString() {
		return "CumulativePricesBean{" + "mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + ", totalTcv="
				+ totalTcv + ", totalMrc=" + totalMrc + ", totalNrc=" + totalNrc + ", totalArc=" + totalArc + '}';
	}
}
