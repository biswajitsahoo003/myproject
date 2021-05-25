package com.tcl.dias.serviceactivation.swagger.constants;

/**
 * Swagger Constants related information
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface SwaggerConstants {

	interface ApiOperations {

		String SET_RF_PING_STABILITY = "Set wireless/rf ping stability";
		String GET_PING_DETAILS = "Get ping test details";
		String GET_SS_DUMP_DETAILS = "Get SS-Dump details";

		public static class ServiceActivation {
			private ServiceActivation() {
			}

			public static final String GET_ACTIVATION_DETAIILS = "Used to get Activation details";
			public static final String SAVE_FAILOVER_TESTING_DETAILS = "save failover testing details";
			public static final String IZOSDWAN_SAVE_BILLING_ISSUE_DETAILS = "save billing issue details for izosdwan";
			public static final String IZOSDWAN_SAVE_SERVICE_ISSUE_DETAILS = "save service issue details for izosdwan";
			public static final String SAVE_RAISE_TURNUP_ISSUE_DETAILS = "save raise turnup issue details for izosdwan";
			public static final String UPLOAD_CGW_LOGS = "Uploading Cgw logs in Cgw Config";
			public static final String SAVE_BILLING_ISSUE_DETAILS = "save billing issue details";
			public static final String SAVE_SERVICE_ISSUE_DETAILS = "save service issue details";
			public static final String SAVE_E2E_TESTING_SDWAN = "save Sdwan E2e Testing details";
			public static final String SAVE_SDWAN_PROVISION_DETAILS = "save Sdwan Provision Details";
			public static final String GET_SS_IP_DETAILS = "Get SS IP Details";
		}		
	}

}
