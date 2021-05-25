package com.tcl.dias.l2oworkflowutils.mfutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflowutils.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Mf Attachment Util class
 *
 * @author krutsrin
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class MfAttachmentUtil {


	    public static final Logger LOGGER = LoggerFactory.getLogger(MfAttachmentUtil.class);

	    @Value("${file.upload.base.dir}")
	    String fileUploadBaseDir;

	    @Autowired
	    MQUtils mqUtils;

	    @Value("${attachment.queue}")
	    String attachmentQueue;

	    @Value("${file.download.queue}")
	    String fileDownloadQueue;

	    @Value("${attachment.requestId.queue}")
	    String attachmentRequestIdQueue;

	    @Autowired
	    FileStorageService fileStorageService;

	    @Value("${temp.download.url.expiryWindow}")
	    String tempDownloadUrlExpiryWindow;

	    /**
	     * Save Attachment via file Storage
	     *
	     * @param fileType
	     * @param file
	     * @return {@link Integer}
	     */
	    public Integer saveAttachment(String fileType, MultipartFile file) throws TclCommonException {
	        Integer attachmentId = null;
	        String folderName = getFolderName(fileType);
	        File folder = new File(folderName);
	        if (!folder.exists()) {
	            folder.mkdirs();
	        }
	        try {
	            Path path = Paths.get(folderName);
	            Long filePath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
	            if (Objects.nonNull(filePath)) {
	                AttachmentBean attachmentBean = new AttachmentBean();
	                attachmentBean.setPath(folderName);
	                attachmentBean.setFileName(file.getOriginalFilename());
	                String attachmentRequest = toJson(attachmentBean);
	                LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :",
	                        MDC.get(CommonConstants.MDC_TOKEN_KEY));
	                attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
	            	if (attachmentId==null || attachmentId==-1) {
	                    throw new TclCommonRuntimeException(ExceptionConstants.MF_L20_REPORT_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	                }
	            }
	        } catch (IOException e) {
	            throw new TclCommonRuntimeException(ExceptionConstants.FILE_UPLOAD_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	        } catch (TclCommonException e) {
	            throw new TclCommonException(ExceptionConstants.QUEUE_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	        }
	        return attachmentId;
	    }

	    /**
	     * Get folder Name based on file type
	     *
	     * @param fileType
	     * @return {@link String}
	     */
	    private String getFolderName(String fileType) {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	        return String.format("%s/%s/%s", fileUploadBaseDir, fileType, now.format(formatter));
	    }

	    /**
	     * Method to save attachment in object storage
	     *
	     * @param requestId
	     * @param url
	     * @return {@link Integer}
	     */
	    public Integer saveObjectAttachment(String requestId, String url) throws TclCommonException {
	        AttachmentBean attachmentBean = new AttachmentBean();
	        attachmentBean.setPath(url);
	        attachmentBean.setFileName(requestId);
	        String attachmentRequest = toJson(attachmentBean);
	        Integer attachmentId = null;
	        LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :",
	                MDC.get(CommonConstants.MDC_TOKEN_KEY));
	        try {
	            attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
	            if (attachmentId==null || attachmentId==-1) {
	                throw new TclCommonRuntimeException(ExceptionConstants.MF_L20_REPORT_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	            }
	        } catch (TclCommonException e) {
	            throw new TclCommonException(ExceptionConstants.QUEUE_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	        }
	        return attachmentId;
	    }

	    /**
	     * Method to fetch Attachment Resource
	     *
	     * @param attachmentId
	     * @return {@link Resource}
	     * @throws TclCommonException
	     */
	    public Optional<Resource> getAttachmentResource(Integer attachmentId) throws TclCommonException {
	        String attachmentFilePath = getAttachmentPath(attachmentId);
	        return Optional.ofNullable(attachmentFilePath).flatMap(filePath -> {
	            File[] files = new File(filePath).listFiles();
	            String attachmentPath = null;
	            if (Objects.isNull(files)) {
	            	LOGGER.debug("Files are not available in the location...");
	                throw new TclCommonRuntimeException(ExceptionConstants.MF_L20_REPORT_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	            }
	            for (File file : files) {
	                if (file.isFile()) {
	                    attachmentPath = file.getAbsolutePath();
	                }
	            }
	            Resource resource = toResource(attachmentPath);
	            if (resource != null && (resource.exists() || resource.isReadable())) {
	                return Optional.of(resource);
	            } else {
	                return Optional.empty();
	            }
	        });
	    }

	    /**
	     * Method to get attachment Path
	     *
	     * @param attachmentId
	     * @return {@link String}
	     * @throws TclCommonException
	     */
	    private String getAttachmentPath(Integer attachmentId) throws TclCommonException {
	        LOGGER.info("MDC Filter token value in before Queue call fetchAttachmentResource {} :",
	                MDC.get(CommonConstants.MDC_TOKEN_KEY));
	        String attachmentPath = null;
	        try {
	        	
	        	LOGGER.info(" before file download Queue call");
	        	Object response = mqUtils.sendAndReceive(fileDownloadQueue,
	                    String.valueOf(attachmentId));
	        	LOGGER.info("After file download Queue call {}",(String) response);

	            attachmentPath =(String) response;
	            if (StringUtils.isBlank(attachmentPath)) {
	                throw new TclCommonRuntimeException(ExceptionConstants.MF_L20_REPORT_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	            }
	        } catch (TclCommonException e) {
	            throw new TclCommonException(ExceptionConstants.QUEUE_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	        }
	        return attachmentPath;
	    }

	    /**
	     * Method to fetch Resource from file path
	     *
	     * @param path
	     * @return {@link Resource}
	     */
	    private static Resource toResource(String path) {
	        try {
	            Path attachmentLocation = Paths.get(path);
	            return new UrlResource(attachmentLocation.toUri());
	        } catch (Exception e) {
	            LOGGER.warn(String.format("Error occurred while reading attachment file: %s", path), e);
	            return null;
	        }
	    }

	    /**
	     * Method to get attachment resource via object
	     *
	     * @param attachmentId
	     * @return
	     * @throws TclCommonException
	     */
	    public String getObjectStorageAttachmentResource(Integer attachmentId) throws TclCommonException {
	        String tempDownloadUrl = null;
	        try {
	            LOGGER.info("MDC Filter token value in before Queue call attachmentRequestIdQueue {} :",
	                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
	            String response = ((String) mqUtils.sendAndReceive(attachmentRequestIdQueue, String.valueOf(attachmentId)));
	            AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response, AttachmentBean.class);
	            if (Objects.nonNull(attachmentBean)) {
	                tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(), Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
	            }
	        } catch (TclCommonException e) {
	            throw new TclCommonException(ExceptionConstants.QUEUE_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	        }
	        return tempDownloadUrl;
	    }

	

/**
 * Convert from object to json
 *
 * @param object
 * @return String
 */
public static String toJson(Object object) {
	String json = null;
	try {
		json = Utils.convertObjectToJson(object);
	} catch (Exception e) {
		LOGGER.debug(" Error while converting to json", e);
		Throwables.propagate(e);
	}
	return json;
}

/**
 * Convert from json to object
 *
 * @param jsonStr
 * @param valueType
 * @param           <T>
 * @return
 */


public static <T> T fromJson(String jsonStr, Class<T> valueType) {
	T object = null;
	try {
		object = new ObjectMapper().readValue(jsonStr, valueType);
	} catch (Exception e) {
		LOGGER.error( "Inside fromJson method",e);
		Throwables.propagate(e);
	}
	return object;
}

}
