package com.tcl.dias.servicefulfillmentutils.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.CustomerCodeBean;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.entities.Attachment;
import com.tcl.dias.networkaugment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class will be used as component to upload documents
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class UploadAttachmentComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadAttachmentComponent.class);

    public static final String DATEFORMAT = "yyyyMMddHHmmss";

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;

    @Value("${file.upload-dir}")
    String uploadPath;

    @Value("${customercode.customerlecode.queue}")
    String getCutomerCodeAndCustomerLeCode;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    MQUtils mqUtils;


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
    public AttachmentBean uploadAttachment(MultipartFile file, Integer attachmentId, String attachmentCategory) throws TclCommonException, IOException {
        AttachmentBean attachmentBean = new AttachmentBean();
        //To enable shift change disabled to true
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            return saveAttachmentWithObjectStorage(file, attachmentBean,attachmentCategory);
        } else {
            attachmentBean = saveAttachmentWithFileStorage(file, attachmentBean, attachmentCategory);
            return Objects.nonNull(attachmentId) ? updateAttachmentStorageUrl(attachmentId, attachmentBean, file) : saveAttachment(file, attachmentBean);
        }


    }

    /**
     * Object storage
     *
     * @param file
     * @param attachmentBean
     * @param attachmentCategory 
     * @param //customerLeId
     * @return AttachmentBean
     * @throws TclCommonException
     */
    private AttachmentBean saveAttachmentWithObjectStorage(MultipartFile file, AttachmentBean attachmentBean, String attachmentCategory) throws TclCommonException {
        TempUploadUrlInfo tempUploadUrlInfo;
       // CustomerCodeBean customerCodeBean = getCustomerCodeAndCustomerLeCode(customerLeId);
       // StringBuffer productName = constructProductName(customerCodeBean);
        tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
                Long.parseLong(tempUploadUrlExpiryWindow),"O2C");
        attachmentBean.setStoragePathUrl(tempUploadUrlInfo.getTemporaryUploadUrl());
        attachmentBean.setContainerName(tempUploadUrlInfo.getContainerName());
        attachmentBean.setName(tempUploadUrlInfo.getContainerName() + "/" + tempUploadUrlInfo.getRequestId());
        attachmentBean.setSwiftEnabled(true);
        attachmentBean.setCategory(attachmentCategory);
        return attachmentBean;
    }

    /**
     * File Storage
     *
     * @param file
     * @param attachmentBean
     * @param attachmentCategory
     * @return AttachmentBean
     * @throws IOException
     */
    private AttachmentBean saveAttachmentWithFileStorage(MultipartFile file, AttachmentBean attachmentBean, String attachmentCategory) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);
        File fileFolder = new File(uploadPath + now.format(formatter));
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        Path path = Paths.get(uploadPath + now.format(formatter));
        Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
        attachmentBean.setStoragePathUrl(fileFolder + File.separator + file.getOriginalFilename());
        
        attachmentBean.setName(file.getOriginalFilename());
        attachmentBean.setCategory(attachmentCategory);
        attachmentBean.setSwiftEnabled(false);
        return attachmentBean;
    }

    /**
     * update attachment table for give attachment id
     *
     * @param attachmentId
     * @param attachmentBean
     * @param file
     * @return AttachmentBean
     */
    private AttachmentBean updateAttachmentStorageUrl(Integer attachmentId, AttachmentBean attachmentBean, MultipartFile file) {
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

    /**
     * saves record in attachment table
     *
     * @param file
     * @param attachmentBean
     * @return AttachmentBean
     */
    private AttachmentBean saveAttachment(MultipartFile file, AttachmentBean attachmentBean) {
        Attachment attachment = new Attachment();
        attachment.setName(attachmentBean.getName());
        attachment.setCategory(attachmentBean.getCategory());
        attachment.setType(attachmentBean.getType());
        attachment.setUriPathOrUrl(attachmentBean.getStoragePathUrl());
        attachment.setContentTypeHeader("");
        attachment.setCreatedBy(Utils.getSource());
        attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        return Optional.ofNullable(attachmentRepository.save(attachment))
                .map(AttachmentBean::mapToBean)
                .get();
    }

    /**
     * Queue call to customer db to fetch customer code and customerLe Code
     *
     * @param customerLeId
     * @return CustomerCodeBean
     * @throws TclCommonException
     */
    public CustomerCodeBean getCustomerCodeAndCustomerLeCode(Integer customerLeId) throws TclCommonException {
        LOGGER.info("Sending request to getCustomerCodeAndCustomerLeCode:: {}", customerLeId);
        String response = (String) mqUtils.sendAndReceive(getCutomerCodeAndCustomerLeCode, customerLeId.toString());
        LOGGER.info("Received response from getCustomerCodeAndCustomerLeCode:: {}", response);
        CustomerCodeBean customerCodeBean = new CustomerCodeBean();
        if (StringUtils.isNotBlank(response))
            customerCodeBean = Utils.convertJsonToObject(response, CustomerCodeBean.class);
        return customerCodeBean;
    }

}
