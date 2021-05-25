package com.tcl.dias.customer.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.*;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.bean.AttachmentBean;
import com.tcl.dias.customer.bean.CallidusDetailsBean;
import com.tcl.dias.customer.bean.CallidusIncentiveRequestBean;
import com.tcl.dias.customer.bean.CallidusPartnerCommisions;
import com.tcl.dias.customer.bean.CustomerLegalEntityBean;
import com.tcl.dias.customer.bean.PartnerCommissionsResponse;
import com.tcl.dias.customer.bean.PartnerContractingAddress;
import com.tcl.dias.customer.bean.PartnerCustomerLeAttributeBean;
import com.tcl.dias.customer.bean.PartnerEndCustomerMappingBean;
import com.tcl.dias.customer.bean.PartnerMonthlyIncentive;
import com.tcl.dias.customer.bean.PartnerNNIBean;
import com.tcl.dias.customer.bean.PartnerProductClassificationBean;
import com.tcl.dias.customer.bean.SapCommissions;
import com.tcl.dias.customer.constants.DocumentConstant;
import com.tcl.dias.customer.constants.ExceptionConstants;
import com.tcl.dias.customer.constants.PartnerCustomerConstants;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityProductResponseDto;
import com.tcl.dias.customer.entity.entities.*;
import com.tcl.dias.customer.entity.repository.*;
import com.tcl.dias.customer.utils.PartnerCustomerUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.tcl.dias.common.constants.CommonConstants.COLON;
import static com.tcl.dias.common.constants.CommonConstants.COMMA;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.EMAIL;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.CUSTOMER_CONTACT_ENTITY;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.CUSTOMER_CONTACT_NAME;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.INDUSTRY;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.INDUSTRY_SUB_TYPE;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.LE_CONTACT;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.MST_ATTRIBUTE_SUPPLIER_LE_OWNER;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.NNI_ID;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.REGISTRATION_NUMBER;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.SUB_INDUSTRY;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.TYPE_OF_BUSINESS;
import static com.tcl.dias.customer.constants.PartnerCustomerConstants.WEBSITE;
import static com.tcl.dias.customer.constants.ServiceSpecificConstant.*;
import static com.tcl.dias.customer.constants.SpConstants.ACCOUNT_OWNER;
import static com.tcl.dias.customer.constants.SpConstants.ACCOUNT_OWNER_EMAILID;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * Partner related information for Partner Portal
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
@Transactional
public class PartnerCustomerService {

	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String THIRTY_FIRST_MARCH = "31-Mar-";
	public static final String FIRST_APRIL = "01-Apr-";
	public static final String DD_MMM_YYYY = "dd-MMM-yyyy";
	public static final String D_MMM_YY = "d-MMM-yy";
	public static final String CALLDUS_ELIGIBLE_INCENTIVES = "calldusEligibleIncentives";
	public static final String SAP_RECEIVED_INCENTIVES = "sapReceivedIncentives";
	private static final Logger LOGGER = LoggerFactory.getLogger(PartnerCustomerService.class);
	public static final String CUSTOMER_TYPE = "CUSTOMER TYPE";
	public static final String TCL_MAPPED_END_CUSTOMER = "TCLMappedEndCustomer";
	public static final String TEMPORARILY_MAPPED_END_CUSTOMER = "TemporarilyMappedEndCustomer";

	@Autowired
	PartnerLegalEntityRepository partnerLegalEntityRepository;

	@Autowired
	CustomerLegalEntityRepository customerLegalEntityRepository;
	
	@Autowired
	CustomerLeAttributeValueRepository customerLeAttributeValueRepository;

	@Autowired
	CallidusDataRepository callidusDataRepository;
	
	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.partner.product.detail.queue}")
	String partnerProductQueue;

	@Autowired
	PartnerRepository partnerRepository;

	@Autowired
	MstLeAttributeRepository mstLeAttributeRepository;

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

	public static final String DATEFORMAT = "yyyyMMddHHmmss";

	@Value("${document.upload}")
	String uploadPath;

	@Autowired
	SAPDataRepository sapDataRepository;

	@Autowired
	PartnerLeAttributeValueRepository partnerLeAttributeValueRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	PartnerEngagementRepository partnerEngagementRepository;

	@Autowired
	PartnerProductConfigurationRepository partnerProductConfigurationRepository;

	@Autowired
	PartnerAttributeValueRepository partnerAttributeValueRepository;

	@Autowired
	MstCustomerSpAttributeRepository mstCustomerSpAttributeRepository;
	
	@Autowired
	CustomerAttributeValueRepository customerAttributeValueRepository;

	@Autowired
	private PartnerLeBillingInfoRepository partnerLeBillingInfoRepository;

	@Autowired
	private PartnerLegalEntitySapCodeRepository partnerLegalEntitySapCodeRepository;

	@Autowired
	private PartnerProfileRepository partnerProfileRepository;

	@Autowired
	PartnerLeCurrencyRepository partnerLeCurrencyRepository;

	@Value("${rabbitmq.partner.contracting.address.queue}")
	String getPartnerContractingAddressQueue;

	@Autowired
	PartnerEndCustomerMappingRepository partnerEndCustomerMappingRepository;

	@Value("${rabbitmq.partner.temp.end.customers}")
	String partnerTempEndCustomerQueue;

	@Autowired
	CustomerService customerService;

	@Autowired
	MstCountryRepository mstCountryRepository;

	@Autowired
	ServiceProviderLegalEntityCountryRepository spleCountryRepository;

	/**
	 * Get partner legal entities by list of partner le ids
	 *
	 * @param partnerId
	 * @return {@link List<PartnerLegalEntityBean>}
	 */
	public List<PartnerLegalEntityBean> getPartnerLegalEntitiesByPartnerId(Integer partnerId) {
		List<PartnerLegalEntity> partnerLegalEntities = partnerLegalEntityRepository.findByPartnerId(partnerId);
		return partnerLegalEntities.stream().map(partnerLegalEntity -> {
			PartnerLegalEntityBean partnerLegalEntityBean = new PartnerLegalEntityBean();
			partnerLegalEntityBean.setId(partnerLegalEntity.getId());
			partnerLegalEntityBean.setEntityName(partnerLegalEntity.getEntityName());
			partnerLegalEntityBean.setPartnerId(partnerLegalEntity.getPartner().getId());
			partnerLegalEntityBean.setTpsSfdcCuid(partnerLegalEntity.getTpsSfdcCuid());
			partnerLegalEntityBean.setAgreementId(partnerLegalEntity.getAgreementId());
			return partnerLegalEntityBean;
		}).collect(Collectors.toList());
	}

	/**
	 * Get End Customer Details By partner ID
	 *
	 * @param partnerId
	 * @return {@link List<PartnerLegalEntityBean>}
	 */
	public List<PartnerEndCustomerLeBean> getEndCustomerLesByPartnerId(Integer partnerId) {
		List<PartnerEndCustomerMapping> endCustomerLes = partnerEndCustomerMappingRepository.findByPartnerId(partnerId);
		List<PartnerEndCustomerLeBean> endCustomerLeBeans = endCustomerLes.stream().filter(endCustomerLe ->
				Objects.nonNull(endCustomerLe.getEndCustomerCuid()) && Objects.nonNull(endCustomerLe.getEndCustomerLeName()))
				.map(endCustomerLe -> {
					PartnerEndCustomerLeBean partnerEndCustomerLeBean = new PartnerEndCustomerLeBean();
					partnerEndCustomerLeBean.setEndCustomerCUID(endCustomerLe.getEndCustomerCuid());
					partnerEndCustomerLeBean.setEndCustomerLeName(endCustomerLe.getEndCustomerLeName());
					partnerEndCustomerLeBean.setSource(TCL_MAPPED_END_CUSTOMER);
					return partnerEndCustomerLeBean;
				}).collect(Collectors.toList());

		List<PartnerTempCustomerDetailsBean> tempEndCustomerDetails = getTempEndCustomerDetailsMQ(partnerId);
		if(!CollectionUtils.isEmpty(tempEndCustomerDetails)){
			List<String> endCustomerLeNames = endCustomerLeBeans.stream().map(PartnerEndCustomerLeBean::getEndCustomerLeName).collect(Collectors.toList());
			tempEndCustomerDetails.stream().filter(tempBean -> Objects.nonNull(tempBean.getCustomerLegalEntityCuid()))
					.filter(tempBean -> !endCustomerLeNames.contains(tempBean.getCustomerName()))
					.forEach(tempBean -> {
						PartnerEndCustomerLeBean partnerEndCustomerLeBean = new PartnerEndCustomerLeBean();
						LOGGER.info("Temp bean customer name is {}", tempBean.getCustomerName());
						partnerEndCustomerLeBean.setEndCustomerLeName(tempBean.getCustomerName());
						partnerEndCustomerLeBean.setEndCustomerCUID(tempBean.getCustomerLegalEntityCuid());
						partnerEndCustomerLeBean.setTempCustomerLeId(tempBean.getId().toString());
						partnerEndCustomerLeBean.setSource(TEMPORARILY_MAPPED_END_CUSTOMER);
						endCustomerLeBeans.add(partnerEndCustomerLeBean);
					});
		}
		return endCustomerLeBeans;
	}

	public String getPartnerAccountName(Integer partnerId) {
		MstCustomerSpAttribute mstCustomerSpAttributeForAccountOwnerName = mstCustomerSpAttributeRepository
				.findByNameAndStatus(ACCOUNT_OWNER, (byte) 1);
		String accountOwnerName = partnerAttributeValueRepository.findByPartnerId(partnerId).stream()
				.filter(partnerAttributeValue -> partnerAttributeValue.getMstCustomerSpAttributeId()
						.equals(mstCustomerSpAttributeForAccountOwnerName.getId()))
				.findFirst().get().getAttributeValues();

		MstCustomerSpAttribute mstCustomerSpAttributeForAccountEmail = mstCustomerSpAttributeRepository
				.findByNameAndStatus(ACCOUNT_OWNER_EMAILID, (byte) 1);
		String accountOwnerEmail = partnerAttributeValueRepository
				.findByPartnerId(partnerId).stream().filter(partnerAttributeValue -> partnerAttributeValue
						.getMstCustomerSpAttributeId().equals(mstCustomerSpAttributeForAccountEmail.getId()))
				.findFirst().get().getAttributeValues();

		return accountOwnerName + COMMA + accountOwnerEmail;
	}

	public List<PartnerLegalEntityBean> getPartnerLegalEntities(List<Integer> partnerLeIds) {
		List<PartnerLegalEntityBean> partnerLegalEntityBeans = partnerLeIds.stream().map(partnerLegalEntityRepository::findById).map(Optional::get)
				.map(this::fromPartnerLegalEntity).collect(Collectors.toList());
		LOGGER.info("PartnerLegalEntityBeans :: {}", partnerLegalEntityBeans);
		return partnerLegalEntityBeans;

	}

	/**
	 * Get partner legal entity bean by partner legal entity
	 *
	 * @param partnerLegalEntity
	 * @return {@link PartnerLegalEntityBean}
	 */
	private PartnerLegalEntityBean fromPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
		PartnerLegalEntityBean bean = new PartnerLegalEntityBean();
		bean.setId(partnerLegalEntity.getId());
		bean.setEntityName(partnerLegalEntity.getEntityName());
		bean.setPartnerId(partnerLegalEntity.getPartner().getId());
		bean.setTpsSfdcCuid(partnerLegalEntity.getTpsSfdcCuid());
		return bean;
	}

	/**
	 * Get customer legal entities by partner legal entity id
	 *
	 * @param partnerLegalEntityId
	 * @return {@link CustomerLegalEntityBean}
	 */
	public CustomerLegalEntityBean getCustomerLegalEntity(Integer partnerLegalEntityId) {
		String partnerLegalEntityName = partnerLegalEntityRepository.findById(partnerLegalEntityId).get()
				.getEntityName();
		CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findByEntityName(partnerLegalEntityName)
				.get();
		return CustomerLegalEntityBean.fromEntity(customerLegalEntity);
	}

	/**
	 * Get customer by partner legal entity
	 *
	 * @param partnerLegalEntityId
	 * @return {@link List<CustomerDto>}
	 */
	public List<CustomerDto> getCustomers(Integer partnerLegalEntityId) {
		List<CustomerLegalEntity> customerLegalEntities = customerLegalEntityRepository
				.findAllCustomerLegalEntityByPartnerLegalEntity(partnerLegalEntityId);
		return customerLegalEntities.stream().map(customerLegalEntity -> {
			CustomerDto customerDto = new CustomerDto();
			customerDto.setCustomerId(customerLegalEntity.getCustomer().getId());
			customerDto.setCustomerName(customerLegalEntity.getCustomer().getCustomerName());
			return customerDto;
		}).collect(Collectors.toList());
	}

	/**
	 * Get Master Product Families Id
	 *
	 * @param partnerId
	 * @param classification
	 * @return {@link List<MstProductFamilyBean>}
	 */
	public List<MstProductFamilyBean> getProducts(Integer partnerId, String classification) throws TclCommonException {
		List<PartnerProductConfiguration> partnerProductConfigurations = partnerProductConfigurationRepository
				.findByPartnerId(partnerId);

		List<Integer> productFamilyId = partnerProductConfigurations.stream()
				.filter(partnerProductConfiguration -> classification
						.equalsIgnoreCase(partnerProductConfiguration.getClassification()))
				.map(PartnerProductConfiguration::getErfProdCatalogProductFamilyId).collect(Collectors.toList());

		String partnerProductResponse = (String) mqUtils.sendAndReceive(partnerProductQueue, null);

		LOGGER.info("All Products :: {}", partnerProductResponse);

		List<MstProductFamilyBean> mstProductFamilyBeans = Utils.fromJson(partnerProductResponse,
				new TypeReference<List<MstProductFamilyBean>>() {
				});

		LOGGER.info("All Products Object :: {}", mstProductFamilyBeans);

		mstProductFamilyBeans.stream().map(mstProductFamilyBean -> {
			if (GSIP.equalsIgnoreCase(mstProductFamilyBean.getName())) {
				mstProductFamilyBean.setName("GSC");
			}
			return mstProductFamilyBean;
		}).collect(Collectors.toList());
		return mstProductFamilyBeans.stream().filter(p -> productFamilyId.contains(p.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * Update partner files for partnerleID
	 *
	 * @param file
	 * @param partnerLeId
	 * @param attachmentType
	 * @param partnerType
	 * @return {@link AttachmentBean}
	 */
	public AttachmentBean uploadPartnerFiles(MultipartFile file, Integer partnerLeId, String attachmentType,
			String partnerType, String productName) throws TclCommonException {
		Objects.requireNonNull(file, "File cannot be null");
		Objects.requireNonNull(partnerLeId, "PartnerLeId cannot be null");
		Objects.requireNonNull(attachmentType, "Attachment Type cannot be null");

		PartnerDocumentContext context = createPartnerDocumentContext(file, partnerLeId, attachmentType, partnerType,
				productName);

		context.partnerLegalEntity = validatePartnerLegalEntityById(partnerLeId);
		context.mstLeAttribute = getMstLeAttributeId(attachmentType);

		uploadPartnerFilesByObjectOrFileStorage(context);
		if (Objects.nonNull(context.partnerType)) {
			updatePartnerTypeInPartnerLeAttributeValues(context);
		}
		return createAndUpdateAttachmentInPartnerLeAttributeValueBasedOnDocumentType(context);
	}

	/**
	 * Update partner type in partner le attribute values
	 *
	 * @param context
	 */
	private PartnerLeAttributeValue updatePartnerTypeInPartnerLeAttributeValues(PartnerDocumentContext context) {
		MstLeAttribute mstLeAttribute = getMstLeAttributeId(PartnerCustomerConstants.PARTNER_TYPE);
		PartnerLeAttributeValue partnerLeAttributeValue = getPartnerLegalAttribute(context.partnerLeId, mstLeAttribute);
		if (Objects.isNull(partnerLeAttributeValue)) {
			return savePartnerLeAttributeValues(context.partnerType, context.partnerLegalEntity, mstLeAttribute, null);
		} else {
			return updatePartnerLeAttributeValues(context.partnerType, partnerLeAttributeValue);
		}
	}

	/**
	 * Update attachment Id in partner le attribute value based on document type
	 * Product document requires product name
	 *
	 * @param context
	 * @return {@link Attachment}
	 */
	private AttachmentBean createAndUpdateAttachmentInPartnerLeAttributeValueBasedOnDocumentType(
			PartnerDocumentContext context) {
		String attachmentPath = context.fileProperties.get(DocumentConstant.ATTACHMENT_PATH);
		String fileName = context.fileProperties.get(DocumentConstant.FILE_NAME);

		if (context.mstLeAttribute.getName().contains(PartnerCustomerConstants.MST_ATTRIBUTE_WITH_NAME_PRODUCT)) {
			PartnerLeAttributeValue partnerLeAttributeValue = getPartnerLegalAttributeByMstLeAttributeAndProductName(
					context.partnerLeId, context.mstLeAttribute, context.productName);
			return createAndUpdateAttachmentInPartnerLeAttributeValue(context, attachmentPath, fileName,
					partnerLeAttributeValue);
		} else {
			PartnerLeAttributeValue partnerLeAttributeValue = getPartnerLegalAttribute(context.partnerLeId,
					context.mstLeAttribute);
			return createAndUpdateAttachmentInPartnerLeAttributeValue(context, attachmentPath, fileName,
					partnerLeAttributeValue);
		}
	}

	/**
	 * Update attachment Id in partner le attribute value
	 *
	 * @param context
	 * @return {@link Attachment}
	 */
	private AttachmentBean createAndUpdateAttachmentInPartnerLeAttributeValue(PartnerDocumentContext context,
			String attachmentPath, String fileName, PartnerLeAttributeValue partnerLeAttributeValue) {
		if (Objects.isNull(partnerLeAttributeValue)) {
			Attachment attachment = createAttachment(attachmentPath, context.attachmentType, fileName);
			savePartnerLeAttributeValues(String.valueOf(attachment.getId()), context.partnerLegalEntity,
					context.mstLeAttribute, context.productName);
			return getAttachmentBeanResponse(attachment);
		} else {
			Optional<Attachment> attachment = attachmentRepository
					.findById(Integer.valueOf(partnerLeAttributeValue.getAttributeValues()));
			if (attachment.isPresent()) {
				updateAttachment(attachment.get(), attachmentPath, context.attachmentType, fileName);
				return getAttachmentBeanResponse(attachment.get());
			} else {
				Attachment newAttachment = createAttachment(attachmentPath, context.attachmentType, fileName);
				updatePartnerLeAttributeValues(String.valueOf(newAttachment.getId()), partnerLeAttributeValue);
				return getAttachmentBeanResponse(newAttachment);
			}
		}
	}

	/**
	 * Upload partner Files by object or file storage
	 *
	 * @param context
	 * @throws TclCommonException
	 * @throws IOException
	 */
	private void uploadPartnerFilesByObjectOrFileStorage(PartnerDocumentContext context) throws TclCommonException {
		String attachmentPath = null;
		String fileName = null;
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				StoredObject storedObject = fileStorageService.uploadFiles(context.file.getOriginalFilename(),
						CommonConstants.PARTNER, context.file.getInputStream());
				if (storedObject != null && storedObject.getURL() != null && !storedObject.getURL().isEmpty()) {
					attachmentPath = storedObject.getURL().substring(storedObject.getURL().indexOf(swiftApiContainer),
							storedObject.getURL().lastIndexOf("/"));
					attachmentPath = attachmentPath.replaceAll("%2F", "/").replaceAll("%20", " ");
					fileName = storedObject.getName();
				}
			} else {
				attachmentPath = copyFileToFileStorage(context.file, context.partnerLeId);
				fileName = context.file.getOriginalFilename();
			}
			context.fileProperties.put(DocumentConstant.ATTACHMENT_PATH, attachmentPath);
			context.fileProperties.put(DocumentConstant.FILE_NAME, fileName);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Method to update the attachment
	 *
	 * @param attachment
	 * @param attachmentPath
	 * @param attachmentType
	 * @param fileName
	 */
	private void updateAttachment(Attachment attachment, String attachmentPath, String attachmentType,
			String fileName) {
		attachment.setUriPathOrUrl(attachmentPath);
		attachment.setDisplayName(fileName);
		attachment.setName(attachmentType);
		attachmentRepository.save(attachment);
	}

	/**
	 * Method to get AttachmentBean Response
	 *
	 * @param attachment
	 * @return {@link AttachmentBean}
	 */
	private AttachmentBean getAttachmentBeanResponse(Attachment attachment) {
		AttachmentBean response = new AttachmentBean();
		response.setId(attachment.getId());
		response.setDisplayName(attachment.getDisplayName());
		response.setName(attachment.getName());
		response.setUriPath(attachment.getUriPathOrUrl());
		return response;
	}

	/**
	 * Method to get AttachmentBean Response by variables
	 *
	 * @param temporaryUploadUrl
	 * @param requestId
	 * @return {@link AttachmentBean}
	 */
	private AttachmentBean getAttachmentBeanResponse(String temporaryUploadUrl, String requestId) {
		AttachmentBean response = new AttachmentBean();
		response.setName(requestId);
		response.setUriPath(temporaryUploadUrl);
		return response;
	}

	/**
	 * Save Attachment as Partner Le Attribute
	 *
	 * @param attributeValue
	 * @param partnerLegalEntity
	 * @param mstLeAttribute
	 */
	private PartnerLeAttributeValue savePartnerLeAttributeValues(String attributeValue,
			PartnerLegalEntity partnerLegalEntity, MstLeAttribute mstLeAttribute, String productName) {
		PartnerLeAttributeValue partnerLeAttributeValue = new PartnerLeAttributeValue();
		partnerLeAttributeValue.setMstLeAttribute(mstLeAttribute);
		partnerLeAttributeValue.setPartnerLegalEntity(partnerLegalEntity);
		partnerLeAttributeValue.setAttributeValues(attributeValue);
		partnerLeAttributeValue.setProductName(productName);
		partnerLeAttributeValueRepository.save(partnerLeAttributeValue);
		return partnerLeAttributeValue;
	}

	/**
	 * Update attachment in partner le attribute
	 *
	 * @param attributeValue
	 * @param partnerLeAttributeValue
	 */
	private PartnerLeAttributeValue updatePartnerLeAttributeValues(String attributeValue,
			PartnerLeAttributeValue partnerLeAttributeValue) {
		partnerLeAttributeValue.setAttributeValues(attributeValue);
		partnerLeAttributeValueRepository.save(partnerLeAttributeValue);
		return partnerLeAttributeValue;
	}

	/**
	 * Create a attachment
	 *
	 * @param attachmentPath
	 * @param attachmentType
	 * @param fileName
	 * @return {@link Attachment}
	 */
	private Attachment createAttachment(String attachmentPath, String attachmentType, String fileName) {
		Attachment attachment = new Attachment();
		attachment.setDisplayName(fileName);
		attachment.setName(attachmentType);
		attachment.setUriPathOrUrl(attachmentPath);
		attachmentRepository.save(attachment);
		return attachment;
	}

	/**
	 * Get Partner legal attribute by partner le id and attribute id
	 *
	 * @param partnerLeId
	 * @param mstLeAttribute
	 * @return {@link PartnerLeAttributeValue}
	 */
	private PartnerLeAttributeValue getPartnerLegalAttribute(Integer partnerLeId, MstLeAttribute mstLeAttribute) {
		List<PartnerLeAttributeValue> partnerLeAttributeValues = partnerLeAttributeValueRepository
				.findByPartnerLegalEntity_IdAndMstLeAttribute(partnerLeId, mstLeAttribute);
		if (!CollectionUtils.isEmpty(partnerLeAttributeValues)) {
			return partnerLeAttributeValues.stream().findFirst().get();
		}
		return null;
	}

	/**
	 * Copy the file to file storage and get folder path
	 *
	 * @param file
	 * @param partnerLeId
	 * @return {@link String}
	 */
	private String copyFileToFileStorage(MultipartFile file, Integer partnerLeId) throws TclCommonException {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);
		String newFolder = uploadPath + "PARTNER" + partnerLeId + now.format(formatter);
		File fileFolder = new File(newFolder);
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		Path path = Paths.get(newFolder);
		try {
			Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		return newFolder;
	}

	/**
	 * Get or Validate Partner Legal entity by partner le id
	 *
	 * @param partnerLeId
	 * @return {@link PartnerLegalEntity}
	 */
	private PartnerLegalEntity validatePartnerLegalEntityById(Integer partnerLeId) {
		return partnerLegalEntityRepository.findById(partnerLeId).orElseGet(() -> {
			throw new TclCommonRuntimeException(ExceptionConstants.INVALID_PARTNER_LE_ID,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		});
	}

	/**
	 * Get Mst atrribute id by attribute name
	 *
	 * @param attachmentType
	 * @return {@link MstLeAttribute}
	 */
	private MstLeAttribute getMstLeAttributeId(String attachmentType) {
		return mstLeAttributeRepository.findByName(attachmentType).orElseGet(() -> {
			throw new TclCommonRuntimeException(ExceptionConstants.INVALID_MST_ATTRIBUTE,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		});
	}

	/**
	 * Get Partner Document List by classfication
	 *
	 * @param partnerLeId
	 * @param classification
	 * @param productName
	 * @return {@link List}
	 */
	public List<AttachmentBean> getPartnerDocumentsList(Integer partnerLeId, String classification,
			String productName) {
		Objects.requireNonNull(partnerLeId, "PartnerLeId cannot be null");
		Objects.requireNonNull(classification, "Classification cannot be null");
		Objects.requireNonNull(productName, "Product name cannot be null");

		validatePartnerLegalEntityById(partnerLeId);
		String partnerType = getPartnerType(partnerLeId);
		List<MstLeAttribute> classficationBasedMstLeAttributes = getMstLeAttributeIds(classification);

		List<AttachmentBean> documentLists = new ArrayList<>();

		getDocumentsExceptProductDocument(partnerLeId, partnerType, classficationBasedMstLeAttributes, documentLists);
		getProductDocument(partnerLeId, productName, classficationBasedMstLeAttributes, documentLists);
		return documentLists;
	}

	/**
	 * Get documents except product document - service schedule
	 *
	 * @param partnerLeId
	 * @param partnerType
	 * @param classificationBasedMstLeAttributes
	 * @param documentLists
	 */
	private void getDocumentsExceptProductDocument(Integer partnerLeId, String partnerType,
			List<MstLeAttribute> classificationBasedMstLeAttributes, List<AttachmentBean> documentLists) {
		getMstLeAttributeBasedOnPartnerType(partnerType,
				classificationBasedMstLeAttributes)
						.stream()
						.filter(mstLeAttribute -> !mstLeAttribute.getName()
								.contains(PartnerCustomerConstants.MST_ATTRIBUTE_WITH_NAME_PRODUCT))
						.forEach(mstLeAttribute -> {
							PartnerLeAttributeValue partnerLeAttributeValue = getPartnerLegalAttribute(partnerLeId,
									mstLeAttribute);
							if (Objects.nonNull(partnerLeAttributeValue)) {
								AttachmentBean attachmentBean = getAttachmentBeanForPartnerDocuments(mstLeAttribute,
										partnerLeAttributeValue);
								documentLists.add(attachmentBean);
							}
						});
	}

	/**
	 * Create Attachment bean based on mstleAttribute value and partner le
	 *
	 * @param mstLeAttribute
	 * @param partnerLeAttributeValue
	 * @return {@link AttachmentBean}
	 */
	private AttachmentBean getAttachmentBeanForPartnerDocuments(MstLeAttribute mstLeAttribute,
			PartnerLeAttributeValue partnerLeAttributeValue) {
		AttachmentBean attachmentBean = new AttachmentBean();
		attachmentBean.setId(Integer.valueOf(partnerLeAttributeValue.getAttributeValues()));
		attachmentBean.setDocType(getDisplayName(mstLeAttribute));
		attachmentBean.setName(mstLeAttribute.getName());
		return attachmentBean;
	}

	/**
	 * Get Product based document - service schedule
	 *
	 * @param partnerLeId
	 * @param productName
	 * @param classificationBasedMstLeAttributes
	 * @param documentLists
	 */
	private void getProductDocument(Integer partnerLeId, String productName,
			List<MstLeAttribute> classificationBasedMstLeAttributes, List<AttachmentBean> documentLists) {
		classificationBasedMstLeAttributes.stream()
				.filter(attr -> attr.getName().contains(PartnerCustomerConstants.MST_ATTRIBUTE_WITH_NAME_PRODUCT))
				.findFirst().ifPresent(mstLeAttribute -> {
					PartnerLeAttributeValue partnerLeAttributeValue = getPartnerLegalAttributeByMstLeAttributeAndProductName(
							partnerLeId, mstLeAttribute, productName);
					if (Objects.nonNull(partnerLeAttributeValue)) {
						AttachmentBean attachmentBean = getAttachmentBeanForPartnerDocuments(mstLeAttribute,
								partnerLeAttributeValue);
						documentLists.add(attachmentBean);
					}
				});
	}

	private PartnerLeAttributeValue getPartnerLegalAttributeByMstLeAttributeAndProductName(Integer partnerLeId,
			MstLeAttribute mstLeAttribute, String productName) {
		return partnerLeAttributeValueRepository.findByPartnerLegalEntity_IdAndMstLeAttributeAndProductName(partnerLeId,
				mstLeAttribute, productName);
	}

	/**
	 * Exclude documents based on partner type
	 *
	 * @param partnerType
	 * @param classficationBasedMstLeAttributes
	 * @return {@link List}
	 */
	private List<MstLeAttribute> getMstLeAttributeBasedOnPartnerType(String partnerType,
			List<MstLeAttribute> classficationBasedMstLeAttributes) {
		if (partnerType.equalsIgnoreCase(PartnerCustomerConstants.PARTNER_TYPE_NEW)) {
			return classficationBasedMstLeAttributes.stream().filter(mstLeAttribute -> !PartnerCustomerUtils
					.excludedDocumentsForNewPartner().contains(mstLeAttribute.getName())).collect(Collectors.toList());
		} else {
			return classficationBasedMstLeAttributes.stream().filter(mstLeAttribute -> !PartnerCustomerUtils
					.excludedDocumentsForExistingPartner().contains(mstLeAttribute.getName()))
					.collect(Collectors.toList());
		}
	}

	/**
	 * Method to get partner Type by partner LeID
	 *
	 * @param partnerLeId
	 * @return {@link String}
	 */
	private String getPartnerType(Integer partnerLeId) {
		return getPartnerLegalAttribute(partnerLeId, getMstLeAttributeId(PartnerCustomerConstants.PARTNER_TYPE))
				.getAttributeValues();
	}

	/**
	 * Get Display Name for document by mstLeAttribute
	 *
	 * @param mstLeAttribute
	 * @return {@link String}
	 */
	private String getDisplayName(MstLeAttribute mstLeAttribute) {
		Map<String, String> documentNames = PartnerCustomerUtils.getDocumentNameByMstAttributeName();
		String documentNameByMstAttributeName = documentNames.keySet().stream()
				.filter(s -> mstLeAttribute.getName().contains(s)).findFirst().orElse(null);
		return documentNames.get(documentNameByMstAttributeName);
	}

	/**
	 * Get Mst Le AttributeIds list which contains classfication as
	 * MstleAttributeName
	 *
	 * @param mstLeAttributeName
	 * @return {@link List}
	 */
	private List<MstLeAttribute> getMstLeAttributeIds(String mstLeAttributeName) {
		List<MstLeAttribute> mstLeAttributes = mstLeAttributeRepository.findByMstLeAttributeName(mstLeAttributeName);
		if (mstLeAttributes.isEmpty()) {
			throw new TclCommonRuntimeException(ExceptionConstants.INVALID_MST_ATTRIBUTE,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		} else {
			return mstLeAttributes;
		}
	}

	/**
	 * Method to get partner documents
	 *
	 * @param partnerLeId
	 * @param attachmentId
	 * @return {@link Resource}
	 */
	public Resource getPartnerDocuments(Integer partnerLeId, Integer attachmentId) {
		Objects.requireNonNull(partnerLeId, "PartnerLeId cannot be null");
		Objects.requireNonNull(attachmentId, "AttachmentId cannot be null");
		validatePartnerLegalEntityById(partnerLeId);
		validateAttachmentId(partnerLeId, attachmentId);
		return attachmentRepository.findById(attachmentId).map(this::downloadDocument).orElseGet(() -> {
			throw new TclCommonRuntimeException(ExceptionConstants.INVALID_PARTNER_LE_ID,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		});
	}

	/**
	 * Method to download document by attachment path
	 *
	 * @param attachment
	 * @return {@link Resource}
	 */
	private Resource downloadDocument(Attachment attachment) {
		Resource resource = null;
		try {
			LOGGER.info("Path received :: {}", attachment.getUriPathOrUrl());
			File[] files = new File(attachment.getUriPathOrUrl()).listFiles();
			String attachmentPath = null;
			for (File file : files) {
				if (file.isFile()) {
					attachmentPath = file.getAbsolutePath();
					LOGGER.info("File Abs path :: {}", attachmentPath);
				}
			}
			Path attachmentLocation = Paths.get(attachmentPath);
			resource = new UrlResource(attachmentLocation.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (MalformedURLException e) {
			LOGGER.warn("Error in processing download malformered url {}", e.getMessage());
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Validate partnerLeId and attachmentId in partner attribute values
	 *
	 * @param partnerLeId
	 * @param attachmentId
	 * @return {@link PartnerLeAttributeValue}
	 */
	private PartnerLeAttributeValue validateAttachmentId(Integer partnerLeId, Integer attachmentId) {
		return partnerLeAttributeValueRepository
				.findByPartnerLeIdAndAttachmentId(partnerLeId, String.valueOf(attachmentId)).orElseGet(() -> {
					throw new TclCommonRuntimeException(ExceptionConstants.INVALID_ATTACHMENT_ID,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				});
	}

	/**
	 * Get temporary download url for a attachment after validating partner le id
	 * and attachmentid
	 *
	 * @param partnerLeId
	 * @param attachmentId
	 * @return {@link String}
	 */
	public String getAttachmentTempDownloadUrl(Integer partnerLeId, Integer attachmentId) {
		Objects.requireNonNull(partnerLeId, "PartnerLeId cannot be null");
		Objects.requireNonNull(attachmentId, "AttachmentId cannot be null");
		validatePartnerLegalEntityById(partnerLeId);
		validateAttachmentId(partnerLeId, attachmentId);
		return attachmentRepository.findById(attachmentId).map(this::getTempDownloadUrl).orElseGet(() -> {
			throw new TclCommonRuntimeException(ExceptionConstants.INVALID_PARTNER_LE_ID,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		});
	}

	/**
	 * Get temporary download url for a attachment
	 *
	 * @param attachment
	 * @return {@link String}
	 */
	private String getTempDownloadUrl(Attachment attachment) {
		try {
			return fileStorageService.getTempDownloadUrl(attachment.getDisplayName(),
					Long.parseLong(tempDownloadUrlExpiryWindow), attachment.getUriPathOrUrl(), false);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.OBJECT_STORAGE_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Method to update the partner Type
	 *
	 * @param partnerLeId
	 * @return {@link PartnerCustomerLeAttributeBean}
	 */
	public PartnerCustomerLeAttributeBean updatePartnerType(Integer partnerLeId) {
		validatePartnerLegalEntityById(partnerLeId);
		PartnerDocumentContext context = createPartnerDocumentContext(null, partnerLeId, null,
				PartnerCustomerConstants.PARTNER_TYPE_EXISTING, null);
		PartnerLeAttributeValue partnerLeAttributeValue = updatePartnerTypeInPartnerLeAttributeValues(context);
		return PartnerCustomerLeAttributeBean.fromPartnerLeAttributeBean(partnerLeAttributeValue);
	}

	/**
	 * Get Partner's Classification
	 *
	 * @param partnerId
	 * @return {@link PartnerProductClassificationBean}
	 */
	public PartnerProductClassificationBean getPartnerClassifications(Integer partnerId) {
		PartnerProductClassificationBean partnerProductClassificationBean = new PartnerProductClassificationBean();
		List<PartnerProductConfiguration> partnerProductConfigurations = partnerProductConfigurationRepository
				.findByPartnerId(partnerId);
		partnerProductClassificationBean.setClassification(partnerProductConfigurations.stream()
				.map(PartnerProductConfiguration::getClassification).collect(Collectors.toSet()));
		return partnerProductClassificationBean;
	}

	/**
	 * Get partner account id from partner id
	 *
	 * @param partnerId
	 * @return
	 */
	public PartnerDetailsBean getPartnerDetails(Integer partnerId) {
		LOGGER.info("Partner details for id {}", partnerId);
		PartnerDetailsBean partnerDetailsBean = new PartnerDetailsBean();
		MstCustomerSpAttribute customerTypeMstCustomerAttribute = mstCustomerSpAttributeRepository.findByNameAndStatus(CUSTOMER_TYPE,(byte)1);
		partnerRepository.findById(partnerId).map(partner -> {
			partnerDetailsBean.setPartnerId(partnerId);
			partnerDetailsBean.setEntityOwnerName(partner.getName());
			partnerDetailsBean.setPartnerSfdcAccountId(partner.getTpsSfdcAccountId());
			partnerDetailsBean.setAccountId18(partner.getAccountId18());
			//partnerDetailsBean.setPartnerProfile(partnerProfileRepository.findById(partner.getPartnerProfileId()).get().getCode());
			partnerDetailsBean.setPartnerProfileId(partner.getPartnerProfileId());
			PartnerProfile partnerProfile = Optional.ofNullable(partnerProfileRepository.findById(partner.getPartnerProfileId())).get()
					.orElse(new PartnerProfile());
			partnerDetailsBean.setPartnerProfile(partnerProfile.getCode());

			PartnerAttributeValue customeTypeAttributeValue = partnerAttributeValueRepository.findByPartnerIdAndMstCustomerSpAttributeId(partnerId, customerTypeMstCustomerAttribute.getId());
			if(Objects.nonNull(customeTypeAttributeValue)){
				partnerDetailsBean.setCustomerType(customeTypeAttributeValue.getAttributeValues());
			}
			return partnerDetailsBean;
		}).orElseGet(() -> null);
		return partnerDetailsBean;
	}

	/**
	 * Get Billing Accounts
	 *
	 * @param partnerLegalIds
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getBillingAccounts(List<Integer> partnerLegalIds) throws TclCommonException {
		List<String> billingAccountIds = new LinkedList<String>();
		try {

			if (CollectionUtils.isEmpty(partnerLegalIds)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			List<Map<String, Object>> billingAccounts = partnerLeBillingInfoRepository
					.findBillingAccountForPartnerLegalEntity_IdInAndIsactive(partnerLegalIds, "Yes");

			billingAccountIds = billingAccounts.stream().map(billingAccount -> {
				return (String) billingAccount.get("bill_acc_no");
			}).collect(Collectors.toList());

			return billingAccountIds;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Get Sap Code based on Partner Le
	 *
	 * @param partnerLeIds
	 * @param value
	 * @return {@link LeSapCodeResponse}
	 */
	public LeSapCodeResponse getSapCodeBasedOnPartnerLe(List<Integer> partnerLeIds, String value) {
		LeSapCodeResponse leSapCodeResponse = new LeSapCodeResponse();
		if (!CollectionUtils.isEmpty(partnerLeIds)) {
			String type = "SAP Code";
			if (Objects.nonNull(value)) {
				type = value;
			}
			List<String> partnerLeCompanyCode = partnerLegalEntitySapCodeRepository
					.getPartnerLeCompanyCode(partnerLeIds, type);
			partnerLeCompanyCode.forEach(leSap -> {
				LeSapCodeBean bean = new LeSapCodeBean();
				bean.setCodeValue(leSap.toString());
				leSapCodeResponse.getLeSapCodes().add(bean);

			});
		}
		return leSapCodeResponse;
	}

	/**
	 * Get SECS Code Based on Partner LE
	 *
	 * @param partnerLeIds
	 * @param value
	 * @return
	 */
	public LeSapCodeResponse getSecsCodeBasedOnPartnerLe(Integer partnerLeIds) {
		LeSapCodeResponse leSapCodeResponse = new LeSapCodeResponse();
		if (Objects.nonNull(partnerLeIds)) {
			List<String> partnerLeCompanyCode = partnerLegalEntitySapCodeRepository.getPartnerLeSecsCode(partnerLeIds);
			partnerLeCompanyCode.forEach(leSap -> {
				LeSapCodeBean bean = new LeSapCodeBean();
				bean.setCodeValue(leSap.toString());
				leSapCodeResponse.getLeSapCodes().add(bean);
			});
		}
		return leSapCodeResponse;
	}

	public PartnerTempCustomerDetailsBean getPartnerLeAttributes(String partnerLeId) {
		LOGGER.info("Get partner le attributes for le id {}", partnerLeId);
		List<PartnerLeAttributeValue> partnerLeAttributeValues = partnerLeAttributeValueRepository.findByPartnerLegalEntity_Id(Integer.valueOf(partnerLeId));
		PartnerTempCustomerDetailsBean partnerTempCustomerDetailsBean = new PartnerTempCustomerDetailsBean();
		if(!CollectionUtils.isEmpty(partnerLeAttributeValues)){
			partnerTempCustomerDetailsBean.setIndustry(getLeAttributeValueIfPresent(partnerLeAttributeValues, INDUSTRY));
			partnerTempCustomerDetailsBean.setIndustrySubtype(getLeAttributeValueIfPresent(partnerLeAttributeValues, INDUSTRY_SUB_TYPE));
			partnerTempCustomerDetailsBean.setCustomerWebsite(getLeAttributeValueIfPresent(partnerLeAttributeValues, WEBSITE));
			partnerTempCustomerDetailsBean.setSubIndustry(getLeAttributeValueIfPresent(partnerLeAttributeValues, SUB_INDUSTRY));
			partnerTempCustomerDetailsBean.setRegistrationNumber(getLeAttributeValueIfPresent(partnerLeAttributeValues, REGISTRATION_NUMBER));
			partnerTempCustomerDetailsBean.setCustomerContactName(getLeAttributeValueIfPresent(partnerLeAttributeValues, CUSTOMER_CONTACT_NAME));
			partnerTempCustomerDetailsBean.setCustomerContactEmail(getLeAttributeValueIfPresent(partnerLeAttributeValues, EMAIL));
			partnerTempCustomerDetailsBean.setCustomerContactNumber(getLeAttributeValueIfPresent(partnerLeAttributeValues, LE_CONTACT));
			partnerTempCustomerDetailsBean.setTypeOfBusiness(getLeAttributeValueIfPresent(partnerLeAttributeValues, TYPE_OF_BUSINESS));
			partnerTempCustomerDetailsBean.setSupplierLeOwner(getLeAttributeValueIfPresent(partnerLeAttributeValues, MST_ATTRIBUTE_SUPPLIER_LE_OWNER));
			getPartnerContractingAddress(partnerLeAttributeValues, partnerTempCustomerDetailsBean);
		}
		return partnerTempCustomerDetailsBean;
	}

	public String getCustomerDetailsByCustomerId(String customerLeId) {
		String response = "";
		LOGGER.info("Get customer le attributes for customer id {}", customerLeId);
		CustomerLegalEntity customerLegalEntity = customerLegalEntityRepository.findById(Integer.valueOf(customerLeId))
				.orElseThrow(() -> new TclCommonRuntimeException("Customer Legal Entity not found"));
		Customer customer = Optional.ofNullable(customerLegalEntity.getCustomer())
				.orElseThrow(() -> new TclCommonRuntimeException("Customer not found"));
		String customerLegalEntityName = customerLegalEntity.getEntityName();
		String customerName = customer.getCustomerName();
		response = customerLegalEntityName + COLON + customerName;
		return response;
	}

	private void getPartnerContractingAddress(List<PartnerLeAttributeValue> partnerLeAttributeValues,
											  PartnerTempCustomerDetailsBean partnerTempCustomerDetailsBean) {
		try {
			String locationId = getLeAttributeValueIfPresent(partnerLeAttributeValues, CUSTOMER_CONTACT_ENTITY);
			String response = (String) mqUtils.sendAndReceive(getPartnerContractingAddressQueue, locationId);
			PartnerContractingAddress partnerContractingAddress = Utils.convertJsonToObject(response, PartnerContractingAddress.class);
			partnerTempCustomerDetailsBean.setStreet(
					Objects.isNull(partnerContractingAddress.getAddress()) ? "" : partnerContractingAddress.getAddress());
			partnerTempCustomerDetailsBean.setCity(
					Objects.isNull(partnerContractingAddress.getCity()) ? "" : partnerContractingAddress.getCity());
			partnerTempCustomerDetailsBean.setState(
					Objects.isNull(partnerContractingAddress.getState()) ? "" : partnerContractingAddress.getState());
			partnerTempCustomerDetailsBean.setPostalCode(
					Objects.isNull(partnerContractingAddress.getPincode()) ? "" : partnerContractingAddress.getPincode());
			partnerTempCustomerDetailsBean.setCountry(
					Objects.isNull(partnerContractingAddress.getCountry()) ? "" : partnerContractingAddress.getCountry());
		}catch (Exception e){
			LOGGER.error("Error in fetching partner contracting address {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonRuntimeException("Error in fetching Partner Contracting Address");
		}
	}

	private String getLeAttributeValueIfPresent(List<PartnerLeAttributeValue> partnerLeAttributeValues, String attributeName) {
		Optional<PartnerLeAttributeValue> leAttributeValue = partnerLeAttributeValues.stream()
				.filter(partnerLeAttributeValue -> partnerLeAttributeValue.getMstLeAttribute().getName().equals(attributeName)).findFirst();
		if(leAttributeValue.isPresent()){
			LOGGER.info("partner le attribute found for attibute name {}", attributeName);
			return leAttributeValue.get().getAttributeValues();
		}
		return null;
	}

	public Set<CustomerLegalEntityProductResponseDto> getSupplierLegalEntityDetailsByPartnerLegalIdForService(Integer partnerLegalEntityId, String product, Integer secsId, List<String> services) throws TclCommonException {
		/* if(services.contains(DOMESTIC_VOICE)){
			Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = new HashSet<>();
			LOGGER.info("Has Domestic voice service");
			Optional<PartnerLegalEntity> partnerLegalEntity = partnerLegalEntityRepository.findById(partnerLegalEntityId);
			if(partnerLegalEntity.isPresent()){
				Set<PartnerLeCountry> partnerLeCountries = partnerLegalEntity.get().getPartnerLeCountries();
				if(!partnerLeCountries.isEmpty()){
					Integer countryId = partnerLeCountries.stream().findFirst().get().getCountryId();
					MstCountry mstCountry = mstCountryRepository.findById(countryId).get();
					LOGGER.info("Country name of partner le {} is {}", mstCountry.getName(), partnerLegalEntityId);
					String currency = customerService.getCurrencyDetails(mstCountry.getName());
					LOGGER.info("Currency is {}", currency);
					List<SpLeCountry> supplierCountries = spleCountryRepository.getSupplierByCountryAndProduct(mstCountry.getId(), ServiceSpecificConstant.GSIP);
					if (Objects.isNull(supplierCountries) || 0 == supplierCountries.size()) {
						LOGGER.warn("Supplier Legal Entity is not available");
					} else {
						List<ServiceProviderLegalEntity> serviceProviderLegalEntities = supplierCountries.stream().map(spLeCountry -> spLeCountry.getServiceProviderLegalEntity()).distinct().collect(Collectors.toList());
						customerLegalEntityProductResponseDtos = serviceProviderLegalEntities.stream().map(serviceProviderLegalEntity -> {
							CustomerLegalEntityProductResponseDto customerLegalEntityProductResponseDto = new CustomerLegalEntityProductResponseDto();
							customerLegalEntityProductResponseDto.setSple(serviceProviderLegalEntity.getEntityName());
							customerLegalEntityProductResponseDto.setServiceProviderId(serviceProviderLegalEntity.getId());
							customerLegalEntityProductResponseDto.setCurrency(currency);
							return customerLegalEntityProductResponseDto;
						}).collect(Collectors.toSet());

					}
				}
			}
			return customerLegalEntityProductResponseDtos;
		}
		else{ */
			return customerService.getSupplierLegalEntityDetailsByPartnerLegalIdForService(partnerLegalEntityId, product, secsId, services);
		//}
	}

	private static class PartnerDocumentContext {
		Integer partnerLeId;
		String attachmentType;
		String partnerType;
		String productName;
		MultipartFile file;
		Map<String, String> fileProperties;
		PartnerLegalEntity partnerLegalEntity;
		MstLeAttribute mstLeAttribute;
	}

	/**
	 * Method to create context for partner documents
	 *
	 * @param file
	 * @param partnerLeId
	 * @param attachmentType
	 * @param partnerType
	 * @param productName
	 * @return {@link PartnerDocumentContext}
	 */
	private PartnerDocumentContext createPartnerDocumentContext(MultipartFile file, Integer partnerLeId,
			String attachmentType, String partnerType, String productName) {
		PartnerDocumentContext context = new PartnerDocumentContext();
		context.file = file;
		context.partnerLeId = partnerLeId;
		context.attachmentType = attachmentType;
		context.productName = productName;
		context.partnerType = Objects.nonNull(partnerType) ? partnerType.toUpperCase() : null;
		context.fileProperties = new HashMap<>();
		return context;
	}

	public List<CustomerLeBean> getCustomerLeNames(String customerLeName, String countryName) {
		List<CustomerLeBean> customeLeNames = new ArrayList<>();
		try {
			LOGGER.info("customerLeName :: {}", customerLeName);
			LOGGER.info("countryName :: {}", countryName);
			List<Map<String, Object>> customerLegalEntities = customerLegalEntityRepository
					.findByEntityNameAndCountry(customerLeName, countryName);
			if (Objects.nonNull(customerLegalEntities) && !customerLegalEntities.isEmpty()) {
				customeLeNames = customerLegalEntities.stream().map(this::constructCustomerLeBean)
						.collect(Collectors.toList());
			}
		} catch (Exception ex) {
			LOGGER.error("Error in getCustomerLeNames method" + ex.getMessage());
		}
		return customeLeNames;
	}

	private CustomerLeBean constructCustomerLeBean(Map<String, Object> customeLeNames) {
		CustomerLeBean customerLeBean = new CustomerLeBean();
		customerLeBean.setLegalEntityId((Integer) customeLeNames.get("id"));
		customerLeBean.setLegalEntityName((String) customeLeNames.get("entity_name"));
		Integer customerId=(Integer) customeLeNames.get("customer_id");
		customerLeBean.setType("NON-RTM");
		MstCustomerSpAttribute mstCustomerSpAttributeForAccountRtm = mstCustomerSpAttributeRepository
				.findByNameAndStatus(PartnerCustomerConstants.ACCOUNT_RTM, (byte) 1);
		MstCustomerSpAttribute mstCustomerSpAttributeForFYSegmentation = mstCustomerSpAttributeRepository
				.findByNameAndStatus(PartnerCustomerConstants.FY_SEGMENTATION, (byte) 1);
		if (mstCustomerSpAttributeForAccountRtm!=null) {
			List<CustomerAttributeValue> customerLeAttList = customerAttributeValueRepository
					.findByCustomerIdAndMstCustomerSpAttribute(customerId, mstCustomerSpAttributeForAccountRtm);
			for (CustomerAttributeValue customerAttributeValue : customerLeAttList) {
				if(customerAttributeValue.getAttributeValues()!=null&&customerAttributeValue.getAttributeValues().toLowerCase().contains("partner")) {
					customerLeBean.setType("RTM");
				}
			}
		}
		if (mstCustomerSpAttributeForFYSegmentation!=null) {
			List<CustomerAttributeValue> customerLeAttList = customerAttributeValueRepository
					.findByCustomerIdAndMstCustomerSpAttribute(customerId, mstCustomerSpAttributeForFYSegmentation);
			for (CustomerAttributeValue customerAttributeValue : customerLeAttList) {
				if(customerAttributeValue.getAttributeValues()!=null) {
					customerLeBean.setFySegmentation(customerAttributeValue.getAttributeValues());
				}
			}
		}
		return customerLeBean;
	}

	/**
	 * Get partner profile by partner id
	 *
	 * @param partnerId
	 * @return {@link PartnerProfileBean}
	 */
	public PartnerProfileBean getPartnerProfileByPartnerId(Integer partnerId) {
		PartnerProfileBean partnerProfileBean = new PartnerProfileBean();
		partnerRepository.findById(partnerId).map(partner -> {
			partnerProfileRepository.findById(partner.getPartnerProfileId()).map(partnerProfile -> {
				partnerProfileBean.setCode(partnerProfile.getCode());
				partnerProfileBean.setId(partnerProfile.getId());
				partnerProfileBean.setIsActive(partnerProfile.getIsActive());
				partnerProfileBean.setName(partnerProfile.getName());
				return partnerProfileBean;
			}).orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.INVALID_PARTNER_PROFILE_ID,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR));
			return partner;
		}).orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.INVALID_PARTNER_ID,
				ResponseResource.R_CODE_INTERNAL_SERVER_ERROR));
		return partnerProfileBean;
	}

	/**
	 * Get Customer Le Name By Sap Code
	 *
	 * @param sapCodes
	 * @return {@link List< ClassificationBean >}
	 */
	public List<ClassificationBean> getPartnerLeNameBySapCode(List<String> sapCodes) {
		List<Map<String, Object>> result = partnerLegalEntityRepository.findAllPartnerLegalEntityBySapCode(sapCodes);
		LOGGER.info("DB Response for Sap Code :: {}", result.toString());
		return result.stream().map(this::createClassificationBean).collect(Collectors.toList());
	}

	/**
	 * Get Customer Le Name By Billing Code
	 *
	 * @param billingAccountIds
	 * @return {@link List< ClassificationBean >}
	 */
	public List<ClassificationBean> getPartnerLeNameByBillingAccountIds(List<String> billingAccountIds) {
		List<Map<String, Object>> result = partnerLegalEntityRepository
				.findAllPartnerLegalEntityByBillingAccountIds(billingAccountIds);
		LOGGER.info("DB Response for Billing Acc Id :: {}", result.toString());
		return result.stream().map(this::createClassificationBean).collect(Collectors.toList());
	}

	private ClassificationBean createClassificationBean(final Map<String, Object> result) {
		ClassificationBean classificationBean = new ClassificationBean();
		classificationBean.setSapCode(result.get("code_value").toString());
		classificationBean.setPartnerLeName(result.get("entity_name").toString());
		classificationBean.setPartnerLeId((Integer) result.get("le_id"));
		return classificationBean;
	}

	public PartnerDetailsBean getPartnerDetailsWithAttributes(Integer partnerId) {
		PartnerDetailsBean partnerDetailsBean = getPartnerDetails(partnerId);
		List<PartnerLegalEntity> partnerLegalEntities = partnerLegalEntityRepository.findByPartnerId(partnerId);
		if (!partnerLegalEntities.isEmpty()) {
			PartnerLegalEntity partnerLegalEntity = partnerLegalEntities.stream().findFirst().get();

			LOGGER.info("Partner legal entity selected is {}", partnerLegalEntity.getId());
			MstLeAttribute mstLeAttribute = getMstLeAttributeId(
					PartnerCustomerConstants.MST_ATTRIBUTE_SUPPLIER_LE_OWNER);
			PartnerLeAttributeValue partnerLeAttributeValue = getPartnerLegalAttribute(
					partnerLegalEntity.getId(), mstLeAttribute);
			partnerDetailsBean.setEntityOwnerName(partnerLeAttributeValue.getAttributeValues());
		}
		return partnerDetailsBean;
	}

	/**
	 * Method to get Partner Monthly Incentive Details from Callidus and SAP
	 *
	 * @param partnerId
	 * @return {@link Map}
	 */
	public Map<String, PartnerMonthlyIncentive> getPartnerMonthlyIncentives(Integer partnerId) {
		Objects.requireNonNull(partnerId, "PartnerID cannot be null");
		List<String> partnerCUIDs = getPartnerCUIDs(partnerId);

		Map<String, PartnerMonthlyIncentive> response = new HashMap<>();
		getCallidusIncentives(partnerCUIDs, response);
		getSAPIncentives(partnerCUIDs, response);
		return response;
	}

	/**
	 * Get Partner CUID by partner Id
	 *
	 * @param partnerId
	 * @return {@link List}
	 * @throws TclCommonException
	 */
	public List<String> getPartnerCUIDs(Integer partnerId) {
		List<PartnerLegalEntityBean> partnerLegalEntityBeans = getPartnerLegalEntitiesByPartnerId(partnerId);
		return partnerLegalEntityBeans.stream().map(PartnerLegalEntityBean::getTpsSfdcCuid)
				.collect(Collectors.toList());
	}

	private Map<String, PartnerMonthlyIncentive> getCallidusIncentives(List<String> partnerCUIDs,
			Map<String, PartnerMonthlyIncentive> response) {
		List<CallidusPartnerCommisions> callidusPartnerCommisions = getCallidusPartnerCommisions(partnerCUIDs);
		List<CallidusPartnerCommisions> pastAndPresentFinancialYearCommissions = filterCallidusIncludingOnlyPastAndPresentFinancialYear(
				callidusPartnerCommisions);
		PartnerMonthlyIncentive partnerMonthlyIncentive = calculateCallidusIncentive(
				pastAndPresentFinancialYearCommissions);
		response.put(CALLDUS_ELIGIBLE_INCENTIVES, partnerMonthlyIncentive);
		return response;
	}

	/**
	 * Method to filter callidus data based on past and present financial year
	 *
	 * @param callidusPartnerCommisions
	 */
	private List<CallidusPartnerCommisions> filterCallidusIncludingOnlyPastAndPresentFinancialYear(
			List<CallidusPartnerCommisions> callidusPartnerCommisions) {
		LocalDate pastFinancialYearStartDate = getLastFinancialYear().get(START_DATE);
		LocalDate presentFinancialYearEndDate = getPresentFinancialYear().get(END_DATE);
		return callidusPartnerCommisions.stream().filter(
				commisions -> convertStringToLocalDate(commisions.getCompDate()).isAfter(pastFinancialYearStartDate)
						&& convertStringToLocalDate(commisions.getCompDate()).isBefore(presentFinancialYearEndDate))
				.collect(Collectors.toList());
	}

	private Map<String, PartnerMonthlyIncentive> getSAPIncentives(List<String> partnerCUIDs,
			Map<String, PartnerMonthlyIncentive> response) {
		List<SapCommissions> sapCommissions = getSapCommissions(partnerCUIDs);
		// TODO: Method to get SAP Information
		PartnerMonthlyIncentive partnerMonthlyIncentive = calculateSAPIncentive(sapCommissions);
		response.put(SAP_RECEIVED_INCENTIVES, partnerMonthlyIncentive);
		return response;
	}

	/**
	 * Method to get SAP commissions
	 *
	 * @param partnerCUIDs
	 * @return {@link List}
	 */
	private List<SapCommissions> getSapCommissions(List<String> partnerCUIDs) {
		List<SapCommissions> sapCommissions = sapDataRepository.findAllByPartnerIdIn(partnerCUIDs).stream()
				.map(SapCommissions::fromSAPData).collect(Collectors.toList());
		return sapCommissions;
	}

	/**
	 * Method to calcualte callidus incentive
	 *
	 * @param bean
	 * @return {@link PartnerMonthlyIncentive}
	 */
	private PartnerMonthlyIncentive calculateCallidusIncentive(List<CallidusPartnerCommisions> bean) {
		PartnerMonthlyIncentive partnerMonthlyIncentive = new PartnerMonthlyIncentive();
		partnerMonthlyIncentive.setOneTimeIncentive(Double.valueOf(0));
		partnerMonthlyIncentive.setSellWithIncentive(Double.valueOf(0));
		if (!CollectionUtils.isEmpty(bean)) {
			// TODO:Filter based on year and calculation criteria,and currency conversion
			// clarification is required.
			Map<String, List<CallidusPartnerCommisions>> groupByCommisssionType = bean.stream()
					.collect(Collectors.groupingBy(CallidusPartnerCommisions::getCommissionType, Collectors.toList()));
			groupByCommisssionType.forEach((commissionType, incentives) -> {
				if (commissionType.contains(PartnerCustomerConstants.CALLIDUS_SELL_WITH_COMISSION)) {
					partnerMonthlyIncentive.setSellWithIncentive(incentives.stream()
							.mapToDouble(CallidusPartnerCommisions::getIncentiveValueInBaseCurrency).sum());
				} else if (commissionType.contains(PartnerCustomerConstants.CALLIDUS_LEAD_REFERRAL_COMISSION)) {
					partnerMonthlyIncentive.setOneTimeIncentive(incentives.stream()
							.mapToDouble(CallidusPartnerCommisions::getIncentiveValueInBaseCurrency).sum());
				}
			});

			partnerMonthlyIncentive.setIncentive(
					partnerMonthlyIncentive.getSellWithIncentive() + partnerMonthlyIncentive.getOneTimeIncentive());
			partnerMonthlyIncentive.setCurrency(bean.stream().findFirst().get().getPartnerBaseCurrency());
			// List<CallidusPartnerMonthlyIncentive> incentives =
			// bean.stream().filter(callidusPartnerMonthlyIncentive ->
			// callidusPartnerMonthlyIncentive.getYear().equals(LocalDate.now().getYear())).collect(Collectors.toList());
		}
		return partnerMonthlyIncentive;
	}

	/**
	 * Get Commission details of all partner legal id's from Callidus
	 *
	 * @param partnerCUIDs
	 * @return {@link List<CallidusPartnerCommisions>}
	 */
	public List<CallidusPartnerCommisions> getCallidusPartnerCommisions(List<String> partnerCUIDs) {
		List<CallidusPartnerCommisions> callidusPartnerCommisions = callidusDataRepository
				.findAllByPartnerIdIn(partnerCUIDs).stream().map(CallidusPartnerCommisions::fromCallidusData)
				.collect(Collectors.toList());
		return callidusPartnerCommisions;
	}

	/**
	 * Method to calcualte SAP incentive
	 *
	 * @param bean
	 * @return {@link PartnerMonthlyIncentive}
	 */
	private PartnerMonthlyIncentive calculateSAPIncentive(List<SapCommissions> bean) {
		PartnerMonthlyIncentive partnerMonthlyIncentive = new PartnerMonthlyIncentive();
		if (!CollectionUtils.isEmpty(bean)) {
			partnerMonthlyIncentive.setIncentive(bean.stream().mapToDouble(SapCommissions::getCommissionsPaid).sum());
			partnerMonthlyIncentive.setCurrency(bean.stream().findFirst().get().getCurrency());
		}
		return partnerMonthlyIncentive;
	}

	public PartnerCommissionsResponse getPartnerCommissionDetails(Integer partnerId,
			CallidusIncentiveRequestBean callidusIncentiveRequestBean) {
		validatePartnerId(partnerId);
		List<String> partnerCUIDs = getPartnerCUIDs(partnerId);
		List<CallidusPartnerCommisions> callidusPartnerCommisions = getCallidusPartnerCommisions(partnerCUIDs);
		return getPartnerCommissionResponse(callidusPartnerCommisions, callidusIncentiveRequestBean);
	}

	/**
	 * Validate the provided partnerId
	 *
	 * @param partnerId
	 */
	public void validatePartnerId(Integer partnerId) {
		Partner partner = partnerRepository.findById(partnerId).orElseGet(() -> {
			return null;
		});
		if (Objects.isNull(partner)) {
			throw new TclCommonRuntimeException(ExceptionConstants.INVALID_PARTNER_ID,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Response bean for Partner Commissions from Callidus
	 *
	 * @param customerName
	 * @param callidusPartnerCommisions
	 * @param callidusIncentiveRequestBean
	 * @return {@link PartnerCommissionsResponse}
	 */
	private PartnerCommissionsResponse getPartnerCommissionResponse(
			List<CallidusPartnerCommisions> callidusPartnerCommisions,
			CallidusIncentiveRequestBean callidusIncentiveRequestBean) {
		PartnerCommissionsResponse partnerCommissionsResponse = new PartnerCommissionsResponse();
		String callidusCommissionType = getCallidusCommisionType(callidusIncentiveRequestBean.getCommissionType());
		getComissionsDetails(callidusPartnerCommisions, callidusIncentiveRequestBean, callidusCommissionType,
				partnerCommissionsResponse);
		getEligibleProductsAndCurrencyAndCustomer(callidusCommissionType, callidusPartnerCommisions,
				partnerCommissionsResponse);
		return partnerCommissionsResponse;
	}

	/**
	 * Get callidus commission type based on commssiong type from Request param
	 *
	 * @param commisionType
	 * @return {@link String}
	 */
	private String getCallidusCommisionType(String commisionType) {
		String callidusCommissionType = null;
		if (commisionType.equalsIgnoreCase(PartnerCustomerConstants.SELL_WITH_COMISSION)) {
			callidusCommissionType = PartnerCustomerConstants.CALLIDUS_SELL_WITH_COMISSION;
		} else if (commisionType.equalsIgnoreCase(PartnerCustomerConstants.ONE_TIME_REFERRAL_COMISSION)) {
			callidusCommissionType = PartnerCustomerConstants.CALLIDUS_LEAD_REFERRAL_COMISSION;
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.INVALID_CALLIDUS_COMMISSION_TYPE,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return callidusCommissionType;
	}

	/**
	 * Get Commission Details of partner by product name and currency and type
	 *
	 * @param callidusPartnerCommisions
	 * @param productName
	 * @param currency
	 * @param commisionType
	 * @param partnerCommissionsResponse
	 */
	private void getComissionsDetails(List<CallidusPartnerCommisions> callidusPartnerCommisions,
			CallidusIncentiveRequestBean callidusIncentiveRequestBean, String callidusCommissionType,
			PartnerCommissionsResponse partnerCommissionsResponse) {
		List<CallidusDetailsBean> commissions = new ArrayList<>();
		callidusPartnerCommisions = filterCallidusPartnerComissionsByProductName(callidusPartnerCommisions,
				callidusIncentiveRequestBean.getProductName());
		callidusPartnerCommisions = filterCallidusPartnerComissionsByCurrency(callidusPartnerCommisions,
				callidusIncentiveRequestBean.getCurrency());

		if (callidusCommissionType.equalsIgnoreCase(PartnerCustomerConstants.CALLIDUS_SELL_WITH_COMISSION)) {
			callidusPartnerCommisions = filterCallidusPartnerComissionsByCustomerName(callidusPartnerCommisions,
					callidusIncentiveRequestBean.getCustomerName());
		}

		callidusPartnerCommisions = searchByOpportunityId(callidusPartnerCommisions,
				callidusIncentiveRequestBean.getOpportunityId());

		Map<String, List<CallidusPartnerCommisions>> groupByCommisssionType = callidusPartnerCommisions.stream()
				.collect(Collectors.groupingBy(CallidusPartnerCommisions::getCommissionType, Collectors.toList()));

		groupByCommisssionType.forEach((type, callidusCommisions) -> {
			if (type.contains(callidusCommissionType)) {
				commissions.addAll(CallidusDetailsBean.fromCallidusPartnerCommissions(callidusCommisions));
			}
		});

		partnerCommissionsResponse.setCommissions(commissions);
	}

	/**
	 * Method to filter based on opportunity Id
	 *
	 * @param callidusPartnerCommisions
	 * @param opportunityId
	 * @return {@link List}
	 */
	private List<CallidusPartnerCommisions> searchByOpportunityId(
			List<CallidusPartnerCommisions> callidusPartnerCommisions, String opportunityId) {
		if (Objects.nonNull(opportunityId)) {
			return callidusPartnerCommisions.stream()
					.filter(commission -> commission.getOpptyId().contains(opportunityId)).collect(Collectors.toList());
		} else {
			return callidusPartnerCommisions;
		}
	}

	/**
	 * Filter Callidus commissions by customer Name
	 *
	 * @param callidusPartnerCommisions
	 * @param customerName
	 * @return {@link List}
	 */
	private List<CallidusPartnerCommisions> filterCallidusPartnerComissionsByCustomerName(
			List<CallidusPartnerCommisions> callidusPartnerCommisions, String customerName) {
		if (PartnerCustomerConstants.ALL.equalsIgnoreCase(customerName)) {
			return callidusPartnerCommisions;
		} else {
			return callidusPartnerCommisions.stream().filter(callidusPartnerCommision -> customerName
					.equalsIgnoreCase(callidusPartnerCommision.getCustomerName())).collect(Collectors.toList());
		}
	}

	/**
	 * Get Callidus Partner Commissions By Product Name
	 *
	 * @param callidusPartnerCommisions
	 * @param productName
	 * @return {@link List}
	 */
	private List<CallidusPartnerCommisions> filterCallidusPartnerComissionsByProductName(
			List<CallidusPartnerCommisions> callidusPartnerCommisions, String productName) {
		if (PartnerCustomerConstants.ALL.equalsIgnoreCase(productName)) {
			return callidusPartnerCommisions.stream()
					.filter(callidusPartnerCommision -> Arrays
							.asList(PartnerCustomerConstants.CALLIDUS_GSC_PRODUCT,
									PartnerCustomerConstants.CALLIDUS_GVPN_PRODUCT,
									PartnerCustomerConstants.CALLIDUS_ILL_PRODUCT)
							.contains(callidusPartnerCommision.getServiceName()))
					.collect(Collectors.toList());
		} else {
			return callidusPartnerCommisions.stream().filter(
					callidusPartnerCommision -> callidusPartnerCommision.getServiceName().equalsIgnoreCase(productName))
					.collect(Collectors.toList());
		}
	}

	/**
	 * Get Callidus Partner Commissions By Currency
	 *
	 * @param callidusPartnerCommisions
	 * @param currency
	 * @return {@link List}
	 */
	private List<CallidusPartnerCommisions> filterCallidusPartnerComissionsByCurrency(
			List<CallidusPartnerCommisions> callidusPartnerCommisions, String currency) {
		if (PartnerCustomerConstants.ALL.equalsIgnoreCase(currency)) {
			return callidusPartnerCommisions;
		} else {
			return callidusPartnerCommisions.stream().filter(
					callidusPartnerCommision -> callidusPartnerCommision.getTranCurrency().equalsIgnoreCase(currency))
					.collect(Collectors.toList());
		}
	}

	/**
	 * Get Eligible Product and Currency and Customer of a partner
	 *
	 * @param commisionType
	 * @param callidusPartnerCommisions
	 * @param partnerCommissionsResponse
	 */
	private void getEligibleProductsAndCurrencyAndCustomer(String callidusCommissionType,
			List<CallidusPartnerCommisions> callidusPartnerCommisions,
			PartnerCommissionsResponse partnerCommissionsResponse) {
		partnerCommissionsResponse.setEligibleProducts(callidusPartnerCommisions.stream()
				.filter(commission -> commission.getCommissionType().contains(callidusCommissionType))
				.map(CallidusPartnerCommisions::getServiceName).distinct().collect(Collectors.toList()));

		partnerCommissionsResponse.setEligibleCurrency(callidusPartnerCommisions.stream()
				.filter(commission -> commission.getCommissionType().contains(callidusCommissionType))
				.map(CallidusPartnerCommisions::getTranCurrency).distinct().collect(Collectors.toList()));

		if (callidusCommissionType.equalsIgnoreCase(PartnerCustomerConstants.CALLIDUS_SELL_WITH_COMISSION)) {
			partnerCommissionsResponse.setCustomerNames(callidusPartnerCommisions.stream()
					.filter(commission -> commission.getCommissionType()
							.equalsIgnoreCase(PartnerCustomerConstants.CALLIDUS_SELL_WITH_COMISSION))
					.map(CallidusPartnerCommisions::getCustomerName).filter(Objects::nonNull).distinct()
					.collect(Collectors.toList()));
		}
	}

	/**
	 * Method to get Present Financial Year Details
	 *
	 * @return {@link Map<String, LocalDate>}
	 */
	private static Map<String, LocalDate> getPresentFinancialYear() {
		Map<String, LocalDate> dates = new HashMap();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int financialYear = month < 3 ? currentYear - 1 : currentYear;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MMM_YYYY);
		dates.put(START_DATE, LocalDate.parse(FIRST_APRIL + String.valueOf(financialYear - 1), formatter));
		dates.put(END_DATE, LocalDate.parse(THIRTY_FIRST_MARCH + String.valueOf(financialYear), formatter));
		return dates;
	}

	/**
	 * Method to get Last Financial Year Details
	 *
	 * @return {@link Map<String, LocalDate>}
	 */
	private Map<String, LocalDate> getLastFinancialYear() {
		Map<String, LocalDate> dates = new HashMap();
		int pastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MMM_YYYY);
		dates.put(START_DATE, LocalDate.parse(FIRST_APRIL + String.valueOf(pastYear - 1), formatter));
		dates.put(END_DATE, LocalDate.parse(THIRTY_FIRST_MARCH + String.valueOf(pastYear), formatter));
		return dates;
	}

	/**
	 * Method to get convert String to local date
	 *
	 * @return {@link Map<String, LocalDate>}
	 */
	private LocalDate convertStringToLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(D_MMM_YY);
		return LocalDate.parse(date, formatter);
	}

	/**
	 * Get Partners Detail By Partner Id
	 *
	 * @param partnerIds
	 * @return {@link List<PartnerDetailsBean>}
	 */
	public List<PartnerDetailsBean> getPartnerDetailsByPartnerIds(List<Integer> partnerIds) {
		List<PartnerDetailsBean> partnerDetailsBeansList= new ArrayList<>();
		List<Partner> partners = partnerRepository.findAllById(partnerIds);
		List<Integer> partnerLeIds = new ArrayList<>();
		partners.stream().forEach(partner->partner.getPartnerLegalEntities().
		forEach(legal->partnerLeIds.add(legal.getId())));
		List<Integer> partnerLeIDs = new ArrayList<>();
		userInfoUtils.getPartnerDetails().forEach(les->partnerLeIDs.add(les.getPartnerLeId()));
		
		partners.stream().forEach(partner -> {
			PartnerDetailsBean partnerDetailsBean = new PartnerDetailsBean();
			partnerDetailsBean.setPartnerId(partner.getId());
			partnerDetailsBean.setPartnerName(partner.getName());
			Set<String> customerCuid = new HashSet<>();
			partner.getPartnerLegalEntities().stream().forEach(legal -> {
				if (partnerLeIDs.contains(legal.getId())) {
					customerCuid.add(legal.getTpsSfdcCuid());
					partnerDetailsBean.setCuid(customerCuid);
				}
			});
			partnerDetailsBeansList.add(partnerDetailsBean);
		});
//		return partners.stream().map(partner -> {
//			PartnerDetailsBean partnerDetailsBean = new PartnerDetailsBean();
//			partnerDetailsBean.setPartnerId(partner.getId());
//			partnerDetailsBean.setPartnerName(partner.getName());
//			return partnerDetailsBean;
//		}).collect(Collectors.toList());
		return partnerDetailsBeansList;
	}

	/**
	 * Get Partner commissions by data range
	 *
	 * @param partnerCUIDs
	 * @param startDate
	 * @param endDate
	 * @return {@link List}
	 */
	public List<CallidusPartnerCommisions> getPartnerCommisionsBetweenDateRange(List<String> partnerCUIDs,
			String startDate, String endDate) {
		List<CallidusPartnerCommisions> callidusPartnerCommisions = callidusDataRepository
				.findCommissionDataByDateRange(partnerCUIDs, startDate, endDate).stream()
				.map(CallidusPartnerCommisions::fromCallidusData).collect(Collectors.toList());
		return callidusPartnerCommisions;
	}

	/**
	 * 
	 * Get all Partner legal entity with pagination and search
	 * 
	 * @param name
	 * @param page
	 * @param size
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<PartnerLegalEntityBean> searchPartnerLegalEntity(String name, Integer page, Integer size,
			Integer partnerId, List<Integer> partnerLeIds) throws TclCommonException {
		if (page == null || size == null || page <= 0 || size <= 0) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<PartnerLegalEntityBean> partnerLeBeanList = new ArrayList<>();
			Page<PartnerLegalEntity> partnerLes = null;
			Specification<PartnerLegalEntity> specs = PartnerLegalEntitySpecification.getAllLegalEntities(partnerId,
					name, partnerLeIds);
			partnerLes = partnerLegalEntityRepository.findAll(specs, PageRequest.of(page - 1, size));
			if (partnerLes != null) {
				partnerLes.stream().forEach(partnerLe -> {
					PartnerLegalEntityBean partnerLegalEntityBean = constructPartnerLeBean(partnerLe);
					if (partnerLegalEntityBean != null)
						partnerLeBeanList.add(partnerLegalEntityBean);
				});
			}
			if (partnerLes != null) {
				return new PagedResult(partnerLeBeanList, partnerLes.getTotalElements(), partnerLes.getTotalPages());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	private PartnerLegalEntityBean constructPartnerLeBean(PartnerLegalEntity partnerLe) {
		if (partnerLe != null) {
			PartnerLegalEntityBean partnerLegalEntityBean = new PartnerLegalEntityBean();
			partnerLegalEntityBean.setId(partnerLe.getId());
			partnerLegalEntityBean.setPartnerId(partnerLe.getPartner().getId());
			partnerLegalEntityBean.setEntityName(partnerLe.getEntityName());
			partnerLegalEntityBean.setTpsSfdcCuid(partnerLe.getTpsSfdcCuid());
			return partnerLegalEntityBean;
		}
		return null;
	}

	/**
	 * 
	 * Get all Partner with pagination and search
	 * 
	 * @param name
	 * @param page
	 * @param size
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<PartnerDetailsBean> searchPartner(String name, Integer page, Integer size)
			throws TclCommonException {
		if (page == null || size == null || page <= 0 || size <= 0) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<PartnerDetailsBean> partnerDetailsBeans = new ArrayList<>();
			Page<Partner> partners = null;
			Specification<Partner> specs = PartnerSpecification.getAllCustomers(name);
			partners = partnerRepository.findAll(specs, PageRequest.of(page - 1, size));
			if (partners != null) {
				partners.stream().forEach(partner -> {
					PartnerDetailsBean partnerDetailsBean = constructPartnerDetailBean(partner);
					if (partnerDetailsBean != null)
						partnerDetailsBeans.add(partnerDetailsBean);
				});
			}
			if (partners != null) {
				return new PagedResult(partnerDetailsBeans, partners.getTotalElements(), partners.getTotalPages());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	private PartnerDetailsBean constructPartnerDetailBean(Partner partner) {
		if (partner != null) {
			PartnerDetailsBean partnerDetailsBean = new PartnerDetailsBean();
			partnerDetailsBean.setPartnerId(partner.getId());
			partnerDetailsBean.setPartnerName(partner.getName());
			return partnerDetailsBean;
		}
		return null;
	}

	/**
	 * Get Partner NNI ID by Partner ID
	 *
	 * @param partnerId
	 * @return {@link PartnerNNIBean}
	 */
	public PartnerNNIBean getPartnerNNIIDs(Integer partnerId) {
		PartnerNNIBean partnerNNIBean = new PartnerNNIBean();
		partnerNNIBean.setPartnerId(partnerId);

		MstCustomerSpAttribute mstCustomerSpAttributeForNNIID = mstCustomerSpAttributeRepository.findByNameAndStatus(NNI_ID, (byte) 1);

		PartnerAttributeValue nniIDValues = new PartnerAttributeValue();
		if(Objects.nonNull(mstCustomerSpAttributeForNNIID)) {
			nniIDValues = partnerAttributeValueRepository.findByPartnerIdAndMstCustomerSpAttributeId(partnerId,
					mstCustomerSpAttributeForNNIID.getId());
			if(Objects.nonNull(nniIDValues.getAttributeValues())) {
				partnerNNIBean.setNniIDs(Arrays.asList(nniIDValues.getAttributeValues().split(COMMA)));
			}
		}

		return partnerNNIBean;
	}

	/**
	 * Get Partner Deatils For Notification By Partner Le ID
	 * @param partnerLeId
	 * @return {@link PartnerNotificationDetail}
	 */
	public PartnerNotificationDetail getPartnerDetailsForNotification(String partnerLeId) {
		List<Map<String, Object>> partnerDetailsForNotification = partnerRepository.findPartnerDetailsForNotification(partnerLeId);
		List<PartnerNotificationDetail> partnerNotificationDetails = Utils.mapRows(() -> partnerDetailsForNotification, PartnerNotificationDetail::toConvert);
		String orgIds = partnerNotificationDetails.stream().map(PartnerNotificationDetail::getOrgId).collect(Collectors.joining(COMMA));
		partnerNotificationDetails.stream().findFirst().get().setPartnerOrgId(orgIds);
		return partnerNotificationDetails.stream().findFirst().get();
	}

	/**
	 * Get partner products based on classification
	 *
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	public Set<MstClassificationProductBean> getPartnerProductClassification(Integer partnerId) throws TclCommonException {

		List<PartnerProductConfiguration> partnerProductConfiguration = partnerProductConfigurationRepository.findByPartnerId(partnerId);
		Set<MstProductFamilyBean> mstProductFamilyBeans;
		try {
			String partnerProductResponse = (String) mqUtils.sendAndReceive(partnerProductQueue, null);
			LOGGER.info("All Products :: {}", partnerProductResponse);

			mstProductFamilyBeans = Utils.fromJson(partnerProductResponse,
					new TypeReference<Set<MstProductFamilyBean>>() {
					});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		Map<String, Set<PartnerProductConfiguration>> productMapByClassification = partnerProductConfiguration.stream().collect(Collectors.groupingBy(configuration -> configuration.getClassification(), Collectors.toSet()));

		return createMstClassificationProductBean(productMapByClassification, mstProductFamilyBeans);
	}

	public Set<MstProductFamilyBean> getPartnerProductClassificationv1(Integer partnerId) throws TclCommonException{
		Set<MstProductFamilyBean> partnerProductClassificationList=new HashSet<>();
		List<PartnerProductConfiguration> partnerProductConfiguration=partnerProductConfigurationRepository.findByPartnerId(partnerId);
		List<Integer> productCatalogList= partnerProductConfiguration.stream().map(PartnerProductConfiguration::getErfProdCatalogProductFamilyId).distinct().collect(Collectors.toList());
		try {
			String partnerProductResponse = (String) mqUtils.sendAndReceive(partnerProductQueue, null);
			LOGGER.info("All Products :: {}", partnerProductResponse);

			List<MstProductFamilyBean> mstProductFamilyBeans = Utils.fromJson(partnerProductResponse,
					new TypeReference<List<MstProductFamilyBean>>() {
					});
			for(Integer productId:productCatalogList){
				List<String> classifications=new ArrayList<>();
				classifications=partnerProductConfiguration.stream().filter(optClassification->optClassification.getErfProdCatalogProductFamilyId().equals(productId)).map(PartnerProductConfiguration::getClassification).distinct().collect(Collectors.toList());
				MstProductFamilyBean productFamilyBean=mstProductFamilyBeans.stream().filter(product->product.getProductCatalogFamilyId().equals(productId)).findFirst().get();
				/* The below condition if else is to removed after sell through implementation*/
				productFamilyBean.setClassification(classifications);
				partnerProductClassificationList.add(productFamilyBean);
			}
		}
		catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return partnerProductClassificationList;
	}

	private Set<MstClassificationProductBean> createMstClassificationProductBean(Map<String, Set<PartnerProductConfiguration>> productMapClassifications, Set<MstProductFamilyBean> mstProductFamilyBeans) {
		return productMapClassifications.entrySet().stream().map(productMapClassification -> {
			MstClassificationProductBean mstClassificationProductBean = new MstClassificationProductBean();
			mstClassificationProductBean.setClassification(productMapClassification.getKey());
			mstClassificationProductBean.setProducts(productMapClassification.getValue().stream().map(product -> {
				return mstProductFamilyBeans.stream().filter(mstProductFamilyBean -> mstProductFamilyBean.getProductCatalogFamilyId().equals(product.getErfProdCatalogProductFamilyId())).findFirst().get();
			}).collect(Collectors.toSet()));
			return mstClassificationProductBean;
		}).collect(Collectors.toSet());
	}

	public Set<PartnerProfileBean> getPartnerProfileListByPartnerId(Integer partnerId) {
		Set<PartnerProfileBean> partnerProfileBeanList = new HashSet<>();
		List<PartnerProfile> partnerProfileList = partnerProfileRepository.findProfileByPartnerId(partnerId);
		partnerProfileBeanList = partnerProfileList.stream().map(partnerProfile -> {
			PartnerProfileBean partnerProfileBean = new PartnerProfileBean();
			partnerProfileBean.setCode(partnerProfile.getCode());
			partnerProfileBean.setId(partnerProfile.getId());
			partnerProfileBean.setIsActive(partnerProfile.getIsActive());
			partnerProfileBean.setName(partnerProfile.getName());
			return partnerProfileBean;
		}).collect(Collectors.toSet());

		return partnerProfileBeanList;
	}

	/**
	 * Get Partner's Classification
	 *
	 * @param partnerId
	 * @return {@link PartnerProductClassificationBean}
	 */
	public String getIsClassificationsForPartner(Integer partnerId,String classification) {
		String response=null;
		PartnerProductClassificationBean partnerProductClassificationBean =getPartnerClassifications(partnerId);
		Set<String> listOfClassifcation=partnerProductClassificationBean.getClassification();
		if(listOfClassifcation.contains(classification)){
			response=PartnerCustomerConstants.ENABLED;
		}
		else{
			response=PartnerCustomerConstants.DISABLED;
		}
		return response;
	}

	/**
	 * Get Partner Le Currency
	 *
	 * @param partnerLeId
	 * @return
	 */
	public List<String> getPartnerLeCurrencies(Integer partnerLeId) {
		LOGGER.info("Partner LE Id :: {}", partnerLeId);
		List<String> partnerLeCurrency = partnerLeCurrencyRepository.getPartnerLeCurrency(partnerLeId);
		LOGGER.info("Partner Le Currencies :: {}", partnerLeCurrency);
		return partnerLeCurrency;
	}

	/**
	 * Get Temp end customer details from oms by partner id
	 *
	 * @param partnerId
	 * @return {@link PartnerDetailsBean}
	 * @throws TclCommonException
	 */
	public List<PartnerTempCustomerDetailsBean> getTempEndCustomerDetailsMQ(Integer partnerId) {
		List<PartnerTempCustomerDetailsBean> partnerTempCustomerDetailsBeans = new ArrayList<>();
		String response;
		try {
			LOGGER.info("MDC Filter token value in before Queue call partner temp end customers {} :");
			response = (String) mqUtils.sendAndReceive(partnerTempEndCustomerQueue, Utils.convertObjectToJson(partnerId));
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_TEMP_END_CUSTOMER_DETAILS_MQ_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		if (Objects.nonNull(response)) {
			partnerTempCustomerDetailsBeans = Utils.fromJson(response, new TypeReference<List<PartnerTempCustomerDetailsBean>>() {
			});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_TEMP_END_CUSTOMER_DETAILS_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		return partnerTempCustomerDetailsBeans;
	}

	/**
	 * Get partner End customer details by partnerEndCustomerCuid
	 *
	 * @param partnerEndCustomerCuid
	 * @return {@link PartnerEndCustomerMappingBean}
	 * @throws TclCommonException
	 */
	public PartnerEndCustomerMappingBean getPartnerEndCustomerMappingDetails(String partnerEndCustomerCuid){
		PartnerEndCustomerMappingBean partnerEndCustomerMappingBean = null;
		PartnerEndCustomerMapping partnerEndCustomerMapping = partnerEndCustomerMappingRepository.findByEndCustomerCuid(partnerEndCustomerCuid);
		if(Objects.nonNull(partnerEndCustomerMapping)){
			partnerEndCustomerMappingBean = new PartnerEndCustomerMappingBean();
			partnerEndCustomerMappingBean.setPartnerCustomerLeName(Objects.isNull(partnerEndCustomerMapping.getEndCustomerLeName())
					? "" : partnerEndCustomerMapping.getEndCustomerLeName());

			/*partnerEndCustomerMappingBean.setPartnerCustomerLeWebsite(Objects.isNull(partnerEndCustomerMapping.getEndCustomerWebsite())
					? "" : partnerEndCustomerMapping.getEndCustomerWebsite());
			partnerEndCustomerMappingBean.setPartnerCustomerContactEmail(Objects.isNull(partnerEndCustomerMapping.getEndCustomerContactEmail())
					? "" : partnerEndCustomerMapping.getEndCustomerContactEmail());
			partnerEndCustomerMappingBean.setPartnerCustomerContactName(Objects.isNull(partnerEndCustomerMapping.getEndCustomerContactName())
					? "" : partnerEndCustomerMapping.getEndCustomerContactName());*/
			loadAddressDetails(partnerEndCustomerMapping.getEndCustomerErfLocId(), partnerEndCustomerMappingBean);
		}
		return partnerEndCustomerMappingBean;
	}

	/**
	 * Get location details for Partner End customer
	 *
	 * @param customerErfLocationId
	 * @return {@link PartnerEndCustomerMappingBean}
	 * @throws TclCommonException
	 */
	public void loadAddressDetails(Integer customerErfLocationId, PartnerEndCustomerMappingBean partnerEndCustomerMappingBean){
		String response = null;
		try {
			//MQ call to get location details
			response = (String) mqUtils.sendAndReceive(getPartnerContractingAddressQueue, String.valueOf(customerErfLocationId));
			PartnerContractingAddress addressDetails = Utils.convertJsonToObject(response, PartnerContractingAddress.class);
			partnerEndCustomerMappingBean.setPartnerCustomerAddress(Objects.isNull(addressDetails.getAddress())
					? "" : addressDetails.getAddress());
			partnerEndCustomerMappingBean.setPartnerCustomerLeCity(Objects.isNull(addressDetails.getCity())
					? "" : addressDetails.getCity());
			partnerEndCustomerMappingBean.setPartnerCustomerLeState(Objects.isNull(addressDetails.getState())
					? "" : addressDetails.getState());
			partnerEndCustomerMappingBean.setPartnerCustomerLeZip(Objects.isNull(addressDetails.getPincode())
					? "" : addressDetails.getPincode());
			partnerEndCustomerMappingBean.setPartnerCustomerLeCountry(Objects.isNull(addressDetails.getCountry())
					? "" : addressDetails.getCountry());
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
	}

}
