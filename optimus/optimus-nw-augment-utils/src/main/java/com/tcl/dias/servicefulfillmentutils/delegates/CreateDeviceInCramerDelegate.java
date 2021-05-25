package com.tcl.dias.servicefulfillmentutils.delegates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.networkaugment.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.networkaugment.entity.repository.NwaEorEquipDetailsRepository;
import com.tcl.dias.networkaugment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillmentutils.beans.*;
import com.tcl.dias.servicefulfillmentutils.service.v1.NetworkAugmentationWorkFlowService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NetworkInventoryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CreateDeviceInCramerDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(CreateDeviceInCramerDelegate.class);

    public static final String CRAMER_ORDER_STATUS_INSTALLED = "Installed";
    public static final String CRAMER_ORDER_STATUS_READY_IN_SERVICE = "Ready - In-Service";

    @Autowired
    NetworkAugmentationWorkFlowService workFlowService;


    @Autowired
    NwaEorEquipDetailsRepository nwaEorEquipDetailsRepository;

    @Autowired
    ScOrderRepository scOrderRepository;

    @Value("${app.cramer.baseUrl}")
    String cramerBaseUrl;

    @Value("${app.createNode.baseUrl}")
    String createNodeBaseUrl;

    @Value("${app.cardAddUrl}")
    private String cardAddUrl;

    @Value("${app.cardstatus}")
    private  String cardStatusUrl;

    private ObjectMapper mapper = new ObjectMapper();

    private static final Map<String,String> cardStatusMap = new HashMap<>();
    static{
        cardStatusMap.put("change_card_status_in_cramer_task","Ready - In-Service");
        cardStatusMap.put("change_card_status_in_cramer_retry_task","Ready - In-Service");
        cardStatusMap.put("eor_tx_pro_cramer_status_RIS_task",CRAMER_ORDER_STATUS_READY_IN_SERVICE);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

        Boolean rejectionFlag = (Boolean) delegateExecution.getVariable("rejection");
        if (rejectionFlag != null && rejectionFlag) {
            delegateExecution.setVariable("isError", true);

            return;
        }else {
            delegateExecution.setVariable("isError", false);
        }
        Task task = workFlowService.processServiceTask(delegateExecution);
        String orderCode = task.getOrderCode();
        String orderType = task.getOrderType();
        String errorMessage = "";
        String taskDefKey = delegateExecution.getCurrentActivityId();
        System.out.println("task def key is : " + taskDefKey);
        switch(taskDefKey) {
            case "create_device_in_cramer_task" :
            case "eor_tx_pro_create_device_crmr_task" :
            case "eor_eth_pro_create_device_crmr_task" :
            case "eor_equip_wl_prov_create_cramer_device_service_task":
            case "eor_equip_wl_prov_create_crmr_device_wf_service_task":
                try{
                    System.out.println("======== Create Device Cramer call is done here.....");
                    CramerResponseBean respStatus = this.doCramerCreateApiCall(orderCode);
                    System.out.println("===== create Cramer Object Response"+respStatus);
                    this.handleCramerResponse(respStatus, "createDevice");

                    System.out.println("=============Create node in cramer is called here");
                    CramerResponseBean respStatus1 = this.doCreateNodeCall(orderCode);
                    System.out.println("===== create Cramer Node Response "+respStatus1);
                    this.handleCramerResponse(respStatus1, "createNode");

                    delegateExecution.setVariable("isError", false);

                }catch (Exception ex) {
                    ex.printStackTrace();
                    delegateExecution.setVariable("isError", true);
                    System.out.println("======== Create Device or Create Node Cramer call got exception....."+ex.getMessage());
                    errorMessage = ex.getMessage();
                }
                break;
            case "update_device_status_in_cramer_task":
            case "eor_tx_pro_cramer_status_installed_task":
            case "eor_eth_pro_cramer_status_installed_task":
                try{
                    ProvisioningStatusResponse resp = this.doCramerUpdateApiCall(orderCode, orderType, CRAMER_ORDER_STATUS_INSTALLED);
                    this.handleProvisioningStatusResponse(resp);
                    delegateExecution.setVariable("isError", false);
                    System.out.println("======== Update Device Cramer call is done here for ....." + delegateExecution.getCurrentActivityId());

                    NwaEorEquipDetails nwaEorEquipDetails = nwaEorEquipDetailsRepository.findByOrderCode(task.getOrderCode());
                    if(nwaEorEquipDetails != null) {
                        String taskKey = task.getMstTaskDef().getKey().toLowerCase();
                        switch (taskKey) {
                            case "eor_tx_pro_cramer_status_installed_task":
                            case "eor_eth_pro_cramer_status_installed_task":
                                if (nwaEorEquipDetails != null && "SDNWNOC_NWA".equalsIgnoreCase(nwaEorEquipDetails.getSdNocSaNoc()) ) {
                                    System.out.println("isSdNoc is true" );
                                    delegateExecution.setVariable("isSdNoc", true);
                                } else {
                                    System.out.println("isSdNoc is false" );
                                    delegateExecution.setVariable("isSdNoc", false);
                                }
                        }
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                    delegateExecution.setVariable("isError", true);
                    System.out.println("======== Update Device Cramer call got exception....."+ex.getMessage());
                    errorMessage = ex.getMessage();
                }
                break;
            case "update_device_status_read_in_service_cramer_task":
            case "change_card_status_in_cramer_task":
            case "eor_ip_card_removal_change_ready_status_task":
            case "eor_tx_card_pro_updqate_inventory_in_crmr_task":
            case "eor_tx_pro_cramer_status_RIS_task" :
            case "eor_eth_card_pro_updqate_inventory_in_crmr_task":
            case "eor_eth_pro_cramer_status_RIS_task" :
            case "eor_wl_ip_alloc_update_crmr_status_wf" :
                try{
                    ProvisioningStatusResponse resp = this.doCramerUpdateApiCall(orderCode, orderType, CRAMER_ORDER_STATUS_READY_IN_SERVICE);
                    this.handleProvisioningStatusResponse(resp);
                    delegateExecution.setVariable("isError", false);
                    System.out.println("== Update Device Status in Cramer call is done here for ....."+delegateExecution.getCurrentActivityId());
                }catch (Exception ex) {
                    ex.printStackTrace();
                    delegateExecution.setVariable("isError", true);
                    System.out.println("======== Update Device Status in Cramer call got exception....."+ex.getMessage());
                    errorMessage = ex.getMessage();
                }
                break;
            case "add_card_in_cramer_task":
            case "eor_tx_card_pro_create_resource_raiseWO1_task":
            case "eor_eth_card_pro_create_resource_raiseWO1_task":
                try{
                    System.out.println("======== Create Device Cramer while add card - call is done here.....");
                    CramerResponseBean respStatus = this.doCramerCreateApiCall(orderCode);
                    System.out.println("===== create Cramer Object while add card - Response is .."+respStatus);
                    this.handleCramerResponse(respStatus, "addCard");

                    ScOrder scOrder = scOrderRepository.findByOpOrderCode(orderCode);
                    Set<NwaEorEquipDetails> nwaEorEquipDetailsSet = scOrder.getNwaEorEquipDetails();
                    String hostName  = "";
                    List<AddCardResponseBean> respList = new ArrayList<>();
                    if(nwaEorEquipDetailsSet!= null) {
                        NwaEorEquipDetails nwaEorEquipDetails = nwaEorEquipDetailsSet.iterator().next();
                        hostName = nwaEorEquipDetails.getEqpmntName();
                    }
                    Set<NwaOrderDetailsExtnd> nwaOrderDetailsExtndSet = scOrder.getNwaOrderDetailsExtnds();
                    if(nwaOrderDetailsExtndSet != null){
                        NwaOrderDetailsExtnd nwaOrderDetailsExtnd = nwaOrderDetailsExtndSet.iterator().next();
                        if (nwaOrderDetailsExtnd != null && "SDNWNOC_NWA".equalsIgnoreCase(nwaOrderDetailsExtnd.getConfigrationManagment())) {
                            System.out.println("isSdNoc is true");
                            delegateExecution.setVariable("isSdNoc", true);
                        } else {
                            System.out.println("isSdNoc is false");
                            delegateExecution.setVariable("isSdNoc", false);
                        }
                    }


                    Set<NwaCardDetails> nwaCardDetails = scOrder.getNwaCardDetails();
                    if(nwaCardDetails != null){
                        for(NwaCardDetails cardDetails:nwaCardDetails){

                            /*Map<String, Object> addCardMap = new HashMap() ;
                            addCardMap.put("cardType", cardDetails.getCardType());
                            addCardMap.put( "id", 0);
                            addCardMap.put("eorId",orderCode);
                            addCardMap.put("name",cardDetails.getCardType());
                            Map<String,String> nodeMap = new HashMap<>();
                            nodeMap.put("hostname",hostName);
                            addCardMap.put("node", nodeMap);
                            addCardMap.put("shelfNumber",cardDetails.getShelfDetails());
                            addCardMap.put("shelfSlotNumber",cardDetails.getShelfSlotDetails());
                            if(cardDetails.getSlotDetails() != null && !cardDetails.getSlotDetails().isEmpty()){
                                addCardMap.put("cardSlotNumber",cardDetails.getSlotDetails());
                                addCardMap.put("subSlotNumber",cardDetails.getSubSlotNo());
                            }
                            System.out.println("Add card request map : "+addCardMap);*/

                            String cardType = cardDetails.getCardType();
                            String name = cardDetails.getCardType();
                            String sNum = cardDetails.getShelfDetails();
                            String ssNum = cardDetails.getShelfSlotDetails();
                            String cSlotNum = cardDetails.getSlotDetails()!=null?cardDetails.getSlotDetails(): "";
                            String cSubSlotNum = cardDetails.getSubSlotNo()!=null?cardDetails.getSubSlotNo(): "";
                            /*String cSlotNum = cardDetails.getSlotDetails();
                            String cSubSlotNum = cardDetails.getSubSlotNo();*/

                            AddCardToCramerBean payload = new AddCardToCramerBean();
                            payload.setCardType(cardType);
                            payload.setName(name);
                            Map<String,String> nodeMap = new HashMap<>();
                            nodeMap.put("hostName",hostName);
                            payload.setNode(nodeMap);
                            payload.setShelfNumber(sNum);
                            payload.setShelfSlotNumber(ssNum);
                            payload.setCardSlotNumber(cSlotNum);
                            payload.setSubSlotNumber(cSubSlotNum);
                            payload.setEorId(orderCode);
                            payload.setId(0);
                            System.out.println("===== addCard Request payLoad "+payload);

                            //AddCardResponseBean resp = this.doCramerCardAddApiCall(payload);
                            AddCardResponseBean resp = this.doCramerCardAddApiCall(payload);
                            System.out.println("===== addCard Response payLoad  "+resp);

                            if(resp==null) {
                                throw new RuntimeException("Cannot deal will null add card response");
                            }
                            if(StringUtils.isEmpty(resp.getId()) || resp.getId() == null ){
                                throw new RuntimeException("Id in add card response is null for payload: "+payload);
                            }
                            if("0".equals(resp.getId().trim())) {
                                throw new RuntimeException("Id in add card response is 0 for payload: "+payload);
                            }
                            respList.add(resp);
                            System.out.println("=== id from the response is "+resp.getId());
                            System.out.println(" first response from response List is "+respList.get(0));
                            if(resp.getId() == null){
                                delegateExecution.setVariable("isError", true);
                            }else {
                                delegateExecution.setVariable("isError", false);
                            }
                            System.out.println("===== addCard Request payLoad "+respList);
                        }
                    }else {
                        System.out.println(" CardDetails is null ");
                        delegateExecution.setVariable("isError", true);
                    }
                    System.out.println("======== Add card Response in Cramer call is done here....."+respList);
                }catch (Exception ex) {
                    ex.printStackTrace();
                    delegateExecution.setVariable("isError", true);
                    System.out.println("======== Add card response in Cramer call got exception....."+ex.getMessage());
                }
               // delegateExecution.setVariable("isError", false);

                break;
            case "eor_ip_card_rmvl_rmv_card_port_in_cramer_task":
                //TODO remove card service task.
                break;
            case "eor_ip_card_removal_check_and_verify_task":
                //TODO cehck and very cramer taks.
                break;
            default:
                throw new TclCommonRuntimeException("Dont know how to process cramer task: "+delegateExecution.getCurrentActivityId());

        }

        workFlowService.processServiceTaskCompletion(delegateExecution ,errorMessage);
    }

    private void handleProvisioningStatusResponse(ProvisioningStatusResponse resp) {
        System.out.println(" Response is ....." + resp);
        if(resp==null) {
            throw new RuntimeException("Cannot deal with null provisioning response");
        }
        if(StringUtils.isEmpty(resp.getOutput())){
            throw new RuntimeException("Error occurred: "+resp.getErrorCode()+", "+resp.getErrorShortDescription()+", "+resp.getErrorLongDescription());
        }

        boolean output = Boolean.parseBoolean(resp.getOutput().toLowerCase());
        if(!output) {
            throw new RuntimeException("Error occurred, output is not true: "+resp.getErrorCode()+", "+resp.getErrorShortDescription()+", "+resp.getErrorLongDescription());
        }
    }

    private void handleCramerResponse(CramerResponseBean respStatus, String cramerCallType) {
        System.out.println("Error Code from Response :"+respStatus.getErrorCode());
        switch(cramerCallType) {
            case "createDevice":

                if(respStatus.getErrorCode() != null) {
                    //throw new RuntimeException("Cannot handle null response.");
                    throw new RuntimeException("Cramer api for create object failed::"+respStatus.getErrorCode()+", "+respStatus.getErrorShortDescription()+", "+respStatus.getErrorLongDescription());
                }

                /*if(StringUtils.isEmpty(respStatus.getStatus())) {
                    throw new RuntimeException("Cramer api for create object failed::"+respStatus.getErrorCode()+", "+respStatus.getErrorShortDescription()+", "+respStatus.getErrorLongDescription());
                }*/
            case "createNode" :
                if(respStatus.getErrorCode() != null) {
                    //throw new RuntimeException("Cannot handle null response.");
                    throw new RuntimeException("Cramer api for create node failed::"+respStatus.getErrorCode()+", "+respStatus.getErrorShortDescription()+", "+respStatus.getErrorLongDescription());
                }

                /*if(StringUtils.isEmpty(respStatus.getStatus())) {
                    throw new RuntimeException("Cramer api for create node failed::"+respStatus.getErrorCode()+", "+respStatus.getErrorShortDescription()+", "+respStatus.getErrorLongDescription());
                }*/
            case "addCard" :
                if(respStatus.getErrorCode() != null) {
                    //throw new RuntimeException("Cannot handle null response.");
                    throw new RuntimeException("Cramer api for add card failed::"+respStatus.getErrorCode()+", "+respStatus.getErrorShortDescription()+", "+respStatus.getErrorLongDescription());
                }

                /*if(StringUtils.isEmpty(respStatus.getStatus())) {
                    throw new RuntimeException("Cramer api for add card failed::"+respStatus.getErrorCode()+", "+respStatus.getErrorShortDescription()+", "+respStatus.getErrorLongDescription());
                }*/
                break;
        }

    }

    /**
     * http://uswv1vusp002a.vsnl.co.in:8102/CWSInventoryCreator4Eor/rest/node/createNode
     *
     * Request -
     *
     * {
     * "eorId": "EOR_677454",
     * "hostName": "NODE_677454",
     * "make": "ALCATEL IP",
     * "model": "ALU7750 SR-12",
     * "owner": "VSNL",
     * "inventoryOwner" : "TCL",
     * "nodeRole" : "HUB",
     * "coverage": "Access",
     * "location": {
     * "building": "CMB"
     * }
     * }
     * @param orderCode
     */
    private CramerResponseBean doCreateNodeCall(String orderCode) {
        RestTemplate restTemplate = getRestTemplate();
        String createNodeUrl = String.format("%s/node/createNode",createNodeBaseUrl) ;
        Object payLoad = this.resolvePayload(orderCode);
        System.out.println("=========Create node payload ... "+payLoad);
        HttpEntity<Object> requestEntity = new HttpEntity<>(payLoad);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<CramerResponseBean> responseEntity = restTemplate.exchange(createNodeUrl, HttpMethod.POST, requestEntity, CramerResponseBean.class);
        System.out.println("======= Create node cramer call ..."+createNodeUrl);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("Error in apiCall call");
        }
        return responseEntity.getBody();
    }

    private Object resolvePayload(String orderCode) {
        NwaEorEquipDetails nwa = nwaEorEquipDetailsRepository.findByOrderCode(orderCode);
        Map<String, Object> payload = new HashMap<>();
        payload.put("eorId", orderCode);
        payload.put("hostName", nwa.getEqpmntName());
        payload.put("make",nwa.getDeviceType());
        payload.put("model", nwa.getModel());
        payload.put("owner",nwa.getNodeOwner());
        payload.put("invetoryOwner", "TCL");
        payload.put("nodeRole", "HUB");
        payload.put("coverage", nwa.getCoverage());
        Map<String, String> bldMap = new HashMap<>();
        if(StringUtils.isNotEmpty(nwa.getBldg())) {
            if ("others".equalsIgnoreCase(nwa.getBldg())) {
                bldMap.put("building", nwa.getBldgOther());
                //payload.put("location", nwa.getBldgOther());
            } else {
                bldMap.put("building", nwa.getBldg());
                // payload.put("location", nwa.getBldg());
            }
        }
        payload.put("location",bldMap);


        return payload;
    }


    /**
     * Endpoint:
     * http://uswv1vusp002a.vsnl.co.in:8102/CWSInventoryCreator4Eor/rest/object/setProvisionStatus
     *
     *
     * Request Body:
     * {
     * "orderId" : "EOR_DUM_000000",
     * "orderType" : "EOR",
     * "provisioningStatus" : "Installed"
     * }
     *
     * Success Response:
     * {
     * "orderId": "EOR_DUM_000000",
     * "orderType": "EOR",
     * "provisioningStatus": "Installed",
     * "output": "TRUE"
     * }
     *
     * Failure Response:
     * {
     * "errorCode": "EOR Not found in Cramer or has not assoicated objects",
     * "errorShortDescription": "EOR Not found in Cramer or has not assoicated objects",
     * "errorLongDescription": "EOR Not found in Cramer or has not assoicated objects",
     * "module": null
     * }
     */
/*RestTemplate restTemplate = new RestTemplate();
HttpHeaders headers = new HttpHeaders();
headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

HttpEntity<String> entity = new HttpEntity<>("body", headers);

restTemplate.exchange(url, HttpMethod.POST, entity, String.class);*/

    private ProvisioningStatusResponse doCramerUpdateApiCall(String orderCode,String orderType, String provisioningStatus) {
        ProvisioningStatusRequest payLoad = new ProvisioningStatusRequest();
        payLoad.setOrderId(orderCode);
        payLoad.setOrderType(orderType);
        payLoad.setProvisioningStatus(provisioningStatus);
        RestTemplate restTemplate = getRestTemplate();
        String createNodeUrl = String.format("%s/object/setProvisionStatus",createNodeBaseUrl) ;
        System.out.println("=========doSetStatusApiCall payload ... ("+orderCode+", "+createNodeUrl);
        HttpEntity<Object> requestEntity = new HttpEntity<>(payLoad);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProvisioningStatusResponse> responseEntity = restTemplate.exchange(createNodeUrl, HttpMethod.POST,
                requestEntity, ProvisioningStatusResponse.class);
        System.out.println("======= doSetStatusApiCall cramer call ..."+createNodeUrl);
        ProvisioningStatusResponse respBody = responseEntity.getBody();
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException(respBody.getErrorCode()+", "+respBody.getErrorShortDescription()+", "+respBody.getErrorLongDescription());
        }

        return respBody;

    }

    private ProvisioningStatusResponse doCramerCardUpdateApiCall(String orderCode,String orderType, String provisioningStatus) {
        ProvisioningStatusRequest payLoad = new ProvisioningStatusRequest();
        payLoad.setOrderId(orderCode);
        payLoad.setOrderType(orderType);
        payLoad.setProvisioningStatus(provisioningStatus);
        RestTemplate restTemplate = getRestTemplate();

        System.out.println("=========doSetCardStatusApiCall payload ... "+payLoad+", "+cardStatusUrl);
        HttpEntity<Object> requestEntity = new HttpEntity<>(payLoad);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProvisioningStatusResponse> responseEntity = restTemplate.exchange(cardStatusUrl, HttpMethod.POST,
                requestEntity, ProvisioningStatusResponse.class);
        System.out.println("======= doSetCardStatusApiCall cramer call ..."+cardStatusUrl);
        ProvisioningStatusResponse respBody = responseEntity.getBody();
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException(respBody.getErrorCode()+", "+respBody.getErrorShortDescription()+", "+respBody.getErrorLongDescription());
        }

        return respBody;

    }

    private CramerResponseBean doCramerCreateApiCall(String orderCode) {
        String cramerUrl = String.format("%s/CreateObject?orderId=%s&orderType=EOR&technology=TF",cramerBaseUrl,orderCode) ;
        System.out.println("======Cramer CreateObject call "+cramerUrl);
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(cramerUrl, HttpMethod.GET, null, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("Error in Cramer call::"+cramerUrl);
        }
        String respStr = responseEntity.getBody();
        System.out.println("======Cramer CreateObject respStr: "+respStr);
        CramerResponseBean cramerRespBean = null;
        if(StringUtils.isNotEmpty(respStr)) {
            try {
                cramerRespBean = mapper.readValue(respStr, CramerResponseBean.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Error response string from creamer create is empty for "+orderCode+", "+cramerUrl);
        }
        return cramerRespBean;
    }

    private AddCardResponseBean doCramerCardAddApiCall(AddCardToCramerBean payLoad) {
        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(payLoad,headers);

        System.out.println("===== api call "+cardAddUrl);
        ResponseEntity<AddCardResponseBean> responseEntity = restTemplate.exchange(cardAddUrl, HttpMethod.POST, requestEntity, AddCardResponseBean.class);
        System.out.println("======= Create add card cramer call ..."+cardAddUrl);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("Error in apiCall call");
        }
        return responseEntity.getBody();
    }

    private RestTemplate getRestTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        return restTemplateBuilder.build();
    }
}