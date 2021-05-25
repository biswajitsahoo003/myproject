use oms;

CREATE TABLE `odr_teamsdr_service_commercial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `odr_service_detail_id` int(11) DEFAULT NULL,
  `component_name` varchar(100) DEFAULT NULL,
  `component_desc` varchar(100) DEFAULT NULL,
  `component_type` varchar(25) DEFAULT NULL,
  `charge_item` varchar(45) DEFAULT NULL,
  `hsn_code` varchar(45) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit_mrc` double DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `unit_nrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `effective_usage` double DEFAULT NULL,
  `effective_overage` double DEFAULT NULL,
  `contract_type` varchar(45) DEFAULT NULL,
  `is_active` char(1) DEFAULT 'Y',
  `created_date` timestamp NULL DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;