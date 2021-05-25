package com.tcl.dias.preparefulfillment.servicefulfillment.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tcl.dias.common.beans.CpeBom;
import com.tcl.dias.common.beans.CpeBomDetails;
import com.tcl.dias.common.constants.*;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAdditionalServiceParamBean;
import com.tcl.dias.common.teamsdr.beans.CpeSubchargesBean;
import com.tcl.dias.common.teamsdr.beans.MediaGatewayListPricesBean;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.TeamsDRPlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.constants.*;
import com.tcl.dias.servicefulfillment.entity.entities.CancellationRequest;
import com.tcl.dias.servicefulfillment.entity.entities.CpeDeviceNameDetail;
import com.tcl.dias.servicefulfillment.entity.repository.CancellationRequestRepository;
import com.tcl.dias.servicefulfillment.entity.repository.CpeDeviceNameDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Time;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SIComponentAttributeBean;
import com.tcl.dias.common.beans.SIComponentBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrCommercialBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrProductDetail;
import com.tcl.dias.common.fulfillment.beans.OdrServiceAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.fulfillment.beans.OdrWebexCommercialBean;
import com.tcl.dias.common.servicefulfillment.beans.ServiceFulfillmentRequest;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderResponse;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.preparefulfillment.service.ServiceCatalogueService;
import com.tcl.dias.preparefulfillment.servicefulfillment.mapper.ServiceFulfillmentMapper;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetRelation;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScGstAddress;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttributes;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommericalComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScWebexServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceFulfillmentJob;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRelationRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScGstAddressRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScWebexServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceFulfillmentJobRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceCommercialOdrMapper;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationNotEligibleNoticationBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IPCServiceFulfillmentConstant;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import static com.tcl.dias.common.constants.CommonConstants.CPE_BOM;
import static com.tcl.dias.common.constants.CommonConstants.CPE_BOM_RESPONSE;
import static com.tcl.dias.common.constants.CommonConstants.LM;
import static com.tcl.dias.common.constants.CommonConstants.OUTRIGHT_PURCHASE;
import static com.tcl.dias.common.constants.CommonConstants.PHYSICAL_IMPLEMENTATION;
import static com.tcl.dias.common.constants.CommonConstants.PHYSICAL_IMP_PARTCODE;
import static com.tcl.dias.common.constants.CommonConstants.RENTAL_PURCHASE;

/**
 * This file contains the ServiceFulfillmentService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceFulfillmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFulfillmentService.class);
	public static final String PO_ATTACHMENT_CATEGORY = "PO";
	public static final String TAX_ATTACHMENT_CATEGORY = "Tax";

	@Value("${rabbitmq.o2c.ipcfulfillment}")
	private String o2cIPCFulfillmentQueue;

	@Autowired
	ServiceFulfillmentMapper serviceFulfillmentMapper;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScAttachmentRepository scAttachmentRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ServiceFulfillmentJobRepository serviceFulfillmentJobRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ProcessL2OService processL2OService;

	@Autowired
	ServiceCatalogueService serviceCatalogueService;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.odr.oe.process.queue}")
	String odrOrderEnrichmentProcessQueue;

	@Value("${rabbitmq.odr.oe.status.queue}")
	String odrOrderEnrichStatusQueue;
	
	@Value("${rabbitmq.o2c.migration.si.details.queue}")
	String siMigrationQueue;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ProcessPlanRepository processPlanRepository;

	@Autowired
	TaskCacheService taskCacheService;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceCommercialRepository scServiceCommercialRepository;
	
	@Autowired
	ScGstAddressRepository scGstAddressRepository;
	
	@Autowired
	ServiceStatusDetailsRepository serviceStatusDetailsRepository;
	
	@Autowired
	private UserInfoUtils userInfoUtils;
	
	@Autowired
	ScAssetRepository scAssetRepository;
	
	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;
	
	@Autowired
	ScAssetRelationRepository scAssetRelationRepository;

	@Autowired
	CancellationRequestRepository cancellationRequestRepository;

	@Autowired
	TaskService taskService;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	ScWebexServiceCommercialRepository scWebexServiceCommercialRepository;
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepo;
	
	@Autowired
	CpeDeviceNameDetailRepository cpeDeviceNameDetailRepository;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;
	
	@Autowired
	NotificationService notificationService;

	@Autowired
	ScTeamsDRServiceCommercialRepository scTeamsDRServiceCommercialRepository;

	@Autowired
	CpeCostDetailsRepository cpeCostDetailsRepository;

	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;

	@Autowired
	MstVendorCpeInternationalRepository mstVendorCpeInternationalRepository;
	
	@Value("${queue.usage.eventsource}")
	String eventSourceQueue;
	
	/**
	 * 
	 * processfulfillmentDate - This method processes the odrOrderBean
	 * 
	 * @param odrOrderBean
	 * @return Set<ScServiceDetail>
	 * @throws TclCommonException
	 */
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Set<ScServiceDetail> processfulfillmentDate(OdrOrderBean odrOrderBean) throws TclCommonException {
		//Migration Order Changes
		if (("MACD".equals(odrOrderBean.getOrderType()) || "TERMINATION".equalsIgnoreCase(odrOrderBean.getOrderType()))
				&& !odrOrderBean.getOpOrderCode().startsWith("GSC")) {
			LOGGER.info("OrderCode::{}",odrOrderBean.getOpOrderCode());
			for(OdrServiceDetailBean odrServiceDetail:odrOrderBean.getOdrServiceDetails()){
				if(Objects.nonNull(odrServiceDetail.getUuid())){
					LOGGER.info("Uuid::{}",odrServiceDetail.getUuid());
					getServiceDetail(odrServiceDetail.getUuid(),odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
				}else if(Objects.nonNull(odrServiceDetail.getParentServiceUuid())){
					LOGGER.info("Parent Uuid::{}",odrServiceDetail.getParentServiceUuid());
					getServiceDetail(odrServiceDetail.getParentServiceUuid(),odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
				}
			}
		}
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
			ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
			contrEntity.setScOrder(scOrderEntity);
			scContractingInfo.add(contrEntity);
		});
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		List<String> internationalServiceIdEmptyList=new ArrayList<String>();
		odrOrderBean.getOdrServiceDetails().stream().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity = null;
			if (odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				serviceEntity = serviceFulfillmentMapper.mapIPCServiceEntityToBean(serviceDetail, odrOrderBean);
			} else {
				try {
					serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail, odrOrderBean);
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(e);
				}
			}
			if(odrOrderBean.getOpOrderCode().startsWith("IZOPC") && serviceEntity.getUuid().isEmpty()){
				LOGGER.info("IZOPC Uuid not generated for orderCode::{} with upstream Id::{}", odrOrderBean.getOpOrderCode(),serviceEntity.getErfOdrServiceId());
				internationalServiceIdEmptyList.add(String.valueOf(serviceEntity.getErfOdrServiceId()));
			}
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			if (scOrderEntity.getOrderType().equalsIgnoreCase("Termination")) {
				serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATION_INPROGRESS));
				serviceEntity.setServiceConfigStatus(TaskStatusConstants.TERMINATION_INPROGRESS);
				serviceEntity.setActivationConfigStatus(TaskStatusConstants.TERMINATION_INPROGRESS);
				serviceEntity.setBillingStatus(TaskStatusConstants.TERMINATION_INPROGRESS);
				serviceEntity.setIsDelivered(TaskStatusConstants.TERMINATION_INPROGRESS);
			} else {
				serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
				serviceEntity.setServiceConfigStatus(TaskStatusConstants.PENDING);
				serviceEntity.setActivationConfigStatus(TaskStatusConstants.PENDING);
				serviceEntity.setBillingStatus(TaskStatusConstants.PENDING);
			}
	
			if (serviceEntity.getOrderType() == null) {
				serviceEntity.setOrderType(scOrderEntity.getOrderType());
			}
			if (serviceEntity.getOrderCategory() == null) {
				serviceEntity.setOrderCategory(scOrderEntity.getOrderCategory());
			}
			serviceEntity.setServiceAceptanceStatus(TaskStatusConstants.PENDING);
			serviceEntity.setAssuranceCompletionStatus(TaskStatusConstants.PENDING);
			serviceEntity.setIsDelivered(TaskStatusConstants.PENDING);
			scOrderRepository.save(scOrderEntity);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag &&serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}
			
			ScOrderAttribute orderAttribute = scOrderAttrs.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("PO_NUMBER")).findFirst().orElse(null);

			if ((serviceDetail.getOdrContractInfo() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))|| (orderAttribute!=null && orderAttribute.getAttributeValue()!=null))
				scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY, PO_ATTACHMENT_CATEGORY));
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components=serviceDetail.getOdrComponentBeans();
			LOGGER.info("OdrComponentBeans : {}", components);
			if (components != null) {
				for (OdrComponentBean scComponentBean : components) {
					LOGGER.info("Into Component Bean");
					ScComponent scComponent=null;
					if (serviceEntity.getId() != null && serviceEntity.getOrderType() != null && serviceEntity.getOrderType().equalsIgnoreCase("Termination")) {
						scComponent = scComponentRepository
								.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(
										serviceEntity.getId(), "LM", scComponentBean.getSiteType());

					}
					if (scComponent == null) {
						scComponent = new ScComponent();

					}
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					LOGGER.info("odrComponentAttributeBeans for componentBean {} : {}", scComponentBean.getId(), scComponentBean.getOdrComponentAttributeBeans());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						LOGGER.info("Into Component Attributes");
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						if(serviceEntity.getId() != null && serviceEntity.getOrderType() != null  && serviceEntity.getOrderType().equalsIgnoreCase("Termination")) {
							scComponentAttribute.setScServiceDetailId(serviceEntity.getId());
						}
						if (scComponentAttribute.getAttributeName().equals("siteGstAddress")
								&& scComponentAttribute.getAttributeValue() != null) {
							LOGGER.info("Constructing site Gst address for IAS & GVPN{} ",scComponentAttribute.getAttributeValue());
							try {
								GstAddressBean gstAddress = Utils.convertJsonToObject(
										scComponentAttribute.getAttributeValue(), GstAddressBean.class);
								if (gstAddress != null) {
									ScGstAddress scGstAddress = new ScGstAddress();
									scGstAddress.setBuildingName(gstAddress.getBuildingName());
									scGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
									scGstAddress.setDistrict(gstAddress.getDistrict());
									scGstAddress.setFlatNumber(gstAddress.getFlatNumber());
									scGstAddress.setLatitude(gstAddress.getLatitude());
									scGstAddress.setLocality(gstAddress.getLocality());
									scGstAddress.setLongitude(gstAddress.getLongitude());
									scGstAddress.setPincode(gstAddress.getPinCode());
									scGstAddress.setState(gstAddress.getState());
									scGstAddress.setStreet(gstAddress.getStreet());
									scGstAddress = scGstAddressRepository.save(scGstAddress);
									
									scComponentAttribute.setAttributeName("siteGstAddressId");
									scComponentAttribute.setAttributeAltValueLabel(scGstAddress.getId().toString());
									scComponentAttribute.setAttributeValue(scGstAddress.getId().toString());
									LOGGER.info("Site Gst Address id is {}", scComponentAttribute.getAttributeValue());
									
								}
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					if(serviceEntity.getOrderType() != null && serviceEntity.getOrderType().equalsIgnoreCase("Termination")) {
						Integer serviceId = serviceEntity.getId();
						if(serviceDetail.getOdrServiceAttributes() != null && !serviceDetail.getOdrServiceAttributes().isEmpty()) {
							for (OdrServiceAttributeBean serviceAttr : serviceDetail.getOdrServiceAttributes()) {
								if (serviceAttr.getAttributeName().equals("approvalMailAvailable")) {
									scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "approvalMailAvailable",
											serviceAttr.getAttributeValue(), serviceId));
								}
								if (serviceAttr.getAttributeName().equals("csmEmail")) {
									scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "csmEmail",
											serviceAttr.getAttributeValue(), serviceId));
								}
								if (serviceAttr.getAttributeName().equals("csmUserName")) {
									scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "csmUserName",
											serviceAttr.getAttributeValue(), serviceId));
								}
							}
						}
						scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "terminationEffectiveDate",
								serviceDetail.getTerminationEffectiveDate(), serviceId));
						serviceEntity.setTerminationEffectiveDate(serviceDetail.getTerminationEffectiveDate());
						serviceEntity.setTerminationFlowTriggered(CommonConstants.YES);
						serviceEntity.setCustomerRequestorDate(serviceDetail.getCustomerRequestorDate());

						scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "customerRequestorDate",
								serviceDetail.getCustomerRequestorDate(), serviceId));
						scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "etcValue",
								serviceDetail.getEtcValue(), serviceId));
						scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "etcWaiver",
								serviceDetail.getEtcWaiver(), serviceId));
						scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "terminationFlowTriggered",
								"Yes", serviceId));
						scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "backdatedTermination",
								serviceDetail.getBackdatedTermination(), serviceId));
						scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "terminationReason",
								serviceDetail.getTerminationReason(), serviceId));
					}
					Integer serviceId = serviceEntity.getId();
					if(serviceDetail.getOdrServiceAttributes() != null && !serviceDetail.getOdrServiceAttributes().isEmpty()) {
						for (OdrServiceAttributeBean serviceAttr : serviceDetail.getOdrServiceAttributes()) {
							if (serviceAttr.getAttributeName().equals("Service type")
									&& (serviceAttr.getAttributeValue() != null
											&& serviceAttr.getAttributeValue().toLowerCase().contains("usage"))) {
								scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "eventSource",
										getEventSourceforBurstable(serviceEntity), serviceId));
							}
						}
					}
					LOGGER.info("ErfOdrServiceId for scServiceDetail : {}", serviceEntity.getErfOdrServiceId());
					LOGGER.info("Adding Component");
					if(!compMapper.containsKey(serviceEntity.getErfOdrServiceId())) {
						compMapper.put(serviceEntity.getErfOdrServiceId(), new ArrayList<>());
					}
					compMapper.get(serviceEntity.getErfOdrServiceId()).add(scComponent);
				}
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			scServiceDetails.add(serviceEntity);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.save(serviceEntity);
			if(serviceEntity.getScAssets() != null) {
				HashMap<Integer, ScAsset> scAssetList=new HashMap<>();
				List<ScAssetAttribute> scAssetAttributeList=new ArrayList<>();
				List<ScAssetRelation> scAssetRelationList=new ArrayList<>();
				for (ScAsset asset : serviceEntity.getScAssets()) {
					asset.setScServiceDetail(scServiceDetail);
					scAssetList.put(asset.getOdrAssetId(), asset);
					if(asset.getScAssetAttributes() != null)
						scAssetAttributeList.addAll(asset.getScAssetAttributes());
					if(asset.getScAssetRelations() != null)
						scAssetRelationList.addAll(asset.getScAssetRelations());
				}
				LOGGER.info("ScAssets data: "+scAssetList.toString());
				scAssetRepository.saveAll(serviceEntity.getScAssets());
				scAssetAttributeRepository.saveAll(scAssetAttributeList);
				if(!scAssetRelationList.isEmpty()) {
					scAssetRelationList.forEach(scAssetRelation -> {
						scAssetRelation.setScAssetId(scAssetList.get(scAssetRelation.getScAssetId()).getId());
						scAssetRelation.setScRelatedAssetId(scAssetList.get(scAssetRelation.getScRelatedAssetId()).getId());
					});
					scAssetRelationRepository.saveAll(scAssetRelationList);
				}
				LOGGER.info("ScAssets are stored");
			}
		});
		scContractInfoRepository.saveAll(scContractingInfo);
		scOrderAttributeRepository.saveAll(scOrderAttrs);
		//scServiceDetailRepository.saveAll(scServiceDetails);

		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			//String scServiceId = serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial=serviceFulfillmentMapper.mapOdrCommercialToServiceCommercial(odrCommercial);
			if(scCommercialMapper.get(odrCommercial.getServiceId())==null) {
				List<ScServiceCommercial> scServiceCommercials=new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
			}else {
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scCommercial);
			}
			
			
		}
		
		Map<Integer, String> serviceIdMapper = new HashMap<>();
		Map<Integer, ScServiceDetail> serviceParentIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			createServiceStaus(scServiceDetail, TaskStatusConstants.INPROGRESS);
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
			serviceParentIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail);
		}
		Map<Integer,List<String>> masterSlaveMap = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				LOGGER.info("ScServiceDetail erfOdrServiceId : {}",scServiceDetail.getErfOdrServiceId());
				LOGGER.info("Getting ComMapper");
				List<ScComponent> scCompos=compMapper.get(scServiceDetail.getErfOdrServiceId());
				LOGGER.info("ScServiceDetail Id : {}", scServiceDetail.getId());
				scCompos.forEach(scCompo -> {
					scCompo.setScServiceDetailId(scServiceDetail.getId());
					scCompo.setUuid(scServiceDetail.getUuid());
					for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
						LOGGER.info("ScComponentAttribute : {}, Key : {}, Value : {}", scCmpAttr.getId(), scCmpAttr.getAttributeName(), scCmpAttr.getAttributeValue());
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					LOGGER.info("Saving Component");
					scComponentRepository.save(scCompo);
				});
			}
			
			if(scCommercialMapper.get(scServiceDetail.getErfOdrServiceId())!=null) {
				List<ScServiceCommercial> scCommercials=scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);
					
				}
			}
			
			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				scServiceDetailRepository.save(scServiceDetail);
			}
			if (scServiceDetail.getMasterVrfServiceId() != null) {
				LOGGER.info("Odr Master vrf service ID --> {}",scServiceDetail.getMasterVrfServiceId());
				ScServiceDetail scServiceMaster = scServiceDetails.stream()
						.filter(service -> service.getErfOdrServiceId().equals(scServiceDetail.getMasterVrfServiceId()))
						.findFirst().orElse(null);
				if (scServiceMaster != null) {
					LOGGER.info("SC Master vrf service ID --> {}",scServiceMaster.getId());
					scServiceDetail.setMasterVrfServiceId(scServiceMaster.getId());
					scServiceDetail.setMasterVrfServiceCode(scServiceMaster.getUuid());
					scServiceDetailRepository.save(scServiceDetail);
					List<String> slaveList = new ArrayList<>();
					if(masterSlaveMap.containsKey(scServiceMaster.getId())) {
						slaveList =  masterSlaveMap.get(scServiceMaster.getId());
						slaveList.add(scServiceDetail.getUuid());
					}else {
						slaveList.add(scServiceDetail.getUuid());
					}
					masterSlaveMap.put(scServiceMaster.getId(), slaveList);
				}
			}
			
			
			if(scServiceDetail.getParentId() != null) {
				if(serviceParentIdMapper.get(scServiceDetail.getParentId()) != null) {
					scServiceDetail.setParentId(serviceParentIdMapper.get(scServiceDetail.getParentId()).getId());
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
	}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (scServiceDetail.getIsMultiVrf() != null && scServiceDetail.getIsMultiVrf().equals(CommonConstants.Y)
					&& masterSlaveMap.containsKey(scServiceDetail.getId())) {
				addOrModifyScServiceAttr("SLAVE_VRF_SERVICE_ID",
						StringUtils.join(masterSlaveMap.get(scServiceDetail.getId()), ","), scServiceDetail,
						scServiceDetail.getCreatedBy());
			}
		}

		//MACD Changes
		if("MACD".equals(odrOrderBean.getOrderType()) && ("ADD_IP".equalsIgnoreCase(odrOrderBean.getOrderCategory()) 
				|| "CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory()))){
			LOGGER.info("MACD Changes");
			updateComponentAttrs(odrOrderBean,"BOTH");
		}
		//GVPN MACD Program Name Changes
		odrOrderBean.getOdrServiceDetails().stream().forEach(odrServiceDetailBean ->{
		if(Objects.nonNull(odrServiceDetailBean.getErfPrdCatalogProductName()) 
				&& "GVPN".equalsIgnoreCase(odrServiceDetailBean.getErfPrdCatalogProductName())
			&& "MACD".equals(odrOrderBean.getOrderType())){
			LOGGER.info("GVPN MACD Changes");
			if("CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory())){
				updateComponentAttrs(odrOrderBean,"GVPN");
			}else if("ADD_SITE".equalsIgnoreCase(odrOrderBean.getOrderCategory())){
				LOGGER.info("ADD SITE VPN NAME");
				updateProgramName(odrServiceDetailBean);
			}
			
			String orderSubCategory = StringUtils.trimToEmpty(odrServiceDetailBean.getOrderSubCategory());
			orderSubCategory= OrderCategoryMapping.getOrderSubCategory(orderSubCategory);
          //GVPN MACD COS criteria missing attribute saving
			LOGGER.info("orderSubCategory=>{}, orderID:{}",orderSubCategory,odrOrderBean.getUuid());
			if(!"ADD_SITE".equals(odrOrderBean.getOrderCategory())
                    && (!orderSubCategory.toLowerCase().contains("parallel"))) {
				LOGGER.info("MACD Cos Details");
				updateCosCriteriaMissingAttribute(odrServiceDetailBean,odrServiceDetailBean.getUuid(),odrServiceDetailBean.getUuid());
				//updateCosModel(odrServiceDetailBean.getUuid(),odrServiceDetailBean.getUuid());
			}else if(odrServiceDetailBean.getParentServiceUuid()!=null && !odrServiceDetailBean.getParentServiceUuid().isEmpty()){
				LOGGER.info("MACD ADD SITE or Parallel Cos Details::{}",odrServiceDetailBean.getParentServiceUuid());
				updateCosCriteriaMissingAttribute(odrServiceDetailBean,serviceIdMapper.get(odrServiceDetailBean.getId()),odrServiceDetailBean.getParentServiceUuid());
				//updateCosModel(serviceIdMapper.get(odrServiceDetailBean.getId()),odrServiceDetailBean.getParentServiceUuid());
			}
		}
		if("MACD".equals(odrOrderBean.getOrderType()) && !odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) { 
			String uuid=null;
			
			if(odrServiceDetailBean.getParentServiceUuid()!=null) {
				uuid=odrServiceDetailBean.getParentServiceUuid();
			}
			else {
				uuid=odrServiceDetailBean.getUuid();

			}
			
			ScServiceDetail prevActiveServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(odrServiceDetailBean.getParentServiceUuid(), "ACTIVE");
			ScServiceDetail currentServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(serviceIdMapper.get(odrServiceDetailBean.getId()), "INPROGRESS");
			
				if (prevActiveServiceDetail != null && currentServiceDetail != null) {
					if (currentServiceDetail.getSiteAlias() == null) {
						currentServiceDetail.setSiteAlias(prevActiveServiceDetail.getSiteAlias());
						LOGGER.info("TPSSecsId has changed, prevSeriveId: {} currentSeriveId: {}",
								prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());
					}
					setLocatItContactDetails(prevActiveServiceDetail, currentServiceDetail);
					setDemarcationDetails(prevActiveServiceDetail, currentServiceDetail);
					scServiceDetailRepository.save(currentServiceDetail);
					ScOrder prevOrder = prevActiveServiceDetail.getScOrder();
					ScOrder currentOrder = currentServiceDetail.getScOrder();
					if (currentOrder.getTpsSecsId() == null) {
						currentOrder.setTpsSecsId(prevOrder.getTpsSecsId());
						LOGGER.info("TPSSecsId has changed, prevSeriveId: {} currentSeriveId: {}",
								prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());
					}
					if (currentOrder.getTpsSfdcCuid() == null) {
						currentOrder.setTpsSfdcCuid(prevOrder.getTpsSfdcCuid());
						LOGGER.info("TPSSfdcCuid has changed, prevSeriveId: {} currentSeriveId: {}",
								prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());
					}
					scOrderRepository.save(currentOrder);
				}
		}
		
		
		
		});
		
		//Update wanIpProvidedByCust
		if(Objects.nonNull(odrOrderBean.getOpOrderCode())){
			ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(odrOrderBean.getOpOrderCode(), "Y");
			if(Objects.nonNull(scOrder)){
				LOGGER.info("ScOrder exists");
				List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findByScOrderId(scOrder.getId());
				if(Objects.nonNull(scServiceDetailList) && !scServiceDetailList.isEmpty()){
					LOGGER.info("ScServiceDetail exists");
					for(ScServiceDetail scServiceDetail:scServiceDetailList){
						updateWanIpProvidedByCust(scServiceDetail);
					}
				}
			}
		}
		
		if (odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)
				&& !orderDetail.keySet().isEmpty()) {
			mqUtils.send(o2cIPCFulfillmentQueue, Utils.convertObjectToJson(orderDetail));
		}
		
		if(odrOrderBean.getOpOrderCode().startsWith("IZOPC") && !internationalServiceIdEmptyList.isEmpty()){
			LOGGER.info("ISD Code not exists for izopc::{} for upstream Ids::{}",odrOrderBean.getOpOrderCode(),internationalServiceIdEmptyList.size());
			List<String> tempToMailList= new ArrayList<>(Arrays.asList("OPTIMUS-O2C-SUPPORT@tatacommunications.onmicrosoft.com"));
			//List<String> tempToMailList= new ArrayList<>(Arrays.asList("dimple.subburaj@tatacommunications.com"));
			notificationService.notifyInternationalServiceIdTrigger(null,tempToMailList,odrOrderBean.getOpOrderCode(),String.join(",", internationalServiceIdEmptyList));
		}

		return scServiceDetails;

	}
	
	/**
	 * 
	 * processfulfillmentDate - This method processes the odrOrderBean
	 * 
	 * @param odrOrderBean
	 * @return Set<ScServiceDetail>
	 * @throws TclCommonException
	 */
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Set<ScServiceDetail> processfulfillmentDateForIpc(OdrOrderBean odrOrderBean) throws TclCommonException {
		/*if ("MACD".equals(odrOrderBean.getOrderType())) {
			LOGGER.info("OrderCode::{}", odrOrderBean.getOpOrderCode());
			for (OdrServiceDetailBean odrServiceDetail : odrOrderBean.getOdrServiceDetails()) {
				if (Objects.nonNull(odrServiceDetail.getUuid())) {
					LOGGER.info("Uuid::{}", odrServiceDetail.getUuid());
					getServiceDetail(odrServiceDetail.getUuid(), odrOrderBean.getOpOrderCode(),
							odrOrderBean.getOrderType());
				} else if (Objects.nonNull(odrServiceDetail.getParentServiceUuid())) {
					LOGGER.info("Parent Uuid::{}", odrServiceDetail.getParentServiceUuid());
					getServiceDetail(odrServiceDetail.getParentServiceUuid(), odrOrderBean.getOpOrderCode(),
							odrOrderBean.getOrderType());
				}
			}
		}*/
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
			ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
			contrEntity.setScOrder(scOrderEntity);
			scContractingInfo.add(contrEntity);
		});
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, ScComponent> compMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		odrOrderBean.getOdrServiceDetails().stream().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity = null;
			if (odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				serviceEntity = serviceFulfillmentMapper.mapIPCServiceEntityToBean(serviceDetail, odrOrderBean);
			} else {
				try {
					serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail, odrOrderBean);
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(e);
				}
			}
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			serviceEntity.setServiceConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setActivationConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setBillingStatus(TaskStatusConstants.PENDING);
			if (scOrderEntity.getOrderType() != null) {
				serviceEntity.setOrderType(scOrderEntity.getOrderType());
			}
			if (scOrderEntity.getOrderCategory() != null) {
				serviceEntity.setOrderCategory(scOrderEntity.getOrderCategory());
			}
			serviceEntity.setServiceAceptanceStatus(TaskStatusConstants.PENDING);
			serviceEntity.setAssuranceCompletionStatus(TaskStatusConstants.PENDING);
			serviceEntity.setIsDelivered(TaskStatusConstants.PENDING);
			scOrderRepository.save(scOrderEntity);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag && serviceDetail.getTaxExemptionFlag() != null
					&& serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag() != null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}

			ScOrderAttribute orderAttribute = scOrderAttrs.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("PO_NUMBER")).findFirst().orElse(null);

			if ((serviceDetail.getOdrContractInfo() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))
					|| (orderAttribute != null && orderAttribute.getAttributeValue() != null))
				scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY, PO_ATTACHMENT_CATEGORY));
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components = serviceDetail.getOdrComponentBeans();
			LOGGER.info("OdrComponentBeans : {}", components);
			if (components != null) {
				for (OdrComponentBean scComponentBean : components) {
					LOGGER.info("Into Component Bean");
					ScComponent scComponent = new ScComponent();
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					LOGGER.info("odrComponentAttributeBeans for componentBean {} : {}", scComponentBean.getId(),
							scComponentBean.getOdrComponentAttributeBeans());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						LOGGER.info("Into Component Attributes");
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					LOGGER.info("ErfOdrServiceId for scServiceDetail : {}", serviceEntity.getErfOdrServiceId());
					LOGGER.info("Adding Component");
					compMapper.put(serviceEntity.getErfOdrServiceId(), scComponent);
				}
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			scServiceDetails.add(serviceEntity);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.save(serviceEntity);
			if (serviceEntity.getScAssets() != null) {
				HashMap<Integer, ScAsset> scAssetList = new HashMap<>();
				List<ScAssetAttribute> scAssetAttributeList = new ArrayList<>();
				List<ScAssetRelation> scAssetRelationList = new ArrayList<>();
				for (ScAsset asset : serviceEntity.getScAssets()) {
					asset.setScServiceDetail(scServiceDetail);
					scAssetList.put(asset.getOdrAssetId(), asset);
					if (asset.getScAssetAttributes() != null)
						scAssetAttributeList.addAll(asset.getScAssetAttributes());
					if (asset.getScAssetRelations() != null)
						scAssetRelationList.addAll(asset.getScAssetRelations());
				}
				LOGGER.info("ScAssets data: " + scAssetList.toString());
				scAssetRepository.saveAll(serviceEntity.getScAssets());
				scAssetAttributeRepository.saveAll(scAssetAttributeList);
				if (!scAssetRelationList.isEmpty()) {
					scAssetRelationList.forEach(scAssetRelation -> {
						scAssetRelation.setScAssetId(scAssetList.get(scAssetRelation.getScAssetId()).getId());
						scAssetRelation
								.setScRelatedAssetId(scAssetList.get(scAssetRelation.getScRelatedAssetId()).getId());
					});
					scAssetRelationRepository.saveAll(scAssetRelationList);
				}
				LOGGER.info("ScAssets are stored");
			}
		});
		scContractInfoRepository.saveAll(scContractingInfo);
		scOrderAttributeRepository.saveAll(scOrderAttrs);
		/* scServiceDetailRepository.saveAll(scServiceDetails); */
		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			// String scServiceId = serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial = serviceFulfillmentMapper
					.mapOdrCommercialToServiceCommercial(odrCommercial);
			if (scCommercialMapper.get(odrCommercial.getServiceId()) == null) {
				List<ScServiceCommercial> scServiceCommercials = new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
			} else {
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scCommercial);
			}

		}

		Map<Integer, String> serviceIdMapper = new HashMap<>();
		Map<Integer, ScServiceDetail> serviceParentIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			createServiceStaus(scServiceDetail, TaskStatusConstants.INPROGRESS);
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
			serviceParentIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail);
		}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				LOGGER.info("ScServiceDetail erfOdrServiceId : {}", scServiceDetail.getErfOdrServiceId());
				LOGGER.info("Getting ComMapper");
				ScComponent scCompo = compMapper.get(scServiceDetail.getErfOdrServiceId());
				LOGGER.info("ScServiceDetail Id : {}", scServiceDetail.getId());
				scCompo.setScServiceDetailId(scServiceDetail.getId());
				scCompo.setUuid(scServiceDetail.getUuid());
				for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
					LOGGER.info("ScComponentAttribute : {}, Key : {}, Value : {}", scCmpAttr.getId(),
							scCmpAttr.getAttributeName(), scCmpAttr.getAttributeValue());
					scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
				}
				LOGGER.info("Saving Component");
				scComponentRepository.save(scCompo);
			}

			if (scCommercialMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScServiceCommercial> scCommercials = scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);

				}
			}

			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				scServiceDetailRepository.save(scServiceDetail);
			}

			if (scServiceDetail.getParentId() != null) {
				if (serviceParentIdMapper.get(scServiceDetail.getParentId()) != null) {
					scServiceDetail.setParentId(serviceParentIdMapper.get(scServiceDetail.getParentId()).getId());
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
		}

		// MACD Changes
		if ("MACD".equals(odrOrderBean.getOrderType()) && ("ADD_IP".equalsIgnoreCase(odrOrderBean.getOrderCategory())
				|| "CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory()))) {
			LOGGER.info("MACD Changes");
			updateComponentAttrs(odrOrderBean, "BOTH");
		}
		// GVPN MACD Program Name Changes
		odrOrderBean.getOdrServiceDetails().stream().forEach(odrServiceDetailBean -> {
			if (Objects.nonNull(odrServiceDetailBean.getErfPrdCatalogProductName())
					&& "GVPN".equalsIgnoreCase(odrServiceDetailBean.getErfPrdCatalogProductName())
					&& "MACD".equals(odrOrderBean.getOrderType())) {
				LOGGER.info("GVPN MACD Changes");
				if ("CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory())) {
					updateComponentAttrs(odrOrderBean, "GVPN");
				} else if ("ADD_SITE".equalsIgnoreCase(odrOrderBean.getOrderCategory())) {
					LOGGER.info("ADD SITE VPN NAME");
					updateProgramName(odrServiceDetailBean);
				}

				String orderSubCategory = StringUtils.trimToEmpty(odrServiceDetailBean.getOrderSubCategory());
				orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderSubCategory);
				// GVPN MACD COS criteria missing attribute saving
				LOGGER.info("orderSubCategory=>{}, orderID:{}", orderSubCategory, odrOrderBean.getUuid());
				if (!"ADD_SITE".equals(odrOrderBean.getOrderCategory())
						&& (!orderSubCategory.toLowerCase().contains("parallel"))) {
					LOGGER.info("MACD Cos Details");
					updateCosCriteriaMissingAttribute(odrServiceDetailBean, odrServiceDetailBean.getUuid(),
							odrServiceDetailBean.getUuid());
					// updateCosModel(odrServiceDetailBean.getUuid(),odrServiceDetailBean.getUuid());
				} else if (odrServiceDetailBean.getParentServiceUuid() != null
						&& !odrServiceDetailBean.getParentServiceUuid().isEmpty()) {
					LOGGER.info("MACD ADD SITE or Parallel Cos Details::{}",
							odrServiceDetailBean.getParentServiceUuid());
					updateCosCriteriaMissingAttribute(odrServiceDetailBean,
							serviceIdMapper.get(odrServiceDetailBean.getId()),
							odrServiceDetailBean.getParentServiceUuid());
					// updateCosModel(serviceIdMapper.get(odrServiceDetailBean.getId()),odrServiceDetailBean.getParentServiceUuid());
				}
			}
		});

		// Update wanIpProvidedByCust
		if (Objects.nonNull(odrOrderBean.getOpOrderCode())) {
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(odrOrderBean.getOpOrderCode(), "Y");
			if (Objects.nonNull(scOrder)) {
				LOGGER.info("ScOrder exists");
				List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findByScOrderId(scOrder.getId());
				if (Objects.nonNull(scServiceDetailList) && !scServiceDetailList.isEmpty()) {
					LOGGER.info("ScServiceDetail exists");
					for (ScServiceDetail scServiceDetail : scServiceDetailList) {
						updateWanIpProvidedByCust(scServiceDetail);
					}
				}
			}
		}

		if (odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)
				&& !orderDetail.keySet().isEmpty()) {
			mqUtils.send(o2cIPCFulfillmentQueue, Utils.convertObjectToJson(orderDetail));
		}

		return scServiceDetails;
	}

	private void setDemarcationDetails(ScServiceDetail prevActiveServiceDetail, ScServiceDetail currentServiceDetail) {
		if (currentServiceDetail.getDemarcationBuildingName() == null) {
			if (prevActiveServiceDetail.getDemarcationBuildingName() != null) {
				currentServiceDetail.setDemarcationBuildingName(prevActiveServiceDetail.getDemarcationBuildingName());
			} else {
				ScComponentAttribute attribute = getScCompentAttribute(prevActiveServiceDetail,
						"demarcationBuildingName");
				if (attribute != null) {
					currentServiceDetail.setDemarcationBuildingName(attribute.getAttributeValue());
				}
			}
			LOGGER.info("demarcationBuildingName has updated, prevSeriveId: {} currentSeriveId: {}", prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());
		}
		if (currentServiceDetail.getDemarcationFloor() == null) {
			if (prevActiveServiceDetail.getDemarcationFloor() != null) {
				currentServiceDetail.setDemarcationFloor(prevActiveServiceDetail.getDemarcationFloor());
			} else {
				ScComponentAttribute attribute = getScCompentAttribute(prevActiveServiceDetail, "demarcationFloor");
				if (attribute != null) {
					currentServiceDetail.setDemarcationFloor(attribute.getAttributeValue());
				}
			}
			LOGGER.info("demarcationFloor has updated, prevSeriveId: {} currentSeriveId: {}", prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());

		}
		if (currentServiceDetail.getDemarcationRack() == null) {
			if (prevActiveServiceDetail.getDemarcationRack() != null) {
				currentServiceDetail.setDemarcationRack(prevActiveServiceDetail.getDemarcationRack());
			} else {
				ScComponentAttribute attribute = getScCompentAttribute(prevActiveServiceDetail, "demarcationRack");
				if (attribute != null) {
					currentServiceDetail.setDemarcationRack(attribute.getAttributeValue());
				}
			}
			LOGGER.info("demarcationRack has updated, prevSeriveId: {} currentSeriveId: {}", prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());

		}
		if (currentServiceDetail.getDemarcationRoom() == null) {
			if (prevActiveServiceDetail.getDemarcationRoom() != null) {
				currentServiceDetail.setDemarcationRoom(prevActiveServiceDetail.getDemarcationRoom());
			} else {
				ScComponentAttribute attribute = getScCompentAttribute(prevActiveServiceDetail, "demarcationRoom");
				if (attribute != null) {
					currentServiceDetail.setDemarcationRoom(attribute.getAttributeValue());
				}
			}
			LOGGER.info("demarcationRoom has updated, prevSeriveId: {} currentSeriveId: {}", prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());
		}
		
	}

	private void setLocatItContactDetails(ScServiceDetail prevActiveServiceDetail,
			ScServiceDetail currentServiceDetail) {
		if (currentServiceDetail.getLocalItContactEmail() == null) {
			if (prevActiveServiceDetail.getLocalItContactEmail() != null) {
				currentServiceDetail.setLocalItContactEmail(prevActiveServiceDetail.getLocalItContactEmail());
			} else {
				ScComponentAttribute attribute = getScCompentAttribute(prevActiveServiceDetail, "localItContactEmail");
				if (attribute != null) {
					currentServiceDetail.setLocalItContactEmail(attribute.getAttributeValue());
				}
			}
			LOGGER.info("localItContactEmail has updated, prevSeriveId: {} currentSeriveId: {}",
					prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());

		}

		if (currentServiceDetail.getLocalItContactMobile() == null) {
			if (prevActiveServiceDetail.getLocalItContactMobile() != null) {
				currentServiceDetail.setLocalItContactMobile(prevActiveServiceDetail.getLocalItContactMobile());
			} else {
				ScComponentAttribute attribute = getScCompentAttribute(prevActiveServiceDetail, "localItContactMobile");
				if (attribute != null) {
					currentServiceDetail.setLocalItContactMobile(attribute.getAttributeValue());
				}
			}
			LOGGER.info("localItContactMobile has updated, prevSeriveId: {} currentSeriveId: {}",
					prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());

		}
		if (currentServiceDetail.getLocalItContactName() == null) {
			if (prevActiveServiceDetail.getLocalItContactName() != null) {
				currentServiceDetail.setLocalItContactName(prevActiveServiceDetail.getLocalItContactName());
			} else {
				ScComponentAttribute attribute = getScCompentAttribute(prevActiveServiceDetail, "localItContactName");
				if (attribute != null) {
					currentServiceDetail.setLocalItContactName(attribute.getAttributeValue());
				}
			}
			LOGGER.info("localItContactName has updated, prevSeriveId: {} currentSeriveId: {}",
					prevActiveServiceDetail.getUuid(), currentServiceDetail.getUuid());

		}
	}

	private ScComponentAttribute getScCompentAttribute(ScServiceDetail prevActiveServiceDetail, String attributeName) {
		return scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							prevActiveServiceDetail.getId(), attributeName, AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
	}
	
	private void updateCosModel(String uuid, String prevUuid) {
		LOGGER.info("updateCosModel=>{}",uuid);
		ScServiceDetail prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(prevUuid, "ACTIVE");
		ScServiceDetail currentServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(uuid, "INPROGRESS");
		if(prevActiveServiceDetail!=null && currentServiceDetail!=null){
			LOGGER.info("Both Prev Current Detail: {} and Current detail:{} exists,",prevActiveServiceDetail.getId(),currentServiceDetail.getId());
			ScServiceAttribute prevCosServiceAttribute = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(prevActiveServiceDetail.getId(), "COS_MODEL");
			if(prevCosServiceAttribute!=null && prevCosServiceAttribute.getAttributeValue()!=null 
					&& !prevCosServiceAttribute.getAttributeValue().isEmpty()){
				LOGGER.info("Prev Cos Model exists");
				Map<String, String> scAttribute = new HashMap<>();
				scAttribute.put("COS_MODEL", prevCosServiceAttribute.getAttributeValue());
				componentAndAttributeService.updateServiceAttributes(currentServiceDetail.getId(), scAttribute);
			}
		}
	}

	private void updateWanIpProvidedByCust(ScServiceDetail scServiceDetail) {
		LOGGER.info("updateWanIpProvidedByCust::",scServiceDetail.getId());
		ScComponent scComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(scServiceDetail.getId(), "LM", "A");
		ScComponentAttribute wanIpAddressComp=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "wanIpAddress", "LM", "A");
		if(Objects.nonNull(wanIpAddressComp) && Objects.nonNull(wanIpAddressComp.getAttributeValue())){
			LOGGER.info("wanIpAddress exists for ::",scServiceDetail.getId());
			ScComponentAttribute wanIpCustProvidedComp=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "wanIpProvidedByCust", "LM", "A");
			if(Objects.nonNull(wanIpCustProvidedComp)){
				LOGGER.info("wanIpCusProvided exists for ::",scServiceDetail.getId());
				wanIpCustProvidedComp.setAttributeValue("Yes");
				wanIpCustProvidedComp.setUpdatedBy("OPTIMUS");
				wanIpCustProvidedComp.setUpdatedDate(new Timestamp(new Date().getTime()));
				scComponentAttributesRepository.save(wanIpCustProvidedComp);
			}else{
				LOGGER.info("wanIpCusProvided not exists for ::",scServiceDetail.getId());
				ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
				scComponentAttribute.setAttributeAltValueLabel("wanIpProvidedByCust");
				scComponentAttribute.setAttributeName("wanIpProvidedByCust");
				scComponentAttribute.setAttributeValue("Yes");
				scComponentAttribute.setCreatedBy("OPTIMUS");
				scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
				scComponentAttribute.setIsActive(CommonConstants.Y);
				scComponentAttribute.setScComponent(scComponent);
				scComponentAttribute.setUuid(Utils.generateUid());
				scComponentAttribute.setScServiceDetailId(scServiceDetail.getId());
				scComponent.getScComponentAttributes().add(scComponentAttribute);
				scComponentRepository.save(scComponent);
			}
		}
	}

	private void updateProgramName(OdrServiceDetailBean odrServiceDetailBean) {

		try {
			String vpnName = "";
			List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findByTpsServiceIdOrUuid(
					odrServiceDetailBean.getParentServiceUuid(), odrServiceDetailBean.getParentServiceUuid());
			if (Objects.isNull(scServiceDetailList) || scServiceDetailList.isEmpty()) {
				LOGGER.info("ADD SITE Migration order doesn't exists in SF: {}",
						odrServiceDetailBean.getParentServiceUuid());
				String response = (String) mqUtils.sendAndReceive(siMigrationQueue,
						odrServiceDetailBean.getParentServiceUuid());
				LOGGER.info("Received response from getSIOrder for migration:: {}", response);
				SIGetOrderResponse orderResponse = Utils.fromJson(response, SIGetOrderResponse.class);
				if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus())) {
					SIOrderDataBean siOrderData = orderResponse.getOrder();
					Optional<SIServiceDetailDataBean> siServiceOptional = siOrderData.getServiceDetails().stream()
							.findFirst();
					if (siServiceOptional.isPresent()) {
						LOGGER.info("ADD SITE VPN NAME FROM SI");
						vpnName = siServiceOptional.get().getVpnName();
					}
				} else {
					LOGGER.error("Error in retrieving getSIOrder for migration:: {}", orderResponse.getMessage());
					throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
			} else {
				Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailList.stream().findFirst();
				if (scServiceDetailOptional.isPresent()) {
					LOGGER.info("ADD SITE VPN NAME FROM SF");
					vpnName = scServiceDetailOptional.get().getVpnName();
				}
			}

			if (Objects.nonNull(vpnName) && !vpnName.isEmpty()) {
				LOGGER.info("ADD SITE VPN Name exists");
				ScServiceDetail currentServiceDetail = scServiceDetailRepository
						.findByUuidAndMstStatus_code(odrServiceDetailBean.getUuid(), "INPROGRESS");
				if (Objects.nonNull(currentServiceDetail)) {
					Boolean isVpnNameExists[] = { false };
					ScComponent currentScComponent = scComponentRepository
							.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(
									currentServiceDetail.getId(), "LM", "A");
					currentScComponent.getScComponentAttributes().stream().forEach(scCompAttr -> {
						if ("programName".equals(scCompAttr.getAttributeName())) {
							isVpnNameExists[0] = true;
						}
					});
					if (!isVpnNameExists[0]) {
						LOGGER.info("ADD SITE VPN Name creating newly");
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(vpnName);
						scComponentAttribute.setAttributeName("programName");
						scComponentAttribute.setAttributeValue("programName");
						scComponentAttribute.setCreatedBy("OPTIMUS SF");
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(currentScComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						scComponentAttribute.setScServiceDetailId(currentServiceDetail.getId());
						currentScComponent.getScComponentAttributes().add(scComponentAttribute);
						scComponentRepository.save(currentScComponent);
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in updateProgramName:: {}", e);
		}
	}

	public void retrieveAndProcessMigrationOrder(OdrOrderBean odrOrderBean) throws TclCommonException {
		Optional<OdrServiceDetailBean> optionalOdrServiceDetailBean=odrOrderBean.getOdrServiceDetails().stream().findFirst();
		if(optionalOdrServiceDetailBean.isPresent()){
			OdrServiceDetailBean odrServiceDetailBean=optionalOdrServiceDetailBean.get();
			List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findByTpsServiceIdOrUuid(odrServiceDetailBean.getUuid(),odrServiceDetailBean.getUuid());
			if(Objects.isNull(scServiceDetailList) || scServiceDetailList.isEmpty()){
		//for(OdrServiceDetailBean odrServiceDetailBean:odrOrderBean.getOdrServiceDetails()){
				LOGGER.info("Migration order doesn't exists in SF: {}",odrServiceDetailBean.getUuid());
				String response = (String) mqUtils.sendAndReceive(siMigrationQueue, odrServiceDetailBean.getUuid());
				LOGGER.info("Received response from getSIOrder for migration:: {}", response);
				SIGetOrderResponse orderResponse = Utils.fromJson(response, SIGetOrderResponse.class);
				if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus())) {
					SIOrderDataBean siOrderData = orderResponse.getOrder();
					Map<String, String> orderDetail = new HashMap<>();
					ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(siOrderData);
					Set<ScContractInfo> scContractingInfo = new HashSet<>();
					Optional<SIServiceDetailDataBean> siServiceDetailOptional=siOrderData.getServiceDetails().stream().findFirst();
					if(siServiceDetailOptional.isPresent()){	
						ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(siServiceDetailOptional.get().getContractInfo());
						contrEntity.setScOrder(scOrderEntity);
						scContractingInfo.add(contrEntity);
					};
					Set<ScServiceDetail> scServiceDetails = new HashSet<>();
					Map<Integer, String> serviceMapper = new HashMap<>();
					Map<Integer, ScComponent> compMapper = new HashMap<>();
					siOrderData.getServiceDetails().stream().forEach(serviceDetail -> {
						ScServiceDetail serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail, siOrderData);
						orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
						orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
						serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
						serviceEntity.setIsActive("Y");
						scOrderRepository.save(scOrderEntity);
						serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
						serviceEntity.setScOrder(scOrderEntity);
						List<SIComponentBean> components=serviceDetail.getComponents();
						if (components != null) {
							for (SIComponentBean siComponentBean : components) {
								ScComponent scComponent = new ScComponent();
								scComponent.setComponentName(siComponentBean.getComponentName());
								scComponent.setCreatedBy(siComponentBean.getCreatedBy());
								scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
								scComponent.setIsActive(CommonConstants.Y);
								scComponent.setSiteType("A");
								scComponent.setUuid(Utils.generateUid());
								for (SIComponentAttributeBean siComponentAttributeBean : siComponentBean
										.getSiComponentAttributes()) {
									ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
									scComponentAttribute.setAttributeAltValueLabel(siComponentAttributeBean.getAttributeValue());
									scComponentAttribute.setAttributeName(siComponentAttributeBean.getAttributeName());
									scComponentAttribute.setAttributeValue(siComponentAttributeBean.getAttributeValue());
									scComponentAttribute.setCreatedBy(siComponentAttributeBean.getCreatedBy());
									scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
									scComponentAttribute.setIsActive(CommonConstants.Y);
									scComponentAttribute.setScComponent(scComponent);
									scComponentAttribute.setUuid(Utils.generateUid());
									scComponent.getScComponentAttributes().add(scComponentAttribute);
								}
								compMapper.put(serviceEntity.getErfOdrServiceId(), scComponent);
							}
						}
						scServiceDetails.add(serviceEntity);
					});
					scContractInfoRepository.saveAll(scContractingInfo);
					scServiceDetailRepository.saveAll(scServiceDetails);
					Map<Integer, String> serviceIdMapper = new HashMap<>();
					for (ScServiceDetail scServiceDetail : scServiceDetails) {
						serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
					}
					for (ScServiceDetail scServiceDetail : scServiceDetails) {
						if(compMapper.get(scServiceDetail.getErfOdrServiceId())!=null) {
							ScComponent scCompo=compMapper.get(scServiceDetail.getErfOdrServiceId());
							scCompo.setScServiceDetailId(scServiceDetail.getId());
							for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
								scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
							}
							scComponentRepository.save(scCompo);
						}
						
						if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
							scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
							scServiceDetailRepository.save(scServiceDetail);
						}
					}
				} else {
					LOGGER.error("Error in retrieving getSIOrder for migration:: {}", orderResponse.getMessage());
					throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
			}
		}
	}

	private void updateComponentAttrs(OdrOrderBean odrOrderBean, String type) {
		if("MACD".equals(odrOrderBean.getOrderType()) && Objects.nonNull(odrOrderBean.getOrderCategory())){
			Optional<OdrServiceDetailBean> optionalOdrServiceDetail=odrOrderBean.getOdrServiceDetails().stream().findFirst();
			if(optionalOdrServiceDetail.isPresent()){
				LOGGER.info("Previous Service Detail exists to update local contact details");
				ScServiceDetail prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(optionalOdrServiceDetail.get().getUuid(), "ACTIVE");
				ScServiceDetail currentServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(optionalOdrServiceDetail.get().getUuid(), "INPROGRESS");
				updateMissingComponentAttributes(prevActiveServiceDetail,currentServiceDetail,odrOrderBean,type);
			}
		}
	}

	private void updateMissingComponentAttributes(ScServiceDetail prevActiveServiceDetail,
			ScServiceDetail currentServiceDetail, OdrOrderBean odrOrderBean, String type) {
		if(Objects.nonNull(prevActiveServiceDetail) && Objects.nonNull(currentServiceDetail)){
			ScComponent prevScComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(prevActiveServiceDetail.getId(),"LM","A");
				if(Objects.nonNull(prevScComponent) && Objects.nonNull(prevScComponent.getScComponentAttributes()) 
						&& !prevScComponent.getScComponentAttributes().isEmpty()){
					LOGGER.info("Previous Component Attr exists");
					/*if("ADD_IP".equalsIgnoreCase(odrOrderBean.getOrderCategory())){
						addLocalDetails(prevScComponent,currentServiceDetail);
					}*/
					
				String orderSubCategory = currentServiceDetail.getOrderSubCategory();
				String orderCategory = odrOrderBean.getOrderCategory();
				orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
				orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);

					if(Objects.nonNull(currentServiceDetail.getAccessType()) && !currentServiceDetail.getAccessType().toLowerCase().contains("offnet") 
							&& ("ADD_IP".equalsIgnoreCase(odrOrderBean.getOrderCategory()) 
							|| "CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory())
							|| (Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("lm")
									||orderSubCategory.toLowerCase().contains("bso") || "Shifting".equalsIgnoreCase(orderSubCategory))))){
						addMuxDetails(prevScComponent,currentServiceDetail);
					}
					if("ADD_IP".equalsIgnoreCase(orderCategory) 
							|| "CHANGE_BANDWIDTH".equalsIgnoreCase(orderCategory)
							|| (Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("lm")
									||orderSubCategory.toLowerCase().contains("bso") || "Shifting".equalsIgnoreCase(orderSubCategory)))){
						addLMDetails(prevScComponent,currentServiceDetail);
					}
					if("CHANGE_BANDWIDTH".equalsIgnoreCase(orderCategory) && "GVPN".equals(type)){
						addProgramNameDetails(prevScComponent,currentServiceDetail);
					}
					
					ScComponent currentScComponent = scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(currentServiceDetail.getId(),"LM", "A");
					Optional<ScComponentAttribute> lmTypeSccomponentAttr = currentScComponent.getScComponentAttributes().stream().
							filter(currentScComponetAttr -> currentScComponetAttr.getAttributeName().equalsIgnoreCase("lmType")).findFirst();
						if(lmTypeSccomponentAttr.isPresent()
								&& (lmTypeSccomponentAttr.get().getAttributeValue().equalsIgnoreCase("OffnetRF")
										|| lmTypeSccomponentAttr.get().getAttributeValue().equalsIgnoreCase("offnet wireless"))
								               && (currentServiceDetail.getOrderSubCategory() != null
												|| !currentServiceDetail.getOrderSubCategory().toLowerCase().contains("bso")
												|| !currentServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))) {
							LOGGER.info("Updating nniId Attribute from SI for service id :" +currentServiceDetail.getId());
							addNniIdAttributeDetail(currentScComponent,prevScComponent, currentServiceDetail.getId());
					     	}
						if(lmTypeSccomponentAttr.isPresent()
								&& (lmTypeSccomponentAttr.get().getAttributeValue()!=null && !lmTypeSccomponentAttr.get().getAttributeValue().isEmpty()
										&& lmTypeSccomponentAttr.get().getAttributeValue().toLowerCase().contains("offnet"))) {
							LOGGER.info("Updating bsoCircuitId Attribute from SI for service id " +currentServiceDetail.getId());
						    addbsoCircuitIdAttribute(currentScComponent,prevScComponent, currentServiceDetail.getId());
				         }
						addCpeAttribute(prevScComponent,currentServiceDetail);
	              }
	          }
	    }

	 private void addbsoCircuitIdAttribute(ScComponent currentScComponent, ScComponent prevScComponent, Integer serviceId) {
			prevScComponent.getScComponentAttributes().stream().forEach(prevScCompAttr -> {
	            if ("bsoCircuitId".equalsIgnoreCase(prevScCompAttr.getAttributeName())) {
	                LOGGER.info("bsoCircuitId value is " + prevScCompAttr.getAttributeValue());
	                addAttrDetails("bsoCircuitId",currentScComponent, prevScCompAttr,serviceId);
	            }
	          });
	        }
		
		private void addNniIdAttributeDetail(ScComponent currentScComponent,ScComponent prevScComponent, Integer serviceId) {
			prevScComponent.getScComponentAttributes().stream().forEach(prevScCompAttr -> {
				if ("nniId".equalsIgnoreCase(prevScCompAttr.getAttributeName())) {
					LOGGER.info("nniId value is " + prevScCompAttr.getAttributeValue());
					addAttrDetails("nniId",currentScComponent, prevScCompAttr,serviceId);
				}
			});
		}

		private void addAttrDetails(String attributename,ScComponent currentScComponent, ScComponentAttribute prevScCompAttr,Integer serviceId) {
				 Optional<ScComponentAttribute> scComponetAttribute=currentScComponent.getScComponentAttributes().stream().
						 filter(scCurrentComponetAttr->scCurrentComponetAttr.getAttributeName().equalsIgnoreCase(attributename)).findFirst();
				if (scComponetAttribute.isPresent()) {
					updateAttributeDetails(scComponetAttribute.get(), prevScCompAttr);
				} else {
					createScComponetAttribute(attributename,prevScCompAttr.getAttributeValue(),serviceId,currentScComponent,Utils.getSource());
				}
		    }

		private void updateAttributeDetails(ScComponentAttribute currentScComponentAttribute, ScComponentAttribute prevScComponentAttribute) {
			if (Objects.nonNull(currentScComponentAttribute) && Objects.nonNull(currentScComponentAttribute.getAttributeValue()) && !currentScComponentAttribute.getAttributeName().isEmpty()) {
				LOGGER.info(currentScComponentAttribute.getAttributeName()+"exists :"+prevScComponentAttribute.getAttributeValue());
						currentScComponentAttribute.setAttributeValue(prevScComponentAttribute.getAttributeValue());
						scComponentAttributesRepository.save(currentScComponentAttribute);
					}
				}
	
		  private void createScComponetAttribute(String attrName, String attrValue, Integer serviceId, ScComponent scComponent,String userName) {
			  ScComponentAttribute attribute = new ScComponentAttribute();
				attribute.setAttributeName(attrName);
				attribute.setAttributeValue(attrValue);
				attribute.setAttributeAltValueLabel(attrName);
				attribute.setScComponent(scComponent);
				attribute.setScServiceDetailId(serviceId);
				attribute.setIsActive("Y");
				attribute.setCreatedDate(new Timestamp(new Date().getTime()));
				attribute.setCreatedBy(userName);
				scComponentAttributesRepository.save(attribute);
		  }
			
			private void addProgramNameDetails(ScComponent prevScComponent, ScServiceDetail currentServiceDetail) {
				prevScComponent.getScComponentAttributes().stream().forEach(prevScCompAttr ->{
					if("programName".equalsIgnoreCase(prevScCompAttr.getAttributeName())){
						LOGGER.info("programName");
						addComponentAttrDetails(currentServiceDetail,prevScCompAttr,false);
					}
				});
			}
		
			private void addLMDetails(ScComponent prevScComponent, ScServiceDetail currentServiceDetail) {
				prevScComponent.getScComponentAttributes().stream().forEach(prevScCompAttr ->{
					if("lmType".equalsIgnoreCase(prevScCompAttr.getAttributeName())
							|| "lastMileProvider".equalsIgnoreCase(prevScCompAttr.getAttributeName())
							|| "wanIpAddress".equalsIgnoreCase(prevScCompAttr.getAttributeName())
							|| "wanIpProvidedByCust".equalsIgnoreCase(prevScCompAttr.getAttributeName())){
						LOGGER.info("addLMWanDetails");
						addComponentAttrDetails(currentServiceDetail,prevScCompAttr,true);
					}
				});
			}
			
	private void addMuxDetails(ScComponent prevScComponent, ScServiceDetail currentServiceDetail) {
		prevScComponent.getScComponentAttributes().stream().forEach(prevScCompAttr ->{
			if("endMuxNodeIp".equalsIgnoreCase(prevScCompAttr.getAttributeName())
					|| "endMuxNodeName".equalsIgnoreCase(prevScCompAttr.getAttributeName())){
				LOGGER.info("addMuxDetails");
				addComponentAttrDetails(currentServiceDetail,prevScCompAttr,false);
			}
		});
	}

	private void addLocalDetails(ScComponent prevScComponent, ScServiceDetail currentServiceDetail) {
		prevScComponent.getScComponentAttributes().stream().forEach(prevScCompAttr ->{
			if("localItContactEmailId".equalsIgnoreCase(prevScCompAttr.getAttributeName())
					|| "localItContactMobile".equalsIgnoreCase(prevScCompAttr.getAttributeName()) 
					|| "localItContactName".equalsIgnoreCase(prevScCompAttr.getAttributeName())){
				LOGGER.info("addLocalContactDetails");
				addComponentAttrDetails(currentServiceDetail,prevScCompAttr,false);
			}
		});
	}

	public void addComponentAttrDetails(ScServiceDetail currentServiceDetail,ScComponentAttribute prevScCompAttr, boolean isLmUpdate){
		if(Objects.nonNull(currentServiceDetail)){
			ScComponent currentScComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(currentServiceDetail.getId(),"LM","A");;
			if(isLmUpdate){
				updateLMDetails(currentScComponent,prevScCompAttr,currentServiceDetail);
			}else{
				addCompAttr(currentScComponent,prevScCompAttr,currentServiceDetail);
			}
		}
	}

	private void addCompAttr(ScComponent currentScComponent, ScComponentAttribute prevScCompAttr, ScServiceDetail currentServiceDetail) {
		if(Objects.nonNull(currentScComponent)){
			Boolean attributeExists[]={false};
			currentScComponent.getScComponentAttributes().stream().forEach(scCompAttr -> {
				if(prevScCompAttr.getAttributeName().equals(scCompAttr.getAttributeName())){
					attributeExists[0]=true;
				}
			});
			if(!attributeExists[0]){
				LOGGER.info("Attribute doesn't exists: {}",prevScCompAttr.getAttributeName());
				ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
				scComponentAttribute.setAttributeAltValueLabel(prevScCompAttr.getAttributeAltValueLabel());
				scComponentAttribute.setAttributeName(prevScCompAttr.getAttributeName());
				scComponentAttribute.setAttributeValue(prevScCompAttr.getAttributeValue());
				scComponentAttribute.setCreatedBy(prevScCompAttr.getCreatedBy());
				scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
				scComponentAttribute.setIsActive(CommonConstants.Y);
				scComponentAttribute.setScComponent(currentScComponent);
				scComponentAttribute.setUuid(Utils.generateUid());
				scComponentAttribute.setScServiceDetailId(currentServiceDetail.getId());
				currentScComponent.getScComponentAttributes().add(scComponentAttribute);
				scComponentRepository.save(currentScComponent);
			}
		}
	}

	private void updateLMDetails(ScComponent currentScComponent, ScComponentAttribute prevScCompAttr, ScServiceDetail currentServiceDetail) {
		if(Objects.nonNull(currentScComponent) && Objects.nonNull(currentScComponent.getScComponentAttributes())){
			boolean isLMExists[]={false};
			currentScComponent.getScComponentAttributes().stream().forEach(currentScCompAttr -> {
				if("lmType".equals(currentScCompAttr.getAttributeName()) && "lmType".equals(prevScCompAttr.getAttributeName())
						&& (Objects.isNull(currentScCompAttr.getAttributeValue()) || "MACD".equals(currentScCompAttr.getAttributeValue()))){
					LOGGER.info("LM type exists");
					isLMExists[0]=true;
					currentScCompAttr.setAttributeValue(prevScCompAttr.getAttributeValue());
					scComponentAttributesRepository.save(currentScCompAttr);
				}
				if("lastMileProvider".equals(currentScCompAttr.getAttributeName()) && "lastMileProvider".equals(prevScCompAttr.getAttributeName()) && ((Objects.isNull(currentScCompAttr.getAttributeValue()))
						|| (Objects.nonNull(currentScCompAttr.getAttributeValue()) && currentScCompAttr.getAttributeValue().isEmpty()))){
					LOGGER.info("LM Provider exists");
					isLMExists[0]=true;
					currentScCompAttr.setAttributeValue(prevScCompAttr.getAttributeValue());
					scComponentAttributesRepository.save(currentScCompAttr);
				}
				if("wanIpAddress".equals(currentScCompAttr.getAttributeName()) && "wanIpAddress".equals(prevScCompAttr.getAttributeName()) && ((Objects.isNull(currentScCompAttr.getAttributeValue()))
						|| (Objects.nonNull(currentScCompAttr.getAttributeValue()) && currentScCompAttr.getAttributeValue().isEmpty()))){
					LOGGER.info("Wan IP Address exists");
					isLMExists[0]=true;
					currentScCompAttr.setAttributeValue(prevScCompAttr.getAttributeValue());
					scComponentAttributesRepository.save(currentScCompAttr);
				}
				if("wanIpProvidedByCust".equals(currentScCompAttr.getAttributeName()) && "wanIpProvidedByCust".equals(prevScCompAttr.getAttributeName()) && ((Objects.isNull(currentScCompAttr.getAttributeValue()))
						|| (Objects.nonNull(currentScCompAttr.getAttributeValue()) && currentScCompAttr.getAttributeValue().isEmpty()))){
					LOGGER.info("Wan IP Provided By Customer exists");
					isLMExists[0]=true;
					currentScCompAttr.setAttributeValue(prevScCompAttr.getAttributeValue());
					scComponentAttributesRepository.save(currentScCompAttr);
				}
			});
			if(!isLMExists[0]){
				LOGGER.info("LM doesn't exists");
				addCompAttr(currentScComponent,prevScCompAttr,currentServiceDetail);
			}
		}
	}

	public void triggerWorkflow(Set<ScServiceDetail> scServiceDetails) {
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			LOGGER.info("work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());
			boolean ipcFlag = scServiceDetail.getScOrder().getOpOrderCode().startsWith("IPC");
			boolean nplFlag = scServiceDetail.getScOrder().getOpOrderCode().startsWith("NPL");
			boolean izopcFlag = scServiceDetail.getScOrder().getOpOrderCode().startsWith("IZOPC");
			
			Boolean status = false;
			if (ipcFlag) {
				ServiceFulfillmentRequest svcFulfillment = serviceCatalogueService
						.processServiceFulFillmentData(scServiceDetail.getId());
				LOGGER.info("IPC work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());
				status = processL2OService.processIPCL2ODataToFlowable(svcFulfillment);
			}else if(nplFlag) {
				LOGGER.info("NPL work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());
				status = processL2OService.processNPLL2ODataToFlowable(scServiceDetail.getId(), scServiceDetail,true);
			}else if(izopcFlag) {
				LOGGER.info("IZOPC work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());
				status = processL2OService.processIZOPCL2ODataToFlowable(scServiceDetail.getId(), scServiceDetail,true);
			}else {
				status = processL2OService.processL2ODataToFlowable(scServiceDetail.getId(), scServiceDetail,true);
			}
			if (!status) {
				ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
				srviceFulfillmentJob.setCreatedBy("system");
				srviceFulfillmentJob.setCreatedTime(new Date());
				srviceFulfillmentJob.setServiceId(scServiceDetail.getId());
				srviceFulfillmentJob.setType("NEW_SERVICE");
				srviceFulfillmentJob.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
				// srviceFulfillmentJob.setServiceUuid(scServiceDetail.getUuid());
				srviceFulfillmentJob.setRetryCount(0);
				srviceFulfillmentJob.setStatus("NEW");
				serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
			} else {
				if (StringUtils.isNotBlank(scServiceDetail.getLocalItContactName())
						&& (scServiceDetail.getIsOeCompleted() != null
						&& scServiceDetail.getIsOeCompleted().equals(CommonConstants.Y))) {
					initiateBasicEnrichment(scServiceDetail.getId(), scServiceDetail.getErfOdrServiceId(),
							scServiceDetail);
				}
			}
		}
	}
	
	public void triggerGSCWorkflow(Set<ScServiceDetail> scServiceDetails) {
		List<ScServiceDetail> gscServiceDetails=new ArrayList<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GSIP") && scServiceDetail.getParentId()==null) {
				gscServiceDetails.add(scServiceDetail);
			}
		}
		for (ScServiceDetail scServiceDetail : gscServiceDetails) {
			Boolean status = false;
			status = processL2OService.processGSCL2ODataToFlowable(scServiceDetail.getId(),scServiceDetail,null,null);
			if (!status) {
				ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
				srviceFulfillmentJob.setCreatedBy("system");
				srviceFulfillmentJob.setCreatedTime(new Date());
				srviceFulfillmentJob.setServiceId(scServiceDetail.getId());
				srviceFulfillmentJob.setType("NEW_SERVICE");
				srviceFulfillmentJob.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
				// srviceFulfillmentJob.setServiceUuid(scServiceDetail.getUuid());
				srviceFulfillmentJob.setRetryCount(0);
				srviceFulfillmentJob.setStatus("NEW");
				serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
			} 
			/*else {
				if (StringUtils.isNotBlank(scServiceDetail.getLocalItContactName())
						&& (scServiceDetail.getIsOeCompleted() != null
						&& scServiceDetail.getIsOeCompleted().equals(CommonConstants.Y))) {
					initiateBasicEnrichment(scServiceDetail.getId(), scServiceDetail.getErfOdrServiceId(),
							scServiceDetail);
				}
			}*/
		}
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void triggerGSCWorkflow(PlanItemRequestBean planItemRequestBean) {
		
			Boolean status = false;
			status = processL2OService.processGSCL2ODataToFlowable(planItemRequestBean.getServiceId(),null,planItemRequestBean.getPlanItemDefinitionId(),planItemRequestBean.getPlanItem());
			if (!status) {
				ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
				srviceFulfillmentJob.setCreatedBy("system");
				srviceFulfillmentJob.setCreatedTime(new Date());
				srviceFulfillmentJob.setServiceId(planItemRequestBean.getServiceId());
				srviceFulfillmentJob.setType("NEW_SERVICE");
				//srviceFulfillmentJob.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
				// srviceFulfillmentJob.setServiceUuid(scServiceDetail.getUuid());
				srviceFulfillmentJob.setRetryCount(0);
				srviceFulfillmentJob.setStatus("NEW");
				serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
			} 
			/*else {
				if (StringUtils.isNotBlank(scServiceDetail.getLocalItContactName())
						&& (scServiceDetail.getIsOeCompleted() != null
						&& scServiceDetail.getIsOeCompleted().equals(CommonConstants.Y))) {
					initiateBasicEnrichment(scServiceDetail.getId(), scServiceDetail.getErfOdrServiceId(),
							scServiceDetail);
				}
			}*/
		
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void processOrderEnrichmentfulfillmentDate(OdrOrderBean odrOrderBean) {
		try {
			LOGGER.info("Response Received {}", odrOrderBean);
			Set<ScServiceDetail> scServiceDetails = new HashSet<>();
			for (OdrServiceDetailBean serviceDetail : odrOrderBean.getOdrServiceDetails()) {
				LOGGER.info("processOrderEnrichmentfulfillment for odr_service_id={}", serviceDetail.getId());
				// List<CommonFlowStatus>
				// commonFlowStatuses=commonFlowStatusRepository.findByRefIdAndStatus(serviceDetail.getErfOdrServiceId()+"","INPROGRESS");
				// if(!commonFlowStatuses.isEmpty()) {
				// persistFulfillmentJob(serviceDetail);
				// continue;
				// }
				ScServiceDetail scServiceDetail = scServiceDetailRepository
						.findFirstByErfOdrServiceIdOrderByIdDesc(serviceDetail.getId());
				if (scServiceDetail != null) {
					LOGGER.info(
							"scServiceDetail Received scServiceDetail={},odr_service_id={} scServiceDetail.getLocalItContactName()={}",
							scServiceDetail.getId(), serviceDetail.getId(), scServiceDetail.getLocalItContactName());

					if (scServiceDetail.getLocalItContactName() != null && (scServiceDetail.getIsOeCompleted() != null
							&& scServiceDetail.getIsOeCompleted().equals(CommonConstants.Y))) {

						Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
								scServiceDetail.getId(), "basic_enrichment");
						if (task != null) {
							LOGGER.info(
									"processOrderEnrichmentfulfillmentDate task={} Execution ID={} ProcessInstId={}",
									task, task.getWfTaskId(), task.getWfProcessInstId());
							Execution execution = runtimeService.createExecutionQuery()
									.processInstanceId(task.getWfProcessInstId()).activityId("basic_enrichment")
									.singleResult();
							if (execution != null) {
								triggerBasicEnrichment(scServiceDetail, task, serviceDetail.getId());
							}
						}else {
							List<ServiceFulfillmentJob> serviceFulfillmentCount = serviceFulfillmentJobRepository
									.findByErfOdrServiceIdAndTypeAndStatus(serviceDetail.getId(), "ORDER_ENRICHMENT", "FAILURE");
							LOGGER.info("processOrderEnrichmentfulfillmentDate Failed to get basic_enrichment task with retry count {}", serviceFulfillmentCount.size());
							if (serviceFulfillmentCount.isEmpty()) {
								ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
								srviceFulfillmentJob.setCreatedBy("system");
								srviceFulfillmentJob.setCreatedTime(new Date());
								srviceFulfillmentJob.setType("ORDER_ENRICHMENT");
								srviceFulfillmentJob.setRetryCount(serviceFulfillmentCount.size() + 1);
								// srviceFulfillmentJob.setServiceUuid(serviceCode);
								srviceFulfillmentJob.setErfOdrServiceId(serviceDetail.getId());
								srviceFulfillmentJob.setServiceId(scServiceDetail.getId());
								srviceFulfillmentJob.setStatus("FAILURE");
								serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
								LOGGER.info("processOrderEnrichmentfulfillmentDate serviceFulfillmentJob saved with id {} ", srviceFulfillmentJob.getId());
							}
						}
						
						continue;
					}

					
					scServiceDetail.setLocalItContactEmail(serviceDetail.getLocalItContactEmail());
					scServiceDetail.setLocalItContactName(serviceDetail.getLocalItContactName());					
					scServiceDetail.setBillingGstNumber(serviceDetail.getBillingGstNumber());
					scServiceDetail.setIsOeCompleted(CommonConstants.Y);
					Map<String, String> atMap = new HashMap<>();
					
					for (OdrComponentBean odrComponent : serviceDetail.getOdrComponentBeans()) {				
						for (OdrComponentAttributeBean odrComponentAttributeBean : odrComponent.getOdrComponentAttributeBeans()) {
							atMap.put(odrComponentAttributeBean.getName(), odrComponentAttributeBean.getValue());
						}
						componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM,odrComponent.getSiteType());						
					}
					if (serviceDetail.getOdrGstAddress() != null) {
						ScGstAddress scGstAddress = scServiceDetail.getScGstAddress();
						if (scServiceDetail.getScGstAddress() == null) {
							scGstAddress = new ScGstAddress();
						}
						scGstAddress.setBuildingName(serviceDetail.getOdrGstAddress().getBuildingName());
						scGstAddress.setBuildingNumber(serviceDetail.getOdrGstAddress().getBuildingNumber());
						scGstAddress.setCreatedBy(serviceDetail.getOdrGstAddress().getCreatedBy());
						scGstAddress.setCreatedTime(serviceDetail.getOdrGstAddress().getCreatedTime());
						scGstAddress.setDistrict(serviceDetail.getOdrGstAddress().getDistrict());
						scGstAddress.setFlatNumber(serviceDetail.getOdrGstAddress().getFlatNumber());
						scGstAddress.setLatitude(serviceDetail.getOdrGstAddress().getLatitude());
						scGstAddress.setLocality(serviceDetail.getOdrGstAddress().getLocality());
						scGstAddress.setLongitude(serviceDetail.getOdrGstAddress().getLongitude());
						scGstAddress.setPincode(serviceDetail.getOdrGstAddress().getPincode());
						scGstAddress.setState(serviceDetail.getOdrGstAddress().getState());
						scGstAddress.setStreet(serviceDetail.getOdrGstAddress().getStreet());
						scServiceDetail.setScGstAddress(scGstAddress);
					}
					
					LOGGER.info("serviceDetail.getTaxExemptionFlag() {} ", serviceDetail.getTaxExemptionFlag());
					Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
					boolean isTaxFlag = false;
					boolean isAlreadyUploaded = false;
					for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
						ScAttachment scAttachment = serviceFulfillmentMapper
								.mapServiceAttachmentToEntity(odrAttachmentBean, scServiceDetail);
						Map<String, Object> attachmentMapper = scAttachmentRepository
								.findByServiceCodeAndType(scServiceDetail.getUuid(), odrAttachmentBean.getType());
						if (!attachmentMapper.isEmpty()) {
							LOGGER.info("Already Attachment and odrAttachment is found , so updating {}",
									attachmentMapper);
							scAttachment.getAttachment().setId((Integer) attachmentMapper.get("attachment_id"));
							scAttachment.setId((Integer) attachmentMapper.get("sc_attachment_id"));
							isAlreadyUploaded = true;
						} else {
							if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
								isTaxFlag = true;
							}
							LOGGER.info("Creating a new attachment as already the attachment object is not found");
						}
						scAttachments.add(scAttachment);
					}

					if (!isAlreadyUploaded && !isTaxFlag && serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
						scAttachments.add(
								persistAttachment(scServiceDetail, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
					}
					if (!isAlreadyUploaded &&serviceDetail.getTaxExemptionFlag()!=null &&  serviceDetail.getTaxExemptionFlag().equals("Y")) {
						scAttachments.add(persistAttachment(scServiceDetail, "TAXSD", "TAXSD"));
						scAttachments.add(persistAttachment(scServiceDetail, "GSTCET", "GSTCET"));
					}

					if (!isAlreadyUploaded) {
						if (scServiceDetail != null && scServiceDetail.getScContractInfo() != null
								&& scServiceDetail.getScContractInfo().getPoMandatoryStatus() != null) {
							String poStatus = scServiceDetail.getScContractInfo().getPoMandatoryStatus();
							if (poStatus != null && poStatus.equals("Y"))
								scAttachments.add(persistAttachment(scServiceDetail, PO_ATTACHMENT_CATEGORY,
										PO_ATTACHMENT_CATEGORY));
						}
					}

					scServiceDetail.setScAttachments(scAttachments);
					scServiceDetailRepository.save(scServiceDetail);
					scServiceDetails.add(scServiceDetail);
					initiateBasicEnrichment(scServiceDetail.getId(), serviceDetail.getId(),scServiceDetail);

				} else {
					persistFulfillmentJob(serviceDetail);
				}
				// scServiceDetailRepository.saveAll(scServiceDetails);

				LOGGER.info("scServiceDetail updated ");
			}
		} catch (Exception e) {
			LOGGER.error("Error in saving order enrichment service fulfillment ", e);
		}

	}

	private ScAttachment persistAttachment(ScServiceDetail scServiceDetail, String category, String type) {
		Attachment attachment = new Attachment();
		attachment.setCategory(category);
		attachment.setType(type);
		attachment.setIsActive("Y");
		attachment.setVerified("N");
		attachment.setCreatedBy(Utils.getSource());
		attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		attachment.setVerificationFailureReason("");
		Attachment taxAttachment = attachmentRepository.save(attachment);
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(taxAttachment);
		scAttachment.setScServiceDetail(scServiceDetail);
		scAttachment.setIsActive("Y");
		scAttachment.setSiteType("A");
		scAttachment.setOrderId(scServiceDetail.getScOrder()!=null ?scServiceDetail.getScOrder().getId():null);
		return scAttachment;
	}

	private void persistFulfillmentJob(OdrServiceDetailBean serviceDetail) {
		LOGGER.info("Failed to get the service Id for odr service={} ", serviceDetail.getId());
		ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
		srviceFulfillmentJob.setCreatedBy("system");
		srviceFulfillmentJob.setCreatedTime(new Date());
		srviceFulfillmentJob.setType("ORDER_ENRICHMENT");
		srviceFulfillmentJob.setErfOdrServiceId(serviceDetail.getId());
		// srviceFulfillmentJob.setServiceUuid(scServiceDetail.getUuid());
		// srviceFulfillmentJob.setServiceId(scServiceDetail.getId());
		srviceFulfillmentJob.setRetryCount(0);
		srviceFulfillmentJob.setStatus("FAILURE");
		serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
	}
	
	/**
	 * 
	 * @param serviceId
	 * @param erfOdrServiceId
	 * @param scServiceDetail
	 * 
	 * @author AmitSoni
	 * date: 09-Apr-2019
	 * Purpose: Fix for OTBPROD-6524 - Error occurs when task is going to be closed from backend.
	 */

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void initiateBasicEnrichment(Integer serviceId, Integer erfOdrServiceId,ScServiceDetail scServiceDetail) {
		LOGGER.info("initiateBasicEnrichment serviceId={}", serviceId);

		LOGGER.info("erfOdrServiceId=======" + erfOdrServiceId);

		

		if (scServiceDetail == null) {
			scServiceDetail = scServiceDetailRepository.findFirstByErfOdrServiceIdOrderByIdDesc(erfOdrServiceId);
			// Old Code
			//if (scServiceDetail == null) scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
			// New Code
			if (scServiceDetail == null) {
				Optional<ScServiceDetail> optScServiceDetail = scServiceDetailRepository.findById(serviceId);
				if(optScServiceDetail.isPresent()) {
					scServiceDetail = optScServiceDetail.get();
					serviceId = scServiceDetail.getId();
					LOGGER.info("Value is present=====" + serviceId);
				}
				else {
					 LOGGER.info("Value is not present...");
				}
			}
			// Old Code
			// serviceId = scServiceDetail.getId();
		}
		Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,
				"basic_enrichment");
		boolean basicEnrichmentTaskAvailable = true;
		if (task != null) {
			triggerBasicEnrichment(scServiceDetail,task,erfOdrServiceId);
		} else {
			basicEnrichmentTaskAvailable = false;
			LOGGER.info("basic_enrichment task is empty");
		}
		if (!basicEnrichmentTaskAvailable) {
			List<ServiceFulfillmentJob> serviceFulfillmentCount = serviceFulfillmentJobRepository
					.findByErfOdrServiceIdAndTypeAndStatus(erfOdrServiceId, "ORDER_ENRICHMENT", "FAILURE");
			LOGGER.info("Failed to get basic_enrichment task with retry count {}", serviceFulfillmentCount.size());
			if (serviceFulfillmentCount.isEmpty()) {
				ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
				srviceFulfillmentJob.setCreatedBy("system");
				srviceFulfillmentJob.setCreatedTime(new Date());
				srviceFulfillmentJob.setType("ORDER_ENRICHMENT");
				srviceFulfillmentJob.setRetryCount(serviceFulfillmentCount.size() + 1);
				// srviceFulfillmentJob.setServiceUuid(serviceCode);
				srviceFulfillmentJob.setErfOdrServiceId(erfOdrServiceId);
				srviceFulfillmentJob.setServiceId(serviceId);
				srviceFulfillmentJob.setStatus("FAILURE");
				serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
				LOGGER.info("ORDER_ENRICHMENT serviceFulfillmentJob saved with id {} ", srviceFulfillmentJob.getId());
			}
		}
		}
	
	
	private void triggerBasicEnrichment(ScServiceDetail scServiceDetail, Task task, Integer erfOdrServiceId){
		
		LOGGER.info("triggerBasicEnrichment Execution ID={} ProcessInstId={}", task.getWfTaskId(),
				task.getWfProcessInstId());
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId())
				.activityId("basic_enrichment").singleResult();
		if (execution != null) {		
			
			LOGGER.info("WfProcessInstId={} executionId={}", task.getWfProcessInstId(), execution.getId());
			if (scServiceDetail != null) {
				runtimeService.setVariable(execution.getId(), MasterDefConstants.LOCAL_IT_CONTACT_NAME,
						scServiceDetail.getLocalItContactName());
				runtimeService.setVariable(execution.getId(), MasterDefConstants.LOCAL_IT_CONTACT_EMAIL,
						scServiceDetail.getLocalItContactEmail());
			}
	
			runtimeService.trigger(execution.getId());
			List<ServiceFulfillmentJob> serviceFulfillmentCount = serviceFulfillmentJobRepository
					.findByErfOdrServiceIdAndTypeAndStatus(erfOdrServiceId, "ORDER_ENRICHMENT", "FAILURE");
			for (ServiceFulfillmentJob serviceFulfillmentJob : serviceFulfillmentCount) {
				serviceFulfillmentJob.setStatus("SUCCESS");
				serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
				LOGGER.info("Saving the order enrichment as Success for {}", erfOdrServiceId);
			}
		}
	}

	public void retriggerOE(Integer serviceId, Integer erfOdrServiceId)
			throws TclCommonException, IllegalArgumentException {
		LOGGER.info("retriggerOE serviceId={}", serviceId);
		ScServiceDetail scServiceDetail = scServiceDetailRepository
				.findFirstByErfOdrServiceIdOrderByIdDesc(erfOdrServiceId);
		LOGGER.info("Retriggering the order enrichment");
		Map<String, Object> requestparam = new HashMap<>();
		requestparam.put("orderId", scServiceDetail.getScOrder().getErfOrderId());
		requestparam.put("productName", scServiceDetail.getErfPrdCatalogProductName());
		requestparam.put("userName", scServiceDetail.getCreatedBy());
		mqUtils.send(odrOrderEnrichmentProcessQueue, Utils.convertObjectToJson(requestparam));
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Set<ScServiceDetail> processfulfillmentDateForNpl(OdrOrderBean odrOrderBean) throws Exception {
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
			ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
			contrEntity.setScOrder(scOrderEntity);
			scContractingInfo.add(contrEntity);
		});
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		odrOrderBean.getOdrServiceDetails().stream().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity = null;
			if (odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				serviceEntity = serviceFulfillmentMapper.mapIPCServiceEntityToBean(serviceDetail, odrOrderBean);
			} else {
				serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBeanNpl(serviceDetail, odrOrderBean);
			}
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			if (scOrderEntity.getOrderType().equalsIgnoreCase("Termination")) {
				serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATION_INPROGRESS));
				serviceEntity.setServiceConfigStatus(TaskStatusConstants.TERMINATION_INPROGRESS);
				serviceEntity.setActivationConfigStatus(TaskStatusConstants.TERMINATION_INPROGRESS);
				serviceEntity.setBillingStatus(TaskStatusConstants.TERMINATION_INPROGRESS);
				serviceEntity.setIsDelivered(TaskStatusConstants.TERMINATION_INPROGRESS);
			}
			else {
				serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
				serviceEntity.setServiceConfigStatus(TaskStatusConstants.PENDING);
				serviceEntity.setActivationConfigStatus(TaskStatusConstants.PENDING);
				serviceEntity.setBillingStatus(TaskStatusConstants.PENDING);
				serviceEntity.setIsDelivered(TaskStatusConstants.PENDING);
			}
			if (serviceEntity.getOrderType() == null) {
				serviceEntity.setOrderType(scOrderEntity.getOrderType());
			}
			if (serviceEntity.getOrderCategory() == null) {
				serviceEntity.setOrderCategory(scOrderEntity.getOrderCategory());
			}
			

			scOrderRepository.save(scOrderEntity);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag &&serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}

			if (serviceDetail.getOdrContractInfo() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))
				scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY, PO_ATTACHMENT_CATEGORY));
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components=serviceDetail.getOdrComponentBeans();
			if (components != null) {
				for (OdrComponentBean scComponentBean : components) {
					ScComponent scComponent=null;
					if (serviceEntity.getId() != null && serviceEntity.getOrderType() != null && serviceEntity.getOrderType().equalsIgnoreCase("Termination")) {
						scComponent = scComponentRepository
								.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(
										serviceEntity.getId(), "LM", scComponentBean.getSiteType());

					}
					if (scComponent == null) {
						scComponent = new ScComponent();

					}
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						if(serviceEntity.getId() != null && serviceEntity.getOrderType() != null  && serviceEntity.getOrderType().equalsIgnoreCase("Termination")) {
							scComponentAttribute.setScServiceDetailId(serviceEntity.getId());
						}
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						
						LOGGER.info("Component Attribute Name{} ",scComponentAttribute.getAttributeName() );
						if (scComponentAttribute.getAttributeName().equals("siteGstAddress")
								&& scComponentAttribute.getAttributeValue() != null && CommonConstants.MMR_CROSS_CONNECT
										.equalsIgnoreCase(serviceEntity.getErfPrdCatalogOfferingName())) {
							try {
								GstAddressBean gstAddress = Utils.convertJsonToObject(
										scComponentAttribute.getAttributeValue(), GstAddressBean.class);
								if (gstAddress != null) {
									ScGstAddress scGstAddress = new ScGstAddress();
									scGstAddress.setBuildingName(gstAddress.getBuildingName());
									scGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
									scGstAddress.setDistrict(gstAddress.getDistrict());
									scGstAddress.setFlatNumber(gstAddress.getFlatNumber());
									scGstAddress.setLatitude(gstAddress.getLatitude());
									scGstAddress.setLocality(gstAddress.getLocality());
									scGstAddress.setLongitude(gstAddress.getLongitude());
									scGstAddress.setPincode(gstAddress.getPinCode());
									scGstAddress.setState(gstAddress.getState());
									scGstAddress.setStreet(gstAddress.getStreet());
									LOGGER.info("save scGstAddress {} ",serviceDetail.getUuid());
									scGstAddress = scGstAddressRepository.save(scGstAddress);
									
									scComponentAttribute.setAttributeName("siteGstAddressId");
									scComponentAttribute.setAttributeAltValueLabel(scGstAddress.getId().toString());
									scComponentAttribute.setAttributeValue(scGstAddress.getId().toString());
									LOGGER.info("Site Gst Address id is {}", scComponentAttribute.getAttributeValue());
									
								}
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
						
						if(serviceEntity.getOrderType().equalsIgnoreCase("Termination")) {
							Integer serviceId = serviceEntity.getId();
							if(serviceDetail.getOdrServiceAttributes() != null && !serviceDetail.getOdrServiceAttributes().isEmpty()) {
								LOGGER.info("save approvalMailAvailable {} ",serviceDetail.getUuid());
								
								for (OdrServiceAttributeBean serviceAttr : serviceDetail.getOdrServiceAttributes()) {
									if (serviceAttr.getAttributeName().equals("approvalMailAvailable")) {
										LOGGER.info("save approvalMailAvailable {} ",serviceDetail.getUuid());
										
										scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "approvalMailAvailable",
												serviceAttr.getAttributeValue(), serviceId));
									}
									if (serviceAttr.getAttributeName().equals("csmEmail")) {
										LOGGER.info("save csmEmail {} ",serviceDetail.getUuid());
										
										scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "csmEmail",
												serviceAttr.getAttributeValue(), serviceId));
									}
									if (serviceAttr.getAttributeName().equals("csmUserName")) {
										LOGGER.info("save csmUserName {} ",serviceDetail.getUuid());
										
										scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "csmUserName",
												serviceAttr.getAttributeValue(), serviceId));
									}
								}
							}
							
							LOGGER.info("save terminationEffectiveDate {} ",serviceDetail.getUuid());
							
							scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "terminationEffectiveDate",
									serviceDetail.getTerminationEffectiveDate(), serviceId));
							serviceEntity.setTerminationEffectiveDate(serviceDetail.getTerminationEffectiveDate());
							serviceEntity.setTerminationFlowTriggered(CommonConstants.YES);
							serviceEntity.setCustomerRequestorDate(serviceDetail.getCustomerRequestorDate());
							scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "customerRequestorDate",
									serviceDetail.getCustomerRequestorDate(), serviceId));
							scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "etcValue",
									serviceDetail.getEtcValue(), serviceId));
							scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "etcWaiver",
									serviceDetail.getEtcWaiver(), serviceId));
							scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "terminationFlowTriggered",
									"Yes", serviceId));
							scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "backdatedTermination",
									serviceDetail.getBackdatedTermination(), serviceId));
							scComponent.getScComponentAttributes().add(saveComponentAttr(scComponent, "terminationReason",
									serviceDetail.getTerminationReason(), serviceId));
						}
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					if (compMapper.get(serviceEntity.getErfOdrServiceId()) != null) {
						LOGGER.info("update compMapper {} ",serviceEntity.getErfOdrServiceId());

						compMapper.get(serviceEntity.getErfOdrServiceId()).add(scComponent);
					}else {
						LOGGER.info("save compMapper {} ",serviceEntity.getErfOdrServiceId());

						List<ScComponent> scComp=new ArrayList<>();
						scComp.add(scComponent);
						compMapper.put(serviceEntity.getErfOdrServiceId(), scComp);
					}
				}
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			scServiceDetails.add(serviceEntity);
			

		});
		LOGGER.info("save scContractingInfo started");

		scContractInfoRepository.saveAll(scContractingInfo);
		LOGGER.info("save scContractingInfo ended");

		scOrderAttributeRepository.saveAll(scOrderAttrs);
		LOGGER.info("save scOrderAttrs ended");

		scServiceDetailRepository.saveAll(scServiceDetails);
		LOGGER.info("save scServiceDetails ended");

		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			//String scServiceId = serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial=serviceFulfillmentMapper.mapOdrCommercialToServiceCommercial(odrCommercial);
			if(scCommercialMapper.get(odrCommercial.getServiceId())==null) {
				List<ScServiceCommercial> scServiceCommercials=new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
			}else {
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scCommercial);
			}
		}	
		
		Map<Integer, String> serviceIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (scServiceDetail.getOrderType().equalsIgnoreCase("Termination")) {
				
				LOGGER.info("save updateServiceStatusAndCreatedNewStatus");

				updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.TERMINATION_INPROGRESS);
			} else {
				LOGGER.info("save createServiceStaus");

				createServiceStaus(scServiceDetail, TaskStatusConstants.INPROGRESS);

			}
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
		}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScComponent> scCompo = compMapper.get(scServiceDetail.getErfOdrServiceId());
				LOGGER.info("scCompo size:{}",scCompo.size());

				for (ScComponent scCompone : scCompo) {
					LOGGER.info("scCompo type:{} and:service id {} ",scCompone.getSiteType(),scServiceDetail.getId());

					scCompone.setScServiceDetailId(scServiceDetail.getId());
					for (ScComponentAttribute scCmpAttr : scCompone.getScComponentAttributes()) {
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					LOGGER.info("save comp",scCompone.getSiteType(),scServiceDetail.getId());

					scComponentRepository.save(scCompone);
					LOGGER.info("save comp end",scCompone.getSiteType(),scServiceDetail.getId());

				}
			}
			LOGGER.info("save scCommercialMapper sarted");

			if(scCommercialMapper.get(scServiceDetail.getErfOdrServiceId())!=null) {
				List<ScServiceCommercial> scCommercials=scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);
					LOGGER.info("save scCommercialMapper ended");

				}
				
			}
			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				LOGGER.info("save scServiceDetail started");

				scServiceDetailRepository.save(scServiceDetail);
				LOGGER.info("save scServiceDetail ended");

			}
			
			
			
			
			
		}
				
		
		if (odrOrderBean.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)
				&& !orderDetail.keySet().isEmpty()) {
			mqUtils.send(o2cIPCFulfillmentQueue, Utils.convertObjectToJson(orderDetail));
		}

		return scServiceDetails;

	}
	
	private void mapOdrComponentToScComponent(OdrComponentBean odrComponentBean,Integer scServiceDetailId) {
		ScComponent scComponent =new ScComponent();
		scComponent.setComponentName(odrComponentBean.getComponentName());
		scComponent.setCreatedBy(odrComponentBean.getCreatedBy());
		scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponent.setUpdatedBy(odrComponentBean.getUpdatedBy());
		scComponent.setUpdatedDate(new Timestamp(new Date().getTime()));
		scComponent.setScServiceDetailId(scServiceDetailId);
		ScComponent scComponent1 = scComponentRepository.saveAndFlush(scComponent);
		if(odrComponentBean.getOdrComponentAttributeBeans()!=null && !odrComponentBean.getOdrComponentAttributeBeans().isEmpty()) {
			odrComponentBean.getOdrComponentAttributeBeans().stream().forEach(comp->{
				mapOdrCompAttributeToSc(scComponent1, comp, scServiceDetailId);
			});
		}
	}
	
	private void mapOdrCompAttributeToSc(ScComponent scComponent,OdrComponentAttributeBean odrComponentAttributeBean,Integer serviceDetailId) {
		ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
		scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
		scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
		scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
		scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponentAttribute.setIsActive("Y");
		scComponentAttribute.setScComponent(scComponent);
		scComponentAttribute.setScServiceDetailId(serviceDetailId);
		scComponentAttribute.setUpdatedBy(odrComponentAttributeBean.getUpdatedBy());
		scComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		scComponentAttributesRepository.saveAndFlush(scComponentAttribute);
	}


	public void getServiceDetail(String serviceId, String orderCode,String orderType) throws TclCommonException {
		LOGGER.info("getServiceDetail:: {}", serviceId,orderCode);
		List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findByTpsServiceIdAndMstStatus_codeOrUuidAndMstStatus_code(serviceId,"ACTIVE",serviceId,"ACTIVE");
		if(Objects.isNull(scServiceDetailList) || scServiceDetailList.isEmpty() || "Termination".equalsIgnoreCase(orderType)){
			LOGGER.info("Service Id not exists in SF::{}", serviceId);
			String response = (String) mqUtils.sendAndReceive(siMigrationQueue, serviceId);
			LOGGER.info("Received response from getSIOrder for migration:: {}", response);
			SIGetOrderResponse orderResponse = Utils.fromJson(response, SIGetOrderResponse.class);
			if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus()) && orderResponse.getOrder()!=null) {
				SIOrderDataBean siOrderData = orderResponse.getOrder();
				//Map<String, String> orderDetail = new HashMap<>();
				ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(siOrderData);
				Set<ScContractInfo> scContractingInfo = new HashSet<>();
				Optional<SIServiceDetailDataBean> siServiceDetailOptional=siOrderData.getServiceDetails().stream().findFirst();
				if(siServiceDetailOptional.isPresent()){	
					ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(siServiceDetailOptional.get().getContractInfo());
					contrEntity.setScOrder(scOrderEntity);
					scContractingInfo.add(contrEntity);
				};
				Set<ScServiceDetail> scServiceDetails = new HashSet<>();
				Map<Integer, String> serviceMapper = new HashMap<>();
				Map<Integer, ScComponent> compMapper = new HashMap<>();
				siOrderData.getServiceDetails().stream().forEach(serviceDetail -> {
					ScServiceDetail serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail, siOrderData);
					/*orderDetail.put("ORDER_CODE", orderCode);
					orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());*/
					serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
					serviceEntity.setIsActive("Y");
					scOrderRepository.save(scOrderEntity);
					serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
					serviceEntity.setScOrder(scOrderEntity);
					List<SIComponentBean> components=serviceDetail.getComponents();
					if (components != null) {
						for (SIComponentBean siComponentBean : components) {
							ScComponent scComponent = new ScComponent();
							scComponent.setComponentName(siComponentBean.getComponentName());
							scComponent.setCreatedBy(siComponentBean.getCreatedBy());
							scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
							scComponent.setIsActive(CommonConstants.Y);
							scComponent.setSiteType("A");
							scComponent.setUuid(Utils.generateUid());
							for (SIComponentAttributeBean siComponentAttributeBean : siComponentBean
									.getSiComponentAttributes()) {
								ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
								scComponentAttribute.setAttributeAltValueLabel(siComponentAttributeBean.getAttributeValue());
								scComponentAttribute.setAttributeName(siComponentAttributeBean.getAttributeName());
								scComponentAttribute.setAttributeValue(siComponentAttributeBean.getAttributeValue());
								scComponentAttribute.setCreatedBy(siComponentAttributeBean.getCreatedBy());
								scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
								scComponentAttribute.setIsActive(CommonConstants.Y);
								scComponentAttribute.setScComponent(scComponent);
								scComponentAttribute.setUuid(Utils.generateUid());
								scComponent.getScComponentAttributes().add(scComponentAttribute);
							}
							
							compMapper.put(serviceEntity.getErfOdrServiceId(), scComponent);
						}
					}
					scServiceDetails.add(serviceEntity);
				});
				scContractInfoRepository.saveAll(scContractingInfo);
				scServiceDetailRepository.saveAll(scServiceDetails);
				Map<Integer, String> serviceIdMapper = new HashMap<>();
				for (ScServiceDetail scServiceDetail : scServiceDetails) {
					serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
				}
				for (ScServiceDetail scServiceDetail : scServiceDetails) {
					if(compMapper.get(scServiceDetail.getErfOdrServiceId())!=null) {
						ScComponent scCompo=compMapper.get(scServiceDetail.getErfOdrServiceId());
						scCompo.setScServiceDetailId(scServiceDetail.getId());
						for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
							scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
						}
						scComponentRepository.save(scCompo);
					}
					
					if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
						scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
						scServiceDetailRepository.save(scServiceDetail);
					}
				}
				scServiceDetailRepository.flush();
			} else {
				LOGGER.error("Error in retrieving getSIOrder for migration:: {}", orderResponse.getMessage());
				throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		}
	}

	private ScComponentAttribute saveComponentAttr(ScComponent scComponent, String attrName, String attrValue, Integer serviceId) {
		ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
		scComponentAttribute.setAttributeAltValueLabel("");
		scComponentAttribute.setAttributeName(attrName);
		scComponentAttribute.setAttributeValue(attrValue);
		scComponentAttribute.setCreatedBy("");
		scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponentAttribute.setIsActive(CommonConstants.Y);
		scComponentAttribute.setScComponent(scComponent);
		scComponentAttribute.setUuid(Utils.generateUid());
		scComponentAttribute.setScServiceDetailId(serviceId);
		return scComponentAttribute;
	}
	
	public void retrieveServiceDetail(String serviceId, String orderCode) throws TclCommonException {
		LOGGER.info("retrieveServiceDetail:: {}", serviceId, orderCode);
		List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findByTpsServiceIdOrUuid(serviceId,
				serviceId);
		if (Objects.isNull(scServiceDetailList) || scServiceDetailList.isEmpty()) {
			LOGGER.info("Service Id ::{}", serviceId);
			String response = (String) mqUtils.sendAndReceive(siMigrationQueue, serviceId);
			LOGGER.info("Received response from getSIOrder:: {}", response);
			SIGetOrderResponse orderResponse = Utils.fromJson(response, SIGetOrderResponse.class);
			if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus())) {
				SIOrderDataBean siOrderData = orderResponse.getOrder();
				// Map<String, String> orderDetail = new HashMap<>();
				ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(siOrderData);
				Set<ScContractInfo> scContractingInfo = new HashSet<>();
				Optional<SIServiceDetailDataBean> siServiceDetailOptional = siOrderData.getServiceDetails().stream()
						.findFirst();
				if (siServiceDetailOptional.isPresent()) {
					ScContractInfo contrEntity = serviceFulfillmentMapper
							.mapContractingInfoEntityToBean(siServiceDetailOptional.get().getContractInfo());
					contrEntity.setScOrder(scOrderEntity);
					scContractingInfo.add(contrEntity);
				}
				Set<ScServiceDetail> scServiceDetails = new HashSet<>();
				Map<Integer, String> serviceMapper = new HashMap<>();
				Map<Integer, ScComponent> compMapper = new HashMap<>();
				// site type A
				SIServiceDetailDataBean serviceDetail = siOrderData.getServiceDetails().stream()
						.filter(siDetail -> siDetail.getSiteType().equalsIgnoreCase("SiteA")).findFirst().get();
				if (serviceDetail != null) {
					ScServiceDetail serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail,
							siOrderData);
					/*
					 * orderDetail.put("ORDER_CODE", orderCode); orderDetail.put("ORDER_SERVICE_ID",
					 * serviceEntity.getUuid());
					 */
					serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
					serviceEntity.setIsActive("Y");
					scOrderRepository.save(scOrderEntity);
					serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
					serviceEntity.setScOrder(scOrderEntity);
					List<SIComponentBean> components = serviceDetail.getComponents();
					if (components != null) {
						for (SIComponentBean siComponentBean : components) {
							ScComponent scComponent = new ScComponent();
							scComponent.setComponentName(siComponentBean.getComponentName());
							scComponent.setCreatedBy(siComponentBean.getCreatedBy());
							scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
							scComponent.setIsActive(CommonConstants.Y);
							scComponent.setSiteType("A");
							scComponent.setUuid(Utils.generateUid());
							for (SIComponentAttributeBean siComponentAttributeBean : siComponentBean
									.getSiComponentAttributes()) {
								ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
								scComponentAttribute
										.setAttributeAltValueLabel(siComponentAttributeBean.getAttributeValue());
								scComponentAttribute.setAttributeName(siComponentAttributeBean.getAttributeName());
								scComponentAttribute.setAttributeValue(siComponentAttributeBean.getAttributeValue());
								scComponentAttribute.setCreatedBy(siComponentAttributeBean.getCreatedBy());
								scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
								scComponentAttribute.setIsActive(CommonConstants.Y);
								scComponentAttribute.setScComponent(scComponent);
								scComponentAttribute.setUuid(Utils.generateUid());
								scComponent.getScComponentAttributes().add(scComponentAttribute);
							}
							compMapper.put(serviceEntity.getErfOdrServiceId(), scComponent);
						}
					}
					scServiceDetails.add(serviceEntity);
				}
				scContractInfoRepository.saveAll(scContractingInfo);
				scServiceDetailRepository.saveAll(scServiceDetails);
				Map<Integer, String> serviceIdMapper = new HashMap<>();
				for (ScServiceDetail scServiceDetail : scServiceDetails) {
					serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
				}
				for (ScServiceDetail scServiceDetail : scServiceDetails) {
					if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
						ScComponent scCompo = compMapper.get(scServiceDetail.getErfOdrServiceId());
						scCompo.setScServiceDetailId(scServiceDetail.getId());
						for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
							scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
						}
						scComponentRepository.save(scCompo);
					}

					if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
						scServiceDetail
								.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
						scServiceDetailRepository.save(scServiceDetail);
					}
				}
				scServiceDetailRepository.flush();
				// site Type B
				SIServiceDetailDataBean serviceDetailB = siOrderData.getServiceDetails().stream()
						.filter(siDetail -> siDetail.getSiteType().equalsIgnoreCase("SiteB")).findFirst().get();
				if (serviceDetailB != null) {
					ScServiceDetail scServiceDet = scServiceDetails.stream().findFirst().get();
					List<SIComponentBean> components = serviceDetailB.getComponents();
					if (components != null) {
						for (SIComponentBean siComponentBean : components) {
							ScComponent scComponent = new ScComponent();
							scComponent.setComponentName(siComponentBean.getComponentName());
							scComponent.setCreatedBy(siComponentBean.getCreatedBy());
							scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
							scComponent.setIsActive(CommonConstants.Y);
							scComponent.setSiteType("B");
							scComponent.setUuid(Utils.generateUid());
							for (SIComponentAttributeBean siComponentAttributeBean : siComponentBean
									.getSiComponentAttributes()) {
								ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
								scComponentAttribute
										.setAttributeAltValueLabel(siComponentAttributeBean.getAttributeValue());
								scComponentAttribute.setAttributeName(siComponentAttributeBean.getAttributeName());
								scComponentAttribute.setAttributeValue(siComponentAttributeBean.getAttributeValue());
								scComponentAttribute.setCreatedBy(siComponentAttributeBean.getCreatedBy());
								scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
								scComponentAttribute.setIsActive(CommonConstants.Y);
								scComponentAttribute.setScComponent(scComponent);
								scComponentAttribute.setUuid(Utils.generateUid());
								scComponent.getScComponentAttributes().add(scComponentAttribute);
							}
							if (scServiceDet != null) {
								scComponent.setScServiceDetailId(scServiceDet.getId());
								for (ScComponentAttribute scCmpAttr : scComponent.getScComponentAttributes()) {
									scCmpAttr.setScServiceDetailId(scServiceDet.getId());
								}
								scComponentRepository.save(scComponent);
							}
						}
					}
				}
			} else {
				LOGGER.error("Error in retrieving getSIOrder for migration:: {}", orderResponse.getMessage());
				throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		}
	}

	private Map<String, String> getCosMappingBetweenServiceInventoryAndO2CScServiceAttribute() {
		/*this below map have attribute_name mapping between service inventory
		si_service_attribute to O2C sc_service_attribute*/
		Map<String, String> attributes = new HashMap<>();
		/*attributes.put("cos 1", "cos 1");
		attributes.put("cos 2", "cos 2");
		attributes.put("cos 3", "cos 3");
		attributes.put("cos 4", "cos 4");
		attributes.put("cos 5", "cos 5");
		attributes.put("cos 6", "cos 6");*/
		attributes.put("COS1_Criteria", "cos 1 criteria");
		attributes.put("COS2_Criteria", "cos 2 criteria");
		attributes.put("COS3_Criteria", "cos 3 criteria");
		attributes.put("COS4_Criteria", "cos 4 criteria");
		attributes.put("COS5_Criteria", "cos 5 criteria");
		attributes.put("COS6_Criteria", "cos 6 criteria");
		attributes.put("COS1_Criteria_Value1", "cos 1 criteria value");
		attributes.put("COS2_Criteria_Value1", "cos 2 criteria value");
		attributes.put("COS3_Criteria_Value1", "cos 3 criteria value");
		attributes.put("COS4_Criteria_Value1", "cos 4 criteria value");
		attributes.put("COS5_Criteria_Value1", "cos 5 criteria value");
		attributes.put("COS6_Criteria_Value1", "cos 6 criteria value");
		return attributes;
	}

	private void updateCosCriteriaMissingAttribute(OdrServiceDetailBean odrServiceDetailBean,String uuid,String prevUuid) {
		Map<String, String> attributesMap = getCosMappingBetweenServiceInventoryAndO2CScServiceAttribute();

		LOGGER.info("updateCosCriteriaMissingAttribute=>{}",uuid);
		ScServiceDetail prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(prevUuid, "ACTIVE");
		ScServiceDetail currentServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(uuid, "INPROGRESS");
		
		Set<ScServiceAttribute> currScServiceAttributesSet =currentServiceDetail.getScServiceAttributes();
		Map<String, String> currScServiceAttributes = new HashMap<>();
		for (ScServiceAttribute scAttribute : currScServiceAttributesSet) {
			currScServiceAttributes.put(scAttribute.getAttributeName(),scAttribute.getAttributeValue());
		}
		
		
		
		LOGGER.info("Service ID for current service details"+currentServiceDetail.getId());
		prevActiveServiceDetail.getScServiceAttributes().stream().forEach(prevScServiceAttribute -> {
			if (attributesMap.containsKey(prevScServiceAttribute.getAttributeName()) && Objects.nonNull(prevScServiceAttribute.getAttributeValue())) {

				Map<String, String> scAttribute = new HashMap<>();
				/*String cosValue = getCosValue(prevScServiceAttribute.getAttributeName(), prevScServiceAttribute.getAttributeValue());
				if (Objects.nonNull(cosValue)) {
					scAttribute.put(attributesMap.get(prevScServiceAttribute.getAttributeName()), cosValue);
				}*/
				if(!currScServiceAttributes.containsKey(attributesMap.get(prevScServiceAttribute.getAttributeName())) 
						|| StringUtils.isBlank(currScServiceAttributes.get(attributesMap.get(prevScServiceAttribute.getAttributeName())))) {
					LOGGER.info("prevAttributeName()=>{} curAttributeName={} curAttributeValue={}"+prevScServiceAttribute.getAttributeName(),attributesMap.get(prevScServiceAttribute.getAttributeName()),currScServiceAttributes.get(attributesMap.get(prevScServiceAttribute.getAttributeName())));
					String cosCriteria = getCosCriteria(prevScServiceAttribute.getAttributeName(), prevScServiceAttribute.getAttributeValue());
					if (Objects.nonNull(cosCriteria)) {
						String cosKey = attributesMap.get(prevScServiceAttribute.getAttributeName()).substring(0, 5);
						String curCosPer = StringUtils.trimToEmpty(currScServiceAttributes.get(cosKey));
						if(getCosValue(cosKey,curCosPer)!=null) {
							scAttribute.put(attributesMap.get(prevScServiceAttribute.getAttributeName()), cosCriteria);
						}
					}
					String cosCriteriaValue = getCosCriteriaValue(prevScServiceAttribute.getAttributeName(), prevScServiceAttribute.getAttributeValue());
					if (Objects.nonNull(cosCriteriaValue)){
						String cosKey = attributesMap.get(prevScServiceAttribute.getAttributeName()).substring(0, 5);
						String curCosPer = StringUtils.trimToEmpty(currScServiceAttributes.get(cosKey));
						if(getCosValue(cosKey,curCosPer)!=null) {
							scAttribute.put(attributesMap.get(prevScServiceAttribute.getAttributeName()), cosCriteriaValue);
						}
					}
				}
				if(!scAttribute.isEmpty()) {
					LOGGER.info("Cos attributes key and values need to save "+attributesMap);
					componentAndAttributeService.updateServiceAttributes(currentServiceDetail.getId(), scAttribute);
				}

			}
		});
	}

	private String getCosValue(String attributeName, String attributeValue) {
		String attrValue = null;
		if ("cos 1".equalsIgnoreCase(attributeName) || "cos 2".equalsIgnoreCase(attributeName) ||
				"cos 3".equalsIgnoreCase(attributeName) || "cos 4".equalsIgnoreCase(attributeName) ||
				"cos 5".equalsIgnoreCase(attributeName) || "cos 6".equalsIgnoreCase(attributeName)) {
			LOGGER.info("COS attribute and value "+attributeName+" , "+attributeValue);
			if (!attributeValue.isEmpty() && Integer.parseInt(attributeValue.replace("%", "")) > 0) {
				attrValue = attributeValue;
			}
			LOGGER.info("COS retrun value "+attrValue);
		}
		return attrValue;
	}

	private String getCosCriteria(String attributeName, String attributeValue) {
		String attrValue = null;
		if ("COS1_Criteria".equalsIgnoreCase(attributeName) || "COS2_Criteria".equalsIgnoreCase(attributeName) ||
				"COS3_Criteria".equalsIgnoreCase(attributeName) || "COS4_Criteria".equalsIgnoreCase(attributeName) ||
				"COS5_Criteria".equalsIgnoreCase(attributeName) || "COS6_Criteria".equalsIgnoreCase(attributeName)) {
			LOGGER.info("COS_Criteria attribute and value "+attributeName+" , "+attributeValue);
			if (StringUtils.containsIgnoreCase(attributeValue,"DSCP")) {
				attrValue = "DSCP";
			}
			if (StringUtils.containsIgnoreCase(attributeValue,"IPprecendence")) {
				attrValue = "Ip precedence";
			}
			if (StringUtils.containsIgnoreCase(attributeValue,"IP Address")) {
				attrValue = "IP Address";
			}
			if (StringUtils.containsIgnoreCase(attributeValue,"OTHERS")) {
				attrValue = "Any";
			}
			LOGGER.info("COS_Criteria retrun value "+attrValue);
		}
		return attrValue;
	}

	private String getCosCriteriaValue(String attributeName, String attributeValue) {
		String attrValue = null;
		if ("COS1_Criteria_Value1".equalsIgnoreCase(attributeName) || "COS2_Criteria_Value1".equalsIgnoreCase(attributeName) ||
				"COS3_Criteria_Value1".equalsIgnoreCase(attributeName) || "COS4_Criteria_Value1".equalsIgnoreCase(attributeName) ||
				"COS5_Criteria_Value1".equalsIgnoreCase(attributeName)|| "COS6_Criteria_Value1".equalsIgnoreCase(attributeName)) {
			LOGGER.info("COS_Criteria_Value attribute and value "+attributeName+" , "+attributeValue);
			if (!attributeValue.isEmpty()
					&& !"NA".equalsIgnoreCase(attributeValue) && !"Not Available".equalsIgnoreCase(attributeValue)){
				attrValue = attributeValue;
			}
			LOGGER.info("COS_Criteria_Value return value "+attrValue);
		}
		return attrValue;
	}
	
	/**
	 * Method to process service fulfillment for GDE
	 * @param odrOrderBean
	 * @return
	 * @throws Exception
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Set<ScServiceDetail> processfulfillmentDateForGde(OdrOrderBean odrOrderBean) throws Exception {
		
		if("MACD".equals(odrOrderBean.getOrderType())){
			LOGGER.info("OrderCode:: {} ",odrOrderBean.getOpOrderCode());
			for(OdrServiceDetailBean odrServiceDetail:odrOrderBean.getOdrServiceDetails()){
				if(Objects.nonNull(odrServiceDetail.getUuid())){
					LOGGER.info("Uuid:: {} ",odrServiceDetail.getUuid());
					getServiceDetail(odrServiceDetail.getUuid(),odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
				}else if(Objects.nonNull(odrServiceDetail.getParentServiceUuid())){
					LOGGER.info("Parent Uuid:: {}",odrServiceDetail.getParentServiceUuid());
					getServiceDetail(odrServiceDetail.getParentServiceUuid(),odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
				}
			}
		}
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
			ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
			contrEntity.setScOrder(scOrderEntity);
			scContractingInfo.add(contrEntity);
		});
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		odrOrderBean.getOdrServiceDetails().stream().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBeanGde(serviceDetail, odrOrderBean);
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			scOrderRepository.save(scOrderEntity);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag &&serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}

			if (serviceDetail.getOdrContractInfo() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))
				scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY, PO_ATTACHMENT_CATEGORY));
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);																					   
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components=serviceDetail.getOdrComponentBeans();
			if (components != null) {
				for (OdrComponentBean scComponentBean : components) {
					ScComponent scComponent = new ScComponent();
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					if (compMapper.get(serviceEntity.getErfOdrServiceId()) != null) {
						compMapper.get(serviceEntity.getErfOdrServiceId()).add(scComponent);
					}else {
						List<ScComponent> scComp=new ArrayList<>();
						scComp.add(scComponent);
						compMapper.put(serviceEntity.getErfOdrServiceId(), scComp);
					}
								
																			  
																									 
				}

			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			scServiceDetails.add(serviceEntity);
			

		});
		scContractInfoRepository.saveAll(scContractingInfo);
		scOrderAttributeRepository.saveAll(scOrderAttrs);
		scServiceDetailRepository.saveAll(scServiceDetails);
		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			//String scServiceId = serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial=serviceFulfillmentMapper.mapOdrCommercialToServiceCommercial(odrCommercial);
			if(scCommercialMapper.get(odrCommercial.getServiceId())==null) {
				List<ScServiceCommercial> scServiceCommercials=new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
			}else {
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scCommercial);
			}
											  
		}	
		Map<Integer, String> serviceIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
																										 
																										 
																					   
															   
					   
		}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScComponent> scCompo = compMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScComponent scCompone : scCompo) {
					scCompone.setScServiceDetailId(scServiceDetail.getId());
					for (ScComponentAttribute scCmpAttr : scCompone.getScComponentAttributes()) {
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					scComponentRepository.save(scCompone);  
				}
													   
			}
			if(scCommercialMapper.get(scServiceDetail.getErfOdrServiceId())!=null) {
				List<ScServiceCommercial> scCommercials=scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);
				}				   
			}
			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				scServiceDetailRepository.save(scServiceDetail);
			}
				   
		}

		return scServiceDetails;															   
										  
	}
	
	private ServiceStatusDetails createServiceStaus(ScServiceDetail scServiceDetail, String mstStatus) {

		ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
		serviceStatusDetails.setScServiceDetail(scServiceDetail);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceStatusDetails.setUserName(userInfoUtils.getUserInformation().getUserId());
		}
		serviceStatusDetails.setStartTime(new Timestamp(new Date().getTime()));

		serviceStatusDetails.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setStatus(mstStatus);
		serviceStatusDetailsRepository.save(serviceStatusDetails);

		return serviceStatusDetails;

	}
	private ServiceStatusDetails updateServiceStatusAndCreatedNewStatus(ScServiceDetail scServiceDetail,String status) {
			
			ServiceStatusDetails serviceStatusDetails=	serviceStatusDetailsRepository.findFirstByScServiceDetail_idOrderByIdDesc(scServiceDetail.getId());
			
			if(serviceStatusDetails!=null) {
				serviceStatusDetails.setEndTime(new Timestamp(new Date().getTime()));
				serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));
				serviceStatusDetailsRepository.save(serviceStatusDetails);
			}
			createServiceStaus(scServiceDetail, status);
			return serviceStatusDetails;
		}

	@Transactional(readOnly = false)
	public void processCancellationRequestFromL2O(OdrOrderBean request) throws TclCommonException {
		LOGGER.info("Inside cancellation process of O2C");
		if (request != null && request.getCancellationOrderCode() != null && request.getOdrServiceDetails()!=null) {
			request.getOdrServiceDetails().stream().forEach(odrServiceDetailBean -> {
				if (odrServiceDetailBean!=null && odrServiceDetailBean.getUuid()!=null) {
					LOGGER.info("Inside cancellation process of O2C with cancel order code and service code : {} {}",request.getCancellationOrderCode(),odrServiceDetailBean.getUuid());
					ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrder_OpOrderCodeAndIsMigratedOrder(odrServiceDetailBean.getUuid(), request.getCancellationOrderCode());
					if (isEligibleForCancellation(scServiceDetail)) {
						LOGGER.info("Inside cancellation process with service status : {}", scServiceDetail.getMstStatus().getCode());
						CancellationRequest cancellationRequest = new CancellationRequest();
						cancellationRequest.setCancellationOrderCode(request.getCancellationOrderCode());
						cancellationRequest.setCancellationServiceCode(odrServiceDetailBean.getUuid());
						cancellationRequest.setOrderCode(request.getOpOrderCode());
						cancellationRequest.setServiceId(scServiceDetail.getId());
						cancellationRequest.setSfdcOpportunityId(request.getSfdcOptyId());
						cancellationRequest.setCancellationCharges(odrServiceDetailBean.getCancellationCharges());
						cancellationRequest.setChargesAbsorbedOrpassed(odrServiceDetailBean.getAbsorbedOrPassedOn());
						if (odrServiceDetailBean.getOdrServiceAttributes()!=null && odrServiceDetailBean.getOdrServiceAttributes().stream().anyMatch
								(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().
										equalsIgnoreCase("cancellationReason") && odrServiceAttributeBean.getAttributeValue() != null)) {
							cancellationRequest.setCancellationReason(odrServiceDetailBean.getOdrServiceAttributes().stream().
									filter(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().equalsIgnoreCase("cancellationReason")).findFirst().get().getAttributeValue());
						}
						if (odrServiceDetailBean.getOdrServiceAttributes()!=null && odrServiceDetailBean.getOdrServiceAttributes().stream().anyMatch
								(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().
										equalsIgnoreCase("leadToRFSDate") && odrServiceAttributeBean.getAttributeValue() != null)) {
							cancellationRequest.setLeadToRFSDays(odrServiceDetailBean.getOdrServiceAttributes().stream().
									filter(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().equalsIgnoreCase("leadToRFSDate")).findFirst().get().getAttributeValue());
						}
						if (odrServiceDetailBean.getOdrServiceAttributes()!=null && odrServiceDetailBean.getOdrServiceAttributes().stream().anyMatch
								(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().
										equalsIgnoreCase("effectiveDateOfChange") && odrServiceAttributeBean.getAttributeValue() != null)) {
							cancellationRequest.setEffectiveDateOfChange(DateUtil.convertStringToTimeStampYYMMDD(odrServiceDetailBean.getOdrServiceAttributes().stream().
									filter(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().equalsIgnoreCase("effectiveDateOfChange")).findFirst().get().getAttributeValue()));
						}
						String customerRequestorDate=null;
						if (odrServiceDetailBean.getOdrServiceAttributes()!=null && odrServiceDetailBean.getOdrServiceAttributes().stream().anyMatch
								(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().
										equalsIgnoreCase("customerRequestorDate") && odrServiceAttributeBean.getAttributeValue() != null)) {
							customerRequestorDate=odrServiceDetailBean.getOdrServiceAttributes().stream().
									filter(odrServiceAttributeBean -> odrServiceAttributeBean.getAttributeName().equalsIgnoreCase("customerRequestorDate")).findFirst().get().getAttributeValue();
						}
						if(request.getCreatedBy()!=null && !request.getCreatedBy().isEmpty()){
							cancellationRequest.setCancellationCreatedBy(request.getCreatedBy());
						}
						cancellationRequestRepository.save(cancellationRequest);
						try {
							boolean waitingResourceRelease=false;
							if(scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED_INITIATED)) {
								waitingResourceRelease=true;
							}
							 if(odrServiceDetailBean.getOdrAttachments()!=null && odrServiceDetailBean.getOdrAttachments().stream().filter(odrAttachmentBean -> odrAttachmentBean.getType().equalsIgnoreCase("CANCEL_EMAIL")).findFirst().isPresent()) {
								 Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
								 OdrAttachmentBean odrAttachmentBeanVal = odrServiceDetailBean.getOdrAttachments().stream().filter(odrAttachmentBean -> odrAttachmentBean.getType().equalsIgnoreCase("CANCEL_EMAIL")).findFirst().get();
								 ScAttachment scAttachment = serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrServiceDetailBean.getOdrAttachments().stream().filter(odrAttachmentBean -> odrAttachmentBean.getType().equalsIgnoreCase("CANCEL_EMAIL")).findFirst().get(), scServiceDetail);
								 Map<String, Object> attachmentMapper = scAttachmentRepository
										 .findByServiceCodeAndType(scServiceDetail.getUuid(), odrAttachmentBeanVal.getType());
								 if (!attachmentMapper.isEmpty()) {
									 LOGGER.info("Already Attachment and odrAttachment is found , so updating {}",
											 attachmentMapper);
									 scAttachment.getAttachment().setId((Integer) attachmentMapper.get("attachment_id"));
									 scAttachment.setId((Integer) attachmentMapper.get("sc_attachment_id"));
								 }
								 scAttachments.add(scAttachment);
								 scServiceDetail.setScAttachments(scAttachments);
							 }
							taskService.processCancellationFromL2O(scServiceDetail, "Customer", cancellationRequest.getCancellationReason(), cancellationRequest,waitingResourceRelease,customerRequestorDate);

						} catch (TclCommonException e) {
							LOGGER.error("processCancellationRequestFromL2O with service:{} and error:{}",scServiceDetail.getUuid(),e);

							throw new TclCommonRuntimeException(
									com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANCELLLATION_NOT_PROCESSED_IN_O2C,
									ResponseResource.R_CODE_ERROR);
						}
					} else {
						LOGGER.error("Service is not in inprogree or hold with service code and status : {} {}",
								scServiceDetail.getUuid(), scServiceDetail.getMstStatus().getCode());
					}
				}
			});

		}
	}

	/**
	 * @author vivek
	 *
	 * @return
	 */
	private boolean isEligibleForCancellation(ScServiceDetail scServiceDetail) {
		if (scServiceDetail != null && (TaskStatusConstants.INPROGRESS
				.equalsIgnoreCase(scServiceDetail.getMstStatus().getCode())
				|| TaskStatusConstants.HOLD.equalsIgnoreCase(scServiceDetail.getMstStatus().getCode())
				|| TaskStatusConstants.RESOURCE_RELEASED_INITIATED
						.equalsIgnoreCase(scServiceDetail.getMstStatus().getCode())
				|| TaskStatusConstants.RESOURCE_RELEASED.equalsIgnoreCase(scServiceDetail.getMstStatus().getCode())
				|| TaskStatusConstants.JEOPARDY_INITIATED.equalsIgnoreCase(scServiceDetail.getMstStatus().getCode()))
				&& !"Yes".equalsIgnoreCase(scServiceDetail.getCancellationFlowTriggered())) {
			return true;
		}
		return false;
	}

	/**
	 * @author vivek
	 *
	 * @param scServiceDetails
	 * @throws TclCommonException 
	 */
	public void triggerTerminationWorkflow(Set<ScServiceDetail> scServiceDetails) throws TclCommonException {
		
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			boolean nplFlag = scServiceDetail.getScOrder().getOpOrderCode().startsWith("NPL");
			if (nplFlag) {
				processL2OService.processNplTerminationWorkflow(scServiceDetail.getId(), scServiceDetail, true);

			} else {
				processL2OService.processTerminationWorkflow(scServiceDetail.getId(), scServiceDetail, true);

			}

		}
	}
	
	@Transactional(readOnly = false)
	public boolean isNotValidOrderForTermination(OdrOrderBean odrOrderBean) {
		try {
			LOGGER.info("Termination Validation Check for order code {} ", odrOrderBean.getOpOrderCode());
			List<TerminationNotEligibleNoticationBean> terminationNotEligibleNoticationBeans = new ArrayList<>();
			
			for (OdrServiceDetailBean odrServiceDetailBean : odrOrderBean.getOdrServiceDetails()) {
				if(odrServiceDetailBean.getOrderType() != null && odrServiceDetailBean.getOrderType().equalsIgnoreCase("Termination")) {
					
					String reason = processL2OService.getTerminationNotEligibeReason(odrServiceDetailBean, false);
					if(reason != null) {
						LOGGER.info("Termination Validation Check for service code {} and reason {} ", odrServiceDetailBean.getUuid(), reason);
						TerminationNotEligibleNoticationBean terminationNotEligibleNoticationBean = new TerminationNotEligibleNoticationBean();
						terminationNotEligibleNoticationBean.setOrderCode(odrOrderBean.getOpOrderCode());
						terminationNotEligibleNoticationBean.setReason(reason);
						terminationNotEligibleNoticationBean.setServiceCode(odrServiceDetailBean.getUuid());
						terminationNotEligibleNoticationBean.setTerminationEffectiveDate(odrServiceDetailBean.getTerminationEffectiveDate());
						terminationNotEligibleNoticationBeans.add(terminationNotEligibleNoticationBean);
					}
				}
			}
			if(!terminationNotEligibleNoticationBeans.isEmpty()) {
				LOGGER.info("Termination Validation Check for order code {} and List Size {} ",
						odrOrderBean.getOpOrderCode(), terminationNotEligibleNoticationBeans.size());
					processL2OService.sendTerminationNotEligibleNotification(terminationNotEligibleNoticationBeans, odrOrderBean.getOpOrderCode(), "100%");
					return true;
				}
		} catch (Exception e) {
			LOGGER.error("Error while Termination validation ordercode {} and error {}",  odrOrderBean.getOpOrderCode(), e);
			return true;
		}
		return false;
	}

	
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public Set<ScServiceDetail> processfulfillmentDateWebEx(OdrOrderBean odrOrderBean) throws Exception {
		// Migration Order Changes
		/*
		 * if ("MACD".equals(odrOrderBean.getOrderType())) {
		 * LOGGER.info("OrderCode::{}", odrOrderBean.getOpOrderCode()); for
		 * (OdrServiceDetailBean odrServiceDetail : odrOrderBean.getOdrServiceDetails())
		 * { if (Objects.nonNull(odrServiceDetail.getUuid())) { LOGGER.info("Uuid::{}",
		 * odrServiceDetail.getUuid()); getServiceDetail(odrServiceDetail.getUuid(),
		 * odrOrderBean.getOpOrderCode()); } else if
		 * (Objects.nonNull(odrServiceDetail.getParentServiceUuid())) {
		 * LOGGER.info("Parent Uuid::{}", odrServiceDetail.getParentServiceUuid());
		 * getServiceDetail(odrServiceDetail.getParentServiceUuid(),
		 * odrOrderBean.getOpOrderCode()); } } }
		 */
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		if (odrOrderBean.getOdrContractInfos() != null && !odrOrderBean.getOdrContractInfos().isEmpty()) {
			odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
				ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
				contrEntity.setScOrder(scOrderEntity);
				scContractingInfo.add(contrEntity);
			});
		}
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, ScServiceDetail> serviceParentIdMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<Integer, String> compOdrMapper = new HashMap<>();
		Map<Integer, Integer> compOdrFinalMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		Map<Integer, List<ScWebexServiceCommercial>> scWebexCommercialMapper = new HashMap<>();
		odrOrderBean.getOdrServiceDetails().stream().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity = null;
			try {
				serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail, odrOrderBean);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(e);
			}
			// Migration Order Changes
			if ("MACD".equals(serviceDetail.getOrderType())) {
				LOGGER.info("OrderCode::{}", odrOrderBean.getOpOrderCode());
				try {
					if (Objects.nonNull(serviceDetail.getUuid())) {
						LOGGER.info("Uuid::{}", serviceDetail.getUuid());
						getServiceDetail(serviceDetail.getUuid(), odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
					} else if (Objects.nonNull(serviceDetail.getParentServiceUuid())) {
						LOGGER.info("Parent Uuid::{}", serviceDetail.getParentServiceUuid());
						getServiceDetail(serviceDetail.getParentServiceUuid(), odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
					}
				}catch(Exception e) {
					LOGGER.error("Error on fetching the inventory details ",e);
				}

			}
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			serviceEntity.setServiceConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setActivationConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setBillingStatus(TaskStatusConstants.PENDING);
			serviceEntity.setServiceAceptanceStatus(TaskStatusConstants.PENDING);
			serviceEntity.setAssuranceCompletionStatus(TaskStatusConstants.PENDING);
			serviceEntity.setIsDelivered(TaskStatusConstants.PENDING);
			scOrderRepository.save(scOrderEntity);
			scContractInfoRepository.saveAll(scContractingInfo);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag && serviceDetail.getTaxExemptionFlag() != null
					&& serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag() != null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}

			ScOrderAttribute orderAttribute = scOrderAttrs.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("PO_NUMBER")).findFirst().orElse(null);

			if ((serviceDetail.getOdrContractInfo() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))
					|| (orderAttribute != null && orderAttribute.getAttributeValue() != null))
				scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY, PO_ATTACHMENT_CATEGORY));
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceParentIdMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity);
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components = serviceDetail.getOdrComponentBeans();
			LOGGER.info("OdrComponentBeans : {}", components);
			if (components != null) {
				List<ScComponent> scComponentList = new ArrayList<>();
				for (OdrComponentBean scComponentBean : components) {
					LOGGER.info("Into Component Bean");
					ScComponent scComponent = new ScComponent();
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					LOGGER.info("odrComponentAttributeBeans for componentBean {} : {}", scComponentBean.getId(),
							scComponentBean.getOdrComponentAttributeBeans());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						LOGGER.info("Into Component Attributes");
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						if (scComponentAttribute.getAttributeName().equals("siteGstAddress")
								&& scComponentAttribute.getAttributeValue() != null) {
							LOGGER.info("Constructing Site Gst address for Webex{} ",scComponentAttribute.getAttributeValue());
							try {
								GstAddressBean gstAddress = Utils.convertJsonToObject(
										scComponentAttribute.getAttributeValue(), GstAddressBean.class);
								if (gstAddress != null) {
									ScGstAddress scGstAddress = new ScGstAddress();
									scGstAddress.setBuildingName(gstAddress.getBuildingName());
									scGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
									scGstAddress.setDistrict(gstAddress.getDistrict());
									scGstAddress.setFlatNumber(gstAddress.getFlatNumber());
									scGstAddress.setLatitude(gstAddress.getLatitude());
									scGstAddress.setLocality(gstAddress.getLocality());
									scGstAddress.setLongitude(gstAddress.getLongitude());
									scGstAddress.setPincode(gstAddress.getPinCode());
									scGstAddress.setState(gstAddress.getState()!=null?gstAddress.getState().toUpperCase():gstAddress.getState());
									scGstAddress.setStreet(gstAddress.getStreet());
									scGstAddress = scGstAddressRepository.save(scGstAddress);
									
									scComponentAttribute.setAttributeName("siteGstAddressId");
									scComponentAttribute.setAttributeAltValueLabel(scGstAddress.getId().toString());
									scComponentAttribute.setAttributeValue(scGstAddress.getId().toString());
									LOGGER.info("Site Gst Address id is {}", scComponentAttribute.getAttributeValue());
									
								}
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					LOGGER.info("ErfOdrServiceId for scServiceDetail : {}", serviceEntity.getErfOdrServiceId());
					LOGGER.info("Adding Component");
					scComponentList.add(scComponent);
					compOdrMapper.put(scComponentBean.getId(), scComponent.getUuid());
				}
				compMapper.put(serviceEntity.getErfOdrServiceId(), scComponentList);
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			/*scServiceDetails.add(serviceEntity);*/

			scServiceDetails.add(serviceEntity);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.save(serviceEntity);
			List<ScAsset> scAssetList=new ArrayList<>();
			for (ScAsset asset : serviceEntity.getScAssets()) {
				asset.setScServiceDetail(scServiceDetail);
				scAssetList.add(asset);
			}
			LOGGER.info("ScAssets data: "+scAssetList.toString());
			scAssetRepository.saveAll(scAssetList);
			LOGGER.info("ScAssets are stored");

		});

		scOrderAttributeRepository.saveAll(scOrderAttrs);
		/*scServiceDetailRepository.saveAll(scServiceDetails);*/
		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			// String scServiceId =
			// serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial = serviceFulfillmentMapper
					.mapOdrCommercialToServiceCommercial(odrCommercial);
			if (scCommercialMapper.get(odrCommercial.getServiceId()) == null) {
				List<ScServiceCommercial> scServiceCommercials = new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
			} else {
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scCommercial);
			}

		}

		for (OdrWebexCommercialBean odrWebexCommercial : odrOrderBean.getOdrWebexCommercialBean()) {
			ScWebexServiceCommercial scWebexServiceCommercial = serviceFulfillmentMapper
					.mapOdrCommercialToWebexCommercial(odrWebexCommercial);
			if (scWebexCommercialMapper.get(odrWebexCommercial.getServiceId()) == null) {
				List<ScWebexServiceCommercial> scServiceCommercials = new ArrayList<>();
				scServiceCommercials.add(scWebexServiceCommercial);
				scWebexCommercialMapper.put(odrWebexCommercial.getServiceId(), scServiceCommercials);
			} else {
				scWebexCommercialMapper.get(odrWebexCommercial.getServiceId()).add(scWebexServiceCommercial);
			}
		}

		Map<Integer, String> serviceIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
		}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				LOGGER.info("ScServiceDetail erfOdrServiceId : {}", scServiceDetail.getErfOdrServiceId());
				LOGGER.info("Getting ComMapper");
				List<ScComponent> scCompos = compMapper.get(scServiceDetail.getErfOdrServiceId());
				for(ScComponent scCompo : scCompos) {
					LOGGER.info("ScServiceDetail Id : {}", scServiceDetail.getId());
					scCompo.setScServiceDetailId(scServiceDetail.getId());
					scCompo.setUuid(scServiceDetail.getUuid());
					for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
						LOGGER.info("ScComponentAttribute : {}, Key : {}, Value : {}", scCmpAttr.getId(),
								scCmpAttr.getAttributeName(), scCmpAttr.getAttributeValue());
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					LOGGER.info("Saving Component");
					scComponentRepository.saveAndFlush(scCompo);
				}
			}

			if (scCommercialMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScServiceCommercial> scCommercials = scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);

				}
			}

			if (scWebexCommercialMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScWebexServiceCommercial> scCommercials = scWebexCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScWebexServiceCommercial scWebexCommercial : scCommercials) {
					scWebexCommercial.setScServiceDetail(scServiceDetail);
					scWebexServiceCommercialRepository.save(scWebexCommercial);
				}
				Double nrc = scCommercials.stream().collect(Collectors.summingDouble(detail -> detail.getNrc()));
				Double arc = scCommercials.stream().collect(Collectors.summingDouble(detail -> detail.getArc()));
				if ((nrc != null && nrc > 0) && (arc != null && arc > 0)) {
					scServiceDetail.setMrc(arc / 12);
					scServiceDetail.setArc(arc);
					scServiceDetail.setNrc(nrc);
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
			
			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				scServiceDetailRepository.save(scServiceDetail);
			}
			
			if(scServiceDetail.getParentId() != null) {
				if(serviceParentIdMapper.get(scServiceDetail.getParentId()) != null) {
					scServiceDetail.setParentId(serviceParentIdMapper.get(scServiceDetail.getParentId()).getId());
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
		}
		// MACD Changes
		if ("MACD".equals(odrOrderBean.getOrderType()) && ("ADD_IP".equalsIgnoreCase(odrOrderBean.getOrderCategory())
				|| "CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory()))) {
			LOGGER.info("MACD Changes");
			updateComponentAttrs(odrOrderBean, "BOTH");
		}
		// GVPN MACD Program Name Changes
		odrOrderBean.getOdrServiceDetails().stream().forEach(odrServiceDetailBean -> {
			if (Objects.nonNull(odrServiceDetailBean.getErfPrdCatalogProductName())
					&& "GVPN".equalsIgnoreCase(odrServiceDetailBean.getErfPrdCatalogProductName())
					&& "MACD".equals(odrOrderBean.getOrderType())) {
				LOGGER.info("GVPN MACD Changes");
				if ("CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory())) {
					updateComponentAttrs(odrOrderBean, "GVPN");
				} else if ("ADD_SITE".equalsIgnoreCase(odrOrderBean.getOrderCategory())) {
					LOGGER.info("ADD SITE VPN NAME");
					updateProgramName(odrServiceDetailBean);
				}

				String orderSubCategory = StringUtils.trimToEmpty(odrServiceDetailBean.getOrderSubCategory());
				// GVPN MACD COS criteria missing attribute saving
				LOGGER.info("orderSubCategory=>{}, orderID:{}", orderSubCategory, odrOrderBean.getUuid());
				if (!"ADD_SITE".equals(odrOrderBean.getOrderCategory())
						&& (!orderSubCategory.toLowerCase().contains("parallel"))) {
					LOGGER.info("MACD Cos Details");
					updateCosCriteriaMissingAttribute(odrServiceDetailBean, odrServiceDetailBean.getUuid(),
							odrServiceDetailBean.getUuid());
					// updateCosModel(odrServiceDetailBean.getUuid(),odrServiceDetailBean.getUuid());
				} else if (odrServiceDetailBean.getParentServiceUuid() != null
						&& !odrServiceDetailBean.getParentServiceUuid().isEmpty()) {
					LOGGER.info("MACD ADD SITE or Parallel Cos Details::{}",
							odrServiceDetailBean.getParentServiceUuid());
					updateCosCriteriaMissingAttribute(odrServiceDetailBean,
							serviceIdMapper.get(odrServiceDetailBean.getId()),
							odrServiceDetailBean.getParentServiceUuid());
					// updateCosModel(serviceIdMapper.get(odrServiceDetailBean.getId()),odrServiceDetailBean.getParentServiceUuid());
				}
			}
		});
		LOGGER.info("Order Code:"+odrOrderBean.getOpOrderCode());
		// Update wanIpProvidedByCust
		if (Objects.nonNull(odrOrderBean.getOpOrderCode())) {
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(odrOrderBean.getOpOrderCode(), "Y");
			if (Objects.nonNull(scOrder)) {
				LOGGER.info("ScOrder exists");
				List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findByScOrderId(scOrder.getId());
				if (Objects.nonNull(scServiceDetailList) && !scServiceDetailList.isEmpty()) {
					LOGGER.info("ScServiceDetail exists");
					for (ScServiceDetail scServiceDetail : scServiceDetailList) {
						updateWanIpProvidedByCust(scServiceDetail);
					}
				}
			}
		}
		scComponentRepository.flush();
		if (compOdrMapper != null && !compOdrMapper.isEmpty()) {
			LOGGER.info("Got mappings in compOdrMapper");
			compOdrMapper.forEach((k, v) -> {
				try {
					LOGGER.info("Key is {} and value is {}", k, v!=null?v:CommonConstants.NO);
				}catch(Exception e) {
					e.printStackTrace();
				}
			});
		}
		if (Objects.nonNull(odrOrderBean.getSolutionComponentBeans())) {
			List<ScSolutionComponent> scSolutionComponents = new ArrayList<>();
			odrOrderBean.getSolutionComponentBeans().stream().forEach(solComp -> {
				
				ScSolutionComponent scSolutionComponent = new ScSolutionComponent();
				scSolutionComponent.setComponentGroup(solComp.getComponentGroup());
				if (solComp.getCpeComponentId() != null && compOdrFinalMapper.containsKey(solComp.getCpeComponentId())
						&& Objects.nonNull(compOdrFinalMapper.get(solComp.getCpeComponentId()))
						&& compOdrFinalMapper.get(solComp.getCpeComponentId()) != null) {
					LOGGER.info("Got component id as {} mapping information for {}",
							compOdrFinalMapper.get(solComp.getCpeComponentId()), solComp.getCpeComponentId());
					scSolutionComponent.setCpeComponentId(compOdrFinalMapper.get(solComp.getCpeComponentId()));
				}
				scSolutionComponent.setIsActive(CommonConstants.Y);
				scSolutionComponent.setOrderCode(scOrderEntity.getUuid());
				scSolutionComponent.setScOrder(scOrderEntity);
				if (solComp.getParentServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getParentServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						LOGGER.info("Processing for service id {} component type {} cpe component id {}",
								scServiceDetail.getUuid(), scSolutionComponent.getComponentGroup(),
								scSolutionComponent.getCpeComponentId());
						scSolutionComponent.setScServiceDetail2(scServiceDetail);
						scSolutionComponent.setParentServiceCode(scServiceDetail.getUuid());
					}
				}
				if (solComp.getServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						scSolutionComponent.setScServiceDetail1(scServiceDetail);
						scSolutionComponent.setServiceCode(scServiceDetail.getUuid());
					}
				}
				if (solComp.getSolutionServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getSolutionServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						scSolutionComponent.setScServiceDetail3(scServiceDetail);
						scSolutionComponent.setSolutionCode(scServiceDetail.getUuid());
					}
				}
				if (scSolutionComponent.getScServiceDetail1() != null) {
					scSolutionComponents.add(scSolutionComponent);
				}
			});
			if (Objects.nonNull(scSolutionComponents)) {
				scSolutionComponentRepository.saveAll(scSolutionComponents);
			}
		}
		return scServiceDetails;
	}

	/**
	 * To trigger TeamsDR Workflow
	 *
	 * @param scServiceDetails
	 */
	public void triggerTeamsDRWorkflow(Set<ScServiceDetail> scServiceDetails) {
		LOGGER.info("triggerTeamsDRWorkflow");
		List<ScServiceDetail> teamsdrServiceDetails = new ArrayList<>();
		List<ScServiceDetail> mediaGatewayIndiaDetails = new ArrayList<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (scServiceDetail.getErfPrdCatalogProductName()
					.equalsIgnoreCase(TeamsDROdrConstants.TEAMSDR_SOLUTION)) {
				teamsdrServiceDetails.add(scServiceDetail);
			}
		}
		if (!teamsdrServiceDetails.isEmpty()) {
			for (ScServiceDetail teamsdrServiceDetail : teamsdrServiceDetails) {
				LOGGER.info("work flow triggered for the teamsdr service id:{},order code:{},order:{}",
						teamsdrServiceDetail.getId(), teamsdrServiceDetail.getScOrderUuid(),
						teamsdrServiceDetail.getScOrder().getOpOrderCode());
				processL2OService.processTeamsDRL2ODataToFlowable(teamsdrServiceDetail);
			}
		}
	}

	public void triggerWebExWorkflow(Set<ScServiceDetail> scServiceDetails) {
		LOGGER.info("scServiceDetails:"+scServiceDetails);
		List<ScServiceDetail> gvpnServiceDetails= new ArrayList<>();
		List<ScServiceDetail> gscServiceDetails=new ArrayList<>();
		List<ScServiceDetail> ucaasServiceDetails=new ArrayList<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("UCAAS")) {
				ucaasServiceDetails.add(scServiceDetail);
			} else if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GSIP") && scServiceDetail.getParentId() == null) {
				gscServiceDetails.add(scServiceDetail);
			}
			else if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
				gvpnServiceDetails.add(scServiceDetail);
			}
		}
		boolean status=false;
		LOGGER.info("GVPN scServiceDetails:"+gvpnServiceDetails);
		LOGGER.info("GSC scServiceDetails:"+gscServiceDetails);
		LOGGER.info("UCAAS scServiceDetails:"+ucaasServiceDetails);
		if(!gvpnServiceDetails.isEmpty()) {
			for (ScServiceDetail scServiceDetail : gvpnServiceDetails) {

				status=false;
				LOGGER.info("Gvpn work flow to be triggered for the order code: {}", scServiceDetail.getScOrderUuid());
				status = processL2OService.processL2ODataToFlowable(scServiceDetail.getId(), scServiceDetail,true);
				LOGGER.info("Status of GVPN trigger workflow:"+status);
				if (!status) {
					addServiceFulfillmentJob(status, scServiceDetail);}
				else {
					if (StringUtils.isNotBlank(scServiceDetail.getLocalItContactName())
							&& (scServiceDetail.getIsOeCompleted() != null
							&& scServiceDetail.getIsOeCompleted().equals(CommonConstants.Y))) {
						initiateBasicEnrichment(scServiceDetail.getId(), scServiceDetail.getErfOdrServiceId(),
								scServiceDetail);
					}
				}
				break;

			}
		}
		
		if((!gvpnServiceDetails.isEmpty()&&status)|| gvpnServiceDetails.isEmpty() && !gscServiceDetails.isEmpty()) {
			for (ScServiceDetail scServiceDetail : gscServiceDetails) {
				status=false;
				
				status = processL2OService.processGSCL2ODataToFlowable(scServiceDetail.getId(),scServiceDetail);
				LOGGER.info("Status of gsc trigger workflow:"+status);
				if (!status) {
					addServiceFulfillmentJob(status, scServiceDetail);
					break;
				}
			}
		}

		if((!gvpnServiceDetails.isEmpty()&&status)|| gvpnServiceDetails.isEmpty() && !ucaasServiceDetails.isEmpty()) {
			for (ScServiceDetail scServiceDetail : ucaasServiceDetails) {
				status=false;
				
				status = processL2OService.processWebExL2ODataToFlowable(scServiceDetail.getId(),scServiceDetail);
				LOGGER.info("Status of webex trigger workflow:"+status);
				if (!status) {
					addServiceFulfillmentJob(status, scServiceDetail);
					break;
				}
			}
		}
		/*for (ScServiceDetail scServiceDetail : scServiceDetails) {
			Boolean status = false;
			if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("UCAAS")) {
				LOGGER.info("WebEx work flow to be triggered for the order code: {}", scServiceDetail.getScOrderUuid());
				ServiceFulfillmentRequest svcFulfillment = serviceCatalogueService
						.processWebExServiceFulFillmentData(scServiceDetail.getId());
				LOGGER.info("WebEx work flow triggered for the order code: {}", scServiceDetail.getScOrderUuid());

				status = processL2OService.processWebExL2ODataToFlowable(svcFulfillment);
			} else {
				status = processL2OService.processL2ODataToFlowable(scServiceDetail.getId(), scServiceDetail);
			}
			if (!status) {
				ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
				srviceFulfillmentJob.setCreatedBy("system");
				srviceFulfillmentJob.setCreatedTime(new Date());
				srviceFulfillmentJob.setServiceId(scServiceDetail.getId());
				srviceFulfillmentJob.setType("NEW_SERVICE");
				srviceFulfillmentJob.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
				// srviceFulfillmentJob.setServiceUuid(scServiceDetail.getUuid());
				srviceFulfillmentJob.setRetryCount(0);
				srviceFulfillmentJob.setStatus("NEW");
				serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
			} else {
				*//*
		 * if (StringUtils.isNotBlank(scServiceDetail.getLocalItContactName()) &&
		 * (scServiceDetail.getIsOeCompleted() != null &&
		 * scServiceDetail.getIsOeCompleted().equals(CommonConstants.Y))) {
		 * initiateBasicEnrichment(scServiceDetail.getId(),
		 * scServiceDetail.getErfOdrServiceId(), scServiceDetail); }
		 *//*
			}
		}*/
	}
	
	public void addServiceFulfillmentJob(boolean status,ScServiceDetail scServiceDetail)
	{
		if (!status) {
			ServiceFulfillmentJob srviceFulfillmentJob = new ServiceFulfillmentJob();
			srviceFulfillmentJob.setCreatedBy("system");
			srviceFulfillmentJob.setCreatedTime(new Date());
			srviceFulfillmentJob.setServiceId(scServiceDetail.getId());
			srviceFulfillmentJob.setType("NEW_SERVICE");
			srviceFulfillmentJob.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
			// srviceFulfillmentJob.setServiceUuid(scServiceDetail.getUuid());
			srviceFulfillmentJob.setRetryCount(0);
			srviceFulfillmentJob.setStatus("NEW");
			serviceFulfillmentJobRepository.save(srviceFulfillmentJob);
		} else {
			/*
			 * if (StringUtils.isNotBlank(scServiceDetail.getLocalItContactName()) &&
			 * (scServiceDetail.getIsOeCompleted() != null &&
			 * scServiceDetail.getIsOeCompleted().equals(CommonConstants.Y))) {
			 * initiateBasicEnrichment(scServiceDetail.getId(),
			 * scServiceDetail.getErfOdrServiceId(), scServiceDetail); }
			 */
		}
	}
	
	public String processFulfillment(OdrOrderBean request) {
		try {
			Set<ScServiceDetail> scServiceDetails = new HashSet<>();
			if (request.getOpOrderCode().startsWith("NPL")) {
				scServiceDetails = processfulfillmentDateForNpl(request);
			} else if(request.getOpOrderCode().startsWith("UCWB")) {
				scServiceDetails = processfulfillmentDateWebEx(request);
				triggerWebExWorkflow(scServiceDetails);
				return "success";
			} else if(request.getOpOrderCode().startsWith(IPCServiceFulfillmentConstant.IPC)) {
				scServiceDetails = processfulfillmentDateForIpc(request);
			} else {
				LOGGER.info("Fulfillment Product Name-"+request.getOpOrderCode());
				scServiceDetails = processfulfillmentDate(request);
			}

			triggerWorkflow(scServiceDetails);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
		}
		return "success";
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public Set<ScServiceDetail> processfulfillmentDateIzosdwan(OdrOrderBean odrOrderBean) throws Exception {
		LOGGER.info("processfulfillmentDateIzosdwan invoked for ::{}", odrOrderBean.getOpOrderCode());
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		scOrderEntity.setIsBundleOrder(CommonConstants.Y);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		if (odrOrderBean.getOdrContractInfos() != null && !odrOrderBean.getOdrContractInfos().isEmpty()) {
			odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
				ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
				contrEntity.setScOrder(scOrderEntity);
				scContractingInfo.add(contrEntity);
			});
		}
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<String, Integer> compOdrMapper = new HashMap<>();
		Map<Integer, Integer> compOdrFinalMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercialOdrMapper>> scCommercialMapper = new HashMap<>();
		Integer componentCount = 1;
		List<String> internationalServiceIdEmptyList= new ArrayList<>();
		for(OdrServiceDetailBean serviceDetail : odrOrderBean.getOdrServiceDetails()) {
			ScServiceDetail serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail,
					odrOrderBean);
			if(serviceEntity.getUuid()==null || serviceEntity.getUuid().isEmpty()){
				LOGGER.info("Uuid not generated for orderCode::{} with upstream Id::{}", odrOrderBean.getOpOrderCode(),serviceEntity.getErfOdrServiceId());
				internationalServiceIdEmptyList.add(String.valueOf(serviceEntity.getErfOdrServiceId()));
			}
			// Migration Order Changes
			if ("MACD".equals(serviceDetail.getOrderType())) {
				LOGGER.info("OrderCode::{}", odrOrderBean.getOpOrderCode());
				try {
				if (Objects.nonNull(serviceDetail.getUuid())) {
					LOGGER.info("Uuid::{}", serviceDetail.getUuid());
					getServiceDetail(serviceDetail.getUuid(), odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
					setOptimusServiceIdForInternationalOrders(serviceDetail, odrOrderBean.getOpOrderCode(),serviceEntity);
				} else if (Objects.nonNull(serviceDetail.getParentServiceUuid())) {
					LOGGER.info("Parent Uuid::{}", serviceDetail.getParentServiceUuid());
					getServiceDetail(serviceDetail.getParentServiceUuid(), odrOrderBean.getOpOrderCode(),odrOrderBean.getOrderType());
				}
				}catch(Exception e) {
					LOGGER.error("Error on fetching the inventory details ",e);
				}
				ScServiceDetail prevActiveServiceDetail=null;
				if(Objects.nonNull(serviceDetail.getUuid())){
					LOGGER.info("Link Id for UUID::{}",serviceDetail.getUuid());
					prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceDetail.getUuid(), "ACTIVE");
					if(prevActiveServiceDetail==null) {
						LOGGER.info("Prev Active Service Detail Not Exists Id for International UUID::{}",serviceDetail.getUuid());
						prevActiveServiceDetail=getInternationalActiveOptimusServiceDetail(serviceDetail, odrOrderBean.getOpOrderCode());
					}
				}else if(Objects.nonNull(serviceDetail.getParentServiceUuid())){
					LOGGER.info("Link Id for Parent UUID::{}",serviceDetail.getParentServiceUuid());
					prevActiveServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceDetail.getParentServiceUuid(), "ACTIVE");
				}
				serviceEntity.setServiceLinkId(Objects.nonNull(prevActiveServiceDetail)?prevActiveServiceDetail.getId():null);
			}
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			serviceEntity.setServiceConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setActivationConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setBillingStatus(TaskStatusConstants.PENDING);
			serviceEntity.setServiceAceptanceStatus(TaskStatusConstants.PENDING);
			serviceEntity.setAssuranceCompletionStatus(TaskStatusConstants.PENDING);
			serviceEntity.setIsDelivered(TaskStatusConstants.PENDING);
			scOrderRepository.save(scOrderEntity);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag && serviceDetail.getTaxExemptionFlag() != null
					&& serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			OdrOrderAttributeBean isBillingIntl = odrOrderBean.getOdrOrderAttributes().stream().filter(odrOrdAtt->odrOrdAtt.getAttributeValue()!=null && odrOrdAtt.getAttributeValue().equalsIgnoreCase(IzosdwanCommonConstants.IS_BILLING_INTL)).findFirst().orElse(null);
			if (serviceDetail.getTaxExemptionFlag() != null && serviceDetail.getTaxExemptionFlag().equals("Y")
					&& (isBillingIntl != null && isBillingIntl.getAttributeValue().equals(CommonConstants.N))) {
				LOGGER.info("Is Billing intl value {}",isBillingIntl);
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}

			ScOrderAttribute orderAttribute = scOrderAttrs.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("PO_NUMBER")).findFirst().orElse(null);

			if ((serviceDetail.getOdrContractInfo() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))
					|| (orderAttribute != null && orderAttribute.getAttributeValue() != null))
				scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY, PO_ATTACHMENT_CATEGORY));
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components = serviceDetail.getOdrComponentBeans();
			LOGGER.info("OdrComponentBeans : {}", components);
			if (components != null) {
				List<ScComponent> scComponents = new ArrayList<>();
				for (OdrComponentBean scComponentBean : components) {
					LOGGER.info("Into Component Bean");
					ScComponent scComponent = new ScComponent();
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					LOGGER.info("odrComponentAttributeBeans for componentBean {} : {}", scComponentBean.getId(),
							scComponentBean.getOdrComponentAttributeBeans());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						LOGGER.info("Into Component Attributes");
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						scComponentAttribute.setIsAdditionalParam(odrComponentAttributeBean.getIsAdditionalParam());
						if (odrComponentAttributeBean.getIsAdditionalParam() != null
								&& odrComponentAttributeBean.getIsAdditionalParam().equals(CommonConstants.Y)) {
							LOGGER.info("Got additional service param indicator for attribute {}",
									odrComponentAttributeBean.getName());
							ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
							scAdditionalServiceParam.setAttribute(
									odrComponentAttributeBean.getOdrAdditionalServiceParam().getAttribute());
							scAdditionalServiceParam.setCategory(
									odrComponentAttributeBean.getOdrAdditionalServiceParam().getCategory());
							scAdditionalServiceParam.setCreatedBy(
									odrComponentAttributeBean.getOdrAdditionalServiceParam().getCreatedBy());
							scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
							scAdditionalServiceParam.setIsActive(CommonConstants.Y);
							scAdditionalServiceParam.setReferenceType(
									odrComponentAttributeBean.getOdrAdditionalServiceParam().getReferenceType());
							scAdditionalServiceParam.setReferenceId(
									odrComponentAttributeBean.getOdrAdditionalServiceParam().getReferenceId());
							scAdditionalServiceParam
									.setValue(odrComponentAttributeBean.getOdrAdditionalServiceParam().getValue());
							scAdditionalServiceParamRepo.save(scAdditionalServiceParam);
							scComponentAttribute
									.setAttributeValue(scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
							LOGGER.info("additional service param id for attribute {} is {}",
									odrComponentAttributeBean.getName(),
									scAdditionalServiceParam.getId() + CommonConstants.EMPTY);
						}
						if (scComponentAttribute.getAttributeName().equals("siteGstAddress")
								&& scComponentAttribute.getAttributeValue() != null) {
							LOGGER.info("Constructing Gst address IZOSDWAN{} ",scComponentAttribute.getAttributeValue());
							try {
								GstAddressBean gstAddress = Utils.convertJsonToObject(
										scComponentAttribute.getAttributeValue(), GstAddressBean.class);
								if (gstAddress != null) {
									ScGstAddress scGstAddress = new ScGstAddress();
									scGstAddress.setBuildingName(gstAddress.getBuildingName());
									scGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
									scGstAddress.setDistrict(gstAddress.getDistrict());
									scGstAddress.setFlatNumber(gstAddress.getFlatNumber());
									scGstAddress.setLatitude(gstAddress.getLatitude());
									scGstAddress.setLocality(gstAddress.getLocality());
									scGstAddress.setLongitude(gstAddress.getLongitude());
									scGstAddress.setPincode(gstAddress.getPinCode());
									scGstAddress.setState(gstAddress.getState());
									scGstAddress.setStreet(gstAddress.getStreet());
									scGstAddress = scGstAddressRepository.save(scGstAddress);
									
									scComponentAttribute.setAttributeName("siteGstAddressId");
									scComponentAttribute.setAttributeAltValueLabel(scGstAddress.getId().toString());
									scComponentAttribute.setAttributeValue(scGstAddress.getId().toString());
									LOGGER.info("Site Gst Address id is {}", scComponentAttribute.getAttributeValue());
									
								}
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					LOGGER.info("ErfOdrServiceId for scServiceDetail : {}", serviceEntity.getErfOdrServiceId());
					LOGGER.info("Adding Component");
					scComponents.add(scComponent);
					
					compOdrMapper.put(scComponent.getUuid(),scComponentBean.getId());
					componentCount++;
				}
				compMapper.put(serviceEntity.getErfOdrServiceId(), scComponents);
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			scServiceDetails.add(serviceEntity);

		}
		LOGGER.info("Component count : {}",componentCount);
		scContractInfoRepository.saveAll(scContractingInfo);
		scOrderAttributeRepository.saveAll(scOrderAttrs);
		scServiceDetailRepository.saveAll(scServiceDetails);
		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			// String scServiceId =
			// serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial = serviceFulfillmentMapper
					.mapOdrCommercialToServiceCommercial(odrCommercial);
			if (scCommercialMapper.get(odrCommercial.getServiceId()) == null) {
				List<ScServiceCommercialOdrMapper> scServiceCommercials = new ArrayList<>();
				
				ScServiceCommercialOdrMapper scServiceCommercialOdrMapper = new ScServiceCommercialOdrMapper();
				scServiceCommercialOdrMapper.setOdrCommercialBean(odrCommercial);
				scServiceCommercialOdrMapper.setScServiceCommercial(scCommercial);
				scServiceCommercials.add(scServiceCommercialOdrMapper);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
				
			} else {
				ScServiceCommercialOdrMapper scServiceCommercialOdrMapper = new ScServiceCommercialOdrMapper();
				scServiceCommercialOdrMapper.setOdrCommercialBean(odrCommercial);
				scServiceCommercialOdrMapper.setScServiceCommercial(scCommercial);
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scServiceCommercialOdrMapper);
			}

		}

		Map<Integer, String> serviceIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
		}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				LOGGER.info("ScServiceDetail erfOdrServiceId : {}", scServiceDetail.getErfOdrServiceId());
				LOGGER.info("Getting ComMapper");
				List<ScComponent> scCompos = compMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScComponent scCompo : scCompos) {
					String existingUUID = scCompo.getUuid();

					LOGGER.info("ScServiceDetail Id : {}", scServiceDetail.getId());
					scCompo.setScServiceDetailId(scServiceDetail.getId());
					scCompo.setUuid(scServiceDetail.getUuid());
					for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
						LOGGER.info("ScComponentAttribute : {}, Key : {}, Value : {}", scCmpAttr.getId(),
								scCmpAttr.getAttributeName(), scCmpAttr.getAttributeValue());
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					LOGGER.info("Saving Component");
					scCompo = scComponentRepository.saveAndFlush(scCompo);
					if (compOdrMapper.containsKey(existingUUID) && scCompo != null && scCompo.getId() != null) {
						LOGGER.info("Pushing primary key for the component id to map {} {} for {}", existingUUID,
								scCompo.getId(), compOdrMapper.get(existingUUID));
						compOdrFinalMapper.put(compOdrMapper.get(existingUUID), scCompo.getId());
					}
				}
			}

			if (scCommercialMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScServiceCommercialOdrMapper> scCommercials = scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercialOdrMapper scServiceCommercialOdrMapper : scCommercials) {
					ScServiceCommercial scCommercial = scServiceCommercialOdrMapper.getScServiceCommercial();
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					if(compOdrFinalMapper.containsKey(scServiceCommercialOdrMapper.getOdrCommercialBean().getComponentId())) {
						scCommercial.setComponentId(compOdrFinalMapper.get(scServiceCommercialOdrMapper.getOdrCommercialBean().getComponentId()));
					}
					scServiceCommercialRepository.save(scCommercial);

				}
			}

			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				scServiceDetailRepository.save(scServiceDetail);
			}
		}
		// MACD Changes
		if ("MACD".equals(odrOrderBean.getOrderType()) && ("ADD_IP".equalsIgnoreCase(odrOrderBean.getOrderCategory())
				|| "CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory())
				|| (odrOrderBean.getOpOrderCode().toLowerCase().contains("izosdwan")
						&& "CHANGE_ORDER".equalsIgnoreCase(odrOrderBean.getOrderCategory())))) {
			LOGGER.info("MACD Changes");
			updateComponentAttrs(odrOrderBean, "BOTH");
		}
		// GVPN MACD Program Name Changes
		odrOrderBean.getOdrServiceDetails().stream().forEach(odrServiceDetailBean -> {
			if (Objects.nonNull(odrServiceDetailBean.getErfPrdCatalogProductName())
					&& "GVPN".equalsIgnoreCase(odrServiceDetailBean.getErfPrdCatalogProductName())
					&& "MACD".equals(odrOrderBean.getOrderType())) {
				LOGGER.info("GVPN MACD Changes");
				if ("CHANGE_BANDWIDTH".equalsIgnoreCase(odrOrderBean.getOrderCategory())
						|| (odrOrderBean.getOpOrderCode().toLowerCase().contains("izosdwan")
								&& "CHANGE_ORDER".equalsIgnoreCase(odrOrderBean.getOrderCategory()))) {
					updateComponentAttrs(odrOrderBean, "GVPN");
				} else if ("ADD_SITE".equalsIgnoreCase(odrOrderBean.getOrderCategory())) {
					LOGGER.info("ADD SITE VPN NAME");
					updateProgramName(odrServiceDetailBean);
				}

				String orderSubCategory = StringUtils.trimToEmpty(odrServiceDetailBean.getOrderSubCategory());
				// GVPN MACD COS criteria missing attribute saving
				LOGGER.info("orderSubCategory=>{}, orderID:{}", orderSubCategory, odrOrderBean.getUuid());
				if (!"ADD_SITE".equals(odrOrderBean.getOrderCategory())
						&& (!orderSubCategory.toLowerCase().contains("parallel"))) {
					LOGGER.info("MACD Cos Details");
					updateCosCriteriaMissingAttribute(odrServiceDetailBean, odrServiceDetailBean.getUuid(),
							odrServiceDetailBean.getUuid());
					// updateCosModel(odrServiceDetailBean.getUuid(),odrServiceDetailBean.getUuid());
				} else if (odrServiceDetailBean.getParentServiceUuid() != null
						&& !odrServiceDetailBean.getParentServiceUuid().isEmpty()) {
					LOGGER.info("MACD ADD SITE or Parallel Cos Details::{}",
							odrServiceDetailBean.getParentServiceUuid());
					updateCosCriteriaMissingAttribute(odrServiceDetailBean,
							serviceIdMapper.get(odrServiceDetailBean.getId()),
							odrServiceDetailBean.getParentServiceUuid());
					// updateCosModel(serviceIdMapper.get(odrServiceDetailBean.getId()),odrServiceDetailBean.getParentServiceUuid());
				}
			}
		});

		// Update wanIpProvidedByCust
		if (Objects.nonNull(odrOrderBean.getOpOrderCode())) {
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(odrOrderBean.getOpOrderCode(), "Y");
			if (Objects.nonNull(scOrder)) {
				LOGGER.info("ScOrder exists");
				List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findByScOrderId(scOrder.getId());
				if (Objects.nonNull(scServiceDetailList) && !scServiceDetailList.isEmpty()) {
					LOGGER.info("ScServiceDetail exists");
					for (ScServiceDetail scServiceDetail : scServiceDetailList) {
						updateWanIpProvidedByCust(scServiceDetail);
					}
				}
			}
		}
		scComponentRepository.flush();
		LOGGER.info("Size of cpeComp --> {} and final map {} ", compOdrMapper.size(),
				compOdrFinalMapper.size());
		if (Objects.nonNull(odrOrderBean.getSolutionComponentBeans())) {
			List<ScSolutionComponent> scSolutionComponents = new ArrayList<>();
			odrOrderBean.getSolutionComponentBeans().stream().forEach(solComp -> {
				
				ScSolutionComponent scSolutionComponent = new ScSolutionComponent();
				scSolutionComponent.setComponentGroup(solComp.getComponentGroup());
				if (solComp.getCpeComponentId() != null && compOdrFinalMapper.containsKey(solComp.getCpeComponentId())
						&& Objects.nonNull(compOdrFinalMapper.get(solComp.getCpeComponentId()))
						&& compOdrFinalMapper.get(solComp.getCpeComponentId()) != null) {
					LOGGER.info("Got component id as {} mapping information for {}",
							compOdrFinalMapper.get(solComp.getCpeComponentId()), solComp.getCpeComponentId());
					scSolutionComponent.setCpeComponentId(compOdrFinalMapper.get(solComp.getCpeComponentId()));
				}
				scSolutionComponent.setIsActive(CommonConstants.Y);
				scSolutionComponent.setOrderCode(scOrderEntity.getUuid());
				scSolutionComponent.setScOrder(scOrderEntity);
				if (solComp.getParentServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getParentServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						LOGGER.info("Processing for service id {} component type {} cpe component id {}",
								scServiceDetail.getUuid(), scSolutionComponent.getComponentGroup(),
								scSolutionComponent.getCpeComponentId());
						scSolutionComponent.setScServiceDetail2(scServiceDetail);
						scSolutionComponent.setParentServiceCode(scServiceDetail.getUuid());
					}
				}
				if (solComp.getServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						scSolutionComponent.setScServiceDetail1(scServiceDetail);
						scSolutionComponent.setServiceCode(scServiceDetail.getUuid());
					}
				}
				if (solComp.getSolutionServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getSolutionServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						scSolutionComponent.setScServiceDetail3(scServiceDetail);
						scSolutionComponent.setSolutionCode(scServiceDetail.getUuid());
					}
				}
				if (scSolutionComponent.getScServiceDetail1() != null) {
					scSolutionComponents.add(scSolutionComponent);
				}
			});
			if (Objects.nonNull(scSolutionComponents)) {
				scSolutionComponentRepository.saveAll(scSolutionComponents);
				scSolutionComponentRepository.flush();
			}
		}
		generateCpeDeviceNameForAllOverlays(scOrderEntity);
		persistSharedServiceIdAttributes(scOrderEntity);
		if(!internationalServiceIdEmptyList.isEmpty()){
				LOGGER.info("ISD Code not exists for izosdwan::{} for upstream Ids::{}",odrOrderBean.getOpOrderCode(),internationalServiceIdEmptyList.size());
				List<String> tempToMailList= new ArrayList<>(Arrays.asList("OPTIMUS-O2C-SUPPORT@tatacommunications.onmicrosoft.com"));
				notificationService.notifyInternationalServiceIdTrigger(null,tempToMailList,odrOrderBean.getOpOrderCode(),String.join(",", internationalServiceIdEmptyList));
		}
		return scServiceDetails;
		
	}

	/**
	 * 
	 * Generate CPE Device Name for IZOSDWAN Orders
	 * @param scOrder
	 */
	public void generateCpeDeviceNameForAllOverlays(ScOrder scOrder) {
		LOGGER.info("Generating cpe device name for all the CPE for Order {} with id {}", scOrder.getUuid(),
				scOrder.getId());
		try {
			List<ScSolutionComponent> scSolutionComponents = scSolutionComponentRepository
					.findAllByOrderCodeAndComponentGroupAndIsActive(scOrder.getUuid(), "OVERLAY", CommonConstants.Y);
			List<ScOrderAttribute> scOrderAttributes = scOrderAttributeRepository
					.findByScOrder_IdAndIsActive(scOrder.getId(), CommonConstants.Y);
			scSolutionComponents.stream().forEach(scSolComp -> {
				List<ScComponent> scComponents = scComponentRepository
						.findByScServiceDetailIdOrderByIdDesc(scSolComp.getScServiceDetail1().getId());
				Integer cpeIdentifier = CommonConstants.ACTIVE;
				for (ScComponent scComponent : scComponents) {
					List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
							.findByScComponent(scComponent);
					generateDeviceNameForCpe(scComponentAttributes, scComponent, scOrderAttributes, scOrder,
							cpeIdentifier, scSolComp.getScServiceDetail1());
					cpeIdentifier++;
				}
			});
		} catch (Exception e) {
			LOGGER.error("Error on Generating the CPE Device name", e);
		}
		LOGGER.info("Completed generateCpeDeviceNameForAllOverlays");
	}
	
	private void generateDeviceNameForCpe(List<ScComponentAttribute> scComponentAttributes, ScComponent scComponent,
			List<ScOrderAttribute> scOrderAttributes, ScOrder scOrder, Integer cpeIdentifier,
			ScServiceDetail scServiceDetail) {
		LOGGER.info("Generating CPE Device name to sc comp attr for sc comp {}", scComponent.getId());
		try {
			String trigram = scOrderAttributes.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase(IzosdwanCommonConstants.CUSTOMER_TRIGRAM))
					.findFirst().map(attr -> attr.getAttributeValue()).orElse(CommonConstants.EMPTY);
			String countryCode = scComponentAttributes.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("destinationCountryCode")).findFirst()
					.map(attr -> attr.getAttributeValue()).orElse(CommonConstants.EMPTY);
			String city = scComponentAttributes.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("destinationCity")).findFirst()
					.map(attr -> attr.getAttributeValue()).orElse(CommonConstants.EMPTY);
			city = city.replaceAll(" ", "").toUpperCase();
			if (city.length() >= 13) {
				city = city.substring(0, 13);
			}
			Integer siteIdentifier = CommonConstants.ACTIVE;
			CpeDeviceNameDetail cpeDeviceNameDetailOld = cpeDeviceNameDetailRepository
					.findFirstByCountryCodeAndCityAndErfCustCustomerIdOrderByIdDesc(countryCode, city,
							scOrder.getErfCustCustomerId());
			if (cpeDeviceNameDetailOld != null) {
				siteIdentifier = cpeDeviceNameDetailOld.getSiteIdentifier() + 1;
			}
			String siteIden = siteIdentifier.toString();
			if (siteIden.length() == 1) {
				siteIden = "00".concat(siteIden);
			}
			if (siteIden.length() == 2) {
				siteIden = "0".concat(siteIden);
			}
			String cpeIdent = cpeIdentifier.toString();
			if (cpeIdent.length() == 1) {
				cpeIdent = "0".concat(cpeIdent);
			}
			LOGGER.info(
					"Trigram --> {} , country code --> {}, city --> {} ,site Identifier --> {}, cpe identifier -->{}",
					trigram, countryCode, city, siteIden, cpeIdent);
			if (StringUtils.isNotEmpty(trigram) && StringUtils.isNotEmpty(countryCode) && StringUtils.isNotEmpty(city)
					&& StringUtils.isNotEmpty(siteIden) && StringUtils.isNotEmpty(cpeIdent)) {
				String cpeDeviceName = trigram.concat("-").concat(countryCode).concat("-").concat(city).concat("-")
						.concat(siteIden).concat("-").concat("R").concat("-").concat(cpeIdent);
				LOGGER.info("CPE Device name for sc comp attr for sc comp {} is {}", scComponent.getId(),
						cpeDeviceName);
				CpeDeviceNameDetail cpeDeviceNameDetail = new CpeDeviceNameDetail();
				cpeDeviceNameDetail.setCity(city);
				cpeDeviceNameDetail.setCountryCode(countryCode);
				cpeDeviceNameDetail.setCpeDeviceName(cpeDeviceName);
				cpeDeviceNameDetail.setErfCustCustomerId(scOrder.getErfCustCustomerId());
				cpeDeviceNameDetail.setScComponent(scComponent);
				cpeDeviceNameDetail.setScServiceDetail(scServiceDetail);
				cpeDeviceNameDetail.setSiteIdentifier(siteIdentifier);
				cpeDeviceNameDetailRepository.saveAndFlush(cpeDeviceNameDetail);

				LOGGER.info("Updating CPE Device name to sc comp attr for sc comp {}", scComponent.getId());

				ScComponentAttribute scComponentAttribute = scComponentAttributes.stream()
						.filter(scCompAttr -> scCompAttr.getAttributeName().equalsIgnoreCase("cpeDeviceName"))
						.findFirst().orElse(null);
				if (scComponentAttribute == null) {
					scComponentAttribute = new ScComponentAttribute();
					scComponentAttribute.setCreatedBy(scComponent.getCreatedBy());
					scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponentAttribute.setUuid(Utils.generateUid());
				}
				scComponentAttribute.setAttributeAltValueLabel(cpeDeviceName);
				scComponentAttribute.setAttributeName("cpeDeviceName");
				scComponentAttribute.setAttributeValue(cpeDeviceName);
				scComponentAttribute.setIsActive(CommonConstants.Y);
				scComponentAttribute.setScComponent(scComponent);
				scComponentAttribute.setIsAdditionalParam(CommonConstants.N);
				scComponent.getScComponentAttributes().add(scComponentAttribute);
				scComponentRepository.saveAndFlush(scComponent);
			}
		} catch (Exception e) {
			LOGGER.error("Error on Generating the CPE Device name", e);
		}
	}
	
	private void persistSharedServiceIdAttributes(ScOrder scOrder) {
		LOGGER.info("Inside persistSharedServiceIdAttributes for {}", scOrder.getUuid());
		try {
			List<ScSolutionComponent> scSolutionComponents = scSolutionComponentRepository
					.findByOrderCodeAndCpeComponentIdIsNotNull(scOrder.getUuid());
			if (scSolutionComponents != null && !scSolutionComponents.isEmpty()) {
				List<ScComponentAttribute> scComponentAttributes = new ArrayList<>();
				Map<Integer, List<String>> compServiceMap = new HashMap<>();
				List<Integer> compIds = scSolutionComponents.stream().filter(scSol -> scSol.getCpeComponentId() != null)
						.map(sc -> sc.getCpeComponentId()).distinct().collect(Collectors.toList());
				if (compIds != null) {
					compIds.stream().forEach(id -> {
						List<String> scServiceDetails = scSolutionComponents.stream()
								.filter(sc -> sc.getCpeComponentId().equals(id)).map(sc -> sc.getServiceCode())
								.distinct().collect(Collectors.toList());
						if (scServiceDetails != null && !scServiceDetails.isEmpty()) {
							compServiceMap.put(id, scServiceDetails);
						}
					});
				}
				if (compServiceMap != null && !compServiceMap.isEmpty()) {
					compServiceMap.forEach((k, v) -> {
						Optional<ScComponent> scComponent = scComponentRepository.findById(k);
						if (scComponent.isPresent() && v != null && !v.isEmpty() && v.size() > 1) {
							ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
							scComponentAttribute.setAttributeAltValueLabel(StringUtils.join(v, ","));
							scComponentAttribute.setAttributeName(IzosdwanCommonConstants.SHARED_SERVICE_IDS);
							scComponentAttribute.setAttributeValue(StringUtils.join(v, ","));
							scComponentAttribute.setCreatedBy(scOrder.getCreatedBy());
							scComponentAttribute.setCreatedDate(scOrder.getCreatedDate());
							scComponentAttribute.setIsActive(CommonConstants.Y);
							scComponentAttribute.setScComponent(scComponent.get());
							scComponentAttribute.setUuid(Utils.generateUid());
							scComponentAttribute.setIsAdditionalParam(CommonConstants.N);
							scComponentAttribute.setScServiceDetailId(scComponent.get().getScServiceDetailId());
							scComponentAttributes.add(scComponentAttribute);
						}

					});
				}
				if (scComponentAttributes != null && !scComponentAttributes.isEmpty()) {
					scComponentAttributesRepository.saveAll(scComponentAttributes);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error on persistSharedServiceIdAttributes", e);
		}
	}

	public void triggerSDWANWorkflow(Set<ScServiceDetail> scServiceDetails) {
		LOGGER.info("triggerSDWANWorkflow");
		ScServiceDetail scServiceDetail=scServiceDetails.stream().findFirst().orElse(null);
		if(scServiceDetail!=null){
			LOGGER.info("work flow triggered for the izosdwan service id:{},order code:{},order:{}",scServiceDetail.getId(),scServiceDetail.getScOrderUuid(),scServiceDetail.getScOrder().getOpOrderCode());
			processL2OService.processSDWANSolutionL2ODataToFlowable(scServiceDetail.getScOrderUuid());
		}
	}
	private void addCpeAttribute(ScComponent prevScComponent, ScServiceDetail currentServiceDetail) {
		Set<String> attributeNames = Stream.of("amcStartDate","cpeAmcStartDate" ,"amcEndDate","cpeAmcEndDate", "cpeInstallationPoNumber", "cpeInstallationPrVendorName","cpeInstallationPrNumber", "cpeInstallationPoVendorName", "cpeSupplyHardwarePoNumber",
				"cpeSupportPoNumber", "cpeSupportPrVendorName", "cpeSupportPoVendorName","cpeSupportPrNumber","cpeLicencePoNumber", "cpeSupplyHardwarePoNumber","cpeSupplyHardwarePrNumber","cpeSupplyHardwarePoVendorName","cpeSupplyHardwarePrVendorName","cpeSerialNumber").collect(Collectors.toCollection(HashSet::new));
		Map<String, String> atMap = new HashMap<>();
		prevScComponent.getScComponentAttributes().stream().forEach(prevScCompAttr ->{
			LOGGER.info("CPE attribute name and value {} {} :",prevScCompAttr.getAttributeName(),prevScCompAttr.getAttributeValue());
			if(attributeNames.contains(prevScCompAttr.getAttributeName()) && prevScCompAttr.getAttributeValue()!=null){
				if("amcStartDate".equalsIgnoreCase(prevScCompAttr.getAttributeName()) || "cpeAmcStartDate".equalsIgnoreCase(prevScCompAttr.getAttributeName())){
					atMap.put("cpeAmcStartDate",prevScCompAttr.getAttributeValue());
				}else if("amcEndDate".equalsIgnoreCase(prevScCompAttr.getAttributeName()) || "cpeAmcEndDate".equalsIgnoreCase(prevScCompAttr.getAttributeName())){
					atMap.put("cpeAmcEndDate",prevScCompAttr.getAttributeValue());
				}/*else if("cpeSupplyHardwareVendorName".equalsIgnoreCase(prevScCompAttr.getAttributeName()) || "cpeSupplyHardwarePoVendorName".equalsIgnoreCase(prevScCompAttr.getAttributeName())){
					atMap.put("cpeSupplyHardwarePoVendorName",prevScCompAttr.getAttributeValue());
				}*/else{
					atMap.put(prevScCompAttr.getAttributeName(),prevScCompAttr.getAttributeValue());
				}
				componentAndAttributeService.updateAttributes(currentServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");
			}
		});
	}

	/**
	 * Method to process contract info.
	 * @param scContractingInfo
	 * @param odrOrderBean
	 * @param scOrderEntity
	 */
	private void processContractInfo(Set<ScContractInfo> scContractingInfo,OdrOrderBean odrOrderBean,ScOrder scOrderEntity){
		// Mapping odrContractInfo to scContractInfo.
		if (Objects.nonNull(odrOrderBean.getOdrContractInfos()) && !odrOrderBean.getOdrContractInfos().isEmpty()) {
			odrOrderBean.getOdrContractInfos().forEach(contractingInfo -> {
				ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
				contrEntity.setScOrder(scOrderEntity);
				scContractingInfo.add(contrEntity);
			});
		}
	}

	/**
	 * Method to process odrAttributes
	 * @param scOrderAttrs
	 * @param odrOrderBean
	 * @param scOrderEntity
	 */
	private void processOdrAttributes(Set<ScOrderAttribute> scOrderAttrs,OdrOrderBean odrOrderBean,ScOrder scOrderEntity){
		// Mapping odrOrderAttributes to scOrderAttribute.
		odrOrderBean.getOdrOrderAttributes().forEach(odrOrderAttributeBean ->  {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributeBean);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		});
	}

	/**
	 * Method to process odr service detail.
	 * @param orderDetail
	 * @param scServiceDetails
	 * @param odrOrderBean
	 * @param scOrderEntity
	 * @param scContractingInfo
	 * @param scOrderAttrs
	 * @param serviceMapper
	 * @param serviceParentIdMapper
	 * @param compOdrMapper
	 * @param compMapper
	 */
	private void processOdrServiceDetail(Map<String, String> orderDetail,Set<ScServiceDetail> scServiceDetails,OdrOrderBean odrOrderBean,
			ScOrder scOrderEntity,Set<ScContractInfo> scContractingInfo,Set<ScOrderAttribute> scOrderAttrs,
			Map<Integer, String> serviceMapper,Map<Integer, ScServiceDetail> serviceParentIdMapper,
			Map<Integer, String> compOdrMapper,Map<Integer, List<ScComponent>> compMapper){
		// Mapping odrServiceDetail to scServiceDetail.
		odrOrderBean.getOdrServiceDetails().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity = null;
			try {
				serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail, odrOrderBean);
				serviceEntity.setLastmileScenario(null);
				serviceEntity.setLastmileConnectionType(null);
				serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(e);
			}

			// Migration Order Changes
			if ("MACD".equals(serviceDetail.getOrderType())) {
				LOGGER.info("OrderCode::{}", odrOrderBean.getOpOrderCode());
				try {
					if (Objects.nonNull(serviceDetail.getUuid())) {
						LOGGER.info("Uuid::{}", serviceDetail.getUuid());
						getServiceDetail(serviceDetail.getUuid(), odrOrderBean.getOpOrderCode(),serviceDetail.getOrderType());
					} else if (Objects.nonNull(serviceDetail.getParentServiceUuid())) {
						LOGGER.info("Parent Uuid::{}", serviceDetail.getParentServiceUuid());
						getServiceDetail(serviceDetail.getParentServiceUuid(), odrOrderBean.getOpOrderCode(),serviceDetail.getOrderType());
					}
				}catch(Exception e) {
					LOGGER.error("Error on fetching the inventory details ",e);
				}

			}

			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			scOrderRepository.save(scOrderEntity);
			scContractInfoRepository.saveAll(scContractingInfo);


			Set<ScAttachment> scAttachments = new HashSet<>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase(TAX_ATTACHMENT_CATEGORY)) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag && serviceDetail.getTaxExemptionFlag() != null
					&& serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag() != null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}

			ScOrderAttribute orderAttribute = scOrderAttrs.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("PO_NUMBER")).findFirst().orElse(null);

			if ((serviceDetail.getOdrContractInfo() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null
					&& serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))
					|| (orderAttribute != null && orderAttribute.getAttributeValue() != null))
				scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY, PO_ATTACHMENT_CATEGORY));
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}

			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceParentIdMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity);
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components = serviceDetail.getOdrComponentBeans();
			LOGGER.info("OdrComponentBeans : {}", components);
			if (components != null) {
				List<ScComponent> scComponentList = new ArrayList<>();
				for (OdrComponentBean scComponentBean : components) {
					ScComponent scComponent = new ScComponent();
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						scComponentAttribute.setIsAdditionalParam(odrComponentAttributeBean.getIsAdditionalParam());
						if(Objects.nonNull(odrComponentAttributeBean.getIsAdditionalParam()) &&
								CommonConstants.Y.equals(odrComponentAttributeBean.getIsAdditionalParam())){
							ScAdditionalServiceParam scAdditionalServiceParam = saveAdditionalServiceParam(odrComponentAttributeBean);
							scComponentAttribute.setAttributeValue(String.valueOf(scAdditionalServiceParam.getId()));
							scComponentAttribute.setAttributeAltValueLabel(String.valueOf(scAdditionalServiceParam.getId()));
						}
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					LOGGER.info("ErfOdrServiceId for scServiceDetail : {}", serviceEntity.getErfOdrServiceId());
					LOGGER.info("Adding Component");
					scComponentList.add(scComponent);
					compOdrMapper.put(scComponentBean.getId(), scComponent.getUuid());
				}
				compMapper.put(serviceEntity.getErfOdrServiceId(), scComponentList);
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			/*scServiceDetails.add(serviceEntity);*/

			if (serviceDetail.getOdrGstAddress() != null) {
				ScGstAddress scGstAddress = serviceEntity.getScGstAddress();
				if (serviceEntity.getScGstAddress() == null) {
					scGstAddress = new ScGstAddress();
				}
				scGstAddress.setBuildingName(serviceDetail.getOdrGstAddress().getBuildingName());
				scGstAddress.setBuildingNumber(serviceDetail.getOdrGstAddress().getBuildingNumber());
				scGstAddress.setCreatedBy(serviceDetail.getOdrGstAddress().getCreatedBy());
				scGstAddress.setCreatedTime(serviceDetail.getOdrGstAddress().getCreatedTime());
				scGstAddress.setDistrict(serviceDetail.getOdrGstAddress().getDistrict());
				scGstAddress.setFlatNumber(serviceDetail.getOdrGstAddress().getFlatNumber());
				scGstAddress.setLatitude(serviceDetail.getOdrGstAddress().getLatitude());
				scGstAddress.setLocality(serviceDetail.getOdrGstAddress().getLocality());
				scGstAddress.setLongitude(serviceDetail.getOdrGstAddress().getLongitude());
				scGstAddress.setPincode(serviceDetail.getOdrGstAddress().getPincode());
				scGstAddress.setState(serviceDetail.getOdrGstAddress().getState());
				scGstAddress.setStreet(serviceDetail.getOdrGstAddress().getStreet());
				serviceEntity.setScGstAddress(scGstAddress);
			}

			LOGGER.info("serviceDetail.getTaxExemptionFlag() {} ", serviceDetail.getTaxExemptionFlag());
			boolean isAlreadyUploaded = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				ScAttachment scAttachment = serviceFulfillmentMapper
						.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity);
				Map<String, Object> attachmentMapper = scAttachmentRepository
						.findByServiceCodeAndType(serviceDetail.getUuid(), odrAttachmentBean.getType());
				if (!attachmentMapper.isEmpty()) {
					LOGGER.info("Already Attachment and odrAttachment is found , so updating {}", attachmentMapper);
					scAttachment.getAttachment().setId((Integer) attachmentMapper.get("attachment_id"));
					scAttachment.setId((Integer) attachmentMapper.get("sc_attachment_id"));
					isAlreadyUploaded = true;
				} else {
					if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
						isTaxFlag = true;
					}
					LOGGER.info("Creating a new attachment as already the attachment object is not found");
				}
				scAttachments.add(scAttachment);
			}

			scServiceDetails.add(serviceEntity);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.save(serviceEntity);
			List<ScAsset> scAssetList=new ArrayList<>();
			for (ScAsset asset : serviceEntity.getScAssets()) {
				asset.setScServiceDetail(scServiceDetail);
				scAssetList.add(asset);
			}
			LOGGER.info("ScAssets data: "+scAssetList.toString());
			scAssetRepository.saveAll(scAssetList);
			LOGGER.info("ScAssets are stored");

		});
	}

	/**
	 * Method to save additional service param.
	 * @param odrComponentAttributeBean
	 * @return
	 */
	private ScAdditionalServiceParam saveAdditionalServiceParam(OdrComponentAttributeBean odrComponentAttributeBean){
		OdrAdditionalServiceParamBean additionalServiceParamBean = odrComponentAttributeBean.getOdrAdditionalServiceParam();
		ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
		scAdditionalServiceParam.setValue(additionalServiceParamBean.getValue());
		scAdditionalServiceParam.setAttribute(additionalServiceParamBean.getAttribute());
		scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
		scAdditionalServiceParam.setIsActive(CommonConstants.Y);
		scAdditionalServiceParam.setCategory(additionalServiceParamBean.getCategory());
		scAdditionalServiceParam.setCreatedBy(additionalServiceParamBean.getCreatedBy());
		scAdditionalServiceParamRepo.save(scAdditionalServiceParam);
		return scAdditionalServiceParam;
	}

	/**
	 * Method to process odr commercials
	 * @param odrOrderBean
	 * @param scCommercialMapper
	 */
	private void processOdrCommercials(OdrOrderBean odrOrderBean,Map<Integer, List<ScServiceCommercial>> scCommercialMapper ){
		odrOrderBean.getOdrCommercialBean().forEach(odrCommercialBean -> {
			ScServiceCommercial scCommercial = serviceFulfillmentMapper
					.mapOdrCommercialToServiceCommercial(odrCommercialBean);
			if (scCommercialMapper.get(odrCommercialBean.getServiceId()) == null) {
				List<ScServiceCommercial> scServiceCommercials = new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercialBean.getServiceId(), scServiceCommercials);
			} else {
				scCommercialMapper.get(odrCommercialBean.getServiceId()).add(scCommercial);
			}
		});
	}

	/**
	 * Method to process teamsdrcommercials
	 * @param odrOrderBean
	 * @param scTeamsDrCommercialMapper
	 */
	private void processTeamsDRCommercials(OdrOrderBean odrOrderBean,Map<Integer, List<ScTeamsDRServiceCommercial>>
			scTeamsDrCommercialMapper){
		odrOrderBean.getOdrTeamsDRCommercialBean().forEach(odrTeamsDRCommercialBean -> {
			ScTeamsDRServiceCommercial scTeamsDRServiceCommercial = serviceFulfillmentMapper
					.mapOdrCommercialToTeamsDRCommercial(odrTeamsDRCommercialBean);
			if (scTeamsDrCommercialMapper.get(odrTeamsDRCommercialBean.getServiceId()) == null) {
				List<ScTeamsDRServiceCommercial> scServiceCommercials = new ArrayList<>();
				scServiceCommercials.add(scTeamsDRServiceCommercial);
				scTeamsDrCommercialMapper.put(odrTeamsDRCommercialBean.getServiceId(), scServiceCommercials);
			} else {
				scTeamsDrCommercialMapper.get(odrTeamsDRCommercialBean.getServiceId()).add(scTeamsDRServiceCommercial);
			}

		});
	}

	/**
	 * Method to process service details.
	 * @param scServiceDetails
	 * @param compMapper
	 * @param scCommercialMapper
	 * @param serviceIdMapper
	 * @param serviceParentIdMapper
	 */
	private void processServiceDetails(Set<ScServiceDetail> scServiceDetails,Map<Integer, List<ScComponent>> compMapper,
			Map<Integer, List<ScServiceCommercial>> scCommercialMapper,Map<Integer, String> serviceIdMapper,
			Map<Integer, ScServiceDetail> serviceParentIdMapper,Map<Integer, List<ScTeamsDRServiceCommercial>> scTeamsDRServiceCommercialsMapper){
		scServiceDetails.forEach(scServiceDetail ->  {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				LOGGER.info("ScServiceDetail erfOdrServiceId : {}", scServiceDetail.getErfOdrServiceId());
				LOGGER.info("Getting ComMapper");
				List<ScComponent> scCompos = compMapper.get(scServiceDetail.getErfOdrServiceId());
				for(ScComponent scCompo : scCompos) {
					LOGGER.info("ScServiceDetail Id : {}", scServiceDetail.getId());
					scCompo.setScServiceDetailId(scServiceDetail.getId());
					scCompo.setUuid(scServiceDetail.getUuid());
					for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
						LOGGER.info("ScComponentAttribute : {}, Key : {}, Value : {}", scCmpAttr.getId(),
								scCmpAttr.getAttributeName(), scCmpAttr.getAttributeValue());
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					LOGGER.info("Saving Component");
					scComponentRepository.saveAndFlush(scCompo);
				}
			}

			if (scCommercialMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScServiceCommercial> scCommercials = scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);

				}
			}

			if (scTeamsDRServiceCommercialsMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScTeamsDRServiceCommercial> scCommercials = scTeamsDRServiceCommercialsMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScTeamsDRServiceCommercial scTeamsDRServiceCommercial : scCommercials) {
					scTeamsDRServiceCommercial.setScServiceDetail(scServiceDetail);
					scTeamsDRServiceCommercialRepository.save(scTeamsDRServiceCommercial);
				}
				Double nrc =  scCommercials.stream().filter(scTeamsDRServiceCommercial -> Objects.nonNull(scTeamsDRServiceCommercial)
						&& Objects.nonNull(scTeamsDRServiceCommercial.getNrc())).mapToDouble(ScTeamsDRServiceCommercial::getNrc).sum();

				Double arc = scCommercials.stream().filter(scTeamsDRServiceCommercial -> Objects.nonNull(scTeamsDRServiceCommercial)
						&& Objects.nonNull(scTeamsDRServiceCommercial.getArc())).mapToDouble(ScTeamsDRServiceCommercial::getArc).sum();


				if (nrc > 0 && arc != null && arc > 0) {
					scServiceDetail.setMrc(arc / 12);
					scServiceDetail.setArc(arc);
					scServiceDetail.setNrc(nrc);
					scServiceDetailRepository.save(scServiceDetail);
				}
			}

			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				scServiceDetailRepository.save(scServiceDetail);
			}

			if(scServiceDetail.getParentId() != null) {
				if(serviceParentIdMapper.get(scServiceDetail.getParentId()) != null) {
					scServiceDetail.setParentId(serviceParentIdMapper.get(scServiceDetail.getParentId()).getId());
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
		});
	}

	/**
	 * Method to process solution components.
	 * @param odrOrderBean
	 * @param compOdrFinalMapper
	 * @param scOrderEntity
	 */
	private void processSolutionComponents(OdrOrderBean odrOrderBean,Map<Integer, Integer> compOdrFinalMapper,
			ScOrder scOrderEntity){
		if (Objects.nonNull(odrOrderBean.getSolutionComponentBeans())) {
			List<ScSolutionComponent> scSolutionComponents = new ArrayList<>();
			odrOrderBean.getSolutionComponentBeans().forEach(solComp -> {

				ScSolutionComponent scSolutionComponent = new ScSolutionComponent();
				scSolutionComponent.setComponentGroup(solComp.getComponentGroup());
				if (solComp.getCpeComponentId() != null && compOdrFinalMapper.containsKey(solComp.getCpeComponentId())
						&& Objects.nonNull(compOdrFinalMapper.get(solComp.getCpeComponentId()))
						&& compOdrFinalMapper.get(solComp.getCpeComponentId()) != null) {
					LOGGER.info("Got component id as {} mapping information for {}",
							compOdrFinalMapper.get(solComp.getCpeComponentId()), solComp.getCpeComponentId());
					scSolutionComponent.setCpeComponentId(compOdrFinalMapper.get(solComp.getCpeComponentId()));
				}
				scSolutionComponent.setIsActive(CommonConstants.Y);
				scSolutionComponent.setOrderCode(scOrderEntity.getUuid());
				scSolutionComponent.setScOrder(scOrderEntity);
				if (solComp.getParentServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getParentServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						LOGGER.info("Processing for service id {} component type {} cpe component id {}",
								scServiceDetail.getUuid(), scSolutionComponent.getComponentGroup(),
								scSolutionComponent.getCpeComponentId());
						scSolutionComponent.setScServiceDetail2(scServiceDetail);
						scSolutionComponent.setParentServiceCode(scServiceDetail.getUuid());
					}
				}
				if (solComp.getServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						scSolutionComponent.setScServiceDetail1(scServiceDetail);
						scSolutionComponent.setServiceCode(scServiceDetail.getUuid());
					}
				}
				if (solComp.getSolutionServiceDetailId() != null) {
					ScServiceDetail scServiceDetail = scServiceDetailRepository
							.findFirstByErfOdrServiceIdOrderByIdDesc(solComp.getSolutionServiceDetailId());
					if (Objects.nonNull(scServiceDetail)) {
						scSolutionComponent.setScServiceDetail3(scServiceDetail);
						scSolutionComponent.setSolutionCode(scServiceDetail.getUuid());
					}
				}
				if (scSolutionComponent.getScServiceDetail1() != null) {
					scSolutionComponents.add(scSolutionComponent);
				}
			});
			scSolutionComponentRepository.saveAll(scSolutionComponents);
		}
	}

	/**
	 * Method to process cpebom from mst cost catalogue
	 * @param scComponents
	 * @param scServiceDetail
	 * @param scComponent
	 * @param cpeBomResponse
	 * @throws TclCommonException
	 */
	private void processCpeBom(List<ScComponent> scComponents,
			ScServiceDetail scServiceDetail,ScComponent scComponent,String cpeBomResponse)
			throws TclCommonException {
		AtomicReference<String> purchaseType = new AtomicReference<>();
		scComponents.stream().filter(scComponent1 -> LM.equals(scComponent1.getComponentName())).findAny().flatMap(
				scComponent1 -> scComponentAttributesRepository.findByScComponent(scComponent1).stream()
						.filter(scComponentAttribute1 -> "Media Gateway Purchase Type"
								.equals(scComponentAttribute1.getAttributeName())).findAny())
				.ifPresent(scComponentAttribute1 -> {
					purchaseType.set(scComponentAttribute1.getAttributeValue());
				});

		CpeBom cpeBom = new CpeBom();
		cpeBom.setBomName(scComponent.getComponentName());
		cpeBom.setUniCode(scComponent.getComponentName());
		List<CpeBomDetails> cpeBomDetails = new ArrayList<>();
		cpeBom.setCpeBomDetails(cpeBomDetails);
		MediaGatewayListPricesBean mediaGatewayListPricesBean = Utils.convertJsonToObject(cpeBomResponse,MediaGatewayListPricesBean.class);
		LOGGER.info("Bean after convertion :: {}",mediaGatewayListPricesBean.toString());
		processCpeBomFromCostCatalogue(mediaGatewayListPricesBean.getCpeAmcCharges().getCpeSubcharges(),
				purchaseType.get(),scComponent,scServiceDetail,cpeBomDetails,mediaGatewayListPricesBean.getQty());

		processCpeBomFromCostCatalogue(mediaGatewayListPricesBean.getCpeOutrightRentalCharges().getCpeSubcharges(),
				purchaseType.get(),scComponent,scServiceDetail,cpeBomDetails,mediaGatewayListPricesBean.getQty());

		if(Objects.nonNull(mediaGatewayListPricesBean.getCpeManagement().getCpeSubcharges()) &&
				!mediaGatewayListPricesBean.getCpeManagement().getCpeSubcharges().isEmpty()){
			processCpeBomFromCostCatalogue(mediaGatewayListPricesBean.getCpeManagement().getCpeSubcharges(),
					purchaseType.get(),scComponent,scServiceDetail,cpeBomDetails,mediaGatewayListPricesBean.getQty());
		}
		ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
		scComponentAttribute.setAttributeName(CPE_BOM);
		scComponentAttribute.setIsAdditionalParam(CommonConstants.Y);
		scComponentAttribute.setCreatedBy(Utils.getSource());
		scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponentAttribute.setScComponent(scComponent);
		//		scComponent.getScComponentAttributes().add(scComponentAttribute);
		ScAdditionalServiceParam scAdditionalServiceParam = new ScAdditionalServiceParam();
		scAdditionalServiceParam.setCategory(CPE_BOM);
		scAdditionalServiceParam.setAttribute("cpe-bom-resource");
		try {
			scAdditionalServiceParam.setValue(Utils.convertObjectToJson(cpeBom));
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
		scAdditionalServiceParam.setCreatedBy(Utils.getSource());
		scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
		scAdditionalServiceParam.setIsActive(CommonConstants.Y);
		scAdditionalServiceParam = scAdditionalServiceParamRepo.save(scAdditionalServiceParam);
		scComponentAttribute.setAttributeValue(String.valueOf(scAdditionalServiceParam.getId()));
		scComponentAttribute.setAttributeAltValueLabel(String.valueOf(scAdditionalServiceParam.getId()));
		scComponentAttribute.setScServiceDetailId(scComponent.getScServiceDetailId());
		scComponentAttributesRepository.save(scComponentAttribute);
	}

	/**
	 * Method to process cpeBomFromCostCatalogue.
	 * @param cpeSubchargesBeans
	 * @param purchaseType
	 * @param scComponent
	 * @param scServiceDetail
	 * @param cpeBomDetails
	 */
	private void processCpeBomFromCostCatalogue(List<CpeSubchargesBean> cpeSubchargesBeans, String purchaseType,
			ScComponent scComponent, ScServiceDetail scServiceDetail, List<CpeBomDetails> cpeBomDetails,
			Integer quantity) {
		cpeSubchargesBeans.forEach(cpeSubchargesBean -> {
			if (RENTAL_PURCHASE.equals(purchaseType)) {
				if(PHYSICAL_IMPLEMENTATION.equals(cpeSubchargesBean.getComponentType()))
					cpeSubchargesBean.setComponentName(PHYSICAL_IMP_PARTCODE);
				mstCostCatalogueRepository.
						findByBundledBomAndProductCodeAndCategoryForRental(scComponent.getComponentName(),
								cpeSubchargesBean.getComponentName(), cpeSubchargesBean.getComponentType()).forEach(
						mstCostCatalogue -> persistCpeCostDetails(mstCostCatalogue, cpeBomDetails, scComponent,
								scServiceDetail,cpeSubchargesBean,quantity));
			} else if (OUTRIGHT_PURCHASE.equals(purchaseType)) {
				mstCostCatalogueRepository.
						findByBundledBomAndProductCodeAndCategoryForOutright(scComponent.getComponentName(),
								cpeSubchargesBean.getComponentName(), cpeSubchargesBean.getComponentType())
						.forEach(mstCostCatalogue ->
								persistCpeCostDetails(mstCostCatalogue, cpeBomDetails, scComponent, scServiceDetail,cpeSubchargesBean,quantity));
			}
		});
	}

	/**
	 * Method to persist cpe cost details.
	 * @param mstCostCatalogue
	 * @param cpeBomDetails
	 * @param scComponent
	 * @param scServiceDetail
	 */
	private void persistCpeCostDetails(MstCostCatalogue mstCostCatalogue,List<CpeBomDetails> cpeBomDetails,
			ScComponent scComponent,ScServiceDetail scServiceDetail, CpeSubchargesBean cpeSubchargesBean,
			Integer quantity){
		CpeBomDetails cpeBomDetail = new CpeBomDetails();
		cpeBomDetail.setHsnCode(mstCostCatalogue.getHsnCode());
		cpeBomDetail.setListPrice(mstCostCatalogue.getTotalListPriceUsd());
		cpeBomDetail.setLongDesc(mstCostCatalogue.getDescription());
		cpeBomDetail.setProductCategory(mstCostCatalogue.getCategory());
		cpeBomDetail.setProductCode(mstCostCatalogue.getProductCode());
		cpeBomDetail.setQuantity(mstCostCatalogue.getQuantity());
		cpeBomDetails.add(cpeBomDetail);

		CpeCostDetails cpeCostDetails = new CpeCostDetails();
		//		cpeCostDetails.setId(cpeSubchargesBean.geti);
		cpeCostDetails.setOem(CommonConstants.TEAMSDR);
		cpeCostDetails.setBundledBom(scComponent.getComponentName());
		cpeCostDetails.setCalculatedPrice(mstCostCatalogue.getTotalListPriceUsd()*quantity);
		cpeCostDetails.setPerListPriceUsd(mstCostCatalogue.getPerListPriceUsd());
		cpeCostDetails.setCategory(mstCostCatalogue.getCategory());
		cpeCostDetails.setCreatedDate(new Timestamp(new Date().getTime()));
		cpeCostDetails.setCurrency(mstCostCatalogue.getCurrency());
		cpeCostDetails.setDescription(mstCostCatalogue.getDescription());
		cpeCostDetails.setHsnCode(mstCostCatalogue.getHsnCode());
		if (CommonConstants.INDIA_SITES.equalsIgnoreCase(scServiceDetail.getDestinationCountry())) {
			cpeCostDetails.setVendorName(mstCostCatalogue.getVendorName());
			cpeCostDetails.setVendorCode(mstCostCatalogue.getVendorCode());
		} else if (Objects.nonNull(scServiceDetail.getDestinationCountry())) {
			MstVendorCpeInternational mstVendorCpeInternational = mstVendorCpeInternationalRepository
					.findByCountry(scServiceDetail.getDestinationCountry());
			if (Objects.isNull(mstVendorCpeInternational)) {
				mstVendorCpeInternational = mstVendorCpeInternationalRepository
						.findByCountry(TeamsDRFulfillmentConstants.REST_OF_THE_WORLD);
			}
			cpeCostDetails.setVendorCode(mstVendorCpeInternational.getVendorCode());
			cpeCostDetails.setVendorName(mstVendorCpeInternational.getVendorName());
		}
		cpeCostDetails.setComponentId(scComponent.getId());
		cpeCostDetails.setServiceId(scServiceDetail.getId());
		cpeCostDetails.setServiceCode(scServiceDetail.getUuid());
		cpeCostDetails.setQuantity(quantity);
		cpeCostDetails.setComponentId(scComponent.getId());
		cpeCostDetails.setProductCode(mstCostCatalogue.getProductCode());
		cpeCostDetails.setMaterialCode(Objects.nonNull(mstCostCatalogue.getRentalMaterialCode()) ?
				mstCostCatalogue.getRentalMaterialCode() : mstCostCatalogue.getSaleMaterialCode());
		cpeCostDetails = cpeCostDetailsRepository.save(cpeCostDetails);
		cpeBomDetail.setId(cpeCostDetails.getId());
	}

	/**
	 * Method to process cpe bom.
	 * @param scServiceDetails
	 */
	private void processCpeBom(Set<ScServiceDetail> scServiceDetails){
		scServiceDetails.forEach(scServiceDetail -> {
			List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(scServiceDetail.getId());
			scComponents.forEach(scComponent -> {
				if(Objects.nonNull(scComponent.getScComponentAttributes())
						&& !scComponent.getScComponentAttributes().isEmpty()){
					scComponent.getScComponentAttributes().forEach(scComponentAttribute -> {
						if(CPE_BOM_RESPONSE.equals(scComponentAttribute.getAttributeName())){
							scAdditionalServiceParamRepo
									.findById(Integer.valueOf(scComponentAttribute.getAttributeValue())).ifPresent(scAdditionalServiceParam -> {
								// Method to process cpe bom from cost catalogue..
								try {
									if(!LM.equals(scComponent.getComponentName())){
										processCpeBom(scComponents,scServiceDetail,scComponent,
												scAdditionalServiceParam.getValue());
									}
								} catch (TclCommonException e) {
									e.printStackTrace();
								}
							});
						}
					});
				}
			});
		});
	}

	/**
	 * Method to process fulfillmentdate for teamsdr.
	 * @param odrOrderBean
	 * @return
	 * @throws Exception
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public Set<ScServiceDetail> processfulfillmentDateForTeamsDR(OdrOrderBean odrOrderBean)  {
		Map<String, String> orderDetail = new HashMap<>();
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, ScServiceDetail> serviceParentIdMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<Integer, String> compOdrMapper = new HashMap<>();
		Map<Integer, Integer> compOdrFinalMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		Map<Integer, List<ScTeamsDRServiceCommercial>> scTeamsDRServiceCommercialsMapper = new HashMap<>();
		Map<Integer, String> serviceIdMapper = new HashMap<>();

		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		processContractInfo(scContractingInfo,odrOrderBean,scOrderEntity);
		processOdrAttributes(scOrderAttrs,odrOrderBean,scOrderEntity);
		processOdrServiceDetail(orderDetail,scServiceDetails,odrOrderBean,scOrderEntity,scContractingInfo,scOrderAttrs,
				serviceMapper,serviceParentIdMapper,compOdrMapper,compMapper);
		processOdrCommercials(odrOrderBean,scCommercialMapper);
		processTeamsDRCommercials(odrOrderBean,scTeamsDRServiceCommercialsMapper);
		scOrderAttributeRepository.saveAll(scOrderAttrs);
		scServiceDetails.forEach(scServiceDetail -> serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid()));
		processServiceDetails(scServiceDetails,compMapper,scCommercialMapper,serviceIdMapper,serviceParentIdMapper,scTeamsDRServiceCommercialsMapper);
		processSolutionComponents(odrOrderBean,compOdrFinalMapper,scOrderEntity);
		processCpeBom(scServiceDetails);
		return scServiceDetails;
	}
	
	private void setOptimusServiceIdForInternationalOrders(OdrServiceDetailBean odrServiceDetail, String opOrderCode, ScServiceDetail serviceEntity) {
		LOGGER.info("setOptimusServiceIdForInternationalOrders for uuid:{},productName::{} with orderCode:{}", odrServiceDetail.getUuid(),odrServiceDetail.getErfPrdCatalogProductName(),opOrderCode);
		ScServiceDetail activeInternationalScServiceDetail=getInternationalActiveOptimusServiceDetail( odrServiceDetail, opOrderCode);
		if(activeInternationalScServiceDetail!=null && !activeInternationalScServiceDetail.getUuid().equalsIgnoreCase(odrServiceDetail.getUuid())) {
			LOGGER.info("International Active Optimus Service Id exists::{} ", activeInternationalScServiceDetail.getUuid());
			serviceEntity.setUuid(activeInternationalScServiceDetail.getUuid());
		}
		if(((odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZO Internet WAN") && !odrServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))
				|| odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("DIA") 
				|| (odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN") && !odrServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))) 
				&& serviceEntity.getTpsServiceId()==null) {
			LOGGER.info("International Tps Service Id Not exists for ::{} ", odrServiceDetail.getUuid());
			serviceEntity.setTpsServiceId(odrServiceDetail.getUuid());
		}
	}
	
	private ScServiceDetail getInternationalActiveOptimusServiceDetail(OdrServiceDetailBean odrServiceDetail, String opOrderCode) {
		LOGGER.info("getInternationalActiveOptimusServiceDetail for uuid:{},productName::{} with orderCode:{}", odrServiceDetail.getUuid(),odrServiceDetail.getErfPrdCatalogProductName(),opOrderCode);
		ScServiceDetail activeInternationalScServiceDetail=null;
		if((odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZO Internet WAN") && !odrServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))
				|| odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("DIA") 
				|| (odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN") && !odrServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))) {
			LOGGER.info("International Order for uuid:{} and country:{}", odrServiceDetail.getUuid(),odrServiceDetail.getDestinationCountry());
			activeInternationalScServiceDetail=scServiceDetailRepository.findByMstStatus_codeAndIsMigratedOrderAndTpsServiceId("ACTIVE","N",odrServiceDetail.getUuid());
		}
		return activeInternationalScServiceDetail;
	}
	

	public void addOrModifyScServiceAttr(String attrName, String attrValue, ScServiceDetail scServiceDetail,
			String userName) {
		ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), attrName);
		if (scServiceAttribute != null) {
			scServiceAttribute.setAttributeValue(attrValue);
			scServiceAttribute.setUpdatedBy(userName);
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		} else {
			scServiceAttribute = new ScServiceAttribute();
			scServiceAttribute.setAttributeName(attrName);
			scServiceAttribute.setAttributeValue(attrValue);
			scServiceAttribute.setScServiceDetail(scServiceDetail);
			scServiceAttribute.setUpdatedBy(userName);
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setCreatedBy(userName);
			scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setAttributeAltValueLabel(attrName);
			scServiceAttribute.setIsAdditionalParam(CommonConstants.N);
			scServiceAttribute.setIsActive(CommonConstants.Y);
		}
		scServiceAttributeRepository.save(scServiceAttribute);
	}

	public void addOrModifyScComponentAttr(String attrName, String attrValue, ScServiceDetail scServiceDetail,
			String userName, ScComponent scComponent) {
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponentOrderByIdDesc(scServiceDetail.getId(),
						attrName, scComponent);
		if (scComponentAttribute != null) {
			scComponentAttribute.setAttributeValue(attrValue);
			scComponentAttribute.setUpdatedBy(userName);
			scComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		} else {
			scComponentAttribute = new ScComponentAttribute();
			scComponentAttribute.setAttributeName(attrName);
			scComponentAttribute.setAttributeValue(attrValue);
			scComponentAttribute.setScServiceDetailId(scServiceDetail.getId());
			scComponentAttribute.setScComponent(scComponent);
			scComponentAttribute.setUpdatedBy(userName);
			scComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scComponentAttribute.setCreatedBy(userName);
			scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scComponentAttribute.setIsAdditionalParam(CommonConstants.N);
			scComponentAttribute.setAttributeAltValueLabel(attrName);
			scComponentAttribute.setIsActive(CommonConstants.Y);
		}
		scComponentAttributesRepository.save(scComponentAttribute);
	}
	
	public String getEventSourceforBurstable(ScServiceDetail scServiceDetail) {
		String eventSource="";
		LOGGER.info("MDC Filter token value in before Queue call getEventSoruceforBurstable {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		try {
			eventSource = (String) mqUtils.sendAndReceive(eventSourceQueue,String.valueOf(scServiceDetail.getUuid()));
			if (StringUtils.isEmpty(eventSource) || "Not Available".equals(eventSource)) {
				String custName = scServiceDetail.getScOrder().getErfCustLeName().replaceAll("[^a-zA-Z0-9]", "")
						.substring(0, 6);
				eventSource = custName.concat("-").concat(scServiceDetail.getUuid());
			}
		} catch (TclCommonException e) {
						e.printStackTrace();
		}
		LOGGER.info("eventSource Response {}", eventSource);
		
		return eventSource;
	}
	
	/**
	 * 
	 * processRenewalfulfillmentData - This method processes the odrOrderBean
	 * 
	 * @param odrOrderBean
	 * @return Set<ScServiceDetail>
	 * @throws TclCommonException
	 */
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ScServiceDetail processRenewalfulfillmentData(OdrOrderBean odrOrderBean) throws TclCommonException {
		LOGGER.info("processRenewalfulfillmentData method invoked::{}",odrOrderBean);
		LOGGER.info("OrderCode::{}",odrOrderBean.getOpOrderCode());
		for (OdrServiceDetailBean odrServiceDetail : odrOrderBean.getOdrServiceDetails()) {
			if (Objects.nonNull(odrServiceDetail.getUuid())) {
				LOGGER.info("Uuid::{}", odrServiceDetail.getUuid());
				getServiceDetail(odrServiceDetail.getUuid(), odrOrderBean.getOpOrderCode(),
						odrOrderBean.getOrderType());
			} else if (Objects.nonNull(odrServiceDetail.getParentServiceUuid())) {
				LOGGER.info("Parent Uuid::{}", odrServiceDetail.getParentServiceUuid());
				getServiceDetail(odrServiceDetail.getParentServiceUuid(), odrOrderBean.getOpOrderCode(),
						odrOrderBean.getOrderType());
			}
		}
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		ScOrderAttribute effectiveDateAttribute = scOrderAttrs.stream()
				.filter(attr -> attr.getAttributeName().equalsIgnoreCase("Effective Date")).findFirst().orElse(null);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
			ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
			contrEntity.setScOrder(scOrderEntity);
			if(effectiveDateAttribute!=null && effectiveDateAttribute.getAttributeValue()!=null) {
				contrEntity.setContractStartDate(DateUtil.convertDateStringDDMMMYYYYToIncTimestampByMonth(effectiveDateAttribute.getAttributeValue(),0)); 
				contrEntity.setContractEndDate(DateUtil.convertDateStringDDMMMYYYYToIncTimestampByMonth(effectiveDateAttribute.getAttributeValue(),contrEntity.getOrderTermInMonths().intValue()));
			}
			scContractingInfo.add(contrEntity);
		});
		ScServiceDetail renewalScServiceDetail = new ScServiceDetail();
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		odrOrderBean.getOdrServiceDetails().stream().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity = null;
			try {
				serviceEntity = serviceFulfillmentMapper.mapServiceEntityToBean(serviceDetail, odrOrderBean);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(e);
			}
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			serviceEntity.setServiceConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setActivationConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setBillingStatus(TaskStatusConstants.PENDING);
	
			if (serviceEntity.getOrderType() == null) {
				serviceEntity.setOrderType(scOrderEntity.getOrderType());
			}
			if (serviceEntity.getOrderCategory() == null) {
				serviceEntity.setOrderCategory(scOrderEntity.getOrderCategory());
			}
			serviceEntity.setServiceAceptanceStatus(TaskStatusConstants.PENDING);
			serviceEntity.setAssuranceCompletionStatus(TaskStatusConstants.PENDING);
			serviceEntity.setIsDelivered(TaskStatusConstants.PENDING);
			scOrderRepository.save(scOrderEntity);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag &&serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}
			
			ScOrderAttribute orderAttribute = scOrderAttrs.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase("PO_NUMBER")).findFirst().orElse(null);

			/*
			 * if ((serviceDetail.getOdrContractInfo() != null &&
			 * serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null &&
			 * serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))||
			 * (orderAttribute!=null && orderAttribute.getAttributeValue()!=null))
			 * scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY,
			 * PO_ATTACHMENT_CATEGORY));
			 */
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components=serviceDetail.getOdrComponentBeans();
			LOGGER.info("OdrComponentBeans : {}", components);
			if (components != null) {
				for (OdrComponentBean scComponentBean : components) {
					LOGGER.info("Into Component Bean");
					ScComponent scComponent=null;
					if (scComponent == null) {
						scComponent = new ScComponent();

					}
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					LOGGER.info("odrComponentAttributeBeans for componentBean {} : {}", scComponentBean.getId(), scComponentBean.getOdrComponentAttributeBeans());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						LOGGER.info("Into Component Attributes");
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						if(serviceEntity.getId() != null && serviceEntity.getOrderType() != null  && serviceEntity.getOrderType().equalsIgnoreCase("Termination")) {
							scComponentAttribute.setScServiceDetailId(serviceEntity.getId());
						}
						if (scComponentAttribute.getAttributeName().equals("siteGstAddress")
								&& scComponentAttribute.getAttributeValue() != null) {
							LOGGER.info("Constructing site Gst address for IAS & GVPN{} ",scComponentAttribute.getAttributeValue());
							try {
								GstAddressBean gstAddress = Utils.convertJsonToObject(
										scComponentAttribute.getAttributeValue(), GstAddressBean.class);
								if (gstAddress != null) {
									ScGstAddress scGstAddress = new ScGstAddress();
									scGstAddress.setBuildingName(gstAddress.getBuildingName());
									scGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
									scGstAddress.setDistrict(gstAddress.getDistrict());
									scGstAddress.setFlatNumber(gstAddress.getFlatNumber());
									scGstAddress.setLatitude(gstAddress.getLatitude());
									scGstAddress.setLocality(gstAddress.getLocality());
									scGstAddress.setLongitude(gstAddress.getLongitude());
									scGstAddress.setPincode(gstAddress.getPinCode());
									scGstAddress.setState(gstAddress.getState());
									scGstAddress.setStreet(gstAddress.getStreet());
									scGstAddress = scGstAddressRepository.save(scGstAddress);
									
									scComponentAttribute.setAttributeName("siteGstAddressId");
									scComponentAttribute.setAttributeAltValueLabel(scGstAddress.getId().toString());
									scComponentAttribute.setAttributeValue(scGstAddress.getId().toString());
									LOGGER.info("Site Gst Address id is {}", scComponentAttribute.getAttributeValue());
									
								}
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					LOGGER.info("ErfOdrServiceId for scServiceDetail : {}", serviceEntity.getErfOdrServiceId());
					LOGGER.info("Adding Component");
					if(!compMapper.containsKey(serviceEntity.getErfOdrServiceId())) {
						compMapper.put(serviceEntity.getErfOdrServiceId(), new ArrayList<>());
					}
					compMapper.get(serviceEntity.getErfOdrServiceId()).add(scComponent);
				}
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			scServiceDetails.add(serviceEntity);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.save(serviceEntity);
			if(serviceEntity.getScAssets() != null) {
				HashMap<Integer, ScAsset> scAssetList=new HashMap<>();
				List<ScAssetAttribute> scAssetAttributeList=new ArrayList<>();
				List<ScAssetRelation> scAssetRelationList=new ArrayList<>();
				for (ScAsset asset : serviceEntity.getScAssets()) {
					asset.setScServiceDetail(scServiceDetail);
					scAssetList.put(asset.getOdrAssetId(), asset);
					if(asset.getScAssetAttributes() != null)
						scAssetAttributeList.addAll(asset.getScAssetAttributes());
					if(asset.getScAssetRelations() != null)
						scAssetRelationList.addAll(asset.getScAssetRelations());
				}
				LOGGER.info("ScAssets data: "+scAssetList.toString());
				scAssetRepository.saveAll(serviceEntity.getScAssets());
				scAssetAttributeRepository.saveAll(scAssetAttributeList);
				if(!scAssetRelationList.isEmpty()) {
					scAssetRelationList.forEach(scAssetRelation -> {
						scAssetRelation.setScAssetId(scAssetList.get(scAssetRelation.getScAssetId()).getId());
						scAssetRelation.setScRelatedAssetId(scAssetList.get(scAssetRelation.getScRelatedAssetId()).getId());
					});
					scAssetRelationRepository.saveAll(scAssetRelationList);
				}
				LOGGER.info("ScAssets are stored");
			}
		});
		scContractInfoRepository.saveAll(scContractingInfo);
		scOrderAttributeRepository.saveAll(scOrderAttrs);
		//scServiceDetailRepository.saveAll(scServiceDetails);

		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			//String scServiceId = serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial=serviceFulfillmentMapper.mapOdrCommercialToServiceCommercial(odrCommercial);
			if(scCommercialMapper.get(odrCommercial.getServiceId())==null) {
				List<ScServiceCommercial> scServiceCommercials=new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
			}else {
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scCommercial);
			}
			
			
		}
		
		Map<Integer, String> serviceIdMapper = new HashMap<>();
		Map<Integer, ScServiceDetail> serviceParentIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			createServiceStaus(scServiceDetail, TaskStatusConstants.INPROGRESS);
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
			serviceParentIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail);
		}
		Map<Integer,List<String>> masterSlaveMap = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				LOGGER.info("ScServiceDetail erfOdrServiceId : {}",scServiceDetail.getErfOdrServiceId());
				LOGGER.info("Getting ComMapper");
				List<ScComponent> scCompos=compMapper.get(scServiceDetail.getErfOdrServiceId());
				LOGGER.info("ScServiceDetail Id : {}", scServiceDetail.getId());
				scCompos.forEach(scCompo -> {
					scCompo.setScServiceDetailId(scServiceDetail.getId());
					scCompo.setUuid(scServiceDetail.getUuid());
					for (ScComponentAttribute scCmpAttr : scCompo.getScComponentAttributes()) {
						LOGGER.info("ScComponentAttribute : {}, Key : {}, Value : {}", scCmpAttr.getId(), scCmpAttr.getAttributeName(), scCmpAttr.getAttributeValue());
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					LOGGER.info("Saving Component");
					scComponentRepository.save(scCompo);
				});
			}
			
			if(scCommercialMapper.get(scServiceDetail.getErfOdrServiceId())!=null) {
				List<ScServiceCommercial> scCommercials=scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);
					
					if (scCommercial.getReferenceName().contains("Burstable")) {
						Double arc = scCommercial.getArc()!= null ? scCommercial.getArc() : 0;
						if(arc>0){
							getEventSourceforBurstable(scServiceDetail);
						}
					}
				}
			}
			
			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				scServiceDetailRepository.save(scServiceDetail);
			}
			if (scServiceDetail.getMasterVrfServiceId() != null) {
				LOGGER.info("Odr Master vrf service ID --> {}",scServiceDetail.getMasterVrfServiceId());
				ScServiceDetail scServiceMaster = scServiceDetails.stream()
						.filter(service -> service.getErfOdrServiceId().equals(scServiceDetail.getMasterVrfServiceId()))
						.findFirst().orElse(null);
				if (scServiceMaster != null) {
					LOGGER.info("SC Master vrf service ID --> {}",scServiceMaster.getId());
					scServiceDetail.setMasterVrfServiceId(scServiceMaster.getId());
					scServiceDetail.setMasterVrfServiceCode(scServiceMaster.getUuid());
					scServiceDetailRepository.save(scServiceDetail);
					List<String> slaveList = new ArrayList<>();
					if(masterSlaveMap.containsKey(scServiceMaster.getId())) {
						slaveList =  masterSlaveMap.get(scServiceMaster.getId());
						slaveList.add(scServiceDetail.getUuid());
					}else {
						slaveList.add(scServiceDetail.getUuid());
					}
					masterSlaveMap.put(scServiceMaster.getId(), slaveList);
				}
			}
			
			
			if(scServiceDetail.getParentId() != null) {
				if(serviceParentIdMapper.get(scServiceDetail.getParentId()) != null) {
					scServiceDetail.setParentId(serviceParentIdMapper.get(scServiceDetail.getParentId()).getId());
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
		}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (scServiceDetail.getIsMultiVrf() != null && scServiceDetail.getIsMultiVrf().equals(CommonConstants.Y)
					&& masterSlaveMap.containsKey(scServiceDetail.getId())) {
				addOrModifyScServiceAttr("SLAVE_VRF_SERVICE_ID",
						StringUtils.join(masterSlaveMap.get(scServiceDetail.getId()), ","), scServiceDetail,
						scServiceDetail.getCreatedBy());
			}
		}
		
		if(!scServiceDetails.isEmpty()) {
			LOGGER.info("Renewals scServiceDetails exists::{}",scServiceDetails.size());
			ScServiceDetail scServiceDetail=scServiceDetails.stream().findFirst().orElse(null);
			renewalScServiceDetail.setUuid(scOrderEntity.getUuid());
			renewalScServiceDetail.setScOrder(scOrderEntity);
			renewalScServiceDetail.setOrderType("RENEWALS");
			renewalScServiceDetail.setScOrderUuid(scOrderEntity.getUuid());
			renewalScServiceDetail.setIsActive("Y");
			renewalScServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
			if(scServiceDetail!=null) {
				renewalScServiceDetail.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
				renewalScServiceDetail.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
			}
			renewalScServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			Optional<OdrServiceDetailBean> odrServiceDetailBeanOptional =odrOrderBean.getOdrServiceDetails().stream().findFirst();
			if(odrServiceDetailBeanOptional.isPresent()) {
				LOGGER.info("odrServiceDetailBeanOptional exists");
				OdrServiceDetailBean odrServiceDetailBean=odrServiceDetailBeanOptional.get();
				Optional<OdrAttachmentBean> odrAttachmentBeanOptional =odrServiceDetailBean.getOdrAttachments().stream().filter(oa -> "RENEWALS".equalsIgnoreCase(oa.getType())).findFirst();
				if(odrAttachmentBeanOptional.isPresent()) {
					LOGGER.info("odrAttachmentBeanOptional exists");
					OdrAttachmentBean odrAttachmentBean=odrAttachmentBeanOptional.get();
					scAttachments.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, renewalScServiceDetail));
					renewalScServiceDetail.setScAttachments(scAttachments);
				}
			}
			scServiceDetailRepository.save(renewalScServiceDetail);
		}
		return renewalScServiceDetail;
	}
	
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ScServiceDetail processRenewalfulfillmentDateForNpl(OdrOrderBean odrOrderBean) throws Exception {
		LOGGER.info("processRenewalfulfillmentDateForNpl method invoked::{}",odrOrderBean);
		LOGGER.info("OrderCode::{}",odrOrderBean.getOpOrderCode());
		for (OdrServiceDetailBean odrServiceDetail : odrOrderBean.getOdrServiceDetails()) {
			if (Objects.nonNull(odrServiceDetail.getUuid())) {
				LOGGER.info("Uuid::{}", odrServiceDetail.getUuid());
				getServiceDetail(odrServiceDetail.getUuid(), odrOrderBean.getOpOrderCode(),
						odrOrderBean.getOrderType());
			} else if (Objects.nonNull(odrServiceDetail.getParentServiceUuid())) {
				LOGGER.info("Parent Uuid::{}", odrServiceDetail.getParentServiceUuid());
				getServiceDetail(odrServiceDetail.getParentServiceUuid(), odrOrderBean.getOpOrderCode(),
						odrOrderBean.getOrderType());
			}
		}
		Map<String, String> orderDetail = new HashMap<>();
		ScOrder scOrderEntity = serviceFulfillmentMapper.mapOrderBeanToEntity(odrOrderBean);
		Set<ScOrderAttribute> scOrderAttrs = new HashSet<>();
		for (OdrOrderAttributeBean odrOrderAttributes : odrOrderBean.getOdrOrderAttributes()) {
			ScOrderAttribute odrAttrEntity = serviceFulfillmentMapper.mapOrderAttrEntityToBean(odrOrderAttributes);
			odrAttrEntity.setScOrder(scOrderEntity);
			scOrderAttrs.add(odrAttrEntity);
		}
		ScOrderAttribute effectiveDateAttribute = scOrderAttrs.stream()
				.filter(attr -> attr.getAttributeName().equalsIgnoreCase("Effective Date")).findFirst().orElse(null);
		Set<ScContractInfo> scContractingInfo = new HashSet<>();
		odrOrderBean.getOdrContractInfos().stream().forEach(contractingInfo -> {
			ScContractInfo contrEntity = serviceFulfillmentMapper.mapContractingInfoEntityToBean(contractingInfo);
			contrEntity.setScOrder(scOrderEntity);
			if(effectiveDateAttribute!=null && effectiveDateAttribute.getAttributeValue()!=null) {
				contrEntity.setContractStartDate(DateUtil.convertDateStringDDMMMYYYYToIncTimestampByMonth(effectiveDateAttribute.getAttributeValue(),0)); 
				contrEntity.setContractEndDate(DateUtil.convertDateStringDDMMMYYYYToIncTimestampByMonth(effectiveDateAttribute.getAttributeValue(),contrEntity.getOrderTermInMonths().intValue()));
			}
			scContractingInfo.add(contrEntity);
		});
		ScServiceDetail renewalScServiceDetail = new ScServiceDetail();
		Set<ScServiceDetail> scServiceDetails = new HashSet<>();
		Map<Integer, String> serviceMapper = new HashMap<>();
		Map<Integer, List<ScComponent>> compMapper = new HashMap<>();
		Map<Integer, List<ScServiceCommercial>> scCommercialMapper = new HashMap<>();
		odrOrderBean.getOdrServiceDetails().stream().forEach(serviceDetail -> {
			ScServiceDetail serviceEntity =  serviceFulfillmentMapper.mapServiceEntityToBeanNpl(serviceDetail, odrOrderBean);
			orderDetail.put("ORDER_CODE", odrOrderBean.getOpOrderCode());
			orderDetail.put("ORDER_SERVICE_ID", serviceEntity.getUuid());
			serviceEntity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			serviceEntity.setServiceConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setActivationConfigStatus(TaskStatusConstants.PENDING);
			serviceEntity.setBillingStatus(TaskStatusConstants.PENDING);
			serviceEntity.setIsDelivered(TaskStatusConstants.PENDING);
			if (serviceEntity.getOrderType() == null) {
				serviceEntity.setOrderType(scOrderEntity.getOrderType());
			}
			if (serviceEntity.getOrderCategory() == null) {
				serviceEntity.setOrderCategory(scOrderEntity.getOrderCategory());
			}
			

			scOrderRepository.save(scOrderEntity);
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			boolean isTaxFlag = false;
			for (OdrAttachmentBean odrAttachmentBean : serviceDetail.getOdrAttachments()) {
				if (odrAttachmentBean.getType().equalsIgnoreCase("Tax")) {
					isTaxFlag = true;
				}
				scAttachments
						.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, serviceEntity));
			}

			if (!isTaxFlag &&serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, TAX_ATTACHMENT_CATEGORY, TAX_ATTACHMENT_CATEGORY));
			}
			if (serviceDetail.getTaxExemptionFlag()!=null && serviceDetail.getTaxExemptionFlag().equals("Y")) {
				scAttachments.add(persistAttachment(serviceEntity, "TAXSD", "TAXSD"));
				scAttachments.add(persistAttachment(serviceEntity, "GSTCET", "GSTCET"));
			}

			/*
			 * if (serviceDetail.getOdrContractInfo() != null &&
			 * serviceDetail.getOdrContractInfo().getPoMandatoryStatus() != null &&
			 * serviceDetail.getOdrContractInfo().getPoMandatoryStatus().equals("Y"))
			 * scAttachments.add(persistAttachment(serviceEntity, PO_ATTACHMENT_CATEGORY,
			 * PO_ATTACHMENT_CATEGORY));
			 */
			LOGGER.info("Product Detail");
			if (null != serviceDetail.getOdrProductDetail()) {
				Set<ScProductDetail> scProductDetails = new HashSet<>();
				for (OdrProductDetail odrProductDetail : serviceDetail.getOdrProductDetail()) {
					ScProductDetail scProductDetail = serviceFulfillmentMapper
							.mapProductDetailEntityToBean(odrProductDetail);
					if (null != odrProductDetail.getOdrProductAttributes()) {
						Set<ScProductDetailAttributes> scProductDetailAttributes = serviceFulfillmentMapper
								.mapProductDetailAttr(odrProductDetail.getOdrProductAttributes(), scProductDetail);
						scProductDetail.setScProductDetailAttributes(scProductDetailAttributes);
					}
					if (null != odrProductDetail.getOdrServiceCommercials()) {
						Set<ScServiceCommericalComponent> scServiceCommericalComponents = serviceFulfillmentMapper
								.mapServiceCommercialComponents(odrProductDetail.getOdrServiceCommercials(),
										scProductDetail);
						scProductDetail.setScServiceCommercialComponent(scServiceCommericalComponents);
					}
					scProductDetail.setScServiceDetail(serviceEntity);
					scProductDetails.add(scProductDetail);
				}
				serviceEntity.setScProductDetail(scProductDetails);
			}
			serviceMapper.put(serviceEntity.getErfOdrServiceId(), serviceEntity.getUuid());
			serviceEntity.setScAttachments(scAttachments);
			serviceEntity.setScOrder(scOrderEntity);
			Set<OdrComponentBean> components=serviceDetail.getOdrComponentBeans();
			if (components != null) {
				for (OdrComponentBean scComponentBean : components) {
					ScComponent scComponent=null;
					if (scComponent == null) {
						scComponent = new ScComponent();

					}
					scComponent.setComponentName(scComponentBean.getComponentName());
					scComponent.setCreatedBy(scComponentBean.getCreatedBy());
					scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
					scComponent.setIsActive(CommonConstants.Y);
					scComponent.setSiteType(scComponentBean.getSiteType());
					scComponent.setUuid(Utils.generateUid());
					for (OdrComponentAttributeBean odrComponentAttributeBean : scComponentBean
							.getOdrComponentAttributeBeans()) {
						ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
						scComponentAttribute.setAttributeAltValueLabel(odrComponentAttributeBean.getValue());
						scComponentAttribute.setAttributeName(odrComponentAttributeBean.getName());
						scComponentAttribute.setAttributeValue(odrComponentAttributeBean.getValue());
						scComponentAttribute.setCreatedBy(odrComponentAttributeBean.getCreatedBy());
						scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scComponentAttribute.setIsActive(CommonConstants.Y);
						scComponentAttribute.setScComponent(scComponent);
						scComponentAttribute.setUuid(Utils.generateUid());
						
						LOGGER.info("Component Attribute Name{} ",scComponentAttribute.getAttributeName() );
						if (scComponentAttribute.getAttributeName().equals("siteGstAddress")
								&& scComponentAttribute.getAttributeValue() != null && CommonConstants.MMR_CROSS_CONNECT
										.equalsIgnoreCase(serviceEntity.getErfPrdCatalogOfferingName())) {
							try {
								GstAddressBean gstAddress = Utils.convertJsonToObject(
										scComponentAttribute.getAttributeValue(), GstAddressBean.class);
								if (gstAddress != null) {
									ScGstAddress scGstAddress = new ScGstAddress();
									scGstAddress.setBuildingName(gstAddress.getBuildingName());
									scGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
									scGstAddress.setDistrict(gstAddress.getDistrict());
									scGstAddress.setFlatNumber(gstAddress.getFlatNumber());
									scGstAddress.setLatitude(gstAddress.getLatitude());
									scGstAddress.setLocality(gstAddress.getLocality());
									scGstAddress.setLongitude(gstAddress.getLongitude());
									scGstAddress.setPincode(gstAddress.getPinCode());
									scGstAddress.setState(gstAddress.getState());
									scGstAddress.setStreet(gstAddress.getStreet());
									LOGGER.info("save scGstAddress {} ",serviceDetail.getUuid());
									scGstAddress = scGstAddressRepository.save(scGstAddress);
									
									scComponentAttribute.setAttributeName("siteGstAddressId");
									scComponentAttribute.setAttributeAltValueLabel(scGstAddress.getId().toString());
									scComponentAttribute.setAttributeValue(scGstAddress.getId().toString());
									LOGGER.info("Site Gst Address id is {}", scComponentAttribute.getAttributeValue());
									
								}
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
						scComponent.getScComponentAttributes().add(scComponentAttribute);
					}
					if (compMapper.get(serviceEntity.getErfOdrServiceId()) != null) {
						LOGGER.info("update compMapper {} ",serviceEntity.getErfOdrServiceId());

						compMapper.get(serviceEntity.getErfOdrServiceId()).add(scComponent);
					}else {
						LOGGER.info("save compMapper {} ",serviceEntity.getErfOdrServiceId());

						List<ScComponent> scComp=new ArrayList<>();
						scComp.add(scComponent);
						compMapper.put(serviceEntity.getErfOdrServiceId(), scComp);
					}
				}
			}
			if (odrOrderBean.getStage() != null && odrOrderBean.getStage().equals("ORDER_COMPLETED")) {
				serviceEntity.setIsOeCompleted(CommonConstants.Y);
			} else {
				serviceEntity.setIsOeCompleted(CommonConstants.N);
			}
			scServiceDetails.add(serviceEntity);
			

		});
		LOGGER.info("save scContractingInfo started");

		scContractInfoRepository.saveAll(scContractingInfo);
		LOGGER.info("save scContractingInfo ended");

		scOrderAttributeRepository.saveAll(scOrderAttrs);
		LOGGER.info("save scOrderAttrs ended");

		scServiceDetailRepository.saveAll(scServiceDetails);
		LOGGER.info("save scServiceDetails ended");

		for (OdrCommercialBean odrCommercial : odrOrderBean.getOdrCommercialBean()) {
			//String scServiceId = serviceMapper.get(odrCommercial.getServiceId());
			ScServiceCommercial scCommercial=serviceFulfillmentMapper.mapOdrCommercialToServiceCommercial(odrCommercial);
			if(scCommercialMapper.get(odrCommercial.getServiceId())==null) {
				List<ScServiceCommercial> scServiceCommercials=new ArrayList<>();
				scServiceCommercials.add(scCommercial);
				scCommercialMapper.put(odrCommercial.getServiceId(), scServiceCommercials);
			}else {
				scCommercialMapper.get(odrCommercial.getServiceId()).add(scCommercial);
			}
		}	
		
		Map<Integer, String> serviceIdMapper = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			LOGGER.info("save createServiceStaus");
			createServiceStaus(scServiceDetail, TaskStatusConstants.INPROGRESS);
			serviceIdMapper.put(scServiceDetail.getErfOdrServiceId(), scServiceDetail.getUuid());
		}
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			if (compMapper.get(scServiceDetail.getErfOdrServiceId()) != null) {
				List<ScComponent> scCompo = compMapper.get(scServiceDetail.getErfOdrServiceId());
				LOGGER.info("scCompo size:{}",scCompo.size());

				for (ScComponent scCompone : scCompo) {
					LOGGER.info("scCompo type:{} and:service id {} ",scCompone.getSiteType(),scServiceDetail.getId());

					scCompone.setScServiceDetailId(scServiceDetail.getId());
					for (ScComponentAttribute scCmpAttr : scCompone.getScComponentAttributes()) {
						scCmpAttr.setScServiceDetailId(scServiceDetail.getId());
					}
					LOGGER.info("save comp",scCompone.getSiteType(),scServiceDetail.getId());

					scComponentRepository.save(scCompone);
					LOGGER.info("save comp end",scCompone.getSiteType(),scServiceDetail.getId());

				}
			}
			LOGGER.info("save scCommercialMapper sarted");

			if(scCommercialMapper.get(scServiceDetail.getErfOdrServiceId())!=null) {
				List<ScServiceCommercial> scCommercials=scCommercialMapper.get(scServiceDetail.getErfOdrServiceId());
				for (ScServiceCommercial scCommercial : scCommercials) {
					scCommercial.setScServiceId(scServiceDetail.getId());
					scCommercial.setServiceId(scServiceDetail.getUuid());
					scServiceCommercialRepository.save(scCommercial);
					LOGGER.info("save scCommercialMapper ended");

				}
				
			}
			if (scServiceDetail.getErdPriSecServiceLinkId() != null) {
				scServiceDetail.setPriSecServiceLink(serviceIdMapper.get(scServiceDetail.getErdPriSecServiceLinkId()));
				LOGGER.info("save scServiceDetail started");

				scServiceDetailRepository.save(scServiceDetail);
				LOGGER.info("save scServiceDetail ended");

			}	
		}
		if(!scServiceDetails.isEmpty()) {
			LOGGER.info("Renewals scServiceDetails exists::{}",scServiceDetails.size());
			ScServiceDetail scServiceDetail=scServiceDetails.stream().findFirst().orElse(null);
			renewalScServiceDetail.setUuid(scOrderEntity.getUuid());
			renewalScServiceDetail.setScOrder(scOrderEntity);
			renewalScServiceDetail.setOrderType("RENEWALS");
			renewalScServiceDetail.setScOrderUuid(scOrderEntity.getUuid());
			renewalScServiceDetail.setIsActive("Y");
			renewalScServiceDetail.setCreatedDate(new Timestamp(new Date().getTime()));
			if(scServiceDetail!=null) {
				renewalScServiceDetail.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
				renewalScServiceDetail.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
			}
			renewalScServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			Set<ScAttachment> scAttachments = new HashSet<ScAttachment>();
			Optional<OdrServiceDetailBean> odrServiceDetailBeanOptional =odrOrderBean.getOdrServiceDetails().stream().findFirst();
			if(odrServiceDetailBeanOptional.isPresent()) {
				LOGGER.info("odrServiceDetailBeanOptional exists");
				OdrServiceDetailBean odrServiceDetailBean=odrServiceDetailBeanOptional.get();
				Optional<OdrAttachmentBean> odrAttachmentBeanOptional =odrServiceDetailBean.getOdrAttachments().stream().filter(oa -> "RENEWALS".equalsIgnoreCase(oa.getType())).findFirst();
				if(odrAttachmentBeanOptional.isPresent()) {
					LOGGER.info("odrAttachmentBeanOptional exists");
					OdrAttachmentBean odrAttachmentBean=odrAttachmentBeanOptional.get();
					scAttachments.add(serviceFulfillmentMapper.mapServiceAttachmentToEntity(odrAttachmentBean, renewalScServiceDetail));
					renewalScServiceDetail.setScAttachments(scAttachments);
				}
			}
			scServiceDetailRepository.save(renewalScServiceDetail);
		}
		return renewalScServiceDetail;

	}
	
	public void triggerRenewalWorkflow(ScServiceDetail renewalScServiceDetail) {
		LOGGER.info("triggerRenewalWorkflow");
		if(renewalScServiceDetail!=null){
			LOGGER.info("work flow triggered for the renewal service id:{},order code:{},order:{}",renewalScServiceDetail.getId(),renewalScServiceDetail.getScOrderUuid(),renewalScServiceDetail.getScOrder().getOpOrderCode());
			processL2OService.processRenewalL2ODataToFlowable(renewalScServiceDetail);
		}
	}

	/**
	 * Method to trigger teamsDR Managed Services
	 * @param planItemRequestBean
	 */
	public void triggerTeamsDRManagedServices(TeamsDRPlanItemRequestBean planItemRequestBean)
			throws TclCommonException {
		try {
			LOGGER.info("Inside triggerTeamsDRManagedServices - Waiting for 30 seconds");
			TimeUnit.SECONDS.sleep(30);
			Boolean status = false;
			status = processL2OService.processManagedServices(planItemRequestBean.getServiceId(), null,
					planItemRequestBean.getPlanItemDefinitionId(), planItemRequestBean.getPlanItem(),
					planItemRequestBean.getComponentId(), planItemRequestBean.getFlowGroupId());
			if (!status) {
				ServiceFulfillmentJob serviceFulfillmentJob = new ServiceFulfillmentJob();
				serviceFulfillmentJob.setCreatedBy("system");
				serviceFulfillmentJob.setCreatedTime(new Date());
				serviceFulfillmentJob.setServiceId(planItemRequestBean.getServiceId());
				serviceFulfillmentJob.setType("NEW_SERVICE");
				//srviceFulfillmentJob.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
				// srviceFulfillmentJob.setServiceUuid(scServiceDetail.getUuid());
				serviceFulfillmentJob.setRetryCount(0);
				serviceFulfillmentJob.setStatus("NEW");
				serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
			}
		} catch (Exception e) {
			LOGGER.info("Error in triggerTeamsDRManagedServices {}, {}", planItemRequestBean.getPlanItemDefinitionId(),
					e.getMessage());
		}
	}


}
