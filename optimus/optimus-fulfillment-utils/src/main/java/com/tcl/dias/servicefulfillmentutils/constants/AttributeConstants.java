package com.tcl.dias.servicefulfillmentutils.constants;

/**
 * This file contains the AttributeConstants.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AttributeConstants {

	public static final String ORDER = "ORDER";
	public static final String ORDER_ATTRIBUTES = "ORDER_ATTRIBUTES";
	public static final String SERVICE = "SERVICE";
	public static final String SERVICE_ATTRIBUTES = "SERVICE_ATTRIBUTES";
	public static final String CUSTOMER_CONTRACT_INFO = "CUSTOMER_CONTRACT_INFO";
	public static final String COMPONENT = "COMPONENT";
	public static final String COMPONENT_ATTRIBUTES = "COMPONENT_ATTRIBUTES";
	public static final String VENDORS = "VENDORS";
	public static final String FIELD_ENGINEER = "FIELD_ENGINEER";
	public static final String APPOINTMENT = "APPOINTMENT";
	public static final String ATTACHMENT = "ATTACHMENT";
	public static final String ATTACHMENTLIST = "ATTACHMENTLIST";
	public static final String TASKDATA = "TASKDATA";

	public static final String COMPONENT_LM = "LM";
	public static final String SITETYPE_A = "A";
	public static final String SITETYPE_B = "B";
	public static final String COMPONENT_CPE = "CPE";
	public static final String COMPONENT_ENRICHMENT = "ENRICHMENT";

	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";

	public static final String OTHER_AVAILABLE_NODES = "OTHER_AVAILABLE_NODES";

	//Webex License will use same common component LM due to current impl
	public static final String COMPONENT_WEBEXLICENSE = COMPONENT_LM;
	public static final String WEBEX_LICENSE="WebEx-License";
	public static final String WEBEX_LICENSE_OFFERNING_NAME="Cisco WebEx CCA";

	public static final String ORDER_ENRICHMENT_STAGE = "order_enrichment_stage";
	public static final String ORDER_EXPERIENCE_SURVEY_STAGE = "experience_survey_stage";
	
	public static final String COMPONENT_GSCLICENSE = COMPONENT_LM;
	
	public static final String IS_WORKING_TEMP_TERMINATION_NO = "isWorkingTemporaryTerminationNumber";

	// For teamsdr
	public static final String ENDPOINT_MRN_NO = "endpointMrnNumber";
	public static final String ENDPOINT_MIN_NO = "endpointMinNumber";
	public static final String COURIER_DISPATCH_VENDOR_NAME = "courierDispatchVendorName";
	public static final String COURIER_TRACK_NUMBER = "courierTrackNumber";
	public static final String ENDPOINT_DISPATCH_DATE = "endpointDispatchDate";
	public static final String DISTRIBUTION_CENTER_NAME = "distributionCenterName";
	public static final String DISTRIBUTION_CENTER_ADDRESS = "distributionCenterAddress";
	public static final String ENDPOINT_SERIAL_NUMBER = "endpointSerialNumber";
	public static final String ENDPOINT_DELIVERY_DATE = "endpointDeliveryDate";
	public static final String ENDPOINT_END_OF_SALE = "endpointEndOfSale";
	public static final String ENDPOINT_END_OF_LIFE = "endpointEndOfLife";
	public static final String CLOSED = "CLOSED";
	public static final String DELIVERY_STATUS = "deliveryStatus";
	public static final String ENDPOINT_CARD_SERIAL_NUMBER = "endpointCardSerialNumber";
	public static final String DATE_OF_ENDPOINT_INSTALLATION = "dateOfEndpointInstallation";
	public static final String ENDPOINT_AMC_START_DATE = "endpointAmcStartDate";
	public static final String ENDPOINT_AMC_END_DATE = "endpointAmcEndDate";
	public static final String ENDPOINT_CONSOLE_CABLE_CONNECTED = "endpointConsoleCableConnected";
	public static final String MANAGEMENT_INTERFACE_IP = "managementInterfaceIp";
	public static final String GATEWAY_IP = "gatewayIp";
	public static final String SUBNET_MASK = "subnetMask";
	public static final String SBC_LOCATED_BEHIND_FIREWALL = "sbcLocatedBehindFirewall";
	public static final String SRN_DATE = "srnDate";
	public static final String CUSTOMER_HANDOVER_COMPLETED = "customerHandoverCompleted";
	public static final String CUSTOMER_HANDOVER_COMPLETED_DATE = "customerHandoverCompletedDate";
	public static final String INSTALLATION_COMPLETED = "installationCompleted";
	public static final String INSTALLATION_DATE = "installationDate";
	public static final String GATEWAY_REMOTELY_ACCESSIBLE = "mgRemotelyAccessible";
	public static final String GSMC_TICKET = "gsmcTicket";
	public static final String FIREWALL_OPENED = "firewallOpened";
	public static final String PORT_OPENED = "portOpened";
	public static final String UAT_COMPLETED = "uatCompleted";
	public static final String UAT_DATE = "uatDate";
	public static final String REMOTE_ACCESS_AFTER_HARDENING = "remoteAccessAfterHardening";
	public static final String VERIFY_SBC_IS_ONBOARDED_ON_EMS = "verifySbcOnboarded";
	public static final String VERIFY_SNMP_TRAPS_POLLING = "verfiySnmsTrapsAndPolling";
	public static final String CONFIGURATION_COMPLETED = "configurationCompleted";
	public static final String CONFIGURATION_DATE = "configurationDate";
	public static final String GENERATE_CSR = "generateCSR";
	public static final String DNS_FOR_FQDN = "dnsForFQDN";
	public static final String CPE_BOM_RESPONSE = "cpeBomResponse";
	public static final String SECURITY_CERTIFICATE = "securityCertificateName";
	public static final String DR_SITE = "drSite";
	public static final String SITE_NAME = "siteName";
	public static final String TATA_SCOPE_OF_WORK = "tataScopeOfWork";
	public static final String NO_OF_USERS_ON_SITE = "noOfUsersOnSite";
	public static final String SITE_TESTING_PROVISION_SPOC = "siteTestingProvisioningSpoc";
	public static final String SPOC_CONTACT = "spocContactNumber";
	public static final String PRIMARY_TEST_USER_DOMAIN_ACTIVATION = "primaryTestUserDomainActivation";
	public static final String TEST_USER_1_CREDENTIALS = "testUser1Credentials";
	public static final String SECONDARY_TEST_USER_DOMAIN_ACTIVATION = "secondaryTestUserDomainActivation";
	public static final String TEST_USER_2_CREDENTIALS = "testUser2Credentials";
	public static final String DELEGATE_ADMIN_ACCESS = "delegateAdminAccess";
	public static final String TENANT_LOGIN_NAME = "tenantLoginName";
	public static final String TENANT_LOGIN_PASSWORD = "tenantLoginPassWord";
	public static final String COMMON_TRAINING_DATA = "commonTrainingData";
	public static final String IS_END_USER_TRAINING = "isEndUserTraining";
	public static final String IS_ADVANCED_LEVEL_TRAINING = "isAdvancedLevelTraining";
	public static final String USER_TRAINING_DATA = "userTrainingData";
	public static final String ADVANCED_TRAINING_DATA = "advancedTrainingData";
	public static final String MANAGEMENT_AND_MONITORING_DATA = "managementAndMonitoringData";
	public static final String MS_UAT_TESTING_STATUS="msUatTestingStatus";
	public static final String MS_UAT_COMPLETION_DATE="msUatCompletionDate";
	public static final String MS_UAT_ATT_ID="msUatAttId";
	public static final String COUNTRY_NAME = "countryName";
	public static final String SC_COMPONENT_ID = "scComponentId";
	public static final String DID_RANGE_ALLOCATED = "didRangeAllocated";
	public static final String BATCH_DATE = "batchDate";
	public static final String BATCH_TIME = "batchTime";
	public static final String BATCH_USER_COUNT = "batchUserCount";
	public static final String BATCH_COUNT = "batchCount";
	public static final String REMAINING_USER_COUNT = "remainingUserCount";
	public static final String MS_USER_MAPPING_ATTACHMENT_ID = "msUmAttId";
	public static final String MS_DR_CONFIG_STATUS="msDrConfigStatus";
	public static final String MS_DR_CONFIG_DATE="msDrConfigDate";
	public static final String MS_DR_ATT_ID = "drAttachmentIds";
	public static final String SITE_TYPE = "site_type";
	public static final String TOTAL_USERS = "total_users";
	public static final String PENDING_USER_COUNT = "pendingCount";
	public static final String END_USER_TRAINING = "endUserTraining";
	public static final String ADVANCED_LEVEL_TRAINING = "advancedLevelTraining";
	public static final String IS_TRAINING_OPEN = "isTrainingOpen";
	public static final String IS_TRAINING_PRESENT = "isTrainingPresent";
	public static final String TENANT_CONFIG_COMPLETED = "tenantConfigurationCompleted";
	public static final String TENANT_ALERT_COMPLETED = "tenantAlertConfigCompleted";
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String IS_PENDING_USER_AVAILABLE = "isPendingUserAvailable";
	public static final String KEY_CASE_INST_ID = "caseInstId";
	public static final String IS_MANAGEMENT_MONITORING_PRESENT = "isManagementMonitoringPresent";
	public static final String IS_MANAGEMENT_MONITORING_OPEN = "isManagementMonitoringOpen";
	public static final String IS_MS_EXP_SURVEY_OPEN ="isMSExperienceSurveyOpen";
}
