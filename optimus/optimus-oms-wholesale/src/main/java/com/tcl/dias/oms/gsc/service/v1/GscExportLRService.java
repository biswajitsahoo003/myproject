package com.tcl.dias.oms.gsc.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.gsc.beans.GscMultiMacdServiceBean;
import com.tcl.dias.common.gsc.beans.GscSlaBean;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.CustomFeasibilityRequest;
import com.tcl.dias.oms.beans.ExcelBean;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.PortBandwithConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.gsc.beans.GscCityNumber;
import com.tcl.dias.oms.gsc.beans.GscOrderBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tcl.dias.common.constants.CommonConstants.INDIA_INTERNATIONAL_SITES;
import static com.tcl.dias.common.constants.CommonConstants.INTERNATIONAL_SITES;
import static com.tcl.dias.common.constants.CommonConstants.QUOTE_SITE_TYPE;
import static com.tcl.dias.common.constants.LeAttributesConstants.MULTI_MACD_GSC_SERVICE_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ACCESS_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ADDITIONAL_IPS_MRC_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ARRANGED_BY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ATTRIBUTE_REQUIRED;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.AUTO_COF_NUMBER;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLABLE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_ADDRESS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_CATEGORY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_COMPONENT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_CURRENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_INCREMENT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_METHOD;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_RATIO;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BUY_DUAL_MANAGED_GVPN;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BUY_DUAL_UNMANAGED_GVPN;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BUY_SINGLE_MANAGED_GVPN;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BUY_SINGLE_UNMANAGED_GVPN;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CIRCUIT_UNIT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COMPONENTS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CONCURRENT_SESSIONS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CONTRACT_TERMS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS1;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS2;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS3;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS4;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS5;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS6;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_1;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_2;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_3;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_4;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_5;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_6;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_MODEL;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.COS_PROFILE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CPE_CONTRACT_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CPE_MANAGED;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CPE_MANAGED_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CPE_MRC_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CREDIT_LIMIT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUBE_LICENSES;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUID_VALUE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUSTOMER_CONTRACTING_BILLING_ADDRESS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUSTOMER_LE_GST;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUSTOMER_SECS_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.CUST_IP_ADDRESS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.DEPARTMENT_BILLING;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.DEPARTMENT_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.DESTINATION_COUNTRY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.DID_SITE_ADDRESS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_DOMESTIC_DOWNSTREAM_ORDER_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_CONFIGURATION_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_DETAIL_MRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_DETAIL_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_MRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_TCV;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_SUPPLIER_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSIP;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GST_ADDR;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GST_NO;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GST_NUM;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GVPN;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GVPNTYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GVPN_BACKUP;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GVPN_COMMON;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GVPN_FLAVOR;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.HAND_OFF;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.HAND_OFF_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.INTERCONNECT_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.INTERFACE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.INTERNET_PORT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.INVOICE_METHOD;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.IZOPrivate;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.JITTER_SERVICE_LEVEL_GSC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LASTMILE_MRC_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LAST_MILE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LATENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LAYER_MEDIUM;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LEAD_TIME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LE_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LE_STATE_GST_ADDRESS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LE_STATE_GST_NUMBER;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LINKTYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LM_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LOCATION;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.LR_SECTION;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.Le_Owner;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.MST_BILLING_RATIO;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.NA;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.NOT_APPLICABLE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.NOT_AVAILABLE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.OPPORTUNITY_CLASSIFICATION;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ORDER_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ORDER_REF_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ORDER_SECS_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ORDER_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PACKET_DROP_GSC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PAYMENT_METHOD;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PAYMENT_OPTIONS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PAYMENT_TERM_VALUE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PBX_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PORT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PORT_MODE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PORT_MRC_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.POST_DIAL_DELAY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PO_DATE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PO_DATE_KEY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PO_NUMBER;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PO_NUMBER_KEY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PRI_SEC_MAPPING;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PRODUCT_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PROGRAM_MANAGER;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PVDM_QUANTITIES;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.REMARKS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.REPORTING;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.REQUESTED_DATE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SECONDARY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SECONDARY_C;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SELL_TO;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SERVICE_AVAILABILITY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SERVICE_OPTION;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SFDC_ID;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SINGLE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SIP_CIRCUIT_UNIT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SIP_TRUNK_ATTRIBUTES;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SITE_ADDRESS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SITE_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SITE_GST;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SITE_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SLA;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SOURCE_COUNTRY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SWITCHING_UNIT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SWITCH_UNIT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.TAX_EXEMPTION;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.TCL;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.TOLL_FREE_NUMBERS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.TYPE_OF_CONNECTIVITY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.UIFN_NUMBER;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.UNIFR;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.VPN_PORT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.VPN_TOPOLOGY;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_CHANGE_OUTPULSE;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_NUMBER_REMOVE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.DOMESTIC_VOICE_SITE_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.INTERCONNECT_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.SELECTED_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACLNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ADDITIONAL_INFORMATION;
import static com.tcl.dias.oms.gsc.util.GscConstants.BEST_EFFORT;
import static com.tcl.dias.oms.gsc.util.GscConstants.CALLING_SERVICE_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CALLS_PER_SECOND;
import static com.tcl.dias.oms.gsc.util.GscConstants.CERTIFICATE_AUTHORITY_SUPPORT;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANGING_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANNEL_ARC;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANNEL_MRC;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANNEL_NRC;
import static com.tcl.dias.oms.gsc.util.GscConstants.CIRCT_GR_CD_RERT;
import static com.tcl.dias.oms.gsc.util.GscConstants.CIRCUIT_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.CITY_SELECTION;
import static com.tcl.dias.oms.gsc.util.GscConstants.CITY_WISE_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.CODEC;
import static com.tcl.dias.oms.gsc.util.GscConstants.CUSTOMER_DEVICE_IP;
import static com.tcl.dias.oms.gsc.util.GscConstants.CUSTOMER_PUBLIC_IP;
import static com.tcl.dias.oms.gsc.util.GscConstants.DEDICATED;
import static com.tcl.dias.oms.gsc.util.GscConstants.DELETE_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscConstants.DIAL_PLAN_LOGIC;
import static com.tcl.dias.oms.gsc.util.GscConstants.DID_ARC;
import static com.tcl.dias.oms.gsc.util.GscConstants.DID_MRC;
import static com.tcl.dias.oms.gsc.util.GscConstants.DID_NRC;
import static com.tcl.dias.oms.gsc.util.GscConstants.DTMF_RELAY_SUPPORT;
import static com.tcl.dias.oms.gsc.util.GscConstants.EMERGENCY_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscConstants.EQUIPMENT_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscConstants.FQDN;
import static com.tcl.dias.oms.gsc.util.GscConstants.GLOBAL_OUTBOUND_DYNAMIC_COLUMN;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.IP_ADDRESS_SPACE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ITFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.LIST_OF_NUMBERS_TO_BE_PORTED;
import static com.tcl.dias.oms.gsc.util.GscConstants.LNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.NNI_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.NO;
import static com.tcl.dias.oms.gsc.util.GscConstants.NO_OF_CONCURRENT_CHANNEL;
import static com.tcl.dias.oms.gsc.util.GscConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_SETUP_ARC;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_SETUP_MRC;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_SETUP_NRC;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;
import static com.tcl.dias.oms.gsc.util.GscConstants.PHONE_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.PREFIX_ADDITION;
import static com.tcl.dias.oms.gsc.util.GscConstants.PSTN;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_FIXED;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_MOBILE;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_SPECIAL;
import static com.tcl.dias.oms.gsc.util.GscConstants.REQUIRED_ON_A_AND_B_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscConstants.REQUIRED_PORTING_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ROUTING_TOPOLOGY;
import static com.tcl.dias.oms.gsc.util.GscConstants.SESSION_KEEP_ALIVE_TIMER;
import static com.tcl.dias.oms.gsc.util.GscConstants.SUPPORTED_SIP_PRIVACY_HEADERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.SURCHARGE_RATE;
import static com.tcl.dias.oms.gsc.util.GscConstants.SWTCH_UNIT_CD_RERT;
import static com.tcl.dias.oms.gsc.util.GscConstants.TERMINATION_NUMBER_ISD_CODE;
import static com.tcl.dias.oms.gsc.util.GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.TERM_NAME;
import static com.tcl.dias.oms.gsc.util.GscConstants.TERM_RATE;
import static com.tcl.dias.oms.gsc.util.GscConstants.TRANSPORT_PROTOCOL;
import static com.tcl.dias.oms.gsc.util.GscConstants.UIFN;
import static com.tcl.dias.oms.gsc.util.GscConstants.UIFN_REGISTRATION_CHARGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.YES;

/**
 * Services to handle export to LR  functionality
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscExportLRService {

    public static final String INTL = "INTL";
    private static final Logger LOGGER = LoggerFactory.getLogger(GscExportLRService.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;
    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;
    @Autowired
    OrderGscRepository orderGscRepository;
    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;
    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;
    @Autowired
    GscOrderDetailService gscOrderDetailService;
    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;
    @Autowired
    MstProductComponentRepository mstProductComponentRepository;
    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;
    @Autowired
    OrderIllSitesRepository orderIllSitesRepository;
    @Autowired
    OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;
    @Autowired
    OrderPriceRepository orderPriceRepository;
    @Autowired
    MQUtils mqUtils;
    @Autowired
    GscSlaService gscSlaService;
    @Value("${rabbitmq.location.address.request}")
    String apiAddressQueue;
    @Value("${rabbitmq.location.itcontact.request}")
    String locationItContactQueue;
    @Value("${rabbitmq.poplocation.detail}")
    String popQueue;
    @Value("${rabbitmq.mstaddress.detail}")
    String addressDetail;
    @Autowired
    GscOrderService gscOrderService;
    @Value("${rabbitmq.site.gst.queue}")
    String siteGstQueue;
    //    @Autowired
//    PartnerService partnerService;
    @Autowired
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;
    @Autowired
    AdditionalServiceParamRepository additionalServiceParamRepository;
    @Value("${rabbitmq.product.city.location.queue}")
    private String cityDetailsQueue;
    @Value("${rabbitmq.product.acans.city.location.queue}")
    private String acansCityDetailsQueue;
    private String portingNumbers = "";

    @Value("${rabbitmq.wholesale.customer.interconnect.name}")
    private String interconnectNameQueue;

    private static String getLeadTime(Timestamp confCreatedDate, String exptDeliveryDate) {
        String leadTime = null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        try {
            Date dateExpected = dateFormatter.parse(exptDeliveryDate);
            Date dateCreated = new Date(confCreatedDate.getTime());
            LocalDate dateBefore = dateCreated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateAfter = dateExpected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            leadTime = String.valueOf(ChronoUnit.DAYS.between(dateBefore, dateAfter));
        } catch (Exception ex) {
            LOGGER.info("Error in calculating Lead Time for GSC" + ex.getMessage());
            throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ex,
                    ResponseResource.R_CODE_ERROR);
        }
        return leadTime;
    }

    /**
     * returnExcel - method holding functionality to export order details as LR
     * Excel
     *
     * @param orderId
     * @param response
     * @throws IOException
     * @throws TclCommonException
     */
    public void returnExcel(Integer orderId, HttpServletResponse response) throws IOException, TclCommonException {

        Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
        Objects.requireNonNull(response, HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
        List<ExcelBean> listBook = getExcelList(orderId);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        sheet.setColumnWidth(50000, 50000);
        int rowCount = 0;

        for (ExcelBean aBook : listBook) {
            Row row = sheet.createRow(rowCount);
            writeBook(aBook, row);
            rowCount++;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        workbook.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();
        String fileName = "OrderDetails-" + orderId + ".xls";
        response.reset();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        workbook.close();
        try {
            FileCopyUtils.copy(outArray, response.getOutputStream());
        } catch (Exception e) {

        }

        outByteStream.flush();
        outByteStream.close();
    }

    /**
     * getExcelList - converts order into list of excel bean
     *
     * @param orderId
     * @return
     * @throws TclCommonException
     */
    public List<ExcelBean> getExcelList(Integer orderId) throws TclCommonException {

        Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
        List<ExcelBean> listBook = new ArrayList<>();
        Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
        Objects.requireNonNull(order, ORDER_NULL_MESSAGE);
        MstProductFamily gscProductFamily = mstProductFamilyRepository
                .findByNameAndStatus(GSIP, (byte) 1);
        OrderToLe gscOrderToLe = getOrderToLeBasedOnProductFamily(order, gscProductFamily);
        Map<String, String> attributeValues = getAttributeValues(gscOrderToLe);
        ExcelBean info = new ExcelBean(LR_SECTION,
                ATTRIBUTE_REQUIRED, REMARKS);
        info.setOrder(0);
        info.setGscQuoteId(0);
        listBook.add(info);
        boolean isIntl = false;
        boolean isGscMultiMacd = false;

        if (attributeValues.containsKey(QUOTE_SITE_TYPE)
                && (attributeValues.get(QUOTE_SITE_TYPE).equalsIgnoreCase(INTERNATIONAL_SITES) || attributeValues.get(QUOTE_SITE_TYPE).equalsIgnoreCase(INDIA_INTERNATIONAL_SITES))) {
            isIntl = true;
        }

        if (attributeValues.containsKey(LeAttributesConstants.IS_GSC_MULTI_MACD) && attributeValues.get(LeAttributesConstants.IS_GSC_MULTI_MACD).equalsIgnoreCase("Yes")) {
            isGscMultiMacd = true;
        }

        createOrderDetails(listBook, attributeValues, gscOrderToLe, isIntl, isGscMultiMacd);
        List<OrderGsc> orderGscList = getGscQuotes(gscOrderToLe);

        boolean finalIsGscMultiMacd = isGscMultiMacd;
        final String[] accessType = new String[1];
        orderGscList.stream().forEach(orderGsc -> {
            createQuoteGscDetails(listBook, orderGsc);

            List<OrderGscDetail> orderGscDetails = orderGscDetailRepository.findByorderGsc(orderGsc);
            orderGscDetails.stream().forEach(orderGscDetail -> {

                createQuoteGscConfigurationDetails(listBook, orderGscDetail);
                MstProductComponent component = mstProductComponentRepository.findByName(orderGsc.getProductName());
                MstProductComponent gscConfigComponent = mstProductComponentRepository.findByName(GSC_CONFIG_PRODUCT_COMPONENT_TYPE.toUpperCase());
                List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), component, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
                Optional<OrderProductComponent> orderProductComponent = orderProductComponents.stream().findFirst();
                List<OrderProductComponent> orderProductCongigComponents = orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), gscConfigComponent, GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
                Optional<OrderProductComponent> orderProductConfigComponent = orderProductCongigComponents.stream().findFirst();
                if (orderProductComponent.isPresent()) {
                    createQuoteGscConfigurationAttributesDetails(listBook, orderGscDetail, gscOrderToLe,
                            orderProductComponent.get(), orderProductConfigComponent, finalIsGscMultiMacd);
                }

                MstProductComponent accessTypeComponent = mstProductComponentRepository
                        .findByName(orderGsc.getAccessType());
                List<OrderProductComponent> accessTypeorderProductComponents = orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), accessTypeComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE);
                Optional<OrderProductComponent> accessTypeOrderProductComponent = accessTypeorderProductComponents
                        .stream().findFirst();
                if (accessTypeOrderProductComponent.isPresent()) {
                    accessType[0] = String.valueOf(accessTypeOrderProductComponent.get());
                    getSipAttributes(listBook, accessTypeOrderProductComponent.get(), gscOrderToLe, finalIsGscMultiMacd);
                }

            });
        });

        if (isGscMultiMacd) {
            getSIPTrunkAttributes(listBook, gscOrderToLe, accessType[0]);
            getInterconnectAttributes(listBook, gscOrderToLe, accessType[0]);
        }
        createBillingDetails(listBook, attributeValues, gscOrderToLe);
        return listBook;
    }

    /**
     * Method to get orderToLe based on product family
     *
     * @param order
     * @param mstProductFamily
     * @return
     */
    private OrderToLe getOrderToLeBasedOnProductFamily(Order order, MstProductFamily mstProductFamily) {
        OrderToLe orderToLeResult = null;
        for (OrderToLe orderToLe : order.getOrderToLes()) {
            if (mstProductFamily != null) {
                OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
                        .findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
                if (Objects.nonNull(orderToLeProductFamily)) {
                    orderToLeResult = orderToLeProductFamily.getOrderToLe();
                    break;
                }
            }

        }
        return orderToLeResult;
    }

    /**
     * createBillingDetails
     *
     * @param listBook
     * @param attributeValues
     * @param orderToLe
     */
    private void createBillingDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
                                      OrderToLe orderToLe) {
        ExcelBean billingMethod = new ExcelBean(BILLING_COMPONENT, BILLING_METHOD,
                attributeValues.containsKey(BILLING_METHOD) ? attributeValues.get(BILLING_METHOD) : "");
        billingMethod.setOrder(7);
        billingMethod.setGscQuoteId(0);
        listBook.add(billingMethod);

        ExcelBean billingCurrency = new ExcelBean(BILLING_COMPONENT, BILLING_CURRENCY,
                attributeValues.containsKey(BILLING_CURRENCY) ? attributeValues.get(BILLING_CURRENCY) : "");
        billingCurrency.setOrder(7);
        billingCurrency.setGscQuoteId(0);
        listBook.add(billingCurrency);

        ExcelBean billingIncrement = new ExcelBean(BILLING_COMPONENT,
                BILLING_INCREMENT,
                attributeValues.containsKey(BILLING_INCREMENT) ? attributeValues.get(BILLING_INCREMENT) : "");
        billingIncrement.setOrder(7);
        billingIncrement.setGscQuoteId(0);
        listBook.add(billingIncrement);

        ExcelBean billingType = new ExcelBean(BILLING_COMPONENT,
                BILLING_TYPE,
                attributeValues.containsKey(BILLING_TYPE) ? attributeValues.get(BILLING_TYPE) : "");
        billingType.setOrder(7);
        billingType.setGscQuoteId(0);
        listBook.add(billingType);

        ExcelBean billingFrequency = new ExcelBean(BILLING_COMPONENT,
                BILLING_FREQUENCY,
                attributeValues.containsKey(BILLING_FREQUENCY) ? attributeValues.get(BILLING_FREQUENCY) : "");
        billingFrequency.setOrder(1);
        billingFrequency.setGscQuoteId(0);
        listBook.add(billingFrequency);

    }

    /**
     * Method to create quote gsc details
     *
     * @param listBook
     * @param gscQuote
     */
    private void createQuoteGscDetails(List<ExcelBean> listBook, OrderGsc gscQuote) {
        ExcelBean book17 = new ExcelBean(GSC_QUOTE_DETAILS, PRODUCT_NAME, gscQuote.getProductName());
        book17.setOrder(1);
        book17.setGscQuoteId(gscQuote.getId());
        listBook.add(book17);

        ExcelBean book18 = new ExcelBean(GSC_QUOTE_DETAILS, ACCESS_TYPE, gscQuote.getAccessType());
        book18.setOrder(1);
        book18.setGscQuoteId(gscQuote.getId());
        listBook.add(book18);

        String mrc = "0.0";
        if (Objects.nonNull(gscQuote.getMrc())) {
            mrc = String.format("%.2f", gscQuote.getMrc());
        }

        ExcelBean book19 = new ExcelBean(GSC_QUOTE_DETAILS, GSC_QUOTE_MRC, mrc);
        book19.setOrder(1);
        book19.setGscQuoteId(gscQuote.getId());
        listBook.add(book19);

        String nrc = "0.0";
        if (Objects.nonNull(gscQuote.getNrc())) {
            nrc = String.format("%.2f", gscQuote.getNrc());
        }
        ExcelBean book20 = new ExcelBean(GSC_QUOTE_DETAILS, GSC_QUOTE_NRC, nrc);
        book20.setOrder(1);
        book20.setGscQuoteId(gscQuote.getId());
        listBook.add(book20);

        String tcv = "0.0";
        if (Objects.nonNull(gscQuote.getTcv())) {
            tcv = String.format("%.2f", gscQuote.getTcv());
        }
        ExcelBean book21 = new ExcelBean(GSC_QUOTE_DETAILS, GSC_QUOTE_TCV, tcv);
        book21.setOrder(1);
        book21.setGscQuoteId(gscQuote.getId());
        listBook.add(book21);
    }

    private void createQuoteGscConfigurationDetails(List<ExcelBean> listBook, OrderGscDetail orderGscDetail) {
        ExcelBean book22 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, SOURCE_COUNTRY, orderGscDetail.getSrc());
        book22.setOrder(2);
        book22.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book22);

        ExcelBean book23 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, DESTINATION_COUNTRY, orderGscDetail.getDest());
        book23.setOrder(2);
        book23.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book23);

        String mrc = "0.0";
        if (Objects.nonNull(orderGscDetail.getMrc())) {
            mrc = String.format("%.2f", orderGscDetail.getMrc());
        }
        ExcelBean book24 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, GSC_QUOTE_DETAIL_MRC, mrc);
        book24.setOrder(2);
        book24.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book24);

        String nrc = "0.0";
        if (Objects.nonNull(orderGscDetail.getNrc())) {
            nrc = String.format("%.2f", orderGscDetail.getNrc());
        }
        ExcelBean book25 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, GSC_QUOTE_DETAIL_NRC, nrc);
        book25.setOrder(2);
        book25.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book25);
    }

    /**
     * Method to create quotegsc configuration details
     *
     * @param listBook
     * @param orderProductComponent
     */
    private void createQuoteGscConfigurationAttributesDetails(List<ExcelBean> listBook, OrderGscDetail orderGscDetail, OrderToLe orderToLe, OrderProductComponent orderProductComponent, Optional<OrderProductComponent> orderProductGscConfigComponent, boolean isGscMultiMacd) {

        Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes = orderProductComponent.getOrderProductComponentsAttributeValues();
        List<String> attributesList = Arrays.asList(RATE_PER_MINUTE_FIXED, RATE_PER_MINUTE_MOBILE, RATE_PER_MINUTE_SPECIAL,
                TERM_NAME, DID_ARC, DID_MRC, DID_NRC, CHANNEL_ARC, CHANNEL_MRC,
                CHANNEL_NRC, ORDER_SETUP_ARC, ORDER_SETUP_MRC, ORDER_SETUP_NRC, TERM_RATE,
                PHONE_TYPE, SURCHARGE_RATE, UIFN_REGISTRATION_CHARGE, GLOBAL_OUTBOUND_DYNAMIC_COLUMN,
                LIST_OF_NUMBERS_TO_BE_PORTED, TERMINATION_NUMBER_WORKING_OUTPULSE, CITY_SELECTION, EMERGENCY_ADDRESS,
                CALLING_SERVICE_TYPE, QUANTITY_OF_NUMBERS, CITY_WISE_QUANTITY_OF_NUMBERS, GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE);

        List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
        Map<String, Integer> attributesMap = getAttributesMap(attributes);
        LOGGER.info("Access Type:" + orderGscDetail.getOrderGsc().getAccessType());
        Try<GscOrderDataBean> gscOrderDataBean = gscOrderService.getGscOrderById(orderToLe.getOrder().getId());
        Set<OrderProductComponentsAttributeValue> orderProductGscConfigComponentAttributes = null;
        if (orderProductGscConfigComponent.isPresent()) {
            orderProductGscConfigComponentAttributes = orderProductGscConfigComponent.get().getOrderProductComponentsAttributeValues();
        }
        switch (orderProductComponent.getMstProductComponent().getName()) {

            case ITFS: {

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && !isGscMultiMacd && (orderToLe.getOrderCategory().equalsIgnoreCase(DELETE_NUMBER) || orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                Integer rpmFixedId = attributesMap.get(RATE_PER_MINUTE_FIXED);
                ExcelBean book26 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_FIXED,
                        getAttributeValue(rpmFixedId, orderProductComponentAttributes));
                book26.setOrder(3);
                book26.setGscQuoteDetailId(0);
                listBook.add(book26);

                Integer rpmMobileId = attributesMap.get(RATE_PER_MINUTE_MOBILE);
                ExcelBean book27 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_MOBILE,
                        getAttributeValue(rpmMobileId, orderProductComponentAttributes));
                book27.setOrder(3);
                book27.setGscQuoteDetailId(0);
                listBook.add(book27);

                Integer rpmSpecialId = attributesMap.get(RATE_PER_MINUTE_SPECIAL);
                ExcelBean book28 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_SPECIAL,
                        getAttributeValue(rpmSpecialId, orderProductComponentAttributes));
                book28.setOrder(3);
                book28.setGscQuoteDetailId(0);
                listBook.add(book28);

                Integer qnId = attributesMap.get(QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);

                Integer portedId = attributesMap.get(LIST_OF_NUMBERS_TO_BE_PORTED);
                ExcelBean portBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        LIST_OF_NUMBERS_TO_BE_PORTED, getAttributeValue(portedId, orderProductComponentAttributes));
                portBook.setOrder(3);
                portBook.setGscQuoteDetailId(0);
                listBook.add(portBook);
                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> ITFS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if (Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                break;
            }
            case LNS: {

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && !isGscMultiMacd
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                List<GscCityNumber> cityDetails = gscOrderDetailService.getCityNumberConfiguration(orderGscDetail.getId(),
                        LNS);
                Map<String, String> citiesWithCode = queueForLNSCityNamesCall();

                List<GscTfnBean> cityWithNpa = gscOrderDetailService.getCityNpaConfigurationList(orderGscDetail.getId(),
                        GscConstants.LNS);
                LOGGER.info("No of npa cities is {} ", cityWithNpa.size());

                cityDetails.stream().forEach(gscCityNumber -> {
                    final boolean[] portingFlag = {false};
                    ExcelBean cityBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            CITY_SELECTION, citiesWithCode.get(gscCityNumber.getOriginCity()));
                    cityBook.setOrder(3);
                    cityBook.setGscQuoteDetailId(0);
                    listBook.add(cityBook);
                    gscCityNumber.getLnsPortings().forEach(gscLNSPortingNumber -> {

                        String isPortingRequired = getIsPortingRequired(gscLNSPortingNumber.getIsPortingRequired());

                        if (YES.equalsIgnoreCase(isPortingRequired)) {
                            ExcelBean portingRequiredBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                    "Porting Required", isPortingRequired);
                            portingRequiredBook.setOrder(3);
                            portingRequiredBook.setGscQuoteDetailId(0);
                            listBook.add(portingRequiredBook);
                            portingFlag[0] = true;

                            ExcelBean totalPortingNumbersBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                    "Total Porting Numbers", gscLNSPortingNumber.getTotalNumbers().toString());
                            totalPortingNumbersBook.setOrder(3);
                            totalPortingNumbersBook.setGscQuoteDetailId(0);
                            listBook.add(totalPortingNumbersBook);

                            gscLNSPortingNumber.getTfnBeans().stream().forEach(gscTfnBean -> {
                                portingNumbers = gscTfnBean.getNumber() + ", " + portingNumbers;
                            });

                            ExcelBean portingNumbersBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                    "All Porting Numbers", portingNumbers);
                            portingNumbersBook.setOrder(3);
                            portingNumbersBook.setGscQuoteDetailId(0);
                            listBook.add(portingNumbersBook);
                        } else {
                            ExcelBean howManyPortingNumbers = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                    "New Numbers", gscLNSPortingNumber.getTotalNumbers().toString());
                            howManyPortingNumbers.setOrder(3);
                            howManyPortingNumbers.setGscQuoteDetailId(0);
                            listBook.add(howManyPortingNumbers);
                        }
                        portingNumbers = "";

                        cityWithNpa.stream().map(gscTfnBean -> {
                            LOGGER.info("city for npa {}", gscTfnBean.getCity());
                            LOGGER.info("origin city {}", gscCityNumber.getOriginCity());
                            LOGGER.info("origin city with code {}", citiesWithCode.get(gscCityNumber.getOriginCity()));
                            return gscTfnBean;
                        }).filter(gscTfnBean -> gscTfnBean.getCity().equalsIgnoreCase(gscCityNumber.getOriginCity())).findFirst().ifPresent(gscTfnBean -> {
                            LOGGER.info("gsctfn city code {} and npa {} ", gscTfnBean.getCity(), gscTfnBean.getNpa());
                            ExcelBean howManyPortingNumbers = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                    "NPA", gscTfnBean.getNpa());
                            howManyPortingNumbers.setOrder(3);
                            howManyPortingNumbers.setGscQuoteDetailId(0);
                            listBook.add(howManyPortingNumbers);
                            cityWithNpa.remove(gscTfnBean);
                        });
                    });
                    if (!portingFlag[0]) {
                        ExcelBean portingRequiredBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                "Porting Required", NO);
                        portingRequiredBook.setOrder(3);
                        portingRequiredBook.setGscQuoteDetailId(0);
                        listBook.add(portingRequiredBook);
                    }
                });


                Integer rpmFixedId = attributesMap.get(RATE_PER_MINUTE_FIXED);
                ExcelBean book29 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_FIXED,
                        getAttributeValue(rpmFixedId, orderProductComponentAttributes));
                book29.setOrder(3);
                book29.setGscQuoteDetailId(0);
                listBook.add(book29);

                Integer rpmMobileId = attributesMap.get(RATE_PER_MINUTE_MOBILE);
                ExcelBean book30 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_MOBILE,
                        getAttributeValue(rpmMobileId, orderProductComponentAttributes));
                book30.setOrder(3);
                book30.setGscQuoteDetailId(0);
                listBook.add(book30);

                Integer rpmSpecialId = attributesMap.get(RATE_PER_MINUTE_SPECIAL);
                ExcelBean book31 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_SPECIAL,
                        getAttributeValue(rpmSpecialId, orderProductComponentAttributes));
                book31.setOrder(3);
                book31.setGscQuoteDetailId(0);
                listBook.add(book31);

                Integer qnId = attributesMap.get(QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);

                Integer portedId = attributesMap.get(LIST_OF_NUMBERS_TO_BE_PORTED);
                ExcelBean portBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        LIST_OF_NUMBERS_TO_BE_PORTED, getAttributeValue(portedId, orderProductComponentAttributes));
                portBook.setOrder(3);
                portBook.setGscQuoteDetailId(0);
                listBook.add(portBook);

                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> LNS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if (Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }
                break;
            }

            case UIFN: {

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && !isGscMultiMacd
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);

                }

                Integer rpmFixedId = attributesMap.get(RATE_PER_MINUTE_FIXED);
                ExcelBean book32 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_FIXED,
                        getAttributeValue(rpmFixedId, orderProductComponentAttributes));
                book32.setOrder(3);
                book32.setGscQuoteDetailId(0);
                listBook.add(book32);

                Integer rpmMobileId = attributesMap.get(RATE_PER_MINUTE_MOBILE);
                ExcelBean book33 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_MOBILE,
                        getAttributeValue(rpmMobileId, orderProductComponentAttributes));
                book33.setOrder(3);
                book33.setGscQuoteDetailId(0);
                listBook.add(book33);

                Integer rpmSpecialId = attributesMap.get(RATE_PER_MINUTE_SPECIAL);
                ExcelBean book34 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_SPECIAL,
                        getAttributeValue(rpmSpecialId, orderProductComponentAttributes));
                book34.setOrder(3);
                book34.setGscQuoteDetailId(0);
                listBook.add(book34);


                Integer uifnRegChargeId = attributesMap.get(UIFN_REGISTRATION_CHARGE);
                String charge = getAttributeValue(uifnRegChargeId, orderProductComponentAttributes);
                if (charge.isEmpty())
                    charge = "0";
                ExcelBean uifnRegChargeBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        UIFN_REGISTRATION_CHARGE,
                        String.format("%.2f", Double.parseDouble(charge)));
                uifnRegChargeBook.setOrder(3);
                uifnRegChargeBook.setGscQuoteDetailId(0);
                listBook.add(uifnRegChargeBook);

                Integer qnId = attributesMap.get(QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);

                Integer portedId = attributesMap.get(LIST_OF_NUMBERS_TO_BE_PORTED);
                ExcelBean portBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        LIST_OF_NUMBERS_TO_BE_PORTED, getAttributeValue(portedId, orderProductComponentAttributes));
                portBook.setOrder(3);
                portBook.setGscQuoteDetailId(0);
                listBook.add(portBook);


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> UIFN.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if (Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                break;
            }
            case ACANS: {

                List<OrderProductComponentsAttributeValue> cityWiseNumberAttributes = getComponentAttributesBasedOnSpecificAttribute(
                        attributesMap.get(CITY_WISE_QUANTITY_OF_NUMBERS), orderProductComponentAttributes);

                cityWiseNumberAttributes.stream().forEach(orderProductComponentsAttributeValue -> {

                    List<String> numberValues = new LinkedList<>(Arrays.asList(orderProductComponentsAttributeValue.getAttributeValues().split(":")));

                    ExcelBean cityNameValue = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            CITY_SELECTION, numberValues.get(0).split("_")[1]);
                    cityNameValue.setOrder(3);
                    cityNameValue.setGscQuoteDetailId(0);
                    listBook.add(cityNameValue);

                    ExcelBean cityQuantityOfNumbers = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            CITY_WISE_QUANTITY_OF_NUMBERS, String.valueOf(numberValues.size() - 1));
                    cityQuantityOfNumbers.setOrder(3);
                    cityQuantityOfNumbers.setGscQuoteDetailId(0);
                    listBook.add(cityQuantityOfNumbers);
                });

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD)
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                Integer rpmFixedId = attributesMap.get(RATE_PER_MINUTE_FIXED);
                ExcelBean book35 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_FIXED,
                        getAttributeValue(rpmFixedId, orderProductComponentAttributes));
                book35.setOrder(3);
                book35.setGscQuoteDetailId(0);
                listBook.add(book35);

                Integer rpmMobileId = attributesMap.get(RATE_PER_MINUTE_MOBILE);
                ExcelBean book36 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_MOBILE,
                        getAttributeValue(rpmMobileId, orderProductComponentAttributes));
                book36.setOrder(3);
                book36.setGscQuoteDetailId(0);
                listBook.add(book36);

                Integer rpmSpecialId = attributesMap.get(RATE_PER_MINUTE_SPECIAL);
                ExcelBean book37 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_SPECIAL,
                        getAttributeValue(rpmSpecialId, orderProductComponentAttributes));
                book37.setOrder(3);
                book37.setGscQuoteDetailId(0);
                listBook.add(book37);

                Integer qnId = attributesMap.get(QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> ACANS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if (Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                break;
            }
            case ACLNS: {

                List<OrderProductComponentsAttributeValue> cityWiseNumberAttributes = getComponentAttributesBasedOnSpecificAttribute(
                        attributesMap.get(CITY_WISE_QUANTITY_OF_NUMBERS), orderProductComponentAttributes);

                cityWiseNumberAttributes.stream().forEach(orderProductComponentsAttributeValue -> {

                    List<String> numberValues = new LinkedList<>(Arrays.asList(orderProductComponentsAttributeValue.getAttributeValues().split(":")));

                    ExcelBean cityNameValue = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            CITY_SELECTION, numberValues.get(0).split("_")[1]);
                    cityNameValue.setOrder(3);
                    cityNameValue.setGscQuoteDetailId(0);
                    listBook.add(cityNameValue);

                    ExcelBean cityQuantityOfNumbers = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            CITY_WISE_QUANTITY_OF_NUMBERS, String.valueOf(numberValues.size() - 1));
                    cityQuantityOfNumbers.setOrder(3);
                    cityQuantityOfNumbers.setGscQuoteDetailId(0);
                    listBook.add(cityQuantityOfNumbers);
                });

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD)
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                Integer rpmFixedId = attributesMap.get(RATE_PER_MINUTE_FIXED);
                ExcelBean book35 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_FIXED,
                        getAttributeValue(rpmFixedId, orderProductComponentAttributes));
                book35.setOrder(3);
                book35.setGscQuoteDetailId(0);
                listBook.add(book35);

                Integer rpmMobileId = attributesMap.get(RATE_PER_MINUTE_MOBILE);
                ExcelBean book36 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_MOBILE,
                        getAttributeValue(rpmMobileId, orderProductComponentAttributes));
                book36.setOrder(3);
                book36.setGscQuoteDetailId(0);
                listBook.add(book36);

                Integer rpmSpecialId = attributesMap.get(RATE_PER_MINUTE_SPECIAL);
                ExcelBean book37 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_SPECIAL,
                        getAttributeValue(rpmSpecialId, orderProductComponentAttributes));
                book37.setOrder(3);
                book37.setGscQuoteDetailId(0);
                listBook.add(book37);

                Integer qnId = attributesMap.get(QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> ACLNS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if (Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                break;
            }
            case ACDTFS: {

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && !isGscMultiMacd
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                Integer rpmFixedId = attributesMap.get(RATE_PER_MINUTE_FIXED);
                ExcelBean book38 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_FIXED,
                        getAttributeValue(rpmFixedId, orderProductComponentAttributes));
                book38.setOrder(3);
                book38.setGscQuoteDetailId(0);
                listBook.add(book38);

                Integer rpmMobileId = attributesMap.get(RATE_PER_MINUTE_MOBILE);
                ExcelBean book39 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_MOBILE,
                        getAttributeValue(rpmMobileId, orderProductComponentAttributes));
                book39.setOrder(3);
                book39.setGscQuoteDetailId(0);
                listBook.add(book39);

                Integer rpmSpecialId = attributesMap.get(RATE_PER_MINUTE_SPECIAL);
                ExcelBean book40 = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        RATE_PER_MINUTE_SPECIAL,
                        getAttributeValue(rpmSpecialId, orderProductComponentAttributes));
                book40.setOrder(3);
                book40.setGscQuoteDetailId(0);
                listBook.add(book40);

                if (orderGscDetail.getOrderGsc().getAccessType().equalsIgnoreCase(PSTN) ||
                        orderGscDetail.getOrderGsc().getAccessType().equalsIgnoreCase(DEDICATED)) {
                    Integer qnId = attributesMap.get(QUANTITY_OF_NUMBERS);
                    ExcelBean qnBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            QUANTITY_OF_NUMBERS,
                            getAttributeValue(qnId, orderProductComponentAttributes));
                    qnBook.setOrder(3);
                    qnBook.setGscQuoteDetailId(0);
                    listBook.add(qnBook);
                }


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> ACDTFS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if (Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                                LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                break;
            }

            default:

                LOGGER.info("Invalid master product component Name");

        }

    }

    private String getIsPortingRequired(Byte value) {
        if (1 == value.intValue()) {
            return YES;
        }
        return NO;
    }

    /**
     * Method to get tfn Numbers
     *
     * @param orderGscDetail
     * @return
     */
    private String getTfnNumbers(OrderGscDetail orderGscDetail) {
        StringBuilder tfnNumbersList = new StringBuilder();
        try {
            List<GscTfnBean> tfnNumbers = gscOrderDetailService.getNumbersForConfiguration(orderGscDetail, null, 10,
                    false);
            if (Objects.nonNull(tfnNumbers)) {
                tfnNumbers.stream().forEach(tfnNumber -> {
                    tfnNumbersList.append(tfnNumber.getNumber()).append(CommonConstants.COMMA);

                });
            }
        } catch (Exception e) {
            LOGGER.error("error in processing tfnNumbers", e);
        }
        String numbers = tfnNumbersList.toString();
        if (numbers.length() > 1) {
            numbers = tfnNumbersList.toString().substring(0, tfnNumbersList.toString().length() - 1);

        } else
            numbers = "";
        return numbers;

    }

    /**
     * Method to get SIP attributes
     *
     * @param listBook
     * @param orderProductComponent
     * @param isGscMultiMacd
     */
    private void getSipAttributes(List<ExcelBean> listBook, OrderProductComponent orderProductComponent, OrderToLe orderToLe, boolean isGscMultiMacd) {

        Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes = orderProductComponent
                .getOrderProductComponentsAttributeValues();
        List<String> attributesList = Arrays.asList(REQUIRED_ON_A_AND_B_NUMBER,
                DTMF_RELAY_SUPPORT, SUPPORTED_SIP_PRIVACY_HEADERS,
                SESSION_KEEP_ALIVE_TIMER, PREFIX_ADDITION, CUSTOMER_PUBLIC_IP,
                TRANSPORT_PROTOCOL, CODEC, NO_OF_CONCURRENT_CHANNEL,
                EQUIPMENT_ADDRESS, ROUTING_TOPOLOGY, DIAL_PLAN_LOGIC,
                CALLS_PER_SECOND, CERTIFICATE_AUTHORITY_SUPPORT, FQDN,
                IP_ADDRESS_SPACE, CUSTOMER_DEVICE_IP, ADDITIONAL_INFORMATION,
                TERMINATION_NUMBER_WORKING_OUTPULSE, QUANTITY_OF_NUMBERS,
                LIST_OF_NUMBERS_TO_BE_PORTED, OLD_TERMINATION_NUMBER_WORKING_OUTPULSE,
                TERMINATION_NUMBER_ISD_CODE, REQUIRED_PORTING_NUMBERS,
                SELECTED_TERMINATION_NUMBER_OUTPULSE,
                SWTCH_UNIT_CD_RERT, CIRCT_GR_CD_RERT, CIRCUIT_ID, NNI_ID,
                DOMESTIC_VOICE_SITE_ADDRESS);
        List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
        Map<String, Integer> attributesMap = getAttributesMap(attributes);

        switch (orderProductComponent.getMstProductComponent().getName()) {
            case PSTN: {

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE)) {
                    Integer oldTermId = attributesMap.get(OLD_TERMINATION_NUMBER_WORKING_OUTPULSE);

                    ExcelBean oldTermNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            OLD_TERMINATION_NUMBER_WORKING_OUTPULSE, appendTerminationCountryISDCode(oldTermId, orderProductComponentAttributes, attributesMap));
                    oldTermNumBook.setOrder(3);
                    oldTermNumBook.setGscQuoteDetailId(0);
                    listBook.add(oldTermNumBook);
                }
                Integer termId = attributesMap.get(TERMINATION_NUMBER_WORKING_OUTPULSE);
                ExcelBean termNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        TERMINATION_NUMBER_WORKING_OUTPULSE, appendTerminationCountryISDCode(termId, orderProductComponentAttributes, attributesMap));
                termNumBook.setOrder(3);
                termNumBook.setGscQuoteDetailId(0);
                listBook.add(termNumBook);

                //showing MACD Add country selected outpulse number in LR export
                getSelectedOutpulseNumber(listBook, attributesMap, orderProductComponentAttributes);

                String requiredPortingNumbers = getAttributeValue(attributesMap.get(REQUIRED_PORTING_NUMBERS), orderProductComponentAttributes);
                if (StringUtils.isNoneBlank(requiredPortingNumbers) && (!Pattern.compile("^[,]").matcher(requiredPortingNumbers).find())) {
                    ExcelBean portingBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            REQUIRED_PORTING_NUMBERS, requiredPortingNumbers);
                    portingBook.setOrder(3);
                    portingBook.setGscQuoteDetailId(0);
                    listBook.add(portingBook);
                }

            }
            break;

            case DEDICATED: {

                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && orderToLe.getOrderCategory().equalsIgnoreCase(CHANGING_OUTPULSE)) {
                    Integer oldTermId = attributesMap.get(OLD_TERMINATION_NUMBER_WORKING_OUTPULSE);
                    ExcelBean oldTermNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            OLD_TERMINATION_NUMBER_WORKING_OUTPULSE, getAttributeValue(oldTermId, orderProductComponentAttributes));
                    oldTermNumBook.setOrder(3);
                    oldTermNumBook.setGscQuoteDetailId(0);
                    listBook.add(oldTermNumBook);
                }
               /* //show MACD service attribute
                if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD)) {
                    getMACDServiceAttributes(listBook,attributesMap,orderProductComponentAttributes);
                }*/
                Integer termId = attributesMap.get(TERMINATION_NUMBER_WORKING_OUTPULSE);
                ExcelBean termNumBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                        TERMINATION_NUMBER_WORKING_OUTPULSE, getAttributeValue(termId, orderProductComponentAttributes));
                termNumBook.setOrder(3);
                termNumBook.setGscQuoteDetailId(0);
                listBook.add(termNumBook);

                //showing MACD Add country selected outpulse number in LR export
                getSelectedOutpulseNumber(listBook, attributesMap, orderProductComponentAttributes);

                getDIDSiteAddress(listBook, attributesMap, orderProductComponentAttributes);
               /* if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && isGscMultiMacd) {
                    getSIPTrunkAttributes(listBook, orderToLe, orderProductComponent.getMstProductComponent().getName());
                }*/

                Integer requiredId = attributesMap.get(REQUIRED_ON_A_AND_B_NUMBER);
                ExcelBean book26 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        REQUIRED_ON_A_AND_B_NUMBER,
                        getAttributeValue(requiredId, orderProductComponentAttributes));
                book26.setOrder(3);
                book26.setGscQuoteDetailId(0);
                listBook.add(book26);

                Integer dtmfId = attributesMap.get(DTMF_RELAY_SUPPORT);
                ExcelBean book27 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        DTMF_RELAY_SUPPORT,
                        getAttributeValue(dtmfId, orderProductComponentAttributes));
                book27.setOrder(3);
                book27.setGscQuoteDetailId(0);
                listBook.add(book27);

                Integer privacyHeaderId = attributesMap.get(SUPPORTED_SIP_PRIVACY_HEADERS);
                ExcelBean book28 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        SUPPORTED_SIP_PRIVACY_HEADERS,
                        getAttributeValue(privacyHeaderId, orderProductComponentAttributes));
                book28.setOrder(3);
                book28.setGscQuoteDetailId(0);
                listBook.add(book28);

                Integer sessionAliveId = attributesMap.get(SESSION_KEEP_ALIVE_TIMER);
                ExcelBean book29 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        SESSION_KEEP_ALIVE_TIMER,
                        getAttributeValue(sessionAliveId, orderProductComponentAttributes));
                book29.setOrder(3);
                book29.setGscQuoteDetailId(0);
                listBook.add(book29);

                Integer prefixAdId = attributesMap.get(PREFIX_ADDITION);
                ExcelBean book30 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        PREFIX_ADDITION,
                        getAttributeValue(prefixAdId, orderProductComponentAttributes));
                book30.setOrder(3);
                book30.setGscQuoteDetailId(0);
                listBook.add(book30);

                Integer customerPublicId = attributesMap.get(CUSTOMER_PUBLIC_IP);
                ExcelBean book31 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        CUSTOMER_PUBLIC_IP,
                        getAttributeValue(customerPublicId, orderProductComponentAttributes));
                book31.setOrder(3);
                book31.setGscQuoteDetailId(0);
                listBook.add(book31);

                Integer transportId = attributesMap.get(TRANSPORT_PROTOCOL);
                ExcelBean book32 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        TRANSPORT_PROTOCOL,
                        getAttributeValue(transportId, orderProductComponentAttributes));
                book32.setOrder(3);
                book32.setGscQuoteDetailId(0);
                listBook.add(book32);

                Integer codecId = attributesMap.get(CODEC);
                String codecValue = getAttributeValue(codecId, orderProductComponentAttributes);
                if (!StringUtils.isAllBlank(codecValue)) {
                    ExcelBean book33 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                            CODEC,
                            codecValue);
                    book33.setOrder(3);
                    book33.setGscQuoteDetailId(0);
                    listBook.add(book33);
                }

                Integer channelId = attributesMap.get(NO_OF_CONCURRENT_CHANNEL);
                ExcelBean book34 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        NO_OF_CONCURRENT_CHANNEL,
                        getAttributeValue(channelId, orderProductComponentAttributes));
                book34.setOrder(3);
                book34.setGscQuoteDetailId(0);
                listBook.add(book34);

                Integer equipmentId = attributesMap.get(EQUIPMENT_ADDRESS);
                ExcelBean book35 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        EQUIPMENT_ADDRESS,
                        getAttributeValue(equipmentId, orderProductComponentAttributes));
                book35.setOrder(3);
                book35.setGscQuoteDetailId(0);
                listBook.add(book35);

                Integer routingId = attributesMap.get(ROUTING_TOPOLOGY);
                ExcelBean book36 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        ROUTING_TOPOLOGY,
                        getAttributeValue(routingId, orderProductComponentAttributes));
                book36.setOrder(3);
                book36.setGscQuoteDetailId(0);
                listBook.add(book36);

                Integer dialPlanId = attributesMap.get(DIAL_PLAN_LOGIC);
                ExcelBean book37 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        DIAL_PLAN_LOGIC,
                        getAttributeValue(dialPlanId, orderProductComponentAttributes));
                book37.setOrder(3);
                book37.setGscQuoteDetailId(0);
                listBook.add(book37);

                Integer callsId = attributesMap.get(CALLS_PER_SECOND);
                ExcelBean book38 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        CALLS_PER_SECOND,
                        getAttributeValue(callsId, orderProductComponentAttributes));
                book38.setOrder(3);
                book38.setGscQuoteDetailId(0);
                listBook.add(book38);

                Integer certificateId = attributesMap.get(CERTIFICATE_AUTHORITY_SUPPORT);
                ExcelBean book39 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        CERTIFICATE_AUTHORITY_SUPPORT,
                        getAttributeValue(certificateId, orderProductComponentAttributes));
                book39.setOrder(3);
                book39.setGscQuoteDetailId(0);
                listBook.add(book39);

                Integer fqdnId = attributesMap.get(FQDN);
                ExcelBean book40 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        FQDN,
                        getAttributeValue(fqdnId, orderProductComponentAttributes));
                book40.setOrder(3);
                book40.setGscQuoteDetailId(0);
                listBook.add(book40);

                Integer adInfoId = attributesMap.get(ADDITIONAL_INFORMATION);
                ExcelBean book43 = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS,
                        ADDITIONAL_INFORMATION,
                        getAttributeValue(adInfoId, orderProductComponentAttributes));
                book43.setOrder(3);
                book43.setGscQuoteDetailId(0);
                listBook.add(book43);

                String requiredPortingNumbers = getAttributeValue(attributesMap.get(REQUIRED_PORTING_NUMBERS), orderProductComponentAttributes);
                if (StringUtils.isNoneBlank(requiredPortingNumbers) && (!Pattern.compile("^[,]").matcher(requiredPortingNumbers).find())) {
                    ExcelBean portingBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                            REQUIRED_PORTING_NUMBERS, requiredPortingNumbers);
                    portingBook.setOrder(3);
                    portingBook.setGscQuoteDetailId(0);
                    listBook.add(portingBook);
                }


                break;
            }

            default:

                LOGGER.info("Invalid master product component Name");

        }

    }

    private String appendTerminationCountryISDCode(Integer id, Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes, Map<String, Integer> attributesMap) {
        String[] terminationNumbersArr = getAttributeValue(id, orderProductComponentAttributes).split(CommonConstants.COMMA);
        List<String> terminationNumbers = Arrays.stream(terminationNumbersArr).map(terminationNumber -> getAttributeValue(attributesMap.get(TERMINATION_NUMBER_ISD_CODE),
                orderProductComponentAttributes) + terminationNumber).collect(Collectors.toList());

        return String.join(CommonConstants.COMMA, terminationNumbers);
    }

    /**
     * Method to concat values
     *
     * @param attributes
     * @return
     */
    private String concatValues(List<OrderProductComponentsAttributeValue> attributes) {
        StringBuilder values = new StringBuilder();
        if (attributes.size() == 1) {
            Optional<OrderProductComponentsAttributeValue> attribute = attributes.stream().findFirst();
            values.append(attribute.get().getAttributeValues());
        } else {
            attributes.stream().forEach(attribute -> {
                values.append(attribute.getAttributeValues()).append(CommonConstants.COMMA);
            });
        }

        String value = values.toString();
        if (value.endsWith(",")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    /**
     * Method to get attributes map
     *
     * @param productAttributes
     * @return
     */
    private Map<String, Integer> getAttributesMap(List<ProductAttributeMaster> productAttributes) {
        Map<String, Integer> attributesMap = new HashMap<>();
        productAttributes.stream().forEach(attribute -> {
            attributesMap.put(attribute.getName(), attribute.getId());
        });
        return attributesMap;
    }

    /**
     * Method to get component attributes based on specific attribute
     *
     * @param attributeId
     * @param componentAttributeList
     * @return
     */
    private List<OrderProductComponentsAttributeValue> getComponentAttributesBasedOnSpecificAttribute(
            Integer attributeId, Set<OrderProductComponentsAttributeValue> componentAttributeList) {
        List<OrderProductComponentsAttributeValue> attributeValuesList = new ArrayList<>();
        componentAttributeList.stream().forEach(componentAttribute -> {
            if (componentAttribute.getProductAttributeMaster().getId() == attributeId) {
                attributeValuesList.add(componentAttribute);
            }
        });
        return attributeValuesList;
    }

    /**
     * Method to get attribute Value
     *
     * @param attributeId
     * @return
     */
    private String getAttributeValue(Integer attributeId,
                                     Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes) {
        String value = "";
        if (Objects.nonNull(orderProductComponentAttributes) && Objects.nonNull(attributeId)) {
            List<OrderProductComponentsAttributeValue> attributeValues = getComponentAttributesBasedOnSpecificAttribute(
                    attributeId, orderProductComponentAttributes);

            if (!attributeValues.isEmpty()) {
                value = concatValues(attributeValues);
            }
        }
        return value;
    }

    /**
     * writeBook - writes data into excel
     *
     * @param aBook
     * @param row
     * @throws TclCommonException
     */
    public void writeBook(ExcelBean aBook, Row row) throws TclCommonException {
        if (Objects.isNull(aBook) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
        Cell cell = row.createCell(1);
        if (aBook.getGscQuoteId() != null) {
            if (aBook.getGscQuoteId() == 0) {
                cell.setCellValue(aBook.getLrSection());
            } else if (aBook.getGscQuoteId() != 0) {
                cell.setCellValue(aBook.getLrSection() + ":" + aBook.getGscQuoteId());
            }

        } else {
            if (aBook.getGscQuoteDetailId() != null) {
                if (aBook.getGscQuoteDetailId() == 0) {
                    cell.setCellValue(aBook.getLrSection());
                } else if (aBook.getGscQuoteDetailId() != 0) {
                    cell.setCellValue(aBook.getLrSection() + ":" + aBook.getGscQuoteDetailId());
                }
            }
        }
        cell = row.createCell(2);
        cell.setCellValue(aBook.getAttributeName());

        cell = row.createCell(3);
        cell.setCellValue(aBook.getAttributeValue());

    }

    /**
     * getLinks - retrieves gscQuotes under an orderToLe
     *
     * @param orderToLe
     * @param version
     * @return
     */
    private List<OrderGsc> getGscQuotes(OrderToLe orderToLe) {

        List<OrderGsc> orderGscList = new ArrayList<>();
        MstProductFamily mstProductFamily = mstProductFamilyRepository
                .findByNameAndStatus(GSIP, (byte) 1);
        if (mstProductFamily != null) {
            OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
                    .findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
            List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
                    .findByOrderToLeProductFamily(orderToLeProductFamily);

            orderProductSolutionList.stream().forEach(orderProductSolution -> {
                orderGscList
                        .addAll(orderGscRepository.findByorderProductSolutionAndStatus(orderProductSolution, (byte) 1));
            });
        }

        return orderGscList;
    }

    /**
     * getAttributeValues - retrieves attribute values for an OrderToLe
     *
     * @param orderToLe
     * @return
     * @throws TclCommonException
     */
    public Map<String, String> getAttributeValues(OrderToLe orderToLe) throws TclCommonException {
        Map<String, String> attributeMap = new HashMap<>();
        try {

            Objects.requireNonNull(orderToLe, ORDER_NULL_MESSAGE);

            orderToLe.getOrdersLeAttributeValues().forEach(attr -> {
                attributeMap.put(attr.getMstOmsAttribute().getName(), attr.getAttributeValue());

            });

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
        }
        return attributeMap;
    }

    /**
     * createOrderDetails - extracts order related details as excel bean
     *
     * @param listBook
     * @param attributeValues
     * @param isIntl
     * @param isGscMultiMacd
     */
    private void createOrderDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
                                    OrderToLe orderToLe, boolean isIntl, boolean isGscMultiMacd) {
        ExcelBean orderDetails = new ExcelBean(ORDER_DETAILS,
                ORDER_REF_ID, orderToLe.getOrder().getOrderCode());
        orderDetails.setOrder(1);
        orderDetails.setGscQuoteId(0);
        listBook.add(orderDetails);

        //if (orderToLe.getOrderType().equalsIgnoreCase(ORDER_TYPE_MACD) && !isGscMultiMacd) {
        ExcelBean orderSecIdDetails = new ExcelBean(ORDER_DETAILS,
                ORDER_SECS_ID, attributeValues.containsKey(CUSTOMER_SECS_ID)
                ? attributeValues.get(CUSTOMER_SECS_ID)
                : NOT_AVAILABLE);
        orderSecIdDetails.setOrder(1);
        orderSecIdDetails.setGscQuoteId(0);
        listBook.add(orderSecIdDetails);
//        }
//        else {
//            ExcelBean orderSecIdDetails = new ExcelBean(ORDER_DETAILS,
//                    ORDER_SECS_ID, attributeValues.containsKey(ORG_NO)
//                    ? attributeValues.get(ORG_NO)
//                    : NOT_AVAILABLE);
//            orderSecIdDetails.setOrder(1);
//            orderSecIdDetails.setGscQuoteId(0);
//            listBook.add(orderSecIdDetails);
//        }

        ExcelBean supplierInfo = new ExcelBean(ORDER_DETAILS,
                GSC_SUPPLIER_NAME,
                attributeValues.containsKey(GSC_SUPPLIER_NAME)
                        ? attributeValues.get(GSC_SUPPLIER_NAME)
                        : TCL);
        supplierInfo.setOrder(1);
        supplierInfo.setGscQuoteId(0);
        listBook.add(supplierInfo);

        ExcelBean ownerInfo = new ExcelBean(ORDER_DETAILS,
                Le_Owner,
                attributeValues.containsKey(Le_Owner)
                        ? attributeValues.get(Le_Owner)
                        : NOT_APPLICABLE);
        ownerInfo.setOrder(1);
        ownerInfo.setGscQuoteId(0);
        listBook.add(ownerInfo);

        ExcelBean customerInfo = new ExcelBean(ORDER_DETAILS,
                CUSTOMER_LE_NAME,
                attributeValues.containsKey(LE_NAME)
                        ? attributeValues.get(LE_NAME)
                        : NOT_APPLICABLE);
        customerInfo.setOrder(1);
        customerInfo.setGscQuoteId(0);
        listBook.add(customerInfo);

        ExcelBean cuId = new ExcelBean(ORDER_DETAILS,
                CUID,
                attributeValues.getOrDefault(CUID_VALUE, CommonConstants.EMPTY));
        cuId.setOrder(1);
        cuId.setGscQuoteId(0);
        listBook.add(cuId);

        ExcelBean orderType = new ExcelBean(ORDER_DETAILS,
                ORDER_TYPE, orderToLe.getOrderType());
        orderType.setOrder(1);
        orderType.setGscQuoteId(0);
        listBook.add(orderType);

        ExcelBean billingAddress = new ExcelBean(ORDER_DETAILS,
                CUSTOMER_CONTRACTING_BILLING_ADDRESS,
                attributeValues.containsKey(BILLING_ADDRESS)
                        ? attributeValues.get(BILLING_ADDRESS)
                        : "NA");
        billingAddress.setOrder(1);
        billingAddress.setGscQuoteId(0);
        listBook.add(billingAddress);

        ExcelBean paymentCurrency = new ExcelBean(ORDER_DETAILS,
                PAYMENT_CURRENCY, orderToLe.getCurrencyCode());
        paymentCurrency.setOrder(1);
        paymentCurrency.setGscQuoteId(0);
        listBook.add(paymentCurrency);

        ExcelBean paymentMethod = new ExcelBean(ORDER_DETAILS,
                PAYMENT_METHOD,
                attributeValues.get(INVOICE_METHOD));
        paymentMethod.setOrder(1);
        paymentMethod.setGscQuoteId(0);
        listBook.add(paymentMethod);

        ExcelBean paymentOptions = new ExcelBean(ORDER_DETAILS,
                PAYMENT_OPTIONS,
                attributeValues.getOrDefault(PAYMENT_OPTIONS, NA));
        paymentOptions.setOrder(1);
        paymentOptions.setGscQuoteId(0);
        listBook.add(paymentOptions);

        ExcelBean paymentTerm = new ExcelBean(ORDER_DETAILS,
                PAYMENT_TERM_VALUE,
                attributeValues.getOrDefault(PAYMENT_OPTIONS, NA));
        paymentTerm.setOrder(1);
        paymentTerm.setGscQuoteId(0);
        listBook.add(paymentTerm);

        ExcelBean autoCofNumber = new ExcelBean(ORDER_DETAILS,
                AUTO_COF_NUMBER, orderToLe.getOrder().getOrderCode());
        autoCofNumber.setOrder(1);
        autoCofNumber.setGscQuoteId(0);
        listBook.add(autoCofNumber);

        ExcelBean sfdc = new ExcelBean(ORDER_DETAILS,
                SFDC_ID, orderToLe.getTpsSfdcCopfId());
        sfdc.setOrder(1);
        sfdc.setGscQuoteId(0);
        listBook.add(sfdc);

        if (isIntl == false) {
            ExcelBean book5 = new ExcelBean(ORDER_DETAILS,
                    PROGRAM_MANAGER, "");
            book5.setOrder(1);
            book5.setGscQuoteId(0);
            listBook.add(book5);
        }

        Integer months = 0;
        //Since old orders had saved as 1 Year Contract term
        if (orderToLe.getTermInMonths().contains("Year")) {
            months = Integer.valueOf(orderToLe.getTermInMonths()
                    .replace("Year", "")
                    .trim()) * 12;
        } else {
            months = Integer.valueOf(orderToLe.getTermInMonths()
                    .replace("months", "")
                    .trim());
        }

        ExcelBean book6 = new ExcelBean(ORDER_DETAILS,
                CONTRACT_TERMS, months + " months");
        book6.setOrder(1);
        book6.setGscQuoteId(0);
        listBook.add(book6);
        if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) && Objects.nonNull(orderToLe.getClassification())) {
            ExcelBean book8 = new ExcelBean(ORDER_DETAILS,
                    OPPORTUNITY_CLASSIFICATION,
                    orderToLe.getClassification());
            book8.setOrder(1);
            book8.setGscQuoteId(0);
            listBook.add(book8);
        } else {

            ExcelBean book8 = new ExcelBean(ORDER_DETAILS,
                    OPPORTUNITY_CLASSIFICATION,
                    SELL_TO);
            book8.setOrder(1);
            book8.setGscQuoteId(0);
            listBook.add(book8);
        }
        StringBuilder gstDetail = new StringBuilder();

        String gstAddress = "";
        String gstNo = "";


        if (attributeValues.containsKey(LE_STATE_GST_NUMBER)) {
            gstNo = attributeValues.get(LE_STATE_GST_NUMBER);

        } else if (attributeValues.containsKey(GST_NUM)) {
            gstNo = attributeValues.get(GST_NUM);

        }
        if (attributeValues.containsKey(LE_STATE_GST_ADDRESS)) {
            gstAddress = attributeValues.get(LE_STATE_GST_ADDRESS);
        } else if (attributeValues.containsKey(GST_ADDR)) {
            gstAddress = attributeValues.get(GST_ADDR);
        }

        if (gstDetail == null || gstDetail.toString().equalsIgnoreCase(CommonConstants.EMPTY)) {
            gstDetail.append(Objects.nonNull(gstNo) ? gstNo : CommonConstants.EMPTY);
            gstDetail.append(",")
                    .append(Objects.nonNull(gstAddress) ? gstAddress : "NA");
        } else {
            if (gstDetail.toString().endsWith(CommonConstants.RIGHT_SLASH)) {
                gstDetail.setLength(gstDetail.length() - 1);
            }
        }

        ExcelBean book9 = new ExcelBean(ORDER_DETAILS,
                CUSTOMER_LE_GST,
                gstDetail.toString().matches("^[a-zA-Z0-9]+$") ? gstDetail.toString() : CommonConstants.EMPTY);
        book9.setOrder(1);
        book9.setGscQuoteId(0);
        listBook.add(book9);

        ExcelBean book10 = new ExcelBean(ORDER_DETAILS,
                ACCOUNT_MANAGER,
                attributeValues.containsKey(ACCOUNT_MANAGER)
                        ? attributeValues.get(ACCOUNT_MANAGER)
                        : "");
        book10.setOrder(1);
        book10.setGscQuoteId(0);
        listBook.add(book10);

        ExcelBean book12 = new ExcelBean(ORDER_DETAILS,
                CREDIT_LIMIT,
                attributeValues.containsKey(CREDIT_LIMIT)
                        ? attributeValues.get(CREDIT_LIMIT)
                        : "NA");
        book12.setOrder(1);
        book12.setGscQuoteId(0);
        listBook.add(book12);

        ExcelBean book13 = new ExcelBean(ORDER_DETAILS,
                ADVANCE_AMOUNT,
                attributeValues.containsKey(ADVANCE_AMOUNT)
                        ? attributeValues.get(ADVANCE_AMOUNT)
                        : "NA");
        book13.setOrder(1);
        book13.setGscQuoteId(0);
        listBook.add(book13);

        ExcelBean book14 = new ExcelBean(ORDER_DETAILS,
                BILLING_RATIO,
                attributeValues.containsKey(MST_BILLING_RATIO)
                        ? attributeValues.get(MST_BILLING_RATIO)
                        : "NA");
        book14.setOrder(1);
        book14.setGscQuoteId(0);
        listBook.add(book14);

        ExcelBean book15 = new ExcelBean(ORDER_DETAILS,
                PO_NUMBER,
                attributeValues.containsKey(PO_NUMBER_KEY)
                        ? attributeValues.get(PO_NUMBER_KEY)
                        : "NA");
        book15.setOrder(1);
        book15.setGscQuoteId(0);
        listBook.add(book15);

        ExcelBean book16 = new ExcelBean(ORDER_DETAILS,
                PO_DATE, attributeValues.containsKey(PO_DATE_KEY)
                ? attributeValues.get(PO_DATE_KEY)
                : "NA");
        book16.setOrder(1);
        book16.setGscQuoteId(0);
        listBook.add(book16);

        ExcelBean book17 = new ExcelBean(ORDER_DETAILS,
                DEPARTMENT_BILLING,
                attributeValues.containsKey(DEPARTMENT_BILLING)
                        ? attributeValues.get(DEPARTMENT_BILLING)
                        : CommonConstants.NO);
        book17.setOrder(1);
        book17.setGscQuoteId(0);
        listBook.add(book17);

        ExcelBean book18 = new ExcelBean(ORDER_DETAILS,
                DEPARTMENT_NAME,
                attributeValues.containsKey(DEPARTMENT_NAME)
                        ? attributeValues.get(DEPARTMENT_NAME)
                        : "NA");
        book18.setOrder(1);
        book18.setGscQuoteId(0);
        listBook.add(book18);


    }

    /**
     * Method to get list of sites
     *
     * @param orderToLe
     * @param version
     * @return
     */
    public List<OrderIllSite> getSites(OrderToLe orderToLe) {

        List<OrderIllSite> orderIllSitesList = new ArrayList<>();
        MstProductFamily mstProductFamily = mstProductFamilyRepository
                .findByNameAndStatus(GVPN, (byte) 1);
        if (mstProductFamily != null) {
            OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
                    .findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
            List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
                    .findByOrderToLeProductFamily(orderToLeProductFamily);

            orderProductSolutionList.stream().forEach(orderProductSolution -> orderIllSitesList.addAll(
                    orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution, (byte) 1)));
        }

        return orderIllSitesList;
    }

    /**
     * Method to create site details based on feasibility
     *
     * @param orderSiteFeasibility
     * @param listBook
     * @param site
     * @param feasible
     * @throws TclCommonException
     */
    private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
                                                     List<ExcelBean> listBook, OrderIllSite site, CustomFeasibilityRequest feasible,
                                                     Map<String, String> dependentAttributesOfGVPN) throws TclCommonException {
        ExcelBean book19 = new ExcelBean(SITE_DETAILS,
                FEASIBILITY_REQUEST_ID, orderSiteFeasibility.getFeasibilityCode());
        book19.setOrder(4);
        book19.setGscQuoteId(site.getId());
        listBook.add(book19);
        ExcelBean book20 = new ExcelBean(SITE_DETAILS,
                FEASIBILITY_RESPONSE_ID, orderSiteFeasibility.getFeasibilityCode());
        book20.setOrder(4);
        book20.setGscQuoteId(site.getId());
        listBook.add(book20);

        ExcelBean book3 = new ExcelBean(SITE_DETAILS,
                CUSTOMER_SEGMENT, "");
        book3.setOrder(4);
        book3.setGscQuoteId(site.getId());
        listBook.add(book3);

        ExcelBean book4 = new ExcelBean(SITE_DETAILS,
                LM_TYPE,
                feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
        book4.setOrder(4);
        book4.setGscQuoteId(site.getId());
        listBook.add(book4);
        dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

        ExcelBean book5 = new ExcelBean(SITE_DETAILS,
                SFDC_FEASIBILITY_ID,
                orderSiteFeasibility.getSfdcFeasibilityId() == null ? "" : orderSiteFeasibility.getSfdcFeasibilityId());
        book5.setOrder(4);
        book5.setGscQuoteId(site.getId());
        listBook.add(book5);
    }

    /**
     * Method to create default site details
     *
     * @param listBook
     * @param site
     */
    private void createDefaultSiteDetails(List<ExcelBean> listBook, OrderIllSite site) {
        ExcelBean book17 = new ExcelBean(SITE_DETAILS,
                BILLING_CATEGORY, BILLABLE);
        book17.setOrder(4);
        book17.setGscQuoteId(site.getId());
        listBook.add(book17);

        ExcelBean rfsDate = new ExcelBean(SITE_DETAILS,
                REQUESTED_DATE,
                site.getRequestorDate() != null ? DateUtil.convertDateToSlashString(site.getRequestorDate()) : "NA");
        rfsDate.setOrder(4);
        rfsDate.setGscQuoteId(site.getId());
        listBook.add(rfsDate);

        ExcelBean layerMedium = new ExcelBean(SITE_DETAILS,
                LAYER_MEDIUM, "Layer 3");
        layerMedium.setOrder(4);
        layerMedium.setGscQuoteId(site.getId());
        listBook.add(layerMedium);

        ExcelBean unfr = new ExcelBean(SITE_DETAILS,
                UNIFR, NO);
        unfr.setOrder(4);
        unfr.setGscQuoteId(site.getId());
        listBook.add(unfr);

        ExcelBean reporting = new ExcelBean(SITE_DETAILS,
                REPORTING, "Standard Reporting");
        reporting.setOrder(4);
        reporting.setGscQuoteId(site.getId());
        listBook.add(reporting);

        ExcelBean gvpnType = new ExcelBean(SITE_DETAILS,
                GVPNTYPE, "Fixed");
        gvpnType.setOrder(4);
        gvpnType.setGscQuoteId(site.getId());
        listBook.add(gvpnType);

        ExcelBean izoPrivate = new ExcelBean(SITE_DETAILS,
                IZOPrivate, NO);
        izoPrivate.setOrder(4);
        izoPrivate.setGscQuoteId(site.getId());
        listBook.add(izoPrivate);

        ExcelBean linkType = new ExcelBean(SITE_DETAILS,
                LINKTYPE, "Customer Facing");
        linkType.setOrder(4);
        linkType.setGscQuoteId(site.getId());
        listBook.add(linkType);

        ExcelBean gvpnFlavour = new ExcelBean(SITE_DETAILS,
                GVPN_FLAVOR, "GVPN");
        gvpnFlavour.setOrder(4);
        gvpnFlavour.setGscQuoteId(site.getId());
        listBook.add(gvpnFlavour);

    }

    /**
     * Method to create site location for excel
     *
     * @param site
     * @param listBook
     * @throws TclCommonException
     */
    private void createSiteLocationForExcel(OrderIllSite site, List<ExcelBean> listBook) throws TclCommonException {
        String response = returnApiAddressForSites(site.getErfLocSitebLocationId());
        AddressDetail adDetail = null;
        if (response != null) {
            adDetail = (AddressDetail) Utils.convertJsonToObject(response, AddressDetail.class);
        }
        String address = "";
        if (adDetail != null) {
            address = adDetail.getAddressLineOne() + " " + adDetail.getLocality() + " " + adDetail.getCity() + " " + adDetail.getState() + ""
                    + adDetail.getPincode();
        }
        ExcelBean siteLocation = new ExcelBean(SITE_DETAILS,
                SITE_ADDRESS, address);
        siteLocation.setOrder(4);
        siteLocation.setGscQuoteId(site.getId());
        listBook.add(siteLocation);
    }

    /**
     * Method to return API adresss for sites
     *
     * @param sites
     * @return
     */
    public String returnApiAddressForSites(Integer sites) {
        try {
            return (String) mqUtils.sendAndReceive(apiAddressQueue, String.valueOf(sites));
        } catch (Exception e) {
            LOGGER.error("error in process location", e);
        }
        return null;
    }

    /**
     * Method to return location It Contact Name
     *
     * @param id
     * @return
     */
    public String returnLocationItContactName(Integer id) {
        try {
            LOGGER.info("MDC Filter token value in before Queue call returnLocationItContactName {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            return (String) mqUtils.sendAndReceive(locationItContactQueue, String.valueOf(id));
        } catch (Exception e) {
            LOGGER.error("error in process location", e);
        }
        return null;
    }

    /**
     * Method to create sla details
     *
     * @param site
     * @param listBook
     * @throws TclCommonException
     */
    private void createSlaDetails(OrderGsc orderGsc, OrderIllSite site, List<ExcelBean> listBook)
            throws TclCommonException {
        GscSlaBeanListener slaBeansListener = gscSlaService.getSlaDetails(orderGsc.getAccessType());
        List<GscSlaBean> slaBeans = slaBeansListener.getGscSlaBeans();
        if (Objects.nonNull(slaBeans)) {
            slaBeans.stream().forEach(sla -> {

                switch (sla.getSlaName()) {
                    case SERVICE_AVAILABILITY:
                        ExcelBean ntwUpTime = new ExcelBean(SLA,
                                SERVICE_AVAILABILITY, sla.getDefaultValue());
                        ntwUpTime.setOrder(5);
                        ntwUpTime.setGscQuoteId(site.getId());
                        if (!listBook.contains(ntwUpTime)) {
                            listBook.add(ntwUpTime);
                        }
                        break;
                    case PACKET_DROP_GSC:

                        ExcelBean pckdrp = new ExcelBean(SLA,
                                PACKET_DROP_GSC, sla.getDefaultValue());
                        pckdrp.setOrder(5);
                        pckdrp.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp)) {
                            listBook.add(pckdrp);
                        }
                        break;
                    case JITTER_SERVICE_LEVEL_GSC:
                        ExcelBean pckdrp2 = new ExcelBean(SLA,
                                JITTER_SERVICE_LEVEL_GSC, sla.getDefaultValue());
                        pckdrp2.setOrder(5);
                        pckdrp2.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp2)) {
                            listBook.add(pckdrp2);
                        }
                        break;
                    case POST_DIAL_DELAY:
                        ExcelBean pckdrp3 = new ExcelBean(SLA,
                                POST_DIAL_DELAY, sla.getDefaultValue());
                        pckdrp3.setOrder(5);
                        pckdrp3.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp3)) {
                            listBook.add(pckdrp3);
                        }
                        break;
                    case LATENCY:
                        ExcelBean pckdrp4 = new ExcelBean(SLA,
                                LATENCY, sla.getDefaultValue());
                        pckdrp4.setOrder(5);
                        pckdrp4.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp4)) {
                            listBook.add(pckdrp4);
                        }
                        break;
                    default:
                        LOGGER.info("Invalid SLA name" + sla.getSlaName());

                }
            });
        }
    }

    /**
     * Method to create billing component price
     *
     * @param listBook
     * @param site
     */
    private void createBillingComponentPrice(List<ExcelBean> listBook, OrderIllSite site) {

        MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
                .findByName(VPN_PORT);
        extractComponentPrices(site, internetPortmstProductComponent, listBook,
                PORT_MRC_NRC);

        MstProductComponent lastMilePortmstProductComponent = mstProductComponentRepository
                .findByName(FPConstants.LAST_MILE.toString());
        extractComponentPrices(site, lastMilePortmstProductComponent, listBook,
                LASTMILE_MRC_NRC);

        MstProductComponent addIpsPortmstProductComponent = mstProductComponentRepository
                .findByName(FPConstants.ADDITIONAL_IP.toString());
        extractComponentPrices(site, addIpsPortmstProductComponent, listBook,
                ADDITIONAL_IPS_MRC_NRC);

        MstProductComponent cpemstProductComponent = mstProductComponentRepository
                .findByName(FPConstants.CPE.toString());
        extractComponentPrices(site, cpemstProductComponent, listBook, CPE_MRC_NRC);

    }

    /**
     * Method to extract component prices
     *
     * @param site
     * @param mstProductComponent
     * @param listBook
     * @param attrName
     */
    private void extractComponentPrices(OrderIllSite site, MstProductComponent mstProductComponent,
                                        List<ExcelBean> listBook, String attrName) {
        List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                .findByReferenceIdAndMstProductComponent(site.getId(), mstProductComponent);
        if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
            for (OrderProductComponent orderProductComponent : orderProductComponents) {
                OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
                        String.valueOf(orderProductComponent.getId()), COMPONENTS);

                if (price != null) {
                    ExcelBean book36 = new ExcelBean(
                            BILLING_COMPONENT + "("
                                    + orderProductComponent.getType().toUpperCase() + ")",
                            attrName, price.getEffectiveMrc() + "," + price.getEffectiveNrc());
                    book36.setOrder(6);
                    book36.setGscQuoteId(site.getId());
                    listBook.add(book36);
                }
            }

        }
    }

    /**
     * Method to get city selection
     *
     * @return
     */
    private String getLNSCitySelection(Integer orderGscDetailId, String serviceType) {
        List<GscCityNumber> cityDetails = gscOrderDetailService.getCityNumberConfiguration(orderGscDetailId,
                serviceType);

        List<String> cityCodes = cityDetails.stream().map(GscCityNumber::getOriginCity)
                .collect(Collectors.toList());
        LOGGER.info("City Codes" + cityCodes.toString());
        Map<String, String> citiesWithCode = queueForLNSCityNamesCall();
        LOGGER.info("All cities Codes" + citiesWithCode.toString());
        return cityCodes.stream().map(city -> {
            return citiesWithCode.get(city);

        }).collect(Collectors.joining(","));
    }

    /**
     * Method to call queue for city names
     *
     * @return
     */
    private Map<String, String> queueForLNSCityNamesCall() {
        Map<String, String> cityCodeAndNameList = new HashMap<>();
        try {
            String response = (String) mqUtils.sendAndReceive(cityDetailsQueue, "");
            LOGGER.info("Response received from queue:" + response);
            cityCodeAndNameList = (Map<String, String>) GscUtils.fromJson(response, Map.class);
            LOGGER.info("queue response" + cityCodeAndNameList.toString());
        } catch (Exception e) {
            LOGGER.info("City Queue Exception :: ", e.getMessage());
        }
        return cityCodeAndNameList;
    }

    /**
     * Method to get city selection
     *
     * @return
     */
    private String getACANSCitySelection(Integer orderGscDetailId, String serviceType) {
        List<GscCityNumber> cityDetails = gscOrderDetailService.getCityNumberConfiguration(orderGscDetailId,
                serviceType);

        List<String> cityCodes = cityDetails.stream().map(GscCityNumber::getOriginCity)
                .collect(Collectors.toList());
        LOGGER.info("City Codes" + cityCodes.toString());
        Map<String, String> citiesWithCode = queueForACANSCityNamesCall();
        LOGGER.info("All cities Codes" + citiesWithCode.toString());
        return cityCodes.stream().map(city -> {
            return citiesWithCode.get(city);

        }).collect(Collectors.joining(","));
    }

    /**
     * Method to call queue for city names
     *
     * @return
     */
    private Map<String, String> queueForACANSCityNamesCall() {
        Map<String, String> cityCodeAndNameList = new HashMap<>();
        try {
            String response = (String) mqUtils.sendAndReceive(acansCityDetailsQueue, "");
            LOGGER.info("Response received from queue:" + response);
            cityCodeAndNameList = (Map<String, String>) GscUtils.fromJson(response, Map.class);
            LOGGER.info("queue response" + cityCodeAndNameList.toString());
        } catch (Exception e) {
            LOGGER.info("City Queue Exception :: ", e.getMessage());
        }
        return cityCodeAndNameList;
    }

    public String getEmergencyAddress(String emergencyAddressId) {
        String emergencyAddressValue = "";
        try {
            String response = (String) mqUtils.sendAndReceive(addressDetail, emergencyAddressId);
            if (StringUtils.isNotBlank(response)) {
                AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(response, AddressDetail.class);

                emergencyAddressValue = Optional.ofNullable(addressDetail.getAddressLineOne()).orElse("") + ","
                        + Optional.ofNullable(addressDetail.getAddressLineTwo()).orElse("") + ","
                        + Optional.ofNullable(addressDetail.getLocality()).orElse("") + ","
                        + Optional.ofNullable(addressDetail.getCity()).orElse("") + ","
                        + Optional.ofNullable(addressDetail.getState()).orElse("") + ","
                        + Optional.ofNullable(addressDetail.getCountry()).orElse("") + ","
                        + Optional.ofNullable(addressDetail.getPincode()).orElse("");
            }
        } catch (Exception e) {
            LOGGER.warn("Error in process location {}", e.getMessage());
        }
        return emergencyAddressValue;
    }

    private void getDownStreamAttributeValues(List<ExcelBean> listBook, GscOrderBean gscOrderBean) {

        if (gscOrderBean.getInternationalDownStreamOrderId() != null && !StringUtils.isBlank(gscOrderBean.getInternationalDownStreamOrderId())) {
            ExcelBean bookInternatinalDownStreamOrder = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID, gscOrderBean.getInternationalDownStreamOrderId());
            bookInternatinalDownStreamOrder.setOrder(3);
            bookInternatinalDownStreamOrder.setGscQuoteDetailId(0);
            listBook.add(bookInternatinalDownStreamOrder);
        }

        if (gscOrderBean.getInternationalDownStreamSubOrderId() != null && !StringUtils.isBlank(gscOrderBean.getInternationalDownStreamSubOrderId())) {
            ExcelBean bookInternatinalDownStreamSubOrder = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID, gscOrderBean.getInternationalDownStreamSubOrderId());
            bookInternatinalDownStreamSubOrder.setOrder(3);
            bookInternatinalDownStreamSubOrder.setGscQuoteDetailId(0);
            listBook.add(bookInternatinalDownStreamSubOrder);
        }

        if (gscOrderBean.getDomesticDownStreamOrderId() != null && !StringUtils.isBlank(gscOrderBean.getDomesticDownStreamOrderId())) {
            ExcelBean bookDomesticDownStreamOrder = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_DOMESTIC_DOWNSTREAM_ORDER_ID, gscOrderBean.getDomesticDownStreamOrderId());
            bookDomesticDownStreamOrder.setOrder(3);
            bookDomesticDownStreamOrder.setGscQuoteDetailId(0);
            listBook.add(bookDomesticDownStreamOrder);
        }

        if (gscOrderBean.getDomesticDownStreamSubOrderId() != null && !StringUtils.isBlank(gscOrderBean.getDomesticDownStreamSubOrderId())) {
            ExcelBean bookDomesticDownStreamSubOrder = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID, gscOrderBean.getDomesticDownStreamSubOrderId());
            bookDomesticDownStreamSubOrder.setOrder(3);
            bookDomesticDownStreamSubOrder.setGscQuoteDetailId(0);
            listBook.add(bookDomesticDownStreamSubOrder);
        }

        if (gscOrderBean.getInterConnectDownStreamOrderId() != null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamOrderId())) {
            ExcelBean bookInterconectDownStreamOrder = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID, gscOrderBean.getInterConnectDownStreamOrderId());
            bookInterconectDownStreamOrder.setOrder(3);
            bookInterconectDownStreamOrder.setGscQuoteDetailId(0);
            listBook.add(bookInterconectDownStreamOrder);
        }


        if (gscOrderBean.getInterConnectDownStreamSubOrderId() != null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamSubOrderId())) {
            ExcelBean bookInterconnectDownStreamSubOrder = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID, gscOrderBean.getInterConnectDownStreamSubOrderId());
            bookInterconnectDownStreamSubOrder.setOrder(3);
            bookInterconnectDownStreamSubOrder.setGscQuoteDetailId(0);
            listBook.add(bookInterconnectDownStreamSubOrder);
        }

        if (gscOrderBean.getInterConnectDownStreamDomesticVTSSubOrderId() != null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamDomesticVTSSubOrderId())) {
            ExcelBean bookInterconectDownStreamOrderVts = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS, gscOrderBean.getInterConnectDownStreamDomesticVTSSubOrderId());
            bookInterconectDownStreamOrderVts.setOrder(3);
            bookInterconectDownStreamOrderVts.setGscQuoteDetailId(0);
            listBook.add(bookInterconectDownStreamOrderVts);
        }

        if (gscOrderBean.getInterConnectDownStreamDomesticNVTSubOrderId() != null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamDomesticNVTSubOrderId())) {
            ExcelBean bookInterconectDownStreamOrderNvt = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT, gscOrderBean.getInterConnectDownStreamDomesticNVTSubOrderId());
            bookInterconectDownStreamOrderNvt.setOrder(3);
            bookInterconectDownStreamOrderNvt.setGscQuoteDetailId(0);
            listBook.add(bookInterconectDownStreamOrderNvt);
        }
    }

    /**
     * getGstDetails
     *
     * @param gstNo
     * @param customerLeID
     * @return
     */
    private String getGstDetails(String gstNo, Integer customerLeID) {

        SiteGstDetail siteGstDetail = new SiteGstDetail();
        siteGstDetail.setGstNo(gstNo);
        siteGstDetail.setCustomerLeId(customerLeID);

        if (Objects.nonNull(gstNo) && Objects.nonNull(customerLeID)) {
            try {
                String leGst = (String) mqUtils.sendAndReceive(siteGstQueue, Utils.convertObjectToJson(siteGstDetail));

                if (StringUtils.isNotBlank(leGst)) {
                    LeStateInfo leStateInfo = (LeStateInfo) Utils.convertJsonToObject(leGst, LeStateInfo.class);
                    if (Objects.nonNull(leStateInfo.getAddress())) {
                        return leStateInfo.getAddress();
                    } else {
                        LOGGER.info("Address is empty for legal entity {} and site gst {}", customerLeID, gstNo);
                    }
                }
            } catch (TclCommonException | IllegalArgumentException e) {

                LOGGER.error("error in getting gst response");
            }
        }

        return "";
    }

    private void getSelectedOutpulseNumber(List<ExcelBean> listBook, Map<String, Integer> attributesMap,
                                           Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes) {
        Integer selectedOutpulseNumberId = attributesMap.get(SELECTED_TERMINATION_NUMBER_OUTPULSE);
        String selectedOutpulseNumber = getAttributeValue(selectedOutpulseNumberId, orderProductComponentAttributes);
        if (Objects.nonNull(selectedOutpulseNumber) && !selectedOutpulseNumber.isEmpty()) {
            ExcelBean selectedOutpulseNumberBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    UIFN_NUMBER,
                    getAttributeValue(selectedOutpulseNumberId, orderProductComponentAttributes));
            selectedOutpulseNumberBook.setOrder(3);
            selectedOutpulseNumberBook.setGscQuoteDetailId(0);
            listBook.add(selectedOutpulseNumberBook);
        }
    }

    private void getDIDSiteAddress(List<ExcelBean> listBook, Map<String, Integer> attributesMap,
                                   Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes) {
        Integer didSiteAddressValues = attributesMap.get(DOMESTIC_VOICE_SITE_ADDRESS);
        String didSiteAddressNumber = getAttributeValue(didSiteAddressValues, orderProductComponentAttributes);
        if (Objects.nonNull(didSiteAddressNumber) && !didSiteAddressNumber.isEmpty()) {
            ExcelBean didSiteAddress = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                    DID_SITE_ADDRESS,
                    getAttributeValue(didSiteAddressValues, orderProductComponentAttributes));
            didSiteAddress.setOrder(3);
            didSiteAddress.setGscQuoteDetailId(0);
            listBook.add(didSiteAddress);
        }
    }

    /**
     * getMACDServiceAttributes to show MACD service attribute
     *
     * @param List<ExcelBean>
     * @param Map<String,     Integer>
     *                        Set<OrderProductComponentsAttributeValue>
     */
    private void getMACDServiceAttributes(List<ExcelBean> listBook, Map<String, Integer> attributesMap,
                                          Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes) {
        Integer switchUnit = attributesMap.get(SWTCH_UNIT_CD_RERT);
        ExcelBean switchUnitBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                SWITCH_UNIT, getAttributeValue(switchUnit, orderProductComponentAttributes));
        switchUnitBook.setOrder(3);
        switchUnitBook.setGscQuoteDetailId(0);
        listBook.add(switchUnitBook);

        Integer circuitUnit = attributesMap.get(CIRCT_GR_CD_RERT);
        ExcelBean circuitUnitBook = new ExcelBean(GSC_ATTRIBUTES_DETAILS,
                CIRCUIT_UNIT, getAttributeValue(circuitUnit, orderProductComponentAttributes));

        circuitUnitBook.setOrder(3);
        circuitUnitBook.setGscQuoteDetailId(0);
        listBook.add(circuitUnitBook);
    }


    /**
     * Get sip trunk attributes in lr export
     *
     * @param listBook
     * @param orderToLe
     */
    private void getSIPTrunkAttributes(List<ExcelBean> listBook, OrderToLe orderToLe, String accessType) {
        OrdersLeAttributeValue ordersLeAttributeValue = null;

        List<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository.findByOrderIDAndMstOmsAttributeName(orderToLe.getOrder().getId(), MULTI_MACD_GSC_SERVICE_DETAILS);
        if (Objects.nonNull(ordersLeAttributeValues) && !ordersLeAttributeValues.isEmpty()) {

            ordersLeAttributeValue = ordersLeAttributeValues.stream().findFirst().get();
        }
        if (Objects.nonNull(ordersLeAttributeValue) && Objects.nonNull(ordersLeAttributeValue.getAttributeValue()) && Objects.nonNull(accessType)) {
            AdditionalServiceParams additionalServiceParams = additionalServiceParamRepository.findById(Integer.valueOf(ordersLeAttributeValue.getAttributeValue())).get();
            LOGGER.info("additionalServiceParams ID :: {}", additionalServiceParams.getId());
            List<GscMultiMacdServiceBean> gscMultiMacdServiceBeans = Utils.fromJson(additionalServiceParams.getValue(), new TypeReference<List<GscMultiMacdServiceBean>>() {
            });

            AtomicInteger count = new AtomicInteger(0);
            if (!CollectionUtils.isEmpty(gscMultiMacdServiceBeans)) {
                gscMultiMacdServiceBeans.stream().forEach(gscMultiMacdServiceBean -> {
                    count.getAndIncrement();
                    ExcelBean circuitId = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS + ":" + SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                            CIRCUIT_ID,
                            gscMultiMacdServiceBean.getCircuitID());
                    circuitId.setOrder(3);
                    circuitId.setGscQuoteDetailId(0);
                    listBook.add(circuitId);
                    ExcelBean customerIP = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS + ":" + SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                            CUST_IP_ADDRESS,
                            gscMultiMacdServiceBean.getIpAddress());
                    customerIP.setOrder(3);
                    customerIP.setGscQuoteDetailId(0);
                    listBook.add(customerIP);
                    ExcelBean circuitUnit = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS + ":" + SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                            SIP_CIRCUIT_UNIT,
                            gscMultiMacdServiceBean.getSipTrunkGroup());
                    circuitUnit.setOrder(3);
                    circuitUnit.setGscQuoteDetailId(0);
                    listBook.add(circuitUnit);
                    ExcelBean switchingUnit = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS + ":" + SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                            SWITCHING_UNIT,
                            gscMultiMacdServiceBean.getTclSwitch());
                    switchingUnit.setOrder(3);
                    switchingUnit.setGscQuoteDetailId(0);
                    listBook.add(switchingUnit);
                });
            }
        }
    }

    private void getInterconnectAttributes(List<ExcelBean> listBook, OrderToLe orderToLe, String accessType) {
//        String interConnectName = "";
        if (!(REQUEST_TYPE_CHANGE_OUTPULSE.equalsIgnoreCase(orderToLe.getOrderCategory()) ||
                REQUEST_TYPE_NUMBER_REMOVE.equalsIgnoreCase(orderToLe.getOrderCategory()))) {
            String interConnectId = ordersLeAttributeValueRepository.findByMstOmsAttribute_NameAndOrderToLe(INTERCONNECT_ID, orderToLe)
                    .stream().findFirst().get().getAttributeValue();
            LOGGER.info("Interconnect ID for Additional Service Param :: {}", interConnectId);
            String interConnectIdValue = additionalServiceParamRepository.findById(Integer.valueOf(interConnectId)).get().getValue();

            ExcelBean interconnectId = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS
                    + ":" + INTERCONNECT_ID,
                    INTERCONNECT_ID, interConnectIdValue);
            interconnectId.setOrder(3);
            interconnectId.setGscQuoteDetailId(0);
            listBook.add(interconnectId);

            String interConnectIdName = getInterconnectName(interConnectIdValue);

            LOGGER.info("Interconnect Name :: {}", interConnectIdName);
            ExcelBean interconnectIdName = new ExcelBean(GSC_SIP_ATTRIBUTES_DETAILS
                    + ":" + INTERCONNECT_NAME,
                    INTERCONNECT_NAME, interConnectIdName);
            interconnectIdName.setOrder(3);
            interconnectIdName.setGscQuoteDetailId(0);
            listBook.add(interconnectIdName);
        }
    }

    private String getInterconnectName(String interconnectId) {
        String interconnectName = "";
        if (Objects.nonNull(interconnectId)) {
            try {
                interconnectName = (String) mqUtils.sendAndReceive(interconnectNameQueue, interconnectId);
                LOGGER.info("Interconnect Response :: {} ", interconnectName);
            } catch (Exception e) {
                LOGGER.warn("Interconnect Name Not Found for Interconnect ID :: {}", interconnectId, e.getCause());
            }
        }
        return interconnectName;
    }
}
