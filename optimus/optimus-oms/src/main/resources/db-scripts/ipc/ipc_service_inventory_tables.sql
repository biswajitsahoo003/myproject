use service_inventory_uat_v4;

ALTER TABLE `si_asset`
	ADD COLUMN `business_unit` VARCHAR(45) NULL DEFAULT NULL AFTER `SHARED_CPE_SERVICE_ID`,
	ADD COLUMN `zone` VARCHAR(45) NULL DEFAULT NULL AFTER `business_unit`,
	ADD COLUMN `environment` VARCHAR(45) NULL DEFAULT NULL AFTER `zone`;

CREATE VIEW `vw_order_service_asset_info` AS
    SELECT 
        `so`.`id` AS `order_id`,
        `ssd`.`pop_site_code` AS `pop_site_code`,
        `ssd`.`uuid` AS `uuid`,
        `ssd`.`service_status` AS `service_status`,
        `sa`.`id` AS `asset_id`,
        `sa`.`business_unit` AS `business_unit`,
        `sa`.`zone` AS `zone`,
        `sa`.`type` AS `type`,
        `ssd`.`erf_prd_catalog_product_id` AS `erf_prd_catalog_product_id`,
        `so`.`erf_cust_le_id` AS `erf_cust_le_id`
    FROM
        (((`si_order` `so`
        JOIN `si_service_detail` `ssd` ON (`so`.`id` = `ssd`.`si_order_id`))
        JOIN `si_asset_to_service` `sas` ON (`sas`.`SI_service_detail_id` = `ssd`.`id`))
        JOIN `si_asset` `sa` ON (`sa`.`id` = `sas`.`SI_Asset_ID`));