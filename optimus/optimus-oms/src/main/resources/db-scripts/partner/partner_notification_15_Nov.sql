use oms_uat;

alter table `partner_temp_customer_details`
	ADD COLUMN `erf_partner_legal_entity_id` VARCHAR(10) NULL DEFAULT NULL AFTER `erf_partner_id`;


use notification_nfw;

insert into `notification_action` (`name`, `erf_prd_catalog_product_name`, `code`, `is_default_user_notification_action`, `is_active`)
VALUES ('Partner Create Entity Notification', 'IAS', 'PCE-IAS', '0', '1');
insert into `notification_action` (`name`, `erf_prd_catalog_product_name`, `code`, `is_default_user_notification_action`, `is_active`)
VALUES ('Partner Create Entity Notification', 'GVPN', 'PCE-GVPN', '0', '1');
insert into `notification_action` (`name`, `erf_prd_catalog_product_name`, `code`, `is_default_user_notification_action`, `is_active`)
VALUES ('Partner Create Entity Notification', 'GSC', 'PCE-GSC', '0', '1');

insert into `notification_template` (`name`,`code`,`template_reference_name`,`notification_type`,`notification_header_footer_id`,`is_active`)
values ('gsc_sell_through_create_entity', 'PCE', 'gsc_sell_through_create_entity', 'Email', null, '1');