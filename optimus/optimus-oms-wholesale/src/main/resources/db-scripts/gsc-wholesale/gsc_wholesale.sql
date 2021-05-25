use oms_uat;

alter table quote_to_le
    add is_wholesale tinyint null;

alter table order_to_le
	add is_wholesale tinyint null;

-- Master product component data
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
values ('ACLNS', 'ACLNS product for GSC product family', 1, 'gsc', current_timestamp());

-- Master product component data
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
values ('ACSNS', 'ACSNS product for GSC product family', 1, 'gsc', current_timestamp());


INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
values ('Dedicated', 'Dedicated ', 1, 'gsc', current_timestamp());

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
values ('Dedicated', 'Dedicated access type GSC product family', 1, 'gsc', current_timestamp());

INSERT INTO mst_oms_attributes (name, description, is_active, created_by, created_time)
values ('Interconnect ID', 'Interconnect ID ', 1, 'gsc', current_timestamp());


use customer_uat;
INSERT INTO mst_le_attributes (name, description, type, status) VALUES ('TSA', 'TSA Attachment', 'Attachment', null)
INSERT INTO mst_le_attributes (name, description, type, status) VALUES ('Service Addendum', 'Service Addendum', 'Attachment', null)

create table customer_le_secs_attribute_values
(
    id                   int NOT NULL auto_increment
        primary key,
    customer_le_id       int          null,
    customer_le_secs_id     int null,
    mst_le_attributes_id int          null,
    attribute_values     varchar(500) null,
    product_name         varchar(45)  null,
    constraint fk_customer_secs_le_attribute_values_customer_legal_entity1
        foreign key (customer_le_id) references customer_legal_entity (id),
    constraint fk_customer_secs_le_attribute_values_mst_le_attributes1
        foreign key (mst_le_attributes_id) references mst_le_attributes (id),
    constraint fk_customer_secs_le_attribute_values_cus_le_entity_sap_code1
        foreign key (customer_le_secs_id) references customer_legal_entity_sap_code (id)
);

INSERT INTO oms_uat.product_attribute_master 
(name, description, status, category, created_by, created_time)
VALUES ('Origin Network', 'Origin Network', '1', 'NEW', 'gsc', now());
