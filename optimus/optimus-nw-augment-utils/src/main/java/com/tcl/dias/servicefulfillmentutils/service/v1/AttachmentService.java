package com.tcl.dias.servicefulfillmentutils.service.v1;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.entities.Appointment;
import com.tcl.dias.networkaugment.entity.entities.AppointmentDocuments;
import com.tcl.dias.networkaugment.entity.entities.Attachment;
import com.tcl.dias.networkaugment.entity.entities.MstAppointmentDocuments;
import com.tcl.dias.networkaugment.entity.entities.ScAttachment;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.repository.AppointmentDocumentsRepository;
import com.tcl.dias.networkaugment.entity.repository.AppointmentRepository;
import com.tcl.dias.networkaugment.entity.repository.AttachmentRepository;
import com.tcl.dias.networkaugment.entity.repository.MstAppointmentDocumentRepository;
import com.tcl.dias.networkaugment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstAppointmentDocumentBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceResponse;
import com.tcl.dias.servicefulfillmentutils.beans.ValidateSupportingDocBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.helper.UploadAttachmentComponent;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly = true,isolation=Isolation.READ_COMMITTED)
public class AttachmentService  extends ServiceFulfillmentBaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentService.class);
    public static final String DATEFORMAT = "yyyyMMddHHmmss";

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Value("${temp.download.url.expiryWindow}")
    String tempDownloadUrlExpiryWindow;

    @Value("${swift.documentservice.url}")
    String documentServiceUrl;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ScAttachmentRepository scAttachmentRepository;

    @Autowired
    AttachmentRepository attachmentRepository;


    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    TaskCacheService taskCacheService;

    @Autowired
    org.flowable.engine.TaskService flowableTaskService;

    @Autowired
    UploadAttachmentComponent uploadAttachmentComponent;

    @Autowired
    AppointmentDocumentsRepository appointmentDocumentsRepository;

    @Autowired
    MstAppointmentDocumentRepository mstAppointmentDocumentRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;

    @Autowired
    FlowableBaseService flowableBaseService;

    @Autowired
    DocumentService documentService;

    @Autowired
    AttachmentService attachmentService;

    @Value("${swift.auth.url}")
    String authUrl;

    @Value("${swift.username}")
    String swiftUserName;

    @Value("${swift.auth.key}")
    String swiftAuthKey;

    private static final String VALIDATE_ATTACHMENT = "_validate_attachment";

    /**
     * This will return list of documents/attachments for given taskID
     *
     * @param task
     * @return List<AttachmentBean>
     * @author Vishesh Awasthi
     */
    public List<AttachmentBean> getAttachmentListByTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        String authToken = attachmentService.getAuthToken();

        List<AttachmentBean> attachmanets= Optional.ofNullable(task)
                .map(Task::getServiceId)
                .map(this::fetchAttachmentIds)
                .map(this::getListOfAttachments)
                .map(this::mapToAttachmentBean)
                .orElse(ImmutableList.of());
        attachmanets.stream().filter(attachmentBean->attachmentBean.getName()!=null).forEach(
                attachmentBean -> {
                    attachmentBean.setStoragePathUrl(documentServiceUrl+attachmentBean.getName());
                    LOGGER.info("attachmentBean.getName(). "+attachmentBean.getName());
                    attachmentBean.setName(attachmentBean.getName());
                }
        );
        return attachmanets;

    }
    public byte[] downloadFile(String folderName, String fileName) throws TclCommonRuntimeException, TclCommonException {

        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        String fileUrl = "";//tempUploadUrlInfo+ "/" + folderName + "/" + fileName;
        ResponseEntity<String> responseEntity = restTemplate.exchange(fileUrl, HttpMethod.GET, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("File Download Failed");
        }
        String data = responseEntity.getBody();

        return data.getBytes();
    }
    public String getAuthToken() throws TclCommonRuntimeException {
        String userName = getUserName();
        String authKey = getAuthKey();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-User", getUserName().trim());
        httpHeaders.add("X-Auth-Key", getAuthKey());
        System.out.println("======Auth Token"+authKey);

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        //ResponseEntity<Void> responseEntity = restTemplate.getForEntity(getAuthURL(), Void.class);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(getAuthURL(),HttpMethod.GET, httpEntity, Void.class);
        HttpHeaders rHttpHeaders = responseEntity.getHeaders();

        List<String> authTokenList = rHttpHeaders.get("X-Auth-Token");
        String authToken = null;
        if(authTokenList != null && authTokenList.size() > 0) {
            authToken = authTokenList.get(0);
        }

        System.out.println("========= auth Token"+ authToken);
        return authToken;
    }
    private String getAuthURL() {
        // return "http://inp44xdapp3221/auth";
        System.out.println("============= auth URL "+authUrl);
        return authUrl;
    }
    private String getUserName() {
        // return "optnwachn1";
        System.out.println("============= Username "+swiftUserName);
        return swiftUserName;
    }

    private String getAuthKey() {
        // return "nyTwxCcqvjOCQW0ImqJA3QN05PGh8J5oLAKlsdrX";
        System.out.println("============= swiftAuthKey "+swiftAuthKey);
        return swiftAuthKey;
    }
    private RestTemplate getRestTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        return restTemplateBuilder.build();
    }
    /**
     * This will return attachment for given ServiceID and category
     *
     * @param serviceId
     * @param category
     * @return AttachmentBean
     * @author Samuel
     */
    public AttachmentBean getAttachmentByServiceIdAndCategory(Integer serviceId,String category,String siteType) {
        Objects.requireNonNull(serviceId, "serviceId cannot be null");
        ScAttachment scAttachment = scAttachmentRepository.findFirstByScServiceDetail_IdAndAttachment_categoryAndSiteTypeOrderByIdDesc(serviceId, category, siteType);
        if(scAttachment!=null) return AttachmentBean.mapToBean(scAttachment.getAttachment());
        else return null;
    }


    /**
     * This will return all attachment for given ServiceID and category list
     *
     * @param serviceId
     * @param categoryList
     * @return List<AttachmentBean>
     * @author Samuel
     * @param siteType
     */
    public List<AttachmentBean> getAllAttachmentByServiceIdAndcategory(Integer serviceId,List<String> categoryList, String siteType) {
        Objects.requireNonNull(serviceId, "serviceId cannot be null");
        List<ScAttachment> scAttachments = new ArrayList<>();
        categoryList.forEach(category -> {
            ScAttachment scAttachment =scAttachmentRepository.findFirstByScServiceDetail_IdAndAttachment_categoryAndSiteTypeOrderByIdDesc(serviceId, category, siteType);
            if(scAttachment!=null)scAttachments.add(scAttachment);
        });

        return  scAttachments.stream()
                .map(e -> e.getAttachment())
                .map(AttachmentBean::mapToBean)
                .collect(Collectors.toList());
    }

    /**
     * This method maps the Attachment to AttachmentBean.class
     *
     * @param attachments
     * @return List<AttachmentBean>
     */
    private List<AttachmentBean> mapToAttachmentBean(List<Attachment> attachments) {
        return attachments.stream()
                .map(AttachmentBean::mapToBean)
                .collect(Collectors.toList());
    }


    /**
     * Returns list of attachments
     *
     * @param attachmentIds
     * @return List<Attachment>
     */
    private List<Attachment> getListOfAttachments(List<Integer> attachmentIds) {
        return Optional.ofNullable(attachmentRepository.findAllByIdInOrderByIdDesc(attachmentIds))
                .orElse(ImmutableList.of());
    }

    /**
     * Returns list of attachmentIds for particular serviceId
     *
     * @param serviceId
     * @return List<AttachmentIds>
     */
    private List<Integer> fetchAttachmentIds(Integer serviceId) {
        return Optional.ofNullable(scAttachmentRepository.findAllByScServiceDetail_Id(serviceId))
                .orElseThrow(() -> new TclCommonRuntimeException(ResponseResource.RES_NO_DATA, ResponseResource.R_CODE_NOT_FOUND))
                .stream()
                .map(ScAttachment::getAttachment)
                .map(Attachment::getId)
                .collect(Collectors.toList());
    }

    /**
     * This method updates Task status with task as input
     * <p>
     * updateTaskStatus
     *
     * @param task
     * @return
     */
    private Task updateTaskStatus(Task task) {
        task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
        task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
        return taskRepository.save(task);
    }

    /**
     * This method is used to save and update attachment details
     *
     * @param validateSupportingDocBean
     * @return List<AttachmentBean>
     * @throws TclCommonException
     */
    @Transactional(readOnly = false)
    public ValidateSupportingDocBean updateAndSaveAttachment(ValidateSupportingDocBean validateSupportingDocBean) throws TclCommonException {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> componentMap = new HashMap<>();
        Map<String, String> serviceMap = new HashMap<>();
        Boolean isValidDocuments = true;
        if (Objects.nonNull(validateSupportingDocBean.getTaskId()) ) {
            Task task = getTaskByIdAndWfTaskId(validateSupportingDocBean.getTaskId(),validateSupportingDocBean.getWfTaskId());
            if (Objects.nonNull((validateSupportingDocBean.getTaskId())) && Objects.nonNull(validateSupportingDocBean.getDocuments()) && !validateSupportingDocBean.getDocuments().isEmpty()) {
                for (AttachmentBean attachmentBean : validateSupportingDocBean.getDocuments()) {
                    String isVerified = StringUtils.trimToEmpty(attachmentBean.getVerified());
                    if (isVerified.equalsIgnoreCase("N")) isValidDocuments = false;

                    if (Objects.isNull(attachmentBean.getId())) {    //Making an entry of attachment requested from customer
                        Attachment attachment = UploadAttachmentAsRequested(attachmentBean);
                        makeEntryInScAttachment(task, attachment.getId());
                    } else
                        saveAttachment(attachmentBean,task);
                }
                map.put(task.getMstTaskDef().getKey() + VALIDATE_ATTACHMENT, Utils.convertObjectToJson(validateSupportingDocBean));
            }
            map.put("isValidDocuments", isValidDocuments);

            if(validateSupportingDocBean.getDeemedAcceptanceApplicable()!=null && validateSupportingDocBean.getDeemedAcceptanceApplicable().equalsIgnoreCase("No")) {
                map.put("deemedAcceptanceDuration", "P300D");
            }
            componentMap.put("deemedAcceptanceApplicable", validateSupportingDocBean.getDeemedAcceptanceApplicable());
            componentMap.put("billFreePeriod", validateSupportingDocBean.getBillFreePeriod());
            componentMap.put("poDocType", validateSupportingDocBean.getPoDocType());
            componentMap.put("taxExemption",validateSupportingDocBean.getTaxExemption());
            componentMap.put("PO_NUMBER", validateSupportingDocBean.getCustomerPoNumber());
            componentMap.put("PO_DATE", validateSupportingDocBean.getCustomerPoDate());
            serviceMap.put("TAX_EXCEMPTED_REASON",validateSupportingDocBean.getTaxExemptionReason());

            componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceMap, "SITE_PROPERTIES");
//            componentAndAttributeService.updateAttributes(task.getServiceId(),componentMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
            flowableBaseService.taskDataEntry(task,validateSupportingDocBean,map);
        }
        return validateSupportingDocBean;
    }

    private Attachment UploadAttachmentAsRequested(AttachmentBean attachmentBean) {
        LOGGER.info("Uploading requested attachment with details {}",attachmentBean.toString());
        Attachment attachment = new Attachment();
        attachment.setCategory(attachmentBean.getCategory());
        attachment.setType(attachmentBean.getType());
        attachment.setIsActive("Y");
        attachment.setVerified("N");
        attachment.setCreatedBy(Utils.getSource());
        attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        attachment.setVerificationFailureReason(attachmentBean.getVerificationFailureReason());
        return attachmentRepository.save(attachment);
    }

    /**
     * save the attachment with updates values
     *
     * @param attachmentBean
     */
    public void saveAttachment(AttachmentBean attachmentBean, Task task) {

        Optional<Attachment> optAttachment = attachmentRepository.findById(attachmentBean.getId());
        if (optAttachment.isPresent()) {
            Attachment attachment = optAttachment.get();
            updateAttachmentData(attachment, attachmentBean);
            attachmentRepository.save(attachment);
            if (attachmentBean.getIsNew() != null && attachmentBean.getIsNew().equalsIgnoreCase("Y")) {
                Optional<ScServiceDetail> optionalScServiceDetails = scServiceDetailRepository
                        .findById(task.getServiceId());
                if (optionalScServiceDetails.isPresent()) {
                    ScServiceDetail scServiceDetail = optionalScServiceDetails.get();
                    ScAttachment scAttachment = new ScAttachment();
                    scAttachment.setAttachment(attachment);
                    scAttachment.setScServiceDetail(scServiceDetail);
                    scAttachment.setIsActive("Y");
//					scAttachment.setSiteType(task.getSiteType() == null ? "A" : task.getSiteType());
                    scAttachment.setOrderId(
                            scServiceDetail.getScOrder() != null ? scServiceDetail.getScOrder().getId() : null);
                    scAttachmentRepository.save(scAttachment);
                }
            }
        }

    }

    /**
     * @param attachment
     * @param attachmentBean
     * @return Attachment
     */

    private Attachment updateAttachmentData(Attachment attachment, AttachmentBean attachmentBean) {
        attachment.setCategory(attachmentBean.getCategory());
        attachment.setType(attachmentBean.getType());
        attachment.setVerified(attachmentBean.getVerified());
        attachment.setVerificationFailureReason(attachmentBean.getVerificationFailureReason());
        attachment.setUpdatedBy(Utils.getSource());
        attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

        return attachment;
    }

    /**
     * if attachment Id is present, api will update attachment details
     * if attachment Id is not present, new attachment record will be created
     *
     * @param attachmentBean
     * @return ServiceResponse
     */
    @Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
    public ServiceResponse updateAttachmentStorageUrl(AttachmentBean attachmentBean) {
        Objects.requireNonNull(attachmentBean, "{} cannot be null");
        ServiceResponse serviceResponse = new ServiceResponse();
        AttachmentBean savedAttachment = Objects.nonNull(attachmentBean.getId()) ? updateAttachment(attachmentBean) : saveNewAttachment(attachmentBean);
        serviceResponse.setUrlPath(savedAttachment.getStoragePathUrl());
        serviceResponse.setDocumentId(savedAttachment.getId().toString());
        return serviceResponse;
    }

    private AttachmentBean updateAttachment(AttachmentBean attachmentBean) {
        return attachmentRepository.findById(attachmentBean.getId())
                .map(attachment -> saveAttachment(attachment, attachmentBean.getStoragePathUrl()))
                .map(AttachmentBean::mapToBean)
                .get();
    }
    private AttachmentBean saveNewAttachment(AttachmentBean attachmentBean) {
        Attachment attachment = new Attachment();
        attachment.setName(attachmentBean.getName());
        attachment.setCategory(attachmentBean.getCategory());
        attachment.setType(attachmentBean.getCategory());
        attachment.setUriPathOrUrl(attachmentBean.getStoragePathUrl());
        attachment.setCreatedBy(Utils.getSource());
        attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        return Optional.ofNullable(attachmentRepository.save(attachment))
                .map(AttachmentBean::mapToBean)
                .get();
    }

    private Attachment saveAttachment(Attachment attachment, String url) {
        attachment.setUriPathOrUrl(url);
        attachment.setUpdatedBy(Utils.getSource());
        attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        return attachmentRepository.save(attachment);
    }

    /**
     * This method is use to save attachment
     * if swift is enabled, file will be saved using Object(Object Storage)
     * if swift is not enabled, file will be saved using File Storage
     *
     * @param file
     * @param //customerLeId
     * @param attachmentId
     * @param attachmentCategory
     * @return AttachmentBean
     * @throws IOException
     * @throws TclCommonException
     * @author Vishesh Awasthi
     */
    @Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
    public AttachmentBean uploadAttachment(MultipartFile file, Integer attachmentId, String attachmentCategory) throws TclCommonException, IOException {
        LOGGER.info("uploadAttachment file name:{}",file.getName());
        LOGGER.info("uploadAttachment file name original:{}",file.getOriginalFilename());

        return uploadAttachmentComponent.uploadAttachment(file, attachmentId,attachmentCategory);
    }

    /**
     * This will download attachment for given attachment Id
     *
     * @param attachmentId
     * @param response
     * @return
     */
    public String downloadAttachment(Integer attachmentId, HttpServletResponse response) throws TclCommonException {
        Optional<Attachment> storagePath = attachmentRepository.findById(attachmentId);
        if (!swiftApiEnabled.equalsIgnoreCase("true")) {
            return Optional.ofNullable(Paths.get(storagePath.get().getUriPathOrUrl()))
                    .map(path -> buildResponse(path, response))
                    .get();
        } else {
            String tempPathForAttachment = getTempPathForAttachment(storagePath.get().getName(),storagePath.get().getUriPathOrUrl());
            LOGGER.info("Temp Path for attachment : {}",tempPathForAttachment);
            return tempPathForAttachment;
        }
    }

    private String buildResponse(Path path, HttpServletResponse response) {
        response.reset();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Expires" + CommonConstants.COLON, "0");
        response.setHeader("Content-Disposition", path.toString());
        try {
            Files.copy(path, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new TclCommonRuntimeException("Exception occurred while downloading attachment", R_CODE_ERROR);
        }
        return path.toString();
    }

    private String getTempPathForAttachment(String fileName,String containerName) throws TclCommonException {
        return fileStorageService.getTempDownloadUrl(
                fileName, Long.parseLong(tempDownloadUrlExpiryWindow),
                containerName,false);
    }


    /**
     * Sets Header values
     *
     * @param file
     * @return
     */
    public ResponseEntity<Resource> settingHeaderValues(Resource file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "filename=" + file.getFilename());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).body(file);
    }

    /**
     * This method is used to get task based on task Id
     * <p>
     * getTask
     *
     * @param taskId
     * @return
     */

    private Task getTask(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.TASK_NOT_FOUND, R_CODE_ERROR));
    }

    /**
     * list Mst Document for give service Id and type.
     *
     * @param serviceId
     * @param type
     * @return List<MstAppointmentDocumentBean>
     */
    public List<MstAppointmentDocumentBean> getMstAppointmentForServiceAndType(final Integer serviceId, String type) {
        List<MstAppointmentDocumentBean> lAppointmentDocumentBeans = null;
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(type);

        Appointment appointment = appointmentRepository
                .findFirstByServiceIdAndAppointmentTypeOrderByIdDesc(serviceId, type).orElse(null);

        if (appointment != null) {
            lAppointmentDocumentBeans = getMstDocIds(appointment).stream().map(MstAppointmentDocumentBean::mapToBean)
                    .collect(Collectors.toList());
        }

        return lAppointmentDocumentBeans;

    }

    /**
     * returns list of appointment document IDs for give appointment.
     *
     * @param appointment
     * @return List<Ids>
     */
    private List<MstAppointmentDocuments> getMstDocIds(Appointment appointment) {
        return appointmentDocumentsRepository.findByAppointment(appointment)
                .stream()
                .map(AppointmentDocuments::getMstAppointmentDocuments)
                .collect(Collectors.toList());
    }

}
