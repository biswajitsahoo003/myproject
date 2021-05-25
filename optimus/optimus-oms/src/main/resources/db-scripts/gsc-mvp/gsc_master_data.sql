use oms_uat;
-- Quote configuration level component
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSIP.CONFIG', 'GSIP configuration common product component', 1, 'root', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'GSIP.CONFIG'
    )
LIMIT 1;

-- Quote level component
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSIP.COMMON', 'GSIP common product component', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'GSIP.COMMON'
    )
LIMIT 1;

-- Master product component data
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'ITFS', 'ITFS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'ITFS'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'DTFS', 'DTFS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'DTFS'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'LNS', 'LNS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'LNS'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'UIFN', 'UIFN product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'UIFN'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'VTS', 'VTS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'VTS'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Domestic Voice', 'Domestic Voice product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'Domestic Voice'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'ACANS', 'ACANS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'ACANS'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'ACDTFS', 'ACDTFS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'ACDTFS'
    )
LIMIT 1;

-- Master product component data
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'ACSNS', 'ACSNS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'ACSNS'
    )
LIMIT 1;

- Master product component data
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'ACLNS', 'ACLNS product for GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
        SELECT name FROM mst_product_component WHERE name = 'ACLNS'
    )
LIMIT 1;

-- Access type components
INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'PSTN', 'PSTN access type GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'PSTN'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Public IP', 'Public IP access type GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'Public IP'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'MPLS', 'MPLS access type GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'MPLS'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN', 'GVPN/MPLS access type GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'GVPN'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'NNI', 'NNI access type GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'NNI'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Dedicated', 'Dedicated access type GSC product family', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'Dedicated'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Global Outbound', 'Global Outbound ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'Global Outbound'
    )
LIMIT 1;

INSERT INTO mst_product_component (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'ORDER_GSC', 'ORDER_GSC ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_product_component WHERE name = 'ORDER_GSC'
    )
LIMIT 1;


-- Master product attributes for all product offerings and access types
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Certificate Authority Support', 'Certificate Authority Support ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Certificate Authority Support'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Termination Number (Working Outpulse)',
             'Termination Number (Working Outpulse) ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Termination Number (Working Outpulse)'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Origin country', 'Origin country ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Origin country'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Destination Country', 'Destination Country ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Destination Country'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Quantity Of Numbers', 'Quantity Of Numbers ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Quantity Of Numbers'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'No Of Concurrent channel', 'No Of Concurrent channel ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'No Of Concurrent channel'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'No Of Site per country', 'No Of Site per country ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'No Of Site per country'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Codec', 'Codec ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Codec'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Porting service needed', 'Porting service needed ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Porting service needed'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'List of numbers to be ported', 'List of numbers to be ported ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'List of numbers to be ported'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Calling Service Type', 'Calling Service Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Calling Service Type'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Customer Public IP', 'Customer Public IP ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Customer Public IP'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'IP address & Mask', 'IP address & Mask ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'IP address & Mask'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Routing Topology', 'Routing Topology ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Routing Topology'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Transport Protocol', 'Transport Protocol ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Transport Protocol'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Prefix addition', 'Prefix addition ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Prefix addition'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT '+ required on A & B number (E.164)',
             '+ required on A & B number (E.164) ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = '+ required on A & B number (E.164)'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'DTMF Relay support', 'DTMF Relay support ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'DTMF Relay support'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Supported SIP Privacy headers', 'Supported SIP Privacy headers ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Supported SIP Privacy headers'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Session Keep Alive Timer', 'Session Keep Alive Timer ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Session Keep Alive Timer'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Access Parameters', 'Access Parameters ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Access Parameters'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Existing Network', 'Existing Network ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Existing Network'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Service Type', 'Service Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Service Type'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Backup Port Required', 'Backup Port Required ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Backup Port Required'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'BGP Peering on', 'BGP Peering on ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'BGP Peering on'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Routes Exchanged', 'Routes Exchanged ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Routes Exchanged'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS Model', 'COS Model ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS Model'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS Profile', 'COS Profile ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS Profile'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS 1%', 'COS 1% ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS 1%'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS 2%', 'COS 2% ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS 2%'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS 3%', 'COS 3% ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS 3%'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS 4%', 'COS 4% ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS 4%'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS 5%', 'COS 5% ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS 5%'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'COS 6%', 'COS 6% ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'COS 6%'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Inter CoS Burst', 'Inter CoS Burst ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Inter CoS Burst'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'TCL Pop', 'TCL Pop ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'TCL Pop'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'BGP Timer', 'BGP Timer ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'BGP Timer'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Is Authentication Required for Protocol',
             'Is Authentication Required for Protocol ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Is Authentication Required for Protocol'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'BGP AS number', 'BGP AS number ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'BGP AS number'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Port Speed', 'Port Speed ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Port Speed'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Bandwidth Unit', 'Bandwidth Unit ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Bandwidth Unit'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Burstable Bandwidth', 'Burstable Bandwidth ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Burstable Bandwidth'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Interface', 'Interface ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Interface'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN Routing Protocol', 'GVPN Routing Protocol ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GVPN Routing Protocol'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Connector Type', 'Connector Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Connector Type'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN Service Type', 'GVPN Service Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GVPN Service Type'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'AS Number Provided By', 'AS Number Provided By ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'AS Number Provided By'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'WAN IP Address', 'WAN IP Address ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'WAN IP Address'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'CPE Required ?', 'CPE Required ? ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'CPE Required ?'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Site Type', 'Site Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Site Type'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN Resilency', 'GVPN Resilency ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GVPN Resilency'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'SLT Varient', 'SLT Varient ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'SLT Varient'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN Access Topology', 'GVPN Access Topology ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GVPN Access Topology'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Port Mode', 'Port Mode ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Port Mode'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Profile Topology', 'Profile Topology ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Profile Topology'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Usage Model', 'Usage Model ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Usage Model'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'IP Address Arrangement', 'IP Address Arrangement ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'IP Address Arrangement'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'WAN IP provided by', 'WAN IP provided by ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'WAN IP provided by'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Service Flavor', 'Service Flavor ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Service Flavor'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'VPN Topology', 'VPN Topology ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'VPN Topology'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'CPE Management', 'CPE Management ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'CPE Management'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Access Required ?', 'Access Required ? ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Access Required ?'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'CPE Supply Type', 'CPE Supply Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'CPE Supply Type'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Service Availibility', 'Service Availibility ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Service Availibility'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Reports Type', 'Reports Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Reports Type'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Multicast Required ?', 'Multicast Required ? ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Multicast Required ?'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Multicasting mode', 'Multicasting mode ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Multicasting mode'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'BFD Required', 'BFD Required ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'BFD Required'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Extended LAN Enabled', 'Extended LAN Enabled ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Extended LAN Enabled'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSIP Origin City', 'GSIP Origin City ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GSIP Origin City'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'List of numbers assigned/selected',
             'List of numbers assigned/selected ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'List of numbers assigned/selected'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Customer Public IP or FQDN', 'Customer Public IP or FQDN ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Customer Public IP or FQDN'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Equipment address', 'Equipment address ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Equipment address'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Dial plan logic (Prefix or CLI)',
             'Dial plan logic (Prefix or CLI) ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Dial plan logic (Prefix or CLI)'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Calls Per Second (CPS)', 'Calls Per Second (CPS) ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Calls Per Second (CPS)'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Do you support all certificate for Interconnect type MPLS with transport TLS. If no, provide customized value.',
             ' Do you support all certificate for Interconnect type MPLS with transport TLS. If no, provide customized value. ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name =
                  'Do you support all certificate for Interconnect type MPLS with transport TLS. If no, provide customized value.'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'City Selection', 'City Selection ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'City Selection'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Transport', 'Transport ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Transport'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Emergency Address', 'Emergency Address ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Emergency Address'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Requestor Date for Service', 'Requestor Date for Service ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Requestor Date for Service'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Expected Delivery Date', 'Expected Delivery Date ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Expected Delivery Date'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Inbound Volume', 'Inbound Volume ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Inbound Volume'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Inbound Countries', 'Inbound Countries ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Inbound Countries'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Outbound Volume', 'Outbound Volume ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Outbound Volume'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Outbound Countries', 'Outbound Countries ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Outbound Countries'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Rate per Minute(fixed)', 'Rate per Minute(fixed) ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Rate per Minute(fixed)'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Rate per Minute(mobile)', 'Rate per Minute(mobile) ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Rate per Minute(mobile)'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Rate per Minute(special)', 'Rate per Minute(special) ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Rate per Minute(special)'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Partner interconnect requirement -  Yes or NO (If Yes then (Prefix or CLI)).',
             'Partner interconnect requirement -  Yes or NO (If Yes then (Prefix or CLI)). ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Partner interconnect requirement -  Yes or NO (If Yes then (Prefix or CLI)).'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'DID MRC',
             'DID MRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'DID MRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Termination Name',
             'Termination Name ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Termination Name'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'DID NRC',
             'DID NRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'DID NRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'DID ARC',
             'DID ARC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'DID ARC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Order Setup MRC',
             'Order Setup MRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Order Setup MRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Order Setup NRC',
             'Order Setup NRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Order Setup NRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Order Setup ARC',
             'Order Setup ARC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Order Setup ARC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Channel MRC',
             'Channel MRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Channel MRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Channel NRC',
             'Channel NRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Channel NRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Channel ARC',
             'Channel ARC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Channel ARC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Termination Rate',
             'Termination Rate ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Termination Rate'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Service Availibility',
             'Service Availibility ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Service Availibility'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN MRC',
             'GVPN MRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GVPN MRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN NRC',
             'GVPN NRC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GVPN NRC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN ARC',
             'GVPN ARC ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GVPN ARC'
    )
LIMIT 1;
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GVPN TCV',
             'GVPN TCV ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GVPN TCV'
    )
LIMIT 1;

-- Additional attributes for configurations
INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Apply to All configurations',
             'Apply to All configurations ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
               SELECT name
               FROM product_attribute_master
               WHERE name = 'Apply to All configurations'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Apply to All Inbound products',
             'Apply to All Inbound products ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
               SELECT name
               FROM product_attribute_master
               WHERE name = 'Apply to All Inbound products'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Phone Type',
             'Phone Type ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
               SELECT name
               FROM product_attribute_master
               WHERE name = 'Phone Type'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Certificate Authority Support',
             'Certificate Authority Support ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
               SELECT name
               FROM product_attribute_master
               WHERE name = 'Certificate Authority Support'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Surcharge Rate',
             'Surcharge Rate ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Surcharge Rate'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'IP Address Space',
             'IP Address Space ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'IP Address Space'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'TIMEZONE',
             'TIMEZONE ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'TIMEZONE'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'UIFN Registration Charge',
             'UIFN Registration Charge ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'UIFN Registration Charge'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Global Outbound Rate Column',
             'Global Outbound Rate Column ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Global Outbound Rate Column'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'City wise Quantity Of Numbers',
             'City wise Quantity Of Numbers ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'City wise Quantity Of Numbers'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'City wise Porting service needed',
             'City wise Porting service needed ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'City wise Porting service needed'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'TFN_Reservation_ID',
             'TFN_Reservation_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'TFN_Reservation_ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Customer Device IP',
             'Customer Device IP ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Customer Device IP'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'FQDN',
             'FQDN ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'FQDN'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Additional Information',
             'Additional Information ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'Additional Information'
    )
LIMIT 1;


INSERT INTO mst_order_site_status (name, is_active)
SELECT *
FROM (SELECT 'MACD Order In Progress', 1) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM mst_order_site_status
            WHERE name = 'MACD Order In Progress'
    )
LIMIT 1;

INSERT INTO mst_oms_attributes (name,category, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'OSP Attachment',null, 'OSP Attachment ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_oms_attributes WHERE name = 'OSP Attachment'
    )
LIMIT 1;

COMMIT;

INSERT INTO mst_oms_attributes (name,category, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'CUSTOMER_SECS_ID',null, 'CUSTOMER_SECS_ID ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_oms_attributes WHERE name = 'CUSTOMER_SECS_ID'
    )
LIMIT 1;

COMMIT;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Old Termination Number (Working Outpulse)',
             'Old Termination Number (Working Outpulse) ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Old Termination Number (Working Outpulse)'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Termination Number ISD Code',
             'Termination Number ISD Code ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Termination Number ISD Code'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Admin Changed Price',
             'Admin Changed Price ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Admin Changed Price'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Required porting numbers',
             'Required porting numbers ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Required porting numbers'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID',
             'GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_DOMESTIC_DOWNSTREAM_ORDER_ID',
             'GSC_DOMESTIC_DOWNSTREAM_ORDER_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GSC_DOMESTIC_DOWNSTREAM_ORDER_ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID',
             'GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID',
             'GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID',
             'GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name
            FROM product_attribute_master
            WHERE name = 'GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID',
             'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GSC_INTERCONNECT_SUB_DOWNSTREAM_ORDER_ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS',
             'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT',
             'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC MACD Product Reference Order ID',
             'GSC MACD Product Reference Order ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'GSC MACD Product Reference Order ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'SIP Trunk',
             'SIP Trunk ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'SIP Trunk'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'NNI ID',
             'NNI ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'NNI ID'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Dedicated',
             'Dedicated ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Dedicated'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Selected Termination Number (Working Outpulse)',
             'Selected Termination Number (Working Outpulse) ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Selected Termination Number (Working Outpulse)'
    )
LIMIT 1;

COMMIT;

--insert script for MACD service attribute
-- 11/Nov/2019 After Boron Release

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'CIRCT_GR_CD_RERT',
             'CIRCT_GR_CD_RERT ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'CIRCT_GR_CD_RERT'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'SWTCH_UNIT_CD_RERT',
             'SWTCH_UNIT_CD_RERT ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'SWTCH_UNIT_CD_RERT'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Circuit_ID',
             'Circuit_ID ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Circuit_ID'
    )
LIMIT 1;

INSERT INTO mst_oms_attributes (name,category, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'Department Billing',
             'Department Billing ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_oms_attributes WHERE name = 'Department Billing'
    )
LIMIT 1;

INSERT INTO mst_oms_attributes (name,category, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'Department Name',
             'Department Name ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_oms_attributes WHERE name = 'Department Name'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'GSC MACD DID SITE ADDRESS', 'GSC MACD DID SITE ADDRESS ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
SELECT name FROM product_attribute_master WHERE name = 'GSC MACD DID SITE ADDRESS'
)
LIMIT 1;

INSERT INTO mst_oms_attributes (name, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'GSC Multi Macd',
              'GSC Multi Macd ',
              1,
              'gsc',
              current_timestamp()) AS tmp
WHERE NOT EXISTS(
                SELECT name FROM mst_oms_attributes WHERE name = 'GSC Multi Macd'
                )
LIMIT 1;


INSERT INTO mst_oms_attributes (name, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'Multi Macd Gsc Service details',
             'Multi Macd Gsc Service details ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM mst_oms_attributes WHERE name = 'Multi Macd Gsc Service details'
    )
LIMIT 1;

alter table quote_le_attribute_values
	modify column attribute_value text;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
VALUES ('Rate per Minute', 'Rate per Minute', 1, 'gsc', current_timestamp());

INSERT INTO mst_oms_attributes (name, description, is_active, created_by, created_time)
VALUES ('Add Permanently Blocked Destinations of GO', 'Add Permanently Blocked Destinations of Global Outbound', 1, 'gsc', current_timestamp());

INSERT INTO mst_oms_attributes (name, description, is_active, created_by, created_time)
VALUES ('Select All of GO', 'Select All of Global Outbound', 1, 'gsc', current_timestamp());

--pi_3_iteration_2

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Solution Type', 'Solution Type ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Solution Type'
    )
LIMIT 1;


INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Registration Number', 'Registration Number ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Registration Number'
    )
LIMIT 1;


INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Working Temporary Termination Number', 'Working Temporary Termination Number ', 1, 'gsc', current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Working Temporary Termination Number'
    )
LIMIT 1;

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
VALUES ('Type Of Cpe', 'Type Of Cpe', 1, 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
VALUES ('Power Cable', 'Power Cable', 1, 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
VALUES ('Additional CPE Information', 'Additional CPE Information', 1, 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
VALUES ('City wise Area Code', 'City wise Area Code', 1, 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
VALUES ('SFP', 'SFP module', 1, 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
VALUES ('NIM', 'NIM module', 1, 'gsc', current_timestamp());


-- For O2C
INSERT INTO mst_oms_attributes (name, category, description, is_active, created_by, created_time)
VALUES ('SUPPLIER_SECS_ID', null, 'SUPPLIER_SECS_ID ', 1, 'gsc', current_timestamp());
    INSERT
INTO product_attribute_master (name, description, status, category, created_by, created_time)
VALUES ('Approve Quote Type', 'Approve Quote Type ', 1, 'NEW', 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, category, created_by, created_time)
VALUES ('SUPPLIER_SECS_ID', 'SUPPLIER_SECS_ID ', 1, 'NEW', 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, category, created_by, created_time)
VALUES ('GSC Service Abbreviation', 'GSC Service Abbreviation ', 1, 'NEW', 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, category, created_by, created_time)
VALUES ('Termination Outpulse Type', 'Termination Outpulse Type ', 1, 'NEW', 'gsc', current_timestamp());

INSERT INTO product_attribute_master (name, description, status, category, created_by, created_time)
VALUES ('City Name', 'City Name ', 1, 'NEW', 'gsc', current_timestamp());

INSERT INTO mst_oms_attributes (name, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'Multi Macd Trunk Data Type',
              'Multi Macd Trunk Data Type ',
              1,
              'gsc',
              current_timestamp()) AS tmp
WHERE NOT EXISTS(
                SELECT name FROM mst_oms_attributes WHERE name = 'Multi Macd Trunk Data Type'
                )
LIMIT 1;


INSERT INTO mst_oms_attributes (name, description, is_active, created_by, created_time)
SELECT *
FROM (SELECT 'Taxation Questionnaire',
              'Taxation Questionnaire ',
              1,
              'gsc',
              current_timestamp()) AS tmp
WHERE NOT EXISTS(
                SELECT name FROM mst_oms_attributes WHERE name = 'Taxation Questionnaire'
                )
LIMIT 1;

COMMIT;
