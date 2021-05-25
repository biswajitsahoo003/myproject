-- Need to be executed in service inventory

CREATE OR REPLACE
    ALGORITHM = UNDEFINED 
    DEFINER = `optimus`@`%` 
    SQL SECURITY DEFINER
VIEW `vw_si_service_info` AS
    SELECT 
        `srvDetail`.`id` AS `sys_id`,
        `srvDetail`.`tps_service_id` AS `srv_service_id`,
        `srvDetail`.`site_alias` AS `srv_site_alias`,
        `srvDetail`.`erf_prd_catalog_product_id` AS `srv_product_family_id`,
        `srvDetail`.`erf_prd_catalog_offering_id` AS `srv_product_offering_id`,
        `srvDetail`.`erf_prd_catalog_product_name` AS `srv_product_family_name`,
        `srvDetail`.`erf_prd_catalog_offering_name` AS `srv_product_offering_name`,
        `srvDetail`.`primary_tps_service_id` AS `srv_primary_service_id`,
        `srvDetail`.`service_sequence` AS `srv_service_record_sequence`,
        `srvDetail`.`primary_secondary` AS `srv_pri_sec`,
        `srvDetail`.`pri_sec_service_link` AS `srv_pri_sec_link`,
        `srvDetail`.`gsc_order_sequence_id` AS `srv_gsc_order_sequence_id`,
        `srvDetail`.`lat_long` AS `srv_lat_long`,
        `srvDetail`.`service_classification` AS `srv_service_classification`,
        `srvDetail`.`service_option` AS `srv_service_management_option`,
        `srvDetail`.`site_topology` AS `srv_gvpn_site_topology`,
        `srvDetail`.`service_topology` AS `srv_vpn_topology`,
        `srvDetail`.`vpn_name` AS `srv_vpn_name`,
        `srvDetail`.`service_class` AS `srv_gvpn_cos`,
        `srvDetail`.`site_address` AS `srv_customer_site_address`,
        `srvDetail`.`access_type` AS `srv_access_type`,
        `srvDetail`.`bw_portspeed` AS `srv_bandwidth`,
        `srvDetail`.`bw_unit` AS `srv_bandwidth_unit`,
        `srvDetail`.`bw_portspeed_alt_name` AS `srv_bandwidth_disp_name`,
        `srvDetail`.`lastmile_bw` AS `srv_lastmile_bandwidth`,
        `srvDetail`.`lastmile_bw_unit` AS `srv_lastmile_bandwidth_unit`,
        `srvDetail`.`lastmile_bw_alt_name` AS `srv_lastmile_bandwidth_disp_name`,
        `srvDetail`.`pop_site_address` AS `srv_pop_address`,
        `srvDetail`.`pop_site_code` AS `srv_pop_code`,
        `srvDetail`.`source_country` AS `srv_source_country`,
        `srvDetail`.`service_status` AS `srv_service_status`,
        `srvDetail`.`service_commissioned_date` AS `srv_commissioned_date`,
        `srvDetail`.`service_termination_date` AS `srv_termination_date`,
        `srvDetail`.`lastmile_provider` AS `srv_lastmile_provider`,
        `srvDetail`.`lastmile_type` AS `srv_last_mile_type`,
        `srvDetail`.`burstable_bw_portspeed` AS `srv_burstable_bw`,
        `srvDetail`.`burstable_bw_unit` AS `srv_vurstable_bw_unit`,
        `srvDetail`.`burstable_bw_portspeed_alt_name` AS `srv_burstable_bw_disp_name`,
        `srvDetail`.`destination_country` AS `srv_destination_country`,
        `srvDetail`.`destination_country_code` AS `srv_destination_country_code`,
        `srvDetail`.`destination_country_code_repc` AS `srv_destination_country_code_repc`,
        `srvDetail`.`destination_city` AS `srv_destination_city`,
        `srvDetail`.`source_country_code` AS `srv_source_country_code`,
        `srvDetail`.`source_country_code_repc` AS `srv_source_country_code_repc`,
        `srvDetail`.`source_city` AS `srv_source_city`,
        `srvDetail`.`site_type` AS `srv_site_type`,
        `srvDetail`.`site_end_interface` AS `srv_site_end_interface`,
        `srvDetail`.`demarcation_floor` AS `demarcation_floor`,
        `srvDetail`.`denarcation_room` AS `denarcation_room`,
        `srvDetail`.`demarcation_apartment` AS `demarcation_apartment`,
        `ordDetail`.`id` AS `order_sys_id`,
        `ordDetail`.`op_order_code` AS `order_code`,
        `ordDetail`.`opportunity_classification` AS `opportunity_type`,
        `ordDetail`.`demo_flag` AS `order_demo_flag`,
        `ordDetail`.`erf_cust_customer_id` AS `order_customer_id`,
        `ordDetail`.`erf_cust_customer_name` AS `order_customer`,
        `ordDetail`.`erf_cust_le_id` AS `order_cust_le_id`,
        `ordDetail`.`erf_cust_le_name` AS `order_cust_le_name`,
        `ordDetail`.`erf_cust_sp_le_id` AS `order_sp_le_id`,
        `ordDetail`.`erf_cust_sp_le_name` AS `order_sp_le_name`,
        `ordDetail`.`erf_cust_partner_id` AS `order_partner`,
        `ordDetail`.`erf_cust_partner_name` AS `order_partner_name`,
        `ordDetail`.`erf_user_initiator_id` AS `order_initiator`,
        `ordDetail`.`created_by` AS `order_created_by`,
        `ordDetail`.`created_date` AS `order_created_date`,
        `ordDetail`.`is_active` AS `order_is_active`,
        `ordDetail`.`is_multiple_le` AS `order_multiple_le_flag`,
        `ordDetail`.`is_bundle_order` AS `order_bundle_order_flag`,
        `contractInfo`.`tps_sfdc_cuid` AS `sfdc_cuid`,
        `contractInfo`.`account_manager` AS `account_manager`,
        `contractInfo`.`account_manager_email` AS `account_manager_email`,
        `contractInfo`.`customer_contact` AS `customer_contact`,
        `contractInfo`.`customer_contact_email` AS `customer_contact_email`,
        `contractInfo`.`order_term_in_months` AS `order_term_in_months`,
        `contractInfo`.`billing_frequency` AS `billing_frequency`,
        `contractInfo`.`contract_start_date` AS `contract_start_date`,
        `contractInfo`.`contract_end_date` AS `contract_end_date`,
        `contractInfo`.`last_macd_date` AS `last_macd_date`,
        `contractInfo`.`mrc` AS `mrc`,
        `contractInfo`.`nrc` AS `nrc`,
        `contractInfo`.`arc` AS `arc`,
        `contractInfo`.`discount_mrc` AS `discount_mrc`,
        `contractInfo`.`discount_nrc` AS `discount_nrc`,
        `contractInfo`.`discount_arc` AS `discount_arc`,
        `contractInfo`.`erf_cust_currency_id` AS `currency_id`,
        `contractInfo`.`billing_address` AS `billing_address`,
        `contractInfo`.`billing_method` AS `billing_method`
    FROM
        ((`si_service_detail` `srvDetail`
        JOIN `si_order` `ordDetail` ON (`srvDetail`.`si_order_id` = `ordDetail`.`id`))
        JOIN `si_contract_info` `contractInfo` ON (`contractInfo`.`SI_order_id` = `ordDetail`.`id`))


ALTER TABLE `si_service_contacts` 
CHANGE COLUMN `contact_type` `contact_type` ENUM('Customer','Vendor','Internal', 'LocalITContact') NULL DEFAULT NULL ;

ALTER TABLE si_service_detail
                ADD COLUMN `primary_tps_service_id` VARCHAR(45) NULL DEFAULT NULL AFTER `tps_service_id`,
                ADD COLUMN `service_sequence` SMALLINT NULL DEFAULT NULL AFTER `primary_tps_service_id`;

                
CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `optimus`@`%` 
    SQL SECURITY DEFINER
VIEW `vw_service_asset_info` AS
    SELECT 
        `srvAsset`.`id` AS `asset_sys_id`,
        `srvAssetMap`.`SI_service_detail_id` AS `srv_sys_id`,
        `srvAssetMap`.`SI_service_detail_tps_service_id` AS `service_id`,
        `srvAsset`.`name` AS `asset_name`,
        `srvAsset`.`type` AS `asset_type`,
        `srvAsset`.`fqdn` AS `fqdn`,
        `srvAsset`.`management_ip` AS `management_ip`,
        `srvAsset`.`public_ip` AS `public_ip`,
        `srvAsset`.`gateway_ip` AS `gateway_ip`,
        `srvAsset`.`parent_id` AS `parent_id`,
        `srvAsset`.`description` AS `description`,
        `srvAsset`.`model` AS `model`,
        `srvAsset`.`mac_id` AS `mac_id`,
        `srvAsset`.`monitoring_tool` AS `monitoring_tool`,
        `srvAsset`.`managed_by` AS `managed_by`,
        `srvAsset`.`serial_no` AS `serial_no`,
        `srvAsset`.`owner` AS `owner`,
        `srvAsset`.`circuit_id` AS `circuit_id`,
        `srvAssetMap`.`is_active` AS `is_active`,
        `srvAssetMap`.`start_date` AS `asset_to_srv_support_start_date`,
        `srvAssetMap`.`end_date` AS `asset_to_srv_support_end_date`,
        `srvAssetMap`.`circuit_status` AS `circuit_status`,
        `srvAsset`.`origin_ntwrk` AS `origin_ntwrk`
        ,srvAsset.scope_of_management
        ,srvAsset.support_type
        ,srvAsset.is_shared_ind
    FROM
        (`si_asset` `srvAsset`
        JOIN `si_asset_to_service` `srvAssetMap` ON (`srvAssetMap`.`SI_Asset_ID` = `srvAsset`.`id`));
        
        
        CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `optimus`@`%` 
    SQL SECURITY DEFINER
VIEW `vw_si_service_info` AS
    SELECT 
        `srvDetail`.`id` AS `sys_id`,
        `srvDetail`.`tps_service_id` AS `srv_service_id`,
        `srvDetail`.`site_alias` AS `srv_site_alias`,
        `srvDetail`.`erf_prd_catalog_product_id` AS `srv_product_family_id`,
        `srvDetail`.`erf_prd_catalog_offering_id` AS `srv_product_offering_id`,
        `srvDetail`.`erf_prd_catalog_product_name` AS `srv_product_family_name`,
        `srvDetail`.`erf_prd_catalog_offering_name` AS `srv_product_offering_name`,
        `srvDetail`.`primary_tps_service_id` AS `srv_primary_service_id`,
        `srvDetail`.`service_sequence` AS `srv_service_record_sequence`,
        `srvDetail`.`primary_secondary` AS `srv_pri_sec`,
        `srvDetail`.`pri_sec_service_link` AS `srv_pri_sec_link`,
        `srvDetail`.`gsc_order_sequence_id` AS `srv_gsc_order_sequence_id`,
        `srvDetail`.`lat_long` AS `srv_lat_long`,
        `srvDetail`.`service_classification` AS `srv_service_classification`,
        `srvDetail`.`service_option` AS `srv_service_management_option`,
        `srvDetail`.`site_topology` AS `srv_gvpn_site_topology`,
        `srvDetail`.`service_topology` AS `srv_vpn_topology`,
        `srvDetail`.`vpn_name` AS `srv_vpn_name`,
        `srvDetail`.`service_class` AS `srv_gvpn_cos`,
        `srvDetail`.`site_address` AS `srv_customer_site_address`,
        `srvDetail`.`access_type` AS `srv_access_type`,
        `srvDetail`.`bw_portspeed` AS `srv_bandwidth`,
        `srvDetail`.`bw_unit` AS `srv_bandwidth_unit`,
        `srvDetail`.`bw_portspeed_alt_name` AS `srv_bandwidth_disp_name`,
        `srvDetail`.`lastmile_bw` AS `srv_lastmile_bandwidth`,
        `srvDetail`.`lastmile_bw_unit` AS `srv_lastmile_bandwidth_unit`,
        `srvDetail`.`lastmile_bw_alt_name` AS `srv_lastmile_bandwidth_disp_name`,
        `srvDetail`.`pop_site_address` AS `srv_pop_address`,
        `srvDetail`.`pop_site_code` AS `srv_pop_code`,
        `srvDetail`.`source_country` AS `srv_source_country`,
        `srvDetail`.`service_status` AS `srv_service_status`,
        `srvDetail`.`service_commissioned_date` AS `srv_commissioned_date`,
        `srvDetail`.`service_termination_date` AS `srv_termination_date`,
        `srvDetail`.`lastmile_provider` AS `srv_lastmile_provider`,
        `srvDetail`.`lastmile_type` AS `srv_last_mile_type`,
        `srvDetail`.`burstable_bw_portspeed` AS `srv_burstable_bw`,
        `srvDetail`.`burstable_bw_unit` AS `srv_vurstable_bw_unit`,
        `srvDetail`.`burstable_bw_portspeed_alt_name` AS `srv_burstable_bw_disp_name`,
        `srvDetail`.`destination_country` AS `srv_destination_country`,
        `srvDetail`.`destination_country_code` AS `srv_destination_country_code`,
        `srvDetail`.`destination_country_code_repc` AS `srv_destination_country_code_repc`,
        `srvDetail`.`destination_city` AS `srv_destination_city`,
        `srvDetail`.`source_country_code` AS `srv_source_country_code`,
        `srvDetail`.`source_country_code_repc` AS `srv_source_country_code_repc`,
        `srvDetail`.`source_city` AS `srv_source_city`,
        `srvDetail`.`site_type` AS `srv_site_type`,
        `srvDetail`.`site_end_interface` AS `srv_site_end_interface`,
        `srvDetail`.`demarcation_floor` AS `demarcation_floor`,
        `srvDetail`.`denarcation_room` AS `denarcation_room`,
        `srvDetail`.`demarcation_apartment` AS `demarcation_apartment`,
        `ordDetail`.`id` AS `order_sys_id`,
        `ordDetail`.`op_order_code` AS `order_code`,
        `ordDetail`.`opportunity_classification` AS `opportunity_type`,
        `ordDetail`.`demo_flag` AS `order_demo_flag`,
        `ordDetail`.`erf_cust_customer_id` AS `order_customer_id`,
        `ordDetail`.`erf_cust_customer_name` AS `order_customer`,
        `ordDetail`.`erf_cust_le_id` AS `order_cust_le_id`,
        `ordDetail`.`erf_cust_le_name` AS `order_cust_le_name`,
        `ordDetail`.`erf_cust_sp_le_id` AS `order_sp_le_id`,
        `ordDetail`.`erf_cust_sp_le_name` AS `order_sp_le_name`,
        `ordDetail`.`erf_cust_partner_id` AS `order_partner`,
        `ordDetail`.`erf_cust_partner_name` AS `order_partner_name`,
        `ordDetail`.`erf_user_initiator_id` AS `order_initiator`,
        `ordDetail`.`created_by` AS `order_created_by`,
        `ordDetail`.`created_date` AS `order_created_date`,
        `ordDetail`.`is_active` AS `order_is_active`,
        `ordDetail`.`is_multiple_le` AS `order_multiple_le_flag`,
        `ordDetail`.`is_bundle_order` AS `order_bundle_order_flag`,
        `contractInfo`.`tps_sfdc_cuid` AS `sfdc_cuid`,
        `contractInfo`.`account_manager` AS `account_manager`,
        `contractInfo`.`account_manager_email` AS `account_manager_email`,
        `contractInfo`.`customer_contact` AS `customer_contact`,
        `contractInfo`.`customer_contact_email` AS `customer_contact_email`,
        `contractInfo`.`order_term_in_months` AS `order_term_in_months`,
        `contractInfo`.`billing_frequency` AS `billing_frequency`,
        `contractInfo`.`contract_start_date` AS `contract_start_date`,
        `contractInfo`.`contract_end_date` AS `contract_end_date`,
        `contractInfo`.`last_macd_date` AS `last_macd_date`,
        `contractInfo`.`mrc` AS `mrc`,
        `contractInfo`.`nrc` AS `nrc`,
        `contractInfo`.`arc` AS `arc`,
        `contractInfo`.`discount_mrc` AS `discount_mrc`,
        `contractInfo`.`discount_nrc` AS `discount_nrc`,
        `contractInfo`.`discount_arc` AS `discount_arc`,
        `contractInfo`.`erf_cust_currency_id` AS `currency_id`,
        `contractInfo`.`billing_address` AS `billing_address`,
        `contractInfo`.`billing_method` AS `billing_method`,
        `srvDetail`.`customer_service_id` AS `customer_service_id`,
        `srvDetail`.`parent_bundle_service_id` AS `parent_bundle_service_id`,
        `srvDetail`.`parent_bundle_service_name` AS `parent_bundle_service_name`
        ,srvDetail.usage_model
        ,srvDetail.routing_protocol
        ,srvDetail.ip_address_arrangement_type
        ,srvDetail.ipv4_address_pool_size
        ,srvDetail.ipv6_address_pool_size
        ,srvDetail.backup_config_mode
    FROM
        ((`si_service_detail` `srvDetail`
        JOIN `si_order` `ordDetail` ON (`srvDetail`.`si_order_id` = `ordDetail`.`id`))
        JOIN `si_contract_info` `contractInfo` ON (`contractInfo`.`SI_order_id` = `ordDetail`.`id`));
        
        
        
ALTER TABLE `service_inventory_uat_v4`.`si_asset` 
ADD COLUMN `wan_ip_address` VARCHAR(45) NULL AFTER `is_shared_ind`,
ADD COLUMN `wan_ip_provider` VARCHAR(45) NULL AFTER `wan_ip_address`;


ALTER TABLE `service_inventory_uat_v4`.`si_service_detail` 
ADD COLUMN `access_topology` VARCHAR(45) NULL COMMENT 'Access Topology for GVPN Siolution : Example Values are Resilient/ Redundant/ Unmanaged ' AFTER `backup_config_mode`;

CREATE OR REPLACE
VIEW `vw_si_service_info` AS
    SELECT 
        `srvDetail`.`id` AS `sys_id`,
        `srvDetail`.`tps_service_id` AS `srv_service_id`,
        `srvDetail`.`site_alias` AS `srv_site_alias`,
        `srvDetail`.`erf_prd_catalog_product_id` AS `srv_product_family_id`,
        `srvDetail`.`erf_prd_catalog_offering_id` AS `srv_product_offering_id`,
        `srvDetail`.`erf_prd_catalog_product_name` AS `srv_product_family_name`,
        `srvDetail`.`erf_prd_catalog_offering_name` AS `srv_product_offering_name`,
        `srvDetail`.`primary_tps_service_id` AS `srv_primary_service_id`,
        `srvDetail`.`service_sequence` AS `srv_service_record_sequence`,
        `srvDetail`.`primary_secondary` AS `srv_pri_sec`,
        `srvDetail`.`pri_sec_service_link` AS `srv_pri_sec_link`,
        `srvDetail`.`gsc_order_sequence_id` AS `srv_gsc_order_sequence_id`,
        `srvDetail`.`lat_long` AS `srv_lat_long`,
        `srvDetail`.`service_classification` AS `srv_service_classification`,
        `srvDetail`.`service_option` AS `srv_service_management_option`,
        `srvDetail`.`site_topology` AS `srv_gvpn_site_topology`,
        `srvDetail`.`service_topology` AS `srv_vpn_topology`,
        `srvDetail`.`vpn_name` AS `srv_vpn_name`,
        `srvDetail`.`service_class` AS `srv_gvpn_cos`,
        `srvDetail`.`site_address` AS `srv_customer_site_address`,
        `srvDetail`.`access_type` AS `srv_access_type`,
        `srvDetail`.`bw_portspeed` AS `srv_bandwidth`,
        `srvDetail`.`bw_unit` AS `srv_bandwidth_unit`,
        `srvDetail`.`bw_portspeed_alt_name` AS `srv_bandwidth_disp_name`,
        `srvDetail`.`lastmile_bw` AS `srv_lastmile_bandwidth`,
        `srvDetail`.`lastmile_bw_unit` AS `srv_lastmile_bandwidth_unit`,
        `srvDetail`.`lastmile_bw_alt_name` AS `srv_lastmile_bandwidth_disp_name`,
        `srvDetail`.`pop_site_address` AS `srv_pop_address`,
        `srvDetail`.`pop_site_code` AS `srv_pop_code`,
        `srvDetail`.`source_country` AS `srv_source_country`,
        `srvDetail`.`service_status` AS `srv_service_status`,
        `srvDetail`.`service_commissioned_date` AS `srv_commissioned_date`,
        `srvDetail`.`service_termination_date` AS `srv_termination_date`,
        `srvDetail`.`lastmile_provider` AS `srv_lastmile_provider`,
        `srvDetail`.`lastmile_type` AS `srv_last_mile_type`,
        `srvDetail`.`burstable_bw_portspeed` AS `srv_burstable_bw`,
        `srvDetail`.`burstable_bw_unit` AS `srv_vurstable_bw_unit`,
        `srvDetail`.`burstable_bw_portspeed_alt_name` AS `srv_burstable_bw_disp_name`,
        `srvDetail`.`destination_country` AS `srv_destination_country`,
        `srvDetail`.`destination_country_code` AS `srv_destination_country_code`,
        `srvDetail`.`destination_country_code_repc` AS `srv_destination_country_code_repc`,
        `srvDetail`.`destination_city` AS `srv_destination_city`,
        `srvDetail`.`source_country_code` AS `srv_source_country_code`,
        `srvDetail`.`source_country_code_repc` AS `srv_source_country_code_repc`,
        `srvDetail`.`source_city` AS `srv_source_city`,
        `srvDetail`.`site_type` AS `srv_site_type`,
        `srvDetail`.`site_end_interface` AS `srv_site_end_interface`,
        `srvDetail`.`demarcation_floor` AS `demarcation_floor`,
        `srvDetail`.`denarcation_room` AS `denarcation_room`,
        `srvDetail`.`demarcation_apartment` AS `demarcation_apartment`,
        `ordDetail`.`id` AS `order_sys_id`,
        `ordDetail`.`op_order_code` AS `order_code`,
        `ordDetail`.`opportunity_classification` AS `opportunity_type`,
        `ordDetail`.`demo_flag` AS `order_demo_flag`,
        `ordDetail`.`erf_cust_customer_id` AS `order_customer_id`,
        `ordDetail`.`erf_cust_customer_name` AS `order_customer`,
        `ordDetail`.`erf_cust_le_id` AS `order_cust_le_id`,
        `ordDetail`.`erf_cust_le_name` AS `order_cust_le_name`,
        `ordDetail`.`erf_cust_sp_le_id` AS `order_sp_le_id`,
        `ordDetail`.`erf_cust_sp_le_name` AS `order_sp_le_name`,
        `ordDetail`.`erf_cust_partner_id` AS `order_partner`,
        `ordDetail`.`erf_cust_partner_name` AS `order_partner_name`,
        `ordDetail`.`erf_user_initiator_id` AS `order_initiator`,
        `ordDetail`.`created_by` AS `order_created_by`,
        `ordDetail`.`created_date` AS `order_created_date`,
        `ordDetail`.`is_active` AS `order_is_active`,
        `ordDetail`.`is_multiple_le` AS `order_multiple_le_flag`,
        `ordDetail`.`is_bundle_order` AS `order_bundle_order_flag`,
        `contractInfo`.`tps_sfdc_cuid` AS `sfdc_cuid`,
        `contractInfo`.`account_manager` AS `account_manager`,
        `contractInfo`.`account_manager_email` AS `account_manager_email`,
        `contractInfo`.`customer_contact` AS `customer_contact`,
        `contractInfo`.`customer_contact_email` AS `customer_contact_email`,
        `contractInfo`.`order_term_in_months` AS `order_term_in_months`,
        `contractInfo`.`billing_frequency` AS `billing_frequency`,
        `contractInfo`.`contract_start_date` AS `contract_start_date`,
        `contractInfo`.`contract_end_date` AS `contract_end_date`,
        `contractInfo`.`last_macd_date` AS `last_macd_date`,
        `contractInfo`.`mrc` AS `mrc`,
        `contractInfo`.`nrc` AS `nrc`,
        `contractInfo`.`arc` AS `arc`,
        `contractInfo`.`discount_mrc` AS `discount_mrc`,
        `contractInfo`.`discount_nrc` AS `discount_nrc`,
        `contractInfo`.`discount_arc` AS `discount_arc`,
        `contractInfo`.`erf_cust_currency_id` AS `currency_id`,
        `contractInfo`.`billing_address` AS `billing_address`,
        `contractInfo`.`billing_method` AS `billing_method`,
        `srvDetail`.`customer_service_id` AS `customer_service_id`,
        `srvDetail`.`parent_bundle_service_id` AS `parent_bundle_service_id`,
        `srvDetail`.`parent_bundle_service_name` AS `parent_bundle_service_name`,
        `srvDetail`.`usage_model` AS `usage_model`,
        `srvDetail`.`routing_protocol` AS `routing_protocol`,
        `srvDetail`.`ip_address_arrangement_type` AS `ip_address_arrangement_type`,
        `srvDetail`.`ipv4_address_pool_size` AS `ipv4_address_pool_size`,
        `srvDetail`.`ipv6_address_pool_size` AS `ipv6_address_pool_size`,
        `srvDetail`.`backup_config_mode` AS `backup_config_mode`,
        `srvDetail`.`access_topology` AS `access_topology`
    FROM
        ((`si_service_detail` `srvDetail`
        JOIN `si_order` `ordDetail` ON (`srvDetail`.`si_order_id` = `ordDetail`.`id`))
        JOIN `si_contract_info` `contractInfo` ON (`contractInfo`.`SI_order_id` = `ordDetail`.`id`))
;

-- --- --- 

CREATE OR REPLACE
VIEW `vw_service_asset_info` AS
    SELECT 
        `srvAsset`.`id` AS `asset_sys_id`,
        `srvAssetMap`.`SI_service_detail_id` AS `srv_sys_id`,
        `srvAssetMap`.`SI_service_detail_tps_service_id` AS `service_id`,
        `srvAsset`.`name` AS `asset_name`,
        `srvAsset`.`type` AS `asset_type`,
        `srvAsset`.`fqdn` AS `fqdn`,
        `srvAsset`.`management_ip` AS `management_ip`,
        `srvAsset`.`public_ip` AS `public_ip`,
        `srvAsset`.`gateway_ip` AS `gateway_ip`,
        `srvAsset`.`parent_id` AS `parent_id`,
        `srvAsset`.`description` AS `description`,
        `srvAsset`.`model` AS `model`,
        `srvAsset`.`mac_id` AS `mac_id`,
        `srvAsset`.`monitoring_tool` AS `monitoring_tool`,
        `srvAsset`.`managed_by` AS `managed_by`,
        `srvAsset`.`serial_no` AS `serial_no`,
        `srvAsset`.`owner` AS `owner`,
        `srvAsset`.`circuit_id` AS `circuit_id`,
        `srvAssetMap`.`is_active` AS `is_active`,
        `srvAssetMap`.`start_date` AS `asset_to_srv_support_start_date`,
        `srvAssetMap`.`end_date` AS `asset_to_srv_support_end_date`,
        `srvAssetMap`.`circuit_status` AS `circuit_status`,
        `srvAsset`.`origin_ntwrk` AS `origin_ntwrk`,
        `srvAsset`.`scope_of_management` AS `scope_of_management`,
        `srvAsset`.`support_type` AS `support_type`,
        `srvAsset`.`is_shared_ind` AS `is_shared_ind`,
        `srvAsset`.`wan_ip_address` AS `wan_ip_address`,
        `srvAsset`.`wan_ip_provider` AS `wan_ip_provider`
    FROM
        (`si_asset` `srvAsset`
        JOIN `si_asset_to_service` `srvAssetMap` ON (`srvAssetMap`.`SI_Asset_ID` = `srvAsset`.`id`))
;


ALTER TABLE `service_inventory_uat_v4`.`si_asset_to_service`  ADD COLUMN `date_of_installation` TIMESTAMP NULL AFTER `erf_cust_currency_id`;

ALTER TABLE `service_inventory_uat_v4`.`si_contract_info`  ADD COLUMN `billing_start_date` TIMESTAMP NULL AFTER `billing_method`, ADD COLUMN `billing_end_date` TIMESTAMP NULL AFTER `billing_start_date`;

