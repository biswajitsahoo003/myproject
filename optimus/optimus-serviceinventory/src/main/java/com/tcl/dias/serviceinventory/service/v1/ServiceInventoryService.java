package com.tcl.dias.serviceinventory.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.CustomerLegalEntityDetails;
import com.tcl.dias.common.beans.GscServiceDetailBean;
import com.tcl.dias.common.beans.MacdFlagServiceRequest;
import com.tcl.dias.common.beans.MasterVRFBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SIComponentAttributeBean;
import com.tcl.dias.common.beans.SIComponentBean;
import com.tcl.dias.common.beans.SIContractInfoBean;
import com.tcl.dias.common.beans.ServiceDetailBean;
import com.tcl.dias.common.beans.ServiceDetailedInfoBean;
import com.tcl.dias.common.beans.SiServiceDetailBean;
import com.tcl.dias.common.beans.SlaveVRFBean;
import com.tcl.dias.common.beans.SuipListBean;
import com.tcl.dias.common.beans.UserProductsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscWholesaleInterconnectBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderAttributeBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.servicefulfillment.beans.ScServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.RfDumpWirelessOneBean;
import com.tcl.dias.common.serviceinventory.beans.OrderSummaryBeanResponse;
import com.tcl.dias.common.serviceinventory.beans.SIAssetComponentBean;
import com.tcl.dias.common.serviceinventory.beans.SIAssetRelationBean;
import com.tcl.dias.common.serviceinventory.beans.SICustomerInfoBean;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIPriceRevisionDetailBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAssetComponentBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoGVPNBean;
import com.tcl.dias.common.serviceinventory.beans.SiServiceSiContractInfoBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.SIExistingGVPNBean;
import com.tcl.dias.common.webex.beans.SIInfoSearchBean;
import com.tcl.dias.serviceinventory.beans.AssetDetailBean;
import com.tcl.dias.serviceinventory.beans.AttributeDetail;
import com.tcl.dias.serviceinventory.beans.CloudServiceInformationBean;
import com.tcl.dias.serviceinventory.beans.ComponentBean;
import com.tcl.dias.serviceinventory.beans.ContactBeans;
import com.tcl.dias.serviceinventory.beans.CustomerOrderDetailBean;
import com.tcl.dias.serviceinventory.beans.CustomerOrderDetailsBean;
import com.tcl.dias.serviceinventory.beans.GSCConfigurationDetailsBean;
import com.tcl.dias.serviceinventory.beans.GSCConfigurationDisntictDetailsBean;
import com.tcl.dias.serviceinventory.beans.GSCServiceCatalogRequest;
import com.tcl.dias.serviceinventory.beans.GdeSIServiceDetailedResponse;
import com.tcl.dias.serviceinventory.beans.GdeSIServiceInformationBean;
import com.tcl.dias.serviceinventory.beans.GdeSISolutionDataOffering;
import com.tcl.dias.serviceinventory.beans.GdeSolutionAttributes;
import com.tcl.dias.serviceinventory.beans.GscServiceInventoryConfigurationRequestBean;
import com.tcl.dias.serviceinventory.beans.IPCInformationBean;
import com.tcl.dias.serviceinventory.beans.NPLSIServiceDetailedResponse;
import com.tcl.dias.serviceinventory.beans.NPLSISolutionDataOffering;
import com.tcl.dias.serviceinventory.beans.NPLSolutionAttributes;
import com.tcl.dias.serviceinventory.beans.PartnerDetailBean;
import com.tcl.dias.serviceinventory.beans.ProductFamilyRequest;
import com.tcl.dias.serviceinventory.beans.SIAssetBean;
import com.tcl.dias.serviceinventory.beans.SIAttributeBean;
import com.tcl.dias.serviceinventory.beans.SIConfigurationByLeBean;
import com.tcl.dias.serviceinventory.beans.SIConfigurationCountryBean;
import com.tcl.dias.serviceinventory.beans.SICountryBean;
import com.tcl.dias.serviceinventory.beans.SIDetailedInfoResponse;
import com.tcl.dias.serviceinventory.beans.SINumberConfigurationBean;
import com.tcl.dias.serviceinventory.beans.SINumbersBySiteBean;
import com.tcl.dias.serviceinventory.beans.SIOrderBean;
import com.tcl.dias.serviceinventory.beans.SIOrderNumberBean;
import com.tcl.dias.serviceinventory.beans.SIProductFamilySummaryBean;
import com.tcl.dias.serviceinventory.beans.SIServiceDetailBean;
import com.tcl.dias.serviceinventory.beans.SIServiceDetailedResponse;
import com.tcl.dias.serviceinventory.beans.SIServiceExcelBean;
import com.tcl.dias.serviceinventory.beans.SIServiceInformationBean;
import com.tcl.dias.serviceinventory.beans.SISiteConfigurationBean;
import com.tcl.dias.serviceinventory.beans.SISolutionDataOffering;
import com.tcl.dias.serviceinventory.beans.SISolutionOffering;
import com.tcl.dias.serviceinventory.beans.SatSocResponse;
import com.tcl.dias.serviceinventory.beans.ServiceCatalogRequest;
import com.tcl.dias.serviceinventory.beans.SiAttachmentBean;
import com.tcl.dias.serviceinventory.beans.SolutionAttributes;
import com.tcl.dias.serviceinventory.beans.UpdateDIDServiceBean;
import com.tcl.dias.serviceinventory.beans.UpdateDataServiceBean;
import com.tcl.dias.serviceinventory.beans.UpdateTollFreeServiceBean;
import com.tcl.dias.serviceinventory.constants.ExceptionConstants;
import com.tcl.dias.serviceinventory.constants.MACDConstants;
import com.tcl.dias.serviceinventory.constants.RenewalsServiceAttributeConstants;
import com.tcl.dias.serviceinventory.constants.SiServiceAttributeConstants;
import com.tcl.dias.serviceinventory.dao.v1.ServiceInventoryDao;
import com.tcl.dias.serviceinventory.entity.entities.Attachment;
import com.tcl.dias.serviceinventory.entity.entities.GscInterconnectDetails;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetCommercial;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetComponent;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetRelation;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetToService;
import com.tcl.dias.serviceinventory.entity.entities.SIAttachment;
import com.tcl.dias.serviceinventory.entity.entities.SIComponentAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIContractInfo;
import com.tcl.dias.serviceinventory.entity.entities.SICustomerInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIInfoGVPNServiceUCAAS;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIOrderAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAdditionalInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceInfo;
import com.tcl.dias.serviceinventory.entity.entities.ServiceInvRf;
import com.tcl.dias.serviceinventory.entity.entities.SiServiceContact;
import com.tcl.dias.serviceinventory.entity.entities.ViewGscServiceCircuitLinkDetail;
import com.tcl.dias.serviceinventory.entity.entities.ViewPriceRevisionDetail;
import com.tcl.dias.serviceinventory.entity.entities.ViewSiServiceInfoAll;
import com.tcl.dias.serviceinventory.entity.entities.VwOrderServiceAssetInfo;
import com.tcl.dias.serviceinventory.entity.entities.VwServiceAssetInfo;
import com.tcl.dias.serviceinventory.entity.repository.AttachmentRepository;
import com.tcl.dias.serviceinventory.entity.repository.GscInterconnectDetailsRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetCommercialRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetComponentRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRelationRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAttachmentRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIComponentRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIContractInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SICustomerInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIInfoGVPNServiceUCAASRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceSlaRepository;
import com.tcl.dias.serviceinventory.entity.repository.ServiceInvRfRepository;
import com.tcl.dias.serviceinventory.entity.repository.ServiceInvSatSocRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiGenevaComponentMvRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceContactRepository;
import com.tcl.dias.serviceinventory.entity.repository.ViewPriceRevisionDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwGscPulseChargeConfigDetailsRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwGscServiceCircuitLinkDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwOrderServiceAssetInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwSiServiceInfoAllRepository;
import com.tcl.dias.serviceinventory.helper.mapper.ServiceInventoryHelperMapper;
import com.tcl.dias.serviceinventory.entity.repository.VwServiceAssetInfoRepository;
import com.tcl.dias.serviceinventory.mapper.ServiceInventoryMapper;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import com.tcl.dias.serviceinventory.util.ServiceInventoryUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ACCESS_TYPE;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ASSET_COUNT;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ASSET_ID;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ASSET_TYPE;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ASSET_TYPE_TFN;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.CUSTOMER_LE_ID;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.CUSTOMER_LE_NAME;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.DESTINATION;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.DOMESTIC_VOICE;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ERROR_KEY_ORDER_NOT_FOUND;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.NO_COUNT;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.NUMBER;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ORDER_ID;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ORDER_SEQUENCE_ID;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ORIGIN;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.ORIGIN_NETWORK;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.OUTPULSE;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.OUTPULSE_EXCEL_ATTR;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.PRODUCT_FAMILY_GSIP;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.SECS_ID;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.SITE_ADDRESS;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.SITE_COUNT;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.SUB_VARIANT;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.TERMINATED;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.TIGER_ORDER_ID;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryConstants.TOLL_FREE_NUMBER;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryUtils.error;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryUtils.mapRows;
import static java.lang.String.valueOf;

/**
 * Service class for service inventory related API
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceInventoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryService.class);
    public static final String SWTCH_UNIT_CD_RERT = "SWTCH_UNIT_CD_RERT";
    public static final String TERM_IP = "TERM_IP";
	public static final String CIRCT_GR_CD_RERT = "CIRCT_GR_CD_RERT";
	public static final String CIRCUIT_ID = "Circuit_ID";
	public static final String CIRCUIT_UNITS = "Circuit Units";
	public static final String CIRCUIT_ID1 = "Circuit ID";
	public static final String SWITCH_UNITS = "Switch Units";
    public static final String ID = "id";
    public static final String CIRCT_GR_CD = "circt_gr_cd";
    public static final String CIRCUIT_ID2 = "circuit_id";
    public static final String CUST_IP_ADDR = "cust_ip_addr";
    public static final String ERF_CUST_CUSTOMER_ID = "erf_cust_customer_id";
    public static final String SWTCH_UNIT_CD = "swtch_unit_cd";
    public static final String SFDC_CUID = "sfdc_cuid";
	public static final String ALL = "ALL";
	public static final String ORG_ID = "org_no";
	public static final String INTERCONNECT_ID = "interconnect_id";
	public static final String INTERCONNECT_NAME = "interconnect_name";

	@Autowired
	SIServiceSlaRepository siServiceSlaRepository;

	@Autowired
	SIOrderRepository siOrderRepository;
	
	@Autowired
	VwOrderServiceAssetInfoRepository vwOrderServiceAssetInfoRepository;

	@Autowired
	SiServiceContactRepository siServiceContactRepository;

	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	AuthTokenDetailRepository authTokenDetailRepository;

	@Autowired
	SIAssetRepository siAssetRepository;
	
	@Autowired
	SIAssetAttributeRepository siAssetAttributeRepository;

	@Autowired
	SIAssetRelationRepository siAssetRelationRepository;
	
	@Autowired
	SIServiceInfoRepository siServiceInfoRepository;

	@Autowired
	ServiceInvRfRepository serviceInvRfRepository;

	@Autowired
	SIComponentRepository siComponentRepository;

	@Autowired
	SIAttachmentRepository siAttachmentRepository;

	@Autowired
	ViewPriceRevisionDetailRepository viewPriceRevisionDetailRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.product.get.all}")
	String productDetailsQueue;
	
	@Value("${info.oms.get.order.count.queue}")
	String ordersCountQueue;
	
	@Value("${si.base.url}")
	String baseUrl;
	
	@Value("${si.request.url}")
	String requestUrl;
	
	@Value("${si.appId}")
	String appId;
	
	@Value("${si.appSecret}")
	String appSecret;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${rabbitmq.get.partner.legal.entities}")
	String partnerIdsDetailsQueue;

	@Autowired
    FileStorageService fileStorageService;

	@Autowired
	ServiceInventoryDao serviceInventoryDao;

	@Value("${api.googlemap.key}")
	private String googleMapApiKey;

	@Value("${api.googlemap}")
	private String googleMapAPI;
	
	@Value("${oms.customer.queue}")
	private String cusQueue;

	@Value("${oms.macd.queue}")
	private String macdQueue;
	
	@Value("${oms.ipc.macd.queue}")
	private String macdIPCQueue;

	@Autowired
	RestClientService restClientService;
	
	@Autowired
	SiServiceAttributeRepository siServiceAttributeRepository;

	@Autowired
	VwOrderServiceAssetDetailSpecification vwOrderServiceAssetDetailSpecification;
	
	@Autowired
	SIAssetComponentRepository siAssetComponentRepository;
	
	@Autowired
	SIAssetCommercialRepository siAssetCommercialRepository;
	
	@Autowired
	ServiceInventoryMapper serviceInventoryMapper;

	@Autowired
	ServiceInventoryHelperMapper serviceInventoryHelperMapper;

	@Autowired
	SIContractInfoRepository siContractInfoRepository;
	
	@Autowired
	SIOrderAttributeRepository siOrderAttributeRepository;
	
	@Autowired
	VwSiServiceInfoAllRepository vwSiServiceInfoAllRepository;

	@Autowired
	ServiceInvSatSocRepository serviceInvSatSocRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	AttachmentRepository attachmentRepository;
	
	@Autowired
	SIInfoGVPNServiceUCAASRepository siInfoGVPNServiceUCAASRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;
	
	@Autowired
	SiGenevaComponentMvRepository siGenevaComponentMvRepository;

	@Autowired
	VwGscServiceCircuitLinkDetailRepository vwGscServiceCircuitLinkDetailRepository;
	
	@Autowired
	SICustomerInfoRepository siCustomerInfoRepository;
	
	@Value("${rabbitmq.product.get.by.id}")
	String productDetailsByIdQueue;

	@Autowired
	SIServiceAdditionalInfoRepository siServiceAdditionalInfoRepository;

	@Autowired
	GscInterconnectDetailsRepository gscInterconnectDetailsRepository;

	@Autowired
	VwGscPulseChargeConfigDetailsRepository vwGscPulseChargeConfigDetailsRepository;
	
	@Value("${rabbitmq.engagementInfo.get}")
	String getEngagementDetails;
	
	@Autowired
	VwServiceAssetInfoRepository vwServiceAssetInfoRepository;
	

	/**
	 * Method to get order details
	 *
	 * @param erfCustId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SIOrderBean> getOrderDetails(Integer erfCustId) throws TclCommonException {

		if (Objects.isNull(erfCustId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		return siOrderRepository.findByErfCustCustomerIdAndOrderStatusNotIgnoreCase(erfCustId.toString(), TERMINATED)
				.stream().map(SIOrderBean::new).collect(Collectors.toList());
	}

	/**
	 * Method to get service details for a particular service ID
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceDetailBean getServiceDetails(String serviceId, String product, String number, String outpulse) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		
		Optional<SIServiceDetail> optDetail = null;
		if("GSC".equalsIgnoreCase(product)) {
			if (StringUtils.isBlank(number) ) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			if(outpulse!=null) {
				optDetail = siServiceDetailRepository.findByTpsServiceIdForGSC(serviceId, number, outpulse);
			}else {
			    optDetail =  siServiceDetailRepository.findByTpsServiceIdWithoutOutpulseForGSC(serviceId, number);		
			}    
		} else {
			optDetail = siServiceDetailRepository.findByTpsServiceId(serviceId);
		}

		if (!optDetail.isPresent()) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}

		return new SIServiceDetailBean(optDetail.get());

	}
	
	/**
	 * Method to get service details for a particular service ID - MF Specific
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public String getLastMileProvider(String serviceId, String product, String number, String outpulse) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		
		Optional<SIServiceDetail> optDetail = null;
		if("GSC".equalsIgnoreCase(product)) {
			if (StringUtils.isBlank(number) || StringUtils.isBlank(outpulse)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			optDetail = siServiceDetailRepository.findByTpsServiceIdForGSC(serviceId, number, outpulse);
		} else {
			optDetail = siServiceDetailRepository.findByTpsServiceId(serviceId);
		}

		if (!optDetail.isPresent()) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}

		return optDetail.get().getLastmileProvider();

	}

	/**
	 * Method to get service details for a particular service ID for NPL product.
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SIServiceDetailBean> getServiceDetailsForNPL(String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		return siServiceDetailRepository.findByTpsServiceIdForNPL(serviceId)
				.stream()
				.map(detail -> new SIServiceDetailBean(detail))
				.collect(Collectors.toList());
	}
	/**
	 * Method to get the customer service id based on the service id
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	
	public String getCustomerServiceId(String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		
		Optional<SIServiceDetail> optDetail = null;

			optDetail = siServiceDetailRepository.findByTpsServiceId(serviceId);

		if (!optDetail.isPresent()) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}

		return optDetail.get().getCustomerServiceId();
		
		}
	
	private SIAttributeBean toAttribute(Map<String, Object> row) {
		SIAttributeBean bean = new SIAttributeBean();
		bean.setName((String) row.get(SUB_VARIANT));
		bean.setValue(valueOf(row.getOrDefault(ASSET_COUNT, 0)));
		return bean;
	}

	private SICountryBean toCountry(Map<String, Object> row) {
		SICountryBean bean = new SICountryBean();
		bean.setOrigin((String) row.get(ORIGIN));
		bean.setDestination((String) row.get(DESTINATION));
		bean.setAccessType(valueOf(row.getOrDefault(ACCESS_TYPE, "")));
		bean.setNumbersCount(valueOf(row.getOrDefault(NO_COUNT, 0)));
		bean.setOrderId((Integer) row.get(ORDER_ID));
		return bean;
	}

	private SIAssetBean toAsset(Map<String, Object> row) {
		SIAssetBean bean = new SIAssetBean();
		bean.setOrigin((String) row.get(ORIGIN));
		bean.setDestination((String) row.get(DESTINATION));
		bean.setAssetId((Integer) row.get(ASSET_ID));
		bean.setOrderId((Integer) row.get(ORDER_ID));
		bean.setAssetType((String) row.get(ASSET_TYPE));
		bean.setNumber((String) row.get(NUMBER));
		bean.setOutpulse((String) row.get(OUTPULSE));
		bean.setOriginNetwork((String) row.get(ORIGIN_NETWORK));
		bean.setTigerOrderId((String) row.get(TIGER_ORDER_ID));
		return bean;
	}

	private List<Integer> getCustomerLeIds(Integer customerId) {
		LOGGER.info("CustomerID:: {}", customerId);
		List<Integer> legalEntityIds = new ArrayList<>();
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			LOGGER.info("Partner flow :: partner Id {}", customerId);
			legalEntityIds.addAll(siOrderRepository.findCustomerLesByPartnerId(customerId));
		} else {
			LOGGER.info("Customer flow :: customer Id {}", customerId);
			legalEntityIds = new ArrayList<>(userInfoUtils.getByErfCustomerId(customerId));
		}
		LOGGER.info("LegalEntity {}", legalEntityIds);
		return legalEntityIds;
	}

	/**
	 * Fetch product family wise statistics
	 *
	 * @param productFamilyName
	 * @return
	 */
	public Try<List<SIProductFamilySummaryBean>> getProductFamilyStats(Integer customerId, String productFamilyName){
		String productFlyName = Optional.ofNullable(productFamilyName).orElse(PRODUCT_FAMILY_GSIP);
		
		/* Queue call start for product short names [CST-585]*/
		String queueResponse = null;
		List<ProductInformationBean> productInformationBeansList = new ArrayList<>();
		//Map<Integer, String> productDetailsMapper = new HashMap<>();
		SIProductFamilySummaryBean summaryBean = new SIProductFamilySummaryBean();
		
		try {
			queueResponse = (String) mqUtils.sendAndReceive(productDetailsQueue, null);
			LOGGER.info("getProductFamilyStats:: queueResponse{} ",queueResponse);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		if (StringUtils.isNotBlank(queueResponse)) {
			productInformationBeansList = Utils.fromJson(queueResponse,
					new TypeReference<List<ProductInformationBean>>() {
					});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		
		for(ProductInformationBean result : productInformationBeansList) {
			if(result.getProductName().equalsIgnoreCase(productFlyName)) {
				summaryBean.setProductfamilyShortName(result.getProductShortName());
			}
		}
		List<SIAttributeBean> attributes = mapRows(
					() -> siServiceDetailRepository.fetchProductFamilywiseAssetCountByCustomerIdAndByType(
							getCustomerLeIds(customerId), productFlyName, ASSET_TYPE_TFN),
					this::toAttribute);
		summaryBean.setProductFamily(productFlyName);
		summaryBean.setAttributes(attributes);
		Integer totalNumbers = attributes.stream().map(SIAttributeBean::getValue).mapToInt(Integer::valueOf).sum();
		summaryBean.setSummary(String.format("%s numbers", totalNumbers));
		return Try.success(ImmutableList.of(summaryBean));
	}

	public SIOrderDataBean getSiOrderData(String serviceId) {
		Optional<SIServiceDetail> serviceDetail = siServiceDetailRepository.findByTpsServiceId(serviceId);
		Optional<SIOrder> siOrder = null;
		SIOrderDataBean orderDataBean = null;
		if (Objects.nonNull(serviceDetail)) {
			siOrder = siOrderRepository.findById(serviceDetail.get().getSiOrder().getId());

			if (Objects.nonNull(siOrder)) {
				orderDataBean = null;
				orderDataBean.setUuid(siOrder.get().getUuid());
				orderDataBean.setCustomerGroupName(siOrder.get().getCustomerGroupName());
				orderDataBean.setCustomerSegment(siOrder.get().getCustomerSegment());
				orderDataBean.setDemoFlag(siOrder.get().getDemoFlag());
				orderDataBean.setErfCustCustomerId(siOrder.get().getErfCustCustomerId());
				orderDataBean.setErfCustLeId(siOrder.get().getErfCustLeId());
				orderDataBean.setErfCustSpLeId(siOrder.get().getErfCustSpLeId());
				orderDataBean.setId(siOrder.get().getId());
				orderDataBean.setSfdcCuid(siOrder.get().getTpsSfdcCuid());
				orderDataBean.setOrderStatus(siOrder.get().getOrderStatus());
				orderDataBean.setOrderSource(siOrder.get().getOrderSource());
				orderDataBean.setOrderType(siOrder.get().getOrderType());
				orderDataBean.setOrderStartDate(siOrder.get().getOrderStartDate());
				orderDataBean.setTpsSecsId(siOrder.get().getTpsSecsId());
				orderDataBean.setTpsSfdcId(siOrder.get().getTpsCrmOptyId());
				orderDataBean.setTpsCrmCofId(siOrder.get().getTpsCrmCofId());
			}
		}

		return orderDataBean;

	}

	public List<SiAttachmentBean> getAllAttachments(String serviceCode) {
		SIServiceDetail siServiceDetail = siServiceDetailRepository
				.findFirstByUuidAndServiceStatusIgnoreCaseAndIsActiveOrderByIdDesc(
						serviceCode, "Active", CommonConstants.Y);
		List<SiAttachmentBean> siAttachmentBeans = new ArrayList<>();
		if(Objects.nonNull(siServiceDetail) && !CollectionUtils.isEmpty(siServiceDetail.getSiAttachments())){
			siServiceDetail.getSiAttachments().forEach(siAttachment -> {
				SiAttachmentBean siAttachmentBean = new SiAttachmentBean();
				Attachment attachment = siAttachment.getAttachment();
				siAttachmentBean.setStoragePathUrl(attachment.getStoragePathUrl());
				siAttachmentBean.setCategory(attachment.getCategory());
				siAttachmentBean.setContentTypeHeader(attachment.getContentTypeHeader());
				siAttachmentBean.setCreatedBy(attachment.getCreatedBy());
				siAttachmentBean.setCreatedDate(attachment.getCreatedDate());
				siAttachmentBean.setName(attachment.getName());
				siAttachmentBean.setIsActive(attachment.getIsActive());
				siAttachmentBean.setAttachmentId(attachment.getId());
				siAttachmentBean.setOfferingName(siAttachment.getOfferingName());
				siAttachmentBeans.add(siAttachmentBean);
			});
		}
		
		
		Set<Integer> attachIdSet = new TreeSet<>();
 		if(Objects.nonNull(siServiceDetail) && !CollectionUtils.isEmpty(siServiceDetail.getSiAttachments())){
			siServiceDetail.getSiAttachments().forEach(siAttachment -> {
				attachIdSet.add(siAttachment.getAttachment().getId());
			});
		}
 		if(!attachIdSet.isEmpty()){
 			List list = new ArrayList(attachIdSet);
 	 		Collections.sort(list, Collections.reverseOrder());
 	 		Set<Integer> resultSet = new LinkedHashSet(list);
 	 		if(!resultSet.isEmpty()){
 	 			Integer latestOne=resultSet.stream().findFirst().get();
 	 			LOGGER.info("Latest record::", latestOne);
 	 			if(Objects.nonNull(siServiceDetail) && !CollectionUtils.isEmpty(siServiceDetail.getSiAttachments())){
 	 				siServiceDetail.getSiAttachments().stream().filter(siAttachment -> siAttachment.getAttachment().getId()==latestOne).forEach(siAttachment -> {
 	 					SiAttachmentBean siAttachmentBean = new SiAttachmentBean();
 	 					Attachment attachment = siAttachment.getAttachment();
 	 					siAttachmentBean.setStoragePathUrl(attachment.getStoragePathUrl());
 	 					siAttachmentBean.setCategory(attachment.getCategory());
 	 					siAttachmentBean.setContentTypeHeader(attachment.getContentTypeHeader());
 	 					siAttachmentBean.setCreatedBy(attachment.getCreatedBy());
 	 					siAttachmentBean.setCreatedDate(attachment.getCreatedDate());
 	 					siAttachmentBean.setName(attachment.getName());
 	 					siAttachmentBean.setIsActive(attachment.getIsActive());
 	 					siAttachmentBean.setAttachmentId(attachment.getId());
 	 					siAttachmentBean.setOfferingName(siAttachment.getOfferingName());
 	 					siAttachmentBeans.add(siAttachmentBean);
 	 					//return siAttachmentBeans;
 	 				});
 	 			}
 	 		}
 		}
		return siAttachmentBeans;
	}

	public List<Map<String, Object>> getServiceSlas(String serviceCode) {
		SIServiceDetail siServiceDetail = siServiceDetailRepository
				.findFirstByUuidAndServiceStatusIgnoreCaseAndIsActiveOrderByIdDesc(
						serviceCode, "Active", CommonConstants.Y);
		if(Objects.nonNull(siServiceDetail)){
			return siServiceSlaRepository.findByServiceId(siServiceDetail.getId());
		}
		return null;
	}

	public void deleteRfInventory(int id) {
		serviceInvRfRepository.deleteAllById(id);
	}

	public List<RfDumpWirelessOneBean> enrichRfDetailList(SuipListBean suipListBean, String provider) {
		List <RfDumpWirelessOneBean>rfDumpWirelessOneBeanList = new ArrayList<>();
		if(!StringUtils.isEmpty(provider)) {
			if (provider.contains("p2p") || provider.equalsIgnoreCase("Radwin from TCL POP")) {
				String wirelessScenario="Radwin from TCL POP";
				List <ServiceInvRf> serviceInvRfs = new ArrayList<>();
				serviceInvRfs = serviceInvRfRepository.findWithProviderAndStatus(wirelessScenario);
				for (ServiceInvRf serviceInvRf: serviceInvRfs ) {
					RfDumpWirelessOneBean rfDumpWirelessOneBean = new RfDumpWirelessOneBean();
					BeanUtils.copyProperties(serviceInvRf , rfDumpWirelessOneBean);
					rfDumpWirelessOneBeanList.add(rfDumpWirelessOneBean);
				}
			}
		}
		else {

			suipListBean.getSuipList().forEach(suIp -> {
				RfDumpWirelessOneBean rfDumpWirelessOneBean = new RfDumpWirelessOneBean();
				ServiceInvRf serviceInvRf = serviceInvRfRepository.findWithSsIpAndStatus(suIp);
				if (!Objects.isNull(serviceInvRf)) {
					BeanUtils.copyProperties(serviceInvRf, rfDumpWirelessOneBean);
				} else {
					rfDumpWirelessOneBean.setErrorMessage("No data found for the given SuIp : " + suIp);
				}
				rfDumpWirelessOneBeanList.add(rfDumpWirelessOneBean);
			});
		}
		return  rfDumpWirelessOneBeanList;
	}

    static class GetOrderDataContext {
		SIGetOrderRequest request;
		SIOrderDataBean orderData;
		SIOrder order;
	}

	private GetOrderDataContext populateAssets(GetOrderDataContext context) {
		if (!context.request.getAssets()) {
			return context;
		}
		List<String> assetTypes = context.request.getAssetTypes();
		List<String> assetRelationTypes = context.request.getAssetRelationTypes();
		Preconditions.checkArgument(!CollectionUtils.isEmpty(assetTypes), "At least one asset type(s) is mandatory");
		Preconditions.checkArgument(!CollectionUtils.isEmpty(assetRelationTypes),
				"At least one asset relation type(s) is mandatory");
		context.orderData.getServiceDetails().forEach(siServiceDetail -> {
			List<Integer> assetIds = siAssetRepository.findAllAssetIdsByServiceDetailAndAssetTypes(
					siServiceDetail.getId(), assetTypes, assetRelationTypes);
			if (!CollectionUtils.isEmpty(assetIds)) {
				List<SIAsset> assets = siAssetRepository.findAllById(assetIds);
				List<com.tcl.dias.common.serviceinventory.beans.SIAssetBean> SIAssetBeans = assets.stream()
						.map(siAsset -> {
							com.tcl.dias.common.serviceinventory.beans.SIAssetBean bean = new com.tcl.dias.common.serviceinventory.beans.SIAssetBean();
							bean.setId(siAsset.getId());
							bean.setAssetGroupId(siAsset.getAssetGroupId());
							bean.setAssetGroupType(siAsset.getAssetGroupType());
							bean.setAssetStatus(siAsset.getAssetStatus());
							bean.setAssetTag(siAsset.getAssetTag());
							bean.setErfCustomerId(siAsset.getErfCustomerId());
							bean.setPublicIp(siAsset.getPublicIp());
							bean.setSerialNo(siAsset.getSerialNo());
							bean.setTerminationDate(siAsset.getTerminationDate());
							bean.setType(siAsset.getType());
							bean.setName(siAsset.getName());
							bean.setFqdn(siAsset.getFqdn());
							if (context.request.getAssetAttributes()) {
							}
							return bean;
						}).collect(Collectors.toList());
				siServiceDetail.setAssets(SIAssetBeans);
				List<SIAssetRelation> relations = siAssetRelationRepository
						.findAllBySiAssetIdInAndRelationTypeIn(assetIds, assetRelationTypes);
				List<SIAssetRelationBean> relationBeans = relations.stream().map(siAssetRelation -> {
					SIAssetRelationBean bean = new SIAssetRelationBean();
					bean.setAssetId(siAssetRelation.getSiAssetId());
					bean.setSiRelatedAssetId(siAssetRelation.getSiRelatedAssetId());
					bean.setRelationType(siAssetRelation.getRelationType());
					bean.setStartDate(siAssetRelation.getStartDate());
					bean.setEndDate(siAssetRelation.getEndDate());
					return bean;
				}).collect(Collectors.toList());
				siServiceDetail.setAssetRelations(relationBeans);
			}
		});
		return context;
	}

	private GetOrderDataContext populateServiceDetails(GetOrderDataContext context) {
		List<SIServiceDetailDataBean> detailDataBeans = context.order.getSiServiceDetails().stream()
				.filter(siServiceDetail -> "Active".equals(siServiceDetail.getServiceStatus()) && "Y".equals(siServiceDetail.getIsActive()))
				.map(siServiceDetail -> {
					SIServiceDetailDataBean bean = new SIServiceDetailDataBean();
					bean.setId(siServiceDetail.getId());
					bean.setBillingAccountId(siServiceDetail.getBillingAccountId());
					bean.setErfLocSiteAddressId(siServiceDetail.getErfLocSiteAddressId());
					bean.setBillingType(siServiceDetail.getBillingType());
					bean.setErfLocGscDestinationCityId(siServiceDetail.getErfLocDestinationCityId());
					bean.setErfLocGscDestinationCountryId(siServiceDetail.getErfLocDestinationCountryId());
					bean.setErfLocGscSourceCityId(siServiceDetail.getErfLocSourceCityId());
					bean.setErfLocGscSrcCountryId(siServiceDetail.getErfLocSrcCountryId());
					bean.setErfPrdCatalogOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
					bean.setErfPrdCatalogOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
					bean.setErfPrdCatalogParentProductName(siServiceDetail.getErfPrdCatalogParentProductName());
					bean.setErfPrdCatalogParentProductOfferingName(
							siServiceDetail.getErfPrdCatalogParentProductOfferingName());
					bean.setErfPrdCatalogProductId(siServiceDetail.getErfPrdCatalogProductId());
					bean.setErfPrdCatalogProductName(siServiceDetail.getErfPrdCatalogProductName());
					bean.setFeasibilityId(siServiceDetail.getFeasibilityId());
					bean.setGscDestinationCity(siServiceDetail.getDestinationCity());
					bean.setGscDestinationCountry(siServiceDetail.getDestinationCountry());
					bean.setSourceCountryCode(siServiceDetail.getSourceCountryCode());
					bean.setDestinationCountryCode(siServiceDetail.getDestinationCountryCode());
					bean.setGscSourceCity(siServiceDetail.getSourceCity());
					bean.setGscSourceCountry(siServiceDetail.getSourceCountry());
					bean.setParentBundleServiceId(siServiceDetail.getParentBundleServiceId());
					bean.setOrderUuid(siServiceDetail.getSiOrderUuid());
					bean.setTpsServiceId(siServiceDetail.getTpsServiceId());
					bean.setTpsSourceServiceId(siServiceDetail.getTpsSourceServiceId());
					bean.setUuid(siServiceDetail.getUuid());
					bean.setMrc(siServiceDetail.getMrc());
					bean.setArc(siServiceDetail.getArc());
					bean.setNrc(siServiceDetail.getNrc());
					bean.setLastmileBw(siServiceDetail.getLastmileBw());
					bean.setLastmileBwUnit(siServiceDetail.getLastmileBwUnit());
					bean.setPortBw(siServiceDetail.getBwPortspeed());
					bean.setPortBwUnit(siServiceDetail.getBwUnit());
					bean.setAlias(siServiceDetail.getSiteAlias());
					bean.setServiceCommissionedDate(siServiceDetail.getServiceCommissionedDate());
					bean.setLinkId(siServiceDetail.getPriSecServiceLink());
					bean.setLinkType(siServiceDetail.getPrimarySecondary());
					bean.setLatLong(siServiceDetail.getLatLong());
					bean.setVpnName(siServiceDetail.getVpnName());
					bean.setAccessProvider(siServiceDetail.getLastmileProvider());
					bean.setAccessType(siServiceDetail.getAccessType());
					bean.setSiteAddress(siServiceDetail.getSiteAddress());
					bean.setPriSecServLink(siServiceDetail.getPriSecServiceLink());
					bean.setDemarcationApartment(siServiceDetail.getDemarcationApartment());
					bean.setDemarcationFloor(siServiceDetail.getDemarcationFloor());
					bean.setDemarcationRack(siServiceDetail.getDemarcationRack());
					bean.setDemarcationRoom(siServiceDetail.getDemarcationRoom());

					//reset bandwidth for GVPN
					/*if(siServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN"))
					{
						resetBandwidth(bean);
					}*/

					final ObjectMapper mapper=new ObjectMapper();
					LOGGER.info("serviceDetailId"+siServiceDetail.getId());
					List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(siServiceDetail.getId());
					LOGGER.info("data {}", data);
					if(data.stream().findFirst().isPresent()) {
						ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
						bean.setContractTerm(serviceDetail.getTermInMonths());
						bean.setReferenceOrderId(siServiceDetail.getGscOrderSequenceId());
						bean.setBillingFrequency(serviceDetail.getBillingFrequency());
						bean.setBillingMethod(serviceDetail.getBillingMethod());
						bean.setPaymentTerm(serviceDetail.getPaymentTerm());
						if (context.request.getServiceDetailAttributes()) {
							// TODO populate service detail attributes
						}
					}
					bean.setAttributes(populateServiceAttribute(siServiceDetail.getSiServiceAttributes()));
					return bean;
				}).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(detailDataBeans)) {
			context.orderData.setServiceDetails(detailDataBeans);
		}
		return context;
	}

	private GetOrderDataContext populateServiceWholesaleDetails(GetOrderDataContext context) {
		List<SIServiceDetailDataBean> detailDataBeans = context.order.getSiServiceDetails().stream()
				.filter(siServiceDetail -> "Active".equals(siServiceDetail.getServiceStatus()) && "Y".equals(siServiceDetail.getIsActive()) && context.request.getOriginCountry().equals(siServiceDetail.getSourceCountry()))
				.map(siServiceDetail -> {
					SIServiceDetailDataBean bean = new SIServiceDetailDataBean();
					bean.setId(siServiceDetail.getId());
					bean.setBillingAccountId(siServiceDetail.getBillingAccountId());
					bean.setErfLocSiteAddressId(siServiceDetail.getErfLocSiteAddressId());
					bean.setBillingType(siServiceDetail.getBillingType());
					bean.setErfLocGscDestinationCityId(siServiceDetail.getErfLocDestinationCityId());
					bean.setErfLocGscDestinationCountryId(siServiceDetail.getErfLocDestinationCountryId());
					bean.setErfLocGscSourceCityId(siServiceDetail.getErfLocSourceCityId());
					bean.setErfLocGscSrcCountryId(siServiceDetail.getErfLocSrcCountryId());
					bean.setErfPrdCatalogOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
					bean.setErfPrdCatalogOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
					bean.setErfPrdCatalogParentProductName(siServiceDetail.getErfPrdCatalogParentProductName());
					bean.setErfPrdCatalogParentProductOfferingName(
							siServiceDetail.getErfPrdCatalogParentProductOfferingName());
					bean.setErfPrdCatalogProductId(siServiceDetail.getErfPrdCatalogProductId());
					bean.setErfPrdCatalogProductName(siServiceDetail.getErfPrdCatalogProductName());
					bean.setFeasibilityId(siServiceDetail.getFeasibilityId());
					bean.setGscDestinationCity(siServiceDetail.getDestinationCity());
					bean.setGscDestinationCountry(siServiceDetail.getDestinationCountry());
					bean.setSourceCountryCode(siServiceDetail.getSourceCountryCode());
					bean.setDestinationCountryCode(siServiceDetail.getDestinationCountryCode());
					bean.setGscSourceCity(siServiceDetail.getSourceCity());
					bean.setGscSourceCountry(siServiceDetail.getSourceCountry());
					bean.setParentBundleServiceId(siServiceDetail.getParentBundleServiceId());
					bean.setOrderUuid(siServiceDetail.getSiOrderUuid());
					bean.setTpsServiceId(siServiceDetail.getTpsServiceId());
					bean.setTpsSourceServiceId(siServiceDetail.getTpsSourceServiceId());
					bean.setUuid(siServiceDetail.getUuid());
					bean.setMrc(siServiceDetail.getMrc());
					bean.setArc(siServiceDetail.getArc());
					bean.setNrc(siServiceDetail.getNrc());
					bean.setLastmileBw(siServiceDetail.getLastmileBw());
					bean.setLastmileBwUnit(siServiceDetail.getLastmileBwUnit());
					bean.setPortBw(siServiceDetail.getBwPortspeed());
					bean.setPortBwUnit(siServiceDetail.getBwUnit());
					bean.setAlias(siServiceDetail.getSiteAlias());
					bean.setServiceCommissionedDate(siServiceDetail.getServiceCommissionedDate());
					bean.setLinkId(siServiceDetail.getPriSecServiceLink());
					bean.setLinkType(siServiceDetail.getPrimarySecondary());
					bean.setLatLong(siServiceDetail.getLatLong());
					bean.setVpnName(siServiceDetail.getVpnName());
					bean.setAccessProvider(siServiceDetail.getLastmileProvider());
					bean.setAccessType(siServiceDetail.getAccessType());
					bean.setSiteAddress(siServiceDetail.getSiteAddress());
					bean.setPriSecServLink(siServiceDetail.getPriSecServiceLink());
					bean.setDemarcationApartment(siServiceDetail.getDemarcationApartment());
					bean.setDemarcationFloor(siServiceDetail.getDemarcationFloor());
					bean.setDemarcationRack(siServiceDetail.getDemarcationRack());
					bean.setDemarcationRoom(siServiceDetail.getDemarcationRoom());

					//reset bandwidth for GVPN
					/*if(siServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN"))
					{
						resetBandwidth(bean);
					}*/

					final ObjectMapper mapper=new ObjectMapper();
					LOGGER.info("serviceDetailId"+siServiceDetail.getId());
					List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(siServiceDetail.getId());
					LOGGER.info("data {}", data);
					if(data.stream().findFirst().isPresent()) {
						ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
						bean.setContractTerm(serviceDetail.getTermInMonths());
						bean.setReferenceOrderId(siServiceDetail.getGscOrderSequenceId());
						bean.setBillingFrequency(serviceDetail.getBillingFrequency());
						bean.setBillingMethod(serviceDetail.getBillingMethod());
						bean.setPaymentTerm(serviceDetail.getPaymentTerm());
						if (context.request.getServiceDetailAttributes()) {
							// TODO populate service detail attributes
						}
					}
					bean.setAttributes(populateServiceAttribute(siServiceDetail.getSiServiceAttributes()));
					return bean;
				}).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(detailDataBeans)) {
			context.orderData.setServiceDetails(detailDataBeans);
		}
		return context;
	}



	@Transactional
	public Integer getOrderIdFromServiceId(String tpsServiceId)
	{
		SIServiceDetail siServiceDetail = new SIServiceDetail();
		if(siServiceDetailRepository.findByTpsServiceId(tpsServiceId).isPresent())
			siServiceDetail = siServiceDetailRepository.findByTpsServiceId(tpsServiceId).get() ;
		if(Objects.nonNull(siServiceDetail.getSiOrder().getId())){
			Integer orderId = siServiceDetail.getSiOrder().getId();
			//Integer response = Integer.parseInt(orderId.toString());
			LOGGER.info("order id is {}", orderId);
			return orderId;
		}
		return null;
	}

	private GetOrderDataContext populateOrderData(GetOrderDataContext context) {
		Integer orderId = Ints.tryParse(context.request.getOrderId());
		SIOrder order = Optional.ofNullable(orderId).flatMap(siOrderRepository::findById)
				.orElseThrow(() -> error(ERROR_KEY_ORDER_NOT_FOUND,
						String.format("Order with id: %s not found", context.request.getOrderId())));
		SIOrderDataBean orderDataBean = context.orderData;
		orderDataBean.setUuid(order.getUuid());
		orderDataBean.setCustomerGroupName(order.getCustomerGroupName());
		orderDataBean.setCustomerSegment(order.getCustomerSegment());
		orderDataBean.setDemoFlag(order.getDemoFlag());
		orderDataBean.setErfCustCustomerId(order.getErfCustCustomerId());
		orderDataBean.setErfCustLeId(order.getErfCustLeId());
		orderDataBean.setErfCustSpLeId(order.getErfCustSpLeId());
		orderDataBean.setId(order.getId());
		orderDataBean.setSfdcCuid(order.getTpsSfdcCuid());
		orderDataBean.setOrderStatus(order.getOrderStatus());
		orderDataBean.setOrderSource(order.getOrderSource());
		orderDataBean.setOrderType(order.getOrderType());
		orderDataBean.setOrderStartDate(order.getOrderStartDate());
		orderDataBean.setTpsSecsId(order.getTpsSecsId());
		orderDataBean.setTpsSfdcId(order.getTpsCrmOptyId());
		orderDataBean.setTpsCrmCofId(order.getTpsCrmCofId());
		context.order = order;
		if (context.request.getOrderAttributes()) {
			// TODO populate order attributes
		}
		return context;
	}

	@Transactional
	public Try<SIOrderDataBean> getOrderData(SIGetOrderRequest request) {
		return Try.success(request).map(request1 -> {
			GetOrderDataContext context = new GetOrderDataContext();
			context.request = request1;
			context.orderData = new SIOrderDataBean();
			return context;
		}).map(this::populateOrderData)
				.map(this::populateServiceDetails)
				.map(this::populateAssets)
				.map(ctx -> ctx.orderData);
	}

	@Transactional
	public Try<SIOrderDataBean> getOrderWholesaleData(SIGetOrderRequest request) {
		return Try.success(request).map(request1 -> {
			GetOrderDataContext context = new GetOrderDataContext();
			context.request = request1;
			context.orderData = new SIOrderDataBean();
			return context;
		}).map(this::populateOrderData)
				.map(this::populateServiceWholesaleDetails)
				.map(this::populateAssets)
				.map(ctx -> ctx.orderData);
	}


	/**
	 * Get Configurations based on access type
	 *
	 * @param productName
	 * @param accessType
	 * @param tollFreeNumber
	 * @param orderId
	 * @return {@link List<SIConfigurationCountryBean>}
	 */

	public Try<List<SIConfigurationCountryBean>> getAccessTypeBasedConfigurations(Integer customerId, String productName, String accessType, String tollFreeNumber, String orderId) {
		List<SICountryBean> countries =  new ArrayList<>();
//				serviceInventoryDao.findConfigurationsByParams(productName, accessType, tollFreeNumber,
//								ASSET_TYPE_TFN, getCustomerLeIds(customerId));
		if(ServiceInventoryConstants.UIFN.equalsIgnoreCase(productName)) {
			return Try.success(getSiConfigurationCountryBeanForUIFN(countries));
		}
		return Try.success(getSiConfigurationCountryBean(countries));

	}

	/**
	 * Get Numbers for given configurations
	 *
	 * @param accessType
	 * @param productName
	 * @param origin
	 * @param destination
	 * @return {@link List< SINumberConfigurationBean >}
	 */
	public Try<List<SINumberConfigurationBean>> getNumbersFromConfigurations(String productName, GscServiceInventoryConfigurationRequestBean request) {
		List<GSCConfigurationDetailsBean> gscConfigurationDetailsBeans = new ArrayList<>();
		if (Objects.isNull(request.getDestination()) || CollectionUtils.isEmpty(request.getDestination())) {
			gscConfigurationDetailsBeans = mapRows(() -> siServiceDetailRepository.findAssetNumberBasedOnConfigurationsByOrigin(request.getCustomerId(), request.getOrigin(), productName, ASSET_TYPE_TFN), this::toConfigurationDetails);
		} else {
			gscConfigurationDetailsBeans = mapRows(() -> siServiceDetailRepository.findAssetNumberBasedOnConfigurations(request.getCustomerId(), request.getOrigin(), request.getDestination(), productName, ASSET_TYPE_TFN), this::toConfigurationDetails);
		}

		if(!CollectionUtils.isEmpty(gscConfigurationDetailsBeans)) {
			gscConfigurationDetailsBeans = updateOutpulseNumbersForGSC(gscConfigurationDetailsBeans, request, productName);
		}

		List<GSCConfigurationDetailsBean> configurationDetailsBeanBasedOnFilterValue = getConfigurationDetailsBeanBasedOnFilterValue(request, gscConfigurationDetailsBeans);

		return Try.success(configurationDetailsBeanBasedOnFilterValue.stream().map(configurationDetailsBean -> {
			SINumberConfigurationBean siNumberConfigurationBean = new SINumberConfigurationBean();
			siNumberConfigurationBean.setAssetId(configurationDetailsBean.getAssetId());
			siNumberConfigurationBean.setAssetType(configurationDetailsBean.getAssetType());
			SIOrderNumberBean siOrderNumberBean = new SIOrderNumberBean();
			siOrderNumberBean.setOrderId(valueOf(configurationDetailsBean.getOrderId()));
			siOrderNumberBean.setNumber(configurationDetailsBean.getTollFreeNumber());
			// TODO: Need to update this value using SI_Asset_Relation table
			siOrderNumberBean.setOutpulse(configurationDetailsBean.getOutpulse());
			siOrderNumberBean.setStatus("OPEN");
			siOrderNumberBean.setOriginNetwork(configurationDetailsBean.getOriginNetwork());
			siOrderNumberBean.setTigerOrderId(configurationDetailsBean.getTigerOrderId());
			siNumberConfigurationBean.setAttributes(siOrderNumberBean);
			return siNumberConfigurationBean;
		}).collect(Collectors.toList()));
	}

	private List<GSCConfigurationDetailsBean> updateOutpulseNumbersForGSC(List<GSCConfigurationDetailsBean> gscConfigurationDetailsBeans, GscServiceInventoryConfigurationRequestBean request, String productName) {

		List<GSCConfigurationDetailsBean> outpulseBeans = new ArrayList<>();
		List<GSCConfigurationDetailsBean> gscConfigurationDetailsBeansForOutpulse = new ArrayList<>();
		if (Objects.isNull(request.getDestination()) || CollectionUtils.isEmpty(request.getDestination())) {
			gscConfigurationDetailsBeansForOutpulse = mapRows(() -> siServiceDetailRepository.findAssetNumberBasedOnConfigurationsByOrigin(request.getCustomerId(), request.getOrigin(), productName, OUTPULSE_EXCEL_ATTR), this::toConfigurationDetails);
		} else {
			gscConfigurationDetailsBeansForOutpulse = mapRows(() -> siServiceDetailRepository.findAssetNumberBasedOnConfigurations(request.getCustomerId(), request.getOrigin(), request.getDestination(), productName, OUTPULSE_EXCEL_ATTR), this::toConfigurationDetails);
		}
		outpulseBeans.addAll(gscConfigurationDetailsBeansForOutpulse);
		gscConfigurationDetailsBeans.stream().forEach( gscConfigurationDetailsBean -> {
			outpulseBeans.stream().forEach( outpulseBean -> {
			if(gscConfigurationDetailsBean.getGscOrderSequenceId().equalsIgnoreCase(outpulseBean.getGscOrderSequenceId()))
				gscConfigurationDetailsBean.setOutpulse(outpulseBean.getOutpulse());
		});

		/*if(!CollectionUtils.isEmpty(gscConfigurationDetailsBeansForOutpulse)) {
			for (int i = 0; i < gscConfigurationDetailsBeans.size(); i++) {
				String tollFreeSequenceId = gscConfigurationDetailsBeans.get(i).getGscOrderSequenceId();
				for (int j = 0; j < gscConfigurationDetailsBeansForOutpulse.size(); j++) {
					if (tollFreeSequenceId.equalsIgnoreCase(gscConfigurationDetailsBeansForOutpulse.get(j).getGscOrderSequenceId())) {
						gscConfigurationDetailsBeans.get(i).setOutpulse(gscConfigurationDetailsBeansForOutpulse.get(j).getOutpulse());
					}
				}
			}
		}*/
	});
		return gscConfigurationDetailsBeans;
	}

	/**
	 * Get Configurations by order id and product name
	 *
	 * @param orderId
	 * @param productName
	 * @return {@link SIConfigurationCountryBean}
	 */
	public Try<List<SIConfigurationCountryBean>> getConfigurationsByProductAndOrder(Integer customerId, String orderId,
																					String productName) {
		List<SICountryBean> countries = mapRows(
				() -> siServiceDetailRepository.findConfigurationsByOrderAndProductNameAndOrderId(orderId, productName,
						getCustomerLeIds(customerId), ASSET_TYPE_TFN),
				this::toCountry);
		return Try.success(getSiConfigurationCountryBean(countries));
	}

	/**
	 * Get Configurations by product name
	 *
	 * @param productName
	 * @return {@link SIConfigurationCountryBean}
	 */
	public Try<List<SIConfigurationCountryBean>> getConfigurationsByProduct(Integer customerId, String productName) {
		if (ServiceInventoryConstants.DOMESTIC_VOICE.equalsIgnoreCase(productName)) {
			List<SIConfigurationByLeBean> siConfigurationByLeBeans = mapRows(() ->
					siServiceDetailRepository.findSiteAddressConfigurationsByProductName(productName, getCustomerLeIds(customerId), ASSET_TYPE_TFN), this::toCustomerLe);
			return Try.success(getSiConfigurationByCustomerLe(siConfigurationByLeBeans));
		} else {
			List<SICountryBean> countries = mapRows(() -> siServiceDetailRepository
					.findConfigurationsByProductName(productName, getCustomerLeIds(customerId), ASSET_TYPE_TFN, customerId), this::toCountry);
			if (ServiceInventoryConstants.UIFN.equalsIgnoreCase(productName)) {
				return Try.success(getSiConfigurationCountryBeanForUIFN(countries));
			}
			return Try.success(getSiConfigurationCountryBean(countries));
		}
	}

	/**
	 * Set all the confirguations by customer le in SI configuraion bean
	 *
	 * @param siConfigurationByLeBeans
	 * @return {@link List}
	 */
	private List<SIConfigurationCountryBean> getSiConfigurationByCustomerLe(List<SIConfigurationByLeBean> siConfigurationByLeBeans) {
		if(!CollectionUtils.isEmpty(siConfigurationByLeBeans)){
			return siConfigurationByLeBeans.stream().map(siConfigurationByLeBean -> {
				SIConfigurationCountryBean siConfigurationCountryBean = new SIConfigurationCountryBean();
				siConfigurationCountryBean.setCustomerLeName(siConfigurationByLeBean.getCustomerLeName());
				siConfigurationCountryBean.setCustomerLeId(siConfigurationByLeBean.getCustomerLeId());
				siConfigurationCountryBean.setAccessType(siConfigurationByLeBean.getAccessType());
				siConfigurationCountryBean.setNumbersCount(Integer.valueOf(siConfigurationByLeBean.getNumbersCount()));
				siConfigurationCountryBean.setSiteCount(Integer.valueOf(siConfigurationByLeBean.getSiteCount()));
				return siConfigurationCountryBean;
			}).collect(Collectors.toList());
		}
		else{
			return ImmutableList.of();
		}
	}

	/**
	 * Method to map the configuration output to bean
	 *
	 * @param row
	 * @return {@link SIConfigurationByLeBean}
	 */
	private SIConfigurationByLeBean toCustomerLe(Map<String, Object> row) {
		SIConfigurationByLeBean configuration = new SIConfigurationByLeBean();
		configuration.setAccessType(valueOf(row.getOrDefault(ACCESS_TYPE, "")));
		configuration.setCustomerLeId((Integer) row.get(CUSTOMER_LE_ID));
		configuration.setCustomerLeName((String) row.get(CUSTOMER_LE_NAME));
		configuration.setNumbersCount(valueOf(row.getOrDefault(NO_COUNT, 0)));
		configuration.setSiteCount(valueOf(row.getOrDefault(SITE_COUNT, 0)));
		return configuration;
	}

	private List<SIConfigurationCountryBean> getSiConfigurationCountryBean(List<SICountryBean> countries) {

		return countries.stream().map(siCountryBean -> {
			SIConfigurationCountryBean siConfigurationCountryBean = new SIConfigurationCountryBean();
			siConfigurationCountryBean.setOrigin(Arrays.asList(siCountryBean.getOrigin()));
			siConfigurationCountryBean.setDestination(Arrays.asList(siCountryBean.getDestination()));
			siConfigurationCountryBean.setAccessType(siCountryBean.getAccessType());
			siConfigurationCountryBean.setNumbersCount(Integer.valueOf(siCountryBean.getNumbersCount()));
			return siConfigurationCountryBean;
		}).collect(Collectors.toList());

	}

	public List<CustomerOrderDetailBean> getCustomerServices(Integer customerId) throws TclCommonException {
		List<CustomerOrderDetailBean> sIOrders = new ArrayList<>();
		List<CustomerOrderDetailBean> sIOrdersIZOSDWANUnderProv = new ArrayList<>();
		CustomerOrderDetailBean customerOrderDetail = null;
		try {
			List<Integer> customerLeIds = getCustomerLeIds(customerId);
			LOGGER.info("CustomerLeIds to be listed {}", customerLeIds);
			
			/* Queue call start for product short names [CST-585]*/
			String queueResponse = null;
			List<ProductInformationBean> productInformationBeansList = new ArrayList<>();
			
			try {
				queueResponse = (String) mqUtils.sendAndReceive(productDetailsQueue, null);
				LOGGER.info("getCustomerServices::: productDetailsQueue:: {}",queueResponse);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
			if (StringUtils.isNotBlank(queueResponse)) {
				productInformationBeansList = Utils.fromJson(queueResponse,
						new TypeReference<List<ProductInformationBean>>() {
						});
			} else {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			/* queue call-end*/
			
			List<Map<String, Object>> siOrders = siOrderRepository.findByCustLeIdInAndOrderStatus(customerLeIds,
					ServiceInventoryConstants.TERMINATED,customerId);
			for (Map<String, Object> sIOrder : siOrders) {
				customerOrderDetail = new CustomerOrderDetailBean();
				customerOrderDetail.setCity(sIOrder.get("city") != null ? (String) sIOrder.get("city") : null);
				customerOrderDetail.setCustomerLeId(
						sIOrder.get("customerleId") != null ? (Integer) sIOrder.get("customerleId") : null);
				customerOrderDetail.setCustomerLeName(
						sIOrder.get("customerleName") != null ? (String) sIOrder.get("customerleName") : null);
				customerOrderDetail.setLatLong(sIOrder.get("latLong") != null ? (String) sIOrder.get("latLong") : null);
				customerOrderDetail.setProductName(
						sIOrder.get("productName") != null ? (String) sIOrder.get("productName") : null);
				customerOrderDetail
						.setServiceId(sIOrder.get("serviceId") != null ? (String) sIOrder.get("serviceId") : null);
				customerOrderDetail.setSiteAddress(
						sIOrder.get("siteAddress") != null ? (String) sIOrder.get("siteAddress") : null);
				//loop for productShort name
				for(ProductInformationBean result:productInformationBeansList) {
					if((sIOrder.get("productName")).equals(result.getProductName())) {
					customerOrderDetail.setProductShortName(result.getProductShortName());
				}
				}
				
				customerOrderDetail.setStatus(
						sIOrder.get("status") != null ? (String) sIOrder.get("status") : null);
				customerOrderDetail.setSiteAlias(
						sIOrder.get("siteAlias") != null ? (String) sIOrder.get("siteAlias") : null);
				if(customerOrderDetail.getProductName().equalsIgnoreCase(ServiceInventoryConstants.IZO_SDWAN) && customerOrderDetail.getStatus().equalsIgnoreCase(ServiceInventoryConstants.UNDER__PROVISIONING))
					sIOrdersIZOSDWANUnderProv.add(customerOrderDetail);
				sIOrders.add(customerOrderDetail);
			}

			if(!sIOrders.isEmpty() && !sIOrdersIZOSDWANUnderProv.isEmpty() )
				sIOrders.removeAll(sIOrdersIZOSDWANUnderProv);
		} catch (Exception e) {
			LOGGER.warn("Error in listing the CutomerOrder Details {}", ExceptionUtils.getCause(e));
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, e, ResponseResource.R_CODE_ERROR);
		}
		return sIOrders;
	}

	private String getLatLngDetails(String cityArg) throws TclCommonException {
		String geocodeReponse = "";
		String googleurl = "";
		String latLong = null;
		try {
			googleurl = org.apache.commons.lang3.StringUtils.join(googleMapAPI, "?key=", googleMapApiKey, "&address=",
					cityArg);

			RestResponse response = restClientService.get(googleurl);

			if (response.getStatus() == Status.SUCCESS) {
				geocodeReponse = response.getData();
			}

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(geocodeReponse);
			JSONObject jb = (JSONObject) obj;

			JSONArray jsonResultsObject = (JSONArray) jb.get("results");
			if (!jsonResultsObject.isEmpty()) {
				JSONObject jsonObject2 = (JSONObject) jsonResultsObject.get(0);
				JSONObject jsonObject3 = (JSONObject) jsonObject2.get("geometry");
				JSONObject location = (JSONObject) jsonObject3.get("location");

				latLong = location.get("lat") + "," + location.get("lng");
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}
		return latLong;
	}

	public Map<String, Long> getTrackOrderCount(Integer customerId) throws TclCommonException {
		Map<String, Long> orderCounts = new HashMap<>();
		List<String> orderStaus = new ArrayList<>();
		List<String> orderType = new ArrayList<>();

		try {
			;
			// getCustomerLeIds().forEach(customerLeId -> {
			/** getting change orders */
			orderStaus.add("Active");
			orderStaus.add("Change order in progress");
			orderType.add("TERMINATE");
			orderType.add("NEW");
			long changeOrder = siOrderRepository
					.countByErfCustLeIdInAndOrderStatusInAndOrderTypeNotIn(getCustomerLeIds(customerId), orderStaus, orderType);
			orderCounts.put("Change Orders", changeOrder);

			/** getting new orders */
			orderStaus.clear();
			orderType.clear();
			orderStaus.add("Active");
			orderType.add("NEW");
			long newOrder = siOrderRepository.countByErfCustLeIdInAndOrderStatusInAndOrderTypeIn(getCustomerLeIds(customerId),
					orderStaus, orderType);
			orderCounts.put("New Orders", newOrder);

			/** getting incomplete order from OMS */
			String response = (String) mqUtils.sendAndReceive(ordersCountQueue,
					StringUtils.join(getCustomerLeIds(customerId), ','));
			if (StringUtils.isNotBlank( response)) {
				long incompleteOrder = (Long) Utils.convertJsonToObject(response, Long.class);
				orderCounts.put("Incomplete Orders", incompleteOrder);
			}
			// });
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}
		return orderCounts;

	}

	private List<SIConfigurationCountryBean> getSiConfigurationCountryBeanForUIFN(List<SICountryBean> countries) {
		List<SIConfigurationCountryBean> siConfigurationCountryBeans = new ArrayList<>();

		Map<Integer, List<SICountryBean>> map = countries.stream()
				.collect(Collectors.groupingBy(SICountryBean::getOrderId));

		map.entrySet().stream().forEach(order -> {
			SIConfigurationCountryBean siConfigurationCountryBean = new SIConfigurationCountryBean();
			List<String> origin = new ArrayList<>();
			List<String> destination = new ArrayList<>();
			order.getValue().stream().forEach(siCountryBean -> {
				origin.add(siCountryBean.getOrigin());
				destination.add(siCountryBean.getDestination());
				siConfigurationCountryBean.setAccessType(siCountryBean.getAccessType());
				if (Objects.isNull(siConfigurationCountryBean.getNumbersCount())) {
					siConfigurationCountryBean.setNumbersCount(Integer.valueOf(siCountryBean.getNumbersCount()));
				} else {
					siConfigurationCountryBean.setNumbersCount(siConfigurationCountryBean.getNumbersCount()
							+ Integer.valueOf(siCountryBean.getNumbersCount()));
				}
			});
			siConfigurationCountryBean.setOrigin(origin.stream().distinct().collect(Collectors.toList()));
			siConfigurationCountryBean.setDestination(destination.stream().distinct().collect(Collectors.toList()));
			siConfigurationCountryBeans.add(siConfigurationCountryBean);
		});

		return siConfigurationCountryBeans;

	}

	/**
	 * Get Service details based on login user and product
	 *
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param vrfFlag
	 * @return SIServiceInformationBean
	 * @throws TclCommonException
	 */
	public SIServiceInformationBean getAllServiceDetailsByProduct(Integer productId, Integer page, Integer size,Integer customerId,Integer partnerId,Integer customerLeId
			,String vrfFlag, Boolean isTermination)
			throws TclCommonException {
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		List<Integer> serviceDetailIds = new ArrayList<>();
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		if(vrfFlag == null) {
			LOGGER.info("vrfFlag is passed as "+vrfFlag + "so changing the value of vrfFlag to No");
			vrfFlag = "No";
		}
		if(isTermination == null) {
			LOGGER.info("isTermination flag is null, assuming false");
			isTermination = Boolean.FALSE;
		}
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<CustomerLegalEntityDetails> customerLegalEntityDetails = new ArrayList<>();
		try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			//Added customerLeId selection for multicircuit
			if(Objects.nonNull(customerLeId))
			{
				customerIds.clear();
				customerIds.add(customerLeId);
			}

			LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<Map<String, Object>> data = null;
				if(vrfFlag.equalsIgnoreCase("Yes")) {
					LOGGER.info("vrfFlag is passed as "+vrfFlag + " retrieving the service IDs with Multi VRF Flag as Yes");
					if (page == -1) {
						data = siOrderRepository.getServiceDetailWithMvrf(customerIds, partnerLeIds, productId, customerId,
								partnerId,vrfFlag);
						// Multi Vrf Yes scenario
						LOGGER.info("vrfFlag is passed as "+vrfFlag + " retrieved list of serviceIds with Multi VRF Flag as Yes and of size "+ data.size());
					} else {
						page = (page - 1) * size;
						data = siOrderRepository.getServiceDetailWithMvrf(customerIds, partnerLeIds, productId, page, size,
								customerId, partnerId,vrfFlag);
						// Multi Vrf Yes scenario
						LOGGER.info("vrfFlag is passed as "+vrfFlag + " retrieved list of serviceIds with Multi VRF Flag as Yes and of size "+ data.size());
						Integer totalItems = siServiceDetailRepository.getServiceCountByProduct(productId, customerIds,
								partnerLeIds, customerId, partnerId);
						siServiceInformationBean.setTotalItems(totalItems);
						if (totalItems != null) {
							siServiceInformationBean.setTotalPages(totalItems / size);
						}
					}
				}else {
					LOGGER.info("vrfFlag is passed as "+vrfFlag + " retrieving the service IDs with Multi VRF Flag as No");
					if (page == -1) {
						data = siOrderRepository.getServiceDetail(customerIds, partnerLeIds, productId, customerId,
								partnerId);
					} else {
						page = (page - 1) * size;
						data = siOrderRepository.getServiceDetail(customerIds, partnerLeIds, productId, page, size,
								customerId, partnerId);
						Integer totalItems = siServiceDetailRepository.getServiceCountByProduct(productId, customerIds,
								partnerLeIds, customerId, partnerId);
						siServiceInformationBean.setTotalItems(totalItems);
						if (totalItems != null) {
							siServiceInformationBean.setTotalPages(totalItems / size);
						}
					}
				}
				if (data != null && !data.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					data.stream().forEach(map -> {
						serviceDetailBeans.add(mapper.convertValue(map, ServiceDetailBean.class));
					});
					List<Integer> sdwanSiSrvIds = new ArrayList<>();
					if(serviceDetailBeans.stream().findAny().get().getProductName().equalsIgnoreCase("IZO SDWAN")) {
						List<ServiceDetailBean> underProvServices = new ArrayList<>();
						serviceDetailBeans.stream().forEach(siDetail->{
							if(siDetail.getServiceStatus().equalsIgnoreCase("Under Provisioning")) {
								underProvServices.add(siDetail);
							}
						});
						serviceDetailBeans.removeAll(underProvServices);
						serviceDetailBeans.stream().forEach(siSrv->{
							sdwanSiSrvIds.add(siSrv.getSiServiceDetailId());
						});
						List<SIServiceAttribute> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSiSrvIds,
								Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME));
						serviceDetailBeans.stream().forEach(si->{
							serviceAttributes.stream().
							filter(siAttr-> siAttr.getSiServiceDetail().getId().equals(si.getSiServiceDetailId()))
							.forEach(siAttr->{
								si.setOrganisationName(siAttr.getAttributeValue());
							});
						});
					}
					
					if(serviceDetailBeans.stream().findAny().get().getProductName().equalsIgnoreCase("IZOPC")) {
						serviceDetailBeans.stream().forEach(si->{
							
							List<Map<String, Object>> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetailIdAndAttributeName(si.getSiServiceDetailId(), "Cloud Provider");
							LOGGER.info("Response serviceAttributes {}",serviceAttributes);
							serviceAttributes.stream().forEach(attr->{
								si.setCloudProvider((String) attr.get("attribute_value"));							
								
							});
							
						});
					}
				}
				// added for multivrf macd set master and slave info
				if (!serviceDetailBeans.isEmpty()) {
					LOGGER.info("MULTI VRF MACD before set master and slave info"
							+ serviceDetailBeans.get(0).getProductName());
					if (serviceDetailBeans.get(0).getProductName() != null) {
						if(serviceDetailBeans.get(0).getProductName().equalsIgnoreCase("GVPN")) {
							constructMultiVrfMasterAndSlaveInfo(serviceDetailBeans);
						}
					}
					String productName=serviceDetailBeans.get(0).getProductName();
					if(productName!=null&&productName.equals(CommonConstants.NPL)){
						serviceDetailBeans.stream().forEach(siSrv->{
							Optional<SIServiceAdditionalInfo> siServAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(siSrv.getSiServiceDetailId(), "Cross Connect Type");
							if(siServAddInf.isPresent()){
								siSrv.setCrossConnectType(siServAddInf.get().getAttributeValue());
							}
						});

					}
				}


				siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
				siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId));
				siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId));
				siServiceInformationBean.getAlias().removeIf(Objects::isNull);
				List<Map<String, Object>> leData=null;
				if(Boolean.TRUE.equals(isTermination)) {
					 leData = siServiceDetailRepository.getDistinctLeDetailsByProductForInactiveCircuits(productId,
							customerIds, partnerLeIds, customerId, partnerId);
				} else {
				leData = siServiceDetailRepository.getDistinctLeDetailsByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId);
				}
				LOGGER.info("leData {}", leData);

				if (leData != null && !leData.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					leData.stream().forEach(map -> {
						customerLegalEntityDetails.add(mapper.convertValue(map, CustomerLegalEntityDetails.class));
					});
				}
				siServiceInformationBean.setCustomerLegalEntityDetails(customerLegalEntityDetails);
				siServiceInformationBean.getServiceDetailBeans().stream().
				forEach(map->{
					serviceDetailIds.add(map.getSiServiceDetailId());
				});
				
			/*
				 * Method Call -> to fetch Customer details from Customer table in OMS by ERF_id
				 * in service inventory Map<Integer,List<OmsCustomerBean>> map =
				 * getCustomerId(customerIds); serviceDetailBeans.forEach(detailBean->{
				 * detailBean.setCustomerId(map.get(detailBean.getCustomerId()).stream().
				 * findFirst().get().getCustomerId().toString()); });
				 */

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siServiceInformationBean;
	}
	
	/**
	 * Get Service details based on login user and product
	 *
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceInformationBean getAllServiceDetailsByProductWithNdeFilter(Integer productId, Integer page, Integer size,Integer customerId,Integer partnerId,Integer customerLeId,boolean ndeFlag)
			throws TclCommonException {
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<CustomerLegalEntityDetails> customerLegalEntityDetails = new ArrayList<>();
		try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			//Added customerLeId selection for multicircuit
			if(Objects.nonNull(customerLeId))
			{
				customerIds.clear();
				customerIds.add(customerLeId);
			}

			LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<Map<String, Object>> data = null;
				if (page == -1) {
					data = siOrderRepository.getServiceDetail(customerIds, partnerLeIds, productId, customerId,
							partnerId);
				} else {
					page = (page - 1) * size;
					data = siOrderRepository.getServiceDetail(customerIds, partnerLeIds, productId, page, size,
							customerId, partnerId);
					Integer totalItems = siServiceDetailRepository.getServiceCountByProduct(productId, customerIds,
							partnerLeIds, customerId, partnerId);
					siServiceInformationBean.setTotalItems(totalItems);
					if (totalItems != null) {
						siServiceInformationBean.setTotalPages(totalItems / size);
					}
				}
				
				if (data != null && !data.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					data.stream().forEach(map -> {
						serviceDetailBeans.add(mapper.convertValue(map, ServiceDetailBean.class));
					});
					siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				}
				
				if (data != null && !data.isEmpty() && ndeFlag == true) {
					String attributeValue = "Hub National Dedicated Ethernet";
					for(int index=0;index<serviceDetailBeans.size();index++) {
						int serviceDetailId = serviceDetailBeans.get(index).getSiServiceDetailId();
						SIServiceAttribute ndeStatus = siServiceAttributeRepository.findBySiServiceDetailId(serviceDetailId, attributeValue);
						if(!ndeStatus.getAttributeValue().equals(attributeValue)) {
							serviceDetailBeans.remove(index);
						}
					}
					siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				}
				
				if (data != null && !data.isEmpty()&& ndeFlag == false) {
					String attributeValue = "Normal";
					for(int index=0;index<serviceDetailBeans.size();index++) {
						int serviceDetailId = serviceDetailBeans.get(index).getSiServiceDetailId();
						SIServiceAttribute ndeStatus = siServiceAttributeRepository.findBySiServiceDetailId(serviceDetailId, attributeValue);
						if(!ndeStatus.getAttributeValue().equals(attributeValue)) {
							serviceDetailBeans.remove(index);
						}
					}
					siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				}
				//siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
				siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId));
				siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId));
				siServiceInformationBean.getAlias().removeIf(Objects::isNull);
				List<Map<String, Object>> leData = siServiceDetailRepository.getDistinctLeDetailsByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId);
				LOGGER.info("leData {}", leData);

				if (leData != null && !leData.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					leData.stream().forEach(map -> {
						customerLegalEntityDetails.add(mapper.convertValue(map, CustomerLegalEntityDetails.class));
					});
				}
				siServiceInformationBean.setCustomerLegalEntityDetails(customerLegalEntityDetails);
				
			/*
				 * Method Call -> to fetch Customer details from Customer table in OMS by ERF_id
				 * in service inventory Map<Integer,List<OmsCustomerBean>> map =
				 * getCustomerId(customerIds); serviceDetailBeans.forEach(detailBean->{
				 * detailBean.setCustomerId(map.get(detailBean.getCustomerId()).stream().
				 * findFirst().get().getCustomerId().toString()); });
				 */

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siServiceInformationBean;
	}


	public SIServiceInformationBean getAllServiceDetailsByProductForPartner(Integer productId, Integer page, Integer size,Integer customerId,Integer partnerId,Integer customerLeId,Integer partnerLeId)
			throws TclCommonException {
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<CustomerLegalEntityDetails> customerLegalEntityDetails = new ArrayList<>();
		try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				if(partnerLeId!=null){
					partnerLeIds.add(partnerLeId);
				}
				else {
					partnerLeIds = new ArrayList<>(getPartnerLeIds());
				}
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			//Added customerLeId selection for multicircuit
			if(Objects.nonNull(customerLeId))
			{
				customerIds=new ArrayList<>();
				customerIds.add(customerLeId);
			}

			LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<Map<String, Object>> data = null;
				if (page == -1) {
					data = siOrderRepository.getServiceDetailForPartner(customerIds, partnerLeIds, productId, customerId,
							partnerId);
				} else {
					page = (page - 1) * size;
					data = siOrderRepository.getServiceDetailForPartner(customerIds, partnerLeIds, productId, page, size,
							customerId, partnerId);
					Integer totalItems = siServiceDetailRepository.getServiceCountByProduct(productId, customerIds,
							partnerLeIds, customerId, partnerId);
					siServiceInformationBean.setTotalItems(totalItems);
					if (totalItems != null) {
						siServiceInformationBean.setTotalPages(totalItems / size);
					}
				}

				if (data != null && !data.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					data.stream().forEach(map -> {
						serviceDetailBeans.add(mapper.convertValue(map, ServiceDetailBean.class));
					});
				}
				siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
				siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId));
				siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId));
				siServiceInformationBean.getAlias().removeIf(Objects::isNull);
				List<Map<String, Object>> leData = siServiceDetailRepository.getDistinctLeDetailsByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId);
				LOGGER.info("leData {}", leData);

				if (leData != null && !leData.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					leData.stream().forEach(map -> {
						customerLegalEntityDetails.add(mapper.convertValue(map, CustomerLegalEntityDetails.class));
					});
				}
				siServiceInformationBean.setCustomerLegalEntityDetails(customerLegalEntityDetails);

				/*
				 * Method Call -> to fetch Customer details from Customer table in OMS by ERF_id
				 * in service inventory Map<Integer,List<OmsCustomerBean>> map =
				 * getCustomerId(customerIds); serviceDetailBeans.forEach(detailBean->{
				 * detailBean.setCustomerId(map.get(detailBean.getCustomerId()).stream().
				 * findFirst().get().getCustomerId().toString()); });
				 */

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siServiceInformationBean;
	}
	/**
	 * Get Service details based on productsegment, login user and product
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public CloudServiceInformationBean getServiceDetailsByProductSegmentsProduct(Integer productId, Integer page, Integer size, String city, String cloudType,
			String businessUnit, String zone, String opptyClassification, String partnerLeName, String searchText) throws TclCommonException {
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		CloudServiceInformationBean cloudServiceInformationBean = new CloudServiceInformationBean();
		try {
			List<Integer> customerLeIds = new ArrayList<>(getCustomerLeIds());
			List<Integer> partnerLeIds = new ArrayList<>(getPartnerLeIds());
			if (!customerLeIds.isEmpty() || !partnerLeIds.isEmpty()) {

				Set<String> cityList = new TreeSet<>();
				Set<String> businessUnitList = new TreeSet<>();
				Set<String> zoneList = new TreeSet<>();

				if(!customerLeIds.isEmpty()) {
					cityList.addAll(vwOrderServiceAssetInfoRepository.getDistinctCityBasedOnProductIdAndCustomerLeIds(customerLeIds, productId));
					businessUnitList.addAll(vwOrderServiceAssetInfoRepository.getDistinctBusinessUnitBasedOnProductIdAndCustomerLeIds(customerLeIds, productId));
					zoneList.addAll(vwOrderServiceAssetInfoRepository.getDistinctZoneBasedOnProductIdAndCustomerLeIds(customerLeIds, productId));
				}

				if(!partnerLeIds.isEmpty()) {
					cityList.addAll(vwOrderServiceAssetInfoRepository.getDistinctCityBasedOnProductIdAndPartnerLeIds(partnerLeIds, productId));
					businessUnitList.addAll(vwOrderServiceAssetInfoRepository.getDistinctBusinessUnitBasedOnProductIdAndPartnerLeIds(partnerLeIds, productId));
					zoneList.addAll(vwOrderServiceAssetInfoRepository.getDistinctZoneBasedOnProductIdAndPartnerLeIds(partnerLeIds, productId));
					cloudServiceInformationBean.setPartnerLeList(vwOrderServiceAssetInfoRepository.getDistinctPartnerNamesBasedOnProductIdAndPartnerLeIds(partnerLeIds, productId));
				}

				cloudServiceInformationBean.setCityList(new ArrayList<String>(cityList));
				cloudServiceInformationBean.setBusinessUnitList(new ArrayList<String>(businessUnitList));
				cloudServiceInformationBean.setZoneList(new ArrayList<String>(zoneList));

				Set<String> additionalServiceIds = new HashSet<>();

				if(("All").equals(cloudType) && !StringUtils.isAllBlank(searchText)) {

					// Search for Corresponding DC Service IDs
					Set<String> tpsServiceIds = new HashSet<>();
					tpsServiceIds.add(searchText);
					List<Map<String, Object>> dcServiceDetails = siServiceAttributeRepository.findByTpsServiceIdAndAttributeName(tpsServiceIds, "DC_SERVICE_ID");
					for(Map<String, Object> dcServiceDetail : dcServiceDetails) {
						additionalServiceIds.add(String.valueOf(dcServiceDetail.get("attribute_value")));
					}

					// Search for Corresponding DR Service IDs
					List<String> drServiceDetails = siServiceAttributeRepository.findTpsServiceIdByAttrNameAndAttrValue("DC_SERVICE_ID", searchText);
					for(String drServiceDetail : drServiceDetails) {
						additionalServiceIds.add(drServiceDetail);
					}
				}

				Specification<VwOrderServiceAssetInfo> specOrderTotalCount = vwOrderServiceAssetDetailSpecification.getOrders(city, cloudType, businessUnit, zone, opptyClassification, partnerLeName, searchText, additionalServiceIds, customerLeIds, partnerLeIds, productId);
				List<VwOrderServiceAssetInfo> orderServiceAssetList = vwOrderServiceAssetInfoRepository.findAll(specOrderTotalCount);

				if(Objects.nonNull(orderServiceAssetList) && !orderServiceAssetList.isEmpty()) {
					int totalItems = orderServiceAssetList.size();
					cloudServiceInformationBean.setTotalItems(totalItems);
					cloudServiceInformationBean.setTotalPages(totalItems / size);

					List<VwOrderServiceAssetInfo> specOrderServicePaginationList = getOrderDetailsBasedOnPagination(city, cloudType, businessUnit, zone, opptyClassification, partnerLeName, searchText, additionalServiceIds, customerLeIds, partnerLeIds, productId, page, size);

					Map<Integer,List<Integer>> orderAssetMap = new HashMap<>();
					Set<String> serviceIdSet = new HashSet<>();
					List<Integer> assetIdSet = new ArrayList<>();
					Map<Integer,List<SIAsset>> orderAssetDetailMap = new HashMap<>();
					List<IPCInformationBean> ipcInfoList= new ArrayList<>();

					List<VwOrderServiceAssetInfo> specOrderServiceAssetDetailList = getAssetDetailsBasedOnBaseParams(city, cloudType, businessUnit, zone, opptyClassification, partnerLeName, searchText, additionalServiceIds, customerLeIds, partnerLeIds, productId, orderAssetMap, assetIdSet, serviceIdSet);

					getAssetDetailsBasedOnServiceIdAndType(businessUnit,zone,orderAssetMap,assetIdSet,serviceIdSet);

					Map<String,Object> ipcMacd = getIPCMacdFlags(String.join(",", serviceIdSet));

					getAssetDetailsBasedOnAssetIdAndGroupByOrder(assetIdSet, orderAssetMap, orderAssetDetailMap);

					getOrderDetails(specOrderServicePaginationList, specOrderServiceAssetDetailList, cloudServiceInformationBean, ipcInfoList, orderAssetDetailMap, ipcMacd);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return cloudServiceInformationBean;
	}
	
	
	private List<VwOrderServiceAssetInfo> getOrderDetailsBasedOnPagination(String city, String cloudType, String businessUnit, String zone,
			String opptyClassification, String partnerLeName, String serviceId, Set<String> additionalServiceIds,
			List<Integer> customerLeIds, List<Integer> partnerLeIds, Integer productId, int page, int size) {
		Specification<VwOrderServiceAssetInfo> specOrderPagination = vwOrderServiceAssetDetailSpecification.getOrderBasedAssetDetails(city, cloudType,
				businessUnit, zone, opptyClassification, partnerLeName,
				serviceId, additionalServiceIds, customerLeIds, partnerLeIds, productId, true);
		Page<VwOrderServiceAssetInfo> specOrderServicePagination = vwOrderServiceAssetInfoRepository.findAll(specOrderPagination, PageRequest.of(page - 1, size));
		return specOrderServicePagination.getContent();
	}

	private void getAssetDetailsBasedOnAssetIdAndGroupByOrder(List<Integer> assetIdSet,Map<Integer,List<Integer>> orderAssetMap,Map<Integer,List<SIAsset>> orderAssetDetailMap) {
		List<SIAsset> siAssetList=siAssetRepository.findByIdIn(assetIdSet);
		orderAssetMap.forEach((orderId,assetIds)->{
				siAssetList.stream().forEach(siAsset->{
				List<SIAsset> matchedSIAssetList = new ArrayList<>();
				if(assetIds.contains(siAsset.getId())){
					if(orderAssetDetailMap.containsKey(orderId)){
						orderAssetDetailMap.get(orderId).add(siAsset);
					}else{
						matchedSIAssetList.add(siAsset);
						orderAssetDetailMap.put(orderId, matchedSIAssetList);
					}
				}
			});
		});
	}

	private List<VwOrderServiceAssetInfo> getAssetDetailsBasedOnBaseParams(String city, String cloudType, String businessUnit, String zone,
			String opptyClassification, String partnerLeName, String serviceId, Set<String> additionalServiceIds,
			List<Integer> customerLeIds, List<Integer> partnerLeIds, Integer productId,
			Map<Integer,List<Integer>> orderAssetMap, List<Integer> assetIdSet, Set<String> serviceIdSet) {
		Specification<VwOrderServiceAssetInfo> specOrderServiceAssetDetail = vwOrderServiceAssetDetailSpecification.getOrderBasedAssetDetails(city, cloudType,
				businessUnit, zone, opptyClassification, partnerLeName, serviceId, additionalServiceIds, customerLeIds, partnerLeIds, productId, false);
		List<VwOrderServiceAssetInfo> specOrderServiceAssetDetailList = vwOrderServiceAssetInfoRepository.findAll(specOrderServiceAssetDetail);
		specOrderServiceAssetDetailList.stream().forEach(vwOrderServiceAssetInfo ->{
			List<Integer> assetIdList = new ArrayList<>();
			if(orderAssetMap.containsKey(vwOrderServiceAssetInfo.getOrderId())) {
				orderAssetMap.get(vwOrderServiceAssetInfo.getOrderId()).add(vwOrderServiceAssetInfo.getAssetId());
			} else {
				assetIdList.add(vwOrderServiceAssetInfo.getAssetId());
				orderAssetMap.put(vwOrderServiceAssetInfo.getOrderId(), assetIdList);
			}
			assetIdSet.add(vwOrderServiceAssetInfo.getAssetId());
			serviceIdSet.add(vwOrderServiceAssetInfo.getServiceId());
		});
		return specOrderServiceAssetDetailList;
	}

	private void getAssetDetailsBasedOnServiceIdAndType(String businessUnit,String zone,Map<Integer,List<Integer>> orderAssetMap,List<Integer> assetIdSet,Set<String> serviceIdSet) {
		if(!StringUtils.isAllBlank(businessUnit)&&!businessUnit.equalsIgnoreCase("All") || !StringUtils.isAllBlank(zone)&&!zone.equalsIgnoreCase("All")) {
			Set<String> typeSet =ImmutableSet.of("ACCESS","ADDON");
			Specification<VwOrderServiceAssetInfo> specServiceAssetDetails =vwOrderServiceAssetDetailSpecification.getServiceBasedAssetDetails(serviceIdSet, typeSet);
			List<VwOrderServiceAssetInfo> specServiceAssetDetailList = vwOrderServiceAssetInfoRepository.findAll(specServiceAssetDetails);
			specServiceAssetDetailList.stream().forEach(vwOrderServiceAssetInfo ->{
				List<Integer> assetIdList = new ArrayList<>();
				if(orderAssetMap.containsKey(vwOrderServiceAssetInfo.getOrderId())){
					orderAssetMap.get(vwOrderServiceAssetInfo.getOrderId()).add(vwOrderServiceAssetInfo.getAssetId());
				}else{
					orderAssetMap.put(vwOrderServiceAssetInfo.getOrderId(), assetIdList);
				}
				assetIdSet.add(vwOrderServiceAssetInfo.getAssetId());
			});
        }
	}

	private void getOrderDetails(List<VwOrderServiceAssetInfo> specOrderServicePaginationList,List<VwOrderServiceAssetInfo> specOrderServiceAssetDetailList,CloudServiceInformationBean cloudServiceInformationBean,List<IPCInformationBean> ipcInfoList,Map<Integer,List<SIAsset>> orderAssetDetailMap,Map<String,Object> ipcMacd) {
		specOrderServicePaginationList.stream().forEach(vwOrder ->{
			IPCInformationBean ipcInformationBean = new IPCInformationBean();
			ipcInformationBean.setOrderId(vwOrder.getOrderId());
			ipcInformationBean.setLocation(vwOrder.getCity());
			ipcInformationBean.setServiceId(vwOrder.getServiceId());
			ipcInformationBean.setServiceCreatedDate(vwOrder.getServiceCreatedDate());
			ipcInformationBean.setSiteType(vwOrder.getSiteType());
			ipcInformationBean.setIsMacdInitiated(null!=ipcMacd.get(vwOrder.getServiceId())?(Boolean)ipcMacd.get(vwOrder.getServiceId()):false);
			specOrderServiceAssetDetailList.stream()
				.filter( vwOrderServiceAssetInfo -> vwOrder.getOrderId().equals(vwOrderServiceAssetInfo.getOrderId()))
				.findFirst().ifPresent(vwOrderServiceAssetInfo -> {
					List<SIAsset> siAssetList =orderAssetDetailMap.get(vwOrderServiceAssetInfo.getOrderId());
					List<AssetDetailBean> vmDetailList= new ArrayList<>();
					List<AssetDetailBean> accessDetailList= new ArrayList<>();
					List<AssetDetailBean> addOnDetailList= new ArrayList<>();
					getAssetDetails(siAssetList,vmDetailList,accessDetailList,addOnDetailList);
					ipcInformationBean.setVmDetailList(vmDetailList);
					ipcInformationBean.setAccessDetailList(accessDetailList);
					ipcInformationBean.setAddOnDetailList(addOnDetailList);
					ipcInfoList.add(ipcInformationBean);
					cloudServiceInformationBean.setIpcInfoList(ipcInfoList);
				});
		});
	}

	private void getAssetDetails(List<SIAsset> siAssetList, List<AssetDetailBean> vmDetailList,List<AssetDetailBean> accessDetailList, List<AssetDetailBean> addOnDetailList) {
		siAssetList.stream().forEach(siAsset ->{
			AssetDetailBean assetDetailBean = new AssetDetailBean();
			getAssetBeanDetails(assetDetailBean,siAsset);
			Map<String,String> assetAttrMap =new HashMap<>();
			siAsset.getSiAssetAttributes().stream().forEach(siAssetAttr -> {
				assetAttrMap.put(siAssetAttr.getAttributeName(), siAssetAttr.getAttributeValue());
			});
			assetDetailBean.setAttrValues(assetAttrMap);
			getAssetList(siAsset,assetDetailBean,vmDetailList,accessDetailList,addOnDetailList);
		});
	}

	private void getAssetBeanDetails(AssetDetailBean assetDetailBean,SIAsset siAsset) {
		assetDetailBean.setAssetId(siAsset.getId());
		assetDetailBean.setAssetName(siAsset.getName());
		assetDetailBean.setBusinessUnit(siAsset.getBusinessUnit());
		assetDetailBean.setZone(siAsset.getZone());
		if(Objects.nonNull(siAsset.getCloudCode())){
			assetDetailBean.setCloudCode(siAsset.getCloudCode());
		}
		if(Objects.nonNull(siAsset.getParentCloudCode())){
			assetDetailBean.setParentCloudCode(siAsset.getParentCloudCode());
		}
		
	}

	/**
	 * Get Service Info
	 * @param productId and serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public IPCInformationBean getServiceInfoDetailByProductSegmentsProduct(Integer productId,String serviceId,String assetId) throws TclCommonException {
		if (productId == null || serviceId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}else{
			IPCInformationBean ipcInformationBean = new IPCInformationBean();
			Map<String,Object> serviceInfo=siServiceDetailRepository.getServiceInfoBasedOnServiceIdAndProductId(serviceId, productId);
			if(Objects.nonNull(serviceInfo) && !serviceInfo.isEmpty()) {
				ServiceDetailBean serviceDetailBean = new ObjectMapper().convertValue(serviceInfo, ServiceDetailBean.class);
				Map<String,Object> ipcMacd=getIPCMacdFlags(serviceId);
				serviceDetailBean.setIsMacdInitiated(ipcMacd.get(serviceId)!=null?(Boolean)ipcMacd.get(serviceId):false);
				ipcInformationBean.setServiceDetailBean(serviceDetailBean);
				ipcInformationBean.setSiteType(serviceDetailBean.getSiteType());
				List<Integer> assetIdList= new ArrayList<>();
				if (Objects.nonNull(serviceDetailBean.getAssetId()) && StringUtils.isNotBlank(serviceDetailBean.getAssetId())) {
					checkAssetIdExists(assetId,serviceDetailBean);
					parseAsset(serviceDetailBean.getAssetId(),assetIdList);
					List<SIAsset> siAssetList=siAssetRepository.findByIdIn(assetIdList);
					List<AssetDetailBean> vmDetailList= new ArrayList<>();
					List<AssetDetailBean> accessDetailList= new ArrayList<>();
					List<AssetDetailBean> addOnDetailList= new ArrayList<>();
					getAssetDetailsByComponentGrouping(siAssetList,vmDetailList,accessDetailList,addOnDetailList);
					ipcInformationBean.setVmDetailList(vmDetailList);
					ipcInformationBean.setAccessDetailList(accessDetailList);
					ipcInformationBean.setAddOnDetailList(addOnDetailList);
				}
			}
			return ipcInformationBean;
		}
	}	
	
	private void checkAssetIdExists(String assetId, ServiceDetailBean serviceDetailBean) {
		if (Objects.nonNull(assetId) && StringUtils.isNotBlank(assetId)) {
			serviceDetailBean.setAssetId(assetId);
		}
	}

	private void getAssetDetailsByComponentGrouping(List<SIAsset> siAssetList, List<AssetDetailBean> vmDetailList,
			List<AssetDetailBean> accessDetailList, List<AssetDetailBean> addOnDetailList) {
		for(SIAsset siAsset:siAssetList){
			AssetDetailBean assetDetailBean = new AssetDetailBean();
			getAssetBeanDetails(assetDetailBean,siAsset);
			List<ComponentBean> componentBeanList = new ArrayList<>();
			Map<String,AssetDetailBean> componentBeanMap = new HashMap<>();
			getAssetBasedComponents(siAsset,assetDetailBean,componentBeanMap,componentBeanList);
			getAssetList(siAsset,assetDetailBean,vmDetailList,accessDetailList,addOnDetailList);
		}
		
	}

	private void getAssetList(SIAsset siAsset, AssetDetailBean assetDetailBean, List<AssetDetailBean> vmDetailList,
			List<AssetDetailBean> accessDetailList, List<AssetDetailBean> addOnDetailList) {
		if(siAsset.getType().equals("CLOUD")){
			vmDetailList.add(assetDetailBean);
		}else if(siAsset.getType().equals("ACCESS")){
			accessDetailList.add(assetDetailBean);
		}else if(siAsset.getType().equals("ADDON")){
			addOnDetailList.add(assetDetailBean);
		}
	}

	private void getAssetBasedComponents(SIAsset siAsset, AssetDetailBean assetDetailBean,Map<String,AssetDetailBean> componentBeanMap,List<ComponentBean> componentBeanList) {
		siAsset.getSiAssetAttributes().forEach(siAssetAttr -> {
			if(null!=componentBeanMap.get(siAssetAttr.getCategory())){		
				AssetDetailBean existingBean=componentBeanMap.get(siAssetAttr.getCategory());
				existingBean.getComponentBeanList().forEach(cb->{
					if(siAssetAttr.getCategory().equals(cb.getName())){
						AttributeDetail attrDetail = new AttributeDetail();
						attrDetail.setName(siAssetAttr.getAttributeName());
						attrDetail.setValue(siAssetAttr.getAttributeValue());
						cb.getAttributes().add(attrDetail);
					}
				});
			}else{
				ComponentBean componentBean = new ComponentBean();
				componentBean.setName(siAssetAttr.getCategory());
				AttributeDetail attrDetail = new AttributeDetail();
				attrDetail.setName(siAssetAttr.getAttributeName());
				attrDetail.setValue(siAssetAttr.getAttributeValue());
				List<AttributeDetail> attrDetailList= new ArrayList<>();
				attrDetailList.add(attrDetail);
				componentBean.setAttributes(attrDetailList);
				componentBeanList.add(componentBean);
				assetDetailBean.setComponentBeanList(componentBeanList);
				componentBeanMap.put(siAssetAttr.getCategory(), assetDetailBean);
			}
		});
	}

	private List<String> splitStringByComma(String input){
		return (List)Arrays.asList(input.split(ServiceInventoryConstants.SPLIT_BY_COMMA));
	}		

	private void parseAsset(String assetId, List<Integer> assetIdList) {
		List<String> assets=splitStringByComma(assetId);
		assetIdList.addAll((List<Integer>)Lists.transform(assets, Integer::parseInt));
	}
	
	/**
	 * Method to get IPC macd details from service Id
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private Map<String,Object> getIPCMacdFlags(String serviceIds) throws TclCommonException, IllegalArgumentException {
		Map<String, Object> macdFlags =new HashMap<>();
		if(Objects.nonNull(serviceIds)) {
			LOGGER.debug("IPCMacdRequest" + serviceIds);
			String response = (String) mqUtils.sendAndReceive(macdIPCQueue, serviceIds);
			LOGGER.debug("IPCMacdResponse" + response);
			if (Objects.nonNull(response) && StringUtils.isNotBlank(response)) {
				macdFlags = (Map<String, Object>) Utils.convertJsonToObject(response, Map.class);
			}
		}
		return macdFlags;
	}
	
	/**
	 * Method to set IpAddress provided
	 * @param serviceDetails
	 * @return
	 */
	private List<ServiceDetailBean> setIpAddressProvided(List<ServiceDetailBean> serviceDetails)
	{
		List<ServiceDetailBean> serviceDetailBeans=new ArrayList<>();
		if(Objects.nonNull(serviceDetails)&&!serviceDetails.isEmpty()) {
            for (ServiceDetailBean serviceDetailBean : serviceDetails) {

                List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository.getAssetDetailWithAttributes(serviceDetailBean.getSiServiceDetailId());
                Optional<Map<String, Object>> assetDetailInfo = assetDetailInfos.stream().findFirst();
                Map<String, Object> assetDetail = new HashMap<>();
                if (assetDetailInfo.isPresent()) {
                    assetDetail = assetDetailInfo.get();
                    String ipAddressProvided = (String) assetDetail.get("ip_address_provided");
                    serviceDetailBean.setIpAddressProvidedBy(ipAddressProvided);
                }
                serviceDetailBeans.add(serviceDetailBean);
            }
        }
		return serviceDetailBeans;

	}
	
	
	public SIServiceInformationBean getAllServiceDetails() throws TclCommonException {
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		try {
			List<Integer> customerIds = new ArrayList<>(getCustomerLeIds());
			if (!customerIds.isEmpty()) {
				List<Map<String, Object>> data = siOrderRepository.getServiceDetail(customerIds);
				if (data != null && !data.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					data.stream().forEach(map -> {
						serviceDetailBeans.add(mapper.convertValue(map, ServiceDetailBean.class));
					});
				}
				siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByLeId(customerIds));
				siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByLeId(customerIds));
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siServiceInformationBean;
	}

	/**
	 * Get product wise count for service
	 *
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	public List<ProductInformationBean> getAllProductServiceInformation(Integer customerId,Integer partnerId)
			throws TclCommonException, IllegalArgumentException {
		List<ProductInformationBean> response = new ArrayList<>();
		List<ProductInformationBean> responseFromEngagement = new ArrayList<>();
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		try {
			String queueResponse = (String) mqUtils.sendAndReceive(productDetailsQueue, null);
			String queueResponseFromAuth=(String)mqUtils.sendAndReceive(getEngagementDetails, null);
			LOGGER.info("queueResponse :"+ queueResponse);
			LOGGER.info("queueResponseFromAuth :"+ queueResponseFromAuth);
			List<Map<String, Object>> productInformationBeans = (List<Map<String, Object>>) Utils
					.convertJsonToObject(queueResponse, List.class);
			List<Map<String, Object>> engagementBeans=new ArrayList<Map<String,Object>>();
			if(Objects.nonNull(queueResponseFromAuth) && !queueResponseFromAuth.isEmpty())
				engagementBeans = (List<Map<String, Object>>) Utils
					.convertJsonToObject(queueResponseFromAuth, List.class);
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(partnerId!=null?getPartnerLeIds(partnerId):getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(customerId!=null?getCustomerLeIdsByCustomerId(customerId):getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			

			final List<Integer> finalPartnerLeIds = partnerLeIds;
			final List<Integer> finalCustomerIds = customerIds;
			LOGGER.info("Customer ID is ----> {} "+ customerIds.toString());
			LOGGER.info("Partner ID is ----> {} "+ partnerLeIds.toString());
			if(Objects.nonNull(engagementBeans) && !engagementBeans.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				engagementBeans.stream().forEach(map->{
					UserProductsBean userProductsBean = mapper.convertValue(map,
							UserProductsBean.class);
					if(ServiceInventoryConstants.MHS.equalsIgnoreCase(userProductsBean.getProductName()) 
							|| ServiceInventoryConstants.MSS.equalsIgnoreCase(userProductsBean.getProductName())) {
						ProductInformationBean productInformationBean =new ProductInformationBean();
						productInformationBean.setProductId(userProductsBean.getProductId());
						productInformationBean.setProductName(userProductsBean.getProductName());
						LOGGER.info("engagementBeans product Name res from queue  ------ {}"+ userProductsBean.getProductName());
						responseFromEngagement.add(productInformationBean);
					}
						
				});
			}
			LOGGER.info("getAllProductServiceInformation - responseFromEngagement{} :"+ responseFromEngagement.toString());
			if (productInformationBeans != null && (customerIds != null && !customerIds.isEmpty() || partnerLeIds!=null && !partnerLeIds.isEmpty())) {
				final ObjectMapper mapper = new ObjectMapper();
				productInformationBeans.stream().forEach(map -> {
					ProductInformationBean productInformationBean = mapper.convertValue(map,
							ProductInformationBean.class);
					Integer count = 0;
					LOGGER.info("view Spec start for Customer: {} or Partner: {} ", finalCustomerIds, finalPartnerLeIds);
					LOGGER.info("Initiating productName ==================> {}",productInformationBean.getProductName());
					if("IZO Private Cloud".equals(productInformationBean.getProductName()) || "IPC".equals(productInformationBean.getProductName())){
						//Specification<VwOrderServiceAssetInfo> specOrderTotalCount = vwOrderServiceAssetDetailSpecification.getOrders(null, null, null, null, null, null, null, null, finalCustomerIds, finalPartnerLeIds, productInformationBean.getProductId());
						//List<VwOrderServiceAssetInfo> orderServiceAssetList = vwOrderServiceAssetInfoRepository.findAll(specOrderTotalCount);
						//count = orderServiceAssetList.size();
						LOGGER.info("view Spec start for ipc {}, {}, {}", finalCustomerIds, finalPartnerLeIds, productInformationBean.getProductId());
						count = siServiceDetailRepository.getServiceCountByStandaloneProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						LOGGER.info("Processing productName {} : count {}",productInformationBean.getProductName(),count);
					} else if("IAS".equals(productInformationBean.getProductName()) || "GVPN".equals(productInformationBean.getProductName()) || 
							"IZO Internet WAN".equals(productInformationBean.getProductName()) || "DIA".equals(productInformationBean.getProductName()) ||
							"BYON Internet".equals(productInformationBean.getProductName()) || "BYON MPLS".equals(productInformationBean.getProductName()) || "IZOPC".equals(productInformationBean.getProductName()) || "IZO Private Connect".equals(productInformationBean.getProductName())){
						LOGGER.info("Processing productName {}",productInformationBean.getProductName());
						 count = siServiceDetailRepository.getServiceCountByStandaloneProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						 LOGGER.info("Processing productName {} : count {}",productInformationBean.getProductName(),count);
						 LOGGER.info("view Spec start for ias, gvpn {} ,{},{},{},{}", finalCustomerIds,finalPartnerLeIds,productInformationBean.getProductId(),customerId,partnerId);
						 LOGGER.info("Product Count for GVPN DIA BYON  count {}, -----------------productName {}", count,productInformationBean.getProductName());
					} else if("IZO SDWAN".equals(productInformationBean.getProductName())){
						 count = siServiceDetailRepository.getServiceCountByStandaloneIzoSdwan(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						 LOGGER.info("view Spec start for izosdwan count {}", count);
						 if(count != 0) {
							 Integer networkProductCounts = siServiceDetailRepository.getNetworkServiceCount(Arrays.asList("IAS", "GVPN","IZO Internet WAN","DIA","BYON Internet","BYON MPLS"), finalCustomerIds, finalPartnerLeIds,customerId,partnerId, productInformationBean.getProductId());
							 productInformationBean.setNetworkProductCounts(networkProductCounts);
						 }
						 LOGGER.info("view Spec start for izosdwan 2 {} ,{},{},{},{}", finalCustomerIds, finalPartnerLeIds,customerId,partnerId, productInformationBean.getProductId());
					} else if("GDE".equals(productInformationBean.getProductName()) || "NDE".equals(productInformationBean.getProductName()) ||
							"GSIP".equals(productInformationBean.getProductName()) || "NPL".equals(productInformationBean.getProductName())){
						 //count = siServiceDetailRepository.getServiceCountByProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds);
						 count = siServiceDetailRepository.getServiceCountByProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						 if("GDE".equals(productInformationBean.getProductName())) {
							 count = count/2;
						 }
						 LOGGER.info("view Spec start for gde, nde, gsip, npl {} ,{},{},{},{}", productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
					}
					if (count != null && count > 0) {
						productInformationBean.setCount(count);
						LOGGER.info("Final Product Count  {}, ----####-----> {}", productInformationBean.getCount(), productInformationBean.toString());
						response.add(productInformationBean);
					}
				});
				
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		if(Objects.nonNull(response) && !response.isEmpty()) {
			boolean isMHSPresent = response.stream().anyMatch(bean -> (bean.getProductName().equals(ServiceInventoryConstants.MHS)));
			boolean isMSSPresent = response.stream().anyMatch(bean -> (bean.getProductName().equals(ServiceInventoryConstants.MSS)));
			if(!isMHSPresent && !responseFromEngagement.isEmpty() && Objects.nonNull(responseFromEngagement)) 
				response.addAll(responseFromEngagement.stream().filter(obj->obj.getProductName().equalsIgnoreCase(ServiceInventoryConstants.MHS)).collect(Collectors.toList()));
			if(!isMSSPresent && !responseFromEngagement.isEmpty() && Objects.nonNull(responseFromEngagement)) 
				response.addAll(responseFromEngagement.stream().filter(obj->obj.getProductName().equalsIgnoreCase(ServiceInventoryConstants.MSS)).collect(Collectors.toList()));
		}
		 LOGGER.info("response {}", response);
		return response;
	}

	private Set<Integer> getCustomerLeIds() {
		Set<Integer> customerLeIds = new HashSet<>();
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
		if(customerDetails != null) {
			for (CustomerDetail customerDetail : customerDetails) {
				customerLeIds.add(customerDetail.getCustomerLeId());
			}
		}
		return customerLeIds;
	}
	
	private Set<Integer> getCustomerLeIdsByCustomerId(Integer customerId) {
		LOGGER.info("customerId {}",customerId);
		Set<Integer> customerLeIds = userInfoUtils.getByErfCustomerId(customerId);
		return customerLeIds;
	}
	
	private Set<Integer> getPartnerLeIds() {
		Set<Integer> partnerLeIds = new HashSet<>();
		List<PartnerDetail> partnerDetails = userInfoUtils.getPartnerDetails();
		if(partnerDetails != null) {
			for (PartnerDetail partnerDetail : partnerDetails) {
				partnerLeIds.add(partnerDetail.getPartnerLeId());
			}
		}
		return partnerLeIds;
	}
	
	private Set<Integer> getPartnerLeIds(Integer partnerId) {
		Set<Integer> partnerLeIds = userInfoUtils.getByErfPartnerId(partnerId);
		return partnerLeIds;
	}

	/**
	 * Update alias name for service
	 *
	 * @param serviceDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	public String updateAliasNameForService(ServiceDetailBean serviceDetailBean) throws TclCommonException {
		String response = null;
		if (serviceDetailBean == null || serviceDetailBean.getServiceId() == null
				|| serviceDetailBean.getAlias() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			if(!StringUtils.isAllEmpty(serviceDetailBean.getSiteType())) {
				return updateAliasDetailsForNpl(serviceDetailBean);
			}
			Optional<SIServiceDetail> siServiceDetail = siServiceDetailRepository
					.findByTpsServiceId(serviceDetailBean.getServiceId());
			if (siServiceDetail.isPresent()) {
				SIServiceDetail serviceDetail = siServiceDetail.get();
				serviceDetail.setSiteAlias(serviceDetailBean.getAlias());
				siServiceDetailRepository.saveAndFlush(serviceDetail);
				response = ResponseResource.RES_SUCCESS;
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	private String updateAliasDetailsForNpl(ServiceDetailBean serviceDetailBean) throws TclCommonException {

		String response = null;
		try {
			Optional<SIServiceDetail> siServiceDetail = siServiceDetailRepository
					.findByTpsServiceIdAndSiteType(serviceDetailBean.getServiceId(),serviceDetailBean.getSiteType());
			if (siServiceDetail.isPresent()) {
				SIServiceDetail serviceDetail = siServiceDetail.get();
				serviceDetail.setSiteAlias(serviceDetailBean.getAlias());
				siServiceDetailRepository.saveAndFlush(serviceDetail);
				response = ResponseResource.RES_SUCCESS;
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	
	}

	/**
	 * Get Service details based on login user and product and serviceID
	 *
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	/**
	 * Get Service details based on login user and product and serviceID
	 *
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceInformationBean getAllServiceDetailsByProductAndServiceIds(ServiceCatalogRequest request)
			throws TclCommonException {

		List<Integer> partnerLeIds = new ArrayList<>();
		List<Integer> customerIds = new ArrayList<>();
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		
		List<ContactBeans> contacts=new ArrayList<>();
		try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<Map<String, Object>> data = null;
				if(request.getProduct() != null && !request.getProduct().isEmpty() 
						&& "GSC".equalsIgnoreCase(request.getProduct())) {
					List<Map<String, Object>> dataGsc = new ArrayList<>();
					
					List<GSCServiceCatalogRequest> gscServiceRequest = request.getGscServiceRequest();
					List<Integer> finalPartnerLeIds = partnerLeIds;
					List<Integer> finalCustomerLeIds = customerIds;
					gscServiceRequest.stream().forEach(val -> {
						String num = val.getTollfreeNum();
						String outpulse = val.getOutpulse();
						String orderId = val.getOrderId();
						if ( StringUtils.isBlank(num) || StringUtils.isBlank(orderId)) {
							LOGGER.error("Tollfree number / Outpulse value is not provided for the GSC service");
							throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
						}
						LOGGER.info("finalCustomerLeIds {} :: finalPartnerLeIds {} :: orderId {} :: num {} :: outpulse {}",finalCustomerLeIds, finalPartnerLeIds, orderId, num, outpulse);
						Map<String, Object> gscData= new HashMap<>();
						if(outpulse!=null)
						 gscData = siOrderRepository.getServiceDetailByServiceIdsForGSC(finalCustomerLeIds, finalPartnerLeIds, orderId, num, outpulse);
						else
							gscData = siOrderRepository.getServiceDetailByServiceIdsWithoutOutpulseForGSC(finalCustomerLeIds, finalPartnerLeIds, orderId, num);    
						
						dataGsc.add(gscData);
					});
					data = dataGsc;
				}
				else {
					List<String> serviceIds = request.getServiceIds();
					if (serviceIds == null || serviceIds.isEmpty()) {
						LOGGER.error("Service Id's not provided");
						throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
					}
					data = siOrderRepository.getServiceDetailByServiceIds(customerIds, partnerLeIds, serviceIds);
				}
				
				if (data != null && !data.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					data.stream().forEach(map -> {
						serviceDetailBeans.add(mapper.convertValue(map, ServiceDetailBean.class));
					});
				}
				siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);

				for (ServiceDetailBean serviceDetailBean : serviceDetailBeans) {
					List<Map<String, Object>> serviceContacts=siServiceContactRepository.findBySiServiceDetail_Id(serviceDetailBean.getSiServiceDetailId());

					if (serviceContacts != null && !serviceContacts.isEmpty()) {
						final ObjectMapper mapper = new ObjectMapper();
						serviceContacts.stream().forEach(map -> {
							ContactBeans con = mapper.convertValue(map, ContactBeans.class);
							if (!contacts.contains(con)) {
								contacts.add(con);
							}
						});
					}
					serviceDetailBean.setServiceAssuranceContacts(serviceContacts);
				}
			}
			siServiceInformationBean.setContacts(contacts);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siServiceInformationBean;
	}

	/**
	 * Get Service data by access type and GSC product name
	 * @param page
	 * @param size
	 * @param accessType
	 * @param productName
	 * @return
	 */
	public Page<Map<String, Object>> getServiceDataByAccessTypeAndProduct(Integer page, Integer size, String accessType,
			String productName) {
		Objects.requireNonNull(accessType, "Access type cannot be null");
		Objects.requireNonNull(size, "Size cannot be null");
		Objects.requireNonNull(page, "Page cannot be null");
		Objects.requireNonNull(productName, "Product name cannot be null");
		switch (accessType) {
		case ServiceInventoryConstants.ACCESS_TYPE_GVPN:
			return siServiceDetailRepository.returnGVPNServiceData(productName,
					PageRequest.of(page, size));
		default:
			return Page.empty();
		}
	}
	
	public PagedResult<List<SIServiceInformationBean>> getServiceDetailsWithPaginationAndSearch(Integer productId,Integer page,Integer size,String city,
																								String alias,String searchText, Integer customerId,
																								Integer partnerId,Integer customerLeId,
																								String opportunityMode,
																								Integer ndeFlag, String vrfFlag) throws TclCommonException{
        List<SIServiceInformationBean> list = new ArrayList<>();
        SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
        List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if(productId==null || page==null || size==null || page<=0 || size<=0) {
               throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
        }
        try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			//Added customerLeId selection for multicircuit
			if(Objects.nonNull(customerLeId))
			{
				customerIds=new ArrayList<>();
				customerIds.add(customerLeId);
			}
				//LOGGER.info("CustomerIds"+customerIds+"CustomerId"+customerId+"PartnerId"+partnerId);
			LOGGER.info("CustomerID :: {}", customerId);
			LOGGER.info("CustomerLeIDs :: {}", customerIds);
			LOGGER.info("PartnerId :: {}", partnerId);
			LOGGER.info("PartnerLeIds :: {}", partnerLeIds);

               if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				   Specification<SIServiceDetail> spec;
				   if(Objects.nonNull(ndeFlag) && ndeFlag == 1) {
					   //NDE
					   String attributeValue = "Hub National Dedicated Ethernet";
					   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
						   spec = SIServiceDetailSpecification.getServiceDetailsWithNdeFilter(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,attributeValue);
					   }
					   else{
						   spec = SIServiceDetailSpecification.getServiceDetailsWithNdeFilter(city, alias, searchText, customerIds, null, productId,customerId,null, null,attributeValue);
					   }
					   Page<SIServiceDetail> data = siServiceDetailRepository.findAll(spec, PageRequest.of(page - 1, size));
					   LOGGER.info("Data"+data);
					   if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
						   List<SIServiceDetail> serviceDetails = data.getContent();
						   serviceDetails.stream().forEach(serviceDetail->{
							   constructServiceDetailBean(serviceDetail, serviceDetailBeans);
						   });
							if(serviceDetailBeans.stream().findAny().get().getProductName().equalsIgnoreCase("IZOPC")) {
								serviceDetailBeans.stream().forEach(si->{
									
									List<Map<String, Object>> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetailIdAndAttributeName(si.getSiServiceDetailId(), "Cloud Provider");
									LOGGER.info("Response serviceAttributes {}",serviceAttributes);
									serviceAttributes.stream().forEach(attr->{
										si.setCloudProvider((String) attr.get("attribute_value"));							
										
									});
									
								});
							}
						   siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
						   siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
						   siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.getAlias().removeIf(Objects::isNull);
						   list.add(siServiceInformationBean);
						   return new PagedResult(list, data.getTotalElements(), data.getTotalPages());
					   }
				   }else if(Objects.nonNull(ndeFlag) && ndeFlag == 0) {
					   //NDE
					   String attributeValue = "Normal";
					   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
						   spec = SIServiceDetailSpecification.getServiceDetailsWithNdeFilter(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,attributeValue);
					   }
					   else{
						   spec = SIServiceDetailSpecification.getServiceDetailsWithNdeFilter(city, alias, searchText, customerIds, null, productId,customerId,null, null,attributeValue);
					   }
					   Page<SIServiceDetail> data = siServiceDetailRepository.findAll(spec, PageRequest.of(page - 1, size));
					   LOGGER.info("Data"+data);
					   if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
						   List<SIServiceDetail> serviceDetails = data.getContent();
						   serviceDetails.stream().forEach(serviceDetail->{
							   constructServiceDetailBean(serviceDetail, serviceDetailBeans);
						   });
							if(serviceDetailBeans.stream().findAny().get().getProductName().equalsIgnoreCase("IZOPC")) {
								serviceDetailBeans.stream().forEach(si->{
									
									List<Map<String, Object>> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetailIdAndAttributeName(si.getSiServiceDetailId(), "Cloud Provider");
									LOGGER.info("Response serviceAttributes {}",serviceAttributes);
									serviceAttributes.stream().forEach(attr->{
										si.setCloudProvider((String) attr.get("attribute_value"));							
										
									});
									
								});
							}
						   siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
						   siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
						   siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.getAlias().removeIf(Objects::isNull);
						   list.add(siServiceInformationBean);
						   return new PagedResult(list, data.getTotalElements(), data.getTotalPages());
					   }
				   }else if(Objects.nonNull(vrfFlag)){
					   // GVPN
					   if(vrfFlag.equalsIgnoreCase("Yes")) {
						   // if toggle is ON 
						   LOGGER.info("User enabled the toggle the vrfFlag is set to "+ vrfFlag);
						   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
							   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,vrfFlag);
						   }
						   else{
							   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, customerIds, null, productId,customerId,null, null,vrfFlag);
						   }
					   }else {
						   if(Objects.nonNull(searchText)) {
							   // If toggle is OFF and search text is present 
							   LOGGER.info("User has not enabled the toggle but there is searchText given "+ searchText);
							   LOGGER.info("Checking whether the serachText "+searchText+ " is valid or not");
							   Optional<SIServiceDetail> siServiceDetail = siServiceDetailRepository.findByTpsServiceId(searchText);
							   if(siServiceDetail.isPresent()) {
								   LOGGER.info("searchText " +searchText+" is valid");
								   if(Objects.nonNull(siServiceDetail.get().getMultiVrfSolution())) {
									   // toggle OFF but service ID is present and having mvrf Yes so sending No as per toggle
									   LOGGER.info("User searched for serviceID "+searchText+ 
											   "and multi_vrf_solution is "+siServiceDetail.get().getMultiVrfSolution()+" sending the vrfFlag as No as toggle is disabled");
									   vrfFlag = "No";
									   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
										   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,vrfFlag);
									   }
									   else{
										   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, customerIds, null, productId,customerId,null, null,vrfFlag);
									   }
								   }else {
									   // toggle OFF but the value for mvrf flag is null for given service ID so passing null
									   LOGGER.info("User searched for serviceID "+searchText+ 
											   "and multi_vrf_solution is "+siServiceDetail.get().getMultiVrfSolution()+" sending the vrfFlag as null to ignore multiVrfSolution");
									   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
										   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,null);
									   }
									   else{
										   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, customerIds, null, productId,customerId,null, null,null);
									   }

								   }
							   }else {
								   LOGGER.info("searchText " +searchText+" is invalid");
								   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
									   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,null);
								   }
								   else{
									   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, customerIds, null, productId,customerId,null, null,null);
								   }
							   }
								
						   }else {
							   // toggle OFF and there is no search text
							   LOGGER.info("If searchText is not given in the filter and toggle is OFF");
							   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
								   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,null);
							   }
							   else{
								   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, customerIds, null, productId,customerId,null, null,null);
							   }
						   }
					   }
					   
					   Page<SIServiceDetail> data = siServiceDetailRepository.findAll(spec, PageRequest.of(page - 1, size));
					   LOGGER.info("Data"+data);
					   if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
						   List<SIServiceDetail> serviceDetails = data.getContent();
						   serviceDetails.stream().forEach(serviceDetail->{
							   constructServiceDetailBean(serviceDetail, serviceDetailBeans);
						   });
						// added for multivrf macd set master and slave info
							if (!serviceDetailBeans.isEmpty()) {
								LOGGER.info("MULTI VRF MACD before set master and slave info"
										+ serviceDetailBeans.get(0).getProductName());
								if (serviceDetailBeans.get(0).getProductName() != null) {
									if(serviceDetailBeans.get(0).getProductName().equalsIgnoreCase("GVPN")) {
										constructMultiVrfMasterAndSlaveInfo(serviceDetailBeans);
									}
								}
							}
							if(serviceDetailBeans.stream().findAny().get().getProductName().equalsIgnoreCase("IZOPC")) {
								serviceDetailBeans.stream().forEach(si->{
									
									List<Map<String, Object>> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetailIdAndAttributeName(si.getSiServiceDetailId(), "Cloud Provider");
									LOGGER.info("Response serviceAttributes {}",serviceAttributes);
									serviceAttributes.stream().forEach(attr->{
										si.setCloudProvider((String) attr.get("attribute_value"));							
										
									});
									
								});
							}
						   siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
						   siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
						   siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.getAlias().removeIf(Objects::isNull);
						   list.add(siServiceInformationBean);
						   return new PagedResult(list, data.getTotalElements(), data.getTotalPages());
					   }
				   }else {
						   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
							   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, null, partnerLeIds, productId,customerId, partnerId, opportunityMode,null);
						   }
						   else{
							   spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, customerIds, null, productId,customerId,null, null,null);
						   }
					   Page<SIServiceDetail> data = siServiceDetailRepository.findAll(spec, PageRequest.of(page - 1, size));
					   LOGGER.info("Data"+data);
					   if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
						   List<SIServiceDetail> serviceDetails = data.getContent();
						   serviceDetails.stream().forEach(serviceDetail->{
							   constructServiceDetailBean(serviceDetail, serviceDetailBeans);
						   });
							if(serviceDetailBeans.stream().findAny().get().getProductName().equalsIgnoreCase("IZOPC")) {
								serviceDetailBeans.stream().forEach(si->{
									
									List<Map<String, Object>> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetailIdAndAttributeName(si.getSiServiceDetailId(), "Cloud Provider");
									LOGGER.info("Response serviceAttributes {}",serviceAttributes);
									serviceAttributes.stream().forEach(attr->{
										si.setCloudProvider((String) attr.get("attribute_value"));							
										
									});
									
								});
							}
						   siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
						   siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
						   siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
						   siServiceInformationBean.getAlias().removeIf(Objects::isNull);
						   list.add(siServiceInformationBean);
						   return new PagedResult(list, data.getTotalElements(), data.getTotalPages());
					   }
				   }
               }
        }catch(Exception e) {
               throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
               }
        return null;
 }


	public PagedResult<List<SIServiceInformationBean>> getLeServiceDetailsWithPaginationAndSearch(Integer productId,Integer page,Integer size,String city,
																								String alias,String searchText, Integer customerId,
																								Integer partnerId,Integer partnerLeId,Integer customerLeId,
																								String opportunityMode) throws TclCommonException{
		List<SIServiceInformationBean> list = new ArrayList<>();
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if(productId==null || page==null || size==null || page<=0 || size<=0) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				if(partnerLeId!=null){
					partnerLeIds.add(partnerLeId);
				}
				else {
					partnerLeIds = new ArrayList<>(getPartnerLeIds());
				}
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			//Added customerLeId selection for multicircuit
			if(Objects.nonNull(customerLeId))
			{
				customerIds=new ArrayList<>();
				customerIds.add(customerLeId);
			}
			else{
				customerIds=null;
			}
			//LOGGER.info("CustomerIds"+customerIds+"CustomerId"+customerId+"PartnerId"+partnerId);
			LOGGER.info("CustomerID :: {}", customerId);
			LOGGER.info("CustomerLeIDs :: {}", customerIds);
			LOGGER.info("PartnerId :: {}", partnerId);
			LOGGER.info("PartnerLeIds :: {}", partnerLeIds);

			if ((customerIds!=null&&!customerIds.isEmpty()) || (partnerLeIds!=null&&!partnerLeIds.isEmpty())) {
				Specification<SIServiceDetail> spec;

				spec = SIServiceDetailSpecification.getServiceDetails(city, alias, searchText, customerIds, partnerLeIds, productId,customerId, partnerId, opportunityMode,null);
				Page<SIServiceDetail> data = siServiceDetailRepository.findAll(spec, PageRequest.of(page - 1, size));
				LOGGER.info("Data"+data);
				if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
					List<SIServiceDetail> serviceDetails = data.getContent();
					serviceDetails.stream().forEach(serviceDetail->{
						constructServiceDetailBean(serviceDetail, serviceDetailBeans);
					});
					siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
					siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
					if(partnerLeIds.size()>1){
						siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId, customerIds, partnerLeIds, customerId, partnerId));
						siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByProduct(productId, customerIds, partnerLeIds, customerId, partnerId));
					}
					else {
						siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByLe(productId, customerIds, partnerLeIds, customerId, partnerId));
						siServiceInformationBean.setCities(siServiceDetailRepository.getDistictCityByLe(productId, customerIds, partnerLeIds, customerId, partnerId));
					}
					siServiceInformationBean.getAlias().removeIf(Objects::isNull);
					list.add(siServiceInformationBean);
					return new PagedResult(list, data.getTotalElements(), data.getTotalPages());
				}
			}
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		return new PagedResult(list,0,0);
	}

	private void constructServiceDetailBean(SIServiceDetail siServiceDetail,List<ServiceDetailBean> serviceDetailBeans) {
        ServiceDetailBean serviceDetailBean = new ServiceDetailBean();
        serviceDetailBean.setAccessType(siServiceDetail.getAccessType());
        serviceDetailBean.setAlias(siServiceDetail.getSiteAlias());
        if(siServiceDetail.getSiOrder()!=null) {
               SIOrder siOrder = siServiceDetail.getSiOrder();
               serviceDetailBean.setCustomerId(siOrder.getErfCustCustomerId());
               serviceDetailBean.setCustomerName(siOrder.getErfCustCustomerName());
               if(siOrder.getErfCustLeId()!=null) {
               serviceDetailBean.setLeId(siOrder.getErfCustLeId().toString());
               serviceDetailBean.setLeName(siOrder.getErfCustLeName());
               serviceDetailBean.setCustomerSegment(siOrder.getCustomerSegment());
               serviceDetailBean.setOpportunityClassification(siOrder.getOpportunityClassification());
               serviceDetailBean.setOrderCode(siOrder.getOpOrderCode());
               serviceDetailBean.setSfdcAccountId(siOrder.getSfdcAccountId());
               serviceDetailBean.setSfdcCuid(siOrder.getTpsSfdcCuid());
               if(siOrder.getErfCustSpLeId()!=null) {
               serviceDetailBean.setSupplierLeId(siOrder.getErfCustSpLeId().toString());
               }
               serviceDetailBean.setSupplierLeName(siOrder.getErfCustSpLeName());
               
               }
               
        }
        serviceDetailBean.setDestinationCity(siServiceDetail.getDestinationCity());
        serviceDetailBean.setLatLong(siServiceDetail.getLatLong());
        serviceDetailBean.setOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
   serviceDetailBean.setOfferingName(siServiceDetail.getErfPrdCatalogParentProductOfferingName());
        serviceDetailBean.setPortSpeed(siServiceDetail.getBwPortspeed());
        serviceDetailBean.setPortSpeedUnit(siServiceDetail.getBwUnit());
        serviceDetailBean.setProductId(siServiceDetail.getErfPrdCatalogProductId());
        serviceDetailBean.setProductName(siServiceDetail.getErfPrdCatalogProductName());
        serviceDetailBean.setServiceClass(siServiceDetail.getServiceClass());
        serviceDetailBean.setServiceClassification(siServiceDetail.getServiceClassification());
        serviceDetailBean.setServiceId(siServiceDetail.getTpsServiceId());
        serviceDetailBean.setServiceTopology(siServiceDetail.getServiceTopology());
        serviceDetailBean.setSiServiceDetailId(siServiceDetail.getId());
        serviceDetailBean.setSiteAddress(siServiceDetail.getSiteAddress());
        serviceDetailBean.setSiteLinkLabel(siServiceDetail.getSiteLinkLabel());
        serviceDetailBean.setSiteTopology(siServiceDetail.getSiteTopology());
        serviceDetailBean.setSiteType(siServiceDetail.getSiteType());
        serviceDetailBean.setSmEmail(siServiceDetail.getSmEmail());
        serviceDetailBean.setSmName(siServiceDetail.getSmName());
        serviceDetailBean.setSourceCity(siServiceDetail.getSourceCity());
        serviceDetailBean.setPrimaryServiceId(siServiceDetail.getTpsServiceId());
        serviceDetailBean.setSecondaryServiceId(siServiceDetail.getPriSecServiceLink());
        serviceDetailBean.setLinkType(siServiceDetail.getPrimarySecondary());
        serviceDetailBean.setVpnName(siServiceDetail.getVpnName());
        serviceDetailBean.setIsActive(siServiceDetail.getIsActive());
        serviceDetailBean.setServiceStatus(siServiceDetail.getServiceStatus()); 
        //to be added
        serviceDetailBean.setSiOrderId(siServiceDetail.getSiOrder().getId());
		serviceDetailBean.setTaxExemptionFlag(siServiceDetail.getTaxExemptionFlag());
		serviceDetailBean.setBillingAccountId(siServiceDetail.getBillingAccountId());
		serviceDetailBean.setBillingGstNumber(siServiceDetail.getBillingGstNumber());
		serviceDetailBean.setCommissionedDate(siServiceDetail.getServiceCommissionedDate());
		serviceDetailBean.setIpAddressProvidedBy(siServiceDetail.getIpAddressProvidedBy());
		serviceDetailBean.setSiteLocationId(siServiceDetail.getErfLocSiteAddressId());
		serviceDetailBean.setRemarks(siServiceDetail.getRemarks());
		serviceDetailBean.setAssociateBillableId(siServiceDetail.getAssociateBillableId());
		
		//billing address and terminmonths,contract start date and contract end date to be contract info
		final ObjectMapper mapper=new ObjectMapper();
		List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(siServiceDetail.getId());
		ServiceDetailBean serviceDetail=mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
		serviceDetailBean.setContractEndDate(siServiceDetail.getSiContractInfo().getContractEndDate());
		serviceDetailBean.setContractStartDate(siServiceDetail.getSiContractInfo().getContractStartDate());
		serviceDetailBean.setTermInMonths(siServiceDetail.getSiContractInfo().getOrderTermInMonths());
		serviceDetailBean.setBillingAddress(siServiceDetail.getSiContractInfo().getBillingAddress());
		serviceDetailBean.setCircuitExpiryDate(siServiceDetail.getCircuitExpiryDate());
        serviceDetailBean.setBillingType(siServiceDetail.getBillingType());


		if(siServiceDetail.getErfPrdCatalogProductName().equals(CommonConstants.NPL)){
			serviceDetailBean.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
			Optional<SIServiceAdditionalInfo> siServAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(siServiceDetail.getId(), "Cross Connect Type");

			if(siServAddInf.isPresent()){
				serviceDetailBean.setCrossConnectType(siServAddInf.get().getAttributeValue());
			}
		}
		//serviceAssurance contacts to be added

        serviceDetailBeans.add(serviceDetailBean);
 }

	

	/**
	 * Method to get ip attribute details for a particular service ID
	 *
	 * @param serviceId
	 * @return List<Map<String,Object>>
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getServiceDetailsIpAttribute(String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<Map<String, Object>> ipAttributeList = siServiceDetailRepository.findIPAttributeByServiceId(serviceId);

		/*if (ipAttributeList == null || ipAttributeList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.IP_ATTRIBUTE_NOT_FOUND, ResponseResource.R_CODE_ERROR);
		}*/
		
		return ipAttributeList;

	}

	public Boolean getSourceCountryForVpn(String vpnId) throws TclCommonException {
		boolean isDomestic = false;
		if (StringUtils.isBlank(vpnId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		
		List<SIServiceDetail> serviceDetailList = siServiceDetailRepository.findByBillingAccountId(vpnId);
		if(serviceDetailList!= null && !serviceDetailList.isEmpty()) {
				if(serviceDetailList.stream().allMatch(serviceDetail -> serviceDetail.getSourceCountry().equalsIgnoreCase("INDIA")))
					isDomestic = true;
		}	
		
		return isDomestic;
	}
	

	/**
	 * To get the Detailed information for ILL services
	 * 
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIDetailedInfoResponse getDeatiledSIInfoForILLService(String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		SIDetailedInfoResponse response = new SIDetailedInfoResponse();
		try {
			List<Map<String, Object>> details = siServiceInfoRepository.getServiceDetailByServiceId(serviceId, "IAS");
			if (details == null || details.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			final ObjectMapper mapper = new ObjectMapper();
			details.stream().forEach(detail -> {
				SISolutionOffering solution = new SISolutionOffering();
				String productName = (String) detail.get("product_name");
				Integer sysId = (Integer) detail.get("sys_id");
				LOGGER.info("System Id"+sysId);
				response.setProductName(productName);
				response.setSolutions(solution);
				List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(sysId);

				ServiceDetailBean serviceDetail=mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
				setSolutionAttributes(solution,detail);
				solution.setBillingAccountId(serviceDetail.getBillingAccountId());
				solution.setBillingAddress(serviceDetail.getBillingAddress());
				solution.setBillingGstNumber(serviceDetail.getBillingGstNumber());
				solution.setContractEndDate(serviceDetail.getContractEndDate());
				solution.setContractStartDate(serviceDetail.getContractStartDate());
				solution.setLeId(serviceDetail.getLeId());
				solution.setLeName(serviceDetail.getLeName());
				solution.setSupplierLeId(serviceDetail.getSupplierLeId());
				solution.setSupplierLeName(serviceDetail.getSupplierLeName());
				solution.setTaxExemptionFlag(serviceDetail.getTaxExemptionFlag());
				solution.setSiServiceDetailId(serviceDetail.getSiServiceDetailId());
				solution.setSiOrderId(serviceDetail.getSiOrderId());
				solution.setTermInMonths(serviceDetail.getTermInMonths());
				solution.setPrimaryServiceId(serviceDetail.getPrimaryServiceId());
				solution.setSecondaryServiceId(serviceDetail.getSecondaryServiceId());
				populateSolutions(sysId, detail, solution);
			});
			return response;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
    
	/**
	 * To get the Detailed information for IWAN services
	 * 
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIDetailedInfoResponse getDeatiledSIInfoForIWANService(String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		SIDetailedInfoResponse response = new SIDetailedInfoResponse();
		try {
			List<Map<String, Object>> details = siServiceInfoRepository.getServiceDetailByServiceId(serviceId, "IWAN");
			if (details == null || details.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			final ObjectMapper mapper = new ObjectMapper();
			details.stream().forEach(detail -> {
				SISolutionOffering solution = new SISolutionOffering();
				String productName = (String) detail.get("product_name");
				Integer sysId = (Integer) detail.get("sys_id");
				LOGGER.info("System Id"+sysId);
				response.setProductName(productName);
				response.setSolutions(solution);
				List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(sysId);

				ServiceDetailBean serviceDetail=mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
				setSolutionAttributes(solution,detail);
				solution.setBillingAccountId(serviceDetail.getBillingAccountId());
				solution.setBillingAddress(serviceDetail.getBillingAddress());
				solution.setBillingGstNumber(serviceDetail.getBillingGstNumber());
				solution.setContractEndDate(serviceDetail.getContractEndDate());
				solution.setContractStartDate(serviceDetail.getContractStartDate());
				solution.setLeId(serviceDetail.getLeId());
				solution.setLeName(serviceDetail.getLeName());
				solution.setSupplierLeId(serviceDetail.getSupplierLeId());
				solution.setSupplierLeName(serviceDetail.getSupplierLeName());
				solution.setTaxExemptionFlag(serviceDetail.getTaxExemptionFlag());
				solution.setSiServiceDetailId(serviceDetail.getSiServiceDetailId());
				solution.setSiOrderId(serviceDetail.getSiOrderId());
				solution.setTermInMonths(serviceDetail.getTermInMonths());
				solution.setPrimaryServiceId(serviceDetail.getPrimaryServiceId());
				solution.setSecondaryServiceId(serviceDetail.getSecondaryServiceId());
				populateSolutions(sysId, detail, solution);
			});
			return response;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the Detailed information for GVPN services
	 * 
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public SIDetailedInfoResponse getDeatiledSIInfoForGvpnService(String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		try {
			List<Map<String, Object>> details = siServiceInfoRepository.getServiceDetailByServiceId(serviceId, "GVPN");
			if (details == null || details.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			final ObjectMapper mapper = new ObjectMapper();
			SIDetailedInfoResponse response = new SIDetailedInfoResponse();
			details.stream().forEach(detail -> {
				SISolutionOffering solution = new SISolutionOffering();
				String productName = (String) detail.get("product_name");
				Integer sysId = (Integer) detail.get("sys_id");
				setSolutionAttributes(solution,detail);

				response.setProductName(productName);
				response.setSolutions(solution);
				List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(sysId);
				ServiceDetailBean serviceDetail=mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
				solution.setBillingAccountId(serviceDetail.getBillingAccountId());
				solution.setBillingAddress(serviceDetail.getBillingAddress());
				solution.setBillingGstNumber(serviceDetail.getBillingGstNumber());
				solution.setContractEndDate(serviceDetail.getContractEndDate());
				solution.setContractStartDate(serviceDetail.getContractStartDate());
				solution.setLeId(serviceDetail.getLeId());
				solution.setLeName(serviceDetail.getLeName());
				solution.setSupplierLeId(serviceDetail.getSupplierLeId());
				solution.setSupplierLeName(serviceDetail.getSupplierLeName());
				solution.setTaxExemptionFlag(serviceDetail.getTaxExemptionFlag());
				solution.setSiServiceDetailId(serviceDetail.getSiServiceDetailId());
				solution.setSiOrderId(serviceDetail.getSiOrderId());
				solution.setTermInMonths(serviceDetail.getTermInMonths());
				solution.setPrimaryServiceId(serviceDetail.getPrimaryServiceId());
				solution.setSecondaryServiceId(serviceDetail.getSecondaryServiceId());
				populateSolutions(sysId, detail, solution);
			});
			return response;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	private void populateSolutions(Integer sysId, Map<String, Object> detail, SISolutionOffering solutions) {
		/*// hardcoded vales for testing - to be removed when actual data populated
		solutions.setOfferingName((String) detail.get("offering_name"));
		solutions.setSiteAddress((String) detail.get("site_address"));
		solutions.setSiteAlias((String) detail.get("site_alias"));
		// solutions.setSlaCommittment((String) detail.get("srv_product_offering_name"));
		solutions.setAccessType((String) detail.get("access_type"));
		// solutions.setAccessProvider((String) detail.get("srv_product_offering_name"));
		solutions.setPrimaryOrSecondary((String) detail.get("pri_sec"));
		// solutions.setCpeProvider((String) detail.get("srv_product_offering_name"));
		// solutions.setLocation((String) detail.get("srv_product_offering_name"));
		solutions.setLatLong((String) detail.get("lat_long"));
		// solutions.setUsageModel((String) detail.get("srv_product_offering_name"));
		solutions.setServiceStatus((String) detail.get("service_status"));
		// solutions.setCommissioningDate((String) detail.get("srv_product_offering_name"));
		solutions.setLastMileProvider((String) detail.get("lastmile_provider"));
*/
		List<ComponentBean> components = new ArrayList<>();
		/*solutions.setComponents(components);*/
		
		populateComponents(sysId, components, detail);
	}

	private void populateComponents(Integer sysId, List<ComponentBean> components, Map<String, Object> detail) {
		List<Map<String, Object>> assetDetails = siServiceInfoRepository.getAssetWithAttributes(sysId);
		
		assetDetails.stream().forEach( asset -> {
			AttributeDetail attribute = new AttributeDetail();
			attribute.setName((String) asset.get("attribute_name"));
			attribute.setValue((String) asset.get("attribute_value"));
			
			String componentName = (String) asset.get("component");
			Optional<ComponentBean> component = getComponent(componentName, components);
			if(component.get() != null) {
				List<AttributeDetail> attributesList = component.get().getAttributes();
				attributesList.add(attribute);
			}
			else {
				ComponentBean newComponent = new ComponentBean();
				newComponent.setName(componentName);

				List<AttributeDetail> attributesList = new ArrayList<>();
				attributesList.add(attribute);
				newComponent.setAttributes(attributesList);
			}
		});
	}

	private Optional<ComponentBean> getComponent(String componentName, List<ComponentBean> components) {
		return components.stream().filter(comp -> comp.getName().equals(componentName)).findFirst();
	}

	
	/**
	 * Method to get service details for a particular service ID and its related
	 * service id details
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SIServiceInfoBean> getServiceDetailsPrimarySecondary(String serviceId) throws TclCommonException {

		LOGGER.info("Service Id Recieved from request is ----> {} ", serviceId);
		List<SIServiceInfoBean> siServiceDetailBeanList = new ArrayList<>();

		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		// Optional<SIServiceDetail> optDetail =
		// siServiceDetailRepository.findByTpsServiceId(serviceId);
		Optional<SIServiceInfo> siServiceInfoOpt = siServiceInfoRepository.findByServiceId(serviceId);

		if (!siServiceInfoOpt.isPresent()) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}
		try {
			siServiceDetailBeanList.add(constructSiServiceInfoBean(siServiceInfoOpt.get()));
		} catch (TclCommonException e) {
			LOGGER.error("Error in construct Si Service Info Bean", e);
		}

		//siServiceDetailBeanList.add(constructSiServiceInfoBean(siServiceInfoOpt.get()));

		if (StringUtils.isNotBlank(siServiceInfoOpt.get().getPrimarySecondaryLink())) {
			Optional<SIServiceInfo> relatedServiceBean = siServiceInfoRepository
					.findByServiceId(siServiceInfoOpt.get().getPrimarySecondaryLink());

			if (!relatedServiceBean.isPresent())
				throw new TclCommonException(ExceptionConstants.INVALID_RELATED_SERVICEID,
						ResponseResource.R_CODE_ERROR);

			siServiceDetailBeanList.add(constructSiServiceInfoBean(relatedServiceBean.get()));
		}

		return siServiceDetailBeanList;

	}

	private SIServiceInfoBean constructSiServiceInfoBean(SIServiceInfo siServiceDetail) throws TclCommonException{
		final String[] totalBandWidth= {""};
		if(Objects.nonNull(siServiceDetail) && Objects.nonNull(siServiceDetail.getServiceId())){
			LOGGER.info("Inside method contruct si service info bean for service Id ----> {} ", siServiceDetail.getServiceId());
		}
		SIServiceInfoBean siServiceDetailBean = new SIServiceInfoBean();
		Set<SIServiceAttributeBean> attributes = new HashSet<>();
		if (siServiceDetail != null) {
			siServiceDetailBean.setId(siServiceDetail.getId());
			siServiceDetailBean.setTpsServiceId(siServiceDetail.getServiceId());
			siServiceDetailBean.setProductName(siServiceDetail.getProductFamilyName());
			// siServiceDetailBean.setServiceTopology(siServiceDetail.get);
			siServiceDetailBean.setVpnName(siServiceDetail.getVpnName());
			siServiceDetailBean.setPrimaryOrSecondary(siServiceDetail.getPrimaryOrSecondary());
			siServiceDetailBean.setSiteEndInterface(siServiceDetail.getSiteEndInterface());
			siServiceDetailBean.setLatLong(siServiceDetail.getLatLong());
			// siServiceDetailBean.setSiteTopology(siServiceDetail.getSiteTopology());
			siServiceDetailBean.setBandwidthPortSpeed(siServiceDetail.getBandwidth());
			siServiceDetailBean.setBandwidthUnit(siServiceDetail.getBandwidthUnit());
			siServiceDetailBean.setLastMileBandwidth(siServiceDetail.getLastMileBandwidth());
			siServiceDetailBean.setLastMileBandwidthUnit(siServiceDetail.getLastMileBandwidthUnit());
			siServiceDetailBean.setLastMileProvider(siServiceDetail.getLastMileProvider());
			siServiceDetailBean.setLastMileType(siServiceDetail.getLastMileType());
			siServiceDetailBean.setBurstableBandwidthPortspeed(siServiceDetail.getBurstableBandwidth());
			siServiceDetailBean.setBurstableBandwidthUnit(siServiceDetail.getBurstableBandwidthUnit());
			siServiceDetailBean.setServiceOption(siServiceDetail.getServiceManagementOption());
			siServiceDetailBean.setServiceStatus(siServiceDetail.getServiceStatus());
			siServiceDetailBean.setMrc(siServiceDetail.getMrc());
			siServiceDetailBean.setNrc(siServiceDetail.getNrc());
			siServiceDetailBean.setArc(siServiceDetail.getArc());
			siServiceDetailBean.setSiOrderId(siServiceDetail.getOrderSysId());
			siServiceDetailBean.setSiteAddress(siServiceDetail.getCustomerSiteAddress());
			siServiceDetailBean.setLocationId(siServiceDetail.getLocationId());
			siServiceDetailBean.setPriSecServiceLink(siServiceDetail.getPrimarySecondaryLink());
			// siServiceDetailBean.setTaxExemptionFlag(siServiceDetail.getTaxExemptionFlag());
			siServiceDetailBean.setTpsSfdcParentOptyId(siServiceDetail.getOpportunityId());
			if(siServiceDetail.getCommissionedDate() != null ) {
				siServiceDetailBean.setServiceCommissionedDate(siServiceDetail.getCommissionedDate().toString());
			}
			siServiceDetailBean.setDemoFlag(Objects.nonNull(siServiceDetail.getOrderDemoFlag())?siServiceDetail.getOrderDemoFlag():"");
			siServiceDetailBean.setDemoType(Objects.nonNull(siServiceDetail.getBillingType())?siServiceDetail.getBillingType():"");
			siServiceDetailBean.setBillingCurrency(siServiceDetail.getBillingCurrency());
			siServiceDetailBean.setPortMode(siServiceDetail.getSrvPortMode());
			// siServiceDetailBean.setServiceTerminationDate(siServiceDetail.getTerminationDate().toString());
			//constructAssetInfo(siServiceDetail, siServiceDetailBean, attributes);
			
			 //added for gvpn multivrf macd update port bw values to total vrf bandwidth value
			if (siServiceDetail.getProductFamilyName() != null
					&& siServiceDetail.getProductFamilyName().equalsIgnoreCase("GVPN")) {
				LOGGER.info("si service info bean  service Id ----> {} ", siServiceDetail.getServiceId());
				Optional<SIServiceDetail> servicedetail = siServiceDetailRepository
						.findByTpsServiceIdAndIsActive(siServiceDetail.getServiceId(), CommonConstants.Y);
				if (servicedetail.isPresent()) {
					if (servicedetail.get().getMultiVrfSolution() != null) {
						LOGGER.info("MULTIVRF FLAG" + servicedetail.get().getMultiVrfSolution() + "port bw" + siServiceDetailBean.getBandwidthPortSpeed() + "unit por bw" + siServiceDetailBean.getBandwidthUnit());
						if (servicedetail.get().getMultiVrfSolution().equalsIgnoreCase("Yes")) {
							totalBandWidth[0] = getAttributesValues(servicedetail.get(),
									CommonConstants.TOTAL_VRF_BANDWIDTH_MBPS);
							LOGGER.info("total vrf bandwidth" + totalBandWidth[0]);
							siServiceDetailBean.setBandwidthPortSpeed(totalBandWidth[0]);
							siServiceDetailBean.setBandwidthUnit("Mbps");
						}
					}
				}
			}

			try {
				constructAssetInfo(siServiceDetail, siServiceDetailBean, attributes);
				getDemoOrderContractDetails(attributes,siServiceDetail);
			} catch (TclCommonException e) {
				LOGGER.error("Error in constructing asset info", e);
			}



		}
		return siServiceDetailBean;
	}

	private void constructAssetInfo(SIServiceInfo siServiceDetail, SIServiceInfoBean siServiceDetailBean,
			Set<SIServiceAttributeBean> attributes) throws TclCommonException{
		LOGGER.info("Inside constructAssetInfo method ");
		Set<SIServiceAttributeBean> assetAttributes = new HashSet<>();

		List<Map<String, Object>> assetDetails = siServiceInfoRepository
				.getAssetTypeDetailWithAttributes(siServiceDetail.getId(),"CPE");
		assetDetails.stream().forEach(entry -> {
			SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
			//siServiceAttributeBean.setCategory(entry.get("component").toString());
			siServiceAttributeBean.setAttributeName("CPE Basic Chassis");
			String cpeModel = (String)entry.get("cpe_model");
			siServiceAttributeBean.setAttributeValue(cpeModel);
			LOGGER.info("Service attribute is ---> {} ", siServiceAttributeBean);
			attributes.add(siServiceAttributeBean);
			
			if(entry.get(MACDConstants.OEM_VENDOR) != null) {
				SIServiceAttributeBean siServiceAttributeOem = new SIServiceAttributeBean();
				//siServiceAttributeBean.setCategory(entry.get("component").toString());
				siServiceAttributeOem.setAttributeName("oem");
				String oem = (String)entry.get(MACDConstants.OEM_VENDOR);
				siServiceAttributeOem.setAttributeValue(oem);
				attributes.add(siServiceAttributeOem);
				
			} else if(entry.get(MACDConstants.CPE_SERIAL_NO) != null) {
				SIServiceAttributeBean siServiceAttributeCpeSerialNo = new SIServiceAttributeBean();
				//siServiceAttributeBean.setCategory(entry.get("component").toString());
				siServiceAttributeCpeSerialNo.setAttributeName("cpeSerialNumber");
				String cpeSerialNo = (String)entry.get(MACDConstants.CPE_SERIAL_NO);
				siServiceAttributeCpeSerialNo.setAttributeValue(cpeSerialNo);
				attributes.add(siServiceAttributeCpeSerialNo);
			}
		});
		siServiceDetailBean.setAttributes(attributes);
		assetAttributes.addAll(attributes);

		List<Map<String, Object>> serviceAttributeDetails =	siServiceInfoRepository.getServiceAttributesBasedOnServiceDetailId(siServiceDetail.getId());
		serviceAttributeDetails.stream().forEach(attribute -> {
			if(attribute.get(MACDConstants.VENDOR_ID) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.VENDOR_ID);
				String vendorId = (String)attribute.get(MACDConstants.VENDOR_ID);
				siServiceAttributeBean.setAttributeValue(vendorId);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("Vendor ID : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.VENDOR_ID), siServiceDetail.getId());
			} else if(attribute.get(MACDConstants.VENDOR_NAME) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.VENDOR_NAME);
				String vendorName = (String)attribute.get(MACDConstants.VENDOR_NAME);
				siServiceAttributeBean.setAttributeValue(vendorName);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("Vendor NAME : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.VENDOR_NAME), siServiceDetail.getId());
			} else if(attribute.get(MACDConstants.BURSTABLE_BW) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.BURSTABLE_BW);
				String burstableBw = (String)attribute.get(MACDConstants.BURSTABLE_BW);
				siServiceAttributeBean.setAttributeValue(burstableBw);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("BURSTABLE_BW : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.BURSTABLE_BW), siServiceDetail.getId());
			} else if(attribute.get(MACDConstants.LOCAL_LOOP_BW) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.LOCAL_LOOP_BW);
				String localLoopBw = (String)attribute.get(MACDConstants.LOCAL_LOOP_BW);
				siServiceAttributeBean.setAttributeValue(localLoopBw);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("LOCAL_LOOP_BW : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.LOCAL_LOOP_BW), siServiceDetail.getId());
			} else if(attribute.get(MACDConstants.SITE_TYPE) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.SITE_TYPE);
				String siteType = (String)attribute.get(MACDConstants.SITE_TYPE);
				siServiceAttributeBean.setAttributeValue(siteType);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("SITE_TYPE : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.SITE_TYPE), siServiceDetail.getId());
			}  else if(attribute.get(MACDConstants.CPE_BASIC_CHASSIS) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.CPE_BASIC_CHASSIS);
				String cpeBasicChassis = (String)attribute.get(MACDConstants.CPE_BASIC_CHASSIS);
				siServiceAttributeBean.setAttributeValue(cpeBasicChassis);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("CPE_BASIC_CHASSIS : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.CPE_BASIC_CHASSIS), siServiceDetail.getId());
			} else if(attribute.get(MACDConstants.BTS_DEVICE_TYPE) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.BTS_DEVICE_TYPE);
				String btsDeviceType = (String)attribute.get(MACDConstants.BTS_DEVICE_TYPE);
				siServiceAttributeBean.setAttributeValue(btsDeviceType);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("BTS_DEVICE_TYPE : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.BTS_DEVICE_TYPE), siServiceDetail.getId());
			} else if(attribute.get(MACDConstants.BACKHAUL_PROVIDER) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.BACKHAUL_PROVIDER);
				String backHaulProvider = (String)attribute.get(MACDConstants.BACKHAUL_PROVIDER);
				siServiceAttributeBean.setAttributeValue(backHaulProvider);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("BACKHAUL_PROVIDER : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.BACKHAUL_PROVIDER), siServiceDetail.getId());
			} else if(MACDConstants.PRODUCT_FLAVOUR.equals(attribute.get("attribute_name"))) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.PRODUCT_FLAVOUR);
				String productFlavour = (String)attribute.get("attribute_value");
				siServiceAttributeBean.setAttributeValue(productFlavour);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("PRODUCT_FLAVOUR : {}, service detail id {}", 
						(String)attribute.get("attribute_value"), siServiceDetail.getId());
			} else if(MACDConstants.LL_ARRANGE_BY.equals(attribute.get("attribute_name"))) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.LL_ARRANGE_BY);
				String llArrangeBy = (String)attribute.get("attribute_value");
				siServiceAttributeBean.setAttributeValue(llArrangeBy);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("LL_ARRANGE_BY : {}, service detail id {}",
						(String)attribute.get("attribute_value"), siServiceDetail.getId());
			} else if(MACDConstants.SHARED_LM_REQUIRED.equals(attribute.get("attribute_name"))) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.SHARED_LM_REQUIRED);
				String sharedLmRequired = (String)attribute.get("attribute_value");
				siServiceAttributeBean.setAttributeValue(sharedLmRequired);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("SHARED_LM_REQUIRED : {}, service detail id {}",
						(String)attribute.get("attribute_value"), siServiceDetail.getId());
			}
				
			
		});
		siServiceDetailBean.setAttributes(assetAttributes);
		
	}
	
	/**
	 * Method to get Customer details from erf_cus_customer_id
	 * @param erfId
	 * Author SuruchiA
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */

	/*private Map<Integer, List<OmsCustomerBean>> getCustomerId(List<Integer> erfIds) throws TclCommonException, IllegalArgumentException {
		//String requestPayload = Utils.convertObjectToJson(erfIds);
		String response =  (String) mqUtils.sendAndReceive(cusQueue,StringUtils.join(erfIds,","),
				MDC.get(CommonConstants.MDC_TOKEN_KEY));		
		OmsCustomerBean[] omsCustomerBeans = (OmsCustomerBean[]) Utils.convertJsonToObject(response, OmsCustomerBean[].class);	
		return Arrays.asList(omsCustomerBeans).stream().collect(Collectors.groupingBy(OmsCustomerBean::getErfCusId));
	}*/

	/**
	 * Method to get macd details from service Ids
	 * @param serviceIds
	 * Author SuruchiA
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */

	private Map<String,Object> getMacdFlags(Map<String,String> serviceIds) throws TclCommonException, IllegalArgumentException {
		//String requestPayload = Utils.convertObjectToJson(erfIds);
		Map<String, Object> macdFlags =new HashMap<>();
		if(Objects.nonNull(serviceIds)&&!serviceIds.isEmpty()) {
			MacdFlagServiceRequest macdServiceIds = new MacdFlagServiceRequest();
			macdServiceIds.setMacdServiceIds(serviceIds);
			String response = (String) mqUtils.sendAndReceive(macdQueue, Utils.convertObjectToJson(macdServiceIds));
			LOGGER.info("MacdResponse" + response);

			if (Objects.nonNull(response) && StringUtils.isNotBlank(response)) {
				macdFlags = (Map<String, Object>) Utils.convertJsonToObject(response, Map.class);
			}
		}
		return macdFlags;
	}

	public static class Context {
		Integer rowCount = 0;
		Row row;
	}


	/**
	 * Method to fetch inventory details for given product
	 * @param productFamilyName
	 * @param workbook
	 * @throws TclCommonException
	 */
	public void getProductFamilyDetails(String productFamilyName,XSSFWorkbook workbook)
			throws TclCommonException {

		Set<SIServiceExcelBean> serviceDetailExcelBeansNonGsip = new HashSet<>();
		List<SIServiceExcelBean> serviceDetailExcelBeansGSIP = new ArrayList<>();
		List<SIServiceExcelBean> serviceDetailExcelBeansNPL = new ArrayList<>();
		Map<String, List<SIServiceExcelBean>> serviceAttrGrouped= new HashMap<>();
		Integer productID = null;
		try {
			List<Integer> customerIds = null;
			List<Integer> partnerLeIds =null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(getPartnerLeIds());
			}
			else
			{
				customerIds=new ArrayList<>(getCustomerLeIds());
			}
			//List<Integer> listOfPrductIds = siServiceDetailRepository.getProductIdByProductName(productFamilyName);
			List<Integer> listOfPrductIds =new ArrayList<>();
			String queueResponse = (String) mqUtils.sendAndReceive(productDetailsQueue, null);
			LOGGER.info("queueResponse to fetch productId :"+ queueResponse);
			List<Map<String, Object>>  productInformationBeans = (List<Map<String, Object>>) Utils
					.convertJsonToObject(queueResponse, List.class);
			final ObjectMapper objectMapper = new ObjectMapper();
			productInformationBeans.stream().forEach(map -> {
				ProductInformationBean productInformationBean = objectMapper.convertValue(map,
						ProductInformationBean.class);
				if (productInformationBean.getProductName().equalsIgnoreCase(productFamilyName) ) {
					listOfPrductIds.add(productInformationBean.getProductId());
				 }
				 });

			if (listOfPrductIds != null && !listOfPrductIds.isEmpty()) {
				productID = listOfPrductIds.get(0);
			}
			
			List<Map<String, Object>> dataForNonGsip = null;
			List<Map<String, Object>> dataForGsip = null;
			if ((customerIds!=null&&!customerIds.isEmpty())||(partnerLeIds!=null&&!partnerLeIds.isEmpty())) {
				/** original query commented for performance and added a new query by merging 2 more queries to main query */
				
				//	data = siOrderRepository.getServiceDetailForExcel(customerIds, partnerLeIds,productID);
				if(!productFamilyName.equalsIgnoreCase("GSIP"))
					dataForNonGsip = siOrderRepository.getServiceDetailForExcelData(customerIds, partnerLeIds,productID);
				else
				dataForGsip=siOrderRepository.getServiceDetailForExcelDataGSIP(customerIds, partnerLeIds,productID);
				
				if (dataForNonGsip != null && !dataForNonGsip.isEmpty()) {
					if (dataForNonGsip != null && !dataForNonGsip.isEmpty()) {
						final ObjectMapper mapper2 = new ObjectMapper();
						dataForNonGsip.stream().forEach(map -> {
							serviceDetailExcelBeansNonGsip.add(mapper2.convertValue(map, SIServiceExcelBean.class));
							serviceDetailExcelBeansNPL.add(mapper2.convertValue(map, SIServiceExcelBean.class));
						});
					}
					 serviceAttrGrouped=
							 serviceDetailExcelBeansNonGsip.stream().distinct().collect(Collectors.groupingBy(SIServiceExcelBean::getServiceId));

					// set B end Last mile provider to null as GVPN and IAS doesnot have it..
					if(productFamilyName.equals("GVPN") || productFamilyName.equals("IAS")) {
						serviceAttrGrouped.entrySet().parallelStream().forEach(map->map.getValue().stream().forEach(siBean -> siBean.setbEndLlProvider(null)));
					}
				}
				if (dataForGsip != null && !dataForGsip.isEmpty()) {
						final ObjectMapper mapper2 = new ObjectMapper();
						dataForGsip.stream().forEach(map -> {
							serviceDetailExcelBeansGSIP.add(mapper2.convertValue(map, SIServiceExcelBean.class));
						});
						
				 
				}
			}
			if(!productFamilyName.equalsIgnoreCase("GSIP")) {
			Map<String,SIServiceExcelBean> nplMap = new HashMap<String,SIServiceExcelBean>();
			if (productFamilyName.equals("NPL")) {
				serviceDetailExcelBeansNPL.stream().filter(s -> s.getServiceId() != null).forEach(p -> {
					
					if(p.getSiteType().equals("SiteB")) {
						p.setDestinationAddress(p.getSourceAddress());
						p.setSourceAddress(null);
					}
					
					if (nplMap.containsKey(p.getServiceId())) {
						SIServiceExcelBean existingBean = nplMap.get(p.getServiceId());
						if(existingBean.getSourceAddress() ==null) {
							existingBean.setSourceAddress(p.getSourceAddress());
						}else if(existingBean.getDestinationAddress() ==null){
							existingBean.setDestinationAddress(p.getDestinationAddress());
						}
					}
						nplMap.putIfAbsent(p.getServiceId(), p);
				});
				serviceDetailExcelBeansNPL.clear();
				serviceDetailExcelBeansNPL.addAll(nplMap.values());
				serviceAttrGrouped=
						serviceDetailExcelBeansNPL.stream().distinct().collect(Collectors.groupingBy(SIServiceExcelBean::getServiceId));
				}

			// Looping ServiceDetail bean to get remaining attrs using filter - service ID
			// of each row.
			/**
			 * added the below code for passing all serviceId's and passing to a query instead of passing
			 * single serviceId in for each loop
			 */
			List<String> serviceIds= new ArrayList<>();
			serviceAttrGrouped.entrySet().forEach(map->map.getValue()
			.stream().filter(s -> s.getServiceId() != null).forEach(p -> {
				serviceIds.add(p.getServiceId());
			}));
			
			
			List<Map<String, Object>> lastMileLists = siServiceDetailRepository
					.querySIServiceViewForLastMile(serviceIds, productFamilyName);
			
			List<Map<String, Object>> attrvalLists = siServiceDetailRepository
					.fetchAttrValByServiceId(serviceIds);
			
			List<Map<String,Object>> aEndInterfaceDetls = siServiceDetailRepository.fetchAEndInterface(serviceIds);
			
			
			serviceAttrGrouped.entrySet().stream().forEach(map->map.getValue().parallelStream().forEach(p -> {
		
				List<Map<String, Object>> lastMileList= lastMileLists.parallelStream().
						filter(s->(s.get("mileServiceId").toString()).equalsIgnoreCase(p.getServiceId())).collect(Collectors.toList());
				
				if (lastMileList != null && !lastMileList.isEmpty()) {
					lastMileList.forEach(lastMile -> {
						if (lastMile.get(ServiceInventoryConstants.SRV_SITE_TYPE)
								.equals(ServiceInventoryConstants.SITE_A)) {

							if (lastMile.get(ServiceInventoryConstants.SRV_LASTMILE_BANDWIDTH) != null
									&& (!valueOf(lastMile.get(ServiceInventoryConstants.SRV_LASTMILE_BANDWIDTH))
											.equals(ServiceInventoryConstants.NULL_STR))) {
								p.setaEndllBandwidth(
										valueOf(lastMile.get(ServiceInventoryConstants.SRV_LASTMILE_BANDWIDTH)));
							}
						}
						if (lastMile.get(ServiceInventoryConstants.SRV_SITE_TYPE)
								.equals(ServiceInventoryConstants.SITE_B)) {
							if (lastMile.get(ServiceInventoryConstants.SRV_LASTMILE_BANDWIDTH) != null
									&& (!valueOf(lastMile.get(ServiceInventoryConstants.SRV_LASTMILE_BANDWIDTH))
											.equals(ServiceInventoryConstants.NULL_STR))) {
								p.setbEndLlBandwidth(
										valueOf(lastMile.get(ServiceInventoryConstants.SRV_LASTMILE_BANDWIDTH)));
							}
						}
					});
				}
				// DB call to si_service_attributes fetch values for passed attrs
				if (!productFamilyName.equals("GSIP")) {
					List<Map<String, Object>> attrvalList = attrvalLists.parallelStream().
							filter(s->(s.get("tpsServiceId").toString()).equalsIgnoreCase(p.getServiceId())).collect(Collectors.toList());
					if (attrvalList != null && !attrvalList.isEmpty()) {
						attrvalList.parallelStream().forEach(x -> {
							if (x.get(ServiceInventoryConstants.ATTRIBUTE_NAME) != null) {
								if (x.get(ServiceInventoryConstants.ATTRIBUTE_NAME)
										.equals(ServiceInventoryConstants.ROUTING_PROTOCOL)
										&& x.get(ServiceInventoryConstants.ATTRIBUTE_VAL) != null
										&& (!valueOf(x.get(ServiceInventoryConstants.ATTRIBUTE_VAL))
												.equals(ServiceInventoryConstants.NULL_STR))) {

									p.setRoutingProtocol(
											valueOf(x.get(ServiceInventoryConstants.ATTRIBUTE_VAL)));

								}
								if (x.get(ServiceInventoryConstants.ATTRIBUTE_NAME)
										.equals(ServiceInventoryConstants.B_END_INTERFACE)
										&& x.get(ServiceInventoryConstants.ATTRIBUTE_VAL) != null
										&& (!valueOf(x.get(ServiceInventoryConstants.ATTRIBUTE_VAL))
												.equals(ServiceInventoryConstants.NULL_STR))) {
									p.setbEndInterface(
											valueOf(x.get(ServiceInventoryConstants.ATTRIBUTE_VAL)));
								}
							}
						});
					}
					// Get A end interface..
					List<Map<String, Object>> aEndInterfaceDetl = aEndInterfaceDetls.parallelStream().
							filter(s->(s.get("tpsService").toString()).equalsIgnoreCase(p.getServiceId())).collect(Collectors.toList());
					
					if (aEndInterfaceDetl != null && !aEndInterfaceDetl.isEmpty()) {
						aEndInterfaceDetl.forEach(x -> {
							if ((x!=null) && x.get(ServiceInventoryConstants.A_END_INTERFACE) != null
									&& (!valueOf(x.get(ServiceInventoryConstants.A_END_INTERFACE))
											.equals(ServiceInventoryConstants.NULL_STR))) {
								p.setaEndInterface(valueOf(x.get(ServiceInventoryConstants.A_END_INTERFACE)));
							}

						});
					}
					
				}
			})); // end of main loop
			}

		} catch (Exception e) {
			LOGGER.error("Error in getProductFamilyDetails ",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		String[] columnsNonGSIP = {"AccountName", "Legal Entity", "ServiceType", "Product Flavor", "Order Id",
				"CustomerServiceId", "Alias", "Final Status", "CommissioningDate", "Service option type",
				"Scope of management", "Primary/Secondary", "service link", "Circuit_Bandwidth", "Service Topology",
				"Routing Protocol", "A End Interface", "B End Interface", "A_End_LAST_MILE_Provider",
				"A_END_LAST_MILE_BANDWIDTH", "B_End_LAST_MILE_Provider", "B_END_LAST_MILE_BANDWIDTH", "A_END_SITE_CITY",
				"A_END_SITE_ADDRESS", "B_END_SITE_CITY", "B_END_SITE_ADDRESS"};

		String[] columnsGSIP = { "AccountName", "Legal Entity", "Order ID","Alias", "Order Number", "Service Type",
				"End Customer Name", "Origin Country", "Origin City","Access Number", "Outpulse", "Origin Network",
				"Termination Country abbreviation","Acess Number Type", "Access Type", "Parent ID", "Parent Service", "Commissioning Date",
				"Service Status (blocked/unblocked)" };

		String[] columns = productFamilyName.equals("GSIP") ? columnsGSIP : columnsNonGSIP;

		// Create a Workbook
		Sheet sheet = workbook.createSheet(productFamilyName);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// context class obj for initializing row and rowCount
		Context context = new Context();

		// Create a Row
		Row headerRow = sheet.createRow(0);

		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}
		// Remove underprovision data from izo sdwan
		List<SIServiceExcelBean> filteredData = new ArrayList<>();
		serviceAttrGrouped.entrySet().parallelStream().forEach(map->map.getValue().stream().forEach(bean->{
			if(bean.getFinalStatus()!=null && bean.getServiceType() !=null && bean.getFinalStatus().equalsIgnoreCase(ServiceInventoryConstants.UNDER__PROVISIONING) &&  bean.getServiceType().equalsIgnoreCase(ServiceInventoryConstants.IZO_SDWAN)) {
				filteredData.add(bean);
			}
		}));
		serviceDetailExcelBeansNonGsip.removeAll(filteredData);	
		// Create cells
		if (productFamilyName.equals("GSIP")) {
			serviceDetailExcelBeansGSIP.stream().forEach(nextBean -> {
				if (nextBean.getAssetType()!=null && nextBean.getAssetType()
						.equals(OUTPULSE_EXCEL_ATTR)
						&& nextBean.getAssetName() != null
						&& (!valueOf(nextBean.getAssetName())
								.equals(ServiceInventoryConstants.NULL_STR))) {
					nextBean.setOutpulse(valueOf(nextBean.getAssetName()));
				}
				if (nextBean.getAssetType()!=null && nextBean.getAssetType()
						.equals(ServiceInventoryConstants.ASSET_TYPE_TFN)
						&& nextBean.getAssetName() != null
						&& (!valueOf(nextBean.getAssetName())
								.equals(ServiceInventoryConstants.NULL_STR))) {
					nextBean.setAccessNumber(valueOf(nextBean.getAssetName()));
				}
				context.rowCount++;
				context.row = sheet.createRow(context.rowCount);
				context.row.createCell(0).setCellValue(nextBean.getAccountName());
				context.row.createCell(1).setCellValue(nextBean.getLegalEntity());
				context.row.createCell(2).setCellValue(nextBean.getCustomerServiceID());
				context.row.createCell(3).setCellValue(nextBean.getAlias());
				context.row.createCell(4).setCellValue(nextBean.getOrderSysID());
				context.row.createCell(5).setCellValue(nextBean.getServiceType());
				context.row.createCell(6).setCellValue(nextBean.getEndCustName());
				context.row.createCell(7).setCellValue(nextBean.getSourceCountry());
				context.row.createCell(8).setCellValue(nextBean.getOriginCity());
				context.row.createCell(9).setCellValue(nextBean.getAccessNumber());
				context.row.createCell(10).setCellValue(nextBean.getOutpulse());
				context.row.createCell(11).setCellValue(nextBean.getOriginNetwork());
				context.row.createCell(12).setCellValue(nextBean.getDestCntryCode());
				context.row.createCell(13).setCellValue(nextBean.getAccessNumberType());
				context.row.createCell(14).setCellValue(nextBean.getAccessType());
				context.row.createCell(15).setCellValue(nextBean.getParentID());
				context.row.createCell(16).setCellValue(nextBean.getParentService());
				if (nextBean.getCommissionedDate() != null) { // Sun May 20 00:00:00 IST 2012
					Date dateStr = nextBean.getCommissionedDate();
					DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					context.row.createCell(17).setCellValue(formatter.format(dateStr));
				}
				context.row.createCell(18).setCellValue(nextBean.getFinalStatus());
			});
		} else {
			// Create Other rows and cells with product family data
			serviceAttrGrouped.entrySet().forEach(map->map.getValue()
			.stream().forEach(nextBean -> {
					context.rowCount++;
					context.row = sheet.createRow(context.rowCount);
					context.row.createCell(0).setCellValue(nextBean.getAccountName());
					context.row.createCell(1).setCellValue(nextBean.getLegalEntity());
					context.row.createCell(2).setCellValue(nextBean.getServiceType());
					context.row.createCell(3).setCellValue(nextBean.getPrdFlavour());
					context.row.createCell(4).setCellValue(nextBean.getOrderID());
					context.row.createCell(5).setCellValue(nextBean.getCustomerServiceID());
					context.row.createCell(6).setCellValue(nextBean.getAlias());
					context.row.createCell(7).setCellValue(nextBean.getFinalStatus());

					if (nextBean.getCommissionedDate() != null) { // Sun May 20 00:00:00 IST 2012
						Date dateStr = nextBean.getCommissionedDate();
						DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						context.row.createCell(8).setCellValue(formatter.format(dateStr));
					}
					context.row.createCell(9).setCellValue(nextBean.getServiceOptionType()); // from attribute name/val
					context.row.createCell(10).setCellValue(nextBean.getScopeOfManagement());
					context.row.createCell(11).setCellValue(nextBean.getPriSec());
					context.row.createCell(12).setCellValue(nextBean.getServiceLink());
					context.row.createCell(13).setCellValue(nextBean.getPortSpeed());
					context.row.createCell(14).setCellValue(nextBean.getServiceTopology());
					context.row.createCell(15).setCellValue(nextBean.getRoutingProtocol()); // from attribute name/val
					context.row.createCell(16).setCellValue(nextBean.getaEndInterface());// from attribute name/val
					context.row.createCell(17).setCellValue(nextBean.getbEndInterface());// from attribute name/val
					context.row.createCell(18).setCellValue(nextBean.getaEndLlProvider());
					context.row.createCell(19).setCellValue(nextBean.getaEndllBandwidth());
					context.row.createCell(20).setCellValue(nextBean.getbEndLlProvider());
					context.row.createCell(21).setCellValue(nextBean.getbEndLlBandwidth());
					context.row.createCell(22).setCellValue(nextBean.getSourceCity());
					
					 CellStyle style = workbook.createCellStyle(); //Create new style
		                style.setWrapText(true); //Set wordwrap
					context.row.createCell(23).setCellValue(nextBean.getSourceAddress());
					context.row.getCell(23).setCellStyle(style);
					context.row.createCell(24).setCellValue(nextBean.getDestinationCity());
					context.row.createCell(25).setCellValue(nextBean.getDestinationAddress());
					context.row.getCell(25).setCellStyle(style);
				
			}));
		}
	}
	
	/**
	 * 
	 * @param productFamilyRequest
	 * @param workbook
	 * @throws TclCommonException
	 */
	public void getProductFamilyDetails(ProductFamilyRequest productFamilyRequest, XSSFWorkbook workbook)
			throws TclCommonException {

		List<Map<String, Object>> data = null;
		try {
			Integer customerId = productFamilyRequest.getCustomerId();
			String accessType = productFamilyRequest.getAccessType();
			String legalEntity = productFamilyRequest.getLegalEntity();
			Integer productId = productFamilyRequest.getProductId();
			String serviceType = productFamilyRequest.getServiceType();
			Integer secsId = productFamilyRequest.getSecsId();

			StringBuilder query = new StringBuilder(
					"select distinct serviceinfo.order_customer as \"Account Name\",\r\n" + 
					"serviceinfo.order_cust_le_name as \"Legal Entity\",\r\n" + 
					"serviceinfo.secs_id as \"Secs\",\r\n" + 
					"serviceinfo.srv_service_id as \"Order Id\",\r\n" + 
					"pr.sub_variant as \"Service\",\r\n" + 
					"sa1.name \"Access Number\",\r\n" + 
					"serviceinfo.srv_access_type as \"Access Type\",\r\n" + 
					"serviceinfo.srv_source_country as \"Origin Country\",\r\n" + 
					"serviceinfo.srv_source_city as \"Origin City\",\r\n" + 
					"case when serviceinfo.srv_access_type = 'PSTN' then \r\n" +
					"serviceinfo.srv_destination_country \r\n" +
					"else '' end as \"Termination Country\", \r\n" +
					"sa.Name \"Outpulse\",\r\n" + 
					"sa.origin_ntwrk \"Origin Network\",\r\n" + 
					"case when (ss.service_commissioned_date is not null or ss.service_commissioned_date != '')\r\n" + 
					"then date_format(ss.service_commissioned_date,'DD-MM-YYYY') else ''\r\n" + 
					"end \"Commissioning Date\",\r\n" + 
					"srv_service_status as \"Service Status (blocked/unblocked)\"\r\n" + 
					"from service_inventory_uat_v4.vw_si_service_info_all serviceinfo,\r\n" + 
					"service_inventory_uat_v4.si_asset sa,\r\n" + 
					"service_inventory_uat_v4.si_asset sa1,\r\n" + 
					"service_inventory_uat_v4.si_service_detail ss\r\n" + 
					"left join service_inventory_uat_v4.si_product_reference pr\r\n" + 
					"			on pr.id = product_reference_id\r\n" + 
					"where serviceinfo.srv_service_id = ss.tps_service_id\r\n" + 
					"	  and sa.SI_service_detail_id = ss.id\r\n" + 
					"	  and sa1.SI_service_detail_id = ss.id\r\n" + 
					"	  and sa1.SI_service_detail_id = sa.SI_service_detail_id \r\n" + 
					"	  and sa.type = 'Outpulse'\r\n" + 
					"	  and sa1.`type`='Toll-Free'\r\n" + 
					"	  and order_customer_id ="+customerId+"\r\n" + 
					"	  and srv_product_family_id ="+productId+"\r\n");
			

			if (ServiceInventoryUtils.checkIfNotNull(legalEntity)) {
				query.append(" and serviceinfo.order_cust_le_name= '" + legalEntity + "'");
			}
			if (ServiceInventoryUtils.checkIfNotNull(accessType)) {
				query.append(" and serviceinfo.srv_access_type= '" + accessType + "'");
			}
			if (ServiceInventoryUtils.checkIfNotNull(accessType)) {
				query.append(" and serviceinfo.secs_id= '" + secsId + "'");
			}
			if (ServiceInventoryUtils.checkIfNotNull(serviceType)) {
				query.append(" and pr.sub_variant= '" + serviceType + "'");
			}

			LOGGER.info("query = {}", query.toString());
			
            data = jdbcTemplate.queryForList(query.toString());
			
			if (!(data.isEmpty())) {
                Sheet sheet = workbook.createSheet("GSIP");
                ((XSSFSheet) sheet).lockFormatColumns(false);
                int rowNum = 0;
                Row headerRow = sheet.createRow(rowNum++);
                int headingCol = 0;

                
                // Create a Font for styling header cells
    			Font headerFont = workbook.createFont();
    			headerFont.setBold(true);
    			headerFont.setFontHeightInPoints((short) 12);
    			
                // Create a CellStyle with the font
    			CellStyle headerCellStyle = workbook.createCellStyle();
    			headerCellStyle.setFont(headerFont);
                
                LOGGER.info("Setting XLS Header ");
                for (String key : data.get(0).keySet()) {
                       int autosizecolumn = headingCol++;
                       XSSFCell cell = (XSSFCell) headerRow.createCell(autosizecolumn);
                       cell.setCellValue(key);
                       cell.setCellStyle(headerCellStyle);
                       sheet.autoSizeColumn(autosizecolumn);
                }
                 LOGGER.info("Completed Setting XLS Header");
                
                LOGGER.info("Setting XLS Values ");
                for (int record = 0; record < data.size(); record++) {
                	ServiceInventoryUtils.replaceNullValues(data.get(record), "");
                       Row excelRow = sheet.createRow(rowNum++);
                       int dataCol = 0;
                       for (String key : data.get(0).keySet()) {
                              XSSFCell cell = (XSSFCell) excelRow.createCell(dataCol++);
                              cell.setCellValue(String.valueOf(data.get(record).get(key)));
                       }
                }
                LOGGER.info("Completed Setting XLS Values");
			}
			

		} catch (Exception e) {
			LOGGER.error("Error in getProductFamilyDetails ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Method to construct inventory excel for the given product List
	 * @param productNameList
	 * @param response
	 * @return HttpServletResponse
	 * @throws IOException
	 * @throws TclCommonException,TclCommonRuntimeException
	 */
	public HttpServletResponse constructInventoryExcel(List<String> productNameList, HttpServletResponse response)
			throws IOException, TclCommonException, TclCommonRuntimeException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		productNameList.forEach(x -> {
			try {
				LOGGER.info("Processing Report for {}",x);
				getProductFamilyDetails(x, workbook);
			} catch (Exception e) {
				LOGGER.error("Exception occured in constructing inventory excel for product : " + x,e);
			}
		});
		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		outArray = outByteStream.toByteArray();
		response.reset();
		response.setContentType("application/octet-stream");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + "productFamily" + ".xlsx" + "\"");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			try {
				outByteStream.flush();
				outByteStream.close();
			} catch (Exception e) {

			}
		}
		return response;
	}

	/**
	 * 
	 * @param productFamilyRequest
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 * @throws TclCommonRuntimeException
	 */
	public HttpServletResponse constructInventoryExcelDownload(ProductFamilyRequest productFamilyRequest,
			HttpServletResponse response) throws IOException, TclCommonException, TclCommonRuntimeException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			LOGGER.info("Processing Report for {}", productFamilyRequest);
			getProductFamilyDetails(productFamilyRequest, workbook);
		} catch (Exception e) {
			LOGGER.error("Exception occured in constructing inventory excel for product : " + productFamilyRequest, e);
		}
		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		outArray = outByteStream.toByteArray();
		response.reset();
		response.setContentType("application/octet-stream");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + "productFamily" + ".xlsx" + "\"");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			try {
				outByteStream.flush();
				outByteStream.close();
			} catch (Exception e) {

			}
		}
		return response;
	}

	public Map<String,String> getLRDetailsForIzoBasedOnServiceId(String serviceId) {
		Map<String,String> map = new HashMap<>();
		Optional<SIServiceDetail> siServiceDetail = siServiceDetailRepository.findByTpsServiceId(serviceId);
		if(siServiceDetail.isPresent()) {
			List<SIServiceAttribute> siServiceAttributes = siServiceAttributeRepository.findBySiServiceDetail(siServiceDetail.get());
			if(siServiceAttributes!=null && !siServiceAttributes.isEmpty()) {
				siServiceAttributes.stream().forEach(siAttri->{
					if(siAttri.getAttributeName().equals(SiServiceAttributeConstants.COS_MODEL) && siAttri.getAttributeValue()!=null) {
						map.put(SiServiceAttributeConstants.COS_MODEL,siAttri.getAttributeValue());
					}
					if(siAttri.getAttributeName().equals(SiServiceAttributeConstants.COS_PROFILE) && siAttri.getAttributeValue()!=null) {
						map.put(SiServiceAttributeConstants.COS_PROFILE,siAttri.getAttributeValue());
					}
					if(siAttri.getAttributeName().equals(SiServiceAttributeConstants.IPV4_STACK) && siAttri.getAttributeValue()!=null) {
						map.put(SiServiceAttributeConstants.IPV4_STACK,siAttri.getAttributeValue());
					}
					if(siAttri.getAttributeName().equals(SiServiceAttributeConstants.OUT_OF_CONTRACT) && siAttri.getAttributeValue()!=null) {
						map.put(SiServiceAttributeConstants.OUT_OF_CONTRACT,siAttri.getAttributeValue());
					}
					if(siAttri.getAttributeName().equals(SiServiceAttributeConstants.SCOPE_OF_MANAGEMENT) && siAttri.getAttributeValue()!=null) {
						map.put(SiServiceAttributeConstants.SCOPE_OF_MANAGEMENT,siAttri.getAttributeValue());
					}
		
				});
			}
			if(siServiceDetail.get().getServiceOption()!=null) {
				map.put(SiServiceAttributeConstants.SERVICE_OPTION, siServiceDetail.get().getServiceOption());
			}
			if(siServiceDetail.get().getSiteEndInterface()!=null) {
				map.put(SiServiceAttributeConstants.SITE_END_INTERFACE, siServiceDetail.get().getSiteEndInterface());
			}
			if(siServiceDetail.get().getVpnName()!=null) {
				map.put(SiServiceAttributeConstants.VPN_NAME, siServiceDetail.get().getVpnName());
			}
		}
		return map;

	}



	//Service Detailed View
	/**
	 * Method to get detailed SI info
	 *
	 * @param serviceId
	 * @param productName
	 * @param components
	 * @return
	 * @throws TclCommonException
	 */
	public SIServiceDetailedResponse getDetailedSIInfo(String serviceId, String productName, Boolean isTermination) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		if(isTermination == null) {
			LOGGER.info("isTermination flag is null, assuming false");
			isTermination = Boolean.FALSE;
		}
		String isMaster="";
		String masterServiceId="";
		SIServiceDetailedResponse response = new SIServiceDetailedResponse();
		Map<String,Object> primaryAttributes=new HashMap<>();
		Map<String,Object> secondaryAttributes=new HashMap<>();
		try {
			List<Map<String, Object>> details = null;
			if(Boolean.TRUE.equals(isTermination)) {
				details = siServiceInfoRepository.getServiceDetailAttributesByServiceIdLatest(serviceId, productName);

			} else {
			 details = siServiceInfoRepository.getServiceDetailAttributesByServiceId(serviceId, productName);
			}
			if (details == null || details.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			Optional<Map<String, Object>> serviceDetailInfo=details.stream().findFirst();
			if(serviceDetailInfo.isPresent())
			{
				Map<String, Object> serviceDetail=serviceDetailInfo.get();
				SISolutionDataOffering solution = new SISolutionDataOffering();
				Integer sysId = (Integer) serviceDetail.get("sys_id");
				response.setProductName(productName);
				response.setSolutions(solution);
				String linkType=(String) serviceDetail.get("pri_sec");
				String linkId=(String) serviceDetail.get("pri_sec_link");
				solution.setSiServiceDetailId(sysId);
				solution.setLinkType(linkType);
				if(Objects.nonNull(serviceDetail.get("erf_locationId"))) {
					solution.setLocationId(Integer.parseInt((String) serviceDetail.get("erf_locationId")));
				}
				
				Optional<SIServiceDetail> siServiceDetailOptional=siServiceDetailRepository.findById(sysId);
				if(siServiceDetailOptional.isPresent()){
					SIServiceDetail siServiceDetail=siServiceDetailOptional.get();
					List<Map<String, Object>> assetDetailInfos =null;
					if(siServiceDetail.getErfPrdCatalogProductName().contains("IAS") || siServiceDetail.getErfPrdCatalogProductName().contains("GVPN")){
						assetDetailInfos = siServiceInfoRepository.getAssetTypeDetailWithAttributes(sysId,"CPE");
					}else{
						assetDetailInfos = siServiceInfoRepository.getAssetDetailWithAttributes(sysId);
					}
					Optional<Map<String, Object>> assetDetailInfo=assetDetailInfos.stream().findFirst();
					Map<String, Object> assetDetail=new HashMap<>();
					if(assetDetailInfo.isPresent()) {
						assetDetail = assetDetailInfo.get();
					}
                   
					primaryAttributes=getFieldAttributes(serviceDetail,assetDetail,primaryAttributes);
	                SolutionAttributes primarySolutionAttributes=setServiceSolutionAttributes(sysId);
	                if (Objects.nonNull(primaryAttributes.get("Demo Type"))) {
						primarySolutionAttributes.setDemoType(primaryAttributes.get("Demo Type").toString());
					}
					if (Objects.nonNull(primaryAttributes.get("Demo Flag"))) {
						primarySolutionAttributes.setDemoFlag(primaryAttributes.get("Demo Flag").toString());
					}
					setDemarcDetails(primaryAttributes, primarySolutionAttributes);


					solution.setPrimary(primarySolutionAttributes);
					if(Objects.nonNull(linkType)&&(linkType.equalsIgnoreCase("PRIMARY")||linkType.equalsIgnoreCase("SECONDARY"))) {

						Integer secondarySysId = getServiceSysId(linkId, productName, isTermination);
						LOGGER.info("Secondary System Id "+ secondarySysId);
						if (Objects.nonNull(secondarySysId)) {
							SolutionAttributes secondarySolutionAttributes = setServiceSolutionAttributes(secondarySysId);
							if (Objects.nonNull(secondaryAttributes.get("Demo Type"))) {
								secondarySolutionAttributes.setDemoType(secondaryAttributes.get("Demo Type").toString());
							}
							if (Objects.nonNull(secondaryAttributes.get("Demo Flag"))) {
								secondarySolutionAttributes.setDemoFlag(secondaryAttributes.get("Demo Flag").toString());
							}

							setDemarcDetails(secondaryAttributes, secondarySolutionAttributes);
							solution.setSecondary(secondarySolutionAttributes);
							secondaryAttributes=getSecondaryFieldAttributes(linkId,productName);

							LOGGER.info("Demo flag and type for service id ---> {}  in SecondarySolutionAttributes is ----> {}  -----  {} ", secondarySolutionAttributes.getServiceId(),
									secondarySolutionAttributes.getDemoFlag(),secondarySolutionAttributes.getDemoType());
						}
					}

					LOGGER.info("Demo flag and type for service id ---> {}  in primarySolutionAttributes is ----> {}  -----  {} ", primarySolutionAttributes.getServiceId(),
							primarySolutionAttributes.getDemoFlag(),primarySolutionAttributes.getDemoType());

					solution=getAttributes(solution,linkType,linkId,productName,primaryAttributes,secondaryAttributes,details,assetDetailInfos);
					
					// added for multivrf macd gvpn set master and slave info
						LOGGER.info("MULTI VRF MACD before set master and slave info"
								+ productName+"service id"+siServiceDetail.getTpsServiceId()+"multi vrf flag"+siServiceDetail.getMultiVrfSolution());
					if (productName != null) {
						if (productName.equalsIgnoreCase("GVPN")) {
							if (siServiceDetail.getMultiVrfSolution() != null) {
								if (siServiceDetail.getMultiVrfSolution().equalsIgnoreCase("Yes")) {
									isMaster = getAttributesValues(siServiceDetail, CommonConstants.MASTER_VRF_FLAG);
									solution.setIsMultiVRF(siServiceDetail.getMultiVrfSolution());
									LOGGER.info("VRF MASTER FLAG" + isMaster);
									if (!isMaster.isEmpty()) {
										if (isMaster.equalsIgnoreCase("Yes")) {
											LOGGER.info("INSIDE master " + isMaster+"linkType:"+linkType);
											solution.setIsMasterVrf(true);
											solution=constructMultiVrfBean(solution,siServiceDetail,linkType,"Master");
											
										} else if (isMaster.equalsIgnoreCase("No")) {
											LOGGER.info("INSIDE slave " + isMaster);
											solution.setIsMasterVrf(false);
											masterServiceId = getAttributesValues(siServiceDetail,
													CommonConstants.MASTER_VRF_SERVICE_ID);
											LOGGER.info("INSIDE slave masterServiceId " + masterServiceId);
											if (!masterServiceId.isEmpty()) {
												Optional<SIServiceDetail> masterServiceDetail = siServiceDetailRepository
														.findByTpsServiceId(masterServiceId);
												if (masterServiceDetail.isPresent()) {
													solution=constructMultiVrfBean(solution,masterServiceDetail.get(),linkType,"Salve");
												}
											}

										}
									}
								}
							}
						}
					}
				}
			}
			return response;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	private void setDemarcDetails(Map<String, Object> primaryAttributes, SolutionAttributes primarySolutionAttributes) {
		primarySolutionAttributes.setDemarcFloor(primaryAttributes.get("demarcation_floor")!=null?primaryAttributes.get("demarcation_floor").toString():null);
		primarySolutionAttributes.setDemarcRack(primaryAttributes.get("demarcation_rack")!=null?primaryAttributes.get("demarcation_rack").toString():null);
		primarySolutionAttributes.setDemarcApartment(primaryAttributes.get("demarcation_apartment")!=null?primaryAttributes.get("demarcation_apartment").toString():null);
		primarySolutionAttributes.setDemarcRoom(primaryAttributes.get("demarcation_room")!=null?primaryAttributes.get("demarcation_room").toString():null);
	}

	/**
	 * Method to get detailed ServiceInventory info for NPL service. Addressed separately since it has A-End & B-End details for a single circuitId.
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public NPLSIServiceDetailedResponse getNPLDetailedSIInfo(String serviceId, Boolean isTermination) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		if(isTermination == null) {
			LOGGER.info("isTermination is null, assuming false");
			isTermination = Boolean.FALSE;
		}

		String productName = "NPL";
		NPLSIServiceDetailedResponse response = new NPLSIServiceDetailedResponse();
		List<NPLSISolutionDataOffering> siInfo = new ArrayList<>();

		try {
			List<Integer> sysIds = null;
			if(Boolean.TRUE.equals(isTermination)) {
				sysIds = siServiceInfoRepository.findLatestSysIdsForAServiceId(serviceId, productName);

			} else {
			sysIds = siServiceInfoRepository.findSysIdsForAServiceId(serviceId, productName);
			}
			final Boolean isTerm = isTermination;
			sysIds.stream().forEach(id -> {

				List<Map<String, Object>> details = Boolean.TRUE.equals(isTerm) ? siServiceInfoRepository.getLatestServiceDetailAttributesForNPL(serviceId,
						id) : siServiceInfoRepository.getServiceDetailAttributesForNPL(serviceId,
						id);
				if (details == null || details.isEmpty()) {
					throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT,
							ResponseResource.R_CODE_ERROR);
				}

				details.stream().forEach(serviceDetail -> {
					NPLSISolutionDataOffering solution = null;
					Map<String, Object> attributes = new HashMap<>();

					Integer sysId = (Integer) serviceDetail.get("sys_id");
					String linkType = (String) serviceDetail.get("pri_sec");
					
					Optional<NPLSISolutionDataOffering> solutions = siInfo.stream()
							.filter(si -> si.getServiceId().equals(sysId)).findFirst();
					if (solutions.isPresent()) {
						solution = solutions.get();
					} else {
						solution = new NPLSISolutionDataOffering();
						siInfo.add(solution);
					}
					
					NPLSolutionAttributes solutionAttributes = setNPLSolutionAttributes(sysId);
					solution.setAttributes(solutionAttributes);
					solution.setServiceId(sysId);
					solution.setLinkType(linkType);
					solution.setSiteType(solutionAttributes.getSiteType());
					solution.setSiteClassification((String) serviceDetail.get("site_classification"));

					List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository
							.getAssetDetailWithAttributes(sysId);
					Optional<Map<String, Object>> assetDetailInfo = assetDetailInfos.stream().findFirst();
					Map<String, Object> assetDetail = new HashMap<>();
					if (assetDetailInfo.isPresent()) {
						assetDetail = assetDetailInfo.get();
					}
					try {
						attributes = getFieldAttributes(serviceDetail, assetDetail, attributes);
						getAttributesForNPL(solution, attributes, details, assetDetailInfos);
					} catch (TclCommonException e) {
						LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
			response.setProductName(productName);
			response.setSiInfo(siInfo);
			return response;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Method to get secondary field attributes
	 *
	 * @param sysId
	 * @return
	 */
	private Map<String,Object> getSecondaryFieldAttributes(String serviceId,String productName)
	{
		Map<String,Object> secondaryAttributes=new HashMap<>();
		List<Map<String, Object>> data = siServiceInfoRepository.getServiceDetailAttributesByServiceId(serviceId, productName);
		if(data.stream().findFirst().isPresent()) {
			Map<String, Object> serviceDetail=data.stream().findFirst().get();
			List<Map<String, Object>> assetDetailInfos = new ArrayList<>();
			Integer sysId = (Integer) serviceDetail.get("sys_id");
			if(serviceDetail.get("product_name").equals("IAS") || serviceDetail.get("product_name").equals("GVPN")){
				assetDetailInfos = siServiceInfoRepository.getAssetTypeDetailWithAttributes(sysId,"CPE");
			}else{
				assetDetailInfos = siServiceInfoRepository.getAssetDetailWithAttributes(sysId);
			}

//			List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository.getAssetDetailWithAttributes(sysId);
			Optional<Map<String, Object>> assetDetailInfo=assetDetailInfos.stream().findFirst();
			Map<String, Object> assetDetail=new HashMap<>();
			if(assetDetailInfo.isPresent()) {
				assetDetail = assetDetailInfo.get();
			}
			secondaryAttributes=getFieldAttributes(serviceDetail,assetDetail,secondaryAttributes);
		}
		return secondaryAttributes;
	}

	/**
	 * Get Field Attributes
	 *
	 * @param serviceDetail
	 * @param assetDetail
	 * @param attributes
	 * @return
	 */
	private Map<String,Object> getFieldAttributes(Map<String, Object> serviceDetail,Map<String, Object> assetDetail,Map<String,Object> attributes) {
		String serviceOption=null;
		String ipAddressProvided=null;
		if (Objects.nonNull(serviceDetail)) {
			LOGGER.info("Inside getFieldAttributes method");
			LOGGER.info("serviceDetail attributes : {}",serviceDetail.get("LL_ARRANGE_BY"));
			Integer sysServiceId=(Integer) serviceDetail.get("sys_id");
			LOGGER.info("Service details for service ID -----> {} is ----> {} ", serviceDetail.get("service_id"), serviceDetail.toString());
			String demoFlag = (String) serviceDetail.get("demo_flag");
			String demoType = (String) serviceDetail.get("demo_type");
			String interfaceName = (String) serviceDetail.get("interface");
			String portBandwidth = (String) serviceDetail.get("port_bandwidth");
			String portBandwidthUnit = (String) serviceDetail.get("port_bandwidth_unit");
			String burstableBandwidth = (String) serviceDetail.get("burstable_bandwidth");
			String usageModel = (String) serviceDetail.get("usage_model");
			String ipAddressArrangementType = (String) serviceDetail.get("ipAddress_arrangement_type");
			String ipv4Size = (String) serviceDetail.get("ipv4_size");
			String ipv6Size = (String) serviceDetail.get("ipv6_size");
			String routingProtocol = (String) serviceDetail.get("routing_protocol");
			String lastmileBandwidth = (String) serviceDetail.get("lastmile_bandwidth");
			String lastmileBandwidthUnit = (String) serviceDetail.get("lastmile_bandwidth_unit");
			String backupConfiguration = (String) serviceDetail.get("backup_configuration");
			String serviceVarient = (String) serviceDetail.get("service_varient");
            String serviceType = (String) serviceDetail.get("service_type");
            String resiliency = (String) serviceDetail.get("resiliency_ind");
			ipAddressProvided = (String) serviceDetail.get("IP_address_provided_by");
			String additionalIp=(String) serviceDetail.get("additional_ip");
			serviceOption=(String)serviceDetail.get("service_option");
			String vpnTopology=(String) serviceDetail.get("vpnTopology");
			String siteType=(String) serviceDetail.get("siteType");
			String accessTopology=(String) serviceDetail.get("accessTopology");
			Double mrc = (Double) serviceDetail.get("mrc");
			Double nrc = (Double) serviceDetail.get("nrc");
			Double arc = (Double) serviceDetail.get("arc");
			String demarcationFloor = (String) serviceDetail.get("demarcation_floor");
			String demarcationRack= (String) serviceDetail.get("demarcation_rack");
			String demarcationRoom= (String) serviceDetail.get("demarcation_room");
			String demarcationApartement= (String) serviceDetail.get("demarcation_apartment");
			String classification=(String)serviceDetail.get("opportunity_type");

			String offeringName=(String) serviceDetail.get("offering_name");

			LOGGER.info("Demo Order flag and type for service id is---->{} ---- {} ---- {} ",serviceDetail.get("service_id"),demoFlag,demoType);

			portBandwidth=setBandwidthConversion(portBandwidth,portBandwidthUnit);
			if(Objects.nonNull(portBandwidthUnit)&&(portBandwidthUnit.equalsIgnoreCase("kbps")||portBandwidthUnit.equalsIgnoreCase("gbps")))
			portBandwidthUnit="mpbs";
			lastmileBandwidth=setBandwidthConversion(lastmileBandwidth,lastmileBandwidthUnit);
            if(Objects.nonNull(lastmileBandwidthUnit)&&(lastmileBandwidthUnit.equalsIgnoreCase("kbps")||lastmileBandwidthUnit.equalsIgnoreCase("gbps")))
			lastmileBandwidthUnit="mbps";

            attributes.put("Interface", interfaceName);
			attributes.put("Demo Flag", demoFlag);
			attributes.put("Demo Type", demoType);
			/*attributes.put("Port Bandwidth", portBandwidth + " " + portBandwidthUnit);*/
			attributes.put("Port Bandwidth", portBandwidth);
			attributes.put("Burstable Bandwidth", burstableBandwidth);
			attributes.put("Usage Model", usageModel);
			attributes.put("IP Address Arrangement", ipAddressArrangementType);
			attributes.put("IPv4 Address Pool Size", ipv4Size);
			attributes.put("IPv6 Address Pool Size", ipv6Size);
			attributes.put("Routing Protocol", routingProtocol);
			/*attributes.put("Local Loop Bandwidth", lastmileBandwidth + " " + lastmileBandwidthUnit);*/
			attributes.put("Local Loop Bandwidth", lastmileBandwidth);
			attributes.put("Service Variant", serviceVarient);
			attributes.put("Backup Configuration", backupConfiguration);
            attributes.put("Service type", serviceType);
            attributes.put("Resiliency", resiliency);
			attributes.put("IP Address Provided By", ipAddressProvided);
			attributes.put("Additional IPs",additionalIp);
			attributes.put("VPN Topology",vpnTopology);
			attributes.put("Site Type",siteType);
			attributes.put("Access Topology",accessTopology);
			attributes.put("mrc", mrc != null ? String.valueOf(mrc) : null);
			attributes.put("nrc", nrc != null ? String.valueOf(nrc) : null);
			attributes.put("arc", arc != null ? String.valueOf(arc) : null);

			attributes.put("demarcation_floor", Objects.nonNull(demarcationFloor) ? demarcationFloor : "NIL");
			attributes.put("demarcation_room" , Objects.nonNull(demarcationRoom) ? demarcationRoom : "NIL");
			attributes.put("demarcation_rack" , Objects.nonNull(demarcationRack) ? demarcationRack : "NIL");
			attributes.put("demarcation_apartment" , Objects.nonNull(demarcationApartement) ? demarcationApartement : "NIL");
			attributes.put("classification",classification);

			if(offeringName.equals("MMR Cross Connect")) {
				Optional<SIServiceAdditionalInfo> crossconnectAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(sysServiceId, "Cross Connect Type");
				Optional<SIServiceAdditionalInfo> mediaTypeAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(sysServiceId, "Media Type");
				Optional<SIServiceAdditionalInfo> noFiberAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(sysServiceId, "No. of Fiber pairs");
				Optional<SIServiceAdditionalInfo> fiberTypeAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(sysServiceId, "Fiber Type");
				Optional<SIServiceAdditionalInfo> fiberEntryNeededAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(sysServiceId, "Do you want Fiber Entry");
				String crossConnectType=null;
				String mediaType=null;
				String fiberEntryNeeded=null;
				String fiberEntryType=null;
				String fiberPairNumber=null;
				if(crossconnectAddInf.isPresent()) {
					crossConnectType = crossconnectAddInf.get().getAttributeValue();
				}
				if(mediaTypeAddInf.isPresent()) {
					mediaType = mediaTypeAddInf.get().getAttributeValue();
				}
				if(noFiberAddInf.isPresent()) {
					fiberPairNumber = noFiberAddInf.get().getAttributeValue();
				}
				if(fiberEntryNeededAddInf.isPresent()) {
					fiberEntryNeeded = fiberEntryNeededAddInf.get().getAttributeValue();
				}
				if(fiberTypeAddInf.isPresent()) {
					fiberEntryType = fiberTypeAddInf.get().getAttributeValue();
				}
				attributes.put("Cross Connect Type", crossConnectType != null ? String.valueOf(crossConnectType) : null);
				attributes.put("Media Type", mediaType != null ? String.valueOf(mediaType) : null);
				if (mediaType != null && mediaType.toUpperCase().contains("CABLE")) {
					attributes.put("No. of Cable pairs", fiberPairNumber != null ? String.valueOf(fiberPairNumber) : null);
				} else {
					attributes.put("No. of Fiber pairs", fiberPairNumber != null ? String.valueOf(fiberPairNumber) : null);
				}
				attributes.put("Do you want fiber entry", fiberEntryNeeded != null ? String.valueOf(fiberEntryNeeded) : null);
				attributes.put("Fiber Type", fiberEntryType != null ? String.valueOf(fiberEntryType) : null);
			}
			LOGGER.info("Demarc details for service Id ---> {}  is ---> {} ---> {} ----> {} ---> {}  ", serviceDetail.get("service_id"), demarcationApartement,demarcationFloor,demarcationRack, demarcationRoom);
		}

		if (Objects.nonNull(assetDetail)) {
			String cpeModel = (String) assetDetail.get("cpe_model");
			String scopeManagement = (String) assetDetail.get("scope_management");

			String cpeSupportType = (String) assetDetail.get("cpe_support_type");

			/*String wanIpAddress=(String) assetDetail.get("wan_ip_address");*/
			/*String wanIpProvider=(String) assetDetail.get("wan_ip_provider");*/
			attributes.put("CPE Basic Chassis", cpeModel);
			attributes.put("WAN IP Address"," ");
			attributes.put("WAN IP Provided By",ipAddressProvided);
			attributes.put("Connector Type","LC");

			if(Objects.nonNull(serviceOption)&&serviceOption.equalsIgnoreCase("Unmanaged"))
			{
				attributes.put("CPE Support Type", "Customer provided");
				attributes.put("CPE Management Type", "Unmanaged");
			}
			else {
				attributes.put("CPE Support Type", cpeSupportType);
				attributes.put("CPE Management Type", scopeManagement);
			}

		}
		LOGGER.info("Demo Order flag and type before returning 'attributes' for service id is  ---->  {} ----   {} ---- {} ",serviceDetail.get("service_id"),
				attributes.get("Demo Flag"),attributes.get("Demo Type"));
		LOGGER.info("CPE Basic Chassis -- in asset detail {}", attributes.get("CPE Basic Chassis"));
		return attributes;
	}

	/**
	 * Method to get attributes of primary and secondary
	 *
	 * @param linkType
	 * @param linkId
	 * @param productName
	 * @param primaryAttributes
	 * @param secondaryAttributes
	 * @param details
	 * @param assetDetailInfo
	 * @throws TclCommonException
	 */
	public SISolutionDataOffering getAttributes(SISolutionDataOffering solution, String linkType,String linkId,String productName,Map<String,Object> primaryAttributes,Map<String,Object> secondaryAttributes,List<Map<String, Object>> details,List<Map<String, Object>> assetDetailInfo) throws TclCommonException
	{
		Map<String,Object> attributesPrimary=new HashMap<>();
		Map<String,Object> attributesSecondary=new HashMap<>();

		if(Objects.nonNull(linkType)&&(linkType.equalsIgnoreCase("SINGLE")))
		{
			attributesPrimary.putAll(getPrimaryAttributes(primaryAttributes,details,assetDetailInfo));
		}
		else if(Objects.nonNull(linkType)&&(linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SECONDARY")))
		{
            attributesPrimary.putAll(getPrimaryAttributes(primaryAttributes,details,assetDetailInfo));
			attributesSecondary.putAll(getSecondaryAttributes(linkId,productName,secondaryAttributes));
		}
		/*components=populateAttributes(components,attributesPrimary,attributesSecondary);*/

		//to be changed
		solution.setPrimaryAttributes(constructAttributes(primaryAttributes));
		if(Objects.nonNull(linkType)&&(linkType.equalsIgnoreCase("PRIMARY")||linkType.equalsIgnoreCase("SECONDARY"))) {

			//to be changed

			solution.setSecondaryAttributes(constructAttributes(secondaryAttributes));
		}
		return solution;
	}

	/**
	 * Method to get attributes for NPL service.
	 * 
	 * @param solution
	 * @param attributes
	 * @param details
	 * @param assetDetailInfo
	 * @throws TclCommonException
	 */
	public void getAttributesForNPL(NPLSISolutionDataOffering solution, Map<String,Object> attributes, List<Map<String, Object>> details, List<Map<String, Object>> assetDetailInfo) throws TclCommonException
	{
		Map<String,Object> attributesPopulated = new HashMap<>();
		attributesPopulated.putAll(getAttributesMap(details, assetDetailInfo));
		solution.setAttributeDetail(constructAttributes(attributes));
	}
	
	/**
	 * Method to get attribute details
	 * @param attributes
	 * @return
	 */
	public List<AttributeDetail> constructAttributes(Map<String,Object> attributes)
	{
		List<AttributeDetail> attributeDetails=new ArrayList<>();
		if(Objects.nonNull(attributes)) {
			attributes.entrySet().stream().forEach(attributeElement -> {
				AttributeDetail attribute = new AttributeDetail();
				attribute.setName(attributeElement.getKey());
				attribute.setValue((String) attributeElement.getValue());
				attributeDetails.add(attribute);
			});
		}
		return attributeDetails;
	}

	/**
	 * Get SecondaryAttributes
	 * @param linkId
	 * @param attributes
	 * @return
	 */
	public Map<String,Object> getSecondaryAttributes(String linkId,String productName,Map<String,Object> attributes)
	{
		if(Objects.nonNull(linkId))
		{
			List<Map<String, Object>> servicedetails = siServiceInfoRepository.getServiceDetailAttributesByServiceId(linkId, productName);
			if(Objects.nonNull(servicedetails)&&!servicedetails.isEmpty()) {
				Optional<Map<String, Object>> serviceDetailInfo2 = servicedetails.stream().findFirst();
				if (serviceDetailInfo2.isPresent()) {
					Map<String, Object> detail = serviceDetailInfo2.get();
					Integer sysId = (Integer) detail.get("sys_id");
					List<Map<String, Object>> assetDetails = siServiceInfoRepository.getAssetDetailWithAttributes(sysId);
					if (assetDetails != null && !assetDetails.isEmpty()) {
						attributes.putAll(getAttributesMap(servicedetails, assetDetails));
					}


				}
			}
		}
		return attributes;
	}

	/**
	 * Method to get service system Id
	 * @param serviceId
	 * @param productName
	 * @return
	 */
	public Integer getServiceSysId(String serviceId, String productName, Boolean isTermination)
	{
		LOGGER.info("linkId" + serviceId);
		Integer serviceSysId=null;
		List<Map<String, Object>> servicedetails = null;
		if(Boolean.TRUE.equals(isTermination)) {
			servicedetails = siServiceInfoRepository.getServiceDetailAttributesByServiceIdLatest(serviceId, productName);
		} else {
		 servicedetails = siServiceInfoRepository.getServiceDetailAttributesByServiceId(serviceId, productName);
		}
		if(Objects.nonNull(servicedetails)&&!servicedetails.isEmpty()) {
			Optional<Map<String, Object>> serviceDetailInfo2 = servicedetails.stream().findFirst();
			if (serviceDetailInfo2.isPresent()) {
				Map<String, Object> detail = serviceDetailInfo2.get();
				serviceSysId = (Integer) detail.get("sys_id");
			}
		}
		return serviceSysId;
	}
	/**
	 * Method to set solution attributes
	 *
	 * @param solution
	 * @param detail
	 */
	public void setSolutionAttributes(SISolutionOffering solution,Map<String, Object> detail)
	{
		// hardcoded vales for testing - to be removed when actual data populated
		solution.setOfferingName((String) detail.get("offering_name"));
		solution.setSiteAddress((String) detail.get("site_address"));
		solution.setSiteAlias((String) detail.get("site_alias"));
		// solutions.setSlaCommittment((String) detail.get("srv_product_offering_name"));
		solution.setAccessType((String) detail.get("access_type"));
		// solutions.setAccessProvider((String) detail.get("srv_product_offering_name"));
		solution.setPrimaryOrSecondary((String) detail.get("pri_sec"));
		// solutions.setCpeProvider((String) detail.get("srv_product_offering_name"));
		// solutions.setLocation((String) detail.get("srv_product_offering_name"));
		solution.setLatLong((String) detail.get("lat_long"));
		// solutions.setUsageModel((String) detail.get("srv_product_offering_name"));
		solution.setServiceStatus((String) detail.get("service_status"));
		// solutions.setCommissioningDate((String) detail.get("srv_product_offering_name"));
		solution.setLastMileProvider((String) detail.get("lastmile_provider"));
		solution.setAccessProvider((String) detail.get("lastmile_provider"));


	}

	/**
	 * Method to get primary and secondary attributes
	 *
	 * @param linkId
	 * @param productName
	 * @param attributes1
	 * @param attributes2
	 * @param serviceDetailInfo1
	 * @param assetDetailInfo1
	 * @throws TclCommonException
	 */
	public Map<String,Object> getPrimaryAttributes(Map<String,Object> attributes,List<Map<String, Object>> serviceDetailInfo,List<Map<String, Object>> assetDetailInfo) throws TclCommonException
	{
		attributes.putAll(getAttributesMap(serviceDetailInfo,assetDetailInfo));
		return attributes;
	}



	/**
	 * Method to get attributes map from serviceInfo and assetInfo
	 *
	 * @param serviceDetailInfo
	 * @param assetDetailInfo
	 * @return
	 */
	public Map<String,Object> getAttributesMap(List<Map<String, Object>> serviceDetailInfo,List<Map<String, Object>> assetDetailInfo)
	{
		Map<String,Object> attributesMap=new HashMap<>();
		collectAttributes(attributesMap,serviceDetailInfo);
		collectAttributes(attributesMap,assetDetailInfo);
		return attributesMap;
	}


	/**
	 * Method to collect attributes
	 *
	 * @param attributesMap
	 * @param info
	 */
	public void collectAttributes(Map<String,Object> attributesMap,List<Map<String, Object>> info)
	{
		if(Objects.nonNull(info)) {
			info.stream().forEach(detail -> {
				String attributeName = (String) detail.get("attribute_name");
				String attributeValue = (String) detail.get("attribute_value");
				if (!("CPE Basic Chassis".equalsIgnoreCase(attributeName))) {
					if ((MACDConstants.COS_1.equalsIgnoreCase(attributeName) ||
							MACDConstants.COS_2.equalsIgnoreCase(attributeName) ||
							MACDConstants.COS_3.equalsIgnoreCase(attributeName) ||
							MACDConstants.COS_4.equalsIgnoreCase(attributeName) ||
							MACDConstants.COS_5.equalsIgnoreCase(attributeName) ||
							MACDConstants.COS_6.equalsIgnoreCase(attributeName)) && Objects.isNull(attributeValue)) {
						attributeValue = MACDConstants.ZERO_PERCENT;
					}
					attributesMap.put(attributeName, attributeValue);
				}});
		}
	}

	/**
	 * Method to populate attributes
	 *
	 * @param components
	 * @param primaryAttributes
	 * @param secondaryAttributes
	 */
	public List<ComponentBean> populateAttributes(List<ComponentBean> components,Map<String,Object> primaryAttributes, Map<String,Object> secondaryAttributes)
	{
		components.stream().forEach(component -> {
			if(Objects.nonNull(component.getType())&&component.getType().equalsIgnoreCase("PRIMARY"))
			{
				if(Objects.nonNull(primaryAttributes)&&!primaryAttributes.isEmpty()) {
					component.getAttributes().stream().forEach(attribute -> {
						LOGGER.info("ATTRIBUTE "+(String) primaryAttributes.get(attribute.getName()));
						attribute.setValue((String) primaryAttributes.get(attribute.getName()));
					});
				}
			}
			else
			{
				if(Objects.nonNull(secondaryAttributes)&&!secondaryAttributes.isEmpty()) {
					component.getAttributes().stream().forEach(attribute -> {
						attribute.setValue((String) secondaryAttributes.get(attribute.getName()));
					});
				}
			}


		});
		return components;
	}


	/**
	 * Method to set solution attributes
	 *
	 * @param solution
	 * @param detail
	 */
	public SolutionAttributes setServiceSolutionAttributes(Integer sysId)
	{
		final ObjectMapper mapper = new ObjectMapper();
		SolutionAttributes solutionAttributes=new SolutionAttributes();
		List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(sysId);
		if(data.stream().findFirst().isPresent()) {
			ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);

			solutionAttributes.setOfferingName(serviceDetail.getOfferingName());
			solutionAttributes.setSiteAddress(serviceDetail.getSiteAddress());
			solutionAttributes.setSiteAlias(serviceDetail.getAlias());
			solutionAttributes.setAccessType(serviceDetail.getAccessType());
			solutionAttributes.setLatLong(serviceDetail.getLatLong());
			solutionAttributes.setBillingAccountId(serviceDetail.getBillingAccountId());
			solutionAttributes.setBillingAddress(serviceDetail.getBillingAddress());
			solutionAttributes.setBillingGstNumber(serviceDetail.getBillingGstNumber());
			solutionAttributes.setContractEndDate(serviceDetail.getContractEndDate());
			solutionAttributes.setContractStartDate(serviceDetail.getContractStartDate());
			solutionAttributes.setLeId(serviceDetail.getLeId());
			solutionAttributes.setLeName(serviceDetail.getLeName());
			solutionAttributes.setSupplierLeId(serviceDetail.getSupplierLeId());
			solutionAttributes.setSupplierLeName(serviceDetail.getSupplierLeName());
			solutionAttributes.setTaxExemptionFlag(serviceDetail.getTaxExemptionFlag());
			solutionAttributes.setSiServiceDetailId(serviceDetail.getSiServiceDetailId());
			solutionAttributes.setSiOrderId(serviceDetail.getSiOrderId());
			solutionAttributes.setTermInMonths(serviceDetail.getTermInMonths());
			solutionAttributes.setPrimaryServiceId(serviceDetail.getPrimaryServiceId());
			solutionAttributes.setSecondaryServiceId(serviceDetail.getSecondaryServiceId());
			solutionAttributes.setServiceStatus(serviceDetail.getServiceStatus());
			solutionAttributes.setLastMileProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setAccessProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setProductId(serviceDetail.getProductId());
			solutionAttributes.setServiceId(serviceDetail.getServiceId());
			solutionAttributes.setIpAddressProvidedBy(serviceDetail.getIpAddressProvidedBy());
			solutionAttributes.setCustomerId(serviceDetail.getCustomerId());
			solutionAttributes.setVpnName(serviceDetail.getVpnName());
			solutionAttributes.setServiceTopology(serviceDetail.getServiceTopology());
			solutionAttributes.setSiteLocationId(serviceDetail.getSiteLocationId());
			solutionAttributes.setPortSpeed(serviceDetail.getPortSpeed());
			solutionAttributes.setPortSpeedUnit(serviceDetail.getPortSpeedUnit());
			solutionAttributes.setShowCosMessage(serviceDetail.getShowCosMessage());
			solutionAttributes.setCircuitExpiryDate(serviceDetail.getCircuitExpiryDate());
			solutionAttributes.setAssociateBillableId(serviceDetail.getAssociateBillableId());

            solutionAttributes.setPortSpeed(setBandwidthConversion(solutionAttributes.getPortSpeed(),solutionAttributes.getPortSpeedUnit()));
            if(Objects.nonNull(solutionAttributes.getPortSpeedUnit())&&(solutionAttributes.getPortSpeedUnit().equalsIgnoreCase("kbps")||solutionAttributes.getPortSpeedUnit().equalsIgnoreCase("gbps")))
                solutionAttributes.setPortSpeedUnit("mpbs");

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date=serviceDetail.getCommissionedDate();
			if(Objects.nonNull(date)) {
				String strDate = dateFormat.format(date);
				solutionAttributes.setCommissioningDate(strDate);
			}
			List<Map<String, Object>> localItContacts=siServiceContactRepository.findBySiServiceDetail_Id(serviceDetail.getSiServiceDetailId());

			Optional<Map<String, Object>> serviceDetailInfo=localItContacts.stream().findFirst();
			if(serviceDetailInfo.isPresent()) {
				Map<String, Object> detail = serviceDetailInfo.get();
					String contactName=(String)detail.get("contactName");
					String contactType=(String) detail.get("contactType");
					String businessEmail=(String)detail.get("businessEmail");
					String businessPhone=(String)detail.get("businessMobile");
					if(Objects.nonNull(contactType)&&contactType.equalsIgnoreCase("LocalITContact")) {
						solutionAttributes.setLocalItPhoneno(businessPhone);
						solutionAttributes.setLocalItName(contactName);
						solutionAttributes.setLocalItEmail(businessEmail);
					}
			}


		}
		return solutionAttributes;

	}

	/**
	 * @author chetchau
	 * this method is used to update data service details
	 * @param serviceId
	 * @param updateDataServiceBean
	 * @throws TclCommonException
	 * @return
	 */
	public RestResponse updateDataServiceDetails(String serviceId, UpdateDataServiceBean updateDataServiceBean) throws TclCommonException {
		RestResponse response = null;	
		Objects.requireNonNull(serviceId, "serviceId cannot be null");
		try {
			response = restClientService.patch(baseUrl + requestUrl + "/" + "circuits" + "/" + serviceId,
					Utils.convertObjectToJson(updateDataServiceBean),
					getBasicAuth(appId, appSecret, getHeader(), null));

		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.TOLLFREE_SERVICE_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return response;
	}
	/**
	 * @author chetan chaudhary
	 * This method is used to update toll free service alias filters
	 * @param orderId
	 * @param updateTollFreeServiceBean
	 * @throws TclCommonException
	 * @return
	 * @
	 */
	public RestResponse updateTollFreeServiceDetails(String orderId, UpdateTollFreeServiceBean updateTollFreeServiceBean) throws TclCommonException {
		RestResponse response =null;
		Objects.requireNonNull(orderId, "orderId cannot be null");
		String url = baseUrl + requestUrl + "/" + "numbers" + "/" + orderId;

		try {
			response = restClientService.patch(url, Utils.convertObjectToJson(updateTollFreeServiceBean),
					getBasicAuth(appId, appSecret, getHeader(), null));

		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.TOLLFREE_SERVICE_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return response;

	}
	/**
	 * @author chetan chaudhary
	 * this method is used to update the DIDservice alias filter
	 * @param DIDServiceId
	 * @param updateDIDServiceBean
	 * @throws TclCommonException
	 * @return
	 */
	public RestResponse updateDidServiceDetails(String DIDServiceId, UpdateDIDServiceBean updateDIDServiceBean) throws TclCommonException{
		RestResponse restResponse = null;
		Objects.requireNonNull(DIDServiceId,"DID Service ID cannot be null");
		try {
			restResponse = restClientService.patch(baseUrl + requestUrl + "/" + "DIDNumbers" + "/" + DIDServiceId,
					Utils.convertObjectToJson(updateDIDServiceBean), getBasicAuth(appId, appSecret, getHeader(), null));
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.TOLLFREE_SERVICE_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return restResponse;
	}
	
	/**
	 * getAuthorizatinHeader used to have the header details
	 * 
	 * @return
	 */
	private HashMap<String, String> getHeader() {

		HashMap<String, String> authorizationHeader = new HashMap<>();
		authorizationHeader.put("Accept", "application/json");
		authorizationHeader.put("Content-Type", "application/json");

		return authorizationHeader;
	}

	/**
	 * @author vivek used to get the basic auth with encoded form getBasicAuth
	 * @param appId
	 * @param appSecret
	 * @return
	 * 
	 * 
	 */


	/* need to pass login user id */
	private HttpHeaders getBasicAuth(String appId, String appSecret, Map<String, String> contentTypes,
			String YAuthUser) {

		HttpHeaders headers = new HttpHeaders();
		if (appId != null && appSecret != null) {
			String auth = appId + ":" + appSecret;
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.set("Authorization", authHeader);
		}
		if (YAuthUser != null) {
			headers.set("YAuthorization", YAuthUser);

		} else {
			headers.set("YAuthorization", "optimus_inventurus@legomail.com");
		}
		if (contentTypes != null && !contentTypes.isEmpty()) {

			contentTypes.forEach((key, value) -> {
				headers.set(key, value);

			});
		}
		return headers;

	}

	/**
	 * Method to set macd flag
	 *
	 * @param serviceDetailBeans
	 * @return
	 * @throws TclCommonException
	 */
	public List<ServiceDetailBean> setMacdFlag(List<ServiceDetailBean> serviceDetailBeans)throws TclCommonException
	{

		Map<String,String> serviceIds=new HashMap<>();
		serviceDetailBeans.stream().forEach(serviceDetailBean ->{
			if(serviceDetailBean.getLinkType()!=null && !serviceDetailBean.getLinkType().equalsIgnoreCase("single"))
				serviceIds.put(serviceDetailBean.getServiceId(),serviceDetailBean.getSecondaryServiceId());
			else
				serviceIds.put(serviceDetailBean.getServiceId(),"NIL");
			});
		Map<String,Object> macdFlags=getMacdFlags(serviceIds);
		if(!macdFlags.isEmpty()) {
			serviceDetailBeans.stream().forEach(serviceDetailBean -> {
				Boolean macdFlag=false;
				Boolean secMacdFlag=false;
				
				if(serviceDetailBean.getLinkType() == null || serviceDetailBean.getLinkType().isEmpty())
					return;
					
				if(serviceDetailBean.getLinkType().equalsIgnoreCase("single")) {
					 macdFlag = (Boolean) macdFlags.get(serviceDetailBean.getServiceId());
				}
				else {
					 macdFlag = (Boolean) macdFlags.get(serviceDetailBean.getServiceId());
					 secMacdFlag = (Boolean) macdFlags.get(serviceDetailBean.getSecondaryServiceId());
				}

					if (Objects.nonNull(macdFlag) && macdFlag||Objects.nonNull(secMacdFlag) && secMacdFlag)
						serviceDetailBean.setIsMacdInitiated(true);

			});
		}
		return serviceDetailBeans;
	}

	/**
	 * Method to reset bandwidth as per business rule
	 *
	 * @param bean
	 */
	/*private void resetBandwidth(SIServiceDetailDataBean bean)
	{
		String portBandwidthUnit=null;
		String lastmileBandwidthUnit=null;
		String bandwidthUnit=bean.getLastmileBwUnit();
		String portBandwidth=bean.getPortBw();
		String lastmileBandwidth=bean.getLastmileBw();

        portBandwidth=setBandwidthConversion(portBandwidth,portBandwidthUnit);
        if(Objects.nonNull(portBandwidthUnit)&&(portBandwidthUnit.equalsIgnoreCase("kbps")||portBandwidthUnit.equalsIgnoreCase("gbps")))
            portBandwidthUnit="mpbs";
        lastmileBandwidth=setBandwidthConversion(lastmileBandwidth,lastmileBandwidthUnit);
        if(Objects.nonNull(lastmileBandwidthUnit)&&(lastmileBandwidthUnit.equalsIgnoreCase("kbps")||lastmileBandwidthUnit.equalsIgnoreCase("gbps")))
            lastmileBandwidthUnit="mbps";
		bean.setPortBw(portBandwidth);
		bean.setLastmileBw(lastmileBandwidth);
		bean.setLastmileBwUnit(lastmileBandwidthUnit);
	}*/
	
	/**
	 * Method to set solution attributes for NPL service.
	 *
	 * @param sysId
	 * @return {@NPLSolutionAttributes}
	 */
	public NPLSolutionAttributes setNPLSolutionAttributes(Integer sysId) {
		final ObjectMapper mapper = new ObjectMapper();
		NPLSolutionAttributes solutionAttributes = new NPLSolutionAttributes();
		List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(sysId);
		if (data.stream().findFirst().isPresent()) {
			ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(),
					ServiceDetailBean.class);

			solutionAttributes.setOfferingName(serviceDetail.getOfferingName());
			solutionAttributes.setSiteAddress(serviceDetail.getSiteAddress());
			solutionAttributes.setSiteAlias(serviceDetail.getAlias());
			solutionAttributes.setAccessType(serviceDetail.getAccessType());
			solutionAttributes.setLatLong(serviceDetail.getLatLong());
			solutionAttributes.setBillingAccountId(serviceDetail.getBillingAccountId());
			solutionAttributes.setBillingAddress(serviceDetail.getBillingAddress());
			solutionAttributes.setBillingGstNumber(serviceDetail.getBillingGstNumber());
			solutionAttributes.setContractEndDate(serviceDetail.getContractEndDate());
			solutionAttributes.setContractStartDate(serviceDetail.getContractStartDate());
			solutionAttributes.setLeId(serviceDetail.getLeId());
			solutionAttributes.setLeName(serviceDetail.getLeName());
			solutionAttributes.setSupplierLeId(serviceDetail.getSupplierLeId());
			solutionAttributes.setSupplierLeName(serviceDetail.getSupplierLeName());
			solutionAttributes.setTaxExemptionFlag(serviceDetail.getTaxExemptionFlag());
			solutionAttributes.setSiServiceDetailId(serviceDetail.getSiServiceDetailId());
			solutionAttributes.setSiOrderId(serviceDetail.getSiOrderId());
			solutionAttributes.setTermInMonths(serviceDetail.getTermInMonths());
			solutionAttributes.setPrimaryServiceId(serviceDetail.getPrimaryServiceId());
			solutionAttributes.setSecondaryServiceId(serviceDetail.getSecondaryServiceId());
			solutionAttributes.setServiceStatus(serviceDetail.getServiceStatus());
			solutionAttributes.setLastMileProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setAccessProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setProductId(serviceDetail.getProductId());
			solutionAttributes.setServiceId(serviceDetail.getServiceId());
			solutionAttributes.setIpAddressProvidedBy(serviceDetail.getIpAddressProvidedBy());
			solutionAttributes.setCustomerId(serviceDetail.getCustomerId());
			solutionAttributes.setVpnName(serviceDetail.getVpnName());
			solutionAttributes.setServiceTopology(serviceDetail.getServiceTopology());
			solutionAttributes.setSiteLocationId(serviceDetail.getSiteLocationId());
			solutionAttributes.setPortSpeed(serviceDetail.getPortSpeed());
			solutionAttributes.setPortSpeedUnit(serviceDetail.getPortSpeedUnit());
			solutionAttributes.setCircuitSpeed(serviceDetail.getPortSpeed());
			solutionAttributes.setSiteType(serviceDetail.getSiteType());
			solutionAttributes.setAssociateBillableId(serviceDetail.getAssociateBillableId());
			solutionAttributes.setCircuitExpiryDate(serviceDetail.getCircuitExpiryDate());
			// solutionAttributes.setChargeType(chargeType);
			// solutionAttributes.setNetworkProtection(networkProtection);

			if (Objects.nonNull(solutionAttributes.getPortSpeedUnit())
					&& solutionAttributes.getPortSpeedUnit().equalsIgnoreCase("kbps")) {
				if (Objects.nonNull(solutionAttributes.getPortSpeed())) {
					switch (solutionAttributes.getPortSpeed().trim()) {
					case "512": {
						solutionAttributes.setPortSpeed("0.5");
						solutionAttributes.setPortSpeedUnit("mpbs");
						break;
					}
					case "256": {
						solutionAttributes.setPortSpeed("0.25");
						solutionAttributes.setPortSpeedUnit("mpbs");
						break;
					}
					case "1024": {
						solutionAttributes.setPortSpeed("1");
						solutionAttributes.setPortSpeedUnit("mpbs");
					}
					default:
						break;
					}
				}
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = serviceDetail.getCommissionedDate();
			if (Objects.nonNull(date)) {
				String strDate = dateFormat.format(date);
				solutionAttributes.setCommissioningDate(strDate);
			}

			List<Map<String, Object>> localItContacts = siServiceContactRepository
					.findBySiServiceDetail_Id(serviceDetail.getSiServiceDetailId());
			Optional<Map<String, Object>> serviceDetailInfo = localItContacts.stream().findFirst();
			if (serviceDetailInfo.isPresent()) {
				Map<String, Object> detail = serviceDetailInfo.get();
				String contactName = (String) detail.get("contactName");
				String contactType = (String) detail.get("contactType");
				String businessEmail = (String) detail.get("businessEmail");
				String businessPhone = (String) detail.get("businessMobile");
				if (Objects.nonNull(contactType) && "LocalITContact".equalsIgnoreCase(contactType)) {
					solutionAttributes.setLocalItPhoneno(businessPhone);
					solutionAttributes.setLocalItName(contactName);
					solutionAttributes.setLocalItEmail(businessEmail);
				}
			}
		}
		return solutionAttributes;
	}
	
    /**
     * Method to do bandwidth conversion
     *
     * @param bandwidth
     * @param bandwidthUnit
     * @return
     */
	private String setBandwidthConversion(String bandwidth, String bandwidthUnit)
    {
        Double bandwidthValue=0D;
        if(Objects.nonNull(bandwidth)&&Objects.nonNull(bandwidthUnit))
        {
            switch (bandwidthUnit.trim().toLowerCase())
            {
                case "kbps": {
                    bandwidthValue = Double.parseDouble(bandwidth.trim());
                    bandwidthValue = bandwidthValue / 1024;
                    bandwidth = bandwidthValue.toString();
                    break;
                }
                case "gbps": {
                    bandwidthValue = Double.parseDouble(bandwidth.trim());
                    bandwidthValue = bandwidthValue * 1000;
                    bandwidth = bandwidthValue.toString();
                    break;
                }
                default: {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidth = bandwidthValue.toString();
					break;
				}
            }

            int index=bandwidth.indexOf(".");
            if(index>0) {
				LOGGER.info("bandwidth value" + bandwidth);
				String precisions = bandwidth.substring(index + 1);
				LOGGER.info("precision value" + precisions);
				if (precisions.length() > 3) {
					DecimalFormat df = new DecimalFormat("#.###");
					df.setRoundingMode(RoundingMode.CEILING);
					String value = df.format(bandwidthValue);
					LOGGER.info("Formatted value" + value);
					bandwidth = value;
				} else {
					if ("0".equalsIgnoreCase(precisions)) {

						bandwidth = valueOf(bandwidthValue.intValue());
					}
				}
			}
        }

        return bandwidth;
    }
	
	 /**
     * Method to get distinct Customercuid's based on partner Id
     *
     * @param partnerId
     *  
     * @return List<String>
     */
	public List<String> getCustomerCUIDByPartnerId(Integer partnerId){
        LOGGER.info("Method getCustomerCUIDByPartnerId partnerId "+partnerId);
		List<String> customerCuids=new ArrayList<>();
		customerCuids=siOrderRepository.findDistinctCustomerCUIDByPartnerId(partnerId);
		LOGGER.info("Method customerCUID partnerId "+customerCuids);
		return customerCuids;
	}
	
	/**
     * getIPCSIAssetDetails - Method is used to get the list of IPC service inventory assets by serviceId.
     *
     * @param serviceId
     * @return List<SIServiceAssetComponentBean>
     */
	@Transactional
	public List<SIServiceAssetComponentBean> getIPCSIAssetDetails(String serviceId) throws TclCommonException {
		List<SIServiceAssetComponentBean> serviceAssetComponentBeanList =null;
		try{
			if (StringUtils.isBlank(serviceId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			List<String> nameList= new ArrayList<>();
			nameList.add("Access");
			nameList.add("IPC addon");
			List<VwOrderServiceAssetInfo> vwOrderList=vwOrderServiceAssetInfoRepository.findByServiceIdAndStatusNotAndNameIn(serviceId,"Terminated",nameList);
			if(Objects.nonNull(vwOrderList) && !vwOrderList.isEmpty()){
				LOGGER.info("Assets for given service Id exists");			
				String vwOrderJson= Utils.convertObjectToJson(vwOrderList);
				serviceAssetComponentBeanList = Arrays.asList(new ObjectMapper().readValue(vwOrderJson, SIServiceAssetComponentBean[].class));
				LOGGER.info("Process component bean"+serviceAssetComponentBeanList.size());
				Map<String,Integer> assetIDTypeMap = new HashMap<>();
				Map<String,SIServiceAssetComponentBean> assetTypeComponentBeanMap= new HashMap<>();
				for(SIServiceAssetComponentBean siServiceAssetComponentBean:serviceAssetComponentBeanList){
					LOGGER.info("Asset Id::"+siServiceAssetComponentBean.getAssetId());
					assetIDTypeMap.put(siServiceAssetComponentBean.getName(), siServiceAssetComponentBean.getAssetId());
					assetTypeComponentBeanMap.put(siServiceAssetComponentBean.getName(),siServiceAssetComponentBean);
				}
				//Get access details 
				getAccessDetails(assetIDTypeMap,assetTypeComponentBeanMap);
				//Get addOn details
				getAddOnDetails(assetIDTypeMap,assetTypeComponentBeanMap);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in getting detailed IPCSIAssetDetails for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return serviceAssetComponentBeanList;
	}

	private void getAddOnDetails(Map<String, Integer> assetIDTypeMap,
			Map<String, SIServiceAssetComponentBean> assetTypeComponentBeanMap) {
		LOGGER.info("GetAddOnDetails method invoked");
		Integer addOnAssetId=assetIDTypeMap.get("IPC addon");
		List<String> categories=siAssetComponentRepository.getAssetDetailsBasedOnServiceDetailIds(addOnAssetId);
		categories.add(ServiceInventoryConstants.IPC_ASSET_CATEGORY_VPN_CONNECTION);
		categories.add(ServiceInventoryConstants.IPC_ASSET_CATEGORY_MANAGED);
		Optional<SIAsset> siAddOnAsset=siAssetRepository.findById(addOnAssetId);
		List<SIAssetAttribute> siAssetAttrList=siAssetAttributeRepository.findBySiAssetAndCategoryIn(siAddOnAsset.get(), categories);
		Map<String,SIAssetComponentBean> siAssetComponentMap= new HashMap<>();
		for(SIAssetAttribute siAssetAttribute:siAssetAttrList){
			SIAssetComponentBean siAssetComponentBean = null;
			if(siAssetComponentMap.get(siAssetAttribute.getCategory())!=null){
				siAssetComponentBean = siAssetComponentMap.get(siAssetAttribute.getCategory());
				siAssetComponentBean.setCategory(siAssetAttribute.getCategory());
				siAssetComponentBean.setName(siAssetComponentBean.getName().concat(",").concat(siAssetAttribute.getAttributeName()));
				siAssetComponentBean.setValue(siAssetComponentBean.getValue().concat(",").concat(siAssetAttribute.getAttributeValue()));
				siAssetComponentMap.put(siAssetAttribute.getCategory(), siAssetComponentBean);
			}else{
				siAssetComponentBean = new SIAssetComponentBean();
				siAssetComponentBean.setCategory(siAssetAttribute.getCategory());
				siAssetComponentBean.setName(siAssetAttribute.getAttributeName());
				siAssetComponentBean.setValue(siAssetAttribute.getAttributeValue());
			}
			siAssetComponentMap.put(siAssetAttribute.getCategory(), siAssetComponentBean);
		}
		SIAssetCommercial siAssetCommercial=siAssetCommercialRepository.findBySiAsset(siAddOnAsset.get());
		List<SIAssetComponentBean> addOnList= new ArrayList<>();
		if(Objects.nonNull(siAssetCommercial) && Objects.nonNull(siAssetCommercial.getSiAssetComponents())){
			for(SIAssetComponent siAssetComponent:siAssetCommercial.getSiAssetComponents()){
				SIAssetComponentBean siAssetComponentBean = siAssetComponentMap.get(siAssetComponent.getItem());
				siAssetComponentBean.setCategory(siAssetComponent.getItem());
				siAssetComponentBean.setArc(siAssetComponent.getArc());
				siAssetComponentBean.setMrc(siAssetComponent.getMrc());
				siAssetComponentBean.setNrc(siAssetComponent.getNrc());
				addOnList.add(siAssetComponentBean);
			}
		}
		
		//Set AddOn details
		assetTypeComponentBeanMap.get("IPC addon").setSiAssetComponentList(addOnList);
		LOGGER.info("GetAddOnDetails method ends");
	}

	private void getAccessDetails(Map<String,Integer> assetIDTypeMap,Map<String,SIServiceAssetComponentBean> assetTypeComponentBeanMap) {
		LOGGER.info("GetAccessDetails method invoked");
		Integer accessAssetId=assetIDTypeMap.get("Access");
		LOGGER.info("AssetID::"+accessAssetId);
		Optional<SIAsset> siAccessAsset=siAssetRepository.findById(accessAssetId);
		List<SIAssetComponentBean> accessList= new ArrayList<>();
		SIServiceAssetComponentBean siServiceAssetComponentBean=assetTypeComponentBeanMap.get("Access");
		if(siAccessAsset.isPresent()){
			LOGGER.info("Asset exists");
			Set<SIAssetAttribute> siAssetAttributes=siAccessAsset.get().getSiAssetAttributes();
			SIAssetComponentBean siAssetComponentBean = new SIAssetComponentBean();
			for(SIAssetAttribute siAssetAttribute:siAssetAttributes){
				if(siAssetAttribute.getAttributeName().equals("accessOption")){
					siAssetComponentBean.setName(siAssetAttribute.getAttributeValue());
				}else if(!siAssetAttribute.getAttributeName().equals("accessOption") && !siAssetAttribute.getAttributeValue().isEmpty()){
					siAssetComponentBean.setCategory(siAssetAttribute.getCategory());
					siAssetComponentBean.setValue(siAssetAttribute.getAttributeValue());
				}
				siAssetComponentBean.setArc(siServiceAssetComponentBean.getArc());
				siAssetComponentBean.setMrc(siServiceAssetComponentBean.getMrc());
				siAssetComponentBean.setNrc(siServiceAssetComponentBean.getNrc());
			}
			accessList.add(siAssetComponentBean);
		}
		//Set Access details
		siServiceAssetComponentBean.setSiAssetComponentList(accessList);
		LOGGER.info("GetAccessDetails method ends");
	}
	/**
	 * Method to persist fulfillment data to service inventory
	 *
	 * @param ScOrderBean scOrderBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void processServiceInventoryData(ScOrderBean scOrderBean) {
		LOGGER.info("processServiceInventoryData method invoked");
		if(scOrderBean.getOpOrderCode().startsWith("IPC")){
			mapScOrderDetailsToSIEntity(scOrderBean);
		}
		LOGGER.info("processServiceInventoryData method ends");
	}
	
	private void addExistingOrderAttributes(SIOrder currentSiOrderEntity,Set<SIOrderAttribute> siOrderAttrs, SIOrder prevSiOrder,List<String> orderAttrs) {
		prevSiOrder.getSiOrderAttributes().stream().
				filter(prevSIOrderAttribute -> orderAttrs.contains(prevSIOrderAttribute.getAttributeName())).
					forEach(prevSIOrderAttribute ->{
						SIOrderAttribute siAttrEntity = serviceInventoryMapper.mapOrderAttrEntityToBean(prevSIOrderAttribute);
						siAttrEntity.setSiOrder(currentSiOrderEntity);
						siOrderAttrs.add(siAttrEntity);
		});
	}

	private void updateServiceStatusBasedOnServiceDetailIds(List<Integer> prevSiServiceDetailIds){
		if(!prevSiServiceDetailIds.isEmpty()){
			siServiceDetailRepository.updateServiceStatus(MACDConstants.TERMINATED,CommonConstants.N,prevSiServiceDetailIds);
		}
	}
	
	private List<Integer> getServiceDetailIdsBasedOnOrder(SIOrder siOrder){
		List<Integer> prevSiServiceDetailIds= new ArrayList<>();
		siOrder.getSiServiceDetails().stream().forEach(prevSIServiceDetail -> {
			prevSiServiceDetailIds.add(prevSIServiceDetail.getId());
		});
		return prevSiServiceDetailIds;
	}
	
	public void mapScOrderDetailsToSIEntity(ScOrderBean scOrderBean) {
		LOGGER.info("mapScOrderDetailsToSIEntity method invoked");
		SIOrder siOrder=siOrderRepository.findByOpOrderCode(scOrderBean.getOpOrderCode());
		if(null==siOrder){
			LOGGER.info("Order Code doesn't exists already::"+scOrderBean.getOpOrderCode());
			// SIOrder
			SIOrder siOrderEntity = serviceInventoryMapper.mapScOrderBeanToEntity(scOrderBean);
			// SIContractInfo
			Set<SIContractInfo> siContractingInfos = mapScContractInfoToEntity(scOrderBean, siOrderEntity);
			// SIOrderAttr
			Set<SIOrderAttribute> siOrderAttrs = mapScOrderAttrToEntity(scOrderBean, siOrderEntity);
			boolean isAccessExists[] = { false };
			boolean isAddOnExists[] = { false };
			Set<String> vmParentCloudCodeSet = new HashSet<>();
			Set<SIServiceDetail> siServiceDetails = new HashSet<>();
			scOrderBean.getScServiceDetails().stream().forEach(serviceDetail -> {
				// SIServiceDetail
				SIServiceDetail siServiceEntity = serviceInventoryMapper.mapScServiceEntityToBean(serviceDetail);
				// SIServiceContact
				Set<SiServiceContact> siServiceContacts = new HashSet<>();
				SiServiceContact siServiceContact = serviceInventoryMapper.mapServiceContact(serviceDetail, scOrderBean);
				siServiceContact.setSiServiceDetail(siServiceEntity);
				siServiceContacts.add(siServiceContact);
				siServiceEntity.setSiServiceContacts(siServiceContacts);
				// SIAttachment
				Set<SIAttachment> siAttachments = new HashSet<>();
				serviceDetail.getScAttachments().stream().forEach(scAttachmentBean -> {
					siAttachments
							.add(serviceInventoryMapper.mapScServiceAttachmentToEntity(scAttachmentBean, siServiceEntity));
				});

				if (null != serviceDetail.getScProductDetail()) {
					Set<SIAsset> siAssets = new HashSet<>();
					Set<SIAssetToService> siAssetToServices = new HashSet<>();
					// SIAssetDetails
					siAssets = mapScAssetToEntity(serviceDetail, siAssets, siAssetToServices, siServiceEntity);
					if (MACDConstants.DELETE_VM_SERVICE.equals(serviceDetail.getOrderCategory())) {
						serviceDetail.getScProductDetail().stream().filter(scProductDetail -> ("CLOUD".equals(scProductDetail.getType()) && scProductDetail.getParentCloudCode() != null)).forEach(scProductDetail -> {
							vmParentCloudCodeSet.add(scProductDetail.getParentCloudCode());
						});
					}
					LOGGER.info("Asset size::" + siAssets.size());
					siServiceEntity.setSiAssets(siAssets);

					siAssets.stream().forEach(siAsset -> {
						if ("ACCESS".equals(siAsset.getType())) {
							isAccessExists[0] = true;
						} else if ("ADDON".equals(siAsset.getType())) {
							isAddOnExists[0] = true;
						} else if ("CLOUD".equals(siAsset.getType())) {
							if (null != siAsset.getParentCloudCode()) {
								vmParentCloudCodeSet.add(siAsset.getParentCloudCode());
							}
						}
					});

				}
				siServiceEntity.setSiOrder(siOrderEntity);
				siServiceEntity.setSiAttachments(siAttachments);
				// Setting siContractInfo in SIServiceDetail, as considering it will
				// be only one for each order
				siServiceEntity.setSiContractInfo(siContractingInfos.stream().findFirst().get());
				siServiceDetails.add(siServiceEntity);
			});
			if (scOrderBean.getOrderType().equals(MACDConstants.MACD_ORDER)) {
				if (MACDConstants.CONNECTIVITY_UPGRADE_SERVICE.equals(scOrderBean.getOrderCategory())
						&& null != scOrderBean.getParentOpOrderCode()) {
						LOGGER.info("Connectivity Upgrade");
						processConnectivityUpgrade(scOrderBean, siOrderEntity, siOrderAttrs, siServiceDetails);
				} else if (MACDConstants.ADDITIONAL_SERVICE_UPGRADE.equals(scOrderBean.getOrderCategory())
						&& null != scOrderBean.getParentOpOrderCode()) {
						LOGGER.info("Add On");
						processAddOn(scOrderBean, siOrderEntity, siOrderAttrs, siServiceDetails);
				} else if (MACDConstants.ADD_CLOUDVM_SERVICE.equals(scOrderBean.getOrderCategory())
						&& null != scOrderBean.getParentOpOrderCode()) {
						LOGGER.info("Add VM");
						processAddVM(scOrderBean, siOrderEntity, siOrderAttrs, siServiceDetails, isAccessExists,
								isAddOnExists);
				} else if ((MACDConstants.UPGRADE_VM_SERVICE.equals(scOrderBean.getOrderCategory()) || MACDConstants.DELETE_VM_SERVICE.equals(scOrderBean.getOrderCategory()))
						&& null != scOrderBean.getParentOpOrderCode()) {
						LOGGER.info("Update/Delete VM: {}", scOrderBean.getOrderCategory());
						processUpgradeVM(scOrderBean, siOrderEntity, siOrderAttrs, siServiceDetails, isAccessExists,
								isAddOnExists, vmParentCloudCodeSet);
				}
			}
			persistOrderToServiceInventory(siOrderEntity, siContractingInfos, siOrderAttrs, siServiceDetails);
		}else{
			LOGGER.info("Order Code exists already::"+scOrderBean.getOpOrderCode());
		}
		LOGGER.info("mapScOrderDetailsToSIEntity method ends");
	}
	
	private void processAddVM(ScOrderBean scOrderBean,SIOrder siOrderEntity,Set<SIOrderAttribute> siOrderAttrs, Set<SIServiceDetail> siServiceDetails, boolean[] isAccessExists, boolean[] isAddOnExists) {
		SIOrder prevSiOrder=siOrderRepository.findByOpOrderCode(scOrderBean.getParentOpOrderCode());
		if (null != prevSiOrder) {
			addExistingOrderAttributes(siOrderEntity, siOrderAttrs, prevSiOrder,
					MACDConstants.IPC_ORDER_ATTRIBUTE_LIST);
			LOGGER.info("Prev Order exists");
			// Update Previous ServiceDetail service status as Terminated
			List<Integer> prevSiServiceDetailIds = getServiceDetailIdsBasedOnOrder(prevSiOrder);
			updateServiceStatusBasedOnServiceDetailIds(prevSiServiceDetailIds);
			prevSiOrder.getSiServiceDetails().stream().forEach(prevSiServiceDetail -> {
				prevSiServiceDetail.getSiAssets().stream().forEach(prevSiAsset -> {
					if ("CLOUD".equals(prevSiAsset.getType())
							|| (!isAccessExists[0] && "ACCESS".equals(prevSiAsset.getType())
									|| (!isAddOnExists[0] && "ADDON".equals(prevSiAsset.getType())))) {
						mapPreviousAssets(siServiceDetails, prevSiAsset);
					}else if ("ADDON".equals(prevSiAsset.getType())) {
						onboardAddonAttrAndCalculatePrice(siServiceDetails,prevSiAsset,MACDConstants.ADD_CLOUDVM_SERVICE);
					}
				});
			});
			// Update Service Detail and Contract Info Price
			updateServiceDetailContractInfoPrice(siServiceDetails);
		}
	}
	
	private void onboardAddonAttrAndCalculatePrice(Set<SIServiceDetail> siServiceDetails, SIAsset prevSiAsset,String macdServiceType) {
		siServiceDetails.stream().forEach(currentSiServiceDetail -> {
			currentSiServiceDetail.getSiAssets().stream()
					.filter(currentSiAsset -> "ADDON".equals(currentSiAsset.getType()))
					.forEach(currentSiAsset -> {
						// Onboard prevAsset Attr
						onboardPrevAssetAttr(prevSiAsset, currentSiAsset, macdServiceType);
						// Onboard prevAsset Component
						onboardPrevAssetComponent(prevSiAsset, currentSiAsset);
						// Update currentAsset Commercial
						updateAssetCommercialPrice(currentSiAsset);
			});
		});
		
	}

	private void processUpgradeVM(ScOrderBean scOrderBean,SIOrder siOrderEntity,Set<SIOrderAttribute> siOrderAttrs, Set<SIServiceDetail> siServiceDetails, boolean[] isAccessExists, boolean[] isAddOnExists, Set<String> vmParentCloudCodeSet) {
		SIOrder prevSiOrder=siOrderRepository.findByOpOrderCode(scOrderBean.getParentOpOrderCode());
		if (null != prevSiOrder) {
			addExistingOrderAttributes(siOrderEntity, siOrderAttrs, prevSiOrder,
					MACDConstants.IPC_ORDER_ATTRIBUTE_LIST);
			LOGGER.info("Prev Order exists");
			// Update Previous ServiceDetail service status as Terminated
			List<Integer> prevSiServiceDetailIds = getServiceDetailIdsBasedOnOrder(prevSiOrder);
			updateServiceStatusBasedOnServiceDetailIds(prevSiServiceDetailIds);
			prevSiOrder.getSiServiceDetails().stream().forEach(prevSiServiceDetail -> {
				prevSiServiceDetail.getSiAssets().stream().forEach(prevSiAsset -> {
					if (!isAccessExists[0] && "ACCESS".equals(prevSiAsset.getType())
							|| (!isAddOnExists[0] && "ADDON".equals(prevSiAsset.getType()))) {
						mapPreviousAssets(siServiceDetails, prevSiAsset);
					} else if ("CLOUD".equals(prevSiAsset.getType())) {
						siServiceDetails.stream().forEach(currentSiServiceDetail -> {
							if (!vmParentCloudCodeSet.contains(prevSiAsset.getCloudCode())) {
								mapPreviousAssets(siServiceDetails, prevSiAsset);
							}
						});
					} else if ("ADDON".equals(prevSiAsset.getType())) {
						onboardAddonAttrAndCalculatePrice(siServiceDetails,prevSiAsset,MACDConstants.UPGRADE_VM_SERVICE);
					}
				});
			});
			// Update Service Detail and Contract Info Price
			updateServiceDetailContractInfoPrice(siServiceDetails);
		}
	}

	private void processAddOn(ScOrderBean scOrderBean,SIOrder siOrderEntity,Set<SIOrderAttribute> siOrderAttrs, Set<SIServiceDetail> siServiceDetails) {
		SIOrder prevSiOrder = siOrderRepository.findByOpOrderCode(scOrderBean.getParentOpOrderCode());
		if (null != prevSiOrder) {
			addExistingOrderAttributes(siOrderEntity, siOrderAttrs, prevSiOrder,
					MACDConstants.IPC_ORDER_ATTRIBUTE_LIST);
			LOGGER.info("Prev Order exists");
			// Update Previous ServiceDetail service status as Terminated
			List<Integer> prevSiServiceDetailIds = getServiceDetailIdsBasedOnOrder(prevSiOrder);
			updateServiceStatusBasedOnServiceDetailIds(prevSiServiceDetailIds);
			prevSiOrder.getSiServiceDetails().stream().forEach(prevSiServiceDetail -> {
				prevSiServiceDetail.getSiAssets().stream().forEach(prevSiAsset -> {
					if ("CLOUD".equals(prevSiAsset.getType()) || "ACCESS".equals(prevSiAsset.getType())) {
						mapPreviousAssets(siServiceDetails, prevSiAsset);
					} else if ("ADDON".equals(prevSiAsset.getType())) {
						onboardAddonAttrAndCalculatePrice(siServiceDetails, prevSiAsset, MACDConstants.ADDITIONAL_SERVICE_UPGRADE);
					}
				});
			});
			// Update Service Detail and Contract Info Price
			updateServiceDetailContractInfoPrice(siServiceDetails);
		}
	}
	
	private void processConnectivityUpgrade(ScOrderBean scOrderBean,SIOrder siOrderEntity,Set<SIOrderAttribute> siOrderAttrs, Set<SIServiceDetail> siServiceDetails) {
		SIOrder prevSiOrder=siOrderRepository.findByOpOrderCode(scOrderBean.getParentOpOrderCode());
		if (null != prevSiOrder) {
			addExistingOrderAttributes(siOrderEntity, siOrderAttrs, prevSiOrder,
					MACDConstants.IPC_ORDER_ATTRIBUTE_LIST);
			LOGGER.info("Prev Order exists");
			// Update Previous ServiceDetail service status as Terminated
			List<Integer> prevSiServiceDetailIds = getServiceDetailIdsBasedOnOrder(prevSiOrder);
			updateServiceStatusBasedOnServiceDetailIds(prevSiServiceDetailIds);
			prevSiOrder.getSiServiceDetails().stream().forEach(prevSiServiceDetail -> {
				prevSiServiceDetail.getSiAssets().stream().forEach(prevSiAsset -> {
					if ("CLOUD".equals(prevSiAsset.getType())) {
						mapPreviousAssets(siServiceDetails, prevSiAsset);
					} else if ("ADDON".equals(prevSiAsset.getType())) {
						onboardAddonAttrAndCalculatePrice(siServiceDetails,prevSiAsset,MACDConstants.CONNECTIVITY_UPGRADE_SERVICE);
					}
				});
			});
			// Update Service Detail and Contract Price
			updateServiceDetailContractInfoPrice(siServiceDetails);
		}
	}

	private void onboardPrevAssetAttr(SIAsset prevSiAsset, SIAsset currentSiAsset, String macdServiceType) {
		List<SIAssetAttribute> siAssetAttrList = new ArrayList<>();
		prevSiAsset.getSiAssetAttributes().stream().forEach(prevSiAssetAttr ->{									
			boolean isAttrExists[]={false};
			currentSiAsset.getSiAssetAttributes().stream().forEach(currentSiAssetAttr ->{
				if(currentSiAssetAttr.getCategory().equals(prevSiAssetAttr.getCategory())){
					isAttrExists[0]=true;
					if(macdServiceType.equals(MACDConstants.CONNECTIVITY_UPGRADE_SERVICE)){
						checkAndUpdateConnectivityUpgrade(currentSiAssetAttr,prevSiAssetAttr,currentSiAsset);
					}else if(macdServiceType.equals(MACDConstants.ADDITIONAL_SERVICE_UPGRADE)){
						checkAndUpdateAddOn(currentSiAssetAttr,prevSiAssetAttr,currentSiAsset);
					}else if(macdServiceType.equals(MACDConstants.ADD_CLOUDVM_SERVICE)
							|| macdServiceType.equals(MACDConstants.UPGRADE_VM_SERVICE) ){
						checkAndUpdateVM(currentSiAssetAttr,prevSiAssetAttr,currentSiAsset);
					}	
				}
			});
			if(!isAttrExists[0]){
				SIAssetAttribute siAssetAttribute = serviceInventoryMapper.mapAssetAttributeToBean(prevSiAssetAttr);
				siAssetAttribute.setSiAsset(currentSiAsset);
				siAssetAttrList.add(siAssetAttribute);
			}
		});
		if(!siAssetAttrList.isEmpty()){
			currentSiAsset.getSiAssetAttributes().addAll(siAssetAttrList);
		}
	}
	
	private void checkAndUpdateConnectivityUpgrade(SIAssetAttribute currentSiAssetAttr,SIAssetAttribute prevSiAssetAttr, SIAsset currentSiAsset){
		if(currentSiAssetAttr.getCategory().equals(ServiceInventoryConstants.IPC_ASSET_CATEGORY_ADDITIONAL_IP)){
			updateAssetAttr(currentSiAssetAttr,prevSiAssetAttr,currentSiAsset);
		}
	}
	
	private void checkAndUpdateAddOn(SIAssetAttribute currentSiAssetAttr,SIAssetAttribute prevSiAssetAttr, SIAsset currentSiAsset){
		if (ServiceInventoryConstants.IPC_ASSET_CATEGORY_VPN_CONNECTION.equals(currentSiAssetAttr.getCategory())
				|| ((currentSiAssetAttr.getCategory().startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_MYSQL)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_POSTGRESQL)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_MSSQL_SERVER)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_ZERTO)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_DOUBLE_TAKE))
						&& ServiceInventoryConstants.IPC_ATTRIBUTE_QUANTITY
								.equals(currentSiAssetAttr.getAttributeName()))) {
			if (currentSiAssetAttr.getAttributeName().equals(prevSiAssetAttr.getAttributeName())) {
				updateAssetAttr(currentSiAssetAttr, prevSiAssetAttr, currentSiAsset);
			}
		}
	}
	
	private void checkAndUpdateVM(SIAssetAttribute currentSiAssetAttr,SIAssetAttribute prevSiAssetAttr, SIAsset currentSiAsset){
		if (ServiceInventoryConstants.IPC_ASSET_CATEGORY_ADDITIONAL_IP.equals(currentSiAssetAttr.getCategory())
				|| ServiceInventoryConstants.IPC_ASSET_CATEGORY_VPN_CONNECTION.equals(currentSiAssetAttr.getCategory())
				|| ((currentSiAssetAttr.getCategory().startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_MYSQL)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_POSTGRESQL)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_MSSQL_SERVER)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_ZERTO)
						|| currentSiAssetAttr.getCategory()
								.startsWith(ServiceInventoryConstants.IPC_ASSET_CATEGORY_DOUBLE_TAKE))
						&& ServiceInventoryConstants.IPC_ATTRIBUTE_QUANTITY
								.equals(currentSiAssetAttr.getAttributeName()))) {
			if (currentSiAssetAttr.getAttributeName().equals(prevSiAssetAttr.getAttributeName())) {
				updateAssetAttr(currentSiAssetAttr, prevSiAssetAttr, currentSiAsset);
			}
		}
	}

	private void updateAssetAttr(SIAssetAttribute currentSiAssetAttr,SIAssetAttribute prevSiAssetAttr, SIAsset currentSiAsset){
		currentSiAssetAttr.setAttributeValue(valueOf(Integer.valueOf(currentSiAssetAttr.getAttributeValue())
				+Integer.valueOf(prevSiAssetAttr.getAttributeValue())));
		currentSiAsset.getSiAssetAttributes().add(currentSiAssetAttr);
	}
	
	private void onboardPrevAssetComponent(SIAsset prevSiAsset, SIAsset currentSiAsset) {
		Map<String, SIAssetComponent> siAssetCompMap = new HashMap<>();
		prevSiAsset.getSiAssetCommercials().stream().forEach(prevSiAssetCommercial -> {
			prevSiAssetCommercial.getSiAssetComponents().stream().forEach(prevSiAssetComponent -> {
				siAssetCompMap.put(prevSiAssetComponent.getItem(), serviceInventoryMapper.mapAssetComponentToBean(prevSiAssetComponent, currentSiAsset.getSiAssetCommercials().get(0)));
			});
		});
		
		currentSiAsset.getSiAssetCommercials().stream().forEach(currentSiAssetCommercial -> {
			currentSiAssetCommercial.getSiAssetComponents().stream().forEach(currentSiAssetComponent -> {
				if(siAssetCompMap.containsKey(currentSiAssetComponent.getItem()) 
						&& (!("VDOM").equalsIgnoreCase(currentSiAssetComponent.getItem()) 
						&& !("Backup").equalsIgnoreCase(currentSiAssetComponent.getItem()) 
						&& !("managed").equalsIgnoreCase(currentSiAssetComponent.getItem()))) {
					SIAssetComponent prevSiAssetComponent = siAssetCompMap.get(currentSiAssetComponent.getItem());
					currentSiAssetComponent.setArc(currentSiAssetComponent.getArc() + prevSiAssetComponent.getArc());
					currentSiAssetComponent.setMrc(currentSiAssetComponent.getMrc() + prevSiAssetComponent.getMrc());
					currentSiAssetComponent.setNrc(currentSiAssetComponent.getNrc() + prevSiAssetComponent.getNrc());
					siAssetCompMap.put(currentSiAssetComponent.getItem(), currentSiAssetComponent);
				} else {
					siAssetCompMap.put(currentSiAssetComponent.getItem(), currentSiAssetComponent);
				}
			});
		});
		
		List<SIAssetComponent> siAssetComponentList = new ArrayList<>();
		siAssetCompMap.forEach((item, component) -> {
			siAssetComponentList.add(component);
		});
		currentSiAsset.getSiAssetCommercials().get(0).setSiAssetComponents(siAssetComponentList);
	}

	private void updateAssetCommercialPrice(SIAsset currentSiAsset) {
		currentSiAsset.getSiAssetCommercials().stream().forEach(currentSiAssetCommercial ->{
			double[] arc={0.0};
			double[] mrc={0.0};
			double[] nrc={0.0};
			if(currentSiAssetCommercial.getSiAssetComponents()!=null) {
				currentSiAssetCommercial.getSiAssetComponents().stream().forEach(currentSiAssetComponent ->{
					arc[0]=arc[0]+currentSiAssetComponent.getArc();
					mrc[0]=mrc[0]+currentSiAssetComponent.getMrc();
					nrc[0]=nrc[0]+currentSiAssetComponent.getNrc();
				});
			}
			currentSiAssetCommercial.setArc(limitDoubleValue(arc[0],2));
			currentSiAssetCommercial.setMrc(limitDoubleValue(mrc[0],2));
			currentSiAssetCommercial.setNrc(limitDoubleValue(nrc[0],2));
		});
	}

	private void updateServiceDetailContractInfoPrice(Set<SIServiceDetail> siServiceDetails) {
		double[] total_mrc={0.0};
		double[] total_nrc={0.0};
		double[] total_arc={0.0};
		siServiceDetails.stream().forEach(siServiceDetail ->{
			siServiceDetail.getSiAssets().stream().forEach(siAsset -> {
				siAsset.getSiAssetCommercials().stream().forEach(siAssetCommercial ->{
					total_mrc[0]=total_mrc[0]+siAssetCommercial.getMrc();
					total_nrc[0]=total_nrc[0]+siAssetCommercial.getNrc();
					total_arc[0]=total_arc[0]+siAssetCommercial.getArc();
				});
			});
		});
		siServiceDetails.stream().forEach(currentSiServiceDetail ->{
			currentSiServiceDetail.setMrc(limitDoubleValue(total_mrc[0],2));
			currentSiServiceDetail.setNrc(limitDoubleValue(total_nrc[0],2));
			currentSiServiceDetail.setArc(limitDoubleValue(total_arc[0],2));
			currentSiServiceDetail.getSiContractInfo().setMrc(BigDecimal.valueOf(total_mrc[0]));
			currentSiServiceDetail.getSiContractInfo().setNrc(BigDecimal.valueOf(total_nrc[0]));
			currentSiServiceDetail.getSiContractInfo().setArc(BigDecimal.valueOf(total_arc[0]));
		});
	}

	private void mapPreviousAssets(Set<SIServiceDetail> siServiceDetails, SIAsset prevSiAsset){
		siServiceDetails.stream().forEach(currentSiServiceDetail ->{
			Set<SIAsset> siAssets = new HashSet<>();
			Set<SIAssetToService> siAssetToServices = new HashSet<>();
			//SIAssetDetails
			siAssets=mapAssetToEntity(prevSiAsset,siAssets,siAssetToServices,currentSiServiceDetail);
			LOGGER.info("Asset size::"+siAssets.size());
		});
	}
	
	
	private double limitDoubleValue(double input, int roundUp){
		return new BigDecimal(input).setScale(roundUp, RoundingMode.HALF_UP).doubleValue();
	}
	
	private Set<SIAsset> mapAssetToEntity(SIAsset prevSiAsset, Set<SIAsset> siAssets,
			Set<SIAssetToService> siAssetToServices, SIServiceDetail currentSiServiceDetail) {
		
			List<SIAssetCommercial> siAssetCommercials = new ArrayList<>();
			//SIAsset
			SIAsset siAsset = serviceInventoryMapper.mapAssetEntityToBean(prevSiAsset);
			siAsset.setSiServiceDetail(currentSiServiceDetail);
			if (null != prevSiAsset.getSiAssetAttributes() && !prevSiAsset.getSiAssetAttributes().isEmpty()) {
				//SIAssetAttribute
				Set<SIAssetAttribute> siAssetAttrs = serviceInventoryMapper.mapAssetAttr(prevSiAsset.getSiAssetAttributes(), siAsset);
				siAsset.setSiAssetAttributes(siAssetAttrs);
			}
			//SIAssetCommercial
			SIAssetCommercial siAssetCommercial =serviceInventoryMapper.mapAssetCommercialToBean(prevSiAsset.getSiAssetCommercials().get(0),siAsset);
			if (null != prevSiAsset.getSiAssetCommercials() && !prevSiAsset.getSiAssetCommercials().isEmpty()) {
				List<SIAssetComponent> siAssetComponents = new ArrayList<>();
				//SIAssetComponent
				prevSiAsset.getSiAssetCommercials().stream().forEach(prevSiAssetCommercial ->{
					prevSiAssetCommercial.getSiAssetComponents().stream().forEach(prevSiAssetComp ->{
						SIAssetComponent siAssetComponent = serviceInventoryMapper.mapAssetComponentToBean(prevSiAssetComp, siAssetCommercial);
						siAssetComponents.add(siAssetComponent);
					});
				});
				LOGGER.info("Component size::"+siAssetComponents.size());
				siAssetCommercial.setSiAssetComponents(siAssetComponents);
			}
			siAssetCommercials.add(siAssetCommercial);
			siAsset.setSiAssetCommercials(siAssetCommercials);
			siAsset.setSiServiceDetail(currentSiServiceDetail);
			//SIAssetToService
			SIAssetToService siAssetToService=serviceInventoryMapper.mapSIAssetToService(currentSiServiceDetail,siAsset);
			siAssetToServices.add(siAssetToService);
			siAsset.setSiAssetToServices(siAssetToServices);
			LOGGER.info("SiAssetToService size::"+siAssetToServices.size());
			siAssets.add(siAsset);
			currentSiServiceDetail.getSiAssetToServices().addAll(siAssetToServices);
			currentSiServiceDetail.getSiAssets().addAll(siAssets);
			return siAssets;
	}
	
	private Set<SIAsset> mapScAssetToEntity(ScServiceDetailBean serviceDetail, Set<SIAsset> siAssets,
			Set<SIAssetToService> siAssetToServices,SIServiceDetail siServiceEntity) {
		
		serviceDetail.getScProductDetail().stream().filter(scProductDetail -> (!Arrays.asList("IPC Discount",CommonConstants.EARLY_TERMINATION_CHARGES).contains(scProductDetail.getSolutionName()) && 
				!("CLOUD".equals(scProductDetail.getType()) && scProductDetail.getParentCloudCode() != null && MACDConstants.DELETE_VM_SERVICE.equals(serviceDetail.getOrderCategory())))).forEach(scProductDetail -> {
			List<SIAssetCommercial> siAssetCommercials = new ArrayList<>();
			//SIAsset
			SIAsset siAsset = serviceInventoryMapper.mapScProductDetailEntityToBean(scProductDetail);
			siAsset.setSiServiceDetail(siServiceEntity);
			if (null != scProductDetail.getScProductAttributes() && !scProductDetail.getScProductAttributes().isEmpty()) {
				//SIAssetAttribute
				Set<SIAssetAttribute> siAssetAttrs = serviceInventoryMapper.mapScProductDetailAttr(scProductDetail.getScProductAttributes(), siAsset);
				siAsset.setSiAssetAttributes(siAssetAttrs);
			}
			//SIAssetCommercial
			SIAssetCommercial siAssetCommercial =serviceInventoryMapper.mapScProductPriceDetailToBean(scProductDetail,siAsset);
			if (null != scProductDetail.getScServiceCommercials() && !scProductDetail.getScServiceCommercials().isEmpty()) {
				List<SIAssetComponent> siAssetComponents = new ArrayList<>();
				//SIAssetComponent
				scProductDetail.getScServiceCommercials().stream().forEach(scServiceCommercial ->{
					SIAssetComponent siAssetComponent = serviceInventoryMapper.mapScServiceCommercialToComponent(scServiceCommercial, siAssetCommercial);
					siAssetComponents.add(siAssetComponent);
				});
				LOGGER.info("Component size::"+siAssetComponents.size());
				siAssetCommercial.setSiAssetComponents(siAssetComponents);
			}
			siAssetCommercials.add(siAssetCommercial);
			siAsset.setSiAssetCommercials(siAssetCommercials);
			siAsset.setSiServiceDetail(siServiceEntity);
			//SIAssetToService
			SIAssetToService siAssetToService=serviceInventoryMapper.mapSIAssetToService(siServiceEntity,siAsset);
			siAssetToServices.add(siAssetToService);
			siAsset.setSiAssetToServices(siAssetToServices);
			LOGGER.info("SiAssetToService size::"+siAssetToServices.size());
			siAssets.add(siAsset);
		});
		siServiceEntity.setSiAssetToServices(siAssetToServices);
		return siAssets;
	}

	private void persistOrderToServiceInventory(SIOrder siOrderEntity, Set<SIContractInfo> siContractingInfos,
			Set<SIOrderAttribute> siOrderAttrs, Set<SIServiceDetail> siServiceDetails) {
		LOGGER.info("persistOrderToServiceInventory method invoked");
		siOrderRepository.save(siOrderEntity);
		siContractInfoRepository.saveAll(siContractingInfos);
		siOrderAttributeRepository.saveAll(siOrderAttrs);
		siServiceDetailRepository.saveAll(siServiceDetails);
		LOGGER.info("persistOrderToServiceInventory method ends");
	}

	private Set<SIOrderAttribute> mapScOrderAttrToEntity(ScOrderBean scOrderBean, SIOrder siOrderEntity) {
		Set<SIOrderAttribute> siOrderAttrs = new HashSet<>();
		for (ScOrderAttributeBean scOrderAttribute : scOrderBean.getScOrderAttributes()) {
			SIOrderAttribute siAttrEntity = serviceInventoryMapper.mapScOrderAttrEntityToBean(scOrderAttribute);
			siAttrEntity.setSiOrder(siOrderEntity);
			siOrderAttrs.add(siAttrEntity);
		}
		return siOrderAttrs;
	}

	private Set<SIContractInfo> mapScContractInfoToEntity(ScOrderBean scOrderBean,SIOrder siOrderEntity){
		Set<SIContractInfo> siContractingInfos = new HashSet<>();
		scOrderBean.getScContractInfos().stream().forEach(contractingInfo -> {
			SIContractInfo siContrEntity = serviceInventoryMapper.mapContractingInfoEntityToBean(contractingInfo);
				siContrEntity.setSiOrder(siOrderEntity);
				siContractingInfos.add(siContrEntity);
		});
		return siContractingInfos;
	}

	private List<com.tcl.dias.common.serviceinventory.beans.SIAttributeBean> populateServiceAttribute(Set<SIServiceAttribute> siServiceAttributes) {
		List<com.tcl.dias.common.serviceinventory.beans.SIAttributeBean> detailDataBeans = siServiceAttributes.stream().map(attribute -> {
			com.tcl.dias.common.serviceinventory.beans.SIAttributeBean siAttributeBean = new com.tcl.dias.common.serviceinventory.beans.SIAttributeBean();
			siAttributeBean.setName(attribute.getAttributeName());
			siAttributeBean.setValue(attribute.getAttributeValue());
			siAttributeBean.setCategory(attribute.getCategory());
			siAttributeBean.setCreatedBy(attribute.getCreatedBy());
			siAttributeBean.setUpdatedBy(attribute.getUpdatedBy());
			siAttributeBean.setCreatedDate(attribute.getCreatedDate());
			siAttributeBean.setUpdatedDate(attribute.getUpdatedDate());
			return siAttributeBean;
		}).collect(Collectors.toList());
		return detailDataBeans;
	}
	
	private com.tcl.dias.common.serviceinventory.beans.SIAttributeBean populateServiceAttr(String attrName, String attrValue, String category) {
			com.tcl.dias.common.serviceinventory.beans.SIAttributeBean siAttributeBean = new com.tcl.dias.common.serviceinventory.beans.SIAttributeBean();
			siAttributeBean.setName(attrName);
			siAttributeBean.setValue(attrValue);
			siAttributeBean.setCategory(category);
			siAttributeBean.setCreatedBy("OPTIMUS ETL DATA MASSAGING");
			siAttributeBean.setUpdatedBy("OPTIMUS ETL DATA MASSAGING");
			siAttributeBean.setCreatedDate(new Timestamp(new Date().getTime()));
			siAttributeBean.setUpdatedDate(new Timestamp(new Date().getTime()));
			return siAttributeBean;
	}
	
	
	public String updateShowCosMessage(ServiceDetailBean serviceBean) throws TclCommonException {
		String response = null;
		if(serviceBean.getPrimaryServiceId() == null)
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		
		Optional<SIServiceDetail> siServiceDetailOpt = siServiceDetailRepository.findByTpsServiceId(serviceBean.getPrimaryServiceId());
		if(siServiceDetailOpt.isPresent()) {
			siServiceDetailOpt.get().setShowCosMessage(Byte.valueOf(serviceBean.getShowCosMessage())); 
			siServiceDetailRepository.saveAndFlush(siServiceDetailOpt.get());
		}
		return CommonConstants.SUCCESS;
	}
	
    public List<SIServiceDetailsBean> getOrderDataBasedOnService(String[] serviceIds) throws TclCommonException {
    	final String[] totalBandWidth= {""};
        List<SIServiceDetailsBean>  serviceDetailsBeanList  =  new ArrayList<>();
        if(serviceIds == null || serviceIds.length == 0) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
        List<String> serviceIdsList = Arrays.asList(serviceIds);
       List<SIServiceInfo> serviceInfoList =  siServiceInfoRepository.findByServiceIdIn(serviceIdsList);
       if( serviceInfoList != null && !serviceInfoList.isEmpty()) {
    	   serviceInfoList.stream().forEach(serviceInfo -> {
    		   LOGGER.info("Servie Info {}", serviceInfo.getServiceId());
    		   SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();
    		   serviceDetailBean.setAccessType(serviceInfo.getAccessType());
    		   serviceDetailBean.setSiteAddress(serviceInfo.getCustomerSiteAddress());
    		   serviceDetailBean.setAccessProvider(serviceInfo.getLastMileProvider());
    		   serviceDetailBean.setArc(serviceInfo.getArc());
    		   serviceDetailBean.setBillingFrequency(serviceInfo.getBillingFrequency());
    		   serviceDetailBean.setBillingMethod(serviceInfo.getBillingMethod());
    		   serviceDetailBean.setContractTerm(serviceInfo.getOrderTermInMonths());
    		   serviceDetailBean.setErfCustomerId(serviceInfo.getOrderCustomerId());
    		   serviceDetailBean.setErfCustomerLeId(serviceInfo.getOrderCustLeId());
    		   serviceDetailBean.setErfCustomerLeName(serviceInfo.getOrderCustLeName());
    		   serviceDetailBean.setErfCustomerName(serviceInfo.getOrderCustomer());
    		   serviceDetailBean.setErfPrdCatalogOfferingId(serviceInfo.getProductOfferingId());
    		   serviceDetailBean.setErfPrdCatalogOfferingName(serviceInfo.getProductOfferingName());
    		   serviceDetailBean.setErfPrdCatalogProductName(serviceInfo.getProductFamilyName());
    		   serviceDetailBean.setLastmileBw(serviceInfo.getLastMileBandwidth());
    		   serviceDetailBean.setLastmileBwUnit(serviceInfo.getLastMileBandwidthUnit());
    		   serviceDetailBean.setPortBw(serviceInfo.getBandwidth());
    		   serviceDetailBean.setPortBwUnit(serviceInfo.getBandwidthUnit());
    		   serviceDetailBean.setLinkType(serviceInfo.getPrimaryOrSecondary());
    		   serviceDetailBean.setTpsServiceId(serviceInfo.getServiceId());
    		   serviceDetailBean.setPriSecServLink(serviceInfo.getPrimarySecondaryLink());
    		   serviceDetailBean.setNrc(serviceInfo.getNrc());
    		   serviceDetailBean.setReferenceOrderId(serviceInfo.getOrderSysId());
    		   serviceDetailBean.setOrderCode(serviceInfo.getOrderCode());
    		   serviceDetailBean.setVpnName(serviceInfo.getVpnName());
    		   serviceDetailBean.setErfLocSiteAddressId(serviceInfo.getLocationId());
    		   serviceDetailBean.setLatLong(serviceInfo.getLatLong());
    		   serviceDetailBean.setId(serviceInfo.getId());
    		   serviceDetailBean.setErfSpLeId(serviceInfo.getOrderSpLeId());
    		   serviceDetailBean.setErfSpLeName(serviceInfo.getOrderSpLeName());
    		   serviceDetailBean.setCustomerCurrencyId(serviceInfo.getCurrencyId());
    		   serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
    		   serviceDetailBean.setMrc(serviceInfo.getMrc());
    		   serviceDetailBean.setDemoFlag(Objects.nonNull(serviceInfo.getOrderDemoFlag())?serviceInfo.getOrderDemoFlag():"");
    		   serviceDetailBean.setDemoType(Objects.nonNull(serviceInfo.getBillingType())?serviceInfo.getBillingType():"");

    		   LOGGER.info("Demo type and flag for service id ---> {} are --- {}  ---- {}  " , serviceDetailBean.getTpsServiceId()
					   ,serviceDetailBean.getDemoType(),serviceDetailBean.getDemoFlag());

    		   if(Objects.nonNull(serviceInfo.getSiteType())){
				   serviceDetailBean.setSrvSiteType(serviceInfo.getSiteType());
				   LOGGER.info("Site Type for service id --> {}  is ----> {} ", serviceInfo.getServiceId(),serviceDetailBean.getSrvSiteType());
			   }
    		   serviceDetailBean.setContractStartDate(serviceInfo.getContractStartDate());
    		   serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
    		   serviceDetailBean.setDemarcationApartment(serviceInfo.getDemarcationApartment());
    		   serviceDetailBean.setDemarcationFloor(serviceInfo.getDemarcationFloor());
    		   serviceDetailBean.setDemarcationRoom(serviceInfo.getDemarcationRoom());
    		   serviceDetailBean.setDemarcationRack(serviceInfo.getDemarcationRack());
			   LOGGER.info("Order Partner id for service info id {} is  {} ", serviceInfo.getServiceId(), serviceInfo.getOrderPartner());
			   serviceDetailBean.setPartnerId(serviceInfo.getOrderPartner());
			   serviceDetailBean.setPartnerCuid(serviceInfo.getPartnerCuid());
			   serviceDetailBean.setErfCustPartnerLeId(serviceInfo.getErfCustPartnerLeId());
			   serviceDetailBean.setOpportunityType(serviceInfo.getOpportunityType());
			   serviceDetailBean.setLastMileType(serviceInfo.getLastMileType());
			   serviceDetailBean.setTpsCopfId(serviceInfo.getTpsCopfId());
			   serviceDetailBean.setTpsSfdcCuid(serviceInfo.getSfdcCuId());
			   serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
			   serviceDetailBean.setBurstableBw(serviceInfo.getBurstableBandwidth());
			   serviceDetailBean.setBurstableBwUnit(serviceInfo.getBurstableBandwidthUnit());
			   serviceDetailBean.setCircuitExpiryDate(serviceInfo.getCircuitExpiryDate());
			   serviceDetailBean.setOrderCategory(serviceInfo.getCurrentOpportunityType());
			   serviceDetailBean.setSiteEndInterface(serviceInfo.getSiteEndInterface());

			   serviceDetailBean.setBillingCurrency(serviceInfo.getBillingCurrency());
			   serviceDetailBean.setPortMode(serviceInfo.getSrvPortMode());
			   serviceDetailBean.setsCommisionDate(serviceInfo.getCommissionedDate());
			   serviceDetailBean.setIpv4AddressPoolsize(serviceInfo.getIpv4AddressPoolSize());

			   serviceDetailBean.setSourceCity(serviceInfo.getSourceCity());
			   serviceDetailBean.setDestinationCity(serviceInfo.getDestinationCity());
			   serviceDetailBean.setAccountManager(serviceInfo.getAccountManager());

			   //added for gvpn multivrf macd update port bw values to total vrf bandwidth value
				if (serviceInfo.getProductFamilyName() != null
						&& serviceInfo.getProductFamilyName().equalsIgnoreCase("GVPN")) {
					Optional<SIServiceDetail> siServiceDetail = siServiceDetailRepository
							.findByTpsServiceIdAndIsActive(serviceInfo.getServiceId(), CommonConstants.Y);
					if (siServiceDetail.isPresent()) {
						if (siServiceDetail.get().getMultiVrfSolution() != null) {
							LOGGER.info("MULTIVRF FLAG" + siServiceDetail.get().getMultiVrfSolution() + "port bw"
									+ serviceDetailBean.getPortBw() + "unit por bw"
									+ serviceDetailBean.getPortBwUnit());
							if (siServiceDetail.get().getMultiVrfSolution().equalsIgnoreCase("Yes")) {
								totalBandWidth[0] = getAttributesValues(siServiceDetail.get(),
										CommonConstants.TOTAL_VRF_BANDWIDTH_MBPS);
								LOGGER.info("total vrf bandwidth" + totalBandWidth[0]);
								serviceDetailBean.setTotalVrfBandwith(totalBandWidth[0]);
								serviceDetailBean.setPortBw(totalBandWidth[0]);
								serviceDetailBean.setPortBwUnit("Mbps");
							}
						}
					}
				}
			   constructAssetAttributes(serviceInfo, serviceDetailBean);
			   constructComponentAttributes(serviceInfo, serviceDetailBean);
			   try {
				   getDemoOrderContractDetails(serviceDetailBean.getAssetAttributes(),serviceInfo);
			   } catch (TclCommonException e) {
				   e.printStackTrace();
			   }
			   serviceDetailsBeanList.add(serviceDetailBean);
    	   });
    	   
       }
        return serviceDetailsBeanList;
    }

	private void constructComponentAttributes(SIServiceInfo serviceInfo, SIServiceDetailsBean serviceDetailBean) {
		List<SIComponentBean> componentList = new ArrayList<>();
		SIComponentBean componentBean = new SIComponentBean();
		componentBean.setComponentName("LM");
		componentBean.setSiServiceDetailId(serviceDetailBean.getId());
		componentBean.setUuid(serviceDetailBean.getTpsServiceId());
		List<SIComponentAttributeBean> componentAttributeBeanList = new ArrayList<>();
		List<Map<String, Object>> serviceComponentAttributeDetails = siServiceInfoRepository.getServiceComponentAttributesBasedOnServiceDetailId(serviceInfo.getId());
		if(serviceComponentAttributeDetails != null && !serviceComponentAttributeDetails.isEmpty()) {
			
			serviceComponentAttributeDetails.stream().forEach(attribute -> {
				LOGGER.info("Entering with attribute name {}, attributeValue {}", attribute.get("attribute_name"), attribute.get("attribute_value"));
				String attributeName = (String) attribute.get("attribute_name");
				String attributeValue = (String) attribute.get("attribute_value");
				if(attributeName.equalsIgnoreCase(MACDConstants.END_MUX_NODE_IP)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.END_MUX_NODE_IP);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("END_MUX_NODE_IP: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else 	if(attributeName.equalsIgnoreCase(MACDConstants.END_MUX_NODE_NAME)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.END_MUX_NODE_NAME);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("END_MUX_NODE_NAME: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.END_MUX_NODE_PORT)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.END_MUX_NODE_PORT);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("END_MUX_NODE_PORT: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.MUX_MAKE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.MUX_MAKE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("MUX_MAKE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.STRUCTURE_TYPE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.STRUCTURE_TYPE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("STRUCTURE_TYPE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.MUX_MAKE_MODEL)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.MUX_MAKE_MODEL);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("MUX_MAKE_MODEL: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.LAST_MILE_SCNEARIO)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.LAST_MILE_SCNEARIO);
					String lastMileScenario = (String)attribute.get(MACDConstants.LAST_MILE_SCNEARIO);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("LAST_MILE_SCNEARIO: {}, service detail id {}", 
							(String)attribute.get(MACDConstants.LAST_MILE_SCNEARIO), serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.LM_CONNECTION_TYPE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.LM_CONNECTION_TYPE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("LM_CONNECTION_TYPE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.MAST_PO_NUMBER)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.MAST_PO_NUMBER);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("MAST_PO_NUMBER: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.OFFNET_SUPPLIER_BILLSTART_DATE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.OFFNET_SUPPLIER_BILLSTART_DATE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("OFFNET_SUPPLIER_BILLSTART_DATE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.RFMAKE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.RFMAKE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("RFMAKE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				}
			});
		} 
		
		componentBean.setSiComponentAttributes(componentAttributeBeanList);
		componentList.add(componentBean);
		serviceDetailBean.setComponentBean(componentList);
		
	}

	public HashSet<SIServiceDetailsBean> getOrderDataBasedOnServiceForPriSec(String[] serviceIds) throws TclCommonException {
		HashSet<SIServiceDetailsBean> serviceDetailBeanSet= new HashSet<>();
		SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();

		if(serviceIds == null || serviceIds.length == 0) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Inside getOrderDataBasedOnServiceForPriSec for serviceIds {}", serviceIds);
		List<String> serviceIdsList = Arrays.asList(serviceIds);
		List<SIServiceInfo> serviceInfoList =  siServiceInfoRepository.findByServiceIdIn(serviceIdsList);
		if( serviceInfoList != null && !serviceInfoList.isEmpty()) {


			serviceInfoList.stream().forEach(serviceInfo -> {
				LOGGER.info("Service Info {}", serviceInfo.getServiceId());

				constructServiceDetail(serviceInfo,serviceDetailBeanSet);
			});

		}
		List<SIServiceDetailsBean>  serviceDetailsBeanList  =  new ArrayList<SIServiceDetailsBean>(serviceDetailBeanSet);
		LOGGER.info("Fetched data for {} circuit", serviceDetailsBeanList.stream().findFirst().get().getLinkType());

		if (Objects.nonNull(serviceDetailsBeanList))
		{
			serviceDetailsBeanList.stream().forEach(siServiceDetailsBean -> {
				if (StringUtils.isNotBlank(siServiceDetailsBean.getPriSecServLink())) {
					String secondaryCircuit = siServiceDetailsBean.getPriSecServLink();
					Optional<SIServiceInfo> secondaryServiceInfo = siServiceInfoRepository.findByServiceId(secondaryCircuit);
					constructServiceDetail(secondaryServiceInfo.get(),serviceDetailBeanSet);
					LOGGER.info("Fetched data for related circuit {}", secondaryCircuit);
				}
			});

		}
		LOGGER.info("ServiceDetailsBeanSet {}" ,serviceDetailBeanSet);
		return serviceDetailBeanSet;
	}

	private void constructServiceDetail(SIServiceInfo serviceInfo, HashSet<SIServiceDetailsBean> serviceDetailBeanSet) {

		LOGGER.info("Service Info inside constructServiceDetail {}", serviceInfo.getServiceId());

		SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();

		serviceDetailBean.setAccessType(serviceInfo.getAccessType());
		serviceDetailBean.setAccessProvider(serviceInfo.getLastMileProvider());
		serviceDetailBean.setArc(serviceInfo.getArc());
		serviceDetailBean.setBillingFrequency(serviceInfo.getBillingFrequency());
		serviceDetailBean.setBillingMethod(serviceInfo.getBillingMethod());
		serviceDetailBean.setContractTerm(serviceInfo.getOrderTermInMonths());
		serviceDetailBean.setErfCustomerId(serviceInfo.getOrderCustomerId());
		serviceDetailBean.setErfCustomerLeId(serviceInfo.getOrderCustLeId());
		serviceDetailBean.setErfCustomerLeName(serviceInfo.getOrderCustLeName());
		serviceDetailBean.setErfCustomerName(serviceInfo.getOrderCustomer());
		serviceDetailBean.setErfPrdCatalogOfferingId(serviceInfo.getProductOfferingId());
		serviceDetailBean.setErfPrdCatalogOfferingName(serviceInfo.getProductOfferingName());
		serviceDetailBean.setLastmileBw(serviceInfo.getLastMileBandwidth());
		serviceDetailBean.setLastmileBwUnit(serviceInfo.getLastMileBandwidthUnit());
		serviceDetailBean.setPortBw(serviceInfo.getBandwidth());
		serviceDetailBean.setPortBwUnit(serviceInfo.getBandwidthUnit());
		serviceDetailBean.setLinkType(serviceInfo.getPrimaryOrSecondary());
		serviceDetailBean.setTpsServiceId(serviceInfo.getServiceId());
		serviceDetailBean.setPriSecServLink(serviceInfo.getPrimarySecondaryLink());
		serviceDetailBean.setNrc(serviceInfo.getNrc());
		serviceDetailBean.setReferenceOrderId(serviceInfo.getOrderSysId());
		serviceDetailBean.setVpnName(serviceInfo.getVpnName());
		serviceDetailBean.setErfLocSiteAddressId(serviceInfo.getLocationId());
		serviceDetailBean.setLatLong(serviceInfo.getLatLong());
		serviceDetailBean.setId(serviceInfo.getId());
		serviceDetailBean.setErfSpLeId(serviceInfo.getOrderSpLeId());
		serviceDetailBean.setErfSpLeName(serviceInfo.getOrderSpLeName());
		serviceDetailBean.setCustomerCurrencyId(serviceInfo.getCurrencyId());
		serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
		serviceDetailBean.setMrc(serviceInfo.getMrc());
		if(Objects.nonNull(serviceInfo.getSiteType())){
			serviceDetailBean.setSrvSiteType(serviceInfo.getSiteType());
			LOGGER.info("Site Type for service id --> {}  is ----> {} ", serviceInfo.getServiceId(),serviceDetailBean.getSrvSiteType());
		}
		serviceDetailBean.setContractStartDate(serviceInfo.getContractStartDate());
		serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
		serviceDetailBean.setDemarcationApartment(serviceInfo.getDemarcationApartment());
		serviceDetailBean.setDemarcationFloor(serviceInfo.getDemarcationFloor());
		serviceDetailBean.setDemarcationRoom(serviceInfo.getDemarcationRoom());
		serviceDetailBean.setDemarcationRack(serviceInfo.getDemarcationRack());
		LOGGER.info("Order Partner id for service info id {} is  {} ", serviceInfo.getServiceId(), serviceInfo.getOrderPartner());
		serviceDetailBean.setPartnerId(serviceInfo.getOrderPartner());
		serviceDetailBean.setPartnerCuid(serviceInfo.getPartnerCuid());
		serviceDetailBean.setErfCustPartnerLeId(serviceInfo.getErfCustPartnerLeId());
		serviceDetailBean.setOpportunityType(serviceInfo.getOpportunityType());
		serviceDetailBean.setOrderCategory(serviceInfo.getCurrentOpportunityType());
		constructAssetAttributes(serviceInfo, serviceDetailBean);
		try {
			getDemoOrderContractDetails(serviceDetailBean.getAssetAttributes(),serviceInfo);
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
		serviceDetailBeanSet.add(serviceDetailBean);
	}


	private void constructAssetAttributes(SIServiceInfo serviceInfo, SIServiceDetailsBean serviceDetailBean) {
		Set<SIServiceAttributeBean> assetAttributes = new HashSet<>();
		LOGGER.info("Inside constructAssetAttributes for service id", serviceDetailBean.getTpsServiceId());
		List<Map<String, Object>> assetDetails = siServiceInfoRepository
				.getAssetDetailWithAttributes(serviceInfo.getId());
		assetDetails.stream().forEach(entry -> {
			SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
			//siServiceAttributeBean.setCategory(entry.get("component").toString());
			siServiceAttributeBean.setAttributeName("CPE Basic Chassis");
			String cpeModel = (String)entry.get("cpe_model");
			siServiceAttributeBean.setAttributeValue(cpeModel);
			LOGGER.info("Attribute bean is ---> {} ", siServiceAttributeBean);
			assetAttributes.add(siServiceAttributeBean);
			
			if(entry.get(MACDConstants.OEM_VENDOR) != null) {
				SIServiceAttributeBean siServiceAttributeOem = new SIServiceAttributeBean();
				//siServiceAttributeBean.setCategory(entry.get("component").toString());
				siServiceAttributeOem.setAttributeName("oem");
				String oem = (String)entry.get(MACDConstants.OEM_VENDOR);
				siServiceAttributeOem.setAttributeValue(oem);
				assetAttributes.add(siServiceAttributeOem);
				
			} else if(entry.get(MACDConstants.CPE_SERIAL_NO) != null) {
				SIServiceAttributeBean siServiceAttributeCpeSerialNo = new SIServiceAttributeBean();
				//siServiceAttributeBean.setCategory(entry.get("component").toString());
				siServiceAttributeCpeSerialNo.setAttributeName("cpeSerialNumber");
				String cpeSerialNo = (String)entry.get(MACDConstants.CPE_SERIAL_NO);
				siServiceAttributeCpeSerialNo.setAttributeValue(cpeSerialNo);
				assetAttributes.add(siServiceAttributeCpeSerialNo);
			}

			  //added for multi site excel
			  if(Objects.nonNull(serviceInfo.getServiceManagementOption())&&serviceInfo.
					getServiceManagementOption().equalsIgnoreCase("Unmanaged")) {
				  LOGGER.info("INside Unmanaged");
				SIServiceAttributeBean sicpeSupportType = new SIServiceAttributeBean();
				sicpeSupportType.setAttributeName("CPE Support Type");
				sicpeSupportType.setAttributeValue("Customer provided");
				assetAttributes.add(sicpeSupportType);

				SIServiceAttributeBean sicpeMangementType = new SIServiceAttributeBean();
				sicpeMangementType.setAttributeName("CPE Management Type");
				sicpeMangementType.setAttributeValue("Unmanaged");
				assetAttributes.add(sicpeMangementType);



			} if(Objects.nonNull(serviceInfo.getServiceManagementOption())&&serviceInfo.
					getServiceManagementOption().equalsIgnoreCase("managed")) {
				LOGGER.info("INside managed");
				String scopeManagement = (String)entry.get("scope_management");
				String cpeSupportType = (String)entry.get("cpe_support_type");
				SIServiceAttributeBean sicpeSupportType = new SIServiceAttributeBean();
				sicpeSupportType.setAttributeName("CPE Support Type");
				sicpeSupportType.setAttributeValue(cpeSupportType);
				assetAttributes.add(sicpeSupportType);

				SIServiceAttributeBean sicpeMangementType = new SIServiceAttributeBean();
				sicpeMangementType.setAttributeName("CPE Management Type");
				sicpeMangementType.setAttributeValue(scopeManagement);
				assetAttributes.add(sicpeMangementType);
			}

		});
		List<Map<String, Object>> serviceAttributeDetails =	siServiceInfoRepository.getServiceAttributesBasedOnServiceDetailId(serviceInfo.getId());
		try {
			if(!serviceAttributeDetails.isEmpty())
				LOGGER.info("serviceAttributeDetails {}", Utils.convertObjectToJson(serviceAttributeDetails));
		} catch (TclCommonException e) {
			LOGGER.error("error when converting object to json getServiceAttributesBasedOnServiceDetailId");
		}
		serviceAttributeDetails.stream().forEach(attribute -> {
			LOGGER.info("attribute {}", attribute.get("attribute_name"));
			if(attribute.get(MACDConstants.VENDOR_ID) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.VENDOR_ID);
				String vendorId = (String)attribute.get(MACDConstants.VENDOR_ID);
				siServiceAttributeBean.setAttributeValue(vendorId);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("Vendor ID : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.VENDOR_ID), serviceInfo.getId());
			} else if(attribute.get(MACDConstants.VENDOR_NAME) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.VENDOR_NAME);
				String vendorName = (String)attribute.get(MACDConstants.VENDOR_NAME);
				siServiceAttributeBean.setAttributeValue(vendorName);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("Vendor NAME : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.VENDOR_NAME), serviceInfo.getId());
			} else if(attribute.get(MACDConstants.BURSTABLE_BW) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.BURSTABLE_BW);
				String burstableBw = (String)attribute.get(MACDConstants.BURSTABLE_BW);
				siServiceAttributeBean.setAttributeValue(burstableBw);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("BURSTABLE_BW : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.BURSTABLE_BW), serviceInfo.getId());
			} else if(attribute.get(MACDConstants.LOCAL_LOOP_BW) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.LOCAL_LOOP_BW);
				String localLoopBw = (String)attribute.get(MACDConstants.LOCAL_LOOP_BW);
				siServiceAttributeBean.setAttributeValue(localLoopBw);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("LOCAL_LOOP_BW : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.LOCAL_LOOP_BW), serviceInfo.getId());
			} else if(attribute.get(MACDConstants.SITE_TYPE) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.SITE_TYPE);
				String siteType = (String)attribute.get(MACDConstants.SITE_TYPE);
				siServiceAttributeBean.setAttributeValue(siteType);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("SITE_TYPE : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.SITE_TYPE), serviceInfo.getId());
			}  else if(attribute.get(MACDConstants.CPE_BASIC_CHASSIS) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.CPE_BASIC_CHASSIS);
				String cpeBasicChassis = (String)attribute.get(MACDConstants.CPE_BASIC_CHASSIS);
				siServiceAttributeBean.setAttributeValue(cpeBasicChassis);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("CPE_BASIC_CHASSIS : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.CPE_BASIC_CHASSIS), serviceInfo.getId());
			} else if(attribute.get(MACDConstants.BTS_DEVICE_TYPE) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.BTS_DEVICE_TYPE);
				String btsDeviceType = (String)attribute.get(MACDConstants.BTS_DEVICE_TYPE);
				siServiceAttributeBean.setAttributeValue(btsDeviceType);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("BTS_DEVICE_TYPE : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.BTS_DEVICE_TYPE), serviceInfo.getId());
			} else if(attribute.get(MACDConstants.B_END_BACKHAUL_PROVIDER) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.B_END_BACKHAUL_PROVIDER);
				String bendBackHaulProvider = (String)attribute.get(MACDConstants.B_END_BACKHAUL_PROVIDER);
				siServiceAttributeBean.setAttributeValue(bendBackHaulProvider);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("B_END_BACKHAUL_PROVIDER : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.B_END_BACKHAUL_PROVIDER), serviceInfo.getId());
			} else if(attribute.get(MACDConstants.A_END_BACKHAUL_PROVIDER) != null) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.A_END_BACKHAUL_PROVIDER);
				String aendBackhaulProvider = (String)attribute.get(MACDConstants.A_END_BACKHAUL_PROVIDER);
				siServiceAttributeBean.setAttributeValue(aendBackhaulProvider);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("A_END_BACKHAUL_PROVIDER : {}, service detail id {}", 
						(String)attribute.get(MACDConstants.A_END_BACKHAUL_PROVIDER), serviceInfo.getId());
			} else if(MACDConstants.PRODUCT_FLAVOUR.equals(attribute.get("attribute_name"))) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.PRODUCT_FLAVOUR);
				String productFlavour = (String)attribute.get("attribute_value");
				siServiceAttributeBean.setAttributeValue(productFlavour);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("PRODUCT_FLAVOUR : {}, service detail id {}", 
						(String)attribute.get("attribute_value"), serviceInfo.getId());
			} else if(MACDConstants.LL_ARRANGE_BY.equals(attribute.get("attribute_name"))) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.LL_ARRANGE_BY);
				String llArrangedBy = (String)attribute.get("attribute_value");
				siServiceAttributeBean.setAttributeValue(llArrangedBy);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("LL_ARRANGE_BY : {}, service detail id {}",
						(String)attribute.get("attribute_value"), serviceInfo.getId());
			} else if(MACDConstants.SHARED_LM_REQUIRED.equals(attribute.get("attribute_name"))) {
				SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
				siServiceAttributeBean.setAttributeName(MACDConstants.SHARED_LM_REQUIRED);
				String sharedLmRequired = (String)attribute.get("attribute_value");
				siServiceAttributeBean.setAttributeValue(sharedLmRequired);
				assetAttributes.add(siServiceAttributeBean);
				LOGGER.info("SHARED_LM_REQUIRED : {}, service detail id {}",
						(String)attribute.get("attribute_value"), serviceInfo.getId());
			}
				
			
		});
		serviceDetailBean.setAssetAttributes(assetAttributes);
		
	}

	/**
	 * Method to get list of detailedInfo
	 * @param serviceIdList
	 * @param productName
	 * @return
	 */
	public List<SIServiceDetailedResponse> getServiceDetailedInfoList(List<String> serviceIdList,String productName, Boolean isTermination)
	{
		Boolean[] isTerm = { Boolean.FALSE };
		List<SIServiceDetailedResponse> serviceDetailResponseList=new ArrayList<>();
		if(isTermination != null)
		{
			LOGGER.info("isTermination is not null, passing value");
			isTerm[0] = isTermination;
		}
		if(Objects.nonNull(serviceIdList))
		{
			serviceIdList.stream().forEach(serviceId -> {
				try {
					SIServiceDetailedResponse response = getDetailedSIInfo(serviceId, productName,isTerm[0]);
					serviceDetailResponseList.add(response);
				}
				catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}


			});

		}
		return serviceDetailResponseList;

	}
	
	/**
	 * Method to get service details for a particular service ID and its related
	 * service id details
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public OrderSummaryBeanResponse getServiceDetailsListPrimarySecondary(String[] serviceId) throws TclCommonException {
		OrderSummaryBeanResponse orderSummaryBeanResponse = new OrderSummaryBeanResponse();
		Map<String,List<SIServiceInfoBean>> siServiceDetailBeanMap = new HashMap<>();
		

		if (serviceId == null || serviceId.length == 0) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		// Optional<SIServiceDetail> optDetail =
		// siServiceDetailRepository.findByTpsServiceId(serviceId);
		List<SIServiceInfo> siServiceInfoList = siServiceInfoRepository.findByServiceIdIn(Arrays.asList(serviceId));

		if (siServiceInfoList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		siServiceInfoList.stream().forEach(serviceInfo -> {
			List<SIServiceInfoBean> serviceInfoBeanList = new ArrayList<>();
			try {
				serviceInfoBeanList.add(constructSiServiceInfoBean(serviceInfo));
			} catch (TclCommonException e) {
				LOGGER.error("Error in construct Si Service Info Bean", e);
			}


			/*
			 * if (StringUtils.isNotBlank(serviceInfo.getPrimarySecondaryLink())) {
			 * Optional<SIServiceInfo> relatedServiceBean = siServiceInfoRepository
			 * .findByServiceId(serviceInfo.getPrimarySecondaryLink());
			 * 
			 * if (!relatedServiceBean.isPresent()) throw new
			 * TclCommonRuntimeException(ExceptionConstants.INVALID_RELATED_SERVICEID,
			 * ResponseResource.R_CODE_ERROR);
			 * 
			 * serviceInfoBeanList.add(constructSiServiceInfoBean(relatedServiceBean.get()))
			 * ;
			 * 
			 * }
			 */
		siServiceDetailBeanMap.put(serviceInfo.getServiceId(),serviceInfoBeanList);
		});
		orderSummaryBeanResponse.setServiceMap(siServiceDetailBeanMap);
		return orderSummaryBeanResponse;

	}


	public List<SIServiceDetailsBean> getOrderDataBasedOnAddSite(String[] serviceIds) throws TclCommonException {
		List<SIServiceDetailsBean>  serviceDetailsBeanList  =  new ArrayList<>();
        if(serviceIds == null || serviceIds.length == 0) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
        List<String> serviceIdsList = Arrays.asList(serviceIds);
       List<ViewSiServiceInfoAll> serviceInfoList =  vwSiServiceInfoAllRepository.findByServiceIdIn(serviceIdsList);
       if( serviceInfoList != null && !serviceInfoList.isEmpty()) {
    	   serviceInfoList.stream().forEach(serviceInfo -> {
    		   LOGGER.info("Servie Info {}", serviceInfo.getServiceId());
    		   SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();
    		   serviceDetailBean.setAccessType(serviceInfo.getAccessType());
    		   serviceDetailBean.setAccessProvider(serviceInfo.getLastMileProvider());
    		   serviceDetailBean.setArc(serviceInfo.getArc());
    		   serviceDetailBean.setBillingFrequency(serviceInfo.getBillingFrequency());
    		   serviceDetailBean.setBillingMethod(serviceInfo.getBillingMethod());
    		   serviceDetailBean.setContractTerm(serviceInfo.getOrderTermInMonths());
    		   serviceDetailBean.setErfCustomerId(serviceInfo.getOrderCustomerId());
    		   serviceDetailBean.setErfCustomerLeId(serviceInfo.getOrderCustLeId());
    		   serviceDetailBean.setErfCustomerLeName(serviceInfo.getOrderCustLeName());
    		   serviceDetailBean.setErfCustomerName(serviceInfo.getOrderCustomer());
    		   serviceDetailBean.setErfPrdCatalogOfferingId(serviceInfo.getProductOfferingId());
    		   serviceDetailBean.setErfPrdCatalogOfferingName(serviceInfo.getProductOfferingName());
    		   serviceDetailBean.setLinkType(serviceInfo.getPrimaryOrSecondary());
    		   serviceDetailBean.setLastmileBw(serviceInfo.getLastMileBandwidth());
    		   serviceDetailBean.setLastmileBwUnit(serviceInfo.getLastMileBandwidthUnit());
    		   serviceDetailBean.setPortBw(serviceInfo.getBandwidth());
    		   serviceDetailBean.setPortBwUnit(serviceInfo.getBandwidthUnit());
    		   serviceDetailBean.setTpsServiceId(serviceInfo.getServiceId());
    		   serviceDetailBean.setPriSecServLink(serviceInfo.getPrimarySecondaryLink());
    		   serviceDetailBean.setNrc(serviceInfo.getNrc());
    		   serviceDetailBean.setReferenceOrderId(serviceInfo.getOrderSysId());
    		   serviceDetailBean.setVpnName(serviceInfo.getVpnName());
    		   serviceDetailBean.setId(serviceInfo.getId());
    		   serviceDetailBean.setErfSpLeId(serviceInfo.getOrderSpLeId());
    		   serviceDetailBean.setErfSpLeName(serviceInfo.getOrderSpLeName());
    		   serviceDetailBean.setCustomerCurrencyId(serviceInfo.getCurrencyId());
    		   //serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
    		   //serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
    		   serviceDetailBean.setOpportunityType(serviceInfo.getOpportunityType());
    		   serviceDetailBean.setMrc(serviceInfo.getMrc());
			   LOGGER.info("Order Partner id for service info id {} is  {} ", serviceInfo.getServiceId(), serviceInfo.getOrderPartner());
    		   serviceDetailBean.setPartnerId(serviceInfo.getOrderPartner());
			   serviceDetailBean.setPartnerCuid(serviceInfo.getPartnerCuid());
			   serviceDetailBean.setErfCustPartnerLeId(serviceInfo.getErfCustPartnerLeId());
			   serviceDetailBean.setDemarcationApartment(serviceInfo.getDemarcationApartment());
			   serviceDetailBean.setDemarcationFloor(serviceInfo.getDemarcationFloor());
			   serviceDetailBean.setDemarcationRoom(serviceInfo.getDemarcationRoom());
			   //serviceDetailBean.setDemarcationRack(serviceInfo.getDemarcationRack());
    		   serviceDetailBean.setContractStartDate(serviceInfo.getContractStartDate());
    		   serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
    		   serviceDetailBean.setServiceManagementOption(serviceInfo.getServiceManagementOption());
    		   serviceDetailBean.setServiceTopology(serviceDetailBean.getServiceTopology());
    		   serviceDetailBean.setSiteTopology(serviceInfo.getGvpnSiteTopology());
    		   serviceDetailBean.setErfLocSiteAddressId(serviceInfo.getLocationId());
    		   
    		   List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository.getAssetDetailWithAttributes(serviceInfo.getId());
   				Optional<Map<String, Object>> assetDetailInfo=assetDetailInfos.stream().findFirst();
   				Map<String, Object> assetDetail=new HashMap<>();
   				if(assetDetailInfo.isPresent()) {
   				assetDetail = assetDetailInfo.get();
   				}
   				
   				if (Objects.nonNull(assetDetail)) {
   					
   					if(Objects.nonNull(serviceDetailBean.getServiceManagementOption())&&serviceDetailBean.getServiceManagementOption().equalsIgnoreCase("Unmanaged"))
   					{
   						serviceDetailBean.setScopeOfManagement("Customer provided");
   						serviceDetailBean.setSupportType("Unmanaged");
   					}
   					else {
   						String scopeManagement = (String) assetDetail.get("scope_management");
   	   					String cpeSupportType = (String) assetDetail.get("cpe_support_type");
   	   					serviceDetailBean.setScopeOfManagement(scopeManagement);
						serviceDetailBean.setSupportType(cpeSupportType);
   					}		
   				}
   				
    		   serviceDetailsBeanList.add(serviceDetailBean);
    	   });
    	   
       }
        return serviceDetailsBeanList;
	}

	public HashSet<SIServiceDetailsBean> getOrderDataForServiceForPriSecAddSite(String[] serviceIds) throws TclCommonException {
		HashSet<SIServiceDetailsBean> serviceDetailBeanSet= new HashSet<>();
		SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();
		LOGGER.info("Inside getOrderDataForServiceForPriSecAddSite for serviceIds {}", serviceIds);

		if(serviceIds == null || serviceIds.length == 0) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<String> serviceIdsList = Arrays.asList(serviceIds);
		List<ViewSiServiceInfoAll> serviceInfoList =  vwSiServiceInfoAllRepository.findByServiceIdIn(serviceIdsList);

		if( serviceInfoList != null && !serviceInfoList.isEmpty()) {


			serviceInfoList.stream().forEach(serviceInfo -> {
				LOGGER.info("Service Info {}", serviceInfo.getServiceId());

				constructServiceDetailForAddSite(serviceInfo,serviceDetailBeanSet);
			});

		}

		List<SIServiceDetailsBean>  serviceDetailsBeanList  =  new ArrayList<SIServiceDetailsBean>(serviceDetailBeanSet);

		LOGGER.info("Fetched data for {} circuit", serviceDetailsBeanList.stream().findFirst().get().getLinkType());

		if (Objects.nonNull(serviceDetailsBeanList))
		{
			serviceDetailsBeanList.stream().forEach(siServiceDetailsBean -> {
				if (StringUtils.isNotBlank(siServiceDetailsBean.getPriSecServLink())) {
					String secondaryCircuit = siServiceDetailsBean.getPriSecServLink();
					Optional<ViewSiServiceInfoAll> secondaryServiceInfo = vwSiServiceInfoAllRepository.findByServiceId(secondaryCircuit);
					constructServiceDetailForAddSite(secondaryServiceInfo.get(),serviceDetailBeanSet);
					LOGGER.info("Fetched data for related circuit {}", secondaryCircuit);
				}
			});
		}
		LOGGER.info("ServiceDetailsBeanSet for Add Site {}" ,serviceDetailBeanSet);
		return serviceDetailBeanSet;
	}

	private void constructServiceDetailForAddSite(ViewSiServiceInfoAll serviceInfo, HashSet<SIServiceDetailsBean> serviceDetailBeanSet)
	{
		LOGGER.info("Service Info inside constructServiceDetailForAddSite {}", serviceInfo.getServiceId());

		SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();
		serviceDetailBean.setAccessType(serviceInfo.getAccessType());
		serviceDetailBean.setAccessProvider(serviceInfo.getLastMileProvider());
		serviceDetailBean.setArc(serviceInfo.getArc());
		serviceDetailBean.setBillingFrequency(serviceInfo.getBillingFrequency());
		serviceDetailBean.setBillingMethod(serviceInfo.getBillingMethod());
		serviceDetailBean.setContractTerm(serviceInfo.getOrderTermInMonths());
		serviceDetailBean.setErfCustomerId(serviceInfo.getOrderCustomerId());
		serviceDetailBean.setErfCustomerLeId(serviceInfo.getOrderCustLeId());
		serviceDetailBean.setErfCustomerLeName(serviceInfo.getOrderCustLeName());
		serviceDetailBean.setErfCustomerName(serviceInfo.getOrderCustomer());
		serviceDetailBean.setErfPrdCatalogOfferingId(serviceInfo.getProductOfferingId());
		serviceDetailBean.setErfPrdCatalogOfferingName(serviceInfo.getProductOfferingName());
		serviceDetailBean.setLinkType(serviceInfo.getPrimaryOrSecondary());
		serviceDetailBean.setLastmileBw(serviceInfo.getLastMileBandwidth());
		serviceDetailBean.setLastmileBwUnit(serviceInfo.getLastMileBandwidthUnit());
		serviceDetailBean.setPortBw(serviceInfo.getBandwidth());
		serviceDetailBean.setPortBwUnit(serviceInfo.getBandwidthUnit());
		serviceDetailBean.setTpsServiceId(serviceInfo.getServiceId());
		serviceDetailBean.setPriSecServLink(serviceInfo.getPrimarySecondaryLink());
		serviceDetailBean.setNrc(serviceInfo.getNrc());
		serviceDetailBean.setReferenceOrderId(serviceInfo.getOrderSysId());
		serviceDetailBean.setVpnName(serviceInfo.getVpnName());
		serviceDetailBean.setId(serviceInfo.getId());
		serviceDetailBean.setErfSpLeId(serviceInfo.getOrderSpLeId());
		serviceDetailBean.setErfSpLeName(serviceInfo.getOrderSpLeName());
		serviceDetailBean.setCustomerCurrencyId(serviceInfo.getCurrencyId());
		//serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
		//serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
		serviceDetailBean.setOpportunityType(serviceInfo.getOpportunityType());
		serviceDetailBean.setMrc(serviceInfo.getMrc());
		LOGGER.info("Order Partner id for service info id {} is  {} ", serviceInfo.getServiceId(), serviceInfo.getOrderPartner());
		serviceDetailBean.setPartnerId(serviceInfo.getOrderPartner());
		//serviceDetailBean.setPartnerCuid(serviceInfo.getPartnerCuid());
		//serviceDetailBean.setErfCustPartnerLeId(serviceInfo.getErfCustPartnerLeId());
		serviceDetailBean.setDemarcationApartment(serviceInfo.getDemarcationApartment());
		serviceDetailBean.setDemarcationFloor(serviceInfo.getDemarcationFloor());
		serviceDetailBean.setDemarcationRoom(serviceInfo.getDemarcationRoom());
		//serviceDetailBean.setDemarcationRack(serviceInfo.getDemarcationRack());
		serviceDetailBean.setContractStartDate(serviceInfo.getContractStartDate());
		serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
		serviceDetailBean.setServiceManagementOption(serviceInfo.getServiceManagementOption());
		serviceDetailBean.setServiceTopology(serviceDetailBean.getServiceTopology());
		serviceDetailBean.setSiteTopology(serviceInfo.getGvpnSiteTopology());

		List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository.getAssetDetailWithAttributes(serviceInfo.getId());
		Optional<Map<String, Object>> assetDetailInfo=assetDetailInfos.stream().findFirst();
		Map<String, Object> assetDetail=new HashMap<>();
		if(assetDetailInfo.isPresent()) {
			assetDetail = assetDetailInfo.get();
		}

		if (Objects.nonNull(assetDetail)) {

			if(Objects.nonNull(serviceDetailBean.getServiceManagementOption())&&serviceDetailBean.getServiceManagementOption().equalsIgnoreCase("Unmanaged"))
			{
				serviceDetailBean.setScopeOfManagement("Customer provided");
				serviceDetailBean.setSupportType("Unmanaged");
			}
			else {
				String scopeManagement = (String) assetDetail.get("scope_management");
				String cpeSupportType = (String) assetDetail.get("cpe_support_type");
				serviceDetailBean.setScopeOfManagement(scopeManagement);
				serviceDetailBean.setSupportType(cpeSupportType);
			}
		}

		serviceDetailBeanSet.add(serviceDetailBean);
	}


	@Transactional
	public SIOrderDataBean getMigrationServiceInventoryDetails(String serviceId) {
		List<SIServiceDetail> siServiceDetailList=siServiceDetailRepository.findByTpsServiceIdAndServiceStatusIn(serviceId,Arrays.asList("Active","Under Provisioning"));
		if(Objects.nonNull(siServiceDetailList) && !siServiceDetailList.isEmpty()){
			Optional<SIServiceDetail> siDetailOptional=siServiceDetailList.stream().findFirst();
			if(siDetailOptional.isPresent()){
				Integer orderId=siDetailOptional.get().getSiOrder().getId();
				Optional<SIOrder> siOrderOptional=siOrderRepository.findById(orderId);
				if(siOrderOptional.isPresent()){
					SIOrderDataBean siOrderDataBean = new SIOrderDataBean();
					SIOrder siOrder=siOrderOptional.get();
					populateOrderData(siOrderDataBean,siOrder);
					populateMigrationServiceDetails(siOrderDataBean,siServiceDetailList);
					return siOrderDataBean;
				}
			}
		}
		return null;
	}
	
	private void populateMigrationServiceDetails(SIOrderDataBean siOrderDataBean, List<SIServiceDetail> siServiceDetailList) {
		List<SIServiceDetailDataBean> serviceDetailBeans = new ArrayList<>();
		siServiceDetailList.stream().forEach(siServiceDetail ->{
			SIServiceDetailDataBean bean = new SIServiceDetailDataBean();
			bean.setId(siServiceDetail.getId());
			bean.setBillingAccountId(siServiceDetail.getBillingAccountId());
			bean.setErfLocSiteAddressId(siServiceDetail.getErfLocSiteAddressId());
			bean.setBillingType(siServiceDetail.getBillingType());
			bean.setErfLocGscDestinationCityId(siServiceDetail.getErfLocDestinationCityId());
			bean.setErfLocGscDestinationCountryId(siServiceDetail.getErfLocDestinationCountryId());
			bean.setErfLocGscSourceCityId(siServiceDetail.getErfLocSourceCityId());
			bean.setErfLocGscSrcCountryId(siServiceDetail.getErfLocSrcCountryId());
			bean.setErfPrdCatalogOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
			bean.setErfPrdCatalogOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
			bean.setErfPrdCatalogParentProductName(siServiceDetail.getErfPrdCatalogParentProductName());
			bean.setErfPrdCatalogParentProductOfferingName(
					siServiceDetail.getErfPrdCatalogParentProductOfferingName());
			bean.setErfPrdCatalogProductId(siServiceDetail.getErfPrdCatalogProductId());
			bean.setErfPrdCatalogProductName(siServiceDetail.getErfPrdCatalogProductName());
			bean.setFeasibilityId(siServiceDetail.getFeasibilityId());
			bean.setGscDestinationCity(siServiceDetail.getDestinationCity());
			bean.setGscDestinationCountry(siServiceDetail.getDestinationCountry());
			bean.setSourceCountryCode(siServiceDetail.getSourceCountryCode());
			bean.setDestinationCountryCode(siServiceDetail.getDestinationCountryCode());
			bean.setGscSourceCity(siServiceDetail.getSourceCity());
			bean.setGscSourceCountry(siServiceDetail.getSourceCountry());
			bean.setParentBundleServiceId(siServiceDetail.getParentBundleServiceId());
			bean.setOrderUuid(siServiceDetail.getSiOrderUuid());
			bean.setTpsServiceId(siServiceDetail.getTpsServiceId());
			bean.setTpsSourceServiceId(siServiceDetail.getTpsSourceServiceId());
			bean.setUuid(siServiceDetail.getUuid());
			bean.setMrc(siServiceDetail.getMrc());
			bean.setArc(siServiceDetail.getArc());
			bean.setNrc(siServiceDetail.getNrc());
			bean.setLastmileBw(siServiceDetail.getLastmileBw());
			bean.setLastmileBwUnit(siServiceDetail.getLastmileBwUnit());
			bean.setPortBw(siServiceDetail.getBwPortspeed());
			bean.setPortBwUnit(siServiceDetail.getBwUnit());
			bean.setAlias(siServiceDetail.getSiteAlias());
			bean.setServiceCommissionedDate(siServiceDetail.getServiceCommissionedDate());
			bean.setLinkId(siServiceDetail.getPriSecServiceLink());
			bean.setLinkType(siServiceDetail.getPrimarySecondary());
			bean.setLatLong(siServiceDetail.getLatLong());
			bean.setVpnName(siServiceDetail.getVpnName());
			bean.setAccessProvider(siServiceDetail.getLastmileProvider());
			bean.setAccessType(siServiceDetail.getAccessType());
			bean.setSiteAddress(siServiceDetail.getSiteAddress());
			bean.setPriSecServLink(siServiceDetail.getPriSecServiceLink());
			bean.setCreatedBy(siServiceDetail.getCreatedBy());
			bean.setUpdatedBy(siServiceDetail.getUpdatedBy());
			bean.setCreatedDate(siServiceDetail.getCreatedDate());
			bean.setUpdatedDate(siServiceDetail.getUpdatedDate());
			bean.setIpAddressArrangementType(siServiceDetail.getIpAddressArrangementType());
			bean.setLastMileProvider(siServiceDetail.getLastmileProvider());
			bean.setLmType(siServiceDetail.getAccessType());
			bean.setServiceOption(siServiceDetail.getServiceOption());
			bean.setSiteTopology(siServiceDetail.getSiteTopology());
			bean.setServiceTopology(siServiceDetail.getServiceTopology());
			bean.setSiteType(siServiceDetail.getSiteType());
			bean.setOrderType(siServiceDetail.getOrderType());
			bean.setOrderCategory(siServiceDetail.getOrderCategory());
			final ObjectMapper mapper=new ObjectMapper();
			LOGGER.info("serviceDetailId"+siServiceDetail.getId());
			List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(siServiceDetail.getId());
			LOGGER.info("data {}", data);
			if(data.stream().findFirst().isPresent()) {
				ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
				bean.setContractTerm(serviceDetail.getTermInMonths());
				bean.setReferenceOrderId(siServiceDetail.getGscOrderSequenceId());
				bean.setBillingFrequency(serviceDetail.getBillingFrequency());
				bean.setBillingMethod(serviceDetail.getBillingMethod());
				bean.setPaymentTerm(serviceDetail.getPaymentTerm());
			}
			bean.setContractInfo(populateContractInfo(siServiceDetail.getSiContractInfo()));
			bean.setAttributes(populateServiceAttribute(siServiceDetail.getSiServiceAttributes()));
			com.tcl.dias.common.serviceinventory.beans.SIAttributeBean ipAddrBean=populateServiceAttr("IP Address Arrangement",applyIpAddressArrangementTypeRule(siServiceDetail.getIpAddressArrangementType()),"Internet Port");
			bean.getAttributes().add(ipAddrBean);
			bean.setComponents(populateComponent(siServiceDetail));
			serviceDetailBeans.add(bean);
		});
		siOrderDataBean.setServiceDetails(serviceDetailBeans);
	}

	private void populateServiceDetails(SIOrderDataBean siOrderDataBean, SIOrder siOrder) {
		List<SIServiceDetailDataBean> serviceDetailBeans = new ArrayList<>();
		siOrder.getSiServiceDetails().stream().forEach(siServiceDetail ->{
			SIServiceDetailDataBean bean = new SIServiceDetailDataBean();
			bean.setId(siServiceDetail.getId());
			bean.setBillingAccountId(siServiceDetail.getBillingAccountId());
			bean.setErfLocSiteAddressId(siServiceDetail.getErfLocSiteAddressId());
			bean.setBillingType(siServiceDetail.getBillingType());
			bean.setErfLocGscDestinationCityId(siServiceDetail.getErfLocDestinationCityId());
			bean.setErfLocGscDestinationCountryId(siServiceDetail.getErfLocDestinationCountryId());
			bean.setErfLocGscSourceCityId(siServiceDetail.getErfLocSourceCityId());
			bean.setErfLocGscSrcCountryId(siServiceDetail.getErfLocSrcCountryId());
			bean.setErfPrdCatalogOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
			bean.setErfPrdCatalogOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
			bean.setErfPrdCatalogParentProductName(siServiceDetail.getErfPrdCatalogParentProductName());
			bean.setErfPrdCatalogParentProductOfferingName(
					siServiceDetail.getErfPrdCatalogParentProductOfferingName());
			bean.setErfPrdCatalogProductId(siServiceDetail.getErfPrdCatalogProductId());
			bean.setErfPrdCatalogProductName(siServiceDetail.getErfPrdCatalogProductName());
			bean.setFeasibilityId(siServiceDetail.getFeasibilityId());
			bean.setGscDestinationCity(siServiceDetail.getDestinationCity());
			bean.setGscDestinationCountry(siServiceDetail.getDestinationCountry());
			bean.setSourceCountryCode(siServiceDetail.getSourceCountryCode());
			bean.setDestinationCountryCode(siServiceDetail.getDestinationCountryCode());
			bean.setGscSourceCity(siServiceDetail.getSourceCity());
			bean.setGscSourceCountry(siServiceDetail.getSourceCountry());
			bean.setParentBundleServiceId(siServiceDetail.getParentBundleServiceId());
			bean.setOrderUuid(siServiceDetail.getSiOrderUuid());
			bean.setTpsServiceId(siServiceDetail.getTpsServiceId());
			bean.setTpsSourceServiceId(siServiceDetail.getTpsSourceServiceId());
			bean.setUuid(siServiceDetail.getUuid());
			bean.setMrc(siServiceDetail.getMrc());
			bean.setArc(siServiceDetail.getArc());
			bean.setNrc(siServiceDetail.getNrc());
			bean.setLastmileBw(siServiceDetail.getLastmileBw());
			bean.setLastmileBwUnit(siServiceDetail.getLastmileBwUnit());
			bean.setPortBw(siServiceDetail.getBwPortspeed());
			bean.setPortBwUnit(siServiceDetail.getBwUnit());
			bean.setAlias(siServiceDetail.getSiteAlias());
			bean.setServiceCommissionedDate(siServiceDetail.getServiceCommissionedDate());
			bean.setLinkId(siServiceDetail.getPriSecServiceLink());
			bean.setLinkType(siServiceDetail.getPrimarySecondary());
			bean.setLatLong(siServiceDetail.getLatLong());
			bean.setVpnName(siServiceDetail.getVpnName());
			bean.setAccessProvider(siServiceDetail.getLastmileProvider());
			bean.setAccessType(siServiceDetail.getAccessType());
			bean.setSiteAddress(siServiceDetail.getSiteAddress());
			bean.setPriSecServLink(siServiceDetail.getPriSecServiceLink());
			bean.setCreatedBy(siServiceDetail.getCreatedBy());
			bean.setUpdatedBy(siServiceDetail.getUpdatedBy());
			bean.setCreatedDate(siServiceDetail.getCreatedDate());
			bean.setUpdatedDate(siServiceDetail.getUpdatedDate());
			bean.setIpAddressArrangementType(siServiceDetail.getIpAddressArrangementType());
			bean.setLastMileProvider(siServiceDetail.getLastmileProvider());
			bean.setLmType(siServiceDetail.getAccessType());
			bean.setServiceOption(siServiceDetail.getServiceOption());
			bean.setSiteTopology(siServiceDetail.getSiteTopology());
			bean.setServiceTopology(siServiceDetail.getServiceTopology());
			bean.setSiteType(siServiceDetail.getSiteType());
			final ObjectMapper mapper=new ObjectMapper();
			LOGGER.info("serviceDetailId"+siServiceDetail.getId());
			List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(siServiceDetail.getId());
			LOGGER.info("data {}", data);
			if(data.stream().findFirst().isPresent()) {
				ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(), ServiceDetailBean.class);
				bean.setContractTerm(serviceDetail.getTermInMonths());
				bean.setReferenceOrderId(siServiceDetail.getGscOrderSequenceId());
				bean.setBillingFrequency(serviceDetail.getBillingFrequency());
				bean.setBillingMethod(serviceDetail.getBillingMethod());
				bean.setPaymentTerm(serviceDetail.getPaymentTerm());
			}
			bean.setContractInfo(populateContractInfo(siServiceDetail.getSiContractInfo()));
			bean.setAttributes(populateServiceAttribute(siServiceDetail.getSiServiceAttributes()));
			com.tcl.dias.common.serviceinventory.beans.SIAttributeBean ipAddrBean=populateServiceAttr("IP Address Arrangement",applyIpAddressArrangementTypeRule(siServiceDetail.getIpAddressArrangementType()),"Internet Port");
			bean.getAttributes().add(ipAddrBean);
			bean.setComponents(populateComponent(siServiceDetail));
			serviceDetailBeans.add(bean);
		});
		siOrderDataBean.setServiceDetails(serviceDetailBeans);
	}

	private com.tcl.dias.common.beans.SIComponentAttributeBean populateComponentAttr(String attrName,
																					 String attrValue) {
		SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
		siComponentAttributeBean.setAttributeAltValueLabel(attrName);
		siComponentAttributeBean.setAttributeName(attrName);
		siComponentAttributeBean.setAttributeValue(attrValue);
		siComponentAttributeBean.setIsActive("Y");
		siComponentAttributeBean.setCreatedBy("OPTIMUS ETL DATA MASSAGING");
		siComponentAttributeBean.setCreatedDate(new Timestamp(new Date().getTime()));
		siComponentAttributeBean.setUpdatedBy("OPTIMUS ETL DATA MASSAGING");
		siComponentAttributeBean.setUpdatedDate(new Timestamp(new Date().getTime()));
		return siComponentAttributeBean;
	}

	private String applyLMTypeRule(String inputLmType) {
		String lmType=null;
		if(Objects.nonNull(inputLmType)){
			if("ONNET WIRELINE".equalsIgnoreCase(inputLmType)){
				lmType="OnnetWL";
			}else if("OFFNET WIRELINE".equalsIgnoreCase(inputLmType)){
				lmType="OffnetWL";
			}else if("ONNET WIRELESS".equalsIgnoreCase(inputLmType)){
				lmType="OnnetRF";
			}/*else if("WIRELESS".equalsIgnoreCase(inputLmType)){
				lmType="OnnetRF";
			}*/else if("OFFNET WIRELESS".equalsIgnoreCase(inputLmType)){
				lmType="OffnetRF";
			}
		}
		return lmType;
	}

	private String applyIpAddressArrangementTypeRule(String ipAddressArrangementType) {
		String ipType=null;
		if(Objects.nonNull(ipAddressArrangementType)){
			if(ipAddressArrangementType.contains("Dual") || ipAddressArrangementType.contains("DUAL") || (ipAddressArrangementType.contains("IPv4") && ipAddressArrangementType.contains("IPv6"))){
				ipType="Dual";
			}else if(ipAddressArrangementType.contains("IPv4")){
				ipType="IPv4";
			}else if(ipAddressArrangementType.contains("IPv6")){
				ipType="IPv6";
			}
		}
		return ipType;
	}

	private SIContractInfoBean populateContractInfo(SIContractInfo siContractInfo) {
		SIContractInfoBean siContractInfoBean =new SIContractInfoBean();
		siContractInfoBean.setAccountManager(siContractInfo.getAccountManager());
		siContractInfoBean.setAccountManagerEmail(siContractInfo.getAccountManagerEmail());
		siContractInfoBean.setArc(siContractInfo.getArc());
		siContractInfoBean.setBillingAddress(siContractInfo.getBillingAddress());
		siContractInfoBean.setBillingContactId(siContractInfo.getBillingContactId());
		siContractInfoBean.setBillingCurrency(siContractInfo.getBillingCurrency());
		siContractInfoBean.setBillingFrequency(siContractInfo.getBillingFrequency());
		siContractInfoBean.setBillingMethod(siContractInfo.getBillingMethod());
		siContractInfoBean.setContractEndDate(siContractInfo.getContractEndDate());
		siContractInfoBean.setContractStartDate(siContractInfo.getContractStartDate());
		siContractInfoBean.setCreatedBy(siContractInfo.getCreatedBy());
		siContractInfoBean.setCreatedDate(siContractInfo.getCreatedDate());
		siContractInfoBean.setCustomerContact(siContractInfo.getCustomerContact());
		siContractInfoBean.setCustomerContactEmail(siContractInfo.getCustomerContactEmail());
		siContractInfoBean.setDiscountArc(siContractInfo.getDiscountArc());
		siContractInfoBean.setDiscountMrc(siContractInfo.getDiscountMrc());
		siContractInfoBean.setDiscountNrc(siContractInfo.getDiscountNrc());
		siContractInfoBean.setErfCustCurrencyId(siContractInfo.getErfCustCurrencyId());
		siContractInfoBean.setErfCustLeId(siContractInfo.getErfCustLeId());
		siContractInfoBean.setErfCustLeName(siContractInfo.getErfCustLeName());
		siContractInfoBean.setErfCustSpLeId(siContractInfo.getErfCustSpLeId());
		siContractInfoBean.setErfCustSpLeName(siContractInfo.getErfCustSpLeName());
		siContractInfoBean.setErfLocBillingLocationId(siContractInfo.getErfLocBillingLocationId());
		siContractInfoBean.setId(siContractInfo.getId());
		siContractInfoBean.setIsActive(siContractInfo.getIsActive());
		siContractInfoBean.setLastMacdDate(siContractInfo.getLastMacdDate());
		siContractInfoBean.setNrc(siContractInfo.getNrc());
		siContractInfoBean.setMrc(siContractInfo.getMrc());
		siContractInfoBean.setOrderTermInMonths(siContractInfo.getOrderTermInMonths());
		siContractInfoBean.setPaymentTerm(siContractInfo.getPaymentTerm());
		siContractInfoBean.setTpsSfdcCuid(siContractInfo.getTpsSfdcCuid());
		siContractInfoBean.setUpdatedBy(siContractInfo.getUpdatedBy());
		siContractInfoBean.setUpdatedDate(siContractInfo.getUpdatedDate());
		return siContractInfoBean;
	}

	private List<SIComponentBean> populateComponent(SIServiceDetail siServiceDetail) {
		List<SIComponentBean> siComponents=new ArrayList<>();
		SIComponentBean siComponentBean =new SIComponentBean();
		siComponentBean.setComponentName("LM");
		siComponentBean.setCreatedBy("OPTIMUS ETL DATA MASSAGING");
		siComponentBean.setCreatedDate(new Timestamp(new Date().getTime()));
		siComponentBean.setIsActive("Y");
		siComponentBean.setSiServiceDetailId(siServiceDetail.getId());
		siComponentBean.setUpdatedBy("OPTIMUS ETL DATA MASSAGING");
		siComponentBean.setUpdatedDate(new Timestamp(new Date().getTime()));
		siComponentBean.setUuid(siServiceDetail.getTpsServiceId());
		com.tcl.dias.common.beans.SIComponentAttributeBean lmComponentAttr=populateComponentAttr("lmType",applyLMTypeRule(siServiceDetail.getAccessType()));
		com.tcl.dias.common.beans.SIComponentAttributeBean lmProviderComponentAttr=populateComponentAttr("lastMileProvider",siServiceDetail.getLastmileProvider());
		com.tcl.dias.common.beans.SIComponentAttributeBean cpeTypeComponentAttr=populateComponentAttr("cpeManagementType",siServiceDetail.getServiceOption());
		com.tcl.dias.common.beans.SIComponentAttributeBean vpnNameComponentAttr=populateComponentAttr("programName",siServiceDetail.getVpnName());
		SIComponentAttributeBean extendedLanComponentAttr=applyExtendedLanRule(siServiceDetail.getSiServiceAttributes());
		SIComponentAttributeBean wanIpAddressComponentAttr=applyWanIpAddressRule(siServiceDetail.getSiServiceAttributes());
		SIComponentAttributeBean wanIpProvidedByCustComponentAttr=applyWanIpProvidedByCustRule(siServiceDetail.getSiServiceAttributes());
		SIComponentAttributeBean nnidAttr=applyNNIDRule(siServiceDetail.getSiServiceAttributes());
		SIComponentAttributeBean custProvidedAsNumberAttr=applyCustProvidedAsNumberRule(siServiceDetail.getSiServiceAttributes());
		List<SIComponentAttributeBean> siComponentAttrs= new ArrayList<>();
		siComponentAttrs.add(lmComponentAttr);
		siComponentAttrs.add(lmProviderComponentAttr);
		siComponentAttrs.add(cpeTypeComponentAttr);
		siComponentAttrs.add(vpnNameComponentAttr);
		siComponentAttrs.add(extendedLanComponentAttr);
		siComponentAttrs.add(wanIpAddressComponentAttr);
		siComponentAttrs.add(wanIpProvidedByCustComponentAttr);
		 siComponentAttrs.add(nnidAttr);
		siComponentAttrs.add(custProvidedAsNumberAttr);
		SIComponentAttribute endMuxNodeIpComponentAttribute=serviceInventoryHelperMapper.getSiComponentAttrFindFirstOrderByIdDesc(siServiceDetail,"endMuxNodeIp");
		if(Objects.nonNull(endMuxNodeIpComponentAttribute)){
			siComponentAttrs.add(populateComponentAttr("endMuxNodeIp",endMuxNodeIpComponentAttribute.getAttributeValue()));
		}
		SIComponentAttribute endMuxNodeNameComponentAttribute=serviceInventoryHelperMapper.getSiComponentAttrFindFirstOrderByIdDesc(siServiceDetail,"endMuxNodeName");
		if(Objects.nonNull(endMuxNodeNameComponentAttribute)){
			siComponentAttrs.add(populateComponentAttr("endMuxNodeName",endMuxNodeNameComponentAttribute.getAttributeValue()));
		}
		List<String> attributes = new ArrayList<>(Arrays.asList("endMuxNodePort","muxMake","muxMakeModel","amcStartDate","cpeAmcStartDate" ,"amcEndDate","cpeAmcEndDate", "cpeInstallationPoNumber", "cpeInstallationPrVendorName","cpeInstallationPrNumber", "cpeInstallationPoVendorName", "cpeSupplyHardwarePoNumber",
				"cpeSupportPoNumber", "cpeSupportPrVendorName", "cpeSupportPoVendorName","cpeSupportPrNumber","cpeLicencePoNumber", "cpeSupplyHardwarePoNumber","cpeSupplyHardwarePrNumber","cpeSupplyHardwarePoVendorName","cpeSupplyHardwarePrVendorName","cpeSerialNumber","amc_required"));
		addAttributes(attributes,siComponentAttrs,siServiceDetail);
		siComponentBean.setSiComponentAttributes(siComponentAttrs);
		siComponents.add(siComponentBean);
		return siComponents;
	}

	private void addAttributes(List<String> attributes,List<SIComponentAttributeBean> siComponentAttrs,SIServiceDetail siServiceDetail) {		
		if(!attributes.isEmpty()) {
			attributes.forEach(attr->{
				SIComponentAttribute componentAttribute=serviceInventoryHelperMapper.getSiComponentAttrFindFirstOrderByIdDesc(siServiceDetail,attr);
				if(Objects.nonNull(componentAttribute)){
					siComponentAttrs.add(populateComponentAttr(attr,componentAttribute.getAttributeValue()));
				}
			});
		}
	}
	
	private SIComponentAttributeBean applyExtendedLanRule(Set<SIServiceAttribute> siServiceAttributes) {
		String[] extendedLan={"No"};
		siServiceAttributes.stream().filter( ssa -> "Extended LAN Required?".equals(ssa.getAttributeName())).forEach( ssa ->{
			extendedLan[0]=ssa.getAttributeValue();
		});
		return populateComponentAttr("extendedLanRequired",extendedLan[0]);
	}
	
	private SIComponentAttributeBean applyNNIDRule(Set<SIServiceAttribute> siServiceAttributes) {
		String[] nniId={null};
		siServiceAttributes.stream().filter( ssa -> "LM_NNI_ID_AEND".equals(ssa.getAttributeName())).forEach( ssa ->{
			nniId[0]=ssa.getAttributeValue();
		});
		return populateComponentAttr("nniId",nniId[0]);
	}
	private SIComponentAttributeBean applyWanIpAddressRule(Set<SIServiceAttribute> siServiceAttributes) {
		String[] wanIpAddress={null};
		siServiceAttributes.stream().filter( ssa -> "IPV4_CUST_WAN_IP".equals(ssa.getAttributeName())).forEach( ssa ->{
			wanIpAddress[0]=ssa.getAttributeValue();
		});
		return populateComponentAttr("wanIpAddress",wanIpAddress[0]);
	}
	
	private SIComponentAttributeBean applyWanIpProvidedByCustRule(Set<SIServiceAttribute> siServiceAttributes) {
		String[] wanIpProvidedByCust={null};
		siServiceAttributes.stream().filter( ssa -> "WAN_IP_PROV_BY_CUST".equals(ssa.getAttributeName())).forEach( ssa ->{
			wanIpProvidedByCust[0]=ssa.getAttributeValue();
		});
		return populateComponentAttr("wanIpProvidedByCust",wanIpProvidedByCust[0]);
	}
	
	private SIComponentAttributeBean applyCustProvidedAsNumberRule(Set<SIServiceAttribute> siServiceAttributes) {
		String[] custProvidedAsNumber={null};
		siServiceAttributes.stream().filter( ssa -> "AS Number".equals(ssa.getAttributeName())).forEach( ssa ->{
			custProvidedAsNumber[0]=ssa.getAttributeValue();
		});
		return populateComponentAttr("custProvidedAsNumber",custProvidedAsNumber[0]);
	}

	private void populateOrderData(SIOrderDataBean orderDataBean, SIOrder order) {
		orderDataBean.setUuid(order.getUuid());
		orderDataBean.setOpOrderCode(order.getOpOrderCode());
		orderDataBean.setCustomerGroupName(order.getCustomerGroupName());
		orderDataBean.setCustomerSegment(order.getCustomerSegment());
		orderDataBean.setDemoFlag(order.getDemoFlag());
		orderDataBean.setErfCustCustomerId(order.getErfCustCustomerId());
		orderDataBean.setErfCustLeId(order.getErfCustLeId());
		orderDataBean.setErfCustSpLeId(order.getErfCustSpLeId());
		orderDataBean.setErfCustPartnerId(order.getErfCustPartnerId());
		orderDataBean.setOrderCategory(order.getOrderCategory());
		orderDataBean.setOrderStartDate(order.getOrderStartDate());
		orderDataBean.setTpsSapCrnId(order.getTpsSapCrnId());
		orderDataBean.setErfCustLeName(order.getErfCustLeName());
		orderDataBean.setId(order.getId());
		orderDataBean.setSfdcCuid(order.getTpsSfdcCuid());
		orderDataBean.setOrderStatus(order.getOrderStatus());
		orderDataBean.setOrderSource(order.getOrderSource());
		orderDataBean.setOrderType(order.getOrderType());
		orderDataBean.setOrderStartDate(order.getOrderStartDate());
		orderDataBean.setTpsSecsId(order.getTpsSecsId());
		orderDataBean.setTpsSfdcId(order.getTpsCrmOptyId());
		orderDataBean.setTpsCrmCofId(order.getTpsCrmCofId());
		orderDataBean.setErfCustCustomerName(order.getErfCustCustomerName());
		orderDataBean.setErfCustPartnerLeId(order.getErfCustPartnerLeId());
		orderDataBean.setErfCustPartnerName(order.getErfCustPartnerName());
		orderDataBean.setErfCustSpLeName(order.getErfCustSpLeName());
		orderDataBean.setErfUserCustomerUserId(order.getErfUserCustomerUserId());
		orderDataBean.setErfUserInitiatorId(order.getErfUserInitiatorId());
		orderDataBean.setIsBundleOrder(order.getIsBundleOrder());
		orderDataBean.setIsActive(order.getIsActive());
		orderDataBean.setIsMultipleLe(order.getIsMultipleLe());
		orderDataBean.setLastMacdDate(order.getLastMacdDate());
		orderDataBean.setMacdCreatedDate(order.getMacdCreatedDate());
		orderDataBean.setOpportunityClassification(order.getOpportunityClassification());
		orderDataBean.setOrderEndDate(order.getOrderEndDate());
		orderDataBean.setParentId(order.getParentId());
		orderDataBean.setParentOpOrderCode(order.getParentOpOrderCode());
		orderDataBean.setSfdcAccountId(order.getSfdcAccountId());
		orderDataBean.setTpsCrmOptyId(order.getTpsCrmOptyId());
		orderDataBean.setTpsCrmSystem(order.getTpsCrmSystem());
		orderDataBean.setTpsSfdcCuid(order.getTpsSfdcCuid());
		orderDataBean.setPartnerCuid(order.getPartnerCuid());
		orderDataBean.setCreatedBy(order.getCreatedBy());
		orderDataBean.setUpdatedBy(order.getUpdatedBy());
	}

	public List<SIServiceInfoBean> getServiceDetailsforNPL(String serviceId) throws TclCommonException {
		List<SIServiceInfoBean> siServiceDetailBeanList = new ArrayList<>();

		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		
		List<SIServiceInfo> siServiceInfo = siServiceInfoRepository.findByServiceIdNPL(serviceId);

		if (siServiceInfo.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}

		siServiceDetailBeanList.addAll(constructSiServiceInfoBean(siServiceInfo));

		return siServiceDetailBeanList;
	}

	@Transactional(readOnly = false)
	public List<Map<String,Object>> processServiceInventorySatSocData(String serviceCode, String src) {
		SatSocResponse result = null;
		List<Map<String, Object>> results = null;
		try {
			if (Utils.validateAlphaNumberic(serviceCode)) {

				if(StringUtils.isNotBlank(src) && "optimus".equalsIgnoreCase(src)) {
					results = this.jdbcTemplate.queryForList("call proc_service_inventory_data_display('" + serviceCode + "')");
				}else {
					results = this.jdbcTemplate.queryForList("call proc_service_inv_SatSoc('" + serviceCode + "')");
				}
				LOGGER.info("Output result {}", results);
			}else {
				LOGGER.warn("Invalid Service Code {}", serviceCode);
			}
		} catch (Exception e) {
			LOGGER.error("Error in processServiceInventorySatSocData", e);
		}
		return results;
	}

	private List<SIServiceInfoBean> constructSiServiceInfoBean(List<SIServiceInfo> siServiceInfo) {
		List<SIServiceInfoBean> serviceInfoBeanList = new ArrayList<>();
		Set<SIServiceAttributeBean> attributes = new HashSet<>();
		siServiceInfo.stream().forEach(serviceInfo -> {
			SIServiceInfoBean siServiceInfoBean = new SIServiceInfoBean();
			
			siServiceInfoBean.setId(serviceInfo.getId());
			siServiceInfoBean.setTpsServiceId(serviceInfo.getServiceId());
			siServiceInfoBean.setProductName(serviceInfo.getProductFamilyName());
			// siServiceInfoBean.setServiceTopology(serviceInfo.get);
			siServiceInfoBean.setVpnName(serviceInfo.getVpnName());
			siServiceInfoBean.setPrimaryOrSecondary(serviceInfo.getPrimaryOrSecondary());
			LOGGER.info("INTERFACE VALUE "+serviceInfo.getSiteEndInterface());
			siServiceInfoBean.setSiteEndInterface(serviceInfo.getSiteEndInterface());
			siServiceInfoBean.setLatLong(serviceInfo.getLatLong());
			// siServiceInfoBean.setSiteTopology(serviceInfo.getSiteTopology());
			siServiceInfoBean.setProductOfferingName(serviceInfo.getProductOfferingName());
			siServiceInfoBean.setBandwidthPortSpeed(serviceInfo.getBandwidth());
			siServiceInfoBean.setBandwidthUnit(serviceInfo.getBandwidthUnit());
			siServiceInfoBean.setLastMileBandwidth(serviceInfo.getLastMileBandwidth());
			siServiceInfoBean.setLastMileBandwidthUnit(serviceInfo.getLastMileBandwidthUnit());
			siServiceInfoBean.setLastMileProvider(serviceInfo.getLastMileProvider());
			siServiceInfoBean.setLastMileType(serviceInfo.getLastMileType());
			siServiceInfoBean.setBurstableBandwidthPortspeed(serviceInfo.getBurstableBandwidth());
			siServiceInfoBean.setBurstableBandwidthUnit(serviceInfo.getBurstableBandwidthUnit());
			siServiceInfoBean.setServiceOption(serviceInfo.getServiceManagementOption());
			siServiceInfoBean.setServiceStatus(serviceInfo.getServiceStatus());
			siServiceInfoBean.setMrc(serviceInfo.getMrc());
			siServiceInfoBean.setNrc(serviceInfo.getNrc());
			siServiceInfoBean.setArc(serviceInfo.getArc());
			siServiceInfoBean.setSiOrderId(serviceInfo.getOrderSysId());
			siServiceInfoBean.setSiteAddress(serviceInfo.getCustomerSiteAddress());
			siServiceInfoBean.setLocationId(serviceInfo.getLocationId());
			siServiceInfoBean.setPriSecServiceLink(serviceInfo.getPrimarySecondaryLink());
			siServiceInfoBean.setSiteType(serviceInfo.getSiteType());
			siServiceInfoBean.setCommittedSla(serviceInfo.getCommittedSla());
			siServiceInfoBean.setBillingFrequency(serviceInfo.getBillingFrequency());
			siServiceInfoBean.setBillingMethod(serviceInfo.getBillingMethod());
			siServiceInfoBean.setContractTerm(serviceInfo.getOrderTermInMonths());
			siServiceInfoBean.setContractStartDate(serviceInfo.getContractStartDate());
			siServiceInfoBean.setContractEndDate(serviceInfo.getContractEndDate());
			siServiceInfoBean.setSourceCity(serviceInfo.getSourceCity());
			siServiceInfoBean.setTpsCrmCofId(serviceInfo.getTpsCrmCofId());
			siServiceInfoBean.setOrderCode(serviceInfo.getOrderCode());
			siServiceInfoBean.setTpsCopfId(serviceInfo.getTpsCopfId());
			siServiceInfoBean.setOrderCategory(serviceInfo.getCurrentOpportunityType());
			siServiceInfoBean.setCircuitExpiryDate(serviceInfo.getCircuitExpiryDate());
			siServiceInfoBean.setBillingCurrency(serviceInfo.getBillingCurrency());
			siServiceInfoBean.setPortMode(serviceInfo.getSrvPortMode());
			siServiceInfoBean.setCustomerId(serviceInfo.getOrderCustomerId());
			siServiceInfoBean.setCustomerLeId(serviceInfo.getOrderCustLeId());
			siServiceInfoBean.setCustomerName(serviceInfo.getOrderCustomer());
			siServiceInfoBean.setTpsSfdcParentOptyId(serviceInfo.getOpportunityId());
			siServiceInfoBean.setSpLeId(serviceInfo.getOrderSpLeId());
			siServiceInfoBean.setCustomerCurrencyId(serviceInfo.getCurrencyId());
			siServiceInfoBean.setBillingCurrency(serviceInfo.getBillingCurrency());
			siServiceInfoBean.setCurrentOpportunityType(serviceInfo.getCurrentOpportunityType());
			if(serviceInfo.getProductOfferingName().equals("MMR Cross Connect")) {
				Optional<SIServiceAdditionalInfo> crossconnectAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(serviceInfo.getId(), "Cross Connect Type");
				siServiceInfoBean.setCrossConnectType(crossconnectAddInf.get().getAttributeValue());
			}

			// siServiceInfoBean.setTaxExemptionFlag(serviceInfo.getTaxExemptionFlag());
			if(serviceInfo.getCommissionedDate() != null )
			siServiceInfoBean.setServiceCommissionedDate(serviceInfo.getCommissionedDate().toString());
			siServiceInfoBean.setUuid(serviceInfo.getUuid());

			// siServiceInfoBean.setServiceTerminationDate(serviceInfo.getTerminationDate().toString());
			try {
				constructAssetInfo(serviceInfo, siServiceInfoBean, attributes);
				 constructComponentAttributesForNPL(serviceInfo, siServiceInfoBean);
				getDemoOrderContractDetails(attributes, serviceInfo);
			} catch (TclCommonException e) {
				LOGGER.error("Error in constructing asset or demo contract info info", e);
			}





			serviceInfoBeanList.add(siServiceInfoBean);
		
		});
		return serviceInfoBeanList; 
			
		
	}

	private void constructComponentAttributesForNPL(SIServiceInfo serviceInfo, SIServiceInfoBean siServiceInfoBean) {

		List<SIComponentBean> componentList = new ArrayList<>();
		SIComponentBean componentBean = new SIComponentBean();
		componentBean.setComponentName("LM");
		componentBean.setSiServiceDetailId(serviceInfo.getId());
		componentBean.setUuid(serviceInfo.getServiceId());
		componentList.add(componentBean);
		List<SIComponentAttributeBean> componentAttributeBeanList = new ArrayList<>();
		List<Map<String, Object>> serviceComponentAttributeDetails = siServiceInfoRepository.getServiceComponentAttributesBasedOnServiceDetailId(serviceInfo.getId());
		if(serviceComponentAttributeDetails != null && !serviceComponentAttributeDetails.isEmpty()) {
			serviceComponentAttributeDetails.stream().forEach(attribute -> {
				LOGGER.info("Entering with attribute name {}, attributeValue {}", attribute.get("attribute_name"), attribute.get("attribute_value"));
				String attributeName = (String) attribute.get("attribute_name");
				String attributeValue = (String) attribute.get("attribute_value");
				if(attributeName.equalsIgnoreCase(MACDConstants.END_MUX_NODE_IP)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.END_MUX_NODE_IP);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("END_MUX_NODE_IP: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else 	if(attributeName.equalsIgnoreCase(MACDConstants.END_MUX_NODE_NAME)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.END_MUX_NODE_NAME);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("END_MUX_NODE_NAME: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.END_MUX_NODE_PORT)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.END_MUX_NODE_PORT);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("END_MUX_NODE_PORT: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.MUX_MAKE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.MUX_MAKE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("MUX_MAKE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.STRUCTURE_TYPE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.STRUCTURE_TYPE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("STRUCTURE_TYPE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.MUX_MAKE_MODEL)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.MUX_MAKE_MODEL);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("MUX_MAKE_MODEL: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.LAST_MILE_SCNEARIO)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.LAST_MILE_SCNEARIO);
					String lastMileScenario = (String)attribute.get(MACDConstants.LAST_MILE_SCNEARIO);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("LAST_MILE_SCNEARIO: {}, service detail id {}", 
							(String)attribute.get(MACDConstants.LAST_MILE_SCNEARIO), serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.LM_CONNECTION_TYPE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.LM_CONNECTION_TYPE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("LM_CONNECTION_TYPE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.MAST_PO_NUMBER)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.MAST_PO_NUMBER);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("MAST_PO_NUMBER: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.OFFNET_SUPPLIER_BILLSTART_DATE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.OFFNET_SUPPLIER_BILLSTART_DATE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("OFFNET_SUPPLIER_BILLSTART_DATE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				} else if(attributeName.equalsIgnoreCase(MACDConstants.RFMAKE)) {
					SIComponentAttributeBean siComponentAttributeBean = new SIComponentAttributeBean();
					siComponentAttributeBean.setAttributeName(MACDConstants.RFMAKE);
					siComponentAttributeBean.setAttributeValue(attributeValue);
					siComponentAttributeBean.setId((Integer)attribute.get("component_id"));
					siComponentAttributeBean.setSiServiceDetailId(((Integer)attribute.get("service_detail_id")));
					siComponentAttributeBean.setUuid((String) attribute.get(attribute.get("service_id")));
					componentAttributeBeanList.add(siComponentAttributeBean);
					LOGGER.info("RFMAKE: {}, service detail id {}", 
							attributeValue, serviceInfo.getId());
				}
			});
		} 
		
		componentBean.setSiComponentAttributes(componentAttributeBeanList);
		siServiceInfoBean.setComponentBean(componentList);
		
			
	}

	private void getDemoOrderContractDetails(Set<SIServiceAttributeBean> attributes, SIServiceInfo serviceInfo)throws TclCommonException {
		Optional<SIServiceAdditionalInfo> siServAddInf = siServiceAdditionalInfoRepository.findBySysIdAndAttributeName(serviceInfo.getId(), "DEMO_PERIOD_IN_DAYS");
		siServAddInf.ifPresent(info->{
			SIServiceAttributeBean siServiceAttributeBean = new SIServiceAttributeBean();
			siServiceAttributeBean.setAttributeName("Demo Period in days");
			siServiceAttributeBean.setAttributeValue(info.getAttributeValue());
			attributes.add(siServiceAttributeBean);
			LOGGER.info("Attributes Size in SI service info bean is ---> {} and the attributes are -----> {} ", attributes.size(), attributes);
		});
	}

	/**
	 * Get Service details based on login user and product
	 *
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public GdeSIServiceInformationBean getGdeServiceDetails(Integer productId, Integer page, Integer size,Integer customerId,Integer partnerId,Integer customerLeId)
			throws TclCommonException {
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		GdeSIServiceInformationBean gdeSiServiceInformationBean = new GdeSIServiceInformationBean();
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<CustomerLegalEntityDetails> customerLegalEntityDetails = new ArrayList<>();
		try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			if(Objects.nonNull(customerLeId))
			{
				customerIds.clear();
				customerIds.add(customerLeId);
			}
			LOGGER.info("CustomerIds" + customerIds + "CustomerId" + customerId + "PartnerId" + partnerId);
			if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<Map<String, Object>> data = null;
				if (page == -1) {
					data = siOrderRepository.getServiceDetail(customerIds, partnerLeIds, productId, customerId,
							partnerId);
				} else {
					page = (page - 1) * size;
					data = siOrderRepository.getServiceDetail(customerIds, partnerLeIds, productId, page, size,
							customerId, partnerId);
					Integer totalItems = siServiceDetailRepository.getServiceCountByProduct(productId, customerIds,
							partnerLeIds, customerId, partnerId);
					totalItems = totalItems/2;
					gdeSiServiceInformationBean.setTotalItems(totalItems);
					if (totalItems != null) {
						gdeSiServiceInformationBean.setTotalPages(totalItems / size);
					}
				}

				Set<Integer> siServiceDetailsIds = new HashSet<>();
				if (data != null && !data.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					data.stream().forEach(map -> {
						serviceDetailBeans.add(mapper.convertValue(map, ServiceDetailBean.class));
						siServiceDetailsIds.add((Integer) map.get("siServiceDetailId"));
					});
				}
				LOGGER.info("Service Details Bean {}",serviceDetailBeans);
				List<Map<String, Object>> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetailIdInAndAttributeName(siServiceDetailsIds, "ON_DEMAND_IDENTIFIER");
				LOGGER.info("Response serviceAttributes {}",serviceAttributes);
				serviceAttributes.stream().forEach(attr->{
					serviceDetailBeans.stream().forEach(detail->{
						LOGGER.info("Before assigning bod identifier {}",attr.get("serviceDetailId"));
						if(detail.getSiServiceDetailId().equals((Integer) attr.get("serviceDetailId"))) {
							detail.setBodIdentifier((String) attr.get("attribute_value"));
							LOGGER.info("After assigning bod identifier {}",attr.get("serviceDetailId"));
						}
					});
					
				});
				gdeSiServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
				gdeSiServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
				gdeSiServiceInformationBean.setSiteACities(siServiceDetailRepository.getDistictCityByProductAndSiteType(productId,
						customerIds, partnerLeIds, customerId, partnerId, MACDConstants.SITEA));
				gdeSiServiceInformationBean.setSiteBCities(siServiceDetailRepository.getDistictCityByProductAndSiteType(productId,
						customerIds, partnerLeIds, customerId, partnerId, MACDConstants.SITEB));
				gdeSiServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId));
				gdeSiServiceInformationBean.getAlias().removeIf(Objects::isNull);
				List<Map<String, Object>> leData = siServiceDetailRepository.getDistinctLeDetailsByProduct(productId,
						customerIds, partnerLeIds, customerId, partnerId);
				LOGGER.info("leData {}", leData);

				if (leData != null && !leData.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					leData.stream().forEach(map -> {
						customerLegalEntityDetails.add(mapper.convertValue(map, CustomerLegalEntityDetails.class));
					});
				}
				gdeSiServiceInformationBean.setCustomerLegalEntityDetails(customerLegalEntityDetails);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gdeSiServiceInformationBean;
	}
	
	/**
	 * Get Existing GVPN Information For Product UCAAS
	 *
	 * @param siInfoGvpn
	 * @return
	 */
	public SIExistingGVPNBean getGVPNInfoForUCAAS(SIExistingGVPNBean siInfoGvpn) {
		Integer page = siInfoGvpn.getPage();
		Integer totalPages = 0;
		Integer totalItems = 0;
		page = (page - 1) * siInfoGvpn.getSize();
		List<SIInfoGVPNServiceUCAAS> serviceInfos = siInfoGVPNServiceUCAASRepository
				.findByCustomerIdAndSize(siInfoGvpn.getCustomerId(), page, siInfoGvpn.getSize());
		if (Objects.nonNull(serviceInfos) && !serviceInfos.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			List<SIServiceInfoGVPNBean> serviceInfoBeans = serviceInfos.stream()
					.map(serviceInfo -> mapper.convertValue(serviceInfo, SIServiceInfoGVPNBean.class))
					.collect(Collectors.toList());
			siInfoGvpn.setServiceInfos(serviceInfoBeans);
		}
		List<SIInfoGVPNServiceUCAAS> gvpnServices = siInfoGVPNServiceUCAASRepository
				.findByCustomerId(siInfoGvpn.getCustomerId());
		totalItems = gvpnServices.size();

		if (siInfoGvpn.getSize() != 0) {
			totalPages = totalItems / siInfoGvpn.getSize();
			if (totalItems % siInfoGvpn.getSize() != 0)
				totalPages++;
		}
		siInfoGvpn.setTotalPages(totalPages);
		siInfoGvpn.setTotalItems(totalItems);

		siInfoGvpn.setCities(gvpnServices.stream().map(SIInfoGVPNServiceUCAAS::getSourceCity).filter(Objects::nonNull)
				.distinct().collect(Collectors.toList()));
		siInfoGvpn.setAlias(gvpnServices.stream().map(SIInfoGVPNServiceUCAAS::getSiteAlias).filter(Objects::nonNull)
				.collect(Collectors.toList()));
		return siInfoGvpn;
	}

	/**
	 * Get GVPN info by search criteria
	 *
	 * @param searchBean
	 * @return
	 */
	public SIExistingGVPNBean getGVPNInfoBySearchCriteria(SIInfoSearchBean searchBean) throws TclCommonException {

		LOGGER.info("Entering search {}", searchBean.getCustomerId() + "  " + searchBean.getSearchText());
		SIExistingGVPNBean siInfoGvpn = new SIExistingGVPNBean();
		try {
			Specification<SIInfoGVPNServiceUCAAS> siInfoSpec;

			siInfoSpec = SIInfoGVPNSpecification.getServiceDetails(searchBean.getCity(), searchBean.getAlias(),
					searchBean.getSearchText(), searchBean.getCustomerId(), searchBean.getPartnerId());
			Page<SIInfoGVPNServiceUCAAS> data = siInfoGVPNServiceUCAASRepository.findAll(siInfoSpec,
					PageRequest.of(searchBean.getPage() - 1, searchBean.getSize()));

			LOGGER.info("Data from service inventory repository view: " + data);
			ObjectMapper mapper = new ObjectMapper();
			List<SIServiceInfoGVPNBean> serviceInfoBean = data.stream()
					.map(serviceInfo -> mapper.convertValue(serviceInfo, SIServiceInfoGVPNBean.class))
					.collect(Collectors.toList());
			siInfoGvpn.setServiceInfos(serviceInfoBean);
			siInfoGvpn.setTotalPages(data.getTotalPages());
			siInfoGvpn.setTotalItems((int) data.getTotalElements());

			List<SIInfoGVPNServiceUCAAS> gvpnServices = siInfoGVPNServiceUCAASRepository
					.findByCustomerId(searchBean.getCustomerId());
			siInfoGvpn.setCities(gvpnServices.stream().map(SIInfoGVPNServiceUCAAS::getSourceCity)
					.filter(Objects::nonNull).distinct().collect(Collectors.toList()));
			siInfoGvpn.setAlias(gvpnServices.stream().map(SIInfoGVPNServiceUCAAS::getSiteAlias).filter(Objects::nonNull)
					.collect(Collectors.toList()));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siInfoGvpn;
	}

	/**
	 * Download gvpn Service inventory info in excel format
	 *
	 * @param siInfoGvpn
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public SIExistingGVPNBean downloadSIInfoGVPN(SIExistingGVPNBean siInfoGvpn) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<SIInfoGVPNServiceUCAAS> serviceInfos = siInfoGVPNServiceUCAASRepository
					.findByCustomerId(siInfoGvpn.getCustomerId());
			LOGGER.info("Service Info from repo : {} ", Utils.convertObjectToJson(serviceInfos));
			List<SIServiceInfoGVPNBean> serviceInfoBeans = serviceInfos.stream().map(
					siInfoGVPNServiceUCAAS -> mapper.convertValue(siInfoGVPNServiceUCAAS, SIServiceInfoGVPNBean.class))
					.collect(Collectors.toList());
			siInfoGvpn.setServiceInfos(serviceInfoBeans);
		} catch (Exception e) {
			LOGGER.info("Error when retrieving from repository {}", e.getMessage());
		}

		return siInfoGvpn;
	}

	/**
	 * Get GVPN information using service ID
	 *
	 * @param serviceId
	 * @return
	 */
	public SIServiceInfoGVPNBean getGVPNInfoByServiceId(String serviceId) {
		SIInfoGVPNServiceUCAAS siInfo = siInfoGVPNServiceUCAASRepository.findByServiceId(serviceId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(siInfo, SIServiceInfoGVPNBean.class);
	}
	
	/**
	 * Method to get detailed ServiceInventory info for GDE service. 
	 * Addressed separately since it has A-End & B-End details for a single circuitId.
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public GdeSIServiceDetailedResponse getGDEDetailedSIInfo(String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Inside getGDEDetailedSIInfo to fetch data for GDE service id {} ",serviceId);

		String productName = "GDE";
		GdeSIServiceDetailedResponse response = new GdeSIServiceDetailedResponse();
		List<GdeSISolutionDataOffering> siInfo = new ArrayList<>();
		Map<String,String> networkProtections =new HashMap<>();
		Map<String,String> bodIndetifier =new HashMap<>();
		try {
			List<Integer> sysIds = siServiceInfoRepository.findSysIdsForAServiceId(serviceId, productName);
			sysIds.stream().forEach(id -> {
				List<Map<String, Object>> details = siServiceInfoRepository.getServiceDetailAttributesForGDE(serviceId,id,productName);
				if (details == null || details.isEmpty()) {
					throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT,
							ResponseResource.R_CODE_ERROR);
				}

				details.stream().forEach(serviceDetail -> {
					GdeSISolutionDataOffering solution = null;
					Map<String, Object> attributes = new HashMap<>();

					Integer sysId = (Integer) serviceDetail.get("sys_id");
					String linkType = (String) serviceDetail.get("pri_sec");
					
					Optional<GdeSISolutionDataOffering> solutions = siInfo.stream()
							.filter(si -> si.getServiceId().equals(sysId)).findFirst();
					if (solutions.isPresent()) {
						solution = solutions.get();
					} else {
						solution = new GdeSISolutionDataOffering();
						siInfo.add(solution);
					}
					String siteType = (String)serviceDetail.get("site_type");
					String attribute = (String) serviceDetail.get("attribute_name");
					if(attribute.equalsIgnoreCase("Network_Protection")) {
						LOGGER.info("Getting networktype for sitetype {} ", siteType);
						networkProtections.put(siteType.toLowerCase(), (String) serviceDetail.get("attribute_value"));
					} 
					if(attribute.equalsIgnoreCase("ON_DEMAND_IDENTIFIER")) {
						LOGGER.info("Getting ON_DEMAND_IDENTIFIER for sitetype {} ", siteType);
						bodIndetifier.put(siteType.toLowerCase(), (String) serviceDetail.get("attribute_value"));
					}
					GdeSolutionAttributes solutionAttributes = constructGdeSolutionAttributes(sysId);
					solution.setAttributes(solutionAttributes);
					solution.setServiceId(sysId);
					solution.setLinkType(linkType);
					solution.setSiteType(solutionAttributes.getSiteType());
					solution.setSiteClassification((String) serviceDetail.get("site_classification"));
					List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository
							.getAssetDetailWithAttributes(sysId);
					Optional<Map<String, Object>> assetDetailInfo = assetDetailInfos.stream().findFirst();
					Map<String, Object> assetDetail = new HashMap<>();
					if (assetDetailInfo.isPresent()) {
						assetDetail = assetDetailInfo.get();
					}
					try {
						attributes = getFieldAttributes(serviceDetail, assetDetail, attributes);
						getAttributesForGde(solution, attributes, details, assetDetailInfos);
					} catch (TclCommonException e) {
						LOGGER.error("Exception fetching detailed ServiceInformation for GDE service id {}", serviceId);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
			response.setProductName(productName);
			siInfo.stream().forEach(info->{
				LOGGER.info("Setting  networkprotection for sitetype {} ", info.getServiceId(), info.getSiteType());
				if(info.getSiteType().equalsIgnoreCase("siteA")) {
					info.getAttributes().setNetworkProtection(networkProtections.get("sitea"));
					info.getAttributes().setBodIdentifier(bodIndetifier.get("sitea"));
				} 
				if(info.getSiteType().equalsIgnoreCase("siteB")) {
					info.getAttributes().setNetworkProtection(networkProtections.get("siteb"));
					info.getAttributes().setBodIdentifier(bodIndetifier.get("siteb"));
				}
			});
			response.setSiInfo(siInfo);
			return response;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for GDE service id{}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Method to set solution attributes for GDE service.
	 *
	 * @param sysId
	 * @return {@GdeSolutionAttributes}
	 */
	public GdeSolutionAttributes constructGdeSolutionAttributes(Integer sysId) {
		LOGGER.info("Constructing  attributes for GDE serviceid {} ",sysId);
		final ObjectMapper mapper = new ObjectMapper();
		GdeSolutionAttributes solutionAttributes = new GdeSolutionAttributes();
		List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(sysId);
		if (data.stream().findFirst().isPresent()) {
			ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(),
					ServiceDetailBean.class);

			solutionAttributes.setOfferingName(serviceDetail.getOfferingName());
			solutionAttributes.setSiteAddress(serviceDetail.getSiteAddress());
			solutionAttributes.setSiteAlias(serviceDetail.getAlias());
			solutionAttributes.setAccessType(serviceDetail.getAccessType());
			solutionAttributes.setLatLong(serviceDetail.getLatLong());
			solutionAttributes.setBillingAccountId(serviceDetail.getBillingAccountId());
			solutionAttributes.setBillingAddress(serviceDetail.getBillingAddress());
			solutionAttributes.setBillingGstNumber(serviceDetail.getBillingGstNumber());
			solutionAttributes.setContractEndDate(serviceDetail.getContractEndDate());
			solutionAttributes.setContractStartDate(serviceDetail.getContractStartDate());
			solutionAttributes.setLeId(serviceDetail.getLeId());
			solutionAttributes.setLeName(serviceDetail.getLeName());
			solutionAttributes.setSupplierLeId(serviceDetail.getSupplierLeId());
			solutionAttributes.setSupplierLeName(serviceDetail.getSupplierLeName());
			solutionAttributes.setTaxExemptionFlag(serviceDetail.getTaxExemptionFlag());
			solutionAttributes.setSiServiceDetailId(serviceDetail.getSiServiceDetailId());
			solutionAttributes.setSiOrderId(serviceDetail.getSiOrderId());
			solutionAttributes.setTermInMonths(serviceDetail.getTermInMonths());
			solutionAttributes.setPrimaryServiceId(serviceDetail.getPrimaryServiceId());
			solutionAttributes.setSecondaryServiceId(serviceDetail.getSecondaryServiceId());
			solutionAttributes.setServiceStatus(serviceDetail.getServiceStatus());
			solutionAttributes.setLastMileProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setAccessProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setProductId(serviceDetail.getProductId());
			solutionAttributes.setServiceId(serviceDetail.getServiceId());
			solutionAttributes.setIpAddressProvidedBy(serviceDetail.getIpAddressProvidedBy());
			solutionAttributes.setCustomerId(serviceDetail.getCustomerId());
			solutionAttributes.setVpnName(serviceDetail.getVpnName());
			solutionAttributes.setServiceTopology(serviceDetail.getServiceTopology());
			solutionAttributes.setSiteLocationId(serviceDetail.getSiteLocationId());
			solutionAttributes.setPortSpeed(serviceDetail.getPortSpeed());
			solutionAttributes.setPortSpeedUnit(serviceDetail.getPortSpeedUnit());
			solutionAttributes.setCircuitSpeed(serviceDetail.getPortSpeed());
			solutionAttributes.setSiteType(serviceDetail.getSiteType());
			// solutionAttributes.setChargeType(chargeType);
			// solutionAttributes.setNetworkProtection(networkProtection);

			if (Objects.nonNull(solutionAttributes.getPortSpeedUnit())
					&& solutionAttributes.getPortSpeedUnit().equalsIgnoreCase("kbps")) {
				if (Objects.nonNull(solutionAttributes.getPortSpeed())) {
					switch (solutionAttributes.getPortSpeed().trim()) {
					case "512": {
						solutionAttributes.setPortSpeed("0.5");
						solutionAttributes.setPortSpeedUnit("mpbs");
						break;
					}
					case "256": {
						solutionAttributes.setPortSpeed("0.25");
						solutionAttributes.setPortSpeedUnit("mpbs");
						break;
					}
					case "1024": {
						solutionAttributes.setPortSpeed("1");
						solutionAttributes.setPortSpeedUnit("mpbs");
					}
					default:
						break;
					}
				}
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = serviceDetail.getCommissionedDate();
			if (Objects.nonNull(date)) {
				String strDate = dateFormat.format(date);
				solutionAttributes.setCommissioningDate(strDate);
			}

			List<Map<String, Object>> localItContacts = siServiceContactRepository
					.findBySiServiceDetail_Id(serviceDetail.getSiServiceDetailId());
			Optional<Map<String, Object>> serviceDetailInfo = localItContacts.stream().findFirst();
			if (serviceDetailInfo.isPresent()) {
				Map<String, Object> detail = serviceDetailInfo.get();
				String contactName = (String) detail.get("contactName");
				String contactType = (String) detail.get("contactType");
				String businessEmail = (String) detail.get("businessEmail");
				String businessPhone = (String) detail.get("businessMobile");
				if (Objects.nonNull(contactType) && "LocalITContact".equalsIgnoreCase(contactType)) {
					solutionAttributes.setLocalItPhoneno(businessPhone);
					solutionAttributes.setLocalItName(contactName);
					solutionAttributes.setLocalItEmail(businessEmail);
				}
			}
		}
		return solutionAttributes;
	}
	
	/**
	 * Method to get attributes for GDE service.
	 * 
	 * @param solution
	 * @param attributes
	 * @param details
	 * @param assetDetailInfo
	 * @throws TclCommonException
	 */
	public void getAttributesForGde(GdeSISolutionDataOffering solution, Map<String,Object> attributes, List<Map<String, Object>> details, List<Map<String, Object>> assetDetailInfo) throws TclCommonException
	{
		LOGGER.info("Fetching attributes for GDE serviceid");
		Map<String,Object> attributesPopulated = new HashMap<>();
		attributesPopulated.putAll(getAttributesMap(details, assetDetailInfo));
		solution.setAttributeDetail(constructAttributes(attributes));
	}
	
	/**
	 * Method to search the details
	 * @param productId
	 * @param page
	 * @param size
	 * @param siteAcity
	 * @param siteBcity
	 * @param alias
	 * @param searchText
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public PagedResult<List<SIServiceInformationBean>> getGDEServiceDetailsWithPaginationAndSearch(Integer productId,Integer page,Integer size,String siteAcity,String siteBcity,String alias,String searchText, Integer customerId,Integer partnerId,Integer customerLeId) throws TclCommonException{
        List<GdeSIServiceInformationBean> list = new ArrayList<>();
        GdeSIServiceInformationBean siServiceInformationBean = new GdeSIServiceInformationBean();
        List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if(productId==null || page==null || size==null || page<=0 || size<=0) {
               throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
        }
        try {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			//Added customerLeId selection for multicircuit
			if(Objects.nonNull(customerLeId))
			{
				customerIds.clear();
				customerIds.add(customerLeId);
			}
				LOGGER.info("CustomerIds"+customerIds+"CustomerId"+customerId+"PartnerId"+partnerId);
               if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
            	   List<String> serviceIdsByCity = new ArrayList<>();
				   Specification<SIServiceDetail> spec;
				   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
					  spec = SIServiceDetailSpecification.getGDEServiceDetails(siteAcity, siteBcity,alias, searchText, null, partnerLeIds, productId,null,partnerId);
//					  spec = SIServiceDetailSpecification.getGDEServiceDetailsForServiceId(null, alias, searchText, null, partnerLeIds, productId,null,partnerId);
				   }
				   else{
					   
					   if((siteAcity != null && !siteAcity.equalsIgnoreCase("ALL")) && (siteBcity != null && !siteBcity.equalsIgnoreCase("ALL"))) {
	            		   serviceIdsByCity = siServiceDetailRepository.findBySiteACityAndSiteBCity(customerIds, customerId, siteAcity, siteBcity);
	            	   } else if ((siteAcity != null && siteAcity.equalsIgnoreCase("ALL")) && (siteBcity != null && !siteBcity.equalsIgnoreCase("ALL"))
	            			   || (siteAcity != null && !siteAcity.equalsIgnoreCase("ALL")) && (siteBcity != null && siteBcity.equalsIgnoreCase("ALL"))) {
	            		   String siteType = "SiteA";
	            		   String sourceCity = siteAcity;
	            		   if(siteAcity.equalsIgnoreCase("ALL")) {
	            			   siteType = "SiteB";
	            			   sourceCity = siteBcity;
	            		   }   
	            		   serviceIdsByCity = siServiceDetailRepository.findBySourceCityAndSiteType(customerIds, customerId, sourceCity, siteType);
	            	   }
					   spec = SIServiceDetailSpecification.getGDEServiceDetailsForServiceId(serviceIdsByCity, alias, searchText, customerIds, null, productId,customerId,null);
				   }
                     Page<SIServiceDetail> data = siServiceDetailRepository.findAll(spec, PageRequest.of(page - 1, size));
                     LOGGER.info("Data"+data);
                     if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
                            List<SIServiceDetail> serviceDetails = data.getContent();
                            serviceDetails.stream().forEach(serviceDetail->{
                                   constructServiceDetailBean(serviceDetail, serviceDetailBeans);
                            });
                            siServiceInformationBean.setServiceDetailBeans(serviceDetailBeans);
                            siServiceInformationBean.setServiceDetailBeans(setMacdFlag(serviceDetailBeans));
                            siServiceInformationBean.setAlias(siServiceDetailRepository.getDistictAliasByProduct(productId, customerIds, partnerLeIds,customerId,partnerId));
                            siServiceInformationBean.setSiteACities(siServiceDetailRepository.getDistictCityByProductAndSiteType(productId, customerIds, partnerLeIds,customerId,partnerId, MACDConstants.SITEA));
                            siServiceInformationBean.setSiteBCities(siServiceDetailRepository.getDistictCityByProductAndSiteType(productId, customerIds, partnerLeIds,customerId,partnerId, MACDConstants.SITEB));
                            siServiceInformationBean.getAlias().removeIf(Objects::isNull);
                            list.add(siServiceInformationBean);
                            long totalItem = data.getTotalElements()/2;
                            return new PagedResult(list, totalItem, data.getTotalPages());
                     }
               }
        }catch(Exception e) {
               throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
               }
        return null;
 }
	
	public SiServiceSiContractInfoBean getSiContractInfoByServiceId(String serviceId, String productName) {
		SiServiceSiContractInfoBean siServiceSiContractInfoBean = new SiServiceSiContractInfoBean(); 
		try {
			List<Map<String, Object>> contractInfo = siServiceDetailRepository.findByTpsServiceIdAndProduct(serviceId, productName);
			if(!contractInfo.isEmpty()) {
				Map<String, Object> siContractInfo = contractInfo.stream().findFirst().get();
				String bwUnit = (String) siContractInfo.get("bandwidthUnit");
				String portBw = (String) siContractInfo.get("baseBandwidth");
				siServiceSiContractInfoBean.setBaseBandwidth(setBandwidthConversion(portBw, bwUnit));
				siServiceSiContractInfoBean.setBandwidthUnit(bwUnit);
				if(Objects.nonNull(bwUnit)&&(bwUnit.equalsIgnoreCase("kbps")||bwUnit.equalsIgnoreCase("gbps")))
					siServiceSiContractInfoBean.setBandwidthUnit("Mpbs");
				List<Map<String, Object>> pricingComponents = siGenevaComponentMvRepository.findByServiceIdAndStatusAndProduct(serviceId);
				if(!pricingComponents.isEmpty()) {
					Map<String, Object> priceComponents = pricingComponents.stream().findFirst().get();
					siServiceSiContractInfoBean.setBillingCurrency((String) priceComponents.get("currency"));
					 String onnet = (String) priceComponents.get("onnetArc");
					 if(!StringUtils.isEmpty(onnet))
						 siServiceSiContractInfoBean.setMrc(Double.parseDouble(onnet));
				}
			}
		} catch(Exception e) {
			LOGGER.error("Error while fetching si contract infor for the service id {}  and product {}",serviceId, productName);
		}
		return siServiceSiContractInfoBean;
	}

	public void terminateService(String serviceId) {
		LOGGER.info("ServiceInventoryService.terminateService method invoked");
		if (serviceId != null) {
			LOGGER.info("ServiceInventoryService.terminateService service Id exists::{}", serviceId);
			SIServiceDetail existingSiServiceDetail = siServiceDetailRepository
					.findFirstByTpsServiceIdOrUuidAndServiceStatusIgnoreCaseAndIsActive(serviceId, serviceId, "Active",
							CommonConstants.Y);
			if (existingSiServiceDetail != null) {
				LOGGER.info("Service Id exists in Service Inventory");
				existingSiServiceDetail.setUuid(serviceId);
				existingSiServiceDetail.setServiceStatus("Terminated");
				existingSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
				existingSiServiceDetail.setUpdatedBy("Optimus O2C");
				siServiceDetailRepository.save(existingSiServiceDetail);
			}
		}
		LOGGER.info("ServiceInventoryService.terminateService ends");
	}

	public String downloadAttachment(Integer attachmentId, HttpServletResponse response) throws TclCommonException {
        Optional<Attachment> storagePath = attachmentRepository.findById(attachmentId);
            String tempPathForAttachment = getTempPathForAttachment(storagePath.get().getName(),storagePath.get().getStoragePathUrl());
            LOGGER.info("Temp Path for attachment : {}",tempPathForAttachment);
            return tempPathForAttachment;
    }

	private String buildResponse(Path path, HttpServletResponse response) {
        response.reset();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Expires" + CommonConstants.COLON, "0");
        response.setHeader("Content-Disposition", path.toString());
        try {
            Files.copy(path, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new TclCommonRuntimeException("Exception occurred while downloading attachment", R_CODE_ERROR);
        }
        return path.toString();
    }

	 private String getTempPathForAttachment(String fileName,String containerName) throws TclCommonException {
	        return fileStorageService.getTempDownloadUrl(
	        		fileName, Long.parseLong(tempDownloadUrlExpiryWindow),
	        		containerName,false);
	    }

	/**
	 * Get Domestic Voice Sites Details
	 *
	 * @param siOrderId
	 * @return List<String>
	 */
	public List<String> getGscDomesticVoiceSites(String siOrderId) {
		return siServiceDetailRepository.findBySiOrderId(siOrderId);
	}
	
	/**
	 * Update alias name for GDE service 
	 *
	 * @param serviceDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	public String updateAliasNameForGdeCircuit(ServiceDetailBean serviceDetailBean) throws TclCommonException {
		String response = null;
		if (serviceDetailBean == null || serviceDetailBean.getServiceId() == null
				|| serviceDetailBean.getAlias() == null || serviceDetailBean.getProductName() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			if(serviceDetailBean.getProductName().equalsIgnoreCase("GDE")) {
				return updateAliasDetailsForGde(serviceDetailBean);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	/**
	 * Method to update alias for GDE siteA and siteB
	 * @param serviceDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	private String updateAliasDetailsForGde(ServiceDetailBean serviceDetailBean) throws TclCommonException {

		String response = null;
		try {
			List<SIServiceDetail> siServiceDetail = siServiceDetailRepository.findByTpsServiceIdForNPL(serviceDetailBean.getServiceId());
			siServiceDetail.stream().forEach(service->{
				service.setSiteAlias(serviceDetailBean.getAlias());
				siServiceDetailRepository.saveAndFlush(service);
			});
			response = CommonConstants.SUCCESS;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	
	}

	/**
	 * Get sites from configuration
	 *
	 * @param accessType
	 * @param customerLeId
	 * @param productName
	 * @return {@link List}
	 */
	public List<SISiteConfigurationBean> getSitesFromConfigurations(String accessType, Integer customerLeId, String productName) {
		List<Map<String, Object>> sitesByCustomerLe = siServiceDetailRepository.findSitesByCustomerLe(accessType, customerLeId, productName, ASSET_TYPE_TFN);
		if (!CollectionUtils.isEmpty(sitesByCustomerLe)) {
			return mapRows(() ->sitesByCustomerLe, this::toSite);
		} else {
			return ImmutableList.of();
		}
	}

	/**
	 * Configure SI Site configuration bean from map
	 *
	 * @param row
	 * @return {@link SIConfigurationByLeBean}
	 */
	private SISiteConfigurationBean toSite(Map<String, Object> row) {
		SISiteConfigurationBean siSiteConfigurationBean = new SISiteConfigurationBean();
		siSiteConfigurationBean.setSiOrderId((Integer)(row.get(ORDER_ID)));
		siSiteConfigurationBean.setSiteAddress((String) row.get(SITE_ADDRESS));
		siSiteConfigurationBean.getAttributes().put("number", (String) row.get(NUMBER));
		return siSiteConfigurationBean;
	}

	/**
	 * Get numbers list by site address as pdf
	 *
	 * @param response
	 * @param accessType
	 * @param customerLeId
	 * @param productName
	 * @param siteAddress
	 * @return {@link String}
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public String getNumbersListBySiteAddressAsPdf(HttpServletResponse response, String accessType, Integer customerLeId, String productName, String siteAddress) throws DocumentException, IOException, TclCommonException {
		SINumbersBySiteBean siNumbersBySiteBean = new SINumbersBySiteBean();
		siNumbersBySiteBean.setSiteAddress(siteAddress);
		siNumbersBySiteBean.setNumbers(getNumbersListBySiteAddress(accessType, customerLeId, productName, siteAddress));
		return downloadNumbersAsPdf(siNumbersBySiteBean, response);
	}

	/**
	 * Get numbers list by site address
	 *
	 * @param accessType
	 * @param customerLeId
	 * @param productName
	 * @param siteAddress
	 * @return {@link List}
	 */
	public List<String> getNumbersListBySiteAddress(String accessType, Integer customerLeId, String productName, String siteAddress) {
		List<Map<String, Object>> numbersListBySiteAddress = siServiceDetailRepository.findNumbersListBySiteAddress(accessType, customerLeId, productName, siteAddress, ASSET_TYPE_TFN);
		if (!CollectionUtils.isEmpty(numbersListBySiteAddress)) {
			LOGGER.info("No of Numbers available for the site are {} ", numbersListBySiteAddress.size());
			return mapRows(() -> numbersListBySiteAddress, this::toNumber);
		} else {
			LOGGER.info("No numbers available for given site");
			return ImmutableList.of();
		}
	}

	/**
	 * map number
	 *
	 * @param row return {@link String}
	 */
	private String toNumber(Map<String, Object> row) {
		return (String) row.get(NUMBER);
	}

	/**
	 * Download the list of numbers in a pdf
	 *
	 * @param siNumbersBySiteBean
	 * @param response
	 * @return {@link String}
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	private String downloadNumbersAsPdf(SINumbersBySiteBean siNumbersBySiteBean, HttpServletResponse response) throws DocumentException, IOException, TclCommonException {
		String html = "";
		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		Map<String, Object> variable = objectMapper.convertValue(ImmutableMap.of("siNumbersBySite", siNumbersBySiteBean), Map.class);
		org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
		context.setVariables(variable);
		html = templateEngine.process("site_address_numbers_template", context);
		PDFGenerator.createPdf(html, outByteStream);
		outArray = outByteStream.toByteArray();
		response.reset();
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setContentLength(outArray.length);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + "SITE_ADDRESS_NUMBERS" + "\"");
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		outByteStream.flush();
		outByteStream.close();
		return html;
	}

	/**
	 * Method to fetch site configurations based on search params
	 *
	 * @param customerLeId
	 * @param productName
	 * @param accessType
	 * @param number
	 * @param customerId
	 * @return {@link List<SIConfigurationCountryBean}
	 */
	public List<SIConfigurationCountryBean> getSiteConfigurationsBasedOnSearch(String customerLeId, String productName, String accessType, String number, Integer customerId) {
		List<SIConfigurationByLeBean> siConfigurationByLeBeans =  new ArrayList<>();
				//serviceInventoryDao.findSiteConfigurationsByParams(productName, ASSET_TYPE_TFN, request);
		return getSiConfigurationByCustomerLe(siConfigurationByLeBeans);
	}
	/**
	 * Get GVPN service details with parent product GSC
	 *
	 * @param customerId
	 * @param accessType
	 * @param page
	 * @param size
	 * @return {@link SIServiceInformationBean}
	 */
	public SIServiceInformationBean getServiceDetailsOfGsc(Integer customerId, Integer page, Integer size) throws TclCommonException {
		Objects.requireNonNull(size, "Size cannot be null");
		Objects.requireNonNull(page, "Page cannot be null");
		Objects.requireNonNull(customerId, "Customer Id cannot be null");
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<GscServiceDetailBean> gscServiceDetailBeans = getAllServiceDetailsOfGscByPage(customerId, page, size);

		if (!CollectionUtils.isEmpty(gscServiceDetailBeans)) {
			siServiceInformationBean.setGscServiceDetailBeans(gscServiceDetailBeans);
			setTotalItemsAndPagesBasedOnServiceInformation(customerId, size, siServiceInformationBean);

		} else {
			LOGGER.info("Access type should be either mpls or public ip");
		}
		return siServiceInformationBean;
	}

	/**
	 * Get GSC Interconnect details
	 *
	 * @param orgId
	 * @param page
	 * @param size
	 * @return {@link SIServiceInformationBean}
	 */
	public SIServiceInformationBean getGscInterconnectDetails(Integer orgId, Integer page, Integer size) throws TclCommonException {
		Objects.requireNonNull(size, "Size cannot be null");
		Objects.requireNonNull(page, "Page cannot be null");
		Objects.requireNonNull(orgId, "Org Id cannot be null");
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<GscWholesaleInterconnectBean> gscInterconnectDetailBeans = getAllInterconnectDetailsOfGscByPage(orgId, page, size);

		if (!CollectionUtils.isEmpty(gscInterconnectDetailBeans)) {
			siServiceInformationBean.setGscWholesaleInterconnectBeans(gscInterconnectDetailBeans);
			setTotalItemsAndPagesBasedOnInterconnectInformation(orgId, size, siServiceInformationBean);

		} else {
			LOGGER.info("Access type should be either mpls or public ip");
		}
		return siServiceInformationBean;
	}

	/**
     * Get all service details of gsc by page and size
     *
     * @param customerId
     * @param page
     * @param size
     */
    private List<GscServiceDetailBean> getAllServiceDetailsOfGscByPage(Integer customerId, Integer page, Integer size) {
		page = (page - 1) * size;
		List<Map<String, Object>> data = vwGscServiceCircuitLinkDetailRepository.getServiceDetailsByCustomerId(customerId, page, size);
		if (!CollectionUtils.isEmpty(data)) {
			return mapRows(() -> data, this::toGscServiceDetails);
		} else {
			LOGGER.info("No service details for customer id : {} ", customerId);
			return ImmutableList.of();
		}
	}

    /**
     * Get all interconnect details of Gsc by page and size
     *
     * @param orgId
     * @param page
     * @param size
     */
    private List<GscWholesaleInterconnectBean> getAllInterconnectDetailsOfGscByPage(Integer orgId, Integer page, Integer size) {
		page = (page - 1) * size;
		List<Map<String, Object>> data = gscInterconnectDetailsRepository.getInterconnectDetailsByOrgId(orgId, page, size);
		if (!CollectionUtils.isEmpty(data)) {
			return mapRows(() -> data, this::toGscInterconnectDetails);
		} else {
			LOGGER.info("No Interconnect details for org id : {} ", orgId);
			return ImmutableList.of();
		}
	}

	/**
	 * Set total items and pages based on service information
	 *
	 * @param customerId
	 * @param size
	 * @param siServiceInformationBean
	 */
	private void setTotalItemsAndPagesBasedOnServiceInformation(Integer customerId, Integer size, SIServiceInformationBean siServiceInformationBean) {
		Integer totalItems = vwGscServiceCircuitLinkDetailRepository.getCountOfServiceDetails(customerId);
		siServiceInformationBean.setTotalItems(totalItems);
		if (Objects.nonNull(totalItems)) {
			siServiceInformationBean.setTotalPages(totalItems / size);
		}
	}

	/**
	 * Set total items and pages based on org information
	 *
	 * @param orgId
	 * @param size
	 * @param siServiceInformationBean
	 */
	private void setTotalItemsAndPagesBasedOnInterconnectInformation(Integer orgId, Integer size, SIServiceInformationBean siServiceInformationBean) {
		Integer totalItems = gscInterconnectDetailsRepository.getCountOfInterconnectDetails(orgId);
		siServiceInformationBean.setTotalItems(totalItems);
		if (Objects.nonNull(totalItems)) {
			siServiceInformationBean.setTotalPages(totalItems / size);
		}
	}

	/**
	 * API to get service details of gsc by search
	 *
	 * @param city
	 * @param page
	 * @param size
	 * @param circuitID
	 * @param ipAddress
	 * @param customerId
	 * @param tclSwitch
	 * @return {@link PagedResult<List<SIServiceInformationBean>>}
	 */
	public PagedResult<List<SIServiceInformationBean>> getGscServiceDetailsWithPaginationAndSearch(Integer page, Integer size, String circuitID, String ipAddress, String sipTrunkGroup, String tclSwitch, Integer customerId) {
		Objects.requireNonNull(page, "page cannot be null");
		Objects.requireNonNull(size, "size cannot be null");
		Objects.requireNonNull(customerId, "customerId cannot be null");
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		SIServiceInformationBean siServiceInformationBean = new SIServiceInformationBean();
		List<SIServiceInformationBean> list = new ArrayList<>();

		Specification<ViewGscServiceCircuitLinkDetail> serviceDetailsSpec = SIServiceDetailSpecification.getGscServiceDetails(customerId, circuitID, ipAddress, sipTrunkGroup, tclSwitch);

		Page<ViewGscServiceCircuitLinkDetail> data = vwGscServiceCircuitLinkDetailRepository.findAll(serviceDetailsSpec, PageRequest.of(page - 1, size));

		if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
			List<ViewGscServiceCircuitLinkDetail> gscServiceCircuitLinkDetails = data.getContent();
			List<GscServiceDetailBean> gscServiceDetailBeans = gscServiceCircuitLinkDetails.stream().map(this::constructGscServiceCircuitLinkDetailBean).collect(Collectors.toList());
			siServiceInformationBean.setGscServiceDetailBeans(gscServiceDetailBeans);
			list.add(siServiceInformationBean);
			return new PagedResult(list, data.getTotalElements(), data.getTotalPages());
		}
		else {
			LOGGER.info("No Service details found with given search criteria");
		}
		return null;
	}

	/**
	 * construct Gsc Service Circuit Link Details Bean
	 *
	 * @param viewGscServiceCircuitLinkDetail
	 * @return  {@link GscServiceDetailBean}
	 */
	private GscServiceDetailBean constructGscServiceCircuitLinkDetailBean(ViewGscServiceCircuitLinkDetail viewGscServiceCircuitLinkDetail) {
		GscServiceDetailBean gscServiceDetailBean = new GscServiceDetailBean();
		gscServiceDetailBean.setId(viewGscServiceCircuitLinkDetail.getId());
		gscServiceDetailBean.setSfdcCUID(viewGscServiceCircuitLinkDetail.getSfdcCuid());
		gscServiceDetailBean.setSipTrunkGroup(viewGscServiceCircuitLinkDetail.getCircuitGrCd());
		gscServiceDetailBean.setCircuitID(viewGscServiceCircuitLinkDetail.getCircuitId());
		gscServiceDetailBean.setIpAddress(viewGscServiceCircuitLinkDetail.getCustomerIpAddress());
		gscServiceDetailBean.setErfCusCustomerId(viewGscServiceCircuitLinkDetail.getErfCusCustomerId());
		gscServiceDetailBean.setTclSwitch(viewGscServiceCircuitLinkDetail.getSwitchingUnitCd());
		return gscServiceDetailBean;
	}

	/**
	 * Configure GscServiceDetails from map
	 *
	 * @param row
	 * @return {@link GscServiceDetailBean}
	 */
	private GscServiceDetailBean toGscServiceDetails(Map<String, Object> row) {
		GscServiceDetailBean gscServiceDetailBean = new GscServiceDetailBean();
        gscServiceDetailBean.setId(Objects.nonNull(row.get(ID)) ? row.get(ID).toString() :  null);
        gscServiceDetailBean.setSfdcCUID(Objects.nonNull(row.get(SFDC_CUID)) ? row.get(SFDC_CUID).toString()  : null);
        gscServiceDetailBean.setSipTrunkGroup(Objects.nonNull(row.get(CIRCT_GR_CD)) ? row.get(CIRCT_GR_CD).toString()  : null);
        gscServiceDetailBean.setCircuitID(Objects.nonNull(row.get(CIRCUIT_ID2)) ? row.get(CIRCUIT_ID2).toString()  : null);
        gscServiceDetailBean.setIpAddress(Objects.nonNull(row.get(CUST_IP_ADDR)) ? row.get(CUST_IP_ADDR).toString()  : null);
        gscServiceDetailBean.setErfCusCustomerId(Objects.nonNull(row.get(ERF_CUST_CUSTOMER_ID)) ? row.get(ERF_CUST_CUSTOMER_ID).toString()  : null);
        gscServiceDetailBean.setTclSwitch(Objects.nonNull(row.get(SWTCH_UNIT_CD)) ? row.get(SWTCH_UNIT_CD).toString()  : null);
        return gscServiceDetailBean;
	}
	
	/**
	 * Configure GscInterconnectDetails from map
	 *
	 * @param row
	 * @return {@link GscWholesaleInterconnectBean}
	 */
	private GscWholesaleInterconnectBean toGscInterconnectDetails(Map<String, Object> row) {
		GscWholesaleInterconnectBean gscInterconnectBean = new GscWholesaleInterconnectBean();
		gscInterconnectBean.setOrgNo(Objects.nonNull(row.get(ORG_ID)) ? row.get(ORG_ID).toString() :  null);
		gscInterconnectBean.setInterconnectId(Objects.nonNull(row.get(INTERCONNECT_ID)) ? row.get(INTERCONNECT_ID).toString()  : null);
		gscInterconnectBean.setInterconnectName(Objects.nonNull(row.get(INTERCONNECT_NAME)) ? row.get(INTERCONNECT_NAME).toString()  : null);
        return gscInterconnectBean;
	}

	/**
	 * @param cuid
	 * @return
	 * @throws TclCommonException
	 */
	public List<SICustomerInfoBean> getServiceDetailsforNDE(String cuid) throws TclCommonException {
		List<SICustomerInfoBean> siCustomerDetailBeanList = new ArrayList<>();

		if (StringUtils.isBlank(cuid)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<SICustomerInfo> siCustomerInfo = siCustomerInfoRepository.findByCuId(cuid);
		LOGGER.info("customer id detail:: {}", siCustomerInfo);
		if (!siCustomerInfo.isEmpty()) {
			siCustomerDetailBeanList.addAll(constructSiCustomerInfoBean(siCustomerInfo));
	    }
		return siCustomerDetailBeanList;
	}
	
	/**
	 * @param siCustomerInfo
	 * @return
	 */
	private List<SICustomerInfoBean> constructSiCustomerInfoBean(List<SICustomerInfo> siCustomerInfo) {
		List<SICustomerInfoBean> customerInfoBeanList = new ArrayList<>();
		siCustomerInfo.stream().forEach(customerInfo -> {
			SICustomerInfoBean siCustomerInfoBean = new SICustomerInfoBean();
			siCustomerInfoBean.setId(customerInfo.getId());
			siCustomerInfoBean.setCuId(customerInfo.getCuId());
			siCustomerInfoBean.setEhsLocId(customerInfo.getEhsLocId());
			siCustomerInfoBean.setEhsAddress(customerInfo.getEhsAddress());
			siCustomerInfoBean.setEhsId(customerInfo.getEhsId());
			customerInfoBeanList.add(siCustomerInfoBean);
		
		});
		return customerInfoBeanList; 
			
		
	}
	
	
	/**
	 * Method to get detailed ServiceInventory info for NDE service. Addressed separately since it has A-End & B-End details for a single circuitId.
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public NPLSIServiceDetailedResponse getNDEDetailedSIInfo(String serviceId, Boolean isTermination) throws TclCommonException {
		LOGGER.info("Entered into getNDEDetailedSIInfo"+serviceId);
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		if(isTermination == null) {
			LOGGER.info("isTermination is null, assuming false");
			isTermination = Boolean.FALSE;
		}

		String productName = "NDE";
		NPLSIServiceDetailedResponse response = new NPLSIServiceDetailedResponse();
		List<NPLSISolutionDataOffering> siInfo = new ArrayList<>();

		try {
			List<Integer> sysIds = null;
			if(Boolean.TRUE.equals(isTermination)) {
				sysIds = siServiceInfoRepository.findLatestSysIdsForAServiceId(serviceId, productName);
			} else {
			 sysIds = siServiceInfoRepository.findSysIdsForAServiceId(serviceId, productName);
			}
			final Boolean isTerm = isTermination;
			sysIds.stream().forEach(id -> {

				List<Map<String, Object>> details =  Boolean.TRUE.equals(isTerm) ? siServiceInfoRepository.getLatestServiceDetailAttributesForNDE(serviceId,
						id) : siServiceInfoRepository.getServiceDetailAttributesForNDE(serviceId,
						id);
				if (details == null || details.isEmpty()) {
					throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT,
							ResponseResource.R_CODE_ERROR);
				}

				details.stream().forEach(serviceDetail -> {
					NPLSISolutionDataOffering solution = null;
					Map<String, Object> attributes = new HashMap<>();

					Integer sysId = (Integer) serviceDetail.get("sys_id");
					String linkType = (String) serviceDetail.get("pri_sec");
					
					Optional<NPLSISolutionDataOffering> solutions = siInfo.stream()
							.filter(si -> si.getServiceId().equals(sysId)).findFirst();
					if (solutions.isPresent()) {
						solution = solutions.get();
					} else {
						solution = new NPLSISolutionDataOffering();
						siInfo.add(solution);
					}
					
					NPLSolutionAttributes solutionAttributes = setNDESolutionAttributes(sysId,serviceId);
					solution.setAttributes(solutionAttributes);
					solution.setServiceId(sysId);
					solution.setLinkType(linkType);
					solution.setSiteType(solutionAttributes.getSiteType());
					solution.setSiteClassification((String) serviceDetail.get("site_classification"));

					List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository
							.getAssetDetailWithAttributes(sysId);
					Optional<Map<String, Object>> assetDetailInfo = assetDetailInfos.stream().findFirst();
					Map<String, Object> assetDetail = new HashMap<>();
					if (assetDetailInfo.isPresent()) {
						assetDetail = assetDetailInfo.get();
					}
					try {
						attributes = getFieldAttributes(serviceDetail, assetDetail, attributes);
						getAttributesForNDE(solution, attributes, details, assetDetailInfos,serviceId);
					} catch (TclCommonException e) {
						LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
			response.setProductName(productName);
			response.setSiInfo(siInfo);
			return response;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Method to set solution attributes for NDE service.
	 *
	 * @param sysId
	 * @return {@NPLSolutionAttributes}
	 */
	public NPLSolutionAttributes setNDESolutionAttributes(Integer sysId,String serviceId) {
		final ObjectMapper mapper = new ObjectMapper();
		NPLSolutionAttributes solutionAttributes = new NPLSolutionAttributes();
		List<Map<String, Object>> data = siOrderRepository.getServiceDetailByServiceId(sysId);
		if (data.stream().findFirst().isPresent()) {
			ServiceDetailBean serviceDetail = mapper.convertValue(data.stream().findFirst().get(),
					ServiceDetailBean.class);

			solutionAttributes.setOfferingName(serviceDetail.getOfferingName());
			solutionAttributes.setSiteAddress(serviceDetail.getSiteAddress());
			solutionAttributes.setSiteAlias(serviceDetail.getAlias());
			solutionAttributes.setAccessType(serviceDetail.getAccessType());
			solutionAttributes.setLatLong(serviceDetail.getLatLong());
			solutionAttributes.setBillingAccountId(serviceDetail.getBillingAccountId());
			solutionAttributes.setBillingAddress(serviceDetail.getBillingAddress());
			solutionAttributes.setBillingGstNumber(serviceDetail.getBillingGstNumber());
			solutionAttributes.setContractEndDate(serviceDetail.getContractEndDate());
			solutionAttributes.setContractStartDate(serviceDetail.getContractStartDate());
			solutionAttributes.setLeId(serviceDetail.getLeId());
			solutionAttributes.setLeName(serviceDetail.getLeName());
			solutionAttributes.setSupplierLeId(serviceDetail.getSupplierLeId());
			solutionAttributes.setSupplierLeName(serviceDetail.getSupplierLeName());
			solutionAttributes.setTaxExemptionFlag(serviceDetail.getTaxExemptionFlag());
			solutionAttributes.setSiServiceDetailId(serviceDetail.getSiServiceDetailId());
			solutionAttributes.setSiOrderId(serviceDetail.getSiOrderId());
			solutionAttributes.setTermInMonths(serviceDetail.getTermInMonths());
			solutionAttributes.setPrimaryServiceId(serviceDetail.getPrimaryServiceId());
			solutionAttributes.setSecondaryServiceId(serviceDetail.getSecondaryServiceId());
			solutionAttributes.setServiceStatus(serviceDetail.getServiceStatus());
			solutionAttributes.setLastMileProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setAccessProvider(serviceDetail.getLastmileProvider());
			solutionAttributes.setProductId(serviceDetail.getProductId());
			solutionAttributes.setServiceId(serviceDetail.getServiceId());
			solutionAttributes.setIpAddressProvidedBy(serviceDetail.getIpAddressProvidedBy());
			solutionAttributes.setCustomerId(serviceDetail.getCustomerId());
			solutionAttributes.setVpnName(serviceDetail.getVpnName());
			solutionAttributes.setServiceTopology(serviceDetail.getServiceTopology());
			solutionAttributes.setSiteLocationId(serviceDetail.getSiteLocationId());
			solutionAttributes.setPortSpeed(serviceDetail.getPortSpeed());
			solutionAttributes.setPortSpeedUnit(serviceDetail.getPortSpeedUnit());
			solutionAttributes.setSiteType(serviceDetail.getSiteType());
			solutionAttributes.setAssociateBillableId(serviceDetail.getAssociateBillableId());
			solutionAttributes.setCircuitExpiryDate(serviceDetail.getCircuitExpiryDate());
			// solutionAttributes.setChargeType(chargeType);
			// solutionAttributes.setNetworkProtection(networkProtection);
			
			

			if (Objects.nonNull(solutionAttributes.getPortSpeedUnit())
					&& solutionAttributes.getPortSpeedUnit().equalsIgnoreCase("kbps")) {
				if (Objects.nonNull(solutionAttributes.getPortSpeed())) {
					switch (solutionAttributes.getPortSpeed().trim()) {
					case "512": {
						solutionAttributes.setPortSpeed("0.5");
						solutionAttributes.setPortSpeedUnit("mpbs");
						break;
					}
					case "256": {
						solutionAttributes.setPortSpeed("0.25");
						solutionAttributes.setPortSpeedUnit("mpbs");
						break;
					}
					case "1024": {
						solutionAttributes.setPortSpeed("1");
						solutionAttributes.setPortSpeedUnit("mpbs");
					}
					default:
						break;
					}
				}
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = serviceDetail.getCommissionedDate();
			if (Objects.nonNull(date)) {
				String strDate = dateFormat.format(date);
				solutionAttributes.setCommissioningDate(strDate);
			}

			List<Map<String, Object>> localItContacts = siServiceContactRepository
					.findBySiServiceDetail_Id(serviceDetail.getSiServiceDetailId());
			Optional<Map<String, Object>> serviceDetailInfo = localItContacts.stream().findFirst();
			if (serviceDetailInfo.isPresent()) {
				Map<String, Object> detail = serviceDetailInfo.get();
				String contactName = (String) detail.get("contactName");
				String contactType = (String) detail.get("contactType");
				String businessEmail = (String) detail.get("businessEmail");
				String businessPhone = (String) detail.get("businessMobile");
				if (Objects.nonNull(contactType) && "LocalITContact".equalsIgnoreCase(contactType)) {
					solutionAttributes.setLocalItPhoneno(businessPhone);
					solutionAttributes.setLocalItName(contactName);
					solutionAttributes.setLocalItEmail(businessEmail);
				}
			}
		}
		return solutionAttributes;
	}

	/**
	 * Method to getAttributesValues.
	 *
	 * @param sysId
	 * @return {SIServiceDetail,attributeName}
	 */
	public String getAttributesValues(SIServiceDetail detail,String attributeName) {
		String attributeValue="";
		SIServiceAttribute attribute=siServiceAttributeRepository.findBySiServiceDetailAndAttributeNameOrderByIdDesc(detail, attributeName);
		if(attribute!=null) {
			attributeValue=attribute.getAttributeValue();
		}
		return attributeValue;
		
	}
	
	/**
	 * Method to getAttributesValues for nde.
	 *
	 * @param sysId
	 * @return {SIServiceDetail,attributeName}
	 */
	public SIServiceInfoBean getAttributesValuesNde(String serviceId) {
		LOGGER.info("ENTERED INTO getAttributesValuesNde");
		SIServiceInfoBean info=new SIServiceInfoBean();
		List<SIServiceDetail> siServiceData = siServiceDetailRepository
				.findByTpsServiceIdForNPL(serviceId);
		String ethrnetFlavour=getAttributesValues(siServiceData.get(0),"PRODUCT_FLAVOUR");
		String ishub=getAttributesValues(siServiceData.get(0),"CONNECTING_2_HUBS");
		String ehsid=getAttributesValues(siServiceData.get(0),"ASSOC_ETHERNET_HUB_SETUP_ID");
		info.setEthernetFlavour(ethrnetFlavour);
		info.setIsHub(ishub);
		info.setEhsId(ehsid);
		return info;
		
	}
	
	
	/**
	 * Method to get attributes for NDE service.
	 * 
	 * @param solution
	 * @param attributes
	 * @param details
	 * @param assetDetailInfo
	 * @throws TclCommonException
	 */
	public void getAttributesForNDE(NPLSISolutionDataOffering solution, Map<String,Object> attributes, List<Map<String, Object>> details, List<Map<String, Object>> assetDetailInfo,String serviceId) throws TclCommonException
	{
		Map<String,Object> attributesPopulated = new HashMap<>();
		attributesPopulated.putAll(getAttributesMap(details, assetDetailInfo));
		solution.setAttributeDetail(constructNDEAttributes(attributes,serviceId));
	}
	
	
	
	
	/**
	 * Method to get attribute details
	 * @param attributes
	 * @return
	 */
	public List<AttributeDetail> constructNDEAttributes(Map<String,Object> attributes,String serviceId)
	{
		List<AttributeDetail> attributeDetails=new ArrayList<>();
		if(Objects.nonNull(attributes)) {
			attributes.entrySet().stream().forEach(attributeElement -> {
				AttributeDetail attribute = new AttributeDetail();
				attribute.setName(attributeElement.getKey());
				attribute.setValue((String) attributeElement.getValue());
				attributeDetails.add(attribute);
			});
		}
		         // NDE attributes
					List<SIServiceDetail> siServiceData = siServiceDetailRepository
							.findByTpsServiceIdForNPL(serviceId);
						String serviceAvailability = getAttributesValues(siServiceData.get(0),"UPTIME");
						AttributeDetail attribute = new AttributeDetail();
						attribute.setName("Service Availability");
						attribute.setValue(serviceAvailability);
						attributeDetails.add(attribute);
						
						String portType=getAttributesValues(siServiceData.get(0),"PORT_TYPE");
						AttributeDetail attributePort = new AttributeDetail();
						attributePort.setName("Port Type");
						if(portType!=null) {
						attributePort.setValue(portType);
						}
						else {
							attributePort.setValue("Fixed");
						}
						attributeDetails.add(attributePort);
						
						AttributeDetail attributeaEndInter = new AttributeDetail();
						attributeaEndInter.setName("Interface Type - A end");
						attributeaEndInter.setValue(siServiceData.get(0).getSiteEndInterface());
						attributeDetails.add(attributeaEndInter);
						
						AttributeDetail attributeBendInter = new AttributeDetail();
						attributeBendInter.setName("Interface Type - B end");
						attributeBendInter.setValue(siServiceData.get(0).getSiteEndInterface());
						attributeDetails.add(attributeBendInter);
						
						String circuitSpeed=siServiceData.get(0).getLastmileBw()+siServiceData.get(0).getLastmileBwUnit();
						AttributeDetail attributeCp = new AttributeDetail();
						attributeCp.setName("Circuit Speed");
						attributeCp.setValue(circuitSpeed);
						attributeDetails.add(attributeCp);
						
						String connectorType = getAttributesValues(siServiceData.get(0),"CONNECTOR_TYPE");
						AttributeDetail attributeACt = new AttributeDetail();
						attributeACt.setName("Connector Type - A end");
						if(connectorType!=null) {
						attributeACt.setValue(connectorType);
						}
						else {
							attributeACt.setValue("");
						}
						attributeDetails.add(attributeACt);
						
						AttributeDetail attributeBCt = new AttributeDetail();
						attributeBCt.setName("Connector Type - B end");
						if(connectorType!=null) {
						attributeBCt.setValue(connectorType);
						}
						else {
							attributeBCt.setValue("");
						}
						attributeDetails.add(attributeBCt);
						
						String isHub = getAttributesValues(siServiceData.get(0),"PRODUCT_FLAVOUR");
						AttributeDetail attributeHp = new AttributeDetail();
						attributeHp.setName("Hub Parented");
						if(isHub!=null) {
							if(isHub.equalsIgnoreCase("Hub National Dedicated Ethernet")) {
								attributeHp.setValue("Yes");
							}
							else {
								attributeHp.setValue("No");
							}
						}
						else {
							attributeHp.setValue("No");
						}
						attributeDetails.add(attributeHp);
						
						
						String ehsId = getAttributesValues(siServiceData.get(0),"ASSOC_ETHERNET_HUB_SETUP_ID");
						AttributeDetail attributeEhs = new AttributeDetail();
						attributeEhs.setName("Hub Parent ID");
						if(ehsId!=null) {
						attributeEhs.setValue(ehsId);
						}
						else {
							attributeEhs.setValue("");
						}
						attributeDetails.add(attributeEhs);
						
						
		for (SIServiceDetail detail : siServiceData) {
			String siteType = getAttributesValues(detail, "SITE_TYPE");
			String frameSize = getAttributesValues(detail, "FRAME_SIZE");
			if (siteType != null) {
				if (frameSize != null) {
					if (siteType.equalsIgnoreCase("SiteA")) {
						AttributeDetail attributefS = new AttributeDetail();
						attributefS.setName("A Frame Sizes");
						attributefS.setValue(frameSize);
						attributeDetails.add(attributefS);
					} if (siteType.equalsIgnoreCase("SiteB")) {
						AttributeDetail attributefSbB = new AttributeDetail();
						attributefSbB.setName("B Frame Sizes");
						attributefSbB.setValue(frameSize);
						attributeDetails.add(attributefSbB);
					}
				} else {
					if (siteType.equalsIgnoreCase("SiteA")) {
						AttributeDetail attributefS = new AttributeDetail();
						attributefS.setName("A Frame Sizes");
						attributefS.setValue("");
						attributeDetails.add(attributefS);
					} if (siteType.equalsIgnoreCase("SiteB")) {
						AttributeDetail attributefSbB = new AttributeDetail();
						attributefSbB.setName("B Frame Sizes");
						attributefSbB.setValue("");
						attributeDetails.add(attributefSbB);
					}
				}
			} else {
				
				AttributeDetail attributefS = new AttributeDetail();
				attributefS.setName("A Frame Sizes");
				attributefS.setValue("");
				attributeDetails.add(attributefS);
				
				AttributeDetail attributefSbB = new AttributeDetail();
				attributefSbB.setName("B Frame Sizes");
				attributefSbB.setValue("");
				attributeDetails.add(attributefSbB);
				
			}
		}
				
						
						String vlanId = getAttributesValues(siServiceData.get(0),"VLAN_ID");
						AttributeDetail attributeVd = new AttributeDetail();
						attributeVd.setName("VLAN ID");
						if(vlanId!=null) {
						attributeVd.setValue(vlanId);
						}
						else {
							attributeVd.setValue("");
						}
						attributeDetails.add(attributeVd);
						
						
						String networkProtection = getAttributesValues(siServiceData.get(0),"Network_Protection");
						AttributeDetail attributeNp = new AttributeDetail();
						attributeNp.setName("Network Protection");
						if(networkProtection!=null) {
							attributeNp.setValue(networkProtection);
						}
						else {
							attributeNp.setValue("");
						}
						attributeDetails.add(attributeNp);
						

		
		return attributeDetails;
	}
	
	
	/**
	 * Method to get detailed ServiceInventory details list .
	 *
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	public List<NPLSIServiceDetailedResponse> getNDEMcDetailedSIInfoList(List<String> serviceIdList, Boolean isTermination)
			throws TclCommonException {
		List<NPLSIServiceDetailedResponse> serviceDetailResponseList = new ArrayList<>();
		LOGGER.info("Entered into getNDEMcDetailedSIInfoList");
		String productName = "NDE";
		if (Objects.isNull(serviceIdList)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("NDEMcDetailedSIInfoList" + serviceIdList.toString());

		if(isTermination == null) {
			LOGGER.info("isTermination is null, assuming it as false");
			isTermination = Boolean.FALSE;
		}
		try {
			for (String serviceId : serviceIdList) {
				NPLSIServiceDetailedResponse response = new NPLSIServiceDetailedResponse();
				List<NPLSISolutionDataOffering> siInfo = new ArrayList<>();
				LOGGER.info("Entered into serviceId id" + serviceId);
				List<Integer> sysIds = null;
				if(Boolean.TRUE.equals(isTermination)) {
					sysIds = siServiceInfoRepository.findLatestSysIdsForAServiceId(serviceId, productName);
				} else {
				 sysIds = siServiceInfoRepository.findSysIdsForAServiceId(serviceId, productName);
				}
				final Boolean isTerm = isTermination;
				sysIds.stream().forEach(id -> {
					List<Map<String, Object>> details =  Boolean.TRUE.equals(isTerm) ? siServiceInfoRepository.getLatestServiceDetailAttributesForNDE(serviceId,
							id) : siServiceInfoRepository.getServiceDetailAttributesForNDE(serviceId,
							id);
					if (details == null || details.isEmpty()) {
						throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT,
								ResponseResource.R_CODE_ERROR);
					}

					details.stream().forEach(serviceDetail -> {
						NPLSISolutionDataOffering solution = null;
						Map<String, Object> attributes = new HashMap<>();

						Integer sysId = (Integer) serviceDetail.get("sys_id");
						String linkType = (String) serviceDetail.get("pri_sec");

						Optional<NPLSISolutionDataOffering> solutions = siInfo.stream()
								.filter(si -> si.getServiceId().equals(sysId)).findFirst();
						if (solutions.isPresent()) {
							solution = solutions.get();
						} else {
							solution = new NPLSISolutionDataOffering();
							siInfo.add(solution);
						}

						NPLSolutionAttributes solutionAttributes = setNDESolutionAttributes(sysId, serviceId);
						solution.setAttributes(solutionAttributes);
						solution.setServiceId(sysId);
						solution.setLinkType(linkType);
						solution.setSiteType(solutionAttributes.getSiteType());
						solution.setSiteClassification((String) serviceDetail.get("site_classification"));

						List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository
								.getAssetDetailWithAttributes(sysId);
						Optional<Map<String, Object>> assetDetailInfo = assetDetailInfos.stream().findFirst();
						Map<String, Object> assetDetail = new HashMap<>();
						if (assetDetailInfo.isPresent()) {
							assetDetail = assetDetailInfo.get();
						}
						try {
							attributes = getFieldAttributes(serviceDetail, assetDetail, attributes);
							getAttributesForNDE(solution, attributes, details, assetDetailInfos, serviceId);
						} catch (TclCommonException e) {
							LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
					});
				});

				LOGGER.info("siInfo list size" + siInfo.size());
				response.setProductName(productName);
				response.setSiInfo(siInfo);
				serviceDetailResponseList.add(response);
			}
			LOGGER.info("serviceDetailResponseList list size" + serviceDetailResponseList.size());
			return serviceDetailResponseList;
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed getNDEMcDetailedSIInfoList for {}");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	//added for nde mc
	public List<SIServiceInfoBean> getServiceDetailsforNPLMc(String[] serviceIds) throws TclCommonException {
		LOGGER.info("enter into getServiceDetailsforNPLMc"+serviceIds);
		List<SIServiceInfoBean> siServiceDetailBeanList = new ArrayList<>();

		for (String serviceId : serviceIds) {
			LOGGER.info("enter into for loop id"+serviceId);
			if (StringUtils.isBlank(serviceId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			List<SIServiceInfo> siServiceInfo = siServiceInfoRepository.findByServiceIdNPL(serviceId);

			if (siServiceInfo.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
			}

			siServiceDetailBeanList.addAll(constructSiServiceInfoBean(siServiceInfo));
		}
		LOGGER.info("response siServiceDetailBeanList"+siServiceDetailBeanList.size());
		return siServiceDetailBeanList;
	}
	
	


	private List<PartnerLegalEntityBean> returnPartnerInformations(Set<Integer> partnerLeIds) {
		List<PartnerLegalEntityBean> partnerLegalEntityBeans;
		String response;
		List<Integer> partnerLes = partnerLeIds.stream().collect(Collectors.toList());
		try {
			response = (String) mqUtils.sendAndReceive(partnerIdsDetailsQueue, Utils.convertObjectToJson(partnerLes));
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_LEGAL_ENTITY_MQ_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		if (org.apache.commons.lang.StringUtils.isNotBlank(response)) {
			partnerLegalEntityBeans = Utils.fromJson(response, new TypeReference<List<PartnerLegalEntityBean>>() {
			});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_LEGAL_ENTITY_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		return partnerLegalEntityBeans;
	}


	public List<PartnerDetailBean> getCustomerLeBasedOnPartnerLe()
	{
		List<PartnerDetailBean> partnerDetailBeanList=new ArrayList<>();
		List<PartnerLegalEntityBean> listOfPartnerLegalEntity=returnPartnerInformations(getPartnerLeIds());
		for(PartnerLegalEntityBean partnerLe:listOfPartnerLegalEntity) {
			List<SIOrder> listOfSiOrderForPartnerLe = siOrderRepository.findCustomerLeByPartnerLeId(partnerLe.getId());
			PartnerDetailBean partnerDetailsBasedOnPartnerLeId = new PartnerDetailBean();
			if (listOfSiOrderForPartnerLe != null && !listOfSiOrderForPartnerLe.isEmpty()) {
				partnerDetailsBasedOnPartnerLeId.setPartnerLeId(partnerLe.getId());
				partnerDetailsBasedOnPartnerLeId.setPartnerLeName(partnerLe.getEntityName());
				List<CustomerOrderDetailsBean> listOfCustomerLeForPartnerLe = new ArrayList<>();
				for (SIOrder siOrder : listOfSiOrderForPartnerLe) {
					CustomerOrderDetailsBean customerLeOrderDetail = new CustomerOrderDetailsBean(siOrder);
					listOfCustomerLeForPartnerLe.add(customerLeOrderDetail);
				}
				partnerDetailsBasedOnPartnerLeId.setCustomers(listOfCustomerLeForPartnerLe);
				partnerDetailBeanList.add(partnerDetailsBasedOnPartnerLeId);
			}
		}
	return partnerDetailBeanList;
	}

	/**
	 * Get Interconnect Details by Org ID
	 *
	 * @param orgId
	 * @return {@link GscWholesaleInterconnectBean}
	 */
	public List<GscWholesaleInterconnectBean> getWholesaleCustomerInterconnect(Integer orgId) {
		LOGGER.info("Customer Org No :: {}", orgId);
		List<GscInterconnectDetails> gscInterconnectDetails = gscInterconnectDetailsRepository.findByOrgNo(orgId);
		return gscInterconnectDetails.stream()
				.map(this::toGscInterconnectBean)
				.collect(Collectors.toList());
	}

	private GscWholesaleInterconnectBean toGscInterconnectBean(GscInterconnectDetails gscInterconnectDetails) {
		GscWholesaleInterconnectBean gscWholesaleInterconnectBean = new GscWholesaleInterconnectBean();
		gscWholesaleInterconnectBean.setId(gscInterconnectDetails.getId());
		gscWholesaleInterconnectBean.setInterconnectId(String.valueOf(gscInterconnectDetails.getInterconnectId()));
		// Combining interconnect ID and Name for UI
		gscWholesaleInterconnectBean.setInterconnectName(String.valueOf(gscInterconnectDetails.getInterconnectId())
				+" - "+ gscInterconnectDetails.getInterconnectName());
		gscWholesaleInterconnectBean.setOrgNo(String.valueOf(gscInterconnectDetails.getOrgNo()));
		return gscWholesaleInterconnectBean;
	}

	/**
	 * Get Interconnect Name By ID
	 *
	 * @param interconnectId
	 * @return
	 */
	public String getInterconnectNameById(String interconnectId) {
		return gscInterconnectDetailsRepository.getInterconnectName(interconnectId);
	}

	/**
	 * Get GSC Pulse Charge Countries by Product Name and SECS ID.
	 *
	 * @param productName
	 * @param secsId
	 * @return
	 */
	public Set<Map<String, Object>> getGscPulseChargeCountries(String productName, String secsId) {
		List<String> secsIds = Arrays.asList(secsId.split("\\s*,\\s*"));
		LOGGER.info("Secs IDs :: {}", secsIds);
		return vwGscPulseChargeConfigDetailsRepository.findAllCountriesByProductNameAndSecsId(productName, secsIds);
	}

	/**
	 * Get GSC Product Names by SECS ID.
	 *
	 * @param secsId
	 * @return
	 */
	public List<String> getGscProductNames(String secsId) {
		List<String> secsIds = Arrays.asList(secsId.split("\\s*,\\s*"));
		LOGGER.info("Secs IDs :: {}", secsIds);
		LOGGER.info("Distinct product names :: {} ", vwGscPulseChargeConfigDetailsRepository.findProductNamesBySecsId(secsIds).toString());
		return vwGscPulseChargeConfigDetailsRepository.findProductNamesBySecsId(secsIds);
	}
	
	/* setMultiVrInfo
	 *
	 * @param ServiceDetailBeanList
	 * @return
	 * @throws TclCommonException
	 */
	public List<ServiceDetailBean> constructMultiVrfMasterAndSlaveInfo(List<ServiceDetailBean> ServiceDetailBeanList) {
		LOGGER.info("Entered into setMultiVrfMasterAndSlaveInfo" + ServiceDetailBeanList.size() + "productname"
				+ ServiceDetailBeanList.get(0).getProductName());
		try {
			if (!ServiceDetailBeanList.isEmpty()) {
				ServiceDetailBeanList.stream().forEach(ServiceDetailBean -> {
					String isMaster = "";
					String linkType = null;
					String masterServiceId = "";
					LOGGER.info("inside loop service id" + ServiceDetailBean.getServiceId());
					Optional<SIServiceDetail> serviceDetail = siServiceDetailRepository
							.findByTpsServiceId(ServiceDetailBean.getServiceId());
					if (serviceDetail.isPresent()) {
						linkType = serviceDetail.get().getPrimarySecondary();
						LOGGER.info(
								"multi vrf flag " + serviceDetail.get().getMultiVrfSolution() + "linkType" + linkType);
						if (serviceDetail.get().getMultiVrfSolution() != null) {
							if (serviceDetail.get().getMultiVrfSolution().equalsIgnoreCase("Yes")) {
								ServiceDetailBean.setMultiVrfSolution(serviceDetail.get().getMultiVrfSolution());
								isMaster = getAttributesValues(serviceDetail.get(), CommonConstants.MASTER_VRF_FLAG);
								LOGGER.info("MASTER_VRF_FLAG flag " + serviceDetail.get().getMultiVrfSolution());
								if (!isMaster.isEmpty()) {
									if (isMaster.equalsIgnoreCase("Yes")) {
										LOGGER.info("inside master");
										ServiceDetailBean.setIsMasterVrf(true);
										ServiceDetailBean = constructMultiVrfInfo(ServiceDetailBean,
												serviceDetail.get(), linkType, "Master");

									} else if (isMaster.equalsIgnoreCase("No")) {
										LOGGER.info("inside slave");
										ServiceDetailBean.setIsMasterVrf(false);
										masterServiceId = getAttributesValues(serviceDetail.get(),
												CommonConstants.MASTER_VRF_SERVICE_ID);
										LOGGER.info("inside slave masterServiceId"+masterServiceId);
										if (!masterServiceId.isEmpty()) {
											Optional<SIServiceDetail> masterServiceDetail = siServiceDetailRepository
													.findByTpsServiceId(masterServiceId);
											if (masterServiceDetail.isPresent()) {
												ServiceDetailBean = constructMultiVrfInfo(ServiceDetailBean,
														masterServiceDetail.get(), linkType, "Slave");
											}
										}

									}
								}
							}
						}

					}
				});
			}

		} catch (Exception e) {
			LOGGER.error("Exception in setting master and slave info in setMultiVrfMasterAndSlaveInfo" + e);
		}
		return ServiceDetailBeanList;

	}
	/* setMasterAndSlaveInfo
	 *
	 * @param ServiceDetailBeanList
	 * @return
	 * @throws TclCommonException
	 */
	public MasterVRFBean constructMasterSalveDetails(SIServiceDetail serviceDetail) {
		LOGGER.info("Inside setMasterSalveDetails " + serviceDetail.getTpsServiceId()+"bandwidth"+serviceDetail.getBwPortspeed()+" "+serviceDetail.getBwUnit());
		MasterVRFBean masterInfo = new MasterVRFBean();
		try {
			List<SlaveVRFBean> slaveDetails = new ArrayList<SlaveVRFBean>();
			String masterVrfName = "";
			String masterVrfBandwidth = "";
			String masterFlexicos = "";
			String noOfVrfs = "";
			String billingType = "";
			String vrfname = "";
			String vrfBandwidth = "";
			String flexicos = "";
			String slaveServiceIds = "";
			String totalBandWidth = "";
			masterVrfName = getAttributesValues(serviceDetail, CommonConstants.CUSTOMER_PROJECT_NAME);
			masterVrfBandwidth = serviceDetail.getBwPortspeed()+" "+serviceDetail.getBwUnit();
			masterFlexicos = getAttributesValues(serviceDetail, CommonConstants.FLEXICOS);
			noOfVrfs = getAttributesValues(serviceDetail, CommonConstants.NUMBER_OF_VRFS);
			billingType = CommonConstants.VRF_BASED_BILLING;
			slaveServiceIds = getAttributesValues(serviceDetail, CommonConstants.SLAVE_VRF_SERVICE_ID);
			totalBandWidth = getAttributesValues(serviceDetail, CommonConstants.TOTAL_VRF_BANDWIDTH_MBPS);
			LOGGER.info("totalBandWidth "+totalBandWidth);
			if(!totalBandWidth.isEmpty()) {
				if(!totalBandWidth.contains("Mbps")) {
				   totalBandWidth=totalBandWidth+" "+"Mbps";
				}
			}
			masterInfo.setMasterVrfName(masterVrfName);
			masterInfo.setMasterVrfBandwidth(masterVrfBandwidth);
			masterInfo.setBillingType(billingType);
			masterInfo.setMasterFlexiqos(masterFlexicos);
			masterInfo.setNoOfVrfs(noOfVrfs);
			masterInfo.setTotalVrfBandwidth(totalBandWidth);
			masterInfo.setMasterServiceId(serviceDetail.getTpsServiceId());
			LOGGER.info("slaveServiceIds list" + slaveServiceIds);
			if (!slaveServiceIds.isEmpty()) {
				String ServiceIds[] = slaveServiceIds.split(",");
				for (String slaveId : ServiceIds) {
					SlaveVRFBean slaveVrf = new SlaveVRFBean();
					vrfname = "";
					vrfBandwidth = "";
					flexicos = "";
					LOGGER.info("slaveServiceIds inside loop" + slaveId);
					Optional<SIServiceDetail> slaveinfo = siServiceDetailRepository.findByTpsServiceId(slaveId);
					if (slaveinfo.isPresent()) {
						LOGGER.info("slave bandwidth" + slaveinfo.get().getBwPortspeed()+" "+slaveinfo.get().getBwUnit());
						vrfname = getAttributesValues(slaveinfo.get(), CommonConstants.CUSTOMER_PROJECT_NAME);
						vrfBandwidth = slaveinfo.get().getBwPortspeed()+" "+slaveinfo.get().getBwUnit();
						flexicos = getAttributesValues(slaveinfo.get(), CommonConstants.FLEXICOS);
						slaveVrf.setVrfName(vrfname);
						slaveVrf.setVrfBandwidth(vrfBandwidth);
						slaveVrf.setFlexiqos(flexicos);
						slaveVrf.setServiceId(slaveinfo.get().getTpsServiceId());
						slaveDetails.add(slaveVrf);
					}
				}
				masterInfo.setSlaveVRFDetails(slaveDetails);
			}

		} catch (Exception e) {
			LOGGER.error("Exception in setMasterSalveDetails" + e);
		}
		return masterInfo;
	}
	
	/* setMultiVrfBean
	 *
	 * @param ServiceDetailBeanList
	 * @return
	 * @throws TclCommonException
	 */
	public SISolutionDataOffering constructMultiVrfBean(SISolutionDataOffering solution, SIServiceDetail siServiceDetail,
			String linkType, String isMasterSlave){
		LOGGER.info("ENTERED into setMultiVrfBean linktype" + linkType + "isMasterSlave" + isMasterSlave + "serviceid"
				+ siServiceDetail.getTpsServiceId());
		try {
			MasterVRFBean primaryMasterInfo = new MasterVRFBean();
			MasterVRFBean secondaryMasterInfo = new MasterVRFBean();
			if (Objects.nonNull(linkType) && (linkType.equalsIgnoreCase("PRIMARY"))) {
				primaryMasterInfo = constructMasterSalveDetails(siServiceDetail);
				solution.setPrimaryMasterVRFDetail(primaryMasterInfo);
				LOGGER.info("DUAL scenario inside" + isMasterSlave + "primary linktype and secandory service id"
						+ siServiceDetail.getPriSecServiceLink() + "serviceid" + siServiceDetail.getTpsServiceId());
				if (siServiceDetail.getPriSecServiceLink() != null) {
					Optional<SIServiceDetail> siSecondaryServiceDetail = siServiceDetailRepository
							.findByTpsServiceId(siServiceDetail.getPriSecServiceLink());
					if (siSecondaryServiceDetail.isPresent()) {
						secondaryMasterInfo = constructMasterSalveDetails(siSecondaryServiceDetail.get());
						solution.setSecondaryMasterVRFDetail(secondaryMasterInfo);
					}
				}
			}
			else if (Objects.nonNull(linkType) && (linkType.equalsIgnoreCase("SECONDARY"))) {
				secondaryMasterInfo = constructMasterSalveDetails(siServiceDetail);
				solution.setSecondaryMasterVRFDetail(secondaryMasterInfo);
				LOGGER.info("DUAL scenario inside " + isMasterSlave + " secondary linktype and primary service id"
						+ siServiceDetail.getPriSecServiceLink() + "serviceid" + siServiceDetail.getTpsServiceId());
				if (siServiceDetail.getPriSecServiceLink() != null) {
					Optional<SIServiceDetail> primaryServiceDetail = siServiceDetailRepository
							.findByTpsServiceId(siServiceDetail.getPriSecServiceLink());
					if (primaryServiceDetail.isPresent()) {
						primaryMasterInfo = constructMasterSalveDetails(primaryServiceDetail.get());
						solution.setPrimaryMasterVRFDetail(primaryMasterInfo);
					}
				}
			} else {
				LOGGER.info("inside single scenario service id" + siServiceDetail.getTpsServiceId());
				primaryMasterInfo = constructMasterSalveDetails(siServiceDetail);
				solution.setPrimaryMasterVRFDetail(primaryMasterInfo);
			}
			//set total vrf bandwidth instead of port speed for multivrf
			if (solution.getPrimary() != null && solution.getPrimaryMasterVRFDetail()!=null) {
				LOGGER.info("primary PORT SPEED" + solution.getPrimary().getPortSpeed() + "PORT UNIT"
						+ solution.getPrimary().getPortSpeedUnit()+"toatlvrfBandwwith"+solution.getPrimaryMasterVRFDetail().getTotalVrfBandwidth());
				if (!solution.getPrimaryMasterVRFDetail().getTotalVrfBandwidth().isEmpty()) {
					String[] bandwith = solution.getPrimaryMasterVRFDetail().getTotalVrfBandwidth().split(" ");
					LOGGER.info("after split PORT SPEED" + bandwith[0]);
					solution.getPrimary().setPortSpeed(bandwith[0]);
					solution.getPrimary().setPortSpeedUnit("Mbps");
				}
			}
			if(solution.getSecondary()!=null && solution.getSecondaryMasterVRFDetail()!=null) {
				LOGGER.info("secondary PORT SPEED" + solution.getSecondary().getPortSpeed() + "PORT UNIT"
						+ solution.getSecondary().getPortSpeedUnit()+"toatlvrfBandwwith"+solution.getSecondaryMasterVRFDetail().getTotalVrfBandwidth());
				if (!solution.getSecondaryMasterVRFDetail().getTotalVrfBandwidth().isEmpty()) {
					String[] bandwith = solution.getSecondaryMasterVRFDetail().getTotalVrfBandwidth().split(" ");
					LOGGER.info("after split PORT SPEED" + bandwith[0]);
					solution.getSecondary().setPortSpeed(bandwith[0]);
					solution.getSecondary().setPortSpeedUnit("Mbps");
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in settig setMultiVrfBean ServiceInformation for {}"+ ex);
		}
		return solution;
	}
	
	/* constructMultiVrfInfo 
	 *
	 * @param ServiceDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	public ServiceDetailBean constructMultiVrfInfo(ServiceDetailBean serviceDetailBean,SIServiceDetail serviceDetail,
			String linkType, String isMasterSlave){
		LOGGER.info("ENTERED into constructMultiVrfInfo linktype" + linkType + "isMasterSlave" + isMasterSlave + "serviceid"
				+ serviceDetailBean.getServiceId());
		try {
			MasterVRFBean primaryMasterInfo = new MasterVRFBean();
			MasterVRFBean secondaryMasterInfo = new MasterVRFBean();
			if (Objects.nonNull(linkType) && (linkType.equalsIgnoreCase("PRIMARY"))) {
				primaryMasterInfo = constructMasterSalveDetails(serviceDetail);
				serviceDetailBean.setPrimaryMasterVRFDetail(primaryMasterInfo);
				LOGGER.info(
						"DUAL scenario inside "+isMasterSlave+" primary linktype and secandory service id"
								+ serviceDetail.getPriSecServiceLink() + "serviceid"
								+ serviceDetail.getTpsServiceId());
				if (serviceDetail.getPriSecServiceLink() != null) {
					Optional<SIServiceDetail> siSecondaryServiceDetail = siServiceDetailRepository
							.findByTpsServiceId(serviceDetail.getPriSecServiceLink());
					if (siSecondaryServiceDetail.isPresent()) {
						secondaryMasterInfo = constructMasterSalveDetails(
								siSecondaryServiceDetail.get());
						serviceDetailBean.setSecondaryMasterVRFDetail(secondaryMasterInfo);
					}
				}
			}
			else if (Objects.nonNull(linkType) && (linkType.equalsIgnoreCase("SECONDARY"))) {
				secondaryMasterInfo = constructMasterSalveDetails(serviceDetail);
				serviceDetailBean.setSecondaryMasterVRFDetail(secondaryMasterInfo);
				LOGGER.info(
						"DUAL scenario inside "+isMasterSlave+"secondary linktype and primary service id"
								+ serviceDetail.getPriSecServiceLink() + "serviceid"
								+ serviceDetail.getTpsServiceId());
				if (serviceDetail.getPriSecServiceLink() != null) {
					Optional<SIServiceDetail> primaryServiceDetail = siServiceDetailRepository
							.findByTpsServiceId(serviceDetail.getPriSecServiceLink());
					if (primaryServiceDetail.isPresent()) {
						primaryMasterInfo = constructMasterSalveDetails(
								primaryServiceDetail.get());
						serviceDetailBean.setPrimaryMasterVRFDetail(primaryMasterInfo);
					}
				}
			} else {
				LOGGER.info("inside single scenario service id"
						+ serviceDetail.getTpsServiceId());
				primaryMasterInfo = constructMasterSalveDetails(serviceDetail);
				serviceDetailBean.setPrimaryMasterVRFDetail(primaryMasterInfo);

			}
			//set total vrf bandwith instead of port speed for multivrf
			if(serviceDetailBean.getPrimaryMasterVRFDetail()!=null) {
				LOGGER.info("inside primary toatal vrfbandwidth"+serviceDetailBean.getPrimaryMasterVRFDetail().getTotalVrfBandwidth()+"portspeed"+serviceDetailBean.getPortSpeed()+
						"unit"+serviceDetailBean.getPortSpeedUnit());
				if(!serviceDetailBean.getPrimaryMasterVRFDetail().getTotalVrfBandwidth().isEmpty()) {
					String[] bandwith = serviceDetailBean.getPrimaryMasterVRFDetail().getTotalVrfBandwidth().split(" ");
					LOGGER.info("after split PORT SPEED" + bandwith[0]);
					serviceDetailBean.setPortSpeed(bandwith[0]);
					serviceDetailBean.setPortSpeedUnit("Mbps");
				}
			}
			
		} catch (Exception ex) {
			LOGGER.error("Exception in settig constructMultiVrfInfo ServiceInformation for {}"+ ex);
		}
		return serviceDetailBean;
	}



	/**
	 * PIPF-373
	 * @param serviceId
	 * @param product
	 * @param number
	 * @param outpulse
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String,String> getLMProviderAndAccesstype(String serviceId, String product, String number, String outpulse) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		LOGGER.info("Servicee id is {}", serviceId);
		Map<String,String> map = new HashMap<>();
		Optional<SIServiceDetail> optDetail = null;
		List<SIServiceDetail> siServiceDetailList = null;
		SIServiceDetail siDetail = null;
		if("GSC".equalsIgnoreCase(product)) {
			if (StringUtils.isBlank(number) || StringUtils.isBlank(outpulse)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			optDetail = siServiceDetailRepository.findByTpsServiceIdForGSC(serviceId, number, outpulse);
			LOGGER.info("Inventory details {]", optDetail.toString());
			if (!optDetail.isPresent()) {
				throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
			}

			map.put("lastMileProvider", optDetail.get().getLastmileProvider());
			map.put("accessType", optDetail.get().getAccessType());

		} else {
			siServiceDetailList = siServiceDetailRepository.findByTpsServiceIdForNPL(serviceId);
			LOGGER.info("Inventory details list{]", siServiceDetailList.toString());
			if (siServiceDetailList.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
			}
			siDetail= siServiceDetailList.stream().sorted(Comparator.comparing(SIServiceDetail::getUpdatedDate).reversed()).findFirst().orElse(null); // picking latest value from inventory
			map.put("lastMileProvider", siDetail.getLastmileProvider());
			map.put("accessType", siDetail.getAccessType());
		}

		LOGGER.info("map::{}"+map.toString());
		return map;

	}

	/**
	 * Get All distinct details based on each gsc configurations
	 *
	 * @param productName
	 * @param request
	 * @return {@link SIConfigurationCountryBean}
	 */
	public GSCConfigurationDisntictDetailsBean getDistinctDetailsOfEachConfigurations(String productName, GscServiceInventoryConfigurationRequestBean request) {
		GSCConfigurationDisntictDetailsBean gscConfigurationDisntictDetailsBean = new GSCConfigurationDisntictDetailsBean();

		List<GSCConfigurationDetailsBean> gscConfigurationDetailsBeans = mapRows(() ->
				siServiceDetailRepository.findAllConfigurationDetails(productName, request.getCustomerId(), ASSET_TYPE_TFN), this::toConfigurationDetails);

		if(!CollectionUtils.isEmpty(gscConfigurationDetailsBeans)){
			List<GSCConfigurationDetailsBean> configurationDetailsBeanBasedOnFilterValue = getConfigurationDetailsBeanBasedOnFilterValue(request, gscConfigurationDetailsBeans);

			setAllDistinctDetails(gscConfigurationDisntictDetailsBean, configurationDetailsBeanBasedOnFilterValue);

			setAllConfigurationsOfGsc(productName, request, gscConfigurationDisntictDetailsBean);
		}
		return gscConfigurationDisntictDetailsBean;
	}

	/**
	 *  set all configurations of gsc
	 *
	 * @param productName
	 * @param request
	 * @param gscConfigurationDisntictDetailsBean
	 */
	private void setAllConfigurationsOfGsc(String productName, GscServiceInventoryConfigurationRequestBean request, GSCConfigurationDisntictDetailsBean gscConfigurationDisntictDetailsBean) {
		List<SIConfigurationCountryBean> siConfigurationCountryBean = new ArrayList<>();
		if(!productName.equalsIgnoreCase(DOMESTIC_VOICE)) {
			LOGGER.info("Product is not domestic voice, but is {}", productName);
			setAllConfigurationsOfProductsOtherThanDomesticVoice(productName, request, gscConfigurationDisntictDetailsBean);
		}
		else{
			LOGGER.info("Product is {}", productName);
			setAllConfigurationsOfDomesticVoice(productName, request,gscConfigurationDisntictDetailsBean);
		}
	}

	/**
	 * set all configurations of domestic voice
	 *  @param productName
	 * @param request
	 * @param gscConfigurationDisntictDetailsBean
	 */
	private void setAllConfigurationsOfDomesticVoice(String productName, GscServiceInventoryConfigurationRequestBean request, GSCConfigurationDisntictDetailsBean gscConfigurationDisntictDetailsBean) {
		List<SIConfigurationCountryBean> siConfigurationCountryBean;
		if (!ALL.equalsIgnoreCase(request.getCustomerLeId()) || !ALL.equalsIgnoreCase(request.getSecsId()) || !ALL.equalsIgnoreCase(request.getAccessType()) || Objects.nonNull(request.getNumber())) {
			LOGGER.info("Search by params");
			siConfigurationCountryBean = getAllConfigurationsBasedOnFiltersForDomesticVoice(productName, request);
		} else {
			LOGGER.info("get all the configurations of product");
			siConfigurationCountryBean = getAllConfigurationsOfDomesticVoice(productName, request.getCustomerId());
		}
		gscConfigurationDisntictDetailsBean.setSiConfigurations(siConfigurationCountryBean);
	}

	/**
	 *  get all configurations of dv
	 *  @param productName
	 * @param customerId
	 */
	private List<SIConfigurationCountryBean> getAllConfigurationsOfDomesticVoice(String productName, Integer customerId) {
		List<SIConfigurationCountryBean> siConfigurationCountryBean;
		List<SIConfigurationByLeBean> siConfigurationByLeBeans = mapRows(() ->
				siServiceDetailRepository.findSiteAddressConfigurationsByProductName(productName, getCustomerLeIds(customerId), ASSET_TYPE_TFN), this::toCustomerLe);
		siConfigurationCountryBean = getSiConfigurationByCustomerLe(siConfigurationByLeBeans);
		return siConfigurationCountryBean;
	}

	/**
	 * get all configurations based on filters for dv
	 *  @param productName
	 * @param request
	 */
	private List<SIConfigurationCountryBean> getAllConfigurationsBasedOnFiltersForDomesticVoice(String productName, GscServiceInventoryConfigurationRequestBean request) {
		List<SIConfigurationCountryBean> siConfigurationCountryBean;
		List<SIConfigurationByLeBean> siConfigurationByLeBeans = serviceInventoryDao.findSiteConfigurationsByParams(productName, ASSET_TYPE_TFN, request);
		siConfigurationCountryBean = getSiConfigurationByCustomerLe(siConfigurationByLeBeans);
		return siConfigurationCountryBean;
	}

	/***
	 * set all configurations of products other than DV
	 *
	 * @param productName
	 * @param request
	 * @param gscConfigurationDisntictDetailsBean
	 */
	private void setAllConfigurationsOfProductsOtherThanDomesticVoice(String productName, GscServiceInventoryConfigurationRequestBean request, GSCConfigurationDisntictDetailsBean gscConfigurationDisntictDetailsBean) {
		List<SIConfigurationCountryBean> siConfigurationCountryBean;
		if (!ALL.equalsIgnoreCase(request.getCustomerLeId()) || !ALL.equalsIgnoreCase(request.getSecsId()) || !ALL.equalsIgnoreCase(request.getAccessType()) || Objects.nonNull(request.getNumber())) {
			LOGGER.info("Search by params");
			siConfigurationCountryBean = getAllConfigurationsBasedOnFilterValue(productName, request);
		} else {
			LOGGER.info("get all the configurations of product");
			siConfigurationCountryBean = getAllConfigurations(productName, request.getCustomerId());
		}
		gscConfigurationDisntictDetailsBean.setSiConfigurations(siConfigurationCountryBean);
	}

	/**
	 * get all configurations
	 *  @param productName
	 * @param customerId
	 */
	private List<SIConfigurationCountryBean> getAllConfigurations(String productName, Integer customerId) {
		List<SIConfigurationCountryBean> siConfigurationCountryBean;
		List<SICountryBean> countries = mapRows(() -> siServiceDetailRepository.findConfigurationsByProductName(productName, getCustomerLeIds(customerId), ASSET_TYPE_TFN, customerId), this::toCountry);
		if (ServiceInventoryConstants.UIFN.equalsIgnoreCase(productName)) {
			siConfigurationCountryBean = getSiConfigurationCountryBeanForUIFN(countries);
		}
		siConfigurationCountryBean = getSiConfigurationCountryBean(countries);
		return siConfigurationCountryBean;
	}

	/**
	 * get all configurations based on filters
	 * @param productName
	 * @param request
	 */
	private List<SIConfigurationCountryBean> getAllConfigurationsBasedOnFilterValue(String productName, GscServiceInventoryConfigurationRequestBean request) {
		List<SIConfigurationCountryBean> siConfigurationCountryBean;
		List<SICountryBean> countries = new ArrayList<>();
		if(!ALL.equalsIgnoreCase(request.getCustomerLeId())){
			LOGGER.info("customer Le Id is {}", request.getCustomerLeId());
			countries = serviceInventoryDao.findConfigurationsByParams(productName, ASSET_TYPE_TFN, request, Arrays.asList(Integer.valueOf(request.getCustomerLeId())));
		}
		else{
			countries = serviceInventoryDao.findConfigurationsByParams(productName, ASSET_TYPE_TFN, request, getCustomerLeIds(request.getCustomerId()));
		}

		if (ServiceInventoryConstants.UIFN.equalsIgnoreCase(productName)) {
			siConfigurationCountryBean = getSiConfigurationCountryBeanForUIFN(countries);
		}
		siConfigurationCountryBean = getSiConfigurationCountryBean(countries);
		return siConfigurationCountryBean;
	}

	/**
	 * set all distinct details in response
	 *
	 * @param gscConfigurationDisntictDetailsBean
	 * @param configurationDetailsBeanBasedOnFilterValue
	 */
	private void setAllDistinctDetails(GSCConfigurationDisntictDetailsBean gscConfigurationDisntictDetailsBean, List<GSCConfigurationDetailsBean> configurationDetailsBeanBasedOnFilterValue) {
		Map<Integer, String> customerLeMapping = configurationDetailsBeanBasedOnFilterValue.stream().collect(Collectors.toMap(GSCConfigurationDetailsBean::getCustomerLeId, GSCConfigurationDetailsBean::getCustomerLeName, (existing, replacement) -> existing));
		gscConfigurationDisntictDetailsBean.setCustomerLes(customerLeMapping);
		LOGGER.info("Distinct customer le are {}", customerLeMapping.size());

		List<String> accessTypes = configurationDetailsBeanBasedOnFilterValue.stream().map(GSCConfigurationDetailsBean::getAccessType).filter(Objects::nonNull).distinct().collect(Collectors.toList());
		gscConfigurationDisntictDetailsBean.setAccessTypes(accessTypes);
		LOGGER.info("Distinct accessTypes are {}", accessTypes.size());

		List<String> secsIds = configurationDetailsBeanBasedOnFilterValue.stream().map(GSCConfigurationDetailsBean::getSecsId).filter(Objects::nonNull).filter(s-> s!="null").distinct().collect(Collectors.toList());
		gscConfigurationDisntictDetailsBean.setSecsIds(secsIds);
		LOGGER.info("Distinct secsId are {}", secsIds.size());
	}

	/**
	 * Get configuration details bean based on filter value
	 *
	 * @param request
	 * @param gscConfigurationDetailsBeans
	 * @return
	 */
	private List<GSCConfigurationDetailsBean> getConfigurationDetailsBeanBasedOnFilterValue(GscServiceInventoryConfigurationRequestBean request, List<GSCConfigurationDetailsBean> gscConfigurationDetailsBeans) {
		if(!ALL.equalsIgnoreCase(request.getCustomerLeId())){
			LOGGER.info("Filtering with customer le id {}", request.getCustomerLeId());
			gscConfigurationDetailsBeans = gscConfigurationDetailsBeans.stream().filter(gscConfigurationDetailsBean -> gscConfigurationDetailsBean.getCustomerLeId().equals(Integer.valueOf(request.getCustomerLeId())))
					.collect(Collectors.toList());
		}

		if(!ALL.equalsIgnoreCase(request.getSecsId())){
			LOGGER.info("Filtering with secsId {}", request.getSecsId());
			gscConfigurationDetailsBeans = gscConfigurationDetailsBeans.stream().filter(gscConfigurationDetailsBean -> gscConfigurationDetailsBean.getSecsId().equalsIgnoreCase(request.getSecsId()))
					.collect(Collectors.toList());
		}

		if(!ALL.equalsIgnoreCase(request.getAccessType())){
			LOGGER.info("Filtering with accessType {}", request.getAccessType());
			gscConfigurationDetailsBeans = gscConfigurationDetailsBeans.stream().filter(gscConfigurationDetailsBean -> gscConfigurationDetailsBean.getAccessType().equalsIgnoreCase(request.getAccessType()))
					.collect(Collectors.toList());
		}

		if(Objects.nonNull(request.getNumber())){
			LOGGER.info("Filtering with number {}", request.getNumber());
			gscConfigurationDetailsBeans = gscConfigurationDetailsBeans.stream().filter(gscConfigurationDetailsBean -> gscConfigurationDetailsBean.getTollFreeNumber().contains(request.getNumber()))
					.collect(Collectors.toList());
		}
		return gscConfigurationDetailsBeans;
	}


	/**
	 * Method to map the configuration output to bean
	 *
	 * @param row
	 * @return {@link GSCConfigurationDetailsBean}
	 */
	private GSCConfigurationDetailsBean toConfigurationDetails(Map<String, Object> row) {
		GSCConfigurationDetailsBean gscConfigurationDetailsBean = new GSCConfigurationDetailsBean();
		gscConfigurationDetailsBean.setCustomerLeId((Integer) row.get(CUSTOMER_LE_ID));
		gscConfigurationDetailsBean.setCustomerLeName((String) row.get(CUSTOMER_LE_NAME));
		gscConfigurationDetailsBean.setAccessType(valueOf(row.get(ACCESS_TYPE)));
		gscConfigurationDetailsBean.setSecsId(valueOf(row.get(SECS_ID)));
		gscConfigurationDetailsBean.setTollFreeNumber(valueOf(row.get(TOLL_FREE_NUMBER)));
		gscConfigurationDetailsBean.setOrigin((String) row.get(ORIGIN));
		gscConfigurationDetailsBean.setDestination((String) row.get(DESTINATION));
		gscConfigurationDetailsBean.setAssetId((Integer) row.get(ASSET_ID));
		gscConfigurationDetailsBean.setOrderId((Integer) row.get(ORDER_ID));
		gscConfigurationDetailsBean.setAssetType((String) row.get(ASSET_TYPE));
		//gscConfigurationDetailsBean.setNumber((String) row.get(NUMBER));
		gscConfigurationDetailsBean.setOutpulse((String) row.get(OUTPULSE));
		gscConfigurationDetailsBean.setOriginNetwork((String) row.get(ORIGIN_NETWORK));
		gscConfigurationDetailsBean.setTigerOrderId((String) row.get(TIGER_ORDER_ID));
		gscConfigurationDetailsBean.setGscOrderSequenceId((String) row.get(ORDER_SEQUENCE_ID));
		return gscConfigurationDetailsBean;
	}

	/**
	 * Update Outpulse By SI Order ID
	 *
	 * @param siOrderId
	 * @param outpulse
	 */
	public void updateOutpulseBySIOrderId(String siOrderId, String outpulse, String referenceOrderId) throws TclCommonException {
		LOGGER.info("SI Order ID :: {} and Outpulse :: {} and referenceOrderId :: {}", siOrderId, outpulse, referenceOrderId);
//		siServiceDetailRepository.findAllBySiOrderId(siOrderId).stream().forEach(siServiceDetail -> {
//			if (referenceOrderId.equalsIgnoreCase(siServiceDetail.getGscOrderSequenceId())) {
//				siServiceDetail.getSiAssets().stream().forEach(siAsset -> {
//					if ("Toll-Free".equalsIgnoreCase(siAsset.getType())) {
//						siAsset.setFqdn(outpulse);
//						siAssetRepository.save(siAsset);
//					}
//				});
//			}
//		});
		try {
			List<SIAsset> siAssets = siAssetRepository.findByGscOrderSequenceId(siOrderId, referenceOrderId);
			siAssets.stream().findFirst().ifPresent(siAsset -> {
				siAsset.setName(outpulse);
				siAssetRepository.save(siAsset);
				LOGGER.info("GSC Outpulse Saved Successfully; SI Asset ID :: {}", siAsset.getId());
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	


	public List<SIServiceDetailsBean> getServiceDetailsForCancellation(String[] serviceIds) throws TclCommonException {
		List<SIServiceDetailsBean>  serviceDetailsBeanList  =  new ArrayList<>();
        if(serviceIds == null || serviceIds.length == 0) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
        LOGGER.info("getServiceDetailsForCancellation serviceid {} ", serviceIds);
        List<String> serviceIdsList = Arrays.asList(serviceIds);
       List<ViewSiServiceInfoAll> serviceInfoList =  vwSiServiceInfoAllRepository.findByServiceIdInAndServiceStatusAndIsActive(serviceIdsList, "Under Provisioning","N");
       if( serviceInfoList != null && !serviceInfoList.isEmpty()) {
    	   serviceInfoList.stream().forEach(serviceInfo -> {
    		   LOGGER.info("getServiceDetailsForCancellation Servie Info {}", serviceInfo.getServiceId());
    		   SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();
    		   serviceDetailBean.setAccessType(serviceInfo.getAccessType());
    		   serviceDetailBean.setAccessProvider(serviceInfo.getLastMileProvider());
    		   serviceDetailBean.setArc(serviceInfo.getArc()!=null ?serviceInfo.getArc():0 );
    		   serviceDetailBean.setBillingFrequency(serviceInfo.getBillingFrequency());
    		   serviceDetailBean.setBillingMethod(serviceInfo.getBillingMethod());
    		   serviceDetailBean.setContractTerm(serviceInfo.getOrderTermInMonths());
    		   serviceDetailBean.setErfCustomerId(serviceInfo.getOrderCustomerId());
    		   serviceDetailBean.setErfCustomerLeId(serviceInfo.getOrderCustLeId());
    		   serviceDetailBean.setErfCustomerLeName(serviceInfo.getOrderCustLeName());
    		   serviceDetailBean.setErfCustomerName(serviceInfo.getOrderCustomer());
    		   serviceDetailBean.setErfPrdCatalogOfferingId(serviceInfo.getProductOfferingId());
    		   serviceDetailBean.setErfPrdCatalogOfferingName(serviceInfo.getProductOfferingName());
    		   serviceDetailBean.setLinkType(serviceInfo.getPrimaryOrSecondary());
    		   serviceDetailBean.setLastmileBw(serviceInfo.getLastMileBandwidth());
    		   serviceDetailBean.setLastmileBwUnit(serviceInfo.getLastMileBandwidthUnit());
    		   serviceDetailBean.setPortBw(serviceInfo.getBandwidth());
    		   serviceDetailBean.setPortBwUnit(serviceInfo.getBandwidthUnit());
    		   serviceDetailBean.setTpsServiceId(serviceInfo.getServiceId());
    		   serviceDetailBean.setPriSecServLink(serviceInfo.getPrimarySecondaryLink());
    		   serviceDetailBean.setNrc(serviceInfo.getNrc());
    		   serviceDetailBean.setReferenceOrderId(serviceInfo.getOrderSysId());
    		   serviceDetailBean.setVpnName(serviceInfo.getVpnName());
    		   serviceDetailBean.setId(serviceInfo.getId());
    		   serviceDetailBean.setErfSpLeId(serviceInfo.getOrderSpLeId());
    		   serviceDetailBean.setErfSpLeName(serviceInfo.getOrderSpLeName());
    		   serviceDetailBean.setCustomerCurrencyId(serviceInfo.getCurrencyId());
    		   //serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
    		   //serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
    		   serviceDetailBean.setOpportunityType(serviceInfo.getOpportunityType());
    		   serviceDetailBean.setMrc(serviceInfo.getMrc());
			   LOGGER.info("Order Partner id for service info id {} is  {} ", serviceInfo.getServiceId(), serviceInfo.getOrderPartner());
    		   serviceDetailBean.setPartnerId(serviceInfo.getOrderPartner());
			   serviceDetailBean.setPartnerCuid(serviceInfo.getPartnerCuid());
			   serviceDetailBean.setErfCustPartnerLeId(serviceInfo.getErfCustPartnerLeId());
			   serviceDetailBean.setDemarcationApartment(serviceInfo.getDemarcationApartment());
			   serviceDetailBean.setDemarcationFloor(serviceInfo.getDemarcationFloor());
			   serviceDetailBean.setDemarcationRoom(serviceInfo.getDemarcationRoom());
			   //serviceDetailBean.setDemarcationRack(serviceInfo.getDemarcationRack());
    		   serviceDetailBean.setContractStartDate(serviceInfo.getContractStartDate());
    		   serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
    		   serviceDetailBean.setServiceManagementOption(serviceInfo.getServiceManagementOption());
    		   serviceDetailBean.setServiceTopology(serviceDetailBean.getServiceTopology());
    		   serviceDetailBean.setSiteTopology(serviceInfo.getGvpnSiteTopology());
    		   serviceDetailBean.setErfLocSiteAddressId(serviceInfo.getLocationId());
    		   serviceDetailBean.setMrc(serviceInfo.getMrc()!=null ?serviceInfo.getMrc():0);
    		   serviceDetailBean.setNrc(serviceInfo.getNrc()!=null ?serviceInfo.getNrc():0);
    		   
   				
   				
    		   serviceDetailsBeanList.add(serviceDetailBean);
    	   });
    	   
       }
        return serviceDetailsBeanList;
	}
	

	public List<SIServiceAttributeBean> getServiceInventoryAttributes(String serviceCode) {
		LOGGER.info("Inside getServiceInventoryAttributes with service id {}", serviceCode);
		List<SIServiceAttributeBean> siServiceAttributeBeans=new ArrayList<>();
		SIServiceDetail siServiceDetail = siServiceDetailRepository
				.findFirstByUuidAndServiceStatusIgnoreCaseAndIsActiveOrderByIdDesc(
						serviceCode, "Active", CommonConstants.Y);
		if (siServiceDetail!=null) {
			siServiceAttributeBeans=serviceInventoryHelperMapper.getSiServiceAttributes(siServiceDetail);
			LOGGER.info("Service atrributes size : {}",siServiceAttributeBeans.size());
		}
		LOGGER.info("ServiceInventoryService terminateService ends");
		return siServiceAttributeBeans;
	}
	
	
	public List<NPLSIServiceDetailedResponse> getNPLDetailedSIInfoMc(List<String> serviceIdList, Boolean isTermination) throws TclCommonException {
		List<NPLSIServiceDetailedResponse> serviceDetailResponse = new ArrayList<>();
		if (serviceIdList == null || serviceIdList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		if(isTermination == null) {
			LOGGER.info("isTermination is null, assuming as false");
			isTermination = Boolean.FALSE;
		}

		try {
			for(String serviceId : serviceIdList) {
				String productName = "NPL";
				NPLSIServiceDetailedResponse response = new NPLSIServiceDetailedResponse();
				List<NPLSISolutionDataOffering> siInfo = new ArrayList<>();
				List<Integer> sysIds = null;
				if(Boolean.TRUE.equals(isTermination)) {
					sysIds = siServiceInfoRepository.findLatestSysIdsForAServiceId(serviceId, productName);
				}
				else {
					sysIds = siServiceInfoRepository.findSysIdsForAServiceId(serviceId, productName);
				}
				final Boolean isTerm = isTermination;
			sysIds.stream().forEach(id -> {

				List<Map<String, Object>> details = Boolean.TRUE.equals(isTerm) ? siServiceInfoRepository.getLatestServiceDetailAttributesForNPL(serviceId,
						id) : siServiceInfoRepository.getServiceDetailAttributesForNPL(serviceId,
						id);
				if (details == null || details.isEmpty()) {
					throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT,
							ResponseResource.R_CODE_ERROR);
				}

				details.stream().forEach(serviceDetail -> {
					NPLSISolutionDataOffering solution = null;
					Map<String, Object> attributes = new HashMap<>();

					Integer sysId = (Integer) serviceDetail.get("sys_id");
					String linkType = (String) serviceDetail.get("pri_sec");
					
					Optional<NPLSISolutionDataOffering> solutions = siInfo.stream()
							.filter(si -> si.getServiceId().equals(sysId)).findFirst();
					if (solutions.isPresent()) {
						solution = solutions.get();
					} else {
						solution = new NPLSISolutionDataOffering();
						siInfo.add(solution);
					}
					
					NPLSolutionAttributes solutionAttributes = setNPLSolutionAttributes(sysId);
					solution.setAttributes(solutionAttributes);
					solution.setServiceId(sysId);
					solution.setLinkType(linkType);
					solution.setSiteType(solutionAttributes.getSiteType());
					solution.setSiteClassification((String) serviceDetail.get("site_classification"));

					List<Map<String, Object>> assetDetailInfos = siServiceInfoRepository
							.getAssetDetailWithAttributes(sysId);
					Optional<Map<String, Object>> assetDetailInfo = assetDetailInfos.stream().findFirst();
					Map<String, Object> assetDetail = new HashMap<>();
					if (assetDetailInfo.isPresent()) {
						assetDetail = assetDetailInfo.get();
					}
					try {
						attributes = getFieldAttributes(serviceDetail, assetDetail, attributes);
						getAttributesForNPL(solution, attributes, details, assetDetailInfos);
					} catch (TclCommonException e) {
						LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
			response.setProductName(productName);
			response.setSiInfo(siInfo);
			serviceDetailResponse.add(response);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		
		return serviceDetailResponse;
	}


	/**
	 * Get GSC Service Abbreviation for O2C
	 *
	 * @param erfCustLeId
	 * @param subVariant
	 * @return
	 */
	public String getGscServiceAbbreviation(String erfCustLeId, String erfSupplierLeId, String subVariant) {
		List<Map<String, String>> gscServiceAbbreviations = siOrderRepository.getGscServiceAbbreviation(erfCustLeId, erfSupplierLeId, subVariant);

		return gscServiceAbbreviations.stream().map(values -> {
			return values.get("service_varient");
		}).collect(Collectors.toList()).get(0);
	}

	public SiServiceDetailBean getSIServiceDetail(String serviceId) throws TclCommonException {

		LOGGER.info("Queue request {}", serviceId);
		if (StringUtils.isBlank(serviceId)) {
			LOGGER.info("null or blank service id is received in request: {}", serviceId);
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}
		List<SIServiceDetail> siServiceDetails = siServiceDetailRepository.findByTpsServiceIdAndCircuitStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(
				serviceId.trim(), RenewalsServiceAttributeConstants.ACTIVE,
				RenewalsServiceAttributeConstants.IS_ACTIVE);
		if (CollectionUtils.isEmpty(siServiceDetails)) {
			LOGGER.info("Empty response from MySQL");
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}
		SIServiceDetail detail = siServiceDetails.get(0);
		SiServiceDetailBean siServiceDetailBean = new SiServiceDetailBean();
		siServiceDetailBean.setSiOrderId(detail.getSiOrder().getId());
		siServiceDetailBean.setSiServiceDetailId(null); // yet to confirm, which attribute should be mapped from siServiceDetails
		siServiceDetailBean.setParentOpportunityId(null); // yet to confirm, which attribute should be mapped from siServiceDetails
		siServiceDetailBean.setPriSec(detail.getPrimarySecondary());
		return siServiceDetailBean;
	}
	/**
	 * Get Gsc Access Types for given SECS ID
	 *
	 * @param secsId
	 * @return
	 */
	public Set<String> getGscAccessTypeDetails(Integer secsId) {
		Set<String> accessTypes = new HashSet<>();
		accessTypes.add("PSTN");

		List<GscInterconnectDetails> gscInterconnectDetails = gscInterconnectDetailsRepository.findByOrgNo(secsId);
		if(!CollectionUtils.isEmpty(gscInterconnectDetails)) {
			accessTypes.add("Dedicated");
		}

		return accessTypes;
	}

	 public List<SIServiceDetailsBean> getOrderDataBasedOnServiceTermination(String[] serviceIds) throws TclCommonException {


	    	final String[] totalBandWidth= {""};
	        List<SIServiceDetailsBean>  serviceDetailsBeanList  =  new ArrayList<>();
	        if(serviceIds == null || serviceIds.length == 0) {
	            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
	        }
	        List<String> serviceIdsList = Arrays.asList(serviceIds);
	       List<SIServiceInfo> serviceInfoList =  siServiceInfoRepository.findServiceIdIn(serviceIdsList);
	       if( serviceInfoList != null && !serviceInfoList.isEmpty()) {
	    	   serviceInfoList.stream().forEach(serviceInfo -> {
	    		   LOGGER.info("Servie Info {}", serviceInfo.getServiceId());
	    		   SIServiceDetailsBean serviceDetailBean = new SIServiceDetailsBean();
	    		   serviceDetailBean.setAccessType(serviceInfo.getAccessType());
	    		   serviceDetailBean.setSiteAddress(serviceInfo.getCustomerSiteAddress());
	    		   serviceDetailBean.setAccessProvider(serviceInfo.getLastMileProvider());
	    		   serviceDetailBean.setArc(serviceInfo.getArc());
	    		   serviceDetailBean.setBillingFrequency(serviceInfo.getBillingFrequency());
	    		   serviceDetailBean.setBillingMethod(serviceInfo.getBillingMethod());
	    		   serviceDetailBean.setContractTerm(serviceInfo.getOrderTermInMonths());
	    		   serviceDetailBean.setErfCustomerId(serviceInfo.getOrderCustomerId());
	    		   serviceDetailBean.setErfCustomerLeId(serviceInfo.getOrderCustLeId());
	    		   serviceDetailBean.setErfCustomerLeName(serviceInfo.getOrderCustLeName());
	    		   serviceDetailBean.setErfCustomerName(serviceInfo.getOrderCustomer());
	    		   serviceDetailBean.setErfPrdCatalogOfferingId(serviceInfo.getProductOfferingId());
	    		   serviceDetailBean.setErfPrdCatalogOfferingName(serviceInfo.getProductOfferingName());
	    		   serviceDetailBean.setErfPrdCatalogProductName(serviceInfo.getProductFamilyName());
	    		   serviceDetailBean.setLastmileBw(serviceInfo.getLastMileBandwidth());
	    		   serviceDetailBean.setLastmileBwUnit(serviceInfo.getLastMileBandwidthUnit());
	    		   serviceDetailBean.setPortBw(serviceInfo.getBandwidth());
	    		   serviceDetailBean.setPortBwUnit(serviceInfo.getBandwidthUnit());
	    		   serviceDetailBean.setLinkType(serviceInfo.getPrimaryOrSecondary());
	    		   serviceDetailBean.setTpsServiceId(serviceInfo.getServiceId());
	    		   serviceDetailBean.setPriSecServLink(serviceInfo.getPrimarySecondaryLink());
	    		   serviceDetailBean.setNrc(serviceInfo.getNrc());
	    		   serviceDetailBean.setReferenceOrderId(serviceInfo.getOrderSysId());
	    		   serviceDetailBean.setOrderCode(serviceInfo.getOrderCode());
	    		   serviceDetailBean.setVpnName(serviceInfo.getVpnName());
	    		   serviceDetailBean.setErfLocSiteAddressId(serviceInfo.getLocationId());
	    		   serviceDetailBean.setLatLong(serviceInfo.getLatLong());
	    		   serviceDetailBean.setId(serviceInfo.getId());
	    		   serviceDetailBean.setErfSpLeId(serviceInfo.getOrderSpLeId());
	    		   serviceDetailBean.setErfSpLeName(serviceInfo.getOrderSpLeName());
	    		   serviceDetailBean.setCustomerCurrencyId(serviceInfo.getCurrencyId());
	    		   serviceDetailBean.setParentOpportunityId(serviceInfo.getOpportunityId());
	    		   serviceDetailBean.setMrc(serviceInfo.getMrc());
	    		   serviceDetailBean.setDemoFlag(Objects.nonNull(serviceInfo.getOrderDemoFlag())?serviceInfo.getOrderDemoFlag():"");
	    		   serviceDetailBean.setDemoType(Objects.nonNull(serviceInfo.getBillingType())?serviceInfo.getBillingType():"");

	    		   LOGGER.info("Demo type and flag for service id ---> {} are --- {}  ---- {}  " , serviceDetailBean.getTpsServiceId()
						   ,serviceDetailBean.getDemoType(),serviceDetailBean.getDemoFlag());

	    		   if(Objects.nonNull(serviceInfo.getSiteType())){
					   serviceDetailBean.setSrvSiteType(serviceInfo.getSiteType());
					   LOGGER.info("Site Type for service id --> {}  is ----> {} ", serviceInfo.getServiceId(),serviceDetailBean.getSrvSiteType());
				   }
	    		   serviceDetailBean.setContractStartDate(serviceInfo.getContractStartDate());
	    		   serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
	    		   serviceDetailBean.setDemarcationApartment(serviceInfo.getDemarcationApartment());
	    		   serviceDetailBean.setDemarcationFloor(serviceInfo.getDemarcationFloor());
	    		   serviceDetailBean.setDemarcationRoom(serviceInfo.getDemarcationRoom());
	    		   serviceDetailBean.setDemarcationRack(serviceInfo.getDemarcationRack());
				   LOGGER.info("Order Partner id for service info id {} is  {} ", serviceInfo.getServiceId(), serviceInfo.getOrderPartner());
				   serviceDetailBean.setPartnerId(serviceInfo.getOrderPartner());
				   serviceDetailBean.setPartnerCuid(serviceInfo.getPartnerCuid());
				   serviceDetailBean.setErfCustPartnerLeId(serviceInfo.getErfCustPartnerLeId());
				   serviceDetailBean.setOpportunityType(serviceInfo.getOpportunityType());
				   serviceDetailBean.setLastMileType(serviceInfo.getLastMileType());
				   serviceDetailBean.setTpsCopfId(serviceInfo.getTpsCopfId());
				   serviceDetailBean.setTpsSfdcCuid(serviceInfo.getSfdcCuId());
				   serviceDetailBean.setContractEndDate(serviceInfo.getContractEndDate());
				   serviceDetailBean.setBurstableBw(serviceInfo.getBurstableBandwidth());
				   serviceDetailBean.setBurstableBwUnit(serviceInfo.getBurstableBandwidthUnit());
				   serviceDetailBean.setCircuitExpiryDate(serviceInfo.getCircuitExpiryDate());
				   serviceDetailBean.setOrderCategory(serviceInfo.getCurrentOpportunityType());
				   serviceDetailBean.setSiteEndInterface(serviceInfo.getSiteEndInterface());

				   serviceDetailBean.setBillingCurrency(serviceInfo.getBillingCurrency());
				   serviceDetailBean.setPortMode(serviceInfo.getSrvPortMode());
				   serviceDetailBean.setsCommisionDate(serviceInfo.getCommissionedDate());
				   serviceDetailBean.setIpv4AddressPoolsize(serviceInfo.getIpv4AddressPoolSize());

				   serviceDetailBean.setSourceCity(serviceInfo.getSourceCity());
				   serviceDetailBean.setDestinationCity(serviceInfo.getDestinationCity());
				   serviceDetailBean.setAccountManager(serviceInfo.getAccountManager());
				   serviceDetailBean.setCurrentOpportunityType(serviceInfo.getCurrentOpportunityType());
				   serviceDetailBean.setUuid(serviceInfo.getUuid());
				   serviceDetailBean.setBillingType(serviceInfo.getBillingType());

				   //added for gvpn multivrf macd update port bw values to total vrf bandwidth value
					if (serviceInfo.getProductFamilyName() != null
							&& serviceInfo.getProductFamilyName().equalsIgnoreCase("GVPN")) {
						Optional<SIServiceDetail> siServiceDetail = siServiceDetailRepository
								.findByTpsServiceIdAndIsActive(serviceInfo.getServiceId(), CommonConstants.Y);
						if (siServiceDetail.isPresent()) {
							if (siServiceDetail.get().getMultiVrfSolution() != null) {
								LOGGER.info("MULTIVRF FLAG" + siServiceDetail.get().getMultiVrfSolution() + "port bw"
										+ serviceDetailBean.getPortBw() + "unit por bw"
										+ serviceDetailBean.getPortBwUnit());
								if (siServiceDetail.get().getMultiVrfSolution().equalsIgnoreCase("Yes")) {
									totalBandWidth[0] = getAttributesValues(siServiceDetail.get(),
											CommonConstants.TOTAL_VRF_BANDWIDTH_MBPS);
									LOGGER.info("total vrf bandwidth" + totalBandWidth[0]);
									serviceDetailBean.setTotalVrfBandwith(totalBandWidth[0]);
									serviceDetailBean.setPortBw(totalBandWidth[0]);
									serviceDetailBean.setPortBwUnit("Mbps");
								}
							}
						}
					}
				   constructAssetAttributes(serviceInfo, serviceDetailBean);
				   constructComponentAttributes(serviceInfo, serviceDetailBean);
				   try {
					   getDemoOrderContractDetails(serviceDetailBean.getAssetAttributes(),serviceInfo);
				   } catch (TclCommonException e) {
					   e.printStackTrace();
				   }
				   serviceDetailsBeanList.add(serviceDetailBean);
	    	   });

	       }
	        return serviceDetailsBeanList;


	 }


	public List<SIServiceInfoBean> getServiceDetailsforNPLTermination(String[] serviceIds) throws TclCommonException {
		List<SIServiceInfoBean> siServiceDetailBeanList = new ArrayList<>();
		if (serviceIds == null || serviceIds.length == 0) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<String> serviceIdsList = Arrays.asList(serviceIds);
		if (serviceIdsList != null && !serviceIdsList.isEmpty()) {
			serviceIdsList.stream().forEach(serviceId -> {

				List<SIServiceInfo> siServiceInfo = siServiceInfoRepository.findServiceIdNPL(serviceId);


				siServiceDetailBeanList.addAll(constructSiServiceInfoBean(siServiceInfo));
			});
		}

		return siServiceDetailBeanList;
	}

	public List<SIServiceInfoBean> getServiceDetailsPrimarySecondaryForInactiveCircuits(String serviceId) throws TclCommonException {

		LOGGER.info("getServiceDetailsPrimarySecondaryForInactiveCircuits - Service Id Recieved from request is ----> {} ", serviceId);
		List<SIServiceInfoBean> siServiceDetailBeanList = new ArrayList<>();

		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		// Optional<SIServiceDetail> optDetail =
		// siServiceDetailRepository.findByTpsServiceId(serviceId);
		Optional<SIServiceInfo> siServiceInfoOpt = siServiceInfoRepository.findServiceIdFromViewInfoAll(serviceId);

		if (!siServiceInfoOpt.isPresent()) {
			throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
		}
		try {
			siServiceDetailBeanList.add(constructSiServiceInfoBean(siServiceInfoOpt.get()));
		} catch (TclCommonException e) {
			LOGGER.error("Error in construct Si Service Info Bean", e);
		}

		//siServiceDetailBeanList.add(constructSiServiceInfoBean(siServiceInfoOpt.get()));

		if (StringUtils.isNotBlank(siServiceInfoOpt.get().getPrimarySecondaryLink())) {
			Optional<SIServiceInfo> relatedServiceBean = siServiceInfoRepository
					.findServiceIdFromViewInfoAll(siServiceInfoOpt.get().getPrimarySecondaryLink());

			if (!relatedServiceBean.isPresent())
				throw new TclCommonException(ExceptionConstants.INVALID_RELATED_SERVICEID,
						ResponseResource.R_CODE_ERROR);

			siServiceDetailBeanList.add(constructSiServiceInfoBean(relatedServiceBean.get()));
		}

		return siServiceDetailBeanList;

	}

	public List<SIPriceRevisionDetailBean> getPriceRevisionDetailForTermination(String[] serviceIds) throws TclCommonException {
		List<SIPriceRevisionDetailBean> siPriceRevisionDetailBeanList = new ArrayList<>();
		if (serviceIds == null || serviceIds.length == 0) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<String> serviceIdsList = Arrays.asList(serviceIds);
		if (serviceIdsList != null && !serviceIdsList.isEmpty()) {
			serviceIdsList.stream().forEach(serviceId -> {

				Optional<ViewPriceRevisionDetail> vwPriceRevisionDetail = viewPriceRevisionDetailRepository.findByServiceId(serviceId);

				if (vwPriceRevisionDetail.isPresent()) {
					siPriceRevisionDetailBeanList.add(constructSiPriceRevisionDetailBean(vwPriceRevisionDetail.get()));
				}
			});
		}

		return siPriceRevisionDetailBeanList;
	}

	private SIPriceRevisionDetailBean constructSiPriceRevisionDetailBean(ViewPriceRevisionDetail vwPriceRevisionDetail) {
		SIPriceRevisionDetailBean siPriceRevisionDetailBean = new SIPriceRevisionDetailBean();

		if(Objects.nonNull(vwPriceRevisionDetail) && Objects.nonNull(vwPriceRevisionDetail.getServiceId())){
			LOGGER.info("Inside method contruct si price revision detail bean for service Id ----> {} ", vwPriceRevisionDetail.getServiceId());
			siPriceRevisionDetailBean.setServiceId(vwPriceRevisionDetail.getServiceId());
			siPriceRevisionDetailBean.setMaxPriceRevDate(vwPriceRevisionDetail.getMaxPriceRevDate());
			siPriceRevisionDetailBean.setEffDateOfPriceRevision(vwPriceRevisionDetail.getEffDateOfPriceRevision());
		}

		return siPriceRevisionDetailBean;
	}

	/**
	 * Method to get inventory count for provided customer / partner based on engagements 
	 * @param customerId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	public List<ProductInformationBean> getAllProductServiceInformationCount(Integer customerId,Integer partnerId)
			throws TclCommonException, IllegalArgumentException {
		List<ProductInformationBean> response = new ArrayList<>();
		List<ProductInformationBean> responseFromEngagement = new ArrayList<>();
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		try {
			String queueResponse = (String) mqUtils.sendAndReceive(productDetailsQueue, null);
			String queueResponseFromAuth=(String)mqUtils.sendAndReceive(getEngagementDetails, null);
			LOGGER.info("queueResponse :"+ queueResponse);
			LOGGER.info("queueResponseFromAuth :"+ queueResponseFromAuth);
			List<Map<String, Object>> productInformationBeans = (List<Map<String, Object>>) Utils
					.convertJsonToObject(queueResponse, List.class);
			List<Map<String, Object>> engagementBeans=new ArrayList<Map<String,Object>>();
			if(Objects.nonNull(queueResponseFromAuth) && !queueResponseFromAuth.isEmpty())
				engagementBeans = (List<Map<String, Object>>) Utils
					.convertJsonToObject(queueResponseFromAuth, List.class);
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				partnerLeIds = new ArrayList<>(partnerId!=null?getPartnerLeIds(partnerId):getPartnerLeIds());
				customerIds = Arrays.asList((Integer) null);
			} else {
				customerIds = new ArrayList<>(customerId!=null?getCustomerLeIdsByCustomerId(customerId):getCustomerLeIds());
				partnerLeIds = Arrays.asList((Integer) null);
			}
			

			final List<Integer> finalPartnerLeIds = partnerLeIds;
			final List<Integer> finalCustomerIds = customerIds;
			LOGGER.info("Customer ID is ----> {} "+ customerIds.toString());
			LOGGER.info("Partner ID is ----> {} "+ partnerLeIds.toString());
			if(Objects.nonNull(engagementBeans) && !engagementBeans.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				engagementBeans.stream().forEach(map->{
					UserProductsBean userProductsBean = mapper.convertValue(map,
							UserProductsBean.class);
					if(ServiceInventoryConstants.MHS.equalsIgnoreCase(userProductsBean.getProductName()) 
							|| ServiceInventoryConstants.MSS.equalsIgnoreCase(userProductsBean.getProductName())) {
						ProductInformationBean productInformationBean =new ProductInformationBean();
						productInformationBean.setProductId(userProductsBean.getProductId());
						productInformationBean.setProductName(userProductsBean.getProductName());
						LOGGER.info("engagementBeans product Name res from queue  ------ {}"+ userProductsBean.getProductName());
						responseFromEngagement.add(productInformationBean);
					}
						
				});
			}
			LOGGER.info("getAllProductServiceInformationcount - responseFromEngagement{} :"+ responseFromEngagement.toString());
			if (engagementBeans != null && (customerIds != null && !customerIds.isEmpty() || partnerLeIds!=null && !partnerLeIds.isEmpty())) {
//				final ObjectMapper mapper = new ObjectMapper();
				engagementBeans.stream().forEach(map -> {
					ProductInformationBean productInformationBean = new ProductInformationBean();
//					productInformationBean.setProductName(map.get("productName")!=null?(String) map.get("productName"):null);
//					productInformationBean.setProductId(map.get("productId")!=null?(Integer) map.get("productId"):null);
					Integer productIdfromEngagement = (map.get("productId")!=null?(Integer) map.get("productId"):null);
					Optional<Map<String, Object>> productInfoBean = productInformationBeans.stream().filter(pb -> pb.get("productId").equals(productIdfromEngagement)).findFirst();
					LOGGER.info("getAllProductServiceInformation engagement and product catalogue matched product {}", productInfoBean.get());
					final ObjectMapper mapper = new ObjectMapper();
				    productInformationBean = mapper.convertValue(productInfoBean.get(), ProductInformationBean.class);

					Integer count = 0;
					LOGGER.info("view Spec start for Customer: {} or Partner: {} ", finalCustomerIds, finalPartnerLeIds);
					LOGGER.info("Initiating productName ==================> {}",productInformationBean.getProductName());
					if("IZO Private Cloud".equals(productInformationBean.getProductName()) || "IPC".equals(productInformationBean.getProductName())){
						//Specification<VwOrderServiceAssetInfo> specOrderTotalCount = vwOrderServiceAssetDetailSpecification.getOrders(null, null, null, null, null, null, null, null, finalCustomerIds, finalPartnerLeIds, productInformationBean.getProductId());
						//List<VwOrderServiceAssetInfo> orderServiceAssetList = vwOrderServiceAssetInfoRepository.findAll(specOrderTotalCount);
						//count = orderServiceAssetList.size();
						LOGGER.info("view Spec start for ipc {}, {}, {}", finalCustomerIds, finalPartnerLeIds, productInformationBean.getProductId());
						count = siServiceDetailRepository.getServiceCountByStandaloneProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						LOGGER.info("Processing productName {} : count {}",productInformationBean.getProductName(),count);
					} else if("IAS".equals(productInformationBean.getProductName()) || "GVPN".equals(productInformationBean.getProductName()) || 
							"IZO Internet WAN".equals(productInformationBean.getProductName()) || "DIA".equals(productInformationBean.getProductName()) ||
							"BYON Internet".equals(productInformationBean.getProductName()) || "BYON MPLS".equals(productInformationBean.getProductName()) || "IZOPC".equals(productInformationBean.getProductName()) || "IZO Private Connect".equals(productInformationBean.getProductName())){
						LOGGER.info("Processing productName {}",productInformationBean.getProductName());
						 count = siServiceDetailRepository.getServiceCountByStandaloneProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						 LOGGER.info("Processing productName {} : count {}",productInformationBean.getProductName(),count);
						 LOGGER.info("view Spec start for ias, gvpn {} ,{},{},{},{}", finalCustomerIds,finalPartnerLeIds,productInformationBean.getProductId(),customerId,partnerId);
						 LOGGER.info("Product Count for GVPN DIA BYON  count {}, -----------------productName {}", count,productInformationBean.getProductName());
					} else if("IZO SDWAN".equals(productInformationBean.getProductName())){
						 count = siServiceDetailRepository.getServiceCountByStandaloneIzoSdwan(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						 LOGGER.info("view Spec start for izosdwan count {}", count);
						 if(count != 0) {
							 Integer networkProductCounts = siServiceDetailRepository.getNetworkServiceCount(Arrays.asList("IAS", "GVPN","IZO Internet WAN","DIA","BYON Internet","BYON MPLS"), finalCustomerIds, finalPartnerLeIds,customerId,partnerId, productInformationBean.getProductId());
							 productInformationBean.setNetworkProductCounts(networkProductCounts);
						 }
						 LOGGER.info("view Spec start for izosdwan 2 {} ,{},{},{},{}", finalCustomerIds, finalPartnerLeIds,customerId,partnerId, productInformationBean.getProductId());
					} else if("GDE".equals(productInformationBean.getProductName()) || "NDE".equals(productInformationBean.getProductName()) ||
							"GSIP".equals(productInformationBean.getProductName()) || "NPL".equals(productInformationBean.getProductName())){
						 //count = siServiceDetailRepository.getServiceCountByProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds);
						 count = siServiceDetailRepository.getServiceCountByProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						 if("GDE".equals(productInformationBean.getProductName())) {
							 count = count/2;
						 }
						 LOGGER.info("view Spec start for gde, nde, gsip, npl {} ,{},{},{},{}", productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
					}else{
						 //count = siServiceDetailRepository.getServiceCountByProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds);
						 count = siServiceDetailRepository.getServiceCountByProduct(productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
						 LOGGER.info("view Spec start for non-network products {} ,{},{},{},{}", productInformationBean.getProductId(), finalCustomerIds, finalPartnerLeIds,customerId,partnerId);
					}

					if (count != null && count > 0) {
						productInformationBean.setCount(count);
						LOGGER.info("Final Product Count  {}, ----####-----> {}", productInformationBean.getCount(), productInformationBean.toString());
						response.add(productInformationBean);
					}
				});
				
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		if(Objects.nonNull(response) && !response.isEmpty()) {
			boolean isMHSPresent = response.stream().anyMatch(bean -> (bean.getProductName().equals(ServiceInventoryConstants.MHS)));
			boolean isMSSPresent = response.stream().anyMatch(bean -> (bean.getProductName().equals(ServiceInventoryConstants.MSS)));
			if(!isMHSPresent && !responseFromEngagement.isEmpty() && Objects.nonNull(responseFromEngagement)) 
				response.addAll(responseFromEngagement.stream().filter(obj->obj.getProductName().equalsIgnoreCase(ServiceInventoryConstants.MHS)).collect(Collectors.toList()));
			if(!isMSSPresent && !responseFromEngagement.isEmpty() && Objects.nonNull(responseFromEngagement)) 
				response.addAll(responseFromEngagement.stream().filter(obj->obj.getProductName().equalsIgnoreCase(ServiceInventoryConstants.MSS)).collect(Collectors.toList()));
		}
		 LOGGER.info("response {}", response);
		return response;
	}

	public List<ServiceDetailedInfoBean> fetchServiceDetailsInfoUtil(String serviceId) throws TclCommonException{
		List<ServiceDetailedInfoBean> serviceDetailedInfos = new ArrayList<>();
		try {
			LOGGER.info("Entering fetchServiceDetailsInfoUtil for service id {} ", serviceId);
			List<ViewSiServiceInfoAll> vwSiServiceInfo = vwSiServiceInfoAllRepository.findByServiceIdInOrderByIdDesc(Arrays.asList(serviceId));
			if(vwSiServiceInfo != null && !vwSiServiceInfo.isEmpty()) {
				ViewSiServiceInfoAll viewSiServiceInfoAll = vwSiServiceInfo.get(0);
				String productName = viewSiServiceInfoAll.getProductFamilyName();
				LOGGER.info("The service id belongs to products {} {} ", serviceId, productName);
				if(productName.equals("IAS") ||productName.equals("GVPN")) {
					List<SIServiceAttribute> serviceAttributes = siServiceAttributeRepository.findBySiServiceDetail_IdInAndAttributeNameIn(
							Arrays.asList(viewSiServiceInfoAll.getId()),
							Arrays.asList("Access Required", "Additional IPs","IPV4_ADDR_POOL_SIZE","IPV6_ADDR_POOL_SIZE","no_additional_ipv4_address",
									"no_additional_ipv6_address","IP Address Arrangement for Additional IPs"));
					LOGGER.info("fetchServiceDetailsInfoUtil serviceAttributes size for serviceid {} {} ",serviceId,serviceAttributes.size());
					SIAsset siAsset = siAssetRepository.findFirstBySiServiceDetail_IdAndTypeIgnoreCaseOrderByIdDesc(viewSiServiceInfoAll.getId(),"CPE");
//					VwServiceAssetInfo assetDetails = vwServiceAssetInfoRepository
//							.getAssetTypeDetails(viewSiServiceInfoAll.getId(),"CPE");
					LOGGER.info("Getting asset CPE for serviceid {} {} ", serviceId, siAsset.getModel());
					
					constructServiceDetailsInfoBean(serviceDetailedInfos, viewSiServiceInfoAll, serviceAttributes,siAsset);
					
				} else if(productName.equals("NPL") || productName.equals("NDE")) {
					Optional<ViewSiServiceInfoAll> serviceDetailSiteA = vwSiServiceInfo.stream().filter(vwsi-> vwsi.getSiteType().equalsIgnoreCase("SiteA")).findFirst();
					Optional<ViewSiServiceInfoAll> serviceDetailSiteB = vwSiServiceInfo.stream().filter(vwsi-> vwsi.getSiteType().equalsIgnoreCase("SiteB")).findFirst();
					LOGGER.info("Entering fetchServiceDetailsInfoUtil link products {} sysIdA {} sysIdB{} ", serviceId, serviceDetailSiteA.get().getId(), serviceDetailSiteB.get().getId());
					List<SIServiceAttribute> serviceAttributesSiteA = siServiceAttributeRepository.findBySiServiceDetail_IdInAndAttributeNameIn(
							Arrays.asList(serviceDetailSiteA.get().getId()), Arrays.asList("Access Required", "Additional IPs","IPV4_ADDR_POOL_SIZE",
									"IPV6_ADDR_POOL_SIZE","no_additional_ipv4_address","no_additional_ipv6_address"));
					List<SIServiceAttribute> serviceAttributesSiteB = siServiceAttributeRepository.findBySiServiceDetail_IdInAndAttributeNameIn(
							Arrays.asList(serviceDetailSiteB.get().getId()), Arrays.asList("Access Required", "Additional IPs","IPV4_ADDR_POOL_SIZE",
									"IPV6_ADDR_POOL_SIZE","no_additional_ipv4_address","no_additional_ipv6_address"));
					LOGGER.info("fetchServiceDetailsInfoUtil serviceAttributes size for serviceid {} siteA attribute {} siteB size {} ",serviceId,
							serviceAttributesSiteA.size(), serviceAttributesSiteB.size());
					constructServiceDetailsInfoBean(serviceDetailedInfos, serviceDetailSiteA.get(), serviceAttributesSiteA,null);
					constructServiceDetailsInfoBean(serviceDetailedInfos, serviceDetailSiteB.get(), serviceAttributesSiteB,null);
				}
				
			} else
				throw new TclCommonException(ExceptionConstants.INVALID_SERVICEID, ResponseResource.R_CODE_ERROR);
			
			
			
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		return serviceDetailedInfos;
		
	}

	private void constructServiceDetailsInfoBean(List<ServiceDetailedInfoBean> serviceDetailedInfos,
			ViewSiServiceInfoAll viewSiServiceInfoAll, List<SIServiceAttribute> serviceAttributes,
			SIAsset assetDetails) throws TclCommonException {
		try {
		LOGGER.info("constructServiceDetailsInfoBean srvid {} product {} ", viewSiServiceInfoAll.getServiceId(), viewSiServiceInfoAll.getProductFamilyName());
//		final ObjectMapper mapper = new ObjectMapper();
		ServiceDetailedInfoBean serviceDetailedInfo = new ServiceDetailedInfoBean();
//		ServiceDetailedInfoBean serviceDetailedInfo = mapper.convertValue(viewSiServiceInfoAll,
//	    		ServiceDetailedInfoBean.class);
		serviceDetailedInfo.setId(viewSiServiceInfoAll.getId());	
		serviceDetailedInfo.setServiceId(viewSiServiceInfoAll.getServiceId());
		serviceDetailedInfo.setProductFamilyName(viewSiServiceInfoAll.getProductFamilyName());
		serviceDetailedInfo.setProductFamilyId(viewSiServiceInfoAll.getProductFamilyId());
		serviceDetailedInfo.setProductOfferingName(viewSiServiceInfoAll.getProductOfferingName());
		serviceDetailedInfo.setProductOfferingId(viewSiServiceInfoAll.getProductOfferingId());
		serviceDetailedInfo.setPrimaryOrSecondary(viewSiServiceInfoAll.getPrimaryOrSecondary());
		serviceDetailedInfo.setPrimarySecondaryLink(viewSiServiceInfoAll.getPrimarySecondaryLink());
		serviceDetailedInfo.setIsActive(viewSiServiceInfoAll.getIsActive());
		serviceDetailedInfo.setServiceStatus(viewSiServiceInfoAll.getServiceStatus());
		serviceDetailedInfo.setSourceCity(viewSiServiceInfoAll.getSourceCity());
		serviceDetailedInfo.setSourceCountry(viewSiServiceInfoAll.getSourceCountry());
		serviceDetailedInfo.setSiteAddress(viewSiServiceInfoAll.getCustomerSiteAddress());
		serviceDetailedInfo.setLocationId(viewSiServiceInfoAll.getLocationId());
		serviceDetailedInfo.setLatLong(viewSiServiceInfoAll.getLatLong());
		serviceDetailedInfo.setServiceClassification(viewSiServiceInfoAll.getServiceClassification());
		serviceDetailedInfo.setServiceManagementOption(viewSiServiceInfoAll.getServiceManagementOption());
		serviceDetailedInfo.setAccessType(viewSiServiceInfoAll.getAccessType());
		serviceDetailedInfo.setLastMileProvider(viewSiServiceInfoAll.getLastMileProvider());
		serviceDetailedInfo.setBandwidth(viewSiServiceInfoAll.getBandwidth());
		serviceDetailedInfo.setLastMileBandwidth(viewSiServiceInfoAll.getLastMileBandwidth());
		serviceDetailedInfo.setBandwidthUnit(viewSiServiceInfoAll.getBandwidthUnit());
		serviceDetailedInfo.setLastMileBandwidthUnit(viewSiServiceInfoAll.getLastMileBandwidthUnit());
		serviceDetailedInfo.setLastMileType(viewSiServiceInfoAll.getLastMileType());
		serviceDetailedInfo.setSiteEndInterface(viewSiServiceInfoAll.getSiteEndInterface());
		serviceDetailedInfo.setOpportunityType(viewSiServiceInfoAll.getOpportunityType());
		serviceDetailedInfo.setOrderSysId(viewSiServiceInfoAll.getOrderSysId());
		serviceDetailedInfo.setOrderCode(viewSiServiceInfoAll.getOrderCode());
		serviceDetailedInfo.setOrderCustomerId(viewSiServiceInfoAll.getOrderCustomerId());
		serviceDetailedInfo.setOrderCustomer(viewSiServiceInfoAll.getOrderCustomer());
		serviceDetailedInfo.setOrderCustLeName(viewSiServiceInfoAll.getOrderCustLeName());
		serviceDetailedInfo.setOrderCustLeId(viewSiServiceInfoAll.getOrderCustLeId());
		serviceDetailedInfo.setAccountManager(viewSiServiceInfoAll.getAccountManager());
		serviceDetailedInfo.setAccountManagerEmail(viewSiServiceInfoAll.getAccountManagerEmail());
		serviceDetailedInfo.setPartnerName(viewSiServiceInfoAll.getOrderPartnerName());
		serviceDetailedInfo.setOrderPartner(viewSiServiceInfoAll.getOrderPartner());
		serviceDetailedInfo.setErfCustPartnerLeId(viewSiServiceInfoAll.getErfCustPartnerLeId());
		serviceDetailedInfo.setSfdcCuId(viewSiServiceInfoAll.getSfdcCuId());
		serviceDetailedInfo.setPartnerCuid(viewSiServiceInfoAll.getPartnerCuid());
		serviceDetailedInfo.setOpportunityId(viewSiServiceInfoAll.getOpportunityId());
		serviceDetailedInfo.setOrderTermInMonths(viewSiServiceInfoAll.getOrderTermInMonths());
		serviceDetailedInfo.setBillingFrequency(viewSiServiceInfoAll.getBillingFrequency());
		serviceDetailedInfo.setServiceVarient(viewSiServiceInfoAll.getServiceVarient());
		serviceDetailedInfo.setResiliencyInd(viewSiServiceInfoAll.getResiliencyInd());
		serviceDetailedInfo.setServiceType(viewSiServiceInfoAll.getServiceType());
		serviceDetailedInfo.setBillingCurrency(viewSiServiceInfoAll.getBillingCurrency()); 
		serviceDetailedInfo.setPaymentCurrency(viewSiServiceInfoAll.getPaymentCurrency()) ;
		serviceDetailedInfo.setOpportunityId(viewSiServiceInfoAll.getOpportunityId());
		serviceDetailedInfo.setCircuitExpiryDate(viewSiServiceInfoAll.getCircuitExpiryDate());
		serviceDetailedInfo.setIzoSdwanSrvcId(viewSiServiceInfoAll.getIzoSdwanSrvcId());
		serviceDetailedInfo.setContractStartDate(viewSiServiceInfoAll.getContractStartDate());
		serviceDetailedInfo.setRemarks(viewSiServiceInfoAll.getRemarks());
		serviceDetailedInfo.setAdditionalIpArrangement(viewSiServiceInfoAll.getIpAddressArrangement());
		LOGGER.info("initialized ServiceDetailedInfoBean setContractStartDate");
		persistServiceAttributes(serviceAttributes, serviceDetailedInfo);
		if(assetDetails != null) {
			LOGGER.info("assetDetails {} model {}", assetDetails.toString(), assetDetails.getModel());
			serviceDetailedInfo.setCpeModel(assetDetails.getModel());
			serviceDetailedInfo.setAssetAttrId(assetDetails.getId());
		}
		serviceDetailedInfos.add(serviceDetailedInfo);
		} catch(Exception e) {
			LOGGER.error("Error occured while constructServiceDetailsInfoBean response {} ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		
		}
	}

	private void persistServiceAttributes(List<SIServiceAttribute> serviceAttributes,
			ServiceDetailedInfoBean serviceDetailedInfo) {
		serviceAttributes.stream().forEach(servAttr->{
			LOGGER.info("service attributesname {} value {} ", servAttr.getAttributeName(), servAttr.getAttributeValue());
			if(servAttr.getAttributeName().equalsIgnoreCase("Access Required")) {
				serviceDetailedInfo.setAccessRequired(servAttr.getAttributeValue());
			} else if(servAttr.getAttributeName().equalsIgnoreCase("Additional IPs")) {
				serviceDetailedInfo.setAdditionalIps(servAttr.getAttributeValue());
			} else if(servAttr.getAttributeName().equalsIgnoreCase("IPV4_ADDR_POOL_SIZE")) {
				serviceDetailedInfo.setAdditionalIpv4PoolSize(servAttr.getAttributeValue());
			} else if(servAttr.getAttributeName().equalsIgnoreCase("IPV6_ADDR_POOL_SIZE")) {
				serviceDetailedInfo.setAdditionalIpv6PoolSize(servAttr.getAttributeValue());
			} else if(servAttr.getAttributeName().equalsIgnoreCase("no_additional_ipv4_address")) {
				serviceDetailedInfo.setadditionalIpv4Count(servAttr.getAttributeValue());
			} else if(servAttr.getAttributeName().equalsIgnoreCase("no_additional_ipv6_address")) {
				serviceDetailedInfo.setadditionalIpv4Count(servAttr.getAttributeValue());
			}
		});
	}


}
