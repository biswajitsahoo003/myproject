package com.tcl.dias.service.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.service.beans.ServiceResponse;
import com.tcl.dias.service.constants.AttachmentTypeConstants;
import com.tcl.dias.service.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * contains the services required for upload the file
 * 
 * @author SEKHAR ER
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class FilesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilesService.class);

	@Value("${file.upload-dir}")
	private String uploadFolder;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Value("${oms.attachment.queue}")
	String omsAttachmentQueue;

	@Value("${file.download.queue}")
	String downloadQueue;

	@Autowired
	MQUtils mqUtils;

	/**
	 * This method takes file as input and stores the file in specified location
	 * 
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public ServiceResponse processUploadFiles(MultipartFile file, Integer orderToLeId, Integer quoteToLeId,
			String attachmentType, List<Integer> referenceId, String referenceName)
			throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();

		validateOmsRequest(file, orderToLeId, quoteToLeId, attachmentType, referenceId,
				referenceName);

		try {

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// Get the file and save it somewhere
			String newFolder = uploadFolder + now.format(formatter);
			File filefolder = new File(newFolder);
			if (!filefolder.exists()) {
				filefolder.mkdirs();

			}
			Path path = Paths.get(newFolder);
			Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
			if (newpath != null) {
				fileUploadResponse.setFileName(file.getOriginalFilename());
				fileUploadResponse.setStatus(Status.SUCCESS);
				AttachmentBean attachmentBean = new AttachmentBean();
				attachmentBean.setPath(newFolder);
				attachmentBean.setFileName(file.getOriginalFilename());

				String attachmentrequest = Utils.convertObjectToJson(attachmentBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				Integer response = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentrequest);
				fileUploadResponse.setAttachmentId(response);
				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();

				for (Integer refId : referenceId) {

					OmsAttachBean omsAttachBean = new OmsAttachBean();

					omsAttachBean.setAttachmentId(response);
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
					omsAttachBean.setOrderLeId(orderToLeId);
					omsAttachBean.setQouteLeId(quoteToLeId);
					omsAttachBean.setReferenceId(refId);
					omsAttachBean.setReferenceName(referenceName);
					omsAttachBeanList.add(omsAttachBean);
				}
				
				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(omsAttachmentQueue, oattachmentrequest);

			}

			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.FAILED_TO_UPLOAD,ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * Downloads the file by taking attachment Id. It connects with customer
	 * microservice through rabbitmq for file path by attachment id.
	 * 
	 * getAttachments
	 * 
	 * @param quoteId
	 * @param orderId
	 * @param quoteLeId
	 * @param operationName
	 * @param attachmentId
	 * @return Resource
	 * @throws TclCommonException
	 */
	public Resource getAttachments(Integer attachmentId) throws TclCommonException {
		try {
			if (attachmentId == null || attachmentId == 0) {
				throw new TclCommonException(ExceptionConstants.ATTACHMENT_ID_MISSING, ResponseResource.R_CODE_ERROR);
			}
			LOGGER.info("MDC Filter token value in before Queue call getAttachments {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String pathResponse = (String) Utils.convertJsonToObject(
					(String) mqUtils.sendAndReceive(downloadQueue, String.valueOf(attachmentId)), String.class);
			LOGGER.info("Path received :: {}", pathResponse);
			File[] files = new File(pathResponse).listFiles();
			String attachmentPath = null;
			for (File file : files) {
				if (file.isFile()) {
					attachmentPath = file.getAbsolutePath();
					LOGGER.info("File Abs path :: {}", attachmentPath);
				}
			}
			Path attachmentLocation = Paths.get(attachmentPath);
			Resource resource = new UrlResource(attachmentLocation.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST, ResponseResource.R_CODE_ERROR);
			}
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing download malformered url {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.MALFORMED_URL_EXCEPTION,e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void validateOmsRequest(MultipartFile file, Integer orderToLeId, Integer quoteToLeId, String attachmentType, List<Integer> referenceId, String referenceName)
			throws TclCommonException {

		if (Objects.isNull(orderToLeId) && Objects.isNull(quoteToLeId)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (Objects.isNull(file)) {
			throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (Objects.isNull(referenceName)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (CollectionUtils.isEmpty(referenceId)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (Objects.isNull(attachmentType)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

}
