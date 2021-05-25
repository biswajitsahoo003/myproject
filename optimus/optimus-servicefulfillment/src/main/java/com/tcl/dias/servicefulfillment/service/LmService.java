package com.tcl.dias.servicefulfillment.service;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.CITY;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.COUNTRY;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CATEGORY;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.PRODUCT_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.STATE;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import com.tcl.dias.servicefulfillmentutils.beans.SdwanOrderDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.AccessRingInfo;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.AdditionalTechnicalDetailsBean;
import com.tcl.dias.servicefulfillment.beans.BuildingAuthorityContractRequest;
import com.tcl.dias.servicefulfillment.beans.CapexBean;
import com.tcl.dias.servicefulfillment.beans.CompleteAcceptanceTestingBean;
import com.tcl.dias.servicefulfillment.beans.CompleteOspAcceptanceBean;
import com.tcl.dias.servicefulfillment.beans.ConductCrossConnectSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.ConductSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.ConductSiteSurveyManBean;
import com.tcl.dias.servicefulfillment.beans.ConfigureMuxBean;
import com.tcl.dias.servicefulfillment.beans.ConfirmAccessRingBean;
import com.tcl.dias.servicefulfillment.beans.ConfirmLmAcceptanceBean;
import com.tcl.dias.servicefulfillment.beans.CreateInventoryRecord;
import com.tcl.dias.servicefulfillment.beans.DefineScopeWorkProjectPlanBeanList;
import com.tcl.dias.servicefulfillment.beans.DeliverMuxBean;
import com.tcl.dias.servicefulfillment.beans.DemarcDetailsBean;
import com.tcl.dias.servicefulfillment.beans.IBDBean;
import com.tcl.dias.servicefulfillment.beans.InstallKroneBean;
import com.tcl.dias.servicefulfillment.beans.InstallMuxBean;
import com.tcl.dias.servicefulfillment.beans.IntegrateMuxBean;
import com.tcl.dias.servicefulfillment.beans.InternalCablingCompletionStatus;
import com.tcl.dias.servicefulfillment.beans.InternalCablingDetails;
import com.tcl.dias.servicefulfillment.beans.InternalCablingRequest;
import com.tcl.dias.servicefulfillment.beans.IzopcAdditionalTechnicalDetailsBean;
import com.tcl.dias.servicefulfillment.beans.LocationUploadValidationBean;
import com.tcl.dias.servicefulfillment.beans.LocationValidationColumnBean;
import com.tcl.dias.servicefulfillment.beans.MastInstallationPermissionBean;
import com.tcl.dias.servicefulfillment.beans.MastInstallationPlanBean;
import com.tcl.dias.servicefulfillment.beans.MrnForMuxBean;
import com.tcl.dias.servicefulfillment.beans.MrnOspMuxRequest;
import com.tcl.dias.servicefulfillment.beans.NetworkAugmentation;
import com.tcl.dias.servicefulfillment.beans.NetworkInventoryBean;
import com.tcl.dias.servicefulfillment.beans.OSPBean;
import com.tcl.dias.servicefulfillment.beans.PRowRequest;
import com.tcl.dias.servicefulfillment.beans.PaymentBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoBuildingAuthorityBean;
import com.tcl.dias.servicefulfillment.beans.ProvideRfDataJeopardyBean;
import com.tcl.dias.servicefulfillment.beans.ProwCostApproval;
import com.tcl.dias.servicefulfillment.beans.RaiseDependencyBean;
import com.tcl.dias.servicefulfillment.beans.RowBeanRequest;
import com.tcl.dias.servicefulfillment.beans.SdwaCloudGatewayBean;
import com.tcl.dias.servicefulfillment.beans.SdwanAdditionalTechnicalDetails;
import com.tcl.dias.servicefulfillment.beans.SiteReadinessDetailBean;
import com.tcl.dias.servicefulfillment.beans.SiteSurveyRescheduleRequest;
import com.tcl.dias.servicefulfillment.beans.TerminateDateBean;
import com.tcl.dias.servicefulfillment.beans.UpdateDependencyRemarksBean;
import com.tcl.dias.servicefulfillment.beans.VendorDetailsBean;
import com.tcl.dias.servicefulfillment.entity.entities.AceIPMapping;
import com.tcl.dias.servicefulfillment.entity.entities.Appointment;
import com.tcl.dias.servicefulfillment.entity.entities.AppointmentDocuments;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;
import com.tcl.dias.servicefulfillment.entity.entities.MstAppointmentSlots;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.entities.Vendors;
import com.tcl.dias.servicefulfillment.entity.repository.AceIPMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstAppointmentDocumentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstAppointmentSlotsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstVendorsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VendorsRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AssistCmipBean;
import com.tcl.dias.servicefulfillmentutils.beans.ByonReadinessBean;
import com.tcl.dias.servicefulfillmentutils.beans.CosDetail;
import com.tcl.dias.servicefulfillmentutils.beans.CpeDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerAppointmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetails;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.LLDAndMigrationBean;
import com.tcl.dias.servicefulfillmentutils.beans.LLDPreparationBean;
import com.tcl.dias.servicefulfillmentutils.beans.MuxDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.PlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionAttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * LmService this class is used to perform last mile implementation related
 * tasks.
 * 
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited LmService used for the lm related
 *            implementation endpoint
 */

@Service
@Transactional(readOnly = true)
public class LmService extends ServiceFulfillmentBaseService{
	
	private static final Logger logger = LoggerFactory.getLogger(LmService.class);
	public static final String UA="\u00A0";

	
	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	FieldEngineerRepository fieldEngineerRepository;

	@Autowired
	VendorsRepository vendorsRepository;

	@Autowired
	MstAppointmentSlotsRepository mstAppointmentSlotsRepository;

	@Autowired
	MstAppointmentDocumentRepository mstAppointmentDocumentRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	AceIPMappingRepository aceIPMappingRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	MstTaskDefRepository mstTaskDefRepository;
	
	
	private static MissingCellPolicy xRow;
	
	 @Autowired
	 RuntimeService runtimeService;
	 
	 @Autowired
	 MstVendorsRepository mstVendorsRepository;
	 

    @Autowired
    ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	  @Autowired
	  FlowableBaseService flowableBaseService;
	  
	  @Autowired
	  CommonFulfillmentUtils commonFulfillmentUtils;
	  
	  @Autowired
	 private TaskAssignmentRepository taskAssignmentRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;

	/**
	 * This method is used to check site readiness confirmation.
	 *
	 * @author mayanks
	 * @param taskId
	 * @param siteReadinessDetailBean
	 * @param wfMap
	 * @return SiteReadinessDetailBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public SiteReadinessDetailBean siteReadinessConfirmation(SiteReadinessDetailBean siteReadinessDetailBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(siteReadinessDetailBean.getTaskId(), siteReadinessDetailBean.getWfTaskId());
		Map<String, Object> wfMap = new HashMap<>();
		Map<String, String> atMap = new HashMap<>();
		if (siteReadinessDetailBean.getIsSiteReady().equals("Ready")) {
			wfMap.put("siteReadinessConfirmation", true);
			atMap.put("siteReadinessConfirmation", String.valueOf(true));

		} else {
			wfMap.put("siteReadinessConfirmation", false);
			atMap.put("siteReadinessConfirmation", String.valueOf(false));

		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if (siteReadinessDetailBean.getIsSiteReady().equalsIgnoreCase("Not Ready")) {

			return (SiteReadinessDetailBean)updateTentativeDate(task, siteReadinessDetailBean, wfMap,
					DateUtil.convertStringToDateYYMMDD(siteReadinessDetailBean.getTentativeDate()));
		} else {
			processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,siteReadinessDetailBean.getDelayReason(),null, siteReadinessDetailBean);
			return (SiteReadinessDetailBean)flowableBaseService.taskDataEntry(task, siteReadinessDetailBean, wfMap);
		}

	}

	/**
	 * This method is used to saves mux details.
	 *
	 * @author mayanks
	 * @param taskId
	 * @param muxDetailBean
	 * @return MuxDetailBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public MuxDetailBean saveMuxDetails(MuxDetailBean muxDetailBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(muxDetailBean.getTaskId(), muxDetailBean.getWfTaskId());
		Map<String, Object> flowableMap = new HashMap<>();
		
		Map<String, String> compMap = new HashMap<>();
		compMap.put("endMuxNodeIp", muxDetailBean.getEndMUXNodeIP());
		compMap.put("endMuxNodeName",muxDetailBean.getEndMUXNodeName());
		compMap.put("endMuxNodePort",muxDetailBean.getEndMuxNodePort());
		compMap.put("skipAutoClr", muxDetailBean.getSkipAutoClr());
		compMap.put("exceptionClousureCategory",muxDetailBean.getExceptionClousureCategory());
		compMap.put("exceptionClosureRemarks",muxDetailBean.getExceptionClosureRemarks());

		flowableMap.put("isMuxIPAvailable", true);
		flowableMap.put("muxName", muxDetailBean.getEndMUXNodeName());
		
		String action = StringUtils.trimToEmpty(muxDetailBean.getAction());
		if(StringUtils.isBlank(action))action="close";
		if("Yes".equalsIgnoreCase(muxDetailBean.getSkipAutoClr())){
			logger.info("saveMuxDetails::Skip");
			flowableMap.put("action", "skip");
		}else{
			logger.info("saveMuxDetails::close");
			flowableMap.put("action", action);
		}
		
		validateInputs(task, muxDetailBean);

		if (muxDetailBean.getIsPEInternalCablingRequired() != null
				&& muxDetailBean.getIsPEInternalCablingRequired().equalsIgnoreCase("yes")) {
			flowableMap.put("isPEInternalCablingRequired", true);
			compMap.put("peEndPhysicalPort", muxDetailBean.getPeEndPhysicalPort());
			compMap.put("isPEInternalCablingRequired",muxDetailBean.getIsPEInternalCablingRequired());
		}
		
		
		
		flowableMap.put("lmConnectionTypeChange", false);	
		
		if (muxDetailBean.getLastmileException() != null
				&& muxDetailBean.getLastmileException().equalsIgnoreCase("YES")) {
			
			String lastMileScenario=null;
			if (muxDetailBean.getLastmileConnectScenerio().equalsIgnoreCase("Connected Building")) {

				flowableMap.put("isConnectedSite", false);
				flowableMap.put("prowRequired", true);
				flowableMap.put("rowRequired ", false);
				flowableMap.put("isConnectedBuilding", true);
				flowableMap.put("lmConnectionTypeChange", true);
				lastMileScenario="Onnet Wireline - Connected Building";
				action ="changelm";
				flowableMap.put("action", action);

			} else if (muxDetailBean.getLastmileConnectScenerio().equalsIgnoreCase("Not Connected")) {

				flowableMap.put("isConnectedSite", false);
				flowableMap.put("prowRequired", true);
				flowableMap.put("rowRequired ", true);
				flowableMap.put("isConnectedBuilding", false);
				flowableMap.put("lmConnectionTypeChange", true);
				lastMileScenario="Onnet Wireline - Near Connect";
				action ="changelm";
				flowableMap.put("action", action);

			}
			if(lastMileScenario!=null) {
				compMap.put("lastMileScenario", lastMileScenario);
				
				Optional<ScServiceDetail> optionalServisc=scServiceDetailRepository.findById(task.getServiceId());
				if (optionalServisc.isPresent()) {
					ScServiceDetail scServiceDetail = optionalServisc.get();
					scServiceDetail.setLastmileScenario(lastMileScenario);
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), compMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if(CommonConstants.GVPN.equalsIgnoreCase(task.getScServiceDetail().getErfPrdCatalogProductName())
				&& task.getScServiceDetail()!=null && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getMultiVrfSolution())) {
			copyMasterVrfAttributesToSlave(task.getScServiceDetail(),task.getSiteType(),compMap);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,muxDetailBean.getDelayReason(),null, muxDetailBean);
		return (MuxDetailBean) flowableBaseService.taskDataEntry(task, muxDetailBean, flowableMap);
	}

	/**
	 * saves field engineer details in task data and fieldEngineer table
	 * 
	 * @author mayanks
	 * @param taskId
	 * @param fieldEngineerDetailsBean
	 * @return fieldEngineerDetailsBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public FieldEngineerDetailsBean saveFieldEngineerDetails(FieldEngineerDetailsBean fieldEngineerDetailsBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(fieldEngineerDetailsBean.getTaskId(), fieldEngineerDetailsBean.getWfTaskId());
		saveFieldEngineer(task, fieldEngineerDetailsBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,fieldEngineerDetailsBean.getDelayReason(),null, fieldEngineerDetailsBean);
		flowableBaseService.taskDataEntry(task, fieldEngineerDetailsBean);
		return fieldEngineerDetailsBean;
	}
	
	@Transactional(readOnly = false)
	public FieldEngineerDetailsBean saveOffnetFieldEngineerDetails(FieldEngineerDetailsBean fieldEngineerDetailsBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(fieldEngineerDetailsBean.getTaskId(),fieldEngineerDetailsBean.getWfTaskId());
		saveFieldEngineer(task, fieldEngineerDetailsBean);
		createAppointmentTask(task,fieldEngineerDetailsBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,fieldEngineerDetailsBean.getDelayReason(),null, fieldEngineerDetailsBean);
		flowableBaseService.taskDataEntry(task, fieldEngineerDetailsBean);
		return fieldEngineerDetailsBean;
	}
	
	private void createAppointmentTask(Task task,FieldEngineerDetailsBean fieldEngineerDetailsBean) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(new Timestamp(DateUtil.convertStringToDateYYMMDD(fieldEngineerDetailsBean.getAppointmentDate()).getTime()));
		appointment.setServiceId(task.getServiceId());
		appointment.setTask(task);
		appointment.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
		appointment.setAppointmentType(getTaskType(task.getMstTaskDef().getKey()));
		appointment.setMstAppointmentSlot(getMstAppointmentSlotById(fieldEngineerDetailsBean.getAppointmentSlot()));
		appointmentRepository.save(appointment);
	}

	/**
	 * This private method is used to save field engineer details.
	 * 
	 * @author mayanks
	 * @param task
	 * @param fieldEngineerDetailsBean
	 */
	protected void saveFieldEngineer(Task task, FieldEngineerDetailsBean fieldEngineerDetailsBean) {
		FieldEngineer fieldEngineer = new FieldEngineer();
		fieldEngineer.setName(fieldEngineerDetailsBean.getName());
		fieldEngineer.setTask(task);
		fieldEngineer.setMobile(fieldEngineerDetailsBean.getContactNumber());
		fieldEngineer.setEmail(fieldEngineerDetailsBean.getEmailId());
		fieldEngineer.setServiceId(task.getServiceId());
		fieldEngineer.setAppointmentType(getTaskType(task.getMstTaskDef().getKey()));
		fieldEngineer.setSecondaryname(fieldEngineerDetailsBean.getSecondaryName());
		fieldEngineer.setSecondarymobile(fieldEngineerDetailsBean.getSecondaryContactNumber());
		fieldEngineer.setSecondaryemail(fieldEngineerDetailsBean.getSecondaryEmailId());
		fieldEngineerRepository.save(fieldEngineer);

	}
	
	protected String getTaskType(String key) {     
    	String value ="";
    	if(key.contains("arrange-field-engineer")) {
    		value = key.replace("arrange-field-engineer-","");    
    	}else if(key.contains("customer-appointment")) {
    		value = key.replace("customer-appointment-","");      		
    	}else if(key.contains("select-vendor")) {
    		value = key.replace("select-vendor-","");
    	}else if(key.contains("assign-poc-")) {
    		value = key.replace("assign-poc-","poc-");
    	}
        return value;
    }

	/**
	 * save vendor details in task data and vendor table
	 *
	 * @author mayanks
	 * @param vendorDetailsBean
	 * @return vendorDetailsBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public VendorDetailsBean saveVendorDetails(VendorDetailsBean vendorDetailsBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(vendorDetailsBean.getTaskId(), vendorDetailsBean.getWfTaskId());
		saveVendorDetail(task, vendorDetailsBean);
		
		if(vendorDetailsBean.getVendorType()!=null && vendorDetailsBean.getVendorType().equalsIgnoreCase("OSP")) {
			FieldEngineerDetailsBean fieldEngineerDetail=new FieldEngineerDetailsBean();
			fieldEngineerDetail.setName(vendorDetailsBean.getOspFeName());
			fieldEngineerDetail.setContactNumber(vendorDetailsBean.getOspFeContactNumber());
			fieldEngineerDetail.setEmailId(vendorDetailsBean.getOspFeEmailId());
			fieldEngineerDetail.setSecondaryContactNumber(vendorDetailsBean.getOspSecondaryFeContactNumber());
			fieldEngineerDetail.setSecondaryEmailId(vendorDetailsBean.getOspSecondaryFeEmailId());
			fieldEngineerDetail.setSecondaryName(vendorDetailsBean.getOspSecondaryFeName());
			saveFieldEngineer(task, fieldEngineerDetail);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,vendorDetailsBean.getDelayReason(),null, vendorDetailsBean);
		flowableBaseService.taskDataEntry(task, vendorDetailsBean);
		return vendorDetailsBean;
	}

	/**
	 * This private method is used to save vendor details.
	 * 
	 * @author mayanks
	 * @param task
	 * @param vendorDetailsBean
	 */
	private void saveVendorDetail(Task task, VendorDetailsBean vendorDetailsBean) {
		
		Vendors vendors = new Vendors();
		
		if(vendorDetailsBean.getMstVendorId()!=null) {
			vendors.setMstVendor(getMstVendor(vendorDetailsBean.getMstVendorId()));
		}else {
			MstVendor mstVendor = new MstVendor();
			mstVendor.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
			mstVendor.setName(vendorDetailsBean.getName());
			mstVendor.setContactName(vendorDetailsBean.getContactName());
			mstVendor.setEmail(vendorDetailsBean.getEmailId());
			mstVendor.setPhoneNumber(vendorDetailsBean.getContactNumber());
			mstVendor.setCircle(vendorDetailsBean.getCircle());
			mstVendor.setVendorId(vendorDetailsBean.getVendorId());
			mstVendor.setType(vendorDetailsBean.getVendorType());
			mstVendorsRepository.save(mstVendor);
			vendors.setMstVendor(mstVendor);
		}
		
		
		vendors.setName(vendorDetailsBean.getName());
		vendors.setEmail(vendorDetailsBean.getEmailId());
		vendors.setPhoneNumber(vendorDetailsBean.getContactNumber());
		vendors.setTask(task);
		vendors.setServiceId(task.getServiceId());		
		vendorsRepository.save(vendors);
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
	public Object scheduleCustomerAppointment(CustomerAppointmentBean customerAppointmentBean)
			throws TclCommonException {
		Map<String, Object> flowableMap = new HashMap<>();
		Task appointmentTask = getTaskByIdAndWfTaskId(customerAppointmentBean.getTaskId(), customerAppointmentBean.getWfTaskId());
		String taskDefKey = appointmentTask.getMstTaskDef().getKey();
		flowableMap.put(taskDefKey + "-end-time", DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()));
		flowableMap.put(taskDefKey + "-time-slot", customerAppointmentBean.getAppointmentSlot());
		
		List<MstTaskDef> mstTaskDefs=mstTaskDefRepository.findByDependentTaskKey(taskDefKey);
		
		if(!mstTaskDefs.isEmpty()) {
			
			mstTaskDefs.forEach(mstdef->{
				flowableMap.put(mstdef.getFormKey()+"-start-time",DateUtil.convertStringToDateYYMMDD( customerAppointmentBean.getAppointmentDate()));
				flowableMap.put(mstdef.getFormKey()+"-time-slot", customerAppointmentBean.getAppointmentSlot());
			});
		}
		if (customerAppointmentBean.getConnectorType() != null) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("connectorType", customerAppointmentBean.getConnectorType());
			componentAndAttributeService.updateAttributes(appointmentTask.getServiceId(), atMap,
					AttributeConstants.COMPONENT_LM, appointmentTask.getSiteType());
		}
/*
		switch (taskDefKey) {
		
		
		case "customer-appointment-ss":
			flowableMap.put("lm-conduct-site-survey-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("lm-conduct-site-survey-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-osp":
			flowableMap.put("lm-complete-osp-work-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("lm-complete-osp-work-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-ibd":
			flowableMap.put("lm-complete-ibd-work-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("lm-complete-ibd-work-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-mux-installation":
			flowableMap.put("lm-install-mux-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("lm-install-mux-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-rf-ss":
			flowableMap.put("lm-rf-conduct-site-survey-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("lm-rf-conduct-site-survey-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-rf-installation":
			flowableMap.put("install-rf-equipment-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("install-rf-equipment-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-lm-test":
			flowableMap.put("conduct-lm-test-onnet-wireline-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("conduct-lm-test-onnet-wireline-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-failover-testing":
			flowableMap.put("conduct-failover-test-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("conduct-failover-test-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-cpe-installation":
			flowableMap.put("install-cpe-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("install-cpe-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		case "customer-appointment-cable-extension":
			flowableMap.put("complete-internal-cabling-ce-start-time", customerAppointmentBean.getAppointmentDate());
			flowableMap.put("complete-internal-cabling-ce-time-slot", customerAppointmentBean.getAppointmentSlot());
			break;
		}*/

		Task task = saveAppointmentDetails(appointmentTask, customerAppointmentBean);
		if (customerAppointmentBean.getAttachmentPermissionId() != null) {
			makeEntryInScAttachment(task, customerAppointmentBean.getAttachmentPermissionId());
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,customerAppointmentBean.getDelayReason(),null, customerAppointmentBean);
		flowableBaseService.taskDataEntry(task, customerAppointmentBean, flowableMap);
		return customerAppointmentBean;
	}

	@Transactional(readOnly = false)
	public Object scheduleSdwanCustomerAppointment(CustomerAppointmentBean customerAppointmentBean)
			throws TclCommonException {
		Map<String, Object> flowableMap = new HashMap<>();
		Task appointmentTask = getTaskByIdAndWfTaskId(customerAppointmentBean.getTaskId(),
				customerAppointmentBean.getWfTaskId());
		String taskDefKey = appointmentTask.getMstTaskDef().getKey();
		flowableMap.put(taskDefKey + "-end-time",
				DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()));
		flowableMap.put(taskDefKey + "-time-slot", customerAppointmentBean.getAppointmentSlot());

		List<MstTaskDef> mstTaskDefs = mstTaskDefRepository.findByDependentTaskKey(taskDefKey);

		if (!mstTaskDefs.isEmpty()) {

			mstTaskDefs.forEach(mstdef -> {
				flowableMap.put(mstdef.getFormKey() + "-start-time",
						DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()));
				flowableMap.put(mstdef.getFormKey() + "-time-slot", customerAppointmentBean.getAppointmentSlot());
			});
		}
		if (customerAppointmentBean.getConnectorType() != null) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("connectorType", customerAppointmentBean.getConnectorType());
			componentAndAttributeService.updateAttributes(appointmentTask.getServiceId(), atMap,
					AttributeConstants.COMPONENT_LM, appointmentTask.getSiteType());
		}

		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(appointmentTask.getServiceId());
		ScSolutionComponent scSolutionComp = scSolutionComponentRepository.findByScServiceDetail1(scServiceDetail.get());
		Task task = null;
		if("UNDERLAY".equalsIgnoreCase(scSolutionComp.getComponentGroup())){
			logger.info("saving appointment details for underlay: {} and overlayId: {}",appointmentTask.getServiceId(),scSolutionComp.getScServiceDetail2().getId());
			task = saveSdwanAppointmentDetails(appointmentTask, customerAppointmentBean,scSolutionComp.getScServiceDetail2().getId());
		}else{
			logger.info("saving appointment details for overlay: {}",appointmentTask.getServiceId());
			task = saveSdwanAppointmentDetails(appointmentTask, customerAppointmentBean, appointmentTask.getServiceId());
		}

		if (customerAppointmentBean.getAttachmentPermissionId() != null) {
			makeEntryInScAttachment(task, customerAppointmentBean.getAttachmentPermissionId());
		}

		logger.info("IZOSDWAN scheduleCustomerAppointment Task exists for taskId::{},ordercode::{},serviceId::{}",
				task.getId(), task.getOrderCode(), task.getServiceId());
		ScSolutionComponent scSolutionComponent = scSolutionComponentRepository
				.findByScServiceDetail1_idAndOrderCodeAndIsActive(task.getServiceId(), task.getOrderCode(), "Y");
		if (scSolutionComponent != null) {
			logger.info("ScheduleCustomerAppointment.ScSolutionExists::{}", scSolutionComponent.getId());
			List<Integer> serviceList = null;
			if ("OVERLAY".equalsIgnoreCase(scSolutionComponent.getComponentGroup())) {
				logger.info("ScheduleCustomerAppointment.OVERLAY enters to downtime::{}", task.getId());
				serviceList = scSolutionComponentRepository.getServiceDetailsBySolutionIdAndOverlayId(
						scSolutionComponent.getScServiceDetail3().getId(),
						scSolutionComponent.getScServiceDetail1().getId(),
						scSolutionComponent.getScServiceDetail1().getId(), "Y");
			} else if ("UNDERLAY".equalsIgnoreCase(scSolutionComponent.getComponentGroup())) {
				logger.info("ScheduleCustomerAppointment.UNDERLAY enters to downtime::{}", task.getId());
				serviceList = scSolutionComponentRepository
						.getServiceDetailsByOrderCodeAndUnderlayId(task.getOrderCode(), task.getServiceId(), "Y");
				logger.info("Adding overlay id for underlay::{}", scSolutionComponent.getScServiceDetail2().getId());
				serviceList.add(scSolutionComponent.getScServiceDetail2().getId());
			}
			if (serviceList != null && !serviceList.isEmpty()) {
				logger.info("ServiceList exists::{}", serviceList.size());
				for (Integer serviceId : serviceList) {
					logger.info("serviceId::{}", serviceId);
					List<Task> waitForCustomerTasks = taskRepository
							.findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(serviceId,
									"wait-for-customer-appointment-for-cpe-installation", "CLOSED");
					for (Task waitForCustomerTask : waitForCustomerTasks) {
						Execution execution = runtimeService.createExecutionQuery()
								.processInstanceId(waitForCustomerTask.getWfProcessInstId())
								.activityId("wait-for-customer-appointment-for-cpe-installation").singleResult();
						if (execution != null) {
							logger.info("Execution exists for service Id::{},with excution id::{}",
									waitForCustomerTask.getServiceId(), execution.getId());
							runtimeService.trigger(execution.getId());
						}
					}
				}
			}
		}
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, customerAppointmentBean.getDelayReason(), null,
				customerAppointmentBean);
		flowableBaseService.taskDataEntry(task, customerAppointmentBean, flowableMap);
		return customerAppointmentBean;
	}

	/**
	 * This method is used to Create MRN for Mux.
	 *
	 * @param taskId
	 * @param mrnForMuxBean
	 * @return MrnForMuxBean
	 * @throws TclCommonException
	 * @author diksha garg createMrnForMux
	 */
	@Transactional(readOnly = false)
	public MrnForMuxBean createMnrForMux(MrnForMuxBean mrnForMuxBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(mrnForMuxBean.getTaskId(), mrnForMuxBean.getWfTaskId());
		validateInputs(task, mrnForMuxBean);
		if (mrnForMuxBean.getDocumentIds() != null ) {
			mrnForMuxBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, String> atMap = new HashMap<>();
		Map<String, Object> flowableMap = new HashMap<>();
		if(mrnForMuxBean.getMrnMuxRequired()!=null && mrnForMuxBean.getMrnMuxRequired().equalsIgnoreCase("yes"))
		{
			flowableMap.put("mrnMuxRequired","Yes" );
		}
		else
		{
			flowableMap.put("mrnMuxRequired", "No");
			Task lmDeliveryMux = taskRepository
					.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(task.getServiceId(), "lm-deliver-mux");
			if (lmDeliveryMux != null
					&& lmDeliveryMux.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)) {
				lmDeliveryMux.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
				taskRepository.save(lmDeliveryMux);

			}
		}
		atMap.put("muxMrnNo", mrnForMuxBean.getMrnNumber());
		atMap.put("mrnMuxRequired", mrnForMuxBean.getMrnMuxRequired());
		atMap.put("muxMrnDate", mrnForMuxBean.getMrnDate());
		atMap.put("muxDistributionCenterName", mrnForMuxBean.getDistributionCenterName());
		atMap.put("muxDistributionCenterAddress", mrnForMuxBean.getDistributionCenterAddress());
		atMap.put("mrnMuxremarks", mrnForMuxBean.getRemarks());

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		String errorMessage=mrnForMuxBean.getRemarks();
		if (Objects.nonNull(task.getScServiceDetail())&& StringUtils.isNotBlank(errorMessage)) {
			componentAndAttributeService.updateAdditionalAttributes(task.getScServiceDetail(),
					task.getMstTaskDef().getKey()+"_error",
					componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
					AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());

		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,mrnForMuxBean.getDelayReason(),null, mrnForMuxBean);
		return (MrnForMuxBean) flowableBaseService.taskDataEntry(task, mrnForMuxBean,flowableMap);
	}

	/**
	 * This method is used to Release MRN for OSP/IBD Material.
	 *
	 * @param taskId
	 * @param mrnForMuxBean
	 * @return MrnForMuxBean
	 * @throws TclCommonException
	 * @author diksha garg releaseMrnForOspIbdMaterial
	 */
	@Transactional(readOnly = false)
	public MrnOspMuxRequest releaseMrnForOspIbdMaterial(MrnOspMuxRequest mrnOspMuxRequest)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(mrnOspMuxRequest.getTaskId(), mrnOspMuxRequest.getWfTaskId());
		validateInputs(task, mrnOspMuxRequest);
		if (mrnOspMuxRequest.getDocumentIds() != null && !mrnOspMuxRequest.getDocumentIds().isEmpty())
			mrnOspMuxRequest.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,mrnOspMuxRequest.getDelayReason(),null, mrnOspMuxRequest);
		return (MrnOspMuxRequest) flowableBaseService.taskDataEntry(task, mrnOspMuxRequest);
	}

	/**
	 * This method is used to Install Mux.
	 *
	 * @param taskId
	 * @param installMuxBean
	 * @return InstallMuxBean
	 * @throws TclCommonException
	 * @author diksha garg installMux
	 */
	@Transactional(readOnly = false)
	public InstallMuxBean installMux(InstallMuxBean installMuxBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(installMuxBean.getTaskId(), installMuxBean.getWfTaskId());
		validateInputs(task, installMuxBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,installMuxBean.getDelayReason(),null, installMuxBean);
	
		if(!CollectionUtils.isEmpty(installMuxBean.getDocumentIds())){
			installMuxBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("appointmentAction", "close");

		return (InstallMuxBean) flowableBaseService.taskDataEntry(task, installMuxBean,wfMap);
	}

	/**
	 * This method is used to Define Scope of Work & Project Plan.
	 *
	 * @param taskId
	 * @param defineScopeWorkProjectPlanBean
	 * @return DefineScopeWorkProjectPlanBean
	 * @throws TclCommonException
	 * @author diksha garg defineScopeWorkProjectPlan
	 */
	@Transactional(readOnly = false)
	public DefineScopeWorkProjectPlanBeanList defineScopeWorkProjectPlan(
			DefineScopeWorkProjectPlanBeanList defineScopeWorkProjectPlanBeanList) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(defineScopeWorkProjectPlanBeanList.getTaskId(), defineScopeWorkProjectPlanBeanList.getWfTaskId());
		validateInputs(task, defineScopeWorkProjectPlanBeanList);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,defineScopeWorkProjectPlanBeanList.getDelayReason(),null, defineScopeWorkProjectPlanBeanList);
		return (DefineScopeWorkProjectPlanBeanList) flowableBaseService.taskDataEntry(task, defineScopeWorkProjectPlanBeanList);
	}

	/**
	 * This method is used to Provide PO to Building Authority.
	 *
	 * @param taskId
	 * @param providePoBuildingAuthorityBean
	 * @return ProvidePoBuildingAuthorityBean
	 * @throws TclCommonException
	 * @author diksha garg providePoBuildingAuthority
	 */
	@Transactional(readOnly = false)
	public ProvidePoBuildingAuthorityBean providePoBuildingAuthority(
			ProvidePoBuildingAuthorityBean providePoBuildingAuthorityBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(providePoBuildingAuthorityBean.getTaskId(), providePoBuildingAuthorityBean.getWfTaskId());
		validateInputs(task, providePoBuildingAuthorityBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("prowContractEndDate", providePoBuildingAuthorityBean.getContractEndDate());
		atMap.put("prowContractStartDate", providePoBuildingAuthorityBean.getContractStartDate());
		atMap.put("prowOneTimeCharge", String.valueOf(providePoBuildingAuthorityBean.getOneTimeCharge()));
		atMap.put("prowRecurringFixedCharge", String.valueOf(providePoBuildingAuthorityBean.getRecurringFixedCharge()));
		atMap.put("prowRecurringVariableCharge",
				String.valueOf(providePoBuildingAuthorityBean.getRecurringVariableCharge()));
		atMap.put("prowTaxes", String.valueOf(providePoBuildingAuthorityBean.getTaxes()));
		atMap.put("prowBankGuarantee", String.valueOf(providePoBuildingAuthorityBean.getBankGuarantee()));
		atMap.put("prowSecurityDeposit", String.valueOf(providePoBuildingAuthorityBean.getSecurityDeposit()));
		atMap.put("prowInvoiceNumber", String.valueOf(providePoBuildingAuthorityBean.getInvoiceNumber()));
		if (providePoBuildingAuthorityBean.getDocumentIds() != null && !providePoBuildingAuthorityBean.getDocumentIds().isEmpty()) {
			providePoBuildingAuthorityBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,providePoBuildingAuthorityBean.getDelayReason(),null, providePoBuildingAuthorityBean);
		return (ProvidePoBuildingAuthorityBean) flowableBaseService.taskDataEntry(task, providePoBuildingAuthorityBean);
	}

	

	/**
	 * ` Review and finalize Capex Deviation
	 * <p>
	 * approveCapex
	 *
	 * @param taskId
	 * @param capexBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public CapexBean approveCapex(CapexBean capexBean) throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(capexBean.getTaskId(), capexBean.getWfTaskId());
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,capexBean.getDelayReason(),null, capexBean);
		Map<String, Object> wfMap = new HashMap<>();
	   	wfMap.put("action", "close");
		return (CapexBean)flowableBaseService.taskDataEntry(task, capexBean,wfMap);
	}

	/**
	 * This method is used to complete the OSP Work.
	 *
	 * @param taskId
	 * @param ospBean
	 * @return
	 * @throws IOException
	 */
	@Transactional(readOnly = false)
	public Object completeOSPWork(OSPBean ospBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(ospBean.getTaskId(), ospBean.getWfTaskId());
		String action = "CLOSED";

		if (ospBean.getAction() != null && ospBean.getAction().equalsIgnoreCase("save")) {
			action = "SAVED";
		}
		ospBean.getDocumentIds()
				.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,action,ospBean.getDelayReason(),null, ospBean);
		Map<String, Object> wfMap = new HashMap<>();
	   	wfMap.put("action", "close");
	   	
		if (action.equalsIgnoreCase("CLOSED")) {

			return flowableBaseService.taskDataEntry(task, ospBean,wfMap);
		} else {
			return flowableBaseService.taskDataEntrySave(task, ospBean);
		}
		
	}


	/**
	 * This method is used to complete the IBD Work.
	 *
	 * @param taskId
	 * @param ospBean
	 * @return
	 */
	@Transactional(readOnly = false)
	public Object completeIBDWork(IBDBean ibdBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(ibdBean.getTaskId(), ibdBean.getWfTaskId());
		Map<String, String> fMap = new HashMap<>();
		
		String action = "CLOSED";

		if (ibdBean.getAction() != null && ibdBean.getAction().equalsIgnoreCase("save")) {
			action = "SAVED";
		}
		fMap.put("ibdCompletionDate", ibdBean.getCompletionDate());
		fMap.put("cableLength", String.valueOf(ibdBean.getCableLength()));
		fMap.put("fms", ibdBean.getFms());
		fMap.put("typeOfInternalCabling", ibdBean.getTypeOfInternalCabling());
		fMap.put("gisLocationId", ibdBean.getGisLocationId());
		fMap.put("typeOfRack", ibdBean.getTypeOfRack());
		fMap.put("rackSize", ibdBean.getRackSize());
		fMap.put("rackName", ibdBean.getRackName());
		fMap.put("rackId", ibdBean.getRackId());
		fMap.put("cableId", ibdBean.getCableId());
		if(ibdBean.getRackWidth()!=null){
			fMap.put("rackWidth", String.valueOf(ibdBean.getRackWidth()));
		}
		fMap.put("rackHeight", ibdBean.getRackHeight());
		if(ibdBean.getRackDepth()!=null){
			fMap.put("rackDepth", String.valueOf(ibdBean.getRackDepth()));
		}
		if(ibdBean.getDocumentIds()!=null && !ibdBean.getDocumentIds().isEmpty()){
			ibdBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), fMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,action,ibdBean.getDelayReason(),null, ibdBean);
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("appointmentAction", "close");
		wfMap.put("action", "close");

		if (action.equalsIgnoreCase("CLOSED")) {

			return flowableBaseService.taskDataEntry(task, ibdBean, wfMap);
		} else {
			return flowableBaseService.taskDataEntrySave(task, ibdBean);
		}

	}


	/**
	 * Pay to Building Authority to conduct IBD Work.
	 *
	 * @param documentIds
	 * @param attachmentIdBean
	 * @param taskId
	 * @param attachmentIdBeans
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Transactional(readOnly = false)
	public PaymentBean payBuildingAuthority(PaymentBean paymentBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(paymentBean.getTaskId(), paymentBean.getWfTaskId());
		validateInputs(task, paymentBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("prowTransactionId",paymentBean.getProwTransactionId());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if (paymentBean.getDocumentIds() != null && !paymentBean.getDocumentIds().isEmpty())
			paymentBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,paymentBean.getDelayReason(),null, paymentBean);
		return (PaymentBean) flowableBaseService.taskDataEntry(task, paymentBean);
	}

	

	/**
	 * This method is used to build authority contract.
	 *
	 * @author mayanks
	 * @param taskId
	 * @param rowBean
	 * @return RowBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public BuildingAuthorityContractRequest buildingAuthorityContract(
			BuildingAuthorityContractRequest buContractRequest) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(buContractRequest.getTaskId(), buContractRequest.getWfTaskId());
		validateInputs(task, buContractRequest);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("prowContractEndDate", buContractRequest.getContractEndDate());
		atMap.put("prowContractStartDate", buContractRequest.getContractStartDate());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if (buContractRequest.getDocumentIds() != null && !buContractRequest.getDocumentIds().isEmpty())
			buContractRequest.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, buContractRequest.getDelayReason(), null, buContractRequest);
		return (BuildingAuthorityContractRequest) flowableBaseService.taskDataEntry(task, buContractRequest);
	}

	/**
	 * This method is used to apply for PROW.
	 * 
	 * @author mayanks
	 * @param taskId
	 * @param rowBean
	 * @return RowBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public PRowRequest applyForPRow(PRowRequest pRowRequest) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(pRowRequest.getTaskId(), pRowRequest.getWfTaskId());
		Map<String, Object> flowableMap = new HashMap<>();
		flowableMap.put("action", "close");
		boolean ibdContractRequired = false;

		if (pRowRequest.getContractRequired() != null && pRowRequest.getContractRequired().equalsIgnoreCase("Yes")) {
			ibdContractRequired = true;
		}

		flowableMap.put("ibdContractRequired", ibdContractRequired);
		validateInputs(task, pRowRequest);


		Map<String, String> atMap = new HashMap<>();
		atMap.put("buildingAuthorityName", pRowRequest.getBuildingAuthorityName());
		atMap.put("buildingAuthorityAddress", pRowRequest.getBuildingAuthorityAddress());
		atMap.put("buildingAuthorityContactNumber", pRowRequest.getBuildingAuthorityContactNumber());
		atMap.put("buildingAuthorityVendorId", pRowRequest.getBuildingAuthorityVendorId());
		atMap.put("prowOneTimeCharge", String.valueOf(pRowRequest.getOneTimeCharge()));
		atMap.put("prowRecurringFixedCharge", String.valueOf(pRowRequest.getRecurringFixedCharge()));
		atMap.put("prowRecurringVariableCharge", String.valueOf(pRowRequest.getRecurringVariableCharge()));
		atMap.put("prowTaxes", String.valueOf(pRowRequest.getTaxes()));
		atMap.put("prowBankGuarantee", String.valueOf(pRowRequest.getBankGuarantee()));
		atMap.put("prowSecurityDeposit", String.valueOf(pRowRequest.getSecurityDeposit()));
		atMap.put("ibdContractRequired", pRowRequest.getContractRequired());
		atMap.put("buildingAuthorityEmailId", pRowRequest.getBuildingAuthorityEmailId());
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		if (pRowRequest.getDocumentIds() != null && !pRowRequest.getDocumentIds().isEmpty())
			pRowRequest.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,pRowRequest.getDelayReason(),null, pRowRequest);
		return (PRowRequest) flowableBaseService.taskDataEntry(task, pRowRequest, flowableMap);
	}

	/**
	 * This method is used to row government payments
	 * 
	 * @author mayanks
	 * @param taskId
	 * @param attachmentId
	 * @return AttachmentIdBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public PaymentBean rowGovtPayments( PaymentBean paymentBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(paymentBean.getTaskId(), paymentBean.getWfTaskId());
		validateInputs(task, paymentBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("rowTransactionId",paymentBean.getRowTransactionId());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if (paymentBean.getDocumentIds() != null && !paymentBean.getDocumentIds().isEmpty())
			paymentBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,paymentBean.getDelayReason(),null, paymentBean);
		return (PaymentBean)flowableBaseService.taskDataEntry(task, paymentBean);
	}

	/**
	 * This method is used to Complete IBD Acceptance Testing.
	 *
	 * @param taskId
	 * @param completeAcceptanceTestingBean
	 * @return CompleteAcceptanceTestingBean
	 * @throws TclCommonException
	 * @author diksha garg completeAcceptanceTesting
	 */
	@Transactional(readOnly = false)
	public CompleteAcceptanceTestingBean completeAcceptanceTesting(
			CompleteAcceptanceTestingBean completeAcceptanceTestingBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(completeAcceptanceTestingBean.getTaskId(),completeAcceptanceTestingBean.getWfTaskId());
		validateInputs(task, completeAcceptanceTestingBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,completeAcceptanceTestingBean.getDelayReason(),null, completeAcceptanceTestingBean);
		return (CompleteAcceptanceTestingBean) flowableBaseService.taskDataEntry(task, completeAcceptanceTestingBean);
	}

	/**
	 * This method is used to Complete OSP Acceptance Testing.
	 *
	 * @param taskId
	 * @param CompleteOspAcceptanceBean
	 * @return CompleteOspAcceptanceBean
	 * @throws TclCommonException
	 * @author diksha garg completeOspAcceptance
	 */
	@Transactional(readOnly = false)
	public CompleteOspAcceptanceBean completeOspAcceptance(
			CompleteOspAcceptanceBean completeOspAcceptanceBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(completeOspAcceptanceBean.getTaskId(),completeOspAcceptanceBean.getWfTaskId());
        Map<String, Object> wfMap = new HashMap<>();
        wfMap.put("ospAcceptanceStatus", completeOspAcceptanceBean.getOspAcceptanceReport());
        wfMap.put("ibdAcceptanceStatus", completeOspAcceptanceBean.getIbdAcceptanceReport());
		validateInputs(task, completeOspAcceptanceBean);
		Map<String,String> map= new HashMap<>();
		if(completeOspAcceptanceBean.getOspAcceptanceReport() !=null &&completeOspAcceptanceBean.getOspAcceptanceReport().equalsIgnoreCase("Rejected")) {
				map.put("ospAcceptanceFailureReason",completeOspAcceptanceBean.getOSPRemarks());
		}else {
			map.put("ospAcceptanceFailureReason","Accepted");
		}
		if(completeOspAcceptanceBean.getIbdAcceptanceReport() !=null && completeOspAcceptanceBean.getIbdAcceptanceReport().equalsIgnoreCase("Rejected")) {
				map.put("ibdAcceptanceFailureReason",completeOspAcceptanceBean.getIBDRemarks());
		}else {
			map.put("ibdAcceptanceFailureReason","Accepted");
		}
		saveIbdAndOspErrorMessage(map,task);
		if (!CollectionUtils.isEmpty(completeOspAcceptanceBean.getDocumentIds())) {
			completeOspAcceptanceBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,completeOspAcceptanceBean.getDelayReason(),null, completeOspAcceptanceBean);
		return (CompleteOspAcceptanceBean) flowableBaseService.taskDataEntry(task, completeOspAcceptanceBean,wfMap);
	}

	private void saveIbdAndOspErrorMessage(Map<String, String> map, Task task) {
		if (!CollectionUtils.isEmpty(map))
			map.entrySet()
					.forEach(entry -> {
						try {
							componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(), entry.getKey(),
									componentAndAttributeService.getErrorMessageDetails(entry.getValue(), "0000"),
									AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getName());
						} catch (TclCommonException e) {
							logger.error("Exception occurred in saveIbdAndOspErrorMessage, {}", e);
						}
					});
	}

	/**
	 * Confirm Access Ring.
	 *
	 * @param taskId
	 * @param confirmAccessRingBean
	 * @return ConfirmAccessRingBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ConfirmAccessRingBean confirmAccessRing( ConfirmAccessRingBean confirmAccessRingBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(confirmAccessRingBean.getTaskId(),confirmAccessRingBean.getWfTaskId());
		validateInputs(task, confirmAccessRingBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,confirmAccessRingBean.getDelayReason(),null, confirmAccessRingBean);
		return (ConfirmAccessRingBean) flowableBaseService.taskDataEntry(task, confirmAccessRingBean);
	}

	/**
	 * This method is used to apply for ROW Permission.
	 * 
	 * @author mayanks
	 * @param taskId
	 * @param rowBean
	 * @return RowBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public RowBeanRequest applyForRowPermission( RowBeanRequest rowBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(rowBean.getTaskId(),rowBean.getWfTaskId());
		validateInputs(task, rowBean);
		

		Map<String, String> atMap = new HashMap<>();
		
		atMap.put("roadAuthorityName", rowBean.getRoadAuthorityName());
		atMap.put("rowOneTimeCharge", String.valueOf(rowBean.getOneTimeCharge()));
		atMap.put("rowRecurringFixedCharge", String.valueOf(rowBean.getRecurringFixedCharge()));
		atMap.put("rowRecurringVariableCharge", String.valueOf(rowBean.getRecurringVariableCharge()));
		atMap.put("rowTaxes", String.valueOf(rowBean.getTaxes()));
		String demandNoteAvailable = StringUtils.trimToEmpty(rowBean.getDemandNoteAvailable());
		atMap.put("demandNoteAvailable", demandNoteAvailable);
		
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		
		if (rowBean.getDocumentIds() != null && !rowBean.getDocumentIds().isEmpty()) {
			rowBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,rowBean.getDelayReason(),null, rowBean);
		
		
		try {
			logger.info("demandNoteAvailable {}",demandNoteAvailable);
			if(demandNoteAvailable.equalsIgnoreCase("No")) {
				Map<String, Object> processVar = new HashMap<>();
				processVar.put(ORDER_CODE,task.getOrderCode());
				processVar.put(SC_ORDER_ID,task.getScOrderId());
				processVar.put(SERVICE_ID,task.getServiceId());
				processVar.put(SERVICE_CODE,task.getServiceCode());
				processVar.put(CITY,task.getCity());
				processVar.put(STATE,task.getState());
				processVar.put(COUNTRY,task.getCountry());
				processVar.put(ORDER_TYPE,task.getOrderType());
				processVar.put(ORDER_CATEGORY,task.getOrderCategory());
				processVar.put(PRODUCT_NAME,task.getServiceType());			
				processVar.put("site_type",task.getSiteType());
				processVar.put(task.getMstTaskDef().getMstActivityDef().getKey() + "_ID",task.getActivity().getId());				
				
				runtimeService.startProcessInstanceByKey("row_exception_workflow", processVar);
			}
		}catch(Exception ee) {
			logger.error("Exception occurred in row_exception_workflow ", ee);
		}
		return (RowBeanRequest) flowableBaseService.taskDataEntry(task, rowBean);
	}
	
	
	@Transactional(readOnly = false)
	public RowBeanRequest applyForRowPermissionException( RowBeanRequest rowBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(rowBean.getTaskId(),rowBean.getWfTaskId());
		validateInputs(task, rowBean);
		

		Map<String, String> atMap = new HashMap<>();
		
		atMap.put("roadAuthorityName", rowBean.getRoadAuthorityName());
		atMap.put("rowOneTimeCharge", String.valueOf(rowBean.getOneTimeCharge()));
		atMap.put("rowRecurringFixedCharge", String.valueOf(rowBean.getRecurringFixedCharge()));
		atMap.put("rowRecurringVariableCharge", String.valueOf(rowBean.getRecurringVariableCharge()));
		atMap.put("rowTaxes", String.valueOf(rowBean.getTaxes()));
		
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		
		if (rowBean.getDocumentIds() != null && !rowBean.getDocumentIds().isEmpty()) {
			rowBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,rowBean.getDelayReason(),null, rowBean);
		
		return (RowBeanRequest) flowableBaseService.taskDataEntry(task, rowBean);
	}

	/**
	 * This method is used to save appointment details.
	 * 
	 * @param task
	 * @param customerAppointmentBean
	 * @return Task
	 */
	private Task saveAppointmentDetails(Task task, CustomerAppointmentBean customerAppointmentBean) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(new Timestamp(DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()).getTime()));
		appointment.setLocalItname(customerAppointmentBean.getLocalContactName());
		appointment.setLocalItEmail(customerAppointmentBean.getLocalContactEMail());
		appointment.setLocalItContactMobile(customerAppointmentBean.getLocalContactNumber());
		appointment.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));	
		appointment.setServiceId(task.getServiceId());
		appointment.setDescription(customerAppointmentBean.getOtherDocument());
		appointment.setMstAppointmentSlot(getMstAppointmentSlotById(customerAppointmentBean.getAppointmentSlot()));
		appointment.setTask(task);
		appointment.setAppointmentType(getMstAppointmentType(task.getMstTaskDef().getKey()));

		Set<AppointmentDocuments> appointmentDocuments = new HashSet<>();
		customerAppointmentBean.getDocumentAttachments().forEach(attachment -> {
			AppointmentDocuments appointmentDocument = new AppointmentDocuments();
			appointmentDocument.setAppointment(appointment);
			appointmentDocument.setMstAppointmentDocuments(mstAppointmentDocumentRepository.findById(attachment).get());
			appointmentDocuments.add(appointmentDocument);
		});
		appointment.setAppointmentDocuments(appointmentDocuments);
		appointmentRepository.save(appointment);
		return task;
	}

	/**
	 * This method is used to get mst appointment type.
	 * 
	 * @author mayanks
	 * @param type
	 */
	public String getMstAppointmentType(String type) {
		if (!type.contains("appointment")) {
			return type;
		}
		String[] app = type.split("appointment");
		String s = app[1];
		return s.substring(1, s.length());
	}

	/**
	 * This method is to get mst appointment slot by id.
	 * 
	 * @author mayanks
	 * @param slotId
	 * @return
	 */
	private MstAppointmentSlots getMstAppointmentSlotById(Integer slotId) {
		return mstAppointmentSlotsRepository.findById(slotId).orElseThrow(
				() -> new TclCommonRuntimeException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_ERROR));
	}

	/**
	 * This method is used to Deliver Mux.
	 *
	 * @param taskId
	 * @param deliverMuxBean
	 * @return DeliverMuxBean
	 * @throws TclCommonException
	 * @author diksha garg deliverMux
	 */
	@Transactional(readOnly = false)
	public DeliverMuxBean deliverMux( DeliverMuxBean deliverMuxBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(deliverMuxBean.getTaskId(),deliverMuxBean.getWfTaskId());
		validateInputs(task, deliverMuxBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,deliverMuxBean.getDelayReason(),null, deliverMuxBean);
		return (DeliverMuxBean) flowableBaseService.taskDataEntry(task, deliverMuxBean);
	}
	
	/**
	 * This private method is used to save field engineer details.
	 * 
	 * @author mayanks
	 * @param task
	 * @param fieldEngineerDetailsBean
	 */
	private void saveFieldEngineer(Task task, FieldEngineerDetails fieldEngineerDetailsBean,String type) {
		FieldEngineer fieldEngineer = new FieldEngineer();
		fieldEngineer.setName(fieldEngineerDetailsBean.getFeName());
		fieldEngineer.setTask(task);
		fieldEngineer.setMobile(fieldEngineerDetailsBean.getFeMobileNumber());
		fieldEngineer.setEmail(fieldEngineerDetailsBean.getFeEmail());
		fieldEngineer.setServiceId(task.getServiceId());
		fieldEngineer.setAppointmentType(getTaskType(type));
		fieldEngineer.setPrimaryEmail(fieldEngineerDetailsBean.getIsPrimary());
		fieldEngineer.setSecondaryname(fieldEngineerDetailsBean.getSecondaryName());
		fieldEngineer.setSecondarymobile(fieldEngineerDetailsBean.getSecondaryContactNumber());
		fieldEngineer.setSecondaryemail(fieldEngineerDetailsBean.getSecondaryEmailId());
		fieldEngineer.setFeType(fieldEngineerDetailsBean.getFeType());
		fieldEngineer.setWorkType(fieldEngineerDetailsBean.getWorkType());
		fieldEngineerRepository.save(fieldEngineer);

	}
	

	/**
	 * This method is used to Create Work Order for Internal Cabling.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param internalCablingDetails
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public void orderForInternalCabling( InternalCablingDetails internalCablingDetails)
			throws TclCommonException {
		logger.info("orderForInternalCabling method invoked::{}", internalCablingDetails.getTaskId());
		Task task = getTaskByIdAndWfTaskId(internalCablingDetails.getTaskId(), internalCablingDetails.getWfTaskId());
		validateInputs(task, internalCablingDetails);
		Map<String, String> map = new HashMap<>();
		Map<String, Object> wfMap = new HashMap<>();
		if (internalCablingDetails.getCablingBy() != null && internalCablingDetails.getTestingBy() != null) {
			map.put("cablingBy", internalCablingDetails.getCablingBy());
			map.put("testingBy", internalCablingDetails.getTestingBy());
			updateCreatedWorkOrderInternatCablingCE(task, internalCablingDetails, wfMap, map);
			updateCreatedWorkOrderInternatCablingPE(task, internalCablingDetails, wfMap, map);

		}
		cableSwappingConditionForMacd(internalCablingDetails, task, wfMap, map);
		updateCreatedWorkOrderSwappingCE(task, internalCablingDetails, wfMap, map);
		updateCreatedWorkOrderSwappingPE(task, internalCablingDetails, wfMap, map);

		/*if (internalCablingDetails.getCablingManagedByCustomer() != null
				&& !internalCablingDetails.getCablingManagedByCustomer().isEmpty()) {
			logger.info("Cable Managed By Customer");
			map.put("cablingManagedByCustomer", internalCablingDetails.getCablingManagedByCustomer());
			wfMap.put("cablingManagedByCustomer", internalCablingDetails.getCablingManagedByCustomer());
		}
		if (internalCablingDetails.getCablingRequiredAtPop() != null
				&& !internalCablingDetails.getCablingRequiredAtPop().isEmpty()) {
			logger.info("Cable Managed By Pop");
			map.put("cablingRequiredAtPop", internalCablingDetails.getCablingRequiredAtPop());
			wfMap.put("cablingRequiredAtPop", internalCablingDetails.getCablingRequiredAtPop());
		}
		if (internalCablingDetails.getCableSwappingRequired() != null
				&& !internalCablingDetails.getCableSwappingRequired().isEmpty()) {
			logger.info("Cable Swapping");
			if ("create-workorder-internal-cabling".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("CS:CE");
				map.put("cableSwappingCERequired", internalCablingDetails.getCableSwappingRequired());
				wfMap.put("cableSwappingCERequired", internalCablingDetails.getCableSwappingRequired());
			} else if ("create-workorder-internal-cabling-pe".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("CS:PE");
				map.put("cableSwappingPERequired", internalCablingDetails.getCableSwappingRequired());
				wfMap.put("cableSwappingPERequired", internalCablingDetails.getCableSwappingRequired());
			}
		}

		if (internalCablingDetails.getWorkOrderRequired() != null
				&& !internalCablingDetails.getWorkOrderRequired().isEmpty()) {
			logger.info("WorkOrder Required");
			if ("create-workorder-internal-cabling".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("WO:CE");
				map.put("workOrderCERequired", internalCablingDetails.getWorkOrderRequired());
				wfMap.put("workOrderCERequired", internalCablingDetails.getWorkOrderRequired());
				if ("Yes".equalsIgnoreCase(internalCablingDetails.getWorkOrderRequired())) {
					logger.info("WO:CE Yes");
					map.put("ceVendorId", internalCablingDetails.getVendorId());
					map.put("ceVendorName", internalCablingDetails.getVendorName());
					map.put("ceVendorAddress", internalCablingDetails.getVendorAddress());
					map.put("ceVendorEmail", internalCablingDetails.getVendorEmailId());
					map.put("ceWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
					map.put("ceWorkOrderDate", internalCablingDetails.getWorkOrderDate().toString());
					map.put("ceScopeofWork", internalCablingDetails.getScopeofWork().toString());
				}
			} else if ("create-workorder-internal-cabling-pe".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("WO:PE");
				map.put("workOrderPERequired", internalCablingDetails.getWorkOrderRequired());
				wfMap.put("workOrderPERequired", internalCablingDetails.getWorkOrderRequired());
				if ("Yes".equalsIgnoreCase(internalCablingDetails.getWorkOrderRequired())) {
					logger.info("WO:PE Yes");
					map.put("peVendorId", internalCablingDetails.getVendorId());
					map.put("peVendorName", internalCablingDetails.getVendorName());
					map.put("peVendorAddress", internalCablingDetails.getVendorAddress());
					map.put("peVendorEmail", internalCablingDetails.getVendorEmailId());
					map.put("peWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
					map.put("peWorkOrderDate", internalCablingDetails.getWorkOrderDate().toString());
					map.put("peScopeofWork", internalCablingDetails.getScopeofWork().toString());
				}
			} else if ("create-workorder-cable-swap".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("WOS:CE");
				map.put("workOrderSwapCERequired", internalCablingDetails.getWorkOrderRequired());
				wfMap.put("workOrderSwapCERequired", internalCablingDetails.getWorkOrderRequired());
				if ("Yes".equalsIgnoreCase(internalCablingDetails.getWorkOrderRequired())) {
					logger.info("WOS:CE Yes");
					map.put("ceSwapVendorId", internalCablingDetails.getVendorId());
					map.put("ceSwapVendorName", internalCablingDetails.getVendorName());
					map.put("ceSwapVendorAddress", internalCablingDetails.getVendorAddress());
					map.put("ceSwapVendorEmail", internalCablingDetails.getVendorEmailId());
					map.put("ceSwapWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
					map.put("ceSwapWorkOrderDate", internalCablingDetails.getWorkOrderDate().toString());
					map.put("ceSwapScopeofWork", internalCablingDetails.getScopeofWork().toString());
				}
			} else if ("create-workorder-cable-swap-pe".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("WOS:PE");
				map.put("workOrderSwapPERequired", internalCablingDetails.getWorkOrderRequired());
				wfMap.put("workOrderSwapPERequired", internalCablingDetails.getWorkOrderRequired());
				if ("Yes".equalsIgnoreCase(internalCablingDetails.getWorkOrderRequired())) {
					logger.info("WOS:PE Yes");
					map.put("peSwapVendorId", internalCablingDetails.getVendorId());
					map.put("peSwapVendorName", internalCablingDetails.getVendorName());
					map.put("peSwapVendorAddress", internalCablingDetails.getVendorAddress());
					map.put("peSwapVendorEmail", internalCablingDetails.getVendorEmailId());
					map.put("peSwapWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
					map.put("peSwapWorkOrderDate", internalCablingDetails.getWorkOrderDate().toString());
					map.put("peSwapScopeofWork", internalCablingDetails.getScopeofWork().toString());
				}
			}
		}*/
		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, internalCablingDetails.getDelayReason(), null,
				internalCablingDetails);
		flowableBaseService.taskDataEntry(task, internalCablingDetails, wfMap);
	}

	/**
	 * @author vivek
	 *
	 * @param task
	 * @param internalCablingDetails
	 * @param wfMap
	 * @param map
	 */
	private void updateCreatedWorkOrderSwappingPE(Task task, InternalCablingDetails internalCablingDetails,
			Map<String, Object> wfMap, Map<String, String> map) {
		if ("create-workorder-cable-swap-pe".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
			logger.info("CS:PE");
			map.put("cablingBy", internalCablingDetails.getCableSwappingBy());

			if ("Vendor".equalsIgnoreCase(internalCablingDetails.getCableSwappingBy())
					|| "TCL".equalsIgnoreCase(internalCablingDetails.getCableSwappingBy())) {
				if (internalCablingDetails.getCableSwappingRequired() != null) {
					map.put("cableSwappingPERequired", internalCablingDetails.getCableSwappingRequired());
				}
				wfMap.put("cableSwappingPERequired", "Yes");

				if (internalCablingDetails.getVendorId() != null && internalCablingDetails.getVendorName() != null) {
					map.put("peSwapVendorId", internalCablingDetails.getVendorId());
					map.put("peSwapVendorName", internalCablingDetails.getVendorName());
					if (internalCablingDetails.getVendorAddress() != null) {
						map.put("peSwapVendorAddress", internalCablingDetails.getVendorAddress());
					}
					if (internalCablingDetails.getVendorEmailId() != null) {
						map.put("peSwapVendorEmail", internalCablingDetails.getVendorEmailId());
					}
					if (internalCablingDetails.getWorkOrderNumber() != null) {
						map.put("peSwapWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
					}
					if (internalCablingDetails.getWorkOrderDate() != null) {
						map.put("peSwapWorkOrderDate", internalCablingDetails.getWorkOrderDate());
					}
					if (internalCablingDetails.getScopeofWork() != null) {
						map.put("peSwapScopeofWork", internalCablingDetails.getScopeofWork());
					}
				}
			} else {
					map.put("cableSwappingPERequired", "No");
					wfMap.put("cableSwappingPERequired", "No");
			}

			if (internalCablingDetails.getFeDetails() != null && !internalCablingDetails.getFeDetails().isEmpty()) {
				internalCablingDetails.getFeDetails().forEach(feDestails -> {
					saveFieldEngineer(task, feDestails, "arrange-field-engineer-cable-swap-pe-man");

				});

			}

		}
	}

	/**
	 * @author vivek
	 *
	 * @param task
	 * @param internalCablingDetails
	 * @param wfMap
	 * @param map
	 */
	private void updateCreatedWorkOrderSwappingCE(Task task, InternalCablingDetails internalCablingDetails,
			Map<String, Object> wfMap, Map<String, String> map) {
		if ("create-workorder-cable-swap".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
			logger.info("CS:CE");
			map.put("cableSwappingBy", internalCablingDetails.getCableSwappingBy());

			if ("Vendor".equalsIgnoreCase(internalCablingDetails.getCableSwappingBy())
					|| "TCL".equalsIgnoreCase(internalCablingDetails.getCableSwappingBy())) {
				map.put("cableSwappingCERequired", "Yes");
				wfMap.put("cableSwappingCERequired", "Yes");
				if (internalCablingDetails.getVendorId() != null && internalCablingDetails.getVendorName() != null) {

					map.put("ceSwapVendorId", internalCablingDetails.getVendorId());
					map.put("ceSwapVendorName", internalCablingDetails.getVendorName());
					if (internalCablingDetails.getVendorAddress() != null) {
						map.put("ceSwapVendorAddress", internalCablingDetails.getVendorAddress());
					}
					if (internalCablingDetails.getVendorEmailId() != null) {
						map.put("ceSwapVendorEmail", internalCablingDetails.getVendorEmailId());
					}
					if (internalCablingDetails.getWorkOrderNumber() != null) {
						map.put("ceSwapWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
					}
					if (internalCablingDetails.getWorkOrderDate() != null) {
						map.put("ceSwapWorkOrderDate", internalCablingDetails.getWorkOrderDate());
					}
					if(internalCablingDetails.getScopeofWork()!=null) {
					map.put("ceSwapScopeofWork", internalCablingDetails.getScopeofWork());
					}
				}
			} else {
				map.put("cableSwappingCERequired", "No");
				wfMap.put("cableSwappingCERequired", "No");
			}
			
			if (internalCablingDetails.getFeDetails() != null && !internalCablingDetails.getFeDetails().isEmpty()) {
				internalCablingDetails.getFeDetails().forEach(feDestails -> {
					saveFieldEngineer(task, feDestails, "arrange-field-engineer-cable-swap-ce-man");

				});

			}
		

		}		
	}

	/**
	 * @author vivek
	 *
	 * @param internalCablingDetails
	 * @param task
	 */
	private void cableSwappingConditionForMacd(InternalCablingDetails internalCablingDetails, Task task,Map<String, Object> wfMap, Map<String, String> map) {
		if (internalCablingDetails.getCableSwappingRequired() != null) {

			if ("create-workorder-internal-cabling".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("CS:CE");
				map.put("cableSwappingCEByCustomer", internalCablingDetails.getCableSwappingByCustomer());

				if (internalCablingDetails.getCableSwappingRequired().equalsIgnoreCase("yes")
						&& internalCablingDetails.getCableSwappingByCustomer().equalsIgnoreCase("No")) {
					logger.info("CS:CE Yes");

					map.put("cableSwappingCERequired", "Yes");
					wfMap.put("cableSwappingCERequired", "Yes");
				} else {
					logger.info("CS:CE No");

					map.put("cableSwappingCERequired", internalCablingDetails.getCableSwappingRequired());
					wfMap.put("cableSwappingCERequired", "No");
				}

			} else if ("create-workorder-internal-cabling-pe".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				logger.info("CS:PE");
				map.put("cableSwappingPEByCustomer", internalCablingDetails.getCableSwappingByCustomer());

				if (internalCablingDetails.getCableSwappingRequired()!=null &&internalCablingDetails.getCableSwappingRequired().equalsIgnoreCase("yes")
						&& internalCablingDetails.getCableSwappingByCustomer().equalsIgnoreCase("No")) {
					logger.info("CS:PE Yes");

					map.put("cableSwappingPERequired", "Yes");
					wfMap.put("cableSwappingPERequired", "Yes");

				} else {
					logger.info("CS:PE No");

					map.put("cableSwappingPERequired","No");
					wfMap.put("cableSwappingPERequired", "No");
				}

			}

		}
		
		
	}

	/**
	 * @author vivek
	 *
	 * @param task
	 * @param internalCablingDetails
	 * @param wfMap
	 * @param map
	 */
	private void updateCreatedWorkOrderInternatCablingPE(Task task, InternalCablingDetails internalCablingDetails,
			Map<String, Object> wfMap, Map<String, String> map) {
		if ("create-workorder-internal-cabling-pe".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
			if ("None".equalsIgnoreCase(internalCablingDetails.getCablingBy())
					|| "Customer".equalsIgnoreCase(internalCablingDetails.getCablingBy())) {
				wfMap.put("cablingRequiredAtPop", "No");

			}

			else {
				wfMap.put("cablingRequiredAtPop", "Yes");

				logger.info("WO:PE Yes");
				map.put("peVendorId", internalCablingDetails.getVendorId());
				map.put("peVendorName", internalCablingDetails.getVendorName());
				if (internalCablingDetails.getVendorAddress() != null) {
					map.put("peVendorAddress", internalCablingDetails.getVendorAddress());
				}

				if (internalCablingDetails.getVendorEmailId() != null) {

					map.put("peVendorEmail", internalCablingDetails.getVendorEmailId());
				}
				if (internalCablingDetails.getWorkOrderNumber() != null) {
					map.put("peWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
				}
				if (internalCablingDetails.getWorkOrderDate() != null) {
					map.put("peWorkOrderDate", internalCablingDetails.getWorkOrderDate());
				}
				map.put("peScopeofWork", internalCablingDetails.getScopeofWork());

			}
			if ("None".equalsIgnoreCase(internalCablingDetails.getTestingBy())
					|| "Customer".equalsIgnoreCase(internalCablingDetails.getTestingBy())) {

			} else {
				logger.info("WO:PE Testing yes Yes");

			}

			if (internalCablingDetails.getFeDetails() != null && !internalCablingDetails.getFeDetails().isEmpty()) {
				internalCablingDetails.getFeDetails().forEach(feDestails -> {
					saveFieldEngineer(task, feDestails, "arrange-field-engineer-cable-extension-pe");

				});

			}
		}
	}

	/**
	 * @author vivek
	 *
	 * @param task
	 * @param internalCablingDetails
	 * @param wfMap
	 * @param map
	 */
	private void updateCreatedWorkOrderInternatCablingCE(Task task, InternalCablingDetails internalCablingDetails,
			Map<String, Object> wfMap, Map<String, String> map) {
		if ("create-workorder-internal-cabling".equalsIgnoreCase(task.getMstTaskDef().getKey()) || "create-workorder-cc-internal-cabling".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
			if (internalCablingDetails.getScopeofWork() != null) {
				map.put("ceScopeofWork", internalCablingDetails.getScopeofWork());
			}

			if ("None".equalsIgnoreCase(internalCablingDetails.getCablingBy())
					|| "Customer".equalsIgnoreCase(internalCablingDetails.getCablingBy())) {
				wfMap.put("cablingManagedByCustomer", "Yes");

			}

			else {
				wfMap.put("cablingManagedByCustomer", "No");

				if (internalCablingDetails.getVendorId() != null && internalCablingDetails.getVendorName() != null) {
					logger.info("WO:CE Yes");
					map.put("ceVendorId", internalCablingDetails.getVendorId());
					map.put("ceVendorName", internalCablingDetails.getVendorName());
					map.put("ceVendorAddress", internalCablingDetails.getVendorAddress());
					map.put("ceVendorEmail", internalCablingDetails.getVendorEmailId());
					if(internalCablingDetails.getWorkOrderNumber()!=null) {
					map.put("ceWorkOrderNumber", internalCablingDetails.getWorkOrderNumber());
					}
					if(internalCablingDetails.getWorkOrderDate()!=null) {
					map.put("ceWorkOrderDate", internalCablingDetails.getWorkOrderDate());
					}

				}

			}

			if ("None".equalsIgnoreCase(internalCablingDetails.getTestingBy())
					|| "Customer".equalsIgnoreCase(internalCablingDetails.getTestingBy())) {

			} else {
				logger.info("WO:CE Testing yes Yes");

			}
			if (internalCablingDetails.getFeDetails() != null && !internalCablingDetails.getFeDetails().isEmpty()) {
				internalCablingDetails.getFeDetails().forEach(feDestails -> {
					saveFieldEngineer(task, feDestails, "arrange-field-engineer-cable-extension-ce");

				});

			}

		}
	}

	/**
	 * This method is used to Complete Internal Cabling.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param internalCablingRequest
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public void completeInternalCabling( InternalCablingRequest internalCablingRequest)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(internalCablingRequest.getTaskId(),internalCablingRequest.getWfTaskId());
		validateInputs(task, internalCablingRequest);
		Map<String, Object> wfMap = new HashMap<>();
		if("complete-cc-internal-cabling-ce-man".equalsIgnoreCase(task.getMstTaskDef().getKey())){
			logger.info("Complete CC Internal Cabling CE MAN::{}",task.getServiceId());
			Map<String, String> compAttrMap = new HashMap<>();
			Map<String, String> serviceAttrMap = new HashMap<>();
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					task.getServiceId(), AttributeConstants.COMPONENT_LM, "A");
			String isBothSiteSameInterface=StringUtils.trimToEmpty(scComponentAttributesAMap.get("isBothSiteSameInterface"));
			if(!isBothSiteSameInterface.isEmpty()){
				logger.info("isBothSiteSameInterface exists");
				if("Yes".equalsIgnoreCase(isBothSiteSameInterface)){
					logger.info("isBothSiteSameInterface::{}",isBothSiteSameInterface);
					compAttrMap.put("rackDetail", internalCablingRequest.getRackDetail());
					compAttrMap.put("typeOfRack", internalCablingRequest.getTypeOfRack());
					compAttrMap.put("connectivityDetail", internalCablingRequest.getConnectivityDetail());
					componentAndAttributeService.updateAttributes(task.getServiceId(),compAttrMap,AttributeConstants.COMPONENT_LM,"A");
					compAttrMap.put("rackDetail", internalCablingRequest.getRackDetailB());
					compAttrMap.put("typeOfRack", internalCablingRequest.getTypeOfRackB());
					compAttrMap.put("connectivityDetail", internalCablingRequest.getConnectivityDetailB());
					componentAndAttributeService.updateAttributes(task.getServiceId(),compAttrMap,AttributeConstants.COMPONENT_LM,"B");
				}else if("No".equalsIgnoreCase(isBothSiteSameInterface)){
					logger.info("isBothSiteSameInterface::{},{}",isBothSiteSameInterface,task.getSiteType());
					compAttrMap.put("rackDetail", internalCablingRequest.getRackDetail());
					compAttrMap.put("typeOfRack", internalCablingRequest.getTypeOfRack());
					compAttrMap.put("connectivityDetail", internalCablingRequest.getConnectivityDetail());
					componentAndAttributeService.updateAttributes(task.getServiceId(),compAttrMap,AttributeConstants.COMPONENT_LM,task.getSiteType());
				}
			}
			serviceAttrMap.put("pathAvailable", internalCablingRequest.getPathAvailable());
			serviceAttrMap.put("fiberAvailable", internalCablingRequest.getFiberAvailable());
			serviceAttrMap.put("typeOfIBDWork", internalCablingRequest.getTypeOfIBDWork());
			serviceAttrMap.put("surveyRemarks", internalCablingRequest.getSurveyRemarks());
			if("Yes".equalsIgnoreCase(internalCablingRequest.getPathAvailable())){
				logger.info("Path Available::Yes");
				wfMap.put("pathAvailable",true);
			}else if("No".equalsIgnoreCase(internalCablingRequest.getPathAvailable())){
				logger.info("Path Available::No");
				wfMap.put("pathAvailable",false);
			}
			wfMap.put("fiberAvailable",internalCablingRequest.getFiberAvailable());
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceAttrMap);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,internalCablingRequest.getDelayReason(),null, internalCablingRequest);
		
		wfMap.put("appointmentAction", "close");
		wfMap.put("action", "close");
		flowableBaseService.taskDataEntry(task, internalCablingRequest,wfMap);
	}

	/**
	 * This method is used to Confirm Internal Cabling completion.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param completionStatus
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public void confirmInternalCablingCompletion( InternalCablingCompletionStatus completionStatus)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(completionStatus.getTaskId(),completionStatus.getWfTaskId());
		validateInputs(task, completionStatus);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,completionStatus.getDelayReason(),null, completionStatus);
		flowableBaseService.taskDataEntry(task, completionStatus);
	}

	/**
	 * This method is used to Confirm Last Mile Acceptance
	 *
	 * @param taskId
	 * @param confirmLmAcceptanceBean
	 * @return ConfirmLmAcceptanceBean
	 * @throws TclCommonException
	 * @author diksha garg confirmLmAcceptance
	 */
	@Transactional(readOnly = false)
	public ConfirmLmAcceptanceBean confirmLmAcceptance( ConfirmLmAcceptanceBean confirmLmAcceptanceBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(confirmLmAcceptanceBean.getTaskId(),confirmLmAcceptanceBean.getWfTaskId());
		validateInputs(task, confirmLmAcceptanceBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,confirmLmAcceptanceBean.getDelayReason(),null, confirmLmAcceptanceBean);
		
		Map<String, String> map = new HashMap<>();
		
		if (StringUtils.isNotBlank(confirmLmAcceptanceBean.getSupplierBillStartDate())){
			map.put("offnetSupplierBillStartDate", confirmLmAcceptanceBean.getSupplierBillStartDate());
		}
		if (StringUtils.isNotBlank(confirmLmAcceptanceBean.getLastMileSla())){
			map.put("lastMileSla", confirmLmAcceptanceBean.getLastMileSla());
		}
		if (StringUtils.isNotBlank(confirmLmAcceptanceBean.getLastMileSlaDeviation())){
			map.put("lastMileSlaDeviation", confirmLmAcceptanceBean.getLastMileSlaDeviation());
		}
		if (StringUtils.isNotBlank(confirmLmAcceptanceBean.getLastMileSlaDeviationReason())){
			map.put("lastMileSlaDeviationReason", confirmLmAcceptanceBean.getLastMileSlaDeviationReason());
		}
		if (Objects.nonNull(confirmLmAcceptanceBean.getSupplierBsoCircuitId()) && StringUtils.isNotBlank(confirmLmAcceptanceBean.getSupplierBsoCircuitId())){
			map.put("bsoCircuitId", confirmLmAcceptanceBean.getSupplierBsoCircuitId());
		}
		if(!CollectionUtils.isEmpty(confirmLmAcceptanceBean.getDocumentIds())){
			confirmLmAcceptanceBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(),map,AttributeConstants.COMPONENT_LM,task.getSiteType());
		
		return (ConfirmLmAcceptanceBean) flowableBaseService.taskDataEntry(task, confirmLmAcceptanceBean);
	}

	/**
	 * This method is used for Mast Installation Plan
	 *
	 * @param taskId
	 * @param mastInstallationPlanBean
	 * @return MastInstallationPlanBean
	 * @throws TclCommonException
	 * @author Yogesh mastInstallationPlan
	 */
	@Transactional(readOnly = false)
	public MastInstallationPlanBean mastInstallationPlan(
			MastInstallationPlanBean mastInstallationPlanBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(mastInstallationPlanBean.getTaskId(),mastInstallationPlanBean.getWfTaskId());
		validateInputs(task, mastInstallationPlanBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,mastInstallationPlanBean.getDelayReason(),null, mastInstallationPlanBean);
		return (MastInstallationPlanBean) flowableBaseService.taskDataEntry(task, mastInstallationPlanBean);
	}

	/**
	 * This method is used for Integrating Mux
	 * 
	 * @author Mayank S
	 * @param taskId
	 * @param integrateMuxBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public IntegrateMuxBean integrateMux( IntegrateMuxBean integrateMuxBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(integrateMuxBean.getTaskId(),integrateMuxBean.getWfTaskId());
		validateInputs(task, integrateMuxBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,integrateMuxBean.getDelayReason(),null, integrateMuxBean);
		return (IntegrateMuxBean) flowableBaseService.taskDataEntry(task, integrateMuxBean);
	}

	/**
	 * This method is used to configure Mux
	 * 
	 * @author Mayank S
	 * @param taskId
	 * @param configureMuxBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ConfigureMuxBean configureMux( ConfigureMuxBean configureMuxBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(configureMuxBean.getTaskId(),configureMuxBean.getWfTaskId());
		validateInputs(task, configureMuxBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,configureMuxBean.getDelayReason(),null, configureMuxBean);
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("isMuxIPAvailable", true);		
		return (ConfigureMuxBean) flowableBaseService.taskDataEntry(task, configureMuxBean);
	}

	/**
	 * This method is used to create Inventory record.
	 *
	 * @author Vishesh Awasthi
	 * @param taskId
	 * @param createInventoryRecord
	 * @return CreateInventoryRecord
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public CreateInventoryRecord createInventoryRecord( CreateInventoryRecord createInventoryRecord)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(createInventoryRecord.getTaskId(),createInventoryRecord.getWfTaskId());
		validateInputs(task, createInventoryRecord);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("ospInventoryRecordCreatedDate", String.valueOf(createInventoryRecord.getDateOfRecord()));
		atMap.put("eorDetails", createInventoryRecord.getEorDetails());
		atMap.put("endMuxNodeIp", createInventoryRecord.getEndMuxNodeIp());
		atMap.put("endMuxNodeName", createInventoryRecord.getEndMuxNodeName());
		atMap.put("endMuxNodePort", createInventoryRecord.getEndMuxNodePort());
		atMap.put("gisId", createInventoryRecord.getGisId());
		atMap.put("peEndPhysicalPort", createInventoryRecord.getPeEndPhysicalPort());
		atMap.put("isPEInternalCablingRequired", createInventoryRecord.getIsPEInternalCablingRequired());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		Map<String, Object> fMap = new HashMap<>();
		if (StringUtils.isNotEmpty(createInventoryRecord.getIsPEInternalCablingRequired()) &&
				createInventoryRecord.getIsPEInternalCablingRequired().equalsIgnoreCase("yes"))
			fMap.put("isPEInternalCablingRequired", true);
		if(CommonConstants.GVPN.equalsIgnoreCase(task.getScServiceDetail().getErfPrdCatalogProductName())
				&& task.getScServiceDetail()!=null && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getMultiVrfSolution())) {
			copyMasterVrfAttributesToSlave(task.getScServiceDetail(),task.getSiteType(),atMap);
		}
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, createInventoryRecord.getDelayReason(), null, createInventoryRecord);
		return (CreateInventoryRecord) flowableBaseService.taskDataEntry(task, createInventoryRecord, fMap);
	}

	/**
	 * This method is used to raise Planned Event
	 * 
	 * @author Vishesh Awasthi
	 * @param taskId
	 * @param plannedEventBean
	 * @return
	 * @throws TclCommonException
	 */

	@Transactional(readOnly = false)
	public PlannedEventBean raisePlannedEvent( PlannedEventBean plannedEventBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(plannedEventBean.getTaskId(),plannedEventBean.getWfTaskId());
		validateInputs(task, plannedEventBean);
        Map<String, String> atMap = new HashMap<>();
        atMap.put("plannedEventId", plannedEventBean.getPlannedEventId());
        atMap.put("plannedEventStartTime", plannedEventBean.getPeStartDateAndTime());
        atMap.put("plannedEventEndTime", plannedEventBean.getPeEndDateAndTime());
        componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,plannedEventBean.getDelayReason(),null, plannedEventBean);
		return (PlannedEventBean) flowableBaseService.taskDataEntry(task, plannedEventBean);
	}
	
	/**
	 * This method is used for prowCostApproval
	 * 
	 * @author vivek 
	 * @param taskId
	 * @param costApproval
	 * @return
	 * @throws TclCommonException
	 */

	
	@Transactional(readOnly = false)
	public ProwCostApproval prowCostApproval( ProwCostApproval costApproval) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(costApproval.getTaskId(),costApproval.getWfTaskId());
		validateInputs(task, costApproval);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("pRowCostApproved", costApproval.getpRowCostApproved());
		atMap.put("costApprovalRemarks", costApproval.getCostApprovalRemarks());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, costApproval.getDelayReason(), null, costApproval);
		return (ProwCostApproval) flowableBaseService.taskDataEntry(task, costApproval);
	}

	public Object updateTentativeDate(Task task, Object detailBean, Map<String, Object> wfMap, Date tentativeDate)
			throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		saveTaskData(beanData, task);
		task.setDuedate(new Timestamp(tentativeDate.getTime()));
		taskRepository.save(task);
		return detailBean;
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
	
	/**
	 *
	 * @param taskId
	 * @param additionalTechnicalDetailsBean
	 * @return AdditionalTechnicalDetailsBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public AdditionalTechnicalDetailsBean saveAdditionalTechnicalDetails(
			AdditionalTechnicalDetailsBean additionalTechnicalDetailsBean) throws TclCommonException {
		logger.info("Inside saveAdditionalTechnicalDetails method with request body : {}", additionalTechnicalDetailsBean);
		Task task = getTaskByIdAndWfTaskId(additionalTechnicalDetailsBean.getTaskId(), additionalTechnicalDetailsBean.getWfTaskId());
		try {
			Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository.findById(task.getServiceId());

			if (optionalServiceDetails.isPresent()) {

				ScServiceDetail serviceDetails = optionalServiceDetails.get();
				String customeName = null;
				if (additionalTechnicalDetailsBean.getProgramName() == null || org.apache.commons.lang3.StringUtils
						.isBlank(additionalTechnicalDetailsBean.getProgramName())) {

					customeName = splitCustomerName(serviceDetails.getScOrder().getErfCustCustomerName());
				} else {
					customeName = splitCustomerName63(serviceDetails.getScOrder().getErfCustCustomerName());

				}
				

				String vpnName = splitProjectName(customeName, additionalTechnicalDetailsBean.getProgramName());
				serviceDetails.setVpnSolutionId(vpnName);
				if(Objects.nonNull(additionalTechnicalDetailsBean.getPrimarySecondary()) && !additionalTechnicalDetailsBean.getPrimarySecondary().isEmpty()){
					serviceDetails.setPrimarySecondary(additionalTechnicalDetailsBean.getPrimarySecondary());
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getPrisecLink()) && !additionalTechnicalDetailsBean.getPrisecLink().isEmpty()){
					serviceDetails.setPriSecServiceLink(additionalTechnicalDetailsBean.getPrisecLink());
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getProductOfferingName()) && !additionalTechnicalDetailsBean.getProductOfferingName().isEmpty()){
					serviceDetails.setErfPrdCatalogOfferingName(additionalTechnicalDetailsBean.getProductOfferingName());
				}
				scServiceDetailRepository.save(serviceDetails);

				if(CommonConstants.GVPN.equalsIgnoreCase(serviceDetails.getErfPrdCatalogProductName())
						&& serviceDetails.getMultiVrfSolution()!=null && CommonConstants.Y.equalsIgnoreCase(serviceDetails.getMultiVrfSolution())){
					//Copy master service detail to slave
					copyMasterVrfDetailsToSlave(serviceDetails);
				}
				
				if(Objects.nonNull(additionalTechnicalDetailsBean.getLanIpAddress()) && !additionalTechnicalDetailsBean.getLanIpAddress().isEmpty()){
					String[] lanIps=additionalTechnicalDetailsBean.getLanIpAddress().split(",");
					logger.info("LanIp exists::{}",lanIps.length);
					List<AceIPMapping> aceIpMappingList= new ArrayList<>();
					for(String lanIp:lanIps){
						List<AceIPMapping> existingAceIPMappingList=aceIPMappingRepository.findByScServiceDetail_IdAndAceIp(serviceDetails.getId(),lanIp);
						if((Objects.isNull(existingAceIPMappingList) || existingAceIPMappingList.isEmpty()) && lanIp.contains(".") && lanIp.contains("/")){
							logger.info("LanIp not exists::{}",lanIp);
								AceIPMapping aceIPMapping= new AceIPMapping();
								aceIPMapping.setScServiceDetail(serviceDetails);
								aceIPMapping.setAceIp(lanIp);
								aceIpMappingList.add(aceIPMapping);
						}
					}
					if(!aceIpMappingList.isEmpty()){
						logger.info("AceIpMapping exists");
						aceIPMappingRepository.saveAll(aceIpMappingList);
					}
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getCosDetails()) && !additionalTechnicalDetailsBean.getCosDetails().isEmpty()){
			        logger.info("saveCosAttributeDetails for serviceId:{}",serviceDetails.getUuid());

					saveCosAttributeDetails(serviceDetails,additionalTechnicalDetailsBean.getCosDetails());
				}
			}
		} catch (Exception e) {
			logger.error("update vpn solution fix:{}", e);
		}
		
		
		String jsonString = Utils.convertObjectToJson(additionalTechnicalDetailsBean);
		Map<String, String> atMap = conductAdditionalTechBeanToMap(jsonString); 
		String wanIpProvidedBy = org.apache.commons.lang3.StringUtils.trimToEmpty(additionalTechnicalDetailsBean.getWanIpProvidedBy());
		if(Objects.nonNull(wanIpProvidedBy)) {
			Map<String, String> serviceMap = new HashMap<>();
			serviceMap.put("WAN IP Provided By", wanIpProvidedBy);
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceMap, "VPN Port");
		}
		if("customer".equalsIgnoreCase(wanIpProvidedBy) ) {
			atMap.put("wanIpProvidedByCust", "Yes");
			additionalTechnicalDetailsBean.setWanIpProvidedByCust("Yes");
		}else {
			atMap.put("wanIpProvidedByCust", "No");
			atMap.put("wanIpAddress", null);
			additionalTechnicalDetailsBean.setWanIpProvidedByCust("No");
		}

		if(additionalTechnicalDetailsBean.getChangedVRFName()!=null && !additionalTechnicalDetailsBean.getChangedVRFName().isEmpty()){
			atMap.put("changedVRFName", additionalTechnicalDetailsBean.getChangedVRFName());
		}

		if(additionalTechnicalDetailsBean.getLanPoolRoutingNeeded()!=null) {
			atMap.put("lanPoolRoutingNeeded", additionalTechnicalDetailsBean.getLanPoolRoutingNeeded());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		Map<String, String> siteTopologyMap = new HashMap<>(); 
		siteTopologyMap.put("Site Type", additionalTechnicalDetailsBean.getSiteType());
		siteTopologyMap.put("VPN Topology", additionalTechnicalDetailsBean.getVpnTopology());
		componentAndAttributeService.updateSiteTopologyServiceAttributes(task.getServiceId(),siteTopologyMap);
		validateInputs(task, additionalTechnicalDetailsBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,additionalTechnicalDetailsBean.getDelayReason(),null, additionalTechnicalDetailsBean);
		Map<String, Object> flowableMap = new HashMap<>();
		flowableMap.put("isLagBundleOrder", false);
		ScServiceAttribute isLagBundleOrderAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(task.getServiceId(),"isLagBundleOrder");
		if(isLagBundleOrderAttribute!=null && isLagBundleOrderAttribute.getAttributeValue()!=null && !isLagBundleOrderAttribute.getAttributeValue().isEmpty()
				&& isLagBundleOrderAttribute.getAttributeValue().equalsIgnoreCase("Yes")) {
			flowableMap.put("isLagBundleOrder", true);
		}
		return (AdditionalTechnicalDetailsBean) flowableBaseService.taskDataEntry(task, additionalTechnicalDetailsBean,flowableMap);
	}
	

	protected String splitCustomerName(String customerName) {
		StringBuilder buffer = new StringBuilder();

		AtomicInteger atomicInteger = new AtomicInteger(0);
		if(customerName.length()<=3) {
			return customerName.replaceAll(" ", "_");
		}else {
			List<String> customerarr = Pattern.compile(" ").splitAsStream(customerName).limit(3)
					.collect(Collectors.toList());

			if(customerarr.size()==1){

				buffer.append(customerarr.get(0).length()<=18 ? customerarr.get(0) : customerarr.get(0).substring(0,18));
			}
			else if(customerarr.size()==2) {
				buffer.append(customerName.length()<=18 ? customerName :customerName.substring(0,18));

			}
			else {

				customerarr.forEach(cust -> {
					atomicInteger.incrementAndGet();

					if (atomicInteger.get() == 3) {
						buffer.append(cust.length() >= 4 ? (cust.substring(0, 4) + " ") : cust+" ");
					} else {
						if (cust.length() > 7) {
							buffer.append(cust.substring(0, 6) + " ");

						} else {
							buffer.append(cust + " ");
						}
					}

				});
			}
			return buffer.toString().trim().replaceAll(" ", "_");
		}
	}
	
	protected String splitCustomerName63(String customerName) {
		StringBuilder buffer = new StringBuilder();

		AtomicInteger atomicInteger = new AtomicInteger(0);
		if (customerName.length() <= 3) {
			buffer.append(customerName.replaceAll(" ", "_"));
		} else {
			List<String> customerarr = Pattern.compile(" ").splitAsStream(customerName).limit(2)
					.collect(Collectors.toList());

			if (customerarr.size() == 1) {

				buffer.append(
						customerarr.get(0).length() <= 10 ? customerarr.get(0) : customerarr.get(0).substring(0, 10));
			} else {

				customerarr.forEach(cust -> {
					atomicInteger.incrementAndGet();

					if (atomicInteger.get() == 1) {
						buffer.append(cust.length() >= 6 ? (cust.substring(0, 6) + " ") : cust + " ");
					} else if (atomicInteger.get() == 2) {
						buffer.append(cust.length() >= 3 ? (cust.substring(0, 3) + " ") : cust + " ");

					}

				});
			}
		}
		return buffer.toString().trim().replaceAll(" ", "_");
	}
	
	
	
	//6,3 only if project na and 4,2
	
	protected String splitProjectName(String customerName,String projectName) {

		if(projectName==null || projectName.isEmpty()){
			return customerName;
		}

		StringBuilder buffer = new StringBuilder();
		AtomicInteger atomicInteger = new AtomicInteger(0);

		buffer.append(customerName + "_");

		if(projectName.length()<=4){
			buffer.append(projectName.replaceAll(" ", "_"));
		}
		else{
			List<String> projectArr = Pattern.compile(" ").splitAsStream(projectName).limit(3)
					.collect(Collectors.toList());

			projectArr.forEach(project->{
				atomicInteger.incrementAndGet();

				if(atomicInteger.get()==1){
					buffer.append(project.length()>=4 ? project.substring(0,4) : project).append("_");
				}
				else if(atomicInteger.get()==2){
					buffer.append(project.length()>=2 ? project.substring(0,2) : project);
				}
			});
		}

		return buffer.toString().trim().replaceAll(" ", "_");
	}

	/**This method returns the list of ace ips
	 * @param taskId
	 * @param wfTaskId 
	 * @return
	 */
	@Transactional
	public List<String> getAceIps(Integer taskId,String wfTaskId) {
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		if(null!=task) {
		List<AceIPMapping> aceIPMappingList = aceIPMappingRepository.findAllByScServiceDetail_Id(task.getServiceId());
		if(null!=aceIPMappingList) {
			return aceIPMappingList.stream().map(aceIP -> aceIP.getAceIp()).collect(Collectors.toList());	
		}
		}
		return new ArrayList<>();
	}
	
	
	@Transactional
	public Set<LocationUploadValidationBean> processAceIps(Integer taskId,String wfTaskId, MultipartFile file) {
		DataFormatter objDefaultFormat = new DataFormatter();
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		Set<LocationUploadValidationBean> validatorBean = new HashSet<>();
		try {
			if (null != file && null != task) {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
			List<String> aceIpList = new ArrayList<>();
			for (Sheet sheet : workbook) {
				for (int i = 0; i <= getLastRowWithData(sheet); i++) {
					Cell aceIpCell = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
					FormulaEvaluator aceValuator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
					aceValuator.evaluate(aceIpCell);
					String aceIpString = StringUtils.trimToEmpty((objDefaultFormat.formatCellValue(aceIpCell, aceValuator)));

						if (aceIpCell != null) {
						String aceIp = StringUtils.replace(aceIpString, "\\s", "");

						aceIpList.add(aceIp);
					} else {
						if (!isRowBlank(sheet.getRow(i))) {
							List<LocationValidationColumnBean> columnValidationBeanList = new ArrayList<>();
							LocationUploadValidationBean eachRowFullValidation = new LocationUploadValidationBean();
							eachRowFullValidation
									.setRowDetails("Row " + sheet.getRow(i).getRowNum());
							if (isCellBlank(aceIpCell) || isCellEmpty(aceIpCell) || null == aceIpCell) {
								LocationValidationColumnBean columnBeanCountry = new LocationValidationColumnBean();
								columnBeanCountry.setColumnName("ACE IP");
								columnBeanCountry.setErrorMessage("ACE IP ADDRESSES CANT BE EMPTY!");
								columnValidationBeanList.add(columnBeanCountry);
							}
							eachRowFullValidation.setColumns(columnValidationBeanList);
							validatorBean.add(eachRowFullValidation);
						} else {
							logger.info("Full row is blank/Invalid input and the row number is {}", sheet.getRow(i));
						}
					}
				}
			}

			if(null!=aceIpList && aceIpList.size()>0 && (null==validatorBean || validatorBean.size()==0)) {
				saveAceIps(task.getServiceId(), aceIpList);
			}
			}
		}catch(Exception e) {
			logger.error("Exception while saving processAceIps {}", e);
		}
		return validatorBean;
	}
	
	

	private void saveAceIps(Integer serviceId, List<String> aceIpList) {
		try {
			Optional<ScServiceDetail> serviceDetail = scServiceDetailRepository.findById(serviceId);
			if(serviceDetail.isPresent()) {
				List<AceIPMapping> aceIPMappingList = aceIPMappingRepository.findAllByScServiceDetail_Id(serviceId);
				if(!aceIPMappingList.isEmpty()) {
					aceIPMappingRepository.deleteByScServiceDetail_Id(serviceId);
				}
				List<AceIPMapping> aceIpMappingList = aceIpList.stream().map(aceIp -> new AceIPMapping(serviceDetail.get(),aceIp)).collect(Collectors.toList());
				aceIPMappingRepository.saveAll(aceIpMappingList);
			}
			logger.info("Ace IP Mapping entries are saved successfully!");
		}catch(Exception e) {
			logger.error("Exception while saving saveAceIps {}", e);
		}
	}

	/**
	 * Method to convert conductSiteBean To Map
	 *
	 * @param jsonString
	 * @throws TclCommonException
	 */
	private static Map<String, String> conductAdditionalTechBeanToMap(String jsonString) throws TclCommonException {
		Map<String, String> atMap = new HashMap<>();
		try {
			JSONParser parser = new JSONParser();
			JSONObject mainObject = (JSONObject) parser.parse(jsonString);
			@SuppressWarnings("unchecked")
			Set<String> setKeys = mainObject.keySet();
			setKeys.stream().forEach(eachKey -> {
			
				if (!eachKey.equalsIgnoreCase("cosDetails") && !eachKey.equalsIgnoreCase("wfTaskId") && !eachKey.equalsIgnoreCase("taskId")) {
					String eachValue = String.valueOf(mainObject.get(eachKey));
				
					atMap.put(eachKey, StringUtils.trimToEmpty(eachValue));
				}
			});

		} catch (Exception e) {
			throw new TclCommonException("Exception occured while conductAdditionalTechBeanToMap {} ", e);
		}
		return atMap;
	}

	
	public int getLastRowWithData(Sheet currentSheet) {
		int rowCount = 0;
		Iterator<Row> iter = currentSheet.rowIterator();

		while (iter.hasNext()) {
			Row r = iter.next();
			if (!this.isRowBlank(r)) {
				rowCount = r.getRowNum();
			}
		}

		return rowCount;
	}
	
	public boolean isRowBlank(Row r) {
		boolean ret = true;

		/*
		 * If a row is null, it must be blank.
		 */
		if (r != null) {
			Iterator<Cell> cellIter = r.cellIterator();
			/*
			 * Iterate through all cells in a row.
			 */
			while (cellIter.hasNext()) {
				/*
				 * If one of the cells in given row contains data, the row is considered not
				 * blank.
				 */
				if (!this.isCellBlank(cellIter.next())) {
					ret = false;
					break;
				}
			}
		}

		return ret;
	}

	public boolean isCellBlank(Cell c) {
		return (c == null || c.getCellTypeEnum() == CellType.BLANK);
	}

	public boolean isCellEmpty(Cell c) {
		return c == null || c.getCellTypeEnum() == CellType.BLANK
				|| (c.getCellTypeEnum() == CellType.STRING && c.getStringCellValue().isEmpty());
	}
	
	
	@Transactional(readOnly = false)
	   public Object saveConductSiteSurveyBean( ConductSiteSurveyBean conductSiteSurveyBean)
			throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
		ConductSiteSurveyBean response = null;
		try {
			Task task = getTaskByIdAndWfTaskId(conductSiteSurveyBean.getTaskId(), conductSiteSurveyBean.getWfTaskId());
			String action = "CLOSED";

			if (conductSiteSurveyBean.getAction() != null
					&& conductSiteSurveyBean.getAction().equalsIgnoreCase("save")) {
				action = "SAVED";
			}
			wfMap.put("action", "close");
			String jsonString = Utils.convertObjectToJson(conductSiteSurveyBean);
			Map<String, String> atMap = conductSiteBeanToMap(jsonString);
			
			wfMap.put("hasOSPCapexDeviation", false);
			wfMap.put("hasIBDCapexDeviation", false);

			if ("OSP".equalsIgnoreCase(conductSiteSurveyBean.getPermissionRequired())) {
				wfMap.put("rowRequired", true);
				wfMap.put("prowRequired", true);
			} else if ("IBD".equalsIgnoreCase(conductSiteSurveyBean.getPermissionRequired())) {
				wfMap.put("prowRequired", true);
				wfMap.put("rowRequired", false);				
			}

			else {
				wfMap.put("prowRequired", true);
				wfMap.put("rowRequired", true);
			}

			if ("Yes".equalsIgnoreCase(conductSiteSurveyBean.getOspCapexDeviation())) {
				wfMap.put("hasOSPCapexDeviation", true);
			} else {
				wfMap.put("hasOSPCapexDeviation", false);
			}

			if ("Yes".equalsIgnoreCase(conductSiteSurveyBean.getIbdCapexDeviation())) {
				wfMap.put("hasIBDCapexDeviation", true);
			} else {
				wfMap.put("hasIBDCapexDeviation", false);
			}

			wfMap.put("appointmentAction", "close");

			if (conductSiteSurveyBean.getDocumentIds() != null && !conductSiteSurveyBean.getDocumentIds().isEmpty()) {
				conductSiteSurveyBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			setSiteReadinessValuesIntoFlow(task, wfMap);
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
					task.getSiteType());

			processTaskLogDetails(task, action, conductSiteSurveyBean.getDelayReason(), null, conductSiteSurveyBean);
			if (action.equalsIgnoreCase("CLOSED")) {
				response = (ConductSiteSurveyBean) flowableBaseService.taskDataEntry(task, conductSiteSurveyBean,
						wfMap);

			} else {
				response = (ConductSiteSurveyBean) flowableBaseService.taskDataEntrySave(task, conductSiteSurveyBean);

			}
		} catch (Exception e) {
			throw new TclCommonException("Exception occured in saveConductSiteSurveyBean {} ", e);
		}
		return response;
	}



    
	
	@Transactional(readOnly = false)
    public Object saveConductCrossConnectSiteSurveyBean( ConductCrossConnectSiteSurveyBean conductCrossConnectSiteSurveyBean)
                  throws TclCommonException {
           Map<String, Object> wfMap = new HashMap<>();
           ConductCrossConnectSiteSurveyBean response = null;
		try {
			Task task = getTaskByIdAndWfTaskId(conductCrossConnectSiteSurveyBean.getTaskId(),conductCrossConnectSiteSurveyBean.getWfTaskId());
			wfMap.put("action", "close");
			String action = "CLOSED";

			if (conductCrossConnectSiteSurveyBean.getAction() != null && conductCrossConnectSiteSurveyBean.getAction().equalsIgnoreCase("save")) {
				action = "SAVED";
			}
			
			if ("Yes".equalsIgnoreCase(conductCrossConnectSiteSurveyBean.getIbdCapexDeviation())) {
				wfMap.put("hasIBDCapexDeviation", true);
			} else {
				wfMap.put("hasIBDCapexDeviation", false);
			}
			Map<String, String> compAttrMap = new HashMap<>();
			Map<String, String> serviceAttrMap = new HashMap<>();
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					task.getServiceId(), AttributeConstants.COMPONENT_LM, "A");
			String isBothSiteSameInterface=StringUtils.trimToEmpty(scComponentAttributesAMap.get("isBothSiteSameInterface"));
			if(!isBothSiteSameInterface.isEmpty()){
				logger.info("isBothSiteSameInterface exists");
				if("Yes".equalsIgnoreCase(isBothSiteSameInterface)){
					logger.info("isBothSiteSameInterface::{}",isBothSiteSameInterface);
					if(conductCrossConnectSiteSurveyBean.getDemarcationWing()!=null) {
					compAttrMap.put("demarcationWing", conductCrossConnectSiteSurveyBean.getDemarcationWing());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationFloor()!=null) {

					compAttrMap.put("demarcationFloor", conductCrossConnectSiteSurveyBean.getDemarcationFloor());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationBuildingName()!=null) {

					compAttrMap.put("demarcationBuildingName", conductCrossConnectSiteSurveyBean.getDemarcationBuildingName());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationRoom()!=null) {

					compAttrMap.put("demarcationRoom", conductCrossConnectSiteSurveyBean.getDemarcationRoom());
					}
					if(conductCrossConnectSiteSurveyBean.getRackSize()!=null) {

					compAttrMap.put("rackSize", conductCrossConnectSiteSurveyBean.getRackSize());
					}
					if(conductCrossConnectSiteSurveyBean.getFdf()!=null) {

					compAttrMap.put("fdf", conductCrossConnectSiteSurveyBean.getFdf());
					}
					if(conductCrossConnectSiteSurveyBean.getCableLength()!=null) {

					compAttrMap.put("cableLength", conductCrossConnectSiteSurveyBean.getCableLength());
					}
					if(conductCrossConnectSiteSurveyBean.getHeightOfServerRoom()!=null) {

					compAttrMap.put("heightOfServerRoom", conductCrossConnectSiteSurveyBean.getHeightOfServerRoom());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdLength()!=null) {

					compAttrMap.put("ibdLength", conductCrossConnectSiteSurveyBean.getIbdLength());
					}
					if(conductCrossConnectSiteSurveyBean.getFibrePatchChordLength()!=null) {

					compAttrMap.put("fibrePatchChordLength", conductCrossConnectSiteSurveyBean.getFibrePatchChordLength());
					}
					if(conductCrossConnectSiteSurveyBean.getTypeOfInternalCabling()!=null) {

					compAttrMap.put("typeOfInternalCabling", conductCrossConnectSiteSurveyBean.getTypeOfInternalCabling());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdCapexDeviation()!=null) {

					compAttrMap.put("ibdCapexDeviation", conductCrossConnectSiteSurveyBean.getIbdCapexDeviation());
					}
					if(conductCrossConnectSiteSurveyBean.getSiteSurveyRemarks()!=null) {

					compAttrMap.put("siteSurveyRemarks", conductCrossConnectSiteSurveyBean.getSiteSurveyRemarks());
					}
					if(conductCrossConnectSiteSurveyBean.getRevisedIbdCapex()!=null) {

					compAttrMap.put("revisedIbdCapex", conductCrossConnectSiteSurveyBean.getRevisedIbdCapex());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdDeviationReason()!=null) {

					compAttrMap.put("ibdDeviationReason", conductCrossConnectSiteSurveyBean.getIbdDeviationReason());
					}
				

					componentAndAttributeService.updateAttributes(task.getServiceId(),compAttrMap,AttributeConstants.COMPONENT_LM,"A");
					if(conductCrossConnectSiteSurveyBean.getDemarcationWingB()!=null) {

					compAttrMap.put("demarcationWing", conductCrossConnectSiteSurveyBean.getDemarcationWingB());
					}
					
					if(conductCrossConnectSiteSurveyBean.getDemarcationFloorB()!=null) {

					compAttrMap.put("demarcationFloor", conductCrossConnectSiteSurveyBean.getDemarcationFloorB());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationBuildingNameB()!=null) {

					compAttrMap.put("demarcationBuildingName", conductCrossConnectSiteSurveyBean.getDemarcationBuildingNameB());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationRoomB()!=null) {

					compAttrMap.put("demarcationRoom", conductCrossConnectSiteSurveyBean.getDemarcationRoomB());
					}
					if(conductCrossConnectSiteSurveyBean.getRackSizeB()!=null) {

					compAttrMap.put("rackSize", conductCrossConnectSiteSurveyBean.getRackSizeB());
					}
					if(conductCrossConnectSiteSurveyBean.getFdf()!=null) {

					compAttrMap.put("fdf", conductCrossConnectSiteSurveyBean.getFdf());
					}
					if(conductCrossConnectSiteSurveyBean.getCableLength()!=null) {

					compAttrMap.put("cableLength", conductCrossConnectSiteSurveyBean.getCableLength());
					}
					if(conductCrossConnectSiteSurveyBean.getHeightOfServerRoom()!=null) {

					compAttrMap.put("heightOfServerRoom", conductCrossConnectSiteSurveyBean.getHeightOfServerRoom());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdLength()!=null) {

					compAttrMap.put("ibdLength", conductCrossConnectSiteSurveyBean.getIbdLength());
					}
					if(conductCrossConnectSiteSurveyBean.getFibrePatchChordLength()!=null) {

					compAttrMap.put("fibrePatchChordLength", conductCrossConnectSiteSurveyBean.getFibrePatchChordLength());
					}
					if(conductCrossConnectSiteSurveyBean.getTypeOfInternalCabling()!=null) {

					compAttrMap.put("typeOfInternalCabling", conductCrossConnectSiteSurveyBean.getTypeOfInternalCabling());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdCapexDeviation()!=null) {

					compAttrMap.put("ibdCapexDeviation", conductCrossConnectSiteSurveyBean.getIbdCapexDeviation());
					}
					if(conductCrossConnectSiteSurveyBean.getSiteSurveyRemarks()!=null) {

					compAttrMap.put("siteSurveyRemarks", conductCrossConnectSiteSurveyBean.getSiteSurveyRemarks());
					}
					if(conductCrossConnectSiteSurveyBean.getRevisedIbdCapex()!=null) {

					compAttrMap.put("revisedIbdCapex", conductCrossConnectSiteSurveyBean.getRevisedIbdCapex());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdDeviationReason()!=null) {

					compAttrMap.put("ibdDeviationReason", conductCrossConnectSiteSurveyBean.getIbdDeviationReason());
					}
					componentAndAttributeService.updateAttributes(task.getServiceId(),compAttrMap,AttributeConstants.COMPONENT_LM,"B");
				}else if("No".equalsIgnoreCase(isBothSiteSameInterface)){
					logger.info("isBothSiteSameInterface::{},{}",isBothSiteSameInterface,task.getSiteType());
					if(conductCrossConnectSiteSurveyBean.getDemarcationWing()!=null) {

					compAttrMap.put("demarcationWing", conductCrossConnectSiteSurveyBean.getDemarcationWing());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationFloor()!=null) {

					compAttrMap.put("demarcationFloor", conductCrossConnectSiteSurveyBean.getDemarcationFloor());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationBuildingName()!=null) {

					compAttrMap.put("demarcationBuildingName", conductCrossConnectSiteSurveyBean.getDemarcationBuildingName());
					}
					if(conductCrossConnectSiteSurveyBean.getDemarcationRoom()!=null) {

					compAttrMap.put("demarcationRoom", conductCrossConnectSiteSurveyBean.getDemarcationRoom());
					}
					if(conductCrossConnectSiteSurveyBean.getRackSize()!=null) {

					compAttrMap.put("rackSize", conductCrossConnectSiteSurveyBean.getRackSize());
					}
					if(conductCrossConnectSiteSurveyBean.getFdf()!=null) {

					compAttrMap.put("fdf", conductCrossConnectSiteSurveyBean.getFdf());
					}
					if(conductCrossConnectSiteSurveyBean.getCableLength()!=null) {

					compAttrMap.put("cableLength", conductCrossConnectSiteSurveyBean.getCableLength());
					}
					if(conductCrossConnectSiteSurveyBean.getHeightOfServerRoom()!=null) {

					compAttrMap.put("heightOfServerRoom", conductCrossConnectSiteSurveyBean.getHeightOfServerRoom());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdLength()!=null) {

					compAttrMap.put("ibdLength", conductCrossConnectSiteSurveyBean.getIbdLength());
					}
					if(conductCrossConnectSiteSurveyBean.getFibrePatchChordLength()!=null) {

					compAttrMap.put("fibrePatchChordLength", conductCrossConnectSiteSurveyBean.getFibrePatchChordLength());
					}
					if(conductCrossConnectSiteSurveyBean.getTypeOfInternalCabling()!=null) {

					compAttrMap.put("typeOfInternalCabling", conductCrossConnectSiteSurveyBean.getTypeOfInternalCabling());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdCapexDeviation()!=null) {

					compAttrMap.put("ibdCapexDeviation", conductCrossConnectSiteSurveyBean.getIbdCapexDeviation());
					}
					if(conductCrossConnectSiteSurveyBean.getSiteSurveyRemarks()!=null) {

					compAttrMap.put("siteSurveyRemarks", conductCrossConnectSiteSurveyBean.getSiteSurveyRemarks());
					}
					if(conductCrossConnectSiteSurveyBean.getRevisedIbdCapex()!=null) {

					compAttrMap.put("revisedIbdCapex", conductCrossConnectSiteSurveyBean.getRevisedIbdCapex());
					}
					if(conductCrossConnectSiteSurveyBean.getIbdDeviationReason()!=null) {

					compAttrMap.put("ibdDeviationReason", conductCrossConnectSiteSurveyBean.getIbdDeviationReason());
					}
					componentAndAttributeService.updateAttributes(task.getServiceId(),compAttrMap,AttributeConstants.COMPONENT_LM,task.getSiteType());
				}
			}
			serviceAttrMap.put("pathAvailable", conductCrossConnectSiteSurveyBean.getIbdPathAvailable());
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceAttrMap);
			wfMap.put("appointmentAction", "close");
			processTaskLogDetails(task, action, conductCrossConnectSiteSurveyBean.getDelayReason(),
					null, conductCrossConnectSiteSurveyBean);
			
		 	
			if (action.equalsIgnoreCase("CLOSED")) {

				response = (ConductCrossConnectSiteSurveyBean) flowableBaseService.taskDataEntry(task, conductCrossConnectSiteSurveyBean, wfMap);
			} else {
				response = (ConductCrossConnectSiteSurveyBean) flowableBaseService.taskDataEntrySave(task, conductCrossConnectSiteSurveyBean);
			}
		} catch (Exception e) {
                  throw new TclCommonException("Exception occured in saveConductCrossConnectSiteSurveyBean {} ", e);
           }
           return response;
    }
    
    /**
    * This method is used to save Conduct Site Survey Bean
    *
    * @param taskId
    * @param customerAppointmentBean
    * @return
    * @throws TclCommonException
    */

    @Transactional(readOnly = false,isolation=Isolation.READ_UNCOMMITTED)
    public ConductSiteSurveyManBean saveConductSiteSurveyManBean( ConductSiteSurveyManBean conductSiteSurveyManBean)
			throws TclCommonException {
		Map<String, Object> wfMap = new HashMap<>();
		try {
			Task task = getTaskByIdAndWfTaskId(conductSiteSurveyManBean.getTaskId(),
					conductSiteSurveyManBean.getWfTaskId());

			wfMap.put("action", "close");
			String jsonString = Utils.convertObjectToJson(conductSiteSurveyManBean);
			Map<String, String> atMap = conductSiteBeanToMap(jsonString);

			atMap.put("manSapBundleId", String.valueOf(conductSiteSurveyManBean.getSapBundleId()));

			if ("yes".equalsIgnoreCase(conductSiteSurveyManBean.getIsConnectedSite())) {
				wfMap.put("prowRequired", false);
				wfMap.put("rowRequired", false);
				wfMap.put("isMuxIPAvailable", false);
				wfMap.put("isConnectedSite", true);


				if (conductSiteSurveyManBean.getEndMuxNodeIp() != null) {
					atMap.put("endMuxNodeIp", conductSiteSurveyManBean.getEndMuxNodeIp());
				}
				if (conductSiteSurveyManBean.getEndMuxNodeName() != null) {
					atMap.put("endMuxNodeName", conductSiteSurveyManBean.getEndMuxNodeName());
				}
				if (conductSiteSurveyManBean.getEndMuxNodePort() != null) {
					atMap.put("endMuxNodePort", conductSiteSurveyManBean.getEndMuxNodePort());
				}
				String lastMileScenario = "Onnet Wireline - Connected Customer";
				atMap.put("lastMileScenario", lastMileScenario);

				Optional<ScServiceDetail> optionalServisc = scServiceDetailRepository.findById(task.getServiceId());
				if (optionalServisc.isPresent()) {
					ScServiceDetail scServiceDetail = optionalServisc.get();
					scServiceDetail.setLastmileScenario(lastMileScenario);
					scServiceDetailRepository.save(scServiceDetail);
				}

				try {
					Task networkConfirmation = taskRepository
							.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeOrderByCreatedTimeDesc(task.getServiceId(),
									"network-augmentation", task.getSiteType() == null ? "A" : task.getSiteType());
					if (networkConfirmation != null && !networkConfirmation.getMstStatus().getCode()
							.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
						saveUserNameToNull(networkConfirmation);
						flowableBaseService.taskDataEntry(networkConfirmation, conductSiteSurveyManBean, wfMap);
					}

				} catch (Exception e) {
					logger.error("network-confirmation task close error:{}", e);
				}

				Task holdTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeOrderByCreatedTimeDesc(
						task.getServiceId(), "lm-conduct-site-survey",
						task.getSiteType() == null ? "A" : task.getSiteType());
				if (holdTask != null
						&& !holdTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
					saveUserNameToNull(holdTask);
					flowableBaseService.taskDataEntry(holdTask, conductSiteSurveyManBean, wfMap);
				}

			}

			if (conductSiteSurveyManBean.getMuxMakeModel() != null) {
				atMap.put("muxMakeModel", conductSiteSurveyManBean.getMuxMakeModel());
			}
			if (conductSiteSurveyManBean.getMuxMake() != null) {
				atMap.put("muxMake", conductSiteSurveyManBean.getMuxMake());
			}

			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
					task.getSiteType());

			if ("Ready".equalsIgnoreCase(conductSiteSurveyManBean.getSiteReadinessStatus())) {
				wfMap.put("siteReadinessStatus", true);
			} else {
				wfMap.put("siteReadinessStatus", false);
			}

			if ("No".equalsIgnoreCase(conductSiteSurveyManBean.getIsBOPNetworkSelected())) {
				wfMap.put("isNetworkSelectedPerBOP", false);
			} else {
				wfMap.put("isNetworkSelectedPerBOP", true);
			}

			if (conductSiteSurveyManBean.getDocumentIds() != null
					&& !conductSiteSurveyManBean.getDocumentIds().isEmpty()) {
				conductSiteSurveyManBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			setSiteReadinessValuesIntoFlow(task, wfMap);
			wfMap.put("appointmentAction", "close");
			processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, conductSiteSurveyManBean.getDelayReason(),
					null, conductSiteSurveyManBean);
			return (ConductSiteSurveyManBean) flowableBaseService.taskDataEntry(task, conductSiteSurveyManBean, wfMap);
		} catch (Exception e) {
			throw new TclCommonException("Exception occured in saveConductSiteSurveyBean {} ", e);
		}

	}
    
    private void saveUserNameToNull(Task task) {
    	try {
    	TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst()
				.orElse(null);
		taskAssignment.setUserName(null);
		taskAssignmentRepository.save(taskAssignment);
    	}
    	catch (Exception e) {
			logger.error("saveUserNameToNull:{}",e);

		}
    }

	private void setSiteReadinessValuesIntoFlow(Task task, Map<String, Object> wfMap) {

		try {
			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository.
					findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(), "siteReadinessConfirmation", "LM", task.getSiteType());
            
			logger.info("value of sc ScComponentAttribute:{}", scComponentAttribute);

			if (scComponentAttribute != null) {
				wfMap.put("siteReadinessConfirmation",
						scComponentAttribute.getAttributeValue().equalsIgnoreCase("true")?true:false);
			}
		} catch (Exception e) {
			logger.error("error in setting value in flowable");
		}

	}
    @Transactional(readOnly = false)
	public SiteSurveyRescheduleRequest siteSurveyReschedule(
			SiteSurveyRescheduleRequest siteSurveyRescheduleRequest) throws TclCommonException {

		SiteSurveyRescheduleRequest rescheduleRequest = new SiteSurveyRescheduleRequest();

		Task task = taskRepository.findByIdAndWfTaskId(siteSurveyRescheduleRequest.getTaskId(), siteSurveyRescheduleRequest.getWfTaskId());
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("appointmentAction", "reschedule");

		if (task!=null) {
			//Task task = optionalTask.get();
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));

			taskRepository.save(task);
			if (task.getMstTaskDef().getDependentTaskKey() != null) {
				Task appointmentTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
						siteSurveyRescheduleRequest.getServiceId(), task.getMstTaskDef().getDependentTaskKey());

				if (appointmentTask != null) {
					saveAppointmentDetails(appointmentTask, siteSurveyRescheduleRequest);
				}
			}

			flowableBaseService.taskDataEntryHold(task, rescheduleRequest, wfMap);

			if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-conduct-site-survey")) {

				Task holdTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
						siteSurveyRescheduleRequest.getServiceId(), "lm-conduct-site-survey-man");
				if (holdTask != null) {
					if (!holdTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {

						flowableBaseService.taskDataEntryHold(holdTask, rescheduleRequest, wfMap);
						holdTask.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
						taskRepository.save(holdTask);
					}

					
				}

			} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-conduct-site-survey-man")) {
				Task holdTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
						siteSurveyRescheduleRequest.getServiceId(), "lm-conduct-site-survey");
				if (holdTask != null) {
					
					if (!holdTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
						flowableBaseService.taskDataEntryHold(holdTask, rescheduleRequest, wfMap);
						holdTask.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
						taskRepository.save(holdTask);
					}

					
				}

			}
			processTaskLogDetails(task,TaskStatusConstants.REMARKS,siteSurveyRescheduleRequest.getReason(),null, siteSurveyRescheduleRequest);
		}
		return rescheduleRequest;
	}
    
    @Transactional(readOnly = false)
   	public SiteSurveyRescheduleRequest newAppointment(
   			SiteSurveyRescheduleRequest siteSurveyRescheduleRequest) throws TclCommonException {

   		SiteSurveyRescheduleRequest rescheduleRequest = new SiteSurveyRescheduleRequest();

		Task task = taskRepository.findByIdAndWfTaskId(siteSurveyRescheduleRequest.getTaskId(), siteSurveyRescheduleRequest.getWfTaskId());

   		

   		if (task!=null) {
   			//Task task = optionalTask.get();
   
			Task appointmentTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
					siteSurveyRescheduleRequest.getServiceId(), task.getMstTaskDef().getDependentTaskKey());

			if (appointmentTask != null) {
				saveAppointmentDetails(appointmentTask, siteSurveyRescheduleRequest);
			}
		    if (task.getMstTaskDef().getKey().equalsIgnoreCase("define-offfnet-project-plan")) {
		    	Map<String, Object> wfMap = new HashMap<>();
				wfMap.put("offnetSowAction", "reschedule");
		    	flowableBaseService.taskDataEntryHold(task, rescheduleRequest, wfMap);
		    	/*Execution execution = runtimeService.createExecutionQuery()
		                .processInstanceId(task.getWfProcessInstId())
		                .signalEventSubscriptionName("sow-appointment-signal")
		                .singleResult();
		    	logger.info("trigger sow-appointment-signal execution={}, ProcessInstId={}",execution,task.getWfProcessInstId());
		    	if(execution!=null) {
		    		runtimeService.signalEventReceived("sow-appointment-signal-def", execution.getId());			    
		    	}*/
		    	processTaskLogDetails(task,TaskStatusConstants.REMARKS,siteSurveyRescheduleRequest.getReason(),null, siteSurveyRescheduleRequest);
		    }
   			
   		}
   		return rescheduleRequest;
   	}

    
    /**
	 * This method is used to save appointment details.
	 * 
	 * @param task
	 * @param customerAppointmentBean
	 * @return Task
	 */
	private Task saveAppointmentDetails(Task task, SiteSurveyRescheduleRequest customerAppointmentBean) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(new Timestamp(DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()).getTime()));
		appointment.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
		appointment.setServiceId(task.getServiceId());
		appointment.setDescription(customerAppointmentBean.getDescription());
		appointment.setReason(customerAppointmentBean.getReason());
		appointment.setReschedule("Y");
		appointment.setScopeOfWork(customerAppointmentBean.getScopeOfWork());
		appointment.setMstAppointmentSlot(getMstAppointmentSlotById(customerAppointmentBean.getAppointmentSlot()));
		appointment.setTask(task);
		appointment.setAppointmentType(getMstAppointmentType(task.getMstTaskDef().getKey()));
		appointmentRepository.save(appointment);
		return task;
	}

	/**
	 * This method is used for lm jeopardy 
	 * @param task
	 * @param networkAugmentation
	 * @return 
	 * @throws TclCommonException 
	 */
    @Transactional(readOnly = false)
	public NetworkAugmentation lmJeopardy( NetworkAugmentation networkAugmentation) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(networkAugmentation.getTaskId(),networkAugmentation.getWfTaskId());
		validateInputs(task, networkAugmentation);
		Map<String, Object> flowableMap = new HashMap<>();
		flowableMap.put("offnetLmJeopardyAction", networkAugmentation.getAction());
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,networkAugmentation.getAction(),null, networkAugmentation);
		return (NetworkAugmentation) flowableBaseService.taskDataEntry(task, networkAugmentation, flowableMap);
	}

	@Transactional(readOnly = false)
	public DemarcDetailsBean provideDemarcDetails( DemarcDetailsBean demarcDetailsBean ) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(demarcDetailsBean.getTaskId(),demarcDetailsBean.getWfTaskId());
		validateInputs(task, demarcDetailsBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("demarcationWing", demarcDetailsBean.getDemarcationWing());
		atMap.put("demarcationFloor", demarcDetailsBean.getDemarcationFloor());
		atMap.put("demarcationRoom", demarcDetailsBean.getDemarcationRoom());
		atMap.put("demarcationBuildingName", demarcDetailsBean.getDemarcationBuildingName());
		atMap.put("remarks", demarcDetailsBean.getRemarks());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, demarcDetailsBean.getDelayReason(), null, demarcDetailsBean);
		return (DemarcDetailsBean) flowableBaseService.taskDataEntry(task, demarcDetailsBean);
	}

	/**
	 * This method is used for Mast Installation permission 
	 * @param task
	 * @param documentIds
	 * @param delayReason
	 * @param rejectReason
	 * @return 
	 * @throws TclCommonException 
	 */
    @Transactional(readOnly = false)
	public MastInstallationPermissionBean mastInstallationPermission( MastInstallationPermissionBean mastInstallationPermissionBean ) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(mastInstallationPermissionBean.getTaskId(),mastInstallationPermissionBean.getWfTaskId());
		validateInputs(task, mastInstallationPermissionBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,mastInstallationPermissionBean.getDelayReason(),null, mastInstallationPermissionBean);
		return (MastInstallationPermissionBean) flowableBaseService.taskDataEntry(task, mastInstallationPermissionBean);
	}

	@Transactional(readOnly = false)
	public RaiseDependencyBean raiseDependency(
			RaiseDependencyBean raiseDependencyBean) throws TclCommonException {
		Task task = taskRepository.findByIdAndWfTaskId(raiseDependencyBean.getTaskId(), raiseDependencyBean.getWfTaskId());

		if (task!=null) {
			//Task task = optionalTask.get();
			logger.info(("Task Name:"+task.getMstTaskDef().getKey()));
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));

			Map<String, Object> wfMap = new HashMap<>();
			Map<String, String> componentMap = new HashMap<>();
			wfMap.put("action", raiseDependencyBean.getAction());
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("service-handover-CMIP") &&
					raiseDependencyBean.getAction()!=null && raiseDependencyBean.getAction().equalsIgnoreCase("CIM")){
				logger.info("raiseDependency invoked with CIM as action and task id : {}",task.getId());
				flowableBaseService.taskDataEntry(task, raiseDependencyBean, wfMap);
			}else{
				if((task.getMstTaskDef().getKey().equalsIgnoreCase("izopc-e2e-service-testing-jeopardy") || task.getMstTaskDef().getKey().equalsIgnoreCase("e2e-service-testing-jeopardy"))
						&& raiseDependencyBean.getAction().equalsIgnoreCase("CIM")) {
					wfMap.put("serviceConfigurationAction", "CIM");
					wfMap.put("action", "CIM");
				}
				flowableBaseService.taskDataEntryHold(task, raiseDependencyBean, wfMap);
			}
			taskRepository.save(task);
			if (raiseDependencyBean!=null && StringUtils.isNotBlank(raiseDependencyBean.getReason())) {
				Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
				String errorMessage = raiseDependencyBean.getReason();
				if (scServiceDetail.isPresent()&& StringUtils.isNotBlank(errorMessage)) {
					if(task.getMstTaskDef().getKey().equalsIgnoreCase("commercial-vetting-termination"))
					{
						logger.info("raiseDependency invoked with TERMINATION DESK as action and task id : {}, and terminationDiscrepancyReason: {}",task.getId(), errorMessage);

						componentMap.put("terminationDiscrepancyReason", errorMessage);
						componentAndAttributeService.updateAttributes(task.getServiceId(), componentMap,
									AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
					}
					if ("NPL".equalsIgnoreCase(task.getServiceType())) {
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
								task.getSiteType().toLowerCase()+"_"+task.getMstTaskDef().getKey()+"_error",
								componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
								AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
					}else{
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
								task.getMstTaskDef().getKey()+"_error",
								componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
								AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
					}
				}

			}
		}

		return raiseDependencyBean;
	}

    @Transactional(readOnly = false)
	public NetworkInventoryBean updateAttributesComponent( NetworkInventoryBean networkInventoryBean)
			throws TclCommonException {
    	logger.info("updateAttributesComponent invoked");
		Task task = getTaskByIdAndWfTaskId(networkInventoryBean.getTaskId(),networkInventoryBean.getWfTaskId());
		validateInputs(task, networkInventoryBean);
		String serviceCode = task.getServiceCode();
		String serviceType=task.getServiceType();
		logger.info("serviceType::{}",serviceType);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,
				"INPROGRESS");
		
		if(networkInventoryBean.getAccessRingInfoList() != null && !networkInventoryBean.getAccessRingInfoList().isEmpty()) {
			Map<String, String> varMap = new HashMap<>();
			varMap.put("accessRingTopology", networkInventoryBean.getAccessRingInfoList().get(0).getTopologyName());
			varMap.put("mplsNonFeasibilityReason", networkInventoryBean.getAccessRingInfoList().get(0).getMplsNonFeasibilityReason());
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), varMap, AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
		}

		if (networkInventoryBean.getType().equalsIgnoreCase("ADD")) {
			String accessRingInfo =null;
			if ("NPL".equalsIgnoreCase(task.getServiceType())) {
				accessRingInfo = componentAndAttributeService.getAdditionalAttributes(scServiceDetail,
						task.getSiteType().toLowerCase()+"_access_rings");
			}else{
				accessRingInfo = componentAndAttributeService.getAdditionalAttributes(scServiceDetail,
						"access_rings");
			}
			if (accessRingInfo != null) {

				AccessRingInfo[] accessRingArray = Utils.convertJsonToObject(accessRingInfo, AccessRingInfo[].class);
				List<AccessRingInfo> accessRingInfoList = Arrays.stream(accessRingArray).collect(Collectors.toList());
				accessRingInfoList.addAll(networkInventoryBean.getAccessRingInfoList());
				if ("NPL".equalsIgnoreCase(task.getServiceType())) {
					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, task.getSiteType().toLowerCase()+"_access_rings",
							Utils.convertObjectToJson(accessRingInfoList));
				}else{
					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "access_rings",
							Utils.convertObjectToJson(accessRingInfoList));
				}

			} else {
				if ("NPL".equalsIgnoreCase(task.getServiceType())) {
					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, task.getSiteType().toLowerCase()+"_access_rings",
							Utils.convertObjectToJson(networkInventoryBean.getAccessRingInfoList()));
				}else{
					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "access_rings",
							Utils.convertObjectToJson(networkInventoryBean.getAccessRingInfoList()));
				}
				
			}

		} else if (networkInventoryBean.getType().equalsIgnoreCase("EDIT")) {
			if ("NPL".equalsIgnoreCase(task.getServiceType())) {
				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, task.getSiteType().toLowerCase()+"_access_rings",
						Utils.convertObjectToJson(networkInventoryBean.getAccessRingInfoList()));
			}else{
				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "access_rings",
						Utils.convertObjectToJson(networkInventoryBean.getAccessRingInfoList()));
			}
			

		}

		Map<String, String> atMap = new HashMap<>();
		atMap.put("accessRingName", networkInventoryBean.getSelectedRing());
		atMap.put("networkSuggestionRemarks", networkInventoryBean.getNetworkSuggestionRemarks());
		
		if ("network-augmentation-dependency".equalsIgnoreCase(task.getMstTaskDef().getKey()) 
				&& networkInventoryBean!=null && StringUtils.isNotBlank(networkInventoryBean.getNetworkSuggestionRemarks())) {
	         String errorMessage = networkInventoryBean.getNetworkSuggestionRemarks();
	         if ("NPL".equalsIgnoreCase(task.getServiceType())) {
	        	 componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
	        			 task.getSiteType().toLowerCase()+"_"+task.getMstTaskDef().getKey()+"_error",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
							AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
	         }else{
	        	 componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
							task.getMstTaskDef().getKey()+"_error",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
							AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
	         }
	      }
		Map<String, Object> flowableMap = new HashMap<>();
		if ("network-augmentation".equalsIgnoreCase(task.getMstTaskDef().getKey())){
			flowableMap.put("action", "close");
	    }
		if (networkInventoryBean.getDocumentIds() != null ) {
			networkInventoryBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType() == null ? "A" : task.getSiteType());

		return (NetworkInventoryBean) flowableBaseService.taskDataEntry(task, networkInventoryBean,flowableMap);
	}

    @Transactional(readOnly = false)
	public ProvideRfDataJeopardyBean provideRfDataJjeopardy( ProvideRfDataJeopardyBean provideRfDataJeopardyBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(provideRfDataJeopardyBean.getTaskId(),provideRfDataJeopardyBean.getWfTaskId());
		validateInputs(task, provideRfDataJeopardyBean);
		
		 String errorMessage = provideRfDataJeopardyBean.getRfDataJeopardyRemarks();
		 Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
		if (scServiceDetail.isPresent()&& StringUtils.isNotBlank(errorMessage)) {			
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
					task.getMstTaskDef().getKey()+"_error",
					componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
					AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());

		}      
		
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, provideRfDataJeopardyBean.getRfDataJeopardyRemarks(), null, provideRfDataJeopardyBean);
		return (ProvideRfDataJeopardyBean) flowableBaseService.taskDataEntry(task, provideRfDataJeopardyBean);
	}


	private void saveCosAttributeDetails(ScServiceDetail scServiceDetail, List<CosDetail> cosDetails) {

		if(!cosDetails.isEmpty()) {
	        logger.info("saveCosAttributeDetails if not empty for serviceId:{}",scServiceDetail.getUuid());

			cosDetails.stream().forEach(cosDetail -> {
				processCosServiceAttributes(cosDetail.getCosKey(), cosDetail.getCosValue(), scServiceDetail);
				processCosServiceAttributes(cosDetail.getCriteriaKey(), cosDetail.getCriteriaValue(), scServiceDetail);
				processCosServiceAttributes(cosDetail.getCriteriaTypeKey(), cosDetail.getCriteriaTypeValue(),
						scServiceDetail);
			});
		}

	}

	private void processCosServiceAttributes(String attributeKey, String attributeValue, ScServiceDetail scServiceDetail) {
		if (!StringUtils.isBlank(attributeValue)) {
			List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
					.findByScServiceDetail_idAndAttributeNameAndIsActive(scServiceDetail.getId(), attributeKey, "Y");
			if (Objects.nonNull(scServiceAttributes) && !scServiceAttributes.isEmpty()) {
				scServiceAttributes.forEach(scServiceAttribute -> {
					scServiceAttribute.setCategory("GVPN Common");
					updateServiceAttribute(attributeKey, attributeValue, scServiceAttribute);
				});

			} else {
				createServiceAttribute(attributeKey, attributeValue, "GVPN Common", scServiceDetail);
			}
		}
	}
	private void createServiceAttribute(String attributeName, String attributeValue, String category,ScServiceDetail scServiceDetail){
		if(Objects.nonNull(attributeValue) && !attributeValue.isEmpty()) {
			ScServiceAttribute scServiceAttribute = new ScServiceAttribute();
			scServiceAttribute.setAttributeValue(attributeValue);
			scServiceAttribute.setAttributeAltValueLabel(attributeValue);
			scServiceAttribute.setAttributeName(attributeName);
			scServiceAttribute.setCategory(category);
			scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsActive("Y");
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsAdditionalParam("N");
			scServiceAttribute.setScServiceDetail(scServiceDetail);
			scServiceAttributeRepository.save(scServiceAttribute);
		}
	}

	private void updateServiceAttribute(String attributeName, String attributeValue, ScServiceAttribute scServiceAttribute){
			scServiceAttribute.setAttributeName(attributeName);
			scServiceAttribute.setAttributeValue(attributeValue);
			scServiceAttribute.setAttributeAltValueLabel(attributeValue);
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceAttributeRepository.save(scServiceAttribute);

	}



	/**
	 * This method is used to update termination date
	 *
	 * @param taskId
	 * @param terminateDateBean
	 * @return TerminateDateBean
	 * @throws TclCommonException
	 * @author Thamizhselvi Perumal
	 */
	@Transactional(readOnly = false)
	public TerminateDateBean updateTerminationDate(TerminateDateBean terminateDateBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(terminateDateBean.getTaskId(),terminateDateBean.getWfTaskId());
		validateInputs(task, terminateDateBean);
		Map<String, String> atMap = new HashMap<>();
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("cancel-offnet-po")) {
			if (Objects.nonNull(terminateDateBean.getOffnetTerminationDate())) {
				atMap.put("cancellationDate", terminateDateBean.getOffnetTerminationDate());
			}
			if (Objects.nonNull(terminateDateBean.getIsSupplierEtcAvailable())) {
				atMap.put("isSupplierEtcAvailable", terminateDateBean.getIsSupplierEtcAvailable());
			}
			if (Objects.nonNull(terminateDateBean.getSupplierEtcCharges())) {
				atMap.put("supplierEtcCharges", terminateDateBean.getSupplierEtcCharges());
			}
		} else {
			atMap.put("terminationDate", terminateDateBean.getOffnetTerminationDate());
		}
		Map<String, Object> varMap = new HashMap<>();
		varMap.put("action",terminateDateBean.getAction());

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if(!CollectionUtils.isEmpty(terminateDateBean.getDocumentIds())){
			terminateDateBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,terminateDateBean.getDelayReason(),null, terminateDateBean);
		return (TerminateDateBean) flowableBaseService.taskDataEntry(task, terminateDateBean,varMap);
	}

	@Transactional(readOnly = false)
	public UpdateDependencyRemarksBean updateProvideRemarks( UpdateDependencyRemarksBean updateDependencyRemarksBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(updateDependencyRemarksBean.getTaskId(),updateDependencyRemarksBean.getWfTaskId());
		validateInputs(task, updateDependencyRemarksBean);

		String errorMessage = updateDependencyRemarksBean.getRemarks();
		Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("action", "close");
		if ("e2e-service-testing-cim-dependency".equals(task.getMstTaskDef().getKey())) {			
			wfMap.put("serviceConfigurationAction", "close");
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
					"e2eTestingFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
						AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
			
		}else if (scServiceDetail.isPresent()&& StringUtils.isNotBlank(errorMessage)) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
					task.getMstTaskDef().getKey()+"_error",
					componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
					AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
		}
		

		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, errorMessage, null, updateDependencyRemarksBean);
		return (UpdateDependencyRemarksBean) flowableBaseService.taskDataEntry(task, updateDependencyRemarksBean,wfMap);
	}
	
	
	/**
	 * Method for Krone Mrn creation
	 *
	 * @param taskId
	 * @param mrnForMuxBean
	 * @return MrnForMuxBean
	 * @throws TclCommonException
	 * @author Yogesh
	 */
	@Transactional(readOnly = false)
	public MrnForMuxBean createMnrForKrone( MrnForMuxBean mrnForMuxBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(mrnForMuxBean.getTaskId(),mrnForMuxBean.getWfTaskId());
		validateInputs(task, mrnForMuxBean);
		if (mrnForMuxBean.getDocumentIds() != null ) {
			mrnForMuxBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, String> atMap = new HashMap<>();
		atMap.put("kroneMrnNo", mrnForMuxBean.getMrnNumber());
		atMap.put("kroneMrnDate", mrnForMuxBean.getMrnDate());
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("isKroneRequired", mrnForMuxBean.getIsKroneRequired());
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		String errorMessage=mrnForMuxBean.getRemarks();
		if (Objects.nonNull(task.getScServiceDetail())&& StringUtils.isNotBlank(errorMessage)) {
			componentAndAttributeService.updateAdditionalAttributes(task.getScServiceDetail(),
					task.getMstTaskDef().getKey()+"_error",
					componentAndAttributeService.getErrorMessageDetails(errorMessage, task.getMstTaskDef().getName()),
					AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());

		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,mrnForMuxBean.getDelayReason(),null, mrnForMuxBean);
		processTaskLogDetails(task,TaskStatusConstants.REMARKS,mrnForMuxBean.getRemarks(),null, mrnForMuxBean);
		return (MrnForMuxBean) flowableBaseService.taskDataEntry(task, mrnForMuxBean,wfMap);
	}
	
	/**
	 * Method to Install Krone
	 * 
	 * @param taskId
	 * @param installKroneBean
	 * @return
	 * @throws TclCommonException
	 * 
	 * @author yogesh
	 */
	@Transactional(readOnly = false)
	public InstallKroneBean installKrone( InstallKroneBean installKroneBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(installKroneBean.getTaskId(),installKroneBean.getWfTaskId());
		validateInputs(task, installKroneBean);
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,installKroneBean.getDelayReason(),null, installKroneBean);
		return (InstallKroneBean) flowableBaseService.taskDataEntry(task, installKroneBean);
	}

	/**
	 * @author vivek
	 *
	 * @param taskDetailsBaseBean
	 * @return
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly = false)
	public TaskDetailsBaseBean provideMandatoryDetails(TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(taskDetailsBaseBean.getTaskId(),taskDetailsBaseBean.getWfTaskId());
		validateInputs(task, taskDetailsBaseBean);
		
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, taskDetailsBaseBean.getDelayReason(), null,taskDetailsBaseBean);
		return (DemarcDetailsBean) flowableBaseService.taskDataEntry(task, taskDetailsBaseBean);
	}

	@Transactional(readOnly = false)
	public SdwanAdditionalTechnicalDetails saveSdwanAdditionalTechnicalDetails(
			SdwanAdditionalTechnicalDetails sdwanAdditionalTechnicalDetailsBean) throws TclCommonException {

        logger.info("saveSdwanAdditionalTechnicalDetails for serviceId:{}",sdwanAdditionalTechnicalDetailsBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(sdwanAdditionalTechnicalDetailsBean.getTaskId(), sdwanAdditionalTechnicalDetailsBean.getWfTaskId());
		try {
			if(Objects.nonNull(sdwanAdditionalTechnicalDetailsBean.getVpnName())){
				processServiceAttributes("vpnName", sdwanAdditionalTechnicalDetailsBean.getVpnName(),task.getScServiceDetail(),"IZOSDWAN_COMMON");
			}
			if(Objects.nonNull(sdwanAdditionalTechnicalDetailsBean.getDirectorInstanceMapping())){
				processServiceAttributes("directorInstanceMapping", sdwanAdditionalTechnicalDetailsBean.getDirectorInstanceMapping(),
						task.getScServiceDetail(),"IZOSDWAN_COMMON");
			}
			if(sdwanAdditionalTechnicalDetailsBean.getIsSecurityAddonApplicable().equalsIgnoreCase("Y")) {
				componentAndAttributeService.updateAdditionalAttributes(task.getScServiceDetail(), "securityAddonData"
						, sdwanAdditionalTechnicalDetailsBean.getSecurityAddonData());
			}
			if (sdwanAdditionalTechnicalDetailsBean.getDocumentIds() != null && !sdwanAdditionalTechnicalDetailsBean.getDocumentIds().isEmpty()) {
				logger.info("Document exists::{}",sdwanAdditionalTechnicalDetailsBean.getDocumentIds().size());
				sdwanAdditionalTechnicalDetailsBean.getDocumentIds()
				.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			persistCpeNetworkDetailsAndTemplateName(task,sdwanAdditionalTechnicalDetailsBean);
		} catch (Exception e) {
			logger.error("update advance enrichment :{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,sdwanAdditionalTechnicalDetailsBean.getDelayReason(),null, sdwanAdditionalTechnicalDetailsBean);
		return (SdwanAdditionalTechnicalDetails) flowableBaseService.taskDataEntry(task, sdwanAdditionalTechnicalDetailsBean);
	}


	public void persistCpeNetworkDetailsAndTemplateName(Task task, SdwanAdditionalTechnicalDetails sdwanAdditionalTechnicalDetailsBean){

        logger.info("persistCpeNetworkDetailsAndTemplateName service Id:{}",sdwanAdditionalTechnicalDetailsBean.getServiceId());
        /*String regionCode = "";
	    if (!sdwanAdditionalTechnicalDetailsBean.getDirectorInstanceMapping().isEmpty()) {
            regionCode = sdwanAdditionalTechnicalDetailsBean.getDirectorInstanceMapping().toLowerCase().substring(0, 3);
        }
		String customerName = getCustomerName(task.getScServiceDetail().getScOrder().getErfCustCustomerName());*/

		List<CpeDetailsBean> cpeDetailsBeanList = sdwanAdditionalTechnicalDetailsBean.getCpeDetails();
		if(!cpeDetailsBeanList.isEmpty()) {
			for (CpeDetailsBean cpeDetailsBean : cpeDetailsBeanList) {
				if (!cpeDetailsBean.getCpeNetworkDetails().isEmpty()){
					componentAndAttributeService.updateComponentAdditionalAttributes(task.getScServiceDetail(), cpeDetailsBean.getComponentId(),
							"cpeNetworkDetails", cpeDetailsBean.getCpeNetworkDetails());
				}
				logger.info("persisted cpeNetworkDetails for componentId :{}", cpeDetailsBean.getComponentId());

				/*String templateName = "";
				if(!sdwanAdditionalTechnicalDetailsBean.getNoOfCpe().isEmpty() && !sdwanAdditionalTechnicalDetailsBean.getNoOfWanLinks().isEmpty()
				&& !regionCode.isEmpty() && !customerName.isEmpty()){
					String noOfCpe = convertToNumeric(sdwanAdditionalTechnicalDetailsBean.getNoOfCpe());
				String noOfWan = convertToNumeric(sdwanAdditionalTechnicalDetailsBean.getNoOfWanLinks());
				if (cpeDetailsBeanList.size() == 1 || cpeDetailsBean.getDetails().size()>1) {
						logger.info("setting templateName for single or shared cpe site component id::{}",cpeDetailsBean.getComponentId());
						templateName = regionCode + "_" + customerName + "_" + noOfCpe+ "cpe_" + noOfWan + "wan";
						persistTemplateNameinScAttribute(templateName, task, cpeDetailsBean.getComponentId());
					logger.info("templateName set for single/shared cpe scenario:{}, componentId:{}", templateName,cpeDetailsBean.getComponentId());
					} else {
						logger.info("setting templateName for dual cpe site component id::{}",cpeDetailsBean.getComponentId());
					if (cpeDetailsBean.getDetails().get(0).getLinkType().equalsIgnoreCase("Primary")) {
							templateName = regionCode + "_" + customerName + "_" + noOfCpe + "cpe_" + noOfWan + "wan" + "_1";
							persistTemplateNameinScAttribute(templateName, task, cpeDetailsBean.getComponentId());
							logger.info("templateName set for primary link:{}, componentId:{}", templateName,cpeDetailsBean.getComponentId());

						} else if (cpeDetailsBean.getDetails().get(0).getLinkType().equalsIgnoreCase("Secondary")) {
							templateName = regionCode + "_" + customerName + "_" + noOfCpe + "cpe_" + noOfWan + "wan" + "_2";
						persistTemplateNameinScAttribute(templateName, task, cpeDetailsBean.getComponentId());
							logger.info("templateName set for secondary link:{}, componentId:{}", templateName,cpeDetailsBean.getComponentId());
						}
					}
				}*/
			}
		}
	}

	/*public void persistTemplateNameinScAttribute(String templateName,Task task, Integer ComponentId){
		Map<String, String> atmap = new HashMap<>();
		atmap.put("templateName", templateName);
		componentAndAttributeService.updateAttributesByScComponent(task.getServiceId(),atmap,ComponentId);
	}*/

	private void processServiceAttributes(String attributeKey, String attributeValue, ScServiceDetail scServiceDetail,String category) {
		ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), attributeKey);
		if (Objects.nonNull(scServiceAttribute)) {
			scServiceAttribute.setCategory(category);
			updateServiceAttribute(attributeKey, attributeValue, scServiceAttribute);
		} else {
			createServiceAttribute(attributeKey, attributeValue, category, scServiceDetail);
		}
	}

//	public String getCustomerName(String customerName){
//		String str = customerName.replaceAll(" ", "_").toLowerCase();
//		Integer noOfSpaces = org.springframework.util.StringUtils.countOccurrencesOf(str,"_");
//		return str.substring(0,15+noOfSpaces);
//	}

//	public String convertToNumeric(String attribute){
//		if (attribute.equalsIgnoreCase("one"))
//			return "1";
//		if (attribute.equalsIgnoreCase("two"))
//			return "2";
//		if (attribute.equalsIgnoreCase("three"))
//			return "3";
//		return "";
//	}

	@Transactional(readOnly = false)
	public ByonReadinessBean saveByonReadinessDetails(
			ByonReadinessBean byonReadinessBean) throws TclCommonException {
		logger.info("Enter saveByonReadinessDetails for serviceId:{}",byonReadinessBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(byonReadinessBean.getTaskId(), byonReadinessBean.getWfTaskId());
		try {
			Map<String, String> byonReadinessAttributes = new HashMap<>();
			byonReadinessAttributes.put("portBandwidth", byonReadinessBean.getPortBandwidth());
			byonReadinessAttributes.put("thirdPartyServiceID", byonReadinessBean.getThirdPartyServiceID());
			byonReadinessAttributes.put("thirdPartyIPAddress", byonReadinessBean.getThirdPartyIPAddress());
			byonReadinessAttributes.put("thirdPartyTicketingContact", byonReadinessBean.getThirdPartyTicketingContact());
			//byonReadinessAttributes.put("3rd Party Escalation Matrix", byonReadinessBean.getThirdPartyEscalationMatrix());
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(),byonReadinessAttributes,"BYON_COMMON");
			List<ScComponent> scComponentList = scComponentRepository.findByScServiceDetailId(task.getServiceId());
			if(scComponentList!=null && !scComponentList.isEmpty()) {
				logger.info("scComponentList is present {}",scComponentList.size());
				Optional<ScComponent> scComponentOptional = scComponentList.stream().findFirst();
				if(scComponentOptional.isPresent()) {
					ScComponent scComponent=scComponentOptional.get();
					logger.info("scComponent is present {}",scComponent.getId());
					componentAndAttributeService.updateAttributesByScComponent(task.getServiceId(), byonReadinessAttributes, scComponent.getId());
				}
			}
			if (byonReadinessBean.getDocumentIds() != null && !byonReadinessBean.getDocumentIds().isEmpty()) {
				logger.info("Document exists::{}",byonReadinessBean.getDocumentIds().size());
				byonReadinessBean.getDocumentIds()
				.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			logger.info("Saved byon readiness details {}",byonReadinessBean.getServiceId());
		}
		catch (Exception e) {
			logger.error("saveByonReadinessDetails :{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,byonReadinessBean.getDelayReason(),null, byonReadinessBean);
		return (ByonReadinessBean) flowableBaseService.taskDataEntry(task, byonReadinessBean);

	}	
	
	@Transactional(readOnly = false)
	public SdwaCloudGatewayBean saveSdwanCgwAdvanceEnrichmentDetails(
			SdwaCloudGatewayBean sdwanCloudGatewayBean) throws TclCommonException {
		logger.info("Enter saveSdwanCgwAdvanceEnrichmentDetails for serviceId:{}",sdwanCloudGatewayBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(sdwanCloudGatewayBean.getTaskId(), sdwanCloudGatewayBean.getWfTaskId());
		try {	
			if (Objects.nonNull(task.getScServiceDetail())) { 
				if(Objects.nonNull(sdwanCloudGatewayBean.getCosDetails()) && !sdwanCloudGatewayBean.getCosDetails().isEmpty()){        		
					saveSdwanCgwCosAttributeDetails(task.getScServiceDetail(),sdwanCloudGatewayBean.getCosDetails());
					logger.info("Saved sdwan cloud gateway primary advance enrichment details {}",sdwanCloudGatewayBean.getServiceId());
				}            	
				ScServiceDetail scServiceDetailSecondary = scServiceDetailRepository.findByUuidAndScOrderUuid(sdwanCloudGatewayBean.getSecondaryCGWServiceId(),sdwanCloudGatewayBean.getOrderCode());
				if (Objects.nonNull(scServiceDetailSecondary) && Objects.nonNull(sdwanCloudGatewayBean.getCosDetails()) && !sdwanCloudGatewayBean.getCosDetails().isEmpty()) {     		
						saveSdwanCgwCosAttributeDetails(scServiceDetailSecondary,sdwanCloudGatewayBean.getCosDetails());
						logger.info("Saved sdwan cloud gateway secondary advance enrichment details {}",sdwanCloudGatewayBean.getServiceId());
				}
			}
		}
		catch (Exception e) {
			logger.error("saveSdwanCgwAdvanceEnrichmentDetails :{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,sdwanCloudGatewayBean.getDelayReason(),null, sdwanCloudGatewayBean);
		return (SdwaCloudGatewayBean) flowableBaseService.taskDataEntry(task, sdwanCloudGatewayBean);

	}	

	private void saveSdwanCgwCosAttributeDetails(ScServiceDetail scServiceDetail, List<CosDetail> cosDetails) {
		cosDetails.stream().forEach(cosDetail -> {
			processServiceAttributes(cosDetail.getCosKey(), cosDetail.getCosValue(), scServiceDetail,"CGW Common");
			processServiceAttributes(cosDetail.getCriteriaKey(), cosDetail.getCriteriaValue(), scServiceDetail,"CGW Common");
			processServiceAttributes(cosDetail.getCriteriaTypeKey(), cosDetail.getCriteriaTypeValue(), scServiceDetail,"CGW Common");
		});

	}

	@Transactional(readOnly = false)
	public LLDAndMigrationBean saveLLDAndMigrationDocuments(LLDAndMigrationBean lldAndMigrationBean)throws TclCommonException {
		logger.info("saveLLDAndMigrationDocument for serviceId:{}", lldAndMigrationBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(lldAndMigrationBean.getTaskId(),lldAndMigrationBean.getWfTaskId());
		validateInputs(task, lldAndMigrationBean);

		if (lldAndMigrationBean.getLldDocumentIds() != null && !lldAndMigrationBean.getLldDocumentIds().isEmpty()) {
			for(SolutionAttachmentBean attachmentIdBean:lldAndMigrationBean.getLldDocumentIds()){
				Optional<Attachment> attachmentOptional=attachmentRepository.findById(attachmentIdBean.getAttachmentId());
				if(attachmentOptional.isPresent()){
					logger.info("LLD Attachment Exists");
					Attachment attachment=attachmentOptional.get();
					List<ScAttachment> scLLDAttachmentList=scAttachmentRepository.findAllByScServiceDetail_IdAndAttachment(task.getServiceId(),attachment);
					if(scLLDAttachmentList==null || scLLDAttachmentList.isEmpty()){
						logger.info("LLdDocumentIds persisted");
						makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId());
					}
				}
			}
		}

		if(!task.getMstStatus().getCode().equalsIgnoreCase("CLOSED")){
			if (lldAndMigrationBean.getMigrationDocumentIds() != null && !lldAndMigrationBean.getMigrationDocumentIds().isEmpty()) {
				lldAndMigrationBean.getMigrationDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
				logger.info("MigrationDocumentIds persisted");
			}
			
			if (lldAndMigrationBean.getSupportingDocumentIds() != null && !lldAndMigrationBean.getSupportingDocumentIds().isEmpty()) {
				lldAndMigrationBean.getSupportingDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
				logger.info("SupportingDocumentIds persisted");
			}
		}

		// For teamsdr..
		// To store securityCertifcate and its document Ids.
		if (Objects.nonNull(task.getScServiceDetail()) && Objects.nonNull(task.getScServiceDetail().getScOrderUuid())
				&& CommonConstants.UCDR.equals(task.getScServiceDetail().getScOrderUuid())) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put(AttributeConstants.SECURITY_CERTIFICATE, lldAndMigrationBean.getSecurityCertificateName());
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
					task.getSiteType());

			if (Objects.nonNull(lldAndMigrationBean.getTeamsDRDocumentIds())
					&& !lldAndMigrationBean.getTeamsDRDocumentIds().isEmpty()) {
				for (SolutionAttachmentBean attachmentIdBean : lldAndMigrationBean.getTeamsDRDocumentIds()) {
					Optional<Attachment> attachmentOptional = attachmentRepository
							.findById(attachmentIdBean.getAttachmentId());
					if (attachmentOptional.isPresent()) {
						Attachment attachment = attachmentOptional.get();
						List<ScAttachment> scLLDAttachmentList = scAttachmentRepository
								.findAllByScServiceDetail_IdAndAttachment(task.getServiceId(), attachment);
						if (scLLDAttachmentList == null || scLLDAttachmentList.isEmpty()) {
							makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId());
						}else{
							scLLDAttachmentList.forEach(scAttachment -> {
								scAttachment.setAttachment(attachment);
								scAttachmentRepository.save(scAttachment);
							});
						}
					}
				}
			}
		}

		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,lldAndMigrationBean.getDelayReason(),null, lldAndMigrationBean);
		return (LLDAndMigrationBean) flowableBaseService.taskDataEntry(task, lldAndMigrationBean);
	}

	@Transactional(readOnly = false)
	public LLDPreparationBean saveLldScheduleCall(LLDPreparationBean lldPreparationBean)throws TclCommonException {
		logger.info("saveLldScheduleCall for serviceId:{}", lldPreparationBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(lldPreparationBean.getTaskId(),lldPreparationBean.getWfTaskId());
		logger.info("saveLldScheduleCall for task def key:{}", task.getMstTaskDef().getKey());
		validateInputs(task, lldPreparationBean);

		List<ScComponent> scComponentList = scComponentRepository.findByScServiceDetailId(task.getServiceId());
		Optional<ScComponent> scComponentOptional = scComponentList.stream().findFirst();

		if(scComponentOptional.isPresent()) {
			ScComponent scComponent=scComponentOptional.get();
			logger.info("saveLLDScheduleInternalCall for component id:{}", scComponent.getId());
			if (lldPreparationBean.getMomDetails() != null && !lldPreparationBean.getMomDetails().isEmpty()) {
				logger.info("momDetails exist");
				if(task.getMstTaskDef().getKey().contains("internal-call-schedule"))
				componentAndAttributeService.updateComponentAdditionalAttributes(task.getScServiceDetail(), scComponent.getId(),
						"internalCallDetails", lldPreparationBean.getMomDetails());
				else if(task.getMstTaskDef().getKey().contains("customer-kick-off-call-schedule"))
					componentAndAttributeService.updateComponentAdditionalAttributes(task.getScServiceDetail(), scComponent.getId(),
							"kickOffMomDetails", lldPreparationBean.getMomDetails());
				logger.info("mailMOMDetails persisted in additional !!");
			}
			if (lldPreparationBean.getDocumentIds() != null && !lldPreparationBean.getDocumentIds().isEmpty())
				lldPreparationBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			logger.info("LLldDocumentIds persisted");

			Map<String, String> atMap = new HashMap<>();
			if (lldPreparationBean.getScheduledDate() != null && !lldPreparationBean.getScheduledDate().isEmpty()) {
				if(task.getMstTaskDef().getKey().contains("internal-call-schedule"))
					atMap.put("internalCallDate", lldPreparationBean.getScheduledDate());
				else if(task.getMstTaskDef().getKey().contains("customer-kick-off-call-schedule"))
					atMap.put("customerCallDate",lldPreparationBean.getScheduledDate());
			}
			if (lldPreparationBean.getScheduledFromTime() != null && !lldPreparationBean.getScheduledFromTime().isEmpty()) {
				if(task.getMstTaskDef().getKey().contains("internal-call-schedule"))
					atMap.put("internalCallFromTime", lldPreparationBean.getScheduledFromTime());
				else if(task.getMstTaskDef().getKey().contains("customer-kick-off-call-schedule"))
					atMap.put("customerCallFromTime", lldPreparationBean.getScheduledFromTime());
			}
			if (lldPreparationBean.getScheduledToTime() != null && !lldPreparationBean.getScheduledToTime().isEmpty()) {
				if(task.getMstTaskDef().getKey().contains("internal-call-schedule"))
					atMap.put("internalCallToTime", lldPreparationBean.getScheduledToTime());
				else if(task.getMstTaskDef().getKey().contains("customer-kick-off-call-schedule"))
					atMap.put("customerCallToTime", lldPreparationBean.getScheduledToTime());
			}
			if (lldPreparationBean.getCustomerContactName() != null && !lldPreparationBean.getCustomerContactName().isEmpty()) {
				atMap.put("lldCustomerContactName", lldPreparationBean.getCustomerContactName());
			}
			if (lldPreparationBean.getCustomerContactMobile() != null && !lldPreparationBean.getCustomerContactMobile().isEmpty()) {
				atMap.put("lldCustomerContactMobile", lldPreparationBean.getCustomerContactMobile());
			}
			if (lldPreparationBean.getCustomerContactEmailId() != null && !lldPreparationBean.getCustomerContactEmailId().isEmpty()) {
				atMap.put("lldCustomerEmailId", lldPreparationBean.getCustomerContactEmailId());
			}
			if (lldPreparationBean.getIpDetailsReceived() != null && !lldPreparationBean.getIpDetailsReceived().isEmpty()) {
				atMap.put("ipDetailsReceived", lldPreparationBean.getIpDetailsReceived());
			}
			if (lldPreparationBean.getSddSignedOff() != null && !lldPreparationBean.getSddSignedOff().isEmpty()) {
				atMap.put("sddSignedOff", lldPreparationBean.getSddSignedOff());
			}
			if (lldPreparationBean.getCustomerPOReceived() != null && !lldPreparationBean.getCustomerPOReceived().isEmpty()) {
				atMap.put("customerPORececived", lldPreparationBean.getCustomerPOReceived());
			}
			componentAndAttributeService.updateAttributesByScComponent(task.getServiceId(), atMap, scComponent.getId());
			logger.info("saveLldScheduleCall component attributes persisted");
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS, lldPreparationBean.getDelayReason(),null, lldPreparationBean);
		return (LLDPreparationBean) flowableBaseService.taskDataEntry(task, lldPreparationBean);
	}
	
	/**
	 * 
	 * Persist the Assist CMIP task details for IZOSDWAN
	 * @author AnandhiV
	 * @param assistCmipBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public AssistCmipBean persistAssistCmipTaskDetails(AssistCmipBean assistCmipBean) throws TclCommonException {
		if (assistCmipBean.getTaskId() == null || assistCmipBean.getWfTaskId() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		logger.info("Inside Assist CMIP task persistance part task id -->  {} , wf task id --> {}",
				assistCmipBean.getTaskId(), assistCmipBean.getWfTaskId());
		Task task = taskRepository.findByIdAndWfTaskId(assistCmipBean.getTaskId(), assistCmipBean.getWfTaskId());
		if (task != null) {

			if (assistCmipBean.getServiceLevelUpdateBeans() != null
					&& !assistCmipBean.getServiceLevelUpdateBeans().isEmpty()) {
				assistCmipBean.getServiceLevelUpdateBeans().stream().forEach(serviceLevelUpdateBean -> {
					Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository
							.findById(serviceLevelUpdateBean.getServiceId());
					if (scServiceDetail.isPresent()) {
						scServiceDetail.get().setRrfsDate(serviceLevelUpdateBean.getRrfsDate());
						ScSolutionComponent scSolutionComponent = scSolutionComponentRepository
								.findByScServiceDetail1(scServiceDetail.get());
						if (scSolutionComponent != null) {
							scSolutionComponent.setPriority(serviceLevelUpdateBean.getPriority());
							scSolutionComponentRepository.saveAndFlush(scSolutionComponent);
						}
						scServiceDetailRepository.saveAndFlush(scServiceDetail.get());
					}
				});
			}
			processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, assistCmipBean.getDelayReason(), null,
					assistCmipBean);
			logger.info("Ending Assist CMIP task persistance part task id -->  {} , wf task id --> {}",
					assistCmipBean.getTaskId(), assistCmipBean.getWfTaskId());
		}
		return (AssistCmipBean) flowableBaseService.taskDataEntry(task, assistCmipBean);
	}

	private Task saveSdwanAppointmentDetails(Task task, CustomerAppointmentBean customerAppointmentBean,Integer serviceId) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(new Timestamp(DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()).getTime()));
		appointment.setLocalItname(customerAppointmentBean.getLocalContactName());
		appointment.setLocalItEmail(customerAppointmentBean.getLocalContactEMail());
		appointment.setLocalItContactMobile(customerAppointmentBean.getLocalContactNumber());
		appointment.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
		appointment.setServiceId(serviceId);
		appointment.setDescription(customerAppointmentBean.getOtherDocument());
		appointment.setMstAppointmentSlot(getMstAppointmentSlotById(customerAppointmentBean.getAppointmentSlot()));
		appointment.setTask(task);
		appointment.setAppointmentType("sdwan-"+getMstAppointmentType(task.getMstTaskDef().getKey()));

		Set<AppointmentDocuments> appointmentDocuments = new HashSet<>();
		customerAppointmentBean.getDocumentAttachments().forEach(attachment -> {
			AppointmentDocuments appointmentDocument = new AppointmentDocuments();
			appointmentDocument.setAppointment(appointment);
			appointmentDocument.setMstAppointmentDocuments(mstAppointmentDocumentRepository.findById(attachment).get());
			appointmentDocuments.add(appointmentDocument);
		});
		appointment.setAppointmentDocuments(appointmentDocuments);
		appointmentRepository.save(appointment);
		return task;
	}

	@Transactional(readOnly = false)
	public SdwanOrderDetailsBean saveSdwanOrderDetails(SdwanOrderDetailsBean sdwanOrderDetailsBean)
			throws TclCommonException {
		logger.info("saveSdwanOrderDetails for serviceId:{}", sdwanOrderDetailsBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(sdwanOrderDetailsBean.getTaskId(),sdwanOrderDetailsBean.getWfTaskId());
		validateInputs(task, sdwanOrderDetailsBean);
		ScComponent scComponent = scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(),"LM",task.getSiteType());
		Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(sdwanOrderDetailsBean.getServiceId());
		Map<String, String> atMap = new HashMap<>();
		if(scComponent!=null) {
			logger.info("scComponent exists:: {}", scComponent.getId());
			if (sdwanOrderDetailsBean.getOrderPlacedStatus() != null && !sdwanOrderDetailsBean.getOrderPlacedStatus().isEmpty()) {
				atMap.put("orderPlacedStatus", sdwanOrderDetailsBean.getOrderPlacedStatus());
				componentAndAttributeService.updateAttributesByScComponent(task.getServiceId(), atMap, scComponent.getId());
			}
		}
		if (scServiceDetailOpt.isPresent()) {
			ScServiceDetail scServiceDetail = scServiceDetailOpt.get();
			logger.info("Service detail id exists :: {}", scServiceDetail.getId());
			if(sdwanOrderDetailsBean.getLegacyServiceId() != null && !sdwanOrderDetailsBean.getLegacyServiceId().isEmpty()){
				logger.info("Legacy ServiceId exists:: {}", sdwanOrderDetailsBean.getLegacyServiceId());
				scServiceDetail.setTpsServiceId(sdwanOrderDetailsBean.getLegacyServiceId());
				scServiceDetailRepository.saveAndFlush(scServiceDetail);
			}
			if(sdwanOrderDetailsBean.getTigerOrderId() != null && !sdwanOrderDetailsBean.getTigerOrderId().isEmpty()){
				logger.info("Tiger order id exists:: {}", sdwanOrderDetailsBean.getTigerOrderId());
				scServiceDetail.setTigerOrderId(sdwanOrderDetailsBean.getTigerOrderId());
				scServiceDetailRepository.saveAndFlush(scServiceDetail);
			}
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,sdwanOrderDetailsBean.getDelayReason(),null, sdwanOrderDetailsBean);
		return (SdwanOrderDetailsBean) flowableBaseService.taskDataEntry(task, sdwanOrderDetailsBean);
	}

	private void copyMasterVrfDetailsToSlave(ScServiceDetail masterServiceDetail){
		logger.info("Inside copyMasterVrfDetailsToSlave method :{}",masterServiceDetail.getId());
		List<ScServiceDetail> scServiceDetails=scServiceDetailRepository.findByMasterVrfServiceId(masterServiceDetail.getId());
		if(scServiceDetails!=null && !scServiceDetails.isEmpty()){
			scServiceDetails.stream().forEach(slaveServiceDetail1 -> {
				if(Objects.nonNull(masterServiceDetail.getVpnSolutionId()) && !masterServiceDetail.getVpnSolutionId().isEmpty()){
					slaveServiceDetail1.setVpnSolutionId(masterServiceDetail.getVpnSolutionId());
				}
				if(Objects.nonNull(masterServiceDetail.getPrimarySecondary()) && !masterServiceDetail.getPrimarySecondary().isEmpty()){
					slaveServiceDetail1.setPrimarySecondary(masterServiceDetail.getPrimarySecondary());
				}
				if(Objects.nonNull(masterServiceDetail.getPriSecServiceLink()) && !masterServiceDetail.getPriSecServiceLink().isEmpty()){
					slaveServiceDetail1.setPriSecServiceLink(masterServiceDetail.getPriSecServiceLink());
				}
				if(Objects.nonNull(masterServiceDetail.getErfPrdCatalogOfferingName()) && !masterServiceDetail.getErfPrdCatalogOfferingName().isEmpty()){
					slaveServiceDetail1.setErfPrdCatalogOfferingName(masterServiceDetail.getErfPrdCatalogOfferingName());
				}
				scServiceDetailRepository.save(slaveServiceDetail1);
			});

		}
	}

	public void copyMasterVrfAttributesToSlave(ScServiceDetail masterServiceDetail,String siteType ,Map<String, String> atMap)
			throws TclCommonException {
		logger.info("Inside copyMasterVrfAttributesToSlave method :{} ",masterServiceDetail.getId());
		List<ScServiceDetail> serviceDetailList=scServiceDetailRepository.findByMasterVrfServiceId(masterServiceDetail.getId());
		serviceDetailList.stream().forEach(slaveServiceDetail -> {
			slaveServiceDetail.setLastmileScenario(masterServiceDetail.getLastmileScenario());
			scServiceDetailRepository.save(slaveServiceDetail);
			componentAndAttributeService.updateAttributes(slaveServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM,siteType);
		});
	}
	
	
	/**
	 *
	 * @param taskId
	 * @param additionalTechnicalDetailsBean
	 * @return IzopcAdditionalTechnicalDetailsBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public IzopcAdditionalTechnicalDetailsBean saveIzopcAdditionalTechnicalDetails(
			IzopcAdditionalTechnicalDetailsBean additionalTechnicalDetailsBean) throws TclCommonException {
		logger.info("Inside saveIzopcAdditionalTechnicalDetails method with request body : {}", additionalTechnicalDetailsBean);
		Task task = getTaskByIdAndWfTaskId(additionalTechnicalDetailsBean.getTaskId(), additionalTechnicalDetailsBean.getWfTaskId());
		validateInputs(task, additionalTechnicalDetailsBean);
		try {
			Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository.findById(task.getServiceId());
			if (optionalServiceDetails.isPresent()) {
				ScServiceDetail serviceDetails = optionalServiceDetails.get();
				String customeName = null;
				if (additionalTechnicalDetailsBean.getProgramName() == null || org.apache.commons.lang3.StringUtils
						.isBlank(additionalTechnicalDetailsBean.getProgramName())) {

					customeName = splitCustomerName(serviceDetails.getScOrder().getErfCustCustomerName());
				} else {
					customeName = splitCustomerName63(serviceDetails.getScOrder().getErfCustCustomerName());

				}

				String vpnName = splitProjectName(customeName, additionalTechnicalDetailsBean.getProgramName());
				serviceDetails.setVpnSolutionId(vpnName);
				if(Objects.nonNull(additionalTechnicalDetailsBean.getPrimarySecondary()) && !additionalTechnicalDetailsBean.getPrimarySecondary().isEmpty()){
					serviceDetails.setPrimarySecondary(additionalTechnicalDetailsBean.getPrimarySecondary());
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getPrisecLink()) && !additionalTechnicalDetailsBean.getPrisecLink().isEmpty()){
					serviceDetails.setPriSecServiceLink(additionalTechnicalDetailsBean.getPrisecLink());
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getProductOfferingName()) && !additionalTechnicalDetailsBean.getProductOfferingName().isEmpty()){
					serviceDetails.setErfPrdCatalogOfferingName(additionalTechnicalDetailsBean.getProductOfferingName());
				}
				scServiceDetailRepository.save(serviceDetails);
				
				if(Objects.nonNull(additionalTechnicalDetailsBean.getLanIpAddress()) && !additionalTechnicalDetailsBean.getLanIpAddress().isEmpty()){
					String[] lanIps=additionalTechnicalDetailsBean.getLanIpAddress().split(",");
					logger.info("LanIp exists::{}",lanIps.length);
					List<AceIPMapping> aceIpMappingList= new ArrayList<>();
					for(String lanIp:lanIps){
						List<AceIPMapping> existingAceIPMappingList=aceIPMappingRepository.findByScServiceDetail_IdAndAceIp(serviceDetails.getId(),lanIp);
						if((Objects.isNull(existingAceIPMappingList) || existingAceIPMappingList.isEmpty()) && lanIp.contains(".") && lanIp.contains("/")){
							logger.info("LanIp not exists::{}",lanIp);
								AceIPMapping aceIPMapping= new AceIPMapping();
								aceIPMapping.setScServiceDetail(serviceDetails);
								aceIPMapping.setAceIp(lanIp);
								aceIpMappingList.add(aceIPMapping);
						}
					}
					if(!aceIpMappingList.isEmpty()){
						logger.info("AceIpMapping exists");
						aceIPMappingRepository.saveAll(aceIpMappingList);
					}
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getCosDetails()) && !additionalTechnicalDetailsBean.getCosDetails().isEmpty()){
			        logger.info("saveCosAttributeDetails for serviceId:{}",serviceDetails.getUuid());

					saveCosAttributeDetails(serviceDetails,additionalTechnicalDetailsBean.getCosDetails());
				}

				String jsonString = Utils.convertObjectToJson(additionalTechnicalDetailsBean);
				Map<String, String> atMap = conductAdditionalTechBeanToMap(jsonString);
				String wanIpProvidedBy = org.apache.commons.lang3.StringUtils.trimToEmpty(additionalTechnicalDetailsBean.getWanIpProvidedBy());
				if(Objects.nonNull(wanIpProvidedBy)) {
					Map<String, String> serviceMap = new HashMap<>();
					serviceMap.put("WAN IP Provided By", wanIpProvidedBy);
					componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceMap, "VPN Port");
				}
				if(additionalTechnicalDetailsBean.getCloudProvider()!=null && (additionalTechnicalDetailsBean.getCloudProvider().equalsIgnoreCase("Google Public Cloud") || 
						additionalTechnicalDetailsBean.getCloudProvider().equalsIgnoreCase("Google Cloud Interconnect-Private"))) {
					Map<String, String> serviceMap = new HashMap<>();
					serviceMap.put("WAN IP Provided By", "Customer");
					componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceMap, "VPN Port");
				}
				if("customer".equalsIgnoreCase(wanIpProvidedBy) ) {
					logger.info("wanIpProvidedBy customer for serviceId:{}",serviceDetails.getUuid());
					atMap.put("wanIpProvidedByCust", "Yes");
					additionalTechnicalDetailsBean.setWanIpProvidedByCust("Yes");
					//Other than Google
					if(additionalTechnicalDetailsBean.getWanIpAddress()!=null 
							&& !additionalTechnicalDetailsBean.getWanIpAddress().isEmpty()) {
						logger.info("WanIpAddress exists for serviceId:{}",serviceDetails.getUuid());
						saveWanIpDetails(task.getServiceId(),atMap,additionalTechnicalDetailsBean.getWanIpAddress(),"Primary");
					}
					//Other than Google and IBM
					if(additionalTechnicalDetailsBean.getSecondaryWanIpAddress()!=null 
							&& !additionalTechnicalDetailsBean.getSecondaryWanIpAddress().isEmpty()) {
						logger.info("SecondaryWanIpAddress exists for serviceId:{}",serviceDetails.getUuid());
						saveWanIpDetails(task.getServiceId(),atMap,additionalTechnicalDetailsBean.getSecondaryWanIpAddress(),"Secondary");
					}
				}else {
					logger.info("wanIpProvidedBy tcl for serviceId:{}",serviceDetails.getUuid());
					atMap.put("wanIpProvidedByCust", "No");
					atMap.put("wanIpAddress", null);
					atMap.put("secondaryWanIpAddress", null);
					atMap.put("vsnlWanIpAddress",null);
					atMap.put("wanIpPool",null);
					atMap.put("secondaryVsnlWanIpAddress",null);
					atMap.put("secondaryWanIpPool",null);
					additionalTechnicalDetailsBean.setWanIpProvidedByCust("No");
				}
				if(additionalTechnicalDetailsBean.getCloudProvider()!=null && (additionalTechnicalDetailsBean.getCloudProvider().equalsIgnoreCase("Google Public Cloud") || 
						additionalTechnicalDetailsBean.getCloudProvider().equalsIgnoreCase("Google Cloud Interconnect-Private"))) {
					atMap.put("wanIpProvidedByCust", "Yes");
					atMap.put("wanIpProvidedBy", "Customer");
				}
				if(additionalTechnicalDetailsBean.getChangedVRFName()!=null && !additionalTechnicalDetailsBean.getChangedVRFName().isEmpty()){
					atMap.put("changedVRFName", additionalTechnicalDetailsBean.getChangedVRFName());
				}

				if(additionalTechnicalDetailsBean.getLanPoolRoutingNeeded()!=null) {
					atMap.put("lanPoolRoutingNeeded", additionalTechnicalDetailsBean.getLanPoolRoutingNeeded());
				}

				if("No".equalsIgnoreCase(additionalTechnicalDetailsBean.getPrefixForCspProvidedByCustomer())){
					logger.info("prefixForCsp not provided by customer for serviceId:{}",serviceDetails.getUuid());
					atMap.put("prefixForCsp", null);
				}

				if("No".equalsIgnoreCase(additionalTechnicalDetailsBean.getPrefixFromMplsToCspProvidedByCustomer())){
					logger.info("prefixFromMplsToCsp not provided by customer for serviceId:{}",serviceDetails.getUuid());
					atMap.put("prefixFromMplsToCsp", null);
				}

				if(!"Customer".equalsIgnoreCase(additionalTechnicalDetailsBean.getPublicNATIpProvidedBy())){
					logger.info("publicNATIp not provided by customer for serviceId:{}",serviceDetails.getUuid());
					atMap.put("publicNATIp", null);
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getCustomerPrefixes()) && !additionalTechnicalDetailsBean.getCustomerPrefixes().isEmpty()){
					String[] prefixes = additionalTechnicalDetailsBean.getCustomerPrefixes().split(",");
					saveValueInAceIpMapping(prefixes,serviceDetails);
				}
				logger.info("saveIzopcAdditionalTechnicalDetails.atMap:{}",atMap);
				componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

				Map<String, String> serviceMap = new HashMap<>();
				if(Objects.nonNull(additionalTechnicalDetailsBean.getCloudProviderRefID()) && !additionalTechnicalDetailsBean.getCloudProviderRefID().isEmpty()) {
					serviceMap.put("Cloud Provider Ref ID", additionalTechnicalDetailsBean.getCloudProviderRefID());
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getSecondCloudProviderRefID()) && !additionalTechnicalDetailsBean.getSecondCloudProviderRefID().isEmpty()) {
					serviceMap.put("secondCloudProviderRefID", additionalTechnicalDetailsBean.getSecondCloudProviderRefID());
				}
				if(Objects.nonNull(additionalTechnicalDetailsBean.getCspProvidedVlanId()) && !additionalTechnicalDetailsBean.getCspProvidedVlanId().isEmpty()) {
					serviceMap.put("CSP Provided VLAN ID", additionalTechnicalDetailsBean.getCspProvidedVlanId());
				}
				componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceMap, "IZO Private Connect Port");

				Map<String, String> siteTopologyMap = new HashMap<>();
				siteTopologyMap.put("Site Type", additionalTechnicalDetailsBean.getSiteType());
				siteTopologyMap.put("VPN Topology", additionalTechnicalDetailsBean.getVpnTopology());
				componentAndAttributeService.updateSiteTopologyServiceAttributes(task.getServiceId(),siteTopologyMap);
			}
		} catch (Exception e) {
			logger.error("saveIzopcAdditionalTechnicalDetails :: error:{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,additionalTechnicalDetailsBean.getDelayReason(),null, additionalTechnicalDetailsBean);
		return (IzopcAdditionalTechnicalDetailsBean) flowableBaseService.taskDataEntry(task, additionalTechnicalDetailsBean);
	}

	public void saveValueInAceIpMapping( String[] value, ScServiceDetail serviceDetails){
		logger.info("saveValueInAceIpMapping:: Ips exists with value:: {}", value);
		List<AceIPMapping> aceIpMappingList= new ArrayList<>();
		for(String ip :value){
			List<AceIPMapping> existingAceIPMappingList=aceIPMappingRepository.findByScServiceDetail_IdAndAceIp(serviceDetails.getId(),ip);
			if((Objects.isNull(existingAceIPMappingList) || existingAceIPMappingList.isEmpty()) && ip.contains(".") && ip.contains("/")){
				logger.info("saveValueInAceIpMapping:: Ip not exists::{}",ip);
				AceIPMapping aceIPMapping= new AceIPMapping();
				aceIPMapping.setScServiceDetail(serviceDetails);
				aceIPMapping.setAceIp(ip);
				aceIpMappingList.add(aceIPMapping);
			}
		}
		if(!aceIpMappingList.isEmpty()){
			logger.info("saveValueInAceIpMapping:: AceIpMapping list exists");
			aceIPMappingRepository.saveAll(aceIpMappingList);
		}
	}
	
	private void saveWanIpDetails(
			Integer serviceId,Map<String, String> atMap, String wanIpAddress, String redundancyRole) {
		logger.info("saveWanIpDetails for serviceId::{} with wanIpAddress::{} with redundancyRole::{}", serviceId,wanIpAddress,redundancyRole);
		if(redundancyRole.equalsIgnoreCase("Primary")) {
			atMap.put("vsnlWanIpAddress",getIpAddressSplitForCustomerProvidedWan(wanIpAddress,1)+"/"+subnet(wanIpAddress).trim());
			atMap.put("wanIpPool",getIpAddressSplitForCustomerProvidedWan(wanIpAddress,2)+"/"+subnet(wanIpAddress).trim());
		}else {
			atMap.put("secondaryVsnlWanIpAddress",getIpAddressSplitForCustomerProvidedWan(wanIpAddress,1)+"/"+subnet(wanIpAddress).trim());
			atMap.put("secondaryWanIpPool",getIpAddressSplitForCustomerProvidedWan(wanIpAddress,2)+"/"+subnet(wanIpAddress).trim());
		}
	}
	
	public  String getIpAddressSplitForCustomerProvidedWan(String ipAddress,int count){
		logger.info("getIpAddressSplitForCustomerProvidedWan for ipAddress::{},count::{}", ipAddress,count);
		String array[]=ipAddress.split("/");
		String ssMgmtIp=array[0];
	    String[] splitValue = ssMgmtIp.split("\\.");
	    splitValue[splitValue.length-1] = String.valueOf(Integer.parseInt(splitValue[splitValue.length-1])-count);
	    String output = Arrays.asList(splitValue).stream().map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));
	    return output;
	}
	
	public String subnet(String ipAddress) {
		logger.info("subnet for ipAddress::{}", ipAddress);
		try {
			if (ipAddress != null) {
				String a[] = ipAddress.split("/");
				return a[1];
			}
		} catch (Exception e) {
			return ipAddress;
		}
		return ipAddress;
	}
}

