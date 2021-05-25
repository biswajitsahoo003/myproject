package com.tcl.dias.serviceactivation.cramer.getclrasync;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.createclrasync.beans.CLRcreationFailureResponse;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.CramerServiceErrorDetails;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.CramerTxServiceRequestFailure;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.GetCLRInfoResponse;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.OrderInfo;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.SubmitGetCramerServiceFailureResponse;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import javax.xml.bind.JAXBException;

@Endpoint
public class GetClrAsyncEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(GetClrAsyncEndPoint.class);

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    GetClrInfoAsyncDao getClrInfoAsyncDao;

    @Autowired
    NetworkInventoryRepository networkInventoryRepository;
    

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @PayloadRoot(namespace = "http://ACE_Common_Lib/ACE_SP_GetCLRStatus/CramerGetCLRResponse", localPart = "getCLRInfoResponse")
    @ResponsePayload
    public void processGetClrAsyncRequest(@RequestPayload GetCLRInfoResponse getCLRInfoResponse) throws Exception {

        logger.info("GetClrAsyncEndPoint received input getCLRInfoResponse {} ", getCLRInfoResponse);

		NetworkInventory networkInventory = new NetworkInventory();
		try {

			if (getCLRInfoResponse !=null && getCLRInfoResponse.getHeader() !=null &&  getCLRInfoResponse.getHeader().getRequestID() !=null) {
				String processInstanceId = getCLRInfoResponse.getHeader().getRequestID();
				String instance[]=  processInstanceId.split("#");
				processInstanceId=instance[1];

					
					if(getCLRInfoResponse.getResponse()!=null && getCLRInfoResponse.getResponse().getService()!=null) {

						String serviceCode =getCLRInfoResponse.getResponse().getServiceId();
						networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
						networkInventory.setServiceCode(null!=serviceCode?serviceCode:"NA");
						networkInventory.setType(CramerConstants.GET_CLR_ASYNC);
						networkInventory.setRequestId(processInstanceId);
						networkInventory.setResponse("VOID");
						try {
							Thread.sleep(10000);
						} catch (Exception e) {
							logger.error("Exception in processGetClrAsyncRequest-threadwait", e);
						}
						Execution execution = runtimeService.createExecutionQuery().processInstanceId(instance[1]).activityId("get-clr-async").singleResult();
						if (execution != null) {
							
							String serviceContents = StringUtils.trimToEmpty((String)runtimeService.getVariable(execution.getId(), "serviceContents"));
							logger.info("{} serviceContent in get clr async request {}",serviceCode, serviceContents);
							getClrInfoAsyncDao.saveTxConfigurationDetails(getCLRInfoResponse, serviceContents);  //saves Tx,worker,protection,nodeToConfigure details.

							//getting customer node port and saving as customerNodePort attribute in scComponentAttribute.
							getClrInfoAsyncDao.extractCustomerPort(getCLRInfoResponse);

							OrderInfo orderInfo =getCLRInfoResponse.getResponse();
							Boolean isIsNOCActionRequired = orderInfo.getService().isIsNOCActionRequired();
							Boolean isIsACEActionRequired = orderInfo.getService().isIsACEActionRequired();
							logger.info("{} isIsNOCActionRequired: {} isIsACEActionRequired: {}", serviceCode, isIsNOCActionRequired,isIsACEActionRequired);


							if(isIsACEActionRequired==false) {
								logger.info("{} isIsACEActionRequired: {}. Hence setting serviceContents {} to empty", serviceCode, isIsACEActionRequired,serviceContents);
								serviceContents ="";
							}
							 runtimeService.setVariable(execution.getId(), "txManualConfigRequired", isIsNOCActionRequired);
							 runtimeService.setVariable(execution.getId(), "serviceContents", serviceContents);
							 runtimeService.setVariable(execution.getId(), "getCLRSuccess", true);
							 runtimeService.trigger(execution.getId());
							
						}else {
							networkInventory.setResponse("Execution is null");
							logger.info("processGetClrAsyncRequest Execution is null for {}", processInstanceId);
						}
				} else {
					networkInventory.setServiceCode("NA");
					logger.info("GetClrAsyncEndPoint::processGetClrAsyncRequest response is null {} ",getCLRInfoResponse);
				}
			} else {
				networkInventory.setServiceCode("NA");
				logger.info("GetClrAsyncEndPoint::processGetClrAsyncRequest id/process instance id is null or invalid to trigger flowable execution {} ",
						getCLRInfoResponse);
			}


			try {
				networkInventory.setRequest(Utils.convertObjectToXmlString(getCLRInfoResponse,GetCLRInfoResponse.class));
				networkInventoryRepository.save(networkInventory);
			}catch(Exception e) {
				logger.error("Exception in processGetClrAsyncRequest", e);

			}
		} catch (TclCommonException |JAXBException e) {
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			logger.error("Exception in processGetClrAsyncRequest", e);
		}

	}


    @PayloadRoot(namespace = "http://ACE_Common_Lib/ACE_SP_GetCLRStatus/CramerGetCLRResponse", localPart = "submitGetCramerServiceFailureResponse")
    @ResponsePayload
    public void processSubmitGetCramerServiceFailureResponse(@RequestPayload SubmitGetCramerServiceFailureResponse submitGetCramerServiceFailureResponse)  {
        logger.info("GetClrAsyncEndPoint received input SubmitGetCramerServiceFailureResponse {} ", submitGetCramerServiceFailureResponse);
        NetworkInventory networkInventory = new NetworkInventory();
     
        if (submitGetCramerServiceFailureResponse !=null && submitGetCramerServiceFailureResponse.getHeader() !=null &&  submitGetCramerServiceFailureResponse.getHeader().getRequestID() !=null) {
            String processInstanceId = submitGetCramerServiceFailureResponse.getHeader().getRequestID();
            
        	try {
				Thread.sleep(10000);
			} catch (Exception e) {
				logger.error("Exception in processSubmitGetCramerServiceFailureResponse-threadwait", e);
			}
        	
            Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).activityId("get-clr-async").singleResult();
          
            	networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
            	networkInventory.setServiceCode(null!=submitGetCramerServiceFailureResponse.getRequest().getServiceID()?submitGetCramerServiceFailureResponse.getRequest().getServiceID():"NA");
            	networkInventory.setType(CramerConstants.GET_CLR_ASYNC_FAILURE);
            	networkInventory.setRequestId(processInstanceId);
            	if (execution != null) {
	            	String serviceContents = StringUtils.trimToEmpty((String)runtimeService.getVariable(execution.getId(), "serviceContents"));
	            	logger.info("serviceContent in get clr async request {}", serviceContents);
	                runtimeService.setVariable(execution.getId(), "getCLRSuccess", false);
	                runtimeService.trigger(execution.getId());                                       
	            } else {
	            	logger.info("processGetClrAsyncRequest Execution is null for {}", processInstanceId);
	            }
        } else {
            logger.info(
                    "GetClrAsyncEndPoint::processSubmitGetCramerServiceFailureResponse id/process instance id is null or invalid to trigger flowable execution {} ",
                    submitGetCramerServiceFailureResponse);
        }
        
        
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(submitGetCramerServiceFailureResponse.getRequest().getServiceID(),TaskStatusConstants.INPROGRESS);

			logger.info("getErrorMessageDetails condition meet for scServiceDetail:{} and serviceId:{}",
					scServiceDetail, submitGetCramerServiceFailureResponse.getRequest().getServiceID());

			if (scServiceDetail != null & submitGetCramerServiceFailureResponse != null
					&& submitGetCramerServiceFailureResponse.getRequest() != null
					&& submitGetCramerServiceFailureResponse.getRequest().getErrorDetails() != null
					&& !submitGetCramerServiceFailureResponse.getRequest().getErrorDetails().isEmpty()) {
				logger.info("getErrorMessageDetails condition meet for processSubmitGetCramerServiceFailureResponse");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
						"serviceDesignCallFailureReason", getErrorMessageDetails(submitGetCramerServiceFailureResponse),
						AttributeConstants.ERROR_MESSAGE, "get-clr-async");
			}
		} catch (Exception e) {
			logger.info("processSubmitGetCramerServiceFailureResponse error message capture   {} ", e);
		}
 
        
        try {
        	networkInventory.setRequest(Utils.convertObjectToXmlString(submitGetCramerServiceFailureResponse,SubmitGetCramerServiceFailureResponse.class));
        	networkInventory.setResponse("VOID");
            networkInventoryRepository.save(networkInventory);
        }catch(Exception e) {
        	logger.error("Exception in processSubmitGetCramerServiceFailureResponse", e);
        }
        
    }


	private String getErrorMessageDetails(SubmitGetCramerServiceFailureResponse submitGetCramerServiceFailureResponse) throws TclCommonException {
		logger.info(" CreateClrAsyncEndPoint getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

		if (submitGetCramerServiceFailureResponse != null
				&& submitGetCramerServiceFailureResponse.getRequest() != null) {
			logger.info("CreateClrAsyncEndPoint getErrorMessageDetails forund");
			CramerTxServiceRequestFailure errorDetails = submitGetCramerServiceFailureResponse.getRequest();

			if (errorDetails != null && errorDetails.getErrorDetails() != null
					&& !errorDetails.getErrorDetails().isEmpty()) {

				for (CramerServiceErrorDetails serviceErrorDetails : errorDetails.getErrorDetails()) {
					ErrorBean errorBean = new ErrorBean();
					errorBean.setErrorLongDescription(serviceErrorDetails.getErrorLongDescription());
					errorBean.setErrorCode(serviceErrorDetails.getErrorCode());
					errorBean.setErrorShortDescription(serviceErrorDetails.getErrorShortDescription());
					errorDetailsBean.getErrorDetails().add(errorBean);
				}
			}
		}

		return Utils.convertObjectToJson(errorDetailsBean);

	}
}