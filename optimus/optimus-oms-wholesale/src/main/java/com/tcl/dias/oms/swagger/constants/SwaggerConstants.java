package com.tcl.dias.oms.swagger.constants;

/**
 * This file contains the SwaggerConstants.java class. This class conatins
 * swagger constants
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SwaggerConstants {
    private SwaggerConstants() {
    }

    public static class ApiOperations {

        private ApiOperations() {

        }

        public static class Quotes {
            public static final String GET_LOCATION = "Get location details by given id";
            public static final String CREATE_DOCUMENT = "Create Document";
            public static final String CREATE_QUOTE = "create the quote";
            public static final String SHARE_QUOTE = "Share quote to the input email";
            public static final String TRIGGER_EMAIL = "Trigger Email to the delegated user";
            public static final String APPROVE_QUOTES = "Approve Sites";
            public static final String QUOTES_DOCUSIGN = "Docusign for quotes";
            public static final String TRIGGER_PRICING = "Trigger components for pricing engine";
            public static final String GET_CONTACT_ATTRIBUTE = "used to get contact attribute";
            public static final String CREATE_UPDATE_DELETE_ATTRIBUTES = "used to create/update/delete product component attributes";
            public static final String GET_SLA_DETAILS = "Get sla details";
            public static final String GET_OUTBOUND_DETAILS = "Get outbound details";
            public static final String GET_OUTBOUNG_SURCHARGE_PRICES = "Get outbound surcharge prices";
            public static final String TRIGGER_MAIL_GSC = "Trigger Mail Notification";
            public static final String MOVE_FILES_OBJECT_STORAGE = "Move files from file system to Object storage";

            public static final String UPDATE_QUOTE_LE_WITH_SERVICEIDS = "Update Quote to le with service ids";
            public static final String UPDATE_QUOTE_LE_ATTRIBUTES = "Update Quote to le attributes";

            private Quotes() {
                // DO NOTHING
            }

        }

        public static class INTERNALSTAGEVIEW {

            public static final String DOWNLOADEXCEL = "used to download excel for order summary";
            private INTERNALSTAGEVIEW() {

            }
            // NPL related constants - end


        }

        public static class LR {

            public static final String GET_ALL_ORDERS = "Get all unprocessed Lr";
            public static final String GET_NOTIFICATION = "Push from LR to notification Status";

            private LR() {

            }

        }

        public static class Orders {
            public static final String DASH_BOARD_DETAILS = "Get order dashboard details";
            public static final String DASH_BOARD_ORDER_DETAILS = "Get order details";
            public static final String UPDATE_QUOTE_LE_STATUS = "Update the status based on quote to le id";
            public static final String UPDATE_ORDER_LE_STATUS = "Update the status based on order to le id";
            public static final String UPDATE_ATTRIBUTES = "used to update product component attributes";
            public static final String GET_CONFGIGURATIONS = "used to get order gsc details";
            public static final String UPDATE_CONFIGURATIONS = "Update one or more order configurations";
            public static final String GET_ORDER_ATTRIBUTES = "used to get order related attributes";
            public static final String GET_CONFIGURATION_NUMBERS = "used to get numbers from number inventory API";
            public static final String RESERVE_CONFIGURATION_NUMBERS = "used to reserve numbers from number inventory API";
            public static final String GET_CONFIGURATION_DOCUMENTS = "Used to get documents applicable for this configuration";
            public static final String UPLOAD_CONFIGURATION_DOCUMENT = "Used to upload document specific to configuration";
            public static final String DOWNLOAD_CONFIGURATION_DOCUMENT = "Used to download document specific to configuration";
            public static final String DOWNLOAD_OUTBOUND_PRICES = "download outbound prices";
            public static final String GET_LNS_CONFIGURATION_NUMBERS = "used to get Lns city tfn numbers based on city code";
            public static final String UPDATE_TIGER_REQUEST = "update tiger request";

            private Orders() {
                // DO NOTHING
            }

        }

        public static class GSC {

            public static final String GET_GSC_QUOTE_SUMMARY = "used to get gsc specific quote summary";
            public static final String TRIGGER_PRICING = "used to get the price details for quote";
            public static final String GET_GSC_QUOTE_DETAILS = "used to get gsc specific quote details by id";
            public static final String GET_GSC_QUOTE_PDF = "used to get gsc quote pdf";
            public static final String GET_GSC_ORDER_DETAILS = "used to get gsc specific order details by id";
            public static final String GET_GSC_COF_PDF = "used to get gsc cof pdf";
            public static final String DELETE_GSC_QUOTE = "used to delete gsc quote";
            public static final String GET_GSC_ORDER_EXCEL_DOWNLOAD = "Download the GSC order excel sheet";
            public static final String GET_EMERGENCY_ADDRESS_DETAILS = "Getting emergency address details";
            public static final String GET_A_TO_Z_EXCEL_DOWNLOAD = "Download the A to Z GSC excel sheet";
            public static final String ORDER_ENRICHMENT_TEMPLATE_EXCEL = "Template Excel";
            public static final String ORDER_ENRICHMENT_BULK_UPLOAD_EXCEL = "Template Upload";

            private GSC() {

            }

            public static class MACD {
                public static final String PLACE_MACD_REQUEST = "place a MACD request";

                private MACD() {
                }
            }
        }

    }


}
