CREATE TABLE `quote_cloud` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`service_id` VARCHAR(50) NULL DEFAULT NULL,
	`cloud_code` VARCHAR(100) NULL DEFAULT NULL,
	`parent_cloud_code` VARCHAR(100) NULL DEFAULT NULL,
	`quote_to_le_id` INT(11) NULL DEFAULT NULL,
	`quote_id` INT(11) NULL DEFAULT NULL,
	`product_solutions_id` INT(11) NULL DEFAULT NULL,
	`dc_location_id` VARCHAR(50) NULL DEFAULT NULL,
	`resource_display_name` VARCHAR(50) NULL DEFAULT NULL,
	`arc` DOUBLE NULL DEFAULT NULL,
	`mrc` DOUBLE NULL DEFAULT NULL,
	`nrc` DOUBLE NULL DEFAULT NULL,
	`tcv` DOUBLE NULL DEFAULT NULL,
	`effective_date` TIMESTAMP NULL DEFAULT NULL,
	`fp_status` ENUM('P','MP') NULL DEFAULT 'P',
	`is_active` CHAR(1) NULL DEFAULT 'Y',
	`status` VARCHAR(45) NULL DEFAULT NULL,
	`is_task_triggered` TINYINT(4) NOT NULL,
	`created_time` TIMESTAMP NULL DEFAULT NULL,
	`created_by` INT(11) NULL DEFAULT NULL,
	`updated_by` INT(11) NULL DEFAULT NULL,
	`updated_time` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_quote_cloud_quote_to_le` (`quote_to_le_id`),
	INDEX `FK_quote_cloud_quote` (`quote_id`),
	INDEX `FK_quote_cloud_product_solutions` (`product_solutions_id`),
	CONSTRAINT `FK_quote_cloud_product_solutions` FOREIGN KEY (`product_solutions_id`) REFERENCES `product_solutions` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK_quote_cloud_quote` FOREIGN KEY (`quote_id`) REFERENCES `quote` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK_quote_cloud_quote_to_le` FOREIGN KEY (`quote_to_le_id`) REFERENCES `quote_to_le` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

CREATE TABLE `odr_service_commercial_component` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`partent_item` INT(11) NULL DEFAULT NULL,
	`odr_service_detail_id` INT(11) NOT NULL,
	`item_type` VARCHAR(50) NOT NULL,
	`item` VARCHAR(250) NOT NULL,
	`nrc` DOUBLE NOT NULL,
	`mrc` DOUBLE NOT NULL,
	`arc` DOUBLE NOT NULL,
	`created_by` VARCHAR(45) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_odr_service_commerical_component_odr_service_detail` (`odr_service_detail_id`),
	CONSTRAINT `FK_odr_service_commerical_component_odr_service_detail` FOREIGN KEY (`odr_service_detail_id`) REFERENCES `odr_service_detail` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;


