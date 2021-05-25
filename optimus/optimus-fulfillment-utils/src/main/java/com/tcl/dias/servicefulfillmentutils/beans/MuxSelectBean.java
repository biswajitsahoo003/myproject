package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * This file contains the MuxSelectBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MuxSelectBean {

	private Integer rightBw;
	private Integer leftBw;
	private String leftOperation;
	private String rightOperation;
	private String lineRate;
	private String muxType;

	public MuxSelectBean() {
		// DO NOTHING
	}

	public MuxSelectBean(Integer leftBw, String leftOperation, String rightOperation, Integer rightBw, String lineRate,
			String muxType) {
		this.rightBw = rightBw;
		this.leftBw = leftBw;
		this.leftOperation = leftOperation;
		this.rightOperation = rightOperation;
		this.lineRate = lineRate;
		this.muxType = muxType;
	}

	public Integer getRightBw() {
		return rightBw;
	}

	public void setRightBw(Integer rightBw) {
		this.rightBw = rightBw;
	}

	public Integer getLeftBw() {
		return leftBw;
	}

	public void setLeftBw(Integer leftBw) {
		this.leftBw = leftBw;
	}

	public String getLeftOperation() {
		return leftOperation;
	}

	public void setLeftOperation(String leftOperation) {
		this.leftOperation = leftOperation;
	}

	public String getRightOperation() {
		return rightOperation;
	}

	public void setRightOperation(String rightOperation) {
		this.rightOperation = rightOperation;
	}

	public String getLineRate() {
		return lineRate;
	}

	public void setLineRate(String lineRate) {
		this.lineRate = lineRate;
	}

	public String getMuxType() {
		return muxType;
	}

	public void setMuxType(String muxType) {
		this.muxType = muxType;
	}

	@Override
	public String toString() {
		return "MuxSelectBean [rightBw=" + rightBw + ", leftBw=" + leftBw + ", leftOperation=" + leftOperation
				+ ", rightOperation=" + rightOperation + ", lineRate=" + lineRate + ", muxType=" + muxType + "]";
	}

}
