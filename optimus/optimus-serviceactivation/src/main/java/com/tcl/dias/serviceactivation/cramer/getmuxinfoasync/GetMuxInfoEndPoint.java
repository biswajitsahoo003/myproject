package com.tcl.dias.serviceactivation.cramer.getmuxinfoasync;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.tcl.dias.common.beans.MuxDetailsItem;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.Acknowledgement;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.GetMuxInfo;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.GetMuxInfoAck;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.MuxDetail;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.SubmitMuxDetailFailureResponse;
import com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.SubmitMuxDetailFailureResponseAck;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Endpoint
public class GetMuxInfoEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(GetMuxInfoEndPoint.class);

    @Autowired
    NetworkInventoryRepository networkInventoryRepository;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    @Autowired
    ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

    //return getMuxInfoAck
    @PayloadRoot(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", localPart = "getMuxInfo")
    @ResponsePayload
    public GetMuxInfoAck processMuxAsyncRequest(@RequestPayload GetMuxInfo request) {
        logger.info("processMuxAsyncRequest started {} ", request);
        Acknowledgement ack = new Acknowledgement();
        GetMuxInfoAck getMuxInfoAck = new GetMuxInfoAck();
        getMuxInfoAck.setAcknowledgement(ack);

        NetworkInventory networkInventory = new NetworkInventory();
        try {
            // Save request & response
            networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
            networkInventory.setServiceCode(StringUtils.trimToEmpty(request.getServiceId()));
            networkInventory.setRequestId(StringUtils.trimToEmpty(request.getRequestId()));
            networkInventory.setType(CramerConstants.GET_MUX_INFO_ASYNC);

            triggerWorkflow(request);
            if (Objects.nonNull(request.getRequestId())) {
                ack.setStatus(true);
            } else {
                ack.setStatus(false);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ack.setStatus(false);
            ack.setErrorCode("500");
            ack.setErrorMessage(e.getMessage());
        }
        try {
            networkInventory.setRequest(Utils.convertObjectToXmlString(request, GetMuxInfo.class));
            networkInventory.setResponse(Utils.convertObjectToXmlString(getMuxInfoAck, GetMuxInfoAck.class));
            networkInventoryRepository.save(networkInventory);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return getMuxInfoAck;
    }

    private void triggerWorkflow(GetMuxInfo getMuxInfo) {
        String response = "false";
        String processInstanceId = "";
        try {
            logger.info("GetMuxInfo {} ", getMuxInfo);
            processInstanceId = getMuxInfo.getRequestId();
            if (Objects.nonNull(processInstanceId)) {
                Thread.sleep(10000);
                String[] instanceId = processInstanceId.split("#");
                processInstanceId = instanceId[1];
                String nodeName = instanceId[2];
                Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
                        .activityId("get-mux-info-async").singleResult();
                if (execution != null) {
                    if (!CollectionUtils.isEmpty(getMuxInfo.getAEndMuxDetails())) {
                    	
                    	Optional<ScServiceDetail> serviceDetail = Optional.ofNullable(scServiceDetailRepository.findByUuidAndMstStatus_code(getMuxInfo.getServiceId(),"INPROGRESS"));

                        runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", false);

                        Map<Boolean, List<MuxDetail>> checkIfNodeIsReady = checkIfNodeIsReady(nodeName, getMuxInfo);
                        String errorMessage=null;
                        if (!CollectionUtils.isEmpty(checkIfNodeIsReady.get(true))) {
                            runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", true);
                            runtimeService.setVariable(execution.getId(), "lmConnectionTypeChange", false);
                            logger.info(nodeName + " is ready");

                        }else if (!CollectionUtils.isEmpty(checkIfNodeIsReady.get(false))) {
                            errorMessage= nodeName+" is not available";
                            String otherAvailableNodes = "";

                            try {
                                List<MuxDetail> muxDetails = checkIfNodeIsReady.get(false);
                                otherAvailableNodes = Utils.convertObjectToJson(constructMuxDetails(muxDetails));
                                saveErrorReposeIfNodeAvailable(serviceDetail,errorMessage);
                                saveOtherNodeAvailable(serviceDetail, otherAvailableNodes);
                            } catch (Exception e) {
                                logger.error(String.format("Exception occurred in while converting repose or saving saveErrorReposeIfNodeAvailable to json in trigger workflow for processMuxAsyncRequest"));
                            }
                            logger.info(String.format(" %s is not ready but other available node are %s", nodeName, otherAvailableNodes));
                            runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", false);
                        }
                            
                        logger.info("mux provision status is not ready {}", getMuxInfo.getAEndMuxDetails());
                        runtimeService.trigger(execution.getId());
                    }
                } else {
                    logger.info("Execution is null for {} ", processInstanceId);
                }
                response = "true";
            } else {
                logger.info("Request ID id null for GetMuxInfo::triggerWorkflow");
            }
            logger.info("muxInfoAsyncListener response to network inventory service {} ", response);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private  List<MuxDetailsItem> constructMuxDetails(List<MuxDetail> muxDetails) {
        List<MuxDetailsItem> muxDetailsItems = new ArrayList<>();
        muxDetails.forEach(muxDetail -> {
            MuxDetailsItem muxDetailsItem = new MuxDetailsItem();
            muxDetailsItem.setMux(muxDetail.getMuxName());
            muxDetailsItem.setMuxIp(muxDetail.getMuxIP());
            muxDetailsItem.setMuxPort("");
            muxDetailsItems.add(muxDetailsItem);
        });
        return  muxDetailsItems;
    }

    private void saveOtherNodeAvailable(Optional<ScServiceDetail> scServiceDetail, String otherAvailaibleNodes) {
        componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
                "mux_details",
                otherAvailaibleNodes,
                AttributeConstants.OTHER_AVAILABLE_NODES, "select-mux");
    }

    private void saveErrorReposeIfNodeAvailable(Optional<ScServiceDetail> scServiceDetail, String errorMessage) throws TclCommonException {
        componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
                "muxInfoErrorMessage",
                componentAndAttributeService.getErrorMessageDetails(errorMessage,"500"),
                AttributeConstants.ERROR_MESSAGE, "select-mux");
    }


    private Map<Boolean, List<MuxDetail>> checkIfNodeIsReady(String nodeName, GetMuxInfo getMuxInfo) {
        return getMuxInfo.getAEndMuxDetails()
                .stream()
                .collect(Collectors.partitioningBy(muxDetail -> nodeName.equalsIgnoreCase(muxDetail.getMuxName())
                        && "Ready - In-Service".equalsIgnoreCase(muxDetail.getMuxprovisionStatus())));
    }

    @PayloadRoot(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", localPart = "submitMuxDetailFailureResponse")
    @ResponsePayload
    public SubmitMuxDetailFailureResponseAck processSubmitMuxDetailFailureResponse(@RequestPayload SubmitMuxDetailFailureResponse request) {
        logger.info("processSubmitMuxDetailFailureResponse started {} ", request);
        SubmitMuxDetailFailureResponseAck submitMuxDetailFailureResponseAck = new SubmitMuxDetailFailureResponseAck();
        Acknowledgement ack = new Acknowledgement();
        NetworkInventory networkInventory = new NetworkInventory();
        try {
            // Save request & response
            networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
            networkInventory.setServiceCode(StringUtils.trimToEmpty(request.getServiceId()));
            networkInventory.setRequestId(StringUtils.trimToEmpty(request.getRequestId()));
            networkInventory.setType(CramerConstants.GET_MUX_INFO_ASYNC);
            triggerWorkflow(request);
            if (Objects.nonNull(request.getRequestId())) {
                ack.setStatus(true);
            } else {
                ack.setStatus(false);
                ack.setErrorMessage(request.getFailure().getErrorLongDescription());
                ack.setErrorCode(request.getFailure().getErrorCode());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ack.setStatus(false);
            ack.setErrorCode("000000");
            ack.setErrorMessage("Exception");
        }
        submitMuxDetailFailureResponseAck.setAcknowledgement(ack);
        try {
            networkInventory.setRequest(Utils.convertObjectToXmlString(request, SubmitMuxDetailFailureResponse.class));
            networkInventory.setResponse(Utils.convertObjectToXmlString(submitMuxDetailFailureResponseAck, SubmitMuxDetailFailureResponseAck.class));
            networkInventoryRepository.save(networkInventory);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return submitMuxDetailFailureResponseAck;
    }

    private String triggerWorkflow(SubmitMuxDetailFailureResponse submitMuxDetailFailureResponse) {
        String response = "false";
        try {
            if (Objects.nonNull(submitMuxDetailFailureResponse.getRequestId())) {
                Thread.sleep(10000);
                String reqId = submitMuxDetailFailureResponse.getRequestId();
                String[] instanceId = reqId.split("#");
                String processInstanceId = instanceId[1];
                if (Objects.nonNull(processInstanceId)) {


                    Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).activityId("get_mux_info_async").singleResult();
                    if (execution != null) {
                        runtimeService.setVariable(execution.getId(), "muxInfoErrorMessage", submitMuxDetailFailureResponse.getFailure().getErrorLongDescription());
                        runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", false);
                        runtimeService.trigger(execution.getId());
                        logger.info("MuxInfoAsyncFailureListener triggered the process successfully  {} ", execution.getId());
                    } else {
                        logger.info("Execution is null for {} ", processInstanceId);
                    }
                    response = "true";
                } else {
                    logger.info("processId is empty! Response {}", submitMuxDetailFailureResponse);
                }
            }
            try {
                ScServiceDetail scServiceDetail = scServiceDetailRepository
                        .findByUuidAndMstStatus_code(submitMuxDetailFailureResponse.getServiceId(),TaskStatusConstants.INPROGRESS);

                logger.info("getErrorMessageDetails condition meet for GetMuxInfoEndPoint with serviceId:{}", submitMuxDetailFailureResponse.getServiceId());


                if (scServiceDetail != null && submitMuxDetailFailureResponse != null && submitMuxDetailFailureResponse.getFailure() != null) {
                    logger.info("getErrorMessageDetails condition meet for GetMuxInfoEndPoint");

                    componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "muxInfoErrorMessage",
                            getErrorMessageDetails(submitMuxDetailFailureResponse), AttributeConstants.ERROR_MESSAGE,
                            "get_mux_info_async");
                }
            } catch (Exception e) {
                logger.info("processCreateClrAsyncRequest error message capture   {} ", e);
            }

            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return response;
        }
    }

    private String getErrorMessageDetails(SubmitMuxDetailFailureResponse submitMuxDetailFailureResponse)
            throws TclCommonException {
        logger.info("getErrorMessageDetails started for netb response");

        ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
        errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

        if (submitMuxDetailFailureResponse != null && submitMuxDetailFailureResponse.getFailure() != null) {

            logger.info("getErrorMessageDetails forund");
            com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans.MuxDetailFailureResponse message = submitMuxDetailFailureResponse
                    .getFailure();
            ErrorBean errorBean = new ErrorBean();
            errorBean.setErrorLongDescription(message.getErrorLongDescription());
            errorBean.setErrorCode(message.getErrorCode());
            errorBean.setErrorShortDescription(message.getErrorShortDescription());
            errorDetailsBean.getErrorDetails().add(errorBean);
        }

        return Utils.convertObjectToJson(errorDetailsBean);

    }

}