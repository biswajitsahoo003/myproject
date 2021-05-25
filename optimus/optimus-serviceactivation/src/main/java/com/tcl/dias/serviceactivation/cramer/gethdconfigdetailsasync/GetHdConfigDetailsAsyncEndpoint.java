package com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.Acknowledgement;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.GetHDCONFIGDetails;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.GetHDCONFIGDetailsResponse;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.HDCONFIGDetails;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.HDCONFIGDetailsAcknowledgement;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.HDCONFIGFailureResponse;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.SubmitHDCONFIGFailureResponse;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.SubmitHDCONFIGFailureResponse2;
import com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.SubmitHDCONFIGFailureResponseResponse;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Endpoint
public class GetHdConfigDetailsAsyncEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(GetHdConfigDetailsAsyncEndpoint.class);

    @Autowired
    MQUtils mqUtils;

    @Autowired
    NetworkInventoryRepository networkInventoryRepository;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;

    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @PayloadRoot(namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails", localPart = "getHDCONFIGDetails")
    @ResponsePayload
    public GetHDCONFIGDetailsResponse processGetHdConfigDetailsRequest(@RequestPayload GetHDCONFIGDetails getHDCONFIGDetails)
            throws TclCommonException {
        logger.info("processGetHdConfigDetailsRequest started {} ", getHDCONFIGDetails);
        NetworkInventory networkInventory = new NetworkInventory();
        String serviceCode = "";
        String errorMessage = "";
        String errorCode = "";
        GetHDCONFIGDetailsResponse getHDCONFIGDetailsResponse = new GetHDCONFIGDetailsResponse();
        HDCONFIGDetailsAcknowledgement hdCONFIGDetailsAcknowledgement = new HDCONFIGDetailsAcknowledgement();
        Acknowledgement ack = new Acknowledgement();
        try {
            if (Objects.nonNull(getHDCONFIGDetails)
                    && Objects.nonNull(getHDCONFIGDetails.getHDCONFIGDetails())
                    && StringUtils.isNotBlank(getHDCONFIGDetails.getHDCONFIGDetails().getRequestID())) {
                HDCONFIGDetails request = getHDCONFIGDetails.getHDCONFIGDetails();
                String processInstanceId = request.getRequestID();
                String[] instanceId = processInstanceId.split("#");
                processInstanceId = instanceId[1];

                Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
                        .activityId("hd-config-async").singleResult();

                serviceCode = processInstanceId;
                if (Objects.nonNull(processInstanceId)) {
                    networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
                    networkInventory.setServiceCode(serviceCode);
                    networkInventory.setRequestId(StringUtils.trimToEmpty(request.getRequestID()));
                    networkInventory.setRequest(Utils.convertObjectToXmlString(getHDCONFIGDetails, GetHDCONFIGDetails.class));
                    networkInventory.setType(CramerConstants.GET_HD_CONFIG_DETAILS);

                    runtimeService.setVariable(execution.getId(), "getHdConfigAsyncSuccess", true);
                    runtimeService.trigger(execution.getId());

                    if (Objects.nonNull(request.getRequestID()) && request.isStatus()) {
                        ack.setStatus(true);
                    } else {
                        ack.setStatus(false);
                        saveInComponentAttributeAndAdditionalParams(request, serviceCode);
                    }
                    networkInventory.setResponse(Utils.convertObjectToXmlString(getHDCONFIGDetailsResponse, GetHDCONFIGDetailsResponse.class));
                    networkInventoryRepository.save(networkInventory);
                    logger.info("processGetHdConfigDetailsRequest response {} ", Utils.convertObjectToJson(ack));
                } else
                    logger.info("processGetHdConfigDetailsRequest Execution is null for {}", processInstanceId);
            } else {
                logger.info(
                        "GetHdConfigDetailsAsyncEndpoint::processGetHdConfigDetailsRequest id/process instance id is null or invalid to trigger flowable execution {} ",
                        getHDCONFIGDetails);
            }
            getHDCONFIGDetailsResponse.setHDCONFIGDetailsAck(hdCONFIGDetailsAcknowledgement);
            hdCONFIGDetailsAcknowledgement.setAcknowledgement(ack);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ack.setStatus(false);
            ack.setErrorCode("500");
            ack.setErrorMessage(e.getMessage());
            networkInventory.setResponse(e.getMessage());
            networkInventoryRepository.save(networkInventory);
        }
        return getHDCONFIGDetailsResponse;
    }

    private void saveInComponentAttributeAndAdditionalParams(HDCONFIGDetails request, String serviceCode) {
        ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
        logger.info("entering saveInComponentAttributeAndAdditionalParams processGetHdConfigDetailsRequest with serviceCode:{}", serviceCode);
        if (scServiceDetail != null && org.apache.commons.lang3.StringUtils.isNotBlank(request.getReason())) {
            try {
                logger.info("getErrorMessageDetails condition meet for GetHdConfigDetailsAsyncEndpoint");
                Map<String, String> hdConfigAsyncMap = new HashMap<>();
                hdConfigAsyncMap.put("hdConfigStatus", String.valueOf(request.isStatus()));

                componentAndAttributeService.updateAttributes(scServiceDetail.getId(),
                        hdConfigAsyncMap, AttributeConstants.COMPONENT_LM,"A");

                componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "hdConfigErrorReason",
                        componentAndAttributeService.getErrorMessageDetails(request.getReason(), ""),
                        AttributeConstants.ERROR_MESSAGE, "hd-config-async");
            } catch (Exception e) {
                logger.error("exception occurred in saveInComponentAttributeAndAdditionalParams ----------->{}", e);
            }
        }
    }

    @PayloadRoot(namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails", localPart = "submitHDCONFIGFailureResponse")
    @ResponsePayload
    public SubmitHDCONFIGFailureResponseResponse processHDCONFIGFailureResponse(@RequestPayload SubmitHDCONFIGFailureResponse failureResponse)
            throws TclCommonException, JAXBException {
        logger.info("processHDCONFIGFailureResponse started {} ", failureResponse);
        String serviceCode = "";
        String errorMessage = "";
        String errorCode = "";
        NetworkInventory networkInventory = new NetworkInventory();
        SubmitHDCONFIGFailureResponseResponse submitHDCONFIGFailureResponseResponse = new SubmitHDCONFIGFailureResponseResponse();
        HDCONFIGDetailsAcknowledgement hdconfigDetailsAcknowledgement = new HDCONFIGDetailsAcknowledgement();
        Acknowledgement acknowledgement = new Acknowledgement();
        try {
            if (Objects.nonNull(failureResponse) && Objects.nonNull(failureResponse.getSubmitHDCONFIGFailure())) {
                SubmitHDCONFIGFailureResponse2 submitHDCONFIGFailure = failureResponse.getSubmitHDCONFIGFailure();
                networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                networkInventory.setServiceCode(StringUtils.trimToEmpty(submitHDCONFIGFailure.getRequestID()));
                networkInventory.setRequestId(StringUtils.trimToEmpty(submitHDCONFIGFailure.getServiceID()));
                networkInventory.setType(CramerConstants.GET_HD_CONFIG_DETAILS);

                if (Objects.nonNull(submitHDCONFIGFailure.getHDCONFIGFailureResponse())) {
                    HDCONFIGFailureResponse hdconfigFailureResponse = submitHDCONFIGFailure.getHDCONFIGFailureResponse();
                    errorCode = hdconfigFailureResponse.getErrorCode();
                    errorMessage = hdconfigFailureResponse.getErrorLongDescription();
                    ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(submitHDCONFIGFailure.getServiceID(),"INPROGRESS");
                    componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "hdConfigErrorReason",
                            componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
                            AttributeConstants.ERROR_MESSAGE, "hd-config-async");
                }
                networkInventory.setResponse(Utils.convertObjectToXmlString(failureResponse,SubmitHDCONFIGFailureResponse.class));
                networkInventoryRepository.save(networkInventory);
            }
        } catch (Exception e) {
            acknowledgement.setStatus(false);
            acknowledgement.setErrorCode("000000");
            acknowledgement.setErrorMessage(e.getMessage());
            hdconfigDetailsAcknowledgement.setAcknowledgement(acknowledgement);
            submitHDCONFIGFailureResponseResponse.setSubmitHDCONFIGFailureAck(hdconfigDetailsAcknowledgement);
            logger.error(String.format("Exception in processHDCONFIGFailureResponse, %s", e));
        }
        return submitHDCONFIGFailureResponseResponse;
    }

}
