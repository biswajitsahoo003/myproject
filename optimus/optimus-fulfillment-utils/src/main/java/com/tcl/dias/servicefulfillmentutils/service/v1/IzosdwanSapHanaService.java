package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.tcl.dias.common.beans.AutoPoResponse;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.utils.Currencies;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.ForexService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.CpeMaterialRequestDetails;
import com.tcl.dias.servicefulfillment.entity.entities.MrnCreationEntity;
import com.tcl.dias.servicefulfillment.entity.entities.MrnSequenceNumber;
import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.MstStateToDistributionCenterMapping;
import com.tcl.dias.servicefulfillment.entity.entities.ProCreation;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SapPoDtlOptimus;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.repository.AuditLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.CpeMaterialRequestDetailsRepository;
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
import com.tcl.dias.servicefulfillmentutils.beans.DispatchCPEBean;
import com.tcl.dias.servicefulfillmentutils.beans.SerialNumberBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoHeader;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoLineItems;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoMsgs;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoServiceItems;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PRHeader;
import com.tcl.dias.servicefulfillmentutils.beans.sap.ParentPoBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PoStatusBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PoStatusResponseBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.GOODSRECEIPT;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.InstallCpeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.InventoryDetailRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.InventoryDetailResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.OPTIMUSMaterialStockDi;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.OptimusWbsElementRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.WbsElementRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.WbsElementResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.CircuitDetails;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.HeaderDetails;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.InstallCpe;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.InstallCpeGIRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.LastMileRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.LineItems;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.MTNCreationRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.MaterialDetails;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.MaterialTransfer;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.OPTIMUSREFXINDIAASPACCESS;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.PR2PO;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.PR2POInstallSupport;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.PR2POInstallSupportRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.PR2PORequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.S4HanaMTN;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.SerialNo;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.ServiceItems;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.TrackCpeGRRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.TrackCpeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.VmiStock;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.VmiStockMaterial;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.WBSMaterialDetail;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.request.WBSTransferRequest;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.AutoPrPoRespones;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.GoodsRecipt;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.GrnMaterialDetail;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.GrnS4HanaResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.InstallCpeResponseBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.InventoryDetailResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.LastMileResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.MTNCreationResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.MinGoodsIssueResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.MinMaterailDetails;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.MinResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.OPTIMUSREFXINDIAASPACCESSRESP;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.S4HANAOPTIMUSMaterialStockDisResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.S4HanaOptimusGoodsIssue;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.VmIStockResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.VmiStockUpdate;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.WBSTransferResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.WbsElement;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.response.WbsResponseBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.sap.response.DisplayMaterialResponse;
import com.tcl.dias.servicefulfillmentutils.sap.response.SapQuantityAvailableRequest;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import java.util.TreeMap;
import java.util.stream.Collectors;

public class IzosdwanSapHanaService extends ServiceFulfillmentBaseService{

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IzosdwanSapHanaService.class);

    @Autowired
    RestClientService restClientService;

    @Value("${sap.hana.quantity.detail.url}")
    String sapQuantityDetailsUrl;

    @Value("${sap.mrn.transfer.url}")
    String sapTransferUrl;

    @Value("${sap.mrn.place.order.url}")
    String mrnMuxCreationUrl;

    @Value("${sap.hana.autopr.url}")
    String sapAutoPrUrl;

    @Value("${sap.hana.autopo.url}")
    String sapAutoPoUrl;

    @Value("${sap.hana.user.name}")
    String userName;

    @Value("${sap.hana.password}")
    String password;

    @Value("${sap.hana.mtn.user.name}")
    String mtnUserName;

    @Value("${sap.hana.mtn.password}")
    String mtnPassword;

    @Value("${sap.hana.lm.url}")
    String sapLastMileUrl;

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

    @Autowired
    private CpeMaterialRequestDetailsRepository cpeMaterialRequestDetailsRepository;

    @Value("${sap.hana.vmi.stock.url}")
    String sapHanaVmiStockUrl;

    @Value("${sap.hana.wbs.transfer.url}")
    String sapHanaWbsTranseferUrl;

    @Value("${sap.hana.track.cbe.url}")
    String sapHanaTrackCbeUrl;

    @Value("${sap.hana.install.cpe.url}")
    String sapHanaInstallCpeUrl;

    @Value("${sap.hana.mtn.place.order.url}")
    String mrnHanaMuxCreationUrl;

    @Value("${sap.hana.wbselement.url}")
    String wbsElementUrl;

    DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatterCdate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatterPo = new SimpleDateFormat("yyyyMMdd");

    public static final String MST_CATALOG_ROUTER_CATAGORY = "Router";
    public static final String MST_CATALOG_LICENSE_CATAGORY = "License";

    public boolean checMaterialQuantityInInventory(String serviceCode, Integer serviceId, String type, DelegateExecution execution, Integer overlayComponentId, boolean isNearsetInventory) throws TclCommonException, ParseException {
        InventoryDetailResponse sapQuantityResponse = null;
        boolean quantityAvailable = false;

        Map<String, Double> hardWarematerialQuantityMapping = new HashMap<>();

        Map<String, Double> licencedMaterialQuantityMapping = new HashMap<>();

        Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> availableQuantityMap = new HashMap<>();

        Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> filteredQuantityMap = new HashMap<>();




        String level5Wbs = null;


        try {

            ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "INPROGRESS");

            Optional<ScComponent> scComponentOptional = scComponentRepository.findById(overlayComponentId);


            if (scServiceDetail != null && scComponentOptional.isPresent()) {

                if (scComponentOptional.get().getScComponentAttributes().stream().filter(scComponentAttribute -> scComponentAttribute.getAttributeName().equalsIgnoreCase("level5Wbs")).findFirst().isPresent()) {
                    level5Wbs = scComponentOptional.get().getScComponentAttributes().stream().filter(scComponentAttribute -> scComponentAttribute.getAttributeName().equalsIgnoreCase("level5Wbs")).findFirst().get().getAttributeValue();
                }

                InventoryDetailRequest checkRequest = null;

                MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = null;
                if(isNearsetInventory) {
                    String destinationState=scComponentOptional.get().getScComponentAttributes().stream().filter(scComponentAttribute -> scComponentAttribute.getAttributeName().equalsIgnoreCase("destinationState")).findFirst().get().getAttributeValue();
                    List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
                            .findByState(destinationState);

                    LOGGER.info(
                            "checMaterialQuantityAndPlaceMrn distributionCenterMapping with service code:{} and state:{} and distributionCenterMapping :{}",
                            serviceCode, destinationState, distributionCenterMapping);

                    mstStateToDistributionCenterMapping = distributionCenterMapping
                            .stream().findFirst().orElse(null);
                }

                List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                        .findByScServiceDetailIdAndComponentId(scServiceDetail.getId(), overlayComponentId);

                checkRequest = constructQuantityRequest(scServiceDetail, type, hardWarematerialQuantityMapping,
                        mstStateToDistributionCenterMapping, licencedMaterialQuantityMapping, cpeMaterialRequestDetails);


                AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(checkRequest), null,
                        serviceCode, type + "CHECKINVENTORY", (execution == null ? null : execution.getProcessInstanceId()));


                LOGGER.info("SapQuantityCheckRequest request  for service code:{} and request:{}", serviceCode,
                        Utils.convertObjectToJson(checkRequest));

                if (checkRequest == null || checkRequest.getOPTIMUSMaterialStockDis().isEmpty()) {
                    return false;
                }

                LOGGER.info("Sap Quantity URL {} and username:{} and password:{}", sapQuantityDetailsUrl, userName, password);
                LOGGER.info("Sap Quantity request {}", Utils.convertObjectToJson(checkRequest));

                RestResponse response = restClientService.postWithBasicAuthentication(sapQuantityDetailsUrl,
                        Utils.convertObjectToJson(checkRequest), createCommonHeader(), userName, password);


                inventory.setResponse(response.getData());
                auditLogRepositoryRepository.save(inventory);

                LOGGER.info("sap response for service code:{} and status{}:sap response data{}:error respone {}",
                        serviceCode, response.getStatus(), response.getData(), response.getErrorMessage());

                if (response != null && response.getStatus() == Status.SUCCESS) {

                    sapQuantityResponse =  Utils.convertJsonToObject(response.getData(),
                            InventoryDetailResponse.class);

                    LOGGER.info("Sap Quantity response {}", Utils.convertObjectToJson(sapQuantityResponse));

                    LOGGER.info(
                            "sapQuantityResponse data for service code:{} and :{}:material map:{} and licencedMaterialQuantityMapping:{}",
                            serviceCode, response.getData(), Utils.convertObjectToJson(hardWarematerialQuantityMapping),
                            Utils.convertObjectToJson(licencedMaterialQuantityMapping));

                    isQuantityAvailable(hardWarematerialQuantityMapping,
                            licencedMaterialQuantityMapping, sapQuantityResponse, type, availableQuantityMap,

                            execution, scServiceDetail,level5Wbs,false);

                    InventoryDetailResponse inventoryDetailResponse = new InventoryDetailResponse();


                    LOGGER.info("sap quantity response for service code:{} and hardWarematerialQuantityMapping :{}and availableQuantityMap:{}",
                            serviceCode, Utils.convertObjectToJson(hardWarematerialQuantityMapping), Utils.convertObjectToJson(availableQuantityMap));

                    for (Map.Entry<String, Double> entry : hardWarematerialQuantityMapping.entrySet()) {

                        if (availableQuantityMap.containsKey(entry.getKey())) {
                            List<S4HANAOPTIMUSMaterialStockDisResponse> displayMaterialResponses = availableQuantityMap
                                    .get(entry.getKey());

                            List<S4HANAOPTIMUSMaterialStockDisResponse> filteResponse = displayMaterialResponses
                                    .stream()
                                    .filter(res -> (res.getWBSNUMBER() != null
                                            && !res.getWBSNUMBER().trim().isEmpty() && res.getPLANT() != null
                                            && !res.getPLANT().trim().isEmpty() && res.getSTORAGELOCATION() != null
                                            && !res.getSTORAGELOCATION().trim().isEmpty()))
                                    .limit(entry.getValue().intValue()).collect(Collectors.toList());
                            inventoryDetailResponse.getMTS4HANAOPTIMUSMaterialStockDisResponse().getS4HANAOPTIMUSMaterialStockDisResponse().addAll(filteResponse);

                            filteredQuantityMap.put(entry.getKey(), filteResponse);
                        }
                    }


                    LOGGER.info("sap quantity response for service code:{} and licencedMaterialQuantityMapping :{}and availableQuantityMap:{}",
                            serviceCode, Utils.convertObjectToJson(licencedMaterialQuantityMapping), Utils.convertObjectToJson(availableQuantityMap));

                    for (Map.Entry<String, Double> entry : licencedMaterialQuantityMapping.entrySet()) {

                        if (availableQuantityMap.containsKey(entry.getKey())) {
                            List<S4HANAOPTIMUSMaterialStockDisResponse> displayMaterialResponses = availableQuantityMap
                                    .get(entry.getKey());

                            List<S4HANAOPTIMUSMaterialStockDisResponse> filteResponse = displayMaterialResponses.stream()
                                    .limit( entry.getValue().intValue()).collect(Collectors.toList());
                            inventoryDetailResponse.getMTS4HANAOPTIMUSMaterialStockDisResponse().getS4HANAOPTIMUSMaterialStockDisResponse().addAll(filteResponse);
                            filteredQuantityMap.put(entry.getKey(), filteResponse);
                        }

                    }

                    LOGGER.info("sap quantity response for service code:{} and response :{}", serviceCode,
                            Utils.convertObjectToJson(inventoryDetailResponse));

                }
                if(licencedMaterialQuantityMapping.isEmpty()) {
                    if(execution != null) {
                        execution.setVariable("cpeLicenseNeeded", false);
                    }
                    LOGGER.info("cpeLicenseNeeded for servicecode:{} and value:{}", scServiceDetail.getUuid(), false);
                } else {
                    if(execution != null) {
                        execution.setVariable("cpeLicenseNeeded", true);
                    }
                    LOGGER.info("cpeLicenseNeeded for servicecode:{} and value{}", scServiceDetail.getUuid(), true);
                }


                saveCpeMaterailRequestDetailsAndPrPoCheck(checkRequest, scServiceDetail, type, filteredQuantityMap,
                        execution, cpeMaterialRequestDetails, level5Wbs, overlayComponentId, isNearsetInventory);

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("checMaterialQuantityAndPlaceMrn with service code:{} and error:{}", serviceCode, e);
            return false;
        }
        return quantityAvailable;

    }


    public boolean checMaterialAvailableInWbsVmi(String serviceCode, Integer serviceId, String type, String invType,Integer overlayComponentId,
                                                 String typeOfExpenses, DelegateExecution execution) throws TclCommonException, ParseException {
        InventoryDetailResponse sapQuantityResponse = null;

        Map<String, Double> hardWarematerialQuantityMapping = new HashMap<>();

        Map<String, Double> licencedMaterialQuantityMapping = new HashMap<>();

        Map<String, MstCostCatalogue> materialCodeMstCostCatalogMap = new HashMap<>();

        Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> availableQuantityMap = new HashMap<>();

        Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> filteredQuantityMap = new HashMap<>();

        String cpeType = null;

        if (invType.toLowerCase().contains("outright")) {
            cpeType = "outright";

        } else if (invType.toLowerCase().contains("rental")) {
            cpeType = "rental";
        }
        String level5Wbs = null;
        boolean isWbsAvailabe = false;

        try {

            ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,
                    "INPROGRESS");
            Optional<ScComponent> scComponentOptional = scComponentRepository.findById(overlayComponentId);

            if (scServiceDetail != null && scComponentOptional.isPresent()) {

                ScComponentAttribute wbScComponentAttribute = scComponentAttributesRepository
                        .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                                scServiceDetail.getId(), "level5Wbs", "LM", "A");
                if (wbScComponentAttribute != null) {
                    level5Wbs = wbScComponentAttribute.getAttributeValue();
                }
                String destinationState=scComponentOptional.get().getScComponentAttributes().stream().filter(scComponentAttribute -> scComponentAttribute.getAttributeName().equalsIgnoreCase("destinationState")).findFirst().get().getAttributeValue();
                List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
                        .findByState(destinationState);

                LOGGER.info(
                        "checMaterialQuantityAndPlaceMrn distributionCenterMapping with service code:{} and state:{} and distributionCenterMapping :{}",
                        serviceCode, destinationState, distributionCenterMapping);
                InventoryDetailRequest checkRequest = null;

                MstStateToDistributionCenterMapping   = distributionCenterMapping
                        .stream().findFirst().orElse(null);

                List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository.findByScServiceDetailId(scServiceDetail.getId());

                checkRequest = constructQuantityRequest(scServiceDetail, type, hardWarematerialQuantityMapping,
                        mstStateToDistributionCenterMapping, licencedMaterialQuantityMapping, cpeMaterialRequestDetails);

                AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(checkRequest), null,
                        serviceCode, type + "CHECKINVENTORYVMI", (execution == null ? null : execution.getProcessInstanceId()));

                LOGGER.info("SapQuantityCheckRequest request  for service code:{} and request:{}", serviceCode,
                        Utils.convertObjectToJson(checkRequest));

                if (checkRequest == null || checkRequest.getOPTIMUSMaterialStockDis().isEmpty()) {
                    return false;
                }

                LOGGER.info("Sap Quantity URL {}, username:{}, password:{}", sapQuantityDetailsUrl, userName, password);
                LOGGER.info("Sap Quantity request {}", Utils.convertObjectToJson(checkRequest));

                RestResponse response = restClientService.postWithBasicAuthentication(sapQuantityDetailsUrl,
                        Utils.convertObjectToJson(checkRequest), createCommonHeader(), userName, password);
                inventory.setResponse(response.getData());
                auditLogRepositoryRepository.save(inventory);

                LOGGER.info("sap response for service code:{} and status{}:sap response data{}:error respone {}",
                        serviceCode, response.getStatus(), response.getData(), response.getErrorMessage());

                if (response.getStatus() == Status.SUCCESS) {

                    sapQuantityResponse = Utils.convertJsonToObject(response.getData(), InventoryDetailResponse.class);

                    LOGGER.info("Sap Quantity response {}", Utils.convertObjectToJson(sapQuantityResponse));

                    LOGGER.info(
                            "sapQuantityResponse data for service code:{} and :{}:material map:{} and licencedMaterialQuantityMapping:{}",
                            serviceCode, response.getData(), hardWarematerialQuantityMapping,
                            licencedMaterialQuantityMapping);

                    isQuantityAvailable(hardWarematerialQuantityMapping, licencedMaterialQuantityMapping,
                            sapQuantityResponse, type, availableQuantityMap,

                            execution, scServiceDetail, level5Wbs,true);


                    LOGGER.info(
                            "sap quantity response for service code:{} and hardWarematerialQuantityMapping :{}and availableQuantityMap:{}",
                            serviceCode, hardWarematerialQuantityMapping, availableQuantityMap);


                    isWbsAvailabe = true;
                    for (Map.Entry<String, Double> entry : hardWarematerialQuantityMapping.entrySet()) {
                        List<S4HANAOPTIMUSMaterialStockDisResponse> displayMaterialResponses = availableQuantityMap
                                .get(entry.getKey());
                        if (displayMaterialResponses == null || displayMaterialResponses.isEmpty()
                                || entry.getValue() < displayMaterialResponses.size()) {
                            isWbsAvailabe = false;
                            break;
                        }

                    }
                    if(execution != null) {
                        execution.setVariable("isCpeAvailableInInventory", isWbsAvailabe);
                    }
                    LOGGER.info("isCpeAvailableInInventory for service code:{} and value:{}", serviceCode, isWbsAvailabe);
                }
            }
        } catch (Exception e) {

            LOGGER.error("checMaterialAvailableInWbsVmi with service code:{} and error:{}", serviceCode, e);
            return false;
        }
        return isWbsAvailabe;

    }

    public Map<String, String> wbsToWbsTransfer(String serviceCode, String ivType, String typeOfExpense, String type, Integer componentId) throws TclCommonException {
        ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
        return placeWbsTransfer(serviceCode, ivType, typeOfExpense, scServiceDetail, null, type, componentId);

    }

    public Map<String, String> placeWbsTransfer(String serviceCode, String ivType,
                                                String typeOfExpense, ScServiceDetail scServiceDetail, String processInstanceId, String type, Integer componentId)
            throws TclCommonException {
        String wbsTransferSuccess = "true";
        String errorMessage= StringUtils.EMPTY;
        String wbsMinNo=StringUtils.EMPTY;
        Map<String, String> responseMapper = new HashMap<>();
        try {

            List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                    .findByScServiceDetailIdAndServiceCodeAndAvailableAndComponentId(scServiceDetail.getId(),
                            scServiceDetail.getUuid(), "Y",componentId);

            WBSTransferRequest wbsTransferRequest = constructWbsTransferRequest(ivType, scServiceDetail,
                    cpeMaterialRequestDetails);

            AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(wbsTransferRequest), null,
                    serviceCode, type + "WBSTRANSFER", processInstanceId);



            LOGGER.info("placeMrsTransfer request for service code:{} and request:{}", serviceCode,
                    Utils.convertObjectToJson(wbsTransferRequest));

            RestResponse response = restClientService.postWithBasicAuthentication(sapHanaWbsTranseferUrl,
                    Utils.convertObjectToJson(wbsTransferRequest), createCommonHeader(), userName, password);
            inventory.setResponse(response.getData());
            auditLogRepositoryRepository.save(inventory);

            LOGGER.info("placeMrsTransfer for service code:{} and  response:{}", serviceCode,
                    Utils.convertObjectToJson(response));

            if (response.getStatus() == Status.SUCCESS) {
                WBSTransferResponse wbsTrasferResponse = Utils.convertJsonToObject(response.getData(),
                        WBSTransferResponse.class);

                if (wbsTrasferResponse != null && wbsTrasferResponse.getMaterialTransfer()!=null) {
                    if (!wbsTrasferResponse.getMaterialTransfer().getStatus().equalsIgnoreCase("Transferred")) {
                        wbsTransferSuccess = "false";
                        errorMessage=wbsTrasferResponse.getMaterialTransfer().getRemark();
                    }
                    else {
                        List<String> remark = Arrays.asList(wbsTrasferResponse.getMaterialTransfer().getRemark().split(":"));
                        if(remark.size()<2) {
                            wbsTransferSuccess = "false";
                            errorMessage="Obd number is not recieved";
                        }
                        else {
                            wbsMinNo = remark.get(1);
                        }
                    }
                } else {
                    wbsTransferSuccess = "false";
                    errorMessage="Wbs transfer failure";
                }
            } else {
                wbsTransferSuccess = "false";
                errorMessage="SYSTEM ERROR";
            }

            LOGGER.info("WBS Transfer for service code: {} and value:{}", scServiceDetail.getUuid(), wbsTransferSuccess);

            updatePurchaseRequest(scServiceDetail, cpeMaterialRequestDetails, "WBSTRANSFER", wbsTransferSuccess);
            //update wbs min number
            if(!wbsMinNo.isEmpty()) {
                if(cpeMaterialRequestDetails != null && !cpeMaterialRequestDetails.isEmpty()) {
                    for(CpeMaterialRequestDetails materialRequestDetails : cpeMaterialRequestDetails) {
                        materialRequestDetails.setWbsMinNumber(wbsMinNo.trim());
                        cpeMaterialRequestDetailsRepository.save(materialRequestDetails);
                    }
                }
            }
        } catch (Exception e) {
            errorMessage= CramerConstants.SYSTEM_ERROR;
            wbsTransferSuccess = "false";
            responseMapper.put("wbsTransfer", wbsTransferSuccess);
            responseMapper.put("errorMessage", errorMessage);
            return responseMapper;
        }
        responseMapper.put("wbsTransfer", wbsTransferSuccess);
        responseMapper.put("errorMessage", errorMessage);
        return responseMapper;
    }




    private void saveCpeMaterailRequestDetailsAndPrPoCheck(InventoryDetailRequest inventoryDetailRequest,
                                                           ScServiceDetail scServiceDetail, String type,
                                                           Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> filteredQuantityMap, DelegateExecution execution,
                                                           List<CpeMaterialRequestDetails> cpeMaterialRequestDetails, String level5Wbs, Integer overlayComponentId, boolean isNearsetInventory) throws TclCommonException, ParseException {


        Map<String, List<CpeMaterialRequestDetails>> cpeMaterialRequestDetailsMap = cpeMaterialRequestDetails.stream()
                .collect(Collectors.groupingBy(CpeMaterialRequestDetails::getMaterialCode));


        boolean isCpeAvailble = checkIsCpeAvailableOrNot(scServiceDetail, filteredQuantityMap, execution, cpeMaterialRequestDetailsMap);

        if(!isCpeAvailble && isNearsetInventory) {
            checMaterialQuantityInInventory(scServiceDetail.getUuid(), scServiceDetail.getId(), type, execution, overlayComponentId, false);
            return;
        }


        for (Map.Entry<String, List<CpeMaterialRequestDetails>> entry : cpeMaterialRequestDetailsMap.entrySet()) {
            List<CpeMaterialRequestDetails> cpeMaterialRequestDetailsList = entry.getValue();
            List<S4HANAOPTIMUSMaterialStockDisResponse> responses = filteredQuantityMap.get(entry.getKey());

            if(responses != null && !responses.isEmpty()) {
                for(int cpeIndex=0; cpeIndex<cpeMaterialRequestDetailsList.size(); cpeIndex++ ) {
                    for(int resIndex=cpeIndex; resIndex<responses.size();) {
                        saveCpeMaterialRequest(scServiceDetail, responses.get(resIndex), isCpeAvailble, level5Wbs,
                                cpeMaterialRequestDetailsList.get(cpeIndex));
                        break;
                    }
                }
            }
        }


        LOGGER.info("CpeMaterialRequestDetails saved,  service code {}", scServiceDetail.getUuid());
    }


    private boolean checkIsCpeAvailableOrNot(ScServiceDetail scServiceDetail,
                                             Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> filteredQuantityMap, DelegateExecution execution,
                                             Map<String, List<CpeMaterialRequestDetails>> cpeMaterialRequestDetailsMap) {
        boolean isCpeAvailble = false;
        boolean isOthersAvalible = true;

        for (Map.Entry<String, List<CpeMaterialRequestDetails>> entry : cpeMaterialRequestDetailsMap.entrySet()) {

            List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = entry.getValue();
            CpeMaterialRequestDetails cpeMaterialRequestDetail = cpeMaterialRequestDetails.get(0);
            if (entry.getKey() != null
                    && cpeMaterialRequestDetail.getCatagory().equalsIgnoreCase(MST_CATALOG_ROUTER_CATAGORY)
                    && filteredQuantityMap.get(entry.getKey()) != null
                    && Integer.valueOf(cpeMaterialRequestDetails.size())
                    .equals(filteredQuantityMap.get(entry.getKey()).size())) {
                isCpeAvailble = true;
                LOGGER.info("CPE is availabe for service code {}", scServiceDetail.getUuid());
            } else if (isOthersAvalible
                    && (cpeMaterialRequestDetail.getCatagory() == null
                    || !cpeMaterialRequestDetail.getCatagory().equalsIgnoreCase(MST_CATALOG_LICENSE_CATAGORY))
                    && (filteredQuantityMap.get(entry.getKey()) == null || !Integer.valueOf(cpeMaterialRequestDetails.size())
                    .equals(filteredQuantityMap.get(entry.getKey()).size()))) {
                LOGGER.info("isOthersAvalible for service code {} and value is  {} and materail code {}",scServiceDetail.getUuid(), false, entry.getKey());
                isOthersAvalible = false;
            }
        }

        if (isCpeAvailble) {
            if(execution != null) {
                execution.setVariable("isCpeAvailableInInventory", isCpeAvailble);
            }
            LOGGER.info("isCpeAvailableInInventory for service code:{} and value: {}", scServiceDetail.getUuid(),
                    isCpeAvailble);
        } else {
            if(execution != null) {
                execution.setVariable("cpePrPONeeded", true);
            }
            LOGGER.info("cpePrPoNeeded for service code:{} and value: {}", scServiceDetail.getUuid(), true);
        }

        if (isCpeAvailble && !isOthersAvalible) {
            if(execution != null) {
                execution.setVariable("cpePrPONeeded", isCpeAvailble);
            }
            LOGGER.info("cpePrPoNeeded for service code:{} and value:{}", scServiceDetail.getUuid(), true);
        }
        return isCpeAvailble;
    }


    private void saveCpeMaterialRequest(ScServiceDetail scServiceDetail, S4HANAOPTIMUSMaterialStockDisResponse response,
                                        boolean isCpeAvailble, String level5Wbs, CpeMaterialRequestDetails cpeMaterialRequestDetails) {
        cpeMaterialRequestDetails.setAvailable("Y");
        cpeMaterialRequestDetails.setMaterialCode(response.getMATERIALCODE());
        cpeMaterialRequestDetails.setPlant(response.getPLANT());
        cpeMaterialRequestDetails.setCategoryOfInventory(response.getCATEGORYOFINVENTORY());
        cpeMaterialRequestDetails.setPurchaseGroup(response.getPURCHASEGROUP());
        cpeMaterialRequestDetails.setValuationtType(response.getBatchValuationType());
        cpeMaterialRequestDetails.setScServiceDetailId(scServiceDetail.getId());
        cpeMaterialRequestDetails.setServiceCode(scServiceDetail.getUuid());
        cpeMaterialRequestDetails.setStorageLocation(response.getSTORAGELOCATION());
        cpeMaterialRequestDetails.setSapSerialNumber(response.getSAPSerialNumber());
        cpeMaterialRequestDetails.setOemSerialNumber(response.getOEMSerialNumber());
        cpeMaterialRequestDetails.setUnitOfMeasure(response.getUNITOFMEASURE());
        cpeMaterialRequestDetails.setFormWbsNumber(response.getWBSNUMBER());
        cpeMaterialRequestDetails.setToWbsNumber(level5Wbs);
        cpeMaterialRequestDetails.setPoNumber(response.getPONumber());
        cpeMaterialRequestDetailsRepository.save(cpeMaterialRequestDetails);
    }

    private void updatePurchaseRequest(ScServiceDetail scServiceDetail,
                                       List<CpeMaterialRequestDetails> cpeMaterialRequestDetails, String purchaseType, String wbsTransferSuccess) {
        if (!cpeMaterialRequestDetails.isEmpty()) {
            cpeMaterialRequestDetails.forEach(cpeMat -> {
                cpeMat.setCpePurchaseType(purchaseType);
                cpeMat.setWbsTransferDate(new Timestamp(new Date().getTime()));
                if(wbsTransferSuccess.equalsIgnoreCase("true")) {
                    cpeMat.setWbsTransferStatus("Y");
                }else {
                    cpeMat.setWbsTransferStatus("N");
                }
                cpeMaterialRequestDetailsRepository.save(cpeMat);
            });

        }
    }



    @Transactional(readOnly = false)
    public void saveCpeMaterielReqDetails(ScServiceDetail scServiceDetail, Map<String, Object> processVar) throws TclCommonException {

        LOGGER.info("Save CPE materail request started ... for service code {}", scServiceDetail.getUuid());

        List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(scServiceDetail.getId());
        processVar.put("wbsRequestNeeded", true);

        List<CpeBomResource> cpeBomResources = new ArrayList<>();

        for (ScComponent scComponent : scComponents) {

            ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
                    .findFirstByScComponent_idAndAttributeName(scComponent.getId(), "CPE Basic Chassis");

            ScComponentAttribute destinationState = scComponentAttributesRepository
                    .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                            scServiceDetail.getId(), "destinationState", "LM", "A");

            MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = null;

            if(destinationState != null) {

                List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
                        .findByState(destinationState.getAttributeValue());

                LOGGER.info(
                        "distributionCenterMapping with service code:{} and state:{}",
                        scServiceDetail.getUuid(), destinationState.getAttributeValue());

                if(distributionCenterMapping != null) {
                    mstStateToDistributionCenterMapping = distributionCenterMapping
                            .stream().findFirst().orElse(null);
                }

            }



            if(scComponentAttribute != null) {
                LOGGER.info("constructQuantityRequest cpeChassisAttr for servicecode:{} and cpeChassisAttr is:{}",
                        scServiceDetail.getUuid(), scComponentAttribute);


                LOGGER.info("constructQuantityRequest scServiceAttribute for servicecode:{} and cpeChassisAttr is:{}",
                        scServiceDetail.getUuid(), scComponentAttribute);

                String serviceParamId = scComponentAttribute.getAttributeValue();

                LOGGER.info("constructQuantityRequest serviceParamId for servicecode:{} and cpeChassisAttr is:{}",
                        scServiceDetail.getUuid(), serviceParamId);

                if (StringUtils.isNotBlank(serviceParamId)) {
                    Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
                            .findById(Integer.valueOf(serviceParamId));
                    if (scAdditionalServiceParam.isPresent()) {
                        String bomResponse = scAdditionalServiceParam.get().getValue();

                        LOGGER.info("l bomResponse for servicecode:{} and bomResponse is:{}",
                                scServiceDetail.getUuid(), bomResponse);

                        CpeBomResource[] bomResourcess = Utils.convertJsonToObject(bomResponse, CpeBomResource[].class);
                        cpeBomResources.add(bomResourcess[0]);

                        List<MstCostCatalogue> mstCostCatalogues = mstCostCatalogueRepository
                                .findByDistinctBundledBomGroupByMaterailCode(bomResourcess[0].getBomName());
                        boolean isCpeLicenseNeeded = false;
                        if(scServiceDetail != null) {
                            for (MstCostCatalogue mstCostCatalogue : mstCostCatalogues) {
                                if(mstCostCatalogue.getCategory()!= null && mstCostCatalogue.getCategory().equalsIgnoreCase(MST_CATALOG_LICENSE_CATAGORY)) {
                                    isCpeLicenseNeeded = true;
                                }
                                for (int index = 1; index <= mstCostCatalogue.getQuantity(); index++) {
                                    CpeMaterialRequestDetails cpeMaterialRequestDetails = new CpeMaterialRequestDetails();
                                    cpeMaterialRequestDetails.setAvailable("N");
                                    cpeMaterialRequestDetails.setServiceCode(scServiceDetail.getUuid());
                                    cpeMaterialRequestDetails.setScServiceDetailId(scServiceDetail.getId());
                                    cpeMaterialRequestDetails.setCreatedDate(mstCostCatalogue.getCreatedDate());
                                    cpeMaterialRequestDetails.setBundledBom(mstCostCatalogue.getBundledBom());
                                    cpeMaterialRequestDetails.setQuantity(Double.valueOf(index).toString());
                                    cpeMaterialRequestDetails.setComponentId(null);
                                    cpeMaterialRequestDetails.setVendorCode(mstCostCatalogue.getVendorCode());
                                    cpeMaterialRequestDetails.setVendorName(mstCostCatalogue.getVendorName());
                                    cpeMaterialRequestDetails.setCalculatedPrice(null);
                                    cpeMaterialRequestDetails.setPerListPriceUsd(mstCostCatalogue.getTotalListPriceUsd());
                                    cpeMaterialRequestDetails.setCurrency(mstCostCatalogue.getCurrency());
                                    cpeMaterialRequestDetails.setConversion(mstCostCatalogue.getConversion());
                                    cpeMaterialRequestDetails.setHsnCode(mstCostCatalogue.getHsnCode());
                                    cpeMaterialRequestDetails.setIncrementalRate(mstCostCatalogue.getIncrementalRate());
                                    cpeMaterialRequestDetails.setProcurementDiscountPercentage(mstCostCatalogue.getProcurementDiscountPercentage());
                                    cpeMaterialRequestDetails.setMaterialCode(mstCostCatalogue.getMaterialCode());
                                    cpeMaterialRequestDetails.setComponentId(scComponent.getId());
                                    cpeMaterialRequestDetails.setCatagory(mstCostCatalogue.getCategory());
                                    cpeMaterialRequestDetails.setInstallationCost(mstCostCatalogue.getInstallationCost());
                                    cpeMaterialRequestDetails.setInstallationServiceNumber(mstCostCatalogue.getInstallationServiceNumber());
                                    cpeMaterialRequestDetails.setInstallationShortText(mstCostCatalogue.getInstallationShortText());
                                    cpeMaterialRequestDetails.setInstallationVendorId(mstCostCatalogue.getInstallationVendorId());
                                    cpeMaterialRequestDetails.setInstallationVendorName(mstCostCatalogue.getInstallationVendorName());
                                    cpeMaterialRequestDetails.setSupportCost(mstCostCatalogue.getSupportCost());
                                    cpeMaterialRequestDetails.setSupportServiceNumber(mstCostCatalogue.getSupportServiceNumber());
                                    cpeMaterialRequestDetails.setSupportShortText(mstCostCatalogue.getSupportShortText());
                                    cpeMaterialRequestDetails.setSupportVendorId(mstCostCatalogue.getSupportVendorId());
                                    cpeMaterialRequestDetails.setSupportVendorName(mstCostCatalogue.getSupportVendorName());
                                    cpeMaterialRequestDetails.setPerListPriceInr(mstCostCatalogue.getPerListPriceInr());
                                    cpeMaterialRequestDetails.setTotalListPriceInr(mstCostCatalogue.getTotalListPriceInr());
                                    cpeMaterialRequestDetails.setDiscount(mstCostCatalogue.getDiscount());
                                    cpeMaterialRequestDetails.setPerTotalPriceInr(mstCostCatalogue.getPerTotalPriceInr());
                                    cpeMaterialRequestDetails.setTpInr(mstCostCatalogue.getTpInr());
                                    cpeMaterialRequestDetails.setFreight(mstCostCatalogue.getFreight());
                                    cpeMaterialRequestDetails.setCustomDuty(mstCostCatalogue.getCustomDuty());
                                    cpeMaterialRequestDetails.setTotalPriceInr(mstCostCatalogue.getTotalPriceInr());
                                    cpeMaterialRequestDetails.setTotalTpInr(mstCostCatalogue.getTotalTpInr());
                                    cpeMaterialRequestDetails.setDistiMargin(mstCostCatalogue.getDistiMargin());
                                    cpeMaterialRequestDetails.setProductCode(mstCostCatalogue.getProductCode());
                                    cpeMaterialRequestDetails.setDescription(mstCostCatalogue.getDescription());
                                    if(mstStateToDistributionCenterMapping != null) {
                                        cpeMaterialRequestDetails
                                                .setPlant(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
                                        cpeMaterialRequestDetails.setStorageLocation(String.valueOf(mstStateToDistributionCenterMapping
                                                .getMasterTclDistributionCenter().getSapStorageLocation()));
                                        cpeMaterialRequestDetails.setDistributionCenterAddress(
                                                mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()
                                                        .getDistributionCenterAddress());
                                        cpeMaterialRequestDetails.setDistributionCenterName(
                                                mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()
                                                        .getDistributionCenterName());
                                    }
                                    cpeMaterialRequestDetailsRepository.save(cpeMaterialRequestDetails);
                                }
                            }
                        }

                        if(isCpeLicenseNeeded) {
                            processVar.put("cpeLicenseNeeded", true);
                            LOGGER.info("cpeLicenseNeeded is {} for service code {}", isCpeLicenseNeeded, scServiceDetail.getUuid());
                        } else {
                            LOGGER.info("cpeLicenseNeeded is {} for service code {}", isCpeLicenseNeeded, scServiceDetail.getUuid());
                            processVar.put("cpeLicenseNeeded", false);
                        }

                    }
                }
            }
        }
        LOGGER.info("Save CPE materail request ended ... for service code {}", scServiceDetail.getUuid());

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
        return auditLogRepositoryRepository.save(networkInventory);

    }




    private WBSTransferRequest constructWbsTransferRequest(String ivType, ScServiceDetail scServiceDetail,
                                                           List<CpeMaterialRequestDetails> cpeMaterialRequestDetails) {
        WBSTransferRequest wbsTransferRequest = new WBSTransferRequest();
        if(cpeMaterialRequestDetails != null && !cpeMaterialRequestDetails.isEmpty()) {
            String cpeType = null;
            if (ivType.toLowerCase().contains("outright")) {
                cpeType = "Outright";
            } else {
                cpeType = "Rental";
            }
            ScComponentAttribute attribute = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "level5Wbs", "LM", "A");
            LOGGER.info(
                    "constructMaterialTransferRequest  for service code:{} and  level5Wbs:{} and cpetype is:{}",
                    scServiceDetail.getUuid(), attribute, cpeType);
            MaterialTransfer materialTransfer = new MaterialTransfer();
            materialTransfer.setServiceId(scServiceDetail.getUuid());
            materialTransfer.setDocumentDate(formatter.format(new Date()));
            materialTransfer.setPostingDate(formatter.format(new Date()));
            List<WBSMaterialDetail> wbsMaterialDetails = new ArrayList<>();
            for (CpeMaterialRequestDetails cpeMaterialRequestDetail : cpeMaterialRequestDetails) {
                WBSMaterialDetail materialdetails = new WBSMaterialDetail();
                materialdetails.setFromValuationType("NEW");
                materialdetails.setReceivingValuationType("NEW");
                materialdetails.setMATERIALCODE(cpeMaterialRequestDetail.getMaterialCode());
                materialdetails.setPlant(cpeMaterialRequestDetail.getPlant());
                materialdetails.setQuantity(cpeMaterialRequestDetail.getQuantity());
                //check
                materialdetails.setRecvgStorageLocation((cpeMaterialRequestDetail.getStorageLocation()!=null && !cpeMaterialRequestDetail.getStorageLocation().isEmpty())?Integer.parseInt(cpeMaterialRequestDetail.getStorageLocation()): null);
                materialdetails.setRecvgWBSElement(attribute.getAttributeValue());
                materialdetails.setWBSElement(cpeMaterialRequestDetail.getFormWbsNumber());
                SerialNo serialNo = new SerialNo();
                serialNo.setSerialNumber(cpeMaterialRequestDetail.getOemSerialNumber());
                materialdetails.setSerialNo(serialNo);
                materialdetails.setStorageLocation((cpeMaterialRequestDetail.
                        getStorageLocation()!=null && !cpeMaterialRequestDetail.getStorageLocation().isEmpty())
                        ?cpeMaterialRequestDetail.getStorageLocation():null);
                materialdetails.setUnitOfEntry(cpeMaterialRequestDetail.getQuantity());
                wbsMaterialDetails.add(materialdetails);
            }
            materialTransfer.setMATERIALDETAILS(wbsMaterialDetails);
            wbsTransferRequest.setMaterialTransfer(materialTransfer);
            LOGGER.info("wbsTransfer request:{}",Utils.toJson(wbsTransferRequest));
        }
        return wbsTransferRequest;
    }



    public boolean placeMrnOrder(String serviceCode, String type,
                                 String invType, String typeOfExpenses, ScServiceDetail scServiceDetail, String processInstanceId,
                                 String taskName, String siteType, Integer componentId) throws TclCommonException {

        MTNCreationResponse mtnCreationReponse = null;
        MTNCreationRequest mtnCreationRequest = new MTNCreationRequest();


        Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributes(
                scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

        List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
                .findByState(scComponentAttributesAMap.get("destinationState"));

        MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping.stream()
                .findFirst().orElse(null);
        constructMrnCreationRequest(scServiceDetail, mtnCreationRequest, type, mstStateToDistributionCenterMapping,
                invType, typeOfExpenses, scComponentAttributesAMap, componentId);
        AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(mtnCreationRequest), null,
                serviceCode, "PLACEMRN", processInstanceId);

        LOGGER.info("mrnPlaceOrderRequest data for service code:{} and mrnPlaceOrderRequest :{}", serviceCode,
                Utils.toJson(mtnCreationRequest));

        RestResponse response = restClientService.postWithBasicAuthentication(mrnHanaMuxCreationUrl,
                Utils.convertObjectToJson(mtnCreationRequest), createCommonHeader(), mtnUserName, mtnPassword);
        inventory.setResponse(response.getData());
        auditLogRepositoryRepository.save(inventory);

        LOGGER.info("mrnPlaceOrders response status{}:sap response data{}:error respone {}", response.getStatus(),
                response.getData(), response.getErrorMessage());

        if (response.getStatus() == Status.SUCCESS) {
            mtnCreationReponse = Utils.convertJsonToObject(response.getData(), MTNCreationResponse.class);
            LOGGER.info("mrnCreationReponse data  for service code:{} and mrnCreationReponse:{}", serviceCode,
                    mtnCreationReponse);

            if (mtnCreationReponse != null && mtnCreationReponse.getMTS4HANAOPTIMUSMTNResponse() != null
                    && !mtnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN().isEmpty()
                    && (mtnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN().get(0).getStatus()
                    .equalsIgnoreCase("Sucess")
                    || mtnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN().get(0)
                    .getStatus().equalsIgnoreCase("Success"))) {

                Map<String, String> mapper = new HashMap<>();
                String remark=mtnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN()
                        .get(0).getRemark();
                LOGGER.info("remark:{}",remark);
                if (type.equalsIgnoreCase("MUX")) {
                    mapper.put("muxMrnNo", remark.split(":")[1]);
                    mapper.put("muxMrnStatus", mtnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN()
                            .get(0).getStatus());
                    if (mtnCreationRequest.getOPTIMUSS4HANAMTN() != null) {
                        mapper.put("muxOptimusId", mtnCreationRequest.getOPTIMUSS4HANAMTN().getOptimusId());
                    }
                    mapper.put("muxMrnCreationDate", DateUtil.convertDateToString(new Date()));
                } else if (type.equalsIgnoreCase("CPE")) {
                    mapper.put("cpeMrnNo", remark.split(":")[1]);
                    mapper.put("cpeMrnStatus", mtnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN()
                            .get(0).getStatus());
                    mapper.put("cpeMrnCreationDate", DateUtil.convertDateToString(new Date()));
                    if (mtnCreationRequest.getOPTIMUSS4HANAMTN() != null) {
                        mapper.put("muxOptimusId", mtnCreationRequest.getOPTIMUSS4HANAMTN().getOptimusId());
                    }
                }
                List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository.findByScServiceDetailIdAndComponentId(scServiceDetail.getId(), componentId);
                saveMrnCreation(mtnCreationReponse, remark.split(":")[1],scServiceDetail, processInstanceId, type, taskName,cpeMaterialRequestDetails,null);
                componentAndAttributeService.updateAttributes(scServiceDetail.getId(), mapper,
                        AttributeConstants.COMPONENT_LM, siteType);
                return true;
            }

        }

        return false;

    }


    private void constructMrnCreationRequest(ScServiceDetail scServiceDetail,
                                             MTNCreationRequest mtnCreationRequest, String type,
                                             MstStateToDistributionCenterMapping distributionCenterMapping, String invType, String typeOfExpenses, Map<String, String> scComponentAttributesAMap,
                                             Integer componentId)
            throws TclCommonException {
        String cpeType = null;
        if (invType.toLowerCase().contains("outright")) {
            cpeType = "Outright";
        } else {
            cpeType = "Rental";
        }

        Map<String, String> attributesMap = commonFulfillmentUtils.getComponentAttributesDetails(
                Arrays.asList("level5Wbs"), scServiceDetail.getId(), "LM", "A");

        LOGGER.info("constructMrnCreationRequest for serviceid:{} and cpeTye is:{}", scServiceDetail.getUuid(),
                cpeType);

        List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository.findByScServiceDetailIdAndComponentId(scServiceDetail.getId(), componentId);
        constructMrnPlaceRequest(cpeMaterialRequestDetails, mtnCreationRequest, scServiceDetail, attributesMap, cpeType,
                distributionCenterMapping, typeOfExpenses, type, scComponentAttributesAMap);

    }

    private void constructMrnPlaceRequest(List<CpeMaterialRequestDetails> cpeMaterialRequestDetails,
                                          MTNCreationRequest mtnCreationRequest, ScServiceDetail scServiceDetail, Map<String, String> attributesMap,
                                          String cpeType, MstStateToDistributionCenterMapping distributionCenterMapping, String typeOfExpenses,
                                          String type, Map<String, String> scComponentAttributesAMap) {
        String sapCode = DateUtil.generateUidForSap(6, type);
        S4HanaMTN mtnRequest = new S4HanaMTN();
        mtnRequest.setOptimusId(sapCode);
        mtnRequest.setMtnRequestType("GIO");
        mtnRequest.setCopfId(scServiceDetail.getScOrder().getUuid());
        mtnRequest.setProjectName(scServiceDetail.getScOrder().getErfCustLeName());
        mtnRequest.setMtnRequestCategory(cpeType.equalsIgnoreCase("Rental") ? "REN" : "SAL");
        mtnRequest.setDistributionCentre(
                distributionCenterMapping.getMasterTclDistributionCenter().getPlant());
        mtnRequest.setMtnDate(formatter.format(new Date()));
        mtnRequest.setIssueTo("On Tata Communications Ltd. A/C");
        mtnRequest.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
        mtnRequest.setDeliveryAdress(scComponentAttributesAMap.get("siteAddress"));
        mtnRequest.setDeliveryLocationCity(scComponentAttributesAMap.get("destinationCity"));
        mtnRequest.setDeliveryState(scComponentAttributesAMap.get("destinationState"));
        mtnRequest.setPinCode(scComponentAttributesAMap.get("destinationPincode"));
        mtnRequest.setContactName(scComponentAttributesAMap.get("localItContactName"));
        mtnRequest.setContactNo(scComponentAttributesAMap.get("localItContactMobile"));
        List<MaterialDetails> materialDetails = new ArrayList<>();
        for (CpeMaterialRequestDetails requestDetails : cpeMaterialRequestDetails) {
            MaterialDetails details = new MaterialDetails();
            details.setCircuitId(scServiceDetail.getUuid());
            details.setMaterialCode(requestDetails.getMaterialCode());
            details.setPlant(requestDetails.getPlant());
            details.setStorageLocation(requestDetails.getStorageLocation());
            details.setWbsNumber(attributesMap.getOrDefault("level5Wbs", null));
            details.setSerialNumber(requestDetails.getOemSerialNumber());
            materialDetails.add(details);
        }
        mtnRequest.setmaterialDetails(materialDetails);
        mtnCreationRequest.setOPTIMUSS4HANAMTN(mtnRequest);
        LOGGER.info("mrnCreationRequest inside getMrnPlaceRequest:{}", mtnCreationRequest);
    }
    private void getMrnPlaceRequest(SapQuantityAvailableRequest sapQuantityResponse,
                                    MTNCreationRequest mtnCreationRequest, ScServiceDetail scServiceDetail,
                                    Map<String, String> attributesMap, String cpeType,
                                    MstStateToDistributionCenterMapping distributionCenterMapping, String typeOfExpenses, String type, Map<String, String> scComponentAttributesAMap) {
        String sapCode = DateUtil.generateUidForSap(6, type);
        S4HanaMTN mtnRequest = new S4HanaMTN();
        mtnRequest.setOptimusId(sapCode);
        //confirm
        mtnRequest.setMtnRequestType("GIO");
        mtnRequest.setCopfId(scServiceDetail.getScOrder().getUuid());
        mtnRequest.setProjectName(scServiceDetail.getScOrder().getErfCustLeName());
        //confirm
        mtnRequest.setMtnRequestCategory(cpeType.equalsIgnoreCase("Rental") ? "REN" : "SAL");
        mtnRequest.setDistributionCentre(
                distributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
        mtnRequest.setMtnDate(formatter.format(new Date()));
        mtnRequest.setIssueTo("On Tata Communications Ltd. A/C");
        mtnRequest.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
        mtnRequest.setDeliveryAdress(scComponentAttributesAMap.get("siteAddress"));
        mtnRequest.setDeliveryLocationCity(scComponentAttributesAMap.get("destinationCity"));
        mtnRequest.setDeliveryState(scComponentAttributesAMap.get("destinationState"));
        mtnRequest.setPinCode(scComponentAttributesAMap.get("destinationPincode"));
        mtnRequest.setContactName(scComponentAttributesAMap.get("localItContactName"));
        mtnRequest.setContactNo(scComponentAttributesAMap.get("localItContactMobile"));
        List<MaterialDetails> materialDetails = new ArrayList<>();
        for (DisplayMaterialResponse displayMaterialResponse : sapQuantityResponse.getDisplayMaterial()) {
            MaterialDetails details = new MaterialDetails();
            details.setCircuitId(scServiceDetail.getUuid());
            details.setMaterialCode(displayMaterialResponse.getMaterialCode());
            details.setPlant(displayMaterialResponse.getPlant());
            details.setStorageLocation(displayMaterialResponse.getStorageLocation());
            if (cpeType.equalsIgnoreCase("Rental")) {
                details.setWbsNumber(displayMaterialResponse.getWBSNumber());
            }
            details.setSerialNumber(displayMaterialResponse.getSAPSerialNumber());
            materialDetails.add(details);
        }
        mtnRequest.setmaterialDetails(materialDetails);
        mtnCreationRequest.setOPTIMUSS4HANAMTN(mtnRequest);
        LOGGER.info("mrnCreationRequest inside getMrnPlaceRequest:{}",mtnCreationRequest);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public MrnSequenceNumber getMrnSequenceNumber(String serviceCode, String type) {
        MrnSequenceNumber mrnSequenceNumber = new MrnSequenceNumber();
        mrnSequenceNumber.setServiceCode(serviceCode);
        mrnSequenceNumber.setType(type);
        return mrnSequenceNumberRepository.save(mrnSequenceNumber);


    }

    private void getMrnPlaceRequestforOutright(ConfirmMaterialAvailabilityBean availabilityBean,
                                               MTNCreationRequest mtnCreationRequest, ScServiceDetail scServiceDetail,
                                               Map<String, String> attributesMap, String cpeType,
                                               MstStateToDistributionCenterMapping distributionCenterMapping, String typeOfExpenses, String type, Map<String, String> scComponentAttributesAMap) {
        String sapCode = DateUtil.generateUidForSap(6, type);


        S4HanaMTN mtnRequest = new S4HanaMTN();
        mtnRequest.setOptimusId(sapCode);
        //confirm
        mtnRequest.setMtnRequestType("GIO");
        mtnRequest.setCopfId(String.valueOf(scServiceDetail.getScOrder().getUuid()));
        mtnRequest.setProjectName(scServiceDetail.getScOrder().getErfCustLeName());
        //confirm
        mtnRequest.setMtnRequestCategory(cpeType.equalsIgnoreCase("Rental") ? "REN" : "SAL");
        mtnRequest.setDistributionCentre(
                distributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
        mtnRequest.setMtnDate(formatter.format(new Date()));
        mtnRequest.setIssueTo("On Tata Communications Ltd. A/C");
        mtnRequest.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
        mtnRequest.setDeliveryAdress(scComponentAttributesAMap.get("siteAddress"));
        mtnRequest.setDeliveryLocationCity(scComponentAttributesAMap.get("destinationCity"));
        mtnRequest.setDeliveryState(scComponentAttributesAMap.get("destinationState"));
        mtnRequest.setPinCode(scComponentAttributesAMap.get("destinationPincode"));
        mtnRequest.setContactName(scComponentAttributesAMap.get("localItContactName"));
        mtnRequest.setContactNo(scComponentAttributesAMap.get("localItContactMobile"));
        List<MaterialDetails> materialDetails = new ArrayList<>();
        if (availabilityBean != null && availabilityBean.getSerialNumber() != null
                && !availabilityBean.getSerialNumber().isEmpty()) {

            for (SerialNumberBean displayMaterialResponse : availabilityBean.getSerialNumber()) {

                MaterialDetails details = new MaterialDetails();
                details.setCircuitId(scServiceDetail.getUuid());
                details.setMaterialCode(displayMaterialResponse.getMaterialCode());
                details.setPlant(distributionCenterMapping.getMasterTclDistributionCenter().getPlant());
                details.setStorageLocation(String
                        .valueOf(distributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
                if (cpeType.equalsIgnoreCase("Rental")) {
                    details.setWbsNumber(attributesMap.getOrDefault("level5Wbs", null));
                }
                details.setSerialNumber(displayMaterialResponse.getSerialNumber());
                materialDetails.add(details);
            }
            mtnRequest.setmaterialDetails(materialDetails);
            mtnCreationRequest.setOPTIMUSS4HANAMTN(mtnRequest);
        }
        LOGGER.info("mrnCreationRequest inside getMrnPlaceRequestforOutright:{}",mtnCreationRequest);
    }

    private String constructSiteMuxAddress(Map<String, String> scComponentAttributesMap) {
        return scComponentAttributesMap.getOrDefault("muxDeliveryAddress", "") + ","
                + scComponentAttributesMap.getOrDefault("muxDeliveryLocality", "") + ","
                + scComponentAttributesMap.getOrDefault("muxDeliveryCity", "") + ","
                + scComponentAttributesMap.getOrDefault("muxDeliveryCountry", "") + ","
                + scComponentAttributesMap.getOrDefault("muxDeliveryPincode", "");

    }

    private InventoryDetailRequest constructQuantityRequest(ScServiceDetail scServiceDetail, String type,
                                                            Map<String, Double> materialQuantityMapping, MstStateToDistributionCenterMapping distributionCenterMapping,
                                                            Map<String, Double> licencedMaterialQuantityMapping,
                                                            List<CpeMaterialRequestDetails> cpeMaterialRequestDetails) throws TclCommonException {

        InventoryDetailRequest quantityCheckRequest = new InventoryDetailRequest();
        String purchangeGroup = null;
        if (type.equalsIgnoreCase("CPE")) {
            purchangeGroup = "C15";
        } else {
            purchangeGroup = "C01";
        }


        LOGGER.info("constructQuantityRequest cpeType for servicecode:{} ", scServiceDetail.getUuid());

        if (type.equalsIgnoreCase("cpe")) {

            if (cpeMaterialRequestDetails != null) {

                for (CpeMaterialRequestDetails cpeMaterialRequestDetail : cpeMaterialRequestDetails) {

                    OPTIMUSMaterialStockDi displayMaterial = new OPTIMUSMaterialStockDi();
                    displayMaterial.setBMBUNDLE("");

                    String materialCode = null;

                    if (cpeMaterialRequestDetail.getCatagory() != null
                            && cpeMaterialRequestDetail.getCatagory().equalsIgnoreCase(MST_CATALOG_LICENSE_CATAGORY)) {
                        displayMaterial.setMATERIALCODE(cpeMaterialRequestDetail.getMaterialCode());
                        materialCode = cpeMaterialRequestDetail.getMaterialCode();
                        if(licencedMaterialQuantityMapping.get(materialCode) != null) {
                            Double strQty = convertStrToDouble(cpeMaterialRequestDetail.getQuantity())
                                    + (licencedMaterialQuantityMapping.get(materialCode) != null ? licencedMaterialQuantityMapping.get(materialCode): 0.00 );
                            licencedMaterialQuantityMapping.put(materialCode, strQty);
                        }else {
                            licencedMaterialQuantityMapping.put(materialCode,
                                    convertStrToDouble(cpeMaterialRequestDetail.getQuantity()));
                        }
                    } else {
                        displayMaterial.setMATERIALCODE(cpeMaterialRequestDetail.getMaterialCode());
                        materialCode = cpeMaterialRequestDetail.getMaterialCode();
                        if(materialQuantityMapping.get(materialCode) != null) {
                            Double strQty = convertStrToDouble(cpeMaterialRequestDetail.getQuantity())
                                    + (materialQuantityMapping.get(materialCode) != null ? materialQuantityMapping.get(materialCode) : 0.00);
                            materialQuantityMapping.put(materialCode, strQty);
                        }else {
                            materialQuantityMapping.put(materialCode,
                                    convertStrToDouble(cpeMaterialRequestDetail.getQuantity()));
                        }
                    }

                    if(distributionCenterMapping != null) {
                        displayMaterial
                                .setPLANT(distributionCenterMapping.getMasterTclDistributionCenter().getPlant());
                        displayMaterial.setSTORAGELOCATION(String.valueOf(distributionCenterMapping
                                .getMasterTclDistributionCenter().getSapStorageLocation()));
                    }else {
                        displayMaterial.setPLANT("");
                        displayMaterial.setSTORAGELOCATION("");
                    }
                    displayMaterial.setPURCHASEGROUP("");
                    displayMaterial.setValuationType("NEW");
                    displayMaterial.setCATEGORYOFINVENTORY("");
                    quantityCheckRequest.getOPTIMUSMaterialStockDis().add(displayMaterial);
                }

            }
        }

        return quantityCheckRequest;
    }


    private Double convertStrToDouble(String strqty) {
        Double qty = 0.00;
        try {
            qty = strqty == null ? 0.00: Double.valueOf(strqty);
        } catch (Exception e) {
            LOGGER.error("Error while converting string to double String value is {} and error {}", strqty, e);
        }
        return qty;
    }

    private boolean isQuantityAvailable(Map<String, Double> hardWareMaterialQuantityMapping,
                                        Map<String, Double> licencedMaterialQuantityMapping, InventoryDetailResponse sapQuantityResponse, String type,
                                        Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> availableQuantityMap, DelegateExecution execution,
                                        ScServiceDetail scServiceDetail,String level5Wbs,boolean checkFromSameWbs) {
        boolean licenceAvailable = false;

        LOGGER.info("po number receiver service code:{} for check inventory:{} and level5Wbs no:{}", scServiceDetail.getUuid(), level5Wbs);

        if(sapQuantityResponse == null || sapQuantityResponse.getMTS4HANAOPTIMUSMaterialStockDisResponse() == null ||
                sapQuantityResponse.getMTS4HANAOPTIMUSMaterialStockDisResponse().getS4HANAOPTIMUSMaterialStockDisResponse() == null || sapQuantityResponse.getMTS4HANAOPTIMUSMaterialStockDisResponse()
                .getS4HANAOPTIMUSMaterialStockDisResponse().isEmpty()) {
            return false;
        }

        getCpeMaterialQuantity(sapQuantityResponse, level5Wbs, availableQuantityMap,checkFromSameWbs);



        LOGGER.info("availableQuantityMap size for  details for service code:{} and quantity size:{}",
                scServiceDetail.getUuid(), availableQuantityMap.size());

        LOGGER.info("availableQuantityMap details for service code:{}  and :{}", scServiceDetail.getUuid(),
                availableQuantityMap);
        if (availableQuantityMap.size() == 0) {
            return false;
        }

        if (type.equalsIgnoreCase("cpe")) {


            if (licencedMaterialQuantityMapping.size() > 0) {

                licenceAvailable = true;
                for (Map.Entry<String, Double> entry : licencedMaterialQuantityMapping.entrySet()) {

                    if (availableQuantityMap.containsKey(entry.getKey())) {

                        if (availableQuantityMap.get(entry.getKey()).size() >= entry.getValue()) {
                            LOGGER.info(
                                    "harware quantity for material code for request and reponse from sap size:{} and ",
                                    entry.getValue(), availableQuantityMap.get(entry.getKey()).size());
                        } else {
                            licenceAvailable = false;
                        }

                    } else {
                        licenceAvailable = false;
                    }

                }


                if(execution != null) {
                    execution.setVariable("isCpeLcAvailableInInventory", licenceAvailable);
                }
                LOGGER.info("isCpeLcAvailableInInventory for servicecode:{} and value:{}", scServiceDetail.getUuid(), licenceAvailable);

            }



        }

        return false;
    }



    private void getCpeMaterialQuantity(InventoryDetailResponse displayMaterialResponses, String level5Wbs,
                                        Map<String, List<S4HANAOPTIMUSMaterialStockDisResponse>> availableQuantityMap, boolean checkFromSameWbs) {


        for (S4HANAOPTIMUSMaterialStockDisResponse displayMaterialResponse : displayMaterialResponses.getMTS4HANAOPTIMUSMaterialStockDisResponse()
                .getS4HANAOPTIMUSMaterialStockDisResponse()) {

            if (displayMaterialResponse.getSTATUS().equalsIgnoreCase("AVAILABLE")) {
                if (checkFromSameWbs && level5Wbs != null
                        && level5Wbs.equalsIgnoreCase(displayMaterialResponse.getWBSNUMBER())) {
                    if (availableQuantityMap.containsKey(displayMaterialResponse.getMATERIALCODE())) {
                        List<S4HANAOPTIMUSMaterialStockDisResponse> valueDisplayRespone = availableQuantityMap
                                .get(displayMaterialResponse.getMATERIALCODE());
                        valueDisplayRespone.add(displayMaterialResponse);
                        availableQuantityMap.put(displayMaterialResponse.getMATERIALCODE(), valueDisplayRespone);

                    } else {
                        List<S4HANAOPTIMUSMaterialStockDisResponse> valueDisplayRespone = new ArrayList<>();
                        valueDisplayRespone.add(displayMaterialResponse);
                        availableQuantityMap.put(displayMaterialResponse.getMATERIALCODE(), valueDisplayRespone);
                    }

                } else {

                    if (availableQuantityMap.containsKey(displayMaterialResponse.getMATERIALCODE())) {
                        List<S4HANAOPTIMUSMaterialStockDisResponse> valueDisplayRespone = availableQuantityMap
                                .get(displayMaterialResponse.getMATERIALCODE());
                        valueDisplayRespone.add(displayMaterialResponse);
                        availableQuantityMap.put(displayMaterialResponse.getMATERIALCODE(), valueDisplayRespone);

                    } else {
                        List<S4HANAOPTIMUSMaterialStockDisResponse> valueDisplayRespone = new ArrayList<>();
                        valueDisplayRespone.add(displayMaterialResponse);
                        availableQuantityMap.put(displayMaterialResponse.getMATERIALCODE(), valueDisplayRespone);
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

    public MinResponse getMinStatus(MinResponse minResponseBean) throws TclCommonException {
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
     * processAutoPr - This method is used to process the autoPR
     *
     * @param serviceCode
     * @param dealType
     * @param processInstanceId
     * @return
     * @throws TclCommonException
     */
    public AutoPrPoRespones processAutoPr(String serviceCode, String dealType, boolean isMaterial,
                                          String processInstanceId, String typeOfExpenses, List<CpeMaterialRequestDetails> cpeMaterialRequestDetails, Integer componentId)
            throws TclCommonException {

        LOGGER.info("Inside processAutoPr {}", serviceCode);

        AutoPrPoRespones autoPRResponse = null;
        ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "INPROGRESS");

        if (scServiceDetail != null) {
            Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributes(
                    scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

            PR2PORequest autoPRRequest = constructAutoPrRequest(scServiceDetail, dealType, isMaterial,
                    processInstanceId, typeOfExpenses, scComponentAttributesAMap, cpeMaterialRequestDetails);
            LOGGER.info("PRPO request for service code {}, url {}, request {}", serviceCode, sapAutoPrUrl, Utils.convertObjectToJson(autoPRRequest));
            AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPRRequest), null, serviceCode, dealType + "PRPO", processInstanceId);
            RestResponse autoPrResponse = restClientService.postWithBasicAuthentication(sapAutoPrUrl,
                    Utils.convertObjectToJson(autoPRRequest), createCommonHeader(), userName, password);
            LOGGER.info("PRPO Response for service code {} response {}", serviceCode, Utils.convertObjectToJson(autoPrResponse));
            if (autoPrResponse.getStatus() == Status.SUCCESS) {
                inventory.setResponse(autoPrResponse.getData());
                auditLogRepositoryRepository.save(inventory);
                autoPRResponse = Utils.convertJsonToObject(autoPrResponse.getData(), AutoPrPoRespones.class);
            }
        }
        return autoPRResponse;
    }


    public AutoPrPoRespones processAutoPr(String serviceCode, String dealType, Boolean isMaterial, String typeOfExpenses, Integer componentId) throws TclCommonException {

        ScServiceDetail serviceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
        List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = null;
        if(isMaterial) {
            LOGGER.info("processAutoPr for hardware service code: {}", serviceDetail.getUuid());
            cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                    .findByScServiceDetailIdAndServiceCodeAndAvailableAndCatagoryNotAndComponentId(serviceDetail.getId(),
                            serviceDetail.getUuid(), "N", MST_CATALOG_LICENSE_CATAGORY, componentId);
        } else {
            LOGGER.info("processAutoPr for license service code: {}", serviceDetail.getUuid());
            cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                    .findByScServiceDetailIdAndServiceCodeAndAvailableAndCatagoryAndComponentId(serviceDetail.getId(),
                            serviceDetail.getUuid(), "N", MST_CATALOG_LICENSE_CATAGORY, componentId);
        }
        if(cpeMaterialRequestDetails == null || cpeMaterialRequestDetails.isEmpty()) {
            LOGGER.info("cpeMaterialRequestDetails not availble for service code: {}", serviceDetail.getUuid());
        }
        return processAutoPr(serviceCode, dealType, isMaterial, typeOfExpenses, null, cpeMaterialRequestDetails, componentId);
    }




    /**
     * constructAutoPrRequest - This method is used to construct the autoPRRequest
     *
     * @param scServiceDetail
     * @param invType
     * @param processInstanceId
     * @param scComponentAttributesAMap
     * @return
     */
    private PR2PORequest constructAutoPrRequest(ScServiceDetail scServiceDetail, String invType, boolean isMaterial,
                                                String processInstanceId, String typeOfExpenses, Map<String, String> scComponentAttributesAMap, List<CpeMaterialRequestDetails> cpeMaterialRequestDetails) throws TclCommonException {
        LOGGER.info("Entering the constructAutoPrRequest with serviceId {}", scServiceDetail.getUuid());
        PR2PORequest autoPrRequest = new PR2PORequest();
        List<PR2PO> prLines = new ArrayList<>();
        List<String> invalidMaterialCodes = new ArrayList<>();

        autoPrRequest.setPR2PO(prLines);
        Double exchangeValue = forexService.convertCurrencySapHana(Currencies.USD, Currencies.INR);
        LOGGER.info("Exchange Value {}", exchangeValue);
        if (!appEnv.equals("PROD")) {
            if (exchangeValue == null) {
                LOGGER.info("Going into the UAT default date mode with date 20210317");
                exchangeValue = forexService.convertCurrencyWithDate(Currencies.USD, Currencies.INR, "20210317");//THIS IS ONLY FOR UAT
                LOGGER.info("exchange Value {}", exchangeValue);
            } else {
                LOGGER.info("extracted exchange Value {}", exchangeValue);
            }
        }


        Map<String, CpeMaterialRequestDetails> materialCodeAndCpeMaterialRequestDetailsMap = new HashMap<>();

        //PR/PO should occur only once for each material
        for (CpeMaterialRequestDetails cpeMaterialRequestDetail : cpeMaterialRequestDetails) {
            if(materialCodeAndCpeMaterialRequestDetailsMap.get(cpeMaterialRequestDetail.getMaterialCode()) == null) {
                materialCodeAndCpeMaterialRequestDetailsMap.put(cpeMaterialRequestDetail.getMaterialCode(), cpeMaterialRequestDetail);
            }
        }


        for (Map.Entry<String, CpeMaterialRequestDetails> entry : materialCodeAndCpeMaterialRequestDetailsMap.entrySet()) {
            constructPrLine(scServiceDetail, invType, isMaterial, processInstanceId, prLines, entry.getValue(),
                    typeOfExpenses, exchangeValue, scComponentAttributesAMap, autoPrRequest, invalidMaterialCodes);

        }
        LOGGER.info("Material Codes not available in VMI :", invalidMaterialCodes);


        if (!invalidMaterialCodes.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(invalidMaterialCodes.get(0), "-");
            while (tokenizer.hasMoreTokens()) {
                tokenizer.nextToken();
                String createdDate = tokenizer.nextToken();
//                autoPrRequest.getPRHeader().setServiceIdQuoteRef("Service Id :" + scServiceDetail.getUuid() + " BACK_TO_BACK_ORDER - " + createdDate);
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
    private void constructPrLine(ScServiceDetail scServiceDetail, String invType, boolean isMaterial,
                                 String processInstanceId, List<PR2PO> prLines, CpeMaterialRequestDetails cpeMaterialRequestDetail, String typeOfExpenses,
                                 Double exchangeValue, Map<String, String> scComponentAttributesAMap, PR2PORequest autoPrRequest, List<String> invalidMaterialCodes) throws TclCommonException {

        String materialCode = cpeMaterialRequestDetail.getMaterialCode();
        String catagory = cpeMaterialRequestDetail.getCatagory();
        PR2PO prLine = new PR2PO();
        prLine.setCreatedBy("E9988");
        prLine.setRequisitioner("E9988");
        prLine.setMaterialCode(materialCode);
        prLine.setReqTrackingNumber(processInstanceId);
        prLine.setOpportunityRegion("INDIA");
        prLine.setQuantity(cpeMaterialRequestDetail.getQuantity());
        prLine.setVendorId(cpeMaterialRequestDetail.getVendorCode());
        prLine.setReceivedDate(formatter.format(scServiceDetail.getScOrder().getCreatedDate()));
        prLine.setCUID(scServiceDetail.getScOrder().getTpsSfdcCuid());
        prLine.setValuationType("NEW");
        prLine.setMaterialGroup("");
        String typeOfDeal = "";
        if (invType.toLowerCase().contains("outright")) {
            typeOfDeal = "OUTRIGHT";
        } else {
            typeOfDeal = "RENTAL";
        }
        prLine.setOpportunityCategory("CAT 1-2");
        prLine.setTypeOfDeal(typeOfDeal);
        if (scServiceDetail.getErfPrdCatalogProductName().equals("IAS")) {
            prLine.setCircuitType("ILL");
        }else {
            prLine.setCircuitType(scServiceDetail.getErfPrdCatalogProductName());
        }
        prLine.setCustomerName(scServiceDetail.getScOrder().getErfCustCustomerName());


        LOGGER.info("Inside constructPrLine {}", scServiceDetail.getUuid());

        TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(scServiceDetail.getId(), "install-cpe", "A");

        if (taskPlan != null) {
            LocalDateTime cpeTime = taskPlan.getPlannedStartTime().toLocalDateTime().minusDays(7);

            LOGGER.info("cpe time:{}", cpeTime);

            if (cpeTime.isAfter(LocalDateTime.now().plusDays(7))) {
                prLine.setDeliveryDate(formatter.format(Timestamp.valueOf(cpeTime)));


            } else {
                prLine.setDeliveryDate(formatter.format(Timestamp.valueOf(LocalDateTime.now().plusDays(7))));

            }

        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 3);
            prLine.setDeliveryDate(formatter.format(cal.getTime()));
        }


        addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "cpePoDeliveryDate", prLine.getDeliveryDate());

        List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
                .findByState(scComponentAttributesAMap.get("destinationState"));
        for (MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping : distributionCenterMapping) {
            prLine.setStorageLocation(String.valueOf(
                    mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
            prLine.setPlant(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
            break;
        }
        if (scComponentAttributesAMap.get("destinationCountry").equalsIgnoreCase("india")) {
            prLine.setPurchasingOrganisation("TCIN");
        } else {
            prLine.setPurchasingOrganisation("INTL");
        }
        prLine.setUnitOfMeasurement("NOS");
        List<MstCostCatalogue> mstCostCatalogues  = mstCostCatalogueRepository.findByMaterialCodeAndBundledBom(materialCode, cpeMaterialRequestDetail.getBundledBom());
        if (isMaterial && invType.toLowerCase().contains("outright")) {
            LOGGER.info("Inside constructPrLine for Hardware service code:{} and invType: {}", scServiceDetail.getUuid(), invType);
            VmIStockResponse inventoryCheckResponse = checkVmiStockInventory(scServiceDetail.getUuid(), "", processInstanceId, materialCode);

            LOGGER.info("inventoryCheckResponse: {} for service code: {}", inventoryCheckResponse, scServiceDetail.getUuid());

            VmiStockUpdate vmistockupdate = getAvailbleVmiStock(cpeMaterialRequestDetail, inventoryCheckResponse);

            LOGGER.info("available vmistockupdate : {} for service code: {}", vmistockupdate, scServiceDetail.getUuid());

            if (vmistockupdate != null) {
                if (prLines.isEmpty()) {
                    LOGGER.info("OLAITEMNO: {} and service code: {}", scServiceDetail.getUuid(), vmistockupdate.getOLAITEMNO());

                    prLine.setOlaNumber(vmistockupdate.getOLANUMBER());
                    prLine.setOlaItemNumber(vmistockupdate.getOLAITEMNO());
                }

            } else {
                invalidMaterialCodes.add(materialCode + "-" + cpeMaterialRequestDetail.getCreatedDate());
                if (prLines.isEmpty()) {
                    LOGGER.info("BACK_TO_BACK_ORDER for service code:{} and invType: {}", scServiceDetail.getUuid(), invType);
                }

            }

        } else if (!isMaterial) {
            LOGGER.info("Inside constructPrLine for License service code:{} and invType: {}", scServiceDetail.getUuid(), invType);
            prLine.setUnitOfMeasurement("AU");
            prLine.setUnitOfMeasurement1("EA");
            prLine.setServiceNo(cpeMaterialRequestDetail.getMaterialCode());
            //  prLine.setMaterialGroup("ITSWPERLC");// PROD Catelogue
        } else if (isMaterial && invType.toLowerCase().contains("rental")) {
            LOGGER.info("Inside constructPrLine for Hardware service code:{} and invType: {}", scServiceDetail.getUuid(), invType);


            VmIStockResponse inventoryCheckResponse = checkVmiStockInventory(scServiceDetail.getUuid(), "", processInstanceId, materialCode);

            LOGGER.info("inventoryCheckResponse: {} for service code: {}", inventoryCheckResponse, scServiceDetail.getUuid());

            VmiStockUpdate vmistockupdate = getAvailbleVmiStock(cpeMaterialRequestDetail, inventoryCheckResponse);

            LOGGER.info("available vmistockupdate : {} for service code: {}", vmistockupdate, scServiceDetail.getUuid());

            if (vmistockupdate != null) {
                if (prLines.isEmpty()) {

                    LOGGER.info("Inside constructPrLine for License service code:{} and invType: {}", scServiceDetail.getUuid(), invType);

                }
                prLine.setOlaNumber(vmistockupdate.getOLANUMBER());
                prLine.setOlaItemNumber(vmistockupdate.getOLAITEMNO());
                prLine.setVendorId("");
            } else {
                if (prLines.isEmpty()) {

                    LOGGER.info("Inside constructPrLine for Hardware service code:{} and invType: {}", scServiceDetail.getUuid(), invType);
                }
            }
        }
        prLine.setAcctAssignmentCategory("Q");


        Double totalValuationPrice = 0.0;
        Double totallLp = 0.0;
        Double incrementalPrice = 0.0;
        boolean isCurrencyconversionRequired = true;
        Double totalTpInInr = null;
        prLine.setCurrency("INR");
        for (MstCostCatalogue mstCostCatalogue : mstCostCatalogues) {
            LOGGER.info("Total Valuation Price before {} and total price after ddp {}", totalValuationPrice,
                    mstCostCatalogue.getTotalPriceDdp());
            if (mstCostCatalogue.getConversion() != null
                    && mstCostCatalogue.getConversion().equalsIgnoreCase("no")) {
                isCurrencyconversionRequired = false;
                LOGGER.info("Currency conversion not required for this catalog {}", mstCostCatalogue.getBundledBom());
                if(mstCostCatalogue.getCurrency() != null && mstCostCatalogue.getCurrency().equalsIgnoreCase("INR")) {
                    totalTpInInr = mstCostCatalogue.getTotalTpInr();
                } else if (mstCostCatalogue.getCurrency() != null && mstCostCatalogue.getCurrency().equalsIgnoreCase("USD")) {
                    totalTpInInr = mstCostCatalogue.getTotalPriceUsd();
                    prLine.setCurrency("USD");
                }
            }
            totalValuationPrice = totalValuationPrice + mstCostCatalogue.getTotalPriceDdp();
            totallLp = totallLp + mstCostCatalogue.getPerListPriceUsd();
            LOGGER.info("Total Valuation Price after  {}", totalValuationPrice);
            incrementalPrice = incrementalPrice + mstCostCatalogue.getIncrementalRate();
        }

        ScComponentAttribute level5Wbs = scComponentAttributesRepository
                .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "level5Wbs", "LM", "A");

        if (level5Wbs != null)
            prLine.setWBSElement(level5Wbs.getAttributeValue());// From Budget Matrix


        LOGGER.info("FOREX Rate for USD to INR is {}", exchangeValue);
        if (exchangeValue != null && prLine.getOlaNumber() == null && isCurrencyconversionRequired) {
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

        //If Currency Conversion not required then set Total TP in INR
        if(!isCurrencyconversionRequired && totalTpInInr != null) {
            if (isMaterial) {
                prLine.setValuationPrice(String.valueOf(totalTpInInr));
            } else {
                prLine.setGrossPrice(String.valueOf(totalTpInInr));
            }
        }


        prLine.setCOFRefNo(scServiceDetail.getScOrderUuid());
        prLine.setServiceId(scServiceDetail.getUuid());
        //If Its Hardware PO we should need to send Router as First Line Item
        if(catagory != null && catagory.equalsIgnoreCase(MST_CATALOG_ROUTER_CATAGORY) && prLines.size() > 1) {
            prLines.add(0, prLine);
        }else {
            prLines.add(prLine);
        }
    }


    private VmiStockUpdate getAvailbleVmiStock(CpeMaterialRequestDetails cpeMaterialRequestDetail,
                                               VmIStockResponse inventoryCheckResponse) {
        if (inventoryCheckResponse != null
                && inventoryCheckResponse.getVMISTOCKUPDATE() != null
                && !inventoryCheckResponse.getVMISTOCKUPDATE().isEmpty()) {
            TreeMap<Date, VmiStockUpdate> sortedMap = new TreeMap<>();
            for (VmiStockUpdate vmistockupdate : inventoryCheckResponse.getVMISTOCKUPDATE()) {
                String strQty = vmistockupdate.getOPENQTY();
                if(strQty != null && cpeMaterialRequestDetail.getQuantity() != null) {
                    Double qty = Double.valueOf(strQty);
                    if(Double.valueOf(cpeMaterialRequestDetail.getQuantity()) <= qty && vmistockupdate.getVALIDITYENDDATE() != null
                            && !vmistockupdate.getVALIDITYENDDATE().isEmpty()) {
                        sortedMap.put(DateUtil.convertStringToDateHypen(vmistockupdate.getVALIDITYENDDATE()), vmistockupdate);
                    }
                }
            }
            if(!sortedMap.isEmpty()) {
                return sortedMap.lastEntry().getValue();
            }
        }
        return null;
    }

    /**
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
            typeOfDeal = "OUTRIGHT";
        } else {
            typeOfDeal = "RENTAL";
        }
        prHeader.setOpportunityCategory("CAT 1-2");
        prHeader.setTypeOfDeal(typeOfDeal);
        prHeader.setCircuitType(productName);
        prHeader.setCOPFId("");// TODO - Get from L20
        prHeader.setCUID(scServiceDetail.getScOrder().getTpsSfdcCuid());
        prHeader.setReceivedDate(formatter.format(scServiceDetail.getScOrder().getCreatedDate()));
        prHeader.setCustomerName(scServiceDetail.getScOrder().getErfCustCustomerName());
        return prHeader;
    }

    public void processPoStatusResponse(PoStatusResponseBean poStatusBean) {
        LOGGER.info("Entering processPoStatusResponse...");
        if (Objects.nonNull(poStatusBean.getPoStatusBean())) {
            PoStatusBean poStatus = poStatusBean.getPoStatusBean();
            LOGGER.info("processPoStatusResponse PO Number={}", poStatus.getPONumber());
            Optional<ProCreation> proCreationOp = proCreationRepository.findByPoNumber(poStatus.getPONumber());
            if (proCreationOp.isPresent()) {
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
                                saveTaskLogForPo(proCreation, poStatus);
                                runtimeService.trigger(execution.getId());
                            }
                        });

                Integer serviceId = proCreation.getServiceId();
                componentAndAttributeService.updateAttributes(serviceId, mapper, AttributeConstants.COMPONENT_LM, "A");


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
                createProcessTaskLog(task, "PO RELEASED", message, null, null);
            }
        } catch (Exception e) {
            LOGGER.info("saveTaskLogForPo for service code:{} and error:{}", proCreation.getServiceCode(), e);
        }

    }

    public void processMinResponse(MinResponse minResponseBean) throws TclCommonException {
        LOGGER.info("Entering processMinResponse... for MRN no:{} and response:{}", minResponseBean.getGoodsIssue().get(0).getMrnNo(), Utils.convertObjectToJson(minResponseBean));
        if (Objects.nonNull(minResponseBean.getGoodsIssue()) && !minResponseBean.getGoodsIssue().isEmpty()) {

            MinGoodsIssueResponse minResponse = minResponseBean.getGoodsIssue().get(0);

            //process wbs min response from sap
            if(minResponse.getMovementType()!=null && !minResponse.getMovementType().isEmpty() && minResponse.getMovementType().equalsIgnoreCase("415")) {
                processWbsMinResponse(minResponse);
                return;
            }

            DispatchCPEBean dispatchCPEBean= new DispatchCPEBean();
            dispatchCPEBean.setCpeSerialNumber(StringUtils.EMPTY);

            Optional<MrnCreationEntity> mrnCreationOp = mrnCreationRepository.findByMrnNumber(minResponse.getMrnNo());
            LOGGER.info("Entering processMinResponse... for MRN no:{} and Min no:{} and is present:{}", minResponse.getMrnNo(), minResponse.getObdNo(),mrnCreationOp.isPresent());

            if (mrnCreationOp.isPresent()) {
                MrnCreationEntity mrnCreation = mrnCreationOp.get();

                mrnCreation.setCourierName(minResponse.getCourierName());
                mrnCreation.setMinNumer(minResponse.getObdNo());
                mrnCreation.setVehicleDockertNumber(minResponse.getVehDocketNo());
                mrnCreation.setMovementTye(minResponse.getMovementType());
                mrnCreation.setMinStatus("Transferred");

                List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                        .findByMrnNumber(minResponse.getMrnNo());
                if(cpeMaterialRequestDetails != null && !cpeMaterialRequestDetails.isEmpty()) {
                    cpeMaterialRequestDetails.forEach(cpe -> {
                        //dispatchCPEBean.setCpeSerialNumber(cpe.getOemSerialNumber());
                        if(cpe.getCatagory().toLowerCase().equalsIgnoreCase("router") && dispatchCPEBean.getCpeSerialNumber().isEmpty()) {
                            dispatchCPEBean.setCpeSerialNumber(cpe.getOemSerialNumber());
                        }
                        cpe.setMinNumber(minResponse.getObdNo());
                        cpe.setVehicleDockertNumber(minResponse.getVehDocketNo());
                        cpe.setCourierName(minResponse.getCourierName());
                        cpe.setMovementType(minResponse.getMovementType());
                        cpe.setPostingDate(DateUtil.convertStringToTimeStampDDMMYYYWithHypen(minResponse.getPostingDate()));
                        cpe.setDocumentDate(DateUtil.convertStringToTimeStampDDMMYYYWithHypen(minResponse.getDocumentDate()));
                        cpe.setMinStatus("Transferred");
                        cpeMaterialRequestDetailsRepository.save(cpe);
                    });
                }

                Integer serviceId = mrnCreation.getServiceId();
                Optional.ofNullable(mrnCreationRepository.save(mrnCreation))
                        .ifPresent(mrn -> {
                            LOGGER.info("Triggering task {} for minNumber {}", mrn.getTaskName() + "_async", mrn.getMinNumer());
                            Execution execution = runtimeService.createExecutionQuery().processInstanceId(mrn.getProcessInstanceId())
                                    .activityId("generate-cpe-mrn-async").singleResult();
                            LOGGER.info(
                                    "Entering execution... for MRN no :{}and is taskName:{} and ProcessinstanceId:{}",
                                    minResponse.getObdNo(), mrn.getTaskName(), mrn.getProcessInstanceId());

                            runtimeService.setVariable(execution.getId(), "minStatus", true);

                            runtimeService.trigger(execution.getId());

                        });
                Map<String, String> mapper = new HashMap<>();
                mapper.put("muxMinNo", minResponse.getObdNo());
                mapper.put("muxMinStatus", "Transferred");
                componentAndAttributeService.updateAttributes(serviceId, mapper, AttributeConstants.COMPONENT_LM, "A");

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

                //auto close dispatch cpe
                //update attribuets
                Map<String, String> atMap = new HashMap<>();
                atMap.put("cpeMinNumber", minResponse.getObdNo());
                atMap.put("cpeMrnNumber", minResponse.getMrnNo());
                atMap.put("courierDispatchVendorName", minResponse.getCourierName());
                atMap.put("vehicleDocketTrackNumber", dispatchCPEBean.getVehicleDocketTrackNumber());
                atMap.put("cpeDispatchDate", DateUtil.convertDateToString(new Date()));
                componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_LM,"A");
                dispatchCPEBean.setCpeMinNumber(minResponse.getObdNo());
                dispatchCPEBean.setCpeMrnNumber(minResponse.getMrnNo());
                dispatchCPEBean.setCourierDispatchVendorName(minResponse.getCourierName());
                dispatchCPEBean.setVehicleDocketTrackNumber(minResponse.getVehDocketNo());
                dispatchCPEBean.setCpeDispatchDate(DateUtil.convertDateToString(new Date()));
                Task dispatchCpeTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId, "dispatch-cpe");
                if (dispatchCpeTask != null && !dispatchCpeTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
                    dispatchCPEBean.setTaskId(dispatchCpeTask.getId());
                    dispatchCPEBean.setWfTaskId(dispatchCpeTask.getWfTaskId());
                    processTaskLogDetails(dispatchCpeTask, "CLOSED", null, "Min response received from sap", null);
                    flowableBaseService.taskDataEntry(dispatchCpeTask, dispatchCPEBean);
                }
            }
        }
    }

    public void processWbsMinResponse(MinGoodsIssueResponse minResponse) {
        LOGGER.info("Inside processWbsMinResponse with movement type:{}", minResponse.getMovementType());
        Integer serviceId = null;
        List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                .findByWbsMinNumber(minResponse.getObdNo());
        if (cpeMaterialRequestDetails != null && !cpeMaterialRequestDetails.isEmpty()
                && minResponse.getMaterialDetails() != null && !minResponse.getMaterialDetails().isEmpty()) {
            if (cpeMaterialRequestDetails.size() == minResponse.getMaterialDetails().size()) {
                Map<String, CpeMaterialRequestDetails> materialCodeMap = cpeMaterialRequestDetails.stream()
                        .collect(Collectors.toMap(CpeMaterialRequestDetails::getMaterialCode, cpeReq -> cpeReq));
                for (MinMaterailDetails materialDetail : minResponse.getMaterialDetails()) {
                    CpeMaterialRequestDetails cpeMaterialRequestDetail = materialCodeMap
                            .get(materialDetail.getMaterialCode().trim());
                    if (cpeMaterialRequestDetail != null) {
                        if (serviceId == null)
                            serviceId = cpeMaterialRequestDetail.getScServiceDetailId();
                        LOGGER.info("Wbs serial number {} for service code {}", materialDetail.getOemSerailNumber(),
                                cpeMaterialRequestDetail.getServiceCode());
                        if (materialDetail.getOemSerailNumber() != null
                                && !materialDetail.getOemSerailNumber().isEmpty()
                                && !materialDetail.getOemSerailNumber().equalsIgnoreCase("NULL")) {
                            cpeMaterialRequestDetail.setWbsSerialNumber(materialDetail.getOemSerailNumber());
                            cpeMaterialRequestDetailsRepository.save(cpeMaterialRequestDetail);
                        }
                    }
                }
            }

            LOGGER.info("Triggering task {} for wbsMinNumber {}", "wbs-transfer-mrn-async", minResponse.getObdNo());
            Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,
                    "wbs-transfer-mrn-async");
            if (task != null) {
                Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId())
                        .activityId("wbs-transfer-mrn-async").singleResult();
                LOGGER.info("Entering execution... for wbsMinNo :{}and is taskName:{} and ProcessinstanceId:{}",
                        minResponse.getObdNo(), task.getMstTaskDef().getName(), task.getWfProcessInstId());
                runtimeService.trigger(execution.getId());
            }
        }
    }

    public MrnCreationEntity saveMrnCreation(MTNCreationResponse mrnCreationReponse, String mrnNumber,
                                             ScServiceDetail scServiceDetail, String processInstanceId, String type, String taskName,
                                             List<CpeMaterialRequestDetails> cpeMaterialRequestDetails, String minNumber) {

        Optional<MrnCreationEntity> optional = mrnCreationRepository.findByServiceIdAndType(scServiceDetail.getId(), type);
        MrnCreationEntity mrnCreationEntity = null;
        String mrnNo=StringUtils.EMPTY;
        String minNo=StringUtils.EMPTY;
        if (optional.isPresent()) {
            mrnCreationEntity = optional.get();
        } else {
            mrnCreationEntity = new MrnCreationEntity();
        }
        if(mrnCreationReponse!=null)
            mrnNo=mrnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN().get(0).getRemark().split(":")[1];
        else {
            mrnNo=mrnNumber;
            minNo=minNumber;}
        mrnCreationEntity.setMrnNumber(mrnNo);
        mrnCreationEntity.setMinNumer(minNo);
        mrnCreationEntity.setServiceCode(scServiceDetail.getUuid());
        mrnCreationEntity.setServiceId(scServiceDetail.getId());
        mrnCreationEntity.setType(type);
        mrnCreationEntity.setTaskName(taskName);
        mrnCreationEntity.setProcessInstanceId(processInstanceId);
        if(mrnCreationReponse!=null)
            mrnCreationEntity.setMrnStatus(mrnCreationReponse.getMTS4HANAOPTIMUSMTNResponse().getS4HANAOPTIMUSMTN().get(0).getStatus());
        for (CpeMaterialRequestDetails cpeMaterialRequestDetail : cpeMaterialRequestDetails) {
            cpeMaterialRequestDetail.setMrnNumber(mrnNo);
            cpeMaterialRequestDetail.setMinNumber(minNo);
            cpeMaterialRequestDetailsRepository.save(cpeMaterialRequestDetail);
        }
        return mrnCreationRepository.save(mrnCreationEntity);
    }

    @Transactional(readOnly = false)
    public ProCreation saveProCreation(ScServiceDetail scServiceDetail, String processInstanceId, String type,
                                       String poNumber, String prNumber, String poStatus, Timestamp poCreatedDate, String taskName,
                                       List<CpeMaterialRequestDetails> cpeMaterialRequestDetails, Integer componentId, Timestamp prcreatedDate,
                                       String prStatus) {

        Optional<ProCreation> optional = proCreationRepository.findByServiceIdAndType(scServiceDetail.getId(), type);


        ProCreation mrnCreationEntity = null;

        if (optional.isPresent()) {
            mrnCreationEntity = optional.get();
        } else {
            mrnCreationEntity = new ProCreation();
        }

        mrnCreationEntity.setServiceCode(scServiceDetail.getUuid());
        mrnCreationEntity.setServiceId(scServiceDetail.getId());
        mrnCreationEntity.setType(type);
        mrnCreationEntity.setProcessInstanceId(processInstanceId);
        mrnCreationEntity.setTaskName(taskName);
        mrnCreationEntity.setComponentId(componentId);
        if (prNumber != null) {
            mrnCreationEntity.setPrNumber(prNumber);
        }
        if (poStatus != null) {
            mrnCreationEntity.setPoStatus(poStatus);
        }
        if (poNumber != null) {
            mrnCreationEntity.setPoNumber(poNumber);
        }
        if (poCreatedDate != null) {
            mrnCreationEntity.setPoCreatedDate(poCreatedDate);
            mrnCreationEntity.setPoCompletedDate(poCreatedDate);
        }
        if (prcreatedDate != null) {
            mrnCreationEntity.setPrCreatedDate(prcreatedDate);
        }
        if(prStatus != null) {
            mrnCreationEntity.setPrStatus(prStatus);
        }

        ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
                .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                        scServiceDetail.getId(), "level5Wbs", "LM", "A");

        String level5Wbs = null;
        if (scComponentAttribute != null) {
            level5Wbs = scComponentAttribute.getAttributeValue();
        }


        for (CpeMaterialRequestDetails cpeMaterialRequestDetail : cpeMaterialRequestDetails) {
            if (poNumber != null) {
                cpeMaterialRequestDetail.setPoNumber(poNumber);
            }
            if(level5Wbs != null) {
                cpeMaterialRequestDetail.setToWbsNumber(level5Wbs);
            }
            if (prNumber != null) {
                cpeMaterialRequestDetail.setPrNumber(prNumber);
            }
            if (poStatus != null) {
                cpeMaterialRequestDetail.setPoStatus(poStatus);
            }
            if (poCreatedDate != null) {
                cpeMaterialRequestDetail.setPoCreatedDate(poCreatedDate);
                cpeMaterialRequestDetail.setPoCompletedDate(poCreatedDate);
            }
            if (prcreatedDate != null) {
                cpeMaterialRequestDetail.setPrCreatedDate(prcreatedDate);
            }
            if(prStatus != null) {
                cpeMaterialRequestDetail.setPrStatus(prStatus);
            }
            cpeMaterialRequestDetailsRepository.save(cpeMaterialRequestDetail);
        }

        return proCreationRepository.save(mrnCreationEntity);

    }



    /**
     * Process Auto po request for service ID
     *
     * @param serviceCode
     * @return
     * @throws TclCommonException
     * @author AnandhiV
     */
    public String processOffnetAutoPo(Integer scServiceDetailId, DelegateExecution execution) throws TclCommonException {
        LOGGER.info("Inside process auto po for service id {}", scServiceDetailId);
        AutoPoRequest autoPoRequest = new AutoPoRequest();
        AutoPoResponse autoPoResponse = null;
        String errorMessage = "";
        try {
            ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetailId).get();

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

                if (scServiceAttributes.containsKey("feasibility_response_id") && StringUtils.isNotBlank(scServiceAttributes.get("feasibility_response_id"))) {
                    Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
                    if (scServiceDetail.getScOrder().getOrderCategory() != null && !scServiceDetail.getScOrder().getOrderCategory().equals(CommonConstants.NEW)) {
						/*stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
								.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
										((scServiceDetail.getParentUuid() != null
												&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
												? scServiceDetail.getParentUuid()
												: scServiceDetail.getUuid()),
										"A_END_LM");*/
                        String tclServiceId = (scServiceDetail.getParentUuid() != null
                                && StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
                                ? scServiceDetail.getParentUuid()
                                : scServiceDetail.getUuid();
                        stg0SapPoDtlOptimus = getSapData(tclServiceId);
                        ParentPoBean parentPoData = getParentPoData(tclServiceId);
                        updateParentPoDataForTermination(scServiceDetail, stg0SapPoDtlOptimus, parentPoData);
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
                                                        .format(formatter.parse(
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
                        componentAndAttributeService.updateAttributes(scServiceDetailId, prMapper, AttributeConstants.COMPONENT_LM, "A");
                        LOGGER.info("PrMapper Data" + prMapper);
                        errorMessage = autoPoResponse.getPOResponse().getRemark();

                    } else if (execution != null) {
                        execution.setVariable("offnetPOCompleted", false);
                        errorMessage = autoPoResponse.getPOResponse().getRemark();
                    }


                    if (execution != null)
                        saveProCreation(scServiceDetail, execution.getProcessInstanceId(), "OFFNETLM",
                                autoPoResponse.getPOResponse().getPONumber(), null,
                                autoPoResponse.getPOResponse().getPOStatus(), new Timestamp(new Date().getTime()),
                                execution.getCurrentActivityId(), null, null, null,
                                null);

                } else if (execution != null) {
                    execution.setVariable("offnetPOCompleted", false);
                    errorMessage = "Feasibility Response ID is missing from L2O";
                    LOGGER.info("Error message for attribute missing :" + errorMessage);
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
                                       Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) throws TclCommonException {
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
                                      Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) throws TclCommonException {
        LOGGER.info("Inside contructAutoPoHeader");
        AutoPoHeader autoPoHeader = new AutoPoHeader();
        autoPoHeader.setBILLINDCTR(CommonConstants.EMPTY);
        autoPoHeader.setBSART("BS");
        autoPoHeader.setBUKRS("VSIN");
        autoPoHeader.setCHILDPO((stg0SapPoDtlOptimus != null && stg0SapPoDtlOptimus.getChildPoNumber() != null) ? StringUtils.trimToEmpty(stg0SapPoDtlOptimus.getChildPoNumber()) : CommonConstants.EMPTY);
        autoPoHeader.setEBELN(CommonConstants.EMPTY);
        autoPoHeader.setEKGRP(CommonConstants.EMPTY);
        autoPoHeader.setEKORG("VSNL");
        autoPoHeader.setZZSRVTYPE(orderSubCategoryMap.containsKey(
                scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory() : CommonConstants.NEW)
                ? (orderSubCategoryMap.get(scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory()
                : CommonConstants.NEW))
                : CommonConstants.EMPTY);

        String oldSiteAddress = null;
        if (StringUtils.isNotBlank(autoPoHeader.getZZSRVTYPE()) && autoPoHeader.getZZSRVTYPE().equalsIgnoreCase(CommonConstants.SHIFTING_LOCATION)) {
            if (Objects.nonNull(scServiceDetail.getServiceLinkId())) {
                ScServiceDetail scServiceDetailData = scServiceDetailRepository.findById(scServiceDetail.getServiceLinkId()).get();
                oldSiteAddress = scServiceDetailData.getSiteAddress();
                LOGGER.info("Old Site Address-" + oldSiteAddress);
            }
        }

        String headerText = "";
        if (StringUtils.isNotBlank(autoPoHeader.getZZSRVTYPE()) && autoPoHeader.getZZSRVTYPE().equalsIgnoreCase(CommonConstants.SHIFTING_LOCATION)) {
            headerText = "Old A-END : "
                    .concat(Objects.nonNull(oldSiteAddress) ? oldSiteAddress : CommonConstants.EMPTY)
                    .concat("\n New A-END : ")
                    .concat(Objects.nonNull(scOrder.getErfCustCustomerName()) ? scOrder.getErfCustCustomerName() + CommonConstants.SPACE : CommonConstants.EMPTY)
                    .concat((scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
                            : CommonConstants.EMPTY));
        } else
            headerText = "A-END : "
                    .concat(Objects.nonNull(scOrder.getErfCustCustomerName()) ? scOrder.getErfCustCustomerName() + CommonConstants.SPACE : CommonConstants.EMPTY)
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
                ? scServiceAttributes.get("provider_reference_number") : CommonConstants.EMPTY;
        autoPoHeader.setUNSEZ(providerReferenceNumber);
        autoPoHeader.setVERKF(CommonConstants.EMPTY);
        autoPoHeader
                .setWAERS(scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
                        : CommonConstants.EMPTY);
        autoPoHeader.setZBANDWIDTH("LOCAL LOOP");

        String feasibilityId = scServiceAttributes.containsKey("feasibility_response_id") ? scServiceAttributes.get("feasibility_response_id") : CommonConstants.EMPTY;
        if (StringUtils.isBlank(feasibilityId))
            feasibilityId = scServiceAttributes.containsKey("task_id") ? scServiceAttributes.get("task_id") : CommonConstants.EMPTY;
        autoPoHeader.setZSFDCID(feasibilityId);
        autoPoHeader.setZSFDCOPPID(scOrder.getUuid());
        autoPoHeader.setZSITEA(CommonConstants.EMPTY);
        autoPoHeader.setZSITEB(CommonConstants.EMPTY);
        autoPoHeader.setZSPEED(
                scServiceAttributes.containsKey("local_loop_bw") ? scServiceAttributes.get("local_loop_bw").concat("M")
                        : CommonConstants.EMPTY);
        autoPoHeader.setZTERM(CommonConstants.EMPTY);

        String mfContractTerm = StringUtils.trimToEmpty(scServiceAttributes.get("mf_contract_term"));
        String deal = mfContractTerm.replaceAll(" ", "").toLowerCase().replaceAll("months", "-Months");

        autoPoHeader.setZTYPEDEAL(deal);
        autoPoHeader
                .setZZCIRCUIT(scServiceDetail.getErfPrdCatalogProductName() != null
                        ? scServiceDetail.getErfPrdCatalogProductName().equals("IAS") ? "ILL"
                        : scServiceDetail.getErfPrdCatalogProductName()
                        : CommonConstants.EMPTY);
        autoPoHeader.setZZCOPFID(scOrder.getUuid());
        String operatorName = (scServiceAttributes.containsKey("closest_provider_bso_name") ? scServiceAttributes.get("closest_provider_bso_name")
                : (scServiceAttributes.containsKey("vendor_name")
                ? scServiceAttributes.get("vendor_name")
                : CommonConstants.EMPTY));

        autoPoHeader.setZZCPEOWNER(Objects.nonNull(operatorName) && (operatorName.equalsIgnoreCase("RADWIN WITH TTSL BH") ||
                operatorName.equalsIgnoreCase("RADWIN WITH TTML BH")) ?
                ("BH") : ("NOT REQUIRED"));

        autoPoHeader.setZZCUST(scOrder.getErfCustCustomerName());
        autoPoHeader.setZZFESREQID(CommonConstants.EMPTY);
        String interfaceName = (scComponentAttributes.containsKey("interface")
                ? (sapInterfaceMap.containsKey(scComponentAttributes.get("interface"))
                ? sapInterfaceMap.get(scComponentAttributes.get("interface"))
                : scComponentAttributes.get("interface"))
                : CommonConstants.EMPTY);
        autoPoHeader
                .setZZINTTYPE(interfaceName.equalsIgnoreCase("FE") ? "Fast Ethernet" : interfaceName);


        if (StringUtils.isNotBlank(scServiceDetail.getOrderSubCategory()) && !scServiceDetail.getOrderSubCategory().equalsIgnoreCase(CommonConstants.NEW))
            autoPoHeader.setZZOLDPONO((Objects.nonNull(scOrder.getOrderCategory()) && !scOrder.getOrderCategory().equalsIgnoreCase("ADD_SITE") && stg0SapPoDtlOptimus != null && stg0SapPoDtlOptimus.getPoNumber() != null) ? stg0SapPoDtlOptimus.getPoNumber() : CommonConstants.EMPTY);
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
        String lmType = scComponentAttributes.get("lmType");
        if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf") && !scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF")) {
            arcValue = scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                    ? scServiceAttributes.get("lm_arc_bw_prov_ofrf")
                    : null;
        } else if (scServiceAttributes.containsKey("lm_arc_bw_offwl") && !scServiceAttributes.get("lm_arc_bw_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
            arcValue = scServiceAttributes.containsKey("lm_arc_bw_offwl") ? scServiceAttributes.get("lm_arc_bw_offwl")
                    : null;
        }
        String mrcValue = CommonConstants.EMPTY;
        if (arcValue != null) {
            mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
        }
        autoPoHeader.setZZREVMRCVALUE(mrcValue);

        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf") && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF"))) {
            autoPoHeader.setZZREVNRCVALUE(scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                    ? scServiceAttributes.get("lm_nrc_bw_prov_ofrf")
                    : CommonConstants.EMPTY);
        } else if (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl") && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
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
                                          Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) throws TclCommonException {
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
        autoPoLineItems.setZVENREFNO(CommonConstants.EMPTY);
        autoPoLineItems.setZZCHARD(
                scServiceAttributes.containsKey("chargeable_distance") ? scServiceAttributes.get("chargeable_distance")
                        : "0");
        autoPoLineItems.setZZCOMMDT(CommonConstants.EMPTY);

        autoPoLineItems.setZZCRFSDT(scServiceDetail.getServiceCommissionedDate() != null
                ? formatter.format(Timestamp.valueOf(scServiceDetail.getServiceCommissionedDate().toString()))
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
                ? formatter.format(Timestamp.valueOf(commonFulfillmentUtils.getPreviousDate(scServiceDetail.getServiceCommissionedDate().toString())))
                : CommonConstants.EMPTY);
        autoPoLineItems.setZZTERMDT(scServiceDetail.getServiceTerminationDate() != null
                ? formatter.format(Timestamp.valueOf(scServiceDetail.getServiceTerminationDate().toString()))
                : CommonConstants.EMPTY);
        autoPoLineItems.setZZTERMTY(CommonConstants.EMPTY);
        autoPoLineItems.setZZTOEND(
                scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address")
                        : CommonConstants.EMPTY);
        Boolean isNrc = false;
        AutoPoLineItems autoPoLineItems2 = new AutoPoLineItems();
        AutoPoLineItems autoPoLineItems3 = new AutoPoLineItems();
        AutoPoLineItems autoPoLineItems4 = new AutoPoLineItems();
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {

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
                && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")) && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
            BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems3);
            constructAutoPoServiceItemsArcModem(autoPoLineItems3, scOrderAttributes, scComponentAttributes, scServiceDetail,
                    scOrder, scContractInfo, scServiceAttributes);
            auLineItems.add(autoPoLineItems3);
        }
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
            BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems4);
            constructAutoPoServiceItemsOtcModem(autoPoLineItems4, scOrderAttributes, scComponentAttributes, scServiceDetail,
                    scOrder, scContractInfo, scServiceAttributes);
            auLineItems.add(autoPoLineItems4);
        }

        autoPoMsgs.setLINEITEMS(auLineItems);
    }

    private void constructAutoPoServiceItemsArc(AutoPoLineItems autoPoLineItems, Map<String, String> scOrderAttributes,
                                                Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
                                                ScContractInfo scContractInfo, Map<String, String> scServiceAttributes) {
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
            String lmType = scComponentAttributes.get("lmType");
            if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf") && !scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF")) {
                arcValue = scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                        ? scServiceAttributes.get("lm_arc_bw_prov_ofrf")
                        : null;
            } else if (scServiceAttributes.containsKey("lm_arc_bw_offwl") && !scServiceAttributes.get("lm_arc_bw_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                arcValue = scServiceAttributes.containsKey("lm_arc_bw_offwl") ? scServiceAttributes.get("lm_arc_bw_offwl")
                        : null;
            }
            String mrcValue = CommonConstants.EMPTY;
            if (arcValue != null) {
                mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
            }

            if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf") && !scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF")) {
                autoPoServiceItems.setTBTWR(scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                        ? mrcValue
                        : CommonConstants.EMPTY);
            } else if (scServiceAttributes.containsKey("lm_arc_bw_offwl") && !scServiceAttributes.get("lm_arc_bw_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItems.setTBTWR(
                        scServiceAttributes.containsKey("lm_arc_bw_offwl") ? mrcValue
                                : CommonConstants.EMPTY);
            }

            autoPoServiceItems.setWAERS(
                    scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
                            : CommonConstants.EMPTY);
            try {
                autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
                                                ScContractInfo scContractInfo, Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructAutoPoServiceItems");
        List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
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
            if (scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf") && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF")) {
                autoPoServiceItems.setTBTWR(scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                        ? scServiceAttributes.get("lm_nrc_bw_prov_ofrf")
                        : CommonConstants.EMPTY);
            } else if (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl") && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItems.setTBTWR(
                        scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl") ? scServiceAttributes.get("lm_otc_nrc_installation_offwl")
                                : CommonConstants.EMPTY);
            }
            autoPoServiceItems.setWAERS(
                    scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
                            : CommonConstants.EMPTY);
            try {
                autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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

                String tclServiceId = (scServiceDetail.getParentUuid() != null
                        && StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
                        ? scServiceDetail.getParentUuid()
                        : scServiceDetail.getUuid();
                Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = getSapData(tclServiceId);
                ParentPoBean parentPoData = getParentPoData(tclServiceId);
                updateParentPoDataForTermination(scServiceDetail, stg0SapPoDtlOptimus, parentPoData);
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
                                             Map<String, String> sapBuyerMap) {
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
        String feasibilityId = scServiceAttributes.containsKey("feasibility_response_id") ? scServiceAttributes.get("feasibility_response_id") : CommonConstants.EMPTY;
        if (StringUtils.isBlank(feasibilityId))
            feasibilityId = scServiceAttributes.containsKey("task_id") ? scServiceAttributes.get("task_id") : CommonConstants.EMPTY;
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
        String operatorName = (scServiceAttributes.containsKey("closest_provider_bso_name") ? scServiceAttributes.get("closest_provider_bso_name")
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
                                                Map<String, String> orderSubCategoryMap, Map<String, String> sapBuyerMap) {
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
                    ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
                    ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {

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
                && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
            BeanUtils.copyProperties(autoPoLineItems, autoPoLineItems3);
            constructAutoPoServiceItemsArcModemUpdate(autoPoLineItems3, scOrderAttributes, scComponentAttributes, scServiceDetail,
                    scOrder, scContractInfo, scServiceAttributes);
            auLineItems.add(autoPoLineItems3);
        }
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
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
                                                      Map<String, String> scServiceAttributes) {
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
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
                                                      Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructAutoPoServiceItems");
        List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
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
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
                                                Map<String, String> orderSubCategoryMap, Map<String, String> terminateTypeMap, Map<String, String> sapInterfaceMap,
                                                Map<String, String> sapBuyerMap) {
        LOGGER.info("Inside contructAutoPoRequest for terminatePO");
        AutoPoMsgs autoPoMsgs = new AutoPoMsgs();
        contructAutoPoHeaderTerminate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
                scContractInfo, scServiceAttributes, orderSubCategoryMap, sapInterfaceMap, sapBuyerMap);
        constructAutoPoLineItemsTerminate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail,
                scOrder, scContractInfo, scServiceAttributes, orderSubCategoryMap, terminateTypeMap, sapBuyerMap);
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

            if (scServiceDetail.getOrderSubCategory().contains("Parallel"))
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
                                                   Map<String, String> orderSubCategoryMap, Map<String, String> terminateTypeMap, Map<String, String> sapBuyerMap) {
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
        autoPoLineItems.setZCOMMDT(CommonConstants.EMPTY);
        try {
            autoPoLineItems.setZTERMDT(scComponentAttributes.containsKey("terminationDate")
                    ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("terminationDate"), formatterCdate))
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
        try {
            autoPoLineItems.setZZTERMDT(scComponentAttributes.containsKey("terminationDate")
                    ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("terminationDate"), formatterCdate))
                    : CommonConstants.EMPTY);
            //Setting termination date for offnet PO
            if (scServiceDetail.getTerminationFlowTriggered() != null
                    && scServiceDetail.getTerminationFlowTriggered().equalsIgnoreCase(CommonConstants.YES)
                    && scServiceDetail.getTerminationEffectiveDate() != null) {
                LOGGER.info("Termination date for Offnet PO {} :", scServiceDetail.getTerminationEffectiveDate());
                String terminationDate = formatterCdate.format(scServiceDetail.getTerminationEffectiveDate());
                autoPoLineItems.setZZTERMDT(terminationDate != null
                        ? formatter.format(commonFulfillmentUtils.getDate(terminationDate, formatterCdate))
                        : CommonConstants.EMPTY);
                autoPoLineItems.setZTERMDT(terminationDate != null
                        ? formatter.format(commonFulfillmentUtils.getDate(terminationDate, formatterCdate))
                        : CommonConstants.EMPTY);
            }
        } catch (Exception e) {
            LOGGER.info("Error on parsing the date");
        }
        String terminationType = "";
        if ((Objects.nonNull(scOrder.getOrderCategory()) && (scOrder.getOrderCategory().equalsIgnoreCase("CHANGE_BANDWIDTH") || scOrder.getOrderCategory().equalsIgnoreCase("SHIFT_SITE")) && scServiceDetail.getOrderSubCategory().contains("Hot") && scServiceDetail.getOrderSubCategory().contains("BSO Change"))) {
            LOGGER.info("Entering Hot Upgrade-Bso Change case");
            terminationType = getChangeBWOrShiftSiteHotUpgradeTerminationType(scServiceDetail.getOrderSubCategory(), scOrder.getOrderCategory());
        } else if (scServiceDetail.getOrderSubCategory().contains("Parallel")) {
            LOGGER.info("Entering Parallel case");
            boolean bsoFlag = isSameBSOorDifferent(scComponentAttributes.get("vendorId"), scServiceAttributes.get("vendor_id"));
            terminationType = getTerminationTypeBasedOnBSOStatus(scServiceDetail.getOrderSubCategory(), bsoFlag);
        } else {
            LOGGER.info("Entering other cases");
            terminationType = terminateTypeMap.get(scServiceDetail.getOrderSubCategory());
        }
        LOGGER.info("Termination Type:" + terminationType);
        autoPoLineItems.setZZTERMTY(terminationType);
        autoPoLineItems.setZZTOEND(CommonConstants.EMPTY);
        constructAutoPoServiceItemsArcTerminate(autoPoLineItems, scOrderAttributes, scComponentAttributes, scServiceDetail,
                scOrder, scContractInfo, scServiceAttributes);
        auLineItems.add(autoPoLineItems);
        autoPoMsgs.setLINEITEMS(auLineItems);
    }

    private void constructAutoPoServiceItemsArcTerminate(AutoPoLineItems autoPoLineItems,
                                                         Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                         ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                         Map<String, String> scServiceAttributes) {
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
                    ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("terminationDate"), formatterCdate))
                    : CommonConstants.EMPTY); // Need to send the value if MACD
            if (scServiceDetail.getTerminationFlowTriggered() != null
                    && scServiceDetail.getTerminationFlowTriggered().equalsIgnoreCase(CommonConstants.YES)
                    && scServiceDetail.getTerminationEffectiveDate() != null) {
                String terminationDate = formatterCdate.format(scServiceDetail.getTerminationEffectiveDate());
                autoPoServiceItems.setZZDDATE(terminationDate != null
                        ? formatter.format(commonFulfillmentUtils.getDate(terminationDate, formatterCdate))
                        : CommonConstants.EMPTY);
            }

        } catch (Exception e) {
            LOGGER.info("Error on parsing the date");
        }
        autoPoServiceItemsList.add(autoPoServiceItems);
        autoPoLineItems.setSERVICEITEMS(autoPoServiceItemsList);
    }


    public String processOffnetAutoPoUpdate(Integer serviceId, DelegateExecution execution) {

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

                if (execution.getVariable("skipOffnet").equals(false)) {
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
                } else if (execution.getVariable("skipOffnet").equals(true)) {
                    Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
                    if (scServiceDetail.getScOrder().getOrderCategory() != null && !scServiceDetail.getScOrder().getOrderCategory().equals(CommonConstants.NEW)) {
							/*stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
									.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
											((scServiceDetail.getParentUuid() != null
													&& StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
													? scServiceDetail.getParentUuid()
													: scServiceDetail.getUuid()),
											"A_END_LM");*/
                        String tclServiceId = (scServiceDetail.getParentUuid() != null
                                && StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
                                ? scServiceDetail.getParentUuid()
                                : scServiceDetail.getUuid();
                        stg0SapPoDtlOptimus = getSapData(tclServiceId);
                    }

                    addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoNumber", stg0SapPoDtlOptimus.getPoNumber());
                    addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoDate", DateUtil.convertDateToString(new Date()));
                    addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoStatus", "Success");
                    Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
                            .getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
                    contructAutoPoRequestUpdatePrefeasible(autoPoRequest,
                            scServiceDetail, scComponentAttributesAMap, scServiceDetail.getScOrder());
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

            if (autoPoResponse != null && autoPoResponse.getPOResponse() != null && execution != null) {
                prMapper.put("offnetLocalPoUpdateDate", DateUtil.convertDateToString(new Date()));
                prMapper.put("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());
                execution.setVariable("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());

                if (!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
                    execution.setVariable("offnetPOUpdateCompleted", true);
                } else {
                    execution.setVariable("offnetPOUpdateCompleted", false);
                }
                errorMessage = "Offnet PO Number:" + autoPoResponse.getPOResponse().getPONumber();
                errorMessage += ", Offnet PO Update Status:" + autoPoResponse.getPOResponse().getPOStatus();

            } else if (execution != null) {
                execution.setVariable("offnetPOUpdateCompleted", false);
            }

            componentAndAttributeService.updateAttributes(serviceId, prMapper, AttributeConstants.COMPONENT_LM, "A");


        } catch (Exception e) {
            LOGGER.error("Error-creating-autopo-update-request {}", e.getMessage());
            LOGGER.error("Exception-creating-autopoupdate-request {}", e);
        }

        return errorMessage;
    }

    public String processAutoPoTerminate(Integer serviceId, DelegateExecution execution) {
        LOGGER.info("Inside process auto po terminate for service id {}", serviceId);
        AutoPoRequest autoPoRequest = new AutoPoRequest();
        AutoPoResponse autoPoResponse = null;
        String errorMessage = "";

        try {
            ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();

            Map<String, String> prMapper = new HashMap<>();
            if (scServiceDetail != null) {
                String serviceCode = scServiceDetail.getUuid();
                LOGGER.info("Got processOffnetAutoPoTerminate serviceCode {}", serviceCode);
                if (execution.getVariable("skipOffnet").equals(false)) {

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
                            orderSubCategoryMap, terminationTypeMap, sapInterfaceMap, sapBuyerMap);
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

            if (autoPoResponse != null && autoPoResponse.getPOResponse() != null && execution != null) {
                prMapper.put("offnetLocalPoTerminateDate", DateUtil.convertDateToString(new Date()));
                prMapper.put("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());
                execution.setVariable("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());

                if (!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
                    execution.setVariable("offnetPOTerminateCompleted", true);
                } else {
                    execution.setVariable("offnetPOTerminateCompleted", false);
                    errorMessage = "Offnet Terminate PO Failure Reason :" + autoPoResponse.getPOResponse().getRemark();
                }

            } else if (execution != null) {
                execution.setVariable("offnetPOTerminateCompleted", false);
                errorMessage = "Offnet Terminate PO Failure Reason :" + autoPoResponse.getPOResponse().getRemark();
            }
            errorMessage += ", Offnet Terminate PO Number:" + autoPoResponse.getPOResponse().getPONumber();
            errorMessage += ", Offnet PO Terminate Status:" + autoPoResponse.getPOResponse().getPOStatus();

            componentAndAttributeService.updateAttributes(serviceId, prMapper, AttributeConstants.COMPONENT_LM, "A");


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

    @Transactional(readOnly = false)
    public void processGrnStatusResponse(GrnS4HanaResponse grnResponses) throws TclCommonException {
        LOGGER.info("Entering processGRNStatusResponse...");


        if (Objects.nonNull(grnResponses.getGoodsRecipts()) && !grnResponses.getGoodsRecipts().isEmpty()) {
            String cpeSerialNumber = StringUtils.EMPTY;
            Map<String, List<GoodsRecipt>> grnResponseByPoNumber = grnResponses.getGoodsRecipts().stream()
                    .collect(Collectors.groupingBy(grnResponse -> grnResponse.getPoNumber()));

            for (Map.Entry<String, List<GoodsRecipt>> grnResponseMap : grnResponseByPoNumber.entrySet()) {
                LOGGER.info("processGRNStatusResponse PO Number={}", grnResponseMap.getKey());

                Optional<ProCreation> proCreationOp = proCreationRepository.findByPoNumber(grnResponseMap.getKey());
                if (proCreationOp.isPresent() && Objects.nonNull(grnResponseMap.getValue()) && !grnResponseMap.getValue().isEmpty()) {
                    GoodsRecipt grnResponse = grnResponseMap.getValue().stream().findFirst().get();
                    if(grnResponse.getPoNumber() != null) {
                        List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                                .findByPoNumber(grnResponse.getPoNumber().toString());
                        if(cpeMaterialRequestDetails != null && !cpeMaterialRequestDetails.isEmpty()) {
                            Map<String, CpeMaterialRequestDetails> materialCodeReqMap = cpeMaterialRequestDetails.stream()
                                    .collect(Collectors.toMap(CpeMaterialRequestDetails::getMaterialCode, cpeReq -> cpeReq));
                            for (GrnMaterialDetail materialDetail : grnResponse.getMaterialDetails()) {
                                CpeMaterialRequestDetails cpeMaterialRequestDetail = materialCodeReqMap.get(materialDetail.getMaterialCode().trim());
                                if(cpeMaterialRequestDetail != null) {
                                    LOGGER.info("Updated Oem Serail number {} for service code {}",
                                            materialDetail.getOemSerailNumber(),
                                            cpeMaterialRequestDetail.getServiceCode());
                                    if (materialDetail.getOemSerailNumber() != null
                                            && !materialDetail.getOemSerailNumber().isEmpty()
                                            && !materialDetail.getOemSerailNumber().equalsIgnoreCase("NULL")) {
                                        cpeMaterialRequestDetail.setOemSerialNumber(materialDetail.getOemSerailNumber());
                                        if(cpeSerialNumber.isEmpty()) {
                                            cpeSerialNumber = materialDetail.getOemSerailNumber();
                                        }
                                    } else {
                                        cpeMaterialRequestDetail.setOemSerialNumber("-");
                                        if(cpeSerialNumber.isEmpty()) {
                                            cpeSerialNumber = "-";
                                        }
                                    }
                                    cpeMaterialRequestDetail.setGrnNumber(grnResponse.getGrNumber());
                                    cpeMaterialRequestDetail.setGrnDate(DateUtil.convertStringToTimeStampDDMMYYYWithHypen(grnResponse.getGrDate()));
                                    cpeMaterialRequestDetailsRepository.save(cpeMaterialRequestDetail);
                                }
                            }
                        }
                    }
                    Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(proCreationOp.get().getServiceId(), "grn-received-async");


                    Map<String, String> atMap = new HashMap<>();
                    if(!cpeSerialNumber.isEmpty())
                        atMap.put("cpeSerialNumber", cpeSerialNumber);
                    atMap.put("grnNumber", String.valueOf(grnResponse.getGrNumber()));
                    //atMap.put("grnCreationDate",  String.valueOf(grnResponse.getGRNDate()));
                    atMap.put("materialReceived", "Yes");
                    atMap.put("materialReceivedDate", String.valueOf(grnResponse.getGrDate()));

                    runtimeService.trigger(task.getWfTaskId());

                    componentAndAttributeService.updateAttributes(proCreationOp.get().getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

                    //auto close confirm material availability
                    ConfirmMaterialAvailabilityBean confirmMaterialAvailabilityBean = new ConfirmMaterialAvailabilityBean();
                    confirmMaterialAvailabilityBean.setGrnNumber(String.valueOf(grnResponse.getGrNumber()));
                    confirmMaterialAvailabilityBean.setMaterialReceivedDate(String.valueOf(grnResponse.getGrDate()));
                    confirmMaterialAvailabilityBean.setMaterialReceived("Yes");
                    Task confirmCpeTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(proCreationOp.get().getServiceId(), "confirm-material-availability");
                    if (confirmCpeTask != null && !confirmCpeTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
                        confirmMaterialAvailabilityBean.setWfTaskId(confirmCpeTask.getWfTaskId());
                        confirmMaterialAvailabilityBean.setTaskId(confirmCpeTask.getId());
                        processTaskLogDetails(confirmCpeTask, "CLOSED", null, "Grn response received from sap", null);
                        flowableBaseService.taskDataEntry(confirmCpeTask, confirmMaterialAvailabilityBean);
                    }
                }
            }
        }
    }

    private void constructAutoPoServiceItemsOtcModem(AutoPoLineItems autoPoLineItems, Map<String, String> scOrderAttributes,
                                                     Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
                                                     ScContractInfo scContractInfo, Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructAutoPoServiceItems");
        List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
        //need to find attribute for wireless case
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
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
            if (scServiceAttributes.containsKey("lm_otc_modem_charges_offwl") && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItems.setTBTWR(
                        scServiceAttributes.containsKey("lm_otc_modem_charges_offwl") ? scServiceAttributes.get("lm_otc_modem_charges_offwl")
                                : CommonConstants.EMPTY);
            }
            autoPoServiceItems.setWAERS(
                    scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
                            : CommonConstants.EMPTY);
            try {
                autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
                                                     ScContractInfo scContractInfo, Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructAutoPoServiceItems");
        List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
        //need to find attribute for wireless case
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
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
            if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl") && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                arcValue = scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                        ? scServiceAttributes.get("lm_arc_modem_charges_offwl")
                        : null;
            }
            String mrcValue = CommonConstants.EMPTY;
            if (arcValue != null) {
                mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
            }

            if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl") && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItems.setTBTWR(
                        scServiceAttributes.containsKey("lm_arc_modem_charges_offwl") ? mrcValue
                                : CommonConstants.EMPTY);
            }


            autoPoServiceItems.setWAERS(
                    scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
                            : CommonConstants.EMPTY);
            try {
                autoPoServiceItems.setZZCDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
                                                           Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructAutoPoServiceItems");
        List<AutoPoServiceItems> autoPoServiceItemsList = new ArrayList<>();
        String lmType = scComponentAttributes.get("lmType");
        if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl") && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
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
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
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
                        ? formatter.format(commonFulfillmentUtils.getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
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


    private void contructAutoPoRequestUpdatePrefeasible(AutoPoRequest autoPoRequest, ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributes, ScOrder scOrder) {
        LOGGER.info("Inside contructAutoPoRequest");
        AutoPoMsgs autoPoMsgs = new AutoPoMsgs();
        contructAutoPoHeaderUpdatePrefeasible(autoPoMsgs, scServiceDetail, scComponentAttributes, scOrder);
        constructAutoPoLineItemsUpdatePrefeasible(autoPoMsgs);
        autoPoRequest.setPOMSGS(autoPoMsgs);
    }

    private void contructAutoPoHeaderUpdatePrefeasible(AutoPoMsgs autoPoMsgs, ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributes, ScOrder scOrder) {
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

    private void constructAutoPoLineItemsUpdatePrefeasible(AutoPoMsgs autoPoMsgs) {
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

    private void constructAutoPoServiceItemsUpdatePrefeasible(AutoPoLineItems autoPoLineItems) {
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
     *
     * @param oldVendorId
     * @param newVendorId
     * @return
     */
    private boolean isSameBSOorDifferent(String oldVendorId, String newVendorId) {
        LOGGER.info("Old Vendor Id:" + oldVendorId);
        LOGGER.info("New Vendor Id:" + newVendorId);
        boolean flag = false;
        if (Objects.nonNull(oldVendorId) && Objects.nonNull(newVendorId) && oldVendorId.equalsIgnoreCase(newVendorId))
            flag = true;
        return flag;
    }

    /**
     * @param orderCategory
     * @param category
     * @return
     */
    private String getChangeBWOrShiftSiteHotUpgradeTerminationType(String orderSubCategory, String category) {
        String terminationType = "";
        if (orderSubCategory.equalsIgnoreCase("Hot Upgrade-BSO Change") || orderSubCategory.equalsIgnoreCase("Hot Downgrade-BSO Change")) {
            if (category.equalsIgnoreCase("CHANGE_BANDWIDTH"))
                terminationType = "CHANGE BSO";
            else if (category.equalsIgnoreCase("SHIFT_SITE"))
                terminationType = "SHIFTING";
        }
        return terminationType;
    }

    /**
     * Method to get terminatio type based on bso status
     *
     * @param orderCategory
     * @param bsoStatus
     * @return
     */
    private String getTerminationTypeBasedOnBSOStatus(String orderSubCategory, boolean bsoStatus) {
        String terminationType = "";
        if (orderSubCategory.equalsIgnoreCase("Parallel Upgrade")) {
            if (bsoStatus == true)
                terminationType = "UPGRADE";
            else
                terminationType = "CHANGE BSO";
        } else if (orderSubCategory.equalsIgnoreCase("Parallel Downgrade")) {
            if (bsoStatus == true)
                terminationType = "DOWNGRADE";
            else
                terminationType = "CHANGE BSO";
        } else if (orderSubCategory.equalsIgnoreCase("Parallel Shifting")) {
            if (bsoStatus == true)
                terminationType = "SHIFTING";
            else
                terminationType = "CHANGE BSO";
        }
        return terminationType;
    }


    public String processOffnetAutoPoUpdateDummy(Integer serviceId, DelegateExecution execution) {

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

            if (autoPoResponse != null && autoPoResponse.getPOResponse() != null && execution != null) {
                prMapper.put("offnetLocalPoUpdateDate", DateUtil.convertDateToString(new Date()));
                prMapper.put("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());
                execution.setVariable("offnetLocalPoUpdateStatus", autoPoResponse.getPOResponse().getPOStatus());

                if (!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
                    execution.setVariable("offnetPOUpdateCompleted", true);
                } else {
                    execution.setVariable("offnetPOUpdateCompleted", false);
                }
                errorMessage = "Offnet PO Number:" + autoPoResponse.getPOResponse().getPONumber();
                errorMessage += ", Offnet PO Update Status:" + autoPoResponse.getPOResponse().getPOStatus();

            } else if (execution != null) {
                execution.setVariable("offnetPOUpdateCompleted", false);
            }

            componentAndAttributeService.updateAttributes(serviceId, prMapper, AttributeConstants.COMPONENT_LM, "A");


        } catch (Exception e) {
            LOGGER.error("Error-creating-autopo-update-request {}", e.getMessage());
            LOGGER.error("Exception-creating-autopoupdate-request {}", e);
        }

        return errorMessage;
    }


    public String processAutoPoTerminateDummy(Integer serviceId, DelegateExecution execution) {
        LOGGER.info("Inside process auto po terminate for service id {}", serviceId);
        AutoPoRequest autoPoRequest = new AutoPoRequest();
        AutoPoResponse autoPoResponse = null;
        String errorMessage = "";

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
                        orderSubCategoryMap, terminationTypeMap, sapInterfaceMap, sapBuyerMap);

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

            if (autoPoResponse != null && autoPoResponse.getPOResponse() != null && execution != null) {
                prMapper.put("offnetLocalPoTerminateDate", DateUtil.convertDateToString(new Date()));
                prMapper.put("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());
                execution.setVariable("offnetLocalPoTerminateStatus", autoPoResponse.getPOResponse().getPOStatus());

                if (!autoPoResponse.getPOResponse().getPOStatus().equalsIgnoreCase("Failure")) {
                    execution.setVariable("offnetPOTerminateCompleted", true);
                } else {
                    execution.setVariable("offnetPOTerminateCompleted", false);
                    errorMessage = "Offnet Terminate PO Failure Reason :" + autoPoResponse.getPOResponse().getRemark();
                }

            } else if (execution != null) {
                execution.setVariable("offnetPOTerminateCompleted", false);
                errorMessage = "Offnet Terminate PO Failure Reason :" + autoPoResponse.getPOResponse().getRemark();
            }
            errorMessage += ", Offnet Terminate PO Number:" + autoPoResponse.getPOResponse().getPONumber();
            errorMessage += ", Offnet PO Terminate Status:" + autoPoResponse.getPOResponse().getPOStatus();

            componentAndAttributeService.updateAttributes(serviceId, prMapper, AttributeConstants.COMPONENT_LM, "A");


        } catch (Exception e) {
            LOGGER.error("Error-creating-autopo-terminate-request {}", e.getMessage());
            LOGGER.error("Exception-creating-autopoterminate-request {}", e);
        }

        return errorMessage;
    }

    /**
     * Method to get parentPo Data
     *
     * @param tclServiceId
     * @return
     */
    public ParentPoBean getParentPoData(String tclServiceId) {
        ParentPoBean parentPoBean = null;
        if (StringUtils.isNotBlank(tclServiceId)) {
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
     *
     * @param tclServiceId
     * @return
     */
    public Stg0SapPoDtlOptimus getSapData(String tclServiceId) {
        Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
        if (StringUtils.isNotBlank(tclServiceId)) {
            ParentPoBean parentPoBean = getParentPoData(tclServiceId);
            if (Objects.nonNull(parentPoBean)) {
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
     *
     * @param scServiceDetail
     * @param stg0SapPoDtlOptimus
     * @param parentPoData
     */
    public void updateParentPoDataForTermination(ScServiceDetail scServiceDetail, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus, ParentPoBean parentPoData) {
        if (Objects.nonNull(parentPoData)) {
            addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "vendorId", stg0SapPoDtlOptimus.getVendorNo());
            addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "vendorName", parentPoData.getVendorName());
            if (Objects.nonNull(parentPoData.getSfdcProviderName()))
                addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sfdcProviderName", parentPoData.getSfdcProviderName());
            else
                addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sfdcProviderName", parentPoData.getVendorName());
            addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "oldOffnetPoNumber", stg0SapPoDtlOptimus.getPoNumber());
            addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "oldBsoCircuitId", stg0SapPoDtlOptimus.getVendorRefIdOrderId());
        }

    }

    public VmIStockResponse checkVmiStockInventory (String serviceCode, String dealType, String processInstanceId,String materialCode)
            throws TclCommonException {
        VmIStockResponse inventoryCheckResponse = null;
        ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "INPROGRESS");

        /*Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributes(
                scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");*/
        if (scServiceDetail != null) {
            List<String> materialCodes=new ArrayList<>();
            //Add the value to test ,need to remove after test
            materialCodes.add(materialCode);
            VmiStock inventoryCheckRequest = constructVmiStockRequest(materialCodes);
            String vmiStockRequest=Utils.convertObjectToJson(inventoryCheckRequest);
            AuditLog auditLogInventory = saveDataIntoNetworkInventory(vmiStockRequest,
                    null, serviceCode, dealType + "PRPO", processInstanceId);
            //Rest call for VMI Stock
            LOGGER.info("Request to get VMI stock URL {} and request {} ",sapHanaVmiStockUrl,vmiStockRequest);
            RestResponse vmiStockResponse = restClientService.postWithBasicAuthentication(sapHanaVmiStockUrl,
                    vmiStockRequest, createCommonHeader(), userName, password);
            LOGGER.info("Response from VMI stock {} :",vmiStockResponse);
            if (vmiStockResponse.getStatus() == Status.SUCCESS) {
                LOGGER.info("Response data from VMI stock {} :",vmiStockResponse.getData());
                auditLogInventory.setResponse(vmiStockResponse.getData());
                auditLogRepositoryRepository.save(auditLogInventory);
                inventoryCheckResponse = Utils.convertJsonToObject(vmiStockResponse.getData(), VmIStockResponse.class);
            }
        }
        return inventoryCheckResponse;
    }
    private VmiStock constructVmiStockRequest(List<String> materialCodes){
        LOGGER.info("Inside constructVmiStockRequest with material code {} :",materialCodes);
        VmiStock inventoryCheckRequest= new VmiStock();
        materialCodes.stream().forEach(materialCode->{
            VmiStockMaterial vmiStock=new VmiStockMaterial();
            vmiStock.setMATERIALCODE(materialCode);
            inventoryCheckRequest.getVMISTOCK().add(vmiStock);
        });
        return inventoryCheckRequest;
    }


    public boolean trackCPE(Integer serviceId, Integer componentId ) throws TclCommonException {
        LOGGER.info("Inside Track CPE interface for serviceId {} and componentId {}", serviceId, componentId);
        List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository.findByScServiceDetailIdAndComponentId(serviceId, componentId);
        if (cpeMaterialRequestDetails != null && !cpeMaterialRequestDetails.isEmpty()) {
            Optional<CpeMaterialRequestDetails> optCpeMaterialRequestDetail = cpeMaterialRequestDetails
                    .stream().filter(cpe -> cpe.getCatagory() != null
                            && !cpe.getCatagory().equals(MST_CATALOG_LICENSE_CATAGORY) && cpe.getMrnNumber() != null)
                    .findFirst();
            if(optCpeMaterialRequestDetail.isPresent()) {
                CpeMaterialRequestDetails cpeMaterialRequestDetail = optCpeMaterialRequestDetail.get();

                LOGGER.info(" Track CPE ==> MRN number {} for serviceId {} and componentId {}", cpeMaterialRequestDetail.getMrnNumber(), serviceId, componentId);

                TrackCpeRequest trackCBERequest = new TrackCpeRequest();

                List<TrackCpeGRRequest> goodsreceipts = new ArrayList<>();

                LOGGER.info("Request to Track CBE URL {} and request {} ", sapHanaTrackCbeUrl, trackCBERequest);
                ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
                AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(trackCBERequest),
                        null, scServiceDetail.getUuid(), "TRACK-CPE", null);

                TrackCpeGRRequest goodsreceipt = new TrackCpeGRRequest();
                ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
                        .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                                serviceId, "cpeInvoiceNumber", "LM", "A");
                if(scComponentAttribute != null) {
                    goodsreceipt.setInvoiceNo(scComponentAttribute.getAttributeValue());
                }
                goodsreceipt.setMTNNO(cpeMaterialRequestDetail.getMrnNumber());
                goodsreceipt.setOBD(cpeMaterialRequestDetail.getMinNumber());
                goodsreceipt.setReceivedDate(DateUtil.convertDateToDDMMYYYWithHypenString(new Date()));
                goodsreceipt.setTrackingNumber(cpeMaterialRequestDetail.getVehicleDockertNumber());

                goodsreceipts.add(goodsreceipt);

                trackCBERequest.setGoodsReceipt(goodsreceipts);

                LOGGER.info("Request to Track CBE URL {} and request {} ", sapHanaTrackCbeUrl, trackCBERequest);
                RestResponse restResponse = restClientService.postWithBasicAuthentication(sapHanaTrackCbeUrl,
                        Utils.convertObjectToJson(trackCBERequest), createCommonHeader(), userName, password);

                LOGGER.info("Track cpe response:{}",Utils.convertObjectToJson(restResponse));
                if (restResponse != null) {
                    inventory.setResponse(restResponse.getData());
                    auditLogRepositoryRepository.save(inventory);
                }

                if (restResponse.getStatus() == Status.SUCCESS) {

                    LOGGER.info("Response data from installCpeRequest {} :", restResponse.getData());

                    return true;

                }else {
                    LOGGER.error("Track CPE ==> Exception while calling API for serviceId {} and error {} ", serviceId, restResponse);
                }
            }else {
                LOGGER.info(" Track CPE ==> MRN number not exists for serviceId {} and componentId {}", serviceId, componentId);
            }
        }else {
            LOGGER.info(" Track CPE ==> CPE material request details not found  for serviceId {} and componentId {}", serviceId, componentId);
        }

        return true;
    }

    @Transactional(readOnly = false)
    public boolean installCpeRequest (String mrnNumber,String Obd,String trackingNumber,String receivedDate,String invoiceNo)
            throws TclCommonException {




        GOODSRECEIPT goodsReceipt=new GOODSRECEIPT();
        /*ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "INPROGRESS");

        if (scServiceDetail != null) {*/

        InstallCpeRequest installCpeRequest = constructInstallCpeRequest(goodsReceipt);
        String installCpePayload=Utils.convertObjectToJson(installCpeRequest);
           /* AuditLog auditLogInventory = saveDataIntoNetworkInventory(installCpePayload,
                    null, serviceCode, dealType + "PRPO", processInstanceId);*/

        LOGGER.info("Request to get installCpeRequest URL {} and request {} ",sapHanaInstallCpeUrl,installCpePayload);
        RestResponse installCpeResponse = restClientService.postWithBasicAuthentication(sapHanaInstallCpeUrl,
                Utils.convertObjectToJson(installCpeRequest), createCommonHeader(), userName, password);
        LOGGER.info("Response from installCpeRequest {} :",installCpeResponse);
        if (installCpeResponse.getStatus() == Status.SUCCESS) {

            LOGGER.info("Response data from installCpeRequest {} :",installCpeResponse.getData());
               /* auditLogInventory.setResponse(installCpeResponse.getData());
                auditLogRepositoryRepository.save(auditLogInventory);
                inventoryCheckResponse = Utils.convertJsonToObject(installCpeResponse.getData(), InventoryCheckResponse.class);*/

            return true;

        }
        // }

        return true;

    }
    private InstallCpeRequest constructInstallCpeRequest(GOODSRECEIPT goodsReceipt){
        LOGGER.info("Inside constructInstallCpeRequest:");
        InstallCpeRequest inventoryCheckRequest = new InstallCpeRequest();
        /*GoodsReceipt goodsReceipt=new GoodsReceipt();
        goodsReceipt.setMtnNo("");
        goodsReceipt.setObd("");
        goodsReceipt.setReceivedDate("");
        goodsReceipt.setTrackingNumber("");
        goodsReceipt.setInvoiceNo("");*/
        inventoryCheckRequest.getGoodsReceipt().add(goodsReceipt);
        return inventoryCheckRequest;
    }


    @Transactional
    public void processWbsResponse(WbsResponseBean wbsResponseBean) throws TclCommonException{
        LOGGER.info("Entering processWbsResponse with response:{}", Utils.convertObjectToJson(wbsResponseBean));
        boolean supportPrNeeded=false;
        boolean installationPrNeeded = false;

        if (Objects.nonNull(wbsResponseBean.getWbsResponse())) {
            WbsElement wbsElement = wbsResponseBean.getWbsResponse();
            if (Objects.nonNull(wbsElement)) {
                if (wbsElement.getServiceId() != null && !wbsElement.getServiceId().isEmpty()) {
                    LOGGER.info("serviceId from processWbsResponse:{}", wbsElement.getServiceId());
                    ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(wbsElement.getServiceId(), "INPROGRESS");
                    if(scServiceDetail!=null) {
                        //fetch cpe scope
                        ScComponentAttribute cpeScope = scComponentAttributesRepository
                                .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                                        scServiceDetail.getId(), "cpeSiScope", "LM", "A");
                        if(cpeScope!=null) {
                            if(cpeScope.getAttributeValue().toLowerCase().contains("installation")) {
                                installationPrNeeded=true;
                            }
                            if(cpeScope.getAttributeValue().toLowerCase().contains("support")) {
                                supportPrNeeded=true;
                            }
                        }

                        ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
                                .findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "CPE");

                        String cpeType = null;

                        if(scServiceAttribute != null) {
                            cpeType = scServiceAttribute.getAttributeValue();
                        }

                        LOGGER.info("Triggering task {}", "wbs-sap-wait-async");
                        Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
                                scServiceDetail.getId(), "wbs-sap-wait-async");
                        if (task != null) {
                            Execution execution = runtimeService.createExecutionQuery()
                                    .processInstanceId(task.getWfProcessInstId()).activityId("wbs-sap-wait-async")
                                    .singleResult();
                            LOGGER.info("Entering execution... for task:{} and ProcessInstanceId:{}", "wbs-sap-wait-async",
                                    task.getWfProcessInstId());
                            if (Objects.nonNull(execution)) {
                                runtimeService.setVariable(execution.getId(), "wbsRequestNeeded", false);
                                if ((wbsElement.getWbsElementCapex() != null && !wbsElement.getWbsElementCapex().isEmpty())
                                        || (cpeType != null && cpeType.contains("Outright")
                                        && wbsElement.getWbsElementOpex() != null
                                        && !wbsElement.getWbsElementOpex().isEmpty())) {
                                    LOGGER.info("level5Wbs:{}", wbsElement.getWbsElementCapex());
                                    runtimeService.setVariable(execution.getId(), "wbsStatus", true);
                                    runtimeService.setVariable(execution.getId(), "supportPrNeeded", supportPrNeeded);
                                    runtimeService.setVariable(execution.getId(), "installationPrNeeded", installationPrNeeded);
                                    runtimeService.trigger(execution.getId());
                                } else {
                                    LOGGER.info("level5Wbs is empty");
                                    runtimeService.setVariable(execution.getId(), "wbsStatus", false);
                                    runtimeService.setVariable(execution.getId(), "supportPrNeeded", supportPrNeeded);
                                    runtimeService.setVariable(execution.getId(), "installationPrNeeded", installationPrNeeded);
                                    runtimeService.trigger(execution.getId());
                                }
                            }
                        }
                        Map<String, String> mapper = new HashMap<>();

                        if(cpeType != null && cpeType.toLowerCase().contains("outright")) {
                            mapper.put("level5Wbs", wbsElement.getWbsElementOpex());
                            mapper.put("wbsElementOpex", wbsElement.getWbsElementOpex());
                        }else {
                            mapper.put("level5Wbs", wbsElement.getWbsElementCapex());
                            mapper.put("wbsElementOpex", wbsElement.getWbsElementOpex());
                        }

                        componentAndAttributeService.updateAttributes(scServiceDetail.getId(), mapper,
                                AttributeConstants.COMPONENT_LM, "A");

                    }
                }
            }
        }
    }

    /**
     *
     * @param serviceCode
     * @param cpeType
     * @param isMaterial
     * @param processInstanceId
     * @param cpeMaterialRequestDetails
     * @param isSupport
     * @return
     * @throws TclCommonException
     */
    public AutoPrPoRespones processPrSupportOrInstall(String serviceCode, String cpeType,
                                                      String processInstanceId, List<CpeMaterialRequestDetails> cpeMaterialRequestDetails,
                                                      boolean isSupport) throws TclCommonException {

        LOGGER.info("Inside processPrSupportOrLicense for serviceCode: {} and isSupport: {}", serviceCode, isSupport);

        AutoPrPoRespones  prSupportOrIntsallResponse = null;
        ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,
                "INPROGRESS");

        Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
                .getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

        if (scServiceDetail != null) {
            String type="";
            PR2POInstallSupportRequest prRequest = constructPrSupportOrInstallRequest(scServiceDetail, cpeType,
                    processInstanceId, scComponentAttributesAMap, cpeMaterialRequestDetails, isSupport);
            if(isSupport) {
                type="SUPPORT";
            }
            else{
                type="INSTALLATION";
            }
            AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(prRequest), null, serviceCode,
                    type, processInstanceId);
            RestResponse autoPrResponse = restClientService.postWithBasicAuthentication(sapAutoPrUrl,
                    Utils.convertObjectToJson(prRequest), createCommonHeader(), userName, password);
            if (autoPrResponse.getStatus() == Status.SUCCESS) {
                inventory.setResponse(autoPrResponse.getData());
                auditLogRepositoryRepository.save(inventory);
                prSupportOrIntsallResponse = Utils.convertJsonToObject(autoPrResponse.getData(),
                        AutoPrPoRespones.class);
            }
        }
        return prSupportOrIntsallResponse;
    }

    /**
     *
     * @param scServiceDetail
     * @param invType
     * @param isMaterial
     * @param processInstanceId
     * @param scComponentAttributesAMap
     * @param cpeMaterialRequestDetails
     * @param isSupport
     * @return
     * @throws TclCommonException
     */
    private PR2POInstallSupportRequest constructPrSupportOrInstallRequest(ScServiceDetail scServiceDetail,
                                                                          String invType, String processInstanceId, Map<String, String> scComponentAttributesAMap,
                                                                          List<CpeMaterialRequestDetails> cpeMaterialRequestDetails, boolean isSupport) throws TclCommonException {
        LOGGER.info("Entering the constructAutoPrRequest with serviceId {}", scServiceDetail.getUuid());

        PR2POInstallSupportRequest prRequest = new PR2POInstallSupportRequest();
        List<PR2POInstallSupport> prLines = new ArrayList<>();
        List<String> invalidMaterialCodes = new ArrayList<>();
        prRequest.setPR2PO(prLines);


        Map<String, CpeMaterialRequestDetails> materialCodeAndCpeMaterialRequestDetailsMap = new HashMap<>();

        Double cost = null;
        //PR/PO should occur only once for each material
        for (CpeMaterialRequestDetails cpeMaterialRequestDetail : cpeMaterialRequestDetails) {
            if (materialCodeAndCpeMaterialRequestDetailsMap.get(cpeMaterialRequestDetail.getMaterialCode()) == null) {
                materialCodeAndCpeMaterialRequestDetailsMap.put(cpeMaterialRequestDetail.getMaterialCode(),
                        cpeMaterialRequestDetail);
                if(isSupport) {
                    cost = cpeMaterialRequestDetail.getSupportCost();
                }else {
                    cost = cpeMaterialRequestDetail.getInstallationCost();
                }
            }
        }
        for (Map.Entry<String, CpeMaterialRequestDetails> entry : materialCodeAndCpeMaterialRequestDetailsMap
                .entrySet()) {
            constructPrLineSupportOrInstall(scServiceDetail, invType, processInstanceId, prLines, entry.getValue(),
                    cost, scComponentAttributesAMap, prRequest, invalidMaterialCodes, isSupport);

        }
        LOGGER.info("Material Codes not available in VMI :", invalidMaterialCodes);
        return prRequest;
    }


    /**
     *
     * @param scServiceDetail
     * @param invType
     * @param processInstanceId
     * @param prLines
     * @param cpeMaterialRequestDetail
     * @param exchangeValue
     * @param scComponentAttributesAMap
     * @param prRequest
     * @param invalidMaterialCodes
     * @param isSupport
     * @param isMaterial
     * @throws TclCommonException
     */
    private void constructPrLineSupportOrInstall(ScServiceDetail scServiceDetail, String invType,
                                                 String processInstanceId, List<PR2POInstallSupport> prLines,
                                                 CpeMaterialRequestDetails cpeMaterialRequestDetail, Double cost,
                                                 Map<String, String> scComponentAttributesAMap, PR2POInstallSupportRequest prRequest,
                                                 List<String> invalidMaterialCodes, boolean isSupport) throws TclCommonException {
        LOGGER.info("Inside constructPrSupportOrInstallLine for service code: {}, isSupport:{}", scServiceDetail.getUuid(),isSupport);
        PR2POInstallSupport prLine = new PR2POInstallSupport();
        prLine.setMaterialCode("");
        prLine.setValuationPrice(StringUtils.EMPTY);
        prLine.setRequisitioner("E9988");
        prLine.setCreatedBy("E9988");
        if (isSupport) {
            prLine.setVendorId(cpeMaterialRequestDetail.getSupportVendorId());
            prLine.setServiceNo(cpeMaterialRequestDetail.getSupportServiceNumber());
            prLine.setShortText(cpeMaterialRequestDetail.getSupportShortText());
            prLine.setShortText1(cpeMaterialRequestDetail.getSupportShortText());
        } else {
            prLine.setVendorId(cpeMaterialRequestDetail.getInstallationVendorId());
            prLine.setServiceNo(cpeMaterialRequestDetail.getInstallationServiceNumber());
            prLine.setShortText(cpeMaterialRequestDetail.getInstallationShortText());
            prLine.setShortText1(cpeMaterialRequestDetail.getInstallationShortText());
        }
        prLine.setGrossPrice(cost != null ? cost.toString(): "");
        // plant and storage location
        List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
                .findByState(scComponentAttributesAMap.get("destinationState"));
        for (MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping : distributionCenterMapping) {
            prLine.setStorageLocation(String.valueOf(
                    mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getSapStorageLocation()));
            prLine.setPlant(mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getPlant());
            break;
        }

        prLine.setReqTrackingNumber(processInstanceId);
        prLine.setQuantity(StringUtils.EMPTY);
        prLine.setItemCategory("D");
        prLine.setAcctAssignmentCategory("P");
        // purchasing organisation
        if (scComponentAttributesAMap.get("destinationCountry").equalsIgnoreCase("india")) {
            prLine.setPurchasingOrganisation("TCIN");
        } else {
            prLine.setPurchasingOrganisation("INTL");
        }
        prLine.setReceivedDate(formatter.format(scServiceDetail.getScOrder().getCreatedDate()));
        prLine.setValuationType("NEW");
        prLine.setUnitOfMeasurement("AU");
        // delivery date
        TaskPlan taskPlan = null;
        taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(scServiceDetail.getId(), "install-cpe", "A");
        if (taskPlan != null) {
            LocalDateTime cpeTime = taskPlan.getPlannedStartTime().toLocalDateTime().minusDays(7);
            LOGGER.info("cpe time:{}", cpeTime);
            if (cpeTime.isAfter(LocalDateTime.now().plusDays(7))) {
                prLine.setDeliveryDate(formatter.format(Timestamp.valueOf(cpeTime)));
            } else {
                prLine.setDeliveryDate(formatter.format(Timestamp.valueOf(LocalDateTime.now().plusDays(7))));
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 3);
            prLine.setDeliveryDate(formatter.format(cal.getTime()));
        }
        addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "cpePoDeliveryDate", prLine.getDeliveryDate());
        // wbs
        ScComponentAttribute level5Wbs = scComponentAttributesRepository
                .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                        scServiceDetail.getId(), "level5Wbs", "LM", "A");
        if (level5Wbs != null)
            prLine.setWBSElement(level5Wbs.getAttributeValue());
        else
            prLine.setWBSElement(StringUtils.EMPTY);
        prLine.setCostCenter(StringUtils.EMPTY);
        prLine.setGLAccount(StringUtils.EMPTY);
        prLine.setUnitOfMeasurement1("");
        prLine.setQuantity1(cpeMaterialRequestDetail.getQuantity());
        prLine.setCurrency("INR");
        prLine.setMaterialGroup("ITCOSDHDD");
        // opportunity region
        ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
                .findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), "Owner Region");
        if (scServiceAttribute != null && scServiceAttribute.getAttributeValue() != null) {
            LOGGER.info("Opportunity Region exists for service Id::{}", scServiceDetail.getId());
            prLine.setOpportunityRegion(scServiceAttribute.getAttributeValue().toUpperCase());
        } else {
            LOGGER.info("Defaulting Opportunity Region as India for service Id::{}", scServiceDetail.getId());
            prLine.setOpportunityRegion("INDIA");
        }
        prLine.setCircuitType(scServiceDetail.getErfPrdCatalogProductName());
        String typeOfDeal = "";
        if (invType.toLowerCase().contains("outright")) {
            typeOfDeal = "SALE";
        } else {
            typeOfDeal = "RENTAL";
        }
        prLine.setTypeOfDeal(typeOfDeal);
        // confirm -TODO - GET FROM L2O
        prLine.setOpportunityCategory("CAT 1-2");
        prLine.setCUID(scServiceDetail.getScOrder().getTpsSfdcCuid());
        prLine.setCustomerName(scServiceDetail.getScOrder().getErfCustCustomerName());
        prLine.setServiceId(scServiceDetail.getUuid());
        prLine.setCOFRefNo(scServiceDetail.getScOrderUuid());
        prLines.add(prLine);
    }

    /**
     * @author Madhumiethaa
     * @param serviceId
     * @param execution
     * @param siteType
     * @return
     */
    public String processHanaAutoPoTerminate(Integer serviceId, DelegateExecution execution, String siteType) {
        LOGGER.info("Inside process hana auto po terminate for service id {}", serviceId);
        LastMileRequest autoPoRequest = new LastMileRequest();
        LastMileResponse autoPoResponse = null;
        String errorMessage = "";

        try {
            ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();

            Map<String, String> prMapper = new HashMap<>();
            if (scServiceDetail != null) {
                String serviceCode = scServiceDetail.getUuid();
                LOGGER.info("Got processOffnetAutoPoTerminate serviceCode {}", serviceCode);
                if (execution.getVariable("skipOffnet")==null || execution.getVariable("skipOffnet").equals(false)) {

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
                    contructHanaAutoPoRequestTerminate(autoPoRequest, scOrderAttributes, scComponentAttributesAMap,
                            scServiceDetail, scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes,
                            orderSubCategoryMap, terminationTypeMap, sapInterfaceMap, sapBuyerMap);
                }
                LOGGER.info("offnetLmTeriminateRequest json {}", Utils.convertObjectToJson(autoPoRequest));
                AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPoRequest),
                        null, serviceCode, "AUTOPO-TERMINATE", "AutoPO" + serviceCode);
                try {
                    RestResponse response = restClientService.postWithBasicAuthentication(sapLastMileUrl,
                            Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
                    LOGGER.info("Auto PO terminate response {}", Utils.convertObjectToJson(response));
                    if (response != null) {
                        if (response.getData() != null) {
                            LOGGER.info("Auto PO terminate response data{}", response.getData());
                            autoPoResponse = Utils.convertJsonToObject(response.getData(), LastMileResponse.class);
                            LOGGER.info("Auto Po terminate Output json {}", Utils.convertObjectToJson(autoPoResponse));
                            inventory.setResponse(response.getData());
                            auditLogRepositoryRepository.save(inventory);
                        }

                    }

                    if (response != null && response.getStatus() == Status.SUCCESS) {
                        LOGGER.info("Success");
                    }

                } catch (Exception e) {
                    LOGGER.info("Error in processing po terminate request {}", e.getMessage());
                }
            }

            if (autoPoResponse != null && autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP() != null
                    && !autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().isEmpty() && execution != null) {
                OPTIMUSREFXINDIAASPACCESSRESP poResponse = autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0);
                prMapper.put("offnetLocalPoTerminateDate", DateUtil.convertDateToString(new Date()));
                prMapper.put("offnetLocalPoTerminateStatus", poResponse.getPOSTATUS());
                execution.setVariable("offnetLocalPoTerminateStatus", poResponse.getPOSTATUS());

                if (!poResponse.getPOSTATUS().equalsIgnoreCase("Fail")) {
                    execution.setVariable("offnetPOTerminateCompleted", true);
                } else {
                    execution.setVariable("offnetPOTerminateCompleted", false);
                    errorMessage = "Offnet Terminate PO Failure Reason :" + poResponse.getREMARK();
                }

            } else if (execution != null) {
                execution.setVariable("offnetPOTerminateCompleted", false);
                errorMessage = "Offnet Terminate PO Failure Reason :"
                        + autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getREMARK();
            }
            errorMessage += ", Offnet Terminate PO Number:"
                    + autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPONUMBER();
            errorMessage += ", Offnet PO Terminate Status:"
                    + autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPOSTATUS();

            componentAndAttributeService.updateAttributes(serviceId, prMapper, AttributeConstants.COMPONENT_LM, "A");

        } catch (Exception e) {
            LOGGER.error("Error-creating-autopo-terminate-request {}", e.getMessage());
            LOGGER.error("Exception-creating-autopoterminate-request {}", e);
        }

        return errorMessage;
    }

    private void contructHanaAutoPoRequestTerminate(LastMileRequest autoPoRequest,
                                                    Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                    ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                    Map<String, String> scServiceAttributes, Map<String, String> orderSubCategoryMap,
                                                    Map<String, String> terminateTypeMap, Map<String, String> sapInterfaceMap,
                                                    Map<String, String> sapBuyerMap) {
        LOGGER.info("Inside contructAutoPoRequest for terminatePO");
        OPTIMUSREFXINDIAASPACCESS autoPoMsgs = new OPTIMUSREFXINDIAASPACCESS();
        contructHanaAutoPoHeaderTerminate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail,
                scOrder, scContractInfo, scServiceAttributes, orderSubCategoryMap, sapInterfaceMap, sapBuyerMap);
        constructHanaAutoPoTerminationLineItems(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail,
                scOrder, scContractInfo, scServiceAttributes, orderSubCategoryMap, terminateTypeMap, sapBuyerMap);
        autoPoRequest.setOPTIMUSREFXINDIAASPACCESS(autoPoMsgs);
    }

    private void contructHanaAutoPoHeaderTerminate(OPTIMUSREFXINDIAASPACCESS autoPoMsgs,
                                                   Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                   ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                   Map<String, String> scServiceAttributes, Map<String, String> orderSubCategoryMap,
                                                   Map<String, String> sapInterfaceMap, Map<String, String> sapBuyerMap) {
        LOGGER.info("Inside contructHanaAutoPoHeader");
        HeaderDetails autoPoHeader = new HeaderDetails();
        autoPoHeader.setHEADERTEXT(CommonConstants.EMPTY);
        autoPoHeader.setSCENARIOTYPE("TERM");
        autoPoHeader.setREQTYPE("UPDATE");
        autoPoHeader.setPOTYPE(CommonConstants.EMPTY);
        autoPoHeader.setCustomerName(scServiceDetail.getScOrder().getErfCustCustomerName());
        ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
                .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                        scServiceDetail.getId(), "oldBsoCircuitId", AttributeConstants.COMPONENT_LM,
                        AttributeConstants.SITETYPE_A);
        List<Stg0SapPoDtlOptimus> stg0SapPoDtlOptimus = null;
        if (scComponentAttribute != null) {
            stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
                    .findByVendorRefIdOrderIdAndTclServiceIdAndTerminationTypeIsNull(
                            scComponentAttribute.getAttributeValue(), scServiceDetail.getUuid());
        }
        if (StringUtils.isNotBlank(scServiceDetail.getOrderSubCategory())
                && !scServiceDetail.getOrderSubCategory().equalsIgnoreCase(CommonConstants.NEW))
            autoPoHeader.setRECNNR((Objects.nonNull(scOrder.getOrderCategory())
                    && !scOrder.getOrderCategory().equalsIgnoreCase("ADD_SITE") && stg0SapPoDtlOptimus != null
                    && !stg0SapPoDtlOptimus.isEmpty() && stg0SapPoDtlOptimus.get(0).getPoNumber() != null)
                    ? stg0SapPoDtlOptimus.get(0).getPoNumber()
                    : CommonConstants.EMPTY);
        else
            autoPoHeader.setRECNNR(CommonConstants.EMPTY);
        autoPoHeader.setCOMPANYCODE("1000");
        autoPoHeader.setVENDORID(CommonConstants.EMPTY);
        autoPoHeader.setCURRENCY(CommonConstants.EMPTY);
        autoPoHeader.setBUYER(CommonConstants.EMPTY);
        autoPoHeader.setCPEOWNERS(CommonConstants.EMPTY);
        autoPoHeader.setCOPFID(scOrder.getUuid());
        if (scServiceDetail.getTerminationFlowTriggered() != null
                && scServiceDetail.getTerminationFlowTriggered().equalsIgnoreCase("Yes")) {
            autoPoHeader.setServiceID(scServiceDetail.getUuid());

        } else {

            if (scServiceDetail.getOrderSubCategory().contains("Parallel"))
                autoPoHeader.setServiceID(scServiceDetail.getParentUuid());
            else
                autoPoHeader.setServiceID(scServiceDetail.getUuid());

        }
        autoPoHeader.setProductComponent(CommonConstants.EMPTY); // OPTIMUS LOGIC - if ILL AND GVPN - DEFAULT TO
        // A_END_LM , if
        // NDE/NPL -
        autoPoHeader.setPOCategory(CommonConstants.EMPTY);
        autoPoHeader
                .setCircuitType(scServiceDetail.getErfPrdCatalogProductName() != null
                        ? scServiceDetail.getErfPrdCatalogProductName().equals("IAS") ? "ILL"
                        : scServiceDetail.getErfPrdCatalogProductName()
                        : CommonConstants.EMPTY);
        autoPoHeader.setSFDCID(CommonConstants.EMPTY);
        autoPoHeader.setSfdcOppId(CommonConstants.EMPTY);
        autoPoHeader.setUNSEZ(CommonConstants.EMPTY); // Supplier Feasibility ID
        autoPoHeader.setTypeOfDeal(CommonConstants.EMPTY);
        autoPoHeader.setPONumber(CommonConstants.EMPTY);
        autoPoHeader.setSiteA(CommonConstants.EMPTY);
        autoPoHeader.setSiteB(CommonConstants.EMPTY);
        autoPoHeader.setInterfaceType(CommonConstants.EMPTY);
        autoPoHeader.setBANDWIDTHSPEED(CommonConstants.EMPTY);
        autoPoHeader.setMULTIPLEPO(CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR1(
                scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
                        : CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setAENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNAME(scComponentAttributes.containsKey("localItContactName")
                ? scComponentAttributes.get("localItContactName")
                : CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNUMBER(scComponentAttributes.containsKey("localItContactMobile")
                ? scComponentAttributes.get("localItContactMobile")
                : CommonConstants.EMPTY);
        autoPoHeader.setAENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setAENDZIP(CommonConstants.EMPTY);
        autoPoHeader.setBENDADDR1(
                (scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address")
                        : CommonConstants.EMPTY));
        autoPoHeader.setBENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setBENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNAME(scComponentAttributes.containsKey("localItContactName")
                ? scComponentAttributes.get("localItContactName")
                : CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNUMBER(scComponentAttributes.containsKey("localItContactMobile")
                ? scComponentAttributes.get("localItContactMobile")
                : CommonConstants.EMPTY);
        autoPoHeader.setBENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setBENDZIP(CommonConstants.EMPTY);
        autoPoMsgs.setHeaderDetails(autoPoHeader);
    }

    private void constructHanaAutoPoTerminationLineItems(OPTIMUSREFXINDIAASPACCESS autoPoMsgs,
                                                         Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                         ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                         Map<String, String> scServiceAttributes, Map<String, String> orderSubCategoryMap,
                                                         Map<String, String> terminateTypeMap, Map<String, String> sapBuyerMap) {
        LOGGER.info("Inside constructHanaAutoPoTerminationDetails");
        ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
                .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                        scServiceDetail.getId(), "oldBsoCircuitId", AttributeConstants.COMPONENT_LM,
                        AttributeConstants.SITETYPE_A);
        if (scComponentAttribute != null) {
            List<Stg0SapPoDtlOptimus> stg0SapPoDtlOptimuses = stg0SapPoDtlOptimusRepository
                    .findByVendorRefIdOrderIdAndTclServiceIdAndTerminationTypeIsNull(
                            scComponentAttribute.getAttributeValue(), scServiceDetail.getUuid());
            if (stg0SapPoDtlOptimuses != null && !stg0SapPoDtlOptimuses.isEmpty()) {
                LineItems autoPoLineItems = new LineItems();
                String terminationType = "";
                if ((Objects.nonNull(scOrder.getOrderCategory())
                        && (scOrder.getOrderCategory().equalsIgnoreCase("CHANGE_BANDWIDTH")
                        || scOrder.getOrderCategory().equalsIgnoreCase("SHIFT_SITE"))
                        && scServiceDetail.getOrderSubCategory().contains("Hot")
                        && scServiceDetail.getOrderSubCategory().contains("BSO Change"))) {
                    LOGGER.info("Entering Hot Upgrade-Bso Change case");
                    terminationType = getChangeBWOrShiftSiteHotUpgradeTerminationType(
                            scServiceDetail.getOrderSubCategory(), scOrder.getOrderCategory());
                } else if (scServiceDetail.getOrderSubCategory().contains("Parallel")) {
                    LOGGER.info("Entering Parallel case");
                    boolean bsoFlag = isSameBSOorDifferent(scComponentAttributes.get("vendorId"),
                            scServiceAttributes.get("vendor_id"));
                    terminationType = getTerminationTypeBasedOnBSOStatus(scServiceDetail.getOrderSubCategory(),
                            bsoFlag);
                } else {
                    LOGGER.info("Entering other cases");
                    terminationType = terminateTypeMap.get(scServiceDetail.getOrderSubCategory());
                }
                LOGGER.info("Termination Type:" + terminationType);
                autoPoLineItems.setTERMINATIONTYPE(terminationType);
                autoPoLineItems.setFROMEND(CommonConstants.EMPTY);
                autoPoLineItems.setTOEND(CommonConstants.EMPTY);
                autoPoLineItems.setCHARGEABLEDISTANCE(CommonConstants.EMPTY);
                autoPoLineItems.setVENDORREFID(CommonConstants.EMPTY);
                autoPoLineItems.setOPERATORNAME(CommonConstants.EMPTY);
                autoPoLineItems.setCOMMISSIONDATE(CommonConstants.EMPTY);
                try {
                    autoPoLineItems.setTERMINATIONDATE(scComponentAttributes.containsKey("terminationDate")
                            ? formatter.format(commonFulfillmentUtils
                            .getDate(scComponentAttributes.get("terminationDate"), formatterCdate))
                            : CommonConstants.EMPTY);
                    // Setting termination date for offnet PO
                    if (scServiceDetail.getTerminationFlowTriggered() != null
                            && scServiceDetail.getTerminationFlowTriggered().equalsIgnoreCase(CommonConstants.YES)
                            && scServiceDetail.getTerminationEffectiveDate() != null) {
                        LOGGER.info("Termination date for Offnet PO {} :",
                                scServiceDetail.getTerminationEffectiveDate());
                        String terminationDate = formatterCdate.format(scServiceDetail.getTerminationEffectiveDate());
                        autoPoLineItems.setTERMINATIONDATE(terminationDate != null
                                ? formatter.format(commonFulfillmentUtils.getDate(terminationDate, formatterCdate))
                                : CommonConstants.EMPTY);
                        autoPoLineItems.setTERMINATIONDATE(terminationDate != null
                                ? formatter.format(commonFulfillmentUtils.getDate(terminationDate, formatterCdate))
                                : CommonConstants.EMPTY);
                    }
                } catch (Exception e) {
                    LOGGER.info("Error on parsing the date");
                }
                autoPoLineItems.setCrfsDate(CommonConstants.EMPTY);
                autoPoLineItems.setRfsDate(CommonConstants.EMPTY);
                List<ServiceItems> serviceItems = new ArrayList<>();
                for (Stg0SapPoDtlOptimus stg0SapPoDtlOptimus : stg0SapPoDtlOptimuses) {
                    constructAutoPoServiceItemsTerminate(serviceItems, scOrderAttributes, scComponentAttributes,
                            scServiceDetail, scOrder, scContractInfo, scServiceAttributes);
                }
                autoPoLineItems.setServiceItems(serviceItems);
                autoPoMsgs.setLineItems(autoPoLineItems);
            }

        }

    }

    private void constructAutoPoServiceItemsTerminate(List<ServiceItems> serviceItems,
                                                      Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                      ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                      Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructAutoPoServiceItemsTerminate");
        ServiceItems autoPoServiceItems = new ServiceItems();
        autoPoServiceItems.setSERVICENUMBER(CommonConstants.EMPTY);
        autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
        autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
        autoPoServiceItems.setGROSSPRICE(CommonConstants.EMPTY);
        serviceItems.add(autoPoServiceItems);
    }


    /**
     * @author Madhumiethaa
     * @param scServiceDetailId
     * @param execution
     * @return
     * @throws TclCommonException
     */
    public String processHanaOffnetAutoPo(Integer scServiceDetailId, DelegateExecution execution)
            throws TclCommonException {
        LOGGER.info("Inside processHanaOffnetAutoPo for service id {}", scServiceDetailId);
        LastMileRequest autoPoRequest = new LastMileRequest();
        LastMileResponse autoPoResponse = null;
        String errorMessage = "";
        try {
            ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetailId).get();
            String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());

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

                String upgradeType = StringUtils.trimToEmpty(scServiceAttributes.get("upgrade_type"));

                if (scServiceAttributes.containsKey("feasibility_response_id")
                        && StringUtils.isNotBlank(scServiceAttributes.get("feasibility_response_id"))) {
                    Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
                    if (Objects.nonNull(scServiceDetail.getOrderSubCategory()) && (scServiceDetail.getOrderSubCategory()
                            .toLowerCase().contains("bso")
                            || scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")
                            || (StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)))) {
                        String tclServiceId = (scServiceDetail.getParentUuid() != null
                                && StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
                                ? scServiceDetail.getParentUuid()
                                : scServiceDetail.getUuid();
                        stg0SapPoDtlOptimus = getSapData(tclServiceId);
                        ParentPoBean parentPoData = getParentPoData(tclServiceId);
                        updateParentPoDataForTermination(scServiceDetail, stg0SapPoDtlOptimus, parentPoData);
                    }

                    /*
                     * if (Objects.nonNull(stg0SapPoDtlOptimus)) { if
                     * (StringUtils.isNotBlank(stg0SapPoDtlOptimus.getVendorNo())) {
                     * List<Stg0SfdcVendorC> vendorList =
                     * stgSfdcVendorRepository.findByVendorIdC(stg0SapPoDtlOptimus.getVendorNo());
                     * Stg0SfdcVendorC vendor = (Objects.nonNull(vendorList) &&
                     * !vendorList.isEmpty() ? vendorList.stream().findFirst().get() : null); if
                     * (Objects.nonNull(vendor)) { addOrUpdateScComponentAttributes(scServiceDetail,
                     * "LM", "A", "vendorId", stg0SapPoDtlOptimus.getVendorNo());
                     * addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "vendorName",
                     * vendor.getName()); addOrUpdateScComponentAttributes(scServiceDetail, "LM",
                     * "A", "sfdcProviderName", vendor.getSfdcProviderNameC());
                     * addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A",
                     * "oldOffnetPoNumber", stg0SapPoDtlOptimus.getPoNumber());
                     * addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A",
                     * "oldBsoCircuitId", stg0SapPoDtlOptimus.getVendorRefIdOrderId()); } } }
                     */

                    contructHanaAutoPoRequest(autoPoRequest, scOrderAttributes, scComponentAttributesAMap,
                            scServiceDetail, scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes,
                            orderSubCategoryMap, sapInterfaceMap, sapBuyerMap, stg0SapPoDtlOptimus);
                    LOGGER.info("processHanaOffnetAutoPo Request json {}", Utils.convertObjectToJson(autoPoRequest));
                    AuditLog inventory = saveDataIntoNetworkInventory(
                            Utils.convertObjectToJson(autoPoRequest), null, serviceCode,
                            "AUTOPO", "AutoPO" + serviceCode);
                    try {
                        RestResponse response = restClientService.postWithBasicAuthentication(sapLastMileUrl,
                                Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
                        LOGGER.info("processHanaOffnetAutoPo response {}", response);
                        if (response != null) {
                            if (response.getData() != null) {
                                LOGGER.info("processHanaOffnetAutoPo response data{}", response.getData());
                                autoPoResponse = Utils.convertJsonToObject(response.getData(), LastMileResponse.class);
                                if (autoPoResponse != null && autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP() != null
                                        && !autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().isEmpty()) {
                                    OPTIMUSREFXINDIAASPACCESSRESP optimusrefxindiaaspaccessresp = autoPoResponse
                                            .getOPTIMUSREFXINDIAASPACCESSRESP().get(0);
                                    if (optimusrefxindiaaspaccessresp.getPONUMBER() != null) {
                                        LOGGER.info("PO number for service ID {} is {}", serviceCode,
                                                optimusrefxindiaaspaccessresp.getPONUMBER());
                                        addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sapPoNumber",
                                                optimusrefxindiaaspaccessresp.getPONUMBER().toString());
                                    }
//									if (autoPoResponse.getPOResponse().getPODate() != null
//											&& !autoPoResponse.getPOResponse().getPODate().equals(CommonConstants.EMPTY)) {
//										LOGGER.info("PO date for service ID {} is {}", serviceCode,
//												autoPoResponse.getPOResponse().getPODate());
//										addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "sapPoDate",
//												formatter
//														.format(formatterPo.parse(
//																autoPoResponse.getPOResponse().getPODate().toString()))
//														.toString());
//									}
                                }
                                inventory.setResponse(response.getData());
                                auditLogRepositoryRepository.save(inventory);
                            }

                        }
                        if (response != null && response.getStatus() == Status.SUCCESS) {
                            LOGGER.info("Success");
                        }

                    } catch (Exception e) {
                        LOGGER.error("Error in processing po create request {}", e);
                    }

                    LOGGER.info("Enter Execution Data" + execution);
                    if (autoPoResponse != null && autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP() != null
                            && !autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().isEmpty() && execution != null) {
                        OPTIMUSREFXINDIAASPACCESSRESP optimusrefxindiaaspaccessresp = autoPoResponse
                                .getOPTIMUSREFXINDIAASPACCESSRESP().get(0);
                        prMapper.put("offnetLocalPoNumber", optimusrefxindiaaspaccessresp.getPONUMBER().toString());
                        prMapper.put("offnetLocalPoStatus", optimusrefxindiaaspaccessresp.getPOSTATUS());
                        prMapper.put("offnetLocalPoDate", DateUtil.convertDateToString(new Date()));

                        execution.setVariable("offnetLocalPoNumber", optimusrefxindiaaspaccessresp.getPONUMBER());
                        execution.setVariable("offnetLocalPoStatus", optimusrefxindiaaspaccessresp.getPOSTATUS());

                        if (!optimusrefxindiaaspaccessresp.getPOSTATUS().equalsIgnoreCase("Fail")) {
                            execution.setVariable("offnetPOCompleted", true);
                        } else {
                            execution.setVariable("offnetPOCompleted", false);
                        }

                        LOGGER.info("Execution Data" + execution);

                        LOGGER.info("AutoPoResponse Data" + autoPoResponse);

                        errorMessage = "Offnet PO Number:" + optimusrefxindiaaspaccessresp.getPONUMBER();
                        errorMessage += ", Offnet PO Status:" + optimusrefxindiaaspaccessresp.getPOSTATUS();
                        componentAndAttributeService.updateAttributes(scServiceDetailId, prMapper,
                                AttributeConstants.COMPONENT_LM, "A");
                        LOGGER.info("PrMapper Data" + prMapper);
                        errorMessage = optimusrefxindiaaspaccessresp.getREMARK();

                    } else if (execution != null) {
                        execution.setVariable("offnetPOCompleted", false);
                        errorMessage = autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getREMARK();
                    }

                    if (execution != null)
                        saveProCreation(scServiceDetail, execution.getProcessInstanceId(), "OFFNETLM",
                                autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPONUMBER().toString(), null,
                                autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPOSTATUS(),
                                new Timestamp(new Date().getTime()), execution.getCurrentActivityId(), null, null, null, null);

                } else if (execution != null) {
                    execution.setVariable("offnetPOCompleted", false);
                    errorMessage = "Feasibility Response ID is missing from L2O";
                    LOGGER.info("Error message for attribute missing :" + errorMessage);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error-creating-offnet-autopo-request {}", e);
        }
        return errorMessage;
    }

    private void contructHanaAutoPoRequest(LastMileRequest autoPoRequest, Map<String, String> scOrderAttributes,
                                           Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
                                           ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
                                           Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
                                           Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) throws TclCommonException {
        LOGGER.info("Inside contructHanaAutoPoRequest");
        OPTIMUSREFXINDIAASPACCESS autoPoMsgs = new OPTIMUSREFXINDIAASPACCESS();
        contructHanaAutoPoHeader(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
                scContractInfo, scServiceAttributes, orderSubCategoryMap, sapInterfaceMap, sapBuyerMap,
                stg0SapPoDtlOptimus);
        constructHanaAutoPoLineitems(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
                scContractInfo, scServiceAttributes, orderSubCategoryMap, sapBuyerMap, stg0SapPoDtlOptimus);
        autoPoRequest.setOPTIMUSREFXINDIAASPACCESS(autoPoMsgs);
    }

    private void contructHanaAutoPoHeader(OPTIMUSREFXINDIAASPACCESS autoPoMsgs, Map<String, String> scOrderAttributes,
                                          Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
                                          ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
                                          Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
                                          Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) throws TclCommonException {
        LOGGER.info("Inside contructHanaAutoPoHeader");
        HeaderDetails autoPoHeader = new HeaderDetails();
        String oldSiteAddress = null;
        autoPoHeader.setServiceType(orderSubCategoryMap.containsKey(scServiceDetail.getOrderSubCategory() != null
                ? scServiceDetail.getOrderSubCategory()
                : CommonConstants.NEW)
                ? (orderSubCategoryMap.get(
                scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory()
                        : CommonConstants.NEW))
                : CommonConstants.EMPTY);
        if (StringUtils.isNotBlank(autoPoHeader.getServiceType())
                && autoPoHeader.getServiceType().equalsIgnoreCase(CommonConstants.SHIFTING_LOCATION)) {
            if (Objects.nonNull(scServiceDetail.getServiceLinkId())) {
                ScServiceDetail scServiceDetailData = scServiceDetailRepository
                        .findById(scServiceDetail.getServiceLinkId()).get();
                oldSiteAddress = scServiceDetailData.getSiteAddress();
                LOGGER.info("Old Site Address-" + oldSiteAddress);
            }
        }
        String headerText = "";
        if (StringUtils.isNotBlank(autoPoHeader.getServiceType())
                && autoPoHeader.getServiceType().equalsIgnoreCase(CommonConstants.SHIFTING_LOCATION)) {
            headerText = "Old A-END : ".concat(Objects.nonNull(oldSiteAddress) ? oldSiteAddress : CommonConstants.EMPTY)
                    .concat("\n New A-END : ")
                    .concat(Objects.nonNull(scOrder.getErfCustCustomerName())
                            ? scOrder.getErfCustCustomerName() + CommonConstants.SPACE
                            : CommonConstants.EMPTY)
                    .concat((scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
                            : CommonConstants.EMPTY));
        } else
            headerText = "A-END : "
                    .concat(Objects.nonNull(scOrder.getErfCustCustomerName())
                            ? scOrder.getErfCustCustomerName() + CommonConstants.SPACE
                            : CommonConstants.EMPTY)
                    .concat((scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
                            : CommonConstants.EMPTY));

        autoPoHeader.setHEADERTEXT(headerText.concat("\n B-END : ")
                .concat((scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address")
                        : CommonConstants.EMPTY))
                .concat("\n Local Contact Name : ")
                .concat((scComponentAttributes.containsKey("localItContactName")
                        ? scComponentAttributes.get("localItContactName")
                        : CommonConstants.EMPTY))
                .concat("\n Local Contact Number : ")
                .concat((scComponentAttributes.containsKey("localItContactMobile")
                        ? scComponentAttributes.get("localItContactMobile")
                        : CommonConstants.EMPTY)));
        autoPoHeader.setSCENARIOTYPE(CommonConstants.EMPTY);
        autoPoHeader.setREQTYPE("CREATE");
        autoPoHeader.setPOTYPE("LL");
        if (StringUtils.isNotBlank(scServiceDetail.getOrderSubCategory())
                && !scServiceDetail.getOrderSubCategory().equalsIgnoreCase(CommonConstants.NEW))
            autoPoHeader.setRECNNR((Objects.nonNull(scOrder.getOrderCategory())
                    && !scOrder.getOrderCategory().equalsIgnoreCase("ADD_SITE") && stg0SapPoDtlOptimus != null
                    && stg0SapPoDtlOptimus.getPoNumber() != null) ? stg0SapPoDtlOptimus.getPoNumber()
                    : CommonConstants.EMPTY);
        else
            autoPoHeader.setRECNNR(CommonConstants.EMPTY);
        autoPoHeader.setCOMPANYCODE("1000");
        String vendorCode = scServiceAttributes.containsKey("vendor_id") ? scServiceAttributes.get("vendor_id")
                : CommonConstants.EMPTY;
        autoPoHeader.setVENDORID(vendorCode);
        autoPoHeader.setCURRENCY(
                scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
                        : CommonConstants.EMPTY);
        autoPoHeader.setBUYER(vendorCode.equals(CommonConstants.EMPTY) ? CommonConstants.EMPTY
                : (sapBuyerMap.containsKey(vendorCode) ? sapBuyerMap.get(vendorCode) : CommonConstants.EMPTY));
        String operatorName = (scServiceAttributes.containsKey("closest_provider_bso_name")
                ? scServiceAttributes.get("closest_provider_bso_name")
                : (scServiceAttributes.containsKey("vendor_name") ? scServiceAttributes.get("vendor_name")
                : CommonConstants.EMPTY));
        autoPoHeader.setCPEOWNERS(Objects.nonNull(operatorName) && (operatorName.equalsIgnoreCase("RADWIN WITH TTSL BH")
                || operatorName.equalsIgnoreCase("RADWIN WITH TTML BH")) ? ("BH") : ("NOT REQUIRED"));
        autoPoHeader.setCOPFID(scOrder.getUuid());
        autoPoHeader.setServiceID(scServiceDetail.getUuid());
        autoPoHeader.setCustomerName(scServiceDetail.getScOrder().getErfCustCustomerName());
        autoPoHeader.setProductComponent("A_END_LM");
        autoPoHeader.setPOCategory("NORMAL");
        autoPoHeader
                .setCircuitType(scServiceDetail.getErfPrdCatalogProductName() != null
                        ? scServiceDetail.getErfPrdCatalogProductName().equals("IAS") ? "ILL"
                        : scServiceDetail.getErfPrdCatalogProductName()
                        : CommonConstants.EMPTY);
        String feasibilityId = scServiceAttributes.containsKey("feasibility_response_id")
                ? scServiceAttributes.get("feasibility_response_id")
                : CommonConstants.EMPTY;
        if (StringUtils.isBlank(feasibilityId))
            feasibilityId = scServiceAttributes.containsKey("task_id") ? scServiceAttributes.get("task_id")
                    : CommonConstants.EMPTY;
        autoPoHeader.setSFDCID(feasibilityId);
        autoPoHeader.setSfdcOppId(scOrder.getUuid());
        String providerReferenceNumber = scServiceAttributes.containsKey("provider_reference_number")
                ? scServiceAttributes.get("provider_reference_number")
                : CommonConstants.EMPTY;
        autoPoHeader.setUNSEZ(providerReferenceNumber);
        String mfContractTerm = StringUtils.trimToEmpty(scServiceAttributes.get("mf_contract_term"));
        String deal = mfContractTerm.replaceAll(" ", "").toLowerCase().replaceAll("months", "-Months");
        autoPoHeader.setTypeOfDeal(deal);
        if (StringUtils.isNotBlank(scServiceDetail.getOrderSubCategory())
                && !scServiceDetail.getOrderSubCategory().equalsIgnoreCase(CommonConstants.NEW))
            autoPoHeader.setPONumber((Objects.nonNull(scOrder.getOrderCategory())
                    && !scOrder.getOrderCategory().equalsIgnoreCase("ADD_SITE") && stg0SapPoDtlOptimus != null
                    && stg0SapPoDtlOptimus.getPoNumber() != null) ? stg0SapPoDtlOptimus.getPoNumber()
                    : CommonConstants.EMPTY);
        else
            autoPoHeader.setPONumber(CommonConstants.EMPTY);
        autoPoHeader.setSiteA(CommonConstants.EMPTY);
        autoPoHeader.setSiteB(CommonConstants.EMPTY);
        String interfaceName = (scComponentAttributes.containsKey("interface")
                ? (sapInterfaceMap.containsKey(scComponentAttributes.get("interface"))
                ? sapInterfaceMap.get(scComponentAttributes.get("interface"))
                : scComponentAttributes.get("interface"))
                : CommonConstants.EMPTY);
        autoPoHeader.setInterfaceType(interfaceName.equalsIgnoreCase("FE") ? "Fast Ethernet" : interfaceName);
        autoPoHeader.setBANDWIDTHSPEED(
                scServiceAttributes.containsKey("local_loop_bw") ? scServiceAttributes.get("local_loop_bw").concat("M")
                        : CommonConstants.EMPTY);
        autoPoHeader.setBandwidthType("LOCAL LOOP");
        autoPoHeader.setMULTIPLEPO(CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR1(
                scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
                        : CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setAENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNAME(scComponentAttributes.containsKey("localItContactName")
                ? scComponentAttributes.get("localItContactName")
                : CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNUMBER(scComponentAttributes.containsKey("localItContactMobile")
                ? scComponentAttributes.get("localItContactMobile")
                : CommonConstants.EMPTY);
        autoPoHeader.setAENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setAENDZIP(CommonConstants.EMPTY);
        autoPoHeader.setBENDADDR1(
                (scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address")
                        : CommonConstants.EMPTY));
        autoPoHeader.setBENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setBENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNAME(scComponentAttributes.containsKey("localItContactName")
                ? scComponentAttributes.get("localItContactName")
                : CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNUMBER(scComponentAttributes.containsKey("localItContactMobile")
                ? scComponentAttributes.get("localItContactMobile")
                : CommonConstants.EMPTY);
        autoPoHeader.setBENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setBENDZIP(CommonConstants.EMPTY);
        autoPoMsgs.setHeaderDetails(autoPoHeader);
    }

    private void constructHanaAutoPoLineitems(OPTIMUSREFXINDIAASPACCESS autoPoMsgs,
                                              Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                              ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                              Map<String, String> scServiceAttributes, Map<String, String> orderSubCategoryMap,
                                              Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) throws TclCommonException {
        LOGGER.info("Inside constructHanaAutoPoTerminationDetails");
        LineItems autoPoLineItems = new LineItems();
        autoPoLineItems.setTERMINATIONTYPE(CommonConstants.EMPTY);
        autoPoLineItems.setFROMEND(
                scComponentAttributes.containsKey("destinationCity") ? scComponentAttributes.get("destinationCity").toUpperCase()
                        : CommonConstants.EMPTY);
        autoPoLineItems.setTOEND(
                scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address").toUpperCase()
                        : CommonConstants.EMPTY);
        autoPoLineItems.setCHARGEABLEDISTANCE(
                scServiceAttributes.containsKey("chargeable_distance") ? scServiceAttributes.get("chargeable_distance")
                        : "");

        autoPoLineItems
                .setVENDORREFID((scOrder.getOrderType() == null || scOrder.getOrderType().equals(CommonConstants.NEW))
                        ? CommonConstants.EMPTY
                        : ((stg0SapPoDtlOptimus != null && stg0SapPoDtlOptimus.getVendorRefIdOrderId() != null)
                        ? stg0SapPoDtlOptimus.getVendorRefIdOrderId()
                        : CommonConstants.EMPTY));

        autoPoLineItems.setOPERATORNAME(scServiceAttributes.containsKey("closest_provider_bso_name")
                ? scServiceAttributes.get("closest_provider_bso_name")
                : (scServiceAttributes.containsKey("vendor_name") ? scServiceAttributes.get("vendor_name")
                : CommonConstants.EMPTY));
        autoPoLineItems.setCOMMISSIONDATE(CommonConstants.EMPTY);
        autoPoLineItems.setTERMINATIONDATE(CommonConstants.EMPTY);
        autoPoLineItems.setCrfsDate(scServiceDetail.getServiceCommissionedDate() != null
                ? formatter.format(Timestamp.valueOf(scServiceDetail.getServiceCommissionedDate().toString()))
                : CommonConstants.EMPTY);
        autoPoLineItems.setRfsDate(scServiceDetail.getServiceCommissionedDate() != null
                ? formatter.format(Timestamp.valueOf(commonFulfillmentUtils
                .getPreviousDate(scServiceDetail.getServiceCommissionedDate().toString())))
                : CommonConstants.EMPTY);
        List<ServiceItems> serviceItems = new ArrayList<>();
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")
                && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
            constructHanaAutoPoServiceItemsNrc(serviceItems, scOrderAttributes, scComponentAttributes, scServiceDetail,
                    scOrder, scContractInfo, scServiceAttributes);
        }
        constructHanaAutoPoServiceItemsArc(serviceItems, scOrderAttributes, scComponentAttributes, scServiceDetail,
                scOrder, scContractInfo, scServiceAttributes);
        if ((scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")) && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL")) {
            constructHanaAutoPoServiceItemsArcModem(serviceItems, scOrderAttributes, scComponentAttributes,
                    scServiceDetail, scOrder, scContractInfo, scServiceAttributes);
        }
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL"))) {
            constructHanaAutoPoServiceItemsOtcModem(serviceItems, scOrderAttributes, scComponentAttributes,
                    scServiceDetail, scOrder, scContractInfo, scServiceAttributes);
        }
        autoPoLineItems.setServiceItems(serviceItems);
        autoPoMsgs.setLineItems(autoPoLineItems);
    }

    private void constructHanaAutoPoServiceItemsArc(List<ServiceItems> serviceItems,
                                                    Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                    ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                    Map<String, String> scServiceAttributes) throws TclCommonException {
        LOGGER.info("Inside constructAutoPoServiceItems");
        if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                || scServiceAttributes.containsKey("lm_arc_bw_offwl")) {
            ServiceItems autoPoServiceItems = new ServiceItems();
            autoPoServiceItems.setSERVICENUMBER("35000010");
            autoPoServiceItems.setQUANTITY("12");
            autoPoServiceItems.setBASEUOM("MON");
            String arcValue = null;
            String lmType = scComponentAttributes.get("lmType");
            if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                    && !scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType)
                    && lmType.equalsIgnoreCase("OffnetRF")) {
                arcValue = scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                        ? scServiceAttributes.get("lm_arc_bw_prov_ofrf")
                        : null;
            } else if (scServiceAttributes.containsKey("lm_arc_bw_offwl")
                    && !scServiceAttributes.get("lm_arc_bw_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                    && lmType.equalsIgnoreCase("OffnetWL")) {
                arcValue = scServiceAttributes.containsKey("lm_arc_bw_offwl")
                        ? scServiceAttributes.get("lm_arc_bw_offwl")
                        : null;
            }
            String mrcValue = CommonConstants.EMPTY;
            if (arcValue != null) {
                mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
            }
            if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                    && !scServiceAttributes.get("lm_arc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType)
                    && lmType.equalsIgnoreCase("OffnetRF")) {
                autoPoServiceItems.setGROSSPRICE(
                        scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf") ? mrcValue : CommonConstants.EMPTY);
            } else if (scServiceAttributes.containsKey("lm_arc_bw_offwl")
                    && !scServiceAttributes.get("lm_arc_bw_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                    && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItems.setGROSSPRICE(
                        scServiceAttributes.containsKey("lm_arc_bw_offwl") ? mrcValue : CommonConstants.EMPTY);
            }
            serviceItems.add(autoPoServiceItems);
        }
    }

    private void constructHanaAutoPoServiceItemsNrc(List<ServiceItems> serviceItems,
                                                    Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                    ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                    Map<String, String> scServiceAttributes) throws TclCommonException {
        LOGGER.info("Inside constructHanaAutoPoServiceItemsNrc");
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")
                && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
            ServiceItems autoPoServiceItems = new ServiceItems();
            autoPoServiceItems.setSERVICENUMBER("36000000");
            autoPoServiceItems.setQUANTITY("1");
            autoPoServiceItems.setBASEUOM("EA");
            if (scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                    && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType)
                    && lmType.equalsIgnoreCase("OffnetRF")) {
                autoPoServiceItems.setGROSSPRICE(scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                        ? scServiceAttributes.get("lm_nrc_bw_prov_ofrf")
                        : CommonConstants.EMPTY);
            } else if (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                    && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")
                    && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItems.setGROSSPRICE(scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                        ? scServiceAttributes.get("lm_otc_nrc_installation_offwl")
                        : CommonConstants.EMPTY);
            }
            serviceItems.add(autoPoServiceItems);
        }

    }

    private void constructHanaAutoPoServiceItemsArcModem(List<ServiceItems> serviceItems,
                                                         Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                         ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                         Map<String, String> scServiceAttributes) throws TclCommonException {
        LOGGER.info("Inside constructHanaAutoPoServiceItemsArcModem");

        // need to find attribute for wireless case
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL"))) {
            ServiceItems autoPoServiceItem = new ServiceItems();
            autoPoServiceItem.setSERVICENUMBER("35000002");
            autoPoServiceItem.setQUANTITY("12");
            autoPoServiceItem.setBASEUOM("YR");
            String arcValue = null;
            if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                    && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")
                    && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                arcValue = scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                        ? scServiceAttributes.get("lm_arc_modem_charges_offwl")
                        : null;
            }
            String mrcValue = CommonConstants.EMPTY;
            if (arcValue != null) {
                mrcValue = DECIMAL_FORMAT.format((Double.parseDouble(arcValue) / 12));
            }

            if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                    && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0")
                    && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItem.setGROSSPRICE(scServiceAttributes.containsKey("lm_arc_modem_charges_offwl") ? mrcValue
                        : CommonConstants.EMPTY);
            }
            serviceItems.add(autoPoServiceItem);
        }
    }

    private void constructHanaAutoPoServiceItemsOtcModem(List<ServiceItems> serviceItems,
                                                         Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                         ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                         Map<String, String> scServiceAttributes) throws TclCommonException {
        LOGGER.info("Inside constructHanaAutoPoServiceItemsOtcModem");
        // need to find attribute for wireless case
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL"))) {
            ServiceItems autoPoServiceItems = new ServiceItems();
            autoPoServiceItems.setSERVICENUMBER("36000002");
            autoPoServiceItems.setQUANTITY("1");
            autoPoServiceItems.setBASEUOM("EA");
            // Need to find attribute for wireless case
            if (scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                    && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0")
                    && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL")) {
                autoPoServiceItems.setGROSSPRICE(scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                        ? scServiceAttributes.get("lm_otc_modem_charges_offwl")
                        : CommonConstants.EMPTY);
            }
            serviceItems.add(autoPoServiceItems);
        }
    }

    /**
     * @author Madhumiethaa
     * @param serviceId
     * @param execution
     * @return
     */
    public String processHanaOffnetAutoPoUpdate(Integer serviceId, DelegateExecution execution) {

        LOGGER.info("Inside processHanaOffnetAutoPoUpdate serviceid= {}", serviceId);
        LastMileRequest autoPoRequest = new LastMileRequest();
        LastMileResponse autoPoResponse = null;
        String errorMessage = "";

        try {
            ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
            String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());

            Map<String, String> prMapper = new HashMap<>();
            if (scServiceDetail != null) {
                String serviceCode = scServiceDetail.getUuid();
                LOGGER.info("Got processOffnetAutoPoUpdate serviceCode {}", serviceCode);

                if (execution.getVariable("skipOffnet").equals(false)) {
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
                    if (orderCategory != null && !orderCategory.equals(CommonConstants.NEW)) {
                        String tclServiceId = (scServiceDetail.getParentUuid() != null
                                && StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
                                ? scServiceDetail.getParentUuid()
                                : scServiceDetail.getUuid();
                        stg0SapPoDtlOptimus = getSapData(tclServiceId);
                    }
                    contructHanaAutoPoRequestUpdate(autoPoRequest, scOrderAttributes, scComponentAttributesAMap,
                            scServiceDetail, scServiceDetail.getScOrder(), scContractInfo, scServiceAttributes,
                            orderSubCategoryMap, sapInterfaceMap, sapBuyerMap, stg0SapPoDtlOptimus);
                } else if (execution.getVariable("skipOffnet").equals(true)) {

                    Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
                    if (orderCategory != null && !orderCategory.equals(CommonConstants.NEW)) {
                        /*
                         * stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
                         * .findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(
                         * ((scServiceDetail.getParentUuid() != null &&
                         * StringUtils.isNotBlank(scServiceDetail.getParentUuid())) ?
                         * scServiceDetail.getParentUuid() : scServiceDetail.getUuid()), "A_END_LM");
                         */
                        String tclServiceId = (scServiceDetail.getParentUuid() != null
                                && StringUtils.isNotBlank(scServiceDetail.getParentUuid()))
                                ? scServiceDetail.getParentUuid()
                                : scServiceDetail.getUuid();
                        stg0SapPoDtlOptimus = getSapData(tclServiceId);
                    }

                    addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoNumber",
                            stg0SapPoDtlOptimus.getPoNumber());
                    addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoDate",
                            DateUtil.convertDateToString(new Date()));
                    addOrUpdateScComponentAttributes(scServiceDetail, "LM", "A", "offnetLocalPoStatus", "Success");
                    Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
                            .getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
                    contructHanaAutoPoRequestUpdatePrefeasible(autoPoRequest, scServiceDetail,
                            scComponentAttributesAMap, scServiceDetail.getScOrder());
                }

                LOGGER.info("Auto Po update Request json {}", Utils.convertObjectToJson(autoPoRequest));
                AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(autoPoRequest),
                        null, serviceCode, "AUTOPO-UPDATE", "AutoPO" + serviceCode);
                try {
                    RestResponse response = restClientService.postWithBasicAuthentication(sapLastMileUrl,
                            Utils.convertObjectToJson(autoPoRequest), createCommonHeader(), userName, password);
                    LOGGER.info("Auto PO update response {}", response);
                    if (response != null) {
                        if (response.getData() != null) {
                            autoPoResponse = Utils.convertJsonToObject(response.getData(), LastMileResponse.class);
                            LOGGER.info("Auto PO update response data {}", Utils.convertObjectToJson(autoPoResponse));
                            inventory.setResponse(response.getData());
                            auditLogRepositoryRepository.save(inventory);
                        }

                    }

                    if (response != null && response.getStatus() == Status.SUCCESS) {
                        LOGGER.info("Success");
                    }

                } catch (Exception e) {
                    LOGGER.info("Error in processing po update request {}", e.getMessage());
                }
            }

            if (autoPoResponse != null && autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP() != null
                    && !autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().isEmpty() && execution != null) {
                prMapper.put("offnetLocalPoUpdateDate", DateUtil.convertDateToString(new Date()));
                prMapper.put("offnetLocalPoUpdateStatus",
                        autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPOSTATUS());
                execution.setVariable("offnetLocalPoUpdateStatus",
                        autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPOSTATUS());

                if (!autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPOSTATUS()
                        .equalsIgnoreCase("Fail")) {
                    execution.setVariable("offnetPOUpdateCompleted", true);
                } else {
                    execution.setVariable("offnetPOUpdateCompleted", false);
                }
                errorMessage = "Offnet PO Number:"
                        + autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPONUMBER();
                errorMessage += ", Offnet PO Update Status:"
                        + autoPoResponse.getOPTIMUSREFXINDIAASPACCESSRESP().get(0).getPOSTATUS();

            } else if (execution != null) {
                execution.setVariable("offnetPOUpdateCompleted", false);
            }

            componentAndAttributeService.updateAttributes(serviceId, prMapper, AttributeConstants.COMPONENT_LM, "A");

        } catch (Exception e) {
            LOGGER.error("Error-creating-autopo-update-request {}", e.getMessage());
            LOGGER.error("Exception-creating-autopoupdate-request {}", e);
        }

        return errorMessage;
    }

    private void contructHanaAutoPoRequestUpdate(LastMileRequest autoPoRequest, Map<String, String> scOrderAttributes,
                                                 Map<String, String> scComponentAttributes, ScServiceDetail scServiceDetail, ScOrder scOrder,
                                                 ScContractInfo scContractInfo, Map<String, String> scServiceAttributes,
                                                 Map<String, String> orderSubCategoryMap, Map<String, String> sapInterfaceMap,
                                                 Map<String, String> sapBuyerMap, Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) {
        LOGGER.info("Inside contructHanaAutoPoRequestUpdate");
        OPTIMUSREFXINDIAASPACCESS autoPoMsgs = new OPTIMUSREFXINDIAASPACCESS();
        contructHanaAutoPoHeaderUpdate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail, scOrder,
                scContractInfo, scServiceAttributes, orderSubCategoryMap, sapInterfaceMap, sapBuyerMap,
                stg0SapPoDtlOptimus);
        constructHanaAutoPoLineItemsUpdate(autoPoMsgs, scOrderAttributes, scComponentAttributes, scServiceDetail,
                scOrder, scContractInfo, scServiceAttributes, orderSubCategoryMap, sapBuyerMap);
        autoPoRequest.setOPTIMUSREFXINDIAASPACCESS(autoPoMsgs);
    }

    private void contructHanaAutoPoHeaderUpdate(OPTIMUSREFXINDIAASPACCESS autoPoMsgs,
                                                Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                Map<String, String> scServiceAttributes, Map<String, String> orderSubCategoryMap,
                                                Map<String, String> sapInterfaceMap, Map<String, String> sapBuyerMap,
                                                Stg0SapPoDtlOptimus stg0SapPoDtlOptimus) {
        LOGGER.info("Inside contructHanaAutoPoHeaderUpdate");
        HeaderDetails autoPoHeader = new HeaderDetails();

        autoPoHeader.setHEADERTEXT(CommonConstants.EMPTY);
        autoPoHeader.setSCENARIOTYPE("COMM");
        autoPoHeader.setREQTYPE("UPDATE");
        autoPoHeader.setPOTYPE(CommonConstants.EMPTY);
        if (StringUtils.isNotBlank(scServiceDetail.getOrderSubCategory())
                && !scServiceDetail.getOrderSubCategory().equalsIgnoreCase(CommonConstants.NEW))
            autoPoHeader.setRECNNR((Objects.nonNull(scOrder.getOrderCategory())
                    && !scOrder.getOrderCategory().equalsIgnoreCase("ADD_SITE") && stg0SapPoDtlOptimus != null
                    && stg0SapPoDtlOptimus.getPoNumber() != null) ? stg0SapPoDtlOptimus.getPoNumber()
                    : CommonConstants.EMPTY);
        else
            autoPoHeader.setRECNNR(CommonConstants.EMPTY);
        autoPoHeader.setCOMPANYCODE("1000");
        String vendorCode = scServiceAttributes.containsKey("vendor_id") ? scServiceAttributes.get("vendor_id")
                : CommonConstants.EMPTY;
        autoPoHeader.setVENDORID(vendorCode);
        autoPoHeader.setCURRENCY(
                scOrderAttributes.containsKey("Billing Currency") ? scOrderAttributes.get("Billing Currency")
                        : CommonConstants.EMPTY);
        autoPoHeader.setBUYER(vendorCode.equals(CommonConstants.EMPTY) ? CommonConstants.EMPTY
                : (sapBuyerMap.containsKey(vendorCode) ? sapBuyerMap.get(vendorCode) : CommonConstants.EMPTY));
        String operatorName = (scServiceAttributes.containsKey("closest_provider_bso_name")
                ? scServiceAttributes.get("closest_provider_bso_name")
                : (scServiceAttributes.containsKey("vendor_name") ? scServiceAttributes.get("vendor_name")
                : CommonConstants.EMPTY));

        autoPoHeader.setCPEOWNERS(Objects.nonNull(operatorName) && (operatorName.equalsIgnoreCase("RADWIN WITH TTSL BH")
                || operatorName.equalsIgnoreCase("RADWIN WITH TTML BH")) ? ("BH") : ("NOT REQUIRED"));

        autoPoHeader.setCOPFID(scOrder.getUuid());
        autoPoHeader.setServiceID(scServiceDetail.getUuid());
        autoPoHeader.setServiceType(CommonConstants.EMPTY);
        autoPoHeader.setCustomerName(scServiceDetail.getScOrder().getErfCustCustomerName());
        autoPoHeader.setProductComponent(CommonConstants.EMPTY);
        autoPoHeader.setPOCategory(CommonConstants.EMPTY);
        autoPoHeader
                .setCircuitType(scServiceDetail.getErfPrdCatalogProductName() != null
                        ? scServiceDetail.getErfPrdCatalogProductName().equals("IAS") ? "ILL"
                        : scServiceDetail.getErfPrdCatalogProductName()
                        : CommonConstants.EMPTY);
        String feasibilityId = scServiceAttributes.containsKey("feasibility_response_id")
                ? scServiceAttributes.get("feasibility_response_id")
                : CommonConstants.EMPTY;
        if (StringUtils.isBlank(feasibilityId))
            feasibilityId = scServiceAttributes.containsKey("task_id") ? scServiceAttributes.get("task_id")
                    : CommonConstants.EMPTY;
        autoPoHeader.setSFDCID(feasibilityId);
        autoPoHeader.setSfdcOppId(scOrder.getUuid());
        autoPoHeader.setUNSEZ(CommonConstants.EMPTY); // Supplier Feasibility ID
        autoPoHeader.setTypeOfDeal(CommonConstants.EMPTY);
        autoPoHeader.setPONumber(CommonConstants.EMPTY);
        autoPoHeader.setSiteA(CommonConstants.EMPTY);
        autoPoHeader.setSiteB(CommonConstants.EMPTY);
        autoPoHeader.setInterfaceType(CommonConstants.EMPTY);
        autoPoHeader.setBANDWIDTHSPEED(CommonConstants.EMPTY);
        autoPoHeader.setBandwidthType(CommonConstants.EMPTY);
        autoPoHeader.setMULTIPLEPO(CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR1(
                scComponentAttributes.containsKey("siteAddress") ? scComponentAttributes.get("siteAddress")
                        : CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setAENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNAME(scComponentAttributes.containsKey("localItContactName")
                ? scComponentAttributes.get("localItContactName")
                : CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNUMBER(scComponentAttributes.containsKey("localItContactMobile")
                ? scComponentAttributes.get("localItContactMobile")
                : CommonConstants.EMPTY);
        autoPoHeader.setAENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setAENDZIP(CommonConstants.EMPTY);
        autoPoHeader.setBENDADDR1(
                (scServiceAttributes.containsKey("tcl_pop_address") ? scServiceAttributes.get("tcl_pop_address")
                        : CommonConstants.EMPTY));
        autoPoHeader.setBENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setBENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNAME(scComponentAttributes.containsKey("localItContactName")
                ? scComponentAttributes.get("localItContactName")
                : CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNUMBER(scComponentAttributes.containsKey("localItContactMobile")
                ? scComponentAttributes.get("localItContactMobile")
                : CommonConstants.EMPTY);
        autoPoHeader.setBENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setBENDZIP(CommonConstants.EMPTY);
        autoPoMsgs.setHeaderDetails(autoPoHeader);
    }

    private void constructHanaAutoPoLineItemsUpdate(OPTIMUSREFXINDIAASPACCESS autoPoMsgs,
                                                    Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                    ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                    Map<String, String> scServiceAttributes, Map<String, String> orderSubCategoryMap,
                                                    Map<String, String> sapBuyerMap) {
        LOGGER.info("Inside constructHanaAutoPoLineItemsUpdate");
        LineItems autoPoLineItems = new LineItems();
        autoPoLineItems.setTERMINATIONTYPE(CommonConstants.EMPTY);
        autoPoLineItems.setFROMEND(CommonConstants.EMPTY);
        autoPoLineItems.setTOEND(CommonConstants.EMPTY);
        autoPoLineItems.setCHARGEABLEDISTANCE(CommonConstants.EMPTY);
        autoPoLineItems.setVENDORREFID(
                scComponentAttributes.containsKey("bsoCircuitId") ? scComponentAttributes.get("bsoCircuitId")
                        : CommonConstants.EMPTY);
        autoPoLineItems.setOPERATORNAME(CommonConstants.EMPTY);
        try {
            autoPoLineItems.setCOMMISSIONDATE(scComponentAttributes.containsKey("offnetSupplierBillStartDate")
                    ? formatter.format(commonFulfillmentUtils
                    .getDate(scComponentAttributes.get("offnetSupplierBillStartDate"), formatterCdate))
                    : CommonConstants.EMPTY);
        } catch (Exception e) {
            LOGGER.info("Error on parsing the date");
        }
        autoPoLineItems.setTERMINATIONDATE(CommonConstants.EMPTY); // Customer termination date in case of MACD
        autoPoLineItems.setCrfsDate(CommonConstants.EMPTY);
        autoPoLineItems.setRfsDate(CommonConstants.EMPTY);

        List<ServiceItems> serviceItems = new ArrayList<>();
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")
                && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
            constructHanaAutoPoServiceItemsNrcUpdate(serviceItems, scOrderAttributes, scComponentAttributes,
                    scServiceDetail, scOrder, scContractInfo, scServiceAttributes);
        }
        constructHanaAutoPoServiceItemsArcUpdate(serviceItems, scOrderAttributes, scComponentAttributes,
                scServiceDetail, scOrder, scContractInfo, scServiceAttributes);
        if ((scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL"))) {
            constructHanaAutoPoServiceItemsArcModemUpdate(serviceItems, scOrderAttributes, scComponentAttributes,
                    scServiceDetail, scOrder, scContractInfo, scServiceAttributes);
        }
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL"))) {
            constructHanaAutoPoServiceItemsOtcModemUpdate(serviceItems, scOrderAttributes, scComponentAttributes,
                    scServiceDetail, scOrder, scContractInfo, scServiceAttributes);
        }
        autoPoLineItems.setServiceItems(serviceItems);
        autoPoMsgs.setLineItems(autoPoLineItems);
    }

    private void constructHanaAutoPoServiceItemsNrcUpdate(List<ServiceItems> serviceItems,
                                                          Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                          ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                          Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructAutoPoServiceItems");
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_nrc_bw_prov_ofrf")
                && !scServiceAttributes.get("lm_nrc_bw_prov_ofrf").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetRF"))
                || (scServiceAttributes.containsKey("lm_otc_nrc_installation_offwl")
                && !scServiceAttributes.get("lm_otc_nrc_installation_offwl").equals("0")
                && StringUtils.isNotBlank(lmType) && lmType.equalsIgnoreCase("OffnetWL"))) {
            ServiceItems autoPoServiceItem = new ServiceItems();
            autoPoServiceItem.setBASEUOM(CommonConstants.EMPTY);
            autoPoServiceItem.setQUANTITY(CommonConstants.EMPTY);
            autoPoServiceItem.setGROSSPRICE(CommonConstants.EMPTY);
            autoPoServiceItem.setSERVICENUMBER(CommonConstants.EMPTY);
            serviceItems.add(autoPoServiceItem);
        }
    }

    private void constructHanaAutoPoServiceItemsArcUpdate(List<ServiceItems> serviceItems,
                                                          Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                          ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                          Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructHanaAutoPoServiceItemsArcUpdate");
        if (scServiceAttributes.containsKey("lm_arc_bw_prov_ofrf")
                || scServiceAttributes.containsKey("lm_arc_bw_offwl")) {
            ServiceItems autoPoServiceItems = new ServiceItems();
            autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
            autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
            autoPoServiceItems.setGROSSPRICE(CommonConstants.EMPTY);
            autoPoServiceItems.setSERVICENUMBER(CommonConstants.EMPTY);
            serviceItems.add(autoPoServiceItems);
        }
    }

    private void constructHanaAutoPoServiceItemsArcModemUpdate(List<ServiceItems> serviceItems,
                                                               Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                               ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                               Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructHanaAutoPoServiceItemsArcModemUpdate");
        String lmType = scComponentAttributes.get("lmType");
        if (scServiceAttributes.containsKey("lm_arc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_arc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL")) {
            ServiceItems autoPoServiceItems = new ServiceItems();
            autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
            autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
            autoPoServiceItems.setGROSSPRICE(CommonConstants.EMPTY);
            autoPoServiceItems.setSERVICENUMBER(CommonConstants.EMPTY);
            serviceItems.add(autoPoServiceItems);
        }
    }

    private void constructHanaAutoPoServiceItemsOtcModemUpdate(List<ServiceItems> serviceItems,
                                                               Map<String, String> scOrderAttributes, Map<String, String> scComponentAttributes,
                                                               ScServiceDetail scServiceDetail, ScOrder scOrder, ScContractInfo scContractInfo,
                                                               Map<String, String> scServiceAttributes) {
        LOGGER.info("Inside constructHanaAutoPoServiceItemsOtcModemUpdate");
        String lmType = scComponentAttributes.get("lmType");
        if ((scServiceAttributes.containsKey("lm_otc_modem_charges_offwl")
                && !scServiceAttributes.get("lm_otc_modem_charges_offwl").equals("0") && StringUtils.isNotBlank(lmType)
                && lmType.equalsIgnoreCase("OffnetWL"))) {
            ServiceItems autoPoServiceItems = new ServiceItems();
            autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
            autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
            autoPoServiceItems.setGROSSPRICE(CommonConstants.EMPTY);
            autoPoServiceItems.setSERVICENUMBER(CommonConstants.EMPTY);
            serviceItems.add(autoPoServiceItems);
        }
    }

    private void contructHanaAutoPoRequestUpdatePrefeasible(LastMileRequest autoPoRequest,
                                                            ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributes, ScOrder scOrder) {
        LOGGER.info("Inside contructHanaAutoPoRequestUpdatePrefeasible");
        OPTIMUSREFXINDIAASPACCESS autoPoMsgs = new OPTIMUSREFXINDIAASPACCESS();
        contructHanaAutoPoHeaderUpdatePrefeasible(autoPoMsgs, scServiceDetail, scComponentAttributes, scOrder);
        constructHanaAutoPoLineItemsUpdatePrefeasible(autoPoMsgs);
        autoPoRequest.setOPTIMUSREFXINDIAASPACCESS(autoPoMsgs);
    }

    private void contructHanaAutoPoHeaderUpdatePrefeasible(OPTIMUSREFXINDIAASPACCESS autoPoMsgs,
                                                           ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributes, ScOrder scOrder) {
        LOGGER.info("Inside contructHanaAutoPoHeaderUpdatePrefeasible");
        HeaderDetails autoPoHeader = new HeaderDetails();
        autoPoHeader.setHEADERTEXT(CommonConstants.EMPTY);
        autoPoHeader.setSCENARIOTYPE("BWCHANGE");
        autoPoHeader.setREQTYPE("UPDATE");
        autoPoHeader.setPOTYPE(CommonConstants.EMPTY);
        autoPoHeader.setRECNNR("60000196");
        autoPoHeader.setCOMPANYCODE("1000");
        autoPoHeader.setVENDORID(CommonConstants.EMPTY);
        autoPoHeader.setCURRENCY(CommonConstants.EMPTY);
        autoPoHeader.setBUYER(CommonConstants.EMPTY);
        autoPoHeader.setCPEOWNERS(CommonConstants.EMPTY);
        autoPoHeader.setCOPFID(scOrder.getUuid());
        autoPoHeader.setServiceID(scServiceDetail.getUuid());
        autoPoHeader.setServiceType(CommonConstants.EMPTY);
        autoPoHeader.setProductComponent(CommonConstants.EMPTY);
        autoPoHeader.setPOCategory(CommonConstants.EMPTY);
        autoPoHeader
                .setCircuitType(scServiceDetail.getErfPrdCatalogProductName() != null
                        ? scServiceDetail.getErfPrdCatalogProductName().equals("IAS") ? "ILL"
                        : scServiceDetail.getErfPrdCatalogProductName()
                        : CommonConstants.EMPTY);
        autoPoHeader.setSFDCID(CommonConstants.EMPTY);
        autoPoHeader.setSfdcOppId(CommonConstants.EMPTY);
        autoPoHeader.setUNSEZ(CommonConstants.EMPTY); // Supplier Feasibility ID
        autoPoHeader.setTypeOfDeal(CommonConstants.EMPTY);
        autoPoHeader.setPONumber(CommonConstants.EMPTY);
        autoPoHeader.setSiteA(CommonConstants.EMPTY);
        autoPoHeader.setSiteB(CommonConstants.EMPTY);
        autoPoHeader.setInterfaceType(CommonConstants.EMPTY);
        autoPoHeader.setBANDWIDTHSPEED(CommonConstants.EMPTY);
        autoPoHeader.setBandwidthType(CommonConstants.EMPTY);
        autoPoHeader.setMULTIPLEPO(CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR1(CommonConstants.EMPTY);
        autoPoHeader.setAENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setAENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNAME(CommonConstants.EMPTY);
        autoPoHeader.setAENDLOCALCONTACTNUMBER(CommonConstants.EMPTY);
        autoPoHeader.setAENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setAENDZIP(CommonConstants.EMPTY);
        autoPoHeader.setBENDADDR1(CommonConstants.EMPTY);
        autoPoHeader.setBENDADDR2(CommonConstants.EMPTY);
        autoPoHeader.setBENDCITY(CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNAME(CommonConstants.EMPTY);
        autoPoHeader.setBENDLOCALCONTACTNUMBER(CommonConstants.EMPTY);
        autoPoHeader.setBENDSTATE(CommonConstants.EMPTY);
        autoPoHeader.setBENDZIP(CommonConstants.EMPTY);
        autoPoMsgs.setHeaderDetails(autoPoHeader);
    }

    private void constructHanaAutoPoLineItemsUpdatePrefeasible(OPTIMUSREFXINDIAASPACCESS autoPoMsgs) {
        LOGGER.info("Inside constructHanaAutoPoLineItemsUpdatePrefeasible");
        LineItems autoPoLineItems = new LineItems();
        autoPoLineItems.setTERMINATIONTYPE(CommonConstants.EMPTY);
        autoPoLineItems.setFROMEND(CommonConstants.EMPTY);
        autoPoLineItems.setTOEND(CommonConstants.EMPTY);
        autoPoLineItems.setCHARGEABLEDISTANCE(CommonConstants.EMPTY);
        autoPoLineItems.setVENDORREFID(CommonConstants.EMPTY);
        autoPoLineItems.setOPERATORNAME(CommonConstants.EMPTY);
        autoPoLineItems.setCOMMISSIONDATE(CommonConstants.EMPTY);
        autoPoLineItems.setTERMINATIONDATE(CommonConstants.EMPTY); // Customer termination date in case of MACD
        autoPoLineItems.setCrfsDate(CommonConstants.EMPTY);
        autoPoLineItems.setRfsDate(CommonConstants.EMPTY);
        List<ServiceItems> serviceItems = new ArrayList<>();
        constructHanaAutoPoServiceItemsUpdatePrefeasible(serviceItems);
        autoPoLineItems.setServiceItems(serviceItems);
        autoPoMsgs.setLineItems(autoPoLineItems);
    }

    private void constructHanaAutoPoServiceItemsUpdatePrefeasible(List<ServiceItems> serviceItems) {
        LOGGER.info("Inside constructHanaAutoPoServiceItemsUpdatePrefeasible");
        ServiceItems autoPoServiceItems = new ServiceItems();
        autoPoServiceItems.setBASEUOM(CommonConstants.EMPTY);
        autoPoServiceItems.setQUANTITY(CommonConstants.EMPTY);
        autoPoServiceItems.setGROSSPRICE(CommonConstants.EMPTY);
        autoPoServiceItems.setSERVICENUMBER(CommonConstants.EMPTY); // Need to send the value if MACD
        serviceItems.add(autoPoServiceItems);
    }

    @Transactional(readOnly = false)
    public boolean processInstallCpe(ScServiceDetail serviceDetail) throws TclCommonException {
        Boolean installCpeSuccess = false;
        try {
            if (serviceDetail != null) {
                List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
                        .findByScServiceDetailIdAndCatagory(serviceDetail.getId(),"Router");
                // construct install cpe request
                if (cpeMaterialRequestDetails != null && !cpeMaterialRequestDetails.isEmpty()) {
                    CpeMaterialRequestDetails materialRequestDetails = cpeMaterialRequestDetails.get(0);
                    LOGGER.info("materialRequestDetails:{}",materialRequestDetails);
                    InstallCpe installCpeRequest = new InstallCpe();
                    InstallCpeGIRequest installCpeGIRequest = new InstallCpeGIRequest();
                    installCpeGIRequest.setOBD(materialRequestDetails.getMinNumber());
                    CircuitDetails circuitDetails = new CircuitDetails();
                    circuitDetails.setSERVICEID(serviceDetail.getUuid());
                    circuitDetails.setCPEOEMSERIALNO(materialRequestDetails.getOemSerialNumber());
                    installCpeGIRequest.setCIRCUITDETAILS(circuitDetails);
                    installCpeRequest.setGOODSISSUE(installCpeGIRequest);
                    LOGGER.info("install cpe GI request:{}",Utils.convertObjectToJson(installCpeRequest));
                    AuditLog inventory = saveDataIntoNetworkInventory(Utils.convertObjectToJson(installCpeRequest),
                            null, serviceDetail.getUuid(), "INSTALL-CPE", null);
                    LOGGER.info("Install cpe request for service code:{} and request:{}", serviceDetail.getUuid(),
                            Utils.convertObjectToJson(installCpeRequest));
                    RestResponse response = restClientService.postWithBasicAuthentication(sapHanaInstallCpeUrl,
                            Utils.convertObjectToJson(installCpeRequest), createCommonHeader(), userName, password);
                    LOGGER.info("install cpe GI response:{}",Utils.convertObjectToJson(response));
                    if (response != null) {
                        inventory.setResponse(response.getData());
                        auditLogRepositoryRepository.save(inventory);
                    }
                    LOGGER.info("Install cpe response :{} for service code:{}", Utils.convertObjectToJson(response),
                            serviceDetail.getUuid());

                    if (response.getStatus() == Status.SUCCESS) {
                        InstallCpeResponseBean installcpeResponse = Utils.convertJsonToObject(response.getData(), InstallCpeResponseBean.class);
                        if (installcpeResponse != null && installcpeResponse.getS4HanaOptimusGIResponse() != null
                                && !installcpeResponse.getS4HanaOptimusGIResponse().getS4HanaOptimusGoodsIssue().isEmpty()) {
                            S4HanaOptimusGoodsIssue goodsIssue = installcpeResponse.getS4HanaOptimusGIResponse().getS4HanaOptimusGoodsIssue().get(0);
                            if(goodsIssue.getStatus()!=null && goodsIssue.getRemark()!=null && goodsIssue.getStatus().equalsIgnoreCase("success") && !goodsIssue.getRemark().toLowerCase().contains("not posted")) {
                                installCpeSuccess = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return installCpeSuccess;
        }
        return installCpeSuccess;
    }




    public void processWbsElementSapWaitAsync(ScServiceDetail scServiceDetail, DelegateExecution execution) {
        LOGGER.info("Inside Process WbsElement Sap Wait Sync");
        Integer cpeOverlayComponentId = (Integer) execution.getVariable("cpeOverlayComponentId");
        String cpeSiScope=(String)execution.getVariable("cpeSiScope");
        if(scServiceDetail != null) {
            LOGGER.info("Inside Process WbsElement Sap Wait Sync for service code {}", scServiceDetail.getUuid());
            try {
                execution.setVariable("wbsStatus", false);
                WbsElementRequest wbsElementRequest = new WbsElementRequest();
                List<OptimusWbsElementRequest> optimusWbsElementRequests = new ArrayList<>();
                OptimusWbsElementRequest optimusWbsElementRequest = new OptimusWbsElementRequest();
                optimusWbsElementRequest.setServiceId(scServiceDetail.getUuid());
                optimusWbsElementRequests.add(optimusWbsElementRequest);
                wbsElementRequest.setOptimusWbsElementRequests(optimusWbsElementRequests);
                LOGGER.info("WbsElement Sap Wait Sync request:{}", Utils.convertObjectToJson(wbsElementRequest));
                RestResponse response = restClientService.postWithBasicAuthentication(wbsElementUrl,
                        Utils.convertObjectToJson(wbsElementRequest), createCommonHeader(), userName, password);
                LOGGER.info("WbsElement Sap Wait Sync response:{}",Utils.convertObjectToJson(response));
                if(response.getData() != null && !response.getData().isEmpty()) {
                    WbsElementResponse wbsElementResponse = Utils.convertJsonToObject(response.getData(), WbsElementResponse.class) ;

                    boolean supportPrNeeded=false;
                    boolean installationPrNeeded = false;

                    if (wbsElementResponse != null && Objects.nonNull(wbsElementResponse.getWbsElements()) && !wbsElementResponse.getWbsElements().isEmpty()) {
                        com.tcl.dias.servicefulfillmentutils.beans.sap.hana.WbsElement wbsElement = wbsElementResponse.getWbsElements().get(0);
                        if (Objects.nonNull(wbsElement)) {
                            if (wbsElement.getServiceId() != null && !wbsElement.getServiceId().isEmpty()) {
                                LOGGER.info("serviceId from processWbsResponse:{}", wbsElement.getServiceId());
                                if(scServiceDetail!=null) {

                                    if (cpeSiScope != null) {
                                        if (cpeSiScope.toLowerCase().contains("installation")) {
                                            installationPrNeeded = true;
                                        }
                                        if (cpeSiScope.toLowerCase().contains("support")) {
                                            supportPrNeeded = true;
                                        }
                                    }

                                    ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
                                            .findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "CPE");

                                    String cpeType = null;

                                    if (scServiceAttribute != null) {
                                        cpeType = scServiceAttribute.getAttributeValue();
                                    }

                                    if (Objects.nonNull(execution)) {
                                        if ((wbsElement.getWbsElementCapex() != null && !wbsElement.getWbsElementCapex().isEmpty())
                                                || (cpeType != null && cpeType.contains("Outright")
                                                && wbsElement.getWbsElementOpex() != null
                                                && !wbsElement.getWbsElementOpex().isEmpty())) {
                                            LOGGER.info("level5Wbs:{}", wbsElement.getWbsElementCapex());
                                            execution.setVariable("wbsRequestNeeded", false);
                                            execution.setVariable("wbsStatus", true);
                                            execution.setVariable("supportPrNeeded", supportPrNeeded);
                                            execution.setVariable("installationPrNeeded", installationPrNeeded);
                                        } else {
                                            LOGGER.info("level5Wbs is empty");
                                            execution.setVariable(execution.getId(), "wbsStatus", false);
                                            execution.setVariable(execution.getId(), "supportPrNeeded", supportPrNeeded);
                                            execution.setVariable(execution.getId(), "installationPrNeeded", installationPrNeeded);
                                        }
                                    }
                                    Optional<ScComponent> scComponentOptional = scComponentRepository.findById(cpeOverlayComponentId);
                                    if (scComponentOptional.isPresent()) {
                                        Map<String, String> mapper = new HashMap<>();

                                        if (cpeType != null && cpeType.toLowerCase().contains("Outright")) {
                                            mapper.put("level5Wbs", wbsElement.getWbsElementOpex());
                                            mapper.put("wbsElementOpex", wbsElement.getWbsElementOpex());
                                        } else {
                                            mapper.put("level5Wbs", wbsElement.getWbsElementCapex());
                                            mapper.put("wbsElementOpex", wbsElement.getWbsElementOpex());
                                        }

                                        componentAndAttributeService.updateAttributesByScComponent(scServiceDetail.getId(), mapper, cpeOverlayComponentId);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error while trigger WbsElement Sap Wait sync for service code {} and error {}", scServiceDetail.getUuid(), e);
                execution.setVariable("wbsStatus", false);
            }
        }


    }
}
