-- Need to be executed in oms 

use oms_uat;

ALTER TABLE `product_attribute_master` 
ADD COLUMN `category` VARCHAR(45) NULL DEFAULT 'NEW' COMMENT 'Specify whether the Attribute belongs to New Order/MAC Order / Both ' AFTER `status`;

ALTER TABLE `quote_to_le` 
ADD COLUMN `change_request_summary` VARCHAR(100) NULL DEFAULT NULL AFTER `currency_code`;

ALTER TABLE `quote_to_le` 
ADD COLUMN `erf_service_inventory_service_detail_id` INT(11) NULL DEFAULT NULL AFTER `change_request_summary`,
ADD COLUMN `erf_service_inventory_tps_service_id` VARCHAR(45) NULL DEFAULT NULL AFTER `erf_service_inventory_service_detail_id`;


ALTER TABLE `quote_ill_sites` 
ADD COLUMN `erf_service_inventory_tps_service_id` VARCHAR(45) NULL DEFAULT NULL AFTER `is_izo`,
ADD COLUMN `macd_change_bandwidth_flag` VARCHAR(45) NULL DEFAULT NULL AFTER `erf_service_inventory_tps_service_id`;


ALTER TABLE `order_ill_sites` 
ADD COLUMN `erf_service_inventory_tps_service_id` VARCHAR(45) NULL DEFAULT NULL AFTER `is_izo`,
ADD COLUMN `macd_change_bandwidth_flag` VARCHAR(45) NULL DEFAULT NULL AFTER `erf_service_inventory_tps_service_id`;

ALTER TABLE `oms_uat`.`quote_ill_sites` 
CHANGE COLUMN `macd_change_bandwidth_flag` `macd_change_bandwidth_flag` ENUM('Primary','Secondary','Both','Single') NULL DEFAULT NULL ;


ALTER TABLE `oms_uat`.`order_ill_sites` 
CHANGE COLUMN `macd_change_bandwidth_flag` `macd_change_bandwidth_flag` ENUM('Primary','Secondary','Both','Single') NULL DEFAULT NULL ;

ALTER TABLE `quote_to_le`
	ADD COLUMN `tps_sfdc_parent_opty_id` VARCHAR(50) NULL DEFAULT NULL AFTER `tps_sfdc_opty_id`;

ALTER TABLE `order_to_le`
	ADD COLUMN `tps_sfdc_parent_opty_id` VARCHAR(50) NULL DEFAULT NULL AFTER `tps_sfdc_copf_id`;  
 
CREATE TABLE `macd_detail` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`quote_to_le_id` INT(11) NULL DEFAULT NULL,
`tps_service_id` VARCHAR(50) NULL DEFAULT NULL,
`tps_sfdc_id` VARCHAR(50) NULL DEFAULT NULL,
`tps_sfdc_parent_opty_id` VARCHAR(50) NULL DEFAULT NULL,
`order_type` VARCHAR(50) NULL DEFAULT NULL,
`order_category` VARCHAR(50) NULL DEFAULT NULL,
`created_time` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(150) NULL DEFAULT NULL,
`updated_time` TIMESTAMP NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
`is_active` CHAR(1) NULL DEFAULT 'Y',
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
;


ALTER TABLE `oms_uat`.`macd_detail` 
ADD COLUMN `cancellation_date` TIMESTAMP NULL DEFAULT NULL AFTER `is_active`,
ADD COLUMN `cancellation_reason` VARCHAR(150) NULL DEFAULT NULL AFTER `cancellation_date`,
ADD COLUMN `stage` VARCHAR(100) NULL DEFAULT NULL AFTER `cancellation_reason`;


