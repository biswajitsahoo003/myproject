package com.tcl.dias.oms.webex.beans;

/**
 * Bean for calculating total prices cumulatively
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class CumulativePricesBean {

	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;
	private Double totalTcv;
	private Double totalMrc;
	private Double totalNrc;
	private Double totalArc;

	public CumulativePricesBean() {
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
        return "CumulativePricesBean{" +
                "mrc=" + mrc +
                ", nrc=" + nrc +
                ", arc=" + arc +
                ", tcv=" + tcv +
                ", totalTcv=" + totalTcv +
                ", totalMrc=" + totalMrc +
                ", totalNrc=" + totalNrc +
                ", totalArc=" + totalArc +
                '}';
    }
}
