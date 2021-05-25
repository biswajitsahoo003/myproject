use service_fulfillment_uat;

CREATE TABLE `sc_teamsdr_service_commercial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sc_service_detail_id` int(11) DEFAULT NULL,
  `component_name` varchar(100) DEFAULT NULL,
  `component_desc` varchar(100) DEFAULT NULL,
  `component_type` varchar(25) DEFAULT NULL,
  `charge_item` varchar(45) DEFAULT NULL,
  `hsn_code` varchar(45) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit_mrc` double DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `unit_nrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `effective_usage` double DEFAULT NULL,
  `effective_overage` double DEFAULT NULL,
  `contract_type` varchar(45) DEFAULT NULL,
  `is_active` char(1) DEFAULT 'Y',
  `created_date` timestamp NULL DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- PROCESS:

-- LD WORKFLOW
INSERT INTO  `mst_process_def` (`key`, `name`, `stage_def_key`, `code`, `sequence`, `customer_view`,`admin_view`) VALUES ('teamsdr_lld_provision_process', 'TeamsDR Provide LLD Provisioning Details', 'order_enrichment_stage', 'LLDDR', '1', 'Y','Y');

INSERT INTO  `mst_process_def` (`key`, `name`, `stage_def_key`, `code`, `sequence`, `customer_view`,`admin_view`)
VALUES ('teamsdr_prepare_project_plan_process', 'TeamsDR Prepare Project Plan', 'order_enrichment_stage', 'DRPPP', '3', 'N','Y');

INSERT INTO  `mst_process_def` (`key`, `name`, `stage_def_key`, `code`, `sequence`, `customer_view`,`admin_view`)
VALUES ('teamsdr_basic_enrichment_process', 'TeamsDR Provide Dmarc Details', 'order_enrichment_stage', 'DRBE', '1', 'Y','Y');


-- TEAMSDR ENDPOINT IMPLEMENTATION INDIA
INSERT INTO  `mst_process_def` (`key`, `name`, `stage_def_key`, `code`, `sequence`, `customer_view`,`admin_view`)
VALUES ('order_teamsdrendpoint_process', 'TeamsDR Endpoint Ordering on Supplier', 'service_implementation_stage', 'TEAMSDRORD', '3', 'Y','Y');


-- TEAMSDR MANAGED SERVICES PROCESS
INSERT INTO  `mst_process_def` (`key`, `name`, `stage_def_key`, `code`, `sequence`, `customer_view`,`admin_view`)
VALUES ('managed_service_advanced_enrichment_process', 'TeamsDR Managed Services Advance Enrichment Process', 'order_enrichment_stage', 'TEAMSDRORD', '3', 'Y','Y');


-- TEAMSDR MANAGED SERVICES USER MAPPING
INSERT INTO  `mst_process_def` (`key`, `name`, `stage_def_key`, `code`, `sequence`, `customer_view`,`admin_view`)
VALUES ('teamsdr_user_mapping_process', 'TeamsDR Managed Services User Mapping Process', 'service_implementation_stage', 'TEAMSDRORD', '3', 'Y','Y');


-- ACTIVITY:

-- LLD WORKFLOW

INSERT INTO  `mst_activity_def` (`key`, `name`, `process_def_key`, `customer_view`, `sequence`,`admin_view`) VALUES ('teamsdr_lld_provision_activity', 'TeamsDR Provide LLD Provisioning Details', 'teamsdr_lld_provision_process', 'Y', '0' , 'Y');

INSERT INTO  `mst_activity_def` (`key`, `name`, `process_def_key`, `customer_view`, `sequence`,`admin_view`)
VALUES ('teamsdr_prepare_project_plan_activity', 'TeamsDR Create Project Plan', 'teamsdr_prepare_project_plan_process', 'N', 0 , 'Y');

INSERT INTO  `mst_activity_def` (`key`, `name`, `process_def_key`, `customer_view`, `sequence`,`admin_view`)
VALUES ('teamsdr_basic_enrichment_activity', 'TeamsDR Provide LCON Details', 'teamsdr_basic_enrichment_process', 'Y', 0 , 'Y');

-- TEAMSDR ENDPOINT IMPLEMENTATION INDIA
INSERT INTO  `mst_activity_def` (`key`, `name`, `process_def_key`, `customer_view`, `sequence`,`admin_view`)
VALUES ('order_teamsdrendpoint_activity', 'TeamsDR Endpoint Ordering on Supplier', 'order_teamsdrendpoint_process', 'Y', 0 , 'Y');


-- TEAMSDR MANAGED SERVICES ADVANCE ENRICHMENT
INSERT INTO  `mst_activity_def` (`key`, `name`, `process_def_key`, `customer_view`, `sequence`,`admin_view`)
VALUES ('managed_services_advanced_enrichment_activity', 'TeamsDR Managed Services Advance Enrichment Activity', 'managed_service_advanced_enrichment_process', 'Y', 0 , 'Y');

-- TEAMSDR MANAGED SERVICES ADVANCE ENRICHMENT
INSERT INTO  `mst_activity_def` (`key`, `name`, `process_def_key`, `customer_view`, `sequence`,`admin_view`)
VALUES ('teamsdr_user_mapping_activity', 'TeamsDR Managed Services User Mapping Activity', 'teamsdr_user_mapping_process', 'Y', 0 , 'Y');

-- TASKS:>>>>>>>>>>>>>>>>>>>
-- LLD WORKFLOW

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-assign-poc-CMIP', 'TeamsDR Provide CMIP POC Details', 'teamsdr_lld_provision_activity', NULL, 480, NULL, 'R10/PT24H', 'CMIP_CAT3_CAT4', 'CMIP_CAT3_CAT4', 'N', 'Y', 'TeamsDR Provide CMIP POC Details', 'TeamsDR Please provide CMIP POC Details', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-assign-poc-PM', 'TeamsDR Provide PM POC Details', 'teamsdr_lld_provision_activity', NULL, 480, NULL, 'R10/PT24H', 'CIM', 'CIM', 'N', 'Y', 'TeamsDR Provide PM POC Details', 'TeamsDR Please provide PM POC Details', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-assign-poc-SATSOC', 'TeamsDR Provide SATSOC POC Details', 'teamsdr_lld_provision_activity', NULL, 480, NULL, 'R10/PT24H', 'SAT_SOC', 'SAT_SOC', 'N', 'Y', 'TeamsDR Provide SATSOC POC Details', 'TeamsDR Please provide SATSOC POC Details', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-assign-poc-SCM-M-L', 'TeamsDR Provide SCMML POC Details', 'teamsdr_lld_provision_activity', NULL, 480, NULL, 'R10/PT24H', 'SCM_M_L', 'SCM_M_L', 'N', 'Y', 'TeamsDR Provide SCMML POC Details', 'TeamsDR Please provide SCMML POC Details', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-assign-poc-SCM-Mgmt', 'TeamsDR Provide SCM Mgmt POC Details', 'teamsdr_lld_provision_activity', NULL, 480, NULL, 'R10/PT24H', 'SCM_Mgmt', 'SCM_Mgmt', 'N', 'Y', 'TeamsDR Provide SCM Mgmt POC Details', 'TeamsDR Please provide SCM Mgmt POC Details', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-assign-poc-SE', 'TeamsDR Provide SE POC Details', 'teamsdr_lld_provision_activity', NULL, 480, NULL, 'R10/PT24H', 'SE', 'SE', 'N', 'Y', 'TeamsDR Provide SE POC Details', 'TeamsDR Please provide SE POC Details', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-assign-poc-TDA', 'TeamsDR Provide TDA POC Details', 'teamsdr_lld_provision_activity', NULL, 480, NULL, 'R10/PT24H', 'TDA', 'TDA', 'N', 'Y', 'TeamsDR Provide TDA POC Details', 'TeamsDR Please provide TDA POC Details', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-internal-call-schedule', 'TeamsDR Schedule Internal Call', 'teamsdr_lld_provision_activity', NULL, 480, NULL, NULL, 'CIM', 'CIM', 'N', 'Y', 'TeamsDR Schedule Internal Call', 'TeamsDR Please schedule internal call', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-notify-internal-call-mom', 'TeamsDR Notify Internal Call MOM', 'teamsdr_lld_provision_activity', NULL, 10, NULL, NULL, 'CIM', 'SYSTEM', 'N', 'N', 'TeamsDR Notify Internal Call MOM', 'TeamsDR Please Notify Internal Call MOM', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'TeamsDR Schedule Kick Off Call', 'teamsdr_lld_provision_activity', NULL, 480, NULL, NULL, 'CIM', 'CIM', 'N', 'Y', 'TeamsDR Schedule Kick Off Call', 'TeamsDR Please schedule kick off call with customer', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-notify-kick-off-call-mom', 'TeamsDR Notify Customer Kick Off Call MOM', 'teamsdr_lld_provision_activity', NULL, 10, NULL, NULL, 'CIM', 'SYSTEM', 'N', 'N', 'TeamsDR Notify Customer Kick Off Call MOM', 'TeamsDR Please Notify Customer Kick Off Call MOM', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-upload-lld-migration-document', 'Upload LLD & Migration Documents', 'teamsdr_lld_provision_activity', NULL, 9600, NULL, NULL, 'TDA', 'TDA', 'N', 'Y', 'TeamsDR Upload LLD & Migration Documents', 'TeamsDR Please Upload LLD & Migration Documents', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'Y', 'poc-TDA');

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-create-project-plan', 'TeamsDR Create Project Plan', 'teamsdr_prepare_project_plan_activity', NULL, 10, NULL, NULL, 'CIM', 'SYSTEM', 'N', 'N', 'TeamsDR Provide Create Project Plan', 'TeamsDR Please Create Project Plan', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-track-update-order-status', 'TeamsDR Track & Update Order Status', 'teamsdr_prepare_project_plan_activity', NULL, 30, NULL, NULL, 'CIM', 'SYSTEM', 'N', 'N', 'TeamsDR Provide Track & Update Order Status', 'TeamsDR Please Track & Update Order Status', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr_basic_enrichment', 'TeamsDR Basic Enrichment', 'teamsdr_basic_enrichment_activity', NULL, 480, NULL, NULL, 'CIM', 'Customer', 'N', 'N', 'TeamsDR Provide LCON Details', 'TeamsDR Please provide LCON Details', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);


-- TEAMSDR ENDPOINT IMPLEMENTATION INDIA
INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-provide-wbsglcc-details-endpoint', 'Provide WBS GL CC Details for TeamsDR Endpoint', 'order_teamsdrendpoint_activity', NULL, 240, NULL, NULL, 'CIM', 'CIM', 'N', 'Y', 'TeamsDR Provide WBS GLCC', 'Please provide WBS, GL, CC Details for Endpoint', 'Complete', 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-pr-endpoint', 'Prepare PR for TeamsDR Endpoint', 'order_teamsdrendpoint_activity', NULL, 960, NULL, NULL, 'SCM_M_L', 'SCM_M_L', 'N', 'Y', 'TeamsDR prepare PR for Endpoint', 'Please Prepare the PR to be provided to the vendor for TeamsDR Endpoint', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-po-endpoint', 'Prepare PO for TeamsDR Endpoint', 'order_teamsdrendpoint_activity', NULL, 480, NULL, NULL, 'SCM_Legal', 'SCM_Legal', 'N', 'Y', 'TeamsDR Prepare PO for Endpoint', 'Please Prepare the PO to be provided to the vendor for TeamsDR Endpoint', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-po-endpoint-release', 'Release PO for TeamsDR Endpoint', 'order_teamsdrendpoint_activity', NULL, 480, NULL, NULL, 'SCM_Mgmt', 'SCM_Mgmt', 'N', 'Y', 'TeamsDR Prepare PO for Endpoint', 'Release PO to be provided to the vendor for TeamsDR Endpoint', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-confirm-material-availability', 'Confirm Endpoint Availability', 'order_teamsdrendpoint_activity', NULL, 240, NULL, NULL, 'SCM_M_L', 'SCM_M_L', 'N', 'Y', 'TeamsDR confirm Material Availability', 'Please provide GRN details against the PO for CPE Model', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-po-endpoint-release-install-support', 'Release PO for TeamsDR Endpoint Installation / Support', 'order_teamsdrendpoint_activity', NULL, 480, NULL, NULL, 'SCM_Mgmt', 'SCM_Mgmt', 'N', 'Y', 'TeamsDR Prepare PO Release for Endpoint Installation / Support', 'Release PO to be provided to the vendor for TeamsDR Endpoint Installation / Support', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-po-endpoint-install-support', 'Prepare PO for TeamsDR Endpoint Installation / Support', 'order_teamsdrendpoint_activity', NULL, 480, NULL, NULL, 'SCM_Legal', 'SCM_Legal', 'N', 'Y', 'TeamsDR Prepare PO for Endpoint Installation / Support', 'Please Prepare the PO to be provided to the vendor for TeamsDR Endpoint Installation / Support', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-pr-endpoint-install-support', 'Prepare PR for TeamsDR Endpoint Installation / Support', 'order_teamsdrendpoint_activity',NULL, 960, NULL, NULL, 'SCM_M_L', 'SCM_M_L', 'N', 'Y', 'TeamsDR Prepare PO for Endpoint Installation / Support', 'Please Prepare the PR to be provided to the vendor for TeamsDR Endpoint Installation / Support', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-dispatch-endpoint', 'Dispatch Endpoint', 'deliver_cpe_activity',NULL, 1680, NULL, NULL, 'SCM-Warehouse', 'SCM-Warehouse', 'N', 'Y', 'TeamsDR Dispatch CPE', 'Please dispatch the Endpoint from Warehouse and update the shipment details', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-track-endpoint-delivery', 'Track Endpoint Delivery', 'deliver_cpe_activity',NULL, 240, NULL, NULL, 'SCM_M_L', 'SCM_M_L', 'N', 'Y', 'TeamsDR Track CPE Delivery', 'Please provide Endpoint Delivery status', NULL, 'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-install-endpoint', 'Install Endpoint', 'cpe_installation_activity',NULL, 480, NULL, NULL, 'CIM', 'CIM', 'N', 'Y', 'TeamsDR Install Endpoint', 'Please install the Endpoint at customer premise', NULL, 'Y', 'N', 'customer-appointment-endpoint-installation', 'Y', 'Y', 'N', NULL);


INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-customer-endpoint-site-readiness', 'Confirm Site Readiness Details', 'site_readiness_activity',NULL, 240, NULL, NULL, 'CUSTOMER', 'customer', 'Y', 'Y', 'Confirm Site Readiness Details', 'Dear Customer, For a timely delivery, we request you to please ready your site based on the specifications. Please review the specifications and confirm site readiness.', "Confirm", 'N', 'N', 'customer-appointment-endpoint-installation', 'Y', 'Y', 'N', NULL);


-- TEAMSDR MANAGED SERVICES ADVANCE ENRICHMENT

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-managed-services-advanced-enrichment', 'To get advance enrichment attributes for Managed services', 'managed_services_advanced_enrichment_activity',NULL, 240, NULL, NULL, 'CUSTOMER', 'customer', 'Y', 'Y', 'Teamsdr Managed Services Advanced Enrichment', 'To get advance enrichment attributes for Managed services', "Confirm",  'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

-- TEAMSDR MANAGED SERVICES User mapping

INSERT INTO `mst_task_def` (`key`, `name`, `activity_def_key`, `form_key`, `tat`, `wait_time`, `reminder_cycle`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `button_label`, `is_dependent_task`, `dynamic_assignment`, `dependent_task_key`, `admin_view`, `customer_view`, `fe_engineer`, `fe_type`) VALUES ('teamsdr-managed-services-advanced-enrichment', 'To get advance enrichment attributes for Managed services', 'managed_services_advanced_enrichment_activity',NULL, 240, NULL, NULL, 'CUSTOMER', 'customer', 'Y', 'Y', 'Teamsdr Managed Services Advanced Enrichment', 'To get advance enrichment attributes for Managed services', "Confirm",  'N', 'N', NULL, 'Y', 'Y', 'N', NULL);

-- MST TASK ATTRIBUTES:
-- LLD WORKFLOW

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'name', 'FIELD_ENGINEER', 'pmName', 'teamsdr-poc-PM');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'name', 'FIELD_ENGINEER', 'primarySeName', 'teamsdr-poc-SE');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'name', 'FIELD_ENGINEER', 'tdaName', 'teamsdr-poc-TDA');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'email', 'FIELD_ENGINEER', 'primarySeEmail', 'teamsdr-poc-SE');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'email', 'FIELD_ENGINEER', 'tdaEmail', 'teamsdr-poc-TDA');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'email', 'FIELD_ENGINEER', 'pmEmail', 'teamsdr-poc-PM');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'customer_contact_email', 'CUSTOMER_CONTRACT_INFO', 'salesEmail', 'teamsdr-poc-SALES');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'customer_contact', 'CUSTOMER_CONTRACT_INFO', 'salesName', 'teamsdr-poc-SALES');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'secondary_name', 'FIELD_ENGINEER', 'secondarySeName', 'teamsdr-poc-SE');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-customer-kick-off-call-schedule', 'secondary_email', 'FIELD_ENGINEER', 'secondarySeEmail', 'teamsdr-poc-SE');

 ---  ############################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'name', 'FIELD_ENGINEER', 'primaryCmipName', 'teamsdr-poc-CMIP');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'name', 'FIELD_ENGINEER', 'pmName', 'teamsdr-poc-PM');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'name', 'FIELD_ENGINEER', 'primarySatSocName', 'teamsdr-poc-SATSOC');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'name', 'FIELD_ENGINEER', 'primaryScmMLName', 'teamsdr-poc-SCM-M-L');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'name', 'FIELD_ENGINEER', 'primaryScmMgmtName', 'teamsdr-poc-SCM-Mgmt');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'name', 'FIELD_ENGINEER', 'primarySeName', 'teamsdr-poc-SE');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'name', 'FIELD_ENGINEER', 'tdaName', 'teamsdr-poc-TDA');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'email', 'FIELD_ENGINEER', 'primaryCmipEmail', 'teamsdr-poc-CMIP');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'email', 'FIELD_ENGINEER', 'pmEmail', 'teamsdr-poc-PM');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'email', 'FIELD_ENGINEER', 'primarySatSocEmail', 'teamsdr-poc-SATSOC');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'email', 'FIELD_ENGINEER', 'primaryScmMLEmail', 'teamsdr-poc-SCM-M-L');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'email', 'FIELD_ENGINEER', 'primaryScmMgmtEmail', 'teamsdr-poc-SCM-Mgmt');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'email', 'FIELD_ENGINEER', 'primarySeEmail', 'teamsdr-poc-SE');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'email', 'FIELD_ENGINEER', 'tdaEmail', 'teamsdr-poc-TDA');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_name', 'FIELD_ENGINEER', 'secondarySeName', 'teamsdr-poc-SE');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_email', 'FIELD_ENGINEER', 'secondarySeEmail', 'teamsdr-poc-SE');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_email', 'FIELD_ENGINEER', 'secondaryCmipEmail', 'teamsdr-poc-CMIP');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_name', 'FIELD_ENGINEER', 'secondaryCmipName', 'teamsdr-poc-CMIP');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_name', 'FIELD_ENGINEER', 'secondarySatSocName', 'teamsdr-poc-SATSOC');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_email', 'FIELD_ENGINEER', 'secondarySatSocEmail', 'teamsdr-poc-SATSOC');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_name', 'FIELD_ENGINEER', 'secondaryScmMLName', 'teamsdr-poc-SCM-M-L');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_email', 'FIELD_ENGINEER', 'secondaryScmMLEmail', 'teamsdr-poc-SCM-M-L');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_name', 'FIELD_ENGINEER', 'secondaryScmMgmtName', 'teamsdr-poc-SCM-Mgmt');

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-internal-call-schedule', 'secondary_email', 'FIELD_ENGINEER', 'secondaryScmMgmtEmail', 'teamsdr-poc-SCM-Mgmt');


-- TEAMSDR ENDPOINT IMPLEMENTATION INDIA

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-provide-wbsglcc-details-endpoint', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);

-- #####################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'licenceGlCode', 'COMPONENT_ATTRIBUTES', 'licenceGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'licenceCostCenter', 'COMPONENT_ATTRIBUTES', 'licenceCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'licenceDemandIdNo', 'COMPONENT_ATTRIBUTES', 'licenceDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);

-- #######################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'teamsdrSupplyHardwarePrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'teamsdrSupplyHardwarePrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'teamsdrSupplyHardwarePrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'teamsdrEndpointPrDate', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'teamsdrEndpointPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'teamsdrEndpointPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'demandIdNo', 'COMPONENT_ATTRIBUTES', 'demandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'licenceDemandIdNo', 'COMPONENT_ATTRIBUTES', 'licenceDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'licenceCostCenter', 'COMPONENT_ATTRIBUTES', 'licenceCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'licenceGlCode', 'COMPONENT_ATTRIBUTES', 'licenceGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');

-- ##################################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrEndpointPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrEndpointPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrEndpointPrDate', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrSupplyHardwarePrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrSupplyHardwarePrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrSupplyHardwarePrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'licenceGlCode', 'COMPONENT_ATTRIBUTES', 'licenceGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'licenceCostCenter', 'COMPONENT_ATTRIBUTES', 'licenceCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'licenceDemandIdNo', 'COMPONENT_ATTRIBUTES', 'licenceDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'demandIdNo', 'COMPONENT_ATTRIBUTES', 'demandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrEndpointPoDate', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrEndpointPoVendorName', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPoVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrEndpointPoRelease', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPoRelease', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'teamsdrEndpointPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeEndpointPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);


-- #################################################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'teamsdrEndpointPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'teamsdrEndpointPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointSupportPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointSupportPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointInstallationPrDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointInstallationPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'teamsdrEndpointPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointInstallationPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointInstallationPoDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointSupportPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'endpointSupportPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'teamsdrEndpointPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'demandIdNo', 'COMPONENT_ATTRIBUTES', 'demandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'licenceDemandIdNo', 'COMPONENT_ATTRIBUTES', 'licenceDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'licenceCostCenter', 'COMPONENT_ATTRIBUTES', 'licenceCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'licenceGlCode', 'COMPONENT_ATTRIBUTES', 'licenceGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-confirm-material-availability', 'teamsdrEndpointPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrVendorName', 'LM');


-- #############################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'level5Wbs', 'COMPONENT_ATTRIBUTES', 'level5Wbs', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'demandIdNo', 'COMPONENT_ATTRIBUTES', 'demandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointInstallationPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointInstallationPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointInstallationPrDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupportPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupportPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupportPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupplyHardwarePrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupplyHardwarePrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupplyHardwarePrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'distributionCenterName', 'COMPONENT_ATTRIBUTES', 'idcThirdPartyLocation', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointInstallationPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointInstallationPoDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupportPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'endpointSupportPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-release-install-support', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);


-- ####################################################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'level5Wbs', 'COMPONENT_ATTRIBUTES', 'level5Wbs', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'demandIdNo', 'COMPONENT_ATTRIBUTES', 'demandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointInstallationPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointInstallationPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointInstallationPrDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointSupportPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointSupportPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointSupportPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointSupplyHardwarePrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointSupplyHardwarePrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'endpointSupplyHardwarePrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'distributionCenterName', 'COMPONENT_ATTRIBUTES', 'idcThirdPartyLocation', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-po-endpoint-install-support', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);

-- #######################################################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'level5Wbs', 'COMPONENT_ATTRIBUTES', 'level5Wbs', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'demandIdNo', 'COMPONENT_ATTRIBUTES', 'demandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointInstallationPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointInstallationPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointInstallationPrDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointSupportPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointSupportPrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointSupportPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointSupplyHardwarePrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointSupplyHardwarePrVendorName', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'endpointSupplyHardwarePrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'distributionCenterName', 'COMPONENT_ATTRIBUTES', 'idcThirdPartyLocation', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-pr-endpoint-install-support', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);

-- ########################################################################
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'supportGlCode', 'COMPONENT_ATTRIBUTES', 'supportGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'supportDemandIdNo', 'COMPONENT_ATTRIBUTES', 'supportDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'supportCostCenter', 'COMPONENT_ATTRIBUTES', 'supportCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'licenceGlCode', 'COMPONENT_ATTRIBUTES', 'licenceGlCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'licenceCostCenter', 'COMPONENT_ATTRIBUTES', 'licenceCostCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'licenceDemandIdNo', 'COMPONENT_ATTRIBUTES', 'licenceDemandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'costCenter', 'COMPONENT_ATTRIBUTES', 'costCenter', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'glCode', 'COMPONENT_ATTRIBUTES', 'glCode', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'demandIdNo', 'COMPONENT_ATTRIBUTES', 'demandIdNo', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'teamsdrendpointPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointSupportPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointSupportPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointInstallationPoDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointInstallationPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'teamsdrEndpointPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointInstallationPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointInstallationPrDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointSupportPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'endpointSupportPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'teamsdrEndpointPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'teamsdrEndpointPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'distributionCenterAddress', 'COMPONENT_ATTRIBUTES', 'distributionCenterAddress', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'distributionCenterName', 'COMPONENT_ATTRIBUTES', 'idcThirdPartyLocation', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'cpeInvoiceNumber', 'COMPONENT_ATTRIBUTES', 'cpeInvoiceNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'billing_gst_number', 'SERVICE', 'customerGstNumber', NULL);
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-dispatch-endpoint', 'tps_sfdc_cuid', 'ORDER', 'cuid', NULL);


-- ###########################################################################
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'teamsdrEndpointPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'teamsdrEndpointPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointSupportPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointSupportPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointInstallationPrDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointInstallationPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'teamsdrEndpointPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointInstallationPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointInstallationPoDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointSupportPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointSupportPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'teamsdrEndpointPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointMrnNumber', 'COMPONENT_ATTRIBUTES', 'endpointMrnNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointMinNumber', 'COMPONENT_ATTRIBUTES', 'endpointMinNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'courierDispatchVendorName', 'COMPONENT_ATTRIBUTES', 'courierDispatchVendorName', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'courierTrackNumber', 'COMPONENT_ATTRIBUTES', 'courierTrackNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'endpointDispatchDate', 'COMPONENT_ATTRIBUTES', 'endpointDispatchDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'distributionCenterName', 'COMPONENT_ATTRIBUTES', 'idcThirdPartyLocation', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'cpeInvoiceNumber', 'COMPONENT_ATTRIBUTES', 'cpeInvoiceNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'billing_gst_number', 'SERVICE', 'customerGstNumber', NULL);
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-track-endpoint-delivery', 'Invoice Method', 'ORDER_ATTRIBUTES', 'billingInvoiceMethod', NULL);


-- #############################################################################

INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'teamsdrEndpointPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'teamsdrEndpointPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointSupportPrDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointSupportPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointInstallationPrDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointInstallationPrNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPrNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'teamsdrEndpointPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointInstallationPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointInstallationPoDate', 'COMPONENT_ATTRIBUTES', 'cpeInstallationPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointSupportPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoNumber', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'endpointSupportPoDate', 'COMPONENT_ATTRIBUTES', 'cpeSupportPoDate', 'LM');
INSERT INTO `mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-install-endpoint', 'teamsdrEndpointPoNumber', 'COMPONENT_ATTRIBUTES', 'cpeSupplyHardwarePoNumber', 'LM');


-- #################################################################################

INSERT INTO `service_fulfillment_uat`.`mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-advanced-enrichment', 'managementInterfaceIp', 'SERVICE_ATTRIBUTES', 'managementInterfaceIp', 'LM');
INSERT INTO `service_fulfillment_uat`.`mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-advanced-enrichment', 'gatewayIp', 'SERVICE_ATTRIBUTES', 'gatewayIp', 'LM');
INSERT INTO `service_fulfillment_uat`.`mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-advanced-enrichment', 'subnetMask', 'SERVICE_ATTRIBUTES', 'subnetMask', 'LM');
INSERT INTO `service_fulfillment_uat`.`mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('teamsdr-advanced-enrichment', 'sbcLocatedBehindFirewall', 'SERVICE_ATTRIBUTES', 'sbcLocatedBehindFirewall', 'LM');



-- ############################################################
-- new scripts after l20-->prod
INSERT INTO `service_fulfillment_uat`.`mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES (null, 'billing_country','CUSTOMER_CONTRACT_INFO', 'spLeCountry', null);


ALTER TABLE  service_fulfillment_uat.task
ADD COLUMN  `sc_component_id` int(11) DEFAULT NULL,
ADD FOREIGN KEY  `fk_sc_component_id_idx`(`sc_component_id`) REFERENCES  `sc_component` (`id`);


INSERT INTO `service_fulfillment_uat`.`mst_task_attributes` (`id`, `mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES (NULL, 'teamsdr-managed-services-service-acceptance', 'HANDOVERNOTE', 'ATTACHMENT', 'HANDOVERNOTE', 'Handover-note');
