package com.tcl.dias.customer.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.RIGHT_SLASH;
import static com.tcl.dias.common.constants.CommonConstants.TEAMSDR;
import static com.tcl.dias.common.constants.CommonConstants.VOICE;
import static com.tcl.dias.customer.constants.BillingAttributeConstants.CUSTOMER_CONTRACTING_ENTITY;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.IS_ACCOUNT_VERIFIED;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.UNVERIFIED;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.VERIFIED;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.YES;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.ACANS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.ACLNS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.ACDTFS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.ACLNS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.DEDICATED;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.DOMESTIC_VOICE;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.INDIA;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.MPLS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.NNI;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.PSTN;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.PUBLIC_IP;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.DEDICATED;
import static com.tcl.dias.customer.constants.SpConstants.ACCOUNT_RTM;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.CustomerLeCountryCurrencyBean;
import com.tcl.dias.customer.constants.BillingAttributeConstants;
import com.tcl.dias.customer.entity.repository.CustomerLegalEntityCompanyCodeRepository;
import com.tcl.dias.common.beans.*;
import com.tcl.dias.customer.entity.entities.*;
import com.tcl.dias.customer.entity.repository.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.BillingContactInfo;
import com.tcl.dias.common.beans.ClassificationBean;
import com.tcl.dias.common.beans.CustomerAttachmentBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerCodeBean;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeAccountManagerDetails;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.CustomerLeCountryCurrencyBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeListBean;
import com.tcl.dias.common.beans.CustomerLeListListBean;
import com.tcl.dias.common.beans.CustomerLeVO;
import com.tcl.dias.common.beans.DataCenterBean;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.LeStateGstInfoBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.LegalEntityBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MDMOmsRequestBean;
import com.tcl.dias.common.beans.MDMOmsResponseBean;
import com.tcl.dias.common.beans.MSABean;
import com.tcl.dias.common.beans.MSTAddressDetails;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsLeAttributeBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.ServiceProviderLegalBean;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.GscAttachmentTypeConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.customexception.TCLException;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentListener;
import com.tcl.dias.common.gsc.beans.GscOutboundAttachmentBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.bean.AttachmentBean;
import com.tcl.dias.customer.bean.BillingAddress;
import com.tcl.dias.customer.bean.CustomerLeAttributeBean;
import com.tcl.dias.customer.bean.CustomerLeBillingRequestBean;
import com.tcl.dias.customer.bean.CustomerLeContactBean;
import com.tcl.dias.customer.bean.CustomerLeContactDetailsBean;
import com.tcl.dias.customer.bean.CustomerLegalEntityBean;
import com.tcl.dias.customer.bean.CustomerLegalEntityRequestBean;
import com.tcl.dias.customer.bean.GstnInfo;
import com.tcl.dias.customer.bean.IzosdwanSupplierBean;
import com.tcl.dias.customer.bean.IzosdwanSupplierResponseBean;
import com.tcl.dias.customer.bean.IpcCrossBorderBean;
import com.tcl.dias.customer.bean.LeStateGstBean;
import com.tcl.dias.customer.bean.ServiceResponse;
import com.tcl.dias.customer.bean.SiteCountryBean;
import com.tcl.dias.customer.constants.DocumentConstant;
import com.tcl.dias.customer.constants.ExceptionConstants;
import com.tcl.dias.customer.constants.PDFConstants;
import com.tcl.dias.customer.constants.PartnerCustomerConstants;
import com.tcl.dias.customer.constants.ServiceSpecificConstant;
import com.tcl.dias.customer.constants.SpConstants;
import com.tcl.dias.customer.constants.TriggerEmailConstant;
import com.tcl.dias.customer.dto.AttachmentDto;
import com.tcl.dias.customer.dto.AttributesDto;
import com.tcl.dias.customer.dto.CustomerConatctInfoResponseDto;
import com.tcl.dias.customer.dto.CustomerContractingAddressInfo;
import com.tcl.dias.customer.dto.CustomerContractingAddressResponseDto;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.dto.CustomerLeBillingInfoDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityProductResponseDto;
import com.tcl.dias.customer.dto.EntityAddressLocationID;
import com.tcl.dias.customer.dto.ServiceProviderLegalEntityDto;
import com.tcl.dias.customer.dto.SupplierContractingInfo;
import com.tcl.dias.customer.dto.TriggerEmailResponse;
import com.tcl.dias.customer.entity.entities.AccountManager;
import com.tcl.dias.customer.entity.entities.Attachment;
import com.tcl.dias.customer.entity.entities.CurrencyMaster;
import com.tcl.dias.customer.entity.entities.Customer;
import com.tcl.dias.customer.entity.entities.CustomerAttachment;
import com.tcl.dias.customer.entity.entities.CustomerAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLeBillingInfo;
import com.tcl.dias.customer.entity.entities.CustomerLeContact;
import com.tcl.dias.customer.entity.entities.CustomerLeCountry;
import com.tcl.dias.customer.entity.entities.CustomerLeCurrency;
import com.tcl.dias.customer.entity.entities.CustomerLeSecsAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLegalDataCenters;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.LeCcaAddress;
import com.tcl.dias.customer.entity.entities.LeStateGst;
import com.tcl.dias.customer.entity.entities.LegalEntitySapCode;
import com.tcl.dias.customer.entity.entities.MstCountry;
import com.tcl.dias.customer.entity.entities.MstCustomerSegment;
import com.tcl.dias.customer.entity.entities.MstCustomerSpAttribute;
import com.tcl.dias.customer.entity.entities.MstDopMarker;
import com.tcl.dias.customer.entity.entities.MstDopMatrix;
import com.tcl.dias.customer.entity.entities.MstLeAttribute;
import com.tcl.dias.customer.entity.entities.PartnerLeCountry;
import com.tcl.dias.customer.entity.entities.PartnerLegalEntity;
import com.tcl.dias.customer.entity.entities.ServiceProvider;
import com.tcl.dias.customer.entity.entities.ServiceProviderLegalEntity;
import com.tcl.dias.customer.entity.entities.SpLeAttributeValue;
import com.tcl.dias.customer.entity.entities.SpLeCountry;
import com.tcl.dias.customer.entity.entities.SpLeCurrency;
import com.tcl.dias.customer.entity.repository.AccountManagerRepository;
import com.tcl.dias.customer.entity.repository.AttachmentRepository;
import com.tcl.dias.customer.entity.repository.CurrencyMasterRepository;
import com.tcl.dias.customer.entity.repository.CustomerAttatchmentsRepository;
import com.tcl.dias.customer.entity.repository.CustomerAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeBillingInfoRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeContactRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeCountryRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeDataCentersRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeSecsAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.CustomerLegalEntityCompanyCodeRepository;
import com.tcl.dias.customer.entity.repository.CustomerLegalEntityRepository;
import com.tcl.dias.customer.entity.repository.CustomerRepository;
import com.tcl.dias.customer.entity.repository.LeCcaAddressRepository;
import com.tcl.dias.customer.entity.repository.LeStateGstRepository;
import com.tcl.dias.customer.entity.repository.LegalEntitySapCodeRepository;
import com.tcl.dias.customer.entity.repository.MstCountriesCurrencyMasterRepository;
import com.tcl.dias.customer.entity.repository.MstCountryRepository;
import com.tcl.dias.customer.entity.repository.MstCustomerSegmentRepository;
import com.tcl.dias.customer.entity.repository.MstCustomerSpAttributeRepository;
import com.tcl.dias.customer.entity.repository.MstDopMarkerRepository;
import com.tcl.dias.customer.entity.repository.MstDopMatrixRepository;
import com.tcl.dias.customer.entity.repository.MstLeAttributeRepository;
import com.tcl.dias.customer.entity.repository.PartnerLeAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.PartnerLegalEntityRepository;
import com.tcl.dias.customer.entity.repository.ServiceProviderLegalEntityCountryRepository;
import com.tcl.dias.customer.entity.repository.ServiceProviderLegalEntityCurrencyRepository;
import com.tcl.dias.customer.entity.repository.ServiceProviderLegalEntityRepository;
import com.tcl.dias.customer.entity.repository.ServiceProviderRepository;
import com.tcl.dias.customer.entity.repository.SpLeAttributeValueRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import com.tcl.dias.customer.bean.IpcCrossBorderBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.tcl.dias.common.constants.CommonConstants.RIGHT_SLASH;
import static com.tcl.dias.customer.constants.BillingAttributeConstants.CUSTOMER_CONTRACTING_ENTITY;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.IS_ACCOUNT_VERIFIED;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.UNVERIFIED;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.VERIFIED;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.YES;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.ACANS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.ACDTFS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.ACLNS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.DEDICATED;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.DOMESTIC_VOICE;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.INDIA;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.MPLS;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.NNI;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.PSTN;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.PUBLIC_IP;
import static com.tcl.dias.customer.constants.SpConstants.ACCOUNT_RTM;
import static java.util.stream.Collectors.joining;


/**
 * This file contains the Billing details AAnd Contact information details
 *
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class CustomerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	private static final String ATTACHMENT = "Attachment";

	private static final String ATTACHMENT_TYPE = "Others";

	private static final String MQ_ERROR = "Error in getting document";

	private static final String MQ_OMS_ERROR = "Error in processing oms attachment";
	public static final String GSIP_COUNTRY_FILES = "GSIP-COUNTRY-FILES";
	public static final String PRIMARY = "PRIMARY";
	public static final String SECONDARY = "SECONDARY";
	public static final String TERTIARY = "TERTIARY";

	public static final Map<String, String> COUNTRY_MAP = new HashMap<String, String>() {{
		put("Sri Lanka", "APAC");
		put("Australia", "APAC");
		put("China", "APAC");
		put("India", "APAC");
		put("Hong Kong", "APAC");
		put("Japan", "APAC");
		put("United Arab Emirates", "APAC");
		put("Malaysia", "APAC");
		put("New Zealand", "APAC");
		put("Singapore", "APAC");
		put("South Korea", "APAC");
		put("Thailand", "APAC");
		put("Taiwan", "APAC");
		put("Russia", "APAC");
		put("Saudi Arabia", "APAC");
		put("Netherlands", "Europe");
		put("Belgium", "Europe");
		put("France", "Europe");
		put("Germany", "Europe");
		put("Hungary", "Europe");
		put("Ireland", "Europe");
		put("Italy", "Europe");
		put("Portugal", "Europe");
		put("Poland", "Europe");
		put("Sweden", "Europe");
		put("Spain", "Europe");
		put("Switzerland", "Europe");
		put("United Kingdom", "Europe");
		put("Norway", "Europe");
		put("United States of America", "North America");
		put("Brazil", "North America");
		put("Bermuda", "North America");
		put("Canada", "North America");
		put("Guam", "North America");
		put("Puerto Rico", "North America");
	}};

	@Autowired
	CustomerLegalEntityRepository customerLegalEntityRepository;
	
	@Autowired
	PartnerLegalEntityRepository partnerLegalEntityRepository;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${rabbitmq.mstaddress.detail}")
	String mstAddressQueue;

	@Value("${notification.mail.template}")
	String multipleProfileSelectionTemplateId;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Value("${oms.attachment.queue}")
	String omsAttachmentQueue;

	@Value("${oms.gsc.attachment.queue}")
	String omsGscAttachmentQueue;

	@Value("${oms.quote.attachment.queue}")
	String omsQuoteAttachmentQueue;

	@Value("${rabbitmq.product.dc.queue}")
	String productDataCenterqueue;

	@Autowired
	MQUtils mqUtils;

	@Value("${document.upload}")
	String uploadPath;

	@Value("${rabbitmq.location.details.feasibility}")
	protected String locationDetailQueue;

	@Autowired
	ServiceProviderRepository serviceProviderRepository;

	@Autowired
	MstLeAttributeRepository mstLeAttributeRepository;

	@Autowired
	SpLeAttributeValueRepository spLeAttributeValueRepository;

	@Autowired
	CustomerLeAttributeValueRepository customerLeAttributeValueRepository;

	@Autowired
	CustomerLeBillingInfoRepository customerLeBillingInfoRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerLeAttributeValueRepository customerLeAttributeRepository;

	@Autowired
	ServiceProviderLegalEntityRepository serviceProviderLegalEntityRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	MstCountryRepository mstCountryRepository;

	@Autowired
	CustomerAttributeValueRepository customerAttributeValueRepository;

	@Autowired
	ServiceProviderLegalEntityCountryRepository spleCountryRepository;

	@Autowired
	ServiceProviderLegalEntityCurrencyRepository spLeCurrencyRepository;

	@Autowired
	MstCustomerSpAttributeRepository mstCustomerSpAttributeRepository;

	@Autowired
	LeStateGstRepository leStateGstRepository;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	MstCustomerSegmentRepository mstCustomerSegmentRepository;

	@Autowired
	MstDopMatrixRepository mstDopMatrixRepository;

	@Autowired
	MstDopMarkerRepository mstDopMarkerRepository;

	@Autowired
	LeCcaAddressRepository leCcaAddressRepository;

	@Autowired
	PartnerLeAttributeValueRepository partnerLeAttributeValueRepository;


	private static Row.MissingCellPolicy xRow;

	@Value("${attribute.customer.data.values}")
	private String cusAttributeValues;

	@Value("${attribute.supplier.data.values}")
	private String splAttributeValues;

	@Value("${billing.details}")
	private String billingMethod;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	private CustomerLeContactRepository customerLeContactRepository;

	@Autowired
	CustomerLeDataCentersRepository customerLeDataCentersRepository;

	@Value("${ias.ss.location}")
	String iasSsPath;

	@Value("${npl.ss.location}")
	String nplSsPath;

	@Value("${izopc.ss.location}")
	String izopcSsPath;

	@Value("${gvpn.ss.location}")
	String gvpnSsPath;

	@Value("${gsc.ss.location}")
	String gscSsPath;

	@Value("${ias.msa.location}")
	String iasMSAPath;

	@Value("${npl.msa.location}")
	String nplMSAPath;

	@Value("${izopc.msa.location}")
	String izopcMSAPath;

	@Value("${gvpn.msa.location}")
	String gvpnMSAPath;

	@Value("${gsc.msa.location}")
	String gscMSAPath;

	@Value("${country.specific.file.queue}")
	private String DocumentIDQueue;

	@Value("${cmd.bill.queue}")
	String cmdBillUpdateQueue;

	@Autowired
	CustomerLeCountryRepository customerLeCountryRepository;

	@Autowired
	CurrencyMasterRepository currencyMasterRepository;

	@Value("${oms.billing.attribute.update}")
	String updateOmsBillingAttribute;

	@Autowired
	MstCountriesCurrencyMasterRepository mstCountriesCurrencyMasterRepository;

	@Autowired
	LegalEntitySapCodeRepository legalEntitySapCodeRepository;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${swift.api.container}")
	String swiftApiContainer;

	@Value("${rabbitmq.site.gst.queue}")
	String siteGstQueue;

	public static final String DATEFORMAT = "yyyyMMddHHmmss";

	private static final String COUNTRY_NAME_IS_REQUIRED = "Country name is required";
	private static final String CURRENCY_NAME_IS_REQUIRED = "Currency name is required";

	@Value("${rabbitmq.cust.billing.add.address}")
	String customerbillingAddAddress;

	@Value("${rabbitmq.cust.billing.get.address}")
	String customerbillingGetAddress;
	
	@Value("${bcr.enterprise.provider.name}")
	String enterpriseProvider;

	@Value("${bcr.service.provider.name}")
	String serviceProvider;
	
	@Value("${rabbitmq.o2c.ipcquote.crossbordertax}")
	private String crossBorderTaxQueue;
	
	@Autowired
	AccountManagerRepository accountManagerRepository;

	@Value("${rabbitmq.custleid.tostate.queue}")
	private String custLeIdToStateQueue;
	
	@Value("${rabbitmq.oms.izosdwan.quote.countries}")
	String omsCountriesQueue;
	
	@Value("${oms.attachment.queue.sdd}")
	String omsAttachmentQueueSDD;
	
	@Value("${gde.bod.ss.location}")
	String gdeBodSsLocation;
	
	@Value("${gde.bod.msa.location}")
	String gdeBodMSAPath;
	
	@Autowired
	CustomerAttatchmentsRepository customerAttatchmentsRepository;

	@Autowired
	CustomerLeSecsAttributeValueRepository customerLeSecsAttributeValueRepository;

	@Autowired
	CustomerLegalEntityCompanyCodeRepository customerLegalEntityCompanyCodeRepository;

	@Autowired
	CustomerTeamMembersRepository customerTeamMembersRepository;

	@Autowired
	PartnerLegalEntityCompanyCodeRepository partnerLegalEntityCompanyCodeRepository;



	/**
	 * getBillingDetailsById
	 * <p>
	 * Fetches Billing Details Based on custLegalId
	 *
	 * @param custLegalId
	 * @param productName
	 * @return List<AttributesDto>
	 * @throws TclCommonException
	 */

	public List<AttributesDto> getBillingDetailsById(Integer custLegalId, String productName)
			throws TclCommonException {
		List<AttributesDto> attributesDtoList = new ArrayList<>();
		List<CustomerLeAttributeValue> customerLeAttributeValueList = new ArrayList<>();
		try {

			if (Objects.isNull(custLegalId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			if (Objects.isNull(productName)) {
				customerLeAttributeValueList = customerLeAttributeValueRepository
						.findByCustomerLegalEntity_Id(custLegalId);
			} else {
				customerLeAttributeValueList = customerLeAttributeValueRepository
						.findByCustomerLeIdAndProductName(custLegalId, productName);
			}
			if (customerLeAttributeValueList.isEmpty())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			attributesDtoList = customerLeAttributeValueList.stream()
					.filter(customerLeAttribute -> Arrays.asList(billingMethod.split(","))
							.contains(customerLeAttribute.getMstLeAttribute().getType()))
					.map(AttributesDto::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attributesDtoList;
	}

	public List<BillingContact> getBillingContact(Integer custLegalId) throws TclCommonException {
		List<BillingContact> billingContacts = new ArrayList<>();
		try {

			if (Objects.isNull(custLegalId))
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LE_VALIDATE_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			List<CustomerLeBillingInfo> leContacts = customerLeBillingInfoRepository
					.findByCustomerLegalEntity_IdAndIsactive(custLegalId, "Yes");

			billingContacts = leContacts.stream().map(e -> mapEntityToLeContact(e, custLegalId))
					.filter(Utils.distinctByKeys(BillingContact::getBillAddr, BillingContact::getFname,
							BillingContact::getLname, BillingContact::getEmailId))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_BILLING_CONTACT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return billingContacts;
	}

	public List<String> getBillingAccounts(List<Integer> custLegalId) throws TclCommonException {
		List<String> ls = new LinkedList<String>();
		try {

			if (Objects.isNull(custLegalId) || custLegalId.isEmpty())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			List<Map<String, Object>> billingAccounts = customerLeBillingInfoRepository
					.findBillingAccountForCustomerLegalEntity_IdInAndIsactive(custLegalId, "Yes");

			for (Map<String, Object> map : billingAccounts) {
				ls.add((String) map.get("bill_acc_no"));
			}
			return ls;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public List<BillingContactInfo> getBillingContactInfo() throws TclCommonException {
		List<BillingContactInfo> billingContacts = new ArrayList<>();
		try {

			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			Set<Integer> customerLeIds = new HashSet<>();
			for (CustomerDetail customerDetail : customerDetails) {
				customerLeIds.add(customerDetail.getCustomerLeId());
			}

			List<Map<String, Object>> leContacts = customerLeBillingInfoRepository
					.findByCustomerLegalEntity_IdInAndIsactive((new ArrayList<>(customerLeIds)), "Yes");
			for (Map<String, Object> map : leContacts) {
				BillingContactInfo billingContact = new BillingContactInfo();
				billingContact
						.setCustomerLeId(map.get("customerLeId") != null ? (Integer) map.get("customerLeId") : null);
				billingContact.setEmailId(map.get("emailId") != null ? (String) map.get("emailId") : null);
				billingContact.setFirstName(map.get("firstName") != null ? (String) map.get("firstName") : null);
				billingContact.setLastName(map.get("lastName") != null ? (String) map.get("lastName") : null);
				billingContact
						.setMobileNumber(map.get("mobileNumber") != null ? (String) map.get("mobileNumber") : null);
				billingContact.setPhoneNumber(map.get("phoneNumber") != null ? (String) map.get("phoneNumber") : null);
				if (!billingContacts.contains(billingContact)) {
					billingContacts.add(billingContact);

				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return billingContacts;
	}

	/**
	 * used to get customer billing information using billing id
	 * 
	 * @param billingId
	 * @return
	 * @throws TclCommonException
	 */
	public BillingContact getBillingContactByBillingId(Integer billingId) throws TclCommonException {
		BillingContact billingContacts = new BillingContact();
		try {

			if (Objects.isNull(billingId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			Optional<CustomerLeBillingInfo> leContacts = customerLeBillingInfoRepository.findById(billingId);

			/*
			 * billingContacts = (BillingContact) leContacts.map(e ->
			 * mapEntityToLeContact(e, billingId))
			 * .filter(Utils.distinctByKeys(BillingContact::getBillAddr,
			 * BillingContact::getFname, BillingContact::getLname,
			 * BillingContact::getEmailId, BillingContact::getMobileNumber));
			 */

			billingContacts = leContacts.map(e -> mapEntityToLeContact(e, billingId)).get();

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return billingContacts;
	}

	public BillingContact getBillingContactById(Integer contactId) throws TclCommonException {
		try {

			if (Objects.isNull(contactId))
				throw new TclCommonException(ExceptionConstants.BILLING_CONTACT_VALIDATION_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			Optional<CustomerLeBillingInfo> leContacts = customerLeBillingInfoRepository.findById(contactId);
			if (leContacts.isPresent()) {
				return mapEntityToLeContact(leContacts.get(), leContacts.get().getCustomerLegalEntity().getId());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_BILLING_CONTACT_EXCEPTION, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}
	

	/**
	 * mapEntityToLeContact
	 */
	private BillingContact mapEntityToLeContact(CustomerLeBillingInfo customerLeBillingInfo, Integer leId) {
		BillingContact billingContact = new BillingContact();
		if (customerLeBillingInfo.getAddressSeq() != null)
			billingContact.setAddressSeq(customerLeBillingInfo.getAddressSeq());
		if (!Objects.isNull(customerLeBillingInfo.getBillAccNo()))
			billingContact.setBillAccNo(customerLeBillingInfo.getBillAccNo());
		if (!Objects.isNull(customerLeBillingInfo.getBillAddr()))
			billingContact.setBillAddr(customerLeBillingInfo.getBillAddr());
		if (!Objects.isNull(customerLeBillingInfo.getBillContactSeq()))
			billingContact.setBillContactSeq(customerLeBillingInfo.getBillContactSeq());
		if (customerLeBillingInfo.getId() != null)
			billingContact.setBillingInfoid(customerLeBillingInfo.getId());
		if (customerLeBillingInfo.getContactType() != null)
			billingContact.setContactType(customerLeBillingInfo.getContactType());
		if (!Objects.isNull(customerLeBillingInfo.getCountry()))
			billingContact.setCountry(customerLeBillingInfo.getCountry());
		if (customerLeBillingInfo.getCustomerId() != null)
			billingContact.setCustomerId(customerLeBillingInfo.getCustomerId());
		if (leId != null) {
			billingContact.setCustomerLegalEntityId(leId);
			Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(leId);
			if (customerLegalEntity.isPresent()) {
				Customer customer = customerLegalEntity.get().getCustomer();
				if (customer.getCustomerCode() == null) {
					customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
					customerRepository.save(customer);
				}
				if (customerLegalEntity.get().getCustomerLeCode() == null) {
					customerLegalEntity.get()
							.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
					customerLegalEntityRepository.save(customerLegalEntity.get());
				}
				billingContact.setCustomerLeCode(customerLegalEntity.get().getCustomerLeCode());
				billingContact.setCustomerCode(customer.getCustomerCode());
			}
		}
		if (!Objects.isNull(customerLeBillingInfo.getEmailId()))
			billingContact.setEmailId(customerLeBillingInfo.getEmailId());
		if (!Objects.isNull(customerLeBillingInfo.getFname()))
			billingContact.setFname(customerLeBillingInfo.getFname());
		if (!Objects.isNull(customerLeBillingInfo.getLname()))
			billingContact.setLname(customerLeBillingInfo.getLname());
		if (!Objects.isNull(customerLeBillingInfo.getMobileNumber()))
			billingContact.setMobileNumber(customerLeBillingInfo.getMobileNumber());
		if (!Objects.isNull(customerLeBillingInfo.getPhoneNumber()))
			billingContact.setPhoneNumber(customerLeBillingInfo.getPhoneNumber());
		if (!Objects.isNull(customerLeBillingInfo.getTitle()))
			billingContact.setTitle(customerLeBillingInfo.getTitle());
		if (!Objects.isNull(customerLeBillingInfo.getErfloclocationid()))
			billingContact.setErfLocationId(customerLeBillingInfo.getErfloclocationid());
		return billingContact;

	}

	/**
	 * findCustomerEntityByCustomerId - This method is used for fetching
	 * customerLegal entity list from the customer ID
	 *
	 * @param id
	 * @return List<CustomerLegalEntityDto>
	 * @throws TclCommonException
	 */

	public List<CustomerLegalEntityDto> findCustomerEntityByCustomerId(Integer id,String isPartner) throws TclCommonException {

		if (Objects.isNull(id))
			throw new TclCommonException(ExceptionConstants.CUSTOMER_VALIDATION_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		List<CustomerLegalEntity> customerLegalEntities = null;
		List<PartnerLegalEntity> partnerLegalEntities = null;
		List<CustomerLegalEntityDto> customerLegalEntityDtos = new ArrayList<>();
		try {
			if(userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())&&isPartner==null){
				partnerLegalEntities = partnerLegalEntityRepository.findByPartnerId(id);
				getPartnerLegalEntitesList(partnerLegalEntities, customerLegalEntityDtos);
			} else if(userInfoUtils.getUserRoles().contains("OPT_WHOLESALE_CUSTOMER_PORTAL")) {
				Set<Integer> customerLeId = userInfoUtils.getCustomerDetails().stream().map(CustomerDetail::getCustomerLeId).collect(Collectors.toSet());
				LOGGER.info("Wholesale Customer LE IDs :: {}", customerLeId);
				customerLegalEntities = customerLegalEntityRepository.findAllById(customerLeId);
				getCustomerLegalEntitesList(customerLegalEntities, customerLegalEntityDtos);
			}
			else {
				customerLegalEntities = customerLegalEntityRepository.findByCustomerId(id);
				getCustomerLegalEntitesList(customerLegalEntities, customerLegalEntityDtos);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in customerLeDetails  {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_CUSTOMER_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerLegalEntityDtos;
	}


	public List<CustomerLegalEntityDto> findCustomerEntityByCustomerIdForAll(Integer id) throws TclCommonException {

		if (Objects.isNull(id))
			throw new TclCommonException(ExceptionConstants.CUSTOMER_VALIDATION_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		List<CustomerLegalEntity> customerLegalEntities = null;
		List<PartnerLegalEntity> partnerLegalEntities = null;
		List<CustomerLegalEntityDto> customerLegalEntityDtos = new ArrayList<>();
		try {
				customerLegalEntities = customerLegalEntityRepository.findByCustomerId(id);
				getCustomerLegalEntitesList(customerLegalEntities, customerLegalEntityDtos);
		} catch (Exception e) {
			LOGGER.warn("Error in customerLeDetails  {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_CUSTOMER_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerLegalEntityDtos;
	}

	public List<CustomerLegalEntityDto> findCustomerEntityByCustomerLeId(List<Integer> ids) throws TclCommonException {
		if (Objects.isNull(ids))
			throw new TclCommonException(ExceptionConstants.CUSTOMER_VALIDATION_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		List<CustomerLegalEntity> customerLegalEntities = null;
		List<CustomerLegalEntityDto> customerLegalEntityDtos = new ArrayList<>();
		try {
			customerLegalEntities =customerLegalEntityRepository.findAllByIdIn(ids);
			getCustomerLegalEntitesListForMacd(customerLegalEntities, customerLegalEntityDtos);
		}
		catch (Exception e) {
			LOGGER.warn("Error in customerLeDetails  {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_CUSTOMER_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerLegalEntityDtos;
	}


	private List<CustomerLegalEntityDto> getCustomerLegalEntites(List<CustomerLegalEntity> customerLegalEntities, List<CustomerLegalEntityDto> customerLegalEntityDtos,
																 String productName) {
		if (CollectionUtils.isEmpty(customerLegalEntities)) {
			LOGGER.error("Customer Legal Entity is no available");
		}
		String userType = userInfoUtils.getUserType();
		if (!(userType != null && userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString()))) {
			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			Set<Integer> customersSet = new HashSet<>();
			Set<Integer> customerLeIds = new HashSet<>();
			getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
			customerLegalEntities = customerLegalEntities.stream().filter(cle -> customerLeIds.contains(cle.getId()))
					.collect(Collectors.toList());
		}
		for (CustomerLegalEntity customerLegalEntity : customerLegalEntities) {
			CustomerLegalEntityDto customerLegalEntityDto = new CustomerLegalEntityDto(customerLegalEntity);
			List<LeStateGst> leStateGsts = leStateGstRepository.findByCustomerLegalEntity(customerLegalEntity);
			List<BillingAddress> billingAddresses = new ArrayList<>();
			for (LeStateGst leStateGst : leStateGsts) {
				BillingAddress billingAddress = new BillingAddress();
				billingAddress.setLeStateGstId(leStateGst.getId());
				billingAddress.setAddress(leStateGst.getAddress());
				billingAddress.setGstn(leStateGst.getGstNo());
				billingAddresses.add(billingAddress);
			}
//			String currencyShortName = customerLegalEntity.getCustomerLeCurrencies().stream().findFirst().get()
//					.getCurrencyMaster().getShortName();
			customerLegalEntityDto.setCurrency(getCurrencyForCustomerLegalEntity(customerLegalEntity));
			customerLegalEntityDto.setBillingAddresses(billingAddresses);
			if(Objects.nonNull(productName) && TEAMSDR.equals(productName)){
				customerLegalEntityDto.setPoMandatory(getPoMandatoryForCustomerLeId(customerLegalEntity, productName));
			}
			customerLegalEntityDtos.add(customerLegalEntityDto);
		}

		return customerLegalEntityDtos;
	}

	/**
	 * getPoMandatoryForCustomerLeId
	 * @param customerLegalEntity
	 * @param productName
	 * @return
	 */
	private String getPoMandatoryForCustomerLeId(CustomerLegalEntity customerLegalEntity, String productName) {
		String poMandatory = "No";
		Optional<MstLeAttribute> poRequired = mstLeAttributeRepository.findByName("PO_REQUIRED");
		if (poRequired.isPresent()) {
			List<CustomerLeAttributeValue> leAttributeValueList = (List<CustomerLeAttributeValue>) customerLeAttributeRepository
					.findByCustomerLeIdAndMstLeAttributesIdAndProductName(customerLegalEntity.getId(), poRequired.get().getId(), productName);
			poMandatory = leAttributeValueList.stream().findAny().get().getAttributeValues();
		}
		return poMandatory;
	}

	private String getCurrencyForCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
		return customerLegalEntity.getCustomerLeCurrencies().stream().findFirst().get()
				.getCurrencyMaster().getShortName();
	}

	private String getCurrencyForPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
		Integer currency_Id=partnerLegalEntity.getPartnerLeCurrencies().stream().findFirst().get().getCurrencyId();
		Optional<CurrencyMaster> currencyMaster=currencyMasterRepository.findById(currency_Id);
		if(currencyMaster.isPresent())
		{
			return currencyMaster.get().getShortName();
		}
		return "";
	}

	private List<String> getCountryNameById(Integer customerId){
		Optional<MstCountry> mstCountry=mstCountryRepository.findById(customerId);
		List<String> countryList=new ArrayList<>();
		if(mstCountry.isPresent()){
			countryList.add(mstCountry.get().getName());
			return countryList;
		}
		return  countryList;
	}

	private void getMapperCustomerDetails(List<CustomerDetail> customerDetails, Set<Integer> customersSet,
			Set<Integer> customerLeIds) {
		for (CustomerDetail customerDetail : customerDetails) {
			customersSet.add(customerDetail.getErfCustomerId());
			customerLeIds.add(customerDetail.getCustomerLeId());
		}
	}

	private void getMapperPartnerDetails(List<PartnerDetail> PartnerDetailPartnerDetail, Set<Integer> customersSet,
										  Set<Integer> customerLeIds) {
		for (PartnerDetail partnerDetail : PartnerDetailPartnerDetail) {
			customersSet.add(partnerDetail.getErfPartnerId());
			customerLeIds.add(partnerDetail.getPartnerLeId());
		}
	}

	/**
	 * getGstDetails
	 *
	 * @param customerLeId
	 * @param leStateGstId
	 * @return
	 */
	public BillingAddress getGstDetails(Integer customerLeId, Integer leStateGstId) throws TclCommonException {
		BillingAddress billingAddress = null;
		try {

			Optional<LeStateGst> optionalLeState = leStateGstRepository.findById(leStateGstId);
			if (optionalLeState.isPresent()) {
				LeStateGst leStateGst = optionalLeState.get();
				billingAddress = new BillingAddress();
				billingAddress.setAddress(leStateGst.getAddress());
				billingAddress.setGstn(leStateGst.getGstNo());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return billingAddress;
	}

	/**
	 * getEmailIdAndTriggerEmail- This method is to fetch the customer Id and
	 * trigger an email to them when multiple profile is selected
	 *
	 * @param none
	 * @return TriggerEmailResponse
	 * @throws TclCommonException
	 */

	public TriggerEmailResponse getEmailIdAndTriggerEmail() throws TclCommonException {
		TriggerEmailResponse response = new TriggerEmailResponse(Status.SUCCESS.toString());
		try {
			Optional<ServiceProvider> serviceProvider = serviceProviderRepository
					.findByName(TriggerEmailConstant.TATA_ENTITY);

			if (serviceProvider.isPresent()) {
				Set<ServiceProviderLegalEntity> spLegalEntitiesSet = serviceProvider.get()
						.getServiceProviderLegalEntities();
				Optional<MstLeAttribute> mstLeAttribute = mstLeAttributeRepository
						.findByName(TriggerEmailConstant.RELATIONSHIP_MANAGER);
				if (mstLeAttribute.isPresent()) {

					for (ServiceProviderLegalEntity spLegalEntity : spLegalEntitiesSet) {
						Optional<SpLeAttributeValue> spLeAttributeValue = spLeAttributeValueRepository
								.findByServiceProviderLegalEntityAndMstLeAttribute(spLegalEntity, mstLeAttribute.get());
						if (spLeAttributeValue.isPresent()) {
							HashMap<String, Object> map = new HashMap<>();
							map.put("userId", Utils.getSource());
							String notificationBody = constructMailNotificationObject(
									spLeAttributeValue.get().getAttributeValues(),
									TriggerEmailConstant.TRIGGER_MAIL_SUBJECT, map, multipleProfileSelectionTemplateId);
							LOGGER.info("MDC Filter token value in before Queue call getEmailIdAndTriggerEmail {} :",
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							mqUtils.send(notificationMailQueue, notificationBody);

						}
					}
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return response;
	}

	/**
	 * This method is used to send the email to the intended recipient
	 *
	 * @param toAddress
	 * @param subject
	 * @param name
	 * @param templateId
	 * @return String
	 * @throws TclCommonException
	 */
	private String constructMailNotificationObject(String toAddress, String subject, HashMap<String, Object> map,
			String templateId) throws TclCommonException {
		try {
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest.setTemplateId(templateId);
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(toAddress);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setVariable(map);
			return Utils.convertObjectToJson(mailNotificationRequest);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * This Method Takes Customer LegalId and SpLegal id as input and it provides
	 * the customer Information and Supplier Information
	 *
	 * @param custLegalId
	 * @param spLegelId
	 * @return CustomerConatctInfoResponseDto
	 * @throws TclCommonException
	 */
	public CustomerConatctInfoResponseDto getContactInfoDetaisByCustLegalIdAndSPLegalId(Integer custLegalId,
			Integer spLegelId) throws TclCommonException {

		LOGGER.info("Started  Get Contact Info methods to retreive Customer Info and Supplier Info{}{}", custLegalId,
				spLegelId);

		Optional<CustomerLegalEntity> customerLegealEntityDetail = null;
		Optional<Customer> customer;
		Optional<MstLeAttribute> mstAttribute = null;
		SpLeAttributeValue spLeAttributeValue = null;

		List<EntityAddressLocationID> entityAddressList = null;
		List<EntityAddressLocationID> spEntityAddressList = null;

		// validating the null check
		if (Objects.isNull(custLegalId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		if (Objects.isNull(spLegelId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		CustomerConatctInfoResponseDto customerConatctInforesponseDto = new CustomerConatctInfoResponseDto();
		SupplierContractingInfo supplierContractingInfo = new SupplierContractingInfo();
		try {
			customerLegealEntityDetail = customerLegalEntityRepository.findById(custLegalId);

			if (!customerLegealEntityDetail.isPresent()) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
			}

			int customerId = customerLegealEntityDetail.get().getCustomer().getId();

			customer = customerRepository.findById(customerId);
			if (!customer.isPresent()) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
			}

			customerConatctInforesponseDto.setTpsSfdcAccountId(customer.get().getTpsSfdcAccountId());
			customerConatctInforesponseDto.setAgrrementId(customerLegealEntityDetail.get().getAgreementId());
			customerConatctInforesponseDto
					.setCustomerContractingEntity(customerLegealEntityDetail.get().getEntityName());

			CustomerLegalEntity cuLe = customerLegealEntityDetail.get();
			List<CustomerLeAttributeValue> customerLeAttributeValues = customerLeAttributeRepository
					.findByCustomerLegalEntity(cuLe);
			if (customerLeAttributeValues.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
			}

			entityAddressList = customerLeAttributeValues.stream()
					.filter(cusleattrval -> cusAttributeValues.contains(cusleattrval.getMstLeAttribute().getName()))
					.map(EntityAddressLocationID::new).collect(Collectors.toList());
			entityAddressList = entityAddressList.stream().filter(Utils.distinctByKey(EntityAddressLocationID::getName))
					.collect(Collectors.toList());

			customerConatctInforesponseDto.setEntityAdress(entityAddressList);

			mstAttribute = mstLeAttributeRepository.findByName("Account Manager");
			if (!mstAttribute.isPresent()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
			}
			spLeAttributeValue = spLeAttributeValueRepository
					.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(mstAttribute.get().getId(), spLegelId);
			supplierContractingInfo.setAccountManagerId(spLeAttributeValue.getAttributeValues());

			Map<String, Boolean> signingDetails = new HashMap<>();
			mstAttribute = mstLeAttributeRepository.findByName(SpConstants.DOCUSIGN_AVAILABILITY);
			if (!mstAttribute.isPresent()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
			}
			spLeAttributeValue = spLeAttributeValueRepository
					.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(mstAttribute.get().getId(), spLegelId);
			if (spLeAttributeValue != null) {
				signingDetails.put(SpConstants.DOCUSIGN_AVAILABILITY,
						spLeAttributeValue.getAttributeValues().equals("true"));
			} else {
				signingDetails.put(SpConstants.DOCUSIGN_AVAILABILITY, true);
			}
			mstAttribute = mstLeAttributeRepository.findByName(SpConstants.CLICK_THROUGH);
			if (!mstAttribute.isPresent()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
			}
			spLeAttributeValue = spLeAttributeValueRepository
					.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(mstAttribute.get().getId(), spLegelId);
			if (spLeAttributeValue != null) {
				signingDetails.put(SpConstants.CLICK_THROUGH, spLeAttributeValue.getAttributeValues().equals("true"));
			} else {
				signingDetails.put(SpConstants.CLICK_THROUGH, false);
			}
			supplierContractingInfo.setSigningDetails(signingDetails);

			Optional<ServiceProviderLegalEntity> serviceProviderLegalEntity = serviceProviderLegalEntityRepository
					.findById(spLegelId);

			if (!serviceProviderLegalEntity.isPresent()) {
				throw new TclCommonException(ExceptionConstants.SP_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
			}

			customerConatctInforesponseDto
					.setSupplierContractingEntity(serviceProviderLegalEntity.get().getEntityName());
			ServiceProviderLegalEntity sples = serviceProviderLegalEntity.get();
			List<SpLeAttributeValue> supplierLeAttributeValues = spLeAttributeValueRepository
					.findByServiceProviderLegalEntity(sples);

			if (supplierLeAttributeValues.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.SUPPLIER_LE_ATTRIBUTE_VALUE,
						ResponseResource.R_CODE_NOT_FOUND);
			}

			spEntityAddressList = supplierLeAttributeValues.stream()
					.filter(spleattrval -> splAttributeValues.contains(spleattrval.getMstLeAttribute().getName()))
					.map(EntityAddressLocationID::new).collect(Collectors.toList());

			supplierContractingInfo.setContractingEntityId(spEntityAddressList);

			customerConatctInforesponseDto.setSupplierContractingInfo(supplierContractingInfo);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerConatctInforesponseDto;
	}

	/**
	 * getSupplierDetails
	 */
	public SPDetails getSupplierDetails(Integer spLegelId) {
		SPDetails spDetails = new SPDetails();
		spDetails.setEntityName(getSpAttributes(spLegelId, SpConstants.SUPPLIER_CONTRACTING_ENTITY));
		spDetails.setAddress(getSpAttributes(spLegelId, SpConstants.GST_ADDRESS));
		spDetails.setGstnDetails(getSpAttributes(spLegelId, SpConstants.GST_NUMBER));
		spDetails.setNoticeAddress(getSpAttributes(spLegelId, SpConstants.NOTICE_ADDRESS));
		return spDetails;
	}

	/**
	 * getSpAttributes
	 *
	 * @param spLegelId
	 */
	private String getSpAttributes(Integer spLegelId, String attrName) {
		SpLeAttributeValue spLeAttributeValue;
		Optional<MstLeAttribute> mstAttribute = mstLeAttributeRepository.findByName(attrName);
		if (mstAttribute.isPresent()) {
			spLeAttributeValue = spLeAttributeValueRepository
					.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(mstAttribute.get().getId(), spLegelId);
			if (Objects.nonNull(spLeAttributeValue)) {
				return spLeAttributeValue.getAttributeValues();
			}
		}
		return null;
	}

	/**
	 * This method takes customer legalId it provides the attachments where
	 * mstLegalAttributes has name as MSA or Service_Schedule
	 *
	 * @param custLegalId
	 * @return CustomerConatctInfoResponseDto
	 * @throws TclCommonException
	 */
	public List<AttachmentBean> getMSADocumentDetails(Integer custLegalId, String productName)
			throws TclCommonException {
		List<AttachmentBean> attachments = new ArrayList<>();

		try {
			if (Objects.isNull(custLegalId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			AttachmentBean msa = getDocument(custLegalId, DocumentConstant.MSA_DOCUMENT_ID, productName);
			AttachmentBean ss = getDocument(custLegalId, DocumentConstant.SERVICE_SCHEDULE_DOCUMENT_ID, productName);

			if (msa != null) {
				attachments.add(msa);
			}
			if (ss != null) {
				attachments.add(ss);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}

		return attachments;
	}

	/**
	 * getDocument Method to retrieve the document based on the document and
	 * customer legal entity id
	 *
	 * @param custLegalEntityId
	 * @param documentId
	 * @return AttachmentBean
	 * @throws TclCommonException
	 */
	private AttachmentBean getDocument(Integer custLegalEntityId, Integer documentId, String productName)
			throws TclCommonException {
		AttachmentBean attachment = null;
		String docName = null;
		CustomerLeAttributeValue customerLeAttributeValue = null;
		try {
			if (documentId == DocumentConstant.MSA_DOCUMENT_ID)
				docName = DocumentConstant.MSA_DOCUMENT;
			if (documentId == DocumentConstant.SERVICE_SCHEDULE_DOCUMENT_ID)
				docName = DocumentConstant.SERVICE_SCHEDULE_DOCUMENT;
			if (documentId == DocumentConstant.SERVICE_SCHEDULE_DOCUMENT_WITH_VOICE_ADDENDUM_ID)
				docName = DocumentConstant.SERVICE_SCHEDULE_DOCUMENT_WITH_VOICE_ADDENDUM;

			Optional<CustomerLegalEntity> optionalCustomerLegalEntity = customerLegalEntityRepository
					.findById(custLegalEntityId);
			if (!optionalCustomerLegalEntity.isPresent()) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);

			}

			customerLeAttributeValue = getCustomerLeAttributeValue(custLegalEntityId, documentId, productName,
					customerLeAttributeValue);
			if (customerLeAttributeValue != null) {

				attachment = getLegalEntityDocument(docName, customerLeAttributeValue, attachment);

			} else {
				attachment = getCustomerDocument(docName, optionalCustomerLegalEntity.get(), attachment);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}

		return attachment;
	}

	private CustomerLeAttributeValue getCustomerLeAttributeValue(Integer custLegalEntityId, Integer documentId,
			String productName, CustomerLeAttributeValue customerLeAttributeValue) {
		List<CustomerLeAttributeValue> optDocs = (List<CustomerLeAttributeValue>) customerLeAttributeRepository
				.findByCustomerLeIdAndMstLeAttributesIdAndProductName(custLegalEntityId, documentId, productName);

		if (optDocs != null && !optDocs.isEmpty()) {
			customerLeAttributeValue = getCustomerLeAttributeValByProdName(productName, customerLeAttributeValue,
					optDocs);
			if (customerLeAttributeValue == null) {
				Optional<CustomerLeAttributeValue> ret = optDocs.stream()
						.filter(leVal -> Objects.isNull(leVal.getProductName()) || leVal.getProductName().isEmpty())
						.findFirst();
				if (ret.isPresent()) {
					return ret.get();
				}
			}
		}
		return customerLeAttributeValue;
	}

	private CustomerLeAttributeValue getCustomerLeAttributeValByProdName(String productName,
			CustomerLeAttributeValue customerLeAttributeValue, List<CustomerLeAttributeValue> optDocs) {

		Optional<CustomerLeAttributeValue> ret = optDocs.stream()
				.filter(leVal -> leVal.getProductName() != null && leVal.getProductName().equals(productName))
				.findFirst();
		if (ret.isPresent()) {
			return ret.get();
		}
		return customerLeAttributeValue;
	}

	/**
	 * getLeStateGst
	 *
	 * @param leState
	 * @param customerLeId
	 * @return
	 */
	public List<LeStateGstBean> getLeStateGst(String leState, Integer customerLeId) throws TclCommonException {

		List<LeStateGstBean> leStateGstBeans = new ArrayList<>();
		List<LeStateGst> leStateGsts = null;

		try {

			Optional<CustomerLegalEntity> optionalCustomerLegalEntity = customerLegalEntityRepository
					.findById(customerLeId);

			if (optionalCustomerLegalEntity.isPresent()) {
				CustomerLegalEntity customerLegalEntity = optionalCustomerLegalEntity.get();
				if (leState != null) {
					leStateGsts = leStateGstRepository.findByCustomerLegalEntityAndState(customerLegalEntity, leState);
				} else {
					leStateGsts = leStateGstRepository.findByCustomerLegalEntity(customerLegalEntity);

				}
				constructLeStateGst(leStateGstBeans, leStateGsts);

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return leStateGstBeans;
	}

	/*
	 * This Method Takes Customer LegalId and product name accordingly it will
	 * identifies the category and give the currency,sple and lourequired
	 *
	 * @param id
	 *
	 * @param productName
	 *
	 * @return CustomerLegalEntityProductResponseDto
	 *
	 * @throws TclCommonException
	 */

	/**
	 * constructLeStateGst
	 *
	 * @param leStateGstBeans
	 * @param leStateGsts
	 */
	private void constructLeStateGst(List<LeStateGstBean> leStateGstBeans, List<LeStateGst> leStateGsts) {

		if (leStateGsts != null && !leStateGsts.isEmpty()) {
			leStateGsts.forEach(leGst -> {
				LeStateGstBean bean = new LeStateGstBean();
				bean.setId(leGst.getId());
				bean.setState(leGst.getState());
				bean.setGstNo(leGst.getGstNo());
				bean.setAddress(leGst.getAddress());
				leStateGstBeans.add(bean);

			});
		}

	}

	/**
	 * getLegalEntityDocument
	 *
	 * @param docName
	 * @param optDocs
	 * @param attachment2
	 */
	private AttachmentBean getLegalEntityDocument(String docName, CustomerLeAttributeValue optDocs,
			AttachmentBean attachment) {

		Integer attachId = Integer.parseInt(optDocs.getAttributeValues());

		if (attachId > 0) {
			Optional<Attachment> optAttachment = attachmentRepository.findById(attachId);
			if (optAttachment.isPresent()) {
				attachment = new AttachmentBean(optAttachment.get(), docName);

			}
		}

		return attachment;
	}

	/**
	 * getCustomerMsaDocument
	 *
	 * @param docName
	 * @param customerLegalEntity
	 * @param attachment2
	 */
	private AttachmentBean getCustomerDocument(String docName, CustomerLegalEntity customerLegalEntity,
			AttachmentBean attachment) {

		MstCustomerSpAttribute mstCustomerSpAttribute = mstCustomerSpAttributeRepository.findByNameAndStatus(docName,
				(byte) 1);
		if (mstCustomerSpAttribute != null) {

			List<CustomerAttributeValue> customerAttributeValues = customerAttributeValueRepository
					.findByCustomerAndMstCustomerSpAttribute(customerLegalEntity.getCustomer(), mstCustomerSpAttribute);
			if (customerAttributeValues != null && !customerAttributeValues.isEmpty()) {

				CustomerAttributeValue optDoc = customerAttributeValues.get(0);
				Integer attachId = Integer.parseInt(optDoc.getAttributeValues());
				Optional<Attachment> optAttachment = attachmentRepository.findById(attachId);
				if (optAttachment.isPresent()) {
					attachment = new AttachmentBean(optAttachment.get(), docName);

				}
			}
		}
		return attachment;

	}

	public CustomerLegalEntityProductResponseDto findCustomerEntityById(Integer id, String productName, Integer quoteLeId)
			throws TclCommonException {

		if (Objects.isNull(id) || StringUtils.isBlank(productName))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		CustomerLegalEntity customerLegalEntitys = null;
		CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();
		List<SpLeCountry> spleCountryList = null;
		customerLegalEntityProductResponseDto.setLouRequired(true);

		try {
			customerLegalEntitys = customerLegalEntityRepository.findAllById(id);

			if (customerLegalEntitys == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
			}
			if (productName.equals("IAS") || productName.equals("NPL") || productName.equals("GVPN")
					|| productName.equals("IZOPC") || productName.equals(IzosdwanCommonConstants.IZOSDWAN_NAME)||productName.equals("IZO Internet WAN")) {
				customerLegalEntityProductResponseDto.setCurrency("INR");
				Set<CustomerLeCountry> customerLeCountrys = customerLegalEntitys.getCustomerLeCountries();
				if (customerLeCountrys != null) {
					customerLeCountrys.stream()
							.filter(customerLeCountry -> (customerLeCountry.getMstCountry().getName()
									.equalsIgnoreCase(TriggerEmailConstant.INDIA_COUNTRY)))
							.forEach(customerLeCountry -> customerLegalEntityProductResponseDto.setLouRequired(false));
				}

				List<MstCountry> mstCountryList = mstCountryRepository.findByName(TriggerEmailConstant.INDIA_COUNTRY);
				spleCountryList = spleCountryRepository.findByMstCountryAndIsDefault(mstCountryList.get(0), (byte) 1);

				if (spleCountryList == null) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				} else {
					customerLegalEntityProductResponseDto
							.setSple(spleCountryList.get(0).getServiceProviderLegalEntity().getEntityName());
					customerLegalEntityProductResponseDto
							.setServiceProviderId(spleCountryList.get(0).getServiceProviderLegalEntity().getId());
				}

			}
			//Check isWithHoldTaxApplicable and sple for IPC product 
			if (productName.equals("IPC")) {
				customerLegalEntitys.getCustomerLeAttributeValues().stream()
				.filter(customerLeAttr -> (customerLeAttr.getMstLeAttribute().getName()
						.equalsIgnoreCase("Billing Currency")))
				.forEach(customerLeAttr -> customerLegalEntityProductResponseDto.setCurrency(customerLeAttr.getAttributeValues()));
				customerLegalEntityProductResponseDto.setLouRequired(false);
				
				LOGGER.info("Check for isWithHoldTaxApplicable for IPC product");
				Map<String, Object> request = new HashMap<>();
				request.put("QUOTE_LE_ID", quoteLeId);
				request.put("CUSTOMER_LE_ID", id);
				IpcCrossBorderBean crossBorderBean = null;
				String crossBorderBeanResponse = (String) mqUtils.sendAndReceive(crossBorderTaxQueue, Utils.convertObjectToJson(request));
				LOGGER.info("CrossBorderBean: {}", crossBorderBeanResponse);
				
				List<MstCountry> mstCountryList = new ArrayList<MstCountry>();
				if(StringUtils.isNotBlank(crossBorderBeanResponse)) {
					crossBorderBean = Utils.convertJsonToObject(crossBorderBeanResponse, IpcCrossBorderBean.class);
					customerLegalEntityProductResponseDto.setWithHoldTaxApplicable(crossBorderBean.getIsCrossBorderTaxApplicable());
					mstCountryList = mstCountryRepository.findByName(crossBorderBean.getDcLocationCountry());
				} else {
					customerLegalEntityProductResponseDto.setWithHoldTaxApplicable(Boolean.FALSE);
					Set<CustomerLeCountry> customerLeCountrys = customerLegalEntitys.getCustomerLeCountries();
					LOGGER.info("Country Name::"+customerLeCountrys.stream().findFirst().get().getMstCountry().getName());
					mstCountryList = mstCountryRepository.findByName(customerLeCountrys.stream().findFirst().get().getMstCountry().getName());
				}
				
				spleCountryList = spleCountryRepository.findByMstCountryAndIsDefault(mstCountryList.get(0), (byte) 1);
				if (spleCountryList == null) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				} else {
					customerLegalEntityProductResponseDto
							.setSple(spleCountryList.get(0).getServiceProviderLegalEntity().getEntityName());
					customerLegalEntityProductResponseDto
							.setServiceProviderId(spleCountryList.get(0).getServiceProviderLegalEntity().getId());
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerLegalEntityProductResponseDto;

	}

	/**
	 *
	 * @param id
	 * @param productName
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public List<CustomerLegalEntityProductResponseDto> findCustomerEntityByIdForOE(Integer id, String productName, Integer quoteLeId)
			throws TclCommonException {

		if (Objects.isNull(id) || StringUtils.isBlank(productName))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		CustomerLegalEntity customerLegalEntitys = null;
		List<CustomerLegalEntityProductResponseDto> list = new ArrayList<CustomerLegalEntityProductResponseDto>();
		List<SpLeCountry> spleCountryList = null;
		try {
			customerLegalEntitys = customerLegalEntityRepository.findAllById(id);

			if (customerLegalEntitys == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
			}
			if (productName.equals("IAS") || productName.equals("NPL") || productName.equals("GVPN")
					|| productName.equals("IZOPC") || productName.equals(IzosdwanCommonConstants.IZOSDWAN_NAME)) {

				Set<CustomerLeCountry> customerLeCountrys = customerLegalEntitys.getCustomerLeCountries();

				for (CustomerLeCountry customerLeCountry : customerLeCountrys) {

					spleCountryList = spleCountryRepository
							.findByMstCountryAndIsDefault(customerLeCountry.getMstCountry(), (byte) 1);

					if (spleCountryList == null) {
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_NOT_FOUND);
					} else {

						for (SpLeCountry spLeCountry : spleCountryList) {

							CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();
							customerLegalEntityProductResponseDto.setCurrency(
									getCurrencyBySupplierLegalId(spLeCountry.getServiceProviderLegalEntity().getId()));
							customerLegalEntityProductResponseDto
									.setSple(spLeCountry.getServiceProviderLegalEntity().getEntityName());
							customerLegalEntityProductResponseDto
									.setServiceProviderId(spLeCountry.getServiceProviderLegalEntity().getId());

							list.add(customerLegalEntityProductResponseDto);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return list;

	}

	/**
	 * findCustomerEntityAllDetailsByCustomerId - This method is used for fetching
	 * all details about customerLegal entity for the given customer ID
	 *
	 * @param id
	 * @return List<CustomerLegalEntityDto>
	 * @throws TclCommonException
	 * @author NAVEEN GUNASEKARAN
	 */
	public List<CustomerDto> findCustomerEntityAllDetails() throws TclCommonException {
		List<CustomerLegalEntity> customerLegalEntitys = null;
		List<CustomerDto> customerDtoList = new ArrayList<>();
		try {
			List<CustomerDetail> customerDetailsList = userInfoUtils.getCustomerDetails();
			for (CustomerDetail customerDetail : customerDetailsList) {
				customerLegalEntitys = customerLegalEntityRepository
						.findByCustomerId(customerDetail.getErfCustomerId());
				List<Integer> attachmentIds = customerLegalEntitys.stream()
						.flatMap(custLeEntity -> custLeEntity.getCustomerLeAttributeValues().stream()
								.filter(custLeAttrVal -> custLeAttrVal.getMstLeAttribute().getType()
										.equalsIgnoreCase(ATTACHMENT))
								.map(custLeAttrVal -> custLeAttrVal.getAttributeValues()).map(Integer::new))
						.collect(Collectors.toList());

				List<Attachment> attachments = attachmentRepository.findByIdIn(attachmentIds);

				if (customerLegalEntitys.isEmpty()) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				}

				if (!attachments.isEmpty()) {
					List<AttachmentDto> attachmentDtos = attachments.stream().map(AttachmentDto::new)
							.collect(Collectors.toList());
					for (Entry<Integer, List<CustomerLegalEntity>> map : getCustomerToLegalEntityMap(
							customerLegalEntitys).entrySet()) {
						customerDtoList.add(new CustomerDto(map.getKey(), map.getValue(), attachmentDtos));
					}
				} else {
					for (Entry<Integer, List<CustomerLegalEntity>> map : getCustomerToLegalEntityMap(
							customerLegalEntitys).entrySet()) {
						customerDtoList.add(new CustomerDto(map.getKey(), map.getValue()));
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerDtoList;
	}

	/**
	 * getCustomerToLegalEntityMap
	 *
	 * @param customerLegalEntities
	 * @return Map<Integer , List < CustomerLegalEntity>> - groups the legal entity
	 *         by customers
	 */
	private Map<Integer, List<CustomerLegalEntity>> getCustomerToLegalEntityMap(
			List<CustomerLegalEntity> customerLegalEntities) {
		List<CustomerLegalEntity> customerLegalEntityList = new ArrayList<>();
		Map<Integer, List<CustomerLegalEntity>> customerToLegalEntityMapping = new HashMap<>();
		customerLegalEntities.stream().forEach(customerLegalEntity -> {
			if (customerToLegalEntityMapping.get(customerLegalEntity.getCustomer().getId()) == null) {
				customerLegalEntityList.add(customerLegalEntity);
				customerToLegalEntityMapping.put(customerLegalEntity.getCustomer().getId(), customerLegalEntityList);
			} else {
				List<CustomerLegalEntity> customerLegalEntityListTmp = customerToLegalEntityMapping
						.get(customerLegalEntity.getCustomer().getId());
				customerLegalEntityListTmp.add(customerLegalEntity);
				customerToLegalEntityMapping.put(customerLegalEntity.getCustomer().getId(), customerLegalEntityListTmp);
			}
		});
		return customerToLegalEntityMapping;
	}

	/**
	 * findCustomerEntityDetailsByCustomerLeId - This method is used for fetching
	 * all details about customerLegal entity for the given customer le ID
	 *
	 * @param List<CustomerLeId>
	 * @return List<CustomerLegalEntityDto>
	 * @throws TclCommonException
	 * @author ANNE NISHA
	 */

	public List<CustomerLegalEntityDto> findCustomerEntityDetailsByCustomerLeId(Set<Integer> customerLegalEntitIds)
			throws TclCommonException {
		if (Objects.isNull(customerLegalEntitIds))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		List<CustomerLegalEntityDto> customerLegalEntityDtos = null;
		List<CustomerLegalEntity> customerLegalEntitys = null;
		try {
			customerLegalEntitys = customerLegalEntityRepository.findAllByIdIn(new ArrayList<>(customerLegalEntitIds));
			customerLegalEntityDtos = customerLegalEntitys.stream().map(CustomerLegalEntityDto::new)
					.collect(Collectors.toList());

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerLegalEntityDtos;
	}

	/**
	 * This method gives the attachment details for the given attachment Id
	 *
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	public AttachmentBean findAttachmentByAttachmentId(int attachmentId) throws TclCommonException {
		AttachmentBean attachmentBean = new AttachmentBean();

		if (Objects.isNull(attachmentId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		try {

			Optional<Attachment> optAttachment = attachmentRepository.findById(attachmentId);
			if (!optAttachment.isPresent()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Attachment attachment = optAttachment.get();
			attachmentBean.setId(attachment.getId());
			attachmentBean.setName(attachment.getName());
			attachmentBean.setUriPath(attachment.getUriPathOrUrl());
			attachmentBean.setDisplayName(attachment.getDisplayName());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attachmentBean;
	}

	/**
	 * getBillingDetailsForOms
	 *
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public CustomerLeDetailsBean getLeAttributes(Integer customerLeId) throws TclCommonException {
		CustomerLeDetailsBean omsDetailsBean = null;
		List<Attributes> attributes = new ArrayList<>();
		try {
			omsDetailsBean = new CustomerLeDetailsBean();
			Optional<CustomerLegalEntity> opcustomerLegalEntity = customerLegalEntityRepository.findById(customerLeId);
			if (!opcustomerLegalEntity.isPresent()) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_ERROR);

			}
			CustomerLegalEntity cLegalEntity = opcustomerLegalEntity.get();
			omsDetailsBean.setAccounCuId(cLegalEntity.getTpsSfdcCuid());
			omsDetailsBean.setAccountId(cLegalEntity.getCustomer().getAccountId18());
			omsDetailsBean.setLegalEntityName(cLegalEntity.getEntityName());
			List<CustomerLeAttributeValue> customerLeAttributeValueList = customerLeAttributeValueRepository
					.findByCustomerLegalEntity_Id(customerLeId);

			List<CustomerLeBillingInfo> customerContacts = customerLeBillingInfoRepository
					.findByCustomerLegalEntity_IdAndIsactive(customerLeId, "Yes");
			if (!customerContacts.isEmpty()) {
				omsDetailsBean.setBillingContactId(customerContacts.get(0).getId());
			}

			if (customerLeAttributeValueList.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			customerLeAttributeValueList.stream().forEach(customerLeAttributeValue -> {
				Attributes attribute = new Attributes();
				attribute.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
				attribute.setAttributeValue(customerLeAttributeValue.getAttributeValues());
				attribute.setType(customerLeAttributeValue.getMstLeAttribute().getType());
				attributes.add(attribute);
			});
			omsDetailsBean.getAttributes().addAll(attributes);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return omsDetailsBean;
	}

	/**
	 * @return ResponseResource
	 * @throws TclCommonException
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 *            <p>
	 *            used for oms for pricing
	 */
	@Transactional
	public CustomerDetailsBean getcustomerDetails(Integer customerId) throws TclCommonException {
		CustomerDetailsBean customerDetailsBean = new CustomerDetailsBean();

		try {

			Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
			if (!optionalCustomer.isPresent()) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_ERROR);

			}

			List<MstCustomerSpAttribute> mstCustomerSpAttributes = mstCustomerSpAttributeRepository
					.findByNameInAndStatus(getAttributesConstants(), (byte) 1);
			if (mstCustomerSpAttributes != null) {
				mstCustomerSpAttributes.forEach(mstAttributes -> {

					List<CustomerAttributeValue> attributeValues = customerAttributeValueRepository
							.findByCustomerAndMstCustomerSpAttribute(optionalCustomer.get(), mstAttributes);
					if (attributeValues != null) {
						attributeValues.forEach(attr -> {
							CustomerAttributeBean attribute = new CustomerAttributeBean();
							attribute.setValue(attr.getAttributeValues());
							if (attr.getMstCustomerSpAttribute() != null) {
								MstCustomerSpAttribute mstAtrr = attr.getMstCustomerSpAttribute();
								attribute.setName(mstAtrr.getName());
							}
							customerDetailsBean.getCustomerAttributes().add(attribute);

						});
					}

				});
			}
			LOGGER.info("Customer details response {}", customerDetailsBean);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerDetailsBean;
	}

	private List<String> getAttributesConstants() {

		List<String> mstNames = new ArrayList<>();

		mstNames.add(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue());
		mstNames.add(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue());
		mstNames.add(CustomerAttributeConstants.SALES_ORG.getAttributeValue());
		mstNames.add(CustomerAttributeConstants.CUSTOMER_TRIGRAM.getAttributeValue());

		return mstNames;

	}

	/**
	 * uploadFiles willl remove it once upload is done
	 *
	 * @param file
	 * @param customerLeId
	 * @param referenceName
	 * @param attachmentType
	 * @return
	 */
	@Transactional
	public AttachmentBean uploadFiles(MultipartFile file, Integer customerLeId, String referenceName,
			String attachmentType, String productName) throws TclCommonException {

		AttachmentBean fileUploadResponse = new AttachmentBean();
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
						.findById(customerLeId);
				if (customerLegalEntity.isPresent()) {
					Customer customer = customerLegalEntity.get().getCustomer();
					if (customer.getCustomerCode() == null) {
						customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
						customerRepository.save(customer);
					}
					if (customerLegalEntity.get().getCustomerLeCode() == null) {
						customerLegalEntity.get()
								.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
						customerLegalEntityRepository.save(customerLegalEntity.get());
					}
					tempUploadUrlInfo = fileStorageService.getTempUploadUrl(Long.parseLong(tempUploadUrlExpiryWindow),
							customer.getCustomerCode(), customerLegalEntity.get().getCustomerLeCode(), false);
					fileUploadResponse.setUriPath(tempUploadUrlInfo.getTemporaryUploadUrl());
					fileUploadResponse.setName(tempUploadUrlInfo.getRequestId());
				}
			} else {

				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);

				String newFolder = uploadPath + now.format(formatter);
				File filefolder = new File(newFolder);
				if (!filefolder.exists()) {
					filefolder.mkdirs();

				}

				Optional<MstLeAttribute> mOptional = mstLeAttributeRepository.findByName(attachmentType);
				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
						.findById(customerLeId);
				if (mOptional.isPresent()) {
					Path path = Paths.get(newFolder);
					Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
					List<CustomerLeAttributeValue> leAttributeValues = customerLeAttributeRepository
							.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLeId, mOptional.get().getId());
					if(Objects.nonNull(productName) && TEAMSDR.equals(productName)){
						leAttributeValues = customerLeAttributeRepository
								.findByCustomerLegalEntity_IdAndMstLeAttribute_IdAndProductName(customerLeId, mOptional.get().getId(),productName);
					}
					if (leAttributeValues != null && !leAttributeValues.isEmpty()) {
						leAttributeValues.stream().forEach(customerLeAttributeValue -> {
							Optional<Attachment> attachment = attachmentRepository
									.findById(Integer.valueOf(customerLeAttributeValue.getAttributeValues()));
							if (attachment.isPresent()) {
								attachment.get().setUriPathOrUrl(newFolder);
								attachment.get().setDisplayName(referenceName);
								attachment.get().setName(referenceName);
								Attachment attachment2 = attachmentRepository.save(attachment.get());
								fileUploadResponse.setId(attachment2.getId());
								fileUploadResponse.setDisplayName(attachment2.getDisplayName());
								fileUploadResponse.setName(attachment2.getName());

							}

						});
					} else {
						createAttachment(referenceName, customerLegalEntity, fileUploadResponse, newFolder, mOptional,productName);

					}
				}
			}

			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * uploadFiles willl remove it once upload is done
	 *
	 * @param file
	 * @param customerLeId
	 * @param referenceName
	 * @param attachmentType
	 * @return
	 */
	public AttachmentBean upLoadCustomerFile(MultipartFile file, Integer customerId, String referenceName,
			String attachmentType) throws TclCommonException {

		AttachmentBean fileUploadResponse = new AttachmentBean();
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);

			String newFolder = uploadPath + now.format(formatter);
			File filefolder = new File(newFolder);
			if (!filefolder.exists()) {
				filefolder.mkdirs();

			}

			MstCustomerSpAttribute mstCustomerSpAttribute = mstCustomerSpAttributeRepository
					.findByNameAndStatus(attachmentType, (byte) 1);
			Optional<Customer> customerEntity = customerRepository.findById(customerId);
			if (customerEntity.isPresent()) {
				//Path path = Paths.get(newFolder);
				//Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
				List<CustomerAttributeValue> leAttributeValues = customerAttributeValueRepository
						.findByCustomerAndMstCustomerSpAttribute(customerEntity.get(), mstCustomerSpAttribute);
				if (leAttributeValues != null && !leAttributeValues.isEmpty()) {

					for (CustomerAttributeValue customerLeAttributeValue : leAttributeValues) {
						Optional<Attachment> attachment = attachmentRepository
								.findById(Integer.valueOf(customerLeAttributeValue.getAttributeValues()));
						if (attachment.isPresent()) {
							attachment.get().setUriPathOrUrl(newFolder);
							attachment.get().setDisplayName(referenceName);
							attachment.get().setName(referenceName);
							attachmentRepository.save(attachment.get());
						}

					}
				} else {
					createAttachment(referenceName, customerEntity, fileUploadResponse, newFolder,
							mstCustomerSpAttribute);

				}
			}

			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * createAttachment
	 *
	 * @param referenceName
	 * @param customerLegalEntity
	 * @param fileUploadResponse
	 * @param mOptional
	 * @param newFolder
	 */
	private void createAttachment(String referenceName, Optional<CustomerLegalEntity> customerLegalEntity,
			AttachmentBean fileUploadResponse, String newFolder, Optional<MstLeAttribute> mOptional,String productName) {
		Attachment attachment = new Attachment();
		attachment.setDisplayName(referenceName);
		attachment.setName(referenceName);
		attachment.setUriPathOrUrl(newFolder);
		Attachment attachmen = attachmentRepository.save(attachment);
		CustomerLeAttributeValue attributeValue = new CustomerLeAttributeValue();
		if (customerLegalEntity.isPresent())
			attributeValue.setCustomerLegalEntity(customerLegalEntity.get());
		if (mOptional.isPresent())
			attributeValue.setMstLeAttribute(mOptional.get());
		attributeValue.setAttributeValues(String.valueOf(attachmen.getId()));
		if(Objects.nonNull(productName)) attributeValue.setProductName(productName);
		customerLeAttributeRepository.save(attributeValue);
		fileUploadResponse.setId(attachmen.getId());
		fileUploadResponse.setDisplayName(attachmen.getDisplayName());
		fileUploadResponse.setName(attachmen.getName());
		fileUploadResponse.setUriPath(attachmen.getUriPathOrUrl());
	}

	/**
	 * createAttachment
	 *
	 * @param referenceName
	 * @param customerLegalEntity
	 * @param fileUploadResponse
	 * @param mOptional
	 * @param newFolder
	 */
	private void createAttachment(String referenceName, Optional<Customer> customerLegalEntity,
			AttachmentBean fileUploadResponse, String newFolder, MstCustomerSpAttribute mstCustomerSpAttribute) {
		Attachment attachment = new Attachment();
		attachment.setDisplayName(referenceName);
		attachment.setName(referenceName);
		attachment.setUriPathOrUrl(newFolder);
		Attachment attachmen = attachmentRepository.save(attachment);
		CustomerAttributeValue attributeValue = new CustomerAttributeValue();
		if (customerLegalEntity.isPresent())
			attributeValue.setCustomer(customerLegalEntity.get());
		attributeValue.setMstCustomerSpAttribute(mstCustomerSpAttribute);
		attributeValue.setAttributeValues(String.valueOf(attachmen.getId()));
		customerAttributeValueRepository.save(attributeValue);
		fileUploadResponse.setId(attachmen.getId());
		fileUploadResponse.setDisplayName(attachmen.getDisplayName());
		fileUploadResponse.setName(attachmen.getName());
		fileUploadResponse.setUriPath(attachmen.getUriPathOrUrl());
	}

	/**
	 * getAttachments
	 *
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	public Resource getAttachments(Integer customerLeId, Integer attachmentId) throws TclCommonException {
		Resource resource = null;
		try {
			if (attachmentId == null || attachmentId == 0) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			if (validateAttachmentId(customerLeId, attachmentId)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			Optional<Attachment> attachmentRepo = attachmentRepository.findById(attachmentId);
			if (attachmentRepo.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					String tempDownloadUrl = fileStorageService.getTempDownloadUrl(
							attachmentRepo.get().getDisplayName(), Long.parseLong(tempDownloadUrlExpiryWindow),
							attachmentRepo.get().getUriPathOrUrl(), false);
					resource = new ByteArrayResource(tempDownloadUrl.getBytes());
				} else {
					LOGGER.info("Path received :: {}", attachmentRepo.get().getUriPathOrUrl());
					File[] files = new File(attachmentRepo.get().getUriPathOrUrl()).listFiles();
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
						return resource;
					}
				}
			}
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing download malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			// throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
			// ResponseResource.R_CODE_ERROR);
		}
		return resource;
	}

	/**
	 * validateAttachmentId
	 */
	private boolean validateAttachmentId(Integer customerLeId, Integer attachmentId) {
		List<CustomerLeAttributeValue> customerLeAttributes = customerLeAttributeRepository
				.findByCustomerLeIdAndAttachmentId(customerLeId, String.valueOf(attachmentId));
		return (customerLeAttributes == null || customerLeAttributes.isEmpty());
	}

	public ServiceResponse processUploadFiles(MultipartFile file, Integer orderToLeId, Integer quoteToLeId,
			String attachmentType, List<Integer> referenceId, String referenceName, Integer customerLeId,
			String productName) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		TempUploadUrlInfo tempUploadUrlInfo = null;

		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
						.findById(customerLeId);
				if (customerLegalEntity.isPresent()) {
					Customer customer = customerLegalEntity.get().getCustomer();
					if (customer.getCustomerCode() == null) {
						customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
						customerRepository.save(customer);
					}
					if (customerLegalEntity.get().getCustomerLeCode() == null) {
						customerLegalEntity.get()
								.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
						customerLegalEntityRepository.save(customerLegalEntity.get());
					}
					tempUploadUrlInfo = fileStorageService.getTempUploadUrl(Long.parseLong(tempUploadUrlExpiryWindow),
							customer.getCustomerCode(), customerLegalEntity.get().getCustomerLeCode(), false);
					fileUploadResponse.setUrlPath(tempUploadUrlInfo.getTemporaryUploadUrl());
					fileUploadResponse.setFileName(tempUploadUrlInfo.getRequestId());
				}
			} else {
				validateOmsRequest(file, orderToLeId, quoteToLeId, attachmentType, referenceId, referenceName,
						customerLeId);
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
				com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
				attachmentBean.setPath(newFolder);
				attachmentBean.setFileName(file.getOriginalFilename());
				Integer attachmentId = attachmentService.processAttachment(attachmentBean);
				fileUploadResponse.setAttachmentId(attachmentId);

				Optional<MstLeAttribute> mstAttributeOpt = mstLeAttributeRepository.findByName("MSA");
				if (mstAttributeOpt.isPresent()) {
					List<CustomerLeAttributeValue> cusLeAttributesList = customerLeAttributeValueRepository
							.findByCustomerLeIdAndAttachmentId(customerLeId, String.valueOf(attachmentId));
					CustomerLeAttributeValue cusLeAttribute;
					if(TEAMSDR.equals(productName)){
						cusLeAttributesList = customerLeAttributeValueRepository
								.findByCustomerLeIdAndAttachmentIdForTeamsDR(customerLeId, String.valueOf(attachmentId));
					}
					if (cusLeAttributesList.isEmpty()) {
						cusLeAttribute = new CustomerLeAttributeValue();
						cusLeAttribute.setAttributeValues(String.valueOf(attachmentId));
						cusLeAttribute.setMstLeAttribute(mstAttributeOpt.get());
						cusLeAttribute.setCustomerLegalEntity(customerLegalEntityRepository.findAllById(customerLeId));
						cusLeAttribute.setProductName(productName);
						customerLeAttributeValueRepository.save(cusLeAttribute);
					}

				}
				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				referenceId.forEach(refId -> {
					OmsAttachBean omsAttachBean = new OmsAttachBean();
					omsAttachBean.setAttachmentId(attachmentId);
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
					omsAttachBean.setOrderLeId(orderToLeId);
					omsAttachBean.setQouteLeId(quoteToLeId);
					omsAttachBean.setReferenceId(refId);
					omsAttachBean.setReferenceName(referenceName);
					omsAttachBeanList.add(omsAttachBean);
				});

				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(omsAttachmentQueue, oattachmentrequest);

			}

			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	private void validateOmsRequest(MultipartFile file, Integer orderToLeId, Integer quoteToLeId, String attachmentType,
			List<Integer> referenceId, String referenceName, Integer customerLeId) throws TclCommonException {

		if (Objects.isNull(orderToLeId) && Objects.isNull(quoteToLeId)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}

//		if (Objects.isNull(file)) {
//			throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
//		}

		if (Objects.isNull(referenceName)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (CollectionUtils.isEmpty(referenceId)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (Objects.isNull(attachmentType)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (Objects.isNull(customerLeId)) {
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	public String getServiceProviderDetails(Integer id) throws TclCommonException {
		String serviceProvideExcelBean = null;
		try {
			Optional<ServiceProviderLegalEntity> serviceProvider = serviceProviderLegalEntityRepository.findById(id);
			if (serviceProvider.isPresent()) {
				serviceProvideExcelBean = serviceProvider.get().getEntityName();
			}
		} catch (Exception e) {
			throw new TclCommonException("No Service provider");
		}
		return serviceProvideExcelBean;
	}
	
	public ServiceProviderLegalBean getServiceProviderDetailsSdwan(Integer id) throws TclCommonException {
		ServiceProviderLegalBean serviceProviderLegalEntityDto = null;
		try {
			Optional<ServiceProviderLegalEntity> serviceProvider = serviceProviderLegalEntityRepository.findById(id);
			if (serviceProvider.isPresent()) {
				serviceProviderLegalEntityDto = new ServiceProviderLegalBean();
				serviceProviderLegalEntityDto.setEntityName(serviceProvider.get().getEntityName());
				Optional<MstLeAttribute> mstLeAttribute = mstLeAttributeRepository.findByName(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_CUSTOMER);
				if(mstLeAttribute.isPresent()) {
					Optional<SpLeAttributeValue> spLeAttributeValues =  spLeAttributeValueRepository.findByServiceProviderLegalEntityAndMstLeAttribute(serviceProvider.get(), mstLeAttribute.get());
					if(spLeAttributeValues.isPresent()) {
						serviceProviderLegalEntityDto.setContractingAddressId(spLeAttributeValues.get().getAttributeValues());
					}
				}
				
			}
		} catch (Exception e) {
			throw new TclCommonException("No Service provider");
		}
		return serviceProviderLegalEntityDto;
	}

	public List<CustomerDto> getAllCustomer() throws TclCommonException {
		try {
			List<Customer> customerList = customerRepository.findAll();
			if (customerList != null && !customerList.isEmpty()) {
				return constructCustomerDto(customerList);
			} else {
				throw new TclCommonException(ExceptionConstants.NO_CUSTOMER, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}

	public List<CustomerDto> constructCustomerDto(List<Customer> customerList) {
		List<CustomerDto> customerDtoList = new ArrayList<>();
		for (Customer customer : customerList) {
			CustomerDto customerDto = new CustomerDto();
			customerDto.setCustomerId(customer.getId());
			customerDto.setCustomerName(customer.getCustomerName());
			List<CustomerLegalEntityDto> customerLegalEntityDtoList = new ArrayList<>();
			customer.getCustomerLegalEntities().stream().forEach(customerLe -> {
				CustomerLegalEntityDto customerLegalEntityDto = new CustomerLegalEntityDto();
				customerLegalEntityDto.setLegalEntityName(customerLe.getEntityName());
				customerLegalEntityDto.setLegalEntityId(customerLe.getId());
				customerLegalEntityDto.setAgreementId(customerLe.getAgreementId());
				customerLegalEntityDto.setSfdcId(customerLe.getTpsSfdcCuid());
				customerLegalEntityDtoList.add(customerLegalEntityDto);
				customerDto.setLegalEntity(customerLegalEntityDtoList);
			});
			customerDtoList.add(customerDto);
		}
		return customerDtoList;
	}

	/**
	 * getCustomerLeDetails - This method fetches all the legal entity details for
	 * the customerLeIds array input
	 *
	 * @param String[] customerLeIds
	 * @return List<CustomerLeBean>
	 * @throws TclCommonException
	 */
	public List<CustomerLeBean> getCustomerLeDetails(String[] customerLeIds) {
		List<CustomerLeBean> customerEntitiesDtoList = new ArrayList<>();
		for (String customerLeId : customerLeIds) {
			Optional<CustomerLegalEntity> custLE = customerLegalEntityRepository
					.findById(Integer.valueOf(customerLeId));
			if (custLE.isPresent()) {
				CustomerLeBean leBean = new CustomerLeBean();
				leBean.setAgreementId(custLE.get().getAgreementId());
				leBean.setLegalEntityId(custLE.get().getId());
				leBean.setLegalEntityName(custLE.get().getEntityName());
				leBean.setSfdcId(custLE.get().getTpsSfdcCuid());
				leBean.setType("NON-RTM");
				MstCustomerSpAttribute mstCustomerSpAttributeForAccountRtm = mstCustomerSpAttributeRepository
						.findByNameAndStatus(PartnerCustomerConstants.ACCOUNT_RTM, (byte) 1);
				if (mstCustomerSpAttributeForAccountRtm!=null) {
					List<CustomerAttributeValue> customerLeAttList = customerAttributeValueRepository
							.findByCustomerAndMstCustomerSpAttribute(custLE.get().getCustomer(), mstCustomerSpAttributeForAccountRtm);
					for (CustomerAttributeValue customerAttributeValue : customerLeAttList) {
						if(StringUtils.isNotBlank(customerAttributeValue.getAttributeValues()) &&customerAttributeValue.getAttributeValues().toLowerCase().contains("partner")) {
							leBean.setType("RTM");
						}
					}
				}
				Set<CustomerLeCountry> customerLeCountry = custLE.get().getCustomerLeCountries();
				if (customerLeCountry != null && !customerLeCountry.isEmpty()) {
					leBean.setCountry(customerLeCountry.stream()
							.map(countryList -> countryList.getMstCountry().getName())
							.collect(Collectors.toList()));
				}
				customerEntitiesDtoList.add(leBean);
			}
		}
		LOGGER.info("CustomerEntitiesDtoList :: {}", customerEntitiesDtoList);
		return customerEntitiesDtoList;

	}

	public List<CustomerLeBean> getCustomerLeNameById(String[] customerLeIds) {
		List<CustomerLeBean> customerEntitiesDtoList = new ArrayList<>();
		List<String> customerLeIdString = Arrays.asList(customerLeIds);
		List<Integer> custoLeIds = new ArrayList<>();
		for (String le : customerLeIdString) {
			custoLeIds.add(Integer.valueOf(le));
		}
		List<CustomerLegalEntity> custLE = customerLegalEntityRepository.findAllByIdIn(custoLeIds);
		for (CustomerLegalEntity customerLegalEntity : custLE) {
			CustomerLeBean leBean = new CustomerLeBean();
			leBean.setAgreementId(customerLegalEntity.getAgreementId());
			leBean.setLegalEntityId(customerLegalEntity.getId());
			leBean.setLegalEntityName(customerLegalEntity.getEntityName());
			leBean.setSfdcId(customerLegalEntity.getTpsSfdcCuid());
			customerEntitiesDtoList.add(leBean);
		}
		LOGGER.info("Customer Le Details for inputId {} is {}", customerLeIdString, customerEntitiesDtoList);
		return customerEntitiesDtoList;

	}

	/**
	 * getCustDetails - This method fetches all the customer details for the given
	 * customer id
	 *
	 * @param customerIds
	 * @return
	 * @throws TclCommonException
	 */
	public Set<CustomerDetailBean> getCustDetails(String[] customerIds) throws TclCommonException {
		Objects.requireNonNull(customerIds, "Customer ID cannot be null");
		LOGGER.info("CustomerId :: {}", customerIds);

		SortedSet<CustomerDetailBean> customerDetailSet = new TreeSet<>(
				Comparator.comparing(CustomerDetailBean::getCustomerName));
		try {
			int[] customerIntgerIds = Arrays.stream(customerIds).mapToInt(Integer::parseInt).toArray();
			List<Integer> customerIdList = Arrays.stream(customerIntgerIds).boxed().collect(Collectors.toList());
			List<Customer> customerList = customerRepository.findByIdInOrderByCustomerNameAsc(customerIdList);
			List<Integer> custLeIDs = new ArrayList<>();
			userInfoUtils.getCustomerDetails().forEach(les -> custLeIDs.add(les.getCustomerLeId()));
			customerList.stream().forEach(customer -> {
				CustomerDetailBean customerDetail = new CustomerDetailBean();
				customerDetail.setCustomerId(customer.getId());
				customerDetail.setCustomerName(customer.getCustomerName());
				customerDetail.setSFDCAccountId(customer.getAccountId18());
				customerDetail.setCustomercode(customer.getCustomerCode());
				customerDetail.setIsVerified(customer.getIsVerified());
				Set<String> customerCuid = new HashSet<>();
				customer.getCustomerLegalEntities().stream().forEach(legal -> {
					if (custLeIDs.contains(legal.getId())) {
						customerCuid.add(legal.getTpsSfdcCuid());
						customerDetail.setCuid(customerCuid);
					}
				});
				customerDetailSet.add(customerDetail);
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("CustomerDetailSet :: {}", customerDetailSet);
		return customerDetailSet;
	}




	/**
	 * getCustDetails - This method fetches all the customer details for the given
	 * customer id
	 *
	 * @param customerIds
	 * @return
	 * @throws TclCommonException
	 */
	public Set<CustomerDetailBean> getRtmCustDetails(String[] customerIds) throws AmqpRejectAndDontRequeueException {
		Objects.requireNonNull(customerIds, "Customer ID cannot be null");
		LOGGER.info("CustomerId :: {}", customerIds);

		SortedSet<CustomerDetailBean> customerDetailSet = new TreeSet<>(
				Comparator.comparing(CustomerDetailBean::getCustomerName));
		try {
			int[] customerIntgerIds = Arrays.stream(customerIds).mapToInt(Integer::parseInt).toArray();
			List<Integer> customerIdList = Arrays.stream(customerIntgerIds).boxed().collect(Collectors.toList());
			List<Customer> customerList = customerRepository.findByIdInOrderByCustomerNameAsc(customerIdList);
			customerList.stream().forEach(customer -> {
				CustomerDetailBean customerDetail = new CustomerDetailBean();
				customerDetail.setCustomerId(customer.getId());
				customerDetail.setCustomerName(customer.getCustomerName());
				customerDetail.setSFDCAccountId(customer.getAccountId18());
				customerDetail.setCustomercode(customer.getCustomerCode());
				customerDetail.setIsVerified(customer.getIsVerified());
				customerDetail.setType("NON-RTM");
				MstCustomerSpAttribute mstCustomerSpAttributeForAccountRtm = mstCustomerSpAttributeRepository
						.findByNameAndStatus(PartnerCustomerConstants.ACCOUNT_RTM, (byte) 1);
				if (mstCustomerSpAttributeForAccountRtm!=null) {
					List<CustomerAttributeValue> customerLeAttList = customerAttributeValueRepository
							.findByCustomerIdAndMstCustomerSpAttribute(customer.getId(), mstCustomerSpAttributeForAccountRtm);
					if(customerLeAttList!=null&&!customerLeAttList.isEmpty() ) {
						for (CustomerAttributeValue customerAttributeValue : customerLeAttList) {
							if (customerAttributeValue.getAttributeValues() != null && customerAttributeValue.getAttributeValues().toLowerCase().contains("partner")) {
								customerDetail.setType("RTM");
							}
						}
					}
				}
				customerDetailSet.add(customerDetail);
			});
		} catch (Exception e) {
			LOGGER.error("Error occured: {}", e);
			throw new AmqpRejectAndDontRequeueException(e);
		}
		LOGGER.info("CustomerDetailSet :: {}", customerDetailSet);
		return customerDetailSet;
	}

	public Set<Integer> getCustomerLesByCustomerId(String customerId) throws TclCommonException {
		LOGGER.debug("Input CustomerId {}", customerId);
		Set<Integer> customerLeSet = new HashSet<>();
		try {
			List<CustomerLegalEntity> customerLes = customerLegalEntityRepository
					.findByCustomerId(Integer.valueOf(customerId));
			for (CustomerLegalEntity customerLegalEntity : customerLes) {
				customerLeSet.add(customerLegalEntity.getId());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.debug("Output CustomerLeId {}", customerLeSet);
		return customerLeSet;
	}

	/**
	 * @param request
	 * @return
	 * @author VIVEK KUMAR K getSupplierLegalEntityBasedOnCust
	 */
	public CustomerLegalEntityProductResponseDto getSupplierLegalEntityBasedOnCust(
			CustomerLegalEntityRequestBean request) throws TclCommonException {
		CustomerLegalEntity customerLegalEntitys = null;
		CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = null;
		try {
			validateRequest(request);
			customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();
			customerLegalEntityProductResponseDto.setLouRequired(true);

			customerLegalEntitys = customerLegalEntityRepository.findAllById(request.getCustomerLegalEntityId());

			if (customerLegalEntitys == null) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_ERROR);
			}

			CustomerLeCurrency customerLecurrency = customerLegalEntitys.getCustomerLeCurrencies().stream().findAny()
					.orElse(null);
			if (customerLecurrency != null) {
				customerLegalEntityProductResponseDto
						.setCurrency(customerLecurrency.getCurrencyMaster().getShortName());
			} else {
				customerLegalEntityProductResponseDto.setCurrency("INR");

			}

			Set<CustomerLeCountry> customerLeCountrys = customerLegalEntitys.getCustomerLeCountries();
			if (customerLeCountrys != null && !customerLeCountrys.isEmpty()) {
				CustomerLeCountry customerLeCountry = customerLeCountrys.stream().findAny().orElse(null);
				if (customerLeCountry != null && customerLeCountry.getMstCountry().getName()
						.equalsIgnoreCase(TriggerEmailConstant.INDIA_COUNTRY)) {
					customerLegalEntityProductResponseDto.setLouRequired(false);
					MstCountry country = customerLeCountry.getMstCountry();
					validateSpLegalEntity(country, request, customerLegalEntityProductResponseDto);
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerLegalEntityProductResponseDto;

	}

	/**
	 * validateSpLegalEntity
	 *
	 * @param country
	 * @param request
	 * @param customerLegalEntityProductResponseDto
	 * @param spleCountryList
	 */
	private void validateSpLegalEntity(MstCountry country, CustomerLegalEntityRequestBean request,
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto) throws TclCommonException {
		if (isSiteAndCuLeSame(country, request.getSiteCountry())) {

			getSupLegalEntityAgainstCustomerCountry(country, customerLegalEntityProductResponseDto);

		} else if (checkForSiteEquality(request.getSiteCountry())) {
			getSupLegalEntityAgaintsSite(request.getSiteCountry(), customerLegalEntityProductResponseDto);

		} else {
			throw new TclCommonException(ExceptionConstants.MULTIPLE_SITES_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}

	}

	/**
	 * getSupLegalEntityAgaintsSite
	 *
	 * @param siteCountry
	 * @param customerLegalEntityProductResponseDto
	 */
	private void getSupLegalEntityAgaintsSite(List<SiteCountryBean> siteCountry,
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto) throws TclCommonException {
		List<SpLeCountry> spleCountryList = null;

		List<MstCountry> mstCountryList = mstCountryRepository.findByName(siteCountry.get(0).getCountry());
		if (mstCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.MST_COUNTRY_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		}
		spleCountryList = spleCountryRepository.findByMstCountryAndIsDefault(mstCountryList.get(0), (byte) 1);

		if (spleCountryList == null || spleCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.SP_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		} else {
			customerLegalEntityProductResponseDto
					.setSple(spleCountryList.get(0).getServiceProviderLegalEntity().getEntityName());
			customerLegalEntityProductResponseDto
					.setServiceProviderId(spleCountryList.get(0).getServiceProviderLegalEntity().getId());
		}
	}

	/**
	 * getSupLegalEntityAgainstCustomerCountry
	 *
	 * @param country
	 * @param customerLegalEntityProductResponseDto
	 */
	private void getSupLegalEntityAgainstCustomerCountry(MstCountry country,
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto) throws TclCommonException {
		List<SpLeCountry> spleCountryList = null;

		List<MstCountry> mstCountryList = mstCountryRepository.findByName(country.getName());
		if (mstCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.MST_COUNTRY_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		}
		spleCountryList = spleCountryRepository.findByMstCountryAndIsDefault(mstCountryList.get(0), (byte) 1);

		if (spleCountryList == null || spleCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.SP_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		} else {
			customerLegalEntityProductResponseDto
					.setSple(spleCountryList.get(0).getServiceProviderLegalEntity().getEntityName());
			customerLegalEntityProductResponseDto
					.setServiceProviderId(spleCountryList.get(0).getServiceProviderLegalEntity().getId());
		}
	}

	/**
	 * checkForSiteEquality
	 *
	 * @param siteCountry
	 * @return
	 */
	private boolean checkForSiteEquality(List<SiteCountryBean> siteCountry) {

		Map<String, Integer> siteCountryMap = new HashMap<>();
		siteCountry.forEach(site -> {
			if (siteCountryMap.containsKey(site.getCountry())) {
				siteCountryMap.put(site.getCountry(), siteCountryMap.get(site.getCountry()) + 1);
			} else {
				siteCountryMap.put(site.getCountry(), 1);
			}
		});

		return (siteCountryMap.size() == 1);
	}

	/**
	 * isSiteAndCuLeSame
	 *
	 * @param country
	 * @param siteCountry
	 */
	private boolean isSiteAndCuLeSame(MstCountry country, List<SiteCountryBean> siteCountry) {
		for (SiteCountryBean siteCountryBean : siteCountry) {
			if (siteCountryBean.getCountry().equalsIgnoreCase(country.getName())) {
				return true;
			}

		}

		return false;

	}

	/**
	 * validateRequest
	 *
	 * @param request
	 */
	private void validateRequest(CustomerLegalEntityRequestBean request) throws TclCommonException {

		if (request.getCustomerLegalEntityId() == 0 || request.getSiteCountry().isEmpty()) {

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		}

	}

	public CustomerLeContactDetailsBean getContactDetailsForTheCustomerLeId(Integer customerLeId)
			throws TclCommonException {
		if (customerLeId == null) {
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		CustomerLeContactDetailsBean contactDetailsBean = new CustomerLeContactDetailsBean();
		Optional<CustomerLegalEntity> customerLe = customerLegalEntityRepository.findById(customerLeId);
		if (customerLe.isPresent()) {
			contactDetailsBean.setCustomerLeName(customerLe.get().getEntityName());
			List<CustomerLeContact> contacts = customerLeContactRepository.findByCustomerLeId(customerLeId);
			List<CustomerLeContactBean> contactDetails = new ArrayList<>();
			if (contacts != null && !contacts.isEmpty()) {
				contacts.stream().forEach(customerLeContact -> {
					contactDetails.add(constructLeContactBean(customerLeContact));
				});
			}
			contactDetailsBean.setContacts(contactDetails);
		} else {
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		return contactDetailsBean;
	}

	/**
	 * This method updates the contact details of a custpmerLegalEntity
	 * 
	 * @param customerId
	 * @param customerLeId
	 * @param leContactBean
	 * @throws TclCommonException
	 */
	public void updateContactDetailsForTheCustomerLeId(Integer customerId, Integer customerLeId,
			CustomerLeContactBean leContactBean) throws TclCommonException {

		if (customerId == null || customerLeId == null || leContactBean.getContactId() == null) {
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		Optional<CustomerLeContact> customerLeContactOpt = customerLeContactRepository
				.findById(leContactBean.getContactId());
		if (contactExistsLeContact(leContactBean, customerLeContactOpt.get())) {
			throw new TclCommonException(ExceptionConstants.BILLING_CONTACT_EXISTS);
		}
		if (validateLeAddress(customerLeContactOpt.get())) {
			throw new TclCommonException(ExceptionConstants.LE_ADDRESS_EMPTY);
		}
		try {
			CustomerLeContact customerLeContact = new CustomerLeContact();
			Optional<Customer> customer = customerRepository.findById(customerId);
			if (customer.isPresent() && customerLeContactOpt.isPresent()) {
				customerLeContact = customerLeContactOpt.get();
				customerLeContact.setCustomerLeId(customerLeId);
				customerLeContact.setCustomer(customer.get());
				if (!StringUtils.isEmpty(leContactBean.getCustomerLeContactName()))
					customerLeContact.setName(leContactBean.getCustomerLeContactName());
				if (!StringUtils.isEmpty(leContactBean.getMobileNumber()))
					customerLeContact.setMobilePhone(leContactBean.getMobileNumber());
				if (!StringUtils.isEmpty(leContactBean.getTitle()))
					customerLeContact.setTitle(leContactBean.getTitle());
				if (!StringUtils.isEmpty(leContactBean.getCustomerLeContactEmailid()))
					customerLeContact.setEmailId(leContactBean.getCustomerLeContactEmailid());
				customerLeContact = customerLeContactRepository.save(customerLeContact);
			} else {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_ERROR);
			}
			// GET BILLING ADDRESS FROM LOCATION SCHEMA

			MSTAddressDetails getAddress = new MSTAddressDetails();
			getAddress.setLocation_Le_Id(Integer.parseInt(customerLeContact.getErfloclocationid()));
			String attachmentrequest = Utils.convertObjectToJson(getAddress);
			String addresses = (String) mqUtils.sendAndReceive(customerbillingGetAddress, attachmentrequest);
			MSTAddressDetails mstAddressDetails = (MSTAddressDetails) Utils.convertJsonToObject(addresses,
					MSTAddressDetails.class);

			// Add updated le contact deatils to MDM API
			MDMOmsRequestBean mdmRequestBean = constructMdmBeanLeContact(customerLeContact, false,
					leContactBean.getFirstName(), leContactBean.getLastName(), mstAddressDetails,
					leContactBean.getQuoteCode());
			String cmdBillingUpdate = Utils.convertObjectToJson(mdmRequestBean);
			mqUtils.send(cmdBillUpdateQueue, cmdBillingUpdate);

			// update le contact details in cof instead of customer name
			String Contact = updateOmsLeContact(leContactBean.getQuotetoLeId(), LeAttributesConstants.LE_CONTACT,
					customerLeContact.getMobilePhone());
			String Email = updateOmsLeContact(leContactBean.getQuotetoLeId(), LeAttributesConstants.LE_EMAIL,
					customerLeContact.getEmailId());
			String address = updateOmsLeContact(leContactBean.getQuotetoLeId(), LeAttributesConstants.LE_NAME,
					customerLeContact.getName());

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		}

	}

	/**
	 * 
	 * getContactDetailsForEmailIdConsumer function is used to get contact details
	 * for the emailId
	 * 
	 * @param emailId
	 * @return
	 */
	public CustomerContactDetails getContactDetailsForEmailIdConsumer(String emailId) {
		LOGGER.info("Inside method getContactDetailsForEmailIdConsumer");
		CustomerContactDetails contactDetailsBean = new CustomerContactDetails();
		List<CustomerLeContact> customerContacts = customerLeContactRepository.findByEmailIdAndContactTypeIsNull(emailId);
		LOGGER.info("Customer contact where contact type null {}", customerContacts.toString());
		constructCustomerContact(contactDetailsBean, customerContacts);
		if(StringUtils.isBlank(contactDetailsBean.getEmailId())) {
			LOGGER.info("Contact type not null");
			List<CustomerLeContact> customerContactsByEmail = customerLeContactRepository.findByEmailId(emailId);
			LOGGER.info("Customer contact where contact type null {}", customerContactsByEmail.toString());
			constructCustomerContact(contactDetailsBean, customerContactsByEmail);
		}
		LOGGER.info("Contact Bean {}",contactDetailsBean);
		return contactDetailsBean;
	}

	private void constructCustomerContact(CustomerContactDetails contactDetailsBean,
			List<CustomerLeContact> customerContacts) {
		if (!customerContacts.isEmpty()) {
			for (CustomerLeContact customerLeContact : customerContacts) {
				contactDetailsBean.setAddress(customerLeContact.getAddress());
				contactDetailsBean.setAssistantPhone(customerLeContact.getAssistantPhone());
				contactDetailsBean.setCustomerLeId(customerLeContact.getCustomerLeId());
				contactDetailsBean.setEmailId(customerLeContact.getEmailId());
				contactDetailsBean.setFaxNo(customerLeContact.getFaxNo());
				contactDetailsBean.setHomePhone(customerLeContact.getHomePhone());
				contactDetailsBean.setMobilePhone(customerLeContact.getMobilePhone());
				contactDetailsBean.setName(customerLeContact.getName());
				contactDetailsBean.setOtherPhone(customerLeContact.getOtherPhone());
				contactDetailsBean.setTitle(customerLeContact.getTitle());
				if(StringUtils.isNotBlank(customerLeContact.getMobilePhone()) || StringUtils.isNotBlank(customerLeContact.getOtherPhone())){
					LOGGER.info("Contact Id Selected is {}",customerLeContact.getId());
					break;
				}
			}
		}
	}

	public CustomerLeContactBean constructLeContactBean(CustomerLeContact customerLeContact) {
		CustomerLeContactBean customerLeContactBean = new CustomerLeContactBean();
		customerLeContactBean.setCustomerLeContactName(customerLeContact.getName());
		customerLeContactBean.setCustomerLeContactEmailid(customerLeContact.getEmailId());
		customerLeContactBean.setContactId(customerLeContact.getId());
		customerLeContactBean.setTitle(customerLeContact.getTitle());
		customerLeContactBean.setMobileNumber(customerLeContact.getMobilePhone());

		return customerLeContactBean;
	}

	/**
	 * getCustomerLeDataCenter
	 *
	 * @param customerId
	 * @return List of DataCenterBean
	 */
	public List<DataCenterBean> getCustomerLeDataCenter(Integer customerId) throws TclCommonException {
		List<DataCenterBean> dataCenterBeanList = null;
		Set<String> dataCenterIds = new HashSet<>();
		try {
			List<CustomerLegalEntity> customerLegalEntities = customerLegalEntityRepository
					.findByCustomerId(customerId);
			for (CustomerLegalEntity customerLegalEntity : customerLegalEntities) {
				List<CustomerLegalDataCenters> customerLegalDataCenters = customerLeDataCentersRepository
						.findByCustomerleId(customerLegalEntity.getId());
				if (customerLegalDataCenters != null && !customerLegalDataCenters.isEmpty())
					dataCenterIds
							.addAll(customerLegalDataCenters.stream().map(CustomerLegalDataCenters::getDataCenterId)
									.map(String::valueOf).collect(Collectors.toList()));
			}
			String dataCenterId = dataCenterIds.stream().collect(joining(","));
			LOGGER.info("MDC Filter token value in before Queue call getCustomerLeDataCenter {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			dataCenterBeanList = (List<DataCenterBean>) Utils.convertJsonToObject((String) mqUtils
					.sendAndReceive(productDataCenterqueue, dataCenterId),
					List.class);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return dataCenterBeanList;

	}

	public List<Map<Integer, String>> getCustomerLeByLeId(String[] customerLeIds) {
		List<Integer> customerLeIdList = new ArrayList<>();
		List<Map<Integer, String>> customerLeBeanList = new ArrayList<>();
		if (customerLeIds != null) {
			for (String id : customerLeIds) {
				String ids = id.replace("\"", "");
				customerLeIdList.add(Integer.valueOf(ids));
			}
			List<CustomerLegalEntity> customerLegalEntityList = customerLegalEntityRepository
					.findAllById(customerLeIdList);
			for (CustomerLegalEntity customerLegalEntity : customerLegalEntityList) {
				Map<Integer, String> customerLeBean = new HashMap<>();
				customerLeBean.put(customerLegalEntity.getId(), customerLegalEntity.getEntityName());
				customerLeBeanList.add(customerLeBean);
			}

		}

		return customerLeBeanList;
	}

	public List<LegalEntityBean> getLegalEntitiesForTheCustomers(List<Integer> customerIds) throws TclCommonException {
		List<LegalEntityBean> legalEntityList = new ArrayList<>();
		try {
			if (customerIds != null && !customerIds.isEmpty()) {
				List<CustomerLegalEntity> customerLeDetails = customerLegalEntityRepository
						.findByCustomerIdIn(customerIds);
				for (CustomerLegalEntity customerLegalEntity : customerLeDetails) {
					LegalEntityBean leBean = new LegalEntityBean();
					leBean.setCustomerId(customerLegalEntity.getCustomer().getId());
					leBean.setLegalEntityId(customerLegalEntity.getId());
					leBean.setLegalEntityName(customerLegalEntity.getEntityName());
					legalEntityList.add(leBean);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return legalEntityList;
	}

	/**
	 * createAttachment - upload files for MSA and SS documents
	 *
	 * @param referenceName
	 * @param customerLegalEntity
	 * @param fileUploadResponse
	 * @param mOptional
	 * @param newFolder
	 */
	private void createAttachment(String referenceName, Optional<CustomerLegalEntity> customerLegalEntity,
			CustomerAttachmentBean fileUploadResponse, String newFolder, MstLeAttribute mOptional, String productName,
			List<CustomerLeAttributeValue> leAttributeValues) {
		Attachment attachment = new Attachment();
		attachment.setDisplayName(referenceName);
		attachment.setName(referenceName);
		attachment.setUriPathOrUrl(newFolder);
		attachment.setCreatedTime(new Timestamp(new Date().getTime()));

		Attachment attachmen = attachmentRepository.save(attachment);
		if (!leAttributeValues.isEmpty()) {
			leAttributeValues.get(0).setAttributeValues(String.valueOf(attachmen.getId()));
			customerLeAttributeRepository.save(leAttributeValues.get(0));
		} else {
			CustomerLeAttributeValue attributeValue = new CustomerLeAttributeValue();
			if (customerLegalEntity.isPresent())
				attributeValue.setCustomerLegalEntity(customerLegalEntity.get());
			attributeValue.setMstLeAttribute(mOptional);
			attributeValue.setAttributeValues(String.valueOf(attachmen.getId()));
			attributeValue.setProductName(productName);
			customerLeAttributeRepository.save(attributeValue);
		}
		fileUploadResponse.setId(attachmen.getId());
		fileUploadResponse.setDisplayName(attachmen.getDisplayName());
		fileUploadResponse.setName(attachmen.getName());
		fileUploadResponse.setUriPath(attachmen.getUriPathOrUrl());
	}

	@Transactional
	public CustomerAttachmentBean uploadMSASSDocuments(MultipartFile file, Integer customerLeId, String referenceName,
			String attachmentType, String productName) throws TclCommonException {
		CustomerAttachmentBean fileUploadResponse = new CustomerAttachmentBean();
		Optional<Attachment> attachment = null;
		Attachment attachment2 = null;
		List<CustomerLeAttributeValue> leAttributeValues = new ArrayList<>();
		CustomerLeAttributeValue customerLeAttributeValue = null;
		try {
			if(Objects.nonNull(productName) && productName.equalsIgnoreCase(DocumentConstant.ALL)) {
					productName = null;
			} 
			Optional<MstLeAttribute> mOptional = mstLeAttributeRepository.findByName(attachmentType);

			Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(customerLeId);
			if (mOptional.isPresent()) {
				String newFolder = StringUtils.EMPTY;
				TempUploadUrlInfo tempUploadUrlInfo = null;
				MstLeAttribute mstLeAttribute = mOptional.get();
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
//					tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
//							Long.parseLong(tempUploadUrlExpiryWindow),
//							customerLegalEntity.get().getCustomer().getCustomerCode(),
//							customerLegalEntity.get().getCustomerLeCode(),false);
//					fileUploadResponse.setUriPath(tempUploadUrlInfo.getTemporaryUploadUrl());
//					fileUploadResponse.setName(tempUploadUrlInfo.getRequestId());

					if (file != null && customerLegalEntity.isPresent()) {
						Customer customer = customerLegalEntity.get().getCustomer();
						if (customer.getCustomerCode() == null) {
							customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
							customerRepository.save(customer);
						}
						if (customerLegalEntity.get().getCustomerLeCode() == null) {
							customerLegalEntity.get()
									.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
							customerLegalEntityRepository.save(customerLegalEntity.get());
						}
						InputStream inputStream = file.getInputStream();
						StoredObject storedObject = fileStorageService.uploadObject(file.getOriginalFilename(),
								inputStream, customer.getCustomerCode(), customerLegalEntity.get().getCustomerLeCode());
						String[] pathArray = storedObject.getPath().split("/");
						updateMSASSDocumentsUploadedToContainer(storedObject.getName(),
								customerLegalEntity.get().getId(), attachmentType, productName, pathArray[1]);
					}

				} else {
					LocalDateTime now = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);

					newFolder = uploadPath + now.format(formatter);
					File filefolder = new File(newFolder);
					if (!filefolder.exists()) {
						filefolder.mkdirs();

					}
					Path path = Paths.get(newFolder);
					Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));

					customerLeAttributeValue = getCustLeAttributeVal(customerLeId, productName,
							customerLeAttributeValue, mstLeAttribute);

					if (customerLeAttributeValue != null) {
						if (customerLeAttributeValue.getAttributeValues() != null
								&& !customerLeAttributeValue.getAttributeValues().isEmpty()) {

							attachment = attachmentRepository
									.findById(Integer.valueOf(customerLeAttributeValue.getAttributeValues()));

							if (attachment.isPresent()) {
								attachment.get().setUriPathOrUrl(newFolder);
								attachment.get().setName(referenceName);
								attachment.get().setDisplayName(referenceName);
								attachment2 = attachmentRepository.save(attachment.get());
								fileUploadResponse.setId(attachment2.getId());
								fileUploadResponse.setDisplayName(attachment2.getDisplayName());
								fileUploadResponse.setName(attachment2.getName());
							}

						} else {
							createAttachment(referenceName, customerLegalEntity, fileUploadResponse, newFolder,
									mstLeAttribute, productName, leAttributeValues);
						}

					} else {
						createAttachment(referenceName, customerLegalEntity, fileUploadResponse, newFolder,
								mstLeAttribute, productName, leAttributeValues);

					}
				}
			}

			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	private CustomerLeAttributeValue getCustLeAttributeVal(Integer customerLeId, String productName,
			CustomerLeAttributeValue customerLeAttributeValue, MstLeAttribute mOptional) {
		List<CustomerLeAttributeValue> leAttributeValueList = (List<CustomerLeAttributeValue>) customerLeAttributeRepository
				.findByCustomerLeIdAndMstLeAttributesIdAndProductName(customerLeId, mOptional.getId(), productName);

		if (leAttributeValueList != null && !leAttributeValueList.isEmpty()) {
			customerLeAttributeValue = getCustomerLeAttributeValByProdName(productName, customerLeAttributeValue,
					leAttributeValueList);
			if (customerLeAttributeValue == null) {
				Optional<CustomerLeAttributeValue> optCust = leAttributeValueList.stream()
						.filter(leVal -> Objects.isNull(leVal.getProductName()) || leVal.getProductName().isEmpty())
						.findFirst();
				if (optCust.isPresent()) {
					return optCust.get();
				}
			}
		}
		return customerLeAttributeValue;
	}

	public ServiceScheduleBean mapSSDocumentToLe(ServiceScheduleBean serviceScheduleBean) throws TclCommonException {
		if (serviceScheduleBean == null || serviceScheduleBean.getIsSSUploaded() == null
				|| serviceScheduleBean.getProductName() == null || serviceScheduleBean.getCustomerLeId() == null) {
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST, ResponseResource.R_CODE_ERROR);
		}
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {
			Integer customerLeId = serviceScheduleBean.getCustomerLeId();
			Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(customerLeId);
			if (!customerLegalEntity.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ResponseResource.R_CODE_ERROR);
			}
			if (serviceScheduleBean.getIsSSUploaded()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					if (customerLegalEntity.isPresent()) {
						Boolean alreadyAttached = false;
						Attachment attachmentAlreadyPresent = getAttachmentForSSIfPresent(customerLegalEntity.get(),
								serviceScheduleBean.getProductName());
						Attachment attachment = new Attachment();
						if (attachmentAlreadyPresent != null && attachmentAlreadyPresent.getId() != null) {
							attachment.setId(attachmentAlreadyPresent.getId());
							alreadyAttached = true;
						}
						Map<String, String> pathObjects = fileStorageService
								.getObjectsFromContainer(serviceScheduleBean.getProductName());
						if (pathObjects.size() == 0) {
							throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
									ResponseResource.R_CODE_ERROR);
						}
						String document = pathObjects.keySet().stream()
								.filter(documentName -> documentName.contains(("Service"))).findFirst().get();
						attachment.setDisplayName(document);
						attachment.setName(document);
						attachment.setUriPathOrUrl(pathObjects.get(document));
						attachment = attachmentRepository.save(attachment);
						if (attachment.getId() != null) {
							updateCustomerLeAttributeValue(attachment.getId(), customerLegalEntity.get(),
									serviceScheduleBean.getProductName(), alreadyAttached);
						}
					}
				} else {
					Boolean alreadyAttached = false;
					Attachment attachmentAlreadyPresent = getAttachmentForSSIfPresent(customerLegalEntity.get(),
							serviceScheduleBean.getProductName());
					Attachment attachment = new Attachment();
					if (attachmentAlreadyPresent != null && attachmentAlreadyPresent.getId() != null) {
						attachment.setId(attachmentAlreadyPresent.getId());
						alreadyAttached = true;
					}
					attachment.setDisplayName(serviceScheduleBean.getDisplayName());
					attachment.setName(serviceScheduleBean.getName());
					String uriPath = getUriPathBasedOnProduct(serviceScheduleBean.getProductName());
					if (uriPath == null) {
						throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
								ResponseResource.R_CODE_ERROR);
					}
					attachment.setUriPathOrUrl(uriPath);
					attachment = attachmentRepository.save(attachment);
					if (attachment.getId() != null) {
						updateCustomerLeAttributeValue(attachment.getId(), customerLegalEntity.get(),
								serviceScheduleBean.getProductName(), alreadyAttached);
					}
				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return serviceScheduleBean;
	}

	public String getUriPathBasedOnProduct(String productName) {
		switch (productName) {
		case "IAS":
			return iasSsPath;
		case "GVPN":
			return gvpnSsPath;
		case "GSIP":
			return gscSsPath;
		case "NPL":
			return nplSsPath;
		case "IZOPC":
			return izopcSsPath;
		case "GDE":
			return gdeBodSsLocation;
		default:
			break;
		}
		return null;
	}

	public MstLeAttribute getMstLeAttributeBySSUploadFlagName(String attributeName) {
		if (attributeName != null) {
			Optional<MstLeAttribute> mstLeAttribute = mstLeAttributeRepository
					.findByName(DocumentConstant.SS_UPLOADED_FLAG);
			if (mstLeAttribute.isPresent()) {
				return mstLeAttribute.get();
			} else {
				MstLeAttribute leAttribute = new MstLeAttribute();
				leAttribute.setName(DocumentConstant.SS_UPLOADED_FLAG);
				leAttribute.setDescription(DocumentConstant.SS_UPLOADED_FLAG);
				leAttribute.setStatus(CommonConstants.BACTIVE);
				leAttribute.setType(DocumentConstant.SERVICE_SCHEDULE_DOCUMENT);
				leAttribute = mstLeAttributeRepository.save(leAttribute);
				return leAttribute;
			}
		}
		return null;
	}

	public void updateCustomerLeAttributeValue(Integer attachmentId, CustomerLegalEntity customerLegalEntity,
			String productName, Boolean alreadyAttached) {
		MstLeAttribute ssUploadFlag = getMstLeAttributeBySSUploadFlagName(DocumentConstant.SS_UPLOADED_FLAG);
		if (ssUploadFlag != null) {
			customerLeAttributeValueRepository.saveAndFlush(
					constructCustomerLeAttributeValue(ssUploadFlag, customerLegalEntity, "true", productName));
		}
		Optional<MstLeAttribute> serviceSchedule = mstLeAttributeRepository
				.findByName(DocumentConstant.SERVICE_SCHEDULE_DOCUMENT);
		if (serviceSchedule.isPresent() && !alreadyAttached) {
			customerLeAttributeValueRepository.saveAndFlush(constructCustomerLeAttributeValue(serviceSchedule.get(),
					customerLegalEntity, Integer.toString(attachmentId), productName));
		}
	}

	public CustomerLeAttributeValue constructCustomerLeAttributeValue(MstLeAttribute mstLeAttribute,
			CustomerLegalEntity customerLegalEntity, String attributeValue, String productName) {
		CustomerLeAttributeValue customerLeAttributeValue = new CustomerLeAttributeValue();
		customerLeAttributeValue.setAttributeValues(attributeValue);
		customerLeAttributeValue.setCustomerLegalEntity(customerLegalEntity);
		customerLeAttributeValue.setMstLeAttribute(mstLeAttribute);
		customerLeAttributeValue.setProductName(productName);
		return customerLeAttributeValue;

	}

	public Attachment getAttachmentForSSIfPresent(CustomerLegalEntity customerLegalEntity, String productName) {
		Optional<MstLeAttribute> serviceSchedule = mstLeAttributeRepository
				.findByName(DocumentConstant.SERVICE_SCHEDULE_DOCUMENT);
		if (serviceSchedule.isPresent()) {
			List<CustomerLeAttributeValue> customerLeAttributeValues = customerLeAttributeValueRepository
					.findByCustomerLegalEntityAndProductNameAndMstLeAttribute(customerLegalEntity, productName,
							serviceSchedule.get());
			if (customerLeAttributeValues != null && !customerLeAttributeValues.isEmpty()) {
				CustomerLeAttributeValue customerLeAttributeValue = customerLeAttributeValues.get(0);
				if (customerLeAttributeValue != null && customerLeAttributeValue.getAttributeValues() != null) {
					String attachmentId = customerLeAttributeValue.getAttributeValues();
					Optional<Attachment> attachment = attachmentRepository.findById(Integer.valueOf(attachmentId));
					if (attachment.isPresent()) {
						return attachment.get();
					}
				}
			}
		}
		return null;
	}

	public String uploadSSDocument(MultipartFile file, String productName) throws TclCommonException {
		if (file == null || productName == null) {
			throw new TclCommonException(ExceptionConstants.DOC_NOT_FOUND, ResponseResource.R_CODE_BAD_REQUEST);
		}
		String filepath = null;
		try {
			TempUploadUrlInfo tempUploadUrlInfo = null;
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
						Long.parseLong(tempUploadUrlExpiryWindow), productName);
				filepath = tempUploadUrlInfo.getTemporaryUploadUrl();
			} else {
				String newFolder = getUriPathBasedOnProduct(productName);
				if (newFolder == null) {
					throw new TclCommonException(ExceptionConstants.DOC_NOT_FOUND, ResponseResource.R_CODE_BAD_REQUEST);
				}
				File filefolder = new File(newFolder);
				if (!filefolder.exists()) {
					filefolder.mkdirs();
				}
				if (filefolder.isDirectory()) {
					File[] listFiles = filefolder.listFiles();
					if (listFiles != null) {
						for (File files : listFiles) {
							Files.delete(files.toPath());
						}
					}
				}

				Path path = Paths.get(newFolder);
				Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
				filepath = newpath.toString();
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return filepath;
	}

	public CustomerLeDetailsBean getLeAttributesBasedOnProduct(
			CustomerLeAttributeRequestBean customerLeAttributeRequestBean) throws TclCommonException {
		CustomerLeDetailsBean omsDetailsBean = null;
		List<Attributes> attributes = new ArrayList<>();
		try {
			if (customerLeAttributeRequestBean.getCustomerLeId() == null
					&& customerLeAttributeRequestBean.getProductName() == null) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_BAD_REQUEST);
			}
			omsDetailsBean = new CustomerLeDetailsBean();
			Optional<CustomerLegalEntity> opcustomerLegalEntity = customerLegalEntityRepository
					.findById(customerLeAttributeRequestBean.getCustomerLeId());
			if (!opcustomerLegalEntity.isPresent()) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_ERROR);

			}
			CustomerLegalEntity cLegalEntity = opcustomerLegalEntity.get();
			omsDetailsBean.setAccounCuId(cLegalEntity.getTpsSfdcCuid());
			omsDetailsBean.setAccountId(cLegalEntity.getCustomer().getAccountId18());
			omsDetailsBean.setLegalEntityName(cLegalEntity.getEntityName());
			omsDetailsBean.setPreapprovedMrc(cLegalEntity.getPreApprovedMrc());
			omsDetailsBean.setPreapprovedNrc(cLegalEntity.getPreApprovedNrc());
			omsDetailsBean.setPreapprovedBillingMethod(cLegalEntity.getPreApprovedBillingMethod());
			omsDetailsBean.setPreapprovedPaymentTerm(cLegalEntity.getPreApprovedPaymentTerm());
			omsDetailsBean.setCreditCheckAccountType(cLegalEntity.getCreditCheckAccountType());
			omsDetailsBean.setCreditPreapprovedFlag(cLegalEntity.getCreditPreapprovedFlag());
			omsDetailsBean.setBlacklistStatus(cLegalEntity.getBlacklistStatus());
			List<CustomerLeAttributeValue> customerLeAttributeValueList = customerLeAttributeValueRepository
					.findByCustomerLeIdAndProductName(customerLeAttributeRequestBean.getCustomerLeId(),
							customerLeAttributeRequestBean.getProductName());

			List<CustomerLeBillingInfo> customerContacts = customerLeBillingInfoRepository
					.findByCustomerLegalEntity_IdAndIsactive(customerLeAttributeRequestBean.getCustomerLeId(), "Yes");
			if (!customerContacts.isEmpty()) {
				omsDetailsBean.setBillingContactId(customerContacts.get(0).getId());
			}

			if (customerLeAttributeValueList.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			for (CustomerLeAttributeValue customerLeAttributeValue : customerLeAttributeValueList) {
				Attributes attribute = new Attributes();
				attribute.setAttributeName(customerLeAttributeValue.getMstLeAttribute().getName());
				attribute.setAttributeValue(customerLeAttributeValue.getAttributeValues());
				attribute.setType(customerLeAttributeValue.getMstLeAttribute().getType());
				attributes.add(attribute);
			}
			String crnId=getCRNDetailsByCustomerLeId(customerLeAttributeRequestBean.getCustomerLeId());
			if(StringUtils.isNoneBlank(crnId)) {
				Attributes attribute = new Attributes();
				attribute.setAttributeName(CommonConstants.CRN_ID);
				attribute.setAttributeValue(crnId);
				attribute.setType(CommonConstants.CRN_ID);
				attributes.add(attribute);
			}
			omsDetailsBean.getAttributes().addAll(attributes);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return omsDetailsBean;
	}

	/**
	 * @param customerLeId
	 * @param CustomerLeBillingRequestBean
	 * @return CustomerLeBillingInfoDto
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 *            <p>
	 *            Use to edit the customer billing details like (Name,emailId and
	 *            phone number etc)
	 */
	public CustomerLeBillingInfoDto editBillingInfo(Integer custLegalId,
			CustomerLeBillingRequestBean customerLeBillingRequest) throws TclCommonException {

		String queueResponse = null;
		MSTAddressDetails mstAddressDetails = null;
		CustomerLeBillingInfoDto customerLeBillingInfoDto = null;
		try {
			// validate Empty feilds
			if (Objects.isNull(custLegalId) || Objects.isNull(customerLeBillingRequest))
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LE_VALIDATE_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			CustomerLeBillingInfo customerLeBillingInfo = null;
			CustomerLeBillingInfo customerLeBillingInfoUpdated = null;
			customerLeBillingInfo = customerLeBillingInfoRepository
					.findByIdAndCustomerLegalEntity_Id(customerLeBillingRequest.getBillingInfoId(), custLegalId);

			if (customerLeBillingRequest.getBillingInfoId() != null) {
				customerLeBillingInfoUpdated = copyEntityFields(customerLeBillingInfo);
			} else {
				customerLeBillingInfoUpdated = new CustomerLeBillingInfo();
				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(custLegalId);
				if (customerLegalEntity.isPresent()) {
					customerLeBillingInfoUpdated.setCustomerLegalEntity(customerLegalEntity.get());
					customerLeBillingInfoUpdated.setCustomerId(customerLegalEntity.get().getCustomer().getId());
					customerLeBillingInfoUpdated.setBillAccNo(customerLeBillingRequest.getBillAccNo());
					customerLeBillingInfoUpdated.setIsactive(CommonConstants.YES);
				}
			}
			if (customerLeBillingRequest.getUpdateField().equalsIgnoreCase("contact")) {
				// validate billing contact details address exist
				if (billingContactAddressValidate(customerLeBillingInfo)) {
					throw new IllegalArgumentException(ExceptionConstants.BILLING_CONTACT_ADDRESS_EMPTY_VALIDATION);
				}
				if (contactValidate(customerLeBillingInfoUpdated, customerLeBillingRequest))
					throw new IllegalArgumentException(ExceptionConstants.BILLING_CONTACT_VALIDATION_ERROR);
				if (contactExist(customerLeBillingInfoUpdated, customerLeBillingRequest))
					throw new IllegalArgumentException(ExceptionConstants.BILLING_CONTACT_EXISTS_VALIDATION);
				if (!StringUtils.isEmpty(customerLeBillingRequest.getFname()))
					customerLeBillingInfoUpdated.setFname(customerLeBillingRequest.getFname());
				if (!StringUtils.isEmpty(customerLeBillingRequest.getLname()))
					customerLeBillingInfoUpdated.setLname(customerLeBillingRequest.getLname());
				if (!StringUtils.isEmpty(customerLeBillingRequest.getEmailId()))
					customerLeBillingInfoUpdated.setEmailId(customerLeBillingRequest.getEmailId());
				if (!StringUtils.isEmpty(customerLeBillingRequest.getMobileNumber())) {
					customerLeBillingInfoUpdated.setMobileNumber(customerLeBillingRequest.getMobileNumber());
					customerLeBillingInfoUpdated.setPhoneNumber(customerLeBillingRequest.getMobileNumber());
				}
				// GET BILLING ADDRESS FROM LOCATION SCHEMA
				customerLeBillingInfoUpdated = customerLeBillingInfoRepository.save(customerLeBillingInfoUpdated);
				MSTAddressDetails getAddress = new MSTAddressDetails();
				getAddress.setLocation_Le_Id(Integer.parseInt(customerLeBillingInfoUpdated.getErfloclocationid()));
				String attachmentrequest = Utils.convertObjectToJson(getAddress);
				String addresses = (String) mqUtils.sendAndReceive(customerbillingGetAddress, attachmentrequest);
				if (StringUtils.isNotBlank(addresses)) {
					mstAddressDetails = (MSTAddressDetails) Utils.convertJsonToObject(addresses,
							MSTAddressDetails.class);
				}

			} else if (customerLeBillingRequest.getUpdateField().equalsIgnoreCase("address")) {
				CustomerLeBillingInfo customerLeBillingInfoNew = new CustomerLeBillingInfo();
				if (contactValidate(customerLeBillingInfoUpdated, customerLeBillingRequest))
					throw new IllegalArgumentException(ExceptionConstants.BILLING_CONTACT_EMPTY);
				if (contactExist(customerLeBillingInfoUpdated, customerLeBillingRequest))
					throw new IllegalArgumentException(ExceptionConstants.BILLING_CONTACT_EXISTS);
				if (!StringUtils.isEmpty(customerLeBillingRequest.getFname()))
					customerLeBillingInfoNew.setFname(customerLeBillingRequest.getFname());
				if (!StringUtils.isEmpty(customerLeBillingRequest.getLname()))
					customerLeBillingInfoNew.setLname(customerLeBillingRequest.getLname());
				if (!StringUtils.isEmpty(customerLeBillingRequest.getEmailId()))
					customerLeBillingInfoNew.setEmailId(customerLeBillingRequest.getEmailId());
				if (!StringUtils.isEmpty(customerLeBillingRequest.getMobileNumber())) {
					customerLeBillingInfoNew.setMobileNumber(customerLeBillingRequest.getMobileNumber());
					customerLeBillingInfoNew.setPhoneNumber(customerLeBillingRequest.getMobileNumber());
				}
				if (!StringUtils.isEmpty(customerLeBillingRequest.getBillAddr()))
					customerLeBillingInfoNew.setBillAddr(customerLeBillingRequest.getBillAddr());
				if (!StringUtils.isEmpty(customerLeBillingRequest.getCountry()))
					customerLeBillingInfoNew.setCountry(customerLeBillingRequest.getCountry());
				if (!StringUtils.isEmpty(custLegalId.toString())) {
					Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
							.findById(custLegalId);
					customerLeBillingInfoNew.setCustomerLegalEntity(customerLegalEntity.get());
					customerLeBillingInfoNew.setCustomerId(customerLegalEntity.get().getCustomer().getId());
					customerLeBillingInfoNew.setBillAccNo("");
					customerLeBillingInfoNew.setIsactive("Yes");
				}

				// ADD ADDRESS TO LOCATION SCHEMA
				mstAddressDetails = constructMstBean(customerLeBillingRequest,
						customerLeBillingInfoUpdated.getCustomerLegalEntity().getId());
				String attachmentrequest = Utils.convertObjectToJson(mstAddressDetails);
				String locationResponse = (String) mqUtils.sendAndReceive(customerbillingAddAddress, attachmentrequest);
				customerLeBillingInfoNew.setErfloclocationid(locationResponse);
				customerLeBillingInfoUpdated = customerLeBillingInfoRepository.save(customerLeBillingInfoNew);

				if (customerLeBillingRequest.getQuoteToLeId() != null) {
					queueResponse = updateOmsBillingAttribute(customerLeBillingRequest.getQuoteToLeId(),
							customerLeBillingInfoUpdated);
					if (Objects.isNull(queueResponse)) {
						throw new TclCommonException(ExceptionConstants.UPDATE_BILLING_ATTRIBUTE_OMS_ERROR, ResponseResource.R_CODE_ERROR);
					}
				}

			} else {
				throw new TclCommonException(ExceptionConstants.EDIT_BILLING_INFO_ERROR, ResponseResource.R_CODE_ERROR);
			}

			// Add Billing contact and address details to MDM
			if (mstAddressDetails != null) {
				MDMOmsRequestBean mdmRequestBean = constructMdmBean(customerLeBillingInfoUpdated, true,
						mstAddressDetails, customerLeBillingRequest.getQuoteCode());
				String cmdBillingUpdate = Utils.convertObjectToJson(mdmRequestBean);
				mqUtils.send(cmdBillUpdateQueue, cmdBillingUpdate);
			}

			customerLeBillingInfoDto = new CustomerLeBillingInfoDto(customerLeBillingInfoUpdated);

		} catch (Exception e) {

			throw new TclCommonException(ExceptionConstants.EDIT_BILLING_INFO_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerLeBillingInfoDto;
	}

	/**
	 * validate contact details
	 *
	 * @param CustomerLeBillingInfo
	 * @param CustomerLeBillingRequestBean
	 * @throws TclCommonException
	 */
	private boolean contactExists(CustomerLeBillingInfo billingInfo, CustomerLeBillingRequestBean billingRequest) {

		if (StringUtils.isEmpty(billingRequest.getFname()) || StringUtils.isEmpty(billingRequest.getLname())
				|| StringUtils.isEmpty(billingRequest.getMobileNumber())
				|| StringUtils.isEmpty(billingRequest.getEmailId())
				|| billingInfo.getEmailId().equalsIgnoreCase(billingRequest.getEmailId())) {
			return true;
		}
		return false;
	}

	private CustomerLeBillingInfo copyEntityFields(CustomerLeBillingInfo billingInfoOld) {

		CustomerLeBillingInfo billingInfoNew = new CustomerLeBillingInfo();
		billingInfoNew.setAddressSeq(billingInfoOld.getAddressSeq());
		billingInfoNew.setBillAccNo(billingInfoOld.getBillAccNo());
		billingInfoNew.setBillAddr(billingInfoOld.getBillAddr());
		billingInfoNew.setBillContactSeq(billingInfoOld.getBillContactSeq());
		billingInfoNew.setContactType(billingInfoOld.getContactType());
		billingInfoNew.setCountry(billingInfoOld.getCountry());
		billingInfoNew.setCustomerId(billingInfoOld.getCustomerId());
		billingInfoNew.setCustomerLegalEntity(billingInfoOld.getCustomerLegalEntity());
		billingInfoNew.setEmailId(billingInfoOld.getEmailId());
		billingInfoNew.setFname(billingInfoOld.getFname());
		billingInfoNew.setIsactive(billingInfoOld.getIsactive());
		billingInfoNew.setLname(billingInfoOld.getLname());
		billingInfoNew.setMobileNumber(billingInfoOld.getMobileNumber());
		billingInfoNew.setPhoneNumber(billingInfoOld.getPhoneNumber());
		billingInfoNew.setTitle(billingInfoOld.getTitle());
		billingInfoNew.setContactId(billingInfoOld.getContactId());
		billingInfoNew.setErfloclocationid(billingInfoOld.getErfloclocationid());

		return billingInfoNew;
	}

	private String updateOmsBillingAttribute(Integer quoteToLeId, CustomerLeBillingInfo customerLeBillingInfo) {
		String response = null;
		OmsLeAttributeBean omsLeAttributeBean = new OmsLeAttributeBean();
		omsLeAttributeBean.setQuoteToLeId(quoteToLeId);
		omsLeAttributeBean.setAttrName("Billing Address");
		omsLeAttributeBean.setAttrValue(customerLeBillingInfo.getBillAddr());
		omsLeAttributeBean.setUserName(Utils.getSource());
		try {
			response = (String) mqUtils.sendAndReceive(updateOmsBillingAttribute,
					Utils.convertObjectToJson(omsLeAttributeBean));
		} catch (TclCommonException e) {
			LOGGER.error("error in process update oms billing attribute", e);
		}
		return response;
	}

	/**
	 * Upload Country Specific Files
	 *
	 * @param file
	 * @param productName
	 * @param countryName
	 * @return {@link ServiceResponse}
	 * @throws TclCommonException
	 */
	@Transactional
	public ServiceResponse uploadCountrySpecificFiles(MultipartFile file, String countryName, String productName)
			throws TclCommonException {
		Objects.requireNonNull(countryName);
		Objects.requireNonNull(productName);
		ServiceResponse fileUploadResponse = new ServiceResponse();
		TempUploadUrlInfo tempUploadUrlInfo = null;
		String documentId = getDocumentID(file, countryName, productName);
		if (Objects.nonNull(documentId)) {
			try {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					String erfCustomerAttachmentid = getErfCustomerAttachmentIdByDocument(null, documentId + "_OBJECT",
							ATTACHMENT_TYPE);
					StoredObject storedObject = fileStorageService.uploadFiles(file.getOriginalFilename(),
							"GSIP_COUNTRY_FILES_" + productName, file.getInputStream());
					if (storedObject != null && storedObject.getURL() != null && !storedObject.getURL().isEmpty()) {
						String objectStorageUrl = storedObject.getURL().substring(
								storedObject.getURL().indexOf(swiftApiContainer),
								storedObject.getURL().lastIndexOf("/"));
						objectStorageUrl = objectStorageUrl.replaceAll("%2F", "/").replaceAll("%20", " ");
						String updatedFileName = storedObject.getName();
						if (Objects.nonNull(objectStorageUrl)) {
							createAttachmentEntry(updatedFileName, fileUploadResponse, documentId + "_OBJECT",
									erfCustomerAttachmentid, objectStorageUrl);
						}
					}
				} else {
					String erfCustomerAttachmentid = getErfCustomerAttachmentIdByDocument(null, documentId,
							ATTACHMENT_TYPE);
					LocalDateTime now = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);

					String newFolder = uploadPath + now.format(formatter);
					File filefolder = new File(newFolder);
					if (!filefolder.exists()) {
						filefolder.mkdirs();
					}
					Path path = Paths.get(newFolder);
					Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
					if (Objects.nonNull(newpath)) {
						createAttachmentEntry(file.getOriginalFilename(), fileUploadResponse, documentId,
								erfCustomerAttachmentid, newFolder);
					}
				}
			} catch (Exception ex) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
			}
		}
		return fileUploadResponse;
	}

	private void createAttachmentEntry(String originalFileName, ServiceResponse fileUploadResponse, String documentId,
			String erfCustomerAttachmentid, String path) throws TclCommonException {
		if (Objects.isNull(erfCustomerAttachmentid)) {
			com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
			attachmentBean.setPath(path);
			attachmentBean.setFileName(originalFileName);
			Integer attachmentId = attachmentService.processAttachment(attachmentBean);
			fileUploadResponse.setAttachmentId(attachmentId);
			fileUploadResponse.setFileName(originalFileName);
			fileUploadResponse.setStatus(Status.SUCCESS);
			fileUploadResponse.setUrlPath(path);
			fileUploadResponse.setAttachmentId(attachmentId);
			getErfCustomerAttachmentIdByDocument(attachmentId, documentId, ATTACHMENT_TYPE);
		} else {
			Optional<Attachment> attachment = attachmentRepository.findById(Integer.valueOf(erfCustomerAttachmentid));
			attachment.get().setDisplayName(originalFileName);
			attachment.get().setName(originalFileName);
			attachment.get().setUriPathOrUrl(path);
			attachmentRepository.save(attachment.get());
			fileUploadResponse.setFileName(originalFileName);
			fileUploadResponse.setStatus(Status.SUCCESS);
			fileUploadResponse.setUrlPath(path);
			fileUploadResponse.setAttachmentId(Integer.valueOf(erfCustomerAttachmentid));
		}
	}

	private String getDocumentID(MultipartFile file, String countryName, String productName) throws TclCommonException {
		GscCountrySpecificDocumentBean gscCountrySpecificDocumentBean = new GscCountrySpecificDocumentBean();
		String documentId = "";
		if (Objects.nonNull(file)) {
			String fileNameWithOutExt = FilenameUtils.removeExtension(file.getOriginalFilename());
			gscCountrySpecificDocumentBean.setDocumentName(fileNameWithOutExt);
			gscCountrySpecificDocumentBean.setCountryName(countryName);
			gscCountrySpecificDocumentBean.setProductName(productName);
			GscCountrySpecificDocumentListener documentListenerBean = new GscCountrySpecificDocumentListener();
			documentListenerBean.setGscCountrySpecificDocumentBean(gscCountrySpecificDocumentBean);
			String documentListenterRequest = Utils.convertObjectToJson(documentListenerBean);
			LOGGER.info("MDC Filter token value in before Queue call uploadCountrySpecificFiles {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			documentId = (String) mqUtils.sendAndReceive(DocumentIDQueue, documentListenterRequest);
			if (Objects.isNull(documentId)) {
				throw new TclCommonException(MQ_ERROR);
			}
		}
		return documentId;
	}

	private String getErfCustomerAttachmentIdByDocument(Integer attachmentId, String documentId, String attachmentType)
			throws TclCommonException {
		List<OmsAttachBean> omsAttachBeanList = new ArrayList<OmsAttachBean>() {
			{
				add(new OmsAttachBean(attachmentId, documentId, ATTACHMENT_TYPE));
			}
		};

		OmsListenerBean listenerBean = new OmsListenerBean();
		listenerBean.setOmsAttachBean(omsAttachBeanList);
		String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
		LOGGER.info("MDC Filter token value in before Queue call uploadCountrySpecificFiles {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String response = (String) mqUtils.sendAndReceive(omsGscAttachmentQueue, oattachmentrequest);
		return response;
	}

	/**
	 * Function to identify customer legal entity for given values
	 *
	 * @param customerLegalEntityId
	 * @param productName
	 * @param accessType
	 * @param serviceNames
	 * @return {@link CustomerLegalEntityProductResponseDto}
	 */
	public Set<CustomerLegalEntityProductResponseDto> getSupplierLegalEntityDetailsByCustomerLegalIdForService(
			Integer customerLegalEntityId, String productName, List<String> serviceNames) throws TclCommonException {
		Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = new HashSet<>();
		boolean isDomesticVoice = false;

		if (!ServiceSpecificConstant.GSIP.equals(productName)  && !ServiceSpecificConstant.UCAAS.equals(productName)
				&& !ServiceSpecificConstant.MICROSOFT_CLOUD_SOLUTIONS.equals(productName)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		try {
//			if (serviceNames.contains(ACANS) || serviceNames.contains(ACDTFS)) {
//				List<MstCountry> masterCountries = mstCountryRepository.findByName(INDIA);
//				customerLegalEntityProductResponseDtos = getSupplierCountries(masterCountries.stream().findFirst().get(),
//						customerLegalEntityProductResponseDtos, isDomesticVoice, serviceNames);
//			} else {
				CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findAllById(customerLegalEntityId);

				if (Objects.isNull(customerLegalEntity)) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				}
				MstCountry masterCountry = customerLegalEntity.getCustomerLeCountries().stream().findFirst().get()
						.getMstCountry();
				if (serviceNames.contains(DOMESTIC_VOICE)) {
					isDomesticVoice = true;
				}
				customerLegalEntityProductResponseDtos = getSupplierCountries(masterCountry,
						customerLegalEntityProductResponseDtos, isDomesticVoice,serviceNames,productName);
//			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new HashSet<CustomerLegalEntityProductResponseDto>(customerLegalEntityProductResponseDtos);
	}


	private Set<CustomerLegalEntityProductResponseDto> getSupplierCountries(MstCountry masterCountry,
			Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos, boolean isDomesticVoice,List<String> serviceNames,
																			String productName)
			throws TclCommonException {
		try {
			List<SpLeCountry> supplierCountries = spleCountryRepository
					.getSupplierByCountryAndProduct(masterCountry.getId(), productName);
			if (Objects.isNull(supplierCountries) || 0 == supplierCountries.size()) {
				LOGGER.warn("Supplier Legal Entity is not available");
			} else {

				/*Getting distinct supplier legal entity when same is present when product name is present and is null*/
				List<ServiceProviderLegalEntity> serviceProviderLegalEntities = supplierCountries.stream().map(spLeCountry -> spLeCountry.getServiceProviderLegalEntity()).collect(Collectors.toList());
				List<ServiceProviderLegalEntity> distinctServerProviderLegalEntities = serviceProviderLegalEntities.stream().distinct().collect(Collectors.toList());

				distinctServerProviderLegalEntities.stream().forEach(serviceProviderLegalEntity -> {
					CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();

					String entityName = serviceProviderLegalEntity.getEntityName();
					Integer entityId = serviceProviderLegalEntity.getId();
					List<String> currency = currencyMasterRepository.findShortNameById(entityId);

					customerLegalEntityProductResponseDto.setSple(entityName);
					customerLegalEntityProductResponseDto.setServiceProviderId(entityId);
					if (isDomesticVoice) {
						try {
							customerLegalEntityProductResponseDto.setCurrency(getCurrencyDetails(masterCountry.getName()));
						} catch (TclCommonException e) {
							LOGGER.error("Error while getting the supplier currency details ", e);
						}
					} else {
						customerLegalEntityProductResponseDto.setCurrency(currency.stream().collect(Collectors.joining(",")));
					}
					if (serviceNames.contains(ACANS) || serviceNames.contains(ACLNS) || serviceNames.contains(ACDTFS)) {
						customerLegalEntityProductResponseDto.setLouRequired(false);
					} else {
						customerLegalEntityProductResponseDto.setLouRequired(true);
					}
					customerLegalEntityProductResponseDtos.add(customerLegalEntityProductResponseDto);
				});

			}
		} catch (Exception e) {
			LOGGER.error("Error while getting the supplier details ", e);
		}
		return customerLegalEntityProductResponseDtos;
	}


	private CustomerLeContactDetailBean beanFromCustomerLeContact(CustomerLeContact customerLeContact) {
		CustomerLeContactDetailBean bean = new CustomerLeContactDetailBean();
		bean.setId(customerLeContact.getId());
		bean.setName(customerLeContact.getName());
		bean.setAddress(customerLeContact.getAddress());
		bean.setEmailId(customerLeContact.getEmailId());
		bean.setHomePhone(customerLeContact.getHomePhone());
		bean.setMobilePhone(customerLeContact.getMobilePhone());
		bean.setOtherPhone(customerLeContact.getOtherPhone());
		bean.setFax(customerLeContact.getFaxNo());
		bean.setTitle(customerLeContact.getTitle());
		return bean;
	}

	public List<CustomerLeContactDetailBean> getContactDetailsByCustomerLeId(Integer customerLeId)
			throws TclCommonException {
		if (Objects.isNull(customerLeId)) {
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Customer ID from request is ---> {} ", customerLeId);
		Optional<CustomerLegalEntity> customerLe = customerLegalEntityRepository.findById(customerLeId);
		if (customerLe.isPresent()) {
			List<CustomerLeContact> contacts = customerLeContactRepository.findByCustomerLeIdContactTypeIsNull(customerLeId);
			if(Objects.nonNull(contacts) && !contacts.isEmpty()){
				LOGGER.info("Contacts list size is --> {} and id for index 0 is ----> {} ", contacts.size(), contacts.get(0).getId());
			}
			return Optional.ofNullable(contacts).orElse(ImmutableList.of()).stream()
					.map(this::beanFromCustomerLeContact).collect(Collectors.toList());
		} else {
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_ERROR);
		}
	}

	public Resource getAttachments(Integer attachmentId) throws TclCommonException {
		Resource resource = null;
		try {
			Objects.requireNonNull(attachmentId, "AttachmentId is required");
			Optional<Attachment> attachmentRepo = attachmentRepository.findById(attachmentId);
			if (attachmentRepo.isPresent()) {
				LOGGER.info("Path received :: {}", attachmentRepo.get().getUriPathOrUrl());
				File[] files = new File(attachmentRepo.get().getUriPathOrUrl()).listFiles();
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
					return resource;
				} else {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
			}
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing download malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return resource;
	}

	/**
	 * Get temp down load url for country documents for GSIP.
	 * 
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	public String getAttachmentTempDownloadUrl(Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = "";
		Optional<Attachment> attachmentRepo = attachmentRepository.findById(attachmentId);
		if (attachmentRepo.isPresent()) {
			tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentRepo.get().getDisplayName(),
					Long.parseLong(tempDownloadUrlExpiryWindow), attachmentRepo.get().getUriPathOrUrl(), false);
		}
		return tempDownloadUrl;
	}

	/**
	 * Method to add/update a gst number against a le address
	 *
	 * @param customerId
	 * @param customerLeId
	 * @param gstNo
	 * @return
	 * @throws TclCommonException
	 */
	public String saveOrUpdateLeGst(Integer customerId, Integer customerLeId, String gstNo) throws TclCommonException {

		String response = null;
		if (Objects.isNull(customerId) || Objects.isNull(customerLeId) || Objects.isNull(gstNo))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		Optional<CustomerLegalEntity> customerLE = customerLegalEntityRepository.findById(customerLeId);
		if (!customerLE.isPresent())
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_ERROR);

		Optional<MstLeAttribute> mstLeAttribute = mstLeAttributeRepository.findByName(SpConstants.GST_NUMBER);
		if (mstLeAttribute.isPresent()) {
			List<CustomerLeAttributeValue> customerLeAttList = customerLeAttributeValueRepository
					.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLeId, mstLeAttribute.get().getId());
			if (!customerLeAttList.isEmpty()) {
				CustomerLeAttributeValue customerLeAttributeValue = customerLeAttList.get(0);
				customerLeAttributeValue.setAttributeValues(gstNo);
				customerLeAttributeValueRepository.save(customerLeAttributeValue);
			} else {
				CustomerLeAttributeValue customerLeAttributeValue = new CustomerLeAttributeValue();
				customerLeAttributeValue.setAttributeValues(gstNo);
				customerLeAttributeValue.setMstLeAttribute(mstLeAttribute.get());
				customerLeAttributeValue.setCustomerLegalEntity(customerLE.get());
				customerLeAttributeValueRepository.save(customerLeAttributeValue);
			}
		}

		return response;
	}

	/**
	 * Find Customer Entity based on given services
	 *
	 * @param customerId
	 * @param product
	 * @param serviceNames
	 * @param country
	 * @return {@link List<CustomerLegalEntityDto>}
	 */
	public List<CustomerLegalEntityDto> findCustomerEntityByCustomerIdForService(Integer customerId, String product,
			List<String> serviceNames, String country, String accessType) throws TclCommonException {

        LOGGER.info("Product Name :: {}",product);
		if (!ServiceSpecificConstant.GSIP.equals(product) && !ServiceSpecificConstant.UCAAS.equals(product)
				&& !ServiceSpecificConstant.MICROSOFT_CLOUD_SOLUTIONS.equals(product)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}

		List<CustomerLegalEntity> customerLegalEntities = null;
		List<CustomerLegalEntityDto> customerLegalEntityDtos = new ArrayList<>();

		// If dv/gvpn country is required..
		if (serviceNames.contains(DOMESTIC_VOICE) || MPLS.equalsIgnoreCase(accessType)) {
			Objects.requireNonNull(country, "Country cannot be null for Domestic Voice or GVPN");

			if (serviceNames.contains(ACANS) || serviceNames.contains(ACLNS)) {
				country = INDIA;
			}

			customerLegalEntities = findCustomerLegalEntityByIdAndCountry(customerId, country);
			List<CustomerLegalEntityDto> allCustomerLegalEntityDtos = getCustomerLegalEntites(customerLegalEntities, customerLegalEntityDtos, product);
			if(MPLS.equalsIgnoreCase(accessType) && serviceNames.contains(ACDTFS)){
				LOGGER.info("Access type is {} and service is ACDTFS and country is indonesia", accessType);
				customerLegalEntityDtos = allCustomerLegalEntityDtos.stream()
						.filter(customerLegalEntityDto ->
								!customerLegalEntityDto.getCountry().stream().findFirst().get().equalsIgnoreCase("Indonesia"))
						.collect(Collectors.toList());
			}
			return customerLegalEntityDtos;
		} else if (serviceNames.contains(ACANS) || serviceNames.contains(ACLNS) || serviceNames.contains(ACDTFS)) {
//			country = INDIA;
//			customerLegalEntities = findCustomerLegalEntityByIdAndCountry(customerId, country);
//			customerLegalEntityDtos = getCustomerLegalEntites(customerLegalEntities, customerLegalEntityDtos);
			//if public ip, no customer legal entity.
			//others, give list of legal entities except indonesia.
			List<CustomerLegalEntityDto> allCustomerLegalEntityDtos = findCustomerEntityByCustomerId(customerId, null);
			if (serviceNames.contains(ACDTFS) && "Indonesia".equalsIgnoreCase(country)) {
				if (PUBLIC_IP.equalsIgnoreCase(accessType)) {
					LOGGER.info("Access type is public ip and service is ACDTFS and country is indonesia");
					customerLegalEntityDtos =  allCustomerLegalEntityDtos.stream()
							.filter(customerLegalEntityDto ->
									!(customerLegalEntityDto.getCountry().stream().findFirst().get().equalsIgnoreCase("Indonesia")
									|| customerLegalEntityDto.getCountry().stream().findFirst().get().equalsIgnoreCase("India")))
							.collect(Collectors.toList());
				} else {
					LOGGER.info("Access type is {} and service is ACDTFS and country is indonesia", accessType);
					customerLegalEntityDtos = allCustomerLegalEntityDtos.stream()
							.filter(customerLegalEntityDto ->
									!customerLegalEntityDto.getCountry().stream().findFirst().get().equalsIgnoreCase("Indonesia"))
							.collect(Collectors.toList());
				}
			}
			return allCustomerLegalEntityDtos;
		}
		 else if (PSTN.equalsIgnoreCase(accessType) || NNI.equalsIgnoreCase(accessType)) {
			customerLegalEntityDtos = findCustomerEntityByCustomerId(customerId,null);
		} else if (PUBLIC_IP.equalsIgnoreCase(accessType) || DEDICATED.equalsIgnoreCase(accessType)) {
			if(ServiceSpecificConstant.MICROSOFT_CLOUD_SOLUTIONS.equals(product)){
				customerLegalEntityDtos = findCustomerEntityByCustomerId(customerId,null);
			}else {
				country = INDIA;
				customerLegalEntities = findCustomerLegalEntityByIdNotCountry(customerId, country);
				customerLegalEntityDtos = getCustomerLegalEntites(customerLegalEntities, customerLegalEntityDtos, product);
			}
			return customerLegalEntityDtos;
		} else {
			// TODO: Send mail or throw exception
			return customerLegalEntityDtos;
		}

		return customerLegalEntityDtos;
	}

	private List<CustomerLegalEntity> findCustomerLegalEntityByIdAndCountry(Integer customerId, String country) {
		return customerLegalEntityRepository.findAllByGivenCountry(customerId, country);
	}

	private List<CustomerLegalEntity> findCustomerLegalEntityByIdNotCountry(Integer customerId, String country) {
		return customerLegalEntityRepository.findAllExceptGivenCountry(customerId, country);
	}

	/*private List<CustomerLegalEntityDto> createCustomerLegalEntityDto(List<CustomerLegalEntity> customerLegalEntities)
			throws TclCommonException {
		List<CustomerLegalEntityDto> customerLegalEntityDtos = new ArrayList<>();
		if (CollectionUtils.isEmpty(customerLegalEntities)) {
			LOGGER.error("Customer Legal Entity is no available");
		}
		getCustomerLegalEntites(customerLegalEntities, customerLegalEntityDtos);
		return customerLegalEntityDtos;
	}*/

	/**
	 * Method to update the attachments and the customer le attribute values once
	 * the file is uploaded to the storage container
	 *
	 * @param requestId
	 * @param customerLeId
	 * @param referenceName
	 * @param attachmentType
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */

	public String updateMSASSDocumentsUploadedToContainer(String requestId, Integer customerLeId, String attachmentType,
			String productName, String url) throws TclCommonException {
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (Objects.isNull(requestId) || Objects.isNull(customerLeId) || Objects.isNull(attachmentType)
						|| Objects.isNull(url))
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

				CustomerAttachmentBean fileUploadResponse = new CustomerAttachmentBean();
				Optional<Attachment> attachment = null;
				Attachment attachment2 = null;
				List<CustomerLeAttributeValue> leAttributeValues = new ArrayList<>();
				CustomerLeAttributeValue customerLeAttributeValue = null;
				Optional<MstLeAttribute> mOptional = mstLeAttributeRepository.findByName(attachmentType);

				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
						.findById(customerLeId);
				if (mOptional.isPresent()) {
					MstLeAttribute mstLeAttribute = mOptional.get();

					customerLeAttributeValue = getCustLeAttributeVal(customerLeId, productName,
							customerLeAttributeValue, mstLeAttribute);

					if (customerLeAttributeValue != null) {
						if (customerLeAttributeValue.getAttributeValues() != null
								&& !customerLeAttributeValue.getAttributeValues().isEmpty()) {

							attachment = attachmentRepository
									.findById(Integer.valueOf(customerLeAttributeValue.getAttributeValues()));

							if (attachment.isPresent()) {
								attachment.get().setName(requestId);
								attachment.get().setDisplayName(requestId);
								attachment.get().setUriPathOrUrl(url);
								attachment2 = attachmentRepository.save(attachment.get());
								fileUploadResponse.setId(attachment2.getId());
								fileUploadResponse.setDisplayName(attachment2.getDisplayName());
								fileUploadResponse.setName(attachment2.getName());
							}

						} else {
							createAttachment(requestId, customerLegalEntity, fileUploadResponse, url, mstLeAttribute,
									productName, leAttributeValues);
						}

					} else {
						createAttachment(requestId, customerLegalEntity, fileUploadResponse, url, mstLeAttribute,
								productName, leAttributeValues);

					}
				}
			}

		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return null;
	}

	/**
	 * Method to generate the temporary download url for documents uploaded to the
	 * storage container
	 *
	 * @param customerLeId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */

	public String getTempDownloadUrl(Integer customerLeId, Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (attachmentId == null || attachmentId == 0) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				if (validateAttachmentId(customerLeId, attachmentId)) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
				if (attachment.isPresent()) {
					String requestId = attachment.get().getName();

					tempDownloadUrl = fileStorageService.getTempDownloadUrl(requestId,
							Long.parseLong(tempDownloadUrlExpiryWindow), attachment.get().getUriPathOrUrl(), false);
					// attachment.get().setExpiryWindow(Long.parseLong(tempDownloadUrlExpiryWindow));
					// attachment.get().setUriPathOrUrl(tempDownloadUrl);
					// attachmentRepository.save(attachment.get());

				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return tempDownloadUrl;
	}

	public ServiceResponse updateDocumentUploadedDetails(Integer orderLeId, Integer quoteLeId,
			List<Integer> referenceId, String referenceName, String requestId, String attachmentType, String url)
			throws TclCommonException {

		com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
		ServiceResponse fileUploadResponse = new ServiceResponse();
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (Objects.isNull(requestId) || Objects.isNull(referenceId) || Objects.isNull(attachmentType)
						|| Objects.isNull(referenceName) || Objects.isNull(url))
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				attachmentBean.setFileName(requestId);
				attachmentBean.setPath(url);
				Integer attachmentId = attachmentService.processAttachment(attachmentBean);
				if (attachmentId == null)
					throw new TclCommonException(ExceptionConstants.ATTACHMENT_ID_MISSING,
							ResponseResource.R_CODE_ERROR);
				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				for (Integer refId : referenceId) {
					OmsAttachBean omsAttachBean = new OmsAttachBean();
					omsAttachBean.setAttachmentId(attachmentId);
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
					omsAttachBean.setOrderLeId(orderLeId);
					omsAttachBean.setQouteLeId(quoteLeId);
					omsAttachBean.setReferenceId(refId);
					omsAttachBean.setReferenceName(referenceName);
					omsAttachBean.setFileName(requestId);
					omsAttachBean.setPath(url);
					omsAttachBeanList.add(omsAttachBean);
				}

				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(omsAttachmentQueue, oattachmentrequest);
				fileUploadResponse.setAttachmentId(attachmentId);
				fileUploadResponse.setFileName(requestId);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return fileUploadResponse;

	}

	/**
	 * getTempDownloadUrlForDocuments - Method to generate the temporary download
	 * url for documents uploaded to the storage container
	 *
	 * @param customerLeId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */

	public String getTempDownloadUrlForDocuments(Integer customerLeId, Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (attachmentId == null || attachmentId == 0) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
				if (attachment.isPresent()) {

					String requestId = attachment.get().getName();
					tempDownloadUrl = fileStorageService.getTempDownloadUrl(requestId,
							Long.parseLong(tempDownloadUrlExpiryWindow), attachment.get().getUriPathOrUrl(), false);
					// attachment.get().setExpiryWindow(Long.parseLong(tempDownloadUrlExpiryWindow));
					// attachment.get().setUriPathOrUrl(tempDownloadUrl);
					// attachmentRepository.save(attachment.get());

				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return tempDownloadUrl;

	}

	public String generatePassCode(Integer customerLeId) throws TclCommonException {
		String decryptedPassword = StringUtils.EMPTY;
		String encryptedPassword = StringUtils.EMPTY;

		try {
			encryptedPassword = EncryptionUtil.encrypt(customerLeId.toString());
			// decryptedPassword = EncryptionUtil.decrypt(encryptedPassword);
			// LOGGER.info("encrypted password ::{}, decrypted password :: {}",
			// encryptedPassword, decryptedPassword);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return encryptedPassword;

	}

	public void createContainer(Integer customerId, Integer customerLeId, String passcode) throws TclCommonException {
		try {
			String decryptedPasscode = EncryptionUtil.decrypt(passcode);
			if (decryptedPasscode.equalsIgnoreCase(customerLeId.toString())) {
				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
						.findById(customerLeId);
				if (customerLegalEntity.isPresent()) {
					Customer customer = customerLegalEntity.get().getCustomer();
					if (customer.getCustomerCode() == null) {
						customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
						customerRepository.save(customer);
					}
					if (customerLegalEntity.get().getCustomerLeCode() == null) {
						customerLegalEntity.get()
								.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
						customerLegalEntityRepository.save(customerLegalEntity.get());
					}
					fileStorageService.createStorageContainer(customer.getCustomerCode(),
							customerLegalEntity.get().getCustomerLeCode());
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * Mapping MSA documents to legal entity
	 *
	 * @param msaBean
	 * @return MSABean
	 */
	public MSABean mapMSADocumentToLe(MSABean msaBean) throws TclCommonException {
		if (msaBean == null || msaBean.getIsMSAUploaded() == null || msaBean.getProductName() == null
				|| msaBean.getCustomerLeId() == null) {
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST, ResponseResource.R_CODE_ERROR);
		}
		try {
			Integer customerLeId = msaBean.getCustomerLeId();
			Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(customerLeId);
			if (!customerLegalEntity.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ResponseResource.R_CODE_ERROR);
			}
			if (msaBean.getIsMSAUploaded()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					if (customerLegalEntity.isPresent()) {
						Boolean alreadyAttached = false;
						Attachment attachmentAlreadyPresent = getAttachmentForSSIfPresent(customerLegalEntity.get(),
								msaBean.getProductName());
						Attachment attachment = new Attachment();
						if (attachmentAlreadyPresent != null && attachmentAlreadyPresent.getId() != null) {
							attachment.setId(attachmentAlreadyPresent.getId());
							alreadyAttached = true;
						}
						Map<String, String> pathObjects = fileStorageService
								.getObjectsFromContainer(msaBean.getProductName());
						if (pathObjects.size() == 0) {
							throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
									ResponseResource.R_CODE_ERROR);
						}
						String document = pathObjects.keySet().stream()
								.filter(documentName -> documentName.contains(("MSA"))).findFirst().get();
						attachment.setDisplayName(document);
						attachment.setName(document);
						attachment.setUriPathOrUrl(pathObjects.get(document));
						attachment = attachmentRepository.save(attachment);
						if (attachment.getId() != null) {
							updateCustomerLeAttributeValue(attachment.getId(), customerLegalEntity.get(),
									msaBean.getProductName(), alreadyAttached);
						}
					}
				} else {
					Boolean alreadyAttached = false;
					Attachment attachmentAlreadyPresent = getAttachmentForMSAIfPresent(customerLegalEntity.get(),
							msaBean.getProductName());
					Attachment attachment = new Attachment();
					if (attachmentAlreadyPresent != null && attachmentAlreadyPresent.getId() != null) {
						attachment.setId(attachmentAlreadyPresent.getId());
						alreadyAttached = true;
					}
					attachment.setDisplayName(msaBean.getDisplayName());
					attachment.setName(msaBean.getName());
					String uriPath = getUriPathForMSABasedOnProduct(msaBean.getProductName());
					if (uriPath == null) {
						throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
								ResponseResource.R_CODE_ERROR);
					}
					attachment.setUriPathOrUrl(uriPath);
					attachment = attachmentRepository.save(attachment);
					if (attachment.getId() != null) {
						updateCustomerLeAttributeValueForMSA(attachment.getId(), customerLegalEntity.get(),
								msaBean.getProductName(), alreadyAttached);
					}

				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return msaBean;
	}

	private Attachment getAttachmentForMSAIfPresent(CustomerLegalEntity customerLegalEntity, String productName) {
		Optional<MstLeAttribute> msaDetail = mstLeAttributeRepository.findByName(DocumentConstant.MSA_DOCUMENT);
		if (msaDetail.isPresent()) {
			List<CustomerLeAttributeValue> customerLeAttributeValues = customerLeAttributeValueRepository
					.findByCustomerLegalEntityAndProductNameAndMstLeAttribute(customerLegalEntity, productName,
							msaDetail.get());
			if (customerLeAttributeValues != null && !customerLeAttributeValues.isEmpty()) {
				CustomerLeAttributeValue customerLeAttributeValue = customerLeAttributeValues.get(0);
				if (customerLeAttributeValue != null && customerLeAttributeValue.getAttributeValues() != null) {
					String attachmentId = customerLeAttributeValue.getAttributeValues();
					Optional<Attachment> attachment = attachmentRepository.findById(Integer.valueOf(attachmentId));
					if (attachment.isPresent()) {
						return attachment.get();
					}
				}
			}
		}
		return null;
	}

	private String getUriPathForMSABasedOnProduct(String productName) {
		switch (productName) {
		case "IAS":
			return iasMSAPath;
		case "GVPN":
			return gvpnMSAPath;
		case "GSIP":
			return gscMSAPath;
		case "NPL":
			return nplMSAPath;
		case "IZOPC":
			return izopcMSAPath;
		case "GDE":
			return gdeBodMSAPath;
		default:
			break;
		}
		return null;
	}

	/**
	 * Mapping MSA documents to legal entity
	 *
	 * @param attachmentId
	 * @param customerLegalEntity
	 * @param productName
	 * @param alreadyAttached
	 */
	public void updateCustomerLeAttributeValueForMSA(Integer attachmentId, CustomerLegalEntity customerLegalEntity,
			String productName, Boolean alreadyAttached) {
		MstLeAttribute msaUploadFlag = getMstLeAttributeByMSAUploadFlagName(DocumentConstant.MSA_UPLOADED_FLAG);
		if (msaUploadFlag != null) {
			customerLeAttributeValueRepository.saveAndFlush(
					constructCustomerLeAttributeValue(msaUploadFlag, customerLegalEntity, "true", productName));
		}
		Optional<MstLeAttribute> msaDocument = mstLeAttributeRepository.findByName(DocumentConstant.MSA_DOCUMENT);
		if (msaDocument.isPresent() && !alreadyAttached) {
			customerLeAttributeValueRepository.saveAndFlush(constructCustomerLeAttributeValue(msaDocument.get(),
					customerLegalEntity, Integer.toString(attachmentId), productName));
		}
	}

	/**
	 * getting MSA is upload or not for legal entity
	 *
	 * @param attributeName
	 * @return MstLeAttribute
	 */
	public MstLeAttribute getMstLeAttributeByMSAUploadFlagName(String attributeName) {
		if (attributeName != null) {
			Optional<MstLeAttribute> mstLeAttribute = mstLeAttributeRepository
					.findByName(DocumentConstant.MSA_UPLOADED_FLAG);
			if (mstLeAttribute.isPresent()) {
				return mstLeAttribute.get();
			} else {
				MstLeAttribute leAttribute = new MstLeAttribute();
				leAttribute.setName(DocumentConstant.MSA_UPLOADED_FLAG);
				leAttribute.setDescription(DocumentConstant.MSA_UPLOADED_FLAG);
				leAttribute.setStatus(CommonConstants.BACTIVE);
				leAttribute.setType(DocumentConstant.MSA_DOCUMENT);
				leAttribute = mstLeAttributeRepository.save(leAttribute);
				return leAttribute;
			}
		}
		return null;
	}

	/**
	 * Upload MSA for legal entity
	 *
	 * @param file
	 * @param productName
	 * @return String
	 */
	public String uploadMSADocument(MultipartFile file, String productName) throws TclCommonException {
		if (file == null || productName == null) {
			throw new TclCommonException(ExceptionConstants.DOC_NOT_FOUND, ResponseResource.R_CODE_BAD_REQUEST);
		}
		String filepath = null;
		try {
			TempUploadUrlInfo tempUploadUrlInfo = null;
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
						Long.parseLong(tempUploadUrlExpiryWindow), productName);
				filepath = tempUploadUrlInfo.getTemporaryUploadUrl();
			} else {
				String newFolder = getUriPathForMSABasedOnProduct(productName);
				if (newFolder == null) {
					throw new TclCommonException(ExceptionConstants.DOC_NOT_FOUND, ResponseResource.R_CODE_BAD_REQUEST);
				}
				File filefolder = new File(newFolder);
				if (!filefolder.exists()) {
					filefolder.mkdirs();
				}
				if (filefolder.isDirectory()) {
					File[] listFiles = filefolder.listFiles();
					if (listFiles != null) {
						for (File files : listFiles) {
							Files.delete(files.toPath());
						}
					}
				}

				Path path = Paths.get(newFolder);
				Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
				filepath = newpath.toString();
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return filepath;
	}

	public CustomerLeListListBean getAllCustomerIdsWithLeIdsForUserGroupAll() {
		CustomerLeListListBean customerLeListListBean = new CustomerLeListListBean();
		List<CustomerLeListBean> customerLeListBeans = new ArrayList<>();
		List<CustomerLegalEntity> customerLegalEntities = customerLegalEntityRepository.findAll();
		customerLegalEntities.stream().forEach(le -> {
			CustomerLeListBean customerLeListBean = new CustomerLeListBean();
			if (le.getCustomer() != null) {
				customerLeListBean.setCustomerId(le.getCustomer().getId());
				customerLeListBean.setLeId(le.getId());
				customerLeListBeans.add(customerLeListBean);
			}
		});
		customerLeListListBean.setCustomerLeLists(customerLeListBeans);
		return customerLeListListBean;
	}

	/**
	 * Get currency based on country
	 *
	 * @param countryName
	 * @return {@link String}
	 * @throws TclCommonException
	 */
	public String getCurrencyDetails(String countryName) throws TclCommonException {
		Objects.requireNonNull(countryName, COUNTRY_NAME_IS_REQUIRED);
		String currency = mstCountriesCurrencyMasterRepository.findByCountryName(countryName);
		Objects.requireNonNull(currency, CURRENCY_NAME_IS_REQUIRED);
		return currency;
	}

	/**
	 * Save the uploaded document details in attachments
	 *
	 * @param requestId
	 * @param url
	 * @param documentId
	 * @return {@link ServiceResponse}
	 * @throws TclCommonException
	 */
	@Transactional
	public ServiceResponse uploadCountrySpecificFiles(String requestId, String url, String documentId)
			throws TclCommonException {
		ServiceResponse serviceResponse = new ServiceResponse();
		String erfCustomerAttachmentId = getErfCustomerAttachmentIdByDocument(null, documentId, ATTACHMENT_TYPE);
		if (Objects.nonNull(erfCustomerAttachmentId)) {
			Optional<Attachment> attachment = attachmentRepository.findById(Integer.valueOf(erfCustomerAttachmentId));
			attachment.get().setDisplayName(requestId);
			attachment.get().setName(requestId);
			attachment.get().setUriPathOrUrl(url);
			attachmentRepository.save(attachment.get());
			serviceResponse.setAttachmentId(attachment.get().getId());
		} else {
			com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
			attachmentBean.setPath(url);
			attachmentBean.setFileName(requestId);
			Integer attachmentId = attachmentService.processAttachment(attachmentBean);
			getErfCustomerAttachmentIdByDocument(attachmentId, documentId, ATTACHMENT_TYPE);
			serviceResponse.setAttachmentId(attachmentId);
		}
		return serviceResponse;
	}

	/**
	 * Update customer le attributes values
	 *
	 * @param customerLeId
	 * @param attributes
	 */
	@Transactional
	public List<CustomerLeAttributeBean> saveOrUpdateCustomerLeAttributes(Integer customerLeId,
			List<CustomerLeAttributeBean> attributes) {

		CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findById(customerLeId).get();

		if (!Objects.isNull(customerLegalEntity)) {
			for (CustomerLeAttributeBean attribute : attributes) {
				Optional.ofNullable(mstLeAttributeRepository.findByName(attribute.getAttributeName()))
						.ifPresent(mstLeAttribute -> {
							customerLeAttributeValueRepository
									.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLeId,
											mstLeAttribute.get().getId())
									.stream().findFirst()
									.map(customerLeAttributeValue -> updateCustomerLeAttributes(
											customerLeAttributeValue, attribute))
									.orElseGet(() -> createAndSaveCustomerLeAttribute(attribute, mstLeAttribute.get(),
											customerLegalEntity));
						});
			}
		}
		return attributes;
	}

	private CustomerLeAttributeBean createAndSaveCustomerLeAttribute(CustomerLeAttributeBean attributeBean,
			MstLeAttribute mstLeAttribute, CustomerLegalEntity customerLegalEntity) {
		CustomerLeAttributeValue customerLeAttributeValue = new CustomerLeAttributeValue();
		customerLeAttributeValue.setCustomerLegalEntity(customerLegalEntity);
		customerLeAttributeValue.setMstLeAttribute(mstLeAttribute);
		customerLeAttributeValue.setAttributeValues(attributeBean.getAttributeValue());
		customerLeAttributeValueRepository.save(customerLeAttributeValue);
		return attributeBean;
	}

	private CustomerLeAttributeBean updateCustomerLeAttributes(CustomerLeAttributeValue customerLeAttributeValue,
			CustomerLeAttributeBean attributeBean) {
		customerLeAttributeValue.setAttributeValues(attributeBean.getAttributeValue());
		customerLeAttributeValueRepository.save(customerLeAttributeValue);
		return attributeBean;
	}

	/**
	 * getCustomerLeAttributes
	 *
	 * @param custLegalId
	 * @param productName
	 * @return List<AttributesDto>
	 * @throws TclCommonException
	 */

	public List<AttributesDto> getCustomerLeAttributesById(Integer custLegalId, String productName)
			throws TclCommonException {
		List<AttributesDto> attributesDtoList = new ArrayList<>();
		List<CustomerLeAttributeValue> customerLeAttributeValueList = new ArrayList<>();
		try {

			if (Objects.isNull(custLegalId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			if (Objects.isNull(productName)) {
				customerLeAttributeValueList = customerLeAttributeValueRepository
						.findByCustomerLegalEntity_Id(custLegalId);
			} else {
				customerLeAttributeValueList = customerLeAttributeValueRepository
						.findByCustomerLeIdAndProductName(custLegalId, productName);
			}
			if (customerLeAttributeValueList.isEmpty())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			attributesDtoList = customerLeAttributeValueList.stream().map(AttributesDto::new)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attributesDtoList;
	}

	/**
	 * @author DinaharV getSupplierLegalEntityBasedOnCust
	 * @param request
	 * @return
	 */
	public CustomerLegalEntityProductResponseDto getInternationalSupplierLegalEntityBasedOnCust(
			CustomerLegalEntityRequestBean request) throws TclCommonException {
		CustomerLegalEntity customerLegalEntity = null;
		CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = null;
		try {
			validateRequest(request);
			customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();
			customerLegalEntityProductResponseDto.setLouRequired(true);
			customerLegalEntityProductResponseDto.setException(false);

			customerLegalEntity = customerLegalEntityRepository.findAllById(request.getCustomerLegalEntityId());

			if (customerLegalEntity == null) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_ERROR);
			}

			CustomerLeCurrency customerLecurrency = customerLegalEntity.getCustomerLeCurrencies().stream().findAny()
					.orElse(null);

			Set<CustomerLeCountry> customerLeCountrys = customerLegalEntity.getCustomerLeCountries();
			if (customerLeCountrys != null && !customerLeCountrys.isEmpty()) {
				CustomerLeCountry customerLeCountry = customerLeCountrys.stream().findAny().orElse(null);
				if (customerLeCountry != null) {

					if (customerLeCountry.getMstCountry().getName()
							.equalsIgnoreCase(TriggerEmailConstant.INDIA_COUNTRY))
						customerLegalEntityProductResponseDto.setLouRequired(false);

					MstCountry country = customerLeCountry.getMstCountry();
					validateSpLegalEntityForInternational(country, request, customerLegalEntityProductResponseDto,
							customerLecurrency);
				}

			}

		} catch (Exception e) {
			throw new TclCommonRuntimeException(e.getMessage(), e);
		}

		return customerLegalEntityProductResponseDto;

	}

	/**
	 * validateSpLegalEntity
	 *
	 * @param country
	 * @param request
	 * @param customerLegalEntityProductResponseDto
	 * @param spleCountryList
	 */
	private void validateSpLegalEntityForInternational(MstCountry country, CustomerLegalEntityRequestBean request,
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto,
			CustomerLeCurrency custLeCurrency) throws TclCommonException {
		if (isSiteAndCuLeSame(country, request.getSiteCountry()) || request.getSiteCountry().contains(country)) {

			getSupLegalEntityAgainstCustomerCountryInternational(country, customerLegalEntityProductResponseDto,
					custLeCurrency);

		}

		else if (checkForSiteEquality(request.getSiteCountry())) {
			getSupLegalEntityAgaintsSiteInternational(request.getSiteCountry(), customerLegalEntityProductResponseDto);

		}

		else {
			customerLegalEntityProductResponseDto.setException(true);

			// throw new
			// TclCommonException(ExceptionConstants.MULTIPLE_SITES_ERROR,ResponseResource.R_CODE_ERROR);

		}

	}

	/**
	 * getSupLegalEntityAgaintsSite
	 *
	 * @param siteCountry
	 * @param customerLegalEntityProductResponseDto
	 */
	private void getSupLegalEntityAgaintsSiteInternational(List<SiteCountryBean> siteCountry,
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto) throws TclCommonException {
		List<SpLeCountry> spleCountryList = null;

		List<MstCountry> mstCountryList = mstCountryRepository.findByName(siteCountry.get(0).getCountry());
		if (mstCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.MST_COUNTRY_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		}
		spleCountryList = spleCountryRepository.findByMstCountryAndIsDefault(mstCountryList.get(0), (byte) 1);

		if (spleCountryList == null || spleCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.SP_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		} else {
			customerLegalEntityProductResponseDto
					.setSple(spleCountryList.get(0).getServiceProviderLegalEntity().getEntityName());
			customerLegalEntityProductResponseDto
					.setServiceProviderId(spleCountryList.get(0).getServiceProviderLegalEntity().getId());
			// String supplierCurrency =
			// spleCountryList.get(0).getServiceProviderLegalEntity().getSpLeCurrencies().stream().findFirst().get().getCurrencyMaster().getShortName();
			if (mstCountryList.get(0).getName().equalsIgnoreCase("India"))
				customerLegalEntityProductResponseDto.setCurrency("INR");
			else
				customerLegalEntityProductResponseDto.setCurrency("USD");

		}
	}

	/**
	 * getSupLegalEntityAgainstCustomerCountry
	 *
	 * @param country
	 * @param customerLegalEntityProductResponseDto
	 */
	private void getSupLegalEntityAgainstCustomerCountryInternational(MstCountry country,
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto,
			CustomerLeCurrency custLeCurrency) throws TclCommonException {
		List<SpLeCountry> spleCountryList = null;
		SpLeCurrency currency = null;
		List<MstCountry> mstCountryList = mstCountryRepository.findByName(country.getName());
		if (mstCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.MST_COUNTRY_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		}
		spleCountryList = spleCountryRepository.findByMstCountryAndIsDefault(mstCountryList.get(0), (byte) 1);
		currency = spleCountryList.get(0).getServiceProviderLegalEntity().getSpLeCurrencies().stream().findAny()
				.orElse(null);
		if (spleCountryList == null || spleCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.SP_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		} else {
			customerLegalEntityProductResponseDto
					.setSple(spleCountryList.get(0).getServiceProviderLegalEntity().getEntityName());
			customerLegalEntityProductResponseDto
					.setServiceProviderId(spleCountryList.get(0).getServiceProviderLegalEntity().getId());
			if (currency != null)
				customerLegalEntityProductResponseDto.setCurrency(currency.getCurrencyMaster().getShortName());
			else
				customerLegalEntityProductResponseDto.setCurrency(custLeCurrency.getCurrencyMaster().getShortName());

		}
	}

	/**
	 * This method is to add a new gst number for a state
	 * 
	 * @param customerLeId
	 * @param requestBean
	 * @throws TclCommonException
	 */
	public void createLeStateGstInfo(Integer customerLeId, LeStateGstBean requestBean) throws TclCommonException {
		if (Objects.isNull(customerLeId) || !validateLeStateGstRequest(requestBean))
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);
		Optional<CustomerLegalEntity> customerLeOptional = customerLegalEntityRepository.findById(customerLeId);
		customerLeOptional.orElseThrow(() -> new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY));
		List<LeStateGst> existingGstNos = leStateGstRepository
				.findByCustomerLegalEntityAndState(customerLeOptional.get(), requestBean.getState().toUpperCase())
				.stream().filter(lestateGst -> lestateGst.getGstNo().equalsIgnoreCase(requestBean.getGstNo()))
				.collect(Collectors.toList());
		if (!existingGstNos.isEmpty())
			throw new TclCommonException(ExceptionConstants.DUPLICATE_GST_VALUE);
		LeStateGst leStateGst = new LeStateGst();
		leStateGst.setCustomerLegalEntity(customerLeOptional.get());
		leStateGst.setGstNo(requestBean.getGstNo());
		leStateGst.setState(requestBean.getState().toUpperCase());
		leStateGst.setAddress(requestBean.getAddress());
		leStateGstRepository.save(leStateGst);

	}

	private boolean validateLeStateGstRequest(LeStateGstBean requestBean) {

		if (null == requestBean || StringUtils.isEmpty(requestBean.getState())
				|| StringUtils.isEmpty(requestBean.getGstNo()))
			return false;

		return true;
	}

	/**
	 * This methods fetches the gst number of a customer legal entity
	 * 
	 * @param custLeId
	 * @return
	 * @throws TclCommonException
	 */
	public String getGstNumberByCustomerLeId(Integer custLeId) throws TclCommonException {
		String gstNo = null;
		try {

			if (Objects.isNull(custLeId))
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);

			List<CustomerLeAttributeValue> customerLeAttributeValueList = customerLeAttributeValueRepository
					.findByCustomerLegalEntity_Id(custLeId);

			if (customerLeAttributeValueList.isEmpty())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			gstNo = customerLeAttributeValueList.stream()
					.filter(custLeAttrValue -> custLeAttrValue.getMstLeAttribute().getName()
							.equalsIgnoreCase(SpConstants.GST_NUMBER))
					.collect(Collectors.toList()).get(0).getAttributeValues();
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gstNo;
	}

	/**
	 * get sap code based on customer le id
	 * 
	 * @param customerLe
	 * @param value
	 * @return
	 */
	public LeSapCodeResponse getSapCodeBasedonCustLe(List<Integer> customerLe, String value) {
		LeSapCodeResponse reponse = new LeSapCodeResponse();
		if (customerLe != null && !customerLe.isEmpty()) {

			String type = "SAP Code";
			if (value != null) {
				type = value;
			}

			List<LegalEntitySapCode> legalEntitySapCodes = legalEntitySapCodeRepository
					.findBycustomerLeIdInAndCodeType(customerLe, type);

			if (!legalEntitySapCodes.isEmpty()) {

				legalEntitySapCodes.forEach(leSap -> {
					LeSapCodeBean bean = new LeSapCodeBean();
					bean.setCodeValue(leSap.getCodeValue());
					reponse.getLeSapCodes().add(bean);

				});

			}

		}

		return reponse;
	}
	
	/**
	 * get sap code based on customer le id
	 * 
	 * @param customerLe
	 * @param value
	 * @return List of Sapcodes corresponding to the provide le ids and sap code type (SAP code / SECS code / other).
	 */
	public List<String> getCpnyAndSapCodesBasedonCustLe(List<Integer> leIds, String value) {
		if (leIds != null && !leIds.isEmpty()) {
			String type = "SAP Code";
			if (value != null) {
				type = value;
			}
			
			List<String> cpnySapCodes = legalEntitySapCodeRepository.findCpnyAndSapCodeDetails(leIds, type);
			if (!cpnySapCodes.isEmpty()) {
				return cpnySapCodes;
			}
		}
		return new ArrayList<>();
	}

	
	/**
	 * Get Files and move to object storage
	 *
	 * @param customerLeId
	 * @throws TclCommonException
	 */
	public void getFilesToMoveToObjectStorage(Integer customerLeId) throws TclCommonException {
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
						.findById(customerLeId);
				if (!customerLegalEntity.isPresent())
					throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
							ResponseResource.R_CODE_BAD_REQUEST);
				Optional<MstLeAttribute> msaAttribute = mstLeAttributeRepository
						.findByName(DocumentConstant.MSA_DOCUMENT);
				if (!msaAttribute.isPresent())
					throw new TclCommonException(ExceptionConstants.MST_LE_ATTRIBUTE_EMPTY,
							ResponseResource.R_CODE_BAD_REQUEST);

				Optional<MstLeAttribute> ssAttribute = mstLeAttributeRepository
						.findByName(DocumentConstant.SERVICE_SCHEDULE_DOCUMENT);
				if (!ssAttribute.isPresent())
					throw new TclCommonException(ExceptionConstants.MST_LE_ATTRIBUTE_EMPTY,
							ResponseResource.R_CODE_BAD_REQUEST);

				List<CustomerLeAttributeValue> customerLeAttributeValueListMSA = customerLeAttributeValueRepository
						.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLeId, msaAttribute.get().getId());

				uploadMSASSToObjectStorage(customerLegalEntity, msaAttribute, customerLeAttributeValueListMSA);

				List<CustomerLeAttributeValue> customerLeAttributeValueListSS = customerLeAttributeValueRepository
						.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLeId, ssAttribute.get().getId());

				uploadMSASSToObjectStorage(customerLegalEntity, ssAttribute, customerLeAttributeValueListSS);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void uploadMSASSToObjectStorage(Optional<CustomerLegalEntity> customerLegalEntity,
			Optional<MstLeAttribute> msaAttribute, List<CustomerLeAttributeValue> customerLeAttributeValueList) {
		if (!customerLeAttributeValueList.isEmpty()) {
			Customer customer = customerLegalEntity.get().getCustomer();
			if (customer.getCustomerCode() == null) {
				customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
				customerRepository.save(customer);
			}
			if (customerLegalEntity.get().getCustomerLeCode() == null) {
				customerLegalEntity.get()
						.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
				customerLegalEntityRepository.save(customerLegalEntity.get());
			}
			customerLeAttributeValueList.stream().forEach(custLeAtributeValue -> {
				if (custLeAtributeValue.getAttributeValues() != null
						&& !custLeAtributeValue.getAttributeValues().isEmpty()) {
					Optional<Attachment> attachment = attachmentRepository
							.findById(Integer.valueOf(custLeAtributeValue.getAttributeValues()));
					File[] files = new File(attachment.get().getUriPathOrUrl()).listFiles();
					if (files != null && files.length > 0) {
						for (File file : files) {
							if (file.isFile() && customer.getCustomerCode() != null
									&& customerLegalEntity.get().getCustomerLeCode() != null) {

								try {

									InputStream inputStream = new FileInputStream(file);
									StoredObject storedObject = fileStorageService.uploadObject(file.getName(),
											inputStream, customer.getCustomerCode(),
											customerLegalEntity.get().getCustomerLeCode());

									String[] pathArray = storedObject.getPath().split("/");
									updateMSASSDocumentsUploadedToContainer(storedObject.getName(),
											customerLegalEntity.get().getId(), msaAttribute.get().getName(),
											custLeAtributeValue.getProductName(), pathArray[1]);
								} catch (Exception e) {
									throw new TclCommonRuntimeException(e.getMessage(), e);
								}
							}
						}

					}

				}
			});
		}
	}

	/**
	 * Get Customer Code Based on Le ID
	 *
	 * @param customerLeId
	 * @return {@link ObjectStorageListenerBean}
	 * @throws TclCommonException
	 */
	public ObjectStorageListenerBean getCustomerCodeCustomerLeCodeBasedonLeId(Integer customerLeId)
			throws TclCommonException {
		if (customerLeId == null)
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		ObjectStorageListenerBean objListenerBean = new ObjectStorageListenerBean();
		Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(customerLeId);
		if (!customerLegalEntity.isPresent())
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
					ResponseResource.R_CODE_BAD_REQUEST);

		Customer customer = customerLegalEntity.get().getCustomer();
		if (customer.getCustomerCode() == null) {
			customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
			customerRepository.save(customer);
		}
		if (customerLegalEntity.get().getCustomerLeCode() == null) {
			customerLegalEntity.get().setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
			customerLegalEntityRepository.save(customerLegalEntity.get());
		}
		objListenerBean.setCustomerCode(customer.getCustomerCode());
		objListenerBean.setCustomerLeCode(customerLegalEntity.get().getCustomerLeCode());
		return objListenerBean;

	}

	/**
	 * Get Supplier Legal Currency By ID
	 *
	 * @param supplierLegalId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public String getCurrencyBySupplierLegalId(Integer supplierLegalId) throws TclCommonException {
		Objects.requireNonNull(supplierLegalId, "Supplier Legal ID cannot be null");
		SpLeCurrency spLeCurrency = spLeCurrencyRepository.findByServiceProviderLegalEntity_IdAndIsDefault(supplierLegalId, (byte) 1);
		return spLeCurrency.getCurrencyMaster().getShortName();
//		ServiceProviderLegalEntity serviceProviderLegalEntity = serviceProviderLegalEntityRepository.findById(supplierLegalId).get();
//		return getCurrencyDetails(spLeCountry.getMstCountry().getName());
	}

	/**
	 * Get Customer Legal Currency By ID
	 *
	 * @param customerLegalId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public String getCurrencyByCustomerLegalId(Integer customerLegalId) throws TclCommonException {
		Objects.requireNonNull(customerLegalId, "Customer Legal ID cannot be null");
		CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findById(customerLegalId).get();
		String currencyName = getCurrencyForCustomerLegalEntity(customerLegalEntity);
		return currencyName;
	}

	/**
	 * Get outbound prices pdf
	 *
	 * @param quoteCode
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public Resource getOutboundPrices(String quoteCode, String fileType, String fileName) throws TclCommonException {
		Resource resource = null;
		try {
			if (quoteCode == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			GscOutboundAttachmentBean attachmentBean = new GscOutboundAttachmentBean();
			attachmentBean.setFileName(fileName);
			attachmentBean.setFileType(fileType);
			attachmentBean.setQuoteCode(quoteCode);
			String attachmentRequest = Utils.convertObjectToJson(attachmentBean);
			LOGGER.info("MDC Filter token value in before Queue call getting omsAttachment details of quotecode {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String erfCustomerAttachmentId = (String) mqUtils.sendAndReceive(omsQuoteAttachmentQueue, attachmentRequest);
			if (StringUtils.isBlank(erfCustomerAttachmentId)) {
				LOGGER.warn("Erf Customer ID not found for QuoteCode {}", quoteCode);
			}

			Attachment attachment = attachmentRepository.findById(Integer.valueOf(erfCustomerAttachmentId)).get();
			if (Objects.nonNull(attachment)) {
				/*
				 * if (swiftApiEnabled.equalsIgnoreCase("true")) { String tempDownloadUrl =
				 * fileStorageService.getTempDownloadUrl( attachment.getDisplayName(),
				 * Long.parseLong(tempDownloadUrlExpiryWindow), attachment.getUriPathOrUrl());
				 * resource = new ByteArrayResource(tempDownloadUrl.getBytes()); } else {
				 */
				String actualFileName = getDisplayName(fileName, fileType);
				if (StringUtils.isNoneBlank(actualFileName)) {
					Path attachmentLocation = Paths.get(attachment.getUriPathOrUrl() + File.separator + actualFileName);
					resource = new UrlResource(attachmentLocation.toUri());
					if (resource.exists() || resource.isReadable()) {
						return resource;
					} else {
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
					}
				}
				// }
			}
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return resource;
	}

	private String getDisplayName(final String fileName, final String fileType) {
		if (GscAttachmentTypeConstants.GSC_SURCHARGE_PRICES_PDF.toLowerCase().contains(fileName.toLowerCase())) {
			return GscAttachmentTypeConstants.GSC_SURCHARGE_PRICES_PDF;
		} else if (GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF.toLowerCase().contains(fileName.toLowerCase())) {
			if (GscAttachmentTypeConstants.PDF.toLowerCase().contains(fileType.toLowerCase())) {
				return GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF;
			} else {
				return GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_EXCEL;
			}
		}
		return "";
	}

	/**
	 * This method fetches the countries list for a given customer legal entity
	 * 
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getCountriesListForTheCustomerLeId(Integer customerLeId) throws TclCommonException {

		if (customerLeId == null)
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		List<String> countries = customerLeCountryRepository.findByCustomerLegalEntityId(customerLeId).stream()
				.map(leCountry -> leCountry.getMstCountry().getName()).collect(Collectors.toList());
		return countries;
	}

	/**
	 * Get Outbound Prices Temporary URL
	 *
	 * @param quoteCode
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws TclCommonException
	 */
	public String getOutboundPricesTemporaryUrl(String quoteCode, String fileType, String fileName)
			throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			if (quoteCode == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			GscOutboundAttachmentBean attachmentBean = new GscOutboundAttachmentBean();
			attachmentBean.setFileName(fileName);
			attachmentBean.setFileType(fileType);
			attachmentBean.setQuoteCode(quoteCode);
			String attachmentRequest = Utils.convertObjectToJson(attachmentBean);
			LOGGER.info("MDC Filter token value in before Queue call getting omsAttachment details of quotecode {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String erfCustomerAttachmentId = (String) mqUtils.sendAndReceive(omsQuoteAttachmentQueue, attachmentRequest);
			if (StringUtils.isBlank(erfCustomerAttachmentId)) {
				LOGGER.warn("Erf Customer ID not found for QuoteCode {}", quoteCode);
			}

			Attachment attachment = attachmentRepository.findById(Integer.valueOf(erfCustomerAttachmentId)).get();
			if (Objects.nonNull(attachment)) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachment.getDisplayName(),
							Long.parseLong(tempDownloadUrlExpiryWindow), attachment.getUriPathOrUrl(), false);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	/**
	 * Get Files and move to object storage
	 *
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public List<Integer> getFilesToMoveToObjectStorage() throws TclCommonException {
		List<Integer> attachmentIdsList = new ArrayList<>();
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				// List<CustomerLegalEntity> customerLegalEntityList =
				// customerLegalEntityRepository.findAll();
				// if (customerLegalEntityList.isEmpty())
				// throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
				// ResponseResource.R_CODE_BAD_REQUEST);
				Optional<MstLeAttribute> msaAttribute = mstLeAttributeRepository
						.findByName(DocumentConstant.MSA_DOCUMENT);
				if (!msaAttribute.isPresent())
					throw new TclCommonException(ExceptionConstants.MST_LE_ATTRIBUTE_EMPTY,
							ResponseResource.R_CODE_BAD_REQUEST);

				Optional<MstLeAttribute> ssAttribute = mstLeAttributeRepository
						.findByName(DocumentConstant.SERVICE_SCHEDULE_DOCUMENT);
				if (!ssAttribute.isPresent())
					throw new TclCommonException(ExceptionConstants.MST_LE_ATTRIBUTE_EMPTY,
							ResponseResource.R_CODE_BAD_REQUEST);

				List<MstLeAttribute> mstLeAttributesList = new ArrayList<>();
				mstLeAttributesList.add(msaAttribute.get());
				mstLeAttributesList.add(ssAttribute.get());

				List<CustomerLeAttributeValue> customerLeAttributeValList = customerLeAttributeValueRepository
						.findByMstLeAttributeIn(mstLeAttributesList);

				customerLeAttributeValList.stream().forEach(customerLeAttr -> {

					// List<CustomerLeAttributeValue> customerLeAttributeValueListMSA =
					// customerLeAttributeValueRepository
					// .findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLegalEntity.getId(),
					// msaAttribute.get().getId());

					Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
							.findById(customerLeAttr.getCustomerLegalEntity().getId());
					Integer attId = uploadMSASSToObjectStorage(customerLegalEntity.get(), customerLeAttr);

					if (attId != null)
						attachmentIdsList.add(attId);
					// List<CustomerLeAttributeValue> customerLeAttributeValueListSS =
					// customerLeAttributeValueRepository
					// .findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLegalEntity.getId(),
					// ssAttribute.get().getId());
					//
					// uploadMSASSToObjectStorage(customerLegalEntity, customerLeAttr);

				});
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return attachmentIdsList;
	}

	private Integer uploadMSASSToObjectStorage(CustomerLegalEntity customerLegalEntity,
			CustomerLeAttributeValue customerLeAttributeValue) {
		Integer attachmentId = null;
		LOGGER.info("Inside Upload MSA SS");
		Customer customer = customerLegalEntity.getCustomer();
		if (customer.getCustomerCode() == null) {
			customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
			customerRepository.save(customer);
		}
		if (customerLegalEntity.getCustomerLeCode() == null) {
			customerLegalEntity.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.getTpsSfdcCuid()));
			customerLegalEntityRepository.save(customerLegalEntity);
		}
		if (customerLeAttributeValue.getAttributeValues() != null
				&& !customerLeAttributeValue.getAttributeValues().isEmpty()) {
			LOGGER.info("Customer Le {}", customerLeAttributeValue.getAttributeValues());
			Optional<Attachment> attachment = attachmentRepository
					.findById(Integer.valueOf(customerLeAttributeValue.getAttributeValues()));
			LOGGER.info("Attachment received {}", attachment);
			if (attachment.isPresent()) {
				File[] files = new File(attachment.get().getUriPathOrUrl()).listFiles();
				if (files != null && files.length > 0) {
					for (File file : files) {
						if (file.isFile() && customer.getCustomerCode() != null
								&& customerLegalEntity.getCustomerLeCode() != null) {
							try {
								LOGGER.info("Inside Stream");
								InputStream inputStream = new FileInputStream(file);
								StoredObject storedObject = fileStorageService.uploadObject(file.getName(), inputStream,
										customer.getCustomerCode(), customerLegalEntity.getCustomerLeCode());

								String[] pathArray = storedObject.getPath().split("/");
								// updateMSASSDocumentsUploadedToContainer(storedObject.getName(),
								// customerLegalEntity.getId(),
								// customerLeAttributeValue.getMstLeAttribute().getName(),
								// customerLeAttributeValue.getProductName(), pathArray[1]);

								/*
								 * attachment = attachmentRepository
								 * .findById(Integer.valueOf(customerLeAttributeValue.getAttributeValues()));
								 * 
								 * if (attachment.isPresent()) {
								 */
								attachment.get().setName(storedObject.getName());
								attachment.get().setDisplayName(storedObject.getName());
								attachment.get().setUriPathOrUrl(pathArray[1]);
								attachmentRepository.save(attachment.get());
								attachmentId = attachment.get().getId();
//								}
							} catch (Exception e) {
								LOGGER.warn("Exception" + ExceptionUtils.getStackTrace(e));
								throw new TclCommonRuntimeException(e.getMessage(), e);
							}
						}
					}

				}
			}

		}
		return attachmentId;

	}

	/**
	 * get customer code and customer le code based on le id
	 * 
	 * @param customerLeIdsList
	 * @return
	 * @throws TclCommonException
	 */

	public List<ObjectStorageListenerBean> getCustomerCodeCustomerLeCodeBasedonLeId(List<Integer> customerLeIdsList)
			throws TclCommonException {
		List<ObjectStorageListenerBean> objStorageListenerBeanList = new ArrayList<>();

		if (customerLeIdsList == null || customerLeIdsList.isEmpty())
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		customerLeIdsList.stream().forEach(customerLeId -> {
			ObjectStorageListenerBean objListenerBean = new ObjectStorageListenerBean();
			Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(customerLeId);
			if (customerLegalEntity.isPresent()) {
				LOGGER.info("Data Found For Customer LeId {}, {}", customerLeId,
						customerLegalEntity.get().getCustomer().getCustomerCode());
				Customer customer = customerLegalEntity.get().getCustomer();
				if (customer.getCustomerCode() == null) {
					customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
					customerRepository.save(customer);
				}
				if (customerLegalEntity.get().getCustomerLeCode() == null) {
					customerLegalEntity.get()
							.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
					customerLegalEntityRepository.save(customerLegalEntity.get());
				}
				objListenerBean.setCustomerCode(customer.getCustomerCode());
				objListenerBean.setCustomerLeCode(customerLegalEntity.get().getCustomerLeCode());
				objListenerBean.setCustomerId(customer.getId());
				objListenerBean.setCustomerLeId(customerLegalEntity.get().getId());
				objStorageListenerBeanList.add(objListenerBean);
			}

		});

		return objStorageListenerBeanList;
	}
	
	/**
	 * This method return customerCode and customerLeCode
	 *
	 * @author VISHESH AWASTHI
	 * @param customerLeId
	 * @return CustomerCodeBean
	 */
	public CustomerCodeBean getCustomerAndCustomerLeCode(Integer customerLeId) {
		CustomerCodeBean customerCodeBean = new CustomerCodeBean();
		customerLegalEntityRepository.findById(customerLeId).ifPresent(customerLegalEntity -> {
			customerCodeBean.setCustomerCode(customerLegalEntity.getCustomer().getCustomerCode());
			customerCodeBean.setCustomerLeCode(customerLegalEntity.getCustomerLeCode());
		});
		return customerCodeBean;
	}


	/**
	 * Method to search customer name
	 * 
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	public List<CustomerDto> getCustomerNameBySearch(String searchValue) throws TclCommonException {
		List<CustomerDto> customerNames = customerRepository.findCustomerNameBySearch(searchValue).stream()
				.map(customer -> {
					CustomerDto obj = new CustomerDto();
					obj.setCustomerId(customer.getId());
					obj.setCustomerName(customer.getCustomerName());
					return obj;
				}).collect(Collectors.toList());

		return customerNames;
	}

	/**
	 * getLeSiteGst
	 * 
	 * @param gst
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public LeStateInfo getLeSiteGst(String gst, Integer customerLeId) throws TclCommonException {

		try {
			Optional<CustomerLegalEntity> optionalCustomerLegalEntity = customerLegalEntityRepository
					.findById(customerLeId);

			if (optionalCustomerLegalEntity.isPresent()) {
				CustomerLegalEntity customerLegalEntity = optionalCustomerLegalEntity.get();
				LOGGER.info("Customer lgeal entity is {} ", customerLegalEntity);
				LOGGER.info("Gst no is {} ", gst);
				LeStateGst leStateGst=	leStateGstRepository.findByCustomerLegalEntityAndGstNo(customerLegalEntity, gst);
				if (leStateGst != null) {
					LeStateInfo info = new LeStateInfo();
					info.setAddress(leStateGst.getAddress());
					info.setState(leStateGst.getState());
					info.setAddresslineOne(leStateGst.getAddresslineOne());
					info.setAddresslineTwo(leStateGst.getAddresslineTwo());
					info.setAddresslineThree(leStateGst.getAddresslineThree());
					info.setCity(leStateGst.getCity());
					info.setCountry(leStateGst.getCountry());
					info.setPincode(leStateGst.getPincode());
					LOGGER.info("le state info - address is {} ", leStateGst.getAddress());
					return info;
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return null;
	}

	public CustomerLeAccountManagerDetails getCustomerLeAccountManagerDetails(Integer customerLeId)
			throws TclCommonException {
		try {
			CustomerLeAccountManagerDetails customerLeAccountManagerDetails = new CustomerLeAccountManagerDetails();
			List<CustomerLeAttributeValue> customerLeAttributeValues = customerLeAttributeValueRepository
					.findByCustomerLegalEntity_Id(customerLeId);
			Optional<MstLeAttribute> spLeOwnerName = mstLeAttributeRepository.findByName("SUPPLIER_LE_OWNER");
			Optional<MstLeAttribute> spLeOwnerEmail = mstLeAttributeRepository.findByName("SUPPLIER_LE_EMAIL");
			Optional<MstLeAttribute> spLeMobile = mstLeAttributeRepository.findByName("Supplier Mobile");
			if (spLeOwnerName.isPresent()) {
				Optional<CustomerLeAttributeValue> spLeNameValueEntity = customerLeAttributeValues.stream()
						.filter(value -> value.getMstLeAttribute().equals(spLeOwnerName.get())).findFirst();
				if (spLeNameValueEntity.isPresent()) {
					customerLeAccountManagerDetails
							.setAccountManagerName(spLeNameValueEntity.get().getAttributeValues());
				}
			}
			if (spLeOwnerEmail.isPresent()) {
				Optional<CustomerLeAttributeValue> spLeEmailValueEntity = customerLeAttributeValues.stream()
						.filter(value -> value.getMstLeAttribute().equals(spLeOwnerEmail.get())).findFirst();
				if (spLeEmailValueEntity.isPresent()) {
					customerLeAccountManagerDetails
							.setAccountManagerEmailId(spLeEmailValueEntity.get().getAttributeValues());
				}
			}

			if (spLeMobile.isPresent()) {
				Optional<CustomerLeAttributeValue> spLeMobileEntity = customerLeAttributeValues.stream()
						.filter(value -> value.getMstLeAttribute().equals(spLeMobile.get())).findFirst();
				if (spLeMobileEntity.isPresent()) {
					customerLeAccountManagerDetails
							.setAccountManagerMob(spLeMobileEntity.get().getAttributeValues());
				}
			}
			return customerLeAccountManagerDetails;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	public String getAccountManagerName(String custId) throws TclCommonException {
		try {
			Optional<Customer> cust = customerRepository.findById(Integer.parseInt(custId));
			MstCustomerSpAttribute attribute = mstCustomerSpAttributeRepository
					.findByNameAndStatus(SpConstants.ACCOUNT_OWNER, (byte) 1);
			List<CustomerAttributeValue> customer = customerAttributeValueRepository
					.findByCustomerAndMstCustomerSpAttribute(cust.get(), attribute);
			return customer.get(0).getAttributeValues();
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	public String getAccountRtmName(String custId) throws TclCommonException {
		try {
			LOGGER.info("Erf Customer ID :: {}", custId);
			MstCustomerSpAttribute partnerRTMAttribute = mstCustomerSpAttributeRepository
					.findByNameAndStatus(ACCOUNT_RTM, (byte) 1);
			String partnerRtm = customerAttributeValueRepository
					.findByCustomerId(Integer.parseInt(custId)).stream().filter(customerAttributeValue ->
					customerAttributeValue.getMstCustomerSpAttribute().getId().equals(partnerRTMAttribute.getId()))
					.findFirst().get().getAttributeValues();
			LOGGER.info("partnerRtm :: {}", partnerRtm);
			return partnerRtm;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * get customer details based on customer id
	 * 
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	public CustomerDetailBean getCustomerDetailsByCustomerId(Integer customerId) throws TclCommonException {
		CustomerDetailBean customerDetailBean = new CustomerDetailBean();
		try {
			Optional<Customer> customer = customerRepository.findById(customerId);
			if (customer.isPresent()) {
				customerDetailBean.setCustomerId(customer.get().getId());
				customerDetailBean.setCustomerName(customer.get().getCustomerName());
				customerDetailBean.setSFDCAccountId(customer.get().getTpsSfdcAccountId());
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return customerDetailBean;
	}

	/**
	 * add contact id to customer billing info Request
	 * 
	 * @param CustomerLeBilling
	 * @throws TclCommonException
	 */

	public void addContactId(MDMOmsResponseBean mdmResponse) throws TclCommonException {
		Optional<CustomerLeBillingInfo> customer = customerLeBillingInfoRepository
				.findById(mdmResponse.getCustomerLeBillinginfoId());
		if (customer.isPresent()) {
			CustomerLeBillingInfo customerLeBillingInfo = customer.get();
			customerLeBillingInfo.setContactId(mdmResponse.getContactId());
			customerLeBillingInfoRepository.save(customerLeBillingInfo);
		}
	}

	private MSTAddressDetails constructMstBean(CustomerLeBillingRequestBean customerLeBillingRequest,
			Integer legalEntityId) {
		MSTAddressDetails mSTAddressDetails = new MSTAddressDetails();
		mSTAddressDetails.setAddress_Line_One(customerLeBillingRequest.getAddressLineOne());
		mSTAddressDetails.setCity(customerLeBillingRequest.getCity());
		mSTAddressDetails.setCountry(customerLeBillingRequest.getCountry());
		mSTAddressDetails.setCustomer_Le_Id(legalEntityId);
		mSTAddressDetails.setLocality(customerLeBillingRequest.getLocality());
		mSTAddressDetails.setPinCode(customerLeBillingRequest.getPinCode());
		mSTAddressDetails.setState(customerLeBillingRequest.getState());
		return mSTAddressDetails;
	}

	/**
	 * construct MDMBean Request
	 * 
	 * @param mstAddressDetails
	 * @param CustomerLeBilling
	 * @throws TclCommonException
	 */
	public MDMOmsRequestBean constructMdmBean(CustomerLeBillingInfo customerLeBillingInfo, boolean add,
			MSTAddressDetails mstAddressDetails, String quoteCode) throws TclCommonException {
		MDMOmsRequestBean request = new MDMOmsRequestBean();
		request.setAddressSeq(customerLeBillingInfo.getAddressSeq());
		request.setBillAccNo(customerLeBillingInfo.getBillAccNo());
		request.setBillAddr(customerLeBillingInfo.getBillAddr());
		request.setBillContactSeq(customerLeBillingInfo.getBillContactSeq());
		request.setContactType(customerLeBillingInfo.getContactType());
		request.setCountry(customerLeBillingInfo.getCountry());
		request.setCustomerId(customerLeBillingInfo.getCustomerId());
		request.setCustomerLeEntityId(customerLeBillingInfo.getCustomerLegalEntity().getId().toString());
		request.setEmailId(customerLeBillingInfo.getEmailId());
		request.setFname(customerLeBillingInfo.getFname());
		request.setLname(customerLeBillingInfo.getLname());
		request.setPhoneNumber(customerLeBillingInfo.getMobileNumber());
		request.setTitle(customerLeBillingInfo.getTitle());
		if (!add) {
			request.setContactId(customerLeBillingInfo.getContactId());
		}

		request.setSrcKeyId(customerLeBillingInfo.getCustomerLegalEntity().getTpsSfdcCuid());
		request.setIsAdd(add);
		request.setCustomerLeBillingId(customerLeBillingInfo.getId());
		request.setAddressLineOne(mstAddressDetails.getAddress_Line_One());
		request.setPinCode(mstAddressDetails.getPinCode());
		request.setLocality(mstAddressDetails.getLocality());
		request.setCity(mstAddressDetails.getCity());
		request.setState(mstAddressDetails.getState());
		request.setQuoteCode(quoteCode);
		return request;
	}

	/**
	 * construct MDMBean Request
	 * 
	 * @param CustomerLeContact
	 * @throws TclCommonException
	 */

	public MDMOmsRequestBean constructMdmBeanLeContact(CustomerLeContact customerLeContact, boolean add,
			String firstname, String lastname, MSTAddressDetails mstAddressDetails, String quoteCode)
			throws TclCommonException {
		MDMOmsRequestBean request = new MDMOmsRequestBean();
		request.setBillAddr(customerLeContact.getAddress());
		request.setCustomerLeEntityId(customerLeContact.getCustomerLeId().toString());
		request.setEmailId(customerLeContact.getEmailId());
		request.setFname(firstname);
		request.setLname(lastname);
		request.setPhoneNumber(customerLeContact.getMobilePhone());
		if (!add) {
			request.setContactId(customerLeContact.getContactId());

		}
		Optional<CustomerLegalEntity> legel = customerLegalEntityRepository
				.findById(customerLeContact.getCustomerLeId());
		request.setSrcKeyId(legel.get().getTpsSfdcCuid());
		request.setIsAdd(add);
		request.setAddressLineOne(mstAddressDetails.getAddress_Line_One());
		request.setPinCode(mstAddressDetails.getPinCode());
		request.setLocality(mstAddressDetails.getLocality());
		request.setCity(mstAddressDetails.getCity());
		request.setState(mstAddressDetails.getState());
		request.setCountry(mstAddressDetails.getCountry());
		request.setQuoteCode(quoteCode);
		return request;
	}

	/**
	 * This method updates the contact details to Cof
	 *
	 * @param quotoleid
	 * @param attributename
	 * @param attributevalue
	 * @throws TclCommonException
	 */
	private String updateOmsLeContact(Integer quoteToLeId, String attributeName, String attributeValue) {
		String response = null;
		OmsLeAttributeBean omsLeAttributeBean = new OmsLeAttributeBean();
		omsLeAttributeBean.setQuoteToLeId(quoteToLeId);
		omsLeAttributeBean.setAttrName(attributeName);
		omsLeAttributeBean.setAttrValue(attributeValue);
		omsLeAttributeBean.setUserName(Utils.getSource());
		try {
			response = (String) mqUtils.sendAndReceive(updateOmsBillingAttribute,
					Utils.convertObjectToJson(omsLeAttributeBean));
		} catch (TclCommonException e) {
			LOGGER.error("error in process update oms le attribute attribute", e);
		}
		return response;
	}

	/**
	 * get Customer Attribute
	 * 
	 * @param CustomerId
	 * @return
	 * @return
	 * @throws TclCommonException
	 */
	public String getCustomerAttribute(String custId) throws TclCommonException {
		try {
			String response = enterpriseProvider;
			Optional<Customer> cust = customerRepository.findById(Integer.parseInt(custId));
			List<CustomerAttributeValue> customer = customerAttributeValueRepository.findByCustomer(cust.get());
			if (!customer.isEmpty()) {
				for (CustomerAttributeValue customerAttributeDetail : customer) {
					if (CommonConstants.ENTERPRISE.equalsIgnoreCase(customerAttributeDetail.getAttributeValues())) {
						response = enterpriseProvider;
					}
					if (CommonConstants.SERVICEPROVIDER.equalsIgnoreCase(customerAttributeDetail.getAttributeValues())) {
						response = serviceProvider;
					}
				}
			}
			return response;
		} catch (Exception e) {
			LOGGER.error("Error in Customer Attributes ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}

	}

	/**
	 * validate Le contact details
	 *
	 * @param CustomerLeContactBean
	 * @param CustomerLeContact
	 * @throws TclCommonException
	 */
//	private boolean contactExistsLeContact(CustomerLeContactBean lecontactbean, CustomerLeContact lecontact) {
//
//		if (StringUtils.isEmpty(lecontactbean.getFirstName()) || StringUtils.isEmpty(lecontactbean.getLastName())
//				|| StringUtils.isEmpty(lecontactbean.getCustomerLeContactName())
//				|| StringUtils.isEmpty(lecontactbean.getCustomerLeContactEmailid())
//				|| lecontact.getEmailId().equalsIgnoreCase(lecontactbean.getCustomerLeContactEmailid())) {
//			return true;
//		}
//		return false;
//	}

	private boolean contactExistsLeContact(CustomerLeContactBean lecontactbean, CustomerLeContact lecontact) {
		Boolean leContact = leContactEmpty(lecontactbean, lecontact);
		if (!leContact) {
			List<CustomerLeContact> leContactList = customerLeContactRepository
					.findByCustomerLeIdContactTypeIsNull(lecontact.getCustomerLeId());
			if(!leContactList.isEmpty()) {
			for (CustomerLeContact customerLeContact : leContactList) {
				if (lecontactbean.getQuoteCode().startsWith("IPC")) {
					if(lecontactbean.getContactId() != customerLeContact.getId() && customerLeContact.getEmailId().equalsIgnoreCase(lecontactbean.getCustomerLeContactEmailid())) {
						return true;
					}
				} else if (customerLeContact.getEmailId().equalsIgnoreCase(lecontactbean.getCustomerLeContactEmailid())) {
					return true;
				}

			  }
			}
			return false;
		} else {
			return leContact;
		}
	}
	
	/**
	 * validate Le contact details empty
	 *
	 * @param CustomerLeContactBean
	 * @param CustomerLeContact
	 * @throws TclCommonException
	 */
	private boolean leContactEmpty(CustomerLeContactBean lecontactbean,  CustomerLeContact lecontact) {

		if(StringUtils.isEmpty(lecontactbean.getFirstName())
				|| StringUtils.isEmpty(lecontactbean.getLastName())
				|| StringUtils.isEmpty(lecontactbean.getCustomerLeContactName())
				|| StringUtils.isEmpty(lecontactbean.getCustomerLeContactEmailid())){
			return true;
		}
		return false;
	}


	

	/**
	 * validate Billing contact details
	 *
	 * @param CustomerLeBillingInfo
	 * @param CustomerLeBillingRequestBean
	 * @throws TclCommonException
	 */
	private boolean contactExist(CustomerLeBillingInfo customerLeBillingInfoUpdated,
			CustomerLeBillingRequestBean customerLeBillingRequest) {
		List<CustomerLeBillingInfo> billingList = customerLeBillingInfoRepository
				.findByCustomerLegalEntity_IdAndIsactive(customerLeBillingInfoUpdated.getCustomerLegalEntity().getId(),
						"Yes");
		for (CustomerLeBillingInfo biiling : billingList) {
			if(biiling.getEmailId()!=null) {
			if (biiling.getEmailId().equalsIgnoreCase(customerLeBillingRequest.getEmailId())) {
				return true;
			}
			}
		}
		return false;
	}

	/**
	 * validate contact details
	 *
	 * @param CustomerLeBillingInfo
	 * @param CustomerLeBillingRequestBean
	 * @throws TclCommonException
	 */
	private boolean contactValidate(CustomerLeBillingInfo billingInfo, CustomerLeBillingRequestBean billingRequest) {

		if (StringUtils.isEmpty(billingRequest.getFname()) || StringUtils.isEmpty(billingRequest.getLname())
				|| StringUtils.isEmpty(billingRequest.getMobileNumber())
				|| StringUtils.isEmpty(billingRequest.getEmailId())) {
			return true;
		}
		return false;
	}

	/**
	 * validate billing contact address details is exist
	 * 
	 * @param CustomerLeBillingInfo
	 * @throws TclCommonException
	 */
	private boolean billingContactAddressValidate(CustomerLeBillingInfo customerLeBillingInfo) {
		if (StringUtils.isEmpty(customerLeBillingInfo.getErfloclocationid())) {
			return true;
		}
		return false;

	}

	/**
	 * validate Le contact address details is exist
	 * 
	 * @param CustomerLeBillingpartnerEngagementRepositoryInfo
	 * @throws TclCommonException
	 */
	private boolean validateLeAddress(CustomerLeContact customerLeContactOpt) {

		if (StringUtils.isEmpty(customerLeContactOpt.getErfloclocationid())
				|| StringUtils.isEmpty(customerLeContactOpt.getContactId())) {
			return true;
		}
		return false;
	}

	/**
	 * getCustomerList method to get List<CustomerDto> for customer Ids
	 * 
	 * @param customerIds
	 * @return
	 * @throws TclCommonException
	 */
	public List<CustomerDto> getCustomerList(List<Integer> customerIds) throws TclCommonException {
		LOGGER.info("Entering method getCustomerList {}: ", customerIds);
		List<CustomerDto> custDto = new ArrayList<>();
		Set<Integer> setIds = new TreeSet<>();
		try {
			if (customerIds != null && !customerIds.isEmpty()) {
				customerIds.stream().forEach(id -> setIds.add(id));
				if (!setIds.isEmpty()) {
					List<Integer> idList = new ArrayList<>(setIds);
					List<Customer> custList = customerRepository.findAllById(idList);
					custDto = constructCustomerDto(custList);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.NO_CUSTOMER, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return custDto;
	}

	/**
	 * Get customer from customer le id
	 * 
	 * @param customerLeId
	 *
	 * @return {@link CustomerDto}
	 */
	public CustomerDto getCustomerByCustomerLeId(Integer customerLeId) {
		return customerLegalEntityRepository.findById(customerLeId).map(customerLegalEntity -> {
			CustomerDto customer = new CustomerDto();
			customer.setCustomerId(customerLegalEntity.getCustomer().getId());
			customer.setCustomerName(customerLegalEntity.getCustomer().getCustomerName());
			return customer;
		}).orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_ID_EMPTY,
				ResponseResource.R_CODE_BAD_REQUEST));
	}

	/**
	 * Get Customer le details by le id
	 *
	 * @param customerLeIds
	 * @return {@link List<CustomerDetailBean>}
	 */
	public List<CustomerDetailBean> getCustomerDetailsByLeId(List<Integer> customerLeIds) {
		return customerLeIds.stream().map(customerLeId -> customerLegalEntityRepository.findById(customerLeId))
				.map(Optional::get).map(this::getCustomerDetailsBean).collect(Collectors.toList());
	}

	/**
	 * Get customer bean with customer legal entity
	 *
	 * @param customerLegalEntity
	 * @return {@link CustomerDetailBean}
	 */
	private CustomerDetailBean getCustomerDetailsBean(CustomerLegalEntity customerLegalEntity) {
		CustomerDetailBean customerDetailBean = new CustomerDetailBean();
		customerDetailBean.setCustomerId(customerLegalEntity.getCustomer().getId());
		customerDetailBean.setCustomerName(customerLegalEntity.getCustomer().getCustomerName());
		customerDetailBean.setCustomercode(customerLegalEntity.getCustomer().getCustomerCode());
		customerDetailBean.setStatus(Integer.valueOf(customerLegalEntity.getCustomer().getStatus()));
		customerDetailBean.setSFDCAccountId(customerLegalEntity.getCustomer().getTpsSfdcAccountId());
		return customerDetailBean;
	}

	/**
	 * @param customerLeId
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	public CustomerLeVO getCustomerLeDetailsForCreditCheck(Integer customerLeId, String productName) throws TclCommonException {
		CustomerLeVO custLeBean = new CustomerLeVO();
		List<Attributes> attributes = new ArrayList<>();

		if (Objects.isNull(customerLeId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			Optional<CustomerLegalEntity> custLE = customerLegalEntityRepository
					.findById(customerLeId);
			if (custLE.isPresent()) {
				custLeBean.setAgreementId(custLE.get().getAgreementId());
				custLeBean.setLegalEntityId(custLE.get().getId());
				custLeBean.setLegalEntityName(custLE.get().getEntityName());
				custLeBean.setSfdcCuId(custLE.get().getTpsSfdcCuid());
				custLeBean.setPreapprovedMrc(custLE.get().getPreApprovedMrc());
				custLeBean.setPreapprovedNrc(custLE.get().getPreApprovedNrc());
				custLeBean.setPreapprovedPaymentTerm(custLE.get().getPreApprovedPaymentTerm());
				custLeBean.setPreapprovedBillingMethod(custLE.get().getPreApprovedBillingMethod());
				custLeBean.setCreditCheckAccountType(custLE.get().getCreditCheckAccountType());
				custLeBean.setBlacklistStatus(custLE.get().getBlacklistStatus());
				custLeBean.setAccountId(custLE.get().getCustomer().getAccountId18());
				custLeBean.setCreditPreapprovedFlag(custLE.get().getCreditPreapprovedFlag());
				
				List<CustomerLeAttributeValue> customerLeAttributeValueList = customerLeAttributeValueRepository
							.findByCustomerLeIdAndProductName(customerLeId, productName);

				if (customerLeAttributeValueList.isEmpty())
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);


				customerLeAttributeValueList
						.forEach(attr->{
							Attributes attribute = new Attributes();
							attribute.setAttributeName(attr.getMstLeAttribute().getName());
							attribute.setAttributeValue(attr.getAttributeValues());
							attribute.setType(attr.getMstLeAttribute().getType());
							attributes.add(attribute);
						}
				);
				custLeBean.getAttributes().addAll(attributes);

				List<CustomerLeBillingInfo> customerContacts = customerLeBillingInfoRepository
						.findByCustomerLegalEntity_IdAndIsactive(customerLeId, "Yes");
				if (!customerContacts.isEmpty()) {
					custLeBean.setBillingContactId(customerContacts.get(0).getId());
				}



			}
			
			
	return custLeBean;
	}
	


	
	/**
	 * 
	 * This method return CRN Information for the given LE
	 * @param customerLeId
	 * @return
	 */
	public String getCRNDetailsByCustomerLeId(Integer customerLeId) {
		
		List<LegalEntitySapCode> legalEntitySapCodes = legalEntitySapCodeRepository.findCustomerleIdSapDetails(customerLeId);
		if(legalEntitySapCodes!=null && !legalEntitySapCodes.isEmpty()) {
			return legalEntitySapCodes.get(0).getCodeValue();
		}
		return "";
	}
	
	/**
	 * 
	 * This method return customer account manger email id 
	 * @param customerId
	 * @return
	 */
	public String getAccountManagerEmailId(String custId) throws TclCommonException {
		try {
			String email=null;
			Optional<Customer> cust = customerRepository.findById(Integer.parseInt(custId));
			MstCustomerSpAttribute attribute = mstCustomerSpAttributeRepository
					.findByNameAndStatus(SpConstants.ACCOUNT_OWNER_EMAILID, (byte) 1);
			List<CustomerAttributeValue> customer = customerAttributeValueRepository
					.findByCustomerAndMstCustomerSpAttribute(cust.get(), attribute);
			if(!customer.isEmpty()) {
				email=customer.get(0).getAttributeValues();
				return email;
			}
			else {
			return email;
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
	}
	
	/**
	 * 
	 * Get account Manager region based on customer id and user id
	 * @param customerId
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	public String getRegionByCustomerIdAndUserId(Integer customerId, Integer userId) throws TclCommonException {
		try {
			List<AccountManager> accountManagers = accountManagerRepository
					.findByErfCustCustomerIdAndErfUserUserId(customerId, userId);
			if (accountManagers != null && !accountManagers.isEmpty()) {
				return accountManagers.get(0).getRegion();
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return null;
	}

	/**
	 * Get Customer Le Name By Sap Code
	 *
	 * @param sapCodes
	 * @return {@link List<ClassificationBean>}
	 */
	public List<ClassificationBean> getCustomerLeNameBySapCode(List<String> sapCodes) {
		List<Map<String, Object>> result = customerLegalEntityRepository.findAllCustomerLegalEntityBySapCode(sapCodes);
		LOGGER.info("DB Response for Sap Code :: {}", result.toString());
		return result.stream().map(this::createCustomerClassificationBean).collect(Collectors.toList());
	}

	/**
	 * Get Customer Le Name By Billing Code
	 *
	 * @param billingAccountIds
	 * @return {@link List<ClassificationBean>}
	 */
	public List<ClassificationBean> getCustomerLeNameByBillingAccountIds(List<String> billingAccountIds) {
		List<Map<String, Object>> result = customerLegalEntityRepository.findAllCustomerLegalEntityByBillingAccountIds(billingAccountIds);
		LOGGER.info("DB Response for Billing Acc Id :: {}", result.toString());
		return result.stream().map(this::createCustomerClassificationBean).collect(Collectors.toList());
	}

	private ClassificationBean createCustomerClassificationBean(final Map<String, Object> result) {
		ClassificationBean customerClassificationBean = new ClassificationBean();
		customerClassificationBean.setSapCode(result.get("code_value").toString());
		customerClassificationBean.setCustomerLegalName(result.get("entity_name").toString());
		customerClassificationBean.setCustomerLeId((Integer) result.get("le_id"));
		return customerClassificationBean;
	}
	
	/**
	 * This method return Latest CRN Information for the given LE
	 * 
	 * @param customerLeId
	 * @return
	 */
	public String getLatestCRNDetailsByCustomerLeId(Integer customerLeId) {
		LOGGER.info("Entering method getLatestCRNDetailsByCustomerLeId {}: ", customerLeId);
		LegalEntitySapCode legalEntitySapCodes = legalEntitySapCodeRepository
				.findLatestCustomerleIdSapDetails(customerLeId);
		return legalEntitySapCodes != null ? legalEntitySapCodes.getCodeValue() : null;
	} 

	/**
	 * This method return SECS Code related to customer LE ID.
	 * 
	 * @param customerLeId
	 * @return
	 */
	public String getSECSByCustomerLeId(Integer customerLeId) {
		LOGGER.info("Entering method getSECSByCustomerLeId {}: ", customerLeId);
		LegalEntitySapCode legalEntitySapCodes = legalEntitySapCodeRepository.findLeSecsCode(customerLeId);
		return legalEntitySapCodes != null ? legalEntitySapCodes.getCodeValue() : null;
	}

	/**
	 * This method return SECS Code related to customer LE ID.
	 *
	 * @param customerLeId
	 * @return
	 */
	public String getSECSByCustomerLeIdForGsc(Integer customerLeId) {
		LOGGER.info("Entering method getSECSByCustomerLeIdForGsc {}: ", customerLeId);
		String legalEntitySapCodes = legalEntitySapCodeRepository.findSecsCodeByCustomerLeIdForGSC(customerLeId);
		LOGGER.info("SECS Code {}: ", legalEntitySapCodes);
		return legalEntitySapCodes;
	}

	/**
	 * This method return SECS Code related to customer LE ID by flag.
	 *
	 * @param customerLeId
	 * @return {@link String}
	 */
	public String getSECSByCustomerLeIdByFlag(Integer customerLeId) {
		LOGGER.info("Entering method getSECSByCustomerLeId by flag{}: ", customerLeId);
		List<LegalEntitySapCode> legalEntitySapCodes = legalEntitySapCodeRepository.findLesSecsCode(customerLeId);
		Map<String, List<LegalEntitySapCode>> secsCodeByFlag = legalEntitySapCodes.stream().collect(Collectors.groupingBy(LegalEntitySapCode::getSecsSapFlag));
		if(secsCodeByFlag.containsKey(PRIMARY)){
			return secsCodeByFlag.get(PRIMARY).stream().findFirst().get().getCodeValue();
		}
		else if(secsCodeByFlag.containsKey(SECONDARY)){
			return secsCodeByFlag.get(SECONDARY).stream().findFirst().get().getCodeValue();
		}
		else if(secsCodeByFlag.containsKey(TERTIARY)){
			return secsCodeByFlag.get(TERTIARY).stream().findFirst().get().getCodeValue();
		}
		else{
			return null;
		}
	}


	public CustomerLeDetailsBean getSupplierLeAttributesBasedOnProduct(CustomerLeAttributeRequestBean customerLeAttributeRequestBean) throws TclCommonException {
		CustomerLeDetailsBean customerLeDetailsBean = new CustomerLeDetailsBean();
		try {
			if (Objects.isNull(customerLeAttributeRequestBean.getCustomerLeId()) &&
					Objects.isNull(customerLeAttributeRequestBean.getProductName())) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
			}
			ServiceProviderLegalEntity serviceProviderLegalEntity = serviceProviderLegalEntityRepository.findById(customerLeAttributeRequestBean.getCustomerLeId()).get();
			if (Objects.nonNull(serviceProviderLegalEntity)) {
				customerLeDetailsBean.setAccounCuId(serviceProviderLegalEntity.getTpsSfdcCuid());
				customerLeDetailsBean.setAccountId(serviceProviderLegalEntity.getServiceProvider().getTpsSfdcAccountId());
				customerLeDetailsBean.setLegalEntityName(serviceProviderLegalEntity.getEntityName());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return customerLeDetailsBean;
	}
	
	/**
	 * 
	 * Get all Customer legal entity with pagination and search
	 * @param name
	 * @param page
	 * @param size
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<CustomerLegalEntityBean> searchCustomerLegalEntity(String name, Integer page, Integer size,Integer customerId,List<Integer> customerLeIds)
			throws TclCommonException {
		if (page == null || size == null || page <= 0 || size <= 0) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<CustomerLegalEntityBean> customerLeBeanList = new ArrayList<>();
			Page<CustomerLegalEntity> customerLes = null;
			Specification<CustomerLegalEntity> specs = CustomerLegalEntitySpecification.getAllLegalEntities(customerId, name,customerLeIds);
			customerLes = customerLegalEntityRepository.findAll(specs, PageRequest.of(page - 1, size));
			if (customerLes != null) {
				customerLes.stream().forEach(customerLe ->{
					CustomerLegalEntityBean customerLegalEntityBean = constructCustomerLeBean(customerLe);
					if (customerLegalEntityBean != null) {
						customerLeBeanList.add(customerLegalEntityBean);
					}
				});
			}
			if (customerLes != null) {
				return new PagedResult(customerLeBeanList, customerLes.getTotalElements(), customerLes.getTotalPages());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}
	
	private CustomerLegalEntityBean constructCustomerLeBean(CustomerLegalEntity customerLe) {
		if(customerLe!=null) {
			CustomerLegalEntityBean customerLegalEntityBean = new CustomerLegalEntityBean();
			customerLegalEntityBean.setId(customerLe.getId());
			customerLegalEntityBean.setCustomerId(customerLe.getCustomer().getId());
			customerLegalEntityBean.setEntityName(customerLe.getEntityName());
			customerLegalEntityBean.setTpsSfdcCuid(customerLe.getTpsSfdcCuid());
			return customerLegalEntityBean;
		}
		return null;
	}
	
	/**
	 * 
	 * Get all Customer with pagination and search
	 * @param name
	 * @param page
	 * @param size
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<CustomerDetailBean> searchCustomer(String name, Integer page, Integer size)
			throws TclCommonException {
		if (page == null || size == null || page <= 0 || size <= 0) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<CustomerDetailBean> customerBeanList = new ArrayList<>();
			Page<Customer> customers = null;
			Specification<Customer> specs = CustomerSpecification.getAllCustomers(name);
			customers = customerRepository.findAll(specs, PageRequest.of(page - 1, size));
			if (customers != null) {
				customers.stream().forEach(customer ->{
					CustomerDetailBean customerDetailBean = constructCustomerDetailBean(customer);
					if(customerDetailBean!=null)
						customerBeanList.add(customerDetailBean);
				});
			}
			if (customers != null) {
				return new PagedResult(customerBeanList, customers.getTotalElements(), customers.getTotalPages());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}
	
	private CustomerDetailBean constructCustomerDetailBean(Customer customer) {
		if(customer!=null) {
			CustomerDetailBean customerDetailBean = new CustomerDetailBean();
			customerDetailBean.setCustomerId(customer.getId());
			customerDetailBean.setCustomerName(customer.getCustomerName());
			return customerDetailBean;
		}
		return null;
	}
	//rauank
	private void getCustomerLegalEntitesListForMacd(List<CustomerLegalEntity> customerLegalEntities,
											 List<CustomerLegalEntityDto> customerLegalEntityDtos) {
		List<String> locationIds = new ArrayList<>();
		List<Integer> custLeIds = new ArrayList<>();
		Map<String, Integer> customerToLocationMap = new HashMap<>();

		//OPPORTAL-505
		getLocationMappedToCusLeId(customerLegalEntities, locationIds, custLeIds, customerToLocationMap);

//		Map<String, List<LeStateGstInfoBean>> addressDetail =getStateAndCityAccordingToLocationIdQueue(locationIds);

		Map<String, LeStateGstInfoBean> map = new HashMap<>();
		getStateAndCityAccordingToLocationIdQueue(locationIds)
				.entrySet()
				.forEach(entry -> {
					String key = entry.getKey();
					LinkedHashMap<String, String> value = (LinkedHashMap<String, String>) entry.getValue();
					LeStateGstInfoBean leStateGstInfoBean = new LeStateGstInfoBean();


					value.entrySet()
							.forEach(s -> {
								if (s.getKey().equalsIgnoreCase("city"))
									leStateGstInfoBean.setCity(s.getValue());
								if (s.getKey().equalsIgnoreCase("state"))
									leStateGstInfoBean.setState(s.getValue());
							});
					map.put(key, leStateGstInfoBean);
				});

		constructCustomerLegalEntityDto(customerLegalEntities, customerLegalEntityDtos, locationIds, customerToLocationMap, map);

	}

	private void getCustomerLegalEntitesList(List<CustomerLegalEntity> customerLegalEntities,
											 List<CustomerLegalEntityDto> customerLegalEntityDtos) {
		List<String> locationIds = new ArrayList<>();
		List<Integer> custLeIds = new ArrayList<>();
		Map<String, Integer> customerToLocationMap = new HashMap<>();
		customerLegalEntities = getLeIdsForInternalUserNewOrder(customerLegalEntities);

		//OPPORTAL-505
		getLocationMappedToCusLeId(customerLegalEntities, locationIds, custLeIds, customerToLocationMap);

//		Map<String, List<LeStateGstInfoBean>> addressDetail =getStateAndCityAccordingToLocationIdQueue(locationIds);

		Map<String, LeStateGstInfoBean> map = new HashMap<>();
		Map<String, Object> stateCityMapper=getStateAndCityAccordingToLocationIdQueue(locationIds);
		if (stateCityMapper != null) {
			stateCityMapper.entrySet().forEach(entry -> {
				String key = entry.getKey();
				LinkedHashMap<String, String> value = (LinkedHashMap<String, String>) entry.getValue();
				LeStateGstInfoBean leStateGstInfoBean = new LeStateGstInfoBean();

				value.entrySet().forEach(s -> {
					if (s.getKey().equalsIgnoreCase("city"))
						leStateGstInfoBean.setCity(s.getValue());
					if (s.getKey().equalsIgnoreCase("state"))
						leStateGstInfoBean.setState(s.getValue());
				});
				map.put(key, leStateGstInfoBean);
			});
		}
			LOGGER.info("State & city mapped to location ids is : {} ", map);
		constructCustomerLegalEntityDto(customerLegalEntities, customerLegalEntityDtos, locationIds, customerToLocationMap, map);

	}

	private void getPartnerLegalEntitesList(List<PartnerLegalEntity> partnerLegalEntities,
											 List<CustomerLegalEntityDto> customerLegalEntityDtos) {
		List<String> locationIds = new ArrayList<>();
		List<Integer> custLeIds = new ArrayList<>();
		Map<String, Integer> customerToLocationMap = new HashMap<>();
		/*Not Needed for Partner*/
		//partnerLegalEntities = getpartnerLeIdsForInternalUserNewOrder(partnerLegalEntities);

		//OPPORTAL-505
		getLocationMappedToPartnerLeId(partnerLegalEntities, locationIds, custLeIds, customerToLocationMap);

//		Map<String, List<LeStateGstInfoBean>> addressDetail =getStateAndCityAccordingToLocationIdQueue(locationIds);

		Map<String, LeStateGstInfoBean> map = new HashMap<>();
		Map<String, Object> stateCityMapper=getStateAndCityAccordingToLocationIdQueue(locationIds);
		if (stateCityMapper != null) {
			stateCityMapper.entrySet().forEach(entry -> {
				String key = entry.getKey();
				LinkedHashMap<String, String> value = (LinkedHashMap<String, String>) entry.getValue();
				LeStateGstInfoBean leStateGstInfoBean = new LeStateGstInfoBean();

				value.entrySet().forEach(s -> {
					if (s.getKey().equalsIgnoreCase("city"))
						leStateGstInfoBean.setCity(s.getValue());
					if (s.getKey().equalsIgnoreCase("state"))
						leStateGstInfoBean.setState(s.getValue());
				});
				map.put(key, leStateGstInfoBean);
			});
		}
		LOGGER.info("State & city mapped to location ids is : {} ", map);
		constructCustomerLegalEntityDtoForPartner(partnerLegalEntities, customerLegalEntityDtos, locationIds, customerToLocationMap, map);

	}


	private List<CustomerLegalEntity> getLeIdsForInternalUserNewOrder(List<CustomerLegalEntity> customerLegalEntities) {
		String userType = userInfoUtils.getUserType();
		LOGGER.info("User Type {}",userType);
		if (!(userType != null && (userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())))) {
			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			Set<Integer> customersSet = new HashSet<>();
			Set<Integer> customerLeIds = new HashSet<>();
			getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
			LOGGER.info("Total Legal Entities in the list {} - Size {}",customerLeIds,customerLeIds.size());
			List<CustomerLegalEntity> tempList=new ArrayList<>();
			for (CustomerLegalEntity customerLegalEntity : customerLegalEntities) {
				if(customerLeIds.contains(customerLegalEntity.getId())) {
					tempList.add(customerLegalEntity);
				}
			}
			customerLegalEntities=tempList;
		}
		LOGGER.info("Final List Size {}",customerLegalEntities.size());
		return customerLegalEntities;
	}

	private List<PartnerLegalEntity> getpartnerLeIdsForInternalUserNewOrder(List<PartnerLegalEntity> partnerLegalEntities) {
		String userType = userInfoUtils.getUserType();
		if (!(userType != null && userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString()))) {
			Set<Integer> customersSet = new HashSet<>();
			Set<Integer> customerLeIds = new HashSet<>();
				List<PartnerDetail> partnerDetails=userInfoUtils.getPartnerDetails();
			getMapperPartnerDetails(partnerDetails, customersSet, customerLeIds);
			partnerLegalEntities = partnerLegalEntities.stream().filter(cle -> customerLeIds.contains(cle.getId()))
					.collect(Collectors.toList());


		}
		return partnerLegalEntities;
	}

	private void constructCustomerLegalEntityDto(List<CustomerLegalEntity> customerLegalEntities, List<CustomerLegalEntityDto> customerLegalEntityDtos, List<String> locationIds, Map<String, Integer> customerToLocationMap, Map<String, LeStateGstInfoBean> addressDetail) {

		for (CustomerLegalEntity customerLegalEntity : customerLegalEntities) {
			CustomerLegalEntityDto customerLegalEntityDto = new CustomerLegalEntityDto(customerLegalEntity);
			//custLeIds.add(customerLegalEntity.getId());
			List<LeStateGst> leStateGsts = leStateGstRepository.findByCustomerLegalEntity(customerLegalEntity);
			List<BillingAddress> billingAddresses = new ArrayList<>();
			locationIds.forEach(id->{
				if(customerLegalEntity.getId() == customerToLocationMap.get(id)){
					LeStateGstInfoBean leStateGstInfoBean = new LeStateGstInfoBean();
					if(addressDetail.containsKey(id)){

						leStateGstInfoBean.setCity(addressDetail.get(id).getCity());
						leStateGstInfoBean.setState(addressDetail.get(id).getState());

						LOGGER.info("State and city for location id : {} is {} {} and customer le id  {}",id,leStateGstInfoBean.getState(),leStateGstInfoBean.getCity(),customerToLocationMap.get(id));
						for (LeStateGst leStateGst : leStateGsts) {
							if ((Objects.nonNull(leStateGst.getState()) && leStateGst.getState().equalsIgnoreCase(leStateGstInfoBean.getState()))
								/*	&& (Objects.nonNull(leStateGst.getCity())) && leStateGst.getCity().equalsIgnoreCase(leStateGstInfoBean.getCity())*/) {
								BillingAddress billingAddress = new BillingAddress();
								billingAddress.setLeStateGstId(leStateGst.getId());
								billingAddress.setAddress(leStateGst.getAddress());
								billingAddress.setGstn(leStateGst.getGstNo());
								billingAddress.setCity(leStateGst.getCity());
								billingAddress.setState(leStateGst.getState());
								billingAddresses.add(billingAddress);
							}
						}
					}
				}
			});
			customerLegalEntityDto.setCurrency(getCurrencyForCustomerLegalEntity(customerLegalEntity));
			customerLegalEntityDto.setBillingAddresses(billingAddresses);
			customerLegalEntityDtos.add(customerLegalEntityDto);
		}
	}

	private void constructCustomerLegalEntityDtoForPartner(List<PartnerLegalEntity> partnerLegalEntities, List<CustomerLegalEntityDto> customerLegalEntityDtos, List<String> locationIds, Map<String, Integer> customerToLocationMap, Map<String, LeStateGstInfoBean> addressDetail) {

		for (PartnerLegalEntity partnerLegalEntity : partnerLegalEntities) {
			CustomerLegalEntityDto customerLegalEntityDto = new CustomerLegalEntityDto(partnerLegalEntity);
			//custLeIds.add(customerLegalEntity.getId());
			//List<LeStateGst> leStateGsts = leStateGstRepository.findByCustomerLegalEntity(customerLegalEntity);
			List<BillingAddress> billingAddresses = new ArrayList<>();
			locationIds.forEach(id->{
				if(partnerLegalEntity.getId() == customerToLocationMap.get(id)){
					LeStateGstInfoBean leStateGstInfoBean = new LeStateGstInfoBean();
					if(addressDetail.containsKey(id)){

						leStateGstInfoBean.setCity(addressDetail.get(id).getCity());
						leStateGstInfoBean.setState(addressDetail.get(id).getState());


						LOGGER.info("State and city for location id : {} is {} {} and customer le id  {}",id,leStateGstInfoBean.getState(),leStateGstInfoBean.getCity(),customerToLocationMap.get(id));
//						for (LeStateGst leStateGst : leStateGsts) {
//							if ((Objects.nonNull(leStateGst.getState()) && leStateGst.getState().equalsIgnoreCase(leStateGstInfoBean.getState()))
//								/*	&& (Objects.nonNull(leStateGst.getCity())) && leStateGst.getCity().equalsIgnoreCase(leStateGstInfoBean.getCity())*/) {
//								BillingAddress billingAddress = new BillingAddress();
//								billingAddress.setLeStateGstId(leStateGst.getId());
//								billingAddress.setAddress(leStateGst.getAddress());
//								billingAddress.setGstn(leStateGst.getGstNo());
//								billingAddress.setCity(leStateGst.getCity());
//								billingAddress.setState(leStateGst.getState());
//								billingAddresses.add(billingAddress);
//							}
//						}
					}
				}
			});
			Set<PartnerLeCountry> partnerLeCountries = partnerLegalEntity.getPartnerLeCountries();
			if (partnerLeCountries != null && !partnerLeCountries.isEmpty()) {
				Integer countryId=partnerLeCountries.stream().map(PartnerLeCountry::getCountryId).findFirst().get();
				customerLegalEntityDto.setCountry(getCountryNameById(countryId));
			}
			customerLegalEntityDto.setCurrency(getCurrencyForPartnerLegalEntity(partnerLegalEntity));
			customerLegalEntityDto.setBillingAddresses(billingAddresses);
			customerLegalEntityDtos.add(customerLegalEntityDto);
		}
	}
	private void getLocationMappedToCusLeId(List<CustomerLegalEntity> customerLegalEntities, List<String> locationIds, List<Integer> custLeIds, Map<String, Integer> customerToLocationMap) {
		customerLegalEntities.forEach(cle-> { custLeIds.add(cle.getId()); });
		LOGGER.info("custLeIds {} ", custLeIds);
		Integer customerContractingEntityId = mstLeAttributeRepository.findByName(CUSTOMER_CONTRACTING_ENTITY).get().getId();
		LOGGER.info("customerContractingEntityId :: {}", customerContractingEntityId);
		custLeIds.forEach(custLeId-> {
			String locationId = customerLeAttributeRepository
					.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(custLeId,customerContractingEntityId).get(0).getAttributeValues();
			locationIds.add(locationId);
			customerToLocationMap.put(locationId, custLeId);
			LOGGER.info("Customer to Location Map : {} ", customerToLocationMap);
		});
	}

	private void getLocationMappedToPartnerLeId(List<PartnerLegalEntity> partnerLegalEntities, List<String> locationIds, List<Integer> custLeIds, Map<String, Integer> customerToLocationMap) {
		partnerLegalEntities.forEach(cle-> { custLeIds.add(cle.getId()); });
		LOGGER.info("custLeIds {} ", custLeIds);
		Integer customerContractingEntityId = mstLeAttributeRepository.findByName(CUSTOMER_CONTRACTING_ENTITY).get().getId();
		LOGGER.info("customerContractingEntityId :: {}", customerContractingEntityId);
		custLeIds.forEach(custLeId-> {
			String locationId = partnerLeAttributeValueRepository
					.findByPartnerLegalEntity_IdAndMstLeAttribute_Id(custLeId,customerContractingEntityId).get(0).getAttributeValues();
			locationIds.add(locationId);
			customerToLocationMap.put(locationId, custLeId);
			LOGGER.info("Customer to Location Map : {} ", customerToLocationMap);
		});
	}

	private Map<String, Object> getStateAndCityAccordingToLocationIdQueue (List<String> locationIdsList) {

		try {
			String locCommaSeparated = locationIdsList.stream().map(i -> i.trim())
					.collect(Collectors.joining(","));
			LOGGER.info("Location ids: {} ", locCommaSeparated);
			String response = (String) mqUtils.sendAndReceive(custLeIdToStateQueue, locCommaSeparated);
			LOGGER.info("response from location queue : {}",response);
			Map<String, Object> addressDetail = Utils.convertJsonToObject(response,
					HashMap.class);
			LOGGER.info("address details: {} ", addressDetail);
			return addressDetail;
		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}
		return null;

	}


	/**
	 * getSpAttributes
	 *
	 * @param spLegelId
	 */
	public String getSpAttributesValue(Integer spLegelId, Integer mstLeAttrId) {
		SpLeAttributeValue spLeAttributeValue = spLeAttributeValueRepository.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(21,5);
			if (Objects.nonNull(spLeAttributeValue)) {
				System.out.println(spLeAttributeValue.getAttributeValues());
				
				return spLeAttributeValue.getAttributeValues();
			}
		
		return "";
	}
	
	public String getGscSpAttributesValue(Integer spLegelId, Integer mstLeAttrId) {
		SpLeAttributeValue spLeAttributeValue = spLeAttributeValueRepository.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(mstLeAttrId, spLegelId);
			if (Objects.nonNull(spLeAttributeValue)) {
				System.out.println(spLeAttributeValue.getAttributeValues());
				return spLeAttributeValue.getAttributeValues();
			}
		return "";
	}

	/**
	 * Load Mst_Dop_Matrix values from Excel
	 *
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */

	public Object dopExcelMapping(MultipartFile file) throws InvalidFormatException, IOException, TclCommonException
	{
		String response = "";
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
			Sheet worksheet = workbook.getSheetAt(0);


			MstDopMatrix mstDopMatrix = new MstDopMatrix();
			for (Sheet sheet : workbook) {
				for (int i = 1; i <= getLastRowWithData(sheet); i++) {

					Cell firstCell = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
					double sno = Double.parseDouble(firstCell.toString());
					int snum = (int)sno;
					mstDopMatrix.setId(snum);
					Cell regionCell = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
					if(regionCell != null){
						mstDopMatrix.setRegion(regionCell.toString());}
					else
						mstDopMatrix.setRegion("");
					Cell lOneCell = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
					mstDopMatrix.setLevelOneName(lOneCell.toString());
					Cell lOneMailCell = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
					mstDopMatrix.setLevelOneEmail(lOneMailCell.toString());
					Cell lTwoCell = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
					mstDopMatrix.setLevelTwoName(lTwoCell.toString());
					Cell lTwoMailCell = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
					mstDopMatrix.setLevelTwoEmail(lTwoMailCell.toString());
					Cell lThreeCell = sheet.getRow(i).getCell(6, xRow.RETURN_BLANK_AS_NULL);
					mstDopMatrix.setLevelThreeName(lThreeCell.toString());
					Cell lThreeMailCell = sheet.getRow(i).getCell(7, xRow.RETURN_BLANK_AS_NULL);
					mstDopMatrix.setLevelThreeEmail(lThreeMailCell.toString());
					Cell segmentCell = sheet.getRow(i).getCell(8, xRow.RETURN_BLANK_AS_NULL);
					String segmentName = segmentCell.toString();
					Optional<MstCustomerSegment> mstCustomerSegment = mstCustomerSegmentRepository.findByName(segmentName);
					mstDopMatrix.setCustomerSegment(mstCustomerSegment.get());
					mstDopMatrix.setDopSigningLevel("TWO");
					mstDopMatrix.setCreatedBy("root");
					mstDopMatrix.setCreatedTime(new java.util.Date());
					mstDopMatrixRepository.saveAndFlush(mstDopMatrix);

				}
			}
			response = "success";
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	/**
	 * Get the index of last row in excel
	 *
	 * @param currentSheet
	 * @return rowCount
	 * @throws TclCommonException
	 */

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

	/**
	 * Find if row is blank in excel
	 *
	 * @param r
	 * @return ret
	 * @throws TclCommonException
	 */
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

	/**
	 * Find if cell is blank in excel
	 *
	 * @param c
	 * @return
	 * @throws TclCommonException
	 */
	public boolean isCellBlank(Cell c) {
		return (c == null || c.getCellTypeEnum() == CellType.BLANK);
	}

	/**
	 * Find if cell is empty in excel
	 *
	 * @param c
	 * @return
	 * @throws TclCommonException
	 */
	public boolean isCellEmpty(Cell c) {
		return c == null || c.getCellTypeEnum() == CellType.BLANK
				|| (c.getCellTypeEnum() == CellType.STRING && c.getStringCellValue().isEmpty());

	}

	/**
	 * Method to get dop email
	 *
	 * @param customerSegment
	 * @param orderValue
	 * @return
	 */
	public Map<String, String> getDopEmail(String accountManagerName, String customerSegment, Double orderValue) {
		String dopSigningLevel = "ZERO";
		Map<String, String> dopMapper = new HashMap<>();
		Optional<MstCustomerSegment> segment = mstCustomerSegmentRepository.findByName(customerSegment);
		if (segment.isPresent()) {
			MstDopMatrix dopMatrix = mstDopMatrixRepository.findByLevelOneNameAndCustomerSegment(accountManagerName, segment.get());
			if (Objects.nonNull(dopMatrix)) {
				List<MstDopMarker> dopMarkers = mstDopMarkerRepository.findByCustomerSegment(segment.get());
				if (!dopMarkers.isEmpty()) {
					for (MstDopMarker marker : dopMarkers) {
						LOGGER.info("ORDER VALUE" + orderValue);
						LOGGER.info("MARKER FROM VALUE" + marker.getFromValue());
						LOGGER.info("MARKER TO VALUE" + marker.getToValue());
						int toValueResult = marker.getToValue().compareTo(0D);
						int fromValueResult = marker.getFromValue().compareTo(0D);
						LOGGER.info("To Value Result--" + toValueResult);
						LOGGER.info("From Value Result--" + fromValueResult);
						if (toValueResult == 0) {
							if (orderValue > marker.getFromValue()) {
								dopSigningLevel = marker.getDopLevel();
								break;
							}
						} else if (fromValueResult == 0) {
							if ((orderValue >= marker.getFromValue()) && orderValue <= marker.getToValue()) {
								dopSigningLevel = marker.getDopLevel();
								break;
							}
						} else {
							if ((orderValue > marker.getFromValue()) && orderValue <= marker.getToValue()) {
								dopSigningLevel = marker.getDopLevel();
								break;
							}
						}

					}
				}
				
				dopMapper = getEmailBasedOnSigningLevel(dopMatrix, dopSigningLevel);
			}
		}
		return dopMapper;
	}

	/**
	 * Method to get email based on signinglevel
	 * @param matrix
	 * @param signingLevel
	 * @return
	 */
	public Map<String, String> getEmailBasedOnSigningLevel(MstDopMatrix matrix, String signingLevel) {
		Map<String, String> dopMapper = new HashMap<>();
		String email = null;
		String name = null;
		LOGGER.info("Signing Level" + signingLevel);
		switch (signingLevel) {
		case "ONE":
			email = matrix.getLevelOneEmail();
			name = matrix.getLevelOneName();
			break;
		case "TWO":
			email = matrix.getLevelTwoEmail();
			name = matrix.getLevelTwoName();
			break;
		case "THREE":
			email = matrix.getLevelThreeEmail();
			name = matrix.getLevelThreeName();
			break;
		default:
			email = matrix.getLevelTwoEmail();
			name = matrix.getLevelTwoName();
			break;
		}
		if (email != null) {
			dopMapper.put("NAME", name);
			dopMapper.put("EMAIL", email);
		}
		return dopMapper;
	}


	/**
	 * Get Customer Le CUID
	 *
	 * @param customerLeId
	 * @return tpsSfdcCuid
	 */
	public String getCustomerLeCuID(String customerLeId) {
		CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findAllById(Integer.valueOf(customerLeId));
		String tpsSfdcCuid = customerLegalEntity.getTpsSfdcCuid();
		LOGGER.info("Customer Le SFDC CUID :: {}", tpsSfdcCuid);
		return tpsSfdcCuid;
	}
	
	/**
	 * This method return All SECS Code related to customer LE ID.
	 * 
	 * @param customerLeId
	 * @return
	 */
	public List<String> getAllSECSByCustomerLeId(Integer customerLeId) {
		LOGGER.info("Entering method getSECSByCustomerLeId {}: ", customerLeId);
		List<String> sapCodes = new ArrayList<>();
		List<LegalEntitySapCode> legalEntitySapCodes = legalEntitySapCodeRepository.findCustomerleIdSapDetails(customerLeId);
		if(legalEntitySapCodes!=null) {
			legalEntitySapCodes.stream().forEach(legalEntitySapCode->{
				sapCodes.add(legalEntitySapCode.getCodeValue());
			});
		}
		return sapCodes;
	}
	
	public Map<Integer,Integer> getCustomerForLe(List<Integer> customerLeIds) {
		Map<Integer,Integer> mapper=new HashMap<>();
		for (Integer customerLeId : customerLeIds) {
			Optional<CustomerLegalEntity> customerLegalEntity=customerLegalEntityRepository.findById(customerLeId);
			if(customerLegalEntity.isPresent()) {
				mapper.put(customerLeId, customerLegalEntity.get().getCustomer().getId());
			}
		}
		LOGGER.info("Response for customerLeIds {} is {}",customerLeIds,mapper);
		return mapper;
	}
	
	public Map<Integer,Integer> getPartnerForLe(List<Integer> partnerLeIds) {
		Map<Integer,Integer> mapper=new HashMap<>();
		for (Integer partnerLeId : partnerLeIds) {
			Optional<PartnerLegalEntity> partnerLegalEntity=partnerLegalEntityRepository.findById(partnerLeId);
			if(partnerLegalEntity.isPresent()) {
				mapper.put(partnerLeId, partnerLegalEntity.get().getPartner().getId());
			}
		}
		LOGGER.info("Response for partnerLeIds {} is {}",partnerLeIds,mapper);
		return mapper;
	}
	
	public IzosdwanSupplierResponseBean getAllSupplierDetailsBasedOnQuote(Integer quoteId, Integer customerLeId,Boolean isByonOnly)
			throws TclCommonException {
		IzosdwanSupplierResponseBean izosdwanSupplierResponseBean = new IzosdwanSupplierResponseBean();
		try {
			CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findAllById(customerLeId);
			if (customerLegalEntity == null) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
						ResponseResource.R_CODE_ERROR);
			}

			Set<CustomerLeCountry> customerLeCountrys = customerLegalEntity.getCustomerLeCountries();
			if (customerLeCountrys != null && !customerLeCountrys.isEmpty()) {
				CustomerLeCountry customerLeCountry = customerLeCountrys.stream().findFirst().orElse(null);
				if (customerLeCountry != null) {
					LOGGER.info("Got CLE Country information ");
					String responseFromOms = (String) mqUtils.sendAndReceive(omsCountriesQueue, quoteId.toString());
					if (responseFromOms != null) {
						LOGGER.info("Got country details for the quote from OMS {}", responseFromOms);
						izosdwanSupplierResponseBean.setIsLouRequired(true);
						izosdwanSupplierResponseBean.setIsTaxClearanceDocumentRequired(false);
						List<String> countries = IzosdwanUtils.fromJson(responseFromOms,
								new TypeReference<List<String>>() {
								});
						if (countries != null && !countries.isEmpty()) {
							List<String> neCountries = mstCountryRepository.getNonEntityCountries();
							Boolean isInternationalCle = customerLeCountry.getMstCountry().getName()
									.equals(IzosdwanCommonConstants.INDIA) ? false : true;
							LOGGER.info("isInternationalCle flag details [}", isInternationalCle);
							String quoteLocationType = IzosdwanCommonConstants.INDIA_ONLY;
							if(isByonOnly!=null && isByonOnly) {
								quoteLocationType = getQuoteLocationTypesBySiteCountries(neCountries, countries,
										customerLeCountry.getMstCountry().getName(), isInternationalCle);
							}
							LOGGER.info("Quote location type got is {}", quoteLocationType);
							List<ServiceProviderLegalEntity> serviceProviderLegalEntities = new ArrayList<>();
							List<SpLeCountry> spLeCountries = new ArrayList<>();
							if (quoteLocationType != null) {
								switch (quoteLocationType) {
								case IzosdwanCommonConstants.INDIA_ONLY: {
									List<String> spLeCountry = new ArrayList<>();
									spLeCountry.add(IzosdwanCommonConstants.INDIA);
									spLeCountries = spleCountryRepository.findByMstCountry_NameIn(spLeCountry);
									izosdwanSupplierResponseBean.setIsLouRequired(false);
									if (isInternationalCle && isByonOnly!=null && isByonOnly) {
										izosdwanSupplierResponseBean.setIsTaxClearanceDocumentRequired(true);
									}
									break;
								}
								case IzosdwanCommonConstants.INDIA_INTL: {

									if (!isInternationalCle) {
										List<String> spLeCountry = new ArrayList<>();
										spLeCountry.add(IzosdwanCommonConstants.INDIA);
										spLeCountries = spleCountryRepository.findByMstCountry_NameIn(spLeCountry);
									}
									break;
								}
								case IzosdwanCommonConstants.INDIA_INTL_NE: {
									if (!isInternationalCle) {
										List<String> spLeCountry = new ArrayList<>();
										spLeCountry.add(IzosdwanCommonConstants.INDIA);
										spLeCountries = spleCountryRepository.findByMstCountry_NameIn(spLeCountry);
									}
									break;
								}
								case IzosdwanCommonConstants.INTL_ONLY: {
									if (!isInternationalCle) {
										spLeCountries = spleCountryRepository.findByMstCountry_NameIn(countries);
										izosdwanSupplierResponseBean.setIsTaxClearanceDocumentRequired(true);
									}
									break;
								}
								case IzosdwanCommonConstants.NE_ONLY: {
									spLeCountries = spleCountryRepository.findAll();
									izosdwanSupplierResponseBean.setIsTaxClearanceDocumentRequired(true);
									break;
								}
								case IzosdwanCommonConstants.SINTL_ONLY: {
									List<String> spLeCountry = new ArrayList<>();
									spLeCountry.add(customerLeCountry.getMstCountry().getName());
									spLeCountries = spleCountryRepository.findByMstCountry_NameIn(spLeCountry);
									break;
								}
								case IzosdwanCommonConstants.SINTL_OINTL_NE: {
									List<String> spLeCountry = new ArrayList<>();
									spLeCountry.add(customerLeCountry.getMstCountry().getName());
									spLeCountries = spleCountryRepository.findByMstCountry_NameIn(spLeCountry);
									break;
								}
								case IzosdwanCommonConstants.SINTL_NE: {
									List<String> spLeCountry = new ArrayList<>();
									spLeCountry.add(customerLeCountry.getMstCountry().getName());
									spLeCountries = spleCountryRepository.findByMstCountry_NameIn(spLeCountry);
									break;
								}
								case IzosdwanCommonConstants.OINTL_ONLY: {
									if (isInternationalCle) {
										spLeCountries = spleCountryRepository.findByMstCountry_NameIn(countries);
										izosdwanSupplierResponseBean.setIsTaxClearanceDocumentRequired(true);
									}
									break;
								}
								case IzosdwanCommonConstants.SINTL_OINTL_INDIA:{
									if (isInternationalCle) {
										List<String> spLeCountry = new ArrayList<>();
										spLeCountry.add(customerLeCountry.getMstCountry().getName());
										spLeCountries = spleCountryRepository.findByMstCountry_NameIn(spLeCountry);
									}
									break;
								}
								case IzosdwanCommonConstants.OTHER_TYPES: {
									spLeCountries = spleCountryRepository.findAll();
									izosdwanSupplierResponseBean.setIsTaxClearanceDocumentRequired(true);
									break;
								}
								default: {
									break;
								}
								}
								if (spLeCountries != null && !spLeCountries.isEmpty()) {
									LOGGER.info("Sp Le countries based on logic");
									serviceProviderLegalEntities = spLeCountries.stream()
											.filter(spLeCoun -> spLeCoun.getServiceProviderLegalEntity() != null)
											.map(spLeCoun -> spLeCoun.getServiceProviderLegalEntity()).distinct()
											.collect(Collectors.toList());
								}
								if (serviceProviderLegalEntities != null && !serviceProviderLegalEntities.isEmpty()) {
									LOGGER.info("Service provider Legal entity details ");
									List<IzosdwanSupplierBean> izosdwanSupplierBeans = new ArrayList<>();
									serviceProviderLegalEntities.stream().forEach(spLe -> {
										izosdwanSupplierBeans.add(constructSupplierDetails(spLe));
									});
									izosdwanSupplierResponseBean.setIzosdwanSupplierBeans(izosdwanSupplierBeans);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occured while finding the supplier", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return izosdwanSupplierResponseBean;
	}
	
	private String getQuoteLocationTypesBySiteCountries(List<String> neCountries, List<String> countries,
			String cleCountry, Boolean isCleInternational) {
		List<String> indiaCountries = countries.stream()
				.filter(country -> country.equals(IzosdwanCommonConstants.INDIA)).collect(Collectors.toList());
		List<String> intlCountries = countries.stream()
				.filter(country -> !country.equals(IzosdwanCommonConstants.INDIA)).collect(Collectors.toList());
		List<String> neSitesCountries = new ArrayList<>();
		countries.stream().forEach(country->{
			if(neCountries.stream().filter(ne->ne.equals(country)).findAny().isPresent()) {
				neSitesCountries.add(country);
			}
		});
		List<String> intlNeCountries = new ArrayList<>();
		List<String> sameIntlCountries = new ArrayList<>();
		Boolean isCleCountryNE = false;
		if (intlCountries != null && !intlCountries.isEmpty()) {
			if (neSitesCountries != null && !neSitesCountries.isEmpty() && neSitesCountries.size()!=intlCountries.size()) {
				intlCountries.stream().forEach(country->{
					if(neSitesCountries.stream().filter(ne->ne.equals(country)).findFirst().isPresent()) {
						intlNeCountries.add(country);
					}
				});
			}
			if (isCleInternational) {
				sameIntlCountries = intlCountries.stream().filter(country -> country.equalsIgnoreCase(cleCountry))
						.collect(Collectors.toList());
				if (sameIntlCountries != null && !sameIntlCountries.isEmpty()
						&& sameIntlCountries.size() == countries.size()) {
					isCleCountryNE = true;
				}
			}
		}
		if (indiaCountries != null && !indiaCountries.isEmpty() && indiaCountries.size() == countries.size()) {
			return IzosdwanCommonConstants.INDIA_ONLY;
		} else if (sameIntlCountries != null && !sameIntlCountries.isEmpty()
				&& sameIntlCountries.size() == countries.size()) {
			return IzosdwanCommonConstants.SINTL_ONLY;
		} else if (neSitesCountries != null && !neSitesCountries.isEmpty()
				&& neSitesCountries.size() == countries.size()) {
			return IzosdwanCommonConstants.NE_ONLY;
		} else if (intlCountries != null && !intlCountries.isEmpty()
				&& (neSitesCountries == null || neSitesCountries.isEmpty()) && !isCleInternational && intlCountries.size() == countries.size()) {
				return IzosdwanCommonConstants.INTL_ONLY;
			
		} else if (indiaCountries != null && !indiaCountries.isEmpty() && intlCountries != null
				&& !intlCountries.isEmpty()) {
			if (!isCleInternational) {
				if (neSitesCountries != null && !neSitesCountries.isEmpty()) {
					return IzosdwanCommonConstants.INDIA_INTL_NE;
				} else {
					return IzosdwanCommonConstants.INDIA_INTL;
				}
			} 
		} else if ((indiaCountries == null || indiaCountries.isEmpty()) && intlCountries != null
				&& !intlCountries.isEmpty()) {
			
			if (sameIntlCountries != null && !sameIntlCountries.isEmpty() && neSitesCountries != null
					&& !neSitesCountries.isEmpty()
					&& !(sameIntlCountries.size() + neSitesCountries.size() == countries.size())) {
				return IzosdwanCommonConstants.SINTL_OINTL_NE;

			} else if (sameIntlCountries != null && !sameIntlCountries.isEmpty() && neSitesCountries != null
					&& !neSitesCountries.isEmpty()
					&& (sameIntlCountries.size() + neSitesCountries.size() == countries.size())) {
				return IzosdwanCommonConstants.SINTL_NE;
			} else if ((sameIntlCountries == null || sameIntlCountries.isEmpty())
					&& (neSitesCountries == null || neSitesCountries.isEmpty()) && isCleInternational) {
				return IzosdwanCommonConstants.OINTL_ONLY;
			}
		}
		if (sameIntlCountries != null && !sameIntlCountries.isEmpty() && (neSitesCountries == null
				|| neSitesCountries.isEmpty()) && indiaCountries != null && !indiaCountries.isEmpty()
				&& (sameIntlCountries.size() + neCountries.size() != countries.size())) {
			return IzosdwanCommonConstants.SINTL_OINTL_INDIA;
		}
		return IzosdwanCommonConstants.OTHER_TYPES;
	}
	
	private IzosdwanSupplierBean constructSupplierDetails(ServiceProviderLegalEntity serviceProviderLegalEntity) {
		IzosdwanSupplierBean izosdwanSupplierBean = new IzosdwanSupplierBean();
		
		List<String> currencyList = new ArrayList<>();
		serviceProviderLegalEntity.getSpLeCurrencies().stream().forEach(spLeCurrency->{
			if(spLeCurrency.getCurrencyMaster()!=null) {
			currencyList.add(spLeCurrency.getCurrencyMaster()!=null?spLeCurrency.getCurrencyMaster().getShortName():"USD");
			}
		});
		izosdwanSupplierBean.setCurrency(currencyList);
		izosdwanSupplierBean.setSupplierId(serviceProviderLegalEntity.getId());
		izosdwanSupplierBean.setSupplierName(serviceProviderLegalEntity.getEntityName());
		return izosdwanSupplierBean;
	}

	/**
	 * Method to get list of customer contracting addresses based on legalEntity Id
	 * @param customerLeId
	 * @return CustomerContractingAddressResponseDto
	 * @throws TclCommonException
	 */
	public CustomerContractingAddressResponseDto getCustomerContractingAddressesByCustomerLeId(Integer customerLeId)throws TclCommonException
	{
		CustomerContractingAddressResponseDto customerContractingAddresses=new CustomerContractingAddressResponseDto();
		List<CustomerContractingAddressInfo> customerContractingInfoList=new ArrayList<>();

		if (Objects.isNull(customerLeId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		Optional<CustomerLegalEntity> customerLegalEntity=customerLegalEntityRepository.findById(customerLeId);
		if(customerLegalEntity.isPresent())
		{
			customerContractingAddresses.setLegalEntityId(customerLegalEntity.get().getId());
			List<LeCcaAddress> ccaAddresses=leCcaAddressRepository.findByCustomerLegalEntity(customerLegalEntity.get());
			List<String> locationIdList=new ArrayList<>();
			ccaAddresses.stream().forEach(ccaAddress-> {
				locationIdList.add(ccaAddress.getLocationId().toString());
					});
			LOGGER.info("LocationId List-"+locationIdList);
				Map<Integer,AddressDetail> addressDetailMap=getAddressDetailByLocationId(locationIdList);
			ccaAddresses.stream().forEach(ccaAddress-> {
				List<GstnInfo> gstnInfos=new ArrayList<>();
				CustomerContractingAddressInfo customerContractingAddressInfo=new CustomerContractingAddressInfo();
			try {
					customerContractingAddressInfo.setLocationId(ccaAddress.getLocationId());
					customerContractingAddressInfo.setIsDefault(ccaAddress.getIsDefault());
					AddressDetail addressDetail =(AddressDetail)addressDetailMap.get(ccaAddress.getLocationId());
					customerContractingAddressInfo.setAddress(addressDetail);
					LOGGER.info("Location Id"+ccaAddress.getLocationId()+"addressDetail"+addressDetail);
					if(Objects.nonNull(addressDetail.getState())) {

						List<LeStateGst> leStateGsts = leStateGstRepository.findByCustomerLegalEntityAndState(ccaAddress.getCustomerLegalEntity(), addressDetail.getState());
						leStateGsts.stream().forEach(leStateGst -> {
							GstnInfo gstn=new GstnInfo();
							gstn.setLeStateGstId(leStateGst.getId());
							gstn.setAddress(leStateGst.getAddress());
							gstn.setCity(leStateGst.getCity());
							gstn.setGstn(leStateGst.getGstNo());
							gstn.setState(leStateGst.getState());
							gstnInfos.add(gstn);
						});
					}
					customerContractingAddressInfo.setGstnInfoList(gstnInfos);
				}
			catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
				customerContractingInfoList.add(customerContractingAddressInfo);
			});
		}
		else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		customerContractingAddresses.setCustomerContractingAddressInfoList(customerContractingInfoList);
		return customerContractingAddresses;
	}

	/**
	 * Method to get addressDetail by locationId
	 * @param locationIdsList
	 * @return
	 */
	private Map<Integer, AddressDetail> getAddressDetailByLocationId (List<String> locationIdsList) {

		try {
			String locCommaSeparated = locationIdsList.stream().map(i -> i.trim())
					.collect(Collectors.joining(","));
			LOGGER.info("Location ids: {} ", locCommaSeparated);
			LocationDetail[] locationDetails=getAddressList(locCommaSeparated);
			List<LocationDetail> locationDetailList=Arrays.asList(locationDetails);
			LOGGER.info("LocationDetails List"+locationDetailList.toString());
			Map<Integer, AddressDetail> addressDetail=new HashMap<>();
			locationDetailList.stream().forEach(locationDetail -> {
				LOGGER.info("Location Id"+locationDetail.getLocationId());
				LOGGER.info("Location Address"+locationDetail.getUserAddress());
				addressDetail.put(locationDetail.getLocationId(),locationDetail.getUserAddress());

			});

			LOGGER.info("address details: {} ", addressDetail);
			return addressDetail;
		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}
		return null;

	}

	/**
	 * @author Thamizhselvi Perumal
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	private LocationDetail[] getAddressList(String locCommaSeparated) throws TclCommonException {
		try {
			String response = (String) mqUtils.sendAndReceive(locationDetailQueue, locCommaSeparated);
			LOGGER.info("Output Payload for location details {}", response);
			LocationDetail[] locDetails = (LocationDetail[]) Utils.convertJsonToObject(response,
					LocationDetail[].class);
			return locDetails;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}


	/**
	 * Get GST Number By Customer Le Id
	 *
	 * @param customerLeId
	 * @return
	 */
	public List<String> getGstNumbersByCustomerLeId(Integer customerLeId) {
		List<CustomerLeAttributeValue> leGsts = customerLeAttributeRepository.findGstByCustomerLegalEntityId(customerLeId);
		List<String> gstNos = new ArrayList<>();
		if (Objects.nonNull(leGsts) && !leGsts.isEmpty()) {
			leGsts.stream().forEach(stateGst -> {
				gstNos.add(stateGst.getAttributeValues());
			});
		}
		return gstNos;
	}

	/**
	 * Get currency value by ID
	 *
	 * @param currencyId
	 */
	public String getCurrencyById(Integer currencyId) {
		return currencyMasterRepository.findById(currencyId).get().getShortName();
	}

	/**
	 * Method to get all supplier details
	 *
	 * @return {@link List<CustomerLegalEntityProductResponseDto>}
	 * @throws TclCommonException
	 * @param customerId
	 */
	public List<CustomerLegalEntityProductResponseDto> getAllSupplierDetails(Integer customerId) {
		List<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = new ArrayList<>();
		List<Map<String, Object>> allSupplierDetails = serviceProviderLegalEntityRepository.findAllSupplierDetails();
		if (!CollectionUtils.isEmpty(allSupplierDetails)) {
			customerLegalEntityProductResponseDtos = allSupplierDetails.stream().map(this::constructCustomerLegalEntityProductResponseDto).collect(Collectors.toList());
		} else {
			LOGGER.info("No Supplier legal entities");
		}
		List<CustomerLegalEntityProductResponseDto> copyCustomerLegalEntityProductResponseDtos = customerLegalEntityProductResponseDtos;

		Map<String, String> supplierByCurrencyMap = new HashMap<>();
		customerLegalEntityProductResponseDtos.stream().forEach(dto -> {
			if(supplierByCurrencyMap.isEmpty() || !supplierByCurrencyMap.containsKey(dto.getSple())){
				String currency = copyCustomerLegalEntityProductResponseDtos.stream().filter(copyDto -> copyDto.getSple().equalsIgnoreCase(dto.getSple()))
						.map(CustomerLegalEntityProductResponseDto::getCurrency).collect(joining(","));
				supplierByCurrencyMap.put(dto.getSple(), currency);

			}
		});

		Map<String, Integer> supplierByIdMap = new HashMap<>();
		customerLegalEntityProductResponseDtos.stream().forEach(dto -> {
			if(!supplierByIdMap.containsKey(dto.getSple())){
				Integer serviceProviderId = copyCustomerLegalEntityProductResponseDtos.stream().filter(copyDto -> copyDto.getSple().equalsIgnoreCase(dto.getSple())).map(CustomerLegalEntityProductResponseDto::getServiceProviderId).findFirst().get();
				supplierByIdMap.put(dto.getSple(), serviceProviderId);
			}
		});

		List<CustomerLegalEntityProductResponseDto> finalCustomerLegalEntityProductResponseDtos = supplierByCurrencyMap.entrySet().stream().map(supplierByCurrency -> {
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();
			String supplierName = supplierByCurrency.getKey();
			customerLegalEntityProductResponseDto.setSple(supplierName);
			customerLegalEntityProductResponseDto.setCurrency(supplierByCurrency.getValue());
			customerLegalEntityProductResponseDto.setServiceProviderId(supplierByIdMap.get(supplierName));
			return customerLegalEntityProductResponseDto;
		}).collect(Collectors.toList());
		return finalCustomerLegalEntityProductResponseDtos;
	}

	/**
	 * Construct CustomerLegalEntityProductResponseDto based on map from repository
	 *
	 * @param supplierDetails
	 * @return {@link CustomerLegalEntityProductResponseDto}
	 */
	private CustomerLegalEntityProductResponseDto constructCustomerLegalEntityProductResponseDto(Map<String, Object> supplierDetails) {
		CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();
		customerLegalEntityProductResponseDto.setSple((String) supplierDetails.get("supplierEntityName"));
		customerLegalEntityProductResponseDto.setServiceProviderId((Integer) supplierDetails.get("serviceProviderId"));
		customerLegalEntityProductResponseDto.setCurrency((String) supplierDetails.get("currency"));
		return customerLegalEntityProductResponseDto;
	}

	public ServiceResponse processUploadFilesSDD(MultipartFile file, Integer orderToLeId, Integer quoteToLeId,
			String attachmentType, String referenceName, Integer customerLeId,
			String productName,Integer referenceId) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();		
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				InputStream inputStream = file.getInputStream();
				StoredObject storedObject = fileStorageService.uploadObjectSDD(file.getOriginalFilename(),
						inputStream, referenceId);
				String[] pathArray = storedObject.getPath().split("/");

				com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
				attachmentBean.setPath(pathArray[1]);
				//attachmentBean.setFileName(file.getOriginalFilename());
				attachmentBean.setFileName(storedObject.getName());
				Attachment attachment = attachmentService.processAttachmentSDD(attachmentBean);
				fileUploadResponse.setAttachmentId(attachment.getId());
				fileUploadResponse.setStatus(Status.SUCCESS);
				fileUploadResponse.setFileName(attachmentBean.getFileName());
				fileUploadResponse.setUrlPath(attachmentBean.getPath());

				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				OmsAttachBean omsAttachBean = new OmsAttachBean();
				omsAttachBean.setAttachmentId(attachment.getId());
				omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
				omsAttachBean.setOrderLeId(orderToLeId);
				omsAttachBean.setQouteLeId(quoteToLeId);
				omsAttachBean.setReferenceId(referenceId);
				omsAttachBean.setReferenceName(referenceName);
				omsAttachBeanList.add(omsAttachBean);

				OmsListenerBean listenerBean = new OmsListenerBean();
				OmsAttachBean omsAttachBean2=new OmsAttachBean();
				omsAttachBean2.setReferenceName(file.getOriginalFilename());
                
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				listenerBean.setOmsAttachmentBean(omsAttachBean2);
				
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(omsAttachmentQueueSDD, oattachmentrequest);	

			} else {
				validateOmsRequestSDD(file, orderToLeId, quoteToLeId, attachmentType, referenceId, referenceName,
						customerLeId);
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
				com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
				attachmentBean.setPath(newFolder);
				attachmentBean.setFileName(file.getOriginalFilename());
				Attachment attachment = attachmentService.processAttachmentSDD(attachmentBean);
				fileUploadResponse.setAttachmentId(attachment.getId());

				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				OmsAttachBean omsAttachBean = new OmsAttachBean();
				omsAttachBean.setAttachmentId(attachment.getId());
				omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
				omsAttachBean.setOrderLeId(orderToLeId);
				omsAttachBean.setQouteLeId(quoteToLeId);
				omsAttachBean.setReferenceId(referenceId);
				omsAttachBean.setReferenceName(referenceName);
				omsAttachBeanList.add(omsAttachBean);
				OmsAttachBean omsAttachBean2=new OmsAttachBean();
				omsAttachBean2.setReferenceName(file.getOriginalFilename());
				
				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				listenerBean.setOmsAttachmentBean(omsAttachBean2);
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(omsAttachmentQueueSDD, oattachmentrequest);

			}
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	private void validateOmsRequestSDD(MultipartFile file, Integer orderToLeId, Integer quoteToLeId, String attachmentType,
			Integer referenceId, String referenceName, Integer customerLeId) throws TclCommonException {

		if (Objects.isNull(orderToLeId) && Objects.isNull(quoteToLeId)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}

//		if (Objects.isNull(file)) {
//			throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
//		}

		if (Objects.isNull(referenceName)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (Objects.isNull(referenceId)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (Objects.isNull(attachmentType)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

//		if (Objects.isNull(customerLeId)) {
//			throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY,
//					ResponseResource.R_CODE_BAD_REQUEST);
//		}
	}
	
	
	/**
	 * getAttachments
	 *
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	public Resource getAttachmentsSDD(Integer attachmentId) throws TclCommonException {
		Resource resource = null;
		try {
			if (attachmentId == null || attachmentId == 0) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			Optional<Attachment> attachmentRepo = attachmentRepository.findById(attachmentId);
			if (attachmentRepo.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					String tempDownloadUrl = fileStorageService.getTempDownloadUrl(
							attachmentRepo.get().getDisplayName(), Long.parseLong(tempDownloadUrlExpiryWindow),
							attachmentRepo.get().getUriPathOrUrl(), false);
					resource = new ByteArrayResource(tempDownloadUrl.getBytes());
				} else {
					LOGGER.info("Path received :: {}", attachmentRepo.get().getUriPathOrUrl());
					File[] files = new File(attachmentRepo.get().getUriPathOrUrl()).listFiles();
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
						return resource;
					}
				}
			}
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing download malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			
		}
		return resource;
	}	
	
	public Map<String, String> getCustomerLeVerification(Integer customerLeId) {
		Map<String, String> customerLeVerification = new HashMap<>();
		customerLeVerification.put(IS_ACCOUNT_VERIFIED, VERIFIED);
		List<CustomerLeAttributeValue> customerLeAttributeValueList = customerLeAttributeValueRepository
				.findAccountVerificationByCustomerLeId(customerLeId, IS_ACCOUNT_VERIFIED);
		LOGGER.info("CustomerLeAttributes size {} ,customer Le id {} ", customerLeAttributeValueList.size(),
				customerLeId);
		for (CustomerLeAttributeValue customerLeAttributeValue : customerLeAttributeValueList) {
			if (customerLeAttributeValue.getAttributeValues().equalsIgnoreCase(YES)) {
				customerLeVerification.put(IS_ACCOUNT_VERIFIED, VERIFIED);
			} else {
				customerLeVerification.put(IS_ACCOUNT_VERIFIED, UNVERIFIED);
			}
		}
		return customerLeVerification;
	}

	/**
	 * getCustomerLeDetails - This method fetches all the legal entity details for
	 * the customer id array input
	 *
	 * @param String[] customerLeIds
	 * @return List<CustomerLeBean>
	 * @throws TclCommonException
	 */
	public List<CustomerLeBean> getCustomerLeDetailsByCustId(String customerId) {
		List<CustomerLeBean> customerEntitiesDtoList = new ArrayList<>();
		LOGGER.info("Entered into getCustomerLeDetailsByCustId " + customerId);
		List<CustomerLegalEntity> custLE = customerLegalEntityRepository.findByCustomerId(Integer.parseInt(customerId));
		if (!custLE.isEmpty()) {
			LOGGER.info("legal entity size" + custLE.size());
			for (CustomerLegalEntity legalEntity : custLE) {
				CustomerLeBean leBean = new CustomerLeBean();
				leBean.setLegalEntityId(legalEntity.getId());
				leBean.setLegalEntityName(legalEntity.getEntityName());
				leBean.setSfdcId(legalEntity.getTpsSfdcCuid());
				customerEntitiesDtoList.add(leBean);
			}
		}

		LOGGER.info("CustomerEntitiesDtoList :: {}", customerEntitiesDtoList);
		return customerEntitiesDtoList;
	}

	/**
	 * Get SECS Code By Customer Le Id
	 *
	 * @param customeLeId
	 * @return
	 */
	public Set<String> getCustomerLeSecsCode(Integer customeLeId) {
		return legalEntitySapCodeRepository.findLesSecsCode(customeLeId).stream()
				.map(LegalEntitySapCode::getCodeValue).collect(Collectors.toSet());
	}
	
	/*	
	 * Upload TSA and Service Addendum documents by secs id for wholesale customer
	 *
	 * @param file
	 * @param customerLeId
	 * @param secsId
	 * @param referenceName
	 * @param attachmentType
	 * @param productName
	 * @return {@link CustomerAttachmentBean}
	 * @throws TclCommonException
	 */
	@Transactional
	public CustomerAttachmentBean uploadWholesaleLegalEntityFile(MultipartFile file, Integer customerLeId, Integer secsId, String referenceName,
																 String attachmentType, String productName) throws TclCommonException {
		CustomerAttachmentBean customerAttachmentBean = new CustomerAttachmentBean();
		try {
			StoredObject storedObject = saveFileObjectStorage(file, customerLeId, secsId);

			String[] pathArray = storedObject.getPath().split(RIGHT_SLASH);
			Attachment attachment = saveAttachment(storedObject.getName(), pathArray[1]);

			saveCustomerLeSecsAttribute(attachmentType, attachment.getId(), customerLeId, secsId, productName);

			customerAttachmentBean = getCustomerAttachmentBean(attachment);
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return customerAttachmentBean;
	}

	private StoredObject saveFileObjectStorage(MultipartFile file, Integer customerLeId, Integer secsId) throws TclCommonException {
		StoredObject storedObject;
		try {
			InputStream inputStream = file.getInputStream();
			Integer customerId = getCustomerByCustomerLeId(customerLeId).getCustomerId();
			String customerCode = getCustomerCode(customerId);
			String customerLeCode = getCustomerLeCode(customerLeId);
			LOGGER.info("CustomerCode :: {} and CustomerLeCode :: {} and SecsId :: {}", customerCode, customerLeCode, secsId);
			storedObject = fileStorageService.uploadObjectWithSecsId(inputStream, customerCode, customerLeCode, String.valueOf(secsId));
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return storedObject;
	}

	private Attachment saveAttachment(String referenceName, String url) {
		Attachment attachment = new Attachment();
		attachment.setDisplayName(referenceName);
		attachment.setName(referenceName);
		attachment.setUriPathOrUrl(url);
		attachment.setCreatedTime(new Timestamp(new Date().getTime()));
		return attachmentRepository.save(attachment);
	}

	private CustomerLeSecsAttributeValue saveCustomerLeSecsAttribute(String attachmentType, Integer attachmentId, Integer customerLeId, Integer secsId, String productName) {
		MstLeAttribute mstLeAttribute = mstLeAttributeRepository.findByName(attachmentType).get();

		// We are tagging sap code table value instead of direct secs id
		LegalEntitySapCode secsSapCode = legalEntitySapCodeRepository.findIdBySecsCode(secsId);

		CustomerLeSecsAttributeValue customerLeSecsAttributeValue = customerLeSecsAttributeValueRepository
				.findByCustomerLeIdAndSecsIdAndMstLeAttributesIdAndProductName(customerLeId, secsSapCode.getId(), mstLeAttribute.getId(), productName);

		if (Objects.nonNull(customerLeSecsAttributeValue)) {
			customerLeSecsAttributeValue.setAttributeValues(String.valueOf(attachmentId));
		} else {
			customerLeSecsAttributeValue = new CustomerLeSecsAttributeValue();
			customerLeSecsAttributeValue.setAttributeValues(String.valueOf(attachmentId));
			customerLeSecsAttributeValue.setCustomerLegalEntity(customerLegalEntityRepository.findAllById(customerLeId));
			customerLeSecsAttributeValue.setLegalEntitySapCode(secsSapCode);
			customerLeSecsAttributeValue.setProductName(productName);
			customerLeSecsAttributeValue.setMstLeAttribute(mstLeAttribute);
		}
		customerLeSecsAttributeValueRepository.save(customerLeSecsAttributeValue);
		return customerLeSecsAttributeValue;
	}

	private CustomerAttachmentBean getCustomerAttachmentBean(Attachment attachment) {
		CustomerAttachmentBean customerAttachmentBean = new CustomerAttachmentBean();
		customerAttachmentBean.setId(attachment.getId());
		customerAttachmentBean.setDisplayName(attachment.getDisplayName());
		customerAttachmentBean.setName(attachment.getName());
		customerAttachmentBean.setUriPath(attachment.getUriPathOrUrl());
		return customerAttachmentBean;
	}

	private String getCustomerCode(Integer customerId) {
		Customer customer = customerRepository.findById(customerId).get();
		if (Objects.isNull(customer.getCustomerCode())) {
			customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
			customerRepository.save(customer);
		}
		return customer.getCustomerCode();
	}

	private String getCustomerLeCode(Integer customerLeId) {
		CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findAllById(customerLeId);
		if (Objects.isNull(customerLegalEntity.getCustomerLeCode())) {
			customerLegalEntity.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.getTpsSfdcCuid()));
			customerLegalEntityRepository.save(customerLegalEntity);
		}
		return customerLegalEntity.getCustomerLeCode();
	}

	/**
	 * This method takes customer legalId it provides the attachments where
	 * mstLegalAttributes has name as TSA and / or SERVICE_ADDENDUM
	 *
	 * @param customerLeId
	 * @param secsId
	 * @param productName
	 *
	 * @return {@link List<AttachmentBean>}
	 * @throws TclCommonException
	 */
	public List<AttachmentBean> getDocumentDetailsBySecsId(Integer customerLeId, Integer secsId, String productName)
			throws TclCommonException {
		List<AttachmentBean> attachments = new ArrayList<>();
		try {
			if (Objects.isNull(customerLeId) && Objects.isNull(secsId)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			// We have 2 documents for wholesale - TSA and Service Addendum
			String tsaDocumentId = getDocumentAttributeValueForSecs(DocumentConstant.TSA_DOCUMENT, customerLeId, secsId, productName);
			String serviceaddendumDocumentId = getDocumentAttributeValueForSecs(DocumentConstant.SERVICE_ADDENDUM_DOCUMENT, customerLeId, secsId, productName);
			if(Objects.nonNull(tsaDocumentId)) {
				Optional.of(attachmentRepository.findById(Integer.parseInt(tsaDocumentId))).ifPresent(attachment -> {
					attachments.add(new AttachmentBean(attachment.get(), DocumentConstant.TSA_DOCUMENT));
				});
			}
			if(Objects.nonNull(serviceaddendumDocumentId)) {
				Optional.of(attachmentRepository.findById(Integer.parseInt(serviceaddendumDocumentId))).ifPresent(attachment -> {
					attachments.add(new AttachmentBean(attachment.get(), DocumentConstant.SERVICE_ADDENDUM_DOCUMENT));
				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}





		return attachments;
	}

	private String getDocumentAttributeValueForSecs(String documentType, Integer customerLeId, Integer secsId, String productName ) {
		MstLeAttribute mstLeAttribute = mstLeAttributeRepository.findByName(documentType).get();

		// We are tagging le sap code table value instead of direct secs id
		LegalEntitySapCode secsSapCode = legalEntitySapCodeRepository.findIdBySecsCode(secsId);

		CustomerLeSecsAttributeValue customerLeSecsAttributeValue = customerLeSecsAttributeValueRepository
				.findByCustomerLeIdAndSecsIdAndMstLeAttributesIdAndProductName(customerLeId, secsSapCode.getId(), mstLeAttribute.getId(), productName);
		if(Objects.nonNull(customerLeSecsAttributeValue)) {
			return customerLeSecsAttributeValue.getAttributeValues();
		}
		return null;
	}
	/**
	 * Get currency and country details by le
	 *
	 * @param currencyId
	 */
	public String getCountryCurrencyByCustomerLe(String customerLeId) throws TclCommonException {
		CustomerLeCountryCurrencyBean customerLeCountryCurrencyBean = new CustomerLeCountryCurrencyBean();
		Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(Integer.valueOf(customerLeId));
		if(customerLegalEntity.isPresent()){
			Map<String, Object> countryCurrencyDetails = currencyMasterRepository.findCurrencyDetailsByLe(customerLegalEntity.get().getId());
			if(!CollectionUtils.isEmpty(countryCurrencyDetails)){
				customerLeCountryCurrencyBean.setCountryId((Integer) countryCurrencyDetails.get("countryId"));
				customerLeCountryCurrencyBean.setCountryName((String) countryCurrencyDetails.get("countryName"));
				customerLeCountryCurrencyBean.setCurrencyId((Integer) countryCurrencyDetails.get("currencyId"));
				customerLeCountryCurrencyBean.setCurrencyName((String) countryCurrencyDetails.get("currencyName"));
			}
		}
		return Utils.convertObjectToJson(customerLeCountryCurrencyBean);
	}

	/**
	 * Get Customer Le CUID
	 *
	 * @param customerLeId
	 * @return tpsSfdcCuid
	 */
	public String getCustomerLeByCuID(String tpsSfdcCuid) {
		CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findCustomerLeByCuid(tpsSfdcCuid);
		String customerleId = customerLegalEntity.getId().toString();
		LOGGER.info("Customer Le SFDC CUID :: {}", customerleId);
		return customerleId;
	}

	/**
	 * Get Support Matrix File
	 *
	 * @return InputStream
	 */
	public InputStream getSupportMatrixAttachment(Attachment attachmentRepo) throws TclCommonException {
		InputStream inputStream = null;
		try {
			if (Objects.nonNull(attachmentRepo)) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					inputStream = fileStorageService.downloadFileAsInputStream(attachmentRepo.getDisplayName(),
							Long.parseLong(tempDownloadUrlExpiryWindow), attachmentRepo.getUriPathOrUrl(), false);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return inputStream;
	}

	/**
	 * Get Attachment Details
	 *
	 * @return Optional<Attachment>
	 * @throws TclCommonException
	 */
	public Attachment getAttachmentDetailsForCustLe() throws TclCommonException {
		Attachment attachmentRepo = null;
		List<Integer> customerLeIds = new ArrayList<>();
		customerLeIds.addAll(new ArrayList<>(getCustomerLeIds()));
		if (customerLeIds.isEmpty() && Objects.isNull(customerLeIds))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		Optional<MstLeAttribute> mstleAttribute = mstLeAttributeRepository.findByName(PDFConstants.SERVICE_SEGMENT);
		if (mstleAttribute.isPresent()) {
			List<CustomerLeAttributeValue> customerLeAttVal = customerLeAttributeValueRepository
					.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLeIds.get(0),
							mstleAttribute.get().getId());
			if (!customerLeAttVal.isEmpty() && Objects.nonNull(customerLeAttVal)) {
				CustomerAttachment custAttach = customerAttatchmentsRepository
						.findByAttachmentName(customerLeAttVal.get(0).getAttributeValues());
				if (Objects.nonNull(custAttach)) {
					attachmentRepo = attachmentRepository.findById(custAttach.getAttachmentId()).get();
				}
			}
		}
		return attachmentRepo;
	}

	/**
	 * Get CustomerLeId for logged in customer
	 *
	 * @return Set<Integer>
	 */
	private Set<Integer> getCustomerLeIds() {
		Set<Integer> customerLeIds = new HashSet<>();
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
		for (CustomerDetail customerDetail : customerDetails) {
			customerLeIds.add(customerDetail.getCustomerLeId());
		}
		return customerLeIds;
	}
	
	
	/**
	 * upload customer attachments
	 * @param file
	 * @param attachmentName
	 * @throws TclCommonException
	 */
	public AttachmentBean uploadAttachmentDocument(MultipartFile file , String attachmentName) throws TclCommonException {
		AttachmentBean attachmentBean= new AttachmentBean();
		try {
			LOGGER.info("Entering uploadAttachmentDocument to upload attachement {} ",attachmentName);
			if(attachmentName == null || file == null || ! Arrays.asList(CommonConstants.SUPPORT_MATRIX_NAMES).contains(attachmentName.toUpperCase()))
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR); 
			 CustomerAttachment customerAttachment = customerAttatchmentsRepository.findByAttachmentName(attachmentName);
			 Attachment attachment = null;
			 if(customerAttachment != null) {
				 Optional<Attachment> attachements = attachmentRepository.findById(customerAttachment.getAttachmentId());
				 attachment = attachements.get();
			 }
				 StoredObject 	storedObject = fileStorageService.uploadAttachements(attachmentName.toUpperCase(), file.getInputStream());
				 LOGGER.info("Uploaded attachement  {} ",storedObject);
				 if(storedObject != null) {
					 if(attachment == null) {
						 attachment = new Attachment(); 
						 String[] pathArray = storedObject.getPath().split("/");
						 attachment.setUriPathOrUrl(pathArray[1]);
						 attachment.setName(storedObject.getName());
						 attachment.setDisplayName(storedObject.getName());
						 attachment.setCreatedTime(new Timestamp(new Date().getTime()));
						 attachment = attachmentRepository.save(attachment);
					 } else {
						 String[] pathArray = storedObject.getPath().split("/");
						 attachment.setUriPathOrUrl(pathArray[1]);
						 attachment.setName(storedObject.getName());
						 attachment.setDisplayName(storedObject.getName());
						 attachment.setCreatedTime(new Timestamp(new Date().getTime()));
						 attachment = attachmentRepository.save(attachment);
					 }
					 if(attachment != null) {
						 if(customerAttachment == null) {
							 CustomerAttachment custAttachment = new CustomerAttachment(); 
							 custAttachment.setAttachmentId(attachment.getId());
							 custAttachment.setAttachmentName(attachmentName.toUpperCase());
							 custAttachment.setCreatedBy(Utils.getSource());
							 custAttachment.setCreatedTime(new Timestamp(new Date().getTime()));
							 custAttachment.setStatus(CommonConstants.BACTIVE);
							 customerAttatchmentsRepository.save(custAttachment);
						 } else {
							 customerAttachment.setAttachmentId(attachment.getId());
							 customerAttachment.setAttachmentName(attachmentName.toUpperCase());
//							 customerAttachment.setCreatedBy(Utils.getSource());
							 customerAttachment.setCreatedTime(new Timestamp(new Date().getTime()));
							 customerAttachment.setStatus(CommonConstants.BACTIVE);
							 customerAttatchmentsRepository.save(customerAttachment);
						 }
						 attachmentBean.setId(attachment.getId());
						 attachmentBean.setDisplayName(attachment.getDisplayName());
						 attachmentBean.setUriPath(attachment.getUriPathOrUrl());
						 attachmentBean.setName(attachment.getName());
					 } else
						 throw new TclCommonException(ExceptionConstants.UPLOAD_SUPPORT_MATRIX_ERROR, ResponseResource.R_CODE_ERROR);
					 
					
				 } 
			 
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR); 
		}
		return  attachmentBean;


	}

	/**
	 * Function to identify supplier legal entity for given values
	 *
	 * @param customerLegalEntityId
	 * @param productName
	 * @param accessType
	 * @param secsId
	 * @return {@link CustomerLegalEntityProductResponseDto}
	 */
	public Set<CustomerLegalEntityProductResponseDto> getSupplierLegalEntityDetailsByCustomerLegalIdAndSecsdId(
			Integer customerLegalEntityId, String productName, Integer secsId) throws TclCommonException {
		Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = new HashSet<>();
		boolean isDomesticVoice = false;
		if (!ServiceSpecificConstant.GSIP.equals(productName)) {
			throw new TCLException("Product Name should be GSIP");
		}
		try {
			List<String> supplierNames = customerLegalEntityCompanyCodeRepository.findSupplierNameByLeIdAndSecsId(customerLegalEntityId, secsId);
			if (!CollectionUtils.isEmpty(supplierNames)) {
				List<ServiceProviderLegalEntity> supplierLegalEntities = supplierNames.stream().map(supplierName -> {
					return serviceProviderLegalEntityRepository.findByEntityName(supplierName);
				}).collect(Collectors.toList());
				customerLegalEntityProductResponseDtos = getSupplierDetailsBySecsId(supplierLegalEntities, customerLegalEntityProductResponseDtos, isDomesticVoice);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new HashSet<CustomerLegalEntityProductResponseDto>(customerLegalEntityProductResponseDtos);
	}

	private Set<CustomerLegalEntityProductResponseDto> getSupplierDetailsBySecsId(List<ServiceProviderLegalEntity> supplierLegalEntities, Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos, Boolean isDomesticVoice) {
		supplierLegalEntities.stream().forEach(serviceProviderLegalEntity -> {
			CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();

			String entityName = serviceProviderLegalEntity.getEntityName();
			Integer entityId = serviceProviderLegalEntity.getId();
			List<String> currency = new ArrayList<>();

			customerLegalEntityProductResponseDto.setSple(entityName);
			customerLegalEntityProductResponseDto.setServiceProviderId(entityId);

			if(isDomesticVoice) {
				Integer isDefault = 1;
				currency = currencyMasterRepository.findShortNameByIdAndIsDefault(entityId,isDefault);
				LOGGER.info("Fetch default currency list for Domestic voice. Size of list is ---> {} ", currency.size());
				if (!currency.isEmpty()) {
					customerLegalEntityProductResponseDto.setCurrency(currency.get(0));
				}
			} else {
				currency = currencyMasterRepository.findShortNameById(entityId);
				LOGGER.info("Fetch default currency list for products other than Domestic voice. Size of list is ---> {} ", currency.size());
				customerLegalEntityProductResponseDto.setCurrency(currency.stream().collect(Collectors.joining(",")));
			}

			customerLegalEntityProductResponseDto.setLouRequired(true);

			customerLegalEntityProductResponseDtos.add(customerLegalEntityProductResponseDto);
		});
		return customerLegalEntityProductResponseDtos;
	}

	/**
	 * Method to generate the temporary download url for documents uploaded to the
	 * storage container
	 *
	 * @param customerLeId
	 * @param secsId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	public String getTempDownloadUrlForSecsId(Integer customerLeId, Integer secsId, Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (attachmentId == null || attachmentId == 0) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				if (validateAttachmentIdForSecsId(customerLeId, secsId, attachmentId)) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
				if (attachment.isPresent()) {
					String requestId = attachment.get().getName();

					tempDownloadUrl = fileStorageService.getTempDownloadUrl(requestId,
							Long.parseLong(tempDownloadUrlExpiryWindow), attachment.get().getUriPathOrUrl(), false);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return tempDownloadUrl;
	}

	private boolean validateAttachmentIdForSecsId(Integer customerLeId, Integer secsId, Integer attachmentId) {
		// We are tagging le sap code table value instead of direct secs id
		LegalEntitySapCode secsSapCode = legalEntitySapCodeRepository.findIdBySecsCode(secsId);

		List<CustomerLeSecsAttributeValue> customerLeAttributes = customerLeSecsAttributeValueRepository
				.findByCustomerLeIdAndSecsIdAndAttachmentId(customerLeId, secsSapCode.getId(), String.valueOf(attachmentId));
		return (customerLeAttributes == null || customerLeAttributes.isEmpty());
	}

	public List<LeOwnerDetailsSfdc> getLeOwnerDetails(Integer customerId, String cusLeId) throws TclCommonException {
		List<LeOwnerDetailsSfdc> detailsSfdcList = new ArrayList<>();
		List<CustomerTeamMembers> customerTeamMembers = customerTeamMembersRepository.findByCustomerIdAndTeamRoleAndIsTeamMember(customerId, "Account Manager", "Y");
		if(!CollectionUtils.isEmpty(customerTeamMembers)){

			customerTeamMembers.forEach(member->{
				LeOwnerDetailsSfdc leOwnerDetailsSfdc = new LeOwnerDetailsSfdc();
				leOwnerDetailsSfdc.setEmail(member.getEmail());
				leOwnerDetailsSfdc.setIsTeamMember(member.getIsTeamMember());
				leOwnerDetailsSfdc.setMobile(member.getMobile());
				leOwnerDetailsSfdc.setOwnerName(member.getName());
				leOwnerDetailsSfdc.setRegion(member.getRegion());
				detailsSfdcList.add(leOwnerDetailsSfdc);
			});
		}
		//For Existing Owner Details

		CustomerLeAccountManagerDetails customerLeAccountManagerDetails = getCustomerLeAccountManagerDetails(Integer.valueOf(cusLeId));
		LeOwnerDetailsSfdc leOwnerDetailsSfdc = new LeOwnerDetailsSfdc();
			leOwnerDetailsSfdc.setEmail(customerLeAccountManagerDetails.getAccountManagerEmailId());
			leOwnerDetailsSfdc.setOwnerName(customerLeAccountManagerDetails.getAccountManagerName());
			leOwnerDetailsSfdc.setMobile(customerLeAccountManagerDetails.getAccountManagerMob());
			leOwnerDetailsSfdc.setRegion("");
			leOwnerDetailsSfdc.setTeamRole("");
			leOwnerDetailsSfdc.setIsTeamMember("");
			leOwnerDetailsSfdc.setTeamRole("Le Owner");
			detailsSfdcList.add(leOwnerDetailsSfdc);
			LOGGER.info("Fetch Owner flag yes and bean set. Size of bean is ---> {} ", detailsSfdcList.size());

		LOGGER.info("List size for customer id ---> {} is ---> {} and info is ---> {} ", customerId,detailsSfdcList.size(),Optional.ofNullable(detailsSfdcList));
		return detailsSfdcList;
	}
	public ServiceResponse processUploadAnyDocument(MultipartFile file, Integer orderToLeId, Integer quoteToLeId,
			String attachmentType, List<Integer> referenceId, String referenceName, Integer customerLeId,
			String productName) throws TclCommonException {
		ServiceResponse fileUploadResponse = new ServiceResponse();
		TempUploadUrlInfo tempUploadUrlInfo = null;

		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
						.findById(customerLeId);
				if (customerLegalEntity.isPresent()) {
					Customer customer = customerLegalEntity.get().getCustomer();
					if (customer.getCustomerCode() == null) {
						customer.setCustomerCode(Utils.getMD5Code(customer.getTpsSfdcAccountId()));
						customerRepository.save(customer);
					}
					if (customerLegalEntity.get().getCustomerLeCode() == null) {
						customerLegalEntity.get()
								.setCustomerLeCode(Utils.getMD5Code(customerLegalEntity.get().getTpsSfdcCuid()));
						customerLegalEntityRepository.save(customerLegalEntity.get());
					}
					String updatedFileName = removeReq(file.getOriginalFilename());
					tempUploadUrlInfo = fileStorageService.getTempUploadUrlWithFileName(Long.parseLong(tempUploadUrlExpiryWindow),
							customer.getCustomerCode(), customerLegalEntity.get().getCustomerLeCode(), false, updatedFileName);
					fileUploadResponse.setUrlPath(tempUploadUrlInfo.getTemporaryUploadUrl());
					fileUploadResponse.setFileName(tempUploadUrlInfo.getRequestId());
				}
			} else {
				validateOmsRequest(file, orderToLeId, quoteToLeId, attachmentType, referenceId, referenceName,
						customerLeId);
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
				com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
				attachmentBean.setPath(newFolder);
				attachmentBean.setFileName(file.getOriginalFilename());
				Integer attachmentId = attachmentService.processAttachment(attachmentBean);
				fileUploadResponse.setAttachmentId(attachmentId);

				Optional<MstLeAttribute> mstAttributeOpt = mstLeAttributeRepository.findByName("MSA");
				if (mstAttributeOpt.isPresent()) {
					List<CustomerLeAttributeValue> cusLeAttributesList = customerLeAttributeValueRepository
							.findByCustomerLeIdAndAttachmentId(customerLeId, String.valueOf(attachmentId));
					CustomerLeAttributeValue cusLeAttribute;
					if (cusLeAttributesList.isEmpty()) {
						cusLeAttribute = new CustomerLeAttributeValue();
						cusLeAttribute.setAttributeValues(String.valueOf(attachmentId));
						cusLeAttribute.setMstLeAttribute(mstAttributeOpt.get());
						cusLeAttribute.setCustomerLegalEntity(customerLegalEntityRepository.findAllById(customerLeId));
						cusLeAttribute.setProductName(productName);
						customerLeAttributeValueRepository.save(cusLeAttribute);
					}

				}
				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				referenceId.forEach(refId -> {
					OmsAttachBean omsAttachBean = new OmsAttachBean();
					omsAttachBean.setAttachmentId(attachmentId);
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
					omsAttachBean.setOrderLeId(orderToLeId);
					omsAttachBean.setQouteLeId(quoteToLeId);
					omsAttachBean.setReferenceId(refId);
					omsAttachBean.setReferenceName(referenceName);
					omsAttachBean.setFileName(file.getOriginalFilename());
					omsAttachBeanList.add(omsAttachBean);
				});

				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(omsAttachmentQueue, oattachmentrequest);

			}

			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}

	private  String removeReq(String c) {

		try {

			Pattern pt = Pattern.compile("[^a-zA-Z0-9\\.]");
			Matcher match = pt.matcher(c);
			while (match.find()) {
				String s = match.group();
				c = c.replace(s, "");
			}
			return c;
		} catch (Exception e) {
			return c.replaceAll(" ", "");
		}
	}


	public List<LeOwnerDetailsSfdc> getCustomeTeamMemberDetails(Integer customerId) throws TclCommonException {
		List<LeOwnerDetailsSfdc> detailsSfdcList = new ArrayList<>();
		try {
		List<CustomerTeamMembers> customerTeamMembers = customerTeamMembersRepository.findByCustomerIdAndTeamRole(customerId, "Customer Success Manager");
		if(!CollectionUtils.isEmpty(customerTeamMembers)){

            customerTeamMembers.forEach(member->{
                LeOwnerDetailsSfdc leOwnerDetailsSfdc = new LeOwnerDetailsSfdc();
                leOwnerDetailsSfdc.setEmail(member.getEmail());
                leOwnerDetailsSfdc.setTeamRole(member.getTeamRole());
                leOwnerDetailsSfdc.setIsTeamMember(member.getIsTeamMember());
                leOwnerDetailsSfdc.setMobile(member.getMobile());
                leOwnerDetailsSfdc.setOwnerName(member.getName());
                leOwnerDetailsSfdc.setRegion(member.getRegion());
                detailsSfdcList.add(leOwnerDetailsSfdc);
            });
        }
        }catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.CUSTOMER_LEGAL_ENTITY_EMPTY, e,
                    ResponseResource.R_CODE_BAD_REQUEST);
        }

        return detailsSfdcList;

    }

    public String getCustomerCodeForSfdc(Approver customerBean) throws TclCommonException {
        LOGGER.info("inside method getCustomerCodeForSfdc with data {}", customerBean.toString());
        String sfdcCode = "";
        try{
            if(Objects.nonNull(customerBean)){
                if("CSM".equalsIgnoreCase(customerBean.getName())){
                    LOGGER.info("csm loooop");
                    List<CustomerTeamMembers> customerTeamMembers = customerTeamMembersRepository.findByEmail(customerBean.getEmail());
                    if(!customerTeamMembers.isEmpty())
                        sfdcCode = customerTeamMembers.stream().findFirst().get().getTmSfdcCode();

                }else if ("CR".equalsIgnoreCase(customerBean.getName())){
                    LOGGER.info("communication recipient loooop");
                    List<CustomerLeContact> customerContactsByEmail = customerLeContactRepository.findByEmailId(customerBean.getEmail());
                    if (!customerContactsByEmail.isEmpty())
                        sfdcCode = customerContactsByEmail.stream().sorted(Comparator.comparing(CustomerLeContact::getCreatedTime).reversed()).findFirst().get().getContactId();
                }
            }
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_CUSTOMER_ERROR, e,
                    ResponseResource.R_CODE_BAD_REQUEST);
        }
        LOGGER.info("sfdc code set {}", sfdcCode);
        return sfdcCode;
    }

    public CustomerContactDetails getCustomerContactByCommunicationRecipient(String customerEmail) throws TclCommonException {
        LOGGER.info("inside method getCustomerContactByCommunicationRecipient with data {}", customerEmail);
        try {
        	CustomerContactDetails contactDetailsBean = new CustomerContactDetails();
            List<CustomerLeContact> customerContactsByEmail = customerLeContactRepository.findByEmailId(customerEmail);
            LOGGER.info("Customer contact : {}", customerContactsByEmail.toString());
            constructCustomerContact(contactDetailsBean, customerContactsByEmail);
            LOGGER.info("Contact Bean {}",contactDetailsBean);
            return contactDetailsBean;
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_CUSTOMER_ERROR, e,
                    ResponseResource.R_CODE_BAD_REQUEST);
        }
    }
	/**
	 * Get supplier legal entity details by partner le and secs id
	 *
	 * @param partnerLegalEntityId
	 * @param productName
	 * @param secsId
	 * @return
	 * @throws TclCommonException
	 */
	public Set<CustomerLegalEntityProductResponseDto> getSupplierLegalEntityDetailsByPartnerLegalIdForService(Integer partnerLegalEntityId, String productName, Integer secsId, List<String> services) throws TclCommonException {
		Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = new HashSet<>();
		boolean isDomesticVoice = false;
		if(services.contains("Domestic Voice")) {
			isDomesticVoice = true;
		}
		if (!ServiceSpecificConstant.GSIP.equals(productName)) {
			throw new TCLException("Product Name should be GSIP");
		}
		try {
			List<String> supplierNames = partnerLegalEntityCompanyCodeRepository.findSupplierNameByLeIdAndSecsId(partnerLegalEntityId, secsId);
			if (!CollectionUtils.isEmpty(supplierNames)) {
				List<ServiceProviderLegalEntity> supplierLegalEntities = supplierNames.stream().map(supplierName -> {
					ServiceProviderLegalEntity byEntityName = serviceProviderLegalEntityRepository.findByEntityName(supplierName);
					if(Objects.nonNull(byEntityName)){
						return byEntityName;
					}
					else {
						return null;
					}
				}).filter(Objects::nonNull).collect(Collectors.toList());
				customerLegalEntityProductResponseDtos = getSupplierDetailsBySecsId(supplierLegalEntities, customerLegalEntityProductResponseDtos, isDomesticVoice);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new HashSet<CustomerLegalEntityProductResponseDto>(customerLegalEntityProductResponseDtos);
	}

	@Transactional
	public List<CustomerLeAttributeBean> saveOrUpdateCustomerAttributes(Integer customerId,
																		  List<CustomerLeAttributeBean> attributes) {

		Customer customer = customerRepository.findById(customerId).get();

		if (!Objects.isNull(customer)) {
			for (CustomerLeAttributeBean attribute : attributes) {
				Optional.ofNullable(mstCustomerSpAttributeRepository.findByNameAndStatus(attribute.getAttributeName(),CommonConstants.BACTIVE))
						.ifPresent(mstSpAttribute -> {
							customerAttributeValueRepository
									.findByCustomerIdAndMstCustomerSpAttribute(customerId,
											mstSpAttribute)
									.stream().findFirst()
									.map(customerLeAttributeValue -> updateCustomerAttributes(
											customerLeAttributeValue, attribute))
									.orElseGet(() -> createAndSaveCustomerAttribute(attribute, mstSpAttribute,
											customer));
						});
			}
		}
		return attributes;
	}


	private CustomerLeAttributeBean updateCustomerAttributes(CustomerAttributeValue customerAttributeValue,
															   CustomerLeAttributeBean attributeBean) {
		customerAttributeValue.setAttributeValues(attributeBean.getAttributeValue());
		customerAttributeValueRepository.save(customerAttributeValue);
		return attributeBean;
	}

	private CustomerLeAttributeBean createAndSaveCustomerAttribute(CustomerLeAttributeBean attributeBean,
																   MstCustomerSpAttribute mstCustomerSpAttribute, Customer customer) {
		CustomerAttributeValue customerAttributeValue = new CustomerAttributeValue();
		customerAttributeValue.setCustomer(customer);
		customerAttributeValue.setMstCustomerSpAttribute(mstCustomerSpAttribute);
		customerAttributeValue.setAttributeValues(attributeBean.getAttributeValue());
		customerAttributeValueRepository.save(customerAttributeValue);
		return attributeBean;
	}
	
	
	public ServiceProviderLegalBean getSpleCountry(Integer id) throws TclCommonException {
		ServiceProviderLegalBean serviceProviderLegalEntityDto = null;
		String country = null;
		try {
			Optional<ServiceProviderLegalEntity> serviceProvider = serviceProviderLegalEntityRepository.findById(id);
			if (serviceProvider.isPresent()) {
				List<SpLeCountry> spleCountries = new ArrayList<>(serviceProvider.get().getSpLeCountries());
				if (spleCountries != null && !spleCountries.isEmpty()) {
					SpLeCountry spLeCountry = spleCountries.get(0);
					country = spLeCountry.getMstCountry().getName();
					LOGGER.info("getMstCountry splecountry{}", country);
				}
				serviceProviderLegalEntityDto = new ServiceProviderLegalBean();
				serviceProviderLegalEntityDto.setEntityName(serviceProvider.get().getEntityName());
				serviceProviderLegalEntityDto.setCountry(country);
			}
		} catch (Exception e) {
			throw new TclCommonException("No Service provider");
		}
		return serviceProviderLegalEntityDto;
	}


	/**
	 *
	 * This method return owner name based on the business segment for a customer.
	 * @param customerId
	 * @return
	 */
	public CustomerContactDetails getOwnerName(Integer custId, Integer customerLeId) throws TclCommonException {
		CustomerContactDetails customerContactDetails = new CustomerContactDetails();
		try {
			if(custId == null || customerLeId == null)
				throw new TclCommonException(ExceptionConstants.CUSTOMER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
			Optional<Customer> cust = customerRepository.findById(custId);
			MstCustomerSpAttribute attribute = mstCustomerSpAttributeRepository
					.findByNameAndStatus(SpConstants.ACCOUNT_OWNER_EMAILID, (byte) 1);
			MstCustomerSpAttribute attributeAccountOwnerName = mstCustomerSpAttributeRepository
					.findByNameAndStatus(SpConstants.ACCOUNT_OWNER, (byte) 1);
			List<CustomerAttributeValue> customerAccountManagerEmail = customerAttributeValueRepository
					.findByCustomerAndMstCustomerSpAttribute(cust.get(), attribute);
			List<CustomerAttributeValue> customerAccountManagerName = customerAttributeValueRepository
					.findByCustomerAndMstCustomerSpAttribute(cust.get(), attributeAccountOwnerName);
			MstCustomerSpAttribute attributeBusinessSegment = mstCustomerSpAttributeRepository
					.findByNameAndStatus(SpConstants.BUSINESS_SEGMENT, (byte) 1);
			List<CustomerAttributeValue> customerBusinessSegment = customerAttributeValueRepository
					.findByCustomerAndMstCustomerSpAttribute(cust.get(), attributeBusinessSegment);
			if(!customerBusinessSegment.isEmpty()) {
				LOGGER.info("attribute business segment {} for customer id {}", customerBusinessSegment.get(0).getAttributeValues(), cust.get().getId());
				if(customerBusinessSegment.get(0).getAttributeValues() != null) {
					if(PartnerCustomerConstants.SEGMENT_1.equalsIgnoreCase(customerBusinessSegment.get(0).getAttributeValues())
							|| PartnerCustomerConstants.SEGMENT_3.equalsIgnoreCase(customerBusinessSegment.get(0).getAttributeValues())) {
						customerContactDetails.setEmailId(customerAccountManagerEmail.get(0).getAttributeValues());
						customerContactDetails.setName(customerAccountManagerName.get(0).getAttributeValues());
					} else if(PartnerCustomerConstants.SEGMENT_2.equalsIgnoreCase(customerBusinessSegment.get(0).getAttributeValues())) {
						Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository.findById(customerLeId);
						if(customerLegalEntity.isPresent()) {
							Optional<MstLeAttribute> spLeOwnerEmail = mstLeAttributeRepository.findByName(PartnerCustomerConstants.SUPPLIER_LE_EMAIL);
							Optional<MstLeAttribute> spLeOwnerName = mstLeAttributeRepository.findByName(PartnerCustomerConstants.MST_ATTRIBUTE_SUPPLIER_LE_OWNER);
							List<CustomerLeAttributeValue> customerLeAttributeValueList = customerLeAttributeValueRepository.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLegalEntity.get().getId(), spLeOwnerEmail.get().getId());
							List<CustomerLeAttributeValue> customerLeAttributeOwnerName = customerLeAttributeValueRepository.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(customerLegalEntity.get().getId(), spLeOwnerName.get().getId());
							if(!customerLeAttributeValueList.isEmpty()) {
								LOGGER.info("Le owner email id for customerle id {} is {}", customerLeAttributeValueList.get(0).getAttributeValues(), customerLegalEntity.get().getId());
								customerContactDetails.setEmailId(customerLeAttributeValueList.get(0).getAttributeValues());
								customerContactDetails.setName(customerLeAttributeOwnerName.get(0).getAttributeValues());
							}
						}
					}
				} else {
					customerContactDetails.setEmailId(customerAccountManagerEmail.get(0).getAttributeValues());
					customerContactDetails.setName(customerAccountManagerName.get(0).getAttributeValues());
				}
			} else {
				customerContactDetails.setEmailId(customerAccountManagerEmail.get(0).getAttributeValues());
				customerContactDetails.setName(customerAccountManagerName.get(0).getAttributeValues());
			}


		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("email and name being returned {}", Utils.convertObjectToJson(customerContactDetails));
		return customerContactDetails;
	}

	/**
	 * Method to retrieve country details of the supplier
	 *
	 * @param supplierLeId
	 * @return ServiceProviderCountryBean
	 * @throws TclCommonException
	 */
	public ServiceProviderCountryBean getCountryDetailsBySPLegalId(Integer spLegelId) throws TclCommonException {
		Integer countryId;
		Optional<ServiceProviderLegalEntity> serviceProviderLegalEntity = serviceProviderLegalEntityRepository
				.findById(spLegelId);
		if (!serviceProviderLegalEntity.isPresent()) {
			throw new TclCommonException(ExceptionConstants.SP_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		ServiceProviderCountryBean serviceProviderCountryBean = new ServiceProviderCountryBean();
		serviceProviderCountryBean.setServiceProviderName(serviceProviderLegalEntity.get().getEntityName());

		List<SpLeCountry> spleCountryList = spleCountryRepository
				.findByServiceProviderLegalEntity(serviceProviderLegalEntity.get());

		if (spleCountryList == null || spleCountryList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.SP_LEGAL_ENTITY_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		} else {
			countryId = spleCountryList.get(0).getMstCountry().getId();
		}

		String country = mstCountryRepository.findById(countryId).get().getName();
		serviceProviderCountryBean.setServiceProviderCountry(country);

		COUNTRY_MAP.forEach((k,v) -> {
			if(k.equalsIgnoreCase(country))
				serviceProviderCountryBean.setServiceProviderCountryGroup(v);
		});
		return serviceProviderCountryBean;
	}


	/**
	 * Method to fetch all the supplier le details.
	 * @param productName
	 * @return
	 */
	public Map<String, List<String>> getSupplierLEDetails(String productName){
		LOGGER.info("Product name received :: {}",productName);
		Map<String, List<String>> spleMapper =  new HashMap<>();
		List<SpLeCountry> spLeCountries = spleCountryRepository.getSupplierByProductAndCountryNotNull(productName);
		if(Objects.nonNull(spLeCountries) && !spLeCountries.isEmpty()){
			List<String> supplierLegalEntities = new ArrayList<>();
			spLeCountries.forEach(spLeCountry -> {
				LOGGER.info("SpleCountry :: {}",spLeCountry);
				String country  = spLeCountry.getMstCountry().getName().toLowerCase();
				if(spleMapper.containsKey(country)){
					spleMapper.get(country).add(spLeCountry.getServiceProviderLegalEntity().getEntityName());
				}else{
					List<String> entities = new ArrayList<>();
					entities.add(spLeCountry.getServiceProviderLegalEntity().getEntityName());
					spleMapper.put(country,entities);
				}
			});
		}
		return spleMapper;
	}
	
	/**
	 * 
	 * @param customerAddressBean
	 * @return
	 * @throws TclCommonException
	 */
	public SiteLevelAddressBean getBillingAndGstAddress(SiteLevelAddressBean siteLevelAddressBean) throws TclCommonException {
		LOGGER.info("Inside getBillingAndGstAddress siteLevelAddressBean{} ", siteLevelAddressBean.toString());
		try {
			Optional<CustomerLegalEntity> optionalCustomerLegalEntity = customerLegalEntityRepository
					.findById(siteLevelAddressBean.getLegalId());
			if (optionalCustomerLegalEntity.isPresent()) {
				CustomerLegalEntity customerLegalEntity = optionalCustomerLegalEntity.get();
				LOGGER.info("Customer lgeal entity id is {} ", customerLegalEntity.getId());

				for(GstAddressInfo gstAddressInfo : siteLevelAddressBean.getGstAddressInfo()) {
					if(gstAddressInfo.getGstNo().equalsIgnoreCase("No Registered GST")) {
						gstAddressInfo.setGstAddress("");
					} else {
						LeStateGst leStateGst = leStateGstRepository.findByGstNoAndCustomerLegalEntityAndStateAndIsActive(gstAddressInfo.getGstNo(),
								customerLegalEntity, gstAddressInfo.getState(), "Yes" );
						if(Objects.nonNull(leStateGst))
						gstAddressInfo.setGstAddress(leStateGst.getAddress());
					}
				}

				for(BillingAddressInfo	billingAddressInfo : siteLevelAddressBean.getBillingAddressInfo())	{
					Optional<CustomerLeBillingInfo> customerLeBillingInfo = customerLeBillingInfoRepository
							.findById(billingAddressInfo.getBillingInfoId());
					if(customerLeBillingInfo.isPresent()) 
						billingAddressInfo.setBillingAddress(customerLeBillingInfo.get().getBillAddr());
				}
				LOGGER.info("response siteLevelAddressBean{} ", siteLevelAddressBean.toString());
			} 
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return siteLevelAddressBean;
	}
	
	/**
	 * getAllLeGst
	 *
	 * @param customerLeId
	 * @return
	 */
	public List<LeGstDetailsBean> getAllLeGstInfo(Integer customerLeId) throws TclCommonException {
		LOGGER.info("Enter into getAllLeGst customerLeId:::" + customerLeId);
		List<LeGstDetailsBean> leGstDetailsBean = new ArrayList<>();
		List<LeStateGst> leStateGsts = null;

		try {
			Optional<CustomerLegalEntity> optionalCustomerLegalEntity = customerLegalEntityRepository
					.findById(customerLeId);
			if (optionalCustomerLegalEntity.isPresent()) {
				CustomerLegalEntity customerLegalEntity = optionalCustomerLegalEntity.get();
				leStateGsts = leStateGstRepository.findByCustomerLegalEntityAndIsActive(customerLegalEntity,"Yes");
				if (!leStateGsts.isEmpty()) {
					constructLeGstInfo(leGstDetailsBean, leStateGsts);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("GST LIST SIZE"+leGstDetailsBean.size());
		return leGstDetailsBean;
	}
	
	/**
	 * constructLeGstInfo
	 *
	 * @param leStateGstBeans
	 * @param leStateGsts
	 */
	private void constructLeGstInfo(List<LeGstDetailsBean> leGstDetailsBean, List<LeStateGst> leStateGsts) {
		LOGGER.info("Enter into  constructLeGstInfo"+leStateGsts.size());
		if (leStateGsts != null && !leStateGsts.isEmpty()) {
			leStateGsts.forEach(leGst -> {
				LeGstDetailsBean bean = new LeGstDetailsBean();
				bean.setId(leGst.getId());
				bean.setState(leGst.getState());
				bean.setGstNo(leGst.getGstNo());
				bean.setAddress(leGst.getAddress());
				
				leGstDetailsBean.add(bean);

			});
		}

	}


	public LeCcaRequestBean getLeCCa(LeCcaRequestBean leCcaRequestBean) throws TclCommonException {

		LOGGER.info("Inside getLeCCa leCcaRequestBean{} ", leCcaRequestBean.toString());
		try {
			List<LeCcaAddress> leCcaAddress = leCcaAddressRepository.findByMdmAddressId(leCcaRequestBean.getCcaId());
			if (leCcaAddress != null && !(leCcaAddress.isEmpty())) {
				if (StringUtils.isNotBlank(leCcaAddress.stream().findFirst().get().getLocationId().toString())) {
					leCcaRequestBean.setCcaLocationId(leCcaAddress.stream().findFirst().get().getLocationId().toString());
					LOGGER.info("Location id in Le cca: {}",leCcaRequestBean.getCcaLocationId());
				}
				else {
					LOGGER.info("Location id is empty");
				}
			}
			else {
				throw new TclCommonException(ExceptionConstants.LE_ADDRESS_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return leCcaRequestBean;
	}

	/**
	 * Get GST Number by ID
	 *
	 * @param gstAttributeValue
	 * @return
	 */
	public String getGSTNumberByAttributeValue(String gstAttributeValue) {
		return leStateGstRepository.findById(Integer.parseInt(gstAttributeValue)).get().getGstNo();
	}
}
