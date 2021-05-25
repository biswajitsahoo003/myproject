package com.tcl.dias.servicefulfillment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.ServiceDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.ApproveMastCommercialBean;
import com.tcl.dias.servicefulfillment.beans.BhPo;
import com.tcl.dias.servicefulfillment.beans.ColoPO;
import com.tcl.dias.servicefulfillment.beans.ColoRequest;
import com.tcl.dias.servicefulfillment.beans.ConductRfSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.CreateMrnBean;
import com.tcl.dias.servicefulfillment.beans.DeliverRfEquipmentDetails;
import com.tcl.dias.servicefulfillment.beans.InstallRfBean;
import com.tcl.dias.servicefulfillment.beans.PoForMastProviderBean;
import com.tcl.dias.servicefulfillment.beans.RfData;
import com.tcl.dias.servicefulfillment.beans.TaskMastRequest;
import com.tcl.dias.servicefulfillment.beans.WoSiteSurveyBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskData;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.IsValidBtsSyncBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Transactional(readOnly = true)
@Service
public class RfLmService extends ServiceFulfillmentBaseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RfLmService.class);


	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	MQUtils mqUtils;

	@Value("${queue.validatebtssync}")
	String validateBtsSyncQueue;
	
	  @Autowired
	  FlowableBaseService flowableBaseService;

	/**
	 * This method is used to provide work order for site survey
	 *
	 * @param taskId
	 * @param woSiteSurveyBean
	 * @return WoSiteSurveyBean
	 * @throws TclCommonException
	 * @author diksha garg provideWoSiteSurvey
	 */
	@Transactional(readOnly = false)
	public WoSiteSurveyBean provideWoSiteSurvey( WoSiteSurveyBean woSiteSurveyBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(woSiteSurveyBean.getTaskId(), woSiteSurveyBean.getWfTaskId());
		validateInputs(task, woSiteSurveyBean);
		processTaskLogDetails(task,"CLOSED",woSiteSurveyBean.getDelayReason(),null, woSiteSurveyBean);
		return (WoSiteSurveyBean) flowableBaseService.taskDataEntry(task, woSiteSurveyBean);
	}

	/**
	 * This method is used to approve mast commercials
	 *
	 * @param taskId
	 * @param ApproveMastCommercialBean
	 * @return ApproveMastCommercialBean
	 * @throws TclCommonException
	 * @author Yogesh approveMastCommercials
	 */
	@Transactional(readOnly = false)
	public ApproveMastCommercialBean approveMastCommercials(
			ApproveMastCommercialBean approveMastCommercialBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(approveMastCommercialBean.getTaskId(),approveMastCommercialBean.getWfTaskId());
		validateInputs(task, approveMastCommercialBean);
		processTaskLogDetails(task,"CLOSED",approveMastCommercialBean.getDelayReason(),null, approveMastCommercialBean);
		return (ApproveMastCommercialBean) flowableBaseService.taskDataEntry(task, approveMastCommercialBean);
	}

	/**
	 * This method is used to po for mast provider
	 *
	 * @param taskId
	 * @param PoForMastProviderBean
	 * @return PoForMastProviderBean
	 * @throws TclCommonException
	 * @author Yogesh poForMastProvider
	 */
	@Transactional(readOnly = false)
	public PoForMastProviderBean poForMastProvider(PoForMastProviderBean poForMastProviderBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForMastProviderBean.getTaskId(), poForMastProviderBean.getWfTaskId());
		validateInputs(task, poForMastProviderBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("mastPoNumber", String.valueOf(poForMastProviderBean.getMastPoNumber()));
		atMap.put("prNumber", poForMastProviderBean.getMastPoDate());
		atMap.put("mastPoDate", poForMastProviderBean.getPrNumber());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if (poForMastProviderBean.getDocumentIds() != null && !poForMastProviderBean.getDocumentIds().isEmpty()) {
			poForMastProviderBean.getDocumentIds().forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		processTaskLogDetails(task,"CLOSED",poForMastProviderBean.getDelayReason(),null, poForMastProviderBean);
		return (PoForMastProviderBean) flowableBaseService.taskDataEntry(task, poForMastProviderBean);
	}

	/**
	 * Creates MRN for RF Equipment.
	 *
	 * @param taskId
	 * @param createMrnBean
	 * @return CreateMrnBean
	 */
	@Transactional(readOnly = false)
	public CreateMrnBean createMrnForRfEquipment(CreateMrnBean createMrnBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(createMrnBean.getTaskId(),createMrnBean.getWfTaskId());
		validateInputs(task, createMrnBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("rfMrnNo", String.valueOf(createMrnBean.getMrnNumber()));
		atMap.put("rfMrnDate", createMrnBean.getMrnDate());
		atMap.put("rfMrnRequestedBy", createMrnBean.getRequestedBy());
		atMap.put("rfMrnApprovedBy", createMrnBean.getApprovedBy());
		atMap.put("rfMrnVerifiedBy", createMrnBean.getVerifiedBy());
		atMap.put("rfDistributionCenterName", createMrnBean.getDistributionCenterName());
		atMap.put("rfDistributionCenterAddress", createMrnBean.getDistributionCenterAddress());
		atMap.put("mrnRfremarks", createMrnBean.getRemarks());

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		String errorMessage=createMrnBean.getRemarks();
		
		if (createMrnBean.getDocumentIds() != null && !createMrnBean.getDocumentIds().isEmpty()) {
			createMrnBean.getDocumentIds().forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		if (Objects.nonNull(task.getScServiceDetail())&& StringUtils.isNotBlank(errorMessage)) {
			componentAndAttributeService.updateAdditionalAttributes(task.getScServiceDetail(),
					task.getMstTaskDef().getKey()+"_error",
					componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
					AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());

		}
		processTaskLogDetails(task,"CLOSED",createMrnBean.getDelayReason(),null, createMrnBean);
		return (CreateMrnBean) flowableBaseService.taskDataEntry(task, createMrnBean);
	}

	/**
	 * Install RF Equipment.
	 *
	 * @param taskId
	 * @param installRfBean
	 * @return InstallRfBean
	 */
	@Transactional(readOnly = false)
	public InstallRfBean installRfEquipment(InstallRfBean installRfBean) throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(installRfBean.getTaskId(), installRfBean.getWfTaskId());
		Response response = null;
		String action = "CLOSED";
		Map<String, Object> wfMap = new HashMap<>();

		if (installRfBean.getAction() != null && installRfBean.getAction().equalsIgnoreCase("save")) {
			action = "SAVED";
		}

		Map<String, String> atMap = new HashMap<>();
		populateMap(atMap, installRfBean);
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (StringUtils.isNotBlank(installRfBean.getBtsIp()) && action.equalsIgnoreCase("CLOSED")) {
			
			IsValidBtsSyncBean validBtsSyncBean = new IsValidBtsSyncBean();
			validBtsSyncBean.setBtsIP(installRfBean.getBtsIp());
			validBtsSyncBean.setCopfId(task.getOrderCode());
			validBtsSyncBean.setRequestId(task.getWfProcessInstId());

			ScComponentAttribute rfMake = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							task.getServiceId(), "rfTechnology", "LM", "A");

			String rfTechnology = "";
			if (rfMake != null)
				rfTechnology = StringUtils.trimToEmpty((String) rfMake.getAttributeValue());

			validBtsSyncBean.setProvider(rfTechnology);
			validBtsSyncBean.setScenarioType("Wireless");
			validBtsSyncBean.setSectorId(installRfBean.getSectorId());
			validBtsSyncBean.setServiceId(StringUtils.trimToEmpty(task.getServiceCode()));
			validBtsSyncBean.setHsuIP(installRfBean.getHsuIp()!=null?installRfBean.getHsuIp().trim():installRfBean.getHsuIp());
			String req = Utils.convertObjectToJson(validBtsSyncBean);

			String isValidBtsSyncResponse = (String) mqUtils.sendAndReceive(validateBtsSyncQueue, req);

			response = Utils.convertJsonToObject(isValidBtsSyncResponse, Response.class);
			if (response != null && response.getStatus() != null && response.getStatus().equals(true)) {
				LOGGER.error("success with  bts value");
			} else {
				InstallRfBean reponse = new InstallRfBean();
				reponse.setStatus(false);
				reponse.setErrorMessage(response.getErrorMessage());
				return reponse;
			}
		}
		validateInputs(task, installRfBean);
		saveInstallRfBean(installRfBean.getTaskId(), installRfBean);
		if(CommonConstants.GVPN.equalsIgnoreCase(task.getScServiceDetail().getErfPrdCatalogProductName())
				&& task.getScServiceDetail()!=null && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getMultiVrfSolution())){
			//Copy master service detail to slave
			copyMasterVrfAttributesToSlave(task.getScServiceDetail().getId(),task.getSiteType(),installRfBean);
		}
		processTaskLogDetails(task, action, installRfBean.getDelayReason(), null, installRfBean);

		if (action.equalsIgnoreCase("CLOSED")) {
			wfMap.put("action", action);
			return (InstallRfBean) flowableBaseService.taskDataEntry(task, installRfBean,wfMap);

		} else {

			return (InstallRfBean) flowableBaseService.taskDataEntrySave(task, installRfBean);

		}

	}
	
	
	

	/**
	 * Save MAST details for particular TASK.
	 * 
	 * @param taskId
	 * @param mastDetailBean
	 * @return
	 */
	@Transactional(readOnly = false)
	public TaskMastRequest saveTaskMastDetails(TaskMastRequest mastDetailBean)
			throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(mastDetailBean.getTaskId(),mastDetailBean.getWfTaskId());
		validateInputs(task, mastDetailBean);
		processTaskLogDetails(task,"CLOSED",mastDetailBean.getDelayReason(),null, mastDetailBean);
		return (TaskMastRequest) flowableBaseService.taskDataEntry(task, mastDetailBean);
	}

	
	/**
	 * Deliver RF Equipment.
	 *
	 * @param taskId
	 * @param rfEquipDetails
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public DeliverRfEquipmentDetails deliverRfEquipment(DeliverRfEquipmentDetails rfEquipDetails)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(rfEquipDetails.getTaskId(), rfEquipDetails.getWfTaskId());
		validateInputs(task, rfEquipDetails);
		processTaskLogDetails(task,"CLOSED",rfEquipDetails.getDelayReason(),null, rfEquipDetails);
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("action", "close");
		return (DeliverRfEquipmentDetails) flowableBaseService.taskDataEntry(task, rfEquipDetails,wfMap);

	}

	/**
	 * This method is used to schedule Customer Appointment
	 *
	 * @param taskId
	 * @param customerAppointmentBean
	 * @return
	 * @throws TclCommonException
	 */

	@Transactional(readOnly = false)
	public Object saveConductSiteSurveyBean( ConductRfSiteSurveyBean conductSiteSurveyBean)
			throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
        ConductRfSiteSurveyBean response = null;
		try {
			Task task = getTaskByIdAndWfTaskId(conductSiteSurveyBean.getTaskId(), conductSiteSurveyBean.getWfTaskId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("airConditioned", conductSiteSurveyBean.getAirConditioned());
			atMap.put("powerAvailable", conductSiteSurveyBean.getPowerAvailable());
			atMap.put("powerSocketAvailable", conductSiteSurveyBean.getPowerSocketAvailable());
			atMap.put("hygienicRoomNearNetworkRoom", conductSiteSurveyBean.getHygienicRoomNearNetworkRoom());
			atMap.put("earthingAvailable", conductSiteSurveyBean.getEarthingAvailable());
			atMap.put("powerBackup", conductSiteSurveyBean.getPowerBackup());
			atMap.put("earthPitAvailable", conductSiteSurveyBean.getEarthPitAvailable());
			atMap.put("mastInstallationPermission", conductSiteSurveyBean.getMastInstallationPermission());
			atMap.put("terraceAccessAvailable", conductSiteSurveyBean.getTerraceAccessAvailable());
			atMap.put("poeRackWallFixPermission", conductSiteSurveyBean.getPoeRackWallFixPermission());
			atMap.put("cableRoutingPermission", conductSiteSurveyBean.getCableRoutingPermission());
			atMap.put("siteAddress", conductSiteSurveyBean.getSiteAddress());
			atMap.put("demarcationWing", conductSiteSurveyBean.getDemarcationWing());
			atMap.put("demarcationFloor", conductSiteSurveyBean.getDemarcationFloor());
			atMap.put("demarcationRoom", conductSiteSurveyBean.getDemarcationRoom());
			atMap.put("demarcationBuildingName", conductSiteSurveyBean.getDemarcationBuildingName());
			atMap.put("siteReadinessStatus", conductSiteSurveyBean.getSiteReadinessStatus());
			atMap.put("siteDeficiencyObservations", conductSiteSurveyBean.getSiteDeficiencyObservations());
			atMap.put("feasibilityStatus", conductSiteSurveyBean.getFeasibilityStatus());
			atMap.put("feasiblityFailureReason", conductSiteSurveyBean.getFeasiblityFailureReason());
			atMap.put("buildingHeight", conductSiteSurveyBean.getBuildingHeight());
			atMap.put("elevation", conductSiteSurveyBean.getElevation());
			atMap.put("typeOfTerrain", conductSiteSurveyBean.getTypeOfTerrain());
			atMap.put("towerHeight", conductSiteSurveyBean.getTowerHeight());
			atMap.put("distanceFromSite1", conductSiteSurveyBean.getDistanceFromSite1());
			atMap.put("distanceFromOtherEnd", conductSiteSurveyBean.getDistanceFromOtherEnd());
			atMap.put("structureType", conductSiteSurveyBean.getStructureType());
			atMap.put("poleHeight", conductSiteSurveyBean.getPoleHeight());
			atMap.put("typeOfPoleAntennaErection", conductSiteSurveyBean.getTypeOfPoleAntennaErection());
			atMap.put("mastProvidedBy", conductSiteSurveyBean.getMastProvidedBy());
			atMap.put("mastHeight", conductSiteSurveyBean.getMastHeight());
			atMap.put("mastReusable", conductSiteSurveyBean.getMastReusable());
			atMap.put("mastReusableRemarks", conductSiteSurveyBean.getMastReusableRemarks());
			atMap.put("typeOfMastAntennaErection", conductSiteSurveyBean.getTypeOfMastAntennaErection());
			atMap.put("mastHeightDeviation", conductSiteSurveyBean.getMastHeightDeviation());
			atMap.put("mastHeightDeviationReason", conductSiteSurveyBean.getMastHeightDeviation());
			atMap.put("polarization", conductSiteSurveyBean.getPolarization());
			atMap.put("antennaAzimuth", conductSiteSurveyBean.getAntennaAzimuth());
			atMap.put("poeToAntennaCableLength", conductSiteSurveyBean.getPoeToAntennaCableLength());
			atMap.put("obstructionType", conductSiteSurveyBean.getObstructionType());
			atMap.put("obstructionHeight", conductSiteSurveyBean.getObstructionHeight());
			atMap.put("phaseNeutral", conductSiteSurveyBean.getPhaseNeutral());
			atMap.put("earthNeutral", conductSiteSurveyBean.getEarthNeutral());
			atMap.put("phaseEarth", conductSiteSurveyBean.getPhaseEarth());
			atMap.put("surveyRemarks", conductSiteSurveyBean.getSurveyRemarks());
			atMap.put("rfInstallationPlannedDate", conductSiteSurveyBean.getRfInstallationPlannedDate());
			atMap.put("rfInstallationPlannedTime", conductSiteSurveyBean.getRfInstallationPlannedTime());
			atMap.put("sameDayInstallation", conductSiteSurveyBean.getSameDayInstallation());
			atMap.put("btsId", conductSiteSurveyBean.getBtsId());
			atMap.put("btsName", conductSiteSurveyBean.getBtsName());
			atMap.put("btsSiteAddress", conductSiteSurveyBean.getBtsSiteAddress());
			atMap.put("btsIp", conductSiteSurveyBean.getBtsIp());
			atMap.put("btsLat", conductSiteSurveyBean.getBtsLat());
			atMap.put("btsLong", conductSiteSurveyBean.getBtsLong());
			atMap.put("customerSiteLat", conductSiteSurveyBean.getCustomerSiteLat());
			atMap.put("customerSiteLong", conductSiteSurveyBean.getCustomerSiteLong());
			atMap.put("latLong", conductSiteSurveyBean.getCustomerSiteLat() + "," + conductSiteSurveyBean.getCustomerSiteLong());
			atMap.put("sectorId", conductSiteSurveyBean.getSectorId());
			atMap.put("reasonForMastRequirement", conductSiteSurveyBean.getReasonForMastRequirement());
			if(conductSiteSurveyBean.getRfSapBundleId()!=null){
				atMap.put("rfSapBundleId", String.valueOf(conductSiteSurveyBean.getRfSapBundleId()));
			}
			if (conductSiteSurveyBean.getRfMake() != null) {
				atMap.put("rfMake", conductSiteSurveyBean.getRfMake());
				atMap.put("rfTechnology",conductSiteSurveyBean.getRfMake().toUpperCase());
			}
			if (conductSiteSurveyBean.getRfMakeModel() != null) {
				atMap.put("rfMakeModel", conductSiteSurveyBean.getRfMakeModel());
			}
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

			wfMap.put("siteReadinessStatus",(conductSiteSurveyBean.getSiteReadinessStatus().equalsIgnoreCase("Ready")));
			wfMap.put("rfSiteFeasible", (conductSiteSurveyBean.getFeasibilityStatus().equalsIgnoreCase("Feasible")));
			wfMap.put("structureType",Objects.nonNull(conductSiteSurveyBean.getStructureType()) ? conductSiteSurveyBean.getStructureType(): "Pole");			
			wfMap.put("sameDayInstallation",Objects.nonNull(conductSiteSurveyBean.getSameDayInstallation())? conductSiteSurveyBean.getSameDayInstallation().equalsIgnoreCase("yes") ? true : false: false);
			wfMap.put("mastApprovalRequired",Objects.nonNull(conductSiteSurveyBean.getMastHeightDeviation())? conductSiteSurveyBean.getMastHeightDeviation().equalsIgnoreCase("yes") ? true : false: false);
			wfMap.put("action","close");
			wfMap.put("appointmentAction", "close");
			if (conductSiteSurveyBean.getDocumentIds() != null && !conductSiteSurveyBean.getDocumentIds().isEmpty()) {
				conductSiteSurveyBean.getDocumentIds().forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			processTaskLogDetails(task,"CLOSED",conductSiteSurveyBean.getDelayReason(),null, conductSiteSurveyBean);
			response = (ConductRfSiteSurveyBean) flowableBaseService.taskDataEntry(task, conductSiteSurveyBean, wfMap);
		} catch (Exception e) {
			throw new TclCommonException("Exception occured in saveConductSiteSurveyBean {} ", e);
		}
		return response;
	}

	private void populateMap(Map<String, String> atMap, InstallRfBean installRfBean){
		atMap.put("suMacAddress", installRfBean.getSuMacAddress());
		atMap.put("typeOfAntenna", installRfBean.getTypeOfAntenna());
		atMap.put("btsSuMacAddress", installRfBean.getBtsSuMacAddress());
		atMap.put("btsSuIp", installRfBean.getBtsSuIp());
		atMap.put("btsSuSerialNumber", installRfBean.getBtsSuSerialNumber());
		atMap.put("radwinServerReachRf", installRfBean.getRadwinServerReachRf());
		atMap.put("suIp", installRfBean.getSuIp());
		atMap.put("rfFrequency", installRfBean.getRfFrequency());
		atMap.put("suSerialNumber", installRfBean.getSuSerialNumber());
		atMap.put("hsuHeightFromGround", installRfBean.getHsuHeightFromGround());
		atMap.put("averageMeanSeaLevel", installRfBean.getAverageMeanSeaLevel());
		atMap.put("hopDistance", installRfBean.getHopDistance());
		atMap.put("cableLength", installRfBean.getCableLength());
		atMap.put("ethernetExtenderUsed", installRfBean.getEthernetExtenderUsed());
		atMap.put("cableType", installRfBean.getCableType());
		atMap.put("patchCord", installRfBean.getPatchCord());
		atMap.put("rfConnectorType", installRfBean.getRfConnectorType());
		atMap.put("customerAcceptance", installRfBean.getCustomerAcceptance());
		atMap.put("lClampPoleUsed", installRfBean.getlClampPoleUsed());
		atMap.put("poleHeight", installRfBean.getPoleHeight());
		atMap.put("rfCableLength", installRfBean.getRfCableLength());
		atMap.put("earthingCableLength", installRfBean.getEarthingCableLength());
		atMap.put("powerCableLength", installRfBean.getPowerCableLength());
		atMap.put("noOfMcb", installRfBean.getNoOfMcb());
		atMap.put("noOfInstallationkits", installRfBean.getNoOfInstallationkits());
		atMap.put("noOfWeatherProofInstallationKits", installRfBean.getNoOfWeatherProofInstallationKits() );
		atMap.put("noOfPatchCordUsed", installRfBean.getNoOfPatchCordUsed());
		atMap.put("pvcConduit", installRfBean.getPvcConduit());
		atMap.put("hsuIp", installRfBean.getHsuIp());
		atMap.put("hsuSubnet", installRfBean.getHsuSubnet());
		atMap.put("gateWayIp", installRfBean.getGateWayIp());
		atMap.put("dataVlanId", installRfBean.getDataVlanId());
		atMap.put("hsuMacAddress", installRfBean.getHsuMacAddress());
		atMap.put("authenticationKey", installRfBean.getAuthenticationKey());
		atMap.put("realm", installRfBean.getRealm());
		atMap.put("defaultPortVid", installRfBean.getDefaultPortVid());
		atMap.put("deviceType", installRfBean.getDeviceType());
		atMap.put("provider", installRfBean.getProvider());
		atMap.put("customerRadioFrequency", installRfBean.getCustomerRadioFrequency());
		atMap.put("btsIp", installRfBean.getBtsIp());
		atMap.put("sectorId", installRfBean.getSectorId());
		atMap.put("btsConverterIp", installRfBean.getBtsConverterIp());
		atMap.put("errorMessage", installRfBean.getErrorMessage());
		atMap.put("smsNo", installRfBean.getSmsNo());
		atMap.put("poeSINo", installRfBean.getPoeSINo());
		atMap.put("frequency", installRfBean.getFrequency());
		if(installRfBean.getMastHeight()!=null){
			atMap.put("mastHeight", String.valueOf(installRfBean.getMastHeight()));
		}
		atMap.put("btsName", installRfBean.getBtsName());
		atMap.put("btsSiteAddress", installRfBean.getBtsSiteAddress());
		if(StringUtils.isNotBlank(installRfBean.getSectorIp())) {
			atMap.put("btsIp", installRfBean.getSectorIp());
		}
		atMap.put("channelBandwidth", installRfBean.getChannelBandwidth());
		if(installRfBean.getPhaseNetural()!=null){
			atMap.put("phaseNetural", String.valueOf(installRfBean.getPhaseNetural()));
		}
		if(installRfBean.getPhaseEarth()!=null){
			atMap.put("phaseEarth", String.valueOf(installRfBean.getPhaseEarth()));
		}
		if(installRfBean.getEarthNeutral()!=null){
			atMap.put("earthNeutral", String.valueOf(installRfBean.getEarthNeutral()));
		}
		atMap.put("earthingCableLengthCustomerEnd",installRfBean.getEarthingCableLengthCustomerEnd());
		atMap.put("earthingCableLengthBtsEnd",installRfBean.getEarthingCableLengthBtsEnd());
		atMap.put("rfCableLengthCustomerEnd", installRfBean.getRfCableLengthCustomerEnd());
		atMap.put("rfCableLengthBtsEnd",installRfBean.getRfCableLengthBtsEnd());
		atMap.put("powerCableLengthBtsEnd",installRfBean.getPowerCableLengthBtsEnd());
		atMap.put("poleMastHeightCustomerEnd",installRfBean.getPoleMastHeightCustomerEnd());
		
		atMap.put("rfMakeModel", installRfBean.getRfMakeModel());
		atMap.put("btsId", installRfBean.getBtsId());
		atMap.put("btsLat", installRfBean.getBtsLat());
		atMap.put("btsLong", installRfBean.getBtsLong());
		atMap.put("customerSiteLat", installRfBean.getCustomerSiteLat());
		atMap.put("customerSiteLong", installRfBean.getCustomerSiteLong());
		atMap.put("latLong", installRfBean.getCustomerSiteLat() + "," + installRfBean.getCustomerSiteLong());
		atMap.put("btsConverterIp", installRfBean.getBtsConverterIp());

		atMap.put("typeOfPole", installRfBean.getTypeOfPole());

		if(installRfBean.getRfEquipmentInstallationDate()!=null){
			atMap.put("rfEquipmentInstallationDate", installRfBean.getRfEquipmentInstallationDate());
		}
	}
	
	@Transactional(readOnly = false)
	public void saveInstallRfBean(Integer taskId, InstallRfBean installRfBean)
			throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
		Task task = getTaskById(taskId);
		Map<String, String> atMap = new HashMap<>();
		populateMap(atMap, installRfBean);

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		
		if (installRfBean.getDocumentIds() != null && !installRfBean.getDocumentIds().isEmpty())
			installRfBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
	
	}

	/**
	 * Method to convert conductSiteBean To Map
	 *
	 * @param jsonString
	 * @throws TclCommonException
	 */
	private static Map<String, String> conductSiteBeanToMap(String jsonString) throws TclCommonException {
		Map<String, String> atMap = new HashMap<>();
		try {
			JSONParser parser = new JSONParser();
			JSONObject mainObject = (JSONObject) parser.parse(jsonString);
			@SuppressWarnings("unchecked")
			Set<String> setKeys = mainObject.keySet();
			setKeys.stream().forEach(eachKey -> {
				if (!eachKey.equalsIgnoreCase("documentIds") && !eachKey.equalsIgnoreCase("wfTaskId") && !eachKey.equalsIgnoreCase("taskId")) {
					String eachValue = String.valueOf(mainObject.get(eachKey));
					atMap.put(eachKey, StringUtils.trimToEmpty(eachValue));
				}
			});

		} catch (Exception e) {
			throw new TclCommonException("Exception occured while convertJsonToMap {} ", e);
		}
		return atMap;
	}
	
	@Transactional(readOnly = false)
	public ColoRequest checkColoAvailability(ColoRequest coloRequest) throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
		ColoRequest response = null;
		Task task = getTaskByIdAndWfTaskId(coloRequest.getTaskId(), coloRequest.getWfTaskId());
		Map<String, String> atMap = new HashMap<>();
		atMap.put("ipSiteId", coloRequest.getIpSiteId());
		atMap.put("ipSiteName", coloRequest.getIpSiteName());
		atMap.put("isTTIdRequired", coloRequest.getIsTTIdRequired());
		atMap.put("ttId", coloRequest.getTtId());
		atMap.put("ttIdRemarks", coloRequest.getTtIdRemarks());
		atMap.put("coloProvider", coloRequest.getColoProvider());
		atMap.put("isColoFeasible", coloRequest.getIsColoFeasible());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		wfMap.put("action","close");

		processTaskLogDetails(task,"CLOSED",coloRequest.getDelayReason(),null, coloRequest);
		response = (ColoRequest) flowableBaseService.taskDataEntry(task, coloRequest, wfMap);

		return response;
	}
	
	@Transactional(readOnly = false)
	public ColoPO providePoColo(ColoPO coloPO) throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
		ColoPO response = null;
	
		Task task = getTaskByIdAndWfTaskId(coloPO.getTaskId(), coloPO.getWfTaskId());
		Map<String, String> atMap = new HashMap<>();
		atMap.put("isColoPORequired", coloPO.getIsColoPORequired());
		atMap.put("coloPONumber", coloPO.getColoPONumber());
		atMap.put("coloProvider", coloPO.getColoProvider());
		atMap.put("coloPORemarks", coloPO.getColoPORemarks());
		atMap.put("ttIdShared", coloPO.getTtIdShared());
	
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		
		wfMap.put("action","close");

		processTaskLogDetails(task,"CLOSED",coloPO.getDelayReason(),null, coloPO);
		response = (ColoPO) flowableBaseService.taskDataEntry(task, coloPO, wfMap);
		
		return response;
	}
	
	@Transactional(readOnly = false)
	public RfData provideRfData( RfData rfData) throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
		RfData response = null;
		
		Task task = getTaskByIdAndWfTaskId(rfData.getTaskId(), rfData.getWfTaskId());
		Map<String, String> atMap = new HashMap<>();
		atMap.put("isColoRequired", rfData.getIsColoRequired());
		atMap.put("ipSiteId", rfData.getIpSiteId());
		atMap.put("btsConverterRequired", rfData.getSwConvertorRequired());
		atMap.put("btsConverterIp", rfData.getBtsConverterIp());
		atMap.put("bsSwitchHostName", rfData.getBsSwitchHostName());
		atMap.put("bsSwitchIp", rfData.getBsSwitchIp());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		
		wfMap.put("action","close");

		processTaskLogDetails(task,"CLOSED",rfData.getDelayReason(),null, rfData);
		response = (RfData) flowableBaseService.taskDataEntry(task, rfData, wfMap);
	
		return response;
	}
	
	@Transactional(readOnly = false)
	public BhPo providePoBh(BhPo bhPo) throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
		BhPo response = null;
	
		Task task = getTaskByIdAndWfTaskId(bhPo.getTaskId(), bhPo.getWfTaskId());
		Map<String, String> atMap = new HashMap<>();
		atMap.put("BHConnectivity", bhPo.getBHConnectivity());
		atMap.put("bhProviderName", bhPo.getBHConnectivity());
		atMap.put("bhLMBWFeasible", bhPo.getBhLMBWFeasible());
		atMap.put("endMuxNodeName", bhPo.getEndMuxNodeName());
		atMap.put("endMuxNodeIp", bhPo.getEndMuxNodeIp());
		atMap.put("endMuxNodePort", bhPo.getEndMuxNodePort());
	
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		
		wfMap.put("action","close");

		processTaskLogDetails(task,"CLOSED",bhPo.getDelayReason(),null, bhPo);
		response = (BhPo) flowableBaseService.taskDataEntry(task, bhPo, wfMap);
	
		return response;
	}
	
	public void copyMasterVrfAttributesToSlave(Integer masterServiceId,String siteType ,InstallRfBean installRfBean)
			throws TclCommonException {
		LOGGER.info("Inside copyMasterVrfAttributesToSlave method :{} ",masterServiceId);
		List<ScServiceDetail> serviceDetailList=scServiceDetailRepository.findByMasterVrfServiceId(masterServiceId);
		Map<String, String> atMap = new HashMap<>();
		populateMap(atMap, installRfBean);
		serviceDetailList.stream().forEach(slaveServiceDetail -> {
			componentAndAttributeService.updateAttributes(slaveServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM,siteType);
			if (installRfBean.getDocumentIds() != null && !installRfBean.getDocumentIds().isEmpty())
				installRfBean.getDocumentIds().forEach(attachmentIdBean -> makeEntryInScAttachment(slaveServiceDetail, attachmentIdBean.getAttachmentId()));
		});


	}
	
}