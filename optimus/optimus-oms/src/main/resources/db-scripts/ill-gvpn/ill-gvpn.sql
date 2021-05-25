use oms_uat;

ALTER TABLE `quote_ill_sites`
ADD COLUMN `is_colo` tinyint NULL DEFAULT 0;