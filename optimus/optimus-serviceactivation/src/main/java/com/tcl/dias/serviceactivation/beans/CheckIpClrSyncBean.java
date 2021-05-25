package com.tcl.dias.serviceactivation.beans;
import java.io.Serializable;
/**
 * CheckIpClrSyncBean class
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CheckIpClrSyncBean implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private String requestId;
		private String requestTime;
		private String requestingSystem;
		private String scenarioType;
		private String serviceId;
		public String getRequestId() {
			return requestId;
		}
		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}
		public String getRequestTime() {
			return requestTime;
		}
		public void setRequestTime(String requestTime) {
			this.requestTime = requestTime;
		}
		public String getRequestingSystem() {
			return requestingSystem;
		}
		public void setRequestingSystem(String requestingSystem) {
			this.requestingSystem = requestingSystem;
		}
		public String getScenarioType() {
			return scenarioType;
		}
		public void setScenarioType(String scenarioType) {
			this.scenarioType = scenarioType;
		}
		public String getServiceId() {
			return serviceId;
		}
		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}
}
