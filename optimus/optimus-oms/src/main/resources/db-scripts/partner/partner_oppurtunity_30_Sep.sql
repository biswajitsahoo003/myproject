use oms_uat;

alter table `opportunity`
	ADD COLUMN `is_deal_registration` VARCHAR(5) NULL DEFAULT NULL AFTER `opty_summary`,
    ADD COLUMN `deal_registration_date` VARCHAR(20) NULL DEFAULT NULL AFTER `is_deal_registration`;

INSERT INTO `notification_nfw`.`notification_template` (`name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES ('gsc_sell_through_verbal_agreement', 'GSTVA', 'gsc_sell_through_verbal_agreement', 'Email', '1');

