package com.tcl.dias.oms.gsc.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class GscAttachmentHelper {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscAttachmentHelper.class);

	@Value("${file.upload.base.dir}")
	String fileUploadBaseDir;

	@Value("${attachment.queue}")
	String attachmentQueue;

	@Value("${file.download.queue}")
	String fileDownloadQueue;

	@Autowired
	MQUtils mqUtils;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${swift.api.container}")
	String swiftApiContainer;

	@Value("${attachment.requestId.queue}")
	String attachmentRequestIdQueue;

	public Integer saveAttachment(MultipartFile file, String... fileNameParts) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String folderName = String.format("%s/%s", fileUploadBaseDir, now.format(formatter));
		String fileNameFragment = Joiner.on("_").join(Optional.ofNullable(fileNameParts).orElse(new String[0]));
		String newFileName = String.format("%s_%s", fileNameFragment, file.getOriginalFilename());
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		try {
			Path path = Paths.get(folderName);
			Long filePath = Files.copy(file.getInputStream(), path.resolve(newFileName));
			if (Objects.nonNull(filePath)) {
				AttachmentBean attachmentBean = new AttachmentBean();
				attachmentBean.setPath(folderName);
				attachmentBean.setFileName(newFileName);
				String attachmentRequest = GscUtils.toJson(attachmentBean);
				LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				return (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return null;
	}

	/**
	 * Method to save attachment in object storage
	 *
	 * @param configId
	 * @param referenceType
	 * @param requestId
	 * @param url
	 * @return {@link Integer}
	 */
	public Integer saveObjectAttachment(String requestId, String url) {
		AttachmentBean attachmentBean = new AttachmentBean();
		attachmentBean.setPath(url);
		attachmentBean.setFileName(requestId);
		String attachmentRequest = GscUtils.toJson(attachmentBean);
		LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		try {
			return (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
		} catch (TclCommonException e) {
			Throwables.propagate(e);
		}
		return null;
	}

	private static Resource toResource(String path) {
		try {
			Path attachmentLocation = Paths.get(path);
			return new UrlResource(attachmentLocation.toUri());
		} catch (Exception e) {
			LOGGER.warn(String.format("Error occurred while reading attachment file: %s", path), e);
			return null;
		}
	}

	public Optional<Resource> fetchAttachmentResource(Integer attachmentId)
			throws TclCommonException, IllegalArgumentException {
		/*if (swiftApiEnabled.equalsIgnoreCase("true")) {
			String tempDownloadUrl = null;
			LOGGER.info("MDC Filter token value in before Queue call attachmentRequestIdQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = ((String) mqUtils.sendAndReceive(attachmentRequestIdQueue, String.valueOf(attachmentId),
					MDC.get(CommonConstants.MDC_TOKEN_KEY)));
			AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response, AttachmentBean.class);
			if (attachmentBean != null) {
				tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
						Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath());
			}
			Resource resource = new ByteArrayResource(tempDownloadUrl.getBytes());
			return Optional.of(resource);
		} else */
			// need double conversion since MQ responds with a String wrapped in double
			// quotes
			LOGGER.info("MDC Filter token value in before Queue call fetchAttachmentResource {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String attachmentFilePath = GscUtils.fromJson((String) mqUtils.sendAndReceive(fileDownloadQueue,
					String.valueOf(attachmentId)), String.class);
			return Optional.ofNullable(attachmentFilePath).flatMap(filePath -> {
				File[] files = new File(filePath).listFiles();
				String attachmentPath = null;
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
	 * Method to save file via object storage
	 *
	 * @param file
	 * @return {@link Integer}
	 */
	public Integer saveFileAttachment(InputStream inputStream,String fileName) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String folderName = String.format("%s/%s", fileUploadBaseDir, now.format(formatter));
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		try {
			Path path = Paths.get(folderName);
			Long filePath = Files.copy(inputStream, path.resolve(fileName));
			if (Objects.nonNull(filePath)) {
				AttachmentBean attachmentBean = new AttachmentBean();
				attachmentBean.setPath(folderName);
				attachmentBean.setFileName(fileName);
				String attachmentRequest = GscUtils.toJson(attachmentBean);
				LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				return (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return null;
	}
	public Optional<String> fetchObjectStorageAttachmentResource(Integer attachmentId)
			throws TclCommonException, IllegalArgumentException {
		String tempDownloadUrl = null;

			LOGGER.info("MDC Filter token value in before Queue call attachmentRequestIdQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = ((String) mqUtils.sendAndReceive(attachmentRequestIdQueue, String.valueOf(attachmentId)));
		if (StringUtils.isNotBlank(response)) {
			AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response, AttachmentBean.class);
			tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
					Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
		}
		return Optional.of(tempDownloadUrl);
	}
}
