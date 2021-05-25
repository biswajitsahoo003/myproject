package com.tcl.dias.servicefulfillment.service.gsc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.GscCommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAssetAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrAssetBean;
import com.tcl.dias.common.fulfillment.beans.OdrAssetRelationBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.CreateSiteRepcRequest;
import com.tcl.dias.servicefulfillment.beans.RaiseJeopardy;
import com.tcl.dias.servicefulfillment.beans.SIPAttributes;
import com.tcl.dias.servicefulfillment.beans.ServiceAttributes;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingProfileApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingProfileCmsIdBean;
import com.tcl.dias.servicefulfillment.beans.gsc.BridgeDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ChangeConfigOutpulseDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CircuitCreationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ClosingTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CommVettingApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CommercialVettingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CreateSiteRequestBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DIDNumberTestBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DIDServiceAcceptenceBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DidNumberRepcJeopardyBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DidNumberTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DidPortingNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DocumentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.EntmmApprovalDocBean;
import com.tcl.dias.servicefulfillment.beans.gsc.EntmmTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.EntmmValidationApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscChildServiceBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscChildServiceDetailBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscSipTrunkConfigApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscSipTrunkRouteLabelCreationApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.NumbersBean;
import com.tcl.dias.servicefulfillment.beans.gsc.OutpulseDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.PlaceOrderToSuppliersBean;
import com.tcl.dias.servicefulfillment.beans.gsc.PortingNumberUpdateBean;
import com.tcl.dias.servicefulfillment.beans.gsc.PortingNumberUpdateListBean;
import com.tcl.dias.servicefulfillment.beans.gsc.PostTestNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ProvisioningValidationApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RateUploadApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RateUploadBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RemoveNumberTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RepcDBCreationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RoutingNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ScAssetBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SelectedSite;
import com.tcl.dias.servicefulfillment.beans.gsc.SelectedSiteBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SelectedSuppliersBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ServiceAcceptanceBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ServiceAcceptanceBillingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SiteDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SupplierResponseBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SupplierResponseDetailBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TaxationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TestNumberBasicEnrichBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TestNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TestNumberWithRoutingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.UifnProcurementListBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ValidateDocumentApprovalAllBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ValidateDocumentApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ValidateDocumentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ViewRoutingNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceBasicEnrichmentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceSalesEngrApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceSalesEngrBean;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.custom.IGscScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroupToAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetRelation;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.FlowGroupAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupRepository;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupToAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRelationRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.service.ComVetGscService;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.CityWiseQuantity;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DIDNumberDetails;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DIDSupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DetailsByCallTypeBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.NumberCountryDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.RepcUpdateJeopardyBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.RoutingDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SharedInCircuitBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SipTrunkDetails;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SiteFunctionsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SitesBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SuppliersBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.TrunkBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.dias.servicefulfillmentutils.service.v1.AttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscPdfService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.RepcService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


@Service
@Transactional(readOnly = true)
public class GscImplementationService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscImplementationService.class);
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	FlowableBaseService flowableBaseService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScAttachmentRepository scAttachmentRepository;

	@Autowired
	MstTaskAttributeRepository mstTaskAttributeRepository;

	@Autowired
	ScAssetRepository scAssetRepository;

	@Autowired
	ScAssetRelationRepository scAssetRelationRepository;

	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;

	@Autowired
	AttributeService attributeService;

	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;

	@Autowired
	ComVetGscService comVetGscService;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttrRepository;
	
	@Autowired
	GscFlowGroupToAssetRepository gscFlowGroupToAssetRepository;
	
	@Autowired
	RepcService repcService;
	
    @Autowired
    RuntimeService runtimeService;
	
	@Autowired
	GscPdfService gscPdfService;
	
	@Autowired
	FlowGroupAttributeRepository flowGroupAttributeRepository;
	
	@Autowired
	GscFlowGroupRepository gscFlowGroupRepository;
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Transactional(readOnly = false)
	public PortingNumberUpdateListBean updatePortingNumber(Integer serviceId, PortingNumberUpdateListBean portingNumberUpdateListBean) throws TclCommonException {
		Optional<ScServiceDetail> scOptional = scServiceDetailRepository.findById(serviceId);
		if(scOptional.isPresent() && Objects.nonNull(portingNumberUpdateListBean.getPortingNumbers())) {
			String userName = Utils.getSource();
			ScServiceDetail scServiceDetail = scOptional.get();
			if(scServiceDetail.getErfPrdCatalogOfferingName().contains("UIFN")) {
				List<ScServiceDetail> childServiceDetails = gscService.getChildServiceDetails(scServiceDetail.getParentId());
				for(ScServiceDetail scServiceDetail2 : childServiceDetails) {
					if(scServiceDetail2.getErfPrdCatalogOfferingName().contains("UIFN")) {
						List<Integer> tollFreeIds = new ArrayList<Integer>();
						List<GscScAsset> torScAssets = scAssetRepository.getTollFreeAndRoutingFromOutpuse(scServiceDetail2.getId());
						torScAssets.forEach(gscScAsset -> {
							if(gscScAsset.getTollfreeId() != null) {
								tollFreeIds.add(gscScAsset.getTollfreeId());
							}
						});
						int index=0;
						for(PortingNumberUpdateBean portingNumberUpdateBean : portingNumberUpdateListBean.getPortingNumbers()) {
							gscService.updatedScAsset(tollFreeIds.get(index),
									portingNumberUpdateBean.getTollfree(), userName);
							index = index + 1;
						}
					}
				}
			} else {
				portingNumberUpdateListBean.getPortingNumbers().forEach(portingNumberUpdateBean -> {
					gscService.updatedScAsset(portingNumberUpdateBean.getTollfreeId(),
							portingNumberUpdateBean.getTollfree(), userName);
				});
			}
		}
		return portingNumberUpdateListBean;
	}

	@Transactional(readOnly = false)
	public VoiceBasicEnrichmentBean basicEnrichmentForVoice(VoiceBasicEnrichmentBean voiceAdvanceEnrichmentBean)
			throws TclCommonException {
		LOGGER.info("Inside basicEnrichmentForVoice Method");

		Task task = getTaskByIdAndWfTaskId(voiceAdvanceEnrichmentBean.getTaskId(),
				voiceAdvanceEnrichmentBean.getWfTaskId());

		validateInputs(task, voiceAdvanceEnrichmentBean);

		Map<String, String> atMap;

		if (Objects.nonNull(voiceAdvanceEnrichmentBean.getSipAttributes())) {
			atMap = new HashMap<>();
			constructSipAttributeMap(voiceAdvanceEnrichmentBean.getSipAttributes(), atMap);
			componentAndAttributeService.updateComponentAndAdditionalAttributes(voiceAdvanceEnrichmentBean.getSipAttributes().getId(), atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, task.getSiteType());
		}

		if (Objects.nonNull(voiceAdvanceEnrichmentBean.getServiceAttributes())) {
			atMap = new HashMap<>();
			constructServiceAttributeMap(voiceAdvanceEnrichmentBean.getServiceAttributes(), atMap);
			componentAndAttributeService.updateComponentAndAdditionalAttributes(voiceAdvanceEnrichmentBean.getServiceAttributes().getId(), atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, task.getSiteType());
		}

		if (Objects.nonNull(voiceAdvanceEnrichmentBean.getDocumentIds())
				&& !voiceAdvanceEnrichmentBean.getDocumentIds().isEmpty())
			voiceAdvanceEnrichmentBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		/**processTaskLogDetails(task, "CLOSED", voiceAdvanceEnrichmentBean.getDelayReason(), null,
				voiceAdvanceEnrichmentBean);**/

		LOGGER.info("Exit from basicEnrichmentForVoice Method");
		/**return (VoiceBasicEnrichmentBean) flowableBaseService.taskDataEntry(task, voiceAdvanceEnrichmentBean);**/
		return voiceAdvanceEnrichmentBean;

	}

	/**
	 * This method is used to build SIP attributes
	 *
	 * @param sipAttributes
	 * @param atMap
	 * @throws TclCommonException
	 * 
	 */

	private void constructSipAttributeMap(SIPAttributes sipAttributes, Map<String, String> atMap)
			throws TclCommonException {

		atMap.put("requiredOnABNumberE164", sipAttributes.getRequiredOnABnumber());
		atMap.put("dtmfRelaySupport", sipAttributes.getDtmfRelaySupport());
		atMap.put("supportedSipPrivacyHeaders", sipAttributes.getSupportSipPrivacyHeaders());
		atMap.put("sessionKeepAliveTimer", sipAttributes.getSessionKeepAliveTimer());
		atMap.put("prefixAddition", sipAttributes.getPrefixAddition());
		atMap.put("transportProtocol", sipAttributes.getTransport());
		atMap.put("codec", sipAttributes.getCodec());
		atMap.put("noOfConcurrentChannel", sipAttributes.getNoOfConcurrentChannel());
		if(!CollectionUtils.isEmpty(sipAttributes.getCustomerPublicIp())) {
			atMap.put("customerPublicIp", String.join(",", sipAttributes.getCustomerPublicIp()));
		}
		atMap.put("equipmentAddress", sipAttributes.getEquipmentAddress());
		atMap.put("routingTopology", sipAttributes.getRoutingTopology());
		atMap.put("ipAddressSpace", sipAttributes.getIpAddressSpace());
		atMap.put("callsPerSecondCps", sipAttributes.getCallsPerSecond());
		atMap.put("dialPlanLogicPrefixOrCli", sipAttributes.getDialPlanLogic());
		atMap.put("fqdn", sipAttributes.getFqdn());
		atMap.put("additionalInformation", sipAttributes.getAdditionalInformaiton());
		atMap.put("certificateAuthoritySupport", sipAttributes.getCertificateAuthoritySupport());
		atMap.put("terminationNumberWorkingOutpulse", sipAttributes.getTerminationNumber());
		atMap.put("workingTemporaryTerminationNumber", sipAttributes.getWorkingOutpulse());
		atMap.put("certificateAuthoritySupportUserValue",
				Utils.convertObjectToJson(sipAttributes.getCertificateAuthoritySupportUserValue()));
	}

	/**
	 * This method is used to build Service attributes
	 *
	 * @param serviceAttributes
	 * @param atMap
	 * @throws TclCommonException
	 * 
	 */

	private void constructServiceAttributeMap(ServiceAttributes serviceAttributes, Map<String, String> atMap) {

		atMap.put("terminationNumberWorkingOutpulse", String.join(",", serviceAttributes.getTerminationNumbers()));
		atMap.put("Required porting numbers", String.join(",", serviceAttributes.getPortNumbers()));
		atMap.put("listOfNumbersToBePorted", String.valueOf(serviceAttributes.getPortedNumbersCount()));
		atMap.put("terminationName", serviceAttributes.getTerminationName());
		atMap.put("quantityOfNumbers", String.valueOf(serviceAttributes.getQuantityOfNumbers()));
		atMap.put("isratePerMinutefixed", String.valueOf(serviceAttributes.getFixedRatePerMin()));
		atMap.put("surchargeRate", String.valueOf(serviceAttributes.getSurchargeRate()));
		atMap.put("cityWisePortingServiceNeeded", serviceAttributes.getPortingSericeNeeded());
		atMap.put("isratePerMinutespecial", String.valueOf(serviceAttributes.getSpecialRatePerMin()));
		atMap.put("isratePerMinutemobile", String.valueOf(serviceAttributes.getMobileRatePerMin()));
		atMap.put("terminationNumberIsdCode", serviceAttributes.getTerminationNumberISDCode());
	}

	@Transactional(readOnly = false)
	public ClosingTaskBean closeBasicEnrichmentTask(ClosingTaskBean closingTaskBean)
			throws TclCommonException {
		LOGGER.info("Inside closeBasicEnrichmentTask Method");

		Task task = getTaskByIdAndWfTaskId(closingTaskBean.getTaskId(), closingTaskBean.getWfTaskId());

		validateInputs(task, closingTaskBean);

		Map<String, Object> fMap = new HashMap<>();
		if (closingTaskBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("isBasicEnrichmentValid", "valid");
			
		} else {
			fMap.put("isBasicEnrichmentValid", "invalid");
			copyApprovalStatusToCountries(task.getServiceId(), "basicEnrichmentStatus", "basicEnrichmentRemarks", "basicEnrichmentStatus", "basicEnrichmentRemarks");
		}

		processTaskLogDetails(task, "CLOSED", closingTaskBean.getDelayReason(), null, closingTaskBean);

		LOGGER.info("Exit from closeBasicEnrichmentTask Method");
		return (ClosingTaskBean) flowableBaseService.taskDataEntry(task, closingTaskBean, fMap);
	}

	
	
	@Transactional(readOnly = false)
	public ValidateDocumentApprovalAllBean validateDocumentApproveOrClarifyAll(
			ValidateDocumentApprovalAllBean validateDocumentApprovalAllBean) throws TclCommonException {
		LOGGER.info("Inside validateDocumentApproveOrClarifyAll Method");
		if (Objects.nonNull(validateDocumentApprovalAllBean)
				&& Objects.nonNull(validateDocumentApprovalAllBean.getValidateDocumentApproval())) {
			for (ValidateDocumentApprovalBean validateDocumentApprovalBean : validateDocumentApprovalAllBean
					.getValidateDocumentApproval()) {
				validateDocumentApproveOrClarify(validateDocumentApprovalBean);
			}
		}
		LOGGER.info("Exit from validateDocumentApproveOrClarifyAll Method");
		return validateDocumentApprovalAllBean;
	}

	@Transactional(readOnly = false)
	public ValidateDocumentApprovalBean validateDocumentApproveOrClarify(
			ValidateDocumentApprovalBean validateDocumentApprovalBean) throws TclCommonException {
		LOGGER.info("Inside validateDocumentApproveOrClarify Method");

		if (Objects.nonNull(validateDocumentApprovalBean)) {
			if (Objects.nonNull(validateDocumentApprovalBean.getStatus())) {
				Map<String, String> atMap = new HashMap<>();
				atMap.put("validDocStatus", validateDocumentApprovalBean.getStatus());
				atMap.put("validDocRemarks", validateDocumentApprovalBean.getRemarks());
				if("VALID".equalsIgnoreCase(validateDocumentApprovalBean.getStatus())) {
					persistSelectedSuppliersInfo(validateDocumentApprovalBean.getId(),validateDocumentApprovalBean.getSelectedSuppliers());
				}
				componentAndAttributeService.updateAttributes(validateDocumentApprovalBean.getId(), atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, "A");
			}

			if (Objects.nonNull(validateDocumentApprovalBean.getDocument())) {
				Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository
						.findById(validateDocumentApprovalBean.getId());
				checkAndMakeEntryInScAttachment(scServiceDetail.get(),
						validateDocumentApprovalBean.getDocument().getAttachmentId(),
						validateDocumentApprovalBean.getDocument().getCategory());
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}

		LOGGER.info("Exit from validateDocumentApproveOrClarify Method");
		return validateDocumentApprovalBean;
	}

	@Transactional(readOnly = false)
	public VoiceBasicEnrichmentBean validateDocumentForVoice(ValidateDocumentBean validateDocumentBean)
			throws TclCommonException {
		LOGGER.info("Inside validateDocumentForVoice Method");

		Task task = getTaskByIdAndWfTaskId(validateDocumentBean.getTaskId(), validateDocumentBean.getWfTaskId());

		validateInputs(task, validateDocumentBean);

		Map<String, Object> fMap = new HashMap<>();
		if (validateDocumentBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("isDocumentsValid", "valid");
			
		} else {
			fMap.put("isDocumentsValid", "invalid");
			copyApprovalStatusToCountries(task.getServiceId(), "validDocStatus", "validDocRemarks", "basicEnrichmentStatus", "basicEnrichmentRemarks");
		}

		processTaskLogDetails(task, "CLOSED", validateDocumentBean.getDelayReason(), null, validateDocumentBean);

		LOGGER.info("Exit from validateDocumentForVoice Method");
		return (VoiceBasicEnrichmentBean) flowableBaseService.taskDataEntry(task, validateDocumentBean, fMap);
	}

	private void persistSelectedSuppliersInfo(Integer serviceId, List<DIDSupplierBean> selectedSuppliers) throws TclCommonException {
		if(Objects.nonNull(selectedSuppliers) && !selectedSuppliers.isEmpty()) {
				Map<String, String> atMap = new HashMap<>();
				atMap.put("selectedSuppliers", Utils.convertObjectToJson(selectedSuppliers));
//				componentAndAttributeService.updateAttributes(serviceId, atMap,AttributeConstants.COMPONENT_GSCLICENSE, "A");
				componentAndAttributeService.updateComponentAndAdditionalAttributes(serviceId, atMap,AttributeConstants.COMPONENT_GSCLICENSE, "A");
		}
	}

	/**
	 * This method is used to get the Documents for validation.
	 *
	 * @param parentServiceId
	 * @return List<ValidateDocumentBean>
	 * 
	 */

	/*
	 * public List<DocumentValidationBean> getDocumentsInfoForValidation(String
	 * parentServiceId) {
	 * LOGGER.info("Inside getDocumentsInfoForValidation Method");
	 * 
	 * List<DocumentValidationBean> validateDocBeanList = new LinkedList<>();
	 * 
	 * List<String> productNamesList =
	 * scServiceDetailRepository.findProductNamesByParentId(parentServiceId);
	 * 
	 * if (Objects.nonNull(productNamesList)) { productNamesList.forEach(productName
	 * -> { List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository
	 * .findByProductNameAndParentId(productName, parentServiceId);
	 * 
	 * if (Objects.nonNull(scServiceDetailList)) { DocumentValidationBean
	 * ValidateDocumentBean = constructValidateDocumentBean(scServiceDetailList);
	 * validateDocBeanList.add(ValidateDocumentBean); } });
	 * 
	 * } LOGGER.info("Exit from getDocumentsInfoForValidation Method"); return
	 * validateDocBeanList; }
	 */

	@Deprecated
	public List<ViewRoutingNumberBean> getRoutingNumberDetails(Integer serviceId, String supplierId) {
		List<GscScAsset> torScAssets = scAssetRepository.getRoutingNumberBySupplier(serviceId, supplierId);
		List<ViewRoutingNumberBean> viewRoutingNumberBeans = new ArrayList<>();
		List<String> attributesList = new ArrayList<String>();
		attributesList.add("routingNoReservationId");
		attributesList.add("routingNoReservationEndDate");
		torScAssets.forEach(toRscAsset -> {
			ViewRoutingNumberBean viewRoutingNumberBean = new ViewRoutingNumberBean();
			viewRoutingNumberBean.setId(toRscAsset.getRoutingId());
			viewRoutingNumberBean.setRoutingNumber(toRscAsset.getRoutingName());
			List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeNameIn(toRscAsset.getRoutingId(), attributesList);
			scAssetAttributes.forEach(scAssetAttribute -> {
				if (scAssetAttribute.getAttributeName().equals("routingNoReservationId")) {
					viewRoutingNumberBean.setRoutingNoReservationId(scAssetAttribute.getAttributeValue());
				} else if (scAssetAttribute.getAttributeName().equals("routingNoReservationEndDate")) {
					viewRoutingNumberBean.setRoutingNoReservationEndDate(scAssetAttribute.getAttributeValue());
				}
			});
			viewRoutingNumberBeans.add(viewRoutingNumberBean);
		});
		return viewRoutingNumberBeans;
	}
	
	public List<ViewRoutingNumberBean> getRoutingNumberDetails(Integer taskId, String wfTaskId, String supplierId) {
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		List<GscScAsset> torScAssets = scAssetRepository.getRoutingNumberBySupplierByFlowGroup(task.getServiceId(), supplierId, task.getGscFlowGroupId());
		List<ViewRoutingNumberBean> viewRoutingNumberBeans = new ArrayList<>();
		List<String> attributesList = new ArrayList<String>();
		attributesList.add("routingNoReservationId");
		attributesList.add("routingNoReservationEndDate");
		torScAssets.forEach(toRscAsset -> {
			ViewRoutingNumberBean viewRoutingNumberBean = new ViewRoutingNumberBean();
			viewRoutingNumberBean.setId(toRscAsset.getRoutingId());
			viewRoutingNumberBean.setRoutingNumber(toRscAsset.getRoutingName());
			List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeNameIn(toRscAsset.getRoutingId(), attributesList);
			scAssetAttributes.forEach(scAssetAttribute -> {
				if (scAssetAttribute.getAttributeName().equals("routingNoReservationId")) {
					viewRoutingNumberBean.setRoutingNoReservationId(scAssetAttribute.getAttributeValue());
				} else if (scAssetAttribute.getAttributeName().equals("routingNoReservationEndDate")) {
					viewRoutingNumberBean.setRoutingNoReservationEndDate(scAssetAttribute.getAttributeValue());
				}
			});
			viewRoutingNumberBeans.add(viewRoutingNumberBean);
		});
		return viewRoutingNumberBeans;
	}

	public List<TestNumberBean> getPortingOutpulseDetails(String serviceId) throws NumberFormatException, TclCommonException {
		List<GscScAsset> torScAssets = scAssetRepository.getTollFreeAndRoutingFromOutpuse(Integer.parseInt(serviceId));
		List<TestNumberBean> testNumberBeans = new ArrayList<>();
		HashMap<String, String> cityCodeAndCountries = gscService.getCityCodeAndCountries(Integer.parseInt(serviceId));
		torScAssets.forEach(toRscAsset -> {
			TestNumberBean testNumberBean = new TestNumberBean();
			testNumberBean.setCustomerOutpulse(toRscAsset.getOutpulseName());
			testNumberBean.setRoutingNumber(toRscAsset.getRoutingName());
			List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeNameIn(toRscAsset.getTollfreeId(), Arrays.asList("cityCode", "isPortNumber"));
			for(ScAssetAttribute scAssetAttribute : scAssetAttributes) {
				if(scAssetAttribute.getAttributeName().equalsIgnoreCase("isPortNumber")) {
					if(scAssetAttribute.getAttributeValue().equals("yes")) {
						testNumberBean.setTollfree(toRscAsset.getTollfreeName());
					}
				} else if(scAssetAttribute.getAttributeName().equalsIgnoreCase("cityCode")) {
					testNumberBean.setCityCode(scAssetAttribute.getAttributeValue());
					if(cityCodeAndCountries.containsKey(scAssetAttribute.getAttributeValue().toUpperCase())) {
						testNumberBean.setCityName(cityCodeAndCountries.get(scAssetAttribute.getAttributeValue().toUpperCase()));
					}
				}
			}
			testNumberBeans.add(testNumberBean);
		});
		return testNumberBeans;
	}

	public List<SupplierResponseBean> getSupplierResponseDetails(Integer taskId,String wfTaskId) {
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		List<SupplierResponseBean> supplierResponseBeans = new ArrayList<SupplierResponseBean>();
		List<String> attributesList = Arrays.asList("supplierId", "supplierName", "supplierActivationDate");
		List<GscScAsset> torScAssets = scAssetRepository.getTollFreeAndRoutingFromOutpuseAndFlowGrpID(task.getServiceId(),task.getGscFlowGroupId());
		torScAssets.forEach(scAssets -> {
			SupplierResponseBean supplierResponseBean = new SupplierResponseBean();
			supplierResponseBean.setOutpulseNumberId(scAssets.getOutpulseId());
			supplierResponseBean.setOutpulseNumber(scAssets.getOutpulseName());
			supplierResponseBean.setRoutingNumberId(scAssets.getRoutingId());
			supplierResponseBean.setRoutingNumber(scAssets.getRoutingName());
			supplierResponseBean.setTollFreeNumberId(scAssets.getTollfreeId());
			supplierResponseBean.setTollFreeNumber(scAssets.getTollfreeName());
			List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeNameIn(scAssets.getRoutingId(), attributesList);
			scAssetAttributes.forEach(scAssetAttribute -> {
				String value = scAssetAttribute.getAttributeValue();
				if(GscConstants.Y.equals(scAssetAttribute.getIsAdditionalParam())) {
					Optional<ScAdditionalServiceParam> optAdditionParam = scAdditionalServiceParamRepository.findById(Integer.parseInt(value));
					if(optAdditionParam.isPresent()) {
						value = optAdditionParam.get().getValue();
					}
				}
				
				if (scAssetAttribute.getAttributeName().equals("supplierId")) {
					supplierResponseBean.setSupplierId(value);
				} else if (scAssetAttribute.getAttributeName().equals("supplierName")) {
					supplierResponseBean.setSupplierName(value);
				} else if (scAssetAttribute.getAttributeName().equals("supplierActivationDate")) {
					supplierResponseBean.setActivationDate(value);
				}
			});
			supplierResponseBeans.add(supplierResponseBean);
		});
		return supplierResponseBeans;
	}

	public List<TestNumberWithRoutingBean> getVasTestingDetails(Integer taskId, String wfTaskId) {
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		String status = GscNumberStatus.IN_TEST_NUMBER;
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("gsc-re-test-numbers")) {
			status = GscNumberStatus.IN_RE_TEST_NUMBER;
		}
		List<GscScAsset> torScAssets = scAssetRepository.findByServiceIdandTypeandAssetId(task.getServiceId(),
				"Toll-Free", task.getGscFlowGroupId(), status);
		Map<Integer, TestNumberWithRoutingBean> testNumberBeans = new HashMap<Integer, TestNumberWithRoutingBean>();
		List<TestNumberWithRoutingBean> testNumbers = new ArrayList<>();
		List<String> attList = new ArrayList<String>();
		attList.add("billingStartDate");
		attList.add("cdrLog");
		attList.add("reservationId");
		torScAssets.forEach(torScAsset -> {
			List<RoutingNumberBean> routingNumberBeans;
			if (testNumberBeans.containsKey(torScAsset.getTollfreeId())) {
				routingNumberBeans = testNumberBeans.get(torScAsset.getTollfreeId()).getRoutingNumberBeans();
			} else {
				TestNumberWithRoutingBean testNumberWithRoutingBean = new TestNumberWithRoutingBean();
				testNumberWithRoutingBean.setIsReserved(false);
				testNumberWithRoutingBean.setTollfree(torScAsset.getTollfreeName());
				testNumberWithRoutingBean.setTollfreeId(torScAsset.getTollfreeId());
				testNumberWithRoutingBean.setCustomerOutpulse(torScAsset.getOutpulseName());
				testNumberWithRoutingBean.setCustomerOutpulseId(torScAsset.getOutpulseId());
				List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
						.findByScAsset_IdAndAttributeNameIn(torScAsset.getTollfreeId(), attList);
				scAssetAttributes.forEach(scAssetAttribute -> {
					String value = scAssetAttribute.getAttributeValue();
					if(GscConstants.Y.equals(scAssetAttribute.getIsAdditionalParam())) {
						Optional<ScAdditionalServiceParam> optAdditionParam = scAdditionalServiceParamRepository.findById(Integer.parseInt(value));
						if(optAdditionParam.isPresent()) {
							value = optAdditionParam.get().getValue();
						}
					}
					
					if (scAssetAttribute.getAttributeName().equals("billingStartDate")) {
						testNumberWithRoutingBean.setBillingStartDate(value);
					} else if (scAssetAttribute.getAttributeName().equals("cdrLog")) {
						testNumberWithRoutingBean.setCdrLog(value);
					} else if (scAssetAttribute.getAttributeName().equals("reservationId")) {
							testNumberWithRoutingBean.setReservationId(value);
							testNumberWithRoutingBean.setIsReserved(true);
					}
				});
				routingNumberBeans = new ArrayList<>();
				testNumberWithRoutingBean.setRoutingNumberBeans(routingNumberBeans);
				testNumberBeans.put(testNumberWithRoutingBean.getTollfreeId(), testNumberWithRoutingBean);
			}
			RoutingNumberBean routingNumberBean = new RoutingNumberBean();
			routingNumberBean.setRoutingNumber(torScAsset.getRoutingName());
			routingNumberBean.setRoutingNumberId(torScAsset.getRoutingId());
			ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeName(torScAsset.getRoutingId(), "supplierActivationDate");
			if (Objects.nonNull(scAssetAttribute)) {
				String value = getAbsoluteAttrValue(scAssetAttribute);
				routingNumberBean.setSupplierActivationDate(value);
			}
			routingNumberBeans.add(routingNumberBean);
		});

		testNumberBeans.forEach((s, testNumberWithRoutingBean) -> {
			testNumbers.add(testNumberWithRoutingBean);
		});
		return testNumbers;
	}

	private String getAbsoluteAttrValue(ScAssetAttribute scAssetAttribute) {
		String value = scAssetAttribute.getAttributeValue();
		if(!StringUtils.isEmpty(value) && GscConstants.Y.equals(scAssetAttribute.getIsAdditionalParam())) {
			Optional<ScAdditionalServiceParam> optAdditionParam = scAdditionalServiceParamRepository.findById(Integer.parseInt(value));
			if(optAdditionParam.isPresent()) {
				value = optAdditionParam.get().getValue();
			}
		}
		return value;
	}

	@Transactional(readOnly = false)
	public PostTestNumberBean postTestNumber(PostTestNumberBean postTestNumberBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(postTestNumberBean.getTaskId(), postTestNumberBean.getWfTaskId());

		validateInputs(task, postTestNumberBean);
		String userName = Utils.getSource();
		List<Integer> scAssets = new ArrayList<Integer>();
		for (ScAssetBean scAsset : postTestNumberBean.getScAssets()){
			Map<String, String> atMap = new HashMap<>();
			atMap.put("billingStartDate", scAsset.getBillStartDate());
			if (scAsset.getCdrLog() != null) {
				atMap.put("cdrLog", scAsset.getCdrLog());
			}
			gscService.updatedScAssetAttribute(scAsset.getTollfreeId(), atMap, userName);
			gscService.updatedScAssetStatus(scAsset.getTollfreeId(), GscNumberStatus.IN_SERVICE_ACCEPT, "system");
			scAssets.add(scAsset.getTollfreeId());
			scAsset.getRoutingNumberBeans().forEach(routingNumber -> {
				if(routingNumber.getSupplierActivationDate() != null) {
					gscService.updatedScAssetAttribute(routingNumber.getRoutingNumberId(), "supplierActivationDate",
							routingNumber.getSupplierActivationDate(), userName);
				}
				gscService.updatedScAssetStatus(routingNumber.getRoutingNumberId(), GscNumberStatus.IN_SERVICE_ACCEPT, "system");
			});
		}

		if (Objects.nonNull(postTestNumberBean.getDocumentIds()) && !postTestNumberBean.getDocumentIds().isEmpty())
			postTestNumberBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		
		Map<String, Object> fMap = new HashMap<>();
		fMap.put("atleastOneNumTestPending", "no");
		if(!task.getMstTaskDef().getKey().equalsIgnoreCase("gsc-re-test-numbers")) {
			GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("TestNumber-ServiceAcceptance", task.getServiceCode(), "serviceCode", "system", scAssets);
			fMap.put(GscConstants.KEY_GSC_FLOW_GROUP_ID_SERVICE_ACCEPTANCE, createGscFlowGroup.getId());
			
			//List<GscScAsset> inSupplierRes = gscService.getTollFreeAndRoutingFromOutpuseByFlowGrpIDAndStatus(task.getServiceId(), task.getGscFlowGroupId(), GscNumberStatus.IN_TEST_NUMBER);
			List<GscScAsset> inSupplierRes = scAssetRepository.findByServiceIdandTypeandAssetId(task.getServiceId(),
					"Toll-Free", task.getGscFlowGroupId(), GscNumberStatus.IN_TEST_NUMBER);
			if(inSupplierRes != null && !inSupplierRes.isEmpty()) {
				fMap.put("atleastOneNumTestPending", "yes");
				
				scAssets = new ArrayList<Integer>();
				for(GscScAsset inSupplierRe:inSupplierRes) {
				//inSupplierRes.forEach(inSupplierRe -> {
					scAssets.add(inSupplierRe.getTollfreeId());
				}
				//});
				createGscFlowGroup = gscService.createGscFlowGroup("TestNumber-Split", task.getServiceCode(), "serviceCode", "system", scAssets);
				fMap.put(GscConstants.KEY_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
			}
		}
		processTaskLogDetails(task, "CLOSED", postTestNumberBean.getDelayReason(), null, postTestNumberBean);
		return (PostTestNumberBean) flowableBaseService.taskDataEntry(task, postTestNumberBean, fMap);
	}

	/**
	 * This method is used to build Document Bean
	 *
	 * @param scServiceDetailList
	 * 
	 */

	/*
	 * private DocumentValidationBean
	 * constructValidateDocumentBean(List<ScServiceDetail> scServiceDetailList) {
	 * 
	 * List<ServiceBean> servicesList = new LinkedList<>(); List<String>
	 * attributesList = Arrays.asList("quantityOfNumbers",
	 * "listOfNumbersToBePorted", "validDocStatus", "validDocRemarks",
	 * "cityWiseAreaCode");
	 * 
	 * DocumentValidationBean validateDocumentBean = new DocumentValidationBean();
	 * validateDocumentBean.setProductName(scServiceDetailList.get(0).
	 * getErfPrdCatalogOfferingName());
	 * 
	 * scServiceDetailList.forEach(serviceDetail -> {
	 * 
	 * List<DocumentBean> documentsList = new LinkedList<>();
	 * 
	 * ServiceBean service = new ServiceBean();
	 * service.setId(serviceDetail.getId());
	 * service.setServiceId(serviceDetail.getUuid());
	 * service.setOriginCountry(serviceDetail.getSourceCountry());
	 * service.setDestCountry(serviceDetail.getDestinationCountry());
	 * service.setStatus("PENDING");
	 * 
	 * int serviceId = service.getId();
	 * 
	 * ScComponent scComponent = scComponentRepository
	 * .findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(
	 * serviceId, AttributeConstants.COMPONENT_GSCLICENSE, "A");
	 * 
	 * if (Objects.nonNull(scComponent)) {
	 * Optional.ofNullable(scComponent.getScComponentAttributes()).get().stream()
	 * .filter(component -> attributesList.contains(component.getAttributeName()))
	 * .forEach(component -> { LOGGER.info("Components for service id {} >> {}",
	 * serviceId, component.getAttributeName()); if
	 * ("quantityOfNumbers".equalsIgnoreCase(component.getAttributeName()))
	 * service.setQty(component.getAttributeValue()); else if
	 * ("listOfNumbersToBePorted".equalsIgnoreCase(component.getAttributeName()))
	 * service.setPortingQty(component.getAttributeValue()); else if
	 * ("validDocStatus".equalsIgnoreCase(component.getAttributeName()))
	 * service.setStatus(component.getAttributeValue()); else if
	 * ("validDocRemarks".equalsIgnoreCase(component.getAttributeName()))
	 * service.setRemarks(component.getAttributeValue()); else if
	 * ("cityWiseAreaCode".equalsIgnoreCase(component.getAttributeName()))
	 * service.setOriginCity(component.getAttributeValue()); }); }
	 * 
	 * Optional.ofNullable(scAttachmentRepository.findAllByScServiceDetail_Id(
	 * serviceId)).get().stream()
	 * .map(ScAttachment::getAttachment).forEach(attachment -> { DocumentBean
	 * document = new DocumentBean(); document.setAttachmentId(attachment.getId());
	 * document.setCategory(attachment.getCategory());
	 * document.setName(attachment.getName());
	 * document.setUrl(attachment.getUriPathOrUrl()); documentsList.add(document);
	 * });
	 * 
	 * service.setDocumentsList(documentsList); servicesList.add(service);
	 * validateDocumentBean.setServices(servicesList); });
	 * 
	 * return validateDocumentBean; }
	 */

	@Transactional(readOnly = false)
	public SelectedSuppliersBean saveSelectedSuppliers(SelectedSuppliersBean supplierDetailBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(supplierDetailBean.getTaskId(), supplierDetailBean.getWfTaskId());

		validateInputs(task, supplierDetailBean);
		if (supplierDetailBean.getSuppliers() != null && !supplierDetailBean.getSuppliers().isEmpty()) {
			SuppliersBean vasSupplierBean = new SuppliersBean();
			vasSupplierBean.setSuppliers(supplierDetailBean.getSuppliers());
			String supplierDetails = Utils.convertObjectToJson(vasSupplierBean);
			Map<String, String> atMap = new HashMap<>();
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("gsc-select-port-suppliers")) {
				atMap.put("suppliersSelected-Port", supplierDetails);
			} else {
				atMap.put("suppliersSelected", supplierDetails);
			}

			componentAndAttributeService.updateComponentAndAdditionalAttributes(task.getServiceId(), atMap,
					AttributeConstants.COMPONENT_GSCLICENSE, "A");
		}

		processTaskLogDetails(task, "CLOSED", supplierDetailBean.getDelayReason(), null, supplierDetailBean);
		return (SelectedSuppliersBean) flowableBaseService.taskDataEntry(task, supplierDetailBean);
	}

	@Transactional(readOnly = false)
	public PlaceOrderToSuppliersBean placeSupplierOrderDetail(PlaceOrderToSuppliersBean supplierDetailBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(supplierDetailBean.getTaskId(), supplierDetailBean.getWfTaskId());

		validateInputs(task, supplierDetailBean);

		String supplierDetails = Utils.convertObjectToJson(supplierDetailBean.getSuppliers());
		Map<String, String> atMap = new HashMap<>();
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("gsc-order-port-suppliers")) {
			atMap.put("supplierOrderFlow-Port", supplierDetailBean.getOrderFlow());
			atMap.put("placeOrderRes-Port", supplierDetails);
		} else {
			atMap.put("supplierOrderFlow", supplierDetailBean.getOrderFlow());
			atMap.put("placeOrderRes", supplierDetails);
		}

		componentAndAttributeService.updateComponentAndAdditionalAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");

		processTaskLogDetails(task, "CLOSED", supplierDetailBean.getDelayReason(), null, supplierDetailBean);
		return (PlaceOrderToSuppliersBean) flowableBaseService.taskDataEntry(task, supplierDetailBean);
	}

	@Transactional(readOnly = false)
	public SupplierResponseDetailBean supplierResponse(SupplierResponseDetailBean supplierResponseDetailBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(supplierResponseDetailBean.getTaskId(),
				supplierResponseDetailBean.getWfTaskId());

		validateInputs(task, supplierResponseDetailBean);
		List<Integer> selectedRoutingAssets = new ArrayList<Integer>();
		String userName = Utils.getSource();
		if (supplierResponseDetailBean.getSupplierResponse() != null) {
			supplierResponseDetailBean.getSupplierResponse().forEach(supplierResponse -> {
				if (supplierResponse.getRoutingNumberId() != null) {
					ScAsset scAsset = gscService.updatedScAsset(supplierResponse.getRoutingNumberId(),
							supplierResponse.getRoutingNumber(), userName);
					gscService.updatedScAssetAttribute(scAsset, "supplierActivationDate",
							supplierResponse.getActivationDate(), userName);
					selectedRoutingAssets.add(supplierResponse.getRoutingNumberId());
					if (Objects.isNull(supplierResponse.getTollFreeNumberId())
							&& Objects.nonNull(supplierResponse.getTollFreeNumber())) {
						ScAsset createScAsset = gscService.createScAsset(task.getServiceId(),
								supplierResponse.getTollFreeNumber(), "Toll-Free", userName);
						scAssetRepository.save(createScAsset);
						List<ScAssetRelation> scAssetRelations = new ArrayList<>();
						scAssetRelations.add(gscService.generateScAssetRelations(createScAsset.getId(),
								supplierResponse.getRoutingNumberId(), "Routing-Number", userName));
						scAssetRelations.add(gscService.generateScAssetRelations(supplierResponse.getRoutingNumberId(),
								createScAsset.getId(), "Toll-Free", userName));
						// Relationship between Outpluse and Toll-Free
						scAssetRelations.add(gscService.generateScAssetRelations(createScAsset.getId(),
								supplierResponse.getOutpulseNumberId(), "Outpulse", userName));
						scAssetRelations.add(gscService.generateScAssetRelations(supplierResponse.getOutpulseNumberId(),
								createScAsset.getId(), "Toll-Free", userName));
						scAssetRelationRepository.saveAll(scAssetRelations);
					}
				}
			});
			if (supplierResponseDetailBean.getDocuments() != null) {
				makeEntryInScAttachment(task, supplierResponseDetailBean.getDocuments().getAttachmentId());
			}
		}
		Map<String, Object> fMap = new HashMap<>();
		if (!selectedRoutingAssets.isEmpty()) {
			selectedRoutingAssets.forEach(selectedRoutingId -> {
				gscService.updatedScAssetStatus(selectedRoutingId, GscNumberStatus.IN_PROCESS_SUPPLIER, userName);	
			});
			/*GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("SupplierResponse", task.getId().toString(),
					"Task", userName, selectedRoutingAssets);
			fMap.put(GscConstants.KEY_GSC_SUPPLIER_RES_FLOW_GROUP_ID, createGscFlowGroup.getId());*/
		}
		processTaskLogDetails(task, "CLOSED", supplierResponseDetailBean.getDelayReason(), null,
				supplierResponseDetailBean);
		return (SupplierResponseDetailBean) flowableBaseService.taskDataEntry(task, supplierResponseDetailBean, fMap);
	}

	/***
	 * This method is used to approve the document or reject the document for Rate
	 * Upload task.
	 ***/
	@Transactional(readOnly = false)
	public RateUploadApprovalBean rateUploadApproval(RateUploadApprovalBean rateUploadApprovalBean)
			throws TclCommonException {
		LOGGER.info("Inside Rate uplaod Approval Method");

		if (Objects.nonNull(rateUploadApprovalBean)) {

			Map<String, String> atMap = new HashMap<>();
			atMap.put("rateUploadStatus", rateUploadApprovalBean.getStatus());
			atMap.put("rateUploadRemarks", rateUploadApprovalBean.getRemarks());

			List<Integer> serviceIdList = rateUploadApprovalBean.getId();
			for (Integer serviceId : serviceIdList) {
				componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE,
						"A");
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Exit from Rate uplaod Approval");

		return rateUploadApprovalBean;
	}

	/*** Rate Upload task. ***/
	@Transactional(readOnly = false)
	public RateUploadBean rateUpload(RateUploadBean rateUploadBean) throws TclCommonException {
		LOGGER.info("Inside rateUpload Method");

		Task task = getTaskByIdAndWfTaskId(rateUploadBean.getTaskId(), rateUploadBean.getWfTaskId());

		validateInputs(task, rateUploadBean);

		Map<String, Object> fMap = new HashMap<>();
		if (rateUploadBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("isRateUploadValid", "valid");
		} else {
			fMap.put("isRateUploadValid", "invalid");
			copyApprovalStatusToCountries(task.getServiceId(), "rateUploadStatus", "rateUploadRemarks", "entmmValidationStatus", "entmmValidationRemarks");
		}

		processTaskLogDetails(task, "CLOSED", rateUploadBean.getDelayReason(), null, rateUploadBean);

		LOGGER.info("Exit from rateUpload Method");
		return (RateUploadBean) flowableBaseService.taskDataEntry(task, rateUploadBean, fMap);
	}

	/***
	 * This method is used to approve the document or reject the document for
	 * Provisioning validation task.
	 ***/
	@Transactional(readOnly = false)
	public ProvisioningValidationApprovalBean provisioningValidationApproval(
			ProvisioningValidationApprovalBean provisioningValidApprovalBean) throws TclCommonException {
		LOGGER.info("Inside Provisioning Validation Approval Method");

		if (Objects.nonNull(provisioningValidApprovalBean)) {

			Map<String, String> atMap = new HashMap<>();
			atMap.put("provisioningValidationStatus", provisioningValidApprovalBean.getStatus());
			atMap.put("provisioningValidationRemarks", provisioningValidApprovalBean.getRemarks());
			


			List<Integer> serviceIdList = provisioningValidApprovalBean.getId();
			for (Integer serviceId : serviceIdList) {
				Optional<ScServiceDetail> optScServiceDetail = scServiceDetailRepository.findById(serviceId);
				String orderType = "";
				String serviceType = "";
				String accessType = "";
				Integer parentServiceId = 0;
				if(optScServiceDetail.isPresent()) {
					orderType = optScServiceDetail.get().getOrderType();
					serviceType = optScServiceDetail.get().getErfPrdCatalogOfferingName();
					accessType = optScServiceDetail.get().getAccessType();
					parentServiceId = optScServiceDetail.get().getParentId();
				}
				if(!StringUtils.isEmpty(provisioningValidApprovalBean.getTemprorayOutpulseConfig())) {
					atMap.put(AttributeConstants.IS_WORKING_TEMP_TERMINATION_NO, provisioningValidApprovalBean.getTemprorayOutpulseConfig());
				}else if ("NEW".equalsIgnoreCase(orderType) && serviceType.contains(GscConstants.SIP) 
						&& !GscConstants.PSTN.equalsIgnoreCase(accessType) && containsVasProducts(parentServiceId)) {
					LOGGER.info("Invalid Temporary Outpulse Config..!");
					throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
				}
				componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE,
						"A");
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Exit from Provisioning Validation Approval");

		return provisioningValidApprovalBean;
	}

	/*
	 * public List<ProvisioningValidationBean> getProvisioningValidationInfo(String
	 * parentServiceId) {
	 * LOGGER.info("Inside getDocumentsInfoForValidation Method");
	 * 
	 * List<ProvisioningValidationBean> provisioningInfoList = new LinkedList<>();
	 * 
	 * List<String> productNamesList =
	 * scServiceDetailRepository.findProductNamesByParentId(parentServiceId);
	 * 
	 * if (Objects.nonNull(productNamesList)) { productNamesList.forEach(productName
	 * -> { List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository
	 * .findByProductNameAndParentId(productName, parentServiceId);
	 * 
	 * if (Objects.nonNull(scServiceDetailList)) { ProvisioningValidationBean
	 * provisioningInfo = constructProvisioningValidationBean(scServiceDetailList);
	 * provisioningInfoList.add(provisioningInfo); } });
	 * 
	 * } LOGGER.info("Exit from getDocumentsInfoForValidation Method"); return
	 * provisioningInfoList; }
	 * 
	 * private ProvisioningValidationBean
	 * constructProvisioningValidationBean(List<ScServiceDetail>
	 * scServiceDetailList) { List<ServiceBean> servicesList = new LinkedList<>();
	 * List<String> attributesList = Arrays.asList("quantityOfNumbers",
	 * "listOfNumbersToBePorted", "cityWiseAreaCode"
	 * ,"provisioningValidationStatus","provisioningValidationRemarks",
	 * "bridgeDetails");
	 * 
	 * ProvisioningValidationBean provisioningInfoBean = new
	 * ProvisioningValidationBean();
	 * provisioningInfoBean.setProductName(scServiceDetailList.get(0).
	 * getErfPrdCatalogOfferingName());
	 * 
	 * scServiceDetailList.forEach(serviceDetail -> {
	 * 
	 * List<DocumentBean> documentsList = new LinkedList<>();
	 * 
	 * ServiceBean service = new ServiceBean();
	 * service.setId(serviceDetail.getId());
	 * service.setServiceId(serviceDetail.getUuid());
	 * service.setOriginCountry(serviceDetail.getSourceCountry());
	 * service.setDestCountry(serviceDetail.getDestinationCountry());
	 * service.setStatus("PENDING");
	 * 
	 * int serviceId = service.getId();
	 * 
	 * ScComponent scComponent = scComponentRepository
	 * .findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(
	 * serviceId, AttributeConstants.COMPONENT_GSCLICENSE, "A");
	 * 
	 * if (Objects.nonNull(scComponent)) {
	 * Optional.ofNullable(scComponent.getScComponentAttributes()).get().stream()
	 * .filter(component -> attributesList.contains(component.getAttributeName()))
	 * .forEach(component -> { LOGGER.info("Components for service id {} >> {}",
	 * serviceId, component.getAttributeName()); if
	 * ("quantityOfNumbers".equalsIgnoreCase(component.getAttributeName()))
	 * service.setQty(Utils.convertAsString(component.getAttributeValue())); else if
	 * ("listOfNumbersToBePorted".equalsIgnoreCase(component.getAttributeName()))
	 * service.setPortingQty(Utils.convertAsString(component.getAttributeValue()));
	 * else if ("cityWiseAreaCode".equalsIgnoreCase(component.getAttributeName()))
	 * service.setOriginCity(Utils.convertAsString(component.getAttributeValue()));
	 * else if
	 * ("provisioningValidationStatus".equalsIgnoreCase(component.getAttributeName()
	 * )) service.setStatus(Utils.convertAsString(component.getAttributeValue()));
	 * else if ("bridgeDetails".equalsIgnoreCase(component.getAttributeName())) try
	 * { service.setBridgeDetails(Utils.convertJsonToObject(Utils.convertAsString(
	 * component.getAttributeValue()), List.class)); } catch (TclCommonException e)
	 * { LOGGER.error(e.toString()); } }); }
	 * 
	 * Optional.ofNullable(scAttachmentRepository.findAllByScServiceDetail_Id(
	 * serviceId)).get().stream()
	 * .map(ScAttachment::getAttachment).forEach(attachment -> { DocumentBean
	 * document = new DocumentBean(); document.setAttachmentId(attachment.getId());
	 * document.setCategory(attachment.getCategory());
	 * document.setName(attachment.getName());
	 * document.setUrl(attachment.getUriPathOrUrl()); documentsList.add(document);
	 * });
	 * 
	 * service.setDocumentsList(documentsList);
	 * 
	 * List<OutpulsePortingNumBean> outpulsePortingNumbers =
	 * constructOutpulsePortingNumbers(serviceId);
	 * 
	 * service.setOutpulsePortingNumbers(outpulsePortingNumbers);
	 * 
	 * servicesList.add(service); provisioningInfoBean.setServices(servicesList);
	 * });
	 * 
	 * return provisioningInfoBean; }
	 * 
	 * private List<OutpulsePortingNumBean> constructOutpulsePortingNumbers(int
	 * serviceId){
	 * 
	 * List<ScAsset> assets =
	 * scAssetRepository.findByServiceIdandType(serviceId,"Outpulse");
	 * List<OutpulsePortingNumBean> outpulsePortingNumbers = new LinkedList<>();
	 * 
	 * if(Objects.nonNull(assets)) { for(ScAsset asset:assets) {
	 * 
	 * OutpulsePortingNumBean outpulsePortingNumBean = new OutpulsePortingNumBean();
	 * outpulsePortingNumBean.setOutpulse(asset.getName()); ScAssetRelation
	 * scAssetRelation =
	 * scAssetRelationRepository.findByScAssetIdAndRelationType(asset.getId(),
	 * "Toll-Free");
	 * 
	 * if(Objects.nonNull(scAssetRelation)) { ScAsset assetTollFreeNum =
	 * scAssetRepository.findById(scAssetRelation.getScRelatedAssetId()).get();
	 * 
	 * if(Objects.nonNull(assetTollFreeNum)) {
	 * outpulsePortingNumBean.setTollFreeNumber(assetTollFreeNum.getName());
	 * Optional<ScAssetAttribute> assetAttr =
	 * Optional.ofNullable(assetTollFreeNum.getScAssetAttributes()).get().stream()
	 * .filter(attr ->
	 * "isPorted".equalsIgnoreCase(attr.getAttributeName())).findFirst();
	 * 
	 * if(assetAttr.isPresent())
	 * outpulsePortingNumBean.setIsPortingNumber(assetAttr.get().getAttributeValue()
	 * ); } } outpulsePortingNumbers.add(outpulsePortingNumBean); } } return
	 * outpulsePortingNumbers; }
	 */

	private boolean containsVasProducts(Integer parentServiceId) {
		List<String> productNames = scServiceDetailRepository.findProductNamesByParentId(String.valueOf(parentServiceId));
		if(!CollectionUtils.isEmpty(productNames)) {
			Optional<String> optProdName = productNames.stream().filter(productName -> (productName.contains("ITFS") || productName.contains("LNS") || productName.contains("UIFN"))).findFirst();
			if(optProdName.isPresent() && !optProdName.get().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false)
	public VoiceBasicEnrichmentBean provisioningValidation(ValidateDocumentBean validateDocumentBean)
			throws TclCommonException {
		LOGGER.info("Inside provisioningValidation Method");

		Task task = getTaskByIdAndWfTaskId(validateDocumentBean.getTaskId(), validateDocumentBean.getWfTaskId());

		validateInputs(task, validateDocumentBean);

		Map<String, Object> fMap = new HashMap<>();
		if (validateDocumentBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("vasProvRejto", "valid");
		} else if (validateDocumentBean.getAction().equalsIgnoreCase("RAISE-BE")){
			fMap.put("vasProvRejto", "enrichment");
			copyApprovalStatusToCountries(task.getServiceId(), "provisioningValidationStatus", "provisioningValidationRemarks", "basicEnrichmentStatus", "basicEnrichmentRemarks");
		} else if (validateDocumentBean.getAction().equalsIgnoreCase("RAISE-VD")){
			fMap.put("vasProvRejto", "validatedoc");
			copyApprovalStatusToCountries(task.getServiceId(), "provisioningValidationStatus", "provisioningValidationRemarks", "validDocStatus", "validDocRemarks");
		}

		processTaskLogDetails(task, "CLOSED", validateDocumentBean.getDelayReason(), null, validateDocumentBean);

		LOGGER.info("Exit from provisioningValidation Method");
		return (VoiceBasicEnrichmentBean) flowableBaseService.taskDataEntry(task, validateDocumentBean, fMap);
	}

	@Transactional(readOnly = false)
	public VoiceBasicEnrichmentBean provisioningValidationDid(ValidateDocumentBean validateDocumentBean)
			throws TclCommonException {
		LOGGER.info("Inside provisioningValidation Method");

		Task task = getTaskByIdAndWfTaskId(validateDocumentBean.getTaskId(), validateDocumentBean.getWfTaskId());

		validateInputs(task, validateDocumentBean);

		Map<String, Object> fMap = new HashMap<>();
		if (validateDocumentBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("didProvRejto", "valid");
		} else if (validateDocumentBean.getAction().equalsIgnoreCase("RAISE-BE")){
			fMap.put("didProvRejto", "enrichment");
			copyApprovalStatusToCountries(task.getServiceId(), "provisioningValidationStatus", "provisioningValidationRemarks", "basicEnrichmentStatus", "basicEnrichmentRemarks");
		} else if (validateDocumentBean.getAction().equalsIgnoreCase("RAISE-VD")){
			fMap.put("didProvRejto", "validatedoc");
			copyApprovalStatusToCountries(task.getServiceId(), "provisioningValidationStatus", "provisioningValidationRemarks", "validDocStatus", "validDocRemarks");
		} else if (validateDocumentBean.getAction().equalsIgnoreCase("RAISE-VS")){
			fMap.put("didProvRejto", "voicesales");
			copyApprovalStatusToCountries(task.getServiceId(), "provisioningValidationStatus", "provisioningValidationRemarks", "voiceSalesEngrStatus", "voiceSalesEngrRemarks");
		}

		processTaskLogDetails(task, "CLOSED", validateDocumentBean.getDelayReason(), null, validateDocumentBean);

		LOGGER.info("Exit from provisioningValidation Method");
		return (VoiceBasicEnrichmentBean) flowableBaseService.taskDataEntry(task, validateDocumentBean, fMap);
	}

	@Transactional(readOnly = false)
	public BridgeDetailsBean updateBridge(BridgeDetailsBean bridgeDetailsBean) throws TclCommonException {
		LOGGER.info("Inside updateBridge Method");

		if (Objects.nonNull(bridgeDetailsBean)) {

			Map<String, String> atMap = new HashMap<>();
			atMap.put("bridgeDetails", Utils.convertObjectToJson(bridgeDetailsBean.getBridgeDetails()));
//			componentAndAttributeService.updateAttributes(bridgeDetailsBean.getServiceId(), atMap,
//					AttributeConstants.COMPONENT_GSCLICENSE, "A");
			componentAndAttributeService.updateComponentAndAdditionalAttributes(bridgeDetailsBean.getServiceId(), atMap,
					AttributeConstants.COMPONENT_GSCLICENSE, "A");
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Exit from updateBridge Method");
		return bridgeDetailsBean;
	}

	public List<GscChildServiceDetailBean> getGscChildServices(String parentServiceId, Integer taskId,
			String wfTaskId) {
		LOGGER.info("Inside getGscChildServices Method");
		List<GscChildServiceDetailBean> gscChildServiceDetailBeans = new LinkedList<>();
		List<String> productNamesList = scServiceDetailRepository.findProductNamesByParentId(parentServiceId);
		if (Objects.nonNull(productNamesList)) {
			Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
			productNamesList.forEach(productName -> {
				List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository
						.findByProductNameAndParentId(productName, parentServiceId);
				if (Objects.nonNull(scServiceDetailList)) {
					GscChildServiceDetailBean gscChildServiceDetailBean = constructGscChildServicesBean(
							scServiceDetailList, task);
					gscChildServiceDetailBeans.add(gscChildServiceDetailBean);
				}
			});
			
			/** Creating Dummy SIP service for MACD orders **/
			Optional<ScServiceDetail> optScServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(parentServiceId));
			if(optScServiceDetail.isPresent()) {
				ScServiceDetail scServiceDetail = optScServiceDetail.get();
				if((GscConstants.PUBLIC_IP.equalsIgnoreCase(scServiceDetail.getAccessType()) || GscConstants.MPLS.equalsIgnoreCase(scServiceDetail.getAccessType())) 
						&& "MACD".equalsIgnoreCase(scServiceDetail.getOrderType())) {
					Map<String, String> compAttrMap = commonFulfillmentUtils.getComponentAttributes(Integer.parseInt(parentServiceId),AttributeConstants.COMPONENT_GSCLICENSE, "A");
					GscChildServiceDetailBean sipDetailsForMacd = new GscChildServiceDetailBean();
					sipDetailsForMacd.setProductName("SIP");
					GscChildServiceBean serviceBean = new GscChildServiceBean();
					serviceBean.setAccessType(scServiceDetail.getAccessType());
					serviceBean.setStatus("VALID");
					Map<String, Object> commonData = new HashMap<>();
					commonData.put("interconnectId", compAttrMap.get("interconnectId"));
					commonData.put("interconnectName", compAttrMap.get("interconnectName"));
					serviceBean.setCommonData(commonData);
					sipDetailsForMacd.setServices(Arrays.asList(serviceBean));
					gscChildServiceDetailBeans.add(sipDetailsForMacd);
				}
			}
		}
		LOGGER.info("Exit from getGscChildServices Method");
		return gscChildServiceDetailBeans;
	}

	public CommercialVettingBean getCommercialVetting(Integer serviceId){
		ScServiceDetail scServiceDetail=scServiceDetailRepository.findById(serviceId).get();
		CommercialVettingBean commercialVettingBean=comVetGscService.constructCommercialVetting(serviceId,scServiceDetail.getScOrder().getId());
		return  commercialVettingBean;
	}

	private GscChildServiceDetailBean constructGscChildServicesBean(List<ScServiceDetail> scServiceDetailList,
			Task task) {
		List<GscChildServiceBean> servicesList = new LinkedList<>();
		GscChildServiceDetailBean gServiceDetailBean = new GscChildServiceDetailBean();
		gServiceDetailBean.setProductName(scServiceDetailList.get(0).getErfPrdCatalogOfferingName());
		scServiceDetailList.forEach(serviceDetail -> {
			String serviceType = gscService.getServiceAttrValue(serviceDetail, "serviceType");
			GscChildServiceBean service = new GscChildServiceBean();
			service.setId(serviceDetail.getId());
			service.setServiceCode(serviceDetail.getUuid());
			service.setAccessType(serviceDetail.getAccessType());
			service.setOriginCountry(serviceDetail.getSourceCountry());
			
			if(GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(serviceType)) {
				service.setDestCountry(gscService.getServiceAttrAndAddParamValue(serviceDetail, "destinationCountry"));
			}else {
				service.setDestCountry(serviceDetail.getDestinationCountry());
			}
			service.setStatus("PENDING");

			Map<String, Object> commonData = null;
			try {
				// TaskBean taskBean = getGSCServiceAttributes(serviceDetail.getId(), taskId,
				// wfTaskId);
				commonData = attributeService.getTaskAttributes(task.getMstTaskDef().getKey(),
						serviceDetail.getId(), task.getSiteType());
				if (commonData != null && !commonData.isEmpty()) {
					
					if(GscConstants.DOMESTIC_VOICE.equalsIgnoreCase(serviceType)) {
						if(commonData.containsKey(GscConstants.QUANTITY_OF_NUMBERS) && commonData.containsKey(GscConstants.PORTING_QTY)) {
							String portQty = Utils.convertAsString(commonData.get(GscConstants.PORTING_QTY));
							int portingQty = portQty.isEmpty()?0:Integer.valueOf(portQty);
							int newQty = Integer.valueOf((String) commonData.get(GscConstants.QUANTITY_OF_NUMBERS)) - portingQty;
							commonData.put("newQty", newQty);
						}
						
					}
					
					if(task.getMstTaskDef().getKey().equalsIgnoreCase("gsc-provisioning-validation")) {
						List<CityWiseQuantity> cityCodeAndCountriesQuantityOfNumbers = gscService.getCityCodeAndCountriesQuantityOfNumbers(serviceDetail.getId());
						commonData.put("cityWiseQuantity", cityCodeAndCountriesQuantityOfNumbers);
					}
					
					service.setCommonData(commonData);
					service.setStatus((String) commonData.getOrDefault("validStatus", "PENDING"));
					service.setRemarks((String) commonData.getOrDefault("validRemarks", ""));
				}
			} catch (TclCommonException e) {
			}

			List<DocumentBean> documentsList = new LinkedList<>();
			Optional.ofNullable(scAttachmentRepository.findAllByScServiceDetail_Id(serviceDetail.getId())).get()
					.stream().map(ScAttachment::getAttachment).forEach(attachment -> {
						DocumentBean document = new DocumentBean();
						document.setAttachmentId(attachment.getId());
						document.setCategory(attachment.getCategory());
						document.setName(attachment.getName());
						document.setUrl(attachment.getUriPathOrUrl());
						documentsList.add(document);
					});
			service.setDocuments(documentsList);
			if(commonData != null && serviceDetail.getErfPrdCatalogOfferingName().equalsIgnoreCase(GscCommonConstants.GSC_SIP)) {
				List<Map<String, String>> deviceInfos = new ArrayList<>();
				List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(serviceDetail.getId());
				for(ScComponent scComponent : scComponents) {
					if(!scComponent.getComponentName().equals("LM")) {
						Map<String, String> deviceInfo = new HashMap<>();
						scComponent.getScComponentAttributes().forEach(scComponentAttr -> {
							deviceInfo.put(scComponentAttr.getAttributeName(), scComponentAttr.getAttributeValue());
						});;
						deviceInfos.add(deviceInfo);
					}
				}
				commonData.put("deviceInfo", deviceInfos);
			}
			servicesList.add(service);
			gServiceDetailBean.setServices(servicesList);
		});
		return gServiceDetailBean;
	}

	public List<OutpulseDetailsBean> getSupplierInfoForValidation(Integer taskId, String wfTaskId) {
		LOGGER.info("Inside getSupplierInfoForValidation Method");
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		String status = GscNumberStatus.IN_TEST_NUMBER;
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("gsc-re-test-numbers")) {
			status = GscNumberStatus.IN_RE_TEST_NUMBER;
		}
		List<GscScAsset> torScAssets = scAssetRepository.findByServiceIdandTypeandAssetId(task.getServiceId(), "Toll-Free", task.getGscFlowGroupId(), status);
		List<OutpulseDetailsBean> outpulseDetails = new ArrayList<>();
		List<String> attributeName = new ArrayList<String>();
		attributeName.add("supplierId");
		attributeName.add("supplierName");
		attributeName.add("detailsByCallType");
		torScAssets.forEach(toRscAsset -> {
			List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeNameIn(toRscAsset.getRoutingId(), attributeName);
			HashMap<String, String> attributes = new HashMap<String, String>();
			scAssetAttributes.forEach(scAssetAttribute -> {
				attributes.put(scAssetAttribute.getAttributeName().toLowerCase(), gscService.getScAssetAttributeValue(scAssetAttribute));
			});
			if (Objects.nonNull(attributes.getOrDefault("detailsbycalltype", null))) {
				List<DetailsByCallTypeBean> detailsByCallTypes = Utils.fromJson(
						attributes.getOrDefault("detailsbycalltype", null),
						new TypeReference<List<DetailsByCallTypeBean>>() {
						});
				detailsByCallTypes.forEach(detailsByCallType -> {
					OutpulseDetailsBean outpulseDetailsBean = new OutpulseDetailsBean();
					outpulseDetailsBean.setTollFreeNumber(toRscAsset.getTollfreeName());
					outpulseDetailsBean.setCustomerOutpulse(toRscAsset.getOutpulseName());
					outpulseDetailsBean.setRoutingNumber(toRscAsset.getRoutingName());
					outpulseDetailsBean.setSupplierId(attributes.getOrDefault("supplierid", ""));
					outpulseDetailsBean.setSupplierName(attributes.getOrDefault("suppliername", ""));
					outpulseDetailsBean.setCallType(detailsByCallType.getCallType());
					if (Objects.nonNull(detailsByCallType.getRoutingDetails())
							&& !detailsByCallType.getRoutingDetails().isEmpty()) {
						RoutingDetailsBean routingDetailsBean = detailsByCallType.getRoutingDetails().get(0);
						outpulseDetailsBean.setPcc(routingDetailsBean.getPcc());
						outpulseDetailsBean.setInMainDigits(routingDetailsBean.getMainIncomingDigits());
						outpulseDetailsBean.setCid(routingDetailsBean.getCid());
						outpulseDetailsBean.setSid(routingDetailsBean.getSid());
					}
					outpulseDetails.add(outpulseDetailsBean);
				});
			} else {
				OutpulseDetailsBean outpulseDetailsBean = new OutpulseDetailsBean();
				outpulseDetailsBean.setTollFreeNumber(toRscAsset.getTollfreeName());
				outpulseDetailsBean.setCustomerOutpulse(toRscAsset.getOutpulseName());
				outpulseDetailsBean.setRoutingNumber(toRscAsset.getRoutingName());
				outpulseDetailsBean.setSupplierId(attributes.getOrDefault("supplierid", ""));
				outpulseDetailsBean.setSupplierName(attributes.getOrDefault("suppliername", ""));
				outpulseDetails.add(outpulseDetailsBean);
			}
		});


		LOGGER.info("Exit from getSupplierInfoFor" +
				" Method");

		LOGGER.info("Exit from getSupplierInfoForValidation Method");

		return outpulseDetails;
	}

	@Transactional(readOnly = false)
	public ValidateDocumentBean validateSupplierInternalDB(ValidateDocumentBean validateDocumentBean)
			throws TclCommonException {
		LOGGER.info("Inside provisioningValidation Method");

		Task task = getTaskByIdAndWfTaskId(validateDocumentBean.getTaskId(), validateDocumentBean.getWfTaskId());
		validateInputs(task, validateDocumentBean);

		processTaskLogDetails(task, "CLOSED", validateDocumentBean.getDelayReason(), null, validateDocumentBean);

		LOGGER.info("Exit from provisioningValidation Method");
		return (ValidateDocumentBean) flowableBaseService.taskDataEntry(task, validateDocumentBean);
	}

	@Transactional(readOnly = false)
	public EntmmValidationApprovalBean entmmValidationApproval(EntmmValidationApprovalBean entmmValidationApprovalBean)
			throws TclCommonException {
		LOGGER.info("Inside ENTMM Validation Approval Method");

		if (Objects.nonNull(entmmValidationApprovalBean)) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("entmmValidationStatus", entmmValidationApprovalBean.getStatus());
			atMap.put("entmmValidationRemarks", entmmValidationApprovalBean.getRemarks());

			List<EntmmApprovalDocBean> entmmApprovalDocBeans = entmmValidationApprovalBean.getEntmmDetails();
			for (EntmmApprovalDocBean entmmApprovalDocBean : entmmApprovalDocBeans) {
				componentAndAttributeService.updateAttributes(entmmApprovalDocBean.getId(), atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, "A");
				if (Objects.nonNull(entmmApprovalDocBean.getDocument())) {
					Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository
							.findById(entmmApprovalDocBean.getId());
					checkAndMakeEntryInScAttachment(scServiceDetail.get(),
							entmmApprovalDocBean.getDocument().getAttachmentId(),
							entmmApprovalDocBean.getDocument().getCategory());
				}
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Exit from ENTMM Validation Approval");

		return entmmValidationApprovalBean;
	}

	@Transactional(readOnly = false)
	public EntmmTaskBean closeEntmmTask(EntmmTaskBean entmmTaskBean) throws TclCommonException {
		LOGGER.info("Inside closeEntmmTask Method");
		Task task = getTaskByIdAndWfTaskId(entmmTaskBean.getTaskId(), entmmTaskBean.getWfTaskId());
		validateInputs(task, entmmTaskBean);

		Map<String, Object> fMap = new HashMap<>();
		if (entmmTaskBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("isEntmmValid", "valid");
		} else {
			fMap.put("isEntmmValid", "invalid");
			copyApprovalStatusToCountries(task.getServiceId(), "entmmValidationStatus", "entmmValidationRemarks", "validDocStatus", "validDocRemarks");
		}

		processTaskLogDetails(task, "CLOSED", entmmTaskBean.getDelayReason(), null, entmmTaskBean);

		LOGGER.info("Exit from closeEntmmTask Method");
		return (EntmmTaskBean) flowableBaseService.taskDataEntry(task, entmmTaskBean, fMap);
	}

	/*** Billing Profile task. ***/
	@Transactional(readOnly = false)
	public BillingProfileCmsIdBean billingProfile(BillingProfileCmsIdBean billingProfileCmsIdBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(billingProfileCmsIdBean.getTaskId(), billingProfileCmsIdBean.getWfTaskId());
		validateInputs(task, billingProfileCmsIdBean);

		Map<String, Object> fMap = new HashMap<>();
		if (billingProfileCmsIdBean.getAction().equalsIgnoreCase("RAISE-TAX")) {
			fMap.put("isCmsValid", "invalid");
			fMap.put("isReopenValidateDocTask", "no");
			copyApprovalStatusToCountries(task.getServiceId(), "billingProfileStatus", "billingProfileRemarks", "taxationStatus", "taxationRemarks");
		} else if (billingProfileCmsIdBean.getAction().equalsIgnoreCase("RAISE-VD")) {
			fMap.put("isCmsValid", "valid");
			fMap.put("isReopenValidateDocTask", "yes");
			copyApprovalStatusToCountries(task.getServiceId(), "billingProfileStatus", "billingProfileRemarks", "validDocStatus", "validDocRemarks");
		} else {
			fMap.put("isCmsValid", "valid");
			fMap.put("isReopenValidateDocTask", "no");
		}

		if (Objects.nonNull(billingProfileCmsIdBean.getBillingProfilebean())) {
			Map<String, String> atMap = new HashMap<>();
			billingProfileCmsIdBean.getBillingProfilebean().forEach(billingProfilebean -> {
				atMap.put("cmsId", billingProfilebean.getCmsId());
				atMap.put("profileId", billingProfilebean.getProfileId());
				atMap.put("profileStartDate", billingProfilebean.getProfileStartDate());
				
				componentAndAttributeService.updateAttributes(billingProfilebean.getServiceId(), atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, task.getSiteType());
			});
		}

		processTaskLogDetails(task, "CLOSED", billingProfileCmsIdBean.getDelayReason(), null, billingProfileCmsIdBean);
		return (BillingProfileCmsIdBean) flowableBaseService.taskDataEntry(task, billingProfileCmsIdBean, fMap);
	}

	/***
	 * This method is used to approve or reject the document for Billing Profile
	 * task.
	 ***/
	@Transactional(readOnly = false)
	public BillingProfileApprovalBean billingProfileApproval(BillingProfileApprovalBean billingProfileApprovalBean)
			throws TclCommonException {
		LOGGER.info("Inside Billing Profile Approval Method");

		if (Objects.nonNull(billingProfileApprovalBean)) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("billingProfileStatus", billingProfileApprovalBean.getStatus());
			atMap.put("billingProfileRemarks", billingProfileApprovalBean.getRemarks());
			
			List<Integer> serviceIdList = billingProfileApprovalBean.getId();
			for (Integer serviceId : serviceIdList) {
				Optional<ScServiceDetail> optScServiceDetail = scServiceDetailRepository.findById(serviceId);
				String orderType = "";
				String serviceType = "";
				String accessType = "";
				if(optScServiceDetail.isPresent()) {
					orderType = optScServiceDetail.get().getOrderType();
					serviceType = optScServiceDetail.get().getErfPrdCatalogOfferingName();
					accessType = optScServiceDetail.get().getAccessType();
				}
				if (!CollectionUtils.isEmpty(billingProfileApprovalBean.getPhysicalAddressId())) {
					atMap.put("physicalAddressId", String.join(",",billingProfileApprovalBean.getPhysicalAddressId()));
				}else if("NEW".equalsIgnoreCase(orderType) && serviceType.contains(GscConstants.SIP) && !GscConstants.PSTN.equalsIgnoreCase(accessType)){
					LOGGER.info("Invalid Physical Address ID..!");
					throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
				}
				componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE,
						"A");
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}

		LOGGER.info("Exit from Billing Profile Approval Method");
		return billingProfileApprovalBean;
	}

	public List<NumberCountryDetailsBean> getNumberCountryDetails(Integer taskId, String wfTaskId) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(task.getServiceId());
		List<NumberCountryDetailsBean> numberCountryDetailsList = new ArrayList<NumberCountryDetailsBean>();
		if (scServiceDetailOpt.isPresent()) {
			final ScServiceDetail scServiceDetail = scServiceDetailOpt.get();

			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

			List<GscScAsset> torScAssets = scAssetRepository.findByServiceIdandTypeandAssetId(task.getServiceId(), "Toll-Free", task.getGscFlowGroupId(), GscNumberStatus.IN_SERVICE_ACCEPT);
			torScAssets.forEach(scAssets -> {
				NumberCountryDetailsBean numberCountryDetailsBean = new NumberCountryDetailsBean();
				numberCountryDetailsBean.setOriginCountry(scServiceDetail.getSourceCountry());
				numberCountryDetailsBean.setDestinationCountry(scServiceDetail.getDestinationCountry());
				numberCountryDetailsBean.setId(scAssets.getTollfreeId());
				numberCountryDetailsBean.setNumbers(scAssets.getTollfreeName());
				numberCountryDetailsBean.setAccessType(scServiceDetail.getAccessType());

				numberCountryDetailsBean
						.setOutpulse(scComponentAttributesmap.getOrDefault("terminationNumberIsdCode", "") + "-"
								+ scAssets.getOutpulseName());
				List<String> callTypeList = gscService.getRepcCallTypeList(scServiceDetail.getId());
				numberCountryDetailsBean.setCallType(String.join(",", callTypeList));
				ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
						.findByScAsset_IdAndAttributeName(scAssets.getTollfreeId(), "billingStartDate");
				if (Objects.nonNull(scAssetAttribute)) {
					String value = scAssetAttribute.getAttributeValue();
					if(GscConstants.Y.equals(scAssetAttribute.getIsAdditionalParam())) {
						Optional<ScAdditionalServiceParam> optAdditionParam = scAdditionalServiceParamRepository.findById(Integer.parseInt(value));
						if(optAdditionParam.isPresent()) {
							value = optAdditionParam.get().getValue();
						}
					}
					
					numberCountryDetailsBean.setBillingStartDate(value);
				}
				numberCountryDetailsList.add(numberCountryDetailsBean);
			});
		}
		return numberCountryDetailsList;
	}

	public List<TestNumberBasicEnrichBean> getPortingOutpulseBasicEnrich(String serviceId) {
		List<TestNumberBasicEnrichBean> testNumberBeans = new ArrayList<>();
		Optional<ScServiceDetail> optScServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		String productName = "";
		if(optScServiceDetail.isPresent()) {
			productName = optScServiceDetail.get().getErfPrdCatalogOfferingName();
		}
		if(productName.contains(GscConstants.DOMESTIC_VOICE)) {
			List<GscScAsset> torScAssets = scAssetRepository.getPortingNumWithNoOutpulse(Integer.parseInt(serviceId),GscConstants.YES);
			torScAssets.forEach(toRscAsset -> {
				TestNumberBasicEnrichBean testNumberBean = new TestNumberBasicEnrichBean();
				testNumberBean.setTollfreeId(toRscAsset.getTollfreeId());
				testNumberBean.setTollfree(toRscAsset.getTollfreeName());
				testNumberBeans.add(testNumberBean);
			});
		}else {
		
			List<GscScAsset> torScAssets = scAssetRepository.getTollFreeAndRoutingFromOutpuse(Integer.parseInt(serviceId));
			torScAssets.forEach(toRscAsset -> {
				TestNumberBasicEnrichBean testNumberBean = new TestNumberBasicEnrichBean();
				testNumberBean.setTollfreeId(toRscAsset.getTollfreeId());
				testNumberBean.setTollfree(toRscAsset.getTollfreeName());
				testNumberBean.setCustomerOutpulseId(toRscAsset.getOutpulseId());
				testNumberBean.setCustomerOutpulse(toRscAsset.getOutpulseName());
				testNumberBean.setRoutingNumber(toRscAsset.getRoutingName());
				ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
						.findByScAsset_IdAndAttributeName(toRscAsset.getTollfreeId(), "cityCode");
				if (Objects.nonNull(scAssetAttribute)) {
					testNumberBean.setCityCode(scAssetAttribute.getAttributeValue());
				}
				testNumberBeans.add(testNumberBean);
			});
		}
		return testNumberBeans;
	}

	/*** Service Acceptance task - Close ***/
	@Transactional(readOnly = false)
	public ServiceAcceptanceBean serviceAcceptence(ServiceAcceptanceBean serviceAcceptanceBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(serviceAcceptanceBean.getTaskId(), serviceAcceptanceBean.getWfTaskId());
		validateInputs(task, serviceAcceptanceBean);

		String userName = Utils.getSource();
		Map<String, Object> fMap = new HashMap<>();
		fMap.put("atleastOneNumRejected", "no");
		fMap.put("atleastOneNumAccepted", "no");
		if (Objects.nonNull(serviceAcceptanceBean.getRejectScAssetsId()) && !serviceAcceptanceBean.getRejectScAssetsId().isEmpty()) {
				fMap.put("atleastOneNumRejected", "yes");
				serviceAcceptanceBean.getRejectScAssetsId().forEach(scAssetId -> {
					gscService.updatedScAssetStatus(scAssetId, GscNumberStatus.IN_RE_TEST_NUMBER, userName);
				});	
		}

		if (Objects.nonNull(serviceAcceptanceBean.getAcceptScAssetsId()) && !serviceAcceptanceBean.getAcceptScAssetsId().isEmpty()) {
			serviceAcceptanceBean.getAcceptScAssetsId().forEach(scAssetId -> {
				gscService.updatedScAssetStatus(scAssetId, GscNumberStatus.IN_SERVICE_ACCEPTED, userName);
			});
			fMap.put("atleastOneNumAccepted", "yes");
		}

		processTaskLogDetails(task, "CLOSED", serviceAcceptanceBean.getDelayReason(), null, serviceAcceptanceBean);
		return (ServiceAcceptanceBean) flowableBaseService.taskDataEntry(task, serviceAcceptanceBean, fMap);
	}

	/*** Taxation - Close ***/
	@Transactional(readOnly = false)
	public TaxationBean closeTaxation(TaxationBean taxationBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(taxationBean.getTaskId(), taxationBean.getWfTaskId());
		validateInputs(task, taxationBean);
		
		if(Objects.nonNull(taxationBean.getTaxExempt())) {
			String userName = Utils.getSource();
			ScOrderAttribute scOrderAttribute = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(task.getScServiceDetail().getScOrder().getId(), "TAX_EXEMPT");
			if(scOrderAttribute == null)
				scOrderAttribute = new ScOrderAttribute();
			scOrderAttribute.setAttributeName("TAX_EXEMPT");
			scOrderAttribute.setAttributeValue(taxationBean.getTaxExempt());
			scOrderAttribute.setScOrder(task.getScServiceDetail().getScOrder());
			scOrderAttribute.setAttributeAltValueLabel("TAX_EXEMPT");
			scOrderAttribute.setCreatedBy(userName);
			scOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scOrderAttribute.setIsActive(CommonConstants.Y);
			scOrderAttribute.setUpdatedBy(userName);
			scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scOrderAttributeRepository.save(scOrderAttribute);
		}
		
		Map<String, Object> fMap = new HashMap<>();
		if (taxationBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("isTaxationValid", "valid");
		} else {
			fMap.put("isTaxationValid", "invalid");
		}

		processTaskLogDetails(task, "CLOSED", taxationBean.getDelayReason(), null, taxationBean);
		return (TaxationBean) flowableBaseService.taskDataEntry(task, taxationBean, fMap);
	}
	
	/*** Commercial Vetting - Close ***/
	@Transactional(readOnly = false)
	public TaskDetailsBaseBean closeCommercialVetting(TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(taskDetailsBaseBean.getTaskId(), taskDetailsBaseBean.getWfTaskId());
		validateInputs(task, taskDetailsBaseBean);
		
		Map<String, Object> fMap = new HashMap<>();
		if (taskDetailsBaseBean.getAction().equalsIgnoreCase("RAISE-BILLING")) {
			fMap.put("raiseDependecyto", "cmsbilling");
			copyApprovalStatusToCountries(task.getServiceId(), "commVettingStatus", "commVettingRemarks", "billingProfileStatus", "billingProfileRemarks");
		} else if (taskDetailsBaseBean.getAction().equalsIgnoreCase("RAISE-VD")) {
			fMap.put("raiseDependecyto", "validatedoc");
			copyApprovalStatusToCountries(task.getServiceId(), "commVettingStatus", "commVettingRemarks", "validDocStatus", "validDocRemarks");
		} else if (taskDetailsBaseBean.getAction().equalsIgnoreCase("RAISE-RATE")) {
			fMap.put("raiseDependecyto", "rateupload");
			copyApprovalStatusToCountries(task.getServiceId(), "commVettingStatus", "commVettingRemarks", "rateUploadStatus", "rateUploadRemarks");
		} else {
			fMap.put("raiseDependecyto", "none");
		}
		
		processTaskLogDetails(task, "CLOSED", taskDetailsBaseBean.getDelayReason(), null, taskDetailsBaseBean);
		return (TaskDetailsBaseBean) flowableBaseService.taskDataEntry(task, taskDetailsBaseBean, fMap);
	}
	
	/*** UIFN Procurement - Close ***/
	@Transactional(readOnly = false)
	public UifnProcurementListBean closeUifnProcure(UifnProcurementListBean uifnProcurementListBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(uifnProcurementListBean.getTaskId(), uifnProcurementListBean.getWfTaskId());
		validateInputs(task, uifnProcurementListBean);
		
		String userName = Utils.getSource();
		if(Objects.nonNull(uifnProcurementListBean.getUifnNumbers())) {
			uifnProcurementListBean.getUifnNumbers().forEach(uifnProcurementBean -> {
				if(Objects.nonNull(uifnProcurementBean.getNumbers()) && !uifnProcurementBean.getNumbers().isEmpty()) {
					Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(uifnProcurementBean.getServiceId());
					if(scServiceDetailOpt.isPresent()) {
						ScServiceDetail scServiceDetail = scServiceDetailOpt.get();
						if(scServiceDetail.getErfPrdCatalogOfferingName().contains("UIFN")) {
							List<ScAsset> scAssets = new ArrayList<ScAsset>();
							uifnProcurementBean.getNumbers().forEach(number -> {
								scAssets.add(gscService.generateScAsset(scServiceDetail, number, "Toll-Free", userName));
							});
							scAssetRepository.saveAll(scAssets);
							List<IGscScAsset> unAssociatedOutpulse = scAssetRepository.getUnAssociatedOutpulse(scServiceDetail.getId());
							if(!scAssets.isEmpty() && !unAssociatedOutpulse.isEmpty()) {
								List<ScAssetRelation> scAssetRelations = new ArrayList<ScAssetRelation>();
								for (int i = 0; i < scAssets.size(); i++) {
									ScAsset tollFreeAsset = scAssets.get(i);
									IGscScAsset outpulseAsset = unAssociatedOutpulse.get(i);
									scAssetRelations.add(gscService.generateScAssetRelations(tollFreeAsset.getId(), outpulseAsset.getAssetId(), "Outpulse", userName));
									scAssetRelations.add(gscService.generateScAssetRelations(outpulseAsset.getAssetId(), tollFreeAsset.getId(), "Toll-Free", userName));
								}
								scAssetRelationRepository.saveAll(scAssetRelations);
							}
						}
					}
				}
			});
		}

		processTaskLogDetails(task, "CLOSED", uifnProcurementListBean.getDelayReason(), null, uifnProcurementListBean);
		return (UifnProcurementListBean) flowableBaseService.taskDataEntry(task, uifnProcurementListBean);
	}
	
	private void copyApprovalStatusToCountries(Integer serviceId, String srcAttributeStatus, String srcAttributeRemarks, String destAttributeStatus, String destAttributeRemarks) {
		List<Integer> countryLevelFlowServices = gscService.getCountryLevelFlowService(serviceId);
		Map<String, String> atMap = new HashMap<>();
		List<String> attributes = Arrays.asList(srcAttributeStatus, srcAttributeRemarks);
		for (Integer countryLevelFlowService : countryLevelFlowServices) {
			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(
					countryLevelFlowService, AttributeConstants.COMPONENT_GSCLICENSE, "A", attributes);
			if (Objects.nonNull(scComponentAttributesmap.get(srcAttributeStatus))
					&& scComponentAttributesmap.get(srcAttributeStatus).equalsIgnoreCase("INVALID")) {
				atMap.put(destAttributeStatus, scComponentAttributesmap.get(srcAttributeStatus));
				atMap.put(destAttributeRemarks, scComponentAttributesmap.get(srcAttributeRemarks));
				componentAndAttributeService.updateAttributes(countryLevelFlowService, atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, "A");
			}
		}
	}
	
	@Transactional(readOnly = false)
	public CommVettingApprovalBean commVettingApproval(
			CommVettingApprovalBean commVettingApprovalBean) throws TclCommonException {
		LOGGER.info("Inside Commerical Vetting Approval Method");

		if (Objects.nonNull(commVettingApprovalBean)) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("commVettingStatus", commVettingApprovalBean.getStatus());
			atMap.put("commVettingRemarks", commVettingApprovalBean.getRemarks());

			List<Integer> serviceIdList = commVettingApprovalBean.getId();
			for (Integer serviceId : serviceIdList) {
				componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE,
						"A");
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Exit from Commerical Vetting Approval");

		return commVettingApprovalBean;
	}

	public List<TrunkBean> getTrunks(Integer serviceId){
		List<TrunkBean> trunks=new ArrayList<>();
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAndAdditionalAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A");
		String trunksString = scComponentAttributesmap.getOrDefault("trunks", null);
		if(Objects.nonNull(trunksString)){
			trunks=Utils.fromJson(trunksString, new TypeReference<List<TrunkBean>>() {
			});
		}
		return trunks;
	}
	
	/*** This method is used to approve or reject the document for Voice Sales Engr task ***/
    @Transactional(readOnly = false)
	public VoiceSalesEngrApprovalBean voiceSalesApproval(VoiceSalesEngrApprovalBean voiceSalesApprovalBean) throws TclCommonException {
    	LOGGER.info("Inside Voice Sales Engr Approval Method");
    	if (Objects.nonNull(voiceSalesApprovalBean)) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("voiceSalesEngrStatus", voiceSalesApprovalBean.getStatus());
			atMap.put("voiceSalesEngrRemarks", voiceSalesApprovalBean.getRemarks());

			List<Integer> serviceIdList = voiceSalesApprovalBean.getId();
			for (Integer serviceId : serviceIdList) {
				Optional<ScServiceDetail> optScServiceDetail = scServiceDetailRepository.findById(serviceId);
				String orderType = "";
				String serviceType = "";
				String accessType = "";
				if(optScServiceDetail.isPresent()) {
					orderType = optScServiceDetail.get().getOrderType();
					serviceType = optScServiceDetail.get().getErfPrdCatalogOfferingName();
					accessType = optScServiceDetail.get().getAccessType();
				}
				if (!CollectionUtils.isEmpty(voiceSalesApprovalBean.getTrunks())) { 
					atMap.put("trunks", Utils.convertObjectToJson(voiceSalesApprovalBean.getTrunks()));
				}else if("NEW".equalsIgnoreCase(orderType) && serviceType.contains(GscConstants.SIP) && !GscConstants.PSTN.equalsIgnoreCase(accessType)){
					LOGGER.info("Invalid - No Trunk Details Found...!");
					throw new TclCommonException(ResponseResource.RES_NO_TRUNKS_DATA, ResponseResource.R_CODE_BAD_REQUEST);
				}
//              componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE,"A");
				componentAndAttributeService.updateComponentAndAdditionalAttributes(serviceId, atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, "A");
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}

		LOGGER.info("Voice Sales Engr Approval");
    	
		return voiceSalesApprovalBean;
    	
	}

    /*** Voice Sales Engr - Close ***/
	@Transactional(readOnly = false)
	public VoiceSalesEngrBean voiceSalesEngr(VoiceSalesEngrBean voiceSalesBean) throws TclCommonException {
		LOGGER.info("Inside  Voice Sales Engr Method");

		Task task = getTaskByIdAndWfTaskId(voiceSalesBean.getTaskId(), voiceSalesBean.getWfTaskId());

		validateInputs(task, voiceSalesBean);

		Map<String, Object> fMap = new HashMap<>();

		if (voiceSalesBean.getAction().equalsIgnoreCase("CLOSE")) {
			fMap.put("voicesalesRejTo", "valid");
		} else if (voiceSalesBean.getAction().equalsIgnoreCase("RAISE-BE")) {
			fMap.put("voicesalesRejTo", "enrichment");
			copyApprovalStatusToCountries(task.getServiceId(), "voiceSalesEngrStatus", "voiceSalesEngrRemarks",
					"basicEnrichmentStatus", "basicEnrichmentRemarks");
		} else if (voiceSalesBean.getAction().equalsIgnoreCase("RAISE-VD")) {
			fMap.put("voicesalesRejTo", "validatedoc");
			copyApprovalStatusToCountries(task.getServiceId(), "voiceSalesEngrStatus", "voiceSalesEngrRemarks",
					"validDocStatus", "validDocRemarks");
		}

		processTaskLogDetails(task, "CLOSED", voiceSalesBean.getDelayReason(), null, voiceSalesBean);

		LOGGER.info("Exit  Voice Sales Engr Method");
		return (VoiceSalesEngrBean) flowableBaseService.taskDataEntry(task, voiceSalesBean, fMap);
	}

	/*** Fetching filtered Sites from REPC ***/
	public SitesBean getSites(Integer serviceId) {
		LOGGER.info("Inside  Get Sites Method");
		SitesBean sitesBean = new SitesBean();
//       Map<String, String> scComponentAttributesmap =
//             commonFulfillmentUtils.getComponentAttributes(serviceId,AttributeConstants.COMPONENT_GSCLICENSE,"A");
		 Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
				.getComponentAndAdditionalAttributes(serviceId, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		String sites = scComponentAttributesmap.getOrDefault("filteredSitesResponse", "");
		if (Objects.nonNull(sites)) {
			sitesBean = Utils.fromJson(sites, new TypeReference<SitesBean>() {});
		}
		LOGGER.info("Exit  Get Sites Method");
		return sitesBean;
	}
	
	/*** Saving the selected Site ***/
	@Transactional(readOnly = false)
	public SelectedSiteBean saveSelectedSite(SelectedSiteBean selectedSiteBean) throws TclCommonException {
		LOGGER.info("Inside  Save Selected site Method");
		Task task = getTaskByIdAndWfTaskId(selectedSiteBean.getTaskId(), selectedSiteBean.getWfTaskId());

		validateInputs(task, selectedSiteBean);
		if (Objects.nonNull(selectedSiteBean)) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("selectedSiteId",selectedSiteBean.getSelectedSite().getSiteId());
			atMap.put("selectedSite", Utils.convertObjectToJson(selectedSiteBean.getSelectedSite()));
//          componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,AttributeConstants.COMPONENT_GSCLICENSE, "A");
			componentAndAttributeService.updateComponentAndAdditionalAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		}

		processTaskLogDetails(task, "CLOSED", selectedSiteBean.getDelayReason(), null, selectedSiteBean);
		LOGGER.info("Exit  Save Selected site Method");
		return (SelectedSiteBean) flowableBaseService.taskDataEntry(task, selectedSiteBean);
	}
	
	/*** Creating New Site ***/
	@Transactional(readOnly = false)
	public CreateSiteRequestBean createNewSite(CreateSiteRequestBean createSiteRequestBean) throws TclCommonException {
		LOGGER.info("Inside Create New Site Method");
		Task task = getTaskByIdAndWfTaskId(createSiteRequestBean.getTaskId(), createSiteRequestBean.getWfTaskId());
		validateInputs(task, createSiteRequestBean);
		if (Objects.nonNull(createSiteRequestBean)) {
			CreateSiteRepcRequest createSiteRepcRequest = new CreateSiteRepcRequest();
			createSiteRepcRequest.setCustomerId(createSiteRequestBean.getCustomerId());
			createSiteRepcRequest.setSiteDetails(createSiteRequestBean.getSiteDetails());
			if (Objects.nonNull(createSiteRepcRequest)) {
				try {
					String status = repcService.requestForCreateNewSite(Utils.convertObjectToJson(createSiteRepcRequest), task.getServiceId(),
							                   task.getServiceCode(), task.getWfProcessInstId());
					if (status.equalsIgnoreCase("success")) {
						createSiteRequestBean.setStatus(status);

//                      Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
//                              .getComponentAttributes(task.getServiceId(), "LM","A");
						Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
								.getComponentAndAdditionalAttributes(task.getServiceId(),AttributeConstants.COMPONENT_GSCLICENSE, "A");
						SelectedSite selectedSite = new SelectedSite();
						SiteDetailsBean siteDetail = createSiteRepcRequest.getSiteDetails().get(0);
						selectedSite.setSiteId(scComponentAttributesmap.getOrDefault("selectedSiteId", ""));
						selectedSite.setSiteName(siteDetail.getSiteName());
						List<SiteFunctionsBean> siteFunctionsList = siteDetail.getSiteFunctions();
						List<String> ss = new ArrayList<>();
						for (SiteFunctionsBean site : siteFunctionsList) {
							ss.add(site.getSiteFunctionCd());
						}
						selectedSite.setSiteFunctions(ss);
						selectedSite.setEquipmentId(siteDetail.getAddressSeqNo());
						selectedSite.setCityAbbr(siteDetail.getLocation().get(0).getCityAbbr());
						selectedSite.setCountryAbbr(siteDetail.getLocation().get(0).getCountryAbbr());
						selectedSite.setGeoCode(siteDetail.getGeoSpaceCode());

						Map<String, String> atMap = new HashMap<>();
						atMap.put("selectedSite", Utils.convertObjectToJson(selectedSite));

//                      componentAndAttributeService.updateAttributes(task.getServiceId(),atMap,
//                              AttributeConstants.COMPONENT_GSCLICENSE, "A");
						componentAndAttributeService.updateComponentAndAdditionalAttributes(task.getServiceId(), atMap,
								AttributeConstants.COMPONENT_GSCLICENSE, "A");
						
						LOGGER.info("Selected Site Details {}", Utils.convertObjectToJson(selectedSite));
						processTaskLogDetails(task, "CLOSED", createSiteRequestBean.getDelayReason(), null,createSiteRequestBean);
						flowableBaseService.taskDataEntry(task, createSiteRequestBean);
						return createSiteRequestBean;
					} else {
						createSiteRequestBean.setStatus(status);
						throw new TclCommonException(ResponseResource.RES_FAILURE,ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}
				} catch (Exception e) {
					createSiteRequestBean.setStatus(e.getMessage());
					throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_ERROR);
				}
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Exit from Create New Site Method");
		return createSiteRequestBean;
	}
    
	/*** Fetching Selected Supplier Trunks ***/
	public List<SharedInCircuitBean> getSelectedSupplierTrunks(Integer serviceId) {
		LOGGER.info("Inside  Get Selected Supplier Trunks Method");
//      Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
//                 AttributeConstants.COMPONENT_GSCLICENSE, "A");
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAndAdditionalAttributes(serviceId,
				   AttributeConstants.COMPONENT_GSCLICENSE, "A");
		List<SharedInCircuitBean> sharedInCircuitList = new ArrayList<>();
		String selectedSupplier = scComponentAttributesmap.getOrDefault("selectedSuppliers", null);
		if (Objects.nonNull(selectedSupplier)) {
			List<DIDSupplierBean> suppliers = Utils.fromJson(selectedSupplier,new TypeReference<List<DIDSupplierBean>>() {});
			for (DIDSupplierBean supplier : suppliers) {
				sharedInCircuitList.addAll(supplier.getSharedInCircuitGroups());
			}
		}
		LOGGER.info("Exit  Get Selected Supplier Trunks Method");
		return sharedInCircuitList;
	}
	
    @Transactional(readOnly = false)
	public DIDNumberTestBean closeDidNumberTestingTask(DIDNumberTestBean didNumberTaskBean) throws TclCommonException {
		LOGGER.info("Inside close DID Number Testing Task for service code {}", didNumberTaskBean.getServiceCode());
		Task task = getTaskByIdAndWfTaskId(didNumberTaskBean.getTaskId(), didNumberTaskBean.getWfTaskId());
		Map<String, Object> processVar = new HashMap<>();
		validateInputs(task, didNumberTaskBean);
		if (Objects.nonNull(didNumberTaskBean)) {

			if (didNumberTaskBean.getDocumentIds() != null && !didNumberTaskBean.getDocumentIds().isEmpty()) {
				didNumberTaskBean.getDocumentIds()
						.forEach(attachmentIdBean ->
								makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}

			
			String flowGroupName=null;
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-testing")) {
				flowGroupName="ProcessSupplierOrderDID-" + "NEW-ORDER-TESTING";
			}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-testing")) {
				flowGroupName="ProcessSupplierOrderDID-" + "PORT-CHANGE-TESTING";

			}
			if (flowGroupName != null) {
				List<Integer> scAssetToMap = didNumberTaskBean.getAssets().stream().map(ass -> ass.getId())
						.collect(Collectors.toList());

				GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup(flowGroupName,
						task.getWfProcessInstId(), "ProcessInstanceId", "system", scAssetToMap);
				processVar.put(GscConstants.NEXT_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
				if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-testing")
						|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-re-testing")) {
					processVar.put(GscConstants.NEXT_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
				} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-testing")
						|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-re-testing")) {
					processVar.put(GscConstants.NEXT_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());

				}	
			}
			
			didNumberTaskBean.getAssets().forEach(asset -> {
				Optional<ScAsset> optionalScAsset = scAssetRepository.findById(asset.getId());
				if (optionalScAsset.isPresent()) {
					ScAsset scAsset = optionalScAsset.get();
					if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-testing")
							|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-re-testing")) {
						scAsset.setStatus("did-new-number-service-acceptence");
					}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-testing") ||
							task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-re-testing")) {
						scAsset.setStatus("did-porting-number-service-acceptence");
					}
					
					if(asset.getBillingDate()!=null) {
					saveOrUpdateScAssetAttribute(scAsset, "billingDate", asset.getBillingDate());
					}
					if(asset.getCdrLog()!=null) {

					
					saveOrUpdateScAssetAttribute(scAsset, "cdrLog", asset.getCdrLog());
					}
					
					scAssetRepository.save(scAsset);
				}
			});
			
			List<ScAsset> scAssets = null;
			
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-testing")
					|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-re-testing")) {
				scAssets = getScAssets(task, task.getMstTaskDef().getKey());
				
				if(scAssets != null && !scAssets.isEmpty()) {
				LOGGER.info("New number Testing is pending for service code {}", didNumberTaskBean.getServiceCode());
				processVar.put("atleastOneNewNumberPendingForTest", "yes");
				}else {
					processVar.put("atleastOneNewNumberPendingForTest", "no");
				}
			}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-testing")||
					task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-re-testing")) {
				
				scAssets = getScAssets(task, task.getMstTaskDef().getKey());
				
				if(scAssets != null && !scAssets.isEmpty()) {
				LOGGER.info("Port number testing is pending for service code {}", didNumberTaskBean.getServiceCode());
				processVar.put("atleastOnePortingNumberPendingForTest", "yes");
				}else {
					processVar.put("atleastOnePortingNumberPendingForTest", "no");
				}

			}
			
		}


		processTaskLogDetails(task, "CLOSED", didNumberTaskBean.getDelayReason(), null, didNumberTaskBean);
		LOGGER.info("Exit close DID Number Testing Task Method for service code {}", didNumberTaskBean.getServiceCode());
		return (DIDNumberTestBean) flowableBaseService.taskDataEntry(task, didNumberTaskBean, processVar);
	}
    
    
    @Transactional(readOnly = false)
   	public DIDServiceAcceptenceBean closeDidNumberServiceAcceptenceTask(DIDServiceAcceptenceBean didNumberTaskBean) throws TclCommonException {
   		LOGGER.info("Inside close DID Number Testing Task for service code {}", didNumberTaskBean.getServiceCode());
   		Task task = getTaskByIdAndWfTaskId(didNumberTaskBean.getTaskId(), didNumberTaskBean.getWfTaskId());
   		Map<String, Object> processVar = new HashMap<>();
   		validateInputs(task, didNumberTaskBean);
   		if (Objects.nonNull(didNumberTaskBean)) {

   			if (didNumberTaskBean.getDocumentIds() != null && !didNumberTaskBean.getDocumentIds().isEmpty()) {
   				didNumberTaskBean.getDocumentIds()
   						.forEach(attachmentIdBean ->
   								makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
   			}

   			processVar.put("atleastOnePortingNumberRejected", "no");
   			processVar.put("atleastOnePortingNumAccepted", "no");
   			processVar.put("atleastOneNumAccepted", "no");
   			processVar.put("atleastDidNewNumRejected", "no");
   			
   			
   			for (OdrAssetBean asset : didNumberTaskBean.getAssets()) {
				
   				Optional<ScAsset> optionalScAsset = scAssetRepository.findById(asset.getId());
   				if (optionalScAsset.isPresent()) {
   					ScAsset scAsset = optionalScAsset.get();
   					if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-service-acceptence")) {
   						if(asset.getServiceAcceptenceStatus() != null && asset.getServiceAcceptenceStatus().equalsIgnoreCase("accept")) {
   							scAsset.setStatus("did-new-number-patch");
   								processVar.put("atleastOneNumAccepted", "yes");
   						} else if (asset.getServiceAcceptenceStatus() != null && asset.getServiceAcceptenceStatus().equalsIgnoreCase("reject")) {
   							scAsset.setStatus("did-new-number-re-testing");
   								processVar.put("atleastDidNewNumRejected", "yes");
   						}
   					}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-service-acceptence")) {
   						if(asset.getServiceAcceptenceStatus() != null && asset.getServiceAcceptenceStatus().equalsIgnoreCase("accept")) {
   							scAsset.setStatus("did-porting-number-patch");
   								processVar.put("atleastOnePortingNumAccepted", "yes");
   						} else if (asset.getServiceAcceptenceStatus() != null && asset.getServiceAcceptenceStatus().equalsIgnoreCase("reject")) {
   							scAsset.setStatus("did-porting-number-re-testing");
   								processVar.put("atleastOnePortingNumberRejected", "yes");
   						}
   					}
   					
   					
   					scAssetRepository.save(scAsset);
   				}
   			}
   			
   			
   		}
   		
   		processTaskLogDetails(task, "CLOSED", didNumberTaskBean.getDelayReason(), null, didNumberTaskBean);
   		LOGGER.info("Exit close DID Number Testing Task Method for service code {}", didNumberTaskBean.getServiceCode());
   		return (DIDServiceAcceptenceBean) flowableBaseService.taskDataEntry(task, didNumberTaskBean, processVar);
   	}

	@Transactional(readOnly = false)
	public DidNumberTaskBean closeDidNumberTask(DidNumberTaskBean didNumberTaskBean) throws TclCommonException {
		LOGGER.info("Inside close DID Number Task");
		Task task = getTaskByIdAndWfTaskId(didNumberTaskBean.getTaskId(), didNumberTaskBean.getWfTaskId());
		Map<String, Object> processVar = new HashMap<>();
		validateInputs(task, didNumberTaskBean);
		if (Objects.nonNull(didNumberTaskBean)) {

			if (didNumberTaskBean.getDocumentIds() != null && !didNumberTaskBean.getDocumentIds().isEmpty()) {
				didNumberTaskBean.getDocumentIds()
						.forEach(attachmentIdBean ->
								makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}

			Map<String, String> atMap = new HashMap<>();
			atMap.put("didReferenceNo", didNumberTaskBean.getReferenceNo());

			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
					AttributeConstants.COMPONENT_GSCLICENSE, "A");
			
			
			String flowGroupName=null;
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-new-order")) {
				flowGroupName="ProcessSupplierOrderDID-" + "NEW-ORDER";
			}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-porting-order")) {
				flowGroupName="ProcessSupplierOrderDID-" + "PORT-CHANGE";

			}
			List<Integer> scAssetToMap=didNumberTaskBean.getAssets().stream().map(ass->ass.getId()).collect(Collectors.toList());
			GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup(
					flowGroupName, task.getWfProcessInstId(),
					"ProcessInstanceId", "system", scAssetToMap);
			processVar.put(GscConstants.NEXT_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-new-order")) {
				processVar.put(GscConstants.NEXT_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
			}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-porting-order")) {
				processVar.put(GscConstants.NEXT_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());

			}
			
			didNumberTaskBean.getAssets().forEach(asset -> {
				Optional<ScAsset> optionalScAsset = scAssetRepository.findById(asset.getId());
				if (optionalScAsset.isPresent()) {
					ScAsset scAsset = optionalScAsset.get();
					if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-new-order")) {
						LOGGER.info("Place Supplier did new order for asset {} for service code {}", scAsset.getName(), task.getServiceCode());
						scAsset.setName(asset.getName());
						scAsset.setStatus("did-number-info-rs-config");
					}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-porting-order")) {
						LOGGER.info("Place Supplier did porting order for asset {} for service code {}", scAsset.getName(), task.getServiceCode());
						scAsset.setStatus("did-porting-number-info-rs-config");
					}
					String attributeName = "supplierActivationDate";
					String value = asset.getSupplierActivationDate();
					saveOrUpdateScAssetAttribute(scAsset, attributeName, value);
					scAssetRepository.save(scAsset);
				}
			});
			
			List<ScAsset> scAssets = null;
			
			LOGGER.info("Checking for Pending details start for service code {}", task.getServiceCode());
			
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-new-order")) {
				
				scAssets = getScAssets(task, "place-supplier-DID-new-order");
				
				if(scAssets != null && !scAssets.isEmpty()) {
				LOGGER.info("New number is pending for service code {} and size {}", task.getServiceCode(), scAssets.size());
				processVar.put("atleastOneNewNumberPending", "yes");
				}else {
					processVar.put("atleastOneNewNumberPending", "no");
				}
			}else if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-supplier-DID-porting-order")) {
				
				scAssets = getScAssets(task, "place-supplier-DID-porting-order");
				LOGGER.info("Port number is pending for service code {} and  size:{}", didNumberTaskBean.getServiceCode(),scAssets);

				if(scAssets != null && !scAssets.isEmpty()) {
				LOGGER.info("Port number is pending for service code {} and size {}", task.getServiceCode(), scAssets.size());
				processVar.put("atleastOnePortingNumberPending", "yes");
				}else {
					processVar.put("atleastOnePortingNumberPending", "no");
				}

			}
			
			LOGGER.info("Checking for Pending details end for service code {}", task.getServiceCode());
			
		}
		List<String> attributes = new ArrayList<String>();
		attributes.add("sipServiceId");
		attributes.add("routeLabel");
		processVar.put("waitForSip", "yes");

		Map<String, String> scComponentAttributes = commonFulfillmentUtils.getComponentAttributes(task.getServiceId(),
				AttributeConstants.COMPONENT_LM, "A", attributes);
		
		if(scComponentAttributes.containsKey("sipServiceId")) {
			Map<String, String> sipAttributes = commonFulfillmentUtils.getComponentAttributes(Integer.valueOf(scComponentAttributes.get("sipServiceId")),
					AttributeConstants.COMPONENT_LM, "A", attributes);
			if(sipAttributes.containsKey("routeLabel")) {
				LOGGER.info("Sip route lable attributes present for service id:{} and lable value {}",task.getServiceCode(),sipAttributes.get("routeLabel"));
				processVar.put("waitForSip", "no");
			}
		}
		


		processTaskLogDetails(task, "CLOSED", didNumberTaskBean.getDelayReason(), null, didNumberTaskBean);
		LOGGER.info("Exit close DID Number Task Method for service code {}");
		return (DidNumberTaskBean) flowableBaseService.taskDataEntry(task, didNumberTaskBean, processVar);
	}
	
	private List<ScAsset> getScAssets(Task task, String status) {
		List<ScAsset> scAssets = null;
		List<GscFlowGroupToAsset> flowGroupToAssets = gscFlowGroupToAssetRepository
				.findByGscFlowGroupId(task.getGscFlowGroupId());
		if(!flowGroupToAssets.isEmpty()) {
		List<Integer> assetIds = flowGroupToAssets.stream().map(e -> e.getScAssetId()).collect(Collectors.toList());
		if(assetIds != null && !assetIds.isEmpty()) {
			scAssets= scAssetRepository.findByIdInAndStatus(assetIds, status);
		}
		}
		return scAssets;
	}

	private void saveOrUpdateScAssetAttribute(ScAsset scAsset, String attributeName, String value) {
		ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
				.findByScAsset_IdAndAttributeName(scAsset.getId(), attributeName);
		if(scAssetAttribute != null) {
			scAssetAttribute
					.setAttributeValue(value);
		}else {
			ScAssetAttribute scAssetAttributeNew = new ScAssetAttribute();
			scAssetAttributeNew.setAttributeName(attributeName);
			scAssetAttributeNew.setScAsset(scAsset);
			scAssetAttributeNew.setAttributeValue(value);
			scAssetAttributeNew.setIsActive("Y");
			scAssetAttributeNew.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			scAssetAttributeRepository.save(scAssetAttributeNew);
		}
	}
	
	/**
	 * 
	 * @param taskId
	 * @param wfTaskId
	 * @return
	 */
	public DIDNumberDetails getDIDNumberDetails(Integer taskId, String wfTaskId) {
		LOGGER.info("Inside  getDIDNumberDetails");
		
		DIDNumberDetails details = new DIDNumberDetails();
		List<OdrAssetBean> assets = new ArrayList<>();
		List<DIDSupplierBean> supplierBean = new ArrayList<>();
		
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		if (task != null) {
			LOGGER.info("flow group id:{}",task.getGscFlowGroupId());
			List<GscFlowGroupToAsset> flowGroupToAssets = gscFlowGroupToAssetRepository
					.findByGscFlowGroupId(task.getGscFlowGroupId());
			if(!flowGroupToAssets.isEmpty()) {
			List<Integer> assetIds = flowGroupToAssets.stream().map(e -> e.getScAssetId()).collect(Collectors.toList());
			LOGGER.info("assetIds:{}",assetIds);
			List<ScAsset> scAssets = scAssetRepository.findByIdInAndStatus(assetIds,task.getMstTaskDef().getKey());
			if (scAssets != null && !scAssets.isEmpty()) {
				LOGGER.info("got sc assets");
				for (ScAsset scAsset : scAssets) {
					OdrAssetBean asset = mapOdrAsset(scAsset);
					assets.add(asset);
				}
			}
			}
			
			 
			List<com.tcl.dias.servicefulfillmentutils.beans.gsc.DocumentBean> documentBeans = new ArrayList<>();
			
			List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_Id(task.getServiceId());
			
			if(scAttachments != null && !scAttachments.isEmpty()) {
				scAttachments.stream()
				.map(ScAttachment::getAttachment).forEach(attachment -> {
					com.tcl.dias.servicefulfillmentutils.beans.gsc.DocumentBean document = new com.tcl.dias.servicefulfillmentutils.beans.gsc.DocumentBean();
					document.setAttachmentId(attachment.getId());
					document.setCategory(attachment.getCategory());
					document.setName(attachment.getName());
					document.setUrl(attachment.getUriPathOrUrl());
					documentBeans.add(document);
				});
			}
			
			details.setDocumentBeans(documentBeans);
			
//			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
//					.getComponentAttributes(task.getServiceId(), AttributeConstants.COMPONENT_GSCLICENSE, "A");
			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAndAdditionalAttributes(task.getServiceId(),
					   AttributeConstants.COMPONENT_GSCLICENSE, "A");
			
			if(scComponentAttributesmap.containsKey("sipServiceId")) {
				Optional<ScServiceDetail> optionalSipServiceDetail = scServiceDetailRepository
						.findById(Integer.parseInt(scComponentAttributesmap.get("sipServiceId")));
				if(optionalSipServiceDetail.isPresent()) {
					List<String> sipAttributes = new ArrayList<String>();
					sipAttributes.add("cmsId");
//					sipAttributes.add("secsId");
					Map<String, String> sipComponentsMap = commonFulfillmentUtils.getComponentAttributes(optionalSipServiceDetail.get().getId(),
							AttributeConstants.COMPONENT_LM, "A", sipAttributes);
					details.setCmsId(sipComponentsMap.get("cmsId"));
//					details.setSecsId(sipComponentsMap.get("secsId"));
				}
			}
			Optional<ScServiceDetail> serviceDetail = scServiceDetailRepository
					.findById(task.getServiceId());
			if(serviceDetail.isPresent()) {
				ScServiceDetail scServiceDetail = serviceDetail.get();
				details.setSecsId(scServiceDetail.getCustOrgNo());
			}
			String supplier = scComponentAttributesmap.getOrDefault("selectedSuppliers", null);
			if (supplier!=null) {
				LOGGER.info("got suuplier info");
				supplierBean = Utils.fromJson(supplier, new TypeReference<List<DIDSupplierBean>>() {});
			}
		}
		
		
		details.setAssets(assets);
		
		if (supplierBean != null && !supplierBean.isEmpty()) {
			for (OdrAssetBean asset : assets) {
				asset.setSupplierId(supplierBean.get(0).getSupplierOrgNo());
				asset.setSupplierName(supplierBean.get(0).getSupplierName());

				ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
						.findByScAsset_IdAndAttributeName(asset.getId(), "supplierActivationDate");
				if (scAssetAttribute != null) {
					asset.setSupplierActivationDate(scAssetAttribute.getAttributeValue());
				}
			}
		}
		
		details.setSupplier(supplierBean);
		LOGGER.info("getDIDNumberDetails end");
		return details;
	}
	
	public OdrAssetBean mapOdrAsset(ScAsset scAsset) {
		OdrAssetBean odrAssetBean = new OdrAssetBean();
		odrAssetBean.setId(scAsset.getId());
		odrAssetBean.setFqdn(scAsset.getFqdn());
		odrAssetBean.setIsActive(scAsset.getIsActive());
		odrAssetBean.setIsSharedInd(scAsset.getIsSharedInd());
		odrAssetBean.setName(scAsset.getName());
		odrAssetBean.setOriginnetwork(scAsset.getOriginnetwork());
		odrAssetBean.setOdrServiceDetailId(scAsset.getOdrServiceDetail().getId());
		odrAssetBean.setPublicIp(scAsset.getPublicIp());
		odrAssetBean.setCreatedBy(scAsset.getCreatedBy());
		odrAssetBean.setCreatedDate(DateUtil.convertTimestampToDate(scAsset.getCreatedDate()));
		odrAssetBean.setUpdatedBy(scAsset.getUpdatedBy());
		odrAssetBean.setUpdatedDate(DateUtil.convertTimestampToDate(scAsset.getUpdatedDate()));
		odrAssetBean.setType(scAsset.getType());
		ScAssetRelation scAssetRelation = scAssetRelationRepository.findByScAssetIdAndRelationType(scAsset.getId(), "Outpulse");
		if(scAssetRelation != null) {
			Optional<ScAsset> optionalOutpulse = scAssetRepository.findById(scAssetRelation.getScRelatedAssetId());
			if(optionalOutpulse.isPresent()) {
				ScAsset scAsset2 = optionalOutpulse.get();
				odrAssetBean.setCustomerOutpulse(scAsset2.getName());
			}
		}
		if(scAsset.getScAssetAttributes() != null)
			for(ScAssetAttribute scAssetAttribute : scAsset.getScAssetAttributes()) {
				odrAssetBean.addOdrAssetAttributeBean(mapOdrAssetAttribute(scAssetAttribute));
			}
		if(scAsset.getScAssetRelations() != null)
			for(ScAssetRelation odrAssetRelation : scAsset.getScAssetRelations()) {
				odrAssetBean.addOdrAssetRelationBean(mapOdrAssetRelation(odrAssetRelation));
			}
		return odrAssetBean;
	}
	
	public OdrAssetAttributeBean mapOdrAssetAttribute(ScAssetAttribute scAssetAttribute) {
		OdrAssetAttributeBean odrAssetAttributeBean = new OdrAssetAttributeBean();
		odrAssetAttributeBean.setId(scAssetAttribute.getId());
		odrAssetAttributeBean.setAttributeAltValueLabel(scAssetAttribute.getAttributeAltValueLabel());
		odrAssetAttributeBean.setAttributeName(scAssetAttribute.getAttributeName());
		odrAssetAttributeBean.setAttributeValue(scAssetAttribute.getAttributeValue());
		odrAssetAttributeBean.setCategory(scAssetAttribute.getCategory());
		odrAssetAttributeBean.setCreatedBy(scAssetAttribute.getCreatedBy());
		odrAssetAttributeBean.setIsActive(scAssetAttribute.getIsActive());
		odrAssetAttributeBean.setUpdatedBy(scAssetAttribute.getUpdatedBy());
		odrAssetAttributeBean.setUpdatedDate(scAssetAttribute.getUpdatedDate());
		return odrAssetAttributeBean;
	}
	
	public OdrAssetRelationBean mapOdrAssetRelation(ScAssetRelation scAssetRelation) {
		OdrAssetRelationBean odrAssetRelationBean = new OdrAssetRelationBean();
		odrAssetRelationBean.setId(scAssetRelation.getId());
		odrAssetRelationBean.setOdrAssetId(scAssetRelation.getScAssetId());
		odrAssetRelationBean.setOdrRelatedAssetId(scAssetRelation.getScRelatedAssetId());
		odrAssetRelationBean.setBusinessRelationName(scAssetRelation.getBusinessRelationName());
		odrAssetRelationBean.setEndDate(scAssetRelation.getEndDate());
		odrAssetRelationBean.setIsActive(scAssetRelation.getIsActive());
		odrAssetRelationBean.setRelationPort(scAssetRelation.getRelationPort());
		odrAssetRelationBean.setRelationResiliency(scAssetRelation.getRelationResiliency());
		odrAssetRelationBean.setRelationType(scAssetRelation.getRelationType());
		odrAssetRelationBean.setRemarks(scAssetRelation.getRemarks());
		odrAssetRelationBean.setStartDate(scAssetRelation.getStartDate());
		odrAssetRelationBean.setCreatedBy(scAssetRelation.getCreatedBy());
		odrAssetRelationBean.setCreatedDate(scAssetRelation.getCreatedDate());
		odrAssetRelationBean.setUpdatedBy(scAssetRelation.getUpdatedBy());
		odrAssetRelationBean.setUpdatedDate(scAssetRelation.getUpdatedDate());
		return odrAssetRelationBean;
	}

	@Transactional(readOnly = false)
	public DidPortingNumberBean closeDidPortingNumberTask(DidPortingNumberBean didPortingNumberBean) throws TclCommonException {
		LOGGER.info("Inside close DID Porting Number Task");
		Task task = getTaskByIdAndWfTaskId(didPortingNumberBean.getTaskId(), didPortingNumberBean.getWfTaskId());
		
		validateInputs(task, didPortingNumberBean);
		if (Objects.nonNull(didPortingNumberBean)) {
			Map<String, String> atMap = new HashMap<>();
			if (didPortingNumberBean.getCityAbbr() != null) {
				atMap.put("cityAbbr", didPortingNumberBean.getCityAbbr());
				componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
						AttributeConstants.COMPONENT_GSCLICENSE, "A");
			}
		}
		
		List<GscFlowGroupToAsset> flowGroupToAssets = gscFlowGroupToAssetRepository.findByGscFlowGroupId(task.getGscFlowGroupId());
		if(flowGroupToAssets!=null && !flowGroupToAssets.isEmpty()) {
			List<Integer> assetIds = flowGroupToAssets.stream().map(flowGroupToAsset -> flowGroupToAsset.getScAssetId()).collect(Collectors.toList());
			List<ScAsset> scAssets=scAssetRepository.findByIdInAndStatus(assetIds, task.getMstTaskDef().getKey());
			if (scAssets !=null && !scAssets.isEmpty()) {
				scAssets.stream().forEach(scAsset -> {
					if ("did-porting-number-info-rs-config".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
						scAsset.setStatus("did-porting-number-order-creation-repc");
					} else if ("did-number-info-rs-config".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
						scAsset.setStatus("did-new-number-order-creation-repc");
					}
                 scAssetRepository.save(scAsset);
				});
			}
		}
		validateInputs(task, didPortingNumberBean);
		processTaskLogDetails(task, "CLOSED", didPortingNumberBean.getDelayReason(), null, didPortingNumberBean);
		LOGGER.info("Exit close DID Porting Number Task");
		return (DidPortingNumberBean) flowableBaseService.taskDataEntry(task, didPortingNumberBean);
	}

	@Transactional(readOnly = false)
	public DidNumberRepcJeopardyBean closeDidNumberRepcJeopardyTask(DidNumberRepcJeopardyBean didNumberRepcJeopardyBean) throws TclCommonException {
		LOGGER.info("Inside close DID Number Repc Jeopardy Task");
		Task task = getTaskByIdAndWfTaskId(didNumberRepcJeopardyBean.getTaskId(), didNumberRepcJeopardyBean.getWfTaskId());
		
		Map<String, Object> flowMap=new HashMap<>();
		flowMap.put("action", didNumberRepcJeopardyBean.getAction());
		
			List<GscFlowGroupToAsset> flowGroupToAssets = gscFlowGroupToAssetRepository
					.findByGscFlowGroupId(task.getGscFlowGroupId());
			if(!flowGroupToAssets.isEmpty()) {
			List<Integer> assetIds = flowGroupToAssets.stream().map(e -> e.getScAssetId()).collect(Collectors.toList());
			List<ScAsset> scAssets=scAssetRepository.findByIdInAndStatus(assetIds, task.getMstTaskDef().getKey());
				if (!scAssets.isEmpty()) {
					scAssets.forEach(sc -> {

						if (task.getMstTaskDef().getKey()
								.equalsIgnoreCase("did-porting-number-order-creation-repc-jeopardy")) {
							if(didNumberRepcJeopardyBean.getAction()!=null && didNumberRepcJeopardyBean.getAction().equalsIgnoreCase("retry")) {
								sc.setStatus("did-porting-number-order-creation-repc");
							}else {
								sc.setStatus("did-porting-number-testing");
							}
						} else if (task.getMstTaskDef().getKey()
								.equalsIgnoreCase("did-new-number-order-creation-repc-jeopardy")) {
							if(didNumberRepcJeopardyBean.getAction()!=null && didNumberRepcJeopardyBean.getAction().equalsIgnoreCase("retry")) {
								sc.setStatus("did-new-number-order-creation-repc");
							}else {
								sc.setStatus("did-new-number-order-creation-testing");
							}

						}

					});
				}
			}
		validateInputs(task, didNumberRepcJeopardyBean);
		processTaskLogDetails(task, "CLOSED", didNumberRepcJeopardyBean.getDelayReason(), null, didNumberRepcJeopardyBean);
		LOGGER.info("Exit close DID Number Repc Jeopardy Task");
		return (DidNumberRepcJeopardyBean) flowableBaseService.taskDataEntry(task, didNumberRepcJeopardyBean,flowMap);
	}

	/**
	 * @param serviceId
	 * @param taskId
	 * @param wfTaskId
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public HttpServletResponse returnDidNumberExcelSheet(Integer serviceId, Integer taskId, String wfTaskId, HttpServletResponse response) throws IOException {
		LOGGER.info("Inside returnDidNumberExcelSheet method :{}", taskId);
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		sheet.setColumnWidth(50000, 50000);
		int rowCount = 0;
		createDidDetailsExcelHeader(sheet.createRow(rowCount));
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		if (task != null) {
			LOGGER.info("flow group id:{}", task.getGscFlowGroupId());
			List<GscFlowGroupToAsset> flowGroupToAssets = gscFlowGroupToAssetRepository.findByGscFlowGroupId(task.getGscFlowGroupId());
			if (!flowGroupToAssets.isEmpty()) {
				List<Integer> assetIds = flowGroupToAssets.stream().map(e -> e.getScAssetId()).collect(Collectors.toList());
				LOGGER.info("assetIds:{}", assetIds);
				List<ScAsset> scAssets = scAssetRepository.findByIdInAndStatus(assetIds, task.getMstTaskDef().getKey());
				if (scAssets != null && !scAssets.isEmpty()) {
					LOGGER.info("got sc assets");
					for (ScAsset scAsset : scAssets) {
						rowCount++;
						Row row = sheet.createRow(rowCount);
						Map<String,String> map=processDidDetails(scAsset,task.getScServiceDetail());
						fillDidDetailsInExcel(row,map);
					}
				}
			}
		}
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "Export DID Details -" + serviceId + ".xls";
		response.reset();
		response.setContentType("application/ms-excel");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {

		}
		outByteStream.flush();
		outByteStream.close();
		return response;
	}

	/**
	 * @param row
	 */
	private void createDidDetailsExcelHeader(Row row) {
		row.createCell(0).setCellValue("action");
		row.createCell(1).setCellValue("serviceName");
		row.createCell(2).setCellValue("orderNo");
		row.createCell(3).setCellValue("orderName");
		row.createCell(4).setCellValue("orgId");
		row.createCell(5).setCellValue("cid");
		row.createCell(6).setCellValue("sid");
		row.createCell(7).setCellValue("cmsId");
		row.createCell(8).setCellValue("serviceId");
		row.createCell(9).setCellValue("didNumber");
		row.createCell(10).setCellValue("tollFreeNumber");
		row.createCell(11).setCellValue("pcc");
		row.createCell(12).setCellValue("callType");
		row.createCell(13).setCellValue("inMainDigits");
		row.createCell(14).setCellValue("destinationCountryCode");
		row.createCell(15).setCellValue("destinationNationalId");
		row.createCell(16).setCellValue("routeTries");
		row.createCell(17).setCellValue("serviceTag");
		row.createCell(18).setCellValue("supplierTrunkGroup");
		row.createCell(19).setCellValue("routeType");
		row.createCell(20).setCellValue("routeSequence");
		row.createCell(21).setCellValue("routePrioritization");
		row.createCell(22).setCellValue("routeEndpoint1");
		row.createCell(23).setCellValue("routeEndpoint2");
		row.createCell(24).setCellValue("outpulseType");
		row.createCell(25).setCellValue("outpulse");
		row.createCell(26).setCellValue("outpulsePrefix");
		row.createCell(27).setCellValue("clid");
		row.createCell(28).setCellValue("customClid");
		row.createCell(29).setCellValue("serviceStatus");
		row.createCell(30).setCellValue("connectionDtgHeader");

	}

	private void fillDidDetailsInExcel(Row row,Map<String,String> cellValue) {
		row.createCell(0).setCellValue(cellValue.get("action"));
		row.createCell(1).setCellValue(cellValue.get("serviceName"));
		row.createCell(2).setCellValue(cellValue.get("orderNo"));
		row.createCell(3).setCellValue(cellValue.get("orderName"));
		row.createCell(4).setCellValue(cellValue.get("orgId"));
		row.createCell(5).setCellValue(cellValue.get("cid"));
		row.createCell(6).setCellValue(cellValue.get("sid"));
		row.createCell(7).setCellValue(cellValue.get("cmsId"));
		row.createCell(8).setCellValue(cellValue.get("serviceId"));
		row.createCell(9).setCellValue(cellValue.get("didNumber"));
		row.createCell(10).setCellValue(cellValue.get("tollFreeNumber"));
		row.createCell(11).setCellValue(cellValue.get("pcc"));
		row.createCell(12).setCellValue(cellValue.get("callType"));
		row.createCell(13).setCellValue(cellValue.get("inMainDigits"));
		row.createCell(14).setCellValue(cellValue.get("destinationCountryCode"));
		row.createCell(15).setCellValue(cellValue.get("destinationNationalId"));
		row.createCell(16).setCellValue(cellValue.get("routeTries"));
		row.createCell(17).setCellValue(cellValue.get("serviceTag"));
		row.createCell(18).setCellValue(cellValue.get("supplierTrunkGroup"));
		row.createCell(19).setCellValue(cellValue.get("routeType"));
		row.createCell(20).setCellValue(cellValue.get("routeSequence"));
		row.createCell(21).setCellValue(cellValue.get("routePrioritization"));
		row.createCell(22).setCellValue(cellValue.get("routeEndpoint1"));
		row.createCell(23).setCellValue(cellValue.get("routeEndpoint2"));
		row.createCell(24).setCellValue(cellValue.get("outpulseType"));
		row.createCell(25).setCellValue(cellValue.get("outpulse"));
		row.createCell(26).setCellValue(cellValue.get("outpulsePrefix"));
		row.createCell(27).setCellValue(cellValue.get("clid"));
		row.createCell(28).setCellValue(cellValue.get("customClid"));
		row.createCell(29).setCellValue(cellValue.get("serviceStatus"));
		row.createCell(30).setCellValue(cellValue.get("connectionDtgHeader"));

	}

	private Map<String,String> processDidDetails(ScAsset scAsset,ScServiceDetail scServiceDetail){
		Map<String,String> didDetailsMap=new HashMap<>();
		List<ScComponentAttribute> scComponentAttributes = scComponentAttrRepository
				.findByScServiceDetailIdAndScComponent_siteType(scServiceDetail.getId(), "A");		didDetailsMap.put("action","Create");
		didDetailsMap.put("action","Create");
		didDetailsMap.put("serviceName","EDV");
		didDetailsMap.put("orderNo",scServiceDetail.getUuid());
		didDetailsMap.put("orderName","NA");
		didDetailsMap.put("orgId","NA");
		didDetailsMap.put("cid",scServiceDetail.getUuid());
		didDetailsMap.put("sid","NA");
		didDetailsMap.put("cmsId",mapAttributeValue(scComponentAttributes,"cmsId"));
		didDetailsMap.put("serviceId","NA");
		didDetailsMap.put("didNumber",scAsset.getName());
		didDetailsMap.put("tollFreeNumber","NA");
		didDetailsMap.put("pcc","NA");
		didDetailsMap.put("callType","NA");
		didDetailsMap.put("inMainDigits","NA");
		didDetailsMap.put("destinationCountryCode","");
		didDetailsMap.put("destinationNationalId",scAsset.getName());
		didDetailsMap.put("routeTries","NA");
		didDetailsMap.put("serviceTag","NA");
		didDetailsMap.put("supplierTrunkGroup","Create");
		didDetailsMap.put("routeType","5");
		didDetailsMap.put("routeSequence","1");
		didDetailsMap.put("routePrioritization","NA");
		didDetailsMap.put("routeEndpoint1","");
		didDetailsMap.put("routeEndpoint2","NA");
		didDetailsMap.put("outpulseType","3");
		ScAssetRelation scAssetRelation = scAssetRelationRepository.findByScAssetIdAndRelationType(scAsset.getId(), "Outpulse");
		if(scAssetRelation != null) {
			Optional<ScAsset> optionalOutpulse = scAssetRepository.findById(scAssetRelation.getScRelatedAssetId());
			if(optionalOutpulse.isPresent()) {
				didDetailsMap.put("outpulse", optionalOutpulse.get().getName());
			}
		}
		didDetailsMap.put("outpulsePrefix","NA");
		didDetailsMap.put("clid","NA");
		didDetailsMap.put("customClid","NA");
		didDetailsMap.put("serviceStatus","NA");
		didDetailsMap.put("connectionDtgHeader","NA");
		return didDetailsMap;
	}
	private String mapAttributeValue(List<ScComponentAttribute> scComponentAttributes,String attributeName){
		if(scComponentAttributes.stream().filter(scComponentAttribute -> scComponentAttribute.getAttributeName().equalsIgnoreCase(attributeName)).findFirst().isPresent()){
			return scComponentAttributes.stream().filter(scComponentAttribute -> scComponentAttribute.getAttributeName().equalsIgnoreCase(attributeName)).findFirst().get().getAttributeValue();
		}else{
			return "NA";
		}
	}
	
	@Transactional(readOnly = false)
	public RepcUpdateJeopardyBean closeRepcUpdateJeopardy(RepcUpdateJeopardyBean repcUpdateJeopardyBean) throws TclCommonException {
		LOGGER.info("Inside closeRepcUpdateJeopardy");
		Task task = getTaskByIdAndWfTaskId(repcUpdateJeopardyBean.getTaskId(), repcUpdateJeopardyBean.getWfTaskId());
		validateInputs(task, repcUpdateJeopardyBean);
		List<GscFlowGroupToAsset> flowGroupToAssets = gscFlowGroupToAssetRepository
				.findByGscFlowGroupId(task.getGscFlowGroupId());
		if(!flowGroupToAssets.isEmpty()) {
		List<Integer> assetIds = flowGroupToAssets.stream().map(e -> e.getScAssetId()).collect(Collectors.toList());
		List<ScAsset> scAssets=scAssetRepository.findByIdInAndStatus(assetIds, task.getMstTaskDef().getKey());
			if (!scAssets.isEmpty()) {
				scAssets.forEach(sc -> {

					if (task.getMstTaskDef().getKey()
							.equalsIgnoreCase("did-new-number-patch-jeopardy")) {
							sc.setStatus("did-new-number-patch");
					} else if (task.getMstTaskDef().getKey()
							.equalsIgnoreCase("did-porting-number-patch-jeopardy")) {
							sc.setStatus("did-porting-number-patch");

					}
					scAssetRepository.save(sc);

				});
			}
		}
		processTaskLogDetails(task, "CLOSED", repcUpdateJeopardyBean.getDelayReason(), null, repcUpdateJeopardyBean);
		LOGGER.info("Exit closeRepcUpdateJeopardy");
		return (RepcUpdateJeopardyBean) flowableBaseService.taskDataEntry(task, repcUpdateJeopardyBean);
	}
	
	public List<ChangeConfigOutpulseDetailsBean> getChangeConfigDetails(Integer taskId, String wfTaskId) {
		LOGGER.info("Inside getChangeConfigDetails Method");
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		List<GscScAsset> torScAssets = scAssetRepository.findByServiceIdandTypeandAssetId(task.getServiceId(), "Toll-Free", task.getGscFlowGroupId(), GscNumberStatus.IN_TEST_NUMBER);
		List<ChangeConfigOutpulseDetailsBean> outpulseDetails = new ArrayList<>();
		torScAssets.forEach(toRscAsset -> {
			ChangeConfigOutpulseDetailsBean outpulseDetailsBean = new ChangeConfigOutpulseDetailsBean();
			if(toRscAsset.getTollfreeId() != null) {
				outpulseDetailsBean.setTollFreeNumberId(toRscAsset.getTollfreeId());
				outpulseDetailsBean.setTollFreeNumber(toRscAsset.getTollfreeName());
			}
			if(toRscAsset.getRoutingId() != null) {
				outpulseDetailsBean.setRoutingNumberId(toRscAsset.getRoutingId());
				outpulseDetailsBean.setRoutingNumber(toRscAsset.getRoutingName());
			}
			if(toRscAsset.getOutpulseId() != null) {
				outpulseDetailsBean.setOutpulseId(toRscAsset.getOutpulseId());
				outpulseDetailsBean.setOutpulse(toRscAsset.getOutpulseName());
			}
			outpulseDetails.add(outpulseDetailsBean);
		});

		return outpulseDetails;
	}
	
	@Transactional(readOnly = false)
	public TaskDetailsBaseBean closeChangeConfig(TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		LOGGER.info("Inside closeChangeConfig");
		Task task = getTaskByIdAndWfTaskId(taskDetailsBaseBean.getTaskId(), taskDetailsBaseBean.getWfTaskId());
		validateInputs(task, taskDetailsBaseBean);
		processTaskLogDetails(task, "CLOSED", taskDetailsBaseBean.getDelayReason(), null, taskDetailsBaseBean);
		LOGGER.info("Exit closeChangeConfig");
		return (TaskDetailsBaseBean) flowableBaseService.taskDataEntry(task, taskDetailsBaseBean);
	}
	
	@Transactional(readOnly = false)
	public String getServiceAcceptancePdf(Integer taskId, String wfTaskId, Integer serviceId,
			HttpServletResponse response) throws TclCommonException {
		LOGGER.info("Inside getServiceAcceptancePdf");
		String tempDownloadUrl=null;
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		if (task != null) {
			tempDownloadUrl = gscPdfService.processdIdHandoverPdfDownload(task.getServiceCode(), serviceId, task.getMstTaskDef().getKey(),
					response);
		}
		LOGGER.info("Exit getServiceAcceptancePdf");
		return tempDownloadUrl;
	}
	
	@Transactional(readOnly = false)
	public GscSipTrunkConfigApprovalBean approveGscSipTrunkConfigTask(GscSipTrunkConfigApprovalBean gscSipConfigApprovalBean) throws TclCommonException {
		LOGGER.info("Inside approveGscSipTrunkConfigTask Method");

		if (Objects.nonNull(gscSipConfigApprovalBean)) {
			if (Objects.nonNull(gscSipConfigApprovalBean.getStatus())) {
				for(Integer id:gscSipConfigApprovalBean.getId()) {
					Map<String, String> atMap = new HashMap<>();
					atMap.put("sipTrunkConfigStatus", gscSipConfigApprovalBean.getStatus());
					if("VALID".equalsIgnoreCase(gscSipConfigApprovalBean.getStatus())) {
						persistSipTrunkInfo(id, gscSipConfigApprovalBean.getTrunks());
					}
					componentAndAttributeService.updateAttributes(id, atMap,AttributeConstants.COMPONENT_GSCLICENSE, "A");
				}
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}

		LOGGER.info("Exit from approveGscSipTrunkConfigTask Method");
		return gscSipConfigApprovalBean;
	}

	private void persistSipTrunkInfo(Integer serviceId, List<TrunkBean> trunks) throws TclCommonException {
		if(!CollectionUtils.isEmpty(trunks)) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("trunks", Utils.convertObjectToJson(trunks));
//			componentAndAttributeService.updateAttributes(serviceId, atMap,AttributeConstants.COMPONENT_GSCLICENSE, "A");
			componentAndAttributeService.updateComponentAndAdditionalAttributes(serviceId, atMap,AttributeConstants.COMPONENT_GSCLICENSE, "A");
		}
	}
	
	@Transactional(readOnly = false)
	public ClosingTaskBean closeSipTrunkConfigTask(ClosingTaskBean closingTaskBean) throws TclCommonException {
		LOGGER.info("Inside closeSipTrunkConfigTask Method");
		Task task = getTaskByIdAndWfTaskId(closingTaskBean.getTaskId(), closingTaskBean.getWfTaskId());
		validateInputs(task, closingTaskBean);
		Map<String, Object> fMap = new HashMap<>();
		processTaskLogDetails(task, "CLOSED", closingTaskBean.getDelayReason(), null, closingTaskBean);
		LOGGER.info("Exit from closeSipTrunkConfigTask Method");
		return (ClosingTaskBean) flowableBaseService.taskDataEntry(task, closingTaskBean, fMap);
	}
	
	@Transactional(readOnly = false)
	public GscSipTrunkRouteLabelCreationApprovalBean approveGscSipTrunkRouteLabelCreation(GscSipTrunkRouteLabelCreationApprovalBean gscSipRouteLabelCreationApprovalBean) throws TclCommonException {
		LOGGER.info("Inside approveGscSipTrunkRouteLabelCreation Method");

		if (Objects.nonNull(gscSipRouteLabelCreationApprovalBean)) {
			if (Objects.nonNull(gscSipRouteLabelCreationApprovalBean.getStatus())) {
				for(Integer id:gscSipRouteLabelCreationApprovalBean.getId()) {
					Map<String, String> atMap = new HashMap<>();
					atMap.put("sipTrunkRouteLabelCreationStatus", gscSipRouteLabelCreationApprovalBean.getStatus());
					atMap.put("routeLabelCreationComments", gscSipRouteLabelCreationApprovalBean.getComments());
					List<TrunkBean> trunks = gscSipRouteLabelCreationApprovalBean.getTrunks();
					atMap.put("routeLabel", trunks.get(0).getRouteLabel());
					atMap.put("interconnectId", trunks.get(0).getInterconnectId());
					if("VALID".equalsIgnoreCase(gscSipRouteLabelCreationApprovalBean.getStatus())) {
						persistSipTrunkInfo(id, trunks);
					}
				
					componentAndAttributeService.updateAttributes(id, atMap, AttributeConstants.COMPONENT_GSCLICENSE, "A");
					
					List<AttachmentIdBean> attachments = gscSipRouteLabelCreationApprovalBean.getDocumentIds();
					if (!CollectionUtils.isEmpty(attachments)){
						attachments.stream().forEach(attachment -> {
							Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(id);
							checkAndMakeEntryInScAttachment(scServiceDetail.get(),
									attachment.getAttachmentId(),
									attachment.getCategory());
						});
					}
				}
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}

		LOGGER.info("Exit from approveGscSipTrunkRouteLabelCreation Method");
		return gscSipRouteLabelCreationApprovalBean;
	}

	@Transactional(readOnly = false)
	public ClosingTaskBean closeSipRouteLabelCreationTask(ClosingTaskBean closingTaskBean) throws TclCommonException {
		LOGGER.info("Inside closeSipRouteLabelCreationTask Method");
		Task task = getTaskByIdAndWfTaskId(closingTaskBean.getTaskId(), closingTaskBean.getWfTaskId());
		validateInputs(task, closingTaskBean);
		Map<String, Object> fMap = new HashMap<>();
		processTaskLogDetails(task, "CLOSED", closingTaskBean.getDelayReason(), null, closingTaskBean);
		closeWaitingTasks(task.getId());
		LOGGER.info("Exit from closeSipRouteLabelCreationTask Method");
		return (ClosingTaskBean) flowableBaseService.taskDataEntry(task, closingTaskBean, fMap);
	}


	private void closeWaitingTasks(Integer serviceId) {
		LOGGER.info("Inside closeWaitingTask Method");
		Optional<ScServiceDetail> opScServiceDetail = scServiceDetailRepository.findById(serviceId);
		if(opScServiceDetail.isPresent()) {
			ScServiceDetail scServiceDetail = opScServiceDetail.get();
			List<String> waitingTasksList = Arrays.asList("did-new-number-wait-for-sip","vas-wait-for-sip");
			List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeNot(scServiceDetail.getParentId(), waitingTasksList, "2");
			if(!CollectionUtils.isEmpty(tasks)) {
				LOGGER.info("Triggerting waiting tasks..!");
				tasks.stream().forEach(task -> {
					String activityKey = task.getMstTaskDef().getMstActivityDef().getKey();
					Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId()).activityId(activityKey).singleResult();
					runtimeService.setVariable(execution.getId(), "waitForSip", "no");
                    runtimeService.trigger(execution.getId());
				});
			}
		}
		LOGGER.info("Exit from closeWaitingTask Method");
	}
	
	
	@Transactional(readOnly = false)
	public RepcDBCreationBean closeRepcDBCircuitCreationTask(RepcDBCreationBean repcDBCreationBean) throws TclCommonException {
		LOGGER.info("Inside repcDBCircuitCreation Method");
		Task task = getTaskByIdAndWfTaskId(repcDBCreationBean.getTaskId(), repcDBCreationBean.getWfTaskId());
		validateInputs(task, repcDBCreationBean);
		if (Objects.nonNull(repcDBCreationBean)) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("repcDbCreationData", Utils.convertObjectToJson(repcDBCreationBean.getCircuitBean()));
//			componentAndAttributeService.updateAttributes(repcDBCreationBean.getServiceId(), atMap,
//					AttributeConstants.COMPONENT_GSCLICENSE, "A");
			componentAndAttributeService.updateComponentAndAdditionalAttributes(repcDBCreationBean.getServiceId(), atMap, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		
			if(Objects.nonNull(repcDBCreationBean.getCircuitBean())) {
				try {
					String status = repcService.requestForCreateCircuitGroup(Utils.convertObjectToJson(repcDBCreationBean.getCircuitBean()), task.getServiceId(),
			                   task.getServiceCode(), task.getWfProcessInstId());
					if(status.equalsIgnoreCase("success")) {
						repcDBCreationBean.setStatus(status);
						processTaskLogDetails(task, "CLOSED", repcDBCreationBean.getDelayReason(), null, repcDBCreationBean);
						flowableBaseService.taskDataEntry(task, repcDBCreationBean);
						return repcDBCreationBean;
					} else {
						repcDBCreationBean.setStatus(status);
						throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}
				} catch (Exception e) {
					repcDBCreationBean.setStatus(e.getMessage());
					throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_ERROR);
				}
			}
		} else {
			throw new TclCommonException(ResponseResource.RES_FAILURE, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Exit from repcDBCircuitCreation Method");
		return repcDBCreationBean;
	}

	public RepcDBCreationBean getRepcDbCreationInfo(Integer serviceId) {
		RepcDBCreationBean repcDbCreationBean=constructRepcDBCreationBean(serviceId);
		return  repcDbCreationBean;
	}
	
	private RepcDBCreationBean constructRepcDBCreationBean(Integer serviceId) {
		RepcDBCreationBean repcDbCreationBean = new RepcDBCreationBean();
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		repcDbCreationBean.setServiceId(serviceId);
		repcDbCreationBean.setSourceCountry(scServiceDetail.getSourceCountry());
		repcDbCreationBean.setDestCountry(scServiceDetail.getDestinationCountry());
//		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAndAdditionalAttributes(serviceId, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		String repcDbCreationData = scComponentAttributesmap.getOrDefault("repcDbCreationData", null);
		CircuitCreationBean circuitCreationBean = new CircuitCreationBean();
		if(Objects.nonNull(repcDbCreationData)){
			circuitCreationBean =Utils.fromJson(repcDbCreationData, new TypeReference<CircuitCreationBean>() {
			});
		}
		String trunksStr = scComponentAttributesmap.getOrDefault("trunks", null);
		if(!StringUtils.isEmpty(trunksStr)) {
			List<TrunkBean> trunksList = Utils.fromJson(trunksStr, new TypeReference<List<TrunkBean>>() {});
			repcDbCreationBean.setNoOfTrunks(trunksList.size());
		}else {
			repcDbCreationBean.setNoOfTrunks(1);
		}
		repcDbCreationBean.setCircuitBean(circuitCreationBean);
		return repcDbCreationBean;
	}
	
    /** Get SIP Handover Note **/
    @Transactional(readOnly = false)
    public String getSipServiceAcceptancePdf(Integer taskId, String wfTaskId, Integer serviceId,
            HttpServletResponse response) throws TclCommonException {
        LOGGER.info("Inside getSipServiceAcceptancePdf");
        String tempDownloadUrl=null;
        Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
        if (task != null) {
            tempDownloadUrl = gscPdfService.processSipHandoverPdfDownload(task.getServiceCode(), serviceId, task.getMstTaskDef().getKey(),
                    response);
        }
        LOGGER.info("Exit getSipServiceAcceptancePdf");
        return tempDownloadUrl;
    }

	@Transactional(readOnly = false)
	public String getVASServiceAcceptancePdf(Integer taskId, String wfTaskId, Integer serviceId, HttpServletResponse response) {
		LOGGER.info("Inside getVASServiceAcceptancePdf");
		String tempDownloadUrl=null;
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		if (task != null) {
			Integer gscFlowGrpId = task.getGscFlowGroupId();
			tempDownloadUrl = gscPdfService.processVasHandoverPdfDownload(task.getServiceCode(), serviceId, gscFlowGrpId, response);
		}
		LOGGER.info("Exit getVASServiceAcceptancePdf");
		return tempDownloadUrl;
	}

	/*** Fetching Sip Trunks Details for Sip Service Acceptance Task ***/
	public List<SipTrunkDetails> getSipTrunkDetails(Integer serviceId) throws TclCommonException {
		LOGGER.info("Inside  Get Sip Trunks Details Method");
//      Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,AttributeConstants.COMPONENT_GSCLICENSE, "A");
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAndAdditionalAttributes(serviceId, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		List<SipTrunkDetails> sipTrunkList = gscPdfService.constructSiptrunkDetails(scComponentAttributesmap);
		LOGGER.info("Exit  Get Sip Trunks Details Method");
		return sipTrunkList;
	}
	
	/**
	 * Raise jeopardy task.
	 *
	 * @param taskId
	 * @param raiseJeopardy
	 * @return RaiseJeopardy
	 */
	@Transactional(readOnly = false)
	public RaiseJeopardy raiseJeopardy(RaiseJeopardy raiseJeopardy) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(raiseJeopardy.getTaskId(),raiseJeopardy.getWfTaskId());

		validateInputs(task, raiseJeopardy);
		processTaskLogDetails(task, "CLOSED", raiseJeopardy.getDelayReason(), null, raiseJeopardy);
		return (RaiseJeopardy) flowableBaseService.taskDataEntry(task, raiseJeopardy);
	}

	public Boolean validateIndiaLe(BillingProfileCmsIdBean billingProfileCmsIdBean) {
		Boolean flag = false;
		Task task = getTaskByIdAndWfTaskId(billingProfileCmsIdBean.getTaskId(), billingProfileCmsIdBean.getWfTaskId());
		if (billingProfileCmsIdBean.getAction().equalsIgnoreCase("RAISE-TAX")) {
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
			LOGGER.info("Validating India LE for service id:{}", scServiceDetail.get().getId());
			ScOrderAttribute scOrder = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(
					scServiceDetail.get().getScOrder().getId(), GscConstants.SUPPLIER_NOTICE_ADDRESS);
			LOGGER.info("Validating India LE for order id:{}", scServiceDetail.get().getScOrder().getId());
			String supplierNoticeAddr = scOrder.getAttributeValue();
			if (!supplierNoticeAddr.isEmpty() && supplierNoticeAddr != null) {
				supplierNoticeAddr = supplierNoticeAddr.toLowerCase();
				if (supplierNoticeAddr.contains("india") || supplierNoticeAddr.contains("ind")) {
					LOGGER.info("supplier notice address :{}", supplierNoticeAddr);
					flag = true;
				}
			}
		}
		return flag;
	}
	
	@Transactional(readOnly = false)
	public TaskDetailsBaseBean closeRemoveNumbers(RemoveNumberTaskBean removeNumberTaskBean) throws TclCommonException {
		LOGGER.info("Inside closeRemoveNumbers");
		Task task = getTaskByIdAndWfTaskId(removeNumberTaskBean.getTaskId(), removeNumberTaskBean.getWfTaskId());
		validateInputs(task, removeNumberTaskBean);
		
		if(removeNumberTaskBean.getRemarks() != null) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("removeNumberRemarks", removeNumberTaskBean.getRemarks());
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_GSCLICENSE, "A");
		}
		
		LOGGER.info("Exit closeRemoveNumbers");
		processTaskLogDetails(task, "CLOSED", removeNumberTaskBean.getDelayReason(), null, removeNumberTaskBean);
		return (TaskDetailsBaseBean) flowableBaseService.taskDataEntry(task, removeNumberTaskBean);
	}

	
	public List<NumbersBean> getTollFreeNumbersForRemoveNumTask(Integer serviceId) {
		List<NumbersBean> numbersToRemove = new LinkedList<>();
		List<ScAsset> scAssets = scAssetRepository.findByScServiceDetail_idAndType(serviceId, "Toll-Free");
		if(!CollectionUtils.isEmpty(scAssets)) {
			scAssets.forEach(scAsset -> {
				NumbersBean numberBean = new NumbersBean();
				numberBean.setTollfreeId(scAsset.getId());
				numberBean.setTollfree(scAsset.getName());
				numbersToRemove.add(numberBean);
			});
		}
		return numbersToRemove;
	}

	public List<DocumentBean> getCDRLogForServiceAcceptanceTask(Integer serviceId) {
		List<DocumentBean> documentBeans = new ArrayList<>();
		if(serviceId!=null) {
			List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_IdAndAttachment_categoryIn(serviceId, Arrays.asList("GSC_CDR","CDRLOG"));
			
			if(!CollectionUtils.isEmpty(scAttachments)) {
				scAttachments.stream()
				.map(ScAttachment::getAttachment).forEach(attachment -> {
					DocumentBean document = new DocumentBean();
					document.setAttachmentId(attachment.getId());
					document.setCategory(attachment.getCategory());
					document.setName(attachment.getName());
					document.setUrl(attachment.getUriPathOrUrl());
					documentBeans.add(document);
				});
			}
		}
		return documentBeans;
	}
}
