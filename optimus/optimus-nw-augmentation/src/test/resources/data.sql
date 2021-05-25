SET SQL_SAFE_UPDATES = 0;

use service_fulfillment_uat;
delete from sc_component_attributes  where sc_service_detail_id in (576);
delete from sc_component  where sc_service_detail_id in (576);
delete from  sc_attachment where service_id in (576) and attachment_id !=1; 
delete from appointment_documents where appointment_id in (select id from appointment  where service_id in (576));
delete from appointment_documents where appointment_id in (select id from appointment  where task_id in (select id from task where service_id=576));
delete from appointment   where task_id in (select id from task where service_id=576);
delete from appointment  where service_id in (576);
delete from task_assignment where task_id in (select id from task where service_id=576);
delete from task_tat_time where task_id in (select id from task where service_id=576);
delete from task_data where task_id in (select id from task where service_id=576);
delete from  task_plan_successor where task_plan_id in (select id from task_plan where service_id=576);
delete from  task_plan where service_id=576;
delete from  activity_plan_successor where activity_plan_id in (select id from activity_plan where service_id in (576));
delete from  activity_plan where service_id in (576);
delete from  process_task_log where task_id in (select id from task where service_id=576);
delete from  process_plan_successor where process_plan_id in (select id from process_plan where service_id in (576));
delete from  process_plan where service_id in (576);
delete from  stage_plan where service_id in (576);
delete from  vendors where task_id in (select id from task where service_id=576);
delete from  field_engineer where task_id in (select id from task where service_id=576);
delete from  task where service_id=576;
delete from  activity where process_id in (select id from process where stage_id in (select id from stage where service_id=576));
delete from  process where stage_id in (select id from stage where service_id=576);
delete from  stage where service_id in (576);

INSERT INTO `service_fulfillment`.`mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('provide-demarc-details', 'select-mux_error', 'SERVICE_ATTRIBUTES', 'errorMessage', null);
INSERT INTO `service_fulfillment`.`mst_task_attributes` (`mst_task_def_key`, `attribute_name`, `category`, `node_name`, `sub_category`) VALUES ('provide-ior-detail', 'select-cloud-name_error', 'SERVICE_ATTRIBUTES', 'errorMessage', null);

SET SQL_SAFE_UPDATES = 1;
commit;

