package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;
import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskData;
import com.tcl.dias.servicefulfillment.entity.entities.Vendors;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstVendorsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VendorsRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;


/**
 * @author vivek
 *
 * 
 */
public abstract class ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFulfillmentBaseService.class);

	public static final String OUTPUT = "_output";

	@Autowired
	protected org.flowable.engine.TaskService flowableTaskService;

	@Autowired
	protected TaskRepository taskRepository;

	@Autowired
	protected TaskDataRepository taskDataRepository;

	@Autowired
	protected TaskCacheService taskCacheService;

	@Autowired
	protected ScAttachmentRepository scAttachmentRepository;

	@Autowired
	protected AttachmentRepository attachmentRepository;

	@Autowired
	protected MstVendorsRepository mstVendorRepository;

	@Autowired
	protected ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	protected UserInfoUtils userInfoUtils;

	@Autowired
	protected ProcessTaskLogRepository processTaskLogRepository;
	
	@Autowired
	protected ServiceStatusDetailsRepository serviceStatusDetailsRepository;
	
	@Autowired
	protected FieldEngineerRepository fieldEngineerRepository;

	@Autowired
	protected VendorsRepository vendorsRepository;
	
	 @Autowired
	 protected MstVendorsRepository mstVendorsRepository;

	/**
	 * used to update action corresponding to a flowable task
	 * 
	 * @author diksha garg
	 * 
	 * @param task
	 * @param action
	 * @param baseRequest TODO
	 */
	@Transactional(readOnly = false)
	protected void processTaskLogDetails(Task task, String action, String description, String actionTo, BaseRequest baseRequest) {
		LOGGER.info("Inside Process Task Log Details Method with task - {}  ", task);
		ProcessTaskLog processTaskLog = createProcessTaskLog(task, action, description, actionTo,baseRequest);
		processTaskLogRepository.save(processTaskLog);
	}

	/**
	 * used to update task log corresponding to a flowable task action
	 * 
	 * @author diksha garg
	 * 
	 * @param task
	 * @param action
	 * @param actionTo
	 * @param actionFrom
	 */
	@Transactional(readOnly = false)
	protected ProcessTaskLog createProcessTaskLog(Task task, String action, String description, String actionTo,BaseRequest baseRequest) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		processTaskLog.setActionTo(actionTo);
		processTaskLog.setServiceCode(task.getServiceCode());
		if (action.equals("CLOSED"))
			action = TaskLogConstants.CLOSED;
		processTaskLog.setAction(action);
		processTaskLog.setQuoteCode(task.getQuoteCode());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setDescrption(description);
		if(baseRequest!=null) {
			processTaskLog.setCategory(baseRequest.getDelayReasonCategory());
			processTaskLog.setSubCategory(baseRequest.getDelayReasonSubCategory());
		}
	

		return processTaskLog;

	}
	
	
	protected ProcessTaskLog createProcessTaskLog( ScServiceDetail scServiceDetail,String action, String description, String actionTo, BaseRequest baseRequest) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setOrderCode(scServiceDetail.getScOrderUuid());
		processTaskLog.setScOrderId(scServiceDetail.getScOrder().getId());
		processTaskLog.setServiceId(scServiceDetail.getId());
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		if(baseRequest!=null) {
			processTaskLog.setCategory(baseRequest.getDelayReasonCategory());
			processTaskLog.setSubCategory(baseRequest.getDelayReasonSubCategory());
		}
		processTaskLog.setActionTo(actionTo);
		processTaskLog.setServiceCode(scServiceDetail.getUuid());
		processTaskLog.setAction(action);
		processTaskLog.setDescrption(description);
		return processTaskLog;

	}

	

	/**
	 * used to get the task by id
	 * 
	 * @param taskId
	 * @return
	 */
	protected Task getTaskById(Integer taskId) {
		return taskRepository.findById(taskId).orElseThrow(
				() -> new TclCommonRuntimeException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_ERROR));
	}
	
	/**
	 * @author vivek
	 *
	 * @param taskId
	 * @param wfTaskId
	 * @return
	 */
	protected Task getTaskByIdAndWfTaskId(Integer taskId,String wfTaskId) {
		return taskRepository.findByIdAndWfTaskId(taskId,wfTaskId);
	}
	
	/**
	 * @author vivek
	 *
	 * @param taskId
	 * @param wfTaskId
	 * @return
	 */
	protected List<Task> getTaskByOrderCodeAndServiceDeatils(String orderCode,String serviceCode,Integer serviceId) {
		return  taskRepository.findByOrderCodeAndServiceCodeAndServiceId(orderCode,serviceCode,serviceId);

	}
	
	
	protected ScServiceDetail getServiceDetailsByOrderCodeAndServiceDeatils(String orderCode,String serviceCode,Integer serviceId) {
		return  scServiceDetailRepository.findByScOrderUuidAndUuidAndId(orderCode,serviceCode,serviceId);

	}




	
	/**
	 * find MstVendor for given id
	 *
	 * @param mstVendorId
	 * @return
	 */
	protected MstVendor getMstVendor(Integer mstVendorId) {
		return mstVendorRepository.findById(mstVendorId)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.MST_VENDOR_NOT_FOUND,
						ResponseResource.R_CODE_ERROR));
	}

	/**
	 * @param task
	 * @param attachmentId
	 * @return
	 */
	protected ScAttachment makeEntryInScAttachment(Task task, Integer attachmentId) {
		return scAttachmentRepository.save(constructScAttachment(task, attachmentId));
	}

	/**
	 * @param task
	 * @param attachmentId
	 * @return
	 */
	protected ScAttachment constructScAttachment(Task task, Integer attachmentId) {
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(getAttachmentById(attachmentId));
		scAttachment.setScServiceDetail(scServiceDetailRepository.findById(task.getServiceId()).get());
		scAttachment.setIsActive("Y");
		scAttachment.setSiteType(task.getSiteType() == null ? "A" : task.getSiteType());
		scAttachment.setOrderId(task.getScOrderId());
		return scAttachment;
	}

	/**
	 * Method to construct attachment for mg teamsdr..
	 * @param task
	 * @param attachmentId
	 * @param mgId
	 * @return
	 */
	protected ScAttachment constructScAttachmentForMediaGateway(Task task, Integer attachmentId,Integer mgId) {
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(getAttachmentById(attachmentId));
		scAttachment.setScServiceDetail(scServiceDetailRepository.findById(task.getServiceId()).get());
		scAttachment.setSiteId(mgId);
		scAttachment.setIsActive("Y");
		scAttachment.setSiteType(task.getSiteType() == null ? "A" : task.getSiteType());
		scAttachment.setOrderId(task.getScOrderId());
		return scAttachment;
	}

	/**
	 * @param attachmentId
	 * @return
	 */
	protected Attachment getAttachmentById(Integer attachmentId) {
		return attachmentRepository.findById(attachmentId).orElseThrow(
				() -> new TclCommonRuntimeException("Attachment not found", ResponseResource.R_CODE_ERROR));
	}
	/**
	 * @param task
	 * @param detailsBean used to validate input
	 */
	protected void validateInputs(Task task, Object detailsBean) {
		Objects.requireNonNull(task, "Task  cannot be null");
		Objects.requireNonNull(detailsBean, "{} cannot be null " + detailsBean);
	}
	
	/**
	 * @param data
	 * @param task
	 * @return TaskData used to save task data
	 */
	protected TaskData saveTaskData(String data, Task task) {
		TaskData taskData = new TaskData();
		taskData.setName(task.getMstTaskDef().getKey() + OUTPUT);
		taskData.setData(data);
		taskData.setTask(task);
		taskData.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
		return taskDataRepository.save(taskData);
	}
	
	@Transactional(readOnly = false)
	protected ScAttachment checkAndMakeEntryInScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId, String category) {
		List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_IdAndAttachment_Id(scServiceDetail.getId(), attachmentId);
		if(scAttachments != null && !scAttachments.isEmpty()) {
			return scAttachments.get(0);
		} else {
			scAttachments = scAttachmentRepository
					.findAllByScServiceDetail_IdAndAttachment_category(scServiceDetail.getId(), category);
			if(scAttachments != null && !scAttachments.isEmpty()) {
				ScAttachment scAttachment = scAttachments.get(0);
				scAttachment.setAttachment(getAttachmentById(attachmentId));
				return scAttachmentRepository.save(scAttachment);
			}
		}
		return scAttachmentRepository.save(constructScAttachment(scServiceDetail, attachmentId));
	}
	
	protected ScAttachment checkAndMakeEntryInScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId) {
		List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_IdAndAttachment_Id(scServiceDetail.getId(), attachmentId);
		if(scAttachments != null && !scAttachments.isEmpty()) {
			return scAttachments.get(0);
		}
		return scAttachmentRepository.save(constructScAttachment(scServiceDetail, attachmentId));
	}

	@Transactional(readOnly = false)
	protected ScAttachment makeEntryInScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId) {
		return scAttachmentRepository.save(constructScAttachment(scServiceDetail, attachmentId));
	}
	
	protected ScAttachment constructScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId) {
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(getAttachmentById(attachmentId));
		scAttachment.setScServiceDetail(scServiceDetail);
		scAttachment.setIsActive("Y");
		scAttachment.setSiteType(scServiceDetail.getSiteType() == null ? "A" : scServiceDetail.getSiteType());
		scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
		return scAttachment;
	}
	
	
	protected Task getTaskData(Integer serviceId, String taskDefKey) {
		return taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId, taskDefKey);
	}
    protected BaseRequest constructBaseRequest(Map<String, Object> argument){
		BaseRequest baseRequest= new BaseRequest();
		if(Objects.nonNull(argument) && !argument.isEmpty()) {
			baseRequest.setDelayReason(Objects.nonNull(argument.get("delayReason"))? String.valueOf(argument.get("delayReason")):null);
			baseRequest.setDelayReasonCategory(Objects.nonNull(argument.get("delayReasonCategory"))? String.valueOf(argument.get("delayReasonCategory")):null);
			baseRequest.setDelayReasonSubCategory(Objects.nonNull(argument.get("delayReasonSubCategory"))? String.valueOf(argument.get("delayReasonSubCategory")):null);
		}
		return baseRequest;
	}
    
    protected ServiceStatusDetails updateServiceStatusAndCreatedNewStatus(ScServiceDetail scServiceDetail,String status,String holdCategory) {
		
		ServiceStatusDetails serviceStatusDetails=	serviceStatusDetailsRepository.findFirstByScServiceDetail_idOrderByIdDesc(scServiceDetail.getId());
		
		if(serviceStatusDetails!=null) {
			serviceStatusDetails.setEndTime(new Timestamp(new Date().getTime()));
			serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));
			serviceStatusDetailsRepository.save(serviceStatusDetails);
		}
		createServiceStaus(scServiceDetail, status,holdCategory);
		return serviceStatusDetails;
	}
    
    protected ServiceStatusDetails createServiceStaus(ScServiceDetail scServiceDetail, String mstStatus,String category) {

		ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
		serviceStatusDetails.setScServiceDetail(scServiceDetail);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceStatusDetails.setUserName(userInfoUtils.getUserInformation().getUserId());
		}
		serviceStatusDetails.setServiceChangeCategory(category);
		serviceStatusDetails.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setStartTime(new Timestamp(new Date().getTime()));;
		serviceStatusDetails.setStatus(mstStatus);
		serviceStatusDetailsRepository.save(serviceStatusDetails);

		return serviceStatusDetails;

	}
    
	protected void saveFiledEngineer(Task task,
			com.tcl.dias.servicefulfillmentutils.beans.VendorDetailsRequest vendorDetailsBean) {

		if (vendorDetailsBean.getVendorType() != null && vendorDetailsBean.getVendorType().equalsIgnoreCase("OSP")) {
			FieldEngineerDetailsBean fieldEngineerDetail = new FieldEngineerDetailsBean();
			fieldEngineerDetail.setName(vendorDetailsBean.getOspFeName());
			fieldEngineerDetail.setContactNumber(vendorDetailsBean.getOspFeContactNumber());
			fieldEngineerDetail.setEmailId(vendorDetailsBean.getOspFeEmailId());
			fieldEngineerDetail.setSecondaryContactNumber(vendorDetailsBean.getOspSecondaryFeContactNumber());
			fieldEngineerDetail.setSecondaryEmailId(vendorDetailsBean.getOspSecondaryFeEmailId());
			fieldEngineerDetail.setSecondaryName(vendorDetailsBean.getOspSecondaryFeName());
			saveFieldEngineer(task, fieldEngineerDetail);
		}
	}

	protected void saveVendorDetail(Task task,
			com.tcl.dias.servicefulfillmentutils.beans.VendorDetailsRequest vendorDetailsBean) {

		Vendors vendors = new Vendors();

		if (vendorDetailsBean.getMstVendorId() != null) {
			vendors.setMstVendor(getMstVendor(vendorDetailsBean.getMstVendorId()));
		} else {
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
		String value = "";
		if (key.contains("arrange-field-engineer")) {
			value = key.replace("arrange-field-engineer-", "");
		} else if (key.contains("customer-appointment")) {
			value = key.replace("customer-appointment-", "");
		} else if (key.contains("select-vendor")) {
			value = key.replace("select-vendor-", "");
		}
		return value;
	}
    
	protected String getEffectiveDateForTermination(String terminationEffectiveDate) throws TclCommonException {
		try{
			LocalDateTime localDateTime=null;
			String fromTime = "11:59";
			LOGGER.info("effective date for termination {} {} :",terminationEffectiveDate, fromTime);
			terminationEffectiveDate = terminationEffectiveDate.split(" ")[0];
			terminationEffectiveDate=terminationEffectiveDate+" "+fromTime;
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(terminationEffectiveDate);
			localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC"));
			terminationEffectiveDate=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			LOGGER.info("Derived effective date for termination::{}",terminationEffectiveDate);
			return terminationEffectiveDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForTermination:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
}
