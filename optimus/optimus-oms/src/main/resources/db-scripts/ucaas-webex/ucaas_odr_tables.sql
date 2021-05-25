use oms;

CREATE TABLE `odr_webex_service_commercial` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`odr_service_detail_id` INT(11) NULL DEFAULT NULL,
	`component_name` VARCHAR(100) NULL DEFAULT NULL,
	`component_desc` VARCHAR(100) NULL DEFAULT NULL,
	`quantity` INT(11) NULL DEFAULT NULL,
	`unit_mrc` DOUBLE NULL DEFAULT NULL,
	`mrc` DOUBLE NULL DEFAULT NULL,
	`unit_nrc` DOUBLE NULL DEFAULT NULL,
	`nrc` DOUBLE NULL DEFAULT NULL,
	`arc` DOUBLE NULL DEFAULT NULL,
	`tcv` DOUBLE NULL DEFAULT NULL,
	`cisco_unit_list_price` DOUBLE NULL DEFAULT NULL,
	`cisco_discnt_prct` DOUBLE NULL DEFAULT NULL,
	`cisco_unit_net_price` DOUBLE NULL DEFAULT NULL,
	`is_active` CHAR(1) NULL DEFAULT 'Y',
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(45) NULL DEFAULT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(45) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
ENGINE=InnoDB;