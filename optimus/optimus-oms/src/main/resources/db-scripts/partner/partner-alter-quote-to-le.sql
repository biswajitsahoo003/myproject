use oms_uat;

alter table `quote_to_le`
	ADD COLUMN `classification` VARCHAR(20) NULL DEFAULT NULL AFTER `erf_service_inventory_tps_service_id`;

alter table `order_to_le`
	ADD COLUMN `classification` VARCHAR(20) NULL DEFAULT NULL AFTER `currency_code`;

alter table oms_attachments modify attachment_type enum('Tax', 'COF', 'Others', 'LOU', 'Solution', 'Partner') null;

