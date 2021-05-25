package com.tcl.dias.customer.service.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.customer.constants.ExceptionConstants;
import com.tcl.dias.customer.entity.entities.Attachment;
import com.tcl.dias.customer.entity.entities.Customer;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.repository.AttachmentRepository;
import com.tcl.dias.customer.entity.repository.CustomerLegalEntityRepository;
import com.tcl.dias.customer.entity.repository.CustomerRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This Class contains the attachment service details
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class AttachmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentService.class);

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerLegalEntityRepository customerLegalEntityRepository;

	@Autowired
	FileStorageService fileStorageService;

	/**
	 * 
	 * processAttachment- This method process the attachment upload
	 * 
	 * @param attachmentRequest
	 * @return Integer
	 * @throws TclCommonException
	 */
	public Integer processAttachment(AttachmentBean attachmentRequest) throws TclCommonException {
		try {
			if (Objects.isNull(attachmentRequest)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			Attachment attachment = new Attachment();
			attachment.setCreatedTime(new Timestamp(new Date().getTime()));
			attachment.setDisplayName(attachmentRequest.getFileName());
			attachment.setName(attachmentRequest.getFileName());
			attachment.setUriPathOrUrl(attachmentRequest.getPath());
			attachmentRepository.save(attachment);
			LOGGER.info("Attachment Saved with the attachment id {}",attachment.getId());
			return attachment.getId();
		} catch (Exception ex) {
			LOGGER.error("Error in attachment {}",ex);
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}

	}

	/**
	 * 
	 * getDownloadPath -This method gets the download path
	 * 
	 * @param attachmentId
	 * @return String
	 * @throws TclCommonException
	 */
	public String getDownloadPath(Integer attachmentId) throws TclCommonException {
		String response = "";
		try {
			Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
			ObjectMapper mapper = new ObjectMapper();
			if (attachment.isPresent()) {
				response = mapper.writeValueAsString(attachment.get().getUriPathOrUrl());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return response;
	}

	/**
	 * getRequestId - this method is used to get the name for the attachment to
	 * generate the temporary download url
	 * 
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */

	public AttachmentBean getRequestId(Integer attachmentId) throws TclCommonException {
		AttachmentBean response = new AttachmentBean();
		try {
			Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
			ObjectMapper mapper = new ObjectMapper();
			if (attachment.isPresent()) {
				// response = mapper.writeValueAsString(attachment.get().getName());
				response.setPath(attachment.get().getUriPathOrUrl());
				response.setFileName(attachment.get().getName());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return response;
	}

	/**
	 * Update File Path for Country Specific documents
	 *
	 * @param listenerBean
	 * @return {@link ObjectStorageListenerBean}
	 */
	public ObjectStorageListenerBean updateFilePathForCountryDocuments(ObjectStorageListenerBean listenerBean) {
		listenerBean.getAttachmentIds().stream().forEach(attachmentId -> {
			Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
			if (attachment.isPresent()) {
				File[] files = new File(attachment.get().getUriPathOrUrl()).listFiles();
				for (File file : files) {
					if (file.isFile()) {
						try {
							InputStream inputStream = new FileInputStream(file);
							StoredObject storedObject = fileStorageService.uploadFiles(file.getName(),
									"GSIP_COUNTRY_TEMPLATE_FILES", inputStream);

							String[] pathArray = storedObject.getPath().split("/");
							attachment.get().setUriPathOrUrl(pathArray[1]);
							attachment.get().setName(storedObject.getName());
							attachment.get().setDisplayName(storedObject.getName());
							attachmentRepository.save(attachment.get());

						} catch (Exception e) {

							throw new TclCommonRuntimeException(ExceptionConstants.GSIP_DOCUMENT_ERROR,
									ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
						}
					}
				}
			}
		});
		return listenerBean;
	}

	/**
	 * Update File Path For Files
	 *
	 * @param omsAttachRequest
	 * @throws TclCommonException
	 */
	@Transactional
	public void updateFilePathForFiles(ObjectStorageListenerBean[] omsAttachRequest) throws TclCommonException {
		if (omsAttachRequest == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		List<ObjectStorageListenerBean> omsAttachmentRequest = new ArrayList(Arrays.asList(omsAttachRequest));
		omsAttachmentRequest.stream().forEach(objStorageListenerBean -> {

			Optional<Attachment> attachment = attachmentRepository.findById(objStorageListenerBean.getAttachmentId());
			if (attachment != null) {
				File[] files = new File(attachment.get().getUriPathOrUrl()).listFiles();
				if (files != null && files.length > 0) {
					for (File file : files) {
						Optional<Customer> customer = customerRepository
								.findById(objStorageListenerBean.getCustomerId());
						Optional<CustomerLegalEntity> customerLegalEntity = customerLegalEntityRepository
								.findById(objStorageListenerBean.getCustomerLeId());
						if (file.isFile() && customer.get().getCustomerCode() != null
								&& customerLegalEntity.get().getCustomerLeCode() != null) {
							try {
								InputStream inputStream = new FileInputStream(file);
								StoredObject storedObject = fileStorageService.uploadObject(file.getName(), inputStream,
										customer.get().getCustomerCode(),
										customerLegalEntity.get().getCustomerLeCode());

								String[] pathArray = storedObject.getPath().split("/");

								attachment.get().setUriPathOrUrl(pathArray[1]);
								attachment.get().setName(storedObject.getName());
								attachment.get().setDisplayName(storedObject.getName());
								attachmentRepository.save(attachment.get());
								LOGGER.info("Attachmnt Id" + attachment.get().getId());

							} catch (Exception e) {
								throw new TclCommonRuntimeException(e.getMessage(), e);
							}
						}
					}
				}
			}

		});
	}

	public Map<String, Object> getAttachmentDetails(Integer attachmentId) throws TclCommonException {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
			if (attachment.isPresent()) {
				LOGGER.info("Attachment found");
				response.put("NAME", attachment.get().getName());
				response.put("CREATED_BY", attachment.get().getCreatedBy());
				response.put("CREATED_TIME", attachment.get().getCreatedTime());
				response.put("DISPLAY_NAME", attachment.get().getDisplayName());
				response.put("EXPIRY_WINDOW", attachment.get().getExpiryWindow());
				response.put("ATTACHMENT_ID", attachment.get().getId());
				response.put("URL_PATH", attachment.get().getUriPathOrUrl());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return response;
	}

	/**
	 * Get Attachment Details from attachment Id's
	 *
	 * @param attachmentIds
	 * @return {@link List<PartnerDocumentBean>}
	 */
	public List<PartnerDocumentBean> getAttachmentDetails(List<Integer> attachmentIds) {
		List<PartnerDocumentBean> partnerDocumentBeans = new ArrayList<>();
		attachmentIds.forEach(attachmentId -> attachmentRepository.findById(attachmentId).map(attachment -> {
			PartnerDocumentBean bean = new PartnerDocumentBean();
			bean.setId(attachment.getId());
			bean.setName(attachment.getName());
			partnerDocumentBeans.add(bean);
			return bean;
		}).orElse(null));
		return partnerDocumentBeans;
	}
	
	/**
	 * 
	 * processAttachment- This method process the attachment upload
	 * 
	 * @param attachmentRequest
	 * @return Integer
	 * @throws TclCommonException
	 */
	public Attachment processAttachmentSDD(AttachmentBean attachmentRequest) throws TclCommonException {
		try {
			if (Objects.isNull(attachmentRequest)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			Attachment attachment = new Attachment();
			attachment.setCreatedTime(new Timestamp(new Date().getTime()));
			attachment.setDisplayName(attachmentRequest.getFileName());
			attachment.setName(attachmentRequest.getFileName());
			attachment.setUriPathOrUrl(attachmentRequest.getPath());
			attachmentRepository.save(attachment);
			LOGGER.info("Attachment Saved with the attachment id {}",attachment.getId());
			return attachment;
		} catch (Exception ex) {
			LOGGER.error("Error in attachment {}",ex);
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}

	}
}
