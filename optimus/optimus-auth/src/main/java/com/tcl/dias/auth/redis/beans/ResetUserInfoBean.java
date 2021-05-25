package com.tcl.dias.auth.redis.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * ResetUserInfoBean.java class- Represents User Information for resetting the
 * 
 * password
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ResetUserInfoBean implements Serializable {
	private static final long serialVersionUID = -7470716023430482017L;
	private String userId;
	private String resetToken;
	private Timestamp createdTime;

	/***
	 * used to getUserId for RestUser information
	 * 
	 * @return String
	 */
	public String getUserId() {
		return userId;
	}

	/***
	 * used to setUserId to current object for RestUser information
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/***
	 * used to getResetToken for RestUser information
	 * 
	 * @return String
	 */
	public String getResetToken() {
		return resetToken;
	}

	/***
	 * used to setResetToken to current object for RestUser information
	 * 
	 * @param resetToken
	 */

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	/***
	 * used to getCreatedTime for RestUser information
	 * 
	 * @return Timestamp
	 */
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	/***
	 * used to setCreatedTime to current object for RestUser information
	 * 
	 * @param createdTime
	 */
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "ResetPasswordBean [userId=" + userId + ", resetToken=" + resetToken + ", createdTime=" + createdTime
				+ "]";
	}

}
