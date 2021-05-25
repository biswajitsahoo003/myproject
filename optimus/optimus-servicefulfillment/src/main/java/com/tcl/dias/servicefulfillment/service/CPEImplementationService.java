package com.tcl.dias.servicefulfillment.service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.ConfigureCpeBean;
import com.tcl.dias.servicefulfillment.beans.CustomerCPEConfigConfirmationBean;
import com.tcl.dias.servicefulfillment.beans.CustomerCPEInstallationConfirmationBean;
import com.tcl.dias.servicefulfillment.beans.DispatchCPEBean;
import com.tcl.dias.servicefulfillment.beans.GenerateCPEInvoiceBean;
import com.tcl.dias.servicefulfillment.beans.InstallCPEBean;
import com.tcl.dias.servicefulfillment.beans.InstallCpeSdwanBean;
import com.tcl.dias.servicefulfillment.beans.PoReleaseCpeInstallationBean;
import com.tcl.dias.servicefulfillment.beans.PoReleaseCpeOrderBean;
import com.tcl.dias.servicefulfillment.beans.PrCpeInstallationBean;
import com.tcl.dias.servicefulfillment.beans.PrCpeOrderBean;
import com.tcl.dias.servicefulfillment.beans.ProvideMinBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPEInstallationBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPEOrderBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPESupportBean;
import com.tcl.dias.servicefulfillment.beans.WbsTransferJeopardy;
import com.tcl.dias.servicefulfillment.beans.cpe.MapNamedCustomerBean;
import com.tcl.dias.servicefulfillment.beans.cpe.ProvideWbsglccDetailBean;
import com.tcl.dias.servicefulfillment.beans.cpe.TrackCpeDeliveryBean;
import com.tcl.dias.servicefulfillment.entity.entities.CpeCostDetails;
import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.CpeCostDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMaterialAvailabilityBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeInstallationBean;
import com.tcl.dias.servicefulfillmentutils.beans.GenerateMrnforCPETransferBean;
import com.tcl.dias.servicefulfillmentutils.beans.SerialNumberBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapHanaService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * CPEImplementationService is used to perform CPE related tasks.
 *
 * @author arjayapa
 */
@Service
@Transactional(readOnly = true)
public class CPEImplementationService extends ServiceFulfillmentBaseService {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CPEImplementationService.class);


	@Autowired
	private ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	private FlowableBaseService flowableBaseService;
	
	@Autowired
	SapService sapService;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	CpeCostDetailsRepository cpeCostDetailsRepository;

	@Autowired
	private SapHanaService sapHanaService;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;
	


	/**
	 * This method is used to create task data for a specific task with appropriate CPE details.
	 *
	 * @param taskId
	 * @param poForCPEOrder
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePoForCPEOrderBean providePoForCPEOrder(ProvidePoForCPEOrderBean poForCPEOrder)throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForCPEOrder.getTaskId(),poForCPEOrder.getWfTaskId());

		validateInputs(task, poForCPEOrder);

		Map<String, String> atMap = new HashMap<>();
		if(StringUtils.isNotBlank(poForCPEOrder.getCpeSupplyHardwarePoNumber())) {
			atMap.put("cpeSupplyHardwarePoNumber", poForCPEOrder.getCpeSupplyHardwarePoNumber());
			atMap.put("cpeSupplyHardwareVendorName", poForCPEOrder.getCpeSupplyHardwareVendorName());
			atMap.put("cpeSupplyHardwarePoDate", poForCPEOrder.getCpeSupplyHardwarePoDate());
		}
		if(StringUtils.isNotBlank(poForCPEOrder.getCpeInstallationHardwarePoNumber())) {
			atMap.put("cpeInstallationPoNumber", poForCPEOrder.getCpeInstallationHardwarePoNumber());
			atMap.put("cpeInstallationVendorName", poForCPEOrder.getCpeInstallationHardwarePoNumber());
			atMap.put("cpeInstallationPoDate", poForCPEOrder.getCpeInstallationHardwarePoDate());
		}

		if(StringUtils.isNotBlank(poForCPEOrder.getCpeSupportPoNumber())) {
			atMap.put("cpeSupportVendorName", poForCPEOrder.getCpeSupportVendorName());
			atMap.put("cpeSupportPoNumber", poForCPEOrder.getCpeSupportPoNumber());
			atMap.put("cpeSupportPoDate", poForCPEOrder.getCpeSupportPoDate());
		}

		if(Objects.nonNull(poForCPEOrder.getCpeLicencePoNumber()) && Objects.nonNull(poForCPEOrder.getCpeLicencePoDate())){
			atMap.put("cpeLicencePoNumber", poForCPEOrder.getCpeLicencePoNumber());
			atMap.put("cpeLicencePoDate", poForCPEOrder.getCpeLicencePoDate());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		if (poForCPEOrder.getDocumentIds() != null && !poForCPEOrder.getDocumentIds().isEmpty())
			poForCPEOrder.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		if (!CollectionUtils.isEmpty(poForCPEOrder.getCpeInstallationHardwarePoDocument()))
			poForCPEOrder.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", poForCPEOrder.getDelayReason(), null, poForCPEOrder);
		
		Map<String, Object> flowableMap = new HashMap<>();
		flowableMap.put("isCpeAvailableInInventory",false);
		return (ProvidePoForCPEOrderBean) flowableBaseService.taskDataEntry(task, poForCPEOrder,flowableMap);
	}

	/**
	 * This method is used to prepare PO for CPE installation.
	 *
	 * @param taskId
	 * @param cpeInstallationBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePoForCPEInstallationBean providePoForCPEInstallation(ProvidePoForCPEInstallationBean cpeInstallationBean) throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(cpeInstallationBean.getTaskId(),cpeInstallationBean.getWfTaskId());
		validateInputs(task, cpeInstallationBean);

		Map<String, String> atMap = new HashMap<>();
		if(StringUtils.isNotBlank(cpeInstallationBean.getCpeInstallationPoNumber())) {
			atMap.put("cpeInstallationPoNumber", cpeInstallationBean.getCpeInstallationPoNumber());
			atMap.put("cpeInstallationVendorName", cpeInstallationBean.getCpeInstallationVendorName());
			atMap.put("cpeInstallationPoDate", cpeInstallationBean.getCpeInstallationPoDate());
		}

		if(StringUtils.isNotBlank(cpeInstallationBean.getCpeSupportPoNumber())) {
			atMap.put("cpeSupportVendorName", cpeInstallationBean.getCpeSupportVendorName());
			atMap.put("cpeSupportPoNumber", cpeInstallationBean.getCpeSupportPoNumber());
			atMap.put("cpeSupportPoDate", cpeInstallationBean.getCpeSupportPoDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		if (cpeInstallationBean.getDocumentIds() != null && !cpeInstallationBean.getDocumentIds().isEmpty())
			cpeInstallationBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,"CLOSED",cpeInstallationBean.getDelayReason(),null, cpeInstallationBean);
		return (ProvidePoForCPEInstallationBean) flowableBaseService.taskDataEntry(task, cpeInstallationBean);
	}

	/**
	 * This method is used to prepare PO for CPE support.
	 *
	 * @param taskId
	 * @param cpeSupportBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePoForCPESupportBean providePoForCPESupport(ProvidePoForCPESupportBean cpeSupportBean)
			throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(cpeSupportBean.getTaskId(),cpeSupportBean.getWfTaskId());
		validateInputs(task, cpeSupportBean);

		Map<String, String> atMap = new HashMap<>();
		atMap.put("cpeSupportVendorName", cpeSupportBean.getCpeSupportVendorName());
		atMap.put("cpeSupportPoNumber", cpeSupportBean.getCpeSupportPoNumber());
		atMap.put("cpeSupportPoDate", cpeSupportBean.getCpeSupportPoDate());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		if (cpeSupportBean.getDocumentIds() != null && !cpeSupportBean.getDocumentIds().isEmpty())
			cpeSupportBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,"CLOSED",cpeSupportBean.getDelayReason(),null, cpeSupportBean);
		return (ProvidePoForCPESupportBean) flowableBaseService.taskDataEntry(task, cpeSupportBean);

	}

	/**
	 * This method is used to generate CPE Invoice.
	 *
	 * @param taskId
	 * @param cpeInvoiceGenerationBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public GenerateCPEInvoiceBean generateCPEInvoice(GenerateCPEInvoiceBean cpeInvoiceGenerationBean)
			throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(cpeInvoiceGenerationBean.getTaskId(),cpeInvoiceGenerationBean.getWfTaskId());
		validateInputs(task, cpeInvoiceGenerationBean);

		if (cpeInvoiceGenerationBean.getDocumentIds() != null && !cpeInvoiceGenerationBean.getDocumentIds().isEmpty())
			cpeInvoiceGenerationBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,"CLOSED",cpeInvoiceGenerationBean.getDelayReason(),null, cpeInvoiceGenerationBean);
		return (GenerateCPEInvoiceBean) flowableBaseService.taskDataEntry(task, cpeInvoiceGenerationBean);

	}

	/**
	 * This method is used to Generate MRN for CPE Transfer
	 *
	 * @param taskId
	 * @param generateMrnforCPETransferBean
	 * @return GenerateMrnforCPETransferBean
	 * @throws TclCommonException
	 * @author Yogesh generateMrnforCPETransfer
	 */
	@Transactional(readOnly = false)
	public GenerateMrnforCPETransferBean generateMrnforCPETransfer(
			GenerateMrnforCPETransferBean generateMrnforCPETransferBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(generateMrnforCPETransferBean.getTaskId(),generateMrnforCPETransferBean.getWfTaskId());
		validateInputs(task, generateMrnforCPETransferBean);

		if (generateMrnforCPETransferBean.getDocumentIds() != null
				&& !generateMrnforCPETransferBean.getDocumentIds().isEmpty())
			generateMrnforCPETransferBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,"CLOSED",generateMrnforCPETransferBean.getDelayReason(),null, generateMrnforCPETransferBean);
		return (GenerateMrnforCPETransferBean) flowableBaseService.taskDataEntry(task, generateMrnforCPETransferBean);
	}

	/**
	 * This method is used to Dispatch CPE
	 *
	 * @param taskId
	 * @param dispatchCPEBean
	 * @return DispatchCPEBean
	 * @throws TclCommonException
	 * @author Yogesh dispatchCpe
	 */
	@Transactional(readOnly = false)
	public DispatchCPEBean dispatchCpe(DispatchCPEBean dispatchCPEBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(dispatchCPEBean.getTaskId(),dispatchCPEBean.getWfTaskId());
		validateInputs(task, dispatchCPEBean);
		Map<String, String> atMap = new HashMap<>();		
		atMap.put("cpeMrnNumber", dispatchCPEBean.getCpeMrnNumber());
		atMap.put("cpeMinNumber", dispatchCPEBean.getCpeMinNumber());
		atMap.put("courierTrackNumber", dispatchCPEBean.getCourierDispatchNumber());
		atMap.put("cpeDispatchDate", dispatchCPEBean.getCpeDispatchDate());
		atMap.put("courierDispatchVendorName", dispatchCPEBean.getCourierDispatchVendorName());
		atMap.put("vehicleDocketTrackNumber", dispatchCPEBean.getVehicleDocketTrackNumber());
		atMap.put("distributionCenterName", dispatchCPEBean.getDistributionCenterName());
		atMap.put("distributionCenterAddress", dispatchCPEBean.getDistributionCenterAddress());
		atMap.put("cpeSerialNumber", dispatchCPEBean.getCpeSerialNumber());
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-dispatch-track-cpe-international")
				&& dispatchCPEBean.getTrackCpeDelivery()!=null && dispatchCPEBean.getTrackCpeDelivery().getCpeDeliveredDate()!=null 
				&& !dispatchCPEBean.getTrackCpeDelivery().getCpeDeliveredDate().isEmpty()){
				LOGGER.info("CPE Delivery Date exists for Sdwan International Service id::{}",task.getServiceId());
				atMap.put("cpeDeliveryDate", dispatchCPEBean.getTrackCpeDelivery().getCpeDeliveredDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,"CLOSED",dispatchCPEBean.getDelayReason(),null, dispatchCPEBean);
		return (DispatchCPEBean) flowableBaseService.taskDataEntry(task, dispatchCPEBean);
	}


	@Transactional(readOnly = false)
	public ProvideMinBean provideMin(ProvideMinBean provideMinBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(provideMinBean.getTaskId(),provideMinBean.getWfTaskId());
		validateInputs(task, provideMinBean);
		Map<String, String> atMap = new HashMap<>();
		Map<String, Object> fMap = new HashMap<>();
		atMap.put("cpeMinNumber", provideMinBean.getCpeMinNumber());
		atMap.put("courierTrackNumber", provideMinBean.getCourierDispatchNumber());
		atMap.put("cpeDispatchDate", provideMinBean.getCpeDispatchDate());
		atMap.put("courierDispatchVendorName", provideMinBean.getCourierDispatchVendorName());
		atMap.put("vehicleDocketTrackNumber", provideMinBean.getVehicleDocketTrackNumber());
		fMap.put("minStatus",true);
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,"CLOSED",provideMinBean.getDelayReason(),null, provideMinBean);
		return (ProvideMinBean) flowableBaseService.taskDataEntry(task, provideMinBean,fMap);
	}

	/**
	 * This method is used to Install CPE
	 *
	 * @param taskId
	 * @param installCPEBean
	 * @return InstallCPEBean
	 * @throws TclCommonException
	 * @author Yogesh installCpe
	 */
	@Transactional(readOnly = false)
	public InstallCPEBean installCpe(InstallCPEBean installCPEBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(installCPEBean.getTaskId(),installCPEBean.getWfTaskId());
		
		validateInputs(task, installCPEBean);

		
		if (installCPEBean.getDocumentIds() != null && !installCPEBean.getDocumentIds().isEmpty())
			installCPEBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		
		Map<String, String> atMap = new HashMap<>();
		atMap.put("cpeSerialNumber", installCPEBean.getCpeSerialNumber());
		atMap.put("cpeAmcStartDate", installCPEBean.getCpeAmcStartDate());
		atMap.put("cpeAmcEndDate", installCPEBean.getCpeAmcEndDate());
		atMap.put("cpeConsoleCableConnected", installCPEBean.getCpeConsoleCableConnected());
		atMap.put("cpeLanInterface", installCPEBean.getCpeLanInterface());
		atMap.put("cpeWanInterface", installCPEBean.getCpeWanInterface());
		atMap.put("cpeCardSerialNumber", installCPEBean.getCpeCardSerialNumber());
		atMap.put("dateOfCpeInstallation", installCPEBean.getDateOfCpeInstallation());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,"CLOSED",installCPEBean.getDelayReason(),null, installCPEBean);
		return (InstallCPEBean) flowableBaseService.taskDataEntry(task, installCPEBean);
	}

	/**
	 * This method is used for Customer CPE Configuration Confirmation
	 *
	 * @param taskId
	 * @param customerCPEConfigConfirmationBean
	 * @return CustomerCPEConfigConfirmationBean
	 * @throws TclCommonException
	 * @author Yogesh customCpeConfigConfirmation
	 * @param map 
	 */
	@Transactional(readOnly = false)
	public CustomerCPEConfigConfirmationBean customCpeConfigConfirmation(
			CustomerCPEConfigConfirmationBean customerCPEConfigConfirmationBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(customerCPEConfigConfirmationBean.getTaskId(),customerCPEConfigConfirmationBean.getWfTaskId());
		validateInputs(task, customerCPEConfigConfirmationBean);
		processTaskLogDetails(task,"CLOSED",customerCPEConfigConfirmationBean.getDelayReason(),null, customerCPEConfigConfirmationBean);
		Map<String, Object> map = new HashMap<>();
		if(customerCPEConfigConfirmationBean!=null && customerCPEConfigConfirmationBean.getCabelingConfimation().equals("Not Ready")) {
			map.put("cpeConfigurationCompleted", false);
		}else {
			map.put("cpeConfigurationCompleted", true);
		}
		return (CustomerCPEConfigConfirmationBean) flowableBaseService.taskDataEntry(task, customerCPEConfigConfirmationBean,map);
	}

	/**
	 * This method is used for Customer CPE Installation Confirmation
	 *
	 * @param taskId
	 * @param customerCPEInstallationConfirmationBean
	 * @return CustomerCPEInstallationConfirmationBean
	 * @throws TclCommonException
	 * @author Yogesh customCpeInstallationConfirmation
	 */
	@Transactional(readOnly = false)
	public CustomerCPEInstallationConfirmationBean customCpeInstallationConfirmation(
			CustomerCPEInstallationConfirmationBean customerCPEInstallationConfirmationBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(customerCPEInstallationConfirmationBean.getTaskId(),customerCPEInstallationConfirmationBean.getWfTaskId());
		validateInputs(task, customerCPEInstallationConfirmationBean);
		processTaskLogDetails(task,"CLOSED",customerCPEInstallationConfirmationBean.getDelayReason(),null, customerCPEInstallationConfirmationBean);
		return (CustomerCPEInstallationConfirmationBean) flowableBaseService.taskDataEntry(task, customerCPEInstallationConfirmationBean);
	}

	/**
	 * This method is used to Configure CPE
	 *
	 * @param taskId
	 * @param configureCpeBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ConfigureCpeBean installCpe(ConfigureCpeBean configureCpeBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(configureCpeBean.getTaskId(),configureCpeBean.getWfTaskId());
		validateInputs(task, configureCpeBean);

		if (configureCpeBean.getDocumentIds() != null && !configureCpeBean.getDocumentIds().isEmpty())
			configureCpeBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,"CLOSED",configureCpeBean.getDelayReason(),null, configureCpeBean);
		return (ConfigureCpeBean) flowableBaseService.taskDataEntry(task, configureCpeBean);
	}

	/**
	 *Map Named Customer.
	 *
	 * @param taskId
	 * @param mapNamedCustomerBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public MapNamedCustomerBean mapNamedCustomer(MapNamedCustomerBean mapNamedCustomerBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(mapNamedCustomerBean.getTaskId(),mapNamedCustomerBean.getWfTaskId());
		validateInputs(task, mapNamedCustomerBean);
		if(mapNamedCustomerBean.getIsNamedCustomer().equalsIgnoreCase("yes")){
			Map<String, String> atMap = new HashMap<>();
			atMap.put("ciscoCustomerName", mapNamedCustomerBean.getCiscoCustomerName());
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		}

		processTaskLogDetails(task,"CLOSED",mapNamedCustomerBean.getDelayReason(),null, mapNamedCustomerBean);
		return (MapNamedCustomerBean) flowableBaseService.taskDataEntry(task, mapNamedCustomerBean);
	}

	/**
	 *Confirm Material Availability.
	 *
	 * @param taskId
	 * @param confirmMaterialAvailabilityBean
	 * @return
	 */
	@Transactional(readOnly = false)
	public ConfirmMaterialAvailabilityBean confirmMaterialAvailability(
			ConfirmMaterialAvailabilityBean confirmMaterialAvailabilityBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(confirmMaterialAvailabilityBean.getTaskId(),confirmMaterialAvailabilityBean.getWfTaskId());
		validateInputs(task, confirmMaterialAvailabilityBean);
		Map<String, String> atMap = new HashMap<>();
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getGrnNumber()))
			atMap.put("grnNumber", confirmMaterialAvailabilityBean.getGrnNumber());
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getGrnCreationDate()))
		atMap.put("grnCreationDate", confirmMaterialAvailabilityBean.getGrnCreationDate());
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getMaterialReceived()))
		atMap.put("materialReceived", confirmMaterialAvailabilityBean.getMaterialReceived());
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getMaterialReceivedDate()))
		atMap.put("materialReceivedDate", confirmMaterialAvailabilityBean.getMaterialReceivedDate());
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getExpectedCpeETADate()))
			atMap.put("expectedCpeETADate", confirmMaterialAvailabilityBean.getExpectedCpeETADate());
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getCpeSerialNumber()))
			atMap.put("cpeSerialNumber", confirmMaterialAvailabilityBean.getCpeSerialNumber());

		/*try {
			if(Objects.nonNull(confirmMaterialAvailabilityBean.getSerialNumber())&&!confirmMaterialAvailabilityBean.getSerialNumber().isEmpty()) {
				String result = confirmMaterialAvailabilityBean.getSerialNumber().stream().map(x -> x.getSerialNumber())
						.collect(Collectors.joining(","));

				atMap.put("cpeSerialNumber", result);
			}
		} catch (Exception e) {
			LOGGER.error("confirmMaterialAvailability getting serial number error:{}", e);
		}*/
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "CONFIRMMATERIALAVAIABILITY",
				Utils.convertObjectToJson(confirmMaterialAvailabilityBean));
		if(confirmMaterialAvailabilityBean.getAction().equalsIgnoreCase("close")){
		processTaskLogDetails(task, "CLOSED", confirmMaterialAvailabilityBean.getDelayReason(), null, confirmMaterialAvailabilityBean);
		confirmMaterialAvailabilityBean=(ConfirmMaterialAvailabilityBean) flowableBaseService.taskDataEntry(task, confirmMaterialAvailabilityBean);
		}
		else if(confirmMaterialAvailabilityBean.getAction().equalsIgnoreCase("update"))
		{
			processTaskLogDetails(task, "REMARKS", confirmMaterialAvailabilityBean.getExpectedCpeETADate(), null, confirmMaterialAvailabilityBean);
			saveTaskData(Utils.convertObjectToJson(confirmMaterialAvailabilityBean), task);
		}
		return confirmMaterialAvailabilityBean;
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
	public ProvideWbsglccDetailBean provideWbsglccDetails(ProvideWbsglccDetailBean provideWbsglccDetailBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(provideWbsglccDetailBean.getTaskId(),provideWbsglccDetailBean.getWfTaskId());
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
		
		String cpeDeliveryRequired = provideWbsglccDetailBean.getCpeDeliveryRequired();
		atMap.put("cpeDeliveryRequired", provideWbsglccDetailBean.getCpeDeliveryRequired());
		Map<String, Object> flowableMap = new HashMap<>();
		
		if(cpeDeliveryRequired.equalsIgnoreCase("no")) {
			flowableMap.put("cpeSiScope","");
			
			atMap.put("cpeDeliveryRequiredRejectionReason", provideWbsglccDetailBean.getCpeDeliveryRequiredRejectionReason());
			flowableMap.put("isCPEArrangedByCustomer",true);
			
			
			Map<String, String> scAtMap = new HashMap<>();
			scAtMap.put("cpe_chassis_changed", "No");
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(),scAtMap);
		}
		
		if(task.getOrderCode().toLowerCase().contains("izosdwan")){
			LOGGER.info("Provide Wbs for Izosdwan Order::{}",task.getServiceId());
			if(provideWbsglccDetailBean.getCpeRequiredDate()!=null){
				LOGGER.info("Cpe Required Date::{}",provideWbsglccDetailBean.getCpeRequiredDate());
				atMap.put("cpeBillStartDate", provideWbsglccDetailBean.getCpeRequiredDate().concat(" 00:00"));
			}
			if(provideWbsglccDetailBean.getVendorPORaisedDate()!=null){
				LOGGER.info("Vendor PO RaisedDate::{}",provideWbsglccDetailBean.getVendorPORaisedDate());
				atMap.put("vendorPORaisedDate", provideWbsglccDetailBean.getVendorPORaisedDate());
				flowableMap.put("prPoTriggerDate",getPrPoTriggeredDate(provideWbsglccDetailBean.getVendorPORaisedDate().concat(" 09:00")));
			}
		}
		
		if(!task.getOrderCode().toLowerCase().contains("izosdwan")){
			LOGGER.info("Provide Wbs for Other Orders::{}",task.getServiceId());
			ScServiceDetail serviceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(task.getServiceCode(),"INPROGRESS");
			List<ScServiceAttribute> cpeChassisAttr = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndIsAdditionalParamAndAttributeValueAndCategory(
							serviceDetail.getId(), CommonConstants.Y, CommonConstants.Y, "CPE Basic Chassis", "CPE");
			String bomName="";
			for (ScServiceAttribute scServiceAttribute : cpeChassisAttr) {
				LOGGER.info("cpeChassisAttr exists for service Id::{}",task.getServiceId());
				String serviceParamId = scServiceAttribute.getAttributeValue();
				if (StringUtils.isNotBlank(serviceParamId)) {
					Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
							.findById(Integer.valueOf(serviceParamId));
					if (scAdditionalServiceParam.isPresent()) {
						String bomResponse = scAdditionalServiceParam.get().getValue();
						LOGGER.info("cpeChassisAttr additional param exists for service Id::{} with bom::{}",task.getServiceId(),bomResponse);
						bomName = getBomName(bomResponse);
					}
				}
			}
			String vendorCode="";
			List<MstCostCatalogue> mstCostCatalogues = mstCostCatalogueRepository.findByDistinctBundledBom(bomName);
			if(mstCostCatalogues!=null && !mstCostCatalogues.isEmpty()){
				LOGGER.info("MstCostCatalogue exists for service Id::{}",task.getServiceId());
				Optional<MstCostCatalogue> mstCostCatalogueOptional =mstCostCatalogues.stream().findFirst();
				if(mstCostCatalogueOptional.isPresent()){
					vendorCode=mstCostCatalogueOptional.get().getVendorCode();
					flowableMap.put("vendorCode",vendorCode);
				}
			}
		}
		LOGGER.info("Provide Wbs flowableMap::{} for service Id::{}",flowableMap,task.getServiceId());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,"CLOSED",provideWbsglccDetailBean.getDelayReason(),null, provideWbsglccDetailBean);
		return (ProvideWbsglccDetailBean) flowableBaseService.taskDataEntry(task, provideWbsglccDetailBean,flowableMap);
	}
	
	@SuppressWarnings("unchecked")
	private String getBomName(String bomResponse) {
		LOGGER.info("getBomName method invoked::{}",bomResponse);
		JSONParser jsonParser = new JSONParser();
		String bomName ="";
		try{
			JSONArray data = (JSONArray) jsonParser.parse(bomResponse);
			Iterator<JSONObject> iterator = data.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonObj = (JSONObject) iterator.next();
				bomName = (String) jsonObj.get("bomName");
				LOGGER.info("bomName::{}",bomName);
				return bomName;
			}
		}catch (Exception e) {
			LOGGER.error("CPEImplementationService.getBomName  Exception {} ", e);
		}
		return bomName;
	}

	private Object getPrPoTriggeredDate(String prpoTriggerDate) throws TclCommonException {
		try{
			LOGGER.info("effective date for prpoTriggerDate{}::",prpoTriggerDate);
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			LocalDateTime inputLocalTime=LocalDateTime.ofInstant(inputDateFormatter.parse(prpoTriggerDate).toInstant(), ZoneId.of("UTC"));
			LocalDateTime currentLocalTime=LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC")).plusMinutes(15);
			LOGGER.info("Current Local Time :{}",currentLocalTime);
			LOGGER.info("Input Local Time :{}",inputLocalTime);
			if(inputLocalTime.isAfter(currentLocalTime)) {
				prpoTriggerDate=inputLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			}else {
				prpoTriggerDate=currentLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			}
			LOGGER.info("Derived effective date for prpoTriggerDate::{}",prpoTriggerDate);
			return prpoTriggerDate;
		} catch (Exception ex) {
			LOGGER.error("Exception for getPrPoTriggeredDate:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Track CPE delivery.
	 *
	 * @param taskId
	 * @param trackCpeDeliveryBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public TrackCpeDeliveryBean trackCpeDelivery(TrackCpeDeliveryBean trackCpeDeliveryBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(trackCpeDeliveryBean.getTaskId(),trackCpeDeliveryBean.getWfTaskId());
		validateInputs(task, trackCpeDeliveryBean);
		if(trackCpeDeliveryBean.getCpeDeliveredDate()!=null && !trackCpeDeliveryBean.getCpeDeliveredDate().isEmpty()){
			LOGGER.info("CPE Delivery Date exists");
			Map<String, String> atMap = new HashMap<>();
			atMap.put("cpeDeliveryDate", trackCpeDeliveryBean.getCpeDeliveredDate());
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		}
		processTaskLogDetails(task,"CLOSED",trackCpeDeliveryBean.getDelayReason(),null, trackCpeDeliveryBean);
		return (TrackCpeDeliveryBean) flowableBaseService.taskDataEntry(task, trackCpeDeliveryBean);
	}

	@Transactional(readOnly = false)
	public PrCpeOrderBean pRCpeOrder(PrCpeOrderBean prCpeOrderBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(prCpeOrderBean.getTaskId(),prCpeOrderBean.getWfTaskId());
		validateInputs(task, prCpeOrderBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("cpeSupplyHardwarePrNumber", prCpeOrderBean.getCpeSupplyHardwarePrNumber());
		atMap.put("cpeSupplyHardwarePrVendorName", prCpeOrderBean.getCpeSupplyHardwarePrVendorName());
		atMap.put("cpeSupplyHardwarePrDate", prCpeOrderBean.getCpeSupplyHardwarePrDate());
		atMap.put("cpeLicencePrNumber", prCpeOrderBean.getCpeLicencePrNumber());
		atMap.put("cpeLicencePrDate", prCpeOrderBean.getCpeLicencePrDate());
		atMap.put("cpeLicenseVendorName",prCpeOrderBean.getCpeLicenseVendorName());
		atMap.put("cpeSupplyHardwarePrStatus", "Success");
		atMap.put("cpeLicencePoStatus", "Success");
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,"CLOSED", prCpeOrderBean.getDelayReason(),null, prCpeOrderBean);
		return (PrCpeOrderBean) flowableBaseService.taskDataEntry(task, prCpeOrderBean);
	}

	@Transactional(readOnly = false)
	public PoReleaseCpeOrderBean poReleaseForCpeOrder(PoReleaseCpeOrderBean poReleaseCpeOrderBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poReleaseCpeOrderBean.getTaskId(),poReleaseCpeOrderBean.getWfTaskId());
		validateInputs(task, poReleaseCpeOrderBean);
		Map<String, String> atMap = new HashMap<>();
		Map<String, Object> fMap = new HashMap<>();
		atMap.put("cpeSupplyHardwarePoReleased", StringUtils.trimToEmpty(poReleaseCpeOrderBean.getPoRelease()));
		atMap.put("cpeLicencePoReleased", StringUtils.trimToEmpty(poReleaseCpeOrderBean.getLicencePoRelease()));
		if(poReleaseCpeOrderBean.getPoRelease().equalsIgnoreCase("yes")) {
			fMap.put("cpeHardwarePoReleased", true);
			fMap.put("cpeLicencePoReleased", true);
			fMap.put("cpeLicencePoStatus", "PO Released");
			fMap.put("cpeHardwarePoStatus", "PO Released");
			atMap.put("cpeLicencePoStatus", "PO Released");
			atMap.put("cpeSupplyHardwarePoStatus", "PO Released");
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,"CLOSED",poReleaseCpeOrderBean.getDelayReason(),null, poReleaseCpeOrderBean);
		return (PoReleaseCpeOrderBean) flowableBaseService.taskDataEntry(task, poReleaseCpeOrderBean,fMap);
	}

	@Transactional(readOnly = false)
	public PrCpeInstallationBean prCpeInstallation(PrCpeInstallationBean prCpeInstallationBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(prCpeInstallationBean.getTaskId(),prCpeInstallationBean.getWfTaskId());
		validateInputs(task, prCpeInstallationBean);
		Map<String, String> atMap = new HashMap<>();
		Map<String, Object> fMap = new HashMap<>();
		atMap.put("cpeInstallationPrNumber", prCpeInstallationBean.getCpeInstallationPrNumber());
		atMap.put("cpeInstallationPrVendorName", prCpeInstallationBean.getCpeInstallationPrVendorName());
		atMap.put("cpeInstallationPrDate", prCpeInstallationBean.getCpeInstallationPrDate());
		atMap.put("cpeSupportPrNumber", prCpeInstallationBean.getCpeSupportPrNumber());
		atMap.put("cpeSupportPrVendorName", prCpeInstallationBean.getCpeSupportPrVendorName());
		atMap.put("cpeSupportPrDate", prCpeInstallationBean.getCpeSupportPrDate());
		atMap.put("cpeInstallationPrVendorEmailId", prCpeInstallationBean.getCpeInstallationPrVendorEmailId());
		atMap.put("cpeSupportPrVendorEmailId", prCpeInstallationBean.getCpeSupportPrVendorEmailId());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, "CLOSED", prCpeInstallationBean.getDelayReason(), null, prCpeInstallationBean);
		return (PrCpeInstallationBean) flowableBaseService.taskDataEntry(task, prCpeInstallationBean);
	}

	@Transactional(readOnly = false)
	public PoReleaseCpeInstallationBean poReleaseCpeInstallation(PoReleaseCpeInstallationBean poReleaseCpeInstallationBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poReleaseCpeInstallationBean.getTaskId(),poReleaseCpeInstallationBean.getWfTaskId());
		validateInputs(task, poReleaseCpeInstallationBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("poReleaseCpeInstall", poReleaseCpeInstallationBean.getPoReleaseCpeInstall());
		atMap.put("poReleaseCpeSupply", poReleaseCpeInstallationBean.getPoReleaseCpeSupply());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, "CLOSED", poReleaseCpeInstallationBean.getDelayReason(), null, poReleaseCpeInstallationBean);
		return (PoReleaseCpeInstallationBean) flowableBaseService.taskDataEntry(task, poReleaseCpeInstallationBean);
	}

	@Transactional(readOnly = false)
	public PrCpeOrderBean manualPrCpeOrder(PrCpeOrderBean prCpeOrderBean) throws TclCommonException {
		LOGGER.info("manualPrCpeOrder method invoked::{}",prCpeOrderBean.toString());
		Task task = getTaskByIdAndWfTaskId(prCpeOrderBean.getTaskId(),prCpeOrderBean.getWfTaskId());
		validateInputs(task, prCpeOrderBean);
		if(prCpeOrderBean.getIsRouterExists()){
			LOGGER.info("IsRouterExists::{}",task.getServiceId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("cpeSupplyHardwarePrNumber", prCpeOrderBean.getCpeSupplyHardwarePrNumber());
			atMap.put("cpeSupplyHardwarePrVendorName", prCpeOrderBean.getCpeSupplyHardwarePrVendorName());
			atMap.put("cpeSupplyHardwarePrDate", prCpeOrderBean.getCpeSupplyHardwarePrDate());
			atMap.put("cpeSupplyHardwarePrStatus", "Success");
			atMap.put("cpeSupplyHardwarePrType", "Manual");
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		}
		LOGGER.info("Task ServiceId::{},ServiceCode::{},VendorCode::{},ProcessInstId::{}",task.getServiceId(),task.getServiceCode(),task.getVendorCode(),task.getWfProcessInstId());
		if(prCpeOrderBean.getCpeSupplyHardwarePrNumber()!=null){
			LOGGER.info("Supply Hardware PR Number::{}");
			sapService.saveOrUpdatePrCreation(task.getServiceCode(),task.getServiceId(),
					task.getWfProcessInstId(),"HARDWARE","cpe-hardware-pr",prCpeOrderBean.getCpeSupplyHardwarePrNumber(),prCpeOrderBean.getCpeSupplyHardwarePrDate(),"Manual",
					task.getVendorCode(),prCpeOrderBean.getCpeSupplyHardwarePrVendorName(),prCpeOrderBean.getCpeComponentId());
		}
		if(prCpeOrderBean.getCpeLicencePrNumber()!=null){
			LOGGER.info("License PR Number::{}");
			sapService.saveOrUpdatePrCreation(task.getServiceCode(),task.getServiceId(),
					task.getWfProcessInstId(),"LICENCE","cpe-license-pr",prCpeOrderBean.getCpeLicencePrNumber(),prCpeOrderBean.getCpeLicencePrDate(),"Manual",
					task.getVendorCode(),prCpeOrderBean.getCpeLicenseVendorName(),prCpeOrderBean.getCpeComponentId());
		}
		
		processTaskLogDetails(task,"CLOSED", prCpeOrderBean.getDelayReason(),null, prCpeOrderBean);
		return (PrCpeOrderBean) flowableBaseService.taskDataEntry(task, prCpeOrderBean);
	}

	@Transactional(readOnly = false)
	public ProvidePoForCPEOrderBean manualPoForCPEOrder(ProvidePoForCPEOrderBean poForCPEOrder) throws TclCommonException {
		LOGGER.info("manualPoForCPEOrder method invoked::{}",poForCPEOrder.toString());
		Task task = getTaskByIdAndWfTaskId(poForCPEOrder.getTaskId(),poForCPEOrder.getWfTaskId());
		validateInputs(task, poForCPEOrder);
		if(poForCPEOrder.getIsRouterExists()){
			LOGGER.info("IsRouterExists::{}",task.getServiceId());
			Map<String, String> atMap = new HashMap<>();
			if(StringUtils.isNotBlank(poForCPEOrder.getCpeSupplyHardwarePoNumber())) {
				atMap.put("cpeSupplyHardwarePoNumber", poForCPEOrder.getCpeSupplyHardwarePoNumber());
				atMap.put("cpeSupplyHardwareVendorName", poForCPEOrder.getCpeSupplyHardwareVendorName());
				atMap.put("cpeSupplyHardwarePoDate", poForCPEOrder.getCpeSupplyHardwarePoDate());
			}
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		}
		LOGGER.info("Task ServiceId::{},SiteType::{}",task.getServiceId(),task.getSiteType());
		ScComponent scComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(), "LM", task.getSiteType());
		if(scComponent!=null){
			LOGGER.info("ScComponent exists::{},VendorCode::{}",scComponent.getId(),task.getVendorCode());
			List<CpeCostDetails> cpeCostDetailList=cpeCostDetailsRepository.findByServiceIdAndServiceCodeAndComponentIdAndVendorCode(task.getServiceId(),task.getServiceCode(),scComponent.getId(),task.getVendorCode());
			if(cpeCostDetailList!=null && !cpeCostDetailList.isEmpty()){
				LOGGER.info("cpeCostDetailList size::{}",cpeCostDetailList.size());
				for(CpeCostDetails cpeCostDetails:cpeCostDetailList){
					cpeCostDetails.setPoNumber(poForCPEOrder.getCpeSupplyHardwarePoNumber());
				}
				cpeCostDetailsRepository.saveAll(cpeCostDetailList);
			}
		}
		LOGGER.info("Task ServiceId::{},ServiceCode::{},VendorCode::{},ProcessInstId::{}",task.getServiceId(),task.getServiceCode(),task.getVendorCode(),task.getWfProcessInstId());
		if(poForCPEOrder.getCpeSupplyHardwarePoNumber()!=null){
			LOGGER.info("Supply Hardware PO Number::{}");
			sapService.updatePoCreation(task.getServiceCode(),task.getServiceId(),
					"HARDWARE",poForCPEOrder.getCpeSupplyHardwarePoNumber(),poForCPEOrder.getCpeSupplyHardwarePoDate(),
					task.getVendorCode(),poForCPEOrder.getCpeSupplyHardwareVendorName(),poForCPEOrder.getCpeComponentId());
		}
		if(poForCPEOrder.getCpeLicencePoNumber()!=null){
		LOGGER.info("Supply Licence PO Number::{}");
		sapService.updatePoCreation(task.getServiceCode(),task.getServiceId(),
					"LICENCE",poForCPEOrder.getCpeLicencePoNumber(),poForCPEOrder.getCpeLicencePoDate(),
					task.getVendorCode(),null,poForCPEOrder.getCpeComponentId());
		}
		if (poForCPEOrder.getDocumentIds() != null && !poForCPEOrder.getDocumentIds().isEmpty()){
			poForCPEOrder.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		if (!CollectionUtils.isEmpty(poForCPEOrder.getCpeInstallationHardwarePoDocument())){
			poForCPEOrder.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		processTaskLogDetails(task, "CLOSED", poForCPEOrder.getDelayReason(), null, poForCPEOrder);
		return (ProvidePoForCPEOrderBean) flowableBaseService.taskDataEntry(task, poForCPEOrder);
	}
	
	
	@Transactional(readOnly = false)
	public PoReleaseCpeOrderBean manualPoReleaseForCpeOrder(PoReleaseCpeOrderBean poReleaseCpeOrderBean) throws TclCommonException {
		LOGGER.info("manualPoReleaseForCpeOrder method invoked::{}",poReleaseCpeOrderBean);
		Task task = getTaskByIdAndWfTaskId(poReleaseCpeOrderBean.getTaskId(),poReleaseCpeOrderBean.getWfTaskId());
		validateInputs(task, poReleaseCpeOrderBean);
		if(poReleaseCpeOrderBean.getIsRouterExists()){
			Map<String, String> atMap = new HashMap<>();
			LOGGER.info("IsRouterExists::{}",task.getServiceId());
			atMap.put("cpeSupplyHardwarePoReleased", StringUtils.trimToEmpty(poReleaseCpeOrderBean.getPoRelease()));
			atMap.put("cpeSupplyHardwarePoStatus", "PO Released");
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		}
		Map<String, Object> fMap = new HashMap<>();
		if("yes".equalsIgnoreCase(poReleaseCpeOrderBean.getPoRelease())) {
			fMap.put("cpeHardwarePoReleased", true);
			fMap.put("cpeHardwarePoStatus", "PO Released");
		}
		if("yes".equalsIgnoreCase(poReleaseCpeOrderBean.getLicencePoRelease())) {
			fMap.put("cpeLicencePoReleased", true);
			fMap.put("cpeLicencePoStatus", "PO Released");
		}
		LOGGER.info("Task ServiceId::{},ServiceCode::{},VendorCode::{},ProcessInstId::{}",task.getServiceId(),task.getServiceCode(),task.getVendorCode(),task.getWfProcessInstId());
		if(poReleaseCpeOrderBean.getPoRelease()!=null){
			LOGGER.info("PO Release");
			sapService.updatePoStatus(task.getServiceCode(),task.getServiceId(),
					"HARDWARE",poReleaseCpeOrderBean.getPoRelease(),
					task.getVendorCode(),poReleaseCpeOrderBean.getCpeComponentId());
		}
		if(poReleaseCpeOrderBean.getLicencePoRelease()!=null){
			LOGGER.info("Licence PO Release");
			sapService.updatePoStatus(task.getServiceCode(),task.getServiceId(),
					"LICENCE",poReleaseCpeOrderBean.getLicencePoRelease(),
					task.getVendorCode(),poReleaseCpeOrderBean.getCpeComponentId());
		}
		processTaskLogDetails(task,"CLOSED",poReleaseCpeOrderBean.getDelayReason(),null, poReleaseCpeOrderBean);
		return (PoReleaseCpeOrderBean) flowableBaseService.taskDataEntry(task, poReleaseCpeOrderBean,fMap);
	}
	
	/**
	 *Confirm Material Availability.
	 *
	 * @param taskId
	 * @param confirmMaterialAvailabilityBean
	 * @return
	 */
	@Transactional(readOnly = false)
	public ConfirmMaterialAvailabilityBean manualConfirmMaterialAvailability(
			ConfirmMaterialAvailabilityBean confirmMaterialAvailabilityBean) throws TclCommonException {
		LOGGER.info("manualConfirmMaterialAvailability method invoked::{}",confirmMaterialAvailabilityBean);
		Task task = getTaskByIdAndWfTaskId(confirmMaterialAvailabilityBean.getTaskId(),confirmMaterialAvailabilityBean.getWfTaskId());
		validateInputs(task, confirmMaterialAvailabilityBean);
		Map<String, String> atMap = new HashMap<>();
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getGrnNumber())){
			atMap.put("grnNumber", confirmMaterialAvailabilityBean.getGrnNumber());
		}
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getGrnCreationDate())){
			atMap.put("grnCreationDate", confirmMaterialAvailabilityBean.getGrnCreationDate());
		}
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getMaterialReceived())){
			atMap.put("materialReceived", confirmMaterialAvailabilityBean.getMaterialReceived());
		}
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getMaterialReceivedDate())){
			atMap.put("materialReceivedDate", confirmMaterialAvailabilityBean.getMaterialReceivedDate());
		}
		if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getExpectedCpeETADate())){
			atMap.put("expectedCpeETADate", confirmMaterialAvailabilityBean.getExpectedCpeETADate());
		}
		/*if(StringUtils.isNotBlank(confirmMaterialAvailabilityBean.getCpeSerialNumber())){
			atMap.put("cpeSerialNumber", confirmMaterialAvailabilityBean.getCpeSerialNumber());
		}*/
		LOGGER.info("Task ServiceId::{},SiteType::{}",task.getServiceId(),task.getSiteType());
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		ScComponent scComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(),"LM",task.getSiteType());
		if (scComponent != null) {
			LOGGER.info("scComponent exists to save confirm material availability::{}", scComponent.getId());
			/*componentAndAttributeService.updateComponentAdditionalAttributes(scServiceDetail, scComponent.getId(),
					"CONFIRMMATERIALAVAIABILITY", Utils.convertObjectToJson(confirmMaterialAvailabilityBean));*/
			String cpeRouterSerialNumber=null;
			if (confirmMaterialAvailabilityBean.getSerialNumber() != null
					&& !confirmMaterialAvailabilityBean.getSerialNumber().isEmpty()) {
				LOGGER.info("Confirm material availability size::{}", confirmMaterialAvailabilityBean.getSerialNumber().size());
				for (SerialNumberBean serialNumberBean : confirmMaterialAvailabilityBean.getSerialNumber()) {
					if (serialNumberBean.getMaterialCode() != null && !serialNumberBean.getMaterialCode().isEmpty()) {
						LOGGER.info("Material Code::{}", serialNumberBean.getMaterialCode());
						List<CpeCostDetails> cpeCostDetailList = cpeCostDetailsRepository
								.findByServiceIdAndServiceCodeAndComponentIdAndMaterialCodeAndVendorCode(task.getServiceId(),
										task.getServiceCode(), scComponent.getId(), serialNumberBean.getMaterialCode(),task.getVendorCode());
						if (cpeCostDetailList != null && !cpeCostDetailList.isEmpty()) {
							LOGGER.info("cpeCostDetailList size::{}", cpeCostDetailList.size());
							for (CpeCostDetails cpeCostDetails : cpeCostDetailList) {
								LOGGER.info("cpeCostDetail category::{}",cpeCostDetails.getCategory());
								cpeCostDetails.setSerialNumber(serialNumberBean.getSerialNumber());
								if("router".equalsIgnoreCase(cpeCostDetails.getCategory())){
									cpeRouterSerialNumber=serialNumberBean.getSerialNumber();
								}
							}
							cpeCostDetailsRepository.saveAll(cpeCostDetailList);
						}
					}
				}
			}
			if(cpeRouterSerialNumber!=null){
				LOGGER.info("Cpe SerialNumber exists for Task Id::{}", task.getVendorCode());
				atMap.put("cpeSerialNumber", cpeRouterSerialNumber);
				componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
			}
		}
		if(confirmMaterialAvailabilityBean.getAction().equalsIgnoreCase("close")){
		processTaskLogDetails(task, "CLOSED", confirmMaterialAvailabilityBean.getDelayReason(), null, confirmMaterialAvailabilityBean);
		confirmMaterialAvailabilityBean=(ConfirmMaterialAvailabilityBean) flowableBaseService.taskDataEntry(task, confirmMaterialAvailabilityBean);
		}
		else if(confirmMaterialAvailabilityBean.getAction().equalsIgnoreCase("update"))
		{
			processTaskLogDetails(task, "REMARKS", confirmMaterialAvailabilityBean.getExpectedCpeETADate(), null, confirmMaterialAvailabilityBean);
			saveTaskData(Utils.convertObjectToJson(confirmMaterialAvailabilityBean), task);
		}
		return confirmMaterialAvailabilityBean;
	}

	/**
	 * This method is used to Install CPE
	 *
	 * @param taskId
	 * @param installCPEBean
	 * @return InstallCPEBean
	 * @throws TclCommonException
	 * @author Yogesh installCpe
	 */
	@Transactional(readOnly = false)
	public InstallCpeSdwanBean installCpeSdwan(InstallCpeSdwanBean installCpeSdwanBean) throws TclCommonException {

		LOGGER.info("installCpeSdwan for serviceId:{}", installCpeSdwanBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(installCpeSdwanBean.getTaskId(),installCpeSdwanBean.getWfTaskId());
		validateInputs(task, installCpeSdwanBean);

		for(CpeInstallationBean cpeInstallationDetail : installCpeSdwanBean.getCpeInstallationDetails()){
			if (cpeInstallationDetail.getDocumentIds() != null && !cpeInstallationDetail.getDocumentIds().isEmpty())
				cpeInstallationDetail.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			LOGGER.info("installCpeSdwan for cpe component id: {}",cpeInstallationDetail.getComponentId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("cpeSerialNumber", cpeInstallationDetail.getCpeSerialNumber());
			atMap.put("cpeAmcStartDate", cpeInstallationDetail.getCpeAmcStartDate());
			atMap.put("cpeAmcEndDate", cpeInstallationDetail.getCpeAmcEndDate());
			atMap.put("cpeConsoleCableConnected", cpeInstallationDetail.getCpeConsoleCableConnected());
			atMap.put("cpeOsVersion", cpeInstallationDetail.getCpeOsVersion());
			atMap.put("cpeCardSerialNumber", cpeInstallationDetail.getCpeCardSerialNumber());
			atMap.put("dateOfCpeInstallation", cpeInstallationDetail.getDateOfCpeInstallation());
			componentAndAttributeService.updateAttributesByScComponent(task.getServiceId(), atMap,cpeInstallationDetail.getComponentId());
		}
		processTaskLogDetails(task,"CLOSED",installCpeSdwanBean.getDelayReason(),null, installCpeSdwanBean);
		return (InstallCpeSdwanBean) flowableBaseService.taskDataEntry(task, installCpeSdwanBean);
	}


	/**
	 * @author vivek
	 *
	 * @param wbsTransferJeopardy
	 * @return
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly = false)
	public WbsTransferJeopardy wbsJeopardy(WbsTransferJeopardy wbsTransferJeopardy) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(wbsTransferJeopardy.getTaskId(), wbsTransferJeopardy.getWfTaskId());

		Map<String, Object> fMap = new HashMap<>();

		if (wbsTransferJeopardy.getAction().equalsIgnoreCase("manualWbs")) {
			fMap.put("action", "manualWbs");

		} else {
			fMap.put("action", "CLOSED");

		}

		processTaskLogDetails(task, "CLOSED", wbsTransferJeopardy.getDelayReason(), wbsTransferJeopardy.getAction(),
				wbsTransferJeopardy);
		return (WbsTransferJeopardy) flowableBaseService.taskDataEntry(task, wbsTransferJeopardy, fMap);
	}
	
	/**
	 * Track SDWAN CPE delivery
	 *
	 * @param taskId
	 * @param trackCpeDeliveryBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public TrackCpeDeliveryBean sdwanTrackCpeDelivery(TrackCpeDeliveryBean trackCpeDeliveryBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(trackCpeDeliveryBean.getTaskId(), trackCpeDeliveryBean.getWfTaskId());
		LOGGER.info("sdwanTrackCpeDelivery method invoked for task Id::{}",task.getId());
		validateInputs(task, trackCpeDeliveryBean);
		if (trackCpeDeliveryBean.getCpeDeliveredDate() != null
				&& !trackCpeDeliveryBean.getCpeDeliveredDate().isEmpty()) {
			LOGGER.info("CPE Delivery Date exists");
			Map<String, String> atMap = new HashMap<>();
			atMap.put("cpeDeliveryDate", trackCpeDeliveryBean.getCpeDeliveredDate());
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
					task.getSiteType());
		}
		if (trackCpeDeliveryBean.getAction().equalsIgnoreCase("close")) {
			processTaskLogDetails(task, "CLOSED", trackCpeDeliveryBean.getDelayReason(), null, trackCpeDeliveryBean);
			trackCpeDeliveryBean = (TrackCpeDeliveryBean) flowableBaseService.taskDataEntry(task, trackCpeDeliveryBean);
		} else if (trackCpeDeliveryBean.getAction().equalsIgnoreCase("update")) {
			processTaskLogDetails(task, "REMARKS", trackCpeDeliveryBean.getStatus(), null, trackCpeDeliveryBean);
			saveTaskData(Utils.convertObjectToJson(trackCpeDeliveryBean), task);
		}
		return trackCpeDeliveryBean;
	}
	
	/**
	 * This method is used to SDWAN Dispatch CPE International
	 *
	 * @param taskId
	 * @param dispatchCPEBean
	 * @return DispatchCPEBean
	 * @throws TclCommonException
	 * @author Dimple S dispatchCpe
	 */
	@Transactional(readOnly = false)
	public DispatchCPEBean sdwanDispatchCpeInternational(DispatchCPEBean dispatchCPEBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(dispatchCPEBean.getTaskId(),dispatchCPEBean.getWfTaskId());
		LOGGER.info("sdwanDispatchCpeInternational method invoked for task Id::{}",task.getId());
		validateInputs(task, dispatchCPEBean);
		Map<String, String> atMap = new HashMap<>();		
		atMap.put("cpeMrnNumber", dispatchCPEBean.getCpeMrnNumber());
		atMap.put("cpeMinNumber", dispatchCPEBean.getCpeMinNumber());
		atMap.put("courierTrackNumber", dispatchCPEBean.getCourierDispatchNumber());
		atMap.put("cpeDispatchDate", dispatchCPEBean.getCpeDispatchDate());
		atMap.put("courierDispatchVendorName", dispatchCPEBean.getCourierDispatchVendorName());
		atMap.put("vehicleDocketTrackNumber", dispatchCPEBean.getVehicleDocketTrackNumber());
		atMap.put("distributionCenterName", dispatchCPEBean.getDistributionCenterName());
		atMap.put("distributionCenterAddress", dispatchCPEBean.getDistributionCenterAddress());
		atMap.put("cpeSerialNumber", dispatchCPEBean.getCpeSerialNumber());
		if(dispatchCPEBean.getTrackCpeDelivery()!=null && dispatchCPEBean.getTrackCpeDelivery().getCpeDeliveredDate()!=null 
				&& !dispatchCPEBean.getTrackCpeDelivery().getCpeDeliveredDate().isEmpty()){
				LOGGER.info("CPE Delivery Date exists for Sdwan International Service id::{}",task.getServiceId());
				atMap.put("cpeDeliveryDate", dispatchCPEBean.getTrackCpeDelivery().getCpeDeliveredDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if (dispatchCPEBean.getAction().equalsIgnoreCase("close")) {
			processTaskLogDetails(task, "CLOSED", dispatchCPEBean.getDelayReason(), null, dispatchCPEBean);
			dispatchCPEBean = (DispatchCPEBean) flowableBaseService.taskDataEntry(task, dispatchCPEBean);
		} else if (dispatchCPEBean.getAction().equalsIgnoreCase("update")) {
			processTaskLogDetails(task, "REMARKS", dispatchCPEBean.getTrackCpeDelivery().getStatus(), null, dispatchCPEBean);
			saveTaskData(Utils.convertObjectToJson(dispatchCPEBean), task);
		}
		return dispatchCPEBean;
	}

}
