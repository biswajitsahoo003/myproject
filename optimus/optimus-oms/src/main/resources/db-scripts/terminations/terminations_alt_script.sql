use oms_uat;

CREATE TABLE `quote_site_service_termination_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `quote_site_to_service_id` INT(11) DEFAULT NULL,
  `effective_date_of_change` TIMESTAMP NULL,
  `customer_mail_received_date` TIMESTAMP NULL,
  `requested_date_for_termination` TIMESTAMP NULL,
  `term_in_months` INT(11) NULL,
  `sub_reason` VARCHAR(255) NULL,
  `reason_for_termination` VARCHAR(255) NULL,
  `communication_recipient` VARCHAR(255) NULL,
  `local_it_contact_name` VARCHAR(255) NULL,
  `local_it_contact_number` VARCHAR(255) NULL,
  `local_it_contact_email_id` VARCHAR(255) NULL,
  `internal_customer` VARCHAR(100) NULL,
  `customer_email_confirmation_erf_cus_attachment_id` INT(11) NULL,
  `etc_applicable` TINYINT(1) NULL,
  `handover_to` VARCHAR(255) NULL,
  `csm_non_csm_name` VARCHAR(255) NULL,
  `csm_non_csm_email_id` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`quote_site_to_service_id`),
  CONSTRAINT `quote_site_service_fk_1` FOREIGN KEY (`quote_site_to_service_id`) REFERENCES `quote_ill_site_to_service` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
  
  ALTER TABLE quote_to_le modify COLUMN quote_type enum('NEW','MACD','MIGRATION','CANCELLATION', 'TERMINATION') DEFAULT NULL;
  
  ALTER TABLE order_to_le modify COLUMN order_type enum('NEW','MACD','MIGRATION','CANCELLATION', 'TERMINATION') DEFAULT NULL;
  
  ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `csm_non_csm_contact_number` VARCHAR(255) NULL AFTER `csm_non_csm_email_id`;


CREATE TABLE `order_site_service_termination_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `order_site_to_service_id` INT(11) DEFAULT NULL,
  `effective_date_of_change` TIMESTAMP NULL,
  `customer_mail_received_date` TIMESTAMP NULL,
  `requested_date_for_termination` TIMESTAMP NULL,
  `term_in_months` INT(11) NULL,
  `sub_reason` VARCHAR(255) NULL,
  `reason_for_termination` VARCHAR(255) NULL,
  `communication_recipient` VARCHAR(255) NULL,
  `local_it_contact_name` VARCHAR(255) NULL,
  `local_it_contact_number` VARCHAR(255) NULL,
  `local_it_contact_email_id` VARCHAR(255) NULL,
  `internal_customer` VARCHAR(100) NULL,
  `customer_email_confirmation_erf_cus_attachment_id` INT(11) NULL,
  `etc_applicable` TINYINT(1) NULL,
  `handover_to` VARCHAR(255) NULL,
  `csm_non_csm_name` VARCHAR(255) NULL,
  `csm_non_csm_email_id` VARCHAR(255) NULL,
  `csm_non_csm_contact_number` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`order_site_to_service_id`),
  CONSTRAINT `order_site_service_fk_1` FOREIGN KEY (`order_site_to_service_id`) REFERENCES `order_ill_site_to_service` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
  ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `termination_sub_type` VARCHAR(255) NULL AFTER `csm_non_csm_contact_number`;

 ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `termination_sub_type` VARCHAR(255) NULL AFTER `csm_non_csm_contact_number`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `quote_to_le_id` INT(11) NULL AFTER `quote_site_to_service_id`;

ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `order_to_le_id` INT(11) NULL AFTER `order_site_to_service_id`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `terminated_parent_order_code` VARCHAR(255) NULL AFTER `termination_sub_type`,
ADD COLUMN `created_by` INT(11) NULL AFTER `terminated_parent_order_code`,
ADD COLUMN `created_time` TIMESTAMP NULL AFTER `created_by`,
ADD COLUMN `updated_by` INT(11) NULL AFTER `created_time`,
ADD COLUMN `updated_time` TIMESTAMP NULL AFTER `updated_by`;

ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `terminated_parent_order_code` VARCHAR(255) NULL AFTER `termination_sub_type`,
ADD COLUMN `created_by` INT(11) NULL AFTER `terminated_parent_order_code`,
ADD COLUMN `created_time` TIMESTAMP NULL AFTER `created_by`,
ADD COLUMN `updated_by` INT(11) NULL AFTER `created_time`,
ADD COLUMN `updated_time` TIMESTAMP NULL AFTER `updated_by`;


ALTER TABLE `oms_attachments` 
CHANGE COLUMN `attachment_type` `attachment_type` ENUM('Tax','COF','Others','LOU','Solution','Partner','MF','SDD','Cancellation','TRF') NULL DEFAULT NULL ;

ALTER TABLE `oms_attachments` 
CHANGE COLUMN `attachment_type` `attachment_type` ENUM('Tax','COF','Others','LOU','Solution','Partner','MF','SDD','Cancellation','TRF','CUSTEMAIL') NULL DEFAULT NULL ;


ALTER TABLE `order_site_service_termination_details` 
CHANGE COLUMN `term_in_months` `term_in_months` VARCHAR(50) NULL DEFAULT NULL ,
ADD COLUMN `termination_send_to_td_date` TIMESTAMP NULL AFTER `terminated_parent_order_code`;


ALTER TABLE `quote_site_service_termination_details` 
CHANGE COLUMN `term_in_months` `term_in_months` VARCHAR(50) NULL DEFAULT NULL ,
ADD COLUMN `termination_send_to_td_date` TIMESTAMP NULL AFTER `terminated_parent_order_code`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `sales_task_response` VARCHAR(500) NULL AFTER `termination_send_to_td_date`;

ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `sales_task_response` VARCHAR(500) NULL AFTER `termination_send_to_td_date`;

ALTER TABLE `quote_ill_site_to_service` 
ADD COLUMN `tps_sfdc_opty_id` VARCHAR(255) NULL AFTER `erf_service_inventory_parent_order_id`;
ALTER TABLE `quote_ill_site_to_service` 
ADD COLUMN `tps_sfdc_product_id` VARCHAR(255) NULL AFTER `tps_sfdc_opty_id`,
ADD COLUMN `tps_sfdc_product_name` VARCHAR(255) NULL AFTER `tps_sfdc_product_id`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `o2c_call_initiated_date` TIMESTAMP NULL AFTER `sales_task_response`;

ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `o2c_call_initiated_date` TIMESTAMP NULL AFTER `sales_task_response`;

ALTER TABLE `odr_service_detail` 
ADD COLUMN `termination_reason` VARCHAR(255) NULL AFTER `multi_vrf_solution`,
ADD COLUMN `negotiation_required` VARCHAR(100) NULL AFTER `termination_reason`,
ADD COLUMN `termination_effective_date` TIMESTAMP NULL AFTER `negotiation_required`,
ADD COLUMN `etc_value` DOUBLE NULL AFTER `termination_effective_date`,
ADD COLUMN `etc_waiver` DOUBLE NULL AFTER `etc_value`,
ADD COLUMN `customer_requestor_date` TIMESTAMP NULL AFTER `etc_waiver`;


ALTER TABLE `order_ill_site_to_service` 
ADD COLUMN `tps_sfdc_opty_id` VARCHAR(255) NULL AFTER `erf_service_inventory_parent_order_id`;
ALTER TABLE `order_ill_site_to_service` 
ADD COLUMN `tps_sfdc_product_id` VARCHAR(255) NULL AFTER `tps_sfdc_opty_id`,
ADD COLUMN `tps_sfdc_product_name` VARCHAR(255) NULL AFTER `tps_sfdc_product_id`;

ALTER TABLE `odr_order` 
ADD COLUMN `termination_parent_order_code` VARCHAR(45) NULL DEFAULT NULL AFTER `parent_order_type`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `retention_reason` VARCHAR(500) NULL DEFAULT NULL AFTER `o2c_call_initiated_date`;

ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `retention_reason` VARCHAR(500) NULL DEFAULT NULL AFTER `o2c_call_initiated_date`;


ALTER TABLE `thirdparty_service_jobs` 
ADD COLUMN `service_ref_id` VARCHAR(255) NULL AFTER `is_complete`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `actual_etc` double NULL DEFAULT 0 AFTER `etc_applicable`,
ADD COLUMN `proposed_etc` double NULL AFTER `actual_etc`,
ADD COLUMN `waiver_type` VARCHAR(15) NULL AFTER `proposed_etc`,
ADD COLUMN `waiver_policy` VARCHAR(255) NULL AFTER `waiver_type`,
ADD COLUMN `waiver_remarks` VARCHAR(255) NULL AFTER `waiver_policy`;

ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `actual_etc` double NULL DEFAULT 0 AFTER `etc_applicable`,
ADD COLUMN `proposed_etc` double NULL AFTER `actual_etc`,
ADD COLUMN `waiver_type` VARCHAR(15) NULL AFTER `proposed_etc`,
ADD COLUMN `waiver_policy` VARCHAR(255) NULL AFTER `waiver_type`,
ADD COLUMN `waiver_remarks` VARCHAR(255) NULL AFTER `waiver_policy`;

INSERT INTO `product_attribute_master` (`name`, `description`, `status`) VALUES ('ETC Charges', 'ETC Charges', '1');


use `notification_nfw`;
INSERT INTO `notification_template` (`id`, `name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES ('101', 'termination_acknowledgement_letter', 'TERMACK', 'terminations acknowledgement template', 'Email', '1');

INSERT INTO `notification_template` (`id`, `name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES ('102', 'termination_initiation_letter', 'TERMINIT', 'terminations initiation template', 'Email', '1');


-- etc waiver scripts --

use oms_uat;
CREATE TABLE `attachments_audit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `quote_code` VARCHAR(255) NULL,
  `quote_to_le_id` INT(11) NULL,
  `quote_site_id` INT(11) NULL,
  `quote_link_id` INT(11) NULL,
  `attachment_type` VARCHAR(255) NULL,
  `erf_cus_attachment_id` INT(11) NULL,
  `version` INT(11) NULL,
  `created_by` INT(11) NULL,
  `created_time` TIMESTAMP NULL,
  `updated_by` INT(11) NULL,
  `updated_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`));
  
  ALTER TABLE `attachments_audit` 
CHANGE COLUMN `attachment_type` `attachment_type` ENUM('Tax','COF','Others','LOU','Solution','Partner','MF','SDD','Cancellation','TRF','CUSTEMAIL') NULL DEFAULT NULL;

ALTER TABLE `attachments_audit` 
ADD COLUMN `reference_name` VARCHAR(255) NULL AFTER `version`,
ADD COLUMN `reference_id` INT(11) NULL AFTER `reference_name`;


ALTER TABLE `quote_ill_site_to_service` 
ADD COLUMN `tps_sfdc_waiver_id` VARCHAR(255) NULL AFTER `quote_izosdwan_site_id`,
ADD COLUMN `tps_sfdc_waiver_name` VARCHAR(255) NULL AFTER `tps_sfdc_waiver_id`;



ALTER TABLE `order_ill_site_to_service` 
ADD COLUMN `tps_sfdc_waiver_id` VARCHAR(255) NULL AFTER `order_izosdwan_site_id`,
ADD COLUMN `tps_sfdc_waiver_name` VARCHAR(255) NULL AFTER `tps_sfdc_waiver_id`;

ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `termination_remarks` VARCHAR(500) NULL AFTER `retention_reason`;



ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `termination_remarks` VARCHAR(500) NULL AFTER `retention_reason`;

ALTER TABLE `attachments_audit` 
ADD COLUMN `filename` VARCHAR(255) NULL AFTER `reference_id`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `final_etc` double NULL AFTER `proposed_etc`,
ADD COLUMN `proposed_by_sales` VARCHAR(15) NULL AFTER `waiver_remarks`;


ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `final_etc` double NULL AFTER `proposed_etc`,
ADD COLUMN `proposed_by_sales` VARCHAR(15) NULL AFTER `waiver_remarks`;

ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `regretted_non_regretted_termination` VARCHAR(255) NULL AFTER `termination_remarks`;


ALTER TABLE `order_site_service_termination_details` 
ADD COLUMN `regretted_non_regretted_termination` VARCHAR(255) NULL AFTER `termination_remarks`;

ALTER TABLE `oms_attachments` 
CHANGE COLUMN `attachment_type` `attachment_type` ENUM('Tax','COF','Others','LOU','Solution','Partner','MF','SDD','Cancellation','TRF','CUSTEMAIL','ADDLDOC','APPRVEMAIL') NULL DEFAULT NULL ;

ALTER TABLE `attachments_audit` 
CHANGE COLUMN `attachment_type` `attachment_type` ENUM('Tax','COF','Others','LOU','Solution','Partner','MF','SDD','Cancellation','TRF','CUSTEMAIL','ADDLDOC','APPRVEMAIL') NULL DEFAULT NULL ;

ALTER TABLE `odr_service_detail` 
CHANGE COLUMN `etc_waiver` `etc_waiver` VARCHAR(250) NULL DEFAULT NULL ;


ALTER TABLE `quote_site_service_termination_details` 
ADD COLUMN `waiver_approval_remarks` VARCHAR(255) NULL AFTER `proposed_by_sales`;









 