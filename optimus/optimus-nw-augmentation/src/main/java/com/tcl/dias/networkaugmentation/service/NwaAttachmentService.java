package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.entities.Attachment;
import com.tcl.dias.networkaugment.entity.entities.ScAttachment;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.repository.AttachmentRepository;
import com.tcl.dias.networkaugment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;

import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.AttachmentService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class NwaAttachmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NwaAttachmentService.class);

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;

    @Value("${temp.upload.url.expiryWindow}")
    Long expiryWindowInSeconds;

    @Value("${temp.download.url.expiryWindow}")
    Long tempDownloadUrlExpiryWindow;

    @Autowired
    AttachmentFileStorageService fileStorageService;

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Value("${file.upload-dir}")
    String uploadPath;

    @Value("${swift.documentservice.url}")
    String documentServiceUrl;
    
    @Value("${swift.api.container}")
    String swiftApiContainer;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @Autowired
    ScAttachmentRepository scAttachmentRepository;

    @Autowired
    TaskRepository taskRepository;

    public static final String DATEFORMAT = "yyyyMMddHHmmss";

    public Attachment uploadAttachment(String name, MultipartFile file, String category, String createdBy, Integer taskId)
            throws TclCommonException, IOException {
        TempUploadUrlInfo tempUploadUrlInfo;
        AttachmentBean attachmentBean = new AttachmentBean();
        tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
                Long.parseLong(tempUploadUrlExpiryWindow), "nwa");
        System.out.println("tempUploadUrlInfo : "+tempUploadUrlInfo);
        attachmentBean.setStoragePathUrl(tempUploadUrlInfo.getTemporaryUploadUrl());
        attachmentBean.setContainerName(tempUploadUrlInfo.getContainerName());
        attachmentBean.setName(tempUploadUrlInfo.getRequestId());
        attachmentBean.setSwiftEnabled(true);
        attachmentBean.setCategory(category);
        attachmentBean.setCreatedBy(createdBy);
        attachmentBean.setUpdatedBy(createdBy);
        System.out.println("attachmentBean : "+attachmentBean.getStoragePathUrl());
        Attachment attachment;
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            attachment = saveAttachment(file, attachmentBean);
        } else {
            attachment = saveAttachment(file, attachmentBean);
//            return Objects.nonNull(attachmentId) ? updateAttachmentStorageUrl(attachmentId, attachmentBean, file) : saveAttachment(file, attachmentBean);
        }
        System.out.println("saved attachment : "+attachment);
        Optional<Task> task = taskRepository.findById(taskId);
        Optional<ScServiceDetail> optionalScServiceDetails = scServiceDetailRepository
                .findById(task.get().getServiceId());
        if (optionalScServiceDetails.isPresent()) {
            ScServiceDetail scServiceDetail = optionalScServiceDetails.get();
            ScAttachment scAttachment = new ScAttachment();
            scAttachment.setAttachment(attachment);
            scAttachment.setScServiceDetail(scServiceDetail);
            scAttachment.setIsActive("Y");
            scAttachment.setOrderId(
                    scServiceDetail.getScOrder() != null ? scServiceDetail.getScOrder().getId() : null);
            scAttachmentRepository.save(scAttachment);
        }
        attachment.setUriPathOrUrl(tempUploadUrlInfo.getTemporaryUploadUrl());
        return attachment;
    }

    private Attachment saveAttachment(MultipartFile file, AttachmentBean attachmentBean) {

        Attachment attachment = new Attachment();
        attachment.setName(attachmentBean.getName());
        attachment.setCategory(attachmentBean.getCategory());
        attachment.setType(attachmentBean.getType());
        System.out.println("attachmentBean.getStoragePathUrl() : "+attachmentBean.getStoragePathUrl());
        String path = swiftApiContainer + CommonConstants.HYPHEN + "nwa";
        attachment.setUriPathOrUrl(path);
        attachment.setContentTypeHeader("");
        attachment.setCreatedBy(attachmentBean.getCreatedBy());
        attachment.setUpdatedBy(attachmentBean.getUpdatedBy());
        attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        attachment.setStorageUrl(documentServiceUrl);
        return attachmentRepository.save(attachment);
    }
    private AttachmentBean saveAttachmentWithFileStorage(MultipartFile file, AttachmentBean attachmentBean) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);
        File fileFolder = new File(uploadPath + now.format(formatter));
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        String fileName=file.getOriginalFilename();
//        if(attachmentCategory!=null && (attachmentCategory.equalsIgnoreCase("CUSTOMEREMAIL"))|| attachmentCategory.equalsIgnoreCase("CANCELEMAIL")) {
//            fileName=removeReq(fileName);
//        }
        Path path = Paths.get(uploadPath + now.format(formatter));
        Files.copy(file.getInputStream(), path.resolve(fileName));
        attachmentBean.setStoragePathUrl(fileFolder + File.separator + fileName);

        attachmentBean.setName(fileName);
//        attachmentBean.setCategory(attachmentCategory);
        attachmentBean.setSwiftEnabled(false);
        return attachmentBean;
    }

    private AttachmentBean updateAttachmentStorageUrl(Integer attachmentId,AttachmentBean attachmentBean, MultipartFile file) {
        attachmentRepository.findById(attachmentId)
                .ifPresent(attachment -> {
                    attachment.setName(file.getName());
                    attachment.setUriPathOrUrl(attachmentBean.getStoragePathUrl());
                    attachment.setUpdatedBy(Utils.getSource());
                    attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
                    attachmentRepository.save(attachment);
                });
        attachmentBean.setId(attachmentId);
        return attachmentBean;
    }


    public Attachment getAttachmentDetails(String attachmentId) throws TclCommonException {
        System.out.println("-----------get details------------------");
        String authToken = attachmentService.getAuthToken();

//        AttachmentBean attachmentBean = new AttachmentBean();
//        tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
//                Long.parseLong(tempUploadUrlExpiryWindow), "nwa");
//        System.out.println("tempUploadUrlInfo : "+tempUploadUrlInfo);

        Optional<Attachment> attachmentObj = attachmentRepository.findById(Integer.parseInt(attachmentId));
        Attachment attachment = attachmentObj.get();
        String url = fileStorageService.getTempDownloadUrl(attachment.getName(),expiryWindowInSeconds,attachment.getUriPathOrUrl(),false);

        //String fileName, long expiryWindowInSeconds, String containerUrl,
//			boolean isInternal
        attachment.setUriPathOrUrl(url);
        return attachment;
    }

    public Attachment updateAttachment(String name, MultipartFile file, String attachmentId) throws TclCommonException {
        TempUploadUrlInfo tempUploadUrlInfo;
        AttachmentBean attachmentBean = new AttachmentBean();
        attachmentBean.setId(Integer.parseInt(attachmentId));
        tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
                Long.parseLong(tempUploadUrlExpiryWindow), "nwa");
        attachmentBean.setStoragePathUrl(tempUploadUrlInfo.getTemporaryUploadUrl());
        attachmentBean.setContainerName(tempUploadUrlInfo.getContainerName());
        attachmentBean.setName(tempUploadUrlInfo.getContainerName() + "/" + tempUploadUrlInfo.getRequestId());
        attachmentBean.setSwiftEnabled(true);

        Attachment attachment = new Attachment();
        attachment.setId(attachmentBean.getId());
        attachment.setName(attachmentBean.getName());
        attachment.setCategory(attachmentBean.getCategory());
        attachment.setType(attachmentBean.getType());
        attachment.setUriPathOrUrl(attachmentBean.getStoragePathUrl());
        attachment.setContentTypeHeader("");
        attachment.setUpdatedBy(Utils.getSource());
        attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            return attachmentRepository.save(attachment);//saveAttachment(file, attachmentBean);
        } else {
            attachment = attachmentRepository.save(attachment);// saveAttachment(file, attachmentBean);
//            return Objects.nonNull(attachmentId) ? updateAttachmentStorageUrl(attachmentId, attachmentBean, file) : saveAttachment(file, attachmentBean);
        }
        return attachment;
    }

    public Integer deleteAttachmentById(String attachmentId) {
        System.out.println("-----------get details------------------");
        Integer attachmentIdVal = Integer.parseInt(attachmentId);
        attachmentService.getAuthToken();
//        scAttachmentRepository.deleteByAttachmentId(Integer.parseInt(attachmentId));
//        Optional<Attachment> attachment = attachmentRepository.findById(attachmentIdVal);
        List<ScAttachment> scAttachment = scAttachmentRepository.findByAtachmentId(attachmentIdVal);
        try{
            if(scAttachment.get(0)!=null){
                scAttachmentRepository.deleteById(scAttachment.get(0).getId());
                attachmentRepository.deleteById(attachmentIdVal);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        return Integer.parseInt(attachmentId);
    }

    public List<AttachmentBean> getAttachmentListByTask(Integer taskId) {
        return attachmentService.getAttachmentListByTask(taskRepository.findById(taskId). get());
    }
}


