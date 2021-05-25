package com.tcl.dias.serviceactivation.activemq.receiver;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.netp.beans.response.ErrorDetails;
import com.tcl.dias.serviceactivation.activation.netp.beans.response.SubmitAck;
import com.tcl.dias.serviceactivation.activation.netp.beans.response.SubmitFailureResponse;
import com.tcl.dias.serviceactivation.activation.netp.beans.response.SubmitSuccessResponse;
import com.tcl.dias.serviceactivation.activation.utils.FtpUtils;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the MessageReceiver.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
@Transactional(readOnly=false)
public class ActivationMessageReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivationMessageReceiver.class);

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	NetworkInventoryRepository networkInventoryRepository;

	@Autowired
	TaskService taskService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	FtpUtils ftpUtils;

	@JmsListener(destination = "NetpSuccessResponseQueue", selector = "originatingSystem = 'OPTIMUS'")
	public void receiveSuccessQueue(String text) {
		try {
			MDC.put(CommonConstants.MDC_TOKEN_KEY,Utils.generateUid(22));
			LOGGER.info("Message Received for success {}", text);
			if (text.contains("submitAck")) {
				SubmitAck submitAck = jaxbXmlToNetpSucess(text);
				String requestId = submitAck.getRequest().getRequestId();
				String processId = "";
				String flowName = "";
      
				if (requestId.contains("_") || requestId.contains("#")) {

					if (requestId.contains("#")) {
						String flow[] = requestId.split("#");
						flowName = flow[2].substring(0, flow[2].lastIndexOf("_"));
						processId = flow[1];
					}
				}

				String netPFlowName = "ACK_" + flowName;
				LOGGER.info("Saving the Network Inventory for Flow Name {}", netPFlowName);
				Optional<NetworkInventory> networkInv = networkInventoryRepository.findByRequestIdAndType(requestId,
						netPFlowName);
				if (networkInv.isPresent()) {
					LOGGER.info("Saving the Network Inventory {}", flowName);
					networkInv.get().setResponse(text);
					networkInventoryRepository.save(networkInv.get());
				}
			}else if (text.contains("submitSuccessResponse")) {
				SubmitSuccessResponse submitSuccessResponse = jaxbXmlToNetpConfigSuccess(text);
				String requestId = submitSuccessResponse.getRequest().getRequestId();
				String processId = "";
				String flowName = "";
				String serviceCode="";
				if (requestId.contains("_") || requestId.contains("#")) {

					if (requestId.contains("#")) {
						String flow[] = requestId.split("#");
						flowName = flow[2].substring(0, flow[2].lastIndexOf("_"));
						processId = flow[1];
						serviceCode = flow[0].substring(4, flow[0].length());
						LOGGER.info("submitSuccessResponse for serviceCode={},flowName{},processId={}", serviceCode,flowName,processId);

					}
				}
				ScServiceDetail	scServiceDetail= scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

				if (flowName.contains("txs")) {

					if (submitSuccessResponse.getRequest().getSuccessMessage() != null && submitSuccessResponse
							.getRequest().getSuccessMessage().contains("UnProtected Links Data")) {

						triggerWorkFlow(requestId, processId, "tx-sdh-configuration-status-async",
								"txSDHConfigurationSuccess", false);
					} else {
						triggerWorkFlow(requestId, processId, "tx-sdh-configuration-status-async",
								"txSDHConfigurationSuccess", true);
					}
				} else if (flowName.contains("txm")) {
					if (submitSuccessResponse.getRequest().getSuccessMessage() != null && submitSuccessResponse
							.getRequest().getSuccessMessage().contains("UnProtected Links Data")) {

						updateErrorForUnprotectedLink(flowName, serviceCode,
								submitSuccessResponse.getRequest().getSuccessMessage());

						triggerWorkFlow(requestId, processId, "tx-sdh-configuration-status-async",
								"txSDHConfigurationSuccess", false);
					} else {

						triggerWorkFlow(requestId, processId, "tx-mpls-configuration-status-async",
								"txMPLSConfigurationSuccess", true);
					}
				}else if (flowName.contains("rf_terminate_config")) {
					LOGGER.info("Rf Terminate Success");
					triggerWorkFlow(requestId, processId, "rf-terminate-configuration-config-async", "rfTerminateConfigurationSuccess",true);	
				}else if (flowName.contains("rf_config")) {
					triggerWorkFlow(requestId, processId, "rf-configuration-config-async", "rfConfigurationSuccess",true);	
				}else if (flowName.contains("ip_terminate_config")) {
					LOGGER.info("Ip Terminate Success");
					triggerWorkFlow(requestId, processId, "ip-terminate-configuration-config-async", "ipTerminateConfigurationSuccess",true);	
				}
				else if (flowName.contains("ip_blocked_config")) {
					LOGGER.info("Ip Blocked Success");
					triggerWorkFlow(requestId, processId, "ip-terminate-configuration-block-config-async", "ipBlockedResourceSuccess",true);	
				}
				else if (flowName.contains("assign_dummy_rf_config")) {
					LOGGER.info("Assign Dummy Rf Success");
					triggerWorkFlow(requestId, processId, "assign-rf-dummy-service-configuration-config-async", "assignRfDummyServiceConfigurationSuccess",true);	
				}
				else if (flowName.contains("assign_dummy_ip_config")) {
					LOGGER.info("Assign Dummy Ip Success");
					triggerWorkFlow(requestId, processId, "assign-dummy-service-configuration-config-async", "assignDummyServiceConfigurationSuccess",true);	
				}
				else if (flowName.contains("release_dummy_ip_config")) {
					LOGGER.info("Release Dummy Ip Success");
					triggerWorkFlow(requestId, processId, "release-dummy-service-configuration-config-async", "releaseDummyServiceConfigurationSuccess",true);	
				}else if (flowName.contains("ip_config")) {
					triggerWorkFlow(requestId, processId, "service-configuration-config-async",
							"serviceConfigurationSuccess", true);
				}else if (flowName.contains("ip_preview")) {
					triggerWorkFlow(requestId, processId, "preview-service-configuration-config-async",
							"previewIpConfigSuccess", true);
				}else if (flowName.contains("ip_cancel")) {
					triggerWorkFlow(requestId, processId, "cancel-service-configuration-config-async",
							"cancelIpConfigSuccess", true);
				}else if (flowName.contains("txsp")) {
					triggerWorkFlow(requestId, processId, "tx-sdh-post-validation-status-async",
							"txSDHpostValidationSuccess", true);
				}else if (flowName.contains("txmp")) {
					triggerWorkFlow(requestId, processId, "tx-mpls-post-validation-status-async",
							"txMPLSPostValidationSuccess", true);
				}else if (flowName.equalsIgnoreCase("post_validation")) {
					

					try {
						LOGGER.info("uploadPostValidationFiles for serviceCode={}", serviceCode);
						ftpUtils.uploadPostValidationFiles(submitSuccessResponse.getRequest().getPathOfOutputFile(),
								scServiceDetail.getScOrder().getOpOrderCode(), serviceCode);
					}

					catch (Exception e) {
						LOGGER.error("uploading post validation file error {}", e);
					}
					
					triggerWorkFlow(requestId, processId, "e2e-service-testing-status", "e2eServiceTestingCompleted",
							true);
				}
				String netPFlowName = "SUB_" + flowName;
				LOGGER.info("Saving the Network Inventory for Flow Name {}", netPFlowName);
				Optional<NetworkInventory> networkInv = networkInventoryRepository.findByRequestIdAndType(requestId,
						netPFlowName);
				if (networkInv.isPresent()) {
					LOGGER.info("Saving the Network Inventory {}", flowName);
					networkInv.get().setResponse(text);
					networkInventoryRepository.save(networkInv.get());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in message recieve", e);
		}
	}
	
	@Transactional(readOnly=false)
	public void updateErrorForUnprotectedLink(String flowName, String serviceCode, String message) {
		LOGGER.info("FlowName: {}", flowName);
		try {
			String errorMessage = getErrorMessageDetailsForUnprotectedLink(message);

			LOGGER.info("updateErrorForUnprotectedLink:{}", errorMessage);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,
					"INPROGRESS");

			LOGGER.info("scServiceDetail for error message fetch:{}", scServiceDetail);

			if (errorMessage != null && scServiceDetail != null && getErrorMessage(flowName) != null) {

				LOGGER.info("updateErrorForUnprotectedLink updateErrorForUnprotectedLink meet");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, getErrorMessage(flowName),
						errorMessage, AttributeConstants.ERROR_MESSAGE, getTaskName(flowName));
			}
		} catch (Exception e) {
			LOGGER.error("error in getting the response for updateErrorForUnprotectedLink-----{}", e);
		}
	}

	/**
	 * triggerWorkFlow
	 * 
	 * @param requestId
	 * @param processId
	 * @throws InterruptedException 
	 */
	private void triggerWorkFlow(String requestId, String processId, String activitId, String deleteGateClass,
			Boolean status) {
		try {
			try {
				Thread.sleep(20000);
			} catch (Exception ex) {
				LOGGER.error("InterruptedException {} : ", ex);
			}
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(processId)
					.activityId(activitId).singleResult();
			if (execution != null) {
				runtimeService.setVariable(execution.getId(), deleteGateClass, status);
				runtimeService.trigger(execution.getId());
			} else {
				LOGGER.info("processIPServiceDetailsRequest Execution is null for processId {}", processId);
			}
		} catch (Exception e) {
			LOGGER.error("error while trigger workflow:{}", e);
		}
	}

	@JmsListener(destination = "NetpErrorResponseQueue", selector = "originatingSystem = 'OPTIMUS'")
	public void receiveFailureQueue(String text) {
		try {
			MDC.put(CommonConstants.MDC_TOKEN_KEY,Utils.generateUid(22));
			LOGGER.info("Message Received for failure {}", text);
			if (text.contains("submitFailureResponse")) {
				SubmitFailureResponse submitAck = jaxbXmlToNetpFailure(text);
				String requestId = submitAck.getRequest().getRequestId();
				String processId = "";
				String flowName = "";
				String serviceCode = "";

				ScServiceDetail scServiceDetail=null;

				if (requestId.contains("_") || requestId.contains("#")) {

					if (requestId.contains("#")) {
						String flow[] = requestId.split("#");
						flowName = flow[2].substring(0, flow[2].lastIndexOf("_"));
						processId = flow[1];
						serviceCode = flow[0].substring(4, flow[0].length());

					} 
				}
				LOGGER.info("FlowName: {}",flowName);
				try {
					String errorMessage = getErrorMessageDetails(submitAck);

					LOGGER.info("getErrorMessageDetails:{}", errorMessage);
					scServiceDetail= scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
					if (flowName!=null && (flowName.contains("ip_blocked_config") || flowName.contains("ip_terminate_config"))) {
						scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "TERMINATION-INPROGRESS");
					}
					if (scServiceDetail == null) {
						scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "ACTIVE");
					}

					LOGGER.info("scServiceDetail for error message fetch:{}", scServiceDetail);

					if (errorMessage != null && scServiceDetail != null && getErrorMessage(flowName) != null) {

						LOGGER.info("getErrorMessageDetails condition meet");

						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
								getErrorMessage(flowName), errorMessage, AttributeConstants.ERROR_MESSAGE,
								getTaskName(flowName));
					}
				} catch (Exception e) {
					LOGGER.error("error in getting the response for getErrorMessageDetails-----{}", e);
				}
				
				

				if (flowName.contains("txs")) {
					triggerWorkFlow(requestId, processId, "tx-sdh-configuration-status-async",
							"txSDHConfigurationSuccess", false);
				}else if (flowName.contains("txm")) {
					triggerWorkFlow(requestId, processId, "tx-mpls-configuration-status-async",
							"txMPLSConfigurationSuccess", false);
				}else if (flowName.contains("rf_terminate_config")) {
					LOGGER.info("Rf Terminate Failure");
					triggerWorkFlow(requestId, processId, "rf-terminate-configuration-config-async", "rfTerminateConfigurationSuccess",false);	
				}else if (flowName.contains("rf_config")) {
					triggerWorkFlow(requestId, processId, "rf-configuration-config-async", "rfConfigurationSuccess",false);	
				}else if (flowName.contains("ip_terminate_config_") || flowName.equalsIgnoreCase("ip_terminate_config")) {
					LOGGER.info("Ip Terminate Failure for service id:{}",serviceCode);
					triggerWorkFlow(requestId, processId, "ip-terminate-configuration-config-async", "ipTerminateConfigurationSuccess",false);	
				}
				else if (flowName.contains("ip_blocked_config_") || flowName.equalsIgnoreCase("ip_blocked_config")) {
					LOGGER.info("Ip ip_blocked_config_ Failure for service id:{}",serviceCode);
					triggerWorkFlow(requestId, processId, "ip-terminate-configuration-block-config-async", "ipBlockedResourceSuccess",false);	
				}
				else if (flowName.contains("assign_dummy_rf_config")) {
					LOGGER.info("Assign Dummy Rf Failure");
					triggerWorkFlow(requestId, processId, "assign-rf-dummy-service-configuration-config-async", "assignRfDummyServiceConfigurationSuccess",true);	
				}
				else if (flowName.contains("assign_dummy_ip_config")) {
					LOGGER.info("Assign Dummy Ip Failure");
					triggerWorkFlow(requestId, processId, "assign-dummy-service-configuration-config-async", "assignDummyServiceConfigurationSuccess",false);	
				}
				else if (flowName.contains("release_dummy_ip_config")) {
					LOGGER.info("Release Dummy Ip Failure");
					triggerWorkFlow(requestId, processId, "release-dummy-service-configuration-config-async", "releaseDummyServiceConfigurationSuccess",false);	
				}else if (flowName.contains("ip_config")) {
					triggerWorkFlow(requestId, processId, "service-configuration-config-async",
							"serviceConfigurationSuccess", false);
				}else if (flowName.contains("ip_preview")) {
					triggerWorkFlow(requestId, processId, "preview-service-configuration-config-async",
							"previewIpConfigSuccess", false);
				}else if (flowName.contains("ip_cancel")) {
					triggerWorkFlow(requestId, processId, "cancel-service-configuration-config-async",
							"cancelIpConfigSuccess", false);
				}else if (flowName.contains("txsp")) {
					triggerWorkFlow(requestId, processId, "tx-sdh-post-validation-status-async",
							"txSDHpostValidationSuccess", false);
				}else if (flowName.contains("txmp")) {
					triggerWorkFlow(requestId, processId, "tx-mpls-post-validation-status-async",
							"txMPLSPostValidationSuccess", false);
				}else if (flowName.equalsIgnoreCase("post_validation")) {
					

					LOGGER.info("e2e-service-testing reponse received {}", flowName);

					try {
						if (submitAck.getRequest() != null && submitAck.getRequest().getErrorDetails() != null) {
							String pathUrl = submitAck.getRequest().getErrorDetails().get(0).getErrorLongDescription();
							String b = pathUrl.substring(pathUrl.lastIndexOf("file path"));
							StringUtils.chomp(b);
							String url = b.substring(b.indexOf("/"), b.lastIndexOf("[")-1);
							ftpUtils.uploadPostValidationFiles(url.trim(),
									scServiceDetail.getScOrder().getOpOrderCode(), serviceCode);
						}

					} catch (Exception e) {
						LOGGER.error("uploading post validation file error {}", e);
					}
					
					triggerWorkFlow(requestId, processId, "e2e-service-testing-status", "e2eServiceTestingCompleted",
							false);

				}
				
				String netPFlowName = "SUB_" + flowName;
				LOGGER.info("Saving the Network Inventory for Flow Name {}", netPFlowName);
				Optional<NetworkInventory> networkInv = networkInventoryRepository.findByRequestIdAndType(requestId,
						netPFlowName);
				if (networkInv.isPresent()) {
					networkInv.get().setResponse(text);
					networkInventoryRepository.save(networkInv.get());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in message recieve for failure", e);
		}
	}

	private String getTaskName(String type) {
		if (type.contains("txs")) {
			return "tx-configuration-sdh";
		} else if (type.contains("txm")) {
			return "tx-configuration-mpls";
		}else if (type.contains("rf_terminate_config")) {
			return "rf-terminate-configuration";
		}else if (type.contains("ip_terminate_config")) {
			return "ip-terminate-configuration";
		}
		
		else if (type.contains("ip_blocked_config")) {
			return "ip-terminate-block-configuration";
		}
		else if (type.contains("assign_dummy_rf_config")) {
			return "assign-rf-dummy-service-configuration";
		}
		else if (type.contains("assign_dummy_ip_config")) {
			return "assign-dummy-service-configuration";
		}
		else if (type.contains("release_dummy_ip_config")) {
			return "release-dummy-service-configuration";
		}

		else if (type.contains("ip_config")) {
			return "service-configuration";
		}

		else if (type.contains("ip_preview")) {
			return "preview-service-configuration";
		}

		else if (type.contains("ip_cancel")) {
			return "cancel-service-configuration";
		}
		/*else if(type.equalsIgnoreCase("txmp")) {
			return "tx-mpls-post-validation-jeopardy";
		}*/
		else if(type.contains("txsp")) {
			return "tx-post-validation-sdh";
		}
		else if (type.equalsIgnoreCase("post_validation")) {
			return "e2e-service-testing";
		}
		

		else if (type.contains("rf_config")) {
			return "rf-configuration";
		}

		else {
			return null;
		}
	}

	private String getErrorMessageDetails(SubmitFailureResponse submitAck) throws TclCommonException {
		LOGGER.info("getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

		if (submitAck != null && submitAck.getRequest() != null && submitAck.getRequest().getErrorDetails() != null) {
			LOGGER.info("getErrorMessageDetails forund");

			for (ErrorDetails errorDetails : submitAck.getRequest().getErrorDetails()) {
				ErrorBean errorBean = new ErrorBean();
				errorBean.setErrorLongDescription(errorDetails.getErrorLongDescription());
				errorBean.setErrorCode(errorDetails.getErrorCode());
				errorBean.setErrorShortDescription(errorDetails.getErrorShortDescription());
				errorDetailsBean.getErrorDetails().add(errorBean);
			}
		}

		return Utils.convertObjectToJson(errorDetailsBean);

	}
	
	private String getErrorMessageDetailsForUnprotectedLink(String errorMessage) throws TclCommonException {
		LOGGER.info("getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

		if (errorMessage != null ) {
			LOGGER.info("getErrorMessageDetails forund");

				ErrorBean errorBean = new ErrorBean();
				errorBean.setErrorLongDescription(errorMessage);
				errorBean.setErrorCode(errorMessage);
				errorBean.setErrorShortDescription(errorMessage);
				errorDetailsBean.getErrorDetails().add(errorBean);
		}

		return Utils.convertObjectToJson(errorDetailsBean);

	}
	
	

	private String getErrorMessage(String type) {
		if (type.contains("txs")) {
			return "txSdhConfigFailureReason";
		} else if (type.contains("txm")) {
			return "txMplsConfigFailureReason";
		}else if (type.contains("assign_dummy_rf_config")) {
			return "assign-rf-dummy-service-configuration";
		}
		else if (type.contains("assign_dummy_ip_config")) {
			return "assign-dummy-service-configuration";
		}
		else if (type.contains("release_dummy_ip_config")) {
			return "release-dummy-service-configuration";
		}
		else if (type.contains("ip_config")) {
			return "ipconfigFailureReason";
		}

		else if (type.contains("ip_preview")) {
			return "previewConfigurationFailureReason";
		}

		else if (type.contains("ip_cancel")) {
			return "ipConfigurationFailureReason";
		}
		
		else if(type.contains("txmp")) {
			return "txMplsPostValidationFailureReason";
		}
		else if(type.contains("txsp")) {
			return "txSdhpostValidationFailureReason";
		}
		
		else if (type.equalsIgnoreCase("post_validation")) {
			return "e2eTestingFailureReason";
		}

		else if (type.contains("rf_config")) {
			return "rFCallFailureReason";
		}else if (type.contains("ip_blocked_config")) {
			return "ipTerminationBlockFailureReason";
		}else if (type.contains("ip_terminate_config")) {
			return "ipTerminateConfigFailureReason";
		}

		else {
			return null;
		}
	}

	private SubmitAck jaxbXmlToNetpSucess(String inputXml) {
		try {
			inputXml = removeSoapHeaders(inputXml);
			JAXBContext context = JAXBContext.newInstance(SubmitAck.class);
			Unmarshaller um = context.createUnmarshaller();
			StringReader reader = new StringReader(inputXml);
			return (SubmitAck) um.unmarshal(reader);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb unmarshalling ", e);
		}
		return null;
	}

	private SubmitFailureResponse jaxbXmlToNetpFailure(String inputXml) {
		try {
			inputXml = removeSoapHeaders(inputXml);
			JAXBContext context = JAXBContext.newInstance(SubmitFailureResponse.class);
			Unmarshaller um = context.createUnmarshaller();
			StringReader reader = new StringReader(inputXml);
			return (SubmitFailureResponse) um.unmarshal(reader);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb unmarshalling ", e);
		}
		return null;
	}

	private SubmitSuccessResponse jaxbXmlToNetpConfigSuccess(String inputXml) {
		try {
			inputXml = removeSoapHeaders(inputXml);
			JAXBContext context = JAXBContext.newInstance(SubmitSuccessResponse.class);
			Unmarshaller um = context.createUnmarshaller();
			StringReader reader = new StringReader(inputXml);
			return (SubmitSuccessResponse) um.unmarshal(reader);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb unmarshalling ", e);
		}
		return null;
	}

	private String removeSoapHeaders(String xml) {
		xml = xml.substring(xml.indexOf("<soapenv:Body>"), xml.indexOf("</soapenv:Body>"));
		xml = xml.replace("<soapenv:Body>", "");
		return xml;
	}
}
