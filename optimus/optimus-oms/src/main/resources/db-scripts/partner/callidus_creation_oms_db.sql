use customer_uat;

create table IF NOT EXISTS callidus_data
(
    id                                   int auto_increment
        primary key,
    partner_name                         varchar(30) null,
    partner_ID                           varchar(20) null,
    partner_profile                      varchar(10) null,
    partner_region                       varchar(10) null,
    segment                              varchar(10) null,
    oppty_ID                             varchar(10) null,
    circuit_ID                           varchar(20) null,
    COPFID                               varchar(20) null,
    CRNID                                varchar(20) null,
    SECSID                               varchar(20) null,
    billing_start_date                   varchar(20) null,
    billing_end_date                     varchar(20) null,
    sub_agent                            varchar(20) null,
    invoice_number                       varchar(20) null,
    opportunity_term                     int         null,
    OB_date                              varchar(20) null,
    commissioned_date                    varchar(20) null,
    contract_expiry_date                 varchar(20) null,
    commission_type                      varchar(30) null,
    base_commission_level_percentage     varchar(10) null,
    discount_level_commission_percentage varchar(10) null,
    multi_year_commission_percentage     varchar(10) null,
    deal_reg_percentage                  varchar(10) null,
    comp_date                            varchar(20) null,
    customer_name                        varchar(20) null,
    service_name                         varchar(10) null,
    tran_currency                        varchar(10) null,
    ACV_in_tran_currency                 double      null,
    billed_value_in_tran_currency        double      null,
    exchange_rate                        double      null,
    partner_base_currency                varchar(10) null,
    billed_value_in_base_currency        double      null,
    incentive_value_in_base_currency     double      null,
    remarks                              varchar(40) null,
    commissioned_percentage              varchar(20) null
);

create table sap_data
(
    id               int auto_increment
        primary key,
    partner_Id       int         not null,
    partner_name     varchar(40) null,
    month            varchar(5)  null,
    year             varchar(4)  null,
    commissions_paid double      null,
    currency         varchar(10) null
);

use oms_uat;

drop table callidus_data;
drop table sap_data;