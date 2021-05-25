package com.tcl.dias.oms.gsc.service.multiLE;

import com.google.common.collect.ImmutableList;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SapCodeRequest;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.gsc.beans.GscAttachmentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.tiger.TigerServiceHelper;
import com.tcl.dias.oms.gsc.tiger.beans.NumberDetails;
import com.tcl.dias.oms.gsc.tiger.beans.NumberReservationResponse;
import com.tcl.dias.oms.gsc.util.*;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tcl.dias.oms.gsc.util.GscConstants.*;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATIONS_NULL_MESSAGE;
import static java.util.Objects.requireNonNull;

/**
 * Class for handling gsc order details (multiple LE)
 */
@Service
public class GscMultiLEOrderDetailService {

	Logger LOGGER = LoggerFactory.getLogger(GscMultiLEOrderDetailService.class);

	@Autowired
	GscMultiLEOrderService gscMultiLEOrderService;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderGscDetailRepository orderGscDetailRepository;

	@Autowired
	GscComponentAttributeValuesHelper attributeValuesPopulator;

	@Autowired
	GscOrderDetailService gscOrderDetailService;

	@Autowired
	GscIsoCountries gscIsoCountries;

	@Autowired
	GscRepcCountries gscRepcCountries;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	TigerServiceHelper tigerServiceHelper;

	@Autowired
	OrderGscTfnRepository orderGscTfnRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	GscOrderService gscOrderService;

	@Value("${rabbitmq.le.sap.queue}")
	String secsCodeQueue;

	@Autowired
	GscAttachmentHelper gscAttachmentHelper;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	GscAttachmentsRepository gscAttachmentRepository;

	/**
	 * Get order product solution
	 *
	 * @param solutionId
	 * @return
	 */
	public Stream<OrderProductSolution> getOrderProductSolution(Integer solutionId) {
		requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		return Stream.of(orderProductSolutionRepository.findById(solutionId)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.ORDER_PRODUCT_EMPTY)));
	}

	/**
	 * Update order configuration
	 *
	 * @param configuration
	 * @return
	 */
	private GscOrderConfigurationBean updateOrderConfiguration(GscOrderConfigurationBean configuration) {
		return orderGscDetailRepository.findById(configuration.getId()).map(orderGscDetail -> {
			orderGscDetail.setDest(configuration.getDestination());
			orderGscDetail.setArc(configuration.getArc());
			orderGscDetail.setMrc(configuration.getMrc());
			orderGscDetail.setNrc(configuration.getNrc());
			orderGscDetail.setSrc(configuration.getSource());
			orderGscDetailRepository.save(orderGscDetail);
			return attributeValuesPopulator.handleComponentAttributeValues(configuration,
					attributeMap -> gscMultiLEOrderService
							.saveOrderProductComponentAttributeMap(attributeMap, configuration.getId()));
		}).orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.GSC_ORDER_EMPTY));
	}

	/**
	 * Update order configurations
	 *
	 * @param orderId
	 * @param solutionId
	 * @param orderGscId
	 * @param configurations
	 * @return
	 * @throws TclCommonException
	 */
	public List<GscOrderConfigurationBean> updateOrderConfigurations(Integer orderId, Integer solutionId,
			Integer orderGscId, List<GscOrderConfigurationBean> configurations) throws TclCommonException {
		requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		requireNonNull(orderGscId, ORDER_GSC_ID_NULL_MESSAGE);
		requireNonNull(configurations, CONFIGURATIONS_NULL_MESSAGE);
		gscMultiLEOrderService.fetchOrderById(orderId);
		getOrderProductSolution(solutionId);
		gscMultiLEOrderService.fetchOrderGscById(orderGscId);
		return configurations.stream().map(this::updateOrderConfiguration).collect(Collectors.toList());
	}

	/**
	 * Fetch order GSC detail by ID
	 *
	 * @param orderGscDetailId
	 * @return
	 */
	private Optional<OrderGscDetail> fetchOrderGscDetailById(Integer orderGscDetailId) {
		LOGGER.info("OrderGscDetail ID :: {}", orderGscDetailId);
		return Optional.of(orderGscDetailRepository.findById(orderGscDetailId)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.GSC_ORDER_EMPTY)));
	}

	/**
	 * Get country code for order GSC detail
	 *
	 * @param orderGscDetail
	 * @return
	 */
	private String getCountryCodeForOrderGscDetail(OrderGscDetail orderGscDetail) {
		String[] src = orderGscDetail.getSrc().split(":");
		String originCountryCode = Optional.ofNullable(gscIsoCountries.forName(src[0]))
				.map(GscIsoCountries.GscCountry::getCode)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR));
		return originCountryCode;
	}

	/**
	 * Get REPC country code for order gsc detail
	 *
	 * @param orderGscDetail
	 * @return
	 */
	private String getRepcCountryCodeForOrderGscDetail(OrderGscDetail orderGscDetail) {
		String[] src = orderGscDetail.getSrc().split(":");
		String originCountryCode = Optional.ofNullable(gscRepcCountries.forName(src[0]))
				.map(GscRepcCountries.GscCountry::getCode)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR));
		return originCountryCode;
	}

	/**
	 * To get customerLeSapCodeDetails
	 *
	 * @param customerLegalEntityId
	 * @return
	 * @throws TclCommonException
	 */
	public LeSapCodeResponse getCustomerLeSapCodeDetails(Integer customerLegalEntityId) throws TclCommonException {
		Objects.requireNonNull(customerLegalEntityId,
				"Error while getting customer LE details. Customer legal entity id cannot be null");
		SapCodeRequest requestBean = new SapCodeRequest();
		List<Integer> customerLeEntityIds = new ArrayList<>();
		customerLeEntityIds.add(customerLegalEntityId);
		requestBean.setCustomerLeIds(customerLeEntityIds);
		requestBean.setType(GscConstants.SECS_CODE);
		String jsonPayload = GscUtils.toJson(requestBean);
		LOGGER.info("MDC Filter token value in before Queue call getCustomerLeDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String mqResponse = (String) mqUtils.sendAndReceive(secsCodeQueue, jsonPayload);
		Objects.requireNonNull(mqResponse,
				String.format("Null response from customer service for customerLegalEntityId: %s",
						customerLegalEntityId));
		LOGGER.info("Response from the customer service ::{}", mqResponse);
		return GscUtils.fromJson(mqResponse, LeSapCodeResponse.class);
	}

	/**
	 * Get organisation ID of customer
	 *
	 * @param orderGscDetail
	 * @return
	 * @throws TclCommonException
	 */
	private String getOrganisationIdOfCustomer(OrderGscDetail orderGscDetail) throws TclCommonException {
		Integer erfCustomerLeId = orderGscDetail.getOrderGsc().getOrderToLe().getErfCusCustomerLegalEntityId();
		LeSapCodeResponse leSapCodeResponse = getCustomerLeSapCodeDetails(erfCustomerLeId);
		LeSapCodeBean leSapCodeBean = leSapCodeResponse.getLeSapCodes().stream().findFirst().get();
		LOGGER.info("leSapCodeBean :: {}", leSapCodeBean.toString());
		// SECS Code and Org No both are same
		String secsCode = leSapCodeBean.getCodeValue();
		LOGGER.info("secsCode :: {}", secsCode);
		/*return tigerServiceHelper.getOrganisationByCuId(secsCode, orderGscDetail.getId(), REF_TYPE_ORDER_GSC_DETAIL)
				.map(Organisation::getOrgId).orElseGet(() -> {
					LOGGER.warn(
							String.format("Organisation with secsCode: %s not found in tiger service", secsCode));
					return null;
				});*/
		return secsCode;
	}

	/**
	 * Get origin country calling code
	 *
	 * @param originCountryCode
	 * @return
	 */
	private int getOriginCountryCalllingCode(String originCountryCode) {
		String twoDigitCountryCode = gscIsoCountries.iso2ForISO3Code(originCountryCode);
		return PhoneNumberUtil.getInstance().getCountryCodeForRegion(twoDigitCountryCode);
	}

	/**
	 * Build reservation attribute bean
	 *
	 * @param reservationId
	 * @return
	 */
	private GscOrderProductComponentsAttributeSimpleValueBean buildReservationAttributeBean(String reservationId) {
		GscOrderProductComponentsAttributeSimpleValueBean valueBean = new GscOrderProductComponentsAttributeSimpleValueBean();
		valueBean.setAttributeName(GscConstants.TFN_Reservation_ID);
		valueBean.setAttributeValue(reservationId);
		valueBean.setDescription("Resevation Id for tfn numbers");
		valueBean.setDisplayValue(reservationId);
		return valueBean;
	}

	/**
	 * Save reservation ID as attribute
	 *
	 * @param numberReservationResponse
	 * @param orderGscDetail
	 * @param serviceType
	 * @return
	 */
	private Optional<NumberReservationResponse> saveReservationIdAsAttribute(
			NumberReservationResponse numberReservationResponse, OrderGscDetail orderGscDetail, String serviceType) {
		String reservationId = numberReservationResponse.getReservationId();
		MstProductComponent mstProductComponent = mstProductComponentRepository
				.findByNameAndStatus(serviceType, GscConstants.STATUS_ACTIVE).stream().findFirst().get();
		OrderProductComponent orderProductComponent = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(orderGscDetail.getId(), mstProductComponent).stream()
				.findFirst().get();
		gscOrderService
				.saveOrderComponentAttributeValue(orderProductComponent, buildReservationAttributeBean(reservationId));
		return Optional.ofNullable(numberReservationResponse);
	}

	/**
	 * Find non UIFN TFN number
	 *
	 * @param gscTfnBean
	 * @return
	 */
	private Optional<OrderGscTfn> findNonUIFNTfnNumber(GscTfnBean gscTfnBean) {
		return Optional.ofNullable(orderGscTfnRepository.findAllByTfnNumber(gscTfnBean.getNumber()))
				.orElse(ImmutableList.of()).stream().filter(orderGscTfn -> !UIFN
						.equalsIgnoreCase(orderGscTfn.getOrderGscDetail().getOrderGsc().getProductName())).findFirst();
	}

	/**
	 * Save GSC TFN bean
	 *
	 * @param gscTfnBean
	 * @param orderGscDetail
	 * @param countryCode
	 * @return
	 */
	private GscTfnBean saveGscTfnBean(GscTfnBean gscTfnBean, OrderGscDetail orderGscDetail, String countryCode) {
		OrderGscTfn orderGscTfn = new OrderGscTfn();
		if (orderGscDetail.getOrderGsc().getProductName().equalsIgnoreCase(UIFN)) {
			orderGscTfn = (Objects.nonNull(gscTfnBean.getId()) ? orderGscTfnRepository.findById(gscTfnBean.getId()) :
					Optional.ofNullable(orderGscTfnRepository
							.findByTfnNumberAndOrderGscDetail(gscTfnBean.getNumber(), orderGscDetail)))
					.orElse(new OrderGscTfn());
		} else {
			orderGscTfn = (Objects.nonNull(gscTfnBean.getId()) ? orderGscTfnRepository.findById(gscTfnBean.getId()) :
					findNonUIFNTfnNumber(gscTfnBean)).orElse(new OrderGscTfn());
		}
		orderGscTfn.setOrderGscDetail(orderGscDetail);
		orderGscTfn.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
		if (Objects.nonNull(gscTfnBean.getCity())) {
			orderGscTfn.setCountryCode(String.format("%s:%s", Optional.ofNullable(gscTfnBean.getCountry()).orElse(""),
					gscTfnBean.getCity()));
		} else {
			orderGscTfn.setCountryCode(countryCode);
		}
		if (Objects.isNull(orderGscTfn.getCreatedTime())) {
			orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
		}
		if (Objects.isNull(orderGscTfn.getCreatedBy())) {
			orderGscTfn.setCreatedBy(Utils.getSource());
		}
		orderGscTfn.setIsPorted(gscTfnBean.getPorted());
		orderGscTfn.setUpdatedBy(Utils.getSource());
		orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
		orderGscTfn.setTfnNumber(gscTfnBean.getNumber());
		orderGscTfnRepository.save(orderGscTfn);
		gscTfnBean.setId(orderGscTfn.getId());
		return gscTfnBean;
	}

	/**
	 * Reserve and save numbers
	 *
	 * @param tfns
	 * @param orderGscDetail
	 * @param originCountryCode
	 * @param serviceType
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private List<GscTfnBean> reserveAndSaveNumbers(List<GscTfnBean> tfns, OrderGscDetail orderGscDetail,
			String originCountryCode, String serviceType) throws TclCommonException, IllegalArgumentException {
		String orgId = getOrganisationIdOfCustomer(orderGscDetail);
		LOGGER.info("orgId :: {}", orgId);
		LOGGER.info("originCountryCode :: {}", originCountryCode);
		if (Objects.isNull(orgId)) {
			// if org id is not found in Tiger return empty number list
			return new ArrayList<>();
		}
		int originCountryCallingCode = getOriginCountryCalllingCode(originCountryCode);
		LOGGER.info("originCountryCallingCode :: {}", originCountryCallingCode);
		List<GscTfnBean> filteredTfns = tfns.stream()
				.filter(gscTfnBean -> !"0".equalsIgnoreCase(gscTfnBean.getNumber())).collect(Collectors.toList());
		if (Objects.isNull(filteredTfns) || filteredTfns.isEmpty())
			return tfns;
		if (DOMESTIC_VOICE.equalsIgnoreCase(serviceType)) {
			serviceType = DID;
		}
		String finalServiceType = serviceType;
		return Optional.ofNullable(tigerServiceHelper
				.reserveNumbersForOrganisation(orgId, String.valueOf(originCountryCallingCode), serviceType,
						filteredTfns, orderGscDetail.getId(), REF_TYPE_ORDER_GSC_DETAIL, ""))
				.filter(numberReservationResponses -> numberReservationResponses.isSuccess()).flatMap(
						numberReservationResponse -> saveReservationIdAsAttribute(numberReservationResponse.get(),
								orderGscDetail, finalServiceType)).map(NumberReservationResponse::getNumberDetails)
				.get().stream().map(GscTfnBean::fromNumberDetails)
				.map(gscTfnBean -> saveGscTfnBean(gscTfnBean, orderGscDetail, originCountryCode))
				.collect(Collectors.toList());
	}

	/**
	 * Get numbers for configuration
	 *
	 * @param orderGscDetail
	 * @param city
	 * @param count
	 * @param autoReserve
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	public List<GscTfnBean> getNumbersForConfiguration(OrderGscDetail orderGscDetail, String city, Integer count,
			Boolean autoReserve) throws TclCommonException, IllegalArgumentException {

		List<OrderGscTfn> inventoryNumbers = orderGscDetail.getOrderGscTfns().stream()
				.filter(orderGscTfn -> !GscConstants.STATUS_ACTIVE_BYTE.equals(orderGscTfn.getIsPorted()))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(inventoryNumbers)) {
			LOGGER.info("Order Gsc Source :: {}", orderGscDetail.getSrc());
			String[] src = orderGscDetail.getSrc().split(":");
			String originIsoCountryCode = getCountryCodeForOrderGscDetail(orderGscDetail);
			String originRepcCountryCode = getRepcCountryCodeForOrderGscDetail(orderGscDetail);
			LOGGER.info("originIsoCountryCode :: {}", originIsoCountryCode);
			LOGGER.info("originRepcCountryCode :: {}", originRepcCountryCode);
			String serviceType = orderGscDetail.getOrderGsc().getProductName();
			String cityCode = null;
			if (Objects.nonNull(city)) {
				cityCode = city;
			} else {
				cityCode = src.length > 1 ? src[1] : null;
			}
			LOGGER.info("City Code :: {}", cityCode);
			String orgId = getOrganisationIdOfCustomer(orderGscDetail);
			LOGGER.info("orgId :: {}", orgId);
			if (Objects.isNull(orgId)) {
				// if org id is not found in Tiger return empty number list
				// return ImmutableList.of();
				return orderGscDetail.getOrderGscTfns().stream().map(GscTfnBean::fromOrderGscTfn)
						.collect(Collectors.toList());
			}
			if (DOMESTIC_VOICE.equalsIgnoreCase(serviceType)) {
				serviceType = DID;
			}
			List<NumberDetails> numbers = tigerServiceHelper
					.getAvailableNumbersByServiceAndOriginCountryCity(serviceType, originRepcCountryCode, cityCode,
							count, orderGscDetail.getId(), REF_TYPE_ORDER_GSC_DETAIL, orgId);
			LOGGER.info("Numbers :: {}", numbers);
			numbers = Optional.ofNullable(numbers).orElse(ImmutableList.of());
			if (autoReserve && !CollectionUtils.isEmpty(numbers)) {
				List<GscTfnBean> tfns = numbers.stream().map(GscTfnBean::fromNumberDetails)
						.collect(Collectors.toList());
				LOGGER.info("TFNS :: {}", tfns);
				LOGGER.info("serviceType :: {}", serviceType);
				reserveAndSaveNumbers(tfns, orderGscDetail, originIsoCountryCode, serviceType);
				LOGGER.info("After TFNS :: {}", tfns);
				return orderGscTfnRepository.findByOrderGscDetail(orderGscDetail).stream()
						.map(GscTfnBean::fromOrderGscTfn).collect(Collectors.toList());
			} else {
				List<GscTfnBean> numbersFromInventory = numbers.stream().map(GscTfnBean::fromNumberDetails)
						.collect(Collectors.toList());
				LOGGER.info("numbersFromInventory :: {}", numbersFromInventory);
				List<GscTfnBean> savedTfns = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail).stream()
						.map(GscTfnBean::fromOrderGscTfn).collect(Collectors.toList());
				LOGGER.info("savedTfns :: {}", savedTfns);
				savedTfns.addAll(numbersFromInventory);
				return savedTfns;
			}
		} else {
			return orderGscDetail.getOrderGscTfns().stream().map(GscTfnBean::fromOrderGscTfn)
					.collect(Collectors.toList());
		}
	}

	/**
	 * Fetch available numbers for specified configuration
	 *
	 * @param configurationId
	 * @param count
	 * @return
	 */
	@Transactional
	public List<GscTfnBean> getAvailableNumbers(Integer configurationId, String city, Integer count,
			Boolean autoReserve) {
		LOGGER.info("Configuration ID :: {}", configurationId);
		LOGGER.info("City :: {}", city);
		LOGGER.info("Count :: {}", count);
		LOGGER.info("AutoReserve :: {}", autoReserve);
		requireNonNull(configurationId, "Configuration Id cannot be null");
		requireNonNull(count, "Count cannot be null");
		requireNonNull(autoReserve, "Auto reserve must be specified as true/false");
		return Optional.of(configurationId).flatMap(this::fetchOrderGscDetailById).map(orderGscDetail -> {
			try {
				return getNumbersForConfiguration(orderGscDetail, city, count, autoReserve);
			} catch (TclCommonException e) {
				LOGGER.info("Error in getAvailableNumbers : {}", e.getMessage());
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
			}
		}).get();
	}

	/**
	 * MEthod to update the uploaded document details in oms
	 *
	 * @param configurationId
	 * @param documentId
	 * @param requestId
	 * @param url
	 * @return {@link GscAttachmentBean}
	 */
	@Transactional
	public GscAttachmentBean updateUploadObjectConfigurationDocument(Integer configurationId, Integer documentId,
																	 String requestId, String url) throws TclCommonException {
		Objects.requireNonNull(documentId, "Document ID SHOULD NOT BE NULL");
		Objects.requireNonNull(requestId, "Request ID SHOULD NOT BE NULL");
		Objects.requireNonNull(url, "Url SHOULD NOT BE NULL");
		Objects.requireNonNull(configurationId, "CONFIGURATION ID SHOULD NOT BE NULL");
		fetchOrderGscDetailById(configurationId);
		return uploadObjectDocumentForConfiguration(fetchConfigurationAttachment(documentId), requestId, url);
	}

	private GscAttachmentBean uploadObjectDocumentForConfiguration(GscAttachments gscAttachments, String requestId,
																   String url) {
		OmsAttachment omsAttachment = gscAttachments.getOmsAttachment();
		String configId = String.valueOf(omsAttachment.getReferenceId());
		String referenceType = GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE.replaceAll("\\.", "_");
		Integer attachmentId = gscAttachmentHelper.saveObjectAttachment(requestId, url);
		omsAttachment.setErfCusAttachmentId(attachmentId);
		omsAttachmentRepository.save(omsAttachment);
		gscAttachments.setStatus(GscConstants.DOCUMENT_STATUS_UPLOADED);
		gscAttachments.setUploadedTime(Timestamp.valueOf(LocalDateTime.now()));
		gscAttachments.setUploadedBy(Utils.getSource());
		GscAttachments updated = gscAttachmentRepository.save(gscAttachments);
		return GscAttachmentBean.fromGscAttachment(updated);
	}


	/**
	 * Method to fetch gsc attachments
	 * @param documentId
	 * @return
	 * @throws TclCommonException
	 */
	private GscAttachments fetchConfigurationAttachment(Integer documentId) throws TclCommonException {
		Optional<GscAttachments> gscAttachments = gscAttachmentRepository.findById(documentId);
		if(!gscAttachments.isPresent()){
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return gscAttachments.get();
	}

	/**
	 * Method to get object storage template.
	 * @param gscAttachment
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private String getObjectStorageTemplateForConfiguration(GscAttachments gscAttachment)
			throws  TclCommonException, IllegalArgumentException {
		OmsAttachment omsAttachment = omsAttachmentRepository
				.findByReferenceNameAndAttachmentType(gscAttachment.getDocumentUId(), GscConstants.OTHERS);
		if (Objects.isNull(omsAttachment)) {
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		Optional<String> tempDownloadUrl = gscAttachmentHelper.fetchObjectStorageAttachmentResource(omsAttachment.getErfCusAttachmentId());
		if(!tempDownloadUrl.isPresent()){
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return tempDownloadUrl.get();
	}

	/**
	 * Method to get object storage attachment
	 * @param gscAttachment
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private String getObjectStorageAttachmentForConfiguration(GscAttachments gscAttachment)
			throws TclCommonException, IllegalArgumentException {
		Integer attachmentId = gscAttachment.getOmsAttachment().getErfCusAttachmentId();
		if (Objects.isNull(attachmentId)) {
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		Optional<String> tempDownloadUrl = gscAttachmentHelper.fetchObjectStorageAttachmentResource(attachmentId);
		if(!tempDownloadUrl.isPresent()){
			throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return tempDownloadUrl.get();
	}

	/**
	 * Get Object Storage Configuartion Attachments
	 *
	 * @param configurationId
	 * @param documentId
	 * @param downloadTemplate
	 * @return
	 */
	public String getObjectStorageConfigurationAttachmentForId(Integer configurationId, Integer documentId,
															   Boolean downloadTemplate) throws TclCommonException {
		Objects.requireNonNull(documentId, "Document Id Should not be null");
		Objects.requireNonNull(configurationId, "Configuration Id should not be null");
		fetchOrderGscDetailById(configurationId);
		GscAttachments gscAttachments = fetchConfigurationAttachment(documentId);
		return downloadTemplate ? getObjectStorageTemplateForConfiguration(gscAttachments) :
				getObjectStorageAttachmentForConfiguration(gscAttachments);
	}
}
