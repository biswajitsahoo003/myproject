use oms_uat;

CREATE TABLE `order_izosdwan_vutm_location_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location_id` int(11) DEFAULT NULL,
  `breakup_location` varchar(45) DEFAULT NULL,
  `max_bw` varchar(45) DEFAULT NULL,
  `default_bw` varchar(45) DEFAULT NULL,
  `selected_bw` varchar(45) DEFAULT NULL,
  `is_active` int(11) DEFAULT NULL,
  `reference_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
)