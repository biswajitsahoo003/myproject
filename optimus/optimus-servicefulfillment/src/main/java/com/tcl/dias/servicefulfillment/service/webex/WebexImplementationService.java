package com.tcl.dias.servicefulfillment.service.webex;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.gsc.AdvancedEnrichmentBean;
import com.tcl.dias.servicefulfillment.beans.webex.AccessCodeActivationBean;
import com.tcl.dias.servicefulfillment.beans.webex.ActivateMicrositeBean;
import com.tcl.dias.servicefulfillment.beans.webex.ActivateVoiceMicrositeBean;
import com.tcl.dias.servicefulfillment.beans.webex.AdfsSsoIntegrationBean;
import com.tcl.dias.servicefulfillment.beans.webex.ComponentAttributeBean;
import com.tcl.dias.servicefulfillment.beans.webex.ComponentTestingBean;
import com.tcl.dias.servicefulfillment.beans.webex.ConfigAccessCodeBean;
import com.tcl.dias.servicefulfillment.beans.webex.ConfigureEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.ConfirmMaterialAvailabilityBean;
import com.tcl.dias.servicefulfillment.beans.webex.CreatePoForEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.CreationCallbackGroupsBean;
import com.tcl.dias.servicefulfillment.beans.webex.CustomerAdoptionBean;
import com.tcl.dias.servicefulfillment.beans.webex.CustomerHandoverBean;
import com.tcl.dias.servicefulfillment.beans.webex.CustomerOnBoardTranningBean;
import com.tcl.dias.servicefulfillment.beans.webex.DispatchEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.EgressRoutingProfileBean;
import com.tcl.dias.servicefulfillment.beans.webex.GenerateOrderForWebexBean;
import com.tcl.dias.servicefulfillment.beans.webex.GenerateWebexEndpointInvoiceBean;
import com.tcl.dias.servicefulfillment.beans.webex.HybridServiceImplementationBean;
import com.tcl.dias.servicefulfillment.beans.webex.InstallEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.OrderSolutionViewBean;
import com.tcl.dias.servicefulfillment.beans.webex.PreparePrForEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvideAdFileBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvidePoForWebexBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvidePoForWebexEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvidePoReleaseForWebexBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvidePoReleaseForWebexEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvidePrForWebexBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvidePrForWebexEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.ProvideWbsglccWebexDetailBean;
import com.tcl.dias.servicefulfillment.beans.webex.ReleasePoForEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.ServiceDetails;
import com.tcl.dias.servicefulfillment.beans.webex.ServiceSolutionViewBean;
import com.tcl.dias.servicefulfillment.beans.webex.ServiceTestingBean;
import com.tcl.dias.servicefulfillment.beans.webex.SolutionDetails;
import com.tcl.dias.servicefulfillment.beans.webex.SolutionViewDetailsBean;
import com.tcl.dias.servicefulfillment.beans.webex.SrnGenerationEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.TdCreationDedicationNumberBean;
import com.tcl.dias.servicefulfillment.beans.webex.TrackEndpointBean;
import com.tcl.dias.servicefulfillment.beans.webex.UpdateCugInCCABean;
import com.tcl.dias.servicefulfillment.beans.webex.WEBEXOrder;
import com.tcl.dias.servicefulfillment.beans.webex.CustomerAdoptionBean;
import com.tcl.dias.servicefulfillment.beans.webex.CustomerHandoverBean;
import com.tcl.dias.servicefulfillment.beans.webex.CustomerOnBoardTranningBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.corba.se.impl.orbutil.RepIdDelegator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.gsc.AdvancedEnrichmentBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional(readOnly = true)
public class WebexImplementationService extends ServiceFulfillmentBaseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebexImplementationService.class);
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	FlowableBaseService flowableBaseService;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	StagePlanRepository stagePlanRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	
	
	@Transactional(readOnly = false)
	public ProvidePoForWebexBean providePoForWebex(ProvidePoForWebexBean poForWebex) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForWebex.getTaskId(), poForWebex.getWfTaskId());

		validateInputs(task, poForWebex);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poForWebex.getWebexLicencePoNumber())
				&& Objects.nonNull(poForWebex.getWebexLicencePoDate())) {
			atMap.put("webexLicencePoNumber", poForWebex.getWebexLicencePoNumber());
			atMap.put("webexLicencePoDate", poForWebex.getWebexLicencePoDate());
			//atMap.put("webexLicencePoVendorName", poForWebex.getWebexLicencePoVendorName());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		if (poForWebex.getDocumentIds() != null && !poForWebex.getDocumentIds().isEmpty())
			poForWebex.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", poForWebex.getDelayReason(), null, poForWebex);
		return (ProvidePoForWebexBean) flowableBaseService.taskDataEntry(task, poForWebex);
	}

	@Transactional(readOnly = false)
	public ProvidePoForWebexEndpointBean providePoForWebexEndpoint(ProvidePoForWebexEndpointBean poForWebexEndpoint)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForWebexEndpoint.getTaskId(), poForWebexEndpoint.getWfTaskId());

		validateInputs(task, poForWebexEndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poForWebexEndpoint.getWebexEndpointPoNumber())
				&& Objects.nonNull(poForWebexEndpoint.getWebexEndpointPoDate())) {
			atMap.put("webexEndpointPoNumber", poForWebexEndpoint.getWebexEndpointPoNumber());
			atMap.put("webexEndpointPoDate", poForWebexEndpoint.getWebexEndpointPoDate());
			//atMap.put("webexEndpointPoVendorName", poForWebexEndpoint.getWebexEndpointPoVendorName());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (poForWebexEndpoint.getDocumentIds() != null && !poForWebexEndpoint.getDocumentIds().isEmpty())
			poForWebexEndpoint.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", poForWebexEndpoint.getDelayReason(), null, poForWebexEndpoint);
		return (ProvidePoForWebexEndpointBean) flowableBaseService.taskDataEntry(task, poForWebexEndpoint);
	}

	@Transactional(readOnly = false)
	public ProvidePrForWebexBean providePrForWebex(ProvidePrForWebexBean prForWebex) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(prForWebex.getTaskId(), prForWebex.getWfTaskId());

		validateInputs(task, prForWebex);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(prForWebex.getWebexLicencePrNumber()) && Objects.nonNull(prForWebex.getWebexLicencePrDate())
				&& Objects.nonNull(prForWebex.getWebexLicencePrVendorName())) {
			atMap.put("webexLicencePrNumber", prForWebex.getWebexLicencePrNumber());
			atMap.put("webexLicencePrDate", prForWebex.getWebexLicencePrDate());
			atMap.put("webexLicencePrVendorName", prForWebex.getWebexLicencePrVendorName());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		if (prForWebex.getDocumentIds() != null && !prForWebex.getDocumentIds().isEmpty())
			prForWebex.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", prForWebex.getDelayReason(), null, prForWebex);
		return (ProvidePrForWebexBean) flowableBaseService.taskDataEntry(task, prForWebex);
	}

	@Transactional(readOnly = false)
	public ProvidePrForWebexEndpointBean providePrForWebexEndpoint(ProvidePrForWebexEndpointBean prForWebexEndpoint)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(prForWebexEndpoint.getTaskId(), prForWebexEndpoint.getWfTaskId());

		validateInputs(task, prForWebexEndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(prForWebexEndpoint.getWebexEndpointPrNumber())
				&& Objects.nonNull(prForWebexEndpoint.getWebexEndpointPrDate())
				&& Objects.nonNull(prForWebexEndpoint.getWebexEndpointPrVendorName())) {
			atMap.put("webexEndpointPrNumber", prForWebexEndpoint.getWebexEndpointPrNumber());
			atMap.put("webexEndpointPrDate", prForWebexEndpoint.getWebexEndpointPrDate());
			atMap.put("webexEndpointPrVendorName", prForWebexEndpoint.getWebexEndpointPrVendorName());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (prForWebexEndpoint.getDocumentIds() != null && !prForWebexEndpoint.getDocumentIds().isEmpty())
			prForWebexEndpoint.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", prForWebexEndpoint.getDelayReason(), null, prForWebexEndpoint);
		return (ProvidePrForWebexEndpointBean) flowableBaseService.taskDataEntry(task, prForWebexEndpoint);
	}

	@Transactional(readOnly = false)
	public GenerateOrderForWebexBean generateOrderForWebex(GenerateOrderForWebexBean orderForWebex)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(orderForWebex.getTaskId(), orderForWebex.getWfTaskId());

		validateInputs(task, orderForWebex);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(orderForWebex.getWebexLicenceInvoiceNumber())
				&& Objects.nonNull(orderForWebex.getWebexLicenceIdNo())) {
			atMap.put("webexLicenceInvoiceNumber", orderForWebex.getWebexLicenceInvoiceNumber());
			atMap.put("webexLicenceWebIdNo", orderForWebex.getWebexLicenceIdNo());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		if (orderForWebex.getDocumentIds() != null && !orderForWebex.getDocumentIds().isEmpty())
			orderForWebex.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", orderForWebex.getDelayReason(), null, orderForWebex);
		return (GenerateOrderForWebexBean) flowableBaseService.taskDataEntry(task, orderForWebex);
	}

	/*
	 * @Transactional(readOnly = false) public GenerateOrderForWebexBean
	 * generateOrderForWebex(GenerateOrderForWebexBean orderForWebex) throws
	 * TclCommonException { Task task =
	 * getTaskByIdAndWfTaskId(orderForWebex.getTaskId(),
	 * orderForWebex.getWfTaskId());
	 * 
	 * validateInputs(task, orderForWebex);
	 * 
	 * Map<String, String> atMap = new HashMap<>();
	 * 
	 * if (Objects.nonNull(orderForWebex.getWebexLicenceInvoiceNumber()) &&
	 * Objects.nonNull(orderForWebex.getWebexLicenceWebIdNo())) {
	 * atMap.put("webexLicenceInvoiceNumber",
	 * orderForWebex.getWebexLicenceInvoiceNumber());
	 * atMap.put("webexLicenceWebIdNo", orderForWebex.getWebexLicenceWebIdNo()); }
	 * 
	 * componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
	 * AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
	 * 
	 * if (orderForWebex.getDocumentIds() != null &&
	 * !orderForWebex.getDocumentIds().isEmpty()) orderForWebex.getDocumentIds()
	 * .forEach(attachmentIdBean -> makeEntryInScAttachment(task,
	 * attachmentIdBean.getAttachmentId()));
	 * 
	 * processTaskLogDetails(task, "CLOSED", orderForWebex.getDelayReason(), null);
	 * return (GenerateOrderForWebexBean) flowableBaseService.taskDataEntry(task,
	 * orderForWebex); }
	 * 
	 * @Transactional(readOnly = false) public GenerateOrderForWebexBean
	 * generateOrderForWebexEndpoint(GenerateOrderForWebexBean
	 * orderForWebexEndpoint) throws TclCommonException { Task task =
	 * getTaskByIdAndWfTaskId(orderForWebexEndpoint.getTaskId(),
	 * orderForWebexEndpoint.getWfTaskId());
	 * 
	 * validateInputs(task, orderForWebexEndpoint);
	 * 
	 * Map<String, String> atMap = new HashMap<>();
	 * 
	 * if (Objects.nonNull(orderForWebexEndpoint.getWebexLicenceInvoiceNumber()) &&
	 * Objects.nonNull(orderForWebexEndpoint.getWebexLicenceWebIdNo())) {
	 * atMap.put("webexLicenceInvoiceNumber",
	 * orderForWebexEndpoint.getWebexLicenceInvoiceNumber());
	 * atMap.put("webexLicenceWebIdNo",
	 * orderForWebexEndpoint.getWebexLicenceWebIdNo()); }
	 * 
	 * componentAndAttributeService.updateAttributesEndpoint(task.getServiceId(),
	 * atMap, task.getSiteType());
	 * 
	 * if (orderForWebexEndpoint.getDocumentIds() != null &&
	 * !orderForWebexEndpoint.getDocumentIds().isEmpty())
	 * orderForWebexEndpoint.getDocumentIds() .forEach(attachmentIdBean ->
	 * makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
	 * 
	 * processTaskLogDetails(task, "CLOSED", orderForWebexEndpoint.getDelayReason(),
	 * null); return (GenerateOrderForWebexBean)
	 * flowableBaseService.taskDataEntry(task, orderForWebexEndpoint); }
	 */

	@Transactional(readOnly = false)
	public ProvidePoReleaseForWebexBean providePoReleaseForWebex(ProvidePoReleaseForWebexBean poReleaseForWebex)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poReleaseForWebex.getTaskId(), poReleaseForWebex.getWfTaskId());

		validateInputs(task, poReleaseForWebex);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poReleaseForWebex.getWebexLicencePoRelease())) {
			atMap.put("webexLicencePoRelease", poReleaseForWebex.getWebexLicencePoRelease());
			atMap.put("webexLicenceCCWWebOrderId", poReleaseForWebex.getWebexLicenceCCWWebOrderId());
			atMap.put("webexLicencePoReleaseCreatedDate", poReleaseForWebex.getWebexLicencePoReleaseCreatedDate());
			if (poReleaseForWebex.getWebexLicencePoRelease().equalsIgnoreCase("yes")) {
				atMap.put("webexLicencePoStatus", "PO Released");
			}
			atMap.put("poReleaseCompletionDate", poReleaseForWebex.getPoReleaseCompletionDate());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", poReleaseForWebex.getDelayReason(), null, poReleaseForWebex);
		return (ProvidePoReleaseForWebexBean) flowableBaseService.taskDataEntry(task, poReleaseForWebex);
	}

	@Transactional(readOnly = false)
	public ProvidePoReleaseForWebexEndpointBean providePoReleaseForWebexEndpoint(
			ProvidePoReleaseForWebexEndpointBean poReleaseForWebexEndpoint) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poReleaseForWebexEndpoint.getTaskId(),
				poReleaseForWebexEndpoint.getWfTaskId());

		validateInputs(task, poReleaseForWebexEndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poReleaseForWebexEndpoint.getWebexEndpointPoRelease())) {
			atMap.put("webexEndpointPoRelease", poReleaseForWebexEndpoint.getWebexEndpointPoRelease());
			atMap.put("endpointPoReleaseCompletionDate", poReleaseForWebexEndpoint.getEndpointPoReleaseCompletionDate());
			if (poReleaseForWebexEndpoint.getWebexEndpointPoRelease().equalsIgnoreCase("yes")) {
				atMap.put("webexEndpointPoStatus", "PO Released");
			}
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		processTaskLogDetails(task, "CLOSED", poReleaseForWebexEndpoint.getDelayReason(), null,
				poReleaseForWebexEndpoint);
		return (ProvidePoReleaseForWebexEndpointBean) flowableBaseService.taskDataEntry(task,
				poReleaseForWebexEndpoint);
	}

	@Transactional(readOnly = false)
	public AdfsSsoIntegrationBean adfsSsoIntegration(AdfsSsoIntegrationBean adfsSsoIntegrationBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(adfsSsoIntegrationBean.getTaskId(), adfsSsoIntegrationBean.getWfTaskId());

		validateInputs(task, adfsSsoIntegrationBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(adfsSsoIntegrationBean)) {
			atMap.put("claimRulesOnCustomerIdp", adfsSsoIntegrationBean.getClaimRulesOnCustomerIdp());
			atMap.put("idpDetails", adfsSsoIntegrationBean.getIdpDetails());
			atMap.put("idpUrl", adfsSsoIntegrationBean.getIdpUrl());
			atMap.put("adfsIntergrationCompletionDate", adfsSsoIntegrationBean.getAdfsIntergrationCompletionDate());
			atMap.put("ssoConfigurationDate", adfsSsoIntegrationBean.getSsoConfigurationDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, "CLOSED", adfsSsoIntegrationBean.getDelayReason(), null, adfsSsoIntegrationBean);
		return (AdfsSsoIntegrationBean) flowableBaseService.taskDataEntry(task, adfsSsoIntegrationBean);
	}

	@Transactional(readOnly = false)
	public ProvideAdFileBean provideAdFile(ProvideAdFileBean provideAdFileBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(provideAdFileBean.getTaskId(), provideAdFileBean.getWfTaskId());

		validateInputs(task, provideAdFileBean);

		if (provideAdFileBean.getDocumentIds() != null && !provideAdFileBean.getDocumentIds().isEmpty())
			provideAdFileBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", provideAdFileBean.getDelayReason(), null, provideAdFileBean);
		return (ProvideAdFileBean) flowableBaseService.taskDataEntry(task, provideAdFileBean);
	}

    @Transactional(readOnly = false)
    public CustomerAdoptionBean customerAdoption(CustomerAdoptionBean customerAdoptionBean)
            throws TclCommonException {
        Task task = getTaskByIdAndWfTaskId(customerAdoptionBean.getTaskId(), customerAdoptionBean.getWfTaskId());

        validateInputs(task, customerAdoptionBean);

        Map<String, String> atMap = new HashMap<>();

        if (Objects.nonNull(customerAdoptionBean)) {
            atMap.put("numberOfTrainingsCompleted", customerAdoptionBean.getNumberOfTrainingsCompleted());
            atMap.put("sharedTrainingMaterial", customerAdoptionBean.getSharedTrainingMaterial());
            atMap.put("overallTrainingFeedbackComments", customerAdoptionBean.getOverallTrainingFeedbackComments());

        }
        componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
                task.getSiteType());
        processTaskLogDetails(task, "CLOSED", customerAdoptionBean.getDelayReason(), null, customerAdoptionBean);
        return (CustomerAdoptionBean) flowableBaseService.taskDataEntry(task, customerAdoptionBean);
    }

    @Transactional(readOnly = false)
    public CustomerHandoverBean customerHandover(CustomerHandoverBean customerHandoverBean)
            throws TclCommonException {
        Task task = getTaskByIdAndWfTaskId(customerHandoverBean.getTaskId(), customerHandoverBean.getWfTaskId());

        validateInputs(task, customerHandoverBean);

        Map<String, String> atMap = new HashMap<>();

        if (Objects.nonNull(customerHandoverBean)) {
            atMap.put("customerHandoverCompleted", customerHandoverBean.getCustomerHandoverCompleted());
            atMap.put("customerHandoverCompletedDate", customerHandoverBean.getCustomerHandoverCompletedDate());
        }
        componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
                task.getSiteType());
        processTaskLogDetails(task, "CLOSED", customerHandoverBean.getDelayReason(), null, customerHandoverBean);
        return (CustomerHandoverBean) flowableBaseService.taskDataEntry(task, customerHandoverBean);
    }

	@Transactional(readOnly = false)
	public AccessCodeActivationBean accessCodeActivation(AccessCodeActivationBean accessCode)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(accessCode.getTaskId(), accessCode.getWfTaskId());

		validateInputs(task, accessCode);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(accessCode)) {
			atMap.put("authCode", accessCode.getAuthCode());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, "CLOSED", accessCode.getDelayReason(), null, accessCode);
		return (AccessCodeActivationBean) flowableBaseService.taskDataEntry(task, accessCode);
	}

	@Transactional(readOnly = false)
	public ConfigAccessCodeBean configAccessCodeEndpoint(ConfigAccessCodeBean configAccessCode)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(configAccessCode.getTaskId(), configAccessCode.getWfTaskId());

		validateInputs(task, configAccessCode);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(configAccessCode)) {
			atMap.put("accessCodeConfigCompleted", configAccessCode.getAccessCodeConfigCompleted());
			atMap.put("accessCodeConfigDate", configAccessCode.getAccessCodeConfigDate());

		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, "CLOSED", configAccessCode.getDelayReason(), null, configAccessCode);
		return (ConfigAccessCodeBean) flowableBaseService.taskDataEntry(task, configAccessCode);
	}

	@Transactional(readOnly = false)
	public ComponentTestingBean componentTesting(ComponentTestingBean componentTestingBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(componentTestingBean.getTaskId(), componentTestingBean.getWfTaskId());

		validateInputs(task, componentTestingBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(componentTestingBean)) {
			atMap.put("scheduleTheMeeting", componentTestingBean.getScheduleTheMeeting());
			atMap.put("meetingPage", componentTestingBean.getMeetingPage());
			atMap.put("recording", componentTestingBean.getRecording());
			atMap.put("screenSharing", componentTestingBean.getScreenSharing());
			atMap.put("meetingVideoTesting", componentTestingBean.getMeetingVideoTesting());
			atMap.put("muteFunctionality", componentTestingBean.getMuteFunctionality());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, "CLOSED", componentTestingBean.getDelayReason(), null, componentTestingBean);
		return (ComponentTestingBean) flowableBaseService.taskDataEntry(task, componentTestingBean);
	}

	@Transactional(readOnly = false)
	public ServiceTestingBean serviceTesting(ServiceTestingBean serviceTestingBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(serviceTestingBean.getTaskId(), serviceTestingBean.getWfTaskId());

		validateInputs(task, serviceTestingBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(serviceTestingBean)) {
			atMap.put("scheduleTheMeeting", serviceTestingBean.getScheduleTheMeeting());
			atMap.put("e2eServiceTestingCompleteStatus", serviceTestingBean.getE2eServiceTestingCompleteStatus());
			atMap.put("meetingPage", serviceTestingBean.getMeetingPage());
			atMap.put("callInTeleconference", serviceTestingBean.getCallInTeleconference());
			atMap.put("callback", serviceTestingBean.getCallback());
			atMap.put("dedicatedNumber", serviceTestingBean.getDedicatedNumber());
			atMap.put("cugCalling", serviceTestingBean.getCugCalling());
			atMap.put("recording", serviceTestingBean.getRecording());
			atMap.put("screenSharing", serviceTestingBean.getScreenSharing());
			atMap.put("meetingVideoTesting", serviceTestingBean.getMeetingVideoTesting());
			atMap.put("cmrTesting", serviceTestingBean.getCmrTesting());
			
			if (serviceTestingBean.getDocumentIds() != null && !serviceTestingBean.getDocumentIds().isEmpty())
				serviceTestingBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		String commissioningDate =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		atMap.put("commissioningDate", commissioningDate);
		
		Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(task.getServiceId(), AttributeConstants.COMPONENT_LM, task.getSiteType());
		String billFreePeriod = scComponentAttributesmap.getOrDefault("billFreePeriod", "0");
		atMap.put("billStartDate", formatWithTimeStampForCommPlusDays(commissioningDate,StringUtils.isNoneBlank(billFreePeriod)?Integer.valueOf(billFreePeriod):0));
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, "CLOSED", serviceTestingBean.getDelayReason(), null, serviceTestingBean);
		return (ServiceTestingBean) flowableBaseService.taskDataEntry(task, serviceTestingBean);
	}

	@Transactional(readOnly = false)
	public PreparePrForEndpointBean preparePrForEndpointInstall(PreparePrForEndpointBean prForEndpoint)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(prForEndpoint.getTaskId(), prForEndpoint.getWfTaskId());

		validateInputs(task, prForEndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(prForEndpoint)) {
			atMap.put("endpointInstallationPrNumber", prForEndpoint.getEndpointInstallationPrNumber());
			atMap.put("endpointInstallationPrVendorName", prForEndpoint.getEndpointInstallationPrVendorName());
			atMap.put("endpointInstallationPrDate", prForEndpoint.getEndpointInstallationPrDate());
			atMap.put("endpointSupportPrNumber", prForEndpoint.getEndpointSupportPrNumber());
			atMap.put("endpointSupportPrVendorName", prForEndpoint.getEndpointSupportPrVendorName());
			atMap.put("endpointSupportPrDate", prForEndpoint.getEndpointSupportPrDate());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (prForEndpoint.getDocumentIds() != null && !prForEndpoint.getDocumentIds().isEmpty())
			prForEndpoint.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", prForEndpoint.getDelayReason(), null, prForEndpoint);
		return (PreparePrForEndpointBean) flowableBaseService.taskDataEntry(task, prForEndpoint);
	}

	@Transactional(readOnly = false)
	public CreatePoForEndpointBean createPoForEndpointInstall(CreatePoForEndpointBean poForEndpoint)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForEndpoint.getTaskId(), poForEndpoint.getWfTaskId());

		validateInputs(task, poForEndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poForEndpoint)) {
			atMap.put("endpointInstallationPoNumber", poForEndpoint.getEndpointInstallationPoNumber());
			atMap.put("endpointInstallationPoDate", poForEndpoint.getEndpointInstallationPoDate());
			atMap.put("endpointSupportPoNumber", poForEndpoint.getEndpointSupportPoNumber());
			atMap.put("endpointSupportPoDate", poForEndpoint.getEndpointSupportPoDate());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (poForEndpoint.getDocumentIds() != null && !poForEndpoint.getDocumentIds().isEmpty())
			poForEndpoint.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", poForEndpoint.getDelayReason(), null, poForEndpoint);
		return (CreatePoForEndpointBean) flowableBaseService.taskDataEntry(task, poForEndpoint);
	}

	@Transactional(readOnly = false)
	public ReleasePoForEndpointBean releasePoForEndpointInstall(ReleasePoForEndpointBean poForEndpointBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForEndpointBean.getTaskId(), poForEndpointBean.getWfTaskId());

		validateInputs(task, poForEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poForEndpointBean)) {
			atMap.put("poReleaseEndpointInstall", poForEndpointBean.getPoReleaseEndpointInstall());
			if (poForEndpointBean.getPoReleaseEndpointInstall().equalsIgnoreCase("yes")) {
				atMap.put("webexEndpointInstallPoStatus", "PO Released");
				atMap.put("endpointInstallPoReleaseCompletionDate", poForEndpointBean.getEndpointInstallPoReleaseCompletionDate());
			}
			atMap.put("poReleaseEndpointSupport", poForEndpointBean.getPoReleaseEndpointSupport());
			if (poForEndpointBean.getPoReleaseEndpointSupport().equalsIgnoreCase("yes")) {
				atMap.put("webexEndpointSupportPoStatus", "PO Released");
				atMap.put("endpointSupportPoReleaseCompletionDate", poForEndpointBean.getEndpointSupportPoReleaseCompletionDate());
			}
			atMap.put("endpointInstallPoReleaseCompletionDate", poForEndpointBean.getEndpointInstallPoReleaseCompletionDate());
			atMap.put("endpointSupportPoReleaseCompletionDate", poForEndpointBean.getEndpointSupportPoReleaseCompletionDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		processTaskLogDetails(task, "CLOSED", poForEndpointBean.getDelayReason(), null, poForEndpointBean);
		return (ReleasePoForEndpointBean) flowableBaseService.taskDataEntry(task, poForEndpointBean);
	}

	@Transactional(readOnly = false)
	public ConfirmMaterialAvailabilityBean confirmMaterialAvailability(
			ConfirmMaterialAvailabilityBean materialAvailability) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(materialAvailability.getTaskId(), materialAvailability.getWfTaskId());

		validateInputs(task, materialAvailability);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(materialAvailability)) {
			atMap.put("expectedETADate", materialAvailability.getExpectedETADate());
			atMap.put("grnNumber", materialAvailability.getGrnNumber());
			atMap.put("materialReceived", materialAvailability.getMaterialReceived());
			atMap.put("materialReceivedDate", materialAvailability.getMaterialReceivedDate());
			atMap.put("action", materialAvailability.getAction());
			if (materialAvailability.getSerialNumber() != null) {
				materialAvailability.getSerialNumber().stream().forEach(serialNumberBean -> {
					Map<String, String> atMap1 = new HashMap<>();
					atMap1.put("serialNumber", serialNumberBean.getSerialNumber());
					LOGGER.info("serialNumberId: {}, ATMAPvalue: {}", serialNumberBean.getSerialNumber(), serialNumberBean.getId());
					componentAndAttributeService.updateAttributesEndpoint(task.getServiceId(), serialNumberBean.getId(),
							atMap1, task.getSiteType());
				});
			}
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, "CLOSED", materialAvailability.getDelayReason(), null, materialAvailability);
		return (ConfirmMaterialAvailabilityBean) flowableBaseService.taskDataEntry(task, materialAvailability);
	}

	@Transactional(readOnly = false)
	public DispatchEndpointBean dispatchEndpoint(DispatchEndpointBean dispatchEndpointBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(dispatchEndpointBean.getTaskId(), dispatchEndpointBean.getWfTaskId());

		validateInputs(task, dispatchEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(dispatchEndpointBean)) {
			atMap.put("endpointMrnNumber", dispatchEndpointBean.getEndpointMrnNumber());
			atMap.put("endpointMinNumber", dispatchEndpointBean.getEndpointMinNumber());
			atMap.put("courierDispatchVendorName", dispatchEndpointBean.getCourierDispatchVendorName());
			atMap.put("courierTrackNumber", dispatchEndpointBean.getCourierTrackNumber());
			atMap.put("endpointDispatchDate", dispatchEndpointBean.getEndpointDispatchDate());
			atMap.put("distributionCenterName", dispatchEndpointBean.getDistributionCenterName());
			atMap.put("distributionCenterAddress", dispatchEndpointBean.getDistributionCenterAddress());
			atMap.put("endpointSerialNumber", dispatchEndpointBean.getEndpointSerialNumber());
			if (Objects.nonNull(dispatchEndpointBean.getSerialNumber())) {
				dispatchEndpointBean.getSerialNumber().stream().forEach(serialNumberBean -> {
					Map<String, String> atMap1 = new HashMap<>();
					atMap1.put("endpointDeliveryDate", serialNumberBean.getDeliveryDate());
					atMap1.put("endpointEndOfSale", serialNumberBean.getEndOfSale());
					atMap1.put("endpointEndOfLife", serialNumberBean.getEndOfLife());
					componentAndAttributeService.updateAttributesEndpoint(task.getServiceId(), serialNumberBean.getId(),
							atMap1, task.getSiteType());
				});
			}
			if (dispatchEndpointBean.getDocumentIds() != null && !dispatchEndpointBean.getDocumentIds().isEmpty())
				dispatchEndpointBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		processTaskLogDetails(task, "CLOSED", dispatchEndpointBean.getDelayReason(), null, dispatchEndpointBean);
		return (DispatchEndpointBean) flowableBaseService.taskDataEntry(task, dispatchEndpointBean);
	}

	@Transactional(readOnly = false)
	public TrackEndpointBean trackEndpoint(TrackEndpointBean trackEndpointBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(trackEndpointBean.getTaskId(), trackEndpointBean.getWfTaskId());

		validateInputs(task, trackEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(trackEndpointBean)) {
			atMap.put("deliveryStatus", trackEndpointBean.getDeliveryStatus());
			atMap.put("endpointDeliveryDate", trackEndpointBean.getEndpointDeliveryDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		processTaskLogDetails(task, "CLOSED", trackEndpointBean.getDelayReason(), null, trackEndpointBean);
		return (TrackEndpointBean) flowableBaseService.taskDataEntry(task, trackEndpointBean);
	}

	@Transactional(readOnly = false)
	public AdvancedEnrichmentBean advancedEnrichmentForWebex(AdvancedEnrichmentBean advancedEnrichment)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(advancedEnrichment.getTaskId(), advancedEnrichment.getWfTaskId());

		validateInputs(task, advancedEnrichment);

		Map<String, String> atMap = new HashMap<>();
		Map<String, Object> fMap = new HashMap<>();
		
		atMap.put("advanceEnrichmentTaskId", advancedEnrichment.getTaskId().toString());
		atMap.put("advanceEnrichmentWfTaskId", advancedEnrichment.getWfTaskId());

		if (Objects.nonNull(advancedEnrichment)) {
			atMap.put("trainingAccessWebex", advancedEnrichment.getTrainingAccessWebex());
			atMap.put("contactName", advancedEnrichment.getContactName());
			atMap.put("contactTitle", advancedEnrichment.getContactTitle());
			atMap.put("emailAddress", advancedEnrichment.getEmailAddress());
			atMap.put("officePhone", advancedEnrichment.getOfficePhone());
			atMap.put("mobilePhone", advancedEnrichment.getMobilePhone());
			atMap.put("businessStartTime", advancedEnrichment.getBusinessStartTime());
			atMap.put("businessEndTime", advancedEnrichment.getBusinessEndTime());
			atMap.put("timeZone", advancedEnrichment.getTimeZone());
			atMap.put("numberOfUsers", advancedEnrichment.getNumberOfUsers());
			atMap.put("numberOfTrainings", advancedEnrichment.getNumberOfTrainings());
			atMap.put("nameOfTrainings", advancedEnrichment.getNameOfTrainings());
			atMap.put("numberOfAdmin", advancedEnrichment.getNumberOfAdmin());
			atMap.put("trainingTimeZone", advancedEnrichment.getTrainingTimeZone());
			atMap.put("orderSituation", advancedEnrichment.getOrderSituation());
			atMap.put("ccaSpTelephonyFeatures", advancedEnrichment.getCcaSpTelephonyFeatures());
			atMap.put("ssoConfiguration", advancedEnrichment.getSsoConfiguration());
			atMap.put("addUserMethod", advancedEnrichment.getAddUserMethod());
			atMap.put("directoryConnector", advancedEnrichment.getDirectoryConnector());
			atMap.put("adFileSyncNeeded", advancedEnrichment.getAdFileSyncNeeded());
			if(Objects.nonNull(advancedEnrichment.getMicrositeDetails())) {
				atMap.put("micrositeDetails", Utils.convertObjectToJson(advancedEnrichment.getMicrositeDetails()));
			}
			if(advancedEnrichment.getDirectoryConnector().equalsIgnoreCase("yes") || advancedEnrichment.getAdFileSyncNeeded().equalsIgnoreCase("yes") || advancedEnrichment.getSsoConfiguration().equalsIgnoreCase("yes")) {
				fMap.put("isAdfs", "yes");
				atMap.put("isAdfs", "yes");
			}
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		if (advancedEnrichment.getDocumentIds() != null && !advancedEnrichment.getDocumentIds().isEmpty())
			advancedEnrichment.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", advancedEnrichment.getDelayReason(), null, advancedEnrichment);
		return (AdvancedEnrichmentBean) flowableBaseService.taskDataEntry(task, advancedEnrichment, fMap);
	}



	/**
	 *
	 * Provide WBSGLCC details.
	 *
	 * @param taskId
	 * @param provideWbsglccDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvideWbsglccWebexDetailBean provideWbsglccDetails(ProvideWbsglccWebexDetailBean provideWbsglccDetailBean,
			String type) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(provideWbsglccDetailBean.getTaskId(),
				provideWbsglccDetailBean.getWfTaskId());
		validateInputs(task, provideWbsglccDetailBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("level5Wbs", provideWbsglccDetailBean.getLevel5Wbs());
		atMap.put("demandIdNo", provideWbsglccDetailBean.getDemandIdNo());
		atMap.put("glCode", provideWbsglccDetailBean.getGlCode());
		atMap.put("costCenter", provideWbsglccDetailBean.getCostCenter());
		atMap.put("supportDemandIdNo", provideWbsglccDetailBean.getSupportDemandIdNo());
		atMap.put("supportGlCode", provideWbsglccDetailBean.getSupportGlCode());
		atMap.put("supportCostCenter", provideWbsglccDetailBean.getSupportCostCenter());
		atMap.put("licenceDemandIdNo", provideWbsglccDetailBean.getLicenceDemandIdNo());
		atMap.put("licenceGlCode", provideWbsglccDetailBean.getLicenceGlCode());
		atMap.put("licenceCostCenter", provideWbsglccDetailBean.getLicenceCostCenter());

		String endpointDeliveryRequired = provideWbsglccDetailBean.getEndpointDeliveryRequired();
		atMap.put("endpointDeliveryRequired", provideWbsglccDetailBean.getEndpointDeliveryRequired());
		Map<String, Object> flowableMap = new HashMap<>();

		if (endpointDeliveryRequired.equalsIgnoreCase("no")) {
			flowableMap.put("endpointSiScope", "");

			atMap.put("endpointDeliveryRequiredRejectionReason",
					provideWbsglccDetailBean.getEndpointDeliveryRequiredRejectionReason());
			flowableMap.put("isEndpointArrangedByCustomer", true);

			Map<String, String> scAtMap = new HashMap<>();
			scAtMap.put("endpoint_chassis_changed", "No");
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(), scAtMap);
		}

		if (type.equalsIgnoreCase("license")) {
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
					AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
		} else {
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
					task.getSiteType());
		}
		processTaskLogDetails(task, "CLOSED", provideWbsglccDetailBean.getDelayReason(), null,
				provideWbsglccDetailBean);
		return (ProvideWbsglccWebexDetailBean) flowableBaseService.taskDataEntry(task, provideWbsglccDetailBean,
				flowableMap);
	}

	/**
	 * This method is used to generate Webex Endpoint Invoice.
	 *
	 * @param generateWebexEndpointInvoiceBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public GenerateWebexEndpointInvoiceBean generateWebexEndpointInvoice(
			GenerateWebexEndpointInvoiceBean generateWebexEndpointInvoiceBean) throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(generateWebexEndpointInvoiceBean.getTaskId(),
				generateWebexEndpointInvoiceBean.getWfTaskId());
		validateInputs(task, generateWebexEndpointInvoiceBean);

		if (generateWebexEndpointInvoiceBean.getDocumentIds() != null
				&& !generateWebexEndpointInvoiceBean.getDocumentIds().isEmpty())
			generateWebexEndpointInvoiceBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task, "CLOSED", generateWebexEndpointInvoiceBean.getDelayReason(), null,
				generateWebexEndpointInvoiceBean);
		return (GenerateWebexEndpointInvoiceBean) flowableBaseService.taskDataEntry(task,
				generateWebexEndpointInvoiceBean);
	}

	/**
	 * This method is used to Generate MRN for Webex Endpoint Transfer
	 *
	 * @param generateMrnforWebexEndpointTransferBean
	 * @throws TclCommonException
	 */
	/*
	 * @Transactional(readOnly = false) public
	 * GenerateMrnforWebexEndpointTransferBean generateMrnforWebexEndpointTransfer(
	 * GenerateMrnforWebexEndpointTransferBean
	 * generateMrnforWebexEndpointTransferBean) throws TclCommonException{
	 * 
	 * Task task =
	 * getTaskByIdAndWfTaskId(generateMrnforWebexEndpointTransferBean.getTaskId(),
	 * generateMrnforWebexEndpointTransferBean.getWfTaskId()); validateInputs(task,
	 * generateMrnforWebexEndpointTransferBean);
	 * 
	 * if (generateMrnforWebexEndpointTransferBean.getDocumentIds() != null &&
	 * !generateMrnforWebexEndpointTransferBean.getDocumentIds().isEmpty())
	 * generateMrnforWebexEndpointTransferBean.getDocumentIds()
	 * .forEach(attachmentIdBean -> makeEntryInScAttachment(task,
	 * attachmentIdBean.getAttachmentId()));
	 * processTaskLogDetails(task,"CLOSED",generateMrnforWebexEndpointTransferBean.
	 * getDelayReason(),null); return (GenerateMrnforWebexEndpointTransferBean)
	 * flowableBaseService.taskDataEntry(task,
	 * generateMrnforWebexEndpointTransferBean); }
	 */

	/**
	 * This method is used to Install Endpoint
	 *
	 * @param installEndpointBean
	 * @return installEndpointBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public InstallEndpointBean installEndpoint(InstallEndpointBean installEndpointBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(installEndpointBean.getTaskId(), installEndpointBean.getWfTaskId());
		validateInputs(task, installEndpointBean);
		if (installEndpointBean.getDocumentIds() != null && !installEndpointBean.getDocumentIds().isEmpty())
			installEndpointBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		Map<String, String> atMap = new HashMap<>();
		atMap.put("endpointCardSerialNumber", installEndpointBean.getEndpointCardSerialNumber());
		atMap.put("dateOfEndpointInstallation", installEndpointBean.getDateOfEndpointInstallation());
		atMap.put("endpointAmcStartDate", installEndpointBean.getEndpointAmcStartDate());
		atMap.put("endpointAmcEndDate", installEndpointBean.getEndpointAmcEndDate());
		atMap.put("endpointConsoleCableConnected", installEndpointBean.getEndpointConsoleCableConnected());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		if (Objects.nonNull(installEndpointBean.getSerialNumber())) {
			installEndpointBean.getSerialNumber().stream().forEach(serialNumberBean -> {
				Map<String, String> atMap1 = new HashMap<>();
//				atMap1.put("endpointDeliveryDate", serialNumberBean.getDeliveryDate());
//				atMap1.put("endpointEndOfSale", serialNumberBean.getEndOfSale());
//				atMap1.put("endpointEndOfLife", serialNumberBean.getEndOfLife());
				atMap1.put("endpointAmcStartDate", serialNumberBean.getAmcStartDate());
				atMap1.put("endpointAmcEndDate", serialNumberBean.getAmcEndDate());
				componentAndAttributeService.updateAttributesEndpoint(task.getServiceId(), serialNumberBean.getId(),
						atMap1, task.getSiteType());
			});
		}
		processTaskLogDetails(task, "CLOSED", installEndpointBean.getDelayReason(), null, installEndpointBean);
		return (InstallEndpointBean) flowableBaseService.taskDataEntry(task, installEndpointBean);
	}

	/**
	 * This method is used to Activate Microsite
	 *
	 * @param ActivateMicrositeBean
	 * @throws TclCommonException
	 */

	@Transactional(readOnly = false)
	public ActivateMicrositeBean activateMicrosite(ActivateMicrositeBean activateMicrosite) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(activateMicrosite.getTaskId(), activateMicrosite.getWfTaskId());

		validateInputs(task, activateMicrosite);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(activateMicrosite) && !activateMicrosite.getMicrositeDetails().isEmpty()) {
			atMap.put("micrositeDetails", Utils.convertObjectToJson(activateMicrosite.getMicrositeDetails()));
		}
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", activateMicrosite.getDelayReason(), null, activateMicrosite);
		return (ActivateMicrositeBean) flowableBaseService.taskDataEntry(task, activateMicrosite);
	}

	/**
	 * This method is used to Td Creation for Dedicated Numbers, Td Creation for
	 * Shared Numbers
	 *
	 * @param ActivateMicrositeBean
	 * @throws TclCommonException
	 */

	@Transactional(readOnly = false)
	public TdCreationDedicationNumberBean tdCreationDedicatedNumbers(
			TdCreationDedicationNumberBean tdCreationDedicationNumberBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(tdCreationDedicationNumberBean.getTaskId(),
				tdCreationDedicationNumberBean.getWfTaskId());

		validateInputs(task, tdCreationDedicationNumberBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(tdCreationDedicationNumberBean)) {
			atMap.put("telephonyDomain", tdCreationDedicationNumberBean.getTelephonyDomain());
			atMap.put("routingProfile", tdCreationDedicationNumberBean.getRoutingProfile());
			atMap.put("cluster", tdCreationDedicationNumberBean.getCluster());
			atMap.put("phoneNumbersA", tdCreationDedicationNumberBean.getPhoneNumbersA());
			atMap.put("creationDScompletionDate", tdCreationDedicationNumberBean.getCreationDScompletionDate());
			

		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", tdCreationDedicationNumberBean.getDelayReason(), null,
				tdCreationDedicationNumberBean);
		return (TdCreationDedicationNumberBean) flowableBaseService.taskDataEntry(task, tdCreationDedicationNumberBean);
	}

	/**
	 * This method is used to Creation of Callback Groups
	 *
	 * @param ActivateMicrositeBean
	 * @throws TclCommonException
	 */

	@Transactional(readOnly = false)
	public CreationCallbackGroupsBean creationCallbackGroups(CreationCallbackGroupsBean creationCallbackGroupsBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(creationCallbackGroupsBean.getTaskId(),
				creationCallbackGroupsBean.getWfTaskId());

		validateInputs(task, creationCallbackGroupsBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(creationCallbackGroupsBean)) {
			atMap.put("creationOfCallbackGroupCompleted",
					creationCallbackGroupsBean.getCreationOfCallbackGroupCompleted());
			atMap.put("callbackCreationCompletionDate",
					creationCallbackGroupsBean.getCallbackCreationCompletionDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", creationCallbackGroupsBean.getDelayReason(), null,
				creationCallbackGroupsBean);
		return (CreationCallbackGroupsBean) flowableBaseService.taskDataEntry(task, creationCallbackGroupsBean);
	}

	/**
	 * This method is used to Egress Routing Profile
	 *
	 * @param ActivateMicrositeBean
	 * @throws TclCommonException
	 */

	@Transactional(readOnly = false)
	public EgressRoutingProfileBean egressRoutingProfile(EgressRoutingProfileBean egressRoutingProfileBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(egressRoutingProfileBean.getTaskId(),
				egressRoutingProfileBean.getWfTaskId());

		validateInputs(task, egressRoutingProfileBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(egressRoutingProfileBean)) {
			atMap.put("ccaInventoryUpdate", egressRoutingProfileBean.getCcaInventoryUpdate());
			atMap.put("ccaInventoryUpdateChangeRequestNumber",
					egressRoutingProfileBean.getCcaInventoryUpdateChangeRequestNumber());
			atMap.put("egressRoutingCompletionDate", egressRoutingProfileBean.getEgressRoutingCompletionDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
		
		List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByScOrderIdAndErfCatalogProductName(task.getOrderCode(), "UCAAS");
		for(ScServiceDetail serviceDetail : serviceDetails) {
			if(serviceDetail.getErfPrdCatalogOfferingName().equalsIgnoreCase(AttributeConstants.WEBEX_LICENSE_OFFERNING_NAME)) {
				atMap = new HashMap<>();
				ScComponent scComponent = scComponentRepository
						.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(), AttributeConstants.COMPONENT_LM, task.getSiteType());
				if (scComponent != null && scComponent.getScComponentAttributes() != null && !scComponent.getScComponentAttributes().isEmpty()) {
					for (ScComponentAttribute compAttr : scComponent.getScComponentAttributes()) {
						atMap.put(compAttr.getAttributeName(), compAttr.getAttributeValue());
					}
				}
				componentAndAttributeService.updateAttributes(serviceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
			}
		}

		processTaskLogDetails(task, "CLOSED", egressRoutingProfileBean.getDelayReason(), null,
				egressRoutingProfileBean);
		return (EgressRoutingProfileBean) flowableBaseService.taskDataEntry(task, egressRoutingProfileBean);
	}

	/**
	 * This method is used to Configure Endpoint
	 *
	 * @param configureEndpointBean
	 * @return configureEndpointBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ConfigureEndpointBean configureEndpoint(ConfigureEndpointBean configureEndpointBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(configureEndpointBean.getTaskId(), configureEndpointBean.getWfTaskId());
		validateInputs(task, configureEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(configureEndpointBean)) {
			atMap.put("endpointConfigurationCompleted", configureEndpointBean.getConfigurationCompleted());
			atMap.put("endpointConfigurationDate", configureEndpointBean.getConfigurationDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (configureEndpointBean.getDocumentIds() != null && !configureEndpointBean.getDocumentIds().isEmpty())
			configureEndpointBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task, "CLOSED", configureEndpointBean.getDelayReason(), null, configureEndpointBean);
		return (ConfigureEndpointBean) flowableBaseService.taskDataEntry(task, configureEndpointBean);
	}

	/**
	 * This method is used to Activate Voice to Microsite
	 *
	 * @param ActivateVoiceMicrositeBean
	 * @return ActivateVoiceMicrositeBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ActivateVoiceMicrositeBean activeVoiceToMicrosite(ActivateVoiceMicrositeBean activateVoiceMicrositeBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(activateVoiceMicrositeBean.getTaskId(),
				activateVoiceMicrositeBean.getWfTaskId());
		validateInputs(task, activateVoiceMicrositeBean);

		if (activateVoiceMicrositeBean.getDocumentIds() != null
				&& !activateVoiceMicrositeBean.getDocumentIds().isEmpty())
			activateVoiceMicrositeBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		Map<String, String> atMap = new HashMap<>();
		if (Objects.nonNull(activateVoiceMicrositeBean)) {
			atMap.put("emailConfirmationDate", activateVoiceMicrositeBean.getEmailConfirmationDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
		processTaskLogDetails(task, "CLOSED", activateVoiceMicrositeBean.getDelayReason(), null,
				activateVoiceMicrositeBean);
		return (ActivateVoiceMicrositeBean) flowableBaseService.taskDataEntry(task, activateVoiceMicrositeBean);
	}

	/**
	 * This method is used to Hybrid Services Implementation with Webex
	 *
	 * @param ActivateVoiceMicrositeBean
	 * @return ActivateVoiceMicrositeBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public HybridServiceImplementationBean hybridServiceImplementation(
			HybridServiceImplementationBean hybridServiceImplementationBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(hybridServiceImplementationBean.getTaskId(),
				hybridServiceImplementationBean.getWfTaskId());
		validateInputs(task, hybridServiceImplementationBean);

		Map<String, String> atMap = new HashMap<>();
		if (Objects.nonNull(hybridServiceImplementationBean)) {
			if (hybridServiceImplementationBean.getDcDeployment() != null) {
				atMap.put("dcDeployment", hybridServiceImplementationBean.getDcDeployment());
			}

			if (hybridServiceImplementationBean.getAdContainerForUserSync() != null) {
				atMap.put("adContainerForUserSync", hybridServiceImplementationBean.getAdContainerForUserSync());
			}

			if (hybridServiceImplementationBean.getSso() != null) {
				atMap.put("sso", hybridServiceImplementationBean.getSso());
			}

			if (hybridServiceImplementationBean.getCc() != null) {
				atMap.put("cc", hybridServiceImplementationBean.getCc());
			}

			if (hybridServiceImplementationBean.getCallService() != null) {
				atMap.put("callService", hybridServiceImplementationBean.getCallService());
			}

			if (hybridServiceImplementationBean.getVideoMesh() != null) {
				atMap.put("videoMesh", hybridServiceImplementationBean.getVideoMesh());
			}

			if (hybridServiceImplementationBean.getDataSecurity() != null) {
				atMap.put("dataSecurity", hybridServiceImplementationBean.getDataSecurity());
			}
			if (hybridServiceImplementationBean.getMessageServices() != null) {
				atMap.put("messageServices", hybridServiceImplementationBean.getMessageServices());
			}

			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
					AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
		}

		processTaskLogDetails(task, "CLOSED", hybridServiceImplementationBean.getDelayReason(), null,
				hybridServiceImplementationBean);
		return (HybridServiceImplementationBean) flowableBaseService.taskDataEntry(task,
				hybridServiceImplementationBean);
	}

	@Transactional(readOnly = false)
	public SrnGenerationEndpointBean srnGenerationEndpoint(SrnGenerationEndpointBean srnGenerationEndpointBean)
			throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(srnGenerationEndpointBean.getTaskId(),
				srnGenerationEndpointBean.getWfTaskId());

		validateInputs(task, srnGenerationEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(srnGenerationEndpointBean)) {
			atMap.put("srnDate", srnGenerationEndpointBean.getSrnDate());

		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (srnGenerationEndpointBean.getDocumentIds() != null && !srnGenerationEndpointBean.getDocumentIds().isEmpty())
			srnGenerationEndpointBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", srnGenerationEndpointBean.getDelayReason(), null,
				srnGenerationEndpointBean);
		return (SrnGenerationEndpointBean) flowableBaseService.taskDataEntry(task, srnGenerationEndpointBean);
	}
	
	/*** Customer On boarding for Training ***/
	@Transactional(readOnly = false)
	public CustomerOnBoardTranningBean customerOnBoardTranning(CustomerOnBoardTranningBean customerOnBoardTranningBean) throws TclCommonException {
		 
		Task task = getTaskByIdAndWfTaskId(customerOnBoardTranningBean.getTaskId(), customerOnBoardTranningBean.getWfTaskId());
		validateInputs(task, customerOnBoardTranningBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(customerOnBoardTranningBean)) {
			atMap.put("customerBoardingDate",customerOnBoardTranningBean.getCustomerBoardingDate());
			atMap.put("customerBoardingCompleted", customerOnBoardTranningBean.getCustomerBoardingCompleted());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		
		processTaskLogDetails(task, "CLOSED", customerOnBoardTranningBean.getDelayReason(), null, customerOnBoardTranningBean);
		return (CustomerOnBoardTranningBean) flowableBaseService.taskDataEntry(task, customerOnBoardTranningBean);
	}
	
	/*** Update CUG numbers in CCA Portal ***/
	@Transactional(readOnly = false)
	public CustomerOnBoardTranningBean updateCUGNumberInCCA(UpdateCugInCCABean updateCugInCCABean) throws TclCommonException {
		 
		Task task = getTaskByIdAndWfTaskId(updateCugInCCABean.getTaskId(), updateCugInCCABean.getWfTaskId());
		validateInputs(task, updateCugInCCABean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(updateCugInCCABean)) {
			atMap.put("updateCugNumbersInCcw", updateCugInCCABean.getUpdateCugNumbersInCcw());
			atMap.put("updateCugcompletionDate", updateCugInCCABean.getUpdateCugcompletionDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		
		processTaskLogDetails(task, "CLOSED", updateCugInCCABean.getDelayReason(), null, updateCugInCCABean);
		return (CustomerOnBoardTranningBean) flowableBaseService.taskDataEntry(task, updateCugInCCABean);
	}

	@Transactional(readOnly = false)
	public List<ComponentAttributeBean> getWebexCompAttributeDetails(Integer serviceId) throws TclCommonException {
		
		List<ComponentAttributeBean> compoAttrList=new ArrayList<ComponentAttributeBean>();
		List<ScComponent> scComponentList=scComponentRepository.findByScServiceDetailIdAndSiteType(serviceId,"A");
		if(Objects.nonNull(scComponentList) && !scComponentList.isEmpty()) {
		for(ScComponent scComponent:scComponentList) {
			ComponentAttributeBean componentAttrBean=new ComponentAttributeBean();
			Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(serviceId,scComponent.getComponentName(), "A");
			componentAttrBean.setComponentId(scComponent.getId());
			componentAttrBean.setComponentName(scComponent.getComponentName());
			componentAttrBean.setComponentAttributes(scComponentAttributesmap);
			compoAttrList.add(componentAttrBean);
			
	    	}
    	}
		return compoAttrList;
	}
	
	public static String formatWithTimeStampForCommPlusDays(String commDate, Integer days) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusDays(days);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(out);

	}
	
	@Transactional(readOnly = false)
	public List<SolutionViewDetailsBean> getSolutionDetailsServices(String solutionCode) throws TclCommonException {
		SolutionViewDetailsBean solutionViewDetailsBean = new SolutionViewDetailsBean();
		List<ScSolutionComponent> scSolutionComponent = scSolutionComponentRepository.findAllBySolutionCodeAndIsActive(solutionCode, "Y");
		List<String> scOrderList = scSolutionComponentRepository.findDistinctOrderBySolutionCodeAndIsActive(solutionCode, "Y");
		List<ScOrder> scOrders = scOrderRepository.findByOpOrderCodeInAndIsActive(scOrderList, "Y");
		List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findAllByScOrderUuidIn(scOrderList);
		List<Integer> serviceIds = new ArrayList<Integer>();
		Map<String, ScOrder> scOrderMap = new HashMap<>();
		if(scOrders!=null && !scOrders.isEmpty()) {
			for (ScOrder scOrder : scOrders){
				scOrderMap.put(scOrder.getOpOrderCode(), scOrder);
			}
		}
		
		Map<String, ScServiceDetail> scServiceDetailMap = new HashMap<>();
		if(scServiceDetailList !=null && !scServiceDetailList.isEmpty()) {
			for (ScServiceDetail scServiceDetail:scServiceDetailList) {
				scServiceDetailMap.put(scServiceDetail.getUuid(), scServiceDetail);
				serviceIds.add(scServiceDetail.getId());
			}
		}
		
		List<StagePlan> stagePlanList = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceIds);
		Map<String, StagePlan> stagePlanMap = new HashMap<>();
		if(stagePlanList !=null && !stagePlanList.isEmpty()) {
			for (StagePlan stagePlan:stagePlanList) {
				stagePlanMap.put(stagePlan.getServiceId() + "_" + stagePlan.getMstStageDef().getKey(), stagePlan);
			}
		}
		
		List<SolutionViewDetailsBean> solutionViewDetailsList = new ArrayList<SolutionViewDetailsBean>();
		List<OrderSolutionViewBean> orderSolutionViewList = new ArrayList<OrderSolutionViewBean>();
		
		LOGGER.info("Solution Orders List orderList:{} and ServiceDetailList:{}", scOrderList, scServiceDetailList);
		for(String scOrderCode: scOrderList) {
			OrderSolutionViewBean orderSolutionViewBean = new OrderSolutionViewBean();
			orderSolutionViewBean = getOrderDetails(scSolutionComponent, orderSolutionViewBean, scOrderMap, solutionCode, scOrderCode, scServiceDetailMap, stagePlanMap);
			orderSolutionViewList.add(orderSolutionViewBean);	
		}
		solutionViewDetailsBean.setOrders(orderSolutionViewList);
		solutionViewDetailsList.add(solutionViewDetailsBean);
		
		return solutionViewDetailsList;
	}
	
	private OrderSolutionViewBean getOrderDetails(List<ScSolutionComponent> scSolutionComponent, OrderSolutionViewBean orderSolutionViewBean, Map<String, ScOrder> scOrderMap, String solutionCode, String scOrderCode, Map<String, ScServiceDetail> scServiceDetailMap, Map<String, StagePlan> stagePlanMap) {
		ScOrder scOrder = scOrderMap.get(scOrderCode);
		orderSolutionViewBean.setOrderCode(scOrder.getUuid());
		orderSolutionViewBean.setOrderId(scOrder.getErfOrderId());
		orderSolutionViewBean.setOrderLeId(scOrder.getErfOrderLeId());
		LOGGER.info("Solution Component List component:{} and ServiceDetailMAP:{}", scSolutionComponent, scServiceDetailMap);
		List<ServiceSolutionViewBean> serviceSolutionViewList = new ArrayList<ServiceSolutionViewBean>();
		scSolutionComponent.stream().filter(el-> el.getParentServiceCode().equals(solutionCode)).forEach(service -> {
			ServiceSolutionViewBean serviceSolutionViewBean = new ServiceSolutionViewBean();
			serviceSolutionViewBean = getServiceDetails(scSolutionComponent, serviceSolutionViewBean, scServiceDetailMap, stagePlanMap, service.getServiceCode());
			serviceSolutionViewList.add(serviceSolutionViewBean);
		});
		orderSolutionViewBean.setServiceDetails(serviceSolutionViewList);
		return orderSolutionViewBean;
	}
	
	private ServiceSolutionViewBean getServiceDetails(List<ScSolutionComponent> scSolutionComponent, ServiceSolutionViewBean serviceSolutionViewBean, Map<String, ScServiceDetail> scServiceDetailMap, Map<String, StagePlan> stagePlanMap, String serviceCode) {
		ScServiceDetail ssd = scServiceDetailMap.get(serviceCode);
		if(ssd !=null) {
			StagePlan stageStartDate = stagePlanMap.get(ssd.getId() + "_" + AttributeConstants.ORDER_ENRICHMENT_STAGE);
			StagePlan stageEndDate = stagePlanMap.get(ssd.getId() + "_" + AttributeConstants.ORDER_EXPERIENCE_SURVEY_STAGE);
			serviceSolutionViewBean.setServiceId(ssd.getId());
			serviceSolutionViewBean.setServiceCode(ssd.getUuid());
			serviceSolutionViewBean.setProductName(ssd.getErfPrdCatalogProductName());
			serviceSolutionViewBean.setOfferingName(ssd.getErfPrdCatalogOfferingName());
			if(stageStartDate != null) {
				serviceSolutionViewBean.setStartDate(stageStartDate.getPlannedStartTime());
			}
			if(stageEndDate != null) {
				serviceSolutionViewBean.setEndDate(stageEndDate.getPlannedEndTime());
			}
			List<ServiceSolutionViewBean> childServiceSolutionViewList = new ArrayList<ServiceSolutionViewBean>();
			scSolutionComponent.stream().filter(el-> el.getParentServiceCode().equals(ssd.getUuid())).forEach(childService -> {
				ServiceSolutionViewBean childServiceSolutionViewBean = new ServiceSolutionViewBean();
				ScServiceDetail childSsd = scServiceDetailMap.get(childService.getServiceCode());
				if(childSsd !=null)
				childServiceSolutionViewBean.setServiceId(childSsd.getId());
				childServiceSolutionViewBean.setServiceCode(childSsd.getUuid());
				childServiceSolutionViewBean.setProductName(childSsd.getErfPrdCatalogProductName());
				childServiceSolutionViewBean.setOfferingName(childSsd.getErfPrdCatalogOfferingName());
				childServiceSolutionViewList.add(childServiceSolutionViewBean);
			});
			serviceSolutionViewBean.setServiceDetails(childServiceSolutionViewList);
		}
		
		return serviceSolutionViewBean;
		
	}
//	For future use recursive service details.
//	private List<ServiceSolutionViewBean> getNestedServiceDetails(List<ScSolutionComponent> scSolutionComponent, String serviceCode, Map<String, ScServiceDetail> scServiceDetailMap, ServiceSolutionViewBean serviceSolutionViewBean){
//		List<ServiceSolutionViewBean> serviceSolutionViewList = new ArrayList<ServiceSolutionViewBean>();
//		scSolutionComponent.stream().filter(el-> el.getParentServiceCode().equals(serviceCode)).forEach(service -> {
//			ScServiceDetail ssd = scServiceDetailMap.get(service.getServiceCode());
//			ServiceSolutionViewBean ssvb= new ServiceSolutionViewBean();
//			if(ssd !=null) {
//				ssvb.setServiceId(ssd.getId());
//				ssvb.setServiceCode(ssd.getUuid());
//				ssvb.setProductName(ssd.getErfPrdCatalogProductName());
//				serviceSolutionViewList.add(ssvb);
//				List<ScSolutionComponent> scSolComp = scSolutionComponent.stream().filter(el-> el.getParentServiceCode().equals(ssd.getUuid())).collect(Collectors.toList());
//				if(scSolComp !=null && scSolComp.size() > 0) {
//					getNestedServiceDetails(scSolutionComponent, ssd.getUuid(), scServiceDetailMap, ssvb);
//				}
//			}
//			ssvb.setServiceDetails(serviceSolutionViewList);
//		});
//		serviceSolutionViewList.add(serviceSolutionViewBean);
//		return serviceSolutionViewList;
//	}
}
