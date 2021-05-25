
package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.ParentPoBean;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.AutoPoResponse;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.utils.Currencies;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.ForexService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.MrnCreationEntity;
import com.tcl.dias.servicefulfillment.entity.entities.MrnSequenceNumber;
import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.MstStateToDistributionCenterMapping;
import com.tcl.dias.servicefulfillment.entity.entities.MstVmi;
import com.tcl.dias.servicefulfillment.entity.entities.ProCreation;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SapPoDtlOptimus;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SfdcVendorC;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.entities.VwBomMaterialDetail;
import com.tcl.dias.servicefulfillment.entity.entities.VwMuxBomMaterialDetail;
import com.tcl.dias.servicefulfillment.entity.repository.AuditLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MrnCreationRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MrnSequenceNumberRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstBudgetMatrixRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstStateToDistributionCenterMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstVmiRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProCreationRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.Stg0SapPoDtlOptimusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StgSfdcVendorRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VwBomMaterialDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VwBomMuxDetailsRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMaterialAvailabilityBean;
import com.tcl.dias.servicefulfillmentutils.beans.SerialNumberBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPRRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPRResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoHeader;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoLineItems;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoMsgs;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoServiceItems;
import com.tcl.dias.servicefulfillmentutils.beans.sap.DisplayMaterialRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.GrnDetails;
import com.tcl.dias.servicefulfillmentutils.beans.sap.GrnResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.GrnResponses;
import com.tcl.dias.servicefulfillmentutils.beans.sap.GrnSapHanaResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MaterialTransfer;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MinResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MinResponseBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MrnCreation;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MrnPlaceOrderRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MrnTrasferRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PRHeader;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PRLine;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PoStatusBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PoStatusResponseBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.SapQuantityCheckRequest;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.sap.bean.CableRequest;
import com.tcl.dias.servicefulfillmentutils.sap.bean.DisplayMaterial;
import com.tcl.dias.servicefulfillmentutils.sap.cable.response.CableDisplayMaterial;
import com.tcl.dias.servicefulfillmentutils.sap.cable.response.CableMaterialResponse;
import com.tcl.dias.servicefulfillmentutils.sap.response.DisplayMaterialResponse;
import com.tcl.dias.servicefulfillmentutils.sap.response.MrnCreationReponse;
import com.tcl.dias.servicefulfillmentutils.sap.response.MrnTrasferResponse;
import com.tcl.dias.servicefulfillmentutils.sap.response.SapQuantityAvailableRequest;
import com.tcl.dias.servicefulfillmentutils.sap.response.SapQuantityResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional(readOnly = false)
public class SapService extends ServiceFulfillmentBaseService{

	@Autowired
	RestClientService restClientService;

	@Value("${sap.quantity.detail.url}")
	String sapQuantityDetailsUrl;
	
	@Value("${sap.calbe.quantity.detail.url}")
	String sapCableQuantityDetailsUrl;

	@Value("${sap.mrn.transfer.url}")
	String sapTransferUrl;

	@Value("${sap.mrn.place.order.url}")
	String mrnMuxCreationUrl;

	@Value("${sap.autopr.url}")
	String sapAutoPrUrl;
	
	@Value("${sap.autopo.url}")
	String sapAutoPoUrl;

	@Value("${sap.user.name}")
	String userName;

	@Value("${sap.password}")
	String password;

	@Autowired
	VwBomMaterialDetailRepository vwBomMaterialDetailRepository;

	@Autowired
	VwBomMuxDetailsRepository vwBomMuxDetailsRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	MstStateToDistributionCenterMappingRepository mstStateToDistributionCenterMappingRepository;

	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;

	@Autowired
	MstBudgetMatrixRepository mstBudgetMatrixRepository;

	@Autowired
	ForexService forexService;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Value("${rabbitmq.cpe.bom.rentalsales.queue}")
	String rentalSalesQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	MstVmiRepository mstVmiRepository;
	
	@Autowired
	AuditLogRepository auditLogRepositoryRepository;

	@Autowired
	ProCreationRepository proCreationRepository;

	@Autowired
	MrnCreationRepository mrnCreationRepository;

	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskPlanRepository taskPlanRepository;
	
	@Autowired
	MrnSequenceNumberRepository mrnSequenceNumberRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Value("${application.env}")
	String appEnv;
	 
	@Autowired
	protected TaskCacheService taskCacheService;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	Stg0SapPoDtlOptimusRepository stg0SapPoDtlOptimusRepository;

	@Autowired
	StgSfdcVendorRepository stgSfdcVendorRepository;
	
	  @Autowired
	  FlowableBaseService flowableBaseService;
	
	DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat formatterCdate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat formatterPo = new SimpleDateFormat("yyyyMMdd");
	

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SapService.class);

	public boolean checMaterialQuantityAndPlaceMrn(String serviceCode, Integer serviceId, String type, String invType,
			String typeOfExpenses, DelegateExecution execution) throws TclCommonException, ParseException {
		SapQuantityResponse sapQuantityResponse = null;
		boolean quantityAvailable = false;
		
		boolean checkForRentalAlso=false;
		boolean isRentalAvailable=false;

		Map<String, String> hardWarematerialQuantityMapping = new HashMap<>();

		Map<String, String> cableMaterialQuantityMapping = new HashMap<>();

		Map<String, String> licencedMaterialQuantityMapping = new HashMap<>();

		Map<String, List<DisplayMaterialResponse>> availableQuantityMap = new HashMap<>();
		Map<String, List<CableDisplayMaterial>> cableAvailableQuantityMap = new HashMap<>();
		

		String purchangeGroup = null;
		if (type.equalsIgnoreCase("CPE")) {
			purchangeGroup = "C15";
		} else {
			purchangeGroup = "C01";
		}
		String cpeType = null;

		if (invType.toLowerCase().contains("outright")) {
			cpeType = "outright";

		} else if (invType.toLowerCase().contains("rental")) {
			cpeType = "rental";
		}
		String level5Wbs=null;


		try {

			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
			
			Map<String, String> scComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(
							scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			ScComponentAttribute wbScComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "level5Wbs", "LM", "A");
			if (wbScComponentAttribute != null) {
				level5Wbs = wbScComponentAttribute.getAttributeValue();
			}

			if (scServiceDetail != null) {

				List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
						.findByState(scComponentAttributesAMap.get("destinationState"));

				LOGGER.info(
						"checMaterialQuantityAndPlaceMrn distributionCenterMapping with service code:{} and state:{} and distributionCenterMapping :{}",
						serviceCode, scComponentAttributesAMap.get("destinationState"), distributionCenterMapping);
				SapQuantityCheckRequest checkRequest = null;

				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping
						.stream().findFirst().orElse(null);
				checkRequest = constructQuantityRequest(scServiceDetail, type, hardWarematerialQuantityMapping,
						mstStateToDistributionCenterMapping, invType, licencedMaterialQuantityMapping);
				AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(checkRequest), null,
						serviceCode, type + "CHECKINVENTORY", execution.getProcessInstanceId());

				LOGGER.info("SapQuantityCheckRequest request  for service code:{} and request:{}", serviceCode,
						Utils.convertObjectToJson(checkRequest));

				if (checkRequest == null || checkRequest.getDisplayMaterial().isEmpty()) {
					return false;
				}

				RestResponse response = restClientService.postWithBasicAuthentication(sapQuantityDetailsUrl,
						Utils.convertObjectToJson(checkRequest), createCommonHeader(), userName, password);
				inventory.setResponse(response.getData());
				auditLogRepositoryRepository.saveAndFlush(inventory);

				LOGGER.info("sap response for service code:{} and status{}:sap response data{}:error respone {}",
						serviceCode, response.getStatus(), response.getData(), response.getErrorMessage());
				if (response != null && response.getStatus() == Status.SUCCESS) {

					sapQuantityResponse = (SapQuantityResponse) Utils.convertJsonToObject(response.getData(),
							SapQuantityResponse.class);

					LOGGER.info(
							"sapQuantityResponse data for service code:{} and :{}:material map:{} and licencedMaterialQuantityMapping:{}",
							serviceCode, response.getData(), hardWarematerialQuantityMapping,
							licencedMaterialQuantityMapping);

					quantityAvailable = isQuantityAvailable(hardWarematerialQuantityMapping,
							licencedMaterialQuantityMapping, sapQuantityResponse, type, availableQuantityMap,

							execution, scServiceDetail);
					LOGGER.info("quantityAvailable for serviceCode:{} and quantityAvailable :{}", serviceCode,
							quantityAvailable);
					SapQuantityAvailableRequest sapQuantityAvailableRequest = new SapQuantityAvailableRequest();


					if (quantityAvailable) {
						LOGGER.info("sap quantity response for service code:{} and hardWarematerialQuantityMapping :{}and availableQuantityMap:{}", serviceCode,
								hardWarematerialQuantityMapping,availableQuantityMap);

						for (Map.Entry<String, String> entry : hardWarematerialQuantityMapping.entrySet()) {

							if (availableQuantityMap.containsKey(entry.getKey())) {
								List<DisplayMaterialResponse> displayMaterialResponses = availableQuantityMap
										.get(entry.getKey());

								List<DisplayMaterialResponse> filteResponse = displayMaterialResponses.stream()
										.limit(Integer.valueOf(entry.getValue())).collect(Collectors.toList());
								sapQuantityAvailableRequest.getDisplayMaterial().addAll(filteResponse);

							}

						}
						
						LOGGER.info("sap quantity response for service code:{} and licencedMaterialQuantityMapping :{}and availableQuantityMap:{}", serviceCode,
								licencedMaterialQuantityMapping,availableQuantityMap);

						for (Map.Entry<String, String> entry : licencedMaterialQuantityMapping.entrySet()) {

							if (availableQuantityMap.containsKey(entry.getKey())) {
								List<DisplayMaterialResponse> displayMaterialResponses = availableQuantityMap
										.get(entry.getKey());

								List<DisplayMaterialResponse> filteResponse = displayMaterialResponses.stream()
										.limit(Integer.valueOf(entry.getValue())).collect(Collectors.toList());
								sapQuantityAvailableRequest.getDisplayMaterial().addAll(filteResponse);

							}

						}

						LOGGER.info("sap quantity response for service code:{} and response :{}", serviceCode,
								Utils.convertObjectToJson(sapQuantityAvailableRequest));

					}
					if (licencedMaterialQuantityMapping.isEmpty()) {
						execution.setVariable("cpeLicenseNeeded", false);

					} else {

						execution.setVariable("cpeLicenseNeeded", true);

					}

					if (quantityAvailable && cpeType.equalsIgnoreCase("rental")) {
						CpeBomResource cpeBomResource = getCpeBomDetails(scServiceDetail);
						if (cpeBomResource != null) {

							List<MstCostCatalogue> mstCostCatalogues = mstCostCatalogueRepository
									.findByDistinctBundledBomForRentalWithCable(cpeBomResource.getBomName());
							CableRequest cableRequest = new CableRequest();


							if (mstCostCatalogues != null && !mstCostCatalogues.isEmpty()) {
								checkForRentalAlso=true;

								for (MstCostCatalogue mstCostCatalogue : mstCostCatalogues) {
									DisplayMaterial displayMaterial = new DisplayMaterial();

									displayMaterial.setBMBundle("");

									displayMaterial.setMaterialCode(mstCostCatalogue.getRentalMaterialCode());

									displayMaterial.setPlant(mstStateToDistributionCenterMapping
											.getMasterTclDistributionCenter().getPlant());
									displayMaterial
											.setStorageLocation(String.valueOf(mstStateToDistributionCenterMapping
													.getMasterTclDistributionCenter().getSapStorageLocation()));
									displayMaterial.setWBSNumber(level5Wbs);

									cableRequest.getDisplayMaterial().add(displayMaterial);
									cableMaterialQuantityMapping.put(mstCostCatalogue.getRentalMaterialCode(),
											String.valueOf(mstCostCatalogue.getQuantity()));

								}
							}
							
							if(!cableRequest.getDisplayMaterial().isEmpty()) {
								checkForRentalAlso=true;
							}
							
							LOGGER.info("sap check inventory rental quantity request:{}",
									Utils.convertObjectToJson(cableRequest));
							AuditLog rentalInv = saveDataIntoNetworkInventory(Utils.convertObjectToJson(cableRequest), null,
									serviceCode, type + "CHECKINVENTORYRENTAL", execution.getProcessInstanceId());

							RestResponse cableResponse = restClientService.postWithBasicAuthentication(
									sapCableQuantityDetailsUrl, Utils.convertObjectToJson(cableRequest),
									createCommonHeader(), userName, password);
							
							rentalInv.setResponse(cableResponse.getData());
							auditLogRepositoryRepository.saveAndFlush(rentalInv);
							
						

							LOGGER.info(
									"sap response for cable responseservice code:{} and status{}:sap response data{}:error respone {}",
									serviceCode, cableResponse.getStatus(), cableResponse.getData(),
									cableResponse.getErrorMessage());

							if (cableResponse != null && cableResponse.getStatus() == Status.SUCCESS) {
								
								LOGGER.info("sap check inventory service code:{}CableMaterialResponse:{}",serviceCode,cableResponse.getData());


								CableMaterialResponse cableQuantityResponse = (CableMaterialResponse) Utils
										.convertJsonToObject(cableResponse.getData(), CableMaterialResponse.class);

								 isRentalAvailable = getCpeRentalQuantityAvailable(cableQuantityResponse,
										cableAvailableQuantityMap, cableMaterialQuantityMapping,level5Wbs,serviceCode);
								 
									LOGGER.info("sap check inventory service code:{}and is Rental:{}",serviceCode,isRentalAvailable);

								 
									LOGGER.info("sap check inventory service code:{}cableAvailableQuantityMap:{} and cableMaterialQuantityMapping:{}",serviceCode,cableAvailableQuantityMap,cableMaterialQuantityMapping);


							}

						}
					}
					
					LOGGER.info("sap check inventory service code:{} checkForRentalAlso:{} for rental request and isRentalAvailable:{}",serviceCode,
							checkForRentalAlso,isRentalAvailable);
					

					
					
					if (checkForRentalAlso && isRentalAvailable) {
						
						LOGGER.info("sap check inventory and servicecode:{}sapQuantityAvailableRequest:{} and cableAvailableQuantityMap:{} and cableMaterialQuantityMappingUtils:{} ",serviceCode,cableAvailableQuantityMap,cableMaterialQuantityMapping,Utils.convertObjectToJson(sapQuantityAvailableRequest));


						for (Map.Entry<String, String> entry : cableMaterialQuantityMapping.entrySet()) {

							if (cableAvailableQuantityMap.containsKey(entry.getKey())) {
								List<CableDisplayMaterial> displayMaterialResponses = cableAvailableQuantityMap
										.get(entry.getKey());

								List<CableDisplayMaterial> filteResponse = displayMaterialResponses.stream()
										.limit(Integer.valueOf(entry.getValue())).collect(Collectors.toList());
								
								LOGGER.info("sap check inventory servicecode:{} and sapQuantityAvailableRequest:{} and filteResponse:{} ",serviceCode,Utils.convertObjectToJson(sapQuantityAvailableRequest),filteResponse.size());

								sapQuantityAvailableRequest.getDisplayMaterial()
										.addAll(convertToCpeChangesResponse(filteResponse));

							}

						}

						quantityAvailable = true;
					}
					
					if(checkForRentalAlso && !isRentalAvailable) {
						quantityAvailable=false;
					}
					
					
					LOGGER.info("sap check inventory sapQuantityAvailableRequest:{} ",Utils.convertObjectToJson(sapQuantityAvailableRequest));
					execution.setVariable("SAPQUANTITYRESPONE" + serviceCode,
							Utils.convertObjectToJson(sapQuantityAvailableRequest));

					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
							"SAPINVENTORYCHECK" + type.toUpperCase(),
							Utils.convertObjectToJson(sapQuantityAvailableRequest));
					

				}

			}
		} catch (Exception e) {
			
			LOGGER.error("checMaterialQuantityAndPlaceMrn with service code:{} and error:{}",serviceCode,e);
			return false;
		}
		return quantityAvailable;

	}
			
			

	private List<DisplayMaterialResponse> convertToCpeChangesResponse(
			List<CableDisplayMaterial> filteResponse) {
		List<DisplayMaterialResponse> displayMaterialResponses=new ArrayList<>();
		for (CableDisplayMaterial cableDisplayMaterial : filteResponse) {
			DisplayMaterialResponse displayMaterialResponse = new DisplayMaterialResponse();
			displayMaterialResponse.setAddressLine1(cableDisplayMaterial.getAddressLine1());
			displayMaterialResponse.setAddressLine2(cableDisplayMaterial.getAddressLine2());
			displayMaterialResponse.setBMBundle(cableDisplayMaterial.getBMBundle());
			displayMaterialResponse.setCategoryOfInventory(cableDisplayMaterial.getCategoryOfInventory());
			displayMaterialResponse.setCity(cableDisplayMaterial.getCity());
			displayMaterialResponse.setCountry(cableDisplayMaterial.getCountry());
			displayMaterialResponse.setItemCategory(cableDisplayMaterial.getItemCategory());
			displayMaterialResponse.setMaterialCode(cableDisplayMaterial.getMaterialCode());
			displayMaterialResponse.setMaterialDescription(cableDisplayMaterial.getMaterialDescription());
			displayMaterialResponse.setMaterialGroup(cableDisplayMaterial.getMaterialGroup());
			displayMaterialResponse.setPlant(cableDisplayMaterial.getPlant());
			displayMaterialResponse.setPlantName(cableDisplayMaterial.getPlantName());
			displayMaterialResponse.setQuantityAvailable("1");
			displayMaterialResponse.setRemark(cableDisplayMaterial.getRemark());
			displayMaterialResponse.setState(cableDisplayMaterial.getState());
			displayMaterialResponse.setStatus(cableDisplayMaterial.getStatus());
			displayMaterialResponse.setStorageLocation(cableDisplayMaterial.getStorageLocation());
			displayMaterialResponse.setStorageLocationDescription(cableDisplayMaterial.getStorageLocationDescription());
			displayMaterialResponse.setUnitOfMeasure(cableDisplayMaterial.getUnitOfMeasure());
			displayMaterialResponse.setWBSNumber(cableDisplayMaterial.getWBSNumber());
			displayMaterialResponse.setZipCode(cableDisplayMaterial.getZipcode());
			displayMaterialResponses.add(displayMaterialResponse);
		}
		
		return displayMaterialResponses;
	}


	public CpeBomResource getCpeBomDetails(ScServiceDetail scServiceDetail) throws TclCommonException {

		List<ScServiceAttribute> cpeChassisAttr = scServiceDetail.getScServiceAttributes().stream()
				.filter(attr -> attr.getAttributeName().equalsIgnoreCase("CPE Basic Chassis"))
				.collect(Collectors.toList());

		LOGGER.info("constructQuantityRequest cpeChassisAttr for servicecode:{} and cpeChassisAttr is:{}",
				scServiceDetail.getUuid(), cpeChassisAttr);

		ScServiceAttribute scServiceAttribute = cpeChassisAttr.stream().findAny().orElse(null);

		LOGGER.info("constructQuantityRequest scServiceAttribute for servicecode:{} and cpeChassisAttr is:{}",
				scServiceDetail.getUuid(), scServiceAttribute);

		String serviceParamId = scServiceAttribute.getAttributeValue();

		LOGGER.info("constructQuantityRequest serviceParamId for servicecode:{} and cpeChassisAttr is:{}",
				scServiceDetail.getUuid(), serviceParamId);

		if (StringUtils.isNotBlank(serviceParamId)) {
			Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
					.findById(Integer.valueOf(serviceParamId));
			if (scAdditionalServiceParam.isPresent()) {
				String bomResponse = scAdditionalServiceParam.get().getValue();

				LOGGER.info("constructQuantityRequest bomResponse for servicecode:{} and bomResponse is:{}",
						scServiceDetail.getUuid(), bomResponse);

				CpeBomResource[] bomResourcess = Utils.convertJsonToObject(bomResponse, CpeBomResource[].class);
				return bomResourcess[0];

			}
		}

		return null;

	}
	
	public AuditLog saveDataIntoNetworkInventory(String request, String response, String serviceCode, String type,
			String requestId) {

		AuditLog networkInventory = new AuditLog();
		networkInventory.setRequest(request);
		networkInventory.setResponse(response);
		networkInventory.setType(type);
		networkInventory.setServiceCode(serviceCode);
		networkInventory.setRequestId(requestId);
		networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
		return auditLogRepositoryRepository.saveAndFlush(networkInventory);

	}

	public boolean placeWbsTransfer(SapQuantityAvailableRequest sapQuantityResponse, String serviceCode, String ivType,
			String typeOfExpense, ScServiceDetail scServiceDetail, String processInstanceId, String type) throws TclCommonException {

		try {

			
			Map<String, String> scComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(
							scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
					.findByState(scComponentAttributesAMap.get("destinationState"));
			MrnTrasferRequest request = null;
			MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping.stream()
					.findFirst().orElse(null);
			request = constructMaterialTransferRequest(sapQuantityResponse, scServiceDetail,
					mstStateToDistributionCenterMapping, ivType, typeOfExpense);
			AuditLog inventory=	saveDataIntoNetworkInventory(Utils.convertObjectToJson(request), null, serviceCode, type+"WBSTRANSFER",processInstanceId);


			
			
			if(request==null || request.getMaterialTransfer().isEmpty()) {
				return true;
			}
				
				LOGGER.info("placeMrsTransfer request for service code:{} and request:{}", serviceCode,
						Utils.convertObjectToJson(request));

			RestResponse response = restClientService.postWithBasicAuthentication(sapTransferUrl,
					Utils.convertObjectToJson(request), createCommonHeader(), userName, password);
			inventory.setResponse(response.getData());
			auditLogRepositoryRepository.saveAndFlush(inventory);

			LOGGER.info("placeMrsTransfer for service code:{} and  response:{}", serviceCode,
					Utils.convertObjectToJson(response));

			if (response != null && response.getStatus() == Status.SUCCESS) {
				MrnTrasferResponse mrnTrasferResponse = Utils.convertJsonToObject(response.getData(),
						MrnTrasferResponse.class);

				if (mrnTrasferResponse != null && !mrnTrasferResponse.getMaterialTransfer().isEmpty()) {
					for (com.tcl.dias.servicefulfillmentutils.sap.response.MaterialTransfer transfer : mrnTrasferResponse
							.getMaterialTransfer()) {
						if (transfer.getStatus().equalsIgnoreCase("Not Transferred")) {
							return false;
						}
					}
				} else {
					return false;
				}

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	private MrnTrasferRequest constructMaterialTransferRequest(SapQuantityAvailableRequest sapQuantityResponse,
			ScServiceDetail scServiceDetail, MstStateToDistributionCenterMapping distributionCenterMapping,
			String invType, String typeOfExpenses) {
		if (sapQuantityResponse != null && sapQuantityResponse.getDisplayMaterial() != null
				&& !sapQuantityResponse.getDisplayMaterial().isEmpty()) {
			MrnTrasferRequest mrnTrasferRequest = new MrnTrasferRequest();
			String cpeType = null;
			if (invType.toLowerCase().contains("outright")) {
				cpeType = "Outright";
			} else {
				cpeType = "Rental";
			}
			
			ScComponentAttribute attribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "level5Wbs", "LM", "A");
			LOGGER.info(
					"constructMaterialTransferRequest  for service code:{} and  level5Wbs:{} and cpetype is:{}",
					scServiceDetail.getUuid(), attribute, cpeType);
		

			String sapCode=DateUtil.generateUidForSap(6, cpeType);

			for (DisplayMaterialResponse displayMaterialResponse : sapQuantityResponse.getDisplayMaterial()) {
				if (!attribute.getAttributeValue().equalsIgnoreCase(displayMaterialResponse.getWBSNumber())) {
					MaterialTransfer materialTransfer = new MaterialTransfer();
					materialTransfer.setOptimusId(sapCode);
					materialTransfer.setDocumentDate(DateUtil.convertDateWithoutHypen(new Date()));
					materialTransfer.setMaterialCode(displayMaterialResponse.getMaterialCode());
					materialTransfer.setPlant(displayMaterialResponse.getPlant());
					materialTransfer.setPostingDate(DateUtil.convertDateWithoutHypen(new Date()));
					materialTransfer.setQuantity(displayMaterialResponse.getQuantityAvailable());
					materialTransfer.setRecvgStorageLocation(displayMaterialResponse.getStorageLocation());
					if (attribute != null) {
						materialTransfer.setRecvgWBSElement(attribute.getAttributeValue());
					}
					materialTransfer.setStorageLocation(displayMaterialResponse.getStorageLocation());
					materialTransfer.setSerialNumber(displayMaterialResponse.getSAPSerialNumber());
					materialTransfer.setUnitOfEntry(displayMaterialResponse.getUnitOfMeasure());
					materialTransfer.setWBSElement(displayMaterialResponse.getWBSNumber());

					mrnTrasferRequest.getMaterialTransfer().add(materialTransfer);
				}

			}

			return mrnTrasferRequest;
		}
		return null;

	}

	public boolean placeMrnOrder(String serviceCode, String type,
			String invType, String typeOfExpenses, ScServiceDetail scServiceDetail, String processInstanceId,
			String taskName,String siteType) throws TclCommonException {

		MrnCreationReponse mrnCreationReponse = null;
		MrnPlaceOrderRequest mrnPlaceOrderRequest = new MrnPlaceOrderRequest();
		
		
		Map<String, String> scComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(
						scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

		List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
				.findByState(scComponentAttributesAMap.get("destinationState"));

		MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping.stream()
				.findFirst().orElse(null);
		constructMrnCreationRequest(scServiceDetail, mrnPlaceOrderRequest, type, mstStateToDistributionCenterMapping,
				invType, typeOfExpenses,scComponentAttributesAMap);
		AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(mrnPlaceOrderRequest), null,
				serviceCode, type + "PLACEMRN", processInstanceId);

		LOGGER.info("mrnPlaceOrderRequest data for service code:{} and mrnPlaceOrderRequest :{}", serviceCode,
				mrnPlaceOrderRequest);

		RestResponse response = restClientService.postWithBasicAuthentication(mrnMuxCreationUrl,
				Utils.convertObjectToJson(mrnPlaceOrderRequest), createCommonHeader(), userName, password);
		inventory.setResponse(response.getData());
		auditLogRepositoryRepository.saveAndFlush(inventory);

		LOGGER.info("mrnPlaceOrders response status{}:sap response data{}:error respone {}", response.getStatus(),
				response.getData(), response.getErrorMessage());

		if (response.getStatus() == Status.SUCCESS) {
			mrnCreationReponse = Utils.convertJsonToObject(response.getData(), MrnCreationReponse.class);
			LOGGER.info("mrnCreationReponse data  for service code:{} and mrnCreationReponse:{}", serviceCode,
					mrnCreationReponse);

			if (mrnCreationReponse != null &&( mrnCreationReponse.getMRNCREATION().getStatus().equalsIgnoreCase("Sucess")
					|| mrnCreationReponse.getMRNCREATION().getStatus().equalsIgnoreCase("Success"))) {

				Map<String, String> mapper = new HashMap<>();
				if (type.equalsIgnoreCase("MUX")) {
					mapper.put("muxMrnNo", mrnCreationReponse.getMRNCREATION().getMRNNo());
					mapper.put("muxMrnStatus", mrnCreationReponse.getMRNCREATION().getStatus());
					if(mrnPlaceOrderRequest.getMRNCREATION()!=null) {
						MrnCreation creation=mrnPlaceOrderRequest.getMRNCREATION().stream().findFirst().orElse(null);
						if(creation!=null) {
						mapper.put("muxOptimusId", creation.getOptimusId());
						}

					}

					mapper.put("muxMrnCreationDate", DateUtil.convertDateToString(new Date()));
				} else if (type.equalsIgnoreCase("CPE")) {
					mapper.put("cpeMrnNo", mrnCreationReponse.getMRNCREATION().getMRNNo());
					mapper.put("cpeMrnStatus", mrnCreationReponse.getMRNCREATION().getStatus());
					mapper.put("cpeMrnCreationDate", DateUtil.convertDateToString(new Date()));
					if(mrnPlaceOrderRequest.getMRNCREATION()!=null) {
						MrnCreation creation=mrnPlaceOrderRequest.getMRNCREATION().stream().findFirst().orElse(null);
						if(creation!=null) {
							mapper.put("cpeOptimusId", creation.getOptimusId());
						}

					}


				}
				saveMrnCreation(mrnCreationReponse, scServiceDetail, processInstanceId, type, taskName);
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), mapper,
						AttributeConstants.COMPONENT_LM,siteType);
				return true;
			}

		}

		return false;

	}



	private void constructMrnCreationRequest(ScServiceDetail scServiceDetail,
		 MrnPlaceOrderRequest mrnPlaceOrderRequest, String type,
			MstStateToDistributionCenterMapping distributionCenterMapping, String invType, String typeOfExpenses, Map<String, String> scComponentAttributesAMap)
			throws TclCommonException {
		String cpeType = null;
		if (invType.toLowerCase().contains("outright")) {
			cpeType = "Outright";
		} else {
			cpeType = "Rental";
		}

		
		Map<String, String> attributesMap=	commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("cpeSupplyHardwarePoNumber","level5Wbs","cpeInstallationVendorName","costCenter",
						"demandIdNo","glCode"),scServiceDetail.getId(), "LM", "A");
		
		LOGGER.info("constructMrnCreationRequest for serviceid:{} and cpeTye is:{}", scServiceDetail.getUuid(),
				cpeType);

		if (cpeType.equalsIgnoreCase("Outright")) {

			String materialDetails = componentAndAttributeService.getAdditionalAttributes(scServiceDetail,
					"CONFIRMMATERIALAVAIABILITY");

			LOGGER.info(
					"constructMrnCreationRequest CONFIRMMATERIALAVAIABILITY serviceid:{} and material details from Additional Param is:{}",
					scServiceDetail.getUuid(), materialDetails);

			ConfirmMaterialAvailabilityBean availabilityBean = Utils.convertJsonToObject(materialDetails,
					ConfirmMaterialAvailabilityBean.class);

			getMrnPlaceRequestforOutright(availabilityBean, mrnPlaceOrderRequest, scServiceDetail, attributesMap,
					cpeType, distributionCenterMapping, typeOfExpenses, type,scComponentAttributesAMap);

		} else {
			String request = null;
			if (type.equalsIgnoreCase("CPE")) {
				request = componentAndAttributeService.getAdditionalAttributes(scServiceDetail, "SAPINVENTORYCHECKCPE");
			} else {
				request = componentAndAttributeService.getAdditionalAttributes(scServiceDetail, "SAPINVENTORYCHECKMUX");
			}

			SapQuantityAvailableRequest sapQuantityResponse = Utils.convertJsonToObject(request,
					SapQuantityAvailableRequest.class);

			getMrnPlaceRequest(sapQuantityResponse, mrnPlaceOrderRequest, scServiceDetail, attributesMap, cpeType,
					distributionCenterMapping, typeOfExpenses, type,scComponentAttributesAMap);
		}

	}

	

	private void getMrnPlaceRequest(SapQuantityAvailableRequest sapQuantityResponse,
			MrnPlaceOrderRequest mrnPlaceOrderRequest, ScServiceDetail scServiceDetail,
			Map<String, String> attributesMap, String cpeType,
			MstStateToDistributionCenterMapping distributionCenterMapping, String typeOfExpenses, String type, Map<String, String> scComponentAttributesAMap) {
		String sapCode = DateUtil.generateUidForSap(6, type);
	String mrnNumber=	String.valueOf(getMrnSequenceNumber(scServiceDetail.getUuid(), type).getId());
		for (DisplayMaterialResponse displayMaterialResponse : sapQuantityResponse.getDisplayMaterial()) {
			MrnCreation mrnCreation = new MrnCreation();

			mrnCreation.setCircuitId(scServiceDetail.getUuid());
			mrnCreation.setOptimusId(sapCode);
			mrnCreation.setProjectName(scServiceDetail.getScOrder().getErfCustLeName());
			mrnCreation.setRentalOrSale(cpeType.equalsIgnoreCase("Rental") ? "RENTAL" : "SALE");
			mrnCreation.setDistributionCenter(
					distributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
			mrnCreation.setIssueTo("On Tata Communications Ltd. A/C");
			mrnCreation.setMRNDate(DateUtil.convertDateWithoutHypen(new Date()));
			mrnCreation.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
			mrnCreation.setDeliveryAddress(scComponentAttributesAMap.get("siteAddress"));
			mrnCreation.setDeliveryLocationCity(scComponentAttributesAMap.get("destinationCity"));
			mrnCreation.setDeliveryState(scComponentAttributesAMap.get("destinationState"));
			mrnCreation.setPinCode(scComponentAttributesAMap.get("destinationPincode"));
			mrnCreation.setContactName(scComponentAttributesAMap.get("localItContactName"));
			mrnCreation.setContactNo(scComponentAttributesAMap.get("localItContactMobile"));
			if (scComponentAttributesAMap.get("taxExemptionFlag") != null
					&& scComponentAttributesAMap.get("taxExemptionFlag").equalsIgnoreCase("Y")) {
				mrnCreation.setCustInSEZOrNONSEZ("Yes");

			} else {
				mrnCreation.setCustInSEZOrNONSEZ("No");

			}
			mrnCreation.setSEZCustGSTNo(scServiceDetail.getBillingGstNumber());
			mrnCreation.setCOFId(String.valueOf(scServiceDetail.getId()));
			mrnCreation.setBMBundle("");
			mrnCreation.setMaterialCode(displayMaterialResponse.getMaterialCode());
			mrnCreation.setEquipmentDesc(displayMaterialResponse.getMaterialDescription());
			mrnCreation.setQuantity(displayMaterialResponse.getQuantityAvailable());
			mrnCreation.setTCLPONo(displayMaterialResponse.getPONumber());


			if (cpeType.equalsIgnoreCase("Outright")) {
				mrnCreation.setGLAccount(attributesMap.getOrDefault("glCode", null));// budget
				mrnCreation.setCostCenter(attributesMap.getOrDefault("costCenter", null));// budetmatrix
			}

			mrnCreation.setSAPSerialNumber(displayMaterialResponse.getSAPSerialNumber());
			// mrnCreation.setOutrightSaleInvNo("");// doubt
			mrnCreation.setReqstrOrProgMgr("E9988");
			mrnCreation.setApprovedBy("E9988");
			mrnCreation.setProduct(scServiceDetail.getErfPrdCatalogParentProductName());
			mrnCreation.setPostingDate(DateUtil.convertDateWithoutHypen(new Date()));
			mrnCreation.setDocumentDate(DateUtil.convertDateWithoutHypen(new Date()));
			mrnCreation.setMaterialSlip("");// blank
			mrnCreation.setDocHeaderText("");// blank
			mrnCreation.setRecState(distributionCenterMapping.getStateCode());
			mrnCreation.setIssState(distributionCenterMapping.getStateCode());

			if (cpeType.equalsIgnoreCase("Rental")) {
				mrnCreation.setMovementType("221");// rule
				mrnCreation.setStockCategory("Q");
				mrnCreation.setWBSNo(displayMaterialResponse.getWBSNumber());

			} else if (cpeType.equalsIgnoreCase("Outright")) {
				mrnCreation.setMovementType("201");// rule
				mrnCreation.setStockCategory("");

			}
			mrnCreation.setPlant(displayMaterialResponse.getPlant());
			mrnCreation.setStorageLocation(displayMaterialResponse.getStorageLocation());

			if (type.equalsIgnoreCase("MUX")) {
				Map<String, String> scComponentAttributesMap=	commonFulfillmentUtils.getComponentAttributesDetails(
						Arrays.asList("muxDeliveryAddress", "muxDeliveryLocality", "muxDeliveryCity",
								"muxDeliveryCountry", "muxDeliveryPincode", "muxMakeModel"),scServiceDetail.getId(), "LM", "A");

				mrnCreation.setUnloadingPoint(constructSiteMuxAddress(scComponentAttributesMap));

			} else {
				mrnCreation.setUnloadingPoint(scComponentAttributesAMap.get("siteAddress"));
			}
			mrnCreation.setVendor(displayMaterialResponse.getVendorNumber());
			mrnCreation.setPurchasingDoc(attributesMap.getOrDefault("cpeSupplyHardwarePoNumber", null));// po number

			mrnCreation.setMRNNo(mrnNumber);
			mrnPlaceOrderRequest.getMRNCREATION().add(mrnCreation);
		}
	}
	
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRES_NEW)
	public MrnSequenceNumber getMrnSequenceNumber(String serviceCode,String type) {
		MrnSequenceNumber mrnSequenceNumber=new MrnSequenceNumber();
		mrnSequenceNumber.setServiceCode(serviceCode);
		mrnSequenceNumber.setType(type);
		return mrnSequenceNumberRepository.save(mrnSequenceNumber);

		
	}

	private void getMrnPlaceRequestforOutright(ConfirmMaterialAvailabilityBean availabilityBean,
			MrnPlaceOrderRequest mrnPlaceOrderRequest, ScServiceDetail scServiceDetail,
			Map<String, String> attributesMap, String cpeType,
			MstStateToDistributionCenterMapping distributionCenterMapping, String typeOfExpenses, String type, Map<String, String> scComponentAttributesAMap) {
		String sapCode=	DateUtil.generateUidForSap(6, type);
		String mrnNumber=	String.valueOf(getMrnSequenceNumber(scServiceDetail.getUuid(), type).getId());


		if (availabilityBean != null && availabilityBean.getSerialNumber() != null
				&& !availabilityBean.getSerialNumber().isEmpty()) {

			for (SerialNumberBean displayMaterialResponse : availabilityBean.getSerialNumber()) {

				MrnCreation mrnCreation = new MrnCreation();

				mrnCreation.setCircuitId(scServiceDetail.getUuid());
				mrnCreation.setOptimusId(sapCode);
				mrnCreation.setProjectName(scServiceDetail.getScOrder().getErfCustLeName());
				mrnCreation.setRentalOrSale(cpeType.equalsIgnoreCase("Rental") ? "RENTAL" : "SALE");
				mrnCreation.setDistributionCenter(
						distributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
				mrnCreation.setIssueTo("On Tata Communications Ltd. A/C");
				mrnCreation.setMRNDate(DateUtil.convertDateWithoutHypen(new Date()));
				mrnCreation.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
				mrnCreation.setDeliveryAddress(scComponentAttributesAMap.get("siteAddress"));
				mrnCreation.setDeliveryLocationCity(scComponentAttributesAMap.get("destinationCity"));
				mrnCreation.setDeliveryState(scComponentAttributesAMap.get("destinationState"));
				mrnCreation.setPinCode(scComponentAttributesAMap.get("destinationPincode"));
				mrnCreation.setContactName(scComponentAttributesAMap.get("localItContactName"));
				mrnCreation.setContactNo(scComponentAttributesAMap.get("localItContactMobile"));
				if (scComponentAttributesAMap.get("taxExemptionFlag") != null
						&& scComponentAttributesAMap.get("taxExemptionFlag").equalsIgnoreCase("Y")) {
					mrnCreation.setCustInSEZOrNONSEZ("Yes");

				} else {
					mrnCreation.setCustInSEZOrNONSEZ("No");

				}
				mrnCreation.setSEZCustGSTNo(scServiceDetail.getBillingGstNumber());
				mrnCreation.setCOFId(String.valueOf(scServiceDetail.getId()));
				mrnCreation.setBMBundle("");
				mrnCreation.setMaterialCode(displayMaterialResponse.getMaterialCode());
				mrnCreation.setEquipmentDesc(displayMaterialResponse.getMaterialDescription());
				mrnCreation.setQuantity(displayMaterialResponse.getQuantity());
				mrnCreation.setTCLPONo(attributesMap.getOrDefault("cpeHardwarePoNumber", null));

				

				if (cpeType.equalsIgnoreCase("Outright")) {
					mrnCreation.setGLAccount(attributesMap.getOrDefault("glCode", null));// budget
					mrnCreation.setCostCenter(attributesMap.getOrDefault("costCenter", null));// budetmatrix
				}

				mrnCreation.setSAPSerialNumber(displayMaterialResponse.getSerialNumber());
				// mrnCreation.setOutrightSaleInvNo("");// doubt
				mrnCreation.setReqstrOrProgMgr("E9988");
				mrnCreation.setApprovedBy("E9988");
				mrnCreation.setProduct(scServiceDetail.getErfPrdCatalogParentProductName());
				mrnCreation.setPostingDate(DateUtil.convertDateWithoutHypen(new Date()));
				mrnCreation.setDocumentDate(DateUtil.convertDateWithoutHypen(new Date()));
				mrnCreation.setMaterialSlip("");// blank
				mrnCreation.setDocHeaderText("");// blank
				mrnCreation.setRecState(distributionCenterMapping.getStateCode());
				mrnCreation.setIssState(distributionCenterMapping.getStateCode());

				if (cpeType.equalsIgnoreCase("Rental")) {
					mrnCreation.setMovementType("221");// rule
					mrnCreation.setStockCategory("Q");
					mrnCreation.setWBSNo(attributesMap.getOrDefault("level5Wbs", null));

				} else if (cpeType.equalsIgnoreCase("Outright")) {
					mrnCreation.setMovementType("201");// rule
					mrnCreation.setStockCategory("");

				}
				mrnCreation.setPlant(distributionCenterMapping.getMasterTclDistributionCenter().getPlant());
				mrnCreation.setStorageLocation(String
						.valueOf(distributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));

				if (type.equalsIgnoreCase("MUX")) {					
					
					Map<String, String> scComponentAttributesMap=	commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("muxDeliveryAddress", "muxDeliveryLocality", "muxDeliveryCity",
									"muxDeliveryCountry", "muxDeliveryPincode", "muxMakeModel"),scServiceDetail.getId(), "LM", "A");

					mrnCreation.setUnloadingPoint(constructSiteMuxAddress(scComponentAttributesMap));

				} else {
					mrnCreation.setUnloadingPoint(scComponentAttributesAMap.get("siteAddress"));
				}
				mrnCreation.setVendor(attributesMap.getOrDefault("cpeInstallationVendorName", null));
				mrnCreation.setPurchasingDoc(attributesMap.getOrDefault("cpeSupplyHardwarePoNumber", null));// po number

				mrnCreation.setMRNNo(mrnNumber);
				mrnPlaceOrderRequest.getMRNCREATION().add(mrnCreation);

			}

		}}

	private String constructSiteMuxAddress(Map<String, String> scComponentAttributesMap) {
		return scComponentAttributesMap.getOrDefault("muxDeliveryAddress", "") + ","
				+ scComponentAttributesMap.getOrDefault("muxDeliveryLocality", "") + ","
				+ scComponentAttributesMap.getOrDefault("muxDeliveryCity", "") + ","
				+ scComponentAttributesMap.getOrDefault("muxDeliveryCountry", "") + ","
				+ scComponentAttributesMap.getOrDefault("muxDeliveryPincode", "");

	}

	private SapQuantityCheckRequest constructQuantityRequest(ScServiceDetail scServiceDetail, String type,
			Map<String, String> materialQuantityMapping, MstStateToDistributionCenterMapping distributionCenterMapping,
			String invType, Map<String, String> licencedMaterialQuantityMapping) throws TclCommonException {

		SapQuantityCheckRequest quantityCheckRequest = new SapQuantityCheckRequest();
		String purchangeGroup = null;
		if (type.equalsIgnoreCase("CPE")) {
			purchangeGroup = "C15";
		} else {
			purchangeGroup = "C01";
		}
		String cpeType = null;

		if (invType.toLowerCase().contains("outright")) {
			cpeType = "outright";

		} else if (invType.toLowerCase().contains("rental")) {
			cpeType = "rental";
		}
		
		LOGGER.info("constructQuantityRequest cpeType for servicecode:{} and cpeType is:{}",scServiceDetail.getUuid(),cpeType);

		if (type.equalsIgnoreCase("cpe")) {

			List<ScServiceAttribute> cpeChassisAttr = scServiceDetail.getScServiceAttributes().stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("CPE Basic Chassis"))
					.collect(Collectors.toList());
			
			LOGGER.info("constructQuantityRequest cpeChassisAttr for servicecode:{} and cpeChassisAttr is:{}",scServiceDetail.getUuid(),cpeChassisAttr);

			ScServiceAttribute scServiceAttribute = cpeChassisAttr.stream().findAny().orElse(null);
			
			LOGGER.info("constructQuantityRequest scServiceAttribute for servicecode:{} and cpeChassisAttr is:{}",scServiceDetail.getUuid(),scServiceAttribute);

			String serviceParamId = scServiceAttribute.getAttributeValue();
			
			LOGGER.info("constructQuantityRequest serviceParamId for servicecode:{} and cpeChassisAttr is:{}",scServiceDetail.getUuid(),serviceParamId);

			if (StringUtils.isNotBlank(serviceParamId)) {
				Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
						.findById(Integer.valueOf(serviceParamId));
				if (scAdditionalServiceParam.isPresent()) {
					String bomResponse = scAdditionalServiceParam.get().getValue();
					
					LOGGER.info("constructQuantityRequest bomResponse for servicecode:{} and bomResponse is:{}",scServiceDetail.getUuid(),bomResponse);


					CpeBomResource[] bomResourcess = Utils.convertJsonToObject(bomResponse, CpeBomResource[].class);
					CpeBomResource bomResources = bomResourcess[0];
					List<MstCostCatalogue> mstCostCatalogues=null;
					
					if (cpeType.equalsIgnoreCase("rental")) {
						mstCostCatalogues=mstCostCatalogueRepository.findByDistinctBundledBomForRental(bomResources.getBomName());
					}
					else {
						 mstCostCatalogues = mstCostCatalogueRepository
									.findByDistinctBundledBom(bomResources.getBomName());
					}

					LOGGER.info("constructQuantityRequest mstCostCatalogues for servicecode:{} and mstCostCatalogues is:{}",scServiceDetail.getUuid(),mstCostCatalogues);

					if (mstCostCatalogues != null) {

						for (MstCostCatalogue mstCostCatalogue : mstCostCatalogues) {

							DisplayMaterialRequest displayMaterial = new DisplayMaterialRequest();
							displayMaterial.setBMBundle("");

							String materialCode = null;

							if (mstCostCatalogue.getServiceNumber() != null) {
								displayMaterial.setMaterialCode(mstCostCatalogue.getServiceNumber());
								materialCode = mstCostCatalogue.getServiceNumber();
								licencedMaterialQuantityMapping.put(materialCode,
										String.valueOf(mstCostCatalogue.getQuantity()));

							} else if (cpeType.equalsIgnoreCase("rental")
									&& mstCostCatalogue.getRentalMaterialCode() != null) {
								displayMaterial.setMaterialCode(mstCostCatalogue.getRentalMaterialCode());
								materialCode = mstCostCatalogue.getRentalMaterialCode();
								materialQuantityMapping.put(materialCode,
										String.valueOf(mstCostCatalogue.getQuantity()));

							} else if (cpeType.equalsIgnoreCase("outright")
									&& mstCostCatalogue.getSaleMaterialCode() != null) {
								displayMaterial.setMaterialCode(mstCostCatalogue.getSaleMaterialCode());
								materialCode = mstCostCatalogue.getSaleMaterialCode();
								materialQuantityMapping.put(materialCode,
										String.valueOf(mstCostCatalogue.getQuantity()));

							}
							displayMaterial
									.setPlant(distributionCenterMapping.getMasterTclDistributionCenter().getPlant());
							displayMaterial.setPurchaseGroup(purchangeGroup);
							displayMaterial.setStorageLocation(String.valueOf(distributionCenterMapping
									.getMasterTclDistributionCenter().getSapStorageLocation()));
							displayMaterial.setMaterialQuantity(String.valueOf(mstCostCatalogue.getQuantity()));
							quantityCheckRequest.getDisplayMaterial().add(displayMaterial);
						}
					}

				}
			}
		}

		else if (type.equalsIgnoreCase("RF")) {

			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "rfMakeModel", "LM", "A");

			LOGGER.info("scComponentAttribute for SapQuantityCheckRequest data rf :{}", scComponentAttribute);

			if (scComponentAttribute != null && distributionCenterMapping != null) {

				LOGGER.info("scComponentAttribute for SapQuantityCheckRequest data value rf :{}",
						scComponentAttribute.getAttributeValue());

				List<VwBomMaterialDetail> vwBomMaterialDetails = vwBomMaterialDetailRepository
						.findByBomCode(scComponentAttribute.getAttributeValue());
				LOGGER.info("scComponentAttribute for SapQuantityCheckRequest vwBomMaterialDetails rf:{}",
						vwBomMaterialDetails);

				if (!vwBomMaterialDetails.isEmpty()) {

					for (VwBomMaterialDetail vwBomMaterialDetail : vwBomMaterialDetails) {

						DisplayMaterialRequest displayMaterial = new DisplayMaterialRequest();
						displayMaterial.setBMBundle("");
						displayMaterial.setMaterialCode(vwBomMaterialDetail.getMaterialCode());
						displayMaterial.setPlant(distributionCenterMapping.getMasterTclDistributionCenter().getPlant());
						displayMaterial.setPurchaseGroup(purchangeGroup);
						displayMaterial.setStorageLocation(String.valueOf(
								distributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
						displayMaterial.setMaterialQuantity(vwBomMaterialDetail.getMaterialQuantity());
						materialQuantityMapping.put(vwBomMaterialDetail.getMaterialCode(),
								vwBomMaterialDetail.getMaterialQuantity());

						quantityCheckRequest.getDisplayMaterial().add(displayMaterial);

					}

				}

			}

		} else if (type.equalsIgnoreCase("mux")) {

			Map<String, String> scComponentAttributesMap=	commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("muxDeliveryAddress", "muxDeliveryLocality", "muxDeliveryCity",
							"muxDeliveryCountry", "muxDeliveryPincode", "muxMakeModel"),scServiceDetail.getId(), "LM", "A");

			String muxMakeModel = scComponentAttributesMap.getOrDefault("muxMakeModel", null);
			LOGGER.info("scComponentAttribute for SapQuantityCheckRequest data mux:{}", muxMakeModel);

			if (muxMakeModel != null) {
				LOGGER.info("scComponentAttribute for SapQuantityCheckRequest data value mux :{}",
						muxMakeModel);

				List<VwMuxBomMaterialDetail> vmMuxBomMaterialDetails = vwBomMuxDetailsRepository
						.findByBomName(muxMakeModel);
				LOGGER.info("scComponentAttribute for SapQuantityCheckRequest vwBomMaterialDetails mux:{}",
						vmMuxBomMaterialDetails);

				if (!vmMuxBomMaterialDetails.isEmpty()) {

					for (VwMuxBomMaterialDetail vmMuxBomMaterialDetail : vmMuxBomMaterialDetails) {

						DisplayMaterialRequest displayMaterial = new DisplayMaterialRequest();
						displayMaterial.setBMBundle("");
						displayMaterial.setMaterialCode(vmMuxBomMaterialDetail.getMaterialCode());
						displayMaterial.setPlant(distributionCenterMapping.getMasterTclDistributionCenter().getPlant());
						displayMaterial.setPurchaseGroup(purchangeGroup);
						displayMaterial.setStorageLocation(String.valueOf(
								distributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
						displayMaterial.setMaterialQuantity(vmMuxBomMaterialDetail.getMaterialQuantity());
						materialQuantityMapping.put(vmMuxBomMaterialDetail.getMaterialCode(),
								vmMuxBomMaterialDetail.getMaterialQuantity());

						quantityCheckRequest.getDisplayMaterial().add(displayMaterial);

					}

				}

			}

		}

		return quantityCheckRequest;
	}

	private boolean isQuantityAvailable(Map<String, String> hardWareMaterialQuantityMapping,
			Map<String, String> licencedMaterialQuantityMapping, SapQuantityResponse sapQuantityResponse, String type,
			Map<String, List<DisplayMaterialResponse>> availableQuantityMap, DelegateExecution execution,
			ScServiceDetail scServiceDetail) {
		boolean licenceAvailable = false;
		String poNumber = null;
		String level5Wbs = null;
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(),
						"cpeSupplyHardwarePoNumber", "LM", "A");

		ScComponentAttribute wbScComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "level5Wbs", "LM", "A");
		if (wbScComponentAttribute != null) {
			level5Wbs = wbScComponentAttribute.getAttributeValue();
		}
		if (scComponentAttribute != null) {
			poNumber = scComponentAttribute.getAttributeValue();
		}
		LOGGER.info("po number receiver service code:{} for check inventory:{} and level5Wbs no:{}",scServiceDetail.getUuid(), poNumber,level5Wbs);

		if (sapQuantityResponse == null || sapQuantityResponse.getDisplayMaterial().isEmpty()) {
			return false;
		}
		
		if (type.equalsIgnoreCase("CPE")) {

			getCpeMaterialQuantity(sapQuantityResponse, poNumber, level5Wbs, availableQuantityMap);

		} else {
			
			getOtherQuantityDetails(sapQuantityResponse,availableQuantityMap);


		}
	

		LOGGER.info("availableQuantityMap size for  details for service code:{}and quantity size:{}",
				scServiceDetail.getUuid(), availableQuantityMap.size());

		LOGGER.info("availableQuantityMap details for service code:{}  and :{}", scServiceDetail.getUuid(),
				availableQuantityMap);
		if (availableQuantityMap.size() == 0) {
			return false;
		}

		if (type.equalsIgnoreCase("cpe")) {

			

			if (licencedMaterialQuantityMapping.size() > 0) {

				licenceAvailable = true;
				for (Map.Entry<String, String> entry : licencedMaterialQuantityMapping.entrySet()) {

					if (availableQuantityMap.containsKey(entry.getKey())) {

						if (availableQuantityMap.get(entry.getKey()).size() >= Integer.valueOf(entry.getValue())) {
							LOGGER.info(
									"harware quantity for material code for request and reponse from sap size:{} and ",
									entry.getValue(), availableQuantityMap.get(entry.getKey()).size());
						} else {
							licenceAvailable = false;
							execution.setVariable("isCpeLcAvailableInInventory", licenceAvailable);
						}

					} else {
						licenceAvailable = false;
						execution.setVariable("isCpeLcAvailableInInventory", licenceAvailable);
					}

				}

				execution.setVariable("isCpeLcAvailableInInventory", licenceAvailable);

			}

			if (hardWareMaterialQuantityMapping.size() > 0) {

				for (Map.Entry<String, String> entry : hardWareMaterialQuantityMapping.entrySet()) {

					if (availableQuantityMap.containsKey(entry.getKey())) {

						if (availableQuantityMap.get(entry.getKey()).size() >= Integer.valueOf(entry.getValue())) {
							LOGGER.info(
									"harware quantity for material code for request and reponse from sap size:{} and ",
									entry.getValue(), availableQuantityMap.get(entry.getKey()).size());
						} else {
							return false;
						}

					} else {
						return false;
					}

				}
				return true;

			}

		} else {

			if (!sapQuantityResponse.getDisplayMaterial().isEmpty()) {

				for (Map.Entry<String, String> entry : hardWareMaterialQuantityMapping.entrySet()) {

					if (availableQuantityMap.containsKey(entry.getKey())) {

						if (Integer.valueOf(entry.getValue()) >= availableQuantityMap.get(entry.getKey()).size()) {
							LOGGER.info(
									"harware quantity for material code for request and reponse from sap size:{} and ",
									entry.getValue(), availableQuantityMap.get(entry.getKey()).size());
						}

					} else {
						return false;
					}

				}
				return true;

			}

		}

		return false;
	}

	private void getOtherQuantityDetails(SapQuantityResponse sapQuantityResponse,
			Map<String, List<DisplayMaterialResponse>> availableQuantityMap) {
		for (DisplayMaterialResponse displayMaterialResponse : sapQuantityResponse.getDisplayMaterial()) {

			if (displayMaterialResponse.getStatus().equalsIgnoreCase("AVAILABLE")) {
				if (availableQuantityMap.containsKey(displayMaterialResponse.getMaterialCode())) {
					List<DisplayMaterialResponse> valueDisplayRespone = availableQuantityMap
							.get(displayMaterialResponse.getMaterialCode());
					valueDisplayRespone.add(displayMaterialResponse);
					availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);

				} else {
					List<DisplayMaterialResponse> valueDisplayRespone = new ArrayList<>();
					valueDisplayRespone.add(displayMaterialResponse);
					availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);
				}

			}

		}
		
	}

	private void getCpeMaterialQuantity(SapQuantityResponse sapQuantityResponse, String poNumber, String level5Wbs,
			Map<String, List<DisplayMaterialResponse>> availableQuantityMap) {
		for (DisplayMaterialResponse displayMaterialResponse : sapQuantityResponse.getDisplayMaterial()) {

			if (displayMaterialResponse.getStatus().equalsIgnoreCase("AVAILABLE")) {
				if (poNumber != null && displayMaterialResponse.getPONumber().equalsIgnoreCase(poNumber)) {
					if (availableQuantityMap.containsKey(displayMaterialResponse.getMaterialCode())) {
						List<DisplayMaterialResponse> valueDisplayRespone = availableQuantityMap
								.get(displayMaterialResponse.getMaterialCode());
						valueDisplayRespone.add(displayMaterialResponse);
						availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);

					} else {
						List<DisplayMaterialResponse> valueDisplayRespone = new ArrayList<>();
						valueDisplayRespone.add(displayMaterialResponse);
						availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);
					}

				} else {

					if (level5Wbs != null && displayMaterialResponse.getWBSNumber().equalsIgnoreCase(level5Wbs)) {

						if (availableQuantityMap.containsKey(displayMaterialResponse.getMaterialCode())) {
							List<DisplayMaterialResponse> valueDisplayRespone = availableQuantityMap
									.get(displayMaterialResponse.getMaterialCode());
							valueDisplayRespone.add(displayMaterialResponse);
							availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);

						} else {
							List<DisplayMaterialResponse> valueDisplayRespone = new ArrayList<>();
							valueDisplayRespone.add(displayMaterialResponse);
							availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);
						}
					}
				}

			}

		}		
	}

	private Map<String, String> createCommonHeader() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		return headers;
	}

	public MinResponseBean getMinStatus(MinResponseBean minResponseBean) throws TclCommonException {
		/*
		 * Map<String, String> atMap = new HashMap<>(); atMap.put("pRowCostApproved",
		 * costApproval.getpRowCostApproved()); atMap.put("costApprovalRemarks",
		 * costApproval.getCostApprovalRemarks());
		 * componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
		 * AttributeConstants.COMPONENT_LM);
		 */
		LOGGER.info("Saving min creation response : " + Utils.convertObjectToJson(minResponseBean));
		return minResponseBean;
	}

	/**
	 * 
	 * processAutoPr - This method is used to process the autoPR
	 * 
	 * @param serviceCode
	 * @param dealType
	 * @param processInstanceId
	 * @return
	 * @throws TclCommonException
	 */
	public AutoPRResponse processAutoPr(String serviceCode, String dealType, Boolean isMaterial,
			String processInstanceId, String typeOfExpenses, List<Map<String, String>> bomMapper,String vendorCode)
			throws TclCommonException {
		AutoPRResponse autoPRResponse = null;
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
		
		Map<String, String> scComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(
						scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
		if (scServiceDetail != null) {
			AutoPRRequest autoPRRequest = constructAutoPrRequest(scServiceDetail, dealType, isMaterial,
					processInstanceId, typeOfExpenses, bomMapper,scComponentAttributesAMap,vendorCode);
			AuditLog inventory=	saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPRRequest), null, serviceCode, dealType+"PRPO",processInstanceId);
			RestResponse autoPrResponse = restClientService.postWithBasicAuthentication(sapAutoPrUrl,
					Utils.convertObjectToJson(autoPRRequest), createCommonHeader(), userName, password);
			if (autoPrResponse.getStatus() == Status.SUCCESS) {
				inventory.setResponse(autoPrResponse.getData());
				auditLogRepositoryRepository.saveAndFlush(inventory);
				autoPRResponse = Utils.convertJsonToObject(autoPrResponse.getData(), AutoPRResponse.class);
			}
		}
		return autoPRResponse;
	}

	/**
	 * 
	 * constructAutoPrRequest - This method is used to construct the autoPRRequest
	 * 
	 * @param scServiceDetail
	 * @param invType
	 * @param processInstanceId
	 * @param scComponentAttributesAMap 
	 * @return
	 */
	private AutoPRRequest constructAutoPrRequest(ScServiceDetail scServiceDetail, String invType, Boolean isMaterial,
			String processInstanceId, String typeOfExpenses, List<Map<String, String>> bomMapper, Map<String, String> scComponentAttributesAMap, String vendorCode) throws TclCommonException {
		LOGGER.info("Entering the constructAutoPrRequest with serviceId {}", scServiceDetail.getUuid());
		LOGGER.info("Extracted the materialDetails {}", bomMapper);
		AutoPRRequest autoPrRequest = new AutoPRRequest();
		autoPrRequest.setPRHeader(constructPrHeader(scServiceDetail, invType));
		List<PRLine> prLines = new ArrayList<PRLine>();
		List<String> invalidMaterialCodes=new ArrayList<>();

		autoPrRequest.getPRHeader().setPRLines(prLines);
		Double exchangeValue = forexService.convertCurrency(Currencies.USD, Currencies.INR);
		LOGGER.info("Exchange Value {}",exchangeValue);
		if (!appEnv.equals("PROD")) {
			if(exchangeValue==null) {
				LOGGER.info("Going into the UAT default date mode with date 20200115");
				exchangeValue = forexService.convertCurrencyWithDate(Currencies.USD, Currencies.INR,"20200115");//THIS IS ONLY FOR UAT
				LOGGER.info("exchange Value {}",exchangeValue);
			}else {
				LOGGER.info("extracted exchange Value {}",exchangeValue);
			}
		}
		for (Map<String, String> materialAttr : bomMapper) {
			constructPrLine(scServiceDetail, invType, isMaterial, processInstanceId, prLines, materialAttr,
					typeOfExpenses,exchangeValue,scComponentAttributesAMap,autoPrRequest,invalidMaterialCodes,vendorCode);

		}
		LOGGER.info("Material Codes not available in VMI :",invalidMaterialCodes);


		if(!invalidMaterialCodes.isEmpty())
		{
			StringTokenizer tokenizer = new StringTokenizer(invalidMaterialCodes.get(0), "-");
			while (tokenizer.hasMoreTokens())
			{
				tokenizer.nextToken();
				String createdDate=tokenizer.nextToken();
				autoPrRequest.getPRHeader().setServiceIdQuoteRef("Service Id :" + scServiceDetail.getUuid() + " BACK_TO_BACK_ORDER - "+ createdDate);
				break;
			}

		}


		return autoPrRequest;
	}

	/**
	 * constructPrLine - Contruct the PrLine
	 * 
	 * @param scServiceDetail
	 * @param invType
	 * @param processInstanceId
	 * @param prLines
	 * @param materialAttr
	 * @param scComponentAttributesAMap 
	 */
	private void constructPrLine(ScServiceDetail scServiceDetail, String invType, Boolean isMaterial,
			String processInstanceId, List<PRLine> prLines, Map<String, String> materialAttr, String typeOfExpenses,
			Double exchangeValue, Map<String, String> scComponentAttributesAMap,AutoPRRequest autoPrRequest,List<String> invalidMaterialCodes, String vendorCode) throws TclCommonException {
		String materialCode = null;
		PRLine prLine = new PRLine();
		String prpoCode = null;
		prLine.setCreatedBy("E9988");
		prLine.setMaterialCode(materialCode);
		prLine.setReqTrackingNumber(processInstanceId);
		prLine.setItemText("");
		TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(scServiceDetail.getId(), "install-cpe", "A");
		
		if (taskPlan != null) {
			LocalDateTime cpeTime = taskPlan.getPlannedStartTime().toLocalDateTime().minusDays(7);
			
			LOGGER.info("cpe time:{}",cpeTime);

			if (cpeTime.isAfter(LocalDateTime.now().plusDays(7))) {
				prLine.setDeliveryDate(formatter.format(Timestamp.valueOf(cpeTime)));


			} else {
				prLine.setDeliveryDate(formatter.format(Timestamp.valueOf(LocalDateTime.now().plusDays(7))));

			}

		}
		else {
			Calendar cal = Calendar.getInstance(); 
			cal.add(Calendar.DATE, 3);
			prLine.setDeliveryDate(formatter.format(cal.getTime()));
		}


		addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "cpePoDeliveryDate",prLine.getDeliveryDate() );

		List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
				.findByState(scComponentAttributesAMap.get("destinationState"));
		for (MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping : distributionCenterMapping) {
			prLine.setStorageLocation(String.valueOf(
					mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
			prLine.setPlant(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
			break;
		}
		prLine.setRequisitioner("E9988");
		if (scComponentAttributesAMap.get("destinationCountry").equalsIgnoreCase("india")) {
			prLine.setPurchasingOrganisation("VSNL");
		} else {
			prLine.setPurchasingOrganisation("INTL");
		}
		prLine.setUnitOfMeasurement("NOS");
		List<MstCostCatalogue> mstCostCatalogues = new ArrayList<>();
		String bomName = materialAttr.get("BOM_NAME");
		if (isMaterial && invType.toLowerCase().contains("outright")) {
			materialCode = materialAttr.get("SALES_CODE");
			mstCostCatalogues = mstCostCatalogueRepository.findBySaleMaterialCodeAndBundledBom(materialCode, bomName);
			prLine.setMaterialCode(materialCode);
			prpoCode = "K";
			//List<MstVmi> mstVmis = mstVmiRepository.findBySaleMaterialCode(materialCode);
			/*if (!mstVmis.isEmpty()) {
				prLine.setItemText(mstVmis.get(0).getPoNumber());
			} else {
				prLine.setItemText("BACK_TO_BACK_ORDER");
			}*/
			mstCostCatalogues = mstCostCatalogueRepository.findBySaleMaterialCodeAndBundledBom(materialCode, bomName);
			CpeBomResource cpeBomResource = getCpeBomDetails(scServiceDetail);
			List<String> longDescs = new ArrayList<>();
			if (cpeBomResource != null) {
				if (!CollectionUtils.isEmpty(cpeBomResource.getResources())) {
					cpeBomResource.getResources().forEach(resource -> {
						if (!(resource.getLongDesc() != null && resource.getLongDesc().trim().equals("-"))) {
							longDescs.add(resource.getLongDesc().trim());
						}
					});
				}
			}
			boolean router = mstCostCatalogues.stream()
					.anyMatch(a -> a.getCategory() != null && a.getCategory().equalsIgnoreCase("Router"));
			List<MstVmi> mstVmis = mstVmiRepository.findBySaleMaterialCode(materialCode);
			if (!mstVmis.isEmpty()) {
				if (prLines.isEmpty()) {
					autoPrRequest.getPRHeader().setServiceIdQuoteRef("Service Id :" + scServiceDetail.getUuid() + " Issuance from VMI stock - "
							+ mstVmis.get(0).getPoNumber());
					//prLine.setItemText("Service Id :" + scServiceDetail.getUuid() + " Issuance from VMI stock - "
						//	+ mstVmis.get(0).getPoNumber() + "_\n"); // TODO - Along with
				} else {
					//prLine.setItemText("\n");// TODO - Cost Catalogue last updated
					// date
				}
				// this text we have
				// to apend the
				// entire bom text
			} else {
				invalidMaterialCodes.add(materialCode+"-"+mstCostCatalogues.get(0).getCreatedDate());
				if (prLines.isEmpty()) {
					autoPrRequest.getPRHeader().setServiceIdQuoteRef("Service Id :" + scServiceDetail.getUuid() + " BACK_TO_BACK_ORDER - "
							+ mstCostCatalogues.get(0).getCreatedDate());
					//prLine.setItemText("Service Id :" + scServiceDetail.getUuid() + " BACK_TO_BACK_ORDER - "
						//	+ mstCostCatalogues.get(0).getCreatedDate() + "\n");// TODO - Cost Catalogue last updated
																					// date
				} else {
					//prLine.setItemText("\n");// TODO - Cost Catalogue last updated
												// date
				}

			}

			try {
				if (router) {
					//prLine.setItemText(
						//	prLine.getItemText().concat(longDescs.stream().collect(Collectors.joining("\n"))));
				}

			} catch (Exception ex) {
				LOGGER.error("Error while appending longDescs in prLine : {}", ex);
			}
			
		} else if (!isMaterial) {
			if (invType.toLowerCase().contains("outright")) {
				prpoCode = "K";
			} else {
				prpoCode = "P";
			}
			prLine.setItemCategory("D");
			mstCostCatalogues = mstCostCatalogueRepository
					.findByServiceNumberAndBundledBomAndProductCode(materialAttr.get("SERVICE_NUMBER"), bomName,materialAttr.get("PROD_CODE"));
			prLine.setUnitOfMeasurement("AU");
			prLine.setUnitOfMeasurement1("EA");
			prLine.setServiceNo(materialAttr.get("SERVICE_NUMBER"));
			prLine.setMaterialGroup("ITSWPERLC");// PROD Catelogue
		} else if (isMaterial && invType.toLowerCase().contains("rental")) {
			materialCode = materialAttr.get("RENTAL_CODE");
			mstCostCatalogues = mstCostCatalogueRepository.findByRentalMaterialCodeAndBundledBom(materialCode, bomName);
			CpeBomResource cpeBomResource = getCpeBomDetails(scServiceDetail);
			List<String> longDescs = new ArrayList<>();
			if (cpeBomResource != null) {
				if (!CollectionUtils.isEmpty(cpeBomResource.getResources())) {
					cpeBomResource.getResources().forEach(resource -> {
						if (!(resource.getLongDesc() != null && resource.getLongDesc().trim().equals("-"))) {
							longDescs.add(resource.getLongDesc().trim());
						}
					});
				}
			}
			prLine.setMaterialCode(materialCode);
			prpoCode = "Q";

			boolean router = mstCostCatalogues.stream()
					.anyMatch(a -> a.getCategory() != null && a.getCategory().equalsIgnoreCase("Router"));
			List<MstVmi> mstVmis = mstVmiRepository.findByRentalMaterialCode(materialCode);
			if (!mstVmis.isEmpty()) {
				if (prLines.isEmpty()) {
					autoPrRequest.getPRHeader().setServiceIdQuoteRef("Service Id :" + scServiceDetail.getUuid() + " Issuance from VMI stock - "
							+ mstVmis.get(0).getPoNumber());
					//prLine.setItemText("Service Id :" + scServiceDetail.getUuid() + " Issuance from VMI stock - "
					//		+ mstVmis.get(0).getPoNumber() + "_\n"); // TODO - Along with
					// this text we have
					// to apend the
					// entire bom text
				} else {
					//prLine.setItemText("\n");// TODO - Cost Catalogue last updated
					// date
				}
			} else {
				invalidMaterialCodes.add(materialCode+"-"+mstCostCatalogues.get(0).getCreatedDate());
				if (prLines.isEmpty()) {
					autoPrRequest.getPRHeader().setServiceIdQuoteRef("Service Id :" + scServiceDetail.getUuid() + " BACK_TO_BACK_ORDER - "
							+ mstCostCatalogues.get(0).getCreatedDate());
					//prLine.setItemText("Service Id :" + scServiceDetail.getUuid() + " BACK_TO_BACK_ORDER - "
						//	+ mstCostCatalogues.get(0).getCreatedDate() + "\n");// TODO - Cost Catalogue last updated
																					// date
				} else {
					//prLine.setItemText("\n");// TODO - Cost Catalogue last updated
																					// date
				}
			}

			try {
				if (router) {
					//prLine.setItemText(
						//	prLine.getItemText().concat(longDescs.stream().collect(Collectors.joining("\n"))));
				}

			} catch (Exception ex) {
				LOGGER.error("Error while appending longDescs in prLine : {}", ex);
			}
		}
		prLine.setAcctAssignmentCategory(prpoCode);
		prLine.setOpportunityId(scServiceDetail.getScOrder().getTpsSfdcOptyId());
		Double totalValuationPrice = 0.0;
		Double totallLp = 0.0;
		Double incrementalPrice = 0.0;
		for (MstCostCatalogue mstCostCatalogue : mstCostCatalogues) {
			LOGGER.info("Total Valuation Price before {} and total price after ddp {}", totalValuationPrice,
					mstCostCatalogue.getTotalPriceDdp());
			totalValuationPrice = totalValuationPrice + mstCostCatalogue.getTotalPriceDdp();
			totallLp = totallLp + mstCostCatalogue.getPerListPriceUsd();
			LOGGER.info("Total Valuation Price after  {}", totalValuationPrice);
			incrementalPrice = incrementalPrice + mstCostCatalogue.getIncrementalRate();
			if (mstCostCatalogue.getServiceNumber() != null) {
				prLine.setServiceNo(mstCostCatalogue.getServiceNumber());// COst Catelogue
			}
			if (mstCostCatalogue.getShortText() != null) {
				prLine.setShortText1(mstCostCatalogue.getShortText());// Cost Catelogue
				
				if (isMaterial && invType.toLowerCase().contains("rental")) {
					if (!(prLine.getShortText1() != null && prLine.getShortText1().trim().equals("-"))) {
						//prLine.setItemText(prLine.getItemText().concat("- \n") + prLine.getShortText1());
					}
				}

				
			}

			if (mstCostCatalogue.getProcurementDiscountPercentage() != null) {
				prLine.setDiscountPercentage(String.valueOf(mstCostCatalogue.getProcurementDiscountPercentage()));// COST
																													// Catelog
			}
			if (prLine.getShortText().equals(""))
				prLine.setShortText(mstCostCatalogue.getProductCode());// FROM
																													// PROD
			if (isMaterial) { // CAT
				if (prLine.getQuantity() == "")
					prLine.setQuantity(String.valueOf(mstCostCatalogue.getQuantity()));// FROM PROD CAT
				prLine.setQuantity1("");
			} else {
				if (mstCostCatalogue.getQuantity() != null) {
					prLine.setQuantity1(String.valueOf(mstCostCatalogue.getQuantity()));// Cost Catelogue
				}
				prLine.setQuantity("");
			}
		}
		if (!isMaterial) {
			prLine.setCurrency1("INR");// Cost Catelogue
		} else {
			prLine.setCurrency("INR");
		}
		String productName = null;
		if (scServiceDetail.getErfPrdCatalogProductName().equals("IAS")) {
			productName = "ILL";
		}
		else if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
			productName= "GLOBAL VPN";

		}
			
		ScComponentAttribute level5Wbs = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "level5Wbs", "LM", "A");

		ScComponentAttribute costCenter = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "costCenter", "LM", "A");

		ScComponentAttribute demandIdNo = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "demandIdNo", "LM", "A");

		ScComponentAttribute glCode = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "glCode", "LM", "A");
		if (invType.toLowerCase().contains("outright")) {
			if (glCode != null)
				prLine.setGLAccount(glCode.getAttributeValue());// From Budget Matrix
			if (costCenter != null)
				prLine.setCostCenter(costCenter.getAttributeValue());// From Budget Matrix
			if (demandIdNo != null)
				prLine.setDemandId(demandIdNo.getAttributeValue());// BM
		}
		if (level5Wbs != null)
			prLine.setWBSElement(level5Wbs.getAttributeValue());// From Budget Matrix

		prLine.setOpportunityId(scServiceDetail.getScOrder().getTpsCrmOptyId());
		prLine.setSiteId(scServiceDetail.getUuid()); // TO BE FROM SFDC

		LOGGER.info("FOREX Rate for USD to INR is {}", exchangeValue);
		if (exchangeValue != null) {
			LOGGER.info("Exchange value {} ::: incrementalPrice {} ::: Total price after ddp {}", exchangeValue,
					incrementalPrice, totalValuationPrice);
			Double finalValue = ((exchangeValue + incrementalPrice) * totalValuationPrice);
			LOGGER.info("Final Price {}", finalValue);
			if (isMaterial) {
				prLine.setValuationPrice(String.valueOf(finalValue));
			} else {
				prLine.setGrossPrice(String.valueOf(finalValue));// Cost Catelogue
			}
		}

		prLine.setListPrice(String.valueOf(totallLp));// From Cost Catelogue PerUnit Price in USD
		prLine.setSiteId(scServiceDetail.getUuid());
		prLine.setListPriceCurrency("USD");
		prLine.setCOFRefNo(scServiceDetail.getScOrderUuid());
		prLine.setCOPFId1("");// TODO SAME as COPF
		prLine.setServiceId(scServiceDetail.getUuid());
		prLine.setProduct(productName);
		prLine.setVendorId(vendorCode);
		prLines.add(prLine);
	}

	/**
	 * 
	 * constructPrHeader - Construct the Pr Header
	 * 
	 * @param scServiceDetail
	 * @return
	 */
	private PRHeader constructPrHeader(ScServiceDetail scServiceDetail, String invType) {
		PRHeader prHeader = new PRHeader();
		String productName = scServiceDetail.getErfPrdCatalogProductName();
		if (scServiceDetail.getErfPrdCatalogProductName().equals("IAS")) {
			productName = "ILL";
		}
		String typeOfDeal = "";
		if (invType.toLowerCase().contains("outright")) {
			typeOfDeal = "SALE";
		} else {
			typeOfDeal = "RENTAL";
		}
		prHeader.setCircuitType(productName);
		prHeader.setCOPFId("");// TODO - Get from L20
		prHeader.setTypeOfDeal(typeOfDeal);
		prHeader.setOpportunityCategory("CAT 1-2");// TODO - GET FROM L2O
		prHeader.setCUID(scServiceDetail.getScOrder().getTpsSfdcCuid());
		prHeader.setReceivedDate(formatter.format(scServiceDetail.getScOrder().getCreatedDate()));
		prHeader.setCustomerName(scServiceDetail.getScOrder().getErfCustCustomerName());
		ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(),
						IzosdwanCommonConstants.OWNER_REGION);
		if (scServiceAttribute != null && scServiceAttribute.getAttributeValue() != null) {
			LOGGER.info("Opportunity Region exists for service Id::{}",scServiceDetail.getId());
			prHeader.setOpportunityRegion(scServiceAttribute.getAttributeValue().toUpperCase());
		}else{
			LOGGER.info("Defaulting Opportunity Region as India for service Id::{}",scServiceDetail.getId());
			prHeader.setOpportunityRegion("INDIA");
		}
		return prHeader;
	}

	public void processPoStatusResponse(PoStatusResponseBean poStatusBean) {
		LOGGER.info("Entering processPoStatusResponse...");
		if (Objects.nonNull(poStatusBean.getPoStatusBean())) {
			PoStatusBean poStatus = poStatusBean.getPoStatusBean();
			LOGGER.info("processPoStatusResponse PO Number={}",poStatus.getPONumber());
			Optional<ProCreation> proCreationOp = proCreationRepository.findByPoNumber(poStatus.getPONumber());
			if(proCreationOp.isPresent()) {
				ProCreation proCreation = proCreationOp.get();
				proCreation.setPoNumber(poStatus.getPONumber());
				proCreation.setPoStatus(poStatus.getPOStatus());
				proCreation.setPoCompletedDate(Timestamp.valueOf(LocalDateTime.now()));
				Map<String, String> mapper = new HashMap<>();
				Optional.ofNullable(proCreationRepository.save(proCreation))
						.ifPresent(pro -> {
							LOGGER.info("Triggering task {} for prNumber {}", pro.getTaskName() + "-async",
									pro.getPrNumber());
							Execution execution = runtimeService.createExecutionQuery()
									.processInstanceId(pro.getProcessInstanceId())
									.activityId(pro.getTaskName() + "-async").singleResult();
							if (Objects.nonNull(execution)) {

								if ("HARDWARE".equalsIgnoreCase(proCreation.getType())) {
									runtimeService.setVariable(execution.getId(), "cpeHardwarePoStatus",
											poStatus.getPOStatus());
									mapper.put("cpeSupplyHardwarePoStatus", "PO Released");
								} else if ("LICENCE".equalsIgnoreCase(proCreation.getType())) {
									runtimeService.setVariable(execution.getId(), "cpeLicencePoStatus",
											poStatus.getPOStatus());
									mapper.put("cpeLicencePoStatus", "PO Released");
								}
								if ("OFFNETLM".equals(proCreation.getType())) {
									runtimeService.setVariable(execution.getId(), "offnetLocalPoStatus",
											poStatus.getPOStatus());
									mapper.put("offnetLocalPoStatus", poStatus.getPOStatus());
								}
								saveTaskLogForPo(proCreation,poStatus);
								runtimeService.trigger(execution.getId());
							}
						});
				
				Integer serviceId = proCreation.getServiceId();
				componentAndAttributeService.updateAttributes(serviceId, mapper,AttributeConstants.COMPONENT_LM,"A");
				
				
			
			}
		}
	}

	
	public void saveTaskLogForPo(ProCreation proCreation, PoStatusBean poStatus) {
		try {
			String message = "Process PoStatus Response from sap for servicecode " + proCreation.getServiceCode() + " task name"
					+ proCreation.getTaskName() + " ,Po status " + poStatus.getPOStatus() + "and po number"
					+ poStatus.getPONumber();

			Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
					proCreation.getServiceId(), proCreation.getTaskName());
			if (task != null) {
				createProcessTaskLog(task, "PO RELEASED", message, null,null);
			}
		} catch (Exception e) {
			LOGGER.info("saveTaskLogForPo for service code:{} and error:{}", proCreation.getServiceCode(), e);
		}

	}
	public void processMinResponse(MinResponseBean minResponseBean) throws TclCommonException {
		LOGGER.info("Entering processMinResponse... for MRN no:{} and response:{}",minResponseBean.getMinResponse().getmINNo(),Utils.convertObjectToJson(minResponseBean));
		if (Objects.nonNull(minResponseBean.getMinResponse())) {
			
			MinResponse minResponse = minResponseBean.getMinResponse();
			Optional<MrnCreationEntity> mrnCreationOp = mrnCreationRepository.findByMrnNumber(minResponse.getmRNNo());
			LOGGER.info("Entering processMinResponse... for MRN no :{}and is present:{}",minResponseBean.getMinResponse().getmINNo(),mrnCreationOp.isPresent());

			if(mrnCreationOp.isPresent()) {
				MrnCreationEntity mrnCreation = mrnCreationOp.get();
					
				 mrnCreation.setCourierName(minResponse.getCourierName());
				 mrnCreation.setMinNumer(minResponse.getmINNo());
				 mrnCreation.setVehicleDockertNumber(minResponse.getVehDocketNo());
				 mrnCreation.setMinStatus(minResponse.getStatus());
				 Integer serviceId = mrnCreation.getServiceId();
				 Optional.ofNullable(mrnCreationRepository.save(mrnCreation))
					.ifPresent(mrn -> {
						LOGGER.info("Triggering task {} for minNumber {}",mrn.getTaskName()+"_async",mrn.getMinNumer());
						Execution execution = runtimeService.createExecutionQuery().processInstanceId(mrn.getProcessInstanceId())
								.activityId(mrn.getTaskName() + "_async").singleResult();
						LOGGER.info("Entering execution... for MRN no :{}and is taskName:{} and ProcessinstanceId:{}",minResponseBean.getMinResponse().getmINNo(),mrn.getTaskName(),mrn.getProcessInstanceId());

							if (Objects.nonNull(execution)) {
								if (minResponse.getStatus().equalsIgnoreCase("Transferred")) {
									runtimeService.setVariable(execution.getId(), "minStatus", true);
									runtimeService.trigger(execution.getId());
								} else {
									runtimeService.setVariable(execution.getId(), "minStatus", false);
									runtimeService.trigger(execution.getId());
								}
							}
					});
				Map<String, String> mapper = new HashMap<>();
				mapper.put("muxMinNo", minResponse.getmINNo());
				mapper.put("muxMinStatus", minResponse.getStatus());
				componentAndAttributeService.updateAttributes(serviceId, mapper,AttributeConstants.COMPONENT_LM,"A");
				
				/*try {
					Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId, "provide-min");
					task.setAssignee("SYSTEM");
			
					task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
					task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
					task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
					taskRepository.save(task);
				}catch (Exception e) {
					LOGGER.error("exception provide-min updateTaskStatusToComplete{}", e);
				*/
				
			}
		
			
		}
	}



	public MrnCreationEntity saveMrnCreation(MrnCreationReponse mrnCreationReponse,ScServiceDetail scServiceDetail,String processInstanceId,String type, String taskName) {

		MrnCreationEntity mrnCreationEntity=new MrnCreationEntity();
		mrnCreationEntity.setMrnNumber(mrnCreationReponse.getMRNCREATION().getMRNNo());
		mrnCreationEntity.setServiceCode(scServiceDetail.getUuid());
		mrnCreationEntity.setServiceId(scServiceDetail.getId());
		mrnCreationEntity.setType(type);
		mrnCreationEntity.setTaskName(taskName);
		mrnCreationEntity.setProcessInstanceId(processInstanceId);
		mrnCreationEntity.setMrnStatus(mrnCreationReponse.getMRNCREATION().getStatus());
		return mrnCreationRepository.save(mrnCreationEntity);

	}

	public ProCreation saveProCreation(ScServiceDetail scServiceDetail,String processInstanceId,String type,String poNumber,String prNumber, String poStatus, Timestamp poCreatedDate, String taskName, String vendorCode) {

		ProCreation mrnCreationEntity=new ProCreation();
		mrnCreationEntity.setServiceCode(scServiceDetail.getUuid());
		mrnCreationEntity.setServiceId(scServiceDetail.getId());
		mrnCreationEntity.setType(type);
		mrnCreationEntity.setProcessInstanceId(processInstanceId);
		mrnCreationEntity.setPrNumber(prNumber);
		mrnCreationEntity.setPoStatus(poStatus);
		mrnCreationEntity.setTaskName(taskName);
		mrnCreationEntity.setPoNumber(poNumber);
		mrnCreationEntity.setPoCreatedDate(poCreatedDate);
		mrnCreationEntity.setVendorCode(vendorCode);
		return proCreationRepository.save(mrnCreationEntity);

	}
	
	public void saveOrUpdatePrCreation(String uuid,Integer serviceId,String processInstanceId,String type,String taskName,String prNumber,String prDate,String prCreatedType,String vendorCode,String vendorName, Integer cpeComponentId) {
		ProCreation proCreationEntity=proCreationRepository.findFirstByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndType(serviceId,uuid,vendorCode,cpeComponentId,type);
		if(proCreationEntity!=null){
			proCreationEntity.setProcessInstanceId(processInstanceId);
			proCreationEntity.setType(type);
			if(prNumber!=null){
				proCreationEntity.setPrNumber(prNumber);
			}
			if(vendorName!=null){
				proCreationEntity.setVendorName(vendorName);
			}
			if(prDate!=null){
				proCreationEntity.setPrCreatedDate(DateUtil.convertStringToTimestamp(prDate +" 00:00:00"));
			}
			if(prCreatedType!=null){
				proCreationEntity.setPrCreatedType(prCreatedType);
			}
			proCreationEntity.setTaskName(taskName);
			proCreationEntity.setPrStatus("Success");
			proCreationRepository.save(proCreationEntity);
		}else{
			proCreationEntity=new ProCreation();
			proCreationEntity.setServiceCode(uuid);
			proCreationEntity.setServiceId(serviceId);
			proCreationEntity.setVendorCode(vendorCode);
			proCreationEntity.setComponentId(cpeComponentId);
			proCreationEntity.setProcessInstanceId(processInstanceId);
			proCreationEntity.setType(type);
			if(prNumber!=null){
				proCreationEntity.setPrNumber(prNumber);
			}
			if(vendorName!=null){
				proCreationEntity.setVendorName(vendorName);
			}
			if(prDate!=null){
				proCreationEntity.setPrCreatedDate(DateUtil.convertStringToTimestamp(prDate +" 00:00:00"));
			}
			if(prCreatedType!=null){
				proCreationEntity.setPrCreatedType(prCreatedType);
			}
			proCreationEntity.setPrStatus("Success");
			proCreationEntity.setTaskName(taskName);
			proCreationRepository.save(proCreationEntity);
		}
	}
	
	public void updatePoCreation(String uuid,Integer serviceId,String type,String poNumber,String poDate,String vendorCode,String vendorName, Integer cpeComponentId) {
		ProCreation proCreationEntity=proCreationRepository.findFirstByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndType(serviceId,uuid,vendorCode,cpeComponentId,type);
		if(proCreationEntity!=null){
			if(poNumber!=null){
				proCreationEntity.setPoNumber(poNumber);
			}
			if(vendorName!=null){
				proCreationEntity.setVendorName(vendorName);
			}
			if(poDate!=null){
				proCreationEntity.setPoCreatedDate(DateUtil.convertStringToTimestamp(poDate +" 00:00:00"));
			}
			proCreationEntity.setPoStatus("Success");
			proCreationRepository.save(proCreationEntity);
		}
	}
	
	private boolean getCpeRentalQuantityAvailable(CableMaterialResponse sapQuantityResponse,
			Map<String, List<CableDisplayMaterial>> availableQuantityMap,
			Map<String, String> hardWareMaterialQuantityMapping,String level5Wbs,String serviceCode) throws TclCommonException {
		
		LOGGER.info("sap check inventory service code:{}cableAvailableQuantityMap:{} and hardWareMaterialQuantityMapping:{} and sapQuantityResponse:{} ",serviceCode,availableQuantityMap,hardWareMaterialQuantityMapping,Utils.convertObjectToJson(sapQuantityResponse));

		for (CableDisplayMaterial displayMaterialResponse : sapQuantityResponse.getDisplayMaterial()) {

			if (displayMaterialResponse.getStatus().equalsIgnoreCase("AVAILABLE") && displayMaterialResponse.getWBSNumber().equalsIgnoreCase(level5Wbs)) {
				if (availableQuantityMap.containsKey(displayMaterialResponse.getMaterialCode())) {
					List<CableDisplayMaterial> valueDisplayRespone = availableQuantityMap
							.get(displayMaterialResponse.getMaterialCode());
					valueDisplayRespone.add(displayMaterialResponse);
					availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);

				} else {
					List<CableDisplayMaterial> valueDisplayRespone = new ArrayList<>();
					valueDisplayRespone.add(displayMaterialResponse);
					availableQuantityMap.put(displayMaterialResponse.getMaterialCode(), valueDisplayRespone);
				}

			}

		}

		if (hardWareMaterialQuantityMapping.size() > 0) {

			for (Map.Entry<String, String> entry : hardWareMaterialQuantityMapping.entrySet()) {

				if (availableQuantityMap.containsKey(entry.getKey())) {

					if (availableQuantityMap.get(entry.getKey()).size() >= Integer.valueOf(entry.getValue())) {
						LOGGER.info("harware quantity for material code for request and reponse from sap size:{} and:{} ",
								entry.getValue(), availableQuantityMap.get(entry.getKey()).size());
					} else {
						return false;
					}

				} else {
					return false;
				}

			}
			return true;

		}

		return false;
	}
	
	/**
	 * 
	 * Process Auto po request for service ID
	 * 
	 * @author AnandhiV
	 * @param serviceCode
	 * @return
	 * @throws TclCommonException
	 */
	public String processOffnetAutoPo(Integer scServiceDetailId,DelegateExecution execution) throws TclCommonException {
		LOGGER.info("Inside process auto po for service id {}", scServiceDetailId);
		AutoPoRequest autoPoRequest = new AutoPoRequest();
		AutoPoResponse autoPoResponse = null;
		String errorMessage = "";
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetailId).get();
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());


			Map<String, String> prMapper = new HashMap<>();
			if (scServiceDetail != null) {
				String serviceCode = scServiceDetail.getUuid();
				LOGGER.info("Got inprogress service details for service id {}", serviceCode);
				Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
				Map<String, String> scOrderAttributes = commonFulfillmentUtils
						.getScOrderAttributes(scServiceDetail.getScOrder().getId());
				Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
						scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
				ScContractInfo scContractInfo = scContractInfoRepository
						.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
				Map<String, String> orderSubCategoryMap = commonFulfillmentUtils.orderSubcategorySapData();
				Map<String, String> sapInterfaceMap = commonFulfillmentUtils.sapInterfaceMappingData();
				Map<String, String> sapBuyerMap = commonFulfillmentUtils.sapBuyerMappingData();
				Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
				String upgradeType = StringUtils.trimToEmpty(scServiceAttributes.get("upgrade_type"));
				String tclServiceId=(scServiceDetail.getParentUuid() != null
						&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
						? scServiceDetail.getParentUuid()
						: scServiceDetail.getUuid();
				stg0SapPoDtlOptimus=getSapData(tclServiceId);

				if (scServiceAttributes.containsKey("feasibility_response_id")&&StringUtils.isNotBlank(scServiceAttributes.get("feasibility_response_id"))) {

					if (Objects.nonNull(scServiceDetail.getOrderSubCategory()) && (scServiceDetail.getOrderSubCategory().toLowerCase().contains("bso")
							|| scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel") 
							|| (StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)))) {
						/*stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
								.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
										((scServiceDetail.getParentUuid() != null
												&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
												? scServiceDetail.getParentUuid()
												: scServiceDetail.getUuid()),
										"A_END_LM");*/
						ParentPoBean parentPoData=getParentPoData(tclServiceId);
						updateParentPoDataForTermination(scServiceDetail,stg0SapPoDtlOptimus,parentPoData);
					}

					/*if (Objects.nonNull(stg0SapPoDtlOptimus)) {
						if (StringUtils.isNotBlank(stg0SapPoDtlOptimus.getVendorNo())) {
							List<Stg0SfdcVendorC> vendorList = stgSfdcVendorRepository.findByVendorIdC(stg0SapPoDtlOptimus.getVendorNo());
							Stg0SfdcVendorC vendor = (Objects.nonNull(vendorList) && !vendorList.isEmpty() ? vendorList.stream().findFirst().get() : null);
							if (Objects.nonNull(vendor)) {
								addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "vendorId", stg0SapPoDtlOptimus.getVendorNo());
								addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "vendorName", vendor.getName());
								addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sfdcProviderName", vendor.getSfdcProviderNameC());
								addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "oldOffnetPoNumber", stg0SapPoDtlOptimus.getPoNumber());
								addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "oldBsoCircuitId", stg0SapPoDtlOptimus.getVendorRefIdOrderId());
							}
						}
					}*/


					contructAutoPoRequest(autoPoRequest, scOrderAttributes, scComponentAttributesAMap, scServiceDetail,
							scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes, orderSubCategoryMap,
							sapInterfaceMap, sapBuyerMap, stg0SapPoDtlOptimus);
					LOGGER.info("Auto Po Request json {}", Utils.convertObjectToJson(autoPoRequest));
					try {
						RestResponse response = restClientService.postWithBasicAuthentication(sapAutoPoUrl,
								Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
						if (response != null) {
							LOGGER.info("RestResponse {}", response);
							if (response.getData() != null) {
								autoPoResponse = Utils.convertJsonToObject(response.getData(), AutoPoResponse.class);
								if (autoPoResponse != null && autoPoResponse.getPOResponse() != null) {
									if (autoPoResponse.getPOResponse().getPONumber() != null && !autoPoResponse
											.getPOResponse().getPONumber().equals(CommonConstants.EMPTY)) {
										LOGGER.info("PO number for service ID {} is {}", serviceCode,
												autoPoResponse.getPOResponse().getPONumber());
										addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sapPoNumber",
												autoPoResponse.getPOResponse().getPONumber());
									}
									if (autoPoResponse.getPOResponse().getPODate() != null
											&& !autoPoResponse.getPOResponse().getPODate().equals(CommonConstants.EMPTY)) {
										LOGGER.info("PO date for service ID {} is {}", serviceCode,
												autoPoResponse.getPOResponse().getPODate());
										addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sapPoDate",
												formatter
														.format(formatterPo.parse(
																autoPoResponse.getPOResponse().getPODate().toString()))
														.toString());
									}
								}
								AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPoRequest),
										response.getData(), serviceCode, "AUTOPO", "AutoPO" + serviceCode);
							}

						}
						if (response != null && response.getStatus() == Status.SUCCESS) {
							LOGGER.info("Success");
						}

					} catch (Exception e) {
						LOGGER.error("Error in processing po request {}", e);
					}


					LOGGER.info("Enter Execution Data" + execution);
				if (autoPoResponse != null && autoPoResponse.getPOResponse() != null && execution != null) {
					prMapper.put("offnetLocalPoNumber", autoPoResponse.getPOResponse().getPONumber());
					prMapper.put("offnetLocalPoStatus", autoPoResponse.getPOResponse().getPOStatus());
					prMapper.put("offnetLocalPoDate", DateUtil.convertDateToString(new Date()));

					execution.setVariable("offnetLocalPoNumber", autoPoResponse.getPOResponse().getPONumber());
					execution.setVariable("offnetLocalPoStatus", autoPoResponse.getPOResponse().getPOStatus());

					if (!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
						execution.setVariable("offnetPOCompleted", true);
					} else {
						execution.setVariable("offnetPOCompleted", false);
					}

					LOGGER.info("Execution Data" + execution);

					LOGGER.info("AutoPoResponse Data" + autoPoResponse);

					errorMessage = "Offnet PO Number:" + autoPoResponse.getPOResponse().getPONumber();
					errorMessage += ", Offnet PO Status:" + autoPoResponse.getPOResponse().getPOStatus();
                    componentAndAttributeService.updateAttributes(scServiceDetailId, prMapper,AttributeConstants.COMPONENT_LM,"A");
					LOGGER.info("PrMapper Data" + prMapper);
					errorMessage = autoPoResponse.getPOResponse().getRemark();

				} else if (execution != null) {
					execution.setVariable("offnetPOCompleted", false);
					errorMessage = autoPoResponse.getPOResponse().getRemark();
				}


				if (execution != null)
					saveProCreation(scServiceDetail, execution.getProcessInstanceId(), "OFFNETLM", autoPoResponse.getPOResponse().getPONumber(), null, autoPoResponse.getPOResponse().getPOStatus(), new Timestamp(new Date().getTime()), execution.getCurrentActivityId(),null);

			} else if (execution != null) {
				execution.setVariable("offnetPOCompleted", false);
				errorMessage = "Feasibility Response ID is missing from L2O";
				LOGGER.info("Error message for attribute missing :"+errorMessage);
			}
			}

		} catch (Exception e) {
			LOGGER.error("Error-creating-offnet-autopo-request {}", e);
		}
		return errorMessage;
	}

	private void contructAutoPoRequest(AutoPoRequest autoPoRequest, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
			Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus)throws TclCommonException {
		LOGGER.info("Inside contructAutoPoRequest");
		AutoPoMsgs autoPoMsgs = new AutoPoMsgs();
		contructAutoPoHeader(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
				scContractInfo, scServiceAttributes, orderSubCategoryMap, sapInterfaceMap, sapBuyerMap,
				stg0SapPoDtlOptimus);
		constructAutoPoLineItems(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
				scContractInfo, scServiceAttributes, orderSubCategoryMap, sapBuyerMap, stg0SapPoDtlOptimus);
		autoPoRequest.setPOMSGS(autoPoMsgs);
	}

	private void contructAutoPoHeader(AutoPoMsgs autoPoMsgs, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
			Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus)throws TclCommonException {
		LOGGER.info("Inside contructAutoPoHeader");
		String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

		AutoPoHeader autoPoHeader = new AutoPoHeader();
		autoPoHeader.setBILLINDCTR(CommonConstants.EMPTY);
		autoPoHeader.setBSART("BS");
		autoPoHeader.setBUKRS("VSIN");
		autoPoHeader.setCHILDPO((stg0SapPoDtlOptimus!=null && stg0SapPoDtlOptimus.getChildPoNumber()!=null)?StringUtils.trimToEmpty(stg0SapPoDtlOptimus.getChildPoNumber()):CommonConstants.EMPTY);
		autoPoHeader.setEBELN(CommonConstants.EMPTY);
		autoPoHeader.setEKGRP(CommonConstants.EMPTY);
		autoPoHeader.setEKORG("VSNL");
		
		String serviceType = orderSubCategoryMap.containsKey(
				scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory() : CommonConstants.NEW)
				? (orderSubCategoryMap.get(scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory()
				: CommonConstants.NEW))
				: CommonConstants.EMPTY;
				
		String upgradeType = StringUtils.trimToEmpty(scServiceAttributes.get("upgrade_type"));
		if(StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) {
			LOGGER.info("Parallel-Upgrade-Hence-Changing-the-service-type-to-NEW serviceId={}",scServiceDetail.getUuid() );
			serviceType="NEW";
		}
								
		autoPoHeader.setZZSRVTYPE(serviceType);

		String oldSiteAddress=null;
		if(StringUtils.isNotBlank(autoPoHeader.getZZSRVTYPE())&&autoPoHeader.getZZSRVTYPE().equalsIgnoreCase(CommonConstants.SHIFTING_LOCATION))
		{
			if(Objects.nonNull(scServiceDetail.getServiceLinkId()))
			{
				ScServiceDetail scServiceDetailData = scServiceDetailRepository.findById(scServiceDetail.getServiceLinkId()).get();
				oldSiteAddress = scServiceDetailData.getSiteAddress();
				LOGGER.info("Old Site Address-" + oldSiteAddress);
			}
		}

		String headerText="";
		if(StringUtils.isNotBlank(autoPoHeader.getZZSRVTYPE())&&autoPoHeader.getZZSRVTYPE().equalsIgnoreCase(CommonConstants.SHIFTING_LOCATION))
		{
			headerText="Old A-END : "
					.concat(Objects.nonNull(oldSiteAddress)?oldSiteAddress:CommonConstants.EMPTY)
					.concat("\n New A-END : ")
						.concat(Objects.nonNull(scOrder.getErfCustCustomerName()) ? scOrder.getErfCustCustomerName()+CommonConstants.SPACE:CommonConstants.EMPTY)
						.concat((scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
							: CommonConstants.EMPTY));
		}
		else
		headerText="A-END : "
				.concat(Objects.nonNull(scOrder.getErfCustCustomerName()) ? scOrder.getErfCustCustomerName()+CommonConstants.SPACE:CommonConstants.EMPTY)
				.concat((scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
						: CommonConstants.EMPTY));


		autoPoHeader.setHEADERTEXT(headerText
				.concat("\n B-END : ")
				.concat((scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address")
						: CommonConstants.EMPTY))
				.concat("\n Local Contact Name : ")
				.concat((scComponentAttributes.containsKey("localItContactName") ? scComponentAttributes.get("localItContactName")
						: CommonConstants.EMPTY))
				.concat("\n Local Contact Number : ")
				.concat((scComponentAttributes.containsKey("localItContactMobile") ? scComponentAttributes.get("localItContactMobile")
						: CommonConstants.EMPTY)));


		String vendorCode = scServiceAttributes.containsKey("vendor_id")
				? scServiceAttributes.get("vendor_id")
				: CommonConstants.EMPTY;
		autoPoHeader.setLIFNR(vendorCode); // Vendor Acc number
		autoPoHeader.setIHREZ(vendorCode.equals(CommonConstants.EMPTY) ? CommonConstants.EMPTY
				: (sapBuyerMap.containsKey(vendorCode) ? sapBuyerMap.get(vendorCode) : CommonConstants.EMPTY));
		autoPoHeader.setPOTYPE("LL");
		autoPoHeader.setREQTYPE("CREATE");
		autoPoHeader.setSCNROTYPE(CommonConstants.EMPTY);
		String providerReferenceNumber = scServiceAttributes.containsKey("provider_reference_number")
				? scServiceAttributes.get("provider_reference_number"): CommonConstants.EMPTY;
		autoPoHeader.setUNSEZ(providerReferenceNumber);
		autoPoHeader.setVERKF(CommonConstants.EMPTY);
		autoPoHeader
				.setWAERS(scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
						: CommonConstants.EMPTY);
		autoPoHeader.setZBANDWIDTH("LOCAL LOOP");
		
		String feasibilityId= scServiceAttributes.containsKey("feasibility_response_id")? scServiceAttributes.get("feasibility_response_id"): CommonConstants.EMPTY;
		if(StringUtils.isBlank(feasibilityId))feasibilityId=scServiceAttributes.containsKey("task_id")? scServiceAttributes.get("task_id"): CommonConstants.EMPTY;
		autoPoHeader.setZSFDCID(feasibilityId);
		autoPoHeader.setZSFDCOPPID(scOrder.getUuid());
		autoPoHeader.setZSITEA(CommonConstants.EMPTY);
		autoPoHeader.setZSITEB(CommonConstants.EMPTY);
		autoPoHeader.setZSPEED(
				scServiceAttributes.containsKey("local_loop_bw") ? scServiceAttributes.get("local_loop_bw").concat("M")
						: CommonConstants.EMPTY);
		autoPoHeader.setZTERM(CommonConstants.EMPTY);
		
		String mfContractTerm = StringUtils.trimToEmpty(scServiceAttributes.get("mf_contract_term"));
		String deal = mfContractTerm.replaceAll(" ", "").toLowerCase().replaceAll("months","-Months");
		
		autoPoHeader.setZTYPEDEAL(deal);
		autoPoHeader
				.setZZCIRCUIT(scServiceDetail.getErfPrdCatalogProductName() != null
						? scServiceDetail.getErfPrdCatalogProductName().equals("IAS") ? "ILL"
								: scServiceDetail.getErfPrdCatalogProductName()
						: CommonConstants.EMPTY);
		autoPoHeader.setZZCOPFID(scOrder.getUuid());
		String operatorName=(scServiceAttributes.containsKey("closest_provider_bso_name") ? scServiceAttributes.get("closest_provider_bso_name")
				: (scServiceAttributes.containsKey("vendor_name")
				? scServiceAttributes.get("vendor_name")
				: CommonConstants.EMPTY));

		autoPoHeader.setZZCPEOWNER(Objects.nonNull(operatorName) && (operatorName.equalsIgnoreCase("RADWIN WITH TTSL BH") ||
					operatorName.equalsIgnoreCase("RADWIN WITH TTML BH")) ?
					("BH") : ("NOT REQUIRED"));

		autoPoHeader.setZZCUST(scOrder.getErfCustCustomerName());
		autoPoHeader.setZZFESREQID(CommonConstants.EMPTY);
		String interfaceName=(scComponentAttributes.containsKey("interface")
				? (sapInterfaceMap.containsKey(scComponentAttributes.get("interface"))
				? sapInterfaceMap.get(scComponentAttributes.get("interface"))
				: scComponentAttributes.get("interface"))
				: CommonConstants.EMPTY);
		autoPoHeader
				.setZZINTTYPE(interfaceName.equalsIgnoreCase("FE")? "Fast Ethernet" : interfaceName);


		if(StringUtils.isNotBlank(scServiceDetail.getOrderSubCategory())&&!scServiceDetail.getOrderSubCategory().equalsIgnoreCase(CommonConstants.NEW))
			autoPoHeader.setZZOLDPONO((Objects.nonNull(orderCategory) && !orderCategory.equalsIgnoreCase("ADD_SITE") && stg0SapPoDtlOptimus!=null && stg0SapPoDtlOptimus.getPoNumber()!=null)?stg0SapPoDtlOptimus.getPoNumber():CommonConstants.EMPTY);
		else
			autoPoHeader.setZZOLDPONO(CommonConstants.EMPTY);
		autoPoHeader.setZZPOCATEGORY("NORMAL");
		autoPoHeader.setZZPRODCOMP("A_END_LM"); // OPTIMUS LOGIC - if ILL AND GVPN - DEFAULT TO A_END_LM , if NDE/NPL -
												// A_END_LM, B_END_LM
		autoPoHeader.setZZREVCURR(
				scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
						: CommonConstants.EMPTY);
		autoPoHeader.setZZREVMRCQUAN(CommonConstants.EMPTY);

		String arcValue = null;
		String lmType=scComponentAttributes.get("lmType");
		if(scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")&&!scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF")) {
			arcValue = scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
					? scServiceAttributes.get("lm_arc_bw_prov_ofrf")
					: null;
		} else if (scServiceAttributes.containsKey("lm_arc_bw_offwl")&&!scServiceAttributes.get("lm_arc_bw_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
			arcValue = scServiceAttributes.containsKey("lm_arc_bw_offwl") ? scServiceAttributes.get("lm_arc_bw_offwl")
					: null;
		}
		String mrcValue = CommonConstants.EMPTY;
		if (arcValue != null) {
			mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
		}
		autoPoHeader.setZZREVMRCVALUE(mrcValue);

		if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")&& !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF"))){
			autoPoHeader.setZZREVNRCVALUE(scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
					? scServiceAttributes.get("lm_nrc_bw_prov_ofrf")
					: CommonConstants.EMPTY);
		} else if (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")&& !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
			autoPoHeader.setZZREVNRCVALUE(scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
					? scServiceAttributes.get("lm_otc_nrc_installation_offwl")
					: CommonConstants.EMPTY);
		}

		autoPoHeader.setZZSERVICEID(scServiceDetail.getUuid());
		autoPoMsgs.setHEADER(autoPoHeader);
	}

	private void constructAutoPoLineItems(AutoPoMsgs autoPoMsgs, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> sapBuyerMap,
			Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) throws TclCommonException{
		LOGGER.info("Inside constructAutoPoLineItems");
		List<AutoPoLineItems> auLineItems = new ArrayList<>();
		AutoPoLineItems autoPoLineItems = new AutoPoLineItems();
		String vendorCode = scServiceAttributes.containsKey("vendor_id")
				? scServiceAttributes.get("vendor_id")
				: CommonConstants.EMPTY;
		autoPoLineItems.setAFNAM(vendorCode.equals(CommonConstants.EMPTY) ? CommonConstants.EMPTY
				: (sapBuyerMap.containsKey(vendorCode) ? sapBuyerMap.get(vendorCode) : CommonConstants.EMPTY));
		autoPoLineItems
				.setCUSTSEGMENT(
						scServiceAttributes.containsKey("customer_segment")
								? ((scServiceAttributes.get("customer_segment").split(" ").length > 0)
										? scServiceAttributes.get("customer_segment").split(" ")[0]
										: scServiceAttributes.get("customer_segment")).toUpperCase()
								: CommonConstants.EMPTY);
		autoPoLineItems.setEBELP(CommonConstants.EMPTY);
		autoPoLineItems.setEPSTP("D");
		autoPoLineItems.setKNTTP("K");
		autoPoLineItems.setKOSTL(CommonConstants.EMPTY);
		autoPoLineItems.setMATKL(CommonConstants.EMPTY);
		autoPoLineItems.setMENGE("1"); // OPTIMUS LOGIC DEFAULT =1
		autoPoLineItems.setSAKNR(CommonConstants.EMPTY);
		autoPoLineItems.setTXZ01("LM ".concat(scOrder.getErfCustCustomerName()));
		autoPoLineItems.setWERKS(CommonConstants.EMPTY);
		autoPoLineItems.setZCOMMDT(CommonConstants.EMPTY);
		autoPoLineItems.setZTERMDT(CommonConstants.EMPTY); // Customer termination date in case of MACD
		/*autoPoLineItems
				.setZVENREFNO((scOrder.getOrderType() == null || scOrder.getOrderType().equals(CommonConstants.NEW))
						? CommonConstants.EMPTY
						: ((stg0SapPoDtlOptimus != null && stg0SapPoDtlOptimus.getVendorRefIdOrderId() != null)
								? stg0SapPoDtlOptimus.getVendorRefIdOrderId()
								: CommonConstants.EMPTY));*/
		if(scServiceDetail.getOrderSubCategory()!=null&&(scServiceDetail.getOrderSubCategory().equalsIgnoreCase("Hot Upgrade")||scServiceDetail.getOrderSubCategory().equalsIgnoreCase("Hot Downgrade")||scServiceDetail.getOrderSubCategory().equalsIgnoreCase("LM Shifting")))
		{
			autoPoLineItems
					.setZVENREFNO((stg0SapPoDtlOptimus != null && stg0SapPoDtlOptimus.getVendorRefIdOrderId() != null)
							? stg0SapPoDtlOptimus.getVendorRefIdOrderId()
							: CommonConstants.EMPTY);
		}
		else
			autoPoLineItems.setZVENREFNO(CommonConstants.EMPTY);
		autoPoLineItems.setZZCHARD(
				scServiceAttributes.containsKey("chargeable_distance") ? scServiceAttributes.get("chargeable_distance")
						: "0");
		autoPoLineItems.setZZCOMMDT(CommonConstants.EMPTY);

		autoPoLineItems.setZZCRFSDT(scServiceDetail.getServiceCommissionedDate() != null
				? formatterPo.format(Timestamp.valueOf(scServiceDetail.getServiceCommissionedDate().toString()))
				: CommonConstants.EMPTY);
		autoPoLineItems.setZZFRMEND(
				scComponentAttributes.containsKey("destinationCity") ? scComponentAttributes.get("destinationCity")
						: CommonConstants.EMPTY);
		autoPoLineItems
				.setZZOPNAME(scServiceAttributes.containsKey("closest_provider_bso_name") ? scServiceAttributes.get("closest_provider_bso_name")
						: (scServiceAttributes.containsKey("vendor_name")
								? scServiceAttributes.get("vendor_name")
								: CommonConstants.EMPTY));
		autoPoLineItems.setZZRFSDT(scServiceDetail.getServiceCommissionedDate() != null
				? formatterPo.format(Timestamp.valueOf(commonFulfillmentUtils.getPreviousDate(scServiceDetail.getServiceCommissionedDate().toString())))
				: CommonConstants.EMPTY);
		autoPoLineItems.setZZTERMDT(scServiceDetail.getServiceTerminationDate() != null
				? formatterPo.format(Timestamp.valueOf(scServiceDetail.getServiceTerminationDate().toString()))
				: CommonConstants.EMPTY);
		autoPoLineItems.setZZTERMTY(CommonConstants.EMPTY);
		autoPoLineItems.setZZTOEND(
				scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address")
						: CommonConstants.EMPTY);
		Boolean isNrc = false;
		AutoPoLineItems autoPoLineItems2 = new AutoPoLineItems();
		AutoPoLineItems autoPoLineItems3 = new AutoPoLineItems();
		AutoPoLineItems autoPoLineItems4 = new AutoPoLineItems();
		String lmType=scComponentAttributes.get("lmType");
		if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
				&& !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF"))
				|| (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
						&& !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {

			BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems2);
			constructAutoPoServiceItemsNrc(autoPoLineItems2, scOrderAttributes, scComponentAttributes, scServiceDetail,
					scOrder, scContractInfo, scServiceAttributes);
			isNrc = true;
		}
		constructAutoPoServiceItemsArc(autoPoLineItems, scOrderAttributes, scComponentAttributes, scServiceDetail,
				scOrder, scContractInfo, scServiceAttributes);

		auLineItems.add(autoPoLineItems);
		if (isNrc) {
			auLineItems.add(autoPoLineItems2);
		}
		if ((scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
				&& !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0"))&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
			BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems3);
			constructAutoPoServiceItemsArcModem(autoPoLineItems3, scOrderAttributes, scComponentAttributes, scServiceDetail,
					scOrder, scContractInfo, scServiceAttributes);
			auLineItems.add(autoPoLineItems3);
		}
		if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
				&& !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems4);
			constructAutoPoServiceItemsOtcModem(autoPoLineItems4, scOrderAttributes, scComponentAttributes, scServiceDetail,
					scOrder, scContractInfo, scServiceAttributes);
			auLineItems.add(autoPoLineItems4);
		}

		autoPoMsgs.setLINEITEMS(auLineItems);
	}

	private void constructAutoPoServiceItemsArc(AutoPoLineItems autoPoLineItems, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
				|| scServiceAttributes.containsKey("lm_arc_bw_offwl")) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM("MON");
			String deliveryDate = CommonConstants.EMPTY;
			if (scServiceDetail.getServiceCommissionedDate() != null) {
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(formatter.parse(formatter.format(scServiceDetail.getServiceCommissionedDate())));
					cal.before(7);
					deliveryDate = formatter.format(cal.getTime());
				} catch (Exception e) {
					LOGGER.info("Error on parsing the date");
				}

			}
			autoPoServiceItems.setEEIND(deliveryDate);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM("35000010");
			autoPoServiceItems.setQUANTITY("12");

			String arcValue = null;
			String lmType=scComponentAttributes.get("lmType");
			if(scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")&&!scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF")) {
				arcValue = scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
						? scServiceAttributes.get("lm_arc_bw_prov_ofrf")
						: null;
			}else if(scServiceAttributes.containsKey("lm_arc_bw_offwl")&&!scServiceAttributes.get("lm_arc_bw_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
				arcValue = scServiceAttributes.containsKey("lm_arc_bw_offwl") ? scServiceAttributes.get("lm_arc_bw_offwl")
						: null;
			}
			String mrcValue = CommonConstants.EMPTY;
			if (arcValue != null) {
				mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
			}

			if(scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")&&!scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF")) {
				autoPoServiceItems.setTBTWR(scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
						? mrcValue
						: CommonConstants.EMPTY);
			} else if(scServiceAttributes.containsKey("lm_arc_bw_offwl")&&!scServiceAttributes.get("lm_arc_bw_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
				autoPoServiceItems.setTBTWR(
						scServiceAttributes.containsKey("lm_arc_bw_offwl") ? mrcValue
								: CommonConstants.EMPTY);
			}

			autoPoServiceItems.setWAERS(
					scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
							: CommonConstants.EMPTY);
			try {
			autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
					? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
					: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoServiceItemsList.add(autoPoServiceItems);
		}

		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}

	private void constructAutoPoServiceItemsNrc(AutoPoLineItems autoPoLineItems, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		String lmType=scComponentAttributes.get("lmType");
		if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
				&& !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF"))
				|| (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
						&& !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM("EA");
			String deliveryDate = CommonConstants.EMPTY;
			if (scServiceDetail.getServiceCommissionedDate() != null) {
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(formatter.parse(formatter.format(scServiceDetail.getServiceCommissionedDate())));
					cal.add(cal.getTime().getDate(), -7);
					deliveryDate = formatter.format(cal.getTime());
				} catch (Exception e) {
					LOGGER.info("Error on parsing the date");
				}

			}
			autoPoServiceItems.setEEIND(deliveryDate);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM("36000000");
			autoPoServiceItems.setQUANTITY("1");
			if (scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")&& !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF")) {
				autoPoServiceItems.setTBTWR(scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
						? scServiceAttributes.get("lm_nrc_bw_prov_ofrf")
						: CommonConstants.EMPTY);
			} else if (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")&& !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
				autoPoServiceItems.setTBTWR(
						scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl") ? scServiceAttributes.get("lm_otc_nrc_installation_offwl")
								: CommonConstants.EMPTY);
			}
			autoPoServiceItems.setWAERS(
					scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
							: CommonConstants.EMPTY);
			try {
			autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
					? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
					: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoServiceItemsList.add(autoPoServiceItems);
		}

		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}

	public AutoPoRequest getAutoPORequest(String serviceCode) throws TclCommonException {
		LOGGER.info("Inside process auto po for service id {}", serviceCode);
		AutoPoRequest autoPoRequest = new AutoPoRequest();
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,
					"INPROGRESS");

			if (scServiceDetail != null) {
				LOGGER.info("Got inprogress service details for service id {}", serviceCode);
				Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
				Map<String, String> scOrderAttributes = commonFulfillmentUtils
						.getScOrderAttributes(scServiceDetail.getScOrder().getId());
				Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
						scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
				ScContractInfo scContractInfo = scContractInfoRepository
						.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
				Map<String, String> orderSubCategoryMap = commonFulfillmentUtils.orderSubcategorySapData();
				Map<String, String> sapInterfaceMap = commonFulfillmentUtils.sapInterfaceMappingData();
				Map<String, String> sapBuyerMap = commonFulfillmentUtils.sapBuyerMappingData();
				/*Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
						.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
								(scServiceDetail.getParentUuid() != null
										&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
												? scServiceDetail.getParentUuid()
												: scServiceDetail.getUuid(),
								"A_END_LM");*/

				String tclServiceId=(scServiceDetail.getParentUuid() != null
						&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
						? scServiceDetail.getParentUuid()
						: scServiceDetail.getUuid();
				Stg0SapPoDtlOptimus stg0SapPoDtlOptimus=getSapData(tclServiceId);
				ParentPoBean parentPoData=getParentPoData(tclServiceId);
				updateParentPoDataForTermination(scServiceDetail,stg0SapPoDtlOptimus,parentPoData);
				contructAutoPoRequest(autoPoRequest, scOrderAttributes, scComponentAttributesAMap, scServiceDetail,
						scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes, orderSubCategoryMap,
						sapInterfaceMap, sapBuyerMap, stg0SapPoDtlOptimus);

				LOGGER.info("Auto Po Request json {}", Utils.convertObjectToJson(autoPoRequest));
			}
		} catch (Exception e) {
			LOGGER.info("Error on creating auto po request {}", e.getMessage());
		}
		return autoPoRequest;
	}

	private void contructAutoPoRequestUpdate(AutoPoRequest autoPoRequest, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
			Map<String, String> sapBuyerMap){
		LOGGER.info("Inside contructAutoPoRequest");
		AutoPoMsgs autoPoMsgs = new AutoPoMsgs();
		contructAutoPoHeaderUpdate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
				scContractInfo, scServiceAttributes, orderSubCategoryMap, sapInterfaceMap, sapBuyerMap);
		constructAutoPoLineItemsUpdate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
				scContractInfo, scServiceAttributes, orderSubCategoryMap, sapBuyerMap);
		autoPoRequest.setPOMSGS(autoPoMsgs);
	}

	private void contructAutoPoHeaderUpdate(AutoPoMsgs autoPoMsgs, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
			Map<String, String> sapBuyerMap) {
		LOGGER.info("Inside contructAutoPoHeader");
		AutoPoHeader autoPoHeader = new AutoPoHeader();
		autoPoHeader.setBILLINDCTR(CommonConstants.EMPTY);
		autoPoHeader.setBSART("BS");
		autoPoHeader.setBUKRS(CommonConstants.EMPTY);
		autoPoHeader.setCHILDPO(CommonConstants.EMPTY);
		autoPoHeader.setEBELN(scComponentAttributes.containsKey("offnetLocalPoNumber") ? StringUtils.trimToEmpty(scComponentAttributes.get("offnetLocalPoNumber"))
				: CommonConstants.EMPTY);
		autoPoHeader.setEKGRP(CommonConstants.EMPTY);
		autoPoHeader.setEKORG(CommonConstants.EMPTY);
		autoPoHeader.setHEADERTEXT(CommonConstants.EMPTY);
		autoPoHeader.setLIFNR(CommonConstants.EMPTY); // Vendor Acc number
		autoPoHeader.setIHREZ(CommonConstants.EMPTY);
		autoPoHeader.setPOTYPE(CommonConstants.EMPTY);
		autoPoHeader.setREQTYPE("UPDATE");
		autoPoHeader.setSCNROTYPE("COMM");
		autoPoHeader.setUNSEZ(CommonConstants.EMPTY); // Supplier Feasibility ID
		autoPoHeader.setVERKF(CommonConstants.EMPTY);
		autoPoHeader
				.setWAERS(CommonConstants.EMPTY);
		autoPoHeader.setZBANDWIDTH(CommonConstants.EMPTY);
		String feasibilityId= scServiceAttributes.containsKey("feasibility_response_id")? scServiceAttributes.get("feasibility_response_id"): CommonConstants.EMPTY;
		if(StringUtils.isBlank(feasibilityId))feasibilityId=scServiceAttributes.containsKey("task_id")? scServiceAttributes.get("task_id"): CommonConstants.EMPTY;
		autoPoHeader.setZSFDCID(feasibilityId);
		autoPoHeader.setZSFDCOPPID(scOrder.getUuid());
		autoPoHeader.setZSITEA(CommonConstants.EMPTY);
		autoPoHeader.setZSITEB(CommonConstants.EMPTY);
		autoPoHeader.setZSPEED(CommonConstants.EMPTY);
		autoPoHeader.setZTERM(CommonConstants.EMPTY);
		autoPoHeader.setZTYPEDEAL(CommonConstants.EMPTY);
		autoPoHeader
				.setZZCIRCUIT(scServiceDetail.getErfPrdCatalogProductName() != null
						? scServiceDetail.getErfPrdCatalogProductName().equals("IAS") ? "ILL"
								: scServiceDetail.getErfPrdCatalogProductName()
						: CommonConstants.EMPTY);
		autoPoHeader.setZZCOPFID(scOrder.getUuid());
		String operatorName=(scServiceAttributes.containsKey("closest_provider_bso_name") ? scServiceAttributes.get("closest_provider_bso_name")
				: (scServiceAttributes.containsKey("vendor_name")
				? scServiceAttributes.get("vendor_name")
				: CommonConstants.EMPTY));

		autoPoHeader.setZZCPEOWNER(Objects.nonNull(operatorName) && (operatorName.equalsIgnoreCase("RADWIN WITH TTSL BH") ||
				operatorName.equalsIgnoreCase("RADWIN WITH TTML BH")) ?
				("BH") : ("NOT REQUIRED"));
		autoPoHeader.setZZCUST(CommonConstants.EMPTY);
		autoPoHeader.setZZFESREQID(CommonConstants.EMPTY);
		autoPoHeader
				.setZZINTTYPE(CommonConstants.EMPTY);
		autoPoHeader.setZZOLDPONO(CommonConstants.EMPTY);
		autoPoHeader.setZZPOCATEGORY(CommonConstants.EMPTY);
		autoPoHeader.setZZPRODCOMP(CommonConstants.EMPTY); // OPTIMUS LOGIC - if ILL AND GVPN - DEFAULT TO A_END_LM , if NDE/NPL -
												// A_END_LM, B_END_LM
		autoPoHeader.setZZREVCURR(CommonConstants.EMPTY);
		autoPoHeader.setZZREVMRCQUAN(CommonConstants.EMPTY);
		autoPoHeader.setZZREVMRCVALUE(CommonConstants.EMPTY);
		autoPoHeader.setZZSERVICEID(scServiceDetail.getUuid());
		autoPoHeader.setZZSRVTYPE(CommonConstants.EMPTY);
		autoPoMsgs.setHEADER(autoPoHeader);
	}

	private void constructAutoPoLineItemsUpdate(AutoPoMsgs autoPoMsgs, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> sapBuyerMap){
		LOGGER.info("Inside constructAutoPoLineItems");
		List<AutoPoLineItems> auLineItems = new ArrayList<>();
		AutoPoLineItems autoPoLineItems = new AutoPoLineItems();
		autoPoLineItems.setAFNAM(CommonConstants.EMPTY);
		autoPoLineItems
				.setCUSTSEGMENT(CommonConstants.EMPTY);
		autoPoLineItems.setEBELP("0010");
		autoPoLineItems.setEPSTP(CommonConstants.EMPTY);
		autoPoLineItems.setKNTTP(CommonConstants.EMPTY);
		autoPoLineItems.setKOSTL(CommonConstants.EMPTY);
		autoPoLineItems.setMATKL(CommonConstants.EMPTY);
		autoPoLineItems.setMENGE("1"); // OPTIMUS LOGIC DEFAULT =1
		autoPoLineItems.setSAKNR(CommonConstants.EMPTY);
		autoPoLineItems.setTXZ01(CommonConstants.EMPTY);
		autoPoLineItems.setWERKS(CommonConstants.EMPTY);
		try {
		autoPoLineItems.setZCOMMDT(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
				? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
				: CommonConstants.EMPTY);
		} catch (Exception e) {
			LOGGER.info("Error on parsing the date");
		}
		autoPoLineItems.setZTERMDT(CommonConstants.EMPTY); // Customer termination date in case of MACD
		/*autoPoLineItems.setZVENREFNO(scOrder.getOrderCategory().equals(CommonConstants.NEW) ? CommonConstants.EMPTY
				: (scServiceAttributes.containsKey("provider_reference_number")
						? scServiceAttributes.get("provider_reference_number")
						: CommonConstants.EMPTY));*/
		autoPoLineItems.setZVENREFNO(scComponentAttributes.containsKey("bsoCircuitId")
				? scComponentAttributes.get("bsoCircuitId")
				: CommonConstants.EMPTY);
		autoPoLineItems.setZZCHARD(CommonConstants.EMPTY);
		try {
		autoPoLineItems.setZZCOMMDT(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
				? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
				: CommonConstants.EMPTY);
		} catch (Exception e) {
			LOGGER.info("Error on parsing the date");
		}
		autoPoLineItems.setZZCRFSDT(CommonConstants.EMPTY);
		autoPoLineItems.setZZFRMEND(CommonConstants.EMPTY);
		autoPoLineItems
				.setZZOPNAME(CommonConstants.EMPTY);
		autoPoLineItems.setZZRFSDT(CommonConstants.EMPTY);
		autoPoLineItems.setZZTERMDT(CommonConstants.EMPTY);
		autoPoLineItems.setZZTERMTY(CommonConstants.EMPTY);
		autoPoLineItems.setZZTOEND(CommonConstants.EMPTY);
		Boolean isNrc = false;
		AutoPoLineItems autoPoLineItems2 = new AutoPoLineItems();
		AutoPoLineItems autoPoLineItems3 = new AutoPoLineItems();
		AutoPoLineItems autoPoLineItems4 = new AutoPoLineItems();
		String lmType=scComponentAttributes.get("lmType");
		
		
		if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
				&& !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF"))
				|| (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
						&& !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {

			BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems2);
			constructAutoPoServiceItemsNrcUpdate(autoPoLineItems2, scOrderAttributes, scComponentAttributes, scServiceDetail,
					scOrder, scContractInfo, scServiceAttributes);
			isNrc = true;
		}
		constructAutoPoServiceItemsArcUpdate(autoPoLineItems, scOrderAttributes, scComponentAttributes, scServiceDetail,
				scOrder, scContractInfo, scServiceAttributes);
		auLineItems.add(autoPoLineItems);
		if (isNrc) {
			auLineItems.add(autoPoLineItems2);
		}

		if ((scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
				&& !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems3);
			constructAutoPoServiceItemsArcModemUpdate(autoPoLineItems3, scOrderAttributes, scComponentAttributes, scServiceDetail,
					scOrder, scContractInfo, scServiceAttributes);
			auLineItems.add(autoPoLineItems3);
		}
		if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
				&& !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems4);
			constructAutoPoServiceItemsOtcModemUpdate(autoPoLineItems4, scOrderAttributes, scComponentAttributes, scServiceDetail,
					scOrder, scContractInfo, scServiceAttributes);
			auLineItems.add(autoPoLineItems4);
		}
		autoPoMsgs.setLINEITEMS(auLineItems);
	}

	private void constructAutoPoServiceItemsArcUpdate(AutoPoLineItems autoPoLineItems,
			Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
			ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
			Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
				|| scServiceAttributes.containsKey("lm_arc_bw_offwl")) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
			autoPoServiceItems.setEEIND(CommonConstants.EMPTY);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM(CommonConstants.EMPTY);
			autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
			autoPoServiceItems.setTBTWR(CommonConstants.EMPTY);
			autoPoServiceItems.setWAERS(CommonConstants.EMPTY);


			try {
			autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
					? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
					: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoServiceItemsList.add(autoPoServiceItems);
		}

		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}

	private void constructAutoPoServiceItemsNrcUpdate(AutoPoLineItems autoPoLineItems,
			Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
			ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
			Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		String lmType=scComponentAttributes.get("lmType");
		if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
				&& !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetRF"))
				|| (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
						&& !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
			autoPoServiceItems.setEEIND(CommonConstants.EMPTY);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM(CommonConstants.EMPTY);
			autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
			autoPoServiceItems.setTBTWR(CommonConstants.EMPTY);
			autoPoServiceItems.setWAERS(CommonConstants.EMPTY);
			try {
			autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
					? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
					: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoLineItems.setEBELP("0020");
			autoPoServiceItemsList.add(autoPoServiceItems);
		}

		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}

	private void contructAutoPoRequestTerminate(AutoPoRequest autoPoRequest, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> terminateTypeMap,Map<String, String> sapInterfaceMap,
			Map<String, String> sapBuyerMap) {
		LOGGER.info("Inside contructAutoPoRequest for terminatePO");
		AutoPoMsgs autoPoMsgs = new AutoPoMsgs();
		contructAutoPoHeaderTerminate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
				scContractInfo, scServiceAttributes, orderSubCategoryMap, sapInterfaceMap, sapBuyerMap);
		constructAutoPoLineItemsTerminate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail,
				scOrder, scContractInfo, scServiceAttributes, orderSubCategoryMap,terminateTypeMap, sapBuyerMap);
		autoPoRequest.setPOMSGS(autoPoMsgs);
	}

	private void contructAutoPoHeaderTerminate(AutoPoMsgs autoPoMsgs, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
			Map<String, String> sapBuyerMap) {
		LOGGER.info("Inside contructAutoPoHeader");
		AutoPoHeader autoPoHeader = new AutoPoHeader();
		autoPoHeader.setBILLINDCTR(CommonConstants.EMPTY);
		autoPoHeader.setBSART("BS");
		autoPoHeader.setBUKRS(CommonConstants.EMPTY);
		autoPoHeader.setCHILDPO(CommonConstants.EMPTY);
		autoPoHeader.setEBELN(scComponentAttributes.containsKey("oldOffnetPoNumber") ? StringUtils.trimToEmpty(scComponentAttributes.get("oldOffnetPoNumber"))
				: CommonConstants.EMPTY);
		autoPoHeader.setEKGRP(CommonConstants.EMPTY);
		autoPoHeader.setEKORG(CommonConstants.EMPTY);
		autoPoHeader.setHEADERTEXT(CommonConstants.EMPTY);
		autoPoHeader.setLIFNR(CommonConstants.EMPTY); // Vendor Acc number
		autoPoHeader.setIHREZ(CommonConstants.EMPTY);
		autoPoHeader.setPOTYPE(CommonConstants.EMPTY);
		autoPoHeader.setREQTYPE("UPDATE");
		autoPoHeader.setSCNROTYPE("TERM");
		autoPoHeader.setUNSEZ(CommonConstants.EMPTY); // Supplier Feasibility ID
		autoPoHeader.setVERKF(CommonConstants.EMPTY);
		autoPoHeader
				.setWAERS(CommonConstants.EMPTY);
		autoPoHeader.setZBANDWIDTH(CommonConstants.EMPTY);
		autoPoHeader.setZSFDCID(CommonConstants.EMPTY);
		autoPoHeader.setZSFDCOPPID(CommonConstants.EMPTY);
		autoPoHeader.setZSITEA(CommonConstants.EMPTY);
		autoPoHeader.setZSITEB(CommonConstants.EMPTY);
		autoPoHeader.setZSPEED(CommonConstants.EMPTY);
		autoPoHeader.setZTERM(CommonConstants.EMPTY);
		autoPoHeader.setZTYPEDEAL(CommonConstants.EMPTY);
		autoPoHeader
				.setZZCIRCUIT(CommonConstants.EMPTY);
		autoPoHeader.setZZCOPFID(scOrder.getUuid());
		autoPoHeader.setZZCPEOWNER(CommonConstants.EMPTY);
		autoPoHeader.setZZCUST(CommonConstants.EMPTY);
		autoPoHeader.setZZFESREQID(CommonConstants.EMPTY);
		autoPoHeader
				.setZZINTTYPE(CommonConstants.EMPTY);
		autoPoHeader.setZZOLDPONO(CommonConstants.EMPTY);
		autoPoHeader.setZZPOCATEGORY(CommonConstants.EMPTY);
		autoPoHeader.setZZPRODCOMP(CommonConstants.EMPTY); // OPTIMUS LOGIC - if ILL AND GVPN - DEFAULT TO A_END_LM , if NDE/NPL -
		// A_END_LM, B_END_LM
		autoPoHeader.setZZREVCURR(CommonConstants.EMPTY);
		autoPoHeader.setZZREVMRCQUAN(CommonConstants.EMPTY);
		autoPoHeader.setZZREVMRCVALUE(CommonConstants.EMPTY);


		if (scServiceDetail.getTerminationFlowTriggered() != null
				&& scServiceDetail.getTerminationFlowTriggered().equalsIgnoreCase("Yes")) {
			autoPoHeader.setZZSERVICEID(scServiceDetail.getUuid());

		} else {

			if(scServiceDetail.getOrderSubCategory().contains("Parallel"))
				autoPoHeader.setZZSERVICEID(scServiceDetail.getParentUuid());
			else
				autoPoHeader.setZZSERVICEID(scServiceDetail.getUuid());

		}

		autoPoHeader.setZZSRVTYPE(CommonConstants.EMPTY);
		autoPoMsgs.setHEADER(autoPoHeader);
	}

	private void constructAutoPoLineItemsTerminate(AutoPoMsgs autoPoMsgs, Map<String, String> scOrderAttributes,
			Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
			ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
			Map<String, String> orderSubCategoryMap, Map<String, String> terminateTypeMap,Map<String, String> sapBuyerMap) {
		LOGGER.info("Inside constructAutoPoLineItems");
		
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "oldBsoCircuitId", AttributeConstants.COMPONENT_LM,
						AttributeConstants.SITETYPE_A);
		if(scComponentAttribute != null) {
			List<Stg0SapPoDtlOptimus> stg0SapPoDtlOptimuses = stg0SapPoDtlOptimusRepository
					.findByVendorRefIdOrderIdAndTclServiceIdAndTerminationTypeIsNull(scComponentAttribute.getAttributeValue(),
							scServiceDetail.getUuid());
			if(stg0SapPoDtlOptimuses != null && !stg0SapPoDtlOptimuses.isEmpty()) {
				List<AutoPoLineItems> auLineItems = new ArrayList<>();
				for (Stg0SapPoDtlOptimus stg0SapPoDtlOptimus : stg0SapPoDtlOptimuses) {
					AutoPoLineItems autoPoLineItems = new AutoPoLineItems();
					autoPoLineItems.setAFNAM(CommonConstants.EMPTY);
					autoPoLineItems
					.setCUSTSEGMENT(CommonConstants.EMPTY);
					autoPoLineItems.setEBELP(stg0SapPoDtlOptimus.getPoLineNo());
					autoPoLineItems.setEPSTP(CommonConstants.EMPTY);
					autoPoLineItems.setKNTTP(CommonConstants.EMPTY);
					autoPoLineItems.setKOSTL(CommonConstants.EMPTY);
					autoPoLineItems.setMATKL(CommonConstants.EMPTY);
					autoPoLineItems.setMENGE("1"); // OPTIMUS LOGIC DEFAULT =1
					autoPoLineItems.setSAKNR(CommonConstants.EMPTY);
					autoPoLineItems.setTXZ01(CommonConstants.EMPTY);
					autoPoLineItems.setWERKS(CommonConstants.EMPTY);
					autoPoLineItems.setZCOMMDT(CommonConstants.EMPTY);
					try{
						autoPoLineItems.setZTERMDT(scComponentAttributes.containsKey("terminationDate")
								? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("terminationDate"),formatterCdate))
										: CommonConstants.EMPTY);
					} catch (Exception e) {
						LOGGER.info("Error on parsing the date");
					}
					// Customer termination date in case of MACD
					/*autoPoLineItems.setZVENREFNO(scOrder.getOrderCategory().equals(CommonConstants.NEW) ? CommonConstants.EMPTY
				: (scServiceAttributes.containsKey("provider_reference_number")
						? scServiceAttributes.get("provider_reference_number")
						: CommonConstants.EMPTY));*/
					autoPoLineItems.setZVENREFNO(CommonConstants.EMPTY);
					autoPoLineItems.setZZCHARD(CommonConstants.EMPTY);
					autoPoLineItems.setZZCOMMDT(CommonConstants.EMPTY);
					autoPoLineItems.setZZCRFSDT(CommonConstants.EMPTY);
					autoPoLineItems.setZZFRMEND(CommonConstants.EMPTY);
					autoPoLineItems
					.setZZOPNAME(CommonConstants.EMPTY);
					autoPoLineItems.setZZRFSDT(CommonConstants.EMPTY);
					try{
						autoPoLineItems.setZZTERMDT(scComponentAttributes.containsKey("terminationDate")
								? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("terminationDate"),formatterCdate))
										: CommonConstants.EMPTY);
						//Setting termination date for offnet PO
						if (scServiceDetail.getTerminationFlowTriggered() != null
								&& scServiceDetail.getTerminationFlowTriggered().equalsIgnoreCase(CommonConstants.YES)
								&& scServiceDetail.getTerminationEffectiveDate()!=null) {
							LOGGER.info("Termination date for Offnet PO {} :",scServiceDetail.getTerminationEffectiveDate());
							String terminationDate= formatterCdate.format(scServiceDetail.getTerminationEffectiveDate());
							autoPoLineItems.setZZTERMDT(terminationDate!=null
									? formatterPo.format(commonFulfillmentUtils.getDate(terminationDate,formatterCdate))
											: CommonConstants.EMPTY);
							autoPoLineItems.setZTERMDT(terminationDate!=null
									? formatterPo.format(commonFulfillmentUtils.getDate(terminationDate,formatterCdate))
											: CommonConstants.EMPTY);
						}
					} catch (Exception e) {
						LOGGER.info("Error on parsing the date");
					}
					String terminationType="";
					String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
					
					if((Objects.nonNull(orderCategory)&&(orderCategory.equalsIgnoreCase("CHANGE_BANDWIDTH")||orderCategory.equalsIgnoreCase("SHIFT_SITE"))&&scServiceDetail.getOrderSubCategory().contains("Hot")&&scServiceDetail.getOrderSubCategory().contains("BSO Change")))
					{
						LOGGER.info("Entering Hot Upgrade-Bso Change case");
						terminationType=getChangeBWOrShiftSiteHotUpgradeTerminationType(scServiceDetail.getOrderSubCategory(),orderCategory);
					}
					else if(scServiceDetail.getOrderSubCategory().contains("Parallel"))
					{
						LOGGER.info("Entering Parallel case");
						boolean bsoFlag=isSameBSOorDifferent(scComponentAttributes.get("vendorId"),scServiceAttributes.get("vendor_id"));
						terminationType=getTerminationTypeBasedOnBSOStatus(scServiceDetail.getOrderSubCategory(),bsoFlag);
					}
					else
					{
						LOGGER.info("Entering other cases");
						terminationType=terminateTypeMap.get(scServiceDetail.getOrderSubCategory());
					}
					LOGGER.info("Termination Type:"+terminationType);
					autoPoLineItems.setZZTERMTY(terminationType);
					autoPoLineItems.setZZTOEND(CommonConstants.EMPTY);
					constructAutoPoServiceItemsArcTerminate(autoPoLineItems, scOrderAttributes, scComponentAttributes, scServiceDetail,
							scOrder, scContractInfo, scServiceAttributes);
					auLineItems.add(autoPoLineItems);
				}
				autoPoMsgs.setLINEITEMS(auLineItems);
			}
		}
		
	}

	private void constructAutoPoServiceItemsArcTerminate(AutoPoLineItems autoPoLineItems,
			Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
			ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
			Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();

			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
			autoPoServiceItems.setEEIND(CommonConstants.EMPTY);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM(CommonConstants.EMPTY);
			autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
			autoPoServiceItems.setTBTWR(CommonConstants.EMPTY);
			autoPoServiceItems.setWAERS(CommonConstants.EMPTY);
			autoPoServiceItems.setZZCDATE(CommonConstants.EMPTY);
			try {
				autoPoServiceItems.setZZDDATE(scComponentAttributes.containsKey("terminationDate")
						? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("terminationDate"), formatterCdate))
						: CommonConstants.EMPTY); // Need to send the value if MACD
				if (scServiceDetail.getTerminationFlowTriggered() != null
						&& scServiceDetail.getTerminationFlowTriggered().equalsIgnoreCase(CommonConstants.YES)
						&& scServiceDetail.getTerminationEffectiveDate()!=null) {
					String terminationDate= formatterCdate.format(scServiceDetail.getTerminationEffectiveDate());
					autoPoServiceItems.setZZDDATE(terminationDate!=null
							? formatterPo.format(commonFulfillmentUtils.getDate(terminationDate,formatterCdate))
							: CommonConstants.EMPTY);
				}

			} catch (Exception e) {
			LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItemsList.add(autoPoServiceItems);
			autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}



	public String processOffnetAutoPoUpdate(Integer serviceId,DelegateExecution execution) {
		
			LOGGER.info("Inside processOffnetAutoPoUpdate serviceid= {}", serviceId);
			AutoPoRequest autoPoRequest = new AutoPoRequest();
			AutoPoResponse autoPoResponse = null;
			String errorMessage = "";
			

			try {
				ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
				String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());

			
				Map<String, String> prMapper = new HashMap<>();
				if (scServiceDetail != null) {
					String serviceCode = scServiceDetail.getUuid();
					LOGGER.info("Got processOffnetAutoPoUpdate serviceCode {}", serviceCode);

					if(execution.getVariable("skipOffnet").equals(false))
					{
						Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
								.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
						Map<String, String> scOrderAttributes = commonFulfillmentUtils
								.getScOrderAttributes(scServiceDetail.getScOrder().getId());
						Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
								scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
						ScContractInfo scContractInfo = scContractInfoRepository
								.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
						Map<String, String> orderSubCategoryMap = commonFulfillmentUtils.orderSubcategorySapData();
						Map<String, String> sapInterfaceMap = commonFulfillmentUtils.sapInterfaceMappingData();
						Map<String, String> sapBuyerMap = commonFulfillmentUtils.sapBuyerMappingData();
						contructAutoPoRequestUpdate(autoPoRequest, scOrderAttributes, scComponentAttributesAMap,
								scServiceDetail, scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes,
								orderSubCategoryMap, sapInterfaceMap, sapBuyerMap);
					}
					else if(execution.getVariable("skipOffnet").equals(true))
					{
						//:To dont know why compared new with order category as of now im using the same
						Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
						if (orderCategory!=null && !orderCategory.equals(CommonConstants.NEW)) {
							/*stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
									.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
											((scServiceDetail.getParentUuid() != null
													&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
													? scServiceDetail.getParentUuid()
													: scServiceDetail.getUuid()),
											"A_END_LM");*/
							String tclServiceId=(scServiceDetail.getParentUuid() != null
									&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
									? scServiceDetail.getParentUuid()
									: scServiceDetail.getUuid();
							stg0SapPoDtlOptimus=getSapData(tclServiceId);
						}

						addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoNumber",stg0SapPoDtlOptimus.getPoNumber());
						addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoDate",DateUtil.convertDateToString(new Date()));
						addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoStatus","Success");
						Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
								.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
						contructAutoPoRequestUpdatePrefeasible(autoPoRequest,
								scServiceDetail,scComponentAttributesAMap,scServiceDetail.getScOrder());
					}

				LOGGER.info("Auto Po Request json {}", Utils.convertObjectToJson(autoPoRequest));
				try {
					RestResponse response = restClientService.postWithBasicAuthentication(sapAutoPoUrl,
							Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
					if (response != null) {
						LOGGER.info("Auto PO Create response {}", Utils.convertObjectToJson(autoPoResponse));
						if (response.getData() != null) {
							autoPoResponse = Utils.convertJsonToObject(response.getData(), AutoPoResponse.class);
							LOGGER.info("Auto Po Output json {}", Utils.convertObjectToJson(autoPoResponse));
							AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPoRequest),
									response.getData(), serviceCode, "AUTOPO", "AutoPO" + serviceCode);
						}

					}

					if (response != null && response.getStatus() == Status.SUCCESS) {
						LOGGER.info("Success");
					}

				} catch (Exception e) {
					LOGGER.info("Error in processing po request {}", e.getMessage());
				}
			}
				
			if(autoPoResponse!=null && autoPoResponse.getPOResponse()!=null && execution!=null) {				
				prMapper.put("offnetLocalPoUpdateDate", DateUtil.convertDateToString(new Date()));
				prMapper.put("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());
				execution.setVariable("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());
				
				if(!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
					execution.setVariable("offnetPOUpdateCompleted", true);
				}else {
					execution.setVariable("offnetPOUpdateCompleted", false);
				}
				errorMessage="Offnet PO Number:"+autoPoResponse.getPOResponse().getPONumber() ;
				errorMessage+=", Offnet PO Update Status:"+autoPoResponse.getPOResponse().getPOStatus();

			}else if(execution!=null) {
				execution.setVariable("offnetPOUpdateCompleted", false);
			}
			
			componentAndAttributeService.updateAttributes(serviceId, prMapper,AttributeConstants.COMPONENT_LM,"A");
			
				
		} catch (Exception e) {
			LOGGER.error("Error-creating-autopo-update-request {}", e.getMessage());
			LOGGER.error("Exception-creating-autopoupdate-request {}", e);
		}

		return errorMessage;
	}

	public String processAutoPoTerminate(Integer serviceId,DelegateExecution execution,String siteType)  {
		LOGGER.info("Inside process auto po terminate for service id {}", serviceId);
		AutoPoRequest autoPoRequest = new AutoPoRequest();
		AutoPoResponse autoPoResponse = null;
		String errorMessage="";

				try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();

			Map<String, String> prMapper = new HashMap<>();
			if (scServiceDetail != null) {
				String serviceCode = scServiceDetail.getUuid();
				LOGGER.info("Got processOffnetAutoPoTerminate serviceCode {}", serviceCode);
                if(execution.getVariable("skipOffnet").equals(false)) {
                	
                
                    Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
                            .getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, siteType);
                    Map<String, String> scOrderAttributes = commonFulfillmentUtils
                            .getScOrderAttributes(scServiceDetail.getScOrder().getId());
                    Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
                            scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
                    ScContractInfo scContractInfo = scContractInfoRepository
                            .findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
                    Map<String, String> orderSubCategoryMap = commonFulfillmentUtils.orderSubcategorySapData();
                    Map<String, String> sapInterfaceMap = commonFulfillmentUtils.sapInterfaceMappingData();
                    Map<String, String> sapBuyerMap = commonFulfillmentUtils.sapBuyerMappingData();
					Map<String, String> terminationTypeMap = commonFulfillmentUtils.terminationTypeSapData();
                    /*Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;*/
                    /*if (scServiceDetail.getScOrder().getOrderCategory() != null && !scServiceDetail.getScOrder().getOrderCategory().equals(CommonConstants.NEW)) {
                        stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
                                .findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
                                        ((scServiceDetail.getParentUuid() != null
                                                && StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
                                                ? scServiceDetail.getParentUuid()
                                                : scServiceDetail.getUuid()),
                                        "A_END_LM");
                    }*/
                    contructAutoPoRequestTerminate(autoPoRequest, scOrderAttributes, scComponentAttributesAMap,
                            scServiceDetail, scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes,
                            orderSubCategoryMap,terminationTypeMap, sapInterfaceMap, sapBuyerMap);
                }
                LOGGER.info("Auto Po Request json {}", Utils.convertObjectToJson(autoPoRequest));
                try {
                    RestResponse response = restClientService.postWithBasicAuthentication(sapAutoPoUrl,
                            Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
                    if (response != null) {
                        LOGGER.info("Auto PO terminate response {}", Utils.convertObjectToJson(autoPoResponse));
                        if (response.getData() != null) {
                            autoPoResponse = Utils.convertJsonToObject(response.getData(), AutoPoResponse.class);
                            LOGGER.info("Auto Po terminate Output json {}", Utils.convertObjectToJson(autoPoResponse));
                            AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPoRequest),
                                    response.getData(), serviceCode, "AUTOPO", "AutoPO" + serviceCode);
                        }

                    }

                    if (response != null && response.getStatus() == Status.SUCCESS) {
                        LOGGER.info("Success");
                    }

                } catch (Exception e) {
                    LOGGER.info("Error in processing po request {}", e.getMessage());
                }
			}

            if(autoPoResponse!=null && autoPoResponse.getPOResponse()!=null && execution!=null) {
                prMapper.put("offnetLocalPoTerminateDate", DateUtil.convertDateToString(new Date()));
                prMapper.put("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());
                execution.setVariable("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());

                if(!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
                    execution.setVariable("offnetPOTerminateCompleted", true);
                }else {
                    execution.setVariable("offnetPOTerminateCompleted", false);
                    errorMessage="Offnet Terminate PO Failure Reason :"+autoPoResponse.getPOResponse().getRemark();
                }

            }else if(execution!=null) {
                execution.setVariable("offnetPOTerminateCompleted", false);
				errorMessage="Offnet Terminate PO Failure Reason :"+autoPoResponse.getPOResponse().getRemark();
            }
			errorMessage+=", Offnet Terminate PO Number:"+autoPoResponse.getPOResponse().getPONumber() ;
			errorMessage+=", Offnet PO Terminate Status:"+autoPoResponse.getPOResponse().getPOStatus();

            componentAndAttributeService.updateAttributes(serviceId, prMapper,AttributeConstants.COMPONENT_LM,"A");


        } catch (Exception e) {
            LOGGER.error("Error-creating-autopo-terminate-request {}", e.getMessage());
            LOGGER.error("Exception-creating-autopoterminate-request {}", e);
        }

        return errorMessage;
	}

	private void addOrUpdateScComponentAttributes(ScServiceDetail scServiceDetail, String componentName,
			String siteType, String attrName, String attrValue) {
		ScComponentAttribute scComponentAttribute = null;
		ScComponent scComponent = scComponentRepository
				.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(scServiceDetail.getId(),
						componentName, siteType);

		if (scComponent.getId() != null) {
			scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							scServiceDetail.getId(), attrName, componentName, siteType);
			if (scComponentAttribute == null) {
				scComponentAttribute = new ScComponentAttribute();
				scComponentAttribute.setCreatedBy(Utils.getSource());
				scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			} else {
				scComponentAttribute.setUpdatedBy(Utils.getSource());
				scComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			}
			scComponentAttribute.setScComponent(scComponent);
			scComponentAttribute.setAttributeName(attrName);
			scComponentAttribute.setAttributeValue(attrValue);
			scComponentAttribute.setIsActive(CommonConstants.Y);
			scComponentAttribute.setScServiceDetailId(scServiceDetail.getId());
			scComponentAttribute.setUuid(Utils.generateUid());
			scComponentAttribute.setAttributeAltValueLabel(attrValue);
			scComponentAttributesRepository.saveAndFlush(scComponentAttribute);
		}

	}
	public void processGrnStatusResponse(GrnSapHanaResponse grnResponses) throws TclCommonException {
		LOGGER.info("Entering processGRNStatusResponse...");

		if (Objects.nonNull(grnResponses.getGrnDetails()) && !grnResponses.getGrnDetails().isEmpty()) {
			Map<Integer, List<GrnDetails>> grnResponseByPoNumber = grnResponses.getGrnDetails().stream()
					.collect(Collectors.groupingBy(grnResponse -> grnResponse.getPONumber()));

			for (Map.Entry<Integer, List<GrnDetails>> grnResponseMap : grnResponseByPoNumber.entrySet()) {
				LOGGER.info("processGRNStatusResponse PO Number={}", grnResponseMap.getKey());

				Optional<ProCreation> proCreationOp = proCreationRepository.findByPoNumber(String.valueOf(grnResponseMap.getKey()));
				if (proCreationOp.isPresent() && Objects.nonNull(grnResponseMap.getValue()) && !grnResponseMap.getValue().isEmpty()) {
					GrnDetails grnResponse = grnResponseMap.getValue().stream().findFirst().get();
					Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(proCreationOp.get().getServiceId(), "confirm-material-availability");
					Map<String, String> atMap = new HashMap<>();
					atMap.put("grnNumber", String.valueOf(grnResponse.getGRNNo()));
					//atMap.put("grnCreationDate",  String.valueOf(grnResponse.getGRNDate()));
					atMap.put("materialReceived", "Yes");
					atMap.put("materialReceivedDate", String.valueOf(grnResponse.getGRNDate()));

					componentAndAttributeService.updateAttributes(proCreationOp.get().getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

					componentAndAttributeService.updateAdditionalAttributes(task.getScServiceDetail(), "CONFIRMMATERIALAVAIABILITY",
							Utils.convertObjectToJson(grnResponses));
					processTaskLogDetails(task, "CLOSED", grnResponse.getRemark(), null, null);
					flowableBaseService.taskDataEntry(task, grnResponseMap.getValue());
				}
			}
		}
	}

	private void constructAutoPoServiceItemsOtcModem(AutoPoLineItems autoPoLineItems, Map<String, String> scOrderAttributes,
												Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
												ScContractInfo scContractInfo, Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		//need to find attribute for wireless case
		String lmType=scComponentAttributes.get("lmType");
		if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
				&& !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM("EA");
			String deliveryDate = CommonConstants.EMPTY;
			if (scServiceDetail.getServiceCommissionedDate() != null) {
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(formatter.parse(formatter.format(scServiceDetail.getServiceCommissionedDate())));
					cal.add(cal.getTime().getDate(), -7);
					deliveryDate = formatter.format(cal.getTime());
				} catch (Exception e) {
					LOGGER.info("Error on parsing the date");
				}

			}
			autoPoServiceItems.setEEIND(deliveryDate);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM("36000002");
			autoPoServiceItems.setQUANTITY("1");
			//Need to find attribute for wireless case
			 if (scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")&& !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
				autoPoServiceItems.setTBTWR(
						scServiceAttributes.containsKey("lm_otc_modem_charges_offwl") ? scServiceAttributes.get("lm_otc_modem_charges_offwl")
								: CommonConstants.EMPTY);
			}
			autoPoServiceItems.setWAERS(
					scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
							: CommonConstants.EMPTY);
			try {
				autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
						? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
						: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoServiceItemsList.add(autoPoServiceItems);
		}

		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}

	private void constructAutoPoServiceItemsArcModem(AutoPoLineItems autoPoLineItems, Map<String, String> scOrderAttributes,
													 Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
													 ScContractInfo scContractInfo, Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		//need to find attribute for wireless case
		String lmType=scComponentAttributes.get("lmType");
		if ((scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
				&& !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM("YR");
			String deliveryDate = CommonConstants.EMPTY;
			if (scServiceDetail.getServiceCommissionedDate() != null) {
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(formatter.parse(formatter.format(scServiceDetail.getServiceCommissionedDate())));
					cal.add(cal.getTime().getDate(), -7);
					deliveryDate = formatter.format(cal.getTime());
				} catch (Exception e) {
					LOGGER.info("Error on parsing the date");
				}

			}
			autoPoServiceItems.setEEIND(deliveryDate);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM("35000002");
			autoPoServiceItems.setQUANTITY("12");

			String arcValue = null;
			if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")&& !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
				arcValue = scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
						? scServiceAttributes.get("lm_arc_modem_charges_offwl")
						: null;
			}
			String mrcValue = CommonConstants.EMPTY;
			if (arcValue != null) {
				mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
			}

			if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")&& !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
				autoPoServiceItems.setTBTWR(
						scServiceAttributes.containsKey("lm_arc_modem_charges_offwl") ? mrcValue
								: CommonConstants.EMPTY);
			}



			autoPoServiceItems.setWAERS(
					scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
							: CommonConstants.EMPTY);
			try {
				autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
						? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
						: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoServiceItemsList.add(autoPoServiceItems);
		}

		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}


	private void constructAutoPoServiceItemsArcModemUpdate(AutoPoLineItems autoPoLineItems,
													  Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
													  ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
													  Map<String, String> scServiceAttributes){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		String lmType=scComponentAttributes.get("lmType");
		if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")&& !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL")) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
			autoPoServiceItems.setEEIND(CommonConstants.EMPTY);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM(CommonConstants.EMPTY);
			autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
			autoPoServiceItems.setTBTWR(CommonConstants.EMPTY);
			autoPoServiceItems.setWAERS(CommonConstants.EMPTY);


			try {
				autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
						? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"),formatterCdate))
						: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoLineItems.setEBELP("0030");
			autoPoServiceItemsList.add(autoPoServiceItems);
		}

		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}

	private void constructAutoPoServiceItemsOtcModemUpdate(AutoPoLineItems autoPoLineItems,
													  Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
													  ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
													  Map<String, String> scServiceAttributes) {
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
		String lmType=scComponentAttributes.get("lmType");
		if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
				&& !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0")&&StringUtils.isNotBlank(lmType)&&lmType.equalsIgnoreCase("OffnetWL"))) {
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
			autoPoServiceItems.setEEIND(CommonConstants.EMPTY);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM(CommonConstants.EMPTY);
			autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
			autoPoServiceItems.setTBTWR(CommonConstants.EMPTY);
			autoPoServiceItems.setWAERS(CommonConstants.EMPTY);
			try {
				autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
						? formatterPo.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
						: CommonConstants.EMPTY);
			} catch (Exception e) {
				LOGGER.info("Error on parsing the date");
			}
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoLineItems.setEBELP("0040");
			autoPoServiceItemsList.add(autoPoServiceItems);
		}
		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}



	private void contructAutoPoRequestUpdatePrefeasible(AutoPoRequest autoPoRequest,ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributes, ScOrder scOrder){
		LOGGER.info("Inside contructAutoPoRequest");
		AutoPoMsgs autoPoMsgs = new AutoPoMsgs();
		contructAutoPoHeaderUpdatePrefeasible(autoPoMsgs,scServiceDetail,scComponentAttributes, scOrder);
		constructAutoPoLineItemsUpdatePrefeasible(autoPoMsgs);
		autoPoRequest.setPOMSGS(autoPoMsgs);
	}

	private void contructAutoPoHeaderUpdatePrefeasible(AutoPoMsgs autoPoMsgs, ScServiceDetail scServiceDetail,  Map<String, String> scComponentAttributes,ScOrder scOrder) {
		LOGGER.info("Inside contructAutoPoHeader");
		AutoPoHeader autoPoHeader = new AutoPoHeader();
		autoPoHeader.setBSART("BS");
		autoPoHeader.setBUKRS(CommonConstants.EMPTY);
		autoPoHeader.setCHILDPO(CommonConstants.EMPTY);
		autoPoHeader.setEBELN(scComponentAttributes.containsKey("offnetLocalPoNumber") ? StringUtils.trimToEmpty(scComponentAttributes.get("offnetLocalPoNumber"))
				: CommonConstants.EMPTY);
		autoPoHeader.setEKGRP(CommonConstants.EMPTY);
		autoPoHeader.setEKORG(CommonConstants.EMPTY);
		autoPoHeader.setHEADERTEXT(CommonConstants.EMPTY);
		autoPoHeader.setLIFNR(CommonConstants.EMPTY); // Vendor Acc number
		autoPoHeader.setIHREZ(CommonConstants.EMPTY);
		autoPoHeader.setPOTYPE(CommonConstants.EMPTY);
		autoPoHeader.setREQTYPE("UPDATE");
		autoPoHeader.setSCNROTYPE("BWCHANGE");
		autoPoHeader.setUNSEZ(CommonConstants.EMPTY); // Supplier Feasibility ID
		autoPoHeader.setVERKF(CommonConstants.EMPTY);
		autoPoHeader
				.setWAERS(CommonConstants.EMPTY);
		autoPoHeader.setZBANDWIDTH(CommonConstants.EMPTY);
		autoPoHeader.setZSFDCID(CommonConstants.EMPTY);
		autoPoHeader.setZSFDCOPPID(CommonConstants.EMPTY);
		autoPoHeader.setZSITEA(CommonConstants.EMPTY);
		autoPoHeader.setZSITEB(CommonConstants.EMPTY);
		autoPoHeader.setZSPEED(CommonConstants.EMPTY);
		autoPoHeader.setZTERM(CommonConstants.EMPTY);
		autoPoHeader.setZTYPEDEAL(CommonConstants.EMPTY);
		autoPoHeader
				.setZZCIRCUIT(CommonConstants.EMPTY);
		autoPoHeader.setZZCOPFID(scOrder.getUuid());
		autoPoHeader.setZZCPEOWNER(CommonConstants.EMPTY);
		autoPoHeader.setZZCUST(CommonConstants.EMPTY);
		autoPoHeader.setZZFESREQID(CommonConstants.EMPTY);
		autoPoHeader
				.setZZINTTYPE(CommonConstants.EMPTY);
		autoPoHeader.setZZOLDPONO(CommonConstants.EMPTY);
		autoPoHeader.setZZPOCATEGORY(CommonConstants.EMPTY);
		autoPoHeader.setZZPRODCOMP(CommonConstants.EMPTY); // OPTIMUS LOGIC - if ILL AND GVPN - DEFAULT TO A_END_LM , if NDE/NPL -
		// A_END_LM, B_END_LM
		autoPoHeader.setZZREVCURR(CommonConstants.EMPTY);
		autoPoHeader.setZZREVMRCQUAN(CommonConstants.EMPTY);
		autoPoHeader.setZZREVMRCVALUE(CommonConstants.EMPTY);
		autoPoHeader.setZZSERVICEID(scServiceDetail.getUuid());
		autoPoHeader.setZZSRVTYPE(CommonConstants.EMPTY);
		autoPoMsgs.setHEADER(autoPoHeader);
	}

	private void constructAutoPoLineItemsUpdatePrefeasible(AutoPoMsgs autoPoMsgs){
		LOGGER.info("Inside constructAutoPoLineItems");
		List<AutoPoLineItems> auLineItems = new ArrayList<>();
		AutoPoLineItems autoPoLineItems = new AutoPoLineItems();
		autoPoLineItems.setAFNAM(CommonConstants.EMPTY);
		autoPoLineItems
				.setCUSTSEGMENT(CommonConstants.EMPTY);
		autoPoLineItems.setEBELP(CommonConstants.EMPTY);
		autoPoLineItems.setEPSTP(CommonConstants.EMPTY);
		autoPoLineItems.setKNTTP(CommonConstants.EMPTY);
		autoPoLineItems.setKOSTL(CommonConstants.EMPTY);
		autoPoLineItems.setMATKL(CommonConstants.EMPTY);
		autoPoLineItems.setMENGE(CommonConstants.EMPTY);
		autoPoLineItems.setSAKNR(CommonConstants.EMPTY);
		autoPoLineItems.setTXZ01(CommonConstants.EMPTY);
		autoPoLineItems.setWERKS(CommonConstants.EMPTY);
		autoPoLineItems.setZCOMMDT(CommonConstants.EMPTY);
		autoPoLineItems.setZTERMDT(CommonConstants.EMPTY);
		autoPoLineItems.setZVENREFNO(CommonConstants.EMPTY);
		autoPoLineItems.setZZCHARD(CommonConstants.EMPTY);
		autoPoLineItems.setZZCOMMDT(CommonConstants.EMPTY);
		autoPoLineItems.setZZCRFSDT(CommonConstants.EMPTY);
		autoPoLineItems.setZZFRMEND(CommonConstants.EMPTY);
		autoPoLineItems
				.setZZOPNAME(CommonConstants.EMPTY);
		autoPoLineItems.setZZRFSDT(CommonConstants.EMPTY);
		autoPoLineItems.setZZTERMDT(CommonConstants.EMPTY);
		autoPoLineItems.setZZTERMTY(CommonConstants.EMPTY);
		autoPoLineItems.setZZTOEND(CommonConstants.EMPTY);
		constructAutoPoServiceItemsUpdatePrefeasible(autoPoLineItems);
		auLineItems.add(autoPoLineItems);

		autoPoMsgs.setLINEITEMS(auLineItems);
	}

	private void constructAutoPoServiceItemsUpdatePrefeasible(AutoPoLineItems autoPoLineItems){
		LOGGER.info("Inside constructAutoPoServiceItems");
		List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
			AutoPoServiceItems autoPoServiceItems = new AutoPoServiceItems();
			autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
			autoPoServiceItems.setEEIND(CommonConstants.EMPTY);
			autoPoServiceItems.setEXTLINE(CommonConstants.EMPTY);
			autoPoServiceItems.setKTEXT1(CommonConstants.EMPTY); // Need info
			autoPoServiceItems.setKTEXTNUM(CommonConstants.EMPTY);
			autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
			autoPoServiceItems.setTBTWR(CommonConstants.EMPTY);
			autoPoServiceItems.setWAERS(CommonConstants.EMPTY);
			autoPoServiceItems.setZZCDATE(CommonConstants.EMPTY);
			autoPoServiceItems.setZZDDATE(CommonConstants.EMPTY); // Need to send the value if MACD
			autoPoServiceItemsList.add(autoPoServiceItems);
		autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
	}

	/**
	 * Method to identify same bso or not
	 * @param oldVendorId
	 * @param newVendorId
	 * @return
	 */
	private boolean isSameBSOorDifferent(String oldVendorId,String newVendorId)
	{
		LOGGER.info("Old Vendor Id:"+oldVendorId);
		LOGGER.info("New Vendor Id:"+newVendorId);
		boolean flag=false;
		if(Objects.nonNull(oldVendorId)&&Objects.nonNull(newVendorId)&&oldVendorId.equalsIgnoreCase(newVendorId))
			flag=true;
		return flag;
	}

	/**
	 *
	 * @param orderCategory
	 * @param category
	 * @return
	 */
	private String getChangeBWOrShiftSiteHotUpgradeTerminationType(String orderSubCategory,String category)
	{
		String terminationType="";
		if(orderSubCategory.equalsIgnoreCase("Hot Upgrade-BSO Change")||orderSubCategory.equalsIgnoreCase("Hot Downgrade-BSO Change"))
		{
			if (category.equalsIgnoreCase("CHANGE_BANDWIDTH"))
				terminationType = "CHANGE BSO";
			else if (category.equalsIgnoreCase("SHIFT_SITE"))
				terminationType = "SHIFTING";
		}
		return terminationType;
	}

	/**
	 * Method to get terminatio type based on bso status
	 * @param orderCategory
	 * @param bsoStatus
	 * @return
	 */
	private String getTerminationTypeBasedOnBSOStatus(String orderSubCategory,boolean bsoStatus)
	{
		String terminationType="";
		if(orderSubCategory.equalsIgnoreCase("Parallel Upgrade"))
		{
			if(bsoStatus==true)
				terminationType="UPGRADE";
			else
				terminationType="CHANGE BSO";
		}
		else if(orderSubCategory.equalsIgnoreCase("Parallel Downgrade"))
		{
			if(bsoStatus==true)
				terminationType="DOWNGRADE";
			else
				terminationType="CHANGE BSO";
		}
		else if(orderSubCategory.equalsIgnoreCase("Parallel Shifting"))
		{
			if(bsoStatus==true)
				terminationType="SHIFTING";
			else
				terminationType="CHANGE BSO";
		}
		return terminationType;
	}


	public String processOffnetAutoPoUpdateDummy(Integer serviceId,DelegateExecution execution) {

		LOGGER.info("Inside processOffnetAutoPoUpdate serviceid= {}", serviceId);
		AutoPoRequest autoPoRequest = new AutoPoRequest();
		AutoPoResponse autoPoResponse = null;
		String errorMessage = "";
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();

			Map<String, String> prMapper = new HashMap<>();
			if (scServiceDetail != null) {
				String serviceCode = scServiceDetail.getUuid();
				LOGGER.info("Got processOffnetAutoPoUpdate serviceCode {}", serviceCode);


					Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
							.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
					Map<String, String> scOrderAttributes = commonFulfillmentUtils
							.getScOrderAttributes(scServiceDetail.getScOrder().getId());
					Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
							scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
					ScContractInfo scContractInfo = scContractInfoRepository
							.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
					Map<String, String> orderSubCategoryMap = commonFulfillmentUtils.orderSubcategorySapData();
					Map<String, String> sapInterfaceMap = commonFulfillmentUtils.sapInterfaceMappingData();
					Map<String, String> sapBuyerMap = commonFulfillmentUtils.sapBuyerMappingData();
					contructAutoPoRequestUpdate(autoPoRequest, scOrderAttributes, scComponentAttributesAMap,
							scServiceDetail, scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes,
							orderSubCategoryMap, sapInterfaceMap, sapBuyerMap);

				LOGGER.info("Auto Po Request json {}", Utils.convertObjectToJson(autoPoRequest));
				try {
					RestResponse response = restClientService.postWithBasicAuthentication(sapAutoPoUrl,
							Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
					if (response != null) {
						LOGGER.info("Auto PO Create response {}", Utils.convertObjectToJson(autoPoResponse));
						if (response.getData() != null) {
							autoPoResponse = Utils.convertJsonToObject(response.getData(), AutoPoResponse.class);
							LOGGER.info("Auto Po Output json {}", Utils.convertObjectToJson(autoPoResponse));
							AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPoRequest),
									response.getData(), serviceCode, "AUTOPO", "AutoPO" + serviceCode);
						}

					}

					if (response != null && response.getStatus() == Status.SUCCESS) {
						LOGGER.info("Success");
					}

				} catch (Exception e) {
					LOGGER.info("Error in processing po request {}", e.getMessage());
				}
			}

			if(autoPoResponse!=null && autoPoResponse.getPOResponse()!=null && execution!=null) {
				prMapper.put("offnetLocalPoUpdateDate", DateUtil.convertDateToString(new Date()));
				prMapper.put("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());
				execution.setVariable("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());

				if(!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
					execution.setVariable("offnetPOUpdateCompleted", true);
				}else {
					execution.setVariable("offnetPOUpdateCompleted", false);
				}
				errorMessage="Offnet PO Number:"+autoPoResponse.getPOResponse().getPONumber() ;
				errorMessage+=", Offnet PO Update Status:"+autoPoResponse.getPOResponse().getPOStatus();

			}else if(execution!=null) {
				execution.setVariable("offnetPOUpdateCompleted", false);
			}

			componentAndAttributeService.updateAttributes(serviceId, prMapper,AttributeConstants.COMPONENT_LM,"A");


		} catch (Exception e) {
			LOGGER.error("Error-creating-autopo-update-request {}", e.getMessage());
			LOGGER.error("Exception-creating-autopoupdate-request {}", e);
		}

		return errorMessage;
	}


	public String processAutoPoTerminateDummy(Integer serviceId,DelegateExecution execution)  {
		LOGGER.info("Inside process auto po terminate for service id {}", serviceId);
		AutoPoRequest autoPoRequest = new AutoPoRequest();
		AutoPoResponse autoPoResponse = null;
		String errorMessage="";

		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();

			Map<String, String> prMapper = new HashMap<>();
			if (scServiceDetail != null) {
				String serviceCode = scServiceDetail.getUuid();
				LOGGER.info("Got processOffnetAutoPoTerminate serviceCode {}", serviceCode);


					Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
							.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
					Map<String, String> scOrderAttributes = commonFulfillmentUtils
							.getScOrderAttributes(scServiceDetail.getScOrder().getId());
					Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
							scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
					ScContractInfo scContractInfo = scContractInfoRepository
							.findFirstByScOrder_id(scServiceDetail.getScOrder().getId());
					Map<String, String> orderSubCategoryMap = commonFulfillmentUtils.orderSubcategorySapData();
					Map<String, String> sapInterfaceMap = commonFulfillmentUtils.sapInterfaceMappingData();
					Map<String, String> sapBuyerMap = commonFulfillmentUtils.sapBuyerMappingData();
					Map<String, String> terminationTypeMap = commonFulfillmentUtils.terminationTypeSapData();
					Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
					/*if (scServiceDetail.getScOrder().getOrderCategory() != null && !scServiceDetail.getScOrder().getOrderCategory().equals(CommonConstants.NEW)) {
						stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
								.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
										((scServiceDetail.getParentUuid() != null
												&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
												? scServiceDetail.getParentUuid()
												: scServiceDetail.getUuid()),
										"A_END_LM");
					}*/
					contructAutoPoRequestTerminate(autoPoRequest, scOrderAttributes, scComponentAttributesAMap,
							scServiceDetail, scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes,
							orderSubCategoryMap,terminationTypeMap, sapInterfaceMap, sapBuyerMap);

				LOGGER.info("Auto Po Request json {}", Utils.convertObjectToJson(autoPoRequest));
				try {
					RestResponse response = restClientService.postWithBasicAuthentication(sapAutoPoUrl,
							Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
					if (response != null) {
						LOGGER.info("Auto PO terminate response {}", Utils.convertObjectToJson(autoPoResponse));
						if (response.getData() != null) {
							autoPoResponse = Utils.convertJsonToObject(response.getData(), AutoPoResponse.class);
							LOGGER.info("Auto Po terminate Output json {}", Utils.convertObjectToJson(autoPoResponse));
							AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPoRequest),
									response.getData(), serviceCode, "AUTOPO", "AutoPO" + serviceCode);
						}

					}

					if (response != null && response.getStatus() == Status.SUCCESS) {
						LOGGER.info("Success");
					}

				} catch (Exception e) {
					LOGGER.info("Error in processing po request {}", e.getMessage());
				}
			}

			if(autoPoResponse!=null && autoPoResponse.getPOResponse()!=null && execution!=null) {
				prMapper.put("offnetLocalPoTerminateDate", DateUtil.convertDateToString(new Date()));
				prMapper.put("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());
				execution.setVariable("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());

				if(!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
					execution.setVariable("offnetPOTerminateCompleted", true);
				}else {
					execution.setVariable("offnetPOTerminateCompleted", false);
					errorMessage="Offnet Terminate PO Failure Reason :"+autoPoResponse.getPOResponse().getRemark();
				}

			}else if(execution!=null) {
				execution.setVariable("offnetPOTerminateCompleted", false);
				errorMessage="Offnet Terminate PO Failure Reason :"+autoPoResponse.getPOResponse().getRemark();
			}
			errorMessage+=", Offnet Terminate PO Number:"+autoPoResponse.getPOResponse().getPONumber() ;
			errorMessage+=", Offnet PO Terminate Status:"+autoPoResponse.getPOResponse().getPOStatus();

			componentAndAttributeService.updateAttributes(serviceId, prMapper,AttributeConstants.COMPONENT_LM,"A");


		} catch (Exception e) {
			LOGGER.error("Error-creating-autopo-terminate-request {}", e.getMessage());
			LOGGER.error("Exception-creating-autopoterminate-request {}", e);
		}

		return errorMessage;
	}



	public void updatePoStatus(String uuid, Integer serviceId, String type,
			String poReleaseStatus, String vendorCode, Integer cpeComponentId) {
		ProCreation proCreationEntity=proCreationRepository.findFirstByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndType(serviceId,uuid,vendorCode,cpeComponentId,type);
		if(proCreationEntity!=null && poReleaseStatus!=null){
			proCreationEntity.setPoStatus(poReleaseStatus.equalsIgnoreCase("Yes")?"PO Released":"PO Not Released");
			proCreationEntity.setPoCompletedDate(Timestamp.valueOf(LocalDateTime.now()));
			proCreationRepository.save(proCreationEntity);
		}
	}
		
	/**
	 * Method to get parentPo Data
	 * @param tclServiceId
	 * @return
	 */
	public ParentPoBean getParentPoData(String tclServiceId)
	{
		ParentPoBean parentPoBean=null;
		if(StringUtils.isNotBlank(tclServiceId)) {
			List<Map<String, Object>> sapMapList = stg0SapPoDtlOptimusRepository.getDataByServiceId(tclServiceId);
			if (Objects.nonNull(sapMapList) && !sapMapList.isEmpty()) {
				Map<String, Object> sapMap = sapMapList.stream().findFirst().get();
				parentPoBean = new ParentPoBean();
				parentPoBean.setPoNumber((String) sapMap.get("oldOffnetPoNumber"));
				parentPoBean.setChildPoNumber((String) sapMap.get("childPoNumber"));
				parentPoBean.setProductComponent((String) sapMap.get("productComponent"));
				parentPoBean.setTclServiceId((String) sapMap.get("serviceId"));
				parentPoBean.setBsoCircuitId((String) sapMap.get("oldBsoCircuitId"));
				parentPoBean.setVendorId((String) sapMap.get("vendorId"));
				parentPoBean.setPoCreationDate((String) sapMap.get("poCreationDate"));
				parentPoBean.setSfdcProviderName((String) sapMap.get("sfdcProviderName"));
				parentPoBean.setVendorName((String) sapMap.get("vendorName"));
				LOGGER.info("parent po data:" + parentPoBean.toString());
			}
		}

		return parentPoBean;
	}

	/**
	 * Method to get sap data
	 * @param tclServiceId
	 * @return
	 */
	public Stg0SapPoDtlOptimus getSapData(String tclServiceId) {
		Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
		if (StringUtils.isNotBlank(tclServiceId)) {
			ParentPoBean parentPoBean = getParentPoData(tclServiceId);
			if(Objects.nonNull(parentPoBean)) {
				stg0SapPoDtlOptimus = new Stg0SapPoDtlOptimus();
				stg0SapPoDtlOptimus.setPoNumber(parentPoBean.getPoNumber());
				stg0SapPoDtlOptimus.setChildPoNumber(parentPoBean.getChildPoNumber());
				stg0SapPoDtlOptimus.setProductComponent(parentPoBean.getProductComponent());
				stg0SapPoDtlOptimus.setTclServiceId(parentPoBean.getTclServiceId());
				stg0SapPoDtlOptimus.setVendorRefIdOrderId(parentPoBean.getBsoCircuitId());
				stg0SapPoDtlOptimus.setVendorNo(parentPoBean.getVendorId());
				stg0SapPoDtlOptimus.setPoCreationDate(parentPoBean.getPoCreationDate());
				LOGGER.info("Parent Po is available:" + parentPoBean.getPoNumber());
			}
		}
		return stg0SapPoDtlOptimus;
	}

	/**
	 * Method to update parent po details for termination process
	 * @param scServiceDetail
	 * @param stg0SapPoDtlOptimus
	 * @param parentPoData
	 */
	public void updateParentPoDataForTermination(ScServiceDetail scServiceDetail,Stg0SapPoDtlOptimus stg0SapPoDtlOptimus,ParentPoBean parentPoData){
		if(Objects.nonNull(parentPoData))
		{
			addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "vendorId", stg0SapPoDtlOptimus.getVendorNo());
			addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "vendorName",parentPoData.getVendorName());
			if(StringUtils.isBlank(parentPoData.getSfdcProviderName()))
				addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sfdcProviderName", parentPoData.getSfdcProviderName());
			else
				addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sfdcProviderName", parentPoData.getVendorName());
			addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "oldOffnetPoNumber", stg0SapPoDtlOptimus.getPoNumber());
			addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "oldBsoCircuitId", stg0SapPoDtlOptimus.getVendorRefIdOrderId());
		}

	}

}
