package com.tcl.dias.serviceactivation.beans;
import java.io.Serializable;
/**
 * CheckClrInfoSyncBean class
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CheckClrInfoSyncBean implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private String objectType;
		private String objectName;
		private String relationshipType;
		private String requestId;
		private String requestingSystem;
		public String getObjectType() {
			return objectType;
		}
		public void setObjectType(String objectType) {
			this.objectType = objectType;
		}
		public String getObjectName() {
			return objectName;
		}
		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}
		public String getRelationshipType() {
			return relationshipType;
		}
		public void setRelationshipType(String relationshipType) {
			this.relationshipType = relationshipType;
		}
		public String getRequestId() {
			return requestId;
		}
		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}
		public String getRequestingSystem() {
			return requestingSystem;
		}
		public void setRequestingSystem(String requestingSystem) {
			this.requestingSystem = requestingSystem;
		}
		 
}
