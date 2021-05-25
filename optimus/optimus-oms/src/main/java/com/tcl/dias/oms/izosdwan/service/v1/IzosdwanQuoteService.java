package com.tcl.dias.oms.izosdwan.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.oms.constants.MACDConstants.MACD_QUOTE_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanCommericalBeanCof;
import com.tcl.dias.oms.izosdwan.beans.MrcDetailsBean;
import com.tcl.dias.oms.izosdwan.pdf.beans.IzosdwanCofPdfBean;
import com.tcl.dias.oms.izosdwan.pdf.beans.IzosdwanCofSiteBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javaswift.joss.model.StoredObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.helper.StringUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.AddonsBean;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.ByonBulkUploadDetail;
import com.tcl.dias.common.beans.CpeRequestBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.IzoSdwanCpeBomInterface;
import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.beans.IzoSdwanSiteDetails;
import com.tcl.dias.common.beans.IzoSdwanSlaRequest;
import com.tcl.dias.common.beans.IzosdwanBandwidthInterface;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesBean;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesUpdateBean;
import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.common.beans.ProfileSelectionInputDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceProviderLegalBean;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.SiCpeBean;
import com.tcl.dias.common.beans.SiSearchBean;
import com.tcl.dias.common.beans.SiServiceDetailBean;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.beans.VProxyAddonsBean;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.beans.VproxyAttributeDetails;
import com.tcl.dias.common.beans.VproxyProductOfferingBean;
import com.tcl.dias.common.beans.VproxyQuestionnaireDet;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.beans.VutmProfileDetailsBean;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.bean.OwnerRegionQueryResponseBean;
import com.tcl.dias.common.sfdc.bean.SfdcCreditCheckQueryRequest;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.AuditMode;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ByonUploadResponse;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.LocationTemplateRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.O2CSubCategoryBean;
import com.tcl.dias.oms.beans.PricingDetailBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuoteIllSitesFeasiblityBean;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteIzoSdwanAttributeValue;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.SiteDocumentBean;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.beans.VproxySolutionBean;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.entity.entities.AuditCwbTrailDetails;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.CpeBomDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.IzosdwanPricingService;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderCwbAuditTrailDetails;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanCpeConfigDetails;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteSla;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteCategory;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanAttributeValue;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanMssPricing;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanVutmLocationDetail;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteCategory;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanVutmLocationDetail;
import com.tcl.dias.oms.entity.enums.ProductType;
import com.tcl.dias.oms.entity.repository.AuditCwbTrailDetailsRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CpeBomDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanPricingServiceRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasiblityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderAuditCwbTrailDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanCpeConfigDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteCategoryRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanMssPricingRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanByonUploadDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanVutmLocationDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteCategoryRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanVutmLocationDetailRepository;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.ill.macd.service.v1.IllMACDService;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.izosdwan.beans.ActiveQuoteAndOrder;
import com.tcl.dias.oms.izosdwan.beans.ArcDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.BandWidthSummaryCpeBean;
import com.tcl.dias.oms.izosdwan.beans.BandwidthDet;
import com.tcl.dias.oms.izosdwan.beans.BandwidthDetails;
import com.tcl.dias.oms.izosdwan.beans.BandwidthRangeDetails;
import com.tcl.dias.oms.izosdwan.beans.BandwidthSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.ChargeableLineItemSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.ComponentDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationCpeInfo;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.CpeBandwidthSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.CpeLinks;
import com.tcl.dias.oms.izosdwan.beans.CpeModel;
import com.tcl.dias.oms.izosdwan.beans.CpeModelDetails;
import com.tcl.dias.oms.izosdwan.beans.CpeSummary;
import com.tcl.dias.oms.izosdwan.beans.CpeSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.CpeTypes;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanCommericalBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPdfSiteBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPricingServiceBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanQuotePdfBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanSolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.NetworkSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.NrcDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.PricingInformationRequestBean;
import com.tcl.dias.oms.izosdwan.beans.ProductPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.QuestionnaireInputDetails;
import com.tcl.dias.oms.izosdwan.beans.QuoteIzosdwanCgwDetails;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SEAMappedSiteDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteInfoBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteUpdateRequest;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeDetails;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeSummary;
import com.tcl.dias.oms.izosdwan.beans.SiteTypes;
import com.tcl.dias.oms.izosdwan.beans.SolutionLevelPricingBreakupDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SolutionPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.VproxyChargableComponents;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionRequestBean;
import com.tcl.dias.oms.izosdwan.pdf.beans.CommercialAttributesVproxy;
import com.tcl.dias.oms.izosdwan.pdf.beans.TechDetailCof;
import com.tcl.dias.oms.izosdwan.pdf.beans.VproxyCommercialDetailsBean;
import com.tcl.dias.oms.izosdwan.utils.OmsIzosdwanUtils;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.service.v1.BundleOmsSfdcService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * 
 * This is the service class for IzoSdwan
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class IzosdwanQuoteService {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IzosdwanQuoteService.class);
	public static final String DATEFORMAT = "yyyyMMddHHmmss";
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	protected OmsSfdcService omsSfdcService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;
	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Autowired
	IzosdwanSiteFeasiblityRepository siteFeasibilityRepository;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	protected DocusignAuditRepository docusignAuditRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	OrderAuditCwbTrailDetailsRepository orderAuditCwbTrailDetailsRepository;

	@Autowired
	OrderSiteCategoryRepository orderSiteCategoryRepository;

	@Autowired
	OrderIzosdwanCpeConfigDetailsRepository orderIzosdwanCpeConfigDetailsRepository;

	@Autowired
	private NotificationService notificationService;

	public static final String PARTNER = "PARTNER";

	@Autowired
	SiteCategoryRepository siteCategoryRepository;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Autowired
	protected MQUtils mqUtils;

	@Autowired
	QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	QuoteIzosdwanCgwDetailRepository quoteIzosdwanCgwDetailRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${rabbitmq.product.byon.interface}")
	private String interfaceType;

	@Value("${rabbitmq.product.country}")
	private String productLocations;

	@Value("${rabbitmq.orderIdInRespecToServiceId.queue}")
	String orderIdCorrespondingToServId;

	@Value("${rabbitmq.product.izosdwan.cpedetails}")
	String cpeBandWidthDetails;

	@Value("${rabbitmq.product.get.vproxy.profile.details}")
	String vproxyProfileDetails;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${rabbitmq.location.get.location.ids}")
	String locationIds;

	@Value("${rabbitmq.product.izosdwan.cpeportdetails}")
	String cpePortDetails;

	@Value("${rabbitmq.product.izosdwan.cpeInterfacedetails}")
	String cpebomInterfaceDetails;

	@Value("${rabbitmq.service.get.selected.site.details}")
	String selectedSiteDetails;

	@Value("${rabbitmq.product.get.profile.details}")
	String profiledetails;

	@Autowired
	IllMACDService illMACDService;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	PartnerService partnerService;

	@Autowired
	protected IllSiteRepository illSiteRepository;

	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	com.tcl.dias.oms.entity.repository.AttachmentRepository attachmentRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteIzoSdwanMssPricingRepository quoteIzoSdwanMssPricingRepository;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;

	@Value("${rabbitmq.si.details.by.customer.queue}")
	String siByCustomerQueue;

	@Value("${rabbitmq.get.sla.details.queue}")
	String slaDetailsQueue;

	@Autowired
	CreditCheckService creditCheckService;

	@Autowired
	QuoteIzoSdwanSlaRepository quoteIzoSdwanSlaRepository;

	@Value("${info.customer_le_location_queue}")
	String customerLeLocationQueue;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

	@Value("${rabbitmq.service.provider.izosdwan}")
	String spQueueIzosdwan;

	@Value("${rabbitmq.customerleattr.product.queue}")
	protected String customerLeAttrQueueProduct;

	@Value("${rabbitmq.o2c.sitedetail}")
	String updateTermsInMonthsSiteDetailQueue;

	private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${izosdwan.get.active.quotesandorders}")
	String getActive;

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	BundleOmsSfdcService bundleOmsSfdcService;

	@Autowired
	QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository;

	@Autowired
	IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;

	@Autowired
	IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;

	@Autowired
	protected OrderRepository orderRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderIzosdwanSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderIzosdwanSiteSlaRepository orderIzosdwanSiteSlaRepository;

	@Autowired
	OrderIzosdwanSiteStageAuditRepository orderIzosdwanSiteStageAuditRepository;

	@Autowired
	OrderIzosdwanSiteStatusAuditRepository orderIzosdwanSiteStatusAuditRepository;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Autowired
	OrderIzosdwanSiteRepository orderIzosdwanSiteRepository;

	@Autowired
	protected OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	protected QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	IzosdwanSiteFeasibilityAuditRepository izosdwanSiteFeasibilityAuditRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	IzosdwanPricingAndFeasibilityService izosdwanPricingAndFeasibilityService;

	@Autowired
	IzosdwanPricingServiceRepository izosdwanPricingServiceRepository;

	@Autowired
	OmsIzosdwanUtils omsIzosdwanUtils;

	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;

	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Value("${rabbitmq.customer.contact.details.queue}")
	String customerLeContactQueueName;

	final DecimalFormat decimalFormat = new DecimalFormat("0.00");

//	public static final Map<String, List<String>> componentSubComponentMap = IzosdwanUtils
//			.getComponentsAndSubComponentsMap();

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Autowired
	IzosdwanQuotePdfService izosdwanQuotePdfService;

	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	IzosdwanQuotePdfService izoSdwnQuotepdfService;

	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	QuoteIzosdwanVutmLocationDetailRepository quoteIzosdwanVutmLocationDetailRepository;

	@Autowired
	OrderIzosdwanVutmLocationDetailRepository orderIzosdwanVutmLocationDetailRepository;

	@Autowired
	SlaMasterRepository slaMasterRepository;

	@Autowired
	OrderIzosdwanCgwDetailRepository orderIzosdwanCgwDetailRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Value("${sfdc.process.ownerregion.query}")
	String ownerRegionQuery;

	@Value("${document.upload}")
	String uploadPath;
	
	@Value("${skip_o2C_for_all_custom_journey_orders}")
	private boolean skipCustomJourneyOrders;
	
	@Value("${skip_o2C_for_all_International_journey_orders}")
	private boolean skipInternationalJourneyOrders;

	@Autowired
	CpeBomDetailsRepository cpeBomDetailsRepository;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;
	@Autowired
	AuditCwbTrailDetailsRepository auditCwbTrailDetailsRepository;

	@Autowired
	OrderIzosdwanAttributeValueRepository orderIzosdwanAttributeValueRepository;
	
	@Value("${rabbitmq.customer.currency.queue}")
    private String getCurrency;

	/**
	 * 
	 * createQuote - This method is used to create a quote The input validation is
	 * done and the corresponding tables are populated with initial set of values
	 * 
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteResponse createQuote(QuoteDetail quoteDetail, Integer erfCustomerId) throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		try {
			validateQuoteDetail(quoteDetail);// validating the input for create Quote
			User user = getUserId(Utils.getSource());
			if (quoteDetail.getQuoteId() != null) {
				response.setQuoteId(quoteDetail.getQuoteId());
				response.setQuoteleId(quoteDetail.getQuoteleId());
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
				if (quoteToLe.isPresent()) {
					persistInSdwanAttributeValues(quoteDetail, quoteToLe.get());
				}
				// checkVendorName(quoteDetail);
				return response;
			}
			QuoteToLe quoteTole = processQuote(quoteDetail, erfCustomerId, user);
			Quote quote = quoteTole.getQuote();
			persistQuoteLeAttributes(user, quoteTole);
			persistInSdwanAttributeValues(quoteDetail, quoteTole);
			if (quoteTole != null) {
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteCode(quote.getQuoteCode());
				response.setQuoteId(quoteTole.getQuote().getId());
			}
			bundleOmsSfdcService.processCreateOpty(quoteTole, quoteDetail.getProductName());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * @link http://www.tatacommunications.com/ getQuoteDetails- This method is used
	 *       to get the quote details
	 * 
	 * @param quoteId
	 * @param version
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteBean getQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, List<Integer> siteIds) throws TclCommonException {
		QuoteBean response = null;
		int[] totalSites = { 0 };
		try {
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites, isSiteProperitiesRequired, siteId, siteIds);
			List<QuoteIzoSdwanAttributeValue> attributeValues = new ArrayList<>();
			getIzoSdwanAttributeValues(quote, attributeValues);
			response.setQuoteSdwanAttributeValues(attributeValues);
			Optional<QuoteToLe> quoteToLe1 = quote.getQuoteToLes().stream().findFirst()
					.filter(quoteToLe -> MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()));
			String name = getProfileName(quote);
			response.setSuggestedProfileName(name);
			if (quoteToLe1.isPresent()) {
				response.setQuoteType(quoteToLe1.get().getQuoteType());
				response.setQuoteCategory(quoteToLe1.get().getQuoteCategory());
				if (Objects.nonNull(quoteToLe1.get().getIsMultiCircuit()) && quoteToLe1.get().getIsMultiCircuit() == 1)
					response.setIsMultiCircuit(true);
				List<String> multiCircuitChangeBandwidthFlag = new ArrayList<>();
				List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository
						.findByQuoteToLe_Id(quoteToLe1.get().getId());
				quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
					if (Objects.nonNull(quoteIllSiteToService)
							&& CommonConstants.BACTIVE.equals(quoteIllSiteToService.getBandwidthChanged()))
						multiCircuitChangeBandwidthFlag.add("true");
					else
						multiCircuitChangeBandwidthFlag.add("false");
				});
				if (multiCircuitChangeBandwidthFlag.contains("false")) {
					response.setIsMulticircuitBandwidthChangeFlag(false);
				} else
					response.setIsMulticircuitBandwidthChangeFlag(true);
			}
			List<SiteTypeDetails> SiteTypeDetails = getSiteTypeDetails(quoteId);
			SiteTypeDetails.stream().forEach(type -> {
				totalSites[0] += type.getNoOfSites();
			});
			response.setIzoSdwanTotalNoOfSites(totalSites[0]);
			List<QuoteIzosdwanCgwDetail> cgWdetails = quoteIzosdwanCgwDetailRepository.findByQuote(quote);
			QuoteIzosdwanCgwDetails cgwBean = new QuoteIzosdwanCgwDetails();

			if (quote.getNsQuote() != null && quote.getNsQuote().equalsIgnoreCase("Y")) {
				List<QuoteIzosdwanCgwDetails> cgwBeans = getCgwDetails(cgWdetails);
				response.setCgwDetails(cgwBeans);
			}

			if (!cgWdetails.isEmpty() && cgWdetails != null) {
				cgwBean.setCgwMigUserModifiedBW(cgWdetails.get(0).getMigrationUserBw());
				cgwBean.setCgwMigSuggestedBW(cgWdetails.get(0).getMigrationSystemBw());
				cgwBean.setMigrationHeteroBandwidth(cgWdetails.get(0).getHetroBw());
				cgwBean.setPrimaryLocation(cgWdetails.get(0).getPrimaryLocation());
				cgwBean.setSecondaryLocation(cgWdetails.get(0).getSecondaryLocation());
				cgwBean.setUseCase1(cgWdetails.get(0).getUseCase1a());
				cgwBean.setUseCase2(cgWdetails.get(0).getUseCase2());
				cgwBean.setUseCase3(cgWdetails.get(0).getUseCase3());
				cgwBean.setUseCase4(cgWdetails.get(0).getUseCase4());
				cgwBean.setComponents(constructQuoteProductComponentCgw(cgWdetails.get(0).getId(), false, false));
				cgwBean.setId(cgWdetails.get(0).getId());
				if (quote.getNsQuote() != null && quote.getNsQuote().equalsIgnoreCase("Y")) {
					cgwBean.setUseCase1aBw(cgWdetails.get(0).getUseCase1aBw());
					cgwBean.setUseCase1aRefId(cgWdetails.get(0).getUseCase1aRefId());
					cgwBean.setUseCase1b(cgWdetails.get(0).getUseCase1b());
					cgwBean.setUseCase1bBw(cgWdetails.get(0).getUseCase1bBw());
					cgwBean.setUseCase1bRefId(cgWdetails.get(0).getUseCase1bRefId());
					cgwBean.setUseCase3Bw(cgWdetails.get(0).getUseCase3Bw());
					cgwBean.setUseCase3RefId(cgWdetails.get(0).getUseCase3RefId());
					cgwBean.setUseCase4Bw(cgWdetails.get(0).getUseCase4Bw());
					cgwBean.setUseCase4RefId(cgWdetails.get(0).getUseCase4RefId());
					cgwBean.setCosModel(cgWdetails.get(0).getCosModel());
				}
			}
			response.setQuoteIzosdwanCgwDetails(cgwBean);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	private List<QuoteIzosdwanCgwDetails> getCgwDetails(List<QuoteIzosdwanCgwDetail> cgWdetails) {
		List<QuoteIzosdwanCgwDetails> cgwBeans = new ArrayList<>();
		if (!CollectionUtils.isEmpty(cgWdetails)) {
			for (QuoteIzosdwanCgwDetail cgWdetail :  cgWdetails) {
				QuoteIzosdwanCgwDetails cgwBean = new QuoteIzosdwanCgwDetails();
				cgwBean.setCgwMigUserModifiedBW(cgWdetail.getMigrationUserBw());
				cgwBean.setCgwMigSuggestedBW(cgWdetail.getMigrationSystemBw());
				cgwBean.setMigrationHeteroBandwidth(cgWdetail.getHetroBw());
				cgwBean.setPrimaryLocation(cgWdetail.getPrimaryLocation());
				cgwBean.setSecondaryLocation(cgWdetail.getSecondaryLocation());
				cgwBean.setUseCase1(cgWdetail.getUseCase1a());
				cgwBean.setUseCase2(cgWdetail.getUseCase2());
				cgwBean.setUseCase3(cgWdetail.getUseCase3());
				cgwBean.setUseCase4(cgWdetail.getUseCase4());
				cgwBean.setComponents(constructQuoteProductComponentCgw(cgWdetail.getId(), false, false));
				cgwBean.setId(cgWdetail.getId());
				cgwBean.setUseCase1aBw(cgWdetail.getUseCase1aBw());
				cgwBean.setUseCase1aRefId(cgWdetail.getUseCase1aRefId());
				cgwBean.setUseCase1b(cgWdetail.getUseCase1b());
				cgwBean.setUseCase1bBw(cgWdetail.getUseCase1bBw());
				cgwBean.setUseCase1bRefId(cgWdetail.getUseCase1bRefId());
				cgwBean.setUseCase3Bw(cgWdetail.getUseCase3Bw());
				cgwBean.setUseCase3RefId(cgWdetail.getUseCase3RefId());
				cgwBean.setUseCase4Bw(cgWdetail.getUseCase4Bw());
				cgwBean.setUseCase4RefId(cgWdetail.getUseCase4RefId());
				cgwBean.setCosModel(cgWdetail.getCosModel());
				cgwBeans.add(cgwBean);
			}

		}
		return cgwBeans;
	}

	private void getIzoSdwanAttributeValues(Quote quote, List<QuoteIzoSdwanAttributeValue> attributeValues) {
		List<QuoteIzoSdwanAttributeValues> sdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
				.findByQuote(quote);
		if (!(sdwanAttributeValues.isEmpty())) {
			for (QuoteIzoSdwanAttributeValues attributeVal : sdwanAttributeValues) {
				QuoteIzoSdwanAttributeValue sdwanVal = new QuoteIzoSdwanAttributeValue();
				sdwanVal.setDisplayValue(attributeVal.getDisplayValue());
				sdwanVal.setAttributeValue(attributeVal.getAttributeValue());
				attributeValues.add(sdwanVal);
			}
		}
	}

	private String getProfileName(Quote quote) {
		String profileName = "";
		try {
			List<QuoteIzoSdwanAttributeValues> val = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(CommonConstants.SUGGESTEDPROFILENAME, quote);
			if (val != null && !val.isEmpty()) {
				profileName = val.get(0).getAttributeValue();
			}

		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("Error in getting profile name from izosdwan attribute values", e);
		}
		return profileName;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ getCpeDetails- This method is used
	 *       to get the cpe and bandwidth details
	 * @param bean
	 * @return
	 * @throws TclCommonException
	 */
	public List<IzoSdwanCpeDetails> getCpeDetails(String vendorName, String addons, String profileName)
			throws TclCommonException {
		List<IzoSdwanCpeDetails> cpeDetails = new ArrayList<>();
		CpeRequestBean bean = new CpeRequestBean();
		if (addons == null) {
			bean.setAddons(addons);
		} else {
			addons = addons.toUpperCase();
			addons = addons.replace(" ", "_");
			bean.setAddons(addons);
		}

		bean.setProfileName(profileName);
		bean.setVendorName(vendorName);
		String cpeBandWidthDet = (String) mqUtils.sendAndReceive(cpeBandWidthDetails, Utils.convertObjectToJson(bean));
		cpeDetails = GscUtils.fromJson(cpeBandWidthDet, new TypeReference<List<IzoSdwanCpeDetails>>() {
		});
		return cpeDetails;
	}

	/**
	 * This method validates the Quote Details Request validateQuoteDetail
	 * 
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	public void validateQuoteDetail(QuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null)) {// TODO validate the inputs for quote
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * This method gets the izo sdwan cpe port details
	 * 
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */

	public List<IzoSdwanSiteDetails> getSiteDetails() throws TclCommonException, IllegalArgumentException {
		List<IzoSdwanSiteDetails> portDetails = new ArrayList<>();
		String cpePortDet = (String) mqUtils.sendAndReceive(cpePortDetails, Utils.convertObjectToJson(""));
		portDetails = GscUtils.fromJson(cpePortDet, new TypeReference<List<IzoSdwanSiteDetails>>() {
		});
		return portDetails;
	}

	@SuppressWarnings("unlikely-arg-type")
	public List<VendorProfileDetailsBean> getProfileSelectionDetails(ProfileSelectionInputDetails inputDetails)
			throws TclCommonException {
		if (inputDetails.getCustomerId() == null) {
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}
		SiSearchBean siSearchBean = new SiSearchBean();
		siSearchBean.setCustomerId(inputDetails.getCustomerId().toString());
		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(inputDetails.getQuoteId());
		if (quoteToLe != null && !quoteToLe.isEmpty()) {
			List<Integer> leIds = new ArrayList<>();
			leIds.add(quoteToLe.get(0).getErfCusCustomerLegalEntityId());
			siSearchBean.setLeIds(leIds);
		}
		String profileDet = (String) mqUtils.sendAndReceive(selectedSiteDetails,
				Utils.convertObjectToJson(siSearchBean));
		LOGGER.info("Result after service inventory queue call is {}", profileDet);
		Integer count = profileDet != null ? GscUtils.fromJson(profileDet, Integer.class) : 0;
		LOGGER.info("Result after service inventory queue call is after processing {}", count);
		List<VendorProfileDetailsBean> vendorProfileDetails = new ArrayList<>();
		String profileDetails = (String) mqUtils.sendAndReceive(profiledetails,
				Utils.convertObjectToJson(inputDetails.getVendorName()));
		LOGGER.info("Making a queue call to producta micro service");
		LOGGER.info("Input for queue call is {}", inputDetails.getVendorName());
		vendorProfileDetails = GscUtils.fromJson(profileDetails, new TypeReference<List<VendorProfileDetailsBean>>() {
		});
		Optional<QuoteIzoSdwanAttributeValues> attrValue = Optional.empty();
		if (inputDetails.getQuoteId() != null) {
			Quote quote = getQuote(inputDetails.getQuoteId());
			List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByQuote(quote);
			attrValue = attributeValues.stream()
					.filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.ISPROFILEVALID)).findFirst();
		}
		// Profile validation for byon uploaded details
		List<QuoteIzosdwanByonUploadDetail> uploadDet = quoteIzosdwanByonUploadDetailRepository
				.selectSiteTypeByQuote(inputDetails.getQuoteId());
		if (inputDetails.getProfileName().isEmpty() && inputDetails.getProfileName() == null) {
			Map<String, List<ProductOfferingsBean>> productDetails = vendorProfileDetails.stream().collect(Collectors
					.toMap(VendorProfileDetailsBean::getVendor, VendorProfileDetailsBean::getProductOfferingsBeans));
			// upto now we are checking only for versa
			if (inputDetails.getVendorName().equalsIgnoreCase("Select")) {
				List<ProductOfferingsBean> productBeanDet = productDetails.get(CommonConstants.VERSA_VENDOR_CODE);
				for (ProductOfferingsBean bean : productBeanDet) {
					if (bean.getProductOfferingsName().equals("Basic")) {
						if (count > 0) {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.DISABLE);
						} else {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.ENABLE);
						}
						if (attrValue.isPresent()) {
							if (attrValue.get().getAttributeValue().equals(CommonConstants.FALSE)) {
								bean.setIsRecommended(false);
								bean.setAction(CommonConstants.DISABLE);
								// attrValue.get().setAttributeValue("true");
								quoteIzoSdwanAttributeValuesRepository.save(attrValue.get());
							}
						}
						if (uploadDet.size() > 0) {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.DISABLE);
						}

					} else {
						bean.setAction(CommonConstants.ENABLE);
						bean.setIsRecommended(false);
					}
				}
			} else if (inputDetails.getVendorName().equalsIgnoreCase("Cisco")) {
				// profile validation need to be added
			}

		} else if (inputDetails.getProfileName().equals(CommonConstants.ALL.toLowerCase())) {
			Map<String, List<ProductOfferingsBean>> productDetails = vendorProfileDetails.stream().collect(Collectors
					.toMap(VendorProfileDetailsBean::getVendor, VendorProfileDetailsBean::getProductOfferingsBeans));
			List<ProductOfferingsBean> productBeanDet = productDetails.get(CommonConstants.VERSA_VENDOR_CODE);
			for (ProductOfferingsBean bean : productBeanDet) {
				if (bean.getProductOfferingsName().equals("Basic")) {
					if (count > 0) {
						bean.setIsRecommended(false);
						bean.setAction(CommonConstants.DISABLE);
					} else {
						bean.setIsRecommended(false);
						bean.setAction(CommonConstants.ENABLE);
					}
					if (attrValue.isPresent()) {
						if (attrValue.get().getAttributeValue().equals("false")) {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.DISABLE);
							// attrValue.get().setAttributeValue("true");
							quoteIzoSdwanAttributeValuesRepository.save(attrValue.get());
						}
					}
					if (uploadDet.size() > 0) {
						bean.setIsRecommended(false);
						bean.setAction(CommonConstants.DISABLE);
					}
				} else {
					bean.setAction(CommonConstants.ENABLE);
					bean.setIsRecommended(false);
				}

			}
		} else if (inputDetails.getProfileName().equals("Basic")) {
			boolean basicFlag = true;
			Map<String, List<ProductOfferingsBean>> productDetails = vendorProfileDetails.stream().collect(Collectors
					.toMap(VendorProfileDetailsBean::getVendor, VendorProfileDetailsBean::getProductOfferingsBeans));
			List<ProductOfferingsBean> productBeanDet = productDetails.get(CommonConstants.VERSA_VENDOR_CODE);
			for (ProductOfferingsBean bean : productBeanDet) {
				if (bean.getProductOfferingsName().equals("Basic")) {
					if (count > 0) {
						bean.setIsRecommended(false);
						bean.setAction(CommonConstants.DISABLE);
					} else {
						bean.setIsRecommended(true);
						bean.setAction(CommonConstants.ENABLE);
						basicFlag = false;
					}
					if (attrValue.isPresent()) {
						if (attrValue.get().getAttributeValue().equals("false")) {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.DISABLE);
							// attrValue.get().setAttributeValue("true");
							quoteIzoSdwanAttributeValuesRepository.save(attrValue.get());
						}
					}
					if (uploadDet.size() > 0) {
						bean.setIsRecommended(false);
						bean.setAction(CommonConstants.DISABLE);
					}
				} else {
					bean.setAction(CommonConstants.ENABLE);
					if (bean.getProductOfferingsName().equals("Enhanced")) {
						if (basicFlag) {
							bean.setIsRecommended(true);
						}
					} else {
						bean.setIsRecommended(false);
					}
				}
			}
		} else {
			Map<String, List<ProductOfferingsBean>> productDetails = vendorProfileDetails.stream().collect(Collectors
					.toMap(VendorProfileDetailsBean::getVendor, VendorProfileDetailsBean::getProductOfferingsBeans));
			if (inputDetails.getVendorName().equalsIgnoreCase("Select")) {
				List<ProductOfferingsBean> productBeanDet = productDetails.get(CommonConstants.VERSA_VENDOR_CODE);
				for (ProductOfferingsBean bean : productBeanDet) {
					if (bean.getProductOfferingsName().equals("Basic")) {
						if (count > 0) {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.DISABLE);
						} else {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.ENABLE);

						}
						if (attrValue.isPresent()) {
							if (attrValue.get().getAttributeValue().equals("false")) {
								bean.setIsRecommended(false);
								bean.setAction(CommonConstants.DISABLE);
								// attrValue.get().setAttributeValue("true");
								quoteIzoSdwanAttributeValuesRepository.save(attrValue.get());
							}
						}
						if (uploadDet.size() > 0) {
							bean.setIsRecommended(false);
							bean.setAction(CommonConstants.DISABLE);
						}
					} else {
						bean.setAction(CommonConstants.ENABLE);
						if (bean.getProductOfferingsName().equals(inputDetails.getProfileName())) {
							bean.setIsRecommended(true);
						} else {
							bean.setIsRecommended(false);
						}
					}
				}
			}
		}
		return vendorProfileDetails;
	}

	private void persistInSdwanAttributeValues(QuoteDetail quoteDetail, QuoteToLe quoteTole) {
		try {
			Quote quote = getQuote(quoteTole.getQuote().getId());
			List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByQuote(quote);
			if (!(attributeValues.isEmpty())) {
				quoteIzoSdwanAttributeValuesRepository.deleteAll(attributeValues);
			}
			if (!(quoteDetail.getQuoteAttributes().isEmpty())) {
				List<QuoteIzoSdwanAttributeValues> attributeValues1 = new ArrayList<>();
				for (QuestionnaireInputDetails val : quoteDetail.getQuoteAttributes()) {
					QuoteIzoSdwanAttributeValues sdwanVal = new QuoteIzoSdwanAttributeValues();
					sdwanVal.setAttributeValue(val.getAttributeValue());
					sdwanVal.setDisplayValue(val.getAttributeName());
					sdwanVal.setQuote(quoteTole.getQuote());
					attributeValues1.add(sdwanVal);
				}
				quoteIzoSdwanAttributeValuesRepository.saveAll(attributeValues1);
			}

		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param userData
	 * @return User
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @throws TclCommonException
	 */

	protected QuoteBean constructQuote(Quote quote, Boolean isFeasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, List<Integer> siteIds) throws TclCommonException {
		QuoteBean quoteDto = new QuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setVendorName(quote.getIzosdwanFlavour());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		quoteDto.setTermInMonths(quote.getTermInMonths());
		quoteDto.setNsQuote(quote.getNsQuote() != null
				? (quote.getNsQuote().equals(CommonConstants.Y) ? CommonConstants.Y : CommonConstants.N)
				: CommonConstants.N);
		if (quote.getCustomer() != null) {
			quoteDto.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		}
		quoteDto.setLegalEntities(
				constructQuoteLeEntitDtos(quote, isFeasibleSites, isSiteProperitiesRequired, siteId, siteIds));

		OrderConfirmationAudit auditEntity = orderConfirmationAuditRepository
				.findByOrderRefUuid(quoteDto.getQuoteCode());
		if (auditEntity != null) {
			quoteDto.setPublicIp(getPublicIp(auditEntity.getPublicIp()));
		} else {
			if (RequestContextHolder.getRequestAttributes() != null) {
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
						.currentRequestAttributes()).getRequest();
				String forwardedIp = request.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
				LOGGER.info("Audit Public IP is {} ", forwardedIp);
				if (forwardedIp != null) {
					quoteDto.setPublicIp(getPublicIp(forwardedIp));
				}
			}
		}
		CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quoteDto.getQuoteCode(),
				Source.MANUAL_COF.getSourceType());
		if (cofDetail != null) {
			quoteDto.setIsManualCofSigned(true);
		}
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quoteDto.getQuoteCode());
		quoteDto.setIsDocusign(docusignAudit != null);
		quoteDto.setCustomerName(quote.getCustomer().getCustomerName());
		quoteDto.setEffectiveDate(quote.getEffectiveDate());
		return quoteDto;

	}

	/**
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SiteTypeDetails> getSiteTypeDetails(Integer quoteId) {
		List<SiteTypeDetails> SiteTypeDetails = new ArrayList<>();

		if (quoteId != null) {

			Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);

			if (quoteDetails != null) {

				ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
				if (solutions != null) {

					List<QuoteIzosdwanSite> sdwanSiteDetails = getSdwanSiteDetails(solutions);
					List<String> siteTypes = quoteIzosdwanSiteRepository
							.getDistinctSiteTypesForSdwan(solutions.getId());
					if (siteTypes != null && !siteTypes.isEmpty()) {
						siteTypes.stream().forEach(type -> {
							SiteTypeDetails siteTypeDetail = new SiteTypeDetails();
							siteTypeDetail.setSiteTypename(type);
							List<QuoteIzosdwanSite> sites = sdwanSiteDetails.stream()
									.filter(site -> site.getIzosdwanSiteType().equalsIgnoreCase(type))
									.collect(Collectors.toList());
							if (siteTypeDetail.getSiteTypename().contains("Dual")
									|| IzosdwanCommonConstants.BYONUNDERLAYSHAREDSITES
											.contains(siteTypeDetail.getSiteTypename())) {
								siteTypeDetail.setNoOfSites(sites.size() / 2);
							} else {
								siteTypeDetail.setNoOfSites(sites.size());
							}
							SiteTypeDetails.add(siteTypeDetail);
							List<QuoteIzosdwanSite> sdwanSiteDetail = new ArrayList<>();
							sdwanSiteDetail = quoteIzosdwanSiteRepository
									.findByProductSolutionAndIzosdwanSiteType(solutions, type);
							List<Integer> siteid = new ArrayList<>();
							sdwanSiteDetail.stream().forEach(site -> {
								if (site.getId() != null) {
									siteid.add(site.getId());
								}
							});
							siteTypeDetail.setSiteIds(siteid);
						});

					}
				}
			}
		}
		return SiteTypeDetails;
	}

	public NetworkSummaryDetails getBandwidthDetails(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			if ((quoteId == null)) {
				throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);

			}
		}
		Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quoteDetails == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		NetworkSummaryDetails networkDetails = new NetworkSummaryDetails();
		try {
			ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteDetails.getId());
			List<QuoteIzosdwanSite> sdwanSiteDetails = getSdwanSiteDetails(solutions);
			constructBwSummaryDetails(sdwanSiteDetails, networkDetails);
			constructSiteTypeSummaryDetails(sdwanSiteDetails, networkDetails, solutions);
			/*
			 * //SFDC Trigger Point for create product ProductSolution
			 * solutionDetails=productSolutionRepository.findByReferenceIdForIzoSdwan(
			 * quoteId); List<QuoteIzoSdwanAttributeValues>
			 * createProduct=quoteIzoSdwanAttributeValuesRepository.
			 * findByDisplayValueAndQuote("CREATEPRODUCT", quoteDetails); if
			 * (createProduct.isEmpty()) { List<String> distinctSiteTypes =
			 * quoteIzosdwanSiteRepository
			 * .getDistinctSiteOfferings(solutionDetails.getId());
			 * List<QuoteIzoSdwanAttributeValues> vals =
			 * quoteIzoSdwanAttributeValuesRepository
			 * .findByDisplayValueAndQuote("BYON100P", quoteDetails); List<QuoteToLe>
			 * quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId); if
			 * (quoteToLes.stream().findFirst().isPresent()) {
			 * 
			 * QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get(); Integer iasCount
			 * = 0; Integer gvpnCount = 0; if (!vals.isEmpty()) { boolean byonStatus =
			 * Boolean.getBoolean(vals.get(0).getAttributeValue()); if (!byonStatus) { for
			 * (String val : distinctSiteTypes) { if (val.contains("Internet Access")) { if
			 * (iasCount == 0) {
			 * bundleOmsSfdcService.processProductServiceForSolution(quoteToLe,
			 * solutionDetails, quoteToLe.getTpsSfdcOptyId(), CommonConstants.IAS);// adding
			 * // productService iasCount++; } } if (val.contains("GVPN")) { if (gvpnCount
			 * == 0) { bundleOmsSfdcService.processProductServiceForSolution(quoteToLe,
			 * solutionDetails, quoteToLe.getTpsSfdcOptyId(), CommonConstants.GVPN);//
			 * adding productService gvpnCount++; } } }
			 * 
			 * } }
			 * 
			 * 
			 * } QuoteIzoSdwanAttributeValues createVal=new QuoteIzoSdwanAttributeValues();
			 * createVal.setQuote(quoteDetails); createVal.setDisplayValue("CREATEPRODUCT");
			 * createVal.setAttributeValue("true");
			 * quoteIzoSdwanAttributeValuesRepository.save(createVal); }
			 */
		} catch (Exception e) {
			LOGGER.error("Error occured on getting network summary details!!", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return networkDetails;
	}

	private void checkVendorName(QuoteDetail quoteDetail) {
		try {
			Quote quote = getQuote(quoteDetail.getQuoteId());

			if (quote.getIzosdwanFlavour().equals(quoteDetail.getVendorName())) {
				LOGGER.info("Vendor name is not changed");
			} else {
				quote.setIzosdwanFlavour(quoteDetail.getVendorName());
			}
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void constructBwSummaryDetails(List<QuoteIzosdwanSite> sdwanSiteDetails,
			NetworkSummaryDetails networkDetails) {
		List<BandwidthSummaryDetails> bandwidthSummaryDet = new ArrayList<>();
		BandwidthSummaryDetails gvpnDet = new BandwidthSummaryDetails();
		gvpnDet.setSitetype(ProductType.GVPN.toString());
		gvpnDet.setRetainedCount(0);
		gvpnDet.setUpgradedCount(0);
		bandwidthSummaryDet.add(gvpnDet);
		BandwidthSummaryDetails iasDet = new BandwidthSummaryDetails();
		iasDet.setSitetype(ProductType.IAS.toString());
		iasDet.setRetainedCount(0);
		iasDet.setUpgradedCount(0);
		bandwidthSummaryDet.add(iasDet);
		BandwidthSummaryDetails byonDet = new BandwidthSummaryDetails();
		byonDet.setSitetype(ProductType.BYON.productTypeName());
		byonDet.setRetainedCount(0);
		byonDet.setUpgradedCount(0);
		bandwidthSummaryDet.add(byonDet);
		for (QuoteIzosdwanSite siteDet : sdwanSiteDetails) {
			getBandDet(bandwidthSummaryDet, siteDet);
		}
		networkDetails.setBandwidthSummaryDe(bandwidthSummaryDet);
	}

//	public List<LocationDetail> getAddressDetails(LocationInputDetails requestBean) throws TclCommonException {
//		List<LocationDetail> locationDetails = new ArrayList<>();
//		LOGGER.info("THE DETAILS ARE {}",requestBean.getTextToSearch());
//		String locationDet = (String) mqUtils.sendAndReceive(addressDetails, Utils.convertObjectToJson(requestBean));
//		locationDetails=GscUtils.fromJson(locationDet, new TypeReference<List<LocationDetail>>() {
//			
//		});
//
//		return locationDetails;
//	}
	private void getBandDet(List<BandwidthSummaryDetails> bandwidthSummaryDet, QuoteIzosdwanSite siteDet) {
		for (BandwidthSummaryDetails details : bandwidthSummaryDet) {
			if (details.getSitetype().equals(siteDet.getIzosdwanSiteProduct())) {
				if (Double.parseDouble(siteDet.getOldPortBandwidth()) == 0
						&& Double.parseDouble(siteDet.getNewPortBandwidth()) == 0) {
					throw new NullPointerException();
				} else {
					if (Double.parseDouble(siteDet.getOldPortBandwidth()) == Double
							.parseDouble(siteDet.getNewPortBandwidth())) {
						Integer count = details.getRetainedCount();
						count++;
						details.setRetainedCount(count);
					} else {
						Integer count = details.getUpgradedCount();
						count++;
						details.setUpgradedCount(count);
					}
				}
			}
		}
	}

	private List<QuoteIzosdwanSite> getSdwanSiteDetails(ProductSolution solutions) {
		List<QuoteIzosdwanSite> siteDet = new ArrayList<>();
		try {
			siteDet = quoteIzosdwanSiteRepository.findByProductSolution(solutions);

		} catch (Exception e) {

		}
		return siteDet;
	}

	/**
	 * 
	 * processQuote- This method builds the quote workflow step by step it creates
	 * by providing the initial set of values
	 * 
	 * @param quoteDetail
	 * @param erfcustomerId
	 * @return Quote
	 * @throws TclCommonException
	 */
	protected QuoteToLe processQuote(QuoteDetail quoteDetail, Integer erfCustomerId, User user)
			throws TclCommonException {
		Customer customer = null;
		if (erfCustomerId != null) {
			customer = getCustomerId(erfCustomerId);// get the customer Id
		} else {
			customer = user.getCustomer();
		}
		Quote quote = null;
		// Checking whether the input is for creating or updating
		quote = constructQuote(customer, user.getId(), quoteDetail.getProductName(), quoteDetail.getEngagementOptyId(),
				quoteDetail.getQuoteCode(), quoteDetail.getVendorName(), quoteDetail.getQuoteId());
		LOGGER.info("Quote Details Saved {}", quote.getQuoteCode());
		LOGGER.debug("Quote Details Saved {}", quote.getQuoteCode());
		LOGGER.debug("Quote Flavour {}", quote.getIzosdwanFlavour());
		quoteRepository.save(quote);

		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote, quoteDetail);
			quoteToLeRepository.save(quoteToLe);
		} else {
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			quoteToLe = quoteToLeEntity.isPresent() ? quoteToLeEntity.get() : null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				quoteToLe.setClassification(quoteDetail.getClassification());
				quoteToLe = quoteToLeRepository.save(quoteToLe);
			}
		}

		MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
		if (quoteToLeProductFamily == null) {
			quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		} else {
			removeUnselectedSolution(quoteDetail, quoteToLeProductFamily, quoteToLe);
		}

		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			quoteToLe.setQuoteToLeProductFamilies(
					Arrays.asList(quoteToLeProductFamily).stream().collect(Collectors.toSet()));
		}

		return quoteToLe;

	}

	/**
	 * getPublicIp
	 */
	public String getPublicIp(String publicIp) {
		String[] publicIps = publicIp.split(",");
		Pattern ipPattern = Pattern.compile(CommonConstants.PUBLIC_IP_PATTERN);
		for (String ip : publicIps) {
			if (ip.contains("%3")) {
				ip = ip.replace("%3", "");
			}
			if (ipPattern.matcher(ip).matches()) {
				return ip;
			}
		}
		return null;
	}

	public QuoteResponse persistSolutionDetails(QuoteDetail quoteDetail) throws TclCommonException {
		try {

			boolean isProductUpdated = false;
			String productOffering = "";
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());

			if (quoteToLe.isPresent()) {
				User user = userRepository.findByIdAndStatus(quoteToLe.get().getQuote().getCreatedBy(),
						CommonConstants.ACTIVE);
				MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
				// updateProfileName(quoteDetail, quoteToLe.get().getQuote());
				if (quoteDetail.getVendorName().equals(quoteToLe.get().getQuote().getIzosdwanFlavour())) {
					LOGGER.info("Vendor was Not Modifed");
				} else {
					quoteToLe.get().getQuote().setIzosdwanFlavour(quoteDetail.getVendorName());
					quoteRepository.save(quoteToLe.get().getQuote());
				}
				QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
				if (quoteToLeProductFamily == null) {
					quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe.get());
					quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
				} else {
					removeUnselectedSolution(quoteDetail, quoteToLeProductFamily, quoteToLe.get());
				}

				for (ProductOfferingsBean solution : quoteDetail.getIzosdwanSolutions()) {
					productOffering = solution.getProductOfferingsName();
					MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user,
							quoteDetail.getVendorName());
					ProductSolution productSolution = productSolutionRepository
							.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
					if (productSolution == null) {
						productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
								Utils.convertObjectToJson(solution));
						productSolution.setSolutionCode(Utils.generateUid());
						List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
								.findByQuote(quoteToLe.get().getQuote());
						if (attributeValues != null && !(attributeValues.isEmpty())) {
							Optional<QuoteIzoSdwanAttributeValues> attrVal = Optional.empty();
							solution.getProductOfferingsName();
							attrVal = attributeValues.stream().filter(
									val -> val.getDisplayValue().contains(IzosdwanCommonConstants.ISPROFILEVALID))
									.findFirst();
							if (attrVal.isPresent()) {
								if ((attrVal.get().getAttributeValue().equals("false"))) {
									if (!(solution.getProductOfferingsName().equals("Basic"))) {
										attrVal.get().setAttributeValue("true");
										quoteIzoSdwanAttributeValuesRepository.save(attrVal.get());
									}
								}
							}
						}
						productSolutionRepository.save(productSolution);
						isProductUpdated = true;
//						if (StringUtils.isNotBlank(quoteToLe.get().getTpsSfdcOptyId()))
//							omsSfdcService.processProductServiceForSolution(quoteToLe.get(), productSolution,
//									quoteToLe.get().getTpsSfdcOptyId());// adding productService
//						// to Sfdc
					} else {
						productSolution.setProductProfileData(Utils.convertObjectToJson(solution));
						List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
								.findByQuote(quoteToLe.get().getQuote());
						if (attributeValues != null && !(attributeValues.isEmpty())) {
							Optional<QuoteIzoSdwanAttributeValues> attrVal = Optional.empty();
							solution.getProductOfferingsName();
							attrVal = attributeValues.stream().filter(
									val -> val.getDisplayValue().contains(IzosdwanCommonConstants.ISPROFILEVALID))
									.findFirst();
							if (attrVal.isPresent()) {
								if ((attrVal.get().getAttributeValue().equals("false"))) {
									if (!(solution.getProductOfferingsName().equals("Basic"))) {
										attrVal.get().setAttributeValue("true");
										quoteIzoSdwanAttributeValuesRepository.save(attrVal.get());
									}
								}
							}
						}
						productSolutionRepository.save(productSolution);
						List<QuoteIzosdwanSite> illSites = quoteIzosdwanSiteRepository
								.findByProductSolutionAndStatus(productSolution, CommonConstants.BACTIVE);
						for (QuoteIzosdwanSite quoteIllSite : illSites) {
							quoteIllSite.setProductSolution(productSolution);
							quoteIzosdwanSiteRepository.save(quoteIllSite);
//							removeComponentsAndAttr(quoteIllSite.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES);
//							for (ComponentDetail componentDetail : solution.getComponents()) {
//								processProductComponent(productFamily, quoteIllSite, componentDetail, user);
//							}
							// Initializing siteProperty
							MstProductComponent sitePropComp = getMstProperties(user);
							List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
									.findByReferenceIdAndReferenceNameAndMstProductComponent(quoteIllSite.getId(),
											QuoteConstants.IZOSDWAN_SITES.toString(), sitePropComp);
							if (quoteProductComponents.isEmpty()) {
								QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
								quoteProductComponent.setMstProductComponent(sitePropComp);
								quoteProductComponent.setReferenceId(quoteIllSite.getId());
								quoteProductComponent.setReferenceName(QuoteConstants.IZOSDWAN_SITES.toString());
								quoteProductComponent.setMstProductFamily(productFamily);
								quoteProductComponentRepository.save(quoteProductComponent);
							}
						}
					}
				}

				Optional<Quote> quote = quoteRepository.findById(quoteDetail.getQuoteId());
				if (quote.get() != null
						&& (quote.get().getNsQuote() == null || quote.get().getNsQuote().equalsIgnoreCase("N"))) {
					LOGGER.info("Quote Created through standar Journey");
					persistIzoSdwanSiteDetails(quoteDetail.getQuoteId(), quoteToLe.get());
				}
				if (isProductUpdated) {
					ProductSolution productSolution = productSolutionRepository
							.findByReferenceIdForIzoSdwan(quoteToLe.get().getQuote().getId());
					bundleOmsSfdcService.processProductServiceForSolution(quoteToLe.get(), productSolution,
							quoteToLe.get().getTpsSfdcOptyId(), CommonConstants.SDWAN,false);// adding productService

				}
				QuoteResponse quoteResponse = new QuoteResponse();
				quoteResponse.setQuoteId(quoteDetail.getQuoteId());
				quoteResponse.setQuoteleId(quoteDetail.getQuoteleId());
				return quoteResponse;
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

//	private void updateProfileName(QuoteDetail quoteDetail, Quote quote) {
//		QuoteIzoSdwanAttributeValues val = quoteIzoSdwanAttributeValuesRepository
//				.findByDisplayValueAndQuote(CommonConstants.SUGGESTEDPROFILENAME, quote);
//		if (val != null) {
//			val.setAttributeValue(quoteDetail.getIzosdwanSolutions().get(0).getProductOfferingsName());
//			quoteIzoSdwanAttributeValuesRepository.save(val);
//		}
//	}

	/**
	 * processProductComponent- This method process the product component details
	 * 
	 * @param productFamily
	 * @param illSite
	 * @param component
	 * @param user
	 * @throws TclCommonException
	 */
	private void processProductComponent(MstProductFamily productFamily, QuoteIllSite illSite,
			ComponentDetail component, User user) throws TclCommonException {
		try {
			MstProductComponent productComponent = getProductComponent(component, user);
			QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily,
					illSite.getId());
			quoteComponent.setType(component.getType());
			quoteProductComponentRepository.save(quoteComponent);
			LOGGER.info("saved successfully");
			for (AttributeDetail attribute : component.getAttributes()) {
				processProductAttribute(quoteComponent, attribute, user);
			}
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @link http://www.tatacommunications.com/ constructMstProperties used to
	 *       construct Mst Properties
	 * @param id
	 * @param localITContactId
	 */
	private MstProductComponent getMstProperties(User user) {

		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setDescription(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;

	}

	/**
	 * processProductAttribute- This method process the product attributes
	 * 
	 * @param quoteComponent
	 * @param attribute
	 * @param user
	 * @throws TclCommonException
	 */
	private void processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user)
			throws TclCommonException {
		try {
			ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
			QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
					productAttribute, attribute);
			quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.INVALID_ATTRIBUTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @throws TclCommonException
	 */
	private Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, List<Integer> siteIds) throws TclCommonException {

		Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
		if ((quote != null) && (getQuoteToLeBasenOnVersion(quote)) != null) {
			for (QuoteToLe quTle : getQuoteToLeBasenOnVersion(quote)) {
				QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
				quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
				quoteToLeDto.setCurrency(quTle.getCurrencyCode());
				quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
				quoteToLeDto.setProductFamilies(constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle),
						isFeasibleSites, isSiteProperitiesRequired, siteId, siteIds, quote));
				quoteToLeDto.setClassification(quTle.getClassification());
				quoteToLeDtos.add(quoteToLeDto);
				partnerService.setExpectedArcAndNrcForPartnerQuote(quote.getQuoteCode(), quoteToLeDto);
			}
		}

		return quoteToLeDtos;

	}

	/**
	 * @link http://www.tatacommunications.com constructLegalAttributes used to
	 *       construct legal attributes
	 * @param quTle
	 * @return
	 */
	private Set<LegalAttributeBean> constructLegalAttributes(QuoteToLe quTle) {

		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		List<QuoteLeAttributeValue> attributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quTle);
		if (attributeValues != null) {

			attributeValues.stream().forEach(attrVal -> {
				LegalAttributeBean attributeBean = new LegalAttributeBean();

				attributeBean.setAttributeValue(attrVal.getAttributeValue());
				attributeBean.setDisplayValue(attrVal.getDisplayValue());
				attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
				leAttributeBeans.add(attributeBean);

			});

		}
		return leAttributeBeans;
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteToLeProductFamily> getProductFamilyBasenOnVersion(QuoteToLe quote) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		return prodFamilys;

	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	protected List<QuoteToLe> getQuoteToLeBasenOnVersion(Quote quote) {
		List<QuoteToLe> quToLes = null;
		quToLes = quoteToLeRepository.findByQuote(quote);
		return quToLes;

	}

	/**
	 * constructMstAttributBean
	 * 
	 * @param mstOmsAttribute
	 * @return
	 */
	private MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setName(mstOmsAttribute.getName());
			mstOmsAttributeBean.setId(mstOmsAttribute.getId());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		}
		return mstOmsAttributeBean;
	}

	/**
	 * 
	 * constructProductAttribute- This method constructs the
	 * {@link QuoteProductComponentsAttributeValue} Entity
	 * 
	 * @param quoteProductComponent
	 * @param productAttributeMaster
	 * @param attributeDetail
	 * @return QuoteProductComponentsAttributeValue
	 */
	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, AttributeDetail attributeDetail) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;

	}

	/**
	 * 
	 * getProductAttributes-This methods takes the attributeName and gets back
	 * {@link ProductAttributeMaster}
	 * 
	 * @param user
	 * 
	 * @param attributeName
	 * @return ProductAttributeMaster
	 * @throws TclCommonException
	 */
	private ProductAttributeMaster getProductAttributes(AttributeDetail attributeDetail, User user)
			throws TclCommonException {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
	}

	/**
	 * @link http://www.tatacommunications.com/ constructQuoteToLeFamilyDtos
	 * @param quoteToLeProductFamilies
	 * @throws TclCommonException
	 */
	private Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
			List<QuoteToLeProductFamily> quoteToLeProductFamilies, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, List<Integer> siteIds, Quote quote)
			throws TclCommonException {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
		if (quoteToLeProductFamilies != null) {
			for (QuoteToLeProductFamily quFamily : quoteToLeProductFamilies) {
				Boolean isVproxy = false;
				QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
				if (quFamily.getMstProductFamily() != null) {
					quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
					quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
					isVproxy = quFamily.getMstProductFamily().getName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY)
							? true
							: false;
					if (isVproxy) {
						quoteToLeProductFamilyBean
								.setComponents(constructQuoteProductComponentVproxy(quFamily.getId(), false, false));
					}
				}
				List<ProductSolutionBean> solutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(quFamily), isFeasibleSites,
								isSiteProperitiesRequired, siteId, siteIds, isVproxy, quote));
				quoteToLeProductFamilyBean.setSolutions(solutionBeans);
				quoteToLeProductFamilyBean.setId(quFamily.getId());
				quoteToLeProductFamilyBeans.add(quoteToLeProductFamilyBean);

			}
		}

		return quoteToLeProductFamilyBeans;
	}

	/**
	 * 
	 * constructProductComponent- This method constructs the
	 * {@link QuoteProductComponent} Entity
	 * 
	 * @param productComponent
	 * @param mstProductFamily
	 * @param illSiteId
	 * @return QuoteProductComponent
	 * @throws TclCommonException
	 */
	private QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.IZOSDWAN_SITES.toString());
		return quoteProductComponent;

	}

	/**
	 * 
	 * getProductComponent- This method takes the component name and gives the
	 * {@link MstProductComponent}
	 * 
	 * @param user
	 * 
	 * @param componentName
	 * @return MstProductComponent
	 * @throws TclCommonException
	 */
	private MstProductComponent getProductComponent(ComponentDetail component, User user) throws TclCommonException {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(component.getName(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setName(component.getName());
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}

		return mstProductComponent;
	}

	/**
	 * 
	 * @param file
	 * @param response
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @author vpachava
	 * @throws TclCommonException
	 */
	@SuppressWarnings("null")
	public ByonUploadResponse uploadIzosdwanByonExcel(MultipartFile file, Integer quoteId)
			throws EncryptedDocumentException, InvalidFormatException, IOException, TclCommonException {
		validateGetQuoteDetail(quoteId);
		List<QuoteIzoSdwanAttributeValues> attributeValues = new ArrayList<>();
		Quote quote = getQuote(quoteId);
		Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
		Sheet sheet = workbook.getSheetAt(0);
		int Lastcellnum = sheet.getRow(0).getLastCellNum();
		LOGGER.info("last row num with data => {} ", getLastRowWithData(sheet));
		LOGGER.info("Last cell number is {}", Lastcellnum);
		// Getting last cell name if it is error cell then remove that cell.
		XSSFCell cellName = (XSSFCell) sheet.getRow(0).getCell((Lastcellnum - 1),
				MissingCellPolicy.RETURN_BLANK_AS_NULL);
		List<ByonBulkUploadDetail> byonUploadDetails = new ArrayList<>();
		LOGGER.info("Cell name is  {}", cellName);
		String attrValue = "";
		List<QuoteIzoSdwanAttributeValues> izoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
				.findByQuote(quote);
		Optional<QuoteIzoSdwanAttributeValues> sdwanVal = izoSdwanAttributeValues.stream()
				.filter(sdwanAttrVal -> sdwanAttrVal.getDisplayValue().equals(IzosdwanCommonConstants.BYON100P))
				.findFirst();
		if (sdwanVal.isPresent()) {
			attrValue = sdwanVal.get().getAttributeValue();
		}
		// Read data from the uploaded excel sheet
		readCellData(sheet, byonUploadDetails, attrValue);
		// return if sheet is empty
		if (byonUploadDetails.size() == 0) {
			ByonUploadResponse response = new ByonUploadResponse();
			response.setFileEmpty(true);
			return response;

		}
		Boolean profileValidationRequired = false;
		for (ByonBulkUploadDetail byonBulkUploadDetail : byonUploadDetails) {
			if (byonBulkUploadDetail.getSiteType() != null && !byonBulkUploadDetail.getSiteType().isEmpty()) {
				if (byonBulkUploadDetail.getSiteType().contains("Dual CPE")) {
					profileValidationRequired = true;
					break;
				}
			}
		}
		LOGGER.info("profileValidationRequired:{}", profileValidationRequired);
		// Optional<ByonBulkUploadDetail>
		// profileValidation=byonUploadDetails.stream().filter(profile->profile.getSiteType().contains("Dual
		// CPE")).findAny();
		checkforValidInputDetails(byonUploadDetails, quote);
		// Persisting data into the data base
		List<QuoteIzosdwanByonUploadDetail> quoteIzoSdwanByon = persistByonUploadDetails(byonUploadDetails, quote,
				attrValue);
		boolean containsWordStatus = false;
		for (QuoteIzosdwanByonUploadDetail byonVal : quoteIzoSdwanByon) {
			if (byonVal.getErrorDetails().contains("can't be")) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORFORRETAIL)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORFORBANDWIDTH)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORFORPRIMARYINTERFACETYPE)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORFORSECONDARYINTERFACETYPE)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(CommonConstants.IASSITENOTEXIST)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(CommonConstants.GVPNSITENOTEXIST)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(CommonConstants.DUALCPESECNARIRO)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(CommonConstants.EXISTIINGADDRESS)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(CommonConstants.PRIMARYSECONDARY)) {
				containsWordStatus = true;

			}
			if (byonVal.getErrorDetails().contains(CommonConstants.BYONSITEADDRESS)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(CommonConstants.PRIMARYSECONDARYSINGLE)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORPRIMARYPORTMODE)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORSECONDARYPORTMODE)) {
				containsWordStatus = true;
			}
			if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORFORCOUNTRY)) {
				containsWordStatus = true;
			}
		}
		// IF present Goto Excel sheet if not validate rules and store in data base
		LOGGER.info("SIZE OF BYON LIST IS {}", byonUploadDetails.size());

		try {
			attributeValues = quoteIzoSdwanAttributeValuesRepository.findByQuote(quote);
			Optional<QuoteIzoSdwanAttributeValues> hasErrorVal = attributeValues.stream()
					.filter(s -> s.getDisplayValue().equals(IzosdwanCommonConstants.HASERROR)).findAny();
			Optional<QuoteIzoSdwanAttributeValues> isProfileVal = attributeValues.stream()
					.filter(s -> s.getDisplayValue().equals(IzosdwanCommonConstants.ISPROFILEVALID)).findAny();
			if (!(hasErrorVal.isPresent())) {
				QuoteIzoSdwanAttributeValues val = new QuoteIzoSdwanAttributeValues();
				val.setDisplayValue(IzosdwanCommonConstants.HASERROR);
				val.setQuote(quote);
				attributeValues.add(val);
			}
			if (!(isProfileVal.isPresent())) {
				QuoteIzoSdwanAttributeValues val1 = new QuoteIzoSdwanAttributeValues();
				val1.setDisplayValue(IzosdwanCommonConstants.ISPROFILEVALID);
				val1.setQuote(quote);
				attributeValues.add(val1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		// izoSdwanAttributeValues.setDisplayValue(CommonConstants.HASERROR.toString());
		if (containsWordStatus) {
			attributeValues.stream().filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.HASERROR))
					.findFirst().get().setAttributeValue("true");

		} else {
			attributeValues.stream().filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.HASERROR))
					.findFirst().get().setAttributeValue("false");
			;

		}
		if (!containsWordStatus) {
			if (profileValidationRequired) {
				List<ProductSolution> solutions = productSolutionRepository.findByReferenceCode(quote.getQuoteCode());
				for (ProductSolution solution : solutions) {
					MstProductOffering productOffering = solution.getMstProductOffering();
					if (productOffering.getProductName().equals("Basic")) {
						attributeValues.stream()
								.filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.ISPROFILEVALID))
								.findFirst().get().setAttributeValue("false");
					} else {
						attributeValues.stream()
								.filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.ISPROFILEVALID))
								.findFirst().get().setAttributeValue("true");
					}
					// quoteIzoSdwanAttributeValuesRepository.saveAll(attributeValues);
				}

			} else {
				attributeValues.stream()
						.filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.ISPROFILEVALID))
						.findFirst().get().setAttributeValue("true");
			}
		} else {
			attributeValues.stream()
					.filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.ISPROFILEVALID)).findFirst()
					.get().setAttributeValue("true");
		}
		quoteIzoSdwanAttributeValuesRepository.saveAll(attributeValues);

		ByonUploadResponse byonresponse = new ByonUploadResponse();
		try {
			attributeValues = quoteIzoSdwanAttributeValuesRepository.findByQuote(quote);
			attributeValues.stream().forEach(val -> {
				if (val.getDisplayValue().equals(IzosdwanCommonConstants.HASERROR)) {
					byonresponse.setByonUploadError(Boolean.parseBoolean(val.getAttributeValue()));
				}
				if (val.getDisplayValue().equals(IzosdwanCommonConstants.ISPROFILEVALID)) {
					byonresponse.setByonProfileValid(Boolean.parseBoolean(val.getAttributeValue()));
				}
			});
		} catch (Exception e) {
			LOGGER.info("Attribute details was not found for the specified quote ", e);
		}

		return byonresponse;

	}

	private void checkforValidInputDetails(List<ByonBulkUploadDetail> byonUploadDetails, Quote quote)
			throws TclCommonException {
		for (ByonBulkUploadDetail byonUploadDet : byonUploadDetails) {
			// checking for Internet Quality
			if (byonUploadDet.getInternetQuality() != null) {
				if (byonUploadDet.getInternetQuality().equals(IzosdwanCommonConstants.RETAILGRADE)) {
					if (byonUploadDet.getErrorMessageToDisplay().isEmpty()) {
						byonUploadDet.setErrorMessageToDisplay("");
					}
					byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
							.concat(IzosdwanCommonConstants.ERRORFORRETAIL + ","));
				}
			}

			if (byonUploadDet.getSiteType() != null && !byonUploadDet.getSiteType().isEmpty()) {
				// checking bandwidth details and interface type for primary
				if (byonUploadDet.getPrimaryPortBandwidth() != null
						&& !byonUploadDet.getPrimaryPortBandwidth().isEmpty()
						&& byonUploadDet.getPrimaryLocaLoopBandwidth() != null
						&& !byonUploadDet.getPrimaryLocaLoopBandwidth().isEmpty()) {
					String portBandwidth = byonUploadDet.getPrimaryPortBandwidth();
					portBandwidth = portBandwidth.replace("Mbps", " ").trim();
					String localLoopBandwidth = byonUploadDet.getPrimaryLocaLoopBandwidth();
					if (localLoopBandwidth.contains("(")) {
						String[] localLoopBand = localLoopBandwidth.split("()");
						Integer indexOfLastNumber = localLoopBandwidth.indexOf("(");
						String bandwidth = getBandwidth(localLoopBand, indexOfLastNumber);
						LOGGER.info("BANDWIDTH IS {}", bandwidth);
						// localLoopBand.
						if (!(Integer.parseInt(bandwidth) >= Integer.parseInt(portBandwidth))) {
							byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
									.concat(IzosdwanCommonConstants.ERRORFORBANDWIDTH));
						}
						// interface type
						validateInterface(Integer.parseInt(bandwidth), byonUploadDet, quote, "primary");
					} else {
						String[] localLoopBand = localLoopBandwidth.split(" ");
						if (!(Integer.parseInt(localLoopBand[0]) >= Integer.parseInt(portBandwidth))) {
							byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
									.concat(IzosdwanCommonConstants.ERRORFORBANDWIDTH));
						}
						validateInterface(Integer.parseInt(localLoopBand[0]), byonUploadDet, quote, "primary");
					}
				}
				// port mode validation
				if (byonUploadDet.getPrimaryPortMode() != null && !byonUploadDet.getPrimaryPortMode().isEmpty()) {
					if (byonUploadDet.getPrimaryPortMode()
							.equalsIgnoreCase(IzosdwanCommonConstants.HEADER_PORTMODEPASSIVE)) {
						byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
								.concat(IzosdwanCommonConstants.ERRORPRIMARYPORTMODE + ","));
					}
				}
			}

			// Checking bandwidth details and interface type for secondary
			if (byonUploadDet.getSiteType() != null && !byonUploadDet.getSiteType().isEmpty()) {
				// if
				// (!(byonUploadDet.getSiteType().contains(IzosdwanCommonConstants.SINGLECPE)))
				// {
				if (byonUploadDet.getSecondaryPortBandwidth() != null
						&& !byonUploadDet.getSecondaryPortBandwidth().isEmpty()
						&& byonUploadDet.getSecondaryLocaLoopBandwidth() != null
						&& !byonUploadDet.getSecondaryLocaLoopBandwidth().isEmpty()) {
					String portBandwidth = byonUploadDet.getSecondaryPortBandwidth();
					portBandwidth = portBandwidth.replace("Mbps", " ").trim();
					String localLoopBandwidth = byonUploadDet.getSecondaryLocaLoopBandwidth();
					if (localLoopBandwidth.contains("(")) {
						String[] localLoopBand = localLoopBandwidth.split("()");
						Integer indexOfLastNumber = (localLoopBandwidth.indexOf("("));
						String bandwidth = getBandwidth(localLoopBand, indexOfLastNumber);
						if (!(Integer.parseInt(bandwidth) >= Integer.parseInt(portBandwidth))) {
							byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
									.concat(IzosdwanCommonConstants.ERRORFORBANDWIDTH));
						}
						// interface type
						validateInterface(Integer.parseInt(bandwidth), byonUploadDet, quote, "secondary");
					} else {
						String[] localLoopBand = localLoopBandwidth.split(" ");
						if (!(Integer.parseInt(localLoopBand[0]) >= Integer.parseInt(portBandwidth))) {
							byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
									.concat(IzosdwanCommonConstants.ERRORFORBANDWIDTH));
						}
						// interface type
						validateInterface(Integer.parseInt(localLoopBand[0]), byonUploadDet, quote, "secondary");
					}
				}
				// port mode validation
				if (byonUploadDet.getSecondaryPortMode() != null && !byonUploadDet.getSecondaryPortMode().isEmpty()) {
					if (byonUploadDet.getSecondaryPortMode()
							.equalsIgnoreCase(IzosdwanCommonConstants.HEADER_PORTMODEACTIVE)) {
						byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
								.concat(IzosdwanCommonConstants.ERRORSECONDARYPORTMODE + ","));
					}
				}
				// }
			}
			// checking for valid country
			if (byonUploadDet.getCountry() != null && !byonUploadDet.getCountry().isEmpty()) {
				String response = (String) mqUtils.sendAndReceive(productLocations,
						IzosdwanCommonConstants.IZOSDWAN_NAME);
				List<String> locations = GscUtils.fromJson(response, new TypeReference<List<String>>() {
				});
				if (locations != null && !locations.isEmpty()) {
					if (!locations.contains(byonUploadDet.getCountry())) {
						byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
								.concat(IzosdwanCommonConstants.ERRORFORCOUNTRY + ","));
					}
				}
			}
			//checking for valid country
			if(byonUploadDet.getCountry()!=null && !byonUploadDet.getCountry().isEmpty()) {
				String response = (String) mqUtils.sendAndReceive(productLocations, IzosdwanCommonConstants.IZOSDWAN_NAME);
				List<String> locations = GscUtils.fromJson(response,
						new TypeReference<List<String>>() {
						});
				if(locations!=null && !locations.isEmpty()) {
					if(!locations.contains(byonUploadDet.getCountry())) {
						byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
								.concat(IzosdwanCommonConstants.ERRORFORCOUNTRY+ ","));
					}
				}
			}
		}
	}

	private void validateInterface(Integer bandwidth, ByonBulkUploadDetail byonUploadDet, Quote quote, String type)
			throws TclCommonException {
		String response = (String) mqUtils.sendAndReceive(interfaceType, quote.getIzosdwanFlavour());
		List<IzosdwanBandwidthInterface> bwInterfaceTypes = GscUtils.fromJson(response,
				new TypeReference<List<IzosdwanBandwidthInterface>>() {
				});
		List<String> interfaceType = new ArrayList<>();
		if (bwInterfaceTypes != null && !bwInterfaceTypes.isEmpty()) {
			interfaceType = bwInterfaceTypes.stream().filter(intType -> intType.getMinBw() >= bandwidth)
					.map(intType -> intType.getInterfaceType()).collect(Collectors.toList());
		}
		if (type.equalsIgnoreCase(IzosdwanCommonConstants.PRIMARY)) {

			if (byonUploadDet.getPrimaryInterfaceType() != null && !byonUploadDet.getPrimaryInterfaceType().isEmpty()
					&& interfaceType != null && !interfaceType.isEmpty()) {
				if (!interfaceType.contains(byonUploadDet.getPrimaryInterfaceType())) {
					byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
							.concat(IzosdwanCommonConstants.ERRORFORPRIMARYINTERFACETYPE + ","));
				}
			}
		}

		else {

			if (byonUploadDet.getSecondaryInterfaceType() != null
					&& !byonUploadDet.getSecondaryInterfaceType().isEmpty() && interfaceType != null
					&& !interfaceType.isEmpty()) {
				if (!interfaceType.contains(byonUploadDet.getSecondaryInterfaceType())) {
					byonUploadDet.setErrorMessageToDisplay(byonUploadDet.getErrorMessageToDisplay()
							.concat(IzosdwanCommonConstants.ERRORFORSECONDARYINTERFACETYPE + ","));
				}
			}
		}
	}

	private String getBandwidth(String[] bandwidth, Integer index) {
		String newBandwidth = "";
		Integer first = 0;
		while (first < index) {
			newBandwidth += bandwidth[first];
			first++;
		}
		return newBandwidth;
	}

	// izosdwan download excel
	@SuppressWarnings("unchecked")
	public void downloadIzoSdwanByonTemplate(LocationTemplateRequest locationTemplate, HttpServletResponse response,
			Boolean isTemplateDownload, Integer quoteId) throws IOException, TclCommonException {
		String headerName;
		Integer index;
		ArrayList<String> country = (ArrayList<String>) locationTemplate.getCountries();
		ArrayList<String> internetQuality = new ArrayList<>(Arrays.asList("Enterprise Grade", "Retail Grade"));
		ArrayList<String> managementOption = new ArrayList<>(
				Arrays.asList(IzosdwanCommonConstants.FULLY_MANAGED, IzosdwanCommonConstants.PROACTIVE_MONITORING));
//		ArrayList<String> topology = new ArrayList<>(Arrays.asList(IzosdwanCommonConstants.HUB,
//				IzosdwanCommonConstants.SPOKE));
		ArrayList<String> byonLTE = new ArrayList<>(
				Arrays.asList(IzosdwanCommonConstants.YES, IzosdwanCommonConstants.NO));

		// Single GVPN Single BYON Single / Dual CPE & Single IAS Single BYON
		// single/Dual CPE
		ArrayList<String> portModePrimary = new ArrayList<>(
				Arrays.asList(IzosdwanCommonConstants.HEADER_PORTMODEACTIVE));
		ArrayList<String> portModeSecondary = new ArrayList<>(
				Arrays.asList(IzosdwanCommonConstants.HEADER_PORTMODEPASSIVE));
		ArrayList<String> acessType = new ArrayList<>(Arrays.asList(IzosdwanCommonConstants.HEADER_ACESSTYPEWIRELINE,
				IzosdwanCommonConstants.HEADER_ACCESSTYPEWIRELESS));
		ArrayList<String> localLoopBandwidth = new ArrayList<>();
		ArrayList<String> bandwidth = new ArrayList<>();
		ArrayList<String> dropdownvalues = new ArrayList<>();
		// Getting interface details through queue call
		Quote quote = getQuote(quoteId);
		List<String> interfaceTypeDet = new ArrayList<>();
		String queueCallResponse = (String) mqUtils.sendAndReceive(interfaceType, quote.getIzosdwanFlavour());
		List<IzosdwanBandwidthInterface> bwInterfaceTypes = GscUtils.fromJson(queueCallResponse,
				new TypeReference<List<IzosdwanBandwidthInterface>>() {
				});
		if (bwInterfaceTypes != null && !bwInterfaceTypes.isEmpty()) {
			interfaceTypeDet = bwInterfaceTypes.stream().map(type -> type.getInterfaceType())
					.collect(Collectors.toList());
		}
		// upload sheet
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet ByonUpload = workbook.createSheet(IzosdwanCommonConstants.BYON_UPLOAD);
			Row header = ByonUpload.createRow(0);
			/**** header ***********/
			header.createCell(0).setCellValue(IzosdwanCommonConstants.HEADER_SERIAL_NO);
			/*** Adding few columns ***/
			header.createCell(1).setCellValue(IzosdwanCommonConstants.HEADER_EXISTINGADDRESS);
			/*** End of added new columns ***/
			header.createCell(2).setCellValue(IzosdwanCommonConstants.HEADER_COUNTRY);
			header.createCell(3).setCellValue(IzosdwanCommonConstants.HEADER_STATE);
			header.createCell(4).setCellValue(IzosdwanCommonConstants.HEADER_CITY);
			header.createCell(5).setCellValue(IzosdwanCommonConstants.HEADER_PINCODE);
			header.createCell(6).setCellValue(IzosdwanCommonConstants.HEADER_LOCALITY);
			header.createCell(7).setCellValue(IzosdwanCommonConstants.HEADER_ADDRESS);
			header.createCell(8).setCellValue(IzosdwanCommonConstants.HEADER_INTERNETQUALITY);
			header.createCell(9).setCellValue(IzosdwanCommonConstants.MANAGEMENT_OPTION);
			header.createCell(10).setCellValue(IzosdwanCommonConstants.HEADER_SITETYPE);
			for (int i = 0; i < 2; i++) {
				if (i == 0) {
					headerName = IzosdwanCommonConstants.HEADER_PRIMARY;
					index = 11;
				} else {
					headerName = IzosdwanCommonConstants.HEADER_SECONDARY;
					index = 21;
				}
				header.createCell(index++)
						.setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_PORTBANDWIDTH);
				header.createCell(index++)
						.setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_LOCALLOOPBANDWDTH);
				header.createCell(index++).setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_PORTMODE);
				header.createCell(index++).setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_ACCESSTYPE);
				header.createCell(index++)
						.setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_INTERFACETYPE);
				header.createCell(index++)
						.setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_THIRD_PARTY_SERVICEID);
				header.createCell(index++)
						.setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_THIRD_PARTY_WAN_IP);
				header.createCell(index++)
						.setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_THIRD_PARTY_PROVIDER_NAME);
				header.createCell(index++)
						.setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_THIRD_PARTY_LINKTYPEAGGREEMENT);
				header.createCell(index).setCellValue(headerName + " " + IzosdwanCommonConstants.HEADER_BYON_4G_LTE);
			}

			/***** header ends ******/
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				if (i == 1) {
					ByonUpload.setColumnWidth(i, 35 * 256);
				}
				if (i == 6 || i == 7 || i == 8 || i == 9 || i == 10) {
					ByonUpload.setColumnWidth(i, 35 * 256);
				}
				if (i > 1 && i <= 5) {
					ByonUpload.setColumnWidth(i, 25 * 256);
				}
				if (i > 10) {
					if (i == 13 || i == 22) {
						ByonUpload.setColumnWidth(i, 21 * 256);
					} else if (i == 16 || i == 17 || i == 18 || i == 25 || i == 26 || i == 27) {
						ByonUpload.setColumnWidth(i, 35 * 256);
					} else {
						ByonUpload.setColumnWidth(i, 15 * 256);
					}

				}
				CellStyle stylerowHeading = workbook.createCellStyle();
				stylerowHeading.setBorderLeft(BorderStyle.THICK);
				stylerowHeading.setBorderRight(BorderStyle.THICK);
				stylerowHeading.setBorderBottom(BorderStyle.THICK);
				stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
				stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setFontHeightInPoints((short) 10);
				XSSFColor myColor = new XSSFColor(java.awt.Color.WHITE);
				font.setColor(myColor);
				stylerowHeading.setFont(font);
				stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
				stylerowHeading.setAlignment(HorizontalAlignment.CENTER);
				stylerowHeading.setWrapText(true);
				header.getCell(i).setCellStyle(stylerowHeading);
				DataFormat fmt = workbook.createDataFormat();
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
				textStyle.setWrapText(true);
				ByonUpload.setDefaultColumnStyle(i, textStyle);
			}
			ArrayList<String> address = new ArrayList<>();
			String attrValue = "";
			List<QuoteIzoSdwanAttributeValues> izoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByQuote(quote);
			Optional<QuoteIzoSdwanAttributeValues> sdwanVal = izoSdwanAttributeValues.stream()
					.filter(sdwanAttrVal -> sdwanAttrVal.getDisplayValue().equals(IzosdwanCommonConstants.BYON100P))
					.findFirst();
			if (sdwanVal.isPresent()) {
				attrValue = sdwanVal.get().getAttributeValue();
				if (attrValue.equals("false")) {
					List<ProductSolution> solutions = productSolutionRepository
							.findByReferenceCode(quote.getQuoteCode());
					if (solutions != null) {
						for (ProductSolution solution : solutions) {
							List<String> iasAddressDetails = quoteIzosdwanSiteRepository
									.selectServiceSiteAddressByProductSolutionAndIzosdwanSiteType(solution.getId(),
											"Single IAS Single CPE");
							List<String> gvpnAddressDetails = quoteIzosdwanSiteRepository
									.selectServiceSiteAddressByProductSolutionAndIzosdwanSiteType(solution.getId(),
											"Single GVPN Single CPE");
							if (iasAddressDetails != null) {
								address.addAll(iasAddressDetails);

							}
							if (gvpnAddressDetails != null) {
								address.addAll(gvpnAddressDetails);
							}

						}
					}
				}
			}
			ArrayList<String> siteType = new ArrayList<>();
			if (attrValue.equals("true")) {
				siteType = new ArrayList<>(Arrays.asList("Single BYON Internet Single CPE",
						"Dual BYON Internet Single CPE", "Dual BYON Internet Dual CPE"));
			} else {
				siteType = new ArrayList<>(Arrays.asList("Single BYON Internet Single CPE",
						"Dual BYON Internet Single CPE", "Dual BYON Internet Dual CPE",
						"Single GVPN Single BYON Internet Single CPE", "Single GVPN Single BYON Internet Dual CPE",
						"Single IAS Single BYON Internet Single CPE", "Single IAS Single BYON Internet Dual CPE"));
			}
			/********* country sheet *********/
			XSSFSheet countrySheet = workbook.createSheet("countrySheet");
			for (int i = 0, length = country.size(); i < length; i++) {
				String name = country.get(i);
				XSSFRow row = countrySheet.createRow(i);
				XSSFCell cell = row.createCell(0);
				cell.setCellValue(name);
				if (i < bandwidth.size()) {
					String name1 = bandwidth.get(i);
					XSSFCell cell1 = row.createCell(4);
					cell1.setCellValue(name1);
				}

			}
			Integer lengthAddr = address.size();
			for (int i = 0; i < lengthAddr; i++) {
				String name = address.get(i);
				XSSFRow row = countrySheet.getRow(i);
				XSSFCell cell = row.createCell(1);
				cell.setCellValue(name);
			}

			for (int i = 0; i < siteType.size(); i++) {
				String name = siteType.get(i);
				XSSFRow row = countrySheet.getRow(i);
				XSSFCell cell = row.createCell(8);
				cell.setCellValue(name);
			}
			Name namedCell = workbook.createName();
			namedCell.setNameName("countrySheet");
			namedCell.setRefersToFormula("countrySheet!$A$1:$A$" + country.size());
			Name namedCellSite = workbook.createName();
			namedCellSite.setNameName("SiteTypeSheet");
			namedCellSite.setRefersToFormula("countrySheet!$I$1:$I$" + siteType.size());
			if (attrValue.equals("false")) {
				Name namedCellAddr = workbook.createName();
				namedCellAddr.setNameName("AddressSheet");
				namedCellAddr.setRefersToFormula("countrySheet!$B$1:$B$" + address.size());
			}
			ByonUpload.addValidationData(izoSdwandataValidation(dropdownvalues, "AddressSheet", ByonUpload, 1, 1));
			ByonUpload.addValidationData(izoSdwandataValidation(dropdownvalues, "countrySheet", ByonUpload, 2, 2));

			ByonUpload.addValidationData(izoSdwandataValidation(internetQuality, "", ByonUpload, 8, 8));
			ByonUpload.addValidationData(izoSdwandataValidation(managementOption, "", ByonUpload, 9, 9));
//			ByonUpload.addValidationData(izoSdwandataValidation(topology, "", ByonUpload, 10, 10));
			ByonUpload.addValidationData(izoSdwandataValidation(dropdownvalues, "SiteTypeSheet", ByonUpload, 10, 10));

			// bandwidth sheet
			bandwidth = new ArrayList<>(Arrays.asList("2 Mbps", "3 Mbps", "4 Mbps", "5 Mbps", "6 Mbps", "8 Mbps",
					"10 Mbps", "15 Mbps", "20 Mbps", "25 Mbps", "30 Mbps", "35 Mbps", "40 Mbps", "45 Mbps", "50 Mbps",
					"60 Mbps", "70 Mbps", "100 Mbps", "200 Mbps", "250 Mbps", "300 Mbps", "400 Mbps", "500 Mbps",
					"600 Mbps", "620 Mbps", "750 Mbps", "1000 Mbps", "2000 Mbps", "3000 Mbps", "4000 Mbps", "5000 Mbps",
					"6000 Mbps", "7000 Mbps", "8000 Mbps", "9000 Mbps", "10000 Mbps"));
			localLoopBandwidth = new ArrayList<>(Arrays.asList("2 Mbps", "4 Mbps", "6 Mbps", "8 Mbps", "10 Mbps",
					"16 Mbps", "20 Mbps", "26 Mbps", "30 Mbps", "36 Mbps", "40 Mbps", "45(DS3) Mbps", "46 Mbps",
					"50 Mbps", "60 Mbps", "70 Mbps", "100 Mbps", "150 Mbps", "155(STM1) Mbps", "156 Mbps", "200 Mbps",
					"250 Mbps", "300 Mbps", "400 Mbps", "500 Mbps", "600 Mbps", "622(STM 4) Mbps", "750 Mbps",
					"1000 Mbps", "2000 Mbps", "3000 Mbps", "4000 Mbps", "5000 Mbps", "6000 Mbps", "7000 Mbps",
					"8000 Mbps", "9000 Mbps", "10000 Mbps"));
			XSSFSheet BandwidthSheet = workbook.createSheet("BandwidthSheet");
			for (int i = 0, length = bandwidth.size(); i < length; i++) {
				String name = bandwidth.get(i);
				String localLoopName = localLoopBandwidth.get(i);
				XSSFRow row = BandwidthSheet.createRow(i);
				XSSFCell cell = row.createCell(0);
				XSSFCell cell1 = row.createCell(1);
				cell.setCellValue(name);
				cell1.setCellValue(localLoopName);
			}

			for (int j = bandwidth.size(), length = localLoopBandwidth.size(); j < length; j++) {
				String localLoopName = localLoopBandwidth.get(j);
				XSSFRow row = BandwidthSheet.createRow(j);
				XSSFCell cell = row.createCell(1);
				cell.setCellValue(localLoopName);
			}

			for (int i = 0; i < IzosdwanCommonConstants.PROVIDER.size(); i++) {
				String name = IzosdwanCommonConstants.PROVIDER.get(i);
				if (i < localLoopBandwidth.size()) {
					XSSFRow row = BandwidthSheet.getRow(i);
					XSSFCell cell = row.createCell(3);
					cell.setCellValue(name);
				} else {
					XSSFRow row = BandwidthSheet.createRow(i);
					XSSFCell cell = row.createCell(3);
					cell.setCellValue(name);
				}
			}

			Name namedCell1 = workbook.createName();
			namedCell1.setNameName("BandwidthSheet");
			namedCell1.setRefersToFormula("BandwidthSheet!$A$1:$A$" + bandwidth.size());
			Name namedCell2 = workbook.createName();
			namedCell2.setNameName("LocalLoopBandwidthSheet");
			namedCell2.setRefersToFormula("BandwidthSheet!$B$1:$B$" + localLoopBandwidth.size());
			Name namedCell3 = workbook.createName();
			namedCell3.setNameName("providerNameSheet");
			namedCell3.setRefersToFormula("BandwidthSheet!$D$1:$D$" + IzosdwanCommonConstants.PROVIDER.size());
			workbook.setSheetHidden(2, true);
			workbook.setSheetHidden(1, true);
			/**** Primary and secondary attributes drop down ******/
			for (int i = 0, j = 11; i < 2; i++, j++) {
				if (j == 11 || j == 21) {
					ByonUpload.addValidationData(
							izoSdwandataValidation(dropdownvalues, "BandwidthSheet", ByonUpload, j, j));
					j++;
				}
				if (j == 12 || j == 22) {
					ByonUpload.addValidationData(
							izoSdwandataValidation(dropdownvalues, "LocalLoopBandwidthSheet", ByonUpload, j, j));
					j++;
				}
				if (j == 13 || j == 23) {
					if (j == 13) {
						ByonUpload.addValidationData(izoSdwandataValidation(portModePrimary, "", ByonUpload, j, j));
					} else {
						ByonUpload.addValidationData(izoSdwandataValidation(portModeSecondary, "", ByonUpload, j, j));
					}
					j++;
				}
				if (j == 14 || j == 24) {
					ByonUpload.addValidationData(izoSdwandataValidation(acessType, "", ByonUpload, j, j));
					j++;
				}
				if (j == 15 || j == 25) {
					ByonUpload.addValidationData(
							izoSdwandataValidation((ArrayList<String>) interfaceTypeDet, "", ByonUpload, j, j));
					j += 3;
				}

				if (j == 18 || j == 28) {
					ByonUpload.addValidationData(
							izoSdwandataValidation(dropdownvalues, "providerNameSheet", ByonUpload, j, j));
					j += 2;
				}

				if (j == 20 || j == 30) {
					ByonUpload.addValidationData(izoSdwandataValidation(byonLTE, "", ByonUpload, j, j));
				}

			}

			// if it is a 100 per byon hide use existing column header
			if (attrValue.equals("true")) {
				ByonUpload.setColumnHidden(1, true);
				// ByonUpload.pr
				// Cells cell=workbook.getC

			}

			boolean dataResponse = true;
			try {
				if (isTemplateDownload == false) {
					dataResponse = storeDataIntoExcel(workbook, quoteId, attrValue);
				}
			} catch (Exception e) {
				LOGGER.info("Passed isTemplateDownload null");
				// TODO: handle exception
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = "byon-template.xlsx";
			if (isTemplateDownload == false) {
				if (dataResponse) {
					fileName = "byon-data.xlsx";
				} else {
					fileName = "byon-data-error.xlsx";
				}
			}
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
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
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing downloadLocationTemplate malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.MALFORMED_URL_EXCEPTION, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadLocationTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
	}

	// Storing data into the excel sheet and checking if error column is there or
	// not if there make error header and place error messages
	private boolean storeDataIntoExcel(Workbook workbook, Integer quoteId, String attributeValue)
			throws TclCommonException {
		List<QuoteIzosdwanByonUploadDetail> quoteIzoSdwanByon = new ArrayList<>();
		boolean containsWordStatus = true;
		validateGetQuoteDetail(quoteId);
		Quote quote = getQuote(quoteId);
		Sheet sheet = workbook.getSheetAt(0);
		int Lastcellnum = sheet.getRow(0).getLastCellNum();
		LOGGER.info("last row num with data => {} ", getLastRowWithData(sheet));
		LOGGER.info("Last cell number is {}", Lastcellnum);
		// Getting last cell name if it is error cell then remove that cell.
		XSSFCell cellName = (XSSFCell) sheet.getRow(0).getCell((Lastcellnum - 1),
				MissingCellPolicy.RETURN_BLANK_AS_NULL);
		if (!(cellName.getStringCellValue().equals("Errors"))) {
			sheet.getRow(0).createCell(Lastcellnum).setCellValue("Errors");
			sheet.setColumnWidth(Lastcellnum + 1, Lastcellnum + 1 * 256);
			sheet.setColumnWidth(Lastcellnum + 1, 20 * 256);
			LOGGER.info("last row num with data => {} ", getLastRowWithData(sheet));
		}
		Lastcellnum = sheet.getRow(0).getLastCellNum();
		LOGGER.info("Last cell number is {}", Lastcellnum);
		cellName = (XSSFCell) sheet.getRow(0).getCell((Lastcellnum - 1), MissingCellPolicy.RETURN_BLANK_AS_NULL);
		LOGGER.info("Cell name is {}", cellName);
		quoteIzoSdwanByon = quoteIzosdwanByonUploadDetailRepository.findByQuote(quote);
		Integer index = 1;
		if (!(quoteIzoSdwanByon.isEmpty())) {
			for (QuoteIzosdwanByonUploadDetail quoteByonDet : quoteIzoSdwanByon) {
				sheet.createRow(index);
				sheet.getRow(index).createCell(0).setCellValue(String.valueOf(index));
				sheet.getRow(index).createCell(1).setCellValue(quoteByonDet.getExistingAddress());
				sheet.getRow(index).createCell(2).setCellValue(quoteByonDet.getCountry());
				LOGGER.info("Getting country from cell{}",
						sheet.getRow(index).getCell(2).getStringCellValue().toString());

				sheet.getRow(index).createCell(3).setCellValue(quoteByonDet.getState());
				sheet.getRow(index).createCell(4).setCellValue(quoteByonDet.getCity());
				sheet.getRow(index).createCell(5).setCellValue(quoteByonDet.getPincode());
				sheet.getRow(index).createCell(6).setCellValue(quoteByonDet.getLocality());
				sheet.getRow(index).createCell(7).setCellValue(quoteByonDet.getAddress());
				sheet.getRow(index).createCell(8).setCellValue(quoteByonDet.getInternetQuality());
				sheet.getRow(index).createCell(9).setCellValue(quoteByonDet.getManagementOption());
				sheet.getRow(index).createCell(10).setCellValue(quoteByonDet.getSiteType());
				sheet.getRow(index).createCell(11).setCellValue(quoteByonDet.getPriPortBw());
				sheet.getRow(index).createCell(12).setCellValue(quoteByonDet.getPriLastmileBw());
				sheet.getRow(index).createCell(13).setCellValue(quoteByonDet.getPriPortMode());
				sheet.getRow(index).createCell(14).setCellValue(quoteByonDet.getPriAccessType());
				sheet.getRow(index).createCell(15).setCellValue(quoteByonDet.getPriInterface());
				sheet.getRow(index).createCell(16).setCellValue(quoteByonDet.getPriThirdpartyServiceId());
				sheet.getRow(index).createCell(17).setCellValue(quoteByonDet.getPriThirdpartyIpAddress());
				sheet.getRow(index).createCell(18).setCellValue(quoteByonDet.getPriThirdpartyProvider());
				sheet.getRow(index).createCell(19).setCellValue(quoteByonDet.getPriThirdPartyLinkUptime());
				sheet.getRow(index).createCell(20).setCellValue(quoteByonDet.getPriThirdpartyByonLte());
				sheet.getRow(index).createCell(21).setCellValue(quoteByonDet.getSecPortBw());
				sheet.getRow(index).createCell(22).setCellValue(quoteByonDet.getSecLastmileBw());
				sheet.getRow(index).createCell(23).setCellValue(quoteByonDet.getSecPortMode());
				sheet.getRow(index).createCell(24).setCellValue(quoteByonDet.getSecAccessType());
				sheet.getRow(index).createCell(25).setCellValue(quoteByonDet.getSecInterface());
				sheet.getRow(index).createCell(26).setCellValue(quoteByonDet.getSecThirdpartyServiceId());
				sheet.getRow(index).createCell(27).setCellValue(quoteByonDet.getSecThirdpartyIpAddress());
				sheet.getRow(index).createCell(28).setCellValue(quoteByonDet.getSecThirdpartyProvider());
				sheet.getRow(index).createCell(29).setCellValue(quoteByonDet.getSecThirdPartyLinkUptime());
				sheet.getRow(index).createCell(30).setCellValue(quoteByonDet.getSecThirdpartyByonLte());
				if (quoteByonDet.getLocationErrorDetails() != null) {
					if (quoteByonDet.getErrorDetails() == null) {
						quoteByonDet.setErrorDetails("");
					}
					quoteByonDet.setErrorDetails(
							quoteByonDet.getErrorDetails().concat(" " + quoteByonDet.getLocationErrorDetails()));
				}
				sheet.getRow(index).createCell(31).setCellValue(quoteByonDet.getErrorDetails());
				index++;
			}
			List<String> containWords = new ArrayList<>();
			containWords.add(IzosdwanCommonConstants.RETAILGRADE);
			containWords.add(IzosdwanCommonConstants.ERRORFORBANDWIDTH);
			containWords.add("can't be");
			for (QuoteIzosdwanByonUploadDetail byonVal : quoteIzoSdwanByon) {
				if (byonVal.getErrorDetails().contains("can't be")) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORFORRETAIL)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(IzosdwanCommonConstants.ERRORFORBANDWIDTH)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(CommonConstants.IASSITENOTEXIST)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(CommonConstants.GVPNSITENOTEXIST)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(CommonConstants.DUALCPESECNARIRO)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(CommonConstants.EXISTIINGADDRESS)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains("Invalid")) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains("Address")) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(CommonConstants.PRIMARYSECONDARY)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(CommonConstants.BYONSITEADDRESS)) {
					containsWordStatus = false;
				} else if (byonVal.getErrorDetails().contains(CommonConstants.PRIMARYSECONDARYSINGLE)) {
					containsWordStatus = false;
				}
			}
//			Optional<QuoteIzosdwanByonUploadDetail> uploadDet = quoteIzoSdwanByon.stream()
//					.filter(byon -> byon.getErrorDetails().conta.findAny();
			if (containsWordStatus) {
				if (cellName.getStringCellValue().toString().equals("Errors")) {
					sheet.getRow(0).removeCell(cellName);
				}
			}
		}
		return containsWordStatus;
	}

	// izoSdwan Excel download dataValidation

	public DataValidation izoSdwandataValidation(ArrayList<String> request, String sheetName, Sheet sheet, int x,
			int y) {
		DataValidation dataValidation = null;
		DataValidationConstraint constraint = null;
		DataValidationHelper validationHelper = null;
		validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 200, x, y);
		if (x == 1 || x == 2 || x == 10 || x == 11 || x == 12 || x == 18 || x == 21 || x == 22 || x == 28) {
			constraint = validationHelper.createFormulaListConstraint(sheetName);
		} else {
			constraint = validationHelper.createExplicitListConstraint(request.stream().toArray(String[]::new));
		}
		// constraint =
		// validationHelper.createExplicitListConstraint(request.stream().toArray(String[]::new));
		dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setSuppressDropDownArrow(true);
		dataValidation.setShowErrorBox(true);
		dataValidation.setShowPromptBox(true);
		return dataValidation;
	}

	private List<QuoteIzosdwanByonUploadDetail> persistByonUploadDetails(List<ByonBulkUploadDetail> byonUploadDetails,
			Quote quote, String attributeValue) throws TclCommonException, IllegalArgumentException {
		List<QuoteIzosdwanByonUploadDetail> quoteIzoSdwanByon = new ArrayList<>();
		List<String> byonSiteTypes = new ArrayList<>();
		byonSiteTypes.add("Single BYON Internet Single CPE");
		byonSiteTypes.add("Dual BYON Internet Single CPE");
		byonSiteTypes.add("Dual BYON Internet Dual CPE");
		quoteIzoSdwanByon = quoteIzosdwanByonUploadDetailRepository.findByQuote(quote);
		if (!(quoteIzoSdwanByon.isEmpty())) {
			quoteIzosdwanByonUploadDetailRepository.deleteInBatch(quoteIzoSdwanByon);
			ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quote.getId());
			// fetch only byon details from izosdwan sites table
			List<QuoteIzosdwanSite> izoSdwanSites1 = quoteIzosdwanSiteRepository
					.findByProductSolutionAndIzosdwanSiteProduct(solutions, "BYON Internet");
			if (!(izoSdwanSites1.isEmpty())) {
				for (QuoteIzosdwanSite sites : izoSdwanSites1) {
					removeComponentsAndAttr(sites.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES);
					deletedIllsiteAndRelation(sites);
				}
				List<QuoteIzosdwanSite> izoSdwanSites = quoteIzosdwanSiteRepository.findByProductSolution(solutions);
				List<IzoSdwanSiteDetails> siteDet = getSiteDetails();
				List<IzoSdwanCpeDetails> cpeDetails = getCpeDetails(null, null, null);
				ProductOfferingsBean productOfferingsBean = null;
				ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quote.getId());
				productOfferingsBean = Utils.convertJsonToObject(productSolution.getProductProfileData(),
						ProductOfferingsBean.class);
				if (!(izoSdwanSites.isEmpty())) {
					for (QuoteIzosdwanSite site : izoSdwanSites) {
						if (site.getIzosdwanSiteType().contains("BYON Internet")) {
							if (site.getIsShared() != null && site.getIsShared().equals("Y")) {
								site.setIzosdwanSiteType(
										site.getIzosdwanSiteType().replace("BYON Internet Single ", ""));
								site.setIsShared("N");
							}
							if (site.getIzosdwanSiteType().contains("Dual")) {
								site.setIzosdwanSiteType(site.getIzosdwanSiteType().replace("BYON Internet Dual ", ""));
							}
							site.setPriSec("Primary");
							String cpeName = cpeSuggestionLogicForSite(quote.getIzosdwanFlavour(),
									solutions.getMstProductOffering().getProductName(), site, siteDet, cpeDetails,
									productOfferingsBean.getAddons());
							site.setNewCpe(cpeName);
							quoteIzosdwanSiteRepository.save(site);
						}
					}
					// deleting price details for byon after reuplad.
					List<QuotePrice> quotePrices = quotePriceRepository.findByQuoteId(quote.getId());
					if (!(quotePrices.isEmpty())) {
						quotePriceRepository.deleteInBatch(quotePrices);
					}

					quoteIzosdwanSiteRepository.deleteInBatch(izoSdwanSites1);
				}
			}
		}
		persistAttributeValues(quote);
		quoteIzoSdwanByon = new ArrayList<>();
		for (ByonBulkUploadDetail byonUploadDet : byonUploadDetails) {
			QuoteIzosdwanByonUploadDetail byonDetail = new QuoteIzosdwanByonUploadDetail();
			byonDetail.setQuote(quote);
			byonDetail.setExistingAddress(byonUploadDet.getExistingAddress());
			byonDetail.setCountry(byonUploadDet.getCountry());
			byonDetail.setCity(byonUploadDet.getCity());
			byonDetail.setState(byonUploadDet.getState());
			byonDetail.setPincode(byonUploadDet.getPinCode());
			byonDetail.setLocality(byonUploadDet.getLocality());
			byonDetail.setAddress(byonUploadDet.getAddress());
			byonDetail.setInternetQuality(byonUploadDet.getInternetQuality());
			byonDetail.setSiteType(byonUploadDet.getSiteType());
			if (byonUploadDet.getPrimaryPortBandwidth() != null) {
				String portBandwidth = byonUploadDet.getPrimaryPortBandwidth();
				portBandwidth = portBandwidth.replace("Mbps", " ").trim();
				byonDetail.setPriPortBw(portBandwidth);
			}
			if (byonUploadDet.getPrimaryLocaLoopBandwidth() != null) {
				if (byonUploadDet.getPrimaryLocaLoopBandwidth().contains("(")) {
					String[] localLoopBand = byonUploadDet.getPrimaryLocaLoopBandwidth().split("()");
					Integer indexOfLastNumber = byonUploadDet.getPrimaryLocaLoopBandwidth().indexOf("(");
					String bandwidth = getBandwidth(localLoopBand, indexOfLastNumber);
					byonDetail.setPriLastmileBw(bandwidth);
				} else {
					String[] localLoopBand = byonUploadDet.getPrimaryLocaLoopBandwidth().split(" ");
					byonDetail.setPriLastmileBw(localLoopBand[0]);
				}
			}
			byonDetail.setPriPortMode(byonUploadDet.getPrimaryPortMode());
			byonDetail.setPriAccessType(byonUploadDet.getPrimayAccessType());
			byonDetail.setPriInterface(byonUploadDet.getPrimaryInterfaceType());
			byonDetail.setPriThirdpartyServiceId(byonUploadDet.getPrimaryThirdPartyServiceId());
			byonDetail.setPriThirdpartyIpAddress(byonUploadDet.getPrimaryThirdPartyIpAddress());
			byonDetail.setPriThirdpartyProvider(byonUploadDet.getPrimaryThirdPartyProvider());
			byonDetail.setPriThirdPartyLinkUptime(byonUploadDet.getPrimaryThirdPartyLinkUptime());
			byonDetail.setPriThirdpartyByonLte(byonUploadDet.getPrimaryThirdPartyByonLTE());
			if (byonUploadDet.getSecondaryPortBandwidth() != null) {
				String portBandwidth = byonUploadDet.getSecondaryPortBandwidth();
				portBandwidth = portBandwidth.replace("Mbps", " ").trim();
				byonDetail.setSecPortBw(portBandwidth);
			}
			if (byonUploadDet.getSecondaryLocaLoopBandwidth() != null) {
				if (byonUploadDet.getSecondaryLocaLoopBandwidth().contains("(")) {
					String[] localLoopBand = byonUploadDet.getSecondaryLocaLoopBandwidth().split("()");
					Integer indexOfLastNumber = (byonUploadDet.getSecondaryLocaLoopBandwidth()).indexOf("(");
					String bandwidth = getBandwidth(localLoopBand, indexOfLastNumber);
					byonDetail.setSecLastmileBw(bandwidth);
				} else {
					String[] localLoopBand = byonUploadDet.getSecondaryLocaLoopBandwidth().split(" ");
					byonDetail.setSecLastmileBw(localLoopBand[0]);
				}
			}
			byonDetail.setSecPortMode(byonUploadDet.getSecondaryPortMode());
			byonDetail.setSecAccessType(byonUploadDet.getSecondaryAccessType());
			byonDetail.setSecInterface(byonUploadDet.getSecondaryInterfaceType());
			byonDetail.setSecThirdpartyServiceId(byonUploadDet.getSecondaryThirdPartyServiceId());
			byonDetail.setSecThirdpartyIpAddress(byonUploadDet.getSecondaryThirdPartyIpAddress());
			byonDetail.setSecThirdpartyProvider(byonUploadDet.getSecondaryThirdPartyProvider());
			byonDetail.setSecThirdPartyLinkUptime(byonUploadDet.getSecondaryThirdPartyLinkUptime());
			byonDetail.setSecThirdpartyByonLte(byonUploadDet.getSecondaryThirdPartyByonLTE());
			byonDetail.setStatus(IzosdwanCommonConstants.OPEN);
			byonDetail.setManagementOption(byonUploadDet.getManagementOption());
//			byonDetail.setCosTopoloy(byonUploadDet.getTopology());
			if (attributeValue.equals("false")) {
				boolean status = checkByonSiteExistsOrNot(byonSiteTypes, byonDetail.getSiteType());
				if (byonUploadDet.getExistingAddress() != null && byonUploadDet.getErrorMessageToDisplay() == null
						|| byonUploadDet.getErrorMessageToDisplay().isEmpty()) {
					if (!status) {
						byonDetail.setStatus(IzosdwanCommonConstants.OPEN);
					} else {
						validateSiteDetailsAndAddress(byonUploadDet, quote, byonDetail);
					}
				} else {
					byonDetail.setStatus(IzosdwanCommonConstants.OPEN);
				}
			}
			byonDetail.setErrorDetails(byonUploadDet.getErrorMessageToDisplay());
			byonDetail.setRetriggerCount(0);
			quoteIzoSdwanByon.add(byonDetail);
		}
		quoteIzosdwanByonUploadDetailRepository.saveAll(quoteIzoSdwanByon);

		return quoteIzoSdwanByon;
	}

	private String cpeSuggestionLogicForSite(String vendorName, String profileName, QuoteIzosdwanSite quoteIzoSdwanSite,
			List<IzoSdwanSiteDetails> siteDet, List<IzoSdwanCpeDetails> allCpeDetails, List<AddonsBean> addons) {

		String cpeName = null;
		Optional<IzoSdwanSiteDetails> siteDetails = siteDet.stream()
				.filter(site -> site.getSiteTypeName().equals(quoteIzoSdwanSite.getIzosdwanSiteType())).findFirst();
		IzoSdwanSiteDetails site = siteDetails.get();
		/*
		 * if(Integer.parseInt(siServiceDetailBean.getBandwidth())<2) {
		 * siServiceDetailBean.setBandwidth("2"); }
		 */
		String addon = null;
		if (addons != null && !addons.isEmpty()) {
			addon = addons.get(addons.size() - 1).getCode();
		}
		List<IzoSdwanCpeDetails> cpeDetails = new ArrayList<>();
		if (vendorName != null) {
			cpeDetails = allCpeDetails.stream().filter(cpe -> vendorName.equalsIgnoreCase(cpe.getVendor()))
					.collect(Collectors.toList());
			LOGGER.info("After filtering Vendor Name !! ");
		}
		if (addon != null) {
			final String addonFinal = addon;
			if (addonFinal != null) {
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> addonFinal.equalsIgnoreCase(cpe.getAddon()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> addonFinal.equalsIgnoreCase(cpe.getAddon()))
							.collect(Collectors.toList());
				}
				LOGGER.info("After filtering Addon Name !! ");
			}
		} else {
			if (cpeDetails != null && !cpeDetails.isEmpty()) {
				cpeDetails = cpeDetails.stream().filter(cpe -> StringUtils.isBlank(cpe.getAddon()))
						.collect(Collectors.toList());
			}
		}
		if (profileName != null) {
			if (profileName.equals("Select Secure Premium")) {
				String name = "SECURE_PREMIUM";
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> name.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> name.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				}
			} else {
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> profileName.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> profileName.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				}
			}

			LOGGER.info("After filtering Profile Name !! ");
		}
		if (cpeDetails != null && !cpeDetails.isEmpty()) {

			LOGGER.info("THE SITE BANDWITH IS {}", quoteIzoSdwanSite.getNewLastmileBandwidth());
			Integer bandDet = Math.round(Float.parseFloat(quoteIzoSdwanSite.getNewLastmileBandwidth()));
			cpeName = getUniqueCpes(site, cpeDetails, bandDet);
			// siServiceDetailBean.getSiCpeBeans().get(0).setSuggestedModel(cpeName);

		}
		return cpeName;

	}

	private void validateSiteDetailsAndAddress(ByonBulkUploadDetail byonUploadDet, Quote quote,
			QuoteIzosdwanByonUploadDetail byonDetail) {
		// If quote izosdwan site details are null
		ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quote.getId());
		List<String> siteTypes = new ArrayList<>();
		siteTypes.add(IzosdwanCommonConstants.SGSC);
		siteTypes.add(IzosdwanCommonConstants.SISC);
		List<QuoteIzosdwanSite> quoteIzoSdwanSites = new ArrayList<>();
		Integer ias = 0;
		Integer gvpn = 0;
		if (byonUploadDet.getSiteType().contains("IAS")) {
			gvpn++;
			quoteIzoSdwanSites = quoteIzosdwanSiteRepository
					.findByIzosdwanSiteProductAndProductSolutionAndServiceSiteAddressAndIzosdwanSiteTypeInOrderByCreatedTimeDesc(
							"IAS", solutions, byonUploadDet.getExistingAddress(), siteTypes);
		} else {
			if (byonUploadDet.getSiteType().contains("GVPN")) {
				ias++;
				quoteIzoSdwanSites = quoteIzosdwanSiteRepository
						.findByIzosdwanSiteProductAndProductSolutionAndServiceSiteAddressAndIzosdwanSiteTypeInOrderByCreatedTimeDesc(
								"GVPN", solutions, byonUploadDet.getExistingAddress(), siteTypes);
			}
		}
		if (quoteIzoSdwanSites != null & !quoteIzoSdwanSites.isEmpty()) {
			QuoteIzosdwanSite site = quoteIzoSdwanSites.get(0);
			String byonSiteType = byonUploadDet.getSiteType();
			byonSiteType = byonSiteType.replace("Single BYON Internet", "");
			byonSiteType = byonSiteType.replace("  ", " ");
			if (byonUploadDet.getSiteType().contains("Single CPE")) {
				if (site.getIzosdwanSiteType().equals(byonSiteType)) {
					byonDetail.setSiteId(site.getId());
					// site.setIzosdwanSiteType(byonDetail.getSiteType().replace("Single CPE",
					// "Shared CPE"));
					byonDetail.setStatus(IzosdwanCommonConstants.COMPLETED);
					byonDetail.setLocationId(site.getErfLocSitebLocationId());
					byonDetail.setLatLong(site.getLatLong());
					byonDetail.setSiteType(byonDetail.getSiteType());
					byonDetail.setSiteId(site.getId());
				}

			} else {
				String productType1 = site.getIzosdwanSiteProduct();
				if (byonSiteType.contains(productType1)) {
					// site.setIzosdwanSiteType(byonDetail.getSiteType().replace("Single CPE", "Dual
					// CPE"));
					byonDetail.setSiteId(site.getId());
					byonDetail.setStatus(IzosdwanCommonConstants.COMPLETED);
					byonDetail.setLocationId(site.getErfLocSitebLocationId());
					byonDetail.setLatLong(site.getLatLong());
				}
			}
		} else {
			if (ias != 0) {
				if (byonUploadDet.getSiteType().contains("GVPN")) {
					byonUploadDet.setErrorMessageToDisplay(
							byonUploadDet.getErrorMessageToDisplay().concat(CommonConstants.GVPNSITENOTEXIST));
					byonDetail.setStatus(IzosdwanCommonConstants.CLOSED);
				}
			} else if (gvpn != 0) {
				if (byonUploadDet.getSiteType().contains("IAS")) {
					byonUploadDet.setErrorMessageToDisplay(
							byonUploadDet.getErrorMessageToDisplay().concat(CommonConstants.IASSITENOTEXIST));
					byonDetail.setStatus(IzosdwanCommonConstants.CLOSED);
				}
			} else {
				byonUploadDet.setErrorMessageToDisplay(
						byonUploadDet.getErrorMessageToDisplay().concat(CommonConstants.INVALIDSITETYPE));
				byonDetail.setStatus(IzosdwanCommonConstants.CLOSED);
			}
		}

	}

	private void persistAttributeValues(Quote quote) {
		try {
			List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByQuote(quote);

			if (!(attributeValues.isEmpty())) {
				persistOrUpdateAttribute(IzosdwanCommonConstants.BYON_INTERNET.toString(), attributeValues, quote,
						"false");

			}
		} catch (Exception e) {
			LOGGER.error("Error in persisting or updating the attributes ", e);
		}

	}

	private void persistOrUpdateAttribute(String displayValue, List<QuoteIzoSdwanAttributeValues> attributeValues,
			Quote quote, String value) {
		Optional<QuoteIzoSdwanAttributeValues> attrValue = attributeValues.stream()
				.filter(attrVal -> attrVal.getDisplayValue().equals(displayValue)).findFirst();
		if (!(attrValue.isPresent())) {
			QuoteIzoSdwanAttributeValues sdwanVal = new QuoteIzoSdwanAttributeValues();
			sdwanVal.setDisplayValue(displayValue);
			sdwanVal.setAttributeValue(value);
			sdwanVal.setQuote(quote);
			quoteIzoSdwanAttributeValuesRepository.save(sdwanVal);

		} else {
			attrValue.get().setAttributeValue(value);
			quoteIzoSdwanAttributeValuesRepository.save(attrValue.get());
		}
	}

	/** Reading excel data and storing into byon bulk upload list **/
	private void readCellData(Sheet sheet, List<ByonBulkUploadDetail> byonUploadDetails, String attributeValue) {
		LOGGER.info("GETTING NUMBER OF ROWS IS {}", getLastRowWithData(sheet));
		List<String> byonSiteTypes = new ArrayList<>();
		byonSiteTypes.add("Single BYON Internet Single CPE");
		byonSiteTypes.add("Dual BYON Internet Single CPE");
		byonSiteTypes.add("Dual BYON Internet Dual CPE");
		for (int i = 1; i <= (getLastRowWithData(sheet)); i++) {
			DataFormatter dataFormatter = new DataFormatter();
			Integer primaryCount = 0;
			Integer secondaryCount = 0;
			String existingAddrCell = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String countryCell = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String stateCell = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(3, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String cityCell = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(4, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String pinZipCell = dataFormatter
					.formatCellValue((sheet.getRow(i).getCell(5, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
			String localityCell = dataFormatter
					.formatCellValue((sheet.getRow(i).getCell(6, MissingCellPolicy.RETURN_BLANK_AS_NULL)));
			String addressCell = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(7, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String internetQuality = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(8, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String managementOption = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(9, MissingCellPolicy.RETURN_BLANK_AS_NULL));
//			String topology = dataFormatter
//					.formatCellValue(sheet.getRow(i).getCell(10, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String siteType = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(10, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryPortBandwidth = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(11, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryLocalLoopBandwidth = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(12, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryPortMode = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(13, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryAccessType = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(14, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryInterfaceType = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(15, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryThirdPartyServiceId = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(16, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryThirdPartyIpAddress = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(17, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryThirdPartyProvider = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(18, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryThirdPartyLinkUptime = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(19, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String primaryThirdPartyBYON4GLTE = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(20, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryPortBandwidth = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(21, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryLocalLoopBandwidth = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(22, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryPortMode = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(23, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryAccessType = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(24, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryInterfaceType = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(25, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryThirdPartyServiceId = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(26, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryThirdPartyIpAddress = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(27, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryThirdPartyProvider = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(28, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryThirdPartyLinkUptime = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(29, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			String secondaryThirdPartyBYON4GLTE = dataFormatter
					.formatCellValue(sheet.getRow(i).getCell(30, MissingCellPolicy.RETURN_BLANK_AS_NULL));

			ByonBulkUploadDetail uploadDet = new ByonBulkUploadDetail();
			uploadDet.setErrorMessageToDisplay("");
			if (countryCell.isEmpty() || null == countryCell) {
				if ((!siteType.isEmpty() || siteType != null) && byonSiteTypes.contains(siteType)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.COUNTRY_CANT_BE_EMPTY + ","));
				}
			} else {
				uploadDet.setCountry(countryCell);
			}
			if (stateCell.isEmpty() || null == stateCell) {
				if ((!siteType.isEmpty() || siteType != null) && byonSiteTypes.contains(siteType)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.STATE_CANT_BE_EMPTY + ","));
				}
			} else {
				uploadDet.setState(stateCell);
			}
			if (cityCell.isEmpty() || null == cityCell) {
				if ((!siteType.isEmpty() || siteType != null) && byonSiteTypes.contains(siteType)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.CITY_CANT_BE_EMPTY + ","));
				}

			} else {
				uploadDet.setCity(cityCell);
			}
			if (pinZipCell.isEmpty() || null == pinZipCell) {
				if ((!siteType.isEmpty() || siteType != null) && byonSiteTypes.contains(siteType)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PINCODE_CANT_BE_EMPTY + ","));
				}

			} else {
				// String val=pinZipCell.getStringCellValue();
				uploadDet.setPinCode(pinZipCell);
			}
			if (localityCell.isEmpty() || null == localityCell) {
				if ((!siteType.isEmpty() || siteType != null) && byonSiteTypes.contains(siteType)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.LOCALITY_CANT_BE_EMPTY + ","));
				}
			} else {
				uploadDet.setLocality(localityCell);
			}
			if (addressCell.isEmpty() || null == addressCell) {
				if ((!siteType.isEmpty() || siteType != null) && byonSiteTypes.contains(siteType)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.ADDRESS_CANT_BE_EMPTY + ","));
				}

			} else {
				uploadDet.setAddress(addressCell);
			}
			if (internetQuality.isEmpty() || null == internetQuality) {
				uploadDet.setErrorMessageToDisplay(uploadDet.getErrorMessageToDisplay()
						.concat(CommonConstants.INTERNETQUALITY_CANT_BE_EMPTY + ","));
			} else {
				uploadDet.setInternetQuality(internetQuality);
			}
			if (managementOption.isEmpty() || null == managementOption) {
				uploadDet.setErrorMessageToDisplay(uploadDet.getErrorMessageToDisplay()
						.concat(CommonConstants.MANAGEMENT_OPTION_CANT_BE_EMPTY + ","));
			} else {
				uploadDet.setManagementOption(managementOption);
			}
//			if(topology.isEmpty() || null == topology) {
//				if(!managementOption.isEmpty() && null != managementOption) {
//					if(managementOption.equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
//						uploadDet.setErrorMessageToDisplay(uploadDet.getErrorMessageToDisplay()
//								.concat(CommonConstants.TOPLOLOGY_CANT_BE_EMPTY + ","));
//					}
//				}
//			}
//			else {
//				uploadDet.setTopology(topology);
//			}
			if (siteType.isEmpty() || null == siteType) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SITETYPE__CANT_BE_EMPTY + ","));
			} else {
				if (attributeValue.equals("false")) {
					boolean byonSiteStatus = checkByonSiteExistsOrNot(byonSiteTypes, siteType);
					if (existingAddrCell.isEmpty() || null == existingAddrCell) {
						if (byonSiteStatus) {
							uploadDet.setErrorMessageToDisplay(uploadDet.getErrorMessageToDisplay()
									.concat(CommonConstants.EXISTIINGADDRESS + ","));
						}
					} else {
						if (!byonSiteStatus) {
							uploadDet.setErrorMessageToDisplay(
									uploadDet.getErrorMessageToDisplay().concat(CommonConstants.BYONSITEADDRESS + ","));
						}
						uploadDet.setExistingAddress(existingAddrCell);
					}
				}
				uploadDet.setSiteType(siteType);
			}

			if (!primaryPortBandwidth.isEmpty() && null != primaryPortBandwidth) {
				uploadDet.setPrimaryPortBandwidth(primaryPortBandwidth);
				primaryCount++;
			}
			if (!(primaryLocalLoopBandwidth.isEmpty()) && null != primaryLocalLoopBandwidth) {
				uploadDet.setPrimaryLocaLoopBandwidth(primaryLocalLoopBandwidth);
				primaryCount++;
			}
			if (!(primaryPortMode.isEmpty()) && null != primaryPortMode) {
				uploadDet.setPrimaryPortMode(primaryPortMode);
				primaryCount++;
			}
			if (!(primaryAccessType.isEmpty()) && null != primaryAccessType) {
				uploadDet.setPrimayAccessType(primaryAccessType);
				primaryCount++;
			}
			if (!(primaryInterfaceType.isEmpty()) && null != primaryInterfaceType) {
				uploadDet.setPrimaryInterfaceType(primaryInterfaceType);
				primaryCount++;
			}
			if (!(primaryThirdPartyServiceId.isEmpty()) && null != primaryThirdPartyServiceId) {
				uploadDet.setPrimaryThirdPartyServiceId(primaryThirdPartyServiceId);
				primaryCount++;
			}
			if (!(primaryThirdPartyIpAddress.isEmpty()) && null != primaryThirdPartyIpAddress) {
				uploadDet.setPrimaryThirdPartyIpAddress(primaryThirdPartyIpAddress);
				;
				primaryCount++;
			}
			if (!(primaryThirdPartyProvider.isEmpty()) && null != primaryThirdPartyProvider) {
				uploadDet.setPrimaryThirdPartyProvider(primaryThirdPartyProvider);
				;
				primaryCount++;
			}
			if (!(primaryThirdPartyLinkUptime.isEmpty()) && null != primaryThirdPartyLinkUptime) {
				uploadDet.setPrimaryThirdPartyLinkUptime(primaryThirdPartyLinkUptime);
				;
				primaryCount++;
			}
			if (!(primaryThirdPartyBYON4GLTE.isEmpty()) && null != primaryThirdPartyBYON4GLTE) {
				uploadDet.setPrimaryThirdPartyByonLTE(primaryThirdPartyBYON4GLTE);
				;
				primaryCount++;
			}
			if (!secondaryPortBandwidth.isEmpty() && null != secondaryPortBandwidth) {
				uploadDet.setSecondaryPortBandwidth(secondaryPortBandwidth);
				secondaryCount++;
			}
			if (!(secondaryLocalLoopBandwidth.isEmpty()) && null != secondaryLocalLoopBandwidth) {
				uploadDet.setSecondaryLocaLoopBandwidth(secondaryLocalLoopBandwidth);
				secondaryCount++;
			}
			if (!(secondaryPortMode.isEmpty()) && null != secondaryPortMode) {
				uploadDet.setSecondaryPortMode(secondaryPortMode);
				secondaryCount++;
			}
			if (!(secondaryAccessType.isEmpty()) && null != secondaryAccessType) {
				uploadDet.setSecondaryAccessType(secondaryAccessType);
				secondaryCount++;
			}
			if (!(secondaryInterfaceType.isEmpty()) && null != secondaryInterfaceType) {
				uploadDet.setSecondaryInterfaceType(secondaryInterfaceType);
				secondaryCount++;
			}
			if (!(secondaryThirdPartyServiceId.isEmpty()) && null != secondaryThirdPartyServiceId) {
				uploadDet.setSecondaryThirdPartyServiceId(secondaryThirdPartyServiceId);
				;
				secondaryCount++;
			}
			if (!(secondaryThirdPartyIpAddress.isEmpty()) && null != secondaryThirdPartyIpAddress) {
				uploadDet.setSecondaryThirdPartyIpAddress(secondaryThirdPartyIpAddress);
				;
				secondaryCount++;
			}
			if (!(secondaryThirdPartyProvider.isEmpty()) && null != secondaryThirdPartyProvider) {
				uploadDet.setSecondaryThirdPartyProvider(secondaryThirdPartyProvider);
				;
				secondaryCount++;
			}
			if (!(secondaryThirdPartyLinkUptime.isEmpty()) && null != secondaryThirdPartyLinkUptime) {
				uploadDet.setSecondaryThirdPartyLinkUptime(secondaryThirdPartyLinkUptime);
				secondaryCount++;
			}
			if (!(secondaryThirdPartyBYON4GLTE.isEmpty()) && null != secondaryThirdPartyBYON4GLTE) {
				uploadDet.setSecondaryThirdPartyByonLTE(secondaryThirdPartyBYON4GLTE);
				;
				secondaryCount++;
			}

			validatePriAndSecDetails(uploadDet, primaryCount, secondaryCount);
			byonUploadDetails.add(uploadDet);
		}

	}

	// This method checks whether the site type is 100 perc byon site or not.
	private boolean checkByonSiteExistsOrNot(List<String> byonSiteType, String siteType) {
		boolean byonSiteStatus = true;
		for (String site : byonSiteType) {
			if (site.equals(siteType)) {
				byonSiteStatus = false;
			}
		}
		return byonSiteStatus;
	}

	// validating primary and secondary details
	private void validatePriAndSecDetails(ByonBulkUploadDetail uploadDet, Integer primaryCount,
			Integer secondaryCount) {
		Integer byonSiteTypeCount = 0;
		List<String> byonSiteTypes = new ArrayList<>();
		byonSiteTypes.add("Single BYON Internet Single CPE");
		byonSiteTypes.add("Dual BYON Internet Single CPE");
		byonSiteTypes.add("Dual BYON Internet Dual CPE");
		if (uploadDet.getSiteType() != null && !uploadDet.getSiteType().isEmpty()) {
			for (String siteType : byonSiteTypes) {
				if (uploadDet.getSiteType().equals(siteType)) {
					if (siteType.contains("Single BYON")) {
						byonSiteTypeCount++;
						if (primaryCount != 10) {
							checkForPriSecError(uploadDet, "Single");
						}
						if (secondaryCount != 0) {
							uploadDet.setErrorMessageToDisplay(uploadDet.getErrorMessageToDisplay()
									.concat(CommonConstants.PRIMARYSECONDARYSINGLE + ","));
						}
					} else {
						byonSiteTypeCount++;
						checkForPriSecError(uploadDet, "Dual");
					}
				}
			}
		} else {
			byonSiteTypeCount = -1;
		}
		// This is for tata underlay + byon primary secondary validation
		if (byonSiteTypeCount != -1 && byonSiteTypeCount == 0) {
			if (primaryCount != 0 && secondaryCount != 0) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARYSECONDARY + ","));
			}
			if (primaryCount != 10 && secondaryCount == 0) {
				if (uploadDet.getPrimaryPortBandwidth() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P + ","));
				}
				if (uploadDet.getPrimaryLocaLoopBandwidth() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_L + ","));
				}
				if (uploadDet.getPrimaryPortMode() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P_MO + ","));
				}
				if (uploadDet.getPrimayAccessType() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_A + ","));
				}
				if (uploadDet.getPrimaryInterfaceType() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_I + ","));
				}
				if (uploadDet.getPrimaryThirdPartyServiceId() == null) {
					if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
						uploadDet.setErrorMessageToDisplay(
								uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_SI + ","));
					}
				}
				if (uploadDet.getPrimaryThirdPartyIpAddress() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_IP + ","));
				}
				if (uploadDet.getPrimaryThirdPartyProvider() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_PR + ","));
				}
				if (uploadDet.getPrimaryThirdPartyLinkUptime() == null) {
					if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
						uploadDet.setErrorMessageToDisplay(
								uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_SL + ","));
					}
				}
				if (uploadDet.getPrimaryThirdPartyByonLTE() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_LTE + ","));
				}
			}
			if (primaryCount == 0 && secondaryCount != 10) {
				if (uploadDet.getSecondaryPortBandwidth() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P + ","));
				}
				if (uploadDet.getSecondaryLocaLoopBandwidth() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_L + ","));
				}
				if (uploadDet.getSecondaryPortMode() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P_MO + ","));
				}
				if (uploadDet.getSecondaryAccessType() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_A + ","));
				}
				if (uploadDet.getSecondaryInterfaceType() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_I + ","));
				}
				if (uploadDet.getSecondaryThirdPartyServiceId() == null) {
					if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
						uploadDet.setErrorMessageToDisplay(
								uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_S_I + ","));
					}
				}
				if (uploadDet.getSecondaryThirdPartyIpAddress() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_I_P + ","));
				}
				if (uploadDet.getSecondaryThirdPartyProvider() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P_R + ","));
				}
				if (uploadDet.getSecondaryThirdPartyLinkUptime() == null) {
					if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
						uploadDet.setErrorMessageToDisplay(
								uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_S_L + ","));
					}
				}
				if (uploadDet.getSecondaryThirdPartyByonLTE() == null) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_L_TE + ","));
				}
			}
		}
	}

	private void checkForPriSecError(ByonBulkUploadDetail uploadDet, String type) {
		if (type.equals("Single")) {
			if (uploadDet.getPrimaryLocaLoopBandwidth() == null || uploadDet.getPrimaryLocaLoopBandwidth().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_L_L_B + ","));
			}
			if (uploadDet.getPrimaryPortBandwidth() == null || uploadDet.getPrimaryPortBandwidth().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_P_B + ","));
			}
			if (uploadDet.getPrimaryInterfaceType() == null || uploadDet.getPrimaryInterfaceType().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_I_T + ","));
			}
			if (uploadDet.getPrimaryPortMode() == null || uploadDet.getPrimaryPortMode().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_P_M + ","));
			}
			if (uploadDet.getPrimayAccessType() == null || uploadDet.getPrimayAccessType().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_A_T + ","));
			}
			if (uploadDet.getPrimaryThirdPartyServiceId() == null
					|| uploadDet.getPrimaryThirdPartyServiceId().isEmpty()) {
				if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_S_I + ","));
				}
			}
			if (uploadDet.getPrimaryThirdPartyIpAddress() == null
					|| uploadDet.getPrimaryThirdPartyIpAddress().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_I_P + ","));
			}
			if (uploadDet.getPrimaryThirdPartyProvider() == null
					|| uploadDet.getPrimaryThirdPartyProvider().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_P_R + ","));
			}
			if (uploadDet.getPrimaryThirdPartyLinkUptime() == null
					|| uploadDet.getPrimaryThirdPartyLinkUptime().isEmpty()) {
				if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_S_L + ","));
				}
			}
			if (uploadDet.getPrimaryThirdPartyByonLTE() == null || uploadDet.getPrimaryThirdPartyByonLTE().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_L_TE + ","));
			}
		} else {
			if (uploadDet.getPrimaryLocaLoopBandwidth() == null || uploadDet.getPrimaryLocaLoopBandwidth().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_L_L_B + ","));
			}
			if (uploadDet.getPrimaryPortBandwidth() == null || uploadDet.getPrimaryPortBandwidth().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_P_B + ","));
			}
			if (uploadDet.getPrimaryInterfaceType() == null || uploadDet.getPrimaryInterfaceType().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_I_T + ","));
			}
			if (uploadDet.getPrimaryPortMode() == null || uploadDet.getPrimaryPortMode().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_P_M + ","));
			}
			if (uploadDet.getPrimayAccessType() == null || uploadDet.getPrimayAccessType().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_A_T + ","));
			}
			if (uploadDet.getPrimaryThirdPartyServiceId() == null
					|| uploadDet.getPrimaryThirdPartyServiceId().isEmpty()) {
				if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_S_I + ","));
				}
			}
			if (uploadDet.getPrimaryThirdPartyIpAddress() == null
					|| uploadDet.getPrimaryThirdPartyIpAddress().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_I_P + ","));
			}
			if (uploadDet.getPrimaryThirdPartyProvider() == null
					|| uploadDet.getPrimaryThirdPartyProvider().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_P_R + ","));
			}
			if (uploadDet.getPrimaryThirdPartyLinkUptime() == null
					|| uploadDet.getPrimaryThirdPartyLinkUptime().isEmpty()) {
				if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_S_L + ","));
				}
			}
			if (uploadDet.getPrimaryThirdPartyByonLTE() == null || uploadDet.getPrimaryThirdPartyByonLTE().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.PRIMARY_L_TE + ","));
			}
			// adding secondary errors
			if (uploadDet.getSecondaryLocaLoopBandwidth() == null
					|| uploadDet.getSecondaryLocaLoopBandwidth().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_L_L_B + ","));
			}
			if (uploadDet.getSecondaryPortBandwidth() == null || uploadDet.getSecondaryPortBandwidth().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P_B + ","));
			}
			if (uploadDet.getSecondaryInterfaceType() == null || uploadDet.getSecondaryInterfaceType().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_I_T + ","));
			}
			if (uploadDet.getSecondaryPortMode() == null || uploadDet.getSecondaryPortMode().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P_M + ","));
			}
			if (uploadDet.getSecondaryAccessType() == null || uploadDet.getSecondaryAccessType().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_A_T + ","));
			}
			if (uploadDet.getSecondaryThirdPartyServiceId() == null
					|| uploadDet.getSecondaryThirdPartyServiceId().isEmpty()) {
				if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_S_I + ","));
				}
			}
			if (uploadDet.getSecondaryThirdPartyIpAddress() == null
					|| uploadDet.getSecondaryThirdPartyIpAddress().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_I_P + ","));
			}
			if (uploadDet.getSecondaryThirdPartyProvider() == null
					|| uploadDet.getSecondaryThirdPartyProvider().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_P_R + ","));
			}
			if (uploadDet.getSecondaryThirdPartyLinkUptime() == null
					|| uploadDet.getSecondaryThirdPartyLinkUptime().isEmpty()) {
				if (uploadDet.getManagementOption().equalsIgnoreCase(IzosdwanCommonConstants.FULLY_MANAGED)) {
					uploadDet.setErrorMessageToDisplay(
							uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_S_L + ","));
				}
			}
			if (uploadDet.getSecondaryThirdPartyByonLTE() == null
					|| uploadDet.getSecondaryThirdPartyByonLTE().isEmpty()) {
				uploadDet.setErrorMessageToDisplay(
						uploadDet.getErrorMessageToDisplay().concat(CommonConstants.SECONDARY_L_TE + ","));
			}

		}
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 * @param productSolutions
	 * @return Set<ProductSolutionBean>
	 * @throws TclCommonException
	 */
	private List<ProductSolutionBean> constructProductSolution(List<ProductSolution> productSolutions,
			Boolean isFeasibleSites, Boolean isSiteProperitiesRequired, Integer siteId, List<Integer> siteIds,
			Boolean isVproxy, Quote quote) throws TclCommonException {
		List<ProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				ProductSolutionBean productSolutionBean = new ProductSolutionBean();

				productSolutionBean.setProductSolutionId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					productSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					productSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					productSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
					if (solution.getProductProfileData() != null) {
						if (!isVproxy) {
							try {
								ProductOfferingsBean productOfferingsBean = Utils.convertJsonToObject(
										solution.getProductProfileData(), ProductOfferingsBean.class);
								if (productOfferingsBean != null && productOfferingsBean.getAddons() != null) {
									productSolutionBean.setIzosdwanAddonsBeans(productOfferingsBean.getAddons());
								}
								if (solution.getQuoteToLeProductFamily().getMstProductFamily().getName()
										.equals(IzosdwanCommonConstants.VUTM)) {
									LOGGER.info("Got vUTM solution");
									productSolutionBean.setComponents(
											constructQuoteProductComponentVutm(solution.getId(), false, false));
								}
							} catch (Exception e) {
								LOGGER.error("Error occured on mapping the addons!!", e);
							}
						} else {
							try {

								VproxySolutionBean vproxyProductOfferingBean = Utils.convertJsonToObject(
										solution.getProductProfileData(), VproxySolutionBean.class);
								productSolutionBean.setVproxySolutionBean(vproxyProductOfferingBean);
								productSolutionBean.setComponents(
										constructQuoteProductComponentVproxy(solution.getId(), false, false));

							} catch (Exception e) {
								LOGGER.error("Error occured on mapping the vproxy offering details!!", e);
							}
						}
					} else {

					}
				}
				if (solution.getProductProfileData() != null) {
					productSolutionBean.setSolution(
							Utils.convertJsonToObject(solution.getProductProfileData(), SolutionDetail.class));
				}
				List<QuoteIllSiteBean> illSiteBeans = getSortedIllSiteDtos(
						constructIllSiteDtos(getIllsitesBasenOnVersion(solution, siteId, siteIds), isFeasibleSites,
								isSiteProperitiesRequired));
				productSolutionBean.setSites(illSiteBeans);
				List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
						.findByQuote(quote);
				if (isVproxy) {
					for (QuoteIzoSdwanAttributeValues val : attributeValues) {
						if ((val.getDisplayValue().equals(IzosdwanCommonConstants.ISSWG))
								&& productSolutionBean.getOfferingName().contains(IzosdwanCommonConstants.SWG)) {
							if (val.getAttributeValue().equals(CommonConstants.YES)) {
								productSolutionBeans.add(productSolutionBean);
							}
						}
						if ((val.getDisplayValue().equals(IzosdwanCommonConstants.ISSPA))
								&& productSolutionBean.getOfferingName().contains(IzosdwanCommonConstants.SPA)) {
							if (val.getAttributeValue().equals(CommonConstants.YES)) {
								productSolutionBeans.add(productSolutionBean);
							}
						}
					}
				} else {
					productSolutionBeans.add(productSolutionBean);

				}

			}
		}
		return productSolutionBeans;
	}

	public boolean isCellEmpty(Cell c) {
		return c == null || c.getCellTypeEnum() == CellType.BLANK
				|| (c.getCellTypeEnum() == CellType.STRING && c.getStringCellValue().isEmpty());
	}

	private List<ProductSolutionBean> getSortedSolution(List<ProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(ProductSolutionBean::getProductSolutionId));
		}

		return solutionBeans;

	}

	public int getLastRowWithData(Sheet currentSheet) {
		int rowCount = 0;
		Iterator<Row> iter = currentSheet.rowIterator();
		while (iter.hasNext()) {
			Row r = iter.next();
			if (!this.isRowBlank(r)) {
				rowCount = r.getRowNum();
			}
		}
		return rowCount;
	}

	public boolean isRowBlank(Row r) {
		boolean ret = true;

		/*
		 * If a row is null, it must be blank.
		 */
		if (r != null) {
			Iterator<Cell> cellIter = r.cellIterator();
			/*
			 * Iterate through all cells in a row.
			 */
			while (cellIter.hasNext()) {
				/*
				 * If one of the cells in given row contains data, the row is considered not
				 * blank.
				 */
				if (!this.isCellBlank(cellIter.next())) {
					ret = false;
					break;
				}
			}
		}

		return ret;
	}

	public boolean isCellBlank(Cell c) {
		return (c == null || c.getCellTypeEnum() == CellType.BLANK);
	}

	/**
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteIllSiteBean> getSortedIllSiteDtos(List<QuoteIllSiteBean> illSiteBeans) {
		if (illSiteBeans != null) {
			illSiteBeans.sort(Comparator.comparingInt(QuoteIllSiteBean::getSiteId));

		}

		return illSiteBeans;
	}

	/**
	 * 
	 * constructProductSolution - This method is used to construct the product
	 * Solution entity
	 * 
	 * @param mstProductOffering
	 * @param quoteToLeProductFamily
	 * @param productProfileData
	 * @return ProductSolution
	 */

	private ProductSolution constructProductSolution(MstProductOffering mstProductOffering,
			QuoteToLeProductFamily quoteToLeProductFamily, String productProfileData) {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setMstProductOffering(mstProductOffering);
		productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolution.setProductProfileData(productProfileData);
		return productSolution;
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<ProductSolution> getProductSolutionBasenOnVersion(QuoteToLeProductFamily family) {
		List<ProductSolution> productSolutions = null;
		productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(family);
		return productSolutions;

	}

	/**
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteIllSiteBean> constructIllSiteDtos(List<QuoteIzosdwanSite> illSites, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired) throws TclCommonException {
		if (isSiteProperitiesRequired == null) {
			isSiteProperitiesRequired = false;
		}

		List<QuoteIllSiteBean> sites = new ArrayList<>();
		if (illSites != null) {
			for (QuoteIzosdwanSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
					if (!isFeasibleSites && !illSite.getFeasibility().equals(CommonConstants.BACTIVE)) {
						continue;
					}
					// Quote quote =
					// illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
					QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					QuoteIllSiteBean illSiteBean = new QuoteIllSiteBean(illSite);
					illSiteBean.setQuoteSla(constructSlaDetails(illSite));
					illSiteBean.setFeasibility(constructSiteFeasibility(illSite));
					List<QuoteProductComponentBean> quoteProductComponentBeans = getSortedComponents(
							constructQuoteProductComponent(illSite.getId(), false, isSiteProperitiesRequired));
					illSiteBean.setComponents(quoteProductComponentBeans);
					illSiteBean.setChangeBandwidthFlag(illSite.getMacdChangeBandwidthFlag());
					illSiteBean.setIsTaskTriggered(illSite.getIsTaskTriggered());
					if (illSite.getMfTaskTriggered() != null) {
						illSiteBean.setMfTaskTriggered(illSite.getMfTaskTriggered());
					} else {
						illSiteBean.setMfTaskTriggered(0);

					}
					illSiteBean.setMfStatus(illSite.getMfStatus());
					/*
					 * try { illSiteBean
					 * .setExistingComponentsListMap((constructExistingComponentsforIsvPage(
					 * quoteToLe, illSiteBean,illSite))); }catch(Exception e) {
					 * LOGGER.error("Error on existing component map ",e); }
					 */
					sites.add(illSiteBean);
				}
			}
		}
		return sites;
	}

	/**
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */

	/**
	 * Method to display the existing components in ISV page for primary and
	 * secondary
	 * 
	 * @param quoteToLe
	 * @param illSiteBean
	 * @return Map<String ,Object>
	 * @throws TclCommonException
	 */
	private List<Map> constructExistingComponentsforIsvPage(QuoteToLe quoteToLe, QuoteIllSiteBean illSiteBean,
			QuoteIzosdwanSite illSite) throws TclCommonException {
		List<Map> existingComponentsList = new ArrayList<>();

		String serviceId = illSite.getErfServiceInventoryTpsServiceId();
		Integer orderId = illSite.getSiParentOrderId();
		String secondaryServiceId = null;
		Integer primaryOrderId = null;
		Integer secondaryOrderId = null;

		/*
		 * if (Objects.nonNull(quoteToLe) &&
		 * MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) &&
		 * Objects.nonNull(serviceId) && Objects.nonNull(orderId)) { Map<String, Object>
		 * primaryComponentsMap = new HashMap<>(); Map<String, Object>
		 * secondaryComponentsMap = new HashMap<>(); Boolean isSecondary = false;
		 * SIServiceDetailDataBean sIServiceDetailDataBean = getOldQuotesNRCARC(orderId,
		 * serviceId); SIServiceDetailDataBean secondaryaryServiceDataBean = new
		 * SIServiceDetailDataBean(); SIServiceDetailDataBean primaryServiceDataBean =
		 * new SIServiceDetailDataBean(); isSecondary =
		 * Objects.nonNull(sIServiceDetailDataBean.getPriSecServLink()); String linkType
		 * = sIServiceDetailDataBean.getLinkType();
		 * 
		 * if (linkType.equalsIgnoreCase("PRIMARY") ||
		 * linkType.equalsIgnoreCase("SINGLE")) { if (isSecondary) { secondaryServiceId
		 * = sIServiceDetailDataBean.getPriSecServLink(); secondaryOrderId =
		 * getOrderIdFromServiceId(secondaryServiceId); secondaryaryServiceDataBean =
		 * getOldQuotesNRCARC(secondaryOrderId, secondaryServiceId); }
		 * primaryServiceDataBean = sIServiceDetailDataBean; }
		 * 
		 * else if (isSecondary && linkType.equalsIgnoreCase("SECONDARY")) { serviceId =
		 * sIServiceDetailDataBean.getPriSecServLink(); primaryOrderId =
		 * getOrderIdFromServiceId(serviceId); primaryServiceDataBean =
		 * getOldQuotesNRCARC(primaryOrderId, serviceId); secondaryaryServiceDataBean =
		 * sIServiceDetailDataBean; }
		 * 
		 * primaryComponentsMap.put("type", MACDConstants.PRIMARY_STRING);
		 * primaryComponentsMap.put("contractTerm",
		 * Objects.nonNull(primaryServiceDataBean.getContractTerm()) ?
		 * primaryServiceDataBean.getContractTerm() : 0);
		 * primaryComponentsMap.put("portBw",
		 * Objects.nonNull(primaryServiceDataBean.getPortBw()) ?
		 * primaryServiceDataBean.getPortBw() : CommonConstants.NULL);
		 * primaryComponentsMap.put("oldArc",
		 * Objects.nonNull(primaryServiceDataBean.getArc()) ?
		 * primaryServiceDataBean.getArc() : 0); primaryComponentsMap.put("oldNrc",
		 * Objects.nonNull(primaryServiceDataBean.getNrc()) ?
		 * primaryServiceDataBean.getNrc() : 0); primaryComponentsMap.put("serviceId",
		 * Objects.nonNull(serviceId) ? serviceId : CommonConstants.NULL);
		 * existingComponentsList.add(0, primaryComponentsMap);
		 * 
		 * if (isSecondary) { secondaryComponentsMap.put("type",
		 * MACDConstants.SECONDARY_STRING); secondaryComponentsMap.put("contractTerm",
		 * Objects.nonNull(secondaryaryServiceDataBean.getContractTerm()) ?
		 * secondaryaryServiceDataBean.getContractTerm() : 0);
		 * secondaryComponentsMap.put("portBw",
		 * Objects.nonNull(secondaryaryServiceDataBean.getPortBw()) ?
		 * secondaryaryServiceDataBean.getPortBw() : CommonConstants.NULL);
		 * secondaryComponentsMap.put("oldArc",
		 * Objects.nonNull(secondaryaryServiceDataBean.getArc()) ?
		 * secondaryaryServiceDataBean.getArc() : 0);
		 * secondaryComponentsMap.put("oldNrc",
		 * Objects.nonNull(secondaryaryServiceDataBean.getNrc()) ?
		 * secondaryaryServiceDataBean.getNrc() : 0);
		 * secondaryComponentsMap.put("serviceId", secondaryServiceId);
		 * existingComponentsList.add(1, secondaryComponentsMap); }
		 * 
		 * }
		 */

		Map<String, Object> primaryComponentsMap = new HashMap<>();
		Map<String, Object> secondaryComponentsMap = new HashMap<>();
		Boolean isSecondary = false;
		SIServiceDetailDataBean sIServiceDetailDataBean = getOldQuotesNRCARC(orderId, serviceId);
		SIServiceDetailDataBean secondaryaryServiceDataBean = new SIServiceDetailDataBean();
		SIServiceDetailDataBean primaryServiceDataBean = new SIServiceDetailDataBean();
		isSecondary = Objects.nonNull(sIServiceDetailDataBean.getPriSecServLink());
		String linkType = sIServiceDetailDataBean.getLinkType();

		if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE")) {
			if (isSecondary) {
				secondaryServiceId = sIServiceDetailDataBean.getPriSecServLink();
				secondaryOrderId = getOrderIdFromServiceId(secondaryServiceId);
				secondaryaryServiceDataBean = getOldQuotesNRCARC(secondaryOrderId, secondaryServiceId);
			}
			primaryServiceDataBean = sIServiceDetailDataBean;
		}

		else if (isSecondary && linkType.equalsIgnoreCase("SECONDARY")) {
			serviceId = sIServiceDetailDataBean.getPriSecServLink();
			primaryOrderId = getOrderIdFromServiceId(serviceId);
			primaryServiceDataBean = getOldQuotesNRCARC(primaryOrderId, serviceId);
			secondaryaryServiceDataBean = sIServiceDetailDataBean;
		}

		primaryComponentsMap.put("type", MACDConstants.PRIMARY_STRING);
		primaryComponentsMap.put("contractTerm",
				Objects.nonNull(primaryServiceDataBean.getContractTerm()) ? primaryServiceDataBean.getContractTerm()
						: 0);
		primaryComponentsMap.put("portBw",
				Objects.nonNull(primaryServiceDataBean.getPortBw()) ? primaryServiceDataBean.getPortBw()
						: CommonConstants.NULL);
		primaryComponentsMap.put("oldArc",
				Objects.nonNull(primaryServiceDataBean.getArc()) ? primaryServiceDataBean.getArc() : 0);
		primaryComponentsMap.put("oldNrc",
				Objects.nonNull(primaryServiceDataBean.getNrc()) ? primaryServiceDataBean.getNrc() : 0);
		primaryComponentsMap.put("serviceId", Objects.nonNull(serviceId) ? serviceId : CommonConstants.NULL);
		existingComponentsList.add(0, primaryComponentsMap);

		if (isSecondary) {
			secondaryComponentsMap.put("type", MACDConstants.SECONDARY_STRING);
			secondaryComponentsMap.put("contractTerm",
					Objects.nonNull(secondaryaryServiceDataBean.getContractTerm())
							? secondaryaryServiceDataBean.getContractTerm()
							: 0);
			secondaryComponentsMap.put("portBw",
					Objects.nonNull(secondaryaryServiceDataBean.getPortBw()) ? secondaryaryServiceDataBean.getPortBw()
							: CommonConstants.NULL);
			secondaryComponentsMap.put("oldArc",
					Objects.nonNull(secondaryaryServiceDataBean.getArc()) ? secondaryaryServiceDataBean.getArc() : 0);
			secondaryComponentsMap.put("oldNrc",
					Objects.nonNull(secondaryaryServiceDataBean.getNrc()) ? secondaryaryServiceDataBean.getNrc() : 0);
			secondaryComponentsMap.put("serviceId", secondaryServiceId);
			existingComponentsList.add(1, secondaryComponentsMap);
		}

		return existingComponentsList;
	}

	/**
	 * constructSiteFeasibility
	 * 
	 * @param illSite
	 * @return
	 */
	private List<SiteFeasibilityBean> constructSiteFeasibility(QuoteIzosdwanSite illSite) {
		List<SiteFeasibilityBean> siteFeasibilityBeans = new ArrayList<>();
		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIzosdwanSiteAndIsSelected(illSite, (byte) 1);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			for (IzosdwanSiteFeasibility siteFeasibility : siteFeasibilities) {
				siteFeasibilityBeans.add(constructSiteFeasibility(siteFeasibility));
			}
		}
		return siteFeasibilityBeans;
	}

	/**
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteProductComponentBean> getSortedComponents(List<QuoteProductComponentBean> quoteComponentBeans) {
		if (quoteComponentBeans != null) {
			quoteComponentBeans.sort(Comparator.comparingInt(QuoteProductComponentBean::getComponentId));

		}

		return quoteComponentBeans;
	}

	public Integer getOrderIdFromServiceId(String tpsId) throws TclCommonException {
		String responseOrderId = (String) mqUtils.sendAndReceive(orderIdCorrespondingToServId, tpsId);
		return Utils.convertJsonToObject(responseOrderId, Integer.class);
	}

	/**
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent
	 * @param id,version
	 */
	private List<QuoteProductComponentBean> constructQuoteProductComponent(Integer id, boolean isSitePropertiesNeeded,
			boolean isSitePropNeeded) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenOnVersion(id, isSitePropertiesNeeded,
				isSitePropNeeded);

		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				QuoteProductComponentBean quoteProductComponentBean = new QuoteProductComponentBean();
				quoteProductComponentBean.setComponentId(quoteProductComponent.getId());
				quoteProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					quoteProductComponentBean
							.setComponentMasterId(quoteProductComponent.getMstProductComponent().getId());
					quoteProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					quoteProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				quoteProductComponentBean.setType(quoteProductComponent.getType());
				quoteProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(quoteProductComponent.getId(),
								isSitePropertiesNeeded, isSitePropNeeded)));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			}

		}
		return quoteProductComponentDtos;

	}

	private List<QuoteProductComponentBean> constructQuoteProductComponentCgw(Integer id,
			boolean isSitePropertiesNeeded, boolean isSitePropNeeded) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getCgwComponents(id,
				IzosdwanCommonConstants.CLOUD_GATEWAY_PORT);

		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				QuoteProductComponentBean quoteProductComponentBean = new QuoteProductComponentBean();
				quoteProductComponentBean.setComponentId(quoteProductComponent.getId());
				quoteProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					quoteProductComponentBean
							.setComponentMasterId(quoteProductComponent.getMstProductComponent().getId());
					quoteProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					quoteProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				quoteProductComponentBean.setType(quoteProductComponent.getType());
				quoteProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(quoteProductComponent.getId(),
								isSitePropertiesNeeded, isSitePropNeeded)));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			}

		}
		return quoteProductComponentDtos;

	}

	/**
	 * @link http://www.tatacommunications.com constructAttribute used to constrcut
	 *       attribute
	 * @param quoteProductComponentsAttributeValues
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValueBean> constructAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		if (quoteProductComponentsAttributeValues != null) {
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {

				QuoteProductComponentsAttributeValueBean qtAttributeValue = new QuoteProductComponentsAttributeValueBean(
						attributeValue);
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setAttributeMasterId(productAttributeMaster.getId());
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}
				qtAttributeValue.setAttributeId(attributeValue.getId());
				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				quoteProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return quoteProductComponentsAttributeValueBean;
	}

	/**
	 * @param isSitePropertiesNeeded
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */

	private List<QuoteProductComponent> getComponentBasenOnVersion(Integer siteId, boolean isSitePropertiesNeeded,
			boolean isSitePropNeeded) {
		List<QuoteProductComponent> components = null;
		components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
				QuoteConstants.IZOSDWAN_SITES.toString());

		return components;

	}

	public List<QuoteProductComponent> getCgwComponents(Integer cgwId, String componentName) {
		List<QuoteProductComponent> components = null;
		components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(cgwId,
				componentName, IzosdwanCommonConstants.IZOSDWAN_CGW);
		return components;
	}

	public List<QuoteProductComponent> getComponentBasenVproxy(Integer siteId, boolean isSitePropertiesNeeded,
			boolean isSitePropNeeded) {
		List<QuoteProductComponent> components = null;
		components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
				IzosdwanCommonConstants.IZOSDWAN_VPROXY);

		return components;

	}

	public List<QuoteProductComponent> getComponentBasenVutm(Integer siteId, boolean isSitePropertiesNeeded,
			boolean isSitePropNeeded) {
		List<QuoteProductComponent> components = null;
		components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
				IzosdwanCommonConstants.IZOSDWAN_VUTM);

		return components;

	}

	/**
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<QuoteProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(QuoteProductComponentsAttributeValueBean::getAttributeId));

		}

		return attributeBeans;
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @constructAttributePriceDto used to construct attribute price
	 */
	private QuotePriceBean constructAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findFirstByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			if (attrPrice != null) {
				priceDto = new QuotePriceBean(attrPrice);
				if (priceDto != null) {
					priceDto.setEffectiveArc(priceDto.getEffectiveArc() != null
							? Double.parseDouble(decimalFormat.format(priceDto.getEffectiveArc()))
							: 0D);
					priceDto.setEffectiveNrc(priceDto.getEffectiveNrc() != null
							? Double.parseDouble(decimalFormat.format(priceDto.getEffectiveNrc()))
							: 0D);
					Double mrc = priceDto.getEffectiveArc()/12;
					if(priceDto.getEffectiveMrc() != null && priceDto.getEffectiveMrc() == 0) {
						priceDto.setEffectiveMrc(Double.parseDouble(decimalFormat.format(mrc)));
					}
					else if(priceDto.getEffectiveMrc() != null && priceDto.getEffectiveMrc() > 0){
						priceDto.setEffectiveMrc(Double.parseDouble(decimalFormat.format(priceDto.getEffectiveMrc())));
					}else{
						priceDto.setEffectiveMrc(0D);
					}
				}
			}
		}
		return priceDto;

	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
			boolean isSitePropRequire, Boolean isSiteRequired) {
		List<QuoteProductComponentsAttributeValue> attributes = null;

		attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

		if (isSitePropRequire) {
			attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentId,
							IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
		} else if (isSiteRequired) {
			attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);
		} else {

			if (attributes != null) {
				return attributes.stream()
						.filter(attr -> (!attr.getProductAttributeMaster().getName()
								.equals(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}

		return attributes;

	}

	/**
	 * constructComponentPriceDto used to get price of componenet
	 * 
	 * @link http://www.tatacommunications.com/
	 * @param QuoteProductComponent
	 */
	private QuotePriceBean constructComponentPriceDto(QuoteProductComponent quoteProductComponent) {
		QuotePriceBean priceDto = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			List<QuotePrice> prices = quotePriceRepository.findByReferenceNameAndReferenceId(
					QuoteConstants.COMPONENTS.toString(), String.valueOf(quoteProductComponent.getId()));
			if (prices != null && !prices.isEmpty())
				priceDto = new QuotePriceBean(prices.get(0));
			if (priceDto != null) {
				priceDto.setEffectiveArc(priceDto.getEffectiveArc() != null
						? (Double.parseDouble(decimalFormat.format(priceDto.getEffectiveArc())))
						: 0D);
				priceDto.setEffectiveNrc(priceDto.getEffectiveNrc() != null
						? (Double.parseDouble(decimalFormat.format(priceDto.getEffectiveNrc())))
						: 0D);

				Double mrc = priceDto.getEffectiveArc()/12;
				if(priceDto.getEffectiveMrc() != null && priceDto.getEffectiveMrc() == 0) {
					priceDto.setEffectiveMrc(Double.parseDouble(decimalFormat.format(mrc)));
				}
				else if(priceDto.getEffectiveMrc() != null && priceDto.getEffectiveMrc() > 0){
					priceDto.setEffectiveMrc(Double.parseDouble(decimalFormat.format(priceDto.getEffectiveMrc())));
				}else{
					priceDto.setEffectiveMrc(0D);
				}
			}

		}
		return priceDto;

	}

	/**
	 * constructSlaDetails
	 * 
	 * @param illSite
	 */
	private List<QuoteSlaBean> constructSlaDetails(QuoteIzosdwanSite illSite) {

		List<QuoteSlaBean> quoteSlas = new ArrayList<>();

		if (illSite.getQuoteIzosdwanSiteSlas() != null) {

			illSite.getQuoteIzosdwanSiteSlas().forEach(siteSla -> {
				QuoteSlaBean sla = new QuoteSlaBean();
				sla.setId(siteSla.getId());
				sla.setSlaEndDate(siteSla.getSlaEndDate());
				sla.setSlaStartDate(siteSla.getSlaStartDate());
				sla.setSlaValue(Utils.convertEval(siteSla.getSlaValue()));
				if (siteSla.getSlaMaster() != null) {
					SlaMaster slaMaster = siteSla.getSlaMaster();
					SlaMasterBean master = new SlaMasterBean();
					master.setId(siteSla.getId());
					master.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
					master.setSlaName(slaMaster.getSlaName());
					sla.setSlaMaster(master);
				}

				quoteSlas.add(sla);
			});
		}

		return quoteSlas;

	}

	/**
	 * 
	 * getProductOffering - This method takes in the
	 * {@link MstProductFamily},productOfferingName and gets back
	 * {@link MstProductOffering}
	 * 
	 * @param mstProductFamily
	 * @param productOfferingName
	 * @return MstProductOffering
	 * @throws TclCommonException
	 */
	protected MstProductOffering getProductOffering(MstProductFamily mstProductFamily, String productOfferingName,
			User user, String vendor) throws TclCommonException {
		MstProductOffering productOffering = null;

		productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatusAndVendorCd(
				mstProductFamily, productOfferingName, (byte) 1, vendor);
		if (productOffering == null) {
			productOffering = new MstProductOffering();
			productOffering.setCreatedBy(user.getUsername());
			productOffering.setCreatedTime(new Date());
			productOffering.setMstProductFamily(mstProductFamily);
			productOffering.setProductName(productOfferingName);
			productOffering.setStatus((byte) 1);
			productOffering.setProductDescription(productOfferingName);
			productOffering.setVendorCd(vendor);
			mstProductOfferingRepository.save(productOffering);

		}
		return productOffering;
	}

	/**
	 * constructSiteFeasibility
	 * 
	 * @param siteFeasibility
	 * @return
	 */
	private SiteFeasibilityBean constructSiteFeasibility(IzosdwanSiteFeasibility siteFeasibility) {
		SiteFeasibilityBean siteFeasibilityBean = new SiteFeasibilityBean();
		siteFeasibilityBean.setFeasibilityCheck(siteFeasibility.getFeasibilityCheck());
		siteFeasibilityBean.setFeasibilityCode(siteFeasibility.getFeasibilityCode());
		siteFeasibilityBean.setFeasibilityMode(siteFeasibility.getFeasibilityMode());
		siteFeasibilityBean.setType(siteFeasibility.getType());
		siteFeasibilityBean.setCreatedTime(siteFeasibility.getCreatedTime());
		siteFeasibilityBean.setProvider(siteFeasibility.getProvider());
		siteFeasibilityBean.setRank(siteFeasibility.getRank());
		siteFeasibilityBean.setResponseJson(siteFeasibility.getResponseJson());
		siteFeasibilityBean.setFeasibilityType(siteFeasibility.getFeasibilityType());
		siteFeasibility.setSfdcFeasibilityId(siteFeasibility.getSfdcFeasibilityId());
		siteFeasibilityBean.setIsSelected(siteFeasibility.getIsSelected());
		return siteFeasibilityBean;
	}

	/**
	 * 
	 * constructQuoteToLeProductFamily-This method construct the
	 * quoteToLeProductFamily entity
	 * 
	 * @param mstProductFamily
	 * @param quoteToLe
	 * @return QuoteToLeProductFamily
	 */

	private QuoteToLeProductFamily constructQuoteToLeProductFamily(MstProductFamily mstProductFamily,
			QuoteToLe quoteToLe) {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
		quoteToLeProductFamily.setQuoteToLe(quoteToLe);
		return quoteToLeProductFamily;

	}

	/**
	 * 
	 * getCustomerId- This method persists the customer if not present or get the
	 * customer details if already present
	 * 
	 * @param customerAcid
	 * @return Customer
	 * @throws TclCommonException
	 */
	private Customer getCustomerId(Integer erfCustomerId) throws TclCommonException {
		Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(erfCustomerId, (byte) 1);
		if (customer == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		return customer;

	}

	/**
	 * persistQuoteLeAttributes
	 * 
	 * @param user
	 * @param quoteTole
	 */
	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole) {
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NAME.toString(),
				user.getFirstName());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_EMAIL.toString(),
				user.getEmailId());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_ID.toString(),
				user.getUsername());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NO.toString(),
				user.getContactNo());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.DESIGNATION.toString(),
				user.getDesignation());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.RECURRING_CHARGE_TYPE.toString(), "ARC");
		// update trigram
		try {
			CustomerDetailsBean customerDetails = processCustomerData(
					quoteTole.getQuote().getCustomer().getErfCusCustomerId());
			if (customerDetails.getCustomerAttributes() != null && !customerDetails.getCustomerAttributes().isEmpty()) {
				customerDetails.getCustomerAttributes().stream().forEach(attr -> {
					if (attr.getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_TRIGRAM))
						updateLeAttribute(quoteTole, user.getUsername(),
								LeAttributesConstants.CUSTOMER_TRIGRAM.toString(), attr.getValue());
				});
			}
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * 
	 * @throws TclCommonException
	 * 
	 */

	private List<QuoteIzosdwanSite> getIllsitesBasenOnVersion(ProductSolution productSolution, Integer siteId,
			List<Integer> siteIds) {

		List<QuoteIzosdwanSite> illsites = new ArrayList<>();

		if (siteId != null) {

			Optional<QuoteIzosdwanSite> quoteIllSite = quoteIzosdwanSiteRepository.findById(siteId);

			if (quoteIllSite.isPresent()) {

				illsites.add(quoteIllSite.get());

			}

		} else if (siteIds != null && !siteIds.isEmpty()) {
			illsites = quoteIzosdwanSiteRepository.findByStatusAndIdIn(CommonConstants.BACTIVE, siteIds);
		} else {

			illsites = quoteIzosdwanSiteRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);

		}

		return illsites;

	}

	/**
	 * updateConstactInfo
	 * 
	 * @param quoteTole
	 * @param user
	 */
	public void updateLeAttribute(QuoteToLe quoteTole, String userName, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;

		List<MstOmsAttribute> mstOmsAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(name, (byte) 1);

		if (!mstOmsAttributesList.isEmpty()) {
			mstOmsAttribute = mstOmsAttributesList.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(userName);
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(name);
			mstOmsAttribute.setDescription(value);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}

		constructLegaAttribute(mstOmsAttribute, quoteTole, name, value);

	}

	/**
	 * @link http://www.tatacommunications.com/ constructLegaAttribute used to
	 *       construct legal attributes
	 * @param mstOmsAttribute
	 * @param quoteTole
	 */
	private void constructLegaAttribute(MstOmsAttribute mstOmsAttribute, QuoteToLe quoteTole, String name,
			String value) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteTole, name);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(value);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			attributeValue.setQuoteToLe(quoteTole);
			attributeValue.setDisplayValue(name);
			quoteLeAttributeValueRepository.save(attributeValue);
		} else {
			updateLeAttrbute(quoteLeAttributeValues, name, value);
		}
		LOGGER.info("Updated the attribute value for {} to {}", name, value);
	}

	/**
	 * updateLeAttrbute
	 * 
	 * @param quoteLeAttributeValues
	 * @param name
	 * @param value
	 */
	private void updateLeAttrbute(List<QuoteLeAttributeValue> quoteLeAttributeValues, String name, String value) {
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setAttributeValue(value);
				attrVal.setDisplayValue(name);
				quoteLeAttributeValueRepository.save(attrVal);

			});
		}

	}

	/**
	 * 
	 * constructQuote-This method constructs quote entity
	 * 
	 * @param customer
	 * @param userId
	 * @return Quote
	 * @param productName
	 * @param engagementOptyId
	 */
	private Quote constructQuote(Customer customer, Integer userId, String productName, String engagementOptyId,
			String quoteCode, String vendorName, Integer quoteId) {
		Quote quote = new Quote();
		if (quoteId != null) {
			Quote quoteOpt = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			if (quoteOpt != null) {
				quote = quoteOpt;
			}
		}
		quote.setCustomer(customer);
		quote.setCreatedBy(userId);
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setIzosdwanFlavour(vendorName);
//    quote.setQuoteCode(Utils.generateRefId(prodName));
		quote.setQuoteCode(
				null != engagementOptyId ? quoteCode : Utils.generateRefIdIzosdwan(IzosdwanCommonConstants.IZOSDWAN));
		quote.setEngagementOptyId(engagementOptyId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date()); // Now use today date.
		cal.add(Calendar.DATE, 130); // Adding 130 days
		quote.setEffectiveDate(cal.getTime());
		return quote;
	}

	/**
	 *
	 * constructQuoteToLe -This method is used to construct QuoteToLe
	 *
	 * @param quote
	 * @param quoteDetail
	 * @return QuoteToLe
	 * @throws TclCommonException 
	 */
	private QuoteToLe constructQuoteToLe(Quote quote, QuoteDetail quoteDetail) throws TclCommonException {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(IzosdwanCommonConstants.SELECT_SERVICES);
		quoteToLe.setTermInMonths("12 months");
		quoteToLe.setCurrencyCode("INR");
	    
		
		quoteToLe.setQuoteType(CommonConstants.NEW);
		quoteToLe.setIsAmended(CommonConstants.BDEACTIVATE);
		quoteToLe.setClassification(quoteDetail.getClassification());
		if (quoteDetail.getCustomerLeId() != null) {
			quoteToLe.setErfCusCustomerLegalEntityId(quoteDetail.getCustomerLeId());
		}
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			Opportunity opportunity = opportunityRepository.findByUuid(quote.getQuoteCode());
			quoteToLe.setTpsSfdcOptyId(opportunity.getTpsOptyId());
			quoteToLe.setErfCusCustomerLegalEntityId(partnerService
					.getCustomerLeIdFromEngagementOpportunityId(Integer.valueOf(quoteDetail.getEngagementOptyId())));
		}
		
		if (quoteDetail.getNs_quote() != null && quoteDetail.getNs_quote().equals("Y")) {
			
			try {
				String mqCurrency;
				mqCurrency = (String) mqUtils.sendAndReceive(getCurrency, String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
				quoteToLe.setCurrencyCode(mqCurrency);
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}

		return quoteToLe;
	}

	/**
	 * 
	 * getProductFamily - This methods gets the {@link MstProductFamily} from the
	 * given product name
	 * 
	 * @param productName
	 * @return MstProductFamily
	 * @throws TclCommonException
	 */
	protected MstProductFamily getProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;

	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	protected Quote getQuote(Integer quoteId) throws TclCommonException {

		Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}

		return quote;
	}

	/**
	 * removeUnselectedSolution-remove unselected solution
	 * 
	 * @param quoteDetail
	 * @param quoteToLeProductFamily
	 */
	private void removeUnselectedSolution(QuoteDetail quoteDetail, QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLe quoteToLe) {
		ProductSolution productSolution = productSolutionRepository
				.findByReferenceIdForIzoSdwan(quoteDetail.getQuoteId());

		if (productSolution != null) {
			for (ProductOfferingsBean solution : quoteDetail.getIzosdwanSolutions()) {
				if (!solution.getProductOfferingsName()
						.equals(productSolution.getMstProductOffering().getProductName())) {
					List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
							.findByStatusAndQuote_id(IzosdwanCommonConstants.MIGRATED, quoteDetail.getQuoteId());
					if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
						LOGGER.info("Got already migrated BYON records!!");
						updateByonUploadDetailsStatus(IzosdwanCommonConstants.COMPLETED,
								quoteIzosdwanByonUploadDetails);
					}
					for (QuoteIzosdwanSite illSites : productSolution.getQuoteIzoSdwanSites()) {
						removeComponentsAndAttr(illSites.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES);
						deletedIllsiteAndRelation(illSites);
					}
					bundleOmsSfdcService.processDeleteProduct(quoteToLe, quoteToLeProductFamily, productSolution);
					productSolutionRepository.delete(productSolution);
				}
			}
		}

	}

	private void removeAllSolutionVproxy(QuoteDetail quoteDetail, QuoteToLe quoteToLe, Boolean deleteProduct) {
		List<ProductSolution> productSolution = productSolutionRepository
				.findByReferenceIdForVproxy(quoteDetail.getQuoteId());

		if (productSolution != null && !productSolution.isEmpty()) {
			productSolution.stream().forEach(solution -> {
				removeComponentsAndAttr(solution.getId(), IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				removeComponentsAndAttr(solution.getQuoteToLeProductFamily().getId(),
						IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				productSolutionRepository.delete(solution);
			});
			if (deleteProduct) {
				bundleOmsSfdcService.processDeleteProduct(quoteToLe, productSolution.get(0).getQuoteToLeProductFamily(),
						productSolution.get(0));
			}

		}

	}

	private void removeUnselectedSolutionVproxy(QuoteDetail quoteDetail, QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLe quoteToLe, ProductSolution productSolution) {

		removeComponentsAndAttr(productSolution.getId(), IzosdwanCommonConstants.IZOSDWAN_VPROXY);
		productSolutionRepository.delete(productSolution);

	}

	private void removeAllSolutionVutm(Integer quoteId, QuoteToLe quoteToLe) {
		ProductSolution productSolution = productSolutionRepository.findByReferenceIdForVutm(quoteId);

		if (productSolution != null) {

			removeComponentsAndAttr(productSolution.getId(), IzosdwanCommonConstants.IZOSDWAN_VUTM);
			removeComponentsAndAttr(productSolution.getQuoteToLeProductFamily().getId(),
					IzosdwanCommonConstants.IZOSDWAN_VUTM);
			productSolutionRepository.delete(productSolution);

			// bundleOmsSfdcService.processDeleteProduct(quoteToLe,
			// productSolution.get(0).getQuoteToLeProductFamily(), productSolution.get(0));

		}

	}

	/**
	 * removeComponentsAndAttr
	 * 
	 * @param siteId
	 */
	private void removeComponentsAndAttr(Integer siteId, String referenceName) {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(siteId, referenceName);
		if (!quoteProductComponents.isEmpty()) {
			quoteProductComponents.forEach(quoteProd -> {

				quoteProd.getQuoteProductComponentsAttributeValues()
						.forEach(attr -> quoteProductComponentsAttributeValueRepository.delete(attr));
				quoteProductComponentRepository.delete(quoteProd);
			});
		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @param familyName
	 * @link http://www.tatacommunications.com/ deletedIllsiteAndRelation used to
	 *       delete ill site and its relation
	 * 
	 * @param quoteIllSite
	 */
	private void deletedIllsiteAndRelation(QuoteIzosdwanSite quoteIllSite) {
		List<QuoteIzosdwanSiteSla> slas = quoteIzoSdwanSlaRepository.findByQuoteIzosdwanSite(quoteIllSite);
		if (slas != null && !slas.isEmpty()) {
			slas.forEach(sl -> {
				quoteIzoSdwanSlaRepository.delete(sl);
			});
		}
		/*
		 * List<QuoteIllSiteSla> slas =
		 * quoteIllSiteSlaRepository.findByQuoteIllSite(quoteIllSite); if (slas != null
		 * && !slas.isEmpty()) { slas.forEach(sl -> {
		 * quoteIllSiteSlaRepository.delete(sl); quoteIllSiteSlaRepository.flush(); });
		 * } List<SiteFeasibility> siteFeasibilities =
		 * siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite); if
		 * (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
		 * siteFeasibilities.forEach(site -> { List<SiteFeasibilityAudit>
		 * siteFeasibilityAuditList = siteFeasibilityAuditRepository
		 * .findBySiteFeasibility(site); if (!siteFeasibilityAuditList.isEmpty())
		 * siteFeasibilityAuditRepository.deleteAll(siteFeasibilityAuditList);
		 * siteFeasibilityRepository.delete(site); }); }
		 */

//		quoteIzosdwanSiteRepository.delete(quoteIllSite);
		// quoteIllSiteSlaRepository.flush();

		List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
				.findByQuoteIzosdwanSite(quoteIllSite);
		if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
			quoteIllSiteToServiceList.stream().forEach(quoteIllSiteToService -> {
				quoteIllSiteToServiceRepository.delete(quoteIllSiteToService);
			});
		}
		quoteIzosdwanSiteRepository.delete(quoteIllSite);
	}

	/**
	 * 
	 * validateGetQuoteDetail
	 * 
	 * @param quoteId
	 * @param erfCustomerId
	 * @throws TclCommonException
	 */
	protected void validateGetQuoteDetail(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	public String persistIzoSdwanSiteDetails(Integer quoteId, QuoteToLe quoteLe) throws TclCommonException {
		if (quoteId == null || quoteLe == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<Quote> quoteOpt = quoteRepository.findById(quoteId);
			LOGGER.info("quote obj:{}", quoteOpt);
			// quoteOpt.get().getIzosdwanFlavour();
			if (quoteOpt.isPresent()) {
				ProductSolution productSolution = productSolutionRepository
						.findByReferenceIdForIzoSdwan(quoteOpt.get().getId());
				List<QuoteIzosdwanSite> sites = quoteIzosdwanSiteRepository.findByProductSolution(productSolution);
				if (sites != null && !sites.isEmpty()) {
					LOGGER.info("Already sites exists");
					/*
					 * sites.stream().forEach(site->{ removeComponentsAndAttr(site.getId());
					 * deletedIllsiteAndRelation(site); });
					 */
				} else {

					String serviceInvReposne = (String) mqUtils.sendAndReceive(siByCustomerQueue,
							Utils.convertObjectToJson(constructSiSerachBean(quoteOpt.get(), quoteLe)));
					LOGGER.info("Response from Service Inventory {}", serviceInvReposne);
					if (StringUtils.isNotBlank(serviceInvReposne)) {
						LOGGER.info("Got response from SI!!");
						@SuppressWarnings("unchecked")
						List<SiServiceDetailBean> siServiceDetailBeans = GscUtils.fromJson(serviceInvReposne,
								new TypeReference<List<SiServiceDetailBean>>() {
								});
						LOGGER.info("Persisting sites for the solution");
						List<IzoSdwanSiteDetails> siteDet = getSiteDetails();
						if (siteDet != null && !siteDet.isEmpty()) {
							LOGGER.info("Site Detail {} -------> {}", siteDet.size(), siteDet);
						}
						List<IzoSdwanCpeDetails> cpeDetails = getCpeDetails(null, null, null);
						Map<String, ProductAttributeMaster> productAttributeMasterMap = new HashMap<>();
						List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
								.findAll();
						if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
							productAttributeMasters.stream().forEach(master -> {
								productAttributeMasterMap.put(master.getName(), master);
							});
						}
						MstProductComponent mstProductComponent = getProductComponentMasterDetail(
								IzosdwanCommonConstants.SITE_PROPERTIES);
						QuoteIzoSdwanAttributeValues quoteIzoSdwanAttributeValues = new QuoteIzoSdwanAttributeValues();
						List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesList = quoteIzoSdwanAttributeValuesRepository
								.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P, quoteId);
						if (quoteIzoSdwanAttributeValuesList != null && !quoteIzoSdwanAttributeValuesList.isEmpty()) {
							quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesList.get(0);
						}
						QuoteIzoSdwanAttributeValues quoteIzoSdwanContractAttributeValues = new QuoteIzoSdwanAttributeValues();
						List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanContractAttributeValuesList = quoteIzoSdwanAttributeValuesRepository
								.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.MINCONTRACTTERM, quoteId);
						if (quoteIzoSdwanContractAttributeValuesList != null
								&& !quoteIzoSdwanContractAttributeValuesList.isEmpty()) {
							quoteIzoSdwanContractAttributeValues = quoteIzoSdwanContractAttributeValuesList.get(0);
						}
						if (siServiceDetailBeans != null && !siServiceDetailBeans.isEmpty()) {
							persistIzosdwanSites(siServiceDetailBeans, productSolution,
									quoteOpt.get().getIzosdwanFlavour(), siteDet, cpeDetails, productAttributeMasterMap,
									mstProductComponent);
							SiServiceDetailBean siServiceDetailBeanIas = siServiceDetailBeans.stream()
									.filter(si -> si.getProduct().equalsIgnoreCase(IzosdwanCommonConstants.IAS_CODE))
									.findFirst().orElse(null);
							SiServiceDetailBean siServiceDetailBeanGvpn = siServiceDetailBeans.stream()
									.filter(si -> si.getProduct().equalsIgnoreCase(IzosdwanCommonConstants.GVPN_CODE))
									.findFirst().orElse(null);
							if (siServiceDetailBeanIas != null) {
								bundleOmsSfdcService.processProductServiceForSolution(quoteLe, productSolution,
										quoteLe.getTpsSfdcOptyId(), CommonConstants.IAS,false);
							}
							if (siServiceDetailBeanGvpn != null) {
								bundleOmsSfdcService.processProductServiceForSolution(quoteLe, productSolution,
										quoteLe.getTpsSfdcOptyId(), CommonConstants.GVPN,false);
							}
							quoteIzoSdwanAttributeValues.setAttributeValue("false");
							quoteIzoSdwanAttributeValues.setDisplayValue(IzosdwanCommonConstants.BYON100P);
							quoteIzoSdwanAttributeValues.setQuote(quoteOpt.get());
							quoteIzoSdwanAttributeValuesRepository.save(quoteIzoSdwanAttributeValues);
						} else {
							List<QuoteIzoSdwanAttributeValues> attrVals = new ArrayList<>();
							quoteIzoSdwanAttributeValues.setAttributeValue("true");
							quoteIzoSdwanAttributeValues.setDisplayValue(IzosdwanCommonConstants.BYON100P);
							quoteIzoSdwanAttributeValues.setQuote(quoteOpt.get());
							quoteIzoSdwanContractAttributeValues.setAttributeValue(
									quoteToLeRepository.findById(quoteLe.getId()).get().getTermInMonths());
							quoteIzoSdwanContractAttributeValues
									.setDisplayValue(IzosdwanCommonConstants.MINCONTRACTTERM);
							quoteIzoSdwanContractAttributeValues.setQuote(quoteOpt.get());
							attrVals.add(quoteIzoSdwanContractAttributeValues);
							attrVals.add(quoteIzoSdwanAttributeValues);
							quoteIzoSdwanAttributeValuesRepository.saveAll(attrVals);
						}
					}

				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private void persistIzosdwanSites(List<SiServiceDetailBean> siServiceDetailBeans, ProductSolution productSolution,
			String vendorName, List<IzoSdwanSiteDetails> siteDet, List<IzoSdwanCpeDetails> cpeDetails,
			Map<String, ProductAttributeMaster> productAttributeMasterMap, MstProductComponent mstProductComponent) {
		if (siServiceDetailBeans != null && !siServiceDetailBeans.isEmpty()) {
			List<Integer> remainingContractPeriod = new ArrayList<>();
			Date date = new Date();
			siServiceDetailBeans.stream().forEach(siServiceDetailBean -> {
				if (siServiceDetailBean.getContractEndDate() != null) {
					Double diffInMonths = ((siServiceDetailBean.getContractEndDate().getTime() - date.getTime())
							* 3.8026486208333E-10);
					Integer remMonth = diffInMonths.intValue();
					if (diffInMonths > diffInMonths.intValue()) {
						remMonth = remMonth + 1;
					}
					remainingContractPeriod.add(remMonth);
				}
				QuoteIzosdwanSite quoteIzosdwanSite = new QuoteIzosdwanSite();
				Set<QuoteIzosdwanSiteSla> quoteIzoSdwanSiteSla = new HashSet<>();
				CheckPortBandwidth(siServiceDetailBean, quoteIzosdwanSite);
				String profilename = productSolution.getMstProductOffering().getProductName();
				ProductOfferingsBean productOfferingsBean;
				try {
					productOfferingsBean = Utils.convertJsonToObject(productSolution.getProductProfileData(),
							ProductOfferingsBean.class);
					if (productOfferingsBean != null) {
						List<AddonsBean> addons = productOfferingsBean.getAddons();
						String cpeName = cpeSuggestionLogic(siServiceDetailBean, vendorName, quoteIzosdwanSite,
								profilename, addons, siteDet, cpeDetails, siServiceDetailBeans);
						IzoSdwanCpeDetails izoSdwanCpeDetails = cpeDetails.stream()
								.filter(detail -> detail.getCpeName().equalsIgnoreCase(cpeName)).findFirst()
								.orElse(null);
						LOGGER.info("Suggested CPE name {}", cpeName);
						if (siServiceDetailBean.getSiCpeBeans() != null
								&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
							quoteIzosdwanSite.setIsShared(siServiceDetailBean.getSiCpeBeans().get(0).getIsShared());
							siServiceDetailBean.getSiCpeBeans().get(0).setSuggestedModel(cpeName);
							if (izoSdwanCpeDetails != null) {
								siServiceDetailBean.getSiCpeBeans().get(0).setL2Ports(izoSdwanCpeDetails.getL2Ports());
								siServiceDetailBean.getSiCpeBeans().get(0).setL3Ports(izoSdwanCpeDetails.getL3Ports());
								if (izoSdwanCpeDetails.getBandwidth() != null
										&& izoSdwanCpeDetails.getBandwidthRate() != null) {
									siServiceDetailBean.getSiCpeBeans().get(0)
											.setCpeMaxBw(izoSdwanCpeDetails.getBandwidth().toString() + " "
													+ izoSdwanCpeDetails.getBandwidthRate());
								}
							}
						} else {
							LOGGER.info("Asset for CPE is not there from inventory hence adding it Manually here!!");
							SiCpeBean scBean = new SiCpeBean();
							scBean.setSuggestedModel(cpeName);
							scBean.setL2Ports(izoSdwanCpeDetails.getL2Ports());
							scBean.setL3Ports(izoSdwanCpeDetails.getL3Ports());
							if (izoSdwanCpeDetails.getBandwidth() != null
									&& izoSdwanCpeDetails.getBandwidthRate() != null) {
								scBean.setCpeMaxBw(izoSdwanCpeDetails.getBandwidth().toString() + " "
										+ izoSdwanCpeDetails.getBandwidthRate());
							}
							List<SiCpeBean> scCpeBeans = new ArrayList<>();
							scCpeBeans.add(scBean);
							siServiceDetailBean.setSiCpeBeans(scCpeBeans);
						}
						quoteIzosdwanSite.setNewCpe(cpeName);
						persistInterface(siServiceDetailBean, cpeName);

					}
				} catch (TclCommonException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}
				quoteIzosdwanSite.setArc(siServiceDetailBean.getArc());

				quoteIzosdwanSite.setArc(0D);

				User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
				if (user != null) {
					quoteIzosdwanSite.setCreatedBy(user.getId());
					quoteIzosdwanSite.setUpdatedBy(user.getId());
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 130); // Adding 130 days
				quoteIzosdwanSite.setCreatedTime(new Date());
				quoteIzosdwanSite.setUpdatedTime(new Date());
				quoteIzosdwanSite.setEffectiveDate(cal.getTime());
				if (siServiceDetailBean.getAddressId() != null) {
					quoteIzosdwanSite.setErfLocSitebLocationId(Integer.parseInt(siServiceDetailBean.getAddressId()));
				}
				quoteIzosdwanSite.setErfServiceInventoryTpsServiceId(siServiceDetailBean.getServiceId());
				quoteIzosdwanSite.setFeasibility(CommonConstants.BACTIVE);
				quoteIzosdwanSite.setFpStatus(IzosdwanCommonConstants.FP);
				quoteIzosdwanSite.setIsIzo(CommonConstants.BDEACTIVATE);
				quoteIzosdwanSite.setIsTaskTriggered(CommonConstants.INACTIVE);
				quoteIzosdwanSite.setMfTaskTriggered(CommonConstants.INACTIVE);
				quoteIzosdwanSite.setIsTaxExempted(CommonConstants.BDEACTIVATE);
				String offering = siServiceDetailBean.getOffering().toLowerCase();
				if (IzosdwanCommonConstants.OFFERING_MAP.containsKey(offering)) {
					quoteIzosdwanSite.setIzosdwanSiteOffering(IzosdwanCommonConstants.OFFERING_MAP.get(offering));
				} else if (offering.contains(IzosdwanCommonConstants.UNMANAGED.toLowerCase())) {
					Boolean isIasUnmanaged = IzosdwanCommonConstants.OFFERING_IAS.stream()
							.anyMatch(s -> offering.contains(s));
					if (isIasUnmanaged)
						quoteIzosdwanSite.setIzosdwanSiteOffering(
								IzosdwanCommonConstants.MANAGED.concat(" ") + siServiceDetailBean.getOffering());
				} else {
					quoteIzosdwanSite.setIzosdwanSiteOffering(siServiceDetailBean.getOffering());
				}
				quoteIzosdwanSite.setIzosdwanSiteType(siServiceDetailBean.getSiteType());
				quoteIzosdwanSite.setMrc(0D);
				if (siServiceDetailBean.getNewLastMileBandwidth() != null) {
					quoteIzosdwanSite.setNewLastmileBandwidth(siServiceDetailBean.getNewLastMileBandwidth());
				} else {
					quoteIzosdwanSite.setNewLastmileBandwidth(siServiceDetailBean.getLastMileBandwidth());
				}
				quoteIzosdwanSite.setNrc(0D);
				quoteIzosdwanSite.setOldLastmileBandwidth(siServiceDetailBean.getLastMileBandwidth());
				quoteIzosdwanSite.setProductSolution(productSolution);
				// quoteIzosdwanSite.setRequestorDate(new Date());
				quoteIzosdwanSite.setSiteCode(Utils.generateUid());
				quoteIzosdwanSite.setStatus(CommonConstants.BACTIVE);
				quoteIzosdwanSite.setIzosdwanSiteProduct(siServiceDetailBean.getProduct());
				quoteIzosdwanSite.setLatLong(siServiceDetailBean.getLatLong());
				quoteIzosdwanSite.setIsFeasiblityCheckRequired(CommonConstants.INACTIVE);
				quoteIzosdwanSite.setIsPricingCheckRequired(CommonConstants.ACTIVE);
				quoteIzosdwanSite.setManagementType(siServiceDetailBean.getManagmentType());
				quoteIzosdwanSite.setSiParentOrderId(siServiceDetailBean.getSiOrderId());
				quoteIzosdwanSite.setSiServiceDetailsId(siServiceDetailBean.getSiServiceDetailId());
				quoteIzosdwanSite.setPrimaryServiceId(siServiceDetailBean.getPrimaryServiceId());
				// set primaryServiceId if not present for dual/shared cpe case
				if (quoteIzosdwanSite.getIzosdwanSiteType().contains(IzosdwanCommonConstants.DCPE)
						|| quoteIzosdwanSite.getIsShared().equalsIgnoreCase("Y")) {
					if (quoteIzosdwanSite.getPrimaryServiceId() == null
							|| quoteIzosdwanSite.getPrimaryServiceId().isEmpty()) {
						if (quoteIzosdwanSite.getErfLocSitebLocationId() != null) {
							List<QuoteIzosdwanSite> site = quoteIzosdwanSiteRepository
									.findByProductSolutionAndIzosdwanSiteTypeAndErfLocSitebLocationId(productSolution,
											quoteIzosdwanSite.getIzosdwanSiteType(),
											quoteIzosdwanSite.getErfLocSitebLocationId());
							if (site != null && !site.isEmpty() && site.get(0).getPrimaryServiceId() != null
									&& !site.get(0).getPrimaryServiceId().isEmpty()) {
								quoteIzosdwanSite.setPrimaryServiceId(site.get(0).getPrimaryServiceId());
							} else {
								quoteIzosdwanSite
										.setPrimaryServiceId(quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
							}
						}
					}
				}
				quoteIzosdwanSite.setServiceSiteCountry(siServiceDetailBean.getCountry());
				try {
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(siServiceDetailBean.getAddressId()));
					if (StringUtils.isNotBlank(locationResponse)) {
						AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
								AddressDetail.class);
						addressDetail = validateAddressDetail(addressDetail);
						quoteIzosdwanSite.setServiceSiteAddress(
								StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
										+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getLocality())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCountry())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getPincode()));

					}
				} catch (TclCommonException e1) {
					LOGGER.warn("Error in getting address details");
				}
				QuoteIzosdwanSiteSla sla = new QuoteIzosdwanSiteSla();

				/*
				 * if (quoteIzosdwanSite.getIsAutoBwUpgraded() != null &&
				 * quoteIzosdwanSite.getIsAutoBwUpgraded().equals(CommonConstants.BACTIVE)) {
				 * quoteIzosdwanSite.setIsFeasiblityCheckRequired(CommonConstants.ACTIVE); }
				 */
				if (siServiceDetailBean.getPriSec() != null
						&& siServiceDetailBean.getPriSec().equalsIgnoreCase("Single")) {
					quoteIzosdwanSite.setPriSec("Primary");
				} else {
					quoteIzosdwanSite.setPriSec(siServiceDetailBean.getPriSec());
				}

				quoteIzosdwanSite = quoteIzosdwanSiteRepository.save(quoteIzosdwanSite);
				try {
					LOGGER.info("Getting DETAILS FROM SERVICE INVENTORY{}");
					String TierType = getSlaTierValue(siServiceDetailBean.getCity(), siServiceDetailBean.getCountry(),
							siServiceDetailBean.getProduct(), siServiceDetailBean.getSiteType(),
							siServiceDetailBean.getServiceVariant(), vendorName);
					if (TierType == null) {
						LOGGER.info("No Tier type for that service id");
					} else {
						sla.setSlaValue(TierType);
						sla.setQuoteIzosdwanSite(quoteIzosdwanSite);
						sla.setSlaMaster(
								slaMasterRepository.findBySlaName(IzosdwanCommonConstants.SERVICE_AVAILABILITY));
						quoteIzoSdwanSiteSla.add(sla);
					}
					quoteIzosdwanSite.setQuoteIzosdwanSiteSlas(quoteIzoSdwanSiteSla);
					quoteIzoSdwanSlaRepository.save(sla);

				} catch (TclCommonException e) {
					LOGGER.info("Error in getting sla details");
					e.printStackTrace();
				}
				quoteIzosdwanSite.getQuoteIzosdwanSiteSlas().stream().forEach(s -> System.out.println(s.getSlaValue()));
				LOGGER.info("THE DETAILS IN QUOTE IZO SDWAN SITE ARE {}", quoteIzosdwanSite.getQuoteIzosdwanSiteSlas());
				LOGGER.info("Persisted in IZOSDWAN site!! with id {} and code {}", quoteIzosdwanSite.getId(),
						quoteIzosdwanSite.getSiteCode());
				updateSiteProperitiesForTheSite(quoteIzosdwanSite, siServiceDetailBean,
						productSolution.getMstProductOffering().getMstProductFamily(), productAttributeMasterMap,
						mstProductComponent);
				createQuoteProductComponentIfNotPresent(quoteIzosdwanSite.getId(), quoteIzosdwanSite.getPriSec(),
						FPConstants.CPE.toString(), user, IzosdwanCommonConstants.IZOSDWAN_SITES);
				createQuoteProductComponentIfNotPresent(quoteIzosdwanSite.getId(), quoteIzosdwanSite.getPriSec(),
						FPConstants.LICENSE_COST.toString(), user, IzosdwanCommonConstants.IZOSDWAN_SITES);
				if (quoteIzosdwanSite.getIzosdwanSiteProduct().equalsIgnoreCase(CommonConstants.IAS)) {
					createQuoteProductComponentIfNotPresent(quoteIzosdwanSite.getId(), quoteIzosdwanSite.getPriSec(),
							FPConstants.INTERNET_PORT.toString(), user, IzosdwanCommonConstants.IZOSDWAN_SITES);
				} else {
					createQuoteProductComponentIfNotPresent(quoteIzosdwanSite.getId(), quoteIzosdwanSite.getPriSec(),
							FPConstants.VPN_PORT.toString(), user, IzosdwanCommonConstants.IZOSDWAN_SITES);
				}
				createQuoteProductComponentIfNotPresent(quoteIzosdwanSite.getId(), quoteIzosdwanSite.getPriSec(),
						FPConstants.LAST_MILE.toString(), user, IzosdwanCommonConstants.IZOSDWAN_SITES);
				LOGGER.info("site prop end!");
				// persist in quote_ill_site_to_service
				QuoteIllSiteToService siteToService = new QuoteIllSiteToService();
				siteToService.setAllowAmendment("NA");
				siteToService.setErfServiceInventoryParentOrderId(siServiceDetailBean.getSiOrderId());
				siteToService.setErfServiceInventoryServiceDetailId(siServiceDetailBean.getSiServiceDetailId());
				siteToService.setErfServiceInventoryTpsServiceId(siServiceDetailBean.getServiceId());
				siteToService.setQuoteToLe(productSolution.getQuoteToLeProductFamily().getQuoteToLe());
				siteToService.setTpsSfdcParentOptyId(siServiceDetailBean.getParentOpportunityId());
				if (siServiceDetailBean.getPriSec() != null
						&& (PDFConstants.PRIMARY.equalsIgnoreCase(siServiceDetailBean.getPriSec())
								|| MACDConstants.SINGLE.equalsIgnoreCase(siServiceDetailBean.getPriSec())))
					siteToService.setType(PDFConstants.PRIMARY);
				else
					siteToService.setType(PDFConstants.SECONDARY);
				siteToService.setQuoteIzosdwanSite(quoteIzosdwanSite);
				LOGGER.info("site save service");
				quoteIllSiteToServiceRepository.save(siteToService);
			});
			try {
				Integer minContractTerm;
				Integer minimumContract = remainingContractPeriod.stream().max(Integer::compare).get();
				if (minimumContract <= 12) {
					minContractTerm = 12;
				} else if (minimumContract > 12 && minimumContract <= 36) {
					minContractTerm = 36;
				} else {
					minContractTerm = 60;
				}
				List<QuoteIzoSdwanAttributeValues> attributeValues = new ArrayList<>();
				attributeValues = quoteIzoSdwanAttributeValuesRepository
						.findByQuote(productSolution.getQuoteToLeProductFamily().getQuoteToLe().getQuote());
				Optional<QuoteIzoSdwanAttributeValues> attrValu = attributeValues.stream()
						.filter(s -> s.getDisplayValue().equals(IzosdwanCommonConstants.MINCONTRACTTERM)).findAny();
				if (!(attrValu.isPresent())) {
					QuoteIzoSdwanAttributeValues val = new QuoteIzoSdwanAttributeValues();
					val.setDisplayValue(IzosdwanCommonConstants.MINCONTRACTTERM);
					val.setQuote(productSolution.getQuoteToLeProductFamily().getQuoteToLe().getQuote());
					attributeValues.add(val);
				}
				attributeValues.stream()
						.filter(val -> val.getDisplayValue().contains(IzosdwanCommonConstants.MINCONTRACTTERM))
						.findFirst().get().setAttributeValue(minContractTerm.toString() + " months");
				quoteIzoSdwanAttributeValuesRepository.saveAll(attributeValues);
				QuoteToLe quoteToLe = quoteToLeRepository.findByQuoteAndId(
						productSolution.getQuoteToLeProductFamily().getQuoteToLe().getQuote(),
						productSolution.getQuoteToLeProductFamily().getQuoteToLe().getId());
				if (quoteToLe != null && quoteToLe.getId() != null) {
					quoteToLe.setTermInMonths(minContractTerm.toString() + " months");
					quoteToLeRepository.save(quoteToLe);
				}
			} catch (Exception e) {
			}
		} else {

		}

	}

	private String getSlaTierValue(String city, String country, String product, String siteType, String serviceVariant,
			String vendorName) throws TclCommonException {
		String tierValue = "";
		IzoSdwanSlaRequest slaDetails = new IzoSdwanSlaRequest();
		slaDetails.setCityName(city);
		slaDetails.setCountryName(country);
		slaDetails.setProductName(product);
		slaDetails.setSiteTypeName(siteType);
		slaDetails.setSltVarient(serviceVariant);
		slaDetails.setVendorName(vendorName);
		String response = (String) mqUtils.sendAndReceive(slaDetailsQueue, Utils.convertObjectToJson(slaDetails));
		tierValue = GscUtils.fromJson(response, String.class);
		return tierValue;
	}

	private String cpeSuggestionLogic(SiServiceDetailBean siServiceDetailBean, String vendorName,
			QuoteIzosdwanSite quoteIzosdwanSite, String profileName, List<AddonsBean> addons,
			List<IzoSdwanSiteDetails> siteDet, List<IzoSdwanCpeDetails> allCpeDetails,
			List<SiServiceDetailBean> siServiceDetailBeans) throws TclCommonException, IllegalArgumentException {
		String cpeName = null;
		LOGGER.info("interface type:{} and site type name {}", siServiceDetailBean.getInterfaceType(),
				siServiceDetailBean.getSiteType());
		Optional<IzoSdwanSiteDetails> siteDetails = siteDet.stream()
				.filter(site -> site.getSiteTypeName().equals(siServiceDetailBean.getSiteType())).findFirst();
		IzoSdwanSiteDetails site = siteDetails.get();
		/*
		 * if(Integer.parseInt(siServiceDetailBean.getBandwidth())<2) {
		 * siServiceDetailBean.setBandwidth("2"); }
		 */
		String addon = null;
		if (addons != null && !addons.isEmpty()) {
			addon = addons.get(addons.size() - 1).getCode();
		}
		List<IzoSdwanCpeDetails> cpeDetails = new ArrayList<>();
		if (vendorName != null) {
			cpeDetails = allCpeDetails.stream().filter(cpe -> vendorName.equalsIgnoreCase(cpe.getVendor()))
					.collect(Collectors.toList());
			LOGGER.info("After filtering Vendor Name !! ");
		}
		if (addon != null) {
			final String addonFinal = addon;
			if (addonFinal != null) {
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> addonFinal.equalsIgnoreCase(cpe.getAddon()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> addonFinal.equalsIgnoreCase(cpe.getAddon()))
							.collect(Collectors.toList());
				}
				LOGGER.info("After filtering Addon Name !! ");
			}
		} else {
			if (cpeDetails != null && !cpeDetails.isEmpty()) {
				cpeDetails = cpeDetails.stream().filter(cpe -> StringUtils.isBlank(cpe.getAddon()))
						.collect(Collectors.toList());
			}
		}
		LOGGER.info("After filtering Addon Name  CPE details are!! {}",
				cpeDetails != null ? Utils.convertObjectToJson(cpeDetails) : "Empty data");
		if (profileName != null) {
			LOGGER.info("Profile name {}", profileName);
			if (profileName.equals("Select Secure Premium")) {
				String name = "SECURE_PREMIUM";
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> name.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> name.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				}
			} else {
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> profileName.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> profileName.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				}
			}

			LOGGER.info("After filtering Profile Name !! ");
		}
		LOGGER.info("After filtering Profile Name  CPE details are!! {}",
				cpeDetails != null ? Utils.convertObjectToJson(cpeDetails) : "Empty data");
		if (cpeDetails != null && !cpeDetails.isEmpty()) {

			if (siServiceDetailBean.getSiCpeBeans() != null && !siServiceDetailBean.getSiCpeBeans().isEmpty()
					&& siServiceDetailBean.getSiCpeBeans().get(0).getIsShared() != null
					&& siServiceDetailBean.getSiCpeBeans().get(0).getIsShared().equals("Y")) {
				List<SiServiceDetailBean> sharedServiceDetails = siServiceDetailBeans.stream()
						.filter(detail -> detail.getSiteType().equals(site.getSiteTypeName())
								&& detail.getPrimaryServiceId().equals(siServiceDetailBean.getPrimaryServiceId()))
						.collect(Collectors.toList());
				Integer bandwidth = 0;
				for (SiServiceDetailBean sumBand : sharedServiceDetails) {
					if (sumBand.getNewLastMileBandwidth() == null) {
						CheckPortBandwidth(sumBand, quoteIzosdwanSite);
					}
					LOGGER.info("GET LAST MILE BANDWITH{}", sumBand.getNewLastMileBandwidth());
					bandwidth += Math.round(Float.parseFloat(sumBand.getNewLastMileBandwidth()));
				}
				cpeName = getUniqueCpes(site, cpeDetails, bandwidth);
				// siServiceDetailBean.getSiCpeBeans().get(0).setSuggestedModel(cpeName);
			} else {
				if (siServiceDetailBean.getNewLastMileBandwidth() == null) {
					LOGGER.info("THE SITE BANDWITH IS {}", siServiceDetailBean.getLastMileBandwidth());
					Integer bandDet = Math.round(Float.parseFloat(siServiceDetailBean.getLastMileBandwidth()));
					cpeName = getUniqueCpes(site, cpeDetails, bandDet);
					// siServiceDetailBean.getSiCpeBeans().get(0).setSuggestedModel(cpeName);
				} else {
					LOGGER.info("THE SITE BANDWITH IS {}", siServiceDetailBean.getNewLastMileBandwidth());
					Integer bandDet = Math.round(Float.parseFloat(siServiceDetailBean.getNewLastMileBandwidth()));
					cpeName = getUniqueCpes(site, cpeDetails, bandDet);
					// siServiceDetailBean.getSiCpeBeans().get(0).setSuggestedModel(cpeName);
				}

			}

		}
		LOGGER.info("Suggested CPE is {}", cpeName);
		return cpeName;
	}

	private String getUniqueCpes(IzoSdwanSiteDetails siteDet, List<IzoSdwanCpeDetails> cpeDet, Integer bandwidth) {
		// Integer bandwidth=Integer.parseInt(band);
		List<IzoSdwanCpeDetails> cpeDetails = cpeDet.stream().filter(cpe -> cpe.getBandwidth() >= bandwidth)
				.collect(Collectors.toList());
		List<IzoSdwanCpeDetails> suggestedCpes = new ArrayList<>();
		for (IzoSdwanCpeDetails cpeData : cpeDetails) {
			if (cpeData.getL2Ports() != null && cpeData.getL2Ports() == 0) {
				Integer sum = 0;
				LOGGER.info("THE CHANGED THING IS{}", cpeData.getProfile());
				if (siteDet.getNoOfL2Ports() != null) {
					sum += siteDet.getNoOfL2Ports();
				}
				if (siteDet.getNoOfL3Ports() != null) {
					sum += siteDet.getNoOfL3Ports();
				}
				if (cpeData.getL3Ports() >= sum) {
					suggestedCpes.add(cpeData);
				}

			}
			LOGGER.info("THE CHANGED THING IS{}", cpeData.getProfile());
			if (cpeData.getL2Ports() != null && cpeData.getL2Ports() != null && siteDet.getNoOfL2Ports() != null
					&& siteDet.getNoOfL3Ports() != null && (cpeData.getL2Ports() >= siteDet.getNoOfL2Ports())
					&& (cpeData.getL3Ports() >= siteDet.getNoOfL3Ports())) {
				suggestedCpes.add(cpeData);
			}
			if (cpeData.getL2Ports() != null && siteDet.getNoOfL2Ports() != null
					&& (cpeData.getL2Ports() < siteDet.getNoOfL2Ports())) {
				Integer sum = 0;
				if (siteDet.getNoOfL2Ports() != null) {
					sum += siteDet.getNoOfL2Ports();
				}
				if (siteDet.getNoOfL3Ports() != null) {
					sum += siteDet.getNoOfL3Ports();
				}
				if (cpeData.getL3Ports() >= sum) {
					suggestedCpes.add(cpeData);
				}
			}
		}
		String suggestedCpe = suggestCpe(suggestedCpes);
		return suggestedCpe;
	}

	private void persistInterface(SiServiceDetailBean siServiceDetailBean, String cpeName) {
		List<IzoSdwanCpeBomInterface> cpeBomInterfaces = new ArrayList<>();
		try {
			cpeBomInterfaces = getInterfaceDetails();
			if (cpeName != null) {
				if (cpeBomInterfaces != null && !cpeBomInterfaces.isEmpty()) {
					cpeBomInterfaces = cpeBomInterfaces.stream()
							.filter(cpe -> cpeName.equalsIgnoreCase(cpe.getBomNameCd())).collect(Collectors.toList());
				}
			}
			if (!cpeBomInterfaces.isEmpty() && cpeBomInterfaces != null) {
				siServiceDetailBean.getSiCpeBeans().get(0)
						.setCpeModelEndOfSale(cpeBomInterfaces.get(0).getCpeModelEndOfSale());
				siServiceDetailBean.getSiCpeBeans().get(0)
						.setCpeModelEndOfLife(cpeBomInterfaces.get(0).getCpeModelEndOfLife());
				for (IzoSdwanCpeBomInterface cpeBomInterface : cpeBomInterfaces) {
					if ((cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP"))
							|| (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP+"))) {
						if (cpeBomInterface.getInterfaceType().equals(siServiceDetailBean.getInterfaceType())) {
							if (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP")
									&& siServiceDetailBean.getSiCpeBeans() != null
									&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
								siServiceDetailBean.getSiCpeBeans().get(0)
										.setSfp(cpeBomInterface.getPhysicalResourceCd());
								siServiceDetailBean.getSiCpeBeans().get(0).setSfpDesc(cpeBomInterface.getDescription());
							}
							if (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP+")
									&& siServiceDetailBean.getSiCpeBeans() != null
									&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
								siServiceDetailBean.getSiCpeBeans().get(0)
										.setSfpplus(cpeBomInterface.getPhysicalResourceCd());
								siServiceDetailBean.getSiCpeBeans().get(0)
										.setSfpPlusDesc(cpeBomInterface.getDescription());

							}
						}

					} else {
						if (cpeBomInterface.getProductCategory().equalsIgnoreCase("CPE")
								&& siServiceDetailBean.getSiCpeBeans() != null
								&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
							siServiceDetailBean.getSiCpeBeans().get(0).setCpe(cpeBomInterface.getPhysicalResourceCd());
							siServiceDetailBean.getSiCpeBeans().get(0).setCpeDesc(cpeBomInterface.getDescription());

						}
						if (cpeBomInterface.getProductCategory().equalsIgnoreCase("NMC")
								&& siServiceDetailBean.getSiCpeBeans() != null
								&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
							siServiceDetailBean.getSiCpeBeans().get(0).setNmc(cpeBomInterface.getPhysicalResourceCd());
							siServiceDetailBean.getSiCpeBeans().get(0).setNmcDesc(cpeBomInterface.getDescription());

						}
						if (cpeBomInterface.getProductCategory().equalsIgnoreCase("Rackmount")
								&& siServiceDetailBean.getSiCpeBeans() != null
								&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
							siServiceDetailBean.getSiCpeBeans().get(0)
									.setRackmount(cpeBomInterface.getPhysicalResourceCd());
							if (cpeBomInterface.getPhysicalResourceCd().equals("RackMount Kit/Shelf")) {
								siServiceDetailBean.getSiCpeBeans().get(0)
										.setRackmountDesc(cpeBomInterface.getDescription());
							} else {
								siServiceDetailBean.getSiCpeBeans().get(0)
										.setRackmountDesc(cpeBomInterface.getDescription());
							}

						}
						if (cpeBomInterface.getProductCategory().contains("Power")
								&& siServiceDetailBean.getSiCpeBeans() != null
								&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
							siServiceDetailBean.getSiCpeBeans().get(0)
									.setPowerCord(cpeBomInterface.getPhysicalResourceCd());
							siServiceDetailBean.getSiCpeBeans().get(0)
									.setPowerCordDesc(cpeBomInterface.getDescription());

						}
					}
				}
			}
		} catch (TclCommonException | IllegalArgumentException e) {

		}
	}

	public List<IzoSdwanCpeBomInterface> getInterfaceDetails() throws TclCommonException, IllegalArgumentException {
		List<IzoSdwanCpeBomInterface> InterfaceDetails = new ArrayList<>();
		String cpeInterfaceDet = (String) mqUtils.sendAndReceive(cpebomInterfaceDetails,
				Utils.convertObjectToJson("Hello"));
		InterfaceDetails = GscUtils.fromJson(cpeInterfaceDet, new TypeReference<List<IzoSdwanCpeBomInterface>>() {
		});
		return InterfaceDetails;
	}

	private String getUniqueCpes(IzoSdwanSiteDetails siteDet, List<IzoSdwanCpeDetails> cpeDet, String band) {
		Integer bandwidth = Integer.parseInt(band);
		List<IzoSdwanCpeDetails> cpeDetails = cpeDet.stream().filter(cpe -> cpe.getBandwidth() >= bandwidth)
				.collect(Collectors.toList());
		List<IzoSdwanCpeDetails> suggestedCpes = new ArrayList<>();
		for (IzoSdwanCpeDetails cpeData : cpeDetails) {
			if (cpeData.getL2Ports() != null && cpeData.getL2Ports() == 0) {
				Integer sum = 0;
				if (siteDet.getNoOfL2Ports() != null) {
					sum += siteDet.getNoOfL2Ports();
				}
				if (siteDet.getNoOfL3Ports() != null) {
					sum += siteDet.getNoOfL3Ports();
				}
				if (cpeData.getL3Ports() >= sum) {
					suggestedCpes.add(cpeData);
				}
			}
			if (cpeData.getL2Ports() != null && cpeData.getL2Ports() != null && siteDet.getNoOfL2Ports() != null
					&& siteDet.getNoOfL3Ports() != null && (cpeData.getL2Ports() >= siteDet.getNoOfL2Ports())
					&& (cpeData.getL3Ports() >= siteDet.getNoOfL3Ports())) {
				suggestedCpes.add(cpeData);
			}
			if (cpeData.getL2Ports() != null && siteDet.getNoOfL2Ports() != null
					&& (cpeData.getL2Ports() < siteDet.getNoOfL2Ports())) {
				Integer sum = 0;
				if (siteDet.getNoOfL2Ports() != null) {
					sum += siteDet.getNoOfL2Ports();
				}
				if (siteDet.getNoOfL3Ports() != null) {
					sum += siteDet.getNoOfL3Ports();
				}
				if (cpeData.getL3Ports() >= sum) {
					suggestedCpes.add(cpeData);
				}
			}
		}
		return suggestCpe(suggestedCpes);
	}

	private String getUniqueCpes(IzoSdwanSiteDetails siteDet, List<IzoSdwanCpeDetails> cpeDet, Integer bandwidth,
			List<SiServiceDetailBean> siServiceDetailBeans) {
		// Integer bandwidth=Integer.parseInt(band);
		String suggestedCpe = null;
		List<IzoSdwanCpeDetails> cpeDetails = cpeDet.stream().filter(cpe -> cpe.getBandwidth() >= bandwidth)
				.collect(Collectors.toList());
		List<IzoSdwanCpeDetails> suggestedCpes = new ArrayList<>();
		for (IzoSdwanCpeDetails cpeData : cpeDetails) {
			if (cpeData.getL2Ports() != null && cpeData.getL2Ports() == 0) {
				Integer sum = 0;
				LOGGER.info("THE CHANGED THING IS{}", cpeData.getProfile());
				if (siteDet.getNoOfL2Ports() != null) {
					sum += siteDet.getNoOfL2Ports();
				}
				if (siteDet.getNoOfL3Ports() != null) {
					sum += siteDet.getNoOfL3Ports();
				}
				if (cpeData.getL3Ports() >= sum) {
					suggestedCpes.add(cpeData);
				}

			}
			LOGGER.info("THE CHANGED THING IS{}", cpeData.getProfile());
			if (cpeData.getL2Ports() != null && cpeData.getL2Ports() != null && siteDet.getNoOfL2Ports() != null
					&& siteDet.getNoOfL3Ports() != null && (cpeData.getL2Ports() >= siteDet.getNoOfL2Ports())
					&& (cpeData.getL3Ports() >= siteDet.getNoOfL3Ports())) {
				suggestedCpes.add(cpeData);
			}
			if (cpeData.getL2Ports() != null && siteDet.getNoOfL2Ports() != null
					&& (cpeData.getL2Ports() < siteDet.getNoOfL2Ports())) {
				Integer sum = 0;
				if (siteDet.getNoOfL2Ports() != null) {
					sum += siteDet.getNoOfL2Ports();
				}
				if (siteDet.getNoOfL3Ports() != null) {
					sum += siteDet.getNoOfL3Ports();
				}
				if (cpeData.getL3Ports() >= sum) {
					suggestedCpes.add(cpeData);
				}
			}
		}
		suggestedCpe = suggestCpe(suggestedCpes);
		return suggestedCpe;
	}

	private String suggestCpe(List<IzoSdwanCpeDetails> cpeDet) {
		IzoSdwanCpeDetails cpeModel = new IzoSdwanCpeDetails();
		for (IzoSdwanCpeDetails cpe : cpeDet) {
			if (cpeModel.getCpePriority() == null) {
				cpeModel = cpe;
			} else {
				if (cpeModel.getCpePriority() != null && cpe.getCpePriority() != null
						&& (cpeModel.getCpePriority() > cpe.getCpePriority())) {
					cpeModel = cpe;
				}
			}
		}
		return cpeModel.getCpeName();
	}

	private void CheckPortBandwidth(SiServiceDetailBean siServiceDetailBean, QuoteIzosdwanSite quoteIzosdwanSite) {

		String autoUpgardedbandwidth = "2";
		LOGGER.info("Port bw {}", siServiceDetailBean.getBandwidth());
		double portBandwidth = Double.valueOf(siServiceDetailBean.getBandwidth());
		LOGGER.info("Lastmile bw {}", siServiceDetailBean.getLastMileBandwidth());
		double portlastMileBandwidth = Double.valueOf(siServiceDetailBean.getLastMileBandwidth());
		double convertedBandwidth = 0.0;
		double convertedLastMileBandwidth = 0.0;
		if (siServiceDetailBean.getBandwidthUnit().equals("Kbps")) {
			convertedBandwidth = (portBandwidth / 1024);
			quoteIzosdwanSite.setOldPortBandwidth(removeDecimalBandwidth(String.valueOf(convertedBandwidth)));
		} else if (siServiceDetailBean.getBandwidthUnit().equals("Gbps")) {
			convertedBandwidth = (portBandwidth * 1000);
			quoteIzosdwanSite.setOldPortBandwidth(removeDecimalBandwidth(String.valueOf(convertedBandwidth)));
		}

		else {
			convertedBandwidth = portBandwidth;
			quoteIzosdwanSite.setOldPortBandwidth(siServiceDetailBean.getBandwidth());

		}

		if (siServiceDetailBean.getLastMileBandwidthUnit().equals("Kbps")) {
			convertedLastMileBandwidth = (portlastMileBandwidth / 1024);
			quoteIzosdwanSite
					.setOldLastmileBandwidth(removeDecimalBandwidth(String.valueOf(convertedLastMileBandwidth)));
		} else if (siServiceDetailBean.getLastMileBandwidthUnit().equals("Gbps")) {
			convertedLastMileBandwidth = (portlastMileBandwidth * 1000);
			quoteIzosdwanSite
					.setOldLastmileBandwidth(removeDecimalBandwidth(String.valueOf(convertedLastMileBandwidth)));
		} else {
			convertedLastMileBandwidth = portlastMileBandwidth;
			quoteIzosdwanSite.setOldLastmileBandwidth(siServiceDetailBean.getLastMileBandwidth());

		}

		if (convertedBandwidth < 2D) {
			quoteIzosdwanSite.setNewPortBandwidth(autoUpgardedbandwidth);
			quoteIzosdwanSite.setIsAutoBwUpgraded(CommonConstants.BACTIVE);
			if (convertedLastMileBandwidth < 2D) {
				quoteIzosdwanSite.setNewLastmileBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
				siServiceDetailBean.setNewLastMileBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
			}

		} else {
			quoteIzosdwanSite.setNewPortBandwidth(removeDecimalBandwidth(String.valueOf(convertedBandwidth)));
			quoteIzosdwanSite
					.setNewLastmileBandwidth(removeDecimalBandwidth(String.valueOf(convertedLastMileBandwidth)));
			quoteIzosdwanSite.setIsAutoBwUpgraded(CommonConstants.BDEACTIVATE);
			siServiceDetailBean
					.setNewLastMileBandwidth(removeDecimalBandwidth(String.valueOf(convertedLastMileBandwidth)));
		}

	}

	private ProductAttributeMaster getProductAttributeMasterByName(String name,
			Map<String, ProductAttributeMaster> productAttributeMasterMap) {
		if (productAttributeMasterMap.containsKey(name)) {
			return productAttributeMasterMap.get(name);
		} else {

			ProductAttributeMaster productAttributeMaster = productAttributeMasterRepository.findByName(name);
			if (productAttributeMaster != null) {
				productAttributeMasterMap.put(name, productAttributeMaster);
			} else {
				productAttributeMaster = new ProductAttributeMaster();
				productAttributeMaster.setCategory(CommonConstants.NEW);
				User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
				if (user != null) {
					productAttributeMaster.setCreatedBy(Utils.getSource());
				}
				productAttributeMaster.setCreatedTime(new Date());
				productAttributeMaster.setDescription(name);
				productAttributeMaster.setName(name);
				productAttributeMaster.setStatus(CommonConstants.BACTIVE);
				productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
				productAttributeMasterMap.put(name, productAttributeMaster);
			}

			return productAttributeMaster;
		}
	}

	private void updateSiteAttributes(ProductAttributeMaster productAttributeMaster,
			QuoteProductComponent quoteProductComponent, String attributeValue) {
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, productAttributeMaster);
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		if (quoteProductComponentsAttributeValueList != null && !quoteProductComponentsAttributeValueList.isEmpty()) {
			LOGGER.info("Got existing attribute details");
			quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueList.get(0);
		}
		quoteProductComponentsAttributeValue.setAttributeValues(attributeValue);
		quoteProductComponentsAttributeValue.setDisplayValue(attributeValue);
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);

	}

	private MstProductComponent getProductComponentMasterDetail(String componentName) {
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(componentName);
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(Utils.getSource());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setDescription(componentName);
			mstProductComponent.setName(componentName);
			mstProductComponent.setStatus(CommonConstants.BACTIVE);
			mstProductComponent = mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;
	}

	private void updateSiteProperitiesForTheSite(QuoteIzosdwanSite quoteIzosdwanSite,
			SiServiceDetailBean siServiceDetailBean, MstProductFamily mstProductFamily,
			Map<String, ProductAttributeMaster> productAttributeMasterMap, MstProductComponent mstProductComponent) {
		LOGGER.info("site prop!");
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(mstProductComponent);
		quoteProductComponent.setReferenceId(quoteIzosdwanSite.getId());
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_SITES);
		quoteProductComponent.setType(quoteIzosdwanSite.getPriSec());
		quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);

		ProductAttributeMaster productAttributeMaster = getProductAttributeMasterByName(
				IzosdwanCommonConstants.ACCESS_TOPOLOGY, productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getAccessTopology());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.ACCESS_TYPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getAccessType());

		if (StringUtils.isNotEmpty(siServiceDetailBean.getGvpnSiteTopology())) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.GVPN_SITE_TYPE,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent,
					siServiceDetailBean.getGvpnSiteTopology());
		}

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.ADDITIONAL_IP_REQ,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getAdditionalIpsRequired());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.PORT_BANDWIDTH,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, quoteIzosdwanSite.getNewPortBandwidth());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.OLD_PORT_BANDWIDTH,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, quoteIzosdwanSite.getOldPortBandwidth());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.OLD_PORT_BANDWIDTH_UNIT,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getBandwidthUnit());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.OLD_ARC,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				(siServiceDetailBean.getArc() != null ? siServiceDetailBean.getArc().toString()
						: CommonConstants.EMPTY));

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.OLD_NRC,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				(siServiceDetailBean.getNrc() != null ? siServiceDetailBean.getNrc().toString()
						: CommonConstants.EMPTY));

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				quoteIzosdwanSite.getNewLastmileBandwidth());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.OLD_LOCAL_LOOP_BANDWIDTH,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getLastMileBandwidth());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.OLD_LOCAL_LOOP_BANDWIDTH_UNIT,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getLastMileBandwidthUnit());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.BILLING_FREQUENCY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getBillingFrequency());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.BILLING_METHOD,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getBillingMethod());

		if (siServiceDetailBean.getCos() != null) {
			String[] cosValues = siServiceDetailBean.getCos().split(":");
			String cos1 = cosValues[0];
			String cos2 = cosValues[1];
			String cos3 = cosValues[2];
			String cos4 = cosValues[3];
			String cos5 = cosValues[4];
			String cos6 = cosValues[5];
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.COS1,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, cos1);
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.COS2,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, cos2);
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.COS3,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, cos3);
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.COS4,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, cos4);
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.COS5,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, cos5);
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.COS6,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, cos6);
		}

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CURRENCY_ID,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getCurrencyId() != null ? siServiceDetailBean.getCurrencyId().toString() : "");

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CUSTOMER_ID,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getCustomerId() != null ? siServiceDetailBean.getCustomerId().toString() : "");

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CUSTOMER_LE_ID,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getCustomerLeId() != null ? siServiceDetailBean.getCustomerLeId().toString() : "");

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.INTERFACE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getInterfaceType());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.PORT_MODE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getPortMode());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.PRI_SEC,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getPriSec());

//		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.MANAGEMENT_TYPE,
//				productAttributeMasterMap);
//		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
//				(siServiceDetailBean.getManagmentType() == null
//						|| IzosdwanCommonConstants.UNMANAGED.equalsIgnoreCase(siServiceDetailBean.getManagmentType()))
//								? IzosdwanCommonConstants.FULLY_MANAGED
//								: siServiceDetailBean.getManagmentType());
//        updateSiteAttributes(productAttributeMaster, quoteProductComponent,
//                siServiceDetailBean.getManagmentType().equalsIgnoreCase(IzosdwanCommonConstants.UNMANAGED)
//                        ? IzosdwanCommonConstants.FULLY_MANAGED
//                        : siServiceDetailBean.getManagmentType());
//		if(quoteIzosdwanSite.getIzosdwanSiteProduct().contains(IzosdwanCommonConstants.BYON)){
//			updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.PROACTIVE_MANAGED);
//		}
//		else if(siServiceDetailBean.getManagmentType().equalsIgnoreCase(IzosdwanCommonConstants.UNMANAGED)){
//			updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.FULLY_MANAGED);
//		}
//		else{
//			updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getManagmentType());
//		}
		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.LASTMILE_PROVIDER,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getLastmileProvider());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.LASTMILE_PROVIDER,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getLastmileProvider());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.ROUTING_PROTOCOL,
				productAttributeMasterMap);
		if (siServiceDetailBean.getSiteType().contains(IzosdwanCommonConstants.DGVPN))
			updateSiteAttributes(productAttributeMaster, quoteProductComponent,
					IzosdwanCommonConstants.ROUTING_PROTOCOL_VALUE);
		else
			updateSiteAttributes(productAttributeMaster, quoteProductComponent,
					siServiceDetailBean.getRoutingProtocol());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SERVICE_ID,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getServiceId());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SERVICE_TYPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getServiceType());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SERVICE_VARIANT,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getServiceVariant());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SITE_TOPOLOGY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getSiteTopology());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SUPPLIER_ID,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getSupplierId() != null ? siServiceDetailBean.getSupplierId().toString() : "");

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.TERM_IN_MONTHS,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getTermsInMonths() != null ? siServiceDetailBean.getTermsInMonths()
						: "");

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.VPN_TOPOLOGY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getVpnTopology());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.VPN_NAME,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getVpnName());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.IP_ADDRESS_ARRANGEMENT,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getIpAddressArrangementType());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.IPV4_POOL_SIZE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getIpv4AddressPoolSize());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.IPV6_POOL_SIZE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getIpv6AddressPoolSize());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.IZOSDWAN_TOPOLOGY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.FULL_MESH);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CONTRACT_END_DATE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getContractEndDate() != null
						? new SimpleDateFormat("yyyy-MM-dd").format(siServiceDetailBean.getContractEndDate())
						: null);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CONTRACT_START_DATE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				siServiceDetailBean.getContractStartDate() != null
						? new SimpleDateFormat("yyyy-MM-dd").format(siServiceDetailBean.getContractStartDate())
						: null);
		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, (IzosdwanCommonConstants.RENTAL));

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.LICENSE_CONTRACT_TYPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, (IzosdwanCommonConstants.RENTAL));

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POP_SITE_ADDRESS,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getPopSiteAddress());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POP_SITE_CODE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getPopSiteCode());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POP_STATE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getSourceState());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POP_COUNTRY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getSourceCountry());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POP_CITY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, siServiceDetailBean.getSourceCity());

		final QuoteProductComponent quoteProductComponent2 = quoteProductComponent;
		if (siServiceDetailBean != null && siServiceDetailBean.getSiCpeBeans() != null
				&& !siServiceDetailBean.getSiCpeBeans().isEmpty()) {
			siServiceDetailBean.getSiCpeBeans().stream().forEach(siCpeBean -> {
				/*
				 * ProductAttributeMaster productAttributeMaster1 =
				 * getProductAttributeMasterByName( IzosdwanCommonConstants.CPE_BASIC_CHASSIS,
				 * productAttributeMasterMap); updateSiteAttributes(productAttributeMaster1,
				 * quoteProductComponent2, siCpeBean.getSuggestedModel());
				 */
				ProductAttributeMaster productAttributeMaster1 = getProductAttributeMasterByName(
						IzosdwanCommonConstants.CPE_BASIC_CHASSIS, productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getSuggestedModel());

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_SERIAL_NO,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getSerialNo());

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_SCOPE,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2,
						IzosdwanCommonConstants.FULLY_MANAGED);

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, (IzosdwanCommonConstants.RENTAL));

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.LICENSE_CONTRACT_TYPE,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, (IzosdwanCommonConstants.RENTAL));
//						(siCpeBean.getSupportType() != null
//								&& siCpeBean.getSupportType().contains(IzosdwanCommonConstants.RENTAL))
//										? siCpeBean.getSupportType()
//										: IzosdwanCommonConstants.OUTRIGHT_SALE);

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_SHARED_OR_NOT,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2,
						(siCpeBean.getIsShared() != null ? siCpeBean.getIsShared() : CommonConstants.N));
				quoteIzosdwanSite.setOldCpe(siCpeBean.getModel());

				if (siCpeBean.getCpe() != null && !siCpeBean.getCpe().isEmpty()) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_NAME,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getCpe());
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_DESC,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getCpeDesc());
				}

				if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.ROUTER_COST,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, StringUtils.EMPTY);
				}

				if (siCpeBean.getNmc() != null && !siCpeBean.getNmc().isEmpty()) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.NMC,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getNmc());
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.NMC_DESC,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getNmcDesc());
					if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
						productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.NMC_COST,
								productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, StringUtils.EMPTY);
					}
				}

				if (siCpeBean.getRackmount() != null && !siCpeBean.getRackmount().isEmpty()) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.RACKMOUNT,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getRackmount());
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.RACKMOUNT_DESC,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getRackmountDesc());
					if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
						productAttributeMaster1 = getProductAttributeMasterByName(
								IzosdwanCommonConstants.RACKMOUNT_COST, productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, StringUtils.EMPTY);
					}
				}

				if (siCpeBean.getSfp() != null && !siCpeBean.getSfp().isEmpty()) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getSfp());
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_DESC,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getSfpDesc());
					if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
						productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_COST,
								productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, StringUtils.EMPTY);
					}
				}

				if (siCpeBean.getSfpplus() != null && !siCpeBean.getSfpplus().isEmpty()) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_PLUS,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getSfpplus());
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_PLUS_DESC,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getSfpPlusDesc());
					if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
						productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_PLUS_COST,
								productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, StringUtils.EMPTY);
					}
				}

				if (siCpeBean.getPowerCord() != null && !siCpeBean.getPowerCord().isEmpty()) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.POWER_CORD,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getPowerCord());
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.POWERCORD_DESC,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getPowerCordDesc());
					if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
						productAttributeMaster1 = getProductAttributeMasterByName(
								IzosdwanCommonConstants.POWER_CORD_COST, productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, StringUtils.EMPTY);
					}
				}

				if (siCpeBean.getL3Ports() != null) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.L3_PORTS,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2,
							Integer.toString(siCpeBean.getL3Ports()));
				}

				if (siCpeBean.getL2Ports() != null) {
					productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.L2_PORTS,
							productAttributeMasterMap);
					updateSiteAttributes(productAttributeMaster1, quoteProductComponent2,
							Integer.toString(siCpeBean.getL2Ports()));
				}

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_MAX_BW,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getCpeMaxBw());

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_MODEL_END_OF_SALE,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getCpeModelEndOfSale());

				productAttributeMaster1 = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_MODEL_END_OF_LIFE,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster1, quoteProductComponent2, siCpeBean.getCpeModelEndOfLife());
				// quoteIzosdwanSite.setNewCpe(siCpeBean.getModel());

			});
		}
		LOGGER.info("Updated the site properities for site ID {}", quoteIzosdwanSite.getId());
	}

	private void constructSiteTypeSummaryDetails(List<QuoteIzosdwanSite> sdwanSiteDetails,
			NetworkSummaryDetails networkDetails, ProductSolution productSolution) {
		SiteTypeSummary siteTypeSummary = new SiteTypeSummary();
		Map<String, Integer> cpeCounts = new HashMap<>();
		List<SiteTypes> siteTypesList = new ArrayList<>();
		List<String> siteTypes = quoteIzosdwanSiteRepository.getDistinctSiteTypesForSdwan(productSolution.getId());
		if (siteTypes != null && !siteTypes.isEmpty()) {
			siteTypes.stream().forEach(type -> {
				SiteTypes siteTypesBean = new SiteTypes();
				siteTypesBean.setSiteTypeName(type);
				List<QuoteIzosdwanSite> sites = sdwanSiteDetails.stream()
						.filter(site -> site.getIzosdwanSiteType().equalsIgnoreCase(type)).collect(Collectors.toList());
				siteTypesBean.setNoOfSites(sites.size());
				constructCpeTypeDetails(siteTypesBean, productSolution, type);
				constructCpeSummaryDetails(sites, siteTypesBean, cpeCounts);
				if (siteTypesBean.getSiteTypeName().contains("Dual")
						|| IzosdwanCommonConstants.BYONUNDERLAYSHAREDSITES.contains(siteTypesBean.getSiteTypeName())) {
					siteTypesBean.setNoOfSites(sites.size() / 2);
				} else {
					siteTypesBean.setNoOfSites(sites.size());
				}
				siteTypesList.add(siteTypesBean);
				constructBandwidthSummaryDetails(siteTypesBean, type, productSolution.getId());
			});

		}

		siteTypeSummary.setSiteDetails(siteTypesList);
		networkDetails.setSiteTypeSummary(siteTypeSummary);
		constructCpeSummaryDetailsInSummary(cpeCounts, sdwanSiteDetails.size(), networkDetails);

	}

	private void constructCpeTypeDetails(SiteTypes siteTypesBean, ProductSolution productSolution, String type) {
		List<CpeTypes> cpeTypes = new ArrayList<>();
		// Primary GVPN
		String primaryGvpnBwRange = quoteIzosdwanSiteRepository.getBandwidthRange(productSolution.getId(), "Primary",
				type, CommonConstants.GVPN);
		// Primary IAS
		String primaryIasBwRange = quoteIzosdwanSiteRepository.getBandwidthRange(productSolution.getId(), "Primary",
				type, CommonConstants.IAS);
		// Primary BYON
		String primaryByonBwRange = quoteIzosdwanSiteRepository.getBandwidthRange(productSolution.getId(), "Primary",
				type, IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
		// Secondary GVPN
		String secGvpnBwRange = quoteIzosdwanSiteRepository.getBandwidthRange(productSolution.getId(), "SECONDARY",
				type, CommonConstants.GVPN);
		// Secondary IAS
		String secIasBwRange = quoteIzosdwanSiteRepository.getBandwidthRange(productSolution.getId(), "SECONDARY", type,
				CommonConstants.IAS);
		// Secondary BYON
		String secByonBwRange = quoteIzosdwanSiteRepository.getBandwidthRange(productSolution.getId(), "SECONDARY",
				type, IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
		if (primaryGvpnBwRange != null || primaryIasBwRange != null || primaryByonBwRange != null) {
			CpeTypes primary = new CpeTypes();
			primary.setCpeTypeName("CPE(Primary)");
			List<CpeLinks> cpeLinksList = new ArrayList<>();
			if (primaryGvpnBwRange != null) {
				CpeLinks cpe = new CpeLinks();
				cpe.setLinkName(CommonConstants.GVPN);
				cpe.setRange(getBand(primaryGvpnBwRange));
				LOGGER.info("GETTING THE CPE RANGE DETAILS{}", cpe.getRange());
				cpeLinksList.add(cpe);
			}
			if (primaryIasBwRange != null) {
				CpeLinks cpe = new CpeLinks();
				cpe.setLinkName(CommonConstants.IAS);
				cpe.setRange(getBand(primaryIasBwRange));
				LOGGER.info("GETTING THE CPE RANGE DETAILS{}", cpe.getRange());
				cpeLinksList.add(cpe);
			}
			if (primaryByonBwRange != null) {
				CpeLinks cpe = new CpeLinks();
				cpe.setLinkName(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
				cpe.setRange(getBand(primaryByonBwRange));
				LOGGER.info("GETTING THE CPE RANGE DETAILS{}", cpe.getRange());
				cpeLinksList.add(cpe);
			}
			primary.setCpeLinks(cpeLinksList);
			cpeTypes.add(primary);
		}
		if (secGvpnBwRange != null || secIasBwRange != null || secByonBwRange != null) {
			CpeTypes primary = new CpeTypes();
			primary.setCpeTypeName("CPE(Secondary)");
			List<CpeLinks> cpeLinksList = new ArrayList<>();
			if (secGvpnBwRange != null) {
				CpeLinks cpe = new CpeLinks();
				cpe.setLinkName(CommonConstants.GVPN);
				cpe.setRange(getBand(secGvpnBwRange));
				LOGGER.info("GETTING THE CPE RANGE DETAILS{}", cpe.getRange());
				cpeLinksList.add(cpe);
			}
			if (secIasBwRange != null) {
				CpeLinks cpe = new CpeLinks();
				cpe.setLinkName(CommonConstants.IAS);
				cpe.setRange(getBand(secIasBwRange));
				LOGGER.info("GETTING THE CPE RANGE DETAILS{}", cpe.getRange());
				cpeLinksList.add(cpe);
			}
			if (secByonBwRange != null) {
				CpeLinks cpe = new CpeLinks();
				cpe.setLinkName(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
				cpe.setRange(getBand(secByonBwRange));
				LOGGER.info("GETTING THE CPE RANGE DETAILS{}", cpe.getRange());
				cpeLinksList.add(cpe);
			}
			primary.setCpeLinks(cpeLinksList);
			cpeTypes.add(primary);
		}
		siteTypesBean.setCpeTypes(cpeTypes);
	}

	public ConfigurationSummaryBean getConfigurationCpeDetails(Integer quoteId, String siteTypeName)
			throws TclCommonException {
		ConfigurationSummaryBean configurationSummaryBean = new ConfigurationSummaryBean();
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quoteDetails == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		try {
			siteTypeName = siteTypeName.replace("%20", " ");
			ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteDetails.getId());
			List<QuoteIzosdwanSite> izoSdwanSiteDetails = quoteIzosdwanSiteRepository
					.findByProductSolutionAndIzosdwanSiteType(solutions, siteTypeName);
			List<CpeTypes> cpeTypes = constructCpeTypeDet(solutions, siteTypeName);
			List<ConfigurationCpeInfo> cpeConfigInformation = new ArrayList<>();
			if (!izoSdwanSiteDetails.isEmpty() && izoSdwanSiteDetails != null) {
				cpeConfigInformation = getCpeConfigInfo(cpeTypes, solutions.getId(), siteTypeName,
						izoSdwanSiteDetails.get(0).getIsShared());
			}
			configurationSummaryBean.setSiteTypeName(siteTypeName);
			Integer siteTypeCount = quoteIzosdwanSiteRepository.getSiteTpeCount(solutions.getId(), siteTypeName);
			if (siteTypeName.contains("Dual")
					|| IzosdwanCommonConstants.BYONUNDERLAYSHAREDSITES.contains(siteTypeName)) {
				siteTypeCount /= 2;
			}
			configurationSummaryBean.setNoOfSites(siteTypeCount);
			configurationSummaryBean.setCpeTypes(cpeTypes);
			configurationSummaryBean.setCpeInfo(cpeConfigInformation);
		} catch (Exception e) {
			LOGGER.error("Error occured on getting Configuration details!!", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return configurationSummaryBean;
	}

	private List<ConfigurationCpeInfo> getCpeConfigInfo(List<CpeTypes> cpeTypes, Integer id, String type,
			String isShared) {
		List<ConfigurationCpeInfo> configCpeInfo = new ArrayList<>();
		if (isShared != null && isShared.equals("Y")) {
			for (CpeTypes cpe : cpeTypes) {
				ConfigurationCpeInfo primaryCpeInfo = new ConfigurationCpeInfo();
				primaryCpeInfo.setCpetype(cpe.getCpeTypeName());
				List<BandWidthSummaryCpeBean> primary = getCpePrimaryDet(id, type);
				List<BandWidthSummaryCpeBean> secondary = getCpeSecondary(id, type);
//				List<BandwidthDet> SecBandwidthSummary = new ArrayList<>();
//				secondary.stream().forEach(s -> SecBandwidthSummary.addAll(s.getBandwidthSummary()));
//				primary.stream().forEach(a -> a.getBandwidthSummary().addAll(SecBandwidthSummary));
				for (BandWidthSummaryCpeBean priBean : primary) {
					for (BandWidthSummaryCpeBean secBean : secondary) {
						if (secBean.getCpeBasicChassis().equalsIgnoreCase(priBean.getCpeBasicChassis()))
							priBean.getBandwidthSummary().addAll(secBean.getBandwidthSummary());
					}
				}
				primaryCpeInfo.setCpeDetails(primary);
				configCpeInfo.add(primaryCpeInfo);
				break;
			}
		} else {
			for (CpeTypes cpe : cpeTypes) {
				if (cpe.getCpeTypeName().equals("Primary")) {
					ConfigurationCpeInfo primaryCpeInfo = new ConfigurationCpeInfo();
					primaryCpeInfo.setCpetype(cpe.getCpeTypeName());
					List<BandWidthSummaryCpeBean> primary = getCpePrimaryDet(id, type);
					primaryCpeInfo.setCpeDetails(primary);
					configCpeInfo.add(primaryCpeInfo);
				}
				if (cpe.getCpeTypeName().equals("Secondary")) {
					ConfigurationCpeInfo secondaryCpeInfo = new ConfigurationCpeInfo();
					secondaryCpeInfo.setCpetype(cpe.getCpeTypeName());
					List<BandWidthSummaryCpeBean> secondary = getCpeSecondary(id, type);
					secondaryCpeInfo.setCpeDetails(secondary);
					configCpeInfo.add(secondaryCpeInfo);
				}
			}
		}
		return configCpeInfo;
	}

	private List<BandWidthSummaryCpeBean> getCpeSecondary(Integer id, String type) {
		List<BandWidthSummaryCpeBean> secondaryCpeInfo = new ArrayList<>();
		List<String> getDistinctCpeModels = quoteIzosdwanSiteRepository.getDistinctCpeTypesForSdwan(id, "Secondary");
		for (String cpeName : getDistinctCpeModels) {
			BandWidthSummaryCpeBean secondaryBandWidthSummaryCpeBean = new BandWidthSummaryCpeBean();
			secondaryBandWidthSummaryCpeBean.setCpeBasicChassis(cpeName);
			Integer cpeCount = quoteIzosdwanSiteRepository.getCpeCount(id, cpeName, "Secondary", type);
			secondaryBandWidthSummaryCpeBean.setNoOfCpes(cpeCount);
			// GVPN SECONDARY local loop
			String gvpnSecondaryLoop = getLocalLoopBand(id, "Secondary", type, ProductType.GVPN.toString(), cpeName);
			// GVPN SECONDARY PORT
			String gvpnSecondaryPort = getPortBandwidth(id, "Secondary", type, ProductType.GVPN.toString(), cpeName);
			// IAS SECONDARY LOCAL
			String iasSecondaryLoop = getLocalLoopBand(id, "Secondary", type, ProductType.IAS.toString(), cpeName);
			// IAS SECONDARY PORT
			String iasSecondaryPort = getPortBandwidth(id, "Secondary", type, ProductType.IAS.toString(), cpeName);
			// BYON SECONDARY LOCAL
			String byonSecondaryLoop = getLocalLoopBand(id, "Secondary", type, ProductType.BYON.productTypeName(),
					cpeName);
			// BYON SECONDARY PORT
			String byonSecondaryPort = getPortBandwidth(id, "Secondary", type, ProductType.BYON.productTypeName(),
					cpeName);

			List<BandwidthDet> bandwidthSummarySecondaryDetails = new ArrayList<>();
			if (gvpnSecondaryLoop != null || gvpnSecondaryPort != null || iasSecondaryLoop != null
					|| iasSecondaryPort != null || byonSecondaryLoop != null || byonSecondaryPort != null) {
				if (gvpnSecondaryPort != null) {
					BandwidthDet bandWidthPort = new BandwidthDet();
					bandWidthPort.setBandwidthRange(getBand(gvpnSecondaryPort) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", bandWidthPort.getBandwidthRange());
					bandWidthPort.setBandwidthTypeName(ProductType.GVPN_PORT_BW.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Secondary.toString());
					bandWidthPort.setPortType(ProductType.Secondary.toString());
					bandWidthPort.setLinkType(ProductType.GVPN.toString());
					bandWidthPort.setSiteIds(siteIds);
					bandwidthSummarySecondaryDetails.add(bandWidthPort);
				}
				if (gvpnSecondaryLoop != null) {
					BandwidthDet gvpnLandLocal = new BandwidthDet();
					gvpnLandLocal.setBandwidthRange(getBand(gvpnSecondaryLoop) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", gvpnLandLocal.getBandwidthRange());
					gvpnLandLocal.setBandwidthTypeName(ProductType.GVPN_LOCAL_LOOP_BW.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Secondary.toString());
					gvpnLandLocal.setPortType(ProductType.Secondary.toString());
					gvpnLandLocal.setLinkType(ProductType.GVPN.toString());
					gvpnLandLocal.setSiteIds(siteIds);
					bandwidthSummarySecondaryDetails.add(gvpnLandLocal);
				}

				if (iasSecondaryPort != null) {
					BandwidthDet iasLandPort = new BandwidthDet();
					iasLandPort.setBandwidthRange(getBand(iasSecondaryPort) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", iasLandPort.getBandwidthRange());
					iasLandPort.setBandwidthTypeName(ProductType.IAS_PORT_BW.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Secondary.toString());
					iasLandPort.setPortType(ProductType.Secondary.toString());
					iasLandPort.setLinkType(ProductType.IAS.toString());
					iasLandPort.setSiteIds(siteIds);
					bandwidthSummarySecondaryDetails.add(iasLandPort);
				}
				if (iasSecondaryLoop != null) {
					BandwidthDet iasLandLocal = new BandwidthDet();
					iasLandLocal.setBandwidthRange(getBand(iasSecondaryLoop) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", iasLandLocal.getBandwidthRange());
					iasLandLocal.setBandwidthTypeName(ProductType.IAS_LOCAL_LOOP_BW.toString());
					iasLandLocal.setPortType(ProductType.Secondary.toString());
					iasLandLocal.setLinkType(ProductType.IAS.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Secondary.toString());
					iasLandLocal.setSiteIds(siteIds);
					bandwidthSummarySecondaryDetails.add(iasLandLocal);
				}
				if (byonSecondaryPort != null) {
					BandwidthDet byonLandPort = new BandwidthDet();
					byonLandPort.setBandwidthRange(getBand(byonSecondaryPort) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", byonLandPort.getBandwidthRange());
					byonLandPort.setBandwidthTypeName(ProductType.BYON_PORT_BW.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Secondary.toString());
					byonLandPort.setPortType(ProductType.Secondary.toString());
					byonLandPort.setLinkType(ProductType.BYON.productTypeName());
					byonLandPort.setSiteIds(siteIds);
					bandwidthSummarySecondaryDetails.add(byonLandPort);
				}
				if (byonSecondaryLoop != null) {
					BandwidthDet byonLandLocal = new BandwidthDet();
					byonLandLocal.setBandwidthRange(getBand(byonSecondaryLoop) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", byonLandLocal.getBandwidthRange());
					byonLandLocal.setBandwidthTypeName(ProductType.BYON_LOCAL_LOOP_BW.toString());
					byonLandLocal.setPortType(ProductType.Secondary.toString());
					byonLandLocal.setLinkType(ProductType.BYON.productTypeName());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Secondary.toString());
					byonLandLocal.setSiteIds(siteIds);
					bandwidthSummarySecondaryDetails.add(byonLandLocal);
				}
				if (bandwidthSummarySecondaryDetails.isEmpty()) {
					continue;
				} else {
					secondaryBandWidthSummaryCpeBean.setBandwidthSummary(bandwidthSummarySecondaryDetails);
					secondaryCpeInfo.add(secondaryBandWidthSummaryCpeBean);
				}
			}

		}
		return secondaryCpeInfo;
	}

	private List<BandWidthSummaryCpeBean> getCpePrimaryDet(Integer id, String type) {
		List<BandWidthSummaryCpeBean> primaryCpeInfo = new ArrayList<>();
		List<String> getDistinctCpeModels = quoteIzosdwanSiteRepository.getDistinctCpeTypesForSdwan(id, "Primary");
		for (String cpeName : getDistinctCpeModels) {
			BandWidthSummaryCpeBean primaryBandWidthSummaryCpeBean = new BandWidthSummaryCpeBean();
			primaryBandWidthSummaryCpeBean.setCpeBasicChassis(cpeName);
			Integer cpeCount = quoteIzosdwanSiteRepository.getCpeCount(id, cpeName, "Primary", type);
			primaryBandWidthSummaryCpeBean.setNoOfCpes(cpeCount);
			// GVPN Port bandwidth
			String gvpnPortBandwidthPrimary = getPortBandwidth(id, "Primary", type, ProductType.GVPN.toString(),
					cpeName);
			// Gvpn Local loop Bandwidth
			String gvpnLocalLoopBandwidthPrimary = getLocalLoopBand(id, "Primary", type, ProductType.GVPN.toString(),
					cpeName);
			// IAS port bandwidth
			String iasPortBandwidthPrimary = getPortBandwidth(id, "Primary", type, ProductType.IAS.toString(), cpeName);
			// IAS local loop bandwidth
			String iasLocalLoopBandwidthPrimary = getLocalLoopBand(id, "Primary", type, ProductType.IAS.toString(),
					cpeName);
			// BYON port bandwidth
			String byonPortBandwidthPrimary = getPortBandwidth(id, "Primary", type, ProductType.BYON.productTypeName(),
					cpeName);
			// BYON local loop bandwidth
			String byonLocalLoopBandwidthPrimary = getLocalLoopBand(id, "Primary", type,
					ProductType.BYON.productTypeName(), cpeName);

			List<BandwidthDet> bandwidthSummaryPrimaryDetails = new ArrayList<>();
			if (gvpnPortBandwidthPrimary != null || gvpnLocalLoopBandwidthPrimary != null
					|| iasPortBandwidthPrimary != null || iasLocalLoopBandwidthPrimary != null
					|| byonPortBandwidthPrimary != null || byonLocalLoopBandwidthPrimary != null) {
				if (gvpnPortBandwidthPrimary != null) {
					BandwidthDet gvpnPortBand = new BandwidthDet();
					gvpnPortBand.setBandwidthRange(getBand(gvpnPortBandwidthPrimary) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", gvpnPortBand.getBandwidthRange());
					gvpnPortBand.setBandwidthTypeName(ProductType.GVPN_PORT_BW.toString());
					gvpnPortBand.setPortType(ProductType.Primary.toString());
					gvpnPortBand.setLinkType(ProductType.GVPN.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Primary.toString());
					gvpnPortBand.setSiteIds(siteIds);
					bandwidthSummaryPrimaryDetails.add(gvpnPortBand);
				}
				if (gvpnLocalLoopBandwidthPrimary != null) {
					BandwidthDet gvpnLocalBand = new BandwidthDet();
					gvpnLocalBand.setBandwidthRange(getBand(gvpnLocalLoopBandwidthPrimary) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", gvpnLocalBand.getBandwidthRange());
					gvpnLocalBand.setBandwidthTypeName(ProductType.GVPN_LOCAL_LOOP_BW.toString());
					gvpnLocalBand.setPortType(ProductType.Primary.toString());
					gvpnLocalBand.setLinkType(ProductType.GVPN.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Primary.toString());
					gvpnLocalBand.setSiteIds(siteIds);
					bandwidthSummaryPrimaryDetails.add(gvpnLocalBand);
				}
				if (iasPortBandwidthPrimary != null) {
					BandwidthDet iasBandPort = new BandwidthDet();
					iasBandPort.setBandwidthRange(getBand(iasPortBandwidthPrimary) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", iasBandPort.getBandwidthRange());
					iasBandPort.setBandwidthTypeName(ProductType.IAS_PORT_BW.toString());
					iasBandPort.setPortType(ProductType.Primary.toString());
					iasBandPort.setLinkType(ProductType.IAS.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Primary.toString());
					iasBandPort.setSiteIds(siteIds);
					bandwidthSummaryPrimaryDetails.add(iasBandPort);
				}
				if (iasLocalLoopBandwidthPrimary != null) {
					BandwidthDet iasLandLocal = new BandwidthDet();
					iasLandLocal.setBandwidthRange(getBand(iasLocalLoopBandwidthPrimary) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", iasLandLocal.getBandwidthRange());
					iasLandLocal.setBandwidthTypeName(ProductType.IAS_LOCAL_LOOP_BW.toString());
					iasLandLocal.setPortType(ProductType.Primary.toString());
					iasLandLocal.setLinkType(ProductType.IAS.toString());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Primary.toString());
					iasLandLocal.setSiteIds(siteIds);
					bandwidthSummaryPrimaryDetails.add(iasLandLocal);
				}
				if (byonPortBandwidthPrimary != null) {
					BandwidthDet byonBandPort = new BandwidthDet();
					byonBandPort.setBandwidthRange(getBand(byonPortBandwidthPrimary) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", byonBandPort.getBandwidthRange());
					byonBandPort.setBandwidthTypeName(ProductType.BYON_PORT_BW.toString());
					byonBandPort.setPortType(ProductType.Primary.toString());
					byonBandPort.setLinkType(ProductType.BYON.productTypeName());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Primary.toString());
					byonBandPort.setSiteIds(siteIds);
					bandwidthSummaryPrimaryDetails.add(byonBandPort);
				}
				if (byonLocalLoopBandwidthPrimary != null) {
					BandwidthDet byonLandLocal = new BandwidthDet();
					byonLandLocal.setBandwidthRange(getBand(byonLocalLoopBandwidthPrimary) + " " + "Mbps");
					LOGGER.info("GETTING THE PORT BAND DETAILS{}", byonLandLocal.getBandwidthRange());
					byonLandLocal.setBandwidthTypeName(ProductType.BYON_LOCAL_LOOP_BW.toString());
					byonLandLocal.setPortType(ProductType.Primary.toString());
					byonLandLocal.setLinkType(ProductType.BYON.productTypeName());
					List<BigInteger> siteIds = quoteIzosdwanSiteRepository.getSiteIdsByPriSec(id, cpeName, type,
							ProductType.Primary.toString());
					byonLandLocal.setSiteIds(siteIds);
					bandwidthSummaryPrimaryDetails.add(byonLandLocal);
				}

			}
			if (bandwidthSummaryPrimaryDetails.isEmpty()) {
				continue;
			} else {
				primaryBandWidthSummaryCpeBean.setBandwidthSummary(bandwidthSummaryPrimaryDetails);
				primaryCpeInfo.add(primaryBandWidthSummaryCpeBean);
			}

		}
		return primaryCpeInfo;
	}

	private List<CpeTypes> constructCpeTypeDet(ProductSolution productSolution, String type) {
		List<CpeTypes> cpeTypes = new ArrayList<>();
		List<CpeLinks> cpePrimaryLinks = new ArrayList<>();
		List<CpeLinks> cpeSecondaryLinks = new ArrayList<>();
		// Primary GVPN
		String primaryGvpnBwRange = quoteIzosdwanSiteRepository.getBandwidth(productSolution.getId(), "Primary", type,
				CommonConstants.GVPN);
		LOGGER.info("THE BANDWIDTH IS {}", primaryGvpnBwRange);
		// Primary IAS
		String primaryIasBwRange = quoteIzosdwanSiteRepository.getBandwidth(productSolution.getId(), "Primary", type,
				CommonConstants.IAS);
		// primary BYON
		String primaryBYONBwRange = quoteIzosdwanSiteRepository.getBandwidth(productSolution.getId(), "Primary", type,
				IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
		// Secondary GVPN
		String secGvpnBwRange = quoteIzosdwanSiteRepository.getBandwidth(productSolution.getId(), "SECONDARY", type,
				CommonConstants.GVPN);
		// Secondary IAS
		String secIasBwRange = quoteIzosdwanSiteRepository.getBandwidth(productSolution.getId(), "SECONDARY", type,
				CommonConstants.IAS);
		String secBYONBwRange = quoteIzosdwanSiteRepository.getBandwidth(productSolution.getId(), "SECONDARY", type,
				IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);

		if (primaryGvpnBwRange != null || primaryIasBwRange != null || primaryBYONBwRange != null) {
			CpeTypes primaryCpeType = new CpeTypes();
			primaryCpeType.setCpeTypeName(ProductType.Primary.toString());
			if (primaryGvpnBwRange != null) {
				CpeLinks gvpnLink = new CpeLinks();
				gvpnLink.setLinkName(ProductType.GVPN.toString());
				gvpnLink.setRange(getBand(primaryGvpnBwRange) + " " + "Mbps");
				LOGGER.info("After Setting the Bandwidth{}", gvpnLink.getRange());
				cpePrimaryLinks.add(gvpnLink);
			}
			if (primaryIasBwRange != null) {
				CpeLinks iasLink = new CpeLinks();
				iasLink.setLinkName(ProductType.IAS.toString());
				iasLink.setRange(getBand(primaryIasBwRange) + " " + "Mbps");
				LOGGER.info("After Setting the Bandwidth{}", iasLink.getRange());
				cpePrimaryLinks.add(iasLink);

			}
			if (primaryBYONBwRange != null) {
				CpeLinks byonLink = new CpeLinks();
				byonLink.setLinkName(ProductType.BYON.productTypeName());
				byonLink.setRange(getBand(primaryBYONBwRange) + " " + "Mbps");
				LOGGER.info("After Setting the Bandwidth{}", byonLink.getRange());
				cpePrimaryLinks.add(byonLink);

			}
			primaryCpeType.setCpeLinks(cpePrimaryLinks);
			cpeTypes.add(primaryCpeType);
		}
		if (secGvpnBwRange != null || secIasBwRange != null || secBYONBwRange != null) {
			CpeTypes secondaryCpeType = new CpeTypes();
			secondaryCpeType.setCpeTypeName(ProductType.Secondary.toString());
			if (secGvpnBwRange != null) {
				CpeLinks gvpnLink = new CpeLinks();
				gvpnLink.setLinkName(ProductType.GVPN.toString());
				gvpnLink.setRange(getBand(secGvpnBwRange) + " " + "Mbps");
				LOGGER.info("After Setting the Bandwidth{}", gvpnLink.getRange());
				cpeSecondaryLinks.add(gvpnLink);
			}
			if (secIasBwRange != null) {
				CpeLinks iasLink = new CpeLinks();
				iasLink.setLinkName(ProductType.IAS.toString());
				iasLink.setRange(getBand(secIasBwRange) + " " + "Mbps");
				LOGGER.info("After Setting the Bandwidth{}", iasLink.getRange());
				cpeSecondaryLinks.add(iasLink);

			}
			if (secBYONBwRange != null) {
				CpeLinks byonLink = new CpeLinks();
				byonLink.setLinkName(ProductType.BYON.productTypeName());
				byonLink.setRange(getBand(secBYONBwRange) + " " + "Mbps");
				LOGGER.info("After Setting the Bandwidth{}", byonLink.getRange());
				cpeSecondaryLinks.add(byonLink);

			}
			secondaryCpeType.setCpeLinks(cpeSecondaryLinks);
			cpeTypes.add(secondaryCpeType);
		}
		return cpeTypes;
	}

	public ConfigurationCpeInfo getCpeInfo(List<ConfigurationCpeInfo> cpeInfo, String type) {
		ConfigurationCpeInfo info = new ConfigurationCpeInfo();
		for (ConfigurationCpeInfo infoDet : cpeInfo) {
			if (infoDet.getCpetype().equals(type)) {
				info = infoDet;
			}
		}
		return info;
	}

	private String getBand(String bandwidth) {
		String[] band = bandwidth.split("-");
		Integer min = Integer.parseInt(band[0]);
		Integer max = Integer.parseInt(band[1]);
		if (min.equals(max)) {
			String bandValue = min.toString();
			LOGGER.info("CHECKING THE BANDWIDTH{}", bandValue);
			return bandValue;
		}
		return bandwidth;
	}

	private String getLocalLoopBand(Integer id, String type, String siteType, String productName, String cpeName) {
		String localLoopBandwidth = quoteIzosdwanSiteRepository.getLocalLoopBandwidthRange(id, type, siteType,
				productName, cpeName);
		return localLoopBandwidth;

	}

	private String getPortBandwidth(Integer id, String type, String siteType, String productName, String cpeName) {
		String portBandwidth = quoteIzosdwanSiteRepository.getPortBandWidth(id, type, siteType, productName, cpeName);
		return portBandwidth;
	}

	private void constructCpeSummaryDetails(List<QuoteIzosdwanSite> quoteIzosdwanSites, SiteTypes siteTypes,
			Map<String, Integer> cpeCounts) {
		CpeSummary cpeSummary = new CpeSummary();
		List<CpeModelDetails> cpeModelDetails = new ArrayList<>();
		// Managed - Retained
		List<QuoteIzosdwanSite> quoteIzosdwanSitesManaged = quoteIzosdwanSites.stream()
				.filter(site -> !site.getManagementType().equalsIgnoreCase("Unmanaged")).collect(Collectors.toList());
		Map<String, Integer> retainedCountManaged = new HashMap<>();
		Map<String, Integer> replacedCountManaged = new HashMap<>();
		Map<String, Integer> newCount = new HashMap<>();
		Integer sharedManaged = 0;
		Integer sharedUnmanaged = 0;
		if (quoteIzosdwanSitesManaged != null) {
			for (QuoteIzosdwanSite site : quoteIzosdwanSitesManaged) {
				if (!(site.getIsShared() != null && site.getIsShared().equals("Y")
						&& (site.getPriSec() != null && site.getPriSec().equalsIgnoreCase("SECONDARY")))) {
					if (site.getOldCpe() == null && site.getNewCpe() != null) {
						if (newCount.containsKey(site.getNewCpe()) && (!(site.getIzosdwanSiteType().contains("Shared")
								&& (site.getPriSec().equalsIgnoreCase("SECONDARY"))))) {
							newCount.put(site.getNewCpe(), newCount.get(site.getNewCpe()) + 1);
						} else {
							newCount.put(site.getNewCpe(), 1);
						}
					} else if (site.getOldCpe() != null && site.getNewCpe() != null
							&& site.getOldCpe().equals(site.getNewCpe())) {
						if (retainedCountManaged.containsKey(site.getNewCpe())) {
							retainedCountManaged.put(site.getNewCpe(), retainedCountManaged.get(site.getNewCpe()) + 1);
						} else {
							retainedCountManaged.put(site.getNewCpe(), 1);
						}
					} else if (site.getOldCpe() != null && site.getNewCpe() != null
							&& !site.getOldCpe().equals(site.getNewCpe())) {
						if (replacedCountManaged.containsKey(site.getNewCpe())) {
							replacedCountManaged.put(site.getNewCpe(), replacedCountManaged.get(site.getNewCpe()) + 1);
						} else {
							replacedCountManaged.put(site.getNewCpe(), 1);
						}
					}
				} else {
					sharedManaged = sharedManaged + 1;
				}
			}
		}
		// UnManaged
		List<QuoteIzosdwanSite> quoteIzosdwanSitesUnManaged = quoteIzosdwanSites.stream()
				.filter(site -> site.getManagementType().equalsIgnoreCase("Unmanaged")).collect(Collectors.toList());
//		Map<String, Integer> replacedCountUnManaged = new HashMap<>();
		if (quoteIzosdwanSitesUnManaged != null) {
			for (QuoteIzosdwanSite site : quoteIzosdwanSitesUnManaged) {
				if (!(site.getIsShared() != null && site.getIsShared().equals("Y") && site.getPriSec() != null
						&& site.getPriSec().contains("SECONDARY"))) {
					if (site.getNewCpe() != null) {
						if (newCount.containsKey(site.getNewCpe())) {
							newCount.put(site.getNewCpe(), newCount.get(site.getNewCpe()) + 1);
						} else {
							newCount.put(site.getNewCpe(), 1);
						}
					}
				} else {
					sharedUnmanaged = sharedUnmanaged + 1;
				}
			}
		}
		if (retainedCountManaged != null && !retainedCountManaged.isEmpty()) {
			CpeModelDetails cpeModelDetailsRetainedManaged = new CpeModelDetails();
			cpeModelDetailsRetainedManaged.setNameOfCpe("Retained CPE");
			List<CpeModel> cpeModels = new ArrayList<>();
			for (String key : retainedCountManaged.keySet()) {
				CpeModel cpeModel = new CpeModel();
				cpeModel.setCpeType(key);
				cpeModel.setNoOfSites(retainedCountManaged.get(key) + sharedManaged);
				cpeModels.add(cpeModel);
				if (cpeCounts.containsKey("Retained")) {
					cpeCounts.put("Retained", cpeCounts.get("Retained") + retainedCountManaged.get(key));
				} else {
					cpeCounts.put("Retained", retainedCountManaged.get(key));
				}
			}

			cpeModelDetailsRetainedManaged.setCpeModel(cpeModels);
			cpeModelDetails.add(cpeModelDetailsRetainedManaged);
		}
		if (replacedCountManaged != null && !replacedCountManaged.isEmpty()) {
			CpeModelDetails cpeModelDetailsRetainedManaged = new CpeModelDetails();
			cpeModelDetailsRetainedManaged.setNameOfCpe("Replaced CPE");
			List<CpeModel> cpeModels = new ArrayList<>();
			for (String key : replacedCountManaged.keySet()) {
				CpeModel cpeModel = new CpeModel();
				cpeModel.setCpeType(key);
				cpeModel.setNoOfSites(replacedCountManaged.get(key) + sharedManaged);
				cpeModels.add(cpeModel);
				if (cpeCounts.containsKey("Replaced")) {
					cpeCounts.put("Replaced", cpeCounts.get("Replaced") + replacedCountManaged.get(key));
				} else {
					cpeCounts.put("Replaced", replacedCountManaged.get(key));
				}
			}
			cpeModelDetailsRetainedManaged.setCpeModel(cpeModels);
			cpeModelDetails.add(cpeModelDetailsRetainedManaged);
		}
		if (newCount != null && !newCount.isEmpty()) {
			CpeModelDetails cpeModelDetailsRetainedManaged = new CpeModelDetails();
			cpeModelDetailsRetainedManaged.setNameOfCpe("New CPE");
			List<CpeModel> cpeModels = new ArrayList<>();
			for (String key : newCount.keySet()) {
				CpeModel cpeModel = new CpeModel();
				cpeModel.setCpeType(key);
				cpeModel.setNoOfSites(newCount.get(key) + sharedUnmanaged + sharedManaged);
				cpeModels.add(cpeModel);
				if (cpeCounts.containsKey("New")) {
					cpeCounts.put("New", cpeCounts.get("New") + newCount.get(key));
				} else {
					cpeCounts.put("New", newCount.get(key));
				}
			}
			cpeModelDetailsRetainedManaged.setCpeModel(cpeModels);
			cpeModelDetails.add(cpeModelDetailsRetainedManaged);
		}
		cpeSummary.setCpeModelDetails(cpeModelDetails);
		siteTypes.setCpeSummaryDet(cpeSummary);

	}

	private void constructBandwidthSummaryDetails(SiteTypes siteTypes, String type, Integer productSolutionId) {
		List<BandwidthDetails> bandwidthDetailsList = new ArrayList<>();
		// Autoupgraded
		List<BigInteger> autoUpBws = quoteIzosdwanSiteRepository.getUniqueBwLessThan2Mb(productSolutionId, type);
		if (autoUpBws != null && !autoUpBws.isEmpty()) {
			BandwidthDetails bandwidthDetails = new BandwidthDetails();
			bandwidthDetails.setNameOfBandwidthType("Auto-Upgraded Bandwidth");
			bandwidthDetails.setNoOfLinks(0);
			List<BandwidthRangeDetails> bandwidthRangeDetails = new ArrayList<>();
			autoUpBws.stream().forEach(auto -> {
				BandwidthRangeDetails bandwidthRangeDetailsBean = new BandwidthRangeDetails();
				bandwidthRangeDetailsBean.setRangeOfBandwidth(auto.toString());
				Integer links = quoteIzosdwanSiteRepository.getCountBasedOnOldBw(auto.intValue(), productSolutionId,
						type);
				bandwidthRangeDetailsBean.setNoOfLinks(links);
				bandwidthDetails.setNoOfLinks(links.intValue() + bandwidthDetails.getNoOfLinks());
				bandwidthRangeDetails.add(bandwidthRangeDetailsBean);
			});
			bandwidthDetails.setBandwidthRangeDet(bandwidthRangeDetails);
			bandwidthDetailsList.add(bandwidthDetails);
		}
		// Retained
		Integer retainedCount = quoteIzosdwanSiteRepository.getCountOfRetainedBandwidth(productSolutionId, type);
		if (retainedCount != null) {
			BandwidthDetails bandwidthDetails = new BandwidthDetails();
			bandwidthDetails.setNameOfBandwidthType("Retained Bandwidth");
			bandwidthDetails.setNoOfLinks(retainedCount);
			bandwidthDetailsList.add(bandwidthDetails);
		}

		// User Upgraded
		Integer replacedCount = quoteIzosdwanSiteRepository.getCountOfReplacedBandwidth(productSolutionId, type);
		Integer autoUpgradedUserReplaced = quoteIzosdwanSiteRepository
				.getCountOfAutoUpgradedBwWhichWasUserUpgrade(productSolutionId, type);
		Integer count = 0;
		if (replacedCount != null && replacedCount > 0) {
			count = count + replacedCount;
		}
		if (autoUpgradedUserReplaced != null && autoUpgradedUserReplaced > 0) {
			count = count + autoUpgradedUserReplaced;
		}
		if (count > 0) {
			BandwidthDetails bandwidthDetails = new BandwidthDetails();
			bandwidthDetails.setNameOfBandwidthType("User Upgraded Bandwidth");
//			bandwidthDetails.setNoOfLinks(replacedCount);
			bandwidthDetails.setNoOfLinks(count);
			bandwidthDetailsList.add(bandwidthDetails);
		}

		siteTypes.setBandwidthDet(bandwidthDetailsList);
	}

	public List<ViewSitesSummaryBean> getSitesBasedOnSiteType(Integer quoteId, String type,
			LocationInputDetails locDetails) throws TclCommonException {
		List<ViewSitesSummaryBean> viewSitesSummaryBeans = new ArrayList<>();
		List<Integer> listOfLocationIds = new ArrayList<>();
		if (quoteId == null || type == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quoteDetails == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		if (locDetails != null) {
			if (locDetails.getTextToSearch() == null || locDetails.getTextToSearch().isEmpty()
					|| locDetails.getLocationIds().isEmpty()) {
				throw new TclCommonException(ExceptionConstants.TEXT_SEARCH, ResponseResource.R_CODE_NOT_FOUND);
			}

			String locDet = (String) mqUtils.sendAndReceive(locationIds, Utils.convertObjectToJson(locDetails));
			LOGGER.info("THE JSON IS {}", locDet.toString());
			listOfLocationIds = GscUtils.fromJson(locDet, List.class);
			if (listOfLocationIds.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.DETAILS_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);
			}
		}
		try {
			ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteDetails.getId());
			List<QuoteIzosdwanSite> sdwanSiteDetails = new ArrayList<>();
			if (listOfLocationIds.isEmpty()) {
				if (type == null || CommonConstants.EMPTY.equals(type)) {
					sdwanSiteDetails = quoteIzosdwanSiteRepository.findByProductSolution(solutions);
				} else {
					sdwanSiteDetails = quoteIzosdwanSiteRepository.findByProductSolutionAndIzosdwanSiteType(solutions,
							type);
				}

				constructViewSiteSummary(sdwanSiteDetails, viewSitesSummaryBeans);
			} else {
				for (Integer id : listOfLocationIds) {
					if (type == null || CommonConstants.EMPTY.equals(type)) {
						sdwanSiteDetails = quoteIzosdwanSiteRepository
								.findByProductSolutionAndErfLocSitebLocationId(solutions, id);

					} else {
						sdwanSiteDetails = quoteIzosdwanSiteRepository
								.findByProductSolutionAndIzosdwanSiteTypeAndErfLocSitebLocationId(solutions, type, id);
					}

					constructViewSiteSummary(sdwanSiteDetails, viewSitesSummaryBeans);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occured on getting network summary details!!", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return viewSitesSummaryBeans;
	}

	private void constructViewSiteSummary(List<QuoteIzosdwanSite> sdwanSiteDetails,
			List<ViewSitesSummaryBean> viewSitesSummaryBeans) {
		sdwanSiteDetails.stream().forEach(site -> {
			if (site.getId() != null && site.getErfLocSitebLocationId() != null) {
				ViewSitesSummaryBean viewSitesSummaryBean = new ViewSitesSummaryBean();
				if (viewSitesSummaryBeans.isEmpty()) {
					viewSitesSummaryBean.setLocationId(site.getErfLocSitebLocationId());
					List<Integer> sites = new ArrayList<>();
					sites.add(site.getId());
					viewSitesSummaryBean.setSiteIds(sites);
					viewSitesSummaryBeans.add(viewSitesSummaryBean);

				} else {
					boolean flag1 = getSites(viewSitesSummaryBeans, site.getId(), site.getErfLocSitebLocationId());
					if (flag1) {
						viewSitesSummaryBean.setLocationId(site.getErfLocSitebLocationId());
						List<Integer> sites = new ArrayList<>();
						sites.add(site.getId());
						viewSitesSummaryBean.setSiteIds(sites);
						viewSitesSummaryBeans.add(viewSitesSummaryBean);
					}
				}
			}
		});
	}

	private boolean getSites(List<ViewSitesSummaryBean> viewSummary, Integer siteId, Integer LocationId) {
		for (ViewSitesSummaryBean sites : viewSummary) {
			if (sites.getLocationId().equals(LocationId)) {
				sites.getSiteIds().add(siteId);
				return false;
			}
		}

		return true;
	}

	private void constructCpeSummaryDetailsInSummary(Map<String, Integer> cpeCounts, Integer noOfSites,
			NetworkSummaryDetails networkSummaryDetails) {
		CpeSummaryDetails cpeSummaryDetails = new CpeSummaryDetails();
		if (cpeCounts.containsKey("New")) {
			cpeSummaryDetails.setNewCpesCount(cpeCounts.get("New"));
		}
		if (cpeCounts.containsKey("Replaced")) {
			cpeSummaryDetails.setReplacedCpesCount(cpeCounts.get("Replaced"));
		}
		if (cpeCounts.containsKey("Retained")) {
			cpeSummaryDetails.setRetainedCpesCount(cpeCounts.get("Retained"));
		}
		CpeBandwidthSummaryDetails cpeBandwidthSummaryDetails = new CpeBandwidthSummaryDetails();
		cpeBandwidthSummaryDetails.setCpeSummaryDet(cpeSummaryDetails);
		cpeBandwidthSummaryDetails.setTotalNoOfSites(0);
		networkSummaryDetails.getSiteTypeSummary().getSiteDetails().stream().forEach(type -> {
			cpeBandwidthSummaryDetails
					.setTotalNoOfSites(cpeBandwidthSummaryDetails.getTotalNoOfSites() + type.getNoOfSites());
		});
		networkSummaryDetails.setCpeBandwidthSummaryDet(cpeBandwidthSummaryDetails);
	}

	public QuoteDetail updateSitePropertiesAttributes(List<UpdateRequest> request) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			validateUpdateRequest(request);
			detail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			Integer userId = null;
			if (user != null) {
				userId = user.getId();
			}
			final Integer id = userId;
			if (!request.isEmpty()) {
				request.stream().forEach(req -> {
					if (req.getSiteIdList() != null && !req.getSiteIdList().isEmpty()) {
						req.getSiteIdList().stream().forEach(siteId -> {
							QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSiteRepository.findByIdAndStatus(siteId,
									(byte) 1);
							MstProductFamily mstProductFamily = mstProductFamilyRepository
									.findByNameAndStatus(req.getFamilyName(), (byte) 1);
							if (quoteIzosdwanSite != null && mstProductFamily != null) {
								MstProductComponent mstProductComponent = getMstProperties(user);
								constructIllSitePropeties(mstProductComponent, quoteIzosdwanSite, user.getUsername(),
										req, mstProductFamily);
								updateIzosdwansiteProperties(quoteIzosdwanSite, req, id);
							}
						});
					}
				});
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	private void updateIzosdwansiteProperties(QuoteIzosdwanSite updateSiteInfo, UpdateRequest request, Integer id) {
		if (request.getAttributeDetails() != null) {
			
			for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
				LOGGER.info("Attribute Value {}", attributeDetail.getValue());
				if (attributeDetail.getName().equals(IzosdwanCommonConstants.CPE_BASIC_CHASSIS)) {
					LOGGER.info("BEFORE SETTING CPE NAME IS {}", updateSiteInfo.getNewCpe());
					LOGGER.info("IN ATTRIBUTE IS {}", attributeDetail.getValue());
					updateSiteInfo.setNewCpe(attributeDetail.getValue());
					updateSiteInfo.setUpdatedBy(id);
					updateSiteInfo.setUpdatedTime(new Date());
					updateCpeAttributes(updateSiteInfo, attributeDetail);
					// updateSiteInfo.setIsFeasiblityCheckRequired(CommonConstants.ACTIVE);
				}

				if (attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
						&& !attributeDetail.getValue().contains("-")) {
					updateSiteInfo.setNewPortBandwidth(attributeDetail.getValue());
					updateSiteInfo.setUpdatedBy(id);
					updateSiteInfo.setUpdatedTime(new Date());
					// updateSiteInfo.setIsFeasiblityCheckRequired(CommonConstants.ACTIVE);

				}

				if (attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
						&& !attributeDetail.getValue().contains("-")) {
					updateSiteInfo.setNewLastmileBandwidth(attributeDetail.getValue());
					updateSiteInfo.setNewLastmileBandwidth(attributeDetail.getValue());
					updateSiteInfo.setUpdatedBy(id);
					updateSiteInfo.setUpdatedTime(new Date());
					if (updateSiteInfo.getOldLastmileBandwidth() != null
							&& !updateSiteInfo.getOldLastmileBandwidth().equals(attributeDetail.getValue())) {
						updateSiteInfo.setIsFeasiblityCheckRequired(CommonConstants.ACTIVE);
						updateSiteInfo.setIsPricingCheckRequired(CommonConstants.INACTIVE);
					}
				}
				if (attributeDetail.getName().equals(IzosdwanCommonConstants.INTERFACE)) {
					if (!updateSiteInfo.getNewCpe().isEmpty() && updateSiteInfo.getNewCpe() != null) {
						updateCpeAttributes(updateSiteInfo, attributeDetail);
					}
				}
				updateSiteInfo = quoteIzosdwanSiteRepository.save(updateSiteInfo);

			}
			List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository
					.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(
							updateSiteInfo.getErfServiceInventoryTpsServiceId(),
							updateSiteInfo.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe());
			if (quoteSiteToService != null && !quoteSiteToService.isEmpty()) {
				LOGGER.info("Updating bandwidth editted data in QuoteIllSiteToService for site id {} ",
						updateSiteInfo.getId());
				quoteSiteToService.get(0)
						.setBandwidthChanged((updateSiteInfo.getIsFeasiblityCheckRequired() != null
								&& updateSiteInfo.getIsFeasiblityCheckRequired().equals(CommonConstants.ACTIVE))
										? CommonConstants.BACTIVE
										: CommonConstants.BDEACTIVATE);
				quoteIllSiteToServiceRepository.save(quoteSiteToService.get(0));
			}
			LOGGER.info("AFTER SETTING CPE NAME IS {}", updateSiteInfo.getNewCpe());
		}
	}

	/**
	 * @author mpalanis
	 * 
	 * @param site
	 * @param attributeDetail
	 */
	private void updateCpeAttributes(QuoteIzosdwanSite site, AttributeDetail attributeDetail) {
		List<IzoSdwanCpeBomInterface> cpeBomInterfaces = new ArrayList<>();
		String nsQuote  = site.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getNsQuote();
		String interfaceType = "";
		try {
			cpeBomInterfaces = getInterfaceDetails();
			if (site.getNewCpe() != null) {
				if (cpeBomInterfaces != null && !cpeBomInterfaces.isEmpty()) {
					cpeBomInterfaces = cpeBomInterfaces.stream()
							.filter(cpe -> site.getNewCpe().equalsIgnoreCase(cpe.getBomNameCd()))
							.collect(Collectors.toList());
				}
			}
			Map<String, ProductAttributeMaster> productAttributeMasterMap = new HashMap<>();
			List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository.findAll();
			if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
				productAttributeMasters.stream().forEach(master -> {
					productAttributeMasterMap.put(master.getName(), master);
				});
			}
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(site.getId(),
							IzosdwanCommonConstants.SITE_PROPERTIES, QuoteConstants.IZOSDWAN_SITES.toString());
			if (!cpeBomInterfaces.isEmpty() && cpeBomInterfaces != null) {
				if (attributeDetail.getName().equalsIgnoreCase(IzosdwanCommonConstants.CPE_BASIC_CHASSIS)) {
					interfaceType = getAttributeValue(site.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
							IzosdwanCommonConstants.INTERFACE);
					removePhysicalResourceAttributes(site.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
							IzosdwanCommonConstants.CPE_BASIC_CHASSIS);
				}
				if (attributeDetail.getName().equalsIgnoreCase(IzosdwanCommonConstants.INTERFACE)) {
					removePhysicalResourceAttributes(site.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
							IzosdwanCommonConstants.INTERFACE);
					interfaceType = attributeDetail.getValue();
				}
				for (IzoSdwanCpeBomInterface cpeBomInterface : cpeBomInterfaces) {
					if ((cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP"))
							|| (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP+"))) {
						if (cpeBomInterface.getInterfaceType().equals(interfaceType)) {
							if (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP")) {
								if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
									LOGGER.info("Updating the sfp");
									updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.SFP,
											productAttributeMasterMap, cpeBomInterface.getPhysicalResourceCd(),
											quoteProductComponentList.get(0));
									updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.SFP_DESC,
											productAttributeMasterMap, cpeBomInterface.getDescription(),
											quoteProductComponentList.get(0));
									if (!site.getServiceSiteCountry().equalsIgnoreCase("India")) {
										updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.SFP_COST,
												productAttributeMasterMap, StringUtils.EMPTY,
												quoteProductComponentList.get(0));
									}
								}
							}
							if (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP+")) {
								if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
									LOGGER.info("Updating the sfp_plus");
									updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.SFP_PLUS,
											productAttributeMasterMap, cpeBomInterface.getPhysicalResourceCd(),
											quoteProductComponentList.get(0));
									updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.SFP_PLUS_DESC,
											productAttributeMasterMap, cpeBomInterface.getDescription(),
											quoteProductComponentList.get(0));
									if (!site.getServiceSiteCountry().equalsIgnoreCase("India")) {
										updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.SFP_PLUS_COST,
												productAttributeMasterMap, StringUtils.EMPTY,
												quoteProductComponentList.get(0));
									}
								}
							}
						}
					} else {
						if (attributeDetail.getName().equalsIgnoreCase(IzosdwanCommonConstants.CPE_BASIC_CHASSIS)) {
							
							if (nsQuote == null && cpeBomInterface.getProductCategory().equalsIgnoreCase("NMC")) {
								updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.NMC,
										productAttributeMasterMap, cpeBomInterface.getPhysicalResourceCd(),
										quoteProductComponentList.get(0));
								updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.NMC_DESC,
										productAttributeMasterMap, cpeBomInterface.getDescription(),
										quoteProductComponentList.get(0));
								if (!site.getServiceSiteCountry().equalsIgnoreCase("India")) {
									updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.NMC_COST,
											productAttributeMasterMap, StringUtils.EMPTY,
											quoteProductComponentList.get(0));
								}
							}
							
							if (cpeBomInterface.getProductCategory().equalsIgnoreCase("Rackmount")) {
								updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.RACKMOUNT,
										productAttributeMasterMap, cpeBomInterface.getPhysicalResourceCd(),
										quoteProductComponentList.get(0));
								updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.RACKMOUNT_DESC,
										productAttributeMasterMap, cpeBomInterface.getDescription(),
										quoteProductComponentList.get(0));
								if (!site.getServiceSiteCountry().equalsIgnoreCase("India")) {
									updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.RACKMOUNT_COST,
											productAttributeMasterMap, StringUtils.EMPTY,
											quoteProductComponentList.get(0));
								}
							}
							if (cpeBomInterface.getProductCategory().contains("Power")) {
								updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.POWER_CORD,
										productAttributeMasterMap, cpeBomInterface.getPhysicalResourceCd(),
										quoteProductComponentList.get(0));
								updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.POWERCORD_DESC,
										productAttributeMasterMap, cpeBomInterface.getDescription(),
										quoteProductComponentList.get(0));
								if (!site.getServiceSiteCountry().equalsIgnoreCase("India")) {
									updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.POWER_CORD_COST,
											productAttributeMasterMap, StringUtils.EMPTY,
											quoteProductComponentList.get(0));
								}
							}
							updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.CPE_MODEL_END_OF_SALE,
									productAttributeMasterMap, cpeBomInterfaces.get(0).getCpeModelEndOfSale(),
									quoteProductComponentList.get(0));
							updateCpePhysicalResourceAttributes(IzosdwanCommonConstants.CPE_MODEL_END_OF_LIFE,
									productAttributeMasterMap, cpeBomInterfaces.get(0).getCpeModelEndOfLife(),
									quoteProductComponentList.get(0));
						}
					}
				}
			}

		} catch (TclCommonException | IllegalArgumentException e) {

		}
	}

	/**
	 * @author mpalanis
	 * @param attributeName
	 * @param productAttributeMasterMap
	 * @param attributeValue
	 * @param quoteProductComponent
	 * @throws TclCommonException
	 */
	public void updateCpePhysicalResourceAttributes(String attributeName,
			Map<String, ProductAttributeMaster> productAttributeMasterMap, String attributeValue,
			QuoteProductComponent quoteProductComponent) throws TclCommonException {
		ProductAttributeMaster productAttributeMaster = getProductAttributeMasterByName(attributeName,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, attributeValue);
	}

	/**
	 * removeComponentsAndAttr
	 * 
	 * @param siteId
	 */
	private void removePhysicalResourceAttributes(Integer siteId, String referenceName, String type) {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
						IzosdwanCommonConstants.SITE_PROPERTIES, QuoteConstants.IZOSDWAN_SITES.toString());
		if (!quoteProductComponents.isEmpty()) {
			quoteProductComponents.forEach(quoteProd -> {

				quoteProd.getQuoteProductComponentsAttributeValues().forEach(attr -> {
					if (type.equalsIgnoreCase(IzosdwanCommonConstants.CPE_BASIC_CHASSIS)
							&& IzosdwanCommonConstants.PHYSICAL_RESOURCE
									.contains(attr.getProductAttributeMaster().getName().toLowerCase())) {
						quoteProductComponentsAttributeValueRepository.delete(attr);
					}
					if (type.equalsIgnoreCase(IzosdwanCommonConstants.INTERFACE)
							&& IzosdwanCommonConstants.PHYSICAL_RESOURCE_INTERFACE
									.contains(attr.getProductAttributeMaster().getName())) {
						quoteProductComponentsAttributeValueRepository.delete(attr);
					}
				});
			});
		}
	}

	/**
	 * validateUpdateRequest
	 * 
	 * @param request
	 */
	protected void validateUpdateRequest(List<UpdateRequest> request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	private void constructIllSitePropeties(MstProductComponent mstProductComponent, QuoteIzosdwanSite orderIllSite,
			String username, UpdateRequest request, MstProductFamily mstProductFamily) {
		List<QuoteProductComponent> orderProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent(orderIllSite.getId(), mstProductComponent);
		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
			updateIllSiteProperties(orderProductComponents, request, username);
		} else {
			createIllSiteAttribute(mstProductComponent, mstProductFamily, orderIllSite, request, username);
		}

	}

	private void createIllSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
			QuoteIzosdwanSite orderIllSite, UpdateRequest request, String username) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(mstProductComponent);
		quoteProductComponent.setReferenceId(orderIllSite.getId());
		quoteProductComponent.setReferenceName(QuoteConstants.IZOSDWAN_SITES.toString());
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setType(orderIllSite.getPriSec());
		quoteProductComponentRepository.save(quoteProductComponent);

		if (request.getAttributeDetails() != null) {
			for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
				if (!(attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
						&& attributeDetail.getValue().contains("-"))
						&& !(attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
								&& attributeDetail.getValue().contains("-"))) {
					createSitePropertiesAttribute(quoteProductComponent, attributeDetail, username);
				}

			}

		}
	}

	private void updateIllSiteProperties(List<QuoteProductComponent> orderProductComponents, UpdateRequest request,
			String username) {
		if (orderProductComponents != null) {
			for (QuoteProductComponent orderProductComponent : orderProductComponents) {

				if (request.getAttributeDetails() != null) {
					for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
						if (!(attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
								&& attributeDetail.getValue().contains("-"))
								&& !(attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
										&& attributeDetail.getValue().contains("-"))) {
							List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
									.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
							if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
								upateSitePropertiesAttribute(productAttributeMasters, attributeDetail,
										orderProductComponent);

							} else {

								createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

							}
						}

					}
				}

			}
		}

	}

	private void createSitePropertiesAttribute(QuoteProductComponent orderProductComponent,
			AttributeDetail attributeDetail, String username) {

		ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
		orderProductComponent.setQuoteProductComponentsAttributeValues(
				createAttributes(attributeMaster, orderProductComponent, attributeDetail));

	}

	private void upateSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
			AttributeDetail attributeDetail, QuoteProductComponent orderProductComponent) {

		List<QuoteProductComponentsAttributeValue> orderProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters.get(0));
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			for (QuoteProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
				orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
				orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				quoteProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);

			}
		} else {

			orderProductComponent.setQuoteProductComponentsAttributeValues(
					createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

		}

	}

	private Set<QuoteProductComponentsAttributeValue> createAttributes(ProductAttributeMaster attributeMaster,
			QuoteProductComponent orderProductComponent, AttributeDetail attributeDetail) {

		Set<QuoteProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();

		QuoteProductComponentsAttributeValue orderProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setQuoteProductComponent(orderProductComponent);
		orderProductComponentsAttributeValue.setProductAttributeMaster(attributeMaster);
		quoteProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);

		return orderProductComponentsAttributeValues;

	}

	private ProductAttributeMaster getPropertiesMaster(String name, AttributeDetail attributeDetail) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}

		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setCreatedBy(name);
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;

	}

	/**
	 * getAccountManagersEmail
	 * 
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	public String getAccountManagersEmail(QuoteToLe quoteToLe) throws TclCommonException {
		return getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL.toString());
	}

	public String getLeAttributes(QuoteToLe quoteTole, String attr) throws TclCommonException {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute(quoteTole, mstOmsAttribute);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
		}
		return attrValue;
	}

	/**
	 * checkQuoteLeFeasibility - this method checks the pricing and feasibility for
	 * the given quote legal entity id.
	 * 
	 * @author NAVEEN GUNASEKARAN checkQuoteLeFeasibility
	 * @param quoteLeId
	 * @return QuoteLeAttributeBean
	 */
	public QuoteLeAttributeBean checkQuoteLeFeasibility(Integer quoteLeId) {
		QuoteLeAttributeBean quoteLeAttributeBean = new QuoteLeAttributeBean();
		Optional<QuoteToLe> optQuoteToLe = quoteToLeRepository.findById(quoteLeId);
		if (optQuoteToLe.isPresent()) {
			quoteLeAttributeBean.setQuoteLegalEntityId(quoteLeId);
			for (QuoteLeAttributeValue quoteLeAttribte : optQuoteToLe.get().getQuoteLeAttributeValues()) {

				if (quoteLeAttribte.getMstOmsAttribute().getName()
						.equalsIgnoreCase(QuoteConstants.ISFEASIBLITYCHECKDONE.toString())) {
					quoteLeAttributeBean.setIsFeasibilityCheckDone(quoteLeAttribte.getAttributeValue());
				}

				else if (quoteLeAttribte.getMstOmsAttribute().getName()
						.equalsIgnoreCase(QuoteConstants.ISPRICINGCHECKDONE.toString())) {
					quoteLeAttributeBean.setIsPricingCheckDone(quoteLeAttribte.getAttributeValue());
				}
			}
		}
		return quoteLeAttributeBean;
	}

	public QuoteProductComponent createQuoteProductComponentIfNotPresent(Integer siteId, String type,
			String componentName, User user, String referenceName) {
		String productName = IzosdwanCommonConstants.IZOSDWAN_NAME;
		if (referenceName.equals(IzosdwanCommonConstants.IZOSDWAN_VPROXY)) {
			productName = IzosdwanCommonConstants.VPROXY;
		} else if (referenceName.equals(IzosdwanCommonConstants.IZOSDWAN_VUTM)) {
			productName = IzosdwanCommonConstants.VUTM;
		}
		QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(siteId, componentName,
						productName);
		if (quoteProductComponent == null) {
			quoteProductComponent = new QuoteProductComponent();
			quoteProductComponent.setMstProductComponent(getMstPropertiesByName(componentName));
			quoteProductComponent.setReferenceId(siteId);
			quoteProductComponent.setReferenceName(referenceName);
			quoteProductComponent.setType(type);
			quoteProductComponent.setMstProductFamily(
					mstProductFamilyRepository.findByNameAndStatus(productName, CommonConstants.BACTIVE));
			quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
		}
		Map<String, List<String>> componentSubComponentMap = IzosdwanUtils.getComponentsAndSubComponentsMap();
		if (componentSubComponentMap.containsKey(componentName)) {
			List<String> subComponentsList = componentSubComponentMap
					.get(quoteProductComponent.getMstProductComponent().getName());
			if (subComponentsList != null && !subComponentsList.isEmpty()) {
//				if(componentName.equals(FPConstants.CPE.toString())) {
//					String cpeSaleType = getAttributeValue(siteId, IzosdwanCommonConstants.SITE_PROPERTIES, "CPE");
//					if(cpeSaleType!=null && cpeSaleType.toLowerCase().contains("rental")) {
//						subComponentsList.remove("CPE Hardware - Outright");
//					}if(cpeSaleType!=null && cpeSaleType.toLowerCase().contains("outright")) {
//						subComponentsList.remove("CPE Hardware - Rental");
//					}
//				}
				LOGGER.info("Got subcomponent list!!");
				for (String subComponentName : subComponentsList) {
					LOGGER.info("Got subcomponent name :: {}", subComponentName);
					ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(
							subComponentName, user);
					createOrReturnExistingQuoteProductCompAttrValue(quoteProductComponent, productAttributeMaster);
				}
			}
		}
		return quoteProductComponent;
	}

	private MstProductComponent getMstPropertiesByName(String name) {

		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameAndStatus(name,
				(byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy("admin");
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(name);
			mstProductComponent.setDescription(name);
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;

	}

	private QuoteProductComponentsAttributeValue createOrReturnExistingQuoteProductCompAttrValue(
			QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster) {
		QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
						productAttributeMaster.getName())
				.stream().findFirst().orElse(null);
		if (qpcav == null) {
			qpcav = new QuoteProductComponentsAttributeValue();
			qpcav.setAttributeValues(CommonConstants.EMPTY);
			qpcav.setDisplayValue(CommonConstants.EMPTY);
			qpcav.setProductAttributeMaster(productAttributeMaster);
			qpcav.setQuoteProductComponent(quoteProductComponent);
			qpcav = quoteProductComponentsAttributeValueRepository.save(qpcav);
		}
		return qpcav;
	}

	private ProductAttributeMaster createOrReturnExistingProdAttMaster(String name, User user) {
		ProductAttributeMaster productAttributeMaster = productAttributeMasterRepository.findByName(name);
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setCategory(CommonConstants.NEW);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(name);
			productAttributeMaster.setName(name);
			productAttributeMaster.setStatus(CommonConstants.BACTIVE);
			productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;
	}

	public Set<LegalAttributeBean> getAllAttributesByQuoteToLeId(Integer quoteToLeId) throws TclCommonException {
		Set<LegalAttributeBean> legalEntityAttributes = null;
		try {
			if (Objects.isNull(quoteToLeId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<QuoteToLe> optQuoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (optQuoteToLe.isPresent()) {
				legalEntityAttributes = constructLegalAttributes(optQuoteToLe.get());
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return legalEntityAttributes;
	}

	public QuoteDetail persistListOfQuoteLeAttributes(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			LOGGER.info("Input Received {}", request);
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			List<AttributeDetail> attributeDetails = request.getAttributeDetails();
			for (AttributeDetail attribute : attributeDetails) {
				if (attribute.getName() != null) {
					LOGGER.info("Attribute Name {} ", attribute.getName());
					MstOmsAttribute mstOmsAttribute = null;
					List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
							.findByNameAndIsActive(attribute.getName(), (byte) 1);
					if (!mstOmsAttributeList.isEmpty()) {
						mstOmsAttribute = mstOmsAttributeList.get(0);
						LOGGER.info("Mst already there with id  {} ", mstOmsAttribute.getId());
					}
					if (mstOmsAttribute == null) {
						mstOmsAttribute = new MstOmsAttribute();
						mstOmsAttribute.setCreatedBy(user.getUsername());
						mstOmsAttribute.setCreatedTime(new Date());
						mstOmsAttribute.setIsActive((byte) 1);
						mstOmsAttribute.setName(attribute.getName());
						mstOmsAttribute.setDescription("");
						mstOmsAttributeRepository.save(mstOmsAttribute);
						LOGGER.info("Mst OMS Saved with id  {} ", mstOmsAttribute.getId());
					}

					saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute, mstOmsAttribute);
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	private void saveLegalEntityAttributes(QuoteToLe quoteToLe, AttributeDetail attribute,
			MstOmsAttribute mstOmsAttribute) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				LOGGER.info("Inside quote to le update");
				attrVal.setMstOmsAttribute(mstOmsAttribute);
				attrVal.setAttributeValue(attribute.getValue());
				attrVal.setDisplayValue(attribute.getName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValueRepository.save(attrVal);
			});
		} else {
			LOGGER.info("Inside quote to create");
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setAttributeValue(attribute.getValue());
			quoteLeAttributeValue.setDisplayValue(attribute.getName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		}
	}

	/**
	 * validateUpdateRequest
	 *
	 * @param request
	 */
	protected void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	public CreateDocumentDto createDocument(CreateDocumentDto documentDto) throws TclCommonException {
		CreateDocumentDto response = new CreateDocumentDto();
		Integer oldCustomerLegalEntityId = null;
		try {
			validateDocumentRequest(documentDto);
			Quote quote = quoteRepository.findByIdAndStatus(documentDto.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			if (documentDto.getIllSitesIds() != null && !documentDto.getIllSitesIds().isEmpty()) {
				List<QuoteIzosdwanSite> illSites = quoteIzosdwanSiteRepository
						.findByIdInAndStatus(documentDto.getIllSitesIds(), (byte) 1);
				illSites.forEach(illsite -> {
					illsite.setIsTaxExempted((byte) 1);
					quoteIzosdwanSiteRepository.save(illsite);
				});

			}

			Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(documentDto.getQuoteLeId());
			if (!optionalQuoteLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);

			}

			CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
			customerLeAttributeRequestBean.setCustomerLeId(documentDto.getCustomerLegalEntityId());
			customerLeAttributeRequestBean.setProductName("IAS");
			LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
					Utils.convertObjectToJson(customerLeAttributeRequestBean));

			if (StringUtils.isNotEmpty(customerLeAttributes)) {
				updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
						CustomerLeDetailsBean.class), optionalQuoteLe.get());
			}

			// String spName =
			// returnServiceProviderName(documentDto.getSupplierLegalEntityId());
			ServiceProviderLegalBean spName = returnServiceProviderNameIzosdwan(documentDto.getSupplierLegalEntityId());
			/*
			 * if (StringUtils.isNotEmpty(spName)) { processAccount(optionalQuoteLe.get(),
			 * spName, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString()); }
			 */

			if (spName != null) {
				processAccount(optionalQuoteLe.get(), spName.getEntityName(),
						LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
				processAccount(optionalQuoteLe.get(), spName.getContractingAddressId(),
						IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS);
			}

			QuoteToLe quoteToLe = optionalQuoteLe.get();
			oldCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
			quoteToLe.setErfCusCustomerLegalEntityId(documentDto.getCustomerLegalEntityId());
			quoteToLe.setErfCusSpLegalEntityId(documentDto.getSupplierLegalEntityId());
			if (quoteToLe.getStage().equals(QuoteStageConstants.CHECKOUT.getConstantCode())) {
				quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			}
			quoteToLeRepository.save(quoteToLe);

			CustomerDetail customerDetail = null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				customerDetail = new CustomerDetail();
				Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(documentDto.getCustomerId(),
						(byte) 1);
				customerDetail.setCustomerId(customer.getId());
			} else {
				customerDetail = userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			}
			// CustomerDetail customerDetail =
			// userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			if (customerDetail != null && !customerDetail.getCustomerId().equals(quote.getCustomer().getId())) {
				Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
				if (customerEntity.isPresent()) {
					quote.setCustomer(customerEntity.get());
					quoteRepository.save(quote);
				}
			}
			processLocationDetailsAndSendToQueue(quoteToLe, quote.getCustomer().getErfCusCustomerId());

			// Credit Check - Start
			LOGGER.info("Before triggering credit check");
			if (Objects.isNull(optionalQuoteLe.get().getQuoteType())
					|| (Objects.nonNull(optionalQuoteLe.get().getQuoteType())
							&& optionalQuoteLe.get().getQuoteType().equals(CommonConstants.NEW))) {
				CustomerLeDetailsBean lePreapprovedValuesBean = (CustomerLeDetailsBean) Utils
						.convertJsonToObject(customerLeAttributes, CustomerLeDetailsBean.class);
//				if(Objects.nonNull(lePreapprovedValuesBean.getCreditCheckAccountType()) && 
//						(lePreapprovedValuesBean.getCreditCheckAccountType().equalsIgnoreCase(CreditCheckConstants.CC_ACCOUNT_TYPE_3A) || lePreapprovedValuesBean.getCreditCheckAccountType().equalsIgnoreCase(CreditCheckConstants.CC_ACCOUNT_TYPE_3B))) {
				processAccount(optionalQuoteLe.get(), lePreapprovedValuesBean.getCreditCheckAccountType(),
						LeAttributesConstants.CREDIT_CHECK_ACCOUNT_TYPE.toString());
				creditCheckService.triggerCreditCheck(documentDto.getCustomerLegalEntityId(), optionalQuoteLe,
						lePreapprovedValuesBean, oldCustomerLegalEntityId);
				response.setCreditCheckStatus(optionalQuoteLe.get().getTpsSfdcStatusCreditControl());
				response.setPreapprovedFlag(
						CommonConstants.BACTIVE.equals(optionalQuoteLe.get().getPreapprovedOpportunityFlag()) ? true
								: false);
//				} else {
//					response.setCreditCheckStatus("NA");
//				}
			}
			LOGGER.info("After triggering credit check");
			// Credit Check - End

			omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
					SFDCConstants.VERBAL_AGREEMENT_STAGE.toString(), quoteToLe);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return response;
	}

	/**
	 * processAccountCuid used to process account details from customer mdm
	 * 
	 * @param quoteToLe
	 * @param request
	 * @param user
	 */
	private void processAccount(QuoteToLe quoteToLe, String attrValue, String attributeName) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attributeName);

		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			if (quoteToLe.getQuote().getNsQuote() == null || quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("N")) {
				quoteLeAttributeValues.forEach(attr -> {
					attr.setAttributeValue(attrValue);
					quoteLeAttributeValueRepository.save(attr);

				});
			}

		} else {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(attrValue);
			attributeValue.setDisplayValue(attributeName);
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);

		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getMstAttributeMasterForBilling
	 *            used to get the attribute master
	 * @param request
	 * @return
	 */
	private MstOmsAttribute getMstAttributeMasterForBilling(String attrName) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName, (byte) 1);
		if (mstOmsAttributes != null && !mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(Utils.getSource());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(attrName);
			mstOmsAttribute.setDescription(attrName);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @param familyName
	 * @link http://www.tatacommunications.com/ processLocationDetailsAndSendToQueue
	 *       used to send location info
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	private void processLocationDetailsAndSendToQueue(QuoteToLe quoteToLe, Integer erfCustomerId) {
		try {
			CustomerLeLocationBean bean = constructCustomerLeAndLocation(quoteToLe, erfCustomerId);
			String request = Utils.convertObjectToJson(bean);
			LOGGER.info("Customer id to be send {} , request {}", erfCustomerId, request);
			LOGGER.info("MDC Filter token value in before Queue call processLocationDetailsAndSendToQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(customerLeLocationQueue, request);
		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param familyName
	 * @link http://www.tatacommunications.com/ processLocationDetailsAndSendToQueue
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	public CustomerLeLocationBean constructCustomerLeAndLocation(QuoteToLe quoteToLe, Integer erfCustomerId) {
		CustomerLeLocationBean customerLeLocationBean = new CustomerLeLocationBean();

		try {
			customerLeLocationBean.setErfCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
			customerLeLocationBean.setCustomerId(erfCustomerId);
			quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteProdFamilies -> {
				quoteProdFamilies.getProductSolutions().stream().forEach(prodSolutions -> {
					prodSolutions.getQuoteIllSites().stream().forEach(illSite -> {
						customerLeLocationBean.getLocationIds().add(illSite.getErfLocSitebLocationId());
					});
				});
			});
		} catch (Exception e) {
			// since it is and internal queue call so we are logging it only
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}

		return customerLeLocationBean;

	}

	public String returnServiceProviderName(Integer id) throws TclCommonException {
		try {
			LOGGER.info("MDC Filter token value in before Queue call returnServiceProviderName {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			return (String) mqUtils.sendAndReceive(spQueue,
					Utils.convertObjectToJson(constructSupplierDetailsRequestBean(id)));
		} catch (Exception e) {
			throw new TclCommonException("No Service Provider Name");
		}
	}

	public ServiceProviderLegalBean returnServiceProviderNameIzosdwan(Integer id) throws TclCommonException {
		try {
			LOGGER.info("MDC Filter token value in before Queue call returnServiceProviderNameIzosdwan {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(spQueueIzosdwan,
					Utils.convertObjectToJson(constructSupplierDetailsRequestBean(id)));
			if (StringUtils.isNotBlank(response)) {
				return Utils.convertJsonToObject(response, ServiceProviderLegalBean.class);
			}
		} catch (Exception e) {
			throw new TclCommonException("No Service Provider Name");
		}
		return null;
	}

	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}");
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
	}

	/**
	 * validateDocumentRequest- This method is used to validate the Document request
	 * 
	 * @param documentDto
	 */
	private void validateDocumentRequest(CreateDocumentDto documentDto) throws TclCommonException {

		if ((documentDto == null) || (documentDto.getQuoteId() == 0) || (documentDto.getCustomerLegalEntityId() == 0)
				|| (documentDto.getSupplierLegalEntityId() == 0)) {
			throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * 
	 * update Terms In Months For QuoteToLe
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param updateRequest
	 * @throws TclCommonException
	 */
	public void updateTermsInMonthsForQuoteToLe(Integer quoteId, Integer quoteToLeId, UpdateRequest updateRequest,
			Integer taskId) throws TclCommonException {
		if (quoteId == null || quoteToLeId == null || updateRequest == null
				|| updateRequest.getTermInMonths() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Optional<Quote> quote = quoteRepository.findById(quoteId);
		if (quote.isPresent()) {
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuoteAndId(quote.get(), quoteToLeId);
			boolean hasTermsInMonthsChanged = false;
			if (quoteToLe != null && quoteToLe.getId() != null) {
				LOGGER.info("Input term received {}", updateRequest.getTermInMonths());
				String terms = (updateRequest.getTermInMonths().substring(0, 2)).concat(" months");
				LOGGER.info("Term to update {}", terms);
				if (!quoteToLe.getTermInMonths().equalsIgnoreCase(terms)) {
					hasTermsInMonthsChanged = true;
				}
				quoteToLe.setTermInMonths(terms);
				quoteToLeRepository.save(quoteToLe);
			}
			Boolean isByonOnly = false;
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P, quote.get().getId());
			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
					&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue() != null
					&& "true".equalsIgnoreCase(quoteIzoSdwanAttributeValues.get(0).getAttributeValue())) {
				isByonOnly = true;
			}
			if (quote.get().getNsQuote() != null && quote.get().getNsQuote().equalsIgnoreCase("Y")) {
				if (hasTermsInMonthsChanged) {
					izosdwanPricingAndFeasibilityService.putEntryInPricingBatch(quoteId, quoteToLeId, isByonOnly, false,
							true, CommonConstants.CUSTOM);
				}
			} else {
				izosdwanPricingAndFeasibilityService.putEntryInPricingBatch(quoteId, quoteToLeId, isByonOnly, false,
						true, CommonConstants.STANDARD);
			}

			/*
			 * if (taskId != null) { // Updating in service fulfilment schema
			 * SiteDetailServiceFulfilmentUpdateBean siteDetailServiceFulfilmentUpdateBean =
			 * new SiteDetailServiceFulfilmentUpdateBean();
			 * siteDetailServiceFulfilmentUpdateBean.setTaskId(taskId);
			 * siteDetailServiceFulfilmentUpdateBean.setTermsInMonths(updateRequest.
			 * getTermInMonths()); mqUtils.send(updateTermsInMonthsSiteDetailQueue,
			 * Utils.convertObjectToJson(siteDetailServiceFulfilmentUpdateBean)); }
			 */
		}
	}

	/**
	 * 
	 * Get the price information of the quote
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public QuotePricingDetailsBean getPriceInformationForTheQuote(Integer quoteId) throws TclCommonException {
		QuotePricingDetailsBean quotePricingDetailsBean = new QuotePricingDetailsBean();
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			if (quote != null) {
				String terms;
				String currency = "INR";
				List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
				if (quoteToLes.stream().findFirst().isPresent()) {
					QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get();
					terms = quoteToLe.getTermInMonths();
					currency = quoteToLe.getCurrencyCode();
					Integer termsInMonths = Integer.parseInt(terms.substring(0, 2));
					// As of now getting price only for SDWAN solution
					ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
					if (productSolution != null) {
						SolutionPricingDetailsBean solutionPricingDetailsBean = new SolutionPricingDetailsBean();
						List<ProductPricingDetailsBean> productPricingDetailsBeans = new ArrayList<>();
						List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
								.findByProductSolution(productSolution);
						if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
							getProductLevelPricingDetails(CommonConstants.IAS, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths);
							getProductLevelPricingDetails(CommonConstants.GVPN, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths);
							getProductLevelPricingDetails(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT,
									quoteIzosdwanSites, productPricingDetailsBeans, termsInMonths);
							getIzosdwanPricingDetails(quoteIzosdwanSites, productPricingDetailsBeans, quote,
									termsInMonths);
							// getCgwPricingDetails(quote, quoteToLe, productPricingDetailsBeans);
							// mockPriceForSdwan(productPricingDetailsBeans);
						}
						// Get Mock Price For Vproxy
						List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanValues = quoteIzoSdwanAttributeValuesRepository
								.findByQuote(quote);
						if (checkVproxyExistOrNot(quoteIzoSdwanValues)) {
							getVproxyPricingDetails(productPricingDetailsBeans, quoteId, termsInMonths);

						}
						solutionPricingDetailsBean.setArc(new BigDecimal(0D));
						solutionPricingDetailsBean.setMrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setNrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setTcv(new BigDecimal(0D));
						solutionPricingDetailsBean.setTcvMrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setProductPricingDetailsBeans(productPricingDetailsBeans);
						calculateSolutionLevelPrice(solutionPricingDetailsBean);
						quotePricingDetailsBean.setIzosdwan(solutionPricingDetailsBean);
					}
					quotePricingDetailsBean.setArc(new BigDecimal(0D));
					quotePricingDetailsBean.setCurrency(currency);
					quotePricingDetailsBean.setMrc(new BigDecimal(0D));
					quotePricingDetailsBean.setNrc(new BigDecimal(0D));
					quotePricingDetailsBean.setTcv(new BigDecimal(0D));
					quotePricingDetailsBean.setTcvMrc(new BigDecimal(0D));
					quotePricingDetailsBean.setTermsInMonths(terms);
					constructQuoteLevelPrice(quotePricingDetailsBean.getIzosdwan(), quotePricingDetailsBean);
					// bundleOmsSfdcService.processUpdateProduct(quoteToLe,
					// quotePricingDetailsBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured on deriving the price {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quotePricingDetailsBean;
	}

	private void getVproxyPricingDetails(List<ProductPricingDetailsBean> productPricingDetailsBeans, Integer quoteId,
			Integer termsInMonths) {
		ProductPricingDetailsBean vproxyBean = new ProductPricingDetailsBean();
		vproxyBean.setProductName(IzosdwanCommonConstants.VPROXY);
		ArcDetailsBean arcDetailsBean = new ArcDetailsBean();
		MrcDetailsBean mrcDetailsBean = new MrcDetailsBean();
		NrcDetailsBean nrcDetailsBean = new NrcDetailsBean();
		arcDetailsBean.setArcTcv(new BigDecimal(0D));
		mrcDetailsBean.setMrcTcv(new BigDecimal(0D));
		nrcDetailsBean.setNrcTcv(new BigDecimal(0D));
		Map<String, Double> arcComponents = new HashMap<>();
		Map<String, Double> mrcComponents = new HashMap<>();
		Map<String, Double> nrcComponents = new HashMap<>();
		List<ComponentDetailsBean> componentDetailsBeansArc = new ArrayList<>();
		List<ComponentDetailsBean> componentDetailsBeansMrc = new ArrayList<>();
		List<ComponentDetailsBean> componentDetailsBeansNrc = new ArrayList<>();
		arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
		mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
		nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
		List<ProductSolution> productSolutions = productSolutionRepository.findByReferenceIdForVproxy(quoteId);
		if (productSolutions != null && !productSolutions.isEmpty()) {
			productSolutions.stream().forEach(solution -> {
				List<QuoteProductComponent> quoteProductComponents = getComponentBasenVproxy(solution.getId(), false,
						false);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					quoteProductComponents.stream().forEach(qProComp -> {
						QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
						if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
							Double arc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveArc()));
							if (arcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								arcComponents.put(qProComp.getMstProductComponent().getName(),
										arcComponents.get(qProComp.getMstProductComponent().getName()) + arc);
							} else {
								arcComponents.put(qProComp.getMstProductComponent().getName(), arc);
							}
						}
						if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
							Double mrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveMrc()));
							if (mrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								mrcComponents.put(qProComp.getMstProductComponent().getName(),
										mrcComponents.get(qProComp.getMstProductComponent().getName()) + mrc);
							} else {
								mrcComponents.put(qProComp.getMstProductComponent().getName(), mrc);
							}
						}
						if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
							Double nrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveNrc()));
							if (nrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								nrcComponents.put(qProComp.getMstProductComponent().getName(),
										nrcComponents.get(qProComp.getMstProductComponent().getName()) + nrc);
							} else {
								nrcComponents.put(qProComp.getMstProductComponent().getName(), nrc);
							}
						}

					});
				}
			});

			List<QuoteProductComponent> quoteProductComponents = getComponentBasenVproxy(
					productSolutions.get(0).getQuoteToLeProductFamily().getId(), false, false);
			if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
				quoteProductComponents.stream().forEach(qProComp -> {
					String componentName = qProComp.getMstProductComponent().getName();
					if (qProComp.getMstProductComponent().getName().equals(IzosdwanCommonConstants.VPROXY_COMMON)) {
						componentName = IzosdwanCommonConstants.SUPPORT_CHARGES;
					}
					QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
					if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
						Double arc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveArc()));
						if (arcComponents.containsKey(componentName)) {
							arcComponents.put(componentName, arcComponents.get(componentName) + arc);
						} else {
							arcComponents.put(componentName, arc);
						}
					}
					if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
						Double mrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveMrc()));
						if (mrcComponents.containsKey(componentName)) {
							mrcComponents.put(componentName, mrcComponents.get(componentName) + mrc);
						} else {
							mrcComponents.put(componentName, mrc);
						}
					}
					if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
						Double nrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveNrc()));
						if (nrcComponents.containsKey(componentName)) {
							nrcComponents.put(componentName, nrcComponents.get(componentName) + nrc);
						} else {
							nrcComponents.put(componentName, nrc);
						}
					}

				});
			}

			if (arcComponents != null && !arcComponents.isEmpty()) {
				arcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					arcDetailsBean.setArcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(arcDetailsBean.getArcTcv())));
					componentDetailsBeansArc.add(componentDetailsBean);
				});
				arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			}
			if (mrcComponents != null && !mrcComponents.isEmpty()) {
				mrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					mrcDetailsBean.setMrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(mrcDetailsBean.getMrcTcv())));
					componentDetailsBeansMrc.add(componentDetailsBean);
				});
				mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			}
			if (nrcComponents != null && !nrcComponents.isEmpty()) {
				nrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					nrcDetailsBean.setNrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(nrcDetailsBean.getNrcTcv())));
					componentDetailsBeansNrc.add(componentDetailsBean);
				});
				nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			}
		}

		vproxyBean.setArcDetailsBean(arcDetailsBean);
		vproxyBean.setMrcDetailsBean(mrcDetailsBean);
		vproxyBean.setNrcDetailsBean(nrcDetailsBean);
		vproxyBean.setTcv(
				IzosdwanUtils.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonths / 12)))
						.add(nrcDetailsBean.getNrcTcv())));
		vproxyBean.setTcvMrc(
				IzosdwanUtils.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonths / 12)))
						.add(nrcDetailsBean.getNrcTcv())));
		productPricingDetailsBeans.add(vproxyBean);
	}

	private void getProductLevelPricingDetails(String productName, List<QuoteIzosdwanSite> quoteIzosdwanSites,
			List<ProductPricingDetailsBean> productPricingDetailsBeans, Integer termsInMonth) {
		// DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// For IAS sites
		List<QuoteIzosdwanSite> iasSites = quoteIzosdwanSites.stream()
				.filter(site -> site.getIzosdwanSiteProduct().equals(productName)).collect(Collectors.toList());
		if (iasSites != null && !iasSites.isEmpty()) {
			ProductPricingDetailsBean productPricingDetailsBean = new ProductPricingDetailsBean();
			ArcDetailsBean arcDetailsBean = new ArcDetailsBean();
			NrcDetailsBean nrcDetailsBean = new NrcDetailsBean();
			MrcDetailsBean mrcDetailsBean = new MrcDetailsBean();
			arcDetailsBean.setArcTcv(new BigDecimal(0D));
			nrcDetailsBean.setNrcTcv(new BigDecimal(0D));
			mrcDetailsBean.setMrcTcv(new BigDecimal(0D));
			productPricingDetailsBean.setProductName(productName);
			Map<String, Double> arcComponents = new HashMap<>();
			Map<String, Double> nrcComponents = new HashMap<>();
			Map<String, Double> mrcComponents = new HashMap<>();

			List<ComponentDetailsBean> componentDetailsBeansArc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansNrc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansMrc = new ArrayList<>();

			arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			iasSites.stream().forEach(sites -> {
				List<QuoteProductComponent> quoteProductComponents = getComponentBasenOnVersion(sites.getId(), false,
						false);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					quoteProductComponents.stream().forEach(qProComp -> {
						LOGGER.info("Component name {} for product {} before",
								qProComp.getMstProductComponent().getName(), productName);
						if (!(qProComp.getMstProductComponent().getName().equalsIgnoreCase(FPConstants.CPE.toString())
								|| qProComp.getMstProductComponent().getName()
										.equalsIgnoreCase(FPConstants.LICENSE_COST.toString()))) {
							LOGGER.info("Component name {} for product {}", qProComp.getMstProductComponent().getName(),
									productName);
							QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
							if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
								Double arc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveArc()));
								if (arcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									arcComponents.put(qProComp.getMstProductComponent().getName(),
											arcComponents.get(qProComp.getMstProductComponent().getName()) + arc);
								} else {
									arcComponents.put(qProComp.getMstProductComponent().getName(), arc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
								Double nrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveNrc()));
								if (nrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									nrcComponents.put(qProComp.getMstProductComponent().getName(),
											nrcComponents.get(qProComp.getMstProductComponent().getName()) + nrc);
								} else {
									nrcComponents.put(qProComp.getMstProductComponent().getName(), nrc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
								Double mrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveMrc()));
								if (mrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									mrcComponents.put(qProComp.getMstProductComponent().getName(),
											mrcComponents.get(qProComp.getMstProductComponent().getName()) + mrc);
								} else {
									mrcComponents.put(qProComp.getMstProductComponent().getName(), mrc);
								}
							}

						}

					});
				}
			});
			if (arcComponents != null && !arcComponents.isEmpty()) {
				arcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					arcDetailsBean.setArcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(arcDetailsBean.getArcTcv())));
					componentDetailsBeansArc.add(componentDetailsBean);
				});
				arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			}
			if (mrcComponents != null && !mrcComponents.isEmpty()) {
				mrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					mrcDetailsBean.setMrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(mrcDetailsBean.getMrcTcv())));
					componentDetailsBeansMrc.add(componentDetailsBean);
				});
				mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			}
			if (nrcComponents != null && !nrcComponents.isEmpty()) {
				nrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					nrcDetailsBean.setNrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(nrcDetailsBean.getNrcTcv())));
					componentDetailsBeansNrc.add(componentDetailsBean);
				});
				nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			}
			productPricingDetailsBean.setArcDetailsBean(arcDetailsBean);
			productPricingDetailsBean.setMrcDetailsBean(mrcDetailsBean);
			productPricingDetailsBean.setNrcDetailsBean(nrcDetailsBean);
			productPricingDetailsBean.setTcv(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonth / 12)))
							.add(nrcDetailsBean.getNrcTcv()))); //confirm
			productPricingDetailsBean.setTcvMrc(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonth / 12)))
							.add(nrcDetailsBean.getNrcTcv())));
			productPricingDetailsBeans.add(productPricingDetailsBean);
			// mockPriceForSdwan(productPricingDetailsBeans);
		}
	}

	
	private void getProductLevelPricingDetailsSfdcSdwan(String productName, List<QuoteIzosdwanSite> quoteIzosdwanSites,
			List<ProductPricingDetailsBean> productPricingDetailsBeans, Integer termsInMonth,Boolean isNew) {
		// DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// For IAS sites
		List<QuoteIzosdwanSite> iasSites = null;
		if(isNew) {
			iasSites = quoteIzosdwanSites.stream().filter(site -> site.getIzosdwanSiteProduct().equals(productName) && site.getErfServiceInventoryTpsServiceId()==null).collect(Collectors.toList());
		}else {
			iasSites = quoteIzosdwanSites.stream().filter(site -> site.getIzosdwanSiteProduct().equals(productName) && site.getErfServiceInventoryTpsServiceId()!=null).collect(Collectors.toList());
		}
		
		if (iasSites != null && !iasSites.isEmpty()) {
			ProductPricingDetailsBean productPricingDetailsBean = new ProductPricingDetailsBean();
			ArcDetailsBean arcDetailsBean = new ArcDetailsBean();
			NrcDetailsBean nrcDetailsBean = new NrcDetailsBean();
			MrcDetailsBean mrcDetailsBean = new MrcDetailsBean();
			arcDetailsBean.setArcTcv(new BigDecimal(0D));
			nrcDetailsBean.setNrcTcv(new BigDecimal(0D));
			mrcDetailsBean.setMrcTcv(new BigDecimal(0D));
			productPricingDetailsBean.setProductName(productName);
			if(isNew) {
				LOGGER.info("OrderType : NEW");
				productPricingDetailsBean.setOrderType(MACDConstants.NEW);
			}else {
				LOGGER.info("OrderType : MACD");
				productPricingDetailsBean.setOrderType(MACDConstants.MACD);
			}
			Map<String, Double> arcComponents = new HashMap<>();
			Map<String, Double> nrcComponents = new HashMap<>();
			Map<String, Double> mrcComponents = new HashMap<>();
			
			List<ComponentDetailsBean> componentDetailsBeansArc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansNrc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansMrc = new ArrayList<>();

			arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			productPricingDetailsBean.setPreviousMrcTotal(BigDecimal.ZERO);
			productPricingDetailsBean.setPreviousArcTotal(BigDecimal.ZERO);
			productPricingDetailsBean.setPreviousNrcTotal(BigDecimal.ZERO);
			iasSites.stream().forEach(sites -> {
				
				BigDecimal previousMrcTotal = productPricingDetailsBean.getPreviousMrcTotal();
				BigDecimal previousArcTotal = productPricingDetailsBean.getPreviousArcTotal();
				BigDecimal previousNrcTotal = productPricingDetailsBean.getPreviousNrcTotal();
				String regex = "[0-9.]+"; 
		        Pattern pattern = Pattern.compile(regex); 
		        Matcher matches; 
				if (!isNew) {
					LOGGER.info("Started MRC ARC NRC calculation {}  ", isNew);
					String prvMrc = getAttributeValue(sites.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, "previousMrc");
					matches = pattern.matcher(prvMrc);
					if (prvMrc != null && matches.matches()) {
						
						previousMrcTotal = previousMrcTotal.add(new BigDecimal(prvMrc));
						productPricingDetailsBean.setPreviousMrcTotal(previousMrcTotal);
						LOGGER.info("MRC is {}  ", previousMrcTotal);
					}

					String prvArc = getAttributeValue(sites.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, "previousArc");
					matches = pattern.matcher(prvArc);
					if (prvMrc != null && matches.matches()) {
						previousArcTotal = previousMrcTotal.add(new BigDecimal(prvArc));
						productPricingDetailsBean.setPreviousArcTotal(previousArcTotal);
						LOGGER.info("ARC is {}  ", previousArcTotal);
					} 

					String prvNrc = getAttributeValue(sites.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, "previousNrc");
					matches = pattern.matcher(prvNrc);
					if (prvNrc != null && matches.matches()) {
						previousNrcTotal = previousMrcTotal.add(new BigDecimal(prvNrc));
						productPricingDetailsBean.setPreviousNrcTotal(previousNrcTotal);
						LOGGER.info("NRC is {}  ", previousNrcTotal);
					} 
				}
				
				List<QuoteProductComponent> quoteProductComponents = getComponentBasenOnVersion(sites.getId(), false,
						false);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					quoteProductComponents.stream().forEach(qProComp -> {
						LOGGER.info("Component name {} for product {} before",
								qProComp.getMstProductComponent().getName(), productName);
						if (!(qProComp.getMstProductComponent().getName().equalsIgnoreCase(FPConstants.CPE.toString())
								|| qProComp.getMstProductComponent().getName()
										.equalsIgnoreCase(FPConstants.LICENSE_COST.toString()))) {
							LOGGER.info("Component name {} for product {}", qProComp.getMstProductComponent().getName(),
									productName);
							QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
							if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
								Double arc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveArc()));
								if (arcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									arcComponents.put(qProComp.getMstProductComponent().getName(),
											arcComponents.get(qProComp.getMstProductComponent().getName()) + arc);
								} else {
									arcComponents.put(qProComp.getMstProductComponent().getName(), arc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
								Double nrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveNrc()));
								if (nrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									nrcComponents.put(qProComp.getMstProductComponent().getName(),
											nrcComponents.get(qProComp.getMstProductComponent().getName()) + nrc);
								} else {
									nrcComponents.put(qProComp.getMstProductComponent().getName(), nrc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
								Double mrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveMrc()));
								if (mrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									mrcComponents.put(qProComp.getMstProductComponent().getName(),
											mrcComponents.get(qProComp.getMstProductComponent().getName()) + mrc);
								} else {
									mrcComponents.put(qProComp.getMstProductComponent().getName(), mrc);
								}
							}

						}

					});
				}
			});
			LOGGER.info("Total  Mrc {} ARC {} NRC {}",productPricingDetailsBean.getPreviousMrcTotal(),productPricingDetailsBean.getPreviousArcTotal(),
					productPricingDetailsBean.getPreviousNrcTotal());
			if (arcComponents != null && !arcComponents.isEmpty()) {
				arcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					arcDetailsBean.setArcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(arcDetailsBean.getArcTcv())));
					componentDetailsBeansArc.add(componentDetailsBean);
				});
				arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			}
			if (mrcComponents != null && !mrcComponents.isEmpty()) {
				mrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					mrcDetailsBean.setMrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(mrcDetailsBean.getMrcTcv())));
					componentDetailsBeansMrc.add(componentDetailsBean);
				});
				mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			}
			if (nrcComponents != null && !nrcComponents.isEmpty()) {
				nrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					nrcDetailsBean.setNrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(nrcDetailsBean.getNrcTcv())));
					componentDetailsBeansNrc.add(componentDetailsBean);
				});
				nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			}
			productPricingDetailsBean.setArcDetailsBean(arcDetailsBean);
			productPricingDetailsBean.setMrcDetailsBean(mrcDetailsBean);
			productPricingDetailsBean.setNrcDetailsBean(nrcDetailsBean);
			productPricingDetailsBean.setTcv(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonth / 12)))
							.add(nrcDetailsBean.getNrcTcv()))); //confirm
			productPricingDetailsBean.setTcvMrc(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonth / 12)))
							.add(nrcDetailsBean.getNrcTcv())));
			productPricingDetailsBeans.add(productPricingDetailsBean);
			// mockPriceForSdwan(productPricingDetailsBeans);
		}
	}

	private void getIzosdwanPricingDetails(List<QuoteIzosdwanSite> quoteIzosdwanSites,
			List<ProductPricingDetailsBean> productPricingDetailsBeans, Quote quote, Integer termsInMonths) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// For IAS sites
		LOGGER.info("Inside Izosdwan!!!");
		if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
			ProductPricingDetailsBean productPricingDetailsBean = new ProductPricingDetailsBean();
			ArcDetailsBean arcDetailsBean = new ArcDetailsBean();
			MrcDetailsBean mrcDetailsBean = new MrcDetailsBean();
			NrcDetailsBean nrcDetailsBean = new NrcDetailsBean();

			arcDetailsBean.setArcTcv(new BigDecimal(0D));
			mrcDetailsBean.setMrcTcv(new BigDecimal(0D));
			nrcDetailsBean.setNrcTcv(new BigDecimal(0D));

			productPricingDetailsBean.setProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
			productPricingDetailsBean.setOrderType(MACDConstants.NEW);
			Map<String, Double> arcComponents = new HashMap<>();
			Map<String, Double> nrcComponents = new HashMap<>();
			Map<String, Double> mrcComponents = new HashMap<>();

			List<ComponentDetailsBean> componentDetailsBeansArc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansNrc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansMrc = new ArrayList<>();

			arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			quoteIzosdwanSites.stream().forEach(sites -> {
				List<QuoteProductComponent> quoteProductComponents = getComponentBasenOnVersion(sites.getId(), false,
						false);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					quoteProductComponents.stream().forEach(qProComp -> {
						LOGGER.info("Component name {}  before check", qProComp.getMstProductComponent().getName());
						if (qProComp.getMstProductComponent().getName().equalsIgnoreCase(FPConstants.CPE.toString())
								|| qProComp.getMstProductComponent().getName()
										.equalsIgnoreCase(FPConstants.LICENSE_COST.toString())) {
							LOGGER.info("Component name {}  after check", qProComp.getMstProductComponent().getName());
							QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
							if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
								Double arc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveArc()));
								if (arcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									arcComponents.put(qProComp.getMstProductComponent().getName(),
											arcComponents.get(qProComp.getMstProductComponent().getName()) + arc);
								} else {
									arcComponents.put(qProComp.getMstProductComponent().getName(), arc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
								Double nrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveNrc()));
								if (nrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									nrcComponents.put(qProComp.getMstProductComponent().getName(),
											nrcComponents.get(qProComp.getMstProductComponent().getName()) + nrc);
								} else {
									nrcComponents.put(qProComp.getMstProductComponent().getName(), nrc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
								Double mrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveMrc()));
								if (mrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
									mrcComponents.put(qProComp.getMstProductComponent().getName(),
											mrcComponents.get(qProComp.getMstProductComponent().getName()) + mrc);
								} else {
									mrcComponents.put(qProComp.getMstProductComponent().getName(), mrc);
								}
							}
							
							if (qProComp.getMstProductComponent().getName()
									.equalsIgnoreCase(FPConstants.CPE.toString())) {
								Optional<QuoteProductComponentsAttributeValue> cpeInstalltionAttr = qProComp
										.getQuoteProductComponentsAttributeValues().stream()
										.filter(attr -> attr.getProductAttributeMaster().getName()
												.equalsIgnoreCase(IzosdwanCommonConstants.CPE_INSTALLATION))
										.findFirst();
								if (cpeInstalltionAttr.isPresent()) {
									QuotePriceBean quotePriceBeanAttr = constructAttributePriceDto(
											cpeInstalltionAttr.get());
									if (quotePriceBeanAttr != null && quotePriceBeanAttr.getEffectiveNrc() != null) {
										Double nrc = Double.parseDouble(
												decimalFormat.format(quotePriceBeanAttr.getEffectiveNrc()));
										if (nrcComponents.containsKey(IzosdwanCommonConstants.LICENCE_ATTRIBUTE)) {
											nrcComponents.put(IzosdwanCommonConstants.LICENCE_ATTRIBUTE,
													nrcComponents.get(IzosdwanCommonConstants.LICENCE_ATTRIBUTE) + nrc);
										} else {
											nrcComponents.put(IzosdwanCommonConstants.LICENCE_ATTRIBUTE, nrc);
										}
									}
								}
							}
						}
					});
				}
				List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails = quoteIzosdwanCgwDetailRepository
						.findByQuote(quote);
				if (quoteIzosdwanCgwDetails != null && !quoteIzosdwanCgwDetails.isEmpty()) {
					for (QuoteIzosdwanCgwDetail quoteIzosdwanCgwDetail : quoteIzosdwanCgwDetails) {
						List<QuoteProductComponent> quoteProductComponents2 = getCgwComponents(
								quoteIzosdwanCgwDetail.getId(), IzosdwanCommonConstants.CLOUD_GATEWAY_PORT);
						for (QuoteProductComponent qpc : quoteProductComponents2) {
							LOGGER.info("Component name {}  ", qpc.getMstProductComponent().getName());
							QuotePriceBean quotePriceBean = constructComponentPriceDto(qpc);
							if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
								Double arc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveArc()));
								if (arcComponents.containsKey(qpc.getMstProductComponent().getName())) {
									arcComponents.put(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT_NAME,
											arcComponents.get(qpc.getMstProductComponent().getName()) + arc);
								} else {
									arcComponents.put(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT_NAME, arc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
								Double nrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveNrc()));
								if (nrcComponents.containsKey(qpc.getMstProductComponent().getName())) {
									nrcComponents.put(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT_NAME,
											nrcComponents.get(qpc.getMstProductComponent().getName()) + nrc);
								} else {
									nrcComponents.put(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT_NAME, nrc);
								}
							}
							if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
								Double mrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveMrc()));
								if (mrcComponents.containsKey(qpc.getMstProductComponent().getName())) {
									mrcComponents.put(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT_NAME,
											mrcComponents.get(qpc.getMstProductComponent().getName()) + mrc);
								} else {
									mrcComponents.put(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT_NAME, mrc);
								}
							}
						}
					}
				}

			});
			if (arcComponents != null && !arcComponents.isEmpty()) {
				arcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.CPE)) {
						componentDetailsBean.setName(IzosdwanCommonConstants.RENTAL_CHARGE);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					arcDetailsBean.setArcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(arcDetailsBean.getArcTcv())));
					componentDetailsBeansArc.add(componentDetailsBean);
				});
				arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			}
			if (mrcComponents != null && !mrcComponents.isEmpty()) {
				mrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.CPE)) {
						componentDetailsBean.setName(IzosdwanCommonConstants.RENTAL_CHARGE);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					mrcDetailsBean.setMrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(mrcDetailsBean.getMrcTcv())));
					componentDetailsBeansMrc.add(componentDetailsBean);
				});
				mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			}
			if (nrcComponents != null && !nrcComponents.isEmpty()) {
				nrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.CPE)) {
						componentDetailsBean.setName(IzosdwanCommonConstants.OUTRIGHT_CHARGE);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					nrcDetailsBean.setNrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(nrcDetailsBean.getNrcTcv())));
					componentDetailsBeansNrc.add(componentDetailsBean);
				});
				nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			}
			productPricingDetailsBean.setArcDetailsBean(arcDetailsBean);
			productPricingDetailsBean.setMrcDetailsBean(mrcDetailsBean);
			productPricingDetailsBean.setNrcDetailsBean(nrcDetailsBean);
			productPricingDetailsBean.setTcv(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonths / 12)))
							.add(nrcDetailsBean.getNrcTcv())));
			productPricingDetailsBean.setTcvMrc(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonths / 12)))
							.add(nrcDetailsBean.getNrcTcv())));//confirm !!
			productPricingDetailsBeans.add(productPricingDetailsBean);
			// mockPriceForSdwan(productPricingDetailsBeans);
		}
	}

	private void getCgwPricingDetails(Quote quote, QuoteToLe quoteToLe,
			List<ProductPricingDetailsBean> productPricingDetailsBeans, Integer termsInMonths) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// For IAS sites
		LOGGER.info("Inside Izosdwan!!!");
		List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails = quoteIzosdwanCgwDetailRepository.findByQuote(quote);
		if (quoteIzosdwanCgwDetails != null && !quoteIzosdwanCgwDetails.isEmpty()) {
			ProductPricingDetailsBean productPricingDetailsBean = new ProductPricingDetailsBean();
			ArcDetailsBean arcDetailsBean = new ArcDetailsBean();
			NrcDetailsBean nrcDetailsBean = new NrcDetailsBean();
			arcDetailsBean.setArcTcv(new BigDecimal(0D));
			nrcDetailsBean.setNrcTcv(new BigDecimal(0D));
			productPricingDetailsBean.setProductName("Cloud Gateway");
			Map<String, Double> arcComponents = new HashMap<>();
			Map<String, Double> nrcComponents = new HashMap<>();
			List<ComponentDetailsBean> componentDetailsBeansArc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansNrc = new ArrayList<>();
			arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			quoteIzosdwanCgwDetails.stream().forEach(quoteIzosdwanCgwDetail -> {
				List<QuoteProductComponent> quoteProductComponents = getCgwComponents(quoteIzosdwanCgwDetail.getId(),
						IzosdwanCommonConstants.CLOUD_GATEWAY_PORT);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					quoteProductComponents.stream().forEach(qProComp -> {
						QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
						if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
							Double arc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveArc()));
							if (arcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								arcComponents.put(qProComp.getMstProductComponent().getName(),
										arcComponents.get(qProComp.getMstProductComponent().getName()) + arc);
							} else {
								arcComponents.put(qProComp.getMstProductComponent().getName(), arc);
							}
						}
						if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
							Double nrc = Double.parseDouble(decimalFormat.format(quotePriceBean.getEffectiveNrc()));
							if (nrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								nrcComponents.put(qProComp.getMstProductComponent().getName(),
										nrcComponents.get(qProComp.getMstProductComponent().getName()) + nrc);
							} else {
								nrcComponents.put(qProComp.getMstProductComponent().getName(), nrc);
							}
						}

					});
				}
			});
			if (arcComponents != null && !arcComponents.isEmpty()) {
				arcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					arcDetailsBean.setArcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(arcDetailsBean.getArcTcv())));
					componentDetailsBeansArc.add(componentDetailsBean);
				});
				arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			}
			if (nrcComponents != null && !nrcComponents.isEmpty()) {
				nrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					nrcDetailsBean.setNrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(nrcDetailsBean.getNrcTcv())));
					componentDetailsBeansNrc.add(componentDetailsBean);
				});
				nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);
			}
			productPricingDetailsBean.setArcDetailsBean(arcDetailsBean);
			productPricingDetailsBean.setNrcDetailsBean(nrcDetailsBean);
			productPricingDetailsBean.setTcv((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonths / 12)))
					.add(nrcDetailsBean.getNrcTcv()));
			productPricingDetailsBeans.add(productPricingDetailsBean);
			// mockPriceForSdwan(productPricingDetailsBeans);
		}
	}

	private void calculateSolutionLevelPrice(SolutionPricingDetailsBean solutionPricingDetailsBean) {
		solutionPricingDetailsBean.getProductPricingDetailsBeans().stream().forEach(proPrice -> {
			solutionPricingDetailsBean.setArc(IzosdwanUtils.formatBigDecimal(
					solutionPricingDetailsBean.getArc().add(proPrice.getArcDetailsBean().getArcTcv())));
			solutionPricingDetailsBean.setMrc(IzosdwanUtils.formatBigDecimal(
					solutionPricingDetailsBean.getMrc().add(proPrice.getMrcDetailsBean().getMrcTcv())));
			solutionPricingDetailsBean.setNrc(IzosdwanUtils.formatBigDecimal(
					solutionPricingDetailsBean.getNrc().add(proPrice.getNrcDetailsBean().getNrcTcv())));
			solutionPricingDetailsBean.setTcv(IzosdwanUtils.formatBigDecimal(
					solutionPricingDetailsBean.getTcv().add(proPrice.getTcv())));
//			solutionPricingDetailsBean.setTcvMrc(IzosdwanUtils.formatBigDecimal(
//					solutionPricingDetailsBean.getTcv().add(proPrice.getTcv())));
		});
	}

	private void constructQuoteLevelPrice(SolutionPricingDetailsBean solutionPricingDetailsBean,
			QuotePricingDetailsBean quotePricingDetailsBean) {
		quotePricingDetailsBean.setArc(IzosdwanUtils
				.formatBigDecimal(solutionPricingDetailsBean.getArc().add(quotePricingDetailsBean.getArc())));
		quotePricingDetailsBean.setNrc(IzosdwanUtils
				.formatBigDecimal(solutionPricingDetailsBean.getNrc().add(quotePricingDetailsBean.getNrc())));
		quotePricingDetailsBean.setMrc(IzosdwanUtils
				.formatBigDecimal(solutionPricingDetailsBean.getMrc().add(quotePricingDetailsBean.getMrc())));
		quotePricingDetailsBean.setTcv(IzosdwanUtils
				.formatBigDecimal(solutionPricingDetailsBean.getTcv().add(quotePricingDetailsBean.getTcv())));
//		quotePricingDetailsBean.setTcvMrc(IzosdwanUtils
//				.formatBigDecimal(solutionPricingDetailsBean.getTcv().add(quotePricingDetailsBean.getTcv())));
	}

	/**
	 * 
	 * Get price
	 * 
	 * @param pricingInformationRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	public SolutionPricingDetailsBean getPriceSiteWise(PricingInformationRequestBean pricingInformationRequestBean)
			throws TclCommonException {
		SolutionPricingDetailsBean solutionPricingDetailsBean = null;
		if (pricingInformationRequestBean != null && pricingInformationRequestBean.getQuoteId() != null)
			try {
				Quote quote = quoteRepository.findByIdAndStatus(pricingInformationRequestBean.getQuoteId(),
						CommonConstants.BACTIVE);
				if (quote != null) {
					// As of now getting price only for SDWAN solution
					ProductSolution productSolution = productSolutionRepository
							.findByReferenceIdForIzoSdwan(pricingInformationRequestBean.getQuoteId());
					QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
					Integer terms = Integer.parseInt(quoteToLe.getTermInMonths().substring(0, 2));
					if (productSolution != null) {
						solutionPricingDetailsBean = new SolutionPricingDetailsBean();
						List<ProductPricingDetailsBean> productPricingDetailsBeans = new ArrayList<>();
						List<QuoteIzosdwanSite> quoteIzosdwanSites = new ArrayList<>();
						if (pricingInformationRequestBean.getSiteIds() != null
								&& !pricingInformationRequestBean.getSiteIds().isEmpty()) {
							quoteIzosdwanSites = quoteIzosdwanSiteRepository.findByIdInAndStatus(
									pricingInformationRequestBean.getSiteIds(), CommonConstants.BACTIVE);
						} else {
							quoteIzosdwanSites = quoteIzosdwanSiteRepository.findByProductSolution(productSolution);
						}

						if (pricingInformationRequestBean.getProductName() != null) {
							quoteIzosdwanSites = quoteIzosdwanSites.stream()
									.filter(site -> site.getIzosdwanSiteProduct()
											.equalsIgnoreCase(pricingInformationRequestBean.getProductName()))
									.collect(Collectors.toList());
						}
						if (pricingInformationRequestBean.getSiteType() != null) {
							quoteIzosdwanSites = quoteIzosdwanSites.stream()
									.filter(site -> site.getIzosdwanSiteType()
											.equalsIgnoreCase(pricingInformationRequestBean.getSiteType()))
									.collect(Collectors.toList());
						}
						if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
							quoteIzosdwanSites.sort(Comparator.comparing(QuoteIzosdwanSite::getIzosdwanSiteProduct));
							quoteIzosdwanSites.stream().forEach(site -> {
								getProductLevelPricingDetailsSitewise(site.getIzosdwanSiteProduct(), site,
										productPricingDetailsBeans, terms);
								getProductLevelPricingDetailsSitewiseForIzosdwan(site.getIzosdwanSiteProduct(), site,
										productPricingDetailsBeans, terms);
							});
						}
						solutionPricingDetailsBean.setArc(new BigDecimal(0D));
						solutionPricingDetailsBean.setTcvMrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setMrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setNrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setTcv(new BigDecimal(0D));
						productPricingDetailsBeans
								.sort(Comparator.comparing(ProductPricingDetailsBean::getProductName).reversed());
						solutionPricingDetailsBean.setProductPricingDetailsBeans(productPricingDetailsBeans);
						calculateSolutionLevelPrice(solutionPricingDetailsBean);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error occured while fetching the pricing information {}", e);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		return solutionPricingDetailsBean;
	}

	private void getProductLevelPricingDetailsSitewise(String productName, QuoteIzosdwanSite quoteIzosdwanSite,
			List<ProductPricingDetailsBean> productPricingDetailsBeans, Integer termsInMonth) {

		if (quoteIzosdwanSite != null) {
			ProductPricingDetailsBean productPricingDetailsBean = new ProductPricingDetailsBean();
			ArcDetailsBean arcDetailsBean = new ArcDetailsBean();
			MrcDetailsBean mrcDetailsBean = new MrcDetailsBean();
			NrcDetailsBean nrcDetailsBean = new NrcDetailsBean();
			arcDetailsBean.setArcTcv(new BigDecimal(0D));
			nrcDetailsBean.setNrcTcv(new BigDecimal(0D));
			mrcDetailsBean.setMrcTcv(new BigDecimal(0D));
			productPricingDetailsBean.setProductName(productName);
			productPricingDetailsBean.setSiteId(quoteIzosdwanSite.getId());
			productPricingDetailsBean.setLocationId(quoteIzosdwanSite.getErfLocSitebLocationId());
			productPricingDetailsBean.setServiceId(quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
			productPricingDetailsBean.setPri_sec(quoteIzosdwanSite.getPriSec());
			productPricingDetailsBean.setBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
			String orderCategory = getAttributeValue(quoteIzosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
					IzosdwanCommonConstants.ORDER_CATEGORY);
			String ordersubCategory = getAttributeValue(quoteIzosdwanSite.getId(),
					IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.ORDER_SUB_CATEGORY);
			Map<String, Double> arcComponents = new HashMap<>();
			Map<String, Double> mrcComponents = new HashMap<>();
			Map<String, Double> nrcComponents = new HashMap<>();
			List<ComponentDetailsBean> componentDetailsBeansArc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansMrc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansNrc = new ArrayList<>();
			arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);

			List<QuoteProductComponent> quoteProductComponents = getComponentBasenOnVersion(quoteIzosdwanSite.getId(),
					false, false);
			if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
				quoteProductComponents.stream().forEach(qProComp -> {
					if (!qProComp.getMstProductComponent().getName().equalsIgnoreCase(FPConstants.CPE.toString())
							&& !qProComp.getMstProductComponent().getName()
									.equalsIgnoreCase(FPConstants.LICENSE_COST.toString())) {
						QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
						if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
							if (arcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								arcComponents.put(qProComp.getMstProductComponent().getName(),
										arcComponents.get(qProComp.getMstProductComponent().getName())
												+ quotePriceBean.getEffectiveArc());
							} else {
								arcComponents.put(qProComp.getMstProductComponent().getName(),
										quotePriceBean.getEffectiveArc());
							}
						}
						if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
							if (mrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								mrcComponents.put(qProComp.getMstProductComponent().getName(),
										mrcComponents.get(qProComp.getMstProductComponent().getName())
												+ quotePriceBean.getEffectiveMrc());
							} else {
								mrcComponents.put(qProComp.getMstProductComponent().getName(),
										quotePriceBean.getEffectiveMrc());
							}
						}
						if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
							if (nrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								nrcComponents.put(qProComp.getMstProductComponent().getName(),
										nrcComponents.get(qProComp.getMstProductComponent().getName())
												+ quotePriceBean.getEffectiveNrc());
							} else {
								nrcComponents.put(qProComp.getMstProductComponent().getName(),
										quotePriceBean.getEffectiveNrc());
							}
						}
					}

				});
			}

			if (arcComponents != null && !arcComponents.isEmpty()) {
				arcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					componentDetailsBean.setOrderType(orderCategory
							+ ((ordersubCategory != null && !ordersubCategory.isEmpty()) ? ("-" + ordersubCategory)
									: ""));
					arcDetailsBean.setArcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(arcDetailsBean.getArcTcv())));

					componentDetailsBeansArc.add(componentDetailsBean);
				});

			}
			if (mrcComponents != null && ! mrcComponents.isEmpty()) {
				mrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if(key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					componentDetailsBean.setOrderType(orderCategory + ((ordersubCategory!=null && !ordersubCategory.isEmpty())? ("-" + ordersubCategory) : ""));
					mrcDetailsBean.setMrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(mrcDetailsBean.getMrcTcv())));
					componentDetailsBeansMrc.add(componentDetailsBean);
				});
			}
			if (nrcComponents != null && !nrcComponents.isEmpty()) {
				nrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)) {
						componentDetailsBean.setName(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
					}
					if (key.equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)) {
						componentDetailsBean.setName(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					componentDetailsBean.setOrderType(orderCategory
							+ ((ordersubCategory != null && !ordersubCategory.isEmpty()) ? ("-" + ordersubCategory)
									: ""));
					nrcDetailsBean.setNrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(nrcDetailsBean.getNrcTcv())));
					componentDetailsBeansNrc.add(componentDetailsBean);
				});

			}
			productPricingDetailsBean.setArcDetailsBean(arcDetailsBean);
			productPricingDetailsBean.setMrcDetailsBean(mrcDetailsBean);
			productPricingDetailsBean.setNrcDetailsBean(nrcDetailsBean);
			productPricingDetailsBean.setTcv(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonth / 12)))
							.add(nrcDetailsBean.getNrcTcv())));
			productPricingDetailsBean.setTcvMrc(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonth / 12)))
							.add(nrcDetailsBean.getNrcTcv())));
			productPricingDetailsBeans.add(productPricingDetailsBean);
		}
	}

	private void getProductLevelPricingDetailsSitewiseForIzosdwan(String productName,
			QuoteIzosdwanSite quoteIzosdwanSite, List<ProductPricingDetailsBean> productPricingDetailsBeans,
			Integer termsInMonths) {

		if (quoteIzosdwanSite != null) {
			ProductPricingDetailsBean productPricingDetailsBean = new ProductPricingDetailsBean();
			ArcDetailsBean arcDetailsBean = new ArcDetailsBean();
			MrcDetailsBean mrcDetailsBean = new MrcDetailsBean();
			NrcDetailsBean nrcDetailsBean = new NrcDetailsBean();
			arcDetailsBean.setArcTcv(new BigDecimal(0D));
			mrcDetailsBean.setMrcTcv(new BigDecimal(0D));
			nrcDetailsBean.setNrcTcv(new BigDecimal(0D));
			productPricingDetailsBean.setProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
			productPricingDetailsBean.setSiteId(quoteIzosdwanSite.getId());
			productPricingDetailsBean.setLocationId(quoteIzosdwanSite.getErfLocSitebLocationId());
			productPricingDetailsBean.setServiceId(quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
			productPricingDetailsBean.setPri_sec(quoteIzosdwanSite.getPriSec());
			productPricingDetailsBean.setBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
			Map<String, Double> arcComponents = new HashMap<>();
			Map<String, Double> mrcComponents = new HashMap<>();
			Map<String, Double> nrcComponents = new HashMap<>();
			List<ComponentDetailsBean> componentDetailsBeansArc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansMrc = new ArrayList<>();
			List<ComponentDetailsBean> componentDetailsBeansNrc = new ArrayList<>();
			arcDetailsBean.setComponentDetailsBeans(componentDetailsBeansArc);
			mrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansMrc);
			nrcDetailsBean.setComponentDetailsBeans(componentDetailsBeansNrc);

			List<QuoteProductComponent> quoteProductComponents = getComponentBasenOnVersion(quoteIzosdwanSite.getId(),
					false, false);
			if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
				quoteProductComponents.stream().forEach(qProComp -> {
					if (qProComp.getMstProductComponent().getName().equalsIgnoreCase(FPConstants.CPE.toString())
							|| qProComp.getMstProductComponent().getName()
									.equalsIgnoreCase(FPConstants.LICENSE_COST.toString())) {
						QuotePriceBean quotePriceBean = constructComponentPriceDto(qProComp);
						if (quotePriceBean != null && quotePriceBean.getEffectiveArc() != null) {
							if (arcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								arcComponents.put(qProComp.getMstProductComponent().getName(),
										arcComponents.get(qProComp.getMstProductComponent().getName())
												+ quotePriceBean.getEffectiveArc());
							} else {
								arcComponents.put(qProComp.getMstProductComponent().getName(),
										quotePriceBean.getEffectiveArc());
							}
						}
						if (quotePriceBean != null && quotePriceBean.getEffectiveMrc() != null) {
							if (mrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								mrcComponents.put(qProComp.getMstProductComponent().getName(),
										mrcComponents.get(qProComp.getMstProductComponent().getName())
												+ quotePriceBean.getEffectiveMrc());
							} else {
								mrcComponents.put(qProComp.getMstProductComponent().getName(),
										quotePriceBean.getEffectiveMrc());
							}
						}
						if (quotePriceBean != null && quotePriceBean.getEffectiveNrc() != null) {
							if (nrcComponents.containsKey(qProComp.getMstProductComponent().getName())) {
								nrcComponents.put(qProComp.getMstProductComponent().getName(),
										nrcComponents.get(qProComp.getMstProductComponent().getName())
												+ quotePriceBean.getEffectiveNrc());
							} else {
								nrcComponents.put(qProComp.getMstProductComponent().getName(),
										quotePriceBean.getEffectiveNrc());
							}
						}
					}

				});
			}

			if (arcComponents != null && !arcComponents.isEmpty()) {
				arcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(FPConstants.CPE.toString())) {
						String cpeType = getCpeType(quoteIzosdwanSite);
						componentDetailsBean.setName(cpeType);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					// componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					arcDetailsBean.setArcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(arcDetailsBean.getArcTcv())));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					componentDetailsBean.setOrderType(CommonConstants.EMPTY);
					// arcDetailsBean.setArcTcv(Double.parseDouble(decimalFormat.format(value +
					// arcDetailsBean.getArcTcv())));
					componentDetailsBeansArc.add(componentDetailsBean);
				});

			}
			if (mrcComponents != null && !mrcComponents.isEmpty()) {
				mrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(FPConstants.CPE.toString())) {
						String cpeType = getCpeType(quoteIzosdwanSite);
						componentDetailsBean.setName(cpeType);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					// componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					mrcDetailsBean.setMrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(mrcDetailsBean.getMrcTcv())));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					componentDetailsBean.setOrderType(CommonConstants.EMPTY);
					// arcDetailsBean.setArcTcv(Double.parseDouble(decimalFormat.format(value +
					// arcDetailsBean.getArcTcv())));
					componentDetailsBeansMrc.add(componentDetailsBean);
				});

			}
			if (nrcComponents != null && !nrcComponents.isEmpty()) {
				nrcComponents.forEach((key, value) -> {
					ComponentDetailsBean componentDetailsBean = new ComponentDetailsBean();
					componentDetailsBean.setName(key);
					if (key.equalsIgnoreCase(FPConstants.CPE.toString())) {
						String cpeType = getCpeType(quoteIzosdwanSite);
						componentDetailsBean.setName(cpeType);
					}
					componentDetailsBean.setValues(IzosdwanUtils.formatBigDecimal(new BigDecimal(value)));
					// componentDetailsBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
					// nrcDetailsBean.setNrcTcv(Double.parseDouble(decimalFormat.format(value +
					// nrcDetailsBean.getNrcTcv())));
					componentDetailsBean.setActionType(MACDConstants.NEW);
					componentDetailsBean.setOrderType(CommonConstants.EMPTY);
					nrcDetailsBean.setNrcTcv(
							IzosdwanUtils.formatBigDecimal(new BigDecimal(value).add(nrcDetailsBean.getNrcTcv())));
					componentDetailsBeansNrc.add(componentDetailsBean);
				});

			}
			productPricingDetailsBean.setArcDetailsBean(arcDetailsBean);
			productPricingDetailsBean.setMrcDetailsBean(mrcDetailsBean);
			productPricingDetailsBean.setNrcDetailsBean(nrcDetailsBean);
			productPricingDetailsBean.setTcv(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonths / 12)))
							.add(nrcDetailsBean.getNrcTcv())));
			productPricingDetailsBean.setTcvMrc(IzosdwanUtils
					.formatBigDecimal((arcDetailsBean.getArcTcv().multiply(new BigDecimal(termsInMonths / 12)))
							.add(nrcDetailsBean.getNrcTcv())));
			productPricingDetailsBeans.add(productPricingDetailsBean);
		}
	}

	/**
	 * 
	 * processQuotePdf
	 * 
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
	public String processQuotePdf(Integer quoteId, HttpServletResponse response, Integer quoteToLeId)
			throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			String html = getQuoteHtml(quote);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "Quote_" + quote.getQuoteCode() + ".pdf";
			response.reset();
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			response.setContentLength(outArray.length);
			response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
			response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());

			bos.flush();
			bos.close();

		} catch (TclCommonException e) {
			LOGGER.warn("Error in Quote Pdf {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (IOException | DocumentException e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	/**
	 * getQuoteHtml
	 * 
	 * @param quoteDetail
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	private String getQuoteHtml(Quote quote) throws TclCommonException {
		IzosdwanQuotePdfBean cofPdfRequest = new IzosdwanQuotePdfBean();
		QuoteBean quoteDetail = izosdwanQuoteService.getQuoteDetails(quote.getId(), null, false, null, null);
		supplierInfo(cofPdfRequest,quote);
		constructVariable(quote, cofPdfRequest);
		constructCloudGatewayDetails(quote, cofPdfRequest);
		// constructSiteDetails(cofPdfRequest, quote.getId());
		constructSiteDetailsToQuote(cofPdfRequest, quote.getId());
		constructVproxyDetails(cofPdfRequest, quote.getId());
		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("izosdwan_quote_template", context);
	}

	private void constructVproxyDetails(IzosdwanQuotePdfBean cofPdfRequest, Integer quoteId) throws TclCommonException {
		/** get vproxy charges **/
		List<ProductSolutionBean> solutions = new ArrayList<>();
		List<QuoteProductComponentBean> commonComponentsVproxy = new ArrayList<>();
		QuoteBean quoteDetail = izosdwanQuoteService.getQuoteDetails(quoteId, null, false, null, null);
		for (QuoteToLeBean quoteLe : quoteDetail.getLegalEntities()) {
			try {
				Optional<QuoteToLeProductFamilyBean> leProductFamilyBean = quoteLe.getProductFamilies().stream()
						.filter(productFamily -> productFamily.getProductName()
								.equalsIgnoreCase(IzosdwanCommonConstants.VPROXY))
						.findFirst();
				if (leProductFamilyBean.isPresent()) {
					solutions = leProductFamilyBean.get().getSolutions();
					if (leProductFamilyBean.get().getComponents() != null
							&& !leProductFamilyBean.get().getComponents().isEmpty()) {
						commonComponentsVproxy = leProductFamilyBean.get().getComponents();
					}
				}
				if (!solutions.isEmpty()) {
					cofPdfRequest.setIsVproxy(true);
					List<VproxyCommercialDetailsBean> vproxyCommercialBeans = new ArrayList<>();
					for (ProductSolutionBean solution : solutions) {
						VproxyCommercialDetailsBean vproxyCommercial = new VproxyCommercialDetailsBean();
						solution.getComponents().forEach(vproxyComponent -> {
							vproxyCommercial.setSolutionName(vproxyComponent.getDescription());
							List<CommercialAttributesVproxy> commercialAttributesVproxy = new ArrayList<>();
							vproxyComponent.getAttributes().forEach(attr -> {
								CommercialAttributesVproxy attribute = new CommercialAttributesVproxy();
								attribute.setAttributeName((!attr.getAttributeValues().isEmpty())
										? (attr.getDisplayValue().concat(" - " + attr.getAttributeValues()))
										: (attr.getDisplayValue()));
								attribute.setArc(getFormattedCurrencyBig(
										IzosdwanUtils
												.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveArc())),
										cofPdfRequest.getCurrency()));
								attribute.setNrc(getFormattedCurrencyBig(
										IzosdwanUtils
												.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveNrc())),
										cofPdfRequest.getCurrency()));
								attribute.setMrc(getFormattedCurrencyBig(
										IzosdwanUtils
												.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveArc()/12)),
										cofPdfRequest.getCurrency()));
								commercialAttributesVproxy.add(attribute);
							});
							vproxyCommercial.setCommercialAttributesVproxy(commercialAttributesVproxy);
						});
						vproxyCommercialBeans.add(vproxyCommercial);
					}
					cofPdfRequest.setCommercialDetailsVproxySolutions(vproxyCommercialBeans);
					// set isVproxyComm
					if (!commonComponentsVproxy.get(0).getAttributes().isEmpty()) {
						cofPdfRequest.setIsVproxyComm(true);
						List<CommercialAttributesVproxy> commonAttributesVproxy = new ArrayList<>();
						commonComponentsVproxy.get(0).getAttributes().forEach(attr -> {
							CommercialAttributesVproxy commonAttribute = new CommercialAttributesVproxy();
							commonAttribute.setAttributeName(attr.getDisplayValue());
							commonAttribute.setArc(getFormattedCurrencyBig(
									IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveArc())),
									cofPdfRequest.getCurrency()));
							commonAttribute.setNrc(getFormattedCurrencyBig(
									IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveNrc())),
									cofPdfRequest.getCurrency()));
							commonAttribute.setMrc(getFormattedCurrencyBig(
									IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveArc()/12)),
									cofPdfRequest.getCurrency()));
							commonAttributesVproxy.add(commonAttribute);
						});
						cofPdfRequest.setCommonComponentsVproxy(commonAttributesVproxy);
					}
				}

				LOGGER.info("commercial:{}", cofPdfRequest.getCommercialDetailsVproxySolutions());
			} catch (Exception e) {
				e.printStackTrace();
			}
			/** get vproxy charges end **/
		}
	}

	private void constructCloudGatewayDetails(Quote quote, IzosdwanQuotePdfBean cofPdfRequest) {
		List<QuoteIzosdwanCgwDetail> cgWdetails = quoteIzosdwanCgwDetailRepository.findByQuote(quote);
		QuoteIzosdwanCgwDetails cgwBean = new QuoteIzosdwanCgwDetails();
		if (!cgWdetails.isEmpty() && cgWdetails != null) {
			cgwBean.setCgwMigUserModifiedBW(cgWdetails.get(0).getMigrationUserBw());
			cgwBean.setCgwMigSuggestedBW(cgWdetails.get(0).getMigrationSystemBw());
			cgwBean.setMigrationHeteroBandwidth(cgWdetails.get(0).getHetroBw());
			cgwBean.setPrimaryLocation(cgWdetails.get(0).getPrimaryLocation());
			cgwBean.setSecondaryLocation(cgWdetails.get(0).getSecondaryLocation());
			cgwBean.setUseCase1(cgWdetails.get(0).getUseCase1a());
			cgwBean.setUseCase2(cgWdetails.get(0).getUseCase2());
			cgwBean.setUseCase3(cgWdetails.get(0).getUseCase3());
			cgwBean.setUseCase4(cgWdetails.get(0).getUseCase4());
			cgwBean.setComponents(constructQuoteProductComponentCgw(cgWdetails.get(0).getId(), false, false));
		}
		if (cgWdetails != null) {
			cgWdetails.stream().forEach(cgw -> {
				cofPdfRequest.setCgwGatewayBW(cgw.getHetroBw().concat(IzosdwanCommonConstants.MBPS));
				cofPdfRequest.setCgwServiceBW(null);
				cofPdfRequest.setPrimaryLocation(cgw.getPrimaryLocation());
				cofPdfRequest.setSecondaryLocation(cgw.getSecondaryLocation());
				cofPdfRequest.setMigrationBandwidth(cgw.getMigrationSystemBw().concat(IzosdwanCommonConstants.MBPS));
			});

			List<QuoteProductComponentBean> pBeans = cgwBean.getComponents();
			if (pBeans != null) {
				pBeans.stream().forEach(price -> {
					QuotePriceBean priceBean = price.getPrice();
					cofPdfRequest.setEffectiveArc(
							new BigDecimal(priceBean.getEffectiveArc()).setScale(2, RoundingMode.HALF_UP));
					cofPdfRequest.setEffectiveNrc(
							new BigDecimal(priceBean.getEffectiveNrc()).setScale(2, RoundingMode.HALF_UP));
					cofPdfRequest.setEffectiveMrc(
							new BigDecimal((priceBean.getEffectiveArc()/12)).setScale(2, RoundingMode.HALF_UP));
				});
			}

		}
	}

	private void constructVariable(Quote quote, IzosdwanQuotePdfBean cofPdfRequest) throws TclCommonException {

		cofPdfRequest.setQuoteCode(quote.getQuoteCode());
		cofPdfRequest.setCreatedDate(DateUtil.convertDateToMMMString(new Date()));
		QuotePricingDetailsBean quotePricingDetailsBean = getPriceInformationForTheQuote(quote.getId());
		cofPdfRequest.setCurrency(quotePricingDetailsBean.getCurrency());
		List<ChargeableLineItemSummaryBean> chargeableLineItemSummaryBeans = new ArrayList<>();
		if (quotePricingDetailsBean != null && quotePricingDetailsBean.getIzosdwan() != null
				&& quotePricingDetailsBean.getIzosdwan().getProductPricingDetailsBeans() != null) {

			quotePricingDetailsBean.getIzosdwan().getProductPricingDetailsBeans().stream().forEach(prod -> {
				if (!(prod.getProductName().equals("BYON Internet"))) {
					ChargeableLineItemSummaryBean chargeableLineItemSummaryBean = new ChargeableLineItemSummaryBean();
					if(cofPdfRequest.getIsIndia()){
						chargeableLineItemSummaryBean.setArc(
							getFormattedCurrencyBig(prod.getArcDetailsBean().getArcTcv(), cofPdfRequest.getCurrency()));
					}else{
						chargeableLineItemSummaryBean.setArc(
								getFormattedCurrencyBig((prod.getArcDetailsBean().getArcTcv().divide(new BigDecimal(12),MathContext.DECIMAL128)), cofPdfRequest.getCurrency()));
					}
//					chargeableLineItemSummaryBean.setArc(
//							getFormattedCurrencyBig(prod.getArcDetailsBean().getArcTcv(), cofPdfRequest.getCurrency()));
					chargeableLineItemSummaryBean.setName(prod.getProductName());
					chargeableLineItemSummaryBean.setNrc(
							getFormattedCurrencyBig(prod.getNrcDetailsBean().getNrcTcv(), cofPdfRequest.getCurrency()));
					chargeableLineItemSummaryBean
							.setTcv(getFormattedCurrencyBig(prod.getTcv(), cofPdfRequest.getCurrency()));
					chargeableLineItemSummaryBeans.add(chargeableLineItemSummaryBean);
				}
			});
		}

		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quote.getId());
		quoteToLe.stream().forEach(q -> {
			String currCode = q.getCurrencyCode();
			String totalbillCurrArc = "Total ARC " + "(" + currCode + ")";
			String totalbillCurrNrc = "Total NRC " + "(" + currCode + ")";
			String totalbillCurrMrc = "Total MRC " + "(" + currCode + ")";
			String billCurrArcMrc = "ARC" + "(" + currCode + ")";
			String billCurrNrc = "NRC" + "(" + currCode + ")";
			String billCurrMrc = "MRC" + "(" + currCode + ")";
			String totalbillCurrCharges = "Total Charges" + "(" + currCode + ")";

			cofPdfRequest.setContractTerm(String.valueOf(q.getTermInMonths()));
			cofPdfRequest.setTotalBillCurArc(totalbillCurrArc);
			cofPdfRequest.setTotalBillCurNrc(totalbillCurrNrc);
			cofPdfRequest.setTotalBillCurMrc(totalbillCurrMrc);
			cofPdfRequest.setBillCurrencyArc(billCurrArcMrc);
			cofPdfRequest.setBillCurrencyNrc(billCurrNrc);
			cofPdfRequest.setBillCurrencyMrc(billCurrMrc);
			cofPdfRequest.setTotalBillCurrCharges(totalbillCurrCharges);
		});

		if(cofPdfRequest.getIsIndia()) {
			cofPdfRequest.setArcTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getArc(), cofPdfRequest.getCurrency()));
		}else{
			cofPdfRequest.setMrcTcv(getFormattedCurrencyBig((quotePricingDetailsBean.getArc().divide(new BigDecimal(12),MathContext.DECIMAL128)), cofPdfRequest.getCurrency()));
		}
		cofPdfRequest.setNrcTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getNrc(), cofPdfRequest.getCurrency()));
		cofPdfRequest.setTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getTcv(), cofPdfRequest.getCurrency()));
		cofPdfRequest.setChargeableLineItemSummaryBeans(chargeableLineItemSummaryBeans);
		// set isPureByon
		List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
				.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P, quote.getId());
		if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
				&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue() != null
				&& "true".equalsIgnoreCase(quoteIzoSdwanAttributeValues.get(0).getAttributeValue())) {
			cofPdfRequest.setIsPureByon(true);
		}

	}

	private void setFeasDetails(Integer id, TechDetailCof dets) {
		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIzosdwanSite_IdAndIsSelected(id, (byte) 1);
		siteFeasibilities.forEach(feas -> {
			dets.setAccessProvide(feas.getProvider());
			dets.setFeasibilityCreatedDate(DateUtil.convertDateToString(feas.getCreatedTime()));
		});

	}

	private void addServiceTypeRowToQuote(List<IzosdwanCommericalBean> izosdwanCommericalBeans) {
		IzosdwanCommericalBean izosdwanCommericalBean = new IzosdwanCommericalBean();
		izosdwanCommericalBean.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setArc(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setNrc(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setServiceType("Fixed");
		izosdwanCommericalBeans.add(izosdwanCommericalBean);
	}

	private void addSubtotalDetailsToQuote(ProductPricingDetailsBean pro,
			List<IzosdwanCommericalBean> izosdwanCommericalBeansCof, IzosdwanQuotePdfBean cofPdfBean) {
		DecimalFormat df = new DecimalFormat("0.00");
		IzosdwanCommericalBean izosdwanCommericalBeanCof = new IzosdwanCommericalBean();
		izosdwanCommericalBeanCof.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
		if(cofPdfBean.getIsIndia()){
			izosdwanCommericalBeanCof
					.setArc(getFormattedCurrencyBig(pro.getArcDetailsBean().getArcTcv(), cofPdfBean.getCurrency()));
		}else{
			izosdwanCommericalBeanCof
					.setArc(getFormattedCurrencyBig(pro.getArcDetailsBean().getArcTcv().divide(new BigDecimal(12), MathContext.DECIMAL128), cofPdfBean.getCurrency()));
		}
//		izosdwanCommericalBeanCof
//				.setArc(getFormattedCurrencyBig(pro.getArcDetailsBean().getArcTcv(), cofPdfBean.getCurrency()));
		izosdwanCommericalBeanCof
				.setNrc(getFormattedCurrencyBig(pro.getNrcDetailsBean().getNrcTcv(), cofPdfBean.getCurrency()));
		izosdwanCommericalBeanCof.setBandwidth(IzosdwanCommonConstants.SUB_TOTAL);
		izosdwanCommericalBeanCof.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCof);
	}

	private void constructSiteDetailsToQuote(IzosdwanQuotePdfBean cofPdfRequest, Integer quoteId) {
		try {
			List<IzosdwanPdfSiteBean> izosdwanCofSiteBeans = new ArrayList<>();
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);

			if (productSolution != null) {
				List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
						.findByProductSolution(productSolution);
				Map<String, List<QuoteIzosdwanSite>> izomap = new HashMap<>();
//				Map<String, List<QuoteIzosdwanSite>> izomaptemp = new HashMap<>();
				Map<String, List<QuoteIzosdwanSite>> izomaptemp2 = new HashMap<>();

//				izomaptemp = quoteIzosdwanSites.stream()
//						.filter(site -> site.getIzosdwanSiteType().contains("Single BYON"))
//						.collect(Collectors.groupingBy(site -> site.getId().toString()));

				izomaptemp2 = quoteIzosdwanSites.stream().filter(site -> StringUtil.isBlank(site.getPrimaryServiceId()))
						.collect(Collectors.groupingBy(site -> site.getErfServiceInventoryTpsServiceId()));

//				izomaptemp2.putAll(izomaptemp);

				izomap = quoteIzosdwanSites.stream().filter(site -> StringUtils.isNotBlank(site.getPrimaryServiceId()))
						.collect(Collectors.groupingBy(site -> site.getPrimaryServiceId()));

				izomap.putAll(izomaptemp2);

//                quoteIzosdwanSites.forEach(single -> {
//                    if(Objects.isNull(single.getPrimaryServiceId())){  StringUtils.isNotBlank("jhdhd");
//                        List<QuoteIzosdwanSite> temp = new ArrayList<>();  single.getPrimaryServiceId().isEmpty() ||  || !site.getPrimaryServiceId().isEmpty()
//                        temp.add(single);
//                        izomaptemp.put(single.getErfServiceInventoryTpsServiceId(), temp);
//                    }
//                });

				if (!CollectionUtils.isEmpty(izomap)) {
					izomap.forEach((k, v) -> {
						String serviceId = k;
						List<QuoteIzosdwanSite> site = v;
						List<Integer> siteId = new ArrayList<>();
						IzosdwanPdfSiteBean izosdwanCofSiteBean = new IzosdwanPdfSiteBean();

						v.forEach(sites -> {
							siteId.add(sites.getId());

							List<QuoteProductComponent> componentid = quoteProductComponentRepository
									.findByReferenceId(sites.getId());
							Map<String, QuoteProductComponentsAttributeValue> componentAttriValueMap = new HashMap<>();
							componentid.forEach(comp -> {

								List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
										.findByQuoteProductComponent(comp);
								quoteProductComponentsAttributeValues.forEach(y -> {
									componentAttriValueMap.put(y.getProductAttributeMaster().getName(), y);
								});

								if (comp.getType().equalsIgnoreCase("primary")) {
									if (sites.getPriSec().equalsIgnoreCase("primary")) {
										TechDetailCof dets = new TechDetailCof();
										izoSdwnQuotepdfService.setdetailsvalues(componentAttriValueMap, dets);
										dets.setValidityCreatedDate(PDFConstants.FEASIBILITY_VALIDAITY);
										String range = quoteIzosdwanSiteRepository.getPortBandWidth(
												productSolution.getId(), "primary", sites.getIzosdwanSiteType(),
												sites.getIzosdwanSiteProduct(), sites.getNewCpe());
										dets.setBandwidthRange(range.concat(IzosdwanCommonConstants.MBPS));
										String siteServiceId = "";
										dets.setPrimaryServiceId(sites.getErfServiceInventoryTpsServiceId());

										izoSdwnQuotepdfService.setCpeBomDetails(componentAttriValueMap, dets);

										setFeasDetails(sites.getId(), dets);

										Map<String, TechDetailCof> mapDet = new HashMap<>();
										mapDet.put("Primary", dets);
//										izosdwanCofSiteBean.setPrimaryDetails(mapDet);
									}
								} else if (comp.getType().equalsIgnoreCase("secondary")) {
									if (sites.getPriSec().equalsIgnoreCase("secondary")) {
										TechDetailCof dets = new TechDetailCof();
										izoSdwnQuotepdfService.setdetailsvalues(componentAttriValueMap, dets);
										dets.setValidityCreatedDate(PDFConstants.FEASIBILITY_VALIDAITY);
										String range = quoteIzosdwanSiteRepository.getPortBandWidth(
												productSolution.getId(), "SECONDARY", sites.getIzosdwanSiteType(),
												sites.getIzosdwanSiteProduct(), sites.getNewCpe());
										dets.setBandwidthRange(range.concat(IzosdwanCommonConstants.MBPS));
										String siteServiceId = "";
										dets.setSecondaryServiceId(sites.getErfServiceInventoryTpsServiceId());

										izoSdwnQuotepdfService.setCpeBomDetails(componentAttriValueMap, dets);

										setFeasDetails(sites.getId(), dets);

										Map<String, TechDetailCof> mapDet = new HashMap<>();
										mapDet.put("Secondary", dets);
//										izosdwanCofSiteBean.setSecondaryDetails(mapDet);
									}
								}
							});

						});

						PricingInformationRequestBean pricingInformationRequestBean = new PricingInformationRequestBean();
						pricingInformationRequestBean.setQuoteId(quoteId);
						pricingInformationRequestBean.setSiteIds(siteId);
						SolutionPricingDetailsBean solutionPricingDetailsBean = null;
						try {
							solutionPricingDetailsBean = getPriceSiteWise(pricingInformationRequestBean);
						} catch (TclCommonException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (solutionPricingDetailsBean != null) {
							List<IzosdwanCommericalBean> izosdwanCommericalBeansCof = new ArrayList<>();
							List<ProductPricingDetailsBean> productPricingDetailsBeans = solutionPricingDetailsBean
									.getProductPricingDetailsBeans();
							if (productPricingDetailsBeans != null && !productPricingDetailsBeans.isEmpty()) {
								productPricingDetailsBeans.stream().forEach(pro -> {
									if (pro.getArcDetailsBean() == null
											|| pro.getArcDetailsBean().getComponentDetailsBeans() == null
											|| pro.getArcDetailsBean().getComponentDetailsBeans().isEmpty()) {
										return;
									}
									QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSites.stream()
											.filter(si -> si.getId().equals(pro.getSiteId())).findFirst().get();
									addServiceTypeRowToQuote(izosdwanCommericalBeansCof);
									Boolean serviceIdAppended = false;
									Boolean isCpeComponentPresent = false;

									if(cofPdfRequest.getIsIndia()){
										for (ComponentDetailsBean comp : pro.getArcDetailsBean()
												.getComponentDetailsBeans()) {

											setValueAcrcMrc(comp,pro,cofPdfRequest,quoteIzosdwanSite,isCpeComponentPresent,izosdwanCofSiteBean,
													serviceIdAppended,izosdwanCommericalBeansCof);
										}
									}
									else{
										for (ComponentDetailsBean comp : pro.getMrcDetailsBean()
												.getComponentDetailsBeans()) {

											setValueAcrcMrc(comp,pro,cofPdfRequest,quoteIzosdwanSite,isCpeComponentPresent,izosdwanCofSiteBean,
													serviceIdAppended,izosdwanCommericalBeansCof);
										}

									}
									addSubtotalDetailsToQuote(pro, izosdwanCommericalBeansCof, cofPdfRequest);
								});
								addTotalDetails(solutionPricingDetailsBean, izosdwanCommericalBeansCof, cofPdfRequest);
							}
							izosdwanCofSiteBean.setIzosdwanCommericalBeans(izosdwanCommericalBeansCof);
							izosdwanCofSiteBean.setAddress(site.get(0).getServiceSiteAddress());
							izosdwanCofSiteBean.setSiteType(site.get(0).getIzosdwanSiteType());
							// izosdwanCofSiteBean.setSiteProduct(site.get(0).getIzosdwanSiteProduct());

						}

						if (izosdwanCofSiteBean != null && izosdwanCofSiteBean.getIzosdwanCommericalBeans() != null
								&& !izosdwanCofSiteBean.getIzosdwanCommericalBeans().isEmpty()) {
							izosdwanCofSiteBeans.add(izosdwanCofSiteBean);
						}
					});
				}
				getTotalPriceForTheQuoteInPdf(quoteId, cofPdfRequest);
			}
			cofPdfRequest.setIzosdwanPdfSiteBeans(izosdwanCofSiteBeans);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setValueAcrcMrc(ComponentDetailsBean comp, ProductPricingDetailsBean pro, IzosdwanQuotePdfBean cofPdfRequest, QuoteIzosdwanSite quoteIzosdwanSite,
								Boolean isCpeComponentPresent , IzosdwanPdfSiteBean izosdwanCofSiteBean,
								Boolean serviceIdAppended, List<IzosdwanCommericalBean> izosdwanCommericalBeansCof){

		IzosdwanCommericalBean izosdwanCommericalBeanCof = new IzosdwanCommericalBean();
		DecimalFormat df = new DecimalFormat("0.00");
		izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig((comp.getValues()),
				cofPdfRequest.getCurrency()));
		ComponentDetailsBean nrcDetails = pro.getNrcDetailsBean()
				.getComponentDetailsBeans().stream()
				.filter(compn -> compn.getName().equals(comp.getName())).findFirst()
				.get();

		izosdwanCommericalBeanCof.setNrc(getFormattedCurrencyBig(nrcDetails.getValues(),
				cofPdfRequest.getCurrency()));
		izosdwanCommericalBeanCof.setChareableLineItem(comp.getName());

		if (comp.getName().equalsIgnoreCase(FPConstants.CPE.toString()) || comp
				.getName().equalsIgnoreCase(FPConstants.LICENSE_COST.toString())) {
			izosdwanCommericalBeanCof.setOrderType(CommonConstants.EMPTY);
			izosdwanCommericalBeanCof.setActionType(CommonConstants.NEW);
			String str = removeDecimalBandwidth(
					quoteIzosdwanSite.getNewPortBandwidth());
			izosdwanCommericalBeanCof
					.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
			// izosdwanCommericalBeanCof.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
			if (comp.getName().equalsIgnoreCase(FPConstants.CPE.toString())) {
				isCpeComponentPresent = true;
				String cpeType = getCpeType(quoteIzosdwanSite);
//												izosdwanCommericalBeanCof.setChareableLineItem(
//														comp.getName() + "(" + quoteIzosdwanSite.getNewCpe() + ")");
				izosdwanCommericalBeanCof.setChareableLineItem(cpeType);
				izosdwanCommericalBeanCof
						.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
			}
		} else {
			if (!serviceIdAppended) {
				izosdwanCommericalBeanCof.setServiceId(
						quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
				serviceIdAppended = true;
			}
			if (comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)
					|| comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)
					|| comp.getName().equalsIgnoreCase(
					ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM)
					|| comp.getName().equalsIgnoreCase(
					ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN)) {
				String orderCategory = getAttributeValue(quoteIzosdwanSite.getId(),
						IzosdwanCommonConstants.SITE_PROPERTIES,
						IzosdwanCommonConstants.ORDER_CATEGORY);
				String ordersubCategory = getAttributeValue(quoteIzosdwanSite.getId(),
						IzosdwanCommonConstants.SITE_PROPERTIES,
						IzosdwanCommonConstants.ORDER_SUB_CATEGORY);
				izosdwanCommericalBeanCof.setOrderType(orderCategory
						+ ((ordersubCategory != null && !ordersubCategory.isEmpty())
						? ("-" + ordersubCategory)
						: ""));
				String newBw = removeDecimalBandwidth(
						quoteIzosdwanSite.getNewPortBandwidth());
				String oldBw = removeDecimalBandwidth(
						quoteIzosdwanSite.getOldPortBandwidth());
				if (newBw.equalsIgnoreCase(oldBw)) {
					izosdwanCommericalBeanCof
							.setActionType(IzosdwanCommonConstants.EXISTING_BANDWIDTH);
				} else {
					izosdwanCommericalBeanCof
							.setActionType(MACDConstants.CHANGE_BANDWIDTH);
				}
				String str = removeDecimalBandwidth(newBw);
				izosdwanCommericalBeanCof
						.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
//												if(comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)){
//													izosdwanCommericalBeanCof.setChareableLineItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
//												}
//												else {
//													izosdwanCommericalBeanCof
//													.setChareableLineItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
//												}
			}

			if (comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE)
					|| comp.getName().equalsIgnoreCase(
					ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM)) {
				String orderCategory = getAttributeValue(quoteIzosdwanSite.getId(),
						IzosdwanCommonConstants.SITE_PROPERTIES,
						IzosdwanCommonConstants.ORDER_CATEGORY);
				String ordersubCategory = getAttributeValue(quoteIzosdwanSite.getId(),
						IzosdwanCommonConstants.SITE_PROPERTIES,
						IzosdwanCommonConstants.ORDER_SUB_CATEGORY);
				izosdwanCommericalBeanCof.setOrderType(orderCategory
						+ ((ordersubCategory != null && !ordersubCategory.isEmpty())
						? ("-" + ordersubCategory)
						: ""));
				String newBw = removeDecimalBandwidth(
						quoteIzosdwanSite.getNewLastmileBandwidth());
				String oldBw = removeDecimalBandwidth(
						quoteIzosdwanSite.getOldLastmileBandwidth());
				if (newBw.equalsIgnoreCase(oldBw)) {
					izosdwanCommericalBeanCof
							.setActionType(IzosdwanCommonConstants.EXISTING_BANDWIDTH);
				} else {
					izosdwanCommericalBeanCof
							.setActionType(MACDConstants.CHANGE_BANDWIDTH);
				}
				String str = removeDecimalBandwidth(newBw);
				izosdwanCommericalBeanCof
						.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
//												izosdwanCommericalBeanCof
//												.setChareableLineItem(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
			}
//											izosdwanCommericalBeanCof.setActionType(MACDConstants.CHANGE_BANDWIDTH);
//											izosdwanCommericalBeanCof.setBandwidth(quoteIzosdwanSite
//													.getNewPortBandwidth().concat(IzosdwanCommonConstants.MBPS));
//											String str = removeDecimalBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
//                                            izosdwanCommericalBeanCof.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
		}

		if (pro.getProductName().equals(IzosdwanCommonConstants.BYONI)) {
			izosdwanCommericalBeanCof.setProduct("IZO SDWAN");
		} else {
			izosdwanCommericalBeanCof.setProduct(pro.getProductName());
		}
		izosdwanCommericalBeanCof.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCof);
		if (isCpeComponentPresent && pro.getProductName()
				.equals(quoteIzosdwanSite.getIzosdwanSiteProduct())) {
			IzosdwanCommericalBean izosdwanCommericalBeanCofCpe = new IzosdwanCommericalBean();
//											if(pro.getProductName().equals(IzosdwanCommonConstants.BYONI)) {
//	                                        	 izosdwanCommericalBeanCof.setProduct("IZO SDWAN");
//	                                        }else {
//	                                        	 izosdwanCommericalBeanCof.setProduct(quoteIzosdwanSite.getIzosdwanSiteProduct());
//	                                        }
			izosdwanCommericalBeanCofCpe
					.setProduct(quoteIzosdwanSite.getIzosdwanSiteProduct());
			if (!serviceIdAppended) {
				izosdwanCommericalBeanCofCpe.setServiceId(
						quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
				serviceIdAppended = true;
			}
			izosdwanCommericalBeanCofCpe.setActionType(IzosdwanCommonConstants.REMOVE);
			izosdwanCommericalBeanCofCpe
					.setChareableLineItem(FPConstants.CPE.toString());

			izosdwanCommericalBeanCofCpe
					.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
			izosdwanCommericalBeanCofCpe.setArc(Double.toString(0D));
			izosdwanCommericalBeanCofCpe.setNrc(Double.toString(0D));
			izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCofCpe);
		}
	}


	/**
	 * @author Madhumiethaa Palanisamy
	 * @param izosdwanSite
	 * @return
	 */
	private String getCpeType(QuoteIzosdwanSite izosdwanSite) {
		String cpeType = "";
		cpeType = getAttributeValue(izosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
				IzosdwanCommonConstants.CPE);
		if (!cpeType.isEmpty()) {
			if (cpeType.contains(IzosdwanCommonConstants.OUTRIGHT_SALE)) {
				cpeType = IzosdwanCommonConstants.OUTRIGHT_CHARGE;
			} else if (cpeType.contains(IzosdwanCommonConstants.RENTAL)) {
				cpeType = IzosdwanCommonConstants.RENTAL_CHARGE;
			}

		}
		return cpeType;
	}

	private void addTotalDetails(SolutionPricingDetailsBean solutionPricingDetailsBean,
			List<IzosdwanCommericalBean> izosdwanCommericalBeansCof, IzosdwanQuotePdfBean cofPdfRequest) {
		DecimalFormat df = new DecimalFormat("0.00");
		IzosdwanCommericalBean izosdwanCommericalBeanCof = new IzosdwanCommericalBean();
		izosdwanCommericalBeanCof.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
		if(cofPdfRequest.getIsIndia()){
			izosdwanCommericalBeanCof
					.setArc(getFormattedCurrencyBig(solutionPricingDetailsBean.getArc(), cofPdfRequest.getCurrency()));
		}else{
			izosdwanCommericalBeanCof
					.setArc(getFormattedCurrencyBig(solutionPricingDetailsBean.getArc().divide(new BigDecimal(12),MathContext.DECIMAL128), cofPdfRequest.getCurrency()));
		}
//		izosdwanCommericalBeanCof
//				.setArc(getFormattedCurrencyBig(solutionPricingDetailsBean.getArc(), cofPdfRequest.getCurrency()));
		izosdwanCommericalBeanCof
				.setNrc(getFormattedCurrencyBig(solutionPricingDetailsBean.getNrc(), cofPdfRequest.getCurrency()));
		izosdwanCommericalBeanCof.setBandwidth(IzosdwanCommonConstants.TOTAL);
		izosdwanCommericalBeanCof.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCof);
	}

	public List<String> getDistinctCpes(Integer quoteId) {
		List<String> cpeNames = new ArrayList<>();

		if (quoteId != null) {

			Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);

			if (quoteDetails != null) {

				ProductSolution solutions = productSolutionRepository
						.findByReferenceIdForIzoSdwan(quoteDetails.getId());
				if (solutions != null) {
					cpeNames = quoteIzosdwanSiteRepository.getDistinctCpeForSdwan(solutions.getId());

				}
			}
		}
		return cpeNames;
	}

	public IzosdwanQuotePdfBean test(Integer quoteId) throws TclCommonException {
		IzosdwanQuotePdfBean izosdwanQuotePdfBean = new IzosdwanQuotePdfBean();
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			constructVariable(quote, izosdwanQuotePdfBean);
			List<IzosdwanPdfSiteBean> izosdwanPdfSiteBeans = new ArrayList<>();
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			if (productSolution != null) {
				List<Integer> locationIds = quoteIzosdwanSiteRepository.getUniqueLocationIds(productSolution.getId());
				if (locationIds != null && !locationIds.isEmpty()) {
					locationIds.stream().forEach(locationId -> {
						IzosdwanPdfSiteBean izosdwanPdfSiteBean = new IzosdwanPdfSiteBean();
						List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
								.findByErfLocSitebLocationIdAndProductSolution(locationId.intValue(), productSolution);
						if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
							try {
								LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(locationId));
								if (StringUtils.isNotBlank(locationResponse)) {
									AddressDetail addressDetail = (AddressDetail) Utils
											.convertJsonToObject(locationResponse, AddressDetail.class);
									addressDetail = validateAddressDetail(addressDetail);
									izosdwanPdfSiteBean.setAddress(StringUtils
											.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo())
											+ CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getLocality())
											+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
											+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState())
											+ CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getCountry())
											+ CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getPincode()));
									// illSolutionSite.setLocationImage(getGoogleMapSnap(addressDetail.getLatLong()));
									izosdwanPdfSiteBean.setCity(StringUtils.trimToEmpty(addressDetail.getCity()));
									izosdwanPdfSiteBean.setCountry(StringUtils.trimToEmpty(addressDetail.getCountry()));
								}
							} catch (Exception e) {
								LOGGER.warn("Error while getting the address for location id {} {}", locationId, e);
							}
							List<Integer> siteIds = quoteIzosdwanSites.stream().map(id -> id.getId())
									.collect(Collectors.toList());
							PricingInformationRequestBean pricingInformationRequestBean = new PricingInformationRequestBean();
							pricingInformationRequestBean.setQuoteId(quoteId);
							pricingInformationRequestBean.setSiteIds(siteIds);
							SolutionPricingDetailsBean solutionPricingDetailsBean = null;
							try {
								solutionPricingDetailsBean = getPriceSiteWise(pricingInformationRequestBean);
							} catch (TclCommonException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (solutionPricingDetailsBean != null) {
								List<IzosdwanCommericalBean> izosdwanCommericalBeans = new ArrayList<>();
								List<ProductPricingDetailsBean> productPricingDetailsBeans = solutionPricingDetailsBean
										.getProductPricingDetailsBeans();
								if (productPricingDetailsBeans != null && !productPricingDetailsBeans.isEmpty()) {
									productPricingDetailsBeans.stream().forEach(pro -> {
										addServiceTypeRow(izosdwanCommericalBeans);
										pro.getArcDetailsBean().getComponentDetailsBeans().stream().forEach(comp -> {
											IzosdwanCommericalBean izosdwanCommericalBean = new IzosdwanCommericalBean();

											izosdwanCommericalBean.setArc(comp.getValues().toString());
											ComponentDetailsBean nrcDetails = pro.getNrcDetailsBean()
													.getComponentDetailsBeans().stream()
													.filter(compn -> compn.getName().equals(comp.getName())).findFirst()
													.get();
											izosdwanCommericalBean.setNrc(nrcDetails.getValues().toString());
											izosdwanCommericalBean.setChareableLineItem(comp.getName());
											QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSites.stream()
													.filter(site -> site.getId().equals(pro.getSiteId())).findFirst()
													.get();

											if (!comp.getName().contains("Lic")) {
												if (pro.getProductName().equals(IzosdwanCommonConstants.BYONI)) {
													izosdwanCommericalBean.setProduct("IZO SDWAN");
												} else {
													izosdwanCommericalBean.setProduct(pro.getProductName());
												}
												izosdwanCommericalBean.setServiceId(
														quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
												izosdwanCommericalBean.setBandwidth(quoteIzosdwanSite
														.getNewPortBandwidth().concat(IzosdwanCommonConstants.MBPS));
												izosdwanCommericalBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
											} else {
												izosdwanCommericalBean
														.setProduct(IzosdwanCommonConstants.IZOSDWAN_NAME);
												izosdwanCommericalBean
														.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
												izosdwanCommericalBean.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
												izosdwanCommericalBean.setActionType("NEW");
											}
											izosdwanCommericalBean.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
											izosdwanCommericalBeans.add(izosdwanCommericalBean);

										});
										addSubtotalDetails(pro, izosdwanCommericalBeans, izosdwanQuotePdfBean);
									});
								}
								izosdwanPdfSiteBean.setIzosdwanCommericalBeans(izosdwanCommericalBeans);
							}
						}

						izosdwanPdfSiteBeans.add(izosdwanPdfSiteBean);
					});
				}
			}
			izosdwanQuotePdfBean.setIzosdwanPdfSiteBeans(izosdwanPdfSiteBeans);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return izosdwanQuotePdfBean;
	}

	private void addSubtotalDetails(ProductPricingDetailsBean pro, List<IzosdwanCommericalBean> izosdwanCommericalBeans,
			IzosdwanQuotePdfBean izosdwanQuotePdfBean) {
		IzosdwanCommericalBean izosdwanCommericalBean = new IzosdwanCommericalBean();
		izosdwanCommericalBean.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setArc(
				getFormattedCurrencyBig(pro.getArcDetailsBean().getArcTcv(), izosdwanQuotePdfBean.getCurrency()));
		izosdwanCommericalBean.setNrc(
				getFormattedCurrencyBig(pro.getNrcDetailsBean().getNrcTcv(), izosdwanQuotePdfBean.getCurrency()));
		izosdwanCommericalBean.setBandwidth(IzosdwanCommonConstants.SUB_TOTAL);
		izosdwanCommericalBean.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeans.add(izosdwanCommericalBean);
	}

	private void addServiceTypeRow(List<IzosdwanCommericalBean> izosdwanCommericalBeans) {
		IzosdwanCommericalBean izosdwanCommericalBean = new IzosdwanCommericalBean();
		izosdwanCommericalBean.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setArc(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setNrc(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBean.setServiceType("Fixed");
		izosdwanCommericalBeans.add(izosdwanCommericalBean);
	}

	/**
	 * Method to validate addressdetail
	 *
	 * @param addressDetail
	 * @return
	 */
	public AddressDetail validateAddressDetail(AddressDetail addressDetail) {
		if (Objects.isNull(addressDetail.getAddressLineOne()))
			addressDetail.setAddressLineOne("");
		if (Objects.isNull(addressDetail.getAddressLineTwo()))
			addressDetail.setAddressLineTwo("");
		if (Objects.isNull(addressDetail.getCity()))
			addressDetail.setCity("");
		if (Objects.isNull(addressDetail.getCountry()))
			addressDetail.setCountry("");
		if (Objects.isNull(addressDetail.getPincode()))
			addressDetail.setPincode("");
		if (Objects.isNull(addressDetail.getLocality()))
			addressDetail.setLocality("");
		if (Objects.isNull(addressDetail.getState()))
			addressDetail.setState("");
		return addressDetail;
	}

	private void constructSiteDetails(IzosdwanQuotePdfBean izosdwanQuotePdfBean, Integer quoteId) {
		try {
			List<IzosdwanPdfSiteBean> izosdwanPdfSiteBeans = new ArrayList<>();
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			if (productSolution != null) {
				List<Integer> locationIds = quoteIzosdwanSiteRepository.getUniqueLocationIds(productSolution.getId());
				if (locationIds != null && !locationIds.isEmpty()) {
					locationIds.stream().forEach(locationId -> {
						IzosdwanPdfSiteBean izosdwanPdfSiteBean = new IzosdwanPdfSiteBean();
						List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
								.findByErfLocSitebLocationIdAndProductSolution(locationId.intValue(), productSolution);
						if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
							try {
								LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(locationId));
								if (StringUtils.isNotBlank(locationResponse)) {
									AddressDetail addressDetail = (AddressDetail) Utils
											.convertJsonToObject(locationResponse, AddressDetail.class);
									addressDetail = validateAddressDetail(addressDetail);
									izosdwanPdfSiteBean.setAddress(StringUtils
											.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo())
											+ CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getLocality())
											+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
											+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState())
											+ CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getCountry())
											+ CommonConstants.SPACE
											+ StringUtils.trimToEmpty(addressDetail.getPincode()));
									// illSolutionSite.setLocationImage(getGoogleMapSnap(addressDetail.getLatLong()));
									izosdwanPdfSiteBean.setCity(StringUtils.trimToEmpty(addressDetail.getCity()));
									izosdwanPdfSiteBean.setCountry(StringUtils.trimToEmpty(addressDetail.getCountry()));
								}
							} catch (Exception e) {
								LOGGER.warn("Error while getting the address for location id {} {}", locationId, e);
							}
							List<Integer> siteIds = quoteIzosdwanSites.stream().map(id -> id.getId())
									.collect(Collectors.toList());
							PricingInformationRequestBean pricingInformationRequestBean = new PricingInformationRequestBean();
							pricingInformationRequestBean.setQuoteId(quoteId);
							pricingInformationRequestBean.setSiteIds(siteIds);
							SolutionPricingDetailsBean solutionPricingDetailsBean = null;
							try {
								solutionPricingDetailsBean = getPriceSiteWise(pricingInformationRequestBean);
							} catch (TclCommonException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (solutionPricingDetailsBean != null) {
								List<IzosdwanCommericalBean> izosdwanCommericalBeans = new ArrayList<>();
								List<ProductPricingDetailsBean> productPricingDetailsBeans = solutionPricingDetailsBean
										.getProductPricingDetailsBeans();
								if (productPricingDetailsBeans != null && !productPricingDetailsBeans.isEmpty()) {
									productPricingDetailsBeans.stream().forEach(pro -> {
										if (pro.getArcDetailsBean() == null
												|| pro.getArcDetailsBean().getComponentDetailsBeans() == null
												|| pro.getArcDetailsBean().getComponentDetailsBeans().isEmpty()) {
											return;
										}
										addServiceTypeRow(izosdwanCommericalBeans);
										pro.getArcDetailsBean().getComponentDetailsBeans().stream().forEach(comp -> {
											IzosdwanCommericalBean izosdwanCommericalBean = new IzosdwanCommericalBean();

											izosdwanCommericalBean.setArc(getFormattedCurrencyBig(comp.getValues(),
													izosdwanQuotePdfBean.getCurrency()));
											ComponentDetailsBean nrcDetails = pro.getNrcDetailsBean()
													.getComponentDetailsBeans().stream()
													.filter(compn -> compn.getName().equals(comp.getName())).findFirst()
													.get();
											izosdwanCommericalBean.setNrc(getFormattedCurrencyBig(
													nrcDetails.getValues(), izosdwanQuotePdfBean.getCurrency()));
											izosdwanCommericalBean.setChareableLineItem(comp.getName());
											QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSites.stream()
													.filter(site -> site.getId().equals(pro.getSiteId())).findFirst()
													.get();
											izosdwanCommericalBean.setBandwidth(quoteIzosdwanSite.getNewPortBandwidth()
													.concat(IzosdwanCommonConstants.MBPS));
											if (!comp.getName().contains("Lic")) {
												izosdwanCommericalBean.setProduct(pro.getProductName());
												izosdwanCommericalBean.setServiceId(
														quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
												izosdwanCommericalBean.setActionType(MACDConstants.CHANGE_BANDWIDTH);
												izosdwanCommericalBean.setBandwidth(quoteIzosdwanSite
														.getNewPortBandwidth().concat(IzosdwanCommonConstants.MBPS));
											} else {
												izosdwanCommericalBean
														.setProduct(IzosdwanCommonConstants.IZOSDWAN_NAME);
												izosdwanCommericalBean
														.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
												izosdwanCommericalBean.setActionType("NEW");
												izosdwanCommericalBean.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
											}
											izosdwanCommericalBean.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
											izosdwanCommericalBeans.add(izosdwanCommericalBean);

										});
										addSubtotalDetails(pro, izosdwanCommericalBeans, izosdwanQuotePdfBean);
									});
								}
								izosdwanPdfSiteBean.setIzosdwanCommericalBeans(izosdwanCommericalBeans);
							}
						}
						if (izosdwanPdfSiteBean != null && izosdwanPdfSiteBean.getIzosdwanCommericalBeans() != null
								&& !izosdwanPdfSiteBean.getIzosdwanCommericalBeans().isEmpty()) {
							izosdwanPdfSiteBeans.add(izosdwanPdfSiteBean);
						}
					});
				}
				getTotalPriceForTheQuoteInPdf(quoteId, izosdwanQuotePdfBean);
			}
			izosdwanQuotePdfBean.setIzosdwanPdfSiteBeans(izosdwanPdfSiteBeans);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getTotalPriceForTheQuoteInPdf(Integer quoteId, IzosdwanQuotePdfBean izosdwanQuotePdfBean)
			throws TclCommonException {
		QuotePricingDetailsBean quotePricingDetailsBean = getPriceInformationForTheQuote(quoteId);
		izosdwanQuotePdfBean.setArcTcv(
				getFormattedCurrencyBig(quotePricingDetailsBean.getArc(), izosdwanQuotePdfBean.getCurrency()));
		izosdwanQuotePdfBean.setNrcTcv(
				getFormattedCurrencyBig(quotePricingDetailsBean.getNrc(), izosdwanQuotePdfBean.getCurrency()));
		izosdwanQuotePdfBean
				.setTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getTcv(), izosdwanQuotePdfBean.getCurrency()));
	}

	@Transactional
	public void updateCurrency(Integer quoteId, Integer quoteToLeId, String inputCurrency) throws TclCommonException {
		try {
			String existingCurrency = null;
			if (Objects.isNull(quoteId) || Objects.isNull(quoteToLeId) || StringUtils.isEmpty(inputCurrency)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			inputCurrency = inputCurrency.toUpperCase();
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				existingCurrency = quoteToLe.get().getCurrencyCode();
				if (!existingCurrency.equalsIgnoreCase(inputCurrency)) {
					Quote quote = quoteToLe.get().getQuote();
					updateQuoteToLeCurrencyValues(quote, inputCurrency, existingCurrency);
					updateQuoteIllSitesCurrencyValues(quoteToLe.get(), inputCurrency, existingCurrency);
					updateQuotePriceCurrencyValues(quote, inputCurrency, existingCurrency);
					updatePaymentCurrencyWithInputCurrency(quoteToLe.get(), inputCurrency);
				}

				try {
					bundleOmsSfdcService.processUpdateProduct(quoteToLe.get());
					LOGGER.info("Trigger update product sfdc");
				} catch (TclCommonException e) {
					LOGGER.info("Error in updating sfdc with pricing");
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	/**
	 *
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuoteToLeCurrencyValues(Quote quote, String inputCurrency, String existingCurrency) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		for (QuoteToLe quoteToLe : quoteToLes) {
			quoteToLe.setFinalArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getFinalArc()));
			quoteToLe.setFinalMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getFinalMrc()));
			quoteToLe.setFinalNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getFinalNrc()));
			quoteToLe.setProposedArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getProposedArc()));
			quoteToLe.setProposedMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getProposedMrc()));
			quoteToLe.setProposedNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getProposedNrc()));
			quoteToLe.setTotalTcv(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getTotalTcv()));
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Method to set termInMonths
	 *
	 * @param context
	 * @param quoteToLe
	 */
	private String setTermInMonths(QuoteToLe quoteToLe) {
		if (quoteToLe.getTermInMonths() != null) {
			return String.valueOf(getMonthsforOpportunityTerms(quoteToLe.getTermInMonths()));
		}
		return "0";
	}

	/**
	 * Method to convert year into months
	 *
	 * @param termPeriod
	 * @return
	 */
	private Integer getMonthsforOpportunityTerms(String termPeriod) {
		String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
		Integer month = Integer.valueOf(reg[0]);
		if (reg.length > 0) {
			if (termPeriod.contains("year")) {
				return month * 12;
			}
		}
		return month;
	}

	/**
	 * Method to get site components
	 *
	 * @param illSite
	 * @return
	 */
	public List<QuoteProductComponent> getSiteComponents(QuoteIllSite illSite) {
		List<QuoteProductComponent> components = new ArrayList<>();
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(illSite.getId(), QuoteConstants.GVPN_SITES.toString());

		quoteProductComponents.stream().forEach(quoteProductComponent -> {
			if (quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.LAST_MILE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
							.equalsIgnoreCase(FPConstants.CPE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
							.equalsIgnoreCase(FPConstants.VPN_PORT.toString())) {
				components.add(quoteProductComponent);
			}
		});
		return components;
	}

	/**
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuoteIllSitesCurrencyValues(QuoteToLe quoteToLe, String inputCurrency, String existingCurrency) {
		Double totalMrc = 0.0;
		Double totalNrc = 0.0;
		Double totalArc = 0.0;

		for (QuoteToLeProductFamily quoteLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
			for (ProductSolution prodSolution : quoteLeProdFamily.getProductSolutions()) {
				for (QuoteIllSite illSite : prodSolution.getQuoteIllSites()) {
					List<QuoteProductComponent> components = getSiteComponents(illSite);

					Double siteTotalMrc = 0.0;
					Double siteTotalNrc = 0.0;
					Double siteTotalArc = 0.0;

					for (QuoteProductComponent component : components) {
						QuotePrice attrPrice = quotePriceRepository.findByReferenceId(component.getId().toString());
						if (Objects.nonNull(attrPrice)) {
							if (Objects.nonNull(attrPrice.getEffectiveMrc())) {
								siteTotalMrc = siteTotalMrc + Utils.setPrecision(attrPrice.getEffectiveMrc(), 2);
							}
							if (Objects.nonNull(attrPrice.getEffectiveNrc())) {
								siteTotalNrc = siteTotalNrc + Utils.setPrecision(attrPrice.getEffectiveNrc(), 2);
							}
							if (Objects.nonNull(attrPrice.getEffectiveArc())) {
								siteTotalArc = siteTotalArc + Utils.setPrecision(attrPrice.getEffectiveArc(), 2);
							}
						}
					}

					illSite.setArc(siteTotalArc);
					illSite.setMrc(siteTotalMrc);
					illSite.setNrc(siteTotalNrc);
					illSite.setArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, siteTotalArc));
					illSite.setMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, siteTotalMrc));
					illSite.setNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, siteTotalNrc));

					Double contractTerm = Double.parseDouble(this.setTermInMonths(quoteToLe));
					if (Objects.nonNull(illSite.getMrc()) && Objects.nonNull(illSite.getNrc())) {
						// Double tcv=(contractTerm*Utils.setPrecision(illSite.getMrc(),
						// 2))+Utils.setPrecision(illSite.getNrc(), 2);
						illSite.setTcv(
								omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getTcv()));

					}
					illSiteRepository.save(illSite);
					totalMrc = totalMrc + illSite.getMrc();
					totalNrc = totalNrc + illSite.getNrc();
					totalArc = totalArc + illSite.getArc();
				}

			}

		}

		if (Objects.nonNull(quoteToLe.getQuote()) && Objects.nonNull(quoteToLe.getQuote().getQuoteCode())) {
			if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
				quoteToLe.setFinalMrc(totalMrc);
				quoteToLe.setFinalNrc(totalNrc);
				quoteToLe.setFinalArc(totalArc);
				quoteToLe.setProposedArc(totalArc);
				quoteToLe.setProposedMrc(totalMrc);
				quoteToLe.setProposedNrc(totalNrc);
				Double contractTerm = Double.parseDouble(this.setTermInMonths(quoteToLe));
				Double totalTcv = (contractTerm * Utils.setPrecision(quoteToLe.getFinalMrc(), 2))
						+ Utils.setPrecision(quoteToLe.getFinalNrc(), 2);
				quoteToLe.setTotalTcv(totalTcv);
				quoteToLeRepository.save(quoteToLe);
			}
		}
	}

	/**
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuotePriceCurrencyValues(Quote quote, String inputCurrency, String existingCurrency) {

		List<QuotePrice> quotePrices = quotePriceRepository.findByQuoteId(quote.getId());
		for (QuotePrice quotePrice : quotePrices) {
			quotePrice.setCatalogArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getCatalogArc()));
			quotePrice.setCatalogMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getCatalogMrc()));
			quotePrice.setCatalogNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getCatalogNrc()));
			quotePrice.setComputedArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getComputedArc()));
			quotePrice.setComputedMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getComputedMrc()));
			quotePrice.setComputedNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getComputedNrc()));
			quotePrice.setEffectiveArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getEffectiveArc()));
			quotePrice.setEffectiveMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getEffectiveMrc()));
			quotePrice.setEffectiveNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getEffectiveNrc()));
			quotePrice.setEffectiveUsagePrice(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
					quotePrice.getEffectiveUsagePrice()));
			quotePrice.setMinimumArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getMinimumArc()));
			quotePrice.setMinimumMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getMinimumMrc()));
			quotePrice.setMinimumNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getMinimumNrc()));
			quotePriceRepository.save(quotePrice);
		}
	}

	/**
	 * @author Thamizhselvi Perumal Method to update Payment Currency attribute as
	 *         input currency
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @throws TclCommonException
	 */
	public void updatePaymentCurrencyWithInputCurrency(QuoteToLe quotele, String inputCurrency) {
		quotele.setCurrencyCode(inputCurrency);
		quoteToLeRepository.save(quotele);
	}

	/**
	 * processMailAttachment method is used to prepare the quote PDF for mail
	 * attachment.
	 *
	 * @param email
	 * @param quoteId
	 * @return ServiceResponse
	 * @throws TclCommonException
	 */
	public ServiceResponse processMailAttachment(String email, Integer quoteId) throws TclCommonException {
		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (Objects.isNull(email) || !Utils.isValidEmail(email)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String quoteHtml = processQuoteHtml(quoteId);
			String fileName = "Quote_" + quoteId + ".pdf";
			notificationService.processShareQuoteNotification(email,
					Base64.getEncoder().encodeToString(quoteHtml.getBytes()), userInfoUtils.getUserFullName(), fileName,
					IzosdwanCommonConstants.IZOSDWAN_NAME);
			fileUploadResponse.setStatus(Status.SUCCESS);
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	public String processQuoteHtml(Integer quoteId) throws TclCommonException {
		String html = null;
		try {
			LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			html = getQuoteHtml(quote);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return html;
	}

	public SIServiceDetailDataBean getOldQuotesNRCARC(Integer orderId, String tpsId) throws TclCommonException {
		SIServiceDetailDataBean sIServiceDetailDataBean = null;
		try {

			SIOrderDataBean sIOrderDataBean = macdUtils.getSiOrderData(String.valueOf(orderId));
			Optional<SIServiceDetailDataBean> serviceDetailBean = sIOrderDataBean.getServiceDetails().stream()
					.filter(sId -> sId.getTpsServiceId().equals(tpsId)).findFirst();
			if (Objects.nonNull(serviceDetailBean)) {
				sIServiceDetailDataBean = serviceDetailBean.get();
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return sIServiceDetailDataBean;
	}

	/**
	 *
	 * Persist byon details in sites
	 *
	 * @param quoteId
	 * @return
	 */
	public String uploadByonSitesAgainstTheQuote(Integer quoteId) {
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(quote);
			if (quote != null && quoteToLe != null && !quoteToLe.isEmpty()) {
				ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
				User user = null;
				user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
				List<IzoSdwanSiteDetails> siteDet = getSiteDetails();
				List<IzoSdwanCpeDetails> cpeDetails = getCpeDetails(null, null, null);
				Map<String, ProductAttributeMaster> productAttributeMasterMap = new HashMap<>();
				List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository.findAll();
				if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
					productAttributeMasters.stream().forEach(master -> {
						productAttributeMasterMap.put(master.getName(), master);
					});
				}
				MstProductComponent mstProductComponent = getProductComponentMasterDetail(
						IzosdwanCommonConstants.SITE_PROPERTIES);
				List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
						.findByStatusAndQuote_id(IzosdwanCommonConstants.COMPLETED, quote.getId());
				// create isByonSfdcPrdtCreated
				createIzosdwanAttributes(quote, IzosdwanCommonConstants.IS_CREATE_PRODUCT_BYON, "false");
				if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
					QuoteIzoSdwanAttributeValues quoteIzoSdwanAttributeValues = new QuoteIzoSdwanAttributeValues();
					List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesList = quoteIzoSdwanAttributeValuesRepository
							.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.IS_CREATE_PRODUCT_BYON, quoteId);
					if (quoteIzoSdwanAttributeValuesList != null && !quoteIzoSdwanAttributeValuesList.isEmpty()
							&& quoteIzoSdwanAttributeValuesList.get(0).getAttributeValue().equalsIgnoreCase("false")) {
						bundleOmsSfdcService.processProductServiceForSolution(quoteToLe.get(0), productSolution,
								quoteToLe.get(0).getTpsSfdcOptyId(), IzosdwanCommonConstants.BYON_INTERNET_PRODUCT,false);
						quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesList.get(0);
						quoteIzoSdwanAttributeValues.setAttributeValue("true");
						quoteIzoSdwanAttributeValuesRepository.save(quoteIzoSdwanAttributeValues);
					}
					updateByonDetailsToSite(quote, productSolution, user, cpeDetails, siteDet, mstProductComponent,
							productAttributeMasterMap, quoteIzosdwanByonUploadDetails);
					updateByonUploadDetailsStatus(IzosdwanCommonConstants.MIGRATED, quoteIzosdwanByonUploadDetails);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while peristing BYON Sites {}", e);
		}
		return ResponseResource.RES_SUCCESS;
	}

	/**
	 * @author mpalanis
	 * @param quote
	 * @param attributeName
	 * @param attributeValue
	 */
	private void createIzosdwanAttributes(Quote quote, String attributeName, String attributeValue) {
		List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesList = quoteIzoSdwanAttributeValuesRepository
				.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.IS_CREATE_PRODUCT_BYON, quote.getId());
		if (quoteIzoSdwanAttributeValuesList == null || quoteIzoSdwanAttributeValuesList.isEmpty()) {
			QuoteIzoSdwanAttributeValues quoteIzoSdwanAttributeValues = new QuoteIzoSdwanAttributeValues();
			quoteIzoSdwanAttributeValues.setAttributeValue(attributeValue);
			quoteIzoSdwanAttributeValues.setDisplayValue(attributeName);
			quoteIzoSdwanAttributeValues.setQuote(quote);
			quoteIzoSdwanAttributeValuesRepository.save(quoteIzoSdwanAttributeValues);
		}
	}

	private void updateByonDetailsToSite(Quote quote, ProductSolution productSolution, User user,
			List<IzoSdwanCpeDetails> allCpeDetails, List<IzoSdwanSiteDetails> siteDet,
			MstProductComponent mstProductComponent, Map<String, ProductAttributeMaster> productAttributeMasterMap,
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails) {

		try {
			if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
				LOGGER.info("BYON for Quote {} ", quote.getQuoteCode());
				quoteIzosdwanByonUploadDetails.stream().forEach(byon -> {
					try {
						if (IzosdwanCommonConstants.BYONSHAREDSITETYPES.contains(byon.getSiteType())) {
							byon.setIsShared("Y");
						} else {
							byon.setIsShared("N");
						}
						LOGGER.info("BYON Site Type {}", byon.getSiteType());
						if (byon.getSiteType().contains("Single GVPN Single BYON")
								|| byon.getSiteType().contains("Single IAS Single BYON")) {
							LOGGER.info("Inside BYON + Tata Underlay");
							persistByonSite(user, "Primary", productSolution, byon, quote, allCpeDetails, siteDet,
									mstProductComponent, productAttributeMasterMap, true);
						} else {
							LOGGER.info("Inside Normal BYON Primary case");
							persistByonSite(user, "Primary", productSolution, byon, quote, allCpeDetails, siteDet,
									mstProductComponent, productAttributeMasterMap, false);
							if (byon.getSiteType().contains("Dual") && byon.getSecPortBw() != null) {
								LOGGER.info("Inside Normal BYON Dual Case");
								persistByonSite(user, "Secondary", productSolution, byon, quote, allCpeDetails, siteDet,
										mstProductComponent, productAttributeMasterMap, false);
							}
						}
					} catch (Exception e) {
						LOGGER.error("Error occured while persisting the sites {}", e);
					}
				});
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while persisting the sites {}", e);
		}
	}

	private String cpeSuggestionLogicForByon(String vendorName, String profileName,
			QuoteIzosdwanByonUploadDetail quoteIzosdwanByonUploadDetail, List<IzoSdwanSiteDetails> siteDet,
			List<IzoSdwanCpeDetails> allCpeDetails, List<AddonsBean> addons, String priSec) {

		String cpeName = null;
		LOGGER.info("interface type:{}", quoteIzosdwanByonUploadDetail.getPriInterface());
		Optional<IzoSdwanSiteDetails> siteDetails = siteDet.stream()
				.filter(site -> site.getSiteTypeName().equals(quoteIzosdwanByonUploadDetail.getSiteType())).findFirst();
		IzoSdwanSiteDetails site = siteDetails.get();
		/*
		 * if(Integer.parseInt(siServiceDetailBean.getBandwidth())<2) {
		 * siServiceDetailBean.setBandwidth("2"); }
		 */
		String addon = null;
		if (addons != null && !addons.isEmpty()) {
			addon = addons.get(addons.size() - 1).getCode();
		}
		List<IzoSdwanCpeDetails> cpeDetails = new ArrayList<>();
		if (vendorName != null) {
			cpeDetails = allCpeDetails.stream().filter(cpe -> vendorName.equalsIgnoreCase(cpe.getVendor()))
					.collect(Collectors.toList());
			LOGGER.info("After filtering Vendor Name !! ");
		}
		if (addon != null) {
			final String addonFinal = addon;
			if (addonFinal != null) {
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> addonFinal.equalsIgnoreCase(cpe.getAddon()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> addonFinal.equalsIgnoreCase(cpe.getAddon()))
							.collect(Collectors.toList());
				}
				LOGGER.info("After filtering Addon Name !! ");
			}
		} else {
			if (cpeDetails != null && !cpeDetails.isEmpty()) {
				cpeDetails = cpeDetails.stream().filter(cpe -> StringUtils.isBlank(cpe.getAddon()))
						.collect(Collectors.toList());
			}
		}
		if (profileName != null) {
			if (profileName.equals("Select Secure Premium")) {
				String name = "SECURE_PREMIUM";
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> name.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> name.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				}
			} else {
				if (cpeDetails != null && !cpeDetails.isEmpty()) {
					cpeDetails = cpeDetails.stream().filter(cpe -> profileName.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				} else {
					cpeDetails = allCpeDetails.stream().filter(cpe -> profileName.equalsIgnoreCase(cpe.getProfile()))
							.collect(Collectors.toList());
				}
			}

			LOGGER.info("After filtering Profile Name !! ");
		}
		if (cpeDetails != null && !cpeDetails.isEmpty()) {

			if (quoteIzosdwanByonUploadDetail.getIsShared().equals("Y")) {
				Integer bandwidth = 0;

				LOGGER.info("GET LAST MILE BANDWITH{}", quoteIzosdwanByonUploadDetail.getPriLastmileBw());
				bandwidth = Math.round(Float.parseFloat(quoteIzosdwanByonUploadDetail.getPriLastmileBw()))
						+ Math.round(Float.parseFloat(quoteIzosdwanByonUploadDetail.getSecLastmileBw()));

				cpeName = getUniqueCpes(site, cpeDetails, bandwidth);
				// siServiceDetailBean.getSiCpeBeans().get(0).setSuggestedModel(cpeName);
			} else {

				LOGGER.info("THE SITE BANDWITH IS {}", quoteIzosdwanByonUploadDetail.getPriLastmileBw());
				LOGGER.info("THE SITE BANDWITH IS {}", quoteIzosdwanByonUploadDetail.getSecLastmileBw());
				if (priSec.equalsIgnoreCase("Primary")) {
					Integer bandDet = Math.round(Float.parseFloat(quoteIzosdwanByonUploadDetail.getPriLastmileBw()));
					cpeName = getUniqueCpes(site, cpeDetails, bandDet);
				} else {
					Integer bandDet = Math.round(Float.parseFloat(quoteIzosdwanByonUploadDetail.getSecLastmileBw()));
					cpeName = getUniqueCpes(site, cpeDetails, bandDet);
				}
				// siServiceDetailBean.getSiCpeBeans().get(0).setSuggestedModel(cpeName);

			}

		}
		return cpeName;

	}

	private void updateSiteAttributeForByon(QuoteIzosdwanByonUploadDetail quoteIzosdwanByonUploadDetail,
			QuoteIzosdwanSite quoteIzosdwanSite, Map<String, ProductAttributeMaster> productAttributeMasterMap,
			MstProductFamily mstProductFamily, MstProductComponent mstProductComponent, String portBw, String lastBw,
			String portMode, String interfaceType, String accessType, String priSec,
			ByonBulkUploadDetail byonBulkUploadDetail, String thirdPartyServiceId, String ipAddress, String provider,
			String byonLte, String linkUptime, QuoteIzosdwanByonUploadDetail byon) {

		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(mstProductComponent);
		quoteProductComponent.setReferenceId(quoteIzosdwanSite.getId());
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_SITES);
		quoteProductComponent.setType(quoteIzosdwanSite.getPriSec());
		quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);

		ProductAttributeMaster productAttributeMaster = getProductAttributeMasterByName(
				IzosdwanCommonConstants.PORT_BANDWIDTH, productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, portBw);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, lastBw);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.ACCESS_TYPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, accessType);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.INTERFACE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, interfaceType);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.PORT_MODE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, portMode);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.PRI_SEC,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, priSec);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_SCOPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.FULLY_MANAGED);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.INTERNET_QUALITY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, byon.getInternetQuality());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.BYON_SCOPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, byon.getManagementOption());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.IZOSDWAN_TOPOLOGY,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.FULL_MESH);

//		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.COS_TOPOLOGY,
//				productAttributeMasterMap);
//		updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getTopology());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.THIRDPARTY_SERVICE_ID,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, thirdPartyServiceId);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.THIRDPARTY_IP_ADDRESS,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, ipAddress);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.THIRDPARTY_PROVIDER_NAME,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, provider);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.THIRDPARTY_LINK_UPTIME,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				((linkUptime != null && !linkUptime.contains("%")) ? linkUptime.concat("%") : linkUptime));

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.BYON_4G_LTE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonLte);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_BASIC_CHASSIS,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, quoteIzosdwanSite.getNewCpe());

		if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.ROUTER_COST,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, StringUtils.EMPTY);
		}

		if (byonBulkUploadDetail.getCpe() != null && !byonBulkUploadDetail.getCpe().isEmpty()) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_NAME,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getCpe());
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_DESC,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getCpeDesc());
		}

		if (byonBulkUploadDetail.getNmc() != null && !byonBulkUploadDetail.getNmc().isEmpty()) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.NMC,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getNmc());
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.NMC_DESC,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getNmcDesc());
			if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
				productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.NMC_COST,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster, quoteProductComponent, StringUtils.EMPTY);
			}
		}

		if (byonBulkUploadDetail.getRackmount() != null && !byonBulkUploadDetail.getRackmount().isEmpty()) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.RACKMOUNT,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getRackmount());
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.RACKMOUNT_DESC,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent,
					byonBulkUploadDetail.getRackmountDesc());
			if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
				productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.RACKMOUNT_COST,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster, quoteProductComponent, StringUtils.EMPTY);
			}
		}

		if (byonBulkUploadDetail.getSfp() != null && !byonBulkUploadDetail.getSfp().isEmpty()) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getSfp());
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_DESC,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getSfpDesc());
			if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
				productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_COST,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster, quoteProductComponent, StringUtils.EMPTY);
			}
		}

		if (byonBulkUploadDetail.getSfpplus() != null && !byonBulkUploadDetail.getSfpplus().isEmpty()) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_PLUS,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getSfpplus());
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_PLUS_DESC,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getSfpPlusDesc());
			if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
				productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.SFP_PLUS_COST,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster, quoteProductComponent, StringUtils.EMPTY);
			}
		}

		if (byonBulkUploadDetail.getPowerCord() != null && !byonBulkUploadDetail.getPowerCord().isEmpty()) {
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POWER_CORD,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getPowerCord());
			productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POWERCORD_DESC,
					productAttributeMasterMap);
			updateSiteAttributes(productAttributeMaster, quoteProductComponent,
					byonBulkUploadDetail.getPowerCordDesc());
			if (!quoteIzosdwanSite.getServiceSiteCountry().equalsIgnoreCase("India")) {
				productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.POWER_CORD_COST,
						productAttributeMasterMap);
				updateSiteAttributes(productAttributeMaster, quoteProductComponent, StringUtils.EMPTY);
			}
		}
		/*
		 * productAttributeMaster =
		 * getProductAttributeMasterByName(IzosdwanCommonConstants.ASSISTANCE_REQUIRED,
		 * productAttributeMasterMap); updateSiteAttributes(productAttributeMaster,
		 * quoteProductComponent, IzosdwanCommonConstants.CONFIGURATION_ONLY);
		 */

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_SUPPORT_TYPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.RENTAL_TYPE);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.LICENSE_CONTRACT_TYPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.RENTAL_TYPE);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, IzosdwanCommonConstants.RENTAL_TYPE);

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.L3_PORTS,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				Integer.toString(byonBulkUploadDetail.getL3Ports()));

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.L2_PORTS,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				Integer.toString(byonBulkUploadDetail.getL2Ports()));

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_MAX_BW,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent, byonBulkUploadDetail.getCpeMaxBw());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_MODEL_END_OF_SALE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				byonBulkUploadDetail.getCpeModelEndOfSale());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_MODEL_END_OF_LIFE,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				byonBulkUploadDetail.getCpeModelEndOfLife());

		productAttributeMaster = getProductAttributeMasterByName(IzosdwanCommonConstants.CPE_SHARED_OR_NOT,
				productAttributeMasterMap);
		updateSiteAttributes(productAttributeMaster, quoteProductComponent,
				quoteIzosdwanByonUploadDetail.getIsShared());

		LOGGER.info("Updated the site properities for site ID {}", quoteIzosdwanSite.getId());
	}

	private void persistByonSite(User user, String priSec, ProductSolution productSolution,
			QuoteIzosdwanByonUploadDetail byon, Quote quote, List<IzoSdwanCpeDetails> allCpeDetails,
			List<IzoSdwanSiteDetails> siteDet, MstProductComponent mstProductComponent,
			Map<String, ProductAttributeMaster> productAttributeMasterMap, Boolean isTataUnderlay)
			throws TclCommonException {
		String portBw = null;
		String lastBw = null;
		String portMode = null;
		String interfaceType = null;
		String accessType = null;
		String thirdPartyServiceId = null;
		String ipAddress = null;
		String provider = null;
		String byonLte = null;
		String linkUptime = null;

		// String byonType = null;
		String siteSiteType = null;
		String serviceId = null;
		String primaryServiceId = null;
		QuoteIzosdwanSite site = new QuoteIzosdwanSite();
		if (isTataUnderlay) {
			LOGGER.info("Found a site type with tata underlay");
			Map<String, String> types = findSitePrisecByByonUploadDetails(priSec, siteSiteType, byon);
			if (types != null) {
				priSec = types.get("byon");
				siteSiteType = types.get("site");
			}
			LOGGER.info("Type for BYON {} and Tata {}", priSec, siteSiteType);
			// priSec = byonType;
		}
		if (priSec.equalsIgnoreCase("Primary")) {
			portBw = byon.getPriPortBw();
			lastBw = byon.getPriLastmileBw();
			portMode = byon.getPriPortMode();
			interfaceType = byon.getPriInterface();
			accessType = byon.getPriAccessType();
			thirdPartyServiceId = byon.getPriThirdpartyServiceId();
			ipAddress = byon.getPriThirdpartyIpAddress();
			provider = byon.getPriThirdpartyProvider();
			byonLte = byon.getPriThirdpartyByonLte();
			linkUptime = byon.getPriThirdPartyLinkUptime();

		} else {
			portBw = byon.getSecPortBw();
			lastBw = byon.getSecLastmileBw();
			portMode = byon.getSecPortMode();
			interfaceType = byon.getSecInterface();
			accessType = byon.getSecAccessType();
			thirdPartyServiceId = byon.getSecThirdpartyServiceId();
			ipAddress = byon.getSecThirdpartyIpAddress();
			provider = byon.getSecThirdpartyProvider();
			byonLte = byon.getSecThirdpartyByonLte();
			linkUptime = byon.getSecThirdPartyLinkUptime();
		}
		QuoteIzosdwanSite quoteIzosdwanSite = new QuoteIzosdwanSite();
		ByonBulkUploadDetail bulkUploadDetail = new ByonBulkUploadDetail();
		quoteIzosdwanSite.setArc(0D);
		if (user != null) {
			quoteIzosdwanSite.setCreatedBy(user.getId());
			quoteIzosdwanSite.setUpdatedBy(user.getId());
			quoteIzosdwanSite.setCreatedTime(new Date());
			quoteIzosdwanSite.setUpdatedTime(new Date());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 130); // Adding 130 days
			quoteIzosdwanSite.setEffectiveDate(cal.getTime());
		}
		quoteIzosdwanSite.setIsShared(byon.getIsShared());
		quoteIzosdwanSite.setFpStatus(IzosdwanCommonConstants.FP);
		quoteIzosdwanSite.setFeasibility(CommonConstants.BACTIVE);
		quoteIzosdwanSite.setIsAutoBwUpgraded(CommonConstants.BDEACTIVATE);
		quoteIzosdwanSite.setIsIzo(CommonConstants.BDEACTIVATE);
		quoteIzosdwanSite.setIsTaskTriggered(CommonConstants.INACTIVE);
		quoteIzosdwanSite.setIsTaxExempted(CommonConstants.BDEACTIVATE);
		quoteIzosdwanSite.setIsFeasiblityCheckRequired(CommonConstants.INACTIVE);
		quoteIzosdwanSite.setIsPricingCheckRequired(CommonConstants.ACTIVE);
		quoteIzosdwanSite.setOldPortBandwidth(portBw);
		quoteIzosdwanSite.setNewPortBandwidth(portBw);
		quoteIzosdwanSite.setOldLastmileBandwidth(lastBw);
		quoteIzosdwanSite.setNewLastmileBandwidth(lastBw);
		quoteIzosdwanSite.setErfLocSitebLocationId(byon.getLocationId());
		quoteIzosdwanSite.setIzosdwanSiteType(byon.getSiteType());
//		quoteIzosdwanSite.setManagementType(IzosdwanCommonConstants.FULLY_MANAGED);
		quoteIzosdwanSite.setManagementType(byon.getManagementOption());
		quoteIzosdwanSite.setLatLong(byon.getLatLong());
		quoteIzosdwanSite.setMrc(0D);
		quoteIzosdwanSite.setNrc(0D);
		quoteIzosdwanSite.setProductSolution(productSolution);
		quoteIzosdwanSite.setSiteCode(Utils.generateUid());
		quoteIzosdwanSite.setStatus(CommonConstants.BACTIVE);
		quoteIzosdwanSite.setPriSec(priSec);
		quoteIzosdwanSite.setIzosdwanSiteProduct(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
		quoteIzosdwanSite.setServiceSiteCountry(byon.getCountry());
		String city = "";
		if (byon.getCity() != null) {
			city = byon.getCity().substring(0, 4).toUpperCase();
		} else {
			QuoteIzosdwanSite izosdwanSite = quoteIzosdwanSiteRepository.findByIdAndStatus(byon.getSiteId(),
					CommonConstants.BACTIVE);
			if (izosdwanSite != null) {
				city = izosdwanSite.getErfServiceInventoryTpsServiceId().substring(3, 7);
			}
		}
		serviceId = Utils.generateServiceIdByon(IzosdwanCommonConstants.BYON + city);
		quoteIzosdwanSite.setErfServiceInventoryTpsServiceId(serviceId);
		if (!byon.getSiteType().equalsIgnoreCase(IzosdwanCommonConstants.SINGLE_BYON)
				&& priSec.equalsIgnoreCase(IzosdwanCommonConstants.HEADER_PRIMARY)) {
			quoteIzosdwanSite.setPrimaryServiceId(serviceId);
		} else {
			if (!isTataUnderlay) {
				List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
						.findByErfLocSitebLocationIdAndProductSolution(byon.getLocationId(), productSolution);
				if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
					site = quoteIzosdwanSites.get(0);
				}
			} else {
				site = quoteIzosdwanSiteRepository.findByIdAndStatus(byon.getSiteId(), CommonConstants.BACTIVE);
			}
			primaryServiceId = site.getErfServiceInventoryTpsServiceId();
			quoteIzosdwanSite.setPrimaryServiceId(primaryServiceId);
		}
		ProductOfferingsBean productOfferingsBean = null;
		try {
			productOfferingsBean = Utils.convertJsonToObject(productSolution.getProductProfileData(),
					ProductOfferingsBean.class);

			if (productOfferingsBean != null) {
				String cpeName = cpeSuggestionLogicForByon(quote.getIzosdwanFlavour(),
						productSolution.getMstProductOffering().getProductName(), byon, siteDet, allCpeDetails,
						productOfferingsBean.getAddons(), priSec);
				persistBYONInterface(byon, cpeName, priSec, bulkUploadDetail, allCpeDetails.stream()
						.filter(cpe -> cpe.getCpeName().equals(cpeName)).findFirst().orElse(null));
				quoteIzosdwanSite.setNewCpe(cpeName);

			}
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(byon.getLocationId()));
			if (StringUtils.isNotBlank(locationResponse)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				addressDetail = validateAddressDetail(addressDetail);
				quoteIzosdwanSite.setServiceSiteAddress(StringUtils.trimToEmpty(addressDetail.getAddressLineOne())
						+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getAddressLineTwo())
						+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getLocality())
						+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
						+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState())
						+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCountry())
						+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getPincode()));
				byon.setCity(addressDetail.getCity());
				byon.setCountry(addressDetail.getCountry());
				quoteIzosdwanSite.setServiceSiteCountry(addressDetail.getCountry());
			}
		} catch (TclCommonException e1) {
			LOGGER.warn("Error in getting address details");
		}
		quoteIzosdwanSite = quoteIzosdwanSiteRepository.save(quoteIzosdwanSite);
		String slaValue = getSlaTierValue(byon.getCity(), byon.getCountry(),
				IzosdwanCommonConstants.BYON_INTERNET_PRODUCT, byon.getSiteType(), null, quote.getIzosdwanFlavour());
		if (slaValue != null) {
			LOGGER.info("SLA value for byon site {} is {}", quoteIzosdwanSite.getSiteCode(), slaValue);
			QuoteIzosdwanSiteSla sla = new QuoteIzosdwanSiteSla();
			sla.setQuoteIzosdwanSite(quoteIzosdwanSite);
			sla.setSlaValue(slaValue);
			sla.setSlaMaster(slaMasterRepository.findBySlaName(IzosdwanCommonConstants.SERVICE_AVAILABILITY));
			quoteIzoSdwanSlaRepository.save(sla);
		}
		updateSiteAttributeForByon(byon, quoteIzosdwanSite, productAttributeMasterMap,
				productSolution.getMstProductOffering().getMstProductFamily(), mstProductComponent, portBw, lastBw,
				portMode, interfaceType, accessType, priSec, bulkUploadDetail, thirdPartyServiceId, ipAddress, provider,
				byonLte, linkUptime, byon);
		createQuoteProductComponentIfNotPresent(quoteIzosdwanSite.getId(), quoteIzosdwanSite.getPriSec(),
				FPConstants.CPE.toString(), user, IzosdwanCommonConstants.IZOSDWAN_SITES);
		createQuoteProductComponentIfNotPresent(quoteIzosdwanSite.getId(), quoteIzosdwanSite.getPriSec(),
				FPConstants.LICENSE_COST.toString(), user, IzosdwanCommonConstants.IZOSDWAN_SITES);
		if (isTataUnderlay) {
			updateExistingSiteDetails(byon.getSiteId(), byon, siteSiteType, quoteIzosdwanSite.getNewCpe(),
					productAttributeMasterMap, serviceId, bulkUploadDetail);
		}

	}

	// byon interface details persistance
	private void persistBYONInterface(QuoteIzosdwanByonUploadDetail byon, String cpeName, String priSec,
			ByonBulkUploadDetail bulkUploadDetail, IzoSdwanCpeDetails izoSdwanCpeDetails) {
		List<IzoSdwanCpeBomInterface> cpeBomInterfaces = new ArrayList<>();
		try {
			cpeBomInterfaces = getInterfaceDetails();
			String byonInterface;
			if (priSec.equalsIgnoreCase("Primary")) {
				byonInterface = byon.getPriInterface();
			} else {
				byonInterface = byon.getSecInterface();
			}
			if (cpeName != null) {
				if (cpeBomInterfaces != null && !cpeBomInterfaces.isEmpty()) {
					cpeBomInterfaces = cpeBomInterfaces.stream()
							.filter(cpe -> cpeName.equalsIgnoreCase(cpe.getBomNameCd())).collect(Collectors.toList());
				}
			}
			if (izoSdwanCpeDetails != null) {
				bulkUploadDetail.setL2Ports(izoSdwanCpeDetails.getL2Ports());
				bulkUploadDetail.setL3Ports(izoSdwanCpeDetails.getL3Ports());
				if (izoSdwanCpeDetails.getBandwidth() != null && izoSdwanCpeDetails.getBandwidthRate() != null) {
					bulkUploadDetail.setCpeMaxBw(
							izoSdwanCpeDetails.getBandwidth().toString() + " " + izoSdwanCpeDetails.getBandwidthRate());
				}
			}

			if (!cpeBomInterfaces.isEmpty() && cpeBomInterfaces != null) {
				bulkUploadDetail.setCpeModelEndOfSale(cpeBomInterfaces.get(0).getCpeModelEndOfSale());
				bulkUploadDetail.setCpeModelEndOfLife(cpeBomInterfaces.get(0).getCpeModelEndOfLife());
				for (IzoSdwanCpeBomInterface cpeBomInterface : cpeBomInterfaces) {
					if ((cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP"))
							|| (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP+"))) {
						if (cpeBomInterface.getInterfaceType().equals(byonInterface)) {
							if (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP")) {
								bulkUploadDetail.setSfp(cpeBomInterface.getPhysicalResourceCd());
								bulkUploadDetail.setSfpDesc(cpeBomInterface.getDescription());
							}
							if (cpeBomInterface.getProductCategory().equalsIgnoreCase("SFP+")) {
								bulkUploadDetail.setSfpplus(cpeBomInterface.getPhysicalResourceCd());
								bulkUploadDetail.setSfpPlusDesc(cpeBomInterface.getDescription());
							}
						}

					} else {
						if (cpeBomInterface.getProductCategory().equalsIgnoreCase("CPE")) {
							bulkUploadDetail.setCpe(cpeBomInterface.getPhysicalResourceCd());
							bulkUploadDetail.setCpeDesc(cpeBomInterface.getDescription());
						}
						if (cpeBomInterface.getProductCategory().equalsIgnoreCase("NMC")) {
							bulkUploadDetail.setNmc(cpeBomInterface.getPhysicalResourceCd());
							bulkUploadDetail.setNmcDesc(cpeBomInterface.getDescription());
						}
						if (cpeBomInterface.getProductCategory().equalsIgnoreCase("Rackmount")) {
							bulkUploadDetail.setRackmount(cpeBomInterface.getPhysicalResourceCd());
							if (cpeBomInterface.getPhysicalResourceCd().equals("RackMount Kit/Shelf")) {
								bulkUploadDetail.setRackmountDesc(cpeBomInterface.getDescription());
							} else {
								bulkUploadDetail.setRackmountDesc(cpeBomInterface.getDescription());
							}
						}
						if (cpeBomInterface.getProductCategory().contains("Power")) {
							bulkUploadDetail.setPowerCord(cpeBomInterface.getPhysicalResourceCd());
							bulkUploadDetail.setPowerCordDesc(cpeBomInterface.getDescription());
						}
					}
				}
			}
		} catch (TclCommonException | IllegalArgumentException e) {

		}
	}

	private void updateByonUploadDetailsStatus(String toStatus,
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails) {
		if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
			quoteIzosdwanByonUploadDetails.stream().forEach(byon -> {
				byon.setStatus(toStatus);
			});
			quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
					.saveAll(quoteIzosdwanByonUploadDetails);
			LOGGER.info("BYON details status updated to {} ", toStatus);
		}
	}

	public void triggerFeasibility(FeasibilityBean request) throws TclCommonException {
		LOGGER.info("Inside triggerFeasibility function");
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(request.getLegalEntityId());
		if (quoteToLeEntity.isPresent()) {
			Boolean proceedForFeasibility = true;
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P,
							quoteToLeEntity.get().getQuote().getId());
			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()) {
				LOGGER.info("100 % BYON quote");
				if (quoteIzoSdwanAttributeValues.get(0).getAttributeValue().equalsIgnoreCase("true")) {
					proceedForFeasibility = false;
				}
			}
			LOGGER.info("proceedForFeasibility {}", proceedForFeasibility);
			if (proceedForFeasibility) {
				izosdwanIllPricingAndFeasiblityService.processFeasibility(request.getLegalEntityId());
				izosdwanGvpnPricingAndFeasibilityService.processFeasibility(request.getLegalEntityId(),
						CommonConstants.GVPN);
			} else {
				LOGGER.info("BY passing the feasibilty check!!");
				izosdwanIllPricingAndFeasiblityService.saveProcessState(quoteToLeEntity.get(),
						FPConstants.IS_FP_DONE.toString(), FPConstants.FEASIBILITY.toString(),
						FPConstants.TRUE.toString());// disable the feasible flag
				izosdwanIllPricingAndFeasiblityService.saveProcessState(quoteToLeEntity.get(),
						FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
						FPConstants.TRUE.toString());// disable pricing flag
			}
			/*
			 * LOGGER.info("Triggering SDWAN Price");
			 * izosdwanPricingAndFeasibilityService.triggerCpeCharges(request.
			 * getLegalEntityId(),false);
			 */
			izosdwanIllPricingAndFeasiblityService.recalculate(quoteToLeEntity.get());
		}
	}

	private SiSearchBean constructSiSerachBean(Quote quote, QuoteToLe quoteLe) throws TclCommonException {
		SiSearchBean siSearchBean = new SiSearchBean();
		List<String> countries = new ArrayList<>();
		countries.add("India");
		List<String> products = new ArrayList<>();
		products.add(CommonConstants.IAS);
		products.add(CommonConstants.GVPN);
		siSearchBean.setCountries(countries);
		siSearchBean.setProducts(products);
		siSearchBean.setCustomerId(quote.getCustomer().getErfCusCustomerId().toString());
		List<Integer> leIds = new ArrayList<>();
		if (quoteLe != null && quoteLe.getErfCusCustomerLegalEntityId() != null) {
			leIds.add(quoteLe.getErfCusCustomerLegalEntityId());
		}
		siSearchBean.setLeIds(leIds);
		if (siSearchBean != null) {
			LOGGER.info("Constructed the si request bean {}", Utils.convertObjectToJson(siSearchBean));
		}
		return siSearchBean;
	}

	public String getDefaultBandwidthForCgw(Integer quoteId, List<QuoteIzoSdwanAttributeValue> attributevalue) {
		try {
			String oldDefaultBandwidth = "";
			String defaultPortBandwidth = "";
			Integer suggestedBandwidth = 0;
			Boolean isuserModifiedMigrationBw = false;
			Boolean isuserModifiedHeteroBw = false;
			String userModifiedMigrationBw = "";
			String userModifiedHeteroBw = "";
			if (quoteId != null) {
				Boolean isCloudGatewayAlreadyPresent = false;
				Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
				if (quoteDetails != null) {
					QuoteIzosdwanCgwDetail quoteIzosdwanCgwDetail = new QuoteIzosdwanCgwDetail();
					User user = getUserId(Utils.getSource());
					ProductSolution solutions = productSolutionRepository
							.findByReferenceIdForIzoSdwan(quoteDetails.getId());
					List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
							.findByQuote(quoteDetails);
					Optional<QuoteIzoSdwanAttributeValues> iscgwheteroFlag = attributeValues.stream()
							.filter(s -> s.getDisplayValue().equals(IzosdwanCommonConstants.IS_CGW_HETERO)).findAny();
					if (!iscgwheteroFlag.isPresent()) {
						QuoteIzoSdwanAttributeValues val = new QuoteIzoSdwanAttributeValues();
						val.setDisplayValue(IzosdwanCommonConstants.IS_CGW_HETERO);
						val.setQuote(quoteDetails);
						attributeValues.add(val);
					}
					if (attributevalue != null && !(attributevalue.isEmpty())) {
						for (QuoteIzoSdwanAttributeValue value : attributevalue) {
							if (value.getDisplayValue()
									.equalsIgnoreCase(IzosdwanCommonConstants.CGW_MIGRATION_USERMODIFIED_BANDWIDTH)) {
								isuserModifiedMigrationBw = true;
								userModifiedMigrationBw = value.getAttributeValue();
							}
							if (value.getDisplayValue()
									.equalsIgnoreCase(IzosdwanCommonConstants.CGW_HETERO_BANDWIDTH)) {
								isuserModifiedHeteroBw = true;
								userModifiedHeteroBw = value.getAttributeValue();
							}

						}
					}
					List<QuoteIzosdwanCgwDetail> cgwDetails = new ArrayList<>();
					cgwDetails = quoteIzosdwanCgwDetailRepository.findByQuote(quoteDetails);
					if (attributevalue == null || attributevalue.isEmpty() || !(isuserModifiedMigrationBw)) {
						if (solutions != null) {
							defaultPortBandwidth = quoteIzosdwanSiteRepository
									.getDefaultPortBandwidth(solutions.getId());
							suggestedBandwidth = Math.round(Float.parseFloat(defaultPortBandwidth));
							defaultPortBandwidth = suggestedBandwidth.toString();
							if (cgwDetails != null && !(cgwDetails.isEmpty())) {
								isCloudGatewayAlreadyPresent = true;
								oldDefaultBandwidth = cgwDetails.get(0).getMigrationSystemBw();
							} else {
								quoteIzosdwanCgwDetail.setQuote(quoteDetails);
								quoteIzosdwanCgwDetail.setCreatedBy(user.getId());
								quoteIzosdwanCgwDetail.setCreatedTime(new Date());
								quoteIzosdwanCgwDetail.setUseCase2(IzosdwanCommonConstants.USECASE2);
								quoteIzosdwanCgwDetail.setPrimaryLocation(IzosdwanCommonConstants.PRIMARY_LOCATION);
								quoteIzosdwanCgwDetail.setSecondaryLocation(IzosdwanCommonConstants.SECONDRY_LOCATION);
								cgwDetails.add(quoteIzosdwanCgwDetail);
							}
							if (oldDefaultBandwidth == null) {
								oldDefaultBandwidth = "";
							}
							if (!oldDefaultBandwidth.equals(defaultPortBandwidth)) {
								cgwDetails.get(0).setMigrationSystemBw(defaultPortBandwidth);
								cgwDetails.get(0).setMigrationUserBw(userModifiedMigrationBw);
							}
						}
					} else {
						cgwDetails.get(0).setMigrationUserBw(userModifiedMigrationBw);
					}
					if (isuserModifiedHeteroBw) {
						cgwDetails.get(0).setHetroBw(userModifiedHeteroBw);
					} else {
						String updatedHeteroBandwidth = cgwDetails.get(0).getHetroBw();
						if (updatedHeteroBandwidth == null) {
							cgwDetails.get(0).setHetroBw(userModifiedHeteroBw);
						}
					}
					if (solutions != null) {
						List<String> siteTypes = quoteIzosdwanSiteRepository
								.getDistinctSiteTypesForSdwan(solutions.getId());
						Integer isHetero = 0;
						if (siteTypes != null && !siteTypes.isEmpty()) {
							for (String type : siteTypes) {
								type = trimSiteType(type);
								for (String type1 : siteTypes) {
									type1 = trimSiteType(type1);
									if (!(type.equals(type1))) {
										if ((type.contains(IzosdwanCommonConstants.GVPN_IAS))
												&& (type1.contains(IzosdwanCommonConstants.GVPN_BYON))) {
											continue;
										}
										if ((type.contains(IzosdwanCommonConstants.SIAS)
												|| type.contains(IzosdwanCommonConstants.DIAS))
												|| type.contains(IzosdwanCommonConstants.SBYON)
												|| type.contains(IzosdwanCommonConstants.DBYON)) {
											if (type1.contains(IzosdwanCommonConstants.SGVPN)
													|| type1.contains(IzosdwanCommonConstants.DGVPN)) {
												isHetero = 1;
											}
										}
										if ((type.contains(IzosdwanCommonConstants.SGVPN)
												|| type.contains(IzosdwanCommonConstants.DGVPN))) {
											if ((type1.contains(IzosdwanCommonConstants.SIAS))
													|| (type1.contains(IzosdwanCommonConstants.DIAS))
													|| (type.contains(IzosdwanCommonConstants.SBYON))
													|| (type1.contains(IzosdwanCommonConstants.DBYON))) {
												isHetero = 1;
											}
										}
									}

									if (isHetero == 1) {
										break;
									}
								}
								if (isHetero == 1) {
									break;
								}

							}

						}
						if (isHetero == 1) {
							attributeValues.stream()
									.filter(val -> val.getDisplayValue()
											.contains(IzosdwanCommonConstants.IS_CGW_HETERO))
									.findFirst().get().setAttributeValue("true");
							cgwDetails.get(0).setUseCase4(IzosdwanCommonConstants.USECASE4);
						} else {
							attributeValues.stream()
									.filter(val -> val.getDisplayValue()
											.contains(IzosdwanCommonConstants.IS_CGW_HETERO))
									.findFirst().get().setAttributeValue("false");

						}
					}
					quoteIzoSdwanAttributeValuesRepository.saveAll(attributeValues);
					cgwDetails.get(0).setUpdatedBy(user.getId());
					cgwDetails.get(0).setUpdatedTime(new Date());
					cgwDetails = quoteIzosdwanCgwDetailRepository.saveAll(cgwDetails);
					/*
					 * if(!isCloudGatewayAlreadyPresent) {
					 * persistPriceAndComponentsForCgw(cgwDetails, solutions, quoteDetails); }
					 */
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while peristing BYON Sites {}", e);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private String trimSiteType(String siteType) {
		if (siteType.contains("Single CPE")) {
			siteType = siteType.replace("Single CPE", "").trim();
		} else {
			if (siteType.contains("Dual CPE")) {
				siteType = siteType.replace("Dual CPE", "").trim();
			}
//			if(siteType.contains("Shared CPE")) {
//				siteType=siteType.replace("Shared CPE","").trim();
//			}
		}
		return siteType;
	}

	/**
	 *
	 * Get all attributes present for the quote
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<IzosdwanQuoteAttributesBean> getAllAttributesByQuoteId(Integer quoteId) throws TclCommonException {
		List<IzosdwanQuoteAttributesBean> izosdwanQuoteAttributesBeans = new ArrayList<>();
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		try {
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByQuote(quote);
			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()) {
				quoteIzoSdwanAttributeValues.stream().forEach(detail -> {
					izosdwanQuoteAttributesBeans.add(constructAttributeBean(detail));
				});
			}
		} catch (Exception e) {
			LOGGER.info("Error in getting the attributes");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return izosdwanQuoteAttributesBeans;
	}

	private IzosdwanQuoteAttributesBean constructAttributeBean(
			QuoteIzoSdwanAttributeValues quoteIzoSdwanAttributeValues) {
		IzosdwanQuoteAttributesBean izosdwanQuoteAttributesBean = new IzosdwanQuoteAttributesBean();
		izosdwanQuoteAttributesBean.setId(quoteIzoSdwanAttributeValues.getId());
		izosdwanQuoteAttributesBean.setName(quoteIzoSdwanAttributeValues.getDisplayValue());
		izosdwanQuoteAttributesBean.setValue(quoteIzoSdwanAttributeValues.getAttributeValue());
		return izosdwanQuoteAttributesBean;
	}

	/**
	 *
	 * Get BYON location validation update status
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean getByonLocationValidationStatus(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		try {
			List<String> statusList = new ArrayList<>();
			statusList.add(IzosdwanCommonConstants.OPEN);
			statusList.add(IzosdwanCommonConstants.INPROGRESS);
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
					.findByStatusInAndQuote_id(statusList, quoteId);
			if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
				return false;
			} else {
				quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
						.findByQuote_idAndLocationErrorDetailsIsNotNull(quoteId);
				if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
					List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
							.findByQuote(quote);

					if (!(attributeValues.isEmpty())) {
						persistOrUpdateAttribute(IzosdwanCommonConstants.HASERROR.toString(), attributeValues, quote,
								"true");

					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Error in getting location validation status");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return true;
	}

	private void updateExistingSiteDetails(Integer siteId, QuoteIzosdwanByonUploadDetail quoteIzosdwanByonUploadDetail,
			String type, String cpeName, Map<String, ProductAttributeMaster> productAttributeMasterMap,
			String serviceId, ByonBulkUploadDetail byonBulkUploadDetail) throws TclCommonException {
		LOGGER.info("Updating existing Site details of site id {} and site type of {} and prisec is {}", siteId,
				quoteIzosdwanByonUploadDetail.getSiteType(), type);
		QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSiteRepository.findByIdAndStatus(siteId,
				CommonConstants.BACTIVE);
		if (quoteIzosdwanSite != null) {
			LOGGER.info("Existing site type {} and prisec is {}", quoteIzosdwanSite.getIzosdwanSiteType(),
					quoteIzosdwanSite.getPriSec());
			quoteIzosdwanSite.setIzosdwanSiteType(quoteIzosdwanByonUploadDetail.getSiteType());
			quoteIzosdwanSite.setPriSec(type);
			quoteIzosdwanSite.setIsShared(quoteIzosdwanByonUploadDetail.getIsShared());
			// update primary service id if underlay is secondary
			if (type.equalsIgnoreCase(IzosdwanCommonConstants.HEADER_SECONDARY)) {
				quoteIzosdwanSite.setPrimaryServiceId(serviceId);
			} else {
				quoteIzosdwanSite.setPrimaryServiceId(quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
			}
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(siteId, QuoteConstants.IZOSDWAN_SITES.toString());
			if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
				for (QuoteProductComponent quoteProductComponent : quoteProductComponentList) {
					quoteProductComponent.setType(type);
					quoteProductComponentRepository.save(quoteProductComponent);
					LOGGER.info("Updating the attributes");
					if (quoteProductComponent.getMstProductComponent().getName()
							.equalsIgnoreCase(IzosdwanCommonConstants.SITE_PROPERTIES) && cpeName != null
							&& quoteIzosdwanByonUploadDetail.getIsShared().equals("Y")) {
						LOGGER.info("CPE needs to be changes since Shared type and name is from {} to {}",
								quoteIzosdwanSite.getNewCpe(), cpeName);
						quoteIzosdwanSite.setNewCpe(cpeName);
						ProductAttributeMaster productAttributeMaster = getProductAttributeMasterByName(
								IzosdwanCommonConstants.CPE_BASIC_CHASSIS, productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster, quoteProductComponent, cpeName);
						productAttributeMaster = getProductAttributeMasterByName(
								IzosdwanCommonConstants.CPE_MODEL_END_OF_SALE, productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster, quoteProductComponent,
								byonBulkUploadDetail.getCpeModelEndOfSale());
						productAttributeMaster = getProductAttributeMasterByName(
								IzosdwanCommonConstants.CPE_MODEL_END_OF_LIFE, productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster, quoteProductComponent,
								byonBulkUploadDetail.getCpeModelEndOfLife());
						productAttributeMaster = getProductAttributeMasterByName(
								IzosdwanCommonConstants.CPE_SHARED_OR_NOT, productAttributeMasterMap);
						updateSiteAttributes(productAttributeMaster, quoteProductComponent,
								quoteIzosdwanByonUploadDetail.getIsShared());
					}
				}
			}
			quoteIzosdwanSite = quoteIzosdwanSiteRepository.save(quoteIzosdwanSite);
			LOGGER.info("Updated the site details");
		}
	}

	private Map<String, String> findSitePrisecByByonUploadDetails(String priSec, String siteSiteType,
			QuoteIzosdwanByonUploadDetail quoteIzosdwanByonUploadDetail) {
		QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSiteRepository
				.findByIdAndStatus(quoteIzosdwanByonUploadDetail.getSiteId(), CommonConstants.BACTIVE);
		if (quoteIzosdwanSite != null) {
			if (quoteIzosdwanByonUploadDetail.getPriAccessType() != null
					&& quoteIzosdwanByonUploadDetail.getPriInterface() != null
					&& quoteIzosdwanByonUploadDetail.getPriLastmileBw() != null
					&& quoteIzosdwanByonUploadDetail.getPriPortBw() != null
					&& quoteIzosdwanByonUploadDetail.getPriPortMode() != null) {
				priSec = "Primary";
				siteSiteType = "Secondary";
				quoteIzosdwanByonUploadDetail.setSecLastmileBw(quoteIzosdwanSite.getNewLastmileBandwidth());
			} else {
				siteSiteType = "Primary";
				priSec = "Secondary";
				quoteIzosdwanByonUploadDetail.setPriLastmileBw(quoteIzosdwanSite.getNewLastmileBandwidth());
			}
			Map<String, String> map = new HashMap<>();
			map.put("byon", priSec);
			map.put("site", siteSiteType);
			return map;
		}
		return null;
	}

	/**
	 *
	 * Check whether BYON data are updated or not in case of 100 % BYON
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean checkByonDetailsUploadedOrNot(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			if (quote != null) {
				List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
						.findByQuote_id(quoteId);
				if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
					if (quoteIzosdwanByonUploadDetails.stream()
							.filter(detail -> (detail.getStatus().equals(IzosdwanCommonConstants.MIGRATED))).findAny()
							.isPresent()) {
						return true;
					} else if (quoteIzosdwanByonUploadDetails.stream()
							.filter(detail -> (!detail.getStatus().equals(IzosdwanCommonConstants.COMPLETED))).findAny()
							.isPresent()) {
						return false;
					}
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.info("Error in getting byon data uploaded flag");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return false;
	}

	public List<String> getUniqueCountriesForTheQuote(Integer quoteId) {
		if (quoteId != null) {
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
					.findByProductSolution(productSolution);
			if (quoteIzosdwanSites != null) {
				return quoteIzosdwanSites.stream().filter(site -> site.getServiceSiteCountry() != null)
						.map(site -> site.getServiceSiteCountry()).distinct().collect(Collectors.toList());
			}
		}
		return new ArrayList<String>();
	}

	private void persistPriceAndComponentsForCgw(List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails,
			ProductSolution productSolution, Quote quote) {
		LOGGER.info("Persisting mock components for CGW for quote {}", quote.getQuoteCode());
		quoteIzosdwanCgwDetails.stream().forEach(cgwDetails -> {
			List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(cgwDetails.getId(), IzosdwanCommonConstants.IZOSDWAN_CGW);
			if (quoteProductComponents == null || quoteProductComponents.isEmpty()) {

				if (cgwDetails.getHetroBw() != null) {
					createMockComponentAndPriceForCgw(productSolution, quote, cgwDetails.getId(),
							IzosdwanCommonConstants.CLOUD_GATEWAY_PORT);
				}
				if (cgwDetails.getMigrationUserBw() != null) {
					createMockComponentAndPriceForCgw(productSolution, quote, cgwDetails.getId(),
							IzosdwanCommonConstants.CLOUD_GATEWAY_MIGRATION);
				}
			}
		});
	}

	private void createMockComponentAndPriceForCgw(ProductSolution productSolution, Quote quote, Integer referenceId,
			String componentName) {
		LOGGER.info("Creating component and price for {}", componentName);
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(componentName);
		if (mstProductComponent != null) {
			quoteProductComponent.setMstProductComponent(mstProductComponent);
			quoteProductComponent.setMstProductFamily(productSolution.getMstProductOffering().getMstProductFamily());
			quoteProductComponent.setReferenceId(referenceId);
			quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_CGW);
			quoteProductComponent.setType(IzosdwanCommonConstants.IZOSDWAN_CGW);
			quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
			if (quoteProductComponent != null && quoteProductComponent.getId() != null) {
				LOGGER.info("QPC created with id {}", quoteProductComponent.getId());
				QuotePrice quotePrice = new QuotePrice();
				quotePrice.setEffectiveArc(1000D);
				quotePrice.setEffectiveMrc(1000D);
				quotePrice.setEffectiveNrc(1000D);
				quotePrice.setEffectiveUsagePrice(1000D);
				quotePrice.setMstProductFamily(productSolution.getMstProductOffering().getMstProductFamily());
				quotePrice.setQuoteId(quote.getId());
				quotePrice.setReferenceId(quoteProductComponent.getId().toString());
				quotePrice.setReferenceName(IzosdwanCommonConstants.COMPONENTS);
				quotePriceRepository.save(quotePrice);
				LOGGER.info("Persisted the mock price for qpc {}", quoteProductComponent.getId());
			}

		}
	}

	/**
	 *
	 * This Method is used to get solution level charges for IZO-SDWAN
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<IzosdwanSolutionLevelCharges> getSolutionLevelChargesForIzosdwan(Integer quoteId)
			throws TclCommonException {
		List<IzosdwanSolutionLevelCharges> izosdwanSolutionLevelChargesList = new ArrayList<>();
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<QuoteIzosdwanCgwDetail> cgwDetails = quoteIzosdwanCgwDetailRepository.findByQuote_Id(quoteId);
			if (cgwDetails != null && !cgwDetails.isEmpty()) {
				LOGGER.info("Got CGW details for quote id {}", quoteId);
				Double totalArc = 0D;
				Double totalNrc = 0D;
				QuoteIzosdwanCgwDetail quoteIzosdwanCgwDetail = cgwDetails.get(0);
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndReferenceName(quoteIzosdwanCgwDetail.getId(),
								IzosdwanCommonConstants.IZOSDWAN_CGW);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					LOGGER.info("Got QPC for CGW");
					for (QuoteProductComponent qpc : quoteProductComponents) {

						QuotePrice quotePrice = quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(
								IzosdwanCommonConstants.COMPONENTS, qpc.getId().toString(), quoteId);
						if (quotePrice != null) {
							LOGGER.info("Got Price for QPC {}", qpc.getId());
							if (quotePrice.getEffectiveArc() != null) {
								totalArc += Double.parseDouble(decimalFormat.format(quotePrice.getEffectiveArc()));
							}
							if (quotePrice.getEffectiveNrc() != null) {
								totalNrc += Double.parseDouble(decimalFormat.format(quotePrice.getEffectiveNrc()));
							}
						}
					}
					LOGGER.info("Total ARC is {} and Total NRC is {}", totalArc, totalNrc);
					IzosdwanSolutionLevelCharges izosdwanSolutionLevelCharges = new IzosdwanSolutionLevelCharges();
					izosdwanSolutionLevelCharges.setName(IzosdwanCommonConstants.CLOUD_SERVICE_GATEWAY);
					izosdwanSolutionLevelCharges.setArc(IzosdwanUtils.formatBigDecimal(new BigDecimal(totalArc)));
					izosdwanSolutionLevelCharges.setNrc(IzosdwanUtils.formatBigDecimal(new BigDecimal(totalNrc)));
					izosdwanSolutionLevelChargesList.add(izosdwanSolutionLevelCharges);
				}
			}
			Quote quote = getQuote(quoteId);
			String terms;
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
			if (quoteToLes.stream().findFirst().isPresent()) {
				QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get();
				terms = quoteToLe.getTermInMonths();
				Integer termsInMonths = Integer.parseInt(terms.substring(0, 2));
				List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanValues = quoteIzoSdwanAttributeValuesRepository
						.findByQuote(quote);
				if (checkVproxyExistOrNot(quoteIzoSdwanValues)) {
					LOGGER.info("create Vproxy Pricing details for quote id {}", quoteId);
					List<ProductPricingDetailsBean> productPricingDetailsBeans = new ArrayList<>();
					getVproxyPricingDetails(productPricingDetailsBeans, quoteId, termsInMonths);
					IzosdwanSolutionLevelCharges vproxyDetails = new IzosdwanSolutionLevelCharges();
					vproxyDetails.setName(IzosdwanCommonConstants.VPROXY);
					vproxyDetails.setArc(new BigDecimal(0D));
					vproxyDetails.setNrc(new BigDecimal(0D));
					if (productPricingDetailsBeans != null && !productPricingDetailsBeans.isEmpty()) {
						LOGGER.info("Got Vproxy pricing details for quote id {}", quoteId);
						vproxyDetails.setArc(productPricingDetailsBeans.get(0).getArcDetailsBean().getArcTcv());
						vproxyDetails.setNrc(productPricingDetailsBeans.get(0).getNrcDetailsBean().getNrcTcv());
					}
					izosdwanSolutionLevelChargesList.add(vproxyDetails);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error in getting solution level pricing details", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return izosdwanSolutionLevelChargesList;
	}

	private boolean checkVproxyExistOrNot(List<QuoteIzoSdwanAttributeValues> attributeValues) {
		boolean status = false;
		for (QuoteIzoSdwanAttributeValues val : attributeValues) {
			if (val.getDisplayValue().equals(IzosdwanCommonConstants.ISSPA)
					|| val.getDisplayValue().equals(IzosdwanCommonConstants.ISSWG)) {
				if (val.getAttributeValue().equals("Yes")) {
					status = true;
				}
			}
		}
		return status;
	}

	private boolean checkVproxyExistOrNotDet(List<QuoteIzoSdwanAttributeValues> attributeValues, String profileName) {
		boolean status = false;
		for (QuoteIzoSdwanAttributeValues val : attributeValues) {
			if (val.getDisplayValue().equals(profileName)) {
				if (val.getAttributeValue().equals("Yes")) {
					status = true;
				}
			}

		}
		return status;
	}

	/**
	 *
	 * This Method is used to get solution level break up charges for IZO-SDWAN -
	 * CGW
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SolutionLevelPricingBreakupDetailsBean> getSolutionLevelBreakUpPricingDetailForCGW(Integer quoteId)
			throws TclCommonException {
		List<SolutionLevelPricingBreakupDetailsBean> solutionLevelPricingBreakupDetailsBeans = new ArrayList<>();
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<QuoteIzosdwanCgwDetail> cgwDetails = quoteIzosdwanCgwDetailRepository.findByQuote_Id(quoteId);
			if (cgwDetails != null && !cgwDetails.isEmpty()) {
				LOGGER.info("Got CGW details for quote id {}", quoteId);
				QuoteIzosdwanCgwDetail quoteIzosdwanCgwDetail = cgwDetails.get(0);
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndReferenceName(quoteIzosdwanCgwDetail.getId(),
								IzosdwanCommonConstants.IZOSDWAN_CGW);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					LOGGER.info("Got QPC for CGW");
					for (QuoteProductComponent qpc : quoteProductComponents) {

						QuotePrice quotePrice = quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(
								IzosdwanCommonConstants.COMPONENTS, qpc.getId().toString(), quoteId);
						if (quotePrice != null) {
							LOGGER.info("Got Price for QPC {}", qpc.getId());
							LOGGER.info("ARC is {} and NRC is {}", quotePrice.getEffectiveArc(),
									quotePrice.getEffectiveNrc());
							SolutionLevelPricingBreakupDetailsBean solutionLevelPricingBreakupDetailsBean = new SolutionLevelPricingBreakupDetailsBean();
							solutionLevelPricingBreakupDetailsBean.setActionType(IzosdwanCommonConstants.NEW);
							solutionLevelPricingBreakupDetailsBean
									.setArc(quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
							solutionLevelPricingBreakupDetailsBean
									.setNrc(quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
							if (qpc.getMstProductComponent().getName()
									.equals(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT)) {
								solutionLevelPricingBreakupDetailsBean.setBandwidth(cgwDetails.get(0).getHetroBw());
								solutionLevelPricingBreakupDetailsBean
										.setName(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT_NAME);
							} else if (qpc.getMstProductComponent().getName()
									.equals(IzosdwanCommonConstants.CLOUD_GATEWAY_MIGRATION)) {
								if (cgwDetails.get(0).getMigrationUserBw().isEmpty()) {
									solutionLevelPricingBreakupDetailsBean
											.setBandwidth(cgwDetails.get(0).getMigrationSystemBw());
								} else {
									solutionLevelPricingBreakupDetailsBean
											.setBandwidth(cgwDetails.get(0).getMigrationUserBw());
								}
								solutionLevelPricingBreakupDetailsBean
										.setName(IzosdwanCommonConstants.CLOUD_GATEWAY_MIGRATION_NAME);
							} else {
								solutionLevelPricingBreakupDetailsBean.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
								solutionLevelPricingBreakupDetailsBean.setName(qpc.getMstProductComponent().getName());
							}
							solutionLevelPricingBreakupDetailsBeans.add(solutionLevelPricingBreakupDetailsBean);
						}
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting solution level breakup pricing details", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return solutionLevelPricingBreakupDetailsBeans;
	}

	/**
	 *
	 * Add or modify Quote attributes
	 *
	 * @param izosdwanQuoteAttributesUpdateBean
	 * @return
	 * @throws TclCommonException
	 */
	public String addOrModifyQuoteAttribute(IzosdwanQuoteAttributesUpdateBean izosdwanQuoteAttributesUpdateBean)
			throws TclCommonException {
		if (izosdwanQuoteAttributesUpdateBean == null || izosdwanQuoteAttributesUpdateBean.getQuoteId() == null
				|| !(izosdwanQuoteAttributesUpdateBean.getIzosdwanQuoteAttributesUpdateRequests() != null
						&& !izosdwanQuoteAttributesUpdateBean.getIzosdwanQuoteAttributesUpdateRequests().isEmpty())) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Quote quote = quoteRepository.findByIdAndStatus(izosdwanQuoteAttributesUpdateBean.getQuoteId(),
					CommonConstants.BACTIVE);
			if (quote != null) {
				Map<String, QuoteIzoSdwanAttributeValues> map = new HashMap<>();
				List<QuoteIzoSdwanAttributeValues> attributeToAddOrModify = new ArrayList<>();
				List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
						.findByQuote(quote);
				if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()) {
					quoteIzoSdwanAttributeValues.stream().forEach(values -> {
						map.put(values.getDisplayValue(), values);
					});

				}
				izosdwanQuoteAttributesUpdateBean.getIzosdwanQuoteAttributesUpdateRequests().stream().forEach(bean -> {
					attributeToAddOrModify.add(addOrModifyAttributes(bean.getName(), bean.getValue(), map, quote));
				});
				if (attributeToAddOrModify != null && !attributeToAddOrModify.isEmpty()) {
					quoteIzoSdwanAttributeValuesRepository.saveAll(attributeToAddOrModify);
				}
				List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
				if (quoteToLes != null && !quoteToLes.isEmpty()) {
					quoteToLes.stream().forEach(quoteToLe -> {
						try {
							removeVproxyDetailsIfUnselected(quote, quoteToLe);
						} catch (TclCommonException e) {
							LOGGER.error("Error on removing if any vproxy solution to be removed!", e);
						}
					});
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error while updating quote attributes", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private QuoteIzoSdwanAttributeValues addOrModifyAttributes(String name, String value,
			Map<String, QuoteIzoSdwanAttributeValues> existingAttributesMap, Quote quote) {
		QuoteIzoSdwanAttributeValues quoteIzoSdwanAttributeValues = new QuoteIzoSdwanAttributeValues();
		if (existingAttributesMap.containsKey(name)) {
			quoteIzoSdwanAttributeValues = existingAttributesMap.get(name);
			quoteIzoSdwanAttributeValues.setAttributeValue(value);
		} else {
			quoteIzoSdwanAttributeValues.setAttributeValue(value);
			quoteIzoSdwanAttributeValues.setDisplayValue(name);
			quoteIzoSdwanAttributeValues.setQuote(quote);
		}
		return quoteIzoSdwanAttributeValues;
	}

	public QuoteDetail updateLegalEntityProperties(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			Boolean variationMatrixCheck = false;
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
			constructQuoteLeAttribute(request, omsAttribute, optionalQuoteToLe.get());

			/*
			 * if (optionalQuoteToLe.isPresent()) { if
			 * (Objects.isNull(optionalQuoteToLe.get().getQuoteType()) ||
			 * (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().
			 * getQuoteType())) && optionalQuoteToLe.get().getIsAmended()!=1) { if
			 * (optionalQuoteToLe.get().getStage().equals(QuoteStageConstants.GET_QUOTE.
			 * getConstantCode())) {
			 * optionalQuoteToLe.get().setStage(QuoteStageConstants.CHECKOUT.getConstantCode
			 * ()); quoteToLeRepository.save(optionalQuoteToLe.get()); } }
			 *
			 * }
			 */

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	/**
	 * getMstAttributeMaster used to get the Attribute Master
	 *
	 * @param request
	 * @return
	 */
	private MstOmsAttribute getMstAttributeMaster(UpdateRequest request, User user) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(request.getAttributeName(), (byte) 1);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(user.getUsername());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(request.getAttributeName());
			mstOmsAttribute.setDescription(request.getAttributeValue());
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	/**
	 * constructQuoteLeAttribute used to construct Quote Le attribute
	 *
	 * @param request
	 * @param omsAttribute
	 * @param quoteToLe
	 */
	private void constructQuoteLeAttribute(UpdateRequest request, MstOmsAttribute omsAttribute, QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, request.getAttributeName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setMstOmsAttribute(omsAttribute);
				attrVal.setAttributeValue(request.getAttributeValue());
				attrVal.setDisplayValue(request.getAttributeName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValueRepository.save(attrVal);
			});
		} else {
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(omsAttribute);
			quoteLeAttributeValue.setAttributeValue(request.getAttributeValue());
			quoteLeAttributeValue.setDisplayValue(request.getAttributeName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		}
	}

	/**
	 *
	 * Add vProxy solutions to IZOSDWAN quote
	 *
	 * @author AnandhiV
	 * @param vproxySolutionRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	public String addVproxySolutions(VproxySolutionRequestBean vproxySolutionRequestBean) throws TclCommonException {
		if (!validateVproxySolutionRequest(vproxySolutionRequestBean)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {

			Integer profileCount = 0;
			MstProductFamily mstProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(vproxySolutionRequestBean.getProductName(), CommonConstants.BACTIVE);
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(vproxySolutionRequestBean.getQuoteLeId());
			if (mstProductFamily != null && quoteToLeOpt.isPresent()) {
				QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLeOpt.get().getId(),
								mstProductFamily.getName());
				if (quoteToLeProductFamily == null) {
					quoteToLeProductFamily = new QuoteToLeProductFamily();
					quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
					quoteToLeProductFamily.setQuoteToLe(quoteToLeOpt.get());
					quoteToLeProductFamily = quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
				}
				if (quoteToLeProductFamily != null && vproxySolutionRequestBean.getVproxySolutionsBeans() != null
						&& !vproxySolutionRequestBean.getVproxySolutionsBeans().isEmpty()) {
					List<ProductSolution> existingSolutions = productSolutionRepository
							.findByReferenceIdForVproxy(quoteToLeOpt.get().getQuote().getId());
					if (existingSolutions != null && !existingSolutions.isEmpty()) {
						QuoteDetail quoteDetail = new QuoteDetail();
						quoteDetail.setQuoteId(quoteToLeOpt.get().getQuote().getId());
						removeAllSolutionVproxy(quoteDetail, quoteToLeOpt.get(), false);
					}
					for (VproxySolutionBean vproxySolutionBean : vproxySolutionRequestBean.getVproxySolutionsBeans()) {
						if (vproxySolutionBean.getVproxyProductOfferingBeans() != null) {
							profileCount++;
							MstProductOffering mstProductOffering = mstProductOfferingRepository
									.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,
											vproxySolutionBean.getVproxyProductOfferingBeans().getProductOfferingName(),
											CommonConstants.BACTIVE);
							if (mstProductFamily != null) {
								ProductSolution productSolution = new ProductSolution();
								productSolution.setMstProductOffering(mstProductOffering);
								try {
									productSolution
											.setProductProfileData(Utils.convertObjectToJson(vproxySolutionBean));
								} catch (TclCommonException e) {
									e.printStackTrace();
								}
								productSolution.setSolutionCode(Utils.generateUid());
								productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
								User user = userRepository.findByIdAndStatus(
										quoteToLeOpt.get().getQuote().getCreatedBy(), CommonConstants.ACTIVE);
								List<VProxyAddonsBean> addons = new ArrayList<>();
								if (vproxySolutionBean.getVproxyProductOfferingBeans() != null && vproxySolutionBean
										.getVproxyProductOfferingBeans().getvProxyAddonsBeans() != null) {
									addons = vproxySolutionBean.getVproxyProductOfferingBeans().getvProxyAddonsBeans();
								}
								String totalNoOfUsers = vproxySolutionBean.getVproxyQuestionnaireDets().stream()
										.filter(quest -> quest.getName().equalsIgnoreCase("Total No. Of Users"))
										.map(quest -> quest.getSelectedValue()).findFirst().orElse(null);
								productSolution = productSolutionRepository.save(productSolution);
								izosdwanPricingAndFeasibilityService.persistVproxyComponent(
										quoteToLeOpt.get().getQuote(), productSolution, user, quoteToLeOpt.get(),
										vproxySolutionBean.getVproxyProductOfferingBeans().getSolutionName(), addons,
										totalNoOfUsers);
								if (profileCount == 1) {
									bundleOmsSfdcService.processProductServiceForSolution(quoteToLeOpt.get(),
											productSolution, quoteToLeOpt.get().getTpsSfdcOptyId(), "Vproxy",false);

								}
							}
						}
					}

				}

			}

		} catch (Exception e) {
			LOGGER.error("Error occured while adding vproxy solutions ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private Boolean validateVproxySolutionRequest(VproxySolutionRequestBean vproxySolutionRequestBean) {
		if (vproxySolutionRequestBean.getQuoteId() == null || vproxySolutionRequestBean.getQuoteLeId() == null
				|| vproxySolutionRequestBean.getProductName() == null) {
			return false;
		}
		return true;
	}

	public List<VproxySolutionsBean> getVproxyProfileDetails(Integer quoteId) throws TclCommonException {
		Quote quote = getQuote(quoteId);
		List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository.findByQuote(quote);
		List<VproxyAttributeDetails> vproxyAttributeDetails = new ArrayList<>();
		List<VproxySolutionsBean> response = new ArrayList<>();
		for (QuoteIzoSdwanAttributeValues attributeVal : attributeValues) {
			VproxyAttributeDetails vproxyVal = new VproxyAttributeDetails();
			vproxyVal.setAttributeName(attributeVal.getDisplayValue());
			vproxyVal.setAttributeValue(attributeVal.getAttributeValue());
			vproxyAttributeDetails.add(vproxyVal);

		}
		LOGGER.info("THE DETAILS {}", vproxyAttributeDetails);
		String vproxyProfileDet = (String) mqUtils.sendAndReceive(vproxyProfileDetails,
				Utils.convertObjectToJson(vproxyAttributeDetails));
		LOGGER.info("Response from the product catalog queue call is {}", vproxyProfileDet);
		response = GscUtils.fromJson(vproxyProfileDet, new TypeReference<List<VproxySolutionsBean>>() {
		});
		// sfdc create product trigger point
		// createProduct(quote.getId());
		return response;
	}

	/*
	 * private void createProduct(Integer quoteId) throws TclCommonException {
	 * //SFDC Trigger Point for create product Quote quoteDetails =
	 * quoteRepository.findByIdAndStatus(quoteId, (byte) 1); ProductSolution
	 * solutionDetails=productSolutionRepository.findByReferenceIdForIzoSdwan(
	 * quoteId); List<QuoteIzoSdwanAttributeValues>
	 * createProduct=quoteIzoSdwanAttributeValuesRepository.
	 * findByDisplayValueAndQuote("CREATEPRODUCT", quoteDetails); if
	 * (createProduct.isEmpty()) { List<String> distinctSiteTypes =
	 * quoteIzosdwanSiteRepository
	 * .getDistinctSiteOfferings(solutionDetails.getId());
	 * List<QuoteIzoSdwanAttributeValues> vals =
	 * quoteIzoSdwanAttributeValuesRepository
	 * .findByDisplayValueAndQuote("BYON100P", quoteDetails);
	 * LOGGER.info("IS 100 PERCENT BYON :{}",vals.get(0).getAttributeValue());
	 * List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId); if
	 * (quoteToLes.stream().findFirst().isPresent()) { QuoteToLe quoteToLe =
	 * quoteToLes.stream().findFirst().get(); Integer iasCount=0; Integer
	 * gvpnCount=0; String ias="Internet Access"; String gvpn="GVPN"; if
	 * (!vals.isEmpty()) { boolean byonStatus =
	 * Boolean.parseBoolean(vals.get(0).getAttributeValue());
	 * LOGGER.info("100 PERCENT BYON BOOLEAN FLAG:{}",byonStatus);
	 * bundleOmsSfdcService.processProductServiceForSolution(quoteToLe,
	 * solutionDetails, quoteToLe.getTpsSfdcOptyId(), CommonConstants.SDWAN);//
	 * adding productServi if (!byonStatus) { for(String val:distinctSiteTypes) {
	 * LOGGER.info("site offering name from db:{}",val); if
	 * (val.toLowerCase().contains(ias.toLowerCase())) {
	 * LOGGER.info("ias site offering name from db:{}",val); if(iasCount==0) {
	 * bundleOmsSfdcService.processProductServiceForSolution(quoteToLe,
	 * solutionDetails, quoteToLe.getTpsSfdcOptyId(), CommonConstants.IAS);// adding
	 * productService iasCount++; } } if
	 * (val.toLowerCase().contains(gvpn.toLowerCase())) {
	 * LOGGER.info("gvpn site offering name from db:{}",val); if(gvpnCount==0) {
	 * bundleOmsSfdcService.processProductServiceForSolution(quoteToLe,
	 * solutionDetails, quoteToLe.getTpsSfdcOptyId(), CommonConstants.GVPN);//
	 * adding productService gvpnCount++; } } }
	 *
	 * } }
	 *
	 *
	 * } QuoteIzoSdwanAttributeValues createVal=new QuoteIzoSdwanAttributeValues();
	 * createVal.setQuote(quoteDetails); createVal.setDisplayValue("CREATEPRODUCT");
	 * createVal.setAttributeValue("true");
	 * quoteIzoSdwanAttributeValuesRepository.save(createVal); } }
	 */

	private void addAddonDetails(List<QuoteIzoSdwanMssPricing> quoteIzoSdwanMssPricings,
			List<VProxyAddonsBean> vProxyAddonsBeans, ProductSolution solution,
			List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails) {
		if (vProxyAddonsBeans != null & !vProxyAddonsBeans.isEmpty()) {
			for (VProxyAddonsBean vproxyAddonsBean : vProxyAddonsBeans) {
				QuoteIzoSdwanMssPricing addonBean = new QuoteIzoSdwanMssPricing();
				addonBean.setComponentName(vproxyAddonsBean.getName());
				addonBean.setComponentType(IzosdwanCommonConstants.ADDON);
				addonBean.setArc(200D);
				addonBean.setNrc(0D);
				addonBean.setActionType(IzosdwanCommonConstants.NEW);
				addonBean.setProduct(IzosdwanCommonConstants.VPROXY);
				addonBean.setSolutionId(solution.getId());
				addonBean.setCurrency(CommonConstants.INR);
				VproxyQuestionnaireDet vproxyAddonQuestionnaireDet = vproxyAddonsBean.getVproxyAddonQuestionnaireDet();
				if (vproxyAddonQuestionnaireDet != null) {
					addonBean.setValue(vproxyAddonQuestionnaireDet.getSelectedValue());
					addonBean.setValueType(vproxyAddonQuestionnaireDet.getName());
				} else {
					Optional<VproxyQuestionnaireDet> question = vproxyQuestionnaireDetails.stream()
							.filter(ques -> ques.getName().equals(IzosdwanCommonConstants.TOTALNOOFUSERS)).findFirst();
					if (question.isPresent()) {
						addonBean.setValue(question.get().getSelectedValue());
						addonBean.setValueType(IzosdwanCommonConstants.USERS);
					}
				}
				quoteIzoSdwanMssPricings.add(addonBean);
			}
		}
	}

	private void addVproxtQuestinnarieDetails(List<QuoteIzoSdwanMssPricing> quoteIzoSdwanMssPricings,
			List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails, ProductSolution solution) {
		if (!vproxyQuestionnaireDetails.isEmpty() && vproxyQuestionnaireDetails != null) {
			for (VproxyQuestionnaireDet vproxyQuest : vproxyQuestionnaireDetails) {
				QuoteIzoSdwanMssPricing profileQuestion = new QuoteIzoSdwanMssPricing();
				if (vproxyQuest.getSelectedValue() != null) {
					profileQuestion.setActionType(IzosdwanCommonConstants.NEW);
					profileQuestion.setComponentName(vproxyQuest.getName());
					profileQuestion.setComponentType(IzosdwanCommonConstants.QUESTION);
					profileQuestion.setValue(vproxyQuest.getSelectedValue());
					profileQuestion.setValueType(IzosdwanCommonConstants.USERS);
					profileQuestion.setArc(100D);
					profileQuestion.setNrc(200D);
					profileQuestion.setProduct(IzosdwanCommonConstants.VPROXY);
					profileQuestion.setSolutionId(solution.getId());
					profileQuestion.setCurrency(CommonConstants.INR);
					quoteIzoSdwanMssPricings.add(profileQuestion);
				}
			}

		}
	}

	private void addVproxyQuestinnarieDetails(List<VproxyChargableComponents> vproxyChargableComponents,
			List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails, ProductSolution solution,
			String existingCurrency) {
		if (!vproxyQuestionnaireDetails.isEmpty() && vproxyQuestionnaireDetails != null) {
			for (VproxyQuestionnaireDet vproxyQuest : vproxyQuestionnaireDetails) {
				VproxyChargableComponents questionChargeableComponent = new VproxyChargableComponents();
				if (vproxyQuest.getSelectedValue() != null && !vproxyQuest.getSelectedValue().isEmpty()) {
					questionChargeableComponent.setActionType(IzosdwanCommonConstants.NEW);
					if (vproxyQuest.getName().equalsIgnoreCase(IzosdwanCommonConstants.TOTALNOOFUSERSMIDDLEEAST)) {
						questionChargeableComponent
								.setComponentName(IzosdwanCommonConstants.BANDWIDTH_SURCHARGE_MIDDLE_EAST);
					} else if (vproxyQuest.getName().contains(IzosdwanCommonConstants.OTHERUSERS)) {
						questionChargeableComponent.setComponentName(IzosdwanCommonConstants.BANDWIDTH_ROW);
					} else {
						questionChargeableComponent.setComponentName(vproxyQuest.getName());
					}
					questionChargeableComponent.setComponentType(IzosdwanCommonConstants.QUESTION);
					questionChargeableComponent.setValue(vproxyQuest.getSelectedValue());
					questionChargeableComponent.setValueType(IzosdwanCommonConstants.USERS);
					questionChargeableComponent.setArc(new BigDecimal(0D).setScale(2, RoundingMode.DOWN));
					questionChargeableComponent.setNrc(new BigDecimal(0D).setScale(2, RoundingMode.DOWN));
					questionChargeableComponent.setCurrencyType(existingCurrency);
					questionChargeableComponent.setComponentId(null); // TO:DO post pricing fix
					vproxyChargableComponents.add(questionChargeableComponent);
				}
			}
		}
	}

	public String persistVproxyPricingDetails(Integer quoteId) {
		if (quoteId != null) {
			Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByQuote(quoteDetails);
			if (quoteDetails != null) {
				List<QuoteIzoSdwanMssPricing> quoteIzoSdwanMssPricings = new ArrayList<>();
				List<ProductSolution> solutions = productSolutionRepository.findByReferenceIdForVproxy(quoteId);
				if (solutions != null && !solutions.isEmpty()) {
					for (ProductSolution solution : solutions) {
						quoteIzoSdwanMssPricings = quoteIzoSdwanMssPricingRepository.findBySolutionId(solution.getId());
						if (quoteIzoSdwanMssPricings != null && !quoteIzoSdwanMssPricings.isEmpty()) {
							quoteIzoSdwanMssPricingRepository.deleteAll();
							quoteIzoSdwanMssPricings = new ArrayList<>();
						}
						try {
							VproxySolutionBean VproxySolutionDetail = Utils
									.convertJsonToObject(solution.getProductProfileData(), VproxySolutionBean.class);

							if (VproxySolutionDetail != null) {
								String profileName = "";
								if (VproxySolutionDetail.getSolutionName().equals(IzosdwanCommonConstants.Secure)) {
									profileName = IzosdwanCommonConstants.ISSWG;
								}
								if (VproxySolutionDetail.getSolutionName().equals(IzosdwanCommonConstants.Private)) {
									profileName = IzosdwanCommonConstants.ISSPA;
								}
								if (checkVproxyExistOrNotDet(attributeValues, profileName)) {
									QuoteIzoSdwanMssPricing solutionPricing = new QuoteIzoSdwanMssPricing();
									// solution level charges
									solutionPricing.setProduct(IzosdwanCommonConstants.VPROXY);
									solutionPricing.setComponentName(VproxySolutionDetail.getSolutionName());
									solutionPricing.setComponentType(IzosdwanCommonConstants.SOLUTION);
									solutionPricing.setSolutionId(solution.getId());
									quoteIzoSdwanMssPricings.add(solutionPricing);

									List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails = VproxySolutionDetail
											.getVproxyQuestionnaireDets();

									// Profile level charges
									VproxyProductOfferingBean offering = VproxySolutionDetail
											.getVproxyProductOfferingBeans();
									QuoteIzoSdwanMssPricing sdwanMssProfile = new QuoteIzoSdwanMssPricing();
									sdwanMssProfile.setProduct(IzosdwanCommonConstants.VPROXY);
									sdwanMssProfile.setComponentName(offering.getProductOfferingName());
									sdwanMssProfile.setComponentType(IzosdwanCommonConstants.OFFERING);
									sdwanMssProfile.setActionType(IzosdwanCommonConstants.NEW);
									sdwanMssProfile.setArc(130D);
									sdwanMssProfile.setNrc(140D);
									sdwanMssProfile.setProduct(IzosdwanCommonConstants.VPROXY);
									sdwanMssProfile.setSolutionId(solution.getId());
									sdwanMssProfile.setCurrency(CommonConstants.INR);
									Optional<VproxyQuestionnaireDet> question = vproxyQuestionnaireDetails.stream()
											.filter(ques -> ques.getName()
													.equals(IzosdwanCommonConstants.TOTALNOOFUSERS))
											.findFirst();
									if (question.isPresent()) {
										sdwanMssProfile.setValue(question.get().getSelectedValue());
										sdwanMssProfile.setValueType(IzosdwanCommonConstants.USERS);
									}
									quoteIzoSdwanMssPricings.add(sdwanMssProfile);
									// Profile Questinnaries
									addVproxtQuestinnarieDetails(quoteIzoSdwanMssPricings, vproxyQuestionnaireDetails,
											solution);

									// Profile Addons
									List<VProxyAddonsBean> vProxyAddonsBeans = offering.getvProxyAddonsBeans();
									addAddonDetails(quoteIzoSdwanMssPricings, vProxyAddonsBeans, solution,
											vproxyQuestionnaireDetails);
									// 3rd stage questionnaire details
									getVproxySupportDetails(quoteDetails, quoteIzoSdwanMssPricings, solution.getId());
								}
								quoteIzoSdwanMssPricingRepository.saveAll(quoteIzoSdwanMssPricings);
							}
						} catch (TclCommonException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

		return ResponseResource.RES_SUCCESS;
	}

	private void getVproxySupportDetails(Quote quote, List<QuoteIzoSdwanMssPricing> quoteIzoSdwanMssPricings,
			Integer solutionId) {
		List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository.findByQuote(quote);
		for (QuoteIzoSdwanAttributeValues attrVal : attributeValues) {
			if (attrVal.getDisplayValue().equals(IzosdwanCommonConstants.ISOTIREQ)
					&& attrVal.getAttributeValue().equals(CommonConstants.YES)) {
				QuoteIzoSdwanMssPricing sdwanMssQuestionnaire = new QuoteIzoSdwanMssPricing();
				sdwanMssQuestionnaire.setProduct(IzosdwanCommonConstants.VPROXY);
				sdwanMssQuestionnaire.setComponentName(IzosdwanCommonConstants.isOTIRequired);
				sdwanMssQuestionnaire.setComponentType(IzosdwanCommonConstants.QUESTION);
				sdwanMssQuestionnaire.setActionType(IzosdwanCommonConstants.NEW);
				sdwanMssQuestionnaire.setArc(130D);
				sdwanMssQuestionnaire.setNrc(140D);
				sdwanMssQuestionnaire.setProduct(IzosdwanCommonConstants.VPROXY);
				sdwanMssQuestionnaire.setCurrency(CommonConstants.INR);
				sdwanMssQuestionnaire.setSolutionId(solutionId);
				quoteIzoSdwanMssPricings.add(sdwanMssQuestionnaire);
			}
			if (attrVal.getDisplayValue().equals(IzosdwanCommonConstants.ISSUPPORTREQ)
					&& attrVal.getAttributeValue().equals(CommonConstants.YES)) {
				Optional<QuoteIzoSdwanAttributeValues> attrValue = attributeValues.stream()
						.filter(val -> val.getDisplayValue().equals(IzosdwanCommonConstants.SUPPORTTYPE)).findFirst();
				if (attrValue.isPresent()) {
					QuoteIzoSdwanMssPricing sdwanMssQuestionnaire = new QuoteIzoSdwanMssPricing();
					sdwanMssQuestionnaire.setProduct(IzosdwanCommonConstants.VPROXY);
					if (attrValue.get().getAttributeValue().equals(IzosdwanCommonConstants.PREMIUM)) {
						sdwanMssQuestionnaire.setComponentName(IzosdwanCommonConstants.PREMIUMVAL);
					}
					if (attrValue.get().getAttributeValue().equals(IzosdwanCommonConstants.STANDARD)) {
						sdwanMssQuestionnaire.setComponentName(IzosdwanCommonConstants.STANDARDVAL);
					}
					sdwanMssQuestionnaire.setComponentType(IzosdwanCommonConstants.QUESTION);
					sdwanMssQuestionnaire.setActionType(IzosdwanCommonConstants.NEW);
					sdwanMssQuestionnaire.setArc(130D);
					sdwanMssQuestionnaire.setNrc(140D);
					sdwanMssQuestionnaire.setProduct(IzosdwanCommonConstants.VPROXY);
					sdwanMssQuestionnaire.setCurrency(CommonConstants.INR);
					sdwanMssQuestionnaire.setSolutionId(solutionId);
					quoteIzoSdwanMssPricings.add(sdwanMssQuestionnaire);
				}

			}
		}
	}

	private void getVproxySupportDetailsForPricing(Quote quote,
			List<VproxyChargableComponents> vproxyChargableComponents, Integer solutionId, String existingCurrency) {
		List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository.findByQuote(quote);
		for (QuoteIzoSdwanAttributeValues attrVal : attributeValues) {
			if (attrVal.getDisplayValue().equals(IzosdwanCommonConstants.ISOTIREQ)
					&& attrVal.getAttributeValue().equals(CommonConstants.YES)) {
				VproxyChargableComponents supportComponent = new VproxyChargableComponents();
				supportComponent.setComponentName(IzosdwanCommonConstants.isOTIRequired);
				supportComponent.setComponentType(IzosdwanCommonConstants.QUESTION);
				supportComponent.setActionType(IzosdwanCommonConstants.NEW);
				supportComponent.setArc(new BigDecimal(0D).setScale(2, RoundingMode.DOWN));
				supportComponent.setNrc(new BigDecimal(0D).setScale(2, RoundingMode.DOWN));
				supportComponent.setCurrencyType(existingCurrency);
				supportComponent.setComponentId(null); // TO:DO Post pricing
				vproxyChargableComponents.add(supportComponent);
			}
			if (attrVal.getDisplayValue().equals(IzosdwanCommonConstants.ISSUPPORTREQ)
					&& attrVal.getAttributeValue().equals(CommonConstants.YES)) {
				Optional<QuoteIzoSdwanAttributeValues> attrValue = attributeValues.stream()
						.filter(val -> val.getDisplayValue().equals(IzosdwanCommonConstants.SUPPORTTYPE)).findFirst();
				if (attrValue.isPresent()) {
					VproxyChargableComponents supportComponent = new VproxyChargableComponents();
					if (attrValue.get().getAttributeValue().equals(IzosdwanCommonConstants.PREMIUM)) {
						supportComponent.setComponentName(IzosdwanCommonConstants.PREMIUMVAL);
					}
					if (attrValue.get().getAttributeValue().equals(IzosdwanCommonConstants.STANDARD)) {
						supportComponent.setComponentName(IzosdwanCommonConstants.STANDARDVAL);
					}
					supportComponent.setComponentType(IzosdwanCommonConstants.QUESTION);
					supportComponent.setActionType(IzosdwanCommonConstants.NEW);
					supportComponent.setArc(new BigDecimal(0D).setScale(2, RoundingMode.DOWN));
					supportComponent.setNrc(new BigDecimal(0D).setScale(2, RoundingMode.DOWN));
					supportComponent.setCurrencyType(existingCurrency);
					supportComponent.setComponentId(null); // TO:DO Post pricing
					vproxyChargableComponents.add(supportComponent);
				}

			}
		}
	}

	/**
	 *
	 * This method is used to get solution level charges for IZO-SDWAN - Vproxy
	 *
	 * @author mpalanis
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<VproxySolutionLevelCharges> getVproxySolutionLevelCharges(Integer quoteId) throws TclCommonException {
		List<VproxySolutionLevelCharges> solutionLevelCharges = new ArrayList<>();
		if (quoteId != null) {
			try {
				Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
				List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(quoteDetails);
				if (quoteToLe != null && !quoteToLe.isEmpty()) {
					String existingCurrency = izosdwanPricingAndFeasibilityService
							.findExistingCurrency(quoteToLe.get(0));
					List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
							.findByQuote(quoteDetails);
					List<ProductSolution> solutions = productSolutionRepository.findByReferenceIdForVproxy(quoteId);
					if (solutions != null && !solutions.isEmpty()) {
						for (ProductSolution solution : solutions) {
							VproxySolutionBean VproxySolutionDetail = Utils
									.convertJsonToObject(solution.getProductProfileData(), VproxySolutionBean.class);
							if (VproxySolutionDetail != null) {
								LOGGER.info("Got solution:{}", VproxySolutionDetail.getSolutionName());
								VproxyProductOfferingBean vproxyProductOfferingBean = VproxySolutionDetail
										.getVproxyProductOfferingBeans();
								List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails = VproxySolutionDetail
										.getVproxyQuestionnaireDets();
								if (vproxyProductOfferingBean != null) {
									LOGGER.info("Got offering!");
									List<VProxyAddonsBean> vProxyAddonsBeans = vproxyProductOfferingBean
											.getvProxyAddonsBeans();
									String profileName = "";
									String componentName = "";
									if (VproxySolutionDetail.getSolutionName().equals(IzosdwanCommonConstants.Secure)) {
										profileName = IzosdwanCommonConstants.ISSWG;
										componentName = IzosdwanCommonConstants.VPROXY_SWG;
									}
									if (VproxySolutionDetail.getSolutionName()
											.equals(IzosdwanCommonConstants.Private)) {
										profileName = IzosdwanCommonConstants.ISSPA;
										componentName = IzosdwanCommonConstants.VPROXY_SPA;
									}
									if (checkVproxyExistOrNotDet(attributeValues, profileName)) {
										VproxySolutionLevelCharges solutionCharges = new VproxySolutionLevelCharges();
										solutionCharges.setSolutionName(vproxyProductOfferingBean.getSolutionName());
										solutionCharges
												.setProfileName(vproxyProductOfferingBean.getProductOfferingName());
										// add chargeable components
										List<VproxyChargableComponents> vproxyChargableComponents = new ArrayList<>();
										MstProductComponent mstProductComponent = mstProductComponentRepository
												.findByName(componentName);
										if (mstProductComponent != null) {
											LOGGER.info("got mst product compoenent:{}", mstProductComponent.getName());
											List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
													.findByReferenceIdAndMstProductComponent(solution.getId(),
															mstProductComponent);
											if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
												LOGGER.info("got quote product component!");
												// get offering price
												QuotePrice componentPrice = quotePriceRepository
														.findFirstByReferenceIdAndReferenceName(
																String.valueOf(quoteProductComponents.get(0).getId()),
																QuoteConstants.COMPONENTS.toString());
												if (componentPrice != null) {
													LOGGER.info("component price for {} : {}",
															vproxyProductOfferingBean.getProductOfferingName(),
															componentPrice.getEffectiveArc());
													// set solutionlevel component
													VproxyChargableComponents chargeableComponent = new VproxyChargableComponents();
													chargeableComponent.setComponentName(
															vproxyProductOfferingBean.getSolutionName());
													chargeableComponent
															.setComponentType(IzosdwanCommonConstants.SOLUTION);
													vproxyChargableComponents.add(chargeableComponent);
													// set offering
													VproxyChargableComponents offeringchargeableComponent = new VproxyChargableComponents();
													offeringchargeableComponent.setComponentName(
															vproxyProductOfferingBean.getProductOfferingName());
													offeringchargeableComponent
															.setComponentType(IzosdwanCommonConstants.OFFERING);
													offeringchargeableComponent
															.setArc(new BigDecimal(componentPrice.getEffectiveArc())
																	.setScale(2, RoundingMode.DOWN));
													offeringchargeableComponent
															.setNrc(new BigDecimal(componentPrice.getEffectiveNrc())
																	.setScale(2, RoundingMode.DOWN));
													offeringchargeableComponent
															.setComponentId(quoteProductComponents.get(0).getId());
													List<String> values = getValuesForVproxyChargeableComponents(null,
															vproxyQuestionnaireDetails);
													if (values != null && !values.isEmpty()) {
														offeringchargeableComponent.setValue(values.get(0));
														offeringchargeableComponent.setValueType(values.get(1));
													}
													offeringchargeableComponent
															.setActionType(IzosdwanCommonConstants.NEW);
													offeringchargeableComponent.setCurrencyType(existingCurrency);
													vproxyChargableComponents.add(offeringchargeableComponent);
													addVproxyQuestinnarieDetails(vproxyChargableComponents,
															vproxyQuestionnaireDetails, solution, existingCurrency);
													// set addon price
													List<QuoteProductComponentsAttributeValue> productComponentAttributeValues = quoteProductComponentsAttributeValueRepository
															.findByQuoteProductComponent_Id(
																	quoteProductComponents.get(0).getId());
													if (vProxyAddonsBeans != null && !vProxyAddonsBeans.isEmpty()
															&& productComponentAttributeValues != null
															&& !productComponentAttributeValues.isEmpty()) {
														for (VProxyAddonsBean addonBean : vProxyAddonsBeans) {
															if (!addonBean.getName().equalsIgnoreCase(
																	IzosdwanCommonConstants.OEM_PREMIUM_SUPPORT)) {
																QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = productComponentAttributeValues
																		.stream()
																		.filter(pca -> pca.getProductAttributeMaster()
																				.getName()
																				.equalsIgnoreCase(addonBean.getName()))
																		.findFirst().orElse(null);
																VproxyChargableComponents addonChargeableComponent = new VproxyChargableComponents();
																addonChargeableComponent
																		.setComponentName(addonBean.getName());
																addonChargeableComponent.setComponentType(
																		IzosdwanCommonConstants.ADDON);
																List<String> addonValues = getValuesForVproxyChargeableComponents(
																		addonBean, vproxyQuestionnaireDetails);
																if (addonValues != null && !addonValues.isEmpty()) {
																	addonChargeableComponent
																			.setValue(addonValues.get(0));
																	addonChargeableComponent
																			.setValueType(addonValues.get(1));
																}
																addonChargeableComponent.setArc(getAddonPrice(
																		quoteProductComponentsAttributeValue,
																		addonBean.getName(),
																		IzosdwanCommonConstants.ARC));
																addonChargeableComponent.setNrc(getAddonPrice(
																		quoteProductComponentsAttributeValue,
																		addonBean.getName(),
																		IzosdwanCommonConstants.NRC));
																addonChargeableComponent
																		.setActionType(IzosdwanCommonConstants.NEW);
																addonChargeableComponent
																		.setCurrencyType(existingCurrency);
																if (quoteProductComponentsAttributeValue != null) {
																	addonChargeableComponent.setComponentId(
																			quoteProductComponentsAttributeValue
																					.getId());
																}
																vproxyChargableComponents.add(addonChargeableComponent);
															}
														}
													}
													getVproxySupportDetailsForPricing(quoteDetails,
															vproxyChargableComponents, solution.getId(),
															existingCurrency);
												}
											}
										}
										solutionCharges.setVproxyChargableComponents(vproxyChargableComponents);
										solutionLevelCharges.add(solutionCharges);
									}
								}
							}
						}
					}
				}
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		}
		return solutionLevelCharges;
	}

	private List<String> getValuesForVproxyChargeableComponents(VProxyAddonsBean addonBean,
			List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails) throws TclCommonException {
		List<String> values = new ArrayList<>();
		if (addonBean == null || addonBean.getVproxyAddonQuestionnaireDet() == null) {
			if (vproxyQuestionnaireDetails != null && !vproxyQuestionnaireDetails.isEmpty()) {
				Optional<VproxyQuestionnaireDet> question = vproxyQuestionnaireDetails.stream()
						.filter(ques -> ques.getName().equals(IzosdwanCommonConstants.TOTALNOOFUSERS)).findFirst();
				if (question.isPresent()) {
					values.add(question.get().getSelectedValue());
					values.add(IzosdwanCommonConstants.USERS);
				}
			}
		} else {
			values.add(addonBean.getVproxyAddonQuestionnaireDet().getSelectedValue());
			if (addonBean.getVproxyAddonQuestionnaireDet().getDescription()
					.contains(IzosdwanCommonConstants.VOLUME_REQUIRED)) {
				values.add(IzosdwanCommonConstants.VOLUME);
			} else if (addonBean.getVproxyAddonQuestionnaireDet().getDescription()
					.equalsIgnoreCase(IzosdwanCommonConstants.SELECT_TYPE)) {
				values.add("");
			} else if (addonBean.getVproxyAddonQuestionnaireDet().getDescription()
					.contains(IzosdwanCommonConstants.MILLION_CELLS)) {
				values.add(StringUtils.capitalize(IzosdwanCommonConstants.MILLION_CELLS));
			} else if (addonBean.getVproxyAddonQuestionnaireDet().getDescription()
					.contains(IzosdwanCommonConstants.CONNECTORS)) {
				values.add(IzosdwanCommonConstants.CONNECTORS);
			} else {
				values.add(addonBean.getVproxyAddonQuestionnaireDet().getDescription());
			}
		}
		return values;
	}

	/**
	 *
	 * get charges for addons at vproxy solution level
	 *
	 * @author mpalanis
	 * @param productComponentAttributeValues
	 * @param addonName
	 * @param priceType
	 * @return
	 * @throws TclCommonException
	 */
	private BigDecimal getAddonPrice(QuoteProductComponentsAttributeValue productComponentAttributeValues,
			String addonName, String priceType) throws TclCommonException {
		BigDecimal price = new BigDecimal(0D);
		QuotePrice addonAttributePrice = new QuotePrice();
		if (productComponentAttributeValues != null) {
			if (productComponentAttributeValues.getProductAttributeMaster().getName().equalsIgnoreCase(addonName)) {
				addonAttributePrice = quotePriceRepository
						.findByReferenceId(String.valueOf(productComponentAttributeValues.getId()));
				LOGGER.info("arc for {} :{} ", addonName, addonAttributePrice.getEffectiveArc());
				LOGGER.info("nrc for {} :{} ", addonName, addonAttributePrice.getEffectiveArc());
				if (priceType.equalsIgnoreCase(IzosdwanCommonConstants.ARC)) {
					price = new BigDecimal(addonAttributePrice.getEffectiveArc()).setScale(2, RoundingMode.DOWN);
				} else {
					price = new BigDecimal(addonAttributePrice.getEffectiveNrc()).setScale(2, RoundingMode.DOWN);
				}
			}

		}
		return price;
	}

	/**
	 *
	 * Update quote stage
	 *
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteToLeId
	 * @param stageName
	 * @return
	 * @throws TclCommonException
	 */
	public String updateQuoteStage(Integer quoteId, Integer quoteToLeId, String stageName) throws TclCommonException {
		if (quoteId == null || quoteToLeId == null || !StringUtils.isNotBlank(stageName)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLeOpt.isPresent()) {
				quoteToLeOpt.get().setStage(stageName);
				quoteToLeRepository.save(quoteToLeOpt.get());
			}
		} catch (Exception e) {
			LOGGER.error("Error in updating the quote stage", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	public void deleteQuote(Integer quoteId) throws TclCommonException {
		try {

			if (Objects.isNull(quoteId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			Optional<Quote> quoteToDelete = quoteRepository.findById(quoteId);

			if (quoteToDelete.isPresent()) {
				Quote quote = quoteToDelete.get();

				quote.getQuoteToLes().stream().forEach(quoteToLe -> {
					try {
						quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteToLeProdFamily -> {
							Optional<Order> order = orderRepository.findByQuote(quote);
							if (order.isPresent()) {
								deleteOrderRelatedDetails(order.get());
							}
							quoteToLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {
								prodSolution.getQuoteIzoSdwanSites().stream().forEach(izoSdwanSite -> {
									List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndReferenceName(izoSdwanSite.getId(),
													QuoteConstants.IZOSDWAN_SITES.toString());
									quoteProductComponentList.stream().forEach(quoteProdComponent -> {
										deleteQuoteProductComponent(quoteProdComponent);
									});
									deleteFeasibilityDetails(izoSdwanSite);
									deleteQuoteSiteToServiceDetails(izoSdwanSite);
									quoteIzosdwanSiteRepository.delete(izoSdwanSite);
								});
								productSolutionRepository.delete(prodSolution);
							});
							quoteToLeProductFamilyRepository.delete(quoteToLeProdFamily);
						});
						deleteQuoteLeAttributeValues(quoteToLe);
						deleteQuoteSdwanAttributeValues(quoteToLe);
						quoteToLeRepository.delete(quoteToLe);

//                         SFDC Update Opportunity - CLOSED DROPPED
//						omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
//								SFDCConstants.CLOSED_DROPPED, quoteToLe);
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
				deleteQuotePrice(quote);
				deleteQuoteIzoSdwanCGWDetails(quote);
				deleteQuoteIzosdwanByonUploadDetails(quote);
				quoteRepository.delete(quote);
			}
		} catch (Exception e) {
			if (e instanceof TclCommonException)
				throw e;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	private void deleteQuoteSiteToServiceDetails(QuoteIzosdwanSite izosdwanSite) {
		LOGGER.info("Inside in deleteQuoteSiteToServiceDetails");
		List<QuoteIllSiteToService> siteToServiceList = quoteIllSiteToServiceRepository
				.findByQuoteIzosdwanSite(izosdwanSite);
		if (siteToServiceList != null && !siteToServiceList.isEmpty())
			quoteIllSiteToServiceRepository.deleteAll(siteToServiceList);

	}

	private void deleteOrderRelatedDetails(Order order) {

		order.getOrderToLes().stream().forEach(orderToLe -> {
			orderToLe.getOrderToLeProductFamilies().stream().forEach(orderToLeProdFamily -> {
				orderToLeProdFamily.getOrderProductSolutions().stream().forEach(orderProdSolution -> {
					orderProdSolution.getOrderIzosdwanSites().stream().forEach(orderIllSite -> {
						List<OrderProductComponent> orderProductComponentList = orderProductComponentRepository
								.findByReferenceId(orderIllSite.getId());
						orderProductComponentList.stream().forEach(orderProdComponent -> {
							deleteOrderProductComponent(orderProdComponent);
						});
						deleteOrderFeasibilityDetails(orderIllSite);
						deleteOrderSiteToServiceDetails(orderIllSite);
						orderIzosdwanSiteRepository.delete(orderIllSite);
					});
					orderProductSolutionRepository.delete(orderProdSolution);
				});
				orderToLeProductFamilyRepository.delete(orderToLeProdFamily);
			});
			deleteOrderLeAttributeValues(orderToLe);
			orderToLeRepository.delete(orderToLe);
		});
		deleteOrderConfirmationAudits(order);
		orderRepository.delete(order);

	}

	private void deleteOrderSiteToServiceDetails(OrderIzosdwanSite orderIllSite) {
		List<OrderIllSiteToService> orderSiteToServiceList = orderIllSiteToServiceRepository
				.findByOrderIzosdwanSite(orderIllSite);
		if (orderSiteToServiceList != null && !orderSiteToServiceList.isEmpty())
			orderIllSiteToServiceRepository.deleteAll(orderSiteToServiceList);

	}

	private void deleteOrderProductComponent(OrderProductComponent orderProdComponent) {
		if (!orderProdComponent.getOrderProductComponentsAttributeValues().isEmpty()) {

			orderProdComponent.getOrderProductComponentsAttributeValues().stream().forEach(attri -> {
				OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attri.getId()), QuoteConstants.ATTRIBUTES.toString());
				if (orderPrice != null)
					orderPriceRepository.delete(orderPrice);
				orderProductComponentsAttributeValueRepository.delete(attri);
			});

			OrderPrice orderPriceAtt = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProdComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (orderPriceAtt != null)
				orderPriceRepository.delete(orderPriceAtt);

			/*
			 * orderProductComponentsAttributeValueRepository
			 * .deleteAll(orderProdComponent.getOrderProductComponentsAttributeValues());
			 */
			orderProductComponentRepository.delete(orderProdComponent);
		}

	}

	private void deleteOrderFeasibilityDetails(OrderIzosdwanSite orderIllSite) {
		List<OrderIzosdwanSiteFeasibility> orderSiteFeasiblityList = orderSiteFeasibilityRepository
				.findByOrderIzosdwanSite(orderIllSite);
		if (!orderSiteFeasiblityList.isEmpty())
			orderSiteFeasibilityRepository.deleteAll(orderSiteFeasiblityList);

		List<OrderIzosdwanSiteSla> orderIllSiteSlaList = orderIzosdwanSiteSlaRepository
				.findByOrderIzosdwanSite(orderIllSite);
		if (!orderIllSiteSlaList.isEmpty())
			orderIzosdwanSiteSlaRepository.deleteAll(orderIllSiteSlaList);

		List<OrderIzosdwanSiteStatusAudit> orderIzosdwanSiteStatusAudits = orderIzosdwanSiteStatusAuditRepository
				.findByOrderIzosdwanSite(orderIllSite);
		if (!orderIzosdwanSiteStatusAudits.isEmpty()) {
			orderIzosdwanSiteStatusAudits.stream().forEach(orderSiteStatusAudit -> {
				List<OrderIzosdwanSiteStageAudit> orderSiteStageAuditList = orderIzosdwanSiteStageAuditRepository
						.findByOrderIzosdwanSiteStatusAudit(orderSiteStatusAudit);
				if (!orderSiteStageAuditList.isEmpty())
					orderIzosdwanSiteStageAuditRepository.deleteAll(orderSiteStageAuditList);
			});
		}

		orderIzosdwanSiteStatusAuditRepository.deleteAll(orderIzosdwanSiteStatusAudits);

	}

	private void deleteOrderSiteToServiceDetails(OrderIllSite orderIllSite) {
		List<OrderIllSiteToService> orderSiteToServiceList = orderIllSiteToServiceRepository
				.findByOrderIllSite(orderIllSite);
		if (orderSiteToServiceList != null && !orderSiteToServiceList.isEmpty())
			orderIllSiteToServiceRepository.deleteAll(orderSiteToServiceList);

	}

	private void deleteOrderLeAttributeValues(OrderToLe orderToLe) {

		Set<OrdersLeAttributeValue> orderLeAttributeValueList = new HashSet<>(
				ordersLeAttributeValueRepository.findByOrderToLe(orderToLe));
		if (!orderLeAttributeValueList.isEmpty())
			ordersLeAttributeValueRepository.deleteAll(orderLeAttributeValueList);

		List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository.findByOrderToLe(orderToLe);
		if (!omsAttachmentsList.isEmpty()) {
			omsAttachmentRepository.deleteAll(omsAttachmentsList);
		}
	}

	private void deleteOrderConfirmationAudits(Order order) {
		OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
				.findByOrderRefUuid(order.getOrderCode());
		if (orderConfirmationAudit != null) {
			orderConfirmationAuditRepository.delete(orderConfirmationAudit);
		}

		CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
		if (cofDetails != null)
			cofDetailsRepository.delete(cofDetails);

		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(order.getOrderCode());
		if (docusignAudit != null)
			docusignAuditRepository.delete(docusignAudit);
	}

	private void deleteQuoteProductComponent(QuoteProductComponent quoteProdComponent) {
		if (!quoteProdComponent.getQuoteProductComponentsAttributeValues().isEmpty()) {
			/*
			 * quoteProductComponentsAttributeValueRepository
			 * .deleteAll(quoteProdComponent.getQuoteProductComponentsAttributeValues());
			 */
			quoteProdComponent.getQuoteProductComponentsAttributeValues().stream().forEach(attri -> {
				QuotePrice quotePrice = quotePriceRepository.findFirstByReferenceIdAndReferenceName(
						String.valueOf(attri.getId()), QuoteConstants.ATTRIBUTES.toString());
				if (quotePrice != null) {
					List<QuotePriceAudit> quotePriceAuditList = quotePriceAuditRepository.findByQuotePrice(quotePrice);
					if (!quotePriceAuditList.isEmpty()) {
						quotePriceAuditRepository.deleteAll(quotePriceAuditList);
					}
					quotePriceRepository.delete(quotePrice);
				}
				quoteProductComponentsAttributeValueRepository.delete(attri);
			});

			QuotePrice quotePriceAttr = quotePriceRepository.findFirstByReferenceIdAndReferenceName(
					String.valueOf(quoteProdComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (quotePriceAttr != null) {
				List<QuotePriceAudit> quotePriceAuditList = quotePriceAuditRepository.findByQuotePrice(quotePriceAttr);
				if (!quotePriceAuditList.isEmpty()) {
					quotePriceAuditRepository.deleteAll(quotePriceAuditList);
				}
				quotePriceRepository.delete(quotePriceAttr);
			}

			quoteProductComponentRepository.delete(quoteProdComponent);
		}
	}

	private void deleteFeasibilityDetails(QuoteIzosdwanSite izoSdwanSite) {

		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIzosdwanSite(izoSdwanSite);
		if (!siteFeasibilities.isEmpty()) {
			siteFeasibilities.stream().forEach(siteFeasibility -> {
				List<IzosdwanSiteFeasibilityAudit> siteFeasibilityAuditList = izosdwanSiteFeasibilityAuditRepository
						.findByIzosdwanSiteFeasibility(siteFeasibility);
				if (!siteFeasibilityAuditList.isEmpty()) {
					izosdwanSiteFeasibilityAuditRepository.deleteAll(siteFeasibilityAuditList);
				}
			});

			siteFeasibilityRepository.deleteAll(siteFeasibilities);
		}

		List<PricingEngineResponse> pricingDetailList = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(izoSdwanSite.getSiteCode(), "Discount");
		if (!pricingDetailList.isEmpty())
			pricingDetailsRepository.deleteAll(pricingDetailList);

		List<QuoteIzosdwanSiteSla> quoteIzosdwanSiteSlaList = quoteIzoSdwanSlaRepository
				.findByQuoteIzosdwanSite(izoSdwanSite);
		if (!quoteIzosdwanSiteSlaList.isEmpty())
			quoteIzoSdwanSlaRepository.deleteAll(quoteIzosdwanSiteSlaList);

	}

	private void deleteQuoteLeAttributeValues(QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValueList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe);
		if (!quoteLeAttributeValueList.isEmpty())
			quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValueList);
		List<QuoteDelegation> quoteDelegationList = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		if (!quoteDelegationList.isEmpty())
			quoteDelegationRepository.deleteAll(quoteDelegationList);
	}

	private void deleteQuotePrice(Quote quote) {
		List<QuotePrice> quotePriceList = quotePriceRepository.findByQuoteId(quote.getId());
		if (!quotePriceList.isEmpty())
			quotePriceRepository.deleteAll(quotePriceList);

		List<OrderPrice> orderPriceList = orderPriceRepository.findByQuoteId(quote.getId());
		if (!orderPriceList.isEmpty())
			orderPriceRepository.deleteAll(orderPriceList);

	}

	private void deleteQuoteSdwanAttributeValues(QuoteToLe quoteToLe) {
		List<QuoteIzoSdwanAttributeValues> quoteLeAttributeValueList = quoteIzoSdwanAttributeValuesRepository
				.findByQuote(quoteToLe.getQuote());
		if (!quoteLeAttributeValueList.isEmpty())
			quoteIzoSdwanAttributeValuesRepository.deleteAll(quoteLeAttributeValueList);
		List<QuoteDelegation> quoteDelegationList = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		if (!quoteDelegationList.isEmpty())
			quoteDelegationRepository.deleteAll(quoteDelegationList);
	}

	private void deleteQuoteIzoSdwanCGWDetails(Quote quote) {
		List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetailList = quoteIzosdwanCgwDetailRepository.findByQuote(quote);
		if (!quoteIzosdwanCgwDetailList.isEmpty()) {
			quoteIzosdwanCgwDetailRepository.deleteAll(quoteIzosdwanCgwDetailList);
		}
	}

	private void deleteQuoteIzosdwanByonUploadDetails(Quote quote) {
		List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetail = quoteIzosdwanByonUploadDetailRepository
				.findByQuote(quote);
		if (!quoteIzosdwanByonUploadDetail.isEmpty()) {
			quoteIzosdwanByonUploadDetailRepository.deleteAll(quoteIzosdwanByonUploadDetail);
		}
	}

	public QuoteIllSitesWithFeasiblityAndPricingBean getFeasiblityAndPricingDetailsForQuoteIllSites(
			Integer quoteIllSiteId) throws TclCommonException {
		QuoteIllSitesWithFeasiblityAndPricingBean quoteIllSiteBeans = new QuoteIllSitesWithFeasiblityAndPricingBean();
		try {
			Optional<QuoteIzosdwanSite> quoteIllSiteDetail = quoteIzosdwanSiteRepository.findById(quoteIllSiteId);
			LOGGER.info("Ill sites received {}", quoteIllSiteDetail);
			if (quoteIllSiteDetail.isPresent()) {
				List<IzosdwanSiteFeasibility> feasiblityDetails = siteFeasibilityRepository
						.findByQuoteIzosdwanSite(quoteIllSiteDetail.get());
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingTypeNotIn(quoteIllSiteDetail.get().getSiteCode(), "Discount");
				quoteIllSiteBeans = constructQuoteIllSitesWithFeasiblityAndPricingDetails(quoteIllSiteDetail.get(),
						feasiblityDetails, pricingDetails);
				LOGGER.info("Feasibility and pricing details for quote Ill sites {}", quoteIllSiteBeans);
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			LOGGER.warn("Cannot get Feasibility and pricing details for quote Ill sites {}");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteIllSiteBeans;
	}

	public QuoteIllSitesWithFeasiblityAndPricingBean constructQuoteIllSitesWithFeasiblityAndPricingDetails(
			QuoteIzosdwanSite quoteIllSite, List<IzosdwanSiteFeasibility> feasiblityDetails,
			List<PricingEngineResponse> pricingDetails) {
		QuoteIllSitesWithFeasiblityAndPricingBean quoteIllSiteBeans = new QuoteIllSitesWithFeasiblityAndPricingBean();
		quoteIllSiteBeans.setSiteId(quoteIllSite.getId());
		quoteIllSiteBeans.setSiteCode(quoteIllSite.getSiteCode());
		quoteIllSiteBeans.setIsFeasible(quoteIllSite.getFeasibility());
		quoteIllSiteBeans.setIsTaxExempted(quoteIllSite.getIsTaxExempted());
		quoteIllSiteBeans.setFeasiblityDetails(constructQuoteFeasiblityResponse(feasiblityDetails));

		quoteIllSiteBeans.setPricingDetails(constructPricingDetails(pricingDetails));
		return quoteIllSiteBeans;
	}

	public List<QuoteIllSitesFeasiblityBean> constructQuoteFeasiblityResponse(
			List<IzosdwanSiteFeasibility> feasiblityDetails) {
		List<QuoteIllSitesFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().filter(siteFeasibility -> {
			if (siteFeasibility.getFeasibilityMode().equalsIgnoreCase("INTL")
					&& Objects.isNull(siteFeasibility.getProvider())) {
				return false;
			} else {
				return true;
			}
		}).forEach(feasiblity -> {
			QuoteIllSitesFeasiblityBean feasiblityBean = new QuoteIllSitesFeasiblityBean();
			feasiblityBean.setId(feasiblity.getId());
			// feasiblityBean.setCreatedTime(new Timestamp(feasiblity.getCreatedTime()));
			feasiblityBean.setFeasibilityCheck(feasiblity.getFeasibilityCheck());
			feasiblityBean.setFeasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setFeasibilityCode(feasiblity.getFeasibilityCode());
			feasiblityBean.setIsSelected(feasiblity.getIsSelected());
			feasiblityBean.setType(feasiblity.getType());
			feasiblityBean.setProvider(feasiblity.getProvider());
			feasiblityBean.setRank(feasiblity.getRank());
			feasiblityBean.setResponse(feasiblity.getResponseJson());
			feasiblityBean.setSiteId(feasiblity.getQuoteIzosdwanSite().getId());
			feasiblityBean.setFeasibilityType(feasiblity.getFeasibilityType());
			feasiblityBean.setSfdcFeasibilityId(feasiblity.getSfdcFeasibilityId());

			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}

	/*
	 * Contruct pricing response from pricingDetail entity
	 *
	 * @author Anandhi Vijayaraghavan
	 *
	 * @param List<PricingDetail>
	 *
	 * @return List<Map<String,String>>
	 */
	public List<PricingDetailBean> constructPricingDetails(List<PricingEngineResponse> pricingDetails) {
		List<PricingDetailBean> pricingResponse = new ArrayList<>();
		pricingDetails.stream().forEach(pricing -> {
			PricingDetailBean pricingBean = new PricingDetailBean();
			pricingBean.setId(pricing.getId());
			pricingBean.setDateTime(pricing.getDateTime());
			pricingBean.setPriceMode(pricingBean.getPriceMode());
			pricingBean.setPricingType(pricing.getPricingType());
			pricingBean.setRequest(pricing.getRequestData());
			pricingBean.setResponse(pricing.getResponseData());
			pricingBean.setSiteCode(pricing.getSiteCode());
			pricingResponse.add(pricingBean);
		});
		return pricingResponse;
	}

	/**
	 *
	 * getPricingServiceCompletionStatusByQuote
	 *
	 * @author AnandhiV
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean getPricingServiceCompletionStatusByQuote(Integer quoteId, Integer quoteToLeId)
			throws TclCommonException {
		try {
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				Optional<QuoteToLe> quoteTole = quoteToLeRepository.findById(quoteToLeId);
				if (quoteTole.isPresent()) {
					String value = getLeAttributes(quoteTole.get(), IzosdwanCommonConstants.ISSDWANPRICINGSUCCESS);
					if (value != null && value.toLowerCase().equals("false")) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting pricing service details status", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return true;
	}

	/**
	 *
	 * getPricingServiceListForTheQuote
	 *
	 * @author AnandhiV
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<IzosdwanPricingServiceBean> getPricingServiceListForTheQuote(Integer quoteId)
			throws TclCommonException {

		List<IzosdwanPricingServiceBean> izosdwanPricingServiceBeans = new ArrayList<>();
		try {
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				List<IzosdwanPricingService> izosdwanPricingServices = izosdwanPricingServiceRepository
						.findByRefId(quote.get().getQuoteCode());
				if (izosdwanPricingServices != null && !izosdwanPricingServices.isEmpty()) {
					izosdwanPricingServices.stream().forEach(service -> {
						izosdwanPricingServiceBeans.add(constructIzosdwanPricingServiceBean(service));
					});

				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting pricing service details ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return izosdwanPricingServiceBeans;
	}

	private IzosdwanPricingServiceBean constructIzosdwanPricingServiceBean(
			IzosdwanPricingService izosdwanPricingService) {
		IzosdwanPricingServiceBean izosdwanPricingServiceBean = new IzosdwanPricingServiceBean();
		izosdwanPricingServiceBean.setServiceType(izosdwanPricingService.getServiceType());
		izosdwanPricingServiceBean.setStatus(izosdwanPricingService.getStatus());
		return izosdwanPricingServiceBean;
	}

	public void recalculateQuotePrices(Integer quoteId, Integer quoteToLeId) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			izosdwanIllPricingAndFeasiblityService.recalculate(quoteToLe.get());
		}
	}

	public void updateBillingInfoForSfdc(CustomerLeDetailsBean request, QuoteToLe quoteToLe) throws TclCommonException {
		try {
			construcBillingSfdcAttribute(quoteToLe, request);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void construcBillingSfdcAttribute(QuoteToLe quoteToLe, CustomerLeDetailsBean request)
			throws TclCommonException {
		if (request.getAttributes() != null) {
			request.getAttributes().forEach(billAttr -> {
				constructBillingAttribute(billAttr, quoteToLe);

			});
		}
		processAccount(quoteToLe, request.getAccounCuId(), LeAttributesConstants.ACCOUNT_CUID.toString());
		processAccount(quoteToLe, request.getAccountId(), LeAttributesConstants.ACCOUNT_NO18.toString());
		// processAccount(quoteToLe, String.valueOf(request.getBillingContactId()),
		// LeAttributesConstants.BILLING_CONTACT_ID.toString());
		processAccount(quoteToLe, request.getLegalEntityName(), LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
	}

	private void constructBillingAttribute(Attributes attribute, QuoteToLe quoteToLe) {

		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getAttributeName());

		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			if(quoteToLe.getQuote().getNsQuote()==null || quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("N")) {
				updateAttributes(attribute, quoteLeAttributeValues);
			}

		} else {
			createAttribute(attribute, quoteToLe);

		}

	}

	private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues) {
		quoteLeAttributeValues.forEach(attr -> {
			if (!attr.getMstOmsAttribute().getName().equalsIgnoreCase("Payment Currency")
					&& !attr.getMstOmsAttribute().getName().equalsIgnoreCase("Billing Currency")
					&& !attr.getMstOmsAttribute().getName()
							.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY)) {
				attr.setAttributeValue(attribute.getAttributeValue());
				quoteLeAttributeValueRepository.save(attr);
			}
		});
	}

	private void createAttribute(Attributes attribute, QuoteToLe quoteToLe) {
		QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
		attributeValue.setAttributeValue(attribute.getAttributeValue());
		attributeValue.setDisplayValue(attribute.getAttributeName());
		attributeValue.setQuoteToLe(quoteToLe);
		MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
		attributeValue.setMstOmsAttribute(mstOmsAttribute);
		quoteLeAttributeValueRepository.save(attributeValue);
	}

	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
				if (docusignAudit != null) {
					throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
				}
				order = constructOrder(quote, detail);
				/*
				 * if (checkO2cEnabled(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				 * orderRepository.save(order); } if (checkO2cEnabledForOffnetWireless(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * orderRepository.save(order); }
				 */
				
				skipO2cCustomJouneyOrders(quote, order);
				
				detail.setOrderId(order.getId());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {

					// omsSfdcService.processSiteDetails(quoteLe);
					LOGGER.info("Quote to le id is {}", quoteLe.getId());

					if (quoteLe.getQuote().getNsQuote() != null
							&& quoteLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {

						createSFDCSiteForCustomeJourney(quoteLe);

					} else {
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe,
								IzosdwanCommonConstants.IZOSDWAN_NAME);
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, IzosdwanCommonConstants.CGW);
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, "IAS");
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, "GVPN");
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe,
								IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);

					}

					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

					Boolean nat = (request.getCheckList() == null
							|| request.getCheckList().equalsIgnoreCase(CommonConstants.NO)) ? Boolean.FALSE
									: Boolean.TRUE;
					Map<String, String> cofObjectMapper = new HashMap<>();
					izosdwanQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(),
							cofObjectMapper);
					User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
					String userEmail = null;
					if (userRepo != null) {
						userEmail = userRepo.getEmailId();
					} // Trigger orderMail
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);

					List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
					uploadSSIfNotPresent(quoteLe);
					/**
					 * commented due to requirement change for MSA mapping while optimus journey
					 */
					// uploadMSAIfNotPresent(quoteLe);
				}
			}
			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equalsIgnoreCase(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}
			}
			// triggerClosedBCROnPlacingOrder(quote);
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	protected Order constructOrder(Quote quote, QuoteDetail detail) throws TclCommonException {
		Order order = new Order();
		order.setCreatedBy(quote.getCreatedBy());
		order.setCreatedTime(new Date());
		order.setStatus(quote.getStatus());
		order.setTermInMonths(quote.getTermInMonths());
		order.setCustomer(quote.getCustomer());
		order.setEffectiveDate(quote.getEffectiveDate());
		order.setStatus(quote.getStatus());
		order.setQuote(quote);
		order.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
		order.setEndDate(quote.getEffectiveDate());
		order.setStartDate(quote.getEffectiveDate());
		order.setStatus(quote.getStatus());
		order.setOrderCode(quote.getQuoteCode());
		order.setQuoteCreatedBy(quote.getCreatedBy());
		order.setEngagementOptyId(quote.getEngagementOptyId());
		order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
		order = orderRepository.save(order);
		order.setOrderToLes(constructOrderToLe(quote, order, detail));
		order.setOrderIzosdwanAttributeValue(constructOrderIzosdwanAttributeValues(quote, order));

		constructOrderProductComponentCgw(quote, order);
		constructOrderVutmLocationDetails(quote, order);

		constructOrderCwbAuditTrailDetails(quote, order);
		constructOrderSiteCategory(quote, order);
		constructOrderIzosdwanCpeConfigDetails(quote, order);

		return order;

	}

	private Set<OrderIzosdwanAttributeValue> constructOrderIzosdwanAttributeValues(Quote quote, Order order) {
		Set<OrderIzosdwanAttributeValue> orderIzosdwanAttributeValues = new HashSet<>();
		List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository.findByQuote(quote);
		for (QuoteIzoSdwanAttributeValues quoteIzoSdwanAttributeValue : quoteIzoSdwanAttributeValues) {
			OrderIzosdwanAttributeValue orderIzosdwanAttributeValue = new OrderIzosdwanAttributeValue();
			orderIzosdwanAttributeValue.setOrder(order);
			orderIzosdwanAttributeValue.setDisplayValue(quoteIzoSdwanAttributeValue.getDisplayValue());
			orderIzosdwanAttributeValue.setAttributeValue(quoteIzoSdwanAttributeValue.getAttributeValue());
			orderIzosdwanAttributeValue.setDisplayValue(quoteIzoSdwanAttributeValue.getDisplayValue());
			orderIzosdwanAttributeValues.add(orderIzosdwanAttributeValue);
		}

		// NS quote value saved in order izosdwan attributes.
		if (CommonConstants.Y.equals(quote.getNsQuote())) {
			OrderIzosdwanAttributeValue orderIzosdwanAttributeValue = new OrderIzosdwanAttributeValue();
			orderIzosdwanAttributeValue.setOrder(order);
			orderIzosdwanAttributeValue.setAttributeValue(quote.getNsQuote());
			orderIzosdwanAttributeValue.setDisplayValue(IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE);
			orderIzosdwanAttributeValues.add(orderIzosdwanAttributeValue);
		}


		if (!CollectionUtils.isEmpty(orderIzosdwanAttributeValues)) {
			orderIzosdwanAttributeValueRepository.saveAll(orderIzosdwanAttributeValues);
		}
		return orderIzosdwanAttributeValues;
	}

	private void constructOrderVutmLocationDetails(Quote quote, Order order) {
		List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetails = quoteIzosdwanVutmLocationDetailRepository.findByReferenceId(quote.getId());
		if (!CollectionUtils.isEmpty(quoteIzosdwanVutmLocationDetails)) {
			LOGGER.info("Processing total {} number of quote vutm details", quoteIzosdwanVutmLocationDetails.size());
			List<OrderIzosdwanVutmLocationDetail> orderIzosdwanVutmLocationDetails = quoteIzosdwanVutmLocationDetails.stream().map(quoteIzosdwanVutmLocationDetail -> {
				OrderIzosdwanVutmLocationDetail orderIzosdwanVutmLocationDetail = new OrderIzosdwanVutmLocationDetail();
				orderIzosdwanVutmLocationDetail.setLocationId(quoteIzosdwanVutmLocationDetail.getLocationId());
				orderIzosdwanVutmLocationDetail.setBreakupLocation(quoteIzosdwanVutmLocationDetail.getBreakupLocation());
				orderIzosdwanVutmLocationDetail.setMaxBw(quoteIzosdwanVutmLocationDetail.getMaxBw());
				orderIzosdwanVutmLocationDetail.setDefaultBw(quoteIzosdwanVutmLocationDetail.getDefaultBw());
				orderIzosdwanVutmLocationDetail.setSelectedBw(quoteIzosdwanVutmLocationDetail.getSelectedBw());
				orderIzosdwanVutmLocationDetail.setIsActive(quoteIzosdwanVutmLocationDetail.getIsActive());
				orderIzosdwanVutmLocationDetail.setReferenceId(order.getId());
				return orderIzosdwanVutmLocationDetail;
			}).collect(Collectors.toList());

			LOGGER.info("Total order vutm details: {}", orderIzosdwanVutmLocationDetails.size());
			orderIzosdwanVutmLocationDetailRepository.saveAll(orderIzosdwanVutmLocationDetails);
		}
	}

	protected List<OrderCwbAuditTrailDetails> constructOrderCwbAuditTrailDetails(Quote quote, Order order)
			throws TclCommonException {
		LOGGER.info("entering into constructOrderCwbAuditTrailDetails method");
		List<OrderCwbAuditTrailDetails> orderCwbAuditTrailDetailList = new ArrayList<>();
		try {
			List<AuditCwbTrailDetails> auditCwbTrailDetail = auditCwbTrailDetailsRepository
					.findAllByQuoteId(quote.getId());

			for (AuditCwbTrailDetails auditCwbTrailDetails : auditCwbTrailDetail) {
				OrderCwbAuditTrailDetails orderCwbAuditTrailDetails = new OrderCwbAuditTrailDetails();
				orderCwbAuditTrailDetails.setId(auditCwbTrailDetails.getId());
				orderCwbAuditTrailDetails.setOrderId(order.getId());
				orderCwbAuditTrailDetails.setCustomerId(auditCwbTrailDetails.getCustomerId());
				orderCwbAuditTrailDetails.setUserName(auditCwbTrailDetails.getUserName());
				orderCwbAuditTrailDetails.setCurrency(auditCwbTrailDetails.getCurrency());
				orderCwbAuditTrailDetails.setVersionNo(auditCwbTrailDetails.getVersionNo());
				orderCwbAuditTrailDetails.setUploadUrl(auditCwbTrailDetails.getUploadUrl());
				orderCwbAuditTrailDetails.setDownloadUrl(auditCwbTrailDetails.getDownloadUrl());
				orderCwbAuditTrailDetails.setCreatedBy(auditCwbTrailDetails.getCreatedBy());
				orderCwbAuditTrailDetails.setUpdatedBy(auditCwbTrailDetails.getUpdatedBy());
				orderCwbAuditTrailDetails.setDownloadDateTime(auditCwbTrailDetails.getDownloadDateTime());
				orderCwbAuditTrailDetails.setUploadDateTime(auditCwbTrailDetails.getUploadDateTime());
				orderCwbAuditTrailDetails.setContractTerm(auditCwbTrailDetails.getContractTerm());
				orderCwbAuditTrailDetails.setReasonForReupload(auditCwbTrailDetails.getReasonForReupload());
				orderCwbAuditTrailDetailList.add(orderCwbAuditTrailDetails);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderAuditCwbTrailDetailsRepository.saveAll(orderCwbAuditTrailDetailList);
	}

	protected List<OrderSiteCategory> constructOrderSiteCategory(Quote quote, Order order) throws TclCommonException {
		LOGGER.info("entering into constructOrderSiteCategory method");
		List<OrderSiteCategory> orderSiteCategoryList = new ArrayList<>();
		try {
			List<SiteCategory> siteCategoryList = siteCategoryRepository.findAllByQuoteId(quote.getId());
			for (SiteCategory siteCategory : siteCategoryList) {
				OrderSiteCategory orderSiteCategory = new OrderSiteCategory();
				orderSiteCategory.setId(siteCategory.getId());
				orderSiteCategory.setErfLocSitebLocationId(siteCategory.getErfLocSitebLocationId());
				orderSiteCategory.setSiteCategory(siteCategory.getSiteCategory());
				orderSiteCategory.setOrderId(order.getId());
				orderSiteCategoryList.add(orderSiteCategory);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderSiteCategoryRepository.saveAll(orderSiteCategoryList);
	}

	protected List<OrderIzosdwanCpeConfigDetails> constructOrderIzosdwanCpeConfigDetails(Quote quote, Order order)
			throws TclCommonException {
		LOGGER.info("entering into constructOrderSiteCategory method");
		List<OrderIzosdwanCpeConfigDetails> orderIzosdwanCpeConfigDetailsList = new ArrayList<>();
		try {
			List<QuoteToLe> quToLes = quoteToLeRepository.findByQuote(quote);
			List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(order);
			Integer orderToLeId = null;
			if (!orderToLes.isEmpty()) {
				OrderToLe orderToLe = orderToLes.get(0);
				orderToLeId = orderToLe.getId();
			}
			if (!quToLes.isEmpty()) {
				QuoteToLe quoteToLe = quToLes.get(0);
				List<CpeBomDetails> cpeBomDetailsList = cpeBomDetailsRepository.findAllByQuoteLeId(quoteToLe.getId());
				for (CpeBomDetails cpeBomDetails : cpeBomDetailsList) {

					OrderIzosdwanCpeConfigDetails orderIzosdwanCpeConfigDetails = new OrderIzosdwanCpeConfigDetails();
					orderIzosdwanCpeConfigDetails.setId(cpeBomDetails.getId());
					orderIzosdwanCpeConfigDetails.setOrderLeId(orderToLeId);
					orderIzosdwanCpeConfigDetails.setLocationId(cpeBomDetails.getLocationId());
					orderIzosdwanCpeConfigDetails.setAttributeType(cpeBomDetails.getAttributeType());
					orderIzosdwanCpeConfigDetails.setAttributeName(cpeBomDetails.getAttributeName());
					orderIzosdwanCpeConfigDetails.setAttributeValue(cpeBomDetails.getAttributeValue());
					orderIzosdwanCpeConfigDetails.setDesc(cpeBomDetails.getDesc());
					orderIzosdwanCpeConfigDetails.setQuantity(cpeBomDetails.getQuantity());
					orderIzosdwanCpeConfigDetails.setParentId(cpeBomDetails.getParentId());
					orderIzosdwanCpeConfigDetails.setCreatedBy(cpeBomDetails.getCreatedBy());
					orderIzosdwanCpeConfigDetails.setCreatedTime(cpeBomDetails.getCreatedTime());
					orderIzosdwanCpeConfigDetails.setUpdatedBy(cpeBomDetails.getUpdatedBy());
					orderIzosdwanCpeConfigDetails.setUpdatedTime(cpeBomDetails.getUpdatedTime());

					orderIzosdwanCpeConfigDetailsList.add(orderIzosdwanCpeConfigDetails);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderIzosdwanCpeConfigDetailsRepository.saveAll(orderIzosdwanCpeConfigDetailsList);
	}

	private Set<OrderToLe> constructOrderToLe(Quote quote, Order order, QuoteDetail detail) {

		return getOrderToLeBasenOnVersion(quote, order, detail);

	}

	private Set<OrderToLe> getOrderToLeBasenOnVersion(Quote quote, Order order, QuoteDetail detail) {
		List<QuoteToLe> quToLes = null;
		Set<OrderToLe> orderToLes = null;

		quToLes = quoteToLeRepository.findByQuote(quote);
		if (quToLes != null) {
			orderToLes = new HashSet<>();
			for (QuoteToLe quoteToLe : quToLes) {
				OrderToLe orderToLe = new OrderToLe();
				orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
				orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
				orderToLe.setFinalArc(quoteToLe.getFinalArc());
				orderToLe.setOrder(order);
				orderToLe.setIsAmended(
						Objects.nonNull(quoteToLe.getIsAmended()) ? quoteToLe.getIsAmended() : BDEACTIVATE);
				orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
				orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
				orderToLe.setProposedArc(quoteToLe.getProposedArc());
				orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
				orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
				orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
				orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
				orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
				orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
				orderToLe.setClassification(quoteToLe.getClassification());
				orderToLe.setPreapprovedOpportunityFlag(quoteToLe.getPreapprovedOpportunityFlag());
				orderToLe.setTpsSfdcApprovedMrc(quoteToLe.getTpsSfdcApprovedMrc());
				orderToLe.setTpsSfdcApprovedNrc(quoteToLe.getTpsSfdcApprovedNrc());
				orderToLe.setTpsSfdcApprovedBy(quoteToLe.getTpsSfdcApprovedBy());
				orderToLe.setTpsSfdcReservedBy(quoteToLe.getTpsSfdcReservedBy());
				orderToLe.setTpsSfdcCreditApprovalDate(quoteToLe.getTpsSfdcCreditApprovalDate());
				orderToLe.setTpsSfdcCreditRemarks(quoteToLe.getTpsSfdcCreditRemarks());
				orderToLe.setTpsSfdcDifferentialMrc(quoteToLe.getTpsSfdcDifferentialMrc());
				orderToLe.setTpsSfdcStatusCreditControl(quoteToLe.getTpsSfdcStatusCreditControl());
				orderToLe.setVariationApprovedFlag(quoteToLe.getVariationApprovedFlag());
				orderToLe.setTpsSfdcSecurityDepositAmount(quoteToLe.getTpsSfdcSecurityDepositAmount());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setCreditCheckTrigerred(quoteToLe.getCreditCheckTriggered());
				orderToLe.setTpsSfdcCreditLimit(quoteToLe.getTpsSfdcCreditLimit());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
				orderToLe.setIsMultiCircuit(quoteToLe.getIsMultiCircuit());
				orderToLeRepository.save(orderToLe);
				orderToLe.setOrdersLeAttributeValues(constructOrderToLeAttribute(orderToLe, quoteToLe));
				detail.getOrderLeIds().add(orderToLe.getId());
				orderToLe
						.setOrderToLeProductFamilies(getOrderProductFamilyBasenOnVersion(quoteToLe, orderToLe, detail));
				orderToLes.add(orderToLe);

			}

		}

		return orderToLes;

	}

	private Set<OrdersLeAttributeValue> constructOrderToLeAttribute(OrderToLe orderToLe, QuoteToLe quoteToLe) {
		Set<OrdersLeAttributeValue> attributeValues = new HashSet<>();

		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
		if (quoteLeAttributeValues != null) {
			quoteLeAttributeValues.stream().forEach(attrVal -> {
				OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
				ordersLeAttributeValue.setAttributeValue(attrVal.getAttributeValue());
				ordersLeAttributeValue.setDisplayValue(attrVal.getDisplayValue());
				ordersLeAttributeValue.setMstOmsAttribute(attrVal.getMstOmsAttribute());
				ordersLeAttributeValue.setOrderToLe(orderToLe);
				ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
				attributeValues.add(ordersLeAttributeValue);

			});
		}

		return attributeValues;
	}

	private Set<OrderToLeProductFamily> getOrderProductFamilyBasenOnVersion(QuoteToLe quote, OrderToLe orderToLe,
			QuoteDetail detail) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		Set<OrderToLeProductFamily> orderFamilys = null;

		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		if (prodFamilys != null) {
			orderFamilys = new HashSet<>();
			for (QuoteToLeProductFamily quoteToLeProductFamily : prodFamilys) {
				OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
				orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
				orderToLeProductFamily.setOrderToLe(orderToLe);
				orderToLeProductFamilyRepository.save(orderToLeProductFamily);
				orderToLeProductFamily.setOrderProductSolutions(constructOrderProductSolution(
						quoteToLeProductFamily.getProductSolutions(), orderToLeProductFamily, detail));
				orderFamilys.add(orderToLeProductFamily);
//				if (!PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				/*
				 * if (!partnerService.quoteCreatedByPartner(quote.getQuote().getId())) {
				 * processEngagement(quote, quoteToLeProductFamily); }
				 */
//				}
			}
		}

		return orderFamilys;

	}

	private Set<OrderProductSolution> constructOrderProductSolution(Set<ProductSolution> productSolutions,
			OrderToLeProductFamily orderToLeProductFamily, QuoteDetail detail) {

		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				List<QuoteIzosdwanSite> quoteIllSites = getIllsitesBasenOnVersion(solution, null, null);

				OrderProductSolution oSolution = new OrderProductSolution();
				if (solution.getMstProductOffering() != null) {
					oSolution.setMstProductOffering(solution.getMstProductOffering());
				}
				oSolution.setSolutionCode(solution.getSolutionCode());
				oSolution.setTpsSfdcProductId(solution.getTpsSfdcProductId());
				oSolution.setTpsSfdcProductName(solution.getTpsSfdcProductName());
				oSolution.setOrderToLeProductFamily(orderToLeProductFamily);
				oSolution.setProductProfileData(solution.getProductProfileData());
				orderProductSolutionRepository.save(oSolution);
				if (quoteIllSites != null && !quoteIllSites.isEmpty()) {
					oSolution.setOrderIzosdwanSites(constructOrderIllSite(quoteIllSites, oSolution, detail));
				}
				// process addon product component attributes
				if (IzosdwanCommonConstants.VUTM.equals(orderToLeProductFamily.getMstProductFamily().getName()) || IzosdwanCommonConstants.VPROXY.equals(orderToLeProductFamily.getMstProductFamily().getName())) {
					String referenceName = IzosdwanCommonConstants.VUTM.equals(orderToLeProductFamily.getMstProductFamily().getName()) ? IzosdwanCommonConstants.IZOSDWAN_VUTM : IzosdwanCommonConstants.IZOSDWAN_VPROXY;
					constructOrderProductComponentForAddons(solution.getId(), oSolution.getId(), referenceName);
				}
				orderProductSolution.add(oSolution);

			}
		}

		return orderProductSolution;
	}

	private Set<OrderIzosdwanSite> constructOrderIllSite(List<QuoteIzosdwanSite> illSites,
			OrderProductSolution oSolution, QuoteDetail detail) {
		Set<OrderIzosdwanSite> sites = new HashSet<>();

		if (illSites != null) {
			for (QuoteIzosdwanSite illSite : illSites) {
				if (illSite.getStatus() == 1 && illSite.getFeasibility() == 1) {
					OrderIzosdwanSite orderSite = new OrderIzosdwanSite();
					orderSite.setIsTaxExempted(illSite.getIsTaxExempted());
					orderSite.setStatus((byte) 1);
					orderSite.setErfLocSiteaLocationId(illSite.getErfLocSiteaLocationId());
					orderSite.setErfLocSitebLocationId(illSite.getErfLocSitebLocationId());
					orderSite.setErfLocSiteaSiteCode(illSite.getErfLocSiteaSiteCode());
					orderSite.setErfLocSitebSiteCode(illSite.getErfLocSitebSiteCode());
					orderSite.setErfLrSolutionId(illSite.getErfLrSolutionId());
					orderSite.setImageUrl(illSite.getImageUrl());
					orderSite.setCreatedBy(illSite.getCreatedBy());
					orderSite.setCreatedTime(new Date());
					orderSite.setFeasibility(illSite.getFeasibility());
					orderSite.setOrderProductSolution(oSolution);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date()); // Now use today date.
					cal.add(Calendar.DATE, 130); // Adding 130 days
					orderSite.setEffectiveDate(cal.getTime());
					orderSite.setMrc(illSite.getMrc());
					orderSite.setFpStatus(illSite.getFpStatus());
					orderSite.setArc(illSite.getArc());
					orderSite.setTcv(illSite.getTcv());
					orderSite.setSiteCode(illSite.getSiteCode());
					orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
					orderSite.setNrc(illSite.getNrc());
					orderSite.setIzosdwanSiteType(illSite.getIzosdwanSiteType());
					orderSite.setRequestorDate(illSite.getRequestorDate());
					orderSite.setSiteCategoryId(illSite.getSiteCategoryId());
					orderIzosdwanSiteRepository.save(orderSite);
					orderSite.setOrderIzosdwanSiteFeasibilities(constructOrderSiteFeasibility(illSite, orderSite));
					orderSite.setOrderIzosdwanSiteSlas(constructOrderSiteSla(illSite, orderSite));
					orderSite.setIzosdwanSiteOffering(illSite.getIzosdwanSiteOffering());
					orderSite.setIzosdwanSiteProduct(illSite.getIzosdwanSiteProduct());
					orderSite.setIzosdwanSiteType(illSite.getIzosdwanSiteType());
					orderSite.setLatLong(illSite.getLatLong());
					orderSite.setManagementType(illSite.getManagementType());
					orderSite.setMarkupPct(illSite.getMarkupPct());
					orderSite.setNewCpe(illSite.getNewCpe());
					orderSite.setNewLastmileBandwidth(illSite.getNewLastmileBandwidth());
					orderSite.setNewPortBandwidth(illSite.getNewPortBandwidth());
					orderSite.setOldCpe(illSite.getOldCpe());
					orderSite.setOldPortBandwidth(illSite.getOldPortBandwidth());
					orderSite.setPrimaryServiceId(illSite.getPrimaryServiceId());
					orderSite.setPriSec(illSite.getPriSec());
					orderSite.setServiceSiteAddress(illSite.getServiceSiteAddress());
					orderSite.setServiceSiteCountry(illSite.getServiceSiteCountry());
					orderSite.setSiParentOrderId(illSite.getSiParentOrderId());
					orderSite.setSiServiceDetailId(illSite.getSiServiceDetailsId());
					orderSite.setErfServiceInventoryTpsServiceId(illSite.getErfServiceInventoryTpsServiceId());
					orderSite.setIsShared(illSite.getIsShared());

					/*
					 * Optional<OrderToLe> orderToLe =
					 * orderToLeRepository.findById(detail.getOrderLeIds().get(0));
					 * if(orderToLe.isPresent() &&
					 * orderToLe.get().getOrderType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE
					 * )){
					 */
					/*
					 * String quoteType =
					 * illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().
					 * getQuoteType(); QuoteToLe quoteToLe =
					 * illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					 * LOGGER.info("quoteToLe quote type {}",quoteType ); if(quoteType != null &&
					 * MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteType)){
					 *
					 * constructOrderIllSiteToService(illSite, orderSite); }
					 */
					constructOrderIllSiteToService(illSite, orderSite);
					constructOrderProductComponent(illSite.getId(), orderSite);
					sites.add(orderSite);
				} else {
					detail.setManualFeasible(true);
				}
			}
		}

		return sites;
	}

	/**
	 * This method is to save OrderIllSiteToService
	 *
	 * @author Madhumiethaa Palanisamy
	 * @param illSite
	 * @param orderSite
	 * @throws TclCommonException
	 */
	private void constructOrderIllSiteToService(QuoteIzosdwanSite illSite, OrderIzosdwanSite orderSite) {
		try {
			String[] nsQuote = { null };
			LOGGER.info(
					"Inside IllQuoteService.constructOrderIllSiteToService to save orderIllSiteService for orderSiteId {} ",
					orderSite.getId());
			List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository
					.findByQuoteIzosdwanSite(illSite);
			List<OrderIllSiteToService> orderIllSiteToServices = new ArrayList<>();
			Quote quote = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
			if (quote != null) {
				nsQuote[0] = quote.getNsQuote();
			}
			LOGGER.info("NS Quote {}", nsQuote[0]);
			if (!quoteIllSiteServices.isEmpty()) {
				quoteIllSiteServices.stream().forEach(quoteSiteService -> {
					LOGGER.info("Setting orderIllSiteTOService for siteId  {} and QuoteLe  {}",
							quoteSiteService.getQuoteIzosdwanSite().getId(), quoteSiteService.getQuoteToLe().getId());
					OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
					orderIllSiteToService.setErfServiceInventoryParentOrderId(
							quoteSiteService.getErfServiceInventoryParentOrderId());
					orderIllSiteToService.setErfServiceInventoryServiceDetailId(
							quoteSiteService.getErfServiceInventoryServiceDetailId());
					orderIllSiteToService
							.setErfServiceInventoryTpsServiceId(quoteSiteService.getErfServiceInventoryTpsServiceId());

					orderIllSiteToService.setOrderIzosdwanSite(orderSite);
					orderIllSiteToService.setTpsSfdcParentOptyId(quoteSiteService.getTpsSfdcParentOptyId());
					orderIllSiteToService.setType(quoteSiteService.getType());
					orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
					if (Objects.nonNull(nsQuote[0]) && CommonConstants.Y.equalsIgnoreCase(nsQuote[0])) {
						List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
								.findByReferenceIdAndMstProductComponent_NameAndReferenceName(illSite.getId(),
										IllSitePropertiesConstants.SITE_PROPERTIES.toString(),
										QuoteConstants.IZOSDWAN_SITES.toString());
						quoteProductComponentList.stream()
								.filter(prodCom -> (quoteSiteService.getType().equalsIgnoreCase(prodCom.getType())))
								.forEach(prodComponent -> {
									LOGGER.info("Entering prod component list loop");
									Optional<QuoteProductComponentsAttributeValue> attValue = prodComponent
											.getQuoteProductComponentsAttributeValues().stream()
											.filter(prodCompAttValue -> IllSitePropertiesConstants.SFDC_ORDER_TYPE
													.toString().equalsIgnoreCase(
															prodCompAttValue.getProductAttributeMaster().getName()))
											.findFirst();

									if (attValue.isPresent()) {
										LOGGER.info("attValue for site properties {} ",
												attValue.get().getAttributeValues());
										orderIllSiteToService.setErfSfdcOrderType(attValue.get().getAttributeValues());
										orderIllSiteToService.setErfSfdcSubType(attValue.get().getAttributeValues());
									}

								});
						if (orderIllSiteToService.getErfSfdcOrderType() == null) {
							orderIllSiteToService.setErfSfdcOrderType(quoteSiteService.getErfSfdcOrderType());
						}
						if (orderIllSiteToService.getErfSfdcSubType() == null) {
							orderIllSiteToService.setErfSfdcSubType(quoteSiteService.getErfSfdcSubType());
						}
					} else {
						orderIllSiteToService.setErfSfdcOrderType(quoteSiteService.getErfSfdcOrderType());
						orderIllSiteToService.setErfSfdcSubType(quoteSiteService.getErfSfdcSubType());
					}
					orderIllSiteToService.setOrderToLe(
							orderSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe());
					if (Objects.nonNull(quoteSiteService.getQuoteIzosdwanSite().getProductSolution()
							.getQuoteToLeProductFamily().getQuoteToLe().getIsAmended())) {
						int result = Byte.compare(BACTIVE, quoteSiteService.getQuoteIzosdwanSite().getProductSolution()
								.getQuoteToLeProductFamily().getQuoteToLe().getIsAmended());
						if (result == 0) {
							orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
							orderIllSiteToService.setParentSiteId(Objects.nonNull(quoteSiteService.getParentSiteId())
									? quoteSiteService.getParentSiteId()
									: -1);
							orderIllSiteToService.setParentOrderId(Objects.nonNull(quoteSiteService.getParentOrderId())
									? quoteSiteService.getParentOrderId()
									: -1);
							orderIllSiteToService.setServiceType(Objects.nonNull(quoteSiteService.getServiceType())
									? quoteSiteService.getServiceType()
									: "NA");
							orderIllSiteToService.setO2cServiceId(Objects.nonNull(quoteSiteService.getO2cServiceId())
									? quoteSiteService.getO2cServiceId()
									: "NA");
						}
					}

					String iasQueueResponse = null;
					try {
						iasQueueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsQueue,
								quoteSiteService.getErfServiceInventoryTpsServiceId());

						if (StringUtils.isNotBlank(iasQueueResponse)) {
							LOGGER.info("queue response from si in constructOrderIllSiteToService {}",
									iasQueueResponse);
							SIServiceInfoBean[] siDetailedInfoResponseIAS = (SIServiceInfoBean[]) Utils
									.convertJsonToObject(iasQueueResponse, SIServiceInfoBean[].class);
							List<SIServiceInfoBean> siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseIAS);
							Optional<SIServiceInfoBean> siServiceInfoBeanOne = siServiceInfoResponse.stream()
									.filter(service -> service.getTpsServiceId()
											.equals(quoteSiteService.getErfServiceInventoryTpsServiceId()))
									.findFirst();
							orderIllSiteToService.setErfServiceInventoryO2cLinkType(
									siServiceInfoBeanOne.get().getPrimaryOrSecondary());
							if (Objects.nonNull(siServiceInfoResponse) && siServiceInfoResponse.size() > 1) {
								LOGGER.info("more than one record in response from queue");
								Optional<SIServiceInfoBean> siServiceInfoBean = siServiceInfoResponse.stream()
										.filter(service -> !service.getTpsServiceId()
												.equals(quoteSiteService.getErfServiceInventoryTpsServiceId()))
										.findFirst();
								if (siServiceInfoBean.isPresent()) {
									orderIllSiteToService.setErfServiceInventoryPriSecLinkServiceId(
											siServiceInfoBean.get().getTpsServiceId());
									Optional<QuoteIllSiteToService> qSiteToService = quoteIllSiteServices.stream()
											.filter(siteToService -> siteToService.getErfServiceInventoryTpsServiceId()
													.equalsIgnoreCase(siServiceInfoBean.get().getTpsServiceId()))
											.findAny();
									String quoteCategory = illSite.getProductSolution().getQuoteToLeProductFamily()
											.getQuoteToLe().getQuoteCategory();
									String macdChangeBandwidhFlag = illSite.getMacdChangeBandwidthFlag();
									LOGGER.info("quote Category {}, macdChangeBandwidthFlag {}", quoteCategory,
											macdChangeBandwidhFlag);
									if (!qSiteToService.isPresent()
											&& ((MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteCategory)
													&& MACDConstants.BOTH_STRING
															.equalsIgnoreCase(macdChangeBandwidhFlag))
													|| MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteCategory))
											&& (Objects.nonNull(nsQuote[0])
													&& CommonConstants.N.equalsIgnoreCase(nsQuote[0]))) {
										LOGGER.info("Service Id {}, primary or secondary {}",
												siServiceInfoBean.get().getTpsServiceId(),
												siServiceInfoBean.get().getPrimaryOrSecondary());
										OrderIllSiteToService relatedServiceDetail = new OrderIllSiteToService();
										relatedServiceDetail.setErfServiceInventoryParentOrderId(
												siServiceInfoBean.get().getSiOrderId());
										relatedServiceDetail.setErfServiceInventoryPriSecLinkServiceId(
												orderIllSiteToService.getErfServiceInventoryTpsServiceId());
										relatedServiceDetail
												.setErfServiceInventoryServiceDetailId(siServiceInfoBean.get().getId());
										relatedServiceDetail.setErfServiceInventoryTpsServiceId(
												siServiceInfoBean.get().getTpsServiceId());
										relatedServiceDetail.setOrderIzosdwanSite(orderSite);
										relatedServiceDetail.setTpsSfdcParentOptyId(
												siServiceInfoBean.get().getTpsSfdcParentOptyId());
										relatedServiceDetail.setErfServiceInventoryO2cLinkType(
												siServiceInfoBean.get().getPrimaryOrSecondary());
										if (MACDConstants.DUAL_PRIMARY
												.equalsIgnoreCase(siServiceInfoBean.get().getPrimaryOrSecondary())
												|| MACDConstants.SINGLE_ALL_CAPS.equalsIgnoreCase(
														siServiceInfoBean.get().getPrimaryOrSecondary())) {

											relatedServiceDetail.setType(PDFConstants.PRIMARY);
										}
										if (MACDConstants.DUAL_SECONDARY
												.equalsIgnoreCase(siServiceInfoBean.get().getPrimaryOrSecondary()))
											relatedServiceDetail.setType(PDFConstants.SECONDARY);

										relatedServiceDetail.setAllowAmendment(quoteSiteService.getAllowAmendment());
										relatedServiceDetail
												.setErfSfdcOrderType(orderIllSiteToService.getErfSfdcOrderType());
										relatedServiceDetail
												.setErfSfdcSubType(orderIllSiteToService.getErfSfdcSubType());

										relatedServiceDetail.setOrderToLe(orderSite.getOrderProductSolution()
												.getOrderToLeProductFamily().getOrderToLe());
										if (Objects.nonNull(quoteSiteService.getQuoteIzosdwanSite().getProductSolution()
												.getQuoteToLeProductFamily().getQuoteToLe().getIsAmended())) {
											int result = Byte.compare(BACTIVE,
													quoteSiteService.getQuoteIzosdwanSite().getProductSolution()
															.getQuoteToLeProductFamily().getQuoteToLe().getIsAmended());
											if (result == 0) {
												relatedServiceDetail
														.setAllowAmendment(quoteSiteService.getAllowAmendment());
												relatedServiceDetail.setParentSiteId(
														Objects.nonNull(quoteSiteService.getParentSiteId())
																? quoteSiteService.getParentSiteId()
																: -1);
												relatedServiceDetail.setParentOrderId(
														Objects.nonNull(quoteSiteService.getParentOrderId())
																? quoteSiteService.getParentOrderId()
																: -1);
												relatedServiceDetail.setServiceType(
														Objects.nonNull(quoteSiteService.getServiceType())
																? quoteSiteService.getServiceType()
																: "NA");
												relatedServiceDetail.setO2cServiceId(
														Objects.nonNull(quoteSiteService.getO2cServiceId())
																? quoteSiteService.getO2cServiceId()
																: "NA");
											}
										}
										orderIllSiteToServices.add(relatedServiceDetail);
									}
								}

							}

						}

					} catch (Exception e) {
						LOGGER.error("error in queue call siRelatedDetailsQueue in constructOrderIllSiteToService {}",
								e);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}

					orderIllSiteToServices.add(orderIllSiteToService);
				});
				orderIllSiteToServiceRepository.saveAll(orderIllSiteToServices);
				LOGGER.info("Inside IllQuoteService.constructOrderIllSiteToService Saved orderillSiteToServicesss");
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while saving orderIllSiteToServices {} ", e);
		}
	}

	private Set<OrderIzosdwanSiteFeasibility> constructOrderSiteFeasibility(QuoteIzosdwanSite quoteIzosdwanSite,
			OrderIzosdwanSite orderSite) {
		Set<OrderIzosdwanSiteFeasibility> orderSiteFeasibilities = new HashSet<>();

		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIzosdwanSite(quoteIzosdwanSite);
		if (siteFeasibilities != null) {
			siteFeasibilities.forEach(sitefeas -> {
				OrderIzosdwanSiteFeasibility orderSiteFeasibility = new OrderIzosdwanSiteFeasibility();
				orderSiteFeasibility.setFeasibilityCode(sitefeas.getFeasibilityCode());
				orderSiteFeasibility.setFeasibilityCheck(sitefeas.getFeasibilityCheck());
				orderSiteFeasibility.setFeasibilityMode(sitefeas.getFeasibilityMode());
				orderSiteFeasibility.setIsSelected(sitefeas.getIsSelected());
				LocalDateTime localDateTime = LocalDateTime.now();
				orderSiteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
				orderSiteFeasibility.setOrderIzosdwanSite(orderSite);
				orderSiteFeasibility.setType(sitefeas.getType());
				orderSiteFeasibility.setProvider(sitefeas.getProvider());
				orderSiteFeasibility.setRank(sitefeas.getRank());
				orderSiteFeasibility.setResponseJson(sitefeas.getResponseJson());
				orderSiteFeasibility.setSfdcFeasibilityId(sitefeas.getSfdcFeasibilityId());
				orderSiteFeasibility.setFeasibilityType(sitefeas.getFeasibilityType());
				orderSiteFeasibilityRepository.save(orderSiteFeasibility);
				orderSiteFeasibilities.add(orderSiteFeasibility);

			});
		}

		return orderSiteFeasibilities;
	}

	private Set<OrderIzosdwanSiteSla> constructOrderSiteSla(QuoteIzosdwanSite illSite, OrderIzosdwanSite orderSite) {
		Set<OrderIzosdwanSiteSla> orderIllSiteSlas = new HashSet<>();

		if (illSite.getQuoteIzosdwanSiteSlas() != null) {
			illSite.getQuoteIzosdwanSiteSlas().forEach(illsiteSla -> {
				OrderIzosdwanSiteSla orderIllSiteSla = new OrderIzosdwanSiteSla();
				orderIllSiteSla.setOrderIzosdwanSite(orderSite);
				orderIllSiteSla.setSlaEndDate(illsiteSla.getSlaEndDate());
				orderIllSiteSla.setSlaStartDate(illsiteSla.getSlaStartDate());
				orderIllSiteSla.setSlaValue(illsiteSla.getSlaValue());
				orderIllSiteSla.setSlaMaster(illsiteSla.getSlaMaster());
				orderIzosdwanSiteSlaRepository.save(orderIllSiteSla);
				orderIllSiteSlas.add(orderIllSiteSla);

			});
		}

		return orderIllSiteSlas;
	}

	private List<OrderProductComponent> constructOrderProductComponent(Integer id, OrderIzosdwanSite illSite) {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(id, QuoteConstants.IZOSDWAN_SITES.toString());
		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				OrderProductComponent orderProductComponent = new OrderProductComponent();
				orderProductComponent.setReferenceId(illSite.getId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
				}
				orderProductComponent.setType(quoteProductComponent.getType());
				orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
				orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
				orderProductComponentRepository.save(orderProductComponent);
				constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
				List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());
				orderProductComponent.setOrderProductComponentsAttributeValues(
						constructOrderAttribute(attributes, orderProductComponent));
				orderProductComponents.add(orderProductComponent);
			}

		}
		return orderProductComponents;

	}

	private List<OrderProductComponent> constructOrderProductComponentForAddons(final Integer id, final Integer referenceId, final String referenceName) {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(id, referenceName);
		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				OrderProductComponent orderProductComponent = new OrderProductComponent();
				orderProductComponent.setReferenceId(referenceId);
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
				}
				orderProductComponent.setType(quoteProductComponent.getType());
				orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
				orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
				orderProductComponentRepository.save(orderProductComponent);
				constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
				List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());
				orderProductComponent.setOrderProductComponentsAttributeValues(
						constructOrderAttribute(attributes, orderProductComponent));
				orderProductComponents.add(orderProductComponent);
			}

		}
		return orderProductComponents;

	}

	private void constructOrderProductComponentCgw(Quote quote, Order order) {

		List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetailList = quoteIzosdwanCgwDetailRepository
				.findByQuote_Id(quote.getId());
		int numberOfCgwDetails = CollectionUtils.isEmpty(quoteIzosdwanCgwDetailList) ? 0 : quoteIzosdwanCgwDetailList.size();
		LOGGER.info("Total: {} CGW details are found for quoteId: {}, quoteCode:{}", numberOfCgwDetails, quote.getId(), quote.getQuoteCode());
		
		if (quoteIzosdwanCgwDetailList != null && !quoteIzosdwanCgwDetailList.isEmpty()) {
			for (QuoteIzosdwanCgwDetail quoteIzosdwanCgwDetail : quoteIzosdwanCgwDetailList) {
				OrderIzosdwanCgwDetail orderIzosdwanCgwDetail = new OrderIzosdwanCgwDetail();
				orderIzosdwanCgwDetail.setCreatedBy(quote.getCreatedBy());
				orderIzosdwanCgwDetail.setCreatedTime(new Date());
				orderIzosdwanCgwDetail.setHetroBw(quoteIzosdwanCgwDetail.getHetroBw());
				orderIzosdwanCgwDetail.setMigrationSystemBw(quoteIzosdwanCgwDetail.getMigrationSystemBw());
				orderIzosdwanCgwDetail.setMigrationUserBw(quoteIzosdwanCgwDetail.getMigrationUserBw());
				orderIzosdwanCgwDetail.setOrder(order);
				orderIzosdwanCgwDetail.setPrimaryLocation(quoteIzosdwanCgwDetail.getPrimaryLocation());
				orderIzosdwanCgwDetail.setSecondaryLocation(quoteIzosdwanCgwDetail.getSecondaryLocation());
				orderIzosdwanCgwDetail.setUpdatedTime(new Date());
				orderIzosdwanCgwDetail.setUpdatedBy(quoteIzosdwanCgwDetail.getUpdatedBy());
				orderIzosdwanCgwDetail.setUseCase1(quoteIzosdwanCgwDetail.getUseCase1a());
				orderIzosdwanCgwDetail.setUseCase2(quoteIzosdwanCgwDetail.getUseCase2());
				orderIzosdwanCgwDetail.setUseCase3(quoteIzosdwanCgwDetail.getUseCase3());
				orderIzosdwanCgwDetail.setUseCase4(quoteIzosdwanCgwDetail.getUseCase4());
			// newly added
			orderIzosdwanCgwDetail.setUseCase1a(quoteIzosdwanCgwDetail.getUseCase1a());
			orderIzosdwanCgwDetail.setUseCase1aBw(quoteIzosdwanCgwDetail.getUseCase1aBw());
			orderIzosdwanCgwDetail.setUseCase1aRefId(quoteIzosdwanCgwDetail.getUseCase1aRefId());
			orderIzosdwanCgwDetail.setUseCase1b(quoteIzosdwanCgwDetail.getUseCase1b());
			orderIzosdwanCgwDetail.setUseCase1bBw(quoteIzosdwanCgwDetail.getUseCase1bBw());
			orderIzosdwanCgwDetail.setUseCase1bRefId(quoteIzosdwanCgwDetail.getUseCase1bRefId());
			orderIzosdwanCgwDetail.setUseCase2Bw(quoteIzosdwanCgwDetail.getUseCase2Bw());
			orderIzosdwanCgwDetail.setUseCase2RefId(quoteIzosdwanCgwDetail.getUseCase2RefId());
			orderIzosdwanCgwDetail.setUseCase3Bw(quoteIzosdwanCgwDetail.getUseCase3Bw());
			orderIzosdwanCgwDetail.setUseCase3RefId(quoteIzosdwanCgwDetail.getUseCase3RefId());
			orderIzosdwanCgwDetail.setUseCase4Bw(quoteIzosdwanCgwDetail.getUseCase4Bw());
			orderIzosdwanCgwDetail.setUseCase4RefId(quoteIzosdwanCgwDetail.getUseCase4RefId());
			orderIzosdwanCgwDetail.setCosModel(quoteIzosdwanCgwDetail.getCosModel());
				orderIzosdwanCgwDetailRepository.save(orderIzosdwanCgwDetail);
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndReferenceName(quoteIzosdwanCgwDetail.getId(),
								IzosdwanCommonConstants.IZOSDWAN_CGW);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					for (QuoteProductComponent quoteProductComponent : quoteProductComponents) {
						OrderProductComponent orderProductComponent = new OrderProductComponent();
						orderProductComponent.setReferenceId(orderIzosdwanCgwDetail.getId());
						if (quoteProductComponent.getMstProductComponent() != null) {
							orderProductComponent
									.setMstProductComponent(quoteProductComponent.getMstProductComponent());
						}
						orderProductComponent.setType(quoteProductComponent.getType());
						orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
						orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
						orderProductComponentRepository.save(orderProductComponent);
						constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
						List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_Id(quoteProductComponent.getId());
						constructOrderAttribute(attributes, orderProductComponent);
					}

				}
			}
		}

	}

	private OrderPrice constructOrderComponentPrice(QuoteProductComponent quoteProductComponent,
			OrderProductComponent orderProductComponent) {
		OrderPrice orderPrice = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			QuotePrice price = quotePriceRepository.findFirstByReferenceIdAndReferenceName(
					String.valueOf(quoteProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (price != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(price.getCatalogMrc());
				orderPrice.setCatalogNrc(price.getCatalogNrc());
				orderPrice.setCatalogArc(price.getCatalogArc());
				orderPrice.setReferenceName(price.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderProductComponent.getId()));
				orderPrice.setComputedMrc(price.getComputedMrc());
				orderPrice.setComputedNrc(price.getComputedNrc());
				orderPrice.setComputedArc(price.getComputedArc());
				orderPrice.setDiscountInPercent(price.getDiscountInPercent());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPrice.setVersion(VersionConstants.ONE.getVersionNumber());
				orderPrice.setMinimumMrc(price.getMinimumMrc());
				orderPrice.setMinimumNrc(price.getMinimumNrc());
				orderPrice.setMinimumArc(price.getMinimumArc());
				orderPrice.setEffectiveMrc(price.getEffectiveMrc());
				orderPrice.setEffectiveNrc(price.getEffectiveNrc());
				orderPrice.setEffectiveArc(price.getEffectiveArc());
				orderPrice.setEffectiveUsagePrice(price.getEffectiveUsagePrice());
				orderPrice.setMstProductFamily(price.getMstProductFamily());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPriceRepository.save(orderPrice);

			}
		}
		return orderPrice;

	}

	private Set<OrderProductComponentsAttributeValue> constructOrderAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues,
			OrderProductComponent orderProductComponent) {
		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();
		if (quoteProductComponentsAttributeValues != null) {
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValue orderAttributeValue = new OrderProductComponentsAttributeValue();
				orderAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
				orderAttributeValue.setDisplayValue(attributeValue.getDisplayValue());
				orderAttributeValue.setProductAttributeMaster(attributeValue.getProductAttributeMaster());
				orderAttributeValue.setOrderProductComponent(orderProductComponent);
				orderProductComponentsAttributeValueRepository.save(orderAttributeValue);
				constructOrderAttributePriceDto(attributeValue, orderAttributeValue);
				orderProductComponentsAttributeValues.add(orderAttributeValue);
			}
		}

		return orderProductComponentsAttributeValues;
	}

	private OrderPrice constructOrderAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue,
			OrderProductComponentsAttributeValue orderAttributeValue) {
		OrderPrice orderPrice = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findFirstByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			orderPrice = new OrderPrice();
			if (attrPrice != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(attrPrice.getCatalogMrc());
				orderPrice.setCatalogNrc(attrPrice.getCatalogNrc());
				orderPrice.setReferenceName(attrPrice.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderAttributeValue.getId()));
				orderPrice.setComputedMrc(attrPrice.getComputedMrc());
				orderPrice.setComputedNrc(attrPrice.getComputedNrc());
				orderPrice.setDiscountInPercent(attrPrice.getDiscountInPercent());
				orderPrice.setQuoteId(attrPrice.getQuoteId());
				orderPrice.setVersion(1);
				orderPrice.setEffectiveUsagePrice(attrPrice.getEffectiveUsagePrice());
				orderPrice.setMinimumMrc(attrPrice.getMinimumMrc());
				orderPrice.setMinimumNrc(attrPrice.getMinimumNrc());
				orderPrice.setEffectiveMrc(attrPrice.getEffectiveMrc());
				orderPrice.setEffectiveNrc(attrPrice.getEffectiveNrc());
				orderPrice.setEffectiveArc(attrPrice.getEffectiveArc());
				orderPriceRepository.save(orderPrice);
			}

		}
		return orderPrice;

	}

	private Boolean checkO2cEnabled(Order order) {
		Boolean status = false;
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
				for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
					for (OrderIzosdwanSite orderIllSite : oProductSolution.getOrderIzosdwanSites()) {
						List<OrderIzosdwanSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIzosdwanSiteIdAndIsSelectedAndType(orderIllSite.getId(),
										CommonConstants.BACTIVE, "primary");
						for (OrderIzosdwanSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
							String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
							if (!(feasibilityMode.equals("OnnetWL") || feasibilityMode.equals("Onnet Wireline")
									|| feasibilityMode.equals("OnnetRF") || feasibilityMode.equals("Onnet Wireless"))) {
								LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
										orderIllSite.getId());
								return false;
							} else {
								status = true;
							}
						}
						List<OrderIzosdwanSiteFeasibility> secOrderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIzosdwanSiteIdAndIsSelectedAndType(orderIllSite.getId(),
										CommonConstants.BACTIVE, "secondary");
						if (!secOrderSiteFeasibilities.isEmpty()) {
							LOGGER.info("The prisec have secondary for site {}", orderIllSite.getId());
							return false;
						} else {
							status = true;
						}
					}

				}

			}

		}
		return status;
	}

	private Boolean checkO2cEnabledForOffnetWireless(Order order) {
		Boolean status = false;
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
				for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
					for (OrderIzosdwanSite orderIllSite : oProductSolution.getOrderIzosdwanSites()) {
						List<OrderIzosdwanSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIzosdwanSiteIdAndIsSelectedAndType(orderIllSite.getId(),
										CommonConstants.BACTIVE, "primary");
						for (OrderIzosdwanSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
							String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
							if (!(feasibilityMode.equals("OffnetRF") || feasibilityMode.equals("Offnet Wireless"))) {
								LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
										orderIllSite.getId());
								return false;
							} else {
								status = true;
							}
						}
						List<OrderIzosdwanSiteFeasibility> secOrderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIzosdwanSiteIdAndIsSelectedAndType(orderIllSite.getId(),
										CommonConstants.BACTIVE, "secondary");
						if (!secOrderSiteFeasibilities.isEmpty()) {
							LOGGER.info("The prisec have secondary for site {}", orderIllSite.getId());
							return false;
						} else {
							status = true;
						}
					}

				}

			}

		}
		return status;
	}

	public void updateOrderConfirmationAudit(String publicIp, String orderRefUuid, String checkList)
			throws TclCommonException {

		try {
			String name = Utils.getSource();
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
					.findByOrderRefUuid(orderRefUuid);
			if (orderConfirmationAudit == null) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setPublicIp(publicIp);
			orderConfirmationAudit.setOrderRefUuid(orderRefUuid);
			orderConfirmationAudit.setMode(AuditMode.CLICK_THROUGH.toString());
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	public void uploadSSIfNotPresent(QuoteToLe quoteToLe) throws TclCommonException {
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.SERVICE_SCHEDULE, CommonConstants.BACTIVE);
		for (MstOmsAttribute mstOmsAttribute : mstOmsAttributes) {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
				ServiceScheduleBean serviceScheduleBean = constructServiceScheduleBean(
						quoteToLe.getErfCusCustomerLegalEntityId());
				LOGGER.info("MDC Filter token value in before Queue call uploadSSIfNotPresent {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
				break;
			}
		}
	}

	public ServiceScheduleBean constructServiceScheduleBean(Integer customerLeId) {
		ServiceScheduleBean serviceScheduleBean = new ServiceScheduleBean();
		serviceScheduleBean.setCustomerLeId(customerLeId);
		serviceScheduleBean.setDisplayName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setProductName(SFDCConstants.IZOSDWAN);
		return serviceScheduleBean;
	}

	protected void cloneQuoteForNonFeasibileSite(Quote quote) {
		Quote nonFeasibleQuote = cloneQuote(quote);
		String productName = null;
		Set<QuoteToLe> quoteToLes = quote.getQuoteToLes();
		for (QuoteToLe quoteToLe : quoteToLes) {
			QuoteToLe nonFeasibleQuoteToLe = cloneQuoteToLe(nonFeasibleQuote, quoteToLe);
			cloneQuoteLeAttributes(quoteToLe, nonFeasibleQuoteToLe);
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilies) {
				productName = quoteToLeProductFamily.getMstProductFamily().getName();
				QuoteToLeProductFamily nonFeasibleProdFamily = cloneQuoteLeToProductFamily(nonFeasibleQuoteToLe,
						quoteToLeProductFamily);
				extractProductSolutions(quoteToLeProductFamily, nonFeasibleProdFamily);
			}
		}
		nonFeasibleQuote.setQuoteCode(Utils.generateRefId(productName));
		quoteRepository.save(nonFeasibleQuote);
	}

	private Quote cloneQuote(Quote quote) {
		Quote nonFeasibleQuote = new Quote();
		nonFeasibleQuote.setCreatedBy(quote.getCreatedBy());
		nonFeasibleQuote.setCreatedTime(new Date());
		nonFeasibleQuote.setCustomer(quote.getCustomer());
		nonFeasibleQuote.setEffectiveDate(quote.getEffectiveDate());
		nonFeasibleQuote.setStatus((byte) 1);
		quoteRepository.save(nonFeasibleQuote);
		return nonFeasibleQuote;
	}

	private QuoteToLe cloneQuoteToLe(Quote nonFeasibleQuote, QuoteToLe quoteToLe) {
		QuoteToLe nonFeasibleQuoteToLe = new QuoteToLe();
		nonFeasibleQuoteToLe.setCurrencyId(quoteToLe.getCurrencyId());
		nonFeasibleQuoteToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
		nonFeasibleQuoteToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
		nonFeasibleQuoteToLe.setStage(QuoteStageConstants.GET_QUOTE.toString());
		nonFeasibleQuoteToLe.setQuote(nonFeasibleQuote);
		quoteToLeRepository.save(nonFeasibleQuoteToLe);
		return nonFeasibleQuoteToLe;
	}

	private void cloneQuoteLeAttributes(QuoteToLe quoteToLe, QuoteToLe nonFeasibleQuoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteLeAttributeValues) {
			QuoteLeAttributeValue nonFeasibleQuoteLeAttributeValue = new QuoteLeAttributeValue();
			nonFeasibleQuoteLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
			nonFeasibleQuoteLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
			nonFeasibleQuoteLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
			nonFeasibleQuoteLeAttributeValue.setQuoteToLe(nonFeasibleQuoteToLe);
			quoteLeAttributeValueRepository.save(nonFeasibleQuoteLeAttributeValue);
		}
	}

	private QuoteToLeProductFamily cloneQuoteLeToProductFamily(QuoteToLe nonFeasibleQuoteToLe,
			QuoteToLeProductFamily quoteToLeProductFamily) {
		QuoteToLeProductFamily nonFeasibleProdFamily = new QuoteToLeProductFamily();
		nonFeasibleProdFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
		nonFeasibleProdFamily.setQuoteToLe(nonFeasibleQuoteToLe);
		quoteToLeProductFamilyRepository.save(nonFeasibleProdFamily);
		return nonFeasibleProdFamily;
	}

	private void extractProductSolutions(QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLeProductFamily nonFeasibleProdFamily) {
		List<ProductSolution> prodSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		for (ProductSolution productSolution : prodSolutions) {
			ProductSolution nonFeasibleProductSolution = cloneProductSolution(nonFeasibleProdFamily, productSolution);
			List<QuoteIzosdwanSite> sites = quoteIzosdwanSiteRepository
					.findByProductSolutionIdAndStatus(productSolution.getId(), CommonConstants.BACTIVE);
			for (QuoteIzosdwanSite quoteIllSite : sites) {
				if (quoteIllSite.getFeasibility().equals(new Byte("0"))) {
					QuoteIzosdwanSite nonQuoteIllSite = cloneIllSite(nonFeasibleProductSolution, quoteIllSite);
					extractNonFeasibleComponents(quoteIllSite, nonQuoteIllSite);
					cloneSlaDetails(quoteIllSite, nonQuoteIllSite);
					cloneFeasilibility(quoteIllSite, nonQuoteIllSite);
					clonePricingDetails(quoteIllSite, nonQuoteIllSite);
				}
			}
		}
	}

	private ProductSolution cloneProductSolution(QuoteToLeProductFamily nonFeasibleProdFamily,
			ProductSolution productSolution) {
		ProductSolution nonFeasibleProductSolution = new ProductSolution();
		nonFeasibleProductSolution.setMstProductOffering(productSolution.getMstProductOffering());
		nonFeasibleProductSolution.setProductProfileData(productSolution.getProductProfileData());
		nonFeasibleProductSolution.setQuoteToLeProductFamily(nonFeasibleProdFamily);
		nonFeasibleProductSolution.setSolutionCode(Utils.generateUid());
		productSolutionRepository.save(nonFeasibleProductSolution);
		return nonFeasibleProductSolution;
	}

	private QuoteIzosdwanSite cloneIllSite(ProductSolution nonFeasibleProductSolution, QuoteIzosdwanSite quoteIllSite) {
		QuoteIzosdwanSite nonQuoteIllSite = new QuoteIzosdwanSite();
		nonQuoteIllSite.setCreatedBy(quoteIllSite.getCreatedBy());
		nonQuoteIllSite.setCreatedTime(new Date());
		nonQuoteIllSite.setErfLocSiteaLocationId(quoteIllSite.getErfLocSiteaLocationId());
		nonQuoteIllSite.setErfLocSiteaSiteCode(quoteIllSite.getErfLocSiteaSiteCode());
		nonQuoteIllSite.setErfLocSitebLocationId(quoteIllSite.getErfLocSitebLocationId());
		nonQuoteIllSite.setErfLocSitebSiteCode(quoteIllSite.getErfLocSitebSiteCode());
		nonQuoteIllSite.setErfLrSolutionId(quoteIllSite.getErfLrSolutionId());
		nonQuoteIllSite.setFeasibility(quoteIllSite.getFeasibility());
		nonQuoteIllSite.setFpStatus(quoteIllSite.getFpStatus());
		nonQuoteIllSite.setImageUrl(quoteIllSite.getImageUrl());
		nonQuoteIllSite.setProductSolution(nonFeasibleProductSolution);
		nonQuoteIllSite.setStatus(quoteIllSite.getStatus());
		nonQuoteIllSite.setSiteCode(Utils.generateUid());
		quoteIzosdwanSiteRepository.save(nonQuoteIllSite);
		return nonQuoteIllSite;
	}

	private void extractNonFeasibleComponents(QuoteIzosdwanSite quoteIllSite, QuoteIzosdwanSite nonQuoteIllSite) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quoteIllSite.getId(), QuoteConstants.IZOSDWAN_SITES.toString());
		for (QuoteProductComponent quoteProductComponent : productComponents) {
			QuoteProductComponent nonFeasibleProductComponent = cloneProductComponent(nonQuoteIllSite,
					quoteProductComponent);
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());
			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				cloneComponentAttributes(nonFeasibleProductComponent, quoteProductComponentsAttributeValue);
			}
		}
	}

	private QuoteProductComponent cloneProductComponent(QuoteIzosdwanSite nonQuoteIllSite,
			QuoteProductComponent quoteProductComponent) {
		QuoteProductComponent nonFeasibleProductComponent = new QuoteProductComponent();
		nonFeasibleProductComponent.setReferenceId(nonQuoteIllSite.getId());
		if (quoteProductComponent.getMstProductComponent() != null) {
			nonFeasibleProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
		}
		nonFeasibleProductComponent.setType(quoteProductComponent.getType());
		nonFeasibleProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
		nonFeasibleProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
		quoteProductComponentRepository.save(nonFeasibleProductComponent);
		return nonFeasibleProductComponent;
	}

	private void cloneComponentAttributes(QuoteProductComponent nonFeasibleProductComponent,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		QuoteProductComponentsAttributeValue nonFeasiblequoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		nonFeasiblequoteProductComponentsAttributeValue
				.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
		nonFeasiblequoteProductComponentsAttributeValue
				.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
		nonFeasiblequoteProductComponentsAttributeValue
				.setProductAttributeMaster(quoteProductComponentsAttributeValue.getProductAttributeMaster());
		nonFeasiblequoteProductComponentsAttributeValue.setQuoteProductComponent(nonFeasibleProductComponent);
		quoteProductComponentsAttributeValueRepository.save(nonFeasiblequoteProductComponentsAttributeValue);
	}

	private void cloneSlaDetails(QuoteIzosdwanSite quoteIllSite, QuoteIzosdwanSite nonQuoteIllSite) {
		List<QuoteIzosdwanSiteSla> slaDetails = quoteIzoSdwanSlaRepository.findByQuoteIzosdwanSite(quoteIllSite);
		for (QuoteIzosdwanSiteSla quoteIllSiteSla : slaDetails) {
			QuoteIzosdwanSiteSla nonFeasibileQuoteIllSiteSla = new QuoteIzosdwanSiteSla();
			nonFeasibileQuoteIllSiteSla.setQuoteIzosdwanSite(nonQuoteIllSite);
			nonFeasibileQuoteIllSiteSla.setSlaEndDate(quoteIllSiteSla.getSlaEndDate());
			nonFeasibileQuoteIllSiteSla.setSlaMaster(quoteIllSiteSla.getSlaMaster());
			nonFeasibileQuoteIllSiteSla.setSlaStartDate(quoteIllSiteSla.getSlaStartDate());
			nonFeasibileQuoteIllSiteSla.setSlaValue(quoteIllSiteSla.getSlaValue());
			quoteIzoSdwanSlaRepository.save(nonFeasibileQuoteIllSiteSla);
		}
	}

	private void cloneFeasilibility(QuoteIzosdwanSite quoteIllSite, QuoteIzosdwanSite nonQuoteIllSite) {
		List<IzosdwanSiteFeasibility> siteFeasiblities = siteFeasibilityRepository
				.findByQuoteIzosdwanSite(quoteIllSite);
		for (IzosdwanSiteFeasibility siteFeasibility : siteFeasiblities) {
			IzosdwanSiteFeasibility nonFeasibleSiteFeasibility = new IzosdwanSiteFeasibility();
			LocalDateTime localDateTime = LocalDateTime.now();
			nonFeasibleSiteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
			nonFeasibleSiteFeasibility.setFeasibilityCheck(siteFeasibility.getFeasibilityCheck());
			nonFeasibleSiteFeasibility.setFeasibilityCode(siteFeasibility.getFeasibilityCode());
			nonFeasibleSiteFeasibility.setFeasibilityMode(siteFeasibility.getFeasibilityMode());
			nonFeasibleSiteFeasibility.setIsSelected(siteFeasibility.getIsSelected());
			nonFeasibleSiteFeasibility.setProvider(siteFeasibility.getProvider());
			nonFeasibleSiteFeasibility.setQuoteIzosdwanSite(nonQuoteIllSite);
			nonFeasibleSiteFeasibility.setRank(siteFeasibility.getRank());
			nonFeasibleSiteFeasibility.setResponseJson(siteFeasibility.getResponseJson());
			nonFeasibleSiteFeasibility.setType(siteFeasibility.getType());
			siteFeasibilityRepository.save(nonFeasibleSiteFeasibility);
		}
	}

	private void clonePricingDetails(QuoteIzosdwanSite quoteIllSite, QuoteIzosdwanSite nonQuoteIllSite) {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(quoteIllSite.getSiteCode(), "Discount");
		for (PricingEngineResponse pricingDetail : pricingDetails) {
			PricingEngineResponse nonFeasiblepricingDetail = new PricingEngineResponse();
			nonFeasiblepricingDetail.setDateTime(pricingDetail.getDateTime());
			nonFeasiblepricingDetail.setPriceMode(pricingDetail.getPriceMode());
			nonFeasiblepricingDetail.setPricingType(pricingDetail.getPricingType());
			nonFeasiblepricingDetail.setRequestData(pricingDetail.getRequestData());
			nonFeasiblepricingDetail.setResponseData(pricingDetail.getResponseData());
			nonFeasiblepricingDetail.setSiteCode(nonQuoteIllSite.getSiteCode());
			pricingDetailsRepository.save(nonFeasiblepricingDetail);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedManualQuotes(Integer quoteId, String ipAddress) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();

			Map<String, String> cofObjectMapper = new HashMap<>();
			CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quote.getQuoteCode(),
					Source.MANUAL_COF.getSourceType());
			if (cofDetail != null) {
				cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetail.getUriPath());
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				order = constructOrder(quote, detail);
				/*
				 * if (checkO2cEnabled(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				 * orderRepository.save(order); } if (checkO2cEnabledForOffnetWireless(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * orderRepository.save(order); }
				 */
				
				skipO2cCustomJouneyOrders(quote, order);
				
				detail.setOrderId(order.getId());
				updateManualOrderConfirmationAudit(ipAddress, order.getOrderCode());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					// omsSfdcService.processSiteDetails(quoteLe);
				
					if (quoteLe.getQuote().getNsQuote() != null
							&& quoteLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {

						createSFDCSiteForCustomeJourney(quoteLe);

					}else {
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe,
								IzosdwanCommonConstants.IZOSDWAN_NAME);
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, IzosdwanCommonConstants.CGW);
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, "IAS");
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, "GVPN");
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe,
								IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
					}
					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
					List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
					Integer userId = order.getCreatedBy();
					String userEmail = null;
					if (userId != null) {
						Optional<User> userDetails = userRepository.findById(userId);
						if (userDetails.isPresent()) {
							userEmail = userDetails.get().getEmailId();
						}
					}

					for (OrderToLe orderToLe : order.getOrderToLes()) {
						LOGGER.info("Order to le is ----> {} ", Optional.ofNullable(orderToLe));
						izosdwanQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
								orderToLe.getId(), cofObjectMapper);
						break;
					}

					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
				}
			}

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equalsIgnoreCase(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}
			}
			// triggerClosedBCROnPlacingOrder(quote);
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedDocusignQuotes(String quoteuuId) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			Quote quote = quoteRepository.findByQuoteCode(quoteuuId);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();

			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);
			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				order = constructOrder(quote, detail);
				/*
				 * if (checkO2cEnabled(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				 * orderRepository.save(order); } if (checkO2cEnabledForOffnetWireless(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * orderRepository.save(order); }
				 */
				skipO2cCustomJouneyOrders(quote, order);

				detail.setOrderId(order.getId());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					// omsSfdcService.processSiteDetails(quoteLe);
					Date cofSignedDate = new Date();
					DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
					if (docusignAudit != null && docusignAudit.getCustomerSignedDate() != null
							&& (docusignAudit.getStatus().equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
									|| docusignAudit.getStatus()
											.equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
						cofSignedDate = docusignAudit.getCustomerSignedDate();
					}
					LOGGER.info("Cof signed date for quote code ---> {} before cof won recieved stage is ----> {} ",
							quote.getQuoteCode(), cofSignedDate);
					
					if (quoteLe.getQuote().getNsQuote() != null
							&& quoteLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {

						createSFDCSiteForCustomeJourney(quoteLe);

					} else {

						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe,
								IzosdwanCommonConstants.IZOSDWAN_NAME);
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, IzosdwanCommonConstants.CGW);
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, "IAS");
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe, "GVPN");
						bundleOmsSfdcService.processCreateSiteLocationInSFDC(quoteLe,
								IzosdwanCommonConstants.BYON_INTERNET_PRODUCT);
						
					}
					omsSfdcService.processUpdateOpportunity(cofSignedDate, quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
					LOGGER.info("Cof signed date for quote code ---> {} after cof won recieved stage is ----> {} ",
							quote.getQuoteCode(), cofSignedDate);
					List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
					Map<String, String> cofObjectMapper = new HashMap<>();
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
					if (cofDetails != null) {
						cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
					}
					Integer userId = order.getCreatedBy();
					String userEmail = null;
					if (userId != null) {
						Optional<User> userDetails = userRepository.findById(userId);
						if (userDetails.isPresent()) {
							userEmail = userDetails.get().getEmailId();
						}
					}
					for (OrderToLe orderToLe : order.getOrderToLes()) {
						List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
								.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES,
										quote.getId(), AttachmentTypeConstants.COF.toString());
						for (OmsAttachment omsAttachment : omsAttachmentList) {
							omsAttachment.setOrderToLe(orderToLe);
							omsAttachment.setReferenceName(CommonConstants.ORDERS);
							omsAttachment.setReferenceId(order.getId());
							omsAttachmentRepository.save(omsAttachment);
						}
						izosdwanQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
								orderToLe.getId(), cofObjectMapper);
						break;
					}
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
				}
			}

			LOGGER.info("set to update the quote stage from order form to enrichment for refId {}", quoteuuId);
			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				LOGGER.info(
						"Before updating the quote stage from order form to enrichment for refId {} with existing stage {}",
						quoteuuId, quoteLe.getStage());
				if (quoteLe.getStage().equalsIgnoreCase(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
					LOGGER.info(
							"After updating the quote stage from order form to enrichment for refId {} stage is ---> {}",
							quoteuuId, quoteLe.getStage());
				}
			}
			// triggerClosedBCROnPlacingOrder(quote);
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}
			if (o2cEnableFlag.equalsIgnoreCase("true")) {
				LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
				// processOrderFlatTable(order.getId());
			} else {
				LOGGER.info("Order flat table is disabled");
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	private void createSFDCSiteForCustomeJourney(QuoteToLe quoteLe) throws TclCommonException {
		bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe,
				IzosdwanCommonConstants.IZOSDWAN_NAME, true);
		bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, IzosdwanCommonConstants.CGW,
				true);
		bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "IAS", true);
		bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "IAS", false);
		bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "GVPN", true);
		bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "GVPN", false);
		bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe,
				IzosdwanCommonConstants.BYON_INTERNET_PRODUCT, true);
		
			bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe,
					IzosdwanCommonConstants.BYON_MPLS_PRODUCT, true);
			bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "vUTM", true);
			bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "vPROXY", true);
			bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "IP Transit", true);
			bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "IP Transit", false);
			bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "IZO Internet WAN",
					true);
			bundleOmsSfdcService.processCreateSiteLocationInSdwanSDFC(quoteLe, "IZO Internet WAN",
					false);
	}

	private void skipO2cCustomJouneyOrders(Quote quote, Order order) {
		if (quote.getNsQuote() != null && quote.getNsQuote().equalsIgnoreCase("Y")) {
			if (skipCustomJourneyOrders) {
				order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
				order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
				orderRepository.save(order);
			} else if (skipInternationalJourneyOrders) {
				skipO2cInternationalOrders(quote, order);

			}
		}
	}
		
	private void skipO2cInternationalOrders(Quote quote, Order order) {

		Integer indianUnderlays = 0;
		Integer usUnderlays = 0;
		List<QuoteIzosdwanSite> quoteIzosdwanSiteList = quoteIzosdwanSiteRepository.getSiteCount(quote.getId());
		if (null != quoteIzosdwanSiteList) {

			for (QuoteIzosdwanSite quoteIzosdwanSite : quoteIzosdwanSiteList) {

				if (quoteIzosdwanSite.getServiceSiteCountry().contains("India")) {
					indianUnderlays++;
				} else {
					usUnderlays++;
				}

			}
		}
		if (usUnderlays > 0) {
			order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
			order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
			orderRepository.save(order);
		}

	}

	protected void processOrderMailNotification(Order order, QuoteToLe quoteToLe, Map<String, String> cofObjectMapper,
			String userEmail) throws TclCommonException {
		String emailId = userEmail != null ? userEmail : customerSupportEmail;
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				emailId, appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName,
				IzosdwanCommonConstants.IZOSDWAN_NAME, quoteToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}

	public void updateManualOrderConfirmationAudit(String publicIp, String orderRefUuid) throws TclCommonException {

		try {
			String name = Utils.getSource();
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
					.findByOrderRefUuid(orderRefUuid);
			if (orderConfirmationAudit == null) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setMode(AuditMode.MANUAL.toString());
			orderConfirmationAudit.setUploadedBy(name);
			orderConfirmationAudit.setPublicIp(publicIp);
			orderConfirmationAudit.setOrderRefUuid(orderRefUuid);
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId,
			String customerEmail, String provisioningLink, Map<String, String> cofObjectMapper, String fileName,
			String productName, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(provisioningLink);
		mailNotificationBean.setCofObjectMapper(cofObjectMapper);
		mailNotificationBean.setFileName(fileName);
		mailNotificationBean.setProductName(productName);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	private MailNotificationBean populatePartnerClassification(QuoteToLe quoteToLe,
			MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny()
					.get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(quoteToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name ::  " + ExceptionUtils.getStackFrames(e));
		}
		return mailNotificationBean;
	}

	public void deleteAllQuote() throws TclCommonException {
		try {
			List<Quote> quotes = quoteRepository.findAll().stream()
					.filter(quote -> quote.getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN))
					.collect(Collectors.toList());
			if (quotes != null && !quotes.isEmpty()) {
				for (Quote quote : quotes) {
					Optional<Order> order = orderRepository.findByQuote(quote);
					if (order.isPresent()) {
						continue;
						// deleteOrderRelatedDetails(order.get());
					}
					quote.getQuoteToLes().stream().forEach(quoteToLe -> {
						try {
							quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteToLeProdFamily -> {

								quoteToLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {
									prodSolution.getQuoteIzoSdwanSites().stream().forEach(izoSdwanSite -> {
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
												.findByReferenceIdAndReferenceName(izoSdwanSite.getId(),
														QuoteConstants.IZOSDWAN_SITES.toString());
										quoteProductComponentList.stream().forEach(quoteProdComponent -> {
											deleteQuoteProductComponent(quoteProdComponent);
										});
										deleteFeasibilityDetails(izoSdwanSite);
										quoteIzosdwanSiteRepository.delete(izoSdwanSite);
									});
									productSolutionRepository.delete(prodSolution);
								});
								quoteToLeProductFamilyRepository.delete(quoteToLeProdFamily);
							});
							deleteQuoteLeAttributeValues(quoteToLe);
							deleteQuoteSdwanAttributeValues(quoteToLe);
							quoteToLeRepository.delete(quoteToLe);

//                             SFDC Update Opportunity - CLOSED DROPPED
//    						omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
//    								SFDCConstants.CLOSED_DROPPED, quoteToLe);
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
					});
					deleteQuotePrice(quote);
					deleteQuoteIzoSdwanCGWDetails(quote);
					deleteQuoteIzosdwanByonUploadDetails(quote);
					quoteRepository.delete(quote);
				}
			}

		} catch (Exception e) {
			if (e instanceof TclCommonException)
				throw e;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	public OpportunityBean retrievePriSecSIDsForMFOppurtunity(OpportunityBean opBean, Integer quoteId, Integer siteId)
			throws TclCommonException {

		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(siteId);
		QuoteToLe quoteToLe = null;
		if (quoteIllSite.isPresent()) {
			quoteToLe = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		}

		if (Objects.nonNull(quoteToLe) && Objects.nonNull(opBean.getServiceId())) {
			String secondaryServiceId = null;
			Boolean isSecondary = false;
			SIServiceDetailDataBean sIServiceDetailDataBean = omsIzosdwanUtils
					.getServiceDetailIzosdwan(opBean.getServiceId());
			isSecondary = Objects.nonNull(sIServiceDetailDataBean.getPriSecServLink());
			String linkType = sIServiceDetailDataBean.getLinkType();

			if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE")) {
				if (isSecondary) {
					secondaryServiceId = sIServiceDetailDataBean.getPriSecServLink();
					LOGGER.info("Primary service Id is ----> {} and Secondary service ID is -----> {} ",
							opBean.getServiceId(), secondaryServiceId);
					opBean.setPrimaryServiceId(sIServiceDetailDataBean.getTpsServiceId());
					opBean.setSecondaryServiceId(secondaryServiceId);
				}

			}

			else if (isSecondary && linkType.equalsIgnoreCase("SECONDARY")) {
				secondaryServiceId = sIServiceDetailDataBean.getTpsServiceId();
				LOGGER.info("Secondary service ID is -----> {}  and primary service Id is ----> {} ",
						secondaryServiceId, opBean.getServiceId());
				opBean.setSecondaryServiceId(secondaryServiceId);
				opBean.setPrimaryServiceId(sIServiceDetailDataBean.getPriSecServLink());

			}
		}
		return opBean;
	}

	public OpportunityBean getOpportunityDetails(Integer quoteId, Integer siteId) throws TclCommonException {
		LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch opportunity details for the quoteId {} ",
				quoteId);
		OpportunityBean opporBean = new OpportunityBean();
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSiteRepository.findByIdAndStatus(siteId,
					CommonConstants.BACTIVE);
			QuoteToLe quoteToLe = optionalQuoteToLe.get();
			opporBean.setProductName(quoteIzosdwanSite.getIzosdwanSiteProduct());
			/*
			 * if( Objects.nonNull(quoteToLe.getQuoteType()) &&
			 * MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			 * if( Objects.nonNull(quoteToLe.getIsAmended()) &&
			 * quoteToLe.getIsAmended()!=1){ List<String>
			 * serviceIds=macdutils.getServiceIds(quoteToLe); String
			 * serviceIdList=serviceIds.stream().findFirst().get();
			 * if(Objects.nonNull(quoteToLe.getIsMultiCircuit())&&CommonConstants.BACTIVE.
			 * equals(quoteToLe.getIsMultiCircuit())) { serviceIds.remove(serviceIdList);
			 * serviceIds.forEach(serviceId -> { serviceIdList.concat("," + serviceId); });
			 * } opporBean.setServiceId(serviceIdList); } }
			 */
			/* opporBean.setServiceId(quoteToLe.getErfServiceInventoryTpsServiceId()); */
			opporBean.setServiceId(quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
			opporBean.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			opporBean.setOpportunityAccountName(quote.getCustomer().getCustomerName());
			Optional<User> user = userRepository.findById(quote.getCreatedBy());
			opporBean.setOpportunityOwnerEmail(user.get().getEmailId());
			opporBean.setOpportunityOwnerName(user.get().getUsername());
			List<IzosdwanSiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository
					.findByQuoteIzosdwanSite_IdAndType(siteId, quoteIzosdwanSite.getPriSec());
			if (selectedSiteFeasibility != null && !selectedSiteFeasibility.isEmpty()) {
				Optional<IzosdwanSiteFeasibility> siteFeasibility = selectedSiteFeasibility.stream().findFirst();
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject) jsonParser.parse(siteFeasibility.get().getResponseJson());
				opporBean.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.CUSTOMER_SEGMENT));
			}
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(siteId, IzosdwanCommonConstants.IZOSDWAN_SITES).stream()
					.filter(comp -> comp.getMstProductComponent().getName()
							.equalsIgnoreCase(IzosdwanCommonConstants.SITE_PROPERTIES))
					.findFirst().orElse(null);

			if (quoteProductComponent != null) {
				LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch site properties for the siteId {} ",
						siteId);
				Map<String, String> map = quoteProductComponent.getQuoteProductComponentsAttributeValues().stream()
						.filter(k -> k.getAttributeValues() != null)
						.collect(Collectors.toMap(k -> k.getProductAttributeMaster().getName(),
								QuoteProductComponentsAttributeValue::getAttributeValues));

				opporBean.setSiteContactName(map.getOrDefault("LCON_NAME", null));
				opporBean.setSiteLocalContactNumber(map.getOrDefault("LCON_CONTACT_NUMBER", null));
				if (map.containsKey("LCON_REMARKS"))
					opporBean.setSalesRemarks(map.getOrDefault("LCON_REMARKS", null));
			}
			String response = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
							quote.getQuoteCode(), SfdcServiceTypeConstants.UPDATE_OPPORTUNITY,
							ThirdPartySource.SFDC.toString(), "SUCCESS")
					.stream().findFirst().map(ThirdPartyServiceJob::getResponsePayload).orElse(StringUtils.EMPTY);

			if (response != null && !response.isEmpty()) {
				LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch opportunity stage");
				ThirdPartyResponseBean thirdPartyResponse = (ThirdPartyResponseBean) Utils.convertJsonToObject(response,
						ThirdPartyResponseBean.class);
				opporBean.setOpportunityStage(thirdPartyResponse.getOpportunity().getStageName());
			}

			opporBean.setOppotunityId(quoteToLe.getTpsSfdcOptyId());

			List<QuoteLeAttributeValue> quoteLeAttributeValueLegalEntity = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.LE_NAME.toString());
			if (quoteLeAttributeValueLegalEntity != null && !quoteLeAttributeValueLegalEntity.isEmpty())
				opporBean.setOpportunityOwnerName(quoteLeAttributeValueLegalEntity.get(0).getAttributeValue());

			List<QuoteLeAttributeValue> quoteLeAttributeValueLeOwnerEmail = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.LE_EMAIL.toString());
			if (quoteLeAttributeValueLeOwnerEmail != null && !quoteLeAttributeValueLeOwnerEmail.isEmpty())
				opporBean.setOpportunityOwnerEmail(quoteLeAttributeValueLeOwnerEmail.get(0).getAttributeValue());
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}

		return opporBean;
	}

	public void updateLconProperities(Integer quoteId, Integer quoteToLeId, List<LconUpdateBean> lconUpdateBeans) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.get().getId());
			if (quoteToLeProductFamilies != null && !quoteToLeProductFamilies.isEmpty()) {
				quoteToLeProductFamilies.forEach(productFamily -> {
					if (productFamily.getMstProductFamily().getName()
							.equalsIgnoreCase(IzosdwanCommonConstants.IZOSDWAN_NAME)) {
						MstProductComponent mstProductComponents = mstProductComponentRepository
								.findByName("SITE_PROPERTIES");
						List<ProductAttributeMaster> lconNameProductAttributeMasters = productAttributeMasterRepository
								.findByNameAndStatus("LCON_NAME", CommonConstants.BACTIVE);
						List<ProductAttributeMaster> lconContactProductAttributeMasters = productAttributeMasterRepository
								.findByNameAndStatus("LCON_CONTACT_NUMBER", CommonConstants.BACTIVE);
						List<ProductAttributeMaster> lconRemarksProductAttributeMasters = productAttributeMasterRepository
								.findByNameAndStatus("LCON_REMARKS", CommonConstants.BACTIVE);
						if (mstProductComponents != null && lconContactProductAttributeMasters != null
								&& !lconContactProductAttributeMasters.isEmpty()
								&& lconNameProductAttributeMasters != null && !lconNameProductAttributeMasters.isEmpty()
								&& lconRemarksProductAttributeMasters != null
								&& !lconRemarksProductAttributeMasters.isEmpty()) {
							if (lconUpdateBeans != null && !lconUpdateBeans.isEmpty()) {
								lconUpdateBeans.stream().forEach(lconUpdateBean -> {

									List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponentAndMstProductFamily(
													lconUpdateBean.getSiteId(), mstProductComponents,
													productFamily.getMstProductFamily());
									if (quoteProductComponents != null && quoteProductComponents.isEmpty()) {
										QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
										quoteProductComponent.setMstProductComponent(mstProductComponents);
										quoteProductComponent
												.setReferenceName(QuoteConstants.IZOSDWAN_SITES.toString());
										quoteProductComponent.setReferenceId(lconUpdateBean.getSiteId());
										quoteProductComponent.setMstProductFamily(productFamily.getMstProductFamily());
										quoteProductComponent.setType("primary");
										quoteProductComponentRepository.save(quoteProductComponent);
									}
									quoteProductComponents = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponentAndMstProductFamily(
													lconUpdateBean.getSiteId(), mstProductComponents,
													productFamily.getMstProductFamily());
									if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
										// LCON - Name
										List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponentAndProductAttributeMaster(
														quoteProductComponents.get(0),
														lconNameProductAttributeMasters.get(0));
										if (quoteProductComponentsAttributeValues != null
												&& quoteProductComponentsAttributeValues.isEmpty()) {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconName());
											quoteProductComponentsAttributeValue
													.setProductAttributeMaster(lconNameProductAttributeMasters.get(0));
											quoteProductComponentsAttributeValue
													.setQuoteProductComponent(quoteProductComponents.get(0));
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										} else {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
													.get(0);
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconName());
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										}
										// LCON - Number
										List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValuesContact = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponentAndProductAttributeMaster(
														quoteProductComponents.get(0),
														lconContactProductAttributeMasters.get(0));
										if (quoteProductComponentsAttributeValuesContact != null
												&& quoteProductComponentsAttributeValuesContact.isEmpty()) {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconNumber());
											quoteProductComponentsAttributeValue.setProductAttributeMaster(
													lconContactProductAttributeMasters.get(0));
											quoteProductComponentsAttributeValue
													.setQuoteProductComponent(quoteProductComponents.get(0));
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										} else {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValuesContact
													.get(0);
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconNumber());
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										}
										// LCON Remarks
										List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValuesRemarks = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponentAndProductAttributeMaster(
														quoteProductComponents.get(0),
														lconRemarksProductAttributeMasters.get(0));
										if (quoteProductComponentsAttributeValuesRemarks != null
												&& quoteProductComponentsAttributeValuesRemarks.isEmpty()) {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconRemarks());
											quoteProductComponentsAttributeValue.setProductAttributeMaster(
													lconRemarksProductAttributeMasters.get(0));
											quoteProductComponentsAttributeValue
													.setQuoteProductComponent(quoteProductComponents.get(0));
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										} else {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValuesRemarks
													.get(0);
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconRemarks());
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										}
									}

									QuoteIzosdwanSite illSite = quoteIzosdwanSiteRepository
											.findByIdAndMfStatus(lconUpdateBean.getSiteId(), "Return");
									if (illSite != null) {
										illSite.setMfStatus(null);
										quoteIzosdwanSiteRepository.save(illSite);
									}

									/*
									 * try { omsSfdcService.updateFeasibility(quoteToLe.get(),
									 * lconUpdateBean.getSiteId()); } catch (TclCommonException e) {
									 * LOGGER.error("Sfdc update feasibility request Failed ", e); }
									 */

								});
							}
						}
					}
				});
			}

		}

	}

	public QuoteDetail updateSitePropertiesAttributes(UpdateRequest request) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			validateUpdateRequest(request);
			detail = new QuoteDetail();
			QuoteIzosdwanSite quoteIllSite = quoteIzosdwanSiteRepository.findByIdAndStatus(request.getSiteId(),
					(byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
					(byte) 1);

			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			User user = getUserId(Utils.getSource());

			MstProductComponent mstProductComponent = getMstProperties(user);
			constructIllSitePropeties(mstProductComponent, quoteIllSite, user.getUsername(), request, mstProductFamily);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	public List<SiteDocumentBean> getSiteDocumentDetails(Integer quoteId) throws TclCommonException {
		List<SiteDocumentBean> siteDocNames = new ArrayList<>();
		try {
			LOGGER.info(
					"Inside IzosdwanQuoteService.getSiteDocumentDetails method to get site document names for quoteId {} ",
					quoteId);
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
			if (quoteToLes.stream().findFirst().isPresent()) {
				QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get();
				List<QuoteToLeProductFamily> quoteLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe(quoteToLe.getId());
				if (!(quoteLeProductFamily.isEmpty())) {
					quoteLeProductFamily.stream().forEach(quotLeProdFamName -> {
						List<ProductSolution> productSolutions = productSolutionRepository
								.findByQuoteToLeProductFamily(quotLeProdFamName);
						productSolutions.stream().forEach(prodSolution -> {
							List<QuoteIzosdwanSite> quoteIllSites = quoteIzosdwanSiteRepository
									.findByProductSolutionAndStatus(prodSolution, (byte) 1);
							quoteIllSites.stream().forEach(ilSite -> {
								List<IzosdwanSiteFeasibility> sitefeas = siteFeasibilityRepository
										.findByQuoteIzosdwanSite_IdAndType(ilSite.getId(), ilSite.getPriSec());
								sitefeas.stream().forEach(sitef -> {
									if (sitef.getSiteDocumentName() != null) {
										SiteDocumentBean siteDocumentBean = new SiteDocumentBean();
										siteDocumentBean.setSiteId(sitef.getQuoteIzosdwanSite().getId());
										siteDocumentBean.setSiteDocumentName(sitef.getSiteDocumentName());
										siteDocNames.add(siteDocumentBean);
									}
								});

							});
						});
					});
				}
				LOGGER.info("Fetched site document names for the quoteToLe {} ", quoteToLe);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return siteDocNames;

	}

	public String removeDecimalBandwidth(String bw) {
		String bandwidth = "";
		if (bw.contains(".")) {
			bandwidth = bw.replaceAll("\\.0*$", "");
		} else
			bandwidth = bw;
		return bandwidth;
	}

	public String uploadDocument(Integer siteId, MultipartFile siteDocument) throws TclCommonException {
		String uploadStatus = CommonConstants.FAILIURE;
		try {
			LOGGER.info("Inside IzosdwanQuoteService.uploadDocument method to upload site document for siteId {} ",
					siteId);
			List<IzosdwanSiteFeasibility> siteFeasibilityResponse = siteFeasibilityRepository
					.findByQuoteIzosdwanSite_Id(siteId);
			if (siteFeasibilityResponse != null && !siteFeasibilityResponse.isEmpty()) {
				LOGGER.info("Izosdwan site feasibility data exists for siteId {}", siteId);
				IzosdwanSiteFeasibility sitef = siteFeasibilityResponse.get(0);
				sitef.setSiteDocument(siteDocument.getBytes());
				sitef.setSiteDocumentName(siteDocument.getOriginalFilename());
				siteFeasibilityRepository.save(sitef);
				uploadStatus = CommonConstants.SUCCESS;
				LOGGER.info("Uploaded site document for siteId {} ", siteId);
			} else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return uploadStatus;

	}

	public TriggerEmailResponse processTriggerMail(TriggerEmailRequest triggerEmailRequest, String initiatorIp)
			throws TclCommonException {
		TriggerEmailResponse triggerEmailResponse = new TriggerEmailResponse(Status.SUCCESS.toString());
		try {
			String userId = triggerEmailRequest.getEmailId();
			int quoteToLeId = triggerEmailRequest.getQuoteToLeId();
			validateTriggerInput(userId, quoteToLeId);
			User user = userRepository.findByEmailIdAndStatus(userId, 1);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				updateContactInfo(user, quoteToLe);
				Optional<QuoteDelegation> quoteDelExists = quoteDelegationRepository
						.findByQuoteToLeAndAssignToAndStatus(quoteToLe.get(), user.getId(),
								UserStatusConstants.OPEN.toString());
				if (!quoteDelExists.isPresent()) {
					QuoteDelegation quoteDelegation = new QuoteDelegation();
					quoteDelegation.setAssignTo(user.getId());
					quoteDelegation.setInitiatedBy(user.getCustomer().getId());
					quoteDelegation.setParentId(0);
					quoteDelegation.setStatus(UserStatusConstants.OPEN.toString());
					quoteDelegation.setType(UserStatusConstants.OTHERS.toString());
					quoteDelegation.setRemarks("");
					quoteDelegation.setIpAddress(initiatorIp);
					quoteDelegation.setIsActive((byte) 1);
					quoteDelegation.setQuoteToLe(quoteToLe.get());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					quoteDelegation.setCreatedTime(timestamp);
					quoteDelegationRepository.save(quoteDelegation);

				}
				String orderRefId = quoteToLe.get().getQuote().getQuoteCode();
				User customerUser = userRepository.findByEmailIdAndStatus(triggerEmailRequest.getEmailId(), 1);
				String leMail = getLeAttributes(quoteToLe.get(), LeAttributesConstants.LE_EMAIL.toString());
				MailNotificationBean mailNotificationBean = populateMailNotificationBean(
						getLeAttributes(quoteToLe.get(), LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY),
						customerUser.getCustomer().getCustomerName(), customerUser.getUsername(),
						customerUser.getContactNo(), triggerEmailRequest.getEmailId(), leMail, orderRefId,
						appHost + adminRelativeUrl, quoteToLe.get());
				notificationService.cofDelegationNotification(mailNotificationBean);

				MailNotificationBean mailNotificationBeanCofDelegate = populateMailNotificationBeanCofDelegate(
						quoteToLe.get(), triggerEmailRequest, user, orderRefId, leMail);
				notificationService.cofCustomerDelegationNotification(mailNotificationBeanCofDelegate);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return triggerEmailResponse;

	}

	private void validateTriggerInput(String userId, Integer quoteId) throws TclCommonException {
		if (userId == null || quoteId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteId);
		if (!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
	}

	/**
	 * updateContactInfo
	 *
	 * @param user
	 * @param quoteToLe
	 */
	private void updateContactInfo(User user, Optional<QuoteToLe> opTQuoteLe) {
		if (user != null && opTQuoteLe.isPresent()) {
			QuoteToLe quToLe = opTQuoteLe.get();
			if (quToLe.getQuoteLeAttributeValues() != null && !quToLe.getQuoteLeAttributeValues().isEmpty()) {
				quToLe.getQuoteLeAttributeValues().forEach(attrval -> {
					if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_ID.toString())) {
						attrval.setAttributeValue(String.valueOf(user.getId()));
						quoteLeAttributeValueRepository.save(attrval);
					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.CONTACT_NAME.toString())) {
						attrval.setAttributeValue(user.getFirstName());
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.CONTACT_EMAIL.toString())) {
						attrval.setAttributeValue(user.getEmailId());
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.CONTACT_NO.toString())) {
						attrval.setAttributeValue(user.getContactNo());
						quoteLeAttributeValueRepository.save(attrval);
					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.DESIGNATION.toString())) {
						attrval.setAttributeValue(user.getDesignation());
						quoteLeAttributeValueRepository.save(attrval);

					}

				});
			}
		}

	}

	private MailNotificationBean populateMailNotificationBean(String customerAccountName, String customerName,
			String userName, String userContactNumber, String userEmail, String accountManagerEmail, String orderRefId,
			String quoteLink, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setUserContactNumber(userContactNumber);
		mailNotificationBean.setUserEmail(userEmail);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
		return mailNotificationBean;

	}

	private MailNotificationBean populateMailNotificationBeanCofDelegate(QuoteToLe quoteToLe,
			TriggerEmailRequest triggerEmailRequest, User user, String orderRefId, String leMail) {
		MailNotificationBean mailNotificationBeanCofDelegate = new MailNotificationBean();
		mailNotificationBeanCofDelegate.setCustomerName(user.getFirstName());
		mailNotificationBeanCofDelegate.setUserEmail(triggerEmailRequest.getEmailId());
		mailNotificationBeanCofDelegate.setAccountManagerEmail(leMail);
		mailNotificationBeanCofDelegate.setOrderId(orderRefId);
		mailNotificationBeanCofDelegate.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBeanCofDelegate.setProductName(CommonConstants.IAS);
		return mailNotificationBeanCofDelegate;
	}

	/**
	 *
	 * updateSfdcStage
	 *
	 * @param quoteToLeId
	 * @param stage
	 * @throws TclCommonException
	 */
	public void updateSfdcStage(Integer quoteToLeId, String stage) throws TclCommonException {
		if (StringUtils.isNotBlank(stage) && (SFDCConstants.PROPOSAL_SENT.toString().equals(stage))) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				String sfdcId = quoteToLe.get().getTpsSfdcOptyId();
				omsSfdcService.processUpdateOpportunity(new Date(), sfdcId, stage, quoteToLe.get());
				creditCheckService.resetCreditCheckFields(quoteToLe.get());
			}

		}
	}

	public void deleteTheQuoteIzosdwanAttributeValues(List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues) {
		if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()) {
			quoteIzoSdwanAttributeValuesRepository.deleteAll(quoteIzoSdwanAttributeValues);
		}
	}

	private void removeVproxyDetailsIfUnselected(Quote quote, QuoteToLe quoteToLe) throws TclCommonException {
		try {
			Boolean isSwgRemoved = true;
			Boolean isSpaRemoved = true;
			Boolean isSeaRemoved = true;
			Boolean isSraRemoved = true;
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesIsSwg = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.ISSWG, quote);
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesIsSpa = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.ISSPA, quote);
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesIsSra = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.IS_SRA, quote);
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesIsSea = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.IS_SEA, quote);
			if (quoteIzoSdwanAttributeValuesIsSwg != null && !quoteIzoSdwanAttributeValuesIsSwg.isEmpty()
					&& quoteIzoSdwanAttributeValuesIsSwg.get(0).getAttributeValue()
							.equalsIgnoreCase(CommonConstants.YES)) {
				isSwgRemoved = false;
			}
			if (quoteIzoSdwanAttributeValuesIsSpa != null && !quoteIzoSdwanAttributeValuesIsSpa.isEmpty()
					&& quoteIzoSdwanAttributeValuesIsSpa.get(0).getAttributeValue()
							.equalsIgnoreCase(CommonConstants.YES)) {
				isSpaRemoved = false;
			}

			if (quoteIzoSdwanAttributeValuesIsSra != null && !quoteIzoSdwanAttributeValuesIsSra.isEmpty()
					&& quoteIzoSdwanAttributeValuesIsSra.get(0).getAttributeValue()
							.equalsIgnoreCase(CommonConstants.YES)) {
				isSraRemoved = false;
			}

			if (quoteIzoSdwanAttributeValuesIsSea != null && !quoteIzoSdwanAttributeValuesIsSea.isEmpty()
					&& quoteIzoSdwanAttributeValuesIsSea.get(0).getAttributeValue()
							.equalsIgnoreCase(CommonConstants.YES)) {
				isSeaRemoved = false;
			}
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByReferenceIdForVproxy(quote.getId());
			QuoteDetail quoteDetail = new QuoteDetail();
			quoteDetail.setQuoteId(quote.getId());
			LOGGER.info("Is SPA Removed is {} and in SWG Removed is {}", isSpaRemoved, isSwgRemoved);
			if (isSpaRemoved && isSwgRemoved) {
				LOGGER.info("Both SWG and SPA are unselected!");

				removeAllSolutionVproxy(quoteDetail, quoteToLe, (isSeaRemoved && isSraRemoved) ? true : false);
			} else {
				if (isSpaRemoved) {
					LOGGER.info("SPA is removed!!");
					ProductSolution productSolution = productSolutions.stream().filter(solution -> solution
							.getMstProductOffering().getProductName().contains(IzosdwanCommonConstants.SPA)).findFirst()
							.orElse(null);
					if (productSolution != null) {

						LOGGER.info("Product Solution id to be removed {} and offering name is {}",
								productSolution.getId(), productSolution.getMstProductOffering().getProductName());
						removeUnselectedSolutionVproxy(quoteDetail, productSolution.getQuoteToLeProductFamily(),
								quoteToLe, productSolution);
					}
				}
				if (isSwgRemoved) {
					LOGGER.info("SWG is removed!!");
					ProductSolution productSolution = productSolutions.stream().filter(solution -> solution
							.getMstProductOffering().getProductName().contains(IzosdwanCommonConstants.SWG)).findFirst()
							.orElse(null);
					if (productSolution != null) {
						LOGGER.info("Product Solution id to be removed {} and offering name is {}",
								productSolution.getId(), productSolution.getMstProductOffering().getProductName());
						removeUnselectedSolutionVproxy(quoteDetail, productSolution.getQuoteToLeProductFamily(),
								quoteToLe, productSolution);
					}
				}
				if (productSolutions != null && !productSolutions.isEmpty()) {
					User user = userRepository.findByIdAndStatus(quote.getCreatedBy(), CommonConstants.ACTIVE);
					izosdwanPricingAndFeasibilityService.persistVproxyCommonComponent(quote,
							productSolutions.get(0).getQuoteToLeProductFamily(), user, quoteToLe);
				}

			}

			if (isSeaRemoved && isSraRemoved) {
				removeAllSolutionVutm(quote.getId(), quoteToLe);
				List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetails = quoteIzosdwanVutmLocationDetailRepository
						.findByReferenceId(quote.getId());
				if (quoteIzosdwanVutmLocationDetails != null && !quoteIzosdwanVutmLocationDetails.isEmpty()) {
					quoteIzosdwanVutmLocationDetailRepository.deleteAll(quoteIzosdwanVutmLocationDetails);
				}
				List<String> propsToDelete = new ArrayList<>();
				propsToDelete.add(IzosdwanCommonConstants.SRA_AVG_BW);
				propsToDelete.add(IzosdwanCommonConstants.SRA_BW_SITE);
				propsToDelete.add(IzosdwanCommonConstants.SRA_CON_FACTOR);
				propsToDelete.add(IzosdwanCommonConstants.SRA_VPN_USERS);
				deleteQuoteAttributesByNameAndQuote(quote, propsToDelete);

			} else {
				if (isSeaRemoved) {
					List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetails = quoteIzosdwanVutmLocationDetailRepository
							.findByReferenceId(quote.getId());
					if (quoteIzosdwanVutmLocationDetails != null && !quoteIzosdwanVutmLocationDetails.isEmpty()) {
						quoteIzosdwanVutmLocationDetailRepository.deleteAll(quoteIzosdwanVutmLocationDetails);
					}
				}
				if (isSraRemoved) {
					List<String> propsToDelete = new ArrayList<>();
					propsToDelete.add(IzosdwanCommonConstants.SRA_AVG_BW);
					propsToDelete.add(IzosdwanCommonConstants.SRA_BW_SITE);
					propsToDelete.add(IzosdwanCommonConstants.SRA_CON_FACTOR);
					propsToDelete.add(IzosdwanCommonConstants.SRA_VPN_USERS);
					deleteQuoteAttributesByNameAndQuote(quote, propsToDelete);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in removeVproxyDetailsIfUnselected", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public void deleteQuoteAttributesByNameAndQuote(Quote quote, List<String> displayNames) {
		List<QuoteIzoSdwanAttributeValues> attributes = quoteIzoSdwanAttributeValuesRepository
				.findByDisplayValueInAndQuote(displayNames, quote);
		if (attributes != null && !attributes.isEmpty()) {
			quoteIzoSdwanAttributeValuesRepository.deleteAll(attributes);
		}
	}

	private List<QuoteProductComponentBean> constructQuoteProductComponentVproxy(Integer id,
			Boolean isSitePropertiesNeeded, Boolean isSitePropNeeded) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenVproxy(id, isSitePropertiesNeeded,
				isSitePropNeeded);

		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				QuoteProductComponentBean quoteProductComponentBean = new QuoteProductComponentBean();
				quoteProductComponentBean.setComponentId(quoteProductComponent.getId());
				quoteProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					quoteProductComponentBean
							.setComponentMasterId(quoteProductComponent.getMstProductComponent().getId());
					quoteProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					quoteProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				quoteProductComponentBean.setType(quoteProductComponent.getType());
				quoteProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(quoteProductComponent.getId(),
								isSitePropertiesNeeded, isSitePropNeeded)));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			}

		}
		return quoteProductComponentDtos;

	}

	private List<QuoteProductComponentBean> constructQuoteProductComponentVutm(Integer id,
			Boolean isSitePropertiesNeeded, Boolean isSitePropNeeded) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenVutm(id, isSitePropertiesNeeded,
				isSitePropNeeded);

		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				QuoteProductComponentBean quoteProductComponentBean = new QuoteProductComponentBean();
				quoteProductComponentBean.setComponentId(quoteProductComponent.getId());
				quoteProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					quoteProductComponentBean
							.setComponentMasterId(quoteProductComponent.getMstProductComponent().getId());
					quoteProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					quoteProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				quoteProductComponentBean.setType(quoteProductComponent.getType());
				quoteProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(quoteProductComponent.getId(),
								isSitePropertiesNeeded, isSitePropNeeded)));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			}

		}
		return quoteProductComponentDtos;

	}

	private String getAddonMetricsBasedOnQuestion(String questionString) {
		if (questionString == null) {
			return IzosdwanCommonConstants.USERS;
		} else if (questionString.toLowerCase().contains(IzosdwanCommonConstants.VOLUME_REQUIRED.toLowerCase())) {
			return IzosdwanCommonConstants.VOLUME;
		} else if (questionString.toLowerCase().contains(IzosdwanCommonConstants.MILLION_CELLS.toLowerCase())) {
			return IzosdwanCommonConstants.MILLION_CELLS;
		} else if (questionString.toLowerCase().contains(IzosdwanCommonConstants.QUANTITY.toLowerCase())) {
			return IzosdwanCommonConstants.QUANTITY;
		} else if (questionString.toLowerCase().contains(IzosdwanCommonConstants.CONNECTORS.toLowerCase())) {
			return IzosdwanCommonConstants.CONNECTORS;
		} else {
			return CommonConstants.EMPTY;
		}
	}

	/**
	 * This Method is for processing the attachment Information
	 *
	 * @param attachmentRequest
	 * @throws TclCommonException
	 */
	public void processOmsAttachmentSDD(OmsListenerBean omsAttachmentRequest) throws TclCommonException {

		try {
			LOGGER.info("Entering into sdd attachment service ");

			if (Objects.isNull(omsAttachmentRequest)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			List<OmsAttachBean> omsAttachBean = omsAttachmentRequest.getOmsAttachBean();
			LOGGER.info("Entering into OMS attachment Bean {}", omsAttachBean);

			for (OmsAttachBean bean : omsAttachBean) {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository
						.findByAttachmentTypeAndErfCusAttachmentIdAndReferenceName(bean.getAttachmentType(),
								bean.getAttachmentId(), bean.getReferenceName());
				if (omsAttachments.isEmpty()) {
					OmsAttachment omsAttachment = new OmsAttachment();
					persistOmsAttachmentSDD(bean, omsAttachment);
					LOGGER.info("persisted if empty");

				} else {
					for (OmsAttachment omsAttachment : omsAttachments) {
						persistOmsAttachmentSDD(bean, omsAttachment);
						LOGGER.info("persisted existing");

					}

				}
			}
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(omsAttachBean.get(0).getQouteLeId());
			izosdwanQuoteService.updateLeAttribute(quoteToLe.get(), Utils.getSource(), CommonConstants.SDD,
					omsAttachBean.get(0).getAttachmentId().toString());

			izosdwanQuoteService.updateLeAttribute(quoteToLe.get(), Utils.getSource(), CommonConstants.SDD_ATTACHMENT,
					omsAttachmentRequest.getOmsAttachmentBean().getReferenceName());

		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * persistOmsAttachmentSDD
	 *
	 * @param bean
	 * @param omsAttachment
	 */
	private void persistOmsAttachmentSDD(OmsAttachBean bean, OmsAttachment omsAttachment) {
		omsAttachment.setErfCusAttachmentId(bean.getAttachmentId());
		omsAttachment.setAttachmentType(bean.getAttachmentType());
		omsAttachment.setReferenceName(bean.getReferenceName());
		omsAttachment.setReferenceId(bean.getReferenceId());
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(bean.getQouteLeId());
		if (quoteToLe.isPresent()) {
			omsAttachment.setQuoteToLe(quoteToLe.get());
		}
		if (!Objects.isNull(bean.getOrderLeId())) {
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(bean.getOrderLeId());
			if (orderToLe.isPresent()) {
				omsAttachment.setOrderToLe(orderToLe.get());
			}
		} else {
			omsAttachment.setOrderToLe(null);
		}
		omsAttachmentRepository.save(omsAttachment);
	}

	public Map<String, List<IzoSdwanCpeBomInterface>> contsructCpeSet(Integer quoteId) throws TclCommonException {
		try {
			Map<String, Set<String>> sampleMap = new HashMap<>();
			Map<String, List<IzoSdwanCpeBomInterface>> cpMapList = new HashMap<>();

			List<IzoSdwanCpeBomInterface> cpeInterface = getInterfaceDetails();

			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			if (productSolution != null) {

				List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
						.findByProductSolution(productSolution);

				quoteIzosdwanSites.stream().forEach(site -> {

					Set<String> attr = new HashSet<>();

					List<QuoteProductComponent> componentid = quoteProductComponentRepository
							.findByReferenceId(site.getId());

					componentid.stream().forEach(comp -> {
						if (comp.getMstProductComponent().getName()
								.equalsIgnoreCase(IzosdwanCommonConstants.SITE_PROPERTIES)) {
							List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
									.findByQuoteProductComponent(comp);

							quoteProductComponentsAttributeValues.forEach(y -> {

								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.CPE_NAME)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}

								}

								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.POWER_CORD)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}
								}

								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.NMC)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}
								}

								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.RACKMOUNT)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}
								}
								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.SFP)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}
								}
								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.SFP_PLUS)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}
								}
								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.SFP_PLUS_DESC)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}
								}

								if (y.getProductAttributeMaster().getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.SFP_DESC)) {
									if (y.getAttributeValues() != null) {
										attr.add(y.getAttributeValues());
									}
								}

							});
						}
					});

					if (!sampleMap.containsKey(site.getNewCpe())) {
						sampleMap.put(site.getNewCpe(), attr);
					} else {
						Set<String> existingattr = sampleMap.get(site.getNewCpe());
						existingattr.addAll(attr);
						sampleMap.replace(site.getNewCpe(), existingattr);

					}
				});

				sampleMap.forEach((k, v) -> {
					String key = k;
					Set<String> temp = v;
					List<IzoSdwanCpeBomInterface> list = new ArrayList<>();
					v.forEach(s -> {

						cpeInterface.forEach(bom -> {
							if (k.equalsIgnoreCase(bom.getBomNameCd())) {
								if (s.equalsIgnoreCase(bom.getPhysicalResourceCd())) {
									IzoSdwanCpeBomInterface bomInterface = new IzoSdwanCpeBomInterface();
									bomInterface.setBomNameCd(bom.getBomNameCd());
									bomInterface.setDescription(bom.getDescription());
									bomInterface.setPhysicalResourceCd(bom.getPhysicalResourceCd());
									bomInterface.setInterfaceType(bom.getInterfaceType());
									bomInterface.setProvider(bom.getProvider());
									bomInterface.setProductCategory(bom.getProductCategory());
									bomInterface.setQuantity("1");
									list.add(bomInterface);
								}

							}
						});
					});
					cpMapList.put(k, list);
				});
			}

			return cpMapList;
		}

		catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}

	}

	public String addVutmSolution(VutmProfileDetailsBean vutmProfileDetailsBean, Integer quoteId, Integer quoteToLeId)
			throws TclCommonException {
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			User user = userRepository.findByIdAndStatus(quote.getCreatedBy(), CommonConstants.ACTIVE);
			MstProductFamily mstProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(IzosdwanCommonConstants.VUTM, CommonConstants.BACTIVE);
			if (mstProductFamily != null && quoteToLe.isPresent()) {
				removeAllSolutionVutm(quoteId, quoteToLe.get());
				QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.get().getId(),
								mstProductFamily.getName());
				if (quoteToLeProductFamily == null) {
					quoteToLeProductFamily = new QuoteToLeProductFamily();
					quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
					quoteToLeProductFamily.setQuoteToLe(quoteToLe.get());
					quoteToLeProductFamily = quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
				}
				if (quoteToLeProductFamily != null) {
					MstProductOffering mstProductOffering = mstProductOfferingRepository
							.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,
									vutmProfileDetailsBean.getOfferingName(), CommonConstants.BACTIVE);
					if (mstProductOffering == null) {
						mstProductOffering = new MstProductOffering();
						mstProductOffering.setCreatedBy(Utils.getSource());
						mstProductOffering.setCreatedTime(new Date());
						mstProductOffering.setMstProductFamily(mstProductFamily);
						mstProductOffering.setProductName(vutmProfileDetailsBean.getOfferingName());
						mstProductOffering.setProductDescription(
								String.join(",", vutmProfileDetailsBean.getOfferingDescription()));
						mstProductOffering.setStatus(CommonConstants.BACTIVE);
						mstProductOffering = mstProductOfferingRepository.save(mstProductOffering);
					}
					ProductSolution productSolution = new ProductSolution();
					productSolution.setMstProductOffering(mstProductOffering);
					productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
					productSolution.setSolutionCode(Utils.generateUid());
					productSolution.setProductProfileData(Utils.convertObjectToJson(vutmProfileDetailsBean));
					productSolution = productSolutionRepository.save(productSolution);

					createQuoteProductComponentIfNotPresent(productSolution.getId(),
							IzosdwanCommonConstants.IZOSDWAN_VUTM, IzosdwanCommonConstants.VUTM_COMMON, user,
							IzosdwanCommonConstants.IZOSDWAN_VUTM);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error on persisting vUTM profiles", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	public List<SEASiteDetailsBean> getVutmLocationMappingDetails(Integer quoteId, Integer quoteToLeId,
			String locationName) throws TclCommonException {
		List<SEASiteDetailsBean> seaSiteDetailsBeans = new ArrayList<>();
		try {
			List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetails = quoteIzosdwanVutmLocationDetailRepository
					.findByReferenceId(quoteId);
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			Map<QuoteIzosdwanSite, String> quoteIzosdwanSites = filterSitesForVutm(
					quoteIzosdwanSiteRepository.findByProductSolution(productSolution));
			Map<QuoteIzosdwanSite, String> unMappedSitesForVutm = new HashMap<>();
			if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
				if (quoteIzosdwanVutmLocationDetails != null && !quoteIzosdwanVutmLocationDetails.isEmpty()) {
					quoteIzosdwanSites.forEach((k, v) -> {
						QuoteIzosdwanVutmLocationDetail quoteIzosdwanVutmLocationDetail = quoteIzosdwanVutmLocationDetails
								.stream().filter(vutm -> vutm.getLocationId().equals(k.getErfLocSitebLocationId()))
								.findFirst().orElse(null);
						if (quoteIzosdwanVutmLocationDetail == null) {
							unMappedSitesForVutm.put(k, v);
						}
					});

				} else {
					unMappedSitesForVutm.putAll(quoteIzosdwanSites);
				}
			}

			if (unMappedSitesForVutm != null && !unMappedSitesForVutm.isEmpty()) {
				seaSiteDetailsBeans.addAll(constructVutmSiteDetailsBeanFromSites(unMappedSitesForVutm));
			}
			if (quoteIzosdwanVutmLocationDetails != null && !quoteIzosdwanVutmLocationDetails.isEmpty()) {
				if (StringUtils.isNotBlank(locationName)) {
					List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetailsLoc = quoteIzosdwanVutmLocationDetails
							.stream().filter(detail -> detail.getBreakupLocation().equals(locationName))
							.collect(Collectors.toList());
					if (quoteIzosdwanVutmLocationDetailsLoc != null && !quoteIzosdwanVutmLocationDetailsLoc.isEmpty()) {
						seaSiteDetailsBeans.addAll(
								constructVutmSiteDetailsBeanFromMappedData(quoteIzosdwanVutmLocationDetailsLoc));
					}
				} else {
					seaSiteDetailsBeans
							.addAll(constructVutmSiteDetailsBeanFromMappedData(quoteIzosdwanVutmLocationDetails));
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error on getting VutmLocationMappingDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return seaSiteDetailsBeans;
	}

	private Map<QuoteIzosdwanSite, String> filterSitesForVutm(List<QuoteIzosdwanSite> quoteIzosdwanSites) {
		Map<QuoteIzosdwanSite, String> sites = new HashMap<>();
		if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
			List<Integer> locIds = quoteIzosdwanSites.stream().filter(site -> site.getErfLocSitebLocationId() != null)
					.map(site -> site.getErfLocSitebLocationId()).distinct().collect(Collectors.toList());
			if (locIds != null && !locIds.isEmpty()) {
				LOGGER.info("Got unique location ids");
				locIds.stream().forEach(locId -> {
					List<QuoteIzosdwanSite> locSites = quoteIzosdwanSites.stream()
							.filter(site -> site.getErfLocSitebLocationId().equals(locId)).collect(Collectors.toList());
					if (locSites != null && !locSites.isEmpty()) {
						LOGGER.info("Got sites for the location id {} and count is {}", locId, locSites.size());
						if (locSites.size() == 1) {
							LOGGER.info("Adding location {} as eligible site since only one product of name {}", locId,
									locSites.get(0).getIzosdwanSiteProduct());
							sites.put(locSites.get(0), locSites.get(0).getNewLastmileBandwidth());
						} else {
							LOGGER.info(
									"Checking for any passive sites present for the location of product IAS or BYON for location {}",
									locId);
							List<QuoteIzosdwanSite> iasOrByonSites = locSites.stream()
									.filter(site -> ((site.getIzosdwanSiteProduct().equals(CommonConstants.IAS)
											|| site.getIzosdwanSiteProduct()
													.equals(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT))))
									.collect(Collectors.toList());
							if (iasOrByonSites != null && !iasOrByonSites.isEmpty()) {
								LOGGER.info("Got IAS or BYON records for location {}", locId);
								for (QuoteIzosdwanSite iasOrByonSite : iasOrByonSites) {

									try {
										String attributeValue = izosdwanPricingAndFeasibilityService.getProperityValue(
												iasOrByonSite, IzosdwanCommonConstants.SITE_PROPERTIES,
												IzosdwanCommonConstants.PORT_MODE, iasOrByonSite.getPriSec());
										if (attributeValue != null
												&& attributeValue.equalsIgnoreCase(IzosdwanCommonConstants.PASSIVE)) {
											LOGGER.info(
													"Got one passive port mode site for the location {} and of site product {}",
													locId, iasOrByonSite.getIzosdwanSiteProduct());
											Integer lastMileBw = 0;
											for (QuoteIzosdwanSite site : iasOrByonSites) {
												if (site.getNewLastmileBandwidth() != null) {
													lastMileBw += Integer.parseInt(site.getNewLastmileBandwidth());
												}
											}

											sites.put(iasOrByonSite, Integer.toString(lastMileBw));
											break;
										}
									} catch (TclCommonException e) {
										LOGGER.info("Error in gettting attribute value!!");
										e.printStackTrace();
									}

								}

							}
						}
					}
				});
			}
		}
		LOGGER.info("Eligible sites for vutm is {}", sites.size());
		return sites;
	}

	private List<SEASiteDetailsBean> constructVutmSiteDetailsBeanFromSites(
			Map<QuoteIzosdwanSite, String> quoteIzosdwanSites) {
		List<SEASiteDetailsBean> seaSiteDetailsBeans = new ArrayList<>();
		quoteIzosdwanSites.forEach((k, v) -> {
			SEASiteDetailsBean seaSiteDetailsBean = new SEASiteDetailsBean();
			seaSiteDetailsBean.setBreakupLocation(null);
			seaSiteDetailsBean.setDefaultBw(Integer.toString(((Integer.parseInt(v) * 6) / 10)));
			seaSiteDetailsBean.setIsSelected(CommonConstants.INACTIVE);
			seaSiteDetailsBean.setLocalLoopBw(v);
			seaSiteDetailsBean.setLocationId(k.getErfLocSitebLocationId());
			seaSiteDetailsBean.setMaxBw(v);
			seaSiteDetailsBean.setSelectedBw(null);
			seaSiteDetailsBean.setId(null);
			seaSiteDetailsBeans.add(seaSiteDetailsBean);
		});
		return seaSiteDetailsBeans;
	}

	private List<SEASiteDetailsBean> constructVutmSiteDetailsBeanFromMappedData(
			List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetails) {
		List<SEASiteDetailsBean> seaSiteDetailsBeans = new ArrayList<>();
		quoteIzosdwanVutmLocationDetails.stream().forEach(detail -> {
			SEASiteDetailsBean seaSiteDetailsBean = new SEASiteDetailsBean();
			seaSiteDetailsBean.setBreakupLocation(detail.getBreakupLocation());
			seaSiteDetailsBean.setDefaultBw(detail.getDefaultBw());
			seaSiteDetailsBean.setIsSelected(detail.getIsActive());
			seaSiteDetailsBean.setLocalLoopBw(detail.getMaxBw());
			seaSiteDetailsBean.setLocationId(detail.getLocationId());
			seaSiteDetailsBean.setMaxBw(detail.getMaxBw());
			seaSiteDetailsBean.setSelectedBw(detail.getSelectedBw());
			seaSiteDetailsBean.setId(detail.getId());
			seaSiteDetailsBeans.add(seaSiteDetailsBean);
		});
		return seaSiteDetailsBeans;
	}

	public SEASiteInfoBean getSiteInformationForVutm(Integer quoteId, Integer quoteToLeId, String locationName)
			throws TclCommonException {
		SEASiteInfoBean seaSiteInfoBean = new SEASiteInfoBean();
		Integer totalMappedCount = 0;
		Integer totalCount = 0;
		Integer totalUnMappedCount = 0;
		List<Integer> locationIds = new ArrayList<>();
		try {
			ProductSolution productSolutionIzosdwan = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			LOGGER.info("Quote id {}", quoteId);
			List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetails = quoteIzosdwanVutmLocationDetailRepository
					.findByReferenceId(quoteId);
			Map<QuoteIzosdwanSite, String> quoteIzosdwanSites = filterSitesForVutm(
					quoteIzosdwanSiteRepository.findByProductSolution(productSolutionIzosdwan));
			Map<QuoteIzosdwanSite, String> unMappedSitesForVutm = new HashMap<>();
			if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
				if (quoteIzosdwanVutmLocationDetails != null && !quoteIzosdwanVutmLocationDetails.isEmpty()) {
					quoteIzosdwanSites.forEach((k, v) -> {
						QuoteIzosdwanVutmLocationDetail quoteIzosdwanVutmLocationDetail = quoteIzosdwanVutmLocationDetails
								.stream().filter(vutm -> vutm.getLocationId().equals(k.getErfLocSitebLocationId()))
								.findFirst().orElse(null);
						if (quoteIzosdwanVutmLocationDetail == null) {
							unMappedSitesForVutm.put(k, v);
						}
					});

				} else {
					unMappedSitesForVutm.putAll(quoteIzosdwanSites);
				}
			}

			if (unMappedSitesForVutm != null && !unMappedSitesForVutm.isEmpty()) {
				totalUnMappedCount = unMappedSitesForVutm.size();
				List<SEASiteDetailsBean> seaSiteDetailsBeans = constructVutmSiteDetailsBeanFromSites(
						unMappedSitesForVutm);
				if (seaSiteDetailsBeans != null && !seaSiteDetailsBeans.isEmpty()) {
					seaSiteInfoBean.setUnMappedSiteDetails(seaSiteDetailsBeans);
				}
				List<Integer> unMappedLocId = new ArrayList<>();
				unMappedSitesForVutm.forEach((k, v) -> {
					if (k.getErfLocSitebLocationId() != null) {
						unMappedLocId.add(k.getErfLocSitebLocationId());
					}
				});
				locationIds.addAll(unMappedLocId.stream().distinct().collect(Collectors.toList()));
			} else {
				seaSiteInfoBean.setUnMappedSiteDetails(new ArrayList<>());
			}
			if (quoteIzosdwanVutmLocationDetails != null && !quoteIzosdwanVutmLocationDetails.isEmpty()) {
				totalMappedCount = quoteIzosdwanVutmLocationDetails.size();
				List<String> breakOutLocations = quoteIzosdwanVutmLocationDetails.stream()
						.filter(detail -> detail.getBreakupLocation() != null)
						.map(detail -> detail.getBreakupLocation()).distinct().collect(Collectors.toList());
				List<SEAMappedSiteDetailsBean> seaMappedSiteDetailsBeans = new ArrayList<>();
				if (StringUtils.isNotBlank(locationName)) {
					List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetailsLoc = quoteIzosdwanVutmLocationDetails
							.stream().filter(detail -> detail.getBreakupLocation().equals(locationName))
							.collect(Collectors.toList());
					if (quoteIzosdwanVutmLocationDetailsLoc != null && !quoteIzosdwanVutmLocationDetailsLoc.isEmpty()) {
						seaMappedSiteDetailsBeans
								.add(constructMappedSiteDetails(quoteIzosdwanVutmLocationDetailsLoc, locationName));
						locationIds.addAll(quoteIzosdwanVutmLocationDetailsLoc.stream()
								.filter(site -> site.getLocationId() != null).map(site -> site.getLocationId())
								.distinct().collect(Collectors.toList()));
					}
				} else {
					breakOutLocations.stream().forEach(location -> {
						List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetailsLoc = quoteIzosdwanVutmLocationDetails
								.stream().filter(detail -> detail.getBreakupLocation().equals(location))
								.collect(Collectors.toList());
						if (quoteIzosdwanVutmLocationDetailsLoc != null
								&& !quoteIzosdwanVutmLocationDetailsLoc.isEmpty()) {
							seaMappedSiteDetailsBeans
									.add(constructMappedSiteDetails(quoteIzosdwanVutmLocationDetailsLoc, location));
							locationIds.addAll(quoteIzosdwanVutmLocationDetailsLoc.stream()
									.filter(site -> site.getLocationId() != null).map(site -> site.getLocationId())
									.distinct().collect(Collectors.toList()));
						}
					});
				}
				seaSiteInfoBean.setMappedBreakupLocation(breakOutLocations);
				seaSiteInfoBean.setMappedSiteDetails(seaMappedSiteDetailsBeans);
			} else {
				seaSiteInfoBean.setMappedBreakupLocation(new ArrayList<>());
				seaSiteInfoBean.setMappedSiteDetails(new ArrayList<>());
			}
			totalCount = totalMappedCount + totalUnMappedCount;
			seaSiteInfoBean.setMappedCount(totalMappedCount);
			seaSiteInfoBean.setTotalCount(totalCount);
			seaSiteInfoBean.setUnMappedCount(totalUnMappedCount);
			if (locationIds != null && !locationIds.isEmpty()) {
				seaSiteInfoBean.setLocationsIds(locationIds.stream().distinct().collect(Collectors.toList()));
			}

		} catch (Exception e) {
			LOGGER.error("Error on getting VutmLocationMappingDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return seaSiteInfoBean;
	}

	private SEAMappedSiteDetailsBean constructMappedSiteDetails(
			List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetails, String breakupLocation) {
		SEAMappedSiteDetailsBean seaMappedSiteDetailsBean = new SEAMappedSiteDetailsBean();
		List<SEASiteDetailsBean> seaSiteDetailsBeans = constructVutmSiteDetailsBeanFromMappedData(
				quoteIzosdwanVutmLocationDetails);
		seaMappedSiteDetailsBean.setLocations(seaSiteDetailsBeans);
		seaMappedSiteDetailsBean.setBreakupLocation(breakupLocation);
		seaMappedSiteDetailsBean.setTotalSites(seaSiteDetailsBeans.size());
		Integer totalLastMileBw = 0;
		for (SEASiteDetailsBean seaSiteDetailsBean : seaSiteDetailsBeans) {
			totalLastMileBw += Integer.parseInt(seaSiteDetailsBean.getSelectedBw());
		}
		seaMappedSiteDetailsBean.setTotalBw(Integer.toString(totalLastMileBw));
		return seaMappedSiteDetailsBean;
	}

	public String updateUvtmSiteMappingDetails(Integer quoteId, Integer quoteToLeId,
			SEASiteUpdateRequest seaSiteUpdateRequest) throws TclCommonException {
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			if (quote != null && seaSiteUpdateRequest != null && seaSiteUpdateRequest.getUpdateRequests() != null
					&& !seaSiteUpdateRequest.getUpdateRequests().isEmpty()) {
				seaSiteUpdateRequest.getUpdateRequests().stream().forEach(req -> {
					if (StringUtils.isNotBlank(req.getBreakupLocation()) && req.getSiteDetails() != null
							&& !req.getSiteDetails().isEmpty()) {
						req.getSiteDetails().stream().forEach(reqDetail -> {

							if (reqDetail.getId() != null) {
								Optional<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetail = quoteIzosdwanVutmLocationDetailRepository
										.findById(reqDetail.getId());
								if (reqDetail.getIsSelected() == 1) {
									quoteIzosdwanVutmLocationDetail.get().setBreakupLocation(req.getBreakupLocation());
									quoteIzosdwanVutmLocationDetail.get().setDefaultBw(reqDetail.getDefaultBw());
									quoteIzosdwanVutmLocationDetail.get().setIsActive(CommonConstants.ACTIVE);
									quoteIzosdwanVutmLocationDetail.get().setLocationId(reqDetail.getLocationId());
									quoteIzosdwanVutmLocationDetail.get().setMaxBw(reqDetail.getMaxBw());
									quoteIzosdwanVutmLocationDetail.get().setReferenceId(quoteId);
									quoteIzosdwanVutmLocationDetail.get().setSelectedBw(reqDetail.getSelectedBw());
									quoteIzosdwanVutmLocationDetailRepository
											.save(quoteIzosdwanVutmLocationDetail.get());
								} else if (reqDetail.getIsSelected() == 0) {
									quoteIzosdwanVutmLocationDetailRepository
											.delete(quoteIzosdwanVutmLocationDetail.get());
								}
							} else {
								if (reqDetail.getIsSelected() == 1) {
									QuoteIzosdwanVutmLocationDetail quoteIzosdwanVutmLocationDetail = new QuoteIzosdwanVutmLocationDetail();
									quoteIzosdwanVutmLocationDetail.setBreakupLocation(req.getBreakupLocation());
									quoteIzosdwanVutmLocationDetail.setDefaultBw(reqDetail.getDefaultBw());
									quoteIzosdwanVutmLocationDetail.setIsActive(CommonConstants.ACTIVE);
									quoteIzosdwanVutmLocationDetail.setLocationId(reqDetail.getLocationId());
									quoteIzosdwanVutmLocationDetail.setMaxBw(reqDetail.getMaxBw());
									quoteIzosdwanVutmLocationDetail.setReferenceId(quoteId);
									quoteIzosdwanVutmLocationDetail.setSelectedBw(reqDetail.getSelectedBw());
									quoteIzosdwanVutmLocationDetailRepository
											.save(quoteIzosdwanVutmLocationDetail);
								}
							}
						});
					}
				});
			}
		} catch (Exception e) {
			LOGGER.error("Error on updating VutmLocationMappingDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	/**
	 * @return ResponseResource
	 * @throws TclCommonException
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to update the
	 *            order subtype and owner region
	 */

	public void updateO2cComponents(Integer quoteId) throws TclCommonException {
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quote != null) {
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
			if (quoteToLes.stream().findFirst().isPresent()) {
				QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get();
				List<QuoteToLeProductFamily> quoteLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe(quoteToLe.getId());
				if (!(quoteLeProductFamily.isEmpty())) {
					quoteLeProductFamily.stream().forEach(quotLeProdFamName -> {
						List<ProductSolution> productSolutions = productSolutionRepository
								.findByQuoteToLeProductFamily(quotLeProdFamName);
						productSolutions.stream().forEach(prodSolution -> {
							List<QuoteIzosdwanSite> quoteIllSites = quoteIzosdwanSiteRepository
									.findByProductSolutionAndStatus(prodSolution, (byte) 1);
							quoteIllSites.stream().forEach(izosdwanSite -> {
								if (!izosdwanSite.getIzosdwanSiteProduct().contains("BYON")) {
									O2CSubCategoryBean subCategory = new O2CSubCategoryBean();
									SIServiceDetailDataBean sIServiceDetailDataBean = null;
									String llBwChange = null;
									String portBwChange = null;
									try {
										String serviceId = izosdwanSite.getErfServiceInventoryTpsServiceId();
										sIServiceDetailDataBean = omsIzosdwanUtils.getServiceDetailIzosdwan(serviceId);
										llBwChange = getLlBwChange(izosdwanSite);
										portBwChange = getPortBwChange(izosdwanSite);
										String upgradeOrDowngradeBwChange = isUpgradeOrDowngrade(llBwChange,
												portBwChange);
										String isBSOChanged = isBSOChanged(sIServiceDetailDataBean, izosdwanSite,
												izosdwanSite.getPriSec());
										evaluateO2COrderSubCategory(isBSOChanged, upgradeOrDowngradeBwChange,
												subCategory, izosdwanSite);
										if (subCategory.getO2cOrderType() == null
												|| subCategory.getO2cOrderType().isEmpty()) {
											subCategory.setO2cOrderType(IzosdwanCommonConstants.ORDER_CATEGORY_CO);
										}
										Map<String, ProductAttributeMaster> productAttributeMasterMap = new HashMap<>();
										List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
												.findAll();
										if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
											productAttributeMasters.stream().forEach(master -> {
												productAttributeMasterMap.put(master.getName(), master);
											});
										}
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
												.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
														izosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
														QuoteConstants.IZOSDWAN_SITES.toString());
										if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
											LOGGER.info("Updating the order type and subtype attributes");
											ProductAttributeMaster productAttributeMaster = getProductAttributeMasterByName(
													IzosdwanCommonConstants.ORDER_SUB_CATEGORY,
													productAttributeMasterMap);
											updateSiteAttributes(productAttributeMaster,
													quoteProductComponentList.get(0), subCategory.getO2cOrderSubType());
											ProductAttributeMaster productAttributeMaster1 = getProductAttributeMasterByName(
													IzosdwanCommonConstants.ORDER_CATEGORY, productAttributeMasterMap);
											updateSiteAttributes(productAttributeMaster1,
													quoteProductComponentList.get(0), subCategory.getO2cOrderType());
										}
										// update in quote_ill_site_to_service
										List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository
												.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(
														izosdwanSite.getErfServiceInventoryTpsServiceId(),
														izosdwanSite.getProductSolution().getQuoteToLeProductFamily()
																.getQuoteToLe());
										if (quoteSiteToService != null && !quoteSiteToService.isEmpty()) {
											LOGGER.info(
													"Updating ordersubacategory data in QuoteIllSiteToService for site id {} ",
													izosdwanSite.getId());
											quoteSiteToService.get(0)
													.setErfSfdcSubType(subCategory.getO2cOrderSubType());
											quoteSiteToService.get(0)
													.setErfSfdcOrderType(subCategory.getO2cOrderType());
											quoteSiteToService.get(0).setQuoteIzosdwanSite(izosdwanSite);
											quoteIllSiteToServiceRepository.save(quoteSiteToService.get(0));
										}
									} catch (Exception e) {

									}
								}
							});
						});

					});
				}
				// sfdc get owner region
				if (Objects.nonNull(quoteToLe.getTpsSfdcOptyId())) {
					SfdcCreditCheckQueryRequest queryRequest = new SfdcCreditCheckQueryRequest();
					queryRequest.setWhereClause(
							IzosdwanCommonConstants.WHERE_CLAUSE + " ('" + quoteToLe.getTpsSfdcOptyId() + "')");
					queryRequest.setObjectName(IzosdwanCommonConstants.OBJECT_NAME);
					queryRequest.setFields(IzosdwanCommonConstants.QUERY_FIELD);
					try {
						String mqResponse = (String) mqUtils.sendAndReceive(ownerRegionQuery,
								Utils.convertObjectToJson(queryRequest));
						LOGGER.info("owner region response:{}", mqResponse);
						if (mqResponse != null) {
							OwnerRegionQueryResponseBean ownerRegionResponse = Utils.convertJsonToObject(mqResponse,
									OwnerRegionQueryResponseBean.class);
							if (ownerRegionResponse != null && ownerRegionResponse.getStatus() != null
									&& ownerRegionResponse.getOpportunityOwnersRegionc() != null
									&& ownerRegionResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)
									&& !ownerRegionResponse.getOpportunityOwnersRegionc().isEmpty()) {
								// persist in quote le attribute values
								Attributes attr = new Attributes();
								attr.setAttributeName(IzosdwanCommonConstants.OWNER_REGION);
								attr.setAttributeValue(ownerRegionResponse.getOpportunityOwnersRegionc());
								List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
										.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attr.getAttributeName());
								if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
									updateAttributes(attr, quoteLeAttributeValues);

								} else {
									createAttribute(attr, quoteToLe);

								}
							}
						}
					} catch (Exception e) {
						throw new TclCommonRuntimeException(e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}
				}
			}
		}
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return the
	 *            bandwidths (port and LL) if upgraded or not
	 * @return attrValue
	 */
	private String isUpgradeOrDowngrade(String llBwChange, String portBwChange) {
		String attrValue = "";
		if (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
				|| CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange)
				|| (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
						&& CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange)))
			attrValue = CommonConstants.DOWNGRADE;

		else if (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)
				|| CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange)
				|| (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)
						&& CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange)))
			attrValue = CommonConstants.UPGRADE;
		LOGGER.info("Change in Bandwidth in isUpgradeOrDowngrade method is {}", attrValue);
		return attrValue;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @param izosdwanSite
	 * @return
	 * @throws TclCommonException
	 */
	private String getLlBwChange(QuoteIzosdwanSite izosdwanSite) throws TclCommonException {

		Double oldLlBw = 0D;
		String oldLLBw = getAttributeValue(izosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
				IzosdwanCommonConstants.OLD_LOCAL_LOOP_BANDWIDTH);
		String oldLlBwUnit = getAttributeValue(izosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
				IzosdwanCommonConstants.OLD_LOCAL_LOOP_BANDWIDTH_UNIT);
		Double newLlBw = Double.parseDouble(getAttributeValue(izosdwanSite.getId(),
				IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH));
		if (StringUtils.isNotBlank(oldLLBw) && StringUtils.isNotBlank(oldLlBwUnit))
			oldLlBw = Double.parseDouble(illPricingFeasibilityService.setBandwidthConversion(oldLLBw, oldLlBwUnit));
		String changeInLlBw = "";

		if (Objects.nonNull(oldLlBw) && Objects.nonNull(newLlBw)) {

			LOGGER.info(
					"Before Comparison, for changeinLLBW old bandwidth is " + oldLlBw + "new bandwidth is " + newLlBw);

			int result = newLlBw.compareTo(oldLlBw);

			if (result > 0)
				changeInLlBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInLlBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInLlBw = CommonConstants.EQUAL;
		}
		LOGGER.info("LL Bw inside getPortBwChange method{}", changeInLlBw);

		return changeInLlBw;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @param izosdwanSite
	 * @return
	 * @throws TclCommonException
	 */
	private String getPortBwChange(QuoteIzosdwanSite izosdwanSite) throws TclCommonException {

		Double oldPortBw = 0D;
		String oldPtBw = getAttributeValue(izosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
				IzosdwanCommonConstants.OLD_PORT_BANDWIDTH);
		String oldPortBwUnit = getAttributeValue(izosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
				IzosdwanCommonConstants.OLD_PORT_BANDWIDTH_UNIT);
		Double newPortBw = Double.parseDouble(getAttributeValue(izosdwanSite.getId(),
				IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.PORT_BANDWIDTH));
		if (StringUtils.isNotBlank(oldPtBw) && StringUtils.isNotBlank(oldPortBwUnit))
			oldPortBw = Double.parseDouble(illPricingFeasibilityService.setBandwidthConversion(oldPtBw, oldPortBwUnit));
		String changeInPortBw = "";

		if (Objects.nonNull(oldPortBw) && Objects.nonNull(newPortBw)) {

			LOGGER.info("Before Comparison, for changeinLLBW old bandwidth is " + oldPortBw + "new bandwidth is "
					+ newPortBw);

			int result = newPortBw.compareTo(oldPortBw);

			if (result > 0)
				changeInPortBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInPortBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInPortBw = CommonConstants.EQUAL;
		}
		LOGGER.info("Port Bw inside getPortBwChange method{}", changeInPortBw);

		return changeInPortBw;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @param siteId
	 * @param componentName
	 * @param attributeName
	 * @return
	 */
	private String getAttributeValue(Integer siteId, String componentName, String attributeName) {
		QuoteProductComponent qpc = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId, componentName,
						IzosdwanCommonConstants.IZOSDWAN_SITES)
				.stream().findFirst().orElse(null);
		if (qpc != null) {
			QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(qpc.getId(), attributeName).stream()
					.findFirst().orElse(null);
			if (qpcav != null) {
				LOGGER.info("Got Attribute value for component {} and attribute name {} as {}", componentName,
						attributeName, qpcav.getAttributeValues());
				return qpcav.getAttributeValues();
			}
		}
		return CommonConstants.EMPTY;
	}

	/**
	 *
	 * @param sIServiceDetailDataBean
	 * @param quoteIllSite
	 * @param type
	 * @return
	 * @throws TclCommonRuntimeException
	 */
	private String isBSOChanged(SIServiceDetailDataBean sIServiceDetailDataBean, QuoteIzosdwanSite quoteIllSite,
			String type) throws TclCommonRuntimeException {
		String isBSOChanged = CommonConstants.NO;
		String[] newLastMileProvider = { null };
		String[] newAccessType = { null };
		String[] newVendorId = { null };
		String[] newVendorName = { null };
		String oldLastMileType = null;
		String newLastMileType = null;
		String[] oldVendorId = { null };
		String[] oldVendorName = { null };
		LOGGER.info("quoteIllSite Id {}, productName {}, type {}", quoteIllSite.getId(), type);
		List<IzosdwanSiteFeasibility> siteFeasibilityList = siteFeasibilityRepository
				.findByQuoteIzosdwanSite_IdAndIsSelectedAndType(quoteIllSite.getId(), CommonConstants.BACTIVE, type);
		if (siteFeasibilityList != null && !siteFeasibilityList.isEmpty()) {
			siteFeasibilityList.stream().forEach(siteFeasibility -> {
				LOGGER.info("site feasibility -provider  :: {}", siteFeasibility.getProvider());
				newLastMileProvider[0] = siteFeasibility.getProvider();
				newAccessType[0] = siteFeasibility.getFeasibilityMode();
				if (Objects.nonNull(newAccessType[0])
						&& newAccessType[0].toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE)) {
					try {
						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(siteFeasibility.getResponseJson());
						newVendorId[0] = (String) jsonObj.get(MACDConstants.VENDOR_ID);
						newVendorName[0] = (String) jsonObj.get(MACDConstants.VENDOR_NAME);
					} catch (org.json.simple.parser.ParseException e) {
						LOGGER.info("Exception {}", e.getMessage());
						throw new TclCommonRuntimeException(e);
					}
				}
			});
		}
		if (Objects.nonNull(sIServiceDetailDataBean.getAttributes())
				&& !sIServiceDetailDataBean.getAttributes().isEmpty()) {
			sIServiceDetailDataBean.getAttributes().stream().forEach(attribute -> {
				if (attribute.getName().equalsIgnoreCase(MACDConstants.VENDOR_ID))
					oldVendorId[0] = attribute.getValue();
				else if (attribute.getName().equalsIgnoreCase(MACDConstants.VENDOR_NAME))
					oldVendorName[0] = attribute.getValue();
			});
		}
		LOGGER.info("New Values :: LAST MILE -provider {}, access Type {}, vendor id {}, vendor name {}",
				newLastMileProvider, newAccessType, newVendorId, newVendorName);
		LOGGER.info(
				"Service inventory last mile provider {}, service inventory access type {}, old vendor id {}, old vendor name {} ",
				sIServiceDetailDataBean.getAccessProvider(), sIServiceDetailDataBean.getAccessType(), oldVendorId,
				oldVendorName);

		if (sIServiceDetailDataBean.getLmType() != null
				&& !MACDConstants.OFFNET_SMALL_CASE.equalsIgnoreCase(sIServiceDetailDataBean.getLmType())) {
			LOGGER.info("passing lm type {}", sIServiceDetailDataBean.getLmType());
			oldLastMileType = normaliseLastMileType(sIServiceDetailDataBean.getLmType());
		} else if (sIServiceDetailDataBean.getAccessType() != null) {
			LOGGER.info("passing access type {}", sIServiceDetailDataBean.getAccessType());
			oldLastMileType = normaliseLastMileType(sIServiceDetailDataBean.getAccessType());
		}
		if (newAccessType[0] != null)
			newLastMileType = normaliseLastMileType(newAccessType[0]);
		LOGGER.info("oldLastMileType {}, newLastMileType {}", oldLastMileType, newLastMileType);

		if (newLastMileType != null && MACDConstants.MACD.equalsIgnoreCase(newLastMileType))
			isBSOChanged = CommonConstants.NO;
		else if (oldLastMileType != null && newLastMileType != null
				&& oldLastMileType.equalsIgnoreCase(newLastMileType)) {
			if (newLastMileType.equals(MACDConstants.ONNET_WIRELINE))
				isBSOChanged = CommonConstants.NO;
			else if (newLastMileType.equals(MACDConstants.ONNET_RF)) {
				LOGGER.info("entering onnetRF comparison");
				if ((sIServiceDetailDataBean.getAccessProvider().contains("PMP")
						&& !newLastMileProvider[0].contains("PMP"))
						|| (!sIServiceDetailDataBean.getAccessProvider().contains("PMP")
								&& newLastMileProvider[0].contains("PMP")))
					isBSOChanged = CommonConstants.YES;
			} else if (newLastMileType.equals(MACDConstants.OFFNET_RF)
					|| newLastMileType.equals(MACDConstants.OFFNET_WIRELINE)) {
				LOGGER.info("entering offnetRF comparison");
				if (oldVendorId[0] != null && newVendorId[0] != null && !oldVendorId[0].equals(newVendorId[0]))
					isBSOChanged = CommonConstants.YES;
			}
		} else if (newLastMileType != null && oldLastMileType != null
				&& !oldLastMileType.equalsIgnoreCase(newLastMileType)
				&& ((oldLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE)
						&& !newLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE))
						|| (oldLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE)
								&& !newLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE))
						|| (oldLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE)
								&& newLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE))
						|| (oldLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE)
								&& newLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE)))) {
			LOGGER.info("entering onnet offnet comparison");
			isBSOChanged = CommonConstants.YES;
		}

		LOGGER.info("BSO changed flag {}", isBSOChanged);

		return isBSOChanged;

	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @param lastMileType
	 * @return
	 */
	private String normaliseLastMileType(String lastMileType) {
		String nLastMileType = null;
		if (lastMileType.toLowerCase().contains("onnet wireless"))
			nLastMileType = MACDConstants.ONNET_RF;
		else if (lastMileType.toLowerCase().contains("offnet wireless"))
			nLastMileType = MACDConstants.OFFNET_RF;
		else if (lastMileType.toLowerCase().contains("offnet wireline"))
			nLastMileType = MACDConstants.OFFNET_WIRELINE;
		else if (lastMileType.toLowerCase().contains("onnet wireline"))
			nLastMileType = MACDConstants.ONNET_WIRELINE;
		LOGGER.info("input last mile type {}, nLastMileType {}", lastMileType, nLastMileType);
		if (nLastMileType == null)
			nLastMileType = lastMileType;
		return nLastMileType;

	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @param isBSOChanged
	 * @param upgradeOrDowngradeBwChange
	 * @param subCategory
	 * @return
	 */

	private O2CSubCategoryBean evaluateO2COrderSubCategory(String isBSOChanged, String upgradeOrDowngradeBwChange,
			O2CSubCategoryBean subCategory, QuoteIzosdwanSite izosdwanSite) {
		LOGGER.info("evaluateO2COrderSubCategory :: isBSOChanged {}, upgradeOrDowngradeBwChange {}", isBSOChanged,
				upgradeOrDowngradeBwChange);
		if (!upgradeOrDowngradeBwChange.isEmpty()) {
			if (CommonConstants.NO.equalsIgnoreCase(isBSOChanged)) {
				subCategory.setO2cOrderType(IzosdwanCommonConstants.ORDER_CATEGORY_CB);
				subCategory.setO2cOrderSubType(CommonConstants.HOT + upgradeOrDowngradeBwChange);
			} else {
				subCategory.setO2cOrderType(IzosdwanCommonConstants.ORDER_CATEGORY_CB);
				subCategory.setO2cOrderSubType(
						CommonConstants.HOT + upgradeOrDowngradeBwChange + MACDConstants.BSO_CHANGE);
			}
		} else {
			if (izosdwanSite.getManagementType() != null
					&& izosdwanSite.getManagementType().equalsIgnoreCase(IzosdwanCommonConstants.UNMANAGED)) {
				subCategory.setO2cOrderType(IzosdwanCommonConstants.ORDER_CATEGORY_CB);
				subCategory.setO2cOrderSubType(IzosdwanCommonConstants.HOT_UPGRADE);
			}
		}
		return subCategory;

	}

	public String getFormattedCurrencyBig(BigDecimal num, String currency) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		if (currency.equals("INR")) {
			formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		}
		com.ibm.icu.text.DecimalFormat formatter1 = (com.ibm.icu.text.DecimalFormat) formatter;
		DecimalFormatSymbols symbols = formatter1.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formatter1.setDecimalFormatSymbols(symbols);
		if (num != null) {
			return formatter1.format(num);
		} else {
			return num + "";
		}

	}

	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}

	public ContactAttributeInfo getContactAttributeDetails(Integer quoteLeId) {
		ContactAttributeInfo contactAttributeInfo = null;
		try {
			contactAttributeInfo = new ContactAttributeInfo();
			Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(quoteLeId);
			if (optionalQuoteLe.isPresent()) {
				QuoteToLeBean quoteToLeBean = new QuoteToLeBean(optionalQuoteLe.get());
				QuoteToLe quoteToLe = optionalQuoteLe.get();
				if (quoteToLeBean != null && quoteToLe != null) {
					CustomerLeContactDetailBean cusLeContact = getCustomerLeContact(quoteToLeBean);
					Map<String, String> gstMap = new HashMap<>();
					String gstNo = "";
					if (cusLeContact != null) {
						contactAttributeInfo = new ContactAttributeInfo();
						contactAttributeInfo.setContactNo(cusLeContact.getMobilePhone());
						contactAttributeInfo.setDesignation(cusLeContact.getTitle());
						contactAttributeInfo.setEmailId(cusLeContact.getEmailId());
						contactAttributeInfo.setFirstName(cusLeContact.getName());
						contactAttributeInfo.setUserId(cusLeContact.getName());
					}
					// GST details
					for (QuoteLeAttributeValue attribute : quoteToLe.getQuoteLeAttributeValues()) {
						if (LeAttributesConstants.LE_STATE_GST_NO.toString()
								.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
							gstMap.put(LeAttributesConstants.LE_STATE_GST_NO, attribute.getAttributeValue());
							LOGGER.info("lestategstno:{}", attribute.getAttributeValue());
						} else if (LeAttributesConstants.GST_NUMBER.toString()
								.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
							gstMap.put(LeAttributesConstants.GST_NUMBER, attribute.getAttributeValue());
							LOGGER.info("gstnumber:{}", attribute.getAttributeValue());
						}

					}
					if (gstMap.containsKey(LeAttributesConstants.LE_STATE_GST_NO)) {
						gstNo = gstMap.get(LeAttributesConstants.LE_STATE_GST_NO);
					} else if (gstMap.containsKey(LeAttributesConstants.GST_NUMBER)) {
						gstNo = gstMap.get(LeAttributesConstants.GST_NUMBER);
					} else
						gstNo = PDFConstants.NO_REGISTERED_GST;
					contactAttributeInfo.setGstNumber(gstNo);
				}
			}
		} catch (TclCommonException | IllegalArgumentException e) {
			LOGGER.error("Error retriving contact attributes {}", e);
		}
		return contactAttributeInfo;
	}

	private CustomerLeContactDetailBean getCustomerLeContact(QuoteToLeBean quoteToLe)
			throws TclCommonException, IllegalArgumentException {
		if (quoteToLe.getCustomerLegalEntityId() != null) {
			LOGGER.info("Customer LE Contact called {}", quoteToLe.getCustomerLegalEntityId());
			String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
					String.valueOf(quoteToLe.getCustomerLegalEntityId()));
			CustomerLeContactDetailBean[] customerLeContacts = (CustomerLeContactDetailBean[]) Utils
					.convertJsonToObject(response, CustomerLeContactDetailBean[].class);
			return customerLeContacts[0];
		} else {
			return null;
		}

	}

	/**
	 * @author mpalanis
	 * @param file
	 * @param referenceName
	 * @param attachmentType
	 * @return
	 * @throws TclCommonException
	 */
	public ServiceResponse processUploadFileSS(MultipartFile file, String referenceName, String attachmentType)
			throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				InputStream inputStream = file.getInputStream();
				StoredObject storedObject = fileStorageService.uploadObjectSDD(file.getOriginalFilename(), inputStream,
						null);
				String[] pathArray = storedObject.getPath().split("/");
				com.tcl.dias.oms.entity.entities.Attachment attachment = new com.tcl.dias.oms.entity.entities.Attachment();
				attachment.setCreatedDate(new Timestamp(new Date().getTime()));
				attachment.setName(storedObject.getName());
				attachment.setStoragePathUrl(pathArray[1]);
				attachment.setType(attachmentType);
				attachment.setCategory(referenceName);
				attachmentRepository.save(attachment);
				LOGGER.info("Attachment Saved with the attachment id {}", attachment.getId());
				fileUploadResponse.setAttachmentId(attachment.getId());
				fileUploadResponse.setStatus(Status.SUCCESS);
				fileUploadResponse.setFileName(storedObject.getName());
				fileUploadResponse.setUrlPath(pathArray[1]);

//				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
//				OmsAttachBean omsAttachBean = new OmsAttachBean();
//				omsAttachBean.setAttachmentId(attachment.getId());
//				omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
//				omsAttachBean.setReferenceName(referenceName);
//				omsAttachBeanList.add(omsAttachBean);
//
//				OmsListenerBean listenerBean = new OmsListenerBean();
//				OmsAttachBean omsAttachBean2=new OmsAttachBean();
//				omsAttachBean2.setReferenceName(file.getOriginalFilename());
//
//				listenerBean.setOmsAttachBean(omsAttachBeanList);
//				listenerBean.setOmsAttachmentBean(omsAttachBean2);
//				processOmsAttachmentSDD(listenerBean);

			} else {
				if (Objects.isNull(file)) {
					throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
				}
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);

				// Get the file and save it somewhere
				String newFolder = uploadPath + now.format(formatter);
				File filefolder = new File(newFolder);
				if (!filefolder.exists()) {
					filefolder.mkdirs();
				}
				Path path = Paths.get(newFolder);
				Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
				if (newpath != null) {
					fileUploadResponse.setFileName(file.getOriginalFilename());
					fileUploadResponse.setStatus(Status.SUCCESS);
				}
				com.tcl.dias.oms.entity.entities.Attachment attachment = new com.tcl.dias.oms.entity.entities.Attachment();
				attachment.setCreatedDate(new Timestamp(new Date().getTime()));
				attachment.setName(file.getOriginalFilename());
				attachment.setStoragePathUrl(newFolder);
				attachment.setType(attachmentType);
				attachment.setCategory(referenceName);
				attachmentRepository.save(attachment);
				LOGGER.info("Attachment Saved with the attachment id {}", attachment.getId());
				fileUploadResponse.setAttachmentId(attachment.getId());

//				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
//				OmsAttachBean omsAttachBean = new OmsAttachBean();
//				omsAttachBean.setAttachmentId(attachment.getId());
//				omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
//				omsAttachBean.setReferenceName(referenceName);
//				omsAttachBeanList.add(omsAttachBean);
//				OmsAttachBean omsAttachBean2=new OmsAttachBean();
//				omsAttachBean2.setReferenceName(file.getOriginalFilename());
//
//				OmsListenerBean listenerBean = new OmsListenerBean();
//				listenerBean.setOmsAttachBean(omsAttachBeanList);
//				listenerBean.setOmsAttachmentBean(omsAttachBean2);
//				processOmsAttachmentSDD(listenerBean);

			}
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * @author mpalanis
	 *
	 * @param key
	 * @return
	 * @throws TclCommonException
	 */
	public String processSSDownloadFile(String key, Integer id) throws TclCommonException {
		Resource resource = null;
		String tempDownloadUrl = StringUtils.EMPTY;
		try {
			Integer quoteId = null;
			String category = "";
			String type = IzosdwanCommonConstants.SERVICE_SCHEDULE;
//			Boolean ispureByon=false;
			// decrypt key
			if (id == null && !key.isEmpty()) {
				String hashValue = EncryptionUtil.decrypt(key);
				quoteId = Integer.parseInt(hashValue);
				LOGGER.info("quoteId:{}", quoteId);
			} else {
				quoteId = id;
			}
			// check if pure byon
//			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
//					.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P, quoteId);
//			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
//					&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue() != null
//					&& "true".equalsIgnoreCase(quoteIzoSdwanAttributeValues.get(0).getAttributeValue())) {
//				ispureByon = true;
//			}
			// get distinct products
			if (quoteId != null) {
				Quote quoteDetails = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
				if (quoteDetails != null) {
					ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
					if (solutions != null) {
						List<String> sdwanSiteProducts = quoteIzosdwanSiteRepository
								.getDistinctSiteProducts(solutions.getId());
//						if(ispureByon) {
//							category = IzosdwanCommonConstants.SERVICE_SCHEDULE_IZOSDWAN;
//						}
						if (sdwanSiteProducts.contains(IzosdwanCommonConstants.IAS_CODE)) {
							category = getServiceVarientIas(solutions);
						} else {
							category = IzosdwanCommonConstants.SERVICE_SCHEDULE_IZOSDWAN_OTHERS;
						}
					}
				}
			}
			LOGGER.info("category:{}", category);
			List<com.tcl.dias.oms.entity.entities.Attachment> attachmentRepo = attachmentRepository
					.findByCategoryAndType(category, type);
			if (attachmentRepo == null || attachmentRepo.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			if (attachmentRepo != null) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentRepo.get(0).getName(),
							Long.parseLong(tempDownloadUrlExpiryWindow), attachmentRepo.get(0).getStoragePathUrl(),
							false);
//					resource = new ByteArrayResource(tempDownloadUrl.getBytes());
				} else {
					LOGGER.info("Path received :: {}", attachmentRepo.get(0).getStoragePathUrl());
					File[] files = new File(attachmentRepo.get(0).getStoragePathUrl()).listFiles();
					if (files == null) {
						return null;
					}

					String attachmentPath = null;
					for (File file : files) {
						if (file.isFile()) {
							attachmentPath = file.getAbsolutePath();
							LOGGER.info("File Abs path :: {}", attachmentPath);
						}
					}
					Path attachmentLocation = Paths.get(attachmentPath);
					resource = new UrlResource(attachmentLocation.toUri());
					if (resource.exists() || resource.isReadable()) {
						return tempDownloadUrl;
					}
				}
			}
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing download malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));

		}
		return tempDownloadUrl;
	}

	/**
	 * @author mpalanis
	 * @param solution
	 * @return
	 * @throws TclCommonException
	 */
	private String getServiceVarientIas(ProductSolution solution) throws TclCommonException {
		String category = StringUtils.EMPTY;
		Set<String> serviceVarient = new HashSet<>();
		List<QuoteIzosdwanSite> sdwanSiteDetails = quoteIzosdwanSiteRepository
				.findByProductSolutionAndIzosdwanSiteProduct(solution, IzosdwanCommonConstants.IAS_CODE);
		sdwanSiteDetails.forEach(site -> {
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(site.getId(),
							IllSitePropertiesConstants.SITE_PROPERTIES.toString(),
							QuoteConstants.IZOSDWAN_SITES.toString());
			if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
				Optional<QuoteProductComponentsAttributeValue> attValue = quoteProductComponentList.get(0)
						.getQuoteProductComponentsAttributeValues().stream()
						.filter(prodCompAttValue -> IzosdwanCommonConstants.SERVICE_VARIANT
								.equalsIgnoreCase(prodCompAttValue.getProductAttributeMaster().getName()))
						.findFirst();
				if (attValue.isPresent()) {
					serviceVarient.add(attValue.get().getAttributeValues());
				}
			}
		});
		serviceVarient.retainAll(IzosdwanCommonConstants.SERVICE_VARIENT);
		if (!serviceVarient.isEmpty()) {
			List<String> varient = new ArrayList<String>(serviceVarient);
			Collections.sort(varient);
			return varient.stream().map(var -> var.toString()).collect(Collectors.joining("_"));
		} else {
			return category;
		}
	}

	/**
	 * @author mpalanis
	 * @param customerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public List<ActiveQuoteAndOrder> getActiveQuoteAndOrderDetails(Integer customerId, Integer customerLeId)
			throws TclCommonException {
		List<ActiveQuoteAndOrder> response = new ArrayList<>();
		if (customerId != null && customerLeId != null) {
			ActiveQuoteAndOrder activeQuote = new ActiveQuoteAndOrder();
			activeQuote.setAttributeName("activeQuotes");
			ActiveQuoteAndOrder activeOrder = new ActiveQuoteAndOrder();
			activeOrder.setAttributeName("activeOrders");
			List<String> quotes = new ArrayList<>();
			List<String> orders = new ArrayList<>();
			// find active quotes
			if (getActive.equalsIgnoreCase("true")) {
				Customer customer = getCustomerId(customerId);
				LOGGER.info("customer Id:{}", customer.getId());
				if (customer != null) {
					List<Quote> quoteDetails = quoteRepository.findActiveQuotesByCustomerIdAndCustomerLeId(
							customer.getId(), customerLeId, IzosdwanCommonConstants.IZOSDWAN, 1);
					if (quoteDetails != null && !quoteDetails.isEmpty()) {
						quoteDetails.stream().forEach(quote -> {
							LOGGER.info("quoteId:{}", quote.getId());
							quotes.add(quote.getQuoteCode());
							// find active orders
							Optional<Order> orderDetails = quote.getOrders().stream()
									.filter(order -> order.getStatus().equals((byte) 1)).findFirst();
							if (orderDetails.isPresent()) {
								LOGGER.info("orderId:{}", orderDetails.get().getId());
								orders.add(orderDetails.get().getOrderCode());
							}
						});
					}
				}
			}
			activeQuote.setAttributeValues(quotes);
			response.add(activeQuote);
			activeOrder.setAttributeValues(orders);
			response.add(activeOrder);
		}
		return response;
	}

	private void supplierInfo(IzosdwanQuotePdfBean cofPdfRequest, Quote quote) throws TclCommonException {

		Optional<QuoteToLe> quoteLe = quote.getQuoteToLes().stream().findFirst();
		if (quoteLe.get().getErfCusSpLegalEntityId() != null) {
			LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
					String.valueOf(quoteLe.get().getErfCusSpLegalEntityId()));
			String add="";
			String country="India";
			if (StringUtils.isNotBlank(supplierResponse)) {
				SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
//				cofPdfRequest.setSupplierAddress(spDetails.getNoticeAddress());
				add=spDetails.getAddress();
				if(add!=null && !add.isEmpty()) {
					if(add.toLowerCase().contains(country.toLowerCase()))
					{
						cofPdfRequest.setIsIndia(true);
					}
				}
//				AddressDetail addressDetail = getAddressDetailBySupplierId(spDetails.getEntityName());
			}
		}
	}
	
	public void checkProcessUpdateProduct(Integer quoteLeId) {
		try {
			LOGGER.info("Triggering bundleOmsSfdcService for quoteLeId:{} ",quoteLeId);
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
			bundleOmsSfdcService.processUpdateProduct(quoteToLeOpt.get());
			LOGGER.info("Trigger update product sfdc");
		} catch (TclCommonException e) {
			LOGGER.info("Error in updating sfdc with pricing");
		}
	}
	
	
	/**
	 * 
	 * Get the price information of the quote
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public QuotePricingDetailsBean getPriceInformationForTheQuoteSfdcSdwan(Integer quoteId) throws TclCommonException {
		QuotePricingDetailsBean quotePricingDetailsBean = new QuotePricingDetailsBean();
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		
		LOGGER.info("Entering Price Info method ---: ");
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			if (quote != null) {
				String terms;
				String currency = "INR";
				List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
				if (quoteToLes.stream().findFirst().isPresent()) {
					QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get();
					terms = quoteToLe.getTermInMonths();
					currency = quoteToLe.getCurrencyCode();
					Integer termsInMonths = Integer.parseInt(terms.substring(0, 2));
					// As of now getting price only for SDWAN solution
					ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
					if (productSolution != null) {
						SolutionPricingDetailsBean solutionPricingDetailsBean = new SolutionPricingDetailsBean();
						List<ProductPricingDetailsBean> productPricingDetailsBeans = new ArrayList<>();
						List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
								.findByProductSolution(productSolution);
						if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
							getProductLevelPricingDetailsSfdcSdwan(CommonConstants.IAS, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,true);
							getProductLevelPricingDetailsSfdcSdwan(CommonConstants.IAS, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,false);
							getProductLevelPricingDetailsSfdcSdwan(CommonConstants.GVPN, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,true);
							getProductLevelPricingDetailsSfdcSdwan(CommonConstants.GVPN, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,false);
							getProductLevelPricingDetailsSfdcSdwan(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT,
									quoteIzosdwanSites, productPricingDetailsBeans, termsInMonths,true);
							getProductLevelPricingDetailsSfdcSdwan(IzosdwanCommonConstants.BYON_MPLS_PRODUCT,
									quoteIzosdwanSites, productPricingDetailsBeans, termsInMonths,true);
							getProductLevelPricingDetailsSfdcSdwan(IzosdwanCommonConstants.DIA_PRODUCT, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,true);
							getProductLevelPricingDetailsSfdcSdwan(IzosdwanCommonConstants.DIA_PRODUCT, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,false);
							getProductLevelPricingDetailsSfdcSdwan(IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,true);
							getProductLevelPricingDetailsSfdcSdwan(IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT, quoteIzosdwanSites,
									productPricingDetailsBeans, termsInMonths,false);
							
							getIzosdwanPricingDetails(quoteIzosdwanSites, productPricingDetailsBeans, quote,
									termsInMonths);
							// getCgwPricingDetails(quote, quoteToLe, productPricingDetailsBeans);
							// mockPriceForSdwan(productPricingDetailsBeans);
						}
						// Get Mock Price For Vproxy
						List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanValues = quoteIzoSdwanAttributeValuesRepository
								.findByQuote(quote);
						if (checkVproxyExistOrNot(quoteIzoSdwanValues)) {
							getVproxyPricingDetails(productPricingDetailsBeans, quoteId, termsInMonths);

						}
						solutionPricingDetailsBean.setArc(new BigDecimal(0D));
						solutionPricingDetailsBean.setMrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setNrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setTcv(new BigDecimal(0D));
						solutionPricingDetailsBean.setTcvMrc(new BigDecimal(0D));
						solutionPricingDetailsBean.setProductPricingDetailsBeans(productPricingDetailsBeans);
						calculateSolutionLevelPrice(solutionPricingDetailsBean);
						quotePricingDetailsBean.setIzosdwan(solutionPricingDetailsBean);
					}
					quotePricingDetailsBean.setArc(new BigDecimal(0D));
					quotePricingDetailsBean.setCurrency(currency);
					quotePricingDetailsBean.setMrc(new BigDecimal(0D));
					quotePricingDetailsBean.setNrc(new BigDecimal(0D));
					quotePricingDetailsBean.setTcv(new BigDecimal(0D));
					quotePricingDetailsBean.setTcvMrc(new BigDecimal(0D));
					quotePricingDetailsBean.setTermsInMonths(terms);
					constructQuoteLevelPrice(quotePricingDetailsBean.getIzosdwan(), quotePricingDetailsBean);
					// bundleOmsSfdcService.processUpdateProduct(quoteToLe,
					// quotePricingDetailsBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured on deriving the price {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quotePricingDetailsBean;
	}
}