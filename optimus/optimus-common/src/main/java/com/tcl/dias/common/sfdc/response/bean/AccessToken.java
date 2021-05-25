
package com.tcl.dias.common.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * AccessToken.class is used for token  realted sfdc
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited 
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessToken {

	private String accessToken;
	private String instanceUrl;
	private String id;
	private String tokenType;
	private String issuedAt;
	private String signature;

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken
	 *            the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the instanceUrl
	 */
	public String getInstanceUrl() {
		return instanceUrl;
	}

	/**
	 * @param instanceUrl
	 *            the instanceUrl to set
	 */
	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * @param tokenType
	 *            the tokenType to set
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * @return the issuedAt
	 */
	public String getIssuedAt() {
		return issuedAt;
	}

	/**
	 * @param issuedAt
	 *            the issuedAt to set
	 */
	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
