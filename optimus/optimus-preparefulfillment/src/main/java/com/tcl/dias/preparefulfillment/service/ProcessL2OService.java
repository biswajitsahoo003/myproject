package com.tcl.dias.preparefulfillment.service;

import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.UIFN;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.CITY;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.CITY_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.CONTRACT_START_DATE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.COUNTRY;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.COUNTRY_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_BUILDING;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_BUILDING_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_SITE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_SITE_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_PORT_CHANGED;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LM_CONNECTION_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LM_CONNECTION_TYPE_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LM_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LM_TYPE_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LOCAL_IT_CONTACT_EMAIL;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LOCAL_IT_CONTACT_EMAIL_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LOCAL_IT_CONTACT_MOBILE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LOCAL_IT_CONTACT_MOBILE_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LOCAL_IT_CONTACT_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.LOCAL_IT_CONTACT_NAME_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.OFFERING_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CATEGORY;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CREATED_DATE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.PRODUCT_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SITE_ADDRESS;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SITE_ADDRESS_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.STATE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.STATE_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_DELETE_VM;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.OPTIMUS_MACD;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.TEMP_OUTPULSE_PRESENT;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SIP_SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.ITFS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.LNS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.ACANS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.ACDTFS;
import static com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants.DR_SITE;
import static com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants.SC_COMPONENT_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants.SITE_TYPE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.tcl.dias.common.constants.*;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.*;
import com.tcl.dias.servicefulfillmentutils.constants.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.flowable.engine.RuntimeService;
import org.hibernate.engine.transaction.spi.IsolationDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.MuxDetailsItem;
import com.tcl.dias.common.beans.ResponseResource;

import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;

import com.tcl.dias.common.servicefulfillment.beans.OrderInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ServiceFulfillmentRequest;
import com.tcl.dias.common.servicefulfillment.beans.ServiceInfoBean;
import com.tcl.dias.servicefulfillment.entity.entities.CityTierMapping;
import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;
import com.tcl.dias.servicefulfillment.entity.entities.MstMuxBomTier1;
import com.tcl.dias.servicefulfillment.entity.entities.MstStateToDistributionCenterMapping;

import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.PmAssignment;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttributes;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScTeamsDRServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.VwBomMaterialDetail;
import com.tcl.dias.servicefulfillment.entity.entities.VwMuxBomMaterialDetail;
import com.tcl.dias.servicefulfillment.entity.repository.CityTierMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;

import com.tcl.dias.servicefulfillment.entity.entities.ServiceLogs;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskRemark;

import com.tcl.dias.servicefulfillment.entity.repository.MstMuxBomTier1Repository;
import com.tcl.dias.servicefulfillment.entity.repository.MstStateToDistributionCenterMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillment.entity.repository.PmAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScTeamsDRServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScWebexServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceLogsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRemarkRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VwBomMaterialDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VwBomMuxDetailsRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.ResourceReInitiatedBean;

import com.tcl.dias.servicefulfillmentutils.beans.TerminateBackhaulPo;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationNotEligibleNoticationBean;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpBean;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpResponseBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
//import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IPCServiceFulfillmentConstant;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.MuxSelectService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.*;

/**
 * @author vivek
 * 
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to start process
 */
@Service
public class ProcessL2OService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessL2OService.class);

	@Autowired
	TaskService taskService;
	
	@Autowired
	RuntimeService runtimeService;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	PmAssignmentRepository pmAssignmentRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScProductDetailRepository scProductDetailRepository;
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Value("${reminder.cycle:R60/PT24H}")
	String reminderCycle;
	
	@Autowired
	MstStateToDistributionCenterMappingRepository mstStateToDistributionCenterMappingRepository;
	 
	@Autowired
	NotificationService notificationService;
	
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Value("${customer.support.email}")
	String customerSupportEmail;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;
	
	@Value("${application.env:DEV}")
	String appEnv;
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${queue.migration}")
	String servicemigration_queue;
	
	@Value("${queue.migration.fti}")
	String servicemigration_queue_fti;	

	@Autowired
	ScWebexServiceCommercialRepository  scWebexServiceCommercialRepository;

	@Autowired
	MuxSelectService muxSelectService;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	VwBomMaterialDetailRepository vwBomMaterialDetailRepository;

	@Autowired
	VwBomMuxDetailsRepository vwBomMuxDetailsRepository;

	@Autowired
	MstMuxBomTier1Repository mstMuxBomTier1Repository;


	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	CityTierMappingRepository cityTierMappingRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	DownTimeDetailsRepository downTimeDetailsRepository;

	@Autowired
	ServiceCatalogueService serviceCatalogueService;
	
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	protected ServiceStatusDetailsRepository serviceStatusDetailsRepository;	

	@Autowired
	protected UserInfoUtils userInfoUtils;
	
	@Autowired
	TaskCacheService taskCacheService;
	
	@Autowired
	TaskRemarkRepository taskRemarkRepository;
	
	@Autowired
	private ServiceLogsRepository serviceLogsRepository;
	
	@Value("${termination.update.onnetWirelessDetails.queue}")
	private String terminationUpdateOnnetWirelessDetailsQueue;
	
	@Value("${termination.get.p2pDetails.queue}")
	private String terminationGetP2PDetailsQueue;
	
	@Value("${queue.persist.migrationData}")
	String persistmigrationDataQueue;
	
	@Autowired
	CmmnHelperService cmmnHelperService;
	
	@Autowired
	ProcessTaskLogRepository processTaskLogRepository;
	
	@Autowired
	ScTeamsDRServiceCommercialRepository scTeamsDRServiceCommercialRepository;
	
	@Transactional(readOnly=false)
	public Boolean processL2ODataToFlowable(Integer serviceId,ScServiceDetail scServiceDetail, boolean welcomeMailTrigger) {
		Boolean status = true;
		boolean isP2PwithoutBH = false;
		boolean isP2PwithBH = false;
		String lmType="";
		String lastMileType="";
		Boolean isValidLM  = false;
		Boolean skipOffnet = false;
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}
			LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",serviceId, scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			String orderSubCategory=scServiceDetail.getOrderSubCategory();
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

			orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
			orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = null;
			if(scContractInfos != null) {
				scContractInfo = scContractInfos.stream().findFirst().orElse(null);
			}

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ",scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",scServiceAttribute.getId(),scServiceAttribute.getAttributeName(),scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}
			
			String oldEquipmentModel=null;
			if("MACD".equalsIgnoreCase(orderType) && !"ADD_SITE".equals(orderCategory)
					&& orderSubCategory!=null && !orderSubCategory.toLowerCase().contains("parallel") && scServiceDetail.getServiceLinkId()!=null){
				LOGGER.info("MACD:: Equipment Model Attr retrival for parent Id::{}",scServiceDetail.getServiceLinkId());
				ScServiceAttribute eqpModelAttr=scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(scServiceDetail.getServiceLinkId(), "EQUIPMENT_MODEL");
				if(eqpModelAttr!=null && eqpModelAttr.getAttributeValue()!=null && !eqpModelAttr.getAttributeValue().isEmpty()){
					LOGGER.info("Equipment Model Attr exists with value::{}",eqpModelAttr.getAttributeValue());
					oldEquipmentModel=eqpModelAttr.getAttributeValue();
				}
			}

			Map<String, Object> processVar = new HashMap<>();

			if (scOrder.getErfCustCustomerName() != null) {
				processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
			}

			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
				if(scContractInfo != null) {
					processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				}
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				processVar.put("parentServiceCode", null);
				processVar.put("parentLmType", null);
				processVar.put("txResourcePathType", null);
				processVar.put("isParallelExists", "false");
				processVar.put("isAmendedOrder", false);

				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())){
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				}else{
					processVar.put("orderSubCategory", "NA");
				}
				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory()) && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){
					LOGGER.info("ProcessL2O:ParallelExists");
					processVar.put("isParallelExists", "true");
					if(Objects.nonNull(scServiceDetail.getParentUuid())){
						processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
					}
					/*ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(), "downtime_duration",
									"IAS Common");
					if(Objects.nonNull(scServiceDownTimeAttr) && Objects.nonNull(scServiceDownTimeAttr.getAttributeValue()) && !scServiceDownTimeAttr.getAttributeValue().isEmpty()){
						LOGGER.info("ProcessL2O:ParallelDownTime");
						processVar.put("parallelDownTime", "PT"+scServiceDownTimeAttr.getAttributeValue()+"D");
					}else{
						LOGGER.info("ProcessL2O:ParallelDownTime is 0");
						processVar.put("parallelDownTime", "PT0M");
					}*/
				}
			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

			if (scServiceDetail != null) {
				
				//GVPN:UPDATE SITE TYPE BASED ON VPN TOPOLOGY
				if("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())){
					LOGGER.info("update vpn details");
					ScServiceAttribute siteTypeServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"Site Type","GVPN Common");
					if(Objects.nonNull(siteTypeServiceAttribute)){
						ScServiceAttribute gvpnCommonServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"VPN Topology","GVPN Common");
						if(Objects.nonNull(gvpnCommonServiceAttribute)){
							LOGGER.info("VPN common");
							updateVpnDetail(gvpnCommonServiceAttribute,siteTypeServiceAttribute);
						}
						ScServiceAttribute sitePropServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"VPN Topology","SITE_PROPERTIES");
						if(Objects.nonNull(sitePropServiceAttribute)){
							LOGGER.info("VPN properties");
							updateVpnDetail(gvpnCommonServiceAttribute,siteTypeServiceAttribute);
						}
					}
				}
				

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				lastMileType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"));

				String connectedBuilding = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_Building_tag"));
				String connectedCustomer = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_cust_tag"));
				String connectedCustomerCategory = StringUtils.trimToEmpty(feasibilityAttributes.get("Orch_Category"));
				
				String btsDeviceType = StringUtils.trimToEmpty(feasibilityAttributes.get("bts_device_type")).toLowerCase();
				
				String solutionType = StringUtils.trimToEmpty(feasibilityAttributes.get("solution_type")).toLowerCase();
				String cpeChassisChanged = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_chassis_changed"));
				String cpeVariant = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_variant"));
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));
				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));
				
				String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
				String upgradeType = StringUtils.trimToEmpty(feasibilityAttributes.get("upgrade_type"));
								
				Double portBW = 0.0;
				
				String bwPortspeed	=scComponentAttributesAMap.get("portBandwidth");
				if (bwPortspeed != null
						&& NumberUtils.isCreatable(bwPortspeed)) {
					portBW = Double.valueOf(bwPortspeed);
				}
				LOGGER.info("Entering CPE maneged copying data {} {} {}  :",scOrder.getOrderType(),cpeChassisChanged,cpeManagementType);
				if("MACD".equalsIgnoreCase(scOrder.getOrderType()) && cpeChassisChanged!=null && "Fully Managed".equalsIgnoreCase(cpeManagementType) && (!cpeChassisChanged.equalsIgnoreCase("Y") || !cpeChassisChanged.equalsIgnoreCase("Yes"))){
					LOGGER.info("Inside CPE related attribute update for MACD order");
					if(scServiceDetail.getOrderSubCategory()!=null && scServiceDetail.getOrderSubCategory().equalsIgnoreCase("Parallel Shifting")){
						LOGGER.info("Inside CPE related attribute update for MACD order for Paralle Shifting");
						compareAndUpdateMacdOrderAttributes(scServiceDetail.getParentUuid(),false,scServiceDetail.getIsAmended());
					}else{
						LOGGER.info("Inside CPE related attribute update for MACD order for other sub category");
						compareAndUpdateMacdOrderAttributes(scServiceDetail.getUuid(),false,scServiceDetail.getIsAmended());
					}

				}

				LOGGER.info("cpeType for serviceid :{} and service code:{} and map:: {} cpeChassisChanged::{} cpeVariant::{} lastMileType::{}", serviceId,scServiceDetail.getUuid(),serviceDetailsAttributes,cpeChassisChanged,cpeVariant,lastMileType);


				String cpeType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE"));
				String cpeModel = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE Basic Chassis"));
				
				String serviceType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Service type"));
				
				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{} serviceType:{}", serviceId,scServiceDetail.getUuid(),cpeType,cpeModel,serviceType);
				
				if(StringUtils.isNotBlank(serviceType) && serviceType.toLowerCase().contains("usage")) {
					processVar.put("burstableOrder", true);
				}
				
				String cpeSiScope = "Customer provided";
				if (cpeType.contains("Rental")) {
					cpeType ="Rental";
					processVar.put("cpeType",cpeType );
				} else if (cpeType.contains("Outright")) {
					cpeType ="Outright";
					processVar.put("cpeType", cpeType);
				} else {
					processVar.put("cpeType", cpeType);
				}

				boolean isLMRequired = true;
				boolean isColoRequired = false;
				boolean negotiationRequiredForOffnetLM = false;
				boolean rowRequired = true;
				boolean prowRequired = true;
				boolean isRFRequired = false;
				

				String bhConnectivity = StringUtils.trimToEmpty(feasibilityAttributes.get("BHConnectivity"));				
				String providerName = StringUtils.trimToEmpty(feasibilityAttributes.get("closest_provider_bso_name"));
				
				if(StringUtils.isBlank(providerName))providerName = StringUtils.trimToEmpty(serviceDetailsAttributes.get("closest_provider_bso_name"));
				
				processVar.put(IS_CONNECTED_SITE, false);
				processVar.put("checkCLRSuccess", false);
				processVar.put(IS_CONNECTED_BUILDING, false);
				processVar.put("isP2PwithoutBH", isP2PwithoutBH);
				processVar.put("isP2PwithBH", isP2PwithBH);
				processVar.put("parallelDownTime", "PT30M");
				processVar.put("isAccountRequired",false);

				
				if(lastMileType.toLowerCase().contains("onnet wireless"))lastMileType="OnnetRF";
				else if(lastMileType.toLowerCase().contains("offnet wireless"))lastMileType="OffnetRF";
				else if(lastMileType.toLowerCase().contains("offnet wireline"))lastMileType="OffnetWL";
				else if(lastMileType.toLowerCase().contains("onnet wireline"))lastMileType="OnnetWL";
				else if(lastMileType.toLowerCase().contains("man") || lastMileType.toLowerCase().contains("wan aggregation"))lastMileType="OnnetWL";

				//if(StringUtils.isBlank(lastMileType))lastMileType="OnnetWL";
				processVar.put("txRequired", false);
				lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileType.toLowerCase().contains("onnetrf") || lastMileType.toLowerCase().equalsIgnoreCase("Onnet Wireless")) {
					LOGGER.info("onnetrf case for Service ID:: {}", scServiceDetail.getUuid());
                    lmScenarioType = "Onnet Wireless";
                    lmConnectionType = "Wireless";
                    lmType = "onnet";
                    processVar.put("sameDayInstallation", false);
                    processVar.put("rfSiteFeasible", true);
                    processVar.put("mastApprovalRequired", true);
                    isValidLM = true;

					if(Objects.nonNull(solutionType) && !solutionType.isEmpty() && "ubrp2pmp".equals(solutionType))
					{
						solutionType = "Radwin from TCL POP";
						LOGGER.info("radwin/ubrp2pmp");
						isP2PwithoutBH = true;
						isColoRequired = true;
						Map<String, String> attrMap= new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
					}

					if(StringUtils.isBlank(providerName) || Objects.isNull(providerName))
					{
						String lastMileProvider = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastMileProvider"));
						providerName= StringUtils.trimToEmpty(lastMileProvider);
						LOGGER.info("providerName set from lastMileProvider as {} for Service ID: {}", lastMileProvider, scServiceDetail.getUuid());
					}

                    if(providerName.toLowerCase().contains("tcl") && providerName.toLowerCase().contains("radwin") && !providerName.toLowerCase().contains("pmp")) {
                    	LOGGER.info("tcl/radwin/not pmp");
                    	isP2PwithoutBH = true;
                    	isColoRequired = true;
                    }else if(providerName.toLowerCase().contains("backhaul") && providerName.toLowerCase().contains("radwin") && !providerName.toLowerCase().contains("pmp")) {
                    	LOGGER.info("backhaul/radwin/not pmp");
                    	isP2PwithBH = true;
                        isColoRequired = true;
                     }else if(providerName.toLowerCase().contains("radwin") && !providerName.toLowerCase().contains("pmp")) {
                    	LOGGER.info("radwin/not pmp");
                     	isP2PwithoutBH = true;
                     	isColoRequired = true;
                     } else if(providerName.toLowerCase().contains("p2p") && !providerName.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p/not pmp");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap= new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
						isP2PwithoutBH = true;
						isColoRequired = true;
                     } else if(StringUtils.isBlank(providerName) && !StringUtils.isBlank(solutionType) && solutionType.toLowerCase().contains("p2p") && !solutionType.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p from solution type/not pmp/provider is blank");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap = new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
						isP2PwithoutBH = true;
						isColoRequired = true;
					} else if(StringUtils.isBlank(providerName) && StringUtils.isBlank(solutionType)){
						isValidLM = false;
						LOGGER.info("service code::{} Not a valid LM::Provider Name & Solution Type is blank",scServiceDetail.getUuid());
                     }
                    
                    if((providerName!=null && providerName.toLowerCase().contains("p2p")) || 
                    		(solutionType!=null && solutionType.toLowerCase().contains("p2p"))) {
                    	Map<String, String> attrMap= new HashMap<>();
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
                    }
                    
                    processVar.put("isP2PwithoutBH", isP2PwithoutBH);
    				processVar.put("isP2PwithBH", isP2PwithBH);
    				
    				if(StringUtils.isBlank(bhConnectivity)) {
    					if(providerName.equalsIgnoreCase("Radwin from TCL POP")) {
    						bhConnectivity="Radwin from TCL POP";
    					}
    				}
                    
    				if(isP2PwithBH) {
	                    if(StringUtils.isNotBlank(bhConnectivity) && !(bhConnectivity.equalsIgnoreCase("NA") || bhConnectivity.equalsIgnoreCase("ONNET") || bhConnectivity.contains("TCL"))) {
	                           processVar.put("txRequired", true);
	                    }else{
	                           processVar.put("txRequired", false);
	                    }
    				}else {
    					 processVar.put("txRequired", false);
    				}
                    processVar.put("skipOffnet", false);
				} else if (lastMileType.toLowerCase().contains("offnetrf") || lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireless")) {
					LOGGER.info("offnetrf");
                    lmScenarioType = "Offnet Wireless";
                    lmConnectionType = "Wireless";
                    lmType = "offnet";
                    isValidLM = true;
                    processVar.put("sameDayInstallation", false);
                    processVar.put("rfSiteFeasible", true);
                    processVar.put("mastApprovalRequired", true);
                    processVar.put("txRequired", false);
                    String supplierOldlocalLoopBw="0.0";
                    if(feasibilityAttributes.containsKey("old_Ll_Bw")){
                    	supplierOldlocalLoopBw=feasibilityAttributes.get("old_Ll_Bw");
                    }
                    if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
							|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
							Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
							&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
                    				
							|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
							Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
							&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {
                    	
                    	if(StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
                    		LOGGER.info("SkipOffnet False");
                        	processVar.put("skipOffnet", false);
                    	}else {
                    		LOGGER.info("SkipOffnet True");                    	
	                    	processVar.put("skipOffnet", true);
	                    	skipOffnet = true;                    		
                    	}
                    }else{
                    	LOGGER.info("SkipOffnet False");
                    	processVar.put("skipOffnet", false);
                    }                    
                    
                    LOGGER.info("service code::{} supplierLocalLoopBw::{} supplierOldlocalLoopBw::{},skipOffnet::{}",scServiceDetail.getUuid(),supplierLocalLoopBw,supplierOldlocalLoopBw,skipOffnet);
				}else if (lastMileType.toLowerCase().contains("offnetwl") || lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireline")) {
					LOGGER.info("offnetwl");
					lmScenarioType = "Offnet Wireline";
                    isValidLM = true;
				
					lmConnectionType = "Wireline";
					lmType = "offnet";
					processVar.put("isMuxIPAvailable", false);
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					processVar.put("skipOffnet", false);
					 String supplierOldlocalLoopBw="0.0";
	                    if(feasibilityAttributes.containsKey("old_Ll_Bw")){
	                    	supplierOldlocalLoopBw=feasibilityAttributes.get("old_Ll_Bw");
	                    }
	                  
	                    if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
	                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
								|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
	                    				
								|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {
	                    	
	                    	if(StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
	                    		LOGGER.info("SkipOffnet False");
	                        	processVar.put("skipOffnet", false);
	                    	}else {
	                    		LOGGER.info("SkipOffnet True");                    	
		                    	processVar.put("skipOffnet", true);
		                    	skipOffnet = true;                    		
	                    	}
	                    }else{
	                    	LOGGER.info("SkipOffnet False");
	                    	processVar.put("skipOffnet", false);
	                    }
	                    
	                    LOGGER.info("service code::{} supplierLocalLoopBw::{} supplierOldlocalLoopBw::{},skipOffnet::{}",scServiceDetail.getUuid(),supplierLocalLoopBw,supplierOldlocalLoopBw,skipOffnet);
				} else if (lastMileType.toLowerCase().contains("onnetwl") || lastMileType.toLowerCase().equalsIgnoreCase("onnet wireline")) {
					LOGGER.info("onnetwl");
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileType="OnnetWL";
					processVar.put("skipOffnet", false);
					isValidLM =true;
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					if(connectedCustomer.contains("1") || connectedCustomerCategory.equalsIgnoreCase("Connected Customer") || (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
                                    && StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Hot Upgrade")
                                    &&  StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Hot Downgrade"))
									|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))) {
						LOGGER.info("ConnectedSite");
						connectedCustomer="1";
						processVar.put(IS_CONNECTED_SITE, true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put("isMuxIPAvailable", false);
						rowRequired = false;
						prowRequired = false;
						lmScenarioType = "Onnet Wireline - Connected Customer";
					} else if (connectedBuilding.contains("1")) {
						LOGGER.info("ConnectedBuilding");
						rowRequired = false;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put("isNetworkSelectedPerBOP", true);
						processVar.put(IS_CONNECTED_BUILDING, true);
						processVar.put(IS_CONNECTED_SITE, false);
						lmScenarioType = "Onnet Wireline - Connected Building";
					} else {
						LOGGER.info("Near Connect");
						rowRequired = true;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put("isNetworkSelectedPerBOP", true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put(IS_CONNECTED_SITE, false);
						lmScenarioType = "Onnet Wireline - Near Connect";
					}
				}
                if(scComponentAttributesAMap.get("destinationCity")!=null && !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
                    processVar.put("tier",setTierValue(scComponentAttributesAMap.get("destinationCity")));
				}
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);
				processVar.put("bhConnectivity", bhConnectivity);
				
				Map<String, String> atMap = new HashMap<>();
				
				if (lastMileType.toLowerCase().contains("offnet")) {
					LOGGER.info("getDestinationState:{} and getSourceState:{}", scComponentAttributesAMap.get("destinationState"),
							scComponentAttributesAMap.get("sourceState"));

					if (scComponentAttributesAMap.get("destinationState") != null && scComponentAttributesAMap.get("sourceState") != null
							&& !scComponentAttributesAMap.get("destinationState")
									.equalsIgnoreCase(scComponentAttributesAMap.get("sourceState"))) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);
					}
					
					if(StringUtils.isNotBlank(supplierLocalLoopBw) && !supplierLocalLoopBw.equals(localLoopBW)) {
						LOGGER.info("supplierLocalLoopBw-and-localLoopBandwidth-not-same-updating-supplierLocalLoopBw= {} and {}", supplierLocalLoopBw,localLoopBW);
						atMap.put("localLoopBandwidth",supplierLocalLoopBw);
					}
					Double supplierLocalLoopBwDouble = 0.0;
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}
				
					Double offnetWlNrc = 0.0;							
					if(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")!=null)  offnetWlNrc= Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf")!=null) offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and  Bandwidth is {} ", offnetWlNrc,offnetRFNrc, supplierLocalLoopBwDouble);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000 )|| supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true");
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}

				LOGGER.info(
						"Input processL2ODataToFlowable received for lmScenarioType::{}  cpeManagementType::{} cpeType::{} isTaxAvailable::{} btsDeviceType::{} serviceId:: {} service code:: {}",
						lmScenarioType, cpeManagementType, cpeType, isTaxAvailable, btsDeviceType,serviceId, scServiceDetail.getUuid());
				
				String offeringName = StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName());
				
				/*if(StringUtils.isBlank(cpeModel)) {
					LOGGER.info("cpeModel is blank setting cpeManagementType to Unmanaged but actual cpeManagementType is {} serviceCode={} orderCode={}",cpeManagementType,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid());
					cpeManagementType="Unmanaged";
				}
				if(StringUtils.trimToEmpty(scOrder.getOrderType()).equalsIgnoreCase("MACD")
						&& (StringUtils.trimToEmpty(scOrder.getOrderCategory()).equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| StringUtils.trimToEmpty(scOrder.getOrderCategory()).equalsIgnoreCase("ADD_IP"))) {
					if("Yes".equalsIgnoreCase(cpeChassisChanged) && StringUtils.isNotBlank(cpeVariant) && !cpeVariant.equalsIgnoreCase("None") && !cpeVariant.equalsIgnoreCase("null") && !cpeVariant.equalsIgnoreCase("NA")) {
						LOGGER.info("MACD cpeModelchange cpeVariant={} cpeChassisChanged={} serviceCode={} orderCode={} cpeManagementType={}",cpeVariant,cpeChassisChanged,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid(),cpeManagementType);
					}else {
						LOGGER.info("MACD cpeManagementType={} cpeChassisChanged={} cpeVariant={} serviceCode={} orderCode={}",cpeManagementType,cpeChassisChanged,cpeVariant,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid());
						
						cpeManagementType="Unmanaged";
					}
				}*/

				if ("Fully Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation, Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Unmanaged".equalsIgnoreCase(cpeManagementType) || offeringName.toLowerCase().contains("unmanaged")) {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Physically Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Configuration Management".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", true);
				} else if ("Proactive Services".equalsIgnoreCase(cpeManagementType)) {							
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
				}else {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				}
				
				if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
						&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("SHIFT_SITE")
								|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) {
					if("Yes".equalsIgnoreCase(cpeChassisChanged) && StringUtils.isNotBlank(cpeVariant) && !cpeVariant.equalsIgnoreCase("None") && !cpeVariant.equalsIgnoreCase("null") && !cpeVariant.equalsIgnoreCase("NA")) {
						LOGGER.info("MACD cpeModelchange cpeVariant={} cpeChassisChanged={} serviceCode={} orderCode={} cpeManagementType={}",cpeVariant,cpeChassisChanged,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid(),cpeManagementType);
						if(StringUtils.isNotBlank(oldEquipmentModel) && cpeVariant.toLowerCase().contains(oldEquipmentModel.toLowerCase())){
							LOGGER.info("Old and Current Cpe Model matched");
							processVar.put("isCPEConfiguredByCustomer", true);
							processVar.put("isCPEArrangedByCustomer", true);
							processVar.put("cpeSiScope", "");
							Map<String, String> attrMap= new HashMap<>();
							attrMap.put("cpe_chassis_changed", "No");
							componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						}else{
							LOGGER.info("Old and Current Cpe Model not matched");
						}
					}else {
						if (!"Unmanaged".equalsIgnoreCase(cpeManagementType) || !offeringName.toLowerCase().contains("unmanaged")) {
							LOGGER.info("STOPPINGCPEFLOW=>MACD cpeManagementType={} cpeChassisChanged={} cpeVariant={} serviceCode={} orderCode={}",cpeManagementType,cpeChassisChanged,cpeVariant,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid());
							processVar.put("isCPEConfiguredByCustomer", true);
							processVar.put("isCPEArrangedByCustomer", true);
							processVar.put("cpeSiScope", "");
						}
						
					}
				}

				processVar.put("documentValidationRequired", true);
				processVar.put("isLMRequired", isLMRequired);
				processVar.put("e2eServiceTestingCompleted", false);
				processVar.put("siteReadinessStatus", true);
				processVar.put("siteReadinessConfirmation", false);
				processVar.put("rowRequired", rowRequired);
				processVar.put("prowRequired", prowRequired);
				processVar.put("hasOSPCapexDeviation", false);
				processVar.put("hasIBDCapexDeviation", false);
				processVar.put("ibdContractRequired", true);
				processVar.put("isMuxRequired", false);
				processVar.put("isCPERequired", true);
				processVar.put("createServiceCompleted", false);
				processVar.put("isCLRSyncCallSuccess", false);
				processVar.put("serviceDesignCompleted", false);
				processVar.put("txConfigurationCompleted", false);
				processVar.put("serviceConfigurationCompleted", false);
				processVar.put("isStandardCPEDiscount", true);
				processVar.put("ipTerminateConfigurationSuccess", false);
				processVar.put("isRFRequired", isRFRequired);
				processVar.put("isColoRequired", isColoRequired);
				processVar.put("failoverTestingRequired", false);
				processVar.put("additionalTechDetailsTaskCompleted", false);
				processVar.put("siteReadinessTaskCompleted", false);
				processVar.put("isValidDocuments", false);
				processVar.put("isSoftLoopPossibleAtCE", false);
				processVar.put("lmTestOnnetWirelessSuccess", true);
				processVar.put("isPEInternalCablingRequired", false);
				processVar.put("isCEInternalCablingRequired", true);
				processVar.put("cableSwappingPERequired", "No");
				processVar.put("cableSwappingCERequired", "No");
				processVar.put("internalCablingResponsibility", "TCL");
				processVar.put("internalCablingInterface", "");
				processVar.put("serviceDeliveryPlanReady", false);
				processVar.put("order_enrichment_complete", false);
				processVar.put("getIpServiceSyncCallCompleted", false);
				processVar.put("validateBtsStatus", false);

				processVar.put("confirmOrderRequired", true);
				if (scServiceDetail.getIsAmended() != null
						&& scServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
					processVar.put("isAmendedOrder", true);

				}
				
				processVar.put("serviceConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("serviceConfigurationMessageSent", false);
				processVar.put("serviceConfigurationAckSuccess", false);
				processVar.put("serviceConfigurationSuccess", false);
				processVar.put("txConfigurationMessageSent", false);
				processVar.put("txConfigurationAckSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txMPLSConfigurationSuccess", false);
				processVar.put("rfConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("rfConfigurationMessageSent", false);
				processVar.put("rfConfigurationAckSuccess", false);
				processVar.put("rfConfigurationSuccess", false);

				processVar.put("previewIpConfigMessageSent", false);
				processVar.put("previewIpConfigAckSuccess", false);
				processVar.put("previewIpConfigSuccess", false);
				processVar.put("cancelIpConfigMessageSent", false);
				processVar.put("cancelIpConfigAckSuccess", false);
				processVar.put("cancelIpConfigSuccess", false);
				processVar.put("txManualConfigRequired", false);
				
				processVar.put("cpeLicensePRCompleted", true);
				processVar.put("cpeHardwarePRCompleted", true);
				processVar.put("isCpeAvailableInInventory", false);	
				processVar.put("cpeLicenseNeeded", true);	
				processVar.put("remainderCycle", reminderCycle);
				processVar.put("deemedAcceptanceDuration", "PT48H");
				processVar.put("cpeConfigurationCompleted", true);
				processVar.put("offnetPOEnabled", true);
				
				processVar.put("siteAType", "A");
				processVar.put("site_type", "A");
				
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				

				if (isLMRequired) {

					if (lastMileType.toLowerCase().contains("rf")) {
						
						String antenaSize = StringUtils.trimToEmpty(feasibilityAttributes.get("Mast_3KM_avg_mast_ht"));
						String mastType = StringUtils.trimToEmpty(feasibilityAttributes.get("mast_type"));
						String structureType = mastType;
						try {
							if (StringUtils.isNotBlank(antenaSize)) {
								Double antenaSizeDouble = Double.parseDouble(antenaSize);
								if (antenaSizeDouble > 6)
									structureType = "Mast";
							}
						} catch (Exception ee) {
							LOGGER.info("Mast_3KM_avg_mast_ht issue {}", ee);

						}
						if (StringUtils.isBlank(structureType)) structureType = "Pole";
						
						atMap.put("structureType", structureType);
						processVar.put("structureType", structureType);

						if (btsDeviceType.contains("radwin") || solutionType.contains("radwin") || "Radwin from TCL POP".equalsIgnoreCase(providerName)) {
							atMap.put("rfTechnology", "RADWIN");
							atMap.put("rfMake", "Radwin");
						} else if (btsDeviceType.contains("cambium") || solutionType.contains("cambium")) {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						} else {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						}/*else if (btsDeviceType.contains("wimax") || solutionType.contains("wimax")) {
							atMap.put("rfTechnology", "WIMAX");
							atMap.put("rfMake", "Wimax");
						}*/ 
						
						atMap.put("bhProviderName", bhConnectivity);
					} else {
						LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);
						if (connectedCustomer.contains("1") && serviceDetailsAttributes.containsKey("mux_details")) {

							try {

								String paramValue = serviceDetailsAttributes.get("mux_details");

								Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
										.findById(Integer.valueOf(paramValue));
								if (scAdditionalServiceParam.isPresent()) {
									String value = scAdditionalServiceParam.get().getValue();

									MuxDetailsItem[] muxDetailBeans = Utils.convertJsonToObject(value,
											MuxDetailsItem[].class);
									MuxDetailsItem muxDetailBean = muxDetailBeans[0];

									atMap.put("endMuxNodeName", muxDetailBean.getMux());
									atMap.put("endMuxNodeIp", muxDetailBean.getMuxIp());

								}
							} catch (Exception e) {
								LOGGER.error("error in parsing mux details : {} ", scServiceDetail.getUuid());

							}
						}

						// atMap.put("endMuxNodeName", "TJ1270-178");
						// atMap.put("endMuxNodeIp", "10.133.22.178");
						// atMap.put("endMuxNodePort", "");

					}

					processBomDetails(scServiceDetail, atMap,scComponentAttributesAMap);

					if (scServiceDetail.getSiteEndInterface() != null) {
						String interfaceType = getInterfaceValue(scServiceDetail.getSiteEndInterface());
						atMap.put("interfaceType", interfaceType);
						processVar.put("internalCablingInterface", interfaceType);
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}
				}
                try{
                	if(Objects.nonNull(lmType) && lmType.equalsIgnoreCase("offnet")) {
						String supplierCategory = getOffnetSupplierCategory(lmConnectionType, feasibilityAttributes);
						if (Objects.nonNull(supplierCategory)) {
							atMap.put("offnetSupplierCategory", supplierCategory);
							processVar.put("offnetSupplierCategory", supplierCategory);
						}
					}
				}catch (Exception ex){
					LOGGER.error("error in getting supplier category : {} ", scServiceDetail.getUuid());
					LOGGER.error("error in getting supplier category : {} ", ex);
				}
				atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);				
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				atMap.put("providerName", providerName);
				
				processVar.put("providerName", providerName);
				
				LOGGER.error("lastMileType : {} isValidLM: {}", lastMileType,isValidLM);
				atMap.put("lmType", lastMileType);
				
				atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				
				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));


				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if(mstStateToDistributionCenterMapping!=null && mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()!=null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
				}
				
				if("MACD".equalsIgnoreCase(StringUtils.trimToEmpty(orderType)) && "N".equalsIgnoreCase(scServiceDetail.getIsMigratedOrder())){
					ScServiceDetail parentScServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if(parentScServiceDetail!=null) {
						ScComponentAttribute parentUuidCommissioningDate = scComponentAttributesRepository
								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
										parentScServiceDetail.getId(), "commissioningDate",
										AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
						if(parentUuidCommissioningDate!=null && parentUuidCommissioningDate.getAttributeValue()!=null) {
							LOGGER.info("Parent Service Commissioning date is {}",parentUuidCommissioningDate.getAttributeValue());
							atMap.put("parentUuidCommissioningDate", parentUuidCommissioningDate.getAttributeValue());
						}
					}
				}

				ScOrderAttribute poDate = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);
				
				if(poDate!=null && poNumber!=null && poDate.getAttributeValue()!=null && poNumber.getAttributeValue()!=null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM,"A");

			}
			processVar.put("processType", "computeProjectPLan");
			processVar.put("demoOrder", "N");
			processVar.put("isDemoOrderBillable", "N");
			processVar.put("demoDays", 0);
			processVar.put("multiVrfBillingRequiredSkip", "No");
			
			if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && (StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("novation"))){
				LOGGER.info("novation::{}",scServiceDetail.getUuid());
				processVar.put("O2CPROCESSKEY", "novation_workflow");
				LOGGER.info("flowable trigerred for  Service id::{} is novation_workflow",scServiceDetail.getUuid());
				scServiceDetail.setWorkFlowName("novation_workflow");
				runtimeService.startProcessInstanceByKey("novation_workflow", processVar);
				
				return status;
				
			}else if(scOrder.getDemoFlag()!=null && scOrder.getDemoFlag().equalsIgnoreCase("Y")) {
				processVar.put("demoOrder", "Y");
				ScOrderAttribute billingType = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_TYPE, scOrder);
				if(billingType.getAttributeValue().equalsIgnoreCase("Billable demo")) {
					processVar.put("isDemoOrderBillable", "Y");
				}
				Map<String, String> atMap = new HashMap<>();
				processVar.put("demoDays", getDemoDays(scContractInfo.getOrderTermInMonths().toString()));
				atMap.put("demoDays",  String.valueOf(processVar.get("demoDays")));
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM,"A");
				Map<String, String> attrMap = new HashMap<>();
				attrMap.put("DEMO_PERIOD_IN_DAYS", String.valueOf(processVar.get("demoDays")));
				componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
				if(scServiceDetail.getOrderType().equalsIgnoreCase("MACD") && scServiceDetail.getOrderCategory().equalsIgnoreCase("DEMO_EXTENSION")) {
					LOGGER.info("Demo Extension Uuid::{}",scServiceDetail.getUuid());
					atMap.put("demoExtensionDays", String.valueOf(processVar.get("demoDays")));
					ScServiceDetail activeServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_codeOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if(activeServiceDetail!=null) {
						ScServiceAttribute activeDemoPeriodInDaysAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(activeServiceDetail.getId(),"DEMO_PERIOD_IN_DAYS");
						if(Objects.nonNull(activeDemoPeriodInDaysAttribute) && activeDemoPeriodInDaysAttribute.getAttributeValue()!=null){
							Integer demoPeriodInDays=Integer.valueOf(String.valueOf(processVar.get("demoDays")))+Integer.valueOf(activeDemoPeriodInDaysAttribute.getAttributeValue());
							attrMap.put("DEMO_PERIOD_IN_DAYS", String.valueOf(demoPeriodInDays));
							componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						}
						Map<String, String> compAttrMap = commonFulfillmentUtils
								.getComponentAttributesDetails(Arrays.asList("demoBillStartDate", "demoBillEndDate","demoDays"),
										activeServiceDetail.getId(), "LM", AttributeConstants.SITETYPE_A);
						if(compAttrMap!=null) {
							atMap.put("parentDemoBillStartDate", compAttrMap.get("demoBillStartDate")!=null ? compAttrMap.get("demoBillStartDate") :"NA" );
							atMap.put("demoBillStartDate", compAttrMap.get("demoBillEndDate")!=null ?compAttrMap.get("demoBillEndDate") :"NA" );
							atMap.put("parentDemoDays", compAttrMap.get("demoDays")!=null ?compAttrMap.get("demoDays") :"NA" );
						}
					}
					componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
							AttributeConstants.COMPONENT_LM,"A");
					processVar.put("O2CPROCESSKEY", "ill-demo-extension-service-fulfilment-handover-workflow");
					LOGGER.info("flowable trigerred for  Service id::{} is ill-demo-extension-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
					scServiceDetail.setWorkFlowName("ill-demo-extension-service-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("ill-demo-extension-service-fulfilment-handover-workflow", processVar);
					if(welcomeMailTrigger) {
						LOGGER.info("Welcome Mail for Demo Extension Uuid::{}",scServiceDetail.getUuid());
						sendNotificationToCustomer(scServiceDetail, scContractInfo, scComponentAttributesAMap);	
					}
					return status;
				}
			}
			if(isValidLM) {
                if("MACD".equalsIgnoreCase(scServiceDetail.getScOrder().getOrderType()) && scServiceDetail.getMultiVrfSolution()!=null && (CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution())||CommonConstants.YES.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution()))){
                    Map<String,String> attrMap=new HashMap<>();
                    if (CommonConstants.N.equalsIgnoreCase(scServiceDetail.getIsMultiVrf()) || CommonConstants.NO.equalsIgnoreCase(scServiceDetail.getIsMultiVrf())){

						if (serviceDetailsAttributes.containsKey("VRF based billing")
								&& serviceDetailsAttributes.get("VRF based billing") != null
								&& "Consolidated billing".equalsIgnoreCase(serviceDetailsAttributes.get("VRF based billing"))) {

							LOGGER.info("multiVrfBillingRequiredSkip skipped for service::{}",
												scServiceDetail.getUuid());
							processVar.put("multiVrfBillingRequiredSkip", "Yes");
	                        attrMap.put("multiVrfBillingRequiredSkip", "Yes");

						}
                        processVar.put("isCPEConfiguredByCustomer", true);
                        processVar.put("isCPEArrangedByCustomer", true);
                        processVar.put("cpeSiScope", "");

                        attrMap.put("cpe_chassis_changed", "No");
                        
                    }
                    componentAndAttributeService.updateAttributes(scServiceDetail.getId(), attrMap,
                            AttributeConstants.COMPONENT_LM,"A");
                }
                
                boolean isMeshOrSpokeExists[]= {true};
                List<ScServiceAttribute> siteTypeServiceAttributes=scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(scServiceDetail.getId(),Arrays.asList("Site Type"));
                if(siteTypeServiceAttributes!=null && !siteTypeServiceAttributes.isEmpty()) {
                	siteTypeServiceAttributes.stream().forEach(attr -> {
                		if(attr.getAttributeValue()!=null && attr.getAttributeValue().toLowerCase().contains("hub")) {
                			isMeshOrSpokeExists[0]=false;
                		}
                	});
                }
                    if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
						|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
								&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE") && isMeshOrSpokeExists[0])) {
					LOGGER.info("ConfirmOrder not required for Service id::{}",scServiceDetail.getId());
					processVar.put("confirmOrderRequired", false);
				}
				if(scServiceDetail.getIsAmended()!=null && scServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
					processVar.put("confirmOrderRequired", true);

				}
				if(isP2PwithoutBH == true || isP2PwithBH == true) {
					
					if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("hot")
									&& !StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso"))
									|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))){
						LOGGER.info("P2PwithoutBH::{}",scServiceDetail.getUuid());
						processVar.put("O2CPROCESSKEY", "p2p-fulfilment-macd-workflow");
						LOGGER.info("flowable trigerred for  Service id::{} is p2p-fulfilment-macd-workflow",scServiceDetail.getUuid());
						scServiceDetail.setWorkFlowName("p2p-fulfilment-macd-workflow");
						runtimeService.startProcessInstanceByKey("p2p-fulfilment-macd-workflow", processVar);
					}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("hot")
									&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso"))
									|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("SHIFT_SITE")
											&& (StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("lm") 
											|| StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso")
											|| StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Shifting"))))){
						LOGGER.info("BSO Workflow::{}",scServiceDetail.getUuid());
						
						processVar.put("O2CPROCESSKEY", "p2p-fulfilment-bso-workflow");
						processVar.put("p2pConfigRequired", false);
						LOGGER.info("flowable trigerred for  Service id::{} is p2p-fulfilment-bso-workflow",scServiceDetail.getUuid());
						scServiceDetail.setWorkFlowName("p2p-fulfilment-bso-workflow");
						runtimeService.startProcessInstanceByKey("p2p-fulfilment-bso-workflow", processVar);
					}else {
						LOGGER.info("New Order Workflow::{}",scServiceDetail.getUuid());
						processVar.put("O2CPROCESSKEY", "p2p-fulfilment-workflow");
						LOGGER.info("flowable trigerred for  Service id::{} is p2p-fulfilment-workflow",scServiceDetail.getUuid());
						scServiceDetail.setWorkFlowName("p2p-fulfilment-workflow");
						runtimeService.startProcessInstanceByKey("p2p-fulfilment-workflow", processVar);
					}
				}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
						|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
								&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))
						|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(orderSubCategory)
								&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("parallel"))){
				
					
					if(lmType.equalsIgnoreCase("offnet")){
						LOGGER.info("NEW  offnet");
						processVar.put("O2CPROCESSKEY", "offnet-fulfilment-handover-new-workflow");
						LOGGER.info("flowable trigerred for  Service id::{} is offnet-fulfilment-handover-new-workflow",scServiceDetail.getUuid());
						scServiceDetail.setWorkFlowName("offnet-fulfilment-handover-new-workflow");
						runtimeService.startProcessInstanceByKey("offnet-fulfilment-handover-new-workflow", processVar);			
					}else {
						LOGGER.info("NEW or ADD SITE or parallel");
						processVar.put("O2CPROCESSKEY", "ill-service-fulfilment-handover-workflow");
						LOGGER.info("flowable trigerred for  Service id::{} is ill-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
						scServiceDetail.setWorkFlowName("ill-service-fulfilment-handover-workflow");
						runtimeService.startProcessInstanceByKey("ill-service-fulfilment-handover-workflow", processVar);			
					}
						
				}else if((lmType.equalsIgnoreCase("offnet")) && StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")){
					LOGGER.info("Offnet MACD");					
					
					if(skipOffnet) {
						LOGGER.info("Offnet Hot Upgrade");	
						processVar.put("O2CPROCESSKEY", "offnet-hot-upgrade-workflow");
						LOGGER.info("flowable trigerred for  Service id::{} is offnet-hot-upgrade-workflow",scServiceDetail.getUuid());
						scServiceDetail.setWorkFlowName("offnet-hot-upgrade-workflow");
						runtimeService.startProcessInstanceByKey("offnet-hot-upgrade-workflow", processVar);
					}else {
						LOGGER.info("Offnet BSO or LM or Downgrade");	
						processVar.put("O2CPROCESSKEY", "offnet-fulfilment-handover-workflow");
						LOGGER.info("flowable trigerred for  Service id::{} is offnet-fulfilment-handover-workflow",scServiceDetail.getUuid());
						scServiceDetail.setWorkFlowName("offnet-fulfilment-handover-workflow");
						runtimeService.startProcessInstanceByKey("offnet-fulfilment-handover-workflow", processVar);
					}
				}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(orderSubCategory)
						&& (StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("lm") 
								|| StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso")
								|| StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Shifting"))){
					LOGGER.info("LM");
					processVar.put(IS_PORT_CHANGED, true);
					processVar.put("isMuxIPAvailable", false);
					processVar.put("O2CPROCESSKEY", "ill-bso-service-fulfilment-handover-workflow");
					LOGGER.info("flowable trigerred for  Service id::{} is ill-bso-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
					scServiceDetail.setWorkFlowName("ill-bso-service-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("ill-bso-service-fulfilment-handover-workflow", processVar);
				}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
						&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))){
					LOGGER.info("CB or ADD IP");
					processVar.put(IS_CONNECTED_SITE, true);
					processVar.put(IS_PORT_CHANGED, false);
					processVar.put("isMuxIPAvailable", false);
					processVar.put("O2CPROCESSKEY", "ill-macd-cb-service-fulfilment-handover-workflow");
					LOGGER.info("flowable trigerred for  Service id::{} is ill-macd-cb-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
					scServiceDetail.setWorkFlowName("ill-macd-cb-service-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("ill-macd-cb-service-fulfilment-handover-workflow", processVar);
				}
			}
			
			if(isValidLM && welcomeMailTrigger) {
				sendNotificationToCustomer(scServiceDetail, scContractInfo, scComponentAttributesAMap);	
			}
			
			 autoMigration(scServiceDetail,scOrder,false);
					

		} catch (Exception e) {

			LOGGER.error("Error processing data", e);
			status = false;
		}
		return status;
	}


	
	
	
	@Transactional
	public Boolean processIPCL2ODataToFlowable(ServiceFulfillmentRequest serviceFulfillmentRequest) {
		Boolean status = true;
		try {
			
			LOGGER.info("Input processIPCL2ODataToFlowable received for L2O :: {}", serviceFulfillmentRequest);
			Map<String, Object> processVar = new HashMap<>();
			
			if (serviceFulfillmentRequest.getCustomerInfo() != null) {
				processVar.put("customerUserName", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCustomerName()));
				processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getOrderInfo().getOrderCreatedBy()));
			}
			if (serviceFulfillmentRequest.getOrderInfo() != null && serviceFulfillmentRequest.getPrimaryServiceInfo() != null) {
				OrderInfoBean orderInfoBean = serviceFulfillmentRequest.getOrderInfo();
				processVar.put(SC_ORDER_ID, orderInfoBean.getScOrderId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(orderInfoBean.getOptimusOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderInfoBean.getOrderType()));
				processVar.put(ORDER_CATEGORY, orderInfoBean.getOrderCategory() != null ? StringUtils.trimToEmpty(orderInfoBean.getOrderCategory()) : "");
				processVar.put(IS_DELETE_VM, IpcConstants.DELETE_VM.equals(String.valueOf(processVar.get(ORDER_CATEGORY))));
				processVar.put(CONTRACT_START_DATE, orderInfoBean.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", orderInfoBean.getOrderCreatedDate());
				processVar.put(ORDER_CREATED_DATE, orderInfoBean.getOrderCreatedDate());
				if (CommonConstants.MACD.equals(orderInfoBean.getOrderType())) {
					processVar.put(OPTIMUS_MACD, true);
				}
				ServiceInfoBean primaryServiceInfo = serviceFulfillmentRequest.getPrimaryServiceInfo();
				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(primaryServiceInfo.getServiceCode()));
				processVar.put(SERVICE_ID, primaryServiceInfo.getServiceId());
				Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(primaryServiceInfo.getServiceId());
				if(scServiceDetail.isPresent()) {
					processVar.put(PRODUCT_NAME, scServiceDetail.get().getErfPrdCatalogProductName());
					processVar.put(CITY, scServiceDetail.get().getSourceCity());
				}
				
				Map<String, String> orderAttributes = orderInfoBean.getOrderAttributes();
				if(orderAttributes.containsKey(CommonConstants.IPC_PROVISIONING_FLOW)) {
					if((CommonConstants.IPC_SERVICE_DELIVERY).equals(orderAttributes.get(CommonConstants.IPC_PROVISIONING_FLOW))) {
						processVar.put(MasterDefConstants.IS_SERVICE_DELIVERY_FLOW, true);
					}
				}
				
				List<ScProductDetail> optProductDetail = scProductDetailRepository.findByScServiceDetailId(primaryServiceInfo.getServiceId());
				if(CollectionUtils.isNotEmpty(optProductDetail)) {
					
					//for VM Task creation
					List<ScProductDetail> vmProductDetail = optProductDetail.stream()
							.filter(scProductDetail -> (!"Access".equals(scProductDetail.getSolutionName()) && 
									!"IPC addon".equals(scProductDetail.getSolutionName())))
			                .collect(Collectors.toList());
					processVar.put("vmProvisioningReq", CollectionUtils.isNotEmpty(vmProductDetail));
					
					//for Access Task creation
					List<ScProductDetail> accessProductDetail = optProductDetail.stream()
							.filter(scProductDetail -> ("Access".equals(scProductDetail.getSolutionName())))
			                .collect(Collectors.toList());
					processVar.put("bandwidthAllocationReq", CollectionUtils.isNotEmpty(accessProductDetail));
				
					//for Addon Task creation
					List<ScProductDetail> addonProductDetail = optProductDetail.stream()
							.filter(scProductDetail -> ("IPC addon".equals(scProductDetail.getSolutionName())))
			                .collect(Collectors.toList());
					
					if(CollectionUtils.isNotEmpty(addonProductDetail)) {
						Set<ScProductDetailAttributes> prdDtlAttr = addonProductDetail.get(0).getScProductDetailAttributes();
						processVar.put("ipAllocationReq", prdDtlAttr.parallelStream()
								.anyMatch(productAttributes -> "Additional Ip".equalsIgnoreCase(productAttributes.getCategory())));
						processVar.put("backupAllocationReq",prdDtlAttr.parallelStream()
								.anyMatch(productAttributes -> "Backup".equalsIgnoreCase(productAttributes.getCategory())));
						processVar.put("vpnConnectionStatusReq",prdDtlAttr.parallelStream()
								.anyMatch(productAttributes -> "VPN Connection".equalsIgnoreCase(productAttributes.getCategory())));	
					}
				}

				ScOrderAttribute scOrderAttribute =scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(CommonConstants.IS_IPC_BILLING_INTL, scServiceDetail.get().getScOrder());
				Map<String, String> serviceAttr = new HashMap<>();
				serviceAttr.put(scOrderAttribute.getAttributeName(), scOrderAttribute.getAttributeValue());
				componentAndAttributeService.loadServiceAttributesIfNotPresent(scServiceDetail.get(), serviceAttr);
				LOGGER.info("scComponentAttributesAMap {} ",scOrderAttribute);
				
				processVar.put(CommonConstants.IS_IPC_BILLING_INTL, false);
				if(scOrderAttribute != null && scOrderAttribute.getAttributeValue().equalsIgnoreCase("Y")){
					LOGGER.info("IPC Billing International For Service Id::{}, Service Code::{}", scServiceDetail.get().getId(), scServiceDetail.get().getUuid());
					processVar.put(CommonConstants.IS_IPC_BILLING_INTL, true);
					processVar.put(CommonConstants.IS_IPC_TAX_CAPTURE_REQUIRED,true);
				}
				
				processVar.put("ipcServiceAcceptanceTimeout", IPCServiceFulfillmentConstant.INTIAL_ORDER_ACCEPTANCE_DURATION);
				processVar.put("processType", "computeProjectPLan");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				processVar.put("remainderCycle", reminderCycle);

				runtimeService.startProcessInstanceByKey("ipc_service_fulfillment_workflow", processVar);
			} else {
				LOGGER.info("invalid data in processIPCL2ODataToFlowable");
			}
			LOGGER.info("processIPCL2ODataToFlowable completed");
			
		} catch (Exception e) {
			LOGGER.error("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}	

	private void autoMigration(ScServiceDetail scServiceDetail, ScOrder scOrder,boolean isTermination) {
		try {

			LOGGER.info("autoMigration method invoked");
			/*
			 * 1. macd 2. ill/gvpn 3. service_code doesnt exist migration status w status =
			 * success & servicestae in servicedetail=ACTIVE re-migrate
			 */
			
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			
			if(isTermination) {
				orderType="MACD";
			}
			if (scServiceDetail != null && !"Eco Internet Access".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName()) && ("MACD".equals(orderType) || scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC"))) {
				LOGGER.info("Auto Migration invoked for MACD");
				String response = null;
				Map<String, String> migrationAttributes = new HashMap<>();
				String uuid = "";
				String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

				if ("ADD_SITE".equals(orderCategory)
						|| (Objects.nonNull(scServiceDetail.getOrderSubCategory())
								&& !scServiceDetail.getOrderSubCategory().isEmpty()
								&& scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))) {
					uuid = scServiceDetail.getParentUuid();
					LOGGER.info("Parent UUID for order subcategory {} is {} with base UUID {}", scServiceDetail.getOrderSubCategory(),
							uuid,scServiceDetail.getUuid());
				} else if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")) {
					ScServiceAttribute referenceServiceIdAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"Service Id");
					if(referenceServiceIdAttribute!=null && referenceServiceIdAttribute.getAttributeValue()!=null && !referenceServiceIdAttribute.getAttributeValue().isEmpty()) {
						LOGGER.info("autoMigration.IZOPC-ServiceId={} with Reference:{}",scServiceDetail.getId(),referenceServiceIdAttribute.getAttributeValue());
						uuid=referenceServiceIdAttribute.getAttributeValue();
					}
				}else { // else -> get uuid , ACTIVE
					uuid = scServiceDetail.getUuid();
					LOGGER.info("UUID for order subcategory {} is {}", scServiceDetail.getOrderSubCategory(), uuid);
				}
				if (isTermination) {
					uuid = scServiceDetail.getUuid();
					migrationAttributes.put("termination", "yes");
					migrationAttributes.put("scServiceDetailsId", String.valueOf(scServiceDetail.getId()));
					migrationAttributes.put("scOrderId", String.valueOf(scOrder.getId()));
					if (!uuid.isEmpty() && scOrder != null && scOrder.getOpOrderCode() != null
							&& !scOrder.getOpOrderCode().isEmpty()) {
						LOGGER.info("Inside Auto Migration for service code {} with ScOrder exisiting", uuid);
						if (scOrder.getOpOrderCode().toLowerCase().contains("gvpn")) {

							migrationAttributes.put("serviceId", uuid);
							migrationAttributes.put("type", "gvpn");
							LOGGER.info("Auto Migration for service code {} with product type {}", uuid, "gvpn");
							mqUtils.send(servicemigration_queue, Utils.convertObjectToJson(migrationAttributes));
							autoFtiMigration(scServiceDetail,scOrder);
						}
						if (scOrder.getOpOrderCode().toLowerCase().contains("ias")) {
							migrationAttributes.put("serviceId", uuid);
							migrationAttributes.put("type", "ill");
							LOGGER.info("Auto Migration for service code {} with product type {}", uuid, "ill");
							mqUtils.send(servicemigration_queue, Utils.convertObjectToJson(migrationAttributes));
							autoFtiMigration(scServiceDetail,scOrder);
						}

						if (!scOrder.getOpOrderCode().toLowerCase().contains("gvpn")
								&& !scOrder.getOpOrderCode().toLowerCase().contains("ias"))
							LOGGER.info("MIGRATION NOT VALID for service code {}", uuid);

				
					}

				}
				else {
					
					if (!uuid.isEmpty() && scOrder != null && scOrder.getOpOrderCode() != null
							&& !scOrder.getOpOrderCode().isEmpty()) {
						LOGGER.info("Inside Auto Migration for service code {} with ScOrder exisiting", uuid);
						if (scOrder.getOpOrderCode().toLowerCase().contains("gvpn") || scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")) {
							migrationAttributes.put("serviceId", uuid);
							migrationAttributes.put("type", "gvpn");
							LOGGER.info("Auto Migration for service code {} with product type {}", uuid, "gvpn");
							mqUtils.send(servicemigration_queue, Utils.convertObjectToJson(migrationAttributes));
							checkSecondary(scServiceDetail,"gvpn",false);
						}
						if (scOrder.getOpOrderCode().toLowerCase().contains("ias")) {
							migrationAttributes.put("serviceId", uuid);
							migrationAttributes.put("type", "ill");
							LOGGER.info("Auto Migration for service code {} with product type {}", uuid, "ill");
							mqUtils.send(servicemigration_queue, Utils.convertObjectToJson(migrationAttributes));
							checkSecondary(scServiceDetail,"ill",false);
						}
				}
				
				

				LOGGER.info("Auto migration response for service code {} is {}:", scServiceDetail.getUuid(), response);
				
			}
		} 
		}catch (Exception e) {
			LOGGER.error("Auto migration failure for service code {} with error {}:", scServiceDetail.getUuid(), e);

			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				
				LOGGER.error("Auto migration response for service code {} is {}:", scServiceDetail.getUuid(), e);

				// return "ERROR -> " + sw.toString();
				notificationService.notifyOptimusO2CAutoMigrationFailure(scServiceDetail.getId(),
						scServiceDetail.getUuid(), sw.toString());
				
				autoFtiMigration(scServiceDetail,scOrder);
				
			} catch (Exception e1) {
				LOGGER.error("Auto migration Email sending failure for service code {} with error {}:",
						scServiceDetail.getUuid(), e1);
			}
		}

	}
	
	private void autoFtiMigration(ScServiceDetail scServiceDetail, ScOrder scOrder) {
		try {

			LOGGER.info("autoFtiMigration method invoked for service Id::{}",scServiceDetail.getId());

			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			if (scServiceDetail != null && ("MACD".equals(orderType) || "TERMINATION".equals(orderType))) {
				LOGGER.info("Auto FTI Migration invoked for MACD or Termination");
				String response = null;
				Map<String, String> migrationAttributes = new HashMap<>();
				String uuid = "";
				String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

				if ("ADD_SITE".equals(orderCategory) || (Objects.nonNull(scServiceDetail.getOrderSubCategory())
						&& !scServiceDetail.getOrderSubCategory().isEmpty()
						&& scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))) {
					uuid = scServiceDetail.getParentUuid();
					LOGGER.info("FTI Parent UUID for order subcategory {} is {} with base UUID {}",
							scServiceDetail.getOrderSubCategory(), uuid, scServiceDetail.getUuid());
				} else { // else -> get uuid , ACTIVE
					uuid = scServiceDetail.getUuid();
					LOGGER.info("FTI UUID for order subcategory {} is {}", scServiceDetail.getOrderSubCategory(), uuid);
				}

				migrationAttributes.put("serviceId", uuid);
				migrationAttributes.put("scServiceDetailId", String.valueOf(scServiceDetail.getId()));
				LOGGER.info("Auto FTI Migration for service code {}", uuid);
				mqUtils.send(servicemigration_queue_fti, Utils.convertObjectToJson(migrationAttributes));
				checkSecondary(scServiceDetail, null,true);

			}
		} catch (Exception e) {
			LOGGER.error("Auto FTI migration failure for service code {} with error {}:", scServiceDetail.getUuid(), e);

		}

	}

	private void checkSecondary(ScServiceDetail scServiceDetail, String type, Boolean isFti) {
		try {

			LOGGER.info("Inside checkSecondary method... for service Code {} and Id {} with isFti {}", scServiceDetail.getUuid(),
					scServiceDetail.getId(),isFti);
			if (scServiceDetail.getPriSecServiceLink() != null && !scServiceDetail.getPriSecServiceLink().isEmpty()) {
				String secUuid = scServiceDetail.getPriSecServiceLink();
				if (!isFti) {
					LOGGER.info("secondary service code {} found for primary service code {} with product type {}",
							secUuid, type);
					Map<String, String> migrationAttributes = new HashMap<>();
					migrationAttributes.put("serviceId", secUuid);
					migrationAttributes.put("type", type);
					LOGGER.info("Auto Migration for secondary service code {} with primary service code {}", secUuid);
					mqUtils.send(servicemigration_queue, Utils.convertObjectToJson(migrationAttributes));
				} else {
					Map<String, String> migrationAttributes = new HashMap<>();
					migrationAttributes.put("serviceId", secUuid);
					LOGGER.info("Auto FTI Migration for secondary service code {} with primary service code {}",
							secUuid);
					mqUtils.send(servicemigration_queue_fti, Utils.convertObjectToJson(migrationAttributes));
				}
			} else {
				LOGGER.info("Secondary Code Migration not required for {}", scServiceDetail.getUuid());
			}
		} catch (Exception e) {
			LOGGER.error("Auto migration failure for secondary service code of primary uuid {} with error {}:",scServiceDetail.getUuid(), e);
		}

	}

	private void updateVpnDetail(ScServiceAttribute gvpnCommonServiceAttribute, ScServiceAttribute siteTypeServiceAttribute) {
		LOGGER.info("updateVpnDetail");
		if("Mesh".equalsIgnoreCase(siteTypeServiceAttribute.getAttributeValue())){
			LOGGER.info("Mesh");
			gvpnCommonServiceAttribute.setAttributeValue("Full Mesh");
		}else if("Hub".equalsIgnoreCase(siteTypeServiceAttribute.getAttributeValue())
				|| "Spoke".equalsIgnoreCase(siteTypeServiceAttribute.getAttributeValue())){
			LOGGER.info("Hub & Spoke");
			gvpnCommonServiceAttribute.setAttributeValue("Hub & Spoke");
		}
		scServiceAttributeRepository.save(gvpnCommonServiceAttribute);
	}

	private String getInterfaceValue(String siteEndInterface) {
		
		if(((siteEndInterface.toLowerCase().contains("100-base") || siteEndInterface.toLowerCase().contains("100 base") || siteEndInterface.toLowerCase().contains("100base")) 
				&& siteEndInterface.toLowerCase().contains("lx") ))return "Optical";

		if ((siteEndInterface.equalsIgnoreCase("Fast Ethernet") || siteEndInterface.equalsIgnoreCase("FE") 
				|| siteEndInterface.contains("G.703")
				|| siteEndInterface.equalsIgnoreCase("V.35")
				|| siteEndInterface.equalsIgnoreCase("Gigabit Ethernet (Electrical)")
				|| siteEndInterface.equalsIgnoreCase("BNC")
				|| siteEndInterface.toUpperCase().contains("10 BASE")
				|| siteEndInterface.toUpperCase().contains("100 BASE")
				|| siteEndInterface.toUpperCase().contains("10-BASE")
				|| siteEndInterface.toUpperCase().contains("100-BASE")
				|| siteEndInterface.toUpperCase().contains("10BASE")
				|| siteEndInterface.toUpperCase().contains("100BASE")
				|| siteEndInterface.contains("Copper")
				|| siteEndInterface.toLowerCase().contains("gig ethernet")
				|| (siteEndInterface.toUpperCase().contains("1000") && siteEndInterface.toUpperCase().contains("TX"))
				|| siteEndInterface.contains("Electrical"))
				) {
			return "Electrical";
		} else if (siteEndInterface.contains("G.957")
				|| siteEndInterface.equalsIgnoreCase("Gigabit Ethernet (Optical)")
				|| siteEndInterface.equalsIgnoreCase("10G WAN PHY")
				|| siteEndInterface.equalsIgnoreCase("10G LAN PHY")
				|| siteEndInterface.toUpperCase().contains("1000")
				|| siteEndInterface.contains("10G")
				|| siteEndInterface.contains("Fiber")
				|| siteEndInterface.contains("Optical")) {
			return "Optical";
		}

		return siteEndInterface;
	}
	
	private String getInterfaceValueBasedOnMediaType(String mediaType) {
		if ("fiber pair".equalsIgnoreCase(mediaType.toLowerCase())) {
			return "Optical";
		} else{
			return "Electrical";
		}
	}

	

	private void processBomDetails(ScServiceDetail scServiceDetail, Map<String, String> taskDataMap, Map<String, String> scComponentAttributes) {

		try {
			
		String lastMileBw= 	scComponentAttributes.get("localLoopBandwidth");
			LOGGER.info("localoopBand------------>:{}", scServiceDetail);
			if (lastMileBw != null && scServiceDetail != null) {

				ScServiceAttribute scenarioType = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "solution_type");

				LOGGER.info("scenarioType---------->:{}", scenarioType);

				String lastMile = scComponentAttributes.get("lastMileProvider");
				String type = "";
				if (lastMile.contains("RF")) {
					type = "RF";
					LOGGER.info("enter into rf details------------>");

					processRfBomDetails(type, lastMileBw, taskDataMap, scenarioType);
				} else {
					type = "MUX";
					LOGGER.info("enter into mux details------------>");

					processMuxBomDetails(lastMileBw, taskDataMap, scServiceDetail,scComponentAttributes);

				}

			}

		} catch (Exception e) {
			LOGGER.error("error in geting bom details from view for process l20 service:{}", e);
		}

	}

	private void processMuxBomDetails(String localoopBand, Map<String, String> taskDataMap,
			ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributesAMap) {
String destinationCity=scComponentAttributesAMap.get("destinationCity");
		if (scServiceDetail != null) {
			LOGGER.info("enter into processMuxBomDetails------------> with city :{} ",
					destinationCity);

			MstMuxBomTier1 mstMuxBomTier1 = mstMuxBomTier1Repository.findByCity(destinationCity);

			if (mstMuxBomTier1 != null) {
				LOGGER.info("enter into mstMuxBomTier1------------> ");

				List<VwMuxBomMaterialDetail> vwMuxBomMaterialDetails = vwBomMuxDetailsRepository
						.findByMakeBandwidthLessThan(Double.valueOf(localoopBand), mstMuxBomTier1.getMake());

				if (!vwMuxBomMaterialDetails.isEmpty()) {
					LOGGER.info("VwMuxBomMaterialDetail------------> :{}", vwMuxBomMaterialDetails);

					VwMuxBomMaterialDetail vwMuxBomMaterialDetail = vwMuxBomMaterialDetails.stream().findFirst()
							.orElse(null);

					taskDataMap.put("muxMakeModel", vwMuxBomMaterialDetail.getBomName());
					taskDataMap.put("muxMake", vwMuxBomMaterialDetail.getMake());
					taskDataMap.put("topologyType", Objects.nonNull(vwMuxBomMaterialDetail.getTopologyType()) ? vwMuxBomMaterialDetail.getTopologyType() : "");
				}

			}

		}

	}

	private void processRfBomDetails(String type, String localoopBand, Map<String, String> taskDataMap,
			ScServiceAttribute scenarioType) {

		if (scenarioType != null) {
			LOGGER.info("enter into processRfBomDetails------------>scenarioType  and local loop :{}{} ",scenarioType,localoopBand);

			List<VwBomMaterialDetail> bomMaterialDetails = vwBomMaterialDetailRepository.findByBandwidthLessThan(type,
					Double.valueOf(localoopBand));
			if (!bomMaterialDetails.isEmpty()) {
				LOGGER.info("bomMaterialDetails:{}",bomMaterialDetails);

				List<VwBomMaterialDetail> filterbomMaterialDetails = bomMaterialDetails.stream()
						.filter(bom -> scenarioType.getAttributeValue().contains(bom.getScenarioType()))
						.collect(Collectors.toList());
				
				if(!filterbomMaterialDetails.isEmpty()) {
					LOGGER.info("bomMaterialDetails after filter:{}",filterbomMaterialDetails);

					VwBomMaterialDetail vwMuxBomMaterialDetail=	filterbomMaterialDetails.stream().findFirst().orElse(null);
					taskDataMap.put("rfMakeModel", vwMuxBomMaterialDetail.getBomName());
					taskDataMap.put("rfMake", vwMuxBomMaterialDetail.getMake());
				}
				else {
					
					VwBomMaterialDetail vwMuxBomMaterialDetail=	bomMaterialDetails.stream().findFirst().orElse(null);
					LOGGER.info("VwBomMaterialDetail after filter:{}",vwMuxBomMaterialDetail);

					if(vwMuxBomMaterialDetail!=null) {
					taskDataMap.put("rfMakeModel", vwMuxBomMaterialDetail.getBomName());
					taskDataMap.put("rfMake", vwMuxBomMaterialDetail.getMake());
					}
				}

			}

		}
	}
	@Transactional(readOnly=false)
	public Boolean processNPLL2ODataToFlowable(Integer serviceId, ScServiceDetail scServiceDetail, boolean welcomeMailTrigger) {
		Boolean status = true;
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}
			LOGGER.info("Input processNPLL2ODataToFlowable received for L2O serviceId:: {} service code:: {}", serviceId,
					scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = null;
			if(scContractInfos != null) {
				scContractInfo = scContractInfos.stream().findFirst().orElse(null);
			}

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",
						scServiceAttribute.getId(), scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}
			
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);
			
			Map<String, String> scComponentAttributesBMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "B");
			LOGGER.info("scComponentAttributesBMap {} ",scComponentAttributesBMap);

			Map<String, Object> processVar = new HashMap<>();
			
			String customerUserName = StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()); 		
			processVar.put("customerUserName",customerUserName );
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

			
			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
				if(scContractInfo != null) {
					processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				}
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				
				processVar.put("parentServiceCode", null);
				processVar.put("isParallelExists", "false");
				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())){
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				}else{
					processVar.put("orderSubCategory", "NA");
				}
				if("MACD".equalsIgnoreCase(orderType) && 
						Objects.nonNull(scServiceDetail.getOrderSubCategory()) && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){
					LOGGER.info("ProcessL2O:ParallelExists");
					processVar.put("isParallelExists", "true");
					if(Objects.nonNull(scServiceDetail.getParentUuid())){
						processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
					}
				}

			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
			String crossConnectType ="";
			if (scServiceDetail != null) {

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				String lastMileTypeA = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_a"));
				String lastMileTypeB = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_b"));
				
				String connectedBuildingA = StringUtils
						.trimToEmpty(feasibilityAttributes.get("a_connected_building_tag"));
				String connectedCustomerA = StringUtils.trimToEmpty(feasibilityAttributes.get("a_connected_cust_tag"));
				
				String connectedCustomerCategoryA = StringUtils.trimToEmpty(feasibilityAttributes.get("a_Orch_Category"));				
				String connectedCustomerCategoryB = StringUtils.trimToEmpty(feasibilityAttributes.get("b_Orch_Category"));
				String connectedBuildingB = StringUtils
						.trimToEmpty(feasibilityAttributes.get("b_connected_building_tag"));
				String connectedCustomerB = StringUtils.trimToEmpty(feasibilityAttributes.get("b_connected_cust_tag"));
				
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));
				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));

				LOGGER.info("cpeType for serviceid :{} and service code:{} and map:: {}", serviceId,
						scServiceDetail.getUuid(), serviceDetailsAttributes);
			
				processVar.put("cpeType", "");
				
				String siteEndInterfaceA = scComponentAttributesAMap.get("interface");
				String siteEndInterfaceB = scComponentAttributesBMap.get("interface");

				boolean isLMRequired = true;
				boolean isColoRequired = false;
				boolean negotiationRequiredForOffnetLM = false;
				boolean rowRequired = true;
				boolean prowRequired = true;
				boolean isRFRequired = false;
				boolean isCPERequired = true;

				processVar.put(IS_CONNECTED_SITE, false);
				processVar.put(IS_CONNECTED_BUILDING, false);
				processVar.put("checkCLRSuccess", false);
				processVar.put("isAccountRequired", false);

				
				

				
				if(lastMileTypeA.toLowerCase().contains("onnet wireless"))lastMileTypeA="OnnetRF";
				else if(lastMileTypeA.toLowerCase().contains("offnet wireless"))lastMileTypeA="OffnetRF";
				else if(lastMileTypeA.toLowerCase().contains("offnet wireline"))lastMileTypeA="OffnetWL";
				else if(lastMileTypeA.toLowerCase().contains("onnet wireline"))lastMileTypeA="OnnetWL";
				else if(lastMileTypeA.toLowerCase().contains("onnetwl_npl"))lastMileTypeA="OnnetWL";
				else if(lastMileTypeA.toLowerCase().contains("man") || lastMileTypeA.toLowerCase().contains("wan aggregation"))lastMileTypeA="OnnetWL";
				
				if(StringUtils.isBlank(lastMileTypeA))lastMileTypeA="OnnetWL";
				
				if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())
						&& "Passive".equalsIgnoreCase(crossConnectType)){
			
					if(lastMileTypeA.toLowerCase().contains("colo")) {
						LOGGER.info("MMR-Passive-COLO-Changed-OnnetWL");
						lastMileTypeA="OnnetWL";
					}
				}
				
								
				
				String lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileTypeA.toLowerCase().contains("offnetwl")) {
					lmScenarioType = "Offnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "offnet";
					lastMileTypeA="OffnetWL";
				} else {
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileTypeA="OnnetWL";
					if (connectedCustomerA.contains("1") || siteEndInterfaceA.contains("G.957") || connectedCustomerCategoryA.equalsIgnoreCase("Connected Customer") ) {
						processVar.put(IS_CONNECTED_SITE, true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						rowRequired = false;
						prowRequired = false;
						lmScenarioType = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingA.contains("1")) {
						rowRequired = false;
						prowRequired = true;
						processVar.put(IS_CONNECTED_BUILDING, true);
						processVar.put(IS_CONNECTED_SITE, false);
						processVar.put("isNetworkSelectedPerBOP", true);
						lmScenarioType = "Onnet Wireline - Connected Building";
					} else {
						rowRequired = true;
						prowRequired = true;
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put(IS_CONNECTED_SITE, false);
						processVar.put("isNetworkSelectedPerBOP", true);
						lmScenarioType = "Onnet Wireline - Near Connect";
					}

				}
				processVar.put("rowRequired", rowRequired);
				processVar.put("prowRequired", prowRequired);
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE,lmConnectionType);

				if (lastMileTypeA.toLowerCase().contains("offnet")) {
					// TODO: check if P2P
					// isColoRequired = true;
					String source = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceState"));
					String destination = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState"));					
				
					LOGGER.info("getDestinationState:{} and getSourceState:{}", source, destination);

					if (destination != null && source != null && !destination.equalsIgnoreCase(source)) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);

					}

					Double portBW = 0.0;
					
					Double supplierLocalLoopBwDouble = 0.0;
					String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
					
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}
					// check NRC >2 Lacks or Bandwidth >100 Mbps
					LOGGER.info("Total NRC Loaded is {} and Bandwidth is {} ", scServiceDetail.getNrc(), supplierLocalLoopBwDouble);
					Double offnetWlNrc = 0.0;							
					if(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")!=null)  offnetWlNrc= Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf")!=null) offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and Bandwidth is {} ", offnetWlNrc,offnetRFNrc, supplierLocalLoopBwDouble);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000 )|| supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true {}",scServiceDetail.getUuid());
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}
				
				if(lastMileTypeB.toLowerCase().contains("onnet wireless"))lastMileTypeB="OnnetRF";
				else if(lastMileTypeB.toLowerCase().contains("offnet wireless"))lastMileTypeB="OffnetRF";
				else if(lastMileTypeB.toLowerCase().contains("offnet wireline"))lastMileTypeB="OffnetWL";
				else if(lastMileTypeB.toLowerCase().contains("onnet wireline"))lastMileTypeB="OnnetWL";
				else if(lastMileTypeB.toLowerCase().contains("onnetwl_npl"))lastMileTypeB="OnnetWL";
				else if(lastMileTypeB.toLowerCase().contains("man") || lastMileTypeB.toLowerCase().contains("wan aggregation"))lastMileTypeB="OnnetWL";
				if(StringUtils.isBlank(lastMileTypeB))lastMileTypeB="OnnetWL";
				
				String subProduct = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Sub Product"));
				crossConnectType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Cross Connect Type"));
				String mediaType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Media Type"));
				
				LOGGER.info("CrossConnectType {}", crossConnectType);
				LOGGER.info("MediaType {}", mediaType);
				Map<String, String> atMap = new HashMap<>();
				Map<String, String> btMap = new HashMap<>();
				String interfaceTypeA="";
				String interfaceTypeB="";
				
				if (isLMRequired) {
					LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);				

				
					processBomDetails(scServiceDetail, atMap,scComponentAttributesAMap);					
					processBomDetails(scServiceDetail, btMap,scComponentAttributesBMap);

					if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())
							&& "Passive".equalsIgnoreCase(crossConnectType)){
						LOGGER.info("MMR Passive for interface");
						if(mediaType!=null && !mediaType.isEmpty()){
							LOGGER.info("mediaType exists");
							String siteEndInterface=getInterfaceValueBasedOnMediaType(mediaType);
							processVar.put("internalCablingInterface", siteEndInterface);
							processVar.put("internalCablingInterfaceB", siteEndInterface);
							atMap.put("interfaceType", siteEndInterface);
							btMap.put("interfaceType", siteEndInterface);
						}else{
							LOGGER.error("mediaType missing for : {} ", scServiceDetail.getUuid());
						}
						if(lastMileTypeB.toLowerCase().contains("colo")) {
							LOGGER.info("MMR-Passive-COLO-Changed-OnnetWL");
							lastMileTypeB="OnnetWL";
						}
						
					}else{
						LOGGER.info("Other than MMR Passive for interface");
						
						if (siteEndInterfaceA != null) {
							interfaceTypeA = getInterfaceValue(siteEndInterfaceA);
							atMap.put("interfaceType", interfaceTypeA);
							if(siteEndInterfaceA.contains("G.957")){//G.957[Optical to Cramer, But for Cabling it is electrical]
								LOGGER.info("A end:: Converting G.957 to Electrical");
								processVar.put("internalCablingInterface", "Electrical");
								processVar.put("interface", "G.957");
							}else{
								LOGGER.info("A end::  Other than G.957");
								processVar.put("internalCablingInterface", interfaceTypeA);
								processVar.put("interface", siteEndInterfaceA);
							}
						} else {
							LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
						}
						
						

						if (siteEndInterfaceB != null) {
							interfaceTypeB = getInterfaceValue(siteEndInterfaceB);
							btMap.put("interfaceType", interfaceTypeB);
							if(siteEndInterfaceB.contains("G.957") || siteEndInterfaceB.equalsIgnoreCase("optical")){ //G.957[Optical to Cramer, But for Cabling it is electrical]
								LOGGER.info("B end:: Converting G.957 to Electrical");
								processVar.put("internalCablingInterfaceB", "Electrical");
								processVar.put("interfaceB", "G.957");
								btMap.put("interface", "G.957");
							}else{
								LOGGER.info("B end::  Other than G.957");
								processVar.put("internalCablingInterfaceB", interfaceTypeB);
								processVar.put("interfaceB", siteEndInterfaceB);
							}
						} else {
							LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
						}
					}
					atMap.put("isBothSiteSameInterface", "No");
					btMap.put("isBothSiteSameInterface", "No");
					if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName()) 
							&& processVar.containsKey("internalCablingInterface")
							&& processVar.containsKey("internalCablingInterfaceB")) {
						LOGGER.info("MMR Interface");
						String interfaceA = StringUtils
								.trimToEmpty((String) processVar.get("internalCablingInterface"));
						String interfaceB = StringUtils
								.trimToEmpty((String) processVar.get("internalCablingInterfaceB"));
						if (!interfaceA.isEmpty() && !interfaceB.isEmpty()
								&& interfaceA.equalsIgnoreCase(interfaceB)) {
							LOGGER.info("MMR both side same interface for Site A::{} and Site B::{}",interfaceA,interfaceB);
							processVar.put("isBothSiteSameInterface", "Yes");
							atMap.put("isBothSiteSameInterface", "Yes");
							btMap.put("isBothSiteSameInterface", "Yes");
						}else{
							LOGGER.info("MMR both side different for Site A::{} and Site B::{}",interfaceA,interfaceB);
							processVar.put("isBothSiteSameInterface", "No");
						}
					}
					if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())){
						atMap.put("permissionRequired", "IBD");
						btMap.put("permissionRequired", "IBD");
					}
				}

				// Lastmile B
				String lmScenarioTypeB = "";
				String lmConnectionTypeB="";
				 if (lastMileTypeB.toLowerCase().contains("offnetwl")) {
					 lmScenarioTypeB = "Offnet Wireline";
					 lmConnectionTypeB = "Wireline";
					lmType = "offnet";
					lastMileTypeB="OffnetWL";
				} else {
					lmScenarioTypeB = "Onnet Wireline";
					lmConnectionTypeB = "Wireline";
					lmType = "onnet";
					lastMileTypeB="OnnetWL";
					if (connectedCustomerB.contains("1") 
							|| siteEndInterfaceB.contains("G.957")
							|| "Hybrid NPL".equalsIgnoreCase(subProduct) 
							|| customerUserName.contains("Tata Teleservices")
							|| interfaceTypeB.contains("Optical")
							|| connectedCustomerCategoryB.equalsIgnoreCase("Connected Customer") ) {
						processVar.put(IS_CONNECTED_SITE_B, true);
						processVar.put(IS_CONNECTED_BUILDING_B, false);
						
						rowRequired = false;
						prowRequired = false;
						lmScenarioTypeB = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingB.contains("1")) {
						rowRequired = false;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put(IS_CONNECTED_BUILDING_B, true);
						processVar.put(IS_CONNECTED_SITE_B, false);
						processVar.put("isNetworkSelectedPerBOPB", true);
						lmScenarioTypeB = "Onnet Wireline - Connected Building";
					} else {
						rowRequired = true;
						prowRequired = true;
						processVar.put(IS_CONNECTED_BUILDING_B, false);
						processVar.put(IS_CONNECTED_SITE_B, false);
						processVar.put("isNetworkSelectedPerBOPB", true);
						lmScenarioTypeB = "Onnet Wireline - Near Connect";
					}
				}
				processVar.put("isMuxInfoAvailable", false);
				processVar.put("txRequired", true);
				processVar.put("isMuxIPAvailable", false);
				processVar.put("rowRequiredB", rowRequired);
				processVar.put("prowRequiredB", prowRequired);
				processVar.put(LM_TYPE_B,lmType);
				processVar.put(LM_CONNECTION_TYPE_B,lmConnectionTypeB);

				if (lastMileTypeA.toLowerCase().contains("offnet")) {
					// TODO: check if P2P
					// isColoRequired = true;
					String source = StringUtils.trimToEmpty(scComponentAttributesBMap.get("sourceState"));
					String destination = StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationState"));	
					LOGGER.info("getDestinationState:{} and getSourceState:{}", source, destination);

					if (destination != null && source != null && !destination.equalsIgnoreCase(source)) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);

					}
					Double supplierLocalLoopBwDouble = 0.0;
					String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
					
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}
					// check NRC >2 Lacks or Bandwidth >100 Mbps
					LOGGER.info("Total NRC Loaded is {} and Bandwidth is {} ", scServiceDetail.getNrc(), supplierLocalLoopBwDouble);
					Double offnetWlNrc = 0.0;							
					if(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")!=null)  offnetWlNrc= Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf")!=null) offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and Bandwidth is {} ", offnetWlNrc,offnetRFNrc, supplierLocalLoopBwDouble);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000 )|| supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true {}",scServiceDetail.getUuid());
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}

				LOGGER.info(
						"Input processNPLL2ODataToFlowable received for lmScenarioType::{}  cpeManagementType::{} cpeType::{} isTaxAvailable::{} serviceId:: {} service code:: {} subProduct::{} interfaceTypeA::{} interfaceTypeB::{}",
						lmScenarioType, cpeManagementType, "", isTaxAvailable, serviceId,
						scServiceDetail.getUuid(),subProduct,interfaceTypeA,interfaceTypeB);
				
				String cpeSiScope = "Unmanagged";
				processVar.put("isCPEConfiguredByCustomer", true);
				processVar.put("isCPEArrangedByCustomer", true);
				processVar.put("cpeSiScope", cpeSiScope);	

				processVar.put("documentValidationRequired", true);
				processVar.put("isLMRequired", isLMRequired);
				processVar.put("e2eServiceTestingCompleted", false);
				processVar.put("siteReadinessStatus", true);
				processVar.put("siteReadinessConfirmation", false);
				processVar.put("hasOSPCapexDeviation", false);
				processVar.put("hasIBDCapexDeviation", false);
				processVar.put("ibdContractRequired", true);
				processVar.put("isMuxRequired", false);
				processVar.put("isCPERequired", isCPERequired);
				processVar.put("createServiceCompleted", false);
				processVar.put("isCLRSyncCallSuccess", false);
				processVar.put("serviceDesignCompleted", false);
				processVar.put("txConfigurationCompleted", false);
				processVar.put("serviceConfigurationCompleted", false);
				processVar.put("isStandardCPEDiscount", true);

				processVar.put("isRFRequired", isRFRequired);
				processVar.put("isColoRequired", isColoRequired);
				processVar.put("failoverTestingRequired", false);
				processVar.put("additionalTechDetailsTaskCompleted", false);
				processVar.put("siteReadinessTaskCompleted", false);
				processVar.put("isValidDocuments", false);
				processVar.put("isSoftLoopPossibleAtCE", false);
				processVar.put("lmTestOnnetWirelessSuccess", true);
				processVar.put("isPEInternalCablingRequired", false);
				processVar.put("isCEInternalCablingRequired", true);
				processVar.put("cableSwappingPERequired", "No");
				processVar.put("cableSwappingCERequired", "No");
				processVar.put("internalCablingResponsibility", "TCL");
				//processVar.put("internalCablingInterface", "");
				processVar.put("serviceDeliveryPlanReady", false);
				processVar.put("order_enrichment_complete", false);
				processVar.put("getIpServiceSyncCallCompleted", false);
				processVar.put("validateBtsStatus", false);

				processVar.put("confirmOrderRequired", true);
				if (scServiceDetail.getIsAmended() != null
						&& scServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
					processVar.put("isAmendedOrder", true);

				}
				
				processVar.put("serviceConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("serviceConfigurationMessageSent", false);
				processVar.put("serviceConfigurationAckSuccess", false);
				processVar.put("serviceConfigurationSuccess", false);
				processVar.put("txConfigurationMessageSent", false);
				processVar.put("txConfigurationAckSuccess", false);
				processVar.put("txConfigurationSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txMPLSConfigurationSuccess", false);
				processVar.put("rfConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("rfConfigurationMessageSent", false);
				processVar.put("rfConfigurationAckSuccess", false);
				processVar.put("rfConfigurationSuccess", false);

				processVar.put("previewIpConfigMessageSent", false);
				processVar.put("previewIpConfigAckSuccess", false);
				processVar.put("previewIpConfigSuccess", false);
				processVar.put("cancelIpConfigMessageSent", false);
				processVar.put("cancelIpConfigAckSuccess", false);
				processVar.put("cancelIpConfigSuccess", false);
				processVar.put("txManualConfigRequired", false);
				processVar.put("sameDayRFInstallation", false);
				

				processVar.put("cpeLicensePRCompleted", true);
				processVar.put("cpeHardwarePRCompleted", true);
				processVar.put("isCpeAvailableInInventory", false);
				processVar.put("cpeLicenseNeeded", true);
				processVar.put("remainderCycle", reminderCycle);
				processVar.put("deemedAcceptanceDuration", "PT48H");
				processVar.put("ipTerminateConfigurationSuccess",false); //Added as it throws error from billing for parallel case

				processVar.put("siteAType", "A");
				processVar.put("siteBType", "B");
				processVar.put("site_type", "A");

				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				

				processVar.put(SITE_ADDRESS,StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				processVar.put(CITY,StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME,StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
		
				processVar.put(SITE_ADDRESS_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(CITY_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactName")));
				processVar.put(SITE_ADDRESS_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(STATE_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationState")));
				processVar.put(COUNTRY_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCountry")));
				
				atMap.put("cpeType","Customer provided");
				atMap.put("cpeSiScope","Customer provided");
				btMap.put("cpeType","Customer provided");
				btMap.put("cpeSiScope","Customer provided");
				
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);				
				btMap.put("lastMileScenario", lmScenarioTypeB);
				btMap.put("lmConnectionType", lmConnectionTypeB);
				atMap.put("lmType", lastMileTypeA);
				btMap.put("lmType", lastMileTypeB);
				
				ScOrderAttribute poDate = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);
				
				if(poDate!=null && poNumber!=null && poDate.getAttributeValue()!=null && poNumber.getAttributeValue()!=null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				LOGGER.info("lmType A= {} lmType B={}", lastMileTypeA,lastMileTypeB);	
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");
				
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), btMap,
						AttributeConstants.COMPONENT_LM, "B");

			}
			processVar.put("processType", "computeProjectPLan");

            if(scComponentAttributesAMap.get("destinationCity")!=null && !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
                processVar.put("tier",setTierValue(scComponentAttributesAMap.get("destinationCity")));
            }
            if(scComponentAttributesBMap.get("destinationCity")!=null && !scComponentAttributesBMap.get("destinationCity").isEmpty()) {
                processVar.put("tierB",setTierValue(scComponentAttributesBMap.get("destinationCity")));
            }
	
			//runtimeService.startProcessInstanceByKey("npl-service-fulfilment-handover-workflow", processVar);
			processVar.put(IS_PORT_CHANGED, true);
			processVar.put("demoOrder", "N");
			processVar.put("isDemoOrderBillable", "N");
			processVar.put("demoDays", 0);
			processVar.put("multiVrfBillingRequiredSkip", "No");
			if(StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()).equalsIgnoreCase("MMR Cross Connect")){
				LOGGER.info("NEW Order for NPL MMR Cross Connect::{}",scServiceDetail.getUuid());
				if(crossConnectType!=null){
					if("Active".equalsIgnoreCase(crossConnectType)){
						LOGGER.info("Active Cross Connect");
						if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")) {
							processVar.put("O2CPROCESSKEY", "npl-crossconnect-active-service-fulfilment-handover-workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is npl-crossconnect-active-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("npl-crossconnect-active-service-fulfilment-handover-workflow", processVar);
						}else {
							processVar.put("O2CPROCESSKEY", "npl-macd-crossconnect-active-service-fulfilment-handover-workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is npl-macd-crossconnect-active-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("npl-macd-crossconnect-active-service-fulfilment-handover-workflow", processVar);
						}
					}else if("Passive".equalsIgnoreCase(crossConnectType)){
						LOGGER.info("Passive Cross Connect");
						if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")) {
							processVar.put("O2CPROCESSKEY", "npl-crossconnect-passive-service-fulfilment-handover-workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is npl-crossconnect-passive-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("npl-crossconnect-passive-service-fulfilment-handover-workflow", processVar);
						}else {
							processVar.put("O2CPROCESSKEY", "npl-macd-crossconnect-passive-service-fulfilment-handover-workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is npl-macd-crossconnect-passive-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("npl-macd-crossconnect-passive-service-fulfilment-handover-workflow", processVar);
						}
					}
				}
			}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(scServiceDetail.getOrderSubCategory())
							&& StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("parallel"))){
				LOGGER.info("NEW or ADD SITE or parallel::{}",scServiceDetail.getUuid());
				processVar.put("O2CPROCESSKEY", "npl-service-fulfilment-handover-workflow-jun20");
				runtimeService.startProcessInstanceByKey("npl-service-fulfilment-handover-workflow-jun20", processVar);
			}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(scServiceDetail.getOrderSubCategory())
					&& (StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("lm") 
							|| StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("bso")
							|| StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).equalsIgnoreCase("Shifting"))){
				LOGGER.info("NPL LM::{}",scServiceDetail.getUuid());
				processVar.put("isMuxIPAvailable", false);
				processVar.put("O2CPROCESSKEY", "npl-bso-service-fulfilment-handover-workflow-jun20");
				LOGGER.info("flowable trigerred for  Service id::{} is npl-bso-service-fulfilment-handover-workflow-jun20",scServiceDetail.getUuid());
				runtimeService.startProcessInstanceByKey("npl-bso-service-fulfilment-handover-workflow-jun20", processVar);
			}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
					&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
							|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("others"))){
				LOGGER.info("CB or Others::{}",scServiceDetail.getUuid());
				processVar.put(IS_CONNECTED_SITE, true);				
				processVar.put(IS_CONNECTED_SITE_B, true);
				processVar.put(IS_CONNECTED_BUILDING, false);
				processVar.put(IS_CONNECTED_BUILDING_B, false);		
				processVar.put(IS_PORT_CHANGED, false);
				processVar.put("isMuxIPAvailable", false);
				processVar.put("O2CPROCESSKEY", "npl-macd-service-fulfilment-handover-workflow-jun20");
				LOGGER.info("flowable trigerred for  Service id::{} is npl-macd-service-fulfilment-handover-workflow-jun20",scServiceDetail.getUuid());
				runtimeService.startProcessInstanceByKey("npl-macd-service-fulfilment-handover-workflow-jun20", processVar);
			}
			
			if(welcomeMailTrigger) {
			notifyCustomerForNPL(scServiceDetail, scContractInfo, scComponentAttributesAMap);
			}

			LOGGER.info("processL2ODataToFlowable completed for NPL");
		} catch (Exception e) {

			LOGGER.error("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}


	
	
	
	
	
	@Transactional(readOnly=false)
	public Boolean processL2ODataToFlowableWorkaround(Integer serviceId,ScServiceDetail scServiceDetail,boolean survey, boolean clrFlow,
			boolean acceptance,boolean exceptionalFlow,boolean offnetClrFlow,boolean cpeConfigurationFlow,boolean clrMacdFlow,boolean nplClrFlow, 
			boolean offnetPostActivationFlow, boolean mastFlow,boolean p2pRfConfigFlow,boolean standaloneCPEflow,boolean pmpConfigFlow,
			boolean p2pTaskConfigFlow,boolean cpeInstallFlow,boolean deliverCpeFlow, boolean offnetHotUpgradeFlow, boolean orderDeliverCpeFlow, 
			boolean p2pLmTestFlow, boolean offnetMacdClrFlow,boolean cpeInstallMacdFlow, boolean casdNewFlow, boolean txConfigurationFlow, boolean offnetOrderFlow) {
		Boolean status = true;
		boolean isP2PwithoutBH = false;
		boolean isP2PwithBH = false;
		String lmType = "onnet";
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}
			LOGGER.info("Input processL2ODataToFlowableWorkaround received for L2O serviceId:: {} service code:: {}",serviceId, scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			
			if(scServiceDetail.getErfPrdCatalogProductName().toLowerCase().contains("npl") || scOrder.getOpOrderCode().toLowerCase().contains("npl")) {
				return processNPLWorkaround(serviceId, scServiceDetail,nplClrFlow,false,false,false,acceptance,survey);
			}
			
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = scContractInfos.stream().findFirst().orElse(null);

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ",scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",scServiceAttribute.getId(),scServiceAttribute.getAttributeName(),scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}

			Map<String, Object> processVar = new HashMap<>();
			
			 String supplierOldlocalLoopBw="0.0";
             if(feasibilityAttributes.containsKey("old_Ll_Bw")){
             	supplierOldlocalLoopBw=feasibilityAttributes.get("old_Ll_Bw");
             }
			
			
			String oldEquipmentModel=null;
			String orderSubCategory = StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory());
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

			if("MACD".equalsIgnoreCase(orderType) && !"ADD_SITE".equals(orderCategory)
					&& !orderSubCategory.toLowerCase().contains("parallel") && scServiceDetail.getServiceLinkId()!=null){
				LOGGER.info("MACD:: Equipment Model Attr retrival for parent Id::{}",scServiceDetail.getServiceLinkId());
				ScServiceAttribute eqpModelAttr=scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(scServiceDetail.getServiceLinkId(), "EQUIPMENT_MODEL");
				if(eqpModelAttr!=null && eqpModelAttr.getAttributeValue()!=null && !eqpModelAttr.getAttributeValue().isEmpty()){
					LOGGER.info("Equipment Model Attr exists with value::{}",eqpModelAttr.getAttributeValue());
					oldEquipmentModel=eqpModelAttr.getAttributeValue();
				}
			}

			if (scOrder.getErfCustCustomerName() != null) {
				processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
			}
			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				processVar.put("parentServiceCode", null);
				processVar.put("isParallelExists", "false");
				processVar.put("isAmendedOrder", false);
				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())){
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				}else{
					processVar.put("orderSubCategory", "NA");
				}
				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory()) && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){
					LOGGER.info("ProcessL2O:ParallelExists");
					processVar.put("isParallelExists", "true");
					if(Objects.nonNull(scServiceDetail.getParentUuid())){
						processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
					}
					/*ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(), "downtime_duration",
									"IAS Common");
					if(Objects.nonNull(scServiceDownTimeAttr) && Objects.nonNull(scServiceDownTimeAttr.getAttributeValue()) && !scServiceDownTimeAttr.getAttributeValue().isEmpty()){
						LOGGER.info("ProcessL2O:ParallelDownTime");
						processVar.put("parallelDownTime", "PT"+scServiceDownTimeAttr.getAttributeValue()+"D");
					}else{
						LOGGER.info("ProcessL2O:ParallelDownTime is 0");
						processVar.put("parallelDownTime", "PT0M");
					}*/
				}
			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

			if (scServiceDetail != null) {
				
				//GVPN:UPDATE SITE TYPE BASED ON VPN TOPOLOGY
				if("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())){
					LOGGER.info("update vpn details");
					ScServiceAttribute siteTypeServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"Site Type","GVPN Common");
					if(Objects.nonNull(siteTypeServiceAttribute)){
						ScServiceAttribute gvpnCommonServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"VPN Topology","GVPN Common");
						if(Objects.nonNull(gvpnCommonServiceAttribute)){
							LOGGER.info("VPN common");
							updateVpnDetail(gvpnCommonServiceAttribute,siteTypeServiceAttribute);
						}
						ScServiceAttribute sitePropServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"VPN Topology","SITE_PROPERTIES");
						if(Objects.nonNull(sitePropServiceAttribute)){
							LOGGER.info("VPN properties");
							updateVpnDetail(sitePropServiceAttribute,siteTypeServiceAttribute);
						}
					}
				}
				

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				String lastMileType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"));

				String connectedBuilding = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_Building_tag"));
				String connectedCustomer = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_cust_tag"));
				String btsDeviceType = StringUtils.trimToEmpty(feasibilityAttributes.get("bts_device_type")).toLowerCase();
				
				String solutionType = StringUtils.trimToEmpty(feasibilityAttributes.get("solution_type")).toLowerCase();
				String cpeChassisChanged = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_chassis_changed"));
				String cpeVariant = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_variant"));
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));
				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));
				
				String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
				
				LOGGER.info("cpeType for serviceid :{} and service code:{} and map:: {} cpeChassisChanged::{} cpeVariant::{}", serviceId,scServiceDetail.getUuid(),serviceDetailsAttributes,cpeChassisChanged,cpeVariant);


				String cpeType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE"));
				String cpeModel = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE Basic Chassis"));
				String serviceType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Service type"));
				
				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{} serviceType={}", serviceId,scServiceDetail.getUuid(),cpeType,cpeModel,serviceType);
				
				if(StringUtils.isNotBlank(serviceType) && serviceType.toLowerCase().contains("usage")) {
					processVar.put("burstableOrder", true);
				}
				
				String cpeSiScope = "Customer provided";
				if (cpeType.contains("Rental")) {
					cpeType ="Rental";
					processVar.put("cpeType",cpeType );
				} else if (cpeType.contains("Outright")) {
					cpeType ="Outright";
					processVar.put("cpeType", cpeType);
				} else {
					processVar.put("cpeType", cpeType);
				}

				boolean isLMRequired = true;
				boolean isColoRequired = false;
				boolean negotiationRequiredForOffnetLM = false;
				boolean rowRequired = true;
				boolean prowRequired = true;
				boolean isRFRequired = false;
				

				String bhConnectivity = StringUtils.trimToEmpty(feasibilityAttributes.get("BHConnectivity"));				
				String providerName = StringUtils.trimToEmpty(feasibilityAttributes.get("closest_provider_bso_name"));
				
				processVar.put(IS_CONNECTED_SITE, false);
				processVar.put(IS_CONNECTED_BUILDING, false);
				processVar.put("isP2PwithoutBH", isP2PwithoutBH);
				processVar.put("isP2PwithBH", isP2PwithBH);
				processVar.put("parallelDownTime", "PT30M");

				
				
				
				if(lastMileType.toLowerCase().contains("onnet wireless"))lastMileType="OnnetRF";
				else if(lastMileType.toLowerCase().contains("offnet wireless"))lastMileType="OffnetRF";
				else if(lastMileType.toLowerCase().contains("offnet wireline"))lastMileType="OffnetWL";
				else if(lastMileType.toLowerCase().contains("onnet wireline"))lastMileType="OnnetWL";
				else if(lastMileType.toLowerCase().contains("man") || lastMileType.toLowerCase().contains("wan aggregation"))lastMileType="OnnetWL";

				if(StringUtils.isBlank(lastMileType))lastMileType="OnnetWL";
				String upgradeType = StringUtils.trimToEmpty(feasibilityAttributes.get("upgrade_type"));
				
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileType.toLowerCase().contains("onnetrf") || lastMileType.toLowerCase().equalsIgnoreCase("Onnet Wireless")) {
                    lmScenarioType = "Onnet Wireless";
                    lmConnectionType = "Wireless";
                    lmType = "onnet";
                    processVar.put("sameDayInstallation", false);
                    processVar.put("rfSiteFeasible", true);
                    processVar.put("mastApprovalRequired", true);
                    
                    
                    if(providerName.toLowerCase().contains("tcl") && providerName.toLowerCase().contains("radwin")) {
                    	isP2PwithoutBH = true;
                    	isColoRequired = true;
                    }else if(providerName.toLowerCase().contains("backhaul") && providerName.toLowerCase().contains("radwin")) {
                        isP2PwithBH = true;
                        isColoRequired = true;
                     }
                    
                    processVar.put("isP2PwithoutBH", isP2PwithoutBH);
    				processVar.put("isP2PwithBH", isP2PwithBH);
    				
    				if(StringUtils.isBlank(bhConnectivity)) {
    					if(providerName.equalsIgnoreCase("Radwin from TCL POP")) {
    						bhConnectivity="Radwin from TCL POP";
    					}
    				}
                    
    				if(providerName.toUpperCase().contains("RADWIN") || btsDeviceType.contains("radwin") || solutionType.contains("radwin")) {
	                    if(StringUtils.isNotBlank(bhConnectivity) && !(bhConnectivity.equalsIgnoreCase("NA") || bhConnectivity.equalsIgnoreCase("ONNET") || bhConnectivity.contains("TCL"))) {
	                           processVar.put("txRequired", true);
	                    }else{
	                           processVar.put("txRequired", false);
	                    }
    				}else {
    					 processVar.put("txRequired", false);
    				}
                    processVar.put("skipOffnet", false);
				} else if (lastMileType.toLowerCase().contains("offnetrf") || lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireless")) {
                    lmScenarioType = "Offnet Wireless";
                    lmConnectionType = "Wireless";
                    lmType = "offnet";
                    processVar.put("sameDayInstallation", false);
                    processVar.put("rfSiteFeasible", true);
                    processVar.put("mastApprovalRequired", true);
                    processVar.put("txRequired", true);
                    
                    if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
							|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
							Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
							&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
                    				
							|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
							Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
							&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {
                    	
                    	if(StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
                    		LOGGER.info("SkipOffnet False");
                        	processVar.put("skipOffnet", false);
                    	}else {
                    		LOGGER.info("SkipOffnet True");                    	
	                    	processVar.put("skipOffnet", true);                   		
                    	}
                    }else{
                    	LOGGER.info("SkipOffnet False");
                    	processVar.put("skipOffnet", false);
                    }
				}else if (lastMileType.toLowerCase().contains("offnetwl") || lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireline")) {
                    lmScenarioType = "Offnet Wireline";

				
					lmConnectionType = "Wireline";
					lmType = "offnet";
					processVar.put("isMuxIPAvailable", false);
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					 if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
	                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
								|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
	                    				
								|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {
						 
						if(StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
                    		LOGGER.info("SkipOffnet False");
                        	processVar.put("skipOffnet", false);
                    	}else {
                    		LOGGER.info("SkipOffnet True");                    	
	                    	processVar.put("skipOffnet", true);                    		
                    	}
                    }else{
                    	LOGGER.info("SkipOffnet False");
                    	processVar.put("skipOffnet", false);
                    }
				} else {
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					processVar.put("skipOffnet", false);
					
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					if(connectedCustomer.contains("1") || (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).equalsIgnoreCase("Hot Upgrade")
                                    &&  StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).equalsIgnoreCase("Hot Downgrade"))
									|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))) {
						connectedCustomer="1";
						processVar.put(IS_CONNECTED_SITE, true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put("isMuxIPAvailable", false);
						rowRequired = false;
						prowRequired = false;
						lmScenarioType = "Onnet Wireline - Connected Customer";
					} else if (connectedBuilding.contains("1")) {
						rowRequired = false;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put(IS_CONNECTED_BUILDING, true);
						processVar.put(IS_CONNECTED_SITE, false);
						processVar.put("isNetworkSelectedPerBOP", true);
						lmScenarioType = "Onnet Wireline - Connected Building";
					} else {
						rowRequired = true;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put(IS_CONNECTED_SITE, false);
						processVar.put("isNetworkSelectedPerBOP", true);
						lmScenarioType = "Onnet Wireline - Near Connect";
					}
				}
				

				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);
				processVar.put("bhConnectivity", bhConnectivity);
				if (lastMileType.toLowerCase().contains("offnet")) {
					// TODO: check if P2P
					// isColoRequired = true;

					LOGGER.info("getDestinationState:{} and getSourceState:{}", scComponentAttributesAMap.get("destinationState"),
							scComponentAttributesAMap.get("sourceState"));

					if (scComponentAttributesAMap.get("destinationState") != null && scComponentAttributesAMap.get("sourceState") != null
							&& !scComponentAttributesAMap.get("destinationState")
									.equalsIgnoreCase(scComponentAttributesAMap.get("sourceState"))) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);

					}

					Double supplierLocalLoopBwDouble = 0.0;
					
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}
					// check NRC >1 Lacks or Bandwidth >100 Mbps
					LOGGER.info("Total NRC Loaded is {} and Bandwidth is {} ", scServiceDetail.getNrc(), supplierLocalLoopBwDouble);
					Double offnetWlNrc = 0.0;							
					if(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")!=null)  offnetWlNrc= Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf")!=null) offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and Bandwidth is {} ", offnetWlNrc,offnetRFNrc, supplierLocalLoopBwDouble);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000 )|| supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true {}",scServiceDetail.getUuid());
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}

				LOGGER.info(
						"Input processL2ODataToFlowable received for lmScenarioType::{}  cpeManagementType::{} cpeType::{} isTaxAvailable::{} btsDeviceType::{} serviceId:: {} service code:: {}",
						lmScenarioType, cpeManagementType, cpeType, isTaxAvailable, btsDeviceType,serviceId, scServiceDetail.getUuid());
				
				String offeringName = StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName());
				
				/*if(StringUtils.isBlank(cpeModel)) {
					LOGGER.info("cpeModel is blank setting cpeManagementType to Unmanaged but actual cpeManagementType is {} serviceCode={} orderCode={}",cpeManagementType,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid());
					cpeManagementType="Unmanaged";
				}
				if(StringUtils.trimToEmpty(scOrder.getOrderType()).equalsIgnoreCase("MACD")
						&& (StringUtils.trimToEmpty(scOrder.getOrderCategory()).equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| StringUtils.trimToEmpty(scOrder.getOrderCategory()).equalsIgnoreCase("ADD_IP"))) {
					if("Yes".equalsIgnoreCase(cpeChassisChanged) && StringUtils.isNotBlank(cpeVariant) && !cpeVariant.equalsIgnoreCase("None") && !cpeVariant.equalsIgnoreCase("null") && !cpeVariant.equalsIgnoreCase("NA")) {
						LOGGER.info("MACD cpeModelchange cpeVariant={} cpeChassisChanged={} serviceCode={} orderCode={} cpeManagementType={}",cpeVariant,cpeChassisChanged,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid(),cpeManagementType);
					}else {
						LOGGER.info("MACD cpeManagementType={} cpeChassisChanged={} cpeVariant={} serviceCode={} orderCode={}",cpeManagementType,cpeChassisChanged,cpeVariant,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid());
						
						cpeManagementType="Unmanaged";
					}
				}*/

				if ("Fully Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation, Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Unmanaged".equalsIgnoreCase(cpeManagementType) || offeringName.toLowerCase().contains("unmanaged")) {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Physically Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Configuration Management".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", true);
				} else if ("Proactive Services".equalsIgnoreCase(cpeManagementType)) {							
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
				}else {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				}
				
				if(!orderDeliverCpeFlow && !standaloneCPEflow && !deliverCpeFlow && !cpeInstallFlow && !cpeInstallMacdFlow) {
					if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("SHIFT_SITE")
									|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) {
						if("Yes".equalsIgnoreCase(cpeChassisChanged) && StringUtils.isNotBlank(cpeVariant) && !cpeVariant.equalsIgnoreCase("None") && !cpeVariant.equalsIgnoreCase("null") && !cpeVariant.equalsIgnoreCase("NA")) {
							LOGGER.info("MACD cpeModelchange cpeVariant={} cpeChassisChanged={} serviceCode={} orderCode={} cpeManagementType={}",cpeVariant,cpeChassisChanged,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid(),cpeManagementType);
							if(StringUtils.isNotBlank(oldEquipmentModel) && cpeVariant.toLowerCase().contains(oldEquipmentModel.toLowerCase())){
								LOGGER.info("Old and Current Cpe Model matched");
								processVar.put("isCPEConfiguredByCustomer", true);
								processVar.put("isCPEArrangedByCustomer", true);
								processVar.put("cpeSiScope", "");
								Map<String, String> attrMap= new HashMap<>();
								attrMap.put("cpe_chassis_changed", "No");
								componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
							}else{
								LOGGER.info("Old and Current Cpe Model not matched");
							}
						}else {
							if (!"Unmanaged".equalsIgnoreCase(cpeManagementType) || !offeringName.toLowerCase().contains("unmanaged")) {
								LOGGER.info("STOPPINGCPEFLOW=>MACD cpeManagementType={} cpeChassisChanged={} cpeVariant={} serviceCode={} orderCode={}",cpeManagementType,cpeChassisChanged,cpeVariant,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid());
								processVar.put("isCPEConfiguredByCustomer", true);
								processVar.put("isCPEArrangedByCustomer", true);
								processVar.put("cpeSiScope", "");
							}
							
						}
					}
				}

				processVar.put("documentValidationRequired", true);
				processVar.put("isLMRequired", isLMRequired);
				processVar.put("e2eServiceTestingCompleted", false);
				processVar.put("siteReadinessStatus", true);
				processVar.put("siteReadinessConfirmation", false);
				processVar.put("rowRequired", rowRequired);
				processVar.put("prowRequired", prowRequired);
				processVar.put("hasOSPCapexDeviation", false);
				processVar.put("hasIBDCapexDeviation", false);
				processVar.put("ibdContractRequired", true);
				processVar.put("isMuxRequired", false);
				processVar.put("isCPERequired", true);
				processVar.put("createServiceCompleted", false);
				processVar.put("isCLRSyncCallSuccess", false);
				processVar.put("serviceDesignCompleted", false);
				processVar.put("txConfigurationCompleted", false);
				processVar.put("serviceConfigurationCompleted", false);
				processVar.put("isStandardCPEDiscount", true);
							
				
				processVar.put("isRFRequired", isRFRequired);
				processVar.put("isColoRequired", isColoRequired);
				processVar.put("failoverTestingRequired", false);
				processVar.put("additionalTechDetailsTaskCompleted", false);
				processVar.put("siteReadinessTaskCompleted", false);
				processVar.put("isValidDocuments", false);
				processVar.put("isSoftLoopPossibleAtCE", false);
				processVar.put("lmTestOnnetWirelessSuccess", true);
				processVar.put("isPEInternalCablingRequired", false);
				processVar.put("isCEInternalCablingRequired", true);
				processVar.put("cableSwappingPERequired", "No");
				processVar.put("cableSwappingCERequired", "No");
				processVar.put("internalCablingResponsibility", "TCL");
				processVar.put("internalCablingInterface", "");
				processVar.put("serviceDeliveryPlanReady", false);
				processVar.put("order_enrichment_complete", false);
				processVar.put("getIpServiceSyncCallCompleted", false);
				processVar.put("validateBtsStatus", false);

				processVar.put("confirmOrderRequired", true);
				if (scServiceDetail.getIsAmended() != null
						&& scServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
					processVar.put("isAmendedOrder", true);

				}
				
				processVar.put("serviceConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("serviceConfigurationMessageSent", false);
				processVar.put("serviceConfigurationAckSuccess", false);
				processVar.put("serviceConfigurationSuccess", false);
				processVar.put("txConfigurationMessageSent", false);
				processVar.put("txConfigurationAckSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txMPLSConfigurationSuccess", false);
				processVar.put("rfConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("rfConfigurationMessageSent", false);
				processVar.put("rfConfigurationAckSuccess", false);
				processVar.put("rfConfigurationSuccess", false);

				processVar.put("previewIpConfigMessageSent", false);
				processVar.put("previewIpConfigAckSuccess", false);
				processVar.put("previewIpConfigSuccess", false);
				processVar.put("cancelIpConfigMessageSent", false);
				processVar.put("cancelIpConfigAckSuccess", false);
				processVar.put("cancelIpConfigSuccess", false);
				processVar.put("txManualConfigRequired", false);
				
				processVar.put("cpeLicensePRCompleted", true);
				processVar.put("cpeHardwarePRCompleted", true);
				processVar.put("isCpeAvailableInInventory", false);	
				processVar.put("cpeLicenseNeeded", true);	
				processVar.put("remainderCycle", reminderCycle);
				processVar.put("deemedAcceptanceDuration", "PT48H");
				
				processVar.put("siteAType", "A");
				processVar.put("site_type", "A");
				
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				Map<String, String> atMap = new HashMap<>();

				if (isLMRequired) {

					if (lastMileType.toLowerCase().contains("rf")) {
						
						String antenaSize = StringUtils.trimToEmpty(feasibilityAttributes.get("Mast_3KM_avg_mast_ht"));
						String mastType = StringUtils.trimToEmpty(feasibilityAttributes.get("mast_type"));
						String structureType = mastType;
						try {
							if (StringUtils.isNotBlank(antenaSize)) {
								Double antenaSizeDouble = Double.parseDouble(antenaSize);
								if (antenaSizeDouble > 6)
									structureType = "Mast";
							}
						} catch (Exception ee) {
							LOGGER.info("Mast_3KM_avg_mast_ht issue {}", ee);

						}
						if (StringUtils.isBlank(structureType)) structureType = "Pole";
						
						atMap.put("structureType", structureType);
						processVar.put("structureType", structureType);

						if (btsDeviceType.contains("radwin") || solutionType.contains("radwin") || "Radwin from TCL POP".equalsIgnoreCase(providerName)) {
							atMap.put("rfTechnology", "RADWIN");
							atMap.put("rfMake", "Radwin");
						}else if (btsDeviceType.contains("cambium") || solutionType.contains("cambium")) {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						} else {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						} /*else if (btsDeviceType.contains("wimax") || solutionType.contains("wimax")) {
							atMap.put("rfTechnology", "WIMAX");
							atMap.put("rfMake", "Wimax");
						}*/ 
						
						atMap.put("bhProviderName", bhConnectivity);
					} else {
						LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);
						if (connectedCustomer.contains("1") && serviceDetailsAttributes.containsKey("mux_details")) {

							try {

								String paramValue = serviceDetailsAttributes.get("mux_details");

								Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
										.findById(Integer.valueOf(paramValue));
								if (scAdditionalServiceParam.isPresent()) {
									String value = scAdditionalServiceParam.get().getValue();

									MuxDetailsItem[] muxDetailBeans = Utils.convertJsonToObject(value,
											MuxDetailsItem[].class);
									MuxDetailsItem muxDetailBean = muxDetailBeans[0];

									atMap.put("endMuxNodeName", muxDetailBean.getMux());
									atMap.put("endMuxNodeIp", muxDetailBean.getMuxIp());

								}
							} catch (Exception e) {
								LOGGER.error("error in parsing mux details : {} ", scServiceDetail.getUuid());

							}
						}

						// atMap.put("endMuxNodeName", "TJ1270-178");
						// atMap.put("endMuxNodeIp", "10.133.22.178");
						// atMap.put("endMuxNodePort", "");

					}

					processBomDetails(scServiceDetail, atMap,scComponentAttributesAMap);

					if (scServiceDetail.getSiteEndInterface() != null) {
						String interfaceType = getInterfaceValue(scServiceDetail.getSiteEndInterface());
						atMap.put("interfaceType", interfaceType);
						processVar.put("internalCablingInterface", interfaceType);
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}
				}

				atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);				
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				atMap.put("providerName", providerName);
				
				LOGGER.error("lastMileType : {} ", lastMileType);
				atMap.put("lmType", lastMileType);
				
				atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				
				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));


				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if(mstStateToDistributionCenterMapping!=null && mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()!=null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
				}

				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM,"A");

			}
			processVar.put("processType", "computeProjectPLan");
			processVar.put("offnetPOEnabled", true);
			processVar.put("demoOrder", "N");
			processVar.put("isDemoOrderBillable", "N");
			processVar.put("demoDays", 0);
			processVar.put("multiVrfBillingRequiredSkip", "No");
			if(scOrder.getDemoFlag()!=null && scOrder.getDemoFlag().equalsIgnoreCase("Y")) {
				processVar.put("demoOrder", "Y");
				ScOrderAttribute billingType = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_TYPE, scOrder);
				if(billingType.getAttributeValue().equalsIgnoreCase("Billable demo")) {
					processVar.put("isDemoOrderBillable", "Y");
				}
				Map<String, String> atMap = new HashMap<>();
				processVar.put("demoDays", getDemoDays(scContractInfo.getOrderTermInMonths().toString()));
				atMap.put("demoDays",  String.valueOf(processVar.get("demoDays")));
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM,"A");
				Map<String, String> attrMap = new HashMap<>();
				attrMap.put("DEMO_PERIOD_IN_DAYS", String.valueOf(processVar.get("demoDays")));
				componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
				if(scServiceDetail.getOrderType().equalsIgnoreCase("MACD") && scServiceDetail.getOrderCategory().equalsIgnoreCase("DEMO_EXTENSION")) {
					LOGGER.info("Demo Extension Uuid::{}",scServiceDetail.getUuid());
					atMap.put("demoExtensionDays", String.valueOf(processVar.get("demoDays")));
					ScServiceDetail activeServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_codeOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if(activeServiceDetail!=null) {
						ScServiceAttribute activeDemoPeriodInDaysAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(activeServiceDetail.getId(),"DEMO_PERIOD_IN_DAYS");
						if(Objects.nonNull(activeDemoPeriodInDaysAttribute) && activeDemoPeriodInDaysAttribute.getAttributeValue()!=null){
							Integer demoPeriodInDays=Integer.valueOf(String.valueOf(processVar.get("demoDays")))+Integer.valueOf(activeDemoPeriodInDaysAttribute.getAttributeValue());
							attrMap.put("DEMO_PERIOD_IN_DAYS", String.valueOf(demoPeriodInDays));
							componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						}
						Map<String, String> compAttrMap = commonFulfillmentUtils
								.getComponentAttributesDetails(Arrays.asList("demoBillStartDate", "demoBillEndDate","demoDays"),
										activeServiceDetail.getId(), "LM", AttributeConstants.SITETYPE_A);
						if(compAttrMap!=null) {
							atMap.put("parentDemoBillStartDate", compAttrMap.get("demoBillStartDate")!=null ? compAttrMap.get("demoBillStartDate") :"NA" );
							atMap.put("demoBillStartDate", compAttrMap.get("demoBillEndDate")!=null ?compAttrMap.get("demoBillEndDate") :"NA" );
							atMap.put("parentDemoDays", compAttrMap.get("demoDays")!=null ?compAttrMap.get("demoDays") :"NA" );
						}
					}
					componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
							AttributeConstants.COMPONENT_LM,"A");
					processVar.put("O2CPROCESSKEY", "ill-demo-extension-service-fulfilment-handover-workflow");
					LOGGER.info("flowable trigerred for  Service id::{} is ill-demo-extension-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
					scServiceDetail.setWorkFlowName("ill-demo-extension-service-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("ill-demo-extension-service-fulfilment-handover-workflow", processVar);
					return status;
				}
			}
			String productName = StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName());
			String orderCode = StringUtils.trimToEmpty(scOrder.getOpOrderCode());
			
			if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))) {
				LOGGER.info("ConfirmOrder not required for Service id::{}",scServiceDetail.getId());
				processVar.put("confirmOrderRequired", false);
			}
			if(scServiceDetail.getIsAmended()!=null && scServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
				processVar.put("confirmOrderRequired", true);

			}
			if(txConfigurationFlow) {
				LOGGER.info("ill-tx-macd-configuration-workaround-workflow");
				runtimeService.startProcessInstanceByKey("ill-tx-macd-configuration-workaround-workflow", processVar);
			}else if(cpeConfigurationFlow) {
				LOGGER.info("cpe-configuration-Workaround");
				runtimeService.startProcessInstanceByKey("cpe-configuration-workaround-workflow", processVar);
			}else if(offnetHotUpgradeFlow) {
				LOGGER.info("offnet-hot-upgrade-workaround");
				runtimeService.startProcessInstanceByKey("offnet-hot-upgrade-workaround", processVar);
			}else if(offnetOrderFlow) {
				LOGGER.info("offnet-order-workaround");
				runtimeService.startProcessInstanceByKey("offnet-order-workaround", processVar);
			}else if(p2pRfConfigFlow) {
				LOGGER.info("wireless-p2p-rfconfig-workaround-workflow");
				runtimeService.startProcessInstanceByKey("wireless-p2p-rfconfig-workaround-workflow", processVar);
			}else if(p2pLmTestFlow) {
				LOGGER.info("p2p-task-lmtest-workaround-workflow");
				runtimeService.startProcessInstanceByKey("p2p-task-lmtest-workaround-workflow", processVar);
			}else if(p2pTaskConfigFlow) {
				LOGGER.info("p2p-task-workaround-workflow");
				runtimeService.startProcessInstanceByKey("p2p-task-workaround-workflow", processVar);
			}else if(orderDeliverCpeFlow) {
				LOGGER.info("standalone-cpe-order-deliver-workflow");
				runtimeService.startProcessInstanceByKey("standalone-cpe-order-deliver-workflow", processVar);
			}else if(deliverCpeFlow) {
				LOGGER.info("deliver-cpe-workflow");
				runtimeService.startProcessInstanceByKey("deliver-cpe-workflow", processVar);
			}else if(cpeInstallFlow) {
				LOGGER.info("cpe-installation-workaround-workflow");
				runtimeService.startProcessInstanceByKey("cpe-installation-workaround-workflow", processVar);
			}else if(cpeInstallMacdFlow) {
				LOGGER.info("cpe-installation-macd-workaround-workflow");
				runtimeService.startProcessInstanceByKey("cpe-installation-macd-workaround-workflow", processVar);
			}else if(pmpConfigFlow) {
				LOGGER.info("wireless-pmp-rfconfig-workaround-workflow");
				runtimeService.startProcessInstanceByKey("wireless-pmp-rfconfig-workaround-workflow", processVar);
			}else if(standaloneCPEflow) {
				LOGGER.info("standalone-cpe-workflow");
				runtimeService.startProcessInstanceByKey("standalone-cpe-workflow", processVar);
			}else if(exceptionalFlow) {
				LOGGER.info("exceptional-Workaround");
				runtimeService.startProcessInstanceByKey("exceptional-workaround-workflow", processVar);
			}else if(offnetClrFlow) {
				LOGGER.info("offnet-clr-Workaround");
				runtimeService.startProcessInstanceByKey("offnet-clr-workaround", processVar);
			}else if(offnetMacdClrFlow) {
				LOGGER.info("offnet-macd-clr-workflow");
				runtimeService.startProcessInstanceByKey("offnet-macd-clr-workflow", processVar);
			}else if(casdNewFlow) {
				LOGGER.info("casd-new-workaround");
				runtimeService.startProcessInstanceByKey("casd-new-workaround", processVar);
			}else if(clrFlow) {
				LOGGER.info("clr-Workaround");
				runtimeService.startProcessInstanceByKey("ill-clr-workaround-workflow", processVar);
			}else if(clrMacdFlow) {
				LOGGER.info("macd-clr-Workaround");
				runtimeService.startProcessInstanceByKey("clr-macd-workaround-workflow", processVar);
			}else if(nplClrFlow) {
				LOGGER.info("npl-clr-Workaround");
				runtimeService.startProcessInstanceByKey("clr-npl-workaround-workflow", processVar);
			}else if(offnetPostActivationFlow) {
				LOGGER.info("offnet-macd-post-activation-workaround");
				runtimeService.startProcessInstanceByKey("offnet-macd-post-activation-workaround", processVar);
			}else if(mastFlow) {
				LOGGER.info("offnet-mast-workaround");
				processVar.put("offnetMastRequired", "Yes");				
				runtimeService.startProcessInstanceByKey("offnet-mast-workaround", processVar);
			}else if(acceptance) {
				LOGGER.info("acceptance-Workaround");
				runtimeService.startProcessInstanceByKey("acceptance-workaround-workflow", processVar);
			}else if(survey) {
				LOGGER.info("survey-Workaround");
				runtimeService.startProcessInstanceByKey("survey-workaround-workflow", processVar);
			}else if(lmType.equals("offnet")) {
				LOGGER.info("offnet-Workaround");
				runtimeService.startProcessInstanceByKey("offnet-fulfilment-workaround", processVar);
			}else if(productName.toLowerCase().contains("npl") || orderCode.toLowerCase().contains("npl")) {
				LOGGER.info("npl-Workaround");
				runtimeService.startProcessInstanceByKey("npl-workaround-workflow", processVar);
			}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(scServiceDetail.getOrderSubCategory())
							&& StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("parallel"))){
				LOGGER.info("NEW-Workaround");
				runtimeService.startProcessInstanceByKey("ill-new-workaround-workflow", processVar);
			}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
					&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
							|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))){
				LOGGER.info("1-macd-Workaround");
				processVar.put(IS_CONNECTED_SITE, true);
				processVar.put(IS_PORT_CHANGED, false);
				processVar.put("isMuxIPAvailable", false);
				runtimeService.startProcessInstanceByKey("ill-macd-workaround-workflow", processVar);
			}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(scServiceDetail.getOrderSubCategory())
					&& (StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("lm") 
							|| StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("bso")
							|| StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).equalsIgnoreCase("Shifting"))){
				LOGGER.info("2-macd-Workaround");
				processVar.put(IS_PORT_CHANGED, true);
				processVar.put("isMuxIPAvailable", false);
				runtimeService.startProcessInstanceByKey("ill-macd-workaround-workflow", processVar);
			}
			

			LOGGER.info("processL2ODataToFlowableWorkaround completed");
		} catch (Exception e) {

			LOGGER.error("Error processing data", e);
			status = false;
		}
		return status;
	}
	
	@Transactional(readOnly=false)
	public Boolean processNPLWorkaround(Integer serviceId, ScServiceDetail scServiceDetail,boolean nplClrFlow,boolean nplMacdClrFlow,boolean nplCloudClrFlow, 
			boolean kroneFlow,boolean acceptance,boolean survey) {
		Boolean status = true;
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}
			LOGGER.info("Input processNPLL2ODataToFlowable received for L2O serviceId:: {} service code:: {}", serviceId,
					scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = scContractInfos.stream().findFirst().orElse(null);

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",
						scServiceAttribute.getId(), scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}
			
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);
			
			Map<String, String> scComponentAttributesBMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "B");
			LOGGER.info("scComponentAttributesBMap {} ",scComponentAttributesBMap);

			Map<String, Object> processVar = new HashMap<>();
			
			String customerUserName = StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()); 		
			processVar.put("customerUserName",customerUserName );
			
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				
				processVar.put("parentServiceCode", null);
				processVar.put("isParallelExists", "false");
				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())){
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				}else{
					processVar.put("orderSubCategory", "NA");
				}
				if("MACD".equalsIgnoreCase(orderType) && 
						Objects.nonNull(scServiceDetail.getOrderSubCategory()) && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){
					LOGGER.info("ProcessL2O:ParallelExists");
					processVar.put("isParallelExists", "true");
					if(Objects.nonNull(scServiceDetail.getParentUuid())){
						processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
					}
				}

			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
			String crossConnectType ="";
			if (scServiceDetail != null) {

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				String lastMileTypeA = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_a"));
				String lastMileTypeB = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_b"));
				
				String connectedBuildingA = StringUtils
						.trimToEmpty(feasibilityAttributes.get("a_connected_building_tag"));
				String connectedCustomerA = StringUtils.trimToEmpty(feasibilityAttributes.get("a_connected_cust_tag"));
				String connectedCustomerCategoryA = StringUtils.trimToEmpty(feasibilityAttributes.get("a_Orch_Category"));
								
				String connectedBuildingB = StringUtils
						.trimToEmpty(feasibilityAttributes.get("b_connected_building_tag"));
				String connectedCustomerB = StringUtils.trimToEmpty(feasibilityAttributes.get("b_connected_cust_tag"));
				
				String connectedCustomerCategoryB = StringUtils.trimToEmpty(feasibilityAttributes.get("b_Orch_Category"));
				
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));
				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));

				LOGGER.info("cpeType for serviceid :{} and service code:{} and map:: {}", serviceId,
						scServiceDetail.getUuid(), serviceDetailsAttributes);
			
				processVar.put("cpeType", "");
			

				boolean isLMRequired = true;
				boolean isColoRequired = false;
				boolean negotiationRequiredForOffnetLM = false;
				boolean rowRequired = true;
				boolean prowRequired = true;
				boolean isRFRequired = false;
				boolean isCPERequired = true;

				processVar.put(IS_CONNECTED_SITE, false);
				processVar.put(IS_CONNECTED_BUILDING, false);
				processVar.put("checkCLRSuccess", false);
				processVar.put("isAccountRequired", false);

				
				

				
				if(lastMileTypeA.toLowerCase().contains("onnet wireless"))lastMileTypeA="OnnetRF";
				else if(lastMileTypeA.toLowerCase().contains("offnet wireless"))lastMileTypeA="OffnetRF";
				else if(lastMileTypeA.toLowerCase().contains("offnet wireline"))lastMileTypeA="OffnetWL";
				else if(lastMileTypeA.toLowerCase().contains("onnet wireline"))lastMileTypeA="OnnetWL";
				else if(lastMileTypeA.toLowerCase().contains("onnetwl_npl"))lastMileTypeA="OnnetWL";
				else if(lastMileTypeA.toLowerCase().contains("man") || lastMileTypeA.toLowerCase().contains("wan aggregation"))lastMileTypeA="OnnetWL";
				
				if(StringUtils.isBlank(lastMileTypeA))lastMileTypeA="OnnetWL";
				
				if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())
						&& "Passive".equalsIgnoreCase(crossConnectType)){
			
					if(lastMileTypeA.toLowerCase().contains("colo")) {
						LOGGER.info("MMR-Passive-COLO-Changed-OnnetWL");
						lastMileTypeA="OnnetWL";
					}
				}
								
				
				String lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileTypeA.toLowerCase().contains("offnetwl")) {
					lmScenarioType = "Offnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "offnet";
					lastMileTypeA="OffnetWL";
				} else {
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileTypeA="OnnetWL";
					if (connectedCustomerA.contains("1") || connectedCustomerCategoryA.equalsIgnoreCase("Connected Customer")) {
						processVar.put(IS_CONNECTED_SITE, true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						rowRequired = false;
						prowRequired = false;
						lmScenarioType = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingA.contains("1")) {
						rowRequired = false;
						prowRequired = true;
						processVar.put(IS_CONNECTED_BUILDING, true);
						processVar.put(IS_CONNECTED_SITE, false);
						processVar.put("isNetworkSelectedPerBOP", true);
						lmScenarioType = "Onnet Wireline - Connected Building";
					} else {
						rowRequired = true;
						prowRequired = true;
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put(IS_CONNECTED_SITE, false);
						processVar.put("isNetworkSelectedPerBOP", true);
						lmScenarioType = "Onnet Wireline - Near Connect";
					}

				}
				processVar.put("rowRequired", rowRequired);
				processVar.put("prowRequired", prowRequired);
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE,lmConnectionType);

				if (lastMileTypeA.toLowerCase().contains("offnet")) {
					// TODO: check if P2P
					// isColoRequired = true;
					String source = StringUtils.trimToEmpty(scComponentAttributesAMap.get("sourceState"));
					String destination = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState"));					
				
					LOGGER.info("getDestinationState:{} and getSourceState:{}", source, destination);

					if (destination != null && source != null && !destination.equalsIgnoreCase(source)) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);

					}

					Double supplierLocalLoopBwDouble = 0.0;
					String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}
					// check NRC >1 Lacks or Bandwidth >100 Mbps
					LOGGER.info("Total NRC Loaded is {} and Bandwidth is {} ", scServiceDetail.getNrc(), supplierLocalLoopBw);
					Double offnetWlNrc = 0.0;							
					if(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")!=null)  offnetWlNrc= Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf")!=null) offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and Bandwidth is {} ", offnetWlNrc,offnetRFNrc, supplierLocalLoopBw);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000 )|| supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true {}",scServiceDetail.getUuid());
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}
				
				if(lastMileTypeB.toLowerCase().contains("onnet wireless"))lastMileTypeB="OnnetRF";
				else if(lastMileTypeB.toLowerCase().contains("offnet wireless"))lastMileTypeB="OffnetRF";
				else if(lastMileTypeB.toLowerCase().contains("offnet wireline"))lastMileTypeB="OffnetWL";
				else if(lastMileTypeB.toLowerCase().contains("onnet wireline"))lastMileTypeB="OnnetWL";
				else if(lastMileTypeB.toLowerCase().contains("onnetwl_npl"))lastMileTypeB="OnnetWL";
				else if(lastMileTypeB.toLowerCase().contains("man") || lastMileTypeB.toLowerCase().contains("wan aggregation"))lastMileTypeB="OnnetWL";
				if(StringUtils.isBlank(lastMileTypeB))lastMileTypeB="OnnetWL";
				
				String subProduct = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Sub Product"));
				crossConnectType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Cross Connect Type"));
				String mediaType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Media Type"));
				
				LOGGER.info("CrossConnectType {}", crossConnectType);
				LOGGER.info("MediaType {}", mediaType);
				Map<String, String> atMap = new HashMap<>();
				Map<String, String> btMap = new HashMap<>();
				String interfaceTypeA="";
				String interfaceTypeB="";
				
				if (isLMRequired) {
					LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);				

				
					processBomDetails(scServiceDetail, atMap,scComponentAttributesAMap);					
					processBomDetails(scServiceDetail, btMap,scComponentAttributesBMap);

					if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())
							&& "Passive".equalsIgnoreCase(crossConnectType)){
						LOGGER.info("MMR Passive for interface");
						if(mediaType!=null && !mediaType.isEmpty()){
							LOGGER.info("mediaType exists");
							String siteEndInterface=getInterfaceValueBasedOnMediaType(mediaType);
							processVar.put("internalCablingInterface", siteEndInterface);
							processVar.put("internalCablingInterfaceB", siteEndInterface);
							atMap.put("interfaceType", siteEndInterface);
							btMap.put("interfaceType", siteEndInterface);
						}else{
							LOGGER.error("mediaType missing for : {} ", scServiceDetail.getUuid());
						}
						
						if(lastMileTypeB.toLowerCase().contains("colo")) {
							LOGGER.info("MMR-Passive-COLO-Changed-OnnetWL");
							lastMileTypeB="OnnetWL";
						}
					}else{
						LOGGER.info("Other than MMR Passive for interface");
						String siteEndInterfaceA = scComponentAttributesAMap.get("interface");

						if (siteEndInterfaceA != null) {
							interfaceTypeA = getInterfaceValue(siteEndInterfaceA);
							atMap.put("interfaceType", interfaceTypeA);
							if(siteEndInterfaceA.contains("G.957")){//G.957[Optical to Cramer, But for Cabling it is electrical]
								LOGGER.info("A end:: Converting G.957 to Electrical");
								processVar.put("internalCablingInterface", "Electrical");
								processVar.put("interface", "G.957");
							}else{
								LOGGER.info("A end::  Other than G.957");
								processVar.put("internalCablingInterface", interfaceTypeA);
								processVar.put("interface", siteEndInterfaceA);
							}
						} else {
							LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
						}
						
						String siteEndInterfaceB = scComponentAttributesBMap.get("interface");

						if (siteEndInterfaceB != null) {
							interfaceTypeB = getInterfaceValue(siteEndInterfaceB);
							btMap.put("interfaceType", interfaceTypeB);
							if(siteEndInterfaceB.contains("G.957") || siteEndInterfaceB.equalsIgnoreCase("optical")){ //G.957[Optical to Cramer, But for Cabling it is electrical]
								LOGGER.info("B end:: Converting G.957 to Electrical");
								processVar.put("internalCablingInterfaceB", "Electrical");
								processVar.put("interfaceB", "G.957");
								btMap.put("interface", "G.957");
							}else{
								LOGGER.info("B end::  Other than G.957");
								processVar.put("internalCablingInterfaceB", interfaceTypeB);
								processVar.put("interfaceB", siteEndInterfaceB);
							}
						} else {
							LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
						}
					}
					atMap.put("isBothSiteSameInterface", "No");
					btMap.put("isBothSiteSameInterface", "No");
					if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName()) 
							&& processVar.containsKey("internalCablingInterface")
							&& processVar.containsKey("internalCablingInterfaceB")) {
						LOGGER.info("MMR Interface");
						String interfaceA = StringUtils
								.trimToEmpty((String) processVar.get("internalCablingInterface"));
						String interfaceB = StringUtils
								.trimToEmpty((String) processVar.get("internalCablingInterfaceB"));
						if (!interfaceA.isEmpty() && !interfaceB.isEmpty()
								&& interfaceA.equalsIgnoreCase(interfaceB)) {
							LOGGER.info("MMR both side same interface for Site A::{} and Site B::{}",interfaceA,interfaceB);
							processVar.put("isBothSiteSameInterface", "Yes");
							atMap.put("isBothSiteSameInterface", "Yes");
							btMap.put("isBothSiteSameInterface", "Yes");
						}else{
							LOGGER.info("MMR both side different for Site A::{} and Site B::{}",interfaceA,interfaceB);
							processVar.put("isBothSiteSameInterface", "No");
						}
					}
					if("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())){
						atMap.put("permissionRequired", "IBD");
						btMap.put("permissionRequired", "IBD");
					}
				}

				// Lastmile B
				String lmScenarioTypeB = "";
				String lmConnectionTypeB="";
				 if (lastMileTypeB.toLowerCase().contains("offnetwl")) {
					 lmScenarioTypeB = "Offnet Wireline";
					 lmConnectionTypeB = "Wireline";
					lmType = "offnet";
					lastMileTypeB="OffnetWL";
				} else {
					lmScenarioTypeB = "Onnet Wireline";
					lmConnectionTypeB = "Wireline";
					lmType = "onnet";
					lastMileTypeB="OnnetWL";
					if (connectedCustomerB.contains("1") 
							|| "Hybrid NPL".equalsIgnoreCase(subProduct) 
							|| customerUserName.contains("Tata Teleservices")
							|| interfaceTypeB.contains("Optical")
							|| connectedCustomerCategoryB.equalsIgnoreCase("Connected Customer")) {
						processVar.put(IS_CONNECTED_SITE_B, true);
						processVar.put(IS_CONNECTED_BUILDING_B, false);
						rowRequired = false;
						prowRequired = false;
						lmScenarioTypeB = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingB.contains("1")) {
						rowRequired = false;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put(IS_CONNECTED_BUILDING_B, true);
						processVar.put(IS_CONNECTED_SITE_B, false);
						processVar.put("isNetworkSelectedPerBOPB", true);
						lmScenarioTypeB = "Onnet Wireline - Connected Building";
					} else {
						rowRequired = true;
						prowRequired = true;
						processVar.put(IS_CONNECTED_BUILDING_B, false);
						processVar.put(IS_CONNECTED_SITE_B, false);
						processVar.put("isNetworkSelectedPerBOPB", true);
						lmScenarioTypeB = "Onnet Wireline - Near Connect";
					}
				}
				processVar.put("isMuxInfoAvailable", false);
				processVar.put("txRequired", true);
				processVar.put("isMuxIPAvailable", false);
				processVar.put("rowRequiredB", rowRequired);
				processVar.put("prowRequiredB", prowRequired);
				processVar.put(LM_TYPE_B,lmType);
				processVar.put(LM_CONNECTION_TYPE_B,lmConnectionTypeB);

				if (lastMileTypeA.toLowerCase().contains("offnet")) {
					// TODO: check if P2P
					// isColoRequired = true;
					String source = StringUtils.trimToEmpty(scComponentAttributesBMap.get("sourceState"));
					String destination = StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationState"));	
					LOGGER.info("getDestinationState:{} and getSourceState:{}", source, destination);

					if (destination != null && source != null && !destination.equalsIgnoreCase(source)) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);

					}
					Double supplierLocalLoopBwDouble = 0.0;
					String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}
					// check NRC >1 Lacks or Bandwidth >100 Mbps
					LOGGER.info("Total NRC Loaded is {} and  Bandwidth is {} ", scServiceDetail.getNrc(), supplierLocalLoopBwDouble);
					Double offnetWlNrc = 0.0;							
					if(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")!=null)  offnetWlNrc= Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf")!=null) offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and  Bandwidth is {} ", offnetWlNrc,offnetRFNrc, supplierLocalLoopBwDouble);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000 )|| supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true {}",scServiceDetail.getUuid());
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}

				LOGGER.info(
						"Input processNPLL2ODataToFlowable received for lmScenarioType::{}  cpeManagementType::{} cpeType::{} isTaxAvailable::{} serviceId:: {} service code:: {} subProduct::{} interfaceTypeA::{} interfaceTypeB::{}",
						lmScenarioType, cpeManagementType, "", isTaxAvailable, serviceId,
						scServiceDetail.getUuid(),subProduct,interfaceTypeA,interfaceTypeB);
				
				String cpeSiScope = "Unmanagged";
				processVar.put("isCPEConfiguredByCustomer", true);
				processVar.put("isCPEArrangedByCustomer", true);
				processVar.put("cpeSiScope", cpeSiScope);	

				processVar.put("documentValidationRequired", true);
				processVar.put("isLMRequired", isLMRequired);
				processVar.put("e2eServiceTestingCompleted", false);
				processVar.put("siteReadinessStatus", true);
				processVar.put("siteReadinessConfirmation", false);
				processVar.put("hasOSPCapexDeviation", false);
				processVar.put("hasIBDCapexDeviation", false);
				processVar.put("ibdContractRequired", true);
				processVar.put("isMuxRequired", false);
				processVar.put("isCPERequired", isCPERequired);
				processVar.put("createServiceCompleted", false);
				processVar.put("isCLRSyncCallSuccess", false);
				processVar.put("serviceDesignCompleted", false);
				processVar.put("txConfigurationCompleted", false);
				processVar.put("serviceConfigurationCompleted", false);
				processVar.put("isStandardCPEDiscount", true);

				processVar.put("isRFRequired", isRFRequired);
				processVar.put("isColoRequired", isColoRequired);
				processVar.put("failoverTestingRequired", false);
				processVar.put("additionalTechDetailsTaskCompleted", false);
				processVar.put("siteReadinessTaskCompleted", false);
				processVar.put("isValidDocuments", false);
				processVar.put("isSoftLoopPossibleAtCE", false);
				processVar.put("lmTestOnnetWirelessSuccess", true);
				processVar.put("isPEInternalCablingRequired", false);
				processVar.put("isCEInternalCablingRequired", true);
				processVar.put("cableSwappingPERequired", "No");
				processVar.put("cableSwappingCERequired", "No");
				processVar.put("internalCablingResponsibility", "TCL");
				//processVar.put("internalCablingInterface", "");
				processVar.put("serviceDeliveryPlanReady", false);
				processVar.put("order_enrichment_complete", false);
				processVar.put("getIpServiceSyncCallCompleted", false);
				processVar.put("validateBtsStatus", false);

				processVar.put("confirmOrderRequired", true);
				processVar.put("serviceConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("serviceConfigurationMessageSent", false);
				processVar.put("serviceConfigurationAckSuccess", false);
				processVar.put("serviceConfigurationSuccess", false);
				processVar.put("txConfigurationMessageSent", false);
				processVar.put("txConfigurationAckSuccess", false);
				processVar.put("txConfigurationSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txMPLSConfigurationSuccess", false);
				processVar.put("rfConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("rfConfigurationMessageSent", false);
				processVar.put("rfConfigurationAckSuccess", false);
				processVar.put("rfConfigurationSuccess", false);

				processVar.put("previewIpConfigMessageSent", false);
				processVar.put("previewIpConfigAckSuccess", false);
				processVar.put("previewIpConfigSuccess", false);
				processVar.put("cancelIpConfigMessageSent", false);
				processVar.put("cancelIpConfigAckSuccess", false);
				processVar.put("cancelIpConfigSuccess", false);
				processVar.put("txManualConfigRequired", false);
				processVar.put("sameDayRFInstallation", false);
				

				processVar.put("cpeLicensePRCompleted", true);
				processVar.put("cpeHardwarePRCompleted", true);
				processVar.put("isCpeAvailableInInventory", false);
				processVar.put("cpeLicenseNeeded", true);
				processVar.put("remainderCycle", reminderCycle);
				processVar.put("deemedAcceptanceDuration", "PT48H");

				processVar.put("siteAType", "A");
				processVar.put("siteBType", "B");
				processVar.put("site_type", "A");

				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				

				processVar.put(SITE_ADDRESS,StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				processVar.put(CITY,StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME,StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
		
				processVar.put(SITE_ADDRESS_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(CITY_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactName")));
				processVar.put(SITE_ADDRESS_B,StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(STATE_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationState")));
				processVar.put(COUNTRY_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCountry")));
				
				atMap.put("cpeType","Customer provided");
				atMap.put("cpeSiScope","Customer provided");
				btMap.put("cpeType","Customer provided");
				btMap.put("cpeSiScope","Customer provided");
				
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);				
				btMap.put("lastMileScenario", lmScenarioTypeB);
				btMap.put("lmConnectionType", lmConnectionTypeB);
				atMap.put("lmType", lastMileTypeA);
				btMap.put("lmType", lastMileTypeB);
				
				ScOrderAttribute poDate = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);
				
				if(poDate!=null && poNumber!=null && poDate.getAttributeValue()!=null && poNumber.getAttributeValue()!=null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				LOGGER.info("lmType A= {} lmType B={}", lastMileTypeA,lastMileTypeB);	
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");
				
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), btMap,
						AttributeConstants.COMPONENT_LM, "B");

			}
			processVar.put("processType", "computeProjectPLan");

            if(scComponentAttributesAMap.get("destinationCity")!=null && !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
                processVar.put("tier",setTierValue(scComponentAttributesAMap.get("destinationCity")));
            }
            if(scComponentAttributesBMap.get("destinationCity")!=null && !scComponentAttributesBMap.get("destinationCity").isEmpty()) {
                processVar.put("tierB",setTierValue(scComponentAttributesBMap.get("destinationCity")));
            }
			//runtimeService.startProcessInstanceByKey("npl-service-fulfilment-handover-workflow", processVar);
			processVar.put(IS_PORT_CHANGED, true);
			processVar.put("cpeConfigurationCompleted", true);
			processVar.put("demoOrder", "N");
			processVar.put("isDemoOrderBillable", "N");
			processVar.put("demoDays", 0);
			processVar.put("multiVrfBillingRequiredSkip", "No");
			if(nplClrFlow) {
				LOGGER.info("npl-clr-Workaround");
				runtimeService.startProcessInstanceByKey("clr-npl-workaround-workflow", processVar);
			}else if(nplCloudClrFlow) {
				LOGGER.info("clr-cloud-npl-workaround-workflow");
				runtimeService.startProcessInstanceByKey("clr-cloud-npl-workaround-workflow", processVar);
			}else if(kroneFlow) {
				LOGGER.info("krone_installation_workflow");
				runtimeService.startProcessInstanceByKey("krone_installation_workflow", processVar);
			}else if(nplMacdClrFlow) {
				LOGGER.info("npl-clr-macd-workaround");
				runtimeService.startProcessInstanceByKey("npl-clr-macd-workaround", processVar);
			}else if(acceptance) {
				LOGGER.info("acceptance-Workaround");
				runtimeService.startProcessInstanceByKey("acceptance-workaround-workflow", processVar);
			}else if(survey) {
				LOGGER.info("survey-Workaround");
				runtimeService.startProcessInstanceByKey("survey-workaround-workflow", processVar);
			}else  {
				LOGGER.info("npl-Workaround");
				runtimeService.startProcessInstanceByKey("npl-workaround-workflow", processVar);
			}


			LOGGER.info("processL2ODataToFlowable completed for NPL");
		} catch (Exception e) {

			LOGGER.error("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}
	

	@Transactional
	public Boolean processWebExL2ODataToFlowable(Integer serviceId,ScServiceDetail scServiceDetail) {
		Boolean status = true;
		try {
			
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);
				scServiceDetail = opScServiceDetail.get();
			}
			LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",serviceId, scServiceDetail.getUuid());
			
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);
			
			LOGGER.info("WebEx work flow to be triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			ServiceFulfillmentRequest serviceFulfillmentRequest = serviceCatalogueService
					.processWebExServiceFulFillmentData(scServiceDetail.getId());
			LOGGER.info("WebEx work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());

			LOGGER.info("Input processWebExL2ODataToFlowable received for L2O :: {}", serviceFulfillmentRequest);
			Map<String, Object> processVar = new HashMap<>();
			Map<String, String> atMap = new HashMap<>();
			if (serviceFulfillmentRequest.getOrderInfo() != null && serviceFulfillmentRequest.getPrimaryServiceInfo() != null) {
				
				if (serviceFulfillmentRequest.getCustomerInfo() != null) {
					processVar.put("customerUserName", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCustomerName()));
					//processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCusomerContactEmail()));
					processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getOrderInfo().getOrderCreatedBy()));
				}
				
				processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getProductName()));
				processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getOfferingName()));
				
				/*
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));*/
					
				OrderInfoBean orderInfoBean = serviceFulfillmentRequest.getOrderInfo();
				processVar.put(SC_ORDER_ID, orderInfoBean.getScOrderId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(orderInfoBean.getOptimusOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderInfoBean.getOrderType()));
				processVar.put(CONTRACT_START_DATE, orderInfoBean.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", orderInfoBean.getOrderCreatedDate());
				processVar.put(ORDER_CREATED_DATE, orderInfoBean.getOrderCreatedDate());
			
				ServiceInfoBean primaryServiceInfo = serviceFulfillmentRequest.getPrimaryServiceInfo();
				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(primaryServiceInfo.getServiceCode()));
				processVar.put(SERVICE_ID, primaryServiceInfo.getServiceId());
				
				LOGGER.info("primaryServiceInfo::{}", primaryServiceInfo, primaryServiceInfo.getServiceDetailsAttributes(), primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
				LOGGER.info("audioModel::{}" ,primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
				processVar.put("audioModel", primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
				
				processVar.put("remainderCycle", reminderCycle);
				
				String endpointType ="Outright";
				atMap.put("endpointType", endpointType);
				processVar.put("endpointType", endpointType);
				atMap.put("cpeType", endpointType);
				processVar.put("cpeType", endpointType);
				
				String cpeManagementType = "Fully Managed";
				atMap.put("cpeManagementType", cpeManagementType);
				processVar.put("cpeManagementType", cpeManagementType);
				
				processVar.put("processType", "computeProjectPLan");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				processVar.put("demoOrder", "N");
				processVar.put("isDemoOrderBillable", "N");
				processVar.put("demoDays", 0);
				processVar.put("multiVrfBillingRequiredSkip", "No");
				atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				
				ScOrderAttribute poDate = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scServiceDetail.getScOrder());
				ScOrderAttribute poNumber = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scServiceDetail.getScOrder());
				
				if(poDate!=null && poNumber!=null && poDate.getAttributeValue()!=null && poNumber.getAttributeValue()!=null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				
				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));

				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if(mstStateToDistributionCenterMapping!=null && mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()!=null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
				}
				
				String workflowKey = "webex-service-fulfilment-handover-workflow";
				//ScWebexServiceCommercial scWebexServiceCommercials = scWebexServiceCommercialRepository.findFirstByScServiceDetailIdAndComponentType(primaryServiceInfo.getServiceId(), "Endpoint");
				//if(scWebexServiceCommercials != null) {
				if(scServiceDetail.getErfPrdCatalogOfferingName().equals("UCCEndpoint")) {
					LOGGER.info("Inside webex endpoint workflow trigger");
					String SiScope = "Supply, Installation, Support";
					atMap.put("cpeSiScope", SiScope);
					processVar.put("cpeSiScope", SiScope);
					
					workflowKey = "webex-endpoint-fulfilment-handover-workflow";
				} else {
					LOGGER.info("Inside webex overlay application workflow trigger");
					String SiScope = "";
					atMap.put("cpeSiScope", SiScope);
					processVar.put("cpeSiScope", SiScope);
				}
				
				componentAndAttributeService.updateAttributes(serviceFulfillmentRequest.getPrimaryServiceInfo().getServiceId(), atMap, AttributeConstants.COMPONENT_LM, "A");
				
				runtimeService.startProcessInstanceByKey(workflowKey, processVar);
			} else {
				LOGGER.info("invalid data in processWebExL2ODataToFlowable");
			}
			LOGGER.info("processWebExL2ODataToFlowable completed");
		} catch (Exception e) {
			LOGGER.error("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}
	
	@Transactional
	public Boolean processGSCL2ODataToFlowable(Integer serviceId,ScServiceDetail scServiceDetail) {
		Boolean status = true;
		try {
			
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);
				scServiceDetail = opScServiceDetail.get();
			}
			
			LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",serviceId, scServiceDetail.getUuid());
			
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);
			
			LOGGER.info("GSC work flow to be triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			ServiceFulfillmentRequest serviceFulfillmentRequest = serviceCatalogueService
					.processWebExServiceFulFillmentData(scServiceDetail.getId());

			Map<String, Object> processVar = new HashMap<>();
			Map<String, String> atMap = new HashMap<>();
			if (serviceFulfillmentRequest.getOrderInfo() != null && serviceFulfillmentRequest.getPrimaryServiceInfo() != null) {
				
				ScServiceDetail webexLicenseServiceDetail = scServiceDetailRepository.findByScOrderAndErfPrdCatalogOfferingName(scServiceDetail.getScOrder(), "Cisco WebEx CCA");
				if(webexLicenseServiceDetail != null) {
					ServiceFulfillmentRequest webexLicenseFulfillmentRequest = serviceCatalogueService.processWebExServiceFulFillmentData(webexLicenseServiceDetail.getId());
					ServiceInfoBean primaryServiceInfo = webexLicenseFulfillmentRequest.getPrimaryServiceInfo();
					LOGGER.info("audioModel::{}" , primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
					processVar.put("audioModel", primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
					atMap.put("audioModel", primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
					
					LOGGER.info("primary_region::{}" , primaryServiceInfo.getServiceDetailsAttributes().get("primary_region"));
					atMap.put("primaryRegion", primaryServiceInfo.getServiceDetailsAttributes().get("primary_region"));
					
					LOGGER.info("cugRequired::{}" , primaryServiceInfo.getServiceDetailsAttributes().get("cug_required"));
					if(primaryServiceInfo.getServiceDetailsAttributes().get("cug_required") != null && primaryServiceInfo.getServiceDetailsAttributes().get("cug_required").equals("1")) {
						processVar.put("iscug", "yes");
						atMap.put("iscug", "yes");
					} else {
						processVar.put("iscug", "no");
						atMap.put("iscug", "no");
					}
					
					LOGGER.info("dial_in::{}" , primaryServiceInfo.getServiceDetailsAttributes().get("dial_in"));
					atMap.put("isDialIn", primaryServiceInfo.getServiceDetailsAttributes().get("dial_in"));
					
					LOGGER.info("dial_out::{}" , primaryServiceInfo.getServiceDetailsAttributes().get("dial_out"));
					atMap.put("isDialOut", primaryServiceInfo.getServiceDetailsAttributes().get("dial_out"));
					
					LOGGER.info("dial_back::{}" , primaryServiceInfo.getServiceDetailsAttributes().get("dial_back"));
					atMap.put("isDialBack", primaryServiceInfo.getServiceDetailsAttributes().get("dial_back"));
					
					if(primaryServiceInfo.getServiceDetailsAttributes().get("audio_model").equalsIgnoreCase("shared") && primaryServiceInfo.getServiceDetailsAttributes().get("cug_required").equals("0")) {
						processVar.put("isEnrichmentNeeded", "No");
					} else {
						processVar.put("isEnrichmentNeeded", "Yes");
					}
					
					
					atMap.put("isServiceAttributes", "1");
				}
				
				if (serviceFulfillmentRequest.getCustomerInfo() != null) {
					processVar.put("customerUserName", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCustomerName()));
					//processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCusomerContactEmail()));
					processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getOrderInfo().getOrderCreatedBy()));
				}
				
				processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getProductName()));
				processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getOfferingName()));
				
				/*
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));*/
					
				OrderInfoBean orderInfoBean = serviceFulfillmentRequest.getOrderInfo();
				processVar.put(SC_ORDER_ID, orderInfoBean.getScOrderId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(orderInfoBean.getOptimusOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderInfoBean.getOrderType()));
				processVar.put(CONTRACT_START_DATE, orderInfoBean.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", orderInfoBean.getOrderCreatedDate());
				processVar.put(ORDER_CREATED_DATE, orderInfoBean.getOrderCreatedDate());
			
				ServiceInfoBean primaryServiceInfo = serviceFulfillmentRequest.getPrimaryServiceInfo();
				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(primaryServiceInfo.getServiceCode()));
				processVar.put(SERVICE_ID, primaryServiceInfo.getServiceId());
				
				/*LOGGER.info("primaryServiceInfo::{}", primaryServiceInfo, primaryServiceInfo.getServiceDetailsAttributes(), primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
				LOGGER.info("audioModel::{}" ,primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));
				processVar.put("audioModel", primaryServiceInfo.getServiceDetailsAttributes().get("audio_model"));*/
				
				processVar.put("remainderCycle", reminderCycle);
				
				/*String endpointType ="Outright";
				atMap.put("endpointType", endpointType);
				processVar.put("endpointType", endpointType);
				atMap.put("cpeType", endpointType);
				processVar.put("cpeType", endpointType);
				
				String cpeManagementType = "Fully Managed";
				atMap.put("cpeManagementType", cpeManagementType);
				processVar.put("cpeManagementType", cpeManagementType);*/
				
				processVar.put("processType", "computeProjectPLan");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				
				atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				
				ScOrderAttribute poDate = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scServiceDetail.getScOrder());
				ScOrderAttribute poNumber = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scServiceDetail.getScOrder());
				
				if(poDate!=null && poNumber!=null && poDate.getAttributeValue()!=null && poNumber.getAttributeValue()!=null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				
				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));

				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if(mstStateToDistributionCenterMapping!=null && mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()!=null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
				}
				
				componentAndAttributeService.updateAttributes(serviceFulfillmentRequest.getPrimaryServiceInfo().getServiceId(), atMap, AttributeConstants.COMPONENT_LM, "A");
				
				runtimeService.startProcessInstanceByKey("gsc-service-fulfilment-handover-workflow", processVar);
				LOGGER.info("GSC work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			} else {
				LOGGER.info("invalid data in processGSCL2ODataToFlowable");
			}
			LOGGER.info("processGSCL2ODataToFlowable completed");
		} catch (Exception e) {
			LOGGER.error("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}

	private String getOffnetSupplierCategory(String lmConnectionType,Map<String, String> feasibilityAttributes){
		String offnetCatogory=null;
		LOGGER.info("Connection type for offnet "+lmConnectionType);
		if("Wireline".equalsIgnoreCase(lmConnectionType)){
			LOGGER.info("Fesibility attribute lm_otc_nrc_installation_offwl value "+feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
			if(Objects.nonNull(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")) && !feasibilityAttributes.get("lm_otc_nrc_installation_offwl").isEmpty())
			{
				Double offnetNrc = Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
				if (offnetNrc <= 25000) {
					offnetCatogory = "A";
				} else if (offnetNrc > 25000 && offnetNrc < 150000) {
					offnetCatogory = "B";
				} else {
					offnetCatogory = "C";
				}
			}
		   }else{
			LOGGER.info("Fesibility attribute avg_mast_ht and delivery_timeLine value "+
					feasibilityAttributes.get("lm_otc_nrc_installation_offwl")+","+feasibilityAttributes.get("delivery_timeLine"));
			Double mastHight=0.0;
			Integer timeline=0;
			if(Objects.nonNull(feasibilityAttributes.get("avg_mast_ht")) && !feasibilityAttributes.get("avg_mast_ht").isEmpty())
			  mastHight= Double.valueOf(StringUtils.trimToEmpty(feasibilityAttributes.get("avg_mast_ht")));
			if(Objects.nonNull(feasibilityAttributes.get("delivery_timeLine")) && !feasibilityAttributes.get("delivery_timeLine").isEmpty())
			  timeline= Integer.valueOf(StringUtils.trimToEmpty(feasibilityAttributes.get("delivery_timeLine")));

			if(timeline>0 && timeline>6){
				offnetCatogory="C";
			}else if(mastHight>0 && mastHight<=6){
				offnetCatogory="A";
			}else{
				offnetCatogory="B";
			}
		}
       return  offnetCatogory;
	}
	
	private String setTierValue(String cityName){
		try {
			if(cityName!=null && !cityName.isEmpty()){
				LOGGER.info("City in process variable :{}",cityName);
				CityTierMapping cityTierMapping =cityTierMappingRepository.findByCityName(cityName);
				if(Objects.nonNull(cityTierMapping) && "tire1".equalsIgnoreCase(cityTierMapping.getTire())){
					return  "tier1";
				}else{
                    return  "tier2";
				}
			}
		}catch (Exception ex){
			LOGGER.error("Error in setTierValue",ex);
		}
        return  "tier2";
	}
	
	@Transactional(readOnly=false)
	public Boolean processSDWANSolutionL2ODataToFlowable(String orderCode) {
		LOGGER.info("processSDWANSolutionL2ODataToFlowable method invoked");
		Boolean status = false;
		try {
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			List<ScServiceDetail> solutionServiceList = scServiceDetailRepository
					.findByScOrderIdAndProductName(scOrder.getId(), "IZOSDWAN_SOLUTION");
			ScOrderAttribute ownerRegion = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(IzosdwanCommonConstants.OWNER_REGION, scOrder);
			for (ScServiceDetail solutionService : solutionServiceList) {
				Map<String, Object> solutionProcessVar = new HashMap<>();
				solutionProcessVar.put(SERVICE_ID, solutionService.getId());
				solutionProcessVar.put(SC_ORDER_ID, scOrder.getId());
				solutionProcessVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				solutionProcessVar.put(ORDER_TYPE, StringUtils.trimToEmpty(solutionService.getOrderType()));
				solutionProcessVar.put(SERVICE_CODE, solutionService.getUuid());
				solutionProcessVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				solutionProcessVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				solutionProcessVar.put(PRODUCT_NAME,
						StringUtils.trimToEmpty(solutionService.getErfPrdCatalogProductName()));
				solutionProcessVar.put(IzosdwanCommonConstants.OWNER_REGION,"");
				if(ownerRegion!=null && ownerRegion.getAttributeValue()!=null && !ownerRegion.getAttributeValue().isEmpty()){
					solutionProcessVar.put(IzosdwanCommonConstants.OWNER_REGION,ownerRegion.getAttributeValue());
				}
				solutionProcessVar.put("root_endDate", new Timestamp(new Date().getTime()));
				solutionProcessVar.put("processType", "computeProjectPLan");
				solutionProcessVar.put("site_type", "A");
				solutionProcessVar.put("remainderCycle", reminderCycle);
				solutionProcessVar.put("O2CPROCESSKEY", "sdwan-lld-workflow");
				LOGGER.info("Solution Flow Trigger::{}", solutionProcessVar);
				runtimeService.startProcessInstanceByKey("sdwan-lld-workflow", solutionProcessVar);
			}
		} catch (Exception e) {

			LOGGER.error("Error for processSDWANSolutionL2ODataToFlowable::{}", e);
			status = false;
		}
		return status;

	}
	@Transactional(readOnly=false)
	public Boolean processSDWANL2ODataToFlowable(String orderCode) throws TclCommonException {
		LOGGER.info("processSDWANL2ODataToFlowable method invoked");
		Boolean status = false;
		try {
			List<String> componentGroups = new ArrayList<>();
			componentGroups.add("OVERLAY");
			componentGroups.add("UNDERLAY");
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(scOrder.getId());
			Map<Integer, ScServiceDetail> scServiceDetailMap = new HashMap<>();
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				scServiceDetailMap.put(scServiceDetail.getId(), scServiceDetail);
			}
			LOGGER.info("ScServiceDetailMap::{}",scServiceDetailMap);
			List<ScSolutionComponent> scSolutionComponents=scSolutionComponentRepository.findByOrderCodeAndComponentGroupAndIsActive(orderCode, "OVERLAY", "Y");
			Map<Integer, String> overlayPriorityMap = new HashMap<>();
			for (ScSolutionComponent scSolutionComponent : scSolutionComponents) {
				overlayPriorityMap.put(scSolutionComponent.getScServiceDetail1().getId(), scSolutionComponent.getPriority());
			}
			LOGGER.info("OverlayPriorityMap::{}",overlayPriorityMap);
			ScServiceDetail scSolutionServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidAndErfPrdCatalogProductNameOrderByIdDesc(orderCode,"IZOSDWAN_SOLUTION");
			Integer solutionId=scSolutionServiceDetail.getId();
				List<Map<String, Integer>> layDetails = scSolutionComponentRepository
						.findScServiceDetailByComponentType(orderCode, componentGroups, "Y");
				if (layDetails != null && !layDetails.isEmpty()) {
					for (Map<String, Integer> overUnderlayMap : layDetails) {
						LOGGER.info("Underlay Ids: {}", overUnderlayMap.get("underlayIds"));
						LOGGER.info("Overlay Ids: {}", overUnderlayMap.get("overlayIds"));
						Integer overlayId = overUnderlayMap.get("overlayIds");
						String underlays = String.valueOf(overUnderlayMap.get("underlayIds"));
						LOGGER.info("Underlay Ids:{}", underlays);
						String[] underlayList = underlays.split(",");
						String flowTriggerType=getFlowTriggerType(underlayList,overlayId,scServiceDetailMap);
						for (String underlay : underlayList) {
							Integer underlayId = Integer.valueOf(underlay);
							if (scServiceDetailMap.containsKey(underlayId)) {
								Map<String, Object> processVar = new HashMap<>();
								LOGGER.info("Underlay flow map::{}", processVar);
								if (scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName()
										.equalsIgnoreCase("BYON Internet")) {
									LOGGER.info("BYON Internet Underlay::{}", underlayId);
									processBYONInternet(scOrder,scServiceDetailMap,processVar,solutionId,overUnderlayMap.get("overlayIds"),underlayId,flowTriggerType);
								} else if (scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName().equalsIgnoreCase("IAS")) {
									LOGGER.info("ILL Underlay:{}", underlayId);
									Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
											underlayId, AttributeConstants.COMPONENT_LM, "A");
									processIASGVPNUnderLay(scServiceDetailMap.get(underlayId),
											overUnderlayMap.get("overlayIds"),
											solutionId,flowTriggerType,scComponentAttributesAMap);
								}else if (scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
									LOGGER.info("GVPN Underlay:{}", underlayId);
									Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
											underlayId, AttributeConstants.COMPONENT_LM, "A");
									LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);
									if(!scComponentAttributesAMap.get("destinationCountry").equalsIgnoreCase("India")){
										LOGGER.info("International GVPN Underlay Code::{} ",scServiceDetailMap.get(underlayId).getUuid());
										processIWANDIAGVPNInternationalUnderlay(scOrder,scServiceDetailMap,processVar,solutionId,overUnderlayMap.get("overlayIds"),underlayId,flowTriggerType);
									}else{
										LOGGER.info("Domestic GVPN Underlay Code::{} ",scServiceDetailMap.get(underlayId).getUuid());
										processIASGVPNUnderLay(scServiceDetailMap.get(underlayId),
												overUnderlayMap.get("overlayIds"),
												solutionId,flowTriggerType,scComponentAttributesAMap);
									}
								}else if (scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName().equalsIgnoreCase("IZO Internet WAN")) {
									LOGGER.info("IWAN Underlay::{}", underlayId);
									if(!scServiceDetailMap.get(underlayId).getDestinationCountry().equalsIgnoreCase("India")){
										LOGGER.info("International IZO Internet WAN Underlay Code::{} ",scServiceDetailMap.get(underlayId).getUuid());
										processIWANDIAGVPNInternationalUnderlay(scOrder,scServiceDetailMap,processVar,solutionId,overUnderlayMap.get("overlayIds"),underlayId,flowTriggerType);
									}
								} else if (scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName().equalsIgnoreCase("DIA")) {
									LOGGER.info(" DIA Underlay::{}", underlayId);
									processIWANDIAGVPNInternationalUnderlay(scOrder,scServiceDetailMap,processVar,solutionId,overUnderlayMap.get("overlayIds"),underlayId,flowTriggerType);
								} 
							}
						}
						LOGGER.info("Overlay Id:{}", overlayId);
						processOverlay(scServiceDetailMap,overlayId,scOrder,solutionId,flowTriggerType,overlayPriorityMap);
					}
				}
			processCGW(scOrder,solutionId);
		} catch (Exception e) {
			LOGGER.error("Exception for processSDWANL2ODataToFlowable for OrderCode::{} with message::{}",orderCode ,e);
			status = false;
			List<String> tempToMailList= new ArrayList<>(Arrays.asList("OPTIMUS-O2C-SUPPORT@tatacommunications.onmicrosoft.com"));
			notificationService.notifySDWANTrigger(null,tempToMailList,orderCode);
		}
		return status;
	}
	
	
	
	
	private String getFlowTriggerType(String[] underlayList, Integer overlayId, Map<Integer, ScServiceDetail> scServiceDetailMap) {
		LOGGER.info("getFlowTriggerType method invoked");
		String flowType="";
		ScServiceDetail overlayServiceDetail=scServiceDetailMap.get(overlayId);
		Boolean isNewByonInternetExists=false;
		Boolean isNewUnderlayExists=false;
		Boolean isMACDUnderlayExists=false;
		if(overlayServiceDetail.getOrderType().equalsIgnoreCase("NEW")){
			LOGGER.info("Overlay Service Code::{} with OrderType::{}",overlayServiceDetail.getUuid(),overlayServiceDetail.getOrderType());
			for(String underlayId:underlayList){
				ScServiceDetail underlayServiceDetail=scServiceDetailMap.get(Integer.valueOf(underlayId));
				LOGGER.info("Underlay Service Id::{}",underlayServiceDetail.getId());
				if(underlayServiceDetail.getOrderType().equalsIgnoreCase("NEW") || 
						(underlayServiceDetail.getOrderType().equalsIgnoreCase("MACD")
								&& ((underlayServiceDetail.getOrderCategory()!=null && underlayServiceDetail.getOrderCategory().equalsIgnoreCase("ADD_SITE"))
										|| (underlayServiceDetail.getOrderSubCategory()!=null && underlayServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))))){
					LOGGER.info("Flow Type is NEW for Service Id::{}",underlayServiceDetail.getId());
					if(underlayServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("BYON Internet")){
						LOGGER.info("Flow Type is NEW for Byon Service Id::{}",underlayServiceDetail.getId());
						isNewByonInternetExists=true;
					}else {
						LOGGER.info("Flow Type is NEW for Product::{} with Service Id::{}",underlayServiceDetail.getErfPrdCatalogProductName(),underlayServiceDetail.getId());
						isNewUnderlayExists=true;
					}
				}else{
					LOGGER.info("Flow Type is MACD for Product::{} with Service Id::{}",underlayServiceDetail.getErfPrdCatalogProductName(),underlayServiceDetail.getId());
					isMACDUnderlayExists=true;
				}
			}
			if((isNewByonInternetExists || !isNewByonInternetExists) && isNewUnderlayExists && isMACDUnderlayExists){ //Few TCL underlays are New and few are Existing + with/without New BYON-IAS
				flowType="HYBRID";
			}else if((isNewByonInternetExists || !isNewByonInternetExists) && (isNewUnderlayExists) && !isMACDUnderlayExists){ //All TCL underlays are New + with/without New BYON-IAS
				flowType="NEW";
			}else if((( isNewByonInternetExists || !isNewByonInternetExists) && !isNewUnderlayExists && isMACDUnderlayExists)  //All underlays are existing with/without New BYON-IAS
					|| (isNewByonInternetExists && !isNewUnderlayExists && !isMACDUnderlayExists)){                            //Only New BYON-IAS
				flowType="MACD";
			}
			LOGGER.info("Overlay Service Code::{} with FlowType::{} and Id::{}",overlayServiceDetail.getUuid(),flowType,overlayId);
		}
		return flowType;
	}

	@Transactional(readOnly=false)
	public void processIWANDIAGVPNInternationalUnderlay(ScOrder scOrder, Map<Integer, ScServiceDetail> scServiceDetailMap,
			Map<String, Object> processVar, Integer solutionId, Integer overlayId, Integer underlayId,
			String flowTriggerType) throws TclCommonException{
		try{
			String productName= StringUtils.trimToEmpty(scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName());
			LOGGER.info("processIWANDIAGVPNInternationalUnderlay method invoked{}", underlayId);
			processVar.put(SERVICE_ID, underlayId);
			processVar.put(SC_ORDER_ID, scOrder.getId());
			processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
			processVar.put(ORDER_TYPE,
					StringUtils.trimToEmpty(scServiceDetailMap.get(underlayId).getOrderType()));
			processVar.put("root_endDate", new Timestamp(new Date().getTime()));
			processVar.put(PRODUCT_NAME, productName);
			processVar.put("processType", "computeProjectPLan");
			processVar.put("site_type", "A");
			processVar.put(SERVICE_CODE, scServiceDetailMap.get(underlayId).getUuid());
			processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
			processVar.put("remainderCycle", reminderCycle);
			processVar.put("overlayId", overlayId);
			processVar.put("solutionId", solutionId);
			processVar.put("flowType", flowTriggerType);
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					underlayId, AttributeConstants.COMPONENT_LM, "A");
			processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
			processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
			processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
			if(productName.equalsIgnoreCase("IZO Internet WAN")){
				processVar.put("iwan-config", "iwan-config");
				processVar.put("O2CPROCESSKEY", "sdwan-iwan-underlay-workflow");
				LOGGER.info("IWAN ProcessVar:{}", processVar);
				runtimeService.startProcessInstanceByKey("sdwan-iwan-underlay-workflow",processVar);
			}else if(productName.equalsIgnoreCase("DIA")){
				processVar.put("dia-config", "dia-config");
				processVar.put("O2CPROCESSKEY", "sdwan-dia-underlay-workflow");
				LOGGER.info("DIA ProcessVar:{}", processVar);
				runtimeService.startProcessInstanceByKey("sdwan-dia-underlay-workflow",processVar);
			}else if(productName.equalsIgnoreCase("GVPN")){
				processVar.put("gvpn-international-config", "gvpn-international-config");
				processVar.put("O2CPROCESSKEY", "sdwan-gvpn-international-underlay-workflow");
				LOGGER.info("GVPN international ProcessVar:{}", processVar);
				runtimeService.startProcessInstanceByKey("sdwan-gvpn-international-underlay-workflow",processVar);
			}
			persistDownTimeDetails(scServiceDetailMap.get(underlayId),solutionId, scOrder.getOpOrderCode(),productName, false, false, false, false,false,false);
			updateO2CTriggerStatus("SUCCESS",solutionId,underlayId);
		}catch(Exception e){
			updateO2CTriggerStatus("FAILURE",solutionId,underlayId);
			LOGGER.error("Exception for IWAN O2C Trigger Id::{},with message::{}", underlayId,e);
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		
	}

	@Transactional(readOnly=false)
	public void processBYONInternet(ScOrder scOrder, Map<Integer, ScServiceDetail> scServiceDetailMap, Map<String, Object> processVar, Integer solutionId, Integer overlayId, Integer underlayId, String flowTriggerType) throws TclCommonException {
		try{
			LOGGER.info("processBYONInternet method invoked{}", underlayId);
			processVar.put(SERVICE_ID, underlayId);
			processVar.put(SC_ORDER_ID, scOrder.getId());
			processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
			processVar.put(ORDER_TYPE,
					StringUtils.trimToEmpty(scServiceDetailMap.get(underlayId).getOrderType()));
			processVar.put("root_endDate", new Timestamp(new Date().getTime()));
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(
					scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName()));
			processVar.put("processType", "computeProjectPLan");
			processVar.put("site_type", "A");
			processVar.put(SERVICE_CODE, scServiceDetailMap.get(underlayId).getUuid());
			processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
			processVar.put("remainderCycle", reminderCycle);
			processVar.put("overlayId", overlayId);
			processVar.put("solutionId", solutionId);
			processVar.put("byon-config", "byon-config");
			processVar.put("flowType", flowTriggerType);
			processVar.put("isByonDomesticSite", true);
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					underlayId, AttributeConstants.COMPONENT_LM, "A");
			processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
			processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
			processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
			if(scComponentAttributesAMap.containsKey("destinationCountry") && scComponentAttributesAMap.get("destinationCountry")!=null && !scComponentAttributesAMap.get("destinationCountry").isEmpty()
					&& !scComponentAttributesAMap.get("destinationCountry").equalsIgnoreCase("India")){
				LOGGER.info("International Site For Byon Underlay::{}",underlayId);
				processVar.put("isByonDomesticSite", false);
			}
			processVar.put("O2CPROCESSKEY", "sdwan-byon-ill-underlay-workflow");
			LOGGER.info("BYON ProcessVar:{}", processVar);
			// runtimeService.startProcessInstanceByKey("test-byon-ill-plan-workflow",
			// processVar);
			runtimeService.startProcessInstanceByKey("sdwan-byon-ill-underlay-workflow",
					processVar);
			persistDownTimeDetails(scServiceDetailMap.get(underlayId),
					solutionId, scOrder.getOpOrderCode(),
					"BYON Internet", false, false, false, false,false,false);
			updateO2CTriggerStatus("SUCCESS",solutionId,underlayId);
		}catch(Exception e){
			updateO2CTriggerStatus("FAILURE",solutionId,underlayId);
			LOGGER.error("Exception for BYON Internet O2C Trigger Id::{},with message::{}", underlayId,e);
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		
	}

	@Transactional(readOnly=false)
	public void processCGW(ScOrder scOrder, Integer solutionId) throws TclCommonException {
		LOGGER.info("processCGW method invoked::{}",solutionId);
		List<ScSolutionComponent> scCGWSolutionComponentList = scSolutionComponentRepository
				.findAllByOrderCodeAndComponentGroupAndIsActive(scOrder.getOpOrderCode(), "CGW", "Y");
		if (scCGWSolutionComponentList != null && !scCGWSolutionComponentList.isEmpty()) {
			LOGGER.info("CGW exists::{}", scCGWSolutionComponentList.size());
			for (ScSolutionComponent scSolutionComponent : scCGWSolutionComponentList) {
				ScServiceDetail cgwScServiceDetail = scSolutionComponent.getScServiceDetail1();
				try {
					Map<String, Object> cgwProcessVar = new HashMap<>();
					cgwProcessVar.put(SERVICE_ID, cgwScServiceDetail.getId());
					cgwProcessVar.put(SC_ORDER_ID, scOrder.getId());
					cgwProcessVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
					cgwProcessVar.put(ORDER_TYPE, StringUtils.trimToEmpty(cgwScServiceDetail.getOrderType()));
					cgwProcessVar.put(SERVICE_CODE, scSolutionComponent.getServiceCode());
					cgwProcessVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
					cgwProcessVar.put(PRODUCT_NAME,
							StringUtils.trimToEmpty(cgwScServiceDetail.getErfPrdCatalogProductName()));
					cgwProcessVar.put("root_endDate", new Timestamp(new Date().getTime()));
					cgwProcessVar.put("processType", "computeProjectPLan");
					cgwProcessVar.put("isAccountRequired", false);
					cgwProcessVar.put("site_type", "A");
					cgwProcessVar.put("remainderCycle", reminderCycle);
					cgwProcessVar.put("solutionId", solutionId);
					cgwProcessVar.put("createServiceCompleted", false);
					cgwProcessVar.put("isCLRSyncCallSuccess", false);
					cgwProcessVar.put("serviceDesignCompleted", false);
					cgwProcessVar.put("isDomesticSite", true);
					cgwProcessVar.put("isTaxCaptureRequired", false);
					cgwProcessVar.put("isBillingInternational", false);
					cgwProcessVar.put("multiVrfBillingRequiredSkip", "No");
					Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
							cgwScServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
					cgwProcessVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
					cgwProcessVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
					cgwProcessVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
					if(scComponentAttributesAMap.containsKey("destinationCountry") && scComponentAttributesAMap.get("destinationCountry")!=null && !scComponentAttributesAMap.get("destinationCountry").isEmpty()
							&& !scComponentAttributesAMap.get("destinationCountry").equalsIgnoreCase("India")){
						LOGGER.info("International Site For CGW::{}",cgwScServiceDetail.getId());
						cgwProcessVar.put("isDomesticSite", false);
					}
					if(scComponentAttributesAMap.containsKey("isBillingInternational") && scComponentAttributesAMap.get("isBillingInternational")!=null && !scComponentAttributesAMap.get("isBillingInternational").isEmpty()
							&& scComponentAttributesAMap.get("isBillingInternational").equalsIgnoreCase("Y")){
						LOGGER.info("Billing International For CGW Id::{}",cgwScServiceDetail.getId());
						cgwProcessVar.put("isBillingInternational", true);
					}
					if (cgwScServiceDetail.getPrimarySecondary().equalsIgnoreCase("Primary")) {
						cgwProcessVar.put("O2CPROCESSKEY", "sdwan-py-cgw-workflow");
						cgwProcessVar.put("cgwType", "Primary");
						cgwProcessVar.put("demoOrder", "N");
						cgwProcessVar.put("isDemoOrderBillable", "N");
						cgwProcessVar.put("demoDays", 0);
						LOGGER.info("Py CGW completed Flow Trigger::{}", cgwProcessVar);
						runtimeService.startProcessInstanceByKey("sdwan-py-cgw-workflow", cgwProcessVar);
					} else if (cgwScServiceDetail.getPrimarySecondary().equalsIgnoreCase("Secondary")) {
						cgwProcessVar.put("O2CPROCESSKEY", "sdwan-sy-cgw-workflow");
						cgwProcessVar.put("cgwType", "Secondary");
						LOGGER.info("Sy CGW completed Flow Trigger::{}", cgwProcessVar);
						runtimeService.startProcessInstanceByKey("sdwan-sy-cgw-workflow", cgwProcessVar);
					}
					LOGGER.info("Update O2C Trigger Status::{}", cgwScServiceDetail.getId());
					updateO2CTriggerStatus("SUCCESS", solutionId, cgwScServiceDetail.getId());
				} catch (Exception e) {
					updateO2CTriggerStatus("FAILURE", solutionId, cgwScServiceDetail.getId());
					LOGGER.error("Exception for CGW O2C Trigger Id::{},with message::{}", cgwScServiceDetail.getId(),e);
					throw new TclCommonException(
							com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}
			}
		}
	}

	@Transactional(readOnly=false)
	public void processOverlay(Map<Integer, ScServiceDetail> scServiceDetailMap, Integer overlayId, ScOrder scOrder, Integer solutionId, String flowTriggerType, Map<Integer, String> overlayPriorityMap) throws TclCommonException {
		LOGGER.info("processOverlay method invoked:{}", overlayId);
		if (scServiceDetailMap.containsKey(overlayId)) {
			try{
				LOGGER.info("ScServiceDetailMap exists for Overlay Id:{}", overlayId);
				Map<String, Object> overlayProcessVar = new HashMap<>();
				overlayProcessVar.put(SERVICE_ID, overlayId);
				overlayProcessVar.put(SC_ORDER_ID, scOrder.getId());
				overlayProcessVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				overlayProcessVar.put(ORDER_TYPE,
						StringUtils.trimToEmpty(scServiceDetailMap.get(overlayId).getOrderType()));
				overlayProcessVar.put(SERVICE_CODE, scServiceDetailMap.get(overlayId).getUuid());
				overlayProcessVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				overlayProcessVar.put(PRODUCT_NAME, StringUtils
						.trimToEmpty(scServiceDetailMap.get(overlayId).getErfPrdCatalogProductName()));
				overlayProcessVar.put("root_endDate", new Timestamp(new Date().getTime()));
				overlayProcessVar.put("processType", "computeProjectPLan");
				overlayProcessVar.put("isAccountRequired", false);
				overlayProcessVar.put("site_type", "A");
				overlayProcessVar.put("remainderCycle", reminderCycle);
				overlayProcessVar.put("solutionId", solutionId);
				overlayProcessVar.put("isStandardCPEDiscount", true);
				overlayProcessVar.put("cpe-config", "cpe-config");
				overlayProcessVar.put("flowType", flowTriggerType);

				overlayProcessVar.put("isCPERequired", true);
				overlayProcessVar.put("isStandardCPEDiscount", true);
				overlayProcessVar.put("cpeLicensePRCompleted", true);
				overlayProcessVar.put("cpeHardwarePRCompleted", true);
				overlayProcessVar.put("isCpeAvailableInInventory", false);
				overlayProcessVar.put("cpeLicenseNeeded", true);
				overlayProcessVar.put("cpeConfigurationCompleted", true);
				overlayProcessVar.put("isDomesticSite", true);
				overlayProcessVar.put("isTaxCaptureRequired", false);
				overlayProcessVar.put("isBillingInternational", false);
				overlayProcessVar.put("overlayPriority", overlayPriorityMap.get(overlayId));
				Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
						overlayId, AttributeConstants.COMPONENT_LM, "A");
				overlayProcessVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				overlayProcessVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				overlayProcessVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				if(scComponentAttributesAMap.containsKey("destinationCountry") && scComponentAttributesAMap.get("destinationCountry")!=null && !scComponentAttributesAMap.get("destinationCountry").isEmpty()
						&& !scComponentAttributesAMap.get("destinationCountry").equalsIgnoreCase("India")){
					LOGGER.info("International Site For Overlay::{}",overlayId);
					overlayProcessVar.put("isDomesticSite", false);
				}
				if(scComponentAttributesAMap.containsKey("isBillingInternational") && scComponentAttributesAMap.get("isBillingInternational")!=null && !scComponentAttributesAMap.get("isBillingInternational").isEmpty()
						&& scComponentAttributesAMap.get("isBillingInternational").equalsIgnoreCase("Y")){
					LOGGER.info("Billing International For Overlay Id::{}",overlayId);
					overlayProcessVar.put("isBillingInternational", true);
				}
				//int underlayCpeCount = scSolutionComponentRepository.findUnderlayBasedOnOverlayAndCpe(overlayId);
				List<Integer> cpeOverlayComponentIds = scComponentRepository
						.findComponentIdScServiceDetailId(overlayId);
				overlayProcessVar.put("isSiteBExists", false);
				overlayProcessVar.put("serviceIdsCount", 0);
				overlayProcessVar.put("cpeOverlayComponentIds", new ArrayList<Integer>());
				if (cpeOverlayComponentIds!=null && !cpeOverlayComponentIds.isEmpty()){
					LOGGER.info("cpeOverlayComponentIds exists:{}", cpeOverlayComponentIds.size());
					if(cpeOverlayComponentIds.size() > 1){
						LOGGER.info("UnderlayCpeCount greater than one which is: {} for OverlayId: {}",
								cpeOverlayComponentIds.size(), overlayId);
						overlayProcessVar.put("isSiteBExists", true);
						overlayProcessVar.put("siteBType", "B");
					}
					overlayProcessVar.put("serviceIdsCount", cpeOverlayComponentIds.size());
					overlayProcessVar.put("cpeOverlayComponentIds", cpeOverlayComponentIds);
				}
				overlayProcessVar.put("O2CPROCESSKEY", "sdwan-overlay-workflow");
				overlayProcessVar.put("demoOrder", "N");
				overlayProcessVar.put("isDemoOrderBillable", "N");
				overlayProcessVar.put("demoDays", 0);
				overlayProcessVar.put("multiVrfBillingRequiredSkip", "No");
				Map<String, String> flowTypeMap=new HashMap<>();
				flowTypeMap.put("flowType", flowTriggerType);
				componentAndAttributeService.updateServiceAttributes(overlayId, flowTypeMap);
				updateDistributionCenterAndCpeDetails(cpeOverlayComponentIds, overlayId, overlayProcessVar);
				// runtimeService.startProcessInstanceByKey("test-plan-overlay-workflow",
				// overlayProcessVar);
				LOGGER.info("Overlay flow map{}", overlayProcessVar);
				runtimeService.startProcessInstanceByKey("sdwan-overlay-workflow", overlayProcessVar);
				Boolean isCpeCustomerAppointmentRequired=false;
				if(flowTriggerType.equalsIgnoreCase("NEW") || flowTriggerType.equalsIgnoreCase("HYBRID")){
					LOGGER.info("Customer Appointment for Overlay:{}", flowTriggerType);
					isCpeCustomerAppointmentRequired=true;
				}
				persistDownTimeDetails(scServiceDetailMap.get(overlayId),
						solutionId, scOrder.getOpOrderCode(),
						"OVERLAY", false, false, false, true,isCpeCustomerAppointmentRequired,false);
				updateO2CTriggerStatus("SUCCESS",solutionId,overlayId);
			}catch(Exception e){
				updateO2CTriggerStatus("FAILURE",solutionId,overlayId);
				LOGGER.error("Exception for SDWAN Overlay Trigger Id::{},with message::{}", overlayId,e);
				throw new TclCommonException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
		}
	}


	private void updateO2CTriggerStatus(String status,Integer solutionServiceDetailId, Integer serviceId) {
		LOGGER.info("updateO2CTriggerStatus invoked for solutionServiceDetailId::{},serviceId::{}", solutionServiceDetailId,serviceId);
		scSolutionComponentRepository.updateStatus(status,solutionServiceDetailId, serviceId);
	}


	private void updateDistributionCenterAndCpeDetails(List<Integer> cpeOverlayComponentIds, Integer overlayId, Map<String, Object> overlayProcessVar) {
		LOGGER.info("updateDistributionCenter invoked for serviceId::{}", overlayId);
		Integer componentId=null;
		for(Integer cpeOverlayComponentId:cpeOverlayComponentIds){
			LOGGER.info("cpeOverlayComponentId invoked for serviceId::{}", cpeOverlayComponentId);
			componentId=cpeOverlayComponentId;
			ScComponentAttribute scCompAttr=scComponentAttributesRepository.findFirstByScServiceDetailIdAndScComponent_idAndAttributeName(overlayId, cpeOverlayComponentId,"destinationState");
			if(scCompAttr!=null && scCompAttr.getAttributeValue()!=null){
				LOGGER.info("destinationState exists for cpeOverlayComponentId invoked for serviceId::{}", cpeOverlayComponentId);
				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scCompAttr.getAttributeValue());
				if(distributionCenterMapping!=null && !distributionCenterMapping.isEmpty()){
					LOGGER.info("distributionCenterMapping exists for serviceId::{}", distributionCenterMapping.size());
					MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
							.stream().findFirst().orElse(null);
					Map<String,String> distributionCenterMap=new HashMap<>();
					if(mstStateToDistributionCenterMapping!=null && mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()!=null) {
						LOGGER.info("DistributionCenterMapping exists for cpeOverlayComponentId invoked for cpeComponentId::{}", cpeOverlayComponentId);
						distributionCenterMap.put("distributionCenterName", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
						distributionCenterMap.put("distributionCenterState", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterState());
						distributionCenterMap.put("distributionCenterPLant", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
						distributionCenterMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
					}
					LOGGER.info("CpeComponentId::{},SiteType::{}",scCompAttr.getScComponent().getId(), scCompAttr.getScComponent().getSiteType());
					componentAndAttributeService.updateAttributes(overlayId, distributionCenterMap,
							AttributeConstants.COMPONENT_LM,scCompAttr.getScComponent().getSiteType());
				}
			}
		}
		
		if(componentId!=null){
			LOGGER.info("CpecomponentId::{} exists",componentId);
			List<String> cpeDetailList = new ArrayList<>();
			cpeDetailList.add("CPE");
			cpeDetailList.add("cpeManagementType");
			List<ScComponentAttribute> scCompAttrs=scComponentAttributesRepository.findByScComponent_idAndAttributeNameIn(componentId,cpeDetailList);
			if(scCompAttrs!=null && !scCompAttrs.isEmpty()){
				LOGGER.info("CPE and CPEMgmtType::{} exists",scCompAttrs.size());
				scCompAttrs.stream().forEach(scCompAttr ->{
					if("CPE".equalsIgnoreCase(scCompAttr.getAttributeName()) && scCompAttr.getAttributeValue()!=null){
						LOGGER.info("CPE attr::{} exists",scCompAttr.getAttributeValue());
						if(scCompAttr.getAttributeValue().contains("Rental")) {
							overlayProcessVar.put("cpeType","Rental" );
						} else if (scCompAttr.getAttributeValue().contains("Outright")) {
							overlayProcessVar.put("cpeType", "Outright");
						} else {
							overlayProcessVar.put("cpeType", scCompAttr.getAttributeValue());
						}
					}
					if("cpeManagementType".equalsIgnoreCase(scCompAttr.getAttributeName()) && "Fully Managed".equalsIgnoreCase(scCompAttr.getAttributeValue())){
						LOGGER.info("cpeManagementType attr::{} exists",scCompAttr.getAttributeValue());
						overlayProcessVar.put("cpeSiScope", "Supply, Installation, Support");
						overlayProcessVar.put("isCPEConfiguredByCustomer", false);
						overlayProcessVar.put("isCPEArrangedByCustomer", false);
					}
				});
			}
		}
	}

	@Transactional(readOnly=false)
	public Boolean processSDWANCPETest(String orderCode, Integer serviceId,boolean survey, boolean clrFlow,boolean acceptance,boolean exceptionalFlow,boolean offnetClrFlow,boolean cpeConfigurationFlow,boolean sdwanPlanFlow, boolean cgwFlow,boolean solutionFlow) {
		Boolean status = true;
		try {
			
			
			
			if(cpeConfigurationFlow) {
				LOGGER.info("sdwan-cpe-workflow");
				List<String> cpeOverlayServiceIdList= new ArrayList<>();
				cpeOverlayServiceIdList.add("091OVERLAYCOMP1");
				cpeOverlayServiceIdList.add("091OVERLAYCOMP2");
				cpeOverlayServiceIdList.add("091OVERLAYCOMP3");
				cpeOverlayServiceIdList.add("091OVERLAYCOMP4");
				/*processVar.put("cpeOverlayServiceIds",cpeOverlayServiceIdList);
				processVar.put("serviceIdsCount",cpeOverlayServiceIdList.size());
				runtimeService.startProcessInstanceByKey("test-cpe-workflow", processVar);*/
			}else if(sdwanPlanFlow) {
				LOGGER.info("sdwanPlanFlow");
				List<String> componentGroups = new ArrayList<>();
				componentGroups.add("OVERLAY");
				componentGroups.add("UNDERLAY");
				ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
				Set<ScServiceDetail> scServiceDetails=scOrder.getScServiceDetails();
				Map<Integer,ScServiceDetail> scServiceDetailMap= new HashMap<>();
				for(ScServiceDetail scServiceDetail:scServiceDetails){
					scServiceDetailMap.put(scServiceDetail.getId(), scServiceDetail);
				}
				LOGGER.info("ScServiceDetailMap::{}",scServiceDetailMap);
				ScServiceDetail scSolutionServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidAndErfPrdCatalogProductNameOrderByIdDesc(orderCode,"IZOSDWAN_SOLUTION");
				Integer solutionId=scSolutionServiceDetail.getId();
				/*List<ScSolutionComponent> scSolutionComponentList = scSolutionComponentRepository
						.findAllByOrderCodeAndComponentGroupAndIsActive(orderCode, "OVERLAY", "Y");
				for (ScSolutionComponent scSolutionComponent : scSolutionComponentList) {*/
					//LOGGER.info("Overlay Id: {}", scSolutionComponent.getScServiceDetail1().getId());
					List<Map<String, Integer>> layDetails = scSolutionComponentRepository
							.findScServiceDetailByComponentType(orderCode, componentGroups, "Y");
					if (layDetails != null && !layDetails.isEmpty()) {
						for (Map<String, Integer> overUnderlayMap : layDetails) {
							LOGGER.info("Underlay Ids: {}", overUnderlayMap.get("underlayIds"));
							LOGGER.info("Overlay Ids: {}", overUnderlayMap.get("overlayIds"));
							String underlays = String.valueOf(overUnderlayMap.get("underlayIds"));
							LOGGER.info("Underlay Ids{}", underlays);
							String[] underlayList = underlays.split(",");
							for (String underlay : underlayList) {
								Integer underlayId = Integer.valueOf(underlay);
								if (scServiceDetailMap.containsKey(underlayId)) {
									Map<String, Object> processVar = new HashMap<>();
									LOGGER.info("Underlay flow map{}", processVar);
									if (scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName()
											.equalsIgnoreCase("BYON Internet")) {
										try{
											LOGGER.info("BYON Underlay{}", underlayId);
											processVar.put(SERVICE_ID, underlayId);
											processVar.put(SC_ORDER_ID, scOrder.getId());
											processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
											processVar.put(ORDER_TYPE,
													StringUtils.trimToEmpty(scServiceDetailMap.get(underlayId).getOrderType()));
											processVar.put("root_endDate", new Timestamp(new Date().getTime()));
											processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(
													scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName()));
											processVar.put("processType", "computeProjectPLan");
											processVar.put("site_type", "A");
											processVar.put(SERVICE_CODE, scServiceDetailMap.get(underlayId).getUuid());
											processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
											processVar.put("remainderCycle", reminderCycle);
											processVar.put("overlayId", overUnderlayMap.get("overlayIds"));
											processVar.put("solutionId", solutionId);
											processVar.put("byon-config", "byon-config");
											processVar.put("O2CPROCESSKEY", "sdwan-byon-ill-underlay-workflow");
											LOGGER.info("BYON ProcessVar:{}", processVar);
											// runtimeService.startProcessInstanceByKey("test-byon-ill-plan-workflow",
											// processVar);
											runtimeService.startProcessInstanceByKey("sdwan-byon-ill-underlay-workflow",
													processVar);
											persistDownTimeDetails(scServiceDetailMap.get(underlayId),
													solutionId, scOrder.getOpOrderCode(),
													"BYON Internet", false, false, false, false,false,false);
											updateO2CTriggerStatus("SUCCESS",solutionId,underlayId);
										}catch(Exception e){
											updateO2CTriggerStatus("FAILURE",solutionId,underlayId);
											LOGGER.error("BYON O2C Trigger Failed:{}",e);
										}
									} else if (scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName()
											.equalsIgnoreCase("IAS")
											|| scServiceDetailMap.get(underlayId).getErfPrdCatalogProductName()
													.equalsIgnoreCase("GVPN")) {
										LOGGER.info("ILL or GVPN Underlay{}", underlayId);
										processIASGVPNUnderLay(scServiceDetailMap.get(underlayId),
												overUnderlayMap.get("overlayIds"),
												solutionId,null,null);
									}
								}
							}

							Integer overlayId = overUnderlayMap.get("overlayIds");
							LOGGER.info("Overlay Id{}", overlayId);
							if (scServiceDetailMap.containsKey(overlayId)) {
								try{
									LOGGER.info("ScServiceDetailMap exists for Overlay Id{}", overlayId);
									Map<String, Object> overlayProcessVar = new HashMap<>();
									overlayProcessVar.put(SERVICE_ID, overlayId);
									overlayProcessVar.put(SC_ORDER_ID, scOrder.getId());
									overlayProcessVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
									overlayProcessVar.put(ORDER_TYPE,
											StringUtils.trimToEmpty(scServiceDetailMap.get(overlayId).getOrderType()));
									overlayProcessVar.put(SERVICE_CODE, scServiceDetailMap.get(overlayId).getUuid());
									overlayProcessVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
									overlayProcessVar.put(PRODUCT_NAME, StringUtils
											.trimToEmpty(scServiceDetailMap.get(overlayId).getErfPrdCatalogProductName()));
									overlayProcessVar.put("root_endDate", new Timestamp(new Date().getTime()));
									overlayProcessVar.put("processType", "computeProjectPLan");
									overlayProcessVar.put("isAccountRequired", false);
									overlayProcessVar.put("site_type", "A");
									overlayProcessVar.put("remainderCycle", reminderCycle);
									overlayProcessVar.put("solutionId", solutionId);
									overlayProcessVar.put("isStandardCPEDiscount", true);
									overlayProcessVar.put("cpe-config", "cpe-config");

									overlayProcessVar.put("isCPERequired", true);
									overlayProcessVar.put("isStandardCPEDiscount", true);
									overlayProcessVar.put("cpeLicensePRCompleted", true);
									overlayProcessVar.put("cpeHardwarePRCompleted", true);
									overlayProcessVar.put("isCpeAvailableInInventory", false);
									overlayProcessVar.put("cpeLicenseNeeded", true);
									overlayProcessVar.put("cpeConfigurationCompleted", true);

									/*int underlayCpeCount = scSolutionComponentRepository
									.findUnderlayBasedOnOverlayAndCpe(overlayId);*/
									List<Integer> cpeOverlayComponentIds = scComponentRepository
											.findComponentIdScServiceDetailId(overlayId);
									overlayProcessVar.put("isSiteBExists", false);
									overlayProcessVar.put("serviceIdsCount", 0);
									overlayProcessVar.put("cpeOverlayComponentIds", new ArrayList<Integer>());
									if (cpeOverlayComponentIds!=null && !cpeOverlayComponentIds.isEmpty()){
										LOGGER.info("cpeOverlayComponentIds exists:{}", cpeOverlayComponentIds.size());
										if(cpeOverlayComponentIds.size() > 1){
											LOGGER.info("UnderlayCpeCount greater than one which is: {} for OverlayId: {}",
													cpeOverlayComponentIds.size(), overlayId);
											overlayProcessVar.put("isSiteBExists", true);
											overlayProcessVar.put("siteBType", "B");
										}
										overlayProcessVar.put("serviceIdsCount", cpeOverlayComponentIds.size());
										overlayProcessVar.put("cpeOverlayComponentIds", cpeOverlayComponentIds);
									}
									overlayProcessVar.put("O2CPROCESSKEY", "sdwan-overlay-workflow");
									updateDistributionCenterAndCpeDetails(cpeOverlayComponentIds, overlayId, overlayProcessVar);
									// runtimeService.startProcessInstanceByKey("test-plan-overlay-workflow",
									// overlayProcessVar);
									LOGGER.info("Overlay flow map{}", overlayProcessVar);
									runtimeService.startProcessInstanceByKey("sdwan-overlay-workflow", overlayProcessVar);
									persistDownTimeDetails(scServiceDetailMap.get(overlayId),
											solutionId, scOrder.getOpOrderCode(),
											"OVERLAY", false, false, false, true,false,false);
									updateO2CTriggerStatus("SUCCESS",solutionId,overlayId);
								}catch(Exception e){
									updateO2CTriggerStatus("FAILURE",solutionId,overlayId);
									LOGGER.error("Overlay O2C Trigger Failed:{}",e);
								}
							}
						}
					}
				
			}else if(cgwFlow){
				LOGGER.info("cgwFlow");
				ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
				Set<ScServiceDetail> scServiceDetails=scOrder.getScServiceDetails();
				Map<Integer,ScServiceDetail> scServiceDetailMap= new HashMap<>();
				for(ScServiceDetail scServiceDetail:scServiceDetails){
					scServiceDetailMap.put(scServiceDetail.getId(), scServiceDetail);
				}
				ScServiceDetail scSolutionServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidAndErfPrdCatalogProductNameOrderByIdDesc(orderCode,"IZOSDWAN_SOLUTION");
				Integer solutionId=scSolutionServiceDetail.getId();
				List<ScSolutionComponent> scSolutionComponentList=scSolutionComponentRepository.findAllByOrderCodeAndComponentGroupAndIsActive(orderCode,"CGW","Y");
				for(ScSolutionComponent scSolutionComponent:scSolutionComponentList){
					ScServiceDetail cgwScServiceDetail = scSolutionComponent.getScServiceDetail1();
					try{
						Map<String, Object> cgwProcessVar = new HashMap<>();
						cgwProcessVar.put(SERVICE_ID, cgwScServiceDetail.getId());
						cgwProcessVar.put(SC_ORDER_ID, scOrder.getId());
						cgwProcessVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
						cgwProcessVar.put(ORDER_TYPE, StringUtils.trimToEmpty(cgwScServiceDetail.getOrderType()));
						cgwProcessVar.put(SERVICE_CODE, scSolutionComponent.getServiceCode());
						cgwProcessVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
						cgwProcessVar.put(PRODUCT_NAME,
								StringUtils.trimToEmpty(cgwScServiceDetail.getErfPrdCatalogProductName()));
						cgwProcessVar.put("root_endDate", new Timestamp(new Date().getTime()));
						cgwProcessVar.put("processType", "computeProjectPLan");
						cgwProcessVar.put("isAccountRequired", false);
						cgwProcessVar.put("site_type", "A");
						cgwProcessVar.put("remainderCycle", reminderCycle);
						cgwProcessVar.put("solutionId", solutionId);
						cgwProcessVar.put("createServiceCompleted", false);
						cgwProcessVar.put("isCLRSyncCallSuccess", false);
						cgwProcessVar.put("serviceDesignCompleted", false);
						if (cgwScServiceDetail.getPrimarySecondary().equalsIgnoreCase("Primary")) {
							cgwProcessVar.put("O2CPROCESSKEY", "sdwan-py-cgw-workflow");
							cgwProcessVar.put("cgwType", "Primary");
							LOGGER.info("Py CGW completed Flow Trigger::{}", cgwProcessVar);
							runtimeService.startProcessInstanceByKey("sdwan-py-cgw-workflow", cgwProcessVar);
						} else if (cgwScServiceDetail.getPrimarySecondary().equalsIgnoreCase("Secondary")) {
							cgwProcessVar.put("O2CPROCESSKEY", "sdwan-sy-cgw-workflow");
							cgwProcessVar.put("cgwType", "Secondary");
							LOGGER.info("Sy CGW completed Flow Trigger::{}", cgwProcessVar);
							runtimeService.startProcessInstanceByKey("sdwan-sy-cgw-workflow", cgwProcessVar);
						}
						LOGGER.info("Update O2C Trigger Status::{}", cgwScServiceDetail.getId());
						updateO2CTriggerStatus("SUCCESS",solutionId,cgwScServiceDetail.getId());
					}catch(Exception e){
						updateO2CTriggerStatus("FAILURE",solutionId,cgwScServiceDetail.getId());
						LOGGER.error("Error for CGW O2C Trigger:{}", e);
					}
				}
			}else if(solutionFlow){
				LOGGER.info("solutionFlow");
				ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
				List<ScServiceDetail> solutionServiceList=scServiceDetailRepository.findByScOrderIdAndProductName(scOrder.getId(),"IZOSDWAN_SOLUTION");
				for(ScServiceDetail solutionService:solutionServiceList){
					Map<String, Object> solutionProcessVar = new HashMap<>();
					solutionProcessVar.put(SERVICE_ID, solutionService.getId());
					solutionProcessVar.put(SC_ORDER_ID, scOrder.getId());
					solutionProcessVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
					solutionProcessVar.put(ORDER_TYPE, StringUtils.trimToEmpty(solutionService.getOrderType()));
					solutionProcessVar.put(SERVICE_CODE, solutionService.getUuid());
					solutionProcessVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
					solutionProcessVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(solutionService.getErfPrdCatalogProductName()));
					solutionProcessVar.put("root_endDate", new Timestamp(new Date().getTime()));
					solutionProcessVar.put("processType", "computeProjectPLan");
					solutionProcessVar.put("site_type", "A");
					solutionProcessVar.put("remainderCycle", reminderCycle);
					solutionProcessVar.put("O2CPROCESSKEY", "sdwan-lld-workflow");
					LOGGER.info("Solution Flow Trigger::{}",solutionProcessVar);
					runtimeService.startProcessInstanceByKey("sdwan-lld-workflow", solutionProcessVar);
				}
			}
			LOGGER.info("processSDWANCPETest completed");
		} catch (Exception e) {

			LOGGER.error("Error processing data", e);
			status = false;
		}
		return status;
	}

	private void persistDownTimeDetails(ScServiceDetail scServiceDetail,Integer solutionId,String orderCode,String productName, boolean isCpeAlreadyManaged,boolean isIpRequired, boolean isTxRequired,boolean isCpeRequired,boolean isCpeCustomerAppointmentRequired,boolean isLMTestRequired) {
		LOGGER.info("persistDownTimeDetails for service id::{},isCpeAlreadyManaged::{},Ip::{},Tx::{},Cpe::{}",scServiceDetail.getId(),isCpeAlreadyManaged,isIpRequired,isTxRequired,isCpeRequired);
		DownTimeDetails downTimeDetails=new DownTimeDetails();
		downTimeDetails.setScServiceDetailId(scServiceDetail.getId());
		downTimeDetails.setSolutionId(solutionId);
		downTimeDetails.setOrderCode(orderCode);
		downTimeDetails.setCreatedBy(scServiceDetail.getCreatedBy());
		downTimeDetails.setCreatedDate(new Timestamp(new Date().getTime()));
		downTimeDetails.setUpdatedDate(new Timestamp(new Date().getTime()));
		downTimeDetails.setUpdatedBy(scServiceDetail.getUpdatedBy());
		downTimeDetails.setIsCpeAlreadyManaged(isCpeAlreadyManaged==true?"Y":"NA");
		downTimeDetails.setIsByonReadyForDownTime("N");
		downTimeDetails.setIsByonReadyForCustomerAppointment("N");
		downTimeDetails.setIsProvisionReadyForDownTime("N");
		downTimeDetails.setIsProvisionReadyForCustomerAppointment("N");
		downTimeDetails.setIsIpDownTimeRequired(isIpRequired==false?"N":"NA");
		downTimeDetails.setIsIpReadyForDownTime("NA");
		downTimeDetails.setIsTxDownTimeRequired(isTxRequired==false?"N":"NA");
		downTimeDetails.setIsTxReadyForDownTime("NA");
		downTimeDetails.setIsCpeDowntimeRequired(isCpeRequired==true?"Y":"NA");
		downTimeDetails.setIsCpeReadyForDownTime("N");
		downTimeDetails.setIsLMTestRequired(isLMTestRequired==true?"Y":"NA");
		downTimeDetails.setIsLMReadyForCustomerAppointment("N");
		downTimeDetails.setIsCpeCustomerAppointmentRequired(isCpeCustomerAppointmentRequired==true?"Y":"NA");
		downTimeDetails.setIsCpeReadyForCustomerAppointment("N");
		downTimeDetails.setIsConfigCompleted("N");
		downTimeDetails.setIsE2ECompleted("N");
		downTimeDetails.setProductName(productName);
		downTimeDetailsRepository.save(downTimeDetails);
	}

	@Transactional(readOnly=false)
	public void processIASGVPNUnderLay(ScServiceDetail scServiceDetail,Integer overlayId, Integer scSolutionId, String flowTriggerType, Map<String, String> scComponentAttributesAMap) throws TclCommonException {
		try {
			boolean isP2PwithoutBH = false;
			boolean isP2PwithBH = false;
			String lmType="";
			String lastMileType="";
			Boolean isValidLM  = false;
			Boolean skipOffnet = false;
			boolean isIpRequired=false;
			boolean isTxRequired=false;
			Map<String, Object> processVar = new HashMap<>();
			LOGGER.info("Input processUnderLay received for L2O serviceId:: {} service code:: {}",scServiceDetail.getId(), scServiceDetail.getUuid());
			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			String orderSubCategory=scServiceDetail.getOrderSubCategory();
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

			orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
			orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = scContractInfos.stream().findFirst().orElse(null);

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ",scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",scServiceAttribute.getId(),scServiceAttribute.getAttributeName(),scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}
			
			String oldEquipmentModel=null;
			if("MACD".equalsIgnoreCase(orderType) && !"ADD_SITE".equals(orderCategory)
					&& orderSubCategory!=null && !orderSubCategory.toLowerCase().contains("parallel") && scServiceDetail.getServiceLinkId()!=null){
				LOGGER.info("MACD:: Equipment Model Attr retrival for parent Id::{}",scServiceDetail.getServiceLinkId());
				ScServiceAttribute eqpModelAttr=scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(scServiceDetail.getServiceLinkId(), "EQUIPMENT_MODEL");
				if(eqpModelAttr!=null && eqpModelAttr.getAttributeValue()!=null && !eqpModelAttr.getAttributeValue().isEmpty()){
					LOGGER.info("Equipment Model Attr exists with value::{}",eqpModelAttr.getAttributeValue());
					oldEquipmentModel=eqpModelAttr.getAttributeValue();
				}
			}

			if (scOrder.getErfCustCustomerName() != null) {
				processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
			}
			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(scServiceDetail.getOrderType()));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(scServiceDetail.getOrderCategory()));
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				processVar.put("parentServiceCode", null);
				processVar.put("parentLmType", null);
				processVar.put("txResourcePathType", null);
				processVar.put("isParallelExists", "false");
				processVar.put("isAmendedOrder", false);
				processVar.put("overlayId",overlayId);
				processVar.put("solutionId", scSolutionId);
				processVar.put("flowType", flowTriggerType);
				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())){
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				}else{
					processVar.put("orderSubCategory", "NA");
				}
				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory()) && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){
					LOGGER.info("ProcessL2O:ParallelExists");
					processVar.put("isParallelExists", "true");
					if(Objects.nonNull(scServiceDetail.getParentUuid())){
						processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
					}
					/*ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(), "downtime_duration",
									"IAS Common");
					if(Objects.nonNull(scServiceDownTimeAttr) && Objects.nonNull(scServiceDownTimeAttr.getAttributeValue()) && !scServiceDownTimeAttr.getAttributeValue().isEmpty()){
						LOGGER.info("ProcessL2O:ParallelDownTime");
						processVar.put("parallelDownTime", "PT"+scServiceDownTimeAttr.getAttributeValue()+"D");
					}else{
						LOGGER.info("ProcessL2O:ParallelDownTime is 0");
						processVar.put("parallelDownTime", "PT0M");
					}*/
				}
			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

			if (scServiceDetail != null) {
				
				//GVPN:UPDATE SITE TYPE BASED ON VPN TOPOLOGY
				if("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())){
					LOGGER.info("update vpn details");
					ScServiceAttribute siteTypeServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"Site Type","GVPN Common");
					if(Objects.nonNull(siteTypeServiceAttribute)){
						ScServiceAttribute gvpnCommonServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"VPN Topology","GVPN Common");
						if(Objects.nonNull(gvpnCommonServiceAttribute)){
							LOGGER.info("VPN common");
							updateVpnDetail(gvpnCommonServiceAttribute,siteTypeServiceAttribute);
						}
						ScServiceAttribute sitePropServiceAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),"VPN Topology","SITE_PROPERTIES");
						if(Objects.nonNull(sitePropServiceAttribute)){
							LOGGER.info("VPN properties");
							updateVpnDetail(sitePropServiceAttribute,siteTypeServiceAttribute);
						}
					}
				}
				

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				lastMileType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"));

				String lastMileProvider = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastMileProvider"));

				String connectedBuilding = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_Building_tag"));
				String connectedCustomer = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_cust_tag"));
				String connectedCustomerCategory = StringUtils.trimToEmpty(feasibilityAttributes.get("Orch_Category"));
				String btsDeviceType = StringUtils.trimToEmpty(feasibilityAttributes.get("bts_device_type")).toLowerCase();
				
				String solutionType = StringUtils.trimToEmpty(feasibilityAttributes.get("solution_type")).toLowerCase();
				/*String cpeChassisChanged = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_chassis_changed"));
				String cpeVariant = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_variant"));
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));*/
				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));
				
				String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
				String upgradeType = StringUtils.trimToEmpty(feasibilityAttributes.get("upgrade_type"));
				
				Double portBW = 0.0;
				
				String bwPortspeed	=scComponentAttributesAMap.get("portBandwidth");
				if (bwPortspeed != null
						&& NumberUtils.isCreatable(bwPortspeed)) {
					portBW = Double.valueOf(bwPortspeed);
				}
				//Have to handle in overlay MACD for CPE attributes similar to standalone IAS and GVPN
				/*LOGGER.info("cpeType for serviceid :{} and service code:{} and map:: {} cpeChassisChanged::{} cpeVariant::{} lastMileType::{}", scServiceDetail.getId(),scServiceDetail.getUuid(),serviceDetailsAttributes,cpeChassisChanged,cpeVariant,lastMileType);


				String cpeType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE"));
				String cpeModel = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE Basic Chassis"));
				
				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{}", scServiceDetail.getId(),scServiceDetail.getUuid(),cpeType,cpeModel);
				String cpeSiScope = "Customer provided";
				if (cpeType.contains("Rental")) {
					cpeType ="Rental";
					processVar.put("cpeType",cpeType );
				} else if (cpeType.contains("Outright")) {
					cpeType ="Outright";
					processVar.put("cpeType", cpeType);
				} else {
					processVar.put("cpeType", cpeType);
				}*/

				boolean isLMRequired = true;
				boolean isColoRequired = false;
				boolean negotiationRequiredForOffnetLM = false;
				boolean rowRequired = true;
				boolean prowRequired = true;
				boolean isRFRequired = false;
				

				String bhConnectivity = StringUtils.trimToEmpty(feasibilityAttributes.get("BHConnectivity"));				
				String providerName = StringUtils.trimToEmpty(feasibilityAttributes.get("closest_provider_bso_name"));
				
				if(StringUtils.isBlank(providerName))providerName = StringUtils.trimToEmpty(serviceDetailsAttributes.get("closest_provider_bso_name"));
				
				processVar.put(IS_CONNECTED_SITE, false);
				processVar.put("checkCLRSuccess", false);
				processVar.put(IS_CONNECTED_BUILDING, false);
				processVar.put("isP2PwithoutBH", isP2PwithoutBH);
				processVar.put("isP2PwithBH", isP2PwithBH);
				processVar.put("parallelDownTime", "PT30M");
				processVar.put("isAccountRequired",false);
				
				if(lastMileType.toLowerCase().contains("onnet wireless"))lastMileType="OnnetRF";
				else if(lastMileType.toLowerCase().contains("offnet wireless"))lastMileType="OffnetRF";
				else if(lastMileType.toLowerCase().contains("offnet wireline"))lastMileType="OffnetWL";
				else if(lastMileType.toLowerCase().contains("onnet wireline"))lastMileType="OnnetWL";
				else if(lastMileType.toLowerCase().contains("man")|| lastMileType.toLowerCase().contains("wan aggregation"))lastMileType="OnnetWL";

				//if(StringUtils.isBlank(lastMileType))lastMileType="OnnetWL";
				processVar.put("txRequired", false);
				lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileType.toLowerCase().contains("onnetrf") || lastMileType.toLowerCase().equalsIgnoreCase("Onnet Wireless")) {
					LOGGER.info("onnetrf case for Service ID:: {}", scServiceDetail.getUuid());
                    lmScenarioType = "Onnet Wireless";
                    lmConnectionType = "Wireless";
                    lmType = "onnet";
                    processVar.put("sameDayInstallation", false);
                    processVar.put("rfSiteFeasible", true);
                    processVar.put("mastApprovalRequired", true);
                    isValidLM = true;

					if(Objects.nonNull(solutionType) && !solutionType.isEmpty() && "ubrp2pmp".equals(solutionType))
					{
						solutionType = "Radwin from TCL POP";
						LOGGER.info("radwin/ubrp2pmp");
						isP2PwithoutBH = true;
						isColoRequired = true;
						Map<String, String> attrMap= new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
					}

					if(StringUtils.isBlank(providerName) || Objects.isNull(providerName))
					{
						providerName= StringUtils.trimToEmpty(lastMileProvider);
						LOGGER.info("providerName set from lastMileProvider as {} for Service ID: {}", lastMileProvider, scServiceDetail.getUuid());
					}
					
                    if(providerName.toLowerCase().contains("tcl") && providerName.toLowerCase().contains("radwin") && !providerName.toLowerCase().contains("pmp")) {
                    	LOGGER.info("tcl/radwin/not pmp");
                    	isP2PwithoutBH = true;
                    	isColoRequired = true;
                    }else if(providerName.toLowerCase().contains("backhaul") && providerName.toLowerCase().contains("radwin") && !providerName.toLowerCase().contains("pmp")) {
                    	LOGGER.info("backhaul/radwin/not pmp");
                    	isP2PwithBH = true;
                        isColoRequired = true;
                     }else if(providerName.toLowerCase().contains("radwin") && !providerName.toLowerCase().contains("pmp")) {
                    	LOGGER.info("radwin/not pmp");
                     	isP2PwithoutBH = true;
                     	isColoRequired = true;
                     }else if(providerName.toLowerCase().contains("p2p") && !providerName.toLowerCase().contains("pmp")) {
 						LOGGER.info("p2p/not pmp");
 						providerName = "Radwin from TCL POP";
 						Map<String, String> attrMap= new HashMap<>();
 						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
 						attrMap.put("solution_type", "Radwin from TCL POP");
 						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
 						Map<String, String> atMap = new HashMap<>();
 						atMap.put("lastMileProvider", providerName);
 						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
 								AttributeConstants.COMPONENT_LM, "A");
 						isP2PwithoutBH = true;
 						isColoRequired = true;
                      } else if(StringUtils.isBlank(providerName) && !StringUtils.isBlank(solutionType) && solutionType.toLowerCase().contains("p2p") && !solutionType.toLowerCase().contains("pmp")) {
 						LOGGER.info("p2p from solution type/not pmp/provider is blank");
 						providerName = "Radwin from TCL POP";
 						Map<String, String> attrMap = new HashMap<>();
 						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
 						attrMap.put("solution_type", "Radwin from TCL POP");
 						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
 						Map<String, String> atMap = new HashMap<>();
 						atMap.put("lastMileProvider", providerName);
 						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
 								AttributeConstants.COMPONENT_LM, "A");
 						isP2PwithoutBH = true;
 						isColoRequired = true;
 					} else if(StringUtils.isBlank(providerName) && StringUtils.isBlank(solutionType)){
 						isValidLM = false;
 						LOGGER.info("service code::{} Not a valid LM::Provider Name & Solution Type is blank",scServiceDetail.getUuid());
                     }
                    
                    processVar.put("isP2PwithoutBH", isP2PwithoutBH);
    				processVar.put("isP2PwithBH", isP2PwithBH);
    				
    				if(StringUtils.isBlank(bhConnectivity)) {
    					if(providerName.equalsIgnoreCase("Radwin from TCL POP")) {
    						bhConnectivity="Radwin from TCL POP";
    					}
    				}
                    
    				if(isP2PwithBH) {
	                    if(StringUtils.isNotBlank(bhConnectivity) && !(bhConnectivity.equalsIgnoreCase("NA") || bhConnectivity.equalsIgnoreCase("ONNET") || bhConnectivity.contains("TCL"))) {
	                    	   LOGGER.info("txRequired for serviceid::{}",scServiceDetail.getId());
	                    	   processVar.put("txRequired", true);
	                           isTxRequired=true;
	                    }else{
	                           processVar.put("txRequired", false);
	                    }
    				}else {
    					 processVar.put("txRequired", false);
    				}
                    processVar.put("skipOffnet", false);
				} else if (lastMileType.toLowerCase().contains("offnetrf") || lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireless")) {
					LOGGER.info("offnetrf");
                    lmScenarioType = "Offnet Wireless";
                    lmConnectionType = "Wireless";
                    lmType = "offnet";
                    isValidLM = true;
                    processVar.put("sameDayInstallation", false);
                    processVar.put("rfSiteFeasible", true);
                    processVar.put("mastApprovalRequired", true);
                    processVar.put("txRequired", false);
                    String supplierOldlocalLoopBw="0.0";
                    if(feasibilityAttributes.containsKey("old_Ll_Bw")){
                    	supplierOldlocalLoopBw=feasibilityAttributes.get("old_Ll_Bw");
                    }
                    if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
							|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
							Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
							&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
                    				
							|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
							Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
							&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {
                    	
                    	if(StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
                    		LOGGER.info("SkipOffnet False");
                        	processVar.put("skipOffnet", false);
                    	}else {
                    		LOGGER.info("SkipOffnet True");                    	
	                    	processVar.put("skipOffnet", true);
	                    	skipOffnet = true;                    		
                    	}
                    }else{
                    	LOGGER.info("SkipOffnet False");
                    	processVar.put("skipOffnet", false);
                    }
                    LOGGER.info("service code::{} supplierOldlocalLoopBw::{},skipOffnet::{}",scServiceDetail.getUuid(),portBW,supplierOldlocalLoopBw,skipOffnet);
				}else if (lastMileType.toLowerCase().contains("offnetwl") || lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireline")) {
					LOGGER.info("offnetwl");
					lmScenarioType = "Offnet Wireline";
                    isValidLM = true;
				
					lmConnectionType = "Wireline";
					lmType = "offnet";
					processVar.put("isMuxIPAvailable", false);
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					processVar.put("skipOffnet", false);
					isTxRequired=true;
					 String supplierOldlocalLoopBw="0.0";
	                    if(feasibilityAttributes.containsKey("old_Ll_Bw")){
	                    	supplierOldlocalLoopBw=feasibilityAttributes.get("old_Ll_Bw");
	                    }
	                  
	                    if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
	                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
								|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
	                    				
								|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {
	                    	
	                    	if(StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
	                    		LOGGER.info("SkipOffnet False");
	                        	processVar.put("skipOffnet", false);
	                    	}else {
	                    		LOGGER.info("SkipOffnet True");                    	
		                    	processVar.put("skipOffnet", true);
		                    	skipOffnet = true;                    		
	                    	}
	                    }else{
	                    	LOGGER.info("SkipOffnet False");
	                    	processVar.put("skipOffnet", false);
	                    }
	                    
	                    LOGGER.info("service code::{} supplierOldlocalLoopBw::{},skipOffnet::{}",scServiceDetail.getUuid(),portBW,supplierOldlocalLoopBw,skipOffnet);
				} else if (lastMileType.toLowerCase().contains("onnetwl") || lastMileType.toLowerCase().equalsIgnoreCase("onnet wireline")) {
					LOGGER.info("onnetwl");
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileType="OnnetWL";
					processVar.put("skipOffnet", false);
					isValidLM =true;
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					isTxRequired=true;
					if(connectedCustomer.contains("1") || connectedCustomerCategory.equalsIgnoreCase("Connected Customer") || (StringUtils.trimToEmpty(scServiceDetail.getOrderType()).equalsIgnoreCase("MACD")
							&& ((StringUtils.trimToEmpty(scServiceDetail.getOrderCategory()).equalsIgnoreCase("CHANGE_BANDWIDTH")
                                    && StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Hot Upgrade")
                                    &&  StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Hot Downgrade"))
									|| StringUtils.trimToEmpty(scServiceDetail.getOrderCategory()).equalsIgnoreCase("ADD_IP")))) {
						LOGGER.info("ConnectedSite");
						connectedCustomer="1";
						processVar.put(IS_CONNECTED_SITE, true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put("isMuxIPAvailable", false);
						rowRequired = false;
						prowRequired = false;
						lmScenarioType = "Onnet Wireline - Connected Customer";
					} else if (connectedBuilding.contains("1")) {
						LOGGER.info("ConnectedBuilding");
						rowRequired = false;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put("isNetworkSelectedPerBOP", true);
						processVar.put(IS_CONNECTED_BUILDING, true);
						processVar.put(IS_CONNECTED_SITE, false);
						lmScenarioType = "Onnet Wireline - Connected Building";
					} else {
						LOGGER.info("Near Connect");
						rowRequired = true;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put("isNetworkSelectedPerBOP", true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put(IS_CONNECTED_SITE, false);
						lmScenarioType = "Onnet Wireline - Near Connect";
					}
				}
				if(scComponentAttributesAMap.get("destinationCity")!=null && !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
                    processVar.put("tier",setTierValue(scComponentAttributesAMap.get("destinationCity")));
				}
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);
				processVar.put("bhConnectivity", bhConnectivity);
				
				Map<String, String> atMap = new HashMap<>();
				
				if (lastMileType.toLowerCase().contains("offnet")) {

					LOGGER.info("getDestinationState:{} and getSourceState:{}", scComponentAttributesAMap.get("destinationState"),
							scComponentAttributesAMap.get("sourceState"));

					if (scComponentAttributesAMap.get("destinationState") != null && scComponentAttributesAMap.get("sourceState") != null
							&& !scComponentAttributesAMap.get("destinationState")
									.equalsIgnoreCase(scComponentAttributesAMap.get("sourceState"))) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);

					}

					if(StringUtils.isNotBlank(supplierLocalLoopBw) && !supplierLocalLoopBw.equals(localLoopBW)) {
						LOGGER.info("supplierLocalLoopBw-and-localLoopBandwidth-not-same-updating-supplierLocalLoopBw= {} and {}", supplierLocalLoopBw,localLoopBW);
						atMap.put("localLoopBandwidth",supplierLocalLoopBw);
					}
					
					
					Double supplierLocalLoopBwDouble = 0.0;
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}
					
					Double offnetWlNrc = 0.0;							
					if(feasibilityAttributes.get("lm_otc_nrc_installation_offwl")!=null)  offnetWlNrc= Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf")!=null) offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and Bandwidth is {} ", offnetWlNrc,offnetRFNrc, supplierLocalLoopBwDouble);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000 )|| supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true");
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}

				LOGGER.info(
						"Input processL2ODataToFlowable received for lmScenarioType::{}  isTaxAvailable::{} btsDeviceType::{} serviceId:: {} service code:: {}",
						lmScenarioType, isTaxAvailable, btsDeviceType,scServiceDetail.getId(), scServiceDetail.getUuid());
				
				String offeringName = StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName());

				/*if ("Fully Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation, Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Unmanaged".equalsIgnoreCase(cpeManagementType) || offeringName.toLowerCase().contains("unmanaged")) {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Physically Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Configuration Management".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", true);
				} else if ("Proactive Services".equalsIgnoreCase(cpeManagementType)) {							
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
				}else {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				}
				
				if(StringUtils.trimToEmpty(scOrder.getOrderType()).equalsIgnoreCase("MACD")
						&& (StringUtils.trimToEmpty(scOrder.getOrderCategory()).equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| StringUtils.trimToEmpty(scOrder.getOrderCategory()).equalsIgnoreCase("SHIFT_SITE")
								|| StringUtils.trimToEmpty(scOrder.getOrderCategory()).equalsIgnoreCase("ADD_IP"))) {
					if("Yes".equalsIgnoreCase(cpeChassisChanged) && StringUtils.isNotBlank(cpeVariant) && !cpeVariant.equalsIgnoreCase("None") && !cpeVariant.equalsIgnoreCase("null") && !cpeVariant.equalsIgnoreCase("NA")) {
						LOGGER.info("MACD cpeModelchange cpeVariant={} cpeChassisChanged={} serviceCode={} orderCode={} cpeManagementType={}",cpeVariant,cpeChassisChanged,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid(),cpeManagementType);
						if(StringUtils.isNotBlank(oldEquipmentModel) && cpeVariant.toLowerCase().contains(oldEquipmentModel.toLowerCase())){
							LOGGER.info("Old and Current Cpe Model matched");
							processVar.put("isCPEConfiguredByCustomer", true);
							processVar.put("isCPEArrangedByCustomer", true);
							processVar.put("cpeSiScope", "");
							Map<String, String> attrMap= new HashMap<>();
							attrMap.put("cpe_chassis_changed", "No");
							componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						}else{
							LOGGER.info("Old and Current Cpe Model not matched");
						}
					}else {
						if (!"Unmanaged".equalsIgnoreCase(cpeManagementType) || !offeringName.toLowerCase().contains("unmanaged")) {
							LOGGER.info("STOPPINGCPEFLOW=>MACD cpeManagementType={} cpeChassisChanged={} cpeVariant={} serviceCode={} orderCode={}",cpeManagementType,cpeChassisChanged,cpeVariant,scServiceDetail.getUuid(),scServiceDetail.getScOrderUuid());
							processVar.put("isCPEConfiguredByCustomer", true);
							processVar.put("isCPEArrangedByCustomer", true);
							processVar.put("cpeSiScope", "");
						}
						
					}
				}*/

				processVar.put("documentValidationRequired", true);
				processVar.put("isLMRequired", isLMRequired);
				processVar.put("e2eServiceTestingCompleted", false);
				processVar.put("siteReadinessStatus", true);
				processVar.put("siteReadinessConfirmation", false);
				processVar.put("rowRequired", rowRequired);
				processVar.put("prowRequired", prowRequired);
				processVar.put("hasOSPCapexDeviation", false);
				processVar.put("hasIBDCapexDeviation", false);
				processVar.put("ibdContractRequired", true);
				processVar.put("isMuxRequired", false);
				//processVar.put("isCPERequired", true);
				processVar.put("createServiceCompleted", false);
				processVar.put("isCLRSyncCallSuccess", false);
				processVar.put("serviceDesignCompleted", false);
				processVar.put("txConfigurationCompleted", false);
				processVar.put("serviceConfigurationCompleted", false);
				processVar.put("isStandardCPEDiscount", true);
				//processVar.put("ipTerminateConfigurationSuccess", false); Have to enable and it is not in scope of MVP
				processVar.put("isRFRequired", isRFRequired);
				processVar.put("isColoRequired", isColoRequired);
				processVar.put("failoverTestingRequired", false);
				processVar.put("additionalTechDetailsTaskCompleted", false);
				processVar.put("siteReadinessTaskCompleted", false);
				processVar.put("isValidDocuments", false);
				processVar.put("isSoftLoopPossibleAtCE", false);
				processVar.put("lmTestOnnetWirelessSuccess", true);
				processVar.put("isPEInternalCablingRequired", false);
				processVar.put("isCEInternalCablingRequired", true);
				processVar.put("cableSwappingPERequired", "No");
				processVar.put("cableSwappingCERequired", "No");
				processVar.put("internalCablingResponsibility", "TCL");
				processVar.put("internalCablingInterface", "");
				processVar.put("serviceDeliveryPlanReady", false);
				processVar.put("order_enrichment_complete", false);
				processVar.put("getIpServiceSyncCallCompleted", false);
				processVar.put("validateBtsStatus", false);
				//Have to handle isAmendedOrder for confirmOrderRequired and it is not in scope for MVP
				processVar.put("confirmOrderRequired", true);
				processVar.put("serviceConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("serviceConfigurationMessageSent", false);
				processVar.put("serviceConfigurationAckSuccess", false);
				processVar.put("serviceConfigurationSuccess", false);
				processVar.put("txConfigurationMessageSent", false);
				processVar.put("txConfigurationAckSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txMPLSConfigurationSuccess", false);
				processVar.put("rfConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("rfConfigurationMessageSent", false);
				processVar.put("rfConfigurationAckSuccess", false);
				processVar.put("rfConfigurationSuccess", false);

				processVar.put("previewIpConfigMessageSent", false);
				processVar.put("previewIpConfigAckSuccess", false);
				processVar.put("previewIpConfigSuccess", false);
				processVar.put("cancelIpConfigMessageSent", false);
				processVar.put("cancelIpConfigAckSuccess", false);
				processVar.put("cancelIpConfigSuccess", false);
				processVar.put("txManualConfigRequired", false);
				
				/*processVar.put("cpeLicensePRCompleted", true);
				processVar.put("cpeHardwarePRCompleted", true);
				processVar.put("isCpeAvailableInInventory", false);	
				processVar.put("cpeLicenseNeeded", true);	*/
				processVar.put("remainderCycle", reminderCycle);
				processVar.put("deemedAcceptanceDuration", "PT48H");
				//processVar.put("cpeConfigurationCompleted", true);
				processVar.put("offnetPOEnabled", true);
				
				processVar.put("siteAType", "A");
				processVar.put("site_type", "A");
				
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));

				if (isLMRequired) {

					if (lastMileType.toLowerCase().contains("rf")) {
						
						String antenaSize = StringUtils.trimToEmpty(feasibilityAttributes.get("Mast_3KM_avg_mast_ht"));
						String mastType = StringUtils.trimToEmpty(feasibilityAttributes.get("mast_type"));
						String structureType = mastType;
						try {
							if (StringUtils.isNotBlank(antenaSize)) {
								Double antenaSizeDouble = Double.parseDouble(antenaSize);
								if (antenaSizeDouble > 6)
									structureType = "Mast";
							}
						} catch (Exception ee) {
							LOGGER.info("Mast_3KM_avg_mast_ht issue {}", ee);

						}
						if (StringUtils.isBlank(structureType)) structureType = "Pole";
						
						atMap.put("structureType", structureType);
						processVar.put("structureType", structureType);

						if (btsDeviceType.contains("radwin") || solutionType.contains("radwin") || "Radwin from TCL POP".equalsIgnoreCase(providerName)) {
							atMap.put("rfTechnology", "RADWIN");
							atMap.put("rfMake", "Radwin");
						} else if (btsDeviceType.contains("cambium") || solutionType.contains("cambium")) {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						} else {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						}
						atMap.put("bhProviderName", bhConnectivity);
					} else {
						LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);
						if (connectedCustomer.contains("1") && serviceDetailsAttributes.containsKey("mux_details")) {

							try {

								String paramValue = serviceDetailsAttributes.get("mux_details");

								Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
										.findById(Integer.valueOf(paramValue));
								if (scAdditionalServiceParam.isPresent()) {
									String value = scAdditionalServiceParam.get().getValue();

									MuxDetailsItem[] muxDetailBeans = Utils.convertJsonToObject(value,
											MuxDetailsItem[].class);
									MuxDetailsItem muxDetailBean = muxDetailBeans[0];

									atMap.put("endMuxNodeName", muxDetailBean.getMux());
									atMap.put("endMuxNodeIp", muxDetailBean.getMuxIp());

								}
							} catch (Exception e) {
								LOGGER.error("error in parsing mux details : {} ", scServiceDetail.getUuid());

							}
						}

					}

					processBomDetails(scServiceDetail, atMap,scComponentAttributesAMap);

					if (scServiceDetail.getSiteEndInterface() != null) {
						String interfaceType = getInterfaceValue(scServiceDetail.getSiteEndInterface());
						atMap.put("interfaceType", interfaceType);
						processVar.put("internalCablingInterface", interfaceType);
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}
				}
                try{
                	if(Objects.nonNull(lmType) && lmType.equalsIgnoreCase("offnet")) {
						String supplierCategory = getOffnetSupplierCategory(lmConnectionType, feasibilityAttributes);
						if (Objects.nonNull(supplierCategory)) {
							atMap.put("offnetSupplierCategory", supplierCategory);
							processVar.put("offnetSupplierCategory", supplierCategory);
						}
					}
				}catch (Exception ex){
					LOGGER.error("error in getting supplier category : {} ", scServiceDetail.getUuid());
					LOGGER.error("error in getting supplier category : {} ", ex);
				}
				/*atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);*/				
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				atMap.put("providerName", providerName);
				
				processVar.put("providerName", providerName);
				
				LOGGER.info("lastMileType : {} isValidLM: {}", lastMileType,isValidLM);
				atMap.put("lmType", lastMileType);
				
				atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				
				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));


				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if(mstStateToDistributionCenterMapping!=null && mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()!=null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant", mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
				}
				
				if("MACD".equalsIgnoreCase(StringUtils.trimToEmpty(scServiceDetail.getOrderType())) && "N".equalsIgnoreCase(scServiceDetail.getIsMigratedOrder())){
					ScServiceDetail parentScServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if(parentScServiceDetail!=null) {
						ScComponentAttribute parentUuidCommissioningDate = scComponentAttributesRepository
								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
										parentScServiceDetail.getId(), "commissioningDate",
										AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
						if(parentUuidCommissioningDate!=null && parentUuidCommissioningDate.getAttributeValue()!=null) {
							LOGGER.info("Parent Service Commissioning date is {}",parentUuidCommissioningDate.getAttributeValue());
							atMap.put("parentUuidCommissioningDate", parentUuidCommissioningDate.getAttributeValue());
						}
					}
				}

				ScOrderAttribute poDate = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);
				
				if(poDate!=null && poNumber!=null && poDate.getAttributeValue()!=null && poNumber.getAttributeValue()!=null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				
				atMap.put("txRequired", processVar.get("txRequired").toString());
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM,"A");

			}
			processVar.put("processType", "computeProjectPLan");
			processVar.put("ip-end", "ip-end");
			processVar.put("ip-config", "ip-config");
			processVar.put("ip-activity-config", "ip-activity-config");
			processVar.put("tx-config", "tx-config");
			processVar.put("tx-end", "tx-end");
			String isCPEManagedAlready=StringUtils.trimToEmpty(scComponentAttributesAMap.get("isCPEManagedAlready"));
			LOGGER.info("isCPEManagedAlready: {}",isCPEManagedAlready);
			Boolean isLMChangeRequired=true;
			Boolean isCpeManagedAlready=false;
			if("MACD".equalsIgnoreCase(scServiceDetail.getOrderType()) && "CHANGE_ORDER".equalsIgnoreCase(scServiceDetail.getOrderCategory())
					&& scServiceDetail.getOrderSubCategory()==null && "true".equalsIgnoreCase(isCPEManagedAlready)){
				 LOGGER.info("LM Change"); 
				isLMChangeRequired=false;
			}
			Boolean isLMTestRequired=false;
			processVar.put("demoOrder", "N");
			processVar.put("isDemoOrderBillable", "N");
			processVar.put("demoDays", 0);			
			processVar.put("multiVrfBillingRequiredSkip", "No");
			if(isValidLM) {
				 LOGGER.info("isValidLM");
				 processVar.put("isP2PMACDFlow", false);
				 processVar.put("isP2PBSOFlow", false);
				 processVar.put("isOffnetMACDFlow", false);
				 processVar.put("isOffnetBSOFlow", false);
				 processVar.put("isMACDFlow", false);
				 processVar.put("isMACDBSOFlow", false);
				 processVar.put("isLMChangeRequired", true);
				 if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
							|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
									&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))) {
						LOGGER.info("Sdwan ConfirmOrder not required for Service id::{}",scServiceDetail.getId());
						processVar.put("confirmOrderRequired", false);
				 }
				 if(!isLMChangeRequired){
					 LOGGER.info("SDWAN LMChangeRequired::{}",scServiceDetail.getUuid());
					 isTxRequired=false;
					 isIpRequired=false;
					 //isCpeManagedAlready=true;
					 processVar.put("isLMChangeRequired", false);
					 processVar.put("O2CPROCESSKEY", "sdwan-underlay-cpe-managed-workflow");
					 runtimeService.startProcessInstanceByKey("sdwan-underlay-cpe-managed-workflow", processVar);
				 }else if(isP2PwithoutBH == true || isP2PwithBH == true) {
					 LOGGER.info("SDWAN P2P Flow");
					 isIpRequired=true;
					 if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
								&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("hot")
										&& !StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso"))
										|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))){
							LOGGER.info("SDWAN P2PwithoutBSO::{}",scServiceDetail.getUuid());
							processVar.put("isP2PMACDFlow", true);
							processVar.put("O2CPROCESSKEY", "sdwan-underlay-p2p-fulfilment-macd-workflow");
							runtimeService.startProcessInstanceByKey("sdwan-underlay-p2p-fulfilment-macd-workflow", processVar);
						}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
								&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("hot")
										&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso"))
										|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("SHIFT_SITE")
												&& (StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("lm") 
												|| StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso")
												|| StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Shifting"))))){
							LOGGER.info("SDWAN P2P BSO Workflow::{}",scServiceDetail.getUuid());
							processVar.put("isP2PBSOFlow", true);
							processVar.put("p2pConfigRequired", false);
							processVar.put("O2CPROCESSKEY", "sdwan-underlay-p2p-fulfilment-bso-workflow");
							runtimeService.startProcessInstanceByKey("sdwan-underlay-p2p-fulfilment-bso-workflow", processVar);
						}else {
							LOGGER.info("SDWAN New Order Workflow::{}",scServiceDetail.getUuid());
							processVar.put("O2CPROCESSKEY", "sdwan-p2p-fulfilment-workflow");
							isLMTestRequired=true;
							LOGGER.info("flowable trigerred for  Service id::{} is sdwan-p2p-fulfilment-workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("sdwan-p2p-fulfilment-workflow", processVar);
						}
				 }else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
							|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
									&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))
							|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(orderSubCategory)
									&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("parallel"))){
					 	LOGGER.info("SDWAN New Order Workflow::{}",scServiceDetail.getUuid());
					 	isLMTestRequired=true;
						if(lmType.equalsIgnoreCase("offnet")){
							LOGGER.info("SDWAN NEW  offnet::{}",scServiceDetail.getUuid());
							processVar.put("O2CPROCESSKEY", "sdwan-offnet-fulfilment-handover-new-workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is sdwan-offnet-fulfilment-handover-new-workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("sdwan-offnet-fulfilment-handover-new-workflow", processVar);			
						}else {
							LOGGER.info("SDWAN NEW or ADD SITE or parallel::{}",scServiceDetail.getUuid());
							processVar.put("O2CPROCESSKEY", "sdwan-ill-service-fulfilment-handover-workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is sdwan-ill-service-fulfilment-handover-workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("sdwan-ill-service-fulfilment-handover-workflow", processVar);			
						}			
					}else if((lmType.equalsIgnoreCase("offnet")) && StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")){
						LOGGER.info("SDWAN Offnet MACD::{}",scServiceDetail.getUuid());
						isIpRequired=true;
						if(skipOffnet) {
							LOGGER.info("SDWAN Offnet Hot Upgrade::{}",scServiceDetail.getUuid());
							processVar.put("isOffnetMACDFlow", true);
							processVar.put("O2CPROCESSKEY", "sdwan-underlay-offnet-hot-upgrade-workflow");
							runtimeService.startProcessInstanceByKey("sdwan-underlay-offnet-hot-upgrade-workflow", processVar);
						}else {
							LOGGER.info("SDWAN Offnet BSO or LM or Downgrade::{}",scServiceDetail.getUuid());
							processVar.put("isOffnetBSOFlow", true);
							processVar.put("O2CPROCESSKEY", "sdwan-underlay-offnet-fulfilment-handover-workflow");
							runtimeService.startProcessInstanceByKey("sdwan-underlay-offnet-fulfilment-handover-workflow", processVar);
						}
				}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && Objects.nonNull(orderSubCategory)
						&& (StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("lm") 
								|| StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso")
								|| StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Shifting"))){
					isIpRequired=true;
					LOGGER.info("SDWAN MACD BSO::{}",scServiceDetail.getUuid());
					processVar.put(IS_PORT_CHANGED, true);
					processVar.put("isMuxIPAvailable", false);
					processVar.put("isMACDBSOFlow", true);
					processVar.put("O2CPROCESSKEY", "sdwan-underlay-ill-bso-service-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("sdwan-underlay-ill-bso-service-fulfilment-handover-workflow", processVar);
				}else if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
						&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))){
					isIpRequired=true;
					LOGGER.info("SDWAN MACD::{}",scServiceDetail.getUuid());
					processVar.put(IS_CONNECTED_SITE, true);
					processVar.put(IS_PORT_CHANGED, false);
					processVar.put("isMuxIPAvailable", false);
					processVar.put("isMACDFlow", true);
					processVar.put("O2CPROCESSKEY", "sdwan-underlay-ill-macd-cb-service-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("sdwan-underlay-ill-macd-cb-service-fulfilment-handover-workflow", processVar);
				}
				
				persistDownTimeDetails(scServiceDetail,scSolutionId,scOrder.getOpOrderCode(),scServiceDetail.getErfPrdCatalogProductName(),isCpeManagedAlready,isIpRequired,isTxRequired,false,false,isLMTestRequired);		
				updateO2CTriggerStatus("SUCCESS",scSolutionId,scServiceDetail.getId());
				LOGGER.info("processVar::{}",processVar);
			}
			//Enable it, after Sukhada Confirmation 
			/*if(isValidLM && welcomeMailTrigger) {
				sendNotificationToCustomer(scServiceDetail, scContractInfo, scComponentAttributesAMap);	
			}*/
			 autoMigration(scServiceDetail,scOrder,false); 
			 LOGGER.info("processUnderLay completed");
		} catch (Exception e) {
			updateO2CTriggerStatus("FAILURE",scSolutionId,scServiceDetail.getId());
			LOGGER.error("Exception for SDWAN ILL/GVPN O2C Trigger Id::{},with message::{}", scServiceDetail.getId(),e);
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional(readOnly=false)
	public Boolean processDataResourceList(ResourceReInitiatedBean resourceReInitiatedBean) throws TclCommonException {
		Boolean response = false;
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(resourceReInitiatedBean.getServiceId());
		if (scServiceDetail.isPresent() && Objects.nonNull(scServiceDetail.get())) {
			ScServiceDetail scDetail = scServiceDetail.get();
			if (scDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED)) {
				if (scDetail.getErfPrdCatalogProductName().toLowerCase().contains("ias")
						|| scDetail.getErfPrdCatalogProductName().toLowerCase().contains("ill")
						|| scDetail.getErfPrdCatalogProductName().toLowerCase().contains("gvpn")) {
					saveTaskRemarksAndServiceLogs(resourceReInitiatedBean, scDetail);
					scDetail.setRrfsDate(DateUtil.convertStringToTimeStampYYMMDD(resourceReInitiatedBean.getRrfsDate()));
					scDetail.setCommissionedDate(Timestamp.valueOf(LocalDateTime.now().plusDays(60)));
					updateServiceStatusAndCreatedNewStatus(scDetail, TaskStatusConstants.INPROGRESS,"RESOURCE-REINITIATED");
					scDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
					response = processL2ODataToFlowable(resourceReInitiatedBean.getServiceId(), scDetail,false);
				} else if (scDetail.getErfPrdCatalogProductName().toLowerCase().contains("npl")) {
					saveTaskRemarksAndServiceLogs(resourceReInitiatedBean, scDetail);
					scDetail.setRrfsDate(DateUtil.convertStringToTimeStampYYMMDD(resourceReInitiatedBean.getRrfsDate()));
					scDetail.setCommissionedDate(Timestamp.valueOf(LocalDateTime.now().plusDays(60)));
					updateServiceStatusAndCreatedNewStatus(scDetail, TaskStatusConstants.INPROGRESS,"RESOURCE-REINITIATED");
					scDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
					response = processNPLL2ODataToFlowable(resourceReInitiatedBean.getServiceId(), scDetail,false);
				}
				
			} else {
				throw new TclCommonException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANT_FETCH_RESOURCE_RELEASED_TASK,
						ResponseResource.R_CODE_ERROR);
			}
		} 
		return response;
	}


	private void saveTaskRemarksAndServiceLogs(ResourceReInitiatedBean resourceReInitiatedBean,
			ScServiceDetail scDetail) {
		if(resourceReInitiatedBean.getRrfsDate() != null) {
			saveTaskRemarksForResourceInitiated(scDetail, TaskStatusConstants.INPROGRESS, resourceReInitiatedBean.getRrfsDate());
			updateServiceLogs(scDetail.getRrfsDate().toString(), resourceReInitiatedBean.getRrfsDate(), scDetail.getId(),
					"RRFS");
		}
	}
	
	private void updateServiceLogs(String currentValue, String changedValue, Integer serviceId, String type) {

		ServiceLogs serviceLogs = new ServiceLogs();
		serviceLogs.setAttributeValue(currentValue);
		serviceLogs.setChangedAttributeValue(changedValue);
		serviceLogs.setServiceId(serviceId);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceLogs.setCreatedBy(userInfoUtils.getUserInformation().getUserId());
		}
		serviceLogs.setType(type);
		serviceLogs.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceLogs.setUpdatedTime(new Timestamp(new Date().getTime()));
		serviceLogsRepository.save(serviceLogs);
	}
	
	protected ProcessTaskLog createProcessTaskLog(Task task, String action, String description, String actionTo,
			BaseRequest baseRequest) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {
			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		processTaskLog.setActionTo(actionTo);
		processTaskLog.setServiceCode(task.getServiceCode());
		if (action.equals("CLOSED"))
			action = TaskLogConstants.CLOSED;
		processTaskLog.setAction(action);
		processTaskLog.setQuoteCode(task.getQuoteCode());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setDescrption(description);
		if (baseRequest != null) {
			processTaskLog.setCategory(baseRequest.getDelayReasonCategory());
			processTaskLog.setSubCategory(baseRequest.getDelayReasonSubCategory());
		}

		return processTaskLog;

	}
	
	private void saveTaskRemarksForResourceInitiated(ScServiceDetail scServiceDetail,String status, String rrfsDate) {
		String userName = userInfoUtils.getUserInformation().getUserId();
		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks("Resource re-initiated on :" + new Date() + " by user :" + userName + " and Rrfs Date :" + rrfsDate);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}
	
	private void saveTaskRemarksForTrfDateExtension(ScServiceDetail scServiceDetail, String trfExtensionDate) {
		String userName = userInfoUtils.getUserInformation().getUserId();
		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks("TRF Extension date updated on :" + new Date() + " by user :" + userName + " and TRF Extension Date :" + trfExtensionDate);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}
	
	protected ServiceStatusDetails updateServiceStatusAndCreatedNewStatus(ScServiceDetail scServiceDetail,String status,String reason) {
		
		ServiceStatusDetails serviceStatusDetails=	serviceStatusDetailsRepository.findFirstByScServiceDetail_idOrderByIdDesc(scServiceDetail.getId());
		
		if(serviceStatusDetails!=null) {
			serviceStatusDetails.setEndTime(new Timestamp(new Date().getTime()));
			serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));
			serviceStatusDetailsRepository.save(serviceStatusDetails);
		}
		createServiceStaus(scServiceDetail, status,reason);
		return serviceStatusDetails;
	}
    
    protected ServiceStatusDetails createServiceStaus(ScServiceDetail scServiceDetail, String mstStatus,String category) {

		ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
		serviceStatusDetails.setScServiceDetail(scServiceDetail);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceStatusDetails.setUserName(userInfoUtils.getUserInformation().getUserId());
		}
		serviceStatusDetails.setServiceChangeCategory(category);
		serviceStatusDetails.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setStartTime(new Timestamp(new Date().getTime()));;
		serviceStatusDetails.setStatus(mstStatus);
		serviceStatusDetails.setServiceChangeCategory(category);
		serviceStatusDetailsRepository.save(serviceStatusDetails);

		return serviceStatusDetails;

	}
	private void compareAndUpdateMacdOrderAttributes(String serviceCode,boolean termination,String isAmended) {
		try {
			ScServiceDetail prevActiveServiceDetail = null;
			ScServiceDetail currentServiceDetail=null;
			if(Objects.nonNull(isAmended) && CommonConstants.Y.equalsIgnoreCase(isAmended)){
				LOGGER.info("Inside amended service block :{}", serviceCode);
				prevActiveServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "AMENDED");
			}else{
				prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "ACTIVE");
			}
			if(termination) {
				 currentServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,TaskStatusConstants.TERMINATION_INPROGRESS);

			}
			else {
				 currentServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "INPROGRESS");

			}
			LOGGER.info("compareAndUpdateMacdOrderAttributes current record:{} and previous service:{}", currentServiceDetail,prevActiveServiceDetail);

			Map<String, String> actualAttr = new HashMap();
			if (Objects.nonNull(prevActiveServiceDetail) && Objects.nonNull(currentServiceDetail)) {
				LOGGER.info("prevActiveServiceDetail id {} and currentServiceDetail id {}",prevActiveServiceDetail.getId(),currentServiceDetail.getId());
				List<String> attributeNames = Arrays.asList("amcStartDate","cpeAmcStartDate" ,"amcEndDate","cpeAmcEndDate", "cpeInstallationPoNumber", "cpeInstallationPrVendorName","cpeInstallationPrNumber", "cpeInstallationPoVendorName", "cpeSupplyHardwarePoNumber",
						"cpeSupportPoNumber", "cpeSupportPrVendorName", "cpeSupportPoVendorName","cpeSupportPrNumber","cpeLicencePoNumber", "cpeSupplyHardwarePoNumber","cpeSupplyHardwarePrNumber","cpeSupplyHardwarePoVendorName","cpeSupplyHardwarePrVendorName","cpeSerialNumber");

				List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository.
						findByScServiceDetailIdAndAttributeNameInAndScComponent_componentNameAndScComponent_siteType
								(prevActiveServiceDetail.getId(), attributeNames, "LM", "A");
				Map<String, String> componentAttr=new HashMap();
				if (Objects.nonNull(scComponentAttributes) && !scComponentAttributes.isEmpty()) {
					scComponentAttributes.forEach(attr->{
						componentAttr.put(attr.getAttributeName(), attr.getAttributeValue());
					});
					
					LOGGER.info("Previous Active ServiceDetail attributes : {}", componentAttr);
					if (componentAttr != null && !componentAttr.isEmpty()) {
						actualAttr.putAll(componentAttr);
						if (componentAttr.containsKey("amcStartDate") && componentAttr.get("amcStartDate") != null) {
							actualAttr.put("cpeAmcStartDate", componentAttr.get("amcStartDate"));
						}
						if (componentAttr.containsKey("amcEndDate") && componentAttr.get("amcEndDate") != null) {
							actualAttr.put("cpeAmcEndDate", componentAttr.get("amcEndDate"));
						}
						if (componentAttr.containsKey("cpeSupplyHardwareVendorName") && componentAttr.get("cpeSupplyHardwareVendorName") != null) {
							actualAttr.put("cpeSupplyHardwarePoVendorName", componentAttr.get("cpeSupplyHardwareVendorName"));
						}
						componentAndAttributeService.updateAttributes(currentServiceDetail.getId(), actualAttr,
								AttributeConstants.COMPONENT_LM, "A");
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Error while compareAndUpdateMacdOrderAttributes :{}", ex);
		}
	}
	
	@Transactional(readOnly=false)
	public Boolean processReInitateService(Integer serviceId, boolean welcomeMailTrigger) throws TclCommonException {
		Boolean response = false;
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(serviceId);
		if (scServiceDetail.isPresent() && Objects.nonNull(scServiceDetail.get())) {
			ScServiceDetail scDetail = scServiceDetail.get();
			if (scDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED)) {
				if (scDetail.getErfPrdCatalogProductName().toLowerCase().contains("ias")
						|| scDetail.getErfPrdCatalogProductName().toLowerCase().contains("ill")
						|| scDetail.getErfPrdCatalogProductName().toLowerCase().contains("gvpn")) {
					scDetail.setCommissionedDate(Timestamp.valueOf(LocalDateTime.now().plusDays(60)));
					updateServiceStatusAndCreatedNewStatus(scDetail, TaskStatusConstants.INPROGRESS,"RESOURCE-REINITIATED");
					scDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
					response = processL2ODataToFlowable(serviceId, scDetail,welcomeMailTrigger);
				} else if (scDetail.getErfPrdCatalogProductName().toLowerCase().contains("npl")) {
					scDetail.setCommissionedDate(Timestamp.valueOf(LocalDateTime.now().plusDays(60)));
					updateServiceStatusAndCreatedNewStatus(scDetail, TaskStatusConstants.INPROGRESS,"RESOURCE-REINITIATED");
					scDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
					response = processNPLL2ODataToFlowable(serviceId, scDetail,welcomeMailTrigger);
				}
				
			} else {
				throw new TclCommonException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANT_FETCH_RESOURCE_RELEASED_TASK,
						ResponseResource.R_CODE_ERROR);
			}
		} 
		return response;
	}


	private void notifyCustomerForNPL(ScServiceDetail scServiceDetail, ScContractInfo scContractInfo,
			Map<String, String> scComponentAttributesAMap) {
		try {
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			if (scContractInfo != null) {
				ccAddresses.add(scContractInfo.getAccountManagerEmail());
			}

			/*List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("CIM");

			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}*/
			
			String cuid = scServiceDetail.getScOrder().getTpsSfdcCuid();
			if(cuid != null) {
				LOGGER.info("checkPM-Assignment-for-cuid:{} and service code:{}",cuid, scServiceDetail.getUuid());
				PmAssignment pmAssignment = pmAssignmentRepository.findFirstByCuidOrderByIdDesc(cuid);
				if (pmAssignment != null && pmAssignment.getProgramManagerEmail() != null
						&& !pmAssignment.getProgramManagerEmail().trim().isEmpty()) {
					scServiceDetail.setAssignedPM(pmAssignment.getProgramManagerEmail());
					scServiceDetailRepository.save(scServiceDetail);
					LOGGER.info("PM-Assignment-for-cuid:{} and service code:{} pm:{}",cuid, scServiceDetail.getUuid(),pmAssignment.getProgramManagerEmail());
				}
			}
			if(StringUtils.isBlank(scServiceDetail.getAssignedPM())) {
				LOGGER.info("checkNPLPM-second-Assignment-for-cuid:{} and service code:{}",cuid, scServiceDetail.getUuid());
				if(scServiceDetail.getScOrder().getErfCustLeName() != null && (scServiceDetail.getScOrder().getErfCustLeName().equalsIgnoreCase("Tata Teleservices (Maharashtra) Limited") || 
					scServiceDetail.getScOrder().getErfCustLeName().equalsIgnoreCase("Tata Teleservices Limited"))) {
					if (scComponentAttributesAMap.get("destinationState") != null && !scComponentAttributesAMap.get("destinationState").isEmpty()) {
						LOGGER.info("PM assignment state:{}",scComponentAttributesAMap.get("destinationState"));
						List<String> pmRegion1 = Arrays.asList("ANDHRA PRADESH","BIHAR","CHANDIGARH","KARNATAKA","KERALA","MADHYA PRADESH","Orissa","PUNJAB","RAJASTHAN","TAMIL NADU","TELANGANA","UTTARAKHAND","WEST BENGAL","ANDAMAN AND NICOBAR ISLANDS");
						List<String> pmRegion2 = Arrays.asList("DELHI", "HARYANA", "UTTAR PRADESH");
						List<String> pmRegion3 = Arrays.asList("GUJARAT", "MAHARASHTRA");
						if(pmRegion1.stream().anyMatch(scComponentAttributesAMap.get("destinationState")::equalsIgnoreCase)){
							scServiceDetail.setAssignedPM("Harshal.Sonawane@tatacommunications.com");
						} else if (pmRegion2.stream().anyMatch(scComponentAttributesAMap.get("destinationState")::equalsIgnoreCase)){
							scServiceDetail.setAssignedPM("Vaibhavraj.Lande@contractor.tatacommunications.com");
						}else if (pmRegion3.stream().anyMatch(scComponentAttributesAMap.get("destinationState")::equalsIgnoreCase)) {
							scServiceDetail.setAssignedPM("falguni.bhatt@tatacommunications.com");
						}
					}else if(scComponentAttributesAMap.get("destinationCity")!=null &&
							("Delhi".equalsIgnoreCase(scComponentAttributesAMap.get("destinationCity"))
									|| "Noida".equalsIgnoreCase(scComponentAttributesAMap.get("destinationCity"))
									|| "Gurgaon".equalsIgnoreCase(scComponentAttributesAMap.get("destinationCity"))
									|| "Kolkata".equalsIgnoreCase(scComponentAttributesAMap.get("destinationCity")))){
						LOGGER.info("PM assignment city:{}",scComponentAttributesAMap.get("destinationCity"));
						scServiceDetail.setAssignedPM("Vaibhavraj.Lande@contractor.tatacommunications.com");
					}
					
					LOGGER.info("PM-Assignment-for-cuid:{} and service code:{} pm:{}",cuid, scServiceDetail.getUuid(),scServiceDetail.getAssignedPM());
					scServiceDetailRepository.save(scServiceDetail);
				}
			}

			notificationService.notifyCustomerWelcome(scServiceDetail, ccAddresses,scComponentAttributesAMap);
		} catch (Exception e) {
			LOGGER.error("Could not send welcome letter : {}", e);
		}
	}
	
	private void sendNotificationToCustomer(ScServiceDetail scServiceDetail, ScContractInfo scContractInfo,
			Map<String, String> scComponentAttributesAMap) {
		try {
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			if (scContractInfo != null) {
				ccAddresses.add(scContractInfo.getAccountManagerEmail());
			}

			/*List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("CIM");

			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}*/
			
			String cuid = scServiceDetail.getScOrder().getTpsSfdcCuid();
			if(cuid != null) {
				LOGGER.info("checkPM-Assignment-for-cuid:{} and service code:{}",cuid, scServiceDetail.getUuid());
				PmAssignment pmAssignment = pmAssignmentRepository.findFirstByCuidOrderByIdDesc(cuid);
				if (pmAssignment != null && pmAssignment.getProgramManagerEmail() != null
						&& !pmAssignment.getProgramManagerEmail().trim().isEmpty()) {
					scServiceDetail.setAssignedPM(pmAssignment.getProgramManagerEmail());
					scServiceDetailRepository.save(scServiceDetail);
					LOGGER.info("PM-Assignment-for-cuid:{} and service code:{} pm:{}",cuid, scServiceDetail.getUuid(),pmAssignment.getProgramManagerEmail());
				}
			}else {
				LOGGER.error("Could-not-send-welcome-letter-cuid-is-null-servicecode:{}",scServiceDetail.getUuid() );
			}

			notificationService.notifyCustomerWelcome(scServiceDetail, ccAddresses,scComponentAttributesAMap);
			//LOGGER.info("Customer Welcome Letter successfully Sent");
		} catch (Exception e) {
			LOGGER.error("Could-not-send-welcome-letter : {}", e);
		}
	}

	/**
	 * To process teams DR L2O Data to flowable
	 *
	 * @param scServiceDetail
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean processTeamsDRL2ODataToFlowable(ScServiceDetail scServiceDetail) {
		LOGGER.info("processTeamsDRL2ODataToFlowable method invoked");
		Boolean status = false;
		try {
			if (Objects.nonNull(scServiceDetail)) {
				Map<String, Object> solutionProcessVar = new HashMap<>();
				solutionProcessVar.put(SERVICE_ID, scServiceDetail.getId());
				solutionProcessVar.put(SC_ORDER_ID, scServiceDetail.getScOrder().getId());
				solutionProcessVar
						.put(ORDER_CODE, StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
				solutionProcessVar.put(ORDER_TYPE, StringUtils.trimToEmpty(scServiceDetail.getOrderType()));
				solutionProcessVar.put(SERVICE_CODE, scServiceDetail.getUuid());
				solutionProcessVar.put(ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());
				solutionProcessVar
						.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
				solutionProcessVar
						.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE,
								scServiceDetail.getScOrder());
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER,
								scServiceDetail.getScOrder());

				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null && poNumber
						.getAttributeValue() != null) {
					solutionProcessVar
							.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					solutionProcessVar.put(LeAttributesConstants.PO_NUMBER,
							StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				solutionProcessVar.put("root_endDate", new Timestamp(new Date().getTime()));
				solutionProcessVar.put("processType", "computeProjectPLan");
				solutionProcessVar.put("remainderCycle", reminderCycle);
				solutionProcessVar.put("O2CPROCESSKEY", "teamsdr-lld-workflow");
				LOGGER.info("Solution Flow Trigger::{}", solutionProcessVar);
				runtimeService.startProcessInstanceByKey("teamsdr-lld-workflow", solutionProcessVar);
			}
		} catch (Exception e) {
			LOGGER.error("Error for processTeamsDRL2ODataToFlowable : {}", e);
			status = false;
		}
		return status;

	}

	/**
	 * To process media gateway (india) flowable
	 *
	 * @param serviceId
	 * @param scServiceDetail
	 * @return
	 */
	@Transactional
	public Boolean processMediaGatewayL2ODataToFlowable(String orderCode) {
		Boolean status = true;
		try {
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(scOrder.getId());

			List<ScServiceDetail> mediaGatewayDetails = new ArrayList<>();
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(
						TeamsDROdrConstants.MICROSOFT_CLOUD_SOLUTIONS) && TeamsDROdrConstants.MEDIA_GATEWAY
						.equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())) {
					mediaGatewayDetails.add(scServiceDetail);
				}
			}

			for (ScServiceDetail mediaGateway : mediaGatewayDetails) {
				processMediaGateway(mediaGateway.getId(), mediaGateway);
			}
			
		} catch (Exception e) {
			LOGGER.error("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}

	@Transactional
	public Boolean processGSCL2ODataToFlowable(Integer serviceId, ScServiceDetail scServiceDetail, String planItemDefinitionId,String planItem ) {
		Boolean status = true;
		try {
			
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);
				scServiceDetail = opScServiceDetail.get();
			}
			
			LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",serviceId, scServiceDetail.getUuid());

			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ",scComponentAttributesAMap);
			
			LOGGER.info("GSC work flow to be triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			ServiceFulfillmentRequest serviceFulfillmentRequest = serviceCatalogueService
					.processServiceFulFillmentData(scServiceDetail.getId());

			Map<String, Object> processVar = new HashMap<>();
			Map<String, String> atMap = new HashMap<>();
			if (serviceFulfillmentRequest.getOrderInfo() != null && serviceFulfillmentRequest.getPrimaryServiceInfo() != null) {
				
				if (serviceFulfillmentRequest.getCustomerInfo() != null) {
					processVar.put("customerUserName", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCustomerName()));
					//processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCusomerContactEmail()));
					processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getOrderInfo().getOrderCreatedBy()));
				}
				
				processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getProductName()));
				processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getOfferingName()));
				
				/*
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));*/
					
				OrderInfoBean orderInfoBean = serviceFulfillmentRequest.getOrderInfo();
				processVar.put(SC_ORDER_ID, orderInfoBean.getScOrderId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(orderInfoBean.getOptimusOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderInfoBean.getOrderType()));
				processVar.put(CONTRACT_START_DATE, orderInfoBean.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", orderInfoBean.getOrderCreatedDate());
				processVar.put(ORDER_CREATED_DATE, orderInfoBean.getOrderCreatedDate());
				
				ServiceInfoBean primaryServiceInfo = serviceFulfillmentRequest.getPrimaryServiceInfo();
				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(primaryServiceInfo.getServiceCode()));
				processVar.put(SERVICE_ID, primaryServiceInfo.getServiceId());
				
				if (GscConstants.PROCURE_UIFN_NUMBERS.equals(planItemDefinitionId) && scServiceDetail.getParentId() != null) {

					// set parent service id and uuid for ProcureUIFNNumbers workflow
					Optional<ScServiceDetail> parentScServiceDetails = scServiceDetailRepository
							.findById(scServiceDetail.getParentId());
					ScServiceDetail parentScServiceDetail = parentScServiceDetails.get();
					processVar.put(SERVICE_ID, parentScServiceDetail.getId());
					processVar.put(SERVICE_CODE, parentScServiceDetail.getUuid());
				} else if (GscConstants.PROCURE_PROV_VAS_NUMBERS.equals(planItemDefinitionId)
						&& scServiceDetail.getParentId() != null) {

					List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByProductNameAndParentId(
							GscConstants.PRODUCT_SIP, String.valueOf(scServiceDetail.getParentId()));
					if (Objects.nonNull(serviceDetails) && !serviceDetails.isEmpty()) {
						ScServiceDetail sipScServiceDetail = serviceDetails.get(0);

						Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(
								sipScServiceDetail.getId(), AttributeConstants.COMPONENT_GSCLICENSE, "A",
								Arrays.asList(AttributeConstants.IS_WORKING_TEMP_TERMINATION_NO));
						String isWorkingTemporaryTerminationNumber = scComponentAttributesmap
								.getOrDefault(AttributeConstants.IS_WORKING_TEMP_TERMINATION_NO, "");
						processVar.put(TEMP_OUTPULSE_PRESENT, isWorkingTemporaryTerminationNumber);
					}
				}else if (GscConstants.PROCURE_DID_NUMBERS.equals(planItemDefinitionId)
						&& scServiceDetail.getParentId() != null) {

					List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByProductNameAndParentId(
							GscConstants.PRODUCT_SIP, String.valueOf(scServiceDetail.getParentId()));
					if (Objects.nonNull(serviceDetails) && !serviceDetails.isEmpty()) {
						ScServiceDetail sipScServiceDetail = serviceDetails.get(0);						
						processVar.put(SIP_SERVICE_ID, sipScServiceDetail.getId());
					}
				}
				processVar.put(GscConstants.ORIGIN_COUNTRY_CODE, scServiceDetail.getSourceCountryCodeRepc());
				processVar.put(GscConstants.CUSTOMER_ORG_ID, scServiceDetail.getCustOrgNo());
				processVar.put(GscConstants.SUPPLIER_ORG_ID, scServiceDetail.getSupplOrgNo());
				processVar.put(GscConstants.ACCESS_TYPE, scServiceDetail.getAccessType());
				processVar.put(GscConstants.SERVICE_TYPE, primaryServiceInfo.getServiceDetailsAttributes().get("serviceType"));
				processVar.put(GscConstants.SERVICE_TYPE_REPC, primaryServiceInfo.getServiceDetailsAttributes().get("serviceTypeRepc"));
				
				if (orderInfoBean.getOptyClassification() != null
						&& (orderInfoBean.getOptyClassification().equalsIgnoreCase("Sell With")
								|| orderInfoBean.getOptyClassification().equalsIgnoreCase("Sell Through"))) {
					processVar.put(GscConstants.IS_PARTNER_ORDER, "yes");	
				} else {
					processVar.put(GscConstants.IS_PARTNER_ORDER, "no");
				}
	
				processVar.put("remainderCycle", reminderCycle);
				
				processVar.put("processType", "computeProjectPLan");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				
				//atMap.put("supplierAddress", "Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				
				componentAndAttributeService.updateAttributes(serviceFulfillmentRequest.getPrimaryServiceInfo().getServiceId(), atMap, AttributeConstants.COMPONENT_LM, "A");

				if (scServiceDetail.getParentId() == null) {
					processVar.put(MasterDefConstants.PARENT_SERVICE_ID, primaryServiceInfo.getServiceId());

					if (scServiceDetail.getAccessType() != null
							&& (scServiceDetail.getAccessType().equals(GscConstants.PUBLIC_IP) || scServiceDetail.getAccessType().equals(GscConstants.MPLS))){
						
						processVar.put(GscConstants.CONTAINS_DID_SERVICE, "no");	
						
						List<String> productNames = scServiceDetailRepository
								.findProductNamesByParentId(primaryServiceInfo.getServiceId().toString());
						List<String> vasProducts = Arrays.asList(UIFN, LNS, ITFS, ACDTFS, ACANS);
						
						for (String productName : productNames) {
							if (vasProducts.stream().anyMatch(productName::contains)) {
								processVar.put(GscConstants.CONTAINS_VAS_SERVICE, "yes");
							} else if (productName.contains(GscConstants.DOMESTIC_VOICE)) {
								processVar.put(GscConstants.CONTAINS_DID_SERVICE, "yes");
							}
						}	
						
						ScOrderAttribute scOrder = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(
								orderInfoBean.getScOrderId(), GscConstants.SUPPLIER_NOTICE_ADDRESS);
						String supplierNoticeAddr = scOrder.getAttributeValue();
						if (!supplierNoticeAddr.isEmpty() && supplierNoticeAddr != null) {
							supplierNoticeAddr = supplierNoticeAddr.toLowerCase();
							if (supplierNoticeAddr.contains("india") || supplierNoticeAddr.contains("ind")) {
								processVar.put(GscConstants.IS_SUPPLIER_INDIA_LE, "yes");
							}
						}

						LOGGER.info("GSC Public IP processVar:: {}", processVar);
						if(Objects.nonNull(scServiceDetail.getOrderCategory()) && "REMOVE_NUMBER".equalsIgnoreCase(scServiceDetail.getOrderCategory())) {
							LOGGER.info("flowable trigerred for  Service id::{} is gsc_remove_numbers_workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("gsc_remove_numbers_workflow", processVar);
						} else if("MACD".equalsIgnoreCase(scServiceDetail.getOrderType())) {
							processVar.put("O2CPROCESSKEY", "gsc_pubip_macd_workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is gsc_pubip_macd_workflow",scServiceDetail.getUuid());
							cmmnHelperService.createCaseInstance("gsc_pubip_macd_workflow", processVar);
						} else {
							processVar.put("O2CPROCESSKEY", "gsc_pubip_workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is gsc_pubip_workflow",scServiceDetail.getUuid());
							cmmnHelperService.createCaseInstance("gsc_pubip_workflow", processVar);
						}
					} else {
						LOGGER.info("GSC PSTN processVar:: {}", processVar);
						if(Objects.nonNull(scServiceDetail.getOrderCategory()) && "REMOVE_NUMBER".equalsIgnoreCase(scServiceDetail.getOrderCategory())) {
							LOGGER.info("flowable trigerred for  Service id::{} is gsc_remove_numbers_workflow",scServiceDetail.getUuid());
							runtimeService.startProcessInstanceByKey("gsc_remove_numbers_workflow", processVar);
						} else if("MACD".equalsIgnoreCase(scServiceDetail.getOrderType())) {
							processVar.put("O2CPROCESSKEY", "gsc_pstn_macd_workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is gsc_pstn_macd_workflow",scServiceDetail.getUuid());
							cmmnHelperService.createCaseInstance("gsc_pstn_macd_workflow", processVar);
						} else {
							processVar.put("O2CPROCESSKEY", "gsc_pstn_workflow");
							LOGGER.info("flowable trigerred for  Service id::{} is gsc_pstn_workflow",scServiceDetail.getUuid());
							cmmnHelperService.createCaseInstance("gsc_pstn_workflow", processVar);
						}
					}

				} else {
					cmmnHelperService.addPlanItemLocalVariables(planItem, processVar, 1);
					cmmnHelperService.startPlanItem(planItem, null);
				}
				LOGGER.info("GSC work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			} else {
				LOGGER.info("invalid data in processGSCL2ODataToFlowable");
			}
			LOGGER.info("processGSCL2ODataToFlowable completed");
		} catch (Exception e) {
			LOGGER.error("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}

	/**
	 * Process media gateway india
	 *
	 * @param serviceId
	 * @param scServiceDetail
	 */
	@Transactional
	public void processMediaGateway(Integer serviceId, ScServiceDetail scServiceDetail) throws TclCommonException {

		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}

			LOGGER.info(
					"Input processMediaGatewayL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
					serviceId, scServiceDetail.getUuid());

			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);

			LOGGER.info("TeamsDR work flow to be triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			ServiceFulfillmentRequest serviceFulfillmentRequest = serviceCatalogueService
					.processTeamsDRServiceFulFillmentData(scServiceDetail.getId());
			LOGGER.info("TeamsDR work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());

			LOGGER.info("Input processTeamsDRL2ODataToFlowable(processMediaGateway) received for L2O :: {}",
					serviceFulfillmentRequest);
			Map<String, Object> processVar = new HashMap<>();
			Map<String, String> atMap = new HashMap<>();
			if (serviceFulfillmentRequest.getOrderInfo() != null && serviceFulfillmentRequest
					.getPrimaryServiceInfo() != null) {

				if (serviceFulfillmentRequest.getCustomerInfo() != null) {
					processVar.put("customerUserName",
							StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCustomerName()));
					//processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCusomerContactEmail()));
					processVar.put("customerEmail",
							StringUtils.trimToEmpty(serviceFulfillmentRequest.getOrderInfo().getOrderCreatedBy()));
				}
				//checking if media gateway country is india or international
				if (scServiceDetail.getDestinationCountry().equalsIgnoreCase(CommonConstants.INDIA_SITES))
					processVar.put(IS_DOMESTIC, CommonConstants.TRUE);
				else
					processVar.put(IS_DOMESTIC, CommonConstants.FALSE);
				ScContractInfo scContractInfo = scContractInfoRepository
						.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
				
				if (CommonConstants.INDIA_SITES.equalsIgnoreCase(scContractInfo.getBillingCountry())) {
					   processVar.put(BillingConstants.IS_INTL_BILLING, Boolean.FALSE);
					   atMap.put(BillingConstants.IS_INTL_BILLING, CommonConstants.N);
				} else {
					   processVar.put(BillingConstants.IS_INTL_BILLING, Boolean.TRUE);
					   atMap.put(BillingConstants.IS_INTL_BILLING, CommonConstants.Y);
				}
				
				processVar.put(SP_LE_COUNTRY, scContractInfo.getBillingCountry());
				processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getProductName()));
				processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getOfferingName()));

				OrderInfoBean orderInfoBean = serviceFulfillmentRequest.getOrderInfo();
				processVar.put(SC_ORDER_ID, orderInfoBean.getScOrderId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(orderInfoBean.getOptimusOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderInfoBean.getOrderType()));
				processVar.put(CONTRACT_START_DATE, orderInfoBean.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", orderInfoBean.getOrderCreatedDate());
				processVar.put(ORDER_CREATED_DATE, orderInfoBean.getOrderCreatedDate());

				ServiceInfoBean primaryServiceInfo = serviceFulfillmentRequest.getPrimaryServiceInfo();
				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(primaryServiceInfo.getServiceCode()));
				processVar.put(SERVICE_ID, primaryServiceInfo.getServiceId());

				processVar.put("remainderCycle", reminderCycle);

				String cpeType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("Media Gateway Purchase Type"));
				String cpeModel = StringUtils.trimToEmpty(scComponentAttributesAMap.get("Select your Media Gateway"));

				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{}", serviceId,
						scServiceDetail.getUuid(), cpeType, cpeModel);
				if (cpeType.contains("Rental")) {
					cpeType = "Rental";
					processVar.put("cpeType", cpeType);
					atMap.put("cpeType", cpeType);
				} else if (cpeType.contains("Outright")) {
					cpeType = "Outright";
					processVar.put("cpeType", cpeType);
					atMap.put("cpeType", cpeType);
				} else {
					processVar.put("cpeType", cpeType);
					atMap.put("cpeType", cpeType);
				}

				processVar.put("processType", "computeProjectPLan");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));

				atMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");

				LOGGER.info("Inside TeamsDR media gateway india workflow trigger");
				String SiScope = "Supply, Installation, Support";
				atMap.put("cpeSiScope", SiScope);
				processVar.put("cpeSiScope", SiScope);

				String workflowKey = "teamsdr-endpoint-fulfilment-handover-workflow";

				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE,
								scServiceDetail.getScOrder());
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER,
								scServiceDetail.getScOrder());

				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null && poNumber
						.getAttributeValue() != null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				componentAndAttributeService
						.updateAttributes(serviceFulfillmentRequest.getPrimaryServiceInfo().getServiceId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");

				runtimeService.startProcessInstanceByKey(workflowKey, processVar);
			} else {
				LOGGER.info("invalid data in processMediaGateway");
			}
		} catch (Exception e) {
			LOGGER.info("Error in process media gateway flowable :{}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR);
		}
		LOGGER.info("processMediaGatewayL2ODataToFlowable completed");
	}
	
	private Integer getDemoDays(String orderTermInMonths) {
		LOGGER.info("getDemoDays method invoked::{}",orderTermInMonths);
		Map<String,Integer> contractTermWithDaysMapping= new HashMap<>();
		contractTermWithDaysMapping.put("0.5",15);
		contractTermWithDaysMapping.put("1.0",30);
		contractTermWithDaysMapping.put("1.5",45);
		contractTermWithDaysMapping.put("2.0",60);
		contractTermWithDaysMapping.put("2.5",75);
		contractTermWithDaysMapping.put("3.0",90);
		if(contractTermWithDaysMapping.containsKey(orderTermInMonths)) {
			LOGGER.info("OrderTermInMonths exists in contractTermWithDaysMapping::{}",contractTermWithDaysMapping.get(orderTermInMonths));
			return contractTermWithDaysMapping.get(orderTermInMonths);
		}else {
			LOGGER.info("OrderTermInMonths not exists in contractTermWithDaysMapping::{}",orderTermInMonths);
			return 0;
		}
	}

	/**
	 * @author vivek
	 *
	 * @param id
	 * @param scServiceDetail
	 * @param b
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly=false,isolation=Isolation.READ_UNCOMMITTED)
	public boolean processTerminationWorkflow(Integer serviceId, ScServiceDetail scServiceDetail, boolean emailTrigger) throws TclCommonException {
		String lmType = "";
		String lastMileType = "";

		if (scServiceDetail == null) {
			
			Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

			scServiceDetail = opScServiceDetail.get();
		}

		LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
				scServiceDetail.getId(), scServiceDetail.getUuid());
		String terminationEffectiveDate = getEffectiveDateForTermination(scServiceDetail.getTerminationEffectiveDate());

		ScOrder scOrder = scServiceDetail.getScOrder();
		String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
		String orderSubCategory = scServiceDetail.getOrderSubCategory();
		String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

		orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
		orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
		List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
		ScContractInfo scContractInfo = null;
		if(scContractInfos != null) {
			scContractInfo = scContractInfos.stream().findFirst().orElse(null);
		}

		List<String> notInCategories = new ArrayList<String>();
		notInCategories.add("FEASIBILITY");
		List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
				.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
						CommonConstants.Y, notInCategories);
		LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
		List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
				.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
						"FEASIBILITY");
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
		LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);
		
		Map<String, String> scComponentAttributesBMap =commonFulfillmentUtils.getComponentAttributes(
				scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "B");
		LOGGER.info("scComponentAttributesBMap {} ",scComponentAttributesBMap);

		Map<String, String> feasibilityAttributes = new HashMap<>();
		for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
			feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
					feasibilityAttribute.getAttributeValue());
		}
		
		List<String> componentLis = Arrays.asList("terminationSubType", "terminationSubReason", "oem",
				"cpeSerialNumber");
		Map<String, String> serviceDetailsAttributes = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();

		for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
			LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}", scServiceAttribute.getId(),
					scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());
			if (componentLis.contains(scServiceAttribute.getAttributeName())) {
				componentMap.put(scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());

			}

			serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());
		}
		
		if(!scComponentAttributesAMap.isEmpty()) {
			if(scComponentAttributesAMap.containsKey("customerMailReceiveDate")) {
				componentMap.put("customerRequestorDate", StringUtils.trimToEmpty(scComponentAttributesAMap.get("customerMailReceiveDate")));
				scServiceDetail.setCustomerRequestorDate(StringUtils.trimToEmpty(scComponentAttributesAMap.get("customerMailReceiveDate")));
			}
			if(scComponentAttributesAMap.containsKey("amc_required")) {
				componentMap.put("amcRequired", StringUtils.trimToEmpty(scComponentAttributesAMap.get("amc_required")));
			}
		}
		
		componentAndAttributeService.updateAttributes(serviceId, componentMap, "LM", "A");
		



		Map<String, Object> processVar = new HashMap<>();
		processVar.put("isBillable",true);
		if(serviceDetailsAttributes.get("billingType")!=null && serviceDetailsAttributes.get("billingType").toLowerCase().contains("non-billable")) {
			processVar.put("isBillable",false);
		}
		if (scOrder.getErfCustCustomerName() != null) {
			processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
		}

		
		if (scOrder != null) {
			processVar.put(SC_ORDER_ID, scOrder.getId());
			processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
			processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
			processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
			if(scContractInfo != null) {
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
			}
			LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
			processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
			processVar.put("parentServiceCode", null);
			processVar.put("parentLmType", null);

			if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
				processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
			} else {
				processVar.put("orderSubCategory", "NA");
			}
			processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
			String cpeType =null;
			String cpeModel = null;
			String offeringName = null;

			if (scServiceDetail != null) {

				// GVPN:UPDATE SITE TYPE BASED ON VPN TOPOLOGY
				if ("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())) {
					LOGGER.info("update vpn details");
					ScServiceAttribute siteTypeServiceAttribute = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
									"Site Type", "GVPN Common");
					if (Objects.nonNull(siteTypeServiceAttribute)) {
						ScServiceAttribute gvpnCommonServiceAttribute = scServiceAttributeRepository
								.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
										"VPN Topology", "GVPN Common");
						if (Objects.nonNull(gvpnCommonServiceAttribute)) {
							LOGGER.info("VPN common");
							updateVpnDetail(gvpnCommonServiceAttribute, siteTypeServiceAttribute);
						}
						ScServiceAttribute sitePropServiceAttribute = scServiceAttributeRepository
								.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
										"VPN Topology", "SITE_PROPERTIES");
						if (Objects.nonNull(sitePropServiceAttribute)) {
							LOGGER.info("VPN properties");
							updateVpnDetail(gvpnCommonServiceAttribute, siteTypeServiceAttribute);
						}
					}
				}

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));

				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				lastMileType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"));

				String btsDeviceType = StringUtils.trimToEmpty(feasibilityAttributes.get("bts_device_type"))
						.toLowerCase();
				
				if (btsDeviceType == null) {
					btsDeviceType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("rfMake")).toLowerCase();

				}
				if(btsDeviceType==null) {
					btsDeviceType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("bts_device_type"))
							.toLowerCase();
				}

				String solutionType = StringUtils.trimToEmpty(feasibilityAttributes.get("solution_type")).toLowerCase();
				String cpeChassisChanged = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_chassis_changed"));
				String cpeVariant = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_variant"));
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));
				if(cpeChassisChanged!=null  && (!cpeChassisChanged.equalsIgnoreCase("Y") || !cpeChassisChanged.equalsIgnoreCase("Yes"))) {
					compareAndUpdateMacdOrderAttributes(scServiceDetail.getUuid(), true, scServiceDetail.getIsAmended());
				}

				LOGGER.info(
						"cpeType for serviceid :{} and service code:{} and map:: {} cpeChassisChanged::{} cpeVariant::{} lastMileType::{}",
						serviceId, scServiceDetail.getUuid(), serviceDetailsAttributes, cpeChassisChanged, cpeVariant,
						lastMileType);

				 cpeType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE"));
				 cpeModel = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE Basic Chassis"));

				 String serviceType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Service type"));
					
				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{} serviceType={}", serviceId,scServiceDetail.getUuid(),cpeType,cpeModel,serviceType);
				
				if(StringUtils.isNotBlank(serviceType) && serviceType.toLowerCase().contains("usage")) {
					processVar.put("burstableOrder", true);
				}
					
				String cpeSiScope = "Customer provided";
				if (cpeType.contains("Rental") || cpeType.equalsIgnoreCase("TATACOMM(Rental)")) {
					cpeType = "Rental";
					processVar.put("cpeType", cpeType);
				} else if (cpeType.contains("Outright")) {
					cpeType = "Outright";
					processVar.put("cpeType", cpeType);
				} else {
					processVar.put("cpeType", cpeType);
				}

				String bhConnectivity = StringUtils.trimToEmpty(feasibilityAttributes.get("BHConnectivity"));
				String providerName = StringUtils.trimToEmpty(feasibilityAttributes.get("closest_provider_bso_name"));

				if (StringUtils.isBlank(providerName)) {
					providerName = StringUtils.trimToEmpty(serviceDetailsAttributes.get("closest_provider_bso_name"));
				}
				if(StringUtils.isBlank(providerName)) {
					providerName = StringUtils.trimToEmpty(scComponentAttributesAMap.get("closest_provider_bso_name"));

				}
				if (StringUtils.isBlank(providerName)) {
					providerName = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastmileProvider"));

				}
				

				if (lastMileType.toLowerCase().contains("onnet wireless") || lastMileType.equalsIgnoreCase("OnnetRF") || lastMileType.equalsIgnoreCase("onnet wireless"))
					lastMileType = "OnnetRF";
				else if (lastMileType.toLowerCase().contains("offnet wireless")|| lastMileType.equalsIgnoreCase("OffnetRF") || lastMileType.equalsIgnoreCase("offnet wireless"))
					lastMileType = "OffnetRF";
				else if (lastMileType.toLowerCase().contains("offnet wireline")|| lastMileType.equalsIgnoreCase("OffnetWL") || lastMileType.equalsIgnoreCase("offnet wireline"))
					lastMileType = "OffnetWL";
				else if (lastMileType.toLowerCase().contains("onnet wireline")|| lastMileType.equalsIgnoreCase("OnnetWL") || lastMileType.equalsIgnoreCase("ONNET WIRELINE"))
					lastMileType = "OnnetWL";
				else if (lastMileType.toLowerCase().contains("man")|| lastMileType.equalsIgnoreCase("OnnetWL") || lastMileType.toLowerCase().contains("wan aggregation"))
					lastMileType = "OnnetWL";

				lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileType.toLowerCase().contains("onnetrf")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Onnet Wireless")) {
					LOGGER.info("onnetrf");
					lmScenarioType = "Onnet Wireless";
					lmConnectionType = "Wireless";
					lmType = "onnet";

					if (providerName.toLowerCase().contains("p2p") && !providerName.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p/not pmp");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap = new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
					} else if (StringUtils.isBlank(providerName) && !StringUtils.isBlank(solutionType)
							&& solutionType.toLowerCase().contains("p2p")
							&& !solutionType.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p from solution type/not pmp/provider is blank");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap = new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
					}

					if (StringUtils.isBlank(bhConnectivity)) {
						if (providerName.equalsIgnoreCase("Radwin from TCL POP")) {
							bhConnectivity = "Radwin from TCL POP";
						}
					}

				} else if (lastMileType.toLowerCase().contains("offnetrf")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireless")) {
					LOGGER.info("offnetrf");
					lmScenarioType = "Offnet Wireless";
					lmConnectionType = "Wireless";
					lmType = "offnet";

				} else if (lastMileType.toLowerCase().contains("offnetwl")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireline")) {
					LOGGER.info("offnetwl");
					lmScenarioType = "Offnet Wireline";

					lmConnectionType = "Wireline";
					lmType = "offnet";
					String supplierOldlocalLoopBw = "0.0";

					LOGGER.info("service code::{} portBW::{} supplierOldlocalLoopBw::{},skipOffnet::{}",
							scServiceDetail.getUuid(), supplierOldlocalLoopBw);
				}

				else if (lastMileType.toLowerCase().contains("onnetwl")
						|| lastMileType.toLowerCase().equalsIgnoreCase("onnet wireline")) {
					LOGGER.info("onnetwl");
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileType = "OnnetWL";

				}
			


				
			
				if (scComponentAttributesAMap.get("destinationCity") != null
						&& !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
					processVar.put("tier", setTierValue(scComponentAttributesAMap.get("destinationCity")));
				}
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);
				processVar.put("bhConnectivity", bhConnectivity);

				Map<String, String> atMap = new HashMap<>();
				
				atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				processVar.put("providerName", providerName);

				atMap.put("lmType", lastMileType);
				
				atMap.put("terminationEffectiveDate", scServiceDetail.getTerminationEffectiveDate());

				LOGGER.info(
						"Input processL2ODataToFlowable received for lmScenarioType::{}  cpeManagementType::{} cpeType::{} isTaxAvailable::{} btsDeviceType::{} serviceId:: {} service code:: {}",
						lmScenarioType, cpeManagementType, cpeType, isTaxAvailable, btsDeviceType, serviceId,
						scServiceDetail.getUuid());

				 offeringName = StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName());

				
				
				if ("Fully Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation, Support";
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Unmanaged".equalsIgnoreCase(cpeManagementType)
						|| offeringName.toLowerCase().contains("unmanaged")) {
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Physically Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation";
				} else if ("Configuration Management".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Support";
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Proactive Services".equalsIgnoreCase(cpeManagementType)) {
					processVar.put("cpeSiScope", cpeSiScope);
				} else {
					processVar.put("cpeSiScope", cpeSiScope);
				}

				processVar.put("siteAType", "A");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				String structureType="";
				if (lastMileType.toLowerCase().contains("rf")) {
					
						String response = null;
							try {
								response = (String) mqUtils.sendAndReceive(terminationUpdateOnnetWirelessDetailsQueue,
										Utils.convertObjectToJson(scServiceDetail.getUuid()));
								LOGGER.info("PMP Details service code: {}  response: {}", scServiceDetail.getUuid(), response);
								if(response != null && !response.trim().isEmpty()) {
									SSDumpResponseBean ssDumpResponseBean = Utils.convertJsonToObject(response, SSDumpResponseBean.class);
									SSDumpBean ssDumpBean = ssDumpResponseBean.getResponse();
									if(ssDumpBean.getSsMac() != null) {
										atMap.put("suMacAddress", ssDumpBean.getSsMac());
									}
									if(ssDumpBean.getSerialNumber() != null) {
										atMap.put("suSerialNumber", ssDumpBean.getSerialNumber());
									}
									if(ssDumpBean.getSsIp() != null) {
										atMap.put("suIp", ssDumpBean.getSsIp());
									}
									if(ssDumpBean.getTowerPoleHeight() != null) {
										atMap.put("poleHeight", ssDumpBean.getTowerPoleHeight());
									}
									if(ssDumpBean.getAntennaHeight()!= null) {
										atMap.put("mastHeight", ssDumpBean.getAntennaHeight());
									}
									if(ssDumpBean.getVendor()!= null) {
										btsDeviceType = ssDumpBean.getVendor();
										atMap.put("rfMake", ssDumpBean.getVendor());
									}
									if(ssDumpBean.getSsMountType()!= null) {
										atMap.put("typeOfPole", ssDumpBean.getSsMountType());
										atMap.put("structureType", ssDumpBean.getSsMountType());

										structureType=ssDumpBean.getSsMountType();
									}
									if(ssDumpBean.getSsMac()!= null) {
										atMap.put("ssMac", ssDumpBean.getSsMac());
									}
								}
							} catch (TclCommonException e) {
								LOGGER.error("Error while update offnet wireless details servicecode {} error {}, ", scServiceDetail.getUuid(), e);
							}
							try {
								if(response == null || response.trim().isEmpty()) {
									response = (String) mqUtils.sendAndReceive(terminationGetP2PDetailsQueue,
											Utils.convertObjectToJson(scServiceDetail.getUuid()));
									LOGGER.info("P2P Details service code: {}  response: {}", scServiceDetail.getUuid(), response);
									if(response != null && !response.trim().isEmpty()) 
									{
										HashMap<String, Object> p2pDetailsMap = Utils.convertJsonToObject(response, HashMap.class);
										if(p2pDetailsMap.get("ssMac") != null) {
											atMap.put("btsSuMacAddress", p2pDetailsMap.get("ssMac").toString());
										}
										if(p2pDetailsMap.get("ssIp") != null) {
											atMap.put("btsIp", p2pDetailsMap.get("ssIp").toString());
										}
										if (p2pDetailsMap.get("latitude") != null) {
											atMap.put("bsLatitude", p2pDetailsMap.get("latitude").toString());
										}
										if (p2pDetailsMap.get("longitude") != null) {
											atMap.put("bsLongitude", p2pDetailsMap.get("longitude").toString());
										}
										if (p2pDetailsMap.get("ip") != null) {
											atMap.put("bsIp", p2pDetailsMap.get("ip").toString());
										}
										if (p2pDetailsMap.get("ssBsName") != null) {
											atMap.put("ssBsName", p2pDetailsMap.get("ssBsName").toString());
										}
										if (p2pDetailsMap.get("ssLatitude") != null) {
											atMap.put("ssLatitude", p2pDetailsMap.get("ssLatitude").toString());
										}
										if (p2pDetailsMap.get("ssLongitude") != null) {
											atMap.put("ssLongitude", p2pDetailsMap.get("ssLongitude").toString());
											
										}if (p2pDetailsMap.get("bsName") != null) {
											atMap.put("btsName", p2pDetailsMap.get("bsName").toString());
										}
										if(p2pDetailsMap.get("antennaMountType") != null) {
											atMap.put("structureType", p2pDetailsMap.get("antennaMountType").toString());

										}
									}
								}
							} catch (Exception e) {
								LOGGER.error("Error while Get P2P details servicecode {} error {}, ", scServiceDetail.getUuid(), e);
							}

			

					atMap.put("structureType", structureType);
					processVar.put("structureType", structureType);

					if (btsDeviceType.contains("radwin") || solutionType.contains("radwin")
							|| "Radwin from TCL POP".equalsIgnoreCase(providerName)) {
						atMap.put("rfTechnology", "RADWIN");
						atMap.put("rfMake", "Radwin");
					} else if (btsDeviceType.contains("cambium") || solutionType.contains("cambium")) {
						atMap.put("rfTechnology", "CAMBIUM");
						atMap.put("rfMake", "Cambium");
					} else {
						atMap.put("rfTechnology", "CAMBIUM");
						atMap.put("rfMake", "Cambium");
					}

					atMap.put("bhProviderName", bhConnectivity);
				} 


				if (scServiceDetail.getSiteEndInterface() != null) {
					String interfaceType = getInterfaceValue(scServiceDetail.getSiteEndInterface());
					atMap.put("interfaceType", interfaceType);
					processVar.put("internalCablingInterface", interfaceType);
				} else {
					if(scComponentAttributesAMap.get("interface") != null && !scComponentAttributesAMap.get("interface").isEmpty()) {
						String interfaceType = getInterfaceValue(scComponentAttributesAMap.get("interface"));
						atMap.put("interfaceType", interfaceType);
						processVar.put("internalCablingInterface", interfaceType);
					}
				}
				try {
					if (Objects.nonNull(lmType) && lmType.equalsIgnoreCase("offnet")) {
						String supplierCategory = getOffnetSupplierCategory(lmConnectionType, feasibilityAttributes);
						if (Objects.nonNull(supplierCategory)) {
							atMap.put("offnetSupplierCategory", supplierCategory);
							processVar.put("offnetSupplierCategory", supplierCategory);
						}
					}
				} catch (Exception ex) {
					LOGGER.error("error in getting supplier category : {} ", scServiceDetail.getUuid());
					LOGGER.error("error in getting supplier category : {} ", ex);
				}
				atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				atMap.put("providerName", providerName);

				processVar.put("providerName", providerName);

				atMap.put("lmType", lastMileType);

				atMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");

				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));

				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if (mstStateToDistributionCenterMapping != null
						&& mstStateToDistributionCenterMapping.getMasterTclDistributionCenter() != null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant",
							mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getSapStorageLocation()));
				}

				if ("MACD".equalsIgnoreCase(StringUtils.trimToEmpty(orderType))
						&& "N".equalsIgnoreCase(scServiceDetail.getIsMigratedOrder())) {
					ScServiceDetail parentScServiceDetail = scServiceDetailRepository
							.findFirstByUuidAndMstStatus_codeOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if (parentScServiceDetail != null) {
						ScComponentAttribute parentUuidCommissioningDate = scComponentAttributesRepository
								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
										parentScServiceDetail.getId(), "commissioningDate",
										AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
						if (parentUuidCommissioningDate != null
								&& parentUuidCommissioningDate.getAttributeValue() != null) {
							LOGGER.info("Parent Service Commissioning date is {}",
									parentUuidCommissioningDate.getAttributeValue());
							atMap.put("parentUuidCommissioningDate", parentUuidCommissioningDate.getAttributeValue());
						}
					}
				}

				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);

				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null
						&& poNumber.getAttributeValue() != null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");

			}
			
			if (lastMileType.equalsIgnoreCase("OnnetRF") || lastMileType.equalsIgnoreCase("onnet wireless") || lastMileType.toLowerCase().contains("wireless")
					|| lastMileType.toLowerCase().contains("rf")) {
				processVar.put("rfDeviceRecovery", "Yes");
				processVar.put("isRFExists", "Yes");

				processVar.put("mastRecovery", "Yes");

			}
			if (lastMileType.equalsIgnoreCase("OnnetWL") || lastMileType.equalsIgnoreCase("Onnet Wireline")) {
				processVar.put("muxRecoveryRequired", "Yes");
				processVar.put("muxRecoveryRequiredSiteA", "Yes");
			}
			if ((cpeType.contains("Rental") || cpeType.equalsIgnoreCase("TATACOMM(Rental)"))
					&& !offeringName.toLowerCase().contains("unmanaged")) {
				processVar.put("cpeRecoveryRequired", "Yes");

			}
			processVar.put("processType", "computeProjectPLan");

		}
		scServiceDetailRepository.save(scServiceDetail);
		processVar.put("ipBlockedResourceSuccess", false);
		processVar.put("terminationFlowTriggered", "Yes");
		processVar.put("remainderCycle", "R60/PT24H");
		processVar.put("getCLRSyncCallCompleted", false);
		processVar.put("getCLRSuccess", false);
		processVar.put("checkCLRSuccess", false);
		processVar.put("terminationEffectiveDate", terminationEffectiveDate);
		processVar.put("terminationEmailDate", getTerminationEmailDate(scServiceDetail.getTerminationEffectiveDate()));
		processVar.put("terminationFromL20", "Yes");
		
		// hardcode to no needed as required by PO 
		processVar.put("cpeRecoveryRequired","No");
		processVar.put("muxRecoveryRequiredSiteA","No");
		processVar.put("muxRecoveryRequiredSiteB","No");
		processVar.put("rfDeviceRecovery","No");
		processVar.put("mastRecovery","No");
		try {
			closeTask(scServiceDetail, "sales-negotiation-termination",
					"Auto closure of Retention Negotiation. 100 percent order reached");
		} catch (Exception e) {
			LOGGER.error("Error in closing sales negotiation for termination: {} ", e);
		}
		runtimeService.startProcessInstanceByKey("termination-full-fledged-workflow", processVar);
		try {
			ScServiceDetail serviceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeAndIsMigratedOrderAndIsActive(scServiceDetail.getUuid(), TaskStatusConstants.ACTIVE, "N", "Y");
			if(serviceDetail != null) {
				migrateFromCurrentInstance(scServiceDetail);
			}
			else {
				autoMigration(scServiceDetail, scOrder, true);
			}
		} catch (Exception e) {
			LOGGER.info("autoMigration for termination failed", serviceId, scServiceDetail.getUuid());
		}
		
		return true;
	}
	
	public void migrateFromCurrentInstance(ScServiceDetail scServiceDetail) {
		if (scServiceDetail != null) {
			String serviceId = scServiceDetail.getUuid();
			String scServiceDetailsId = scServiceDetail.getId().toString();
			Map<String, String> serviceAttributes = new HashMap<>();
			serviceAttributes.put("serviceId", serviceId);
			serviceAttributes.put("scServiceDetailsId", scServiceDetailsId);
			try {
				mqUtils.send(persistmigrationDataQueue, Utils.convertObjectToJson(serviceAttributes));
			} catch (TclCommonException e) {
				LOGGER.error("Persist migration Data failure for service code {} with error {}:",
						scServiceDetail.getUuid(), e);

			}
		}
	}
	
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public TerminationNegotiationResponse processTerminationOffnetPoTrfExtension(
			OdrServiceDetailBean odrServiceDetailBean,ScServiceDetail scServiceDetail) throws TclCommonException {

		TerminationNegotiationResponse terminationNegotiationResponse = new TerminationNegotiationResponse();


		String terminationEffectiveDate = odrServiceDetailBean.getTerminationEffectiveDate();
		String currentValue="";
		if(scServiceDetail!=null) {
			currentValue=scServiceDetail.getTerminationEffectiveDate();
		}
		LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
				scServiceDetail.getId(), scServiceDetail.getUuid());
		
		scServiceDetail.setTerminationEffectiveDate(terminationEffectiveDate);

		Map<String, String> atMap = new HashMap<>();
		atMap.put("terminationEffectiveDate", terminationEffectiveDate);
		
		scServiceDetailRepository.save(scServiceDetail);		
		
		saveTaskRemarksForTrfDateExtension(scServiceDetail, terminationEffectiveDate);
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "terminationEffectiveDate", AttributeConstants.COMPONENT_LM,
						AttributeConstants.SITETYPE_A);

		String userName=StringUtils.EMPTY;
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {
			 userName =userInfoUtils.getUserInformation().getUserId();
		}
		if(scComponentAttribute != null) {
			atMap.put("terminationEffectiveDateChangeRemark", "Termination Effective Date is changed from "+currentValue +" to "+terminationEffectiveDate+" by "+userName);
			updateServiceLogs(currentValue, terminationEffectiveDate, scServiceDetail.getId(), "TRFS Date");
		}
		
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
				AttributeConstants.COMPONENT_LM, "A");
		
		List<Task> tasks = taskRepository.findByServiceId(scServiceDetail.getId());
		boolean terminateoffnetBackhaulPoExist = false;
		boolean terminateoffnetPoExist = false;
		boolean terminateoffnetBackhaulPoExistB = false;
		boolean terminateoffnetPoExistB = false;
		for (Task task : tasks) {
			if (task.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_A)) {
				terminateoffnetBackhaulPoExist = true;
			} else if (task.getMstTaskDef().getKey().equals("terminate-offnet-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_A)) {
				terminateoffnetPoExist = true;
			}else if (task.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_B)) {
				terminateoffnetBackhaulPoExistB = true;
			} else if (task.getMstTaskDef().getKey().equals("terminate-offnet-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_B)) {
				terminateoffnetPoExistB = true;
			}
		}
		if(terminateoffnetBackhaulPoExist || terminateoffnetPoExist || terminateoffnetPoExistB || terminateoffnetBackhaulPoExistB) {
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("ASP_INDIA");
			if (!mstTaskRegionList.isEmpty()) {
				List<String> toAddresses = mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList());
				notificationService.notifyTrfExtensionDate(toAddresses, null,
						scServiceDetail.getScOrder().getUuid(), scServiceDetail.getUuid(), terminationEffectiveDate);
			}
		}
		
		boolean isBackhaulOffnetExist = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_A)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));
		boolean isOffnetPoExist = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-po-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_A)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));

		boolean isBackhaulOffnetExistB = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_B)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));
		boolean isOffnetPoExistB = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-po-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_B)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));

		if((isBackhaulOffnetExist && isOffnetPoExist && !scServiceDetail.getErfPrdCatalogProductName().equals("NPL"))
				|| (isBackhaulOffnetExist && isOffnetPoExist && isBackhaulOffnetExistB && isOffnetPoExistB) ) {
			terminationNegotiationResponse.setStatus("SUCCESS");
			return terminationNegotiationResponse;
		}
		
		Map<String, Object> processVar = new HashMap<>();

		if ((terminateoffnetBackhaulPoExist || terminateoffnetPoExist || terminateoffnetBackhaulPoExistB
				|| terminateoffnetPoExistB)) {
			processVar.put("trfsExtensionRequired", "Yes");
		}
		

		if (terminateoffnetBackhaulPoExist && !isBackhaulOffnetExist) {
			processVar.put("offnetHaulExtensionRequired", "Yes");
		}
		
		if (terminateoffnetBackhaulPoExistB && !isBackhaulOffnetExistB) {
			processVar.put("offnetHaulExtensionRequiredB", "Yes");
		}
		
		if (terminateoffnetPoExist && !isOffnetPoExist) {
			processVar.put("offnetPoExtensionRequired", "Yes");
		}
		
		if (terminateoffnetPoExistB && !isOffnetPoExistB) {
			processVar.put("offnetPoExtensionRequiredB", "Yes");
		}
		
		if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
			processVar.put("siteBType", AttributeConstants.SITETYPE_B);
		}
		
		processVar.put("getCLRSyncCallCompleted", false);
		processVar.put("getCLRSuccess", false);
		processVar.put("remainderCycle", reminderCycle);
		

		ScOrder scOrder = scServiceDetail.getScOrder();
		String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

		List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
		ScContractInfo scContractInfo = null;
		if (scContractInfos != null) {
			scContractInfo = scContractInfos.stream().findFirst().orElse(null);
		}

		if (scOrder.getErfCustCustomerName() != null) {
			processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
		}

		if (scOrder != null) {
			processVar.put(SC_ORDER_ID, scOrder.getId());
			processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
			processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
			if (scContractInfo != null) {
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
			}
			LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
			processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
			processVar.put("parentServiceCode", null);
			processVar.put("parentLmType", null);

			if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
				processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
			} else {
				processVar.put("orderSubCategory", "NA");
			}
			processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

			processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
			processVar.put(SERVICE_ID, scServiceDetail.getId());


			processVar.put("siteAType", "A");
			processVar.put("root_endDate", new Timestamp(new Date().getTime()));

			processVar.put("processType", "computeProjectPLan");

			processVar.put("ipBlockedResourceSuccess", false);
			processVar.put("terminationFlowTriggered", "Yes");
			processVar.put("remainderCycle", "R60/PT24H");
			processVar.put("terminationEffectiveDate", terminationEffectiveDate);
			if (!scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IAS")
					&& !scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
				processVar.put("offnetPoTerminationRequiredB", "No");
				processVar.put("offnetPoTerminationRequired", "No");
				processVar.put("offnetTerminationBackHaulRequiredB", "No");
				processVar.put("offnetTerminationBackHaulRequired", "No");
				processVar.put("offnetHaulExtensionRequired", "No");
				processVar.put("offnetHaulExtensionRequiredB", "No");
				processVar.put("offnetPoExtensionRequired", "No");
				processVar.put("offnetPoExtensionRequiredB", "No");

			}
			
			
			runtimeService.startProcessInstanceByKey("termination-offnet-trfsdate-extension-workflow", processVar);
			terminationNegotiationResponse.setStatus("SUCCESS");
			


		}
		return terminationNegotiationResponse;
	}
	
	
	public void processTerminationOffnetRetained(String serviceCode) throws TclCommonException {

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(
				serviceCode, TaskStatusConstants.TERMINATION_INITIATED);

		String terminationEffectiveDate = scServiceDetail.getTerminationEffectiveDate();
		LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
				scServiceDetail.getId(), scServiceDetail.getUuid());

		
		List<Task> tasks = taskRepository.findByServiceId(scServiceDetail.getId());
		boolean terminateoffnetBackhaulPoExist = false;
		boolean terminateoffnetPoExist = false;
		boolean terminateoffnetBackhaulPoExistB = false;
		boolean terminateoffnetPoExistB = false;
		for (Task task : tasks) {
			if (task.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_A)) {
				terminateoffnetBackhaulPoExist = true;
			} else if (task.getMstTaskDef().getKey().equals("terminate-offnet-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_A)) {
				terminateoffnetPoExist = true;
			} else if (task.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_B)) {
				terminateoffnetBackhaulPoExistB = true;
			} else if (task.getMstTaskDef().getKey().equals("terminate-offnet-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_B)) {
				terminateoffnetPoExistB = true;
			}
		}
		
		Map<String, Object> processVar = new HashMap<>();

		if (((terminateoffnetBackhaulPoExist || terminateoffnetPoExist)
				&& !scServiceDetail.getErfPrdCatalogProductName().equals("NPL"))
				|| (terminateoffnetBackhaulPoExist || terminateoffnetPoExist || terminateoffnetBackhaulPoExistB
						|| terminateoffnetPoExistB)) {
			processVar.put("cutomerRetained", "Yes");
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("ASP_INDIA");
			if (!mstTaskRegionList.isEmpty()) {
				List<String> toAddresses = mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList());
				notificationService.notifyoffnetRetained(toAddresses, new ArrayList<>(),
						scServiceDetail.getScOrder().getUuid(), scServiceDetail.getUuid());
			}
		}else {
			return;
		}
		
		LOGGER.info(
				"terminateoffnetBackhaulPoExist {}, terminateoffnetPoExist {}, terminateoffnetBackhaulPoExistB {}, terminateoffnetPoExistB {} and service code {}",
				terminateoffnetBackhaulPoExist, terminateoffnetPoExist, terminateoffnetBackhaulPoExistB,
				terminateoffnetPoExistB, scServiceDetail.getUuid());

		if (terminateoffnetBackhaulPoExist) {
			processVar.put("offnetHaulRetainedRequired", "Yes");
		}
		if (terminateoffnetPoExist) {
			processVar.put("offnetPoExtensionRequired", "Yes");
		}
		if (terminateoffnetBackhaulPoExistB) {
			processVar.put("offnetHaulRetainedRequiredB", "Yes");
		}
		if (terminateoffnetPoExistB) {
			processVar.put("offnetPoExtensionRequiredB", "Yes");
		}
		
		if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
			processVar.put("siteBType", AttributeConstants.SITETYPE_B);
		}

		ScOrder scOrder = scServiceDetail.getScOrder();
		String orderType = OrderCategoryMapping.getTerminationType(scServiceDetail, scOrder);

		List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
		ScContractInfo scContractInfo = null;
		if (scContractInfos != null) {
			scContractInfo = scContractInfos.stream().findFirst().orElse(null);
		}

		if (scOrder.getErfCustCustomerName() != null) {
			processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
		}

		if (scOrder != null) {
			processVar.put(SC_ORDER_ID, scOrder.getId());
			processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
			processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
			if (scContractInfo != null) {
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
			}
			LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
			processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
			processVar.put("parentServiceCode", null);
			processVar.put("parentLmType", null);

			if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
				processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
			} else {
				processVar.put("orderSubCategory", "NA");
			}
			processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

			processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
			processVar.put(SERVICE_ID, scServiceDetail.getId());


			processVar.put("siteAType", "A");
			processVar.put("root_endDate", new Timestamp(new Date().getTime()));

			processVar.put("processType", "computeProjectPLan");

			processVar.put("ipBlockedResourceSuccess", false);
			processVar.put("terminationFlowTriggered", "Yes");
			processVar.put("remainderCycle", "R60/PT24H");
			processVar.put("terminationEffectiveDate", terminationEffectiveDate);

			runtimeService.startProcessInstanceByKey("termination-offnet-retained-workflow", processVar);

		}
	}
	
	@Transactional(readOnly=false,isolation=Isolation.READ_UNCOMMITTED)
	public boolean processTerminationWorkflowPlan(Integer serviceId, ScServiceDetail scServiceDetail, boolean emailTrigger) throws TclCommonException {
		String lmType = "";
		String lastMileType = "";

		if (scServiceDetail == null) {
			
			Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

			scServiceDetail = opScServiceDetail.get();
		}

		String terminationEffectiveDate = getEffectiveDateForTermination(scServiceDetail.getTerminationEffectiveDate());
		LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
				scServiceDetail.getId(), scServiceDetail.getUuid());

		ScOrder scOrder = scServiceDetail.getScOrder();
		String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
		String orderSubCategory = scServiceDetail.getOrderSubCategory();
		String orderType = OrderCategoryMapping.getTerminationType(scServiceDetail, scOrder);

		orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
		orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
		List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
		ScContractInfo scContractInfo = null;
		if(scContractInfos != null) {
			 scContractInfo = scContractInfos.stream().findFirst().orElse(null);
		}

		List<String> notInCategories = new ArrayList<String>();
		notInCategories.add("FEASIBILITY");
		List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
				.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
						CommonConstants.Y, notInCategories);
		LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
		List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
				.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
						"FEASIBILITY");
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
		LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);
		
		Map<String, String> scComponentAttributesBMap =commonFulfillmentUtils.getComponentAttributes(
				scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "B");
		LOGGER.info("scComponentAttributesBMap {} ",scComponentAttributesBMap);

		Map<String, String> feasibilityAttributes = new HashMap<>();
		for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
			feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
					feasibilityAttribute.getAttributeValue());
		}
		Map<String, String> serviceDetailsAttributes = new HashMap<>();
		for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
			LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}", scServiceAttribute.getId(),
					scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());
			serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());
		}

		Map<String, Object> processVar = new HashMap<>();

		if (scOrder.getErfCustCustomerName() != null) {
			processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
		}

		
		if (scOrder != null) {
			processVar.put(SC_ORDER_ID, scOrder.getId());
			processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
			processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
			processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
			if(scContractInfo != null) {
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
			}
			LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
			processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
			processVar.put("parentServiceCode", null);
			processVar.put("parentLmType", null);

			if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
				processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
			} else {
				processVar.put("orderSubCategory", "NA");
			}
			processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
			String cpeType =null;
			String cpeModel = null;
			String offeringName = null;

			if (scServiceDetail != null) {

				// GVPN:UPDATE SITE TYPE BASED ON VPN TOPOLOGY
				if ("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())) {
					LOGGER.info("update vpn details");
					ScServiceAttribute siteTypeServiceAttribute = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
									"Site Type", "GVPN Common");
					if (Objects.nonNull(siteTypeServiceAttribute)) {
						ScServiceAttribute gvpnCommonServiceAttribute = scServiceAttributeRepository
								.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
										"VPN Topology", "GVPN Common");
						if (Objects.nonNull(gvpnCommonServiceAttribute)) {
							LOGGER.info("VPN common");
							updateVpnDetail(gvpnCommonServiceAttribute, siteTypeServiceAttribute);
						}
						ScServiceAttribute sitePropServiceAttribute = scServiceAttributeRepository
								.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
										"VPN Topology", "SITE_PROPERTIES");
						if (Objects.nonNull(sitePropServiceAttribute)) {
							LOGGER.info("VPN properties");
							updateVpnDetail(gvpnCommonServiceAttribute, siteTypeServiceAttribute);
						}
					}
				}

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));

				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				lastMileType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"));

				String btsDeviceType = StringUtils.trimToEmpty(feasibilityAttributes.get("bts_device_type"))
						.toLowerCase();

				String solutionType = StringUtils.trimToEmpty(feasibilityAttributes.get("solution_type")).toLowerCase();
				String cpeChassisChanged = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_chassis_changed"));
				String cpeVariant = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_variant"));
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));
				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));

				Double portBW = 0.0;

				String bwPortspeed = scComponentAttributesAMap.get("portBandwidth");
				if (bwPortspeed != null && NumberUtils.isCreatable(bwPortspeed)) {
					portBW = Double.valueOf(bwPortspeed);
				}
				if(cpeChassisChanged!=null  && (!cpeChassisChanged.equalsIgnoreCase("Y") || !cpeChassisChanged.equalsIgnoreCase("Yes"))) {
					compareAndUpdateMacdOrderAttributes(scServiceDetail.getUuid(), true,scServiceDetail.getIsAmended());
				}

				LOGGER.info(
						"cpeType for serviceid :{} and service code:{} and map:: {} cpeChassisChanged::{} cpeVariant::{} lastMileType::{}",
						serviceId, scServiceDetail.getUuid(), serviceDetailsAttributes, cpeChassisChanged, cpeVariant,
						lastMileType);

				 cpeType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE"));
				 cpeModel = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE Basic Chassis"));

				 String serviceType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Service type"));
					
				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{} serviceType={}", serviceId,scServiceDetail.getUuid(),cpeType,cpeModel,serviceType);
				
				if(StringUtils.isNotBlank(serviceType) && serviceType.toLowerCase().contains("usage")) {
					processVar.put("burstableOrder", true);
				}
					
				String cpeSiScope = "Customer provided";
				if (cpeType.contains("Rental")) {
					cpeType = "Rental";
					processVar.put("cpeType", cpeType);
				} else if (cpeType.contains("Outright")) {
					cpeType = "Outright";
					processVar.put("cpeType", cpeType);
				} else {
					processVar.put("cpeType", cpeType);
				}

				String bhConnectivity = StringUtils.trimToEmpty(feasibilityAttributes.get("BHConnectivity"));
				String providerName = StringUtils.trimToEmpty(feasibilityAttributes.get("closest_provider_bso_name"));

				if (StringUtils.isBlank(providerName))
					providerName = StringUtils.trimToEmpty(serviceDetailsAttributes.get("closest_provider_bso_name"));

				if (lastMileType.toLowerCase().contains("onnet wireless"))
					lastMileType = "OnnetRF";
				else if (lastMileType.toLowerCase().contains("offnet wireless"))
					lastMileType = "OffnetRF";
				else if (lastMileType.toLowerCase().contains("offnet wireline"))
					lastMileType = "OffnetWL";
				else if (lastMileType.toLowerCase().contains("onnet wireline"))
					lastMileType = "OnnetWL";
				else if (lastMileType.toLowerCase().contains("man") || lastMileType.toLowerCase().contains("wan aggregation"))
					lastMileType = "OnnetWL";

				lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileType.toLowerCase().contains("onnetrf")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Onnet Wireless")) {
					LOGGER.info("onnetrf");
					lmScenarioType = "Onnet Wireless";
					lmConnectionType = "Wireless";
					lmType = "onnet";

					if (providerName.toLowerCase().contains("p2p") && !providerName.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p/not pmp");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap = new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
					} else if (StringUtils.isBlank(providerName) && !StringUtils.isBlank(solutionType)
							&& solutionType.toLowerCase().contains("p2p")
							&& !solutionType.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p from solution type/not pmp/provider is blank");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap = new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
					}

					if (StringUtils.isBlank(bhConnectivity)) {
						if (providerName.equalsIgnoreCase("Radwin from TCL POP")) {
							bhConnectivity = "Radwin from TCL POP";
						}
					}

				} else if (lastMileType.toLowerCase().contains("offnetrf")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireless")) {
					LOGGER.info("offnetrf");
					lmScenarioType = "Offnet Wireless";
					lmConnectionType = "Wireless";
					lmType = "offnet";
					String supplierOldlocalLoopBw = "0.0";
					if (feasibilityAttributes.containsKey("old_Ll_Bw")) {
						supplierOldlocalLoopBw = feasibilityAttributes.get("old_Ll_Bw");
					}

					LOGGER.info("service code::{} portBW::{} supplierOldlocalLoopBw::{},skipOffnet::{}",
							scServiceDetail.getUuid(), portBW, supplierOldlocalLoopBw);
				} else if (lastMileType.toLowerCase().contains("offnetwl")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireline")) {
					LOGGER.info("offnetwl");
					lmScenarioType = "Offnet Wireline";

					lmConnectionType = "Wireline";
					lmType = "offnet";
					String supplierOldlocalLoopBw = "0.0";

					LOGGER.info("service code::{} portBW::{} supplierOldlocalLoopBw::{},skipOffnet::{}",
							scServiceDetail.getUuid(), portBW, supplierOldlocalLoopBw);
				}
				if (scComponentAttributesAMap.get("destinationCity") != null
						&& !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
					processVar.put("tier", setTierValue(scComponentAttributesAMap.get("destinationCity")));
				}
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);
				processVar.put("bhConnectivity", bhConnectivity);

				Map<String, String> atMap = new HashMap<>();
				
				atMap.put("terminationEffectiveDate", scServiceDetail.getTerminationEffectiveDate());

				LOGGER.info(
						"Input processL2ODataToFlowable received for lmScenarioType::{}  cpeManagementType::{} cpeType::{} isTaxAvailable::{} btsDeviceType::{} serviceId:: {} service code:: {}",
						lmScenarioType, cpeManagementType, cpeType, isTaxAvailable, btsDeviceType, serviceId,
						scServiceDetail.getUuid());

				 offeringName = StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName());

				
				
				if ("Fully Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation, Support";
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Unmanaged".equalsIgnoreCase(cpeManagementType)
						|| offeringName.toLowerCase().contains("unmanaged")) {
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Physically Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation";
				} else if ("Configuration Management".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Support";
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Proactive Services".equalsIgnoreCase(cpeManagementType)) {
					processVar.put("cpeSiScope", cpeSiScope);
				} else {
					processVar.put("cpeSiScope", cpeSiScope);
				}

				processVar.put("siteAType", "A");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));

				if (lastMileType.toLowerCase().contains("rf")) {

					String antenaSize = StringUtils.trimToEmpty(feasibilityAttributes.get("Mast_3KM_avg_mast_ht"));
					String mastType = StringUtils.trimToEmpty(feasibilityAttributes.get("mast_type"));
					String structureType = mastType;
					try {
						if (StringUtils.isNotBlank(antenaSize)) {
							Double antenaSizeDouble = Double.parseDouble(antenaSize);
							if (antenaSizeDouble > 6)
								structureType = "Mast";
						}
					} catch (Exception ee) {
						LOGGER.info("Mast_3KM_avg_mast_ht issue {}", ee);

					}
					if (StringUtils.isBlank(structureType))
						structureType = "Pole";

					atMap.put("structureType", structureType);
					processVar.put("structureType", structureType);

					if (btsDeviceType.contains("radwin") || solutionType.contains("radwin")
							|| "Radwin from TCL POP".equalsIgnoreCase(providerName)) {
						atMap.put("rfTechnology", "RADWIN");
						atMap.put("rfMake", "Radwin");
					} else if (btsDeviceType.contains("cambium") || solutionType.contains("cambium")) {
						atMap.put("rfTechnology", "CAMBIUM");
						atMap.put("rfMake", "Cambium");
					} else {
						atMap.put("rfTechnology", "CAMBIUM");
						atMap.put("rfMake", "Cambium");
					}

					atMap.put("bhProviderName", bhConnectivity);
				} else {
					LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);
					if (serviceDetailsAttributes.containsKey("mux_details")) {

						try {

							String paramValue = serviceDetailsAttributes.get("mux_details");

							Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
									.findById(Integer.valueOf(paramValue));
							if (scAdditionalServiceParam.isPresent()) {
								String value = scAdditionalServiceParam.get().getValue();

								MuxDetailsItem[] muxDetailBeans = Utils.convertJsonToObject(value,
										MuxDetailsItem[].class);
								MuxDetailsItem muxDetailBean = muxDetailBeans[0];

								atMap.put("endMuxNodeName", muxDetailBean.getMux());
								atMap.put("endMuxNodeIp", muxDetailBean.getMuxIp());

							}
						} catch (Exception e) {
							LOGGER.error("error in parsing mux details : {} ", scServiceDetail.getUuid());

						}
					}

				}

				processBomDetails(scServiceDetail, atMap, scComponentAttributesAMap);

				if (scServiceDetail.getSiteEndInterface() != null) {
					String interfaceType = getInterfaceValue(scServiceDetail.getSiteEndInterface());
					atMap.put("interfaceType", interfaceType);
					processVar.put("internalCablingInterface", interfaceType);
				} else {
					LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
				}
				try {
					if (Objects.nonNull(lmType) && lmType.equalsIgnoreCase("offnet")) {
						String supplierCategory = getOffnetSupplierCategory(lmConnectionType, feasibilityAttributes);
						if (Objects.nonNull(supplierCategory)) {
							atMap.put("offnetSupplierCategory", supplierCategory);
							processVar.put("offnetSupplierCategory", supplierCategory);
						}
					}
				} catch (Exception ex) {
					LOGGER.error("error in getting supplier category : {} ", scServiceDetail.getUuid());
					LOGGER.error("error in getting supplier category : {} ", ex);
				}
				atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				atMap.put("providerName", providerName);

				processVar.put("providerName", providerName);

				atMap.put("lmType", lastMileType);

				atMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");

				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));

				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if (mstStateToDistributionCenterMapping != null
						&& mstStateToDistributionCenterMapping.getMasterTclDistributionCenter() != null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant",
							mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getSapStorageLocation()));
				}

				if ("MACD".equalsIgnoreCase(StringUtils.trimToEmpty(orderType))
						&& "N".equalsIgnoreCase(scServiceDetail.getIsMigratedOrder())) {
					ScServiceDetail parentScServiceDetail = scServiceDetailRepository
							.findFirstByUuidAndMstStatus_codeOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if (parentScServiceDetail != null) {
						ScComponentAttribute parentUuidCommissioningDate = scComponentAttributesRepository
								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
										parentScServiceDetail.getId(), "commissioningDate",
										AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
						if (parentUuidCommissioningDate != null
								&& parentUuidCommissioningDate.getAttributeValue() != null) {
							LOGGER.info("Parent Service Commissioning date is {}",
									parentUuidCommissioningDate.getAttributeValue());
							atMap.put("parentUuidCommissioningDate", parentUuidCommissioningDate.getAttributeValue());
						}
					}
				}

				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);

				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null
						&& poNumber.getAttributeValue() != null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");

			}
			
			if (lastMileType.equalsIgnoreCase("OnnetRF") || lastMileType.equalsIgnoreCase("onnet wireless")) {
				processVar.put("rfDeviceRecovery", "Yes");
				processVar.put("mastRecovery", "Yes");

			}
			if (lastMileType.equalsIgnoreCase("OnnetWL") || lastMileType.equalsIgnoreCase("Onnet Wireline")) {
				processVar.put("muxRecoveryRequired", "Yes");
				processVar.put("muxRecoveryRequiredSiteA", "Yes");
			}
			if(cpeType.contains("Rental") &&!offeringName.toLowerCase().contains("unmanaged") ) {
				processVar.put("cpeRecoveryRequired", "Yes");

			}
			if(lastMileType.toLowerCase().contains("wireless")
					|| lastMileType.toLowerCase().contains("rf")) {
				processVar.put("isRFExists", "Yes");
			}
			processVar.put("processType", "computeProjectPLan");

		}
		processVar.put("ipBlockedResourceSuccess", false);
		processVar.put("terminationFlowTriggered", "Yes");
		processVar.put("remainderCycle", "R60/PT24H");
		processVar.put("checkCLRSuccess", false);
		processVar.put("terminationEffectiveDate", terminationEffectiveDate);
		processVar.put("processType", "computeProjectPLan");
		runtimeService.startProcessInstanceByKey("plan_termination-full-fledged-workflow", processVar);
		return true;
	}
	
	private String getEffectiveDateForTermination(String terminationEffectiveDate) throws TclCommonException {
		try{
			LocalDateTime localDateTime=null;
			String fromTime = "11:59";
			LOGGER.info("effective date for termination {} {} :",terminationEffectiveDate, fromTime);
			terminationEffectiveDate = terminationEffectiveDate.split(" ")[0];
			terminationEffectiveDate=terminationEffectiveDate+" "+fromTime;
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(terminationEffectiveDate);
			localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC"));
			localDateTime = localDateTime.plusDays(1);
			terminationEffectiveDate=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			LOGGER.info("Derived effective date for termination::{}",terminationEffectiveDate);
			return terminationEffectiveDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForTermination:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	private String getTerminationEmailDate(String terminationEffectiveDate) throws TclCommonException {
		try{
			LocalDateTime localDateTime=null;
			LOGGER.info("Effective date for termination {} :",terminationEffectiveDate);
			terminationEffectiveDate = terminationEffectiveDate.split(" ")[0];
			terminationEffectiveDate=terminationEffectiveDate;
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(terminationEffectiveDate);
			localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC"));
			localDateTime = localDateTime.minusDays(5);
			LocalDateTime currentDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC"));
			if(localDateTime.compareTo(currentDateTime) <= 0) {
				currentDateTime = currentDateTime.plusMinutes(10);
				terminationEffectiveDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			}else {
				terminationEffectiveDate=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			}
			LOGGER.info("Derived Email date for termination::{}",terminationEffectiveDate);
			return terminationEffectiveDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForTermination:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	

	/**
	 * @author vivek
	 *
	 * @param id
	 * @param scServiceDetail
	 * @param b
	 */
	@Transactional(readOnly=false,isolation=Isolation.READ_UNCOMMITTED)
	public boolean processNplTerminationWorkflow(Integer serviceId, ScServiceDetail scServiceDetail, boolean b) {
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}
			String terminationEffectiveDate = getEffectiveDateForTermination(scServiceDetail.getTerminationEffectiveDate());
			LOGGER.info("Input processNPLL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
					serviceId, scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = null;
			if(scContractInfos != null) {
				scContractInfo = scContractInfos.stream().findFirst().orElse(null);
			}

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			List<String> componentLis = Arrays.asList("terminationSubType", "terminationSubReason", "oem",
					"cpeSerialNumber");
			Map<String, String> componentMap = new HashMap<>();

			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",
						scServiceAttribute.getId(), scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
				if (componentLis.contains(scServiceAttribute.getAttributeName())) {
					componentMap.put(scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());

				}
			}
			

		componentAndAttributeService.updateAttributes(serviceId, componentMap, "LM", "A");

			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);

			Map<String, String> scComponentAttributesBMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "B");
			LOGGER.info("scComponentAttributesBMap {} ", scComponentAttributesBMap);

			Map<String, Object> processVar = new HashMap<>();

			String customerUserName = StringUtils.trimToEmpty(scOrder.getErfCustCustomerName());
			processVar.put("customerUserName", customerUserName);
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

			String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			


			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
				if(scContractInfo != null) {
					processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				}
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());

				processVar.put("parentServiceCode", null);
				processVar.put("isParallelExists", "false");
				if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				} else {
					processVar.put("orderSubCategory", "NA");
				}
				if (Objects.nonNull(scServiceDetail.getParentUuid())) {
					processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
				}

			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
			String crossConnectType = "";
			String siteEndInterfaceA = null;
			String siteEndInterfaceB = null;
			if (scServiceDetail != null) {

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());

				String lastMileTypeA = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_a"));
				String lastMileTypeB = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_b"));
				if (StringUtils.isBlank(lastMileTypeA)) {
					lastMileTypeA = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"));

				}
				if (StringUtils.isBlank(lastMileTypeB)) {
					lastMileTypeB = StringUtils.trimToEmpty(scComponentAttributesBMap.get("lmType"));

				}


				LOGGER.info("cpeType for serviceid :{} and service code:{} and map:: {}", scServiceDetail.getUuid(),
						serviceDetailsAttributes);

				processVar.put("cpeType", "");

				siteEndInterfaceA = scComponentAttributesAMap.get("interface");
				siteEndInterfaceB = scComponentAttributesBMap.get("interface");

				if (lastMileTypeA.toLowerCase().contains("onnet wireless") || lastMileTypeB.equalsIgnoreCase("OnnetRF"))
					lastMileTypeA = "OnnetRF";
				else if (lastMileTypeA.toLowerCase().contains("offnet wireless") || lastMileTypeB.equalsIgnoreCase("OffnetRF"))
					lastMileTypeA = "OffnetRF";
				else if (lastMileTypeA.toLowerCase().contains("offnet wireline") || lastMileTypeB.equalsIgnoreCase("OffnetWL"))
					lastMileTypeA = "OffnetWL";
				else if (lastMileTypeA.toLowerCase().contains("onnet wireline") || lastMileTypeB.equalsIgnoreCase("OnnetWL"))
					lastMileTypeA = "OnnetWL";
				else if (lastMileTypeA.toLowerCase().contains("onnetwl_npl") || lastMileTypeB.equalsIgnoreCase("OnnetWL"))
					lastMileTypeA = "OnnetWL";
				else if (lastMileTypeA.toLowerCase().contains("man") || lastMileTypeB.equalsIgnoreCase("OnnetWL") || lastMileTypeA.toLowerCase().contains("wan aggregation"))
					lastMileTypeA = "OnnetWL";

				if (StringUtils.isBlank(lastMileTypeA))
					lastMileTypeA = "OnnetWL";

				String lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileTypeA.toLowerCase().contains("offnetwl")) {
					lmScenarioType = "Offnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "offnet";
					lastMileTypeA = "OffnetWL";
				} else {
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileTypeA = "OnnetWL";

					processVar.put(IS_CONNECTED_SITE, true);
					processVar.put(IS_CONNECTED_BUILDING, false);
					lmScenarioType = "Onnet Wireline - Connected Customer";

				}
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);

				if (lastMileTypeB.toLowerCase().contains("onnet wireless")  || lastMileTypeB.equalsIgnoreCase("OnnetRF"))
					lastMileTypeB = "OnnetRF";
				else if (lastMileTypeB.toLowerCase().contains("offnet wireless") || lastMileTypeB.equalsIgnoreCase("OffnetRF"))
					lastMileTypeB = "OffnetRF";
				else if (lastMileTypeB.toLowerCase().contains("offnet wireline") || lastMileTypeB.equalsIgnoreCase("OffnetWL"))
					lastMileTypeB = "OffnetWL";
				else if (lastMileTypeB.toLowerCase().contains("onnet wireline")|| lastMileTypeB.equalsIgnoreCase("OnnetWL"))
					lastMileTypeB = "OnnetWL";
				else if (lastMileTypeB.toLowerCase().contains("onnetwl_npl")|| lastMileTypeB.equalsIgnoreCase("OnnetWL"))
					lastMileTypeB = "OnnetWL";
				else if (lastMileTypeB.toLowerCase().contains("man")|| lastMileTypeB.equalsIgnoreCase("OnnetWL") || lastMileTypeB.toLowerCase().contains("wan aggregation"))
					lastMileTypeB = "OnnetWL";
				if (StringUtils.isBlank(lastMileTypeB))
					lastMileTypeB = "OnnetWL";

				crossConnectType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Cross Connect Type"));
				String mediaType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Media Type"));

				LOGGER.info("CrossConnectType {}", crossConnectType);
				LOGGER.info("MediaType {}", mediaType);
				Map<String, String> atMap = new HashMap<>();
				Map<String, String> btMap = new HashMap<>();
				atMap.put("terminationEffectiveDate", scServiceDetail.getTerminationEffectiveDate());
				btMap.put("terminationEffectiveDate", scServiceDetail.getTerminationEffectiveDate());
				String interfaceTypeA = "";
				String interfaceTypeB = "";


				processBomDetails(scServiceDetail, atMap, scComponentAttributesAMap);
				processBomDetails(scServiceDetail, btMap, scComponentAttributesBMap);

				if ("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())
						&& "Passive".equalsIgnoreCase(crossConnectType)) {
					LOGGER.info("MMR Passive for interface");
					if (mediaType != null && !mediaType.isEmpty()) {
						LOGGER.info("mediaType exists");
						String siteEndInterface = getInterfaceValueBasedOnMediaType(mediaType);
						processVar.put("internalCablingInterface", siteEndInterface);
						processVar.put("internalCablingInterfaceB", siteEndInterface);
						atMap.put("interfaceType", siteEndInterface);
						btMap.put("interfaceType", siteEndInterface);
					} else {
						LOGGER.error("mediaType missing for : {} ", scServiceDetail.getUuid());
					}
				} else {
					LOGGER.info("Other than MMR Passive for interface");

					if (siteEndInterfaceA != null) {
						interfaceTypeA = getInterfaceValue(siteEndInterfaceA);
						atMap.put("interfaceType", interfaceTypeA);
						if (siteEndInterfaceA.contains("G.957")) {// G.957[Optical to Cramer, But for Cabling it is
																	// electrical]
							LOGGER.info("A end:: Converting G.957 to Electrical");
							processVar.put("internalCablingInterface", "Electrical");
							processVar.put("interface", "G.957");
						} else {
							LOGGER.info("A end::  Other than G.957");
							processVar.put("internalCablingInterface", interfaceTypeA);
							processVar.put("interface", siteEndInterfaceA);
						}
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}

					if (siteEndInterfaceB != null) {
						interfaceTypeB = getInterfaceValue(siteEndInterfaceB);
						btMap.put("interfaceType", interfaceTypeB);
						if (siteEndInterfaceB.contains("G.957") || siteEndInterfaceB.equalsIgnoreCase("optical")) {
							LOGGER.info("B end:: Converting G.957 to Electrical");
							processVar.put("internalCablingInterfaceB", "Electrical");
							processVar.put("interfaceB", "G.957");
							btMap.put("interface", "G.957");
						} else {
							LOGGER.info("B end::  Other than G.957");
							processVar.put("internalCablingInterfaceB", interfaceTypeB);
							processVar.put("interfaceB", siteEndInterfaceB);
						}
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}
				}

				// Lastmile B
				String lmScenarioTypeB = "";
				String lmConnectionTypeB = "";
				if (lastMileTypeB.toLowerCase().contains("offnetwl")) {
					lmScenarioTypeB = "Offnet Wireline";
					lmConnectionTypeB = "Wireline";
					lmType = "offnet";
					lastMileTypeB = "OffnetWL";
				} else {
					lmScenarioTypeB = "Onnet Wireline";
					lmConnectionTypeB = "Wireline";
					lmType = "onnet";
					lastMileTypeB = "OnnetWL";
					lmScenarioTypeB = "Onnet Wireline - Connected Customer";
				}
				processVar.put(LM_TYPE_B, lmType);
				processVar.put(LM_CONNECTION_TYPE_B, lmConnectionTypeB);

				processVar.put("siteAType", "A");
				processVar.put("siteBType", "B");
				processVar.put("site_type", "A");

				processVar.put("root_endDate", new Timestamp(new Date().getTime()));

				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));

				processVar.put(SITE_ADDRESS_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(CITY_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL_B,
						StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE_B,
						StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME_B,
						StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactName")));
				processVar.put(SITE_ADDRESS_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(STATE_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationState")));
				processVar.put(COUNTRY_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCountry")));

				atMap.put("cpeType", "Customer provided");
				atMap.put("cpeSiScope", "Customer provided");
				btMap.put("cpeType", "Customer provided");
				btMap.put("cpeSiScope", "Customer provided");

				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				btMap.put("lastMileScenario", lmScenarioTypeB);
				btMap.put("lmConnectionType", lmConnectionTypeB);
				atMap.put("lmType", lastMileTypeA);
				btMap.put("lmType", lastMileTypeB);

				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);

				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null
						&& poNumber.getAttributeValue() != null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				atMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				LOGGER.info("lmType A= {} lmType B={}", lastMileTypeA, lastMileTypeB);
				
				if(!scComponentAttributesAMap.isEmpty()) {
					if(scComponentAttributesAMap.containsKey("customerMailReceiveDate")) {
						atMap.put("customerRequestorDate", StringUtils.trimToEmpty(scComponentAttributesAMap.get("customerMailReceiveDate")));
						scServiceDetail.setCustomerRequestorDate(StringUtils.trimToEmpty(scComponentAttributesAMap.get("customerMailReceiveDate")));
					}
					if (scComponentAttributesAMap.containsKey("amc_required")) {
						atMap.put("amcRequired",
								StringUtils.trimToEmpty(scComponentAttributesAMap.get("amc_required")));
					}
				}
				
				if(!scComponentAttributesBMap.isEmpty()) {
					if(scComponentAttributesBMap.containsKey("customerMailReceiveDate")) {
						btMap.put("customerRequestorDate", StringUtils.trimToEmpty(scComponentAttributesBMap.get("customerMailReceiveDate")));
						scServiceDetail.setCustomerRequestorDate(StringUtils.trimToEmpty(scComponentAttributesBMap.get("customerMailReceiveDate")));
					}
					if (scComponentAttributesBMap.containsKey("amc_required")) {
						btMap.put("amcRequired",
								StringUtils.trimToEmpty(scComponentAttributesBMap.get("amc_required")));
					}
				}
				
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");

				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), btMap,
						AttributeConstants.COMPONENT_LM, "B");

			}
			processVar.put("processType", "computeProjectPLan");

			if (scComponentAttributesAMap.get("destinationCity") != null
					&& !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
				processVar.put("tier", setTierValue(scComponentAttributesAMap.get("destinationCity")));
			}
			if (scComponentAttributesBMap.get("destinationCity") != null
					&& !scComponentAttributesBMap.get("destinationCity").isEmpty()) {
				processVar.put("tierB", setTierValue(scComponentAttributesBMap.get("destinationCity")));
			}

			if (!siteEndInterfaceB.contains("G.957")) {

				processVar.put("muxRecoveryRequired", "Yes");

				processVar.put("muxRecoveryRequiredSiteB", "Yes");

			}

			if (!siteEndInterfaceB.contains("G.957")) {

				processVar.put("muxRecoveryRequired", "Yes");

				processVar.put("muxRecoveryRequiredSiteA", "Yes");

			}
			scServiceDetailRepository.save(scServiceDetail);
			processVar.put("terminationFlowTriggered", "Yes");
			processVar.put("ipBlockedResourceSuccess", false);
			processVar.put("ipTerminationRequired", "No");
			processVar.put("checkCLRSuccess", false);
			processVar.put("getCLRSyncCallCompleted", false);
			processVar.put("getCLRSuccess", false);
			processVar.put("terminationEffectiveDate", terminationEffectiveDate);
			processVar.put("skipTerminationEmail", "No");
			processVar.put("terminationEmailDate", getTerminationEmailDate(scServiceDetail.getTerminationEffectiveDate()));
			processVar.put("terminationFromL20", "Yes");
			processVar.put("remainderCycle", "R60/PT24H");
			try {
			closeTask(scServiceDetail,"sales-negotiation-termination","Auto closure of Retention Negotiation. 100 percent order reached");
			}
			catch(Exception e) {
				LOGGER.error("Error in closing sales negotiation for termination: {} ", e);
			}
			// hardcode to no needed as required by PO 
			processVar.put("cpeRecoveryRequired","No");
			processVar.put("muxRecoveryRequiredSiteA","No");
			processVar.put("muxRecoveryRequiredSiteB","No");
			processVar.put("rfDeviceRecovery","No");
			processVar.put("mastRecovery","No");
			runtimeService.startProcessInstanceByKey("termination-full-fledged-workflow", processVar);

			LOGGER.info("termination flow completed for NPL");
		} catch (Exception e) {

			LOGGER.error("Error processing data : {} ", e);
		}
		
		return true;
	}
	
	@Transactional(readOnly=false,isolation=Isolation.READ_UNCOMMITTED)
	public boolean processNplTerminationWorkflowPlan(Integer serviceId, ScServiceDetail scServiceDetail, boolean b) {
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}
			String terminationEffectiveDate = getEffectiveDateForTermination(scServiceDetail.getTerminationEffectiveDate());
			LOGGER.info("Input processNPLL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
					serviceId, scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = null;
			if(scContractInfos != null) {
				scContractInfo = scContractInfos.stream().findFirst().orElse(null);
			}

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",
						scServiceAttribute.getId(), scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}

			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);

			Map<String, String> scComponentAttributesBMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "B");
			LOGGER.info("scComponentAttributesBMap {} ", scComponentAttributesBMap);

			Map<String, Object> processVar = new HashMap<>();

			String customerUserName = StringUtils.trimToEmpty(scOrder.getErfCustCustomerName());
			processVar.put("customerUserName", customerUserName);
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

			String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
				if(scContractInfo != null ) {
					processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				}
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());

				processVar.put("parentServiceCode", null);
				processVar.put("isParallelExists", "false");
				if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				} else {
					processVar.put("orderSubCategory", "NA");
				}
				if (Objects.nonNull(scServiceDetail.getParentUuid())) {
					processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
				}

			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
			String crossConnectType = "";
			String siteEndInterfaceA = null;
			String siteEndInterfaceB = null;
			if (scServiceDetail != null) {

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());

				String lastMileTypeA = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_a"));
				String lastMileTypeB = StringUtils.trimToEmpty(feasibilityAttributes.get("lm_type_b"));

				String connectedBuildingA = StringUtils
						.trimToEmpty(feasibilityAttributes.get("a_connected_building_tag"));
				String connectedCustomerA = StringUtils.trimToEmpty(feasibilityAttributes.get("a_connected_cust_tag"));

				String connectedCustomerCategoryA = StringUtils
						.trimToEmpty(feasibilityAttributes.get("a_Orch_Category"));
				String connectedCustomerCategoryB = StringUtils
						.trimToEmpty(feasibilityAttributes.get("b_Orch_Category"));
				String connectedBuildingB = StringUtils
						.trimToEmpty(feasibilityAttributes.get("b_connected_building_tag"));
				String connectedCustomerB = StringUtils.trimToEmpty(feasibilityAttributes.get("b_connected_cust_tag"));

				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));

				LOGGER.info("cpeType for serviceid :{} and service code:{} and map:: {}", scServiceDetail.getUuid(),
						serviceDetailsAttributes);

				processVar.put("cpeType", "");

				siteEndInterfaceA = scComponentAttributesAMap.get("interface");
				siteEndInterfaceB = scComponentAttributesBMap.get("interface");

				if (lastMileTypeA.toLowerCase().contains("onnet wireless"))
					lastMileTypeA = "OnnetRF";
				else if (lastMileTypeA.toLowerCase().contains("offnet wireless"))
					lastMileTypeA = "OffnetRF";
				else if (lastMileTypeA.toLowerCase().contains("offnet wireline"))
					lastMileTypeA = "OffnetWL";
				else if (lastMileTypeA.toLowerCase().contains("onnet wireline"))
					lastMileTypeA = "OnnetWL";
				else if (lastMileTypeA.toLowerCase().contains("onnetwl_npl"))
					lastMileTypeA = "OnnetWL";
				else if (lastMileTypeA.toLowerCase().contains("man") || lastMileTypeA.toLowerCase().contains("wan aggregation"))
					lastMileTypeA = "OnnetWL";

				if (StringUtils.isBlank(lastMileTypeA))
					lastMileTypeA = "OnnetWL";

				String lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileTypeA.toLowerCase().contains("offnetwl")) {
					lmScenarioType = "Offnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "offnet";
					lastMileTypeA = "OffnetWL";
				} else {
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileTypeA = "OnnetWL";
					if (connectedCustomerA.contains("1") || siteEndInterfaceA.contains("G.957")
							|| connectedCustomerCategoryA.equalsIgnoreCase("Connected Customer")) {
						processVar.put(IS_CONNECTED_SITE, true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						lmScenarioType = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingA.contains("1")) {
						lmScenarioType = "Onnet Wireline - Connected Building";
					} else {
						lmScenarioType = "Onnet Wireline - Near Connect";
					}

				}
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);

				if (lastMileTypeB.toLowerCase().contains("onnet wireless"))
					lastMileTypeB = "OnnetRF";
				else if (lastMileTypeB.toLowerCase().contains("offnet wireless"))
					lastMileTypeB = "OffnetRF";
				else if (lastMileTypeB.toLowerCase().contains("offnet wireline"))
					lastMileTypeB = "OffnetWL";
				else if (lastMileTypeB.toLowerCase().contains("onnet wireline"))
					lastMileTypeB = "OnnetWL";
				else if (lastMileTypeB.toLowerCase().contains("onnetwl_npl"))
					lastMileTypeB = "OnnetWL";
				else if (lastMileTypeB.toLowerCase().contains("man") || lastMileTypeB.toLowerCase().contains("wan aggregation"))
					lastMileTypeB = "OnnetWL";
				if (StringUtils.isBlank(lastMileTypeB))
					lastMileTypeB = "OnnetWL";

				String subProduct = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Sub Product"));
				crossConnectType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Cross Connect Type"));
				String mediaType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Media Type"));

				LOGGER.info("CrossConnectType {}", crossConnectType);
				LOGGER.info("MediaType {}", mediaType);
				Map<String, String> atMap = new HashMap<>();
				Map<String, String> btMap = new HashMap<>();
				atMap.put("terminationEffectiveDate", scServiceDetail.getTerminationEffectiveDate());
				btMap.put("terminationEffectiveDate", scServiceDetail.getTerminationEffectiveDate());
				String interfaceTypeA = "";
				String interfaceTypeB = "";

				LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);

				processBomDetails(scServiceDetail, atMap, scComponentAttributesAMap);
				processBomDetails(scServiceDetail, btMap, scComponentAttributesBMap);

				if ("MMR Cross Connect".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())
						&& "Passive".equalsIgnoreCase(crossConnectType)) {
					LOGGER.info("MMR Passive for interface");
					if (mediaType != null && !mediaType.isEmpty()) {
						LOGGER.info("mediaType exists");
						String siteEndInterface = getInterfaceValueBasedOnMediaType(mediaType);
						processVar.put("internalCablingInterface", siteEndInterface);
						processVar.put("internalCablingInterfaceB", siteEndInterface);
						atMap.put("interfaceType", siteEndInterface);
						btMap.put("interfaceType", siteEndInterface);
					} else {
						LOGGER.error("mediaType missing for : {} ", scServiceDetail.getUuid());
					}
				} else {
					LOGGER.info("Other than MMR Passive for interface");

					if (siteEndInterfaceA != null) {
						interfaceTypeA = getInterfaceValue(siteEndInterfaceA);
						atMap.put("interfaceType", interfaceTypeA);
						if (siteEndInterfaceA.contains("G.957")) {// G.957[Optical to Cramer, But for Cabling it is
																	// electrical]
							LOGGER.info("A end:: Converting G.957 to Electrical");
							processVar.put("internalCablingInterface", "Electrical");
							processVar.put("interface", "G.957");
						} else {
							LOGGER.info("A end::  Other than G.957");
							processVar.put("internalCablingInterface", interfaceTypeA);
							processVar.put("interface", siteEndInterfaceA);
						}
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}

					if (siteEndInterfaceB != null) {
						interfaceTypeB = getInterfaceValue(siteEndInterfaceB);
						btMap.put("interfaceType", interfaceTypeB);
						if (siteEndInterfaceB.contains("G.957") || siteEndInterfaceB.equalsIgnoreCase("optical")) {
							LOGGER.info("B end:: Converting G.957 to Electrical");
							processVar.put("internalCablingInterfaceB", "Electrical");
							processVar.put("interfaceB", "G.957");
							btMap.put("interface", "G.957");
						} else {
							LOGGER.info("B end::  Other than G.957");
							processVar.put("internalCablingInterfaceB", interfaceTypeB);
							processVar.put("interfaceB", siteEndInterfaceB);
						}
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}
				}

				// Lastmile B
				String lmScenarioTypeB = "";
				String lmConnectionTypeB = "";
				if (lastMileTypeB.toLowerCase().contains("offnetwl")) {
					lmScenarioTypeB = "Offnet Wireline";
					lmConnectionTypeB = "Wireline";
					lmType = "offnet";
					lastMileTypeB = "OffnetWL";
				} else {
					lmScenarioTypeB = "Onnet Wireline";
					lmConnectionTypeB = "Wireline";
					lmType = "onnet";
					lastMileTypeB = "OnnetWL";
					if (connectedCustomerB.contains("1") || siteEndInterfaceB.contains("G.957")
							|| "Hybrid NPL".equalsIgnoreCase(subProduct)
							|| customerUserName.contains("Tata Teleservices") || interfaceTypeB.contains("Optical")
							|| connectedCustomerCategoryB.equalsIgnoreCase("Connected Customer")) {
						lmScenarioTypeB = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingB.contains("1")) {
						lmScenarioTypeB = "Onnet Wireline - Connected Building";
					} else {
						lmScenarioTypeB = "Onnet Wireline - Near Connect";
					}
				}
				processVar.put(LM_TYPE_B, lmType);
				processVar.put(LM_CONNECTION_TYPE_B, lmConnectionTypeB);

				processVar.put("siteAType", "A");
				processVar.put("siteBType", "B");
				processVar.put("site_type", "A");

				processVar.put("root_endDate", new Timestamp(new Date().getTime()));

				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));

				processVar.put(SITE_ADDRESS_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(CITY_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCity")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL_B,
						StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE_B,
						StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME_B,
						StringUtils.trimToEmpty(scComponentAttributesBMap.get("localItContactName")));
				processVar.put(SITE_ADDRESS_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("siteAddress")));
				processVar.put(STATE_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationState")));
				processVar.put(COUNTRY_B, StringUtils.trimToEmpty(scComponentAttributesBMap.get("destinationCountry")));

				atMap.put("cpeType", "Customer provided");
				atMap.put("cpeSiScope", "Customer provided");
				btMap.put("cpeType", "Customer provided");
				btMap.put("cpeSiScope", "Customer provided");

				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				btMap.put("lastMileScenario", lmScenarioTypeB);
				btMap.put("lmConnectionType", lmConnectionTypeB);
				atMap.put("lmType", lastMileTypeA);
				btMap.put("lmType", lastMileTypeB);

				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);

				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null
						&& poNumber.getAttributeValue() != null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					btMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				atMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				LOGGER.info("lmType A= {} lmType B={}", lastMileTypeA, lastMileTypeB);
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");

				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), btMap,
						AttributeConstants.COMPONENT_LM, "B");

			}
			processVar.put("processType", "computeProjectPLan");

			if (scComponentAttributesAMap.get("destinationCity") != null
					&& !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
				processVar.put("tier", setTierValue(scComponentAttributesAMap.get("destinationCity")));
			}
			if (scComponentAttributesBMap.get("destinationCity") != null
					&& !scComponentAttributesBMap.get("destinationCity").isEmpty()) {
				processVar.put("tierB", setTierValue(scComponentAttributesBMap.get("destinationCity")));
			}

			if (!siteEndInterfaceB.contains("G.957")) {

				processVar.put("muxRecoveryRequired", "Yes");

				processVar.put("muxRecoveryRequiredSiteB", "Yes");

			}

			if (!siteEndInterfaceB.contains("G.957")) {

				processVar.put("muxRecoveryRequired", "Yes");

				processVar.put("muxRecoveryRequiredSiteA", "Yes");

			}
			processVar.put("terminationFlowTriggered", "Yes");
			processVar.put("ipBlockedResourceSuccess", false);
			processVar.put("checkCLRSuccess", false);
			processVar.put("terminationEffectiveDate", terminationEffectiveDate);
			processVar.put("processType", "computeProjectPLan");
			processVar.put("skipTerminationEmail", "No");
			runtimeService.startProcessInstanceByKey("plan_termination-full-fledged-workflow", processVar);

			LOGGER.info("termination flow completed for NPL");
		} catch (Exception e) {

			LOGGER.error("Error processing data : {} ", e);
		}
		
		return true;
	}
	
	@Transactional
	public boolean triggerAutoMigration(Integer scServiceDetaisId, Integer scOrderId, boolean termination) {

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetaisId).get();

		autoMigration(scServiceDetail, scServiceDetail.getScOrder(), termination);
		
		return true;

	}

	@Transactional(readOnly=false)
	public void processSlaveWorkFlow(String serviceCode) {
		ScServiceDetail masterServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,TaskStatusConstants.INPROGRESS);
		if(masterServiceDetail!=null){
		List<ScServiceDetail> slaveServiceDetails = scServiceDetailRepository.findByMasterVrfServiceId(masterServiceDetail.getId());
		if(slaveServiceDetails!=null && !slaveServiceDetails.isEmpty()) {
			slaveServiceDetails.stream().forEach(slaveServiceDetail -> {
                triggerSlaveWorkFlow(slaveServiceDetail.getId(),masterServiceDetail.getWorkFlowName());
			});
		  }
		}
	}
	@Transactional(readOnly=false)
	public Boolean triggerSlaveWorkFlow(Integer serviceId,String flowName) {
		Boolean status = true;
		boolean isP2PwithoutBH = false;
		boolean isP2PwithBH = false;
		String lmType = "";
		String lastMileType = "";
		Boolean isValidLM = false;
		Boolean skipOffnet = false;
		Map<String, Object> processVar = new HashMap<>();

		try {
			Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

			ScServiceDetail scServiceDetail = opScServiceDetail.get();

			processVar.put("multivrfFlow", flowName);
			processVar.put("demoOrder", "N");
			processVar.put("isDemoOrderBillable", "N");
			processVar.put("multiVrfBillingRequiredSkip", "No");
            processVar.put("cpe_chassis_changed", "No");
			LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}", serviceId,
					scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			String orderSubCategory = scServiceDetail.getOrderSubCategory();
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

			orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
			orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = null;
			if (scContractInfos != null) {
				scContractInfo = scContractInfos.stream().findFirst().orElse(null);
			}

			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("FEASIBILITY");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(scServiceDetail.getId(),
							CommonConstants.Y, notInCategories);
			LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
			List<ScServiceAttribute> feasibilityAttributeslist = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategory(scServiceDetail.getId(), CommonConstants.Y,
							"FEASIBILITY");
			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);

			Map<String, String> feasibilityAttributes = new HashMap<>();
			for (ScServiceAttribute feasibilityAttribute : feasibilityAttributeslist) {
				feasibilityAttributes.put(feasibilityAttribute.getAttributeName(),
						feasibilityAttribute.getAttributeValue());
			}
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",
						scServiceAttribute.getId(), scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}

			if(serviceDetailsAttributes.containsKey("VRF based billing") && serviceDetailsAttributes.get("VRF based billing")!=null
			&& "Consolidated billing".equalsIgnoreCase(serviceDetailsAttributes.get("VRF based billing"))){
				LOGGER.info("multiVrfBillingRequiredSkip skipped for service::{}",
						scServiceDetail.getUuid());
				processVar.put("multiVrfBillingRequiredSkip", "Yes");
			}
			String oldEquipmentModel = null;
			if ("MACD".equalsIgnoreCase(orderType) && !"ADD_SITE".equals(orderCategory)
					&& !orderSubCategory.toLowerCase().contains("parallel")
					&& scServiceDetail.getServiceLinkId() != null) {
				LOGGER.info("MACD:: Equipment Model Attr retrival for parent Id::{}",
						scServiceDetail.getServiceLinkId());
				ScServiceAttribute eqpModelAttr = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(
						scServiceDetail.getServiceLinkId(), "EQUIPMENT_MODEL");
				if (eqpModelAttr != null && eqpModelAttr.getAttributeValue() != null
						&& !eqpModelAttr.getAttributeValue().isEmpty()) {
					LOGGER.info("Equipment Model Attr exists with value::{}", eqpModelAttr.getAttributeValue());
					oldEquipmentModel = eqpModelAttr.getAttributeValue();
				}
			}

			if (scOrder.getErfCustCustomerName() != null) {
				processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
			}

			if (scOrder != null) {
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				processVar.put(ORDER_CATEGORY, StringUtils.trimToEmpty(orderCategory));
				if (scContractInfo != null) {
					processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				}
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				processVar.put("parentServiceCode", null);
				processVar.put("parentLmType", null);
				processVar.put("txResourcePathType", null);
				processVar.put("isParallelExists", "false");
				processVar.put("isAmendedOrder", false);

				if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
					processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
				} else {
					processVar.put("orderSubCategory", "NA");
				}
				if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())
						&& scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")) {
					LOGGER.info("ProcessL2O:ParallelExists");
					processVar.put("isParallelExists", "true");
					if (Objects.nonNull(scServiceDetail.getParentUuid())) {
						processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
					}
				}
			}
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

			if (scServiceDetail != null) {

				// GVPN:UPDATE SITE TYPE BASED ON VPN TOPOLOGY
				if ("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())) {
					LOGGER.info("update vpn details");
					ScServiceAttribute siteTypeServiceAttribute = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
									"Site Type", "GVPN Common");
					if (Objects.nonNull(siteTypeServiceAttribute)) {
						ScServiceAttribute gvpnCommonServiceAttribute = scServiceAttributeRepository
								.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
										"VPN Topology", "GVPN Common");
						if (Objects.nonNull(gvpnCommonServiceAttribute)) {
							LOGGER.info("VPN common");
							updateVpnDetail(gvpnCommonServiceAttribute, siteTypeServiceAttribute);
						}
						ScServiceAttribute sitePropServiceAttribute = scServiceAttributeRepository
								.findFirstByScServiceDetail_idAndAttributeNameAndCategory(scServiceDetail.getId(),
										"VPN Topology", "SITE_PROPERTIES");
						if (Objects.nonNull(sitePropServiceAttribute)) {
							LOGGER.info("VPN properties");
							updateVpnDetail(gvpnCommonServiceAttribute, siteTypeServiceAttribute);
						}
					}
				}

				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
				processVar.put(SERVICE_ID, scServiceDetail.getId());
				processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));

				processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
				processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
				processVar.put(COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
				processVar.put(LOCAL_IT_CONTACT_EMAIL,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
				processVar.put(LOCAL_IT_CONTACT_MOBILE,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
				processVar.put(LOCAL_IT_CONTACT_NAME,
						StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
				String isTaxAvailable = StringUtils.trimToEmpty(scComponentAttributesAMap.get("taxExemption"));

				lastMileType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"));

				String connectedBuilding = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_Building_tag"));
				String connectedCustomer = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_cust_tag"));
				String connectedCustomerCategory = StringUtils.trimToEmpty(feasibilityAttributes.get("Orch_Category"));

				String btsDeviceType = StringUtils.trimToEmpty(feasibilityAttributes.get("bts_device_type"))
						.toLowerCase();

				String solutionType = StringUtils.trimToEmpty(feasibilityAttributes.get("solution_type")).toLowerCase();
				String cpeChassisChanged = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_chassis_changed"));
				String cpeVariant = StringUtils.trimToEmpty(feasibilityAttributes.get("cpe_variant"));
				String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("cpeManagementType"));
				String localLoopBW = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidth"));

				String supplierLocalLoopBw = StringUtils.trimToEmpty(feasibilityAttributes.get("local_loop_bw"));
				String upgradeType = StringUtils.trimToEmpty(feasibilityAttributes.get("upgrade_type"));

				Double portBW = 0.0;

				String bwPortspeed = scComponentAttributesAMap.get("portBandwidth");
				if (bwPortspeed != null && NumberUtils.isCreatable(bwPortspeed)) {
					portBW = Double.valueOf(bwPortspeed);
				}
				LOGGER.info("Entering CPE maneged copying data {} {} {}  :", scOrder.getOrderType(), cpeChassisChanged,
						cpeManagementType);
				if ("MACD".equalsIgnoreCase(scOrder.getOrderType()) && cpeChassisChanged != null
						&& "Fully Managed".equalsIgnoreCase(cpeManagementType)
						&& (!cpeChassisChanged.equalsIgnoreCase("Y") || !cpeChassisChanged.equalsIgnoreCase("Yes"))) {
					LOGGER.info("Inside CPE related attribute update for MACD order");
					if (scServiceDetail.getOrderSubCategory().equalsIgnoreCase("Parallel Shifting")) {
						LOGGER.info("Inside CPE related attribute update for MACD order for Paralle Shifting");
						compareAndUpdateMacdOrderAttributes(scServiceDetail.getParentUuid(),true,scServiceDetail.getIsAmended());
					} else {
						LOGGER.info("Inside CPE related attribute update for MACD order for other sub category");
						compareAndUpdateMacdOrderAttributes(scServiceDetail.getUuid(),true,scServiceDetail.getIsAmended());
					}

				}

				LOGGER.info(
						"cpeType for serviceid :{} and service code:{} and map:: {} cpeChassisChanged::{} cpeVariant::{} lastMileType::{}",
						serviceId, scServiceDetail.getUuid(), serviceDetailsAttributes, cpeChassisChanged, cpeVariant,
						lastMileType);

				String cpeType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE"));
				String cpeModel = StringUtils.trimToEmpty(serviceDetailsAttributes.get("CPE Basic Chassis"));

				String serviceType = StringUtils.trimToEmpty(serviceDetailsAttributes.get("Service type"));
				
				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{} serviceType={}", serviceId,scServiceDetail.getUuid(),cpeType,cpeModel,serviceType);
				
				if(StringUtils.isNotBlank(serviceType) && serviceType.toLowerCase().contains("usage")) {
					processVar.put("burstableOrder", true);
				}
				
				String cpeSiScope = "Customer provided";
				if (cpeType.contains("Rental")) {
					cpeType = "Rental";
					processVar.put("cpeType", cpeType);
				} else if (cpeType.contains("Outright")) {
					cpeType = "Outright";
					processVar.put("cpeType", cpeType);
				} else {
					processVar.put("cpeType", cpeType);
				}

				boolean isLMRequired = true;
				boolean isColoRequired = false;
				boolean negotiationRequiredForOffnetLM = false;
				boolean rowRequired = true;
				boolean prowRequired = true;
				boolean isRFRequired = false;

				String bhConnectivity = StringUtils.trimToEmpty(feasibilityAttributes.get("BHConnectivity"));
				String providerName = StringUtils.trimToEmpty(feasibilityAttributes.get("closest_provider_bso_name"));

				if (StringUtils.isBlank(providerName))
					providerName = StringUtils.trimToEmpty(serviceDetailsAttributes.get("closest_provider_bso_name"));

				processVar.put(IS_CONNECTED_SITE, false);
				processVar.put("checkCLRSuccess", false);
				processVar.put(IS_CONNECTED_BUILDING, false);
				processVar.put("isP2PwithoutBH", isP2PwithoutBH);
				processVar.put("isP2PwithBH", isP2PwithBH);
				processVar.put("parallelDownTime", "PT30M");
				processVar.put("isAccountRequired", false);

				if (lastMileType.toLowerCase().contains("onnet wireless"))
					lastMileType = "OnnetRF";
				else if (lastMileType.toLowerCase().contains("offnet wireless"))
					lastMileType = "OffnetRF";
				else if (lastMileType.toLowerCase().contains("offnet wireline"))
					lastMileType = "OffnetWL";
				else if (lastMileType.toLowerCase().contains("onnet wireline"))
					lastMileType = "OnnetWL";
				else if (lastMileType.toLowerCase().contains("man") || lastMileType.toLowerCase().contains("wan aggregation"))
					lastMileType = "OnnetWL";

				// if(StringUtils.isBlank(lastMileType))lastMileType="OnnetWL";
				processVar.put("txRequired", false);
				lmType = "onnet";
				String lmScenarioType = "";
				String lmConnectionType = "Wireline";
				if (lastMileType.toLowerCase().contains("onnetrf")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Onnet Wireless")) {
					LOGGER.info("onnetrf case for Service ID:: {}", scServiceDetail.getUuid());
					lmScenarioType = "Onnet Wireless";
					lmConnectionType = "Wireless";
					lmType = "onnet";
					processVar.put("sameDayInstallation", false);
					processVar.put("rfSiteFeasible", true);
					processVar.put("mastApprovalRequired", true);
					isValidLM = true;

					if (Objects.nonNull(solutionType) && !solutionType.isEmpty() && "ubrp2pmp".equals(solutionType)) {
						solutionType = "Radwin from TCL POP";
						LOGGER.info("radwin/ubrp2pmp");
						isP2PwithoutBH = true;
						isColoRequired = true;
						Map<String, String> attrMap = new HashMap<>();

						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
					}

					if (StringUtils.isBlank(providerName) || Objects.isNull(providerName)) {
						String lastMileProvider = StringUtils
								.trimToEmpty(scComponentAttributesAMap.get("lastMileProvider"));
						providerName = StringUtils.trimToEmpty(lastMileProvider);
						LOGGER.info("providerName set from lastMileProvider as {} for Service ID: {}", lastMileProvider,
								scServiceDetail.getUuid());
					}

					if (providerName.toLowerCase().contains("tcl") && providerName.toLowerCase().contains("radwin")
							&& !providerName.toLowerCase().contains("pmp")) {
						LOGGER.info("tcl/radwin/not pmp");
						isP2PwithoutBH = true;
						isColoRequired = true;
					} else if (providerName.toLowerCase().contains("backhaul")
							&& providerName.toLowerCase().contains("radwin")
							&& !providerName.toLowerCase().contains("pmp")) {
						LOGGER.info("backhaul/radwin/not pmp");
						isP2PwithBH = true;
						isColoRequired = true;
					} else if (providerName.toLowerCase().contains("radwin")
							&& !providerName.toLowerCase().contains("pmp")) {
						LOGGER.info("radwin/not pmp");
						isP2PwithoutBH = true;
						isColoRequired = true;
					} else if (providerName.toLowerCase().contains("p2p")
							&& !providerName.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p/not pmp");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap = new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
						isP2PwithoutBH = true;
						isColoRequired = true;
					} else if (StringUtils.isBlank(providerName) && !StringUtils.isBlank(solutionType)
							&& solutionType.toLowerCase().contains("p2p")
							&& !solutionType.toLowerCase().contains("pmp")) {
						LOGGER.info("p2p from solution type/not pmp/provider is blank");
						providerName = "Radwin from TCL POP";
						Map<String, String> attrMap = new HashMap<>();
						attrMap.put("closest_provider_bso_name", "Radwin from TCL POP");
						attrMap.put("solution_type", "Radwin from TCL POP");
						componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						Map<String, String> atMap = new HashMap<>();
						atMap.put("lastMileProvider", providerName);
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
						isP2PwithoutBH = true;
						isColoRequired = true;
					} else if (StringUtils.isBlank(providerName) && StringUtils.isBlank(solutionType)) {
						isValidLM = false;
						LOGGER.info("service code::{} Not a valid LM::Provider Name & Solution Type is blank",
								scServiceDetail.getUuid());
					}

					processVar.put("isP2PwithoutBH", isP2PwithoutBH);
					processVar.put("isP2PwithBH", isP2PwithBH);

					if (StringUtils.isBlank(bhConnectivity)) {
						if (providerName.equalsIgnoreCase("Radwin from TCL POP")) {
							bhConnectivity = "Radwin from TCL POP";
						}
					}

					if (isP2PwithBH) {
						if (StringUtils.isNotBlank(bhConnectivity) && !(bhConnectivity.equalsIgnoreCase("NA")
								|| bhConnectivity.equalsIgnoreCase("ONNET") || bhConnectivity.contains("TCL"))) {
							processVar.put("txRequired", true);
						} else {
							processVar.put("txRequired", false);
						}
					} else {
						processVar.put("txRequired", false);
					}
					processVar.put("skipOffnet", false);
				} else if (lastMileType.toLowerCase().contains("offnetrf")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireless")) {
					LOGGER.info("offnetrf");
					lmScenarioType = "Offnet Wireless";
					lmConnectionType = "Wireless";
					lmType = "offnet";
					isValidLM = true;
					processVar.put("sameDayInstallation", false);
					processVar.put("rfSiteFeasible", true);
					processVar.put("mastApprovalRequired", true);
					processVar.put("txRequired", false);
					String supplierOldlocalLoopBw = "0.0";
					if (feasibilityAttributes.containsKey("old_Ll_Bw")) {
						supplierOldlocalLoopBw = feasibilityAttributes.get("old_Ll_Bw");
					}
					 if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
	                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
								|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
	                    				
								|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {

						if (StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
							LOGGER.info("SkipOffnet False");
							processVar.put("skipOffnet", false);
						} else {
							LOGGER.info("SkipOffnet True");
							processVar.put("skipOffnet", true);
							skipOffnet = true;
						}
					} else {
						LOGGER.info("SkipOffnet False");
						processVar.put("skipOffnet", false);
					}

					LOGGER.info("service code::{} supplierLocalLoopBw::{} supplierOldlocalLoopBw::{},skipOffnet::{}",
							scServiceDetail.getUuid(), supplierLocalLoopBw, supplierOldlocalLoopBw, skipOffnet);
				} else if (lastMileType.toLowerCase().contains("offnetwl")
						|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireline")) {
					LOGGER.info("offnetwl");
					lmScenarioType = "Offnet Wireline";
					isValidLM = true;

					lmConnectionType = "Wireline";
					lmType = "offnet";
					processVar.put("isMuxIPAvailable", false);
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					processVar.put("skipOffnet", false);
					String supplierOldlocalLoopBw = "0.0";
					if (feasibilityAttributes.containsKey("old_Ll_Bw")) {
						supplierOldlocalLoopBw = feasibilityAttributes.get("old_Ll_Bw");
					}

					 if(StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
	                    		&& ( (((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")))
								|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) <= Double.valueOf(supplierOldlocalLoopBw) )
	                    				
								|| ( (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
										&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot downgrade"))) && 
								Objects.nonNull(supplierLocalLoopBw) && Objects.nonNull(supplierOldlocalLoopBw)
								&& Double.valueOf(supplierLocalLoopBw) == Double.valueOf(supplierOldlocalLoopBw)) ) ) {

						if (StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
							LOGGER.info("SkipOffnet False");
							processVar.put("skipOffnet", false);
						} else {
							LOGGER.info("SkipOffnet True");
							processVar.put("skipOffnet", true);
							skipOffnet = true;
						}
					} else {
						LOGGER.info("SkipOffnet False");
						processVar.put("skipOffnet", false);
					}

					LOGGER.info("service code::{} supplierLocalLoopBw::{} supplierOldlocalLoopBw::{},skipOffnet::{}",
							scServiceDetail.getUuid(), supplierLocalLoopBw, supplierOldlocalLoopBw, skipOffnet);
				} else if (lastMileType.toLowerCase().contains("onnetwl")
						|| lastMileType.toLowerCase().equalsIgnoreCase("onnet wireline")) {
					LOGGER.info("onnetwl");
					lmScenarioType = "Onnet Wireline";
					lmConnectionType = "Wireline";
					lmType = "onnet";
					lastMileType = "OnnetWL";
					processVar.put("skipOffnet", false);
					isValidLM = true;
					processVar.put("isMuxInfoAvailable", false);
					processVar.put("txRequired", true);
					if (connectedCustomer.contains("1")
							|| connectedCustomerCategory.equalsIgnoreCase("Connected Customer")
							|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
									&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
											&& StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Hot Upgrade")
											&& StringUtils.trimToEmpty(orderSubCategory)
													.equalsIgnoreCase("Hot Downgrade"))
											|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))) {
						LOGGER.info("ConnectedSite");
						connectedCustomer = "1";
						processVar.put(IS_CONNECTED_SITE, true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put("isMuxIPAvailable", false);
						rowRequired = false;
						prowRequired = false;
						lmScenarioType = "Onnet Wireline - Connected Customer";
					} else if (connectedBuilding.contains("1")) {
						LOGGER.info("ConnectedBuilding");
						rowRequired = false;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put("isNetworkSelectedPerBOP", true);
						processVar.put(IS_CONNECTED_BUILDING, true);
						processVar.put(IS_CONNECTED_SITE, false);
						lmScenarioType = "Onnet Wireline - Connected Building";
					} else {
						LOGGER.info("Near Connect");
						rowRequired = true;
						prowRequired = true;
						processVar.put("isMuxIPAvailable", true);
						processVar.put("isNetworkSelectedPerBOP", true);
						processVar.put(IS_CONNECTED_BUILDING, false);
						processVar.put(IS_CONNECTED_SITE, false);
						lmScenarioType = "Onnet Wireline - Near Connect";
					}
				}
				if (scComponentAttributesAMap.get("destinationCity") != null
						&& !scComponentAttributesAMap.get("destinationCity").isEmpty()) {
					processVar.put("tier", setTierValue(scComponentAttributesAMap.get("destinationCity")));
				}
				processVar.put(LM_TYPE, lmType);
				processVar.put(LM_CONNECTION_TYPE, lmConnectionType);
				processVar.put("bhConnectivity", bhConnectivity);

				Map<String, String> atMap = new HashMap<>();

				if (lastMileType.toLowerCase().contains("offnet")) {
					LOGGER.info("getDestinationState:{} and getSourceState:{}",
							scComponentAttributesAMap.get("destinationState"),
							scComponentAttributesAMap.get("sourceState"));

					if (scComponentAttributesAMap.get("destinationState") != null
							&& scComponentAttributesAMap.get("sourceState") != null
							&& !scComponentAttributesAMap.get("destinationState")
									.equalsIgnoreCase(scComponentAttributesAMap.get("sourceState"))) {
						processVar.put("offnetDifferentState", true);
					} else {
						processVar.put("offnetDifferentState", false);
					}

					if (StringUtils.isNotBlank(supplierLocalLoopBw) && !supplierLocalLoopBw.equals(localLoopBW)) {
						LOGGER.info(
								"supplierLocalLoopBw-and-localLoopBandwidth-not-same-updating-supplierLocalLoopBw= {} and {}",
								supplierLocalLoopBw, localLoopBW);
						atMap.put("localLoopBandwidth", supplierLocalLoopBw);
					}
					
					Double supplierLocalLoopBwDouble = 0.0;
					if (supplierLocalLoopBw != null
							&& NumberUtils.isCreatable(supplierLocalLoopBw)) {
						supplierLocalLoopBwDouble = Double.valueOf(supplierLocalLoopBw);
					}

					Double offnetWlNrc = 0.0;
					if (feasibilityAttributes.get("lm_otc_nrc_installation_offwl") != null)
						offnetWlNrc = Double.valueOf(feasibilityAttributes.get("lm_otc_nrc_installation_offwl"));
					Double offnetRFNrc = 0.0;
					if (feasibilityAttributes.get("lm_nrc_bw_prov_ofrf") != null)
						offnetRFNrc = Double.valueOf(feasibilityAttributes.get("lm_nrc_bw_prov_ofrf"));
					LOGGER.info("Total offnetWlNrc is {} offnetRFNrc is {} and Bandwidth is {} ", offnetWlNrc,
							offnetRFNrc, supplierLocalLoopBwDouble);
					if ((offnetWlNrc > 100000 || offnetRFNrc > 100000) || supplierLocalLoopBwDouble > 100) {
						negotiationRequiredForOffnetLM = true;
						LOGGER.info("Setting the Negotiation Required for Offnet LM as true");
					}
					processVar.put("negotiationRequiredForOffnetLM", negotiationRequiredForOffnetLM);
				}

				LOGGER.info(
						"Input processL2ODataToFlowable received for lmScenarioType::{}  cpeManagementType::{} cpeType::{} isTaxAvailable::{} btsDeviceType::{} serviceId:: {} service code:: {}",
						lmScenarioType, cpeManagementType, cpeType, isTaxAvailable, btsDeviceType, serviceId,
						scServiceDetail.getUuid());

				String offeringName = StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName());

				if ("Fully Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation, Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Unmanaged".equalsIgnoreCase(cpeManagementType)
						|| offeringName.toLowerCase().contains("unmanaged")) {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				} else if ("Physically Managed".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Supply, Installation";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", false);
				} else if ("Configuration Management".equalsIgnoreCase(cpeManagementType)) {
					cpeSiScope = "Support";
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", false);
					processVar.put("isCPEArrangedByCustomer", true);
				} else if ("Proactive Services".equalsIgnoreCase(cpeManagementType)) {
					processVar.put("cpeSiScope", cpeSiScope);
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
				} else {
					processVar.put("isCPEConfiguredByCustomer", true);
					processVar.put("isCPEArrangedByCustomer", true);
					processVar.put("cpeSiScope", cpeSiScope);
				}

				if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
						&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("SHIFT_SITE")
								|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) {
					if ("Yes".equalsIgnoreCase(cpeChassisChanged) && StringUtils.isNotBlank(cpeVariant)
							&& !cpeVariant.equalsIgnoreCase("None") && !cpeVariant.equalsIgnoreCase("null")
							&& !cpeVariant.equalsIgnoreCase("NA")) {
						LOGGER.info(
								"MACD cpeModelchange cpeVariant={} cpeChassisChanged={} serviceCode={} orderCode={} cpeManagementType={}",
								cpeVariant, cpeChassisChanged, scServiceDetail.getUuid(),
								scServiceDetail.getScOrderUuid(), cpeManagementType);
						if (StringUtils.isNotBlank(oldEquipmentModel)
								&& cpeVariant.toLowerCase().contains(oldEquipmentModel.toLowerCase())) {
							LOGGER.info("Old and Current Cpe Model matched");
							processVar.put("isCPEConfiguredByCustomer", true);
							processVar.put("isCPEArrangedByCustomer", true);
							processVar.put("cpeSiScope", "");
							Map<String, String> attrMap = new HashMap<>();
							attrMap.put("cpe_chassis_changed", "No");
							componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
						} else {
							LOGGER.info("Old and Current Cpe Model not matched");
						}
					} else {
						if (!"Unmanaged".equalsIgnoreCase(cpeManagementType)
								|| !offeringName.toLowerCase().contains("unmanaged")) {
							LOGGER.info(
									"STOPPINGCPEFLOW=>MACD cpeManagementType={} cpeChassisChanged={} cpeVariant={} serviceCode={} orderCode={}",
									cpeManagementType, cpeChassisChanged, cpeVariant, scServiceDetail.getUuid(),
									scServiceDetail.getScOrderUuid());
							processVar.put("isCPEConfiguredByCustomer", true);
							processVar.put("isCPEArrangedByCustomer", true);
							processVar.put("cpeSiScope", "");
						}

					}
				}

				processVar.put("documentValidationRequired", true);
				processVar.put("isLMRequired", isLMRequired);
				processVar.put("e2eServiceTestingCompleted", false);
				processVar.put("siteReadinessStatus", true);
				processVar.put("siteReadinessConfirmation", false);
				processVar.put("rowRequired", rowRequired);
				processVar.put("prowRequired", prowRequired);
				processVar.put("hasOSPCapexDeviation", false);
				processVar.put("hasIBDCapexDeviation", false);
				processVar.put("ibdContractRequired", true);
				processVar.put("isMuxRequired", false);
				processVar.put("isCPERequired", true);
				processVar.put("createServiceCompleted", false);
				processVar.put("isCLRSyncCallSuccess", false);
				processVar.put("serviceDesignCompleted", false);
				processVar.put("txConfigurationCompleted", false);
				processVar.put("serviceConfigurationCompleted", false);
				processVar.put("isStandardCPEDiscount", true);
				processVar.put("ipTerminateConfigurationSuccess", false);
				processVar.put("isRFRequired", isRFRequired);
				processVar.put("isColoRequired", isColoRequired);
				processVar.put("failoverTestingRequired", false);
				processVar.put("additionalTechDetailsTaskCompleted", false);
				processVar.put("siteReadinessTaskCompleted", false);
				processVar.put("isValidDocuments", false);
				processVar.put("isSoftLoopPossibleAtCE", false);
				processVar.put("lmTestOnnetWirelessSuccess", true);
				processVar.put("isPEInternalCablingRequired", false);
				processVar.put("isCEInternalCablingRequired", true);
				processVar.put("cableSwappingPERequired", "No");
				processVar.put("cableSwappingCERequired", "No");
				processVar.put("internalCablingResponsibility", "TCL");
				processVar.put("internalCablingInterface", "");
				processVar.put("serviceDeliveryPlanReady", false);
				processVar.put("order_enrichment_complete", false);
				processVar.put("getIpServiceSyncCallCompleted", false);
				processVar.put("validateBtsStatus", false);

				processVar.put("confirmOrderRequired", true);
				if (scServiceDetail.getIsAmended() != null
						&& scServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
					processVar.put("isAmendedOrder", true);

				}

				processVar.put("serviceConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("serviceConfigurationMessageSent", false);
				processVar.put("serviceConfigurationAckSuccess", false);
				processVar.put("serviceConfigurationSuccess", false);
				processVar.put("txConfigurationMessageSent", false);
				processVar.put("txConfigurationAckSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txSDHConfigurationSuccess", false);
				processVar.put("txMPLSConfigurationSuccess", false);
				processVar.put("rfConfigurationAction", "PE_PROV_CONFIG");
				processVar.put("rfConfigurationMessageSent", false);
				processVar.put("rfConfigurationAckSuccess", false);
				processVar.put("rfConfigurationSuccess", false);

				processVar.put("previewIpConfigMessageSent", false);
				processVar.put("previewIpConfigAckSuccess", false);
				processVar.put("previewIpConfigSuccess", false);
				processVar.put("cancelIpConfigMessageSent", false);
				processVar.put("cancelIpConfigAckSuccess", false);
				processVar.put("cancelIpConfigSuccess", false);
				processVar.put("txManualConfigRequired", false);

				processVar.put("cpeLicensePRCompleted", true);
				processVar.put("cpeHardwarePRCompleted", true);
				processVar.put("isCpeAvailableInInventory", false);
				processVar.put("cpeLicenseNeeded", true);
				processVar.put("remainderCycle", reminderCycle);
				processVar.put("deemedAcceptanceDuration", "PT48H");
				processVar.put("cpeConfigurationCompleted", true);
				processVar.put("offnetPOEnabled", true);

				processVar.put("siteAType", "A");
				processVar.put("site_type", "A");

				processVar.put("root_endDate", new Timestamp(new Date().getTime()));

				if (isLMRequired) {

					if (lastMileType.toLowerCase().contains("rf")) {

						String antenaSize = StringUtils.trimToEmpty(feasibilityAttributes.get("Mast_3KM_avg_mast_ht"));
						String mastType = StringUtils.trimToEmpty(feasibilityAttributes.get("mast_type"));
						String structureType = mastType;
						try {
							if (StringUtils.isNotBlank(antenaSize)) {
								Double antenaSizeDouble = Double.parseDouble(antenaSize);
								if (antenaSizeDouble > 6)
									structureType = "Mast";
							}
						} catch (Exception ee) {
							LOGGER.info("Mast_3KM_avg_mast_ht issue {}", ee);

						}
						if (StringUtils.isBlank(structureType))
							structureType = "Pole";

						atMap.put("structureType", structureType);
						processVar.put("structureType", structureType);

						if (btsDeviceType.contains("radwin") || solutionType.contains("radwin")
								|| "Radwin from TCL POP".equalsIgnoreCase(providerName)) {
							atMap.put("rfTechnology", "RADWIN");
							atMap.put("rfMake", "Radwin");
						} else if (btsDeviceType.contains("cambium") || solutionType.contains("cambium")) {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						} else {
							atMap.put("rfTechnology", "CAMBIUM");
							atMap.put("rfMake", "Cambium");
						} 

						atMap.put("bhProviderName", bhConnectivity);
					} else {
						LOGGER.info("Calling mux Select service for localLoopBw {}", localLoopBW);
						if (connectedCustomer.contains("1") && serviceDetailsAttributes.containsKey("mux_details")) {

							try {

								String paramValue = serviceDetailsAttributes.get("mux_details");

								Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
										.findById(Integer.valueOf(paramValue));
								if (scAdditionalServiceParam.isPresent()) {
									String value = scAdditionalServiceParam.get().getValue();

									MuxDetailsItem[] muxDetailBeans = Utils.convertJsonToObject(value,
											MuxDetailsItem[].class);
									MuxDetailsItem muxDetailBean = muxDetailBeans[0];

									atMap.put("endMuxNodeName", muxDetailBean.getMux());
									atMap.put("endMuxNodeIp", muxDetailBean.getMuxIp());

								}
							} catch (Exception e) {
								LOGGER.error("error in parsing mux details : {} ", scServiceDetail.getUuid());

							}
						}

					}

					processBomDetails(scServiceDetail, atMap, scComponentAttributesAMap);

					if (scServiceDetail.getSiteEndInterface() != null) {
						String interfaceType = getInterfaceValue(scServiceDetail.getSiteEndInterface());
						atMap.put("interfaceType", interfaceType);
						processVar.put("internalCablingInterface", interfaceType);
					} else {
						LOGGER.error("scServiceDetail missing for : {} ", scServiceDetail.getUuid());
					}
				}
				try {
					if (Objects.nonNull(lmType) && lmType.equalsIgnoreCase("offnet")) {
						String supplierCategory = getOffnetSupplierCategory(lmConnectionType, feasibilityAttributes);
						if (Objects.nonNull(supplierCategory)) {
							atMap.put("offnetSupplierCategory", supplierCategory);
							processVar.put("offnetSupplierCategory", supplierCategory);
						}
					}
				} catch (Exception ex) {
					LOGGER.error("error in getting supplier category : {} ", scServiceDetail.getUuid());
					LOGGER.error("error in getting supplier category : {} ", ex);
				}
				atMap.put("cpeType", cpeType);
				atMap.put("cpeSiScope", cpeSiScope);
				atMap.put("lastMileScenario", lmScenarioType);
				atMap.put("lmConnectionType", lmConnectionType);
				atMap.put("providerName", providerName);

				processVar.put("providerName", providerName);

				LOGGER.error("lastMileType : {} isValidLM: {}", lastMileType, isValidLM);
				atMap.put("lmType", lastMileType);

				atMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");

				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));

				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				if (mstStateToDistributionCenterMapping != null
						&& mstStateToDistributionCenterMapping.getMasterTclDistributionCenter() != null) {
					atMap.put("distributionCenterName", mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getDistributionCenterName());
					atMap.put("distributionCenterState", mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getDistributionCenterState());
					atMap.put("distributionCenterPLant",
							mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
					atMap.put("distributionCenterStorageLocation", String.valueOf(mstStateToDistributionCenterMapping
							.getMasterTclDistributionCenter().getSapStorageLocation()));
				}

				if ("MACD".equalsIgnoreCase(StringUtils.trimToEmpty(orderType))
						&& "N".equalsIgnoreCase(scServiceDetail.getIsMigratedOrder())) {
					ScServiceDetail parentScServiceDetail = scServiceDetailRepository
							.findFirstByUuidAndMstStatus_codeOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if (parentScServiceDetail != null) {
						ScComponentAttribute parentUuidCommissioningDate = scComponentAttributesRepository
								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
										parentScServiceDetail.getId(), "commissioningDate",
										AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
						if (parentUuidCommissioningDate != null
								&& parentUuidCommissioningDate.getAttributeValue() != null) {
							LOGGER.info("Parent Service Commissioning date is {}",
									parentUuidCommissioningDate.getAttributeValue());
							atMap.put("parentUuidCommissioningDate", parentUuidCommissioningDate.getAttributeValue());
						}
					}
				}

				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);

				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null
						&& poNumber.getAttributeValue() != null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");

			}
			processVar.put("processType", "computeProjectPLan");
			if (isValidLM && orderType.equalsIgnoreCase("NEW")) {
				if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
						|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
								&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))) {
					LOGGER.info("ConfirmOrder not required for Service id::{}", scServiceDetail.getId());
					processVar.put("confirmOrderRequired", false);
				}
				if (scServiceDetail.getIsAmended() != null
						&& scServiceDetail.getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
					processVar.put("confirmOrderRequired", true);

				}

				processVar.put("isMuxIPAvailable", false);
				processVar.put(IS_CONNECTED_SITE, true);
				processVar.put(IS_PORT_CHANGED, false);
				
				processVar.put("isCPEConfiguredByCustomer", true);
				processVar.put("isCPEArrangedByCustomer", true);
				processVar.put("cpeSiScope", "");
				Map<String, String> attrMap = new HashMap<>();
				attrMap.put("cpe_chassis_changed", "No");
				componentAndAttributeService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
				runtimeService.startProcessInstanceByKey("multi-vrf-salve-workflow", processVar);

			}

			autoMigration(scServiceDetail, scOrder, false);

		} catch (Exception e) {

			LOGGER.error("Error processing data", e);
			status = false;
			// trigger mail to o2c support
			Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);
			if (opScServiceDetail.isPresent()) {
				ScServiceDetail scServiceDetail = opScServiceDetail.get();
				List<String> toMailAddressList = new ArrayList<>(
						Arrays.asList("OPTIMUS-O2C-SUPPORT@tatacommunications.onmicrosoft.com"));
				try {
					notificationService.notifyMultiVrfSlaveTriggerFailure(null, toMailAddressList,
							StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()),
							StringUtils.trimToEmpty(scServiceDetail.getUuid()), StringUtils.trimToEmpty(scServiceDetail.getMasterVrfServiceCode()));
				} catch (TclCommonException e1) {
					LOGGER.error("Error sending slave trigger failure notification for service code :{} and error:{}", scServiceDetail.getUuid(),e1);
				}
			}
		}
		return status;
	}

	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public boolean processTerminationOffnetPoTrfExtensionWorkAround(
			ScServiceDetail scServiceDetail) throws TclCommonException {



		String terminationEffectiveDate = scServiceDetail.getTerminationEffectiveDate();
		LOGGER.info("Input processL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
				scServiceDetail.getId(), scServiceDetail.getUuid());
		
		scServiceDetail.setTerminationEffectiveDate(terminationEffectiveDate);

		Map<String, String> atMap = new HashMap<>();
		atMap.put("terminationEffectiveDate", terminationEffectiveDate);
		
		scServiceDetailRepository.save(scServiceDetail);
		
		saveTaskRemarksForTrfDateExtension(scServiceDetail, terminationEffectiveDate);
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "terminationEffectiveDate", AttributeConstants.COMPONENT_LM,
						AttributeConstants.SITETYPE_A);

		if(scComponentAttribute != null) {
			updateServiceLogs(scComponentAttribute.getAttributeValue(), terminationEffectiveDate, scServiceDetail.getId(), "TRFS Date");
		}
		
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
				AttributeConstants.COMPONENT_LM, "A");
		
		List<Task> tasks = taskRepository.findByServiceId(scServiceDetail.getId());
		boolean terminateoffnetBackhaulPoExist = false;
		boolean terminateoffnetPoExist = false;
		boolean terminateoffnetBackhaulPoExistB = false;
		boolean terminateoffnetPoExistB = false;
		for (Task task : tasks) {
			if (task.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_A)) {
				terminateoffnetBackhaulPoExist = true;
			} else if (task.getMstTaskDef().getKey().equals("terminate-offnet-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_A)) {
				terminateoffnetPoExist = true;
			}else if (task.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_B)) {
				terminateoffnetBackhaulPoExistB = true;
			} else if (task.getMstTaskDef().getKey().equals("terminate-offnet-po") && task.getSiteType().equals(AttributeConstants.SITETYPE_B)) {
				terminateoffnetPoExistB = true;
			}
		}
		if(terminateoffnetBackhaulPoExist || terminateoffnetPoExist || terminateoffnetPoExistB || terminateoffnetBackhaulPoExistB) {
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("ASP_INDIA");
			if (!mstTaskRegionList.isEmpty()) {
				List<String> toAddresses = mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList());
				notificationService.notifyTrfExtensionDate(toAddresses, null,
						scServiceDetail.getScOrder().getUuid(), scServiceDetail.getUuid(), terminationEffectiveDate);
			}
		}
		
		boolean isBackhaulOffnetExist = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-p`o-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_A)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));
		boolean isOffnetPoExist = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-po-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_A)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));

		boolean isBackhaulOffnetExistB = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-backhaul-po-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_B)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));
		boolean isOffnetPoExistB = tasks.stream()
				.anyMatch(t -> (t.getMstTaskDef().getKey().equals("terminate-offnet-po-trf-date-extension")
						&& t.getSiteType().equals(AttributeConstants.SITETYPE_B)
						&& t.getMstStatus().getCode().equals(TaskStatusConstants.OPENED)));

		if((isBackhaulOffnetExist && isOffnetPoExist && !scServiceDetail.getErfPrdCatalogProductName().equals("NPL"))
				|| (isBackhaulOffnetExist && isOffnetPoExist && isBackhaulOffnetExistB && isOffnetPoExistB) ) {
			
			return false;
		}
		
		Map<String, Object> processVar = new HashMap<>();

		if ((terminateoffnetBackhaulPoExist || terminateoffnetPoExist || terminateoffnetBackhaulPoExistB
				|| terminateoffnetPoExistB)) {
			processVar.put("trfsExtensionRequired", "Yes");
		}
		

		if (terminateoffnetBackhaulPoExist && !isBackhaulOffnetExist) {
			processVar.put("offnetHaulExtensionRequired", "Yes");
		}
		
		if (terminateoffnetBackhaulPoExistB && !isBackhaulOffnetExistB) {
			processVar.put("offnetHaulExtensionRequiredB", "Yes");
		}
		
		if (terminateoffnetPoExist && !isOffnetPoExist) {
			processVar.put("offnetPoExtensionRequired", "Yes");
		}
		
		if (terminateoffnetPoExistB && !isOffnetPoExistB) {
			processVar.put("offnetPoExtensionRequiredB", "Yes");
		}
		
		if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
			processVar.put("siteBType", AttributeConstants.SITETYPE_B);
		}
		
		processVar.put("getCLRSyncCallCompleted", false);
		processVar.put("getCLRSuccess", false);
		processVar.put("remainderCycle", reminderCycle);
		

		ScOrder scOrder = scServiceDetail.getScOrder();
		String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

		List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
		ScContractInfo scContractInfo = null;
		if (scContractInfos != null) {
			scContractInfo = scContractInfos.stream().findFirst().orElse(null);
		}

		if (scOrder.getErfCustCustomerName() != null) {
			processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
		}

		if (scOrder != null) {
			processVar.put(SC_ORDER_ID, scOrder.getId());
			processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
			processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
			if (scContractInfo != null) {
				processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
			}
			LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
			processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
			processVar.put("parentServiceCode", null);
			processVar.put("parentLmType", null);

			if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())) {
				processVar.put("orderSubCategory", scServiceDetail.getOrderSubCategory());
			} else {
				processVar.put("orderSubCategory", "NA");
			}
			processVar.put("parentServiceCode", scServiceDetail.getParentUuid());
			processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
			processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

			processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
			processVar.put(SERVICE_ID, scServiceDetail.getId());


			processVar.put("siteAType", "A");
			processVar.put("root_endDate", new Timestamp(new Date().getTime()));

			processVar.put("processType", "computeProjectPLan");

			processVar.put("ipBlockedResourceSuccess", false);
			processVar.put("terminationFlowTriggered", "Yes");
			processVar.put("remainderCycle", "R60/PT24H");
			processVar.put("terminationEffectiveDate", terminationEffectiveDate);
			if (!scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IAS")
					&& !scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
				processVar.put("offnetPoTerminationRequiredB", "No");
				processVar.put("offnetPoTerminationRequired", "No");
				processVar.put("offnetTerminationBackHaulRequiredB", "No");
				processVar.put("offnetTerminationBackHaulRequired", "No");
				processVar.put("offnetHaulExtensionRequired", "No");
				processVar.put("offnetHaulExtensionRequiredB", "No");
				processVar.put("offnetPoExtensionRequired", "No");
				processVar.put("offnetPoExtensionRequiredB", "No");

			}

			runtimeService.startProcessInstanceByKey("termination-offnet-trfsdate-extension-workflow", processVar);

		}
		return true;
	}

	@Transactional(readOnly=false)
	public Boolean processIZOPCL2ODataToFlowable(Integer serviceId,ScServiceDetail scServiceDetail, boolean welcomeMailTrigger) {
		Boolean status = true;
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);

				scServiceDetail = opScServiceDetail.get();
			}
			LOGGER.info("Input processIZOPCL2ODataToFlowable received for L2O serviceId:: {} service code:: {}",
					serviceId, scServiceDetail.getUuid());

			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			List<ScContractInfo> scContractInfos = scContractInfoRepository.findByScOrder_id(scOrder.getId());
			ScContractInfo scContractInfo = null;
			if (scContractInfos != null) {
				scContractInfo = scContractInfos.stream().findFirst().orElse(null);
			}
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_id(scServiceDetail.getId());
			LOGGER.info("Sc Service attributes fetched for {} ", scServiceDetail.getId());
			Map<String, String> serviceDetailsAttributes = new HashMap<>();
			for (ScServiceAttribute scServiceAttribute : serviceDetailsAttributesList) {
				LOGGER.info("ScServiceId {} :::: Attribute Name : {} ===> Attribute Value : {}",
						scServiceAttribute.getId(), scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
				serviceDetailsAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());
			}

			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);

			Map<String, Object> processVar = new HashMap<>();

			if (scOrder.getErfCustCustomerName() != null) {
				processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
			}
			if (scOrder != null) {
				LOGGER.info("scOrder.exists:: {}", scOrder.getId());
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				if (scContractInfo != null) {
					processVar.put(CONTRACT_START_DATE, scContractInfo.getContractStartDate());
				}
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				processVar.put("parentServiceCode", null);
				processVar.put("parentLmType", null);
				processVar.put("isParallelExists", "false");

				processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
				processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));

				if (scServiceDetail != null) {
					LOGGER.info("scServiceDetail.exists:: {}", scServiceDetail.getId());
					processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
					processVar.put(SERVICE_ID, scServiceDetail.getId());
					processVar.put(SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));

					processVar.put(CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
					processVar.put(STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
					processVar.put(COUNTRY,
							StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
					processVar.put(LOCAL_IT_CONTACT_EMAIL,
							StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
					processVar.put(LOCAL_IT_CONTACT_MOBILE,
							StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
					processVar.put(LOCAL_IT_CONTACT_NAME,
							StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
					processVar.put("parallelDownTime", "PT30M");
					processVar.put("isAccountRequired", false);

					Map<String, String> atMap = new HashMap<>();
					processVar.put("documentValidationRequired", true);
					processVar.put("e2eServiceTestingCompleted", false);
					processVar.put("createServiceCompleted", false);
					processVar.put("serviceConfigurationCompleted", false);
					processVar.put("ipTerminateConfigurationSuccess", false);
					processVar.put("failoverTestingRequired", false);
					processVar.put("additionalTechDetailsTaskCompleted", false);
					processVar.put("isValidDocuments", false);
					processVar.put("serviceDeliveryPlanReady", false);
					processVar.put("order_enrichment_complete", false);
					processVar.put("getIpServiceSyncCallCompleted", false);
					processVar.put("confirmOrderRequired", true);
					processVar.put("serviceConfigurationAction", "PE_PROV_CONFIG");
					processVar.put("serviceConfigurationMessageSent", false);
					processVar.put("serviceConfigurationAckSuccess", false);
					processVar.put("serviceConfigurationSuccess", false);
					processVar.put("previewIpConfigMessageSent", false);
					processVar.put("previewIpConfigAckSuccess", false);
					processVar.put("previewIpConfigSuccess", false);
					processVar.put("cancelIpConfigMessageSent", false);
					processVar.put("cancelIpConfigAckSuccess", false);
					processVar.put("cancelIpConfigSuccess", false);
					processVar.put("cpeConfigurationCompleted",true);
					processVar.put("remainderCycle", reminderCycle);
					processVar.put("deemedAcceptanceDuration", "PT48H");
					processVar.put("siteAType", "A");
					processVar.put("site_type", "A");
					processVar.put("root_endDate", new Timestamp(new Date().getTime()));
					processVar.put("cloudProvider",serviceDetailsAttributes.get("Cloud Provider"));
					atMap.put("supplierAddress",
							"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");

					ScOrderAttribute poDate = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
					ScOrderAttribute poNumber = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);

					if (poDate != null && poNumber != null && poDate.getAttributeValue() != null
							&& poNumber.getAttributeValue() != null) {
						atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
						atMap.put(LeAttributesConstants.PO_NUMBER,
								StringUtils.trimToEmpty(poNumber.getAttributeValue()));
					}
					componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
							AttributeConstants.COMPONENT_LM, "A");
					processVar.put("isBillable","Y");
					if(serviceDetailsAttributes.get("Cloud Provider").equalsIgnoreCase("IBM Direct Link")
						&& scServiceDetail.getPrimarySecondary().equalsIgnoreCase("Secondary")){
						processVar.put("isBillable","N");
					}
				}
				processVar.put("processType", "computeProjectPLan");
				processVar.put("O2CPROCESSKEY", "izoprivateconnect-service-fulfilment-handover-workflow");
				LOGGER.info(
						"flowable trigerred for  Service id::{} is izoprivateconnect-service-fulfilment-handover-workflow with processVar::{}",
						scServiceDetail.getUuid(),processVar);
				scServiceDetail.setWorkFlowName("izoprivateconnect-service-fulfilment-handover-workflow");
				runtimeService.startProcessInstanceByKey("izoprivateconnect-service-fulfilment-handover-workflow",
						processVar);
				if (welcomeMailTrigger) {
					sendNotificationToCustomer(scServiceDetail, scContractInfo, scComponentAttributesAMap);
				}
				autoMigration(scServiceDetail,scOrder,false);
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing processIZOPCL2ODataToFlowable data", e);
			status = false;
		}
		return status;
	}

	private void closeTask(ScServiceDetail serviceDetail, String taskDefKey, String remarks) throws TclCommonException {
		String userName = userInfoUtils.getUserInformation().getUserId();
		Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceDetail.getId(),
				taskDefKey);
		if (task != null && !task.getMstStatus().getCode()
				.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
			saveTaskRemarks(serviceDetail, remarks, userName);

			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
			task.setCompletedTime(new Timestamp(new Date().getTime()));
			if (task.getClaimTime() == null) {
				task.setClaimTime(new Timestamp(new Date().getTime()));

			}
			taskRepository.save(task);
			processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, "", "");
		}
	}

	private void saveTaskRemarks(ScServiceDetail scServiceDetail, String remarks, String userName) {
		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks(remarks);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}

	private void processTaskLogDetails(Task task, String action, String description, String actionTo) {
		LOGGER.info("Inside Process Task Log Details Method for task - {}  ", task.getMstTaskDef().getKey());
		ProcessTaskLog processTaskLog = createProcessTaskLog(task, action, description, actionTo, null);
		processTaskLogRepository.save(processTaskLog);
	}
	
	@Transactional(readOnly=false)
	public void processRenewalL2ODataToFlowable(ScServiceDetail renewalScServiceDetail) {
		LOGGER.info("processRenewalL2ODataToFlowable Method called for Service Id:{} and Service Code::{}",
				renewalScServiceDetail.getId(), renewalScServiceDetail.getUuid());
		try {
			ScOrder scOrder = renewalScServiceDetail.getScOrder();
			String orderType = OrderCategoryMapping.getOrderType(renewalScServiceDetail, scOrder);
			Map<String, Object> processVar = new HashMap<>();

			if (scOrder.getErfCustCustomerName() != null) {
				processVar.put("customerUserName", StringUtils.trimToEmpty(scOrder.getErfCustCustomerName()));
			}
			if (scOrder != null) {
				LOGGER.info("scOrder.exists:: {}", scOrder.getId());
				processVar.put(SC_ORDER_ID, scOrder.getId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(scOrder.getOpOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderType));
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", scOrder.getOrderStartDate());
				processVar.put(ORDER_CREATED_DATE, scOrder.getOrderStartDate());
				processVar.put("parentServiceCode", null);
				processVar.put("parentLmType", null);
				processVar.put("isParallelExists", "false");

				processVar.put(PRODUCT_NAME,
						StringUtils.trimToEmpty(renewalScServiceDetail.getErfPrdCatalogProductName()));
				processVar.put(OFFERING_NAME,
						StringUtils.trimToEmpty(renewalScServiceDetail.getErfPrdCatalogOfferingName()));

				if (renewalScServiceDetail != null) {
					LOGGER.info("scServiceDetail.exists:: {}", renewalScServiceDetail.getId());
					processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(renewalScServiceDetail.getUuid()));
					processVar.put(SERVICE_ID, renewalScServiceDetail.getId());
					processVar.put("parallelDownTime", "PT30M");
					processVar.put("isAccountRequired", false);
					processVar.put("documentValidationRequired", true);
					processVar.put("isValidDocuments", false);
					processVar.put("serviceDeliveryPlanReady", false);
					processVar.put("order_enrichment_complete", false);
					processVar.put("remainderCycle", reminderCycle);
					processVar.put("deemedAcceptanceDuration", "PT48H");
					processVar.put("siteAType", "A");
					processVar.put("site_type", "A");
					processVar.put("root_endDate", new Timestamp(new Date().getTime()));

				}
				processVar.put("processType", "computeProjectPLan");
				ScOrderAttribute commercialAttribute = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.IS_COMMERCIAL, scOrder);
				if(commercialAttribute.getAttributeValue().equalsIgnoreCase("Y")) {
					processVar.put("O2CPROCESSKEY", "renewal-commercial-fulfilment-handover-workflow");
					List<Integer> renewalServiceIds=scServiceDetailRepository.getIdByScOrderIdAndUuid(scOrder.getId(), scOrder.getUuid());
					processVar.put("renewalServiceIds", renewalServiceIds);
					processVar.put("renewalServiceIdsCount", renewalServiceIds.size());
					LOGGER.info("flowable trigerred for  Commercial Service id::{} isrenewal-commercial-fulfilment-handover-workflow with processVar::{}",
							renewalScServiceDetail.getUuid(), processVar);
					renewalScServiceDetail.setWorkFlowName("renewal-commercial-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("renewal-commercial-fulfilment-handover-workflow",
							processVar);
				}else {
					processVar.put("O2CPROCESSKEY", "renewal-non-commercial-fulfilment-handover-workflow");
					LOGGER.info("flowable trigerred for Non commercial Service id::{} is renewal-non-commercial-fulfilment-handover-workflow with processVar::{}",
							renewalScServiceDetail.getUuid(), processVar);
					renewalScServiceDetail.setWorkFlowName("renewal-non-commercial-fulfilment-handover-workflow");
					runtimeService.startProcessInstanceByKey("renewal-non-commercial-fulfilment-handover-workflow",
							processVar);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing processRenewalL2ODataToFlowable data", e);
		}
	}

	/**
	 * To process l20 data to flowable
	 * @param orderCode
	 * @return
	 */
	@Transactional
	public Boolean processManagedServicesL2ODataToFlowable(String orderCode) {
		Boolean status = true;
		try {
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(scOrder.getId());
			List<ScServiceDetail> managedServices = new ArrayList<>();
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(
						TeamsDROdrConstants.MICROSOFT_CLOUD_SOLUTIONS) &&
						scServiceDetail.getErfPrdCatalogFlavourName().contains(CommonConstants.PLAN)) {
					managedServices.add(scServiceDetail);
				}
			}
			for (ScServiceDetail serviceDetail : managedServices) {
				processManagedServices(serviceDetail.getId(), serviceDetail, null, null, null, null);
			}
		} catch (Exception e) {
			LOGGER.info("Error processing data : {} ", e);
			status = false;
		}
		return status;
	}

	/**
	 * ProcessManaged Services
	 *
	 * @param serviceId
	 * @param scServiceDetail
	 */
	public Boolean processManagedServices(Integer serviceId, ScServiceDetail scServiceDetail, String planDefinitionId,
			String planItem, Integer componentId, Integer flowGroupId) throws TclCommonException {
		Boolean status = true;
		try {
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);
				scServiceDetail = opScServiceDetail.get();
			}

			LOGGER.info(
					"Input processManagedServiceL2ODataToFlowable received for L2O serviceId :: {} service code:: {}",
					serviceId, scServiceDetail.getUuid());

			Map<String, String> scComponentAttributesAMap;

			if(Objects.nonNull(componentId)){
				scComponentAttributesAMap = commonFulfillmentUtils
						.getComponentAttributesByScComponent(componentId);
			}else{
				scComponentAttributesAMap = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			}

			LOGGER.info("scComponentAttributesAMap {} ", scComponentAttributesAMap);

			LOGGER.info("TeamsDR work flow to be triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			ServiceFulfillmentRequest serviceFulfillmentRequest = serviceCatalogueService
					.processTeamsDRServiceFulFillmentData(scServiceDetail.getId());
			LOGGER.info("TeamsDR work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());

			LOGGER.info("Input processTeamsDRL2ODataToFlowable(processManagedServices) received for L2O :: {}",
					serviceFulfillmentRequest);
			Map<String, Object> processVar = new HashMap<>();
			Map<String, String> atMap = new HashMap<>();
			if (serviceFulfillmentRequest.getOrderInfo() != null && serviceFulfillmentRequest
					.getPrimaryServiceInfo() != null) {

				if (serviceFulfillmentRequest.getCustomerInfo() != null) {
					processVar.put("customerUserName",
							StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCustomerName()));
					//processVar.put("customerEmail", StringUtils.trimToEmpty(serviceFulfillmentRequest.getCustomerInfo().getCusomerContactEmail()));
					processVar.put("customerEmail",
							StringUtils.trimToEmpty(serviceFulfillmentRequest.getOrderInfo().getOrderCreatedBy()));
				}
				ScContractInfo scContractInfo = scContractInfoRepository
						.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());

				if (CommonConstants.INDIA_SITES.equalsIgnoreCase(scContractInfo.getBillingCountry())) {
					processVar.put(BillingConstants.IS_INTL_BILLING, Boolean.FALSE);
					atMap.put(BillingConstants.IS_INTL_BILLING, CommonConstants.N);
				} else {
					processVar.put(BillingConstants.IS_INTL_BILLING, Boolean.TRUE);
					atMap.put(BillingConstants.IS_INTL_BILLING, CommonConstants.Y);
				}

				processVar.put(SP_LE_COUNTRY, scContractInfo.getBillingCountry());
				processVar.put(PRODUCT_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getProductName()));
				processVar.put(OFFERING_NAME, StringUtils.trimToEmpty(serviceFulfillmentRequest.getOfferingName()));

				OrderInfoBean orderInfoBean = serviceFulfillmentRequest.getOrderInfo();
				processVar.put(SC_ORDER_ID, orderInfoBean.getScOrderId());
				processVar.put(ORDER_CODE, StringUtils.trimToEmpty(orderInfoBean.getOptimusOrderCode()));
				processVar.put(ORDER_TYPE, StringUtils.trimToEmpty(orderInfoBean.getOrderType()));
				processVar.put(CONTRACT_START_DATE, orderInfoBean.getContractStartDate());
				LOGGER.info("orderInfoBean.getOrderCreatedDate():: {}", orderInfoBean.getOrderCreatedDate());
				processVar.put(ORDER_CREATED_DATE, orderInfoBean.getOrderCreatedDate());

				ServiceInfoBean primaryServiceInfo = serviceFulfillmentRequest.getPrimaryServiceInfo();
				processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(primaryServiceInfo.getServiceCode()));
				processVar.put(SERVICE_ID, primaryServiceInfo.getServiceId());

				processVar.put("remainderCycle", reminderCycle);

				String cpeType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("Media Gateway Purchase Type"));
				String cpeModel = StringUtils.trimToEmpty(scComponentAttributesAMap.get("Select your Media Gateway"));

				LOGGER.info("cpeType for serviceid :{} and service code:{} and cpe type:: {} cpeModel;{}", serviceId,
						scServiceDetail.getUuid(), cpeType, cpeModel);

				processVar.put("processType", "computeProjectPLan");
				processVar.put("root_endDate", new Timestamp(new Date().getTime()));
				List<ScTeamsDRServiceCommercial> scTeamsDRServiceCommercial = scTeamsDRServiceCommercialRepository
						.findByScServiceDetailIdAndComponentNameAndComponentTypeIn(scServiceDetail.getId(),
								TeamsDRFulfillmentConstants.MANAGEMENT_MONITORING,
								Arrays.asList(TeamsDRFulfillmentConstants.PLAN));
				if (Objects.nonNull(scTeamsDRServiceCommercial) && !scTeamsDRServiceCommercial.isEmpty())
					processVar.put(AttributeConstants.IS_MANAGEMENT_MONITORING_PRESENT, "yes");
				else
					processVar.put(AttributeConstants.IS_MANAGEMENT_MONITORING_PRESENT, "no");

				List<ScTeamsDRServiceCommercial> trainingCommercial = scTeamsDRServiceCommercialRepository
						.findByScServiceDetailIdAndComponentNameAndComponentTypeIn(scServiceDetail.getId(),
								TeamsDRFulfillmentConstants.TRAINING, Arrays.asList(TeamsDRFulfillmentConstants.PLAN));

				if (Objects.nonNull(scTeamsDRServiceCommercial) && !scTeamsDRServiceCommercial.isEmpty()) {
					atMap.put(AttributeConstants.IS_TRAINING_PRESENT, "yes");
					processVar.put(AttributeConstants.IS_TRAINING_PRESENT, "yes");
				} else {
					atMap.put(AttributeConstants.IS_TRAINING_PRESENT, "no");
					processVar.put(AttributeConstants.IS_TRAINING_PRESENT, "no");
				}

				ScOrderAttribute poDate = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE,
								scServiceDetail.getScOrder());
				ScOrderAttribute poNumber = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER,
								scServiceDetail.getScOrder());

				atMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				if (poDate != null && poNumber != null && poDate.getAttributeValue() != null && poNumber
						.getAttributeValue() != null) {
					atMap.put(LeAttributesConstants.PO_DATE, StringUtils.trimToEmpty(poDate.getAttributeValue()));
					atMap.put(LeAttributesConstants.PO_NUMBER, StringUtils.trimToEmpty(poNumber.getAttributeValue()));
				}
				if(Objects.isNull(componentId)){
					componentAndAttributeService
							.updateAttributes(serviceFulfillmentRequest.getPrimaryServiceInfo().getServiceId(), atMap,
									AttributeConstants.COMPONENT_LM, "A");
				}else{
					componentAndAttributeService
							.updateAttributesByScComponent(serviceFulfillmentRequest.getPrimaryServiceInfo().getServiceId(), atMap,
									componentId);
				}

				if(Objects.isNull(planDefinitionId) && Objects.isNull(planItem)){
					LOGGER.info("Triggering CMMN Workflow for managed services...");
					cmmnHelperService.createCaseInstance("bk_teamsdr_managed_services_workflow", processVar);
				}else{
					LOGGER.info("Triggering child plan items for managed services...");
					processVar.put(SC_COMPONENT_ID,componentId);
					processVar.put(SITE_TYPE,DR_SITE);
					if (Objects.nonNull(flowGroupId))
						processVar.put(GSC_FLOW_GROUP_ID, flowGroupId);
					cmmnHelperService.addPlanItemLocalVariablesRepeat(planItem, processVar, 0);
					cmmnHelperService.startPlanItem(planItem, null);
				}
			} else {
				LOGGER.info("invalid data in processManagedServices");
			}
		} catch (Exception e) {
			LOGGER.info("Error in process managed services flowable :{}", e.getMessage());
			status = false;
		}
		LOGGER.info("processManagedServicesL2ODataToFlowable completed");
		return status;
	}
	
	@Transactional(readOnly = false)
	public String getTerminationNotEligibeReason(OdrServiceDetailBean odrServiceDetailBean, boolean isTenPercent) {
		String reason = null;
		String terminationEffectiveDate = odrServiceDetailBean.getTerminationEffectiveDate();
		if (terminationEffectiveDate == null || DateUtil.convertStringToDateYYMMDD(terminationEffectiveDate) == null) {
			return "Termination effective date not valid";
		}
		if (terminationEffectiveDate != null
				&& DateUtil.convertStringToDateYYMMDD(terminationEffectiveDate).compareTo(new Date()) <= 0) {

			return "Termination date is lesser than or equal to current date";
		}

		Set<OdrComponentBean> odrComponentBeans = odrServiceDetailBean.getOdrComponentBeans();
		if(odrComponentBeans == null ||  odrComponentBeans.isEmpty()) {
			LOGGER.info("Termination not eligible validation for component attributes servicecode:{} and reason:{}", odrServiceDetailBean.getUuid(), "Mandatory attributes are empty");
			return "Mandatory attributes are empty";
		}
		
		if(odrServiceDetailBean.getErfPrdCatalogProductName() == null) {
			return "Erf Product Catalog name is Not there";
		}
		
		if(odrComponentBeans != null && !odrComponentBeans.isEmpty()) {
			LOGGER.info("Termination not eligible validation for component attributes servicecode:{}", odrServiceDetailBean.getUuid());
				boolean isSiteAExist = false;
				boolean isSiteBExist = false;
				for (OdrComponentBean odrComponentBean : odrComponentBeans) {
					if(odrComponentBean.getSiteType() != null && odrComponentBean.getSiteType().equalsIgnoreCase(AttributeConstants.SITETYPE_A)) {
						isSiteAExist = true;
					}
					if(odrComponentBean.getSiteType() != null && odrComponentBean.getSiteType().equalsIgnoreCase(AttributeConstants.SITETYPE_B)) {
						isSiteBExist = true;
					}
				}
				if(!isSiteAExist) {
					LOGGER.info("Termination not eligible validation for component attributes servicecode:{} and reason:{}", odrServiceDetailBean.getUuid(), "Site A componenet is not exisit");
					return "Site A component doesn't exist";
				}
				if ((odrServiceDetailBean.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")
						|| odrServiceDetailBean.getErfPrdCatalogProductName().equalsIgnoreCase("NDE")) && !isSiteBExist) {
					LOGGER.info("Termination not eligible validation for component attributes servicecode:{} and reason:{}", odrServiceDetailBean.getUuid(), "Site B componenet is not exisit");
					return "Site B component doesn't exist";
				}
				
			reason =  validateMandatoryAttributes(odrComponentBeans, odrServiceDetailBean.getUuid());
			if(reason != null) {
				return reason;
			}
		}
		

		if(!isTenPercent) {
			List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository
					.findByUuidAndMstStatus_codeAndIsMigratedOrder(odrServiceDetailBean.getUuid(), "ACTIVE", "N");
			if(scServiceDetailList == null || scServiceDetailList.isEmpty()) {
				return "Service ID not belongs to O2C";
			}
		}

		return reason;
	}
	
	private String getNotEligibleReason(String attributeName, String siteType) {
		String reason = null;
		switch (attributeName) {
		case "interface":
			reason = "Interface Value is Not there for "+ siteType;
			break;
		case "siteAddress":
			reason = "Site address value is not there for "+ siteType;
			break;
		case "destinationCountry":
			reason = "Destination country value is not there for "+ siteType;
			break;

		default:
			break;
		}
		return reason;
	}
	
	private String validateMandatoryAttributes(Set<OdrComponentBean> odrComponentBeans, String serviceCode) {
		List<String> mandatoryAttributesSiteA = Arrays.asList("interface", "siteAddress", "destinationCountry");
		List<String> mandatoryAttributesSiteB = Arrays.asList("interface", "siteAddress", "destinationCountry");
		LOGGER.info("Inside Termination not eligible validation for attributes servicecode:{}", serviceCode);

		for (OdrComponentBean odrComponentBean : odrComponentBeans) {
			String reason = null;
			if (odrComponentBean.getSiteType() != null
					&& odrComponentBean.getSiteType().equals(AttributeConstants.SITETYPE_A)
					&& odrComponentBean.getOdrComponentAttributeBeans() != null) {
				LOGGER.info("Inside Site A Termination not eligible validation for attributes servicecode:{}", serviceCode);
				 reason = getNotEligibleReason(mandatoryAttributesSiteA, odrComponentBean,
						AttributeConstants.SITETYPE_A);
				 LOGGER.info("Inside Site A Termination not eligible validation for attributes servicecode:{} and reason:{}", serviceCode, reason);
			} else if (odrComponentBean.getSiteType() != null
					&& odrComponentBean.getSiteType().equals(AttributeConstants.SITETYPE_B)
					&& odrComponentBean.getOdrComponentAttributeBeans() != null) {
				LOGGER.info("Inside Site B Termination not eligible validation for attributes servicecode:{}", serviceCode);
				 reason = getNotEligibleReason(mandatoryAttributesSiteB, odrComponentBean,
						AttributeConstants.SITETYPE_B);
				 LOGGER.info("Inside Site B Termination not eligible validation for attributes servicecode:{} and reason:{}", serviceCode, reason);
			}
			if (reason != null) {
				return reason;
			}
		}
		return null;
	}





	private String getNotEligibleReason(List<String> mandatoryAttributes, OdrComponentBean odrComponentBean, String siteType) {
		for (String attName : mandatoryAttributes) {
			Optional<OdrComponentAttributeBean> odrOptional = odrComponentBean.getOdrComponentAttributeBeans().stream()
					.filter(comp -> comp.getName().equals(attName)).findFirst();
			if(odrOptional.isPresent()) {
				if( odrOptional.get().getValue() == null ||  odrOptional.get().getValue().trim().isEmpty()) {
					return  getNotEligibleReason(attName, siteType);
				}
			}else {
				return  getNotEligibleReason(attName, siteType);
			}
		}
		return null;
	}
	
	@Transactional(readOnly = false)
	public void sendTerminationNotEligibleNotification(
			List<TerminationNotEligibleNoticationBean> terminationNotEligibleNoticationBeans, String orderCode, String terminationType)
			throws TclCommonException {
			LOGGER.info("Notify Terimination not eligible for this order: {} ", orderCode);

			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("TERMINATION_NON_ELIGIBLE_ORDER");
			if (!mstTaskRegionList.isEmpty()) {
				List<String> toAddresses = mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList());
				if (toAddresses != null && !toAddresses.isEmpty()) {
					notificationService.notifyTerminationNotEligible(toAddresses, null,
							terminationNotEligibleNoticationBeans, null, orderCode, terminationType);
				}

		}
	}


}