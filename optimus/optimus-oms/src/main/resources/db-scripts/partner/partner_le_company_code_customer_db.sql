use customer_uat;

create table partner_legal_entity_company_code
(
    id int auto_increment primary key,
    partner_le_sap_code_id int                                          null,
    code_value             varchar(50) charset utf8                     null,
    is_active              enum ('Yes', 'No') charset utf8 default 'No' null,
    constraint FK_partner_le_company_code_partner_le_sap_code
        foreign key (partner_le_sap_code_id) references partner_legal_entity_sap_code (id)
);
