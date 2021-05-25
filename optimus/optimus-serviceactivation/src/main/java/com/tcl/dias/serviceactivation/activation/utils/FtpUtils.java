package com.tcl.dias.serviceactivation.activation.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.Vector;

import org.apache.commons.lang3.StringEscapeUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;

/**
 * This file contains the FtpUtils.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class FtpUtils {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FtpUtils.class);

	@Value("${postvalidation.ftp.hostname}")
	String hostName;

	@Value("${postvalidation.ftp.port}")
	Integer portNumber;

	@Value("${postvalidation.ftp.username}")
	String username;

	@Value("${postvalidation.ftp.password}")
	String password;

	@Value("${postvalidation.ftp.logpath}")
	String logpath;

	@Value("${postvalidation.ftp.localpath}")
	String logLocalpath;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	ScAttachmentRepository scAttachmentRepository;
	
	@Autowired
    ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	MQUtils mqUtils;

	public Boolean uploadPostValidationFiles(String fileName, String orderCode, String serviceId) {
		fileName = fileName.replace(".", "");
		LOGGER.info("Entering postValidation upload to the server with filename {}", fileName);
		Boolean status = true;
		Channel channel = null;
		ChannelSftp cFTP = null;
		Session session = null;
		BufferedWriter buffer = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, hostName, portNumber);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(password);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			cFTP = (ChannelSftp) channel;
			@SuppressWarnings("unchecked")
			Vector<LsEntry> listOfFiles = cFTP.ls(fileName);
			for (int i = 0; i < listOfFiles.size(); i++) {
				LOGGER.info(listOfFiles.get(i).getLongname());
			}
			if (listOfFiles.isEmpty()) {
				LOGGER.info("The mentioned fileName is not found {} so exiting with status false", fileName);
				return false;
			}
			InputStream stream = cFTP.get(fileName);
			//fileName = fileName+".txt";
			LOGGER.info("fileName {} for serviceId={} swiftApiEnabled={}", fileName,serviceId,swiftApiEnabled);
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				StoredObject storedObject = fileStorageService.uploadObjectWithExten(".txt", stream, orderCode, serviceId);
				if(storedObject!=null) {
					LOGGER.info("fileName {} for serviceId={} storedObject={}", storedObject.getName(),serviceId,storedObject);
					String[] pathArray = storedObject.getPath().split("/");
					persistAttachment(storedObject.getName(), serviceId, pathArray[1]);
				}else {
					LOGGER.info("storedObjectisempty for serviceId={}", fileName,serviceId);
				}
			} else {
				// Get the file and save it somewhere
				String localFileName=serviceId+"-"+(new Date().getTime()) + ".txt";
				String fileFullPath = logLocalpath +localFileName;
				File file = new File(fileFullPath);
				file.getParentFile().mkdirs();
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				FileWriter fwriter = new FileWriter(fileFullPath);
				buffer = new BufferedWriter(fwriter);
				String line;
				while ((line = br.readLine()) != null) {
					buffer.write(line);
					buffer.newLine();
				}
				buffer.close();
				persistAttachment(localFileName, serviceId, fileFullPath);
			}
		} catch (Exception e) {
			LOGGER.error("Error in downloading the file from ftp", e);
			status = false;
		} finally {
			if (cFTP != null)
				cFTP.disconnect();
			if (session != null)
				session.disconnect();
			if (channel != null)
				channel.disconnect();
		}
		return status;
	}

	/**
	 * persistAttachment
	 * 
	 * @param fileName
	 * @param serviceId
	 * @param fileFullPath
	 */
	private void persistAttachment(String fileName, String serviceCode, String fileFullPath) {
		Attachment attachment = new Attachment();
		attachment.setCreatedDate(new Timestamp(new Date().getTime()));
		attachment.setName(fileName);
		attachment.setUriPathOrUrl(fileFullPath);
		attachment.setContentTypeHeader(fileName);
		attachment.setType("POST_VALIDATION");
		attachment.setIsActive("Y");
		attachment.setCategory("E2ERESULT");
		attachmentRepository.save(attachment);
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(attachment);
		scAttachment.setAttachmentType("POST_VALIDATION");
		scAttachment.setIsActive("Y");
		scAttachment.setSiteType("A");
		scAttachment.setServiceCode(serviceCode);
		 Optional<ScServiceDetail> serviceDetail = Optional.ofNullable(scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS"));
		 if(serviceDetail.isPresent())scAttachment.setScServiceDetail(serviceDetail.get());
		scAttachmentRepository.save(scAttachment);
		LOGGER.info("Saving the attachment Id {} and sc Attachment Id {}", attachment.getId(), scAttachment.getId());
	}
}
