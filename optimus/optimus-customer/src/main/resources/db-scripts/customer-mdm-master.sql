use customer;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Notice_Address', 'Supplier Notice Address', 'Notice Address', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Notice_Address')
LIMIT 1;


INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Revenue_Segment', 'Supplier Revenue Segment', 'Revenue', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Revenue_Segment')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Billing_code', 'Supplier Billing code', 'Billing code', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Billing_code')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Customer_Type', 'Supplier Customer Type', 'Customer Type', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Customer_Type')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'GST_Address', 'Supplier GST Address', 'GST Address', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'GST_Address')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'GST_Number', 'Supplier GST Number', 'GST Number', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'GST_Number')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Billing Type', 'Customer Legal Billing Type', 'Billing', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Billing Type')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Billing Method', 'Customer Legal Billing Method', 'Billing', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Billing Method')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Billing Frequency', 'Customer Legal Billing Frequency', 'Billing', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Billing Frequency')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Invoice Method', 'Customer Legal Invoice Method', 'Invoice', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Invoice Method')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Billing Currency', 'Customer Legal Billing Currency', 'Billing', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Billing Currency')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Payment Currency', 'Customer Legal Payment Currency', 'Payment', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Payment Currency')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Payment Term (from date of invoice)', 'Customer Legal Payment Term', 'Payment', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Payment Term (from date of invoice)')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Customer Contracting Entity', 'Customer contracting Entity Name', 'Contact', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Customer Contracting Entity')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Customer Contact Name', 'Customer Contact Name ', 'Contact', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Customer Contact Name')
LIMIT 1;

# INSERT INTO mst_le_attributes (name, description, type, status)
# SELECT *
# FROM (SELECT 'Customer Contact Title / Designation', 'Customer Contact Title / Designation Name', 'Contact', 1) AS tmp
# WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name 'Customer Contact Title / Designation')
# LIMIT 1;


INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Billing Address', 'Customer Address', 'Address', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Billing Address')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'MSA', 'MSA Attachment', 'Attachment', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'MSA')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Service Schedule', 'Service Schedule ', 'Attachment', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Service Schedule')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Account Manager', 'Account Manager ', 'Manager', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Account Manager')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Supplier Contracting Entity', 'Supplier Contracting Entity ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Supplier Contracting Entity')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Supplier Contact Name', 'Supplier Contact Name ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Supplier Contact Name')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Supplier Contact Title/Designation', 'Supplier Contact Title/Designation ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Supplier Contact Title/Designation')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Supplier Mobile', 'Supplier Mobile ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Supplier Mobile')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Supplier LandlineNumber', 'Supplier LandlineNumber ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Supplier LandlineNumber')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Supplier Email ID', 'Supplier Email ID ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Supplier Email ID')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Credit Limit', 'Credit Limit ', 'Billing', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Credit Limit')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Deposit Amount', 'Deposit Amount ', 'Billing', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Deposit Amount')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Regional Manager Email', 'Regional Manager Email ', 'Manager', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Regional Manager Email')
LIMIT 1;

# INSERT INTO mst_le_attributes (name, description, type, status)
# SELECT *
# FROM (SELECT '111', '18', '1', 1) AS tmp
# WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = '111')
# LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'SUPPLIER_LE_OWNER', 'SUPPLIER_LE_OWNER ', 'Supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'SUPPLIER_LE_OWNER')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'SUPPLIER_LE_EMAIL', 'SUPPLIER_LE_EMAIL ', 'Supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'SUPPLIER_LE_EMAIL')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Le Contact', 'Supplier Le Owner Contact', 'Supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Le Contact')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Customer Contact Email', 'Customer Contact Email ', 'Contact', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Customer Contact Email')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'SSClickThrough', 'SSClickThrough ', 'ss', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'SSClickThrough')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'MSAClickThrough', 'MSAClickThrough ', 'MSA', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'MSAClickThrough')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Billing Increment', 'Billing Increment ', 'Billing Increment Type', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Billing Increment')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Payment Options', 'Payment Options ', 'Payment Options type', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Payment Options')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Applicable Timezone', 'Applicable Timezone ', 'Timezone Type', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Applicable Timezone')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Pre Payment Value', 'Pre Payment Value ', 'Pre Payment Type', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Pre Payment Value')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'DOCUSIGN_AVAILABILITY', 'DOCUSIGN_AVAILABILITY ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'DOCUSIGN_AVAILABILITY')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'VAT Number', 'VAT Number ', 'VAT', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'VAT Number')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'CLICK_THROUGH', 'CLICK_THROUGH ', 'supplier', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'CLICK_THROUGH')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'PO Number', 'PO Number ', 'PO Number', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'PO Number')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Department Name', 'Department Name ', 'Department Name', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Department Name')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'Send Invoice To', 'Send Invoice To ', 'Send Invoice To', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'Send Invoice To')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'VAT Address', 'VAT Address ', 'VAT', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'VAT Address')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'NAT_VALUE', 'NAT_VALUE ', 'Service Schedule', 1) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'NAT_VALUE')
LIMIT 1;

INSERT INTO mst_le_attributes (name, description, type, status)
SELECT *
FROM (SELECT 'ORG_NO', 'ORG NO FOR SAP CODE OR SECS CODE', 'Billing Number', null) AS tmp
WHERE NOT EXISTS(SELECT name FROM mst_le_attributes where name = 'ORG_NO')
LIMIT 1;


INSERT INTO customer_le_attribute_values (customer_le_id, mst_le_attributes_id, attribute_values, product_name) VALUES (752, 35, 'true', 'GSIP');
INSERT INTO customer_le_attribute_values (customer_le_id, mst_le_attributes_id, attribute_values, product_name) VALUES (752, 40, 'true', 'GSIP');
INSERT INTO customer_le_attribute_values (customer_le_id, mst_le_attributes_id, attribute_values, product_name) VALUES (752, 47, '7023', 'GSIP');
INSERT INTO customer_le_attribute_values (customer_le_id, mst_le_attributes_id, attribute_values, product_name) VALUES (752, 8, 'Arrears', 'GSIP');
INSERT INTO customer_le_attribute_values (customer_le_id, mst_le_attributes_id, attribute_values, product_name) VALUES (752, 13, '60 Days', 'GSIP');
INSERT INTO customer_le_attribute_values (customer_le_id, mst_le_attributes_id, attribute_values, product_name) VALUES (752, 7, 'Standard', 'GSIP');
INSERT INTO customer_le_attribute_values (customer_le_id, mst_le_attributes_id, attribute_values, product_name) VALUES (752, 10, 'Electronic', 'GSIP');


/*create table scripts for customer_attachment table -> 25-09-2020 */

CREATE TABLE customer_attachment(
   id INT NOT NULL AUTO_INCREMENT,
   attachment_id VARCHAR(40) NOT NULL,
   attachment_name VARCHAR(100) NOT NULL,
   status INT NOT NULL,
   created_by VARCHAR(40) ,
   created_time TIMESTAMP,
   PRIMARY KEY ( id )
);

commit ;

